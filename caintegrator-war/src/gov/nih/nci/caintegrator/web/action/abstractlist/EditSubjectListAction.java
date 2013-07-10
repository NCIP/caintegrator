/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.abstractlist;

import gov.nih.nci.caintegrator.domain.application.AbstractList;
import gov.nih.nci.caintegrator.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator.domain.application.SubjectList;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Provides functionality to list and add array designs.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class EditSubjectListAction extends AbstractEditListAction {

    private static final long serialVersionUID = 1L;

    @Override
    AbstractList getAbstractList(String name, boolean isGlobal) {
        return (isGlobal)
            ? getStudy().getStudyConfiguration().getSubjectList(name)
            : getStudySubscription().getSubjectList(name);
    }

    @Override
    void setOpenList(String name) {
        setOpenSubjectListName(name);
        setOpenGlobalSubjectListName(null);
    }

    @Override
    void setOpenGlobalList(String name) {
        setOpenGlobalSubjectListName(name);
        setOpenSubjectListName(null);
    }

    /**
     * @return the subject identifier listing
     */
    public String getSubjectIdentifierListing() {
        StringBuffer listing = new StringBuffer();
        for (SubjectIdentifier subjectIdentifier
                : ((SubjectList) getAbstractList()).getSubjectIdentifiers()) {
            if (listing.length() > 0) {
                listing.append('\n');
            }
            listing.append(subjectIdentifier.getIdentifier());
        }
        return listing.toString();
    }
}
