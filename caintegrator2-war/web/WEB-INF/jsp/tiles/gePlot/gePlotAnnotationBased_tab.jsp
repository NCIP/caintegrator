<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form name="geneExpressionAnnotationInputForm" id="geneExpressionAnnotationInputForm" theme="simple">
                
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="permissibleValuesNeedUpdate" value="false" />
    <!-- The hidden variables for caBio search form. -->
    <s:hidden name="formName" value="geneExpressionAnnotationInputForm" />
    <s:hidden name="showCaBioSearch" value="false" />
    <s:hidden name="geneSymbolElementId" value="annotationGeneSymbolsId" />
       
    <!-- Gene Expression Inputs -->
    <h2>Annotation Based Gene Expression Plots <div class="tabhelp"><a href="javascript:openHelpWindowWithNavigation('GE_plot_annotation_help')" class="help">
   (draft)</a>
    </div></h2>
    
    
    <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                1.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 190px; white-space: nowrap;">
                    Gene Symbol(s) (comma separated list):<br>
                    <div style="font-size: 75%; color: #666666;"></div>
                </td>
                <td class="value_inline" colspan="3">
                    <s:textfield id="annotationGeneSymbolsId" name="gePlotForm.annotationBasedForm.geneSymbol"
                        theme="simple" title="Enter a comma separated list of gene symbols ( Ex: EGFR, BRCA1, etc. )"/>
                    &nbsp;
                    <s:a href=""
                        cssClass="cgapLogo"
                        title="Click to find this Gene Symbol in the Cancer Genome Anatomy Project (CGAP)" onclick="gotoCGAP('%{displayableWorkspace.cgapUrl}','annotationGeneSymbolsId')">&nbsp;</s:a>
                    <s:a href=""
                        cssClass="caBioLogo"
                        title="Click to search caBio for genes based on keywords." onclick="showCaBioInputForm(geneExpressionAnnotationInputForm)">&nbsp;</s:a>
                    <br>
                    <s:url id="caBioGeneSearchInput" action="caBioGeneSearchInput"/>
                    <s:div id="caBioGeneSearchInputDiv" 
                            theme="ajax" 
                            href="%{caBioGeneSearchInput}" 
                            formId="geneExpressionAnnotationInputForm" 
                            loadingText="<img src='images/ajax-loader.gif'/>"
                            listenTopics="caBioSearchInput" refreshOnShow="true"/>
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
	                <s:radio name="gePlotForm.annotationBasedForm.reporterType"
	                list="@gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum@getValueToDisplayableMap()"
	                listKey="key" 
	                listValue="value" />                
                </td>
                <td class="value_inline"></td>
                <td class="value_inline"></td>
            </tr>

            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                <td class="value_inline" style="min-width: 5px; width: 190px; white-space: nowrap;">
                <th> Annotation Type</th>
                <th> Annotation </th>
                <th> Values </th>
            </tr>
            <tr>
                <td class="value_inline" style="min-width: 5px; padding-left: 10px; border-top: 0px;">
                    3.)
                </td>    
                <td class="value_inline" style="min-width: 5px; width: 120px; border-top: 0px;">    
                    Sample Groups:
                </td>
                <td class="value_inline">
                    <s:select name="gePlotForm.annotationBasedForm.annotationTypeSelection" 
                              list="annotationTypes"
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
                <td class="value_inline">
                <s:select name="gePlotForm.annotationBasedForm.selectedValuesIds" 
                              list="gePlotForm.annotationBasedForm.permissibleValues"
                              multiple="true"
                              theme="simple"/>
                </td>
            </tr>

            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                4.)
                </td>
                <td class="value_inline" colspan="4">
                <s:checkbox name="gePlotForm.annotationBasedForm.addPatientsNotInQueriesGroup" theme="simple"/>Add additional group containing all other subjects not found in selected queries.
                </td>
            </tr>
            
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                5.)
                </td>
                <td class="value_inline" colspan="4">
                <s:checkbox name="gePlotForm.annotationBasedForm.addControlSamplesGroup" disabled="%{!hasControlSamples()}" theme="simple"/>Add additional group containing all control samples for this study.
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