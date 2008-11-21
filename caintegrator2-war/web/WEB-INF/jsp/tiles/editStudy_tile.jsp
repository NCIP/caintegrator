<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <s:if test="%{studyConfiguration.id != null}">
        <h1>Edit Study</h1>
    </s:if>
    <s:else>
        <h1>Create New Study</h1>
    </s:else>
    
    <s:form>
        <s:hidden name="studyConfiguration.id"  />
        <s:textfield label="Study Name" name="study.shortTitleText" />
        <s:textarea label="Study Description" name="study.longTitleText" cols="40" rows="4" />
        
        <s:submit action="saveStudy" value="Save" />
        <s:if test="%{studyConfiguration.id != null}">
            <s:submit action="deployStudy" value="Deploy" />
        </s:if>
    </s:form>
    <br>
    <s:if test="%{studyConfiguration.id != null}">
    <s:form action="addStudyLogo" method="post" enctype="multipart/form-data">
        <s:hidden name="studyConfiguration.id"  />
        <s:file name="studyLogoFile" label="Logo File (JPEG or GIF)" accept="image/pjpeg,image/jpeg,image/gif" />
        
        <s:if test="studyConfiguration.studyLogo != null">
            <s:submit value="Change Study Logo" action="addStudyLogo" />    
        </s:if>
        
        <s:else>
            <s:submit value="Add Study Logo" action="addStudyLogo" />
        </s:else>
    </s:form>

    <table class="data">
        <tr>
            <th colspan="4">Clinical Data Sources</th>
        </tr>
        <tr>
            <th>Type</th>
            <th>Description</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <s:iterator value="studyConfiguration.clinicalConfigurationCollection" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:property value="type" /></td>
            <td><s:property value="description" /></td>
            <td>
                <s:if test="%{!currentlyLoaded}">
                            Not Loaded
                </s:if>
                <s:else>
                            Loaded
                </s:else>  
            </td>
            <td>
                <s:url id="editClinicalSource" action="editClinicalSource">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="clinicalSource.id" value="id" />
                </s:url> 
                <s:a href="%{editClinicalSource}">Edit</s:a> 
                <s:if test="%{loadable}" > |
                    <s:url id="loadClinicalSource" action="loadClinicalSource">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="clinicalSourceConfiguration.id" value="id" />
                    </s:url> 
                    <s:a href="%{loadClinicalSource}">
                        <s:if test="%{!currentlyLoaded}">
                            Load All Clinical
                        </s:if>
                        <s:else>
                            Reload All Clinical
                        </s:else>
                    </s:a> 
                </s:if>
            </td>
        </tr>
        </s:iterator>
        <tr>
            <th colspan="4">
                <s:form action="addClinicalFile" method="post" enctype="multipart/form-data">
                    <s:hidden name="studyConfiguration.id"  />
                    <s:file name="clinicalFile" label="File" />
                    <s:submit value="Add Clinical Data File" action="addClinicalFile" />
                </s:form>
            </th>
        </tr>
        
        
        <tr>
            <th colspan="4">
            <table class="wwFormTable">
            <tbody>
            <tr>
                <td>Survival Values</td>
                <td style='font-weight: normal;'>
		            <s:url id="editSurvivalValueDefinitions" action="editSurvivalValueDefinitions">
		                <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
		            </s:url>
                    <div align="right">
                    <s:a href="%{editSurvivalValueDefinitions}"> Edit Survival Values</s:a>
                    </div>
                </td>
            </tr>
            </tbody>
            </table>
            </th>
        </tr>
        
    </table>
    
    <s:form>
    <s:hidden name="studyConfiguration.id"  />
    <table class="data">
        <tr>
            <th colspan="3">Genomic Data Sources</th>
        </tr>
        <tr>
            <th>Server</th>
            <th>Experiment Identifier</th>
            <th>Action</th>
        </tr>
        <s:iterator value="studyConfiguration.genomicDataSources" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:property value="serverProfile.hostname" /></td>
            <td><s:property value="experimentIdentifier" /></td>
            <td>
                <s:url id="editGenomicSource" action="editGenomicSource">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="genomicSource.id" value="id" />
                </s:url> 
                <s:a href="%{editGenomicSource}">Edit</s:a> 
                <s:url id="editSampleMapping" action="editSampleMapping">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="genomicSource.id" value="id" />
                </s:url> 
                <s:a href="%{editSampleMapping}">Map Samples</s:a> 
            </td>
        </tr>
        </s:iterator>
        <tr>
            <th colspan="3"><s:submit action="addGenomicSource" value="Add" theme="simple" /></th>
        </tr>
    </table>
    </s:form>
    
    <s:form>
    <s:hidden name="studyConfiguration.id"  />
    <table class="data">
        <tr>
            <th colspan="4">Imaging Data Sources</th>
        </tr>
        <tr>
            <th>Type</th>
            <th>Description</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <s:iterator value="studyConfiguration.imageAnnotationConfigurations" status="status">
            <s:if test="#status.odd == true">
             <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:property value="type" /></td>
            <td><s:property value="description" /></td>
            <td>
                <s:if test="%{!currentlyLoaded}">
                            Not Loaded
                </s:if>
                <s:else>
                            Loaded
                </s:else>  
            </td>
            <td>
                <s:url id="editImagingSource" action="editImagingSource">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="imagingSource.id" value="id" />
                </s:url> 
                <s:a href="%{editImagingSource}">Edit</s:a> 
                <s:if test="%{loadable}" > |
                    <s:url id="loadImagingSource" action="loadImagingSource">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="imagingSourceConfiguration.id" value="id" />
                    </s:url> 
                    <s:a href="%{loadImagingSource}">
                    
                        <s:if test="%{!currentlyLoaded}">
                            Load All Imaging
                        </s:if>
                        <s:else>
                            Reload All Imaging
                        </s:else>
                    
                    </s:a> 
                </s:if>
            </td>
        </tr>
        </s:iterator>
        <tr>
            <th colspan="4"><s:submit action="addImagingSource" value="Add" theme="simple" /></th>
        </tr>
    </table>
    </s:form>
    </s:if>
</div>

<div class="clear"><br /></div>
