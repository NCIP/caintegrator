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
