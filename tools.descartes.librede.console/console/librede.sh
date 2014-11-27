#!/bin/sh
#
# ==============================================
#  LibReDE : Library for Resource Demand Estimation
# ==============================================
#
# (c) Copyright 2013-2014, by Simon Spinner and Contributors.
#
# Project Info:   http://www.descartes-research.net/
#
# All rights reserved. This software is made available under the terms of the
# Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
# http://www.eclipse.org/legal/epl-v10.html
#
# This software is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
# for more details.
#
# You should have received a copy of the Eclipse Public License (EPL)
# along with this software; if not visit http://www.eclipse.org or write to
# Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
# Email: license (at) eclipse.org
#
# [Java is a trademark or registered trademark of Sun Microsystems, Inc.
# in the United States and other countries.]
#


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
