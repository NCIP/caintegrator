<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<script type='text/javascript' src='dwr/interface/DataElementSearchAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      

    <script type="text/javascript">
    initializeJsp();
    
    function runSearch(studyConfigurationId, fileColumnId, keywords) {
        var searchResultJsp = "";
        if (document.getElementById("annotationDefinitionTable") == null) {
            searchResultJsp = "/WEB-INF/jsp/tiles/editFileColumn_searchResult.jsp";
        }
        
        dwr.engine.setActiveReverseAjax(true);
        DataElementSearchAjaxUpdater.runSearch("subject", studyConfigurationId, fileColumnId,
            keywords, searchResultJsp);
    }
  
    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        DataElementSearchAjaxUpdater.initializeJsp("/WEB-INF/jsp/tiles/editFileColumn_searchResult.jsp");
    }
    </script>
    
    <!--Page Help-->
    
    <div class="pagehelp"><span class="help"><a href="#">&nbsp;</a></span></div>
    
    <!--/Page Help-->           
    <s:actionerror />
    <h1>Assign Annotation Definition for Imaging Column: <s:property value="fileColumn.name" /></h1>

    <s:form action="saveImagingColumnType">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imageAnnotationConfiguration.id" />
        <s:hidden name="fileColumn.id" />
    
        <s:select label="Column Type:" name="columnType" onchange="this.form.submit();" list="columnTypes" required="true" />
    </s:form>
        
    <s:form id="updateDefinition" cssClass="currentAnnotationDefinition">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imageAnnotationConfiguration.id" />
        <s:hidden name="fileColumn.id" />
        <s:if test="%{columnTypeAnnotation}">
            <s:if test="%{fileColumn.fieldDescriptor.definition != null}">
               <hr>
               <h1>Current Annotation Definition: </h1>
                <s:textfield label="Name" name="fileColumn.fieldDescriptor.definition.displayName" readonly="%{readOnly}" />
                <s:textarea label="Definition" name="fileColumn.fieldDescriptor.definition.preferredDefinition" cols="40" rows="4" readonly="%{readOnly}"/>
                <s:textfield label="Keywords" name="fileColumn.fieldDescriptor.definition.keywords"  />
                <s:select label="Data Type" name="fileColumn.fieldDescriptor.definition.type" list="annotationDataTypes" disabled="%{readOnly}" />
            </s:if>
            <s:if test="%{fileColumn.fieldDescriptor.definition.cde != null}">
                <s:textfield label="CDE Public ID" value="%{fileColumn.fieldDescriptor.definition.cde.publicID}" 
                readonly="%{readOnly}"/> 
            </s:if>
            <s:if test="%{permissibleOn}">
                <tr><td><br/></td></tr>

                <s:optiontransferselect
                    name="permissibleUpdateList"
                    list="permissibleValues"
                    doubleName="availableUpdateList"
                    doubleList="availableValues"
                    allowAddAllToLeft="false"
                    allowAddAllToRight="false"
                    allowUpDownOnLeft="false"
                    allowUpDownOnRight="false"
                    leftTitle="Permissible"
                    rightTitle="Non-Permissible"
                    addToLeftLabel=" < Add"
                    addToRightLabel=" Remove >"
                    allowSelectAll="false"
                    size="8"
                    doubleSize="8"
                    multiple="true"
                    doubleMultiple="true"
                    cssStyle="min-width:100px; vertical-align=middle;"
                    buttonCssStyle="min-width:100px;"
                    label="Permissible Values"
                    disabled="%{fromCadsr}" 
                    doubleDisabled="%{fromCadsr}"
                    />
            </s:if>
            <s:submit value="New" action="createNewImagingDefinition" />
        </s:if>
        <s:submit value="Save" action="updateImagingFileColumn" />
    </s:form>
    
    <s:if test="%{columnTypeAnnotation}">
        <hr>
        <h1>Search For an Imaging Annotation Definition: </h1>
        <s:form theme="simple">
            <s:hidden id="searchFormStudyConfigurationId" name="studyConfiguration.id" />
            <s:hidden name="imageAnnotationConfiguration.id" />
            <s:hidden id="searchFormFileColumnId" name="fileColumn.id" />
            <tr>
                <td>
                    <s:textfield label="Keywords" name="keywordsForSearch" id="keywordsForSearch"  />
                </td>
                <td> 
                    <button type="button" onclick="runSearch(document.getElementById('searchFormStudyConfigurationId').value, 
                                      document.getElementById('searchFormFileColumnId').value,
                                      document.getElementById('keywordsForSearch').value)"> Search </button>
                </td>
                <td>
                    <em>Search existing studies and caDSR for definitions.</em>
                </td>
            </tr>
            <div id="errorMessages" style="color: red;"> </div>
        </s:form>
        
        <s:set name="selectDefinitionAction" value="selectImagingDefinition" />
        <s:set name="selectDataElementAction" value="selectImagingDataElement" />
        <div id="searchResult"></div>
        
    </s:if>    
</div>

<div class="clear"><br /></div>