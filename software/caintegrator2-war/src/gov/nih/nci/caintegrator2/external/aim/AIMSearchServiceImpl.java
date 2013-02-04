/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.types.URI.MalformedURIException;

import edu.emory.cci.aim.client.AIMTCGADataServiceClient;
import edu.northwestern.radiology.aim.jaxb.ImageAnnotation;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.cqlresultset.TargetAttribute;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

/**
 * 
 */
public class AIMSearchServiceImpl extends AIMTCGADataServiceClient implements AIMSearchService {
    
    /**
     * Constructor using a connection.
     * @param conn to connect to.
     * @throws MalformedURIException malformed URI exception.
     * @throws RemoteException unable to connect.
     */
    public AIMSearchServiceImpl(ServerConnectionProfile conn) throws MalformedURIException, RemoteException {
        super(conn.getUrl());
    }
    
    /**
     * {@inheritDoc}
     */
    public ImageAnnotation getImageSeriesAnnotation(String seriesInstanceUID) throws ConnectionException {
        ImageAnnotation imageAnnotation = null;
        Attribute seriesInstanceUIDAttribute = retrieveAttribute("instanceUID", Predicate.EQUAL_TO, seriesInstanceUID);
        Association seriesAssociation = retrieveAssociation("edu.northwestern.radiology.aim.Series", "series", 
                seriesInstanceUIDAttribute);
        Association studyAssociation = retrieveAssociation("edu.northwestern.radiology.aim.Study", "study", 
                null);
        Association dicomImageReferenceAssociation = retrieveAssociation(
                "edu.northwestern.radiology.aim.DICOMImageReference", "imageReferenceCollection", null);
        studyAssociation.setAssociation(seriesAssociation);
        dicomImageReferenceAssociation.setAssociation(studyAssociation);
        CQLQuery query = retrieveQuery("edu.northwestern.radiology.aim.ImageAnnotation", 
                dicomImageReferenceAssociation);
        CQLQueryResults result = connectAndExecuteQuery(query);
        if (result != null) {
            CQLQueryResultsIterator iter = new CQLQueryResultsIterator(result, true);
            if (iter.hasNext()) {
                imageAnnotation = AIMJaxbParser.retrieveImageAnnotationFromXMLString((String) iter.next());
            }
        }
        return imageAnnotation;
    }
    
    List<String> retrieveAllSeriesIdentifiers() throws ConnectionException {
        List<String> imageSeriesIdentifiers = new ArrayList<String>();
        final CQLQuery query = new CQLQuery();
        Object target = new Object();
        target.setName("edu.northwestern.radiology.aim.Series");
        query.setTarget(target);
        QueryModifier distinctProjectModifier = new QueryModifier();
        distinctProjectModifier.setCountOnly(false);
        distinctProjectModifier.setDistinctAttribute("instanceUID");
        query.setQueryModifier(distinctProjectModifier);

        CQLQueryResults result = connectAndExecuteQuery(query);
        if (result != null) {
            CQLQueryResultsIterator iter2 = new CQLQueryResultsIterator(result);
            while (iter2.hasNext()) {
                TargetAttribute[] obj = (TargetAttribute[]) iter2.next();
                imageSeriesIdentifiers.add(obj[0].getValue());
            }
        } 
        return imageSeriesIdentifiers;
    }

    List<ImageAnnotation> retrieveAllImageAnnotations() throws ConnectionException {
        List<ImageAnnotation> imageAnnotations = new ArrayList<ImageAnnotation>();
        final CQLQuery query = new CQLQuery();
        Object target = new Object();
        target.setName("edu.northwestern.radiology.aim.ImageAnnotation");
        query.setTarget(target);

        CQLQueryResults result = connectAndExecuteQuery(query);
        if (result != null) {
            CQLQueryResultsIterator iter2 = new CQLQueryResultsIterator(result);
            while (iter2.hasNext()) {
                imageAnnotations.add((ImageAnnotation) iter2.next());
            }
        }
        return imageAnnotations;
    }
    
    private CQLQueryResults connectAndExecuteQuery(CQLQuery cqlQuery) throws ConnectionException {
        try {
            return query(cqlQuery);
        } catch (QueryProcessingExceptionType e) {
            throw new IllegalStateException("Error Processing Query.", e);
        } catch (MalformedQueryExceptionType e) {
            throw new IllegalStateException("Malformed Query.", e);
        } catch (RemoteException e) {
            throw new ConnectionException("Remote Connection Failed.", e);
        }
    }
    
    private CQLQuery retrieveQuery(String targetName, Association assoc) {
        final CQLQuery fcqlq = new CQLQuery();
        Object target = new Object();
        target.setName(targetName);
        fcqlq.setTarget(target);
        target.setAssociation(assoc);
        return fcqlq;
    }
    
    private Attribute retrieveAttribute(String name, Predicate predicate, String value) {
        Attribute att = new Attribute();
        att.setName(name);
        att.setPredicate(predicate);
        att.setValue(value);
        return att;
    }
    
    private Association retrieveAssociation(String name, String roleName, Attribute att) {
        Association assoc = new Association();
        assoc.setName(name);
        assoc.setRoleName(roleName);
        if (att != null) {
            assoc.setAttribute(att);
        }
        return assoc;
    }
}
