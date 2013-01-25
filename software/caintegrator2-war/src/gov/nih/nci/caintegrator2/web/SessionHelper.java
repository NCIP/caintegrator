/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator2.security.SecurityHelper;
import gov.nih.nci.caintegrator2.web.action.analysis.KMPlotMapper;
import gov.nih.nci.caintegrator2.web.action.analysis.geneexpression.GEPlotMapper;
import gov.nih.nci.logging.api.util.StringUtils;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Stores helper variables to our session.
 */
public final class SessionHelper {
    private static final String UNCHECKED = "unchecked";
    private static final String SESSION_HELPER_SESSION_KEY = "sessionHelper"; 
    private static final String DISPLAYABLE_USER_WORKSPACE_SESSION_KEY = "displayableWorkspace";
    private static final String DISPLAYABLE_USER_WORKSPACE_VALUE_STACK_KEY = "displayableWorkspace";
    private static final String KM_PLOT_SESSION_KEY = "kmPlot";
    private static final String GE_PLOT_SESSION_KEY = "gePlot";
    private static final String IS_AUTHORIZED_PAGE = "isAuthorizedPage";
    private Boolean studyManager = null;
    private Boolean platformManager = null;
    
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
     */
    public void refresh(WorkspaceService workspaceService) {
        if (isAuthenticated()) {
            getDisplayableUserWorkspace().refresh(workspaceService);
            getValueStack().set(DISPLAYABLE_USER_WORKSPACE_VALUE_STACK_KEY, getDisplayableUserWorkspace());
        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return SecurityHelper.getCurrentUsername();
    }

    /**
     * @return the authenticated
     */
    public boolean isAuthenticated() {
        return getUsername() != null;
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

    @SuppressWarnings(UNCHECKED)  // Session attribute map is untyped
    private static Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

    private ValueStack getValueStack() {
        return ActionContext.getContext().getValueStack();
    }

    /**
     * @return the studyManager
     */
    public boolean isStudyManager() {
        if (studyManager == null && !StringUtils.isBlank(getUsername())) {
            setStudyManager(SecurityHelper.isStudyManager(getUsername()));
        }
        return studyManager;
    }

    private void setStudyManager(boolean studyManager) {
        this.studyManager = studyManager;
    }

    /**
     * @return the platformManager
     */
    public boolean isPlatformManager() {
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
}
