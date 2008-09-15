        //<![CDATA[
        /*-----------------------------------------------------------+
         | addLoadEvent: Add event handler to body when window loads |
         +-----------------------------------------------------------*/
        function addLoadEvent(func) {
            var oldonload = window.onload;
            
            if (typeof window.onload != "function") {
                window.onload = func;
            } else {
                window.onload = function () {
                    oldonload();
                    func();
                }
            }
        }
        
        /*------------------------------------+
         | Functions to run when window loads |
         +------------------------------------*/
        addLoadEvent(function () {
            initChecklist();
        });
        
        /*----------------------------------------------------------+
         | initChecklist: Add :hover functionality on labels for IE |
         +----------------------------------------------------------*/
        function initChecklist() {
            if (document.all && document.getElementById) {
                // Get all unordered lists
                var lists = document.getElementsByTagName("ul");
                
                for (i = 0; i < lists.length; i++) {
                    var theList = lists[i];
                    
                    // Only work with those having the class "checklist"
                    if (theList.className.indexOf("checklist") > -1) {
                        var labels = theList.getElementsByTagName("label");
                        
                        // Assign event handlers to labels within
                        for (var j = 0; j < labels.length; j++) {
                            var theLabel = labels[j];
                            theLabel.onmouseover = function() { this.className += " hover"; };
                            theLabel.onmouseout = function() { this.className = this.className.replace(" hover", ""); };
                        }
                    }
                }
            }
        }
    //]]>
