/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import java.io.File;

/**
 * Used to load Agilent array designs.
 */
public class AgilentExpressionPlatformSource extends AbstractPlatformSource {

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
    public AgilentExpressionPlatformSource(File annotationFile, String platformName,
            String platformFileName) {
        super(annotationFile);
        this.platformName = platformName;
        this.platformFileName = platformFileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPlatformLoader getLoader() {
        return new AgilentExpressionPlatformLoader(this);
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

}
