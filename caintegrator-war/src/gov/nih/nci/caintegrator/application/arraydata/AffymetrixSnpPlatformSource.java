/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import java.io.File;
import java.util.List;

/**
 * Used to load Affymetrix SNP array designs.
 */
public class AffymetrixSnpPlatformSource extends AbstractPlatformSource {

    private static final long serialVersionUID = 1L;
    private final String platformName;

    /**
     * Creates a new instance.
     * 
     * @param annotationFiles the list of CSV annotation files.
     * @param platformName the platform name.
     */
    public AffymetrixSnpPlatformSource(List<File> annotationFiles, String platformName) {
        super(annotationFiles);
        this.platformName = platformName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPlatformLoader getLoader() throws PlatformLoadingException {
        return new AffymetrixSnpPlatformLoader(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Affymetrix SNP CSV annotation files: " + getAnnotationFileNames();
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
        return PlatformTypeEnum.AFFYMETRIX_SNP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformChannelTypeEnum getPlatformChannelType() {
        return PlatformChannelTypeEnum.ONE_COLOR;
    }

}
