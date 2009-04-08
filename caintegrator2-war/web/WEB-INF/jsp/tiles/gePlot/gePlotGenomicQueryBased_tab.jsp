<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form name="geneExpressionGenomicQueryInputForm" id="geneExpressionGenomicQueryInputForm" theme="simple">
                
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
    
       
    <!-- Gene Expression Inputs -->
    <h2>Genomic Query Based Gene Expression Plots <div class="tabhelp"><a href="javascript:openHelpWindowWithNavigation('GE_plot_gen_queries_help')" class="help">
   (draft)</a>
    </div></h2>
    
    <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                1.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 190px;">
                    Select a Genomic Query:<br>
                    <div style="font-size: 75%; color: #666666;"></div>
                </td>
                <td class="value_inline">
                    <s:select
	                    list="gePlotForm.genomicQueryBasedForm.queries"
	                    listValue="value.name"
	                    listKey="key"
	                    name="gePlotForm.genomicQueryBasedForm.selectedQueryId" 
	                    size="5"
	                    multiple="false"
	                    theme="simple" />                
                </td>
            </tr>
        </table>
        <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                2.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 120px;">
                    Select Reporter Type:
                </td>
                <td class="value_inline">
	                <s:radio name="gePlotForm.genomicQueryBasedForm.reporterType"
	                list="@gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum@getValueToDisplayableMap()"
	                listKey="key" 
	                listValue="value" />                
                </td>
            </tr>
        </table>
        

        <br>
        <div>
        <center>
        
        <button type="button" 
                onclick="document.geneExpressionGenomicQueryInputForm.resetSelected.value = 'true';
                document.geneExpressionGenomicQueryInputForm.action = 'resetGenomicQueryBasedGEPlot.action';
                document.geneExpressionGenomicQueryInputForm.submit();"> Reset 
        </button>
        
        <s:if test="creatable">
            <button type="button" 
                    onclick="document.geneExpressionGenomicQueryInputForm.createPlotSelected.value = 'true';
                    dojo.event.topic.publish('createGenomicQueryPlot');"> Create Plot 
            </button>
        </s:if>
        </center>
        </div>
    <!-- /Gene Expression Inputs -->
    <s:url id="createGenomicQueryBasedGEPlot" action="createGenomicQueryBasedGEPlot"/>
    
    <br>
    <center>
    <s:div id="genomicQueryGePlotDiv" 
            theme="ajax" 
            href="%{createGenomicQueryBasedGEPlot}" 
            formId="geneExpressionGenomicQueryInputForm" 
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createGenomicQueryPlot" refreshOnShow="true" />
        
    </center>
    
</s:form>