/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.common.HibernateUtil;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains an ordered list of definition names and allows retrieval of the associated definition by name.
 */
class AnnotationFieldDescriptorList {
    
    private final List<String> names = new ArrayList<String>();
    private final Map<String, AnnotationFieldDescriptor> nameToDefinitionMap = 
        new HashMap<String, AnnotationFieldDescriptor>();
    
    AnnotationFieldDescriptorList(Collection<AnnotationFieldDescriptor> annotations) {
        if (annotations == null) {
            throw new IllegalArgumentException("Argument definitions was null.");
        }
        for (AnnotationFieldDescriptor annotation : annotations) {
            AnnotationDefinition definition = annotation.getDefinition();
            if (definition != null) { 
                names.add(definition.getDisplayName());
                nameToDefinitionMap.put(definition.getDisplayName(), annotation);
                HibernateUtil.loadCollection(definition.getPermissibleValueCollection());
            }
        }
        Collections.sort(names);
    }

    List<String> getNames() {
        return names;
    }
    
    AnnotationFieldDescriptor getDefinition(String name) {
        return nameToDefinitionMap.get(name);
    }

}
