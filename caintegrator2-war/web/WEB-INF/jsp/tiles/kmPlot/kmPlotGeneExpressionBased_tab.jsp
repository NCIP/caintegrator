<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
                   
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
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
        <center>
        <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.kaplanMeierGeneExpressionInputForm.action = 'resetGeneExpressionBasedKMPlot.action';document.kaplanMeierGeneExpressionInputForm.submit();"><span class="btn_img">Reset</span></s:a>
        <s:if test="creatable">
	        <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.kaplanMeierGeneExpressionInputForm.action = 'createGeneExpressionBasedKMPlot.action';document.kaplanMeierGeneExpressionInputForm.submit();"><span class="btn_img">Create Plot</span></s:a>
        </s:if>
        </center>

    <!-- /Kaplan-Meier Inputs -->
    
    <!-- Kaplan-Meier Graph -->
    <s:set name="kmPlot" value="#session['kmPlot']" />
    <s:if test="#kmPlot.geneExpressionBasedKmPlot != null">
        <br>
        <center>
            <img src="retrieveGeneExpressionKMPlot.action"/>
            <br>
            
            <strong>Log-rank P-value for significance of difference in survival between groups</strong>
            <br>
            <table cellspacing="10">
            
            <!-- Outter Map -->
            <s:iterator value="allStringPValues">
                <s:set name="group1Name" value="key"/>
                <!-- Innter Map -->
                <s:iterator value="value">
                    <s:set name="group2Name" value="key"/>
                    <tr>
                        <td align="right"><s:property value="#group1Name"/></td>
                        <td> vs. </td>
                        <td align="left"> <s:property value="#group2Name"/> </td>
                        <td> = </td> 
                        <td> <s:property value="value"/> </td>
                    </tr>
                </s:iterator> <!-- End Innter Map -->
            </s:iterator> <!-- End Outter Map -->
            </table>
        </center>
    </s:if>
    <!-- /Kaplan-Meier Graph -->