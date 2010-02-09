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
     
    </div>
</div>

<div class="clear"><br /></div>
