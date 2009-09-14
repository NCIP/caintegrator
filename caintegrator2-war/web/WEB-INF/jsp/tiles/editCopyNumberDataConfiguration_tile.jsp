<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_genomic_data_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->
    
    <h1><s:property value="#subTitleText" /></h1>
    <s:actionerror/>
    <s:form name="copyNumberDataConfigurationForm" action="saveCopyNumberDataConfiguration" method="post" enctype="multipart/form-data" >
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="genomicSource.id" />
        <s:hidden name="action" />
        <s:file name="copyNumberMappingFile" label="Subject and Sample to Copy Number Mapping File" />
        
        <s:radio theme="simple" name="useGlad" list="#{true:'Use GenePattern GLAD Web Service'}" onclick="gladUrl.disabled = false; username.disabled = false; password.disabled = false; caDnaCopyUrl.disabled = true" />
        <s:textfield id="gladUrl" name="gladUrl" label="GenePattern Server URL" size="50" required="true" disabled="%{!useGlad}" />
        <s:textfield id="username" name="copyNumberDataConfiguration.segmentationService.username" label="GenePattern Username" size="50" required="true" disabled="%{!useGlad}" />
        <s:password id="password" name="copyNumberDataConfiguration.segmentationService.password" label="GenePattern Password" size="50" showPassword="true" disabled="%{!useGlad}" />
        
        <s:radio theme="simple" name="useGlad" list="#{false:'Use caDNACopy Grid Service'}" onclick="gladUrl.disabled = true; gladUrl.value = ''; username.disabled = true; username.value = ''; password.disabled = true; password.value = ''; caDnaCopyUrl.disabled = false" />
        <s:select id="caDnaCopyUrl" name="caDnaCopyUrl"
                list="caDnaCopyServices" label="CaDNACopy Service URL" required="true" disabled="%{useGlad}" />
        <s:textfield name="copyNumberDataConfiguration.changePointSignificanceLevel" label="Change Point Significance Level" />
        <s:textfield name="copyNumberDataConfiguration.earlyStoppingCriterion" label="Early Stopping Criterion" />
        <s:textfield name="copyNumberDataConfiguration.permutationReplicates" label="Permutation Replicates" />
        <s:textfield name="copyNumberDataConfiguration.randomNumberSeed" label="Random Number Seed" />
        <s:submit value="Save Segmentation Data Calculation Configuration"/>
    </s:form>
    
</div>

<div class="clear"><br /></div>
