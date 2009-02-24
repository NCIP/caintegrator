<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><span class="help"><a href="#">&nbsp;</a></span></div>
    
    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <s:if test="%{studyConfiguration.id != null}">
        <h1>Edit Study</h1>
    </s:if>
    <s:else>
        <h1>Create New Study</h1>
    </s:else>
    
    <script language="javascript">
        function verifyName(theName) {
            if (theName.length > 50) {
                alert("Study name exceeds maximum length of 50 characters.\nPlease shorten the Study name.");
                return (false);
            }
            return (true);
        }
    </script>

    <s:form onsubmit="return verifyName(nameId.value)">
        <s:fielderror />
        <s:hidden name="studyConfiguration.id"  />
        <s:textfield label="Study Name" name="studyConfiguration.study.shortTitleText" id="nameId"/>
        <s:textarea label="Study Description" name="studyConfiguration.study.longTitleText" cols="40" rows="4" />
        <tr>
            <td class="tdLabel" align="right">
                <label class="label">Status:</label>
            </td>
            <td>
                <s:property value="studyConfiguration.status.value"/>
            </td>
        </tr>
        
        <s:submit action="saveStudy" value="Save" />
        <s:if test="%{studyConfiguration.id != null}">
            <s:if test="%{studyConfiguration.deployable}">
                <s:submit action="deployStudy" value="Save and Deploy" />
            </s:if>
            <s:else>
                <s:submit disabled="true" action="deployStudy" value="Save and Deploy" />
            </s:else>
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

    <s:actionerror/>
    
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
                <s:if test="%{!loadable}">
                            Definition Incomplete
                </s:if>
                <s:else>
                    <s:if test="%{currentlyLoaded}">
                            Loaded
                    </s:if>
                    <s:else>
                            Not Loaded
                    </s:else>
                </s:else>  
            </td>
            <td>
                <s:url id="editClinicalSource" action="editClinicalSource">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="clinicalSource.id" value="id" />
                </s:url> 
                <s:a href="%{editClinicalSource}">Edit</s:a> 
                <s:if test="%{loadable}" > |
                    <s:if test="%{currentlyLoaded}">
                       <s:url id="reLoadClinicalSource" action="reLoadClinicalSource">
                          <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                          <s:param name="clinicalSource.id" value="id" />
                       </s:url> 
                       <s:a href="%{reLoadClinicalSource}">Reload All Clinical</s:a>
                    </s:if>
                    <s:else>
                       <s:url id="loadClinicalSource" action="loadClinicalSource">
                          <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                          <s:param name="clinicalSource.id" value="id" />
                       </s:url> 
                       <s:a href="%{loadClinicalSource}">Load Clinical</s:a> 
                    </s:else>
                </s:if>
                <s:url id="deleteClinicalSource" action="deleteClinicalSource">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="clinicalSource.id" value="id" />
                </s:url> 
                <s:a href="%{deleteClinicalSource}" onclick="return confirm('This clinical source file will be permanently deleted.')"> | Delete</s:a>
            </td>
        </tr>
        </s:iterator>
        <tr>
            <th colspan="4">
                <s:form action="addClinicalFile" method="post" enctype="multipart/form-data" cssClass="editStudyFile" >
                    <s:hidden name="studyConfiguration.id" />
                    <s:file name="clinicalFile" label="File (type CSV only)" />
                    <s:submit value="Add Clinical Data Source" action="addClinicalFile" cssClass="editStudyFile" align="left" />
                </s:form>
            </th>
        </tr>
        
        
        <tr>
            <th colspan="4">
            <table class="wwFormTable">
            <tbody>
            <tr>

                <td style='font-weight: normal;'>
		            <s:url id="editSurvivalValueDefinitions" action="editSurvivalValueDefinitions">
		                <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
		            </s:url>
                    <div align="left">
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
                <s:if test="%{!loadable}">
                            Definition Incomplete
                </s:if>
                <s:else>
                    <s:if test="%{currentlyLoaded}">
                            Loaded
                    </s:if>
                    <s:else>
                            Not Loaded
                    </s:else>
                </s:else>  
            </td>
            <td>
                <s:url id="editImagingSource" action="editImagingSource">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="imageAnnotationConfiguration.id" value="id" />
                </s:url> 
                <s:a href="%{editImagingSource}">Edit</s:a> 
                <s:if test="%{loadable}" > |
                    <s:url id="loadImagingSource" action="loadImagingSource">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="imageSourceConfiguration.id" value="id" />
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
