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
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contains configuration information for retrieving images, etc. from NCIA.
 */
public class ImageDataSourceConfiguration extends AbstractCaIntegrator2Object implements TimeStampable {
    /**
     * For the "Automatic" mapping.
     */
    public static final String AUTOMATIC_MAPPING = "Automatic";
    
    private static final long serialVersionUID = 1L;
    private StudyConfiguration studyConfiguration;
    private ImageAnnotationConfiguration imageAnnotationConfiguration;
    private ServerConnectionProfile serverProfile = new ServerConnectionProfile();
    private List<ImageSeriesAcquisition> imageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
    private String collectionName;
    private String mappingFileName;
    private Status status;
    private String statusDescription;
    private Date lastModifiedDate;
    
    /**
     * @return the studyConfiguration
     */
    public StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @param studyConfiguration the studyConfiguration to set
     */
    public void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }

    /**
     * @return the serverProfile
     */
    public ServerConnectionProfile getServerProfile() {
        return serverProfile;
    }

    /**
     * @param serverProfile the serverProfile to set
     */
    @SuppressWarnings("unused")
    private void setServerProfile(ServerConnectionProfile serverProfile) {
        this.serverProfile = serverProfile;
    }

    /**
     * @return the collectionName
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * @param collectionName the collectionName to set
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    /**
     * @return the imageSeriesAcquisitions
     */
    public List<ImageSeriesAcquisition> getImageSeriesAcquisitions() {
        return imageSeriesAcquisitions;
    }

    /**
     * @param imageSeriesAcquisitions the imageSeriesAcquisitions to set
     */
    @SuppressWarnings("unused")
    private void setImageSeriesAcquisitions(List<ImageSeriesAcquisition> imageSeriesAcquisitions) {
        this.imageSeriesAcquisitions = imageSeriesAcquisitions;
    }

    /**
     * @return the mappingFileName
     */
    public String getMappingFileName() {
        return mappingFileName;
    }

    /**
     * @param mappingFileName the fileName to set
     */
    public void setMappingFileName(String mappingFileName) {
        this.mappingFileName = mappingFileName;
    }

    /**
     * @return the imageAnnotationConfiguration
     */
    public ImageAnnotationConfiguration getImageAnnotationConfiguration() {
        return imageAnnotationConfiguration;
    }

    /**
     * @param imageAnnotationConfiguration the imageAnnotationConfiguration to set
     */
    public void setImageAnnotationConfiguration(ImageAnnotationConfiguration imageAnnotationConfiguration) {
        this.imageAnnotationConfiguration = imageAnnotationConfiguration;
    }
    
    /**
     * List of image series acquisitions that are mapped for this source.
     * @return mapped image series acquisitions.
     */
    public List<ImageSeriesAcquisition> getMappedImageSeriesAcquisitions() {
        List<ImageSeriesAcquisition> mappedImageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
        for (ImageSeriesAcquisition acquisition : imageSeriesAcquisitions) {
            if (acquisition.getAssignment() != null) {
                mappedImageSeriesAcquisitions.add(acquisition);
            }
        }
        return mappedImageSeriesAcquisitions;
    }
    
    /**
     * List of image series acquisitions that are unmapped for this source.
     * @return unmapped image series acquisitions.
     */
    public List<ImageSeriesAcquisition> getUnmappedImageSeriesAcquisitions() {
        List<ImageSeriesAcquisition> unmappedImageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
        for (ImageSeriesAcquisition acquisition : imageSeriesAcquisitions) {
            if (acquisition.getAssignment() == null) {
                unmappedImageSeriesAcquisitions.add(acquisition);
            }
        }
        return unmappedImageSeriesAcquisitions;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the statusDescription
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * @param statusDescription the statusDescription to set
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = Cai2Util.trimDescription(statusDescription);
    }

    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayableLastModifiedDate() {
        return DateUtil.getDisplayableTimeStamp(lastModifiedDate); 
    }

    /**
     * Delete mapping file and set the status to NOT_MAPPED.
     */
    public void deleteMappingFile() {
        setMappingFileName(null);
        for (ImageSeriesAcquisition isa : getImageSeriesAcquisitions()) {
            isa.setAssignment(null);
        }
        setStatus(Status.NOT_MAPPED);
    }

}
