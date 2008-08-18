package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;

public class AbstractImagingSourceAction extends AbstractStudyAction{
	
	private ImageAnnotationConfiguration imagingSource=new ImageAnnotationConfiguration();
	
	public void prepare(){
		super.prepare();
		if(getImagingSource().getId()!=null)
		{
			setImagingSource(getStudyManagementService().getRefreshedStudyEntity(getImagingSource()));
		}
	}

	public ImageAnnotationConfiguration getImagingSource() {
        return imagingSource;
    }

    /**
     * @param clinicalSource the clinicalSource to set
     */
    public void setImagingSource(ImageAnnotationConfiguration imagingSource) {
        this.imagingSource = imagingSource;
    }
}
