<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><span class="help"><a href="#">&nbsp;</a></span></div>
    
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
