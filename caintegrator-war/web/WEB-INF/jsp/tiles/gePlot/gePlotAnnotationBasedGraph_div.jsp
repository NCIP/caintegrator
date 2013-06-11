<%@ taglib prefix="s" uri="/struts-tags"%>
     
     <s:actionerror />
     <!-- Gene Expression Graph -->
    <s:set name="gePlot" value="#session['gePlot']" />
    <s:if test="#gePlot.annotationBasedGePlot != null">
        <center>
            <s:set name="retrieveAnnotationGEPlot_mean" id="retrieveAnnotationGEPlot_mean" value="%{retrieveGePlotUrl('MEAN')}"/>
            <s:set name="retrieveAnnotationGEPlot_median" id="retrieveAnnotationGEPlot_median" value="%{retrieveGePlotUrl('MEDIAN')}"/>
            <s:set name="retrieveAnnotationGEPlot_log2" id="retrieveAnnotationGEPlot_log2" value="%{retrieveGePlotUrl('LOG2_INTENSITY')}"/>
            <s:set name="retrieveAnnotationGEPlot_bw" id="retrieveAnnotationGEPlot_bw" value="%{retrieveGePlotUrl('BOX_WHISKER_LOG2_INTENSITY')}"/>
            <b> Plot Type: </b> <a href="#" id="annotationTypeSelection1" style="background-color:yellow;" onclick="setDynamicPlot('annotationBasedImage', '${retrieveAnnotationGEPlot_mean}', 'annotationTypeSelection', '1')">Mean</a> | 
                                 <a href="#" id="annotationTypeSelection2" onclick="setDynamicPlot('annotationBasedImage', '${retrieveAnnotationGEPlot_median}', 'annotationTypeSelection', '2')">Median</a> |
                                 <a href="#" id="annotationTypeSelection3" onclick="setDynamicPlot('annotationBasedImage', '${retrieveAnnotationGEPlot_log2}', 'annotationTypeSelection', '3')">Log2</a> |
                                 <a href="#" id="annotationTypeSelection4" onclick="setDynamicPlot('annotationBasedImage', '${retrieveAnnotationGEPlot_bw}', 'annotationTypeSelection', '4')">Box and Whisker Log2</a>
            <br>
            <div style="overflow:auto; padding: 0 0 10px 0;">
            <img id="annotationBasedImage" src="${retrieveAnnotationGEPlot_mean}" />
            </div>
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
        <br>
        <fieldset style='display:table;width:400; border:1px solid gray; text-align:left; padding:5px;'>
        <legend> Legend: Groups </legend>
        <s:iterator value="#gePlot.annotationBasedGePlot.groupNameToNumberSubjectsMap" status="status">
            <b><s:property value="key"/> = <s:property value="value"/></b><br>
        </s:iterator>
        </fieldset>

        <s:if test="!#gePlot.annotationBasedGePlot.genesNotFound.empty">
	        <fieldset style='display:table;width:400; border:1px solid gray; text-align:left; padding:5px;'>
	        <legend> Legend: Genes Not Found </legend>
	        <s:iterator value="#gePlot.annotationBasedGePlot.genesNotFound" >
	            <b><s:property /></b><br>
	        </s:iterator>
	        </fieldset>
        </s:if>
        </center>
    </s:if>
    <!-- /Gene Expression Graph -->
