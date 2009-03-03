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

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.MatchScoreComparator;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyLogo;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataMatrix;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implementation of the DAO.
 */
public class CaIntegrator2DaoImpl extends HibernateDaoSupport implements CaIntegrator2Dao  {
    
    private static final String UNCHECKED = "unchecked";
    private static final String ANNOTATION_DEFINITION_ASSOCIATION = "annotationDefinition";
    private static final String ANNOTATION_VALUE_ASSOCIATION = "annotationValue";
    private static final String ANNOTATION_VALUE_COLLECTION_ASSOCIATION = "annotationCollection";
    private static final String STUDY_SUBJECT_ASSIGNMENT_ASSOCIATION = "assignment";
    private static final String IMAGE_SERIES_ACQUISITION_ASSOCIATION = "imageStudy";
    private static final String STUDY_ASSOCIATION = "study";
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public UserWorkspace getWorkspace(String username) {
        List results = getCurrentSession().
                        createCriteria(UserWorkspace.class).
                        add(Restrictions.eq("username", username)).list();
        if (results.isEmpty()) {
            return null;
        } else {
            return (UserWorkspace) results.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void refresh(Object persistentObject) {
        getHibernateTemplate().refresh(persistentObject);
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
    public void delete(Object persistentObject) {
        getHibernateTemplate().delete(persistentObject);
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
    public void removeObjects(Collection objects) {
        if (objects != null) {
            getHibernateTemplate().deleteAll(objects);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public List<AnnotationDefinition> findMatches(Collection<String> keywords) {
        List<AnnotationDefinition> annotationDefinitions = new ArrayList<AnnotationDefinition>();
        List<AnnotationDefinition> results = getHibernateTemplate().find("from AnnotationDefinition");
        for (AnnotationDefinition ad : results) {
            if (containsKeyword(ad, keywords)) {
                annotationDefinitions.add(ad);
            }
        }
        Collections.sort(annotationDefinitions, new MatchScoreComparator(keywords));
        return annotationDefinitions;
    }

    private boolean containsKeyword(AnnotationDefinition ad, Collection<String> keywords) {
        for (String keyword : keywords) {
            if (ad.getKeywords() != null 
                && Cai2Util.containsIgnoreCase(Arrays.asList(StringUtils.split(ad.getKeywords())), keyword)) {
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
    public List<ImageSeries> findMatchingImageSeries(AbstractAnnotationCriterion criterion, Study study) {
        if (!criterion.getEntityType().equals(EntityTypeEnum.IMAGESERIES)) {
            return new ArrayList<ImageSeries>();
        } 
        Criteria imageSeriesCrit = getCurrentSession().createCriteria(ImageSeries.class);
        if (criterion instanceof IdentifierCriterion) {
            imageSeriesCrit.add(AbstractAnnotationCriterionHandler.create(criterion).translate());
        } else {
            createAnnotationValuesCriteria(criterion, 
                                            imageSeriesCrit, 
                                           ANNOTATION_VALUE_COLLECTION_ASSOCIATION);
        }
            createStudySubjectAssignmentCriteria(
                        imageSeriesCrit.createCriteria(IMAGE_SERIES_ACQUISITION_ASSOCIATION).
                        createCriteria(STUDY_SUBJECT_ASSIGNMENT_ASSOCIATION), 
                        study);
        
        return imageSeriesCrit.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<SampleAcquisition> findMatchingSamples(AbstractAnnotationCriterion criterion, Study study) {
        if (!criterion.getEntityType().equals(EntityTypeEnum.SAMPLE)) {
            return new ArrayList<SampleAcquisition>();
        } else {
            Criteria sampleAcquisitionCrit = getCurrentSession().createCriteria(SampleAcquisition.class);
            createAnnotationValuesCriteria(criterion, 
                                           sampleAcquisitionCrit, 
                                           ANNOTATION_VALUE_COLLECTION_ASSOCIATION);
            createStudySubjectAssignmentCriteria(
                    sampleAcquisitionCrit.createCriteria(STUDY_SUBJECT_ASSIGNMENT_ASSOCIATION), 
                    study);
            return sampleAcquisitionCrit.list();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<StudySubjectAssignment> findMatchingSubjects(AbstractAnnotationCriterion criterion, Study study) {
        if (!criterion.getEntityType().equals(EntityTypeEnum.SUBJECT)) {
            return new ArrayList<StudySubjectAssignment>();
        }
        Criteria studySubjectAssignmentCrit = getCurrentSession().createCriteria(StudySubjectAssignment.class);
        if (criterion instanceof IdentifierCriterion) {
            studySubjectAssignmentCrit.add(AbstractAnnotationCriterionHandler.create(criterion).translate());
        } else {
            createAnnotationValuesCriteria(criterion, 
                                           studySubjectAssignmentCrit.createCriteria("subjectAnnotationCollection"), 
                                           ANNOTATION_VALUE_ASSOCIATION);
        }
            createStudySubjectAssignmentCriteria(studySubjectAssignmentCrit, study);
        return studySubjectAssignmentCrit.list();

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped    
    public Set<GeneExpressionReporter> findGeneExpressionReporters(Set<String> geneSymbols, 
            ReporterTypeEnum reporterType, Study study) {
        Set<GeneExpressionReporter> reporters = new HashSet<GeneExpressionReporter>();
        Criteria criteria = getCurrentSession().createCriteria(GeneExpressionReporter.class);
        criteria.createCriteria("gene").add(Restrictions.in("symbol", geneSymbols));
        Criteria reporterCriteria = criteria.createCriteria("reporterSet");
        reporterCriteria.add(Restrictions.eq("reporterType", reporterType));
        reporterCriteria.createCriteria("arrayDataCollection").add(Restrictions.eq("study", study));
        reporters.addAll((List<GeneExpressionReporter>) criteria.list());
        return reporters;
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
        valuesCrit.add(Restrictions.eq(ANNOTATION_DEFINITION_ASSOCIATION, criterion.getAnnotationDefinition()));
        valuesCrit.add(AbstractAnnotationCriterionHandler.create(criterion).translate());
    }
    
    private void createStudySubjectAssignmentCriteria(Criteria studySubjectAssignmentCrit, Study study) {
        studySubjectAssignmentCrit.add(Restrictions.eq(STUDY_ASSOCIATION, study));
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
    
    private Session getCurrentSession() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<ArrayDataMatrix> getArrayDataMatrixes(Study study, ReporterTypeEnum reporterType) {
        Query query = getCurrentSession().createQuery("from ArrayDataMatrix where study = :study "
                + "and reporterSet.reporterType = :reporterType");
        query.setEntity("study", study);
        query.setParameter("reporterType", reporterType);
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<Platform> getPlatforms() {
        Query query = getCurrentSession().createQuery("from Platform order by name");
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public boolean isDuplicateStudyName(Study study) {
        long id;
        if (study.getId() == null) {
            id = 0;
        } else {
            id = study.getId().longValue();
        }

        List<Study> result = getHibernateTemplate().find("from Study where shortTitleText = ? and id != ?",
                new Object[] {study.getShortTitleText(), id });

        return !result.isEmpty();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public StudyLogo retrieveStudyLogo(Long studyId, String studyShortTitleText) {
        List<StudyConfiguration> studyConfigurationList = 
                (List<StudyConfiguration>) getCurrentSession().
                    createCriteria(StudyConfiguration.class).
                    createCriteria(STUDY_ASSOCIATION).
                        add(Restrictions.eq("id", studyId)).
                        add(Restrictions.like("shortTitleText", studyShortTitleText)).list();
        if (studyConfigurationList != null && !studyConfigurationList.isEmpty()) {
            return studyConfigurationList.get(0).getStudyLogo();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public AbstractAnnotationValue retrieveValueForAnnotationSubject(StudySubjectAssignment subject,
                                                                    AnnotationDefinition annotationDefinition) {
        Criteria abstractAnnotationValueCriteria = getCurrentSession().
                                        createCriteria(AbstractAnnotationValue.class);
        abstractAnnotationValueCriteria.add(Restrictions.eq(ANNOTATION_DEFINITION_ASSOCIATION, annotationDefinition));
        abstractAnnotationValueCriteria.createCriteria("subjectAnnotation").
                add(Restrictions.eq("studySubjectAssignment", subject));
        List<AbstractAnnotationValue> valueList = abstractAnnotationValueCriteria.list();
        AbstractAnnotationValue value = null;
        if (valueList != null && !valueList.isEmpty()) {
            value = valueList.get(0);
        }                
        return value;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public <T> List<T> retrieveUniqueValuesForStudyAnnotation(Study study, 
                                                               AnnotationDefinition definition,
                                                               EntityTypeEnum entityType,
                                                               Class<T> objectClass) {
        AnnotationTypeEnum annotationType = AnnotationTypeEnum.getByValue(definition.getType());
        if (annotationType == null) {
            throw new IllegalArgumentException("Data Type for the Annotation Definition is unknown.");
        }
        Criteria abstractAnnotationValueCriteria = getCurrentSession().createCriteria(AbstractAnnotationValue.class);
        abstractAnnotationValueCriteria.add(Restrictions.eq(ANNOTATION_DEFINITION_ASSOCIATION, definition));
        addValuesToStudyCriteria(study, entityType, abstractAnnotationValueCriteria);
        
        String uniqueAttribute = "stringValue";
        switch (annotationType) {
        case STRING:
            uniqueAttribute = "stringValue";
            break;
        case NUMERIC:
            uniqueAttribute = "numericValue";
            break;
        case DATE:
            uniqueAttribute = "dateValue";
            break;
        default:
            throw new IllegalStateException("Unknown Annotation Type Type.");
        }
        abstractAnnotationValueCriteria.setProjection(Projections.distinct(Projections.property(uniqueAttribute)));
        return (List<T>) abstractAnnotationValueCriteria.list();
        
    }

    /**
     * Adds the criteria to the AnnotationValue object which will associate it to the given Study.
     * @param study
     * @param entityType
     * @param abstractAnnotationValueCriteria
     */
    private void addValuesToStudyCriteria(Study study, EntityTypeEnum entityType,
            Criteria abstractAnnotationValueCriteria) {
        switch (entityType) {
        case SUBJECT:
            abstractAnnotationValueCriteria.createCriteria("subjectAnnotation")
                                            .createCriteria("studySubjectAssignment")
                                            .add(Restrictions.eq(STUDY_ASSOCIATION, study));
            break;
        case IMAGESERIES:
            abstractAnnotationValueCriteria.createCriteria("imageSeries")
                                           .createCriteria("imageStudy")
                                           .createCriteria(STUDY_SUBJECT_ASSIGNMENT_ASSOCIATION)
                                           .add(Restrictions.eq(STUDY_ASSOCIATION, study));
            break;
        case IMAGE:
            abstractAnnotationValueCriteria.createCriteria("image")
                                            .createCriteria("series")
                                            .createCriteria("imageStudy")
                                            .createCriteria(STUDY_SUBJECT_ASSIGNMENT_ASSOCIATION)
                                            .add(Restrictions.eq(STUDY_ASSOCIATION, study));
            break;
        case SAMPLE:
            abstractAnnotationValueCriteria.createCriteria("sampleAcquisition")
                                           .createCriteria(STUDY_SUBJECT_ASSIGNMENT_ASSOCIATION)
                                           .add(Restrictions.eq(STUDY_ASSOCIATION, study));
            break;
        default:
            throw new IllegalStateException("Unknown Entity Type.");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")      // hibernate operation not parameterized
    public <T> T merge(T persistentObject) {
        return (T) getHibernateTemplate().merge(persistentObject);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public ImageDataSourceConfiguration retrieveImagingDataSourceForStudy(Study study) {
        Criteria imagingDataSourceConfigurationCrit = getCurrentSession().
                                                      createCriteria(ImageDataSourceConfiguration.class);
        imagingDataSourceConfigurationCrit.createCriteria("studyConfiguration")
                                          .add(Restrictions.eq(STUDY_ASSOCIATION, study));
        List<ImageDataSourceConfiguration> sourceList = imagingDataSourceConfigurationCrit.list();
        if (sourceList != null && !sourceList.isEmpty()) {
            return sourceList.get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setFlushMode(int mode) {
        getHibernateTemplate().setFlushMode(mode);
    }
}
