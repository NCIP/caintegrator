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

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyLogo;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CaIntegrator2DaoStub implements CaIntegrator2Dao {

    public boolean getCalled;
    public boolean saveCalled;
    public boolean deleteCalled;
    public boolean getManagedStudiesCalled;
    public boolean findMatchesCalled;
    public boolean findMatchingImageSeriesCalled;
    public boolean findMatchingSamplesCalled;
    public boolean findMatchingSubjectsCalled;
    public boolean getGeneCalled;
    public boolean getPlatformCalled;
    private final StudySubjectAssignment studySubjectAssignment = new StudySubjectAssignment();
    private final Timepoint timepoint = new Timepoint();
    public boolean getArrayDataMatrixesCalled;
    public boolean getWorkspaceCalled;
    public boolean removeObjectsCalled;
    public boolean findGeneExpressionReportersCalled;
    public boolean isDuplicateStudyNameCalled;
    public boolean retrieveStudyLogoCalled;
    public boolean retrieveValueForAnnotationSubjectCalled;
    public boolean mergeCalled;
    public boolean getPlatformsCalled;
    public boolean retrieveImagingDataSourceForStudyCalled;
    public boolean setFlushModeCalled;
    public boolean refreshCalled;
    public boolean retrieveNumberImagesCalled;
    public boolean retrieveNumberImageSeriesCalled;
    public boolean retrievePlatformsForGenomicSourceCalled;
    public boolean retrieveAllSubscribedWorkspacesCalled;

    public UserWorkspace getWorkspace(String username) {
        getWorkspaceCalled = true;
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(username);
        userWorkspace.setSubscriptionCollection(new HashSet<StudySubscription>());
        return userWorkspace;
    }

    public void save(Object entity) {
        saveCalled = true;
    }
    
    public void delete(Object persistentObject) {
        deleteCalled = true;
    }
    
    public void clear() {
        getCalled = false;
        saveCalled = false;
        findMatchesCalled = false;
        getManagedStudiesCalled = false;
        findMatchingImageSeriesCalled = false;
        findMatchingSamplesCalled = false;
        findMatchingSubjectsCalled = false;
        getGeneCalled = false;
        getPlatformCalled = false;
        getArrayDataMatrixesCalled = false;
        studySubjectAssignment.setId(Long.valueOf(1));
        timepoint.setId(Long.valueOf(1));
        getWorkspaceCalled = false;
        removeObjectsCalled = false;
        findGeneExpressionReportersCalled = false;
        isDuplicateStudyNameCalled = false;
        retrieveStudyLogoCalled = false;
        retrieveValueForAnnotationSubjectCalled = false;
        mergeCalled = false;
        retrieveImagingDataSourceForStudyCalled = false;
        getPlatformsCalled = false;
        setFlushModeCalled = false;
        refreshCalled = false;
        retrieveNumberImagesCalled = false;
        retrieveNumberImageSeriesCalled = false;
        retrievePlatformsForGenomicSourceCalled = false;
        retrieveAllSubscribedWorkspacesCalled = false;
    }

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
    
    public List<AnnotationDefinition> findMatches(Collection<String> keywords) {
        findMatchesCalled = true;
        List<AnnotationDefinition> definitions = new ArrayList<AnnotationDefinition>();
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.setDisplayName("definitionName");
        definitions.add(definition);
        return definitions;
    }

    public List<StudyConfiguration> getManagedStudies(String username) {
        getManagedStudiesCalled = true;
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
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
    public List<StudySubjectAssignment> findMatchingSubjects(AbstractAnnotationCriterion criterion, Study study) {
        findMatchingSubjectsCalled = true;
        List<StudySubjectAssignment> ssaList = new ArrayList<StudySubjectAssignment>();
        ssaList.add(studySubjectAssignment);
        return ssaList;
    }

    /**
     * {@inheritDoc}
     */
    public AnnotationDefinition getAnnotationDefinition(String name) {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public Gene getGene(String symbol) {
        getGeneCalled = true;
        Gene gene = new Gene();
        gene.setSymbol(symbol);
        return gene;
    }

    /**
     * {@inheritDoc}
     */
    public Platform getPlatform(String name) {
        getPlatformCalled = true;
        Platform platform = new Platform();
        platform.setName(name);
        return platform;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked"})
    public void removeObjects(Collection objects) {
        removeObjectsCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDuplicateStudyName(Study study) {
        isDuplicateStudyNameCalled =true;
        return false;
    }
    
    public StudyLogo retrieveStudyLogo(Long id, String fileName) {
        retrieveStudyLogoCalled = true;
        return new StudyLogo();
    }

    public AbstractAnnotationValue retrieveValueForAnnotationSubject(StudySubjectAssignment subject,
            AnnotationDefinition annotationDefinition) {
        retrieveValueForAnnotationSubjectCalled = true;
        if (subject != null && annotationDefinition != null) {
            for (SubjectAnnotation subjectAnnotation : subject.getSubjectAnnotationCollection()) {
                if (subjectAnnotation.getAnnotationValue().getAnnotationDefinition().equals(annotationDefinition)) {
                    return subjectAnnotation.getAnnotationValue();
                }
            }
        }
        return null;
    }

    @SuppressWarnings({"PMD", "unchecked"})
    public <T> List<T> retrieveUniqueValuesForStudyAnnotation(Study study, AnnotationDefinition definition,
            EntityTypeEnum entityType, Class<T> objectClass) {
        List values = new ArrayList();
       for (AbstractAnnotationValue value : definition.getAnnotationValueCollection()) {
           if (value instanceof StringAnnotationValue) {
               StringAnnotationValue stringVal = (StringAnnotationValue) value;
               values.add(stringVal.getStringValue());
           } else if (value instanceof NumericAnnotationValue) {
               NumericAnnotationValue numericVal = (NumericAnnotationValue) value;
               values.add(numericVal.getNumericValue());
           } else if (value instanceof DateAnnotationValue) {
               DateAnnotationValue dateVal = (DateAnnotationValue) value;
               values.add(dateVal.getDateValue());
       }
       }
        return values;
    }

    public <T> T merge(T persistentObject) {
        mergeCalled = true;
        return persistentObject;
    }

    public ImageDataSourceConfiguration retrieveImagingDataSourceForStudy(Study study) {
        retrieveImagingDataSourceForStudyCalled = true;
        return new ImageDataSourceConfiguration();
    }

    public List<Platform> getPlatforms() {
        getPlatformsCalled = true;
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    public void setFlushMode(int mode) {
        setFlushModeCalled = true;
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbol, ReporterTypeEnum reporterType,
            Study study) {
        findGeneExpressionReportersCalled = true;
        return Collections.emptySet();
    }
    
    /**
     * {@inheritDoc}
     */
    public void refresh(Object persistentObject) {
        refreshCalled = true;
        
    }

    public int retrieveNumberImageSeries(ImageDataSourceConfiguration configuration) {
        int numberImageSeries = 0;
        for (ImageSeriesAcquisition imageAcquisition : configuration.getImageSeriesAcquisitions()) {
            numberImageSeries += imageAcquisition.getSeriesCollection().size();
        }
        retrieveNumberImageSeriesCalled = true;
        return numberImageSeries;
    }
    
    public int retrieveNumberImages(ImageDataSourceConfiguration configuration) {
        int numberImages = 0;
        for (ImageSeriesAcquisition imageAcquisition : configuration.getImageSeriesAcquisitions()) {
            numberImages += retrieveNumberImages(imageAcquisition.getSeriesCollection());
        }
        retrieveNumberImagesCalled = true;
        return numberImages;
    }
    
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

    public List<UserWorkspace> retrieveAllSubscribedWorkspaces(Study study) {
        retrieveAllSubscribedWorkspacesCalled = true;
        return new ArrayList<UserWorkspace>();
    }

}
