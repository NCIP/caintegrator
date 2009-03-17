<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
     
     <s:actionerror />
     <!-- Gene Expression Graph -->
    <s:set name="gePlot" value="#session['gePlot']" />
    <s:if test="#gePlot.annotationBasedGePlot != null">
        <br>
        <center>
            <s:set name="retrieveAnnotationGEPlot" id="retrieveAnnotationGEPlot" value="%{retrieveGePlotUrl()}"/>
            <img src="${retrieveAnnotationGEPlot}"/>
            <br>
        
        <!-- Legend -->
        <fieldset style='display:table;width:600; border:1px solid gray; text-align:left; padding:5px;'>
        <legend> Legend: Reporters </legend>
        <s:iterator value="#gePlot.annotationBasedGePlot.legendItems" status="status">
            <table style="display:inline; _margin-top:20px;">
	            <tr>
		            <td style="width:10px; height:10px; background-color:<s:property value='color'/>; border:1px solid black;">&nbsp;&nbsp;&nbsp;</td>
		            <td style="padding-right: 10px; padding-left:5px;"><b><s:property value="label"/></b>
		            </td>
	            </tr>
            </table>
        </s:iterator>
        </fieldset>
        </center>
    </s:if>
    <!-- /Kaplan-Meier Graph -->


