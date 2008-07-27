package gov.nih.nci.caintegrator2.application.arraydata.netcdf;

/**
 * Indicates a problem creating a NetCDF file.
 */
public class NetcdfCreationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new instance based on an underlying exception.
     * 
     * @param cause the source exception
     */
    public NetcdfCreationException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the connection problem
     * @param cause the source exception
     */
    public NetcdfCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
