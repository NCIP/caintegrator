<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
            
<div id="leftnav">
    
    <!--Study Logo-->
    
    <div id="study_logo"><img src="images/logo_sample_vasari.gif" alt="Study Logo" /></div>
    
    <!--/Study Logo-->
    
    <!--Menu-->
    <s:url id="manageQueryUrl" action="manageQuery" includeParams="all" escapeAmp ="false">
        <s:param name="selectedAction">createNewQuery</s:param>
    </s:url>

    <ul class="menu">
        <s:set name="sessionHelper" value="#session['sessionHelper']" />
        <li class="stdnav"><div><s:property value="study.shortTitleText"/></div>
            <ul>
                <li><s:url id="homePageUrl" action="workspace" /><a href="${ homePageUrl }">Home</a></li>
                <li class="stdnav"><a href='<s:property value="#manageQueryUrl" />'>Search <s:property value="#sessionHelper.displayableStudySubscription.currentStudySubscription.study.shortTitleText"/></a></li>
            </ul>
        </li>
        
        <s:if test="#sessionHelper.studyManager">
             <li class="stdnav"><div>Study Elements</div>
                <ul>
                    <li><a href="study_elements/subjects.html">Subjects</a></li>
                    <li><a href="study_elements/samples.html">Samples</a></li>
                    <li><a href="study_elements/array_data.html">Array Data</a></li>
                    <li><a href="study_elements/images.html">Images</a></li>
                </ul>
            </li>
        </s:if>
        <!--Tree Control-->
        
        <li class="treenav"><div>Study Data</div>
            <ul class="pde">
                <li><a href="#">Queries</a>
                    <ul>
                        <li><a href="#">Global Queries</a>
                            <ul>
                                <li><a href='<s:property value="#executeQueryUrl" />'>Sample Global Query A</a></li>
                                <li><a href="#" class="queries">Sample Global Query B</a></li>
                                <li><a href="#" class="queries">Sample Global Query C</a></li>
                            </ul>
                        </li>
                        <li><a href="#">Shared Queries</a>
                            <ul>
                                <li><a href="#" class="queries">Sample Shared Query A</a></li>
                                <li><a href="#" class="queries">Sample Shared Query B</a></li>
                                <li><a href="#" class="queries">Sample Shared Query C</a></li>
                            </ul>
                        </li>
                        <li><a href="#">My Queries</a>
                            <ul>
                                <li><a href='/caintegrator2/executeQuery.action?queryName=simple#searchresults' class="queries">Disease = Astrocytoma</a></li>
                                <li><a href='/caintegrator2/executeQuery.action?queryName=image#searchresults' class="queries">Image Series Query</a></li>
                                <li><a href='/caintegrator2/executeQuery.action?queryName=genomic#searchresults' class="queries">Genomic Data Query</a></li>
                                
                                <s:iterator value="studySubscription.queryCollection">
                                    <s:url id="queryURL" action="executeQuery">
                                        <s:param name="queryId" value="id" />
                                    </s:url>
                                    <li><s:a href="%{queryURL}" cssClass="queries"><s:property value="name"/></s:a></li>
                                </s:iterator>
                                
                                <!-- li><a href='/caintegrator2/executeQuery.action?queryName=genomic' class="queries">Genomic Query</a></li -->
                            </ul>
                        </li>
                    </ul>
                </li>
                <li><a href="#">Data Lists</a>
                    <ul>
                        <li><a href="#">Global Data Lists</a>
                            <ul>
                                <li><a href="#" class="lists">Sample Global Data List A</a></li>
                                <li><a href="#" class="lists">Sample Global Data List B</a></li>
                                <li><a href="#" class="lists">Sample Global Data List C</a></li>
                            </ul>
                        </li>
                        <li><a href="#">Shared Data Lists</a>
                            <ul>
                                <li><a href="#" class="lists">Sample Shared Data List A</a></li>
                                <li><a href="#" class="lists">Sample Shared Data List B</a></li>
                                <li><a href="#" class="lists">Sample Shared Data List C</a></li>
                            </ul>
                        </li>
                        <li><a href="#">My Data Lists</a>
                            <ul>
                                <li><a href="#" class="lists">My Sample Data List A</a></li>
                                <li><a href="#" class="lists">My Sample Data List B</a></li>
                                <li><a href="#" class="lists">My Sample Data List C</a></li>
                            </ul>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>
        
        <!--/Tree Control-->
        
        <li class="stdnav"><div>Analysis Tools</div>
            <ul>
                <li><a href="analysis_tools/generate_km_plot.html">Generate KM Plot</a></li>
                <li><a href="analysis_tools/generate_heat_map.html">Generate Heat Map</a></li>
                <li>
                    <s:url id="genePatternAnalysisUrl" action="genePatternAnalysis" />
                    <a href="${genePatternAnalysisUrl}">GenePattern Analysis</a>
                </li>
            </ul>
        </li>
        <s:if test="#sessionHelper.studyManager">
            <li class="stdnav"><div>Study Management</div>
                <ul>
                    <li><s:url id="manageStudiesUrl" action="manageStudies" />
                    <a href="${manageStudiesUrl}">Manage Studies</a></li>
                    <li><s:url id="createStudyUrl" includeParams="none" action="createStudy" />
                    <a href="${createStudyUrl}">Create New Study</a></li>
                </ul>
            </li>
        </s:if>
        <li class="stdnav" style="padding-bottom:0;"><div><span class="lowercase">ca</span>Integrator2 Menu</div>
            <ul>
                
                <li><s:url id="workspaceUrl" action="workspace" />
                <a href="${workspaceUrl}">Workspace</a></li>
                <li class="stdnav"><a href="search.html">Advanced Search</a></li>                
                <li><a href="#">Support</a></li>
                <li><a href="#">Tutorials</a></li>
                <li><a href="#">User Guide</a></li>
            </ul>
        </li>
    </ul>
    
    <!--/Menu-->
                
</div>
