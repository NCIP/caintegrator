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

import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * Provides functionality to read NetCDF files.
 */
class NetCDFReader extends AbstractNetCdfFileHandler {
    
    private static final Logger LOGGER = Logger.getLogger(NetCDFReader.class);

    private final DataRetrievalRequest request;
    private NetcdfFile reader;
    private List<List<AbstractReporter>> sequentialReporterLists;

    NetCDFReader(FileManager fileManager, DataRetrievalRequest request) {
        super(fileManager);
        this.request = request;
    }

    ArrayDataValues retrieveValues() {
        try {
            ArrayDataValues values = new ArrayDataValues(request.getReporters());
            if (ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER.equals(request.getReporterType())) {
                openNetCdfFile(request.getStudy(),
                        request.getArrayDatas().iterator().next().getReporterLists().iterator().next().getId(),
                        request.getReporterType());
            } else {
                openNetCdfFile(request.getStudy(), request.getPlatform().getId(), request.getReporterType());
            }
            for (ArrayDataValueType type : request.getTypes()) {
                loadValues(values, type);
            }
            closeNetCdfFile();
            return values;
        } catch (IOException e) {
            throw new ArrayDataStorageException("Couldn't read data file", e);
        } catch (InvalidRangeException e) {
            throw new ArrayDataStorageException("Couldn't read data file", e);
        }
    }

    private void openNetCdfFile(Study study, Long id, ReporterTypeEnum reporterType) throws IOException {
        reader = NetcdfFile.open(getFile(study, id, reporterType).getAbsolutePath());
    }

    private void closeNetCdfFile() throws IOException {
        reader.close();
    }

    private void loadValues(ArrayDataValues values, ArrayDataValueType type) throws IOException, InvalidRangeException {
        Variable variable = reader.findVariable(type.name());
        for (ArrayData arrayData : request.getArrayDatas()) {
            loadValues(values, variable, type, arrayData);
        }
    }

    private void loadValues(ArrayDataValues values, Variable variable, ArrayDataValueType type, ArrayData arrayData) 
    throws IOException, InvalidRangeException {
        if (Float.class.equals(type.getTypeClass())) {
            loadFloatValues(values, variable, type, arrayData);
        } else {
            throw new IllegalStateException("Unsupported data type " + type.getTypeClass().getName());
        }
    }

    private void loadFloatValues(ArrayDataValues values, Variable variable, ArrayDataValueType type, 
            ArrayData arrayData) 
    throws IOException, InvalidRangeException {
        for (List<AbstractReporter> reporters : getSequentialReporterLists()) {
            float[] floatValues = getFloatValues(variable, reporters, getArrayDataOffset(arrayData));
            values.setFloatValues(arrayData, reporters, type, floatValues);
        }
    }

    private Integer getArrayDataOffset(ArrayData arrayData) throws IOException {
        Integer offset = getArrayDataOffsets().get(arrayData.getId());
        if (offset == null) {
            String message = "NetCDF file "
                + getFile(request.getStudy(), request.getPlatform().getId(),
                        request.getReporterType()).getAbsolutePath()
                + " doesn't contain data for ArrayData with id " + arrayData.getId();
            LOGGER.error(message + ". ArrayData offsets: " + getArrayDataOffsets());
            throw new ArrayDataStorageException(message);
        }
        return offset;
    }

    private float[] getFloatValues(Variable variable, List<AbstractReporter> reporters, Integer arrayDataIndex) 
    throws IOException, InvalidRangeException {
        return (float[]) getValuesArray(variable, reporters, arrayDataIndex).get1DJavaArray(Float.class);
    }

    private Array getValuesArray(Variable variable, List<AbstractReporter> reporters, Integer arrayDataIndex) 
    throws IOException, InvalidRangeException {
        int[] origin = new int[2];
        origin[0] = arrayDataIndex;
        origin[1] = reporters.get(0).getDataStorageIndex();
        int[] size = new int[2];
        size[0] = 1;
        size[1] = reporters.size();
        return variable.read(origin, size);
    }

    private List<List<AbstractReporter>> getSequentialReporterLists() {
        List<AbstractReporter> allReporters = request.getReporters();
        if (sequentialReporterLists == null) {
            sequentialReporterLists = new ArrayList<List<AbstractReporter>>();
            int startIndex = 0;
            int endIndex = 0;
            int currentReporterIndex;
            int previousReporterIndex = request.getReporters().get(0).getDataStorageIndex();
            for (int i = 0; i < allReporters.size(); i++) {
                endIndex = i;
                currentReporterIndex = allReporters.get(i).getDataStorageIndex();
                if (currentReporterIndex - previousReporterIndex > 1) {
                    sequentialReporterLists.add(allReporters.subList(startIndex, endIndex));
                    startIndex = endIndex;
                }
                previousReporterIndex = currentReporterIndex;
            }
            sequentialReporterLists.add(allReporters.subList(startIndex, endIndex + 1));
        }
        return sequentialReporterLists;
    }

    @Override
    NetcdfFile getNetCdfFile() {
        return reader;
    }

}
