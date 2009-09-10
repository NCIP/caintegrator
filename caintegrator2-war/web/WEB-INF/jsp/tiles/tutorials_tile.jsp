<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <h1><s:property value="#subTitleText" /></h1>
    <div class="tutorials">
        <a href="javascript:openOverviewTutorialWindow('overviewTutorial')" class="video">
            A short overview of the functionality of caIntegrator2 and how it integrates with other caBIG tools
        </a>
    </div>
    <div class="tutorials">
        <a href="javascript:openDeployStudyTutorialWindow('deployStudyTutorial')" class="video">
            A demonstration of how a user can deploy a new integrative study using caIntegrator2
        </a>
    </div>
    <div class="tutorials">
        <a href="javascript:openUseStudyTutorialWindow('useStudyTutorial')" class="video">
            A demonstration of the study querying and analysis capabilities of caIntegrator2
        </a>
        
    </div>
    <!--Buttons-->

    <s:div theme="xhtml">
        <ul class="btnrow">
            <li><s:a href="/caintegrator2/index.jsp" cssClass="btn">
                <span class="btn_img">Cancel</span>
            </s:a></li>
        </ul>
    </s:div>
    <!--/Buttons-->
</div>

<div class="clear"><br /></div>
