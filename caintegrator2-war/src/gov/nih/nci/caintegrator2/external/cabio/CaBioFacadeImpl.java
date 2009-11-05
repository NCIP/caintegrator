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

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.CaBioApplicationService;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;

/**
 * Facade to retrieve data from CaBio.
 */
public class CaBioFacadeImpl implements CaBioFacade {

    private CaBioApplicationServiceFactory caBioApplicationServiceFactory;
    private CaIntegrator2Dao dao;
    private String caBioUrl;
    
    /**
     * {@inheritDoc}
     */
    public List<CaBioDisplayableGene> retrieveGenes(CaBioGeneSearchParameters searchParams) 
    throws ConnectionException {
        // When using an ApplicationService must store our context and re-set it so that the user still has old
        // authentication.
        SecurityContext originalContext = SecurityContextHolder.getContext();
        CaBioApplicationService caBioApplicationService =  
            caBioApplicationServiceFactory.retrieveCaBioApplicationService(caBioUrl);
        StringBuffer hqlString = 
            new StringBuffer("SELECT DISTINCT g.symbol, g.id, g.fullName, g.taxon.commonName, g.hugoSymbol " 
                                + " FROM gov.nih.nci.cabio.domain.Gene g"
                                + " WHERE g.symbol is not null ");
        List<String> params = new ArrayList<String>();
        addKeywordsToQuery(searchParams, hqlString, params);
        if (!CaBioGeneSearchParameters.ALL_TAXONS.equals(searchParams.getTaxon())) {
            hqlString.append(" and g.taxon.commonName LIKE ?");
            params.add(searchParams.getTaxon());
        }
        HQLCriteria hqlCriteria = new HQLCriteria(hqlString.toString(), params);
        List<Object> geneResults;
        try {
            geneResults = caBioApplicationService.query(hqlCriteria);
        } catch (ApplicationException e) {
            throw new IllegalStateException("HQL Query Failed", e);
        } finally {
            // Restore context as described above.
            SecurityContextHolder.setContext(originalContext);
        }
        return createCaBioDisplayableGenes(geneResults, searchParams);
    }

    private void addKeywordsToQuery(CaBioGeneSearchParameters searchParams, StringBuffer hqlString, 
            List<String> params) {
        int keywordNum = 0;
        for (String keyword : StringUtils.split(searchParams.getKeywords())) {
            if (keywordNum == 0) {
                hqlString.append(" AND (");
            } else {
                hqlString.append(" " + searchParams.getSearchPreference().getLogicalOperator() + " ");
            }
            addAttributesMatchingKeywords(searchParams.getSearchType(), hqlString, params, keyword);
            keywordNum++;
        }
        hqlString.append(" )");
    }

    private void addAttributesMatchingKeywords(CaBioSearchTypeEnum searchType, StringBuffer hqlString,
            List<String> params, String keyword) {
        int attributeNum = 0;
        for (String attribute : searchType.getCaBioObjectAttributes()) {
            if (attributeNum != 0) {
                hqlString.append(" OR ");
            }
            hqlString.append(" lower(g." + attribute + ") LIKE ? ");
            params.add("%" + keyword.toLowerCase(Locale.getDefault()).trim() + "%");
            attributeNum++;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> retrieveAllTaxons() throws ConnectionException {
        SecurityContext originalContext = SecurityContextHolder.getContext();
        ApplicationService caBioApplicationService = 
            caBioApplicationServiceFactory.retrieveCaBioApplicationService(caBioUrl);
        String hqlString = "SELECT DISTINCT t.commonName " 
            + " FROM gov.nih.nci.cabio.domain.Taxon t";
        HQLCriteria hqlCriteria = new HQLCriteria(hqlString);
        List<String> taxonResults = new ArrayList<String>();
        try {
            List<Object> queryResults = caBioApplicationService.query(hqlCriteria);
            for (Object taxonObj : queryResults) {
                taxonResults.add((String) taxonObj);
            }
        } catch (ApplicationException e) {
            throw new IllegalStateException("HQL Query Failed", e);
        } finally {
            // Restore context as described above.
            SecurityContextHolder.setContext(originalContext);
        }
        return taxonResults;
    }

    private List<CaBioDisplayableGene> createCaBioDisplayableGenes(List<Object> geneResults, 
            CaBioGeneSearchParameters searchParams) {
        List<CaBioDisplayableGene> genes = new ArrayList<CaBioDisplayableGene>();
        for (Object result : geneResults) {
            Object[] geneObject = (Object[]) result;
            CaBioDisplayableGene gene = new CaBioDisplayableGene();
            gene.setSymbol(((String) geneObject[0]).toUpperCase(Locale.getDefault()));
            gene.setId(String.valueOf((Long) geneObject[1]));
            gene.setFullName((String) geneObject[2]);
            gene.setTaxonCommonName((String) geneObject[3]);
            gene.setHugoSymbol((String) geneObject[4]);
            genes.add(gene);
        }
        if (searchParams.isFilterGenesOnStudy() && !genes.isEmpty()) {
            genes = filterGenesNotInStudy(genes, searchParams.getStudy());
        }
        Collections.sort(genes);
        return genes;
    }
    
    private List<CaBioDisplayableGene> filterGenesNotInStudy(List<CaBioDisplayableGene> genes, Study study) {
        List<CaBioDisplayableGene> genesInStudy = new ArrayList<CaBioDisplayableGene>();
        Set<String> symbols = new HashSet<String>();
        for (CaBioDisplayableGene gene : genes) {
            symbols.add(gene.getSymbol());
        }
        symbols = dao.retrieveGeneSymbolsInStudy(symbols, study);
        for (CaBioDisplayableGene gene : genes) {
            if (symbols.contains(gene.getSymbol())) {
                genesInStudy.add(gene);
            }
        }
        return genesInStudy;
    }

    /**
     * @return the caBioApplicationServiceFactory
     */
    public CaBioApplicationServiceFactory getCaBioApplicationServiceFactory() {
        return caBioApplicationServiceFactory;
    }

    /**
     * @param caBioApplicationServiceFactory the caBioApplicationServiceFactory to set
     */
    public void setCaBioApplicationServiceFactory(CaBioApplicationServiceFactory caBioApplicationServiceFactory) {
        this.caBioApplicationServiceFactory = caBioApplicationServiceFactory;
    }

    /**
     * @return the caBioUrl
     */
    public String getCaBioUrl() {
        return caBioUrl;
    }

    /**
     * @param caBioUrl the caBioUrl to set
     */
    public void setCaBioUrl(String caBioUrl) {
        this.caBioUrl = caBioUrl;
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

}
