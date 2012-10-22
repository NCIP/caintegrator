/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and
 * have distributed to and by third parties the caIntegrator2 Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do
 * not include such end-user documentation, You shall include this acknowledgment
 * in the Software itself, wherever such third-party acknowledgments normally
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software.
 * This License does not authorize You to use any trademarks, service marks,
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM,
 * except as required to comply with the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
    private boolean foldChangeGeneSymbol = false;
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
     * @return the formFieldId
     */
    public String getFormFieldId() {
        if (foldChangeGeneSymbol) {
            return "FoldChangeGeneSymbol" + rowIndex + "." + parameterIndex;
        }
        return "queryForm.criteriaGroup.rows[" + rowIndex + "].parameters[" + parameterIndex + "]";
    }

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

    /**
     * @return the foldChangeGeneSymbol
     */
    public boolean isFoldChangeGeneSymbol() {
        return foldChangeGeneSymbol;
    }

    /**
     * @param foldChangeGeneSymbol the foldChangeGeneSymbol to set
     */
    public void setFoldChangeGeneSymbol(boolean foldChangeGeneSymbol) {
        this.foldChangeGeneSymbol = foldChangeGeneSymbol;
    }
}
