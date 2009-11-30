<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<script type='text/javascript' src='dwr/interface/DataElementSearchAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      

    <script type="text/javascript">
    
    initializeJsp();
    
    function runSearch(entityType) {
        var searchResultJsp = "";
        var studyConfigurationId = document.getElementById('searchFormStudyConfigurationId').value; 
        var fileColumnId = document.getElementById('searchFormFileColumnId').value;
        var keywords = document.getElementById('keywordsForSearch').value;
        if (document.getElementById("annotationDefinitionTable") == null) {
            searchResultJsp = "/WEB-INF/jsp/tiles/editFileColumn_searchResult.jsp";
        }
        
        dwr.engine.setActiveReverseAjax(true);
        DataElementSearchAjaxUpdater.runSearch(entityType, studyConfigurationId, fileColumnId,
            keywords, searchResultJsp);
    }
    
    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        DataElementSearchAjaxUpdater.initializeJsp("/WEB-INF/jsp/tiles/editFileColumn_searchResult.jsp");
    }
    
    function changeColumnType() {
        var type = document.getElementById("columnType").value;
        if (document.getElementById("columnType").value == "Identifier") {
            var identifier = '<s:property value="identifier"/>';
            if (identifier != "") {
                if (confirm("You are about to set this column to be an Identifier \n" +
                    "and set the '" + identifier + "' column to null.")) {
                    document.columnTypeForm.submit();
                } else {
                    document.getElementById("columnType").value = '<s:property value="columnType"/>';
                    return;
                }
            }
        }
        document.columnTypeForm.submit();
    }
    </script>

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('assign_annotations_help')" class="help">
   &nbsp;</a>
    </div>
    
    <!--/Page Help-->
          
    <s:actionerror />
    <h1>Assign Annotation Definition for Column: <strong><s:property value="fileColumn.name" /></strong></h1>
    <p>Modify the current annotation definition and click <strong>Save</strong> or search for a new annotation definition and click <strong>Select</strong>.</p>
    <div class="form_wrapper_outer">
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Current Annotation Definition:</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 0 0 1em 1em;"> 

            	<s:form name="columnTypeForm" action="%{saveColumnTypeAction}">
            	    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="sourceId" />
                    <s:hidden name="fileColumn.id" />
            	
                    <s:select id="columnType" label="Column Type:" name="columnType"
                        list="columnTypes" required="true" onchange="changeColumnType()" />
                </s:form>
                
                <br>
                
                <s:form id="updateDefinition" cssClass="currentAnnotationDefinition">
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="sourceId" />
                    <s:hidden name="fileColumn.id" />
            	    <s:if test="%{columnTypeAnnotation}">
            	        <s:if test="%{fileColumn.fieldDescriptor.definition != null}">

            	            <s:textfield label="Name" name="fileColumn.fieldDescriptor.definition.commonDataElement.longName" disabled="%{readOnly}" size="38"/>
            	            <s:textarea label="Definition" name="fileColumn.fieldDescriptor.definition.commonDataElement.definition" cols="40" rows="4" disabled="%{readOnly}"/>
            	            <s:textfield label="Keywords" name="fileColumn.fieldDescriptor.definition.keywords" disabled="%{fromCadsr}" size="38" />
            	            <s:select label="Data Type" 
            	            name="fileColumn.fieldDescriptor.definition.commonDataElement.valueDomain.dataTypeString" 
            	            list="annotationDataTypes" 
            	            disabled="%{readOnly}" />
            	        </s:if>
            	        <s:if test="%{fileColumn.fieldDescriptor.definition.commonDataElement.publicID != null}">
            	            <s:textfield label="CDE Public ID" value="%{fileColumn.fieldDescriptor.definition.commonDataElement.publicID}" 
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
                                allowAddToLeft="%{!fromCadsr}"
                                allowAddToRight="%{!fromCadsr}"
                                />
                    	</s:if>
                   	</s:if>
                    <tr>
	                    <td> </td>
	                    <td>
	                    <br>
	                    <s:if test="%{columnTypeAnnotation}">
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
                    <s:hidden id="searchFormFileColumnId" name="fileColumn.id" />
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
