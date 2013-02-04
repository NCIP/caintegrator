/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;


/**
 * Reporter for DNA Analysis (SNP) arrays.
 */
public class DnaAnalysisReporter extends AbstractReporter {

    private static final long serialVersionUID = 1L;

    private static final int X_CHROMOSOME_VALUE = 23;
    private static final int Y_CHROMOSOME_VALUE = 24;
    private static final int MT_CHROMOSOME_VALUE = 25;

    private String chromosome;
    private Integer position;
    private String dbSnpId;
    private Character alleleA;
    private Character alleleB;

    /**
     * {@inheritDoc}
     */
    public int compareTo(AbstractReporter abstractReporter) {
        DnaAnalysisReporter reporter = (DnaAnalysisReporter) abstractReporter;
        int chromosomeComparison = getChromosomeComparison(reporter);
        if (chromosomeComparison == 0) {
            return getPositionComparison(reporter);
        } else {
            return chromosomeComparison;
        }
    }

    private int getPositionComparison(DnaAnalysisReporter reporter) {
        int positionInt = getPosition() != null ? getPosition() : 0;
        int comparePositionInt = reporter.getPosition() != null ? reporter.getPosition() : 0;
        return positionInt - comparePositionInt;
    }

    private int getChromosomeComparison(DnaAnalysisReporter reporter) {
        if (chromosome == null) {
            return reporter.getChromosome() == null ? 0 : 1;
        } else if (reporter.getChromosome() == null) {
            return -1;
        } else {
            return ((Integer) getChromosomeAsInt()).compareTo(reporter.getChromosomeAsInt());
        }
    }

    /**
     * @return the chromosome
     */
    public String getChromosome() {
        return chromosome;
    }

    /**
     * @param chromosome the chromosome to set
     */
    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
        validateChromosome();
    }

    private void validateChromosome() {
        try {
            getChromosomeAsInt();
        } catch (Exception e) {
            this.chromosome = null;
        }
    }
    
    /**
     * @return the position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * @return the dbSnpId
     */
    public String getDbSnpId() {
        return dbSnpId;
    }

    /**
     * @param dbSnpId the dbSnpId to set
     */
    public void setDbSnpId(String dbSnpId) {
        this.dbSnpId = dbSnpId;
    }

    /**
     * @return the alleleA
     */
    public Character getAlleleA() {
        return alleleA;
    }

    /**
     * @param alleleA the alleleA to set
     */
    public void setAlleleA(Character alleleA) {
        this.alleleA = alleleA;
    }

    /**
     * @return the alleleB
     */
    public Character getAlleleB() {
        return alleleB;
    }

    /**
     * @param alleleB the alleleB to set
     */
    public void setAlleleB(Character alleleB) {
        this.alleleB = alleleB;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
    /**
     * Verifies that the reporter has a valid location.
     * @return T/F value.
     */
    public boolean hasValidLocation() {
        return getChromosome() != null && getPosition() != null;
    }

    /**
     * @return the integer value for the chromosome.
     */
    public int getChromosomeAsInt() {
        if ("X".equalsIgnoreCase(getChromosome())) {
            return X_CHROMOSOME_VALUE;
        } else if ("Y".equalsIgnoreCase(getChromosome())) {
            return Y_CHROMOSOME_VALUE;
        } else if ("MT".equalsIgnoreCase(getChromosome())) {
            return MT_CHROMOSOME_VALUE;
        } else {
            return Integer.parseInt(chromosome);
        }
    }


}
