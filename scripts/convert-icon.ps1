# Convert macOS .icns or PNG iconset to Windows .ico
# Usage: .\convert-icon.ps1
# - Tries to convert resources/icon.iconset/icon_16x16.png and resources/icon.icns
# - Requires ImageMagick (magick) or the 'convert' binary available on PATH
# - Output: resources/icon.ico

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition

# Prefer $PSScriptRoot when available (more robust when called from different hosts)
if ($PSScriptRoot) { $scriptDir = $PSScriptRoot }

# Build resources path and resolve to a simple string path if possible
$resourcesPath = Join-Path $scriptDir "..\resources"
try {
    $resourcesDir = (Resolve-Path $resourcesPath -ErrorAction Stop).Path
}
catch {
    # Fallback to the constructed path if Resolve-Path fails
    $resourcesDir = $resourcesPath
}

$iconIco = Join-Path $resourcesDir "icon.ico"
$iconIcns = Join-Path $resourcesDir "icon.icns"
$iconSet16 = Join-Path $resourcesDir "icon.iconset\icon_16x16.png"

Write-Host "Script directory: $scriptDir"
Write-Host "Resources directory: $resourcesDir"
Write-Host "Looking for: $iconIcns"
Write-Host "Looking for: $iconSet16"

Write-Host "Converting icon to resources/icon.ico..."

# Prefer using magick (ImageMagick 7+)
function Find-ImageMagick {
    # Check common install locations for magick.exe or ImageMagick's convert.exe
    $candidates = @()
    $pf = ${env:ProgramFiles}
    $pf86 = ${env:"ProgramFiles(x86)"}
    if ($pf) {
        $candidates += Get-ChildItem -Path (Join-Path $pf 'ImageMagick*') -Directory -ErrorAction SilentlyContinue | ForEach-Object { Join-Path $_.FullName 'magick.exe' }
        $candidates += Get-ChildItem -Path (Join-Path $pf 'ImageMagick*') -Directory -ErrorAction SilentlyContinue | ForEach-Object { Join-Path $_.FullName 'convert.exe' }
    }
    if ($pf86) {
        $candidates += Get-ChildItem -Path (Join-Path $pf86 'ImageMagick*') -Directory -ErrorAction SilentlyContinue | ForEach-Object { Join-Path $_.FullName 'magick.exe' }
        $candidates += Get-ChildItem -Path (Join-Path $pf86 'ImageMagick*') -Directory -ErrorAction SilentlyContinue | ForEach-Object { Join-Path $_.FullName 'convert.exe' }
    }
    # Chocolatey typical bin
    $candidates += 'C:\ProgramData\chocolatey\bin\magick.exe'
    $candidates += 'C:\ProgramData\chocolatey\bin\convert.exe'

    foreach ($c in $candidates) {
        if (Test-Path $c) { return $c }
    }
    return $null
}

$magickCmd = $null
if (Get-Command magick -ErrorAction SilentlyContinue) {
    $magickCmd = (Get-Command magick).Source
}

if (-not $magickCmd) {
    $found = Find-ImageMagick
    if ($found) {
        Write-Host "Found ImageMagick executable at: $found"
        $magickCmd = $found
    }
}

if ($magickCmd) {
    Write-Host "Using ImageMagick executable: $magickCmd"
    if (Test-Path $iconIco) { Remove-Item $iconIco -Force }

    if (Test-Path $iconIcns) {
        # magick may be the launcher; use it directly without 'convert' subcommand
        & $magickCmd $iconIcns -define icon:auto-resize=64, 48, 32, 16 $iconIco
        if ($LASTEXITCODE -eq 0) { Write-Host "Created $iconIco from $iconIcns"; exit 0 }
    }

    if (Test-Path $iconSet16) {
        & $magickCmd $iconSet16 -resize 256x256 $iconIco
        if ($LASTEXITCODE -eq 0) { Write-Host "Created $iconIco from $iconSet16"; exit 0 }
    }

    Write-Host "No source icon found in resources. Place an icon.icns or icon.iconset and re-run."
    exit 1
}

# Fallback to convert (ImageMagick 6). Note: Windows also has a builtin 'convert.exe' unrelated to ImageMagick.
$convertCmd = Get-Command convert -ErrorAction SilentlyContinue
if ($convertCmd) {
    # Check if this 'convert' is ImageMagick by running 'convert -version'
    Write-Host "Found 'convert' at: $($convertCmd.Source)"
    $verOutput = ''
    try {
        $verOutput = & convert -version 2>&1
    }
    catch {
        $verOutput = $_.Exception.Message
    }

    if ($verOutput -and $verOutput -match 'ImageMagick') {
        Write-Host "Using ImageMagick (convert) to convert."
        if (Test-Path $iconIco) { Remove-Item $iconIco -Force }

        if (Test-Path $iconIcns) {
            & convert $iconIcns -define icon:auto-resize=64, 48, 32, 16 $iconIco
            if ($LASTEXITCODE -eq 0) { Write-Host "Created $iconIco from $iconIcns"; exit 0 }
        }

        if (Test-Path $iconSet16) {
            & convert $iconSet16 -resize 256x256 $iconIco
            if ($LASTEXITCODE -eq 0) { Write-Host "Created $iconIco from $iconSet16"; exit 0 }
        }

        Write-Host "No source icon found in resources. Place an icon.icns or icon.iconset and re-run."
        exit 1
    }
    else {
        Write-Host "The 'convert' command found is not ImageMagick (output:)."
        Write-Host $verOutput
        Write-Host "On Windows the builtin 'convert.exe' is unrelated. Please install ImageMagick and ensure 'magick' is on PATH, or use the full path to ImageMagick's convert.exe."
        # fall through to final instructions
    }
}

# If ImageMagick not available, show instructions
Write-Host "ImageMagick not found. Please install ImageMagick and ensure 'magick' is on PATH."
Write-Host "Or convert manually:"
Write-Host " - On macOS: iconutil -c icns icon.iconset"
Write-Host " - On Windows with ImageMagick: magick convert resources\icon.icns -define icon:auto-resize=64,48,32,16 resources\icon.ico"
exit 2
