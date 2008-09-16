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

package gov.nih.nci.caintegrator2.web.action.study.management;


import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.io.File;
import java.io.IOException;

/**
 * Adds a new new Imaging data source file to a study.
 */
public class AddImageFileAction extends AbstractImagingSourceAction {

    private static final long serialVersionUID = 1L;
    private File imagingFile;
    private String imagingFileContentType;
    private String imagingFileFileName;
    //private StudyManagementService studyManagementService;
    private String hostname;
    private String username;
    private String password;
    private String protocolId;
    
    /**
     * @return String.
     */
    @Override
    public String execute() {
       
        try {
            
            ImageDataSourceConfiguration imageSource = new ImageDataSourceConfiguration();
            imageSource.getServerProfile().setUrl(getHostname());
            // setUrl("http://imaging-dev.nci.nih.gov/wsrf/services/cagrid/NCIACoreService");
            imageSource.setTrialDataProvenance(getProtocolId());
            imageSource.setStudyConfiguration(this.getStudyConfiguration());
            //imageSource.getProtocolId();
            // setting the values of imageSource
            setImageSource(imageSource);
            StudyConfiguration s1 = getStudyConfiguration();
            getStudyManagementService().addImageSource(s1, getImageSource());
            ImageAnnotationConfiguration imageAnnotationSource = 
                        getStudyManagementService().addImageAnnotationFile(getStudyConfiguration(), getImagingFile(), 
                getImagingFileFileName());
            setImagingSource(imageAnnotationSource);
            return SUCCESS;
        } catch (ValidationException e) {
            addFieldError("imagingFile", "Invalid file: " + e.getResult().getInvalidMessage());
            return INPUT;
        } catch (IOException e) {
            return ERROR;
        } catch (ConnectionException e) {
            addActionError("The configured server couldn't reached. Please check the configuration settings.");
            return INPUT;
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
public void validate() {
        if (imagingFile == null) {
            addFieldError("imagingFile", "Imaging File is required");
        }
    }

    
/**
 * @return the Imaging File
 */
public File getImagingFile() {
    return imagingFile;
}

/**
 * @param imagingFile the imagingFile to set
 */
public void setImagingFile(File imagingFile) {
 this.imagingFile = imagingFile;
}

/**
 * @return ImagingFileFileName
 */
public String getImagingFileFileName() {
    return imagingFileFileName;
}

/**
 * @param imagingFileFileName the ImagingFileFileName to set
 */
public void setImagingFileFileName(String imagingFileFileName) {
    this.imagingFileFileName = imagingFileFileName;
}

/**
 * @return ImagingFileContentType
 */
public String getImagingFileContentType() {
    return imagingFileContentType;
}

/**
 * @param imagingFileContentType the ImagingFileContentType to set
 */
public void setImagingFileContentType(String imagingFileContentType) {
    this.imagingFileContentType = imagingFileContentType;
}


/**
 * @return the username
 */
public String getUsername() {
    return username;
}

/**
 * @param username the username to set
 */
public void setUsername(String username) {
    this.username = username;
}

/**
 * @return the password
 */
public String getPassword() {
    return password;
}

/**
 * @param password the password to set
 */
public void setPassword(String password) {
    this.password = password;
}

/**
 * @return the protocolId
 */
public String getProtocolId() {
    return protocolId;
}

/**
 * @param protocolId the protocolId to set
 */
public void setProtocolId(String protocolId) {
    this.protocolId = protocolId;
}

/**
 * @return the hostname
 */
public String getHostname() {
    return hostname;
}

/**
 * @param hostname the hostname to set
 */
public void setHostname(String hostname) {
    this.hostname = hostname;
}

}
