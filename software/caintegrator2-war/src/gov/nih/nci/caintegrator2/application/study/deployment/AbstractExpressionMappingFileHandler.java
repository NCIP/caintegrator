/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

/**
 * 
 */
public abstract class AbstractExpressionMappingFileHandler extends AbstractSupplementalMappingFileHandler {

    private static final String FILE_TYPE = "data";
    private final PlatformHelper platformHelper;
    private final Set<ReporterList> reporterLists;
    private ArrayDataValues arrayDataValues;

    AbstractExpressionMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
        this.platformHelper = new PlatformHelper(dao.getPlatform(genomicSource.getPlatformName()));
        this.reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        this.arrayDataValues = 
            new ArrayDataValues(platformHelper.getAllReportersByType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET));
    }
    
    abstract ArrayDataValues loadArrayData() throws DataRetrievalException, ConnectionException, ValidationException;
    
    /**
     * 
     * @param sample use to create the ArrayData
     * @return ArrayData
     */
    protected ArrayData createArrayData(Sample sample) {
        Array array = new Array();
        array.setPlatform(getPlatformHelper().getPlatform());
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setArray(array);
        if (!getReporterLists().isEmpty()) {
            arrayData.getReporterLists().addAll(getReporterLists());
            for (ReporterList reporterList : getReporterLists()) {
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

    /**
     * @return the arrayDataValues
     */
    protected ArrayDataValues getArrayDataValues() {
        return arrayDataValues;
    }

    /**
     * @param arrayDataValues the arrayDataValues to set
     */
    protected void setArrayDataValues(ArrayDataValues arrayDataValues) {
        this.arrayDataValues = arrayDataValues;
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

    @Override
    File getMappingFile() throws FileNotFoundException {
        return getGenomicSource().getSampleMappingFile();
    }

    /**
     * @return the platformHelper
     */
    protected PlatformHelper getPlatformHelper() {
        return platformHelper;
    }

    /**
     * @return the reporterLists
     */
    protected Set<ReporterList> getReporterLists() {
        return reporterLists;
    }

}
