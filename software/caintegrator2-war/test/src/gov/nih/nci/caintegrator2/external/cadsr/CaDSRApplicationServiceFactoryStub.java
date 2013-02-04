/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.cadsr;

import gov.nih.nci.cadsr.domain.PermissibleValue;
import gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/**
 * 
 */
@SuppressWarnings("unchecked")
public class CaDSRApplicationServiceFactoryStub implements CaDSRApplicationServiceFactory {

    public ApplicationService retrieveCaDsrApplicationService(String caDsrUrl) throws IllegalStateException {
        return new ApplicationServiceStub();
    }
    
    static class ApplicationServiceStub implements ApplicationService {

        public List<Object> getAssociation(Object source, String associationName) throws ApplicationException {
            return null;
        }

        public Integer getMaxRecordsCount() throws ApplicationException {
            return null;
        }

        public Integer getQueryRowCount(Object criteria, String targetClassName) throws ApplicationException {
            return null;
        }

        public List<Object> query(CQLQuery cqlQuery) throws ApplicationException {
            return null;
        }

        public List<Object> query(DetachedCriteria detachedCriteria) throws ApplicationException {
            return null;
        }

        public List<Object> query(HQLCriteria hqlCriteria) throws ApplicationException {
            return null;
        }

        public List<Object> query(CQLQuery cqlQuery, String targetClassName) throws ApplicationException {
            return null;
        }

        public List<Object> query(DetachedCriteria detachedCriteria, String targetClassName)
                throws ApplicationException {
            return null;
        }

        public List<Object> query(HQLCriteria hqlCriteria, String targetClassName) throws ApplicationException {
            return null;
        }

        public List<Object> query(Object criteria, Integer firstRow, String targetClassName)
                throws ApplicationException {
            return null;
        }

        public List<Object> search(Class targetClass, List<?> objList) throws ApplicationException {
            return null;
        }

        public List<Object> search(Class targetClass, Object obj) throws ApplicationException {
            gov.nih.nci.cadsr.domain.DataElement de = new gov.nih.nci.cadsr.domain.DataElement();
            gov.nih.nci.cadsr.domain.EnumeratedValueDomain vd = new gov.nih.nci.cadsr.domain.EnumeratedValueDomain();
            de.setValueDomain(vd);
            vd.setDatatypeName("NUMBER");
            vd.setLongName("Some Value Domain");
            vd.setPublicID(Long.valueOf(2));
            vd.setValueDomainPermissibleValueCollection(new HashSet<ValueDomainPermissibleValue>());
            ValueDomainPermissibleValue vdpv = new ValueDomainPermissibleValue();
            PermissibleValue permissibleValue = new PermissibleValue();
            permissibleValue.setValue("1.0");
            vdpv.setPermissibleValue(permissibleValue);
            vd.getValueDomainPermissibleValueCollection().add(vdpv);
            List<Object> objects = new ArrayList<Object>();
            objects.add(de);
            return objects;
        }

        public List<Object> search(String path, List<?> objList) throws ApplicationException {
            return null;
        }

        public List<Object> search(String path, Object obj) throws ApplicationException {
            return null;
        }
        
        
    }

}
