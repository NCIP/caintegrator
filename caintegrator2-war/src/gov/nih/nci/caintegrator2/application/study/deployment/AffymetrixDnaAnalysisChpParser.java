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

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import affymetrix.calvin.data.GenericData;
import affymetrix.calvin.data.ProbeSetMultiDataBase;
import affymetrix.calvin.data.ProbeSetMultiDataCopyNumberData;
import affymetrix.calvin.data.ProbeSetMultiDataGenotypeData;
import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;
import affymetrix.calvin.exception.UnsignedOutOfLimitsException;
import affymetrix.calvin.parameter.ParameterNameValue;
import affymetrix.calvin.parsers.GenericFileReader;
import affymetrix.calvin.parsers.InvalidFileTypeException;
import affymetrix.calvin.parsers.InvalidVersionException;
import affymetrix.fusion.chp.FusionCHPData;
import affymetrix.fusion.chp.FusionCHPDataReg;
import affymetrix.fusion.chp.FusionCHPMultiDataData;

/**
 * Reads data in Affymetrix CHP (CNCHP) files.
 */
class AffymetrixDnaAnalysisChpParser {
    
    private static final String LO2_RATIO = "Log2Ratio";
    private static final String PARAM_CHIP_TYPE = "affymetrix-algorithm-param-ChipType1";
    private static final String PARAM_ARRAY_TYPE = "affymetrix-array-type";
    private final File chpFile;
    private FusionCHPMultiDataData chpData;
    private FusionCHPData fusionChpData;

    private Map<String, AbstractReporter> reporterMap;

    static {
        FusionCHPMultiDataData.registerReader();
    }
    
    /**
     * Creates a new parser for the provided CHP file.
     * 
     * @param chpFile the CNCHP file.
     */
    AffymetrixDnaAnalysisChpParser(File chpFile) {
        this.chpFile = chpFile;
    }

    void parse(ArrayDataValues values, ArrayData arrayData, MultiDataType multiDataType) throws DataRetrievalException {
        loadReporterMap(values);
        try {
            int numProbeSets = getChpData().getEntryCount(multiDataType);
            for (int i = 0; i < numProbeSets; i++) {
                if (MultiDataType.CopyNumberMultiDataType.equals(multiDataType)) {
                    ProbeSetMultiDataCopyNumberData probeSetData = 
                        getChpData().getCopyNumberEntry(multiDataType, i);
                    loadData(probeSetData, values, arrayData);
                } else {
                    ProbeSetMultiDataGenotypeData probeSetData =
                        getChpData().getGenotypeEntry(multiDataType, i);
                    loadData(probeSetData, values, arrayData);
                    
                }
            }
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't retrieve data from " + chpFile.getAbsolutePath(), e);
        } catch (UnsignedOutOfLimitsException e) {
            throw new DataRetrievalException("Couldn't retrieve data from " + chpFile.getAbsolutePath(), e);
        }
    }

    private void loadReporterMap(ArrayDataValues values) {
        reporterMap = new HashMap<String, AbstractReporter>();
        for (AbstractReporter reporter : values.getReporters()) {
            reporterMap.put(reporter.getName(), reporter);
        }
    }

    private void loadData(ProbeSetMultiDataCopyNumberData probeSetData, ArrayDataValues values, ArrayData arrayData) {
        values.setFloatValue(arrayData, 
                getReporter(probeSetData.getName()), 
                ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 
                getLog2Value(probeSetData));
    }

    private void loadData(ProbeSetMultiDataGenotypeData probeSetData, ArrayDataValues values, ArrayData arrayData) {
        values.setFloatValue(arrayData, 
                getReporter(probeSetData.getName()), 
                ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 
                getLog2Value(probeSetData));
    }

    private AbstractReporter getReporter(String name) {
        return reporterMap.get(name);
    }

    private float getLog2Value(ProbeSetMultiDataBase probeSetData) {
        for (ParameterNameValue nameValue : probeSetData.getMetrics()) {
            if (LO2_RATIO.equals(nameValue.getName())) {
                return nameValue.getValueFloat();
            }
        }
        return 0.0f;
    }

    private FusionCHPMultiDataData getChpData() {
        if (chpData == null) {
            chpData = FusionCHPMultiDataData.fromBase(getFusionChpData());            
        }
        return chpData;
    }
    
    private FusionCHPData getFusionChpData() {
        if (fusionChpData == null) {
            fusionChpData = FusionCHPDataReg.read(chpFile.getAbsolutePath());
        }
        return fusionChpData;
    }
    
    String getArrayDesignName() throws DataRetrievalException {
        GenericFileReader reader = new GenericFileReader();
        reader.setFilename(chpFile.getAbsolutePath());
        GenericData data = new GenericData();
        String arrayName;
        String paramName = PARAM_ARRAY_TYPE;
        try {
            reader.readHeader(data, GenericFileReader.ReadHeaderOption.ReadNoDataGroupHeader);
            if (data
                    .getHeader().getGenericDataHdr().findNameValParam(PARAM_CHIP_TYPE) != null) {
                paramName = PARAM_CHIP_TYPE;
                arrayName =  data.getHeader().getGenericDataHdr().findNameValParam(paramName).getValueAscii();
            } else {
                arrayName =  data.getHeader().getGenericDataHdr().findNameValParam(paramName).getValueText();
            }
            reader.close();
            return arrayName;
        } catch (InvalidVersionException e) {
            throw new DataRetrievalException("Couldn't retrieve the array name from the array design file.  "
                    + "Invalid version from " + chpFile.getAbsolutePath(), e);
        } catch (InvalidFileTypeException e) {
            throw new DataRetrievalException("Couldn't retrieve the array name from the array design file.  "
                    + "Invalid file type from " + chpFile.getAbsolutePath(), e);
        } catch (Exception e) {
            throw new DataRetrievalException("Couldn't retrieve the array name from the array design file.  "
                    + "Looking for parameter named " + paramName + ". Local file path is "
                    + chpFile.getAbsolutePath(), e);
        }
    }
   
}
