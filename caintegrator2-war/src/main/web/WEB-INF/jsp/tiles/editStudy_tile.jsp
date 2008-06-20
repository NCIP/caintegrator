<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <h1>Edit Study</h1>
    
    <s:form>
    <s:textfield label="Study Name" name="study.shortTitleText" />
    <s:textarea label="Study Description" name="study.longTitleText" />
    <s:submit action="saveStudy" value="Save" />
    <s:submit action="deployStudy" value="Deploy" />
    </s:form>
    
    <table>
        <tr>
            <th colspan="2">Clinical Data Sources</th>
        </tr>
        <tr>
            <th>Type</th>
            <th>Description</th>
        </tr>
        <s:form >
        <s:iterator value="studyConfiguration.clinicalConfigurationCollection" status="status">
        <tr>
            <td><s:property value="type" /></td>
            <td><s:property value="description" /></td>
        </tr>
        </s:iterator>
        </s:form>
        <tr>
            <th colspan="2">
                <s:form action="addClinicalFile" method="post" enctype="multipart/form-data">
                    <s:file name="clinicalFile" label="File" accept="text/plain" />
                    <s:submit value="Add Clinical Data File"/>
                </s:form>
            </th>
        </tr>
    </table>
    
    <s:form>
    <table>
        <tr>
            <th colspan="2">Genomic Data Sources</th>
        </tr>
        <tr>
            <th>Server</th>
            <th>Experiment Identifier</th>
        </tr>
        <s:iterator value="studyConfiguration.genomicDataSources" status="status">
        <tr>
            <td><s:property value="serverProfile.hostname" /></td>
            <td><s:property value="experimentIdentifier" /></td>
        </tr>
        </s:iterator>
        <tr>
            <th colspan="2"><s:submit action="addGenomicSource" value="Add" /></th>
        </tr>
    </table>
    </s:form>
            
</div>

<div class="clear"><br /></div>
