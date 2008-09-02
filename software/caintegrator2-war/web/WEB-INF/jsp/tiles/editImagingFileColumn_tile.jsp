<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Assign Annotation Definition for Column: <s:property value="fileColumn.name" /></h1>
    
    <s:form>
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="imagingSource.id" />
        <s:hidden name="fileColumn.id" />
        <table>
            <tr>
                <th>Column Type</th>
                <td>
                    <s:select name="columnType" list="columnTypes" required="true" />
                </td>
            </tr>
            <tr>
                <th>Keywords</th>
                <td>
                    <s:textfield name="fileColumn.fieldDescriptor.keywords"  />
                </td>
            </tr>
            <s:if test="%{fileColumn.fieldDescriptor.definition != null}">
                <tr>
                    <th>Name</th>
                    <td>
                        <s:textfield name="fileColumn.fieldDescriptor.definition.displayName"  />
                    </td>
                </tr>
                <tr>
                    <th>Definition</th>
                    <td>
                        <s:textarea name="fileColumn.fieldDescriptor.definition.preferredDefinition" cols="40" rows="4" />
                    </td>
                </tr>
            </s:if>
            <s:if test="%{fileColumn.fieldDescriptor.definition.cde != null}">
                <tr>
                    <th>CDE Public ID</th>
                    <td>
                        <s:property value="fileColumn.fieldDescriptor.definition.cde.publicID"  />
                    </td>
                </tr>
            </s:if>
            <tr>
                <td colspan="2">
                    <s:submit value="Update" action="updateImagingFileColumn" /><s:submit value="Search" action="searchImagingDefinitions" />
                </td>
            </tr>
        </table>
    </s:form>
    
    <table class="data">
        <tr>
            <th colspan="2">Matching Annotation Definitions</th>
        </tr>
        <tr>
            <th>Name</th>
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
                <s:a href="%{selectDefinition}"><s:property value="displayName" /></s:a>
            </td>
            <td><s:property value="preferredDefinition" /></td>
        </tr>
        </s:iterator>
    </table>
    
    <table class="data">
        <tr>
            <th colspan="3">Matches from caDSR</th>
        </tr>
        <tr>
            <th>Name</th>
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
                <s:url id="selectImagingDataElement" action="selectImagingDataElement">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="fileColumn.id" value="fileColumn.id" />
                    <s:param name="dataElementIndex" value="#status.index" />
                </s:url> 
                <s:a href="%{selectDataElement}"><s:property value="longName" /></s:a>
            </td>
            <td><s:property value="publicId" /></td>
            <td><s:property value="definition" /></td>
        </tr>
        </s:iterator>
    </table>
            
</div>

<div class="clear"><br /></div>
