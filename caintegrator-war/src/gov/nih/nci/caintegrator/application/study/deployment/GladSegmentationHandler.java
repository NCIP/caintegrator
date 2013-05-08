/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.WebServiceException;

import au.com.bytecode.opencsv.CSVReader;
import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator.common.GenePatternUtil;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

/**
 * Adds segmentation data by invoking the GLAD GenePattern service.
 */
class GladSegmentationHandler {

    private static final String OUTPUT_FILENAME = "output.glad";

    private static final float DECIMAL_1000 = 1000.0f;

    private final CaIntegrator2GPClient client;

    GladSegmentationHandler(CaIntegrator2GPClient client) {
        this.client = client;
    }

    void addSegmentationData(DnaAnalysisData dnaAnalysisData) throws DataRetrievalException {
        try {
            List<ParameterInfo> parameters = new ArrayList<ParameterInfo>();
            ParameterInfo inputFileParameter = new ParameterInfo();
            inputFileParameter.setName("dna.analysis.input.file");
            File inputFile = createInputFile(dnaAnalysisData);
            inputFileParameter.setValue(inputFile.getAbsolutePath());
            parameters.add(inputFileParameter);
            ParameterInfo outputFilenameParameter = new ParameterInfo();
            outputFilenameParameter.setName("output.filename");
            outputFilenameParameter.setValue(OUTPUT_FILENAME);
            parameters.add(outputFilenameParameter);
            JobInfo jobInfo = client.runAnalysis("GLAD", parameters);
            jobInfo = GenePatternUtil.waitToComplete(jobInfo, client);
            inputFile.delete();
            handleResults(jobInfo, dnaAnalysisData);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't run GLAD job: " + e.getMessage(), e);
        } catch (WebServiceException e) {
            throw new DataRetrievalException("Couldn't run GLAD job: " + e.getMessage(), e);
        }
    }

    private void handleResults(JobInfo jobInfo, DnaAnalysisData dnaAnalysisData) throws IOException,
    WebServiceException, DataRetrievalException {
        File outputFile = getOutputFile(jobInfo);
        addSegmentationData(outputFile, dnaAnalysisData);
        outputFile.delete();
    }

    private void addSegmentationData(File outputFile, DnaAnalysisData dnaAnalysisData) throws IOException {
        FileReader fileReader = new FileReader(outputFile);
        CSVReader csvReader = new CSVReader(fileReader, '\t');
        csvReader.readNext(); // skip header line;
        Map<String, ArrayData> arrayDataMap = getArrayDataMap(dnaAnalysisData);
        String[] fields;
        while ((fields = csvReader.readNext()) != null) {
            ArrayData arrayData = arrayDataMap.get(fields[0]);
            handleSegmentDataLine(fields, arrayData);
        }
        csvReader.close();
        fileReader.close();
    }

    private void handleSegmentDataLine(String[] fields, ArrayData arrayData) {
        String chromosome = fields[1];
        int start = Integer.parseInt(fields[2]);
        int end = Integer.parseInt(fields[3]);
        int numberOfMarkers = Integer.parseInt(fields[4]);
        float segmentValue = Float.parseFloat(fields[5]);
        SegmentData segmentData = new SegmentData();
        segmentData.setLocation(new ChromosomalLocation());
        segmentData.getLocation().setChromosome(chromosome);
        segmentData.getLocation().setStartPosition(start);
        segmentData.getLocation().setEndPosition(end);
        segmentData.setNumberOfMarkers(numberOfMarkers);
        segmentData.setSegmentValue(segmentValue);
        arrayData.getSegmentDatas().add(segmentData);
    }

    private Map<String, ArrayData> getArrayDataMap(DnaAnalysisData dnaAnalysisData) {
        Map<String, ArrayData> arrayDataMap = new HashMap<String, ArrayData>();
        for (ArrayData arrayData : dnaAnalysisData.getArrayDatas()) {
            arrayDataMap.put(String.valueOf(arrayData.getId()), arrayData);
        }
        return arrayDataMap;
    }

    private File getOutputFile(JobInfo jobInfo)
    throws IOException, WebServiceException, DataRetrievalException {
        File outputFile = client.getResultFile(jobInfo, OUTPUT_FILENAME);
        if (outputFile == null) {
            throw new DataRetrievalException("GLAD job did not complete successfully, output was not returned");
        }
        return outputFile;
    }

    private File createInputFile(DnaAnalysisData dnaAnalysisData) throws IOException {
        File inputFile = File.createTempFile("glad_input", ".cn");
        FileWriter writer = new FileWriter(inputFile);
        List<ArrayData> arrayDatas = new ArrayList<ArrayData>();
        arrayDatas.addAll(dnaAnalysisData.getArrayDatas());
        writeHeader(writer, arrayDatas);
        writeData(writer, arrayDatas, dnaAnalysisData);
        writer.flush();
        writer.close();
        return inputFile;
    }

    private void writeData(FileWriter writer, List<ArrayData> arrayDatas, DnaAnalysisData dnaAnalysisData)
            throws IOException {
        for (int i = 0; i < dnaAnalysisData.getReporters().size(); i++) {
            DnaAnalysisReporter reporter = dnaAnalysisData.getReporters().get(i);
            if (reporter.hasValidLocation()) {
                writeDataLine(writer, arrayDatas, dnaAnalysisData, i, reporter);
            }
        }
    }

    private void writeDataLine(FileWriter writer, List<ArrayData> arrayDatas, DnaAnalysisData dnaAnalysisData, int i,
            DnaAnalysisReporter reporter) throws IOException {
        writer.write(reporter.getName() + "\t" + reporter.getChromosomeAsInt() + "\t" + reporter.getPosition());
        for (ArrayData arrayData : arrayDatas) {
            float nonLogTransformedValue = (float) Math.pow(2, dnaAnalysisData.getValues(arrayData)[i]);
            writer.write("\t" + roundToThreeDecimalPlaces(nonLogTransformedValue));
        }
        writer.write("\n");
    }

    private float roundToThreeDecimalPlaces(float value) {
        return Math.round(value * DECIMAL_1000) / DECIMAL_1000;
    }

    private void writeHeader(FileWriter writer, List<ArrayData> arrayDatas) throws IOException {
        writer.write("SNP\tChromosome\tPhysicalPosition");
        for (ArrayData arrayData : arrayDatas) {
            writer.write("\t" + arrayData.getId());
        }
        writer.write("\n");
    }

}
