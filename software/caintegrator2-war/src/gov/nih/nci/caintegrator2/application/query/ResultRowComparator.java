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
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.SortTypeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * A comparator class for Result Rows, which can only be instantiated by calling the static function
 * sort().  The Sort takes in a collection of rows to sort and a list of columns that need to be sorted
 * upon and based on that it sorts the rows and returns it in a List form.
 */
final class ResultRowComparator implements Comparator <ResultRow> {

    private static final int EQUAL        = 0;
    private static final int LESS_THAN    = -1;
    private static final int GREATER_THAN = 1;

    private final List <ResultColumn> sortColumns;

    /**
     * Private constructor which takes in a list of columns to sort on.
     * @param sortColumns Columns to sort the rows by.
     */
    private ResultRowComparator(List<ResultColumn> sortColumns) {
        this.sortColumns = sortColumns;
    }

    /**
     * Static sort method that is a wrapper for Collections.sort().
     * @param rowsToSort Rows to sort.
     * @param sortColumns Ordered list of columns to sort the rows by.
     * @return List of rows that are sorted.
     */
    static List<ResultRow> sort(Collection<ResultRow> rowsToSort, List<ResultColumn> sortColumns) {
        for (ResultColumn col : sortColumns) {
            if (col.getSortType() == null) {  // Check sort types and if not there assume it's ascending.
                col.setSortType(SortTypeEnum.ASCENDING);
            }
        }
        List<ResultRow> rowsList = Arrays.asList(rowsToSort.toArray(new ResultRow[rowsToSort.size()]));
        Collections.sort(rowsList, new ResultRowComparator(sortColumns));
        int rowNumber = 0;
        for (ResultRow row : rowsList) {
            rowNumber++;
            row.setRowIndex(rowNumber);
        }
        return rowsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(ResultRow row1, ResultRow row2) {
        Iterator <ResultColumn> sortColumnsIterator = sortColumns.iterator();
        ResultColumn currentColumn = sortColumnsIterator.next();
        ResultValue resultValue1 = getResultValueFromRowColumn(row1, currentColumn);
        ResultValue resultValue2 = getResultValueFromRowColumn(row2, currentColumn);
        int order = compareResultValues(resultValue1,
                                        resultValue2,
                                        currentColumn.getSortType());
        if (order == 0 && sortColumnsIterator.hasNext()) { // Tie-breaker check, goes to next sort column
            List <ResultColumn> newColumns = new ArrayList<ResultColumn>();
            newColumns.addAll(sortColumns);
            List <ResultRow> newRows = new ArrayList<ResultRow>();
            newRows.add(row1);
            newRows.add(row2);
            newColumns.remove(0);
            List<ResultRow> sortedRows = ResultRowComparator.sort(newRows, newColumns);
            if (sortedRows.iterator().next() == row1) { // row1 is first
                return -1;
            } else {
                return 1;
            }
        } else {
            return order;
        }
    }

    private ResultValue getResultValueFromRowColumn(ResultRow row, ResultColumn currentColumn) {
        for (ResultValue value : row.getValueCollection()) {
            if (value.getColumn() == currentColumn) {
                return value;
            }
        }
        return null;
    }

    private int compareResultValues(ResultValue rv1, ResultValue rv2, SortTypeEnum sortType) {
        AbstractAnnotationValue value1 = rv1.getValue();
        AbstractAnnotationValue value2 = rv2.getValue();

        if (value1 instanceof NumericAnnotationValue || value2 instanceof NumericAnnotationValue) {
            return handleNumericValues((NumericAnnotationValue) value1, (NumericAnnotationValue) value2, sortType);
        } else if (value1 instanceof StringAnnotationValue || value2 instanceof StringAnnotationValue) {
            return handleStringValues((StringAnnotationValue) value1, (StringAnnotationValue) value2, sortType);
        } else if (value1 instanceof DateAnnotationValue || value2 instanceof DateAnnotationValue) {
            return handleDateValues((DateAnnotationValue) value1, (DateAnnotationValue) value2, sortType);
        } else {
            // Equal when both values are null (imaging data with no annotation)
            // Or could be an error if the values are not one of the above instance.
            return 0;
        }
    }

    private int handleNumericValues(NumericAnnotationValue value1,
                                    NumericAnnotationValue value2,
                                    SortTypeEnum sortType) {
        boolean val1null = value1 == null || value1.getNumericValue() == null;
        boolean val2null = value2 == null || value2.getNumericValue() == null;

        if (!val1null && !val2null) {
            if (value1.getNumericValue() < value2.getNumericValue()) {
                return LESS_THAN * getSortOrder(sortType);
            }
            if (value1.getNumericValue() > value2.getNumericValue()) {
                return GREATER_THAN * getSortOrder(sortType);
            }
            // Must be equal.
            return EQUAL * getSortOrder(sortType);
        } else {
            return handleNullValues(val1null, val2null, sortType);
        }
    }

    private int handleDateValues(DateAnnotationValue value1,
                                 DateAnnotationValue value2,
                                 SortTypeEnum sortType) {
        boolean val1null = value1 == null || value1.getDateValue() == null;
        boolean val2null = value2 == null || value2.getDateValue() == null;

        if (!val1null && !val2null) {
            return DateUtil.toStringForComparison(value1.getDateValue()).compareTo(
                DateUtil.toStringForComparison(value2.getDateValue()));
        } else {
            return handleNullValues(val1null, val2null, sortType);
        }
    }

    private int handleStringValues(StringAnnotationValue value1,
                                    StringAnnotationValue value2,
                                    SortTypeEnum sortType) {
        boolean val1null = value1 == null || value1.getStringValue() == null;
        boolean val2null = value2 == null || value2.getStringValue() == null;

        if (!val1null && !val2null) {
            return value1.getStringValue().compareTo(value2.getStringValue()) * getSortOrder(sortType);
        } else {
            return handleNullValues(val1null, val2null, sortType);
        }
    }

    private int handleNullValues(boolean val1null, boolean val2null, SortTypeEnum sortType) {
        if (val1null && !val2null) {
            return LESS_THAN * getSortOrder(sortType);
        }
        if (!val1null && val2null) {
            return GREATER_THAN * getSortOrder(sortType);
        }
        // Both null means equal.
        return EQUAL * getSortOrder(sortType);
    }

    private int getSortOrder(SortTypeEnum sortType) {
        if (SortTypeEnum.ASCENDING.equals(sortType)) {
            return 1;
        } else if (SortTypeEnum.DESCENDING.equals(sortType)) {
            return -1;
        }
        return 0;
    }
}
