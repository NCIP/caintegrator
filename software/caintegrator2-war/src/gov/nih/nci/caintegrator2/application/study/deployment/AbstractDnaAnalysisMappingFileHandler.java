/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides base handling to retrieve copy number data based on a copy number mapping file.
 */
public abstract class AbstractDnaAnalysisMappingFileHandler extends AbstractCaArrayFileHandler {

    private final CaIntegrator2Dao dao;
    
    AbstractDnaAnalysisMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService);
                this.dao = dao;
    }

    /**
     * 
     * @param sampleName the sample name to retrieve
     * @param subjectIdentifier the study assignment id
     * @return the sample object
     * @throws ValidationException Validation exception
     * @throws FileNotFoundException IO exception
     */
    protected Sample getSample(String sampleName, String subjectIdentifier)
    throws FileNotFoundException, ValidationException {
        Sample sample = getGenomicSource().getSample(sampleName);
        if (sample == null) {
            StudySubjectAssignment assignment = getSubjectAssignment(subjectIdentifier);
            sample = new Sample();
            sample.setName(sampleName);
            SampleAcquisition acquisition = new SampleAcquisition();
            acquisition.setAssignment(assignment);
            acquisition.setSample(sample);
            sample.setSampleAcquisition(acquisition);
            assignment.getSampleAcquisitionCollection().add(acquisition);
            getGenomicSource().getSamples().add(sample);
            dao.save(sample);
        }
        return sample;
    }

    /**
     * 
     * @return the mapping file.
     * @throws FileNotFoundException IO exception.
     */
    protected File getMappingFile() throws FileNotFoundException {
        return getGenomicSource().getDnaAnalysisDataConfiguration().getMappingFile();
    }

    abstract List<ArrayDataValues> loadArrayData()
    throws ConnectionException, DataRetrievalException, ValidationException;
    
    /**
     * Get the platform.
     * @param reporterListNames the set of report lists.
     * @return Platform
     * @throws ValidationException when platform not found or not unique.
     */
    protected Platform getPlatform(Set<String> reporterListNames) throws ValidationException {
        Set<Platform> platforms = new HashSet<Platform>();
        for (String reporterListName : reporterListNames) {
            ReporterList reporterList = dao.getReporterList(reporterListName);
            if (reporterList == null) {
                throw new ValidationException("There is no platform that supports chip type " + reporterListName);
            }
            platforms.add(reporterList.getPlatform());
        }
        if (platforms.size() > 1) {
            throw new ValidationException(
                    "DNA analysis data files for a single sample are mapped to multiple platforms.");
        }
        return platforms.iterator().next();
    }

    /**
     * Clean up.
     * @param dataFile the data file to delete.
     */
    protected void doneWithFile(File dataFile) {
        dataFile.delete();
    }
    
    abstract String getFileType();

    /**
     * Create the ArrayData for the sample.
     * @param sample the sample to get arrayData for.
     * @param reporterLists the set of report lists.
     * @return ArrayData
     */
    protected ArrayData createArrayData(Sample sample, Set<ReporterList> reporterLists) {
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
        if (!reporterLists.isEmpty()) {
            arrayData.getReporterLists().addAll(reporterLists);
            for (ReporterList reporterList : reporterLists) {
                reporterList.getArrayDatas().add(arrayData);    
            }
            array.setPlatform(reporterLists.iterator().next().getPlatform());
        }
        return arrayData;
    }

    private StudySubjectAssignment getSubjectAssignment(String subjectIdentifier)
    throws ValidationException, FileNotFoundException {
        StudySubjectAssignment assignment = 
            getGenomicSource().getStudyConfiguration().getSubjectAssignment(subjectIdentifier);
        if (assignment == null) {
            throw new ValidationException("Subject identifier " + subjectIdentifier + " in DNA analysis mapping file " 
                    + getMappingFile().getAbsolutePath() + " doesn't map to a known subject in the study.");
        }
        return assignment;
    }

    CaIntegrator2Dao getDao() {
        return dao;
    }

}
