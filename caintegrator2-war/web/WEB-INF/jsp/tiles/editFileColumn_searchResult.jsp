<%@ taglib prefix="s" uri="/struts-tags"%>

        <table class="data">
            <tr>
                <th colspan="4">Matching Annotation Definitions from caintegrator2</th>
            </tr>
            <tr>
                <th>Name</th>
                <th>CDE Public ID</th>
                <th>Data Type</th>
                <th>Definition</th>
            </tr>
            <tbody id="annotationDefinitionTable">
            <!-- If this is a return from an error, we need to print the items currently on session -->
            <s:if test="%{!errorMessages.isEmpty}">
                <s:iterator value="definitions" status="status">
                    <s:if test="#status.odd == true">
                      <tr class="odd">
                    </s:if>
                    <s:else>
                      <tr class="even">
                    </s:else>            
                    <td>
                        <s:url id="selectDefinition" action="%{selectDefinitionAction}">
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
            </s:if>
            </tbody>
        </table>    

        <table class="data">
            <tr>
                <th colspan="6">Matching Annotation Definitions from caDSR</th>
            </tr>
            <tr>
                <th>Name</th>
                <th>Actions</th>
                <th nowrap>CDE Public ID</th>
                <th>Context</th>
                <th>Status</th>
                <th>Definition</th>
            </tr>
            <tbody id="cadsrTable">
            <!-- If this is a return from an error, we need to print the items currently on session -->
            <s:if test="%{!errorMessages.isEmpty}">
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
                        <s:url id="selectDataElement" action="%{selectDataElementAction}">
                            <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                            <s:param name="fileColumn.id" value="fileColumn.id" />
                            <s:param name="dataElementIndex" value="#status.index" />
                        </s:url> 
                        <s:url id="viewDataElement" value="%{@gov.nih.nci.caintegrator2.external.cadsr.CaDSRFacade@CDE_URL}" escapeAmp="false">
                            <s:param name="publicId" value="publicID"/>
                            <s:param name="version" value="version"/>
                        </s:url>
                        <s:a href="%{selectDataElement}">Select</s:a> | 
                        <a href="<s:property value='%{viewDataElement}'/>" target="_blank">View</a>
                    </td>
                    <td nowrap><s:property value="publicID" /></td>
                    <td><s:property value="contextName" /></td>
                    <td><s:property value="workflowStatus" /></td>
                    <td><s:property value="definition" /></td>
                    </tr>
                </s:iterator>
            </s:if>
            </tbody>
        </table>    
