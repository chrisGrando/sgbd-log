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
# Version: PROTOTYPE ~ 2023/08/09
# Author: @chrisGrando
# ****************************************************************************************************

import os
from pynput import keyboard

class Menu:

    def __init__(self):
        self.keyboard_listener = None

    # Initialize keyboard listener
    def callKeyboardListener(self):
        self.keyboard_listener = keyboard.Listener(suppress=True)
        self.keyboard_listener.start()

    # Stop keyboard listener
    def killKeyboardListener(self):
        self.keyboard_listener.stop()

    # Set keyboard's input suppress state
    def setSuppress(self, state):
        self.keyboard_listener.suppress(state)

    # Script's main menu screen
    def mainMenu(self):
        item = 0
        loop = True
        leftCursor = ""
        rightCursor = ""

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
            print("             Version: PROTOTYPE ~ 2023/08/09")
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
                    if isinstance(event, keyboard.Events.Release):
                        # Move cursor UP
                        if(event.key == keyboard.Key.up or event.key == keyboard.Key.left):
                            if(item == 0):
                                item = 3
                            ##############
                            item -= 1

                        # Move cursor DOWN
                        if(event.key == keyboard.Key.down or event.key == keyboard.Key.right):
                            if(item == 2):
                                item = -1
                            ##############
                            item += 1

                        # Confirm / loop stop condition
                        if(event.key == keyboard.Key.space):
                            loop = False

                        # End of event
                        break

        # Return selected option
        return item

    # The target OS + Architecture to export the build
    def platformMenu(self):
        item = 0
        loop = True
        leftCursor = ""
        rightCursor = ""
        checked = ""

        # Platforms
        SELECT_ALL = False
        WINDOWS_x86 = False
        WINDOWS_x64 = False
        LINUX_i386 = False
        LINUX_amd64 = False
        JAR_ONLY = False

        # Menu loop
        while loop:
            # Clear the screen
            os.system('clear')

            # Menu header
            print("-----------------------------------------------------------")
            print("Select the platforms you wish to export your project for:")
            print("-----------------------------------------------------------")

            # *** Menu options + cursors ***

            # 1) Select all platforms
            if(item == 0):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            if(SELECT_ALL):
                checked = "X"
            else:
                checked = " "

            option_0 = leftCursor + "[" + checked + "]" + rightCursor + " Select all platforms"

            # 2) Windows x86
            if(item == 1):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            if(WINDOWS_x86):
                checked = "X"
            else:
                checked = " "

            option_1 = leftCursor + "[" + checked + "]" + rightCursor + " Windows x86"

            # 3) Windows x64
            if(item == 2):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            if(WINDOWS_x64):
                checked = "X"
            else:
                checked = " "

            option_2 = leftCursor + "[" + checked + "]" + rightCursor + " Windows x64"

            # 4) Linux i386
            if(item == 3):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            if(LINUX_i386):
                checked = "X"
            else:
                checked = " "

            option_3 = leftCursor + "[" + checked + "]" + rightCursor + " Linux i386"

            # 5) Linux amd64
            if(item == 4):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            if(LINUX_amd64):
                checked = "X"
            else:
                checked = " "

            option_4 = leftCursor + "[" + checked + "]" + rightCursor + " Linux amd64"

            # 6) JAR only
            if(item == 5):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            if(JAR_ONLY):
                checked = "X"
            else:
                checked = " "

            option_5 = leftCursor + "[" + checked + "]" + rightCursor + " JAR only"

            # Render menu options
            print(option_0)
            print(option_1)
            print(option_2)
            print(option_3)
            print(option_4)
            print(option_5)
            print("-----------------------------------------------------------")
            print("CONTROLS:")
            print(" * [Arrow UP] and [Arrow DOWN] ======> Move cursor")
            print(" * [Arrow LEFT] and [Arrow RIGHT] ===> Toggle ON / OFF")
            print(" * [SPACEBAR] =======================> Proceed")

            # Keyboard controls
            with keyboard.Events() as events:
                for event in events:
                    if isinstance(event, keyboard.Events.Release):
                        # Move cursor UP
                        if(event.key == keyboard.Key.up):
                            if(item == 0):
                                item = 6
                            ##############
                            item -= 1

                        # Move cursor DOWN
                        if(event.key == keyboard.Key.down):
                            if(item == 5):
                                item = -1
                            ##############
                            item += 1

                        # Toggle ON
                        if(event.key == keyboard.Key.right):
                            # Windows x86
                            if(item == 1):
                                WINDOWS_x86 = True
                            # Windows x64
                            elif(item == 2):
                                WINDOWS_x64 = True
                            # Linux i386
                            elif(item == 3):
                                LINUX_i386 = True
                            # Linux amd64
                            elif(item == 4):
                                LINUX_amd64 = True
                            # JAR only
                            elif(item == 5):
                                JAR_ONLY = True
                            # Toggle ALL
                            else:
                                SELECT_ALL = True
                                WINDOWS_x86 = True
                                WINDOWS_x64 = True
                                LINUX_i386 = True
                                LINUX_amd64 = True
                                JAR_ONLY = True

                        # Toggle OFF
                        if(event.key == keyboard.Key.left):
                            # Select all platforms
                            SELECT_ALL = False
                            # Windows x86
                            if(item == 1):
                                WINDOWS_x86 = False
                            # Windows x64
                            elif(item == 2):
                                WINDOWS_x64 = False
                            # Linux i386
                            elif(item == 3):
                                LINUX_i386 = False
                            # Linux amd64
                            elif(item == 4):
                                LINUX_amd64 = False
                            # JAR only
                            elif(item == 5):
                                JAR_ONLY = False
                            # Toggle ALL
                            else:
                                WINDOWS_x86 = False
                                WINDOWS_x64 = False
                                LINUX_i386 = False
                                LINUX_amd64 = False
                                JAR_ONLY = False

                        # Confirm / loop stop condition
                        if(event.key == keyboard.Key.space):
                            loop = False

                        # End of event
                        break

            # Check if all but the first option are checked
            if((not SELECT_ALL) and WINDOWS_x86 and WINDOWS_x64 and LINUX_i386 and LINUX_amd64 and JAR_ONLY):
                SELECT_ALL = True

        # Return selected platforms
        return [WINDOWS_x86, WINDOWS_x64, LINUX_i386, LINUX_amd64, JAR_ONLY]

    # The compression method for the binaries
    def compressMenu(self):
        item = 0
        loop = True
        leftCursor = ""
        rightCursor = ""

        # Menu loop
        while loop:
            # Clear the screen
            os.system('clear')

            # Menu header
            print("-----------------------------------------------------------")
            print("Select an compression method to package the binaries:")
            print("-----------------------------------------------------------")

            # *** Menu options + cursor ***

            # 1) Everything as 7z
            if(item == 0):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            option_0 = leftCursor + "[1]" + rightCursor + " Everything as 7z (recommended)"

            # 2) Everything as zip
            if(item == 1):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            option_1 = leftCursor + "[2]" + rightCursor + " Everything as zip"
            
            # 3) Jar and Windows builds as zip, everything else as tar
            if(item == 2):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            option_2 = leftCursor + "[3]" + rightCursor + " Jar and Windows builds as zip, everything else as tar"

            # 4) Don't compress anything
            if(item == 3):
                leftCursor = "=>"
                rightCursor = "<="
            else:
                leftCursor = "  "
                rightCursor = "  "

            option_3 = leftCursor + "[4]" + rightCursor + " Don't compress anything"

            # Render menu options
            print(option_0)
            print(option_1)
            print(option_2)
            print(option_3)
            print("-----------------------------------------------------------")
            print("CONTROLS:")
            print(" * [Arrow Keys] ===> Move cursor")
            print(" * [SPACEBAR] =====> Select option")

            # Keyboard controls
            with keyboard.Events() as events:
                for event in events:
                    if isinstance(event, keyboard.Events.Release):
                        # Move cursor UP
                        if(event.key == keyboard.Key.up or event.key == keyboard.Key.left):
                            if(item == 0):
                                item = 4
                            ##############
                            item -= 1

                        # Move cursor DOWN
                        if(event.key == keyboard.Key.down or event.key == keyboard.Key.right):
                            if(item == 3):
                                item = -1
                            ##############
                            item += 1

                        # Confirm / loop stop condition
                        if(event.key == keyboard.Key.space):
                            loop = False

                        # End of event
                        break

        # Return selected option
        return item

    # Confirmation message
    def yesOrNo(self):
        loop = True
        proceed = None

        print("Do you wish to proceed? (Y / N)")

        while loop:
            # User's choice
            with keyboard.Events() as events:
                for event in events:
                    if isinstance(event, keyboard.Events.Release):
                        # Yes
                        if(event.key == keyboard.KeyCode.from_char("y")):
                            proceed = True
                            loop = False

                        # No
                        if(event.key == keyboard.KeyCode.from_char("n")):
                            proceed = False
                            loop = False

                        # End of event
                        break

        # Return choice
        return proceed

    # Wait for the user to press any key and exit
    def pressAnyKey(self, code=0):
        print("\n*** END OF SCRIPT ***")
        print("Press any key to exit...")

        # Wait for any key press
        with keyboard.Events() as events:
            for event in events:
                if isinstance(event, keyboard.Events.Release):
                    break

        # Exit script
        self.killKeyboardListener()
        exit(code)
