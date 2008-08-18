package gov.nih.nci.caintegrator2.web.action.study.management;

public class SaveImagingSourceAction extends AbstractImagingSourceAction{

	private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        getStudyManagementService().save(getStudyConfiguration());
        return SUCCESS;
    }
}
