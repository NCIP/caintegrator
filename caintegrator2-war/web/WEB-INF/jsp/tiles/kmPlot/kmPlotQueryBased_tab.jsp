<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<s:form name="kaplanMeierQueryInputForm" id="kaplanMeierQueryInputForm" theme="simple">
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
          
    <!-- Kaplan-Meier Inputs -->
    <h2>Query Based Kaplan-Meier Survival Plots</h2>

        <table class="data">
            <tr>
                <td class="value_inline_index" >
                    1.) 
                </td>
                <td class="value_inline">
                    Select Queries:
                </td>
                <td class="optiontransferselect">
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
                    rightTitle="Selected Queries"
                    rightUpLabel="    ^    "
                    rightDownLabel="    v    "
                    addToRightLabel=" Add >"
                    addToLeftLabel=" < Remove "
                    allowSelectAll="false"
                    size="8"
                    doubleSize="8"
                    multiple="true"
                    doubleMultiple="true"
                    cssStyle="min-width:200px; vertical-align=middle; font-weight:bold; color: #475B82; background-color: #E9E9E9;"
                    doubleCssStyle="min-width:200px; vertical-align=middle; font-weight:bold; color: #475B82; background-color: #E9E9E9;"
                    buttonCssStyle="min-width:100px; vertical-align=middle;"
                    />
		        </td>
	        </tr>
	        <tr>
                <td class="value_inline_index" >
                    2.) 
                </td>
		        <td colspan="2" class="value_inline">
                    <s:checkbox name="kmPlotForm.queryBasedForm.exclusiveGroups"/>Exclusive Subjects in Queries (Subjects in upper queries are removed from subsequent queries) 
                </td> 
            </tr>
            <tr>
                <td class="value_inline_index" >
                    3.)
                </td>
                <td colspan="2" class="value_inline">
                    <s:checkbox name="kmPlotForm.queryBasedForm.addPatientsNotInQueriesGroup"/>Add additional group containing all other subjects not found in selected queries.
                </td>
            </tr>
            <tr>
                <td class="value_inline_index" >
                    4.)
                </td>
                <td class="value_inline" >
                    Select Survival Value:
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
    <sx:div id="queryKmPlotDiv" 
            href="%{createQueryBasedKMPlot}" 
            formId="kaplanMeierQueryInputForm" 
            showLoadingText="true"
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createQueryBasedKMPlot" refreshOnShow="true" />
        
    </center>

    <!-- /Kaplan-Meier Inputs -->
    
</s:form>