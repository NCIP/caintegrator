/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier.dto;

import java.io.Serializable;

/**
 * @author caIntegrator Team
 * This calss represents the data associated with a sample. 
*/

public class KMSampleDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	String sampleID; 
	int survivalLength;
	boolean censor;
    

    public KMSampleDTO(String sampleID,int t, boolean c) {
		setSurvivalLength(t);
		setCensor(c); 
		setSampleID(sampleID);
	}
	
	public KMSampleDTO(int t, boolean c) {
		setSurvivalLength(t);
		setCensor(c); 
	}
	/**
	 * @return Returns the censor.
	 */
	public boolean getCensor() {
		return censor;
	}
	/**
	 * @param censor The censor to set.
	 */
	public void setCensor(boolean censor) {
		this.censor = censor;
	}


	/**
	 * @return Returns the survivalLength.
	 */
	public int getSurvivalLength() {
		return survivalLength;
	}
	/**
	 * @param survivalLength The survivalLength to set.
	 */
	public void setSurvivalLength(int survivalLength) {
		this.survivalLength = survivalLength;
	}

	/**
	 * @return Returns the sampleID.
	 */
	public String getSampleID() {
		return sampleID;
	}

	/**
	 * @param sampleID The sampleID to set.
	 */
	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}
}
