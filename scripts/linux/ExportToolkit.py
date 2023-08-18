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
# Version: PROTOTYPE ~ 2023/08/18
# Author: @chrisGrando
# ****************************************************************************************************

import platform
import sys
from Export import Export
from Maven import Maven
from Menu import Menu

class ExportToolkit:

    @staticmethod
    def main():
        # Quits if there's no parameters
        if(len(sys.argv) < 2):
            print("[FATAL] Project's root path not informed...")
            exit(1)

        # *** Variables field ***
        
        rootProjectPath = sys.argv[1]
        architecture = platform.architecture()[0]
        menu = Menu()
        maven = Maven()
        export = Export(architecture, rootProjectPath)

        # *** Main code field ***

        # Main Menu
        menu.callKeyboardListener()
        option = menu.mainMenu()

        # Export & Compress
        if(option == 0):
            # Target platforms
            target = menu.platformMenu()

            # Abort if no platforms were selected
            if(not target[0] and not target[1] and not target[2] and not target[3] and not target[4]):
                print("\n-----------------------------------------------------------")
                print("[WARNING] No platforms were selected. Aborting...")
                print("-----------------------------------------------------------")
                menu.pressAnyKey()

            # Compression method
            compression = menu.compressMenu()

            # App name
            name = menu.appName()

            # App version
            version = menu.appVersion()

            # Run Export & Compress
            export.appName = name
            export.appVersion = version
            export.compression = compression
            export.setPlatforms(target)
            export.runExportAndCompress()

            # Exit
            menu.pressAnyKey()

        # Build project
        elif(option == 1):
            # Java JDK not found
            if(not maven.isJavaInstalled()):
                print("\n[FATAL] Java JDK not found...")
                menu.pressAnyKey(1)

            # Apache Maven not found
            if(not maven.isMavenInstalled()):
                print("\n[FATAL] Apache Maven (mvn) not found...")
                menu.pressAnyKey(1)

            # Ask user's confirmation
            print("\n################################################################")
            print("[WARNING] Everything inside the \"target\" folder will be deleted!")
            proceed = menu.yesOrNo()

            # User allowed to continue
            if(proceed):
                maven.buildProject(rootProjectPath)

            # Exit
            menu.pressAnyKey()

        # Quit
        else:
            menu.pressAnyKey()
        
# ****************************************************************************************************
# Calls main function
if __name__ == "__main__":
    ExportToolkit.main()
