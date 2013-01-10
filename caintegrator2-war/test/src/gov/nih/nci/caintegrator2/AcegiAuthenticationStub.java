/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.AbstractAuthenticationToken;

/**
 * Stub implementation of <code>Authentication</code> interface for use in tests.
 */
@SuppressWarnings("deprecation")
public class AcegiAuthenticationStub extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;
    
    private String username = "username";

    private GrantedAuthority[] authorities = new GrantedAuthority[0];

    /**
     * {@inheritDoc}
     */
    public Object getCredentials() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Object getPrincipal() {
        return getUsername();
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GrantedAuthority[] getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(GrantedAuthority[] authorities) {
        this.authorities = authorities;
    }
    
}
