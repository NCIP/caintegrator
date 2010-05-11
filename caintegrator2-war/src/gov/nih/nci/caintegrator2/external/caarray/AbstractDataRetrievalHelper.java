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

import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.array.ArrayDesign;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.query.HybridizationSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.InvalidReferenceException;
import gov.nih.nci.caarray.services.external.v1_0.UnsupportedCategoryException;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Responsible for retrieving array data from caArray.
 */
abstract class AbstractDataRetrievalHelper {
    
    private final GenomicDataSourceConfiguration genomicSource;
    private final SearchService searchService;
    private final CaIntegrator2Dao dao;
    private final CentralTendencyCalculator centralTendencyCalculator;
    private Map<Hybridization, Sample> hybridizationToSampleMap;
    private Map<String, Biomaterial> nameToCaArraySampleMap;
    private Map<String, Hybridization> idToHybridizationMap;
    private PlatformHelper platformHelper;
    private ArrayDataValues arrayDataValues;
    
    AbstractDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
            SearchService searchService, CaIntegrator2Dao dao) {
                this.genomicSource = genomicSource;
                this.searchService = searchService;
                this.dao = dao;
                this.centralTendencyCalculator = new CentralTendencyCalculator(
                        genomicSource.getTechnicalReplicatesCentralTendency(), 
                        genomicSource.isUseHighVarianceCalculation(), 
                        genomicSource.getHighVarianceThreshold(), 
                        genomicSource.getHighVarianceCalculationType());
    }
    
    protected void init() throws DataRetrievalException {
        arrayDataValues = new ArrayDataValues(platformHelper.
                    getAllReportersByType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET));
    }

    abstract ArrayDataValues retrieveData() 
    throws ConnectionException, DataRetrievalException, InconsistentDataSetsException, FileNotFoundException, 
    InvalidInputException;

    /**
     * @return the genomicSource
     */
    public GenomicDataSourceConfiguration getGenomicSource() {
        return genomicSource;
    }

    /**
     * @return the searchService
     */
    public SearchService getSearchService() {
        return searchService;
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    Map<Hybridization, Sample> getHybridizationToSampleMap() 
    throws InvalidInputException {
        if (hybridizationToSampleMap == null) {
            loadHybridizationToSampleMap();
        }
        return hybridizationToSampleMap;
    }

    protected Biomaterial getCaArraySample(Sample sample) 
    throws InvalidReferenceException, UnsupportedCategoryException {
        return getNameToCaArraySampleMap().get(sample.getName());
    }

    private Map<String, Biomaterial> getNameToCaArraySampleMap() {
        if (nameToCaArraySampleMap == null) {
            loadSampleToCaArraySampleMap();
        }
        return nameToCaArraySampleMap;
    }

    private void loadSampleToCaArraySampleMap() {
        nameToCaArraySampleMap = new HashMap<String, Biomaterial>();
        try {
            List<Biomaterial> samples = 
                CaArrayUtils.getSamples(getGenomicSource().getExperimentIdentifier(), searchService);
            for (Biomaterial biomaterial : samples) {
                nameToCaArraySampleMap.put(biomaterial.getName(), biomaterial);
            }
        } catch (ExperimentNotFoundException e) {
            throw new IllegalStateException("Couldn't retrieve valid experiment");
        }
    }

    protected Platform getPlatform(Hybridization hybridization) throws DataRetrievalException {
        ArrayDesign arrayDesign = hybridization.getArrayDesign();
        if (arrayDesign == null) {
            throw new DataRetrievalException(
                    "There is no array design associated with the array for the hybridization "
                    + hybridization.getName() + ", unable to load array data");
        }
        Platform platform = getDao().getPlatform(arrayDesign.getName());
        if (platform == null) {
            throw new DataRetrievalException("The platform named " + arrayDesign.getName() 
                    + " hasn't been loaded into the system");
        }
        return platform;
    }

    protected Set<Hybridization> getAllHybridizations() throws InvalidInputException {
        return getHybridizationToSampleMap().keySet();
    }

    private void loadHybridizationToSampleMap() throws InvalidInputException {
        hybridizationToSampleMap = new HashMap<Hybridization, Sample>();
        for (Sample sample : getGenomicSource().getSamples()) {
            Biomaterial caArraySample = getCaArraySample(sample);
            List<Hybridization> hybridizations = getHybridizations(caArraySample);
            addToSampleMap(sample, hybridizations);
        }
    }

    protected List<Hybridization> getHybridizations(Biomaterial caArraySample) throws InvalidInputException {
        HybridizationSearchCriteria criteria = new HybridizationSearchCriteria();
        Set<CaArrayEntityReference> sourceSet = new HashSet<CaArrayEntityReference>();
        sourceSet.add(caArraySample.getReference());
        criteria.setBiomaterials(sourceSet);
        return getSearchService().searchForHybridizations(criteria, null).getResults();
    }

    protected void addToSampleMap(Sample sample, List<Hybridization> hybridizations) {
        for (Hybridization hybridization : hybridizations) {
            hybridizationToSampleMap.put(hybridization, sample);
        }
    }

    protected AbstractReporter getReporter(String probeSetName) {
        AbstractReporter reporter = platformHelper.getReporter(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, 
                probeSetName); 
        return reporter;
    }

    protected AbstractReporter getReporter(DesignElement designElement) {
        String probeSetName = designElement.getName();
        return getReporter(probeSetName);
    }

    protected ArrayData createArrayData(Sample sample, String arrayName) 
    throws InvalidInputException {
        Array array = new Array();
        array.setPlatform(platformHelper.getPlatform());
        array.setName(arrayName);
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setArray(array);
        Set<ReporterList> reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        if (!reporterLists.isEmpty()) {
            arrayData.getReporterLists().addAll(reporterLists);
            for (ReporterList reporterList : reporterLists) {
                reporterList.getArrayDatas().add(arrayData);
            }
        }
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.setStudy(getGenomicSource().getStudyConfiguration().getStudy());
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        getDao().save(array);
        return arrayData;
    }

    protected Sample getAssociatedSample(Hybridization hybridization) 
    throws InvalidInputException {
        return getHybridizationToSampleMap().get(getIdToHybridizationMap().get(hybridization.getId()));
    }

    private Map<String, Hybridization> getIdToHybridizationMap() 
    throws InvalidInputException {
        if (idToHybridizationMap == null) {
            idToHybridizationMap = new HashMap<String, Hybridization>();
            for (Hybridization hybridization : getAllHybridizations()) {
                idToHybridizationMap.put(hybridization.getId(), hybridization);
            }
        }
        return idToHybridizationMap;
    }

    protected void setValue(ArrayData arrayData, AbstractReporter reporter, List<Float> values) {
        arrayDataValues.setFloatValue(arrayData, 
                reporter, ArrayDataValueType.EXPRESSION_SIGNAL, values, centralTendencyCalculator);
        
    }

    /**
     * @return the arrayDataValues
     */
    public ArrayDataValues getArrayDataValues() {
        return arrayDataValues;
    }

    /**
     * @return the platformHelper
     */
    public PlatformHelper getPlatformHelper() {
        return platformHelper;
    }

    /**
     * @param platformHelper the platformHelper to set
     */
    public void setPlatformHelper(PlatformHelper platformHelper) {
        this.platformHelper = platformHelper;
    }

}
