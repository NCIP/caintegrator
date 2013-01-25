/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Creates a <code>ResultRows</code> from a given entity based on the required
 * set of entities expected in the results.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity" }) // See handleSubjectRow()
class ResultRowFactory {

    private final Set<EntityTypeEnum> entityTypes;

    ResultRowFactory(Set<EntityTypeEnum> entityTypes) {
        this.entityTypes = entityTypes;
    }

    Set<ResultRow> getImageSeriesRows(Collection<ImageSeries> imageSeriesCollection) {
        Set<ResultRow> rows = new HashSet<ResultRow>();
        for (ImageSeries imageSeries : imageSeriesCollection) {
            ResultRow row = new ResultRow();
            StudySubjectAssignment studySubjectAssignment = imageSeries.getImageStudy().getAssignment();
            row.setSubjectAssignment(studySubjectAssignment);
            if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) {
                row.setImageSeries(imageSeries);
            }
            if (entityTypes.contains(EntityTypeEnum.SAMPLE)) {
                Timepoint imageSeriesTimepoint = imageSeries.getImageStudy().getTimepoint();
                addSampleRows(rows, row, studySubjectAssignment, imageSeriesTimepoint);
            } else {
                rows.add(row);
            }
        }
        return rows;
    }

    private void addSampleRows(Set<ResultRow> resultRows, 
                               ResultRow row, 
                               StudySubjectAssignment studySubjectAssignment,
                               Timepoint timepoint) {
        boolean samplesFound = false;
        for (SampleAcquisition sampleAcquisition 
                : studySubjectAssignment.getSampleAcquisitionCollection()) {
            if (timepoint == null 
                || timepoint == sampleAcquisition.getTimepoint()) {
                ResultRow newRow = cloneResultRow(row);
                newRow.setSampleAcquisition(sampleAcquisition);
                resultRows.add(newRow);
                samplesFound = true;
            }
        }
        if (!samplesFound) {
            resultRows.add(row);
        }
    }

    Set<ResultRow> getSampleRows(Collection<SampleAcquisition> sampleAcquisitions) {
        Set<ResultRow> rows = new HashSet<ResultRow>();
        for (SampleAcquisition sampleAcquisition : sampleAcquisitions) {
            ResultRow row = new ResultRow();
            StudySubjectAssignment studySubjectAssignment = sampleAcquisition.getAssignment();
            row.setSubjectAssignment(studySubjectAssignment);
            if (entityTypes.contains(EntityTypeEnum.SAMPLE)) {
                row.setSampleAcquisition(sampleAcquisition);
            }
            if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) {
                Timepoint sampleAcquisitionTimepoint = sampleAcquisition.getTimepoint();
                addImageSeriesRows(rows, row, studySubjectAssignment, sampleAcquisitionTimepoint);
            } else {
                rows.add(row);
            }
        }
        return rows;
    }
    
    @SuppressWarnings({"PMD.CyclomaticComplexity" })
    private void addImageSeriesRows(Set<ResultRow> resultRows, 
                                        ResultRow row, 
                                        StudySubjectAssignment studySubjectAssignment,
                                        Timepoint timepoint) {
        boolean imageSeriesFound = false;
        for (ImageSeriesAcquisition imageSeriesAcquisition : studySubjectAssignment.getImageStudyCollection()) {
            if (timepoint == null 
                || timepoint == imageSeriesAcquisition.getTimepoint()) {
                for (ImageSeries series : imageSeriesAcquisition.getSeriesCollection()) {
                    ResultRow newRow = cloneResultRow(row);
                    newRow.setImageSeries(series);
                    resultRows.add(newRow);
                    imageSeriesFound = true;
                }
            }
        }
        if (!imageSeriesFound) {
            resultRows.add(row);
        }
    }

    @SuppressWarnings({"PMD.CyclomaticComplexity" }) // Lots of type checking and row adding.
    Set<ResultRow> getSubjectRows(Collection<StudySubjectAssignment> studySubjectAssignments) {
        Set<ResultRow> rows = new HashSet<ResultRow>();
        for (StudySubjectAssignment studySubjectAssignment : studySubjectAssignments) {
            ResultRow row = new ResultRow();
            row.setSubjectAssignment(studySubjectAssignment);
            if (entityTypes.contains(EntityTypeEnum.SAMPLE)) { // Sample
                Set <ResultRow> newResultRows = new HashSet<ResultRow>();
                addSampleRows(newResultRows, row, studySubjectAssignment, null);
                if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) { // Sample + Image Series
                    Set <ResultRow> addedSampleRows = new HashSet<ResultRow>();
                    addedSampleRows.addAll(newResultRows);
                    for (ResultRow newRow : addedSampleRows) {
                        addImageSeriesRows(newResultRows, newRow, studySubjectAssignment, 
                                           newRow.getSampleAcquisition().getTimepoint());
                    }
                }
                rows.addAll(newResultRows);
            } else if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) { // Image Series w/o Sample
                addImageSeriesRows(rows, row, studySubjectAssignment, null);
            } else { // No image series or rows, just clinical
                rows.add(row);
            }
        }
        return rows;
    }

    private ResultRow cloneResultRow(ResultRow row) {
        ResultRow newRow = new ResultRow();
        newRow.setImageSeries(row.getImageSeries());
        newRow.setRowIndex(row.getRowIndex());
        newRow.setSampleAcquisition(row.getSampleAcquisition());
        newRow.setSubjectAssignment(row.getSubjectAssignment());
        newRow.setValueCollection(row.getValueCollection());
        return newRow;
    }

}
