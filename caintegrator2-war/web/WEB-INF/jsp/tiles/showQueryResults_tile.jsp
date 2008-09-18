<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Search Results-->

    
    <div id="searchresults" class="box2" style="display:none;">                                             
        
        <h2>Search Results for: "<s:property value="queryResult.getQuery().getName()"/>"</h2>

        <div class="tableheader">
            <label for="numres">Results per Page:</label>
            <select name="numres" id="numres">
                <option>10</option>
                <option selected="selected">20</option>

                <option>50</option>
                <option>100</option>
            </select>
            <input type="button" id="resultsnum" value="Apply" />
        </div>

        <s:form name="resultsForm">
        
                 This query result id is: <s:property value="queryResult.getId()"/><br>
                 query name: <s:property value="queryResult.getQuery().getName()"/><br>
                 query description: <s:property value="queryResult.getQuery().getDescription()"/><br>  
                 <br>
            <table class="data">
<!--           
                <tr>
                    <th>struts iterator</th>
                    <th>Row Index</th>
                    <th>Row Id</th>
                    <th>Subject Identifier</th>
                    <th>Sample Name</th>
                </tr>
 --> 
                 
                 <s:iterator value="queryResult.getRowCollection()" status="status">
                    <s:if test="#status.count == 1">
                        <tr>
                            <th></th>
                            <th></th>
                            <s:if test="getValueCollection() != null && !getValueCollection().isEmpty()">
                                <s:iterator value="getValueCollection()" status="valueStatus">
                                   <th>column.annotationDefinition.displayName : <s:property value="column.annotationDefinition.displayName" /></th>                
                                </s:iterator>
                            </s:if>
                            <s:else>
                              <th>No Result Values</td>
                            </s:else> 
                        </tr>
                    </s:if>
                    <s:if test="#status.odd == true">
                      <tr class="odd">
                    </s:if>
                    <s:else>
                      <tr class="even">
                    </s:else>
                        <td><s:property value="#status.count" /></td>       
                        <td><s:property value="subjectAssignment.identifier" /></td>
                        
                        <s:if test="getValueCollection() != null && !getValueCollection().isEmpty()">
                            <s:iterator value="getValueCollection()" status="valueStatus">
                                        <td>column.annotationDefinition.displayName : <s:property value="column.annotationDefinition.displayName" /></td>                
                            </s:iterator>
                        </s:if>
                        <s:else>
                          <td>No Result Values</td>
                        </s:else>                
                        
                      </tr>
                </s:iterator>       
            </table>
        </s:form>        
    
        <!--Data List-->

        <table class="data">
            <tr>

                <th style="white-space:nowrap;"><input type="checkbox" name="all" id="all5" /></th>
                <th class="order1"><a href="#">Subject ID</a></th>
                <th><a href="#">Age</a></th>
                <th><a href="#">Sex</a></th>
                <th><a href="#">Weight</a></th>
                <th><a href="#">Height</a></th>

                <th><a href="#">Smoker</a></th>
                <th style="white-space:nowrap;">Image Series</th>
            </tr>
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123458</td>
                <td>33</td>

                <td>F</td>
                <td>163 lbs</td>
                <td>5'6"</td>
                <td>Y</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123457</td>
                <td>45</td>
                <td>M</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123456</td>
                <td>29</td>

                <td>F</td>
                <td>185 lbs</td>
                <td>5'11"</td>
                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                       
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123455</td>
                <td>45</td>
                <td>M</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123454</td>
                <td>29</td>

                <td>M</td>
                <td>185 lbs</td>
                <td>5'11"</td>
                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                       
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123453</td>
                <td>45</td>
                <td>F</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123452</td>
                <td>29</td>

                <td>F</td>
                <td>185 lbs</td>
                <td>5'11"</td>
                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                       
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123451</td>
                <td>45</td>
                <td>F</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123450</td>
                <td>29</td>

                <td>M</td>
                <td>185 lbs</td>
                <td>5'11"</td>
                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                       
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123449</td>
                <td>45</td>
                <td>F</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123458</td>
                <td>33</td>

                <td>M</td>
                <td>163 lbs</td>
                <td>5'6"</td>
                <td>Y</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123457</td>
                <td>45</td>
                <td>M</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123456</td>
                <td>29</td>

                <td>M</td>
                <td>185 lbs</td>
                <td>5'11"</td>
                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                       
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123455</td>
                <td>45</td>
                <td>M</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123454</td>
                <td>29</td>

                <td>F</td>
                <td>185 lbs</td>
                <td>5'11"</td>
                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                       
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123453</td>
                <td>45</td>
                <td>F</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123452</td>
                <td>29</td>

                <td>M</td>
                <td>185 lbs</td>
                <td>5'11"</td>
                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                       
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123451</td>
                <td>45</td>
                <td>M</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                           
            <tr class="odd">
                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123450</td>
                <td>29</td>

                <td>M</td>
                <td>185 lbs</td>
                <td>5'11"</td>
                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>                                       
            <tr>

                <td class="actionthin"><input type="checkbox" name="cb" /></td>
                <td>13614193285123449</td>
                <td>45</td>
                <td>M</td>
                <td>203 lbs</td>
                <td>6'1"</td>

                <td>N</td>
                <td class="action"><a href="../study_elements/image_details.html">View</a></td>
            </tr>
        </table>
        
        <!--/Data List-->
        
        <!--Paging-->

        <div class="pagecontrol">
        
            <p class="small">

                Displaying 1-20 of 240 Total.
            </p>
            
            <div class="paging">

                Page 1 <span class="bar">|</span> <a href="#">2</a> <span class="bar">|</span> <a href="#">3</a> <span class="bar">|</span> <a href="#">4</a> <span class="bar">|</span> <a href="#">5</a> <span class="bar">|</span> <a href="#">6</a> <span class="bar">|</span> <a href="#">7</a> <span class="bar">|</span> <a href="#">8</a> <span class="bar">|</span> <a href="#">9</a> <span class="bar">|</span> <a href="#">10</a> <span class="bar">|</span> <a href="#">11</a> <span class="bar">|</span> <a href="#">12</a>


                &nbsp;&nbsp;
                &lt; Back <span class="bar">|</span> 
                <a href="#">Next &gt;</a>
            </div>
        
        </div>
        
        <!--/Paging-->
                                                                                                            
    </div>

    
<!--/Search Results-->


<div class="clear"><br />
</div>
