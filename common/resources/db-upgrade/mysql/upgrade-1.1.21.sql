insert into CONFIGURATION_PARAMETER (parameter, raw_value) values
    ("CAARRAY_URL", "https://array.nci.nih.gov/caarray");
update SERVER_CONNECTION_PROFILE set URL="https://array.nci.nih.gov/caarray"
    where HOSTNAME = "array.nci.nih.gov" and (URL = "" or URL is null);