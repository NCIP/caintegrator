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
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotServiceStub;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.genepattern.webservice.JobInfo;

public class AnalysisServiceStub implements AnalysisService {
    
    public boolean createKMPlotCalled;
    public boolean createGEPlotCalled;
    public boolean executeGenePatternJobCalled;
    public boolean executeComparativeMarkerSelectionJobCalled;
    public boolean executePcaJobCalled;
    public boolean executeGisticJobCalled;
    public boolean deleteAnalysisJobCalled;
    public boolean deleteGisticAnalysisCalled;
    public boolean isValidGenePatternConnection = true;
    public boolean executeIGVCalled;
    public boolean executeIGVGlobalCalled;
    public boolean createIGVFileCalled;
    
    public void clear() {
        createKMPlotCalled = false;
        createGEPlotCalled = false;
        executeGenePatternJobCalled = false;
        executeComparativeMarkerSelectionJobCalled = false;
        executePcaJobCalled = false;
        executeGisticJobCalled = false;
        deleteAnalysisJobCalled = false;
        deleteGisticAnalysisCalled = false;
        executeIGVCalled = false;
        createIGVFileCalled = false;
    }

    /**
     * {@inheritDoc}
     */
    public List<AnalysisMethod> getGenePatternMethods(ServerConnectionProfile server) {
        List<AnalysisMethod> methods = new ArrayList<AnalysisMethod>();
        AnalysisMethod method = new AnalysisMethod();
        method.setName("method");
        methods.add(method);
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setName("parameter");
        parameter.setType(AnalysisParameterType.STRING);
        StringParameterValue defaultValue = new StringParameterValue();
        defaultValue.setParameter(parameter);
        defaultValue.setValue("default");
        parameter.setDefaultValue(defaultValue);
        parameter.setRequired(true);
        method.getParameters().add(parameter);
        return methods;
    }

    /**
     * {@inheritDoc}
     */
    public JobInfoWrapper executeGenePatternJob(ServerConnectionProfile server, AnalysisMethodInvocation invocation) {
        JobInfoWrapper jobInfo = new JobInfoWrapper();
        executeGenePatternJobCalled = true;
        try {
            jobInfo.setUrl(new URL("http://localhost/resultUrl"));
            jobInfo.setJobInfo(new JobInfo());
            return jobInfo;
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public KMPlot createKMPlot(StudySubscription studySubscription, AbstractKMParameters parameters) {
        createKMPlotCalled = true;
        KMPlotServiceStub kmPlotService = new KMPlotServiceStub();
        return kmPlotService.generatePlot(new KMPlotConfiguration());
    }

    
    public GeneExpressionPlotGroup createGeneExpressionPlot(StudySubscription studySubscription,
            AbstractGEPlotParameters plotParameters) {
        createGEPlotCalled = true;
        return null;
    }

    public File executeGridPreprocessComparativeMarker(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job)
            throws ConnectionException {
        executeComparativeMarkerSelectionJobCalled = true;
        return null;
    }

    public File executeGridPCA(StatusUpdateListener updater, PrincipalComponentAnalysisJob job)
            throws ConnectionException {
        executePcaJobCalled = true;
        return null;
    }

    public File executeGridGistic(StatusUpdateListener updater, GisticAnalysisJob job)
            throws ConnectionException, InvalidCriterionException {
        executeGisticJobCalled = true;
        return null;
    }

    public void deleteAnalysisJob(Long jobId) {
        deleteAnalysisJobCalled = true;
    }

    public <T> T getRefreshedEntity(T entity) {
        return entity;
    }

    public StudySubscription getRefreshedStudySubscription(StudySubscription studySubscription) {
        return studySubscription;
    }

    public List<String> validateGeneSymbols(StudySubscription studySubscription, List<String> geneSymbols)
            throws GenesNotFoundInStudyException {
        return geneSymbols;
    }

    public void deleteGisticAnalysis(GisticAnalysis gisticAnalysis) {
        deleteGisticAnalysisCalled = true;
    }

    public boolean validateGenePatternConnection(ServerConnectionProfile server) {
        return isValidGenePatternConnection;
    }

    public String executeIGV(IGVParameters igvParameters)
            throws InvalidCriterionException {
        executeIGVCalled = true;
        return null;
    }

    public void createIGVFile(StudySubscription studySubscription, Platform platform) throws InvalidCriterionException {
        createIGVFileCalled = true;
        return;
    }
}
