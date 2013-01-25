/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;
import edu.mit.broad.genepattern.gp.services.FileWrapper;
import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import edu.mit.broad.genepattern.gp.services.GenePatternServiceException;
import edu.mit.broad.genepattern.gp.services.JobInfo;
import edu.mit.broad.genepattern.gp.services.ParameterInfo;
import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.CopyNumberData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

/**
 * Adds segmentation data by invoking the GLAD GenePattern service.
 */
class GladSegmentationHandler {

    private static final String OUTPUT_FILENAME = "output.glad";

    private static final float DECIMAL_1000 = 1000.0f;

    private final GenePatternClient client;

    GladSegmentationHandler(GenePatternClient client) {
        this.client = client;
    }

    void addSegmentationData(CopyNumberData copyNumberData) throws DataRetrievalException {
        try {
            List<ParameterInfo> parameters = new ArrayList<ParameterInfo>();
            ParameterInfo inputFileParameter = new ParameterInfo();
            inputFileParameter.setName("copy.number.input.file");
            File inputFile = createInputFile(copyNumberData);
            inputFileParameter.setValue(inputFile.getAbsolutePath());
            parameters.add(inputFileParameter);
            ParameterInfo outputFilenameParameter = new ParameterInfo();
            outputFilenameParameter.setName("output.filename");
            outputFilenameParameter.setValue(OUTPUT_FILENAME);
            parameters.add(outputFilenameParameter);
            JobInfo jobInfo = client.runAnalysis("GLAD", parameters);
            jobInfo = GenePatternUtil.waitToComplete(jobInfo, client);
            inputFile.delete();
            handleResults(jobInfo, copyNumberData);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't run GLAD job: " + e.getMessage(), e);
        } catch (GenePatternServiceException e) {
            throw new DataRetrievalException("Couldn't run GLAD job: " + e.getMessage(), e);
        }
    }

    private void handleResults(JobInfo jobInfo, CopyNumberData copyNumberData) throws IOException,
            GenePatternServiceException, DataRetrievalException {
        File outputFile = getOutputFile(jobInfo);
        addSegmentationData(outputFile, copyNumberData);
        outputFile.delete();
    }

    private void addSegmentationData(File outputFile, CopyNumberData copyNumberData) throws IOException {
        FileReader fileReader = new FileReader(outputFile);
        CSVReader csvReader = new CSVReader(fileReader, '\t');
        csvReader.readNext(); // skip header line;
        Map<String, ArrayData> arrayDataMap = getArrayDataMap(copyNumberData);
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

    private Map<String, ArrayData> getArrayDataMap(CopyNumberData copyNumberData) {
        Map<String, ArrayData> arrayDataMap = new HashMap<String, ArrayData>();
        for (ArrayData arrayData : copyNumberData.getArrayDatas()) {
            arrayDataMap.put(String.valueOf(arrayData.getId()), arrayData);
        }
        return arrayDataMap;
    }

    private File getOutputFile(JobInfo jobInfo) 
    throws IOException, GenePatternServiceException, DataRetrievalException {
        FileWrapper outputFileWrapper = client.getResultFile(jobInfo, OUTPUT_FILENAME);
        if (outputFileWrapper == null) {
            throw new DataRetrievalException("GLAD job did not complete successfully, output was not returned");
        }
        File outputFile = File.createTempFile("output", ".glad");
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        IOUtils.copy(outputFileWrapper.getDataHandler().getInputStream(), fileOutputStream);
        fileOutputStream.close();
        return outputFile;
    }

    private File createInputFile(CopyNumberData copyNumberData) throws IOException {
        File inputFile = File.createTempFile("glad_input", ".cn");
        FileWriter writer = new FileWriter(inputFile);
        List<ArrayData> arrayDatas = new ArrayList<ArrayData>();
        arrayDatas.addAll(copyNumberData.getArrayDatas());
        writeHeader(writer, arrayDatas);
        writeData(writer, arrayDatas, copyNumberData);
        writer.flush();
        writer.close();
        return inputFile;
    }

    private void writeData(FileWriter writer, List<ArrayData> arrayDatas, CopyNumberData copyNumberData)
            throws IOException {
        for (int i = 0; i < copyNumberData.getReporters().size(); i++) {
            DnaAnalysisReporter reporter = copyNumberData.getReporters().get(i);
            if (reporter.hasValidLocation()) {
                writeDataLine(writer, arrayDatas, copyNumberData, i, reporter);
            }
        }
    }

    private void writeDataLine(FileWriter writer, List<ArrayData> arrayDatas, CopyNumberData copyNumberData, int i,
            DnaAnalysisReporter reporter) throws IOException {
        writer.write(reporter.getName() + "\t" + reporter.getChromosomeAsInt() + "\t" + reporter.getPosition());
        for (ArrayData arrayData : arrayDatas) {
            float nonLogTransformedValue = (float) Math.pow(2, copyNumberData.getValues(arrayData)[i]);
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
