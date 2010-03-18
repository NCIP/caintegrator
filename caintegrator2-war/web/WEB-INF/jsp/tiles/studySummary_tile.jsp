<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('welcome_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->         
    
    <!--ADD CONTENT HERE-->
    <s:actionerror/>
                        
    <h1>Welcome to <strong> <s:property value="studySummary.studyName" /></strong></h1>
    
    <s:if test="studySummary.deployed">
    <table class="study_summary">
            <tr>
            <th colspan="4">Overview</th>
        </tr>
        <tr>
            <td align="right">Name:</td>
            <td colspan="3"><b><s:property value="studySummary.studyName" /></b></td>
        </tr>
        <tr>
            <td align="right">Description: </td>
            <td colspan="3" class="wrap"><b><s:property value="studySummary.studyDescription" /></b></td>
        </tr>
        <tr>
            <td align="right">Deployment Status: </td>
            <td><b>
                <s:if test="studySummary.deployed">
                    Deployed
                </s:if>
                <s:else>
                    Not Deployed
                </s:else> 
                </b>           
            </td>
            <td />  
            <td />
        </tr>
        <tr>
            <td align="right">Data Dictionary: </td>
            <td>
                <s:url id="dataDictionaryUrl" includeParams="none" action="viewDataDictionary" />
                <a href="${dataDictionaryUrl}">View</a>
            </td>
            <td />  
            <td />
        </tr>    
        <tr>
            <th colspan="4">Subject Annotation Data</th>
        </tr>
        <tr>
            <td align="right">Number of Subjects:</td>
            <td> <b><s:property value="studySummary.numberSubjects"/> </b> </td>
            <td />  
            <td />
        </tr>
        
        <tr>
            <td align="right">Number of Annotation Columns:</td>
            <td> <b><s:property value="studySummary.numberSubjectAnnotationColumns"/> </b></td>
            <td />  
            <td />
        </tr>
        
        <tr>
	        <td align="right">
	         Survival Definitions:
	        </td>
	        
	        <s:if test="%{studySummary.validSurvivalValueDefinitions.empty}">
		        <td> Not Configured </td>
		        <td />
		        <td />
		        </tr>
	        </s:if>
        <s:else>
	        <td />
	        <td />
	        <td />
        </tr>
        <s:iterator value="%{studySummary.validSurvivalValueDefinitions}" status="survivalValueDefinitionStatus">
        <tr>
            <td />
            <td><b><i><s:property value="name"/></i></b></td>    
            <td> Start: </td>
            <td> <b><s:property value="survivalStartDate.displayName"/> </b></td>
        </tr>
        <tr>
            <td />
            <td />
            <td> Death: </td>
            <td> <b><s:property value="deathDate.displayName"/> </b></td>
        </tr>
        <tr>
            <td />
            <td />
            <td> Last Followup: </td>
            <td> <b><s:property value="lastFollowupDate.displayName"/> </b></td>
        </tr>
        </s:iterator>
        </s:else>
        <s:if test="studySummary.genomicStudy" >
            <tr>
                <th colspan="4">Genomic Data</th>
            </tr>
            <s:iterator value="%{studySummary.genomicDataSources}" status="genomicDataSourceStatus">

            <tr>
                <td align="right">Hostname:</td>
                <td> <b><s:property value="hostName"/></b></td>
                <td />
                <td />
            </tr>
            <tr>
                <td align="right">Experiment:</td>
                <td> 
                    Name: <b><s:property value="experimentName"/></b>
                        <s:if test="hasCaArrayUrl()">
                            <a href="${caArrayUrl}" target="cai2_CaArray" title="Click to go to caArray">
                                <img src="/caintegrator2/images/caArray.gif" border="none"/>
                            </a>
                        </s:if>
                    <br>
                    Array Platforms: 
                        <s:iterator value="%{platforms}" status="platformStatus">
                            <b><s:property value="name"/> </b>
                        </s:iterator>
                </td>
                <td />
                <td />
            </tr>
            <tr>
                <td align="right">Data Type:</td>
                <td> <b><s:property value="dataType"/> </b></td>
                <td />
                <td />
            </tr>
                <s:if test="expressionData">
                    <tr>
                        <td align="right">Number of Samples: </td>
                        <td><b><s:property value="numberSamples" /> </b></td>
                        <td />
                        <td />
                    </tr>
                    <tr>
                        <td align="right">Control Samples Set: </td>
                        <td><b> <s:if test="%{numberControlSamples > 0}">
                            <s:property value="numberControlSampleSetSamples" />
                        </s:if> <s:else>
                        Not Configured
                     </s:else> </b></td>
                        <td />
                        <td />
                    </tr>
                </s:if>
                <s:elseif test="copyNumberData">
                    <tr>
                        <td align="right">Copy Number Samples: </td>
                        <td><b><s:property value="numberSamples" /> </b></td>
                        <td />
                        <td />
                    </tr>
                </s:elseif>
                <s:if test="!#genomicDataSourceStatus.last">
                    <tr>
                    <td colspan="4">
                    <hr align="center" width="80%"/>
                    </td>
                    </tr>
                </s:if>
            </s:iterator>
        </s:if>
    
        <s:if test="studySummary.imagingStudy" >
	        <tr>
	            <th colspan="4">Imaging Data</th>
	        </tr>
            <s:iterator value="%{studySummary.imageDataSources}" status="imageDataSourceStatus">

            <tr>
                <td align="right">Hostname:</td>
                <td> <b><s:property value="hostName"/></b></td>
                <td />
                <td />
            </tr>
            
            <tr>
                <td align="right">Collection:</td>
                <td> <b><s:property value="collectionName"/></b></td>
                <td />
                <td />
            </tr>
            <tr>
                <td align="right">Number of Image Studies Mapped:</td>
                <td> <b><s:property value="numberImageStudies"/></b></td>
                <td />
                <td />
            </tr>
            <tr>
                <td align="right">Number of Image Series Mapped:</td>
                <td> <b><s:property value="numberImageSeries"/></b></td>
                <td />
                <td />
            </tr>
            <tr>
                <td align="right">Number of Images Mapped:</td>
                <td> <b><s:property value="numberImages"/></b></td>
                <td />
                <td />
            </tr>
            
            </s:iterator>
            
            <tr>
                <td align="right">Number of Annotation Columns:</td>
                <td> <b><s:property value="studySummary.numberImageSeriesAnnotationColumns"/> </b></td>
                <td />  
                <td />
            </tr>
        </s:if>
    </table>
    </s:if>
</div>

<div class="clear"><br /></div>
