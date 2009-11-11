<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
     
    <s:actionerror />
    <!-- Gene List Gene Results -->
    <s:if test="!genes.isEmpty()">
    <s:div cssStyle="padding-left: 5px;">
        <s:div cssClass="columnWrapper">
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 20px; padding-bottom: 1px;">
                <input type="checkbox" name="checkall" checked="checked"
                    onclick="checkUncheckAll(this);" />
            </s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 100px;"> Symbol </s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 300px;"> Full Name </s:div>
        </s:div><br>
        <s:iterator value="genes" status="status">
            <s:if test="#status.odd == true">
                <s:div cssClass="columnWrapper" cssStyle="background-color: #ffffff;">
                    <s:div cssClass="left1"><input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>"/> </s:div>
                    <s:div cssClass="left2"><s:property value="symbol" /></s:div>
                    <s:div cssClass="right"><s:property value="fullName" /></s:div>
                </s:div><br>
            </s:if>
            <s:else>
                <s:div cssClass="columnWrapper" cssStyle="background-color: #dcdcdc;">
                    <s:div cssClass="left1"><input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>"/> </s:div>
                    <s:div cssClass="left2"><s:property value="symbol" /></s:div>
                    <s:div cssClass="right"><s:property value="fullName" /></s:div>
                </s:div><br>
            </s:else>
        </s:iterator>
        <button type="button" onclick="captureGeneListCheckBoxes('<s:property value='geneSymbolElementId' />')">Use Genes</button>    
    </s:div>
    </s:if>
    <!-- /Gene List Gene Results -->


