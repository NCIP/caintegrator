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
import gov.nih.nci.caintegrator2.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AuthorizedQuery;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles CompoundCriterion objects.
 */
final class CompoundCriterionHandler extends AbstractCriterionHandler {

    private final Collection <AbstractCriterionHandler> handlers;
    private final CompoundCriterion compoundCriterion;
    private final ResultTypeEnum resultType;

    private CompoundCriterionHandler(Collection <AbstractCriterionHandler> handlers,
                                     CompoundCriterion compoundCriterion,
                                     ResultTypeEnum resultType) {
        this.handlers = handlers;
        this.compoundCriterion = compoundCriterion;
        this.resultType = resultType;
    }


    /**
     * Creates the CompoundCriterionHandler based on the given CompoundCriterion.
     * @param compoundCriterion - compound criterion to create from.
     * @return CompoundCriterionHandler object returned, with the handlers collection filled.
     */
    static CompoundCriterionHandler create(CompoundCriterion compoundCriterion, ResultTypeEnum resultType) {
        Collection<AbstractCriterionHandler> handlers = new HashSet<AbstractCriterionHandler>();
        if (compoundCriterion != null && compoundCriterion.getCriterionCollection() != null) {
            for (AbstractCriterion abstractCriterion : compoundCriterion.getCriterionCollection()) {
                if (abstractCriterion instanceof AbstractAnnotationCriterion) {
                    handlers.add(new AnnotationCriterionHandler((AbstractAnnotationCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof CompoundCriterion) {
                    handlers.add(CompoundCriterionHandler.create((CompoundCriterion) abstractCriterion, resultType));
                } else if (abstractCriterion instanceof GeneNameCriterion) {
                    handlers.add(GeneNameCriterionHandler.create((GeneNameCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof FoldChangeCriterion) {
                    handlers.add(FoldChangeCriterionHandler.create((FoldChangeCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof SubjectListCriterion) {
                    handlers.add(SubjectListCriterionHandler.create((SubjectListCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof CopyNumberAlterationCriterion) {
                   handlers.add(CopyNumberAlterationCriterionHandler.create((CopyNumberAlterationCriterion)
                           abstractCriterion));
                } else if (abstractCriterion instanceof ExpressionLevelCriterion) {
                    handlers.add(ExpressionLevelCriterionHandler.create((ExpressionLevelCriterion) abstractCriterion));
                } else {
                    throw new IllegalStateException("Unknown AbstractCriterion class: " + abstractCriterion);
                }
            }
        }
        return new CompoundCriterionHandler(handlers, compoundCriterion, resultType);
    }

    static CompoundCriterionHandler createAllSampleAnnotation() {
        Collection<AbstractCriterionHandler> handlers = new HashSet<AbstractCriterionHandler>();
        AbstractAnnotationCriterion annotationCriterion = new StringComparisonCriterion();
        annotationCriterion.setEntityType(EntityTypeEnum.SAMPLE);
        handlers.add(new AnnotationCriterionHandler(annotationCriterion));
        return new CompoundCriterionHandler(handlers, null, ResultTypeEnum.CLINICAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query,
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException {
        if (compoundCriterion == null || CollectionUtils.isEmpty(compoundCriterion.getCriterionCollection())) {
            return getAllRows(query.getSubscription().getStudy(), entityTypes);
        } else {
            return getMatchingRows(dao, arrayDataService, entityTypes, query);
        }
    }

    private Set<ResultRow> getAllRows(Study study, Set<EntityTypeEnum> entityTypes) {
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        if (entityTypes.contains(EntityTypeEnum.SUBJECT)) {
            return rowFactory.getSubjectRows(study.getAssignmentCollection());
        } else if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) {
            return rowFactory.getImageSeriesRows(getAllImageSeries(study));
        } else if (entityTypes.contains(EntityTypeEnum.SAMPLE)) {
            return rowFactory.getSampleRows(getAllSampleAcuisitions(study));
        } else {
            return Collections.emptySet();
        }
    }

    private Collection<SampleAcquisition> getAllSampleAcuisitions(Study study) {
        Set<SampleAcquisition> acquisitions = new HashSet<SampleAcquisition>();
        for (StudySubjectAssignment assignment : study.getAssignmentCollection()) {
            acquisitions.addAll(assignment.getSampleAcquisitionCollection());
        }
        return acquisitions;
    }

    private Collection<ImageSeries> getAllImageSeries(Study study) {
        Set<ImageSeries> imageSeriesSet = new HashSet<ImageSeries>();
        for (StudySubjectAssignment assignment : study.getAssignmentCollection()) {
            for (ImageSeriesAcquisition imageSeriesAcquisition : assignment.getImageStudyCollection()) {
                imageSeriesSet.addAll(imageSeriesAcquisition.getSeriesCollection());
            }
        }
        return imageSeriesSet;
    }

    private Set<ResultRow> getMatchingRows(CaIntegrator2Dao dao, ArrayDataService arrayDataService,
            Set<EntityTypeEnum> entityTypes, Query query) throws InvalidCriterionException {
        boolean rowsRetrieved = false;
        Set<ResultRow> allValidRows = new HashSet<ResultRow>();
        for (AbstractCriterionHandler handler : handlers) {
            Set<ResultRow> newRows = handler.getMatches(dao, arrayDataService, query, entityTypes);
            if (!rowsRetrieved) {
                allValidRows = newRows;
                rowsRetrieved = true;
            } else {
                allValidRows = combineResults(allValidRows, newRows, query.isMultiplePlatformQuery());
            }

        }

        allValidRows = removeUnauthorizedStudyElements(allValidRows, dao, query);
        return allValidRows;
    }

    /**
     * This method takes the query result rows as an input and removes any restricted study data
     * as determined by the AuthorizedStudyElementGroups that are assigned to this user.
     * If there are no AuthorizedStudyElementGroups for the study, then the input rowsBeforeRestriction
     * is returned unmodified.
     */
    private Set<ResultRow> removeUnauthorizedStudyElements(Set<ResultRow> rowsBeforeRestriction, CaIntegrator2Dao dao,
            Query query) {
        Study study = query.getSubscription().getStudy();
        StudyConfiguration studyConfiguration = study.getStudyConfiguration();
        boolean isStudyRestricted = CollectionUtils.isNotEmpty(studyConfiguration.getAuthorizedStudyElementsGroups());
        Set<ResultRow> rowsAfterRestriction = new HashSet<ResultRow>();
        String username = getUsername(query);
        if (isStudyRestricted) {
            if (username == null) {
                return rowsAfterRestriction;
            }
            Set<StudySubjectAssignment> listOfAllowedStudySubjectAssignments = new HashSet<StudySubjectAssignment>();
            Set<String> listOfAllowedExperimentIdentifiers = new HashSet<String>();
            List<AuthorizedStudyElementsGroup> authorizedStudyElementGroups = dao
                .getAuthorizedStudyElementGroups(username, studyConfiguration.getId());
            for (AuthorizedStudyElementsGroup asg : authorizedStudyElementGroups) {
                addToListOfAllowedStudySubjectAssignments(dao, listOfAllowedStudySubjectAssignments, study, asg);
                addToListOfAllowedExperimentIdentifiers(listOfAllowedExperimentIdentifiers, asg);
            }

            ResultTypeEnum queryResultType = query.getResultType();
            if (isFilterableResultType(queryResultType)) {
                filterResults(rowsBeforeRestriction, listOfAllowedStudySubjectAssignments,
                              listOfAllowedExperimentIdentifiers, rowsAfterRestriction, queryResultType);
            }
            return rowsAfterRestriction;
        } else { // unrestricted
            return rowsBeforeRestriction;
        }
    }

    private String getUsername(Query query) {
        UserWorkspace userWorkspace = query.getSubscription().getUserWorkspace();
        String username = null;
        if (userWorkspace != null) {
            username = userWorkspace.getUsername();
        }
        return username;
    }

    private void filterResults(Set<ResultRow> rowsBeforeRestriction,
            Set<StudySubjectAssignment> listOfAllowedStudySubjectAssignments,
            Set<String> listOfAllowedExperimentIdentifiers, Set<ResultRow> rowsAfterRestriction,
            ResultTypeEnum queryResultType) {
        List<StudySubjectAssignment> listOfRestrictedStudySubjectAssignments
                                                     = new ArrayList<StudySubjectAssignment>();
        for (ResultRow resultRow : rowsBeforeRestriction) {
            listOfRestrictedStudySubjectAssignments.add(resultRow.getSubjectAssignment());
        }

        listOfRestrictedStudySubjectAssignments.retainAll(listOfAllowedStudySubjectAssignments);
        String expId = StringUtils.EMPTY;
        for (ResultRow resultRow2 : rowsBeforeRestriction) {
            if (areQueryResultsGenomic(queryResultType)) {
                expId = resultRow2.getSampleAcquisition().getSample().getGenomicDataSource().getExperimentIdentifier();
                if (listOfRestrictedStudySubjectAssignments.contains(resultRow2.getSubjectAssignment())
                        && listOfAllowedExperimentIdentifiers.contains(expId)) {
                    rowsAfterRestriction.add(resultRow2);
                }
            } else if (listOfRestrictedStudySubjectAssignments.contains(resultRow2.getSubjectAssignment())) {
                rowsAfterRestriction.add(resultRow2);
            }
        }
    }


    private boolean areQueryResultsGenomic(ResultTypeEnum queryResultType) {
        return queryResultType == ResultTypeEnum.GENE_EXPRESSION || queryResultType == ResultTypeEnum.COPY_NUMBER;
    }

    private void addToListOfAllowedStudySubjectAssignments(CaIntegrator2Dao dao,
            Set<StudySubjectAssignment> listOfAllowedStudySubjectAssignments, Study study,
            AuthorizedStudyElementsGroup asg) {
        for (AuthorizedQuery authorizedQuery : asg.getAuthorizedQuerys()) {
            CompoundCriterion queryCompoundCriterion = authorizedQuery.getQuery().getCompoundCriterion();
            for (AbstractCriterion abstractCriterion : queryCompoundCriterion.getCriterionCollection()) {
                if (abstractCriterion instanceof AbstractAnnotationCriterion) {
                    List<StudySubjectAssignment> matchingSubjects = dao
                        .findMatchingSubjects((AbstractAnnotationCriterion) abstractCriterion, study);
                    listOfAllowedStudySubjectAssignments.addAll(matchingSubjects);
                }
            }
        }
    }

    private void addToListOfAllowedExperimentIdentifiers(Set<String> listOfAllowedExperimentIdentifiers,
            AuthorizedStudyElementsGroup asg) {
        for (AuthorizedGenomicDataSourceConfiguration authorizedGDC
                : asg.getAuthorizedGenomicDataSourceConfigurations()) {
            listOfAllowedExperimentIdentifiers.add(authorizedGDC.getGenomicDataSourceConfiguration()
                                                   .getExperimentIdentifier());
        }
    }

    private boolean isFilterableResultType(ResultTypeEnum queryResultType) {
        return queryResultType == ResultTypeEnum.CLINICAL
                || queryResultType == ResultTypeEnum.GENE_EXPRESSION
                || queryResultType == ResultTypeEnum.COPY_NUMBER
                || queryResultType == ResultTypeEnum.IGV_VIEWER
                || queryResultType == ResultTypeEnum.HEATMAP_VIEWER;
    }

    /**
     * Combines the results of the rows.
     * @param currentValidRows - current rows that are valid.
     * @param newRows - new rows to validate.
     * @param defaultTimepoint - the default timepoint for the study.
     * @return - combination of rows.
     */
    private Set<ResultRow> combineResults(Set<ResultRow> currentValidRows,
                                          Set<ResultRow> newRows, boolean isMultiplePlatformQuery) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
        if (compoundCriterion.getBooleanOperator() != null) {
           switch(compoundCriterion.getBooleanOperator()) {
           case AND:
               combinedResults = combineResultsForAndOperator(currentValidRows, newRows, isMultiplePlatformQuery);
           break;
           case OR:
               combinedResults = combineResultsForOrOperator(currentValidRows, newRows, isMultiplePlatformQuery);
           break;
           default:
               // TODO : figure out what to actually do in this case?
               combinedResults.addAll(currentValidRows);
               combinedResults.addAll(newRows);
           break;
           }

        }
        return combinedResults;
    }


    private Set<ResultRow> combineResultsForAndOperator(Set<ResultRow> currentValidRows,
                                   Set<ResultRow> newRows, boolean isMultiplePlatformQuery) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
           for (ResultRow row : newRows) {
               ResultRow rowFound =
                   QueryUtil.resultRowSetContainsResultRow(currentValidRows, row, isMultiplePlatformQuery);
               if (rowFound != null) {
                   if (isMultiplePlatformQuery) {
                       combinedResults.add(checkRowsForAppropriateReporter(rowFound, row));
                   } else {
                       combinedResults.add(row);
                   }
               }
           }
           return combinedResults;
    }

    private ResultRow checkRowsForAppropriateReporter(ResultRow rowFound, ResultRow row) {
        if (rowFound.getSampleAcquisition() != null && rowFound.getSampleAcquisition().getSample() != null
                && !rowFound.getSampleAcquisition().getSample().getArrayDataCollection().isEmpty()) {
            ReporterTypeEnum reporterType =
                rowFound.getSampleAcquisition().getSample().getArrayDataCollection().
                    iterator().next().getReporterType();
            if (resultType.isReporterMatch(reporterType)) {
                return rowFound;
            }
        }
        return row;
    }


    private Set<ResultRow> combineResultsForOrOperator(Set<ResultRow> currentValidRows,
                                             Set<ResultRow> newRows, boolean isMultiplePlatformQuery) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
        combinedResults.addAll(currentValidRows);
        for (ResultRow row : newRows) {
            ResultRow rowFound =
                QueryUtil.resultRowSetContainsResultRow(currentValidRows, row, isMultiplePlatformQuery);
            if (rowFound == null) {
                combinedResults.add(row);
            }

        }
        return combinedResults;
    }

    @Override
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType,
            Platform platform) {
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.isReporterMatchHandler()) {
                reporters.addAll(handler.getReporterMatches(dao, study, reporterType, platform));
            }
        }
        return reporters;
    }

    @Override
    Set<SegmentData> getSegmentDataMatches(CaIntegrator2Dao dao, Study study, Platform platform)
        throws InvalidCriterionException {
        Set<SegmentData> segmentDatas = new HashSet<SegmentData>();
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasSegmentDataCriterion()) {
                segmentDatas.addAll(handler.getSegmentDataMatches(dao, study, platform));
            }
        }
        return segmentDatas;
    }

    @Override
    boolean isReporterMatchHandler() {
        return true;
    }


    @Override
    boolean hasReporterCriterion() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasReporterCriterion()) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean hasCriterionSpecifiedReporterValues() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasCriterionSpecifiedReporterValues()) {
                return true;
            }
        }
        return false;
    }


    @Override
    GenomicCriteriaMatchTypeEnum getGenomicValueMatchCriterionType(Set<Gene> genes, Float value) {
        for (AbstractCriterionHandler handler : handlers) {
            GenomicCriteriaMatchTypeEnum matchType = handler.getGenomicValueMatchCriterionType(genes, value);
            if (!GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(matchType)) {
                return matchType;
            }
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }

    @Override
    boolean hasSegmentDataCriterion() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasSegmentDataCriterion()) {
                return true;
            }
        }
        return false;
    }


    @Override
    boolean hasCriterionSpecifiedSegmentValues() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasCriterionSpecifiedSegmentValues()) {
                return true;
            }
        }
        return false;
    }


    @Override
    boolean hasCriterionSpecifiedSegmentCallsValues() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasCriterionSpecifiedSegmentCallsValues()) {
                return true;
            }
        }
        return false;
    }


    @Override
    GenomicCriteriaMatchTypeEnum getSegmentValueMatchCriterionType(Float value) {
        for (AbstractCriterionHandler handler : handlers) {
            GenomicCriteriaMatchTypeEnum matchType = handler.getSegmentValueMatchCriterionType(value);
            if (!GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(matchType)) {
                return matchType;
            }
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }


    /**
     * @param callsValue
     * @return
     */
    @Override
    public GenomicCriteriaMatchTypeEnum getSegmentCallsValueMatchCriterionType(
            Integer callsValue) {
        for (AbstractCriterionHandler handler : handlers) {
            GenomicCriteriaMatchTypeEnum matchType = handler.getSegmentCallsValueMatchCriterionType(callsValue);
            if (!GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(matchType)) {
                return matchType;
            }
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }

}
