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
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.application.study.MatchScoreComparator;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implementation of the DAO.
 */
public class CaIntegrator2DaoImpl extends HibernateDaoSupport implements CaIntegrator2Dao  {
    
    private static final String UNCHECKED = "unchecked";
    private static final String ANNOTATION_VALUE_ASSOCIATION = "annotationValue";
    private static final String ANNOTATION_VALUE_COLLECTION_ASSOCIATION = "annotationCollection";

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public UserWorkspace getWorkspace(String username) {
        // TODO Real implementation requires checking ownership in CSM using username argument
        List results = getHibernateTemplate().find("from UserWorkspace");
        if (results.isEmpty()) {
            return null;
        } else {
            return (UserWorkspace) results.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void save(Object persistentObject) {
        getHibernateTemplate().saveOrUpdate(persistentObject);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public <T> T get(Long id, Class<T> objectClass) {
        return (T) getHibernateTemplate().get(objectClass, id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public List<AnnotationFieldDescriptor> findMatches(Collection<String> keywords) {
        List<AnnotationFieldDescriptor> annotationFieldDescriptors = new ArrayList<AnnotationFieldDescriptor>();
        List<AnnotationFieldDescriptor> results = getHibernateTemplate().find("from AnnotationFieldDescriptor");
        for (AnnotationFieldDescriptor afd : results) {
            if (containsKeyword(afd, keywords)) {
                annotationFieldDescriptors.add(afd);
            }
        }
        Collections.sort(annotationFieldDescriptors, new MatchScoreComparator(keywords));
        return annotationFieldDescriptors;
    }

    private boolean containsKeyword(AnnotationFieldDescriptor afd, Collection<String> keywords) {
        for (String keyword : keywords) {
            if (Cai2Util.containsIgnoreCase(afd.getKeywordsAsList(), keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public List<StudyConfiguration> getManagedStudies(String username) {
        // TODO Real implementation requires checking StudyConfiguration ownership in CSM when integrated
        return getHibernateTemplate().find("from StudyConfiguration sc order by sc.study.shortTitleText");
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<ImageSeriesAcquisition> findMatchingImageSeries(AbstractAnnotationCriterion criterion, Study study) {
        if (!criterion.getEntityType().equals(EntityTypeEnum.IMAGESERIES.getValue())) {
            // TODO : This should probably throw an error instead of returning null.
            return null;
        } else {
            Criteria imageSeriesAcquisitionCrit = getHibernateTemplate().
                                                  getSessionFactory().
                                                  getCurrentSession().
                                                  createCriteria(ImageSeriesAcquisition.class);
            createAnnotationValuesCriteria(criterion, 
                                           imageSeriesAcquisitionCrit.createCriteria("seriesCollection"), 
                                           ANNOTATION_VALUE_COLLECTION_ASSOCIATION);
            return imageSeriesAcquisitionCrit.list();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<SampleAcquisition> findMatchingSamples(AbstractAnnotationCriterion criterion, Study study) {
        if (!criterion.getEntityType().equals(EntityTypeEnum.SAMPLE.getValue())) {
            // TODO : This should probably throw an error instead of returning null.
            return null;
        } else {
            Criteria sampleAcquisitionCrit = getHibernateTemplate().
                                             getSessionFactory().
                                             getCurrentSession().
                                             createCriteria(SampleAcquisition.class);
            createAnnotationValuesCriteria(criterion, 
                                           sampleAcquisitionCrit, 
                                           ANNOTATION_VALUE_COLLECTION_ASSOCIATION);
            return sampleAcquisitionCrit.list();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<StudySubjectAssignment> findMatchingSubjects(AbstractAnnotationCriterion criterion, Study study) {
        if (!criterion.getEntityType().equals(EntityTypeEnum.SUBJECT.getValue())) {
            // TODO : This should probably throw an error instead of returning null.
            return null;
        } else {
            Criteria studySubjectAssignmentCrit = getHibernateTemplate().
                                             getSessionFactory().
                                             getCurrentSession().
                                             createCriteria(StudySubjectAssignment.class);
            createAnnotationValuesCriteria(criterion, 
                                           studySubjectAssignmentCrit.createCriteria("subjectAnnotationCollection"), 
                                           ANNOTATION_VALUE_ASSOCIATION);
            return studySubjectAssignmentCrit.list();
        }
    }
    
    /**
     * This function adds the values criteria for getting back the correct annotation values.
     * @param criterion - The main criterion object for the values we want.
     * @param mainAnnotationCriteria - The Criteria on the object that links directly to AbstractAnnotationValue
     * @param annotationValueRelationship - Relationship name that the above object has with AbstractAnnotationValue
     */
    private void createAnnotationValuesCriteria(AbstractAnnotationCriterion criterion,
                                              Criteria mainAnnotationCriteria, 
                                              String annotationValueRelationship) {
        Criteria valuesCrit = mainAnnotationCriteria.createCriteria(annotationValueRelationship);
        Criteria definitionCrit = valuesCrit.createCriteria("annotationDefinition");
        definitionCrit.add(Restrictions.idEq(criterion.getAnnotationDefinition().getId()));
        valuesCrit.add(AbstractAnnotationCriterionHandler.create(criterion).translate());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
    public AnnotationDefinition getAnnotationDefinition(String name) {
        List values = getHibernateTemplate().findByNamedParam("from AnnotationDefinition where displayName = :name", 
                "name", name);
        if (values.isEmpty()) {
            return null;
        } else {
            return (AnnotationDefinition) values.get(0);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
    public Gene getGene(String symbol) {
        List values = getHibernateTemplate().findByNamedParam("from Gene where symbol = :symbol", 
                "symbol", symbol);
        if (values.isEmpty()) {
            return null;
        } else {
            return (Gene) values.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
    public Platform getPlatform(String name) {
        List values = getHibernateTemplate().findByNamedParam("from Platform where name = :name", 
                "name", name);
        if (values.isEmpty()) {
            return null;
        } else {
            return (Platform) values.get(0);
        }
    }
    
}
