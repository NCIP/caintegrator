<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">

    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('login_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->        
    <font color="green"> <s:actionmessage /> </font>
    <!--ADD CONTENT HERE-->
    <s:if test="#session['ACEGI_SECURITY_LAST_EXCEPTION'] != null">
        <font color="red">Invalid username/password. Please try again.</font>
    </s:if>
    
    <h1><s:property value="#subTitleText" /></h1>
   
    <s:form name="loginForm" method="GET" action="/j_acegi_security_check">
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

