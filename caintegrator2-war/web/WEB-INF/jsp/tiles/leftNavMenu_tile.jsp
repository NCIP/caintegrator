<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
            
<div id="leftnav">
    
    <!--Study Logo-->
    <s:if test="studySubscription != null">
	    <div id="study_logo">
	        <s:set name="logo" id="logo" value="'retrieveStudyLogo.action'"/>
	        <img src="${logo}" alt="Study Logo" height="72" width="200"/>
	    </div>
    </s:if>
    
    <!--/Study Logo-->
    
    <!--Menu-->
    <s:url id="newQueryUrl" includeParams="none" action="newQuery" />
    
    <s:url id="notYetImplementedUrl" includeParams="none" action="notYetImplemented" />
    
    <s:url id="tutorialsUrl" includeParams="none" action="/caintegrator2/tutorials" />
    
    <s:set name="sessionHelper" value="#session['sessionHelper']" />

    <s:if test="studySubscription != null">    
    <ul class="menu">
        <li class="stdnavforinvestigator">
            <div><s:property value="currentStudy.shortTitleText"/></div>
            <ul>
                <li><s:url id="homePageUrl" includeParams="none" action="workspace" />
                    <a href="${ homePageUrl }">Home</a>
                </li>
                <li class="stdnav">
                    <a href='<s:property value="#newQueryUrl" />'>
                    Search <s:property value="currentStudy.shortTitleText"/></a>
                </li>
                <li><s:url id="createGeneListUrl" includeParams="none" action="manageGeneList" />
                    <a href="${createGeneListUrl}">Create New Gene List</a>
                </li>
            </ul>
        </li>
        
        <!--Tree Control-->
        
        <li class="treenav"><div>Study Data</div>
            <s:if test="%{anonymousUser}">
                <s:if test="!displayableWorkspace.globalGeneLists.isEmpty() || !displayableWorkspace.globalSubjectLists.isEmpty()">
                    <ul class="pde">
                    <li><a href="#">Global Lists</a>
                        <ul style="padding: 3px 0px 10px 10px;">
                        <s:if test="!displayableWorkspace.globalGeneLists.isEmpty()">
                            <li><a href="#">Gene Lists</a>
                            <ul>
                                <s:iterator value="displayableWorkspace.globalGeneLists">
                                    <s:url id="editGeneListUrl" action="editGeneList" includeParams="none">
                                        <s:param name="selectedAction" value="%{'editGlobalList'}" />
                                        <s:param name="listName" value="name" />
                                    </s:url>
                                    <s:url id="runGeneListQueryUrl" action="manageQuery" includeParams="none">
                                        <s:param name="selectedAction" value="%{'loadGlobalGeneListExecute'}" />
                                        <s:param name="geneListName" value="name" />
                                    </s:url>

                                    <li style="padding: 0px 0px 2px 0px">
                                        <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
                                            <div style="float: left; white-space: nowrap; width: 38px;">
                                                <s:a href="%{runGeneListQueryUrl}" onclick="showBusyDialog();" cssClass="searches"
                                                    cssStyle="background:transparent url('/caintegrator2/images/ico_list.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Run query: %{name}">&nbsp;</s:a>
                                                <s:if test="%{studyManager}">
                                                    <s:a href="%{editGeneListUrl}" cssClass="queryAction"
                                                        cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                        title="Rename List: %{name}">&nbsp;</s:a>
                                                </s:if>
                                            </div>
                                            <div style="float: left; width: 110px; white-space: normal; padding: 4px 0px 0px 0px;">
                                                <s:a href="%{runGeneListQueryUrl}" onclick="showBusyDialog();" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;"
                                                    title="Description: %{description} / Last Modified: %{displayableLastModifiedDate}">
                                                    <s:if test="name == openGlobalGeneListName">
                                                        <strong><s:property value="name"/></strong>
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="name"/>
                                                    </s:else>
                                                </s:a>
                                            </div>
                                        </div>
                                    </li>
                                </s:iterator>
                            </ul>
                            </li>
                        </s:if>
                        <s:if test="!displayableWorkspace.globalSubjectLists.isEmpty()">
                            <li><a href="#">Subject Lists</a>
                            <ul>
                                <s:iterator value="displayableWorkspace.globalSubjectLists">
                                    <s:url id="editSubjectListUrl" action="editSubjectList" includeParams="none">
                                        <s:param name="selectedAction" value="%{'editGlobalList'}" />
                                        <s:param name="listName" value="name" />
                                    </s:url>
                                    <s:url id="runSubjectListQueryUrl" action="manageQuery" includeParams="none">
                                        <s:param name="selectedAction" value="%{'loadGlobalSubjectListExecute'}" />
                                        <s:param name="subjectListName" value="name" />
                                    </s:url>

                                    <li style="padding: 0px 0px 2px 0px">
                                        <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
                                            <div style="float: left; white-space: nowrap; width: 38px;">
                                                <s:a href="%{runSubjectListQueryUrl}" onclick="showBusyDialog();" cssClass="searches"
                                                    cssStyle="background:transparent url('/caintegrator2/images/ico_list.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Run query: %{name}">&nbsp;</s:a>
                                                <s:if test="%{studyManager}">
                                                    <s:a href="%{editSubjectListUrl}" cssClass="queryAction"
                                                        cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                        title="Rename List: %{name}">&nbsp;</s:a>
                                                </s:if>
                                            </div>
                                            <div style="float: left; width: 110px; white-space: normal; padding: 4px 0px 0px 0px;">
                                                <s:a href="%{runSubjectListQueryUrl}" onclick="showBusyDialog();" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;"
                                                    title="Description: %{description} / Last Modified: %{displayableLastModifiedDate}">
                                                    <s:if test="name == openGlobalSubjectListName">
                                                        <strong><s:property value="name"/></strong>
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="name"/>
                                                    </s:else>
                                                </s:a>
                                            </div>
                                        </div>
                                    </li>
                                </s:iterator>
                            </ul>
                            </li>
                        </s:if>
                        </ul>
                    </li>
                    </ul>
                </s:if>
                <s:else>
                    <ul class="pde" style="padding: 3px 0px 10px 10px;">
                        <font color="black"><i>Must be registered to use this feature.</i></font>
                    </ul>
                </s:else>
            </s:if>
            <s:else>
            <ul class="pde">
                <li><a href="#">Saved Queries</a>
                    <ul style="padding: 3px 0px 10px 10px;">
                        <li><a href="#">My Queries</a>
                            <ul>                                
                                <s:iterator value="displayableWorkspace.userQueries">
                                    <s:url id="runQueryUrl" action="manageQuery" includeParams="none">
                                        <s:param name="selectedAction" value="%{'loadExecute'}" />
                                        <s:param name="queryId" value="id" />
                                    </s:url>
                                    <s:url id="editQueryUrl" action="manageQuery" includeParams="none">
                                        <s:param name="selectedAction" value="%{'loadQuery'}" />
                                        <s:param name="queryId" value="id" />
                                    </s:url>
                          
                                    <li style="padding: 0px 0px 2px 0px">
                                        <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
                                            <div style="float: left; white-space: nowrap; width: 32px;">
                                                <s:a href="%{runQueryUrl}" onclick="showBusyDialog();" cssClass="searches" cssStyle="background:transparent url('/caintegrator2/images/ico_search.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Run query: %{name}">&nbsp;</s:a>
                                                <s:a href="%{editQueryUrl}" cssClass="queryAction" cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Edit query: %{name}">&nbsp;</s:a>
                                            </div>
                                            <div style="float: right; width: 110px; white-space: normal; padding: 4px 0px 0px 0px;">
                                                <s:a href="%{runQueryUrl}" onclick="showBusyDialog();" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;" title="Description: %{description} / Last Modified: %{displayableLastModifiedDate}">
                                                    <s:if test="id == openQueryId">
                                                        <strong><s:property value="name"/></strong>
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="name"/>
                                                    </s:else>
                                                </s:a>
                                            </div>
                                        </div>
                                    </li>
                                    
                                </s:iterator>
                                
                            </ul>
                        </li>
                    </ul>
                </li>
                <li><a href="#">Saved Lists</a>
                    <ul style="padding: 3px 0px 10px 10px;">
                    <li><a href="#">Global Lists</a>
                        <ul style="padding: 3px 0px 10px 10px;">
                        <li><a href="#">Gene Lists</a>
                            <ul>
                                <s:iterator value="displayableWorkspace.globalGeneLists">
                                    <s:url id="editGeneListUrl" action="editGeneList" includeParams="none">
                                        <s:param name="selectedAction" value="%{'editGlobalList'}" />
                                        <s:param name="listName" value="name" />
                                    </s:url>
                                    <s:url id="runGeneListQueryUrl" action="manageQuery" includeParams="none">
                                        <s:param name="selectedAction" value="%{'loadGlobalGeneListExecute'}" />
                                        <s:param name="geneListName" value="name" />
                                    </s:url>

                                    <li style="padding: 0px 0px 2px 0px">
                                        <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
                                            <div style="float: left; white-space: nowrap; width: 38px;">
                                                <s:a href="%{runGeneListQueryUrl}" onclick="showBusyDialog();" cssClass="searches"
                                                    cssStyle="background:transparent url('/caintegrator2/images/ico_list.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Run query: %{name}">&nbsp;</s:a>
                                                <s:if test="%{studyManager}">
                                                    <s:a href="%{editGeneListUrl}" cssClass="queryAction"
                                                        cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                        title="Rename List: %{name}">&nbsp;</s:a>
                                                </s:if>
                                            </div>
                                            <div style="float: left; width: 110px; white-space: normal; padding: 4px 0px 0px 0px;">
                                                <s:a href="%{runGeneListQueryUrl}" onclick="showBusyDialog();" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;"
                                                    title="Description: %{description} / Last Modified: %{displayableLastModifiedDate}">
                                                    <s:if test="name == openGlobalGeneListName">
                                                        <strong><s:property value="name"/></strong>
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="name"/>
                                                    </s:else>
                                                </s:a>
                                            </div>
                                        </div>
                                    </li>
                                </s:iterator>
                            </ul>
                        </li>
                        <li><a href="#">Subject Lists</a>
                            <ul>
                                <s:iterator value="displayableWorkspace.globalSubjectLists">
                                    <s:url id="editSubjectListUrl" action="editSubjectList" includeParams="none">
                                        <s:param name="selectedAction" value="%{'editGlobalList'}" />
                                        <s:param name="listName" value="name" />
                                    </s:url>
                                    <s:url id="runSubjectListQueryUrl" action="manageQuery" includeParams="none">
                                        <s:param name="selectedAction" value="%{'loadGlobalSubjectListExecute'}" />
                                        <s:param name="subjectListName" value="name" />
                                    </s:url>

                                    <li style="padding: 0px 0px 2px 0px">
                                        <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
                                            <div style="float: left; white-space: nowrap; width: 38px;">
                                                <s:a href="%{runSubjectListQueryUrl}" onclick="showBusyDialog();" cssClass="searches"
                                                    cssStyle="background:transparent url('/caintegrator2/images/ico_list.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Run query: %{name}">&nbsp;</s:a>
                                                <s:if test="%{studyManager}">
                                                    <s:a href="%{editSubjectListUrl}" cssClass="queryAction"
                                                        cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                        title="Rename List: %{name}">&nbsp;</s:a>
                                                </s:if>
                                            </div>
                                            <div style="float: left; width: 110px; white-space: normal; padding: 4px 0px 0px 0px;">
                                                <s:a href="%{runSubjectListQueryUrl}" onclick="showBusyDialog();" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;"
                                                    title="Description: %{description} / Last Modified: %{displayableLastModifiedDate}">
                                                    <s:if test="name == openGlobalSubjectListName">
                                                        <strong><s:property value="name"/></strong>
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="name"/>
                                                    </s:else>
                                                </s:a>
                                            </div>
                                        </div>
                                    </li>
                                </s:iterator>
                            </ul>
                        </li>
                    </ul>
                    </li>
                    <li><a href="#">My Lists</a>
                        <ul style="padding: 3px 0px 10px 10px;">
                        <li><a href="#">Gene Lists</a>
                            <ul>
                                <s:iterator value="displayableWorkspace.geneLists">
                                    <s:url id="editGeneListUrl" action="editGeneList" includeParams="none">
                                        <s:param name="selectedAction" value="%{'editList'}" />
                                        <s:param name="listName" value="name" />
                                    </s:url>
                                    <s:url id="runGeneListQueryUrl" action="manageQuery" includeParams="none">
                                        <s:param name="selectedAction" value="%{'loadGeneListExecute'}" />
                                        <s:param name="geneListName" value="name" />
                                    </s:url>

                                    <li style="padding: 0px 0px 2px 0px">
                                        <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
                                            <div style="float: left; white-space: nowrap; width: 38px;">
                                                <s:a href="%{runGeneListQueryUrl}" onclick="showBusyDialog();" cssClass="searches"
                                                    cssStyle="background:transparent url('/caintegrator2/images/ico_list.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Run query: %{name}">&nbsp;</s:a>
                                                <s:a href="%{editGeneListUrl}" cssClass="queryAction"
                                                    cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Rename List: %{name}">&nbsp;</s:a>
                                            </div>
                                            <div style="float: left; width: 110px; white-space: normal; padding: 4px 0px 0px 0px;">
                                                <s:a href="%{runGeneListQueryUrl}" onclick="showBusyDialog();" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;"
                                                    title="Description: %{description} / Last Modified: %{displayableLastModifiedDate}">
                                                    <s:if test="name == openGeneListName">
                                                        <strong><s:property value="name"/></strong>
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="name"/>
                                                    </s:else>
                                                </s:a>
                                            </div>
                                        </div>
                                    </li>
                                </s:iterator>
                            </ul>
                        </li>
                        <li><a href="#">Subject Lists</a>
                            <ul>
                                <s:iterator value="displayableWorkspace.subjectLists">
                                    <s:url id="editSubjectListUrl" action="editSubjectList" includeParams="none">
                                        <s:param name="selectedAction" value="%{'editList'}" />
                                        <s:param name="listName" value="name" />
                                    </s:url>
                                    <s:url id="runSubjectListQueryUrl" action="manageQuery" includeParams="none">
                                        <s:param name="selectedAction" value="%{'loadSubjectListExecute'}" />
                                        <s:param name="subjectListName" value="name" />
                                    </s:url>

                                    <li style="padding: 0px 0px 2px 0px">
                                        <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
                                            <div style="float: left; white-space: nowrap; width: 38px;">
                                                <s:a href="%{runSubjectListQueryUrl}" onclick="showBusyDialog();" cssClass="searches"
                                                    cssStyle="background:transparent url('/caintegrator2/images/ico_list.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Run query: %{name}">&nbsp;</s:a>
                                                <s:a href="%{editSubjectListUrl}" cssClass="queryAction"
                                                    cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Rename List: %{name}">&nbsp;</s:a>
                                            </div>
                                            <div style="float: left; width: 110px; white-space: normal; padding: 4px 0px 0px 0px;">
                                                <s:a href="%{runSubjectListQueryUrl}" onclick="showBusyDialog();" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;"
                                                    title="Description: %{description} / Last Modified: %{displayableLastModifiedDate}">
                                                    <s:if test="name == openSubjectListName">
                                                        <strong><s:property value="name"/></strong>
                                                    </s:if>
                                                    <s:else>
                                                        <s:property value="name"/>
                                                    </s:else>
                                                </s:a>
                                            </div>
                                        </div>
                                    </li>
                                </s:iterator>
                            </ul>
                        </li>
                    </ul>
                    </li>
                </ul>
                </li>
                <s:if test="currentStudy.studyConfiguration.hasCopyNumberData()">
                <!-- BEGIN - Saved Copy Number Analysis -->
                <li style="max-height: 150px; overflow-x:hidden; overflow-y: auto;"><a href="#">Saved Copy Number Analysis</a>
                    <ul style="padding: 3px 0px 10px 10px;">                  
	                    <s:iterator value="displayableWorkspace.userGisticAnalysisList">
	                        <s:url id="editGisticUrl" action="editGisticAnalysis" includeParams="none">
	                            <s:param name="selectedAction" value="%{'edit'}" />
	                            <s:param name="gisticAnalysis.id" value="id" />
	                        </s:url>
	              
	                        <li style="padding: 0px 0px 2px 0px">
	                            <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
	                                <div style="float: left; white-space: nowrap; width: 32px;">
	                                    <s:a href="%{editGisticUrl}" cssClass="queryAction" cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
	                                        title="Edit query: %{name}">&nbsp;</s:a>
	                                </div>
	                                <div style="float: right; width: 110px; white-space: normal; padding: 4px 0px 0px 0px;">
	                                    <s:a href="%{editGisticUrl}" onclick="showBusyDialog();" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;" title="Edit: %{name}">
	                                        <s:if test="id == openQueryId">
	                                            <strong><s:property value="name"/></strong>
	                                        </s:if>
	                                        <s:else>
	                                            <s:property value="name"/>
	                                        </s:else>
	                                    </s:a>
	                                </div>
	                            </div>
	                        </li>
	                    </s:iterator>
                    </ul>
                </li>
                <!-- END - Saved Copy Number Analysis -->
                </s:if>
                
            </ul>
            </s:else>
        </li>
        
        <li class="stdnavforinvestigator"><div>Analysis Tools</div>
            <ul>
                <li>
                    <s:url id="kmPlotUrl" includeParams="none" action="initializeKmPlot" />
                    <a href="${ kmPlotUrl }">KM Plot</a></li>
                <li>
                    <s:url id="gePlotUrl" includeParams="none" action="initializeGePlot" />
                    <a href="${ gePlotUrl }">Gene Expression Plot</a></li>
                <li>
                    <s:url id="genePatternAnalysisUrl" includeParams="none" action="genePatternAnalysisStatus" />
                    <a href="${genePatternAnalysisUrl}">GenePattern Analysis</a>
                </li>
            </ul>
        </li>
        <s:if test = "%{!currentStudy.studyConfiguration.externalLinkLists.isEmpty()}" >
        <li class="stdnavforinvestigator"><div>External Links</div>
            <ul>
                <s:iterator value="currentStudy.studyConfiguration.externalLinkLists" >
                    <li>
                        <s:url id="externalLinkUrl" includeParams="none" action="viewExternalLinks" >
                            <s:param name="externalLinkList.id" value="id"/>
                        </s:url>
                        <a href="${ externalLinkUrl }" title='Description: <s:property value="description"/>' ><s:property value="name"/></a>
                    </li>
                </s:iterator>
            </ul>
        </li>
        </s:if>
        
    </ul>
    </s:if>
         
    <s:if test="#sessionHelper.studyManager">
        <ul class="menu">   
            <li class="stdnav"><div>Study Management</div>
                <ul>
                    <li><s:url id="manageStudiesUrl" includeParams="none" action="manageStudies" />
                    <a href="${manageStudiesUrl}">Manage Studies</a></li>
                    <li><s:url id="createStudyUrl" includeParams="none" action="createStudy" />
                    <a href="${createStudyUrl}">Create New Study</a></li>
                </ul>
            </li>
        </ul>
    </s:if>
    <s:if test="#sessionHelper.platformManager">
        <ul class="menu">   
            <li class="stdnav"><div>Application Management</div>
                <ul>
                    <li><s:url id="managePlatformsUrl" includeParams="none" action="managePlatforms" />
                    <a href="${managePlatformsUrl}">Manage Platforms</a></li>
                </ul>
            </li>
        </ul>
    </s:if>
    
    <ul class="menu">
        <li class="stdnavforinvestigator" style="padding-bottom:0;"><div><span class="lowercase">ca</span>Integrator2 Menu</div>
            <ul>
                <s:if test="#sessionHelper.anonymousUser">
                    <li><a href="/caintegrator2/logout.jsp">Login</a></li>
                    <li><a href="/caintegrator2/registration/input.action">Register</a></li>
                </s:if>
                <s:if test="!#sessionHelper.authenticated">
                    <li><a href="/caintegrator2/registration/input.action">Register</a></li>
                    <li><a href="/caintegrator2/workspace.action">Browse Public Studies</a></li>
                </s:if>
                <li><a href="javascript:openHelpWindowWithNavigation('app_support_help')">Support</a></li>
                <li><a href="${tutorialsUrl}">Tutorials</a></li>
                <li><a href="javascript:openUsersGuideWindow('top')">User Guide</a></li>
            </ul>
        </li>
    </ul>
    
    <!--/Menu-->
                
</div>
