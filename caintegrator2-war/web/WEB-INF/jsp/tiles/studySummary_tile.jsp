<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript" src="/caintegrator/common/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/caintegrator/common/js/jTruncate.js"></script>
<script type="text/javascript">

    jQuery.noConflict();

    jQuery().ready(function() {
        jQuery('#studyDescDiv').jTruncate();
    });

</script>

            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('study_summary_help')" class="help">
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
            <td colspan="3" class="wrap"><b><s:div id="studyDescDiv"><s:property value="studySummary.studyDescription" /></s:div></b></td>
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
        <s:if test="%{!anonymousUser}">
        <tr>
            <td align="right">Study Log: </td>
            <td>
                <s:url id="viewStudyLogUrl" includeParams="none" action="viewStudyLog" />
                <a href="${viewStudyLogUrl}">View</a>
            </td>
            <td />  
            <td />
        </tr>
        </s:if>    
        <tr>
            <th colspan="4">Subject Annotation Data</th>
        </tr>
        <tr>
            <td align="right">Last Modified:</td>
            <td> <b><s:property value="studySummary.subjectAnnotationsLastModifiedDate"/> </b> </td>
            <td />  
            <td />
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
            <td> Type: </td>
            <td> <b><s:property value="survivalValueType.value"/> </b></td>
        </tr>
        <tr>
            <td />
            <td />
            <td> Survival Length Units: </td>
            <td> <b><s:property value="survivalLengthUnits.value"/> </b></td>
        </tr>
        <s:if test="%{'By Date'.equals(survivalValueType.value)}">
        <tr>
            <td />
            <td />
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
        </s:if>
        <s:else>
        <tr>
            <td />
            <td />
            <td> Survival Length: </td>
            <td> <b><s:property value="survivalLength.displayName"/> </b></td>
        </tr>
        <s:if test="%{survivalStatus != null}">
        <tr>
            <td />
            <td />
            <td> Survival Status: </td>
            <td> <b><s:property value="survivalStatus.displayName"/> </b></td>
        </tr>
        <tr>
            <td />
            <td />
            <td> Value For Censored: </td>
            <td> <b><s:property value="valueForCensored"/> </b></td>
        </tr>
        </s:if>
        </s:else>
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
                <td align="right">Host type:</td>
                <td> <img src="/caintegrator/images/caArray.gif" border="none"/></td>
                <td />
                <td />
            </tr>            
            <tr>
                <td align="right">Experiment:</td>
                <td> 
                    Name: <b>
                        <s:if test="hasCaArrayUrl()">
                            <a href="${caArrayUrl}" target="cai2_CaArray" title="Click to go to caArray" style="text-decoration: underline; color: black;">
                                <s:property value="experimentName"/>
                            </a>
                        </s:if>
                        <s:else>
                            <s:property value="experimentName"/>
                        </s:else>
                        </b>
                    <br>
                    <s:iterator value="%{platforms}" status="platformStatus">
                        Array Platforms: <b><s:property value="name"/> </b>
                        Genome Build: <b><s:property value="genomeVersion.value"/> </b>
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
            
            <tr>
                <td align="right">Last Modified:</td>
                <td> <b><s:property value="displayableLastModifiedDate"/></b></td>
                <td />
                <td />
            </tr>
            <tr>
            <td align="right">Central Tendency for Technical Replicates:</td>
                <td> <b><s:property value="technicalReplicateCentralTendency"/></b></td>
                <td />
                <td />
            </tr>
            <s:if test="useHighVarianceCalculation">
            <tr>
                <td align="right"><s:property value="highVarianceThresholdLabel" /></td>
                <td> <b><s:property value="highVarianceThresholdString"/></b></td>
                <td />
                <td />
            </tr>
            </s:if>
            
                <s:if test="expressionData">
                    <s:url id="viewControlSamplesUrl" includeParams="none" action="viewControlSamples" >
                        <s:param name="genomicSource.id" value="genomicDataSourceConfiguration.id" />
                    </s:url>
                    
                    <tr>
                        <td align="right">Number of Samples: </td>
                        <td><b><s:property value="numberSamples" /> </b></td>
                        <td />
                        <td />
                    </tr>
                    <tr>
                        <td align="right">Control Samples Set: </td>
                        <td><b> <s:if test="%{numberControlSamples > 0}">
                            <a href="${viewControlSamplesUrl}">
                                <s:property value="numberControlSampleSetSamples" />
                            </a>
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
                <s:elseif test="snpData">
                    <tr>
                        <td align="right">SNP Samples: </td>
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
                <td align="right">Last Modified:</td>
                <td> <b><s:property value="displayableLastModifiedDate"/></b></td>
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
