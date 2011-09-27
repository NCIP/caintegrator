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

import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.study.AbstractClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.MatchScoreComparator;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyLogo;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicIntervalTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.imaging.Image;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implementation of the DAO.
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveClassLength" }) // Until we refactor into multiple DAOs
public class CaIntegrator2DaoImpl extends HibernateDaoSupport implements CaIntegrator2Dao  {

    private static final String UNCHECKED = "unchecked";
    private static final String ANNOTATION_DEFINITION_ASSOCIATION = "annotationDefinition";
    private static final String ANNOTATION_VALUE_ASSOCIATION = "annotationValue";
    private static final String ANNOTATION_VALUE_COLLECTION_ASSOCIATION = "annotationCollection";
    private static final String STUDY_SUBJECT_ASSIGNMENT_ASSOCIATION = "assignment";
    private static final String IMAGE_SERIES_ACQUISITION_ASSOCIATION = "imageStudy";
    private static final String STUDY_ASSOCIATION = "study";
    private static final String PLATFORM_ASSOCIATION = "platform";
    private static final String GENES_ASSOCIATION = "genes";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String SYMBOL_ATTRIBUTE = "symbol";
    private SecurityManager securityManager;
    
    /**
     * {@inheritDoc}
     */
    public UserWorkspace getWorkspace(String username) {
        @SuppressWarnings(UNCHECKED)
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
    public void clearSession() {
        getCurrentSession().clear();
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
    public void saveSubjectSourceStatus(AbstractClinicalSourceConfiguration source) {
        getHibernateTemplate().evict(source);
        AbstractClinicalSourceConfiguration newSource = get(source.getId(), AbstractClinicalSourceConfiguration.class);
        newSource.setStatus(source.getStatus());
        newSource.setStatusDescription(source.getStatusDescription());
        save(newSource);
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
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<UserWorkspace> retrieveAllSubscribedWorkspaces(Study study) {
        Criteria workspaceCriteria = getCurrentSession().createCriteria(UserWorkspace.class);
        workspaceCriteria.createCriteria("subscriptionCollection").
                          add(Restrictions.eq(STUDY_ASSOCIATION, study));
        return workspaceCriteria.list();
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
    public List<StudySubjectAssignment> findMatchingSubjects(SubjectListCriterion subjectListCriterion, Study study) {
        Criteria studySubjectAssignmentCrit = getCurrentSession().createCriteria(StudySubjectAssignment.class);
        studySubjectAssignmentCrit.add(Restrictions.in("identifier", subjectListCriterion.getSubjectIdentifiers()));
        createStudySubjectAssignmentCriteria(studySubjectAssignmentCrit, study);
        return studySubjectAssignmentCrit.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped    
    public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols, 
            ReporterTypeEnum reporterType, Study study, Platform platform) {
        Set<ReporterList> studyReporterLists = getStudyReporterLists(study, reporterType, platform);
        if (studyReporterLists.isEmpty()) {
            return Collections.emptySet();
        }
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        Criteria criteria = getCurrentSession().createCriteria(AbstractReporter.class);
        if (!geneSymbols.isEmpty()) {
            criteria.createCriteria(GENES_ASSOCIATION).add(Restrictions.in(SYMBOL_ATTRIBUTE, geneSymbols));
        }
        criteria.add(Restrictions.in("reporterList", studyReporterLists));
        reporters.addAll((List<AbstractReporter>) criteria.list());
        return reporters;
    }
    
    /**
     * {@inheritDoc}
     * @throws InvalidCriterionException 
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped    
    public List<SegmentData> findMatchingSegmentDatas(CopyNumberAlterationCriterion copyNumberCriterion,
            Study study, Platform platform) throws InvalidCriterionException {
        if (GenomicIntervalTypeEnum.GENE_NAME.equals(copyNumberCriterion.getGenomicIntervalType()) 
              && !copyNumberCriterion.getGeneSymbols().isEmpty() 
              && (platform.getGenomeVersion() == null || !isGenomeVersionMapped(platform.getGenomeVersion()))) {
            throw new InvalidCriterionException("The platform genome version '" 
                    + platform.getGenomeVersion().getValue() + "' is not mapped to chromosomal locations, "
                    + "it is not possible to query based on gene locations.");
        }
        return new CopyNumberAlterationCriterionConverter(copyNumberCriterion).
                        retrieveSegmentDataCriteria(study, platform, getCurrentSession()).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped    
    public List<SegmentData> findMatchingSegmentDatasByLocation(List<SegmentData> segmentDatasToMatch, 
            Study study, Platform platform) {
        Criteria segmentDataCrit = getCurrentSession().createCriteria(SegmentData.class);
        Criteria arrayDataCrit = segmentDataCrit.createCriteria("arrayData");
        Criteria reporterListsCrit = arrayDataCrit.createCriteria("reporterLists");
        reporterListsCrit.add(Restrictions.eq(PLATFORM_ASSOCIATION, platform));
        arrayDataCrit.add(Restrictions.eq(STUDY_ASSOCIATION, study));
        Junction overallOrStatement = Restrictions.disjunction();
        for (SegmentData segmentData : segmentDatasToMatch) {
            ChromosomalLocation location = segmentData.getLocation();
            overallOrStatement.add(Restrictions.conjunction().
                    add(Restrictions.eq("Location.startPosition", location.getStartPosition())).
                    add(Restrictions.eq("Location.endPosition", location.getEndPosition())));
        }
        segmentDataCrit.add(overallOrStatement);
        return segmentDataCrit.list();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isGenomeVersionMapped(GenomeBuildVersionEnum genomeVersion) {
        return !getCurrentSession().createCriteria(GeneLocationConfiguration.class).
                    add(Restrictions.eq("genomeBuildVersion", genomeVersion)).list().isEmpty();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped    
    public GeneLocationConfiguration getGeneLocationConfiguration(GenomeBuildVersionEnum genomeVersion) {
        List<GeneLocationConfiguration> geneLocationConfigurations = 
            getCurrentSession().createCriteria(GeneLocationConfiguration.class).
                add(Restrictions.eq("genomeBuildVersion", genomeVersion)).list();
        return geneLocationConfigurations.isEmpty() ? null : geneLocationConfigurations.get(0);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped    
    public List<Gene> findGenesByLocation(String chromosome, Integer startPosition, Integer endPosition, 
            GenomeBuildVersionEnum genomeBuildVersion) {
        String locStartPosition = "location.startPosition";
        String locEndPosition = "location.endPosition";
        Criteria geneLocationCriteria = getCurrentSession().createCriteria(GeneChromosomalLocation.class);
        // (gene.startPos <= startPosition && gene.endPos >= startPosition) 
        //  || (gene.startPos >= lowerInput  && gene.startPos <= higherInput) 
        Junction overallOrStatement = Restrictions.disjunction();
        overallOrStatement.add(Restrictions.conjunction().add(Restrictions.le(locStartPosition, startPosition))
                .add(Restrictions.ge(locEndPosition, startPosition)));
        overallOrStatement.add(Restrictions.conjunction().add(Restrictions.ge(locStartPosition, startPosition)).add(
                Restrictions.le(locStartPosition, endPosition)));
        geneLocationCriteria.add(overallOrStatement);
        geneLocationCriteria.add(getChromosomeRestriction(chromosome));
        geneLocationCriteria.createCriteria("geneLocationConfiguration").
            add(Restrictions.eq("genomeBuildVersion", genomeBuildVersion));
        geneLocationCriteria.setProjection(Projections.property("geneSymbol"));
        List<String> geneSymbols = geneLocationCriteria.list();
        return geneSymbols.isEmpty() ? new ArrayList<Gene>() 
                : getCurrentSession().createCriteria(Gene.class).setProjection(
                        Projections.distinct(Projections.property(SYMBOL_ATTRIBUTE))).
                        add(Restrictions.in(SYMBOL_ATTRIBUTE, geneSymbols)).
                        addOrder(Order.asc(SYMBOL_ATTRIBUTE)).list();
    }

    private Criterion getChromosomeRestriction(String chromosome) {
        if (Cai2Util.getAlternateChromosome(chromosome) != null) {
            List<String> chromosomes = new ArrayList<String>();
            chromosomes.add(chromosome);
            chromosomes.add(Cai2Util.getAlternateChromosome(chromosome));
            return Restrictions.in("location.chromosome", chromosomes);
        }
        return Restrictions.eq("location.chromosome", chromosome);
    }



    private Set<ReporterList> getStudyReporterLists(Study study, ReporterTypeEnum reporterType, Platform platform) {
        Set<ReporterList> reporterLists = new HashSet<ReporterList>();
        for (ArrayData arrayData : study.getArrayDatas(reporterType, platform)) {
            reporterLists.addAll(arrayData.getReporterLists());
        }
        return reporterLists;
    }
    
    /**
     * This function adds the values criteria for getting back the correct annotation values.
     * @param criterion - The main criterion object for the values we want.
     * @param mainAnnotationCriteria - The Criteria on the object that links directly to AbstractAnnotationValue
     * @param annotationValueRelationship - Relationship name that the above object has with AbstractAnnotationValue
     */
    private void createAnnotationValuesCriteria(AbstractAnnotationCriterion criterion,
            Criteria mainAnnotationCriteria, String annotationValueRelationship) {
        if (criterion != null && criterion.getAnnotationFieldDescriptor() != null) {
            Criteria valuesCrit = mainAnnotationCriteria.createCriteria(annotationValueRelationship);
            valuesCrit.add(Restrictions.eq(ANNOTATION_DEFINITION_ASSOCIATION, criterion.getAnnotationFieldDescriptor()
                .getDefinition()));
            valuesCrit.add(AbstractAnnotationCriterionHandler.create(criterion).translate());
        }
    }
    
    private void createStudySubjectAssignmentCriteria(Criteria studySubjectAssignmentCrit, Study study) {
        studySubjectAssignmentCrit.add(Restrictions.eq(STUDY_ASSOCIATION, study));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
    public AnnotationDefinition getAnnotationDefinition(String name, AnnotationTypeEnum dataType) {
        List<AnnotationDefinition> values = getCurrentSession().createCriteria(AnnotationDefinition.class)
                .add(Restrictions.eq("commonDataElement.longName", name))
                .add(Restrictions.eq("commonDataElement.valueDomain.dataType", dataType)).list();
        if (values.isEmpty()) {
            return null;
        } else {
            return values.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
    public AnnotationDefinition getAnnotationDefinition(Long cdeId, Float version) {
        List<AnnotationDefinition> values = (version == null)
            ? getCurrentSession().createCriteria(AnnotationDefinition.class)
                .add(Restrictions.eq("commonDataElement.publicID", cdeId)).list()
            : getCurrentSession().createCriteria(AnnotationDefinition.class)
                .add(Restrictions.eq("commonDataElement.publicID", cdeId))
                .add(Restrictions.eq("commonDataElement.version", version.toString())).list();
                    
        if (values.isEmpty()) {
            return null;
        } else {
            return latestVersion(values);
        }
    }
    
    private AnnotationDefinition latestVersion(List<AnnotationDefinition> values) {
        AnnotationDefinition result = values.get(0);
        for (AnnotationDefinition definition : values) {
            if (Float.valueOf(result.getCommonDataElement().getVersion())
                    < Float.valueOf(definition.getCommonDataElement().getVersion())) {
                result = definition;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Gene> getGeneSymbolMap() {
        Map <String, Gene> resultMap = new HashMap<String, Gene>();
        Query query = getCurrentSession().createQuery("from Gene");
        for (Object object : query.list()) {
            Gene gene = (Gene) object;
            resultMap.put(gene.getSymbol().toUpperCase(Locale.getDefault()), gene);
        }
        return resultMap;
    }
    
    /**
     * {@inheritDoc}
     */
    public Gene getGene(String symbol) {
        @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
        List values = getHibernateTemplate().findByNamedParam("from Gene where symbol = :symbol", 
                SYMBOL_ATTRIBUTE, symbol.toUpperCase(Locale.getDefault()));
        if (values.isEmpty()) {
            return null;
        } else {
            return (Gene) values.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Gene lookupOrCreateGene(String symbol) {
        Gene gene = getGene(symbol);
        if (gene == null) {
            gene = new Gene();
            gene.setSymbol(symbol.toUpperCase(Locale.getDefault()));
        }
        return gene;
    }

    /**
     * {@inheritDoc}
     */
    public Platform getPlatform(String name) {
        @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
        List values = getHibernateTemplate().findByNamedParam("from Platform where name = :name", 
                NAME_ATTRIBUTE, name);
        if (values.isEmpty()) {
            return null;
        } else {
            return (Platform) values.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public PlatformConfiguration getPlatformConfiguration(String name) {
        @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
        List values = getHibernateTemplate().findByNamedParam("from PlatformConfiguration where name = :name", 
                NAME_ATTRIBUTE, name);
        if (values.isEmpty()) {
            return null;
        } else {
            return (PlatformConfiguration) values.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
    public Set<String> retrieveGeneSymbolsInStudy(Collection<String> symbols, Study study) {
        Criteria reporterCriteria = getCurrentSession().createCriteria(AbstractReporter.class);
        reporterCriteria.createCriteria("reporterList").
                createCriteria("arrayDatas").
                    add(Restrictions.eq(STUDY_ASSOCIATION, study));
        reporterCriteria.createCriteria(GENES_ASSOCIATION).
            add(Restrictions.in(SYMBOL_ATTRIBUTE, symbols));
        Set<AbstractReporter> reporterSet = new HashSet<AbstractReporter>();
        reporterSet.addAll(reporterCriteria.list());
        Set<String> geneSymbols = new HashSet<String>();
        for (AbstractReporter reporter : reporterSet) {
            for (Gene gene : reporter.getGenes()) {
                String symbol = gene.getSymbol();
                if (Cai2Util.containsIgnoreCase(symbols, symbol)) {
                    geneSymbols.add(symbol.toUpperCase(Locale.US));
                }
            }
        }
        return geneSymbols;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)  // Hibernate operations are untyped
    public ReporterList getReporterList(String name) {
        List values = getHibernateTemplate().findByNamedParam("from ReporterList where name = :name", 
                NAME_ATTRIBUTE, name);
        if (values.isEmpty()) {
            return null;
        } else {
            return (ReporterList) values.get(0);
        }
    }

    private Session getCurrentSession() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession();
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
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<PlatformConfiguration> getPlatformConfigurations() {
        List<PlatformConfiguration> platformConfigurationList = 
            (List<PlatformConfiguration>) getCurrentSession().
                createCriteria(PlatformConfiguration.class).list();
        for (PlatformConfiguration configuration : platformConfigurationList) {
            if (configuration.getPlatform() == null) { 
                configuration.setInUse(false);
            } else {
                configuration.setInUse(isPlatformInUsed(configuration.getPlatform()));
            }
        }
        return platformConfigurationList;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public boolean isPlatformInUsed(Platform platform) {
        List<GenomicDataSourceConfiguration> result = getHibernateTemplate().find(
                "from GenomicDataSourceConfiguration where platformName = ?",
                new Object[] {platform.getName()});
        if (result.isEmpty()) {
            result =  getHibernateTemplate().find(
                    "from Array where platform = ?", new Object[] {platform});
        }
        return !result.isEmpty();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public List<StudyConfiguration> getStudyConfigurationsWhichNeedThisPlatform(Platform platform) {
        List<StudyConfiguration> listOfStudyConfigurations = new ArrayList<StudyConfiguration>();
        List<ArrayData> listOfArrayData = null;
        List<GenomicDataSourceConfiguration> genomicDataSourceConfigurationList = getHibernateTemplate().find(
                "from GenomicDataSourceConfiguration where platformName = ?",
                new Object[] {platform.getName()});
        if (!genomicDataSourceConfigurationList.isEmpty()) {
            listOfStudyConfigurations = getHibernateTemplate().find(
                    "select studyConfiguration from GenomicDataSourceConfiguration gdsc "
                    + "where gdsc.platformName = ?",
                    new Object[] {platform.getName()});
        } else {
            listOfArrayData = getHibernateTemplate().find(
                    "select arrayRef.arrayDataCollection from Array arrayRef "
                    + "where arrayRef.platform = ?",
                    new Object[] {platform});
            for (ArrayData arrayData : listOfArrayData) {
                listOfStudyConfigurations.add(arrayData.getStudy().getStudyConfiguration());
            }
        }
        // before returning list remove duplicates by converting to set and back
        Set<StudyConfiguration> setOfStudyConfigurations =
            new LinkedHashSet<StudyConfiguration>(listOfStudyConfigurations);
        return new ArrayList<StudyConfiguration>(setOfStudyConfigurations);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<Study> getPublicStudies() {
        return getCurrentSession().createCriteria(Study.class)
                                  .add(Restrictions.eq("publiclyAccessible", true))
                                  .addOrder(Order.asc("shortTitleText")).list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // Hibernate operations are untyped
    public List<Study> getStudies(String username) {
        secureCurrentSession(username);
        Query query = getCurrentSession().createQuery("from Study order by shortTitleText");
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public boolean isDuplicateStudyName(Study study, String username) {
        secureCurrentSession(username);
        long id = study.getId() == null ? 0 : study.getId().longValue();
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
    public <T> List<T> retrieveUniqueValuesForStudyAnnotation(Study study, 
                                                               AnnotationDefinition definition,
                                                               EntityTypeEnum entityType,
                                                               Class<T> objectClass) {
        AnnotationTypeEnum annotationType = definition.getDataType();
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
    public int retrieveNumberImages(Collection<ImageSeries> imageSeries) {
        Criteria imageCriteria = getCurrentSession().createCriteria(Image.class);
        imageCriteria.add(Restrictions.in("series", imageSeries));
        imageCriteria.setProjection(Projections.rowCount());
        return (Integer) imageCriteria.list().get(0);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public List<Platform> retrievePlatformsForGenomicSource(GenomicDataSourceConfiguration genomicSource) {
        Criteria arrayCriteria = getCurrentSession().createCriteria(Array.class);
        arrayCriteria.setProjection(Projections.distinct(Projections.property(PLATFORM_ASSOCIATION)))
                     .add(Restrictions.isNotNull(PLATFORM_ASSOCIATION))
                     .createCriteria("sampleCollection")
                     .add(Restrictions.eq("genomicDataSource", genomicSource));
                     
        return (List<Platform>) arrayCriteria.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public List<FileColumn> getFileColumnsUsingAnnotationFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor) {
        return (List<FileColumn>) getCurrentSession().createCriteria(FileColumn.class).add(
                Restrictions.eq("fieldDescriptor", fieldDescriptor)).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public List<ResultColumn> getResultColumnsUsingAnnotation(AnnotationFieldDescriptor fieldDescriptor) {
        return (List<ResultColumn>) getCurrentSession().createCriteria(ResultColumn.class).add(
                Restrictions.eq("annotationFieldDescriptor", fieldDescriptor)).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED)
    public List<AbstractAnnotationCriterion> getCriteriaUsingAnnotation(
            AnnotationFieldDescriptor fieldDescriptor) {
        return (List<AbstractAnnotationCriterion>) getCurrentSession().
                createCriteria(AbstractAnnotationCriterion.class).add(
                        Restrictions.eq("annotationFieldDescriptor", fieldDescriptor)).list();
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
    @SuppressWarnings("unchecked")      // hibernate operation not parameterized
    public Set<GisticAnalysis> getGisticAnalysisUsingGenomicSource(
            GenomicDataSourceConfiguration genomicSource) {
        return new HashSet<GisticAnalysis>(getCurrentSession().createCriteria(GisticAnalysis.class).
                createCriteria("samplesUsedForCalculation").
                add(Restrictions.eq("genomicDataSource", genomicSource)).list());
    }
    
    private void secureCurrentSession(String username) {
        if (securityManager != null) {
            try {
                securityManager.initializeFiltersForUserGroups(username, getCurrentSession());
            } catch (CSException e) {
                throw new IllegalStateException("Unable to use instance level filters.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setFlushMode(int mode) {
        getHibernateTemplate().setFlushMode(mode);
    }
    
    /**
     * {@inheritDoc}
     */
    public void runSessionKeepAlive() {
        getCurrentSession().createSQLQuery("select 1").list();
    }
    
    /**
     * @return the securityManager
     */
    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    /**
     * @param securityManager the securityManager to set
     */
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }    
    
}
