#!/bin/bash

# Get the directory where the script is located (project root)
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Compiling MusicStore..."
mkdir -p "$PROJECT_DIR/out"

find "$PROJECT_DIR/Musicstore/src/main/java" -name "*.java" > "$PROJECT_DIR/sources.txt"
javac -encoding UTF-8 -d "$PROJECT_DIR/out" @"$PROJECT_DIR/sources.txt"

if [ $? -eq 0 ]; then
    echo "Successfully compiled MusicStore"
    echo ""
    echo "To execute: java -cp $PROJECT_DIR/out main.java.musicstore.Main"
    echo ""
    read -p "Execute now? (y/n): " resp
    if [[ "$resp" == "y" || "$resp" == "Y" ]]; then
        java -cp "$PROJECT_DIR/out" main.java.musicstore.Main
    fi
else
    echo "Compilation error"
fi

rm -f "$PROJECT_DIR/sources.txt"
