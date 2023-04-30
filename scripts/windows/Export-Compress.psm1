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
$moduleVersion = "2023.04.29"
$moduleAuthor = "chrisGrando"
$moduleDescription = "Module to export project binaries and compress then as 7z, zip or tar.xz."

New-ModuleManifest -Path "$PSScriptRoot\$moduleName.psd1" `
	-ModuleVersion $moduleVersion `
	-Author $moduleAuthor `
	-Description $moduleDescription

#### GLOBAL VARIABLES FIELD ####

$VK_UP = $null
$VK_DOWN = $null
$VK_LEFT = $null
$VK_RIGHT = $null
$VK_SPACEBAR = $null

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

#### EXPORT FUNCTIONS FIELD ####

Export-ModuleMember -Function Set-KeyMap
