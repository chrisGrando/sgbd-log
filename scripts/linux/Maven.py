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
# ExportToolkit ~ Linux Python Edition
# Version: PROTOTYPE ~ 2023/08/08
# Author: @chrisGrando
# ****************************************************************************************************

import os
import subprocess

class Maven:

    # Checks if JDK is avaliable
    def isJavaInstalled(self):
        result = subprocess.call(["command", "-v", "javac"], shell=True)
        return not bool(result)
    
    # Checks if Apache Mavem (mvn) is avaliable
    def isMavenInstalled(self):
        result = subprocess.call(["command", "-v", "mvn"], shell=True)
        return not bool(result)
    
    # Build the project via "mvn clean install" 
    def buildProject(self, path):
        os.system('clear')
        os.chdir(path)
        print("-----------------------------------------------------------")
        subprocess.run(["mvn", "-v"], stderr=subprocess.STDOUT)
        print("-----------------------------------------------------------")
        subprocess.run(["mvn", "clean", "install"], stderr=subprocess.STDOUT)
