/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class GeneExpressionPlotServiceImplTest extends AbstractMockitoTest {

    private GenomicStudyHelper studyHelper;


    @Before
    public void setup() {
        studyHelper = new GenomicStudyHelper();
        studyHelper.getQueryManagementService().setArrayDataService(arrayDataService);
    }

    @Test
    public void testGeneratePlots() throws InvalidCriterionException {
        Query query = studyHelper.createQuery();
        GenomicDataQueryResult result = studyHelper.getQueryManagementService().executeGenomicDataQuery(query);
        List<GenomicDataQueryResult> genomicResults = new ArrayList<GenomicDataQueryResult>();
        genomicResults.add(result);
        GeneExpressionPlotConfiguration configuration = GeneExpressionPlotConfigurationFactory.createPlotConfiguration(
                genomicResults, GenomicValueResultsTypeEnum.GENE_EXPRESSION);

        GeneExpressionPlotServiceImpl plotService = new GeneExpressionPlotServiceImpl();
        GeneExpressionPlotGroup plotGroup = plotService.generatePlots(configuration);
        assertNotNull(plotGroup.getPlot(PlotCalculationTypeEnum.MEAN));
        assertEquals(Integer.valueOf(1), plotGroup.getGroupNameToNumberSubjectsMap().get(query.getName()));
    }
}
