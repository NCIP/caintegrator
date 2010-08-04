<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/PlatformDeploymentAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content" >


<!--Page Help-->

<div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('manage_platforms_help')" class="help">
&nbsp;</a>
</div>

<!--/Page Help-->
    
<script type="text/javascript">
    
    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        PlatformDeploymentAjaxUpdater.initializeJsp();
    }
    
    function CheckPlatformType(type) {
        if (type == "Affymetrix Gene Expression") {
            document.getElementById("platformNameDiv").style.display = "none";
            document.getElementById("platformName").value = "N/A";
            document.getElementById("platformChannelTypeDiv").style.display = "none";
            document.getElementById("platformChannelType").value = "One-Color";
            document.getElementById("commentNameDiv").style.display = "none";
            document.getElementById("addFileButtonDiv").style.display = "none";
            document.getElementById("commentCsvDiv").style.display = "block";
            document.getElementById("commentAdfGemlDiv").style.display = "none";
            document.getElementById("commentAllFormatsDiv").style.display = "none";
        } else if (type == "Affymetrix SNP" || type == "Affymetrix Copy Number"){
            document.getElementById("platformNameDiv").style.display = "block";
            document.getElementById("platformName").value = "";
            document.getElementById("platformChannelTypeDiv").style.display = "none";
            document.getElementById("platformChannelType").value = "One-Color";
            document.getElementById("commentNameDiv").style.display = "none";
            document.getElementById("addFileButtonDiv").style.display = "block";
            document.getElementById("commentCsvDiv").style.display = "block";
            document.getElementById("commentAdfGemlDiv").style.display = "none";
            document.getElementById("commentAllFormatsDiv").style.display = "none";
        } else if (type == "Agilent Gene Expression"){
            document.getElementById("platformNameDiv").style.display = "block";
            document.getElementById("platformName").value = "";
            document.getElementById("platformChannelTypeDiv").style.display = "block";
            document.getElementById("platformChannelType").value = "Two-Color";
            document.getElementById("commentNameDiv").style.display = "block";
            document.getElementById("addFileButtonDiv").style.display = "none";
            document.getElementById("commentCsvDiv").style.display = "none";
            document.getElementById("commentAdfGemlDiv").style.display = "none";
            document.getElementById("commentAllFormatsDiv").style.display = "block";
        } else if (type == "Agilent Copy Number"){
            document.getElementById("platformNameDiv").style.display = "block";
            document.getElementById("platformName").value = "";
            document.getElementById("platformChannelTypeDiv").style.display = "block";
            document.getElementById("platformChannelType").value = "Two-Color";
            document.getElementById("commentNameDiv").style.display = "block";
            document.getElementById("addFileButtonDiv").style.display = "none";
            document.getElementById("commentCsvDiv").style.display = "none";
            document.getElementById("commentAdfGemlDiv").style.display = "block";
            document.getElementById("commentAllFormatsDiv").style.display = "none";
        }
    }
    
    function setSelectedAction(selectAction, type) {
        document.managePlatformForm.selectedAction.value = selectAction;
        if (selectAction == "createPlatform" && type == "Affymetrix SNP" 
            && document.getElementById("platformFile").value != "") {
        	if (confirm("The annotation file must be added to the list of file(s) selected\n"
                + "or it will not be included in the platform creation.")) {
        		showBusyDialog();
        		return true;
        	} else {
        	    return false;
        	}
        }
        showBusyDialog();
        return true;
    }
    
    function submitAction(selectAction, id) {
        document.statusTableForm.selectedAction.value = selectAction;
        document.statusTableForm.platformConfigurationId.value = id;
        if (selectAction == 'delete') {
            if (confirm('This platform will be permanently deleted.')) {
                showBusyDialog();
                document.statusTableForm.submit();
            }
        } else {
            if (document.getElementById("platformType" + id) != null) {
                document.statusTableForm.selectedPlatformType.value = document.getElementById("platformType" + id).value;
            }
            if (document.getElementById("platformChannelType" + id) != null) {
                document.statusTableForm.selectedPlatformChannelType.value = document.getElementById("platformChannelType" + id).value;
            }
            document.statusTableForm.submit();
        }
    }
    
</script>

    <h1><s:property value="#subTitleText" /></h1>
    
    <p>Click <strong>Create Platform</strong> to upload a new platform or view existing platforms.</p>
    <div class="form_wrapper_outer">
 
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Create a New Platform</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">    

                    <s:actionerror/>
                    <s:form id="managePlatformForm" name="managePlatformForm" method="post" enctype="multipart/form-data" theme="css_xhtml">
                        <s:hidden name="selectedAction" />
                        <s:select name="platformType" label="Platform Type"
                            list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum@getValuesToDisplay()"
                            onchange="CheckPlatformType(this.form.platformType.value);" theme="css_xhtml" /><br>
                        <s:div id="platformChannelTypeDiv" cssStyle="%{platformChannelTypeDisplay}">
                            <s:select id="platformChannelType" name="platformChannelType" label="Platform Channel Type"
                                list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformChannelTypeEnum@getValuesToDisplay()"
                                theme="css_xhtml" /><br>
                        </s:div>
                        <s:div id="platformNameDiv" cssStyle="%{platformNameDisplay}">
                            <s:textfield id="platformName" name="platformName" label="Platform Name" size="50"
                                theme="css_xhtml" /><br>
                        </s:div>
                        <s:div id="commentNameDiv" cssClass="inlinehelp_form_element" cssStyle="%{adfGemlFileDisplay}">
                            <span class="wwlbl">(not required for GEML annotation file)</span>
                            <span class="wwctrl"></span>
                        </s:div>
                        <s:file id="platformFile" name="platformFile" label="Annotation File" />
                        <s:div id="commentAdfGemlDiv" cssClass="inlinehelp_form_element" cssStyle="%{adfGemlFileDisplay}">
                            <span class="wwlbl">(adf & GEML xml file format)</span>
                            <span class="wwctrl"></span>
                        </s:div>
                        <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element" cssStyle="%{csvlFileDisplay}">
                            <span class="wwlbl">(csv file format)</span>
                            <span class="wwctrl"></span>
                        </s:div>
                        <s:div id="commentAllFormatsDiv" cssClass="inlinehelp_form_element" cssStyle="%{adfGemlFileDisplay}">
                            <span class="wwlbl">(tab-delimited csv/txt/tsv, adf, or GEML xml file format)</span>
                            <span class="wwctrl"></span>
                        </s:div>
                        <s:div id="addFileButtonDiv" cssStyle="%{addButtonDisplay}">
                            <div class="wwlbl"><label class="label">&nbsp</label></div><br>
                            <div class="wwctrl">
                            <s:submit id="addFileButton" name="addFileButton" value="Add Annotation File" align="center"
                                action="addAnnotationFile" onclick="setSelectedAction('addAnnotationFile')" theme="css_xhtml" />
                            </div><br>
                            <s:textarea label="Additional Annotation File(s) Selected" name="platformForm.fileNames"
                                disabled="true" rows="3" cols="50" theme="css_xhtml" /><br>
                        </s:div>
                        <div class="wwgrp">
                        <div class="wwlbl"><label class="label">&nbsp</label></div>
                        <div class="wwctrl"><s:submit value="Create Platform" align="center" action="createPlatform"
                            onclick="return setSelectedAction('createPlatform', this.form.platformType.value);" theme="css_xhtml" /></div>
                        </div><br>
                        </s:form>
                </td>
            </tr>
    </table>
    
    <table class="form_wrapper_table">
        <tr>
            <th class="title" style="height: 2.5em;">Existing Platforms<span id="platformLoader">
                 <img src="images/ajax-loader.gif"/></span></th>
            <th class="alignright">&nbsp;</th>
        </tr>
        <tr>
            <td colspan="2" style="padding: 5px;">                         
                        
                        <table class="data">
                            <tr>
                                <th>Platform Name</th>
                                <th>Platform Type</th>
                                <th>Platform Channel Type</th>
                                <th>Vendor</th>
                                <th>Array Name(s)</th>
                                <th>Status</th>
                                <th>Status Description</th>
                                <th>Action</th>
                            </tr>
                            <s:form id="statusTableForm" action="updatePlatform" theme="simple">
                                <s:hidden name="selectedAction" />
                                <s:hidden name="platformConfigurationId" />
                                <s:hidden name="selectedPlatformType" />
                                <s:hidden name="selectedPlatformChannelType" />
                                <tbody id="platformDeploymentJobStatusTable" />
                            </s:form>
                        </table>

                </td>
            </tr>
    </table>            
    </div>
</div>

<div class="clear"><br />
</div>
