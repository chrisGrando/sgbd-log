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
# ExportToolkit ~ Windows PowerShell Edition
# Version: PROTOTYPE ~ 2023/04/29
# Author: @chrisGrando
# ****************************************************************************************************

#### COMMAND LINE PARAMETERS FIELD ####

param (
	[Parameter(Mandatory=$true)]
	[string] $projectAbsolutePath
)

#### IMPORT FIELD ####

Import-Module -Name "$projectAbsolutePath/scripts/windows/Export-Compress.psm1" #-Verbose

#### GLOBAL VARIABLES FIELD ####

# Constants
New-Variable -Name KEY_UP -Value 38 -Option Constant
New-Variable -Name KEY_DOWN -Value 40 -Option Constant
New-Variable -Name KEY_LEFT -Value 37 -Option Constant
New-Variable -Name KEY_RIGHT -Value 39 -Option Constant
New-Variable -Name KEY_SPACEBAR -Value 32 -Option Constant
New-Variable -Name WPS_VERSION -Value ([double] "$($PSVersionTable.PSVersion.Major).$($PSVersionTable.PSVersion.Minor)") -Option Constant

# NOT Constants
$currentOption = 0
$maxOptionNumber = 4
$cursorLeft = ""
$cursorRight = ""

#### FUNCTIONS FIELD ####

# Wait for the user to press [any key] to exit
function WaitAndExit {
	Write-Host "`n*** END OF SCRIPT ***"
	Write-Host "Press any key to exit..."
	$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown');
	exit 0
}

# Execute Apache Maven to perform an "clean install"
function BuildMavenProject {
	WaitAndExit
}

# Erase Windows icon (.ico) file cache
function ClearIconCache {
	WaitAndExit
}

# Export project and compress as 7z, zip or tar.xz
function ExportAndCompress {
	Set-KeyMap $KEY_UP $KEY_DOWN $KEY_LEFT $KEY_RIGHT $KEY_SPACEBAR
	WaitAndExit
}

#### MAIN CODE FIELD ####

# Check if Windows PowerShell version is below 5.1 (and abort the script in case that is true)
if ($WPS_VERSION -lt 5.1) {
	Write-Error "[FATAL] Your version of Windows PowerShell ($WPS_VERSION) is incompatible. Please upgrade to version 5.1 or above."
	cmd /c pause
	exit 1
}

# Main loop
while ($true) {
	# Menu header
	Write-Host "***********************************************************"
	Write-Host " _____                      _ _____           _ _    _ _   "
	Write-Host "|  ___|                    | |_   _|         | | |  (_) |  "
	Write-Host "| |____  ___ __   ___  _ __| |_| | ___   ___ | | | ___| |_ "
	Write-Host "|  __\ \/ / '_ \ / _ \| '__| __| |/ _ \ / _ \| | |/ / | __|"
	Write-Host "| |___>  <| |_) | (_) | |  | |_| | (_) | (_) | |   <| | |_ "
	Write-Host "\____/_/\_\ .__/ \___/|_|   \__\_/\___/ \___/|_|_|\_\_|\__|"
	Write-Host "          | |                                              "
	Write-Host "          |_|                                              "
	Write-Host "***********************************************************"
	Write-Host "    Windows PowerShell Edition ~ PROTOTYPE - 28/04/2023"
	Write-Host "                   Author:  @chrisGrando"
	Write-Host "               Using PowerShell version: $WPS_VERSION"
	Write-Host "***********************************************************"
	Write-Host "`nSelect an option below:"
	Write-Host "-----------------------------------------------------------"
	
	# Menu options + cursor
	
	# 1) Export & Compress
	if ($currentOption -eq 0) {
		$cursorLeft = "=>"
		$cursorRight = "<="
	}
	else {
		$cursorLeft = "  "
		$cursorRight = "  "
	}
	
	$option_1 = "$($cursorLeft)[1]$($cursorRight) Export & Compress"
	
	# 2) Clear icon cache
	if ($currentOption -eq 1) {
		$cursorLeft = "=>"
		$cursorRight = "<="
	}
	else {
		$cursorLeft = "  "
		$cursorRight = "  "
	}
	
	$option_2 = "$($cursorLeft)[2]$($cursorRight) Clear icon cache"
	
	# 3) Build project
	if ($currentOption -eq 2) {
		$cursorLeft = "=>"
		$cursorRight = "<="
	}
	else {
		$cursorLeft = "  "
		$cursorRight = "  "
	}
	
	$option_3 = "$($cursorLeft)[3]$($cursorRight) Build project"
	
	# 4) Quit
	if ($currentOption -eq 3) {
		$cursorLeft = "=>"
		$cursorRight = "<="
	}
	else {
		$cursorLeft = "  "
		$cursorRight = "  "
	}
	
	$option_4 = "$($cursorLeft)[4]$($cursorRight) Quit"
	
	# Render options text
	Write-Host $option_1
	Write-Host $option_2
	Write-Host $option_3
	Write-Host $option_4
	Write-Host "-----------------------------------------------------------"
	Write-Host "CONTROLS:"
	Write-Host " * [Arrow Keys] ===> Move cursor"
	Write-Host " * [SPACE] ========> Select option"
	
	# Manage keyboard input
	$key = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown');
	
	# Up or Left key
	if ($key.VirtualKeyCode -eq $KEY_UP -Or $key.VirtualKeyCode -eq $KEY_LEFT) {
		if ($currentOption -le 0) {
			$currentOption = $maxOptionNumber
		}
		$currentOption--;
	}
	
	# Down or Right key
	if ($key.VirtualKeyCode -eq $KEY_DOWN -Or $key.VirtualKeyCode -eq $KEY_RIGHT) {
		if ($currentOption -ge ($maxOptionNumber - 1)) {
			$currentOption = -1
		}
		$currentOption++;
	}
	
	# Spacebar key
	if ($key.VirtualKeyCode -eq $KEY_SPACEBAR) {
		break
	}
	
	# Clear all screen
	clear
}

# An option was selected
switch ($currentOption) {
	# Export & Compress
	0 {
		ExportAndCompress
	}
	# Clear icon cache
	1 {
		ClearIconCache
	}
	# Build project
	2 {
		BuildMavenProject
	}
	# Quit
	3 {
		WaitAndExit
	}
	# Invalid
	default {
		Write-Error "`n[FATAL] Selected option is invalid. Aborting..."
		$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown');
		exit 1
	}
}
