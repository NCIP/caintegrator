<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('welcome_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->          
    
    <!--ADD CONTENT HERE-->
    
    <h1>Welcome to <strong><s:property value="openStudySubscription.study.shortTitleText" /></strong> Powered by caIntegrator2</h1>
    
    <div class="box">
        <h2>About VASARI</h2>
        <p>
            <strong>Description:</strong> <strong>V</strong>isually <strong>A</strong>ssembled <strong>A</strong>ccess to <strong>R</strong>embrandt <strong>I</strong>mages
            (<strong>VASARI</strong>) seeks to integrate neuroimaging analysis with clinical genomic data from the Rembrandt collection. The Rembrandt (REpository of Molecular BRAin Neoplasia Data) database contains images and tumor specimen information, including SNP array,
            expression array, proteomics, and clinical data . 
        </p>
    </div>
    
    <div class="box">
    
        <h2 class="darker">Search VASARI</h2>
        
        <!--Global Search-->
        
        <div class="bigsearch_wrapper">
        
            <div class="bigsearch">
            
                <label for="searcharea">Search</label>
                
                <select name="searcharea" id="searcharea">
                    <option>All Study Data</option>
                    <option>Subjects</option>
                    <option>Samples</option>
                    <option>Array Data</option>
                    <option>Images</option>
                </select>
                
                <input type="text" name="keyword" id="keyword" size="40" title="Keyword" value="&#40;Keyword&#41;" onfocus = "if (this.value == '&#40;Keyword&#41;') this.value='';"  />
                
                <input type="submit" value="Search" />
                
            </div>
            
        </div>
        
        <!--/Global Search-->
        
    </div>
    
    <div class="box">
        <h2>How to Use This Site</h2>
        <p>
            Explore this caIntegrator2-powered study by clicking on the navigational links on left side of the page. 
        </p>
        <ul>
            <li><strong>Study Elements</strong> contains browsable lists of the primary components of the study. </li>
            <li><strong>Study Data</strong> contains global, shared and user-saved Queries and Data Lists.</li>
            <li><strong>Analysis Tools</strong> provides advanced analysis tools as well as integration with external analysis packages. For more information read the <a href="#">Tutorial</a> and <a href="#">User Guide</a>.</li>
        </ul>
    </div>
    
    <!--/ADD CONTENT HERE-->
        
</div>
            
<div class="clear"><br /></div>