/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.preprocess;

import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gridextensions.PreprocessDatasetParameterSet;

import java.util.ArrayList;
import java.util.List;

import org.cabig.icr.asbp.parameter.Parameter;
import org.cabig.icr.asbp.parameter.ParameterList;
import org.springframework.util.StringUtils;

import valuedomain.PreprocessDatasetPreprocessingFlag;

/**
 * Parameters used for PreprocessDataset gene pattern grid service.
 */
public class PreprocessDatasetParameters {
    private static final int NUM_PARAMETERS = 11;
    private static final Float DEFAULT_MIN_CHANGE = 3f;
    private static final Float DEFAULT_MIN_DELTA = 100f;
    private static final Float DEFAULT_THRESHOLD = 20f;
    private static final Float DEFAULT_CEILING = 2.1f;
    private static final Float DEFAULT_PROBABILITY_THRESHOLD = 1f;
    
    private final PreprocessDatasetParameterSet datasetParameters;
    private final List<Query> clinicalQueries = new ArrayList<Query>();
    private SampleSet excludedControlSampleSet;
    private ServerConnectionProfile server;
    private String processedGctFilename;
    
    /**
     * Constructor.
     */
    public PreprocessDatasetParameters() {
        datasetParameters = new PreprocessDatasetParameterSet();
        fillDefaultDatasetParameters();
    }
    
    private void fillDefaultDatasetParameters() {
        datasetParameters.setFilterFlag(false);
        datasetParameters.setPreprocessingFlag(PreprocessDatasetPreprocessingFlag.value2);
        datasetParameters.setMinChange(DEFAULT_MIN_CHANGE);
        datasetParameters.setMinDelta(DEFAULT_MIN_DELTA);
        datasetParameters.setThreshold(DEFAULT_THRESHOLD);
        datasetParameters.setCeiling(DEFAULT_CEILING);
        datasetParameters.setMaxSigmaBinning(1);
        datasetParameters.setProbabilityThreshold(DEFAULT_PROBABILITY_THRESHOLD);
        datasetParameters.setNumExclude(0);
        datasetParameters.setLogBaseTwo(false);
        datasetParameters.setNumberOfColumnsAboveThreshold(1);
    }
    
    /**
     * @return the clinicalQueries
     */
    public List<Query> getClinicalQueries() {
        return clinicalQueries;
    }

    /**
     * @return the datasetParameters
     */
    public PreprocessDatasetParameterSet getDatasetParameters() {
        return datasetParameters;
    }

    /**
     * @return the processedGctFilename
     */
    public String getProcessedGctFilename() {
        return processedGctFilename;
    }

    /**
     * @param processedGctFilename the processedGctFilename to set
     */
    public void setProcessedGctFilename(String processedGctFilename) {
        this.processedGctFilename = processedGctFilename;
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
     * @return the preprocessingFlag
     */
    public String getPreprocessingFlag() {
        return datasetParameters.getPreprocessingFlag().getValue();
    }

    /**
     * @param value of the preprocessingFlag to set
     */
    public void setPreprocessingFlag(String value) {
        datasetParameters.setPreprocessingFlag(PreprocessDatasetPreprocessingFlag.fromString(value));
    }
    
    /**
     * @return PreprocessingFlag options.
     */
    public List<String> getPreprocessingFlagOptions() {
        List<String> options = new ArrayList<String>();
        options.add(PreprocessDatasetPreprocessingFlag._value1);
        options.add(PreprocessDatasetPreprocessingFlag._value2);
        options.add(PreprocessDatasetPreprocessingFlag._value3);
        return options;
    }
    
    /**
     * Creates the parameter list from the parameters given by user.
     * @return parameters to run grid job.
     */
    public ParameterList createParameterList() {
        ParameterList parameterList = new ParameterList();
        Parameter[] params = new Parameter[NUM_PARAMETERS];
        parameterList.setParameterCollection(params);
        params[0] = GenePatternUtil.createBoolean0or1Parameter("filter.flag", datasetParameters.isFilterFlag());
        params[1] = GenePatternUtil.createParameter("preprocessing.flag", getPreprocessingOptionNumber());
        params[2] = GenePatternUtil.createParameter("minchange", String.valueOf(datasetParameters.getMinChange()));
        params[3] = GenePatternUtil.createParameter("mindelta", String.valueOf(datasetParameters.getMinDelta()));
        params[4] = GenePatternUtil.createParameter("threshold", String.valueOf(datasetParameters.getThreshold()));
        params[5] = GenePatternUtil.createParameter("ceiling", String.valueOf(datasetParameters.getCeiling()));
        params[6] = GenePatternUtil.createParameter("max.sigma.binning", String.valueOf(datasetParameters
                .getMaxSigmaBinning()));
        params[7] = GenePatternUtil.createParameter("prob.thres", String.valueOf(datasetParameters
                .getProbabilityThreshold()));
        params[8] = GenePatternUtil.createParameter("num.excl", String.valueOf(datasetParameters.getNumExclude())); 
        params[9] = GenePatternUtil.createParameter("log.base.two", String.valueOf(datasetParameters.isLogBaseTwo()));
        params[10] = GenePatternUtil.createParameter("number.of.columns.above.threshold", String
                .valueOf(datasetParameters.getNumberOfColumnsAboveThreshold()));

        return parameterList;
    }

    /**
     * Because of how odd the mapping is, have to decode it here.
     * no-disc    = value2    = 0
     * discretize = value1    = 1
     * row-norm   = value3    = 2
     * @return the int value representation.
     */
    private String getPreprocessingOptionNumber() {
        String preprocessingFlag = datasetParameters.getPreprocessingFlag().getValue();
        if (PreprocessDatasetPreprocessingFlag.value1.getValue().equals(preprocessingFlag)) {
            return "1";
        }
        if (PreprocessDatasetPreprocessingFlag.value2.getValue().equals(preprocessingFlag)) {
            return "0";
        }
        if (PreprocessDatasetPreprocessingFlag.value3.getValue().equals(preprocessingFlag)) {
            return "2";
        }
        return "0";
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append("-----------------------------").append(nl);
        sb.append("Preprocess Dataset Parameters").append(nl);
        sb.append("-----------------------------").append(nl);
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
            sb.append("Input queries for classification: ").append(
                    StringUtils.arrayToDelimitedString(queryNames, ", ")).append(nl);
        }
        if (server != null) {
            sb.append(server.toString()).append(nl);
        }
        sb.append(GenePatternUtil.parameterListToString(createParameterList())).append(nl);
        return sb.toString();
    }
}
