truncate table CONFIGURATION_PARAMETER;
insert into CONFIGURATION_PARAMETER (parameter, raw_value) values
    ("GRID_INDEX_URL", "@grid.index.url@"),
    ("NBIA_URL", "http://imaging.nci.nih.gov/wsrf/services/cagrid/NCIACoreService"),
    ("CAARRAY_HOST", "array.nci.nih.gov"),
    ("PREPROCESS_DATASET_URL", "http://node255.broadinstitute.org:6060/wsrf/services/cagrid/PreprocessDatasetMAGEService"),
    ("COMPARATIVE_MARKER_SELECTION_URL", "http://node255.broadinstitute.org:11010/wsrf/services/cagrid/ComparativeMarkerSelMAGESvc"),
    ("PCA_URL", "http://node255.broadinstitute.org:6060/wsrf/services/cagrid/PCA"),
    ("CA_DNA_COPY_URL", "http://ncias-d227-v.nci.nih.gov:8080/wsrf/services/cagrid/CaDNAcopy"),
    ("GISTIC_URL", "http://node255.broadinstitute.org:10010/wsrf/services/cagrid/Gistic"),
    ("GENE_PATTERN_URL", "http://genepattern.broadinstitute.org/gp/services/Analysis"),
    ("REGISTRATION_EMAIL_TO", "@user.admin.email.address.to@"),
    ("REGISTRATION_EMAIL_FROM", "@user.admin.email.address.from@"),
    ("UPT_URL", "@upt.url@");
