<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><span class="help"><a href="#">&nbsp;</a></span></div>
    
    <!--/Page Help-->           
    
    <h1>Edit Genomic Data Source</h1>
    <s:actionerror/>
    <s:form action="saveGenomicSource">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="genomicSource.id" />
        <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" />
        <s:textfield label="caArray server JNDI Port" name="genomicSource.serverProfile.port" />
        <s:textfield label="caArray Username" name="genomicSource.serverProfile.username" />
        <s:password label="caArray Password" name="genomicSource.serverProfile.password" />
        <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" />
        <s:submit value="Save" />
    </s:form>
            
</div>

<div class="clear"><br /></div>
