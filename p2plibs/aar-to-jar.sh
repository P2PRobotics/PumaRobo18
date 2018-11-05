#!/bin/bash -x

HERE="$(cd "$(dirname "$0")"; pwd)"
JAR=$(which jar)
RET=$?

if [[ ${RET} != 0 ]]; then 
  echo "ERROR: 'jar' executable not found. Exiting."
  exit 1
fi

AAR_LIB="${HERE}/../../libs"
JAR_LIB="${HERE}"

for aar in "${AAR_LIB}"/*.aar; do
  BASENAME="$(basename -s .aar "${aar}")"
  ( cd "${JAR_LIB}" && "${JAR}" xvf "${aar}" classes.jar && mv classes.jar "${BASENAME}.jar")
done
