<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content" >


<!--Page Help-->

<div class="pagehelp"><a href="javascript:openWikiHelp('FoDnAg', 'id-5-ViewingQueryResults-CreatingaGeneorSubjectList')" class="help">
&nbsp;</a>
</div>

<!--/Page Help-->
    <h1><s:property value="#subTitleText" /></h1>
    
    <s:if test="%{anonymousUser}">
    <font color="red"><strong>Must be registered to use this feature.</strong></font>
    </s:if>
    <s:else>
    <p>Click <strong>Create List</strong> to create a new List.</p>
    <div class="form_wrapper_outer">
 
        <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Create a New List</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">    

                    <s:actionerror/>
                    <s:form id="manageList" name="manageList" method="post" enctype="multipart/form-data" theme="simple">
                        <s:token />
                        <s:hidden name="selectedAction" />
                        <table>
                            <tr>
                                <td><s:textfield id="listName" name="listName" label="List Name" size="100" theme="css_xhtml" requiredLabel="true"/></td>
                            </tr>
                            <tr>
                                <td><s:textarea label="List Description" name="description"
                                    cols="40" rows="4" cssStyle="width: 280px;" theme="css_xhtml" /></td>
                            </tr>
                            <s:if test="%{studyManager}">
                                <tr><td>
                                    <div class="wwgrp">
                                        <div class="wwlbl"><label class="label">Make Visible to Others</label></div>
                                        <div class="wwctrl">
                                            <s:checkbox name="visibleToOther"/>
                                        </div>
                                    </div>
                                </td></tr>
                            </s:if>
                            <tr>
                                <td>
                                    <div class="wwlbl"><label class="label">List Type</label></div>
                                        <div class="wwctrl">
                                            <s:radio id="listType" name="listType"
                                                list="@gov.nih.nci.caintegrator.web.action.abstractlist.ListTypeEnum@values()"
                                                listValue="value" onclick="checkListType(this.value);"/>
                                        </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <s:div id="geneDiv" cssStyle="display: block;">
                                        <div class="wwgrp">
                                            <div class="wwlbl"><label class="label">Gene Symbols</label></div>
                                                <s:component template="genetextfield.ftl" theme="cai2simple">
                                                    <s:param name="createTextField" value="true" />
                                                    <s:param name="textFieldId" value="%{'geneSymbolsId'}"/>
                                                    <s:param name="textFieldName" value="%{'geneInputElements'}"/>
                                                </s:component>
                                            <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element">
	                                            <span class="wwlbl">(comma separated list)</span>
	                                            <span class="wwctrl"></span>
	                                        </s:div>
                                        </div>
                                    </s:div>
                                    <s:div id="subjectDiv" cssStyle="display: none;">
                                        <s:textfield id="subjectInputElements" name="subjectInputElements" label="Subject Ids"
                                            theme="css_xhtml" />
                                        <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element">
                                            <span class="wwlbl">(comma separated list)</span>
                                            <span class="wwctrl"></span>
                                        </s:div>
                                    </s:div>
                                </td>
                            </tr>
                            <tr>
                            <td align="center">
                                OR
                            </td>
                            </tr>
                            <tr>
                                <td><s:file id="listFile" name="listFile" label="Upload File"
                                        theme="css_xhtml" />
                                    <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element">
                                        <span class="wwlbl">(csv file format)</span>
                                        <span class="wwctrl"></span>
                                    </s:div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="wwgrp">
                                        <div class="wwlbl"><label class="label">&nbsp</label></div>
                                        <div class="wwctrl">
                                            <s:submit value="Create List" align="center" action="manageList"
                                            onclick="return setSelectedAction('createList');" theme="simple" />
                                            <s:submit value="Cancel" align="center" action="manageList"
                                            onclick="return setSelectedAction('cancel');" theme="simple" />
                                            </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                            
                    </s:form>
                </td>
            </tr>
        </table>   
    </div>
    </s:else>
    <script type="text/javascript">
        checkListType('<s:property value="listType"/>');
    </script>
</div>

<div class="clear"><br />
</div>

<script type="text/javascript">
    
    function setSelectedAction(selectAction) {
        document.manageList.selectedAction.value = selectAction;
        return true;
    }
    
    function checkListType(type) {
        if (type == "Gene List") {
            document.getElementById("geneDiv").style.display = "block";
            document.getElementById("subjectDiv").style.display = "none";
        }
        else {
            document.getElementById("geneDiv").style.display = "none";
            document.getElementById("subjectDiv").style.display = "block";
        }
    }
    
</script>
