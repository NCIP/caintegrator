/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.study.ExternalLinkList;
import gov.nih.nci.caintegrator.application.study.LogEntry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Action called to delete an externalLinkList.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class DeleteExternalLinksAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private ExternalLinkList externalLinkList = new ExternalLinkList();

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (externalLinkList.getId() != null) {
            externalLinkList = getStudyManagementService().getRefreshedEntity(externalLinkList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (StringUtils.isBlank(getExternalLinkList().getName())) {
            addActionError(getText("struts.messages.error.external.links.delete"));
            return ERROR;
        }
        setStudyLastModifiedByCurrentUser(null, LogEntry.getSystemLogDelete(externalLinkList));
        getStudyManagementService().delete(getStudyConfiguration(), getExternalLinkList());

        return SUCCESS;
    }

    /**
     * @return the externalLinkList
     */
    public ExternalLinkList getExternalLinkList() {
        return externalLinkList;
    }

    /**
     * @param externalLinkList the externalLinkList to set
     */
    public void setExternalLinkList(ExternalLinkList externalLinkList) {
        this.externalLinkList = externalLinkList;
    }
}
