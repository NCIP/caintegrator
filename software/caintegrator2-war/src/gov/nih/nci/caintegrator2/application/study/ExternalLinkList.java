/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 */
public class ExternalLinkList extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String fileName;
    private List<ExternalLink> externalLinks = new ArrayList<ExternalLink>();
    private transient File file;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * @return the externalLinks
     */
    public List<ExternalLink> getExternalLinks() {
        return externalLinks;
    }
    
    /**
     * @param externalLinks the externalLinks to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setExternalLinks(List<ExternalLink> externalLinks) {
        this.externalLinks = externalLinks;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    /**
     * @return a map of category -> list of external links.
     */
    public SortedMap<String, List<ExternalLink>> getLinksByCategory() {
        SortedMap<String, List<ExternalLink>> linksByCategory = new TreeMap<String, List<ExternalLink>>();
        for (ExternalLink externalLink : externalLinks) {
            String category = externalLink.getCategory() == null ? "" : externalLink.getCategory();
            if (linksByCategory.get(category) == null) {
                linksByCategory.put(category, new ArrayList<ExternalLink>());
            }
            linksByCategory.get(category).add(externalLink);
        }
        return linksByCategory;
    }

}
