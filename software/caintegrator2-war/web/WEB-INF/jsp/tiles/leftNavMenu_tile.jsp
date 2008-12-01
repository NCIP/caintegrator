<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
            
<div id="leftnav">
    
    <!--Study Logo-->
    
    <div id="study_logo">
        <s:set name="logo" id="logo" value="%{displayableWorkspace.logoUrl}"/>
        <img src="${logo}" alt="Study Logo" height="72" width="200"/>
    </div>
    
    <!--/Study Logo-->
    
    <!--Menu-->
    <s:url id="newQueryUrl" includeParams="none" action="newQuery" />
    
    <s:url id="notYetImplementedUrl" includeParams="none" action="notYetImplemented" />
    
    <ul class="menu">
        <s:set name="sessionHelper" value="#session['sessionHelper']" />
        <li class="stdnav"><div><s:property value="study.shortTitleText"/></div>
            <ul>
                <li><s:url id="homePageUrl" includeParams="none" action="workspace" /><a href="${ homePageUrl }">Home</a></li>
                <li class="stdnav"><a href='<s:property value="#newQueryUrl" />'>Search <s:property value="studySubscription.study.shortTitleText"/></a></li>
            </ul>
        </li>
        
        <s:if test="#sessionHelper.studyManager">
             <li class="stdnav"><div>Study Elements</div>
                <ul>
                    <li><a href="${ notYetImplementedUrl }">Subjects</a></li>
                    <li><a href="${ notYetImplementedUrl }">Samples</a></li>
                    <li><a href="${ notYetImplementedUrl }">Array Data</a></li>
                    <li><a href="${ notYetImplementedUrl }">Images</a></li>
                </ul>
            </li>
        </s:if>
        <!--Tree Control-->
        
        <li class="treenav"><div>Study Data</div>
            <ul class="pde">
                <li><a href="#">Queries</a>
                    <ul>
                        <li><a href="#">My Queries</a>
                            <ul>
                                <li><a href='/caintegrator2/executeQuery.action?queryName=simple#searchresults' class="queries">Disease = Astrocytoma</a></li>
                                <li><a href='/caintegrator2/executeQuery.action?queryName=image#searchresults' class="queries">Image Series Query</a></li>
                                <li><a href='/caintegrator2/executeQuery.action?queryName=genomic#searchresults' class="queries">Genomic Data Query</a></li>
                                
                                <s:iterator value="studySubscription.queryCollection">
                                    <s:url id="queryURL" action="executeQuery">
                                        <s:param name="queryId" value="id" />
                                    </s:url>
                                    <li><s:a href="%{queryURL}#searchresults" cssClass="queries"><s:property value="name"/></s:a></li>
                                </s:iterator>
                                
                                <!-- li><a href='/caintegrator2/executeQuery.action?queryName=genomic' class="queries">Genomic Query</a></li -->
                            </ul>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>
        
        <!--/Tree Control-->
        
        <li class="stdnav"><div>Analysis Tools</div>
            <ul>
                <li>
                    <s:url id="kmPlotUrl" includeParams="none" action="kmPlotInput" />
                    <a href="${ kmPlotUrl }">Generate KM Plot</a></li>
                <li>
                    <s:url id="genePatternAnalysisUrl" includeParams="none" action="genePatternAnalysis" />
                    <a href="${genePatternAnalysisUrl}">GenePattern Analysis</a>
                </li>
            </ul>
        </li>
        <s:if test="#sessionHelper.studyManager">
            <li class="stdnav"><div>Study Management</div>
                <ul>
                    <li><s:url id="manageStudiesUrl" includeParams="none" action="manageStudies" />
                    <a href="${manageStudiesUrl}">Manage Studies</a></li>
                    <li><s:url id="createStudyUrl" includeParams="none" action="createStudy" />
                    <a href="${createStudyUrl}">Create New Study</a></li>
                </ul>
            </li>
        </s:if>
        <li class="stdnav" style="padding-bottom:0;"><div><span class="lowercase">ca</span>Integrator2 Menu</div>
            <ul>
                <li><a href="${ notYetImplementedUrl }">Advanced Search</a></li>                
                <li><a href="${ notYetImplementedUrl }">Support</a></li>
                <li><a href="${ notYetImplementedUrl }">Tutorials</a></li>
                <li><a href="${ notYetImplementedUrl }">User Guide</a></li>
            </ul>
        </li>
    </ul>
    
    <!--/Menu-->
                
</div>
