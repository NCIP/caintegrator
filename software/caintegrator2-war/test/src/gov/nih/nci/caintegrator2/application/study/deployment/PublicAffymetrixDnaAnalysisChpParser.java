/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.study.deployment.AffymetrixDnaAnalysisChpParser;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;

import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;

/**
 * Extends parser to make functionality available to tests outside of the study package.
 */
public class PublicAffymetrixDnaAnalysisChpParser extends AffymetrixDnaAnalysisChpParser {

    public PublicAffymetrixDnaAnalysisChpParser(File dnaAnalysisChpFile) {
        super(dnaAnalysisChpFile);
    }
    
    @Override
    public void parse(ArrayDataValues values, ArrayData arrayData, MultiDataType multiDataType) throws DataRetrievalException {
        super.parse(values, arrayData, multiDataType);
    }

}
