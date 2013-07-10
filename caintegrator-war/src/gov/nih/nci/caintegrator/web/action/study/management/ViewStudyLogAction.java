/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.study.LogEntry;
import gov.nih.nci.caintegrator.web.action.AbstractDeployedStudyAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.interceptor.ParameterNameAware;


/**
 * View study log action.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ViewStudyLogAction extends AbstractDeployedStudyAction implements ParameterNameAware {

    private static final long serialVersionUID = 1L;
    private List<DisplayableLogEntry> displayableLogEntries = new ArrayList<DisplayableLogEntry>();

    /**
     * {@inheritDoc}
     */
    public boolean acceptableParameterName(String parameterName) {
        return !(parameterName != null
                   && (parameterName.startsWith("d-")
                         || StringUtils.isNumeric(parameterName.substring(0, 1))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        displayableLogEntries.clear();
        if (getStudy() != null) {
            for (LogEntry logEntry : getStudy().getStudyConfiguration().getLogEntries()) {
                if (StringUtils.isNotBlank(logEntry.getDescription())) {
                    displayableLogEntries.add(new DisplayableLogEntry(
                    getWorkspaceService().getRefreshedEntity(logEntry)));
                }
            }
        }
        Collections.sort(displayableLogEntries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (isAnonymousUser()) {
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * @return the displayableLogEntries
     */
    public List<DisplayableLogEntry> getDisplayableLogEntries() {
        return displayableLogEntries;
    }

    /**
     * @param displayableLogEntries the displayableLogEntries to set
     */
    public void setDisplayableLogEntries(List<DisplayableLogEntry> displayableLogEntries) {
        this.displayableLogEntries = displayableLogEntries;
    }
}
