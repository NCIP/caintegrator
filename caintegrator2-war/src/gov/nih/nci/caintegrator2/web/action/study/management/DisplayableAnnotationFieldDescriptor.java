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

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.ValidationException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * 
 */
public class DisplayableAnnotationFieldDescriptor implements Comparable<DisplayableAnnotationFieldDescriptor> {
    
    private AnnotationFieldDescriptor fieldDescriptor;
    private String annotationGroupName;
    private boolean identifierType = false;
    private boolean timepointType = false;
    private List<String> dataValues = new ArrayList<String>();
    private String originalGroupName;
    
    /**
     * Default constructor.
     */
    public DisplayableAnnotationFieldDescriptor() { 
        // Empty Constructor
    }
    
    /**
     * Constructor for fileColumn.
     * @param fileColumn used to construct this object.
     */
    public DisplayableAnnotationFieldDescriptor(FileColumn fileColumn) {
        initialize(fileColumn.getFieldDescriptor());
        try {
            dataValues = fileColumn.getDataValues();
        } catch (ValidationException e) {
            dataValues.clear();
        }
    }
    
    /**
     * Constructor for annotationFieldDescriptor.
     * @param annotationFieldDescriptor used to construct this object.
     */
    public DisplayableAnnotationFieldDescriptor(AnnotationFieldDescriptor annotationFieldDescriptor) {
        initialize(annotationFieldDescriptor);
    }

    private void initialize(AnnotationFieldDescriptor newFieldDescriptor) {
        this.fieldDescriptor = newFieldDescriptor;
        if (fieldDescriptor != null) { // Currently identifiers don't have field descriptors... that will change.
            annotationGroupName = fieldDescriptor.getAnnotationGroup() == null ? "" : fieldDescriptor
                    .getAnnotationGroup().getName();
            originalGroupName = annotationGroupName;
            identifierType = AnnotationFieldType.IDENTIFIER.equals(fieldDescriptor.getType());
            timepointType = AnnotationFieldType.TIMEPOINT.equals(fieldDescriptor.getType());
        }
    }
    
    /**
     * @return the fieldDescriptor
     */
    public AnnotationFieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }
    /**
     * @param fieldDescriptor the fieldDescriptor to set
     */
    public void setFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor) {
        this.fieldDescriptor = fieldDescriptor;
    }
    /**
     * @return the annotationGroupName
     */
    public String getAnnotationGroupName() {
        return annotationGroupName;
    }
    /**
     * @param annotationGroupName the annotationGroupName to set
     */
    public void setAnnotationGroupName(String annotationGroupName) {
        this.annotationGroupName = annotationGroupName;
    }

    /**
     * @return the identifierType
     */
    public boolean isIdentifierType() {
        return identifierType;
    }

    /**
     * @param identifierType the identifierType to set
     */
    public void setIdentifierType(boolean identifierType) {
        this.identifierType = identifierType;
    }

    /**
     * @return the timepointType
     */
    public boolean isTimepointType() {
        return timepointType;
    }

    /**
     * @param timepointType the timepointType to set
     */
    public void setTimepointType(boolean timepointType) {
        this.timepointType = timepointType;
    }


    /**
     * @return the dataValues
     */
    public List<String> getDataValues() {
        return dataValues;
    }

    /**
     * @param dataValues the dataValues to set
     */
    public void setDataValues(List<String> dataValues) {
        this.dataValues = dataValues;
    }
    
    /**
     * Determines if the group value has changed.
     * @return T/F value.
     */
    public boolean isGroupChanged() {
        return !StringUtils.equals(originalGroupName, annotationGroupName);
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(DisplayableAnnotationFieldDescriptor o) {
        if (isIdentifierType() && !o.identifierType) {
            return -1;
        } else if (!isIdentifierType() && o.identifierType) {
            return 1;
        }
        return fieldDescriptor.getName().compareTo(o.getFieldDescriptor().getName());
    }

}