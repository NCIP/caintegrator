/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.gistic;

import edu.wustl.icr.asrv1.common.GenomeAnnotationInformation;
import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.cabig.icr.asbp.parameter.Parameter;
import org.cabig.icr.asbp.parameter.ParameterList;

/**
 * Parameters to run Gistic Gene Pattern grid service.
 */
public class GisticParameters {
    private static final String REMOVE_X_YES = "Yes";
    private static final String REMOVE_X_NO = "No";
    private static final int REMOVE_X_YES_VALUE = 1;
    private static final int REMOVE_X_NO_VALUE = 0;
    private static final Float DEFAULT_AMPLIFICATIONS = .1f;
    private static final Float DEFAULT_DELETIONS = .1f;
    private static final Float DEFAULT_QV_THRESH = .25f;
    
    private Query clinicalQuery;
    private SampleSet excludeControlSampleSet;
    private File cnvSegmentsToIgnoreFile;
    private final ServerConnectionProfile server = new ServerConnectionProfile();
    private GisticRefgeneFileEnum refgeneFile = GisticRefgeneFileEnum.HUMAN_HG16;
    private Float amplificationsThreshold = DEFAULT_AMPLIFICATIONS;
    private Float deletionsThreshold = DEFAULT_DELETIONS;
    private Integer joinSegmentSize = 4;
    private Float qvThresh = DEFAULT_QV_THRESH;
    private String extension = ".gp_gistic";
    private Integer removeX = REMOVE_X_YES_VALUE;


    /**
     * @return the server
     */
    public ServerConnectionProfile getServer() {
        return server;
    }

    /**
     * 
     * @return options list for the "removeX" parameter.
     */
    public Map<Integer, String> getRemoveXOptions() {
        Map<Integer, String> options = new HashMap<Integer, String>();
        options.put(REMOVE_X_YES_VALUE, REMOVE_X_YES);
        options.put(REMOVE_X_NO_VALUE, REMOVE_X_NO);
        return options;
    }
    
    /**
     * Creates the parameter list from the parameters given by user.
     * @return parameters to run grid job.
     */
    public ParameterList createParameterList() {
        ParameterList parameterList = new ParameterList();
        Parameter[] params = new Parameter[6];
        params[0] = GenePatternUtil.createParameter("amplifications.threshold", amplificationsThreshold);
        params[1] = GenePatternUtil.createParameter("deletions.threshold", deletionsThreshold);
        params[2] = GenePatternUtil.createParameter("join.segment.size", joinSegmentSize);
        params[3] = GenePatternUtil.createParameter("qv.thresh", qvThresh);
        params[4] = GenePatternUtil.createParameter("extension", extension);
        params[5] = GenePatternUtil.createParameter("remove.X", removeX);
        
        parameterList.setParameterCollection(params);
        return parameterList;       
    }
    
    /**
     * The genomeBuild information.
     * @return the genomeBuild.
     */
    public GenomeAnnotationInformation createGenomeBuild() {
        GenomeAnnotationInformation genomeBuild = new GenomeAnnotationInformation();
        genomeBuild.setSource("NCBI"); // Unsure what this means.  Got it from the demo client.
        genomeBuild.setBuild(refgeneFile.getParameterValue());
        return genomeBuild;
    }

    /**
     * @return the amplificationThreshold
     */
    public Float getAmplificationsThreshold() {
        return amplificationsThreshold;
    }

    /**
     * @param amplificationsThreshold the amplificationThreshold to set
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
     * @return the qvThresh
     */
    public Float getQvThresh() {
        return qvThresh;
    }

    /**
     * @param qvThresh the qvThresh to set
     */
    public void setQvThresh(Float qvThresh) {
        this.qvThresh = qvThresh;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the removeX
     */
    public Integer getRemoveX() {
        return removeX;
    }

    /**
     * @param removeX the removeX to set
     */
    public void setRemoveX(Integer removeX) {
        this.removeX = removeX;
    }

    /**
     * @return the clinicalQuery
     */
    public Query getClinicalQuery() {
        return clinicalQuery;
    }

    /**
     * @param clinicalQuery the clinicalQuery to set
     */
    public void setClinicalQuery(Query clinicalQuery) {
        this.clinicalQuery = clinicalQuery;
    }

    /**
     * @return the cnvSegmentsToIgnoreFile
     */
    public File getCnvSegmentsToIgnoreFile() {
        return cnvSegmentsToIgnoreFile;
    }

    /**
     * @param cnvSegmentsToIgnoreFile the cnvSegmentsToIgnoreFile to set
     */
    public void setCnvSegmentsToIgnoreFile(File cnvSegmentsToIgnoreFile) {
        this.cnvSegmentsToIgnoreFile = cnvSegmentsToIgnoreFile;
    }

    /**
     * @return the refgeneFile
     */
    public GisticRefgeneFileEnum getRefgeneFile() {
        return refgeneFile;
    }

    /**
     * @param refgeneFile the refgeneFile to set
     */
    public void setRefgeneFile(GisticRefgeneFileEnum refgeneFile) {
        this.refgeneFile = refgeneFile;
    }

    /**
     * @return the excludeControlSampleSet
     */
    public SampleSet getExcludeControlSampleSet() {
        return excludeControlSampleSet;
    }

    /**
     * @param excludeControlSampleSet the excludeControlSampleSet to set
     */
    public void setExcludeControlSampleSet(SampleSet excludeControlSampleSet) {
        this.excludeControlSampleSet = excludeControlSampleSet;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append("--------------------------").append(nl);
        sb.append("Gistic Analysis Parameters").append(nl);
        sb.append("--------------------------").append(nl);
        if (excludeControlSampleSet != null) {
            sb.append("Excluded control samples: ").append(excludeControlSampleSet.getName()).append(nl);
        }
        if (clinicalQuery != null) {
            sb.append("Input query/list for classification: ").append(clinicalQuery.getName()).append(nl);
        }
        if (server != null) {
            sb.append(server.toString()).append(nl);
        }
        GenomeAnnotationInformation genomeBuild = createGenomeBuild();
        sb.append("Genome Build Information: ").append(genomeBuild.getSource()).append(", ").
                append(genomeBuild.getBuild()).append(nl);
        sb.append(GenePatternUtil.parameterListToString(createParameterList())).append(nl);
        return sb.toString();
    }
}
