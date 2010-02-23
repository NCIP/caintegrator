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

import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Represents a column in a <code>DelimitedTextFile</code>.
 */
public class FileColumn extends AbstractCaIntegrator2Object implements Comparable<FileColumn> {
    
    private static final long serialVersionUID = 1L;
    private int position;
    private String name;
    private AnnotationFieldDescriptor fieldDescriptor;
    private AnnotationFile annotationFile;
    private transient List<String> dataValues;

    FileColumn(AnnotationFile annotationFile) {
        this.annotationFile = annotationFile;
    }

    /**
     * Creates a new instance.
     */
    public FileColumn() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(FileColumn column) {
        return position - column.position;
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
     * @return the annotationFile
     */
    public AnnotationFile getAnnotationFile() {
        return annotationFile;
    }

    /**
     * @param annotationFile the annotationFile to set
     */
    public void setAnnotationFile(AnnotationFile annotationFile) {
        this.annotationFile = annotationFile;
    }
    
    /**
     * Returns the list of values in this column.
     * 
     * @return the values.
     * @throws ValidationException fail to load
     */
    public List<String> getDataValues() throws ValidationException {
        if (dataValues == null) {
            loadDataValues();
        }
        return dataValues;
    }
    
    /**
     * Retrieves the unique data values represented in the format of the class type.
     * @param <T> the format of the class type.
     * @param classType the format of the class type.
     * @return Set of values.
     * @throws ValidationException if file column is invalid.
     */
    @SuppressWarnings("unchecked")
    public <T> Set <T> getUniqueDataValues(Class<T> classType) throws ValidationException {
        Set<String> uniqueStringDataValues = new HashSet<String>();
        uniqueStringDataValues.addAll(getDataValues());
        if (classType.equals(String.class)) {
            return (Set<T>) uniqueStringDataValues;
        }
        Set<T> uniqueDataValues = new HashSet<T>();
        for (String stringValue : uniqueStringDataValues) {
            uniqueDataValues.add((T) retrieveValueAsClassType(classType, stringValue));
        }
        return uniqueDataValues;
       
    }

    private <T> Object retrieveValueAsClassType(Class<T> classType, String stringValue)
            throws ValidationException {
        if (classType.equals(Double.class)) {
            if (!NumberUtils.isNumber(stringValue)) {
                throw new ValidationException("Cannot cast column as a number, because of value: " + stringValue);
            }
            return Double.valueOf(stringValue);
        } else if (classType.equals(Date.class)) {
            try {
                return DateUtil.createDate(stringValue);
            } catch (ParseException e) {
                throw new ValidationException("Unable to parse date from the column, because of value: " 
                        + stringValue, e);
            }
        } else {
            throw new IllegalArgumentException("classType is not valid.");
        }
    }

    private void loadDataValues() throws ValidationException {
        dataValues = new ArrayList<String>();
        getAnnotationFile().positionAtData();
        while (getAnnotationFile().hasNextDataLine()) {
            dataValues.add(getAnnotationFile().getDataValue(this));
        }
    }

    /**
     * @return true if this is the identifier column in the file.
     */
    public boolean isIdentifierColumn() {
        return getFieldDescriptor() != null && AnnotationFieldType.IDENTIFIER.equals(fieldDescriptor.getType());
    }

    /**
     * @return true if this is the timepoint column in the file.
     */
    public boolean isTimepointColumn() {
        return getFieldDescriptor() != null && AnnotationFieldType.TIMEPOINT.equals(fieldDescriptor.getType());
    }

    /**
     * If field descriptor doesn't exist, creates a new one, and then sets it to the given type.
     * @param type the field type to set this column as.
     */
    public void setupAnnotationFieldDescriptor(AnnotationFieldType type) {
        if (getFieldDescriptor() == null) {
            setFieldDescriptor(new AnnotationFieldDescriptor());
        }
        fieldDescriptor.setType(type);
    }

    boolean isLoadable() {
        return isIdentifierColumn() || isTimepointColumn() 
        || (getFieldDescriptor() != null && getFieldDescriptor().getDefinition() != null);
    }
    
    /**
     * Checks to see if this would be a valid identifier column.
     * @throws ValidationException if invalid column for identifier.
     */
    public void checkValidIdentifierColumn() throws ValidationException {
        Set<String> currentValues = new HashSet<String>();
        for (String value : getDataValues()) {
            if (currentValues.contains(value)) {
                throw new ValidationException("This column cannot be an identifier column because it "
                        + "has a duplicate value for '" + value + "'.");
            }
            currentValues.add(value);
        }
    }

    void retrieveOrCreateFieldDescriptor(StudyManagementService studyManagementService,
            StudyConfiguration studyConfiguration, EntityTypeEnum type, boolean createNewAnnotationDefinition) {
        if (studyConfiguration != null) {
            fieldDescriptor = studyManagementService.getExistingFieldDescriptorInStudy(getName(), studyConfiguration);
        }
        if (fieldDescriptor == null) {
            fieldDescriptor = new AnnotationFieldDescriptor();
            fieldDescriptor.setName(getName());
            fieldDescriptor.setType(AnnotationFieldType.ANNOTATION);
            fieldDescriptor.setDefinition(studyManagementService.getAnnotationDefinition(getName()));
            if (createNewAnnotationDefinition) {
                createNewAnnotationDefinition(fieldDescriptor, studyManagementService);
            }
            AnnotationGroup defaultGroup = studyManagementService.getDefaultAnnotationGroup(
                    studyConfiguration, type);
            fieldDescriptor.setAnnotationGroup(defaultGroup);
            studyManagementService.daoSave(fieldDescriptor);
            defaultGroup.getAnnotationFieldDescriptors().add(fieldDescriptor);
            studyManagementService.daoSave(defaultGroup);
        }
    }

    private void createNewAnnotationDefinition(AnnotationFieldDescriptor fieldDescriptor2,
            StudyManagementService studyManagementService) {
        AnnotationDefinition annotationDefinition = studyManagementService.getAnnotationDefinition(
                fieldDescriptor2.getName());
        if (annotationDefinition == null) {
            annotationDefinition = new AnnotationDefinition();
            annotationDefinition.setDefault(fieldDescriptor2.getName());
            studyManagementService.daoSave(annotationDefinition);
        }
        fieldDescriptor2.setDefinition(annotationDefinition);
    }

}
