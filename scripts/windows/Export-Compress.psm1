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

$modulePath = $PSScriptRoot -replace '\\', '/'
$moduleName = "Export-Compress"
$moduleVersion = "2023.05.09"
$moduleAuthor = "chrisGrando"
$moduleCompany = "Araucaria Projects"
$moduleDescription = "Module to export project binaries and compress then as 7z, zip or tar.xz."

New-ModuleManifest -Path "$modulePath/$moduleName.psd1" `
	-RootModule $moduleName `
	-ModuleVersion $moduleVersion `
	-Author $moduleAuthor `
	-CompanyName $moduleCompany `
	-Description $moduleDescription

#### GLOBAL VARIABLES FIELD ####

# Keyboard controls
New-Variable -Name VK_UP -Value $null
New-Variable -Name VK_DOWN -Value $null
New-Variable -Name VK_LEFT -Value $null
New-Variable -Name VK_RIGHT -Value $null
New-Variable -Name VK_SPACEBAR -Value $null

# The game folder name with version
New-Variable -Name GAME_FOLDER -Value "jSTG"
New-Variable -Name GAME_VERSION -Value "1.0"

# Platforms to export
New-Variable -Name SELECT_ALL -Value $false
New-Variable -Name WINDOWS_x86 -Value $false
New-Variable -Name WINDOWS_x64 -Value $false
New-Variable -Name LINUX_i386 -Value $false
New-Variable -Name LINUX_amd64 -Value $false
New-Variable -Name JAR_ONLY -Value $false

# Compression option
New-Variable -Name COMPRESS_7Z -Value 0 -Option Constant
New-Variable -Name COMPRESS_ZIP -Value 1 -Option Constant
New-Variable -Name COMPRESS_ZIP_TAR_XZ -Value 2 -Option Constant
New-Variable -Name COMPRESS_NONE -Value 3 -Option Constant
New-Variable -Name COMPRESS_OPTION -Value $null

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
	Set-Variable -Name VK_UP -Value $up -Scope "Script"
	Set-Variable -Name VK_DOWN -Value $down -Scope "Script"
	Set-Variable -Name VK_LEFT -Value $left -Scope "Script"
	Set-Variable -Name VK_RIGHT -Value $right -Scope "Script"
	Set-Variable -Name VK_SPACEBAR -Value $spacebar -Scope "Script"
}

# Set the game's (partial) folder name
function Set-GameFolder {
	clear
	Write-Host "Please enter the game folder name."
	Write-Host "(Tip: Use something short and without spaces)"
	Write-Host "Examples: TouHou_GemumeiNoRei, TH_GameNameExample, TH_GNE."
	Write-Host "***********************************************************"
	$input = Read-Host ">"
	Set-Variable -Name GAME_FOLDER -Value $input -Scope "Script"
}

# Set the game version (used in game's final folder name)
function Set-GameVersion {
	clear
	Write-Host "Please enter the game version."
	Write-Host "Examples: 1.0, 1.1.2a, 0.1-DEMO, 0.3.1-TRIAL"
	Write-Host "***********************************************************"
	$input = Read-Host ">"
	Set-Variable -Name GAME_VERSION -Value $input -Scope "Script"
}

# Menu to select the platforms for exporting
function Invoke-MenuPlatforms {
	
}

# Menu to select the compression method
function Invoke-MenuCompression {
	
}

# Execute the exporting process
function Start-Exporting {
	[CmdletBinding()]
	param (
		[parameter(mandatory)] [string] $rootFolder,
		[parameter(mandatory)] [string] $exportFolderName
	)
	
	# The path where the exported files goes to
	$exportPath = "$($rootFolder)$($exportFolderName)"
	$finalGameFolder = "$($GAME_FOLDER)_v$($GAME_VERSION)"
	
	# TODO: Remove debug info and implement actual function code
	clear
	Write-Host "Export path: $exportPath"
	Write-Host "Final game folder: $finalGameFolder"
}

#### EXPORT FUNCTIONS FIELD ####

Export-ModuleMember -Function Set-KeyMap
Export-ModuleMember -Function Set-GameFolder
Export-ModuleMember -Function Set-GameVersion
Export-ModuleMember -Function Invoke-MenuPlatforms
Export-ModuleMember -Function Invoke-MenuCompression
Export-ModuleMember -Function Start-Exporting
