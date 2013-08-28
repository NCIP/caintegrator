/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.registration;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

/**
 *
 */
public class RegistrationRequest {
    private static final String NEW_LINE = "\n";
    private static final String SEPARATOR_STRING = "----------------------------------------------------------";
    private static final String MAIL_BODY_HEADING = "Someone has requested access to caIntegrator2."
                                       + NEW_LINE + SEPARATOR_STRING
                                       + NEW_LINE + "The registrant entered the following information:"
                                       + NEW_LINE;

    private String loginName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String organization;
    private String role;
    private String fax;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zip;
    private String requestedStudies;
    private String uptUrl;

    /**
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }
    /**
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    /**
     * @return the firstName
     */
    @RequiredStringValidator(shortCircuit = true, message = "First name required.")
    public String getFirstName() {
        return StringUtils.isBlank(firstName) ? null : firstName;
    }
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * @return the lastName
     */
    @RequiredStringValidator(shortCircuit = true, message = "Last name required.")
    public String getLastName() {
        return lastName;
    }
    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * @return the email
     */
    @RequiredStringValidator(shortCircuit = true, message = "Email address required.")
    @EmailValidator(message = "Must enter a valid Email address.")
    public String getEmail() {
        return email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return the phone
     */
    @RequiredStringValidator(message = "Phone number required.")
    @RegexFieldValidator(regexExpression = "(\\+)?([-\\._\\(\\) ]?[\\d]{3,20}[-\\._\\(\\) ]?){2,10}||^$",
                         message = "Must supply valid phone number.")
    public String getPhone() {
        return phone;
    }
    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * @return the organization
     */
    @RequiredStringValidator(message = "Organization required.")
    public String getOrganization() {
        return organization;
    }
    /**
     * @param organization the organization to set
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    /**
     * @return the role
     */
    @RequiredFieldValidator(message = "Must select a role.")
    public String getRole() {
        return role;
    }
    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }
    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }
    /**
     * @return the address1
     */
    @RequiredStringValidator(message = "Address required.")
    public String getAddress1() {
        return address1;
    }
    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }
    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    /**
     * @return the city
     */
    @RequiredStringValidator(message = "City required.")
    public String getCity() {
        return city;
    }
    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * @return the state
     */
    @RequiredStringValidator(message = "State required.")
    public String getState() {
        return state;
    }
    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }
    /**
     * @return the country
     */
    @RequiredStringValidator(message = "Country required.")
    public String getCountry() {
        return country;
    }
    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    /**
     * @return the zip
     */
    @RequiredStringValidator(message = "Postal Code required.")
    public String getZip() {
        return zip;
    }
    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return the requestedStudies
     */
    public String getRequestedStudies() {
        return requestedStudies;
    }
    /**
     * @param requestedStudies the requestedStudies to set
     */
    public void setRequestedStudies(String requestedStudies) {
        this.requestedStudies = requestedStudies;
    }

    /**
     * @return the uptUrl
     */
    public String getUptUrl() {
        return uptUrl;
    }

    /**
     * @param uptUrl the uptUrl to set
     */
    public void setUptUrl(String uptUrl) {
        this.uptUrl = uptUrl;
    }

    /**
     * The mail body for registration request.
     * @return mail body.
     */
    public String getMailBody() {
        String ldapAuthenticationString = StringUtils.isBlank(getLoginName())
                ? "No LDAP Authentication" : "LDAP Authentication Username: " + loginName;
        String requestedStudiesString = StringUtils.isBlank(requestedStudies)
                ? "" : "Requested studies to be accessed: " + requestedStudies;
        return MAIL_BODY_HEADING + NEW_LINE
            +  ldapAuthenticationString + NEW_LINE
            + "First Name: " + firstName + NEW_LINE
            + "Last Name: " + lastName + NEW_LINE
            + "Email: " + email + NEW_LINE
            + "Phone: " + phone + NEW_LINE
            + "Fax: " + fax + NEW_LINE
            + "Organization: " + organization + NEW_LINE
            + "Address1: " + address1 + NEW_LINE
            + "Address2: " + address2 + NEW_LINE
            + "City: " + city + NEW_LINE
            + "State: " + state + NEW_LINE
            + "Country: " + country + NEW_LINE
            + "Zip: " + zip + NEW_LINE
            + "Requested Role(s): " + role + NEW_LINE + NEW_LINE
            + requestedStudiesString + NEW_LINE + SEPARATOR_STRING
            + NEW_LINE + NEW_LINE + getAdditionalInformationString();
    }

    private String getAdditionalInformationString() {
        String additionalInformationString = "Please go here " + uptUrl + ", then Login, click \"Group\", then click "
        + "\"Select an Existing Group\" and search for \"*" + organization
        + "*\" to see if there is already an existing user group for this persons organization.  Note that the user "
        + " group name may already exist under a slightly different variation so you may need to search on the "
        + " possible variations of the organization (example Univ. instead of University).  Please contact the "
        + "group administrator for permission to add this registrant.  " + NEW_LINE + NEW_LINE
        + " If there is not an existing user group for this registrant, then create a new user group, named according "
        + "to the Requested Roles above.  For example create one user group named \"" + organization
        + " Study Managers\" and one group named \"" + organization + " Study Investigators\".";
        return additionalInformationString;
    }

}
