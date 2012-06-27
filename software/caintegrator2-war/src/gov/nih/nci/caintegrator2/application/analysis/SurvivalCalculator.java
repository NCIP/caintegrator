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
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

/**
 * This is a static utility class used for calculating survival values for a subject.
 */
public final class SurvivalCalculator {

    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_WEEK = 7;
    private static final int MONTHS_IN_YEAR = 12;
    private static final double DAYS_IN_YEAR = 365.25;
    private static final double DAYS_IN_MONTH = DAYS_IN_YEAR / MONTHS_IN_YEAR;
    private static final long ONE_HOUR = 60 * 60 * 1000L;


    private SurvivalCalculator() {
    }

    /**
     * Creates survival data based on the definition and subject.
     * @param survivalValueDefinition to calculate survival data for.
     * @param subjectAssignment subject to calculate survival data.
     * @return survival data.
     */
    public static SubjectSurvivalData createSubjectSurvivalData(SurvivalValueDefinition survivalValueDefinition,
            StudySubjectAssignment subjectAssignment) {
        if (SurvivalValueTypeEnum.DATE.equals(survivalValueDefinition.getSurvivalValueType())) {
            return SurvivalCalculator.createDateBasedSurvivalData(survivalValueDefinition, subjectAssignment);
        }
        if (SurvivalValueTypeEnum.LENGTH_OF_TIME.equals(survivalValueDefinition.getSurvivalValueType())) {
            return SurvivalCalculator.createDurationBasedSurvivalData(survivalValueDefinition, subjectAssignment);
        }
        throw new IllegalStateException("Unknown survival value type.");
    }

    private static SubjectSurvivalData createDateBasedSurvivalData(SurvivalValueDefinition survivalValueDefinition,
            StudySubjectAssignment subjectAssignment) {

        DateAnnotationValue subjectSurvivalStartDate = null;
        DateAnnotationValue subjectDeathDate = null;
        DateAnnotationValue subjectLastFollowupDate = null;
        subjectSurvivalStartDate = subjectAssignment.getDateAnnotation(survivalValueDefinition.getSurvivalStartDate());
        subjectDeathDate = subjectAssignment.getDateAnnotation(survivalValueDefinition.getDeathDate());
        subjectLastFollowupDate = subjectAssignment.getDateAnnotation(survivalValueDefinition.getLastFollowupDate());
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
        Integer survivalLength = calculateSurvivalLength(survivalValueDefinition, calSubjectStartDate,
                calSubjectEndDate);
        return new SubjectSurvivalData(survivalLength, censor);
    }

    private static Integer calculateSurvivalLength(SurvivalValueDefinition survivalValueDefinition,
            Calendar calSubjectStartDate, Calendar calSubjectEndDate) {
        Integer survivalLength = Integer.valueOf(0);
        Integer daysBetween = daysBetween(calSubjectStartDate, calSubjectEndDate);
        switch (survivalValueDefinition.getSurvivalLengthUnits()) {
        case DAYS:
            survivalLength = daysBetween;
            break;
        case WEEKS:
            survivalLength = Math.round(daysBetween / DAYS_IN_WEEK);
            break;
        case MONTHS:
            survivalLength = (int) Math.round(daysBetween / DAYS_IN_MONTH);
            break;
        default:
            throw new IllegalStateException("Unknown survival length type.");
        }
        return survivalLength;
    }

    private static SubjectSurvivalData createDurationBasedSurvivalData(SurvivalValueDefinition survivalValueDefinition,
            StudySubjectAssignment subjectAssignment) {
        NumericAnnotationValue survivalLength = subjectAssignment.getNumericAnnotation(
                survivalValueDefinition.getSurvivalLength());
        if (survivalLength == null || survivalLength.getNumericValue() == null) {
            return null;
        }
        return new SubjectSurvivalData((int) Math.round(survivalLength.getNumericValue()),
                getCensorStatusForDurationBasedSurvival(survivalValueDefinition, subjectAssignment));
    }

    private static Boolean getCensorStatusForDurationBasedSurvival(SurvivalValueDefinition survivalValueDefinition,
            StudySubjectAssignment subjectAssignment) {
        Boolean censor = false;
        if (survivalValueDefinition.getSurvivalStatus() != null
            && !StringUtils.isBlank(survivalValueDefinition.getValueForCensored())) {
            String survivalStatus = subjectAssignment.getAnnotationValueAsString(
                    survivalValueDefinition.getSurvivalStatus());
            try {
                censor = Double.valueOf(survivalValueDefinition.getValueForCensored()).
                    equals(Double.valueOf(survivalStatus));
            } catch (Exception e) {
                censor = survivalValueDefinition.getValueForCensored().equals(survivalStatus);
            }
        }
        return censor;
    }

    private static Boolean calculateEndDate(DateAnnotationValue subjectDeathDate,
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


    private static Integer daysBetween(Calendar startDate, Calendar endDate) {
        return Math.round(((endDate.getTimeInMillis() - startDate.getTimeInMillis() + ONE_HOUR))
                                / (ONE_HOUR * HOURS_IN_DAY));
    }

}
