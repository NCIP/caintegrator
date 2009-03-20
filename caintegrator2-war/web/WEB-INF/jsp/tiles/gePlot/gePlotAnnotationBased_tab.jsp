<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form name="geneExpressionAnnotationInputForm" id="geneExpressionAnnotationInputForm" theme="simple">
                
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="permissibleValuesNeedUpdate" value="false" />
       
    <!-- Gene Expression Inputs -->
    <h2>Annotation Based Gene Expression Plots</h2>
    
    <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                1.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 190px;">
                    Gene Symbol(s) (comma separated list):<br>
                    <div style="font-size: 75%; color: #666666;"></div>
                </td>
                <td class="value_inline"><s:textfield name="gePlotForm.annotationBasedForm.geneSymbol" theme="simple" title="Enter a comma separated list of gene symbols ( Ex: EGFR, BRCA1, etc. )"/>                
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
	                <s:radio name="gePlotForm.annotationBasedForm.reporterType"
	                list="@gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum@getValueToDisplayableMap()"
	                listKey="key" 
	                listValue="value" />                
                </td>
            </tr>
        </table>
        <table class="data">
            <tr>
                <th/>
                <th> Annotation Type</th>
                <th> Annotation </th>
                <th> Values </th>
            </tr>
            <tr>
                <td class="value_inline" style="min-width: 5px; padding-left: 10px;">
                    3.) Sample Groups:
                </td>
                <td class="value_inline">
                    <s:select name="gePlotForm.annotationBasedForm.annotationTypeSelection" 
                              list="#{'subject':'Subject', 'imageSeries':'Image Series'}"
                              headerKey="invalidSelection"
                              headerValue="Select Annotation Type"
                              onchange="document.geneExpressionAnnotationInputForm.action = 'gePlotUpdateAnnotationDefinitions.action';document.geneExpressionAnnotationInputForm.submit();"
                              theme="simple"/>
                </td>
                <td class="value_inline">
                <s:select name="gePlotForm.annotationBasedForm.selectedAnnotationId" 
                              list="gePlotForm.annotationBasedForm.annotationDefinitions"
                              listValue="value.displayName"
                              headerKey="-1"
                              headerValue="Select Annotation"
                              onchange="document.geneExpressionAnnotationInputForm.action = 'gePlotUpdatePermissibleValues.action';document.geneExpressionAnnotationInputForm.permissibleValuesNeedUpdate.value = 'true';document.geneExpressionAnnotationInputForm.submit();"
                              theme="simple"/>
                </td>
                <td>
                <s:select name="gePlotForm.annotationBasedForm.selectedValuesIds" 
                              list="gePlotForm.annotationBasedForm.permissibleValues"
                              multiple="true"
                              theme="simple"/>
                </td>
            </tr>
        
        </table>
        

        <br>
        <div>
        <center>
        
        <button type="button" 
                onclick="document.geneExpressionAnnotationInputForm.action = 'resetAnnotationBasedGEPlot.action';
                document.geneExpressionAnnotationInputForm.submit();"> Reset 
        </button>
        
        <s:if test="creatable">
            <button type="button" 
                    onclick="document.geneExpressionAnnotationInputForm.createPlotSelected.value = 'true';
                    dojo.event.topic.publish('createAnnotationPlot');"> Create Plot 
            </button>
        </s:if>
        </center>
        </div>
    <!-- /Gene Expression Inputs -->
    <s:url id="createAnnotationBasedGEPlot" action="createAnnotationBasedGEPlot"/>
    
    <br>
    <center>
    <s:div id="annotationGePlotDiv" 
            theme="ajax" 
            href="%{createAnnotationBasedGEPlot}" 
            formId="geneExpressionAnnotationInputForm" 
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createAnnotationPlot" refreshOnShow="true" />
        
    </center>
    
</s:form>