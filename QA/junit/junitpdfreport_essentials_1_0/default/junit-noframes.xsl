<?xml version="1.0" encoding="UTF-8" ?>

<!--
 $Id: junit-noframes.xsl,v 1.11 2006/10/02 20:57:27 jancumps Exp $
 $Revision: 1.11 $
 $Date: 2006/10/02 20:57:27 $
-->

<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:stringutils="xalan://org.apache.tools.ant.util.StringUtils"
	version="1.0">

<!-- junitreport aggregated results layout:

     <testsuites>          the aggregated result of all junit testfiles
        <testsuite>        the output from a single TestSuite
          <properties>     the defined properties at test execution
            <property>     name/value pair for a single property
          <testcase>       the results from executing a test method
          <system-out>     data written to System.out during the test run
          <system-err>     data written to System.err during the test run
      
     Considerations:
     A package can appear in multiple testcase instances. 
     To correctly calculate package statistics, data from all instances for 
     that package have to be summarized.   
-->        
        
<!-- allow customization of the report by providing hooks for test, package and testsuite description -->        
<xsl:import href="./extra-documentation.xsl"/>

<!-- the root of the document.
     xpath: /testsuites
     This template is called when the transormation starts, 
     and drives the report buildup.
     Page flow is defined:
     - define header and footer (layout-master-set)
     - create front page:
       . summary (resolved by template summary)
       . test description (resolved in extra-documentation,
         template unittest.description)
       . signoff area (resolved in extra-documentation, template signoff)
     - create packages overview:
       . packagelist, overview of all packages covered in 
         the input document (resolved by packagelist)       
       . packages, overview of all testsuites grouped 
         per package (resolved by packages)
     - create details
       . testcase list, overview of all testcases covered in
         the input document, grouped by package and testsuite
         (resolved by classes)
-->
<xsl:template match="testsuites">
  <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <fo:layout-master-set>
      <fo:simple-page-master master-name="simpleA4" page-height="27.937cm" page-width="21.587cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
        <fo:region-body margin-top="2cm" margin-bottom="3cm"/>
        <fo:region-before extent="3cm"/>
        <fo:region-after extent="2cm"/>
      </fo:simple-page-master>
    </fo:layout-master-set>

  <fo:page-sequence master-reference="simpleA4">
    <xsl:call-template name="page-layout"/>
    <fo:flow flow-name="xsl-region-body">
      <xsl:call-template name="summary"/>
      <xsl:call-template name="unittest.description"/>
      <xsl:call-template name="signoff"/>
    </fo:flow>
  </fo:page-sequence>

  <fo:page-sequence master-reference="simpleA4">
    <xsl:call-template name="page-layout"/>
    <fo:flow flow-name="xsl-region-body">
       <xsl:call-template name="packagelist"/>
       <xsl:call-template name="packages"/>
    </fo:flow>
  </fo:page-sequence>
                        
  <xsl:call-template name="classes"/>
  
 </fo:root>
</xsl:template>
    

<!-- Generate a summary calculated from all testcases covered in
     the input document.
     xpath: /testsuites
     Summary flow:
     - define the heading
     - calculate the summary as a summation of the results from all 
       testsuites and testcases
     - draw the summary table
     - select green/red color
-->     
<xsl:template name="summary">
  <fo:block space-before.optimum="3pt" space-after.optimum="15pt">
    Summary
  </fo:block>

  <fo:block font-size="10pt" font-family="Times">

    <xsl:variable name="testCount" select="sum(testsuite/@tests)"/>
    <xsl:variable name="errorCount" select="sum(testsuite/@errors)"/>
    <xsl:variable name="failureCount" select="sum(testsuite/@failures)"/>
    <xsl:variable name="timeCount" select="sum(testsuite/@time)"/>
    <xsl:variable name="successRate" select="($testCount - $failureCount - $errorCount) div $testCount"/>

    <fo:table border-collapse="separate" inline-progression-dimension.maximum="100%" inline-progression-dimension="17.5cm" table-layout="fixed" background-color="lightgray">
      <fo:table-column column-width="6.5cm"/>
      <fo:table-column column-width="3cm"/>
      <fo:table-column column-width="3cm"/>
      <fo:table-column column-width="3cm"/>
      <fo:table-column column-width="2cm"/>

      <fo:table-body>
        <fo:table-row  background-color="lightblue">
          <fo:table-cell >
            <fo:block text-align="left">
              Tests
            </fo:block>
          </fo:table-cell>
          <fo:table-cell >
            <fo:block text-align="left">
              Failures
            </fo:block>
          </fo:table-cell>
          <fo:table-cell >
            <fo:block text-align="left">
              Errors
            </fo:block>
          </fo:table-cell>
          <fo:table-cell >
            <fo:block text-align="left">
              Success rate
            </fo:block>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block text-align="left">
              Time
            </fo:block>
          </fo:table-cell>
        </fo:table-row>

        <fo:table-row font-size="8pt" color="green">
          <xsl:if test="($errorCount+$failureCount) &gt; 0"><xsl:attribute name="color">red</xsl:attribute></xsl:if>
          <xsl:call-template name="summary.line">
            <xsl:with-param name="testCount" select="$testCount"/>
            <xsl:with-param name="errorCount" select="$errorCount"/>
            <xsl:with-param name="failureCount" select="$failureCount"/>
            <xsl:with-param name="timeCount" select="$timeCount"/>
            <xsl:with-param name="successRate" select="$successRate"/>
          </xsl:call-template>
        </fo:table-row>
              
      </fo:table-body>
    </fo:table>
  </fo:block>

  <fo:block space-before.optimum="3pt" space-after.optimum="15pt" font-size="8pt" >
    Note: <fo:inline font-style="italic">failures</fo:inline> are anticipated 
    and checked for with assertions while <fo:inline font-style="italic">errors</fo:inline>  
    are unanticipated.
  </fo:block>
</xsl:template>


<xsl:template name="summary.line">
  <xsl:param name="testCount"/>
  <xsl:param name="errorCount"/>
  <xsl:param name="failureCount"/>
  <xsl:param name="timeCount"/>
  <xsl:param name="successRate"/>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:value-of select="$testCount"/>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:value-of select="$failureCount"/>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:value-of select="$errorCount"/>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:call-template name="display-percent">
        <xsl:with-param name="value" select="$successRate"/>
      </xsl:call-template>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell>
    <fo:block text-align="left">
      <xsl:call-template name="display-time">
        <xsl:with-param name="value" select="$timeCount"/>
      </xsl:call-template>
    </fo:block>
  </fo:table-cell>
</xsl:template>

<xsl:template name="testsuite.test.header">
  <fo:table-row background-color="lightblue">
    <fo:table-cell >
      <fo:block text-align="left">
        Name
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        Tests
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        Errors
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        Failures
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        Time(s)
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        Time Stamp
      </fo:block>
    </fo:table-cell>
  </fo:table-row>
</xsl:template>

<xsl:template name="testcase.test.header">
  <fo:table-row background-color="lightblue">
    <fo:table-cell >
      <fo:block text-align="left">
        Name
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        Status
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        Type
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        Time(s)
      </fo:block>
    </fo:table-cell>
  </fo:table-row>
</xsl:template>

<xsl:template name="packagelist">
  <fo:block space-before.optimum="3pt" space-after.optimum="15pt">
    Packages
  </fo:block>

  <fo:block space-before.optimum="3pt" space-after.optimum="15pt" font-size="8pt" >
    Note: package statistics are not computed recursively, they only sum up all of its testsuites numbers.
  </fo:block>
  
  <fo:block font-size="10pt" font-family="Times">

    <fo:table border-collapse="separate" inline-progression-dimension.maximum="100%" inline-progression-dimension="17.5cm" table-layout="fixed" background-color="lightgray">
      <fo:table-column column-width="9.4cm"/>
      <fo:table-column column-width="1.2cm"/>
      <fo:table-column column-width="1.2cm"/>
      <fo:table-column column-width="1.5cm"/>
      <fo:table-column column-width="1.5cm"/>
      <fo:table-column column-width="2.7cm"/>
      <fo:table-body>
        <xsl:call-template name="testsuite.test.header"/>
      
        <!-- only visit a package once when it has multiple testsuites -->
        <xsl:for-each select="./testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
          <xsl:sort select="@package"/>
          <xsl:variable name="testsuites-in-package" select="/testsuites/testsuite[./@package = current()/@package]"/>
          <xsl:variable name="testCount" select="sum($testsuites-in-package/@tests)"/>
          <xsl:variable name="errorCount" select="sum($testsuites-in-package/@errors)"/>
          <xsl:variable name="failureCount" select="sum($testsuites-in-package/@failures)"/>
          <xsl:variable name="timeCount" select="sum($testsuites-in-package/@time)"/>

          <fo:table-row font-size="8pt" color="green">
            <xsl:if test="($errorCount+$failureCount) &gt; 0"><xsl:attribute name="color">red</xsl:attribute></xsl:if>
            <xsl:call-template name="testsuite.packagelist.line">
              <xsl:with-param name="package" select="@package"/>
              <xsl:with-param name="testCount" select="$testCount"/>
              <xsl:with-param name="errorCount" select="$errorCount"/>
              <xsl:with-param name="failureCount" select="$failureCount"/>
              <xsl:with-param name="timeCount" select="$timeCount"/>
              <xsl:with-param name="timeStamp" select="@timestamp"/>
            </xsl:call-template>
          </fo:table-row>
          
        </xsl:for-each>
      </fo:table-body>
    </fo:table>
  </fo:block>
</xsl:template>

<xsl:template name="testsuite.packagelist.line">
  <xsl:param name="package"/>
  <xsl:param name="testCount"/>
  <xsl:param name="errorCount"/>
  <xsl:param name="failureCount"/>
  <xsl:param name="timeCount"/>
  <xsl:param name="timeStamp"/>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:value-of select="$package"/>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:value-of select="$testCount"/>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:value-of select="$errorCount"/>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:value-of select="$failureCount"/>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:call-template name="display-time">
        <xsl:with-param name="value" select="$timeCount"/>
      </xsl:call-template>
    </fo:block>
  </fo:table-cell>
  <fo:table-cell >
    <fo:block text-align="left">
      <xsl:call-template name="display-timestamp">
        <xsl:with-param name="value" select="$timeStamp"/>
      </xsl:call-template>
    </fo:block>
  </fo:table-cell>
</xsl:template>


  <xsl:template name="packages">
    <xsl:for-each select="/testsuites/testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
      <xsl:sort select="@package"/>
        <fo:block space-before.optimum="15pt" space-after.optimum="15pt" font-size="11pt">
          Package <xsl:value-of select="@package"/>
        </fo:block>
 
        <fo:block font-size="10pt" font-family="Times">

          <fo:table border-collapse="separate" inline-progression-dimension.maximum="100%" inline-progression-dimension="17.5cm" table-layout="fixed" background-color="lightgray">
            <fo:table-column column-width="9.4cm"/>
            <fo:table-column column-width="1.2cm"/>
            <fo:table-column column-width="1.2cm"/>
            <fo:table-column column-width="1.5cm"/>
            <fo:table-column column-width="1.5cm"/>
            <fo:table-column column-width="2.7cm"/>
            <fo:table-body>
            <xsl:call-template name="testsuite.test.header"/>
            <xsl:apply-templates select="/testsuites/testsuite[./@package = current()/@package]" mode="print.test"/> 
            </fo:table-body>
          </fo:table>
        </fo:block>
    </xsl:for-each>
  </xsl:template>

<!-- package information -->
<xsl:template match="testsuite" mode="print.test">

  <fo:table-row font-size="8pt" color="green">
    <xsl:if test="@failures[.&gt; 0] | @errors[.&gt; 0]"><xsl:attribute name="color">red</xsl:attribute></xsl:if>
    <fo:table-cell >
      <fo:block text-align="left">
        <xsl:value-of select="@name"/>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        <xsl:value-of select="@tests"/>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        <xsl:value-of select="@errors"/>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        <xsl:value-of select="@failures"/>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        <xsl:call-template name="display-time">
          <xsl:with-param name="value" select="@time"/>
        </xsl:call-template>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        <xsl:call-template name="display-timestamp">
          <xsl:with-param name="value" select="@timestamp"/>
        </xsl:call-template>
      </fo:block>
    </fo:table-cell>
  </fo:table-row>

</xsl:template>

<xsl:template name="classes">
  <xsl:for-each select="testsuite">
    <xsl:sort select="@name"/>
    <fo:page-sequence master-reference="simpleA4">
      <xsl:call-template name="page-layout"/>
      <fo:flow flow-name="xsl-region-body">

        <fo:block space-before.optimum="3pt" space-after.optimum="15pt">
          TestCase <xsl:value-of select="@name"/>
        </fo:block>
  
        <fo:block font-size="10pt" font-family="Times">

          <fo:table border-collapse="separate" inline-progression-dimension.maximum="100%" inline-progression-dimension="17.5cm" table-layout="fixed" background-color="lightgray">
            <fo:table-column column-width="7.0cm"/>
            <fo:table-column column-width="1.0cm"/>
            <fo:table-column column-width="8.0cm"/>
            <fo:table-column column-width="1.5cm"/>
            <fo:table-body>
              <xsl:call-template name="testcase.test.header"/>
              <xsl:apply-templates select="./testcase" mode="print.test"/>
            </fo:table-body>
          </fo:table>
        </fo:block>
          <xsl:apply-templates select="." mode="testcase.description"/>
      </fo:flow>
    </fo:page-sequence> 

    <xsl:apply-templates select="system-out">
        <xsl:with-param name="title">Log </xsl:with-param>
    </xsl:apply-templates>
    
    <xsl:apply-templates select="system-err">
        <xsl:with-param name="title">Error log </xsl:with-param>
    </xsl:apply-templates>
    
  </xsl:for-each>
</xsl:template>

<!-- Ignore empty output -->
<xsl:template match="system-out[string-length(.)=0]|system-err[string-length(.)=0]"/>

<xsl:template match="system-out|system-err">
   <xsl:param name="title">Log</xsl:param>
   <xsl:variable name="space"> </xsl:variable>
   <fo:page-sequence master-reference="simpleA4">
     <xsl:call-template name="page-layout"/>
     <fo:flow flow-name="xsl-region-body">
       <fo:block font-size="11pt" space-before.optimum="20pt">
      	<xsl:value-of select="$title"/> <xsl:value-of select="../@name"/>
       </fo:block>
       <fo:block linefeed-treatment="preserve" white-space-collapse="false" space-before.optimum="20pt" space-after.optimum="15pt" 
                 font-size="6pt" font-family="Courier" >
         <!--xsl:value-of select="."/-->
         <xsl:call-template name="text_wrapper"> 
           <xsl:with-param name="Text" select="."/>
           <xsl:with-param name="Length" select="30000"/>
         </xsl:call-template>
       </fo:block>
     </fo:flow>
   </fo:page-sequence> 
</xsl:template>

<!-- class information -->
<xsl:template match="testcase" mode="print.test">

  <fo:table-row font-size="8pt" color="green">
    <xsl:if test="failure | error"><xsl:attribute name="color">red</xsl:attribute></xsl:if>
    <fo:table-cell >
      <fo:block text-align="left">
        <!-- if neccessary, the package name will be cut from the testname -->
        <xsl:call-template name="display-testname">
          <xsl:with-param name="testname" select="@name"/>
        </xsl:call-template>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        <xsl:choose>
          <xsl:when test="failure"><xsl:text>Failure</xsl:text></xsl:when>
          <xsl:when test="error"><xsl:text>Error</xsl:text></xsl:when>
          <xsl:otherwise><xsl:text>Success</xsl:text></xsl:otherwise>
        </xsl:choose>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell border-right-width="0.5pt">
      <fo:block language="en" linefeed-treatment="preserve"
        white-space-collapse="false" 
        hyphenate="true"
        text-align="left">
        <xsl:choose>
          <xsl:when test="failure">
          <xsl:apply-templates select="failure"/>          
          </xsl:when>
          <xsl:when test="error">
          <xsl:apply-templates select="error"/>
          </xsl:when>
        </xsl:choose>
      </fo:block>
    </fo:table-cell>
    <fo:table-cell >
      <fo:block text-align="left">
        <xsl:call-template name="display-time">
        <xsl:with-param name="value" select="@time"/>
      </xsl:call-template>
      </fo:block>
    </fo:table-cell>
  </fo:table-row>

</xsl:template>
<!-- Style for the error and failure in the tescase template -->
<xsl:template match="failure | error">
	<xsl:apply-templates select="." mode="message"/>
    <!--  display the stacktrace -->
    <fo:block-container overflow="hidden" extent="100%">
        <fo:block linefeed-treatment="treat-as-space"
              white-space-collapse="true" space-before.optimum="5pt"
              space-after.optimum="2pt" font-size="6pt" font-family="Courier">
        <xsl:value-of select="."/>
        </fo:block>
    </fo:block-container>
    
    <fo:block/>
</xsl:template>

<xsl:template match="failure[@message]|error[@message]" mode="message">
    <xsl:value-of select="@message"/>
</xsl:template>

<xsl:template match="failure|error" mode="message">
	N/A
</xsl:template>

<xsl:template name="display-time">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.000')"/>
</xsl:template>

<xsl:template name="display-percent">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.00%')"/>
</xsl:template>

<xsl:template name="display-timestamp">
    <xsl:param name="value"/>
    <xsl:value-of select="$value"/>
</xsl:template>

<!--
    Remove the package name from the tests (if any).
    This is done by ommitting any text in the testname before the last dot.
-->
<xsl:template name="display-testname">
    <xsl:param name="testname"/>
    <xsl:variable name="part" select="substring-after($testname,'.')"/>
    <xsl:choose>
      <xsl:when test="string-length($part)">
        <xsl:call-template name="display-testname">
          <xsl:with-param name="testname" select="$part"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$testname"/>
      </xsl:otherwise>
    </xsl:choose>
    
</xsl:template>

<xsl:template name="page-layout">
  <fo:static-content flow-name="xsl-region-after">
    <xsl:call-template name="page-footer"/>
  </fo:static-content>
  <fo:static-content flow-name="xsl-region-before">
    <xsl:call-template name="page-header"/>
  </fo:static-content>
</xsl:template>
    

<xsl:template name="page-header">
  <fo:block font-size="10pt" text-align="start" font-weight="bold"
            border-style="solid" border-color="blue" border-width="0.5pt">
    <fo:table border-collapse="separate" inline-progression-dimension.maximum="100%" inline-progression-dimension="14cm" table-layout="fixed">
      <fo:table-column  column-width="7.0cm"/>
      <fo:table-column  column-width="7.0cm"/>
      <fo:table-body>
        <fo:table-row>
          <fo:table-cell >
            <fo:block text-align="left" space-before.optimum="3pt">
              Unit Test Results
            </fo:block>
          </fo:table-cell>
          <fo:table-cell >
            <fo:block text-align="right" space-before.optimum="3pt">
              <xsl:for-each select="/testsuites/testsuite[1]/properties/property">
                <xsl:variable name="propname" select="@name"/>
                <xsl:variable name="propvalue" select="@value"/>
                <xsl:choose>
                  <xsl:when test="$propname = 'ant.project.name'">
                    <xsl:value-of select="$propvalue"/>
                  </xsl:when>
                </xsl:choose>
              </xsl:for-each>
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-body>
    </fo:table>
  </fo:block>
</xsl:template>
    
<xsl:template name="page-footer">
  <fo:block font-size="10pt" text-align="start"
            border-style="solid" border-color="blue" border-width="0.5pt">
    <fo:block text-align="left" space-before.optimum="3pt">
      Page <fo:page-number/>
    </fo:block>
  </fo:block>
</xsl:template>
    
<xsl:template name="text_wrapper">
  <xsl:param name="Text"/>
  <xsl:param name="Length"/>
  <fo:block>
  <xsl:value-of select="substring($Text,1,$Length)"/>
  </fo:block>
  <xsl:if test="string-length($Text) &gt; $Length">
    <xsl:call-template name="text_wrapper">
      <xsl:with-param name="Text" select="substring($Text,($Length) + 1)"/>
      <xsl:with-param name="Length" select="$Length"/>
    </xsl:call-template>
  </xsl:if>
</xsl:template>

</xsl:stylesheet>
