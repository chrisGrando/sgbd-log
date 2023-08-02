#!/bin/bash

#   /$$$$$$                         /$$$$$  /$$$$$$  /$$      
#  /$$__  $$                       |__  $$ /$$__  $$| $$      
# | $$  \__/  /$$$$$$  /$$$$$$$       | $$| $$  \ $$| $$      
# | $$ /$$$$ /$$__  $$| $$__  $$      | $$| $$$$$$$$| $$      
# | $$|_  $$| $$$$$$$$| $$  \ $$ /$$  | $$| $$__  $$| $$      
# | $$  \ $$| $$_____/| $$  | $$| $$  | $$| $$  | $$| $$      
# |  $$$$$$/|  $$$$$$$| $$  | $$|  $$$$$$/| $$  | $$| $$$$$$$$
#  \______/  \_______/|__/  |__/ \______/ |__/  |__/|________/
# ------------------------------------------------------------
# 
# Generic Java Application Launcher
# Version: PROTOTYPE ~ 2023/08/02
# Author: @chrisGrando

### VARIABLES ###

SHELL_PATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
CUSTOM_JRE="$SHELL_PATH/jre/bin/java"
JRE="/dev/null"
JVM_ARGS=""
APP_ARGS=""
JAR="*.jar"

### MAIN SCRIPT ###

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
    export JAVA_HOME="$SHELL_PATH/jre"
    JRE=$CUSTOM_JRE

    # Set bundled Java as executable
    chmod +x $JRE

    # Print custom JRE version
    echo "*******************************************************************"
    echo "[INFO] Using bundled JRE!"
    echo "*******************************************************************"
    exec $JRE -version &
    wait $!
    echo "*******************************************************************\n"

# If no bundled JRE was found, check if java is globally available in the system
elif command -v java > /dev/null ; then
    # Use global path
    JRE="/bin/java"

    # Print global JRE version
    echo "*******************************************************************"
    echo "[INFO] Using global JRE!"
    echo "*******************************************************************"
    exec java -version &
    wait $!
    echo "*******************************************************************\n"

# Terminate script with error code if java was not found
else
    echo "[FATAL] Java (JRE) not found..."
    exit 1
fi

# Search for any ".jar" file in the folder
jar_file="$(find $SHELL_PATH -maxdepth 1 -name $JAR | head -n 1)"

# Launch application if any ".jar" file was found
if [ "$jar_file" != "" ] ; then
    exec $JRE $JVM_ARGS -jar $jar_file $APP_ARGS &
    wait $!

# Terminate script with error code otherwise
else
    echo "[FATAL] JAR file not found..."
    exit 1
fi
