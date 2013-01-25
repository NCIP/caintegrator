/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.comparativemarker;

import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gridextensions.ComparativeMarkerSelectionParameterSet;

import java.util.ArrayList;
import java.util.List;

import org.cabig.icr.asbp.parameter.Parameter;
import org.cabig.icr.asbp.parameter.ParameterList;
import org.springframework.util.StringUtils;

import valuedomain.ComparativeMarkerSelectionPhenotypeTest;
import valuedomain.ComparativeMarkerSelectionTestDirection;
import valuedomain.ComparativeMarkerSelectionTestStatistic;

/**
 * 
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See function getTestStatisticForParamList()
public class ComparativeMarkerSelectionParameters {
    
    private static final Integer DEFAULT_NUMBER_OF_PERMUTATIONS = 1000;
    private static final Integer DEFAULT_RANDOM_SEED = 779948241;
    
    private final List<Query> clinicalQueries = new ArrayList<Query>();
    private final ComparativeMarkerSelectionParameterSet datasetParameters;
    private ServerConnectionProfile server;
    private String classificationFileName;
    
    /**
     * Constructor.
     */
    public ComparativeMarkerSelectionParameters() {
        datasetParameters = new ComparativeMarkerSelectionParameterSet();
        fillDefaultDatasetParameters();
    }
    
    private void fillDefaultDatasetParameters() {
        datasetParameters.setTestDirection(ComparativeMarkerSelectionTestDirection.value1);
        datasetParameters.setTestStatistic(ComparativeMarkerSelectionTestStatistic.value5);
        datasetParameters.setMinStd(1f);
        datasetParameters.setNumberOfPermutations(DEFAULT_NUMBER_OF_PERMUTATIONS);
        datasetParameters.setComplete(false);
        datasetParameters.setBalanced(false);
        datasetParameters.setRandomSeed(DEFAULT_RANDOM_SEED);
        datasetParameters.setSmoothPvalues(false);
        datasetParameters.setPhenotypeTest(ComparativeMarkerSelectionPhenotypeTest.value2);
    }

    /**
     * @return the clinicalQueries
     */
    public List<Query> getClinicalQueries() {
        return clinicalQueries;
    }
    /**
     * @return the datasetParameterSet
     */
    public ComparativeMarkerSelectionParameterSet getDatasetParameters() {
        return datasetParameters;
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
     * @return the testDirection
     */
    public String getTestDirection() {
        return datasetParameters.getTestDirection().getValue();
    }

    /**
     * @param value of the testDirection to set
     */
    public void setTestDirection(String value) {
        datasetParameters.setTestDirection(ComparativeMarkerSelectionTestDirection.fromString(value));
    }
    
    /**
     * @return ComparativeMarkerSelectionTestDirection options.
     */
    public List<String> getTestDirectionOptions() {
        List<String> options = new ArrayList<String>();
        options.add(ComparativeMarkerSelectionTestDirection._value1);
        options.add(ComparativeMarkerSelectionTestDirection._value2);
        options.add(ComparativeMarkerSelectionTestDirection._value3);
        return options;
    }

    /**
     * @return the testStatistic
     */
    public String getTestStatistic() {
        return datasetParameters.getTestStatistic().getValue();
    }

    /**
     * @param value of the testStatistic to set
     */
    public void setTestStatistic(String value) {
        datasetParameters.setTestStatistic(ComparativeMarkerSelectionTestStatistic.fromString(value));
    }
    
    /**
     * @return ComparativeMarkerSelectionTestDirection options.
     */
    public List<String> getTestStatisticOptions() {
        List<String> options = new ArrayList<String>();
        options.add(ComparativeMarkerSelectionTestStatistic._value1);
        options.add(ComparativeMarkerSelectionTestStatistic._value2);
        options.add(ComparativeMarkerSelectionTestStatistic._value3);
        options.add(ComparativeMarkerSelectionTestStatistic._value4);
        options.add(ComparativeMarkerSelectionTestStatistic._value5);
        options.add(ComparativeMarkerSelectionTestStatistic._value6);
        options.add(ComparativeMarkerSelectionTestStatistic._value7);
        options.add(ComparativeMarkerSelectionTestStatistic._value8);
        return options;
    }

    /**
     * @return the phenotypeTest
     */
    public String getPhenotypeTest() {
        return datasetParameters.getPhenotypeTest().getValue();
    }

    /**
     * @param value of the phenotypeTest to set
     */
    public void setPhenotypeTest(String value) {
        datasetParameters.setPhenotypeTest(ComparativeMarkerSelectionPhenotypeTest.fromString(value));
    }
    
    /**
     * @return ComparativeMarkerSelectionTestDirection options.
     */
    public List<String> getPhenotypeTestOptions() {
        List<String> options = new ArrayList<String>();
        options.add(ComparativeMarkerSelectionPhenotypeTest._value1);
        options.add(ComparativeMarkerSelectionPhenotypeTest._value2);
        return options;
    }
    
    /**
     * Creates the parameter list from the parameters given by user.
     * @return parameters to run grid job.
     */
    public ParameterList createParameterList() {
        ParameterList parameterList = new ParameterList();
        Parameter[] params = new Parameter[8];
        params[0] = GenePatternUtil.createParameter("test.direction", 
                                        getTestDirectionForParamList(datasetParameters.getTestDirection()));
        params[1] = GenePatternUtil.createParameter("test.statistic", 
                                        getTestStatisticForParamList(datasetParameters.getTestStatistic()));
        params[2] = GenePatternUtil.createParameter("min.std", String.valueOf(datasetParameters.getMinStd()));
        params[3] = GenePatternUtil.createParameter("number.of.permutations", 
                                                    String.valueOf(datasetParameters.getNumberOfPermutations()));
        params[4] = GenePatternUtil.createParameter("complete", String.valueOf(datasetParameters.isComplete()));
        params[5] = GenePatternUtil.createParameter("balanced", String.valueOf(datasetParameters.isBalanced()));
        params[6] = GenePatternUtil.createParameter("random.seed", String.valueOf(datasetParameters.getRandomSeed()));
        params[7] = GenePatternUtil.createParameter("smooth.p.values", 
                                                    String.valueOf(datasetParameters.isSmoothPvalues()));
        
        parameterList.setParameterCollection(params);
        return parameterList;       
    }

    private String getTestDirectionForParamList(ComparativeMarkerSelectionTestDirection direction) {
        // This extremely random mapping comes from the class 
        // ComparativeMarkerSelMAGESvcImpl.parameterSetToParameterList() code.
        if (ComparativeMarkerSelectionTestDirection.value2.equals(direction)) {
            return "0";
        } else if (ComparativeMarkerSelectionTestDirection.value3.equals(direction)) {
            return "1";
        } else if (ComparativeMarkerSelectionTestDirection.value1.equals(direction)) {
            return "2";    
        }
        throw new IllegalArgumentException("Unknown direction type");
    }
    
    @SuppressWarnings("PMD.CyclomaticComplexity") // Converting CMS objects to string numbers
    private String getTestStatisticForParamList(ComparativeMarkerSelectionTestStatistic statistic) {
        // This extremely random mapping comes from the class 
        // ComparativeMarkerSelMAGESvcImpl.parameterSetToParameterList() code.
        if (ComparativeMarkerSelectionTestStatistic.value1 == statistic) {
            return "1";
        } else if (ComparativeMarkerSelectionTestStatistic.value2 == statistic) {
            return "3";
        } else if (ComparativeMarkerSelectionTestStatistic.value3 == statistic) {
            return "6";
        } else if (ComparativeMarkerSelectionTestStatistic.value4 == statistic) {
            return "5";
        } else if (ComparativeMarkerSelectionTestStatistic.value5 == statistic) {
            return "0";
        } else if (ComparativeMarkerSelectionTestStatistic.value6 == statistic) {
            return "2";
        } else if (ComparativeMarkerSelectionTestStatistic.value7 == statistic) {
            return "7";
        } else if (ComparativeMarkerSelectionTestStatistic.value8 == statistic) {
            return "4";
        }
        throw new IllegalArgumentException("Unknown statistic type");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append("---------------------------------------").append(nl);
        sb.append("Comparative Marker Selection Parameters").append(nl);
        sb.append("---------------------------------------").append(nl);
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
