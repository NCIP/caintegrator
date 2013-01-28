/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.analysis;

import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.Sample;

/**
 * Created after a gistic analysis job is run to store the <code>GisticGenomicRegionReporter</code>
 * to be queried on.
 */
public class GisticAnalysis extends AbstractCopyNumberAnalysis {
    private static final long serialVersionUID = 1L;
    
    private Float amplificationsThreshold;
    private Float deletionsThreshold;
    private String genomeBuildInformation;
    private Integer joinSegmentSize;
    private String queryOrListName;
    private Float qvThreshold;
    private String url;
    private ReporterList reporterList;
    
    /**
     * @return the amplificationsThreshold
     */
    public Float getAmplificationsThreshold() {
        return amplificationsThreshold;
    }
    /**
     * @param amplificationsThreshold the amplificationsThreshold to set
     */
    public void setAmplificationsThreshold(Float amplificationsThreshold) {
        this.amplificationsThreshold = amplificationsThreshold;
    }
    /**
     * @return the deletionsThreshold
     */
    public Float getDeletionsThreshold() {
        return deletionsThreshold;
    }
    /**
     * @param deletionsThreshold the deletionsThreshold to set
     */
    public void setDeletionsThreshold(Float deletionsThreshold) {
        this.deletionsThreshold = deletionsThreshold;
    }
    /**
     * @return the genomeBuildInformation
     */
    public String getGenomeBuildInformation() {
        return genomeBuildInformation;
    }
    /**
     * @param genomeBuildInformation the genomeBuildInformation to set
     */
    public void setGenomeBuildInformation(String genomeBuildInformation) {
        this.genomeBuildInformation = genomeBuildInformation;
    }
    /**
     * @return the joinSegmentSize
     */
    public Integer getJoinSegmentSize() {
        return joinSegmentSize;
    }
    /**
     * @param joinSegmentSize the joinSegmentSize to set
     */
    public void setJoinSegmentSize(Integer joinSegmentSize) {
        this.joinSegmentSize = joinSegmentSize;
    }
    /**
     * @return the queryOrListName
     */
    public String getQueryOrListName() {
        return queryOrListName;
    }
    /**
     * @param queryOrListName the queryOrListName to set
     */
    public void setQueryOrListName(String queryOrListName) {
        this.queryOrListName = queryOrListName;
    }
    /**
     * @return the qvThreshold
     */
    public Float getQvThreshold() {
        return qvThreshold;
    }
    /**
     * @param qvThreshold the qvThreshold to set
     */
    public void setQvThreshold(Float qvThreshold) {
        this.qvThreshold = qvThreshold;
    }
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return the reporterList
     */
    public ReporterList getReporterList() {
        return reporterList;
    }
    /**
     * @param reporterList the reporterList to set
     */
    public void setReporterList(ReporterList reporterList) {
        this.reporterList = reporterList;
    }
    
    /**
     * Return the sample by sample name.
     * @param sampleName use to retrieve sample
     * @return sample
     */
    public Sample getSample(String sampleName) {
        for (Sample sample : getSamplesUsedForCalculation()) {
            if (sample.getName().equalsIgnoreCase(sampleName)) {
                return sample;
            }
        }
        return null;
    }
    
}
