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
$moduleName = "Build-Maven"
$moduleVersion = "2023.05.15"
$moduleAuthor = "chrisGrando"
$moduleCompany = "Araucaria Projects"
$moduleDescription = "Module to use Apache Maven build tools."

New-ModuleManifest -Path "$modulePath/$moduleName.psd1" `
	-RootModule $moduleName `
	-ModuleVersion $moduleVersion `
	-Author $moduleAuthor `
	-CompanyName $moduleCompany `
	-Description $moduleDescription

#### GLOBAL VARIABLES FIELD ####

# Build tools
New-Variable -Name JAVAC -Value $false
New-Variable -Name MVN -Value $false

#### FUNCTIONS FIELD ####

# Check if Java SDK (a.k.a. JDK) is present on the system
function Set-IsJavacAvaliable {
	if (Get-Command "javac" -ErrorAction SilentlyContinue) {
		Set-Variable -Name JAVAC -Value $true -Scope "Script"
	}
}

# Check if Apache Maven is present on the system
function Set-IsMavenAvaliable {
	if (Get-Command "mvn" -ErrorAction SilentlyContinue) {
		Set-Variable -Name MVN -Value $true -Scope "Script"
	}
}

# Build the project with "clean install"
function Start-CleanInstall {
	[CmdletBinding()]
	param (
		[parameter(mandatory)] [string] $rootFolder,
		[parameter(mandatory)] [int] $VK_Y,
		[parameter(mandatory)] [int] $VK_N
	)
	
	$proceed = $false
	
	# Abort if Java SDK (javac) isn't installed
	if (!$JAVAC) {
		clear
		Write-Error "[FATAL] Unable to find Java SDK. Aborting..."
		cmd /c pause
		exit 1
	}
	
	# Abort if Apache Maven (mvn) isn't installed
	if (!$MVN) {
		clear
		Write-Error "[FATAL] Unable to find Apache Maven. Aborting..."
		cmd /c pause
		exit 1
	}
	
	# Warn the user
	Write-Host "`n***********************************************************`n"
	Write-Host "[WARNING] The following operation will perform a `"clean install`" on the project."
	Write-Host "Do you wish to proceed? (Y / N)"
	
	# Loops until the user press "Y" or "N"
	while ($true) {
		# Keyboard input
		$key = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
		
		# User chose "yes"
		if ($key.VirtualKeyCode -eq $VK_Y) {
			$proceed = $true
			break
		}
		
		# User chose "no"
		if ($key.VirtualKeyCode -eq $VK_N) {
			break
		}
	}
	
	# Perform clean install only if user has authorized
	if ($proceed) {
		clear
		cd $rootFolder
		Start-Process -NoNewWindow -Wait -FilePath "mvn" -ArgumentList "-v"
		Start-Process -NoNewWindow -Wait -FilePath "mvn" -ArgumentList "clean", "install"
	}
}

#### EXPORT FUNCTIONS FIELD ####

Export-ModuleMember -Function Set-IsJavacAvaliable
Export-ModuleMember -Function Set-IsMavenAvaliable
Export-ModuleMember -Function Start-CleanInstall
