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
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Handler that returns samples matching the given expression level criterion.
 */
public final class ExpressionLevelCriterionHandler extends AbstractCriterionHandler {

    private final ExpressionLevelCriterion criterion;

    private ExpressionLevelCriterionHandler(ExpressionLevelCriterion criterion) {
        this.criterion = criterion;
    }
    
    /**
     * 
     * @param expressionLevelCriterion the expression level criterion 
     * @return the expression level criterion handler
     */
    public static ExpressionLevelCriterionHandler create(ExpressionLevelCriterion expressionLevelCriterion) {
        return new ExpressionLevelCriterionHandler(expressionLevelCriterion);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query, 
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException {
        Study study = query.getSubscription().getStudy();
        ReporterTypeEnum reporterType = query.getReporterType();
        Platform platform = query.getGeneExpressionPlatform();
        Set<AbstractReporter> reporters = getReporterMatches(dao, study, reporterType, platform);
        
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addReporters(reporters);
        request.addArrayDatas(getCandidateArrayDatas(study, reporterType, platform));
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);        

        ArrayDataValues values = arrayDataService.getData(request);
        return getRows(values, entityTypes);
    }

    private Set<ResultRow> getRows(ArrayDataValues values, Set<EntityTypeEnum> entityTypes) {
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        Set<SampleAcquisition> sampleAcquisitions = new HashSet<SampleAcquisition>();
        for (ArrayData arrayData : values.getArrayDatas()) {
            if (hasExpressionLevelMatch(arrayData, values)) {
                sampleAcquisitions.add(arrayData.getSample().getSampleAcquisition());
            }
        }
        return rowFactory.getSampleRows(sampleAcquisitions);
    }

    private boolean hasExpressionLevelMatch(ArrayData arrayData, ArrayDataValues values) {
        for (AbstractReporter reporter : values.getReporters()) {
            Float expressionValue = values.getFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL);
            if (isExpressionMatch(expressionValue)) {
                return true;
            }
        }
        return false;
    }

    private boolean isExpressionMatch(Float expressionValue) {
        switch (criterion.getRangeType()) {
            case GREATER_OR_EQUAL:
                return isExpressionGreaterMatch(expressionValue);
            case LESS_OR_EQUAL:
                return isExpressionLowerMatch(expressionValue);
            case INSIDE_RANGE:
                return isExpressionGreaterMatch(expressionValue) && isExpressionLowerMatch(expressionValue);
            case OUTSIDE_RANGE:
                return !(isExpressionGreaterMatch(expressionValue) && isExpressionLowerMatch(expressionValue));
            default:
                throw new IllegalStateException("Illegal regulation type: " + criterion.getRangeType());
        }
    }
    
    boolean isGenomicValueMatchCriterion(Set<Gene> genes, Float value) {
        if (isReporterMatch(genes) && isExpressionMatch(value)) {
            return true;
        }
        return false;
    }

    private boolean isReporterMatch(Set<Gene> genes) {
        if (StringUtils.isBlank(criterion.getGeneSymbol())) {
            return true;
        } else {
            for (Gene gene : genes) {
                if (criterion.getGeneSymbol().toUpperCase().contains(gene.getSymbol().toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExpressionLowerMatch(Float expressionValue) {
        return criterion.getUpperLimit() == null ? true : expressionValue <= criterion.getUpperLimit();
    }

    private boolean isExpressionGreaterMatch(Float expressionValue) {
        return criterion.getLowerLimit() == null ? true : expressionValue >= criterion.getLowerLimit();
    }

    private Collection<ArrayData> getCandidateArrayDatas(Study study, 
            ReporterTypeEnum reporterType
            , Platform platform) {
        Set<ArrayData> candidateDatas = new HashSet<ArrayData>();
        candidateDatas.addAll(study.getArrayDatas(reporterType, platform));
        return candidateDatas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType, 
            Platform platform) {
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        if (StringUtils.isBlank(criterion.getGeneSymbol())) {
            reporters.addAll(findReporters(reporterType, study, platform));
        } else {
            reporters.addAll(dao.findReportersForGenes(criterion.getGeneSymbols(), reporterType, study, platform));
        }
        return reporters;
    }
    
    private Set<AbstractReporter> findReporters(ReporterTypeEnum reporterType, Study study, Platform platform) {
        Set<ReporterList> studyReporterLists = getStudyReporterLists(study, reporterType, platform);
        if (studyReporterLists.isEmpty()) {
            return Collections.emptySet();
        }
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        for (ReporterList reporterList : studyReporterLists) {
            reporters.addAll(reporterList.getReporters());
        }
        return reporters;
    }

    private Set<ReporterList> getStudyReporterLists(Study study, ReporterTypeEnum reporterType, Platform platform) {
        Set<ReporterList> reporterLists = new HashSet<ReporterList>();
        for (ArrayData arrayData : study.getArrayDatas(reporterType, platform)) {
            reporterLists.addAll(arrayData.getReporterLists());
        }
        return reporterLists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasEntityCriterion() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasReporterCriterion() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isEntityMatchHandler() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isReporterMatchHandler() {
        return true;
    }
    
    @Override
    boolean hasCriterionSpecifiedReporterValues() {
        return true;
    }
    
    @Override
    Set<SegmentData> getSegmentDataMatches(CaIntegrator2Dao dao, Study study, Platform platform) {
        return Collections.emptySet();
    }

    @Override
    boolean hasSegmentDataCriterion() {
        return false;
    }

    @Override
    boolean hasCriterionSpecifiedSegmentValues() {
        return false;
    }

    @Override
    boolean isSegmentValueMatchCriterion(Float value) {
        return false;
    }


}
