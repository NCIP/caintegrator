/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.web.action.AbstractDeployedStudyAction;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * View data dictionary action.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ViewDataDictionaryAction extends AbstractDeployedStudyAction {

    private static final long serialVersionUID = 1L;

}
