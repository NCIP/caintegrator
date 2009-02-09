<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<s:form name="kaplanMeierQueryInputForm" id="kaplanMeierQueryInputForm" theme="simple">
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
          
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
        <div>
        <center>
        <button type="button" 
                onclick="document.kaplanMeierQueryInputForm.resetSelected.value = 'true';
                document.kaplanMeierQueryInputForm.action = 'resetQueryBasedKMPlot.action';
                document.kaplanMeierQueryInputForm.submit();"> Reset 
        </button>
        <s:if test="creatable">
        
            <button type="button" 
                    onclick="selectAllOptions(document.getElementById('allQueries'));
                    selectAllOptions(document.getElementById('querySelections'));
                    document.kaplanMeierQueryInputForm.createPlotSelected.value = 'true';
                    dojo.event.topic.publish('createQueryBasedKMPlot');"> Create Plot 
            </button>
            
        </s:if>
        </center>
        </div>
        
    <s:url id="createQueryBasedKMPlot" action="createQueryBasedKMPlot"/>
    
    <br><br>
    <center>
    <s:div id="queryKmPlotDiv" 
            theme="ajax" 
            href="%{createQueryBasedKMPlot}" 
            formId="kaplanMeierQueryInputForm" 
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createQueryBasedKMPlot" refreshOnShow="true" />
        
    </center>

    <!-- /Kaplan-Meier Inputs -->
    
</s:form>