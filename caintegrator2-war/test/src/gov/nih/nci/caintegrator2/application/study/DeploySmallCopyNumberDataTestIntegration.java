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
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixDnaPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.security.exceptions.CSException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional(timeout = 2880)
public class DeploySmallCopyNumberDataTestIntegration extends AbstractDeployStudyTestIntegration {
    
    private final static Logger LOGGER = Logger.getLogger(DeploySmallCopyNumberDataTestIntegration.class);

    @Test
    public void testDeployStudy() throws ValidationException, IOException, ConnectionException, PlatformLoadingException, DataRetrievalException, ExperimentNotFoundException, InvalidCriterionException, CSException {
        deployStudy();
        checkCopyNumberData();
    }

    @Override
    protected void configureSegmentationDataCalcuation(CopyNumberDataConfiguration copyNumberDataConfiguration) {
        copyNumberDataConfiguration.getCaDNACopyService().setUrl("http://ncias-d227-v.nci.nih.gov:8080/wsrf/services/cagrid/CaDNAcopy");
        copyNumberDataConfiguration.setRandomNumberSeed(1234567);
    }

    private void checkCopyNumberData() {
        Set<ArrayData> arrayDatas = getStudyConfiguration().getStudy().getArrayDatas(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        assertEquals(1, arrayDatas.size());
        for (ArrayData arrayData : arrayDatas) {
            checkCopyNumberData(arrayData);
        }
    }

    private void checkCopyNumberData(ArrayData arrayData) {
        PlatformHelper platformHelper = new PlatformHelper(arrayData.getArray().getPlatform());
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addType(ArrayDataValueType.COPY_NUMBER_LOG2_RATIO);
        request.addArrayData(arrayData);
        request.addReporters(platformHelper.getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
        ArrayDataValues values = getArrayDataService().getData(request);
        int nonZeroValueCount = 0;
        for (AbstractReporter reporter : values.getReporters()) {
            if (values.getFloatValue(arrayData, reporter, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO) != 0) {
                nonZeroValueCount++;
            }
        }
        assertTrue(nonZeroValueCount > 50000);
        assertFalse(arrayData.getSegmentDatas().isEmpty());
    }

    @Override
    protected boolean getMapImages() {
        return false;
    }
    
    @Override
    protected boolean getLoadImages() {
        return false;
    }
    
    @Override
    protected boolean getLoadImageAnnotation() {
        return false;
    }

    @Override
    protected boolean getLoadDesign() {
        return true;
    }
    
    @Override
    protected boolean getLoadSamples() {
        return true;
    }
    
    @Override
    protected String getCaArrayId() {
        return "jagla-00034";
    }

    @Override
    protected int getExpectedSampleCount() {
        return 3;
    }

    @Override
    protected int getExpectedMappedSampleCount() {
        return 2;
    }

    @Override
    protected int getExpectedControlSampleCount() {
        return 1;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getStudyName() {
        return "Rembrandt with Small Copy Number Data";
    }
    
    @Override
    protected File getCopyNumberFile() {
        return TestDataFiles.SHORT_COPY_NUMBER_FILE;
    }
    
    @Override
    protected AbstractPlatformSource[] getAdditionalPlatformSources() {
        List<File> files = new ArrayList<File>();
        files.add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        files.add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_FILE);
        return new AbstractPlatformSource[] {
                new AffymetrixDnaPlatformSource(files, "GeneChip Human Mapping 100K Set")
        };
    }
    
    @Override
    protected String getCopyNumberCaArrayId() {
        return "liu-00252";
    }

    @Override
    protected File getAnnotationDefinitionsFile() {
        return TestDataFiles.REMBRANDT_ANNOTATION_DEFINITIONS_FILE;
    }


    @Override
    protected File getSampleMappingFile() {
        return TestDataFiles.REMBRANDT_NCRI_SAMPLE_MAPPING_FILE;
    }

    @Override
    protected String getControlSampleSetName() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_SET_NAME;
    }

    @Override
    protected File getControlSamplesFile() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_FILE;
    }

    @Override
    protected String getControlSamplesFileName() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_FILE_PATH;
    }

    @Override
    protected File getSubjectAnnotationFile() {
        return TestDataFiles.REMBRANDT_NCRI_CLINICAL_FILE;
    }

    @Override
    protected AbstractPlatformSource getPlatformSource() {
        return new AffymetrixExpressionPlatformSource(TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE);
    }
    
    @Override
    protected String getDeathDateName() {
        return "Death Date";
    }

    @Override
    protected String getLastFollowupDateName() {
        return "Last Followup Date";
    }

    @Override
    protected String getSurvivalStartDateName() {
        return "Survival Start Date";
    }

    @Override
    protected int getExpectedNumberOfGeneReporters() {
        return 21432;
    }

    @Override
    protected int getExpectedNumberProbeSets() {
        return 54675;
    }

    @Override
    protected String getPlatformVendor() {
        return "Affymetrix";
    }

    @Override
    protected File getImageAnnotationFile() {
        return null;
    }

    @Override
    protected File getImageMappingFile() {
        return null;
    }

    @Override
    protected String getNCIAServerUrl() {
        return null;
    }

    @Override
    protected String getNCIATrialId() {
        return null;
    }

}
