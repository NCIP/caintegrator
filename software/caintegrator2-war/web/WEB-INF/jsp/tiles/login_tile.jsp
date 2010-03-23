<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">
    
    <div class="box">
    <h2 class="darker" style="padding: 6px 10px;">Welcome to caIntegrator2 </h2>
    
    <p style="padding: 0.25em 0pt 1em 2em; max-width: 800px;">
        caIntegrator2 is a web-based software application that allows researchers to set up custom, caBIG®-compatible web portals to conduct integrative research, without requiring programming experience. These portals bring together heterogeneous clinical, microarray and medical imaging data to enrich multidisciplinary research.
    </p>
    </div>
    
    <hr>

    <!--Page Help-->
        
    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('login_help')" class="help">
   &nbsp;</a>
    </div>
    
    <!--/Page Help-->
    
    <font color="green"> <s:actionmessage /> </font>
    <!--ADD CONTENT HERE-->
    <s:if test="#session['ACEGI_SECURITY_LAST_EXCEPTION'] != null">
        <font color="red">Invalid username/password. Please try again.</font>
    </s:if>
    
    <h1><s:property value="#subTitleText" /></h1>
    <s:form name="loginForm" method="POST" action="/j_acegi_security_check">
	   <s:textfield label="Username" name="j_username" />
	   <s:password label="Password" name="j_password" />
	   <s:submit value="Login" />
	   <tr><td><s:a href="registration/input.action">Register Now</s:a></td></tr>
       
    </s:form>
    
</div>

<div class="clear"><br /></div>
<script language="javascript">
     document.loginForm.j_username.focus();
</script>

