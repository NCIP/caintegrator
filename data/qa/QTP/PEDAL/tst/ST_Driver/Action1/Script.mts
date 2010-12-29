'#####################################################################################
'#		PEDAL - Portable Event-Driven Automation Library
'#		SpeedTest, Inc. copyright 2005, 2009
'#		If you have any questions or need support, contact :
'#				SpeedTest Inc.
'#				7017 Eden Mill Rd  Woodbine, MD 21797
'#				410-627-7373, info@speedtestinc.com
'#
'#      This code is the property of SpeedTest Inc. and is conveyed to as a component
'#      of a services contract, without restrictions on its use by that client.
'#      Use by anyone other than client personnel in support of the project for which SpeedTest is contracted
'#      is a copyright infringement.
'#      Client bears sole responsibility for the use and implementation of this code.  It is conveyed by 
'#      SpeedTest without license, guarantee, warranty or support beyond the services contract period.
'#####################################################################################

'#####################################################################################		
'#		The following is the driver script for an automated test framework
'#		It uses an event-driven approach to read data from a data table and 
'#		execute the test based upon that data.
'#		It works in conjunction with the following function libraries which are assoaciated with this test
'#		ST_ObjectFunctionLibrary
'#      ProjectFunctionLibrary
'#####################################################################################
Dim TestName, TestDir, ProjectDir, ResDir
Dim AppUnderTest, BackDirLevel, pos, TestTable, CurrentRow, ErrorFlag, ResultsFileObj
Dim MainBrowser, ObjType, CurrentPage, Object
Dim Data(13)
Dim TestCaseArr()
Dim posOne,posTwo,FLibDir,FLibPointer,InitDir
DataTable.AddSheet("Test Case List")
FLibDir = Environment("TestDir")
posOne = InstrRev(FLibDir,"\",-1,1)
FLibPointer = Left(FLibDir,posOne-1)
posTwo = InstrRev(FLibPointer,"\",-1,1)
InitDir = Left(FLibPointer,posTwo-1)
ExecuteFile FLibPointer &"\BatchReport.vbs"
ExecuteFile FLibPointer &"\proj_func_lib\ProjectFunctionLibrary.vbs"
ExecuteFile FLibPointer &"\ST_obj_func_lib\ST_ObjectFunctionLibrary_GenCommand.vbs"
ExecuteFile FLibPointer &"\ST_keyword_func_lib\ST_KeywordFunctionLibrary.vbs"
ExecuteFile FLibPointer &"\ST_util_lib\ST_UtilityLibrary.vbs"
ErrorFlag=False

		msgbox("Use the following dialog to select the Test Case or Batch Test data file for the test(s) you want to run...")
		'create an instance of the File Browser
		Set ObjFSO = CreateObject("UserAccounts.CommonDialog")
		ObjFSO.InitialDir = InitDir & "\data"
		InitFSO = ObjFSO.ShowOpen
		TestDataFPFile = ObjFSO.FileName 
		'TestEnv = inputbox ("Enter the test environment (ie. QA1 or QA2)", "User Input","QA2")
		'Environment.Value("TestEnv")=TestEnv
		TestURL = inputbox ("Enter the URL for the environment you want to test against.", "User Input","http://")
		Environment.Value("TestURL")=TestURL
		'check to see if a batch file was selected
		FilePathArr = split (TestDataFPFile, "\")
		cntFilePathArr = UBound (FilePathArr,1)
		For i = 1 to cntFilePathArr
			If Lcase(FilePathArr(i)) = "batch" Then
				BatchDataFPFile = TestDataFPFile
			End If
		Next
'End If

'Build array of test cases.  For a single test the array will contain one element.  For a batch test it will contain multiple elements
If BatchDataFPFile = ""  Then
		FilePathArr = split (TestDataFPFile, "\")
		cntFilePathArr = UBound (FilePathArr,1)
		TestName = FilePathArr(cntFilePathArr)
		DataTable.Value("TestCaseName","Test Case List")=TestName
		DataTable.GetSheet("Test Case List").SetCurrentRow(2)
		DataTable.Value("TestCaseName","Test Case List") = "end of batch"
		AppUnderTest = FilePathArr(cntFilePathArr-1)
Else
	FilePathArr = split (BatchDataFPFile, "\")
	cntFilePathArr = UBound (FilePathArr,1)
	TestName = FilePathArr(cntFilePathArr)
	AppUnderTest = FilePathArr(cntFilePathArr-2)
	DataTable.ImportSheet BatchDataFPFile , 1, "Test Case List"
End If

' Start For loop with index TesttCaseNum here which sets TestDataFPFile to TestCaseArr(i)
idxTableRow = 1
Do 


'Do ' loops through the Test Case Arr to perform all tests.  for a single test there will be one test in the array
		'Config settings - all are set relative to the selectionof teh data file - relies on specific folder structure'Define relative paths PEDAL folder structure
		TestDir= Environment("TestDir")
		For BackDirLevel = 1 To 2
			  pos = InstrRev(TestDir, "\", -1, 1)
			  TestDir = Left(TestDir, pos-1) 
		Next
		DataTable.GetSheet("Test Case List").SetCurrentRow idxTableRow
		If  DataTable.Value("TestCaseName","Test Case List") = "end of batch"Then
			exittest
		End If
		
		ProjectDir=TestDir&"\data\"&AppUnderTest&"\"
		ResDir=TestDir&"\results\"&AppUnderTest
		TestName = DataTable.GetSheet("Test Case List").GetParameter("TestCaseName").ValueByRow(idxTableRow)
		HTMLpath = ResDir&"\"&TestName&".html"
		Set HTMLConvert = ST_Reporter.ConvertToHTML(HTMLPath)
		TestTable=ProjectDir &TestName
		
		'Load Object Shared Repository
		RepPath = TestDir&"\tst\proj_object_repository\"&AppUnderTest&"\"&AppUnderTest&".tsr" 
		RepositoriesCollection.RemoveAll() 
		RepositoriesCollection.Add(RepPath)
		RepositoriesCollection.Add(TestDir&"\tst\proj_object_repository\LowLevel.tsr") 'support key stroke entry if needed by non-standard objects

		'Set save location of  QTP results file


		'Set location of text results file for exception report.
		Set ResultsFileObj = CreateResultsFile(ResDir,TestName)
		
		DataTable.ImportSheet TestTable , 1, "Global"
		CurrentRow =1
		MercuryTimers.Timer("TotalTestCaseTime").Start 'Initialize the timer to capture the total test execution time 
		Do' loops through the test case data spreadsheet to perfrom the test
		
			DataTable.SetCurrentRow (CurrentRow)      
			If DataTable("Application", dtGlobalSheet) <> "" Then
				Data(0) = DataTable("Application", dtGlobalSheet)
				If Data(0) = "end of test" Then
					'exittest
					IE.Quit 'Public browser object declared in Proj Func Lib
					Exit Do
				Else
					If lcase(DataTable("debug", dtGlobalSheet) ) <> "useexistingbrowser" And lcase(DataTable("ObjPar1", dtGlobalSheet) ) <> "useexistingbrowser"Then
						'StartApp(Data)
						StartAppURL(Data)
					End If
				End If   	
		   End If
			If DataTable("Page", dtGlobalSheet) <> "" Then
				Data(1) = DataTable("Page", dtGlobalSheet)
			End If
			Data(2) = DataTable("Frame", dtGlobalSheet)
			Data(3) = DataTable("Action", dtGlobalSheet)
			Data(4) = DataTable("Value", dtGlobalSheet)
			Data(5) = DataTable("ObjectNameOrKeyword", dtGlobalSheet)
			Data(6) = DataTable("ObjectType", dtGlobalSheet)
			Data(7) = DataTable("ObjPar1", dtGlobalSheet)
			Data(8) = DataTable("ObjPar2", dtGlobalSheet)
			Data(9) = DataTable("SynchPar1", dtGlobalSheet)
			Data(10) = DataTable("SynchPar2", dtGlobalSheet)
			Data(11) = DataTable("comment", dtGlobalSheet)
			Data(12) = DataTable("debug", dtGlobalSheet)
			Data(13) = DataTable("Field", dtGlobalSheet)

			'For debugging a test can be stopped at any row in the spreadsheet by entering anything in the debug column
			If Data(12) = Lcase("pause") Then
				Set  qtApp = getObject("","QuickTest.Application")
				Set qtTest = qtApp.Test
				Response = msgbox ("Your spreadsheet contains a pause for debugging.  Press OK to pause or Cancel to continue execution.", 1)
				If Response = vbOK Then
						qtTest.Pause
						MercuryTimers.Timer("TotalTestCaseTime").Stop 
				End If
				If Response = vbCancel Then
						'For step-by-step debugging, enter "pause" inthe debug column at the row where you wantto stop, place a break point one the following line of code.
						'Click Cancel in response to the msgbox coded above.  Step-by-step execution can be started from the follwoing line of code.
						qtTest.Continue
				End If
				Set qtApp = Nothing
				Set qtTest = Nothing
			End If

			If (Lcase(Data(3))<> "none") and (Lcase(Data(3))<> "skip") and Data(11)<> "//"Then
				status=ST_ObjectHandler (Data,CurrentRow,ResultsFileObj)
				If status= "Fail" Then
					ErrorFlag = True
				End If
			End If
			CurrentRow = CurrentRow + 1
		
		Loop Until Data(0) = "end of test"
		Reporter.ReportEvent micInfo, "Test Case elapsed Time", "TestCaseElapsedTime: " & MercuryTimers("TotalTestCaseTime").Stop() /1000 & "seconds"
	idxTableRow = idxTableRow + 1	
	Loop

CloseResultsFile ResultsFileObj,ErrorFlag
Set  qtApp = Nothing








































