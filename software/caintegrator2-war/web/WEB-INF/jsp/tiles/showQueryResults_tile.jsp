<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Search Results-->

    
    <div id="searchresults" class="box2" style="display:none;">                                             
        
        <h2>Search Results for: <s:property value="queryResult.getQuery().getName()"/></h2>

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
        
                 <s:property value="queryResult.getQuery().getDescription()"/><br>  
                 <br>
            <table class="data">
                 
                 <s:if test='%{getQuery().getResultType().equals("genomic")}'>
                 </s:if>
                 <s:else>
                <tr>
                    <s:if test="queryResult.hasSubjects" >
                        <th>Subject Identifier</th>
                    </s:if>
                    <s:if test="queryResult.hasImageSeries" >
                        <th>Image Series Identifier</th>
                    </s:if>
                    <s:iterator value="queryResult.headers">
                        <th><s:property /></th>
                    </s:iterator>
                </tr>
                
                <s:iterator value="queryResult.rows" status="status" >
                    <s:if test="#status.odd == true">
                      <tr class="odd">
                    </s:if>
                    <s:else>
                      <tr class="even">
                    </s:else>
                        <s:if test="queryResult.hasSubjects" >
                            <td><s:property value="subjectAssignment.identifier" /></td>
                        </s:if>
                        <s:if test="queryResult.hasImageSeries" >
                            <td>
                                <s:property value="imageSeries.identifier" />
                                <a href='<s:property value="nciaLink" />' target="_">View in NCIA</a>
                            </td>
                        </s:if>
                        <s:iterator value="values">
                            <td><s:property /></td>
                        </s:iterator>
                </s:iterator>
                </s:else>
            </table>
    
           <!--Buttons-->
        <s:url id="testUrlId" namespace="" action="manageQuery.executeQuery">
        </s:url>

        <div class="actionsrow">
            <del class="btnwrapper">
                <ul class="btnrow">
                    <li><s:a href="#" cssClass="btn" href="%{testUrlId}" onclick="document.addCriterionRowForm.submit();"><span class="btn_img"><span class="search">Run Search</span></span></s:a></li>
                </ul>   
            </del>
        </div>
        
        <!--/Buttons-->
    
        <!--Paging-->

        <div class="pagecontrol">
        
            <p class="small">

                Displaying 1-<s:property value="queryResult.numberOfRows"/> of <s:property value="queryResult.numberOfRows"/> Total.
            </p>
            
            <div class="paging">

                Page 1 


                &nbsp;&nbsp;
                &lt; Back <span class="bar">|</span> 
                Next &gt;
            </div>
        
        </div>
        
        <!--/Paging-->
        </s:form>        
                                                                                                            
    </div>

    
<!--/Search Results-->


<div class="clear"><br />
</div>
