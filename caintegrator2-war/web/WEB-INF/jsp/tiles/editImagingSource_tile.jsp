<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><span class="help"><a href="#">&nbsp;</a></span></div>
    
    <!--/Page Help-->           
    
    <h1>Edit Imaging Data Source</h1>
    <h2>Enter a NCIA Data Source</h2>
    <h2>Enter Image Annotation Data From file</h2>  
    <table>
    <s:form action="addImagingFile" method="post" enctype="multipart/form-data" >  <!-- action="saveImagingSource" -->
    <tr>
        <td>
            <s:actionerror />
         </td>
    </tr>
    <tr>
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imageAnnotationConfiguration.id" />
        <s:textfield label=" NCIA Server Grid URL " name="hostname" />
        <s:textfield label=" NCIA Username " name="imageSourceConfiguration.serverProfile.username" />
        <s:password label=" NCIA Password " name="imageSourceConfiguration.serverProfile.password" />
        <s:textfield label=" Protocol Id " name="protocolId" />
        
    </tr>
      
    <tr>
        <th colspan="3">
    		<s:file name="imageAnnotationFile" label="Image Series Annotation File" />
    		<s:file name="imageClinicalMappingFile" label="Clinical/Imaging Mapping File" />
        	<s:submit value="Add" align="center" />
        </th>    
    </tr>
    
    </s:form>      
        
    </table>
</div>

<div class="clear"><br /></div>
