<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
            
<div id="leftnav">
    
    <!--Study Logo-->
    
    <div id="study_logo"><img src="images/logo_sample_vasari.gif" alt="Study Logo" /></div>
    
    <!--/Study Logo-->
    
    <!--Menu-->
    <s:url id="manageQueryUrl" action="manageQuery" includeParams="get">
        <s:param name="injectTest">yes</s:param>
    </s:url>
    
    <ul class="menu">
        <li class="stdnav"><div>VASARI</div>
            <ul>
                <li><a href="home.html" class="selected">Homepage</a></li>
                <li class="stdnav"><a href='<s:property value="#manageQueryUrl" />'>Search VASARI</a></li>            
                <li><s:url id="workspaceUrl" action="workspace" />
                <a href="${workspaceUrl}">Workspace</a></li>
                <li class="stdnav"><a href="search.html">Advanced Search</a></li>
            </ul>
        </li>
        <authz:authorize ifAnyGranted="MODIFY_STUDY_CREATE">
        <li class="stdnav"><div>Study Management</div>
            <ul>
                <li><s:url id="manageStudiesUrl" action="manageStudies" />
                <a href="${manageStudiesUrl}">Manage Studies</a></li>
                <li><s:url id="createStudyUrl" action="createStudy" />
                <a href="${createStudyUrl}">Create New Study</a></li>
            </ul>
        </li>
         <li class="stdnav"><div>Study Elements</div>
            <ul>
                <li><a href="study_elements/subjects.html">Subjects</a></li>
                <li><a href="study_elements/samples.html">Samples</a></li>
                <li><a href="study_elements/array_data.html">Array Data</a></li>
                <li><a href="study_elements/images.html">Images</a></li>
            </ul>
        </li>
        </authz:authorize>
        <!--Tree Control-->
        
        <li class="treenav"><div>Study Data</div>
            <ul class="pde">
                <li><a href="#">Queries</a>
                    <ul>
                        <li><a href="#">Global Queries</a>
                            <ul>
                                <li><a href="#" class="queries">Sample Global Query A</a></li>
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
                                <li><a href="#" class="queries">My Query A</a> <a href='<s:property value="#manageQueryUrl" />'>(edit)</a></li>
                                <li><a href="#" class="queries">My Query B</a></li>
                                <li><a href="#" class="queries">My Query C</a></li>
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
                <li><a href="analysis_tools/genepattern_analysis.html">GenePattern Analysis</a></li>
            </ul>
        </li>
        <li class="stdnav" style="padding-bottom:0;"><div><span class="lowercase">ca</span>Integrator2 Menu</div>
            <ul>
                <li><a href="#">Deploy New Study</a></li>
                <li><a href="#">Support</a></li>
                <li><a href="#">Tutorials</a></li>
                <li><a href="#">User Guide</a></li>
            </ul>
        </li>
    </ul>
    
    <!--/Menu-->
                
</div>
