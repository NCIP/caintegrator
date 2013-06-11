<%@ taglib prefix="s" uri="/struts-tags"%>

    <s:actionerror />
    <!-- Kaplan-Meier Graph -->
    <s:set name="kmPlot" value="#session['kmPlot']" />
    <s:if test="#kmPlot.geneExpressionBasedKmPlot != null">
        <br>
        <center>
        
            <s:set name="retrieveGeneExpressionKMPlot" id="retrieveGeneExpressionKMPlot" value="%{retrieveKmPlotUrl()}"/>
            <img src="${retrieveGeneExpressionKMPlot}"/>
            <br>
            
            <s:if test="!genesNotFound.isEmpty()">
            <strong>Genes Not Found in Study</strong>
            <br/>
            <font color="red">
            <s:iterator value="genesNotFound" status="geneIteratorStatus">
                <s:if test="#geneIteratorStatus.first == false">, </s:if><s:property />
            </s:iterator>
             </font>
            </s:if>
            <br/>
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
                        <td align="right" style="padding:0px 5px 0px 5px;"><s:property value="#group1Name"/></td>
                        <td style="padding:0px 5px 0px 5px;"> vs. </td>
                        <td align="left" style="padding:0px 5px 0px 5px;"> <s:property value="#group2Name"/> </td>
                        <td style="padding:0px 5px 0px 5px;"> = </td> 
                        <td style="padding:0px 5px 0px 5px;"> <s:property value="value"/> </td>
                    </tr>
                </s:iterator> <!-- End Innter Map -->
            </s:iterator> <!-- End Outter Map -->
            </table>
        </center>
    </s:if>
    <!-- /Kaplan-Meier Graph -->