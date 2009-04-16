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
package gov.nih.nci.caintegrator2.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caarray.domain.AbstractCaArrayObject;
import gov.nih.nci.caarray.domain.array.Array;
import gov.nih.nci.caarray.domain.array.ArrayDesign;
import gov.nih.nci.caarray.domain.array.ArrayDesignDetails;
import gov.nih.nci.caarray.domain.array.LogicalProbe;
import gov.nih.nci.caarray.domain.data.DataRetrievalRequest;
import gov.nih.nci.caarray.domain.data.DataSet;
import gov.nih.nci.caarray.domain.data.DesignElementList;
import gov.nih.nci.caarray.domain.data.FloatColumn;
import gov.nih.nci.caarray.domain.data.HybridizationData;
import gov.nih.nci.caarray.domain.data.RawArrayData;
import gov.nih.nci.caarray.domain.file.CaArrayFile;
import gov.nih.nci.caarray.domain.hybridization.Hybridization;
import gov.nih.nci.caarray.domain.project.Experiment;
import gov.nih.nci.caarray.domain.sample.Extract;
import gov.nih.nci.caarray.domain.sample.LabeledExtract;
import gov.nih.nci.caarray.services.data.DataRetrievalService;
import gov.nih.nci.caarray.services.file.FileRetrievalService;
import gov.nih.nci.caarray.services.search.CaArraySearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.NoSamplesForExperimentException;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("PMD")
public class CaArrayFacadeTest {

    private CaArrayFacade caArrayFacade;
    private GeneExpressionReporter reporter1 = createTestReporter("A_probeSet1");
    private GeneExpressionReporter reporter2 = createTestReporter("A_probeSet2");
    
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("caarray-test-config.xml", CaArrayFacadeTest.class); 
        caArrayFacade = (CaArrayFacade) context.getBean("CaArrayFacade"); 
    }

    @Test
    public void testGetSamples() throws ConnectionException, ExperimentNotFoundException, NoSamplesForExperimentException {
        List<Sample> samples = caArrayFacade.getSamples("samples", null);
        assertFalse(samples.isEmpty());
        assertTrue(samples.get(0).getName().equals("sample1") || samples.get(0).getName().equals("sample2"));
        assertTrue(samples.get(1).getName().equals("sample1") || samples.get(1).getName().equals("sample2"));
        boolean experimentNotFound = false;
        try {
            samples = caArrayFacade.getSamples("no-experiment", null);   
        } catch (ExperimentNotFoundException e) {
            experimentNotFound = true;
        }
        assertTrue(experimentNotFound);
    }
    
    @Test
    public void testAffymetrixRetrieveData() throws ConnectionException, DataRetrievalException {
        RetrieveDataDaoStub daoStub = new RetrieveDataDaoStub();
        ((CaArrayFacadeImpl) caArrayFacade).setServiceFactory(new RetrieveDataServiceFactoryStub());
        ((CaArrayFacadeImpl) caArrayFacade).setDao(daoStub);
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setExperimentIdentifier("test-data");
        genomicSource.setPlatformVendor(PlatformVendorEnum.AFFYMETRIX.getValue());
        genomicSource.setStudyConfiguration(new StudyConfiguration());
        Sample sample1 = new Sample();
        Sample sample2 = new Sample();
        sample1.setName("sample1");
        sample2.setName("sample2");
        genomicSource.getSamples().add(sample1);
        genomicSource.getSamples().add(sample2);
        ArrayDataValues values = caArrayFacade.retrieveData(genomicSource);
        assertNotNull(values);
        assertEquals(2, values.getArrayDatas().size());
        for (ArrayData arrayData : values.getArrayDatas()) {
            assertEquals((float) 1.1, (float) values.getFloatValue(arrayData, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
            assertEquals((float) 2.2, (float) values.getFloatValue(arrayData, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
        }
    }
    
    @Test
    public void testAgilentRetrieveData() throws ConnectionException, DataRetrievalException {
        RetrieveDataDaoStub daoStub = new RetrieveDataDaoStub();
        ((CaArrayFacadeImpl) caArrayFacade).setServiceFactory(new RetrieveDataServiceFactoryStub());
        ((CaArrayFacadeImpl) caArrayFacade).setDao(daoStub);
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setExperimentIdentifier("test-data");
        genomicSource.setPlatformVendor(PlatformVendorEnum.AGILENT.getValue());
        genomicSource.setStudyConfiguration(new StudyConfiguration());
        Sample sample1 = new Sample();
        Sample sample2 = new Sample();
        sample1.setName("sample1");
        sample2.setName("sample2");
        genomicSource.getSamples().add(sample1);
        genomicSource.getSamples().add(sample2);
        ArrayDataValues values = caArrayFacade.retrieveData(genomicSource);
        assertNotNull(values);
        assertEquals(2, values.getArrayDatas().size());
        for (ArrayData arrayData : values.getArrayDatas()) {
            assertEquals((float) 0.05, (float) values.getFloatValue(arrayData, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
            assertEquals((float) -0.1234, (float) values.getFloatValue(arrayData, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
        }
    }

    private GeneExpressionReporter createTestReporter(String name) {
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.setName(name);
        return reporter;
    }

    private class RetrieveDataDaoStub extends CaIntegrator2DaoStub {
        
        private Platform platform = createTestPlatform();

        @Override
        public Platform getPlatform(String name) {
            return platform;
        }

        private Platform createTestPlatform() {
            Platform platform = new Platform();
            ReporterList reporters = new ReporterList();
            reporters.setId(1L);
            reporters.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            platform.getReporterLists().add(reporters);
            reporters.getReporters().add(reporter1);
            reporters.getReporters().add(reporter2);
            reporter1.setReporterList(reporters);
            reporter2.setReporterList(reporters);
            return  platform;
        }
        
    }

    private class RetrieveDataServiceFactoryStub extends ServiceStubFactory {

        private final ArrayDesign arrayDesign = createTestArrayDesign();
        LogicalProbe probeSet1 = createTestProbeSet("A_probeSet1");
        LogicalProbe probeSet2 = createTestProbeSet("A_probeSet2");

        @Override
        public CaArraySearchService createSearchService(ServerConnectionProfile profile) throws ConnectionException {
            return new RetrieveDataSearchServiceStub();
        }

        @SuppressWarnings("deprecation")
        private LogicalProbe createTestProbeSet(String name) {
            LogicalProbe probeSet = new LogicalProbe();
            probeSet.setName(name);
            return probeSet;
        }

        @Override
        public DataRetrievalService createDataRetrievalService(ServerConnectionProfile profile) {
            return new RetrieveDataDataRetrievalServiceStub();
        }

        @Override
        public FileRetrievalService createFileRetrievalService(ServerConnectionProfile profile)
                throws ConnectionException {
            return new RetrieveDataFileRetrievalServiceStub();
        }

        @SuppressWarnings("deprecation")
        private ArrayDesign createTestArrayDesign() {
            ArrayDesign design = new ArrayDesign();
            design.setName("test design");
            design.setDesignDetails(new ArrayDesignDetails());
            design.getDesignDetails().getLogicalProbes().add(probeSet1);
            design.getDesignDetails().getLogicalProbes().add(probeSet2);
            return design;
        }

        private class RetrieveDataDataRetrievalServiceStub implements DataRetrievalService {

            public DataSet getDataSet(DataRetrievalRequest request) {
                DataSet dataSet = new DataSet();
                dataSet.setDesignElementList(new DesignElementList());
                dataSet.getDesignElementList().getDesignElements().add(probeSet1);
                dataSet.getDesignElementList().getDesignElements().add(probeSet2);
                dataSet.getQuantitationTypes().addAll(request.getQuantitationTypes());
                for (Hybridization hybridization : request.getHybridizations()) {
                    HybridizationData hybridizationData = dataSet.addHybridizationData(hybridization);
                    FloatColumn column = new FloatColumn();
                    column.setQuantitationType(request.getQuantitationTypes().get(0));
                    column.initializeArray(2);
                    column.getValues()[0] = (float) 1.1;
                    column.getValues()[1] = (float) 2.2;
                    hybridizationData.getColumns().add(column);
                }
                return dataSet;
            }

        }

        private class RetrieveDataSearchServiceStub extends SearchServiceStub {
            
            private final gov.nih.nci.caarray.domain.sample.Sample sample1 = creatTestSample("sample1");
            private final gov.nih.nci.caarray.domain.sample.Sample sample2 = creatTestSample("sample2");
            private final Experiment experiment = createTestDataExperiment();
            
            @SuppressWarnings("unchecked")
            public <T extends AbstractCaArrayObject> List<T> search(T searchObject) {
                List<T> results = new ArrayList<T>();
                if (searchObject instanceof Experiment && ("test-data".equals(((Experiment) searchObject).getPublicIdentifier()))) {
                    results.add((T) experiment);
                } else if (searchObject instanceof gov.nih.nci.caarray.domain.sample.Sample) {
                    results.add((T) getSample((gov.nih.nci.caarray.domain.sample.Sample) searchObject));
                } else {
                    results.add(searchObject);
                }
                return results;
            }

            private gov.nih.nci.caarray.domain.sample.Sample creatTestSample(String name) {
                gov.nih.nci.caarray.domain.sample.Sample sample = new gov.nih.nci.caarray.domain.sample.Sample();
                sample.setName(name);
                Hybridization hybridization = new Hybridization();
                hybridization.setName("hybridiation " + name);
                hybridization.setArray(new Array());
                hybridization.getArray().setDesign(arrayDesign);
                RawArrayData rawArrayData = new RawArrayData();
                rawArrayData.setDataFile(new CaArrayFile());
                hybridization.addRawArrayData(rawArrayData);
                LabeledExtract labeledExtract = new LabeledExtract();
                hybridization.getLabeledExtracts().add(labeledExtract);
                labeledExtract.getHybridizations().add(hybridization);
                Extract extract = new Extract();
                extract.getLabeledExtracts().add(labeledExtract);
                labeledExtract.getExtracts().add(extract);
                extract.getSamples().add(sample);
                sample.getExtracts().add(extract);
                return sample;
            }

            private gov.nih.nci.caarray.domain.sample.Sample getSample(gov.nih.nci.caarray.domain.sample.Sample searchSample) {
                if ("sample1".equals(searchSample.getName())) {
                    return sample1;
                } else if ("sample2".equals(searchSample.getName())) {
                    return sample2;
                } else {
                    return searchSample;
                }
            }

            private Experiment createTestDataExperiment() {
                Experiment experiment = new Experiment();
                experiment.getSamples().add(sample1);
                experiment.getSamples().add(sample2);
                return experiment;
            }
        }
    }
    
    private class RetrieveDataFileRetrievalServiceStub implements FileRetrievalService {

        public byte[] readFile(CaArrayFile arg0) {
            StringBuffer dataFile = new StringBuffer();
            dataFile.append("TYPE\ttext\ttext\n");
            dataFile.append("\n");
            dataFile.append("FEATURES\tProbeName\tLogRatio\n");
            dataFile.append("\n");
            dataFile.append("DATA\tA_probeSet1\t0.05\n");
            dataFile.append("DATA\tDarkCorner\t-0.0034\n");
            dataFile.append("*\n");
            dataFile.append("DATA\tA_probeSet2\t-0.1234\n");
            return dataFile.toString().getBytes();
        }

    }

}
