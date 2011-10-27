<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>

<!--NCI/NIH Header-->
            
<div id="nciheader">        
    <div id="ncilogo"><a href="http://www.cancer.gov"><img
        src="/caintegrator/images/logotype.gif" width="283" height="37"
        alt="Logo: National Cancer Institute" /></a>
    </div>
    <div id="nihtag"><a href="http://www.cancer.gov"><img
        src="/caintegrator/images/tagline.gif" width="295" height="37"
        alt="Logo: U.S. National Institutes of Health | www.cancer.gov" /></a>
    </div>

</div>
        
<!--/NCI/NIH Header-->

<!--PO-Curate Header-->

<div id="appheader">

    <div id="mainlogo">
        <div>
            <a href="index.jsp"><img src="/caintegrator/images/logo_caintegrator2.gif" alt="Logo: caIntegrator" /></a>
        </div>
        <s:set name="holdsvnurl" value="#application['svnUrl']"/>
        <s:set name="holdsvntag" value="#application['caintegrator2Svntag']"/>
        <s:set name="holdsvnrev" value="#application['svnRevision']"/>
        <s:set name="holdbuilddate" value="#application['caintegrator2Builddate']"/>
        <s:div id="versioninfo" title="caINTEGRATOR2, BUILD DATE: %{holdbuilddate}, SVNTAG: %{holdsvntag}, SVNURL: %{holdsvnurl}, SVNREV: %{holdsvnrev}">
            build: <s:property value="%{holdsvntag}"/> | 
            date: <s:property value="%{holdbuilddate}"/>           
        </s:div>
    </div> 

    <div id="userarea_wrapper">
        <s:set name="sessionHelper" value="#session['sessionHelper']" />
        <s:if test="!#sessionHelper.authenticated">
            <s:action name="workspace"/>
        </s:if>
        <div id="user">
            <s:url id="logoutUrl" value="/logout.action" includeParams="none">
                <s:param name="struts.token.name">struts.token</s:param>
                <s:param name="struts.token" value="%{struts.token}" />
            </s:url>
	        <s:if test="anonymousUser"> 
	        Currently not logged in <s:if test="%{selectedPage!='register'}">| <a href="${logoutUrl}">Login</a>
	        </s:if>
	        </s:if>
	        <s:else>
	        Welcome, <s:property value="#sessionHelper.username"/> | <a href="${logoutUrl}">Logout</a>
	        </s:else>
        </div>
        <div id="mystudies">
               <s:form action="workspace" theme="simple">

                    <label for="currentStudySubscriptionId">
	                    <s:if test="anonymousUser"> 
	                        Public 
	                    </s:if>
	                    <s:else>
	                        My
	                    </s:else>
	                       Studies:
                    </label>
                    <s:select label="My Studies" 
                        name="currentStudySubscriptionId" 
                        value="displayableWorkspace.currentStudySubscriptionId" 
                        headerKey="%{@gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace@NO_STUDY_SELECTED_ID}" 
                        headerValue="-- Please Select --"
                        list="displayableWorkspace.orderedSubscriptionList" 
                        listKey="id"
                        listValue="study.shortTitleText" onchange="this.form.submit();" theme="simple" />

               </s:form>
        </div>    
   
         
        
    </div>

</div>

<!--/PO-Curate Header-->
