/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.application.registration;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

/**
 * 
 */
@SuppressWarnings("PMD.TooManyFields") // A lot of fields required for registration.
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
    @RegexFieldValidator(expression = "(\\+)?([-\\._\\(\\) ]?[\\d]{3,20}[-\\._\\(\\) ]?){2,10}||^$", 
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
