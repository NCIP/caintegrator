/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import java.io.File;
import java.util.List;

/**
 * Used to load Affymetrix DNA array designs.
 */
public class AffymetrixDnaPlatformSource extends AbstractPlatformSource {

    private static final long serialVersionUID = 1L;
    private final String platformName;

    /**
     * Creates a new instance.
     * 
     * @param annotationFiles the list of CSV annotation files.
     * @param platformName the platform name.
     */
    public AffymetrixDnaPlatformSource(List<File> annotationFiles, String platformName) {
        super(annotationFiles);
        this.platformName = platformName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPlatformLoader getLoader() throws PlatformLoadingException {
        return new AffymetrixDnaAnalysisPlatformLoader(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Affymetrix DNA CSV annotation files: " + getAnnotationFileNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName() {
        return platformName;
    }


}
