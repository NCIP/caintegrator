/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2;

import java.io.File;

public class TestArrayDesignFiles {

    public static final String EMPTY_PATH = "/arraydesign/affymetrix/emptyfile.csv";
    public static final File EMPTY_FILE = getFile(EMPTY_PATH);
    
    // Affymetrix files
    public static final String HG_U133_PLUS_2_CDF_PATH = "/arraydesign/affymetrix/HG-U133_Plus_2.cdf";
    public static final String HG_U133A_CDF_PATH = "/arraydesign/affymetrix/HG-U133A.cdf";
    public static final String HG_U133_PLUS_2_ANNOTATION_PATH = "/arraydesign/affymetrix/HG-U133_Plus_2.na26.annot.csv";
    public static final String HG_U133_PLUS_2_ANNOTATION_TEST_PATH = "/arraydesign/affymetrix/HG-U133_Plus_2.na26.annot_Test.csv";
    public static final String HG_U133A_ANNOTATION_PATH = "/arraydesign/affymetrix/HG-U133A.na27.annot.csv";
    public static final String YEAST_2_CDF_PATH = "/arraydesign/affymetrix/Yeast_2.cdf";
    public static final String YEAST_2_ANNOTATION_PATH = "/arraydesign/affymetrix/Yeast_2.na26.annot.csv";
    public static final String TEST3_ANNOTATION_PATH = "/arraydesign/affymetrix/Test3.annot.csv";
    public static final String HG_U133B_ANNOTATION_PATH = "/arraydesign/affymetrix/HG-U133B.na27.annot.csv";
    public static final String HG_U95AV2_ANNOTATION_PATH = "/arraydesign/affymetrix/HG_U95Av2.na27.annot.csv";
    public static final String HG_U95B_ANNOTATION_PATH = "/arraydesign/affymetrix/HG_U95B.na27.annot.csv";
    public static final String HG_U95C_ANNOTATION_PATH = "/arraydesign/affymetrix/HG_U95C.na27.annot.csv";
    public static final String HG_U95D_ANNOTATION_PATH = "/arraydesign/affymetrix/HG_U95D.na27.annot.csv";
    public static final String HG_U133B_CDF_PATH = "/arraydesign/affymetrix/HG-U133B.cdf";
    public static final String HG_U95AV2_CDF_PATH = "/arraydesign/affymetrix/HG_U95Av2.CDF";
    public static final String HG_U95B_CDF_PATH = "/arraydesign/affymetrix/HG_U95B.CDF";
    public static final String HG_U95C_CDF_PATH = "/arraydesign/affymetrix/HG_U95C.CDF";
    public static final String HG_U95D_CDF_PATH = "/arraydesign/affymetrix/HG_U95D.CDF";
    public static final String MAPPING_50K_HIND_ANNOTATION_PATH = "/arraydesign/affymetrix/Mapping50K_Hind240.na27.annot.csv";
    public static final String MAPPING_50K_HIND_ANNOTATION_TEST_PATH = "/arraydesign/affymetrix/Mapping50K_Hind240.na27.annot_Test.csv";
    public static final String MAPPING_50K_HIND_CDF_PATH = "/arraydesign/affymetrix/Mapping50K_Hind240.cdf";
    public static final String MAPPING_50K_XBA_CDF_PATH = "/arraydesign/affymetrix/Mapping50K_Xba240.CDF";
    public static final String MAPPING_50K_XBA_ANNOTATION_PATH = "/arraydesign/affymetrix/Mapping50K_Xba240.na27.annot.csv";
    public static final String MAPPING_50K_XBA_ANNOTATION_TEST_PATH = "/arraydesign/affymetrix/Mapping50K_Xba240.na27.annot_Test.csv";
    public static final String GENOME_SNP6_CN_ANNOTATION_TEST_PATH = "/arraydesign/affymetrix/GenomeWideSNP_6.cn.na30.annot_Small.csv";
    
    public static final File HG_U133A_CDF_FILE = getFile(HG_U133A_CDF_PATH);
    public static final File HG_U133A_ANNOTATION_FILE = getFile(HG_U133A_ANNOTATION_PATH);
    public static final File HG_U133_PLUS_2_CDF_FILE = getFile(HG_U133_PLUS_2_CDF_PATH);
    public static final File HG_U133_PLUS_2_ANNOTATION_FILE = getFile(HG_U133_PLUS_2_ANNOTATION_PATH);
    public static final File HG_U133_PLUS_2_ANNOTATION_TEST_FILE = getFile(HG_U133_PLUS_2_ANNOTATION_TEST_PATH);
    public static final File YEAST_2_ANNOTATION_FILE = getFile(YEAST_2_ANNOTATION_PATH);
    public static final File YEAST_2_CDF_FILE = getFile(YEAST_2_CDF_PATH);
    public static final File TEST3_ANNOTATION_FILE = getFile(TEST3_ANNOTATION_PATH);
    public static final File HG_U133B_ANNOTATION_FILE = getFile(HG_U133B_ANNOTATION_PATH);
    public static final File HG_U95AV2_ANNOTATION_FILE = getFile(HG_U95AV2_ANNOTATION_PATH);
    public static final File HG_U95B_ANNOTATION_FILE = getFile(HG_U95B_ANNOTATION_PATH);
    public static final File HG_U95C_ANNOTATION_FILE = getFile(HG_U95C_ANNOTATION_PATH);
    public static final File HG_U95D_ANNOTATION_FILE = getFile(HG_U95D_ANNOTATION_PATH);
    public static final File HG_U133B_CDF_FILE = getFile(HG_U133B_CDF_PATH);
    public static final File HG_U95AV2_CDF_FILE = getFile(HG_U95AV2_CDF_PATH);
    public static final File HG_U95B_CDF_FILE = getFile(HG_U95B_CDF_PATH);
    public static final File HG_U95C_CDF_FILE = getFile(HG_U95C_CDF_PATH);
    public static final File HG_U95D_CDF_FILE = getFile(HG_U95D_CDF_PATH);
    public static final File MAPPING_50K_HIND_ANNOTATION_FILE = getFile(MAPPING_50K_HIND_ANNOTATION_PATH);
    public static final File MAPPING_50K_HIND_ANNOTATION_TEST_FILE = getFile(MAPPING_50K_HIND_ANNOTATION_TEST_PATH);
    public static final File MAPPING_50K_HIND_CDF_FILE = getFile(MAPPING_50K_HIND_CDF_PATH);
    public static final File MAPPING_50K_XBA_CDF = getFile(MAPPING_50K_XBA_CDF_PATH);
    public static final File MAPPING_50K_XBA_ANNOTATION_FILE = getFile(MAPPING_50K_XBA_ANNOTATION_PATH);
    public static final File MAPPING_50K_XBA_ANNOTATION_TEST_FILE = getFile(MAPPING_50K_XBA_ANNOTATION_TEST_PATH);
    public static final File GENOME_SNP6_CN_ANNOTATION_TEST_FILE = getFile(GENOME_SNP6_CN_ANNOTATION_TEST_PATH);


    // Agilent files
    public static final String HUMAN_GENOME_CGH244A_ANNOTATION_PATH = "/arraydesign/agilent/014693_D_GeneList_20070207.txt";
    public static final String AGILENT_G4502A_07_01_TCGA_XML_ANNOTATION_TEST_PATH = "/arraydesign/agilent/AgilentG4502A_07_01.tcga_Test.xml";
    public static final String AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_PATH = "/arraydesign/agilent/AgilentG4502A_07_01.tcga.adf";
    public static final String AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_TEST_PATH = "/arraydesign/agilent/AgilentG4502A_07_01.tcga_Test.adf";
    public static final String AGILENT_G4502A_07_01_ANNOTATION_TEST_PATH = "/arraydesign/agilent/AgilentG4502A_07_01_Test.tsv";
    public static final String AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_PATH = "/arraydesign/agilent/mskcc.org_TCGA_HG-CGH-244A_v081008.ADF";
    public static final String AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_PATH = "/arraydesign/agilent/mskcc.org_TCGA_HG-CGH-244A_v081008_Test.ADF";
    public static final String AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_PATH = "/arraydesign/agilent/mskcc.org_TCGA_HG-CGH-244A_v081008_Test_Bad.ADF";
    public static final String AGILENT_014693_XML_ANNOTATION_FILE_PATH = "/arraydesign/agilent/014693_D_F_20080627.xml";
    public static final String AGILENT_014693_XML_ANNOTATION_FILE_TEST_PATH = "/arraydesign/agilent/014693_D_F_20080627_Test.xml";
    
    public static final File HUMAN_GENOME_CGH244A_ANNOTATION_FILE = getFile(HUMAN_GENOME_CGH244A_ANNOTATION_PATH);
    public static final File AGILENT_G4502A_07_01_TCGA_XML_ANNOTATION_TEST_FILE = getFile(AGILENT_G4502A_07_01_TCGA_XML_ANNOTATION_TEST_PATH);
    public static final File AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_FILE = getFile(AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_PATH);
    public static final File AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_TEST_FILE = getFile(AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_TEST_PATH);
    public static final File AGILENT_G4502A_07_01_ANNOTATION_TEST_FILE = getFile(AGILENT_G4502A_07_01_ANNOTATION_TEST_PATH);
    public static final File AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_FILE = getFile(AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_PATH);
    public static final File AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_FILE = getFile(AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_PATH);
    public static final File AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_FILE = getFile(AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_PATH);
    public static final File AGILENT_014693_XML_ANNOTATION_FILE = getFile(AGILENT_014693_XML_ANNOTATION_FILE_PATH);
    public static final File AGILENT_014693_XML_ANNOTATION_TEST_FILE = getFile(AGILENT_014693_XML_ANNOTATION_FILE_TEST_PATH);
    
    public static File getFile(String resourcePath) {
        return new File(TestArrayDesignFiles.class.getResource(resourcePath).getFile());
    }

}
