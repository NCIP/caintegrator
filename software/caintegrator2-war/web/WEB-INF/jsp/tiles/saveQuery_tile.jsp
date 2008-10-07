<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Save Search-->
    
    <div id="savesearch" class="box2" style="display:none;">                                                
                
        <h2>Save This Search</h2> 

        <table class="form">
        
            <tr>
                <td class="label"><label for="searchname">Search Name:</label></td>
                <td class="value"><s:textfield label="Search Name" name="searchName" theme="simple"/></td>
            </tr>
            <tr>
                <td class="label"><label for="searchdesc">Search Description:</label></td>
                <td class="value"><s:textarea label="Search Description" name="searchDescription" cols="40" rows="2" theme="simple"/></td>

            </tr>
            <tr>
                <td class="label"><input type="checkbox" name="columpref" id="columnpref" /></td>
                <td class="value"><label for="columnpref">Make Column selections my defaults for future searches</label></td>
            </tr>
            <tr>
                <td class="label"><input type="checkbox" name="sortpref" id="sortpref" /></td>
                <td class="value"><label for="sortpref">Make Sort Order selections my defaults for future searches</label></td>

            </tr>

        </table>
        
	        <!--Buttons-->
	        
	        <div class="actionsrow">
	            <del class="btnwrapper">
	                <ul class="btnrow">         
	                    <li><s:a href="#" cssClass="btn" onclick="document.manageQueryForm.selectedAction.value = 'saveQuery';document.manageQueryForm.submit();"><span class="btn_img"><span class="save">Save</span></span></s:a></li>
	                </ul>   
	            </del>
	
	        </div>
	
	        <!--Buttons-->
    </div>
    
<!--/Save Search--> 