<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Edit Genomic Data Source</h1>
    <s:form>
        <table>
            <tr>
                <th>caArray Server Hostname</th>
                <td><s:textfield name="genomicDataSource.serverProfile.hostname"/></td>
            </tr>
            <tr>
                <th>caArray server JNDI Port</th>
                <td><s:textfield name="genomicDataSource.serverProfile.port"/></td>
            </tr>
            <tr>
                <th>caArray Username</th>
                <td><s:textfield name="genomicDataSource.serverProfile.username"/></td>
            </tr>
            <tr>
                <th>caArray Password</th>
                <td><s:textfield name="genomicDataSource.serverProfile.password"/></td>
            </tr>
            <tr>
                <th>caArray Experiment Id</th>
                <td><s:textfield name="genomicDataSource.experimentIdentifier"/></td>
            </tr>
            <tr>
                <td colspan="2"><s:submit action="saveStudy" value="Save" /></td>
            </tr>
        </table>
    </s:form>
            
</div>

<div class="clear"><br /></div>
