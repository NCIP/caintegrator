/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.TaskInfo;
import org.genepattern.webservice.WebServiceException;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;

/**
 * Provides methods for working with GenePattern.
 */
class GenePatternHelper {

    private static final String TYPE_ATTRIBUTE = "type";
    private static final String STRING_TYPE = "java.lang.String";
    private static final String INTEGER_TYPE = "java.lang.Integer";
    private static final String PASSWORD_TYPE = "PASSWORD";
    private static final String FLOAT_TYPE = "java.lang.Float";
    private static final String FILE_TYPE = "java.io.File";
    private static final String GENE_CLUSTER_TEXT_EXTENSION = "gct";
    private static final String CLASS_EXTENSION = "cls";
    private static final String FILE_FORMAT_ATTRIBUTE = "fileFormat";
    private static final String OPTIONAL_ON = "on";
    private static final String OPTIONAL_ATTRIBUTE = "optional";
    private static final String TASK_TYPE_ATTRIBUTE = "taskType";
    private static final String VISUALIZER_TASK_TYPE = "Visualizer";

    private final CaIntegrator2GPClient client;
    private static int tempFileCounter = 0;

    GenePatternHelper(CaIntegrator2GPClient client) {
        this.client = client;
    }

    List<AnalysisMethod> getMethods() throws WebServiceException {
        TaskInfo[] allTasks = client.getTasks();
        List<AnalysisMethod> methods = new ArrayList<AnalysisMethod>();
        for (TaskInfo task : allTasks) {
            if (isSupportedTask(task)) {
                methods.add(convert(task));
            }
        }
        return methods;
    }

    /**
     * Checks a task to ensure that all input file parameters are either GCT files or CLS files preceded by
     * an associated GCT file.
     *
     * @param task the task to check
     * @return true if supported, false otherwise.
     */
    private boolean isSupportedTask(TaskInfo task) {
        return !isVisualizerTask(task) && areFileParametersSupported(task);
    }

    @SuppressWarnings("unchecked")  // Need to downcast getTaskInfoAttributes() from Object
    private boolean isVisualizerTask(TaskInfo task) {
        Map<String, String> taskAttributes = task.getTaskInfoAttributes();
        return VISUALIZER_TASK_TYPE.equals(taskAttributes.get(TASK_TYPE_ATTRIBUTE));
    }

    private boolean areFileParametersSupported(TaskInfo task) {
        boolean precededByGct = false;
        boolean hasGct = false;
        for (ParameterInfo parameterInfo : task.getParameterInfoArray()) {
            if (!isParameterValid(parameterInfo)) {
                return false;
            }
            if (isInputFileParameter(parameterInfo)) {
                if (getFileFormats(parameterInfo).contains(GENE_CLUSTER_TEXT_EXTENSION)) {
                    precededByGct = true;
                    hasGct = true;
                    continue;
                } else if (getFileFormats(parameterInfo).contains(CLASS_EXTENSION) && !precededByGct) {
                    return false;
                } else if (getFileFormats(parameterInfo).contains(CLASS_EXTENSION) && precededByGct) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return hasGct;
    }

    private boolean isParameterValid(ParameterInfo parameterInfo) {
        if (INTEGER_TYPE.equals(getAttributeValue(parameterInfo, TYPE_ATTRIBUTE))
            && !StringUtils.isEmpty(parameterInfo.getValue())) {
            String[] choiceEntries = parameterInfo.getValue().split(";");
            for (String choiceEntry : choiceEntries) {
                String[] parts = choiceEntry.split("=", 2);
                if (parts.length >= 1 && !StringUtils.isNumeric(parts[0])) {
                        return false;
               }
            }
        }
        if (getAttributeValue(parameterInfo, TYPE_ATTRIBUTE) == null) {
            return false;
        }
        return true;
    }

    private Set<String> getFileFormats(ParameterInfo parameterInfo) {
        String fileFormatValue = getAttributeValue(parameterInfo, FILE_FORMAT_ATTRIBUTE);
        Set<String> formats = new HashSet<String>();
        if (fileFormatValue != null) {
            formats.addAll(Arrays.asList(fileFormatValue.split("[;,]")));
        }
        return formats;
    }

    private boolean isInputFileParameter(ParameterInfo parameterInfo) {
        return FILE_TYPE.equals(getAttributeValue(parameterInfo, TYPE_ATTRIBUTE));
    }

    private AnalysisMethod convert(TaskInfo task) {
        AnalysisMethod method = new AnalysisMethod();
        method.setServiceType(AnalysisServiceType.GENEPATTERN);
        method.setName(task.getName());
        method.setDescription(task.getDescription());
        for (ParameterInfo parameterInfo : task.getParameterInfoArray()) {
            method.getParameters().add(convert(parameterInfo));
        }
        return method;
    }

    private AnalysisParameter convert(ParameterInfo parameterInfo) {
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setName(parameterInfo.getName());
        parameter.setDescription(parameterInfo.getDescription());
        parameter.setType(getType(parameterInfo));
        parameter.setRequired(!OPTIONAL_ON.equalsIgnoreCase(getAttributeValue(parameterInfo, OPTIONAL_ATTRIBUTE)));
        if (!StringUtils.isBlank(parameterInfo.getValue())) {
            loadChoices(parameter, parameterInfo);
        }
        if (!StringUtils.isBlank(parameterInfo.getDefaultValue())) {
            setDefaultValue(parameterInfo, parameter);
        }
        return parameter;
    }

    private void loadChoices(AnalysisParameter parameter, ParameterInfo parameterInfo) {
        String[] choiceEntries = parameterInfo.getValue().split(";");
        for (String choiceEntry : choiceEntries) {
            String[] parts = choiceEntry.split("=", 2);
            String value;
            String key;
            if (parts.length == 0) {
                key = "";
                value = "";
            } else if (parts.length == 1) {
                value = parts[0];
                key = value;
            } else {
                value = parts[0];
                key = parts[1];
            }
            parameter.addChoice(key, value);
        }
    }

    private void setDefaultValue(ParameterInfo parameterInfo, AnalysisParameter parameter) {
        if (parameter.getChoiceKeys().isEmpty()) {
            parameter.setDefaultValue(parameter.createValue());
            parameter.getDefaultValue().setValueFromString(parameterInfo.getDefaultValue());
        } else {
            parameter.setDefaultValue(getMatchFromChoices(parameterInfo.getDefaultValue(), parameter));
        }
    }

    private AbstractParameterValue getMatchFromChoices(String stringValue, AnalysisParameter parameter) {
        for (String key : parameter.getChoiceKeys()) {
            AbstractParameterValue value = parameter.getChoice(key);
            if (stringValue.equals(value.getValueAsString())) {
                return value;
            }
        }
        return parameter.getChoice(stringValue);
    }

    private AnalysisParameterType getType(ParameterInfo parameterInfo) {
        String genePatternType = getAttributeValue(parameterInfo, TYPE_ATTRIBUTE);
        if (STRING_TYPE.equals(genePatternType)) {
            return AnalysisParameterType.STRING;
        } else if (INTEGER_TYPE.equals(genePatternType)) {
            return AnalysisParameterType.INTEGER;
        } else if (PASSWORD_TYPE.equals(genePatternType)) {
            return AnalysisParameterType.STRING;
        } else if (FLOAT_TYPE.equals(genePatternType)) {
            return AnalysisParameterType.FLOAT;
        } else if (isInputFileParameter(parameterInfo)
                && getFileFormats(parameterInfo).contains(GENE_CLUSTER_TEXT_EXTENSION)) {
            return AnalysisParameterType.GENOMIC_DATA;
        } else if (isInputFileParameter(parameterInfo)
                && getFileFormats(parameterInfo).contains(CLASS_EXTENSION)) {
            return AnalysisParameterType.SAMPLE_CLASSIFICATION;
        } else {
            throw new IllegalArgumentException("Unsupported GenePattern parameter type " + genePatternType
                    + " for parameter " + parameterInfo.getName());
        }
    }

    private String getAttributeValue(ParameterInfo parameterInfo, String attributeName) {
        return (String) parameterInfo.getAttributes().get(attributeName);
    }

    JobInfoWrapper execute(AnalysisMethodInvocation invocation) throws WebServiceException {
        JobInfoWrapper jobInfo = new JobInfoWrapper();
        List<ParameterInfo> parameters = convert(invocation.getParameterValues());
        jobInfo.setJobInfo(client.runAnalysis(invocation.getMethod().getName(), parameters));
        return jobInfo;
    }

    private List<ParameterInfo> convert(List<AbstractParameterValue> parameterValues) {
        List<ParameterInfo> genePatternParameters = new ArrayList<ParameterInfo>();
        for (AbstractParameterValue parameterValue : parameterValues) {
            genePatternParameters.add(convert(parameterValue));
        }
        return genePatternParameters;
    }

    private ParameterInfo convert(AbstractParameterValue parameterValue) {
        ParameterInfo genePatternParameter = new ParameterInfo();
        genePatternParameter.setName(parameterValue.getParameter().getName());
        if (AnalysisParameterType.GENOMIC_DATA.equals(parameterValue.getParameter().getType())) {
            handleGenomicData((GenomicDataParameterValue) parameterValue, genePatternParameter);
        } else if (AnalysisParameterType.SAMPLE_CLASSIFICATION.equals(parameterValue.getParameter().getType())) {
            handleSampleClassification((SampleClassificationParameterValue) parameterValue, genePatternParameter);
        } else {
            genePatternParameter.setValue(parameterValue.getValueAsString());
        }
        return genePatternParameter;
    }

    private void handleSampleClassification(SampleClassificationParameterValue parameterValue,
            ParameterInfo genePatternParameter) {
        if (!parameterValue.getClassifications().isEmpty()) {
            File clsFile =
                new File(System.getProperty("java.io.tmpdir"), "caintegrator2_job" + tempFileCounter++ + ".cls");
            clsFile.deleteOnExit();
            ClassificationsToClsConverter.writeAsCls(parameterValue, clsFile.getAbsolutePath());
            genePatternParameter.setValue(clsFile.getAbsolutePath());
        }
    }

    private void handleGenomicData(GenomicDataParameterValue parameterValue, ParameterInfo genePatternParameter) {
        File gctFile =
            new File(System.getProperty("java.io.tmpdir"), "caintegrator2_job" + tempFileCounter++ + ".gct");
        gctFile.deleteOnExit();
        GctDatasetFileWriter.writeAsGct(new GctDataset(parameterValue.getGenomicData()),
                                        gctFile.getAbsolutePath());
        genePatternParameter.setValue(gctFile.getAbsolutePath());
    }

}
