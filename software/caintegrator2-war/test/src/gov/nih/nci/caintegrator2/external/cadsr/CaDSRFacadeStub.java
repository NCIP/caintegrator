/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.cadsr;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.util.Collections;
import java.util.List;

public class CaDSRFacadeStub implements CaDSRFacade {

    public boolean retreiveCandidateDataElementsCalled;
    public boolean retrieveValueDomainForDataElementCalled;
    public boolean retreiveDataElementCalled;
    
    public void clear() {
        retreiveCandidateDataElementsCalled = false;
        retrieveValueDomainForDataElementCalled = false;
        retreiveDataElementCalled = false;
    }

    public List<CommonDataElement> retreiveCandidateDataElements(List<String> keywords) {
        retreiveCandidateDataElementsCalled = true;
        return Collections.emptyList();
    }

    public ValueDomain retrieveValueDomainForDataElement(Long dataElementId, Float dataElementVersion) 
        throws ConnectionException {
        retrieveValueDomainForDataElementCalled = true;
        ValueDomain valueDomain = new ValueDomain();
        valueDomain.setDataType(AnnotationTypeEnum.STRING);
        return valueDomain;
    }

    public CommonDataElement retrieveDataElement(Long dataElementId, Float dataElementVersion)
            throws ConnectionException {
        retreiveDataElementCalled = true;
        CommonDataElement commonDataElement = new CommonDataElement();
        commonDataElement.setId(dataElementId);
        return commonDataElement;
    }

}
