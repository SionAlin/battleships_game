#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
BIN_DIR="$PROJECT_DIR/bin"
LIB_DIR="$PROJECT_DIR/lib"

java -jar "$LIB_DIR/*" -cp "$BIN_DIR" --scan-classpath
