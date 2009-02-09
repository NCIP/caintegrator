<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<s:form name="kaplanMeierGeneExpressionInputForm" id="kaplanMeierGeneExpressionInputForm" theme="simple">
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
    
    <!-- Kaplan-Meier Inputs -->
    <h1>Gene Expression Based Kaplan-Meier Survival Plots</h1>

        <table class="data">
            <tr>
                <td class="value_inline">
                    1.) Gene Symbol:  <s:textfield name="kmPlotForm.geneExpressionBasedForm.geneSymbol" theme="simple"/>                
		        </td>
	        </tr>
	        
	        <tr>
		        <td class="value_inline">
                    2.) Overexpressed >= <s:textfield name="kmPlotForm.geneExpressionBasedForm.overexpressedNumber" size="4" theme="simple"/> fold
                </td> 
            </tr>
            <tr>
                <td class="value_inline">
                    3.) Underexpressed >= <s:textfield name="kmPlotForm.geneExpressionBasedForm.underexpressedNumber" size="4" theme="simple"/> fold
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
                onclick="document.kaplanMeierGeneExpressionInputForm.resetSelected.value = 'true';
                document.kaplanMeierGeneExpressionInputForm.action = 'resetGeneExpressionBasedKMPlot.action';
                document.kaplanMeierGeneExpressionInputForm.submit();"> Reset 
        </button>
        <s:if test="creatable">
            <button type="button" 
                    onclick="document.kaplanMeierGeneExpressionInputForm.createPlotSelected.value = 'true';
                    dojo.event.topic.publish('createGeneExpressionPlot');"> Create Plot 
            </button>
        </s:if>
        </center>
        </div>
    <!-- /Kaplan-Meier Inputs -->
    
    <s:url id="createGeneExpressionBasedKMPlot" action="createGeneExpressionBasedKMPlot"/>
    
    <br><br>
    <center>
    <s:div id="geneExpressionKmPlotDiv" 
            theme="ajax" 
            href="%{createGeneExpressionBasedKMPlot}" 
            formId="kaplanMeierGeneExpressionInputForm" 
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createGeneExpressionPlot" refreshOnShow="true" />
        
    </center>
    
</s:form>