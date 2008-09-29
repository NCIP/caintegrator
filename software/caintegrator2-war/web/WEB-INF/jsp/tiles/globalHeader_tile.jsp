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

    <div id="mainlogo"><a href="index.jsp"><img
        src="images/logo_caintegrator2.gif" alt="Logo: caIntegrator2" /></a>
    </div>


    <div id="userarea_wrapper">
        <s:set name="sessionHelper" value="#session['sessionHelper']" />
        <s:if test="!#sessionHelper.authenticated">
            <s:action name="workspace"/>
        </s:if>
        <div id="user">Welcome, <a href="account.html"><s:property value="#sessionHelper.username"/> </a> | <a
           href="logout.jsp">Logout</a></div>
        <div id="mystudies">
               <s:form action="homepage.getStudyDetails" theme="simple">

                    <label for="studynav">My Studies:</label>
                    <s:select label="My Studies" name="studynav" headerKey="1" headerValue="-- Please Select --"
                    list="#sessionHelper.displayableUserWorkspace.studySubscriptions" listKey="study.shortTitleText"
                    listValue="study.shortTitleText" onchange="this.form.submit();" theme="simple" />

               </s:form>
        </div>    
   
         
        
    </div>

</div>

<!--/PO-Curate Header-->
