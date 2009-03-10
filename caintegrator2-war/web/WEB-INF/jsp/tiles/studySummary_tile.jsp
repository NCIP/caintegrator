<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('welcome_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->         
    
    <!--ADD CONTENT HERE-->
    <s:actionerror/>
                        
    <h1>Welcome to <strong> <s:property value="studySummary.studyName" /></strong> Powered by caIntegrator2</h1>
    
    <div class="box">
        <h2>About <s:property value="studySummary.studyName" /> </h2>
        <p>
            <strong>Description:</strong> <s:property value="studySummary.studyDescription" />
        </p>

    </div>
    
    <table class="study_summary">
        <tr>
            <th colspan="3">Clinical Data</th>
        </tr>
        <tr>
            <td align="right" style="padding:0px 15px 0px 15px;"><b>Number of Subjects</b></td>
            <td> </td>
            <td> <s:property value="studySummary.numberSubjects"/> </td>
        </tr>
        
        <tr>
            <td align="right" style="padding:0px 15px 0px 15px;"><b>Number of Annotation Columns</b></td>
            <td> </td>
            <td> <s:property value="studySummary.numberSubjectAnnotationColumns"/> </td>
        </tr>
        <s:iterator value="%{studySummary.study.survivalValueDefinitionCollection}" status="survivalValueDefinitionStatus">
        <tr>
            <td align="right" style="padding:0px 15px 0px 15px;"><b><s:property value="name"/> </b></td>
            <td> Start </td>
            <td> <s:property value="survivalStartDate.displayName"/> </td>
        </tr>
        <tr>
            <td />
            <td> Death </td>
            <td> <s:property value="deathDate.displayName"/> </td>
        </tr>
        <tr>
            <td />
            <td> Last Followup </td>
            <td> <s:property value="lastFollowupDate.displayName"/> </td>
        </tr>
        </s:iterator>
        
        <s:if test="studySummary.genomicStudy" >
            <tr>
                <th colspan="3">Genomic Data</th>
            </tr>
            <s:iterator value="%{studySummary.genomicDataSources}" status="genomicDataSourceStatus">
            <tr>
                <td align="right" style="padding:0px 15px 0px 15px;"><b>Array</b></td>
                <td> </td>
                
                <td> 
                    Experiment Name: <s:property value="experimentName"/>
                    <br>
                    Platforms: 
                        <s:iterator value="%{platforms}" status="platformStatus">
                            <s:property value="name"/> 
                        </s:iterator>
                </td>
            </tr>
            
            <tr>
	            <td align="right" style="padding:0px 15px 0px 15px;"><b>Number of Samples</b></td>
	            <td> </td>
	            <td> <s:property value="numberSamples"/> </td>
            </tr>
            <tr>
                <td align="right" style="padding:0px 15px 0px 15px;"><b>Number of Control Samples</b></td>
                <td> </td>
                <td> <s:property value="numberControlSamples"/> </td>
            </tr>
            <tr>
                <td align="right" style="padding:0px 15px 0px 15px;"><b>Control Samples Set</b></td>
                <td> </td>
                <td> <s:if test="%{studySummary.numberControlSamples > 0}" >
                        Yes
                     </s:if>
                     <s:else>
                        Not Configured.
                     </s:else>  
                </td>
            </tr>
            </s:iterator>
        </s:if>
    
        <s:if test="studySummary.imagingStudy" >
	        <tr>
	            <th colspan="3">Imaging Data</th>
	        </tr>
	        <tr>
                <td align="right" style="padding:0px 15px 0px 15px;"><b>Number of Images</b></td>
                <td> </td>
                <td> <s:property value="studySummary.numberImages"/> </td>
            </tr>
        </s:if>
    </table>
    
</div>

<div class="clear"><br /></div>
