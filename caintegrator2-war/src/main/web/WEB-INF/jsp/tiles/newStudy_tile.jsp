<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <h1>Create a New Study</h1>
    
    <s:form action="saveStudy">
        <s:textfield label="Study Name" name="study.shortTitleText" />
        <s:textarea label="Study Description" name="study.longTitleText" />
        <s:submit action="saveStudy" value="Save" />
        <s:submit action="deployStudy" value="Deploy" />
    </s:form>
            
</div>

<div class="clear"><br /></div>
