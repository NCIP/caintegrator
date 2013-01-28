/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;

/**
 * Genomic data set.
 */
public class GenomicDataParameterValue extends AbstractParameterValue {

    private static final long serialVersionUID = 1L;
    private GenomicDataQueryResult genomicData;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueAsString() {
        return genomicData.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueFromString(String stringValue) {
        throw new IllegalStateException("Can't set value from String");
    }

    /**
     * @return the genomicData
     */
    public GenomicDataQueryResult getGenomicData() {
        return genomicData;
    }

    /**
     * @param genomicData the genomicData to set
     */
    public void setGenomicData(GenomicDataQueryResult genomicData) {
        this.genomicData = genomicData;
    }


}
