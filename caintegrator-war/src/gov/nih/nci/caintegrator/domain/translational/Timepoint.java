/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.translational;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeriesAcquisition;

import java.util.Collection;
import java.util.Date;

/**
 *
 */
public class Timepoint extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;

    private String description;
    private Date endDate;
    private String name;
    private Date startDate;
    private Collection<SampleAcquisition> sampleAcquisitionCollection;
    private Collection<ImageSeriesAcquisition> imageSeriesAcquisitionCollection;

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the sampleAcquisitionCollection
     */
    public Collection<SampleAcquisition> getSampleAcquisitionCollection() {
        return sampleAcquisitionCollection;
    }

    /**
     * @param sampleAcquisitionCollection
     *            the sampleAcquisitionCollection to set
     */
    public void setSampleAcquisitionCollection(Collection<SampleAcquisition> sampleAcquisitionCollection) {
        this.sampleAcquisitionCollection = sampleAcquisitionCollection;
    }

    /**
     * @return the imageSeriesAcquisitionCollection
     */
    public Collection<ImageSeriesAcquisition> getImageSeriesAcquisitionCollection() {
        return imageSeriesAcquisitionCollection;
    }

    /**
     * @param imageSeriesAcquisitionCollection
     *            the imageSeriesAcquisitionCollection to set
     */
    public void setImageSeriesAcquisitionCollection(
            Collection<ImageSeriesAcquisition> imageSeriesAcquisitionCollection) {
        this.imageSeriesAcquisitionCollection = imageSeriesAcquisitionCollection;
    }

}
