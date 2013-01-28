/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import java.io.File;

/**
 * Used to load Agilent Copy Number array designs.
 */
public class AgilentCnPlatformSource extends AbstractPlatformSource {

    private static final long serialVersionUID = 1L;
    private final String platformName;
    private final String platformFileName;
    
    /**
     * Creates a new instance.
     * 
     * @param annotationFile the CSV annotation file.
     * @param platformName the platform name.
     * @param platformFileName the platform file name.
     */
    public AgilentCnPlatformSource(File annotationFile, String platformName,
            String platformFileName) {
        super(annotationFile);
        this.platformName = platformName;
        this.platformFileName = platformFileName;
    }

    private boolean isXmlFile() {
        return platformFileName.endsWith(".xml");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPlatformLoader getLoader() {
        return isXmlFile() ? new AgilentGemlCghPlatformLoader(this)
            : new AgilentTcgaAdfCghPlatformLoader(this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Agilent CSV annotation file: " + getAnnotationFileNames();
    }

    /**
     * @return the platformName
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * @return the platformFileName
     */
    public String getPlatformFileName() {
        return platformFileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformChannelTypeEnum getPlatformChannelType() {
        return PlatformChannelTypeEnum.TWO_COLOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformTypeEnum getPlatformType() {
        return PlatformTypeEnum.AGILENT_COPY_NUMBER;
    }

}
