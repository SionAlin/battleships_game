#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

SRC_DIR="$PROJECT_DIR/src"
BIN_DIR="$PROJECT_DIR/bin"
LIB_DIR="$PROJECT_DIR/lib"

mkdir -p "$BIN_DIR"

find "$SRC_DIR" -name "*.java" > "$PROJECT_DIR/sources.txt"
javac -cp "$LIB_DIR/*" -d "$BIN_DIR" @"$PROJECT_DIR/sources.txt"
STATUS=$?

rm "$PROJECT_DIR/sources.txt"

if [ "$STATUS" -eq 0 ]; then
    echo "Compiled successfully"
else
    echo "Compilation failed"
    exit "$STATUS"
fi
