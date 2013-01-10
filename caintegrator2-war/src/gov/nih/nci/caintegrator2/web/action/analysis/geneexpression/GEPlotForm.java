/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import gov.nih.nci.caintegrator2.web.SessionHelper;


/**
 * Holder for the different types of GE Plot Forms.
 */
public class GEPlotForm {

    private final GEPlotAnnotationBasedActionForm annotationBasedForm = new GEPlotAnnotationBasedActionForm();
    private final GEPlotGeneExpressionQueryBasedActionForm geneExpressionQueryBasedForm =
        new GEPlotGeneExpressionQueryBasedActionForm();
    private final GEPlotClinicalQueryBasedActionForm clinicalQueryBasedForm = new GEPlotClinicalQueryBasedActionForm();

    /**
     * Clears all forms and the GE Plots out of the session.
     */
    public void clear() {
        SessionHelper.clearGePlots();
        annotationBasedForm.clear();
        geneExpressionQueryBasedForm.clear();
        clinicalQueryBasedForm.clear();
    }
    
    /**
     * @return the annotationBasedForm
     */
    public GEPlotAnnotationBasedActionForm getAnnotationBasedForm() {
        return annotationBasedForm;
    }

    /**
     * @return the geneExpressionQueryBasedForm
     */
    public GEPlotGeneExpressionQueryBasedActionForm getGeneExpressionQueryBasedForm() {
        return geneExpressionQueryBasedForm;
    }

    /**
     * @return the clinicalQueryBasedForm
     */
    public GEPlotClinicalQueryBasedActionForm getClinicalQueryBasedForm() {
        return clinicalQueryBasedForm;
    }
}
