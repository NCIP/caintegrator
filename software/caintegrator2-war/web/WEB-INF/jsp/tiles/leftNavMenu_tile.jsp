<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
            
<div id="leftnav">
    
    <!--Study Logo-->
    <s:if test="currentStudy != null">
	    <div id="study_logo">
	        <s:set name="logo" id="logo" value="%{displayableWorkspace.logoUrl}"/>
	        <img src="${logo}" alt="Study Logo" height="72" width="200"/>
	    </div>
    </s:if>
    
    <!--/Study Logo-->
    
    <!--Menu-->
    <s:url id="newQueryUrl" includeParams="none" action="newQuery" />
    
    <s:url id="notYetImplementedUrl" includeParams="none" action="notYetImplemented" />
    
    <s:url id="tutorialsUrl" includeParams="none" action="tutorials" />
    
    <s:set name="sessionHelper" value="#session['sessionHelper']" />

    <s:if test="currentStudy != null">    
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
                                <s:param name="selectedAction" value="%{'newGeneList'}" />
                    <a href="${createGeneListUrl}">Create New Gene List</a>
                </li>
            </ul>
        </li>
        
        <!--Tree Control-->
        
        <li class="treenav"><div>Study Data</div>
            <ul class="pde">
                <li><a href="#">Queries</a>
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
                                                <s:a href="%{runQueryUrl}" cssClass="searches" cssStyle="background:transparent url('/caintegrator2/images/ico_search.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Run query: %{name}">&nbsp;</s:a>
                                                <s:a href="%{editQueryUrl}" cssClass="queryAction" cssStyle="background:transparent url('/caintegrator2/images/ico_edit.gif') no-repeat scroll 0 0; padding:0px 8px 5px 8px;"
                                                    title="Edit query: %{name}">&nbsp;</s:a>
                                            </div>
                                            <div style="float: right; width: 110px; white-space: normal; padding: 0px 0px 0px 0px;">
                                                <s:a href="%{runQueryUrl}" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;" title="Description: %{description}">
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
                        <li><a href="#">My Gene Lists</a>
                            <ul>
                                <s:iterator value="displayableWorkspace.geneLists">
                                    <s:url id="editGeneListUrl" action="editGeneList" includeParams="none">
                                        <s:param name="selectedAction" value="%{'editGeneList'}" />
                                        <s:param name="geneListName" value="name" />
                                    </s:url>

                                    <li style="padding: 0px 0px 2px 0px">
                                        <div style="margin-bottom: 5px; white-space: nowrap; width: 164px;">
                                            <div style="float: right; width: 110px; white-space: normal; padding: 0px 0px 0px 0px;">
                                                <s:a href="%{editGeneListUrl}" cssClass="queries" cssStyle="padding: 0px 0px 0px 0px;"
                                                    title="Description: %{description}"><s:property value="name" />
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
                <s:if test="!#sessionHelper.authenticated">
                    <li><a href="/caintegrator2/index.jsp">Login</a></li>
                    <li><a href="registration/input.action">Register</a></li>
                </s:if>
                <li><a href="javascript:openWin('/caintegrator2//help/Getting_Started.2.8.html#1122280')">Support</a></li>
                <li><a href="${tutorialsUrl}">Tutorials</a></li>
                <li><a href="javascript:openUsersGuideWindow('top')">User Guide</a></li>
            </ul>
        </li>
    </ul>
    
    <!--/Menu-->
                
</div>
