/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.external.caarray.SupplementalDataFile;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
@Transactional (propagation = Propagation.REQUIRED)
abstract class AbstractGenericSupplementalMappingFileHandler extends AbstractUnparsedSupplementalMappingFileHandler {

    static final String FILE_TYPE = "data";
    private final ReporterTypeEnum reporterType;
    private final ArrayDataType dataType;
    private final ArrayDataValueType dataValueType;
    
    AbstractGenericSupplementalMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
        if (genomicSource.isExpressionData()) {
            this.reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET;
            this.dataType = ArrayDataType.GENE_EXPRESSION;
            this.dataValueType = ArrayDataValueType.EXPRESSION_SIGNAL;
        } else {
            this.reporterType = ReporterTypeEnum.DNA_ANALYSIS_REPORTER;
            this.dataType = ArrayDataType.COPY_NUMBER;
            this.dataValueType = ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO;
        }
    }

    @Override
    List<ArrayDataValues> loadArrayData()
    throws DataRetrievalException, ConnectionException, ValidationException {
        loadMappingFile();
        List<ArrayDataValues> arrayDataValues = loadArrayDataValue();
        getDao().save(getGenomicSource().getStudyConfiguration());
        return arrayDataValues;
    }

    abstract List<ArrayDataValues> loadArrayDataValue()
    throws ConnectionException, DataRetrievalException, ValidationException;

    abstract void mappingSample(String subjectId, String sampleName, SupplementalDataFile supplementalDataFile)
    throws FileNotFoundException, ValidationException, ConnectionException, DataRetrievalException;
    
    /**
     * Mapping data.
     */
    protected class MappingData {
        private String subjectId;
        private String sampleName;
        private SupplementalDataFile dataFile;
        /**
         * @return the subjectId
         */
        public String getSubjectId() {
            return subjectId;
        }
        /**
         * @return the sampleName
         */
        public String getSampleName() {
            return sampleName;
        }
        /**
         * @return the dataFile
         */
        public SupplementalDataFile getDataFile() {
            return dataFile;
        }
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

    /**
     * @return the reporterType
     */
    protected ReporterTypeEnum getReporterType() {
        return reporterType;
    }

    /**
     * @return the dataType
     */
    protected ArrayDataType getDataType() {
        return dataType;
    }

    /**
     * @return the dataValueType
     */
    protected ArrayDataValueType getDataValueType() {
        return dataValueType;
    }

 }
