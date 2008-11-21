package gov.nih.nci.caintegrator.plots.kaplanmeier;

/**
 * @author caIntegrator Team
*/

public enum ImageTypes {
    JPEG("jpeg"),
    PNG("png");
    final private String value;
    ImageTypes(String s) {
         this.value = s;
    }
    public String getValue() {
        return value;
    }
}
