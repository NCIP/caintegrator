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
package gov.nih.nci.caintegrator2.application.study.deployment;

import static gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType.EXPRESSION_SIGNAL;
import gov.nih.nci.caintegrator2.application.analysis.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator2.application.analysis.GenePatternClientFactory;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.CopyNumberDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.CopyNumberData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.bioconductor.BioconductorService;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.genepattern.webservice.WebServiceException;

/**
 * Helper class that retrieves data from caArray and loads it into a study.
 */
class GenomicDataHelper {

    private static final double FIFTIETH_PERCENTILE = 50;
    private final CaArrayFacade caArrayFacade;
    private final ArrayDataService arrayDataService;
    private final CaIntegrator2Dao dao;
    private final BioconductorService bioconductorService;
    private final CopyNumberHandlerFactory copyNumberHandlerFactory;
    private ExpressionHandlerFactory expressionHandlerFactory;
    private GenePatternClientFactory genePatternClientFactory;

    GenomicDataHelper(CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao,
            BioconductorService bioconductorService, CopyNumberHandlerFactory copyNumberHandlerFactory) {
        this.caArrayFacade = caArrayFacade;
        this.arrayDataService = arrayDataService;
        this.dao = dao;
        this.bioconductorService = bioconductorService;
        this.copyNumberHandlerFactory = copyNumberHandlerFactory;
    }

    void loadData(StudyConfiguration studyConfiguration) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        for (GenomicDataSourceConfiguration genomicSource : studyConfiguration.getGenomicDataSources()) {
            if (genomicSource.isCopyNumberData()) {
                loadCopyNumberData(genomicSource);
            } else if (genomicSource.isExpressionData()) {
                loadExpressionData(genomicSource);
            }
        }
    }
    
    private void loadCopyNumberData(GenomicDataSourceConfiguration genomicSource)
    throws DataRetrievalException, ConnectionException, ValidationException {
        if (genomicSource.getCopyNumberDataConfiguration() != null) {
                handleCopyNumberData(genomicSource);
        }
    }
    
    private void loadExpressionData(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, DataRetrievalException, ValidationException {
        if (!genomicSource.getSamples().isEmpty()) {
            switch (PlatformVendorEnum.getByValue(genomicSource.getPlatformVendor())) {
            case AFFYMETRIX:
                loadAffymetrixExpressionData(genomicSource);
                break;
            case AGILENT:
                loadAgilentExpressionData(genomicSource);
                break;
            default:
                throw new DataRetrievalException("Unknown platform vendor.");
            }
        }
    }

    private void loadAffymetrixExpressionData(GenomicDataSourceConfiguration genomicSource) throws ConnectionException,
            DataRetrievalException {
        ArrayDataValues probeSetValues = caArrayFacade.retrieveData(genomicSource);
        ArrayDataValues geneValues = createGeneArrayDataValues(probeSetValues);
        arrayDataService.save(probeSetValues);
        arrayDataService.save(geneValues);
    }
    
    private void loadAgilentExpressionData(GenomicDataSourceConfiguration genomicSource)
    throws DataRetrievalException, ConnectionException, ValidationException {
        AgilentSampleMappingFileHandler handler = expressionHandlerFactory.getHandler(
                genomicSource, caArrayFacade, arrayDataService, dao);
        ArrayDataValues probeSetValues = handler.loadArrayData();
        ArrayDataValues geneValues = createGeneArrayDataValues(probeSetValues);
        arrayDataService.save(probeSetValues);
        arrayDataService.save(geneValues);
    }

    private void handleCopyNumberData(GenomicDataSourceConfiguration genomicSource) 
    throws DataRetrievalException, ConnectionException, ValidationException {
        AbstractCopyNumberMappingFileHandler handler = 
            copyNumberHandlerFactory.getHandler(genomicSource, caArrayFacade, arrayDataService, dao);
        List<ArrayDataValues> valuesList = handler.loadArrayData();
        if (genomicSource.getCopyNumberDataConfiguration().isCaDNACopyConfiguration()) {
            for (ArrayDataValues values : valuesList) {
                CopyNumberData copyNumberData = createCopyNumberData(values);
                retrieveSegmentationData(copyNumberData, genomicSource.getCopyNumberDataConfiguration());
            }            
        } else {
            CopyNumberData copyNumberData = createCopyNumberData(valuesList);
            retrieveSegmentationData(copyNumberData, genomicSource.getCopyNumberDataConfiguration());
        }
    }

    private CopyNumberData createCopyNumberData(List<ArrayDataValues> valuesList) {
        List<DnaAnalysisReporter> reporters;
        if (valuesList.isEmpty()) {
            reporters = Collections.emptyList();
        } else {
            reporters = convertToDnaAnalysisReporters(valuesList.get(0).getReporterList());
        }
        CopyNumberData copyNumberData = new CopyNumberData(reporters);
        for (ArrayDataValues values : valuesList) {
            addValues(values, copyNumberData);
        }
        return copyNumberData;
    }

    private CopyNumberData createCopyNumberData(ArrayDataValues values) {
        CopyNumberData copyNumberData = new CopyNumberData(convertToDnaAnalysisReporters(values.getReporterList()));
        addValues(values, copyNumberData);
        return copyNumberData;
    }

    private void addValues(ArrayDataValues values, CopyNumberData copyNumberData) {
        for (ArrayData arrayData : values.getArrayDatas()) {
            copyNumberData.addCopyNumberData(arrayData, 
                    values.getFloatValues(arrayData, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO));
        }
    }

    private void retrieveSegmentationData(CopyNumberData copyNumberData,
            CopyNumberDataConfiguration configuration) throws ConnectionException, DataRetrievalException {
        if (configuration.isCaDNACopyConfiguration()) {
            bioconductorService.addSegmentationData(copyNumberData, configuration);
        } else {
            CaIntegrator2GPClient client;
            try {
                client = getGenePatternClientFactory().retrieveClient(configuration.getSegmentationService());
                GladSegmentationHandler gladHandler = new GladSegmentationHandler(client);
                gladHandler.addSegmentationData(copyNumberData);
            } catch (WebServiceException e) {
                throw new ConnectionException(e.getMessage(), e);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "PMD.UnnecessaryLocalBeforeReturn" })  // for efficient conversion of List.
    private List<DnaAnalysisReporter> convertToDnaAnalysisReporters(ReporterList reporterList) {
        List reporters = reporterList.getReporters();
        return reporters;
    }

    private ArrayDataValues createGeneArrayDataValues(ArrayDataValues probeSetValues) {
        PlatformHelper platformHelper = 
            new PlatformHelper(probeSetValues.getReporterList().getPlatform());
        Set<ReporterList> reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        ArrayDataValues geneValues = new ArrayDataValues(platformHelper.getAllReportersByType(
                                                         ReporterTypeEnum.GENE_EXPRESSION_GENE));
        for (ArrayData arrayData : probeSetValues.getArrayDatas()) {
            loadGeneArrayDataValues(geneValues, probeSetValues, arrayData, platformHelper, reporterLists);
        }
        return geneValues;
    }

    private void loadGeneArrayDataValues(ArrayDataValues geneValues, ArrayDataValues probeSetValues, 
            ArrayData arrayData,
            PlatformHelper platformHelper, Set<ReporterList> reporterLists) {
        ArrayData geneArrayData = createGeneArrayData(arrayData, reporterLists);
        loadGeneValues(geneValues, probeSetValues, arrayData, geneArrayData, platformHelper);
    }

    private void loadGeneValues(ArrayDataValues geneValues, ArrayDataValues probeSetValues, ArrayData arrayData,
            ArrayData geneArrayData, PlatformHelper platformHelper) {
        for (AbstractReporter geneReporter 
                : platformHelper.getAllReportersByType(ReporterTypeEnum.GENE_EXPRESSION_GENE)) {
            Collection<AbstractReporter> probeSetReporters = 
                platformHelper.getReportersForGene(geneReporter.getGenes().iterator().next(), 
                        ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            geneValues.setFloatValue(geneArrayData, geneReporter, ArrayDataValueType.EXPRESSION_SIGNAL,
                    computeGeneReporterValue(probeSetReporters, probeSetValues, arrayData));
        }
    }

    private float computeGeneReporterValue(Collection<AbstractReporter> probeSetReporters, 
            ArrayDataValues probeSetValues, ArrayData arrayData) {
        DescriptiveStatistics statistics = new DescriptiveStatistics();
        for (AbstractReporter reporter : probeSetReporters) {
            statistics.addValue(probeSetValues.getFloatValue(arrayData, reporter, EXPRESSION_SIGNAL));
        }
        return (float) statistics.getPercentile(FIFTIETH_PERCENTILE);
    }

    private ArrayData createGeneArrayData(ArrayData probeSetArrayData, Set<ReporterList> reporterLists) {
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setStudy(probeSetArrayData.getStudy());
        arrayData.setArray(probeSetArrayData.getArray());
        arrayData.setSample(probeSetArrayData.getSample());
        arrayData.getSample().getArrayDataCollection().add(arrayData);
        if (!reporterLists.isEmpty()) {
            arrayData.getReporterLists().addAll(reporterLists);
            for (ReporterList reporterList : reporterLists) {
                reporterList.getArrayDatas().add(arrayData);
            }
        }
        dao.save(arrayData);
        return arrayData;
    }

    GenePatternClientFactory getGenePatternClientFactory() {
        return genePatternClientFactory;
    }

    void setGenePatternClientFactory(GenePatternClientFactory genePatternClientFactory) {
        this.genePatternClientFactory = genePatternClientFactory;
    }

    /**
     * @return the expressionHandlerFactory
     */
    public ExpressionHandlerFactory getExpressionHandlerFactory() {
        return expressionHandlerFactory;
    }

    /**
     * @param expressionHandlerFactory the expressionHandlerFactory to set
     */
    public void setExpressionHandlerFactory(ExpressionHandlerFactory expressionHandlerFactory) {
        this.expressionHandlerFactory = expressionHandlerFactory;
    }

}