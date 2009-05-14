truncate table CONFIGURATION_PARAMETER;
insert into CONFIGURATION_PARAMETER (parameter, raw_value) values
    ("GRID_INDEX_URL", "@grid.index.url@"),
    ("PREPROCESS_DATASET_URL", "http://node255.broad.mit.edu:6060/wsrf/services/cagrid/PreprocessDatasetMAGEService"),
    ("COMPARATIVE_MARKER_SELECTION_URL", "http://node255.broad.mit.edu:6060/wsrf/services/cagrid/ComparativeMarkerSelMAGESvc"),
    ("PCA_URL", "http://node255.broad.mit.edu:6060/wsrf/services/cagrid/PCA");
