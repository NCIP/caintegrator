<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Manage Studies</h1>
    <s:form>
        <table>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Action</th>
            </tr>
            <s:iterator value="studyConfigurations" status="status">
                <tr>
                    <td><s:property value="study.shortTitleText" /></td>
                    <td><s:property value="study.longTitleText" /></td>
                    <td>
                        <s:submit action="editStudy" value="Edit Study">
                            <s:param name="studyConfiguration" value="[0]" />
                        </s:submit>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </s:form>
            
</div>

<div class="clear"><br /></div>
