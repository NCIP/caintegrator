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
        
                 query name: <s:property value="queryResult.getQuery().getName()"/><br>
                 query description: <s:property value="queryResult.getQuery().getDescription()"/><br>  
                 <br>
            <table class="data">
                 
                <tr>
                    <th></th>
                    <th></th>
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
                        <td><s:property value="#status.count" /></td>       
                        <td><s:property value="subjectAssignment.identifier" /></td>
                        <s:iterator value="values">
                            <td><s:property /></td>
                        </s:iterator>
                </s:iterator>
            </table>
        </s:form>        
    
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
