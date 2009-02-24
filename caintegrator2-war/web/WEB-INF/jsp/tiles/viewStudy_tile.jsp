<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('view_study_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->       
    
    <h1>Content of Study: <s:property value="study.shortTitleText" /></h1>
   
    <table class="data">
        <s:iterator value="study.assignmentCollection" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
        </s:iterator>
        <td><s:property value="identifier" /></td>
        </tr>
    </table>
            
</div>

<div class="clear"><br /></div>
