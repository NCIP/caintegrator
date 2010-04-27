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

import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Transports and provides access to genomic data values.
 */
public class ArrayDataValues {
    
    private final List<AbstractReporter> reporters;
    private Set<ReporterList> reporterLists;
    private final Map<AbstractReporter, Integer> reporterIndexMap = new HashMap<AbstractReporter, Integer>();
    private final Map<ArrayDataValueType, TypeValues> typeValuesMap = new HashMap<ArrayDataValueType, TypeValues>();

    /**
     * Creates a new values object.
     * 
     * @param reporters the values object will contain data for these reporters.
     */
    public ArrayDataValues(List<AbstractReporter> reporters) {
        this.reporters = reporters;
        loadReporterIndexMap();
    }

    private void loadReporterIndexMap() {
        for (int i = 0; i < reporters.size(); i++) {
            reporterIndexMap.put(reporters.get(i), i);
        }
    }

    /**
     * @return the reporters
     */
    public List<AbstractReporter> getReporters() {
        return Collections.unmodifiableList(reporters);
    }
    
    /**
     * Gets log2 value of a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporter the reporter the data value is associated to.
     * @param type the type of data
     * @param channelType the channel type of the data array
     * @return the value.
     */
    public double getLog2Value(ArrayData arrayData, AbstractReporter reporter, ArrayDataValueType type,
            PlatformChannelTypeEnum channelType) {
        return PlatformChannelTypeEnum.TWO_COLOR.equals(channelType)
            ? getFloatValue(arrayData, getReporterIndex(reporter), type)
            : Cai2Util.log2(getFloatValue(arrayData, getReporterIndex(reporter), type));
    }
    
    /**
     * Gets a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporter the reporter the data value is associated to.
     * @param type the type of data
     * @return the value.
     */
    public float getFloatValue(ArrayData arrayData, AbstractReporter reporter, ArrayDataValueType type) {
        return getFloatValue(arrayData, getReporterIndex(reporter), type);
    }
    
    /**
     * Gets a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporterIndex the index of the reporter the data value is associated to.
     * @param type the type of data
     * @return the value.
     */
    public float getFloatValue(ArrayData arrayData, int reporterIndex, ArrayDataValueType type) {
        return getTypeValues(type).getFloatValue(arrayData, reporterIndex);
    }

    /**
     * Gets all data for an array / type combination.
     * 
     * @param arrayData the array data the data are associated to.
     * @param type the type of data
     * @return the values.
     */
    public float[] getFloatValues(ArrayData arrayData, ArrayDataValueType type) {
        return getTypeValues(type).getFloatValues(arrayData);
    }

    /**
     * Sets a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporter the reporter the data value is associated to.
     * @param type the type of data
     * @param value the value to set.
     */
    public void setFloatValue(ArrayData arrayData, AbstractReporter reporter, ArrayDataValueType type, float value) {
        getTypeValues(type).setFloatValue(arrayData, reporter, value);
    }

    /**
     * Sets a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data values are associated to.
     * @param forReporters the reporters the data values are associated to.
     * @param type the type of data
     * @param values the values to set.
     */
    public void setFloatValues(ArrayData arrayData, List<AbstractReporter> forReporters, ArrayDataValueType type,
            float[] values) {
        getTypeValues(type).setFloatValues(arrayData, forReporters, values);
    }

    private TypeValues getTypeValues(ArrayDataValueType type) {
        if (type == null) {
            throw new IllegalArgumentException("type was null");
        }
        TypeValues typeValues = typeValuesMap.get(type);
        if (typeValues == null) {
            typeValues = new TypeValues(type, this);
            typeValuesMap.put(type, typeValues);
        }
        return typeValues;
    }

    int getReporterIndex(AbstractReporter reporter) {
        if (reporter == null) {
            throw new IllegalArgumentException("reporter was null");
        }
        return reporterIndexMap.get(reporter);
    }
    
    /**
     * Returns the data types in this values object.
     * 
     * @return the data types.
     */
    public Set<ArrayDataValueType> getTypes() {
        return typeValuesMap.keySet();
    }
    
    /**
     * Returns a set of all <code>ArrayDatas</code> that have data in this values object.
     * 
     * @return the arrays.
     */
    public Set<ArrayData> getArrayDatas() {
        if (typeValuesMap.isEmpty()) {
            return Collections.emptySet();
        } else {
            return typeValuesMap.values().iterator().next().getArrayDatas();
        }
    }

    /**
     * @return the array datas in order by id.
     */
    public List<ArrayData> getOrderedArrayDatas() {
        List<ArrayData> arrayDatas = new ArrayList<ArrayData>();
        arrayDatas.addAll(getArrayDatas());
        Collections.sort(arrayDatas, AbstractCaIntegrator2Object.ID_COMPARATOR);
        return arrayDatas;
    }

    /**
     * Returns the <code>ReporterList</code> that data in this values object is associated with.
     * 
     * @return the reporter list.
     */
    public ReporterList getReporterList() {
        if (getReporters().isEmpty()) {
            return null;
        } else {
            return getReporters().iterator().next().getReporterList();
        }
    }

    /**
     * Returns all reporter lists for reporters in this values object.
     * 
     * @return the reporter lists.
     */
    public Set<ReporterList> getReporterLists() {
        if (reporterLists == null) {
            reporterLists = new HashSet<ReporterList>();
            for (AbstractReporter reporter : reporters) {
                reporterLists.add(reporter.getReporterList());
            }
        }
        return reporterLists;
    }

}
