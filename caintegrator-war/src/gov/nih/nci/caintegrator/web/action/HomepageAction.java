/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Takes the current user to the application homepage.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class HomepageAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;

    private String studynav;
    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        return SUCCESS;
    }
    /**
     * @return studyconfigurationdetails for selected option
     */
    public String getStudyDetails() {
        return SUCCESS;
    }

    /**
     * @return the studynav
     */
    public String getStudynav() {
        return studynav;
    }

    /**
     * @param studynav the studynav to set
     */
    public void setStudynav(String studynav) {
        this.studynav = studynav;
    }

}
