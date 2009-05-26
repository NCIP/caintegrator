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

import gov.nih.nci.caarray.domain.AbstractCaArrayObject;
import gov.nih.nci.caarray.domain.array.AbstractDesignElement;
import gov.nih.nci.caarray.domain.array.AbstractProbe;
import gov.nih.nci.caarray.domain.array.ArrayDesign;
import gov.nih.nci.caarray.domain.hybridization.Hybridization;
import gov.nih.nci.caarray.domain.project.Experiment;
import gov.nih.nci.caarray.domain.sample.Extract;
import gov.nih.nci.caarray.domain.sample.LabeledExtract;
import gov.nih.nci.caarray.services.search.CaArraySearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Responsible for retrieving array data from caArray.
 */
abstract class AbstractDataRetrievalHelper {
    
    private final GenomicDataSourceConfiguration genomicSource;
    private final CaArraySearchService searchService;
    private final CaIntegrator2Dao dao;
    private final Map<Hybridization, Sample> hybridizationToSampleMap = new HashMap<Hybridization, Sample>();
    private PlatformHelper platformHelper;
    private ArrayDataValues arrayDataValues;
    
    AbstractDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
            CaArraySearchService searchService, CaIntegrator2Dao dao) {
                this.genomicSource = genomicSource;
                this.searchService = searchService;
                this.dao = dao;
    }
    
    protected void init() throws DataRetrievalException {
        arrayDataValues = new ArrayDataValues(platformHelper.
                    getAllReportersByType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET));
    }

    abstract ArrayDataValues retrieveData() throws ConnectionException, DataRetrievalException;

    /**
     * @return the genomicSource
     */
    public GenomicDataSourceConfiguration getGenomicSource() {
        return genomicSource;
    }

    /**
     * @return the searchService
     */
    public CaArraySearchService getSearchService() {
        return searchService;
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @return the hybridizationToSampleMap
     */
    public Map<Hybridization, Sample> getHybridizationToSampleMap() {
        return hybridizationToSampleMap;
    }

    protected gov.nih.nci.caarray.domain.sample.Sample getCaArraySample(Sample sample, String experimentIdentifier) {
        gov.nih.nci.caarray.domain.sample.Sample searchSample = new gov.nih.nci.caarray.domain.sample.Sample();
        searchSample.setExperiment(new Experiment());
        searchSample.getExperiment().setPublicIdentifier(experimentIdentifier);
        searchSample.setName(sample.getName());
        return getSearchService().search(searchSample).get(0);
    }

    protected Platform getPlatform(Hybridization hybridization) throws DataRetrievalException {
        Hybridization loadedHybridization = getLoadedCaArrayObject(hybridization);
        ArrayDesign arrayDesign = getLoadedCaArrayObject(loadedHybridization.getArray()).getDesign();
        if (arrayDesign == null) {
            throw new DataRetrievalException(
                    "There is no array design associated with the array for the hybridization "
                    + loadedHybridization.getName() + ", unable to load array data");
        }
        Platform platform = getDao().getPlatform(arrayDesign.getName());
        if (platform == null) {
            throw new DataRetrievalException("The platform named " + arrayDesign.getName() 
                    + " hasn't been loaded into the system");
        }
        return platform;
    }

    protected Set<Hybridization> getAllHybridizations() {
        Set<Hybridization> hybridizationSet = new HashSet<Hybridization>();
        for (Sample sample : getGenomicSource().getSamples()) {
            gov.nih.nci.caarray.domain.sample.Sample caArraySample = getCaArraySample(sample, 
                    getGenomicSource().getExperimentIdentifier());
            Set<Hybridization> hybridizations = getHybridizations(caArraySample);
            addToSampleMap(sample, hybridizations);
            hybridizationSet.addAll(hybridizations);
        }
        return hybridizationSet;
    }

    protected Set<Hybridization> getHybridizations(gov.nih.nci.caarray.domain.sample.Sample caArraySample) {
        Set<Hybridization> hybridizations = new HashSet<Hybridization>();
        Set<Extract> extracts = getLoadedCaArrayObjects(caArraySample.getExtracts());
        for (Extract extract : extracts) {
            hybridizations.addAll(getHybridizations(extract));
        }
        return hybridizations;
    }

    private Set<Hybridization> getHybridizations(Extract extract) {
        Set<Hybridization> hybridizations = new HashSet<Hybridization>();
        Set<LabeledExtract> labeledExtracts = getLoadedCaArrayObjects(extract.getLabeledExtracts());
        for (LabeledExtract labeledExtract : labeledExtracts) {
            hybridizations.addAll(labeledExtract.getHybridizations());
        }
        return hybridizations;
    }

    protected void addToSampleMap(Sample sample, Set<Hybridization> hybridizations) {
        for (Hybridization hybridization : hybridizations) {
            hybridizationToSampleMap.put(hybridization, sample);
        }
    }

    protected AbstractReporter getReporter(String probeSetName) {
        AbstractReporter reporter = platformHelper.getReporter(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, 
                probeSetName); 
        return reporter;
    }

    protected AbstractReporter getReporter(AbstractDesignElement designElement) {
        String probeSetName = ((AbstractProbe) designElement).getName();
        return getReporter(probeSetName);
    }

    protected ArrayData createArrayData(Hybridization hybridization) {
        Array array = new Array();
        array.setPlatform(platformHelper.getPlatform());
        array.setName(hybridization.getName());
        Sample sample = getAssociatedSample(hybridization);
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

    private Sample getAssociatedSample(Hybridization hybridization) {
        return getHybridizationToSampleMap().get(hybridization);
    }

    protected void setValue(ArrayData arrayData, AbstractReporter reporter, float value) {
        arrayDataValues.setFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL, value);
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

    <T extends AbstractCaArrayObject> Set<T> getLoadedCaArrayObjects(Set<T> objects) {
        Set<T> loadedObjects = new HashSet<T>(objects.size());
        for (T t : objects) {
            loadedObjects.add(getLoadedCaArrayObject(t));
        }
        return loadedObjects;
    }

    <T extends AbstractCaArrayObject> T getLoadedCaArrayObject(T object) {
        return getSearchService().search(object).get(0);
    }

}
