<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<s:form name="kaplanMeierQueryInputForm" id="kaplanMeierQueryInputForm" theme="simple">
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    <!-- Kaplan-Meier Inputs -->
    <h1>Query Based Kaplan-Meier Survival Plots</h1>

        <table class="data">
            <tr>
                <td class="value_inline">
                    1.) Select Queries:
                </td>
                <td class="value_inline">
                    <s:optiontransferselect
                    id="allQueries"
                    doubleId="querySelections"
                    name="kmPlotForm.queryBasedForm.unselectedQueryIDs"
                    list="kmPlotForm.queryBasedForm.unselectedQueries"
                    listValue="value.name"
                    doubleName="kmPlotForm.queryBasedForm.selectedQueryIDs"
                    doubleList="kmPlotForm.queryBasedForm.selectedQueries"
                    doubleListValue="value.name"
                    allowAddAllToLeft="false"
                    allowAddAllToRight="false"
                    allowUpDownOnLeft="false"
                    allowUpDownOnRight="true"
                    leftTitle="All Available Queries"
                    rightTitle="Query Groups"
                    addToRightLabel=" Add >"
                    addToLeftLabel=" < Remove "
                    allowSelectAll="false"
                    size="8"
                    doubleSize="8"
                    multiple="true"
                    doubleMultiple="true"
                    cssStyle="min-width:100px; vertical-align=middle;"
                    buttonCssStyle="min-width:100px;"
                    />
		        </td>
	        </tr>
        </table>
        <table class="data">
	        <tr>
		        <td class="value_inline">
                    2.) <s:checkbox name="kmPlotForm.queryBasedForm.exclusiveGroups"/>Exclusive Subjects in Queries (Subjects in upper queries are removed from subsequent queries) 
                </td> 
            </tr>
            <tr>
                <td class="value_inline">
                    3.) <s:checkbox name="kmPlotForm.queryBasedForm.addPatientsNotInQueriesGroup"/>Add additional group containing all other subjects not found in queries.
                </td>
            </tr>
        </table>
        <table class="data">
            <tr>
	            <th/>
	            <th> Survival Value </th>
            </tr>
            <tr>
                <td class="value_inline">
                    4.) Select Survival Measure:
                </td>
                <td class="value_inline">
                <s:select name="kmPlotForm.survivalValueDefinitionId" 
                      list="kmPlotForm.survivalValueDefinitions" 
                      listValue="value.name"
                      theme="simple"/>
                </td>
            </tr>
        </table>
        <br>
        <center>
        <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.kaplanMeierQueryInputForm.action = 'resetQueryBasedKMPlot.action';document.kaplanMeierQueryInputForm.submit();"><span class="btn_img">Reset</span></s:a>
        <s:if test="creatable">
	        <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" 
	               onclick="selectAllOptions(document.getElementById('allQueries'));
	                        selectAllOptions(document.getElementById('querySelections'));
	                        document.kaplanMeierQueryInputForm.action = 'createQueryBasedKMPlot.action';
	                        document.kaplanMeierQueryInputForm.submit();"><span class="btn_img">Create Plot</span></s:a>
        </s:if>
        </center>

    <!-- /Kaplan-Meier Inputs -->
    
    <!-- Kaplan-Meier Graph -->
    <s:set name="kmPlot" value="#session['kmPlot']" />
    <s:if test="#kmPlot.queryBasedKmPlot != null">
        <br>
        <center>
            <img src="retrieveQueryKMPlot.action"/>
            <br>
            
            <strong>Log-rank P-value for significance of difference in survival between groups</strong>
            <br>
            <table cellspacing="10">
            
            <!-- Outter Map -->
            <s:iterator value="allStringPValues">
                <s:set name="group1Name" value="key"/>
                <!-- Innter Map -->
                <s:iterator value="value">
                    <s:set name="group2Name" value="key"/>
                    <tr>
                        <td align="right"><s:property value="#group1Name"/></td>
                        <td> vs. </td>
                        <td align="left"> <s:property value="#group2Name"/> </td>
                        <td> = </td> 
                        <td> <s:property value="value"/> </td>
                    </tr>
                </s:iterator> <!-- End Innter Map -->
            </s:iterator> <!-- End Outter Map -->
            </table>
        </center>
    </s:if>
    <!-- /Kaplan-Meier Graph -->
    
</s:form>