/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cabio.domain.Pathway;
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
    public List<CaBioDisplayableGene> retrieveGenes(CaBioSearchParameters searchParams) 
    throws ConnectionException {
        // When using an ApplicationService must store our context and re-set it so that the user still has old
        // authentication.
        SecurityContext originalContext = SecurityContextHolder.getContext();
        CaBioApplicationService caBioApplicationService =  
            caBioApplicationServiceFactory.retrieveCaBioApplicationService(caBioUrl);
        StringBuffer hqlString = new StringBuffer(getGeneQueryString());
        List<String> params = new ArrayList<String>();
        addKeywordsToQuery(searchParams, hqlString, params);
        if (!CaBioSearchParameters.ALL_TAXONS.equals(searchParams.getTaxon())) {
            hqlString.append(" and o.taxon.commonName LIKE ?");
            params.add(searchParams.getTaxon());
        }
        HQLCriteria hqlCriteria = new HQLCriteria(hqlString.toString(), params);
        List<Object> geneResults;
        try {
            geneResults = caBioApplicationService.query(hqlCriteria);
        } catch (ApplicationException e) {
            throw new ConnectionException("HQL Query Failed", e);
        } finally {
            // Restore context as described above.
            SecurityContextHolder.setContext(originalContext);
        }
        return createCaBioDisplayableGenesFromObjects(geneResults, searchParams);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<CaBioDisplayableGene> retrieveGenesFromPathways(CaBioSearchParameters searchParams) 
    throws ConnectionException {
        SecurityContext originalContext = SecurityContextHolder.getContext();
        CaBioApplicationService caBioApplicationService =  
            caBioApplicationServiceFactory.retrieveCaBioApplicationService(caBioUrl);
        List<Gene> results = new ArrayList<Gene>();
        List<CaBioDisplayableGene> displayableGenes = new ArrayList<CaBioDisplayableGene>();
        try {
            for (Pathway pathway : searchParams.getPathways()) {
                for (Object geneResult : caBioApplicationService.search(Gene.class, pathway)) {
                    results.add((Gene) geneResult);
                }
            }
            displayableGenes = createCaBioDisplayableGenesFromGenes(results, searchParams);
        } catch (ApplicationException e) {
            throw new ConnectionException("CaBio search failed", e);
        } finally {
            SecurityContextHolder.setContext(originalContext);
        }
        return displayableGenes;
    }

    /**
     * {@inheritDoc}
     */
    public List<CaBioDisplayablePathway> retrievePathways(CaBioSearchParameters searchParams) 
    throws ConnectionException {
        // When using an ApplicationService must store our context and re-set it so that the user still has old
        // authentication.
        SecurityContext originalContext = SecurityContextHolder.getContext();
        CaBioApplicationService caBioApplicationService =  
            caBioApplicationServiceFactory.retrieveCaBioApplicationService(caBioUrl);
        StringBuffer hqlString = new StringBuffer(getPathwayQueryString());
        List<String> params = new ArrayList<String>();
        addKeywordsToQuery(searchParams, hqlString, params);
        if (!CaBioSearchParameters.ALL_TAXONS.equals(searchParams.getTaxon())) {
            hqlString.append(" and o.taxon.commonName LIKE ?");
            params.add(searchParams.getTaxon());
        }
        HQLCriteria hqlCriteria = new HQLCriteria(hqlString.toString(), params);
        List<Object> pathwayResults;
        try {
            pathwayResults = caBioApplicationService.query(hqlCriteria);
        } catch (ApplicationException e) {
            throw new ConnectionException("HQL Query Failed", e);
        } finally {
            // Restore context as described above.
            SecurityContextHolder.setContext(originalContext);
        }
        return createCaBioDisplayablePathways(pathwayResults);
    }

    private String getGeneQueryString() {
        return "SELECT DISTINCT o.symbol, o.id, o.fullName, o.taxon.commonName, o.hugoSymbol " 
                            + " FROM gov.nih.nci.cabio.domain.Gene o"
                            + " WHERE o.symbol is not null ";
    }
    
    private String getPathwayQueryString() {
        return "SELECT o.name, o.id, o.displayValue, o.description " 
                            + " FROM gov.nih.nci.cabio.domain.Pathway o"
                            + " WHERE o.name is not null ";
    }

    private void addKeywordsToQuery(CaBioSearchParameters searchParams, StringBuffer hqlString, 
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
        hqlString.append(" ( ");
        for (String attribute : searchType.getSearchableAttributes()) {
            if (attributeNum != 0) {
                hqlString.append(" OR ");
            }
            hqlString.append(" lower(o." + attribute + ") LIKE ? ");
            params.add("%" + keyword.toLowerCase(Locale.getDefault()).trim() + "%");
            attributeNum++;
        }
        hqlString.append(" ) ");
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

    private List<CaBioDisplayableGene> createCaBioDisplayableGenesFromObjects(List<Object> geneResults, 
            CaBioSearchParameters searchParams) {
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
    
    private List<CaBioDisplayableGene> createCaBioDisplayableGenesFromGenes(List<Gene> geneResults, 
            CaBioSearchParameters searchParams) {
        List<CaBioDisplayableGene> genes = new ArrayList<CaBioDisplayableGene>();
        Set<String> usedGeneSymbols = new HashSet<String>();
        for (Gene gene : geneResults) {
            if (!usedGeneSymbols.contains(gene.getSymbol())) {
                usedGeneSymbols.add(gene.getSymbol());
                CaBioDisplayableGene displayableGene = new CaBioDisplayableGene();
                displayableGene.setId(String.valueOf(gene.getId()));
                displayableGene.setFullName(gene.getFullName());
                displayableGene.setHugoSymbol(gene.getHugoSymbol());
                displayableGene.setSymbol(gene.getSymbol());
                displayableGene.setTaxonCommonName(gene.getTaxon().getCommonName());
                genes.add(displayableGene);
            }
        }
        if (searchParams.isFilterGenesOnStudy() && !genes.isEmpty()) {
            genes = filterGenesNotInStudy(genes, searchParams.getStudy());
        }
        Collections.sort(genes);
        return genes;
    }
    
    private List<CaBioDisplayablePathway> createCaBioDisplayablePathways(List<Object> pathwayResults) {
        List<CaBioDisplayablePathway> pathways = new ArrayList<CaBioDisplayablePathway>();
        Set<String> usedNames = new HashSet<String>();
        for (Object result : pathwayResults) {
            Object[] pathwayObject = (Object[]) result;
            String name = ((String) pathwayObject[0]);
            if (!usedNames.contains(name)) {
                usedNames.add(name);
                CaBioDisplayablePathway pathway = new CaBioDisplayablePathway();
                pathway.setName(name);
                pathway.setId(String.valueOf((Long) pathwayObject[1]));
                pathway.setDisplayValue((String) pathwayObject[2]);
                pathway.setDescription((String) pathwayObject[3]);
                pathways.add(pathway);
            }
        }
        Collections.sort(pathways);
        return pathways;
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
