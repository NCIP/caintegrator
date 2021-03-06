/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Used to load Agilent CopyNumber array designs.
 */
public class AgilentGemlCghPlatformLoader extends AbstractPlatformLoader {

    private static final Logger LOGGER = Logger.getLogger(AgilentGemlCghPlatformLoader.class);

    private static final String NAME_ATTRIBUTE = "name";
    private static final String PROJECT_TAG = "project";
    private static final String REPORTER_TAG = "reporter";
    private static final String GENE_TAG = "gene";
    private static final String OTHER_TAG = "other";
    private static final String DONE = "Done";

    private final GemlSaxParser gemlSaxParser;

    /**
     * Constructor.
     * @param source the platform source
     */
    public AgilentGemlCghPlatformLoader(AbstractPlatformSource source) {
        super(source);
        gemlSaxParser = new GemlSaxParser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Platform load(CaIntegrator2Dao dao) throws PlatformLoadingException {
        Platform platform = createPlatform(PlatformVendorEnum.AGILENT);
        loadAnnotationFiles(platform, dao);
        return platform;
    }

    @Override
    public String getPlatformName() throws PlatformLoadingException {
        return new GemlPlatformNameParser().extractPlatformName(getAnnotationFiles().get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    Logger getLogger() {
        return LOGGER;
    }

    /**
     * Handler for extracting the platform name.
     *
     * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
     */
    class GemlPlatformNameParser extends DefaultHandler {
        private String platformName;

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
    class GemlSaxParser extends DefaultHandler {
        private final Pattern chrPattern = Pattern.compile("^chr.*:\\d+-\\d+");
        private CaIntegrator2Dao myDao;
        private Platform myPlatform;
        private ReporterList reporterList;
        private DnaAnalysisReporter reporter;
        private final Set<String> reporterNames = new HashSet<String>();

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
            reporterList.sortAndLoadReporterIndexes();
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
                reporterList.setGenomeVersion(attributes.getValue("value").split(":")[0]);
            }
        }

        private void processProjectTag(Attributes attributes) {
            myPlatform.setName(attributes.getValue(NAME_ATTRIBUTE));
            reporterList = myPlatform.addReporterList(myPlatform.getName(), ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        }

        private void processReporterTag(Attributes attributes) {
            String reporterName = attributes.getValue(NAME_ATTRIBUTE);
            if (reporterName.startsWith("A_") && !reporterNames.contains(reporterName)) {
                reporterNames.add(reporterName);
                reporter = new DnaAnalysisReporter();
                reporter.setName(reporterName);
                setChromosomeInfo(attributes.getValue("systematic_name"));
                reporterList.addReporter(reporter);
            } else {
                reporter = null;
            }
        }

        private void processGeneTag(Attributes attributes) {
            String symbol = attributes.getValue("primary_name");
            Gene gene = getSymbolToGeneMap().get(symbol);
            if (gene == null) {
                gene = lookupOrCreateGene(null, symbol, myDao);
            }
            if (gene != null) {
                reporter.getGenes().add(gene);
            }
        }

        private void setChromosomeInfo(String chrValue) {
            if (chrPattern.matcher(chrValue).matches()) {
                String[] chrCoords = StringUtils.split(chrValue, ":");
                reporter.setChromosome(StringUtils.remove(chrCoords[0].substring(3), "_random"));
                reporter.setPosition(getStartPos(chrCoords[1]));
            }
        }

        private int getStartPos(String value) {
            return NumberUtils.toInt(StringUtils.split(value, '-')[0]);
        }
    }
}
