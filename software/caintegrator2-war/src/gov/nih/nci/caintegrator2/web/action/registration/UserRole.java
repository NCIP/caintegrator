package gov.nih.nci.caintegrator2.web.action.registration;

/**
 * 
 */
public enum UserRole {
    /** 
     * Study Manager Role. 
     */
    STUDY_MANAGER ("Study Manager"),
    /** 
     * Study Investigator Role. 
     */
    STUDY_INVESTIGATOR ("Study Investigator");

    private final String name;


    /**
     * the constructor.
     */
    UserRole(String name) {
        this.name = name;
    }

    

    /**
     * @return display name
     */
    public String getName() {
        return this.name;
    }

}
