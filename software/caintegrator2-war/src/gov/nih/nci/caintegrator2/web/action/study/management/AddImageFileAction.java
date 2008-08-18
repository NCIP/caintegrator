package gov.nih.nci.caintegrator2.web.action.study.management;


import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;

import java.io.File;
import java.io.IOException;

public class AddImageFileAction extends AbstractImagingSourceAction{
	
	private static final long serialVersionUID = 1L;
    private File imagingFile;
    private String imagingFileContentType;
    private String imagingFileFileName;
	
	public String execute()  {
        try {
        	ImageAnnotationConfiguration imagingSource = 
                        getStudyManagementService().addImageAnnotationFile(getStudyConfiguration(), getImagingFile(), 
                		  getImagingFileFileName());
        	setImagingSource(imagingSource);
            return SUCCESS;
        } catch (ValidationException e) {
            addFieldError("imagingFile", "Invalid file: " + e.getResult().getInvalidMessage());
            return INPUT;
        } catch (IOException e) {
            return ERROR;
        }
    }
	
	public void validate() {
        if (imagingFile == null) {
            addFieldError("imagingFile", "Imaging File is required");
        }
    }

	public File getImagingFile() {
		return imagingFile;
	}

	public void setImagingFile(File imagingFile) {
		this.imagingFile = imagingFile;
	}

	public String getImagingFileFileName() {
		return imagingFileFileName;
	}

	public void setImagingFileFileName(String imagingFileFileName) {
		this.imagingFileFileName = imagingFileFileName;
	}

	public String getImagingFileContentType() {
		return imagingFileContentType;
	}

	public void setImagingFileContentType(String imagingFileContentType) {
		this.imagingFileContentType = imagingFileContentType;
	}

}
