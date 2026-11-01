#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

java -cp "$PROJECT_DIR/bin:$PROJECT_DIR/lib/*" Main
