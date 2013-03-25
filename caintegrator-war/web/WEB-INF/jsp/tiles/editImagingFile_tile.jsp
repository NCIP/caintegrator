<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
    // This function is called at body onload because IE 7 puts the footer in the middle of the page
    // sporadically, and this toggles it to go to the proper position.
    function initializeJsp() {
        var tbody = document.getElementById('annotationUploadTypeDiv');
        tbody.style.display = "none";
        tbody.style.display = "";
    }
</script>
<div id="content">
                   
    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>    
   
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('CIDnAg', 'id-2-CreatingaNewStudy-DefineFieldsPageforEditingAnnotations')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->          
    
    <h1><s:property value="#subTitleText" /></h1>

    <div class="form_wrapper_outer">

    <table class="form_wrapper_table">

            <tr>
                <td colspan="2" style="padding: 5px;">
                    
                <s:form id="imagingSourceForm" name="imagingSourceForm" action="saveImagingSourceAnnotations"
                    method="post" enctype="multipart/form-data" theme="css_xhtml">
                <s:token />
                <s:hidden name="studyConfiguration.id" />
                <s:hidden name="imageSourceConfiguration.id" />
                
                <s:if test="imageSourceConfiguration.imageAnnotationConfiguration == null">
                <s:div>
                    <br />
                    <s:div cssStyle="padding: 1em 0 0 0;" id="annotationUploadTypeDiv">
                        <s:div cssClass="wwlbl"><label class="label"><span class="required">*</span>Select Annotation Upload Type: </label></s:div>
                        <s:div>
                            <s:radio theme="css_xhtml" name="uploadType" list="#{'Upload Annotation File':'Upload Annotation File'}" onclick="document.getElementById('aimInputParams').style.display = 'none'; document.getElementById('fileInputParams').style.display = 'block';" />
                            <s:radio theme="css_xhtml" name="uploadType" list="#{'Use AIM Data Service':'Use AIM Data Service'}" onclick="document.getElementById('fileInputParams').style.display = 'none'; document.getElementById('aimInputParams').style.display = 'block';" />
                        </s:div>
                    </s:div>
                    <br />
                
                    <s:div id="fileInputParams" cssStyle="%{fileInputCssStyle}">
	                    <s:file id="imageAnnotationFile" name="imageAnnotationFile" label="Image Series Annotation File"/>
	                    <br/>
                        <s:checkbox name="createNewAnnotationDefinition" id="createNewAnnotationDefinition"
                                label="Create a new Annotation Definition if one is not found" labelposition="left" />
                        <br/>
                    </s:div>
                    <s:div id="aimInputParams" cssStyle="%{aimInputCssStyle}">
	                    <s:select name="aimServerProfile.url" id="aimUrl" accesskey="false"
	                        headerKey="" headerValue="--Enter an AIM Server Grid URL--"
	                        list="aimServices" label=" AIM Server Grid URL " required="true"
	                        cssClass="editable-select" />
	                    <br/>
	                    <s:textfield label=" AIM Username " name="aimServerProfile.username"
	                        id="aimUsername" size="40"/>
	                    <br/>
	                    <s:password label=" AIM Password " name="aimServerProfile.password"
	                        id="aimPassword" size="40"/>
	                    <br/>
                    </s:div>
                    <div style="position: relative; white-space: nowrap;">
                    <div class="wwlbl" id="wwlbl_webServiceUrl"><label class="label" for="editSurvivalValueDefinition_survivalDefinitionFormValues_lastFollowupDateId"></label>
                    </div>
                    <div class="wwctrl" id="wwlbl_webServiceUrl">
		                   <button type="button" 
		                           onclick="document.imagingSourceForm.action = 'cancelImagingSource.action';
		                           document.imagingSourceForm.submit();"> Cancel 
		                   </button>
		                   <button type="button" 
		                           onclick="document.imagingSourceForm.action = 'addImagingSourceAnnotations.action';
		                           document.imagingSourceForm.submit();"> Add </button>
                    </div>
                    </div>
                </s:div>
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
