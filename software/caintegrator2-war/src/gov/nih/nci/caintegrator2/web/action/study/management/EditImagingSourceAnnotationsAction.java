/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.LogEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 */
public class EditImagingSourceAnnotationsAction extends AbstractImagingSourceAction {
    
    private static final long serialVersionUID = 1L;
    private List<DisplayableAnnotationFieldDescriptor> displayableFields = 
        new ArrayList<DisplayableAnnotationFieldDescriptor>();
    private final List<AnnotationGroup> selectableAnnotationGroups = new ArrayList<AnnotationGroup>();
    private final Map<String, AnnotationGroup> annotationGroupNameToGroupMap = new HashMap<String, AnnotationGroup>();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (getImageSourceConfiguration().getImageAnnotationConfiguration() != null) {
            setupAnnotationGroups();
            setupDisplayableFields();
        }
    }

    private void setupAnnotationGroups() {
        selectableAnnotationGroups.clear();
        List<AnnotationGroup> sortedAnnotationGroups = getStudy().getSortedAnnotationGroups(); 
        for (AnnotationGroup group : sortedAnnotationGroups) {
            group = getStudyManagementService().getRefreshedEntity(group);
            selectableAnnotationGroups.add(group);
            annotationGroupNameToGroupMap.put(group.getName(), group);
        }
    }

    private void setupDisplayableFields() {
        displayableFields.clear();
        for (FileColumn fileColumn : getImageSourceConfiguration().getImageAnnotationConfiguration().
                getAnnotationFile().getColumns()) {
            displayableFields.add(new DisplayableAnnotationFieldDescriptor(fileColumn));
        }
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
        for (DisplayableAnnotationFieldDescriptor displayableFieldDescriptor : displayableFields) {
            if (displayableFieldDescriptor.isGroupChanged()) {
                displayableFieldDescriptor.getFieldDescriptor().switchAnnotationGroup(
                        annotationGroupNameToGroupMap.get(displayableFieldDescriptor.getAnnotationGroupName()));
            }
        }
        setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(),
                LogEntry.getSystemLogSave(getImageSourceConfiguration()));
        getStudyManagementService().save(getStudyConfiguration());
        return SUCCESS;
    }

    /**
     * @return the displayableFields
     */
    public List<DisplayableAnnotationFieldDescriptor> getDisplayableFields() {
        return displayableFields;
    }


    /**
     * @param displayableFields the displayableFields to set
     */
    public void setDisplayableFields(List<DisplayableAnnotationFieldDescriptor> displayableFields) {
        this.displayableFields = displayableFields;
    }


    /**
     * @return the selectableAnnotationGroups
     */
    public List<AnnotationGroup> getSelectableAnnotationGroups() {
        return selectableAnnotationGroups;
    }

}
