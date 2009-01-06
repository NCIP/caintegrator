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
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("PMD")
public class StudyManagementServiceStub implements StudyManagementService {

    public boolean deployStudyCalled;
    public boolean saveCalled;
    public boolean deleteCalled;
    public boolean addGenomicSourceCalled;
    public boolean addClinicalAnnotationFileCalled;
    public boolean manageStudiesCalled;
    public boolean getRefreshedStudyEntityCalled;
    public boolean getMatchingDefinitionsCalled;
    public boolean getMatchingDataElementsCalled;
    public boolean setDataElementCalled;
    public boolean setDefinitionCalled;
    public boolean loadClinicalAnnotationCalled;
    public boolean mapSamplesCalled;
    public boolean addImageSourceCalled;
    public boolean addImageAnnotationFileCalled;
    public boolean loadImageAnnotationCalled;
    public boolean mapImageSeriesCalled;
    public boolean createDefinitionCalled;
    public boolean addControlSamplesCalled;
    public boolean isDuplicateStudyNameCalled;
    public boolean addStudyLogoCalled;
    public boolean retrieveStudyLogoCalled;
    public boolean createNewSurvivalValueDefinitionCalled;
    public boolean removeSurvivalValueDefinitionCalled;
    public boolean throwSearchError;
    public boolean retrieveImageDataSourceCalled;
    
    public void loadClinicalAnnotation(StudyConfiguration studyConfiguration) {
        loadClinicalAnnotationCalled = true;
    }

    public void save(StudyConfiguration studyConfiguration) {
        saveCalled = true;
    }

    public void delete(StudyConfiguration studyConfiguration) {
        deleteCalled = true;
    }

    public void delete(DelimitedTextClinicalSourceConfiguration clinicalSource) {
        deleteCalled = true;
    }

    public void deployStudy(StudyConfiguration studyConfiguration) {
        deployStudyCalled = true;
    }

    public void clear() {
        loadClinicalAnnotationCalled = false;
        deployStudyCalled = false;
        saveCalled = false;
        addClinicalAnnotationFileCalled = false;
        addGenomicSourceCalled = false;
        manageStudiesCalled = false;
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
        addControlSamplesCalled = false;
        isDuplicateStudyNameCalled = false;
        addStudyLogoCalled = false;
        retrieveStudyLogoCalled = false;
        createNewSurvivalValueDefinitionCalled = false;
        removeSurvivalValueDefinitionCalled = false;
        throwSearchError = false;
        retrieveImageDataSourceCalled = false;
    }

    public void addGenomicSource(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) {
        addGenomicSourceCalled = true;
        studyConfiguration.getGenomicDataSources().add(genomicSource);
    }

    public DelimitedTextClinicalSourceConfiguration addClinicalAnnotationFile(StudyConfiguration studyConfiguration,
            File annotationFile, String filename) throws ValidationException, IOException {
        if (TestDataFiles.INVALID_FILE_MISSING_VALUE.equals(annotationFile)) {
            throw new ValidationException(new ValidationResult());
        } else if (TestDataFiles.INVALID_FILE_DOESNT_EXIST.equals(annotationFile)) {
            throw new IOException();
        }
        addClinicalAnnotationFileCalled = true;
        return new DelimitedTextClinicalSourceConfiguration();
    }

    public List<StudyConfiguration> getManagedStudies(String username) {
        manageStudiesCalled = true;
        return Collections.emptyList();
    }

    public <T> T getRefreshedStudyEntity(T entity) {
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

    public void setDataElement(FileColumn fileColumn, CommonDataElement dataElement, Study study, EntityTypeEnum entityType) {
        setDataElementCalled = true;
    }

    public void setDefinition(Study study, FileColumn fileColumn, AnnotationDefinition annotationDefinition, 
                                EntityTypeEnum entityType) {
        setDefinitionCalled = true;
    }

    public void mapSamples(StudyConfiguration studyConfiguration, File mappingFile)throws ValidationException {
        mapSamplesCalled = true;
    }
    
    public ImageAnnotationConfiguration addImageAnnotationFile(StudyConfiguration studyConfiguration,
            File annotationFile, String filename) throws ValidationException, IOException {
        if (TestDataFiles.INVALID_FILE_MISSING_VALUE.equals(annotationFile)) {
            throw new ValidationException(new ValidationResult());
        } else if (TestDataFiles.INVALID_FILE_DOESNT_EXIST.equals(annotationFile)) {
            throw new IOException();
        }
        addImageAnnotationFileCalled = true;
        return new ImageAnnotationConfiguration();
    }

    public void addImageSource(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
            throws ConnectionException {
        addImageSourceCalled = true;
    }

    public void loadImageAnnotation(StudyConfiguration studyConfiguration) {
        loadImageAnnotationCalled = true;
    }

    public void mapImageSeriesAcquisitions(StudyConfiguration studyConfiguration, File mappingFile) {
        mapImageSeriesCalled = true;
    }


    public AnnotationDefinition createDefinition(AnnotationFieldDescriptor descriptor, Study study, EntityTypeEnum entityType) {
        createDefinitionCalled = true;
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void addControlSamples(StudyConfiguration studyConfiguration, File controlSampleFile)
            throws ValidationException {
        addControlSamplesCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDuplicateStudyName(Study study) {
        isDuplicateStudyNameCalled = true;
        return false;
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
}
