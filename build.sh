# build.sh
#!/bin/bash
set -e
cd "$(dirname "$0")"
JARS=$(find lib -name '*.jar' | tr '\n' ':')
export JAVA_HOME="${JAVA_HOME:-/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home}"
CP="${JARS}lib/batik-1.7/lib/*:"
export CP

"$JAVA_HOME/bin/javac" -encoding ISO-8859-1 -Xmaxerrs 500 -cp "$CP" -d build \
  $(find src-ctomUtils src-engine src-swing-gui -name '*.java')

# Copie des ressources (img/, etc.)
cd src-swing-gui
find . -type f ! -name '*.java' | while read f; do
  mkdir -p "../build/$(dirname "$f")"
  cp "$f" "../build/$f"
done

