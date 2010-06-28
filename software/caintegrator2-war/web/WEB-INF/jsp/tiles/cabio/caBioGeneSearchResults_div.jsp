<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
    
    
    <s:actionerror />
    <!-- CaBio Pathway Results -->
    <s:if test="%{!caBioPathways.empty}">
        <hr align="center" width="100%" />
        <s:set name="numberPathways" value="%{caBioPathways.size}" />
        <s:div cssStyle="padding-left: 5px;">
        <s:property value="#numberPathways" /> pathway(s) found.
            <s:div cssClass="columnWrapper">
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 20px; padding-bottom: 1px;"> <input type="checkbox" name="checkall" onclick="checkUncheckAll(this,'searchParams.filterGenesOnStudy');" checked="checked"/> </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 125px;"> Name </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 125px;"> Title </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 300px; padding-left: 2px;"> Description </s:div>
            </s:div><br>
            <s:iterator value="caBioPathways" status="status">
                <s:if test="#status.odd == true">
                    <s:div cssClass="columnWrapper" cssStyle="background-color: #ffffff;">
                       <s:div cssClass="left1"><s:checkbox theme="simple" id="caBioPathwayCkBox_%{#status.count - 1}" name="caBioPathways[%{#status.count - 1}].checked"/> </s:div>
                       <s:div cssClass="left2" cssStyle="width: 125px;" title="%{name}"><s:property value="displayableName" /></s:div>
                       <s:div cssClass="left3" cssStyle="width: 125px;"><s:property value="displayValue" /></s:div>
                       <s:div cssClass="right" title="%{description}"><s:property value="displayableDescription" /><s:div cssStyle="font-size: .9em;font-style: italic">  (mouseover for full description)</s:div></s:div>
                    </s:div><br>
                </s:if>
                <s:else>
                    <s:div cssClass="columnWrapper" cssStyle="background-color: #dcdcdc;">
                       <s:div cssClass="left1"><s:checkbox theme="simple" id="caBioPathwayCkBox_%{#status.count - 1}" name="caBioPathways[%{#status.count - 1}].checked"/> </s:div>
                       <s:div cssClass="left2" cssStyle="width: 125px;" title="%{name}"><s:property value="displayableName" /></s:div>
                       <s:div cssClass="left3" cssStyle="width: 125px;"><s:property value="displayValue" /></s:div>
                       <s:div cssClass="right" title="%{description}"><s:property value="displayableDescription" /><s:div cssStyle="font-size: .9em;font-style: italic">  (mouseover for full description)</s:div></s:div>
                    </s:div><br>
                </s:else>
            </s:iterator>
            <button type="button" 
            onclick="runCaBioPathwayGeneSearch(${numberPathways})"> Search Pathways For Genes
            </button>    
        </s:div>
    </s:if>
    <!-- /CaBio Pathway Results -->
    <!-- CaBio Gene Results -->
    <s:if test="%{!caBioGenes.empty}">
        <hr align="center" width="100%" />
        <s:div cssStyle="padding-left: 5px;">
        <s:property value="caBioGeneCount" />
            <s:div cssClass="columnWrapper">
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 20px; padding-bottom: 1px;"> <input type="checkbox" name="checkall" onclick="checkUncheckAll(this,'searchParams.filterGenesOnStudy');" checked="checked"/> </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 100px;"> Symbol </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 100px;"> HUGO Symbol </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 50px;"> Taxon </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 200px;"> Full Name </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 200px;"> Gene Aliases </s:div>
                <s:div cssClass="columnHeader" cssStyle="float: left; width: 200px;"> Database Cross Reference </s:div>
            </s:div><br>
    	    <s:iterator value="caBioGenes" status="status">
                <s:if test="#status.odd == true">
                    <s:div cssClass="columnWrapper" cssStyle="background-color: #ffffff;">
                       <s:div cssClass="left1"><input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>"/> </s:div>
                       <s:div cssClass="left2"><s:property value="symbol" /></s:div>
                       <s:div cssClass="left3"><s:property value="hugoSymbol" /></s:div>
                       <s:div cssClass="left4"><s:property value="taxonCommonName" /></s:div>
                       <s:div cssClass="left5"><s:property value="fullName" /></s:div>
                       <s:div cssClass="left6"><s:property value="geneAliases" /></s:div>
                       <s:div cssClass="right"><s:property value="databaseCrossReferences" /></s:div>
                    </s:div><br>
                </s:if>
                <s:else>
                    <s:div cssClass="columnWrapper" cssStyle="background-color: #dcdcdc;">
                       <s:div cssClass="left1"><input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>"/> </s:div>
                       <s:div cssClass="left2"><s:property value="symbol" /></s:div>
                       <s:div cssClass="left3"><s:property value="hugoSymbol" /></s:div>
                       <s:div cssClass="left4"><s:property value="taxonCommonName" /></s:div>
                       <s:div cssClass="left5"><s:property value="fullName" /></s:div>
                       <s:div cssClass="left6"><s:property value="geneAliases" /></s:div>
                       <s:div cssClass="right"><s:property value="databaseCrossReferences" /></s:div>
                    </s:div><br>
                </s:else>
    	    </s:iterator>
            <button type="button" onclick="captureCaBioCheckBoxes('<s:property value='geneSymbolElementId' />')">Use Genes</button>    
        </s:div>
    </s:if>
    <!-- /CaBio Gene Results -->


