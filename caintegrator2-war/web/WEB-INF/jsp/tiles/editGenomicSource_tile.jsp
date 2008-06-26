<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Edit Genomic Data Source</h1>
    <s:form>
        <s:textfield name="genomicDataSource.serverProfile.hostname" label="caArray Server Hostname" />
        <s:textfield name="genomicDataSource.serverProfile.port" label="caArray server JNDI Port" />
        <s:textfield name="genomicDataSource.serverProfile.username" label="caArray Username" />
        <s:textfield name="genomicDataSource.serverProfile.password" label="caArray Password" />
        <s:textfield name="genomicDataSource.experimentIdentifier" label="caArray Experiment Id" />
        <s:submit action="verifyGenomicSourceConnection" value="Verify Connection" />
        <s:submit action="saveStudy" value="Save" />
    </s:form>
            
</div>

<div class="clear"><br /></div>
