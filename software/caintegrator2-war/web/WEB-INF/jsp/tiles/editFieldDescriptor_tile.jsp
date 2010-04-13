<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<script type='text/javascript' src='dwr/interface/DataElementSearchAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      

    <script type="text/javascript">

    function runSearch(entityType) {
        var searchResultJsp = "";
        var studyConfigurationId = document.getElementById('searchFormStudyConfigurationId').value; 
        var fieldDescriptorId = document.getElementById('searchFormFieldDescriptorId').value;
        var keywords = document.getElementById('keywordsForSearch').value;
        if (document.getElementById("annotationDefinitionTable") == null) {
            searchResultJsp = "/WEB-INF/jsp/tiles/editFieldDescriptor_searchResult.jsp";
        }
        
        dwr.engine.setActiveReverseAjax(true);
        DataElementSearchAjaxUpdater.runSearch(entityType, studyConfigurationId, fieldDescriptorId,
            keywords, searchResultJsp);
    }
    
    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        DataElementSearchAjaxUpdater.initializeJsp("/WEB-INF/jsp/tiles/editFieldDescriptor_searchResult.jsp");
    }
    
    function changeFieldDescriptorType() {
        var type = document.getElementById("fieldDescriptorType").value;
        if (document.getElementById("fieldDescriptorType").value == "Identifier") {
            if (confirm("You are about to set this field descriptor to be an Identifier \n" +
                "which will set any other Identifier columns used in the same source to null.")) {
                document.fieldDescriptorTypeForm.submit();
            } else {
                document.getElementById("fieldDescriptorType").value = '<s:property value="fieldDescriptorType"/>';
                return;
            }
        }
        document.fieldDescriptorTypeForm.submit();
    }
    </script>

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('assign_annotations_help')" class="help">
   &nbsp;</a>
    </div>
    
    <!--/Page Help-->
          
    <s:actionerror />
    <h1>Assign Annotation Definition for Field Descriptor: <strong><s:property value="fieldDescriptor.name" /></strong></h1>
    <p>Modify the current annotation definition and click <strong>Save</strong> or search for a new annotation definition and click <strong>Select</strong>.</p>
    <div class="form_wrapper_outer">
    <table class="form_wrapper_table">
            <s:if test="fieldDescriptor.hasValidationErrors">
            <tr>
                <td colspan="2"><font class="formErrorMsg">There was an error with the last file data load: <br>
                <s:property value="fieldDescriptor.validationErrorMessage"/></font></td>
            </tr>
            </s:if>
            <tr>
                <th class="title" style="height: 2.5em;">Current Annotation Definition:</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 0 0 1em 1em;"> 

            	<s:form name="fieldDescriptorTypeForm" action="%{saveFieldDescriptorTypeAction}">
            	    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="sourceId" />
                    <s:hidden name="groupId" />
                    <s:hidden name="fieldDescriptor.id" />
            	
                    <s:select id="fieldDescriptorType" label="Field Descriptor Type:" name="fieldDescriptorType"
                        list="fieldDescriptorTypes" required="true" onchange="changeFieldDescriptorType()" />
                </s:form>
                
                <br>
                
                <s:form id="updateDefinition" cssClass="currentAnnotationDefinition">
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="sourceId" />
                    <s:hidden name="groupId" />
                    <s:hidden name="fieldDescriptor.id" />
                    <s:if test="%{columnTypeAnnotation}">
            	        <s:if test="%{fieldDescriptor.definition != null}">

            	            <s:textfield label="Name" name="fieldDescriptor.definition.commonDataElement.longName" disabled="%{readOnly}" size="38"/>
            	            <s:textarea label="Definition" name="fieldDescriptor.definition.commonDataElement.definition" cols="40" rows="4" disabled="%{readOnly}"/>
            	            <s:textfield label="Keywords" name="fieldDescriptor.definition.keywords" disabled="%{fromCadsr}" size="38" />
            	            <s:select label="Data Type" 
            	            name="fieldDescriptor.definition.commonDataElement.valueDomain.dataTypeString" 
            	            list="annotationDataTypes" 
            	            disabled="%{readOnly}" />

            	            <tr>
	                            <td class="tdLabel" align="right">
	                                <label class="label">Apply Max Number Mask:</label>
	                            </td>
	                            <td>
	                                <s:checkbox name="maskForm.hasMaxNumberMask" theme="simple" disabled="%{numericMaskDisabled}"/>
	                                <s:textfield name="maskForm.maxNumberMask.maxNumber" theme = "simple" disabled="%{numericMaskDisabled}"/>
	                                <label class="label">(Max Number)</label>
	                            </td>
                            </tr>
            	            
            	            <tr>
                                <td class="tdLabel" align="right">
                                    <label class="label">Apply Numeric Range Mask:</label>
                                </td>
                                <td>
                                    <s:checkbox name="maskForm.hasNumericRangeMask" theme="simple" disabled="%{numericMaskDisabled}"/>
                                    <s:textfield name="maskForm.numericRangeMask.numericRange" theme = "simple" disabled="%{numericMaskDisabled}"/>
                                    <label class="label">(Numeric Range)</label>
                                </td>
                            </tr>
            	            
            	        </s:if>
            	        <s:if test="%{fieldDescriptor.definition.commonDataElement.publicID != null}">
            	            <s:textfield label="CDE Public ID" value="%{fieldDescriptor.definition.commonDataElement.publicID}" 
            	            disabled="%{readOnly}"/> 
            	        </s:if>
                        <s:if test="%{permissibleOn}">
                            <tr><td><br/></td></tr>
                            
                            <s:optiontransferselect
                                doubleName="permissibleUpdateList"
                                doubleList="permissibleValues"
                                name="availableUpdateList"
                                list="availableValues"
                                allowAddAllToLeft="false"
                                allowAddAllToRight="false"
                                allowUpDownOnLeft="false"
                                allowUpDownOnRight="false"
                                rightTitle="Permissible"
                                leftTitle="Non-Permissible"
                                addToLeftLabel=" < Remove"
                                addToRightLabel=" Add > "
                                allowSelectAll="false"
                                size="8"
                                doubleSize="8"
                                multiple="true"
                                doubleMultiple="true"
                                cssStyle="%{permissibleCssStyle}"
                                doubleCssStyle="%{permissibleCssStyle}"
                                buttonCssStyle="min-width:100px;" 
                                label="Permissible Values"
                                allowAddToLeft="%{!readOnly}"
                                allowAddToRight="%{!readOnly}"
                                />
                    	</s:if>
                   	</s:if>
                    <tr>
	                    <td> </td>
	                    <td>
	                    <br>
	                    <s:if test="%{columnTypeAnnotation && readOnly}">
                            <s:submit value="New" action="%{newDefinitionAction}" theme="simple"/>
	                    </s:if>
	                    <s:submit value="Save" action="%{saveAnnotationDefinitionAction}" theme="simple"/>
	                    <s:if test="%{cancelEnabled}">
	                        <s:submit value="Cancel" action="%{cancelAction}" theme="simple"/>
	                    </s:if>
                        </td>
                    </tr>
                    
                </s:form>
                </td>
            </tr>
    </table>            
   
    <s:if test="%{columnTypeAnnotation}">
    <s:set name="entityTypeForSearch" value="entityTypeForSearch" />
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Search for an Annotation Definition: </th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">     

                <s:form name="keywordSearchForm" id="keywordSearchForm" 
                        theme="simple" 
                        onsubmit="runSearch('%{entityTypeForSearch}'); return false;">
                    <s:hidden id="searchFormStudyConfigurationId" name="studyConfiguration.id" />
                    <s:hidden name="sourceId" />
                    <s:hidden name="groupId" />
                    <s:hidden id="searchFormFieldDescriptorId" name="fieldDescriptor.id" />
                    <table style="padding: 0 0 5px 5px;">
                    <tr>
                        <td>
                            <s:textfield label="Keywords" name="keywordsForSearch" id="keywordsForSearch"  />
                        </td>
                        <td> 
        	               <button type="button" onclick="runSearch('${entityTypeForSearch}')"> Search </button>
                        </td>
                        <td style="padding: 0 0 0 1em">
                            <em>Search existing studies and caDSR for definitions.</em>
                        </td>
                    </tr>
                    </table>
                    <div id="errorMessages" style="color: red;"> </div>
                </s:form>
                
                <div id="searchResult" style="padding: 1em 0 0 1.5em;"></div>
                
                </td>
            </tr>
    </table>       
    </s:if>
    
    </div> 
</div>

<div class="clear"><br /></div>
