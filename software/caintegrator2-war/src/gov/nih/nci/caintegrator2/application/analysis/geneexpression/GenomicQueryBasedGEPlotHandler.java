/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.geneexpression;

import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotConfiguration;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotConfigurationFactory;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator2.application.geneexpression.GenomicValueResultsTypeEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * GE Plot Handler for Annotation Based GE Plots.
 */
class GenomicQueryBasedGEPlotHandler extends AbstractGEPlotHandler {

    private final GEPlotGenomicQueryBasedParameters parameters;
        
    GenomicQueryBasedGEPlotHandler(CaIntegrator2Dao dao, 
                                 QueryManagementService queryManagementService, 
                                 GEPlotGenomicQueryBasedParameters parameters, 
                                 GeneExpressionPlotService gePlotService) {
        super(dao, queryManagementService, gePlotService);
        this.parameters = parameters;
    }
    
    /**
     * {@inheritDoc}
     * @throws InvalidCriterionException 
     */
    public GeneExpressionPlotGroup createPlots(StudySubscription subscription)
            throws InvalidCriterionException {
        List<GenomicDataQueryResult> genomicResults = new ArrayList<GenomicDataQueryResult>();
        genomicResults.add(retrieveGenomicResults(subscription));
        GeneExpressionPlotConfiguration configuration = 
                GeneExpressionPlotConfigurationFactory.createPlotConfiguration(genomicResults,
                        Cai2Util.isFoldChangeQuery(parameters.getQuery()) 
                        ? GenomicValueResultsTypeEnum.FOLD_CHANGE : GenomicValueResultsTypeEnum.GENE_EXPRESSION);
        return createGeneExpressionPlot(parameters, configuration);
    }

    
    private GenomicDataQueryResult retrieveGenomicResults(StudySubscription subscription) 
    throws InvalidCriterionException {
        Query query = parameters.getQuery();
        query.setReporterType(parameters.getReporterType());
        query.setSubscription(subscription);
        return getQueryManagementService().executeGenomicDataQuery(query);
    }

}
