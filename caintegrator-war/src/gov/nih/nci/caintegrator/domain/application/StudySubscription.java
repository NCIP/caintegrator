/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.domain.analysis.AbstractCopyNumberAnalysis;
import gov.nih.nci.caintegrator.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator.domain.genomic.AmplificationTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public class StudySubscription extends AbstractCaIntegrator2StudyObject {

    private static final long serialVersionUID = 1L;

    private static final String GLOBAL_GENE_LIST_PREFIX = "[Global]-";
    private static final Pattern GLOBAL_PATTERN = Pattern.compile("^\\[Global\\]-(.+)");
    private static final String GISTIC_AMP_GENE_LIST_PREFIX = "[GISTIC-Amplified]-";
    private static final Pattern GISTIC_AMP_PATTERN = Pattern.compile("^\\[GISTIC-Amplified\\]-(.+)");
    private static final String GISTIC_DEL_GENE_LIST_PREFIX = "[GISTIC-Deleted]-";
    private static final Pattern GISTIC_DEL_PATTERN = Pattern.compile("^\\[GISTIC-Deleted\\]-(.+)");
    
    private Study study;
    private Set<Query> queryCollection = new HashSet<Query>();
    private Set<AbstractPersistedAnalysisJob> analysisJobCollection = new HashSet<AbstractPersistedAnalysisJob>();
    private Set<AbstractCopyNumberAnalysis> copyNumberAnalysisCollection = new HashSet<AbstractCopyNumberAnalysis>();
    private UserWorkspace userWorkspace;
    private Boolean publicSubscription = false;
    
    /**
     * @return the study
     */
    public Study getStudy() {
        return study;
    }
    
    /**
     * @param study the study to set
     */
    public void setStudy(Study study) {
        this.study = study;
    }
    
    /**
     * @return the queryCollection
     */
    public Set<Query> getQueryCollection() {
        return queryCollection;
    }
    
    /**
     * @param queryCollection the queryCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setQueryCollection(Set<Query> queryCollection) {
        this.queryCollection = queryCollection;
    }

    /**
     * @return the userWorkspace
     */
    public UserWorkspace getUserWorkspace() {
        return userWorkspace;
    }

    /**
     * @param userWorkspace the userWorkspace to set
     */
    public void setUserWorkspace(UserWorkspace userWorkspace) {
        this.userWorkspace = userWorkspace;
    }

    /**
     * @return the analysisJobCollection
     */
    public Set<AbstractPersistedAnalysisJob> getAnalysisJobCollection() {
        return analysisJobCollection;
    }

    /**
     * @param analysisJobCollection the analysisJobCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setAnalysisJobCollection(Set<AbstractPersistedAnalysisJob> analysisJobCollection) {
        this.analysisJobCollection = analysisJobCollection;
    }
    
    /**
     * @return the copyNumberAnalysisCollection
     */
    public Set<AbstractCopyNumberAnalysis> getCopyNumberAnalysisCollection() {
        return copyNumberAnalysisCollection;
    }

    /**
     * @param copyNumberAnalysisCollection the copyNumberAnalysisCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setCopyNumberAnalysisCollection(Set<AbstractCopyNumberAnalysis> copyNumberAnalysisCollection) {
        this.copyNumberAnalysisCollection = copyNumberAnalysisCollection;
    }

    /**
     * @return a list of all gene lists including global lists
     */
    public List<GeneList> getAllGeneLists() {
        List<GeneList> resultList = new ArrayList<GeneList>();
        resultList.addAll(getGeneLists());
        resultList.addAll(getStudy().getStudyConfiguration().getGeneLists());
        return resultList;
    }
    
    /**
     * @return a list of all gene list names including global lists and GISTIC Amp & Del lists
     */
    public List<String> getAllGeneListNames() {
        List<String> resultList = new ArrayList<String>();
        resultList.addAll(getGeneListNames());
        for (String name : getStudy().getStudyConfiguration().getGeneListNames()) {
            resultList.add(GLOBAL_GENE_LIST_PREFIX + name);
        }
        for (String name : getGisticAnalysisNames()) {
            resultList.add(GISTIC_AMP_GENE_LIST_PREFIX + name);
            resultList.add(GISTIC_DEL_GENE_LIST_PREFIX + name);
        }
        return resultList;
    }
    
    /**
     * 
     * @return all GISTIC analysis names
     */
    public List<String> getGisticAnalysisNames() {
        List<String> names = new ArrayList<String>();
        for (AbstractCopyNumberAnalysis analysis : getCopyNumberAnalysisCollection()) {
            if (analysis instanceof GisticAnalysis) {
                names.add(analysis.getName());
            }
        }
        return names;
    }

    /**
     * Parse the gene list name from the given name including prefix then retrieve the gene list.
     * @param name then gene list name with prefix
     * @return The gene list
     */
    public Collection<Gene> getSelectedGeneList(String name) {
        if (name != null) {
            Matcher globalNameMatcher = GLOBAL_PATTERN.matcher(name);
            Matcher gisticAmpNameMatcher = GISTIC_AMP_PATTERN.matcher(name);
            Matcher gisticDelNameMatcher = GISTIC_DEL_PATTERN.matcher(name);
            GeneList geneList;
            if (globalNameMatcher.find()) {
                geneList = getStudy().getStudyConfiguration().getGeneList(globalNameMatcher.group(1));
            } else if (gisticAmpNameMatcher.find()) {
                return getGisticGenes(gisticAmpNameMatcher.group(1), AmplificationTypeEnum.AMPLIFIED);
            } else if (gisticDelNameMatcher.find()) {
                return getGisticGenes(gisticDelNameMatcher.group(1), AmplificationTypeEnum.DELETED);
            } else {
                geneList = getGeneList(name);
            }
            return geneList.getGeneCollection();
        }
        return null;
    }

    private Set<Gene>  getGisticGenes(String gisticAnalysisName, AmplificationTypeEnum type) {
        Set<Gene> resultGenes = new HashSet<Gene>();
        for (AbstractCopyNumberAnalysis analysis : getCopyNumberAnalysisCollection()) {
            if (analysis instanceof GisticAnalysis
                    && analysis.getName().equalsIgnoreCase(gisticAnalysisName)) {
                List<Gene> amplifiedGenes = new ArrayList<Gene>();
                List<Gene> deletedGenes = new ArrayList<Gene>();
                Cai2Util.retrieveGisticAmplifiedDeletedGenes((GisticAnalysis) analysis, amplifiedGenes, deletedGenes);
                if (AmplificationTypeEnum.AMPLIFIED.equals(type)) {
                    resultGenes.addAll(amplifiedGenes);
                } else {
                    resultGenes.addAll(deletedGenes);
                }
                break;
            }
        }
        return resultGenes;
    }

    /**
     * @return the publicSubscription
     */
    public Boolean isPublicSubscription() {
        return publicSubscription;
    }

    /**
     * @param publicSubscription the publicSubscription to set
     */
    public void setPublicSubscription(Boolean publicSubscription) {
        this.publicSubscription = publicSubscription == null ? false : publicSubscription;
    }
}
