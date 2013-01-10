/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external;

import java.util.Set;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import static org.junit.Assert.assertEquals;

public class ServerConnectionProfileGenerator extends AbstractTestDataGenerator<ServerConnectionProfile> {

    public static final ServerConnectionProfileGenerator INSTANCE = new ServerConnectionProfileGenerator();
    
    @Override
    public void compareFields(ServerConnectionProfile original, ServerConnectionProfile retrieved) {
        assertEquals(original.getHostname(), retrieved.getHostname());        
        assertEquals(original.getPassword(), retrieved.getPassword());        
        assertEquals(original.getPort(), retrieved.getPort());        
        assertEquals(original.getUrl(), retrieved.getUrl());        
        assertEquals(original.getUsername(), retrieved.getUsername());        
    }

    @Override
    public ServerConnectionProfile createPersistentObject() {
        return new ServerConnectionProfile();
    }

    @Override
    public void setValues(ServerConnectionProfile profile, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        profile.setHostname(getUniqueString());
        profile.setPassword(getUniqueString());
        profile.setPort(getUniqueInt());
        profile.setUrl(getUniqueString());
        profile.setUsername(getUniqueString());
    }

}
