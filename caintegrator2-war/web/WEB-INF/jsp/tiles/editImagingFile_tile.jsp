<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('manage_study_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->          
    
    <h1>Define Fields for Imaging Data</h1>
    <h2>Study Name: <s:property value="studyConfiguration.study.shortTitleText" /></h2>
    <s:form id="imagingSourceForm" name="imagingSourceForm" action="saveImagingSourceAnnotations"
        method="post" enctype="multipart/form-data">
    <s:hidden name="studyConfiguration.id" />
    <s:hidden name="imageSourceConfiguration.id" />
    
    <s:if test="imageSourceConfiguration.imageAnnotationConfiguration == null">
        <s:file name="imageAnnotationFile" label="Image Series Annotation File" />
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
    </s:if>
    
    <s:else>
    <table class="data">
        <tr>
            <th>Field Definition</th>
            <th>Field Header from File</th>
            <th colspan="3" />Data from File</th>
        </tr>
        <s:iterator value="imageSourceConfiguration.imageAnnotationConfiguration.annotationFile.columns" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>
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
                    <s:url id="editImagingFileColumn" action="editImagingFileColumn">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="imageSourceConfiguration.id" value="imageSourceConfiguration.id" />
                        <s:param name="fileColumn.id" value="id" />
                    </s:url>
                    <br>
                    <s:a href="%{editImagingFileColumn}">Change Assignment</s:a> 
                </td>
                <td><s:property value="name" /></td>
                <td><s:if test="%{dataValues.size > 0}"><s:property value="dataValues[0]" /></s:if></td>
                <td><s:if test="%{dataValues.size > 1}"><s:property value="dataValues[1]" /></s:if></td>
                <td><s:if test="%{dataValues.size > 2}"><s:property value="dataValues[2]" /></s:if></td>
            </tr>
        </s:iterator>
    </table>
    <s:submit value="Done" />
    </s:else>
    
    </s:form>
            
</div>

<div class="clear"><br /></div>
