/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

/**
 * Provides useful methods for working with <code>Platforms</code> and they objects they contain.
 */
public class PlatformHelper {
    private final Platform platform;
    private final Map<ReporterTypeEnum, Map<String, AbstractReporter>> reporterMaps = Maps.newHashMap();
    private final Map<ReporterTypeEnum, Map<Gene, Collection<AbstractReporter>>> geneToReporterMaps = Maps.newHashMap();

    /**
     * Creates a new instance.
     *
     * @param platform the associated array design.
     */
    public PlatformHelper(Platform platform) {
        this.platform = platform;
    }

    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Returns the reporter for the type and name given.
     *
     * @param type the reporter type
     * @param name the reporter name
     * @return the matching reporter.
     */
    public AbstractReporter getReporter(ReporterTypeEnum type, String name) {
        return getReporterMap(type).get(name);
    }

    private Map<String, AbstractReporter> getReporterMap(ReporterTypeEnum type) {
        if (!reporterMaps.containsKey(type)) {
            reporterMaps.put(type, createReporterMap(type));
        }
        return reporterMaps.get(type);
    }

    private Map<String, AbstractReporter> createReporterMap(ReporterTypeEnum type) {
        Map<String, AbstractReporter> reporterMap = new HashMap<String, AbstractReporter>();
        Set<ReporterList> reporterLists = getReporterLists(type);
        if (!reporterLists.isEmpty()) {
            for (ReporterList reporterList : reporterLists) {
                for (AbstractReporter reporter : reporterList.getReporters()) {
                    reporterMap.put(reporter.getName(), reporter);
                }
            }
        }
        return reporterMap;
    }

    /**
     * Returns the reporter set for the reporter type given.
     *
     * @param type return set of this type
     * @return the set.
     */
    public Set<ReporterList> getReporterLists(ReporterTypeEnum type) {
        Set<ReporterList> reporterLists = new HashSet<ReporterList>();
        for (ReporterList reporterList : platform.getReporterLists()) {
            if (type == reporterList.getReporterType()) {
                reporterLists.add(reporterList);
            }
        }
        return reporterLists;
    }

    /**
     * Given a Set of ReporterList objects, returns all AbstractReporter.
     * @param type the reporter type to retrieve reports on for the platform.
     * @return all AbstractReporters associated with reporterLists.
     */
    public List<AbstractReporter> getAllReportersByType(ReporterTypeEnum type) {
        List<AbstractReporter> geneReporters = new ArrayList<AbstractReporter>();
        for (ReporterList reporterList : getReporterLists(type)) {
            geneReporters.addAll(reporterList.getReporters());
        }
        return geneReporters;
    }

    /**
     * Returns all reporters associated to a single gene. For gene level reporters this collection
     * should only contain (at most) one reporter, for probe set reporters this will return multiple
     * reporters.
     *
     * @param gene find reporters associated to this gene.
     * @param type find reporters of this type.
     * @return the reporters for the gene.
     */
    public Collection<AbstractReporter> getReportersForGene(Gene gene, ReporterTypeEnum type) {
        return getGeneToReporterMap(type).get(gene);
    }

    private Map<Gene, Collection<AbstractReporter>> getGeneToReporterMap(ReporterTypeEnum type) {
        Map<Gene, Collection<AbstractReporter>> geneReporterMap = geneToReporterMaps.get(type);
        if (geneReporterMap == null) {
            geneReporterMap = createGeneReporterMap(type);
            geneToReporterMaps.put(type, geneReporterMap);
        }
        return geneReporterMap;
    }

    private Map<Gene, Collection<AbstractReporter>> createGeneReporterMap(ReporterTypeEnum type) {
        Map<Gene, Collection<AbstractReporter>> geneToReporterMap = new HashMap<Gene, Collection<AbstractReporter>>();
        for (ReporterList reporterList : getReporterLists(type)) {
            for (AbstractReporter reporter : reporterList.getReporters()) {
                addToGenesToReporterMap(reporter, geneToReporterMap);
            }
        }
        return geneToReporterMap;
    }

    private void addToGenesToReporterMap(AbstractReporter reporter,
            Map<Gene, Collection<AbstractReporter>> geneToReporterMap) {
        for (Gene gene : reporter.getGenes()) {
            Collection<AbstractReporter> reportersForGene = geneToReporterMap.get(gene);
            if (reportersForGene == null) {
                reportersForGene = new HashSet<AbstractReporter>();
                geneToReporterMap.put(gene, reportersForGene);
            }
            reportersForGene.add(reporter);
        }
    }

}
