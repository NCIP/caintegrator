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
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public final class CaIntegrator2DaoTestIntegration extends AbstractTransactionalSpringContextTests {
    
    private CaIntegrator2Dao caIntegrator2Dao;
    private SessionFactory sessionFactory;
    
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/dao-test-config.xml"};
    }

    @Test
    public void testGetWorkspace() {
        UserWorkspace workspace = new UserWorkspace();
        caIntegrator2Dao.save(workspace);

        UserWorkspace workspace2 = this.caIntegrator2Dao.getWorkspace("Anything.");
        assertEquals(workspace.getId(), workspace2.getId());
        
    }

    @Test
    public void testSave() {
        StudyConfiguration studyConfiguration1 = new StudyManagementServiceStub().createStudy();  
        Study study1 = studyConfiguration1.getStudy();
        study1.setLongTitleText("longTitleText");
        study1.setShortTitleText("shortTitleText");
        assertNull(studyConfiguration1.getId());
        assertNull(study1.getId());
        caIntegrator2Dao.save(studyConfiguration1);
        assertNotNull(studyConfiguration1.getId());
        assertNotNull(study1.getId());
        
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        StudyConfiguration studyConfiguration2 = caIntegrator2Dao.get(studyConfiguration1.getId(), StudyConfiguration.class);
        Study study2 = studyConfiguration2.getStudy();
        
        assertEquals(study1.getShortTitleText(), study2.getShortTitleText());
        assertEquals(study1.getLongTitleText(), study2.getLongTitleText());
        assertEquals(study1, study2);
        assertEquals(studyConfiguration1, studyConfiguration2);
    }
    
    @Test 
    @SuppressWarnings({"PMD.ExcessiveMethodLength"})
    public void testFindMatches() {
        // First load 2 AnnotationFieldDescriptors.
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        afd.setKeywords("congestive heart failure");
        afd.setName("Congestive Heart Failure");
        afd.setType(AnnotationFieldType.CHOICE);
        caIntegrator2Dao.save(afd);
        
        AnnotationFieldDescriptor afd2 = new AnnotationFieldDescriptor();
        afd2.setKeywords("congestive");
        afd2.setName("Congestive");
        afd2.setType(AnnotationFieldType.CHOICE);
        caIntegrator2Dao.save(afd2);
        
        AnnotationFieldDescriptor afd3 = new AnnotationFieldDescriptor();
        afd3.setKeywords("congestive failure");
        afd3.setName("Congestive Failure");
        afd3.setType(AnnotationFieldType.CHOICE);
        caIntegrator2Dao.save(afd3);
        
        // Now search for our item on the string "congestive"
        List<String> searchWords = new ArrayList<String>();
        searchWords.add("CoNgeStiVe");
        searchWords.add("HearT");
        searchWords.add("failure");
        List<AnnotationFieldDescriptor> afds1 = caIntegrator2Dao.findMatches(searchWords);
        
        assertNotNull(afds1);
        // Make sure it sorted them properly.
        assertEquals(afds1.get(0).getName(), "Congestive Heart Failure");
        assertEquals(afds1.get(1).getName(), "Congestive Failure");
        assertEquals(afds1.get(2).getName(), "Congestive");
        
        List<String> searchWords2 = new ArrayList<String>();
        searchWords2.add("afdsefda");
        List<AnnotationFieldDescriptor> afds2 = caIntegrator2Dao.findMatches(searchWords2);
        assertEquals(0, afds2.size());
    }

    /**
     * @param caIntegrator2Dao the caIntegrator2Dao to set
     */
    public void setCaIntegrator2Dao(CaIntegrator2Dao caIntegrator2Dao) {
        this.caIntegrator2Dao = caIntegrator2Dao;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
