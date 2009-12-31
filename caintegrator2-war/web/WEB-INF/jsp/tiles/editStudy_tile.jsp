<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/GenomicDataSourceAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/interface/ImagingDataSourceAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      
    
    <script type="text/javascript">

    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        GenomicDataSourceAjaxUpdater.initializeJsp();
        ImagingDataSourceAjaxUpdater.initializeJsp();
    }
    
    function enableDeployButton() {
        document.studyDeploymentForm.deployButton.disabled = false;
    }
    
    </script>

    <s:if test="%{studyConfiguration.id != null}">    
    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    </s:if>    
    
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_study_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->      
    
    <!--ADD CONTENT HERE-->
    
    <s:if test="%{studyConfiguration.id != null}">
        <h1>Edit Study</h1>
                    
        <p>Configure your study, and click the <strong>Save</strong> or <strong>Deploy Study</strong> button at the bottom of the page when complete.</p>
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

    <div class="form_wrapper_outer">

        <table class="form_wrapper_table">
                <tr>
                    <th class="title" style="height: 2.5em;">Study Overview</th>
                    <th class="alignright">&nbsp;</th>
                </tr>
                <tr>
                    <td>

                    <s:form id="studyDeploymentForm" name="studyDeploymentForm" onsubmit="return verifyName(nameId.value)" cssClass="form">        
                        <s:fielderror />
                        <s:hidden name="studyConfiguration.id"  />
                        <s:textfield label="Study Name" name="studyConfiguration.study.shortTitleText" id="nameId" cssStyle="width: 280px;"/>
                        <s:textarea label="Study Description" name="studyConfiguration.study.longTitleText" cols="40" rows="4" cssStyle="width: 280px;"/>
                        <tr>
                            <td class="tdLabel" align="right">
                                <label class="label">Status:</label>
                            </td>
                            <td>
                                <s:property value="studyConfiguration.status.value"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdLabel" align="right">
                                <label class="label">Status Description:</label>
                            </td>
                            <td>
                                <s:property value="studyConfiguration.statusDescription"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdLabel" align="right">
                                <label class="label">Owner:</label>
                            </td>
                            <td>
                                <s:property value="studyConfiguration.userWorkspace.username"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdLabel" align="right">
                                <label class="label">Last Modified By:</label>
                            </td>
                            <td>
                                <s:property value="studyConfiguration.lastModifiedBy.username"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdLabel" align="right">
                                <label class="label">Last Modified Date:</label>
                            </td>
                            <td>
                                <s:property value="studyConfiguration.displayableLastModifiedDate"/>
                            </td>
                        </tr>
                        <s:submit action="saveStudy" value="Save" cssStyle="display:none;"/>
                        <s:if test="%{studyConfiguration.id != null}">
                            <s:if test="%{studyConfiguration.deployable}">
                                <s:submit id="deployButton" action="deployStudy" value="Save and Deploy" cssStyle="display:none;"/>
                            </s:if>
                            <s:else>
                                <s:submit id="deployButton" disabled="true" action="deployStudy" value="Save and Deploy" cssStyle="display:none;"/>
                            </s:else>
                        </s:if>
                    </s:form>
        
                    </td>
                            
                <s:if test="%{studyConfiguration.id != null}">
                    
                    <td>

                        <s:form action="addStudyLogo" method="post" enctype="multipart/form-data" theme="css_xhtml">
                            <s:hidden name="studyConfiguration.id"  />
                            
                            <!--Study Logo-->
                            <s:div cssClass="wwgrp">
                                <s:div cssClass="wwlbl">
                                        <label class="label" for="addStudyLogo_studyLogoFile">Study Logo:</label>
                                </s:div>
                                <s:div cssClass="wwctrl">
                                    <div id="study_logo">
                                        <s:if test="studyConfiguration.studyLogo.logoUrl != null">
								            <s:set name="logo" id="logo" value="'retrieveStudyLogoPreview.action'"/>
								            <img src="${logo}" alt="Study Logo" height="72" width="200"/>
                                        </s:if>
                                        <s:else>None
                                        </s:else>
                                    </div>
                                </s:div>
                            </s:div>                        
                            <!--/Study Logo-->
                            
                            
                            <s:file name="studyLogoFile" label="Logo File" accept="image/pjpeg,image/jpeg,image/gif" />
                            
                            <s:div name="commentdiv" cssClass="inlinehelp_form_element">
                                <span class="wwlbl">
                                &nbsp;
                                </span>
                                <span class="wwctrl">
                                JPEG/GIF, 200x72 maximum
                                </span>
                            </s:div><br>
                            <s:div cssClass="wwgrp">
                                <s:div cssClass="wwlbl">
                                    <label class="label" for="addStudyLogo_studyLogoFile">&nbsp;</label>
                                </s:div>
                                <s:div cssClass="wwctrl">
                                    <s:if test="studyConfiguration.studyLogo != null">
                                        <s:submit type="image" src="images/btn_upload.gif" value="Change Study Logo" action="addStudyLogo" />    
                                    </s:if>
                                    <s:else>
                                        <s:submit type="image" src="images/btn_upload.gif" value="Add Study Logo" action="addStudyLogo" />
                                    </s:else>
                                </s:div>
                            </s:div>
                        </s:form>
                    </td>
                    
                </s:if>
                <s:else>
                        <td></td>
                </s:else>
                </tr>
        </table>
        
        <s:if test="%{studyConfiguration.id != null}">
        <s:actionerror/>
                
        <table class="form_wrapper_table">
            <tbody><tr>
                <th class="title">Clinical Data Sources</th>
                <th class="thbutton">
                    <del class="btnwrapper">                    
                        <ul class="btnrow">
                            <li><a name="action:addClinicalFile_clinicalFile" id="addClinicalFile_clinicalFile" onclick="new Effect.toggle($('add_clinical'),'blind')" class="btn" style="margin: 0pt;" href="javascript://"><span class="btn_img"><span class="add">Add New</span></span></a></li>
                                <s:url id="editSurvivalValueDefinitions" action="editSurvivalValueDefinitions" includeParams="none">
                                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                </s:url>
                            <li><s:a href="%{editSurvivalValueDefinitions}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="edit">Edit Survival Values</span></span></s:a></li>
                        </ul>   
                    </del>
                </th>
            </tr>
            <tr>
                <td class="table_wrapper" colspan="2">              
                    
                    <!--Clinical File Upload--> 
                
                    <div class="formbox" id="add_clinical" style="display: none;">
                        
                        <s:form action="addClinicalFile" method="post" enctype="multipart/form-data" cssClass="form" >
                                    <s:hidden name="studyConfiguration.id" />
                                    <s:file name="clinicalFile" label="Add New Clinical Data Source" />
                                    <s:submit value="Upload Now" action="addClinicalFile" type="image" src="images/btn_upload.gif" cssClass="editStudyFile" align="center" />
                        </s:form>
                        
                    </div>
                    
                    <!--/Clinical File Upload-->
                
                    <table class="data">
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
                            <td style="float: right;">
                                <s:url id="editClinicalSource" action="editClinicalSource" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="clinicalSource.id" value="id" />
                                </s:url>
                                <s:a href="%{editClinicalSource}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="edit_annotations">Edit Annotations</span></span></s:a>
                                <s:if test="%{loadable}" >
                                    <s:if test="%{currentlyLoaded}">
                                       <s:url id="reLoadClinicalSource" action="reLoadClinicalSource" includeParams="none">
                                          <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                          <s:param name="clinicalSource.id" value="id" />
                                       </s:url> 
                                       <s:a href="%{reLoadClinicalSource}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="reload">Reload All Clinical</span></span></s:a>
                                    </s:if>
                                    <s:else>
                                       <s:url id="loadClinicalSource" action="loadClinicalSource" includeParams="none">
                                          <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                          <s:param name="clinicalSource.id" value="id" />
                                       </s:url> 
                                       <s:a href="%{loadClinicalSource}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="load">Load All Clinical</span></span></s:a>
                                    </s:else>
                                </s:if>
                                <s:url id="deleteClinicalSource" action="deleteClinicalSource" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="clinicalSource.id" value="id" />
                                </s:url> 
                                <s:a href="%{deleteClinicalSource}" cssClass="btn" cssStyle="margin: 0pt;" onclick="return confirm('This clinical source file will be permanently deleted.')"><span class="btn_img"><span class="delete">Delete</span></span></s:a>                                
                            </td>
                        </tr>
                        </s:iterator>
                        
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
        
        <table class="form_wrapper_table">
            <tbody><tr>
                <th class="title">Genomic Data Sources<span id="genomicSourceLoader"> <img src="images/ajax-loader.gif"/> </span></th>
                <th class="thbutton">
                    <del class="btnwrapper">                    
                        <ul class="btnrow">
                                <s:url id="addGenomicSource" action="addGenomicSource" includeParams="none">
                                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                </s:url>
                            <li><s:a href="%{addGenomicSource}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="add">Add New</span></span></s:a></li>
                        </ul>   
                    </del>
                </th>
            </tr>
            <tr>
                <td class="table_wrapper" colspan="2">              
        
                    <s:form>
                    <s:hidden name="studyConfiguration.id"  />
                    <table class="data">
                        <tr>
                            <th>Host Name</th>
                            <th>Experiment Identifier</th>
                            <th>File Description</th>
                            <th>Data Type</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        <tbody id="genomicSourceJobStatusTable" />
                    </table>
                    </s:form>
                    
                </td>
            </tr>
            </tbody>
        </table>                    

        <table class="form_wrapper_table">
            <tbody><tr>
                <th class="title">Imaging Data Sources<span id="imagingSourceLoader"> <img src="images/ajax-loader.gif"/> </span></th>
                <th class="thbutton">
                    <del class="btnwrapper">                    
                        <ul class="btnrow">
                                <s:url id="addImagingSource" action="addImagingSource" includeParams="none">
                                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                </s:url>
                            <li><s:a href="%{addImagingSource}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="add">Add New</span></span></s:a></li>
                        </ul>   
                    </del>
                </th>
            </tr>
            <tr>
                <td class="table_wrapper" colspan="2">

                    <s:form>
                    <s:hidden name="studyConfiguration.id"  />
                    <table class="data">
                        <tr>
                            <th>Host Name</th>
                            <th>Collection Name</th>
                            <th>File Description</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        <tbody id="imagingSourceJobStatusTable" />
                    </table>
                    </s:form>
                    
                </td>
            </tr>
            </tbody>
        </table> 
        
        <table class="form_wrapper_table">
            <tbody><tr>
                <th class="title">External Links</th>
                <th class="thbutton">
                    <del class="btnwrapper">                    
                        <ul class="btnrow">
                            <li><a name="action:addExternalLinks_externalLinksFile" id="addExternalLinks_externalLinksFile" onclick="new Effect.toggle($('add_external_links'),'blind')" class="btn" style="margin: 0pt;" href="javascript://"><span class="btn_img"><span class="add">Add New</span></span></a></li>
                        </ul>   
                    </del>
                </th>
            </tr>
            <tr>
                <td class="table_wrapper" colspan="2">              
                    
                    <!--External Links File Upload--> 
                
                    <div class="formbox" id="add_external_links" style="display: none;">
                        
                        <s:form action="addExternalLinks" method="post" enctype="multipart/form-data" cssClass="form" >
                                    <s:hidden name="studyConfiguration.id" />
                                    <s:textfield label="Name" name="externalLinkList.name" />
                                    <s:textarea label="Description" name="externalLinkList.description" cols="50" rows="3"/>
                                    <s:file name="externalLinksFile" label="Add New External Links" />
                                    <s:submit value="Upload Now" action="addExternalLinks" type="image" src="images/btn_upload.gif" cssClass="editStudyFile" align="center" />
                        </s:form>
                        
                    </div>
                    
                    <!--/External Links File Upload-->
                
                    <table class="data">
                        <tr>
                            <th>Name</th>
                            <th>Description</th>
                            <th>File Name</th>
                            <th>Number of Links</th>
                            <th>Action</th>
                        </tr>
                        <s:iterator value="studyConfiguration.externalLinkLists" status="status">
                            <s:if test="#status.odd == true">
                              <tr class="odd">
                            </s:if>
                            <s:else>
                              <tr class="even">
                            </s:else>            
                            <td><s:property value="name" /></td>
                            <td><s:property value="description" /></td>
                            <td><s:property value="fileName"/></td>
                            <td><s:property value="externalLinks.size"/></td>
                            <td style="float: right;">
                            <s:url id="deleteExternalLinks" action="deleteExternalLinks" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="externalLinkList.id" value="id" />
                                </s:url> 
                                <s:a href="%{deleteExternalLinks}" cssClass="btn" cssStyle="margin: 0pt;" onclick="return confirm('These External Links will be permanantly deleted.')"><span class="btn_img"><span class="delete">Delete</span></span></s:a>                                
                            </td>
                            
                        </tr>
                        </s:iterator>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
        </s:if>
    
        <br>
        <div class="bottombtns">
            <ul class="btnrow">
            <s:if test="%{studyConfiguration.id != null}">
                <s:if test="%{studyConfiguration.deployable}">
                    <li><input type="image" src="images/btn_deploy.gif" alt="Deploy Study" name="action:deployStudy" id="editStudy_deployStudy" onClick="document.getElementById('studyDeploymentForm').action='/caintegrator2/deployStudy.action'; document.getElementById('studyDeploymentForm').submit()"/></li>
                </s:if>
                <s:else>
                    <li><img alt="deploy disabled" src="images/btn_deploy_disabled.gif"/></li>
                </s:else>
            </s:if>
            <li><input type="image" src="images/btn_save.gif" alt="Save" name="action:saveStudy" id="editStudy_saveStudy" onClick="document.getElementById('studyDeploymentForm').action='/caintegrator2/saveStudy.action'; document.getElementById('studyDeploymentForm').submit()"/></li>
            <li> <s:url id="manageStudiesUrl" includeParams="none" action="manageStudies" />
            <s:a href="%{manageStudiesUrl}" cssClass="btn" cssStyle="margin:0 5px;"><span class="btn_img"><span class="cancel">Cancel</span></span></s:a></li>
            
            </ul>   
        </div>
    
    </div>
    
</div>

<div class="clear"><br /></div>
