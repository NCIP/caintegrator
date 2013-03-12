/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.domain;

/**
 * <p>
 * Java class for dbOrthoParams complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="dbOrthoParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="input" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="output" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="inputTaxon" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="outputTaxon" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="inputValues" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class DbOrthoParams {

    private String input;
    private String output;
    private String inputTaxon;
    private String outputTaxon;
    private String inputValues;

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
     * @return the inputTaxon
     */
    public String getInputTaxon() {
        return inputTaxon;
    }

    /**
     * @param inputTaxon the inputTaxon to set
     */
    public void setInputTaxon(String inputTaxon) {
        this.inputTaxon = inputTaxon;
    }

    /**
     * @return the outputTaxon
     */
    public String getOutputTaxon() {
        return outputTaxon;
    }

    /**
     * @param outputTaxon the outputTaxon to set
     */
    public void setOutputTaxon(String outputTaxon) {
        this.outputTaxon = outputTaxon;
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
