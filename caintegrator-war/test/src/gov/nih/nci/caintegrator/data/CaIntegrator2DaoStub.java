/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.data;

import gov.nih.nci.caintegrator.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.study.AbstractClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.AuthorizedAnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.AuthorizedQuery;
import gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator.application.study.FileColumn;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyLogo;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.application.WildCardTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.domain.translational.Timepoint;
import gov.nih.nci.security.authorization.domainobjects.Group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CaIntegrator2DaoStub implements CaIntegrator2Dao {

    public boolean getCalled;
    public boolean saveCalled;
    public boolean deleteCalled;
    public boolean deleteGeneListCalled;
    public boolean deleteSubjectListCalled;
    public boolean getManagedStudiesCalled;
    public boolean findMatchesCalled;
    public boolean findMatchingImageSeriesCalled;
    public boolean findMatchingSamplesCalled;
    public boolean findMatchingSubjectsCalled;
    public boolean getGeneCalled;
    public boolean getPlatformCalled;
    public boolean getPlatformConfigurationCalled;
    private StudySubjectAssignment studySubjectAssignment = new StudySubjectAssignment();
    private final Timepoint timepoint = new Timepoint();
    public boolean getArrayDataMatrixesCalled;
    public boolean getWorkspaceCalled;
    public boolean removeObjectsCalled;
    public boolean findGeneExpressionReportersCalled;
    public boolean isDuplicateStudyNameCalled;
    public boolean retrieveStudyLogoCalled;
    public boolean mergeCalled;
    public boolean getPlatformsCalled;
    public boolean getStudiesCalled;
    public boolean getStudyConfigurationsCalled;
    public boolean retrieveImagingDataSourceForStudyCalled;
    public boolean setFlushModeCalled;
    public boolean refreshCalled;
    public boolean retrieveNumberImagesCalled;
    public boolean retrievePlatformsForGenomicSourceCalled;
    public boolean retrieveAllSubscribedWorkspacesCalled;
    private Platform platform = new Platform();
    public final List<FileColumn> fileColumns = new ArrayList<FileColumn>();
    private static final String USER_EXISTS = "studyManager";
    private static final String EXP_ID = "caArray Experiment ID 1";

    @Override
    public UserWorkspace getWorkspace(String username) {
        getWorkspaceCalled = true;
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(username);
        return userWorkspace;
    }

    @Override
    public void save(Object entity) {
        saveCalled = true;
    }

    @Override
    public void delete(Object persistentObject) {
        deleteCalled = true;
    }

    public void clear() {
        getCalled = false;
        saveCalled = false;
        deleteCalled = false;
        deleteGeneListCalled = false;
        deleteSubjectListCalled = false;
        findMatchesCalled = false;
        getManagedStudiesCalled = false;
        findMatchingImageSeriesCalled = false;
        findMatchingSamplesCalled = false;
        findMatchingSubjectsCalled = false;
        getGeneCalled = false;
        getPlatformCalled = false;
        getPlatformConfigurationCalled = false;
        getArrayDataMatrixesCalled = false;
        studySubjectAssignment.setId(Long.valueOf(1));
        timepoint.setId(Long.valueOf(1));
        getWorkspaceCalled = false;
        removeObjectsCalled = false;
        findGeneExpressionReportersCalled = false;
        isDuplicateStudyNameCalled = false;
        retrieveStudyLogoCalled = false;
        mergeCalled = false;
        retrieveImagingDataSourceForStudyCalled = false;
        getPlatformsCalled = false;
        getStudiesCalled = false;
        getStudyConfigurationsCalled = false;
        setFlushModeCalled = false;
        refreshCalled = false;
        retrieveNumberImagesCalled = false;
        retrievePlatformsForGenomicSourceCalled = false;
        retrieveAllSubscribedWorkspacesCalled = false;
    }

    @Override
    public <T> T get(Long id, Class<T> objectClass) {
        getCalled = true;
        T object;
        try {
            object = objectClass.newInstance();
            objectClass.getMethod("setId", new Class[] {Long.class}).invoke(object, id);
            return object;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<AnnotationDefinition> findMatches(Collection<String> keywords) {
        findMatchesCalled = true;
        List<AnnotationDefinition> definitions = new ArrayList<AnnotationDefinition>();
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.getCommonDataElement().setLongName("definitionName");
        definitions.add(definition);
        return definitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImageSeries> findMatchingImageSeries(AbstractAnnotationCriterion criterion, Study study) {
        findMatchingImageSeriesCalled = true;
        ImageSeriesAcquisition imageSeriesAcquisition = new ImageSeriesAcquisition();
        imageSeriesAcquisition.setAssignment(studySubjectAssignment);
        imageSeriesAcquisition.setTimepoint(timepoint);
        ImageSeries series = new ImageSeries();
        series.setImageStudy(imageSeriesAcquisition);
        List<ImageSeries> isList = new ArrayList<ImageSeries>();
        isList.add(series);
        return isList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SampleAcquisition> findMatchingSamples(AbstractAnnotationCriterion criterion, Study study) {
        findMatchingSamplesCalled = true;
        SampleAcquisition sampleAcquisition = new SampleAcquisition();
        sampleAcquisition.setTimepoint(timepoint);
        sampleAcquisition.setAssignment(studySubjectAssignment);
        List<SampleAcquisition> saList = new ArrayList<SampleAcquisition>();
        saList.add(sampleAcquisition);
        return saList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StudySubjectAssignment> findMatchingSubjects(AbstractAnnotationCriterion criterion, Study study) {
        findMatchingSubjectsCalled = true;
        List<StudySubjectAssignment> ssaList = new ArrayList<StudySubjectAssignment>();
        studySubjectAssignment = study.getStudyConfiguration().getOrCreateSubjectAssignment("SubjectID1");
        studySubjectAssignment = populateStudySubjectAssignment(studySubjectAssignment);
        ssaList.add(studySubjectAssignment);
        return ssaList;
    }

    /**
     * Populate a StudySubjectAssignment for testing.  This method only in the daoStub.
     * @param localStudySubjectAssignment
     * @return
     *
     */
    private StudySubjectAssignment populateStudySubjectAssignment(StudySubjectAssignment localStudySubjectAssignment) {
        localStudySubjectAssignment.setId(Long.valueOf(1));
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        StringAnnotationValue stringAnnotationValue = new StringAnnotationValue();
        stringAnnotationValue.setStringValue("F");
        stringAnnotationValue.setAnnotationDefinition(getAd("Gender", AnnotationTypeEnum.STRING));
        subjectAnnotation.setAnnotationValue(stringAnnotationValue);
        localStudySubjectAssignment.getSubjectAnnotationCollection().clear();
        localStudySubjectAssignment.getSubjectAnnotationCollection().add(subjectAnnotation);
        return localStudySubjectAssignment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition getAnnotationDefinition(String name, AnnotationTypeEnum dataType) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition getAnnotationDefinition(Long cdeId, Float version) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Gene getGene(String symbol) {
        getGeneCalled = true;
        Gene gene = new Gene();
        gene.setSymbol(symbol.toUpperCase(Locale.getDefault()));
        return gene;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformConfiguration getPlatformConfiguration(String name) {
        getPlatformConfigurationCalled = true;
        PlatformConfiguration platformConfiguration = new PlatformConfiguration();
        platformConfiguration.setName(name);
        platform.getPlatformConfiguration().setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        platform.getPlatformConfiguration().setPlatformChannelType(PlatformChannelTypeEnum.ONE_COLOR);
        return platformConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Platform getPlatform(String name) {
        getPlatformCalled = true;
        platform.setName(name);
        platform.setPlatformConfiguration(new PlatformConfiguration());
        platform.getPlatformConfiguration().setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        platform.getPlatformConfiguration().setPlatformChannelType(PlatformChannelTypeEnum.ONE_COLOR);
        return platform;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"rawtypes" })
    public void removeObjects(Collection objects) {
        removeObjectsCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDuplicateStudyName(Study study, String username) {
        isDuplicateStudyNameCalled =true;
        return false;
    }

    @Override
    public StudyLogo retrieveStudyLogo(Long id, String fileName) {
        retrieveStudyLogoCalled = true;
        return new StudyLogo();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> retrieveUniqueValuesForStudyAnnotation(Study study, AnnotationDefinition definition,
            EntityTypeEnum entityType, Class<T> objectClass) {
        List<T> values = new ArrayList<T>();
        for (AbstractAnnotationValue value : definition.getAnnotationValueCollection()) {
            if (value instanceof StringAnnotationValue) {
                StringAnnotationValue stringVal = (StringAnnotationValue) value;
                values.add((T) stringVal.getStringValue());
            } else if (value instanceof NumericAnnotationValue) {
                NumericAnnotationValue numericVal = (NumericAnnotationValue) value;
                values.add((T) numericVal.getNumericValue());
            } else if (value instanceof DateAnnotationValue) {
                DateAnnotationValue dateVal = (DateAnnotationValue) value;
                values.add((T) dateVal.getDateValue());
            }
        }
        return values;
    }

    @Override
    public <T> T merge(T persistentObject) {
        mergeCalled = true;
        return persistentObject;
    }

    @Override
    public ImageDataSourceConfiguration retrieveImagingDataSourceForStudy(Study study) {
        retrieveImagingDataSourceForStudyCalled = true;
        return new ImageDataSourceConfiguration();
    }

    @Override
    public List<Platform> getPlatforms() {
        getPlatformsCalled = true;
        return Collections.emptyList();
    }

    @Override
    public List<Study> getStudies(String username) {
        getStudiesCalled = true;
        Study study = new Study();
        List<Study> studies = new ArrayList<Study>();
        studies.add(study);
        return studies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFlushMode(int mode) {
        setFlushModeCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbol, ReporterTypeEnum reporterType,
            Study study, Platform platform) {
        findGeneExpressionReportersCalled = true;
        return Collections.emptySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh(Object persistentObject) {
        refreshCalled = true;
    }

    @Override
    public int retrieveNumberImages(Collection<ImageSeries> imageSeriesCollection) {
        int numberImages = 0;
        for (ImageSeries imageSeries : imageSeriesCollection) {
            if (imageSeries != null && imageSeries.getImageCollection() != null) {
                numberImages += imageSeries.getImageCollection().size();
            }
        }
        retrieveNumberImagesCalled = true;
        return numberImages;
    }


    @Override
    public List<Platform> retrievePlatformsForGenomicSource(GenomicDataSourceConfiguration genomicSource) {
        Set<Platform> platforms = new HashSet<Platform>();
        for (Sample sample : genomicSource.getSamples()) {
            for (Array array : sample.getArrayCollection()) {
                platforms.add(array.getPlatform());
            }
        }
        List<Platform> platformList = new ArrayList<Platform>();
        platformList.addAll(platforms);
        retrievePlatformsForGenomicSourceCalled = true;
        return platformList;
    }

    @Override
    public List<UserWorkspace> retrieveAllSubscribedWorkspaces(Study study) {
        retrieveAllSubscribedWorkspacesCalled = true;
        return new ArrayList<UserWorkspace>();
    }

    @Override
    public ReporterList getReporterList(String name) {
        return platform.addReporterList(name, ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
    }

    @Override
    public Set<String> retrieveGeneSymbolsInStudy(Collection<String> symbols, Study study) {
        Set<String> symbolsSet = new HashSet<String>();
        symbolsSet.addAll(symbols);
        symbolsSet.remove("EGFR");
        return symbolsSet;
    }

    @Override
    public boolean isPlatformInUsed(Platform platform) {
        return false;
    }

    @Override
    public List<PlatformConfiguration> getPlatformConfigurations() {
        return null;
    }

    @Override
    public List<StudySubjectAssignment> findMatchingSubjects(SubjectListCriterion subjectListCriterion, Study study) {
        findMatchingSubjectsCalled = true;
        List<StudySubjectAssignment> ssaList = new ArrayList<StudySubjectAssignment>();
        ssaList.add(studySubjectAssignment);
        return ssaList;
    }

    @Override
    public List<FileColumn> getFileColumnsUsingAnnotationFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor) {

        return fileColumns;
    }

    @Override
    public List<Study> getPublicStudies() {
        getStudiesCalled = true;
        Study study = new Study();
        List<Study> studies = new ArrayList<Study>();
        studies.add(study);
        return studies;
    }

    @Override
    public List<AbstractAnnotationCriterion> getCriteriaUsingAnnotation(AnnotationFieldDescriptor fieldDescriptor) {
        return new ArrayList<AbstractAnnotationCriterion>();
    }

    @Override
    public List<ResultColumn> getResultColumnsUsingAnnotation(AnnotationFieldDescriptor fieldDescriptor) {
        return new ArrayList<ResultColumn>();
    }

    @Override
    public Gene lookupOrCreateGene(String symbol) {
        Gene gene = new Gene();
        gene.setSymbol(symbol.toUpperCase(Locale.getDefault()));
        return gene;
    }

    @Override
    public Map<String, Gene> getGeneSymbolMap() {
        return new HashMap<String, Gene>();
    }

    @Override
    public List<SegmentData> findMatchingSegmentDatas(CopyNumberAlterationCriterion copyNumberCriterion, Study study,
            Platform platform) throws InvalidCriterionException {
        return null;
    }

    @Override
    public List<SegmentData> findMatchingSegmentDatasByLocation(List<SegmentData> segmentDatasToMatch, Study study,
            Platform platform) {
        return null;
    }

    @Override
    public List<Gene> findGenesByLocation(String chromosome, Integer startPosition, Integer endPosition,
            GenomeBuildVersionEnum genomeBuildVersion) {
        return new ArrayList<Gene>();
    }

    @Override
    public boolean isGenomeVersionMapped(GenomeBuildVersionEnum genomeVersion) {
        return true;
    }


    @Override
    public void saveSubjectSourceStatus(AbstractClinicalSourceConfiguration source) {

    }

    @Override
    public void clearSession() {

    }

    @Override
    public GeneLocationConfiguration getGeneLocationConfiguration(GenomeBuildVersionEnum genomeVersion) {
        return null;
    }

    @Override
    public Set<GisticAnalysis> getGisticAnalysisUsingGenomicSource(GenomicDataSourceConfiguration genomicSource) {
        Set<GisticAnalysis> gisticAnalysisSet = new HashSet<GisticAnalysis>();
        gisticAnalysisSet.add(new GisticAnalysis());
        return gisticAnalysisSet;
    }

    @Override
    public void runSessionKeepAlive() {

    }

    @Override
    public List<StudyConfiguration> getStudyConfigurationsWhichNeedThisPlatform(Platform platform) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GenomicDataSourceConfiguration> getAllGenomicDataSources() {
        return new ArrayList<GenomicDataSourceConfiguration>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markStudiesAsNeedingRefresh() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuthorizedStudyElementsGroup> getAuthorizedStudyElementGroups(String username, Long id) {
        List<AuthorizedStudyElementsGroup> authorizedStudyElementsGroup = new ArrayList<AuthorizedStudyElementsGroup>();

        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration.setExperimentIdentifier(EXP_ID);
        studyConfiguration.getGenomicDataSources().add(genomicDataSourceConfiguration);
        study.setStudyConfiguration(studyConfiguration);
        study.setLongTitleText("test");
        authorizedStudyElementsGroup.add(createAuthorizedStudyElementsGroup(study.getStudyConfiguration(), "IntegrationTestAuthorizedStudyElementsGroup1","Gender","F"));
        return authorizedStudyElementsGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnotationFieldDescriptor> getAuthorizedAnnotationFieldDescriptors(String username,
                                                                                StudyConfiguration studyConfiguration) {

        Long studyConfigurationId = studyConfiguration.getId();
        List<AuthorizedStudyElementsGroup> list = new ArrayList<AuthorizedStudyElementsGroup>();
        List<AnnotationFieldDescriptor> listAfd = new ArrayList<AnnotationFieldDescriptor>();
        list = getAuthorizedStudyElementGroups(username, studyConfigurationId);
        for (AuthorizedStudyElementsGroup aseg : list) {
            for (AuthorizedAnnotationFieldDescriptor aafd : aseg.getAuthorizedAnnotationFieldDescriptors()) {
                listAfd.add(aafd.getAnnotationFieldDescriptor());
            }
        }
        return listAfd;
    }

    /**
     * This method creates and returns an AuthorizedStudyElementsGroup that
     * consists of elements from the current studyConfiguration.
     * @param studyConfiguration
     * @param authorizedStudyElementsGroupName
     * @param fieldDescriptorName
     * @param annotationValue
     * @return authorizedStudyElementsGroup
     */
    protected AuthorizedStudyElementsGroup createAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
                                                                                String authorizedStudyElementsGroupName,
                                                                                String fieldDescriptorName,
                                                                                String annotationValue) {
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup = new AuthorizedStudyElementsGroup();
        authorizedStudyElementsGroup.setStudyConfiguration(studyConfiguration);
        String desc = "Created by integration test for study named: " + studyConfiguration.getStudy().getShortTitleText();
        Group group = new Group();
        group.setGroupName(authorizedStudyElementsGroupName);
        group.setGroupDesc(desc);

        authorizedStudyElementsGroup.setAuthorizedGroup(group);
        // add AuthorizedAnnotationFieldDescriptor
        AnnotationFieldDescriptor annotationFieldDescriptor = new AnnotationFieldDescriptor();
        annotationFieldDescriptor = getAfd(fieldDescriptorName, AnnotationFieldType.ANNOTATION);
        AuthorizedAnnotationFieldDescriptor authorizedAnnotationFieldDescriptor = new AuthorizedAnnotationFieldDescriptor();
        authorizedAnnotationFieldDescriptor.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedAnnotationFieldDescriptor.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        authorizedStudyElementsGroup.getAuthorizedAnnotationFieldDescriptors().add(authorizedAnnotationFieldDescriptor);
        // add AuthorizedGenomicDataSourceConfigurations
        AuthorizedGenomicDataSourceConfiguration authorizedGenomicDataSourceConfiguration = new AuthorizedGenomicDataSourceConfiguration();
        authorizedGenomicDataSourceConfiguration.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedGenomicDataSourceConfiguration.setGenomicDataSourceConfiguration(studyConfiguration.getGenomicDataSources().get(0));
        authorizedStudyElementsGroup.getAuthorizedGenomicDataSourceConfigurations().add(authorizedGenomicDataSourceConfiguration);
        // add AuthorizedQuery
        Query query = new Query();
        query.setName("TestAuthorizationQuery");
        query.setDescription(desc);

        for (StudySubscription studySubscription : getWorkspace(USER_EXISTS).getSubscriptionCollection()) {
            if (studySubscription.getStudy().getId().equals(studyConfiguration.getStudy().getId())) {
                query.setSubscription(studySubscription);
            }
        }

        query.setLastModifiedDate(new Date());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        StringComparisonCriterion stringComparisonCriterion = new StringComparisonCriterion();
        stringComparisonCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
        stringComparisonCriterion.setStringValue(annotationValue);
        stringComparisonCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        AbstractCriterion abstractCriterion = stringComparisonCriterion;
        HashSet<AbstractCriterion> abstractCriterionCollection = new HashSet<AbstractCriterion>();
        abstractCriterionCollection.add(abstractCriterion);
        query.getCompoundCriterion().setCriterionCollection(abstractCriterionCollection);
        AuthorizedQuery authorizedQuery = new AuthorizedQuery();
        authorizedQuery.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedQuery.setQuery(query);
        authorizedStudyElementsGroup.getAuthorizedQuerys().add(authorizedQuery);

        return authorizedStudyElementsGroup;
    }

    /**
     * @param fieldDescriptorName
     * @param afdType
     * @return afd
     */
    private AnnotationFieldDescriptor getAfd(String fieldDescriptorName, AnnotationFieldType afdType) {
        AnnotationDefinition ad = getAd(fieldDescriptorName, AnnotationTypeEnum.STRING);
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        afd.setName(fieldDescriptorName);
        afd.setType(afdType);
        afd.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        afd.setDefinition(ad);
        return afd;
    }

    /**
     * Returns a new AnnotationDefinition.
     * @param adName the display name that will be given to the annotation definition.
     * @param adDataType the DataType that will be given to the new
     * @return annotationDefinition
     */
    private AnnotationDefinition getAd(String adName, AnnotationTypeEnum adDataType) {
        AnnotationDefinition ad = new AnnotationDefinition();
        ad.setDisplayName(adName);
        ad.setDataType(adDataType);
        return ad;
    }
}
