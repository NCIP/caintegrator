<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
     
     <s:actionerror />
     <!-- Kaplan-Meier Graph -->
    <s:set name="kmPlot" value="#session['kmPlot']" />
    <s:if test="#kmPlot.annotationBasedKmPlot != null">
        <br>
        <center>
            <s:set name="retrieveAnnotationKMPlot" id="retrieveAnnotationKMPlot" value="%{retrieveKmPlotUrl()}"/>
            <img src="${retrieveAnnotationKMPlot}"/>
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


