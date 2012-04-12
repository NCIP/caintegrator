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
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.cadsr.freestylesearch.util.SearchException;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSSecurityException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("PMD")
public class StudyManagementServiceStub implements StudyManagementService {

    public boolean saveCalled;
    public boolean copyCalled;
    public boolean deleteCalled;
    public boolean addGenomicSourceCalled;
    public boolean addAuthorizedStudyElementsGroupCalled;
    public boolean deleteAuthorizedStudyElementsGroupCalled;
    public boolean addClinicalAnnotationFileCalled;
    public boolean getRefreshedStudyEntityCalled;
    public boolean getMatchingDefinitionsCalled;
    public boolean getMatchingDataElementsCalled;
    public boolean setDataElementCalled;
    public boolean setDefinitionCalled;
    public boolean loadClinicalAnnotationCalled;
    public boolean reLoadClinicalAnnotationCalled;
    public boolean unLoadClinicalAnnotationCalled;
    public boolean loadAimAnnotationsCalled;
    public boolean mapSamplesCalled;
    public boolean addImageSourceCalled;
    public boolean addImageAnnotationFileCalled;
    public boolean loadImageAnnotationCalled;
    public boolean mapImageSeriesCalled;
    public boolean createDefinitionCalled;
    public boolean addControlSampleSetCalled;
    public boolean isDuplicateStudyNameCalled;
    public boolean addStudyLogoCalled;
    public boolean retrieveStudyLogoCalled;
    public boolean createNewSurvivalValueDefinitionCalled;
    public boolean removeSurvivalValueDefinitionCalled;
    public boolean throwSearchError;
    public boolean retrieveImageDataSourceCalled;
    public boolean saveDnaAnalysisMappingFileCalled;
    public boolean saveSampleMappingFileCalled;
    public boolean loadGenomicSourceCalled;
    public boolean throwConnectionException = false;
    public boolean throwValidationException = false;
    public boolean throwIOException = false;
    public boolean getRefreshedImageSourceCalled;
    public boolean addImageSourceToStudyCalled;
    public boolean loadImageSourceCalled;
    public boolean saveFileStoStudyDirectoryCalled;
    public boolean updateImageDataSourceStatusCalled;
    public boolean getRefreshedStudyConfigurationCalled;
    public boolean getRefreshedGenomicSourceCalled;
    public boolean isThrowCSException = false;
    public boolean addExternalLinksToStudyCalled;
    public boolean saveAnnotationGroupCalled = false;
    public boolean daoSaveCalled = false;
    public boolean checkForSampleUpdates = false;

    public ImageDataSourceConfiguration refreshedImageSource = new ImageDataSourceConfiguration();
    public GenomicDataSourceConfiguration refreshedGenomicSource = new GenomicDataSourceConfiguration();
    public DelimitedTextClinicalSourceConfiguration refreshedClinicalSource =
        new DelimitedTextClinicalSourceConfiguration();
    public StudyConfiguration refreshedStudyConfiguration = new StudyConfiguration();


    public DelimitedTextClinicalSourceConfiguration loadClinicalAnnotation(Long studyConfigurationId,
            Long clinicalSourceConfigurationId)
        throws ValidationException {
        loadClinicalAnnotationCalled = true;
        if (refreshedStudyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
        refreshedClinicalSource.setStatus(Status.LOADED);
        return refreshedClinicalSource;
    }

    public StudyConfiguration reLoadClinicalAnnotation(Long studyConfigurationId)
        throws ValidationException {
        reLoadClinicalAnnotationCalled = true;
        if (refreshedStudyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
        return refreshedStudyConfiguration;
    }

    public void save(StudyConfiguration studyConfiguration) {
        saveCalled = true;
    }

    public void delete(StudyConfiguration studyConfiguration) {
        deleteCalled = true;
    }

    public StudyConfiguration deleteClinicalSource(Long studyConfigurationId,
            Long clinicalSourceId) throws ValidationException {
        deleteCalled = true;
        if (refreshedStudyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
        return refreshedStudyConfiguration;
    }

    public void delete(Collection<PermissibleValue> abstractPermissibleValues) {
        deleteCalled = true;
    }

    public void clear() {
        copyCalled = false;
        loadClinicalAnnotationCalled = false;
        reLoadClinicalAnnotationCalled = false;
        unLoadClinicalAnnotationCalled = false;
        saveCalled = false;
        addClinicalAnnotationFileCalled = false;
        addGenomicSourceCalled = false;
        getRefreshedStudyEntityCalled = false;
        getMatchingDefinitionsCalled = false;
        getMatchingDataElementsCalled = false;
        setDataElementCalled = false;
        setDefinitionCalled = false;
        mapSamplesCalled = false;
        addImageAnnotationFileCalled = false;
        addImageSourceCalled = false;
        loadImageAnnotationCalled = false;
        mapImageSeriesCalled = false;
        createDefinitionCalled = false;
        addControlSampleSetCalled = false;
        isDuplicateStudyNameCalled = false;
        addStudyLogoCalled = false;
        retrieveStudyLogoCalled = false;
        createNewSurvivalValueDefinitionCalled = false;
        removeSurvivalValueDefinitionCalled = false;
        throwSearchError = false;
        saveDnaAnalysisMappingFileCalled = false;
        saveSampleMappingFileCalled = false;
        retrieveImageDataSourceCalled = false;
        loadGenomicSourceCalled = false;
        throwConnectionException = false;
        throwValidationException = false;
        throwIOException = false;
        getRefreshedImageSourceCalled = false;
        addImageSourceToStudyCalled = false;
        loadImageSourceCalled = false;
        loadAimAnnotationsCalled = false;
        saveFileStoStudyDirectoryCalled = false;
        updateImageDataSourceStatusCalled = false;
        getRefreshedStudyConfigurationCalled = false;
        getRefreshedGenomicSourceCalled = false;
        isThrowCSException = false;
        addExternalLinksToStudyCalled = false;
        saveAnnotationGroupCalled = false;
        daoSaveCalled = false;
        checkForSampleUpdates = false;
    }

    public void addGenomicSource(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) {
        addGenomicSourceCalled = true;
        studyConfiguration.getGenomicDataSources().add(genomicSource);
    }

    public DelimitedTextClinicalSourceConfiguration addClinicalAnnotationFile(StudyConfiguration studyConfiguration,
            File annotationFile, String filename, boolean createNewAnnotationDefinition)
    throws ValidationException, IOException {
        if (TestDataFiles.INVALID_FILE_MISSING_VALUE.equals(annotationFile)) {
            throw new ValidationException(new ValidationResult());
        } else if (TestDataFiles.INVALID_FILE_DOESNT_EXIST.equals(annotationFile)) {
            throw new IOException();
        }
        addClinicalAnnotationFileCalled = true;
        return new DelimitedTextClinicalSourceConfiguration();
    }

    public <T> T getRefreshedEntity(T entity) {
        getRefreshedStudyEntityCalled = true;
        return entity;
    }

    public List<AnnotationDefinition> getMatchingDefinitions(List<String> keywords) {
        getMatchingDefinitionsCalled = true;
        return Collections.emptyList();
    }

    public List<CommonDataElement> getMatchingDataElements(List<String> keywords) {
        getMatchingDataElementsCalled = true;
        if (throwSearchError) {
            throw new SearchException("Bad Search");
        }
        return Collections.emptyList();
    }

    public void setDataElement(AnnotationFieldDescriptor fieldDescriptor, CommonDataElement dataElement, Study study, EntityTypeEnum entityType, String keywords) throws ValidationException, ConnectionException {
        setDataElementCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    public void setDefinition(Study study, AnnotationFieldDescriptor fieldDescriptor, AnnotationDefinition annotationDefinition,
                                EntityTypeEnum entityType) throws ValidationException {
        setDefinitionCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    public void mapSamples(StudyConfiguration studyConfiguration, File mappingFile, GenomicDataSourceConfiguration genomicSource)throws ValidationException, IOException {
        mapSamplesCalled = true;
        if (studyConfiguration.getStudy().getShortTitleText().equals("Invalid")) {
            throw new ValidationException("Invalid");
        }
        if (studyConfiguration.getStudy().getShortTitleText().equals("IOException")) {
            throw new IOException("Invalid");
        }
    }

    public ImageAnnotationConfiguration addImageAnnotationFile(ImageDataSourceConfiguration imageDataSourceConfiguration,
            File annotationFile, String filename, boolean createNewAnnotationDefinition)
    throws ValidationException, IOException {
        if (TestDataFiles.INVALID_FILE_MISSING_VALUE.equals(annotationFile)) {
            throw new ValidationException(new ValidationResult());
        } else if (TestDataFiles.INVALID_FILE_DOESNT_EXIST.equals(annotationFile)) {
            throw new IOException();
        }
        addImageAnnotationFileCalled = true;
        ImageAnnotationConfiguration imageAnnotationConfiguration = new ImageAnnotationConfiguration();
        imageAnnotationConfiguration.setAnnotationFile(new AnnotationFileStub());
        return imageAnnotationConfiguration;
    }

    public void addImageSource(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
            throws ConnectionException {
        addImageSourceCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
    }

    public void loadImageAnnotation(ImageDataSourceConfiguration imageSource) throws ValidationException {
        loadImageAnnotationCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    public void mapImageSeriesAcquisitions(StudyConfiguration studyConfiguration,
            ImageDataSourceConfiguration imageSource, File mappingFile, ImageDataSourceMappingTypeEnum mappingType)
        throws ValidationException, IOException {
        mapImageSeriesCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
        if (throwIOException) {
            throw new IOException();
        }
    }


    public AnnotationDefinition createDefinition(AnnotationFieldDescriptor descriptor, Study study, EntityTypeEnum entityType, AnnotationTypeEnum annotationType) {
        AnnotationDefinition definition = new AnnotationDefinition();
        if (descriptor != null) {
            descriptor.setDefinition(definition);
        }
        if (annotationType != null) {
            definition.getCommonDataElement().getValueDomain().setDataType(annotationType);
        }
        createDefinitionCalled = true;
        return definition;
    }

    /**
     * {@inheritDoc}
     */
    public void addControlSampleSet(GenomicDataSourceConfiguration genomicSource,
            String controlSampleSetName, File controlSampleFile, String controlFileName)
            throws ValidationException {
        SampleSet sampleSet = new SampleSet();
        sampleSet.setName(controlSampleSetName);
        sampleSet.getSamples().add(new Sample());
        genomicSource.getControlSampleSetCollection().add(sampleSet);
        addControlSampleSetCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDuplicateStudyName(Study study, String username) {
        isDuplicateStudyNameCalled = true;
        return study.getShortTitleText().equals("Duplicate");
    }

    /**
     * {@inheritDoc}
     */
    public void addStudyLogo(StudyConfiguration studyConfiguration, File imageFile, String fileName, String fileType) throws IOException {
        addStudyLogoCalled = true;

    }

    public StudyLogo retrieveStudyLogo(Long id, String name) {
        retrieveStudyLogoCalled = true;
        return new StudyLogo();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.caintegrator2.application.study.StudyManagementService#createNewSurvivalValueDefinition(gov.nih.nci.caintegrator2.domain.translational.Study)
     */
    public SurvivalValueDefinition createNewSurvivalValueDefinition(Study study) {
        createNewSurvivalValueDefinitionCalled = true;
        return new SurvivalValueDefinition();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.caintegrator2.application.study.StudyManagementService#removeSurvivalValueDefinition(gov.nih.nci.caintegrator2.domain.translational.Study, gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition)
     */
    public void removeSurvivalValueDefinition(Study study, SurvivalValueDefinition survivalValueDefinition) {
        removeSurvivalValueDefinitionCalled = true;
    }

    public ImageDataSourceConfiguration retrieveImageDataSource(Study study) {
        retrieveImageDataSourceCalled = true;
        return new ImageDataSourceConfiguration();
    }

    /**
     * {@inheritDoc}
     */
    public void saveDnaAnalysisMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration,
            File mappingFile, String filename) {
        saveDnaAnalysisMappingFileCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public void saveSampleMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration,
            File mappingFile, String filename) throws IOException {
        saveSampleMappingFileCalled = true;
    }

    public void delete(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) {
        deleteCalled = true;
    }

    public void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException {

    }

    public void delete(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
            throws ValidationException {
        deleteCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    public void addGenomicSourceToStudy(StudyConfiguration studyConfiguration,
            GenomicDataSourceConfiguration genomicSource) {
    }

    public void loadGenomicSource(GenomicDataSourceConfiguration genomicSource) throws ConnectionException,
            ExperimentNotFoundException {
        loadGenomicSourceCalled = true;
    }

    public void addImageSourceToStudy(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource) {
        addImageSourceToStudyCalled = true;
    }

    public void loadImageSource(ImageDataSourceConfiguration imageSource) throws ConnectionException {
        loadImageSourceCalled = true;
    }

    public File saveFileToStudyDirectory(StudyConfiguration studyConfiguration, File file) throws IOException {
        saveFileStoStudyDirectoryCalled = true;
        return null;
    }

    public void updateImageDataSourceStatus(StudyConfiguration studyConfiguration) {
        updateImageDataSourceStatusCalled = true;
    }

    public ImageDataSourceConfiguration getRefreshedImageSource(Long id) {
        getRefreshedImageSourceCalled = true;
        return refreshedImageSource;
    }

    public GenomicDataSourceConfiguration getRefreshedGenomicSource(Long id) {
        getRefreshedGenomicSourceCalled = true;
        return refreshedGenomicSource;
    }

    public StudyConfiguration getRefreshedSecureStudyConfiguration(String username, Long id)
    throws CSSecurityException {
        getRefreshedStudyConfigurationCalled = true;
        if (isThrowCSException) {
            throw new CSSecurityException("invalid");
        }
        return refreshedStudyConfiguration;
    }

    public void save(AnnotationDefinition definition) throws ValidationException {

    }

    public void setStudyLastModifiedByCurrentUser(StudyConfiguration studyConfiguration, UserWorkspace lastModifiedBy, TimeStampable object, String systemLogMessage) {

    }

    public void addExternalLinksToStudy(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList)
            throws ValidationException, IOException {
        if (throwValidationException) {
            throw new ValidationException("");
        }
        if (throwIOException) {
            throw new IOException();
        }
        addExternalLinksToStudyCalled = true;
    }

    public void delete(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList) {
        deleteCalled = true;

    }

    public void saveAnnotationGroup(AnnotationGroup annotationGroup, StudyConfiguration studyConfiguration,
            File annotationGroupFile) throws ValidationException {
        if (throwValidationException) {
            throw new ValidationException("");
        }
        saveCalled = true;

    }

    public void delete(StudyConfiguration studyConfiguration, AnnotationGroup annotationGroup) {
        deleteCalled = true;
    }

    public void saveAnnotationGroup(AnnotationGroup annotationGroup, StudyConfiguration studyConfiguration,
            List<AnnotationGroupUploadContent> uploadContents) throws ValidationException, ConnectionException {
        saveAnnotationGroupCalled = true;
    }

    public AnnotationFieldDescriptor updateFieldDescriptorType(AnnotationFieldDescriptor fieldDescriptor, AnnotationFieldType type)
            throws ValidationException {
        fieldDescriptor.setType(type);
        return fieldDescriptor;
    }

    public Set<String> getAvailableValuesForFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor)
            throws ValidationException {
        return new HashSet<String>();
    }

    public void makeFieldDescriptorValid(AnnotationFieldDescriptor descriptor) {

    }

    public void daoSave(Object persistentObject) {
        daoSaveCalled = true;
    }

    public AnnotationDefinition getAnnotationDefinition(String name) {
        return null;
    }

    public AnnotationFieldDescriptor getExistingFieldDescriptorInStudy(String name,
            StudyConfiguration studyConfiguration) {
        return null;
    }

    public void unloadAllClinicalAnnotation(StudyConfiguration studyConfiguration) {
        unLoadClinicalAnnotationCalled = true;
    }

    public ImageAnnotationConfiguration addAimAnnotationSource(ServerConnectionProfile aimConnection,
            ImageDataSourceConfiguration imageSource) {
        return null;
    }

    public void loadAimAnnotations(ImageDataSourceConfiguration imageSource)
    throws ConnectionException, ValidationException {
        loadAimAnnotationsCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    public StudyConfiguration getRefreshedStudyConfiguration(Long id) {
        getRefreshedStudyConfigurationCalled = true;
        return refreshedStudyConfiguration;
    }

    public void saveSubjectSourceStatus(AbstractClinicalSourceConfiguration source) {

    }

    public StudyConfiguration copy(StudyConfiguration copyFrom, StudyConfiguration copyTo)
        throws ValidationException, IOException,
            ConnectionException {
        copyCalled = true;
        return copyTo;
    }

    @Override
    public void addAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
                                                AuthorizedStudyElementsGroup authorizedStudyElementsGroup) {
        addAuthorizedStudyElementsGroupCalled = true;
        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementsGroup);
    }

    @Override
    public void deleteAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
                                        AuthorizedStudyElementsGroup authorizedStudyElementsGroup) {
        deleteAuthorizedStudyElementsGroupCalled = true;
    }


    @Override
    public void createProtectionElement(StudyConfiguration studyConfiguration,
                                        AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException {
    }

    @Override
    public void checkForSampleUpdates(StudyConfiguration sc) throws ConnectionException, ExperimentNotFoundException {
        checkForSampleUpdates = false;
    }
}
