<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    <s:actionerror />
    <h1>Assign Annotation Definition for Imaging Column: <s:property value="fileColumn.name" /></h1>

    <s:form action="saveImagingColumnType">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imagingSource.id" />
        <s:hidden name="fileColumn.id" />
    
        <s:select label="Column Type:" name="columnType" onchange="this.form.submit();" list="columnTypes" required="true" />
    </s:form>
        
    <s:form id="updateDefinition" cssClass="currentAnnotationDefinition">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imagingSource.id" />
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
    <s:form theme="simple" action="searchImagingDefinitions">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imagingSource.id" />
        <s:hidden name="fileColumn.id" />
        <tr>
            <td>
                <s:textfield label="Keywords" name="keywordsForSearch"  />
            </td>
           <td> 
               <s:submit value="Search" action="searchImagingDefinitions" />
           </td>
           <td>
               <em>Search existing studies and caDSR for definitions.</em>
           </td>
    </s:form>
        <br> <br>
    
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
                    <s:url id="selectImagingDefinition" action="selectImagingDefinition">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="fileColumn.id" value="fileColumn.id" />
                        <s:param name="definitionIndex" value="#status.index" />
                    </s:url> 
                    <s:a href="%{selectImagingDefinition}"><s:property value="displayName" /></s:a>
                </td>
                <td><s:property value="cde.publicID" /></td>
                <td><s:property value="type" /></td>
                <td><s:property value="preferredDefinition" /></td>
            </tr>
            </s:iterator>
        </table>
    </s:if>
    <s:else>
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
            <td>
                No matches found for your Search.
            </td>
        </table>    
    </s:else>
    <s:if test="%{!dataElements.isEmpty}">
        <table class="data">
            <tr>
                <th colspan="3">Matches from caDSR</th>
            </tr>
            <tr>
                <th>Name</th>
                <th>Actions</th>
                <th nowrap>CDE Public ID</th>
                <th>Context</th>
                <th>Status</th>
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
                <td nowrap>
                    <s:url id="selectImagingDataElement" action="selectImagingDataElement">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="fileColumn.id" value="fileColumn.id" />
                        <s:param name="dataElementIndex" value="#status.index" />
                    </s:url> 
                    <s:url id="viewDataElement" value="%{@gov.nih.nci.caintegrator2.external.cadsr.CaDSRFacade@CDE_URL}" escapeAmp="false">
                        <s:param name="publicId" value="publicID"/>
                        <s:param name="version" value="version"/>
                    </s:url>
                    <s:a href="%{selectImagingDataElement}">Select</s:a> | 
                    <a href="<s:property value='%{viewDataElement}'/>" target="_blank">View</a>
                </td>
                <td nowrap><s:property value="publicID" /></td>
                <td><s:property value="contextName" /></td>
                <td><s:property value="workflowStatus" /></td>
                <td><s:property value="definition" /></td>
            </tr>
            </s:iterator>
        </table>
    </s:if>
    <s:else>
        <table class="data">
            <tr>
                <th colspan="3">Matches from caDSR</th>
            </tr>
            <tr>
                <th>Name</th>
                <th>Actions</th>
                <th>CDE Public ID</th>
                <th>Context</th>
                <th>Status</th>
                <th>Definition</th>
            </tr>
            <td>
                No matches found in caDSR for your Search.
            </td>
        </table>    
    </s:else>
</s:if>    
</div>

<div class="clear"><br /></div>