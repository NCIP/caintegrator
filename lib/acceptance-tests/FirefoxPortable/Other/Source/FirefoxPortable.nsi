;Copyright 2004-2007 John T. Haller
;Calendar and chrome.rdf procedures Copyright 2004 Gerard Balagué

;Website: http://PortableApps.com/PortableFirefox

;This software is OSI Certified Open Source Software.
;OSI Certified is a certification mark of the Open Source Initiative.

;This program is free software; you can redistribute it and/or
;modify it under the terms of the GNU General Public License
;as published by the Free Software Foundation; either version 2
;of the License, or (at your option) any later version.

;This program is distributed in the hope that it will be useful,
;but WITHOUT ANY WARRANTY; without even the implied warranty of
;MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;GNU General Public License for more details.

;You should have received a copy of the GNU General Public License
;along with this program; if not, write to the Free Software
;Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

!define PORTABLEAPPNAME "Mozilla Firefox, Portable Edition"
!define APPNAME "Firefox"
!define NAME "FirefoxPortable"
!define VER "1.5.9.0"
!define WEBSITE "PortableApps.com/FirefoxPortable"
!define DEFAULTEXE "firefox.exe"
!define DEFAULTAPPDIR "firefox"

;=== Program Details
Name "${PORTABLEAPPNAME}"
OutFile "..\..\${NAME}.exe"
Caption "${PORTABLEAPPNAME} | PortableApps.com"
VIProductVersion "${VER}"
VIAddVersionKey ProductName "${PORTABLEAPPNAME}"
VIAddVersionKey Comments "Allows ${APPNAME} to be run from a removable drive.  For additional details, visit ${WEBSITE}"
VIAddVersionKey CompanyName "PortableApps.com"
VIAddVersionKey LegalCopyright "John T. Haller, portions Gerard Balagué"
VIAddVersionKey FileDescription "${PORTABLEAPPNAME}"
VIAddVersionKey FileVersion "${VER}"
VIAddVersionKey ProductVersion "${VER}"
VIAddVersionKey InternalName "${PORTABLEAPPNAME}"
VIAddVersionKey LegalTrademarks "Firefox is a Trademark of The Mozilla Foundation.  PortableApps.com is a Trademark of Rare Ideas, LLC."
VIAddVersionKey OriginalFilename "${NAME}.exe"
;VIAddVersionKey PrivateBuild ""
;VIAddVersionKey SpecialBuild ""

;=== Runtime Switches
CRCCheck On
;WindowIcon Off
;SilentInstall Silent
AutoCloseWindow True
RequestExecutionLevel user

; Best Compression
SetCompress Auto
SetCompressor /SOLID lzma
SetCompressorDictSize 32
SetDatablockOptimize On

;=== Include
!include "Attrib.nsh"
!include "GetParameters.nsh"
!include "MUI.nsh"
!include "GetParent.nsh"
!include "Registry.nsh"
!include "ReplaceInFile.nsh"
!include "StrRep.nsh"
!include "TextFunc.nsh"
!insertmacro ConfigRead
!insertmacro ConfigWrite


;=== Program Icon
Icon "..\..\App\AppInfo\appicon.ico"

;=== Icon & Stye ===
!define MUI_ICON "..\..\App\AppInfo\appicon.ico"
BrandingText "PortableApps.com - Your Digital Life, Anywhere™"
MiscButtonText "" "" "" "Continue"
InstallButtonText "Continue"

;=== Pages
!define MUI_LICENSEPAGE_CHECKBOX
!insertmacro MUI_PAGE_LICENSE "EULA.rtf"
!insertmacro MUI_PAGE_INSTFILES

;=== Languages
!include "PortableApps.comLauncherLANG_ENGLISH.nsh"

;=== Variables
Var PROGRAMDIRECTORY
Var PROFILEDIRECTORY
Var SETTINGSDIRECTORY
Var PLUGINSDIRECTORY
Var ADDITIONALPARAMETERS
Var ALLOWMULTIPLEINSTANCES
Var SKIPCHROMEFIX
Var SKIPCOMPREGFIX
Var EXECSTRING
Var PROGRAMEXECUTABLE
Var INIPATH
Var ISFILELINE
Var DISABLESPLASHSCREEN
Var DISABLEINTELLIGENTSTART
Var LOCALHOMEPAGE
Var ISDEFAULTDIRECTORY
Var RUNLOCALLY
Var WAITFORPROGRAM
Var LASTPROFILEDIRECTORY
Var APPDATAPATH
Var SECONDARYLAUNCH
Var SHOWLICENSE
Var MOZILLAORGKEYEXISTS
Var MISSINGFILEORPATH
Var CRASHREPORTSDIREXISTS
Var EXTENSIONSDIREXISTS
Var LOCALTEMP

Function .onInit
 	Call GetParameters
	Pop $0
	StrCmp "$0" "SHOWLICENSE" ShowLicense LicenseDone

	ShowLicense:
		StrCpy $SHOWLICENSE "true"
		Goto onInitDone

	LicenseDone:
		SetSilent silent
		Goto onInitDone

	onInitDone:
FunctionEnd


Section "Main"
	;=== Setup variables
	ReadEnvStr $APPDATAPATH "APPDATA"
	StrCpy $LOCALTEMP $TEMP

	;=== Find the INI file, if there is one
		IfFileExists "$EXEDIR\${NAME}.ini" "" NoINI
			StrCpy "$INIPATH" "$EXEDIR"

		;=== Read the parameters from the INI file
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "${APPNAME}Directory"
		StrCpy $PROGRAMDIRECTORY "$EXEDIR\$0"
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "ProfileDirectory"
		StrCpy $PROFILEDIRECTORY "$EXEDIR\$0"
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "SettingsDirectory"
		StrCpy $SETTINGSDIRECTORY "$EXEDIR\$0"

		;=== Check that the above required parameters are present
		IfErrors NoINI

		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "PluginsDirectory"
		StrCpy $PLUGINSDIRECTORY "$EXEDIR\$0"
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "AdditionalParameters"
		StrCpy $ADDITIONALPARAMETERS $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "AllowMultipleInstances"
		StrCpy $ALLOWMULTIPLEINSTANCES $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "SkipChromeFix"
		StrCpy $SKIPCHROMEFIX $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "SkipCompregFix"
		StrCpy $SKIPCOMPREGFIX $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "${APPNAME}Executable"
		StrCpy $PROGRAMEXECUTABLE $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "WaitFor${APPNAME}"
		StrCpy $WAITFORPROGRAM $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "DisableSplashScreen"
		StrCpy $DISABLESPLASHSCREEN $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "DisableIntelligentStart"
		StrCpy $DISABLEINTELLIGENTSTART $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "LocalHomepage"
		StrCpy $LOCALHOMEPAGE $0
		ReadINIStr $0 "$INIPATH\${NAME}.ini" "${NAME}" "RunLocally"
		StrCpy $RUNLOCALLY $0
		StrCmp $RUNLOCALLY "true" "" CleanUpAnyErrors
		StrCpy $WAITFORPROGRAM "true"
		
	CleanUpAnyErrors:
		;=== Any missing unrequired INI entries will be an empty string, ignore associated errors
		ClearErrors

		;=== Check if default directories
		StrCmp $PROGRAMDIRECTORY "$EXEDIR\App\${DEFAULTAPPDIR}" "" EndINI
		StrCmp $PROFILEDIRECTORY "$EXEDIR\Data\profile" "" EndINI
		StrCmp $PLUGINSDIRECTORY "$EXEDIR\Data\plugins" "" EndINI
		StrCpy $SETTINGSDIRECTORY "$EXEDIR\Data\settings" "" EndINI
		StrCpy $ISDEFAULTDIRECTORY "true"
	
	EndINI:
		IfFileExists "$PROGRAMDIRECTORY\$PROGRAMEXECUTABLE" FoundProgramEXE NoProgramEXE

	NoINI:
		;=== No INI file, so we'll use the defaults
		StrCpy $ADDITIONALPARAMETERS ""
		StrCpy $ALLOWMULTIPLEINSTANCES "false"
		StrCpy $SKIPCHROMEFIX "false"
		StrCpy $SKIPCOMPREGFIX "false"
		StrCpy $WAITFORPROGRAM "false"
		StrCpy $PROGRAMEXECUTABLE "${DEFAULTEXE}"
		StrCpy $DISABLESPLASHSCREEN "false"
		StrCpy $DISABLEINTELLIGENTSTART "false"

		IfFileExists "$EXEDIR\App\${DEFAULTAPPDIR}\${DEFAULTEXE}" "" CheckPortableProgramDIR
			StrCpy $PROGRAMDIRECTORY "$EXEDIR\App\${DEFAULTAPPDIR}"
			StrCpy $PROFILEDIRECTORY "$EXEDIR\Data\profile"
			StrCpy $PLUGINSDIRECTORY "$EXEDIR\Data\plugins"
			StrCpy $SETTINGSDIRECTORY "$EXEDIR\Data\settings"
			StrCpy $ISDEFAULTDIRECTORY "true"
			Goto FoundProgramEXE
	
	CheckPortableProgramDIR:
			IfFileExists "$EXEDIR\${NAME}\App\${DEFAULTAPPDIR}\${DEFAULTEXE}" "" NoProgramEXE
			StrCpy $PROGRAMDIRECTORY "$EXEDIR\${NAME}\App\${DEFAULTAPPDIR}"
			StrCpy $PROFILEDIRECTORY "$EXEDIR\${NAME}\Data\profile"
			StrCpy $PLUGINSDIRECTORY "$EXEDIR\${NAME}\Data\plugins"
			StrCpy $SETTINGSDIRECTORY "$EXEDIR\${NAME}\Data\settings"
			Goto FoundProgramEXE

	NoProgramEXE:
		;=== Program executable not where expected
		StrCpy $MISSINGFILEORPATH $PROGRAMEXECUTABLE
		MessageBox MB_OK|MB_ICONEXCLAMATION `$(LauncherFileNotFound)`
		Abort
		
	FoundProgramEXE:
		IfFileExists "$APPDATA\Mozilla\Firefox\*.*" CheckForCrashReports
			StrCpy $WAITFORPROGRAM "true"
			${registry::KeyExists} "HKEY_CURRENT_USER\Software\mozilla.org" $R0
			StrCmp $R0 "-1" CheckForCrashReports ;=== If it doesn't exist, skip the next line
			StrCpy $MOZILLAORGKEYEXISTS "true"
			
	CheckForCrashReports:
		IfFileExists "$APPDATA\Mozilla\Firefox\Crash Reports\*.*" "" CheckForExtensionsDirectory
			StrCpy $CRASHREPORTSDIREXISTS "true"
			StrCpy $WAITFORPROGRAM "true"

	CheckForExtensionsDirectory:
		IfFileExists "$APPDATA\Mozilla\Extensions\*.*" "" CheckIfRunning
			StrCpy $EXTENSIONSDIREXISTS "true"
			StrCpy $WAITFORPROGRAM "true"
	
	CheckIfRunning:
		;=== Check if running
		StrCmp $ALLOWMULTIPLEINSTANCES "true" ProfileWork
		FindProcDLL::FindProc "firefox.exe"
		StrCmp $R0 "1" "" ProfileWork
			;=== Already running, check if it is using the portable profile
			IfFileExists "$PROFILEDIRECTORY\parent.lock" "" WarnAnotherInstance
				StrCpy $SECONDARYLAUNCH "true"
				Goto RunProgram
		
	WarnAnotherInstance:
		MessageBox MB_OK|MB_ICONINFORMATION `$(LauncherAlreadyRunning)`
		Abort
	
	ProfileWork:
	;=== Check for an existing profile
	IfFileExists "$PROFILEDIRECTORY\prefs.js" ProfileFound
		;=== No profile was found
		StrCmp $ISDEFAULTDIRECTORY "true" CopyDefaultProfile CreateProfile
	
	CopyDefaultProfile:
		CreateDirectory "$EXEDIR\Data"
		CreateDirectory "$EXEDIR\Data\plugins"
		CreateDirectory "$EXEDIR\Data\profile"
		CreateDirectory "$EXEDIR\Data\settings"
		CopyFiles /SILENT $EXEDIR\App\DefaultData\plugins\*.* $EXEDIR\Data\plugins
		CopyFiles /SILENT $EXEDIR\App\DefaultData\profile\*.* $EXEDIR\Data\profile
		GoTo ProfileFound
	
	CreateProfile:
		IfFileExists "$PROFILEDIRECTORY\*.*" ProfileFound
		CreateDirectory "$PROFILEDIRECTORY"

	ProfileFound:
		IfFileExists "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" SettingsFound
			CreateDirectory "$SETTINGSDIRECTORY"
			FileOpen $R0 "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" w
			FileClose $R0
			WriteINIStr "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "LastProfileDirectory" "NONE"
			
	SettingsFound:
		StrCmp $SHOWLICENSE "true" "" CheckForLicense
			WriteINIStr "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "AgreedToLicense" "YES"
			Goto TheEnd
	
	CheckForLicense:
		ReadINIStr $0 "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "AgreedToLicense"
		StrCmp $0 "YES" LicenseAgreedTo RelaunchWithLicense

	RelaunchWithLicense:
		System::Call 'kernel32::GetModuleFileNameA(i 0, t .R0, i 1024) i r1'
		ExecWait `$R0 SHOWLICENSE`
		ReadINIStr $0 "$SETTINGSDIRECTORY\FirefoxPortableSettings.ini" "FirefoxPortableSettings" "AgreedToLicense"
		StrCmp $0 "YES" LicenseAgreedTo TheEnd
	
	LicenseAgreedTo:
		;=== Check for read/write
		StrCmp $RUNLOCALLY "true" DisplaySplash
		ClearErrors
		FileOpen $R0 "$PROFILEDIRECTORY\writetest.temp" w
		IfErrors "" WriteSuccessful
			;== Write failed, so we're read-only
			MessageBox MB_YESNO|MB_ICONQUESTION `$(LauncherAskCopyLocal)` IDYES SwitchToRunLocally
			MessageBox MB_OK|MB_ICONINFORMATION `$(LauncherNoReadOnly)`
			Abort
			
	SwitchToRunLocally:
		StrCpy $RUNLOCALLY "true"
		Goto DisplaySplash
	
	WriteSuccessful:
		FileClose $R0
		Delete "$PROFILEDIRECTORY\writetest.temp"
	
	DisplaySplash:
		StrCmp $DISABLESPLASHSCREEN "true" SkipSplashScreen
			;=== Show the splash screen before processing the files
			InitPluginsDir
			File /oname=$PLUGINSDIR\splash.jpg "${NAME}.jpg"
			newadvsplash::show /NOUNLOAD 2000 0 0 -1 /L $PLUGINSDIR\splash.jpg

	SkipSplashScreen:
		;=== Run locally if needed (aka Portable Firefox Live)
		StrCmp $RUNLOCALLY "true" "" CompareProfilePath
		RMDir /r "$LOCALTEMP\${NAME}\"
		CreateDirectory $LOCALTEMP\${NAME}\profile
		CreateDirectory $LOCALTEMP\${NAME}\plugins
		CreateDirectory $LOCALTEMP\${NAME}\program
		CopyFiles /SILENT $PROFILEDIRECTORY\*.* $LOCALTEMP\${NAME}\profile
		StrCpy $PROFILEDIRECTORY $LOCALTEMP\${NAME}\profile
		CopyFiles /SILENT $PLUGINSDIRECTORY\*.* $LOCALTEMP\${NAME}\plugins
		StrCpy $PROFILEDIRECTORY $LOCALTEMP\${NAME}\profile
		CopyFiles /SILENT $PROGRAMDIRECTORY\*.* $LOCALTEMP\${NAME}\program
		StrCpy $PROGRAMDIRECTORY $LOCALTEMP\${NAME}\program
		Push $LOCALTEMP\${NAME}
		Call Attrib

	CompareProfilePath:
		ReadINIStr $LASTPROFILEDIRECTORY "$SETTINGSDIRECTORY\${NAME}Settings.ini" "${NAME}Settings" "LastProfileDirectory"
		StrCmp $PROFILEDIRECTORY $LASTPROFILEDIRECTORY "" AdjustChrome
			StrCmp $DISABLEINTELLIGENTSTART "true" AdjustChrome
				StrCpy $SKIPCHROMEFIX "true"
				StrCpy $SKIPCOMPREGFIX "true"
	
	AdjustChrome:
		WriteINIStr "$SETTINGSDIRECTORY\${NAME}Settings.ini" "${NAME}Settings" "LastProfileDirectory" "$PROFILEDIRECTORY"
		
		;=== Adjust the chrome.rdf
		StrCmp $SKIPCHROMEFIX "true" FixCalendarManager
		IfFileExists "$PROFILEDIRECTORY\chrome\chrome.rdf" "" FixCalendarManager
		FileOpen $0 "$PROFILEDIRECTORY\chrome\chrome.rdf" r
		FileOpen $R0 "$PROFILEDIRECTORY\chrome\chrome.rdf.new" w
		ClearErrors ; if there's an error, we're done with the file
	
		NextLine:
			FileWrite $R0 $4
			FileRead $0 $4
			IfErrors NoMoreLines ;== we've reached the end of the file
			StrCpy $5 $4 35
			StrCmp $5 `                   c:baseURL="jar:f` FoundJarLine
			StrCmp $5 `                   c:baseURL="file:` FoundFileLine NextLine

		FoundJarLine:
			StrCpy $R4 40
			StrCpy $ISFILELINE "0"
			GoTo NotYet

		FoundFileLine:
			StrCpy $R4 40
			StrCpy $ISFILELINE "1"

		NotYet:
			IntOp $R4 $R4 + 1
			StrCpy $7 $4 10 $R4 ;=== looking for the point to strip the extension path
			StrCmp $7 "extensions" PathFound NotYet
	
		PathFound:
			StrCpy $5 $4 "" $R4
			StrCmp $ISFILELINE "0" MakeJarLine MakeFileLine

		MakeJarLine:
			StrCpy $4 `                   c:baseURL="jar:file:///$PROFILEDIRECTORY/$5`
			GoTo NextLine

		MakeFileLine:
			StrCpy $4 `                   c:baseURL="file:///$PROFILEDIRECTORY/$5`
			GoTo NextLine

		NoMoreLines:
			FileClose $0
			FileClose $R0

			;=== Backup the chrome.rdf
			CopyFiles /SILENT "$PROFILEDIRECTORY\chrome\chrome.rdf" "$PROFILEDIRECTORY\chrome\chrome.rdf.old"
			CopyFiles /SILENT "$PROFILEDIRECTORY\chrome\chrome.rdf.new" "$PROFILEDIRECTORY\chrome\chrome.rdf" 

	FixCalendarManager:
		IfFileExists "$PROFILEDIRECTORY\Calendar\CalendarManager.rdf" "" FixPrefsJs

		FileOpen $0 "$PROFILEDIRECTORY\Calendar\CalendarManager.rdf" r
		FileOpen $R0 "$PROFILEDIRECTORY\Calendar\CalendarManager.rdf.new" w
		ClearErrors ; if there's an error, we're done with the file
	
		CMNextLine:
			FileWrite $R0 $4
			FileRead $0 $4
			IfErrors CMNoMoreLines ;== we've reached the end of the file
			StrCpy $5 $4 28
			StrCmp $5 `                   NC:path="` "" CMNextLine
			StrCpy $R4 28
	
		CMNotYet:
			IntOp $R4 $R4 + 1
			StrCpy $7 $4 9 $R4 ; looking for the point to strip the calendar path
			StrCmp $7 "Calendar\" "" CMNotYet
			StrCpy $5 $4 "" $R4
			StrCpy $4 `                   NC:path="$PROFILEDIRECTORY\$5` ;=== the adjusted extension path
			GoTo CMNextLine

		CMNoMoreLines:
			FileClose $0
			FileClose $R0

			;=== Backup the Calendar\CalendarManager.rdf just in case
			CopyFiles /SILENT "$PROFILEDIRECTORY\Calendar\CalendarManager.rdf" "$PROFILEDIRECTORY\Calendar\CalendarManager.rdf.old"
			CopyFiles /SILENT "$PROFILEDIRECTORY\Calendar\CalendarManager.rdf.new" "$PROFILEDIRECTORY\Calendar\CalendarManager.rdf" 

	FixPrefsJs:
		IfFileExists "$PROFILEDIRECTORY\prefs.js" "" FixMimeTypes
		StrCmp $LASTPROFILEDIRECTORY "NONE" FixPrefsJsPart2
		StrCpy $2 $LASTPROFILEDIRECTORY 1 ;Last drive letter
		StrCpy $3 $PROFILEDIRECTORY 1 ;Current drive letter
		StrCmp $2 $3 FixPrefsJsPart2 ;If no change, move on
		
		;=== Fix any local bookmarks
		${ConfigRead} "$PROFILEDIRECTORY\prefs.js" `user_pref("browser.startup.homepage", "` $0
		${StrReplace} $1 'file:///$2' 'file:///$3' $0
		${ConfigWrite} "$PROFILEDIRECTORY\prefs.js" `user_pref("browser.startup.homepage", "` $1 $R0
		
		;=== Replace drive letter entries elsewhere
		${ReplaceInFile} "$PROFILEDIRECTORY\prefs.js" `", "$2:\\` `", "$3:\\`
		Delete "$PROFILEDIRECTORY\prefs.js.old"
	
	FixPrefsJsPart2:
		;=== Be sure the default browser check is disabled
		FileOpen $0 "$PROFILEDIRECTORY\prefs.js" a
		FileSeek $0 0 END
		FileWriteByte $0 "13"
		FileWriteByte $0 "10"
		FileWrite $0 `user_pref("browser.shell.checkDefaultBrowser", false);`
		FileWriteByte $0 "13"
		FileWriteByte $0 "10"
		StrCmp "$LOCALHOMEPAGE" "" FixPrefsJsClose
		FileWrite $0 `user_pref("browser.startup.homepage", "file:///$EXEDIR/$LOCALHOMEPAGE");`
		FileWriteByte $0 "13"
		FileWriteByte $0 "10"
	
	FixPrefsJsClose:
		FileClose $0 
	
	FixMimeTypes:
		IfFileExists "$PROFILEDIRECTORY\mimeTypes.rdf" "" RunProgram
		StrCmp $LASTPROFILEDIRECTORY "NONE" RunProgram
		Push $LASTPROFILEDIRECTORY
		Call GetParent
		Call GetParent
		Call GetParent
		Pop $0 ;Last PortableApps Directory
		StrCpy $0 '$0\'
		Push $PROFILEDIRECTORY
		Call GetParent
		Call GetParent
		Call GetParent
		Pop $1 ;Current PortableApps Directory
		StrCpy $1 '$1\'
		StrCmp $0 $1 RunProgram
		${ReplaceInFile} "$PROFILEDIRECTORY\mimeTypes.rdf" $0 $1
		Delete "$PROFILEDIRECTORY\mimeTypes.rdf.old"
	
	RunProgram:
		StrCmp $SKIPCOMPREGFIX "true" GetPassedParameters

		;=== Delete component registry to ensure compatibility with all extensions
		Delete $PROFILEDIRECTORY\compreg.dat

	GetPassedParameters:
		;=== Get any passed parameters
		Call GetParameters
		Pop $0
		StrCmp "'$0'" "''" "" LaunchProgramParameters

		;=== No parameters
		StrCpy $EXECSTRING `"$PROGRAMDIRECTORY\$PROGRAMEXECUTABLE" -profile "$PROFILEDIRECTORY"`
		Goto CheckMultipleInstances

	LaunchProgramParameters:
		StrCpy $EXECSTRING `"$PROGRAMDIRECTORY\$PROGRAMEXECUTABLE" -profile "$PROFILEDIRECTORY" $0`

	CheckMultipleInstances:
		StrCmp $ALLOWMULTIPLEINSTANCES "true" "" AdditionalParameters
		System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i("MOZ_NO_REMOTE", "1").r0'

	AdditionalParameters:
		StrCmp $ADDITIONALPARAMETERS "" PluginsEnvironment

		;=== Additional Parameters
		StrCpy $EXECSTRING `$EXECSTRING $ADDITIONALPARAMETERS`

	PluginsEnvironment:
		;=== Set the plugins directory if we have a path
		StrCmp $PLUGINSDIRECTORY "" LaunchNow
		IfFileExists "$PLUGINSDIRECTORY\*.*" "" LaunchNow
		System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i("MOZ_PLUGIN_PATH", "$PLUGINSDIRECTORY").r0'

	LaunchNow:
		StrCmp $SECONDARYLAUNCH "true" StartProgramAndExit
		StrCmp $WAITFORPROGRAM "true" "" StartProgramAndExit
		ExecWait $EXECSTRING

	CheckRunning:
		Sleep 2000
		StrCmp $ALLOWMULTIPLEINSTANCES "true" CheckIfRemoveLocalFiles
		FindProcDLL::FindProc "firefox.exe"                  
		StrCmp $R0 "1" CheckRunning CleanupRunLocally
	
	StartProgramAndExit:
		Exec $EXECSTRING
		Goto TheEnd
	
	CleanupRunLocally:
		StrCmp $RUNLOCALLY "true" "" CheckIfRemoveLocalFiles
		RMDir /r "$LOCALTEMP\${NAME}\"

	CheckIfRemoveLocalFiles:
		FindProcDLL::FindProc "firefox.exe"
		Pop $R0
		StrCmp $R0 "1" TheEnd RemoveLocalFiles

	RemoveLocalFiles:
		StrCmp $CRASHREPORTSDIREXISTS "true" RemoveLocalFiles2
		RMDir /r "$APPDATA\Mozilla\Firefox\Crash Reports\"
		
	RemoveLocalFiles2:
		StrCmp $EXTENSIONSDIREXISTS "true" RemoveLocalFiles3
		RMDir /r "$APPDATA\Mozilla\Extensions\"
		
	RemoveLocalFiles3:
		Delete "$APPDATA\Mozilla\Firefox\pluginreg.dat"
		RMDir "$APPDATA\Mozilla\Firefox\" ;=== Will only delete if empty (no /r switch)
		RMDir "$APPDATA\Mozilla\" ;=== Will only delete if empty (no /r switch)
		StrCmp $MOZILLAORGKEYEXISTS "true" TheEnd
			${registry::DeleteKey} "HKEY_CURRENT_USER\Software\mozilla.org" $R0

	TheEnd:
		${registry::Unload}
		newadvsplash::stop /WAIT
SectionEnd