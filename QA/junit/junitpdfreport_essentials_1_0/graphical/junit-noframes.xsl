<?xml version="1.0" encoding="UTF-8" ?>

<!--
$Id$
$Revision$
$Date$
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
        xmlns:stringutils="xalan://org.apache.tools.ant.util.StringUtils" version="1.0">

  <xsl:variable name="progress_table_width">8cm</xsl:variable>
  <xsl:variable name="text_table_width">10cm</xsl:variable>
        
  <xsl:template match="/">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="27.937cm" page-width="21.587cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">	
        <fo:region-body margin-top="2cm"
                        margin-bottom="3cm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>

      <fo:page-sequence master-reference="simpleA4">
              <fo:flow flow-name="xsl-region-body">
                      <xsl:apply-templates select="/testsuites" mode="summary"/>
                      <xsl:apply-templates select="/testsuites" mode="packagelist"/>
                      <xsl:apply-templates select="/testsuites" mode="packages"/>
              </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>


  <xsl:template match="/testsuites" mode="summary">
    <xsl:variable name="testCount" select="sum(testsuite/@tests)"/>
    <xsl:variable name="errorCount" select="sum(testsuite/@errors)"/>
    <xsl:variable name="failureCount" select="sum(testsuite/@failures)"/>
    <xsl:variable name="successRate" select="($testCount - $failureCount - $errorCount) div $testCount"/>
    <xsl:variable name="timeCount" select="sum(testsuite/@time)"/>

    <fo:block>
      Summary
      <fo:table border-collapse="separate" 
      inline-progression-dimension.maximum="100%" 
      width="{$progress_table_width} * 2" table-layout="fixed">
        <fo:table-column column-width="{$progress_table_width}"/>
        <fo:table-column column-width="0.5cm"/>
        <fo:table-column column-width="{$text_table_width}"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell>
              <!-- draw progress bar for summary -->
              <xsl:call-template name="progressbar">
                <xsl:with-param name="successRate" select="$successRate"/>
              </xsl:call-template>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block/>
            </fo:table-cell>
            <fo:table-cell>
              <fo:block>
                <xsl:call-template name="text">
                  <xsl:with-param name="testCount" select="$testCount"/>
                  <xsl:with-param name="errorCount" select="$errorCount"/>
                  <xsl:with-param name="failureCount" select="$failureCount"/>
                  <xsl:with-param name="successRate" select="$successRate"/>
                  <xsl:with-param name="timeCount" select="$timeCount"/>
                </xsl:call-template>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
     
    </fo:block>
  </xsl:template>

  <xsl:template match="/testsuites" mode="packagelist">
    <fo:block space-before.optimum="9pt">
      Packages
    </fo:block>


    <xsl:for-each select="./testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
      <xsl:sort select="@package"/>
      <xsl:variable name="testsuites-in-package" select="/testsuites/testsuite[./@package = current()/@package]"/>
      <xsl:variable name="testCount" select="sum($testsuites-in-package/@tests)"/>
      <xsl:variable name="errorCount" select="sum($testsuites-in-package/@errors)"/>
      <xsl:variable name="failureCount" select="sum($testsuites-in-package/@failures)"/>
      <xsl:variable name="successRate" select="($testCount - $failureCount - $errorCount) div $testCount"/>
      <xsl:variable name="timeCount" select="sum($testsuites-in-package/@time)"/>

      <fo:block space-before.optimum="6pt" font-size="10pt">
        <xsl:value-of select="@package"/>
        <fo:table border-collapse="separate" 
        inline-progression-dimension.maximum="100%" 
        width="{$progress_table_width} * 2" table-layout="fixed">
          <fo:table-column column-width="{$progress_table_width}"/>
          <fo:table-column column-width="0.5cm"/>
          <fo:table-column column-width="{$text_table_width}"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell>
                  <!-- draw progress bar for package -->
                  <xsl:call-template name="progressbar">
                    <xsl:with-param name="successRate" select="$successRate"/>
                  </xsl:call-template>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block/>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block>
                  <xsl:call-template name="text">
                    <xsl:with-param name="testCount" select="$testCount"/>
                    <xsl:with-param name="errorCount" select="$errorCount"/>
                    <xsl:with-param name="failureCount" select="$failureCount"/>
                    <xsl:with-param name="successRate" select="$successRate"/>
                    <xsl:with-param name="timeCount" select="$timeCount"/>
                  </xsl:call-template>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
      </fo:block>      
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="/testsuites" mode="packages">
    <fo:block space-before.optimum="9pt">
      Tests
    </fo:block>


    <xsl:for-each select="./testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
      <xsl:sort select="@package"/>
      <fo:block space-before.optimum="9pt" font-size="10pt">
        Package <xsl:value-of select="@package"/>
        <xsl:apply-templates select="/testsuites/testsuite[./@package = current()/@package]" mode="print.test"/>
      </fo:block>
    </xsl:for-each>
   </xsl:template>
   
   <xsl:template match="testsuite" mode="print.test">
      <xsl:variable name="testCount" select="sum(@tests)"/>
      <xsl:variable name="errorCount" select="sum(@errors)"/>
      <xsl:variable name="failureCount" select="sum(@failures)"/>
      <xsl:variable name="successRate" select="($testCount - $failureCount - $errorCount) div $testCount"/>
      <xsl:variable name="timeCount" select="sum(@time)"/>

      <fo:block space-before.optimum="3pt" font-size="8pt">
        <xsl:value-of select="@name"/>
        <fo:table border-collapse="separate" 
        inline-progression-dimension.maximum="100%" 
        width="{$progress_table_width} * 2" table-layout="fixed">
          <fo:table-column column-width="{$progress_table_width}"/>
          <fo:table-column column-width="0.5cm"/>
          <fo:table-column column-width="{$text_table_width}"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell>
                <!-- draw progress bar for test -->
                <xsl:call-template name="progressbar">
                  <xsl:with-param name="successRate" select="$successRate"/>
                </xsl:call-template>
             </fo:table-cell>
              <fo:table-cell>
                <fo:block/>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block>
                  <xsl:call-template name="text">
                    <xsl:with-param name="testCount" select="$testCount"/>
                    <xsl:with-param name="errorCount" select="$errorCount"/>
                    <xsl:with-param name="failureCount" select="$failureCount"/>
                    <xsl:with-param name="successRate" select="$successRate"/>
                    <xsl:with-param name="timeCount" select="$timeCount"/>
                  </xsl:call-template>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
       </fo:block>
   </xsl:template>
           
  <!-- the progressbar drawing template -->  
  <xsl:template name="progressbar">
    <xsl:param name="successRate"/>
   
    <!-- does not work with fop 0.2x 
    <fo:table border-collapse="separate" 
      inline-progression-dimension.maximum="100%" 
      width="50%" table-layout="fixed">
    -->
    <fo:table border-collapse="separate" 
      inline-progression-dimension.maximum="100%" 
      width="{$progress_table_width}" table-layout="fixed"> 
      
      <xsl:choose>
        <xsl:when test="$successRate &gt; 0">
          <fo:table-column column-width="from-parent('width') * {$successRate}"/>
        </xsl:when>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="$successRate &lt; 1">
          <fo:table-column column-width="from-parent('width') * (1 - {$successRate})"/>
        </xsl:when>
      </xsl:choose>
      
      <fo:table-body>
        <fo:table-row height="6pt">
          <xsl:choose>
            <xsl:when test="$successRate &gt; 0">
              <fo:table-cell border-style="solid" background-color="green">
                <fo:block/>
              </fo:table-cell>    
            </xsl:when>
          </xsl:choose>
          <xsl:choose>
            <xsl:when test="$successRate &lt; 1">
              <fo:table-cell border-style="solid" background-color="red">
                <fo:block/>
              </fo:table-cell>    
            </xsl:when>
          </xsl:choose>
        </fo:table-row>
      </fo:table-body>
    </fo:table>
  
  </xsl:template>

  <!-- the text template -->  
  <xsl:template name="text">
    <xsl:param name="testCount"/>
    <xsl:param name="errorCount"/>
    <xsl:param name="failureCount"/>
    <xsl:param name="successRate"/>
    <xsl:param name="timeCount"/>
    <fo:table border-collapse="separate" 
      inline-progression-dimension.maximum="100%" 
      width="{$progress_table_width}" table-layout="fixed"> 
      <xsl:attribute name="font-size">7pt</xsl:attribute>
      <xsl:attribute name="font-family">Times</xsl:attribute>
      <fo:table-column column-width="1.5cm"/>
      <fo:table-column column-width="1.5cm"/>
      <fo:table-column column-width="1.5cm"/>
      <fo:table-column column-width="3.0cm"/>
      <fo:table-column column-width="2.5cm"/>
      <fo:table-body>
        <fo:table-row>
          <fo:table-cell>
            <fo:block>
              Tests: <xsl:value-of select="($testCount)"/>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block>
              Errors: <xsl:value-of select="$errorCount"/>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block>
              Failures: <xsl:value-of select="$failureCount"/>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block>
              Success rate: <xsl:call-template name="display-percent">
                <xsl:with-param name="value" select="$successRate"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block>
              Time: <xsl:call-template name="display-time">
                <xsl:with-param name="value" select="$timeCount"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-body>
    </fo:table>      
  </xsl:template>
        
  <xsl:template name="display-time">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.000')"/>
  </xsl:template>

  <xsl:template name="display-percent">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.00%')"/>
  </xsl:template>
  
</xsl:stylesheet>