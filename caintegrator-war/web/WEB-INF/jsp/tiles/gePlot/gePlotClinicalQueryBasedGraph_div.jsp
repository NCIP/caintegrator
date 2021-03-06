<%@ taglib prefix="s" uri="/struts-tags"%>
     
     <s:actionerror />
     <!-- Gene Expression Graph -->
    <s:set name="gePlot" value="#session['gePlot']" />
    <s:if test="#gePlot.clinicalQueryBasedGePlot != null">
        <center>
            <s:set name="retrieveClinicalQueryGEPlot_mean" id="retrieveClinicalQueryGEPlot_mean" value="%{retrieveGePlotUrl('MEAN')}"/>
            <s:set name="retrieveClinicalQueryGEPlot_median" id="retrieveClinicalQueryGEPlot_median" value="%{retrieveGePlotUrl('MEDIAN')}"/>
            <s:set name="retrieveClinicalQueryGEPlot_log2" id="retrieveClinicalQueryGEPlot_log2" value="%{retrieveGePlotUrl('LOG2_INTENSITY')}"/>
            <s:set name="retrieveClinicalQueryGEPlot_bw" id="retrieveClinicalQueryGEPlot_bw" value="%{retrieveGePlotUrl('BOX_WHISKER_LOG2_INTENSITY')}"/>
            <b> Plot Type: </b> <a href="#" id="clinicalQueryTypeSelection1" style="background-color:yellow;" onclick="setDynamicPlot('clinicalQueryBasedImage', '${retrieveClinicalQueryGEPlot_mean}', 'clinicalQueryTypeSelection', '1')">Mean</a> | 
                                 <a href="#" id="clinicalQueryTypeSelection2" onclick="setDynamicPlot('clinicalQueryBasedImage', '${retrieveClinicalQueryGEPlot_median}', 'clinicalQueryTypeSelection', '2')">Median</a> |
                                 <a href="#" id="clinicalQueryTypeSelection3" onclick="setDynamicPlot('clinicalQueryBasedImage', '${retrieveClinicalQueryGEPlot_log2}', 'clinicalQueryTypeSelection', '3')">Log2</a> |
                                 <a href="#" id="clinicalQueryTypeSelection4" onclick="setDynamicPlot('clinicalQueryBasedImage', '${retrieveClinicalQueryGEPlot_bw}', 'clinicalQueryTypeSelection', '4')">Box and Whisker Log2</a>
            <br>
            <div style="overflow:auto; padding: 0 0 20px 0;">
            <img id="clinicalQueryBasedImage" src="${retrieveClinicalQueryGEPlot_mean}" />
            </div>
            <br>
        
        <!-- Legend -->
        <fieldset style='display:table;width:600; border:1px solid gray; text-align:left; padding:5px;'>
        <legend> Legend: Reporters </legend>
        <s:iterator value="#gePlot.clinicalQueryBasedGePlot.legendItems" status="status">
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
        <s:iterator value="#gePlot.clinicalQueryBasedGePlot.groupNameToNumberSubjectsMap" status="status">
            <b><s:property value="key"/> = <s:property value="value"/></b><br>
        </s:iterator>
        </fieldset>

        <s:if test="!#gePlot.clinicalQueryBasedGePlot.genesNotFound.empty">
            <fieldset style='display:table;width:400; border:1px solid gray; text-align:left; padding:5px;'>
            <legend> Legend: Genes Not Found </legend>
            <s:iterator value="#gePlot.clinicalQueryBasedGePlot.genesNotFound" >
                <b><s:property /></b><br>
            </s:iterator>
            </fieldset>
        </s:if>
        
        <s:if test="!#gePlot.clinicalQueryBasedGePlot.subjectsNotFound.empty">
            <fieldset style='display:table;width:400; border:1px solid gray; text-align:left; padding:5px;'>
            <legend> Legend: Subjects Not Found </legend>
            <s:iterator value="#gePlot.clinicalQueryBasedGePlot.subjectsNotFound" >
                <b><s:property /></b><br>
            </s:iterator>
            </fieldset>
        </s:if>
        </center>
    </s:if>
    <!-- /Gene Expression Graph -->
