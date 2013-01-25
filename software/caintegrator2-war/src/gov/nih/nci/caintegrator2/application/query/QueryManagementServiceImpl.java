/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.external.ncia.NCIAImageAggregationTypeEnum;
import gov.nih.nci.caintegrator2.external.ncia.NCIAImageAggregator;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableResultRow;

import java.io.File;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the QueryManagementService interface.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See handleCheckedRowForImageStudy()
@Transactional(propagation = Propagation.REQUIRED)
public class QueryManagementServiceImpl implements QueryManagementService {
    
    private CaIntegrator2Dao dao;
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
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        QueryTranslator queryTranslator = new QueryTranslator(query, dao, arrayDataService, resultHandler);
        return queryTranslator.execute();
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public GenomicDataQueryResult executeGenomicDataQuery(Query query) throws InvalidCriterionException {
        GenomicQueryHandler handler = new GenomicQueryHandler(query, dao, arrayDataService);
        return handler.execute();
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
            studySubjectAssignment = dao.get(row.getSubjectAssignment().getId(), StudySubjectAssignment.class);
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
        File csvFile = new File(fileManager.getUserDirectory(result.getQuery().getSubscription()), "genomicData.csv");
        return GenomicDataFileWriter.writeAsCsv(result, csvFile);
    }

    /**
     * {@inheritDoc}
     */
    public void save(Query query) {
        if (query.getId() == null) {
            dao.save(query);
        } else {
            dao.merge(query);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete(Query query) {
        dao.delete(query);
    }

    /**
     * @param dao the dao to set.
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
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
