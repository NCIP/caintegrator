<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/GenomicDataSourceAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/interface/ImagingDataSourceAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/interface/SubjectDataSourceAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      
    
    <script type="text/javascript">

    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        SubjectDataSourceAjaxUpdater.initializeJsp();
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
                        <s:actionerror/>
                        <s:fielderror />
                        <s:token />
                        <s:hidden name="studyConfiguration.id"  />
                        <s:textfield label="Study Name" name="studyConfiguration.study.shortTitleText" id="nameId" cssStyle="width: 280px;"/>
                        <s:textarea label="Study Description" name="studyConfiguration.study.longTitleText" cols="40" rows="4" cssStyle="width: 280px;"/>
                        <tr>
                            <td class="tdLabel" align="right">
                                <label class="label">Allow public to browse this study:</label>
                            </td>
                            <td>
                                <s:checkbox name="studyConfiguration.study.publiclyAccessible" theme="simple"/>
                            </td>
                        </tr>
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
                        <s:if test="%{studyConfiguration.id != null}">
                                <s:url id="viewStudyLog" action="editStudyLog" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="readOnly" value="true" />
                                    <s:param name="struts.token.name">struts.token</s:param>
                                    <s:param name="struts.token" value="%{struts.token}" />                                    
                                </s:url>
                                <s:url id="editStudyLog" action="editStudyLog" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="readOnly" value="false" />
                                    <s:param name="struts.token.name">struts.token</s:param>
                                    <s:param name="struts.token" value="%{struts.token}" />
                                </s:url>
                            <tr>
                                <td class="tdLabel" align="right">
	                                <label class="label">Study Log:</label>
	                            </td>
	                            <td>
	                               <s:a href="%{viewStudyLog}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="search">View Log</span></span></s:a> 
	                               <s:a href="%{editStudyLog}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="edit_annotations">Edit Log</span></span></s:a>
	                            </td>
	                        </tr>
                        </s:if>
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

                        <s:form cssStyle="text-align: center; display: inline-block" action="addStudyLogo" method="post" enctype="multipart/form-data" theme="css_xhtml">
                            <s:hidden name="studyConfiguration.id"  />
                            
                            <!--Study Logo-->
                            <s:div cssClass="wwgrp" cssStyle="display:inline-block">
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
                                        <s:submit type="image" src="images/btn_upload.gif" value="Change Study Logo" action="addStudyLogo" onclick="showBusyDialog();"/>    
                                    </s:if>
                                    <s:else>
                                        <s:submit type="image" src="images/btn_upload.gif" value="Add Study Logo" action="addStudyLogo" onclick="showBusyDialog();" />
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
        <div id="errors" style="color: red;"> </div>
        <table class="form_wrapper_table">
            <tbody><tr>
                <th class="title">Annotation Groups</th>
                <th class="thbutton">
                    <del class="btnwrapper">                    
                        <ul class="btnrow">
                            <s:token />
							<s:url id="addAnnotationGroup" action="addAnnotationGroup" includeParams="none">
							        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
							        <s:param name="struts.token.name">struts.token</s:param>
                                    <s:param name="struts.token" value="%{struts.token}" />
							</s:url>
                            <li><s:a href="%{addAnnotationGroup}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="add">Add New</span></span></s:a></li>	                        
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
                            <th>Group Name</th>
                            <th>Description</th>
                            <th>Number of Annotations</th>
                            <th>Action</th>
                        </tr>
                        
                        <s:iterator value="studyConfiguration.study.sortedAnnotationGroups" status="status">
                            <s:if test="#status.odd == true">
                              <tr class="odd">
                            </s:if>
                            <s:else>
                              <tr class="even">
                            </s:else>
                            <td><s:property value="name" /></td>
                            <td><s:property value="description" /></td>
                            <td><s:property value="annotationFieldDescriptors.size()" /></td>
                            <td style="float: right;">
                                <s:url id="editAnnotationGroup" action="editAnnotationGroup" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="annotationGroup.id" value="id" />
                                    <s:param name="struts.token.name">struts.token</s:param>
                                    <s:param name="struts.token" value="%{struts.token}" />
                                </s:url>
                                <s:a href="%{editAnnotationGroup}" cssClass="btn" cssStyle="margin: 0pt;"><span class="btn_img"><span class="edit_annotations">Edit Group</span></span></s:a>                            
                                <s:if test="deletable">
                                <s:url id="deleteAnnotationGroup" action="deleteAnnotationGroup" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="annotationGroup.id" value="id" />
                                    <s:param name="struts.token.name">struts.token</s:param>
                                    <s:param name="struts.token" value="%{struts.token}" />                                    
                                </s:url> 
                                <s:a href="%{deleteAnnotationGroup}" cssClass="btn" cssStyle="margin: 0pt;" onclick="return confirm('This annotation group will be permanently deleted.')"><span class="btn_img"><span class="delete">Delete</span></span></s:a>                                
                                </s:if>
                            </td>
                        </tr>
                        </s:iterator>
                        
                    </table>
                    </s:form>
                    
                </td>
            </tr>
            </tbody>
        </table>                    
                
        <table class="form_wrapper_table">
            <tbody><tr>
                <th class="title">Subject Annotation Data Sources<span id="subjectSourceLoader"> <img src="images/ajax-loader.gif"/> </span></th>
                <th class="thbutton">
                    <del class="btnwrapper">                    
                        <ul class="btnrow">
                            <li><a name="action:addClinicalFile_clinicalFile" id="addClinicalFile_clinicalFile" onclick="new Effect.toggle($('add_clinical'),'blind')" class="btn" style="margin: 0pt;" href="javascript://"><span class="btn_img"><span class="add">Add New</span></span></a></li>
                                <s:url id="editSurvivalValueDefinitions" action="editSurvivalValueDefinitions" includeParams="none">
                                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                        <s:param name="struts.token.name">struts.token</s:param>
                                        <s:param name="struts.token" value="%{struts.token}" />                                        
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
                                    <s:token />
                                    <s:hidden name="studyConfiguration.id" />
                                    <s:file name="clinicalFile" label="Add New Subject Annotation Data Source" />
                                    <s:checkbox name="createNewAnnotationDefinition" label="Create a new Annotation Definition if one is not found" 
                                        labelposition="left" />
                                    <s:submit value="Upload Now" onclick="showBusyDialog(); document.addClinicalFile.submit();" type="image" src="images/btn_upload.gif" cssClass="editStudyFile" align="center" />
                        </s:form>
                        
                    </div>
                    
                    <!--/Clinical File Upload-->
                
                    <table class="data">
                        <tr>
                            <th>Type</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th>Last Modified</th>
                            <th>Action</th>
                        </tr>
                        <tbody id="subjectSourceJobStatusTable" />
                        
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
                                        <s:param name="struts.token.name">struts.token</s:param>
                                        <s:param name="struts.token" value="%{struts.token}" />
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
                            <th>Last Modified</th>
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
                                    <s:param name="struts.token.name">struts.token</s:param>
                                    <s:param name="struts.token" value="%{struts.token}" />                                        
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
                            <th>Last Modified</th>
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
                                    <s:token />
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
                    <li><input type="image" src="images/btn_deploy.gif" alt="Deploy Study" name="action:deployStudy" id="editStudy_deployStudy" onClick="document.getElementById('studyDeploymentForm').action='/caintegrator/deployStudy.action'; document.getElementById('studyDeploymentForm').submit()"/></li>
                </s:if>
                <s:else>
                    <li><img alt="deploy disabled" src="images/btn_deploy_disabled.gif"/></li>
                </s:else>
            </s:if>
            <li><input type="image" src="images/btn_save.gif" alt="Save" name="action:saveStudy" id="editStudy_saveStudy" onClick="document.getElementById('studyDeploymentForm').action='/caintegrator/saveStudy.action'; document.getElementById('studyDeploymentForm').submit()"/></li>
            <li> <s:url id="manageStudiesUrl" includeParams="none" action="manageStudies">
                    <s:param name="struts.token.name">struts.token</s:param>
                    <s:param name="struts.token" value="%{struts.token}" />     
            </s:url>
            <s:a href="%{manageStudiesUrl}" cssClass="btn" cssStyle="margin:0 5px;"><span class="btn_img"><span class="cancel">Cancel</span></span></s:a></li>
            
            </ul>   
        </div>
    
    </div>
    
</div>

<div class="clear"><br /></div>
