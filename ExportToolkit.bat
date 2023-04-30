@echo off
:: ****************************************************************************************************
::  /$$$$$$$$                                      /$$ /$$$$$$$$               /$$/$$      /$$  /$$    
:: | $$_____/                                     | $$|__  $$__/              | $| $$     |__/ | $$    
:: | $$      /$$   /$$ /$$$$$$  /$$$$$$  /$$$$$$ /$$$$$$ | $$ /$$$$$$  /$$$$$$| $| $$   /$$/$$/$$$$$$  
:: | $$$$$  |  $$ /$$//$$__  $$/$$__  $$/$$__  $|_  $$_/ | $$/$$__  $$/$$__  $| $| $$  /$$| $|_  $$_/  
:: | $$__/   \  $$$$/| $$  \ $| $$  \ $| $$  \__/ | $$   | $| $$  \ $| $$  \ $| $| $$$$$$/| $$ | $$    
:: | $$       >$$  $$| $$  | $| $$  | $| $$       | $$ /$| $| $$  | $| $$  | $| $| $$_  $$| $$ | $$ /$$
:: | $$$$$$$$/$$/\  $| $$$$$$$|  $$$$$$| $$       |  $$$$| $|  $$$$$$|  $$$$$$| $| $$ \  $| $$ |  $$$$/
:: |________|__/  \__| $$____/ \______/|__/        \___/ |__/\______/ \______/|__|__/  \__|__/  \___/  
::                   | $$                                                                              
::                   | $$                                                                              
::                   |__/                                                                              
:: ****************************************************************************************************
:: ExportToolkit ~ Windows Batch Launcher
:: Version: PROTOTYPE ~ 2023/04/29
:: Author: @chrisGrando
:: ****************************************************************************************************
:: NOTE: Since Windows blocks the execution of PowerShell scripts by default, this batch script works
::       merely as a launcher. It temporarily disables security policies and runs the *true* script.
:: ****************************************************************************************************

:: Batch script absolute path
set script_path=%~dp0

:: Checks if a newer version of Windows PowerShell exists
pwsh -version >nul 2>&1 && (
	:: PowerShell Core was found, using it to run the script
	start "Windows PowerShell (Core)" pwsh.exe -ExecutionPolicy Bypass -File "%script_path%\scripts\windows\ExportToolkit.ps1" %script_path%
) || (
	:: Newer version of PowerShell was NOT found, fallback to default
	start "Windows PowerShell (5.1?)" PowerShell.exe -ExecutionPolicy Bypass -File "%script_path%\scripts\windows\ExportToolkit.ps1" %script_path%
)
