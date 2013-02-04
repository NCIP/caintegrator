/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.analysis;

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Parse the GISTIC result zip file.
 */
public class GisticResultZipFileParser {

    private static final int BYTE_SIZE = 1024;
    private  static byte[] buf = new byte[BYTE_SIZE];

    private final Map<String, List<Gene>> ampGeneMap = new HashMap<String, List<Gene>>();
    private final Map<String, List<Gene>> delGeneMap = new HashMap<String, List<Gene>>();
    private Map<String, Map<GisticGenomicRegionReporter, Float>> gisticData;

    private final ReporterList reporterList;
    private final CaIntegrator2Dao dao;

    /**
     * @param dao the caintegrator2 dao
     * @param reporterList the reporter list
     */
    public GisticResultZipFileParser(ReporterList reporterList, CaIntegrator2Dao dao) {
        super();
        this.reporterList = reporterList;
        this.dao = dao;
    }

    /**
     * 
     * @param zipFile the Gistic result zip file
     * @return the gistic data
     * @throws DataRetrievalException data retrieval exception
     */
    public Map<String, Map<GisticGenomicRegionReporter, Float>> parse(File zipFile) throws DataRetrievalException {
        try {
            ampGeneMap.clear();
            delGeneMap.clear();
            mapGenes(zipFile);
            processResults(zipFile);
            return gisticData;
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't parse allLesion file: " + e.getMessage(), e);
        }
    }

    private void processResults(File zipFile) throws IOException, DataRetrievalException {
        ZipInputStream zipInputStream = null;
        ZipEntry zipEntry;
        zipInputStream = new ZipInputStream(
            new FileInputStream(zipFile));

        zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null)  {
            String entryName = zipEntry.getName();
            if (entryName.contains("all_lesions")) {
                GisticAllLesionsFileParser parser = new GisticAllLesionsFileParser(
                        ampGeneMap, delGeneMap, reporterList);
                gisticData = parser.parse(unZipFile(zipInputStream));
                break;
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    private void mapGenes(File zipFile) throws IOException {
        ZipInputStream zipInputStream = null;
        ZipEntry zipEntry;
        zipInputStream = new ZipInputStream(
            new FileInputStream(zipFile));

        zipEntry = zipInputStream.getNextEntry();
        GisticGeneMapFileParser parser = new GisticGeneMapFileParser(dao);
        while (zipEntry != null)  {
            String entryName = zipEntry.getName();
            if (entryName.contains("Amp_genes")) {
                ampGeneMap.putAll(parser.parse(unZipFile(zipInputStream)));
            } else if (entryName.contains("Del_genes")) {
                delGeneMap.putAll(parser.parse(unZipFile(zipInputStream)));
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    private File unZipFile(ZipInputStream zipinputstream) throws IOException {
        int n;
        String outputFileName = System.getProperty("java.io.tmpdir") + "/Gistic_result_file_"
            + System.currentTimeMillis() + ".txt";
        FileOutputStream fileoutputstream = new FileOutputStream(outputFileName);
        while ((n = zipinputstream.read(buf, 0, BYTE_SIZE)) > -1) {
            fileoutputstream.write(buf, 0, n);
        }
        fileoutputstream.close();
        return new File(outputFileName);
    }

}
