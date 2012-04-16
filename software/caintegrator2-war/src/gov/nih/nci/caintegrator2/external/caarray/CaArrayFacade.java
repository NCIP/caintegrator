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
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface to the CaArrayFacade subsystem used to interface with an external caArray 2 server.
 */
public interface CaArrayFacade {

    /**
     * Returns all the samples in the experiment indicated by identifier.
     *
     * @param experimentIdentifier identifies the experiment
     * @param profile contains connection information for the caArray server
     * @return the samples in the experiment.
     * @throws ConnectionException if the subsystem can't connect to the caArray server.
     * @throws ExperimentNotFoundException if the experiment cannot be found.
     */
    List<Sample> getSamples(String experimentIdentifier, ServerConnectionProfile profile)
    throws ConnectionException, ExperimentNotFoundException;

    /**
     * Returns the data for the samples contained in the <code>GenomicDataSourceConfiguration</code>.
     *
     * @param genomicSource retrieve data from this source.
     * @throws ConnectionException if the connection to the caArray server fails.
     * @return the array data values.
     * @throws ConnectionException if the subsystem can't connect to the caArray server.
     * @throws DataRetrievalException if the data couldn't be retrieved from caArray.
     */
    ArrayDataValues retrieveData(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, DataRetrievalException;

    /**
     * Returns the data for the samples contained in the <code>GenomicDataSourceConfiguration</code>.
     *
     * @param genomicSource retrieve data from this source.
     * @param arrayDataService to save array data to.
     * @throws ConnectionException if the connection to the caArray server fails.
     * @return the list of array data values.
     * @throws ConnectionException if the subsystem can't connect to the caArray server.
     * @throws DataRetrievalException if the data couldn't be retrieved from caArray.
     */
    List<ArrayDataValues> retrieveDnaAnalysisData(GenomicDataSourceConfiguration genomicSource,
            ArrayDataService arrayDataService)
    throws ConnectionException, DataRetrievalException;

    /**
     * Retrieves a file from a caArray experiment.
     *
     * @param genomicSource retrieve file from this experiment and server
     * @param filename retrieve the file with this name
     * @return the downloaded file contents.
     * @throws ConnectionException if the server couldn't be reached.
     * @throws FileNotFoundException if the experiment doesn't contain the requested file.
     */
    byte[] retrieveFile(GenomicDataSourceConfiguration genomicSource, String filename)
    throws ConnectionException, FileNotFoundException;

    /**
     * Retrieves a list of files for a caArray experiment.
     * @param genomicSource retrieve the list of files from this experiment and server.
     * @return file list.
     * @throws ConnectionException if the server couldn't be reached.
     * @throws FileNotFoundException if the experiment doesn't contain the requested file.
     */
    List<File> retrieveFilesForGenomicSource(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, FileNotFoundException;


    /**
     * Use this to verify a genomic source will connect and find experiment without
     * throwing an exception.
     * @param genomicSource to test connection.
     * @throws ConnectionException if the server couldn't be reached.
     * @throws ExperimentNotFoundException if the experiment doesn't exist on the server.
     */
    void validateGenomicSourceConnection(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, ExperimentNotFoundException;

    /**
     * Retrieves a map of updated, added, deleted samples for a given experiment.
     * @param experimentIdentifier exp id.
     * @param profile connection profile
     * @return map
     * @throws ConnectionException if the server couldn't be reached.
     * @throws ExperimentNotFoundException if the experiment doesn't contain the requested file.
     */
    Map<String, Date> checkForSampleUpdates(String experimentIdentifier, ServerConnectionProfile profile)
    throws ConnectionException, ExperimentNotFoundException;
}
