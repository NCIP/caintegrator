<?xml version="1.0" encoding="UTF-8" ?>

<!--
 $Id: extra-documentation.xsl,v 1.2 2006/07/04 21:47:00 jancumps Exp $
 $Revision: 1.2 $
 $Date: 2006/07/04 21:47:00 $
-->

<!--
This file allows you to customize the contents of your pdf test report.
You can add a description for the test suite, and 
you can add a description for each test case.
The file itself is required, but the only required content is:
<xsl:template name="unittest.description"/>
-->

<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:stringutils="xalan://org.apache.tools.ant.util.StringUtils"
	version="1.0">

<!-- Test Suite Description -->
 <!-- 
 This section allows you to create a description for your test suite.
 This template is required, but can be empty
 -->
<xsl:template name="unittest.description">
</xsl:template>    

<xsl:template match="testsuite" mode="testcase.description">
</xsl:template>

<xsl:template name="signoff">
</xsl:template>


</xsl:stylesheet>
