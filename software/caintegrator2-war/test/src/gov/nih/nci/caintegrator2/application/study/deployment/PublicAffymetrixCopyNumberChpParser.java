/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.study.deployment.AffymetrixCopyNumberChpParser;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;

/**
 * Extends parser to make functionality available to tests outside of the study package.
 */
public class PublicAffymetrixCopyNumberChpParser extends AffymetrixCopyNumberChpParser {

    public PublicAffymetrixCopyNumberChpParser(File copyNumberChpFile) {
        super(copyNumberChpFile);
    }
    
    @Override
    public void parse(ArrayDataValues values, ArrayData arrayData) throws DataRetrievalException {
        super.parse(values, arrayData);
    }

}
