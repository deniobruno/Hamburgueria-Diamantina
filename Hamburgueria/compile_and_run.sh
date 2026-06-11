#!/bin/bash
# ============================================================
# Script de compilação e execução — Hamburgueria POO
# ============================================================
set -e

# Localiza JDK local se não houver java no PATH do sistema
if ! command -v javac &>/dev/null; then
    if [ -d "$HOME/jdk21/Contents/Home/bin" ]; then
        export JAVA_HOME="$HOME/jdk21/Contents/Home"
        export PATH="$JAVA_HOME/bin:$PATH"
    fi
fi

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC="$SCRIPT_DIR/src"
OUT="$SCRIPT_DIR/out"
LIB="$SCRIPT_DIR/lib/gson-2.10.1.jar"
DATA="$SCRIPT_DIR/data"
JAVADOC_DIR="$SCRIPT_DIR/doc"

mkdir -p "$OUT" "$DATA"

echo "=== Compilando ==="
find "$SRC" -name "*.java" > /tmp/sources.txt
javac -cp "$LIB" -d "$OUT" @/tmp/sources.txt
echo "Compilação OK."

if [[ "$1" == "javadoc" ]]; then
    echo "=== Gerando JavaDoc ==="
    mkdir -p "$JAVADOC_DIR"
    javadoc -cp "$LIB" -d "$JAVADOC_DIR" -sourcepath "$SRC" \
        -subpackages model:sistema:persistencia:relatorios:ui \
        -windowtitle "Hamburgueria Diamantina — POO" \
        -encoding UTF-8 -charset UTF-8
    echo "JavaDoc em: $JAVADOC_DIR/index.html"
    exit 0
fi

echo "=== Executando ==="
cd "$SCRIPT_DIR"
java -cp "$OUT:$LIB" Main
