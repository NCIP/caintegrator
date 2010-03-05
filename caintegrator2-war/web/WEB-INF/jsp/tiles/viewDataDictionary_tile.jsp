<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <div class="form_wrapper_outer">
        <table class="form_wrapper_table">
            <s:iterator value="study.sortedAnnotationGroups" >
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
                                <th>Type</th>
                                <th>Permissible</th>
                            </tr>
                            <s:iterator value="sortedVisibleAnnotationFieldDescriptors">
                                <tr>
                                    <td><s:property value="name"/></td>
                                    <td><s:property value="annotationEntityType.value"/></td>
                                    <td><s:property value="type.value"/></td>
                                    <td><s:property value="hasPermissibleValues"/></td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
    
    <!--Buttons-->

    <s:div theme="xhtml">
        <ul class="btnrow">
            <li><s:a href="/caintegrator2/index.jsp" cssClass="btn">
                <span class="btn_img">Cancel</span>
            </s:a></li>
        </ul>
    </s:div>
    <!--/Buttons-->
</div>

<div class="clear"><br /></div>
