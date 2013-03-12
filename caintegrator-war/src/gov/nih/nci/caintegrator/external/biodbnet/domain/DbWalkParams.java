/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.domain;

/**
 * <p>
 * Java class for dbWalkParams complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="dbWalkParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="dbPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="taxonId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="inputValues" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *  @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class DbWalkParams {

    private String dbPath;
    private String taxonId;
    private String inputValues;

    /**
     * @return the dbPath
     */
    public String getDbPath() {
        return dbPath;
    }

    /**
     * @param dbPath the dbPath to set
     */
    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * @return the taxonId
     */
    public String getTaxonId() {
        return taxonId;
    }

    /**
     * @param taxonId the taxonId to set
     */
    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    /**
     * @return the inputValues
     */
    public String getInputValues() {
        return inputValues;
    }

    /**
     * @param inputValues the inputValues to set
     */
    public void setInputValues(String inputValues) {
        this.inputValues = inputValues;
    }
}
