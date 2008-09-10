<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content"><!--Page Help-->

        <div class="pagehelp"><a href="#" class="help"></a></div>
        
        <h1>Search VASARI</h1>
        
        <!--Tabs -->
            
        <ul id="maintabs" class="tabs">
            <li><a href="#criteria" class="active">Criteria</a></li>
            <li><a href="#columns">Columns</a></li>
            <li><a href="#sortorder">Sorting</a></li>
            <li><a href="#searchresults">Search Results</a></li>
            <li><a href="#savesearch">Save As...</a></li>
        </ul>
        
        <!--/Tabs -->
        
        <!-- 
            directions on http://livepipe.net/control/tabs 
            - make sure you add control.tabs.js to your scripts directory! 
            - Matt 
        -->
        <script type="text/javascript">             
            //<![CDATA[
            Event.observe(window,'load',function(){
                $$('.tabs').each(function(tabs){
                    new Control.Tabs(tabs);
                });
            });
            //]]>
        </script>
        
        
        <!--Tab Box-->
        
        <div id="tabboxwrapper">       
            <jsp:include page="/WEB-INF/jsp/tiles/editQuery_tile.jsp" />
            <jsp:include page="/WEB-INF/jsp/tiles/showQueryResults_tile.jsp" />
        </div>
        
        <!--/Tab Box-->
        
</div>