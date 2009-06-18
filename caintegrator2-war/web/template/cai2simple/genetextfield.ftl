<#include "/${parameters.templateDir}/simple/controlheader.ftl" />

<@s.hidden name="formName" value="${parameters.formId}" />
<!-- <@s.hidden name="geneSymbolElementId" value="${parameters.textFieldId}" /> -->


<#if parameters.createTextField?exists>
    <@s.textfield theme="simple" id="${parameters.textFieldId}" name="${parameters.textFieldName}"
                 title="Enter a comma separated list of gene symbols ( Ex: EGFR, BRCA1, etc. )"/>
</#if>                 
            

&nbsp;

<@s.a theme="simple" href="" 
    cssClass="cgapLogo" 
    title="Click to find this Gene Symbol in the Cancer Genome Anatomy Project (CGAP)" 
    onclick="gotoCGAP('${displayableWorkspace.cgapUrl}','${parameters.textFieldId}')">
    &nbsp;
</@s.a>

<@s.a theme="simple" href="" 
    cssClass="caBioLogo" 
    title="Click to search caBio for genes based on keywords." 
    onclick="showCaBioInputForm(${parameters.formId}, '${parameters.dojoEventTopic}', '${parameters.textFieldId}')">
    &nbsp;
</@s.a>

<br>
<@s.div id="TB_overlay" cssClass="TB_overlayBG"/>
<@s.div theme="ajax" 
    id="caBioGeneSearchInputDiv"
    href="caBioGeneSearchInput.action"
    formId="${parameters.formId}"
    loadingText="<img src='images/ajax-loader.gif'/>"
    listenTopics="${parameters.dojoEventTopic}"
    refreshOnShow="${parameters.refreshOnShow?default('true')}" 
    cssStyle="display:none;visibility:hidden;margin-left:-140px;margin-top:-62px;width:500px;max-height: 300px; overflow:auto;"/>

<#include "/${parameters.templateDir}/simple/controlfooter.ftl" />