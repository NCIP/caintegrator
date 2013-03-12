/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.domain;

/**
 * <p>
 * Java class for db2dbParams complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="db2dbParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="input" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="taxonId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="inputValues" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="outputs" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *  @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class Db2DbParams {

    private String input;
    private String taxonId;
    private String inputValues;
    private String outputs;

    /**
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(String input) {
        this.input = input;
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

    /**
     * @return the outputs
     */
    public String getOutputs() {
        return outputs;
    }

    /**
     * @param outputs the outputs to set
     */
    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }
}
