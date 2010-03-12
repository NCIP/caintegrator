<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
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
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <div class="form_wrapper_outer">
        <table class="form_wrapper_table">
            <s:iterator value="study.sortedAnnotationGroups" status="iterator">
                <tr>
                    <th class="title" style="height: 2.5em;">Group: <font size="-1"><s:property value="name" /></font></th>
                    <th>Description: <font size="-1"><s:property value="description" /></font></th>
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
                                <th>caDSR&nbsp;Name</th>
                                <th>Permissible</th>
                            </tr>
                            <s:iterator value="sortedVisibleAnnotationFieldDescriptors">
                                <s:url id="viewDataElement" value="%{@gov.nih.nci.caintegrator2.external.cadsr.CaDSRFacade@CDE_URL}" escapeAmp="false">
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
                                    <td><s:property value="definition.commonDataElement.valueDomain.longName"/></td>
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
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
</div>

<div class="clear"><br /></div>
