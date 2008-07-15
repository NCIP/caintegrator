<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <h1>Login</h1>
    
    <s:form method="POST" action="j_acegi_security_check">
        <table>
            <tr>
                <th>Username</th>
                <td><s:textfield name="j_username" /></td>
            </tr>
            <tr>
                <th>Password</th>
                <td><s:password name="j_password" /></td>
            </tr>
            <tr>
                <td colspan="2"><s:submit value="Login" /></td>
            </tr>
        </table>
    </s:form>

            
</div>

<div class="clear"><br /></div>
