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
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
@Transactional (propagation = Propagation.REQUIRED)
public abstract class AbstractAffymetrixDnaAnalysisMappingFileHandler
    extends AbstractUnparsedSupplementalMappingFileHandler {

    private final Map<Sample, List<String>> sampleToFilenamesMap = new HashMap<Sample, List<String>>();
    
    AbstractAffymetrixDnaAnalysisMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }
    
    List<ArrayDataValues> loadArrayData() throws DataRetrievalException, ConnectionException, ValidationException {
        try {
            CSVReader reader = new CSVReader(new FileReader(getMappingFile()));
            String[] fields;
            while ((fields = Cai2Util.readDataLine(reader)) != null) {
                String subjectId = fields[0].trim();
                String sampleName = fields[1].trim();
                String dnaAnalysisFilename = fields[2].trim();
                mappingSample(subjectId, sampleName, dnaAnalysisFilename);
            }
            List<ArrayDataValues> arrayDataValues = loadArrayDataValues();
            getDao().save(getGenomicSource().getStudyConfiguration());
            reader.close();
            return arrayDataValues;
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("DNA analysis mapping file not found: ", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read DNA analysis mapping file: ", e);
        }
    }

    private void mappingSample(String subjectIdentifier, String sampleName, String dnaAnalysisFilename) 
    throws ValidationException, FileNotFoundException {
        Sample sample = getSample(sampleName, subjectIdentifier);
        addMappingFile(sample, dnaAnalysisFilename);
    }

    private void addMappingFile(Sample sample, String dnaAnalysisFilename) {
        List<String> filenames = sampleToFilenamesMap.get(sample);
        if (filenames == null) {
            filenames = new ArrayList<String>();
            sampleToFilenamesMap.put(sample, filenames);
        }
        filenames.add(dnaAnalysisFilename);
    }

    List<ArrayDataValues> loadArrayDataValues() 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<ArrayDataValues> values = new ArrayList<ArrayDataValues>();
        for (Sample sample : sampleToFilenamesMap.keySet()) {
            values.add(loadArrayDataValues(sample));
        }
        return values;
    }

    private ArrayDataValues loadArrayDataValues(Sample sample) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<File> dataFiles = new ArrayList<File>();
        try {
            for (String filename : sampleToFilenamesMap.get(sample)) {
                validateDataFileExtension(filename);
                dataFiles.add(getDataFile(filename));
            }
            return loadArrayDataValues(sample, dataFiles);
        } finally {
            for (File file : dataFiles) {
                doneWithFile(file);
            }
        }
    }

    private void validateDataFileExtension(String filename) throws ValidationException {
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        if (!getFileType().equalsIgnoreCase(extension)) {
            throw new ValidationException("Data file must be '." + getFileType() + "' type instead of: " + filename);
        }
    }

    private ArrayDataValues loadArrayDataValues(Sample sample, List<File> chpFiles) 
    throws DataRetrievalException, ValidationException {
        List<AffymetrixDnaAnalysisChpParser> parsers = new ArrayList<AffymetrixDnaAnalysisChpParser>();
        Set<String> reporterListNames = new HashSet<String>();
        for (File chpFile : chpFiles) {
            AffymetrixDnaAnalysisChpParser parser = new AffymetrixDnaAnalysisChpParser(chpFile);
            parsers.add(parser);
            reporterListNames.add(parser.getArrayDesignName());
        }
        Platform platform = getPlatform(reporterListNames);
        return loadArrayDataValues(sample, platform, parsers);
    }

    private ArrayDataValues loadArrayDataValues(Sample sample, Platform platform, 
            List<AffymetrixDnaAnalysisChpParser> parsers) throws DataRetrievalException {
        PlatformHelper helper = new PlatformHelper(platform);
        Set<ReporterList> reporterLists = helper.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        ArrayData arrayData = createArrayData(sample, reporterLists);
        getDao().save(arrayData);
        ArrayDataValues values = 
            new ArrayDataValues(helper.getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
        for (AffymetrixDnaAnalysisChpParser parser : parsers) {
            parser.parse(values, arrayData, getDataType());
        }
        getArrayDataService().save(values);
        return values;
    }

    /**
     * @return the datatype
     */
    abstract MultiDataType getDataType();

}
