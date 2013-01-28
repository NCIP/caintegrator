/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.application;


import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public abstract class AbstractCriterion extends AbstractCaIntegrator2Object implements Cloneable {

    private static final long serialVersionUID = 1L;
    private transient boolean finalMaskApplied = false;

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractCriterion clone() throws CloneNotSupportedException {
        return (AbstractCriterion) super.clone();
    }

    /**
     * @return all gene symbols in this criterion.
     */
    protected List<String> getGeneSymbolsInCriterion() {
        return new ArrayList<String>();
    }

    /**
     *
     * @return all subject identifiers in this criterion.
     */
    protected Set<String> getSubjectIdentifiers() {
        return new HashSet<String>();
    }

    /**
     * @param genomicCriterionType the type of genomic criterion to get platform for.
     * @return default value for platformName is null;
     */
    public abstract String getPlatformName(GenomicCriterionTypeEnum genomicCriterionType);

    /**
     * @return the finalMaskApplied
     */
    public boolean isFinalMaskApplied() {
        return finalMaskApplied;
    }

    /**
     * @param finalMaskApplied the finalMaskApplied to set
     */
    public void setFinalMaskApplied(boolean finalMaskApplied) {
        this.finalMaskApplied = finalMaskApplied;
    }

    /**
     * Determines if this criterion has masked criterion.
     * @return boolean determining if this is a masked criterion or not.
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract") // Default implementation is false
    protected boolean isMaskedCriterion() {
        return false;
    }


}
