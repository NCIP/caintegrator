/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
            if (hqlString.contains("Gene o") || hqlString.contains("DatabaseCrossReference o")) {
                return retrieveGenes();
            } else if (hqlString.contains("Pathway o")) {
                return retrievePathways();
            } else if (hqlString.contains("GeneAlias o")) {
                return retrieveGeneAlias();
            }
            return null;
        }
        
        private List<Object> retrieveGeneAlias() {
            List<Object> objects = new ArrayList<Object>();
            Object[] object1 = new Object[4];
            Long id1 = 1l;
            String name1 = "geneAlias1";
            object1[0] = name1;
            object1[1] = id1;
            objects.add(object1);
            return objects;
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
