/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.common;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator2.application.analysis.GctDataset;
import gov.nih.nci.caintegrator2.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticSamplesMarkers;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cabig.icr.asbp.parameter.FloatParameter;
import org.cabig.icr.asbp.parameter.IntegerParameter;
import org.cabig.icr.asbp.parameter.Parameter;
import org.cabig.icr.asbp.parameter.ParameterList;
import org.cabig.icr.asbp.parameter.StringParameter;
import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.WebServiceException;


/**
 * This is a static utility class for GenePattern type operations.
 */
public final class GenePatternUtil {
    
    private static final long STATUS_REFRESH_INTERVAL_MILLISECONDS = 10000;

    private GenePatternUtil() { }
    
    /**
     * Creates the object which wraps gistic samples and markers from Cai2 objects.
     * @param queryManagementService to query the database for samples.
     * @param parameters the Gistic parameters.
     * @param studySubscription to run queries against and find samples for.
     * @return Gistic object which wraps the Cai2 samples and markers found.
     * @throws InvalidCriterionException if criterion is invalid and query cannot run.
     */
    public static GisticSamplesMarkers createGisticSamplesMarkers(QueryManagementService queryManagementService, 
            GisticParameters parameters, StudySubscription studySubscription) throws InvalidCriterionException {
        Set<Sample> samples = getSamplesForGistic(studySubscription, queryManagementService, parameters);
        GisticSamplesMarkers gisticSamplesMarkers = new GisticSamplesMarkers();
        convertCai2SamplesToGistic(gisticSamplesMarkers, samples);
        return gisticSamplesMarkers;
    }

    /**
     * Returns the samples that match the given query or all non control samples if no query is selected.
     * 
     * @param studySubscription the current study
     * @param queryManagementService used to run the query
     * @param parameters the Gistic parameters
     * @return the matching samples
     * @throws InvalidCriterionException if the query was invalid.
     */
    public static Set<Sample> getSamplesForGistic(StudySubscription studySubscription, 
            QueryManagementService queryManagementService, GisticParameters parameters)
    throws InvalidCriterionException {
        Set<Sample> samples;
        if (parameters.getClinicalQuery() == null) {
            samples = getControlSamples(studySubscription);
        } else {
            samples = queryManagementService.execute(parameters.getClinicalQuery()).getAllSamples();
        }
        return excludeControlSample(samples, parameters.getExcludeControlSampleSet());
    }

    private static Set<Sample> getControlSamples(StudySubscription studySubscription) {
        Set<Sample> samples = new HashSet<Sample>();
        samples.addAll(studySubscription.getStudy().getSamples());
        return samples;
    }
    
    private static Set<Sample> excludeControlSample(Set<Sample> samples, SampleSet controlSampleSet) {
        if (controlSampleSet != null) {
            samples.removeAll(controlSampleSet.getSamples());
        }
        return samples;
    }

    private static void convertCai2SamplesToGistic(GisticSamplesMarkers gisticSamplesMarkers, Set<Sample> samples) {
        for (Sample sample : samples) {
            addArrayDataToGistic(gisticSamplesMarkers, sample);
        }
    }

    private static void addArrayDataToGistic(GisticSamplesMarkers gisticSamplesMarkers, Sample sample) {
        for (ArrayData arrayData : sample.getArrayDataCollection()) {
            boolean segmentDataAdded = false;
            for (ReporterList reporterList : arrayData.getReporterLists()) {
                if (ReporterTypeEnum.DNA_ANALYSIS_REPORTER.equals(reporterList.getReporterType())) {
                    gisticSamplesMarkers.addReporterListToGisticMarkers(reporterList);
                    if (!segmentDataAdded) {
                        gisticSamplesMarkers.addSegmentDataToGisticSamples(arrayData.getSegmentDatas(), sample);
                    }
                    segmentDataAdded = true;
                }
            }
        }
    }
    
    /**
     * Creates the sample classifications from the clinical queries.
     * @param queryManagementService to query database.
     * @param clinicalQueries to be turned into sample classifications.
     * @param sampleColumnOrdering the sample names which are to be used for this classification.
     * @return sample classification.
     * @throws InvalidCriterionException if criterion is not valid.
     */
    public static SampleClassificationParameterValue createSampleClassification(
            QueryManagementService queryManagementService, List<Query> clinicalQueries,
            List<String> sampleColumnOrdering)
            throws InvalidCriterionException {
        Map<String, String> sampleNameToClassificationMap = new HashMap<String, String>();
        Map<String, Sample> sampleNameToSampleMap = new HashMap<String, Sample>();
        runClinicalQueriesForClassification(queryManagementService, clinicalQueries, sampleNameToClassificationMap,
                sampleNameToSampleMap);
        return retrieveSampleClassifications(
                sampleColumnOrdering, sampleNameToClassificationMap, sampleNameToSampleMap);
    }

    private static void runClinicalQueriesForClassification(QueryManagementService queryManagementService,
            List<Query> clinicalQueries, Map<String, String> sampleNameToClassificationMap,
            Map<String, Sample> sampleNameToSampleMap) throws InvalidCriterionException {
        for (Query query : clinicalQueries) {
            ResultColumn sampleColumn = new ResultColumn();
            sampleColumn.setEntityType(EntityTypeEnum.SAMPLE);
            sampleColumn.setColumnIndex(query.getColumnCollection().size());
            query.getColumnCollection().add(sampleColumn);
            QueryResult result = queryManagementService.execute(query);
            for (ResultRow row : result.getRowCollection()) {
                if (row.getSampleAcquisition() != null) {
                    Sample sample = row.getSampleAcquisition().getSample();
                    if (!sampleNameToClassificationMap.containsKey(sample.getName())) {
                        sampleNameToClassificationMap.put(sample.getName(), query.getName());
                        sampleNameToSampleMap.put(sample.getName(), sample);
                    }
                }
            }
        }
    }

    private static SampleClassificationParameterValue retrieveSampleClassifications(List<String> sampleColumnOrdering,
            Map<String, String> sampleNameToClassificationMap, Map<String, Sample> sampleNameToSampleMap) {
        SampleClassificationParameterValue sampleClassifications = new SampleClassificationParameterValue();
        for (String sampleName : sampleColumnOrdering) {
            sampleClassifications.classify(sampleNameToSampleMap.get(sampleName), 
                    sampleNameToClassificationMap.get(sampleName));
        }
        return sampleClassifications;
    }
    
    /**
     * Creates the GCT dataset given the clinical queries (based on all genomic data).
     * @param studySubscription to run queries against.
     * @param clinicalQueries to be turned into GctDataset.
     * @param excludedControlSampleSet the samples to be excluded.
     * @param queryManagementService to query database.
     * @param platformName if this is not null, specifies the platform to use for the genomic query.
     * @return gct dataset for the queries.
     * @throws InvalidCriterionException if criterion is not valid.
     */
    public static GctDataset createGctDataset(StudySubscription studySubscription, Collection<Query> clinicalQueries, 
            SampleSet excludedControlSampleSet, QueryManagementService queryManagementService, String platformName)
    throws InvalidCriterionException {
        Set<Query> clinicalQuerySet = new HashSet<Query>(clinicalQueries);
        Query allGenomicDataQuery = 
            QueryUtil.createAllGeneExpressionDataQuery(studySubscription, clinicalQuerySet, platformName);
        GenomicDataQueryResult genomicData = queryManagementService.executeGenomicDataQuery(allGenomicDataQuery);
        genomicData.excludeSampleSet(excludedControlSampleSet);
        if (genomicData.getRowCollection().isEmpty()) {
            throw new InvalidCriterionException("Unable to create GCT file: No data found from selection.");
        }
        GctDataset gctDataset = new GctDataset(genomicData);
        gctDataset.getSubjectsNotFoundFromQueries().addAll(genomicData.getQuery().getSubjectIdsNotFound());
        return gctDataset;
    }
    
    /**
     * Creates a parameter from given name/value pair.
     * @param name of parameter.
     * @param value of parameter.
     * @return parameter based on name/value pair.
     */
    public static FloatParameter createParameter(String name, Float value) {
        FloatParameter floatParameter = new FloatParameter();
        floatParameter.setName(name);
        floatParameter.setValue(value);
        return floatParameter;
    }
    
    /**
     * Creates a parameter from given name/value pair.
     * @param name of parameter.
     * @param value of parameter.
     * @return parameter based on name/value pair.
     */
    public static IntegerParameter createParameter(String name, Integer value) {
        IntegerParameter integerParameter = new IntegerParameter();
        integerParameter.setName(name);
        integerParameter.setValue(value);
        return integerParameter;
    }
    
    /**
     * Creates a parameter from given name/value pair.
     * @param name of parameter.
     * @param value of parameter.
     * @return parameter based on name/value pair.
     */
    public static StringParameter createParameter(String name, String value) {
        StringParameter stringParameter = new StringParameter();
        stringParameter.setName(name);
        stringParameter.setValue(value);
        return stringParameter;
    }

    /**
     * Creates a parameter from given name/value pair.
     * @param name of parameter.
     * @param value of parameter.
     * @return parameter based on name/value pair.
     */
    public static StringParameter createBoolean0or1Parameter(String name, boolean value) {
        StringParameter stringParameter = new StringParameter();
        stringParameter.setName(name);
        stringParameter.setValue(value ? "1" : "0");
        return stringParameter;
    }
    
    /**
     * Creates a string of the parameter name/value pairs for the given parameter list.
     * @param parameterList list of parameters.
     * @return String value for the list of parameters.
     */
    public static String parameterListToString(ParameterList parameterList) {
        String nl = "\n";
        StringBuffer sb = new StringBuffer();
        for (Parameter parameter : parameterList.getParameterCollection()) {
            sb.append(parameter.getName()).append(": ").append(retrieveParameterValue(parameter)).append(nl);
        }
        return sb.toString();
    }
    
    private static String retrieveParameterValue(Parameter parameter) {
        if (parameter instanceof StringParameter) {
            return ((StringParameter) parameter).getValue();
        } else if (parameter instanceof FloatParameter) {
            return String.valueOf(((FloatParameter) parameter).getValue());
        } else if (parameter instanceof IntegerParameter) {
            return String.valueOf(((IntegerParameter) parameter).getValue());
        }
        return "";
        
    }

    /**
     * Polls GenePattern until a job is complete.
     * 
     * @param jobInfo the job to wait for
     * @param client the GenePattern client.
     * @return the updated job info.
     * @throws WebServiceException if the status couldn't be updated.
     */
    public static JobInfo waitToComplete(JobInfo jobInfo, CaIntegrator2GPClient client) throws WebServiceException {
        JobInfo updatedInfo = jobInfo;
        try {
            while ("Processing".equals(updatedInfo.getStatus()) || "Pending".equals(updatedInfo.getStatus())) {
                Thread.sleep(STATUS_REFRESH_INTERVAL_MILLISECONDS);
                updatedInfo = client.getStatus(updatedInfo);
            }
            return updatedInfo;
        } catch (InterruptedException e) {
            throw new IllegalStateException("Thread shouldn't be interrupted.");
        }
     }
    
}
