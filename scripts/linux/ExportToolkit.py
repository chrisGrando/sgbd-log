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
# Version: PROTOTYPE ~ 2023/08/02
# Author: @chrisGrando
# ****************************************************************************************************

import platform
import sys
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

        # *** Main code field ***
        option = menu.mainMenu()
        print("\nSelected: " + str(option))
        
# ****************************************************************************************************
# Calls main function
if __name__ == "__main__":
    ExportToolkit.main()
