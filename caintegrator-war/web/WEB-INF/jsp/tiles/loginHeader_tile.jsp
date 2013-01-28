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
            <a href="/caintegrator/index.jsp"><img src="/caintegrator/images/logo_caintegrator2.gif" alt="Logo: caIntegrator2" /></a>
        </div>
        <s:set name="holdurl" value="#application['gitUrl']"/>
        <s:set name="holdversion" value="#application['caIntegratorVersion']"/>
        <s:set name="holdrevision" value="#application['gitRevision']"/>
        <s:set name="holdbuilddate" value="#application['builddate']"/>
        <s:div id="versioninfo" title="caINTEGRATOR, BUILD DATE: %{holdbuilddate}, VERSION: %{holdversion}, URL: %{holdurl}, REVISION: %{holdrevision}">
            version: <s:property value="%{holdversion}"/> | 
            date: <s:property value="%{holdbuilddate}"/>           
        </s:div>
    </div>

</div>

<!--/PO-Curate Header-->
