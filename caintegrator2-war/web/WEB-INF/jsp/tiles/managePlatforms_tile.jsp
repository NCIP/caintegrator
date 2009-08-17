<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content" >

<!--Page Help-->

<div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('manage_platforms_help')" class="help">
(draft)</a>
</div>

<!--/Page Help-->
    
<script type="text/javascript">
    function CheckPlatformType(type) {
        if (type == "Affymetrix Gene Expression") {
            document.getElementById("platformNameDiv").style.display = "none";
            document.getElementById("platformName").value = "N/A";
            document.getElementById("addFileButtonDiv").style.display = "none";
        } else if (type == "Affymetrix SNP"){
            document.getElementById("platformNameDiv").style.display = "block";
            document.getElementById("platformName").value = "";
            document.getElementById("addFileButtonDiv").style.display = "block";
        } else if (type == "Agilent Gene Expression"){
            document.getElementById("platformNameDiv").style.display = "block";
            document.getElementById("platformName").value = "";
            document.getElementById("addFileButtonDiv").style.display = "none";
        } else if (type == "Agilent Copy Number"){
            document.getElementById("platformNameDiv").style.display = "block";
            document.getElementById("platformName").value = "";
            document.getElementById("addFileButtonDiv").style.display = "none";
        }
    }
    
    function setSelectedAction(selectAction, type) {
        if (selectAction == "createPlatform" && type == "Affymetrix SNP" 
            && document.getElementById("platformFile").value != "") {
            return confirm ("The annotation file must be added to the list of file(s) selected\n"
                + "or it will not be included in the platform creation.");
        }
        document.managePlatformForm.selectedAction.value = selectAction;
        return true;
    }
</script>

<h1>Manage Platforms</h1>
<s:actionerror/>
<s:form id="managePlatformForm" name="managePlatformForm" method="post" enctype="multipart/form-data" theme="css_xhtml">
    <s:hidden name="selectedAction" />
    <s:select name="platformType" label="Platform Type"
        list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum@getValuesToDisplay()"
        onchange="CheckPlatformType(this.form.platformType.value);" theme="css_xhtml" /><br>
    <s:div id="platformNameDiv" cssStyle="%{platformNameDisabled}">
        <s:textfield id="platformName" name="platformName" label="Platform Name (For NON-GEML xml file)"
            theme="css_xhtml" /><br>
    </s:div>
    <s:file id="platformFile" name="platformFile" label="Annotation File" />
    <s:div id="addFileButtonDiv" cssStyle="%{addButtonDisabled}">
        <div class="wwlbl"><label class="label">&nbsp</label></div><br>
        <div class="wwctrl">
        <s:submit id="addFileButton" name="addFileButton" value="Add Annotation File" align="center"
            action="addAnnotationFile" onclick="setSelectedAction('addAnnotationFile')" theme="css_xhtml" />
        </div><br>
        <s:textarea label="Additional Annotation File(s) Selected" name="platformForm.fileNames"
            rows="3" cols="50" theme="css_xhtml" /><br>
    </s:div>
    <div class="wwgrp">
    <div class="wwlbl"><label class="label">&nbsp</label></div>
    <div class="wwctrl"><s:submit value="Create Platform" align="center" action="createPlatform"
        onclick="return setSelectedAction('createPlatform', this.form.platformType.value);" theme="css_xhtml" /></div>
    </div><br>
</s:form>
    <table class="data">
        <tr>
            <th>Platform Name</th>
            <th>Vendor</th>
            <th>Array Name(s)</th>
            <th>Action</th>
        </tr>
        <s:iterator value="displayablePlatforms" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
                <td><s:property value="platform.name" /></td>
                <td><s:property value="platform.vendor" /></td>
                <td><s:property value="displayableArrayNames" /></td>
                <td>
                    <s:if test="!inUse">
                        <s:url id="deletePlatform" action="deletePlatform">
                            <s:param name="platformName" value="platform.name" />
                        </s:url> 
                        <s:a href="%{deletePlatform}" onclick="return confirm('This platform will be permanently deleted.')">
                            Delete
                        </s:a>
                    </s:if>
                    <s:else>None</s:else>
                </td>
            </tr>
        </s:iterator>
    </table>
</div>

<div class="clear"><br />
</div>
