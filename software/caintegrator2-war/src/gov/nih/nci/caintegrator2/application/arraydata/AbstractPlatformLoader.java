/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Base class for platform loaders.
 */
public abstract class AbstractPlatformLoader {
    
    
    private final Map<String, Gene> symbolToGeneMap = new HashMap<String, Gene>();
    private final Map<String, Integer> headerToIndexMap = new HashMap<String, Integer>();

    private CSVReader annotationFileReader;
    private final AbstractPlatformSource source;

    AbstractPlatformLoader(AbstractPlatformSource source) {
        this.source = source;
    }
    
    /**
     * 
     * @return the platform name
     * @throws PlatformLoadingException when error parsing the annotation file
     */
    public abstract String getPlatformName() throws PlatformLoadingException;

    abstract Platform load(CaIntegrator2Dao dao) throws PlatformLoadingException;

    abstract  Gene createGene(String symbol, String[] fields);

    /**
     * Create platform and set vendor.
     * @param platformVendor the vendor to set.
     * @return the platform
     */
    protected Platform createPlatform(PlatformVendorEnum platformVendor) {
        Platform platform = new Platform();
        platform.setVendor(platformVendor);
        return platform;
    }
    
    /**
     * Load the annotation file for the platform.
     * @param platform the platform
     * @param dao the caIntegrator2Dao
     * @throws PlatformLoadingException when error loading the annotation file.
     */
    protected void loadAnnotationFiles(Platform platform, CaIntegrator2Dao dao) throws PlatformLoadingException {
        try {
            for (File annotationFile : getAnnotationFiles()) {
                handleAnnotationFile(annotationFile, platform, dao);
            }
            dao.save(platform);
        } finally {
            closeAnnotationFileReader();
            cleanUp();
        }
    }
    
    abstract void handleAnnotationFile(File annotationFile, Platform platform, CaIntegrator2Dao dao)
    throws PlatformLoadingException;

    /**
     * 
     * @param headers the mapping header
     * @param requiredHeaders the headers to validate
     * @throws PlatformLoadingException when headers don't match
     */
    protected void loadAnnotationHeaders(String[] headers, String[] requiredHeaders)
    throws PlatformLoadingException {
        for (int i = 0; i < headers.length; i++) {
            headerToIndexMap.put(headers[i], i);
        }
        validate(requiredHeaders);
    }
    
    private void validate(String[] requiredHeaders) throws PlatformLoadingException {
        for (String header : requiredHeaders) {
            if (!headerToIndexMap.containsKey(header)) {
                throw new PlatformLoadingException("Invalid file format; Headers not match.");
            }
        }
    }

    /**
     * 
     * @param fields filed array
     * @param firstField the first field
     * @return true or false
     */
    protected boolean isAnnotationHeadersLine(String[] fields, String firstField) {
        return fields.length > 0 && firstField.equals(fields[0]);
    }

    /**
     * 
     * @param fields field array
     * @param symbol gene symbol
     * @param dao the caIntegratorDao
     * @return the gene
     */
    protected Gene lookupOrCreateGene(String[] fields, String symbol, CaIntegrator2Dao dao) {
        Gene gene = dao.getGene(symbol);
        if (gene == null) {
            gene = createGene(symbol, fields);
        }
        getSymbolToGeneMap().put(symbol.toUpperCase(Locale.getDefault()), gene);
        return gene;
    }

    /**
     * 
     * @param fields field array
     * @param header the header to get the value for
     * @return the value
     */
    protected String getAnnotationValue(String[] fields, String header) {
        if (getHeaderToIndexMap().containsKey(header)) {
            return fields[getHeaderToIndexMap().get(header)];
        }
        return "";
    }

    /**
     * 
     * @param fields field array
     * @param header the header
     * @param noValueSymbol the no value string
     * @return the value
     */
    protected String getAnnotationValue(String[] fields, String header, String noValueSymbol) {
        String value = fields[getHeaderToIndexMap().get(header)];
        return (value == null || value.equals(noValueSymbol)) ? null : value;
    }

    /**
     * Clean up at the end.
     */
    protected void cleanUp() {
        if (getSource().getDeleteFileOnCompletion()) {
            for (File file : getSource().getAnnotationFiles()) {
                file.delete();
            }
        }
    }

    /**
     * Close all files.
     */
    protected void closeAnnotationFileReader() {
        if (annotationFileReader != null) {
            try {
                annotationFileReader.close();
            } catch (IOException e) {
                getLogger().error("Couldn't close annotation file reader for file " 
                        + getAnnotationFileNames());
            }
        }
    }
    abstract Logger getLogger();

    CSVReader getAnnotationFileReader() {
        return annotationFileReader;
    }

    void setAnnotationFileReader(CSVReader annotationFileReader) {
        this.annotationFileReader = annotationFileReader;
    }

    Map<String, Gene> getSymbolToGeneMap() {
        return symbolToGeneMap;
    }

    Map<String, Integer> getHeaderToIndexMap() {
        return headerToIndexMap;
    }

    AbstractPlatformSource getSource() {
        return source;
    }

    final List<File> getAnnotationFiles() {
        return getSource().getAnnotationFiles();
    }

    final String getAnnotationFileNames() {
        return getSource().getAnnotationFileNames();
    }

}
