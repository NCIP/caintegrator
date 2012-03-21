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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AuthorizedStudyElementsGroupTest {

    @Test
    public void testGetAuthorizedStudyElementsGroup() {
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup = new AuthorizedStudyElementsGroup();
        String groupName = "Group 1";
        String groupDescription = "This is a test group for unit test";
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        Long studyConfigId = 12345L;
        studyConfiguration.setId(studyConfigId);

        authorizedStudyElementsGroup.setGroupName(groupName);
        authorizedStudyElementsGroup.setGroupDescription(groupDescription);
        authorizedStudyElementsGroup.setStudyConfiguration(studyConfiguration);
        
        assertEquals(groupName,authorizedStudyElementsGroup.getGroupName());
        assertEquals(groupDescription,authorizedStudyElementsGroup.getGroupDescription());
        assertEquals(studyConfigId,authorizedStudyElementsGroup.getStudyConfiguration().getId());

    }
    
    @Test
    public void testAddAuthorizedStudyElementsGroups() {
        //create StudyConfiguration
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        Study study = new Study();
        String studyName = "Test Study Name for Authorized Elements Group Testing";
        study.setShortTitleText(studyName);
        studyConfiguration.setStudy(study);
        
        //create first AuthorizedStudyElementsGroup
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup1 = new AuthorizedStudyElementsGroup();
        String group1Name = "Group 1";
        String group1Description = "This is test group 1 for unit test";
        authorizedStudyElementsGroup1.setGroupName(group1Name);
        authorizedStudyElementsGroup1.setGroupDescription(group1Description);
        // create authorizedannotationfieldescriptor
        AuthorizedAnnotationFieldDescriptor authorizedAnnotationFieldDescriptor1 = new AuthorizedAnnotationFieldDescriptor();
        AnnotationFieldDescriptor annotationFieldDescriptor1 = new AnnotationFieldDescriptor();
        String afd1Name = "AFD Number 1";
        annotationFieldDescriptor1.setName(afd1Name);
        authorizedAnnotationFieldDescriptor1.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup1);
        authorizedAnnotationFieldDescriptor1.setAnnotationFieldDescriptor(annotationFieldDescriptor1);
        List<AuthorizedAnnotationFieldDescriptor> authorizedAnnotationFieldDescriptorList1 =
            new ArrayList<AuthorizedAnnotationFieldDescriptor>();
        authorizedAnnotationFieldDescriptorList1.add(authorizedAnnotationFieldDescriptor1);
        authorizedStudyElementsGroup1.setAuthorizedAnnotationFieldDescriptors(authorizedAnnotationFieldDescriptorList1);
        authorizedStudyElementsGroup1.getAuthorizedAnnotationFieldDescriptors().add(authorizedAnnotationFieldDescriptor1);
        // create first authorizedabstractcriterion
        AbstractCriterion abstractCriterion1 = (AbstractCriterion) new AbstractAnnotationCriterion();
        AuthorizedAbstractCriterion authorizedAbstractCriterion1 = new AuthorizedAbstractCriterion();
        authorizedAbstractCriterion1.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup1);
        authorizedAbstractCriterion1.setAbstractCriterion(abstractCriterion1);
        List<AuthorizedAbstractCriterion> authorizedAbstractCriterionList1 =
            new ArrayList<AuthorizedAbstractCriterion>();
        authorizedAbstractCriterionList1.add(authorizedAbstractCriterion1);
        authorizedStudyElementsGroup1.setAuthorizedAbstractCriterions(authorizedAbstractCriterionList1);
        authorizedStudyElementsGroup1.getAuthorizedAbstractCriterions().add(authorizedAbstractCriterion1);
        // create first authorizedgenomicdatasource
        AuthorizedGenomicDataSourceConfiguration authorizedGenomicDataSourceConfiguration1 =
                                                    new AuthorizedGenomicDataSourceConfiguration();
        GenomicDataSourceConfiguration genomicDataSourceConfiguration1 = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration1.setStudyConfiguration(studyConfiguration);
        authorizedGenomicDataSourceConfiguration1.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup1);
        authorizedGenomicDataSourceConfiguration1.setGenomicDataSourceConfiguration(genomicDataSourceConfiguration1);
        List<AuthorizedGenomicDataSourceConfiguration> authorizedGenomicDataSourceConfigurationsList1 =
            new ArrayList<AuthorizedGenomicDataSourceConfiguration>();
        authorizedStudyElementsGroup1.setAuthorizedGenomicDataSourceConfigurations(authorizedGenomicDataSourceConfigurationsList1);
        authorizedStudyElementsGroup1.getAuthorizedGenomicDataSourceConfigurations().add(authorizedGenomicDataSourceConfiguration1);
        
        // create second AuthorizedStudyElementsGroup
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup2 = new AuthorizedStudyElementsGroup();        
        String group2Name = "Group 2";
        String group2Description = "This is test group 2 for unit test";        
        authorizedStudyElementsGroup2.setGroupName(group2Name);
        authorizedStudyElementsGroup2.setGroupDescription(group2Description);
        AuthorizedAnnotationFieldDescriptor authorizedAnnotationFieldDescriptor2 = new AuthorizedAnnotationFieldDescriptor();
        AnnotationFieldDescriptor annotationFieldDescriptor2 = new AnnotationFieldDescriptor();
        String afd2Name = "AFD Number 2";
        annotationFieldDescriptor2.setName(afd2Name);
        authorizedAnnotationFieldDescriptor2.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup2);
        authorizedAnnotationFieldDescriptor2.setAnnotationFieldDescriptor(annotationFieldDescriptor2);
        List<AuthorizedAnnotationFieldDescriptor> authorizedAnnotationFieldDescriptorList2 =
            new ArrayList<AuthorizedAnnotationFieldDescriptor>();
        authorizedAnnotationFieldDescriptorList2.add(authorizedAnnotationFieldDescriptor2);
        authorizedStudyElementsGroup2.setAuthorizedAnnotationFieldDescriptors(authorizedAnnotationFieldDescriptorList2);
        authorizedStudyElementsGroup2.getAuthorizedAnnotationFieldDescriptors().add(authorizedAnnotationFieldDescriptor2);
        // create second authorizedabstractcriterion
        AbstractCriterion abstractCriterion2 = (AbstractCriterion) new AbstractAnnotationCriterion();
        AuthorizedAbstractCriterion authorizedAbstractCriterion2 = new AuthorizedAbstractCriterion();
        authorizedAbstractCriterion2.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup2);
        authorizedAbstractCriterion2.setAbstractCriterion(abstractCriterion2);
        List<AuthorizedAbstractCriterion> authorizedAbstractCriterionList2 =
            new ArrayList<AuthorizedAbstractCriterion>();
        authorizedAbstractCriterionList2.add(authorizedAbstractCriterion2);
        authorizedStudyElementsGroup2.setAuthorizedAbstractCriterions(authorizedAbstractCriterionList2);
        authorizedStudyElementsGroup2.getAuthorizedAbstractCriterions().add(authorizedAbstractCriterion2);        

        // create groups list and add them
        List<AuthorizedStudyElementsGroup> authorizedStudyElementsGroups =
            new ArrayList<AuthorizedStudyElementsGroup>();
        
        // the authorizedStudyElementsGroup is ready, now add it to the list in the studyConfiguration
        studyConfiguration.setAuthorizedStudyElementsGroups(authorizedStudyElementsGroups);
        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementsGroup1);       
        
        // test various connections in the authorizedStudyElementsGroup structure
        assertEquals(group1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getGroupName());
        assertEquals(group1Description,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getGroupDescription());
        assertEquals(afd1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedAnnotationFieldDescriptors().get(0).getAnnotationFieldDescriptor().getName());
        assertEquals(group1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedAnnotationFieldDescriptors().get(0).getAuthorizedStudyElementsGroup().getGroupName());
        assertEquals(group1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedAbstractCriterions().get(0).getAuthorizedStudyElementsGroup().getGroupName());
        assertEquals(group1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedGenomicDataSourceConfigurations().get(0).getAuthorizedStudyElementsGroup().getGroupName());
        assertEquals(studyName,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedGenomicDataSourceConfigurations().get(0).getGenomicDataSourceConfiguration().getStudyConfiguration().getStudy().getShortTitleText());
        
        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementsGroup2);
        assertEquals(group2Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(1).getGroupName());
        assertEquals(group2Description,studyConfiguration.getAuthorizedStudyElementsGroups().get(1).getGroupDescription());
         
    }    
}
