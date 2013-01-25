/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * A single operand for a single criterion.
 */
public abstract class AbstractCriterionParameter {
    
    /**
     * Display the operand as a free text field.
     */
    public static final String TEXT_FIELD = "text";
    
    /**
     * Display the operand as a select list (single option).
     */
    public static final String SELECT_LIST = "select";
    
    /**
     * Display the operand as a select list allowing for multi-select.
     */
    public static final String MULTI_SELECT = "multiselect";
    
    private String label = "";
    private String title = "";
    private boolean geneSymbol = false;
    private List<String> availableOperators = new ArrayList<String>();
    private OperatorHandler operatorHandler;
    private final int parameterIndex;
    private int rowIndex;

    private boolean operatorChanged;

    private String newOperator;

    AbstractCriterionParameter(int parameterIndex, int rowIndex) {
        super();
        this.parameterIndex = parameterIndex;
        this.rowIndex = rowIndex;
    }

    void setOperatorHandler(OperatorHandler operatorHandler) {
        this.operatorHandler = operatorHandler;
        availableOperators = getOperatorNames(operatorHandler.getAvailableOperators());
    }

    private List<String> getOperatorNames(CriterionOperatorEnum[] availableOperatorEnums) {
        List<String> operatorNames = new ArrayList<String>(availableOperatorEnums.length);
        for (CriterionOperatorEnum operatorEnum : availableOperatorEnums) {
            operatorNames.add(operatorEnum.getValue());
        }
        return operatorNames;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Returns a string that is used as a displayable title for the operand.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }    

    /**
     * Returns a string that indicates how to display the operand.
     * 
     * @return the display type indicator string.
     */
    public abstract String getFieldType();


    /**
     * @return the current operator.
     */
    public String getOperator() {
        if (operatorHandler.getOperator() == null) {
            return "";
        } else {
            return operatorHandler.getOperator().getValue();
        }
    }

    /**
     * @param operator the new operator value
     */
    public void setOperator(String operator) {
        if (!StringUtils.equals(getOperator(), operator)) {
            newOperator = operator;
            operatorChanged = true;
        }
    }

    /**
     * @return the availableOperators
     */
    public List<String> getAvailableOperators() {
        return availableOperators;
    }

    abstract void validate(ValidationAware action);

    /**
     * @return the formFieldName
     */
    public String getFormFieldName() {
        return "queryForm.criteriaGroup.rows[" + rowIndex + "].parameters[" + parameterIndex + "]";
    }
    

    void processCriteriaChanges() {
        if (operatorChanged) {
            if (StringUtils.isBlank(newOperator)) {
                operatorHandler.operatorChanged(this, null);
            } else {
                operatorHandler.operatorChanged(this, CriterionOperatorEnum.getByValue(newOperator));
            }
            availableOperators = getOperatorNames(operatorHandler.getAvailableOperators());
            operatorChanged = false;
        }
    }

    /**
     * @return the rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * @param rowIndex the rowIndex to set
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * @return the parameterIndex
     */
    public int getParameterIndex() {
        return parameterIndex;
    }

    /**
     * @return the geneSymbol
     */
    public boolean isGeneSymbol() {
        return geneSymbol;
    }

    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(boolean geneSymbol) {
        this.geneSymbol = geneSymbol;
    }
}
