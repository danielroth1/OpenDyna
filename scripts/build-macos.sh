#!/usr/bin/env bash
set -euo pipefail

# Temporary globals for cleanup
STAGING_DIR=""
TEMP_DMG=""

cleanup() {
  # keep going even if rm fails
  set +e
  if [[ -n "${STAGING_DIR:-}" && -d "$STAGING_DIR" ]]; then
    rm -rf "$STAGING_DIR"
  fi
  if [[ -n "${TEMP_DMG:-}" && -f "$TEMP_DMG" ]]; then
    rm -f "$TEMP_DMG"
  fi
}
trap cleanup EXIT

# Build and package the CardGame Java project for macOS using jpackage
# Produces: .app, .pkg, .dmg in the dist/ directory

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DIST_DIR="$ROOT_DIR/dist"
BUILD_DIR="$ROOT_DIR/build"
JAR_NAME="OpenDyna.jar"
MAIN_CLASS="main.Main"
APP_NAME="OpenDyna"
ICON="$ROOT_DIR/resources/icon.icns"
PKG_IDENTIFIER="com.example.opendyna"
APP_VERSION="1.0"
VENDOR="OpenDyna"

usage() {
  echo "Usage: $0 [--jvm-arg \"-Xmx512m\"] [--java-home /path/to/jdk]"
  echo "Requires jpackage (JDK 14+ or newer builds that include jpackage)."
  exit 1
}

if [[ ${1:-""} == "-h" || ${1:-""} == "--help" ]]; then
  usage
fi

# Parse simple args
JAVA_HOME_ARG=""
declare -a JPACKAGE_OPTIONS=()
while [[ $# -gt 0 ]]; do
  case "$1" in
    --java-home)
      shift
      JAVA_HOME_ARG="$1"
      ;;
    --jvm-arg)
      shift
      JPACKAGE_OPTIONS+=(--java-options "$1")
      ;;
    --icon)
      shift
      ICON="$1"
      ;;
    *)
      echo "Unknown arg: $1"
      usage
      ;;
  esac
  shift
done

# Ensure a clean dist directory
if [[ -d "$DIST_DIR" ]]; then
  echo "Cleaning existing dist directory contents: $DIST_DIR"
  # Remove all files and folders inside DIST_DIR but keep the directory itself.
  # This removes hidden files as well. Use a subshell to avoid errors when empty.
  shopt -s dotglob nullglob
  for path in "$DIST_DIR"/*; do
    rm -rf "$path"
  done
  shopt -u dotglob nullglob
else
  mkdir -p "$DIST_DIR"
fi
mkdir -p "$BUILD_DIR"

echo "Compiling Java sources (excluding test sources)..."
# Find java sources but exclude test and autotest directories (which may contain JUnit tests)
# This prevents JUnit-related test files from being compiled or pulled into the jar.
## Only list Java source files. Prune test/autotest directories so they are not included.
## The order here prevents directories from being printed and ensures javac gets only files.
find "$ROOT_DIR/src" \
  \( -path "*/autotest/*" -o -path "*/tests/*" \) -prune -o -type f -name "*.java" -print > "$BUILD_DIR/sources.txt"
mkdir -p "$BUILD_DIR/classes"
javac -d "$BUILD_DIR/classes" -sourcepath "$ROOT_DIR/src" @"$BUILD_DIR/sources.txt"

# Copy non-java resources (images, properties, etc.) from src into classes so they are included in the jar
echo "Copying non-Java resources into classes..."
# Use a portable find+cp to preserve paths and skip .java files
(
  cd "$ROOT_DIR/src"
  find . -type f ! -name "*.java" -print0 | while IFS= read -r -d '' file; do
    dest="$BUILD_DIR/classes/$file"
    mkdir -p "$(dirname "$dest")"
    cp -p "$file" "$dest"
  done
)

# Verify main class exists in compiled classes
MAIN_CLASS_PATH="${MAIN_CLASS//./\/}.class"
if ! find "$BUILD_DIR/classes" -path "*/$MAIN_CLASS_PATH" -print -quit >/dev/null; then
  echo "Warning: main class $MAIN_CLASS not found in compiled classes. Make sure the package/path is correct."
fi

echo "Creating jar $JAR_NAME..."
mkdir -p "$BUILD_DIR/jar"
jar --create --file="$BUILD_DIR/jar/$JAR_NAME" -C "$BUILD_DIR/classes" .

# Determine jpackage executable
if [[ -n "$JAVA_HOME_ARG" ]]; then
  JPACKAGE_BIN="$JAVA_HOME_ARG/bin/jpackage"
else
  JPACKAGE_BIN="$(command -v jpackage || true)"
fi

if [[ ! -x "$JPACKAGE_BIN" ]]; then
  echo "jpackage not found or not executable. Please install a JDK with jpackage and/or pass --java-home."
  exit 2
fi

echo "Packaging with jpackage (app)..."
"$JPACKAGE_BIN" \
  --type app-image \
  --input "$BUILD_DIR/jar" \
  --dest "$DIST_DIR" \
  --name "$APP_NAME" \
  --main-jar "$JAR_NAME" \
  --main-class "$MAIN_CLASS" \
  --app-version "$APP_VERSION" \
  ${ICON:+--icon "$ICON"} \
  ${JPACKAGE_OPTIONS[@]:-}

echo "Packaging installer packages (.pkg and .dmg)..."

"$JPACKAGE_BIN" \
  --type pkg \
  --input "$BUILD_DIR/jar" \
  --dest "$DIST_DIR" \
  --name "$APP_NAME" \
  --main-jar "$JAR_NAME" \
  --main-class "$MAIN_CLASS" \
  --app-version "$APP_VERSION" \
  --vendor "$VENDOR" \
  ${ICON:+--icon "$ICON"} \
  ${JPACKAGE_OPTIONS[@]:-}

# Create dmg from the app image output with an Applications link for drag-and-drop
APP_IMAGE_DIR="$DIST_DIR/${APP_NAME}.app"
if [[ -d "$APP_IMAGE_DIR" ]]; then
  STAGING_DIR="$BUILD_DIR/dmg-staging"
  rm -rf "$STAGING_DIR"
  mkdir -p "$STAGING_DIR"

  # Copy the app into staging
  cp -R "$APP_IMAGE_DIR" "$STAGING_DIR/"

  # Create Applications symlink in staging for drag-and-drop
  ln -s /Applications "$STAGING_DIR/Applications"

  DMG_NAME="${APP_NAME}.dmg"
  DMG_PATH="$DIST_DIR/$DMG_NAME"
  echo "Creating drag-and-drop dmg at $DMG_PATH..."

  # Create compressed read-only dmg directly from staging folder
  if [[ -f "$DMG_PATH" ]]; then
    rm -f "$DMG_PATH"
  fi
  hdiutil create -srcfolder "$STAGING_DIR" -volname "$APP_NAME" -ov -format UDZO "$DMG_PATH"
else
  echo "App image not found at $APP_IMAGE_DIR; skipping dmg creation"
fi

# Also create a zip of the .app for distribution/backups
if [[ -d "$APP_IMAGE_DIR" ]]; then
  ZIP_NAME="${APP_NAME}.app.zip"
  ZIP_PATH="$DIST_DIR/$ZIP_NAME"
  echo "Creating zip of app at $ZIP_PATH..."

  # Use ditto to create an archive that preserves macOS resource forks and metadata
  if command -v ditto >/dev/null 2>&1; then
    # --sequesterRsrc and --keepParent help preserve Finder info and keep the .app as a single entry
    ditto -c -k --sequesterRsrc --keepParent "$APP_IMAGE_DIR" "$ZIP_PATH" || {
      echo "ditto failed, falling back to zip"
      (cd "$DIST_DIR" && zip -r "$ZIP_NAME" "${APP_NAME}.app")
    }
  else
    (cd "$DIST_DIR" && zip -r "$ZIP_NAME" "${APP_NAME}.app")
  fi
fi

echo "Done. Artifacts in: $DIST_DIR"
