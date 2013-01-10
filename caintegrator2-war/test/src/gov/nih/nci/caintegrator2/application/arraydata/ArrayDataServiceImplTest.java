/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ArrayDataServiceImplTest {
 
    private ArrayDataServiceImpl arrayDataService;
    private CaIntegrator2DaoStub dao;
    private Query query;
    
    @Before
    public void setup() {
        dao = new GenomicDataTestDaoStub();
        dao.clear();
        arrayDataService = new ArrayDataServiceImpl();
        arrayDataService.setDao(dao);
        query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setSubscription(new StudySubscription());
        Study study = new Study();
        query.getSubscription().setStudy(study);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);
        studyConfiguration.getGenomicDataSources().add(new GenomicDataSourceConfiguration());
    }

    @Test
    public void testGetPlatformsInStudy() {
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);
        GenomicDataSourceConfiguration genomicDataSource1 = new GenomicDataSourceConfiguration();
        GenomicDataSourceConfiguration genomicDataSource2 = new GenomicDataSourceConfiguration();
        GenomicDataSourceConfiguration genomicDataSource3 = new GenomicDataSourceConfiguration();
        studyConfiguration.getGenomicDataSources().add(genomicDataSource1);
        studyConfiguration.getGenomicDataSources().add(genomicDataSource2);
        studyConfiguration.getGenomicDataSources().add(genomicDataSource3);
        genomicDataSource1.setPlatformName("Expression_1");
        genomicDataSource1.setDataType(PlatformDataTypeEnum.EXPRESSION);
        genomicDataSource2.setPlatformName("Expression_2");
        genomicDataSource2.setDataType(PlatformDataTypeEnum.EXPRESSION);
        genomicDataSource3.setPlatformName("Copy number");
        genomicDataSource3.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        
        Platform platform = arrayDataService.getPlatform("platform1");
        assertEquals("platform1", platform.getName());

        assertEquals(2,arrayDataService.getPlatformsInStudy(study, PlatformDataTypeEnum.EXPRESSION).size());
        assertEquals(1,arrayDataService.getPlatformsInStudy(study, PlatformDataTypeEnum.COPY_NUMBER).size());
        assertEquals(0,arrayDataService.getPlatformsInStudy(study, PlatformDataTypeEnum.SNP).size());
    }
    
    @Test
    public void testLoadGeneLocationFile() throws ValidationException, IOException {
        GeneLocationConfiguration geneLocationConf = 
            arrayDataService.loadGeneLocationFile(TestDataFiles.HG18_GENE_LOCATIONS_SMALL_FILE, 
                    GenomeBuildVersionEnum.HG18);
        assertEquals(GenomeBuildVersionEnum.HG18, geneLocationConf.getGenomeBuildVersion());
        assertEquals(3, geneLocationConf.getGeneLocations().size());
    }

    private class GenomicDataTestDaoStub extends CaIntegrator2DaoStub  {

        @Override
        public Platform getPlatform(String name) {
            Platform platform = new Platform();
            platform.setName(name);
            return platform;
        }

        @Override
        public List<Platform> retrievePlatformsForGenomicSource(GenomicDataSourceConfiguration genomicSource) {
            List<Platform> results = new ArrayList<Platform>();
            Platform platform = new Platform();
            platform.setName(genomicSource.getPlatformName());
            results.add(platform);
            return results;
        }
        
    }
}
