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
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataMatrix;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Runs queries that return <code>GenomicDataQueryResults</code>.
 */
class GenomicQueryHandler {
    
    private final Query query;
    private final CaIntegrator2Dao dao;
    private final ArrayDataService arrayDataService;

    GenomicQueryHandler(Query query, CaIntegrator2Dao dao, ArrayDataService arrayDataService) {
        this.query = query;
        this.dao = dao;
        this.arrayDataService = arrayDataService;
    }

    GenomicDataQueryResult execute() {
        ArrayDataValues values = getDataValues();
        return createResult(values);
    }

    private GenomicDataQueryResult createResult(ArrayDataValues values) {
        GenomicDataQueryResult result = createNewResult();
        createResultRows(result, values);
        Map<AbstractReporter, GenomicDataResultRow> reporterToRowMap = createReporterToRowMap(result);
        for (ArrayData arrayData : values.getAllArrayDatas()) {
            addToResult(values, result, reporterToRowMap, arrayData);
        }
        result.setQuery(query);
        return result;
    }

    private void addToResult(ArrayDataValues values, GenomicDataQueryResult result,
        Map<AbstractReporter, GenomicDataResultRow> reporterToRowMap, ArrayData arrayData) {
        GenomicDataResultColumn column = new GenomicDataResultColumn();
        column.setSampleAcquisition(arrayData.getSample().getSampleAcquisition());
        result.getColumnCollection().add(column);
        for (AbstractReporter reporter : values.getAllReporters()) {
            GenomicDataResultRow row = reporterToRowMap.get(reporter);
            GenomicDataResultValue value = new GenomicDataResultValue();
            value.setColumn(column);
            value.setValue(values.getValue(arrayData, reporter));
            row.getValueCollection().add(value);
        }
    }

    private Map<AbstractReporter, GenomicDataResultRow> createReporterToRowMap(GenomicDataQueryResult result) {
        Map<AbstractReporter, GenomicDataResultRow> rowMap = new HashMap<AbstractReporter, GenomicDataResultRow>();
        for (GenomicDataResultRow row : result.getRowCollection()) {
            rowMap.put(row.getReporter(), row);
        }
        return rowMap;
    }

    private void createResultRows(GenomicDataQueryResult result, ArrayDataValues values) {
        int index = 0;
        for (AbstractReporter reporter : values.getAllReporters()) {
            GenomicDataResultRow row = new GenomicDataResultRow();
            row.setValueCollection(new ArrayList<GenomicDataResultValue>());
            row.setReporter(reporter);
            row.setRowIndex(index++);
            result.getRowCollection().add(row);
        }
    }

    private GenomicDataQueryResult createNewResult() {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        result.setColumnCollection(new ArrayList<GenomicDataResultColumn>());
        result.setRowCollection(new ArrayList<GenomicDataResultRow>());
        return result;
    }

    private ArrayDataValues getDataValues() {
        Collection<ArrayData> arrayDatas = getMatchingArrayDatas();
        Collection<AbstractReporter> reporters = getMatchingReporters();
        List<ArrayDataMatrix> matrixes = getDataMatrixes();
        ArrayDataValues values = new ArrayDataValues();
        for (ArrayDataMatrix matrix : matrixes) {
            values.addValues(arrayDataService.getData(matrix, arrayDatas, reporters));               
        }
        return values;
    }

    private Collection<ArrayData> getMatchingArrayDatas() {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(query.getCompoundCriterion());
        if (criterionHandler.hasEntityCriterion()) {
            Set<EntityTypeEnum> samplesOnly = new HashSet<EntityTypeEnum>();
            samplesOnly.add(EntityTypeEnum.SAMPLE);
            Set<ResultRow> rows = criterionHandler.getMatches(dao, query.getSubscription().getStudy(), samplesOnly);
            return getArrayDatas(rows, ReporterTypeEnum.getByValue(query.getReporterType()));
        } else {
            return getAllArrayDatas(ReporterTypeEnum.getByValue(query.getReporterType()));
        }
    }

    private Set<ArrayData> getAllArrayDatas(ReporterTypeEnum reporterType) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (StudySubjectAssignment assignment : query.getSubscription().getStudy().getAssignmentCollection()) {
            for (SampleAcquisition acquisition : assignment.getSampleAcquisitionCollection()) {
                addMatchesFromArrayDatas(arrayDatas, acquisition.getSample().getArrayDataCollection(), reporterType);
            }
        }
        if (query.getSubscription().getStudy().getControlSampleCollection() != null) {
            addMatchesFromSamples(arrayDatas, query.getSubscription().getStudy().getControlSampleCollection(), 
                    reporterType);
        }
        return arrayDatas;
    }

    private void addMatchesFromSamples(Set<ArrayData> arrayDatas, Collection<Sample> samples, ReporterTypeEnum 
            reporterType) {
        for (Sample sample : samples) {
            addMatchesFromArrayDatas(arrayDatas, sample.getArrayDataCollection(), reporterType);
        }
    }

    private void addMatchesFromArrayDatas(Set<ArrayData> matchingArrayDatas, 
            Collection<ArrayData> candidateArrayDatas,
            ReporterTypeEnum reporterType) {
        for (ArrayData arrayData : candidateArrayDatas) {
            if (arrayData.getReporterSet() != null 
                    && reporterType.equals(ReporterTypeEnum.getByValue(arrayData.getReporterSet().getReporterType()))) {
                matchingArrayDatas.add(arrayData);
            }
        }
    }

    private Set<ArrayData> getArrayDatas(Set<ResultRow> rows, ReporterTypeEnum reporterType) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (ResultRow row : rows) {
            if (row.getSampleAcquisition() != null) {
                Collection<ArrayData> candidateArrayDatas = 
                    row.getSampleAcquisition().getSample().getArrayDataCollection();
                addMatchesFromArrayDatas(arrayDatas, candidateArrayDatas, reporterType);
            }
        }
        return arrayDatas;
    }

    private List<ArrayDataMatrix> getDataMatrixes() {
        return dao.getArrayDataMatrixes(query.getSubscription().getStudy(), 
                ReporterTypeEnum.getByValue(query.getReporterType()));
    }

    private Collection<AbstractReporter> getMatchingReporters() {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(query.getCompoundCriterion());
        if (criterionHandler.hasReporterCriterion()) {
            return criterionHandler.getReporterMatches(dao, query.getSubscription().getStudy(), 
                    ReporterTypeEnum.getByValue(query.getReporterType()));
        } else {
            return getAllReporters();
        }
    }

    private Collection<AbstractReporter> getAllReporters() {
        HashSet<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        for (ArrayDataMatrix matrix : getDataMatrixes()) {
            reporters.addAll(matrix.getReporterSet().getReporters());
        }
        return reporters;
    }

}
