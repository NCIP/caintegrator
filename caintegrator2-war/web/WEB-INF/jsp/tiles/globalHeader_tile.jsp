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
	
	    <div id="user">Welcome, <a href="account.html"><authz:authentication operation="username"/></a> | <a
		   href="logout.jsp">Logout</a></div>
	
	    <div id="mystudies"><label for="studynav">My Studies:</label> <select
		   name="studynav" id="studynav">
		   <option>VASARI</option>
		   <option>Other studies</option>
	    </select>
	    </div>

    </div>

</div>

<!--/PO-Curate Header-->
