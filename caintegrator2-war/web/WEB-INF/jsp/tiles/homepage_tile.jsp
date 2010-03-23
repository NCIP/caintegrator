<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('welcome_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->         
    
    <!--ADD CONTENT HERE-->
    
    <s:actionerror/>
    <div class="box">
    <h2 class="darker" style="padding: 6px 10px;">Welcome to caIntegrator2 </h2>
    
    <p style="padding: 0.25em 0pt 1em 2em; max-width: 800px;">
        caIntegrator2 is a web-based software application that allows researchers to set up custom, caBIG®-compatible web portals to conduct integrative research, without requiring programming experience. These portals bring together heterogeneous clinical, microarray and medical imaging data to enrich multidisciplinary research.
    </p>
    <s:if test="anonymousUser"><p style="padding: 0.25em 0pt 1em 2em; max-width: 800px;"><b>You are currently not logged in</b>, to do so please click "Login" in the left menu.  You will have limited access to only public studies when not logged in and some features are not available.</p></s:if>
    <p style="padding: 0.25em 0pt 1em 2em; max-width: 800px;"><font size="2"><b>To begin please select a study from the "My Studies" drop down menu.</b></font></p>
    </div>
    
</div>

<div class="clear"><br /></div>
