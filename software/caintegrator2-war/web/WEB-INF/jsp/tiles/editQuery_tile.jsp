<%@ page language="java" 
import="java.util.*, 
    com.opensymphony.xwork2.ActionContext,
    gov.nih.nci.caintegrator2.web.action.query.QueryAnnotationCriteria,
    gov.nih.nci.caintegrator2.web.action.query.AnnotationSelection,
    gov.nih.nci.caintegrator2.web.action.query.QueryAnnotationCriteriaImpl" 
pageEncoding="ISO-8859-1" %>

<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Search Criteria-->
    
<div id="criteria" class="box2">
    
    <h2>Define Search Criteria</h2>                                                                                                             
    
    <ul id="sectabs" class="tabs">
        <li><a href="#basic" class="active">Basic Criteria Definition</a></li>
        <li><a href="#advanced">Advanced Criteria Definition</a></li>
    </ul>

    
    <div class="clear"></div>
    <div class="whitebg">
    

    

                     query name: <s:property value="queryResult.getQuery().getName()"/><br>
                     query description: <s:property value="queryResult.getQuery().getDescription()"/><br>  
                     <br>

            
            <!--Basic Criteria Definition-->
            
            <div id="basic">
                
                <s:form action="manageQuery.addCriterionRow" name="addCriterionRowForm">
<!--          
                <s:hidden name="doMethod" value="addRow" />
-->
                <s:hidden name="manageQueryHelper.sAdvancedView" value="true" />
                
                <!-- Add query criterion row selection -->
                <table class="data">
                    <tr>
                        <td colspan="2" class="tableheader">
                            <select name="selectedRowCriterion" id="searchcriteriaadd1" style="margin-left:5px; width:200px">
                                <option>&ndash; Select Criteria Type &ndash;</option>

                                <option value="clinical">Clinical</option>
                                <option value="sample">Gene Expression</option>
                                <option value="image">Image</option>
                            </select>
                            
                             <ul class="btnrow" style="margin:-22px 0 0 200px; height:32px">         
                                <li><s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.addCriterionRowForm.submit();"><span class="btn_img"><span class="add">Add</span></span></s:a></li>
                            </ul>
                        </td>
                    </tr>
                    
                    <!-- Reproduce existing query criteria rows if any -->
<!-- 
                        <s:select name="selectionistranmittedonthisproperty" list="list-being-iterated-over-to-display" listValue="thisisthedisplayedstring" listKey="this-is-the-value-that-is-submitted" />
 -->    
                    <s:iterator value="manageQueryHelper.queryCriteriaRowList" status="itStatus" id="userobj">
                        <tr>
                        <td> rowLabel:<s:property value="#userobj.rowLabel"/> annotationSelection:<s:property value="#userobj.annotationSelection"/></td>
                        <td><s:select name="selectedAnnotations" list="manageQueryHelper.clinicalAnnotationDefinitions" listValue="displayName" listKey="displayName" value="#userobj.annotationSelection" label="#userobj.rowLabel"/></td>

                        </tr>
                    </s:iterator>                    
                    <tr class="odd">
                        <td colspan="2" class="value_inline">
                            <p style="margin:0; padding:2px 0 3px 0; text-align:center;"><strong>No criteria added</strong>. Please select criteria from the pulldown box.</p>
                        </td>
                    </tr>
                </table>
                
                </s:form>

                                                                        
                <div class="tablefooter">
                    <input type="radio" name="basictype1" id="basicor1" checked="checked" /><label for="basicor1" style="font-weight:normal"><strong>OR</strong> Search (match any)</label>
                    &nbsp;
                    <input type="radio" name="basictype1" id="basicand1" /><label for="basicand1" style="font-weight:normal"><strong>AND</strong> Search (match all)</label>
                </div>
                
            </div>

            
            <!--/Basic Criteria Definition-->

            
            <!--Advanced Criteria Definition-->
            
            <div id="advanced" style="display:none">
                                                                        
                <table class="data">
                    <tr>
                        <td colspan="3" class="tableheader">
                            <select name="selectcriteria_adv" id="searchcriteria_adv" style="margin-left:5px; width:200px">
                                <option>&ndash; Select Criteria Type &ndash;</option>

                                <option>Clinical</option>
                                <option>Gene Expression</option>
                                <option>Image</option>
                            </select>
                            <ul class="btnrow" style="margin:-22px 0 0 200px; height:32px">         
                                <li><a href="default2.html#advanced" style="margin:0 5px;" class="btn" onclick="javascript://"><span class="btn_img"><span class="add">Add</span></span></a></li>
                            </ul>

                        </td>
                    </tr>
                    <tr class="odd">
                        <td colspan="3" class="value_inline">
                            <p ><strong>No criteria added</strong>. Please select criteria from the pulldown box.</p>
                        </td>
                    </tr>
                </table>

            </div>
            
            <!--/Advanced Criteria Definition-->
 
            
    </div>
    
</div>

<!--/Search Criteria-->

<!--Columns-->
    
<div id="columns" class="box2" style="display:none;">
    
    <h2>Select Columns for Results</h2>
    
    <div class="checklistwrapper">

        <h3>Subject Annotations</h3>
        <ul class="checklist">
            <li><label for="subj_1"><input id="subj_1" name="subj_1" type="checkbox" /> Patient Age</label></li>
            <li><label for="subj_2"><input id="subj_2" name="subj_2" type="checkbox" /> Patient Height</label></li>
            <li><label for="subj_3"><input id="subj_3" name="subj_2" type="checkbox" /> Patient ID</label></li>

            <li><label for="subj_4"><input id="subj_4" name="subj_4" type="checkbox" /> Patient Weight</label></li>
            <li><label for="subj_5"><input id="subj_5" name="subj_5" type="checkbox" /> Pulse Rate</label></li>
            <li><label for="subj_6"><input id="subj_6" name="subj_6" type="checkbox" /> Karnofsky Score</label></li>
        </ul>
        <div class="checklistfooter">
            <input type="checkbox" name="all" id="all1" /><label for="all1">All</label>

        </div>
    </div>
    
    <div class="checklistwrapper">
        <h3>Sample Annotations</h3>
        <ul class="checklist">
            <li><label for="samp_1"><input id="samp_1" name="samp_1" type="checkbox" /> Sample Type</label></li>
            <li><label for="samp_2"><input id="samp_2" name="samp_2" type="checkbox" /> Collection Protocol</label></li>

            <li><label for="samp_3"><input id="samp_3" name="samp_3" type="checkbox" /> Sample Name</label></li>
            <li><label for="samp_4"><input id="samp_4" name="samp_4" type="checkbox" /> Location</label></li>
            <li><label for="samp_5"><input id="samp_5" name="samp_5" type="checkbox" /> Owner</label></li>
        </ul>
        <div class="checklistfooter">
            <input type="checkbox" name="all" id="all2" /><label for="all2">All</label>

        </div>
    </div>
    
    <div class="checklistwrapper">
        <h3>Array Annotations</h3>
        <ul class="checklist">
            <li><label for="gene_1"><input id="gene_1" name="gene_1" type="checkbox" /> Gene Name</label></li>
            <li><label for="gene_2"><input id="gene_2" name="gene_2" type="checkbox" /> Gene Symbol</label></li>

            <li><label for="gene_3"><input id="gene_3" name="gene_3" type="checkbox" /> Functional Role</label></li>
            <li><label for="gene_4"><input id="gene_4" name="gene_4" type="checkbox" /> Cellular Process</label></li>
            <li><label for="gene_5"><input id="gene_5" name="gene_5" type="checkbox" /> Subcellular Location</label></li>
        </ul>
        <div class="checklistfooter">
            <input type="checkbox" name="all" id="all4" /><label for="all4">All</label>

        </div>
    </div>
    
    <div class="checklistwrapper">
        <h3>Image Annotations</h3>
        <ul class="checklist">
            <li><label for="imag_1"><input id="imag_1" name="imag_1" type="checkbox" /> Sample Type</label></li>
            <li><label for="imag_2"><input id="imag_2" name="imag_2" type="checkbox" /> Collection Protocol</label></li>

            <li><label for="imag_3"><input id="imag_3" name="imag_3" type="checkbox" /> Sample Name</label></li>
            <li><label for="imag_4"><input id="imag_4" name="imag_4" type="checkbox" /> Location</label></li>
            <li><label for="imag_5"><input id="imag_5" name="imag_5" type="checkbox" /> Owner</label></li>
        </ul>
        <div class="checklistfooter">
            <input type="checkbox" name="all" id="all3" /><label for="all3">All</label>

        </div>
    </div>
    
    <div class="clear"></div>
    
</div>
    
<!--/Columns-->

<!--Sort Order-->

    
    <div id="sortorder" class="box2" style="display:none;">
        
        <h2>Set Sort Order for Selected Columns</h2>
        
        <table class="data">
            <tr>
                <th>Column</th>
                <th>Order (L-R)</th>
                <th>Vertical Sorting (1st)</th>

                <th>Vertical Sorting (2nd)</th>
                <th>Action</th>
            </tr>
            <tr class="odd">
                <td class="title">Patient ID</td>
                <td>
                    <select name="order1">

                        <option selected="selected">1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>

                </td>
                <td>
                    <input type="radio" name="sort_1" checked="checked" value="ascending" id="sort1_1_ascending" /><label for="sort1_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort1_1_descending" /><label for="sort1_1_descending">Descending</label>
                </td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort1_2_ascending" /><label for="sort1_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort1_2_descending" /><label for="sort1_2_descending">Descending</label>
                </td>
                <td><a href="#" class="remove">Remove Column</a></td>
            </tr>
            <tr>
                <td class="title">Patient Age</td>
                <td>

                    <select name="order1">
                        <option>1</option>
                        <option selected="selected">2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>

                    </select>
                </td>
                <td>
                    <input type="radio" name="sort_1" value="ascending" id="sort2_1_ascending" /><label for="sort2_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort2_1_descending" /><label for="sort2_1_descending">Descending</label>
                </td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort2_2_ascending" /><label for="sort2_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort2_2_descending" /><label for="sort2_2_descending">Descending</label>
                </td>
                <td><a href="#" class="remove">Remove Column</a></td>
            </tr>
            <tr class="odd">
                <td class="title">Tumor Size</td>
                <td>

                    <select name="order1">
                        <option>1</option>
                        <option>2</option>
                        <option selected="selected">3</option>
                        <option>4</option>
                        <option>5</option>

                    </select>
                </td>
                <td>
                    <input type="radio" name="sort_1" value="ascending" id="sort3_1_ascending" /><label for="sort3_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort3_1_descending" /><label for="sort3_1_descending">Descending</label>
                </td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort3_2_ascending" /><label for="sort3_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort3_2_descending" /><label for="sort3_2_descending">Descending</label>
                </td>
                <td><a href="#" class="remove">Remove Column</a></td>
            </tr>
            <tr>
                <td class="title">Gene Symbol</td>
                <td>

                    <select name="order1">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option selected="selected">4</option>
                        <option>5</option>

                    </select>
                </td>
                <td>
                    <input type="radio" name="sort_1" value="ascending" id="sort4_1_ascending" /><label for="sort4_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort4_1_descending" /><label for="sort4_1_descending">Descending</label>
                </td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort4_2_ascending" /><label for="sort4_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort4_2_descending" /><label for="sort4_2_descending">Descending</label>
                </td>
                <td><a href="#" class="remove">Remove Column</a></td>
            </tr>
            <tr class="odd">
                <td class="title">Expression</td>
                <td>

                    <select name="order1">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option selected="selected">5</option>

                    </select>
                </td>
                <td>
                    <input type="radio" name="sort_1" value="ascending" id="sort5_1_ascending" /><label for="sort5_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort5_1_descending" /><label for="sort5_1_descending">Descending</label>
                </td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort5_2_ascending" /><label for="sort5_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort5_2_descending" /><label for="sort5_2_descending">Descending</label>
                </td>
                <td><a href="#" class="remove">Remove Column</a></td>
            </tr>
            
        </table>
        <!--Buttons-->
        
        <div class="actionsrow">
            <del class="btnwrapper">

                <ul class="btnrow">         
                    <li><a href="javascript://" class="btn" onclick="this.blur();"><span class="btn_img"><span class="cancel">Reset Sorting</span></span></a></li>
                </ul>   
            </del>
        </div>
        
        <!--Buttons-->
                                                                
    </div>
    
<!--/Sort Order-->


    