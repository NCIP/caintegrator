Dim ST_Reporter
Const ForAppending = 8
Const TristateUseDefault = -2
Dim dicMetaDescription
Set ST_Reporter = New clST_Reporter
Dim CaseName,idx,ImgToCapturePath,TName,imgPathRef
Public ErrorFlag
Dim LnBr
LnBr = "<br />"
DataTable.AddSheet("Test Case List")
DataTable.GetSheet("Test Case List").AddParameter "TestCaseName","1"
'idx = DataTable.GetSheet("Test Case List").GetCurrentRow
'CaseName = DataTable.GetSheet("Test Case List").GetParameter("TestCaseName").ValueByRow(idx)




Public Function ST_CustomReporting (Data,status,CurrentRow,ResultsFileObj)


Select Case LCase(status)

   Case "pass"
		 	pass_msg="<strong>" & "Spreadsheet: " & "</strong>" & TestName & LnBr & "<strong>" & "Row: " & "</strong>" & CurrentRow+1 & LnBr &"<strong>"& "Expected value: " & "</strong>" & LnBr& "<DIV style=""color:#006400"">" & Data(4) & "</DIV>" & LnBr & "<strong>" & "Matched actual value: " & "</strong>" & LnBr & "<DIV style=""color:#006400"">" & Actual & "</DIV>"
			ST_Reporter.ReporterEvent micPass,pass_msg,"206", Data(13), ""
			'ST_Reporter.HTMLParser Data(13),pass_msg,"Passed","#006400" ,ImgToCapturePath               'Uncomment if you want report to display verifications that passed
   Case "fail"
			fail_msg= "<strong>" & "Spreadsheet: " & "</strong>" & TestName & LnBr & "<strong>" & "Spreadsheet Row: " & "</strong>" & CurrentRow+1 & LnBr & "<strong>" & "Expected value: " & "</strong>" & LnBr & "<DIV style=""color:#006400"">" & Data(4) & "</DIV>" & LnBr & "<strong>" & "Did not match actual value: " & "</strong>" & LnBr &  "<DIV style=""color:#DC143C"">"& Actual &"</DIV>"
			
			ResultsFileObj.WriteLine (CurrentRow+1&Chr(9)&"Expected: "&Data(4))
			ResultsFileObj.WriteLine (Chr(9)&"Actual  : "&Actual)
			ErrorFlag = True
		
			ImgToCapture = ST_Reporter.CaptureImgWHighlight(GUIObjRef,GUIObjRefParent)
									
			ST_Reporter.ReporterEvent micFail,fail_msg,"201","Failure - " & Data(13),ImgToCapture
			ST_Reporter.HTMLParser Data(13),fail_msg,"Failed","#FF0000",ImgToCapture
				If DataTable.GetSheet("Test Case List").GetRowCount >2 Then
					ST_Reporter.ST_BatchReport BatchDataFPFile
					ST_Reporter.ReporterEvent micFail,BatchDataFPFile,"201","Raise Exception",ImgToCapture
				End If
			

   Case Else
		Reporter.ReportEvent micFail, ReportStepName, "Custom Reporting error- Invalid pass/fail status!"

 End Select
		
End Function

Class clST_Reporter

Public Function ST_BatchReport(TCase)	
			 ST_BatchReport = TCase	
End Function
	
Public Function ReporterEvent(micStatus,msg,iCode,PTNodeName,imgPathPointer)
	imgPathRef = CStr(imgPathPointer)
		Set dicMetaDescription = CreateObject( "Scripting.Dictionary" )
		dicMetaDescription( "Status" ) = micStatus
		dicMetaDescription( "PlainTextNodeName" ) = PTNodeName
		dicMetaDescription( "StepHtmlInfo" ) = "<table align=center border = ""4"" frame = ""box"" cellpadding = ""5"" cellspacing = ""5"">"&_
			"<caption>  Event Log  </caption>" &_
			  "<thead><tr><th>"&Data(13)&"</th></tr></thead>"&_
				"<tbody><tr><td align=center>"&msg&"</td></tr></tbody>"&_
				"<tfoot><tr><td align=center><img border = ""2px"" src = "&imgPathRef&" /></td></tr></tfoot>"

		dicMetaDescription( "DllIconIndex" ) = iCode
		dicMetaDescription( "DllIconSelIndex" ) =iCode
		dicMetaDescription( "DllPAth" ) = Environment( "ProductDir" ) & "\bin\ContextManager.dll"
	Reporter.LogEvent "User", dicMetaDescription, Reporter.GetContext 
End Function


Public Function ConvertToHTML(fpath)
Dim HTMLConvert,HTMLReadyFile,ResHTMLFSO
Set ResHTMLFSO = CreateObject("Scripting.FileSystemObject")
	If (ResHTMLFSO.FileExists(fpath)) Then
		Set HTMLReadyFile = ResHTMLFSO.GetFile(fpath)
		Set HTMLConvert = HTMLReadyFile.OpenAsTextStream(ForAppending,TristateUseDefault)
		Set ConvertToHTML = HTMLConvert
	Else 
		ResHTMLFSO.CreateTextFile(fpath)
		Set HTMLReadyFile = ResHTMLFSO.GetFile(fpath)
		Set HTMLConvert = HTMLReadyFile.OpenAsTextStream(ForAppending,TristateUseDefault)
		Set ConvertToHTML = HTMLConvert
	End If
End Function

Public Function HTMLParser(data,msg,micStat,bgcolor,imgPathPointer)
   imgPathRef = CStr(imgPathPointer)
	HTMLConvert.WriteLine("<table align=""center"" border = ""4"" frame = ""box"" cellpadding = ""5"" cellspacing = ""5"">")
	HTMLConvert.WriteLine("<caption>  Event Log "&Time&" "&Date& " </caption>")
	HTMLConvert.WriteLine("<thead><tr><th>"&data&"</th></tr></thead>")
	HTMLConvert.WriteLine("<tr><td align=center>"&msg&"</td></tr>")
	HTMLConvert.WriteLine("<tr><td align=center><img border = ""2px"" src = "&imgPathRef&" /></td></tr>")
	HTMLConvert.WriteLine("<tfoot><tr><td style=""background-color:"&bgcolor&""" align=""center"">"&micStat&"</td></tr></tfoot>")
End Function

Public Function CaptureImgPathBuilder(Dir,TCName)

	posTC = InstrRev(TCName,".",-1,1)
	TName = Left(TCName,posTC-1)
	rTime = Replace(Time,":","-")
	tTime = Replace(rTime," ","")
	rDate = Replace(Date,"/","-")
	ImgToCapturePath =Dir&"\"&TName&"_"&tTime&"_"&rDate&".png"
	CaptureImgPathBuilder = ImgToCapturePath

End Function

Public Function CaptureImgWHighlight(obj,objParent)
	Dim ImgDir

	ImgDir = CStr(ST_Reporter.CaptureImgPathBuilder (ResDir,TestName))
	'print ImgDir
	Do While Not Eval(obj=objParent)
		Execute objParent&"Object.focus"
		Execute obj&"Object.focus"
		Execute obj&"Object.style.backgroundColor = ""#FFFF00"""
		Execute obj&"Object.style.borderWidth = ""5px"""
		Execute obj&"Object.style.borderColor = ""#FF0000"""
		Execute objParent&"Object.focus"
		Execute objParent&"CaptureBitmap ImgDir,False"
		CaptureImgWHighlight = ImgDir
		Exit Do
	Loop

	If Eval(obj=objParent) Then
		Execute obj&"Object.focus"
		Execute obj&"CaptureBitmap ImgDir,False"
		CaptureImgWHighlight = ImgDir
	End If

End Function

Function HTMLClose
	HTMLConvert.Close
End Function

End Class

Public Function CreateResultsFile(ResDir,TestName)
   Dim ResultsFSO, ResultsFile, ResultsFileObj
   Const ForReading = 1, ForWriting = 2, ForAppending = 8
   Const TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0
   
	Set ResultsFSO = CreateObject("Scripting.FileSystemObject")

	If (ResultsFSO.FileExists(ResDir&"\"&TestName&".txt")) Then
		Set ResultsFile = ResultsFSO.GetFile(ResDir&"\"&TestName&".txt")
		Set ResultsFileObj = ResultsFile.OpenAsTextStream(ForAppending, TristateUseDefault)
	Else
		ResultsFSO.CreateTextFile ResDir&"\"&TestName&".txt", False
		Set ResultsFile = ResultsFSO.GetFile(ResDir&"\"&TestName&".txt")
		Set ResultsFileObj = ResultsFile.OpenAsTextStream(ForAppending, TristateUseDefault)
	End If
 
   ResultsFileObj.WriteLine("Test: "&Testname)
   ResultsFileObj.WriteLine("Run on: "& Now)
   ResultsFileObj.WriteBlankLines(1) 
   ResultsFileObj.WriteLine ("Step"&Chr(9)&"Error") 
   ResultsFileObj.WriteLine ("----"&Chr(9)&"-----------------------------------------------------------------------") 
   Set CreateResultsFile = ResultsFileObj
End Function

'************************************************************************************************************************



'************************************************************************************************************************
Public Function CloseResultsFile(ResultsFileObj,ErrorFlag)
	If ErrorFlag <> True Then
	   ResultsFileObj.WriteLine(Chr(9)&"Test passed - No errors.")
	End If
	ResultsFileObj.WriteBlankLines(3) 
	ResultsFileObj.Close
End Function


