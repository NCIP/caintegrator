<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

    <head>
		<title>caIntegrator2</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="address bar icon" href="favicon.ico" />
		<link rel="icon" href="favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="common/css/caintegrator2.css" />
		<script type="text/javascript" src="common/js/pde.js"></script>
        <script type="text/javascript" src="common/js/prototype.js"></script>
        <script type="text/javascript" src="common/js/scriptaculous.js"></script>
        <script type="text/javascript" src="common/js/checklist.js"></script>
        <script type="text/javascript" src="common/js/caintegrator2.js"></script>
        <script type="text/javascript" src="common/js/onlinehelp.js"></script>
        <script type="text/javascript" src="./struts/optiontransferselect.js"></script> 
        <s:head theme="ajax"/>
	</head>
	<body>
		
		<a href="#content" id="navskip">Skip to Page Content</a>
		
		<div id="wrapper" class="curate">
			
			<!-- Global Header -->	
            
            <tiles:insertAttribute name="globalHeader" />
            
			<!-- /Global Header -->	

			<!--Work Area-->
				
			<div id="main">				
				
				<div id="contentwrapper">
				
				    <!--Content-->
						
	                <tiles:insertAttribute name="content" />
					
					<!--/Content-->
    
                </div>
                
				<!--Left Navigation Column-->
				
				<tiles:insertAttribute name="leftNavMenu" />
								
				<!--/Left Navigation Column-->
				
			</div>			
			
			<!--/Work Area-->		
		
			<!--Footer-->
			
			<tiles:insertAttribute name="globalFooter" />
			
			<!--/Footer-->
		
		</div>
        
			
	</body>
	
</html>
