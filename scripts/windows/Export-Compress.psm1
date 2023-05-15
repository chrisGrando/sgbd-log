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
$moduleVersion = "2023.05.15"
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

# The app folder name with version
New-Variable -Name APP_FOLDER -Value "sgbd-log"
New-Variable -Name APP_VERSION -Value "1.0"

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

# Set the app's (partial) folder name
function Set-AppFolder {
	clear
	Write-Host "Please enter the app folder name or leave it blank (default: $APP_FOLDER)."
	Write-Host "(Tip: Use something short and without spaces)"
	Write-Host "Examples: sgbd-log, sgbd, app."
	Write-Host "***********************************************************"
	$input = Read-Host ">"
	$input = ($APP_FOLDER,$input)[[bool]$input] # Use default folder name if nothing was informed
	Set-Variable -Name APP_FOLDER -Value $input -Scope "Script"
}

# Set the app version (used in app's final folder name)
function Set-AppVersion {
	clear
	Write-Host "Please enter the app version or leave it blank (default: $APP_VERSION)."
	Write-Host "Examples: 1.0, 1.1.2a, 0.1-DEMO, 0.3.1-TRIAL"
	Write-Host "***********************************************************"
	$input = Read-Host ">"
	$input = ($APP_VERSION,$input)[[bool]$input] # Use default value if nothing was informed
	Set-Variable -Name APP_VERSION -Value $input -Scope "Script"
}

# Menu to select the platforms for exporting
function Invoke-MenuPlatforms {
	$currentOption = 0
	$maxOptionNumber = 6
	$cursorLeft = ""
	$cursorRight = ""
	$checked = ""
	
	# Menu loop
	while ($true) {
		clear
		Write-Host "-----------------------------------------------------------"
		Write-Host "Select the platforms you wish to export your project for:"
		Write-Host "-----------------------------------------------------------"
		
		# Menu options + cursors
	
		# 1) Select all platforms
		if ($currentOption -eq 0) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		if ($SELECT_ALL) {
			$checked = "X"
		}
		else {
			$checked = " "
		}
		
		$option_1 = "$($cursorLeft)[$($checked)]$($cursorRight) Select all platforms"
		
		# 2) Windows x86
		if ($currentOption -eq 1) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		if ($WINDOWS_x86) {
			$checked = "X"
		}
		else {
			$checked = " "
		}
		
		$option_2 = "$($cursorLeft)[$($checked)]$($cursorRight) Windows x86"
		
		# 3) Windows x64
		if ($currentOption -eq 2) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		if ($WINDOWS_x64) {
			$checked = "X"
		}
		else {
			$checked = " "
		}
		
		$option_3 = "$($cursorLeft)[$($checked)]$($cursorRight) Windows x64"
		
		# 4) Linux i386
		if ($currentOption -eq 3) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		if ($LINUX_i386) {
			$checked = "X"
		}
		else {
			$checked = " "
		}
		
		$option_4 = "$($cursorLeft)[$($checked)]$($cursorRight) Linux i386"
		
		# 5) Linux amd64
		if ($currentOption -eq 4) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		if ($LINUX_amd64) {
			$checked = "X"
		}
		else {
			$checked = " "
		}
		
		$option_5 = "$($cursorLeft)[$($checked)]$($cursorRight) Linux amd64"
		
		# 6) Jar file only
		if ($currentOption -eq 5) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		if ($JAR_ONLY) {
			$checked = "X"
		}
		else {
			$checked = " "
		}
		
		$option_6 = "$($cursorLeft)[$($checked)]$($cursorRight) Jar file only"
		
		# Render options text
		Write-Host $option_1
		Write-Host $option_2
		Write-Host $option_3
		Write-Host $option_4
		Write-Host $option_5
		Write-Host $option_6
		Write-Host "-----------------------------------------------------------"
		Write-Host "CONTROLS:"
		Write-Host " * [Arrow UP] and [Arrow DOWN] ======> Move cursor"
		Write-Host " * [Arrow LEFT] and [Arrow RIGHT] ===> Toggle ON / OFF"
		Write-Host " * [SPACEBAR] =======================> Proceed"
		
		# Manage keyboard input
		$key = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyUp')
	
		# Up key
		if ($key.VirtualKeyCode -eq $VK_UP) {
			if ($currentOption -le 0) {
				$currentOption = $maxOptionNumber
			}
			$currentOption--
		}
		
		# Down key
		if ($key.VirtualKeyCode -eq $VK_DOWN) {
			if ($currentOption -ge ($maxOptionNumber - 1)) {
				$currentOption = -1
			}
			$currentOption++
		}
		
		# Left key
		if ($key.VirtualKeyCode -eq $VK_LEFT) {
			# Disable associated flags
			switch ($currentOption) {
				0 {
					Set-Variable -Name SELECT_ALL -Value $false -Scope "Script"
					Set-Variable -Name WINDOWS_x86 -Value $false -Scope "Script"
					Set-Variable -Name WINDOWS_x64 -Value $false -Scope "Script"
					Set-Variable -Name LINUX_i386 -Value $false -Scope "Script"
					Set-Variable -Name LINUX_amd64 -Value $false -Scope "Script"
					Set-Variable -Name JAR_ONLY -Value $false -Scope "Script"
				}
				1 {
					Set-Variable -Name SELECT_ALL -Value $false -Scope "Script"
					Set-Variable -Name WINDOWS_x86 -Value $false -Scope "Script"
				}
				2 {
					Set-Variable -Name SELECT_ALL -Value $false -Scope "Script"
					Set-Variable -Name WINDOWS_x64 -Value $false -Scope "Script"
				}
				3 {
					Set-Variable -Name SELECT_ALL -Value $false -Scope "Script"
					Set-Variable -Name LINUX_i386 -Value $false -Scope "Script"
				}
				4 {
					Set-Variable -Name SELECT_ALL -Value $false -Scope "Script"
					Set-Variable -Name LINUX_amd64 -Value $false -Scope "Script"
				}
				5 {
					Set-Variable -Name SELECT_ALL -Value $false -Scope "Script"
					Set-Variable -Name JAR_ONLY -Value $false -Scope "Script"
				}
			}
		}
		
		# Right key
		if ($key.VirtualKeyCode -eq $VK_RIGHT) {
			# Enable associated flags
			switch ($currentOption) {
				0 {
					Set-Variable -Name SELECT_ALL -Value $true -Scope "Script"
					Set-Variable -Name WINDOWS_x86 -Value $true -Scope "Script"
					Set-Variable -Name WINDOWS_x64 -Value $true -Scope "Script"
					Set-Variable -Name LINUX_i386 -Value $true -Scope "Script"
					Set-Variable -Name LINUX_amd64 -Value $true -Scope "Script"
					Set-Variable -Name JAR_ONLY -Value $true -Scope "Script"
				}
				1 {
					Set-Variable -Name WINDOWS_x86 -Value $true -Scope "Script"
				}
				2 {
					Set-Variable -Name WINDOWS_x64 -Value $true -Scope "Script"
				}
				3 {
					Set-Variable -Name LINUX_i386 -Value $true -Scope "Script"
				}
				4 {
					Set-Variable -Name LINUX_amd64 -Value $true -Scope "Script"
				}
				5 {
					Set-Variable -Name JAR_ONLY -Value $true -Scope "Script"
				}
			}
			
			# Check if all but the first option are checked
			if (!$SELECT_ALL -And $WINDOWS_x86 -And $WINDOWS_x64 -And $LINUX_i386 -And $LINUX_amd64 -And $JAR_ONLY) {
				# Set the first option to 'true' as well
				Set-Variable -Name SELECT_ALL -Value $true -Scope "Script"
			}
		}
		
		# Spacebar key
		if ($key.VirtualKeyCode -eq $VK_SPACEBAR) {
			break
		}
	}
}

# Menu to select the compression method
function Invoke-MenuCompression {
	$currentOption = 0
	$maxOptionNumber = 4
	$cursorLeft = ""
	$cursorRight = ""
	
	# Menu loop
	while ($true) {
		clear
		Write-Host "-----------------------------------------------------------"
		Write-Host "Select an compression method to package the binaries:"
		Write-Host "-----------------------------------------------------------"
		
		# Menu options + cursor
		
		# 1) Everything as 7z
		if ($currentOption -eq 0) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		$option_1 = "$($cursorLeft)[1]$($cursorRight) Everything as 7z (recommended)"
		
		# 2) Everything as zip
		if ($currentOption -eq 1) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		$option_2 = "$($cursorLeft)[2]$($cursorRight) Everything as zip"
		
		# 3) Jar and Windows builds as zip, everything else as tar.xz
		if ($currentOption -eq 2) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		$option_3 = "$($cursorLeft)[3]$($cursorRight) Jar and Windows builds as zip, everything else as tar.xz"
		
		# 4) Don't compress anything
		if ($currentOption -eq 3) {
			$cursorLeft = "=>"
			$cursorRight = "<="
		}
		else {
			$cursorLeft = "  "
			$cursorRight = "  "
		}
		
		$option_4 = "$($cursorLeft)[4]$($cursorRight) Don't compress anything"
		
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
		if ($key.VirtualKeyCode -eq $VK_UP -Or $key.VirtualKeyCode -eq $VK_LEFT) {
			if ($currentOption -le 0) {
				$currentOption = $maxOptionNumber
			}
			$currentOption--
		}
		
		# Down or Right key
		if ($key.VirtualKeyCode -eq $VK_DOWN -Or $key.VirtualKeyCode -eq $VK_RIGHT) {
			if ($currentOption -ge ($maxOptionNumber - 1)) {
				$currentOption = -1
			}
			$currentOption++
		}
		
		# Spacebar key
		if ($key.VirtualKeyCode -eq $VK_SPACEBAR) {
			Set-Variable -Name COMPRESS_OPTION -Value $currentOption -Scope "Script"
			break
		}
	}
}

# Execute the exporting process
function Start-Exporting {
	[CmdletBinding()]
	param (
		[parameter(mandatory)] [string] $rootFolder,
		[parameter(mandatory)] [string] $exportFolderName
	)
	
	# Abort if no platforms were selected
	if (!$WINDOWS_x86 -And !$WINDOWS_x64 -And !$LINUX_i386 -And !$LINUX_amd64 -And !$JAR_ONLY) {
		clear
		Write-Host "[WARNING] No platforms were selected. Aborting..."
		Write-Host "-----------------------------------------------------------"
		return
	}
	
	# Abort if "target" folder doesn't exist or is empty
	if (-Not (Test-Path "$($rootFolder)target/*.*")) {
		clear
		Write-Error "[FATAL] Target folder doesn't exist or is empty. Aborting..."
		cmd /c pause
		exit 1
	}
	
	# The path where the exported files goes to
	$exportPath = "$($rootFolder)$($exportFolderName)/"
	$appNameWithVersion = "$($APP_FOLDER)_v$($APP_VERSION)"
	
	# The path to 7z and it's files
	$7z_path = "$($rootFolder)7z/windows/7zr.exe"
	$jre_path = "$($rootFolder)7z/files/"
	
	# Start exporting process
	clear
	Write-Host "STARTING THE EXPORTATION PROCESS..."
	Write-Host "-----------------------------------------------------------"
	
	# Delete exporting folder if it already exists
	if (Test-Path -Path $exportPath) {
		Remove-Item -LiteralPath $exportPath -Force -Recurse
	}
	
	# Create exporting folder
	Write-Host "`nCreating export folder..."
	New-Item -Path $exportPath -ItemType Directory
	
	# 32-bit Windows
	if ($WINDOWS_x86) {
		$finalAppFolderName = "$($appNameWithVersion)_Windows_x86"
		$currentBuildPath = "$($exportPath)$($finalAppFolderName)/"
		
		# Create build folder
		Write-Host "`nCreating $finalAppFolderName build folder..."
		New-Item -Path $currentBuildPath -ItemType Directory
		
		# Copy files to the build folder
		Write-Host "`nCopying files to the $finalAppFolderName folder...`n"
		
		if (Test-Path "$($rootFolder)target/*.jar") {
			Copy-Item -Path "$($rootFolder)target/*" -Destination $currentBuildPath -Filter "*.jar"
			Write-Host "* Jar file [OK]"
		}
		
		if (Test-Path "$($rootFolder)target/*-x32.exe") {
			Copy-Item -Path "$($rootFolder)target/*" -Destination $currentBuildPath -Filter "*-x32.exe"
			Write-Host "* Executable file [OK]"
		}
		
		if (Test-Path "$($rootFolder)database/*.*") {
			New-Item -Path "$($currentBuildPath)database/" -ItemType Directory | Out-Null
			Copy-Item -Path "$($rootFolder)database/*" -Destination "$($currentBuildPath)database/" -Exclude "*.pdf"
			Write-Host "* Database folder [OK]"
		}
		
		if (Test-Path "$($rootFolder)README.md") {
			Copy-Item -Path "$($rootFolder)*" -Destination $currentBuildPath -Filter "README.md"
			Write-Host "* README file [OK]"
		}
		
		# Extract JRE
		Write-Host "`nExtracting JRE contents..."
		cd $currentBuildPath
		Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "x", "-y", "$($jre_path)JRE17_Windows_x86.7z"
		
		# Package everything in the build folder
		Write-Host "`nPackaging the $finalAppFolderName folder contents..."
		cd $exportPath
		
		switch ($COMPRESS_OPTION) {
			$COMPRESS_7Z {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).7z", "$finalAppFolderName"
			}
			$COMPRESS_ZIP {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).zip", "$finalAppFolderName"
			}
			$COMPRESS_ZIP_TAR_XZ {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "-tzip", "$($finalAppFolderName)", "$finalAppFolderName"
			}
			default {
				Write-Host "Compression is disabled. Skipping..."
			}
		}
		
		Write-Host "-----------------------------------------------------------"
	}
	
	# 64-bit Windows
	if ($WINDOWS_x64) {
		$finalAppFolderName = "$($appNameWithVersion)_Windows_x64"
		$currentBuildPath = "$($exportPath)$($finalAppFolderName)/"
		
		# Create build folder
		Write-Host "`nCreating $finalAppFolderName build folder..."
		New-Item -Path $currentBuildPath -ItemType Directory
		
		# Copy files to the build folder
		Write-Host "`nCopying files to the $finalAppFolderName folder...`n"
		
		if (Test-Path "$($rootFolder)target/*.jar") {
			Copy-Item -Path "$($rootFolder)target/*" -Destination $currentBuildPath -Filter "*.jar"
			Write-Host "* Jar file [OK]"
		}
		
		if (Test-Path "$($rootFolder)target/*-x64.exe") {
			Copy-Item -Path "$($rootFolder)target/*" -Destination $currentBuildPath -Filter "*-x64.exe"
			Write-Host "* Executable file [OK]"
		}
		
		if (Test-Path "$($rootFolder)database/*.*") {
			New-Item -Path "$($currentBuildPath)database/" -ItemType Directory | Out-Null
			Copy-Item -Path "$($rootFolder)database/*" -Destination "$($currentBuildPath)database/" -Exclude "*.pdf"
			Write-Host "* Database folder [OK]"
		}
		
		if (Test-Path "$($rootFolder)README.md") {
			Copy-Item -Path "$($rootFolder)*" -Destination $currentBuildPath -Filter "README.md"
			Write-Host "* README file [OK]"
		}
		
		# Extract JRE
		Write-Host "`nExtracting JRE contents..."
		cd $currentBuildPath
		Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "x", "-y", "$($jre_path)JRE17_Windows_x64.7z"
		
		# Package everything in the build folder
		Write-Host "`nPackaging the $finalAppFolderName folder contents..."
		cd $exportPath
		
		switch ($COMPRESS_OPTION) {
			$COMPRESS_7Z {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).7z", "$finalAppFolderName"
			}
			$COMPRESS_ZIP {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).zip", "$finalAppFolderName"
			}
			$COMPRESS_ZIP_TAR_XZ {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "-tzip", "$($finalAppFolderName)", "$finalAppFolderName"
			}
			default {
				Write-Host "Compression is disabled. Skipping..."
			}
		}
		
		Write-Host "-----------------------------------------------------------"
	}
	
	# 32-bit Linux
	if ($LINUX_i386) {
		$finalAppFolderName = "$($appNameWithVersion)_Linux_i386"
		$currentBuildPath = "$($exportPath)$($finalAppFolderName)/"
		
		# Create build folder
		Write-Host "`nCreating $finalAppFolderName build folder..."
		New-Item -Path $currentBuildPath -ItemType Directory
		
		# Copy files to the build folder
		Write-Host "`nCopying files to the $finalAppFolderName folder...`n"
		
		if (Test-Path "$($rootFolder)target/*.jar") {
			Copy-Item -Path "$($rootFolder)target/*" -Destination $currentBuildPath -Filter "*.jar"
			Write-Host "* Jar file [OK]"
		}
		
		if (Test-Path "$($rootFolder)scripts/linux/GenJAL.sh") {
			Copy-Item -Path "$($rootFolder)scripts/linux/*" -Destination $currentBuildPath -Filter "GenJAL.sh"
			Write-Host "* Executable file [OK]"
		}
		
		if (Test-Path "$($rootFolder)database/*.*") {
			New-Item -Path "$($currentBuildPath)database/" -ItemType Directory | Out-Null
			Copy-Item -Path "$($rootFolder)database/*" -Destination "$($currentBuildPath)database/" -Exclude "*.pdf"
			Write-Host "* Database folder [OK]"
		}
		
		if (Test-Path "$($rootFolder)README.md") {
			Copy-Item -Path "$($rootFolder)*" -Destination $currentBuildPath -Filter "README.md"
			Write-Host "* README file [OK]"
		}
		
		# Extract JRE
		Write-Host "`nExtracting JRE contents..."
		cd $currentBuildPath
		Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "x", "-y", "$($jre_path)JRE17_Linux_i386.7z"
		
		# Package everything in the build folder
		Write-Host "`nPackaging the $finalAppFolderName folder contents..."
		cd $exportPath
		
		switch ($COMPRESS_OPTION) {
			$COMPRESS_7Z {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).7z", "$finalAppFolderName"
			}
			$COMPRESS_ZIP {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).zip", "$finalAppFolderName"
			}
			$COMPRESS_ZIP_TAR_XZ {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "-ttar", "$($finalAppFolderName).tar.xz", "$finalAppFolderName"
			}
			default {
				Write-Host "Compression is disabled. Skipping..."
			}
		}
		
		Write-Host "-----------------------------------------------------------"
	}
	
	# 64-bit Linux
	if ($LINUX_amd64) {
		$finalAppFolderName = "$($appNameWithVersion)_Linux_amd64"
		$currentBuildPath = "$($exportPath)$($finalAppFolderName)/"
		
		# Create build folder
		Write-Host "`nCreating $finalAppFolderName build folder..."
		New-Item -Path $currentBuildPath -ItemType Directory
		
		# Copy files to the build folder
		Write-Host "`nCopying files to the $finalAppFolderName folder...`n"
		
		if (Test-Path "$($rootFolder)target/*.jar") {
			Copy-Item -Path "$($rootFolder)target/*" -Destination $currentBuildPath -Filter "*.jar"
			Write-Host "* Jar file [OK]"
		}
		
		if (Test-Path "$($rootFolder)scripts/linux/GenJAL.sh") {
			Copy-Item -Path "$($rootFolder)scripts/linux/*" -Destination $currentBuildPath -Filter "GenJAL.sh"
			Write-Host "* Executable file [OK]"
		}
		
		if (Test-Path "$($rootFolder)database/*.*") {
			New-Item -Path "$($currentBuildPath)database/" -ItemType Directory | Out-Null
			Copy-Item -Path "$($rootFolder)database/*" -Destination "$($currentBuildPath)database/" -Exclude "*.pdf"
			Write-Host "* Database folder [OK]"
		}
		
		if (Test-Path "$($rootFolder)README.md") {
			Copy-Item -Path "$($rootFolder)*" -Destination $currentBuildPath -Filter "README.md"
			Write-Host "* README file [OK]"
		}
		
		# Extract JRE
		Write-Host "`nExtracting JRE contents..."
		cd $currentBuildPath
		Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "x", "-y", "$($jre_path)JRE17_Linux_amd64.7z"
		
		# Package everything in the build folder
		Write-Host "`nPackaging the $finalAppFolderName folder contents..."
		cd $exportPath
		
		switch ($COMPRESS_OPTION) {
			$COMPRESS_7Z {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).7z", "$finalAppFolderName"
			}
			$COMPRESS_ZIP {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).zip", "$finalAppFolderName"
			}
			$COMPRESS_ZIP_TAR_XZ {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "-ttar", "$($finalAppFolderName).tar.xz", "$finalAppFolderName"
			}
			default {
				Write-Host "Compression is disabled. Skipping..."
			}
		}
		
		Write-Host "-----------------------------------------------------------"
	}
	
	# Universal JAR file
	if ($JAR_ONLY) {
		$finalAppFolderName = "$($appNameWithVersion)_Jar"
		$currentBuildPath = "$($exportPath)$($finalAppFolderName)/"
		
		# Create build folder
		Write-Host "`nCreating $finalAppFolderName build folder..."
		New-Item -Path $currentBuildPath -ItemType Directory
		
		# Copy files to the build folder
		Write-Host "`nCopying files to the $finalAppFolderName folder...`n"
		
		if (Test-Path "$($rootFolder)target/*.jar") {
			Copy-Item -Path "$($rootFolder)target/*" -Destination $currentBuildPath -Filter "*.jar"
			Write-Host "* Jar file [OK]"
		}
		
		if (Test-Path "$($rootFolder)database/*.*") {
			New-Item -Path "$($currentBuildPath)database/" -ItemType Directory | Out-Null
			Copy-Item -Path "$($rootFolder)database/*" -Destination "$($currentBuildPath)database/" -Exclude "*.pdf"
			Write-Host "* Database folder [OK]"
		}
		
		if (Test-Path "$($rootFolder)README.md") {
			Copy-Item -Path "$($rootFolder)*" -Destination $currentBuildPath -Filter "README.md"
			Write-Host "* README file [OK]"
		}
		
		# Package everything in the build folder
		Write-Host "`nPackaging the $finalAppFolderName folder contents..."
		cd $exportPath
		
		switch ($COMPRESS_OPTION) {
			$COMPRESS_7Z {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).7z", "$finalAppFolderName"
			}
			$COMPRESS_ZIP {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "$($finalAppFolderName).zip", "$finalAppFolderName"
			}
			$COMPRESS_ZIP_TAR_XZ {
				Start-Process -NoNewWindow -Wait -FilePath $7z_path -ArgumentList "a", "-y", "-sdel", "-mx9", "-tzip", "$($finalAppFolderName)", "$finalAppFolderName"
			}
			default {
				Write-Host "Compression is disabled. Skipping..."
			}
		}
		
		Write-Host "-----------------------------------------------------------"
	}
}

#### EXPORT FUNCTIONS FIELD ####

Export-ModuleMember -Function Set-KeyMap
Export-ModuleMember -Function Set-AppFolder
Export-ModuleMember -Function Set-AppVersion
Export-ModuleMember -Function Invoke-MenuPlatforms
Export-ModuleMember -Function Invoke-MenuCompression
Export-ModuleMember -Function Start-Exporting
