/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2;

import java.io.File;

public class TestDataFiles {

    private static final String AFFYMETRIX_DATA_FILE_DIRECTORY_PATH = "/arraydata/affymetrix";
    private static final String AGILENT_DATA_FILE_DIRECTORY_PATH = "/arraydata/agilent";

    public static final String VALID_FILE_RESOURCE_PATH = "/csvtestclinical.csv";
    public static final String INVALID_FILE_DUPLICATE_IDS_RESOURCE_PATH = "/csvtestclinical-duplicateids.csv";
    public static final String INVALID_FILE_TOO_LONG_IDS_RESOURCE_PATH = "/csvtestclinical-toolongids.csv";
    public static final String VALID_ANNOTATION_GROUP_FILE_PATH = "/csvtestclinical_annotationGroup.csv";
    public static final String VALID_FILE_TIMEPOINT_RESOURCE_PATH = "/csvtestclinical-timepoint.csv";
    public static final String INVALID_FILE_MISSING_VALUE_RESOURCE_PATH = "/csvtestclinical-missing-value.csv";
    public static final String INVALID_FILE_EMPTY_RESOURCE_PATH = "/emptyfile.txt";
    public static final String INVALID_FILE_NO_DATA_RESOURCE_PATH = "/csvtestclinical-no-data.csv";
    public static final String INVALID_FILE_DOESNT_EXIST_PATH = "nofile.txt";
    public static final String REMBRANDT_CLINICAL_FILE_PATH = "/rembrandt_clinical_Aug08_subset.csv";
    public static final String REMBRANDT_ANNOTATION_GROUP_FILE_PATH = "/rembrandt_annotation_group.csv";
    public static final String REMBRANDT_SAMPLE_MAPPING_FILE_PATH = "/rembrandt_caarray_sample_mapping.csv";
    public static final String REMBRANDT_CONTROL_SAMPLE_SET_NAME = "Rembrandt Control Sample Set";
    public static final String REMBRANDT_CONTROL_SAMPLES_FILE_PATH = "/rembrandt_control_samples.csv";
    public static final String SHORT_REMBRANDT_CONTROL_SAMPLE_SET_NAME = "Short Rembrandt Control Sample Set";
    public static final String SHORT_REMBRANDT_CONTROL_SAMPLES_FILE_PATH = "/short_rembrandt_control_samples.csv";
    public static final String SHORT_REMBRANDT_SAMPLE_MAPPING_FILE_PATH = "/short_rembrandt_sample_mapping.csv";
    public static final String GENE_LIST_SAMPLES_FILE_PATH = "/gene_list_samples.csv";
    public static final String JAGLA_00034_CONTROL_SAMPLES_SET_NAME = "JAGLA 00034 Control Sample Set";
    public static final String JAGLA_00034_CONTROL_SAMPLES_FILE_PATH = "/jagla_0034_control_samples.csv";
    public static final String VASARI_IMAGE_ANNOTATION_FILE_PATH = "/vasari_image_annotation_mapped.csv";
    public static final String REMBRANDT_IMAGE_STUDIES_TO_SUBJECT_FILE_PATH = "/rembrandt_ncia_image_studies.csv";
    public static final String SIMPLE_AGILENT_SAMPLE_MAPPING_FILE_PATH = "/simple_Agilent_sample_mapping.csv";
    public static final String SIMPLE_SAMPLE_MAPPING_FILE_PATH = "/simple_sample_mapping.csv";
    public static final String SIMPLE_IMAGE_MAPPING_FILE_PATH = "/simple_image_mapping.csv";
    public static final String SIMPLE_EXTERNAL_LINKS_FILE_PATH = "/simple_external_links.csv";
    public static final String ISPY_IMAGE_ANNOTATION_FILE_PATH = "/ispy_image_annotations.csv";
    public static final String ISPY_IMAGE_SERIES_TO_SUBJECT_FILE_PATH = "/ispy_clinical_image_mapping.csv";
    public static final String REMBRANDT_NCRI_CLINICAL_FILE_PATH = "/rembrandt_clinical_Aug08_subset_mod_for_NCRI.csv";
    public static final String REMBRANDT_NCRI_IMAGE_ANNOTATION_FILE_PATH = "/ncri_image_annotations.csv";
    public static final String REMBRANDT_NCRI_IMAGE_SERIES_TO_SUBJECT_FILE_PATH = "/ncri_image_mapping.csv";
    public static final String REMBRANDT_NCRI_SAMPLE_MAPPING_FILE_PATH = "/ncri_sample_mapping.csv";
    public static final String REMBRANDT_NCRI_COPY_NUMBER_MAPPING_FILE_PATH = "/rembrandt_copy_number_mapping.csv";
    public static final String NCRI_LOGO_FILE_PATH = "/ncri_logo.jpg";
    public static final String HG18_GENE_LOCATIONS_FILE_PATH = "/genome2gene-hg18-20100817.dat";
    public static final String HG19_GENE_LOCATIONS_FILE_PATH = "/genome2gene-hg19-20100817.dat";
    public static final String HG18_GENE_LOCATIONS_SMALL_FILE_PATH = "/glist-hg18-small.txt";

    public static final File VALID_FILE = getFile(VALID_FILE_RESOURCE_PATH);
    public static final File INVALID_FILE_DUPLICATE_IDS = getFile(INVALID_FILE_DUPLICATE_IDS_RESOURCE_PATH);
    public static final File INVALID_FILE_TOO_LONG_IDS = getFile(INVALID_FILE_TOO_LONG_IDS_RESOURCE_PATH);
    public static final File VALID_ANNOTATION_GROUP_FILE = getFile(VALID_ANNOTATION_GROUP_FILE_PATH);
    public static final File VALID_FILE_TIMEPOINT = getFile(VALID_FILE_TIMEPOINT_RESOURCE_PATH);
    public static final File INVALID_FILE_MISSING_VALUE = getFile(INVALID_FILE_MISSING_VALUE_RESOURCE_PATH);
    public static final File INVALID_FILE_EMPTY = getFile(INVALID_FILE_EMPTY_RESOURCE_PATH);
    public static final File INVALID_FILE_NO_DATA = getFile(INVALID_FILE_NO_DATA_RESOURCE_PATH);
    public static final File INVALID_FILE_DOESNT_EXIST= new File(INVALID_FILE_DOESNT_EXIST_PATH);
    public static final File REMBRANDT_CLINICAL_FILE = getFile(REMBRANDT_CLINICAL_FILE_PATH);
    public static final File REMBRANDT_ANNOTATION_GROUP_FILE = getFile(REMBRANDT_ANNOTATION_GROUP_FILE_PATH);
    public static final File REMBRANDT_SAMPLE_MAPPING_FILE = getFile(REMBRANDT_SAMPLE_MAPPING_FILE_PATH);
    public static final File REMBRANDT_CONTROL_SAMPLES_FILE = getFile(REMBRANDT_CONTROL_SAMPLES_FILE_PATH);
    public static final File SHORT_REMBRANDT_SAMPLE_MAPPING_FILE = getFile(SHORT_REMBRANDT_SAMPLE_MAPPING_FILE_PATH);
    public static final File SHORT_REMBRANDT_CONTROL_SAMPLES_FILE = getFile(SHORT_REMBRANDT_CONTROL_SAMPLES_FILE_PATH);
    public static final File SIMPLE_AGILENT_SAMPLE_MAPPING_FILE = getFile(SIMPLE_AGILENT_SAMPLE_MAPPING_FILE_PATH);
    public static final File SIMPLE_SAMPLE_MAPPING_FILE = getFile(SIMPLE_SAMPLE_MAPPING_FILE_PATH);
    public static final File SIMPLE_IMAGE_MAPPING_FILE = getFile(SIMPLE_IMAGE_MAPPING_FILE_PATH);
    public static final File SIMPLE_EXTERNAL_LINKS_FILE = getFile(SIMPLE_EXTERNAL_LINKS_FILE_PATH);
    public static final File VASARI_IMAGE_ANNOTATION_FILE = getFile(VASARI_IMAGE_ANNOTATION_FILE_PATH);
    public static final File REMBRANDT_IMAGE_STUDIES_TO_SUBJECT_FILE = getFile(REMBRANDT_IMAGE_STUDIES_TO_SUBJECT_FILE_PATH);
    public static final File ISPY_IMAGE_ANNOTATION_FILE = getFile(ISPY_IMAGE_ANNOTATION_FILE_PATH);
    public static final File ISPY_IMAGE_SERIES_TO_SUBJECT_FILE = getFile(ISPY_IMAGE_SERIES_TO_SUBJECT_FILE_PATH);
    public static final File NCRI_LOGO = getFile(NCRI_LOGO_FILE_PATH);
    public static final File HG18_GENE_LOCATIONS_FILE = getFile(HG18_GENE_LOCATIONS_FILE_PATH);
    public static final File HG19_GENE_LOCATIONS_FILE = getFile(HG19_GENE_LOCATIONS_FILE_PATH);
    public static final File HG18_GENE_LOCATIONS_SMALL_FILE = getFile(HG18_GENE_LOCATIONS_SMALL_FILE_PATH);

    // Test genomic data files
    public static final String TEST_AGILENT_SINGLE_SAMPLE_MAPPING_FILE_PATH = "/test_Agilent_single_sample_mapping.csv";
    public static final File TEST_AGILENT_SINGLE_SAMPLE_MAPPING_FILE = getFile(TEST_AGILENT_SINGLE_SAMPLE_MAPPING_FILE_PATH);
    public static final String TEST_AGILENT_MULTI_SAMPLE_MAPPING_FILE_PATH = "/test_Agilent_multi_sample_mapping.csv";
    public static final File TEST_AGILENT_MULTI_SAMPLE_MAPPING_FILE = getFile(TEST_AGILENT_MULTI_SAMPLE_MAPPING_FILE_PATH);


    // Files for DC Lung Study (liu-00175 in caarray-stage)
    public static final String DC_LUNG_CLINICAL_FILE_PATH = "/dc_lung_clinical_data.csv";
    public static final String DC_LUNG_SAMPLE_MAPPING_FILE_PATH = "/dc_lung_sample_mapping.csv";
    public static final String DC_LUNG_SAMPLE_MAPPING_FULL_FILE_PATH = "/dc_lung_sample_mapping_full.csv";
    public static final String DC_LUNG_ANNOTATION_DEFINITIONS_FILE_PATH = "/dc_lung_annotation_definitions.csv";

    public static final File DC_LUNG_CLINICAL_FILE = getFile(DC_LUNG_CLINICAL_FILE_PATH);
    public static final File DC_LUNG_SAMPLE_MAPPING_FILE = getFile(DC_LUNG_SAMPLE_MAPPING_FILE_PATH);
    public static final File DC_LUNG_SAMPLE_MAPPING_FULL_FILE = getFile(DC_LUNG_SAMPLE_MAPPING_FULL_FILE_PATH);
    public static final File DC_LUNG_ANNOTATION_DEFINITIONS_FILE = getFile(DC_LUNG_ANNOTATION_DEFINITIONS_FILE_PATH);

    // Files for public DC Lung Study
    public static final String DC_LUNG_PUBLIC_CLINICAL_FILE_PATH = "/dc_lung_clinical_data_public.csv";
    public static final String DC_LUNG_PUBLIC_SAMPLE_MAPPING_FILE_PATH = "/dc_lung_sample_mapping_full.csv";
    public static final String DC_LUNG_PUBLIC_CONTROL_SAMPLE_SET_NAME = "DC Lung Control_Sample Set";
    public static final String DC_LUNG_PUBLIC_CONTROL_SAMPLE_MAPPING_FILE_PATH = "/dc_lung_control_samples.csv";
    public static final String DC_LUNG_PUBLIC_ANNOTATION_GROUP_FILE_PATH = "/dc_lung_public_annotation_group.csv";

    public static final File DC_LUNG_PUBLIC_CLINICAL_FILE = getFile(DC_LUNG_PUBLIC_CLINICAL_FILE_PATH);
    public static final File DC_LUNG_PUBLIC_SAMPLE_MAPPING_FILE = getFile(DC_LUNG_PUBLIC_SAMPLE_MAPPING_FILE_PATH);
    public static final File DC_LUNG_PUBLIC_CONTROL_SAMPLE_MAPPING_FILE = getFile(DC_LUNG_PUBLIC_CONTROL_SAMPLE_MAPPING_FILE_PATH);
    public static final File DC_LUNG_PUBLIC_ANNOTATION_GROUP_FILE = getFile(DC_LUNG_PUBLIC_ANNOTATION_GROUP_FILE_PATH);


    // Samples of genelist file upload
    public static final File GENE_LIST_SAMPLES_FILE = getFile(GENE_LIST_SAMPLES_FILE_PATH);

    // File for Annotation Group
    public static final String ANNOTATION_GROUP_FILE_PATH = "/csvtest_annotationGroup.csv";
    public static final File ANNOTATION_GROUP_FILE = getFile(ANNOTATION_GROUP_FILE_PATH);

    // Samples from caArray Experiment jagla-00034
    public static final File JAGLA_00034_CONTROL_SAMPLES_FILE = getFile(JAGLA_00034_CONTROL_SAMPLES_FILE_PATH);

    // Trial NCRI from NCIA
    public static final File REMBRANDT_NCRI_CLINICAL_FILE = getFile(REMBRANDT_NCRI_CLINICAL_FILE_PATH);
    public static final File REMBRANDT_NCRI_IMAGE_ANNOTATION_FILE = getFile(REMBRANDT_NCRI_IMAGE_ANNOTATION_FILE_PATH);
    public static final File REMBRANDT_NCRI_IMAGE_SERIES_TO_SUBJECT_FILE = getFile(REMBRANDT_NCRI_IMAGE_SERIES_TO_SUBJECT_FILE_PATH);
    public static final File REMBRANDT_NCRI_SAMPLE_MAPPING_FILE = getFile(REMBRANDT_NCRI_SAMPLE_MAPPING_FILE_PATH);
    public static final File REMBRANDT_NCRI_COPY_NUMBER_MAPPING_FILE = getFile(REMBRANDT_NCRI_COPY_NUMBER_MAPPING_FILE_PATH);


    // Copy number test data
    public static final String HIND_COPY_NUMBER_CHP_FILE_PATH = "/arraydata/affymetrix/E10003_T_Hind.CN4.cnchp";
    public static final File HIND_COPY_NUMBER_CHP_FILE = getFile(HIND_COPY_NUMBER_CHP_FILE_PATH);
    public static final String XBA_COPY_NUMBER_CHP_FILE_PATH = "/arraydata/affymetrix/E07733_T_Xba.CN4.cnchp";
    public static final File XBA_COPY_NUMBER_CHP_FILE = getFile(XBA_COPY_NUMBER_CHP_FILE_PATH);

    public static final String AGILENT_COPY_NUMBER_DATA_FILE_PATH = "/arraydata/agilent/mskcc.org_OV.HG-CGH-244A.9.data.txt";
    public static final File AGILENT_COPY_NUMBER_DATA_FILE = getFile(AGILENT_COPY_NUMBER_DATA_FILE_PATH);

    public static final String REMBRANDT_COPY_NUMBER_FILE_PATH = "/rembrandt_copy_number_mapping.csv";
    public static final File REMBRANDT_COPY_NUMBER_FILE = getFile(REMBRANDT_COPY_NUMBER_FILE_PATH);

    public static final String REMBRANDT_COPY_NUMBER_5SAMPLES_FILE_PATH = "/rembrandt_copy_number_mapping_5samples.csv";
    public static final File REMBRANDT_COPY_NUMBER_5SAMPLES_FILE = getFile(REMBRANDT_COPY_NUMBER_5SAMPLES_FILE_PATH);

    public static final String REMBRANDT_COPY_NUMBER_SINGLE_FILE_PATH = "/rembrandt_copy_number_mapping_Agilent-0244A.csv";
    public static final File REMBRANDT_COPY_NUMBER_SINGLE_FILE = getFile(REMBRANDT_COPY_NUMBER_SINGLE_FILE_PATH);

    public static final String SHORT_COPY_NUMBER_FILE_PATH = "/short_copy_number_mapping.csv";
    public static final File SHORT_COPY_NUMBER_FILE = getFile(SHORT_COPY_NUMBER_FILE_PATH);

    public static final String SHORT_AGILENT_COPY_NUMBER_FILE_PATH = "/short_agilent_copy_number_mapping.csv";
    public static final File SHORT_AGILENT_COPY_NUMBER_FILE = getFile(SHORT_AGILENT_COPY_NUMBER_FILE_PATH);

    public static final String TCGA_AGILENT_COPY_NUMBER_MAPPING_FILE_PATH = "/tcga_agilent_copy_number_mapping.csv";
    public static final File TCGA_AGILENT_COPY_NUMBER_MAPPING_FILE = getFile(TCGA_AGILENT_COPY_NUMBER_MAPPING_FILE_PATH);

    public static final String TCGA_AGILENT_COPY_NUMBER_MAPPING_SINGLE_SAMPLE_FILE_PATH = "/tcga_agilent_copy_number_mapping_single_sample.csv";
    public static final File TCGA_AGILENT_COPY_NUMBER_MAPPING_SINGLE_SAMPLE_FILE = getFile(TCGA_AGILENT_COPY_NUMBER_MAPPING_SINGLE_SAMPLE_FILE_PATH);

    public static final String TCGA_LEVEL_2_DATA_FILE_PATH = "/arraydata/agilent/tcga_level_2_test_data.txt";
    public static final File TCGA_LEVEL_2_DATA_FILE = getFile(TCGA_LEVEL_2_DATA_FILE_PATH);

    public static final String TCGA_LEVEL_2_DATA_FILE_ONE_HEADER_PATH = "/arraydata/agilent/tcga_level_2_test_data_one_header.txt";
    public static final File TCGA_LEVEL_2_DATA_FILE_ONE_HEADER = getFile(TCGA_LEVEL_2_DATA_FILE_ONE_HEADER_PATH);

    public static final String TCGA_LEVEL_2_DATA_SINGLE_SAMPLE_FILE_PATH = "/arraydata/agilent/tcga_level_2_single_sample_test_data.txt";
    public static final File TCGA_LEVEL_2_DATA_SINGLE_SAMPLE_FILE = getFile(TCGA_LEVEL_2_DATA_SINGLE_SAMPLE_FILE_PATH);

    public static final String HUAITIAN_LEVEL_2_DATA_FILE_PATH = "/arraydata/agilent/Huaitian_level2_test_data.txt";
    public static final File HUAITIAN_LEVEL_2_DATA_FILE = getFile(HUAITIAN_LEVEL_2_DATA_FILE_PATH);

    // Analysis Data Files
    // CMS
    public static final String CMS_CLASSIFICATIONS_FILE_PATH = "/cms_classifications.cls";
    public static final File CMS_CLASSIFICATIONS_FILE = getFile(CMS_CLASSIFICATIONS_FILE_PATH);

    public static final String CMS_GCT_FILE_PATH = "/cms_features.gct";
    public static final File CMS_GCT_FILE = getFile(CMS_GCT_FILE_PATH);

    // PCA
    public static final String PCA_TRAIN_FILE_PATH = "/pca_train.gct";
    public static final File PCA_TRAIN_FILE = getFile(PCA_TRAIN_FILE_PATH);

    // GISTIC
    public static final String GISTIC_CNV_FILE_PATH = "/gistic_cnv.txt";
    public static final File GISTIC_CNV_FILE = getFile(GISTIC_CNV_FILE_PATH);

    public static final String GISTIC_MARKERS_FILE_PATH = "/gistic_markers.txt";
    public static final File GISTIC_MARKERS_FILE = getFile(GISTIC_MARKERS_FILE_PATH);

    public static final String GISTIC_SAMPLES_FILE_PATH = "/gistic_samples.txt";
    public static final File GISTIC_SAMPLES_FILE = getFile(GISTIC_SAMPLES_FILE_PATH);

    public static final String GISTIC_RESULT_FILE_PATH = "/GISTIC_RESULTS.zip";
    public static final File GISTIC_RESULT_FILE = getFile(GISTIC_RESULT_FILE_PATH);

    // Heat Map
    public static final String HEATMAP_SMALL_BINS_FILE_PATH = "/bins10K.dat";
    public static final String HEATMAP_LARGE_BINS_FILE_PATH = "/bins200K.dat";
    public static final File HEATMAP_SMALL_BINS_FILE = getFile(HEATMAP_SMALL_BINS_FILE_PATH);
    public static final File HEATMAP_LARGE_BINS_FILE = getFile(HEATMAP_LARGE_BINS_FILE_PATH);

    // Platform data files
    public static File getAffymetrixDataFile(String filename) {
        return getFile(AFFYMETRIX_DATA_FILE_DIRECTORY_PATH + "/" + filename);
    }

    public static File getAgilentDataFile(String filename) {
        return getFile(AGILENT_DATA_FILE_DIRECTORY_PATH + "/" + filename);
    }

    public static File getFile(String resourcePath) {
        return new File(TestDataFiles.class.getResource(resourcePath).getFile());
    }


}
