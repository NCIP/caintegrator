<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>


<!--NCI/NIH Header-->
            
<div id="nciheader">        
    <div id="ncilogo"><a href="http://www.cancer.gov"><img
        src="/caintegrator2/images/logotype.gif" width="283" height="37"
        alt="Logo: National Cancer Institute" /></a>
    </div>
    <div id="nihtag"><a href="http://www.cancer.gov"><img
        src="/caintegrator2/images/tagline.gif" width="295" height="37"
        alt="Logo: U.S. National Institutes of Health | www.cancer.gov" /></a>
    </div>

</div>
        
<!--/NCI/NIH Header-->

<!--PO-Curate Header-->

<div id="appheader">

    <div id="mainlogo">
        <div>
            <a href="index.jsp"><img src="/caintegrator2/images/logo_caintegrator2.gif" alt="Logo: caIntegrator2" /></a>
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

</div>

<!--/PO-Curate Header-->
