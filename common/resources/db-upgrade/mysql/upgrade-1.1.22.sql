update SERVER_CONNECTION_PROFILE set URL="https://array-stage.nci.nih.gov/caarray"
    where HOSTNAME = "array-stage.nci.nih.gov" and (URL = "" or URL is null);
update SERVER_CONNECTION_PROFILE set URL="https://array-qa.nci.nih.gov/caarray"
    where HOSTNAME = "array-qa.nci.nih.gov" and (URL = "" or URL is null);