<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script language="javascript">

    function setGroupAnnotations(onOff, size, checkboxListNum){
        for(i=1;i<=size;i++){
            var item = 'queryForm.resultConfiguration.columnSelectionLists[' + checkboxListNum +'].values-' + i;
            document.heatmapViewerForm[item].checked = onOff;
        }
    }
    
    function submitView() {
        if (document.heatmapViewerForm.copyNumberPlatformName.value == "") {
            alert("You must select at least 1 platform.");
            return false;
        }
        document.heatmapViewerForm.target = '_blank';
        document.heatmapViewerForm.selectedAction.value = 'viewAll';
        document.heatmapViewerForm.submit();
        document.heatmapViewerForm.target = '_self';
        return true;
    }

</script>

<div id="content">
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('DYDnAg', '6-AnalyzingStudies-ViewingDatawithHeatMapViewer')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <p>Enter Heat Map Viewer parameters and click <strong>View</strong>.</p>
    <div class="form_wrapper_outer">
 
    <s:form id="heatmapViewerForm" name="heatmapViewerForm" method="post" enctype="multipart/form-data"
        action="viewAllHeatmap" theme="css_xhtml">
        <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Platform</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr><td colspan="2" style="padding: 5px;">    
                <s:actionerror/>
                    <s:hidden name="selectedAction" />
                    <s:select id="copyNumberPlatformName" name="copyNumberPlatformName"
                        headerKey="" headerValue="%{copyNumberPlatformOption}"
                        label="Copy Number Platform" list="copyNumberPlatformsInStudy"/>
                     <s:if test="study.hasCghCalls()" >
                        <s:select name="displayableCopyNumberType" 
                            list="@gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum@getDisplayableValues()"
                            label="Preferred Copy Number Method"/>
                    </s:if>
                    <br/>
                </td>
            </tr>
            <tr><td colspan="2" style="padding: 5px;" align="center">

                    <br/>
                     <s:iterator value="queryForm.resultConfiguration.columnSelectionLists" status="iterator">
                        <div class="checklistwrapper">
                        <h3><s:property value="annotationGroup.name"/></h3>
                        <ul class="checklist">
                        <s:checkboxlist 
                            cssClass="checklist" 
                            name="queryForm.resultConfiguration.columnSelectionLists[%{#iterator.count - 1}].values"
                            list="queryForm.resultConfiguration.columnSelectionLists[#iterator.count - 1].options" 
                            listKey="key" 
                            listValue="displayValue"
                            theme="cai2simple" 
                            value="queryForm.resultConfiguration.columnSelectionLists[#iterator.count - 1].values" />
                        </ul>
                    <br/>
                        <ul>
                        <s:if test="!options.isEmpty()">
                            <s:set name="ckboxSize" value="%{queryForm.resultConfiguration.columnSelectionLists[#iterator.count - 1].options.size}" />
                            <s:set name="iteratorCount" value="%{#iterator.count - 1}"/>
                            <input type="button" value="Select All" onclick="setGroupAnnotations(1,${ckboxSize}, ${iteratorCount})" />
                            <input type="button" value="Unselect All" onclick="setGroupAnnotations(0,${ckboxSize}, ${iteratorCount})" />
                        </s:if>
                        </ul>
                        </div>
                    </s:iterator>
                    <br/>

                    <s:div cssClass="wwgrp">

                        <s:div cssClass="wwlbl"></s:div>

                        <s:div cssClass="wwctrl">

                            <button type="button" onclick="document.heatmapViewerForm.selectedAction.value = 'cancel';

                                document.heatmapViewerForm.submit();"> Cancel </button>

                            <button type="button" onclick="submitView()"> View </button>

                        </s:div>

                    </s:div>

            </td></tr>
        </table>
    </s:form>
    </div>
</div>
<div class="clear"><br /></div>
