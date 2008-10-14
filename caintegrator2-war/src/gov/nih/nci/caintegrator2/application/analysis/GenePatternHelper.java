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

import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import edu.mit.broad.genepattern.gp.services.GenePatternServiceException;
import edu.mit.broad.genepattern.gp.services.ParameterInfo;
import edu.mit.broad.genepattern.gp.services.TaskInfo;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Provides methods for working with GenePattern.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // switch-like statement
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
    
    List<AnalysisMethod> getMethods(GenePatternClient client, ServerConnectionProfile server) 
    throws GenePatternServiceException {
        client.setUrl(server.getUrl());
        client.setUsername(server.getUsername());
        TaskInfo[] allTasks = client.getTasks();
        List<AnalysisMethod> methods = new ArrayList<AnalysisMethod>();
        for (TaskInfo task : allTasks) {
            if (isSupportedTask(task)) {
                methods.add(convert(task));
            }
        }
        return methods;
    }

    private boolean isSupportedTask(TaskInfo task) {
        int gctParameterCount = 0;
        int clsParameterCount = 0;
        for (ParameterInfo parameterInfo : task.getParameterInfoArray()) {
            if (isInputFileParameter(parameterInfo)) {
                if (getFileFormats(parameterInfo).contains(GENE_CLUSTER_TEXT_EXTENSION)) {
                    gctParameterCount++;
                    continue;
                } else if (getFileFormats(parameterInfo).contains(CLASS_EXTENSION)) {
                    clsParameterCount++;
                    continue;
                } else {
                    return false;
                }
            }
        }
        return gctParameterCount == 1 && clsParameterCount <= 1;
    }
    
    private Set<String> getFileFormats(ParameterInfo parameterInfo) {
        String fileFormatValue = getAttributeValue(parameterInfo, FILE_FORMAT_ATTRIBUTE);
        Set<String> formats = new HashSet<String>();
        if (fileFormatValue != null) {
            formats.addAll(Arrays.asList(fileFormatValue.split(";")));
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
        if (!parameterInfo.getChoices().isEmpty()) {
            loadChoices(parameter, parameterInfo);
        }
        if (!StringUtils.isBlank(parameterInfo.getDefaultValue())) {
            setDefaultValue(parameterInfo, parameter);
        }
        return parameter;
    }

    @SuppressWarnings("unchecked")  // HashMap in ParameterInfo is untyped
    private void loadChoices(AnalysisParameter parameter, ParameterInfo parameterInfo) {
        Map<String, String> sourceChoices = parameterInfo.getChoices();
        boolean useKeyForValue = sourceChoices.containsValue("");
        for (String name : sourceChoices.keySet()) {
            String stringValue;
            if (useKeyForValue) {
                stringValue = name;
            } else {
                stringValue = sourceChoices.get(name);
            }
            AbstractParameterValue value = parameter.createValue();
            value.setValueFromString(stringValue);
            parameter.getChoices().put(name, value);
        }
    }

    private void setDefaultValue(ParameterInfo parameterInfo, AnalysisParameter parameter) {
        if (parameter.getChoices().isEmpty()) {
            parameter.setDefaultValue(parameter.createValue());
            parameter.getDefaultValue().setValueFromString(parameterInfo.getDefaultValue());
        } else {
            parameter.setDefaultValue(getValue(parameterInfo.getDefaultValue(), parameter.getChoices()));
        }
    }

    private AbstractParameterValue getValue(String stringValue, Map<String, AbstractParameterValue> choices) {
        for (AbstractParameterValue value : choices.values()) {
            if (stringValue.equals(value.getValueAsString())) {
                return value;
            }
        }
        return choices.get(stringValue);
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")   // switch-like statement
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

}
