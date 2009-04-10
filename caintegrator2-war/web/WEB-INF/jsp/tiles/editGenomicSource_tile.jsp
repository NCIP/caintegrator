<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_genomic_data_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->
    
    <script type="text/javascript">
        function CheckPlatformVendor(vendor) {
            if (vendor != "Agilent") {
                document.getElementById("platformName").disabled = true;
            } else {
                document.getElementById("platformName").disabled = false;
            }
        }
    </script>
    
    <h1>Edit Genomic Data Source</h1>
    <s:actionerror/>
    <s:form action="saveGenomicSource">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="genomicSource.id" />
        <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" />
        <s:textfield label="caArray server JNDI Port" name="genomicSource.serverProfile.port" />
        <!-- NOTE - using custom struts theme to turn off autocomplete -->
        <s:textfield label="caArrayUsername" name="genomicSource.serverProfile.username" theme="cai2xhtml" />
        <s:password label="caArrayPassword" name="genomicSource.serverProfile.password" theme="cai2xhtml"/>
        <!--/NOTE --> 
        <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" />
        <s:select id="platformVendor" name="genomicSource.platformVendor" label="Vendor"
            list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum@getValuesToDisplay()"
            onchange="CheckPlatformVendor(this.form.platformVendor.value);"/>
        <s:select id="platformName" name="genomicSource.platformName" label="Platform (only needed for Agilent)" list="agilentPlatformNames" />
        <s:submit value="Save" />
    </s:form>
    
    <script type="text/javascript">
        CheckPlatformVendor();
    </script>
            
</div>

<div class="clear"><br /></div>
