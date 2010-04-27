<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<s:form name="geneExpressionClinicalQueryInputForm" id="geneExpressionClinicalQueryInputForm" theme="simple">
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
    <!-- For caBio to know which form element to publish gene symbols to. -->
    <s:hidden name="geneSymbolElementId" />

    <!-- Gene Expression Inputs -->
    <h2>Gene Expression Plots based on Saved Queries and Saved Lists</h2>
    
    <div class="tableheader" style="white-space: nowrap; position: relative; width: 25em;">
        <div class="tabhelp" style="white-space:nowrap; position: relative; margin-top: 0.4em; margin-right: 0.5em;">
	       <a href="javascript:openHelpWindowWithNavigation('GE_plot_clin_queries_help')" class="help">
	       &nbsp;</a>
        </div>
    </div>
    

        <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                1.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 190px;">
                    Gene Symbol(s) (comma separated list):<br>
                    <div style="font-size: 75%; color: #666666;"></div>
                </td>
                <td class="value_inline">
                    <s:component template="genetextfield.ftl" theme="cai2simple">
                        <s:param name="createTextField" value="true" />
                        <s:param name="textFieldId" value="%{'clinicalGeneSymbolsId'}"/>
                        <s:param name="textFieldName" value="%{'gePlotForm.clinicalQueryBasedForm.geneSymbol'}"/>
                    </s:component>
                </td>
            </tr>
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                2.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 120px;">
                    Select Reporter Type:
                </td>
                <td class="value_inline">
                    <s:radio name="gePlotForm.clinicalQueryBasedForm.reporterType"
                    list="@gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum@getValueToDisplayableMap()"
                    listKey="key" 
                    listValue="value" />                
                </td>
            </tr>
            
            <s:if test="%{studyHasMultiplePlatforms}">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                2a.)
                </td>
                <td class="value_inline" colspan="4">
                    Select Platform:  
                    <s:select name="gePlotForm.clinicalQueryBasedForm.platformName" 
                        list="platformsInStudy"
                        headerKey="" headerValue="Select Platform"
                         onchange="document.geneExpressionClinicalQueryInputForm.action = 'updateControlSamplesClinicalQueryBasedGEPlot.action';document.geneExpressionClinicalQueryInputForm.submit();"/>
                </td>
            </tr>
            </s:if>
            
            
            <tr>
                <td class="value_inline_index" >
                    3.) 
                </td>
                <td class="value_inline">
                    Select Saved Queries and Lists:
                </td>
                <td class="optiontransferselect">
                    <s:optiontransferselect
                    id="allQueries"
                    doubleId="querySelections"
                    name="gePlotForm.clinicalQueryBasedForm.unselectedQueryNames"
                    list="gePlotForm.clinicalQueryBasedForm.unselectedQueries"
                    listValue="value.displayName"
                    doubleName="gePlotForm.clinicalQueryBasedForm.selectedQueryNames"
                    doubleList="gePlotForm.clinicalQueryBasedForm.selectedQueries"
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
                    4.) 
                </td>
		        <td colspan="2" class="value_inline">
                    <s:checkbox name="gePlotForm.clinicalQueryBasedForm.exclusiveGroups"/>Exclusive Subjects (Subjects in upper Selected Queries or Lists are removed from subsequent Selected Queries or Lists) 
                </td> 
            </tr>
            <tr>
                <td class="value_inline_index" >
                    5.)
                </td>
                <td colspan="2" class="value_inline">
                    <s:checkbox name="gePlotForm.clinicalQueryBasedForm.addPatientsNotInQueriesGroup"/>Add additional group containing all other subjects not found in selected queries and lists.
                </td>
            </tr>
            <tr>
                <td class="value_inline_index" >
                    6.)
                </td>
                <td colspan="2" class="value_inline">
                    <s:checkbox name="gePlotForm.clinicalQueryBasedForm.addControlSamplesGroup"
                        disabled="%{!hasControlSamples()}" />Add additional group containing all control samples for this study.
                    <s:select name="gePlotForm.clinicalQueryBasedForm.controlSampleSetName" list="controlSampleSets"
                        disabled="%{!hasControlSamples()}"/>
                </td>
            </tr>
        </table>
        <br>
        <div>
        <center>
        <button type="button" 
                onclick="document.geneExpressionClinicalQueryInputForm.resetSelected.value = 'true';
                document.geneExpressionClinicalQueryInputForm.action = 'resetClinicalQueryBasedGEPlot.action';
                document.geneExpressionClinicalQueryInputForm.submit();"> Reset 
        </button>
        <s:if test="creatable">
        
            <button type="button" 
                    onclick="selectAllOptions(document.getElementById('allQueries'));
                    selectAllOptions(document.getElementById('querySelections'));
                    document.geneExpressionClinicalQueryInputForm.createPlotSelected.value = 'true';
                    dojo.event.topic.publish('createClinicalQueryBasedGEPlot');"> Create Plot 
            </button>
            
        </s:if>
        </center>
        </div>
        
    <s:url id="createClinicalQueryBasedGEPlot" action="createClinicalQueryBasedGEPlot"/>
    
    <br><br>
    <center>
    <sx:div id="clinicalQueryGePlotDiv"  
            href="%{createClinicalQueryBasedGEPlot}" 
            formId="geneExpressionClinicalQueryInputForm" 
            showLoadingText="true"
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createClinicalQueryBasedGEPlot" refreshOnShow="true" />
        
    </center>

    <!-- /Gene Expression Inputs -->
    
</s:form>