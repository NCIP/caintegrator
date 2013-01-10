/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.analysis;

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Parse the gene map file from GISTIC result.
 */
public class GisticGeneMapFileParser {

    private static final String WIDE_PEAK_BOUNDARIES = "wide peak boundaries";
    private static final String GENES_IN_WIDE_PEAK = "genes in wide peak";

    private final Map<Integer, String> boundariesMap = new HashMap<Integer, String>();
    private final Map<String, List<Gene>> geneMap = new HashMap<String, List<Gene>>();
    private final Map<String, Gene> symbolGeneMap;

    /**
     * @param dao the caintegrator2 dao
     */
    public GisticGeneMapFileParser(CaIntegrator2Dao dao) {
        super();
        symbolGeneMap = dao.getGeneSymbolMap();
    }

    /**
     * @param inputFile input file
     * @throws IOException IO exception
     * @return gene map to wide peak boundaries
     */
    public Map<String, List<Gene>> parse(File inputFile) throws IOException {
        FileReader fileReader = new FileReader(inputFile);
        CSVReader csvReader = new CSVReader(fileReader, '\t');
        String[] fields;
        while ((fields = csvReader.readNext()) != null) {
            if (WIDE_PEAK_BOUNDARIES.equalsIgnoreCase(fields[0].trim())) {
                processBoundaries(fields);
            } else if (GENES_IN_WIDE_PEAK.equalsIgnoreCase(fields[0].trim())
                    || StringUtils.isBlank(fields[0])) {
                processGene(fields);
            }
        }
        csvReader.close();
        fileReader.close();
        removeBoundariesWithNoGenes();
        FileUtils.deleteQuietly(inputFile);
        return geneMap;
    }

    /**
     * @param fields
     */
    private void processBoundaries(String[] fields) {
        for (int i = 1; i < fields.length; i++) {
            boundariesMap.put(i, fields[i].trim());
            geneMap.put(fields[i].trim(), new ArrayList<Gene>());
        }
    }

    private void processGene(String[] fields) {
        for (int i = 1; i < fields.length; i++) {
            if (!StringUtils.isBlank(fields[i]) && !fields[i].contains("[")) {
                geneMap.get(boundariesMap.get(i)).add(lookupOrCreateGene(fields[i].trim()));
            }
        }
    }

    private Gene lookupOrCreateGene(String symbol) {
        String upperSymbol = symbol.toUpperCase(Locale.getDefault());
        Gene gene = symbolGeneMap.get(upperSymbol);
        if (gene == null) {
            gene = new Gene();
            gene.setSymbol(upperSymbol);
            symbolGeneMap.put(upperSymbol, gene);
        }
        return gene;
    }

    private void removeBoundariesWithNoGenes() {
        List<String> boundariesToRemove = new ArrayList<String>();
        for (String boundaries : geneMap.keySet()) {
            if (geneMap.get(boundaries).isEmpty()) {
                boundariesToRemove.add(boundaries);
            }
        }
        for (String boundaries : boundariesToRemove) {
            geneMap.remove(boundaries);
        }
    }
}
