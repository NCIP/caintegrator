<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_imaging_data_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->
    
    <h1>Edit Imaging Data Source</h1>
    <h2>Enter a NBIA Data Source and Image Mapping Data From file</h2>
    <br>
 
    <table>
    <s:form action="addImagingFile" method="post" enctype="multipart/form-data" >  <!-- action="saveImagingSource" -->
    <tr>
        <td>
            <s:actionerror />
         </td>
    </tr>
    <tr>
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imageSourceConfiguration.id" />
        <s:textfield label=" NBIA Server Grid URL " name="url" />
        <s:textfield label=" NBIA Username " name="imageSourceConfiguration.serverProfile.username" />
        <s:password label=" NBIA Password " name="imageSourceConfiguration.serverProfile.password" />
        <s:textfield label=" Collection Name " name="collectionName" />
        
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
