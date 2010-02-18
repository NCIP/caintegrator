<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">                      

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>

    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_annotation_group_help')" class="help">
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

                <table>
                <s:form id="annotationGroupForm" name="annotationGroupForm" 
                    action="saveAnnotationGroup" method="post" enctype="multipart/form-data" > 
                <tr>
                    <td>
                        <s:actionerror />
                     </td>
                </tr>
                <tr>
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="cancelAction" />
                    <s:hidden name="annotationGroup.id" />

                    <s:textfield label="Group Name" name="annotationGroup.name" size="40"/>
                    <s:radio name="annotationGroup.displayableEntityType" 
                            list="@gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum@getValueToDisplayableMap()"
                            required="true" label="Select Annotation Grouping Entity Type:" disabled="%{existingGroup}"/>
                    
                    <s:textarea label="Description" name="annotationGroup.description" cols="40" rows="3"/>
                    
                </tr>
                <tr> 
            	    <td></td>
            	    <td>
            	    <button type="button" 
            	            onclick="document.annotationGroupForm.action = 'cancelAnnotationGroup.action';
            	            document.annotationGroupForm.cancelAction.value = 'true';
            	            document.annotationGroupForm.submit();"> Cancel 
            	    </button>
            	    <button type="button" onclick="document.annotationGroupForm.submit();">Save</button>
            	    </td> 
                </tr>
                </s:form> 
                </table>
                </td>
            </tr>
        </table>
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
	                                <s:if test="%{fieldDescriptor != null}">
	                                    <s:select name="displayableFields[%{#columnIterator.count - 1}].annotationGroupName" 
	                                              list="selectableAnnotationGroups"
	                                              listKey="name"
	                                              listValue="name"
	                                              headerKey="" headerValue="-----" theme="simple" />
	                                </s:if>
	                            </td>
	                            <td>
	                                <s:if test="%{fieldDescriptor != null}">
	                                    <s:checkbox name="displayableFields[%{#columnIterator.count - 1}].fieldDescriptor.shownInBrowse"
	                                        theme="simple" disabled="false"/>
	                                </s:if>
	                            </td>
	                            <td>
	                                <s:if test="%{identifierType}">
	                                    Identifier
	                                </s:if>
	                                <s:elseif test="%{timepointType}">
	                                    Timepoint
	                                </s:elseif>
	                                <s:elseif test="%{fieldDescriptor != null && fieldDescriptor.definition != null}">
	                                    <s:property value="fieldDescriptor.definition.displayName"/> 
	                                </s:elseif>
	                                <s:url id="editGroupFieldDescriptor" action="editGroupFieldDescriptor">
	                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
	                                    <s:param name="groupId" value="annotationGroup.id" />
	                                    <s:param name="fieldDescriptor.id" value="fieldDescriptor.id" />
	                                </s:url>
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
     
    </div>
</div>

<div class="clear"><br /></div>
