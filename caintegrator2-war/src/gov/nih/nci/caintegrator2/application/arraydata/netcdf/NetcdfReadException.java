package gov.nih.nci.caintegrator2.application.arraydata.netcdf;

/**
 * Indicates a problem reading a NetCDF file.
 */
public class NetcdfReadException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new instance based on an underlying exception.
     * 
     * @param cause the source exception
     */
    public NetcdfReadException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the problem
     * @param cause the source exception
     */
    public NetcdfReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
