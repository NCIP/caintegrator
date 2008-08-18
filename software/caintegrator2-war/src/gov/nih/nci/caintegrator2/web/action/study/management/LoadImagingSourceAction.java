package gov.nih.nci.caintegrator2.web.action.study.management;

public class LoadImagingSourceAction extends AbstractImagingSourceAction{

	 private static final long serialVersionUID = 1L;

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public String execute() {
	        getStudyManagementService().loadImageAnnotation(getStudyConfiguration());
	        return SUCCESS;
	    }
}
