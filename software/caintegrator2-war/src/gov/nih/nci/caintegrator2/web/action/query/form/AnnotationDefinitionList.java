/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains an ordered list of definition names and allows retrieval of the associated definition by name.
 */
class AnnotationDefinitionList {
    
    private final List<String> names = new ArrayList<String>();
    private final Map<String, AnnotationDefinition> nameToDefinitionMap = new HashMap<String, AnnotationDefinition>();
    
    AnnotationDefinitionList(Collection<AnnotationDefinition> definitions, boolean addIdentifierToList) {
        if (definitions == null) {
            throw new IllegalArgumentException("Argument definitions was null.");
        }
        if (addIdentifierToList) {
            names.add(IdentifierCriterionWrapper.IDENTIFIER_FIELD_NAME);
            nameToDefinitionMap.put(IdentifierCriterionWrapper.IDENTIFIER_FIELD_NAME, null);
        }
        for (AnnotationDefinition definition : definitions) {
            names.add(definition.getDisplayName());
            nameToDefinitionMap.put(definition.getDisplayName(), definition);
            HibernateUtil.loadCollection(definition.getPermissibleValueCollection());
        }
        Collections.sort(names);
    }

    List<String> getNames() {
        return names;
    }
    
    AnnotationDefinition getDefinition(String name) {
        return nameToDefinitionMap.get(name);
    }

}
