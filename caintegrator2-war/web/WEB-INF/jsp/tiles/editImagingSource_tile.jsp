<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Edit Imaging Data Source</h1>
    <h2>Enter a NCIA Data Source</h2>
    <s:form>  <!-- action="saveImagingSource" -->
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="genomicSource.id" />
        <s:textfield label=" NCIA Server Grid URL " name="imagingSource.serverProfile.hostname" />
        <s:textfield label=" NCIA Username " name="imagingSource.serverProfile.port" />
        <s:password label=" NCIA Password " name="imagingSource.serverProfile.username" />
        <s:textfield label=" Protocol Id " name="imagingSource.serverProfile.password" />
        <s:submit value=" Verify Connection " />
    </s:form>
    
    <h2>Enter Image Annotation Data From file</h2>  
    <table>
    <tr></tr>
    <tr>
        <th colspan="3">
    		<s:form action="addImagingFile" method="post" enctype="multipart/form-data">
        		<s:hidden name="studyConfiguration.id"  />
        		<s:file name="imagingFile" label="Tab-delimited Text File" />
        		<s:submit value="Add" align="center" />
    		</s:form>      
        </th>
    </tr>
    </table>
</div>

<div class="clear"><br /></div>
