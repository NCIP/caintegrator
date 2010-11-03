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
package gov.nih.nci.caintegrator2.application.analysis.igv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

/**
 * 
 */
public class IGVSampleInfoFileWriterTest {
    private Collection<ResultColumn> columns = new HashSet<ResultColumn>();
    

    @Test
    public void testWriteSampleInfoFile() throws IOException {
        File tempDirectory = new File(System.getProperty("java.io.tmpdir") + File.separator + "igvTmp");
        tempDirectory.mkdir();
        IGVSampleInfoFileWriter fileWriter = new IGVSampleInfoFileWriter();
        
        File sampleInfoFile = fileWriter.writeSampleInfoFile(QueryUtil.retrieveSampleValuesMap(setupQueryResult()),
                columns, new File(tempDirectory.getAbsolutePath() + File.separator
                        + IGVFileTypeEnum.SAMPLE_CLASSIFICATION.getFilename()).getAbsolutePath());
        checkFile(sampleInfoFile);
        FileUtils.deleteDirectory(tempDirectory);
    }
    
    private QueryResult setupQueryResult() {
        ResultValue resultValue1 = new ResultValue();
        StringAnnotationValue stringValue1 = new StringAnnotationValue();
        stringValue1.setStringValue("val1");
        StringAnnotationValue stringValue2 = new StringAnnotationValue();
        stringValue2.setStringValue("val2");
        ResultValue resultValue2 = new ResultValue();
        ResultColumn resultColumn1 = new ResultColumn();
        columns.add(resultColumn1);
        resultValue1.setColumn(resultColumn1);
        resultValue1.setValue(stringValue1);
        resultValue2.setColumn(resultColumn1);
        resultValue2.setValue(stringValue2);
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        AnnotationDefinition ad = new AnnotationDefinition();
        ad.setDisplayName("annotation");
        resultColumn1.setAnnotationFieldDescriptor(afd);
        afd.setDefinition(ad);
        
        QueryResult queryResult = new QueryResult();
        queryResult.setRowCollection(new HashSet<ResultRow>());
        ResultRow row1 = new ResultRow();
        StudySubjectAssignment assignment1 = new StudySubjectAssignment();
        assignment1.setIdentifier("assignment1");
        row1.setSubjectAssignment(assignment1);
        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        Sample sample1 = new Sample();
        sample1.setName("sample1");
        sampleAcquisition1.setSample(sample1);
        sampleAcquisition1.setAssignment(assignment1);
        sample1.setSampleAcquisition(sampleAcquisition1);
        assignment1.getSampleAcquisitionCollection().add(sampleAcquisition1);
        row1.setSampleAcquisition(sampleAcquisition1);
        row1.setValueCollection(new ArrayList<ResultValue>());
        row1.getValueCollection().add(resultValue1);
        
        ResultRow row2 = new ResultRow();
        StudySubjectAssignment assignment2 = new StudySubjectAssignment();
        assignment2.setIdentifier("assignment2");
        row2.setSubjectAssignment(assignment2);
        SampleAcquisition sampleAcquisition2 = new SampleAcquisition();
        Sample sample2 = new Sample();
        sample2.setName("sample2");
        sampleAcquisition2.setSample(sample2);
        sampleAcquisition2.setAssignment(assignment2);
        sample2.setSampleAcquisition(sampleAcquisition2);
        assignment2.getSampleAcquisitionCollection().add(sampleAcquisition2);
        row2.setSampleAcquisition(sampleAcquisition2);
        row2.setValueCollection(new ArrayList<ResultValue>());
        row2.getValueCollection().add(resultValue2);
        
        queryResult.getRowCollection().add(row1);
        queryResult.getRowCollection().add(row2);
        return queryResult;
    }

    private void checkFile(File sampleInfoFile) throws IOException {
        assertTrue(sampleInfoFile.exists());
        CSVReader reader = new CSVReader(new FileReader(sampleInfoFile), '\t');
        checkLine(reader.readNext(), "TRACK_ID", "SUBJECT_ID", "annotation");
        checkSampleInfoLine(reader.readNext());       
        checkSampleInfoLine(reader.readNext());       
        reader.close();
    }

    private void checkSampleInfoLine(String[] line) {
        if ("sample1".equals(line[0])) {
            checkLine(line, "sample1", "assignment1", "val1");
        } else {
            checkLine(line, "sample2", "assignment2", "val2");
        }
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }
}
