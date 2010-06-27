<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    <script language="javascript">
        // This function is called at body onload because IE 7 puts the footer in the middle of the page
        // sporadically, and this toggles it to go to the proper position.
        function initializeJsp() {
            var tbody = document.getElementById('survivalTypeDiv');
            tbody.style.display = "none";
            tbody.style.display = "";
        }
    </script>
    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>    
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('survival_values_help')" class="help">
   </a>
    </div>

    <!--/Page Help-->         
    
    <h1>Edit Survival Values</h1>
    <p>Select an existing definition and click <strong>Edit</strong> or click <strong>New</strong>.</p>

    <div class="form_wrapper_outer">

    <table class="form_wrapper_table">

            <tr>
                <td colspan="2" style="padding: 5px;">
    
                <s:actionerror/>
            
                <s:form name="survivalDefinitionForm" theme="simple">
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="actionType" />
                    
            	    <s:select name="survivalDefinitionFormValues.survivalValueDefinitionId" 
            	              list="survivalValueDefinitions" 
            	              listValue="value.name"
                              label="Definitions"
            	              size="5"/>
            	    <br> <br>
            	    <s:url id="newSurvivalValueDefinition" action="newSurvivalValueDefinition">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    </s:url>
            	    <s:a href="%{newSurvivalValueDefinition}" cssClass="btn" cssStyle="margin:0 5px;"><span class="btn_img"><span class="add">New</span></span></s:a>
            	    <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.survivalDefinitionForm.action = 'deleteSurvivalValueDefinition.action';document.survivalDefinitionForm.actionType.value = 'modify';document.survivalDefinitionForm.submit();"><span class="btn_img"><span class="cancel">Remove</span></span></s:a>
            	    <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.survivalDefinitionForm.action = 'editSurvivalValueDefinition.action';document.survivalDefinitionForm.actionType.value = 'modify';document.survivalDefinitionForm.submit();"><span class="btn_img"><span class="edit">Edit</span></span></s:a>
            	    <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.survivalDefinitionForm.action = 'cancelSurvivalValueDefinition.action';document.survivalDefinitionForm.submit();"><span class="btn_img"><span class="cancel">Cancel</span></span></s:a>
                </s:form>
                <s:if test="%{survivalValueDefinition.id != null || newDefinition}">
                <br><br>
                <s:if test="%{!newDefinition}">
                <h1>Survival Value Definition Properties for '<s:property value="survivalValueDefinition.name" />'</h1>
                </s:if>
                <s:else>
                <h1>Properties for New Survival Value Definition</h1>
                </s:else>
                <s:form name="editSurvivalDefinitionForm" theme="css_xhtml">
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="survivalValueDefinition.id" />
                    <br />
		            <s:div cssStyle="padding: 1em 0 0 0;" id="survivalTypeDiv">
		                <s:div cssClass="wwlbl"><label class="label">Survival Definition Type: </label></s:div>
		                <s:div>
		                    <s:radio theme="css_xhtml" name="survivalDefinitionFormValues.survivalValueType" list="#{'By Date':'By Date'}" onclick="document.getElementById('lengthOfTimeInputParams').style.display = 'none'; document.getElementById('dateInputParams').style.display = 'block';" />
		                    <s:radio theme="css_xhtml" name="survivalDefinitionFormValues.survivalValueType" list="#{'By Length of time in study':'By Length of time in study'}" onclick="document.getElementById('dateInputParams').style.display = 'none'; document.getElementById('lengthOfTimeInputParams').style.display = 'block';" />
		                </s:div>
		            </s:div>
		            <br />
                    <s:textfield label="Name" name="survivalDefinitionFormValues.survivalValueDefinitionName" required="true"/>
                    <s:select name="survivalDefinitionFormValues.survivalLengthUnits"
                                  list="@gov.nih.nci.caintegrator2.domain.annotation.SurvivalLengthUnitsEnum@getDisplayableValues()" 
                                  label="Survival Length Units" required="true"/>
                                  
                    <s:div id="dateInputParams" cssStyle="%{dateInputCssStyle}">
	                    <s:select name="survivalDefinitionFormValues.survivalStartDateId" 
	                              list="dateAnnotationDefinitions" 
	                              listValue="value.displayName" label = "Survival Start Date" required="true"/>
	                              
	                    <s:select name="survivalDefinitionFormValues.survivalDeathDateId" 
	                              list="dateAnnotationDefinitions" 
	                              listValue="value.displayName" label = "Death Date" required="true"/>
	                              
	                    <s:select name="survivalDefinitionFormValues.lastFollowupDateId" 
	                              list="dateAnnotationDefinitions" 
	                              listValue="value.displayName" label = "Last Followup Date" required="true"/>
                    </s:div>
                    <s:div id="lengthOfTimeInputParams" cssStyle="%{lengthOfTimeInputCssStyle}">
                        <s:select name="survivalDefinitionFormValues.survivalLengthId"
                                  list="numericAnnotationDefinitions"
                                  listValue="value.displayName" label = "Survival Length" 
                                  headerKey="" headerValue="" required="true"/>
                        <s:select name="survivalDefinitionFormValues.survivalStatusId" 
                                  list="survivalStatusAnnotationDefinitions"
                                  listValue="value.displayName" label="Censored Status" onchange="document.editSurvivalDefinitionForm.action='updateSurvivalStatusValues.action'; document.editSurvivalDefinitionForm.submit();"
                                  headerKey="" headerValue=""/>
                          <s:select name="survivalDefinitionFormValues.valueForCensored"
                                  list="survivalStatusValues" label="Value for Censored"
                                  headerKey="" headerValue=""/>
                    </s:div>
                    
                    <div style="position: relative; white-space: nowrap;">
					<div class="wwlbl" id="wwlbl_webServiceUrl"><label class="label" for="editSurvivalValueDefinition_survivalDefinitionFormValues_lastFollowupDateId"></label>
					</div>
					<div class="wwctrl" id="wwlbl_webServiceUrl">
					   <s:submit value="Save" action="saveSurvivalValueDefinition" />
					</div>
					</div>
                </s:form>
                </s:if>
    
                </td>
            </tr>
        </table>
    </div>    
    
</div>

<div class="clear"><br /></div>
