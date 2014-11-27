#!/bin/sh

# Needed if symbolic link is in the path
BASEPATH=$(readlink -f "$0")
BASEPATH=$(dirname "$BASEPATH")
BASEPATH=$(dirname "$BASEPATH")

# Build classpath
for f in $(find "$BASEPATH/lib/plugins" -name '*.jar')
do
  CLASSPATH=$CLASSPATH:$f
done

# Determine architecture of java
java -d64 -version > /dev/null 2>&1
if [ $? -eq 0 ]
then
  java -cp $CLASSPATH -Djava.library.path=$BASEPATH/lib/os/linux/x86_64 -Djna.library.path=$BASEPATH/lib/os/linux/x86_64 tools.descartes.librede.frontend.Console "$@"
else
  echo "No 64-bit Java installation found. Check your PATH variable."
fi
