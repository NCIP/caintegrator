/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.common.HibernateUtil;

/**
 * Base class for actions that require retrieval of persistent <code>GenomicDataSourceConfigurations</code>.
 */
public abstract class AbstractGenomicSourceAction extends AbstractStudyAction {
    
    private GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();

    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if (getGenomicSource().getId() != null) {
            setGenomicSource(getStudyManagementService().getRefreshedEntity(getGenomicSource()));
            HibernateUtil.loadGenomicSource(getGenomicSource());
        }
    }

    /**
     * @return the genomicSource
     */
    public GenomicDataSourceConfiguration getGenomicSource() {
        return genomicSource;
    }

    /**
     * @param genomicSource the genomicSource to set
     */
    public void setGenomicSource(GenomicDataSourceConfiguration genomicSource) {
        this.genomicSource = genomicSource;
    }
    
    /**
     * @return boolean allow single data file
     */
    public boolean isPossibleSingleDataFile() {
        return isAgilent();
    }
    
    /**
     * @return is Agilent
     */
    protected boolean isAgilent() {
        return PlatformVendorEnum.AGILENT.getValue().equals(getGenomicSource().getPlatformVendor());
    }

    /**
     * @return is Affymetric gene expression
     */
    protected boolean isAffyExpression() {
        return PlatformVendorEnum.AFFYMETRIX.getValue().equals(getGenomicSource().getPlatformVendor())
        && GenomicDataSourceDataTypeEnum.EXPRESSION.equals(getGenomicSource().getDataType());
    }

    /**
     * @return is Affymetrix copy number
     */
    protected boolean isAffyCopyNumber() {
        return PlatformVendorEnum.AFFYMETRIX.getValue().equals(getGenomicSource().getPlatformVendor())
        && GenomicDataSourceDataTypeEnum.COPY_NUMBER.equals(getGenomicSource().getDataType());
    }

    /**
     * @return is Agilent gene expression
     */
    protected boolean isAgilentExpression() {
        return PlatformVendorEnum.AGILENT.getValue().equals(getGenomicSource().getPlatformVendor())
        && GenomicDataSourceDataTypeEnum.EXPRESSION.equals(getGenomicSource().getDataType());
    }

    /**
     * @return is Agilent copy number
     */
    protected boolean isAgilentCopyNumber() {
        return PlatformVendorEnum.AGILENT.getValue().equals(getGenomicSource().getPlatformVendor())
        && GenomicDataSourceDataTypeEnum.COPY_NUMBER.equals(getGenomicSource().getDataType());
    }

}
