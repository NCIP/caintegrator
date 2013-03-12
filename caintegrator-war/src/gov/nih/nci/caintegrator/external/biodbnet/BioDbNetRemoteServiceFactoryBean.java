/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet;

import gov.nih.nci.caintegrator.external.biodbnet.domain.Db2DbParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbFindParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbOrthoParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbReportParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbWalkParams;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.encoding.TypeMapping;
import javax.xml.rpc.encoding.TypeMappingRegistry;

import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.springframework.remoting.jaxrpc.JaxRpcPortProxyFactoryBean;

/**
 * Custom JaxRpcPortProxyFactoryBean to handle registering the serializers & deserializers for the BioDbNet web service
 * complex types.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class BioDbNetRemoteServiceFactoryBean extends JaxRpcPortProxyFactoryBean {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postProcessJaxRpcService(Service service) {
        TypeMappingRegistry registry = service.getTypeMappingRegistry();
        TypeMapping mapping = registry.createTypeMapping();
        registerBeanMapping(mapping, Db2DbParams.class, "db2dbParams");
        registerBeanMapping(mapping, DbReportParams.class, "dbReportParams");
        registerBeanMapping(mapping, DbFindParams.class, "dbFindParams");
        registerBeanMapping(mapping, DbWalkParams.class, "dbWalkParams");
        registerBeanMapping(mapping, DbOrthoParams.class, "dbOrthoParams");
        registry.register("http://schemas.xmlsoap.org/soap/encoding/", mapping);
    }

    private void registerBeanMapping(TypeMapping mapping, Class<?> type, String name) {
        QName qName = new QName("urn:bioDBnet", name);
        mapping.register(type, qName, new BeanSerializerFactory(type, qName), new BeanDeserializerFactory(type, qName));
    }
}
