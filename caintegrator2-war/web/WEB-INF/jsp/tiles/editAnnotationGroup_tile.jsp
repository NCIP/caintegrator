<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">                      

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>

    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('add_annotation_group_help')" class="help">
   </a>
    </div>

    <!--/Page Help-->
    
    <h1><s:property value="#subTitleText" /></h1>

    <p>Enter data for the Annotation Group and optionally upload a Group Definition Source File and click <strong>Save</strong>.</p>
    <div class="form_wrapper_outer">
 
    <table class="form_wrapper_table">
        <tr>
            <th class="title" style="height: 2.5em;">Annotation Group</th>
            <th class="alignright">&nbsp;</th>
        </tr>
        <tr>
            <td colspan="2" style="padding: 5px;">    

                <s:form id="annotationGroupForm" name="annotationGroupForm" 
                    action="saveAnnotationGroup" method="post" enctype="multipart/form-data" >
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="selectedAction" />
                    <s:hidden name="annotationGroup.id" />
                <table>
                    <tr><td> <s:actionerror /></td></tr>
                    <tr><td>
                        <s:textfield label="Group Name" name="groupName" size="40"
                            required="true" theme="css_xhtml"/>
                    </td></tr>
                    <tr><td>
                        <s:textarea label="Description" name="annotationGroup.description" cols="40" rows="3" theme="css_xhtml"/>
                    </td></tr>
                    <s:if test="!existingGroup">
                        <tr><td>
                            <s:file id="annotationGroupFile" name="annotationGroupFile" label="Upload File" theme="css_xhtml"/>
                            <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element" cssStyle="%{csvlFileDisplay}">
                                <span class="wwlbl">(csv file format)</span><span class="wwctrl"></span>
                            </s:div>
                        </td></tr>
                    </s:if>
                </table>
                <div><center>
                    <button type="button" onclick="document.annotationGroupForm.selectedAction.value = 'cancel';
            	        document.annotationGroupForm.submit();">Cancel</button>
            	    <button type="button" onclick="document.annotationGroupForm.selectedAction.value = 'save';
                        document.annotationGroupForm.submit();">Save</button>
                </center></div>
                </s:form> 
            </td>
        </tr>
                </table>
                </td>
            </tr>
        </table>
        <s:if test="existingGroup">
        <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Current Annotation Field Descriptors</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;"> 
	            <s:form action="saveFieldDescriptorsForGroup" method="post" enctype="multipart/form-data">
	                <s:hidden name="studyConfiguration.id" />
	                <s:hidden name="annotationGroup.id" />
	                <table class="data">
	                    <tr>
	                        <th>Annotation Group</th>
	                        <th>Visible</th>
	                        <th>Entity Type</th>
	                        <th>Annotation Definition</th>
	                        <th>Annotation Header from File</th>
	                    </tr>
	                    <s:iterator value="displayableFields" status="columnIterator">
	                        <s:if test="#columnIterator.odd == true">
	                          <tr class="odd">
	                        </s:if>
	                        <s:else>
	                          <tr class="even">
	                        </s:else>         
	                            <td>
	                                <s:select name="displayableFields[%{#columnIterator.count - 1}].annotationGroupName" 
	                                              list="selectableAnnotationGroups"
	                                              listKey="name"
	                                              listValue="name"
	                                              theme="simple" />
	                            </td>
	                            <td>
	                                <s:checkbox name="displayableFields[%{#columnIterator.count - 1}].fieldDescriptor.shownInBrowse"
	                                        theme="simple" disabled="false"/>
	                            </td>
	                            <td>
                                    <s:property value="fieldDescriptor.annotationEntityType"/>
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
	                                <s:elseif test="%{fieldDescriptor.definition != null}">
	                                    <s:property value="fieldDescriptor.definition.displayName"/> 
	                                </s:elseif>
	                                <s:url id="editGroupFieldDescriptor" action="editGroupFieldDescriptor" includeParams="none">
	                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
	                                    <s:param name="groupId" value="annotationGroup.id" />
	                                    <s:param name="fieldDescriptor.id" value="fieldDescriptor.id" />
	                                </s:url>
	                                </font>
                                	<br>
	                                <s:a href="%{editGroupFieldDescriptor}">
	                                    <s:if test="%{identifierType || timepointType || (fieldDescriptor != null && fieldDescriptor.definition != null) }">
	                                        Change Assignment
	                                    </s:if>
	                                    <s:else>
	                                        Assign Annotation Definition
	                                    </s:else>
	                                </s:a> 
	                            </td>
	                            <td><s:property value="fieldDescriptor.name" /></td>
	                        </tr>
	                    </s:iterator>
	                </table>
	                <s:submit value="Update Annotations" />
	                </s:form>
                </td>
            </tr>
        </table>
        </s:if> 
     
    </div>
</div>

<div class="clear"><br /></div>
