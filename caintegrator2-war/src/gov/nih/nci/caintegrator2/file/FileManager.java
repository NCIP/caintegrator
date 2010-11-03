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
package gov.nih.nci.caintegrator2.file;

import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import gov.nih.nci.caintegrator2.application.analysis.GctDataset;
import gov.nih.nci.caintegrator2.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVResult;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.genepattern.gistic.Marker;

/**
 * Provides file storage and retrieval functionality.
 */
public interface FileManager {
    
    /**
     * Persists a file in caIntegrator 2. This allows temp files to be copied into permanent storage associated
     * with a study.
     * 
     * @param sourceFile the contents to be stored. The source file may be deleted after the completion of this method.
     * @param filename the filename to use for the file (may be different from sourceFile.getName()).
     * @param studyConfiguration file is associated with this studyConfiguration.
     * @return the permanent file.
     * @throws IOException if the file couldn't be copied to its destination.
     */
    File storeStudyFile(File sourceFile, String filename, StudyConfiguration studyConfiguration) throws IOException;
    
    /**
     * Deletes the storage directory for the given study.
     * @param studyConfiguration for the directory to delete.
     */
    void deleteStudyDirectory(StudyConfiguration studyConfiguration);
        
    /**
     * Returns the storage directory for the given study.
     * 
     * @param study get directory for this study.
     * @return the directory.
     */
    File getStudyDirectory(Study study);


    /**
     * Creates a temporary directory to use based on the given directory name.
     * @param dirName temporary directory name to use (just name, not path).
     * @return the directory.
     */
    File getNewTemporaryDirectory(String dirName);
    
    /**
     * Creates an igv directory for the session.
     * @param sessionId to create directory for.
     * @return igv directory.
     */
    File getIGVDirectory(String sessionId);
    
    /**
     * Delete the igv directory.
     */
    void deleteAllIGVDirectory();
    
    /**
     * Delete the igv directory for the session.
     * @param sessionId to create directory for.
     */
    void deleteIGVDirectory(String sessionId);
    
    /**
     * Retrieves the IGV File for the study based on the file type and platform name.
     * @param study the study.
     * @param fileType the file type.
     * @param platformName the platform name.
     * @return the file or null if not found.
     */
    File retrieveIGVFile(Study study, IGVFileTypeEnum fileType, String platformName);
    
    /**
     * Creates the IGV GCT File for the given gctDataset.
     * @param gctDataset gct data.
     * @param sessionId directory will be based on this.
     * @return the file.
     */
    File createIGVGctFile(GctDataset gctDataset, String sessionId);
    
    /**
     * Stores the IGV GCTFile for the study with platform name.
     * @param gctDataset gct data.
     * @param study the study.
     * @param platformName the platform name.
     * @return the file.
     */
    File createIGVGctFile(GctDataset gctDataset, Study study, String platformName);
    
    /**
     * Creates the IGV Segment Data file for the given segment datas.
     * @param segmentDatas segment data.
     * @param sessionId directory will be based on this.
     * @return the file.
     */
    File createIGVSegFile(Collection<SegmentData> segmentDatas, String sessionId);
    
    /**
     * Stores the IGV Segment Data File for the study with platform name.
     * @param segmentDatas segment data.
     * @param study the study.
     * @param platformName the platform name.
     * @return the file.
     */
    File createIGVSegFile(Collection<SegmentData> segmentDatas, Study study, String platformName);
    
    /**
     * Creates the IGV Sample Classification File for the given query result.
     * @param queryResult query result data to turn into sample classification file.
     * @param sessionId directory will be based on this.
     * @param columns the columns that are in the query results.
     * @return the file.
     */
    File createIGVSampleClassificationFile(QueryResult queryResult, String sessionId, 
            Collection<ResultColumn> columns);
    
    /**
     * Creates the IGV Session file.
     * @param sessionId directory will be based on this.
     * @param urlPrefix url prefix is needed to write the data to the session file.
     * @param igvResult results of the run.
     */
    void createIGVSessionFile(String sessionId, String urlPrefix, IGVResult igvResult);
    
    /**
     * Retrieves the directory for the study subscription's user.
     * @param studySubscription to get username from.
     * @return the directory.
     */
    File getUserDirectory(StudySubscription studySubscription);
    
    /**
     * Creates a file (with given name) in the study directory.
     * @param studySubscription to get directory for user.
     * @param filename of file to return.
     * @return newly created file.
     */
    File createNewStudySubscriptionFile(StudySubscription studySubscription, String filename);
    
    /**
     * Creates a classification file (.cls) for the given sampleClassifications.
     * @param studySubscription to store file to user directory.
     * @param sampleClassifications for classifying samples.
     * @param clsFilename name of file.
     * @return .cls file.
     */
    File createClassificationFile(StudySubscription studySubscription,
            SampleClassificationParameterValue sampleClassifications, String clsFilename);
    
    /**
     * Creates a GCT file based on the gctDataset.
     * @param studySubscription to store file to study folder.
     * @param gctDataset for genomic data to be written to file.
     * @param filename name of file.
     * @return .gct file.
     */
    File createGctFile(StudySubscription studySubscription, GctDataset gctDataset, String filename);

    /**
     * Creates samples file for GISTIC input.
     * @param studySubscription to store file to study folder.
     * @param samples to write to file.
     * @return gistic input sample segment file.
     * @throws IOException if unable to write file.
     */
    File createSamplesFile(StudySubscription studySubscription, SampleWithChromosomalSegmentSet[] samples)
    throws IOException;
    
    /**
     * Creates markers file for GISTIC input.
     * @param studySubscription to store file to study folder.
     * @param markers to write to file
     * @return gistic input markers file.
     * @throws IOException if unable to write file.
     */
    File createMarkersFile(StudySubscription studySubscription, Marker[] markers) throws IOException;
    
    /**
     * Renames the cnvFile so it can be used by GISTIC grid service.
     * @param cnvFile to rename.
     * @return new cnvFile
     * @throws IOException if unable to rename.
     */
    File renameCnvFile(File cnvFile) throws IOException;
    
    /**
     * Creates the input zip file for a given job.
     * @param studySubscription to store file to study folder.
     * @param job analysis job.
     * @param filename of the zip file to be created.
     * @param files set of input files.
     * @return input zip file.
     */
    ResultsZipFile createInputZipFile(StudySubscription studySubscription, AbstractPersistedAnalysisJob job,
            String filename, File... files);
}
