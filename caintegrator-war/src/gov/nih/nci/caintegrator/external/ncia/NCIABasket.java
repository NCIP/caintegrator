/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;


import java.util.HashSet;
import java.util.Set;

/**
 * caIntegrator2's version of an NCIA Basket.
 */
public class NCIABasket implements NCIAImageAggregator {
    private final Set <String> imageSeriesIDs = new HashSet<String>();
    private final Set <String> imageStudyIDs = new HashSet<String>();
    private final Set <String> imagePatientIDs = new HashSet<String>();
    private NCIAImageAggregationTypeEnum imageAggregationType;
    
    /**
     * @return the imageSeriesIDs
     */
    public Set<String> getImageSeriesIDs() {
        return imageSeriesIDs;
    }
    
    /**
     * @return the imageStudyIDs
     */
    public Set<String> getImageStudyIDs() {
        return imageStudyIDs;
    }
    
    /**
     * @return the imagePatientIDs
     */
    public Set<String> getImagePatientIDs() {
        return imagePatientIDs;
    }
    
    /**
     * @return the imageAggregationType
     */
    public NCIAImageAggregationTypeEnum getImageAggregationType() {
        return imageAggregationType;
    }
    /**
     * @param imageAggregationType the imageAggregationType to set
     */
    public void setImageAggregationType(NCIAImageAggregationTypeEnum imageAggregationType) {
        this.imageAggregationType = imageAggregationType;
    }
    
}
