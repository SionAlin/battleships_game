#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
BIN_DIR="$PROJECT_DIR/bin"
JUNIT_JAR="$PROJECT_DIR/lib/junit-platform-console-standalone.jar"

java -jar "$JUNIT_JAR" -cp "$BIN_DIR" --scan-classpath
