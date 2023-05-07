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

#### MODULE MANIFEST FIELD ####

$moduleName = "Export-Compress"
$moduleVersion = "2023.05.07"
$moduleAuthor = "chrisGrando"
$moduleCompany = "Araucaria Projects"
$moduleDescription = "Module to export project binaries and compress then as 7z, zip or tar.xz."

New-ModuleManifest -Path "$PSScriptRoot\$moduleName.psd1" `
	-RootModule $moduleName `
	-ModuleVersion $moduleVersion `
	-Author $moduleAuthor `
	-CompanyName $moduleCompany `
	-Description $moduleDescription

#### GLOBAL VARIABLES FIELD ####

# Keyboard controls
$VK_UP = $null
$VK_DOWN = $null
$VK_LEFT = $null
$VK_RIGHT = $null
$VK_SPACEBAR = $null

# The game folder name with version
$GAME_FOLDER = "jSTG"
$GAME_VERSION = "1.0"

# Platforms to export
$SELECT_ALL = $false
$WINDOWS_x86 = $false
$WINDOWS_x64 = $false
$LINUX_i386 = $false
$LINUX_amd64 = $false
$JAR_ONLY = $false

# Compression option
New-Variable -Name COMPRESS_7Z -Value 0 -Option Constant
New-Variable -Name COMPRESS_ZIP -Value 1 -Option Constant
New-Variable -Name COMPRESS_ZIP_TAR_XZ -Value 2 -Option Constant
New-Variable -Name COMPRESS_NONE -Value 3 -Option Constant
$COMPRESS_OPTION = $null

#### FUNCTIONS FIELD ####

# Set controls to navigate the menus
function Set-KeyMap {
	[CmdletBinding()]
	param (
		[parameter(mandatory)] [int] $up,
		[parameter(mandatory)] [int] $down,
		[parameter(mandatory)] [int] $left,
		[parameter(mandatory)] [int] $right,
		[parameter(mandatory)] [int] $spacebar
	)
	
	# Set key map
	$VK_UP = $up
	$VK_DOWN = $down
	$VK_LEFT = $left
	$VK_RIGHT = $right
	$VK_SPACEBAR = $spacebar
}

# Set the game's (partial) folder name
function Set-GameFolder {
	
}

# Set the game version (used in game's final folder name)
function Set-GameVersion {
	
}

# Menu to select the platforms for exporting
function Invoke-MenuPlatforms {
	
}

# Menu to select the compression method
function Invoke-MenuCompression {
	
}

# Execute the exporting process
function Start-Exporting {
	
}

#### EXPORT FUNCTIONS FIELD ####

Export-ModuleMember -Function Set-KeyMap
Export-ModuleMember -Function Set-GameFolder
Export-ModuleMember -Function Set-GameVersion
Export-ModuleMember -Function Invoke-MenuPlatforms
Export-ModuleMember -Function Invoke-MenuCompression
Export-ModuleMember -Function Start-Exporting
