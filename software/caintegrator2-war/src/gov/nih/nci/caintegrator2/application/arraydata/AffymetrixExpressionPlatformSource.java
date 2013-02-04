/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import java.io.File;

/**
 * Used to load Affymetrix array designs.
 */
public class AffymetrixExpressionPlatformSource extends AbstractPlatformSource {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * 
     * @param annotationFile the CSV annotation file.
     */
    public AffymetrixExpressionPlatformSource(File annotationFile) {
        super(annotationFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPlatformLoader getLoader() throws PlatformLoadingException {
        return new AffymetrixExpressionPlatformLoader(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Affymetrix CSV annotation file: " + getAnnotationFileNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName() throws PlatformLoadingException {
        return getLoader().getPlatformName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformTypeEnum getPlatformType() {
        return PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformChannelTypeEnum getPlatformChannelType() {
        return PlatformChannelTypeEnum.ONE_COLOR;
    }

}
