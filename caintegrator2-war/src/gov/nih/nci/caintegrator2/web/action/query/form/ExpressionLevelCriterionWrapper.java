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

import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.RangeTypeEnum;

import org.apache.commons.lang3.math.NumberUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a single <code>ExpressionLevelCriterion</code>.
 */
class ExpressionLevelCriterionWrapper extends AbstractGenomicCriterionWrapper {
    private static final int NUMBER_OF_MAX_PARAMETERS = 4;
    private static final int NUMBER_OF_MAX_PARAMETERS_MULTIPLE_PLATFORMS = NUMBER_OF_MAX_PARAMETERS + 1;
    private static final Float DEFAULT_VALUE = 100f;
    private static final String RANGE_TYPE_LABEL = "Range Type";
    static final String EXPRESSION_LEVEL = "Expression Level";

    private final ExpressionLevelCriterion criterion;

    ExpressionLevelCriterionWrapper(GeneExpressionCriterionRow row) {
        this(new ExpressionLevelCriterion(), row);
    }

    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")  // bogus error
    ExpressionLevelCriterionWrapper(ExpressionLevelCriterion criterion, GeneExpressionCriterionRow row) {
        super(row);
        this.criterion = criterion;
        if (criterion.getRangeType() == null) {
            criterion.setRangeType(RangeTypeEnum.GREATER_OR_EQUAL);
            setCriterionDefaults();
        }
        setupDefaultGenomicParameters(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        getParameters().add(createRangeTypeParameter());
        addRangeParameters();
    }

    private void setUpRangeParameters() {
        setCriterionDefaults();
        removeExistingRangeParameters();
        addRangeParameters();
    }

    private void addRangeParameters() {
        switch (criterion.getRangeType()) {
            case GREATER_OR_EQUAL:
                getParameters().add(createGreaterOrEqualParameter());
                break;
            case LESS_OR_EQUAL:
                getParameters().add(createLessOrEqualParameter());
                break;
            case INSIDE_RANGE:
            case OUTSIDE_RANGE:
                getParameters().add(createGreaterOrEqualParameter());
                getParameters().add(createLessOrEqualParameter());
                break;
            default:
                break;
        }
    }

    private void removeExistingRangeParameters() {
        if (getParameters().size() == getNumberMaxParameters()) {
            getParameters().remove(getNumberMaxParameters() - 1);
        }
        if (getParameters().size() == getNumberMaxParameters() - 1) {
            getParameters().remove(getNumberMaxParameters() - 2);
        }
    }

    private int getNumberMaxParameters() {
        return isStudyHasMultipleGeneExpressionPlatforms()
                ? NUMBER_OF_MAX_PARAMETERS_MULTIPLE_PLATFORMS : NUMBER_OF_MAX_PARAMETERS;
    }


    private void setCriterionDefaults() {
        switch (criterion.getRangeType()) {
            case GREATER_OR_EQUAL:
                criterion.setLowerLimit(DEFAULT_VALUE);
                break;
            case LESS_OR_EQUAL:
                criterion.setUpperLimit(DEFAULT_VALUE);
                break;
            case INSIDE_RANGE:
                criterion.setLowerLimit(DEFAULT_VALUE);
                criterion.setUpperLimit(DEFAULT_VALUE);
                break;
            case OUTSIDE_RANGE:
                criterion.setLowerLimit(DEFAULT_VALUE);
                criterion.setUpperLimit(DEFAULT_VALUE);
                break;
            default:
                break;
        }
    }

    private SelectListParameter<RangeTypeEnum> createRangeTypeParameter() {
        OptionList<RangeTypeEnum> options = new OptionList<RangeTypeEnum>();
        options.addOption(RangeTypeEnum.GREATER_OR_EQUAL.getValue(), RangeTypeEnum.GREATER_OR_EQUAL);
        options.addOption(RangeTypeEnum.LESS_OR_EQUAL.getValue(), RangeTypeEnum.LESS_OR_EQUAL);
        options.addOption(RangeTypeEnum.OUTSIDE_RANGE.getValue(), RangeTypeEnum.OUTSIDE_RANGE);
        options.addOption(RangeTypeEnum.INSIDE_RANGE.getValue(), RangeTypeEnum.INSIDE_RANGE);
        ValueSelectedHandler<RangeTypeEnum> handler = new ValueSelectedHandler<RangeTypeEnum>() {
            @Override
            public void valueSelected(RangeTypeEnum value) {
                criterion.setRangeType(value);
                setUpRangeParameters();
            }
        };
        SelectListParameter<RangeTypeEnum> rangeTypeParameter =
            new SelectListParameter<RangeTypeEnum>(getParameters().size(), getRow().getRowIndex(),
                                                        options, handler, criterion.getRangeType());
        rangeTypeParameter.setLabel(RANGE_TYPE_LABEL);
        rangeTypeParameter.setUpdateFormOnChange(true);
        return rangeTypeParameter;
    }

    private TextFieldParameter createLessOrEqualParameter() {
        final String label =
            RangeTypeEnum.INSIDE_RANGE.equals(criterion.getRangeType())
                || RangeTypeEnum.OUTSIDE_RANGE.equals(criterion.getRangeType())
                ? "And" : "Expression level <=";
        TextFieldParameter rangeParameter = new TextFieldParameter(getParameters().size(), getRow().getRowIndex(),
                                                                   criterion.getUpperLimit().toString());
        rangeParameter.setLabel(label);
        ValueHandler expressionHandler = new ValueHandlerAdapter() {

            @Override
            public boolean isValid(String value) {
                return NumberUtils.isNumber(value);
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            @Override
            public void valueChanged(String value) {
                criterion.setUpperLimit(Float.valueOf(value));
            }
        };
        rangeParameter.setValueHandler(expressionHandler);
        return rangeParameter;
    }

    private TextFieldParameter createGreaterOrEqualParameter() {
        String possibleLabel = "Expression level >=";
        if (RangeTypeEnum.INSIDE_RANGE.equals(criterion.getRangeType())) {
            possibleLabel = "Expression level between";
        } else if (RangeTypeEnum.OUTSIDE_RANGE.equals(criterion.getRangeType())) {
            possibleLabel = "Expression level outside of";
        }
        final String label = possibleLabel;
        TextFieldParameter rangeParameter = new TextFieldParameter(getParameters().size(), getRow().getRowIndex(),
                                                                   criterion.getLowerLimit().toString());
        rangeParameter.setLabel(label);
        ValueHandler expressionHandler = new ValueHandlerAdapter() {

            @Override
            public boolean isValid(String value) {
                return NumberUtils.isNumber(value);
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            @Override
            public void valueChanged(String value) {
                criterion.setLowerLimit(Float.valueOf(value));
            }
        };
        rangeParameter.setValueHandler(expressionHandler);
        return rangeParameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractGenomicCriterion getAbstractGenomicCriterion() {
        return criterion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String getFieldName() {
        return EXPRESSION_LEVEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.EXPRESSION_LEVEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean platformParameterUpdateOnChange() {
        return false;
    }

    @Override
    protected void updateControlParameters() {
        // no-op, no control parameters for expression level criterion.

    }

}
