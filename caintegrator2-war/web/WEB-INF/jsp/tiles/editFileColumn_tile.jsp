<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Assign Annotation Definition for Column: <s:property value="fileColumn.name" /></h1>

	<s:form action="saveColumnType">
	    <s:hidden name="studyConfiguration.id" />
        <s:hidden name="clinicalSource.id" />
        <s:hidden name="fileColumn.id" />
	
        <s:select label="Column Type:" name="columnType" onchange="this.form.submit();" list="columnTypes" required="true" />
    </s:form>
        
    <s:form>
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="clinicalSource.id" />
        <s:hidden name="fileColumn.id" />
	    <s:if test="%{columnTypeAnnotation}">
	        <s:if test="%{fileColumn.fieldDescriptor.definition != null}">
	           <hr>
	           <h1>Current Annotation Definition: </h1>
	            <s:textfield label="Name" name="fileColumn.fieldDescriptor.definition.displayName" readonly="%{readOnly}" />
	            <s:textarea label="Definition" name="fileColumn.fieldDescriptor.definition.preferredDefinition" cols="40" rows="4" readonly="%{readOnly}"/>
	            <s:textfield label="Keywords" name="fileColumn.fieldDescriptor.definition.keywords"  />
	            <s:select label="Data Type" name="annotationDataType" list="annotationDataTypes" required="true" />
	        </s:if>
	        <s:if test="%{fileColumn.fieldDescriptor.definition.cde != null}">
	            <s:textfield label="CDE Public ID" value="%{fileColumn.fieldDescriptor.definition.cde.publicID}" 
	            readonly="%{readOnly}"/> 
	        </s:if>
	        <br>
	        <s:submit value="New" action="createNewDefinition" />
        </s:if>
        <s:submit value="Save" action="updateFileColumn" />
    </s:form>
    
    
    <s:if test="%{columnTypeAnnotation}">
    <hr>
    <h1>Search For an Annotation Definition: </h1>
    <s:form theme="simple" action="searchDefinitions">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="clinicalSource.id" />
        <s:hidden name="fileColumn.id" />
        <tr>
            <td>
                <s:textfield label="Keywords" name="keywordsForSearch"  />
            </td>
           <td> 
               <s:submit value="Search" action="searchDefinitions" />
           </td>
           <td>
               <em>Search existing studies and caDSR for definitions.</em>
           </td>
    </s:form>
        <br> <br>
    </s:if>
    <s:if test="%{!definitions.isEmpty}">
    <hr>
	    <table class="data">
	        <tr>
	            <th colspan="2">Matching Annotation Definitions</th>
	        </tr>
	        <tr>
	            <th>Name</th>
	            <th>CDE Public ID</th>
	            <th>Data Type</th>
	            <th>Definition</th>
	        </tr>
	        <s:iterator value="definitions" status="status">
	            <s:if test="#status.odd == true">
	              <tr class="odd">
	            </s:if>
	            <s:else>
	              <tr class="even">
	            </s:else>            
	            <td>
	                <s:url id="selectDefinition" action="selectDefinition">
	                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
	                    <s:param name="fileColumn.id" value="fileColumn.id" />
	                    <s:param name="definitionIndex" value="#status.index" />
	                </s:url> 
	                <s:a href="%{selectDefinition}"><s:property value="displayName" /></s:a>
	            </td>
	            <td><s:property value="cde.publicID" /></td>
	            <td><s:property value="type" /></td>
	            <td><s:property value="preferredDefinition" /></td>
	        </tr>
	        </s:iterator>
	    </table>
    </s:if>
    
    <s:if test="%{!dataElements.isEmpty}">
	    <table class="data">
	        <tr>
	            <th colspan="3">Matches from caDSR</th>
	        </tr>
	        <tr>
	            <th>Name</th>
	            <th>Actions</th>
	            <th>Public ID</th>
	            <th>Definition</th>
	        </tr>
	        <s:iterator value="dataElements" status="status">
	            <s:if test="#status.odd == true">
	              <tr class="odd">
	            </s:if>
	            <s:else>
	              <tr class="even">
	            </s:else>            
	            <td>
                    <s:property value="longName" />
	            </td>
	            <td>
                    <s:url id="selectDataElement" action="selectDataElement">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="fileColumn.id" value="fileColumn.id" />
                        <s:param name="dataElementIndex" value="#status.index" />
                    </s:url> 
                    <s:url id="viewDataElement" value="http://freestyle-qa.nci.nih.gov/freestyle/do/cdebrowser" escapeAmp="false">
	                    <s:param name="publicId" value="publicId"/>
	                    <s:param name="version" value="1"/>
                    </s:url>
                    <s:a href="%{selectDataElement}">Select</s:a>
                    <a href="<s:property value='%{viewDataElement}'/>" target="_blank">View</a>
                </td>
	            <td><s:property value="publicId" /></td>
	            <td><s:property value="definition" /></td>
	        </tr>
	        </s:iterator>
	    </table>
    </s:if>
</div>

<div class="clear"><br /></div>
