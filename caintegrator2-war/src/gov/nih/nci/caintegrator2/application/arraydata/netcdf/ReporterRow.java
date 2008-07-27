package gov.nih.nci.caintegrator2.application.arraydata.netcdf;

/**
 * This is the class that holds the individual rows coming from the netCDF file.
 */
public class ReporterRow {

    private String reporterId;
    private Float[] arrayValues;
    /**
     * @return the reporterId
     */
    public String getReporterId() {
        return reporterId;
    }
    /**
     * @param reporterId the reporterId to set
     */
    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }
    /**
     * @return the arrayValues
     */
    public Float[] getArrayValues() {
        return arrayValues.clone();
    }
    /**
     * @param arrayValues the arrayValues to set
     */
    public void setArrayValues(Float[] arrayValues) {
        this.arrayValues = arrayValues.clone();
    }

    
}
