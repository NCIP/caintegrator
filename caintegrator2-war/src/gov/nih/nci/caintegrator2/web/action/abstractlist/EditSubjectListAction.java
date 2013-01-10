/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.abstractlist;

import gov.nih.nci.caintegrator2.domain.application.AbstractList;
import gov.nih.nci.caintegrator2.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;

/**
 * Provides functionality to list and add array designs.
 */
public class EditSubjectListAction extends EditAbstractListAction {

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
