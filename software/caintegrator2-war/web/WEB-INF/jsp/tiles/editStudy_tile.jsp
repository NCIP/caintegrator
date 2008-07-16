<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <h1>Edit Study</h1>
    
    <s:form>
        <s:hidden name="studyConfiguration.id"  />
            <s:textfield label="Study Name" name="study.shortTitleText" />
            <s:textarea label="Study Description" name="study.longTitleText" cols="40" rows="4" />
            <s:submit action="saveStudy" value="Save" />
            <s:submit action="deployStudy" value="Deploy" />
    </s:form>
    
    <table class="data">
        <tr>
            <th colspan="3">Clinical Data Sources</th>
        </tr>
        <tr>
            <th>Type</th>
            <th>Description</th>
            <th>Action</th>
        </tr>
        <s:form>
        <s:iterator value="studyConfiguration.clinicalConfigurationCollection" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:property value="type" /></td>
            <td><s:property value="description" /></td>
            <td>
                <s:url id="editClinicalSource" action="editClinicalSource">
                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                    <s:param name="clinicalSourceConfiguration.id" value="id" />
                </s:url> 
                <s:a href="%{editClinicalSource}">Edit</s:a> 
                <s:if test="%{loadable}" >
                    <s:url id="loadClinicalSource" action="loadClinicalSource">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="clinicalSourceConfiguration.id" value="id" />
                    </s:url> 
                    <s:a href="%{loadClinicalSource}">Load</s:a> 
                </s:if>
            </td>
        </tr>
        </s:iterator>
        </s:form>
        <tr>
            <th colspan="3">
                <s:form action="addClinicalFile" method="post" enctype="multipart/form-data">
                    <s:hidden name="studyConfiguration.id"  />
                    <s:file name="clinicalFile" label="File" />
                    <s:submit value="Add Clinical Data File" />
                </s:form>
            </th>
        </tr>
    </table>
    
    <s:form>
    <s:hidden name="studyConfiguration.id"  />
    <table class="data">
        <tr>
            <th colspan="2">Genomic Data Sources</th>
        </tr>
        <tr>
            <th>Server</th>
            <th>Experiment Identifier</th>
        </tr>
        <s:iterator value="studyConfiguration.genomicDataSources" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
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
