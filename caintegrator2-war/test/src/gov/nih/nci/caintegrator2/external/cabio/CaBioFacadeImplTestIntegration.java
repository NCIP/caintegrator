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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.cabio.domain.Pathway;
import gov.nih.nci.cabio.domain.Taxon;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("unused")
public class CaBioFacadeImplTestIntegration {

    private CaBioFacadeImpl caBioFacade;
    private static final Logger LOGGER = Logger.getLogger(CaBioFacadeImplTestIntegration.class);    
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "cabio-test-config.xml", CaBioFacadeImplTestIntegration.class); 
        caBioFacade = (CaBioFacadeImpl) context.getBean("caBioFacade"); 
    }

    @Test
    public void testRetrieveGenesFromAlias() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("egfr");
        params.setTaxon("human");
        params.setSearchType(CaBioSearchTypeEnum.GENE_ALIAS);
        params.setFilterGenesOnStudy(false);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenes(params);
        assertTrue(checkSymbolExists("EGFR", genes));
        
        params.setTaxon(CaBioSearchParameters.ALL_TAXONS);
        genes = caBioFacade.retrieveGenes(params);
        assertTrue(!genes.isEmpty());
    }

    @Test
    public void testRetrieveGenesFromDatabase() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("OMIM");
        params.setTaxon("human");
        params.setSearchType(CaBioSearchTypeEnum.DATABASE_CROSS_REF);
        params.setFilterGenesOnStudy(false);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenes(params);
        assertTrue(checkSymbolExists("EGFR", genes));
    }

    @Test
    public void testRetrieveGenes() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("heart");
        params.setTaxon("human");
        params.setFilterGenesOnStudy(false);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenes(params);
        assertTrue(checkSymbolExists("CDH13", genes));
        assertTrue(checkSymbolExists("FABP3", genes));
        assertTrue(checkSymbolExists("HAND1", genes));
        assertTrue(checkSymbolExists("HAND2", genes));
        assertTrue(checkSymbolExists("LBH", genes));
        assertTrue(checkSymbolExists("LOC128102", genes));
        
        params.setTaxon("mouse");
        genes = caBioFacade.retrieveGenes(params);
        assertFalse(checkSymbolExists("CDH13", genes));
        assertTrue(checkSymbolExists("FABP3", genes));
        
        params.setTaxon(CaBioSearchParameters.ALL_TAXONS);
        genes = caBioFacade.retrieveGenes(params);
        assertTrue(!genes.isEmpty());
    }
    
    @Test 
    public void testRetrievePathways() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("h_41bbPathway");
        params.setTaxon("human");
        params.setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        List<CaBioDisplayablePathway> pathways = caBioFacade.retrievePathways(params);
        assertEquals(1, pathways.size());
        assertEquals("475", pathways.get(0).getId());
    }
    
    @Test
    public void testRetrieveGenesFromPathways() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        Taxon taxon = new Taxon();
        taxon.setCommonName("human");
        params.setTaxon("human");
        params.setFilterGenesOnStudy(false);
        Pathway pathway = new Pathway();
        pathway.setId(665l); //h_LDLpathway
        pathway.setTaxon(taxon);
        Pathway pathway2 = new Pathway();
        pathway2.setId(857l); //h_ace2Pathway
        pathway2.setTaxon(taxon);
        params.getPathways().add(pathway);
        params.getPathways().add(pathway2);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenesFromPathways(params);
        assertEquals(19, genes.size());
    }
    
    @Test
    public void testRetrieveAllTaxons() throws ConnectionException {
        List<String> taxons = caBioFacade.retrieveAllTaxons();
        assertFalse(taxons.isEmpty());
    }
    
    private boolean checkSymbolExists(String symbol, List<CaBioDisplayableGene> genes) {
        for (CaBioDisplayableGene gene : genes) {
            if (symbol.equals(gene.getSymbol())) {
                return true;
            }
        }
        return false;
    }
}
