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
# Version: PROTOTYPE ~ 2023/05/14
# Author: @chrisGrando
# ****************************************************************************************************

#### COMMAND LINE PARAMETERS FIELD ####

param (
	[Parameter(Mandatory=$true)]
	[string] $projectAbsolutePath
)

# Replace every "\" character by "/"
$projectAbsolutePath = $projectAbsolutePath -Replace '\\', '/'

#### VERSION CHECK FIELD ####

# Get Windows PowerShell current version
New-Variable -Name WPS_VERSION -Value ([double] "$($PSVersionTable.PSVersion.Major).$($PSVersionTable.PSVersion.Minor)") -Option Constant

# Check if PowerShell version is below 5.1 (and abort the script in case that is true)
if ($WPS_VERSION -lt 5.1) {
	Write-Error "[FATAL] Your version of Windows PowerShell ($WPS_VERSION) is incompatible. Please upgrade to version 5.1 or above."
	cmd /c pause
	exit 1
}

#### IMPORT FIELD ####

Import-Module -Name "$($projectAbsolutePath)scripts/windows/Export-Compress.psm1"

#### GLOBAL VARIABLES FIELD ####

# Constants
New-Variable -Name KEY_UP -Value 38 -Option Constant
New-Variable -Name KEY_DOWN -Value 40 -Option Constant
New-Variable -Name KEY_LEFT -Value 37 -Option Constant
New-Variable -Name KEY_RIGHT -Value 39 -Option Constant
New-Variable -Name KEY_SPACEBAR -Value 32 -Option Constant
New-Variable -Name KEY_Y -Value 89 -Option Constant
New-Variable -Name KEY_N -Value 78 -Option Constant

# NOT Constants
New-Variable -Name currentOption -Value 0
New-Variable -Name maxOptionNumber -Value 4
New-Variable -Name cursorLeft -Value ""
New-Variable -Name cursorRight -Value ""

#### FUNCTIONS FIELD ####

# Wait for the user to press [any key] to exit
function Invoke-WaitAndExit {
	Write-Host "`n*** END OF SCRIPT ***"
	Write-Host "Press any key to exit..."
	$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
	exit 0
}

# Execute Apache Maven to perform an "clean install"
function Invoke-BuildMavenProject {
	
}

# Erase Windows icon (.ico) file cache
function Invoke-ClearIconCache {
	$proceed = $false
	
	# Warn the user
	Write-Host "`n***********************************************************`n"
	Write-Host "[WARNING] The following operation may close all currently open windows."
	Write-Host "Do you wish to proceed? (Y / N)"
	
	# Loops until the user press "Y" or "N"
	while ($true) {
		# Keyboard input
		$key = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
		
		# User chose "yes"
		if ($key.VirtualKeyCode -eq $KEY_Y) {
			$proceed = $true
			break
		}
		
		# User chose "no"
		if ($key.VirtualKeyCode -eq $KEY_N) {
			break
		}
	}
	
	# Check if the user allowed script to continue
	if ($proceed) {
		Write-Host "`nPlease wait..."
		cmd /c call "$($projectAbsolutePath)scripts/windows/ResetIconCache.bat"
	}
}

# Export project and compress as 7z, zip or tar.xz
function Invoke-ExportAndCompress {
	Set-KeyMap $KEY_UP $KEY_DOWN $KEY_LEFT $KEY_RIGHT $KEY_SPACEBAR
	Set-AppFolder
	Set-AppVersion
	Invoke-MenuPlatforms
	Invoke-MenuCompression
	Start-Exporting $projectAbsolutePath "export"
}

#### MAIN CODE FIELD ####

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
	Write-Host "    Windows PowerShell Edition ~ PROTOTYPE - 2023/05/14"
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
	Write-Host " * [SPACEBAR] =====> Select option"
	
	# Manage keyboard input
	$key = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyUp')
	
	# Up or Left key
	if ($key.VirtualKeyCode -eq $KEY_UP -Or $key.VirtualKeyCode -eq $KEY_LEFT) {
		if ($currentOption -le 0) {
			$currentOption = $maxOptionNumber
		}
		$currentOption--
	}
	
	# Down or Right key
	if ($key.VirtualKeyCode -eq $KEY_DOWN -Or $key.VirtualKeyCode -eq $KEY_RIGHT) {
		if ($currentOption -ge ($maxOptionNumber - 1)) {
			$currentOption = -1
		}
		$currentOption++
	}
	
	# Spacebar key
	if ($key.VirtualKeyCode -eq $KEY_SPACEBAR) {
		break
	}
	
	# Clear all screen and restart loop
	clear
}

# An option was selected
switch ($currentOption) {
	# Export & Compress
	0 {
		Invoke-ExportAndCompress
		Invoke-WaitAndExit
	}
	# Clear icon cache
	1 {
		Invoke-ClearIconCache
		Invoke-WaitAndExit
	}
	# Build project
	2 {
		Invoke-BuildMavenProject
		Invoke-WaitAndExit
	}
	# Quit
	3 {
		Invoke-WaitAndExit
	}
	# Invalid
	default {
		Write-Error "`n[FATAL] Selected option is invalid. Aborting..."
		$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
		exit 1
	}
}
