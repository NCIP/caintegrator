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
import gov.nih.nci.caintegrator2.common.Cai2Util;
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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Provides base handling to retrieve copy number data based on a copy number mapping file.
 */
public abstract class AbstractCopyNumberMappingFileHandler {

    private final CaArrayFacade caArrayFacade;
    private final ArrayDataService arrayDataService;
    private final CaIntegrator2Dao dao;
    private final GenomicDataSourceConfiguration genomicSource;
    private final Map<Sample, List<String>> sampleToFilenamesMap = new HashMap<Sample, List<String>>();

    AbstractCopyNumberMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
                this.genomicSource = genomicSource;
                this.caArrayFacade = caArrayFacade;
                this.arrayDataService = arrayDataService;
                this.dao = dao;
    }

    List<ArrayDataValues> loadCopyNumberData() throws DataRetrievalException, ConnectionException, ValidationException {
        try {
            CSVReader reader = new CSVReader(new FileReader(getFile()));
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                String subjectId = fields[0].trim();
                String sampleName = fields[1].trim();
                String copyNumberFilename = fields[2].trim();
                parse(subjectId, sampleName, copyNumberFilename);
            }
            List<ArrayDataValues> arrayDataValues = loadArrayData();
            dao.save(genomicSource.getStudyConfiguration());
            reader.close();
            return arrayDataValues;
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Copy number mapping file not found: ", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read copy number mapping file: ", e);
        }
    }

    private void parse(String subjectIdentifier, String sampleName, String copyNumberFilename) 
    throws ValidationException, FileNotFoundException {
        StudySubjectAssignment assignment = getSubjectAssignment(subjectIdentifier);
        Sample sample = getSample(sampleName, assignment);
        addCopyNumberFile(sample, copyNumberFilename);
    }

    private void addCopyNumberFile(Sample sample, String copyNumberFilename) {
        List<String> filenames = sampleToFilenamesMap.get(sample);
        if (filenames == null) {
            filenames = new ArrayList<String>();
            sampleToFilenamesMap.put(sample, filenames);
        }
        filenames.add(copyNumberFilename);
    }

    private File getFile() throws FileNotFoundException {
        return genomicSource.getCopyNumberDataConfiguration().getMappingFile();
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
                    "Copy Number data files for a single sample are mapped to multiple platforms.");
        }
        return platforms.iterator().next();
    }

    abstract void doneWithFile(File dataFile);

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
        arrayData.setStudy(genomicSource.getStudyConfiguration().getStudy());
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
    
    File getDataFile(String copyNumberFilename) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        try {
            byte[] fileBytes = getCaArrayFacade().retrieveFile(getGenomicSource(), copyNumberFilename);
            File tempFile = File.createTempFile("temp", "." + getFileType());
            Cai2Util.byteArrayToFile(fileBytes, tempFile);
            return tempFile;
        } catch (FileNotFoundException e) {
            throw new ValidationException("Experiment " + getGenomicSource().getExperimentIdentifier() 
                    + " doesn't contain a file named " + copyNumberFilename, e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't write '" + getFileType() + "' file locally", e);
        }
    }
    
    /**
     * @return the file type
     */
    abstract String getFileType();

    private Sample getSample(String sampleName, StudySubjectAssignment assignment) {
        Sample sample = genomicSource.getSample(sampleName);
        if (sample == null) {
            sample = new Sample();
            sample.setName(sampleName);
            SampleAcquisition acquisition = new SampleAcquisition();
            acquisition.setAssignment(assignment);
            acquisition.setSample(sample);
            sample.setSampleAcquisition(acquisition);
            assignment.getSampleAcquisitionCollection().add(acquisition);
            genomicSource.getSamples().add(sample);
            dao.save(sample);
        }
        return sample;
    }

    private StudySubjectAssignment getSubjectAssignment(String subjectIdentifier)
    throws ValidationException, FileNotFoundException {
        StudySubjectAssignment assignment = 
            genomicSource.getStudyConfiguration().getSubjectAssignment(subjectIdentifier);
        if (assignment == null) {
            throw new ValidationException("Subject identifier " + subjectIdentifier + " in copy number mapping file " 
                    + getFile().getAbsolutePath() + " doesn't map to a known subject in the study.");
        }
        return assignment;
    }

    CaArrayFacade getCaArrayFacade() {
        return caArrayFacade;
    }

    ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    CaIntegrator2Dao getDao() {
        return dao;
    }

    GenomicDataSourceConfiguration getGenomicSource() {
        return genomicSource;
    }

    /**
     * @return the sampleToFilenamesMap
     */
    public Map<Sample, List<String>> getSampleToFilenamesMap() {
        return sampleToFilenamesMap;
    }

}
