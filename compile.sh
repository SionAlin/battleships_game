#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

SRC_DIR="$PROJECT_DIR/src"
BIN_DIR="$PROJECT_DIR/bin"
LIB_DIR="$PROJECT_DIR/lib"
JUNIT_JAR="$LIB_DIR/junit-platform-console-standalone.jar"

mkdir -p "$BIN_DIR"

find "$SRC_DIR" -name "*.java" > "$PROJECT_DIR/sources.txt"
javac -cp "$JUNIT_JAR" -d "$BIN_DIR" @"$PROJECT_DIR/sources.txt"

echo "Compiled successfully"
rm "$PROJECT_DIR/sources.txt"
