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
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    private final Map<String, Gene> symbolToGeneMap = new HashMap<String, Gene>();

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
        return new GemlPlatformNameParser().extractPlatformName(
                getAnnotationFiles().get(0));
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
     *
     */
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
    class GemlSaxParser extends DefaultHandler {

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
            reporterList = myPlatform.addReporterList(myPlatform.getName(),
                    ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        }

        private void processReporterTag(Attributes attributes) {
            String reporterName = attributes.getValue(NAME_ATTRIBUTE);
            if (reporterName.startsWith("A_") && !reporterNames.contains(reporterName)) {
                reporterNames.add(reporterName);
                reporter = new DnaAnalysisReporter();
                reporterList.getReporters().add(reporter);
                reporter.setReporterList(reporterList);
                reporter.setName(reporterName);
                setChromosomeInfo(attributes.getValue("systematic_name"));
            } else {
                reporter = null;
            }
        }

        private void processGeneTag(Attributes attributes) {
            String symbol = attributes.getValue("primary_name");
            Gene gene = symbolToGeneMap.get(symbol);
            if (gene == null) {
                gene = lookupOrCreateGene(null, symbol, myDao);
            }
            if (gene != null) {
                reporter.getGenes().add(gene);
            }
        }

        private void setChromosomeInfo(String chrValue) {
            if (chrValue.matches("^chr.*:\\d+-\\d+")) {
                String[] chrCoords = chrValue.split(":");
                reporter.setChromosome(chrCoords[0].substring(3).replaceAll("_random", ""));
                reporter.setPosition(getStartPos(chrCoords[1]));
            }
        }

        private Integer getStartPos(String value) {
            return Integer.parseInt(value.split("-")[0]);
        }

    }

}
