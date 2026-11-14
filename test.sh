#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
BIN_DIR="$PROJECT_DIR/bin"
LIB_DIR="$PROJECT_DIR/lib"

java -jar "$LIB_DIR/junit-platform-console-standalone.jar" execute --class-path "$BIN_DIR:$LIB_DIR/postgresql-42.7.14.jar" --scan-class-path
