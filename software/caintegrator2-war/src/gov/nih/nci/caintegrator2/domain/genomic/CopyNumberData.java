/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Used as input for retrieving segmentation data from Bioconductor.
 */
public class CopyNumberData implements Serializable {
 
    private static final long serialVersionUID = 1L;
    
    private final List<DnaAnalysisReporter> reporters;
    private final Map<ArrayData, float[]> dataToValuesMap = new HashMap<ArrayData, float[]>();
    
    /**
     * Instantiates a new <code>CopyNumberData</code> object.
     * 
     * @param reporters data will be associated with these reporters.
     */
    public CopyNumberData(List<DnaAnalysisReporter> reporters) {
        this.reporters = reporters;
    }
    
    /**
     * Add log2 values for the given array data. Values are in the same order as
     * the reporters used to initialize the <code>CopyNumberData</code> object.
     * 
     * @param arrayData values are for this data.
     * @param copyNumberValues the data values.
     */
    public void addCopyNumberData(ArrayData arrayData, float[] copyNumberValues) {
        dataToValuesMap.put(arrayData, copyNumberValues);
    }

    /**
     * @return all reporters, ordered.
     */
    public List<DnaAnalysisReporter> getReporters() {
        return reporters;
    }
    
    /**
     * @return all array datas.
     */
    public Set<ArrayData> getArrayDatas() {
        return dataToValuesMap.keySet();
    }
    
    /**
     * @param arrayData get values for this array data.
     * @return the values.
     */
    public float[] getValues(ArrayData arrayData) {
        return dataToValuesMap.get(arrayData);
    }

}
