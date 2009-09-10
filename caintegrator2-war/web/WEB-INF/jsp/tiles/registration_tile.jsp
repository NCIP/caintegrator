<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">
    <script language="javascript">
        function toggleLdap() {
            var tbody = document.getElementById('acct_details');
            var isLdap = document.getElementById('registrationForm_ldapAuthenticatetrue').checked;
            if (isLdap) {
                tbody.style.display = "";
            } else {
                tbody.style.display = "none";
            }
        }
    </script>
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('register_user_help')" class="help">
    &nbsp;</a>
    </div>

    <!--/Page Help-->        
    <s:actionerror/>
    
    <h1><s:property value="#subTitleText" /></h1>
   
    <s:form name="registrationForm" id="registrationForm" action="save">
        <table>
        <tbody>
        <tr><td colspan="2"><h2>Security Information</h2></td></tr>
        </tbody>
        <s:if test="%{ldapInstall}">
	        <tbody>
	            <s:radio
	                    name="ldapAuthenticate" label="Do you have an LDAP Account?" list="#{true: 'Yes', false:'No'}"
	                    onchange="toggleLdap();" onclick="toggleLdap();"/>
	        
	        </tbody>
	        <tbody id="acct_details" <s:if test="%{!ldapAuthenticate}">style="display: none"</s:if> >
		    <s:textfield label="Username" name="registrationRequest.loginName" required="true"/>
		    <s:password label="Password" name="password" required="true"/>
		    </tbody>
	    </s:if>
	    <tbody>
	    <s:checkboxlist name="registrationRequest.role" label="Requested Role(s)" 
                list="@gov.nih.nci.caintegrator2.web.action.registration.UserRole@values()" listKey="name" listValue="name" required="true"/>
        <s:textfield name="registrationRequest.requestedStudies" label="Existing Studies to be Accessed" size="50" />
        <tr><td colspan="2"><h2>Account Details</h2></td></tr>
        <s:textfield name="registrationRequest.firstName" label="First Name" size="50" required="true" />
        <s:textfield name="registrationRequest.lastName" label="Last Name" size="50" required="true" />
        <s:textfield name="registrationRequest.email" label="Email" size="50" required="true" />
        <s:textfield name="registrationRequest.organization" label="Organization" size="50" required="true" />
        <s:textfield name="registrationRequest.address1" label="Address 1" size="50" required="true" />
        <s:textfield name="registrationRequest.address2" label="Address 2" size="50" />
        <s:textfield name="registrationRequest.city" label="City" size="50" required="true" />
        <s:textfield name="registrationRequest.state" label="State" size="50" required="true" />
        <s:textfield name="registrationRequest.country" label="Country" size="50" required="true" />        
        <s:textfield name="registrationRequest.zip" label="Postal Code" size="50" required="true" />
        <s:textfield name="registrationRequest.phone" label="Phone" size="50" required="true" />
        <s:textfield name="registrationRequest.fax" label="Fax" size="50" />
        <td colspan="2" align="right">
        <s:submit value="Cancel" action="cancel" theme="simple"/>
	    <s:submit value="Submit Registration Request" action="save" theme="simple"/>
	    </td>
	    </tbody>
	    </table>
    </s:form>
    
            
</div>

<div class="clear"><br /></div>

