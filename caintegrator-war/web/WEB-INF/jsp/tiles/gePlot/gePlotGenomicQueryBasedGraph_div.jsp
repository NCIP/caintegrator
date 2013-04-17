<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
     
     <s:actionerror />
     <!-- Gene Expression Graph -->
    <s:set name="gePlot" value="#session['gePlot']" />
    <s:if test="#gePlot.genomicQueryBasedGePlot != null">
        <center>
            <s:set name="retrieveGenomicQueryGEPlot_mean" id="retrieveGenomicQueryGEPlot_mean" value="%{retrieveGePlotUrl('MEAN')}"/>
            <s:set name="retrieveGenomicQueryGEPlot_median" id="retrieveGenomicQueryGEPlot_median" value="%{retrieveGePlotUrl('MEDIAN')}"/>
            <s:set name="retrieveGenomicQueryGEPlot_log2" id="retrieveGenomicQueryGEPlot_log2" value="%{retrieveGePlotUrl('LOG2_INTENSITY')}"/>
            <s:set name="retrieveGenomicQueryGEPlot_bw" id="retrieveGenomicQueryGEPlot_bw" value="%{retrieveGePlotUrl('BOX_WHISKER_LOG2_INTENSITY')}"/>
            <b> Plot Type: </b> <a href="#" id="genomicQueryTypeSelection1" style="background-color:yellow;" onclick="setDynamicPlot('genomicQueryBasedImage', '${retrieveGenomicQueryGEPlot_mean}', 'genomicQueryTypeSelection', '1')">Mean</a> | 
                                 <a href="#" id="genomicQueryTypeSelection2" onclick="setDynamicPlot('genomicQueryBasedImage', '${retrieveGenomicQueryGEPlot_median}', 'genomicQueryTypeSelection', '2')">Median</a> |
                                 <a href="#" id="genomicQueryTypeSelection3" onclick="setDynamicPlot('genomicQueryBasedImage', '${retrieveGenomicQueryGEPlot_log2}', 'genomicQueryTypeSelection', '3')">Log2</a> |
                                 <a href="#" id="genomicQueryTypeSelection4" onclick="setDynamicPlot('genomicQueryBasedImage', '${retrieveGenomicQueryGEPlot_bw}', 'genomicQueryTypeSelection', '4')">Box and Whisker Log2</a>
            <br>
            <div style="overflow:auto; padding: 0 0 20px 0;">
            <img id="genomicQueryBasedImage" src="${retrieveGenomicQueryGEPlot_mean}" />
            </div>
            <br>
        
        <!-- Legend -->
        <fieldset style='display:table;width:600; border:1px solid gray; text-align:left; padding:5px;'>
        <legend> Legend: Reporters </legend>
        <s:iterator value="#gePlot.genomicQueryBasedGePlot.legendItems" status="status">
            <table style="display:inline; _margin-top:20px;">
                <tr>
                    <td style="width:10px; height:10px; background-color:<s:property value='color'/>; border:1px solid black;">&nbsp;&nbsp;&nbsp;</td>
                    <td style="padding-right: 10px; padding-left:5px;"><b><s:property value="label"/></b>
                    </td>
                </tr>
            </table>
        </s:iterator>
        </fieldset>
        <br>
        <fieldset style='display:table;width:400; border:1px solid gray; text-align:left; padding:5px;'>
        <legend> Legend: Groups </legend>
        <s:iterator value="#gePlot.genomicQueryBasedGePlot.groupNameToNumberSubjectsMap" status="status">
            <b><s:property value="key"/> = <s:property value="value"/></b><br>
        </s:iterator>
        </fieldset>

        </center>
    </s:if>
    <!-- /Gene Expression Graph -->


