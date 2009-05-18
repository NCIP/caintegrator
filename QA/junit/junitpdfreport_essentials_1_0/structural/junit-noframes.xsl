<?xml version="1.0" encoding="UTF-8" ?>

<!--
$Id: junit-noframes.xsl,v 1.9 2006/09/22 15:57:27 jancumps Exp $
$Revision: 1.9 $
$Date: 2006/09/22 15:57:27 $
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0"
	xmlns:date="http://exslt.org/dates-and-times"
                extension-element-prefixes="date">

	<xsl:key name="packages" match="/testsuites/testsuite" use="@package"/>
	
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
		<fo:block>
			<fo:table border-collapse="separate" inline-progression-dimension.maximum="100%" table-layout="fixed" width="100%">
				<fo:table-column column-width="proportional-column-width(1)"/>
				<fo:table-column column-width="55px" />
				<fo:table-column column-width="55px" />
				<fo:table-column column-width="55px" />
				<fo:table-column column-width="55px" />
				
				<fo:table-header>
					<fo:table-row font-weight="bold">
						<fo:table-cell border-style="solid"><fo:block>Name</fo:block></fo:table-cell>
						<fo:table-cell border-style="solid" text-align="center"><fo:block>Successes</fo:block></fo:table-cell>
						<fo:table-cell border-style="solid" text-align="center"><fo:block>Errors</fo:block></fo:table-cell>
						<fo:table-cell border-style="solid" text-align="center"><fo:block>Failures</fo:block></fo:table-cell>
						<fo:table-cell border-style="solid" text-align="center"><fo:block>Time</fo:block></fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-style="solid" border-top-width="3px">
							<fo:block>Total</fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" text-align="center" border-top-width="3px">
							<fo:block><xsl:value-of select="count(testsuite/testcase[not(*)])"/></fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" text-align="center" border-top-width="3px">
							<fo:block><xsl:value-of select="sum(testsuite/@errors)"/></fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" text-align="center" border-top-width="3px">
							<fo:block><xsl:value-of select="sum(testsuite/@failures)"/></fo:block>
						</fo:table-cell>
						<fo:table-cell border-style="solid" text-align="center" border-top-width="3px">
							<fo:block>
								<xsl:call-template name="display-time">
									<xsl:with-param name="value" select="sum(testsuite/@time)"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<xsl:for-each select="testsuite[count(.|key('packages',@package)[1])=1]">						
						<fo:table-row>
							<fo:table-cell border-style="solid" border-top-width="3px">
								<fo:block><xsl:value-of select="@package"/></fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" text-align="center" border-top-width="3px">
								<fo:block><xsl:value-of select="count(key('packages',@package)/testcase[not(*)])"/></fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" text-align="center" border-top-width="3px">
								<fo:block><xsl:value-of select="sum(key('packages',@package)/@errors)"/></fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" text-align="center" border-top-width="3px">
								<fo:block><xsl:value-of select="sum(key('packages',@package)/@failures)"/></fo:block>
							</fo:table-cell>
							<fo:table-cell border-style="solid" border-top-width="3px" text-align="center">
								<fo:block>
									<xsl:call-template name="display-time">
										<xsl:with-param name="value" select="sum(key('packages',@package)/@time)"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:variable name="current" select="@package"/>
						<xsl:apply-templates select="../testsuite[@package=$current]" mode="table-row"/>
					</xsl:for-each>		
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>

	<xsl:template match="testsuite" mode="table-row">
		<fo:table-row>
			<fo:table-cell border-style="solid">
				<fo:block margin-left="20px">
					<xsl:value-of select="@name"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" text-align="center">
				<fo:block><xsl:value-of select="count(testcase[not(*)])"/></fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" text-align="center">
				<fo:block><xsl:value-of select="@errors"/></fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" text-align="center">
				<fo:block><xsl:value-of select="@failures"/></fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" text-align="center">
				<fo:block>
					<xsl:call-template name="display-time">
						<xsl:with-param name="value" select="@time"/>
					</xsl:call-template>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<xsl:apply-templates select="testcase" mode="table-row"/>
	</xsl:template>
	
	<xsl:template match="testcase" mode="table-row">
		<fo:table-row>
			<fo:table-cell border-style="solid">
				<fo:block margin-left="50px">
					<xsl:value-of select="@name"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" background-color="white" text-align="center">
				<xsl:if test="not(*)"><xsl:attribute name="background-color">green</xsl:attribute></xsl:if>
				<fo:block><xsl:value-of select="1-count(*)"/></fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" background-color="white" text-align="center">
				<xsl:if test="error"><xsl:attribute name="background-color">red</xsl:attribute></xsl:if>
				<fo:block><xsl:value-of select="count(error)"/></fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" background-color="white" text-align="center">
				<xsl:if test="failure"><xsl:attribute name="background-color">blue</xsl:attribute></xsl:if>
				<fo:block><xsl:value-of select="count(failure)"/></fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" background-color="white" text-align="center">
				<fo:block>
					<xsl:call-template name="display-time">
						<xsl:with-param name="value" select="@time"/>
					</xsl:call-template>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	
	<xsl:template name="display-time">
		<xsl:param name="value" />
		<xsl:value-of select="format-number($value,'0.000')" />s
	</xsl:template>

	<xsl:template name="today">
		<xsl:if test="function-available('date:year') and function-available('date:month-in-year')  and function-available('date:day-in-month') ">
			<xsl:value-of select="date:year()"/>-<xsl:value-of select="date:month-in-year() + 1"/>-<xsl:value-of select="date:day-in-month()"/>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>