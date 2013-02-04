/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action;

import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;

public abstract class AbstractSessionBasedTest {
    
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
    }

}
