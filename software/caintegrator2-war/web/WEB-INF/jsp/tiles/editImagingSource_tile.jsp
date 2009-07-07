<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
    
    function saveDatasource() {
        if (document.imagingSourceForm.imageSourceId.value != null && document.imagingSourceForm.imageSourceId.value != "") {
            if ((origUrl != document.getElementById("nbiaUrl").value) 
                || (origCollection != document.getElementById("nbiaCollectionName").value)
                || (origUsername != document.getElementById("nbiaUsername").value)
                || (origPassword != document.getElementById("nbiaPassword").value)) {
                if (confirm('Your Datasource connection parameters have changed.  All mappings will be deleted and the datasource reloaded.  Please click OK or Cancel.')) {
                    document.imagingSourceForm.submit();                
                }
            } else {
                document.imagingSourceForm.action = 'mapImagingSource.action';
                document.imagingSourceForm.submit();
            }
        } else {
            document.imagingSourceForm.submit();
        }
    
    }
    
    // 
    // disableFormElement - this script is called by an html element and
    //                      is used to disable a second html element.  If
    //                      the value of "obj" matches "valueToBeMatched",
    //                      then "elementToDisable" is disabled.
    //
    // obj - the html element that is asking for something to be disabled.
    // valueToBeMatched - the value of the html element that must be matched
    //                    for the disabling to take place.
    // formContainingElementToDisable- the name of the form containing the
    //                                 element that is to be disabled.
    // elementToDisable - the element to be disabled.
    //
    function disableFormElement(obj, valueToBeMatched, formContainingElementToDisable, elementToDisable)
    {
        var nameOfCallingElement = obj.name;
        var arrayOfElements=document.getElementsByName(nameOfCallingElement);
        var elementIdToBeDisabled = formContainingElementToDisable.name + '_' + elementToDisable.name;
        
        for (var i = 0; i < arrayOfElements.length; i++){
            if (arrayOfElements[i].value == valueToBeMatched &&
                   arrayOfElements[i].checked==true ) {
                 document.getElementById(elementIdToBeDisabled).value = "";  
                 document.getElementById(elementIdToBeDisabled).disabled = true;
                 break;
            } else {
                document.getElementById(elementIdToBeDisabled).disabled = false;
            }
        } 
        
    }   

</script>            

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
    <s:form id="imagingSourceForm" name="imagingSourceForm" 
        action="saveImagingSource" method="post" enctype="multipart/form-data" >  <!-- action="saveImagingSource" -->
    <tr>
        <td>
            <s:actionerror />
         </td>
    </tr>
    <tr>
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imageSourceConfiguration.id" id="imageSourceId"/>
        <s:textfield required="true" label=" NBIA Server Grid URL " name="imageSourceConfiguration.serverProfile.url" id="nbiaUrl" />
        <s:textfield label=" NBIA Username " name="imageSourceConfiguration.serverProfile.username" id="nbiaUsername"/>
        <s:password label=" NBIA Password " name="imageSourceConfiguration.serverProfile.password" id="nbiaPassword"/>
        <s:textfield required="true" label=" Collection Name " name="imageSourceConfiguration.collectionName" id="nbiaCollectionName"/>

    </tr>
         <script type="text/javascript">
            var origUrl = document.imagingSourceForm.nbiaUrl.value;
            var origCollection = document.imagingSourceForm.nbiaCollectionName.value;
            var origUsername = document.imagingSourceForm.nbiaUsername.value;
            var origPassword = document.imagingSourceForm.nbiaPassword.value;
        </script>
    <tr>
    		<!-- 
    		  <s:file name="imageAnnotationFile" label="Image Series Annotation File" /> 
    		-->
    		<s:if test="imageSourceConfiguration.mappingFileName != null">
    		<tr>
    		    <td class="tdLabel">
    		    <label class="label">Current Mappings:</label>
    		    </td>
    		    <td> <s:property value="imageSourceConfiguration.mappingFileName"/> </td>
   		    </tr>
    		</s:if>
    		<s:radio name="mappingType" 
                list="@gov.nih.nci.caintegrator2.application.study.ImageDataSourceMappingTypeEnum@getStringValues()"
                required="true" label="Select Mapping File Type:"
                onclick="disableFormElement(this, 'Auto (No File Required)', document.imagingSourceForm, imageClinicalMappingFile)"/>
    		<s:file name="imageClinicalMappingFile" label="Clinical/Imaging Mapping File" size="40" disabled="true"/>
    </tr>
    <tr> 
	    <td></td>
	    <td>
	    <button type="button" 
	            onclick="document.imagingSourceForm.action = 'cancelImagingSource.action';
	            document.imagingSourceForm.submit();"> Cancel 
	    </button>
	    <button type="button" onclick="saveDatasource()"> Save </button>
	    </td> 
    </tr>
    
    <s:if test="imageSourceConfiguration.id != null">
        <table class="data">
	        <tr>
	            <th colspan="2">Unmapped Image Series Acquisitions</th>
	        </tr>
	        <tr>
	            <th>Image Series Acquisition Identifier</th>
	            <th>Patient Identifier</th>
	        </tr>
	        <s:iterator value="imageSourceConfiguration.unmappedImageSeriesAcquisitions" status="status">
	            <s:if test="#status.odd == true">
	              <tr class="odd">
	            </s:if>
	            <s:else>
	              <tr class="even">
	            </s:else>            
	            <td><s:property value="identifier" /></td>
	            <td><s:property value="patientIdentifier" /></td>
	            </tr>
	        </s:iterator>
	   </table>
	   
        <table class="data">
            <tr>
                <th colspan="3">Mapped Image Series Acquisitions</th>
            </tr>
            <tr>
                <th>Image Series Acquisition Identifier</th>
                <th>Patient Identifier</th>
                <th>CaIntegrator Subject Identifier</th>
            </tr>
            <s:iterator value="imageSourceConfiguration.mappedImageSeriesAcquisitions" status="status">
                <s:if test="#status.odd == true">
                  <tr class="odd">
                </s:if>
                <s:else>
                  <tr class="even">
                </s:else>            
                <td><s:property value="identifier" /></td>
                <td><s:property value="patientIdentifier" /></td>
                <td><s:property value="assignment.identifier" /></td>
                </tr>
            </s:iterator>
       </table>
    
    </s:if>
    
    </s:form>      
        
    </table>
</div>

<div class="clear"><br /></div>
