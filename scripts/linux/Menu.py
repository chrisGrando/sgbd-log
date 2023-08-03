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

import os
from pynput import keyboard

class Menu:

    def mainMenu(self):
        item = 0
        loop = True
        leftCursor = ""
        rightCursor = ""

        # Initialize keyboard listener
        keyboard_listener = keyboard.Listener(suppress=True)
        keyboard_listener.start()

        # Menu loop
        while loop:
            # Clear the screen
            os.system('clear')
            
            # Menu header
            print("***********************************************************")
            print(" _____                      _ _____           _ _    _ _   ")
            print("|  ___|                    | |_   _|         | | |  (_) |  ")
            print("| |____  ___ __   ___  _ __| |_| | ___   ___ | | | ___| |_ ")
            print("|  __\ \/ / '_ \ / _ \| '__| __| |/ _ \ / _ \| | |/ / | __|")
            print("| |___>  <| |_) | (_) | |  | |_| | (_) | (_) | |   <| | |_ ")
            print("\____/_/\_\ .__/ \___/|_|   \__\_/\___/ \___/|_|_|\_\_|\__|")
            print("          | |                                              ")
            print("          |_|                                              ")
            print("***********************************************************")
            print("           ExportToolkit ~ Linux Python Edition")
            print("             Version: PROTOTYPE ~ 2023/08/02")
            print("                  Author: @chrisGrando")
            print("***********************************************************")
            print("Select an option below:")
            print("-----------------------------------------------------------")

            # *** Menu options + cursor ***

            # 1) Export & Compress
            if(item == 0):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            option_0 = leftCursor + "[1]" + rightCursor + " Export & Compress"

            # 2) Build project
            if(item == 1):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            option_1 = leftCursor + "[2]" + rightCursor + " Build project"
            
            # 3) Quit
            if(item == 2):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            option_2 = leftCursor + "[3]" + rightCursor + " Quit"

            # Render menu options
            print(option_0)
            print(option_1)
            print(option_2)
            print("-----------------------------------------------------------")
            print("CONTROLS:")
            print(" * [Arrow Keys] ===> Move cursor")
            print(" * [SPACEBAR] =====> Select option")

            # Keyboard controls
            with keyboard.Events() as events:
                for event in events:
                    if isinstance(event, keyboard.Events.Press):
                        # Move cursor UP
                        if(event.key == keyboard.Key.up or event.key == keyboard.Key.left):
                            if(item == 0):
                                item = 3
                            ##############
                            item -= 1
                            break

                        # Move cursor DOWN
                        if(event.key == keyboard.Key.down or event.key == keyboard.Key.right):
                            if(item == 2):
                                item = -1
                            ##############
                            item += 1
                            break

                        # Confirm / loop stop condition
                        if(event.key == keyboard.Key.space):
                            loop = False
                            break
        
        # Kill keyboard listener
        keyboard_listener.stop()

        # Return selected option
        return item

    def platformMenu(self):
        item = 0
        loop = True
        leftCursor = ""
        rightCursor = ""
        checked = ""

        print()

    def compressMenu(self):
        item = 0
        loop = True
        leftCursor = ""
        rightCursor = ""

        print()
