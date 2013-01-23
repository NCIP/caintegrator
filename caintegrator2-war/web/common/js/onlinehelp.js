//
// This file contains javascript functions needed to
// link to the online help.
// 
// help constants
var contextPath = "/caintegrator/";
var overviewTutorialURL = contextPath + "tutorials/overview_screencast/overview_screencast.html";
var deployStudyTutorialURL = contextPath + "tutorials/deploy_study_screencast/deploy_study_screencast.html";
var useStudyTutorialURL = contextPath + "tutorials/use_study_screencast/use_study.html";
var wikiHelpURL = "https://wiki.nci.nih.gov/x/";

// actually opens the help window
function openWin(pageURL) {
    window.open(pageURL,"Help","status,scrollbars,resizable,alwaysRaised,dependent,width=1000,height=800");
}

// open tutorial window (default; no navigation)
function openOverviewTutorialWindow(contextName) {
    var pageURL = makeOverviewTutorialURL() + contextName;
    openWin(pageURL);
}

// method to create the full URL for overview tutorial
function makeOverviewTutorialURL(includeNav) {
    return (overviewTutorialURL + "#" + "pagemode=bookmarks" + "&" +"nameddest=");
}

// open deploy study tutorial window (default; no navigation)
function openDeployStudyTutorialWindow(contextName) {
    var pageURL = makeDeployStudyTutorialURL() + contextName;
    openWin(pageURL);
}

// method to create the full URL for deploy study tutorial
function makeDeployStudyTutorialURL(includeNav) {
    return (deployStudyTutorialURL + "#" + "pagemode=bookmarks" + "&" +"nameddest=");
}

// open use study tutorial window (default; no navigation)
function openUseStudyTutorialWindow(contextName) {
    var pageURL = makeUseStudyTutorialURL() + contextName;
    openWin(pageURL);
}

// method to create the full URL for use study tutorial
function makeUseStudyTutorialURL(includeNav) {
    return (useStudyTutorialURL + "#" + "pagemode=bookmarks" + "&" +"nameddest=");
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