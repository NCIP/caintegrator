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

import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.common.EmailUtil;
import gov.nih.nci.security.authentication.helper.LDAPHelper;
import gov.nih.nci.security.exceptions.internal.CSInternalConfigurationException;
import gov.nih.nci.security.exceptions.internal.CSInternalInsufficientAttributesException;
import gov.nih.nci.security.exceptions.internal.CSInternalLoginException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.mail.MessagingException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

import org.apache.commons.lang.StringUtils;
import org.jboss.security.auth.login.XMLLoginConfigImpl;

/**
 * 
 */
@SuppressWarnings("PMD.ReplaceHashtableWithMap") // LDAPHelper.authenticate uses a Hashtable.
public class RegistrationServiceImpl implements RegistrationService {
    
    private static final String REG_EMAIL_SUBJECT = "caIntegrator Registration";
    private static final String CONFIRM_EMAIL_SUBJECT = "caIntegrator Registration Confirmation";
    private static final String CONFIRM_EMAIL_CONTENT = "Dear NCICB User\n\nThank you for your submission concerning"
            + " caIntegrator registration request.  You will receive a followup call or email shortly.\n\nThank you,"
            + "\n\nNCICB Application Support Group";
    
    private ConfigurationHelper configurationHelper;
    
    /**
     * {@inheritDoc}
     */
    public void registerUser(RegistrationRequest registrationRequest) throws MessagingException {
        sendRegistrationConfirmation(registrationRequest);
        sendRegistrationRequestToAdmin(registrationRequest);
    }

    private void sendRegistrationConfirmation(RegistrationRequest registrationRequest) throws MessagingException {
        String emailFrom = configurationHelper.getString(ConfigurationParameter.REGISTRATION_EMAIL_FROM);
        EmailUtil.sendMail(Collections.singletonList(registrationRequest.getEmail()), 
                emailFrom, CONFIRM_EMAIL_SUBJECT, CONFIRM_EMAIL_CONTENT);
    }


    private void sendRegistrationRequestToAdmin(RegistrationRequest registrationRequest) throws MessagingException {
        String admin = configurationHelper.getString(ConfigurationParameter.REGISTRATION_EMAIL_TO);
        EmailUtil.sendMail(Collections.singletonList(admin), registrationRequest.getEmail(), REG_EMAIL_SUBJECT,
                registrationRequest.getMailBody());
    }

    
    /**
     * {@inheritDoc}
     */
    public boolean ldapAuthenticate(Map<String, String> connectionProperties, String userID, String password) 
    throws CSInternalConfigurationException, CSInternalInsufficientAttributesException {
        Hashtable<String, String> connectionPropertiesTable = new Hashtable<String, String>();
        connectionPropertiesTable.putAll(connectionProperties);
        try {
            return LDAPHelper.authenticate(connectionPropertiesTable, userID, password.toCharArray(), null);
        } catch (CSInternalLoginException e) {
            // CSM throws this exception on valid user / wrong pass
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getLdapContextParams() {
        Map<String, String> ldapContextParams = new HashMap<String, String>();
        XMLLoginConfigImpl config = retrieveConfigFile();
        if (config == null) {
            return ldapContextParams;
        }
        AppConfigurationEntry[] entries = config.getAppConfigurationEntry("caintegrator2");
        for (AppConfigurationEntry entry : entries) {
            if (StringUtils.containsIgnoreCase(entry.getLoginModuleName(), "ldap")) {
                Map<String, String> entryMap = (Map<String, String>) entry.getOptions();
                for (String entryKey : entryMap.keySet()) {
                    ldapContextParams.put(entryKey, entryMap.get(entryKey));
                }
            }
        }
        return ldapContextParams;
    }
    
    @SuppressWarnings({ "unchecked" })
    private XMLLoginConfigImpl retrieveConfigFile() {
        try {
            XMLLoginConfigImpl config = (XMLLoginConfigImpl) java.security.AccessController
                .doPrivileged(new java.security.PrivilegedAction() {
                    public Object run() {
                        return Configuration.getConfiguration();
                    }
                });
        return config;
        } catch (RuntimeException e) {
            return null;
        }
    }
    
    /**
     * @return the configurationHelper
     */
    public ConfigurationHelper getConfigurationHelper() {
        return configurationHelper;
    }

    /**
     * @param configurationHelper the configurationHelper to set
     */
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }
    
}
