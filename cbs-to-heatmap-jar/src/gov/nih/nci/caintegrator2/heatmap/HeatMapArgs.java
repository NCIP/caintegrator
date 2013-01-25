/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.heatmap;

import java.util.ArrayList;
import java.util.List;

public class HeatMapArgs {
    // These two are optional, it will either be a file, or it will be from pre-parsed data.
    private String segmentFile;
    private String refGenesFile;
    
    // If previous 2 files are null, then these 2 need to be filled.
    private List<SegmentDataWrapper> segmentDatas = new ArrayList<SegmentDataWrapper>();
    private List<GeneLocationWrapper> geneLocations = new ArrayList<GeneLocationWrapper>();
    
    // These are required files
    private String smallBinFile;
    private String bigBinFile;
    
    // These are the output files that are produced.
    private String genomeOutFile;
    private String geneOutFile;
    
    private String sampleFile;
    private String genderFile;
    private String title = "caIntegrator Heatmap"; 
    private String protocol;
    private String scale = "0.3,0.8,1.3,1.8,2.2,2.7,3.2,3.7,4.2,6,8,10,12,14,16,18,20";
    // exclude very small segments: they are far more likely to be germline copy number variation loci
    private Integer minSegLength = 500; // default
    private Integer base = 2;
    private String platform = "all";
    private String submitter = "all";
    private String contact = "caIntegrator Development Team";
    private String cnvTrack;
    private String project = "caIntegrator Heatmap";
    
    /**
     * @return the smallBinFile
     */
    public String getSmallBinFile() {
        return smallBinFile;
    }
    /**
     * @param smallBinFile the smallBinFile to set
     */
    public void setSmallBinFile(String smallBinFile) {
        this.smallBinFile = smallBinFile;
    }
    /**
     * @return the bigBinFile
     */
    public String getBigBinFile() {
        return bigBinFile;
    }
    /**
     * @param bigBinFile the bigBinFile to set
     */
    public void setBigBinFile(String bigBinFile) {
        this.bigBinFile = bigBinFile;
    }
    /**
     * @return the segmentFile
     */
    public String getSegmentFile() {
        return segmentFile;
    }
    /**
     * @param segmentFile the segmentFile to set
     */
    public void setSegmentFile(String segmentFile) {
        this.segmentFile = segmentFile;
    }
    /**
     * @return the sampleFile
     */
    public String getSampleFile() {
        return sampleFile;
    }
    /**
     * @param sampleFile the sampleFile to set
     */
    public void setSampleFile(String sampleFile) {
        this.sampleFile = sampleFile;
    }
    /**
     * @return the genomeOutFile
     */
    public String getGenomeOutFile() {
        return genomeOutFile;
    }
    /**
     * @param genomeOutFile the genomeOutFile to set
     */
    public void setGenomeOutFile(String genomeOutFile) {
        this.genomeOutFile = genomeOutFile;
    }
    /**
     * @return the geneOutFile
     */
    public String getGeneOutFile() {
        return geneOutFile;
    }
    /**
     * @param geneOutFile the geneOutFile to set
     */
    public void setGeneOutFile(String geneOutFile) {
        this.geneOutFile = geneOutFile;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the refGenes
     */
    public String getRefGenesFile() {
        return refGenesFile;
    }
    /**
     * @param refGenes the refGenes to set
     */
    public void setRefGenesFile(String refGenesFile) {
        this.refGenesFile = refGenesFile;
    }
    /**
     * @return the genderFile
     */
    public String getGenderFile() {
        return genderFile;
    }
    /**
     * @param genderFile the genderFile to set
     */
    public void setGenderFile(String genderFile) {
        this.genderFile = genderFile;
    }
    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }
    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    /**
     * @return the scale
     */
    public String getScale() {
        return scale;
    }
    /**
     * @param scale the scale to set
     */
    public void setScale(String scale) {
        this.scale = scale;
    }
    /**
     * @return the minSegLength
     */
    public Integer getMinSegLength() {
        return minSegLength;
    }
    /**
     * @param minSegLength the minSegLength to set
     */
    public void setMinSegLength(Integer minSegLength) {
        this.minSegLength = minSegLength;
    }
    /**
     * @return the base
     */
    public Integer getBase() {
        return base;
    }
    /**
     * @param base the base to set
     */
    public void setBase(Integer base) {
        this.base = base;
    }
    /**
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }
    /**
     * @param platform the platform to set
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    /**
     * @return the submitter
     */
    public String getSubmitter() {
        return submitter;
    }
    /**
     * @param submitter the submitter to set
     */
    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }
    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }
    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }
    /**
     * @return the cnvTrack
     */
    public String getCnvTrack() {
        return cnvTrack;
    }
    /**
     * @param cnvTrack the cnvTrack to set
     */
    public void setCnvTrack(String cnvTrack) {
        this.cnvTrack = cnvTrack;
    }
    /**
     * @return the project
     */
    public String getProject() {
        return project;
    }
    /**
     * @param project the project to set
     */
    public void setProject(String project) {
        this.project = project;
    }
    /**
     * @return the segmentDatas
     */
    public List<SegmentDataWrapper> getSegmentDatas() {
        return segmentDatas;
    }
    /**
     * @param segmentDatas the segmentDatas to set
     */
    public void setSegmentDatas(List<SegmentDataWrapper> segmentDatas) {
        this.segmentDatas = segmentDatas;
    }
    /**
     * @return the geneLocations
     */
    public List<GeneLocationWrapper> getGeneLocations() {
        return geneLocations;
    }
    /**
     * @param geneLocations the geneLocations to set
     */
    public void setGeneLocations(List<GeneLocationWrapper> geneLocations) {
        this.geneLocations = geneLocations;
    }
}
