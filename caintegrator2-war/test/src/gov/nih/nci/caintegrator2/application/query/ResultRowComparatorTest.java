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

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.SortTypeEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

public class ResultRowComparatorTest {

    @Test
    public void testSort() {
        ResultRow row1 = new ResultRow();
        row1.setId(Long.valueOf(1));
        ResultRow row2 = new ResultRow();
        row2.setId(Long.valueOf(2));
        ResultRow row3 = new ResultRow();
        row3.setId(Long.valueOf(3));
        ResultRow row4 = new ResultRow();
        row4.setId(Long.valueOf(4));
        ResultRow row5 = new ResultRow();
        row5.setId(Long.valueOf(5));

        ResultColumn col1 = new ResultColumn();
        ResultColumn col2 = new ResultColumn();

        Collection<ResultRow> rowCollection = new HashSet<ResultRow>();
        rowCollection.add(row1);
        ResultValue row1col1Value = new ResultValue();
        ResultValue row1col2Value = new ResultValue();
        Collection <ResultValue> row1ValueCollection = new HashSet<ResultValue>();
        row1.setValueCollection(row1ValueCollection);

        rowCollection.add(row2);
        ResultValue row2col1Value = new ResultValue();
        ResultValue row2col2Value = new ResultValue();
        Collection <ResultValue> row2ValueCollection = new HashSet<ResultValue>();
        row2.setValueCollection(row2ValueCollection);

        rowCollection.add(row3);
        ResultValue row3col1Value = new ResultValue();
        ResultValue row3col2Value = new ResultValue();
        Collection <ResultValue> row3ValueCollection = new HashSet<ResultValue>();
        row3.setValueCollection(row3ValueCollection);

        rowCollection.add(row4);
        ResultValue row4col1Value = new ResultValue();
        ResultValue row4col2Value = new ResultValue();
        Collection <ResultValue> row4ValueCollection = new HashSet<ResultValue>();
        row4.setValueCollection(row4ValueCollection);

        rowCollection.add(row5);
        ResultValue row5col1Value = new ResultValue();
        ResultValue row5col2Value = new ResultValue();
        Collection <ResultValue> row5ValueCollection = new HashSet<ResultValue>();
        row5.setValueCollection(row5ValueCollection);

        NumericAnnotationValue numVal1 = new NumericAnnotationValue();
        numVal1.setNumericValue(1.0);
        NumericAnnotationValue numVal2 = new NumericAnnotationValue();
        numVal2.setNumericValue(5.0);
        NumericAnnotationValue numVal3 = new NumericAnnotationValue();
        numVal3.setNumericValue(2.0);
        NumericAnnotationValue numVal4 = new NumericAnnotationValue();
        numVal4.setNumericValue(5.0);
        NumericAnnotationValue numVal5 = new NumericAnnotationValue();
        numVal5.setNumericValue(1.0);

        StringAnnotationValue stringVal1 = new StringAnnotationValue();
        // Commenting this out to make sure that null values are sorted properly. (Always lower)
//        stringVal1.setStringValue("value 1");
        StringAnnotationValue stringVal2 = new StringAnnotationValue();
        stringVal2.setStringValue("value 2");
        StringAnnotationValue stringVal3 = new StringAnnotationValue();
        stringVal3.setStringValue("value 3");
        StringAnnotationValue stringVal4 = new StringAnnotationValue();
        stringVal4.setStringValue("value 4");
        StringAnnotationValue stringVal5 = new StringAnnotationValue();
        stringVal5.setStringValue("value 5");

        row1col1Value.setColumn(col1);
        row1col1Value.setValue(numVal1);
        row1col2Value.setColumn(col2);
        row1col2Value.setValue(stringVal1);
        row1ValueCollection.add(row1col1Value);
        row1ValueCollection.add(row1col2Value);

        row2col1Value.setColumn(col1);
        row2col1Value.setValue(numVal2);
        row2col2Value.setColumn(col2);
        row2col2Value.setValue(stringVal2);
        row2ValueCollection.add(row2col1Value);
        row2ValueCollection.add(row2col2Value);

        row3col1Value.setColumn(col1);
        row3col1Value.setValue(numVal3);
        row3col2Value.setColumn(col2);
        row3col2Value.setValue(stringVal3);
        row3ValueCollection.add(row3col1Value);
        row3ValueCollection.add(row3col2Value);

        row4col1Value.setColumn(col1);
        row4col1Value.setValue(numVal4);
        row4col2Value.setColumn(col2);
        row4col2Value.setValue(stringVal4);
        row4ValueCollection.add(row4col1Value);
        row4ValueCollection.add(row4col2Value);

        row5col1Value.setColumn(col1);
        row5col1Value.setValue(numVal5);
        row5col2Value.setColumn(col2);
        row5col2Value.setValue(stringVal5);
        row5ValueCollection.add(row5col1Value);
        row5ValueCollection.add(row5col2Value);

        col1.setSortOrder(1);
        col1.setSortType(SortTypeEnum.ASCENDING);

        col2.setSortOrder(2);
        col2.setSortType(SortTypeEnum.DESCENDING);

        List <ResultColumn> sortColumns = new ArrayList<ResultColumn>();
        sortColumns.add(col1);
        sortColumns.add(col2);

        // Teset1 is col1.sortType ascending, col2.sortType descending.
        List <ResultRow> sortedRows = ResultRowComparator.sort(rowCollection, sortColumns);
        assertEquals(Long.valueOf(5), sortedRows.get(0).getId());
        assertEquals(Long.valueOf(1), sortedRows.get(1).getId());
        assertEquals(Long.valueOf(3), sortedRows.get(2).getId());
        assertEquals(Long.valueOf(4), sortedRows.get(3).getId());
        assertEquals(Long.valueOf(2), sortedRows.get(4).getId());

        // Test2 is col1.sortType null (should default to ascending), col2.sortType ascending.
        col1.setSortType(null);
        col2.setSortType(SortTypeEnum.ASCENDING);
        assertNull(col1.getSortType());
        List <ResultRow> sortedRows2 = ResultRowComparator.sort(rowCollection, sortColumns);
        assertEquals(Long.valueOf(1), sortedRows2.get(0).getId());
        assertEquals(Long.valueOf(5), sortedRows2.get(1).getId());
        assertEquals(Long.valueOf(3), sortedRows2.get(2).getId());
        assertEquals(Long.valueOf(2), sortedRows2.get(3).getId());
        assertEquals(Long.valueOf(4), sortedRows2.get(4).getId());
        // Test to make sure that column's default to Ascending sort types.
        assertEquals(SortTypeEnum.ASCENDING, col1.getSortType());

    }



}
