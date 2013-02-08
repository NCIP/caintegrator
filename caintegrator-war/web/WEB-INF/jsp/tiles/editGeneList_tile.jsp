<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content" >


<!--Page Help-->

<div class="pagehelp"><a href="javascript:openWikiHelp('FoDnAg', '5-ViewingQueryResults-EditingaGeneorSubjectList')" class="help">
&nbsp;</a>
</div>

<!--/Page Help-->

<script language="javascript">

    function deleteList () {
        if (confirm('This gene list will be permanently deleted.')) {
            submitForm("deleteList");
        }
    }
    
    function submitForm(selectAction) {
        document.editGeneListForm.selectedAction.value = selectAction;
        document.editGeneListForm.submit();
    }
    
</script>

    <h1><s:property value="#subTitleText" /></h1>
    
    <p>Click <strong>Save or Delete</strong> to perform editing on this Gene List.</p>
    <div class="form_wrapper_outer">
    <table class="form_wrapper_table">
            <tr>
                <td colspan="2" style="padding: 5px;">    
                    <font color="green"> <s:actionmessage /> </font>
                    <s:actionerror/>
                    <s:if test="editOn">
                        <s:form id="editGeneListForm" name="editGeneListForm" action="editGeneList"
                            method="post" enctype="multipart/form-data" theme="css_xhtml">
                            <s:token />
                            <s:hidden name="selectedAction" />
                            <s:hidden name="listOldName" />
                            <s:hidden name="globalList" />
                            <s:textfield id="listName" name="listName" label="Gene List Name" size="100" theme="css_xhtml" /><br>
                            
                            <div class="wwgrp" >
                                <div class="wwlbl" >
                                    <label class="label" for="lastModifiedDate">Last Modified Date:</label>
                                </div> 
                                <div class="wwctrl">
                                    <s:property value="abstractList.displayableLastModifiedDate" /><br>
                                </div> 
                            </div> <br/>
                            
                            <s:if test="%{studyManager}">
                                <s:checkbox name="visibleToOther" label="Make Visible to Others" labelposition="left" />
                                <br/>
                            </s:if>
                            <s:textarea label="Gene Symbols" name="geneSymbolListing"
                                disabled="true" rows="5" cols="20" theme="css_xhtml" /><br>

                            <!--Buttons-->

                            <div class="actionsrow"><del class="btnwrapper"><ul class="btnrow">
                                <li><s:a href="#" cssClass="btn" onclick="submitForm('saveList');">
                                    <span class="btn_img"><span class="save">Save</span></span>
                                </s:a></li>
                                <li><s:a href="#" cssClass="btn" onclick="deleteList();">
                                    <span class="btn_img"><span class="delete">Delete</span></span>
                                </s:a></li>
                                <li><s:a href="#" cssClass="btn" onclick="submitForm('cancel');">
                                    <span class="btn_img"><span class="cancel">Cancel</span></span>
                                </s:a></li>
                            </ul></del></div>

                            <!--Buttons-->
                        </s:form>
                    </s:if>
                </td>
            </tr>
    </table>    
    </div>
</div>

<div class="clear"><br />
</div>