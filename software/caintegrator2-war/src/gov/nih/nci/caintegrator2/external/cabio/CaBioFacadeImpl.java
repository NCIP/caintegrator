/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cabio.domain.GeneAlias;
import gov.nih.nci.cabio.domain.Pathway;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.common.domain.DatabaseCrossReference;
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

    private static final Integer GENE_ADDITIONAL_INFO_THRESHOLD = 1000;
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
        StringBuffer hqlString = new StringBuffer(getGeneQueryString(searchParams.getSearchType()));
        List<String> params = new ArrayList<String>();
        addKeywordsToQuery(searchParams, hqlString, params);
        if (!CaBioSearchParameters.ALL_TAXONS.equals(searchParams.getTaxon())) {
            addTaxonParam(searchParams, hqlString, params);
        }
        HQLCriteria hqlCriteria = new HQLCriteria(hqlString.toString(), params);
        List<Object> geneResults;
        try {
            geneResults = caBioApplicationService.query(hqlCriteria);
            return createCaBioDisplayableGenesFromObjects(geneResults, searchParams, caBioApplicationService);
        } catch (ApplicationException e) {
            throw new ConnectionException("HQL Query Failed", e);
        } finally {
            // Restore context as described above.
            SecurityContextHolder.setContext(originalContext);
        }
    }

    private void addTaxonParam(CaBioSearchParameters searchParams, StringBuffer hqlString, List<String> params) {
        if (CaBioSearchTypeEnum.DATABASE_CROSS_REF.equals(searchParams.getSearchType())) {
            hqlString.append(" and o.gene.taxon.commonName LIKE ?");
        } else if (CaBioSearchTypeEnum.GENE_ALIAS.equals(searchParams.getSearchType())) {
            hqlString.append(" and gene.taxon.commonName LIKE ?");
        } else {
            hqlString.append(" and o.taxon.commonName LIKE ?");
        }
        params.add(searchParams.getTaxon());
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
            addTaxonParam(searchParams, hqlString, params);
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

    private String getGeneQueryString(CaBioSearchTypeEnum searchType) {
        if (CaBioSearchTypeEnum.DATABASE_CROSS_REF.equals(searchType)) {
            return "SELECT DISTINCT o.gene.symbol, o.gene.id, o.gene.fullName, "
                    + " o.gene.taxon.commonName, o.gene.hugoSymbol " 
                    + " FROM gov.nih.nci.common.domain.DatabaseCrossReference o"
                    + " WHERE o.gene.symbol is not null ";
        } else if (CaBioSearchTypeEnum.GENE_ALIAS.equals(searchType)) {
            return "SELECT DISTINCT gene.symbol, gene.id, gene.fullName, gene.taxon.commonName, gene.hugoSymbol " 
                    + " FROM gov.nih.nci.cabio.domain.Gene gene"
                    + " LEFT JOIN gene.geneAliasCollection o "
                    + " WHERE gene.symbol is not null";
        }
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
            CaBioSearchParameters searchParams, CaBioApplicationService caBioApplicationService)
    throws ApplicationException {
        List<CaBioDisplayableGene> genes = new ArrayList<CaBioDisplayableGene>();
        boolean lookupAdditionalInfo = true;
        if (geneResults.size() > GENE_ADDITIONAL_INFO_THRESHOLD) {
            lookupAdditionalInfo = false;
        }
        for (Object result : geneResults) {
            CaBioDisplayableGene displayableGene = createDisplayableGene(result, caBioApplicationService, 
                    lookupAdditionalInfo);
            genes.add(displayableGene);
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
            addDisplayableGene(genes, usedGeneSymbols, gene);
        }
        if (searchParams.isFilterGenesOnStudy() && !genes.isEmpty()) {
            genes = filterGenesNotInStudy(genes, searchParams.getStudy());
        }
        Collections.sort(genes);
        return genes;
    }

    private void addDisplayableGene(List<CaBioDisplayableGene> genes, Set<String> usedGeneSymbols, Gene gene) {
        if (StringUtils.isNotBlank(gene.getSymbol()) && !usedGeneSymbols.contains(gene.getSymbol())) {
            usedGeneSymbols.add(gene.getSymbol());
            CaBioDisplayableGene displayableGene = new CaBioDisplayableGene();
            displayableGene.setId(gene.getId());
            displayableGene.setFullName(gene.getFullName());
            displayableGene.setHugoSymbol(gene.getHugoSymbol());
            displayableGene.setSymbol(gene.getSymbol());
            displayableGene.setTaxonCommonName(gene.getTaxon().getCommonName());
            genes.add(displayableGene);
        }
    }

    private CaBioDisplayableGene createDisplayableGene(Object result, CaBioApplicationService caBioApplicationService, 
            boolean lookupAdditionalInfo)
    throws ApplicationException {
        Object[] geneObject = (Object[]) result;
        CaBioDisplayableGene displayableGene = new CaBioDisplayableGene();
        displayableGene.setSymbol(((String) geneObject[0]).toUpperCase(Locale.getDefault()));
        displayableGene.setId((Long) geneObject[1]);
        displayableGene.setFullName((String) geneObject[2]);
        displayableGene.setTaxonCommonName((String) geneObject[3]);
        displayableGene.setHugoSymbol((String) geneObject[4]);
        Gene gene = new Gene();
        gene.setId(displayableGene.getId());
        if (lookupAdditionalInfo) {
            displayableGene.setGeneAliases(getGeneAliases(gene, caBioApplicationService));
            displayableGene.setDatabaseCrossReferences(getDatabaseCrossRefs(gene, caBioApplicationService));
        }
        return displayableGene;
    }

    private String getDatabaseCrossRefs(Gene gene, CaBioApplicationService caBioApplicationService)
    throws ApplicationException {
        List<Object> databaseCrossReferenceObjects = caBioApplicationService.search(DatabaseCrossReference.class, gene);
        List<String> dbCrossReferences = new ArrayList<String>();
        for (Object object : databaseCrossReferenceObjects) {
            dbCrossReferences.add(((DatabaseCrossReference) object).getCrossReferenceId());
        }
        return StringUtils.join(dbCrossReferences, " , ");
    }

    private String getGeneAliases(Gene gene, CaBioApplicationService caBioApplicationService)
    throws ApplicationException {
        List<Object> geneAliasObjects = caBioApplicationService.search(GeneAlias.class, gene);
        List<String> geneAliases = new ArrayList<String>();
        for (Object object : geneAliasObjects) {
            geneAliases.add(((GeneAlias) object).getName());
        }
        return StringUtils.join(geneAliases, " , ");
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
