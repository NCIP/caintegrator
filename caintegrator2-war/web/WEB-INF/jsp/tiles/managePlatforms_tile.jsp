<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">

<!--Page Help-->

<div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('manage_platforms_help')" class="help">
(draft)</a>
</div>

<!--/Page Help-->

<h1>Manage Platforms</h1>
<s:form action="addPlatform" method="post" enctype="multipart/form-data" >
    <table class="data">
        <tr>
            <th colspan="2">
                <s:file name="platformFile" label="Affymetrix Annotation File" />
                <s:submit value="Add" align="center" />
            </th>    
        </tr>
        <tr>
            <th>Name</th>
            <th>Vendor</th>
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
            </tr>
        </s:iterator>
    </table>
</s:form></div>

<div class="clear"><br />
</div>
