/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AbstractParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisMethodInvocation;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
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

import org.apache.commons.lang.StringUtils;

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
    private final Map<String, AnnotationDefinition> nameToClassificationAnnotationMap = 
        new HashMap<String, AnnotationDefinition>();
    private final Map<String, EntityTypeEnum> nameToEntityTypeMap = 
        new HashMap<String, EntityTypeEnum>();
    
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
        nameToEntityTypeMap.clear();
    }

    void addClassificationAnnotations(Collection<AnnotationDefinition> classificationAnnotations, 
            EntityTypeEnum entityType) {
        for (AnnotationDefinition definition : classificationAnnotations) {
            classificationAnnotationNames.add(definition.getDisplayName());
            nameToClassificationAnnotationMap.put(definition.getDisplayName(), definition);
            nameToEntityTypeMap.put(definition.getDisplayName(), entityType);
        }
        Collections.sort(classificationAnnotationNames);
    }

    /**
     * @return the classificationAnnotationNames
     */
    public List<String> getClassificationAnnotationNames() {
        return classificationAnnotationNames;
    }

    AnnotationDefinition getClassificationAnnotation(String name) {
        return nameToClassificationAnnotationMap.get(name);
    }

    EntityTypeEnum getEntityType(String name) {
        return nameToEntityTypeMap.get(name);
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

}
