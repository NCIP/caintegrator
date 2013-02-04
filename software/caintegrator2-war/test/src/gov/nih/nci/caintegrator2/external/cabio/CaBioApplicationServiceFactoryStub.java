/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cabio.domain.Taxon;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.search.GridIdQuery;
import gov.nih.nci.search.RangeQuery;
import gov.nih.nci.search.SearchQuery;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.CaBioApplicationService;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

@SuppressWarnings("unchecked")
public class CaBioApplicationServiceFactoryStub implements CaBioApplicationServiceFactory {
    
    ApplicationServiceStub applicationServiceStub = new ApplicationServiceStub();
    
    public CaBioApplicationService retrieveCaBioApplicationService(String caBioUrl) throws ConnectionException {
        
        return applicationServiceStub;
    }
    
    static class ApplicationServiceStub implements CaBioApplicationService {
        public String hqlString = "";
        public boolean searchCalled = false;

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
            hqlString = hqlCriteria.getHqlString();
            if (hqlString.contains("Gene o")) {
                return retrieveGenes();
            } else if (hqlString.contains("Pathway o")) {
                return retrievePathways();
            }
            return null;
        }
        
        private List<Object> retrievePathways() {
            List<Object> objects = new ArrayList<Object>();
            Object[] object1 = new Object[4];
            Long id1 = 1l;
            String name1 = "pathway1";
            String title1 = "Fullname Test";
            String description1 = "description";
            object1[0] = name1;
            object1[1] = id1;
            object1[2] = title1;
            object1[3] = description1;
            objects.add(object1);
            
            Object[] object2 = new Object[4];
            Long id2 = 2l;
            String name2 = "pathway1";
            String title2 = "Fullname Test";
            String description2 = "description";
            object2[0] = name2;
            object2[1] = id2;
            object2[2] = title2;
            object2[3] = description2;
            objects.add(object2);
            
            Object[] object3 = new Object[4];
            Long id3 = 3l;
            String name3 = "pathway3";
            String title3 = "Fullname Test";
            String description3 = "description";
            object3[0] = name3;
            object3[1] = id3;
            object3[2] = title3;
            object3[3] = description3;
            objects.add(object3);
            
            return objects;
        }

        private List<Object> retrieveGenes() {
            List<Object> objects = new ArrayList<Object>();
            Object[] object1 = new Object[5];
            Long id1 = 1l;
            String symbol1 = "EGFR";
            String fullName1 = "Fullname Test";
            String taxon1 = "human";
            object1[0] = symbol1;
            object1[1] = id1;
            object1[2] = fullName1;
            object1[3] = taxon1;
            object1[4] = symbol1;
            objects.add(object1);
            
            Object[] object2 = new Object[5];
            Long id2 = 2l;
            String symbol2 = "brca1";
            String fullName2 = "Fullname Test";
            String taxon2 = "human";
            object2[0] = symbol2;
            object2[1] = id2;
            object2[2] = fullName2;
            object2[3] = taxon2;
            object2[4] = symbol2;
            objects.add(object2);
            
            Object[] object3 = new Object[5];
            Long id3 = 3l;
            String symbol3 = "egfr";
            String fullName3 = "Fullname Test";
            String taxon3 = "mouse";
            
            object3[0] = symbol3;
            object3[1] = id3;
            object3[2] = fullName3;
            object3[3] = taxon3;
            object3[4] = symbol3;
            objects.add(object3);
            
            return objects;
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
            searchCalled = true;
            List<Object> objects = new ArrayList<Object>();
            if (Gene.class.equals(targetClass)) {
                Taxon taxon = new Taxon();
                taxon.setCommonName("human");
                Gene gene1 = new Gene();
                gene1.setId(1l);
                gene1.setSymbol("EGFR");
                gene1.setTaxon(taxon);
                gene1.setHugoSymbol("EGFR");
                gene1.setFullName("Some description");
                objects.add(gene1);
            }
            return objects;
        }

        public List<Object> search(String path, List<?> objList) throws ApplicationException {
            return null;
        }

        public List<Object> search(String path, Object obj) throws ApplicationException {
            return null;
        }

        public boolean exist(String bigId) throws ApplicationException {
            return false;
        }

        public Object getDataObject(String bigId) throws ApplicationException {
            return null;
        }

        public List search(SearchQuery searchQuery) throws ApplicationException {
            return null;
        }

        public List search(RangeQuery rangeQuery) throws ApplicationException {
            return null;
        }

        public List search(GridIdQuery gridIdQuery) throws ApplicationException {
            return null;
        }

        public List search(Class targetClass, RangeQuery rangeQuery) throws ApplicationException {
            return null;
        }
        
        
    }

}
