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

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.SortTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Creates the actual results for the Query and Subjects that passed the criterion checks.
 */
public class ResultHandlerImpl implements ResultHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryResult createResults(Query query, Set<ResultRow> resultRows, CaIntegrator2Dao dao) {
        QueryResult queryResult = new QueryResult();
        queryResult.setRowCollection(resultRows);
        queryResult.setQuery(query);
        if (CollectionUtils.isNotEmpty(resultRows)) {
            addColumns(queryResult, dao);
            sortRows(queryResult);
        }
        return queryResult;
    }

    /**
     * This function assumes a QueryResult with no columns, just rows, and it fills in the columns
     * and values for each row.
     * @param queryResult - object that contains the rows.
     * @param dao
     */
    private void addColumns(QueryResult queryResult, CaIntegrator2Dao dao) {
        Query query = queryResult.getQuery();
        Collection<ResultColumn> columns = query.retrieveVisibleColumns();
        columns = removeUnauthorizedColumns(columns, query, dao);
        Collection<ResultRow> resultRows = queryResult.getRowCollection();
        for (ResultRow row : resultRows) {
            List<ResultValue> valueList = new ArrayList<ResultValue>();
            for (int i = 0; i < columns.size(); i++) {
                 valueList.add(null);
            }
            for (ResultColumn column : columns) {
                EntityTypeEnum entityType = column.getEntityType();
                ResultValue resultValue = new ResultValue();
                resultValue.setColumn(column);
                resultValue.setValue(null);
                valueList.set(column.getColumnIndex(), resultValue);
                switch(entityType) {
                case IMAGESERIES:
                    resultValue.setValue(handleImageSeriesRow(row, column));
                break;
                case SAMPLE:
                    resultValue.setValue(handleSampleRow(row, column));
                break;
                case SUBJECT:
                    resultValue.setValue(handleSubjectRow(row, column));
                break;
                default:
                    // Might need to throw some sort of error in this case?
                    resultValue.setValue(null);
                break;
                }
            }
            row.setValueCollection(valueList);
        }
    }

    /**
     * Takes an input collection of ResultColumns, and removes any column for
     * which the corresponding AnnotationFieldDescriptor is restricted by the
     * users AuthorizedStudyElementsGroups.
     */
    private Collection<ResultColumn> removeUnauthorizedColumns(Collection<ResultColumn> columns,
            Query query, CaIntegrator2Dao dao) {
        List<AnnotationFieldDescriptor> authorizedAfdList = new ArrayList<AnnotationFieldDescriptor>();
        UserWorkspace userWorkspace = query.getSubscription().getUserWorkspace();

        if (userWorkspace != null) {
            String username = userWorkspace.getUsername();
            StudyConfiguration studyConfig = query.getSubscription().getStudy().getStudyConfiguration();
            authorizedAfdList = dao.getAuthorizedAnnotationFieldDescriptors(username, studyConfig);
        }

        if (CollectionUtils.isNotEmpty(authorizedAfdList)) {
            List<ResultColumn> restrictedColumns = new ArrayList<ResultColumn>();
            restrictedColumns.addAll(columns);

            for (ResultColumn column : columns) {
                if (!authorizedAfdList.contains(column.getAnnotationFieldDescriptor())) {
                    restrictedColumns.remove(column);
                }
            }
            reindexColumns(restrictedColumns);
            return restrictedColumns;
        }

        return columns;
    }

    private void reindexColumns(List<ResultColumn> columns) {
        Collections.sort(columns);
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setColumnIndex(i);
        }
    }

    private AbstractAnnotationValue handleImageSeriesRow(ResultRow row, ResultColumn column) {
        ImageSeries imageSeries = row.getImageSeries();
        if (imageSeries != null) {
            for (AbstractAnnotationValue annotationValue : imageSeries.getAnnotationCollection()) {
                if (annotationValue.getAnnotationDefinition().equals(column.getAnnotationDefinition())) {
                    return retrieveValue(column.getAnnotationFieldDescriptor(), annotationValue);
                }
            }
        }
        return null;
    }

    private AbstractAnnotationValue handleSampleRow(ResultRow row, ResultColumn column) {
        SampleAcquisition sampleAcquisition = row.getSampleAcquisition();
        if (sampleAcquisition != null
                && sampleAcquisition.getAnnotationCollection() != null) {
            for (AbstractAnnotationValue annotationValue : sampleAcquisition.getAnnotationCollection()) {
                if (annotationValue.getAnnotationDefinition().equals(column.getAnnotationDefinition())) {
                    return retrieveValue(column.getAnnotationFieldDescriptor(), annotationValue);
                }
            }
        }
        return null;
    }

    private AbstractAnnotationValue handleSubjectRow(ResultRow row, final ResultColumn column) {
        StudySubjectAssignment studySubjectAssignment = row.getSubjectAssignment();
        if (studySubjectAssignment != null
                && CollectionUtils.isNotEmpty(studySubjectAssignment.getSubjectAnnotationCollection())) {
            for (SubjectAnnotation subjectAnnotation : studySubjectAssignment.getSubjectAnnotationCollection()) {
                if (subjectAnnotation.getAnnotationValue().getAnnotationDefinition()
                        .equals(column.getAnnotationDefinition())) {
                    return retrieveValue(column.getAnnotationFieldDescriptor(),
                            subjectAnnotation.getAnnotationValue());
                }
            }
        }
        return null;
    }

    /**
     * Sort the rows in the Query Result object.
     * @param queryResult - Object that needs the results to be sorted.
     */
    private void sortRows(QueryResult queryResult) {
        Collection<ResultColumn> columnsCollection = queryResult.getQuery().getColumnCollection();
        if (!columnsCollection.isEmpty()) {
            Collection <ResultRow> rowsCollection = queryResult.getRowCollection();
            List<ResultColumn> sortColumns = new ArrayList<ResultColumn>();
            for (ResultColumn column : columnsCollection) {
                if (!SortTypeEnum.UNSORTED.equals(column.getSortType())) {
                    // Sort Order and Column Index are the same for now, can be changed later
                    column.setSortOrder(column.getColumnIndex());
                    sortColumns.add(column);
                }
            }
            if (!sortColumns.isEmpty()) { // Sort only if there's a specified sort column.
                Collections.sort(sortColumns, new ResultColumnComparator());
                queryResult.setRowCollection(ResultRowComparator.sort(rowsCollection, sortColumns));
            }
        }
    }

    private AbstractAnnotationValue retrieveValue(AnnotationFieldDescriptor fieldDescriptor,
            AbstractAnnotationValue value) {
        return fieldDescriptor.getAnnotationMasks().isEmpty() ? value
            : AbstractAnnotationMaskHandler.retrieveMaskedValue(fieldDescriptor.getAnnotationMasks(), value);
    }
}
