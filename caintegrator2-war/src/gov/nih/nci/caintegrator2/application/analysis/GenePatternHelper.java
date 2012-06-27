/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and
 * have distributed to and by third parties the caIntegrator2 Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do
 * not include such end-user documentation, You shall include this acknowledgment
 * in the Software itself, wherever such third-party acknowledgments normally
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software.
 * This License does not authorize You to use any trademarks, service marks,
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM,
 * except as required to comply with the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
