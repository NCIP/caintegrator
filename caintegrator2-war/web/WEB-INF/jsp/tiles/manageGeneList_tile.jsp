<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content" >


<!--Page Help-->

<div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('create_gene_list_help')" class="help">
&nbsp;</a>
</div>

<!--/Page Help-->
    
<script type="text/javascript">
    
    function setSelectedAction(selectAction, geneSymbol) {
        document.manageGeneList.selectedAction.value = selectAction;
        return true;
    }
</script>

    <h1><s:property value="#subTitleText" /></h1>
    
    <s:if test="%{anonymousUser}">
    <font color="red"><strong>Must be registered to use this feature.</strong></font>
    </s:if>
    <s:else>
    <p>Click <strong>Create Gene List</strong> to create a new Gene List.</p>
    <div class="form_wrapper_outer">
 
        <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Create a New Gene List</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">    

                    <s:actionerror/>
                    <s:form id="manageGeneList" name="manageGeneList" method="post" enctype="multipart/form-data" theme="simple">
                        <s:hidden name="selectedAction" />
                        <table>
                            <tr>
                                <td><s:textfield id="geneListName" name="geneListName" label="Gene List Name"
                                    theme="css_xhtml" required="true"/></td>
                            </tr>
                            <tr>
                                <td><s:textarea label="Gene List Description" name="description"
                                    cols="40" rows="4" cssStyle="width: 280px;" theme="css_xhtml" /></td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="wwgrp">
                                        <div class="wwlbl"><label class="label">Gene Symbols</label></div>
                                        <div class="wwctrl">
                                            <s:component template="genetextfield.ftl" theme="cai2simple">
                                                <s:param name="createTextField" value="true" />
                                                <s:param name="textFieldId" value="%{'geneSymbolsId'}"/>
                                                <s:param name="textFieldName" value="%{'geneSymbols'}"/>
                                            </s:component>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td><s:file id="geneListFile" name="geneListFile" label="Upload File"
                                        theme="css_xhtml" />
                                    <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element" cssStyle="%{csvlFileDisplay}">
                                        <span class="wwlbl">(csv file format)</span>
                                        <span class="wwctrl"></span>
                                    </s:div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="wwgrp">
                                        <div class="wwlbl"><label class="label">&nbsp</label></div>
                                        <div class="wwctrl"><s:submit value="Create Gene List" align="center" action="manageGeneList"
                                            onclick="return setSelectedAction('createGeneList', this.form.geneSymbols.value);" theme="css_xhtml" /></div>
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
</div>

<div class="clear"><br />
</div>
