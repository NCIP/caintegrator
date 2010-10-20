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

import gov.nih.nci.caarray.external.v1_0.data.DataSet;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.data.HybridizationData;
import gov.nih.nci.caarray.external.v1_0.data.QuantitationType;
import gov.nih.nci.caarray.external.v1_0.query.DataSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.QuantitationTypeSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Responsible for retrieving array data from caArray.
 */
class DnaAnalysisDataRetrievalHelper extends AbstractDataRetrievalHelper {

    private static final Logger LOGGER = Logger.getLogger(DnaAnalysisDataRetrievalHelper.class);
    private static final ReporterTypeEnum REPORTER_TYPE = ReporterTypeEnum.DNA_ANALYSIS_REPORTER;
    private List<ArrayDataValues> arrayDataValuesList;
    
    DnaAnalysisDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
            DataService dataService, SearchService searchService, CaIntegrator2Dao dao) {
        super(genomicSource, dataService, searchService, dao);
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

    @Override
    ReporterTypeEnum getReporterType() {
        return REPORTER_TYPE;
    }
    
    protected void retrieveData() 
    throws ConnectionException, DataRetrievalException, InconsistentDataSetsException, InvalidInputException {
    
        DataSet dataSet = getDataService().getDataSet(createRequest());
        if (dataSet.getDatas().isEmpty()) {
            throw new DataRetrievalException("No log2 ratio available for experiment: "
                    + getGenomicSource().getExperimentIdentifier());
        }
        Hybridization hybridization = dataSet.getDatas().get(0).getHybridization();
        setPlatformHelper(new PlatformHelper(getPlatform(hybridization)));
        arrayDataValuesList = new ArrayList<ArrayDataValues>();
        convertToArrayDataValuesList(dataSet);
    }
    

    @Override
    protected QuantitationType getSignal(DataSetRequest request) throws InvalidInputException {
        // Select the quantitation types (columns) of Log2Ratio.
        QuantitationTypeSearchCriteria qtCrit = new QuantitationTypeSearchCriteria();
        qtCrit.setHybridization(request.getHybridizations().iterator().next());
        List<QuantitationType> qtypes = getSearchService().searchForQuantitationTypes(qtCrit);
        for (QuantitationType qt : qtypes) {
            if ("DataMatrixCopyNumber.Log2Ratio".equalsIgnoreCase(qt.getName())) {
                return qt;
            }
        }
        return null;
    }
    
    private void convertToArrayDataValuesList(DataSet dataSet) 
    throws DataRetrievalException, InvalidInputException {
        fillSampleToHybridizationDataMap(dataSet);
        for (Sample sample : getSampleToHybridizationDataMap().keySet()) {
            ArrayData arrayData = createArrayData(sample);
            getDao().save(arrayData);
            ArrayDataValues arrayDataValues = new ArrayDataValues(
                    getPlatformHelper().getAllReportersByType(REPORTER_TYPE));
            arrayDataValuesList.add(arrayDataValues);
            
            buildArrayDataValues(dataSet, sample, arrayData, arrayDataValues);
        }
    }

    private void buildArrayDataValues(DataSet dataSet, Sample sample, ArrayData arrayData,
            ArrayDataValues arrayDataValues) {
        List<DesignElement> probeSets = dataSet.getDesignElements();
        List<HybridizationData> hybridizationDatas = getSampleToHybridizationDataMap().get(sample);
        List<float[]> allHybridizationsValues = retrieveAllHybridizationValues(hybridizationDatas);
        for (int i = 0; i < probeSets.size(); i++) {
            AbstractReporter reporter = getReporter(probeSets.get(i));
            if (reporter == null) {
                String probeSetName = ((DesignElement) probeSets.get(i)).getName();
                getLogger().warn("Reporter with name " + probeSetName + " was not found in platform " 
                        + getPlatformHelper().getPlatform().getName());
            } else {
                List<Float> floatValues = new ArrayList<Float>();
                for (float[] values : allHybridizationsValues) {
                    floatValues.add(values[i]);
                }
                arrayDataValues.setFloatValue(arrayData, reporter, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO,
                        floatValues, getCentralTendencyCalculator());
            }
        }
    }
    
    private ArrayData createArrayData(Sample sample) {
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.COPY_NUMBER);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        arrayData.setStudy(getGenomicSource().getStudyConfiguration().getStudy());
        Array array = new Array();
        array.getArrayDataCollection().add(arrayData);
        arrayData.setArray(array);
        array.getSampleCollection().add(sample);
        sample.getArrayCollection().add(array);
        if (!getReporterList(REPORTER_TYPE).isEmpty()) {
            arrayData.getReporterLists().addAll(getReporterList(REPORTER_TYPE));
            for (ReporterList reporterList : getReporterList(REPORTER_TYPE)) {
                reporterList.getArrayDatas().add(arrayData);    
            }
            array.setPlatform(getReporterList(REPORTER_TYPE).iterator().next().getPlatform());
        }
        return arrayData;
    }

    /**
     * @return the arrayDataValuesList
     */
    protected List<ArrayDataValues> getArrayDataValuesList() {
        return arrayDataValuesList;
    }
    
}