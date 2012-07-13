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
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.cabio.CaBioApplicationServiceFactoryStub.ApplicationServiceStub;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CaBioFacadeImplTest {
    private CaBioFacadeImpl caBioFacade;
    private ApplicationServiceStub applicationServiceStub;

    @Before
    public void setUp() throws Exception {
        caBioFacade = new CaBioFacadeImpl();
        caBioFacade.setDao(new CaIntegrator2DaoStub());
        caBioFacade.setCaBioApplicationServiceFactory(new CaBioApplicationServiceFactoryStub());
        applicationServiceStub = (ApplicationServiceStub)
            caBioFacade.getCaBioApplicationServiceFactory().retrieveCaBioApplicationService("");
    }

    @Test
    public void testRetrieveGenes() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(false);
        params.setSearchType(CaBioSearchTypeEnum.GENE_KEYWORDS);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ANY.getValue());
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenes(params);
        assertEquals("BRCA1", genes.get(0).getSymbol());
        assertEquals("EGFR", genes.get(1).getSymbol());
        assertEquals("EGFR", genes.get(2).getSymbol());
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.fullName) LIKE ?  )  OR  (  lower(o.fullName) LIKE ?  )"));
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(true);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ALL.getValue());
        genes = caBioFacade.retrieveGenes(params);
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.fullName) LIKE ?  )  AND  (  lower(o.fullName) LIKE ?  )"));
        assertTrue(applicationServiceStub.hqlString.contains("o.taxon.commonName LIKE ?"));
        assertEquals(1, genes.size());
        assertEquals("BRCA1", genes.get(0).getSymbol());

        params.setSearchType(CaBioSearchTypeEnum.GENE_SYMBOL);
        params.setKeywords("egfr brca");
        params.setSearchPreference(KeywordSearchPreferenceEnum.ANY);
        genes = caBioFacade.retrieveGenes(params);
        // Should contain 4 keyword matches, because 2 keywords, and 2 fields (symbol and hugoSymbol)
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.symbol) LIKE ?  OR  lower(o.hugoSymbol) LIKE ?  )  OR  (  lower(o.symbol) LIKE ?  OR  lower(o.hugoSymbol) LIKE ?  )"));
        params.setSearchPreference(KeywordSearchPreferenceEnum.ALL);
        genes = caBioFacade.retrieveGenes(params);
        // Should contain 4 keyword matches, because 2 keywords, and 2 fields (symbol and hugoSymbol), but it must match both keywords, hence the AND instead of OR.
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.symbol) LIKE ?  OR  lower(o.hugoSymbol) LIKE ?  )  AND  (  lower(o.symbol) LIKE ?  OR  lower(o.hugoSymbol) LIKE ?  )"));

        params.setSearchType(CaBioSearchTypeEnum.DATABASE_CROSS_REF);
        params.setKeywords("OMIM");
        genes = caBioFacade.retrieveGenes(params);
        // Should contain 2 keyword matches
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.crossReferenceId) LIKE ?  )  ) and o.gene.taxon.commonName LIKE ?"));
    }

    @Test
    public void testRetrieveGenesFromGeneAlias() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(false);
        params.setSearchType(CaBioSearchTypeEnum.GENE_ALIAS);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ANY.getValue());
        params.setTaxon(CaBioSearchParameters.ALL_TAXONS);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenes(params);
        assertEquals("BRCA1", genes.get(0).getSymbol());
        assertEquals("EGFR", genes.get(1).getSymbol());
        assertEquals("EGFR", genes.get(2).getSymbol());
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.name) LIKE ?  )  OR  (  lower(o.name) LIKE ?  )"));
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(true);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ALL.getValue());
        params.setTaxon(CaBioSearchParameters.HUMAN_TAXON);
        genes = caBioFacade.retrieveGenes(params);
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.name) LIKE ?  )  AND  (  lower(o.name) LIKE ?  )"));
        assertTrue(applicationServiceStub.hqlString.contains("gene.taxon.commonName LIKE ?"));
        assertEquals(1, genes.size());
        assertEquals("BRCA1", genes.get(0).getSymbol());
    }

    @Test
    public void testRetrievePathways() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(false);
        params.setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ANY.getValue());
        params.setTaxon(CaBioSearchParameters.ALL_TAXONS);
        List<CaBioDisplayablePathway> pathways = caBioFacade.retrievePathways(params);
        assertEquals("pathway1", pathways.get(0).getName());
        assertEquals("pathway3", pathways.get(1).getName());
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.name) LIKE ?  OR  lower(o.displayValue) LIKE ?  OR  lower(o.description) LIKE ?  )  OR  (  lower(o.name) LIKE ?  OR  lower(o.displayValue) LIKE ?  OR  lower(o.description) LIKE ?  )"));
        assertFalse(applicationServiceStub.hqlString.contains("o.taxon.commonName LIKE ?"));
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(true);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ALL.getValue());
        params.setTaxon(CaBioSearchParameters.HUMAN_TAXON);
        pathways = caBioFacade.retrievePathways(params);
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.name) LIKE ?  OR  lower(o.displayValue) LIKE ?  OR  lower(o.description) LIKE ?  )  AND  (  lower(o.name) LIKE ?  OR  lower(o.displayValue) LIKE ?  OR  lower(o.description) LIKE ?  )"));
        assertTrue(applicationServiceStub.hqlString.contains("o.taxon.commonName LIKE ?"));
    }

    @Test
    public void testRetrieveGenesFromPathways() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.getPathways().add(new Pathway());
        params.setTaxon(CaBioSearchParameters.HUMAN_TAXON);
        params.setFilterGenesOnStudy(false);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenesFromPathways(params);
        assertEquals(1, genes.size());
    }

}
