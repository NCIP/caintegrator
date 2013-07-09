<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<s:form name="kaplanMeierQueryInputForm" id="kaplanMeierQueryInputForm" theme="simple">
    <s:token />
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
          
    <!-- Kaplan-Meier Inputs -->
    <h2>Kaplan-Meier Survival Plots based on Saved Queries and Saved Lists</h2>

        <table class="data">
            <tr>
                <td class="value_inline_index" >
                    1.) 
                </td>
                <td class="value_inline">
                    Select Saved Queries and Lists:
                </td>
                <td class="optiontransferselect">
                    <s:optiontransferselect
                    id="allQueries"
                    doubleId="querySelections"
                    name="kmPlotForm.queryBasedForm.unselectedQueryNames"
                    list="kmPlotForm.queryBasedForm.unselectedQueries"
                    listValue="value.displayName"
                    doubleName="kmPlotForm.queryBasedForm.selectedQueryNames"
                    doubleList="kmPlotForm.queryBasedForm.selectedQueries"
                    doubleListValue="value.displayName"
                    allowAddAllToLeft="false"
                    allowAddAllToRight="false"
                    allowUpDownOnLeft="false"
                    allowUpDownOnRight="true"
                    leftTitle="Available Queries and Lists"
                    rightTitle="Selected Queries and Lists"
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
                    <s:checkbox name="kmPlotForm.queryBasedForm.exclusiveGroups"/>Exclusive Subjects (Subjects in upper Selected Queries or Lists are removed from subsequent Selected Queries or Lists) 
                </td> 
            </tr>
            <tr>
                <td class="value_inline_index" >
                    3.)
                </td>
                <td colspan="2" class="value_inline">
                    <s:checkbox name="kmPlotForm.queryBasedForm.addPatientsNotInQueriesGroup"/>Add additional group containing all other subjects not found in selected queries and lists.
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
            loadingText="<img src='images/ajax-loader-processing.gif' alt='ajax icon indicating loading process'/>"
            listenTopics="createQueryBasedKMPlot" refreshOnShow="true" />
        
    </center>

    <!-- /Kaplan-Meier Inputs -->
    
</s:form>