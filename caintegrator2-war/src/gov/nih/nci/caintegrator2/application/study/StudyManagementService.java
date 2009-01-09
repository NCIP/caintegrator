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

import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.external.caarray.NoSamplesForExperimentException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Service used to create, define, deploy and update studies.
 */
public interface StudyManagementService {
    
    /**
     * Saves a study.
     * 
     * @param studyConfiguration study to save
     */
    void save(StudyConfiguration studyConfiguration);
    
    /**
     * Deletes a study.
     * 
     * @param studyConfiguration study to delete
     */
    void delete(StudyConfiguration studyConfiguration);
    
    /**
     * Deletes abstractPermissibleValues.
     * 
     * @param abstractPermissibleValues to delete
     */
    void delete(Collection<AbstractPermissibleValue> abstractPermissibleValues);
    
    /**
     * Deletes a clinical source.
     * 
     * @param clinicalSource clinical source to delete
     */
    void delete(DelimitedTextClinicalSourceConfiguration clinicalSource);
    
    /**
     * Adds a clinical annotation file for use. The file given will be copied to permanent storage allowing the
     * file provided as an argument to be removed after completion of this method.
     * 
     * @param studyConfiguration add the annotation file to this study
     * @param annotationFile annotation file to add.
     * @param filename the name with which the annotation file should be stored 
     *        (allows for the use of files with temp names as input)
     * @return the clinical source configuration created.
     * @throws ValidationException if the file was not a valid annotation file.
     * @throws IOException if the annotation file couldn't be copied to permanent storage.
     */
    DelimitedTextClinicalSourceConfiguration addClinicalAnnotationFile(StudyConfiguration studyConfiguration, 
            File annotationFile, String filename) throws ValidationException, IOException;
    
    /**
     * Adds a logo to the study.
     * @param studyConfiguration add the logo to this study.
     * @param imageFile object to add.
     * @param fileName name of the file for the logo.
     * @param fileType - type of file (such as image\jpeg).
     * @throws IOException if the image file couldn't be loaded.
     */
    void addStudyLogo(StudyConfiguration studyConfiguration, 
                        File imageFile, 
                        String fileName, 
                        String fileType) throws IOException;

    /**
     * Retrieves study logo from the database given a study id and name.
     * @param studyId - ID of the Study object.
     * @param studyShortTitleText - Short Title Text of Study Object.
     * @return Object retrieved from database.
     */
    StudyLogo retrieveStudyLogo(Long studyId, String studyShortTitleText);
    
    /**
     * Loads clinical annotations given a study configuration.
     * 
     * @param studyConfiguration study configuration to load
     * @throws ValidationException fail to load
     */
    void loadClinicalAnnotation(StudyConfiguration studyConfiguration) throws ValidationException;

    /**
     * Deploys or redeploys a study.
     * 
     * @param studyConfiguration the study configuration to deploy
     * @throws ConnectionException if underlying data sources couldn't be reached
     * @throws DataRetrievalException if external data couldn't be retrieved
     */
    void deployStudy(StudyConfiguration studyConfiguration) throws ConnectionException, DataRetrievalException;

    /**
     * Adds a new, initialized genomic data source to the study. Samples related to this data source are
     * retrieved from the source and added to the study.
     * 
     * @param studyConfiguration study configuration to add genomic data source to
     * @param genomicSource genomic source to add
     * @throws ConnectionException if the configured server couldn't be reached.
     * @throws ExperimentNotFoundException if the experiment cannot be found.
     * @throws NoSamplesForExperimentException if there are no samples for a valid experiment.
     */
    void addGenomicSource(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) 
    throws ConnectionException, ExperimentNotFoundException, NoSamplesForExperimentException;

    /**
     * Adds a new, initialized image data source to the study. The <code>ImageSeriesAcquisition</code> related to this 
     * data source is retrieved from the source and added to the study.
     * 
     * @param studyConfiguration study configuration to add image data source to
     * @param imageSource image source to add
     * @throws ConnectionException if the configured server couldn't be reached.
     */
    void addImageSource(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource) 
    throws ConnectionException;
    
    /**
     * Adds an image series annotation file to the study. The file given will be copied to permanent storage 
     * allowing the file provided as an argument to be removed after completion of this method.
     * 
     * @param studyConfiguration add the annotation file to this study
     * @param annotationFile annotation file to add.
     * @param filename the name with which the annotation file should be stored 
     *        (allows for the use of files with temp names as input)
     * @return the clinical source configuration created.
     * @throws ValidationException if the file was not a valid annotation file.
     * @throws IOException if the annotation file couldn't be copied to permanent storage.
     */
    ImageAnnotationConfiguration addImageAnnotationFile(StudyConfiguration studyConfiguration, 
            File annotationFile, String filename) throws ValidationException, IOException;

    /**
     * Loads image annotations given a study configuration.
     * 
     * @param studyConfiguration study configuration to load
     * @throws ValidationException fail to load
     */
    void loadImageAnnotation(StudyConfiguration studyConfiguration) throws ValidationException;

    /**
     * Returns the studies managed by the Study Manager indicated by username.
     * 
     * @param username get studies managed by this user
     * @return the list of managed studies.
     */
    List<StudyConfiguration> getManagedStudies(String username);

    /**
     * Returns the refreshed entity attached to the current Hibernate session.
     * 
     * @param <T> type of object being returned.
     * @param entity a persistent entity with the id set.
     * @return the refreshed entity.
     */
    <T> T getRefreshedStudyEntity(T entity);

    /**
     * Returns an ordered list of existing definitions that match the keywords contained
     * in the given column.
     * 
     * @param keywords match definitions for these keywords.
     * @return the list of matching candidate definitions.
     */
    List<AnnotationDefinition> getMatchingDefinitions(List<String> keywords);

    /**
     * Returns an ordered list of existing CaDSR data elements that match the keywords contained
     * in the given column.
     * 
     * @param keywords match data elements for these keywords.
     * @return the list of matching candidate data elements.
     */
    List<CommonDataElement> getMatchingDataElements(List<String> keywords);

    /**
     * Selects an existing CaDSR data element as the definition for a column.

     * @param fileColumn column receiving definition.
     * @param dataElement the selected data element.
     * @param study the study that the FileColumn belongs to.
     * @param entityType the entityType for the data element.
     * @throws ConnectionException if underlying data sources couldn't be reached.
     * @throws ValidationException if the data element selected is invalid for this definition.
     */
    void setDataElement(FileColumn fileColumn, CommonDataElement dataElement, Study study, EntityTypeEnum entityType)
    throws ConnectionException, ValidationException;

    /**
     * Selects an existing annotation definition for a column.
     * 
     * @param study is the study that the definition is getting set for.
     * @param fileColumn column receiving definition.
     * @param annotationDefinition the selected definition.
     * @param entityType entity type for the annotation definition.
     */
    void setDefinition(Study study, FileColumn fileColumn, AnnotationDefinition annotationDefinition, 
            EntityTypeEnum entityType);

    /**
     * Create the associations between subjects in the study and samples.
     * 
     * @param studyConfiguration study containing the subjects and samples
     * @param mappingFile comma-separated value file that maps subject identifiers to sample names
     * @throws ValidationException if the file was not a valid mapping file.
     * @throws IOException unexpected IO exception
     * 
     */
    void mapSamples(StudyConfiguration studyConfiguration, File mappingFile)
        throws ValidationException, IOException;

    /**
     * Create the associations between subjects in the study and image series.
     * 
     * @param studyConfiguration study containing the subjects and image series
     * @param mappingFile comma-separated value file that maps subject identifiers to image series identifiers
     * @throws IOException unexpected IO exception
     * @throws ValidationException validation exception
     */
    void mapImageSeriesAcquisitions(StudyConfiguration studyConfiguration, File mappingFile)
        throws ValidationException, IOException;
    
    /**
     * Creates a new AnnotationDefinition based on an AnnotationFieldDescriptor.
     * @param descriptor annotation descriptor to use.
     * @param study object to correlate the newly created definition to.
     * @param entityType entity type for the annotation definition.
     * @return The annotation definition that was created.
     */
    AnnotationDefinition createDefinition(AnnotationFieldDescriptor descriptor, Study study, EntityTypeEnum entityType);

    /**
     * Adds the samples specified by identifier in the file to the set of control samples in the
     * study.
     * 
     * @param studyConfiguration add controls for this study
     * @param controlSampleFile file containing the sample identifiers, one per line
     * @throws ValidationException if the file is invalid.
     * @throws IOException unexpected IO exception
     */
    void addControlSamples(StudyConfiguration studyConfiguration, File controlSampleFile)
        throws ValidationException, IOException;

    /**
     * Check for duplicate study name in the database.
     * 
     * @param study
     *            the study object
     * @return true or false
     */
    boolean isDuplicateStudyName(Study study);

    /**
     * Creates a new SurvivalValueDefinition for the study and returns.
     * @param study - Study to create SurvivalValueDefinition for.
     * @return newly created object.
     */
    SurvivalValueDefinition createNewSurvivalValueDefinition(Study study);

    /**
     * Removes a survivalValueDefinition from a study and deletes the object.
     * @param study - Study to remove from.
     * @param survivalValueDefinition - Object to remove from study.
     */
    void removeSurvivalValueDefinition(Study study, SurvivalValueDefinition survivalValueDefinition);
    
    /**
     * Retrieves the image data source for a given study configuration. (If there's more than one,
     * it takes the first one found).
     * @param study to use for data source.
     * @return image data source configuration for the study.
     */
    ImageDataSourceConfiguration retrieveImageDataSource(Study study);

}
