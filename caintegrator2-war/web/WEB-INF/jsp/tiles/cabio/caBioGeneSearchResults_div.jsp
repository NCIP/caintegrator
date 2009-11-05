<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
     
    <s:actionerror />
    <!-- CaBio Gene Results -->
    <s:if test="%{!caBioGenes.empty}">
        <s:div cssStyle="padding-left: 5px;">
            <s:div cssClass="columnWrapper">
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 20px; padding-bottom: 1px;"> <input type="checkbox" name="checkall" onclick="checkUncheckAll(this,'geneSearchParams.filterGenesOnStudy');" checked="checked"/> </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 100px;"> Symbol </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 100px;"> HUGO Symbol </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 50px;"> Taxon </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 300px;"> Full Name </s:div>
            </s:div><br>
    	    <s:iterator value="caBioGenes" status="status">
                <s:if test="#status.odd == true">
                    <s:div cssClass="columnWrapper" cssStyle="background-color: #ffffff;">
                       <s:div cssClass="left1"><input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>"/> </s:div>
                       <s:div cssClass="left2"><s:property value="symbol" /></s:div>
                       <s:div cssClass="left3"><s:property value="hugoSymbol" /></s:div>
                       <s:div cssClass="left4"><s:property value="taxonCommonName" /></s:div>
                       <s:div cssClass="right"><s:property value="fullName" /></s:div>
                    </s:div><br>
                </s:if>
                <s:else>
                    <s:div cssClass="columnWrapper" cssStyle="background-color: #dcdcdc;">
                       <s:div cssClass="left1"><input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>"/> </s:div>
                       <s:div cssClass="left2"><s:property value="symbol" /></s:div>
                       <s:div cssClass="left3"><s:property value="hugoSymbol" /></s:div>
                       <s:div cssClass="left4"><s:property value="taxonCommonName" /></s:div>
                       <s:div cssClass="right"><s:property value="fullName" /></s:div>
                    </s:div><br>
                </s:else>
    	    </s:iterator>
            <button type="button" onclick="captureCaBioCheckBoxes('<s:property value='geneSymbolElementId' />')">Use Genes</button>    
        </s:div>
    </s:if>
    <!-- /CaBio Gene Results -->


