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
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles AnnotationCriterion objects by retrieving the proper data from the DAO.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // See getMatches()
public class AnnotationCriterionHandler extends AbstractCriterionHandler {

    private final AbstractAnnotationCriterion abstractAnnotationCriterion;
    
    /**
     * Constructor based on the abstractAnnotationCriterion to use.
     * @param abstractAnnotationCriterion criterion object to use.
     */
    public AnnotationCriterionHandler(AbstractAnnotationCriterion abstractAnnotationCriterion) {
        this.abstractAnnotationCriterion = abstractAnnotationCriterion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "PMD.ExcessiveMethodLength" })   // switch statement and argument checking
    public Set<ResultRow> getMatches(CaIntegrator2Dao dao, Study study, Set<EntityTypeEnum> entityTypes) {
        EntityTypeEnum entityType = EntityTypeEnum.getByValue(abstractAnnotationCriterion.getEntityType());
        Set<ResultRow> resultRows = new HashSet<ResultRow>();
        switch(entityType) {
        case IMAGESERIES:
            for (ImageSeriesAcquisition imageSeriesAcquisition 
                    : dao.findMatchingImageSeries(abstractAnnotationCriterion, study)) {
                ResultRow row = new ResultRow();
                StudySubjectAssignment studySubjectAssignment = imageSeriesAcquisition.getAssignment();
                row.setImageSeriesAcquisition(imageSeriesAcquisition);
                row.setSubjectAssignment(studySubjectAssignment);
                if (entityTypes.contains(EntityTypeEnum.SAMPLE)) {
                    Timepoint imageSeriesTimepoint = imageSeriesAcquisition.getTimepoint();
                    addSampleRows(resultRows, row, studySubjectAssignment, imageSeriesTimepoint);
                } else {
                    resultRows.add(row);
                }
            }
            break;
        case SAMPLE:
            for (SampleAcquisition sampleAcquisition 
                    : dao.findMatchingSamples(abstractAnnotationCriterion, study)) {
                ResultRow row = new ResultRow();
                StudySubjectAssignment studySubjectAssignment = sampleAcquisition.getAssignment();
                row.setSampleAcquisition(sampleAcquisition);
                row.setSubjectAssignment(studySubjectAssignment);
                if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) {
                    Timepoint sampleAcquisitionTimepoint = sampleAcquisition.getTimepoint();
                    addImageSeriesRows(resultRows, row, studySubjectAssignment, sampleAcquisitionTimepoint);
                } else {
                    resultRows.add(row);
                }
            }
            break;
        case SUBJECT:
            for (StudySubjectAssignment studySubjectAssignment 
                    : dao.findMatchingSubjects(abstractAnnotationCriterion, study)) {
                ResultRow row = new ResultRow();
                row.setSubjectAssignment(studySubjectAssignment);
                if (entityTypes.contains(EntityTypeEnum.SAMPLE)) { // Sample
                    Set <ResultRow> newResultRows = new HashSet<ResultRow>();
                    addSampleRows(newResultRows, row, studySubjectAssignment, null);
                    if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) { // Sample + Image Series
                        for (ResultRow newRow : newResultRows) {
                            addImageSeriesRows(newResultRows, newRow, studySubjectAssignment, 
                                               newRow.getSampleAcquisition().getTimepoint());
                        }
                    }
                    resultRows.addAll(newResultRows);
                } else if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) { // Image Series w/o Sample
                    addImageSeriesRows(resultRows, row, studySubjectAssignment, null);
                } else { // No image series or rows, just clinical
                    resultRows.add(row);
                }
            }
            break;
        default:
            // Might need to handle this differently in the future.
            return null;
        }
        return resultRows;
    }


    private void addSampleRows(Set<ResultRow> resultRows, 
                               ResultRow row, 
                               StudySubjectAssignment studySubjectAssignment,
                               Timepoint timepoint) {
        boolean samplesFound = false;
        for (SampleAcquisition sampleAcquisition 
                : studySubjectAssignment.getSampleAcquisitionCollection()) {
            if (timepoint == null 
                || timepoint == sampleAcquisition.getTimepoint()) {
                ResultRow newRow = row;
                newRow.setSampleAcquisition(sampleAcquisition);
                resultRows.add(newRow);
                samplesFound = true;
            }
        }
        if (!samplesFound) {
            resultRows.add(row);
        }
    }
    
    private void addImageSeriesRows(Set<ResultRow> resultRows, 
                                        ResultRow row, 
                                        StudySubjectAssignment studySubjectAssignment,
                                        Timepoint timepoint) {
        boolean imageSeriesFound = false;
        for (ImageSeriesAcquisition imageSeriesAcquisition : studySubjectAssignment.getImageStudyCollection()) {
            if (timepoint == null 
                || timepoint == imageSeriesAcquisition.getTimepoint()) {
                ResultRow newRow = row;
                newRow.setImageSeriesAcquisition(imageSeriesAcquisition);
                resultRows.add(newRow);
                imageSeriesFound = true;
            }
        }
        if (!imageSeriesFound) {
            resultRows.add(row);
        }
    }

}
