<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<s:form name="kaplanMeierGeneExpressionInputForm" id="kaplanMeierGeneExpressionInputForm" theme="simple">
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
    
    <!-- Kaplan-Meier Inputs -->
    <h2>Gene Expression Based Kaplan-Meier Survival Plots</h2>

        <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                1.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 150px;">
                    Gene Symbol:  <br>
                    <div style="font-size: 75%; color: #666666;"></div>
                </td>
                <td class="value_inline">
                    <s:textfield id="kmGeneSymbolsId" name="kmPlotForm.geneExpressionBasedForm.geneSymbol" theme="simple" />
                    &nbsp;
                    <s:a href=""
                        cssStyle="background:transparent url('/caintegrator2/images/cgaplogo.gif') no-repeat scroll 0 0; padding:0px 70px 5px 8px;"
                        title="Open CGAP" onclick="gotoCGAP('%{displayableWorkspace.cgapUrl}','kmGeneSymbolsId')"/>
		        </td>
	        </tr>
	        
	        <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                2.)
                </td>
		        <td class="value_inline">
                    Overexpressed >= <br>
                </td>
                <td class="value_inline"><s:textfield name="kmPlotForm.geneExpressionBasedForm.overexpressedNumber" size="4" theme="simple"/> fold
                </td> 
            </tr>
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                3.)
                </td>
                <td class="value_inline">
                    Underexpressed >= <br>
                </td>
                <td class="value_inline">
                    <s:textfield name="kmPlotForm.geneExpressionBasedForm.underexpressedNumber" size="4" theme="simple"/> fold
                </td>
            </tr>
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                    4.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 150px;">
                    Select Survival Value:<br>
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