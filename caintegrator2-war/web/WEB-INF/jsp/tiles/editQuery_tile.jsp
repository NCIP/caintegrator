<%@ page language="java" 
import="java.util.*, 
    com.opensymphony.xwork2.ActionContext,
     gov.nih.nci.caintegrator2.web.action.query.AnnotationSelection,
    gov.nih.nci.caintegrator2.web.action.query.QueryAnnotationCriteria" 
pageEncoding="ISO-8859-1" %>

<%@ taglib prefix="s" uri="/struts-tags"%>

<script language="javascript">
        
    function uncheckClinicalAnnotatations(){
    if(document.manageQueryForm.all1.checked == false) {
    var size='<s:property value="getClinicalDefinitionsSize()"/>';
        for(i=1;i<=size;i++){
            var temp = 'selectedClinicalAnnotations-' + i;
            document.manageQueryForm[temp].checked = false;
        }
        }
        else{
        var size='<s:property value="getClinicalDefinitionsSize()"/>';
        for(i=1;i<=size;i++){
            var temp = 'selectedClinicalAnnotations-' + i;
            document.manageQueryForm[temp].checked = true;
        }
        }
    }
    function uncheckImageAnnotatations(){
    if(document.manageQueryForm.all2.checked == false) {
    var size='<s:property value="getImageDefinitionsSize()"/>';
        for(i=1;i<=size;i++){
            var temp = 'selectedImageAnnotations-' + i;
            document.manageQueryForm[temp].checked = false;
        }
      }
      else{
        var size='<s:property value="getImageDefinitionsSize()"/>';
        for(i=1;i<=size;i++){
            var temp = 'selectedImageAnnotations-' + i;
            document.manageQueryForm[temp].checked = true;
        }
      }
    }
</script>
<!--Search Criteria-->
   

<div id="criteria" class="box2">
    
    <h2>Define Search Criteria</h2>                                                                                                             
    
    <ul id="sectabs" class="tabs">
        <li><a href="#basic" class="active">Basic Criteria Definition</a></li>
        <li><a href="#advanced">Advanced Criteria Definition</a></li>
    </ul>

    
    <div class="clear"></div>
    <div class="whitebg">
    

            
            <!--Basic Criteria Definition-->
            
            <div id="basic">
                
                
                <s:hidden name="manageQueryHelper.advancedView" value="false" />
                <s:hidden name="selectedAction" value="" />
                
                <!-- Add query criterion row selection -->
                <table class="data">
	                <tr>
	                <s:actionerror/>
	                </tr>
                    <tr>
                        <td colspan="4" class="tableheader">
                            <select name="selectedRowCriterion" id="searchcriteriaadd1" style="margin-left:5px; width:200px">
                                <option>&ndash; Select Criteria Type &ndash;</option>

                                <option value="subject">Clinical</option>
                                <option value="geneExpression">Gene Expression</option>
                                <option value="imageSeries">Image Series</option>
                            </select>
                            
                             <ul class="btnrow" style="margin:-22px 0 0 200px; height:32px">         
                                <li><s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.manageQueryForm.selectedAction.value = 'addCriterionRow';document.manageQueryForm.submit();"><span class="btn_img"><span class="add">Add</span></span></s:a></li>
                            </ul>
                        </td>
                    </tr>
                    
                    <!-- Reproduce existing query criteria rows if any -->
 
                    <s:if test="!manageQueryHelper.queryCriteriaRowList.isEmpty()">
                        <s:iterator value="manageQueryHelper.queryCriteriaRowList" status="itStatus" id="currentRow">
                            <tr>
                            <td></td>
                            <s:if test="!rowType.value.equals('geneExpression')">
                                <td><s:select name="selectedAnnotations" list="annotationSelections.annotationDefinitions" listValue="displayName" listKey="displayName" value="#currentRow.annotationSelection" label="Criterion" theme="simple"/></td>
                            </s:if>
                            <s:else>
                                <td><s:select name="selectedAnnotations" list="annotationSelections.genomicAnnotationDefinitions" listValue="value" listKey="value" value="#currentRow.annotationSelection" label="Criterion" theme="simple"/></td>
                            </s:else>
                            
                            <td><s:select name="selectedOperators" list="annotationSelections.currentAnnotationOperatorSelections" value="#currentRow.annotationOperatorSelection" theme="simple"/></td>
                            <td><s:textfield name="selectedValues" value="%{annotationValue}" size="30" theme="simple"/></td>
                            </tr>
                        </s:iterator>
                     </s:if>
                     <s:else>   
                        <tr class="odd">
                            <td colspan="4" class="value_inline">
                                <p style="margin:0; padding:2px 0 3px 0; text-align:center;"><strong>No criteria added</strong>. Please select criteria from the pulldown box.</p>
                            </td>
                        </tr>
                     </s:else>   
                </table>
                                                                        
                <div class="tablefooter">
                    <s:radio name="selectedBasicOperator" list="{'or','and'}"></s:radio>
                </div>
				

            
	            <!--Buttons-->

	            <div class="actionsrow">
	                
	                <del class="btnwrapper">
	                    <ul class="btnrow">
                           <li><s:a href="#" cssClass="btn" onclick="document.manageQueryForm.selectedAction.value = 'executeQuery';document.manageQueryForm.submit();"><span class="btn_img"><span class="search">Run Search</span></span></s:a></li><li><span class="btn_img"><span class="search"><br></span></span></li>
	                    </ul>   
	                </del>
	            </div>
			
		
			</div>
            
            <!--/Buttons-->
            
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
        <h2>Select Results Display</h2>
        <div>
        <b>Select Result Type: </b>
        <s:radio name="manageQueryHelper.resultType" list="@gov.nih.nci.caintegrator2.application.query.ResultTypeEnum@getValueToDisplayableMap()" listKey="key" listValue="value"></s:radio>
        <br><br>
        <b>Select Reporter Type: </b>
        <s:radio name="manageQueryHelper.reporterType" list="@gov.nih.nci.caintegrator2.application.arraydata.ReporterTypeEnum@getValueToDisplayableMap()" listKey="key" listValue="value"></s:radio>
        </div>
    
    <h2>Select Columns for Results</h2>
    
    <div class="checklistwrapper">

        <h3>Subject Annotations</h3>
        <ul class="checklist">
            <s:checkboxlist  cssClass="checklist"name="selectedClinicalAnnotations"list="manageQueryHelper.clinicalAnnotationDefinitions" listKey="id" listValue="displayName" theme="cai2simple" value="getSelectedClinicalAnnotations()"  ></s:checkboxlist>  
        </ul>
        <ul>
        <s:checkbox name="all1" value="true" onclick="uncheckClinicalAnnotatations()"/><b>All</b>
        </ul>
    </div>
           
     <div class="checklistwrapper">
        <h3>Image Annotations</h3>
        <ul class="checklist">
            <s:checkboxlist  cssClass="checklist"name="selectedImageAnnotations"list="manageQueryHelper.imageAnnotationDefinitions" listKey="id" listValue="displayName" theme="cai2simple" value="getSelectedImageAnnotations()"></s:checkboxlist>
        </ul>
        
        <ul>
        <s:checkbox name="all2" value="true" onclick="uncheckImageAnnotatations()"/><b>All</b>
        </ul>
    </div>  
    <div class="actionsrow">
                    
                    <del class="btnwrapper">
                        <ul class="btnrow">
                           <li><s:a href="#" cssClass="btn" onclick="document.manageQueryForm.selectedAction.value = 'executeQuery';document.manageQueryForm.submit();"><span class="btn_img"><span class="search">Run Search</span></span></s:a></li>
                        </ul>   
                    </del>
   </div>
       
   <div class="clear"></div>
           
</div>
  
<!--/Columns-->

<!--Sort Order-->

    
    <div id="sortorder" class="box2" style="display:none;">
        
        <h2>Set Sort Order for Selected Columns</h2>
        
        <table class="data">
            <tr>
                <th>Column<br><br><br><br><br><br><br></th>
                <th>Order (L-R)<br><br><br><br><br><br><br></th>
                <th>Vertical Sorting (1st)<br><br><br><br><br><br><br></th>

                <th>Vertical Sorting (2nd)<br><br><br><br><br><br><br></th>
                <th>Action<br><br><br><br><br><br><br></th>
            </tr>
            <tr class="odd">
                <td class="title">Patient ID<br><br><br><br><br><br><br></td>
                <td>
                    <select name="order1">

                        <option selected="selected">1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>

                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_1" checked="checked" value="ascending" id="sort1_1_ascending" /><label for="sort1_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort1_1_descending" /><label for="sort1_1_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort1_2_ascending" /><label for="sort1_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort1_2_descending" /><label for="sort1_2_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td><a href="#" class="remove">Remove Column</a><br><br><br><br><br><br><br></td>
            </tr>
            <tr>
                <td class="title">Patient Age<br><br><br><br><br><br><br></td>
                <td>

                    <select name="order1">
                        <option>1</option>
                        <option selected="selected">2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>

                    </select>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_1" value="ascending" id="sort2_1_ascending" /><label for="sort2_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort2_1_descending" /><label for="sort2_1_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort2_2_ascending" /><label for="sort2_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort2_2_descending" /><label for="sort2_2_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td><a href="#" class="remove">Remove Column</a><br><br><br><br><br><br><br></td>
            </tr>
            <tr class="odd">
                <td class="title">Tumor Size<br><br><br><br><br><br><br></td>
                <td>

                    <select name="order1">
                        <option>1</option>
                        <option>2</option>
                        <option selected="selected">3</option>
                        <option>4</option>
                        <option>5</option>

                    </select>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_1" value="ascending" id="sort3_1_ascending" /><label for="sort3_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort3_1_descending" /><label for="sort3_1_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort3_2_ascending" /><label for="sort3_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort3_2_descending" /><label for="sort3_2_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td><a href="#" class="remove">Remove Column</a><br><br><br><br><br><br><br></td>
            </tr>
            <tr>
                <td class="title">Gene Symbol<br><br><br><br><br><br><br></td>
                <td>

                    <select name="order1">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option selected="selected">4</option>
                        <option>5</option>

                    </select>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_1" value="ascending" id="sort4_1_ascending" /><label for="sort4_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort4_1_descending" /><label for="sort4_1_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort4_2_ascending" /><label for="sort4_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort4_2_descending" /><label for="sort4_2_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td><a href="#" class="remove">Remove Column</a><br><br><br><br><br><br><br></td>
            </tr>
            <tr class="odd">
                <td class="title">Expression<br><br><br><br><br><br><br></td>
                <td>

                    <select name="order1">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option selected="selected">5</option>

                    </select>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_1" value="ascending" id="sort5_1_ascending" /><label for="sort5_1_ascending">Ascending</label>
                    <input type="radio" name="sort_1" value="descending" id="sort5_1_descending" /><label for="sort5_1_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td>
                    <input type="radio" name="sort_2" value="ascending" id="sort5_2_ascending" /><label for="sort5_2_ascending">Ascending</label>

                    <input type="radio" name="sort_2" value="descending" id="sort5_2_descending" /><label for="sort5_2_descending">Descending</label>
                <br><br><br><br><br><br><br></td>
                <td><a href="#" class="remove">Remove Column</a><br><br><br><br><br><br><br></td>
            </tr>
            
        </table>
        <!--Buttons-->
        
        <div class="actionsrow">
            <del class="btnwrapper">

                <ul class="btnrow">         
                    <li>'&lt;&gt;()<a href="javascript://" class="btn" onclick="this.blur();"><span class="btn_img"><span class="cancel">Reset Sorting</span></span></a><br><br><br><br><br><br><br><br><br></li>
                </ul>   
            </del>
        </div>
        
        <!--Buttons-->
                                                                
    </div>
    
<!--/Sort Order-->


    