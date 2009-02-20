<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">

    <!--Page Help-->
    
    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('manage_study_help')" class="help">
    (draft)</a>
    </div>
    
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
                    <s:url id="deleteUrl" action="deleteStudy">
                        <s:param name="studyConfiguration.id" value="id" />
                    </s:url> 
                    <s:a href="%{editUrl}">Edit </s:a> | 
                    <s:a href="%{deleteUrl}" onclick="return confirm('This study will be permanently deleted.')">Delete</s:a> 
                </td>
            </tr>
        </s:iterator>
    </table>
</s:form></div>

<div class="clear"><br />
</div>
