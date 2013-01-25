/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.CaIntegrator2BaseService;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.external.ncia.NCIAImageAggregationTypeEnum;
import gov.nih.nci.caintegrator2.external.ncia.NCIAImageAggregator;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableResultRow;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the QueryManagementService interface.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See handleCheckedRowForImageStudy()
@Transactional(propagation = Propagation.REQUIRED)
public class QueryManagementServiceImpl extends CaIntegrator2BaseService implements QueryManagementService {
    
    private ResultHandler resultHandler;
    private ArrayDataService arrayDataService;
    private FileManager fileManager;

    /**
     * @param resultHandler the resultHandler to set
     */
    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public QueryResult execute(Query query) throws InvalidCriterionException {
        return new QueryTranslator(retrieveQueryToExecute(query), getDao(), 
                arrayDataService, resultHandler).execute();
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public GenomicDataQueryResult executeGenomicDataQuery(Query query) throws InvalidCriterionException {
        return new GenomicQueryHandler(retrieveQueryToExecute(query), 
                getDao(), arrayDataService).execute();
    }
    
    private Query retrieveQueryToExecute(Query query) throws InvalidCriterionException {
        try {
            query.getCompoundCriterion().validateGeneExpressionCriterion();
            if (QueryUtil.isQueryGenomic(query)) {
                addPlatformToQuery(query);
            }
            Query queryToExecute = query.clone();
            addGenesNotFoundToQuery(query);
            addSubjectsNotFoundToQuery(query);
            query.setHasMaskedValues(queryToExecute.getCompoundCriterion().isHasMaskedCriteria());
            if (query.isHasMaskedValues()) {
                maskCompoundCriterion(queryToExecute.getCompoundCriterion());
            }
            checkCriterionColumnsForMasks(query);
            return queryToExecute;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unable to clone query.");
        }
    }

    private void addPlatformToQuery(Query query) throws InvalidCriterionException {
        if (QueryUtil.isQueryCopyNumber(query)) {
            query.setCopyNumberPlatform(
                retrievePlatform(query, retrieveCopyNumberPlatformsForStudy(query.getSubscription().getStudy()),
                        GenomicCriterionTypeEnum.COPY_NUMBER));
        }
        if (QueryUtil.isQueryGeneExpression(query)) {
            query.setGeneExpressionPlatform(
                retrievePlatform(query, retrieveGeneExpressionPlatformsForStudy(query.getSubscription().getStudy()),
                        GenomicCriterionTypeEnum.GENE_EXPRESSION));
        }
    }

    private Platform retrievePlatform(Query query, Set<String> platformNames, 
            GenomicCriterionTypeEnum genomicCriterionType) throws InvalidCriterionException {
        if (platformNames.size() == 1) {
            return getDao().getPlatform(platformNames.iterator().next());
        } else {
            Set<String> allPlatformNames = query.getCompoundCriterion().getAllPlatformNames(genomicCriterionType);
            if (allPlatformNames.size() != 1) {
               throw new InvalidCriterionException("A " + genomicCriterionType.getValue()
                       + " query must contain exactly 1 platform of type " 
                       + genomicCriterionType.getValue() + ".  This one contains " 
                       + allPlatformNames.size() + " platforms.  " 
                       + "Create a query and within the criterion, select a platform.");
            }
            return getDao().getPlatform(allPlatformNames.iterator().next());
        }
    }
    
    private void checkCriterionColumnsForMasks(Query query) {
        if (!query.isHasMaskedValues()) {
            for (ResultColumn column : query.getColumnCollection()) {
                if (column.getAnnotationFieldDescriptor() != null 
                     && !column.getAnnotationFieldDescriptor().getAnnotationMasks().isEmpty()) {
                    query.setHasMaskedValues(true);
                    break;
                }
            }
        }
    }
    
    private void maskCompoundCriterion(CompoundCriterion compoundCriterion) {
        Set<AbstractCriterion> criterionToRemove = new HashSet<AbstractCriterion>();
        Set<AbstractCriterion> criterionToAdd = new HashSet<AbstractCriterion>();
        for (AbstractCriterion criterion : compoundCriterion.getCriterionCollection()) {
            if (!(criterion instanceof CompoundCriterion)) {
                AbstractCriterion newCriterion = retrieveMaskedCriterion(criterion);
                if (!criterion.equals(newCriterion)) {
                    criterionToAdd.add(newCriterion);
                    criterionToRemove.add(criterion);
                }
            } else {
                maskCompoundCriterion((CompoundCriterion) criterion);
            }
        }
        compoundCriterion.getCriterionCollection().addAll(criterionToAdd);
        compoundCriterion.getCriterionCollection().removeAll(criterionToRemove);
    }
    
    private AbstractCriterion retrieveMaskedCriterion(AbstractCriterion abstractCriterion) {
        if (abstractCriterion instanceof AbstractAnnotationCriterion
                && ((AbstractAnnotationCriterion) abstractCriterion).getAnnotationFieldDescriptor() != null
                && !((AbstractAnnotationCriterion) abstractCriterion).getAnnotationFieldDescriptor()
                    .getAnnotationMasks().isEmpty()) {
                return AbstractAnnotationMaskHandler.createMaskedCriterion(
                        ((AbstractAnnotationCriterion) abstractCriterion).getAnnotationFieldDescriptor()
                                .getAnnotationMasks(), abstractCriterion);
            }
        return abstractCriterion;
    }
    
    private void addSubjectsNotFoundToQuery(Query query) throws InvalidCriterionException {
        query.getSubjectIdsNotFound().clear();
        query.getSubjectIdsNotFound().addAll(getAllSubjectsNotFoundInCriteria(query));
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<String> getAllSubjectsNotFoundInCriteria(Query query) throws InvalidCriterionException {
        if (query == null) {
            return new HashSet<String>();
        }
        Set<String> allSubjectsInCriterion = query.getCompoundCriterion().getAllSubjectIds();
        Set<String> allSubjectsNotFound = new HashSet<String>(allSubjectsInCriterion);
        Set<String> allSubjectsInStudy = new HashSet<String>();
        if (!allSubjectsInCriterion.isEmpty()) {
            for (StudySubjectAssignment assignment : query.getSubscription().getStudy().getAssignmentCollection()) {
                allSubjectsInStudy.add(assignment.getIdentifier());
            }
            allSubjectsNotFound.removeAll(allSubjectsInStudy);
            if (!allSubjectsNotFound.isEmpty() && allSubjectsNotFound.size() == allSubjectsInCriterion.size()) {
                String queryNameString = StringUtils.isNotBlank(query.getName()) ? "'" 
                        + query.getName() + "'  " : "";
                throw new InvalidCriterionException(
                        "None of the Subject IDs in the query "
                        +  queryNameString + "were found in the study.");
            }
        }
        return allSubjectsNotFound;
    }
    
    private void addGenesNotFoundToQuery(Query query) throws InvalidCriterionException {
        List<String> allGeneSymbols = query.getCompoundCriterion().getAllGeneSymbols();
        query.getGeneSymbolsNotFound().clear();
        if (!allGeneSymbols.isEmpty() && !isQueryOnAllGenes(allGeneSymbols)) {
            try {
                query.getGeneSymbolsNotFound().addAll(validateGeneSymbols(query.getSubscription(), allGeneSymbols));
            } catch (GenesNotFoundInStudyException e) {
                throw new InvalidCriterionException(e.getMessage(), e);
            }
        }
    }
    
    private boolean isQueryOnAllGenes(Collection<String> allGeneSymbols) {
        if (allGeneSymbols.contains("")) {
            return true;
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<String> retrieveGeneExpressionPlatformsForStudy(Study study) {
        Set<String> platformsInStudy = new HashSet<String>();
        for (Platform platform : arrayDataService.getPlatformsInStudy(
                study, GenomicDataSourceDataTypeEnum.EXPRESSION)) {
            platformsInStudy.add(platform.getName());
        }
        return platformsInStudy;
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<String> retrieveCopyNumberPlatformsForStudy(Study study) {
        Set<String> platformsInStudy = new HashSet<String>();
        for (Platform platform : arrayDataService.getPlatformsInStudy(
                study, GenomicDataSourceDataTypeEnum.COPY_NUMBER)) {
            platformsInStudy.add(platform.getName());
        }
        return platformsInStudy;
    }

    /**
     * {@inheritDoc}
     */
    public List<Query> createQueriesFromSubjectLists(StudySubscription subscription) {
        List<Query> queries = new ArrayList<Query>();
        for (SubjectList subjectList : subscription.getStudy().getStudyConfiguration().getSubjectLists()) {
            queries.add(createQueryFromSubjectList(subscription, subjectList));
        }
        for (SubjectList subjectList : subscription.getSubjectLists()) {
            queries.add(createQueryFromSubjectList(subscription, subjectList));
        }
        return queries;
    }

    /**
     * {@inheritDoc}
     */
    public Query createQueryFromSubjectList(StudySubscription subscription, SubjectList subjectList) {
        Query query = new Query();
        SubjectListCriterion criterion = new SubjectListCriterion();
        criterion.getSubjectListCollection().add(subjectList);
        query.setName(subjectList.getName());
        query.setDescription(subjectList.getDescription());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setSubscription(subscription);
        query.setResultType(ResultTypeEnum.CLINICAL);
        query.getCompoundCriterion().getCriterionCollection().add(criterion);
        query.setSubjectListQuery(true);
        query.setSubjectListVisibility(subjectList.getVisibility());
        return query;
    }
    
    /**
     * {@inheritDoc}
     */
    public NCIADicomJob createDicomJob(List<DisplayableResultRow> checkedRows) {
        NCIADicomJob dicomJob = new NCIADicomJob();
        dicomJob.setImageAggregationType(retrieveAggregationType(checkedRows));
        fillImageAggregatorFromCheckedRows(dicomJob, checkedRows);
        return dicomJob;

    }

    /**
     * {@inheritDoc}
     */
    public NCIABasket createNciaBasket(List<DisplayableResultRow> checkedRows) {
        NCIABasket basket = new NCIABasket();
        basket.setImageAggregationType(retrieveAggregationType(checkedRows));
        fillImageAggregatorFromCheckedRows(basket, checkedRows);
        return basket;
    }
    
    private NCIAImageAggregationTypeEnum retrieveAggregationType(List<DisplayableResultRow> rows) {
        NCIAImageAggregationTypeEnum aggregationType = NCIAImageAggregationTypeEnum.IMAGESERIES;
        for (DisplayableResultRow row : rows) {
            if (row.getImageSeries() == null) {
                aggregationType = NCIAImageAggregationTypeEnum.IMAGESTUDY;
                break;
            }
        }
        return aggregationType;
    }

    private void fillImageAggregatorFromCheckedRows(NCIAImageAggregator imageAggregator, 
                                                    List<DisplayableResultRow> checkedRows) {
        for (DisplayableResultRow row : checkedRows) {
            switch(imageAggregator.getImageAggregationType()) {
            case IMAGESERIES:
                handleCheckedRowForImageSeries(imageAggregator, row);
                break;
            case IMAGESTUDY:
                handleCheckedRowForImageStudy(imageAggregator, row);
                break;
            default:
                throw new IllegalStateException("Aggregate Level Type is unknown.");
            }
        }
    }

    private void handleCheckedRowForImageSeries(NCIAImageAggregator imageAggregator, DisplayableResultRow row) {
        if (row.getImageSeries() != null) {
            imageAggregator.getImageSeriesIDs().add(row.getImageSeries().getIdentifier());
        } else {
            throw new IllegalArgumentException(
                "Aggregation is based on Image Series, and a row doesn't contain an Image Series.");
        }
        
    }
    
    @SuppressWarnings("PMD.CyclomaticComplexity") // Null checks
    private void handleCheckedRowForImageStudy(NCIAImageAggregator imageAggregator, DisplayableResultRow row) {
        StudySubjectAssignment studySubjectAssignment = row.getSubjectAssignment();
        if (studySubjectAssignment != null) {
            studySubjectAssignment = getDao().get(row.getSubjectAssignment().getId(), StudySubjectAssignment.class);
        }
        if (studySubjectAssignment != null 
            && studySubjectAssignment.getImageStudyCollection() != null
            && !studySubjectAssignment.getImageStudyCollection().isEmpty()) {
            for (ImageSeriesAcquisition imageStudy : studySubjectAssignment.getImageStudyCollection()) {
                imageAggregator.getImageStudyIDs().add(imageStudy.getIdentifier());
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public File createCsvFileFromGenomicResults(GenomicDataQueryResult result) {
        File csvFile = new File(fileManager.getNewTemporaryDirectory("tempGenomicResultsDownload"), 
                "genomicData-" + System.currentTimeMillis() + ".csv");
        return GenomicDataFileWriter.writeAsCsv(result, csvFile);
    }

    /**
     * {@inheritDoc}
     */
    public void save(Query query) {
        if (query.getId() == null) {
            getDao().save(query);
        } else {
            getDao().merge(query);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete(Query query) {
        getDao().delete(query);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> validateGeneSymbols(StudySubscription studySubscription, List<String> geneSymbols)
            throws GenesNotFoundInStudyException {
        List<String> genesNotFound = new ArrayList<String>();
        Set<String> genesInStudy = getDao().retrieveGeneSymbolsInStudy(geneSymbols, studySubscription.getStudy());
        if (genesInStudy.isEmpty()) {
            throw new GenesNotFoundInStudyException("None of the specified genes were found in study.");
        }
        for (String geneSymbol : geneSymbols) {
            if (!Cai2Util.containsIgnoreCase(genesInStudy, geneSymbol)) {
                genesNotFound.add(geneSymbol);
            }
        }
        if (!genesNotFound.isEmpty()) {
            Collections.sort(genesNotFound);
        }
        return genesNotFound;
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }


}
