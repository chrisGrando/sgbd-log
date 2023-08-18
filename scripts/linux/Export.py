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

import os
import subprocess
import shutil
import stat

class Export:

    def __init__(self, arch, rootPath):
        self.arch = arch
        self.rootPath = rootPath
        self.bin7z = self.set7z()
        self.appName = "sgbd-log"
        self.appVersion = "1.0"
        self.compression = None
        self.WINDOWS_x86 = False
        self.WINDOWS_x64 = False
        self.LINUX_i386 = False
        self.LINUX_amd64 = False
        self.JAR_ONLY = False

    # Set full path to 7z binaries
    def set7z(self):
        # Default = 32 bits binary
        path = self.rootPath + "/7z/linux_i386/7zz"

        # 64 bits is avaliable
        if "64" in self.arch:
            path = self.rootPath + "/7z/linux_amd64/7zz"

        return path
    
    # Set platforms to export
    def setPlatforms(self, platforms=[]):
        self.WINDOWS_x86 = platforms[0]
        self.WINDOWS_x64 = platforms[1]
        self.LINUX_i386 = platforms[2]
        self.LINUX_amd64 = platforms[3]
        self.JAR_ONLY = platforms[4]

    # Execute Export & Compress
    def runExportAndCompress(self):
        exportPath = self.rootPath + "/export"
        binariesPath = self.rootPath + "/target"
        assetsPath = self.rootPath + "/database"
        jrePath = self.rootPath + "/7z/files"
        appNameWithVersion = self.appName + "_v" + self.appVersion

        # Enum compress option
        COMPRESS_7Z = 0
        COMPRESS_ZIP = 1
        COMPRESS_ZIP_TAR = 2

        # Clear screen
        os.system('clear')

        print("STARTING THE EXPORTATION PROCESS...")
        print("-----------------------------------------------------------\n")

        # Abort if "target" folder doesn't exist or is empty
        if(os.path.exists(binariesPath)):
            if(len(os.listdir(binariesPath)) == 0):
                print("[FATAL] Target folder is empty. Aborting...")
                return
        else:
            print("[FATAL] Target folder doesn't exist. Aborting...")
            return

        # Make 7z executable
        st = os.stat(self.bin7z)
        os.chmod(self.bin7z, st.st_mode | stat.S_IEXEC)

        # Delete export folder if exists
        if(os.path.exists(exportPath)):
            shutil.rmtree(exportPath)

        # Create export folder and open it
        print("Creating export folder...\n" + exportPath)
        os.mkdir(exportPath)
        os.chdir(exportPath)

        # 32-bit Windows
        if(self.WINDOWS_x86):
            finalAppFolderName = appNameWithVersion + "_Windows_x86"
            currentBuildPath = exportPath + "/" + finalAppFolderName
            currentAssetsPath = currentBuildPath + "/database"

            # Create build folder
            print("\nCreating " + finalAppFolderName + " build folder...\n")
            os.mkdir(currentBuildPath)
            os.mkdir(currentAssetsPath)

            # Copy files to the build folder
            print("Copying files to the " + finalAppFolderName + " folder...")

            # Target folder
            for file in os.listdir(binariesPath):
                filePath = binariesPath + "/" + file

                if (file.endswith(".jar") or file.endswith("-x32.exe")):
                    shutil.copy(filePath, currentBuildPath)
                    print(" * " + file + " [OK]...")

            # Assets (database) folder
            if(os.path.exists(assetsPath)):
                for file in os.listdir(assetsPath):
                    filePath = assetsPath + "/" + file

                    if(not file.endswith(".pdf")):
                        shutil.copy(filePath, currentAssetsPath)
                        print(" * " + file + " [OK]...")

            # Readme file
            if(os.path.exists(self.rootPath + "/README.md")):
                shutil.copy(self.rootPath + "/README.md", currentBuildPath)
                print(" * README.md [OK]...")

            # Extract JRE
            print("\nExtracting JRE contents...")
            os.chdir(currentBuildPath)
            auxJRE = jrePath + "/JRE17_Windows_x86.7z"
            subprocess.run([self.bin7z, "x", "-y", auxJRE], stderr=subprocess.STDOUT)

            # Package everything in the build folder
            print("\nPackaging the " + finalAppFolderName + " folder contents...")
            os.chdir(exportPath)

            if(self.compression == COMPRESS_7Z):
                auxFile = finalAppFolderName + ".7z"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP):
                auxFile = finalAppFolderName + ".zip"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP_TAR):
                auxFile = finalAppFolderName + ".zip"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            else:
                print("Compression is disabled. Skipping...")

            print("-----------------------------------------------------------")

        # 64-bit Windows
        if(self.WINDOWS_x64):
            finalAppFolderName = appNameWithVersion + "_Windows_x64"
            currentBuildPath = exportPath + "/" + finalAppFolderName
            currentAssetsPath = currentBuildPath + "/database"

            # Create build folder
            print("\nCreating " + finalAppFolderName + " build folder...\n")
            os.mkdir(currentBuildPath)
            os.mkdir(currentAssetsPath)

            # Copy files to the build folder
            print("Copying files to the " + finalAppFolderName + " folder...")

            # Target folder
            for file in os.listdir(binariesPath):
                filePath = binariesPath + "/" + file

                if (file.endswith(".jar") or file.endswith("-x64.exe")):
                    shutil.copy(filePath, currentBuildPath)
                    print(" * " + file + " [OK]...")

            # Assets (database) folder
            if(os.path.exists(assetsPath)):
                for file in os.listdir(assetsPath):
                    filePath = assetsPath + "/" + file

                    if(not file.endswith(".pdf")):
                        shutil.copy(filePath, currentAssetsPath)
                        print(" * " + file + " [OK]...")

            # Readme file
            if(os.path.exists(self.rootPath + "/README.md")):
                shutil.copy(self.rootPath + "/README.md", currentBuildPath)
                print(" * README.md [OK]...")

            # Extract JRE
            print("\nExtracting JRE contents...")
            os.chdir(currentBuildPath)
            auxJRE = jrePath + "/JRE17_Windows_x64.7z"
            subprocess.run([self.bin7z, "x", "-y", auxJRE], stderr=subprocess.STDOUT)

            # Package everything in the build folder
            print("\nPackaging the " + finalAppFolderName + " folder contents...")
            os.chdir(exportPath)

            if(self.compression == COMPRESS_7Z):
                auxFile = finalAppFolderName + ".7z"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP):
                auxFile = finalAppFolderName + ".zip"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP_TAR):
                auxFile = finalAppFolderName + ".zip"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            else:
                print("Compression is disabled. Skipping...")

            print("-----------------------------------------------------------")

        # 32-bit Linux
        if(self.LINUX_i386):
            finalAppFolderName = appNameWithVersion + "_Linux_i386"
            currentBuildPath = exportPath + "/" + finalAppFolderName
            currentAssetsPath = currentBuildPath + "/database"

            # Create build folder
            print("\nCreating " + finalAppFolderName + " build folder...\n")
            os.mkdir(currentBuildPath)
            os.mkdir(currentAssetsPath)

            # Copy files to the build folder
            print("Copying files to the " + finalAppFolderName + " folder...")

            # Target folder
            for file in os.listdir(binariesPath):
                filePath = binariesPath + "/" + file

                if (file.endswith(".jar")):
                    shutil.copy(filePath, currentBuildPath)
                    print(" * " + file + " [OK]...")

            # Assets (database) folder
            if(os.path.exists(assetsPath)):
                for file in os.listdir(assetsPath):
                    filePath = assetsPath + "/" + file

                    if(not file.endswith(".pdf")):
                        shutil.copy(filePath, currentAssetsPath)
                        print(" * " + file + " [OK]...")

            # Generic Linux Launcher
            if(os.path.exists(self.rootPath + "/scripts/linux/GenJAL.sh")):
                shutil.copy(self.rootPath + "/scripts/linux/GenJAL.sh", currentBuildPath)
                os.rename(currentBuildPath + "/GenJAL.sh", currentBuildPath + "/start.sh")
                print(" * GenJAL.sh [OK]...")

            # Readme file
            if(os.path.exists(self.rootPath + "/README.md")):
                shutil.copy(self.rootPath + "/README.md", currentBuildPath)
                print(" * README.md [OK]...")

            # Extract JRE
            print("\nExtracting JRE contents...")
            os.chdir(currentBuildPath)
            auxJRE = jrePath + "/JRE17_Linux_i386.7z"
            subprocess.run([self.bin7z, "x", "-y", auxJRE], stderr=subprocess.STDOUT)

            # Package everything in the build folder
            print("\nPackaging the " + finalAppFolderName + " folder contents...")
            os.chdir(exportPath)

            if(self.compression == COMPRESS_7Z):
                auxFile = finalAppFolderName + ".7z"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP):
                auxFile = finalAppFolderName + ".zip"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP_TAR):
                auxFile = finalAppFolderName + ".tar"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            else:
                print("Compression is disabled. Skipping...")

            print("-----------------------------------------------------------")

        # 64-bit Linux
        if(self.LINUX_amd64):
            finalAppFolderName = appNameWithVersion + "_Linux_amd64"
            currentBuildPath = exportPath + "/" + finalAppFolderName
            currentAssetsPath = currentBuildPath + "/database"

            # Create build folder
            print("\nCreating " + finalAppFolderName + " build folder...\n")
            os.mkdir(currentBuildPath)
            os.mkdir(currentAssetsPath)

            # Copy files to the build folder
            print("Copying files to the " + finalAppFolderName + " folder...")

            # Target folder
            for file in os.listdir(binariesPath):
                filePath = binariesPath + "/" + file

                if (file.endswith(".jar")):
                    shutil.copy(filePath, currentBuildPath)
                    print(" * " + file + " [OK]...")

            # Assets (database) folder
            if(os.path.exists(assetsPath)):
                for file in os.listdir(assetsPath):
                    filePath = assetsPath + "/" + file

                    if(not file.endswith(".pdf")):
                        shutil.copy(filePath, currentAssetsPath)
                        print(" * " + file + " [OK]...")

            # Generic Linux Launcher
            if(os.path.exists(self.rootPath + "/scripts/linux/GenJAL.sh")):
                shutil.copy(self.rootPath + "/scripts/linux/GenJAL.sh", currentBuildPath)
                os.rename(currentBuildPath + "/GenJAL.sh", currentBuildPath + "/start.sh")
                print(" * GenJAL.sh [OK]...")

            # Readme file
            if(os.path.exists(self.rootPath + "/README.md")):
                shutil.copy(self.rootPath + "/README.md", currentBuildPath)
                print(" * README.md [OK]...")

            # Extract JRE
            print("\nExtracting JRE contents...")
            os.chdir(currentBuildPath)
            auxJRE = jrePath + "/JRE17_Linux_amd64.7z"
            subprocess.run([self.bin7z, "x", "-y", auxJRE], stderr=subprocess.STDOUT)

            # Package everything in the build folder
            print("\nPackaging the " + finalAppFolderName + " folder contents...")
            os.chdir(exportPath)

            if(self.compression == COMPRESS_7Z):
                auxFile = finalAppFolderName + ".7z"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP):
                auxFile = finalAppFolderName + ".zip"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP_TAR):
                auxFile = finalAppFolderName + ".tar"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            else:
                print("Compression is disabled. Skipping...")

            print("-----------------------------------------------------------")

        # Universal JAR file
        if(self.JAR_ONLY):
            finalAppFolderName = appNameWithVersion + "_Jar"
            currentBuildPath = exportPath + "/" + finalAppFolderName
            currentAssetsPath = currentBuildPath + "/database"

            # Create build folder
            print("\nCreating " + finalAppFolderName + " build folder...\n")
            os.mkdir(currentBuildPath)
            os.mkdir(currentAssetsPath)

            # Copy files to the build folder
            print("Copying files to the " + finalAppFolderName + " folder...")

            # Target folder
            for file in os.listdir(binariesPath):
                filePath = binariesPath + "/" + file

                if (file.endswith(".jar")):
                    shutil.copy(filePath, currentBuildPath)
                    print(" * " + file + " [OK]...")

            # Assets (database) folder
            if(os.path.exists(assetsPath)):
                for file in os.listdir(assetsPath):
                    filePath = assetsPath + "/" + file

                    if(not file.endswith(".pdf")):
                        shutil.copy(filePath, currentAssetsPath)
                        print(" * " + file + " [OK]...")

            # Readme file
            if(os.path.exists(self.rootPath + "/README.md")):
                shutil.copy(self.rootPath + "/README.md", currentBuildPath)
                print(" * README.md [OK]...")

            # Package everything in the build folder
            print("\nPackaging the " + finalAppFolderName + " folder contents...")
            os.chdir(exportPath)

            if(self.compression == COMPRESS_7Z):
                auxFile = finalAppFolderName + ".7z"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP):
                auxFile = finalAppFolderName + ".zip"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            elif(self.compression == COMPRESS_ZIP_TAR):
                auxFile = finalAppFolderName + ".zip"
                subprocess.run([self.bin7z, "a", "-y", "-sdel", "-mx9", auxFile, finalAppFolderName], stderr=subprocess.STDOUT)
            else:
                print("Compression is disabled. Skipping...")

            print("-----------------------------------------------------------")
