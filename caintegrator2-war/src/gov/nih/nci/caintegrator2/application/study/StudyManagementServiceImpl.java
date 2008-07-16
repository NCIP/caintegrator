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

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;
import gov.nih.nci.caintegrator2.external.cadsr.CaDSRFacade;
import gov.nih.nci.caintegrator2.external.cadsr.DataElement;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Entry point to the StudyManagementService subsystem.
 */
@Transactional(propagation = Propagation.REQUIRED)
@SuppressWarnings("PMD.CyclomaticComplexity")   // see configure study
public class StudyManagementServiceImpl implements StudyManagementService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(StudyManagementServiceImpl.class);
    private CaIntegrator2Dao dao;
    private FileManager fileManager;
    private CaDSRFacade caDSRFacade;
    
    /**
     * {@inheritDoc}
     */
    public StudyConfiguration createStudy() {
        StudyConfiguration configuration = new StudyConfiguration();
        configuration.getStudy().setAssignmentCollection(new HashSet<StudySubjectAssignment>());
        dao.save(configuration);
        return configuration;
    }

    /**
     * {@inheritDoc}
     */
    public void save(StudyConfiguration studyConfiguration) {
        if (isNew(studyConfiguration)) {
            configureNew(studyConfiguration);
        }
        persist(studyConfiguration);
    }

    private boolean isNew(StudyConfiguration studyConfiguration) {
        return studyConfiguration.getId() == null;
    }

    private void configureNew(StudyConfiguration studyConfiguration) {
        configureNew(studyConfiguration.getStudy());
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")   // multiple simple null checks
    private void configureNew(Study study) {
        if (study.getAssignmentCollection() == null) {
            study.setAssignmentCollection(new HashSet<StudySubjectAssignment>());
        }
        if (study.getImageSeriesAnnotationCollection() == null) {
            study.setImageSeriesAnnotationCollection(new HashSet<AnnotationDefinition>());
        }
        if (study.getSampleAnnotationCollection() == null) {
            study.setSampleAnnotationCollection(new HashSet<AnnotationDefinition>());
        }
        if (study.getSubjectAnnotationCollection() == null) {
            study.setSubjectAnnotationCollection(new HashSet<AnnotationDefinition>());
        }
        if (study.getTimepointCollection() == null) {
            study.setTimepointCollection(new HashSet<Timepoint>());
        }
    }

    /**
     * {@inheritDoc}
     */
    public DelimitedTextClinicalSourceConfiguration addClinicalAnnotationFile(StudyConfiguration studyConfiguration,
            File inputFile, String filename) throws ValidationException, IOException {
        File permanentFile = getFileManager().storeStudyFile(inputFile, filename, studyConfiguration);
        AnnotationFile annotationFile = AnnotationFile.load(permanentFile);
        DelimitedTextClinicalSourceConfiguration clinicalSourceConfig = 
            new DelimitedTextClinicalSourceConfiguration(annotationFile, studyConfiguration);
        dao.save(studyConfiguration);
        return clinicalSourceConfig;
    }

    /**
     * {@inheritDoc}
     */
    public void loadClinicalAnnotation(StudyConfiguration studyConfiguration) {
        for (AbstractClinicalSourceConfiguration configuration 
                : studyConfiguration.getClinicalConfigurationCollection()) {
            configuration.loadAnnontation();
        }
        save(studyConfiguration);
    }

    private void persist(StudyConfiguration studyConfiguration) {
        for (StudySubjectAssignment assignment : studyConfiguration.getStudy().getAssignmentCollection()) {
            save(assignment.getSubjectAnnotation());
            dao.save(assignment.getSubject());
            dao.save(assignment);
        }
        dao.save(studyConfiguration);
    }

    private void save(Collection<SubjectAnnotation> subjectAnnotations) {
        for (SubjectAnnotation annotation : subjectAnnotations) {
            dao.save(annotation.getAnnotationValue());
            dao.save(annotation);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deployStudy(StudyConfiguration studyConfiguration) {
        studyConfiguration.setStatus(Status.DEPLOYED);
        dao.save(studyConfiguration);
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public GenomicDataSourceConfiguration addGenomicSource(StudyConfiguration studyConfiguration) {
        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        studyConfiguration.getGenomicDataSources().add(genomicDataSourceConfiguration);
        dao.save(studyConfiguration);
        return genomicDataSourceConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    public List<StudyConfiguration> getManagedStudies(String username) {
        return dao.getManagedStudies(username);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> T getRefreshedStudyEntity(T entity) {
        Long id;
        try {
            id = (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Entity doesn't have a getId() method", e);
        }
        if (id == null) {
            throw new IllegalArgumentException("Id was null");
        }
        return (T) dao.get(id, entity.getClass());
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * {@inheritDoc}
     */
    public List<AnnotationDefinition> getMatchingDefinitions(FileColumn fileColumn) {
        List<AnnotationDefinition> definitions = new ArrayList<AnnotationDefinition>();
        List<AnnotationFieldDescriptor> matchingDescriptors = 
            dao.findMatches(fileColumn.getFieldDescriptor().getKeywordsAsList());
        for (AnnotationFieldDescriptor descriptor : matchingDescriptors) {
            if (descriptor.getDefinition() != null && !definitions.contains(descriptor.getDefinition())) {
                definitions.add(descriptor.getDefinition());
            }
        }
        return definitions;
    }

    /**
     * {@inheritDoc}
     */
    public List<DataElement> getMatchingDataElements(FileColumn fileColumn) {
        return caDSRFacade.retreiveCandidateDataElements(fileColumn.getFieldDescriptor().getKeywordsAsList());
    }

    /**
     * @return the caDSRFacade
     */
    public CaDSRFacade getCaDSRFacade() {
        return caDSRFacade;
    }

    /**
     * @param caDSRFacade the caDSRFacade to set
     */
    public void setCaDSRFacade(CaDSRFacade caDSRFacade) {
        this.caDSRFacade = caDSRFacade;
    }

    /**
     * {@inheritDoc}
     */
    public void setDataElement(FileColumn fileColumn, DataElement dataElement) {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDisplayName(dataElement.getLongName());
        annotationDefinition.setPreferredDefinition(dataElement.getDefinition());
        CommonDataElement cde = translate(dataElement);
        annotationDefinition.setCde(cde);
        fileColumn.getFieldDescriptor().setDefinition(annotationDefinition);
        dao.save(cde);
        dao.save(annotationDefinition);
        dao.save(fileColumn);
    }

    private CommonDataElement translate(DataElement dataElement) {
        CommonDataElement cde = new CommonDataElement();
        cde.setPublicID(dataElement.getPublicId().toString());
        return cde;
    }

    /**
     * {@inheritDoc}
     */
    public void setDefinition(FileColumn fileColumn, AnnotationDefinition annotationDefinition) {
        fileColumn.getFieldDescriptor().setDefinition(annotationDefinition);
        dao.save(fileColumn);
    }

}
