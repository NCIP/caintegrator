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
package gov.nih.nci.caintegrator2.common;

import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a static utility class used by different caIntegrator2 objects. 
 */
public final class Cai2Util {
    
    private Cai2Util() { }
    
    /**
     * Used to see if a Collection of strings contains a string, ignoring case.
     * @param l - collection of strings.
     * @param s - string item to see if exists in the collection.
     * @return true/false value.
     */
    public static boolean containsIgnoreCase(Collection <String> l, String s) {
        Iterator<String> it = l.iterator();
        while (it.hasNext()) {
            if (it.next().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Used to see if a Set of ResultRow's contains a specific ResultRow (based on SubjectAssignments matching).
     * This needs to be tweaked, not sure if the algorithm is correct.
     * @param rowSet - set of rows.
     * @param rowToTest - ResultRow item to test if it exists in set.
     * @return true/false value.
     */
    public static boolean resultRowSetContainsResultRow(Set<ResultRow> rowSet, 
                                                        ResultRow rowToTest) {
        for (ResultRow curRow : rowSet) {
            if (curRow.getSubjectAssignment().equals(rowToTest.getSubjectAssignment())
                    && timepointsMatchForRows(rowToTest, curRow) 
                    && sameEntitiesMatch(rowToTest, curRow)) {
                    addExtraDataToRow(rowToTest, curRow);
                    return true;
                }
            }
        return false;
    }

    /**
     * If the rowToTest has more info than the row already in the set,
     * then we need to add in that extra data.
     * @param rowToTest
     * @param curRow
     */
    private static void addExtraDataToRow(ResultRow rowToTest, ResultRow curRow) {
        if (isClinicalOnlyRow(curRow) && !isClinicalOnlyRow(rowToTest)) {
            curRow.setImageSeriesAcquisition(rowToTest.getImageSeriesAcquisition());
            curRow.setSampleAcquisition(rowToTest.getSampleAcquisition());
        }
    }

    /**
     * This tests to see if the timepoints match for image series and genomic to see if they belong
     * together.  It does assume that we ignore timepoints for clinical annotations.
     * @param rowToTest
     * @param curRow
     * @return
     */
    private static boolean timepointsMatchForRows(ResultRow rowToTest, ResultRow curRow) {
        if (retrieveTimepointFromRow(rowToTest).equals(retrieveTimepointFromRow(curRow)) 
             || isClinicalOnlyRow(rowToTest) || isClinicalOnlyRow(curRow)) {
            return true;
        }
        return false;
    }
    
    /**
     * The only time a timepoint wouldn't exist for a row is if it has no image seriesAcquisition and
     * no sampleAcquisition, so essentially it is just clinical data.
     * @param row - row to test.
     * @return true/false value.
     */
    private static boolean isClinicalOnlyRow(ResultRow row) {
       if (row.getImageSeriesAcquisition() == null && row.getSampleAcquisition() == null) {
           return true;
       }
       return false;
    }
    
    private static boolean sameEntitiesMatch(ResultRow rowToTest, ResultRow curRow) {
        if (sameImageSeries(rowToTest, curRow) && sameSample(rowToTest, curRow)) {
            return true;
        }
        return false;
    }

    /**
     * @param rowToTest
     * @param curRow
     */
    private static boolean sameSample(ResultRow rowToTest, ResultRow curRow) {
        if (rowToTest.getSampleAcquisition() != null
            && curRow.getSampleAcquisition() != null) {
            if (rowToTest.getSampleAcquisition().equals(curRow.getSampleAcquisition())) {
                return true;
            } 
            return false;
        }
        return true;
    }

    /**
     * @param rowToTest
     * @param curRow
     */
    private static boolean sameImageSeries(ResultRow rowToTest, ResultRow curRow) {
        if (rowToTest.getImageSeriesAcquisition() != null 
            && curRow.getImageSeriesAcquisition() != null) {
            if (rowToTest.getImageSeriesAcquisition().equals(curRow.getImageSeriesAcquisition())) {
                return true;
            }
            return false;
        }
        return true;
    }
    
    private static Timepoint retrieveTimepointFromRow(ResultRow row) {
        Timepoint timepoint = new Timepoint();
        if (row.getImageSeriesAcquisition() != null) {
            timepoint = row.getImageSeriesAcquisition().getTimepoint();
        } else if (row.getSampleAcquisition() != null) {
            timepoint = row.getSampleAcquisition().getTimepoint();
        } 
        return timepoint;
    }
}
