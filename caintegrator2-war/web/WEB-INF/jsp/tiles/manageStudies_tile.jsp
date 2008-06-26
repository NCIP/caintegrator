<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content"><!--Page Help-->

<div class="pagehelp"><a href="#" class="help"></a></div>

<!--/Page Help-->

<h1>Manage Studies</h1>
<s:form>
    <table class="data">
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Action</th>
        </tr>
        <s:iterator value="studyConfigurations" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
                <td><s:property value="study.shortTitleText" /></td>
                <td><s:property value="study.longTitleText" /></td>
                <td>
                    <s:url id="editUrl" action="editStudy">
                        <s:param name="studyConfiguration.id" value="id" />
                    </s:url> 
                    <s:a href="%{editUrl}">Edit</s:a> 
                </td>
            </tr>
        </s:iterator>
    </table>
</s:form></div>

<div class="clear"><br />
</div>
