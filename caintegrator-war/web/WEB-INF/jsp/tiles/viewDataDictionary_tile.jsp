<%@ taglib prefix="s" uri="/struts-tags"%>

<script language="javascript">
    function togglePermissibleView(divId){
        var div = document.getElementById(divId);
        if (div.style.display == "none") {
            div.style.display="block";
        } else {
            div.style.display="none";
        }
    }
</script>

<div id="content">

    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('ngPTAg', 'id-1-GettingStartedwithcaIntegrator-DataDictionary')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->  
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <div class="form_wrapper_outer">
    <s:iterator value="study.sortedAnnotationGroups" status="iterator">
        <table class="form_wrapper_table">
                <tr>
                <td>
					<div class="wwgrp">
					<div class="wwlbl" style="width: 7em; text-align: left;padding: 0 5px 0 5px;">Group Name:</div>
					<div class="wwctrl" style="font-weight: bold;"><s:property value="name" /></div>
					</div>
				</td>
                </tr>
                <tr>
                <td>
                    <div class="wwgrp">
                    <div class="wwlbl" style="width: 7em; text-align: left;padding: 0 5px 0 5px;">Description:</div>
                    <div class="wwctrl" style="font-weight: bold;"><s:property value="description" /></div>
                    </div>
                </td>
                </tr>
                <tr>
                <td style="padding: 0 0 5px 0;"></td>
                </tr>
                <tr>
                    <td colspan="2" style="padding: 5px;">                         
                        <table class="data">
                            <tr>
                                <th>Annotation Field Descriptor</th>
                                <th>Source</th>
                                <th>Data&nbsp;Type</th>
                                <th>Description</th>
                                <th>caDSR&nbsp;ID</th>
                                <th>Permissible</th>
                                <th>Restrictions</th>
                            </tr>
                            <s:iterator value="sortedVisibleAnnotationFieldDescriptors">
                                <s:url id="viewDataElement" value="%{@gov.nih.nci.caintegrator.external.cadsr.CaDSRFacade@CDE_URL}" escapeAmp="false">
                                    <s:param name="cdeId" value="definition.commonDataElement.publicID"/>
                                    <s:param name="version" value="definition.commonDataElement.version"/>
                                </s:url>
                                <tr>
                                    <td><s:property value="displayName"/></td>
                                    <td><s:property value="annotationEntityType.value"/></td>
                                    <td><s:property value="definition.dataType.value"/></td>
                                    <td><s:property value="definition.commonDataElement.definition"/></td>
                                    <td><a href="<s:property value='%{viewDataElement}'/>" target="_blank">
                                        <s:property value="definition.commonDataElement.publicID"/></a></td>
                                    <td>
                                        <s:if test="!definition.permissibleValueCollection.empty">
                                            <s:set name="divId" value="%{displayName}"/>
                                            <a href="javascript:togglePermissibleView('${divId}')">Show/Hide</a>
                                            <div id="${divId}" style="display: none;">
                                                <s:iterator value="permissibleValues">
                                                    <div style="white-space:nowrap;"><s:property value="value"/></div>
                                                </s:iterator>
                                            </div>
                                        </s:if>
                                    </td>
                                    <td>
                                        <s:iterator value="displayableRestrictions">
                                            <div style="white-space:nowrap;"><s:property /> </div>
                                        </s:iterator>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>
        </table>
    </s:iterator>
    </div>
</div>

<div class="clear"><br /></div>
