/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;


/**
 * Possible nbia versions, currently used to know which attribute to use for the image series.
 */
public enum NBIAVersionEnum {

    /**
     * 4.4.
     */
    V4_4("seriesInstanceUID"),
    
    /**
     * 4.5.
     */
    V4_5("instanceUID");
    
    private String seriesIdAtt;
    
    private NBIAVersionEnum(String seriesIdAtt) {
        this.seriesIdAtt = seriesIdAtt;
    }

    /**
     * @return the seriesIdAtt
     */
    public String getSeriesIdAtt() {
        return seriesIdAtt;
    }

    /**
     * @param seriesIdAtt the seriesIdAtt to set
     */
    public void setSeriesIdAtt(String seriesIdAtt) {
        this.seriesIdAtt = seriesIdAtt;
    }

}
