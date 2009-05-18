<?xml version="1.0" encoding="UTF-8" ?>

<!--
$Id: junit-noframes.xsl,v 1.6 2006/07/04 21:47:01 jancumps Exp $
$Revision: 1.6 $
$Date: 2006/07/04 21:47:01 $
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0"
	xmlns:date="http://exslt.org/dates-and-times"
                extension-element-prefixes="date">

	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"
			font-size="10px">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="page"
					margin-top="2cm" margin-bottom="2cm" margin-left="2cm"
					margin-right="2cm">
					<fo:region-body margin-top="2cm"
						margin-bottom="3cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="page">
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template match="testsuites">
		<fo:block font-size="20px" text-align="center">
			Test results
		</fo:block>
		<fo:block text-align="center" padding-bottom="20px">
			<xsl:call-template name="today"/>
		</fo:block>
		<fo:block padding-top="10px" margin-bottom="3px"
			font-style="italic" font-size="13px">
			Summary:
		</fo:block>
		<fo:block>
			Total <xsl:value-of select="sum(testsuite/@tests)" />
			tests in <xsl:value-of select="count(testsuite)" /> classes.
		</fo:block>
		<fo:block>
			Total errors: <xsl:value-of select="sum(testsuite/@errors)"/>
		</fo:block>
		<fo:block>
			Total failures:	<xsl:value-of select="sum(testsuite/@failures)"/>
		</fo:block>
		<fo:block>
			Tests succeded:
			<xsl:value-of select="count(testsuite/testcase[not(error|failure)])"/>
		</fo:block>
		<fo:block>
			Total time: 
				<xsl:call-template name="display-time">
					<xsl:with-param name="value" select="sum(testsuite/@time)"/>
				</xsl:call-template>s
		</fo:block>

		<!-- ======================================================================== -->
		
		<xsl:call-template name="testcases">
			<xsl:with-param name="label">Errors</xsl:with-param>
			<xsl:with-param name="nodes" select="testsuite/testcase[error]"/>
		</xsl:call-template>
		<xsl:call-template name="testcases">
			<xsl:with-param name="label">Failures</xsl:with-param>
			<xsl:with-param name="nodes" select="testsuite/testcase[failure]"/>
		</xsl:call-template>
		<xsl:call-template name="testcases">
			<xsl:with-param name="label">Successes</xsl:with-param>
			<xsl:with-param name="nodes" select="testsuite/testcase[not(*)]"/>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="testcases">
		<xsl:param name="label"/>
		<xsl:param name="nodes"/>
		<xsl:if test="$nodes">
			<fo:block padding-top="10px" font-style="italic" font-size="13px">
				<xsl:value-of select="$label"/>
			</fo:block>
			<fo:list-block>
				<xsl:apply-templates select="$nodes" mode="list"/>
			</fo:list-block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="testcase" mode="list">
		<fo:list-item>
			<fo:list-item-label>
				<fo:block>*</fo:block>
			</fo:list-item-label>
			<fo:list-item-body margin-left="10px">
				<fo:block padding-bottom="5px">
					<xsl:apply-templates select="." mode="content" />
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>

	<xsl:template match="testcase[error]" mode="content">
		Test <xsl:call-template name="testname"/>
		finished with an error message
		<fo:inline font-weight="bold" color="red">
			<xsl:value-of select="error/@message" />
		</fo:inline>
		of type	<fo:inline font-weight="bold">
			<xsl:value-of select="error/@type" />
		</fo:inline>
	</xsl:template>

	<xsl:template match="testcase[failure]" mode="content">
		Test <xsl:call-template name="testname"/>
		failed with a message
		<fo:inline font-weight="bold" color="blue">
			<xsl:value-of select="failure/@message" />
		</fo:inline>
		of type	<fo:inline font-weight="bold">
			<xsl:value-of select="failure/@type"/>
		</fo:inline>
	</xsl:template>

	<xsl:template match="testcase[not(*)]" mode="content">
		Test <xsl:call-template name="testname"/>
		finished successfully in
		<xsl:call-template name="display-time">
			<xsl:with-param name="value" select="@time"/>
		</xsl:call-template>s.
	</xsl:template>

	<xsl:template name="testname">
		<xsl:value-of select="@name" /> 
		 (<fo:inline font-size="8px" >
			<xsl:value-of select="../@package"/>.<xsl:value-of select="../@name"/>
		</fo:inline>)
	</xsl:template>

	<xsl:template name="display-time">
	    <xsl:param name="value"/>
	    <xsl:value-of select="format-number($value,'0.000')"/>
	</xsl:template>

	<xsl:template name="today">
		<xsl:if test="function-available('date:year') and function-available('date:month-in-year')  and function-available('date:day-in-month') ">
			<xsl:value-of select="date:year()"/>-<xsl:value-of select="date:month-in-year() + 1"/>-<xsl:value-of select="date:day-in-month()"/>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>