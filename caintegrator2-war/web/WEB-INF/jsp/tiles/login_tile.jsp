<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <h1>Login</h1>
    
    <s:form method="POST" action="j_acegi_security_check">
	    <s:textfield label="Username" name="j_username" />
	    <s:password label="Password" name="j_password" />
	    <s:submit value="Login" />
    </s:form>

            
</div>

<div class="clear"><br /></div>
