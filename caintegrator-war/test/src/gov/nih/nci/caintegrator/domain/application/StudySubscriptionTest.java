/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator.domain.genomic.AmplificationTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;

import org.junit.Test;

public class StudySubscriptionTest {

    @Test
    public void testAll() {
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        subscription.getStudy().setStudyConfiguration(studyConfiguration);
        Gene gene1 = new Gene();
        gene1.setSymbol("EGFR");
        Gene gene2 = new Gene();
        gene2.setSymbol("BRAC1");
        GeneList geneList1 = new GeneList();
        geneList1.setName("myGeneList");
        geneList1.getGeneCollection().add(gene1);
        GeneList geneList2 = new GeneList();
        geneList2.setName("global-GeneList");
        geneList2.getGeneCollection().add(gene2);
        subscription.getListCollection().add(geneList1);
        studyConfiguration.getListCollection().add(geneList2);
        // Add Gistic
        GisticAnalysis analysis = createGisticAnalysis(gene1, gene2);
        subscription.getCopyNumberAnalysisCollection().add(analysis);

        assertEquals(4, subscription.getAllGeneListNames().size());
        assertEquals(gene1, subscription.getSelectedGeneList("myGeneList").iterator().next());
        assertEquals(gene2, subscription.getSelectedGeneList("[Global]-global-GeneList").iterator().next());
        assertEquals(gene1, subscription.getSelectedGeneList("[GISTIC-Amplified]-Gistic 1").iterator().next());
        assertEquals(gene2, subscription.getSelectedGeneList("[GISTIC-Deleted]-Gistic 1").iterator().next());
    }

    private GisticAnalysis createGisticAnalysis(Gene ampGene, Gene delGene) {
        GisticAnalysis analysis = new GisticAnalysis();
        analysis.setName("Gistic 1");
        ReporterList reporterList = new ReporterList("Test", ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER);
        GisticGenomicRegionReporter ampReporter = new GisticGenomicRegionReporter();
        ampReporter.setReporterList(reporterList);
        ampReporter.setGeneAmplificationType(AmplificationTypeEnum.AMPLIFIED);
        ampReporter.getGenes().add(ampGene);
        GisticGenomicRegionReporter delReporter = new GisticGenomicRegionReporter();
        delReporter.setReporterList(reporterList);
        delReporter.setGeneAmplificationType(AmplificationTypeEnum.DELETED);
        delReporter.getGenes().add(delGene);
        reporterList.getReporters().add(ampReporter);
        reporterList.getReporters().add(delReporter);
        analysis.setReporterList(reporterList);
        return analysis;
    }
}
