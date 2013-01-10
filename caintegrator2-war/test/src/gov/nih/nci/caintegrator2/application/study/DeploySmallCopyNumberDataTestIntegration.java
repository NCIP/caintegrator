/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixSnpPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DeploySmallCopyNumberDataTestIntegration extends AbstractDeployStudyTestIntegration {

    private final static Logger LOGGER = Logger.getLogger(DeploySmallCopyNumberDataTestIntegration.class);

    @Test
    public void testDeployStudy() throws Exception {
        deployStudy();
        checkCopyNumberData();
    }

    @Override
    protected ArrayDataLoadingTypeEnum getCopyNumberLoadingType() {
        return ArrayDataLoadingTypeEnum.CNCHP;
    }

    @Override
    protected void configureSegmentationDataCalcuation(DnaAnalysisDataConfiguration dnaAnalysisDataConfiguration) {
        dnaAnalysisDataConfiguration.getSegmentationService().setUrl("http://bioconductor.nci.nih.gov:8080/wsrf/services/cagrid/CaDNAcopy");
        dnaAnalysisDataConfiguration.setRandomNumberSeed(1234567);
    }

    private void checkCopyNumberData() {
        Set<ArrayData> arrayDatas = getStudyConfiguration().getStudy().getArrayDatas(ReporterTypeEnum.DNA_ANALYSIS_REPORTER, null);
        assertEquals(1, arrayDatas.size());
        for (ArrayData arrayData : arrayDatas) {
            checkCopyNumberData(arrayData);
        }
    }

    private void checkCopyNumberData(ArrayData arrayData) {
        PlatformHelper platformHelper = new PlatformHelper(arrayData.getArray().getPlatform());
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addType(ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO);
        request.addArrayData(arrayData);
        request.addReporters(platformHelper.getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
        ArrayDataValues values = getArrayDataService().getData(request);
        int nonZeroValueCount = 0;
        for (AbstractReporter reporter : values.getReporters()) {
            if (values.getFloatValue(arrayData, reporter, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO) != 0) {
                nonZeroValueCount++;
            }
        }
        assertTrue(nonZeroValueCount > 50000);
        assertFalse(arrayData.getSegmentDatas().isEmpty());
    }

    @Override
    protected boolean getMapImages() {
        return false;
    }

    @Override
    protected boolean getLoadDesign() {
        return true;
    }

    @Override
    protected boolean getLoadSamples() {
        return true;
    }

    @Override
    protected String getCaArrayId() {
        return "jagla-00034";
    }

    @Override
    protected int getExpectedSampleCount() {
        return 3;
    }

    @Override
    protected int getExpectedMappedSampleCount() {
        return 2;
    }

    @Override
    protected int getExpectedControlSampleCount() {
        return 1;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getStudyName() {
        return "Rembrandt with Small Copy Number Data";
    }

    @Override
    protected File getCopyNumberFile() {
        return TestDataFiles.SHORT_COPY_NUMBER_FILE;
    }

    @Override
    protected String getCopyNumberPlatformName() {
       return "GeneChip Human Mapping 100K Set";
    }

    @Override
    protected AbstractPlatformSource[] getAdditionalPlatformSources() {
        List<File> files = new ArrayList<File>();
        files.add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        files.add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_FILE);
        return new AbstractPlatformSource[] {
                new AffymetrixSnpPlatformSource(files, getCopyNumberPlatformName())
        };
    }

    @Override
    protected String getCopyNumberCaArrayId() {
        return "liu-00252";
    }

    @Override
    protected File getAnnotationGroupFile() {
        return TestDataFiles.REMBRANDT_ANNOTATION_GROUP_FILE;
    }


    @Override
    protected File getSampleMappingFile() {
        return TestDataFiles.REMBRANDT_NCRI_SAMPLE_MAPPING_FILE;
    }

    @Override
    protected String getControlSampleSetName() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_SET_NAME;
    }

    @Override
    protected File getControlSamplesFile() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_FILE;
    }

    @Override
    protected String getControlSamplesFileName() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_FILE_PATH;
    }

    @Override
    protected File getSubjectAnnotationFile() {
        return TestDataFiles.REMBRANDT_NCRI_CLINICAL_FILE;
    }

    @Override
    protected AbstractPlatformSource getPlatformSource() {
        return new AffymetrixExpressionPlatformSource(TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE);
    }

    @Override
    protected String getDeathDateName() {
        return "Death Date";
    }

    @Override
    protected String getLastFollowupDateName() {
        return "Last Followup Date";
    }

    @Override
    protected String getSurvivalStartDateName() {
        return "Survival Start Date";
    }

    @Override
    protected int getExpectedNumberOfGeneReporters() {
        return 21432;
    }

    @Override
    protected int getExpectedNumberProbeSets() {
        return 54675;
    }

    @Override
    protected String getPlatformName() {
        return "HG-U133_Plus_2";
    }

    @Override
    protected PlatformVendorEnum getPlatformVendor() {
        return PlatformVendorEnum.AFFYMETRIX;
    }

    @Override
    protected ArrayDataLoadingTypeEnum getLoadingType() {
        return ArrayDataLoadingTypeEnum.PARSED_DATA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean getAuthorizeStudy() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getQueryFieldDescriptorName() {
        return "Gender";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getQueryAnnotationValue() {
        return "F";
    }

}
