/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Used to load Agilent Gene Expression array designs.
 */
class AgilentExpressionXmlPlatformLoader extends AbstractPlatformLoader {
    
    private static final Logger LOGGER = Logger.getLogger(AgilentExpressionPlatformLoader.class);

    private static final String NAME_ATTRIBUTE = "name";
    private static final String PROJECT_TAG = "project";
    private static final String REPORTER_TAG = "reporter";
    private static final String GENE_TAG = "gene";
    private static final String OTHER_TAG = "other";
    private static final String DONE = "Done";
    
    private final GemlSaxParser gemlSaxParser;

    AgilentExpressionXmlPlatformLoader(AgilentExpressionPlatformSource source) {
        super(source);
        gemlSaxParser = new GemlSaxParser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Platform load(CaIntegrator2Dao dao) throws PlatformLoadingException {
        Platform platform = createPlatform(PlatformVendorEnum.AGILENT);
        loadAnnotationFiles(platform, dao);
        return platform;
    }
    
    @Override
    public String getPlatformName() throws PlatformLoadingException {
        return new GemlPlatformNameParser().extractPlatformName(
                getAnnotationFiles().get(0));
    }

    void handleAnnotationFile(File annotationFile, Platform platform, CaIntegrator2Dao dao)
    throws PlatformLoadingException {
        try {
            gemlSaxParser.parseFile(annotationFile, platform, dao);
        } catch (SAXException se) {
            LOGGER.info("Error parsing annotation file: " + annotationFile.getAbsolutePath());
            throw new PlatformLoadingException("Error parsing annotation file", se);
        } catch (ParserConfigurationException pce) {
            LOGGER.info("Error configuring SAX parser.");
            throw new PlatformLoadingException("Error configuring SAX parser", pce);
        } catch (IOException ie) {
            LOGGER.info("IO Error on annotation file: " + annotationFile.getAbsolutePath());
            throw new PlatformLoadingException("I/O Error on annotation file", ie);
        }
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }
    
    /**
     * 
     */
    @SuppressWarnings("PMD.CyclomaticComplexity")   // SAX parser event
    class GemlPlatformNameParser extends DefaultHandler {
        
        private String platformName = null;

        /**
         * Parse the GEML XML file to extract the platform name.
         * @param xmlFile the input XML file.
         * @throws PlatformLoadingException 
         * @exception when error parsing the annotation file.
         */
        String extractPlatformName(File xmlFile)
        throws PlatformLoadingException {
            String errorMsg = "Error parsing GEML file.";
            try {
                // get a factory
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();

                // Ignore the DTD declaration
                final XMLReader parser = sp.getXMLReader();
                parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                parser.setFeature("http://xml.org/sax/features/validation", false);

                sp.parse(xmlFile, this);

            } catch (SAXException e) {
                if (DONE.equalsIgnoreCase(e.getMessage())) {
                    return platformName;
                }
                errorMsg = "Platform name not found.";
                LOGGER.error(errorMsg);
            } catch (ParserConfigurationException e) {
                errorMsg = "Error configurating SAX parser.";
                LOGGER.error(errorMsg);
            } catch (IOException e) {
                errorMsg = "IO Error: " + e.getMessage();
                LOGGER.error(errorMsg);
            }
            throw new PlatformLoadingException(errorMsg);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
            if (qName.equalsIgnoreCase(PROJECT_TAG)) {
                platformName = attributes.getValue(NAME_ATTRIBUTE);
                throw new SAXException(DONE);
            }
        }
    }
    
    /**
     * 
     */
    @SuppressWarnings("PMD.CyclomaticComplexity")   // SAX parser event
    class GemlSaxParser extends DefaultHandler {

        private CaIntegrator2Dao myDao;
        private Platform myPlatform;
        private ReporterList geneReporters;
        private ReporterList probeSetReporters;
        private GeneExpressionReporter reporter;
        private final Set<String> probeSetNames = new HashSet<String>();
        
        /**
         * Parse the GEML XML file.
         * @param xmlFile the input XML file.
         * @param dao the CaIntegrator2Dao.
         * @throws SAXException 
         * @throws ParserConfigurationException 
         * @throws IOException 
         * @exception when error parsing the annotation file.
         */
        void parseFile(File xmlFile, Platform platform, CaIntegrator2Dao dao)
        throws ParserConfigurationException, SAXException, IOException {
            myPlatform = platform;
            myDao = dao;
            
            //get a factory
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            
            //Ignore the DTD declaration
            final XMLReader parser = sp.getXMLReader();
            parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            parser.setFeature("http://xml.org/sax/features/validation", false);
            
            sp.parse(xmlFile, this);
            probeSetReporters.sortAndLoadReporterIndexes();
                
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("PMD.CyclomaticComplexity")   // SAX parser event
        public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
            if (qName.equalsIgnoreCase(PROJECT_TAG)) {
                processProjectTag(attributes);
            } else if (qName.equalsIgnoreCase(REPORTER_TAG)) {
                processReporterTag(attributes);
            } else if (qName.equalsIgnoreCase(GENE_TAG) && reporter != null) {
                processGeneTag(attributes);
            } else if (qName.equalsIgnoreCase(OTHER_TAG)
                    && attributes.getValue(NAME_ATTRIBUTE).equalsIgnoreCase("genomic_build")) {
                geneReporters.setGenomeVersion(attributes.getValue("value").split(":")[0]);
                probeSetReporters.setGenomeVersion(geneReporters.getGenomeVersion());
            }
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public void endElement(String uri, String localName, String qName)
        throws SAXException {
            if (qName.equalsIgnoreCase(PROJECT_TAG)) {
                geneReporters.sortAndLoadReporterIndexes();
                probeSetReporters.sortAndLoadReporterIndexes();
            }
        }

        private void processProjectTag(Attributes attributes) {
            myPlatform.setName(attributes.getValue(NAME_ATTRIBUTE));
            geneReporters =  myPlatform.addReporterList(myPlatform.getName(),
                        ReporterTypeEnum.GENE_EXPRESSION_GENE);
            probeSetReporters = myPlatform.addReporterList(myPlatform.getName(),
                    ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            
        }
        
        private void processReporterTag(Attributes attributes) {
            String probeSetName = attributes.getValue(NAME_ATTRIBUTE);
            if (probeSetName.startsWith("A_") && !probeSetNames.contains(probeSetName)) {
                probeSetNames.add(probeSetName);
                reporter = new GeneExpressionReporter();
                probeSetReporters.getReporters().add(reporter);
                reporter.setReporterList(probeSetReporters);
                reporter.setName(probeSetName);
            } else {
                reporter = null;
            }
        }
        
        private void processGeneTag(Attributes attributes) {
            String symbol = attributes.getValue("primary_name");
            Gene gene = getSymbolToGeneMap().get(symbol);
            if (gene == null) {
                gene = lookupOrCreateGene(null, symbol, myDao);
                addGeneReporter(gene);
            }
            Set<Gene> genes = new HashSet<Gene>();
            if (gene != null) {
                genes.add(gene);
            }
            reporter.getGenes().addAll(genes);
        }

        private void addGeneReporter(Gene gene) {
            GeneExpressionReporter geneReporter = new GeneExpressionReporter();
            geneReporter.getGenes().add(gene);
            geneReporter.setName(gene.getSymbol());
            geneReporter.setReporterList(geneReporters);
            geneReporters.getReporters().add(geneReporter);
        }

    }

}
