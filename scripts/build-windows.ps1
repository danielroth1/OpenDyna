param(
	# Desired output executable name (without extension for non-Graal path)
	[string] $Name = "opendyna",
	# Prefer GraalVM native-image if available (smallest single .exe)
	[switch] $UseGraal,
	# Skip UPX compression
	[switch] $NoUPX
)

# Add: detect simple invocation mistakes where "and" (or "-and") was supplied
# This only catches unbound args (script must start to run); it helps users who typed "and" between commands.
if ($args -and ($args -contains 'and' -or $args -contains 'And' -or $args -contains '-and')) {
	Write-Host "ERROR: The token 'and' was passed to the script. In PowerShell use ';' to separate commands or pass recognized switches."
	Write-Host "Example (run in PowerShell):"
	Write-Host "  .\scripts\build-windows.ps1 -UseGraal -NoUPX"
	Write-Host "Or chain commands with a semicolon:"
	Write-Host "  .\scripts\build-windows.ps1 ; Write-Host 'next step'"
	exit 1
}

$ErrorActionPreference = "Stop"

# Resolve repo root and dist
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$Root = Resolve-Path (Join-Path $ScriptDir "..")
$Dist = Join-Path $Root "dist"
New-Item -ItemType Directory -Force -Path $Dist | Out-Null

# Debug banner: helps confirm which file is being executed and locate older copies
Write-Host "BUILD-WINDOWS.PS1: java-only script (v2) -- executing: $($MyInvocation.MyCommand.Path)"

# --- Add: environment/tool diagnostics (helpful when script claims no build tool found)
Write-Host "`n--- Environment diagnostics ---"
Write-Host "PSVersion = $($PSVersionTable.PSVersion)"
Write-Host "ExecutionPolicy = $(Get-ExecutionPolicy -Scope Process -ErrorAction SilentlyContinue) / $(Get-ExecutionPolicy -Scope CurrentUser -ErrorAction SilentlyContinue) / $(Get-ExecutionPolicy -Scope LocalMachine -ErrorAction SilentlyContinue)"
Write-Host "JAVA_HOME = $env:JAVA_HOME"
try { & java -version 2>&1 | ForEach-Object { Write-Host "java: $_" } } catch { Write-Host "java: not found" }
Write-Host "mvnw.cmd present = $(Test-Path (Join-Path $Root 'mvnw.cmd'))"
Write-Host "mvn on PATH = $((Get-Command mvn -ErrorAction SilentlyContinue) -ne $null)"
Write-Host "gradlew.bat present = $(Test-Path (Join-Path $Root 'gradlew.bat'))"
Write-Host "gradle on PATH = $((Get-Command gradle -ErrorAction SilentlyContinue) -ne $null)"
Write-Host "native-image on PATH = $((Get-Command native-image -ErrorAction SilentlyContinue) -ne $null)"
Write-Host "jpackage on PATH = $((Get-Command jpackage -ErrorAction SilentlyContinue) -ne $null)"
Write-Host "jlink on PATH = $((Get-Command jlink -ErrorAction SilentlyContinue) -ne $null)"
Write-Host "jdeps on PATH = $((Get-Command jdeps -ErrorAction SilentlyContinue) -ne $null)"
Write-Host "--- end diagnostics ---`n"

# --- Replace quick search so the script doesn't match itself and to avoid weird Select-String parsing problems ---
$legacyPattern = "No supported project files detected"
try {
	$ps1Files = Get-ChildItem -Path $Root -Recurse -Filter *.ps1 -ErrorAction SilentlyContinue |
	Where-Object { $_.FullName.ToLower() -ne ($MyInvocation.MyCommand.Path.ToLower()) }

	Write-Host "Checking $($ps1Files.Count) other PS1 files for legacy message..."
	foreach ($f in $ps1Files) {
		try {
			# run Select-String per-file so any parsing / argument issues are isolated
			$res = Select-String -Path $f.FullName -Pattern $legacyPattern -SimpleMatch -ErrorAction SilentlyContinue
			if ($res) {
				Write-Host "Warning: found PS1 file containing the legacy message (`"$legacyPattern`'):"
				Write-Host " - $($f.FullName)"
				# show matched lines to help locate the code to update
				$res | ForEach-Object { Write-Host ("   Line {0}: {1}" -f $_.LineNumber, $_.Line.Trim()) }
			}
		}
		catch {
			Write-Host "Skipping file due to read/parse error: $($f.FullName) -- $($_.Exception.Message)"
		}
	}
}
catch {
	Write-Host "Diagnostic search failed: $($_.Exception.Message)"
	# continue running the script rather than aborting here
}

function Run-Command {
	param([string]$cmd, [string]$args)
	$global:LAST_CMD = "$cmd $args"
	Write-Host "`n> $cmd $args"
	try {
		$psi = New-Object System.Diagnostics.ProcessStartInfo
		$psi.FileName = $cmd
		$psi.Arguments = $args
		$psi.RedirectStandardOutput = $true
		$psi.RedirectStandardError = $true
		$psi.UseShellExecute = $false
		$proc = [System.Diagnostics.Process]::Start($psi)
		$stdout = $proc.StandardOutput.ReadToEnd()
		$stderr = $proc.StandardError.ReadToEnd()
		$proc.WaitForExit()
		if ($stdout) { $stdout.TrimEnd() | ForEach-Object { Write-Host $_ } }
		if ($stderr) { $stderr.TrimEnd() | ForEach-Object { Write-Host $_ } }
		if ($proc.ExitCode -ne 0) { throw "Command failed: $cmd $args (exit $($proc.ExitCode))" }
	}
 catch {
		# rethrow with context
		throw $_
	}
}

function Find-Tool {
	param([string]$name)
	return (Get-Command $name -ErrorAction SilentlyContinue)
}

function Try-UPX {
	param([string]$file)
	if ($NoUPX) { Write-Host "Skipping UPX (NoUPX)"; return }
	$upx = Find-Tool "upx"
	if ($upx) {
		Write-Host "Compressing with upx (best, ultra-brute)"
		Run-Command "upx" "--best --ultra-brute `"$file`""
	}
 else {
		Write-Host "upx not found, skipping compression."
	}
}

function Build-JavaProject {
	# Detect Maven or Gradle and build a jar (favor shaded/fat jars)
	$useMvnw = Test-Path (Join-Path $Root "mvnw.cmd")
	$useMvn = $useMvnw -or (Find-Tool "mvn")
	$useGradlew = Test-Path (Join-Path $Root "gradlew.bat")
	$useGradle = $useGradlew -or (Find-Tool "gradle")

	# Fix: ensure Test-Path is evaluated before
	if ((Test-Path (Join-Path $Root "pom.xml")) -and $useMvn) {
		$mvnc = if ($useMvnw) { (Join-Path $Root "mvnw.cmd") } else { "mvn" }
		Write-Host "Building with Maven..."
		Run-Command $mvnc "-q -DskipTests package"
		$target = Join-Path $Root "target"
		if (-not (Test-Path $target)) { throw "Maven target directory not found." }
		# Prefer shaded/with-dependencies/all jars
		$candidates = @(
			"$target\*with-dependencies*.jar",
			"$target\*shaded*.jar",
			"$target\*all*.jar",
			"$target\*.jar"
		)
	}
 elseif (( (Test-Path (Join-Path $Root "build.gradle")) -or (Test-Path (Join-Path $Root "build.gradle.kts")) )) {
		if (-not $useGradle) { throw "Gradle/gradlew not found." }
		$gradc = if ($useGradlew) { (Join-Path $Root "gradlew.bat") } else { "gradle" }
		Write-Host "Building with Gradle..."
		# Try shadowJar if present; otherwise fallback to jar
		try {
			Run-Command $gradc "shadowJar -q -x test -x check"
		}
		catch {
			Run-Command $gradc "jar -q -x test -x check"
		}
		$target = Join-Path $Root "build\libs"
		if (-not (Test-Path $target)) { throw "Gradle libs directory not found." }
		$candidates = @(
			"$target\*shadow*.jar",
			"$target\*all*.jar",
			"$target\*.jar"
		)
	}
 else {
		# --- Add: expanded diagnostic before throwing so you can see why detection failed ---
		Write-Host "`n--- Build tool detection failure details ---"
		Write-Host "Files in repo root:"
		Get-ChildItem -Path $Root -File | ForEach-Object { Write-Host " - $($_.Name)" }
		Write-Host "pom.xml present: $(Test-Path (Join-Path $Root 'pom.xml'))"
		Write-Host "build.gradle present: $(Test-Path (Join-Path $Root 'build.gradle'))"
		Write-Host "build.gradle.kts present: $(Test-Path (Join-Path $Root 'build.gradle.kts'))"
		Write-Host "mvnw.cmd present: $useMvnw"
		Write-Host "mvn on PATH: $((Get-Command mvn -ErrorAction SilentlyContinue) -ne $null)"
		Write-Host "gradlew.bat present: $useGradlew"
		Write-Host "gradle on PATH: $((Get-Command gradle -ErrorAction SilentlyContinue) -ne $null)"
		Write-Host "--- end detection details ---`n"
		throw "No recognized Java build tool found (pom.xml or build.gradle)."
	}

	$jar = $null
	foreach ($pat in $candidates) {
		$found = Get-ChildItem -Path $pat -File -ErrorAction SilentlyContinue | Sort-Object Length -Descending | Select-Object -First 1
		if ($found) { $jar = $found.FullName; break }
	}
	if (-not $jar) { throw "Built JAR not found." }
	Write-Host "Using JAR: $jar"
	return $jar
}

function Get-MainClassFromJar {
	param([string]$jarPath)
	Add-Type -AssemblyName System.IO.Compression.FileSystem
	$zip = [System.IO.Compression.ZipFile]::OpenRead($jarPath)
	try {
		$entry = $zip.Entries | Where-Object { $_.FullName -ieq "META-INF/MANIFEST.MF" } | Select-Object -First 1
		if (-not $entry) { return $null }
		$sr = New-Object System.IO.StreamReader($entry.Open())
		try {
			while (-not $sr.EndOfStream) {
				$line = $sr.ReadLine()
				if ($line -match '^\s*Main-Class:\s*(.+)\s*$') {
					return $matches[1].Trim()
				}
			}
		}
		finally { $sr.Dispose() }
	}
 finally { $zip.Dispose() }
	return $null
}

function Get-JModsPath {
	# Prefer JAVA_HOME if set
	if ($env:JAVA_HOME) {
		$jmods = Join-Path $env:JAVA_HOME "jmods"
		if (Test-Path $jmods) { return $jmods }
	}
	# Fallback: parse java.home
	$temp = New-TemporaryFile
	try {
		# java -XshowSettings:properties prints to stderr
		$psi = New-Object System.Diagnostics.ProcessStartInfo
		$psi.FileName = "java"
		$psi.Arguments = "-XshowSettings:properties -version"
		$psi.RedirectStandardError = $true
		$psi.UseShellExecute = $false
		$proc = [System.Diagnostics.Process]::Start($psi)
		$err = $proc.StandardError.ReadToEnd()
		$proc.WaitForExit()
		$match = [regex]::Match($err, "java\.home = (.+)")
		if ($match.Success) {
			$jhome = $match.Groups[1].Value.Trim()
			$jmods = Join-Path $jhome "jmods"
			if (Test-Path $jmods) { return $jmods }
		}
	}
 catch { }
	finally { Remove-Item -Force $temp -ErrorAction SilentlyContinue }
	throw "Could not locate jmods; ensure a full JDK (not JRE) is installed and JAVA_HOME is set."
}

function Build-With-Graal {
	param([string]$jarPath)
	$native = Find-Tool "native-image"
	if (-not $native) { throw "GraalVM native-image not found on PATH." }
	$outExe = Join-Path $Dist ($Name + ".exe")
	Write-Host "Building native executable with GraalVM..."
	# Conservative flags for small size; --no-fallback to avoid embedded JVM
	$niArgs = @(
		"-jar `"$jarPath`"",
		"-H:Name=$Name",
		"-H:Path=`"$Dist`"",
		"-H:StripDebug=true",
		"--no-fallback",
		"--initialize-at-build-time"
	) -join " "
	Run-Command "native-image" $niArgs
	if (-not (Test-Path $outExe)) { throw "native-image did not produce $outExe" }
	Try-UPX $outExe
	return $outExe
}

function Build-With-JlinkJpackage {
	param([string]$jarPath)
	$mainClass = Get-MainClassFromJar $jarPath
	if (-not $mainClass) {
		Write-Host "Warning: Main-Class not found in manifest; jpackage will require --main-class. Using detected value: $mainClass"
	}

	$jdeps = Find-Tool "jdeps"
	if (-not $jdeps) { throw "jdeps not found. Please install JDK (with jdeps/jlink/jpackage) and set JAVA_HOME." }

	Write-Host "Analyzing modules with jdeps..."
	$modDeps = & jdeps --ignore-missing-deps --print-module-deps --recursive "$jarPath" 2>$null
	if ([string]::IsNullOrWhiteSpace($modDeps)) { $modDeps = "java.base" }

	$jmods = Get-JModsPath
	$runtimeDir = Join-Path $Dist "runtime"
	if (Test-Path $runtimeDir) { Remove-Item -Recurse -Force $runtimeDir }
	Write-Host "Creating minimized runtime with jlink..."
	$jlinkArgs = @(
		"--module-path `"$jmods`"",
		"--add-modules $modDeps",
		"--strip-debug",
		"--no-header-files",
		"--no-man-pages",
		"--compress=2",
		"--output `"$runtimeDir`""
	) -join " "
	Run-Command "jlink" $jlinkArgs

	# Package app-image (directory) with launcher .exe
	$appName = $Name
	$appImageDest = $Dist
	$appImageDir = Join-Path $appImageDest $appName
	if (Test-Path $appImageDir) { Remove-Item -Recurse -Force $appImageDir }

	# jpackage wants the jar under an input folder
	$inputDir = Join-Path $Dist "input"
	if (Test-Path $inputDir) { Remove-Item -Recurse -Force $inputDir }
	New-Item -ItemType Directory -Path $inputDir | Out-Null
	Copy-Item -Force "$jarPath" "$inputDir/"

	Write-Host "Packaging app-image with jpackage..."
	$jarName = [System.IO.Path]::GetFileName($jarPath)
	$jpkgArgs = @(
		"--type app-image",
		"--name `"$appName`"",
		"--input `"$inputDir`"",
		"--main-jar `"$jarName`""
	)
	if ($mainClass) { $jpkgArgs += "--main-class `"$mainClass`"" }
	$jpkgArgs += @(
		"--runtime-image `"$runtimeDir`"",
		"--dest `"$appImageDest`"",
		"--win-console"
	)
	Run-Command "jpackage" ($jpkgArgs -join " ")

	$launcher = Join-Path $appImageDir ($appName + ".exe")
	if (-not (Test-Path $launcher)) { throw "jpackage did not produce launcher: $launcher" }

	# Optionally compress the launcher only (does not compress runtime image)
	Try-UPX $launcher
	return $launcher
}

Write-Host "Repo root: $Root"
try {
	$jar = Build-JavaProject

	# Choose build path: prefer GraalVM if available or if explicitly requested
	$builtPath = $null
	if ($UseGraal) {
		try {
			$builtPath = Build-With-Graal $jar
		}
		catch {
			Write-Host "GraalVM build failed or unavailable: $($_.Exception.Message)"
			Write-Host "Falling back to jlink + jpackage."
		}
	}
	if (-not $builtPath) {
		$builtPath = Build-With-JlinkJpackage $jar
	}

	# Final size
	if (Test-Path $builtPath) {
		$size = (Get-Item $builtPath).Length
		Write-Host "`nOutput: $builtPath"
		Write-Host ("Size: {0:N0} bytes" -f $size)
	}
 else {
		throw "Build produced no output."
	}
}
catch {
	Write-Host "`nERROR: Build failed."
	Write-Host "Last command: $global:LAST_CMD"
	Write-Host "Exception Message: $($_.Exception.Message)"
	if ($_.Exception.InnerException) { Write-Host "Inner Exception: $($_.Exception.InnerException.Message)" }
	Write-Host "Script StackTrace:`n$($_.ScriptStackTrace)"
	Write-Host "Exception StackTrace:`n$($_.Exception.StackTrace)"
	# Also dump the full error record for completeness
	Write-Host "`nFull error record:"
	$_ | Format-List * -Force
	exit 1
}
