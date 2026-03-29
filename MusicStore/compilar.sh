#!/bin/bash

echo "Compiling MusicStore..."
mkdir -p out

find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt

if [ $? -eq 0 ]; then
    echo "Successfully compiled MusicStore"
    echo ""
    echo "To execute: java -cp out musicstore.Main"
    echo ""
    read -p "Execute now? (y/n): " resp
    if [[ "$resp" == "y" || "$resp" == "Y" ]]; then
        java -cp out musicstore.Main
    fi
else
    echo "Compilation error"
fi

rm -f sources.txt
