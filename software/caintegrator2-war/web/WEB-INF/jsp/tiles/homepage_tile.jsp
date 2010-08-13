<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

            
<div id="content">                      
    
    <!--Page Help-->

    <div class="tabhelp" style="margin-right: 0.5em;margin-top: 1.75em;"><a href="javascript:openHelpWindowWithNavigation('welcome_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->         
    
    <!--ADD CONTENT HERE-->
    
    <s:actionerror/>
    <br></br>
    <div class="box">
    <h2 class="darker" style="padding: 6px 10px;">Welcome to caIntegrator </h2>
    
    <p style="padding: 0.25em 0pt 1em 2em; max-width: 800px;">
        caIntegrator is a web-based software application that allows researchers to set up custom, caBIG®-compatible web portals to conduct integrative research, without requiring programming experience. These portals bring together heterogeneous clinical, microarray and medical imaging data to enrich multidisciplinary research.
    </p>
    <p style="padding: 0.25em 0pt 1em 2em; max-width: 800px; color: rgb(71, 91, 130);"><font size="3"><b>To begin select a study from the 
		<s:if test="anonymousUser"> 
		"Public 
	    </s:if>
	    <s:else>
	     "My
	    </s:else> 
    Studies" drop down menu.</b></font></p>
    <s:if test="anonymousUser"><p style="padding: 0.25em 0pt 1em 2em; max-width: 800px;">Some features that allow a user to customize caIntegrator2 require a login.  <b>You are currently not logged in.</b></p></s:if>
    </div>
    
    <!-- For unauthenticated or anonymous users, have a login form. -->
    <s:if test="%{anonymousUser || !#session['sessionHelper'].authenticated}">
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
	    <script language="javascript">
	        document.loginForm.j_username.focus();
        </script>
    </s:if>
    
</div>

<div class="clear"><br /></div>
