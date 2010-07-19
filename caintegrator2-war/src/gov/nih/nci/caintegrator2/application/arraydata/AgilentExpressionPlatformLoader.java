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
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Used to load Agilent array designs.
 */
class AgilentExpressionPlatformLoader extends AbstractExpressionPlatformLoader {
    
    private static final Logger LOGGER = Logger.getLogger(AgilentExpressionPlatformLoader.class);
    
    private static final String PROBE_SET_ID_HEADER = "ProbeID";
    private static final String GENE_SYMBOL_HEADER = "GeneSymbol";
    private static final String GENE_NAME_HEADER = "GeneName";
    private static final String ACCESSIONS_HEADER = "Accessions";
    private static final String NO_GENE_SYMBOL = "";
    private static final String[] REQUIRED_HEADERS = {PROBE_SET_ID_HEADER, GENE_SYMBOL_HEADER, GENE_NAME_HEADER,
        ACCESSIONS_HEADER};

    // ADF file headers
    private static final String ADF_FIRST_FIELD_HEADER = "X_Block";
    private static final String ADF_PROBE_SET_ID_HEADER = "Reporter_ID";
    private static final String ADF_GENE_SYMBOL_HEADER = "Composite";
    private static final String ADF_NO_GENE_SYMBOL = "NOMATCH";
    private static final String[] ADF_REQUIRED_HEADERS = {ADF_FIRST_FIELD_HEADER, ADF_PROBE_SET_ID_HEADER,
        ADF_GENE_SYMBOL_HEADER};

    private String firstFieldHeader;
    private String probeIdHeader;
    private String geneSymbolHeader;
    private String[] requiredHeaders;
    private String noGeneSymbol;
    private final Set<String> probeSetNames;

    AgilentExpressionPlatformLoader(AgilentExpressionPlatformSource source) {
        super(source);
        probeSetNames = new HashSet<String>();
        setFieldHeaders();
    }
    
    @Override
    public String getPlatformName() throws PlatformLoadingException {
        return getSource().getPlatformName();
    }
    
    private void setFieldHeaders() {
        if (((AgilentExpressionPlatformSource) getSource()).getPlatformFileName().endsWith(".adf")) {
            firstFieldHeader = ADF_FIRST_FIELD_HEADER;
            probeIdHeader = ADF_PROBE_SET_ID_HEADER;
            geneSymbolHeader = ADF_GENE_SYMBOL_HEADER;
            noGeneSymbol = ADF_NO_GENE_SYMBOL;
            requiredHeaders = ADF_REQUIRED_HEADERS;
        } else {
            firstFieldHeader = PROBE_SET_ID_HEADER;
            probeIdHeader = PROBE_SET_ID_HEADER;
            geneSymbolHeader = GENE_SYMBOL_HEADER;
            noGeneSymbol = NO_GENE_SYMBOL;
            requiredHeaders = REQUIRED_HEADERS;
        }
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

    void handleAnnotationFile(File annotationFile, Platform platform, CaIntegrator2Dao dao)
    throws PlatformLoadingException {
        try {
            setAnnotationFileReader(new CSVReader(new FileReader(annotationFile), '\t'));
            loadHeaders();
            platform.setName(((AgilentExpressionPlatformSource) getSource()).getPlatformName());
            ReporterList geneReporters = 
                platform.addReporterList(((AgilentExpressionPlatformSource) getSource()).getPlatformName(),
                        ReporterTypeEnum.GENE_EXPRESSION_GENE);
            ReporterList probeSetReporters = 
                platform.addReporterList(((AgilentExpressionPlatformSource) getSource()).getPlatformName(),
                        ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            loadAnnotations(geneReporters, probeSetReporters, dao);
            probeSetReporters.sortAndLoadReporterIndexes();
            geneReporters.sortAndLoadReporterIndexes();
        } catch (IOException e) {
            LOGGER.error("IO Error reading annotation file.");
            throw new PlatformLoadingException("Couldn't read annotation file " + getAnnotationFileNames(), e);
        }
    }

    private void loadHeaders() throws PlatformLoadingException, IOException {
        String[] fields;
        while ((fields = getAnnotationFileReader().readNext()) != null) {
            if (isAnnotationHeadersLine(fields, firstFieldHeader)) {
                loadAnnotationHeaders(fields, requiredHeaders);
                return;
            }
        }        
        throw new PlatformLoadingException("Invalid Agilent annotation file; headers not found in file: " 
                + getAnnotationFileNames());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void loadAnnotations(String[] fields, ReporterList geneReporters, ReporterList probeSetReporters, 
            CaIntegrator2Dao dao) {
        String probeSetName = getAnnotationValue(fields, probeIdHeader);
        if (probeSetName.startsWith("A_") && !probeSetNames.contains(probeSetName)) {
            probeSetNames.add(probeSetName);
            extractGene(probeSetName, fields, geneReporters, probeSetReporters, dao);
        }
    }
    
    private void extractGene(String probeSetName, String[] fields, ReporterList geneReporters,
            ReporterList probeSetReporters, CaIntegrator2Dao dao) {
        String symbol = getAnnotationValue(fields, geneSymbolHeader);
        Gene gene = getSymbolToGeneMap().get(symbol.toUpperCase(Locale.getDefault()));
        if (gene == null && !symbol.equals(noGeneSymbol) && !symbol.matches("^chr\\d+")) {
            gene = lookupOrCreateGene(fields, symbol, dao);
            addGeneReporter(geneReporters, gene);
        }
        Set<Gene> genes = new HashSet<Gene>();
        if (gene != null) {
            genes.add(gene);
        }
        handleProbeSet(probeSetName, genes, probeSetReporters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Gene createGene(String symbol, String[] fields) {
        Gene gene = new Gene();
        gene.setSymbol(symbol.toUpperCase(Locale.getDefault()));
        gene.setGenbankAccession(getAnnotationValue(fields, ACCESSIONS_HEADER));
        gene.setFullName(getAnnotationValue(fields, GENE_NAME_HEADER));
        return gene;
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

}
