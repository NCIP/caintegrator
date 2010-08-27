<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<script type="text/javascript">

    function disableFormElement(obj)
    {
        var nameOfCallingElement = obj.name;
        var arrayOfElements=document.getElementsByName(nameOfCallingElement);
        
        for (var i = 0; i < arrayOfElements.length; i++){
            if (arrayOfElements[i].value == "Use AIM Data Service" &&
                   arrayOfElements[i].checked==true ) {
                document.getElementById("imageAnnotationFile").disabled = true;
                document.getElementById("createNewAnnotationDefinition").disabled = true;
                document.getElementById("aimUrl").disabled = false;
                document.getElementById("aimUsername").disabled = false;
                document.getElementById("aimPassword").disabled = false;
                break;
            } else {
                document.getElementById("imageAnnotationFile").disabled = false;
                document.getElementById("createNewAnnotationDefinition").disabled = false;
                document.getElementById("aimUrl").disabled = true;
                document.getElementById("aimUsername").disabled = true;
                document.getElementById("aimPassword").disabled = true;
            }
        } 
        
    }   

</script>
<div id="content">
                   
    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>    
   
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('define_fields_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->          
    
    <h1><s:property value="#subTitleText" /></h1>
    <p>Assign annotation definitions to data fields.</p>

    <div class="form_wrapper_outer">

    <table class="form_wrapper_table">

            <tr>
                <td colspan="2" style="padding: 5px;">
                    
                <s:form id="imagingSourceForm" name="imagingSourceForm" action="saveImagingSourceAnnotations"
                    method="post" enctype="multipart/form-data">
                <s:hidden name="studyConfiguration.id" />
                <s:hidden name="imageSourceConfiguration.id" />
                
                <s:if test="imageSourceConfiguration.imageAnnotationConfiguration == null">
                <table>
                    <s:radio name="uploadType" 
                        list="@gov.nih.nci.caintegrator2.application.study.ImageAnnotationUploadType@getStringValues()"
                        required="true" label="Select Annotation Upload Type:"
                        onclick="disableFormElement(this)"/>
                    <s:file id="imageAnnotationFile" name="imageAnnotationFile" label="Image Series Annotation File"
                        disabled="%{annotationFileDisable}" />
                    <s:checkbox name="createNewAnnotationDefinition" id="createNewAnnotationDefinition"
                        label="Create a new Annotation Definition if one is not found" labelposition="left" />
                    <s:select name="aimServerProfile.url" id="aimUrl" accesskey="false"
                        headerKey="" headerValue="--Enter an AIM Server Grid URL--"
                        list="aimServices" label=" AIM Server Grid URL " required="true" disabled="%{aimDisable}"
                        cssClass="editable-select" />
                    <s:textfield label=" AIM Username " name="aimServerProfile.username"
                        disabled="%{aimDisable}" id="aimUsername" size="40"/>
                    <s:password label=" AIM Password " name="aimServerProfile.password"
                        disabled="%{aimDisable}" id="aimPassword" size="40"/>
                    <tr> 
                    <td></td>
                    <td>
                    <button type="button" 
                            onclick="document.imagingSourceForm.action = 'cancelImagingSource.action';
                            document.imagingSourceForm.submit();"> Cancel 
                    </button>
                    <button type="button" 
                            onclick="document.imagingSourceForm.action = 'addImagingSourceAnnotations.action';
                            document.imagingSourceForm.submit();"> Add </button>
                    </td> 
                    </tr>
                </table>
                </s:if>
                
                <s:else>
                <table class="data">
                    <tr>
                    	<th>Annotation Group</th>
                        <th>Visible</th>
                        <th>Annotation Definition</th>
                        <th>Annotation Header from File</th>
                        <th colspan="3" />Data from File</th>
                    </tr>
                    <s:iterator value="displayableFields" status="columnIterator">
                        <s:if test="#columnIterator.odd == true">
                          <tr class="odd">
                        </s:if>
                        <s:else>
                          <tr class="even">
                        </s:else>          
                            <td>
                                <s:if test="%{fieldDescriptor != null}">
                                    <s:select name="displayableFields[%{#columnIterator.count - 1}].annotationGroupName" 
                                              list="selectableAnnotationGroups"
                                              listKey="name"
                                              listValue="name"
                                              theme="simple" />
                                </s:if>
                            </td>
                            <td>
                                <s:if test="%{fieldDescriptor != null}">
                                    <s:checkbox name="displayableFields[%{#columnIterator.count - 1}].fieldDescriptor.shownInBrowse"
                                        theme="simple" />
                                </s:if>
                            </td>
                            <td>
                            	<s:if test="%{fieldDescriptor.hasValidationErrors}">
                                    <font class="formErrorMsg">
                                </s:if>
                                <s:else>
                                    <font color="black">
                                </s:else>
                                <s:if test="%{identifierType}">
                                    Identifier
                                </s:if>
                                <s:elseif test="%{timepointType}">
                                    Timepoint
                                </s:elseif>
                                <s:elseif test="%{fieldDescriptor != null && fieldDescriptor.definition != null}">
                                    <s:property value="fieldDescriptor.definition.displayName"/> 
                                </s:elseif>
                                <s:url id="editImagingFieldDescriptor" action="editImagingFieldDescriptor" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="sourceId" value="imageSourceConfiguration.id" />
                                    <s:param name="fieldDescriptor.id" value="fieldDescriptor.id" />
                                </s:url>
                                </font>
                                <br>
                                <s:a href="%{editImagingFieldDescriptor}">
                                    <s:if test="%{identifierType || timepointType || (fieldDescriptor != null && fieldDescriptor.definition != null) }">
                                        Change Assignment
                                    </s:if>
                                    <s:else>
                                        Assign Annotation Definition
                                    </s:else>
                                </s:a> 
                            </td>
                            <td><s:property value="fieldDescriptor.name" /></td>
                            <td><s:if test="%{dataValues.size > 0}"><s:property value="dataValues[0]" /></s:if></td>
                            <td><s:if test="%{dataValues.size > 1}"><s:property value="dataValues[1]" /></s:if></td>
                            <td><s:if test="%{dataValues.size > 2}"><s:property value="dataValues[2]" /></s:if></td>
                        </tr>
                    </s:iterator>
                </table>
                
                <tr>
                    <td colspan="2"><div align="center">
                    <button type="button" 
                            onclick="document.imagingSourceForm.action = 'cancelImagingSource.action';
                            document.imagingSourceForm.submit();"> Cancel 
                    </button>
                    
                    <button type="button" onclick="document.imagingSourceForm.submit();">Save</button>
                    </div></td>
                </tr>
                
                </s:else>
                
                </s:form>
                </td>
            </tr>
        </table>
    </div>
</div>

<div class="clear"><br /></div>
