/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.genelist;

import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Provides functionality to list and add array designs.
 */
@SuppressWarnings("PMD") // See createPlatform method
public class ManageGeneListAction extends AbstractDeployedStudyAction {

    private static final long serialVersionUID = 1L;
    private FileManager fileManager;
    private File geneListFile;
    private String geneListFileContentType;
    private String geneListFileFileName;
    private String geneListName;
    private String description;
    private String geneSymbols;
    private String selectedAction;
    private String geneListId;
    private List<String> geneSymbolList = new ArrayList<String>();

    private static final String CREATE_GENE_LIST_ACTION = "createGeneList";
    private static final String GENE_LIST_NAME = "geneListName";
    private static final String GENE_LIST_FILE = "geneListFile";
    private static final String EDIT_PAGE = "editPage";

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return CREATE_GENE_LIST_ACTION.equalsIgnoreCase(selectedAction);
    }
    
    /**
     * @return the Struts result.
     */
    public String execute() {
        if (CREATE_GENE_LIST_ACTION.equals(selectedAction)) {
            return createGeneList();
        }
        return SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        if (CREATE_GENE_LIST_ACTION.equalsIgnoreCase(selectedAction)) {
            prepareValueStack();
            validateGeneListName();
            validateGeneListData();
        } else {
            super.validate();
        }
    }
    
    private void validateGeneListName() {
        if (StringUtils.isEmpty(getGeneListName())) {
            addFieldError(GENE_LIST_NAME, "Gene List name is required");
        } else if (getStudySubscription().getGeneList(getGeneListName()) != null) {
            addFieldError(GENE_LIST_NAME, "Gene List name is duplicate: " + getGeneListName());
        }
    }
    
    private void validateGeneListData() {
        geneSymbolList.clear();
        extractGeneSymbols(getGeneListFile());
        extractGeneSymbols(getGeneSymbols());
        if (geneSymbolList.isEmpty()) {
            addActionError("There is nothing to save, you need to enter some genes or upload from a file.");
        }
    }
    
    private void extractGeneSymbols(String inputSymbols) {
        if (!StringUtils.isEmpty(inputSymbols)) {
            for (String symbol : inputSymbols.split(",")) {
                geneSymbolList.add(symbol.trim());
            }
        }
    }
    
    private void extractGeneSymbols(File uploadFile) {
        if (uploadFile != null) {
            CSVReader reader;
            try {
                reader = new CSVReader(new FileReader(uploadFile));
                String[] symbols;
                while ((symbols = reader.readNext()) != null) {
                    for (String symbol : symbols) {
                        geneSymbolList.add(symbol.trim()); 
                    }
                }
                if (geneSymbolList.isEmpty()) {
                    addFieldError(GENE_LIST_FILE, "The upload file is empty");
                }
            } catch (IOException e) {
                addFieldError(GENE_LIST_FILE, " Error reading gene list file");
            }
        }
    }

    /**
     * @return SUCCESS
     */
    private String createGeneList() {
        GeneList geneList = new GeneList();
        geneList.setName(getGeneListName());
        geneList.setDescription(getDescription());
        geneList.setSubscription(getStudySubscription());
        getWorkspaceService().createGeneList(geneList, geneSymbolList);
        return EDIT_PAGE;
    }

    /**
     * @return the geneListFile
     */
    public File getGeneListFile() {
        return geneListFile;
    }

    /**
     * @param geneListFile the geneListFile to set
     */
    public void setGeneListFile(File geneListFile) {
        this.geneListFile = geneListFile;
    }

    /**
     * @return the geneListFileContentType
     */
    public String getGeneListFileContentType() {
        return geneListFileContentType;
    }

    /**
     * @param geneListFileContentType the geneListFileContentType to set
     */
    public void setGeneListFileContentType(String geneListFileContentType) {
        this.geneListFileContentType = geneListFileContentType;
    }

    /**
     * @return the geneListFileFileName
     */
    public String getGeneListFileFileName() {
        return geneListFileFileName;
    }

    /**
     * @param geneListFileFileName the platformFileFileName to set
     */
    public void setGeneListFileFileName(String geneListFileFileName) {
        this.geneListFileFileName = geneListFileFileName;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the selectedAction to set
     */
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * @return the geneListName
     */
    public String getGeneListName() {
        return geneListName;
    }

    /**
     * @param geneListName the geneListName to set
     */
    public void setGeneListName(String geneListName) {
        this.geneListName = geneListName;
    }

    /**
     * @return the geneListId
     */
    public String getGeneListId() {
        return geneListId;
    }

    /**
     * @param geneListId the geneListId to set
     */
    public void setGeneListId(String geneListId) {
        this.geneListId = geneListId;
    }

    /**
     * @return the geneSymbols
     */
    public String getGeneSymbols() {
        return geneSymbols;
    }

    /**
     * @param geneSymbols the geneSymbols to set
     */
    public void setGeneSymbols(String geneSymbols) {
        this.geneSymbols = geneSymbols;
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
     * @return the geneSymbolList
     */
    public List<String> getGeneSymbolList() {
        return geneSymbolList;
    }
    
    /**
     * @return the study subscription
     */
    public StudySubscription getSubscription() {
        return getStudySubscription();
    }
}
