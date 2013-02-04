/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.type.NullableType;
import org.hibernate.type.TypeFactory;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * Got this from http://www.hibernate.org/272.html.  It is to persist our enum types 
 * in hibernate and is only referenced in hbm.xml files.
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ExcessiveMethodLength" })
public class GenericEnumUserType implements UserType, ParameterizedType {
    private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "name";
    private static final String DEFAULT_VALUE_OF_METHOD_NAME = "valueOf";
    
    @SuppressWarnings("unchecked")
    private Class<? extends Enum> enumClass;
    private Method identifierMethod;
    private Method valueOfMethod;
    private NullableType type;
    private int[] mySqlTypes;

    /**
     * {@inheritDoc}
     */
    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty("enumClass");
        Class<?> identifierType;
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException cfne) {
            throw new HibernateException("Enum class not found: " + enumClassName, cfne);
        }

        String identifierMethodName = parameters.getProperty("identifierMethod", DEFAULT_IDENTIFIER_METHOD_NAME);

        try {
            identifierMethod = enumClass.getMethod(identifierMethodName, new Class[0]);
            identifierType = identifierMethod.getReturnType();
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain identifier method", e);
        }

        type = (NullableType) TypeFactory.basic(identifierType.getName());

        if (type == null) {
            throw new HibernateException("Unsupported identifier type " + identifierType.getName());
        }

        mySqlTypes = new int[] {type.sqlType()};

        String valueOfMethodName = parameters.getProperty("valueOfMethod", DEFAULT_VALUE_OF_METHOD_NAME);

        try {
            valueOfMethod = enumClass.getMethod(valueOfMethodName, new Class[] {identifierType});
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain valueOf method", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Class returnedClass() {
        return enumClass;
    }

    /**
     * {@inheritDoc}
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws SQLException {  
        Object identifier = type.get(rs, names[0]);
        if (rs.wasNull()) {
            return null;
        }
        
        try {
            return valueOfMethod.invoke(enumClass, new Object[] {identifier});
        } catch (Exception e) {
            throw new HibernateException("Exception while invoking valueOf method '" 
                    + valueOfMethod.getName() + "' of " + "enumeration class '" + enumClass + "'", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws SQLException {
        try {
            if (value == null) {
                st.setNull(index, type.sqlType());
            } else {
                Object identifier = identifierMethod.invoke(value, new Object[0]);
                type.set(st, identifier, index);
            }
        } catch (Exception e) {
            throw new HibernateException("Exception while invoking identifierMethod '" 
                    + identifierMethod.getName() + "' of " + "enumeration class '" + enumClass + "'", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public int[] sqlTypes() {
        return mySqlTypes.clone();
    }

    /**
     * {@inheritDoc}
     */
    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    /**
     * {@inheritDoc}
     */
    public Object deepCopy(Object value) {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object x, Object y) {
        if (x != null && y != null) {
            return x.equals(y);
        } 
        if (x == null && y == null) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode(Object x) {
        return x.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMutable() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Object replace(Object original, Object target, Object owner) {
        return original;
    }
}
