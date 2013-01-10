/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.cadsr;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.cadsr.domain.DataElement;
import gov.nih.nci.cadsr.domain.EnumeratedValueDomain;
import gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 */
public class CaDSRFacadeImplTest {
    private CaDSRFacadeImpl caDSRFacade;

    @Before
    public void setUp() throws Exception {
        ApplicationService appService = mock(ApplicationService.class);
        when(appService.search(any(Class.class), any())).then(new Answer<List<Object>>() {
            @Override
            public List<Object> answer(InvocationOnMock invocation) throws Throwable {
                DataElement de = new DataElement();
                EnumeratedValueDomain vd = new EnumeratedValueDomain();
                de.setValueDomain(vd);
                vd.setDatatypeName("NUMBER");
                vd.setLongName("Some Value Domain");
                vd.setPublicID(Long.valueOf(2));
                vd.setValueDomainPermissibleValueCollection(new HashSet<ValueDomainPermissibleValue>());
                ValueDomainPermissibleValue vdpv = new ValueDomainPermissibleValue();
                gov.nih.nci.cadsr.domain.PermissibleValue permissibleValue = new gov.nih.nci.cadsr.domain.PermissibleValue();
                permissibleValue.setValue("1.0");
                vdpv.setPermissibleValue(permissibleValue);
                vd.getValueDomainPermissibleValueCollection().add(vdpv);
                List<Object> objects = new ArrayList<Object>();
                objects.add(de);
                return objects;
            }
        });

        CaDSRApplicationServiceFactory appFactory = mock(CaDSRApplicationServiceFactory.class);
        when(appFactory.retrieveCaDsrApplicationService(anyString())).thenReturn(appService);

        caDSRFacade = new CaDSRFacadeImpl();
        caDSRFacade.setSearch(new SearchStub());
        caDSRFacade.setCaDsrApplicationServiceFactory(appFactory);
    }

    @Test
    public void testRetreiveCandidateDataElements() {
        List<CommonDataElement> dataElements = caDSRFacade.retreiveCandidateDataElements(Arrays.asList(new String[]{"congestive", "heart", "failure"}));
        CommonDataElement de = dataElements.get(0);
        assertNotNull(de);
        assertEquals("test", de.getLongName());
        assertEquals("congestive heart failure", de.getDefinition());
        assertEquals(Long.valueOf(2), de.getPublicID());
    }

    @Test
    public void testRetrieveValueDomainForDataElement() throws ConnectionException {
        ValueDomain valueDomain = caDSRFacade.retrieveValueDomainForDataElement(Long.valueOf(1), null);
        assertEquals(AnnotationTypeEnum.NUMERIC, valueDomain.getDataType());
        assertEquals(Long.valueOf(2),valueDomain.getPublicID());
        assertTrue(!valueDomain.getPermissibleValueCollection().isEmpty());
        PermissibleValue numericPermissibleValue = valueDomain.getPermissibleValueCollection().iterator().next();
        assertTrue(numericPermissibleValue.getValue().equals("1.0"));
    }

}
