/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.domain;

/**
 * <p>
 * Java class for dbFindParams complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="dbFindParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="output" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="taxonId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="inputValues" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class DbFindParams {

    private String output;
    private String taxonId;
    private String inputValues;

    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
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
