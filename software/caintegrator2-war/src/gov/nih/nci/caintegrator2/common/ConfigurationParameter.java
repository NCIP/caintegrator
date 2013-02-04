/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import java.io.File;

/**
 * Configurable properties for caIntegrator 2.
 */
public enum ConfigurationParameter {
    
    /**
     * Determines where Study files (NetCDF, annotation files, etc.) are stored on the system.
     */
    STUDY_FILE_STORAGE_DIRECTORY(System.getProperty("java.io.tmpdir")),
    
    /**
     * Determines where to store temporary files for download.
     */
    TEMP_DOWNLOAD_STORAGE_DIRECTORY(System.getProperty("java.io.tmpdir") + File.separator + "tmpDownload"),
    
    /**
     * 
     */
    USER_FILE_STORAGE_DIRECTORY(System.getProperty("java.io.tmpdir") + File.separator + "cai2UserFiles"),
    
    /**
     * Default Grid index service URL.
     */
    GRID_INDEX_URL(
            "http://cagrid-index.nci.nih.gov:8080/wsrf/services/DefaultIndexService"),
    
    /**
     * Default URL for GenePattern service.
     */
    GENE_PATTERN_URL("http://genepattern.broadinstitute.org/gp/services/Analysis"),
    
    /**
     * Default Preprocess Dataset service URL.
     */
    PREPROCESS_DATASET_URL(
            "http://node255.broadinstitute.org:6060/wsrf/services/cagrid/PreprocessDatasetMAGEService"),
    
    /**
     * Default Comparative Marker Selection service URL.
     */
    COMPARATIVE_MARKER_SELECTION_URL(
            "http://node255.broadinstitute.org:11010/wsrf/services/cagrid/ComparativeMarkerSelMAGESvc"),
            
    /**
     * Default PCA service URL.
     */
    PCA_URL("http://node255.broadinstitute.org:6060/wsrf/services/cagrid/PCA"),
            
    /**
     * Default GISTIC service URL.
     */
    GISTIC_URL("http://node255.broadinstitute.org:10010/wsrf/services/cagrid/Gistic"),
            
    /**
     * Default NBIA service URL.
     */
    NBIA_URL("http://imaging.nci.nih.gov/wsrf/services/cagrid/NCIACoreService"),
            
    /**
     * Default CaArray service URL.
     */
    CAARRAY_HOST("array.nci.nih.gov"),
            
    /**
     * Default CaArray service URL.
     */
    CAARRAY_PORT("8080"),
            
    /**
     * Default CaArray Web URL.
     */
    CAARRAY_URL("https://array.nci.nih.gov/caarray"),
    
    /**
     * Default CaDNACopy service URL.
     */
    CA_DNA_COPY_URL("http://cabig.bioconductor.org:80/wsrf/services/cagrid/CaDNAcopy"),
    
    /**
     * Default CaDNACopy service URL.
     */
    CGAP_URL("http://cgap.nci.nih.gov/Genes/RunUniGeneQuery?PAGE=1&SYM=&PATH=&ORG=Hs&TERM="),
    
    /**
     * Default email to send registration requests to.
     */
    REGISTRATION_EMAIL_TO("replace@youremail.com"),
    
    /**
     * Default email to send registration confirmations from.
     */
    REGISTRATION_EMAIL_FROM("NCICB@pop.nci.nih.gov"),
    
    /**
     * URL For UPT.
     */
    UPT_URL("[Unknown UPT URL]");

    private String defaultValue;

    ConfigurationParameter(String defaultValue)  {
        this.defaultValue = defaultValue;
    }
    
    /**
     * @return the default value for the configuration parameter.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

}
