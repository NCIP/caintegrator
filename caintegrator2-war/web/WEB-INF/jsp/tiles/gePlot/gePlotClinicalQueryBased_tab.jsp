<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<s:form name="geneExpressionClinicalQueryInputForm" id="geneExpressionClinicalQueryInputForm" theme="simple">
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
          
    <!-- Gene Expression Inputs -->
    <h2>Clinical Query Based Gene Expression Plots <div class="tabhelp"><a href="javascript:openHelpWindowWithNavigation('GE_plot_clin_queries_help')" class="help">
   (draft)</a>
    </div></h2>

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
                    <s:textfield id="clinicalGeneSymbolsId" name="gePlotForm.clinicalQueryBasedForm.geneSymbol"
                        theme="simple" title="Enter a comma separated list of gene symbols ( Ex: EGFR, BRCA1, etc. )"/>
                    &nbsp;
                    <s:a href=""
                        cssStyle="background:transparent url('/caintegrator2/images/cgaplogo.gif') no-repeat scroll 0 0; padding:0px 70px 5px 8px;"
                        title="Open CGAP" onclick="gotoCGAP('%{displayableWorkspace.cgapUrl}','clinicalGeneSymbolsId')"/>
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
            <tr>
                <td class="value_inline_index" >
                    3.) 
                </td>
                <td class="value_inline">
                    Select Queries:
                </td>
                <td class="optiontransferselect">
                    <s:optiontransferselect
                    id="allQueries"
                    doubleId="querySelections"
                    name="gePlotForm.clinicalQueryBasedForm.unselectedQueryIDs"
                    list="gePlotForm.clinicalQueryBasedForm.unselectedQueries"
                    listValue="value.name"
                    doubleName="gePlotForm.clinicalQueryBasedForm.selectedQueryIDs"
                    doubleList="gePlotForm.clinicalQueryBasedForm.selectedQueries"
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
                    4.) 
                </td>
		        <td colspan="2" class="value_inline">
                    <s:checkbox name="gePlotForm.clinicalQueryBasedForm.exclusiveGroups"/>Exclusive Subjects in Queries (Subjects in upper queries are removed from subsequent queries) 
                </td> 
            </tr>
            <tr>
                <td class="value_inline_index" >
                    5.)
                </td>
                <td colspan="2" class="value_inline">
                    <s:checkbox name="gePlotForm.clinicalQueryBasedForm.addPatientsNotInQueriesGroup"/>Add additional group containing all other subjects not found in selected queries.
                </td>
            </tr>
            <tr>
                <td class="value_inline_index" >
                    6.)
                </td>
                <td colspan="2" class="value_inline">
                    <s:checkbox name="gePlotForm.clinicalQueryBasedForm.addControlSamplesGroup" disabled="%{!hasControlSamples()}" />Add additional group containing all control samples for this study.
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
    <s:div id="clinicalQueryGePlotDiv" 
            theme="ajax" 
            href="%{createClinicalQueryBasedGEPlot}" 
            formId="geneExpressionClinicalQueryInputForm" 
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createClinicalQueryBasedGEPlot" refreshOnShow="true" />
        
    </center>

    <!-- /Gene Expression Inputs -->
    
</s:form>