<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('welcome_help')" class="help">
    (draft)</a>
    </div>
    
    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <p> The Study you have selected is <s:property value="study.shortTitleText" />
            
</div>

<div class="clear"><br /></div>