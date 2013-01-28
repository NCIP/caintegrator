/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web;

import gov.nih.nci.caintegrator.application.analysis.AnalysisViewerTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.security.SecurityHelper;
import gov.nih.nci.caintegrator.web.action.analysis.KMPlotMapper;
import gov.nih.nci.caintegrator.web.action.analysis.geneexpression.GEPlotMapper;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Stores helper variables to our session.
 */
public final class SessionHelper {
    /**
     * .war file context name.
     */
    public static final String WAR_CONTEXT_NAME = "caintegrator";
    private static final String SESSION_HELPER_SESSION_KEY = "sessionHelper";
    private static final String DISPLAYABLE_USER_WORKSPACE_SESSION_KEY = "displayableWorkspace";
    private static final String DISPLAYABLE_USER_WORKSPACE_VALUE_STACK_KEY = "displayableWorkspace";
    private static final String KM_PLOT_SESSION_KEY = "kmPlot";
    private static final String GE_PLOT_SESSION_KEY = "gePlot";
    private static final String ANONYMOUS_USER_WORKSPACE_SESSION_KEY = "anonymousUserWorkspace";
    private static final String IS_AUTHORIZED_PAGE = "isAuthorizedPage";
    private static final String WEBINF_CLASSES_DIR = "WEB-INF" + File.separator + "classes" + File.separator;
    private Boolean studyManager = null;
    private Boolean platformManager = null;
    private Boolean invalidDataBeingAccessed = false;

    private SessionHelper() {

    }

    /**
     * Singleton method to get the instance off of the sessionMap object or create a new one.
     * @return - CSMUserHelper object.
     */
    public static SessionHelper getInstance() {
        SessionHelper instance =
            (SessionHelper) getSession().get(SESSION_HELPER_SESSION_KEY);
        if (instance == null) {
            instance = new SessionHelper();
            getSession().put(SESSION_HELPER_SESSION_KEY, instance);
        }
        return instance;
    }

    /**
     * Refreshes the objects held by this object so that that are up to date and attached
     * to the current Hibernate session. After calling this method, clients can expect the
     * following objects on the object stack.
     * <ul>
     *  <li><b>displayableWorkspace</b> - The current <code>DisplayableUserWorkspace</code></li>
     *  <li><b>workspace</b> - The current <code>UserWorkspace</code></li>
     *  <li><b>studySubscription</b> - The current <code>StudySubscription</code> if one has been selected</li>
     *  <li><b>study</b> - The current <code>Study</code> if one has been selected</li>
     *  <li><b>query</b> - The current <code>Query</code> if one has been selected or is being defined</li>
     *  <li><b>queryResult</b> - The current <code>QueryResult</code> from the last clinical query executed</li>
     *  <li><b>genomicDataQueryResult</b> - The current <code>GenomicDataQueryResult</code> from the last
     *  genomic data query executed</li>
     * </ul>
     *
     * @param workspaceService session used to retrieve workspace.
     * @param isStudyNeedRefresh determines if we need to refresh study on the stack or not.
     */
    public void refresh(WorkspaceService workspaceService, boolean isStudyNeedRefresh) {
        if (isAuthenticated()) {
            getDisplayableUserWorkspace().refresh(workspaceService, isStudyNeedRefresh);
            getValueStack().set(DISPLAYABLE_USER_WORKSPACE_VALUE_STACK_KEY, getDisplayableUserWorkspace());
        }
    }

    /**
     * @return the session id
     */
    public static String getIgvSessionUrl() {
        return getAnalysisViewerUrl(AnalysisViewerTypeEnum.IGV.getValue());
    }

    /**
     *
     * @return the heatmap session url.
     */
    public static String getHeatmapSessionUrl() {
        return getAnalysisViewerUrl(AnalysisViewerTypeEnum.HEATMAP.getValue());
    }


    private static String getAnalysisViewerUrl(String analysisViewerType) {
        HttpServletRequest request = ServletActionContext.getRequest();
        return getRequestBaseUrl(request)
            + "/" + WAR_CONTEXT_NAME +  "/viewer/retrieveFile.jnlp?" + AnalysisViewerFileServlet.SESSION_PARAMETER + "="
            + request.getRequestedSessionId() + "&" + AnalysisViewerFileServlet.VIEWERTYPE_PARAMETER + "="
            + analysisViewerType + "&" + AnalysisViewerFileServlet.FILENAME_PARAMETER + "=";
    }

    /**
     *
     * @param context servlet context.
     * @return heatmap large bins file path.
     */
    public static String getHeatmapLargeBinsFile(ServletContext context) {
        return context.getRealPath(WEBINF_CLASSES_DIR + HeatmapFileTypeEnum.LARGE_BINS_FILE.getFilename());
    }

    /**
     *
     * @param context servlet context.
     * @return heatmap small bins file path.
     */
    public static String getHeatmapSmallBinsFile(ServletContext context) {
        return context.getRealPath(WEBINF_CLASSES_DIR + HeatmapFileTypeEnum.SMALL_BINS_FILE.getFilename());
    }

    /**
     * @return URL for the "/catingrator2/common" directory.
     */
    public static String getCaIntegratorCommonUrl() {
        return getRequestBaseUrl(ServletActionContext.getRequest()) + "/" + WAR_CONTEXT_NAME +  "/common/";
    }

    private static String getRequestBaseUrl(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String requestUrl = request.getRequestURL().toString();
        return requestUrl.substring(0, requestUrl.length() - requestUri.length());
    }

    /**
     * @return the username
     */
    public static String getUsername() {
        return SecurityHelper.getCurrentUsername();
    }

    /**
     * @return the authenticated
     */
    public boolean isAuthenticated() {
        return getUsername() != null;
    }

    /**
     *
     * @return if user is anonymously logged in.
     */
    public static boolean isAnonymousUser() {
        return UserWorkspace.ANONYMOUS_USER_NAME.equals(getUsername());
    }

    /**
     * @return the displayableUserWorkspace
     */
    public DisplayableUserWorkspace getDisplayableUserWorkspace() {
        DisplayableUserWorkspace displayableUserWorkspace =
            (DisplayableUserWorkspace) getSession().get(DISPLAYABLE_USER_WORKSPACE_SESSION_KEY);
        if (displayableUserWorkspace == null && isAuthenticated()) {
            displayableUserWorkspace = new DisplayableUserWorkspace();
            getSession().put(DISPLAYABLE_USER_WORKSPACE_SESSION_KEY, displayableUserWorkspace);
        }
        return displayableUserWorkspace;
    }

    private static Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

    private ValueStack getValueStack() {
        return ActionContext.getContext().getValueStack();
    }

    /**
     * @return the struts token or empty string if no token is found.
     */
    public String getToken() {

        String returnString = "";

        if (ActionContext.getContext().getSession()
                .get(org.apache.struts2.util.TokenHelper.DEFAULT_TOKEN_NAME) != null) {
            returnString =
                (String) ActionContext.getContext().
                getSession().get(org.apache.struts2.util.TokenHelper.DEFAULT_TOKEN_NAME);
        }

        return returnString;
    }

    /**
     * @return the struts token name
     */
    public String getTokenName() {
        return org.apache.struts2.util.TokenHelper.DEFAULT_TOKEN_NAME;

    }

    /**
     * @return the studyManager
     */
    public boolean isStudyManager() {
        if (isAnonymousUser()) {
            return false;
        }
        if (studyManager == null && !StringUtils.isBlank(getUsername())) {
            setStudyManager(SecurityHelper.isStudyManager(getUsername()));
        }
        return studyManager;
    }

    /**
     *
     * @param studyManager the studyManager to set.
     */
    public void setStudyManager(boolean studyManager) {
        this.studyManager = studyManager;
    }

    /**
     * @return the platformManager
     */
    public boolean isPlatformManager() {
        if (isAnonymousUser()) {
            return false;
        }
        if (platformManager == null && !StringUtils.isBlank(getUsername())) {
            setPlatformManager(SecurityHelper.isPlatformManager(getUsername()));
        }
        return platformManager;
    }

    /**
     * @param platformManager the platformManager to set
     */
    public void setPlatformManager(Boolean platformManager) {
        this.platformManager = platformManager;
    }

    /**
     *
     *  @param anonymousUserWorkspace anonymous user workspace to put on the session.
     */
    public static void setAnonymousUserWorkspace(UserWorkspace anonymousUserWorkspace) {
        getSession().put(ANONYMOUS_USER_WORKSPACE_SESSION_KEY, anonymousUserWorkspace);
    }

    /**
     *
     * @return anonymous user workspace on the session.
     */
    public static UserWorkspace getAnonymousUserWorkspace() {
        return (UserWorkspace) getSession().get(ANONYMOUS_USER_WORKSPACE_SESSION_KEY);
    }


    /**
     * Uses a KMPlotMapper object to store multiple KMPlots on the value stack, based on
     * one per plot type.
     * @param plotType the plot type to add the plot to.
     * @param kmPlot the kmPlot to set on the ValueStack.
     */
    public static void setKmPlot(PlotTypeEnum plotType, KMPlot kmPlot) {
        if (getSession().get(KM_PLOT_SESSION_KEY) == null) {
            getSession().put(KM_PLOT_SESSION_KEY, new KMPlotMapper());
        }
        KMPlotMapper map = (KMPlotMapper) getSession().get(KM_PLOT_SESSION_KEY);
        map.getKmPlotMap().put(plotType, kmPlot);
    }

    /**
     *
     * @return the kmPlot on the ValueStack.
     */
    public static KMPlot getAnnotationBasedKmPlot() {
        KMPlotMapper map = (KMPlotMapper) getSession().get(KM_PLOT_SESSION_KEY);
        if (map != null) {
            return map.getAnnotationBasedKmPlot();
        }
        return null;
    }

    /**
     *
     * @return the kmPlot on the ValueStack.
     */
    public static KMPlot getGeneExpressionBasedKmPlot() {
        KMPlotMapper map = (KMPlotMapper) getSession().get(KM_PLOT_SESSION_KEY);
        if (map != null) {
            return map.getGeneExpressionBasedKmPlot();
        }
        return null;
    }

    /**
     *
     * @return the kmPlot on the ValueStack.
     */
    public static KMPlot getQueryBasedKmPlot() {
        KMPlotMapper map = (KMPlotMapper) getSession().get(KM_PLOT_SESSION_KEY);
        if (map != null) {
            return map.getQueryBasedKmPlot();
        }
        return null;
    }

    /**
     * Clears all plots off the session.
     */
    public static void clearKmPlots() {
        KMPlotMapper mapper = (KMPlotMapper) getSession().get(KM_PLOT_SESSION_KEY);
        if (mapper != null) {
            mapper.clear();
        }
    }

    /**
     * Uses a GEPlotMapper object to store multiple GEPlots on the value stack, based on
     * one per plot type.
     * @param plotType the plot type to add the plot to.
     * @param gePlots the gePlots to set on the ValueStack.
     */
    public static void setGePlots(PlotTypeEnum plotType, GeneExpressionPlotGroup gePlots) {
        if (getSession().get(GE_PLOT_SESSION_KEY) == null) {
            getSession().put(GE_PLOT_SESSION_KEY, new GEPlotMapper());
        }
        GEPlotMapper map = (GEPlotMapper) getSession().get(GE_PLOT_SESSION_KEY);
        map.getGePlotMap().put(plotType, gePlots);
    }

    /**
     *
     * @return the Annotation gePlots on the ValueStack.
     */
    public static GeneExpressionPlotGroup getAnnotationBasedGePlots() {
        GEPlotMapper map = (GEPlotMapper) getSession().get(GE_PLOT_SESSION_KEY);
        if (map != null) {
            return map.getAnnotationBasedGePlot();
        }
        return null;
    }

    /**
     *
     * @return the Genomic Query gePlots on the ValueStack.
     */
    public static GeneExpressionPlotGroup getGenomicQueryBasedGePlots() {
        GEPlotMapper map = (GEPlotMapper) getSession().get(GE_PLOT_SESSION_KEY);
        if (map != null) {
            return map.getGenomicQueryBasedGePlot();
        }
        return null;
    }

    /**
     *
     * @return the Clinical Query gePlots on the ValueStack.
     */
    public static GeneExpressionPlotGroup getClinicalQueryBasedGePlots() {
        GEPlotMapper map = (GEPlotMapper) getSession().get(GE_PLOT_SESSION_KEY);
        if (map != null) {
            return map.getClinicalQueryBasedGePlot();
        }
        return null;
    }

    /**
     * Clears all plots off the session.
     */
    public static void clearGePlots() {
        GEPlotMapper mapper = (GEPlotMapper) getSession().get(GE_PLOT_SESSION_KEY);
        if (mapper != null) {
            mapper.clear();
        }
    }

    /**
     * Determines if the currently requested page is authorized for user.
     * @return T/F value.
     */
    public Boolean isAuthorizedPage() {
        Boolean isAuthorizedPage = (Boolean) getSession().get(IS_AUTHORIZED_PAGE);
        if (isAuthorizedPage == null) {
            return true;
        }
        return isAuthorizedPage;
    }

    /**
     * Sets the flag for the requested page to be either T/F.
     * @param isAuthorized if the user is authorized or not to see page.
     */
    public void setAuthorizedPage(Boolean isAuthorized) {
        getSession().put(IS_AUTHORIZED_PAGE, isAuthorized);
    }

    /**
     * @return the invalidDataBeingAccessed
     */
    public Boolean getInvalidDataBeingAccessed() {
        return invalidDataBeingAccessed;
    }

    /**
     * @param invalidDataBeingAccessed the invalidDataBeingAccessed to set
     */
    public void setInvalidDataBeingAccessed(Boolean invalidDataBeingAccessed) {
        this.invalidDataBeingAccessed = invalidDataBeingAccessed;
    }
}
