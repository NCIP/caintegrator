Dim  RepositoryFrom
Set  qtApp = getObject("","QuickTest.Application")
qtApp.WindowState = "Minimized"
Set qtApp = Nothing

 Set RepositoryFrom = CreateObject ("Mercury.ObjectRepositoryUtil")

RepositoryFrom.Load "L:\NCICB\QA\JJones\PEDAL_QTP\tst\proj_object_repository\TermBrowser\TermBrowser.tsr"


'AppUnderTest = inputbox ("Enter the application you are testing", "User Input","Mercury")
'TestName = inputbox ("Enter the test name", "User Input","template")
TestDir= Environment.Value("TestDir")
For K = 1 To 3
      pos = InstrRev(TestDir, "\", -1, 1)
	  TestDir = Left(TestDir, pos-1) 
Next

TestTable=TestDir&"\data\template.xls"
DataTable.Import (TestTable)
CurrentRow =1
DataTable.SetCurrentRow (CurrentRow)      
 

 Call EnumarateAllChildrenProperties(null, CurrentRow)
PopulatedTable=TestDir&"\data\TermBrowser\populatedTemplate.xls"
DataTable.ExportSheet PopulatedTable,1
 'RepositoryFrom.Save

' ******************************************************************************************

' The function enumerates recursively all the Test Objects under a given root. 

' For each Test Object it prompts a message box with its logical name, properties names and 

' values. 

Function EnumarateAllChildrenProperties(Root, CurrentRow)

 

    Dim TOCollection, TestObject, PropertiesCollection, Property, Msg

 
 
    Set TOCollection = RepositoryFrom.GetChildren(Root)

    For i = 0 To TOCollection.Count-1

        Set TestObject = TOCollection.Item(i)

        Msg = RepositoryFrom.GetLogicalName(TestObject) & vbNewLine

        Set PropertiesCollection = TestObject.GetTOProperties()

 

        For n = 0 To PropertiesCollection.Count-1

            Set Property = PropertiesCollection.Item(n)

            Msg = Msg & Property.Name & "-" & Property.Value & vbNewLine


		   
    		If Property.Name = "micclass" Then
				Select Case Property.Value
				   Case "Browser"
					   DataTable.GetSheet("Global").GetParameter("Application").Value=RepositoryFrom.GetLogicalName(TestObject)
					   DataTable.GetSheet("Global").GetParameter("ObjectType").Value=Property.Value
				   Case "Page"
					   DataTable.GetSheet("Global").GetParameter("Page").Value=RepositoryFrom.GetLogicalName(TestObject)
					   DataTable.GetSheet("Global").GetParameter("ObjectType").Value=Property.Value
				   Case "Frame"
					   DataTable.GetSheet("Global").GetParameter("Frame").Value=RepositoryFrom.GetLogicalName(TestObject)
					   DataTable.GetSheet("Global").GetParameter("ObjectType").Value=Property.Value
				   Case Else
					   DataTable.GetSheet("Global").GetParameter("ObjectNameOrKeyword").Value=RepositoryFrom.GetLogicalName(TestObject)
					   DataTable.GetSheet("Global").GetParameter("ObjectType").Value=Property.Value
					   CurrentRow = CurrentRow + 1
					    DataTable.SetCurrentRow (CurrentRow)      
 
			   End Select   	
		   End If


        Next

 '       MsgBox Msg

 

        EnumarateAllChildrenProperties TestObject, CurrentRow

    Next

End Function

 

 

 



 

  




















