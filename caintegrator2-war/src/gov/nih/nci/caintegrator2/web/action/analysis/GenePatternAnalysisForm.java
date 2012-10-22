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
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AbstractParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisMethodInvocation;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Used for Struts representation of the currently configured analysis method.
 */
public class GenePatternAnalysisForm {

    private List<AnalysisMethod> analysisMethods = new ArrayList<AnalysisMethod>();
    private final List<String> analysisMethodNames = new ArrayList<String>();
    private AnalysisMethodInvocation invocation;
    private final List<AbstractAnalysisFormParameter> parameters = new ArrayList<AbstractAnalysisFormParameter>();
    private final List<String> genomicQueryNames = new ArrayList<String>();
    private final Map<String, Query> nameToGenomicQueryMap = new HashMap<String, Query>();
    private final ServerConnectionProfile server = new ServerConnectionProfile();
    private final List<String> classificationAnnotationNames = new ArrayList<String>();
    private final Map<String, AnnotationFieldDescriptor> nameToClassificationAnnotationMap =
        new HashMap<String, AnnotationFieldDescriptor>();
    private final List<String> platformNames = new ArrayList<String>();
    private boolean multiplePlatformsInStudy = false;


    /**
     * Returns the list of all analysis method names.
     *
     * @return the list of analysis method names.
     */
    public List<String> getAnalysisMethodNames() {
        return analysisMethodNames;
    }

    /**
     * Returns the name of the currently selected <code>AnalysisMethod</code>.
     *
     * @return the name.
     */
    public String getAnalysisMethodName() {
        if (getInvocation() != null) {
            return getInvocation().getMethod().getName();
        } else {
            return null;
        }
    }

    /**
     * Sets the currently selected <code>AnalysisMethod</code> by name.
     *
     * @param name the method name
     */
    public void setAnalysisMethodName(String name) {
        if (StringUtils.isEmpty(name)) {
            handleMethodChange(null);
        } else if (!name.equals(getAnalysisMethodName())) {
            handleMethodChange(getAnalysisMethod(name));
        }
    }

    private void handleMethodChange(AnalysisMethod analysisMethod) {
        parameters.clear();
        if (analysisMethod == null) {
            setInvocation(null);
        } else {
            setInvocation(analysisMethod.createInvocation());
        }
    }

    private AnalysisMethod getAnalysisMethod(String name) {
        for (AnalysisMethod method : analysisMethods) {
            if (name.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

    /**
     * @return the invocation.
     */
    public AnalysisMethodInvocation getInvocation() {
        return invocation;
    }

    private void setInvocation(AnalysisMethodInvocation invocation) {
        this.invocation = invocation;
        if (invocation != null) {
            loadParameters();
        }
    }

    private void loadParameters() {
        for (AbstractParameterValue parameterValue : invocation.getParameterValues()) {
            parameters.add(AbstractAnalysisFormParameter.create(this, parameterValue));
        }
    }

    List<AnalysisMethod> getAnalysisMethods() {
        return analysisMethods;
    }

    void setAnalysisMethods(List<AnalysisMethod> analysisMethods) {
        if (analysisMethods == null) {
            throw new IllegalArgumentException("analysisMethods may not be null");
        }
        this.analysisMethods = analysisMethods;
        analysisMethodNames.clear();
        for (AnalysisMethod analysisMethod : analysisMethods) {
            analysisMethodNames.add(analysisMethod.getName());
        }
        if (!analysisMethodNames.isEmpty()) {
            setAnalysisMethodName(analysisMethodNames.get(0));
        } else {
            setAnalysisMethodName(null);
        }
    }

    /**
     * @return the parameters
     */
    public List<AbstractAnalysisFormParameter> getParameters() {
        return parameters;
    }

    /**
     * Indicates that the form is ready to submit to execute an analysis job.
     *
     * @return true if the form may be submitted
     */
    public boolean isExecutable() {
        return getInvocation() != null;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return server.getUrl();
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        server.setUrl(url);
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return server.getUsername();
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        server.setUsername(username);
    }

    /**
     *
     * @return the server.
     */
    public ServerConnectionProfile getServer() {
        return server;
    }

    void validate(ValidationAware action) {
        for (int i = 0; i < getParameters().size(); i++) {
            String fieldName = "analysisForm.parameters[" + i + "].value";
            getParameters().get(i).validate(fieldName, action);
        }
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return getServer().getPassword();
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        getServer().setPassword(password);
    }

    void setGenomicQueries(List<Query> genomicQueries) {
        nameToGenomicQueryMap.clear();
        genomicQueryNames.clear();
        for (Query query : genomicQueries) {
            genomicQueryNames.add(query.getName());
            nameToGenomicQueryMap.put(query.getName(), query);
        }
    }

    List<String> getGenomicQueryNames() {
        return genomicQueryNames;
    }

    Query getGenomicQuery(String name) {
        return nameToGenomicQueryMap.get(name);
    }

    void clearClassificationAnnotations() {
        classificationAnnotationNames.clear();
        nameToClassificationAnnotationMap.clear();
    }

    void addClassificationAnnotations(Collection<AnnotationFieldDescriptor> classificationAnnotations) {
        for (AnnotationFieldDescriptor descriptor : classificationAnnotations) {
            classificationAnnotationNames.add(descriptor.getDefinition().getDisplayName());
            nameToClassificationAnnotationMap.put(descriptor.getDefinition().getDisplayName(), descriptor);
        }
        Collections.sort(classificationAnnotationNames);
    }

    /**
     * @return the classificationAnnotationNames
     */
    public List<String> getClassificationAnnotationNames() {
        return classificationAnnotationNames;
    }

    AnnotationFieldDescriptor getClassificationAnnotation(String name) {
        return nameToClassificationAnnotationMap.get(name);
    }

    /**
     * Returns the URL where information on the currently selected analysis method may be found.
     *
     * @return the method information URL.
     */
    public String getAnalysisMethodInformationUrl() {
        try {
            URL serviceUrl = new URL(server.getUrl());
            URL infoUrl = new URL(serviceUrl.getProtocol(),
                    serviceUrl.getHost(),
                    serviceUrl.getPort(),
                    "/gp/getTaskDoc.jsp");
            return infoUrl.toExternalForm();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Server URL should already have been validated.", e);
        }
    }

    /**
     * @return the multiplePlatformsInStudy
     */
    public boolean isMultiplePlatformsInStudy() {
        return multiplePlatformsInStudy;
    }

    /**
     * @param multiplePlatformsInStudy the multiplePlatformsInStudy to set
     */
    public void setMultiplePlatformsInStudy(boolean multiplePlatformsInStudy) {
        this.multiplePlatformsInStudy = multiplePlatformsInStudy;
    }

    /**
     * @return the platformNames
     */
    public List<String> getPlatformNames() {
        return platformNames;
    }

}
