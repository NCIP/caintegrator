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
