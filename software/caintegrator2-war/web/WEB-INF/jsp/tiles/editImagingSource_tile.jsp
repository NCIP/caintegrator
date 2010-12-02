<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript" src="/caintegrator2/common/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/caintegrator2/common/js/jquery.editable-select.js"></script>
<script type="text/javascript">

    jQuery.noConflict();

    jQuery(function() {
	  jQuery('.editable-select').editableSelect(
	    {
	      bg_iframe: true,
	      onSelect: false,
	      case_sensitive: false, // If set to true, the user has to type in an exact
	                             // match for the item to get highlighted
	      items_then_scroll: 10 // If there are more than 10 items, display a scrollbar
	    }
	  );
      document.getElementById("nbiaUrl").value = document.imagingSourceForm.serverProfileUrl.value;
	});

    function saveDatasource() {
    	document.imagingSourceForm.serverProfileUrl.value = document.getElementById("nbiaUrl").value; // The value isn't set without doing this.
        if (document.imagingSourceForm.imageSourceId.value != null && document.imagingSourceForm.imageSourceId.value != "") {
            if ((origUrl != document.getElementById("nbiaUrl").value) 
                || (origCollection != document.getElementById("nbiaCollectionName").value)
                || (origUsername != document.getElementById("nbiaUsername").value)
                || (origPassword != document.getElementById("nbiaPassword").value)) {
                if (confirm('Your Datasource connection parameters have changed.  All mappings will be deleted and the datasource reloaded.  Please click OK or Cancel.')) {
                	showBusyDialog();
                    document.imagingSourceForm.submit();                
                }
            } else if (origWebUrl != document.getElementById("nbiaWebUrl").value){
            	document.imagingSourceForm.action = 'updateImagingSource.action';
                document.imagingSourceForm.submit();
            } else {
            	showBusyDialog();
                document.imagingSourceForm.action = 'mapImagingSource.action';
                document.imagingSourceForm.submit();
            }
        } else {
        	showBusyDialog();
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

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>

    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_imaging_data_help')" class="help">
   </a>
    </div>

    <!--/Page Help-->
    
    <h1><s:property value="#subTitleText" /></h1>

    <p>Enter a NBIA Data Source and Image Mapping Data from a file and click <strong>Save</strong>.</p>
    <div class="form_wrapper_outer">
 
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Data Source</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">    

                <s:form id="imagingSourceForm" name="imagingSourceForm" 
                    action="saveImagingSource" method="post" enctype="multipart/form-data" >  <!-- action="saveImagingSource" -->
                <table>
                <tr>
                    <td colspan="2">
                        <s:actionerror />
                     </td>
                </tr>
                <s:hidden name="studyConfiguration.id" />
                <s:hidden name="cancelAction" />
                <s:hidden name="imageSourceConfiguration.id" id="imageSourceId"/>
                <s:hidden name="imageSourceConfiguration.serverProfile.url" id="serverProfileUrl" />
                <s:if test="%{studyConfiguration.hasLoadedClinicalDataSource()}">
                <tr>
                    
                    <s:if test="imageSourceConfiguration.statusDescription != null && imageSourceConfiguration.statusDescription.length() > 0">
                        <tr>
                            <td class="tdLabel" align="right">
                                <label class="label">Status Description:</label>
                            </td>
                            <td>
                                <s:property value="imageSourceConfiguration.statusDescription"/>
                            </td>
                        </tr>
                    </s:if>
                    
                    <s:select name="imageSourceConfiguration.serverProfile.url" id="nbiaUrl" accesskey="false"
                        headerKey="" headerValue="--Enter an NBIA Server Grid URL--"
                        list="nbiaServices" label=" NBIA Server Grid URL " required="true"
                        cssClass="editable-select" />
                    <s:textfield label=" NBIA Web URL " name="imageSourceConfiguration.serverProfile.webUrl" id="nbiaWebUrl" size="40" required="true"/>
                    <s:textfield label=" NBIA Username " name="imageSourceConfiguration.serverProfile.username" id="nbiaUsername" size="40"/>
                    <s:password label=" NBIA Password " name="imageSourceConfiguration.serverProfile.password" id="nbiaPassword" size="40"/>
                    <s:textfield required="true" label=" Collection Name " name="imageSourceConfiguration.collectionName" id="nbiaCollectionName" size="40"/>
                </tr>
                     <script type="text/javascript">
                        var origUrl = document.imagingSourceForm.serverProfileUrl.value;
                        var origWebUrl = document.imagingSourceForm.nbiaWebUrl.value;
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
                		<s:file name="imageClinicalMappingFile" label="Subject to Imaging Mapping File" size="40" disabled="true"/>
                </tr>
                </s:if>
                <tr> 
            	    <td></td>
            	    <td>
            	    <button type="button" 
            	            onclick="document.imagingSourceForm.action = 'cancelImagingSource.action';
            	            document.imagingSourceForm.cancelAction.value = 'true';
            	            document.imagingSourceForm.submit();"> Cancel 
            	    </button>
                    
                    <s:if test="%{studyConfiguration.hasLoadedClinicalDataSource()}">
            	       <button type="button" onclick="saveDatasource()"> Save </button>
                    </s:if>
            	    </td> 
                </tr>
                            </td>
                        </tr>
                </table>                        
                </s:form>                        
                </td>
            </tr>
    </table>
   
    <s:if test="imageSourceConfiguration.id != null">
                
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Image Mapping</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">                 
                
                    <table class="data">
                        <tr>
                            <th colspan="2">Unmapped Image Studies</th>
                        </tr>
                        <tr>
                            <th>NBIA Study Instance UID</th>
                            <th>NBIA Subject Identifier</th>
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
            	   
                    <br>
                    
                    <table class="data">
                        <tr>
                            <th colspan="3">Mapped Image Studies</th>
                        </tr>
                        <tr>
                            <th>NBIA Study Instance UID</th>
                            <th>NBIA Subject Identifier</th>
                            <th>caIntegrator Subject Identifier</th>
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
                
                </td>
            </tr>
    </table>
                </s:if>
                    
                </td>
            </tr>
    </table>
    </div>
</div>

<div class="clear"><br /></div>
