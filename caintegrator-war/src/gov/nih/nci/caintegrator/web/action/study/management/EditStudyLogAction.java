/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.study.LogEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.interceptor.ParameterNameAware;

/**
 * Action called to edit an existing clinical data source.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class EditStudyLogAction extends AbstractStudyAction implements ParameterNameAware {

    private static final long serialVersionUID = 1L;
    private List<DisplayableLogEntry> displayableLogEntries = new ArrayList<DisplayableLogEntry>();
    private boolean readOnly = false;

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
        for (LogEntry logEntry : getStudyConfiguration().getLogEntries()) {
            displayableLogEntries.add(new DisplayableLogEntry(
                    getStudyManagementService().getRefreshedEntity(logEntry)));
        }
        Collections.sort(displayableLogEntries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        return SUCCESS;
    }

    /**
     * Save action.
     * @return struts value.
     */
    public String save() {
        for (DisplayableLogEntry displayableLogEntry : displayableLogEntries) {
            if (displayableLogEntry.isUpdateDescription()) {
                displayableLogEntry.getLogEntry().setTrimDescription(displayableLogEntry.getDescription());
            }
        }
        cleanStudyName();
        getStudyManagementService().save(getStudyConfiguration());
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

    /**
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
