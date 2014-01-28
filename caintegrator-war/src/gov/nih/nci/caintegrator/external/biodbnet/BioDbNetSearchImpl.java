/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet;

import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.external.biodbnet.domain.Db2DbParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbWalkParams;
import gov.nih.nci.caintegrator.external.biodbnet.search.GeneResults;
import gov.nih.nci.caintegrator.external.biodbnet.search.PathwayResults;
import gov.nih.nci.caintegrator.external.biodbnet.search.SearchParameters;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.Sets;

/**
 * Implementation of the internal bioDbNet search service.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@Service("bioDbNetService")
public class BioDbNetSearchImpl implements BioDbNetService {
    private static final Logger LOG = Logger.getLogger(BioDbNetSearchImpl.class);
    private static final int GENE_ID_LOOKUP_INDEX = 1;
    private static final int GENE_ID_INDEX = 0;
    private static final int GENE_INFO_INDEX = 1;
    private static final int GENE_SYMBOL_INDEX = 2;
    private static final int GENE_SYNONYM_INDEX = 3;
    private static final int TAXON_INDEX = 4;
    private static final int PATHWAY_INFO_INDEX = 1;

    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("Description:[\\s\\w\\d,()_.;-]*[^\\]]");
    private static final Pattern TAXON_PATTERN = Pattern.compile("Organism:[\\s\\w,]*[^\\]]");
    private static final Pattern PATHWAY_TITLE_PATTERN = Pattern.compile("\\[[\\s\\w\\d:-]*");
    private static final Pattern PATHWAY_NAME_PATTERN = Pattern.compile("^[\\w\\s\\d_-]*");
    private static final String GENE_LOOKUP_ID = "Gene ID";
    private static final String GENE_LOOKUP_SYMBOL = "Gene Symbol";
    private static final String GENE_LOOKUP_OUTPUTS = "Gene Info, Gene Symbol, Gene Synonyms, Taxon ID";
    private static final String EMPTY_VALUE = "-";
    private BioDbNetRemoteService bioDbNetRemoteService;
    private CaIntegrator2Dao dao;


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> retrieveGeneIds(SearchParameters params) {
        Db2DbParams db2dbParams = new Db2DbParams();
        db2dbParams.setTaxonId(params.getTaxon().getTaxonId());
        db2dbParams.setInput(GENE_LOOKUP_SYMBOL);
        db2dbParams.setOutputs(GENE_LOOKUP_ID);
        db2dbParams.setInputValues(params.getInputValues());

        String retrievedGeneIds = bioDbNetRemoteService.db2db(db2dbParams);
        return parseIds(retrievedGeneIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> retrieveGeneIdsByAlias(SearchParameters params) {
        DbWalkParams dbWalkParams = new DbWalkParams();
        dbWalkParams.setDbPath("Gene Symbol and Synonyms->Gene ID");
        dbWalkParams.setInputValues(params.getInputValues());
        dbWalkParams.setTaxonId(params.getTaxon().getTaxonId());

        String retrievedGeneIds = bioDbNetRemoteService.dbWalk(dbWalkParams);
        return parseIds(retrievedGeneIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<GeneResults> retrieveGenesById(SearchParameters params) {
        Set<GeneResults> retrievedGenes = Sets.newTreeSet();

        Db2DbParams db2dbParams = new Db2DbParams();
        db2dbParams.setTaxonId(params.getTaxon().getTaxonId());
        db2dbParams.setInput(GENE_LOOKUP_ID);
        db2dbParams.setOutputs(GENE_LOOKUP_OUTPUTS);
        db2dbParams.setInputValues(params.getInputValues());
        String results = bioDbNetRemoteService.db2db(db2dbParams);
        CSVReader reader = new CSVReader(new StringReader(results), '\t', '\'', 1);
        try {
            List<String[]> lines = reader.readAll();
            for (String[] nextLine : lines) {
                GeneResults gene = extractGene(nextLine);
                if (!emptyGene(gene)) {
                    retrievedGenes.add(gene);
                }
            }
        } catch (IOException e) {
            LOG.error("Unabled to read dbBioNet gene results.", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return params.isFilterGenesOnStudy() && !retrievedGenes.isEmpty()
                ? filterGenes(retrievedGenes, params) : retrievedGenes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<GeneResults> retrieveGenesByPathway(SearchParameters params) {
        Db2DbParams db2dbParams = new Db2DbParams();
        db2dbParams.setInput("Biocarta Pathway Name");
        db2dbParams.setOutputs("Gene ID");
        db2dbParams.setTaxonId(params.getTaxon().getTaxonId());
        db2dbParams.setInputValues(params.getInputValues());

        String results = bioDbNetRemoteService.db2db(db2dbParams);
        Set<String> geneIds = parseIds(results);

        SearchParameters otherParams = new SearchParameters();
        otherParams.setInputValues(StringUtils.join(geneIds, ','));
        otherParams.setTaxon(params.getTaxon());
        otherParams.setFilterGenesOnStudy(params.isFilterGenesOnStudy());

        return retrieveGenesById(otherParams);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<PathwayResults> retrievePathwaysByGeneSymbols(SearchParameters params) {
        Db2DbParams db2dbParams = new Db2DbParams();
        db2dbParams.setInput("Gene Symbol");
        db2dbParams.setOutputs("Biocarta Pathway Name");
        db2dbParams.setTaxonId(params.getTaxon().getTaxonId());
        db2dbParams.setInputValues(params.getInputValues());

        Set<PathwayResults> pathways = Sets.newTreeSet();
        String results = bioDbNetRemoteService.db2db(db2dbParams);
        CSVReader reader = new CSVReader(new StringReader(results), '\t', '\'', 1);
        try {
            List<String[]> lines = reader.readAll();
            for (String[] nextLine : lines) {
                if (!StringUtils.equals(nextLine[PATHWAY_INFO_INDEX], EMPTY_VALUE)) {
                    String pathwayLine = StringUtils.strip(nextLine[PATHWAY_INFO_INDEX], "\"");
                    pathways.addAll(extractPathways(StringUtils.split(pathwayLine, ';')));
                }
            }
        } catch (IOException e) {
            LOG.error("Unabled to read dbBioNet pathway by gene results.", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return pathways;
    }

    /**
     * Given a label: value string, return the value.
     * @param input the input
     * @return the value
     */
    private String getValue(Matcher matcher) {
        String result = StringUtils.EMPTY;
        if (matcher.find()) {
            String[] matchedValue = StringUtils.split(matcher.group(), ':');
            result = StringUtils.trim(matchedValue[1]);
        }
        return result;
    }

    /**
     * Parses ids from the bioDbNet results.
     * @param bioDbNetResults the bioDbNet results
     * @return the parsed ids
     */
    private Set<String> parseIds(String bioDbNetResults) {
        CSVReader reader = new CSVReader(new StringReader(bioDbNetResults), '\t', '\'', 1);
        Set<String> results = new HashSet<String>();
        try {
            List<String[]> lines = reader.readAll();
            for (String[] nextLine : lines) {
                String[] ids = StringUtils.stripAll(StringUtils.split(nextLine[GENE_ID_LOOKUP_INDEX], ';'));
                for (String id : ids) {
                    if (!StringUtils.equals(EMPTY_VALUE, id)) {
                        results.add(id);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Unabled to read dbBioNet gene id results.", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return results;
    }

    /**
     * Extracts the gene information from a line of bioDbNet gene search results.
     * @param line a gene search result line
     * @return the parsed gene
     */
    private GeneResults extractGene(String[] line) {
        Matcher descrptionMatcher = DESCRIPTION_PATTERN.matcher(line[GENE_INFO_INDEX]);
        Matcher taxonMatcher = TAXON_PATTERN.matcher(line[TAXON_INDEX]);

        GeneResults gene = new GeneResults();
        gene.setSymbol(line[GENE_SYMBOL_INDEX]);
        if (NumberUtils.isNumber(line[GENE_ID_INDEX])) {
            gene.setGeneId(Long.valueOf(line[GENE_ID_INDEX]));
        }
        gene.setDescription(getValue(descrptionMatcher));
        gene.setAliases(StringUtils.trim(StringUtils.replaceChars(line[GENE_SYNONYM_INDEX], ';', ',')));
        gene.setTaxon(getValue(taxonMatcher));
        return gene;
    }

    private Set<PathwayResults> extractPathways(String[] pathwayInfo) {
        Set<PathwayResults> pathways = Sets.newHashSet();

        for (String pathway : pathwayInfo) {
            Matcher titleMatcher = PATHWAY_TITLE_PATTERN.matcher(pathway);
            Matcher nameMatcher = PATHWAY_NAME_PATTERN.matcher(pathway);
            nameMatcher.find();
            PathwayResults result = new PathwayResults();
            result.setName(StringUtils.trim(nameMatcher.group()));
            result.setTitle(getValue(titleMatcher));
            pathways.add(result);
        }
        return pathways;
    }

    /**
     * Determines if the parsed gene is actually devoid of any information.
     * @param gene the gene to assess
     * @return true iff the gene contains no information
     */
    private boolean emptyGene(GeneResults gene) {
        return StringUtils.isEmpty(gene.getSymbol()) || StringUtils.equals(gene.getSymbol(), EMPTY_VALUE);
    }

    private Set<GeneResults> filterGenes(Set<GeneResults> unfilteredGenes, SearchParameters params) {
        Set<GeneResults> filteredGenes = Sets.newTreeSet();
        Set<String> genesSymbols = Sets.newTreeSet();
        for (GeneResults gene : unfilteredGenes) {
            genesSymbols.add(gene.getSymbol());
        }
        Set<String> allowedSymbols = dao.retrieveGeneSymbolsInStudy(genesSymbols, params.getStudy());
        for (GeneResults gene : unfilteredGenes) {
            if (allowedSymbols.contains(gene.getSymbol())) {
                filteredGenes.add(gene);
            }
        }
        return filteredGenes;
    }

    /**
     * Sets the bioDbNet remote service.
     * @param svc the remote service
     */
    @Autowired
    public void setBioDbNetRemoteService(BioDbNetRemoteService svc) {
        this.bioDbNetRemoteService = svc;
    }

    /**
     * @param dao the dao to set
     */
    @Autowired
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }
}
