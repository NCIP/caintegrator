<#include "/${parameters.templateDir}/simple/controlheader.ftl" />


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
    onclick="showCaBioInputForm('${parameters.textFieldId}')">
    &nbsp;
</@s.a>

<br>


<#include "/${parameters.templateDir}/simple/controlfooter.ftl" />