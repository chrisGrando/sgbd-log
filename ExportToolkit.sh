#!/bin/bash

# ****************************************************************************************************
#  /$$$$$$$$                                      /$$ /$$$$$$$$               /$$/$$      /$$  /$$    
# | $$_____/                                     | $$|__  $$__/              | $| $$     |__/ | $$    
# | $$      /$$   /$$ /$$$$$$  /$$$$$$  /$$$$$$ /$$$$$$ | $$ /$$$$$$  /$$$$$$| $| $$   /$$/$$/$$$$$$  
# | $$$$$  |  $$ /$$//$$__  $$/$$__  $$/$$__  $|_  $$_/ | $$/$$__  $$/$$__  $| $| $$  /$$| $|_  $$_/  
# | $$__/   \  $$$$/| $$  \ $| $$  \ $| $$  \__/ | $$   | $| $$  \ $| $$  \ $| $| $$$$$$/| $$ | $$    
# | $$       >$$  $$| $$  | $| $$  | $| $$       | $$ /$| $| $$  | $| $$  | $| $| $$_  $$| $$ | $$ /$$
# | $$$$$$$$/$$/\  $| $$$$$$$|  $$$$$$| $$       |  $$$$| $|  $$$$$$|  $$$$$$| $| $$ \  $| $$ |  $$$$/
# |________|__/  \__| $$____/ \______/|__/        \___/ |__/\______/ \______/|__|__/  \__|__/  \___/  
#                   | $$                                                                              
#                   | $$                                                                              
#                   |__/                                                                              
# ****************************************************************************************************
# ExportToolkit ~ Linux Bash Shell Launcher
# Version: PROTOTYPE ~ 2023/08/18
# Author: @chrisGrando
# ****************************************************************************************************

# Absolute path to this shell script
SHELL_PATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

# Path to the Linux scripts
SCRIPT_PATH="$SHELL_PATH/scripts/linux"

# Dependencies
isPythonInstalled="$(command -v python3)"
isPipInstalled="$(command -v pip3)"
isPynputInstalled="$(pip3 show pynput)"

# Checks if Python 3.x is NOT installed
if [ "$isPythonInstalled" = "" ] ; then
    echo "[FATAL] Python 3 not found..."
    exit 1
fi

# Checks if Pip 3 is NOT installed
if [ "$isPipInstalled" = "" ] ; then
    echo "[FATAL] Pip 3 not found..."
    exit 1
fi

# Checks if Python dependency "pynput" is NOT installed (and installs it)
if [ "$isPynputInstalled" = "" ] ; then
    pip3 install pynput &
    wait $!
fi

# Run Python script
exec python3 "$SCRIPT_PATH/ExportToolkit.py" $SHELL_PATH &
wait $!
