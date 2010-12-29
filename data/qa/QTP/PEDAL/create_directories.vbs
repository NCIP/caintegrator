'******************************************************
'************  PSEUDOCODE *****************************
'******************************************************

'Show window with title of Pedal project directory creator
' and display this text to the user:
' Enter a name to use for the project in Pedal.  It should
' have no spaces and none of the following characters specified in the "token" variable below
' and show a field for data entry.
' take the data and put it in variable user_entry
'
' check what is entered:
'  If the text entered has any invalid characters
'    return to prompt
'  otherwise,
'
'  create directory at <current directory>\data\user_entry
'  echo this
'  copy the data from <current directory>\data\Project_Name_With_No_Spaces into 
'		the directory at <current directory>\data\user_entry
'  echo this
'  create directory at <current directory>\tst\proj_object_repository\user_entry
'  echo this
'  create directory at <current directory>\results\user_entry
'  echo this
'
'  echo success!
'=========END OF PSEUDOCODE



'********************************************************************************
'*																				*
'*																				*
'*																				*
'*					Create directories batch program							*
'*						Version 1.0												*
'*					Author: Byron Katz, 10/8/2010								*
'*																				*
'*																				*
'********************************************************************************
'*************************PROGRAM BEGINS*****************************************

'****** VARIABLES *********
Dim repeat 'Flag to set whether we can exit the first Do While loop
				'To confirm that we have entered a satisfactory entry
Dim outMsg	'string that carries all the messages to be displayed 
				'at the end.
Dim token	'String that stores invalid characters

token = " \/:*?""<>`|'.:;{}()[]!@#$%^&*,"


'****** PROGRAM STARTS HERE *********
repeat = False
Do
	strInput = InputBox( "Enter a name to use for the project in Pedal." _
	& "It should have no spaces and none of the following " _
	& "characters: " & token, "PEDAL Project Directory Creator" _
	,"AppName")

	'Here we check if the user has clicked on CANCEL
	If strInput = "" Then
		WScript.quit -1
	End If

	For x=1 To Len(token)
		If InStr(strInput, Mid(token, x, 1)) Then
			MsgBox "You entered: " & strInput & " which has an illegal " & Mid(token, x, 1) & " in it"
			repeat = True
			Exit For
		End If
	Next
Loop Until repeat = False

'If you have gotten to this point, it means the user has entered a
'text that meets our criteria. 

'Here, we create sCurPath which is the path where this script is sitting.
'Everything is done relative to the location of the script.

Dim sCurPath
sCurPath = CreateObject("Scripting.FileSystemObject").GetAbsolutePathName(".")
Set objFSO = CreateObject("Scripting.FileSystemObject")
Dim DirectoryToBeAdded	'Stores the absolute path to the directory that will be created
Dim DirectoryToBeCopied	'Stores the absolute path to the direcotry that will be copied

'copy files To another folder
'Note: vbCrLf means to add a newline

Const OverWriteFiles = True
DirectoryToBeAdded = sCurPath & "\data\" & strInput
DirectoryToBeCopied = sCurPath & "\data\Project_Name_With_No_Spaces"


If objFSO.FolderExists(DirectoryToBeCopied) Then	'If this folder exists, proceed to do the copy.  Some people rename the folder... 
	If Not objFSO.FolderExists(DirectoryToBeAdded) Then		'First, copy over Project_Name_With_No_Spaces into a new directory
		objFSO.CopyFolder DirectoryToBeCopied, DirectoryToBeAdded, OverWriteFiles
		outMsg = outMsg _ 
			& vbCrLf _
			& "Just created " _
			& DirectoryToBeAdded _
			& vbCrLf _
			& "and copied in data from " _
			& DirectoryToBeCopied
	Else outMsg = outMsg & vbCrLf & "The directory at " & DirectoryToBeAdded & " already exists"
	End If
Else
	CreateDirectory (DirectoryToBeAdded)
	CreateDirectory (DirectoryToBeAdded & "\Batch")
	CreateDirectory (DirectoryToBeAdded & "\Templates")
End If

DirectoryToBeAdded = sCurPath & "\results\" & strInput
CreateDirectory (DirectoryToBeAdded)						'Now, create another directory in the results directory	
DirectoryToBeAdded = sCurPath & "\tst\proj_object_repository\" & strInput
CreateDirectory (DirectoryToBeAdded)	'Create another directory in the \tst\proj_object_repository directory
'Create the object repository with the correct name, this will not overwrite it if it already exists
Dim PathToDefaultOR
PathToDefaultOR = sCurPath & "\tst\proj_object_repository\Default.tsr"
If objFSO.FileExists (PathToDefaultOR) Then
	objFSO.CopyFile PathToDefaultOR, DirectoryToBeAdded & "\" & strInput & ".tsr" 
	outMsg = outMsg & vbCrLf & "Created object repository here: " & DirectoryToBeAdded & "\" & strInput & ".tsr"
Else
	outMsg = outMsg & vbCrLf & "FAIL - Create Object Repository failed because the default object repository was not located here: " & vbCrLf & PathToDefaultOR
End If


'*******Now, display the results to our user.
MsgBox outMsg, 0, "Results"


'These little function below checks to see whether we already have a 
'directory where we plan to add one.  If one already exists, it adds a
'string of error message to the output message string.  Otherwise, it
'adds and copies the directories as needed.
Function CreateDirectory (strPath)
		If Not objFSO.FolderExists(strPath) Then
			objFSO.CreateFolder strPath
			outMsg = outMsg & vbCrLf & "Created: " & strPath
		Else outMsg = outMsg & vbCrLf & "The directory at " & strPath & " already exists"
		End If
End Function