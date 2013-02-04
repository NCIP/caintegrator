/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.GeneChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Loader for the Gene Location file, line format is the following:.
 * chromosomeNumber startPosition endPosition geneSymbol
 */
final class GeneLocationFileLoader {
    private static final char DELIMITER = ' ';
    
    private GeneLocationFileLoader() { }
    
    static void loadFile(GeneLocationConfiguration geneLocationConfiguration, 
            File geneLocationFile) throws ValidationException, IOException {
        CSVReader geneFileReader = new CSVReader(new FileReader(geneLocationFile), DELIMITER);
        String[] fields;
        while ((fields = geneFileReader.readNext()) != null) {
            if (fields.length != 4) {
                throw new ValidationException("A line in the Gene Location File has '" 
                        + fields.length + "' fields, expected number is 4");
            }
            GeneChromosomalLocation gcl = new GeneChromosomalLocation();
            ChromosomalLocation cl = new ChromosomalLocation();
            cl.setChromosome(fields[0]);
            cl.setStartPosition(Integer.valueOf(fields[1]));
            cl.setEndPosition(Integer.valueOf(fields[2]));
            gcl.setLocation(cl);
            gcl.setGeneSymbol(fields[3]);
            gcl.setGeneLocationConfiguration(geneLocationConfiguration);
            geneLocationConfiguration.getGeneLocations().add(gcl);
        }
    }

}
