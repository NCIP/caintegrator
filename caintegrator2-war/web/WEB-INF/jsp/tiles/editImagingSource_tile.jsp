<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Edit Imaging Data Source</h1>
    <h2>Enter a NCIA Data Source</h2>
    <h2>Enter Image Annotation Data From file</h2>  
    <table>
    <tr></tr>
    <tr>
    <s:form action="addImagingFile" method="post" enctype="multipart/form-data" >  <!-- action="saveImagingSource" -->
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imagingSource.id" />
        <s:textfield label=" NCIA Server Grid URL " name="hostname" />
        <s:textfield label=" NCIA Username " name="imageSource.serverProfile.username" />
        <s:password label=" NCIA Password " name="imageSource.serverProfile.password" />
        <s:textfield label=" Protocol Id " name="protocolId" />
        
    </tr>
      
    <tr>
        <th colspan="3">
    		<s:file name="imagingFile" label="Image Series Annotation File" />
    		<s:file name="imageClinicalMappingFile" label="Clinical/Imaging Mapping File" />
        	<s:submit value="Add" align="center" />
        </th>    
    </tr>
    
    </s:form>      
        
    </table>
</div>

<div class="clear"><br /></div>
