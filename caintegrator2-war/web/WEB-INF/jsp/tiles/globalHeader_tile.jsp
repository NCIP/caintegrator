<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>

<!--NCI/NIH Header-->
            
<div id="nciheader">        
    <div id="ncilogo"><a href="http://www.cancer.gov"><img
        src="images/logotype.gif" width="283" height="37"
        alt="Logo: National Cancer Institute" /></a>
    </div>
    <div id="nihtag"><a href="http://www.cancer.gov"><img
        src="images/tagline.gif" width="295" height="37"
        alt="Logo: U.S. National Institutes of Health | www.cancer.gov" /></a>
    </div>

</div>
        
<!--/NCI/NIH Header-->

<!--PO-Curate Header-->

<div id="appheader">

    <div id="mainlogo">
        <div>
            <a href="index.jsp"><img src="images/logo_caintegrator2.gif" alt="Logo: caIntegrator2" /></a>
        </div>
        <div id="versioninfo">
            version: <s:property value="#application['caintegrator2Version']"/>,
            revision: <s:property value="#application['svnRevision']"/>            
        </div>
    </div> 

    <div id="userarea_wrapper">
        <s:set name="sessionHelper" value="#session['sessionHelper']" />
        <s:if test="!#sessionHelper.authenticated">
            <s:action name="workspace"/>
        </s:if>
        <div id="user">Welcome, <s:property value="#sessionHelper.username"/> | <a href="logout.jsp">Logout</a></div>
        <div id="mystudies">
               <s:form action="workspace" theme="simple">

                    <label for="currentStudySubscriptionId">My Studies:</label>
                    <s:select label="My Studies" 
                        name="currentStudySubscriptionId" 
                        value="displayableWorkspace.currentStudySubscriptionId" 
                        headerKey="%{@gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace@NO_STUDY_SELECTED_ID}" 
                        headerValue="-- Please Select --"
                        list="workspace.subscriptionCollection" 
                        listKey="id"
                        listValue="study.shortTitleText" onchange="this.form.submit();" theme="simple" />

               </s:form>
        </div>    
   
         
        
    </div>

</div>

<!--/PO-Curate Header-->
