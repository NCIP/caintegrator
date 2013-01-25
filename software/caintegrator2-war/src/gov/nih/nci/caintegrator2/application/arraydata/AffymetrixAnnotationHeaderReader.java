/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Generic reader for Affymetrix annotation CSV files. Loads 
 */
class AffymetrixAnnotationHeaderReader {
    
    private final CSVReader reader;
    private String[] headers;
    private final Map<String, String> fileHeaders = new HashMap<String, String>();

    AffymetrixAnnotationHeaderReader(CSVReader reader) throws IOException, PlatformLoadingException {
        this.reader = reader;
        loadHeaders();
    }

    
    private void loadHeaders() throws IOException, PlatformLoadingException {
        String[] fields;
        while ((fields = reader.readNext()) != null) {
            if (isComment(fields)) {
                continue;
            } else if (isFileHeaderLine(fields)) {
                loadFileHeaderLine(fields);
            } else {
                headers = fields;
                return;
            }
        }        
        throw new PlatformLoadingException("Invalid Affymetrix annotation file; headers not found in file,");
    }

    private boolean isComment(String[] fields) {
        return fields.length > 0 && fields[0].startsWith("##");
    }


    private boolean isFileHeaderLine(String[] fields) {
        return fields.length > 0 && fields[0].startsWith("#%");
    }

    private void loadFileHeaderLine(String[] fields) {
        String[] parts = fields[0].substring(2).split("=");
        fileHeaders.put(parts[0], parts[1]);
    }

    @SuppressWarnings("PMD.MethodReturnsInternalArray") // not a problem
    String[] getDataHeaders() {
        return headers;
    }
    
    Map<String, String> getFileHeaders() {
        return fileHeaders;
    }
    
}
