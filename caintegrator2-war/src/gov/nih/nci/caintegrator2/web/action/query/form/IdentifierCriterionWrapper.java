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

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps access to a <code>IdentifierCriterion</code>.
 */
final class IdentifierCriterionWrapper extends AbstractCriterionWrapper implements OperatorHandler {
    
    private static final Map<CriterionOperatorEnum, WildCardTypeEnum> OPERATOR_TO_WILDCARD_MAP = 
        new HashMap<CriterionOperatorEnum, WildCardTypeEnum>();
    private static final Map<WildCardTypeEnum, CriterionOperatorEnum> WILDCARD_TO_OPERATOR_MAP = 
        new HashMap<WildCardTypeEnum, CriterionOperatorEnum>();
    
    static {
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.BEGINS_WITH, WildCardTypeEnum.WILDCARD_AFTER_STRING);
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.CONTAINS, WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING);
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.ENDS_WITH, WildCardTypeEnum.WILDCARD_BEFORE_STRING);
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.EQUALS, WildCardTypeEnum.WILDCARD_OFF);
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.NOT_EQUAL_TO, WildCardTypeEnum.NOT_EQUAL_TO);

        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.WILDCARD_AFTER_STRING, CriterionOperatorEnum.BEGINS_WITH);
        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING, CriterionOperatorEnum.CONTAINS);
        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.WILDCARD_BEFORE_STRING, CriterionOperatorEnum.ENDS_WITH);
        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.WILDCARD_OFF, CriterionOperatorEnum.EQUALS);
        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.NOT_EQUAL_TO, CriterionOperatorEnum.NOT_EQUAL_TO);
    }
    
    private final IdentifierCriterion criterion;
    public static final String SUBJECT_IDENTIFIER_FIELD_NAME = "Unique Subject Identifier";
    public static final String IMAGE_SERIES_IDENTIFIER_FIELD_NAME = "Unique Image Series Identifier";

    IdentifierCriterionWrapper(IdentifierCriterion criterion, IdentifierCriterionRow row) {
        this.criterion = criterion;
        getParameters().add(createValueParameter(row));
    }
    
    private TextFieldParameter createValueParameter(IdentifierCriterionRow row) {
        TextFieldParameter valueParameter = new TextFieldParameter(0, row.getRowIndex(), 
                                                                   criterion.getStringValue());
        valueParameter.setOperatorHandler(this);
        ValueHandler valueHandler = new ValueHandlerAdapter() {
            /**
             * {@inheritDoc}
             */
            public void valueChanged(String value) {
                criterion.setStringValue(value);
            }
        };
        valueParameter.setValueHandler(valueHandler);
        return valueParameter;
    }

    @Override
    String getFieldName() {
        if (EntityTypeEnum.IMAGESERIES.equals(criterion.getEntityType())) {
            return IMAGE_SERIES_IDENTIFIER_FIELD_NAME;
        }
        return SUBJECT_IDENTIFIER_FIELD_NAME;
        
    }
    

    @Override
    AbstractCriterion getCriterion() {
        return criterion;
    }

    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.IDENTIFIER;
    }

    /**
     * {@inheritDoc}
     */
    public CriterionOperatorEnum[] getAvailableOperators() {
        return CriterionOperatorEnum.STRING_OPERATORS;
    }

    /**
     * {@inheritDoc}
     */
    public CriterionOperatorEnum getOperator() {
        if (criterion.getWildCardType() == null) {
            return null;
        } else {
            return WILDCARD_TO_OPERATOR_MAP.get(criterion.getWildCardType());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void operatorChanged(AbstractCriterionParameter parameter, CriterionOperatorEnum operator) {
        if (operator == null) {
            criterion.setWildCardType(null);
        } else {
            criterion.setWildCardType(OPERATOR_TO_WILDCARD_MAP.get(operator));
        }
    }
}
