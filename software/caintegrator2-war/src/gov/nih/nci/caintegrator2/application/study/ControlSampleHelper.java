/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Reads control sample files and adds samples to the control list for a study.
 */
class ControlSampleHelper {

    private final File controlSampleFile;
    private final GenomicDataSourceConfiguration genomicSource;

    ControlSampleHelper(GenomicDataSourceConfiguration genomicSource, File controlSampleFile) {
        this.genomicSource = genomicSource;
        this.controlSampleFile = controlSampleFile;
    }

    void addControlSamples(String controlSampleSetName, String controlFileName)
            throws ValidationException, IOException {
        if (genomicSource.getStudyConfiguration().getControlSampleSet(controlSampleSetName) != null) {
            throw new ValidationException("Duplicate Control Sample Set Name.");
        }
        SampleSet newControlSampleSet = addControlSampleSet(controlSampleSetName, controlFileName);
        FileReader fileReader = new FileReader(controlSampleFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        String sampleName;
        while ((sampleName = lineNumberReader.readLine()) != null) {
            addControlSample(newControlSampleSet, sampleName.trim(), lineNumberReader.getLineNumber());
        }
        genomicSource.getControlSampleSetCollection().add(newControlSampleSet);
        lineNumberReader.close();
        fileReader.close();
    }
    
    private SampleSet addControlSampleSet(String name, String controlFileName) {
        SampleSet newControlSampleSet = new SampleSet();
        newControlSampleSet.setName(name);
        newControlSampleSet.setFileName(controlFileName);
        return newControlSampleSet;
    }

    private void addControlSample(SampleSet newControlSampleSet, String sampleName,
            int lineNumber) throws ValidationException {
        Sample sample = genomicSource.getSample(sampleName);
        if (sample == null) {
            throw new ValidationException("Invalid sample identifier on line " + lineNumber
                    + ", there is no sample with the identifier " + sampleName + " in the study.");
        } else {
            newControlSampleSet.getSamples().add(sample);
        }
    }

}
