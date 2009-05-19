<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">

<!--Page Help-->

<div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('manage_platforms_help')" class="help">
(draft)</a>
</div>

<!--/Page Help-->
    
<script type="text/javascript">
    function CheckPlatformType(type) {
        if (type == "Affymetrix GeneEx") {
            document.getElementById("platformName").value = "N/A";
            document.getElementById("platformName").disabled = true;
            document.getElementById("platformFile2").disabled = true;
        } else if (type == "Affymetrix SNP"){
            document.getElementById("platformName").value = "";
            document.getElementById("platformName").disabled = false;
            document.getElementById("platformFile2").disabled = false;
        } else if (type == "Agilent GeneEx"){
            document.getElementById("platformName").value = "";
            document.getElementById("platformName").disabled = false;
            document.getElementById("platformFile2").disabled = true;
        }
    }
</script>

<h1>Manage Platforms</h1>
<s:form action="addPlatform" method="post" enctype="multipart/form-data" >
    <table class="data">
        <tr>
            <th colspan="2">
                <s:select name="platformType" label="Platform Type"
                    list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum@getValuesToDisplay()"
                    value="Affymetrix" onchange="CheckPlatformType(this.form.platformType.value);" />
                <s:textfield id="platformName" name="platformName" label="Platform Name"
                    disabled="true" />
                <s:file name="platformFile" label="Annotation File" />
                <s:file id="platformFile2" name="platformFile2" label="Second Annotation File" disabled="true"/>
                <s:hidden name="selectedAction" value="addPlatform" />
                <s:submit value="Add" align="center" />
            </th>    
        </tr>
        <tr>
            <th>Name</th>
            <th>Vendor</th>
            <th>Reporter List</th>
        </tr>
        <s:iterator value="platforms" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
                <td><s:property value="name" /></td>
                <td><s:property value="vendor" /></td>
                <td><s:property value="reporterListListing" /></td>
            </tr>
        </s:iterator>
    </table>
</s:form></div>

<div class="clear"><br />
</div>
