<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">
                   
    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>    
   
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('define_fields_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->          
    
    <h1><s:property value="#subTitleText" /></h1>
    <p>Assign annotation definitions to data fields and click <strong>Done</strong>.</p>
    
    <div class="form_wrapper_outer">

    <table class="form_wrapper_table">

            <tr>
                <td colspan="2" style="padding: 5px;">

                <s:form action="saveClinicalSource" method="post" enctype="multipart/form-data">
                <s:hidden name="studyConfiguration.id" />
                <s:hidden name="clinicalSource.id" />
                <table class="data">
                    <tr>
                        <th>Visisble</th>
                        <th>Field Definition</th>
                        <th>Field Header from File</th>
                        <th colspan="3" />Data from File</th>
                    </tr>
                    <s:iterator value="clinicalSource.annotationFile.columns" status="columnIterator">
                        <s:if test="#columnIterator.odd == true">
                          <tr class="odd">
                        </s:if>
                        <s:else>
                          <tr class="even">
                        </s:else>         
                            <td>
                                <s:if test="%{fieldDescriptor != null}">
                                    <s:checkbox name="clinicalSource.annotationFile.columns.get(%{#columnIterator.count - 1}).fieldDescriptor.shownInBrowse"
                                        theme="simple" disabled="false"/>
                                </s:if>
                            </td>
                            <td>
                                <s:if test="%{identifierColumn}">
                                    Identifier
                                </s:if>
                                <s:elseif test="%{timepointColumn}">
                                    Timepoint
                                </s:elseif>
                                <s:elseif test="%{fieldDescriptor != null && fieldDescriptor.definition != null}">
                                    <s:property value="fieldDescriptor.definition.displayName"/> 
                                </s:elseif>
                                <s:url id="editFileColumn" action="editFileColumn">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="sourceId" value="clinicalSource.id" />
                                    <s:param name="fileColumn.id" value="id" />
                                </s:url>
                                <br>
                                <s:a href="%{editFileColumn}">
                                    <s:if test="%{identifierColumn || timepointColumn || (fieldDescriptor != null && fieldDescriptor.definition != null) }">
                                        Change Assignment
                                    </s:if>
                                    <s:else>
                                        Assign Annotation Definition
                                    </s:else>
                                </s:a> 
                            </td>
                            <td><s:property value="name" /></td>
                            <td><s:if test="%{dataValues.size > 0}"><s:property value="dataValues[0]" /></s:if></td>
                            <td><s:if test="%{dataValues.size > 1}"><s:property value="dataValues[1]" /></s:if></td>
                            <td><s:if test="%{dataValues.size > 2}"><s:property value="dataValues[2]" /></s:if></td>
                        </tr>
                    </s:iterator>
                </table>
                <s:submit value="Done" />
                </s:form>
                </td>
            </tr>
        </table>
    </div>
</div>

<div class="clear"><br /></div>
