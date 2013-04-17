/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;



/**
 * Genome Build.
 */
public enum GenomeBuildVersionEnum {

    /**
     * HG16.
     */
    HG16("hg16"),

    /**
     * HG17.
     */
    HG17("hg17"),

    /**
     * HG18.
     */
    HG18("hg18"),

    /**
     * HG19.
     */
    HG19("hg19");

    private String value;

    private GenomeBuildVersionEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @param genomeVersion the genome version
     * @return the enum of the known genome version else null
     */
    public static GenomeBuildVersionEnum matchGenomVersion(String genomeVersion) {
        if (genomeVersion.contains("g16") || genomeVersion.equalsIgnoreCase("ncbi build 34")) {
            return HG16;
        } else if (genomeVersion.contains("g17") || genomeVersion.equalsIgnoreCase("ncbi build 35")) {
            return HG17;
        } else if (genomeVersion.contains("g18") || genomeVersion.equalsIgnoreCase("ncbi build 36.1")) {
            return HG18;
        } else if (genomeVersion.contains("g19")) {
            return HG19;
        }
        return null;
    }
}
