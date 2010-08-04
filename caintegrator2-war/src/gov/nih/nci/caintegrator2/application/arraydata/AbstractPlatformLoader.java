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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Base class for platform loaders.
 */
public abstract class AbstractPlatformLoader {
    
    
    private final Map<String, Gene> symbolToGeneMap = new HashMap<String, Gene>();
    private final Map<String, Integer> headerToIndexMap = new HashMap<String, Integer>();

    private CSVReader annotationFileReader;
    private final AbstractPlatformSource source;

    AbstractPlatformLoader(AbstractPlatformSource source) {
        this.source = source;
    }
    
    /**
     * 
     * @return the platform name
     * @throws PlatformLoadingException when error parsing the annotation file
     */
    public abstract String getPlatformName() throws PlatformLoadingException;

    abstract Platform load(CaIntegrator2Dao dao) throws PlatformLoadingException;

    /**
     * 
     * @param symbol to set
     * @param fields input fields
     * @return a new gene
     */
    protected Gene createGene(String symbol, String[] fields) {
        Gene gene = new Gene();
        gene.setSymbol(symbol.toUpperCase(Locale.getDefault()));
        return gene;
    }

    /**
     * Create platform and set vendor.
     * @param platformVendor the vendor to set.
     * @return the platform
     */
    protected Platform createPlatform(PlatformVendorEnum platformVendor) {
        Platform platform = new Platform();
        platform.setVendor(platformVendor);
        return platform;
    }
    
    /**
     * Load the annotation file for the platform.
     * @param platform the platform
     * @param dao the caIntegrator2Dao
     * @throws PlatformLoadingException when error loading the annotation file.
     */
    protected void loadAnnotationFiles(Platform platform, CaIntegrator2Dao dao) throws PlatformLoadingException {
        try {
            for (File annotationFile : getAnnotationFiles()) {
                handleAnnotationFile(annotationFile, platform, dao);
            }
            dao.save(platform);
        } finally {
            closeAnnotationFileReader();
            cleanUp();
        }
    }
    
    abstract void handleAnnotationFile(File annotationFile, Platform platform, CaIntegrator2Dao dao)
    throws PlatformLoadingException;

    /**
     * 
     * @param headers the mapping header
     * @param requiredHeaders the headers to validate
     * @throws PlatformLoadingException when headers don't match
     */
    protected void loadAnnotationHeaders(String[] headers, String[] requiredHeaders)
    throws PlatformLoadingException {
        for (int i = 0; i < headers.length; i++) {
            headerToIndexMap.put(headers[i], i);
        }
        validate(requiredHeaders);
    }
    
    private void validate(String[] requiredHeaders) throws PlatformLoadingException {
        for (String header : requiredHeaders) {
            if (!headerToIndexMap.containsKey(header)) {
                throw new PlatformLoadingException("Invalid file format; header '" + header + "' is missing");
            }
        }
    }

    /**
     * 
     * @param fields filed array
     * @param firstField the first field
     * @return true or false
     */
    protected boolean isAnnotationHeadersLine(String[] fields, String firstField) {
        return fields.length > 0 && firstField.equals(fields[0]);
    }

    /**
     * 
     * @param fields field array
     * @param symbol gene symbol
     * @param dao the caIntegratorDao
     * @return the gene
     */
    protected Gene lookupOrCreateGene(String[] fields, String symbol, CaIntegrator2Dao dao) {
        Gene gene = dao.getGene(symbol);
        if (gene == null) {
            gene = createGene(symbol, fields);
        }
        getSymbolToGeneMap().put(gene.getSymbol(), gene);
        return gene;
    }

    /**
     * 
     * @param fields field array
     * @param header the header to get the value for
     * @return the value
     */
    protected String getAnnotationValue(String[] fields, String header) {
        if (getHeaderToIndexMap().containsKey(header)) {
            return fields[getHeaderToIndexMap().get(header)];
        }
        return "";
    }

    /**
     * 
     * @param fields field array
     * @param header the header
     * @param noValueSymbol the no value string
     * @return the value
     */
    protected String getAnnotationValue(String[] fields, String header, String noValueSymbol) {
        String value = fields[getHeaderToIndexMap().get(header)];
        return (value == null || value.equals(noValueSymbol)) ? null : value;
    }

    /**
     * Clean up at the end.
     */
    protected void cleanUp() {
        if (getSource().getDeleteFileOnCompletion()) {
            for (File file : getSource().getAnnotationFiles()) {
                file.delete();
            }
        }
    }

    /**
     * Close all files.
     */
    protected void closeAnnotationFileReader() {
        if (annotationFileReader != null) {
            try {
                annotationFileReader.close();
            } catch (IOException e) {
                getLogger().error("Couldn't close annotation file reader for file " 
                        + getAnnotationFileNames());
            }
        }
    }
    abstract Logger getLogger();

    CSVReader getAnnotationFileReader() {
        return annotationFileReader;
    }

    void setAnnotationFileReader(CSVReader annotationFileReader) {
        this.annotationFileReader = annotationFileReader;
    }

    Map<String, Gene> getSymbolToGeneMap() {
        return symbolToGeneMap;
    }

    Map<String, Integer> getHeaderToIndexMap() {
        return headerToIndexMap;
    }

    AbstractPlatformSource getSource() {
        return source;
    }

    final List<File> getAnnotationFiles() {
        return getSource().getAnnotationFiles();
    }

    final String getAnnotationFileNames() {
        return getSource().getAnnotationFileNames();
    }

}
