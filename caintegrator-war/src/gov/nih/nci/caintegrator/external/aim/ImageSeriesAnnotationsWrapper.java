/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.aim;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Wraps the Group -> Definition -> Value Maps for AIM annotations.
 */
public class ImageSeriesAnnotationsWrapper {
    private final Map<String, Map<String, String>> annotationGroupToDefinitionMap 
                                                    = new HashMap<String, Map<String, String>>();
    
    /**
     * Adds definition -> value to the annotation group map.
     * @param annotationGroupName group for annotations.
     * @param annotationDefinitionName name of annotation.
     * @param annotationValue value for annotation.
     */
    public void addDefinitionValueToGroup(String annotationGroupName, String annotationDefinitionName,
            String annotationValue) {
        if (!annotationGroupToDefinitionMap.containsKey(annotationGroupName)) {
            annotationGroupToDefinitionMap.put(annotationGroupName, new HashMap<String, String>());
        }
        annotationGroupToDefinitionMap.get(annotationGroupName).put(annotationDefinitionName, annotationValue);
    }
    
    /**
     * 
     * @return group names.
     */
    public Set<String> getAnnotationGroupNames() {
        return annotationGroupToDefinitionMap.keySet();
    }
    
    /**
     * 
     * @param annotationGroupName group name.
     * @return definitions for group.
     */
    public Set<String> getAnnotationDefinitions(String annotationGroupName) {
        checkAnnotationGroup(annotationGroupName);
        return annotationGroupToDefinitionMap.get(annotationGroupName).keySet();
    }

    /**
     * Gets annotation value.
     * @param annotationGroupName group.
     * @param annotationDefinitionName definition.
     * @return value.
     */
    public String getAnnotationValueForGroupDefinition(String annotationGroupName, String annotationDefinitionName) {
        checkAnnotationGroup(annotationGroupName);
        checkGroupDefinition(annotationGroupName, annotationDefinitionName);
        return annotationGroupToDefinitionMap.get(annotationGroupName).get(annotationDefinitionName);
    }

    private void checkGroupDefinition(String annotationGroupName, String annotationDefinitionName) {
        if (!annotationGroupToDefinitionMap.get(annotationGroupName).containsKey(annotationDefinitionName)) {
            throw new IllegalArgumentException("Unknown annotation definition: " + annotationDefinitionName);
        }
    }
    
    private void checkAnnotationGroup(String annotationGroupName) {
        if (annotationGroupToDefinitionMap.get(annotationGroupName) == null) {
            throw new IllegalArgumentException("Unknown annotation group: " + annotationGroupName);
        }
    }
    

    /**
     * @return the annotationGroupToDefinitionMap
     */
    public Map<String, Map<String, String>> getAnnotationGroupToDefinitionMap() {
        return annotationGroupToDefinitionMap;
    }
    
}
