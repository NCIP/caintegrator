<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <h1><s:property value="#subTitleText" /></h1>
    <div class="tutorials">
        <a href="javascript:openOverviewTutorialWindow('overviewTutorial')" class="video">
            A short overview of the functionality of caIntegrator and how it integrates with other tools
        </a>
    </div>
    <div class="tutorials">
        <a href="javascript:openDeployStudyTutorialWindow('deployStudyTutorial')" class="video">
            A demonstration of how a user can deploy a new integrative study using caIntegrator
        </a>
    </div>
    <div class="tutorials">
        <a href="javascript:openUseStudyTutorialWindow('useStudyTutorial')" class="video">
            A demonstration of the study querying and analysis capabilities of caIntegrator
        </a>
        
    </div>
    <!--Buttons-->

    <s:div theme="xhtml">
        <ul class="btnrow">
            <li><s:a href="/caintegrator/index.jsp" cssClass="btn">
                <span class="btn_img">Cancel</span>
            </s:a></li>
        </ul>
    </s:div>
    <!--/Buttons-->
</div>

<div class="clear"><br /></div>
