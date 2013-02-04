/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import java.io.File;
import java.util.List;

/**
 * Used to load Affymetrix Copy Number array designs.
 */
public class AffymetrixCnPlatformSource extends AbstractPlatformSource {

    private static final long serialVersionUID = 1L;
    private final String platformName;

    /**
     * Creates a new instance.
     * 
     * @param annotationFiles the list of CSV annotation files.
     * @param platformName the platform name.
     */
    public AffymetrixCnPlatformSource(List<File> annotationFiles, String platformName) {
        super(annotationFiles);
        this.platformName = platformName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPlatformLoader getLoader() throws PlatformLoadingException {
        return new AffymetrixCnPlatformLoader(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Affymetrix Copy Number CSV annotation files: " + getAnnotationFileNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName() {
        return platformName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformTypeEnum getPlatformType() {
        return PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformChannelTypeEnum getPlatformChannelType() {
        return PlatformChannelTypeEnum.ONE_COLOR;
    }

}
