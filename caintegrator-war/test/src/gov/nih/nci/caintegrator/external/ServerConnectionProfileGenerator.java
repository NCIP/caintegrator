/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.Set;

/**
 * Server connection profile generator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class ServerConnectionProfileGenerator extends AbstractTestDataGenerator<ServerConnectionProfile> {

    /**
     * The instance.
     */
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
