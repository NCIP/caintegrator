/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid.pca;

import gov.nih.nci.caintegrator.common.GenePatternUtil;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.List;

import org.cabig.icr.asbp.parameter.Parameter;
import org.cabig.icr.asbp.parameter.ParameterList;
import org.cabig.icr.asbp.parameter.StringParameter;
import org.springframework.util.StringUtils;

/**
 * Parameters to run PCA Gene Pattern grid service.
 */
public class PCAParameters {
    
    private final List<Query> clinicalQueries = new ArrayList<Query>();
    private SampleSet excludedControlSampleSet;
    private ServerConnectionProfile server;
    private String clusterBy = getClusterByOptions().get(1); // rows is default
    private String classificationFileName;
    private String gctFileName;
    private String platformName = null;
    

    /**
     * @return the clinicalQueries
     */
    public List<Query> getClinicalQueries() {
        return clinicalQueries;
    }

    /**
     * @return the server
     */
    public ServerConnectionProfile getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(ServerConnectionProfile server) {
        this.server = server;
    }

    /**
     * 
     * @return options list for the "cluster.by" parameter.
     */
    public List<String> getClusterByOptions() {
        List<String> options = new ArrayList<String>();
        options.add("columns");
        options.add("rows");
        return options;
    }

    /**
     * @return the clusterBy
     */
    public String getClusterBy() {
        return clusterBy;
    }

    /**
     * @param clusterBy the clusterBy to set
     */
    public void setClusterBy(String clusterBy) {
        this.clusterBy = clusterBy;
    }
    
    /**
     * Creates the parameter list from the parameters given by user.
     * @return parameters to run grid job.
     */
    public ParameterList createParameterList() {
        ParameterList parameterList = new ParameterList();
        Parameter[] params = new Parameter[1];
        StringParameter param = new StringParameter();
        param.setName("cluster.by");
        param.setValue(clusterBy);
        params[0] = param;

        parameterList.setParameterCollection(params);
        return parameterList;       
    }

    /**
     * @return the classificationFileName
     */
    public String getClassificationFileName() {
        return classificationFileName;
    }

    /**
     * @param classificationFileName the classificationFileName to set
     */
    public void setClassificationFileName(String classificationFileName) {
        this.classificationFileName = classificationFileName;
    }

    /**
     * @return the gctFileName
     */
    public String getGctFileName() {
        return gctFileName;
    }

    /**
     * @param gctFileName the gctFileName to set
     */
    public void setGctFileName(String gctFileName) {
        this.gctFileName = gctFileName;
    }

    /**
     * @return the excludedControlSampleSet
     */
    public SampleSet getExcludedControlSampleSet() {
        return excludedControlSampleSet;
    }

    /**
     * @param excludedControlSampleSet the excludedControlSampleSet to set
     */
    public void setExcludedControlSampleSet(SampleSet excludedControlSampleSet) {
        this.excludedControlSampleSet = excludedControlSampleSet;
    }
    
    /**
     * @return the platformName
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * @param platformName the platformName to set
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append("---------------------------------------").append(nl);
        sb.append("Principal Component Analysis Parameters").append(nl);
        sb.append("---------------------------------------").append(nl);
        if (excludedControlSampleSet != null) {
            sb.append("Excluded control samples: ").append(excludedControlSampleSet.getName()).append(nl);
        }
        if (!clinicalQueries.isEmpty()) {
            String[] queryNames = new String[clinicalQueries.size()];
            int count = 0;
            for (Query query : clinicalQueries) {
                queryNames[count] = query.getName();
                count++;
            }
            sb.append("Input queries/lists for classification: ").append(
                    StringUtils.arrayToDelimitedString(queryNames, ", ")).append(nl);
        }
        if (server != null) {
            sb.append(server.toString()).append(nl);
        }
        sb.append(GenePatternUtil.parameterListToString(createParameterList())).append(nl);
        return sb.toString();
    }

}
