/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;

import java.io.IOException;
import java.util.Set;

/**
 * Base class for platform loaders.
 */
abstract class AbstractExpressionPlatformLoader extends AbstractPlatformLoader {

    AbstractExpressionPlatformLoader(AbstractPlatformSource source) {
        super(source);
    }

    abstract void loadAnnotations(String[] fields, ReporterList geneReporters, ReporterList probeSetReporters, 
            CaIntegrator2Dao dao);

    protected void loadAnnotations(ReporterList geneReporters, ReporterList probeSetReporters, CaIntegrator2Dao dao) 
    throws IOException {
        String[] fields;
        while ((fields = getAnnotationFileReader().readNext()) != null) {
            loadAnnotations(fields, geneReporters, probeSetReporters, dao);
        }
    }

    protected void addGeneReporter(ReporterList geneReporters, Gene gene) {
        GeneExpressionReporter geneReporter = new GeneExpressionReporter();
        geneReporter.getGenes().add(gene);
        geneReporter.setName(gene.getSymbol());
        geneReporter.setReporterList(geneReporters);
        geneReporters.getReporters().add(geneReporter);
    }

    protected void handleProbeSet(String probeSetName, Set<Gene> genes, ReporterList probeSetReporters) {
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.setName(probeSetName);
        reporter.getGenes().addAll(genes);
        reporter.setReporterList(probeSetReporters);
        probeSetReporters.getReporters().add(reporter);
    }

}
