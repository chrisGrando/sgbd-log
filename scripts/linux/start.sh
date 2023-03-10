#!/bin/bash

# Global variables
SHELL_PATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
CUSTOM_JRE="$SHELL_PATH/jre/bin/java"
JRE="/bin/java"
JVM_ARGS=""
APP_ARGS=""

# Identify which parameters are for the JVM and which are for the application
while [ $# -gt 0 ] ; do
    case $1 in
        -X*) JVM_ARGS="$JVM_ARGS $1" ;;
        -D*) JVM_ARGS="$JVM_ARGS $1" ;;
          *) APP_ARGS="$APP_ARGS $1" ;;
    esac
    shift
done

# Add "-gui" parameter if nothing was informed
if [ "$APP_ARGS" = "" ] ; then
    APP_ARGS="-gui"
fi

# Search for bundled JRE
if test -f $CUSTOM_JRE ; then
    # Set custom paths
    export PATH="$PATH:$SHELL_PATH/jre/bin"
    export JAVA_HOME=$SHELL_PATH/jre
    JRE=$CUSTOM_JRE

    # Print custom JRE version
    echo "*******************************************************************"
    exec $JRE -version &
    wait $!
    echo "*******************************************************************\n"
fi

# Search for any ".jar" file in the folder
JAR_FILE="$(find $SHELL_PATH -maxdepth 1 -name "*.jar" | head -n 1)"

# Launch application (if any ".jar" file was found)
if [ "$JAR_FILE" != "" ] ; then
    exec $JRE $JVM_ARGS -jar $JAR_FILE $APP_ARGS &
    wait $!
else
    echo "JAR file not found..."
fi

