<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!--Search Results-->

<div id="searchresults" class="box2">
<h2>Search Results</h2>

        <s:form>
        
                 This query result id is: <s:property value="queryResult.getId()"/><br>
                 query name: <s:property value="queryResult.getQuery().getName()"/><br>
                 query description: <s:property value="queryResult.getQuery().getDescription()"/><br>  
                 <br>
            <table class="data">
                <tr>
                    <th>struts iterator</th>
                    <th>Row Index</th>
                    <th>Row Id</th>
                    <th>Subject Identifier</th>
                    <th>Sample Name</th>
                </tr>
                
                 <s:iterator value="queryResult.getRowCollection()" status="status">
                    <s:if test="#status.odd == true">
                      <tr class="odd">
                    </s:if>
                    <s:else>
                      <tr class="even">
                    </s:else>
                        <td><s:property value="#status.count" /></td>       
                        <td><s:property value="rowIndex" /></td>
                        <td><s:property value="id" /></td>
                        <td><s:property value="subjectAssignment.identifier" /></td>
                        <td><s:property value="sampleAcquisition.sample.name" /></td>
                        
                        <s:if test="getValueCollection() != null && !getValueCollection().isEmpty()">
                            <s:iterator value="getValueCollection()" status="valueStatus">
                                        <td><s:property value="#valueStatus.getCount" /><s:property value="value" /></td>                
                            </s:iterator>
                        </s:if>
                        <s:else>
                          <td>No Result Values</td>
                        </s:else>                
                        
                    </tr>
                </s:iterator>       
            </table>
        </s:form>
                                                         
</div>

<!--/Search Results-->

<div class="clear"><br />
</div>
