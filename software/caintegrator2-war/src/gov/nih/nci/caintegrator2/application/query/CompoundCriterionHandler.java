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

import gov.nih.nci.caintegrator2.application.study.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneListCriterion;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles CompoundCriterion objects.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
final class CompoundCriterionHandler extends AbstractCriterionHandler {

    private final Collection <AbstractCriterionHandler> handlers;
    private final CompoundCriterion compoundCriterion;
    
    private CompoundCriterionHandler(Collection <AbstractCriterionHandler> handlers, 
                                     CompoundCriterion compoundCriterion) {
        this.handlers = handlers;
        this.compoundCriterion = compoundCriterion;
    }
    

    /**
     * Creates the CompoundCriterionHandler based on the given CompoundCriterion.
     * @param compoundCriterion - compound criterion to create from.
     * @return CompoundCriterionHandler object returned, with the handlers collection filled.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity") // requires switch-like statement
    static CompoundCriterionHandler create(CompoundCriterion compoundCriterion) {
        Collection<AbstractCriterionHandler> handlers = new HashSet<AbstractCriterionHandler>();
        if (compoundCriterion.getCriterionCollection() != null) {
            for (AbstractCriterion abstractCriterion : compoundCriterion.getCriterionCollection()) {
                if (abstractCriterion instanceof AbstractAnnotationCriterion) {
                    handlers.add(new AnnotationCriterionHandler((AbstractAnnotationCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof CompoundCriterion) {
                    handlers.add(CompoundCriterionHandler.create((CompoundCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof GeneCriterion) {
                    handlers.add(GeneCriterionHandler.create((GeneCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof GeneListCriterion) {
                    handlers.add(GeneListCriterionHandler.create((GeneListCriterion) abstractCriterion));
                } else {
                    throw new IllegalStateException("Unknown AbstractCriterion class: " + abstractCriterion);
                }
            }
        }
        return new CompoundCriterionHandler(handlers, compoundCriterion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, Study study, Set<EntityTypeEnum> entityTypes) {
        boolean rowsRetrieved = false;
        Set<ResultRow> allValidRows = new HashSet<ResultRow>();
        for (AbstractCriterionHandler handler : handlers) {
            if (!handler.isEntityMatchHandler()) {
                continue;
            }
            Set<ResultRow> newRows = handler.getMatches(dao, study, entityTypes);
            if (!rowsRetrieved) {
                allValidRows = newRows;
                rowsRetrieved = true;
            } else {
                allValidRows = combineResults(allValidRows, newRows);
            }
            
        }
        return allValidRows;
    }
    
    /**
     * Combines the results of the rows.
     * @param currentValidRows - current rows that are valid.
     * @param newRows - new rows to validate.
     * @param defaultTimepoint - the default timepoint for the study.
     * @return - combination of rows.
     */
    private Set<ResultRow> combineResults(Set<ResultRow> currentValidRows, 
                                          Set<ResultRow> newRows) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
        if (compoundCriterion.getBooleanOperator() != null) {
           BooleanOperatorEnum booleanOperator = BooleanOperatorEnum.
                               getByValue(compoundCriterion.getBooleanOperator());
           switch(booleanOperator) {
           case AND:
               combinedResults = combineResultsForAndOperator(currentValidRows, newRows);
           break;
           case OR:
               combinedResults = combineResultsForOrOperator(currentValidRows, newRows);
           break;
           default:
               // TODO : figure out what to actually do in this case?
               combinedResults.addAll(currentValidRows);
               combinedResults.addAll(newRows);
           break;
           }
           
        }
        return combinedResults;
    }

    
    private Set<ResultRow> combineResultsForAndOperator(Set<ResultRow> currentValidRows, 
                                   Set<ResultRow> newRows) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
           for (ResultRow row : newRows) {
               if (Cai2Util.resultRowSetContainsResultRow(currentValidRows, row)) {
                   combinedResults.add(row);
               }
           }
           return combinedResults;
    }
    
    private Set<ResultRow> combineResultsForOrOperator(Set<ResultRow> currentValidRows,
                                             Set<ResultRow> newRows) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
        combinedResults.addAll(currentValidRows);
        for (ResultRow row : newRows) {
            if (!Cai2Util.resultRowSetContainsResultRow(combinedResults, row)) {
                combinedResults.add(row);
            }
            
        }
        return combinedResults;
    }

    @Override
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study) {
        Set<AbstractReporter> reporters = null;
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.isReporterMatchHandler()) {
                reporters = getCombinedReporterMatches(reporters, handler.getReporterMatches(dao, study));
            }
        }
        return reporters;
    }

    private Set<AbstractReporter> getCombinedReporterMatches(Set<AbstractReporter> reporters,
            Set<AbstractReporter> reporterMatches) {
        BooleanOperatorEnum operator = BooleanOperatorEnum.getByValue(compoundCriterion.getBooleanOperator());
        if (reporters == null) {
            return reporterMatches;
        } else if (BooleanOperatorEnum.AND.equals(operator)) {
            reporters.retainAll(reporterMatches);
        } else if (BooleanOperatorEnum.OR.equals(operator)) {
            reporters.addAll(reporterMatches);
        }
        return reporters;
    }

    @Override
    boolean isEntityMatchHandler() {
        return true;
    }

    @Override
    boolean isReporterMatchHandler() {
        return true;
    }

    @Override
    boolean hasEntityCriterion() {
        boolean hasEntityCriterion = false;
        for (AbstractCriterionHandler handler : handlers) {
            hasEntityCriterion |= handler.hasEntityCriterion();
        }
        return hasEntityCriterion;
    }


    @Override
    boolean hasReporterCriterion() {
        boolean hasReporterCriterion = false;
        for (AbstractCriterionHandler handler : handlers) {
            hasReporterCriterion |= handler.hasReporterCriterion();
        }
        return hasReporterCriterion;
    }
    
}
