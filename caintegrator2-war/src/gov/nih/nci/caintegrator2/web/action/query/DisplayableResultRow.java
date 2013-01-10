/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a row in a result set.
 */
public class DisplayableResultRow {

    private final List<DisplayableResultValue> values = new ArrayList<DisplayableResultValue>();
    private final ResultRow resultRow;
    private boolean checkedRow = true;
    private boolean selectedSubject = true;

    DisplayableResultRow(ResultRow resultRow, Map<String, Integer> columnLocations) {
        this.resultRow = resultRow;
        loadValues(columnLocations);
    }

    private void loadValues(Map<String, Integer> columnLocations) {
        for (int i = 0; i < columnLocations.size(); i++) {
            values.add(new DisplayableResultValue());
        }
        for (ResultValue value : resultRow.getValueCollection()) {
            DisplayableResultValue valueWrapper = new DisplayableResultValue(value);
            values.set(columnLocations.get(value.getColumn().getAnnotationFieldDescriptor().
                        getDefinition().getDisplayName()), valueWrapper);
        }
    }

    /**
     * @return the values
     */
    public List<DisplayableResultValue> getValues() {
        return values;
    }

    /**
     * Get the subject assignment for the current row.
     *
     * @return the assignment.
     */
    public StudySubjectAssignment getSubjectAssignment() {
        return resultRow.getSubjectAssignment();
    }

    /**
     * @return the ImageSeries
     */
    public ImageSeries getImageSeries() {
        return resultRow.getImageSeries();
    }

    /**
     * @return the SampleAcquisition
     */
    public SampleAcquisition getSampleAcquisition() {
        return resultRow.getSampleAcquisition();
    }

    /**
     * @return the link to the image series in NCIA.
     */
    public String getNciaLink() {
        if (getImageSeries() != null) {
            String webUrl = getImageSeries().getImageStudy().getImageDataSource().getServerProfile().getWebUrl();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(webUrl);
            stringBuffer.append("/referencedImages.jsf?source=ISPY&image1TrialId=");
            stringBuffer.append(getImageSeries().getImageStudy().getNciaTrialIdentifier());
            stringBuffer.append("&image1PatientId=");
            stringBuffer.append(getImageSeries().getImageStudy().getAssignment().getIdentifier());
            stringBuffer.append("&image1StudyInstanceUid=");
            stringBuffer.append(getImageSeries().getImageStudy().getIdentifier());
            stringBuffer.append("&image1SeriesInstanceUid=");
            stringBuffer.append(getImageSeries().getIdentifier());
            stringBuffer.append("&image1ImageSopInstanceUid=");
            stringBuffer.append(getImageSeries().getImageCollection().iterator().next().getIdentifier());
            return stringBuffer.toString();
        } else {
            return "";
        }
    }

    /**
     * Currently this function is called to see if a checkbox for this row is necessary, and
     * the only reason it would be necessary is if the row is associated with an Image Series
     * or Image Study.
     * @return T/F value.
     */
    public boolean isImagingRow() {
        if (resultRow.getImageSeries() != null
            || !resultRow.getSubjectAssignment().getImageStudyCollection().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @return the checkedRow
     */
    public boolean isCheckedRow() {
        return checkedRow;
    }

    /**
     * @param checkedRow the checkedRow to set
     */
    public void setCheckedRow(boolean checkedRow) {
        this.checkedRow = checkedRow;
    }

    /**
     * @return the selectedSubject
     */
    public boolean isSelectedSubject() {
        return selectedSubject;
    }

    /**
     * @param selectedSubject the selectedSubject to set
     */
    public void setSelectedSubject(boolean selectedSubject) {
        this.selectedSubject = selectedSubject;
    }
}
