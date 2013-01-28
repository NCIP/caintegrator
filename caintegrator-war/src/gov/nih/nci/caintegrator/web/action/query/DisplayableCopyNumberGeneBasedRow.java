/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class DisplayableCopyNumberGeneBasedRow extends AbstractDisplayableCopyNumberRow {
    private List<GenomicDataResultValue> values = new ArrayList<GenomicDataResultValue>();
    /**
     * @return the values
     */
    public List<GenomicDataResultValue> getValues() {
        return values;
    }
    /**
     * @param values the values to set
     */
    public void setValues(List<GenomicDataResultValue> values) {
        this.values = values;
    }
}
