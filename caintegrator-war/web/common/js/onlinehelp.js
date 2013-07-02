//
// This file contains javascript functions needed to
// link to the online help.
// 
// help constants
var wikiHelpURL = "https://wiki.nci.nih.gov/x/";

// actually opens the help window
function openWin(pageURL) {
    window.open(pageURL,"Help","status,scrollbars,resizable,alwaysRaised,dependent,width=1000,height=800");
}

/**
 * Opens the wiki help on the given page and anchor.
 * @param page the page on the wiki
 * @param anchor the anchor on the page
 */
function openWikiHelp(page, anchor) {
    var url = wikiHelpURL + page + "#" + anchor;
    return openWin(url);
}