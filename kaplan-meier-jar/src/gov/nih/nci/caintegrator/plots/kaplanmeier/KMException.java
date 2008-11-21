package gov.nih.nci.caintegrator.plots.kaplanmeier;


@SuppressWarnings("serial")
public class KMException extends Exception {
    public KMException() {
    }

    public KMException(String message, Throwable cause) {
        super(message, cause);
    }

    public KMException(Throwable cause) {
        super(cause);
    }

    public KMException(String message) {
        super(message);
    }
}
