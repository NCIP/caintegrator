<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Survival Value Definitions for '<s:property value="studyConfiguration.study.shortTitleText" />'</h1>
    
    <s:actionerror/>

    <s:form name="survivalDefinitionForm" theme="simple">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="actionType" />
        
	    <s:select name="survivalDefinitionFormValues.survivalValueDefinitionId" 
	              list="survivalValueDefinitions" 
	              listValue="value.name"
	              size="5"/>
	    <br> <br>
	    <s:url id="newSurvivalValueDefinition" action="newSurvivalValueDefinition">
            <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
            <s:param name="actionType" value="new" />
        </s:url>
	    <s:a href="%{newSurvivalValueDefinition}" cssClass="btn" cssStyle="margin:0 5px;"><span class="btn_img"><span class="add">New</span></span></s:a>
	    <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.survivalDefinitionForm.action = 'deleteSurvivalValueDefinition.action';document.survivalDefinitionForm.actionType.value = 'modify';document.survivalDefinitionForm.submit();"><span class="btn_img"><span class="cancel">Remove</span></span></s:a>
	    <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.survivalDefinitionForm.action = 'editSurvivalValueDefinition.action';document.survivalDefinitionForm.actionType.value = 'modify';document.survivalDefinitionForm.submit();"><span class="btn_img"><span class="edit">Edit</span></span></s:a>
    </s:form>
    <s:if test="%{survivalValueDefinition.id != null}">
    <br><br>
    <h1>Survival Value Definition Properties for '<s:property value="survivalValueDefinition.name" />'</h1>
    
    <s:form>
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="survivalValueDefinition.id" />
        <s:textfield label="Name" name="survivalValueDefinition.name" />
        <s:select name="survivalDefinitionFormValues.survivalStartDateId" 
                  list="dateAnnotationDefinitions" 
                  listValue="value.displayName" label = "Survival Start Date" />
                  
        <s:select name="survivalDefinitionFormValues.survivalDeathDateId" 
                  list="dateAnnotationDefinitions" 
                  listValue="value.displayName" label = "Death Date" />
                  
        <s:select name="survivalDefinitionFormValues.lastFollowupDateId" 
                  list="dateAnnotationDefinitions" 
                  listValue="value.displayName" label = "Last Followup Date" />
                  
        <s:submit value="Save" action="saveSurvivalValueDefinition" />
    </s:form>
    </s:if>
</div>

<div class="clear"><br /></div>
