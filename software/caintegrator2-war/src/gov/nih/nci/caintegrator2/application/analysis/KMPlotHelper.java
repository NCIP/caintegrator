/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caArray
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caArray Software License (the License) is between NCI and You. You (or 
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
 * its rights in the caArray Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caArray Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator Software and any 
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
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotService;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.study.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.PermissibleValueUtil;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.awt.Color;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Helper method for AnalysisService to generate a KMPlot object.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See calculateEndDate function
class KMPlotHelper {
    
    private static final int MONTHS_IN_YEAR = 12;
    private final KMPlotService kmPlotService;
    private final CaIntegrator2Dao dao;
    private final SurvivalValueDefinition survivalValueDefinition;
    private final QueryManagementService queryManagementService;
    private final Map<SubjectGroup, AbstractPermissibleValue> subjectGroupPermissibleValue = 
                                        new HashMap<SubjectGroup, AbstractPermissibleValue>();
    
    KMPlotHelper(KMPlotService kmPlotService, CaIntegrator2Dao dao, SurvivalValueDefinition survivalValueDefinition, 
                 QueryManagementService queryManagementService) {
        this.kmPlotService = kmPlotService;
        this.dao = dao;
        this.survivalValueDefinition = survivalValueDefinition;
        this.queryManagementService = queryManagementService;
    }
    
    KMPlot createPlot(StudySubscription subscription, 
                      AnnotationDefinition groupAnnotationField,
                      EntityTypeEnum groupFieldType,
                      Collection<AbstractPermissibleValue> plotGroupValues) {
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        if (survivalValueDefinition == null) {
            throw new IllegalArgumentException("SurvivalValueDefinition cannot be null");
        }
        if (survivalValueDefinition.getSurvivalStartDate() == null
             || survivalValueDefinition.getDeathDate() == null 
             || survivalValueDefinition.getLastFollowupDate() == null) {
            throw new IllegalArgumentException("Must have a Start Date, Death Date, and Last Followup Date" 
                    + " defined for definition '" + survivalValueDefinition.getName() + "'.");
        }
        Collection <SubjectGroup> subjectGroupCollection = new HashSet<SubjectGroup>();
        retrieveSubjectGroups(plotGroupValues, subjectGroupCollection);
        Collection <ResultRow> subjectRows = 
                retrieveSubjectRowsFromDatabase(groupFieldType, groupAnnotationField, subscription);
        retrieveSubjectSurvivalData(groupAnnotationField, subjectRows, subjectGroupCollection);
        filterGroupsWithoutSurvivalData(configuration, subjectGroupCollection);
        return kmPlotService.generatePlot(configuration);
    }

    private void filterGroupsWithoutSurvivalData(KMPlotConfiguration configuration,
            Collection<SubjectGroup> subjectGroupCollection) {
        for (SubjectGroup group : subjectGroupCollection) {
            if (group.getSurvivalData().isEmpty()) {
                configuration.getFilteredGroups().add(group);
            } else {
                configuration.getGroups().add(group);
            }
        }
    }

    private void retrieveSubjectSurvivalData(AnnotationDefinition groupAnnotationField,
                                            Collection <ResultRow> rows, 
                                            Collection<SubjectGroup> subjectGroupCollection) {
        for (ResultRow row : rows) {
            StudySubjectAssignment subjectAssignment = row.getSubjectAssignment();
            SubjectSurvivalData subjectSurvivalData = createSubjectSurvivalData(subjectAssignment);
            if (subjectSurvivalData != null) {
                AbstractAnnotationValue subjectPlotGroupValue = null;
                for (ResultValue value : row.getValueCollection()) {
                    if (value.getColumn().getAnnotationDefinition().equals(groupAnnotationField)) {
                        subjectPlotGroupValue = value.getValue();
                        break;
                    }
                }
                assignSubjectToGroup(subjectGroupCollection, subjectSurvivalData, subjectPlotGroupValue);
            }
        }
    }

    private SubjectSurvivalData createSubjectSurvivalData(StudySubjectAssignment subjectAssignment) {
        Integer survivalLength = Integer.valueOf(0);
        DateAnnotationValue subjectSurvivalStartDate = null;
        DateAnnotationValue subjectDeathDate = null;
        DateAnnotationValue subjectLastFollowupDate = null;
        subjectSurvivalStartDate = (DateAnnotationValue) dao.retrieveValueForAnnotationSubject(
                                    subjectAssignment, survivalValueDefinition.getSurvivalStartDate());
        subjectDeathDate = (DateAnnotationValue) dao.retrieveValueForAnnotationSubject(
                                    subjectAssignment, survivalValueDefinition.getDeathDate());
        subjectLastFollowupDate = (DateAnnotationValue) dao.retrieveValueForAnnotationSubject(
                                    subjectAssignment, survivalValueDefinition.getLastFollowupDate());
        Calendar calSubjectStartDate = Calendar.getInstance();
        Calendar calSubjectEndDate = Calendar.getInstance();
        if (subjectSurvivalStartDate != null && subjectSurvivalStartDate.getDateValue() != null) {
            calSubjectStartDate.setTime(subjectSurvivalStartDate.getDateValue());
        } else {
            return null;
        }
        Boolean censor = calculateEndDate(subjectDeathDate, subjectLastFollowupDate, calSubjectEndDate);
        if (censor == null) {
            return null;
        }
        survivalLength = monthsBetween(calSubjectStartDate, calSubjectEndDate);
        return new SubjectSurvivalData(survivalLength, censor);
    }

    @SuppressWarnings("PMD.CyclomaticComplexity") // Null checks are necessary
    private Boolean calculateEndDate(DateAnnotationValue subjectDeathDate,
                                    DateAnnotationValue subjectLastFollowupDate, 
                                    Calendar calSubjectEndDate) {
        Boolean censor = false;
        if ((subjectDeathDate == null || subjectDeathDate.getDateValue() == null)
              && (subjectLastFollowupDate != null && subjectLastFollowupDate.getDateValue() != null)) {
            calSubjectEndDate.setTime(subjectLastFollowupDate.getDateValue());
            censor = true;
        } else if ((subjectDeathDate != null && subjectDeathDate.getDateValue() != null)) {
            calSubjectEndDate.setTime(subjectDeathDate.getDateValue());
            censor = false;
        } else {
            return null;
        }
        return censor;
    }

    private void assignSubjectToGroup(Collection<SubjectGroup> subjectGroupCollection,
            SubjectSurvivalData subjectSurvivalData, AbstractAnnotationValue subjectPlotGroupValue) {
        for (SubjectGroup subjectGroup : subjectGroupCollection) {
            if (Cai2Util.annotationValueBelongToPermissibleValue(
                        subjectPlotGroupValue, subjectGroupPermissibleValue.get(subjectGroup))) {
                subjectGroup.getSurvivalData().add(subjectSurvivalData);
                break;
            }
        }
    }

    private void retrieveSubjectGroups(Collection<AbstractPermissibleValue> plotGroupValues,
            Collection<SubjectGroup> subjectGroupCollection) {
        for (AbstractPermissibleValue plotGroupValue : plotGroupValues) {
            SubjectGroup subjectGroup = new SubjectGroup();
            subjectGroup.setName(PermissibleValueUtil.getDisplayString(plotGroupValue));
            subjectGroupPermissibleValue.put(subjectGroup, plotGroupValue);
            subjectGroupCollection.add(subjectGroup);
            subjectGroup.setColor(getColor(subjectGroupCollection.size()));
        }
    }

    private Color getColor(int colorNumber) {
        switch(colorNumber) {
            case 1:
                return Color.GREEN;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.RED;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.DARK_GRAY;
            case 6:
                return Color.YELLOW;
            case 7:
                return Color.LIGHT_GRAY;
            case 8:
                return Color.MAGENTA;
            case 9:
                return Color.ORANGE;
            case 10:
                return Color.PINK;
            default:
                return Color.BLACK;
        }
    }
    
    private Integer monthsBetween(Calendar startDate, Calendar endDate) {
        int yearsBetween = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
        int monthsBetween = endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
        return ((yearsBetween * MONTHS_IN_YEAR) + monthsBetween);
    }
    
    private Collection<ResultRow> retrieveSubjectRowsFromDatabase(EntityTypeEnum groupFieldType, 
                                                 AnnotationDefinition groupAnnotationField,
                                                 StudySubscription subscription) {
        Query query = new Query();
        ResultColumn column = new ResultColumn();
        column.setAnnotationDefinition(groupAnnotationField);
        column.setEntityType(groupFieldType.getValue());
        column.setColumnIndex(0);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.getColumnCollection().add(column);
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND.getValue());
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setSubscription(subscription);
        QueryResult queryResult = queryManagementService.execute(query);
        return queryResult.getRowCollection();
    }
    
    
    
}
