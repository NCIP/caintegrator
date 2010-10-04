package gov.nih.nci.caintegrator2.security;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

@SuppressWarnings("unchecked")
public class SessionFactoryStub implements SessionFactory {

    private static final long serialVersionUID = 1L;

    public boolean getDefinedFilterNamesCalled;
    
    public void clear() {
        getDefinedFilterNamesCalled = false;
    }

    public void close() throws HibernateException {
        

    }

    public void evict(Class persistentClass) throws HibernateException {
        

    }

    public void evict(Class persistentClass, Serializable id) throws HibernateException {
        

    }

    public void evictCollection(String roleName) throws HibernateException {
        

    }

    public void evictCollection(String roleName, Serializable id) throws HibernateException {
        

    }

    public void evictEntity(String entityName) throws HibernateException {
        

    }

    public void evictEntity(String entityName, Serializable id) throws HibernateException {
        

    }

    public void evictQueries() throws HibernateException {
        

    }

    public void evictQueries(String cacheRegion) throws HibernateException {
        

    }

    public Map getAllClassMetadata() throws HibernateException {
        
        return null;
    }

    public Map getAllCollectionMetadata() throws HibernateException {
        
        return null;
    }

    public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException {
        
        return null;
    }

    public ClassMetadata getClassMetadata(String entityName) throws HibernateException {
        
        return null;
    }

    public CollectionMetadata getCollectionMetadata(String roleName) throws HibernateException {
        
        return null;
    }

    public Session getCurrentSession() throws HibernateException {
        
        return null;
    }

    public Set getDefinedFilterNames() {
        getDefinedFilterNamesCalled = true;
        return Collections.EMPTY_SET;
    }

    public FilterDefinition getFilterDefinition(String filterName) throws HibernateException {
        
        return null;
    }

    public Statistics getStatistics() {
        
        return null;
    }

    public boolean isClosed() {
        
        return false;
    }

    public Session openSession() throws HibernateException {
        
        return null;
    }

    public Session openSession(Connection connection) {
        
        return null;
    }

    public Session openSession(Interceptor interceptor) throws HibernateException {
        
        return null;
    }

    public Session openSession(Connection connection, Interceptor interceptor) {
        
        return null;
    }

    public StatelessSession openStatelessSession() {
        
        return null;
    }

    public StatelessSession openStatelessSession(Connection connection) {
        
        return null;
    }

    public Reference getReference() throws NamingException {
        
        return null;
    }

}
