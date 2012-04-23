/**
 * Copyright (c) 2012, 5AM Solutions, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of the author nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public abstract class AbstractMockitoTest {
    protected CaArrayFacade caArrayFacade;

    /**
     * Sets up mocks.
     * @throws Exception on error
     */
    @Before
    public void setUpMocks() throws Exception {
        setUpCaArrayFacade();
    }

    /**
     * Sets up the caArray facade mock objects.
     */
    protected void setUpCaArrayFacade() throws Exception {
        caArrayFacade = mock(CaArrayFacade.class);
        when(caArrayFacade.getSamples(anyString(), any(ServerConnectionProfile.class))).thenReturn(Arrays.asList(new Sample()));
        when(caArrayFacade.retrieveFile(any(GenomicDataSourceConfiguration.class), anyString())).thenReturn(ArrayUtils.EMPTY_BYTE_ARRAY);
        when(caArrayFacade.retrieveFilesForGenomicSource(any(GenomicDataSourceConfiguration.class))).thenReturn(Collections.<File>emptyList());
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class))).thenReturn(new Date());

        Map<String, Date> updateMap = new HashMap<String, Date>();
        updateMap.put("test1", new Date());
        updateMap.put("test2", new Date());
        updateMap.put("test3", new Date());
        when(caArrayFacade.checkForSampleUpdates(anyString(), any(ServerConnectionProfile.class))).thenReturn(updateMap);
        setUpCaArrayFacadeRetrieveData();
        setUpCaArrayFacadeRetrieveDnaAnalysisData();
    }

    private void setUpCaArrayFacadeRetrieveData() throws Exception {
        when(caArrayFacade.retrieveData(any(GenomicDataSourceConfiguration.class))).thenAnswer(new Answer<ArrayDataValues>() {
            @Override
            public ArrayDataValues answer(InvocationOnMock invocation) throws Throwable {
                GenomicDataSourceConfiguration dataSource = (GenomicDataSourceConfiguration) invocation.getArguments()[0];
                List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
                GeneExpressionReporter reporter = new GeneExpressionReporter();
                reporters.add(reporter);
                ArrayDataValues values = new ArrayDataValues(reporters);
                Platform platform = new Platform();
                ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
                reporter.setReporterList(reporterList);
                reporterList.getReporters().addAll(reporters);
                platform.addReporterList("reporterList2", ReporterTypeEnum.GENE_EXPRESSION_GENE);
                for (Sample sample : dataSource.getSamples()) {
                    addExpressionArrayData(sample, platform, reporterList, values);
                }
                return values;
            }
        });
    }

    private void setUpCaArrayFacadeRetrieveDnaAnalysisData() throws Exception {
        when(caArrayFacade.retrieveDnaAnalysisData(any(GenomicDataSourceConfiguration.class), any(ArrayDataService.class)))
        .thenAnswer(new Answer<List<ArrayDataValues>>() {
            @Override
            public List<ArrayDataValues> answer(InvocationOnMock invocation) throws Throwable {
                GenomicDataSourceConfiguration dataSource = (GenomicDataSourceConfiguration) invocation.getArguments()[0];
                List<ArrayDataValues> valuesList = new ArrayList<ArrayDataValues>();
                List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
                GeneExpressionReporter reporter = new GeneExpressionReporter();
                reporters.add(reporter);
                ArrayDataValues values = new ArrayDataValues(reporters);
                Platform platform = new Platform();
                ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
                reporter.setReporterList(reporterList);
                reporterList.getReporters().addAll(reporters);
                for (Sample sample : dataSource.getSamples()) {
                    addDnaAnalysisArrayData(sample, platform, reporterList, values);
                }
                valuesList.add(values);
                return valuesList;
            }
        });
    }

    private void addExpressionArrayData(Sample sample, Platform platform, ReporterList reporterList, ArrayDataValues values) {
        Array array = new Array();
        array.setPlatform(platform);
        array.setName(sample.getName());
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.getReporterLists().add(reporterList);
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        values.setFloatValues(arrayData, reporterList.getReporters(), ArrayDataValueType.EXPRESSION_SIGNAL,
                new float[reporterList.getReporters().size()]);
    }

    private void addDnaAnalysisArrayData(Sample sample, Platform platform, ReporterList reporterList, ArrayDataValues values) {
        Array array = new Array();
        array.setPlatform(platform);
        array.setName(sample.getName());
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.COPY_NUMBER);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.getReporterLists().add(reporterList);
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        values.setFloatValues(arrayData, reporterList.getReporters(), ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO,
                new float[reporterList.getReporters().size()]);
    }


}
