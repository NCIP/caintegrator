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
            document.getElementById("platformName").value = "N/A";
            document.getElementById("platformName").disabled = true;
            document.getElementById("addFileButton").disabled = true;
        } else if (type == "Affymetrix SNP"){
            document.getElementById("platformName").value = "";
            document.getElementById("platformName").disabled = false;
            document.getElementById("addFileButton").disabled = false;
        } else if (type == "Agilent Gene Expression"){
            document.getElementById("platformName").value = "";
            document.getElementById("platformName").disabled = false;
            document.getElementById("addFileButton").disabled = true;
        } else if (type == "Agilent Copy Number"){
            document.getElementById("platformName").value = "";
            document.getElementById("platformName").disabled = false;
            document.getElementById("addFileButton").disabled = true;
        }
    }
    
    function setSelectedAction(selectAction) {
        document.managePlatformForm.selectedAction.value = selectAction;
    }
</script>

<h1>Manage Platforms</h1>
<s:form id="managePlatformForm" name="managePlatformForm" method="post" enctype="multipart/form-data">
    <s:hidden name="selectedAction" />
    <s:actionerror/>
    <table class="data">
        <tr>
            <th colspan="2">
                <s:select name="platformType" label="Platform Type"
                    list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum@getValuesToDisplay()"
                    onchange="CheckPlatformType(this.form.platformType.value);" />
                <s:textfield id="platformName" name="platformName" label="Platform Name (For NON-GEML xml file)"
                    disabled="platformNameDisabled" />
                <s:file name="platformFile" label="Annotation File" />
                <s:submit id="addFileButton" name="addFileButton" value="Add Annotation File" align="center" action="addAnnotationFile"
                    onclick="setSelectedAction('addAnnotationFile')" disabled="addButtonDisabled"/>
                <s:textarea label="Annotation File(s) Selected" name="platformForm.fileNames" rows="3" cols="50" disabled="true"/>
                <s:submit value="Create Platform" align="center" action="createPlatform"
                    onclick="setSelectedAction('createPlatform')"/>
            </th>    
        </tr>
        <tr>
            <th>Name</th>
            <th>Vendor</th>
            <th>Reporter List</th>
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
                <td><s:property value="platform.reporterListListing" /></td>
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
</s:form></div>

<div class="clear"><br />
</div>
