/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.deployment.AffymetrixDnaAnalysisChpParser;
import gov.nih.nci.caintegrator.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

import java.io.File;

import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;

/**
 * Extends parser to make functionality available to tests outside of the study package.
 */
public class PublicAffymetrixDnaAnalysisChpParser extends AffymetrixDnaAnalysisChpParser {

    
    private static GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
    private static CentralTendencyCalculator centralTendencyCalculator = new CentralTendencyCalculator(
            genomicSource.getTechnicalReplicatesCentralTendency(), 
            genomicSource.isUseHighVarianceCalculation(), 
            genomicSource.getHighVarianceThreshold(), 
            genomicSource.getHighVarianceCalculationType());
    
    public PublicAffymetrixDnaAnalysisChpParser(File dnaAnalysisChpFile) {
        super(dnaAnalysisChpFile, centralTendencyCalculator);
    }
    
    @Override
    public void parse(ArrayDataValues values, ArrayData arrayData, MultiDataType multiDataType) throws DataRetrievalException {
        super.parse(values, arrayData, multiDataType);
    }

}
