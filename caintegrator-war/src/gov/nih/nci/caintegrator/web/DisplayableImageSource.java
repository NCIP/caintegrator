/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web;

import gov.nih.nci.caintegrator.application.study.ImageDataSourceConfiguration;

/**
 * Used to display Image Source information on the Study Summary page.
 */
public class DisplayableImageSource {
    private final ImageDataSourceConfiguration imageDataSourceConfiguration;
    private int numberImageStudies;
    private int numberImageSeries;
    private int numberImages;


    /**
     * Constructor which wraps the ImageDataSourceConfiguration.
     * @param imageDataSourceConfiguration - Image data source.
     */
    public DisplayableImageSource(ImageDataSourceConfiguration imageDataSourceConfiguration) {
        if (imageDataSourceConfiguration == null) {
            throw new IllegalStateException("ImageDataSourceConfiguration cannot be null.");
        }
        this.imageDataSourceConfiguration = imageDataSourceConfiguration;
    }
    

    /**
     * @return the hostname
     */
    public String getHostName() {
        return imageDataSourceConfiguration.getServerProfile().getHostname();
    }
    
    /**
     * @return the collectionName.
     */
    public String getCollectionName() {
        return imageDataSourceConfiguration.getCollectionName();
    }


    /**
     * @return the numberImages
     */
    public int getNumberImages() {
        return numberImages;
    }


    /**
     * @param numberImages the numberImages to set
     */
    public void setNumberImages(int numberImages) {
        this.numberImages = numberImages;
    }


    /**
     * @return the imageDataSourceConfiguration
     */
    public ImageDataSourceConfiguration getImageDataSourceConfiguration() {
        return imageDataSourceConfiguration;
    }


    /**
     * @return the numberImageStudies
     */
    public int getNumberImageStudies() {
        return numberImageStudies;
    }


    /**
     * @param numberImageStudies the numberImageStudies to set
     */
    public void setNumberImageStudies(int numberImageStudies) {
        this.numberImageStudies = numberImageStudies;
    }


    /**
     * @return the numberImageSeries
     */
    public int getNumberImageSeries() {
        return numberImageSeries;
    }


    /**
     * @param numberImageSeries the numberImageSeries to set
     */
    public void setNumberImageSeries(int numberImageSeries) {
        this.numberImageSeries = numberImageSeries;
    }
    
    /**
     * 
     * @return last modified date.
     */
    public String getDisplayableLastModifiedDate() {
        return imageDataSourceConfiguration.getDisplayableLastModifiedDate();
    }

}
