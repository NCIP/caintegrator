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

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Holds information for a single clinical criterion.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // see createNewCriterion
public abstract class AbstractAnnotationCriterionRow extends AbstractCriterionRow {
    
    private AbstractCriterionWrapper annotationCriterionWrapper;

    AbstractAnnotationCriterionRow(CriteriaGroup group) {
        super(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName() {
        if (getAnnotationCriterionWrapper() == null) {
            return "";
        } else {
            return getAnnotationCriterionWrapper().getFieldName();
        }
    }

    void handleFieldNameChange(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            setAnnotationCriterionWrapper(null);
        } else if (getAnnotationCriterionWrapper() == null) {
            createAnnotationCriterionWrapper(fieldName);
        } else {
            AnnotationDefinition field = getAnnotationDefinitionList().getDefinition(fieldName);
            CriterionTypeEnum criterionType = getCriterionType(fieldName, field);
            if (criterionType.equals(getAnnotationCriterionWrapper().getCriterionType())) {
                getAnnotationCriterionWrapper().setField(field);
            } else {
                createAnnotationCriterionWrapper(fieldName);
            }
        }
    }

    private CriterionTypeEnum getCriterionType(String fieldName, AnnotationDefinition field) {
        if (getNonAnnotationFieldNames().contains(fieldName)) { 
            return getCriterionTypeForNonAnnotationField(fieldName);
        }
        if (!field.getPermissibleValueCollection().isEmpty()) {
            return CriterionTypeEnum.SELECTED_VALUE;
        } else if (AnnotationTypeEnum.STRING.equals(field.getDataType())) {
            return CriterionTypeEnum.STRING_COMPARISON;
        } else if (AnnotationTypeEnum.NUMERIC.equals(field.getDataType())) {
            return CriterionTypeEnum.NUMERIC_COMPARISON;
        } else if (AnnotationTypeEnum.DATE.equals(field.getDataType())) {
            return CriterionTypeEnum.DATE_COMPARISON;
        } else {
            throw new IllegalArgumentException("Unsupported type " + field.getDataType());
        }
    }

    void createAnnotationCriterionWrapper(String fieldName) {
        AnnotationDefinition field = getAnnotationDefinitionList().getDefinition(fieldName);
        CriterionTypeEnum type = getCriterionType(fieldName, field);
        switch (type) {
        case NUMERIC_COMPARISON:
            setAnnotationCriterionWrapper(createNumericCriterionWrapper(field));
            break;
        case STRING_COMPARISON:
            setAnnotationCriterionWrapper(createStringComparisonCriterionWrapper(field));
            break;
        case DATE_COMPARISON:
            setAnnotationCriterionWrapper(createDateCriterionWrapper(field));
            break;
        case SELECTED_VALUE:
            setAnnotationCriterionWrapper(createSelectedValueCriterionWrapper(field));
            break;
        case IDENTIFIER:
            setAnnotationCriterionWrapper(createIdentifierCriterionWrapper());
            break;
        case SUBJECT_LIST:
            setAnnotationCriterionWrapper(createSubjectListCriterionWrapper(
                    getSubscription()));
            break;
        default:
            throw new IllegalStateException("Unsupported AnnotationType " + type);
        }
    }
    
    private IdentifierCriterionWrapper createIdentifierCriterionWrapper() {
        IdentifierCriterion criterion = new IdentifierCriterion();
        criterion.setEntityType(getEntityType());
        return new IdentifierCriterionWrapper(criterion, this);
    }
    
    private SubjectListCriterionWrapper createSubjectListCriterionWrapper(StudySubscription studySubscription) {
        SubjectListCriterion criterion = new SubjectListCriterion();
        return new SubjectListCriterionWrapper(criterion, studySubscription, this);
    }

    private SelectedValueCriterionWrapper createSelectedValueCriterionWrapper(AnnotationDefinition field) {
        SelectedValueCriterion criterion = new SelectedValueCriterion();
        criterion.setAnnotationDefinition(field);
        criterion.setEntityType(getEntityType());
        criterion.setValueCollection(new HashSet<PermissibleValue>());
        return new SelectedValueCriterionWrapper(criterion, this);
    }

    private StringComparisonCriterionWrapper createStringComparisonCriterionWrapper(AnnotationDefinition field) {
        StringComparisonCriterion criterion = new StringComparisonCriterion();
        criterion.setAnnotationDefinition(field);
        criterion.setEntityType(getEntityType());
        return new StringComparisonCriterionWrapper(criterion, this);
    }

    private NumericComparisonCriterionWrapper createNumericCriterionWrapper(AnnotationDefinition field) {
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setAnnotationDefinition(field);
        criterion.setEntityType(getEntityType());
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.EQUAL);
        return new NumericComparisonCriterionWrapper(criterion, this);
    }

    private DateComparisonCriterionWrapper createDateCriterionWrapper(AnnotationDefinition field) {
        DateComparisonCriterion criterion = new DateComparisonCriterion();
        criterion.setAnnotationDefinition(field);
        criterion.setEntityType(getEntityType());
        criterion.setDateComparisonOperator(DateComparisonOperatorEnum.EQUAL);
        return new DateComparisonCriterionWrapper(criterion, this);
    }

    abstract AnnotationDefinitionList getAnnotationDefinitionList();

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractCriterionWrapper getCriterionWrapper() {
        return getAnnotationCriterionWrapper();
    }

    private AbstractCriterionWrapper getAnnotationCriterionWrapper() {
        return annotationCriterionWrapper;
    }

    private void setAnnotationCriterionWrapper(AbstractCriterionWrapper annotationCriterionWrapper) {
        if (this.annotationCriterionWrapper != null) {
            removeCriterionFromQuery();
        }
        this.annotationCriterionWrapper = annotationCriterionWrapper;
        if (annotationCriterionWrapper != null) {
            addCriterionToQuery();
        }
    }

    /**
     * @return
     */
    abstract EntityTypeEnum getEntityType();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableFieldNames() {
        List<String> availableFieldNames = new ArrayList<String>();
        availableFieldNames.addAll(getNonAnnotationFieldNames());
        availableFieldNames.addAll(getAnnotationDefinitionList().getNames());
        return availableFieldNames;
    }

    @Override
    void setCriterion(AbstractCriterion criterion) {
        this.annotationCriterionWrapper = createCriterionWrapper(criterion);
    };
    
    private AbstractCriterionWrapper createCriterionWrapper(AbstractCriterion criterion) {
        if (criterion instanceof StringComparisonCriterion) {
            StringComparisonCriterion stringComparisonCriterion = (StringComparisonCriterion) criterion;
            return new StringComparisonCriterionWrapper(stringComparisonCriterion, this);
        } else if (criterion instanceof NumericComparisonCriterion) {
            NumericComparisonCriterion numericComparisonCriterion = (NumericComparisonCriterion) criterion;
            return new NumericComparisonCriterionWrapper(numericComparisonCriterion, this);
        } else if (criterion instanceof DateComparisonCriterion) {
            DateComparisonCriterion dateComparisonCriterion = (DateComparisonCriterion) criterion;
            return new DateComparisonCriterionWrapper(dateComparisonCriterion, this);
        } else if (criterion instanceof SelectedValueCriterion) {
            SelectedValueCriterion selectedValueCriterion = (SelectedValueCriterion) criterion;
            return new SelectedValueCriterionWrapper(selectedValueCriterion, this);
        } else if (criterion instanceof SubjectListCriterion) {
            SubjectListCriterion subjectListCriterion = (SubjectListCriterion) criterion;
            return new SubjectListCriterionWrapper(subjectListCriterion, getSubscription(), this);
        } else {
            throw new IllegalArgumentException("Illegal criterion type " + criterion.getClass());
        }
    }
        
    private StudySubscription getSubscription() {
        return getGroup().getForm().getQuery().getSubscription();
    }
    
    abstract List<String> getNonAnnotationFieldNames();
    
    abstract CriterionTypeEnum getCriterionTypeForNonAnnotationField(String fieldName);

}
