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
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Represents a CSV annotation text file.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // switch statement and argument checking
public class AnnotationFile extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private String path;
    private List<FileColumn> columns = new ArrayList<FileColumn>();
    private FileColumn identifierColumn;
    private FileColumn timepointColumn;
    private String currentlyLoaded;
    private transient File file;
    private transient CSVReader reader;
    private transient String[] currentLineValues;
    private transient Map<AnnotationFieldDescriptor, FileColumn> descriptorToColumnMap;
    private final transient Set<String> currentlyLoadedIdentifier = new HashSet<String>();
    

    /**
     * No-arg constructor required by Hibernate.
     */
    @Deprecated
    public AnnotationFile() {
        super();
    } 
    
    private AnnotationFile(File file) {
        setPath(file.getAbsolutePath());
    }

    /**
     * @return the columns
     */
    public List<FileColumn> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<FileColumn> columns) {
        this.columns = columns;
    }
    
    static AnnotationFile load(File file, CaIntegrator2Dao dao) throws ValidationException {
        AnnotationFile annotationFile = new AnnotationFile(file);
        annotationFile.validateFileFormat();
        annotationFile.loadColumns(dao);
        return annotationFile;
    }

    private void loadColumns(CaIntegrator2Dao dao) throws ValidationException {
        resetReader();
        loadNextLine();
        for (int index = 0; index < currentLineValues.length; index++) {
            FileColumn column = new FileColumn(this);
            column.setPosition(index);
            column.setName(currentLineValues[index].trim());
            column.createFieldDescriptor(dao);
            columns.add(column);
        }
    }

    private void validateFileFormat() throws ValidationException {
        validateFileExists();
        validateFileNotEmpty();
        validateFileHasData();
        validateDataLinesConsistent();
    }

    private void validateFileNotEmpty() throws ValidationException {
        resetReader();
        if (readNext() == null) {
            throwValidationException("The data file was empty.");
        }
        
    }

    private void validateFileHasData() throws ValidationException {
        positionAtData();
        if (readNext() == null) {
            throwValidationException("The data file contained no data (header line only).");
        }
    }

    private void validateDataLinesConsistent() throws ValidationException {
        resetReader();
        int numberOfHeaders = readNext().length;
        int currentLine = 2;
        String[] values;
        while ((values = readNext()) != null) {
            if (values.length != numberOfHeaders) {
                throwValidationException("Number of values in line " + currentLine 
                        + " inconsistent with header line. Expected " + numberOfHeaders + " but found " 
                        + values.length + " values.");
            }
            currentLine++;
        }
    }

    private void validateFileExists() throws ValidationException {
        if (!getFile().exists()) {
            throwValidationException("The file " + getFile().getAbsolutePath() + " could not be found");
        }
    }
    private void throwValidationException(String message) throws ValidationException {
        ValidationResult result = new ValidationResult();
        result.setInvalidMessage(message);
        throw new ValidationException(result);
    }
    
    /**
     * Returns the underlying file.
     * 
     * @return the file.
     */
    public File getFile() {
        if (file == null) {
            file = new File(getPath());
        }
        return file;
    }

    private String getPath() {
        return path;
    }

    @SuppressWarnings("unused")
    private void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the identifierColumn
     */
    public FileColumn getIdentifierColumn() {
        return identifierColumn;
    }

    /**
     * @param identifierColumn the identifierColumn to set
     */
    public void setIdentifierColumn(FileColumn identifierColumn) {
        this.identifierColumn = identifierColumn;
        if (identifierColumn != null) {
            identifierColumn.setFieldDescriptor(null);
            if (identifierColumn.equals(getTimepointColumn())) {
                setTimepointColumn(null);
            }
        }
    }
    

    /**
     * @return the timepointColumn
     */
    public FileColumn getTimepointColumn() {
        return timepointColumn;
    }

    /**
     * @param timepointColumn the timepointColumn to set
     */
    public void setTimepointColumn(FileColumn timepointColumn) {
        this.timepointColumn = timepointColumn;
        if (timepointColumn != null) {
            timepointColumn.setFieldDescriptor(null);
            if (timepointColumn.equals(getIdentifierColumn())) {
                setIdentifierColumn(null);
            }
        }
    }
    
    private String[] readNext() throws ValidationException {
        try {
            return reader.readNext();
        } catch (IOException e) {
            throwValidationException("Error reading file.");
            return null;
        }
    }

    void positionAtData() throws ValidationException {
        resetReader();
        loadNextLine();
    }
    
    private void loadNextLine() throws ValidationException {
         currentLineValues = readNext();            
    }

    boolean hasNextDataLine() throws ValidationException {
        loadNextLine();
        return currentLineValues != null;
    }

    String getDataValue(AnnotationFieldDescriptor descriptor) throws ValidationException {
        return getDataValue(getDescriptorToColumnMap().get(descriptor));
    }

    String getDataValue(FileColumn fileColumn) throws ValidationException {
        if (currentLineValues == null) {
            throwValidationException("Caller must first call hasNextDataLine() before retrieving data.");
        }
        return currentLineValues[fileColumn.getPosition()];
    }

    private void resetReader() throws ValidationException {
        try {
            reader = new CSVReader(new FileReader(getFile()));
        } catch (FileNotFoundException e) {
            throwValidationException("Can't reset file: " + getFile().getAbsolutePath());
        }
    }

    void loadDescriptors(Collection<AnnotationFieldDescriptor> existingDesciptors) {
        for (FileColumn column : columns) {
            loadDescriptor(column, existingDesciptors);
        }
    }

    private void loadDescriptor(FileColumn column, Collection<AnnotationFieldDescriptor> existingDesciptors) {
        if (!isAnnotationColumn(column)) {
            return;
        }
        for (AnnotationFieldDescriptor descriptor : existingDesciptors) {
            if (descriptor.getName().equals(column.getName())) {
                column.setFieldDescriptor(descriptor);
                return;
            }
        }
    }

    private boolean isAnnotationColumn(FileColumn column) {
        return !(column.equals(getIdentifierColumn()) || column.equals(getTimepointColumn()));
    }

    private Map<AnnotationFieldDescriptor, FileColumn> getDescriptorToColumnMap() {
        if (descriptorToColumnMap == null) {
            loadDescriptorToColumnMap();
        }
        return descriptorToColumnMap;
    }

    private void loadDescriptorToColumnMap() {
        descriptorToColumnMap = new HashMap<AnnotationFieldDescriptor, FileColumn>();
        for (FileColumn column : columns) {
            if (column.getFieldDescriptor() != null) {
                descriptorToColumnMap.put(column.getFieldDescriptor(), column);
            }
        }
    }

    List<AnnotationFieldDescriptor> getDescriptors() {
        List<AnnotationFieldDescriptor> descriptors = new ArrayList<AnnotationFieldDescriptor>();
        for (FileColumn column : columns) {
            if (column.getFieldDescriptor() != null) {
                descriptors.add(column.getFieldDescriptor());
            }
        }
        return descriptors;
    }

    ValidationResult validate() {
        ValidationResult result = new ValidationResult();
        if (getIdentifierColumn() == null) {
            result.setInvalidMessage("The data file does not have an identifier column indicated.");
        }
        return result;
    }

    /**
     * Indicates the identifier column by position.
     * 
     * @param index the index of the column (zero-based)
     */
    public void setIdentifierColumnIndex(int index) {
        setIdentifierColumn(getColumns().get(index));
    }

    void loadAnnontation(AbstractAnnotationHandler handler) throws ValidationException {
        currentlyLoadedIdentifier.clear();
        handler.addDefinitionsToStudy(getAnnotationDefinitions());
        positionAtData();
        while (hasNextDataLine()) {
            String identifier = getDataValue(getIdentifierColumn());
            if (currentlyLoadedIdentifier.contains(identifier)) {
                throw new ValidationException("Multiples identifiers found for '" + identifier + "'");
            }
            currentlyLoadedIdentifier.add(identifier);
            handler.handleIdentifier(identifier);
            loadAnnotationLine(handler);
        }
        setCurrentlyLoaded(String.valueOf(true));
    }

    private Set<AnnotationDefinition> getAnnotationDefinitions() {
        Set<AnnotationDefinition> definitions = new HashSet<AnnotationDefinition>();
        for (AnnotationFieldDescriptor descriptor : getDescriptors()) {
            if (descriptor.getDefinition() != null) {
                definitions.add(descriptor.getDefinition());
            }
        }
        return definitions;
    }

    private void loadAnnotationLine(AbstractAnnotationHandler handler) throws ValidationException {
        for (AnnotationFieldDescriptor annotationDescriptor : getDescriptors()) {
            String value = getDataValue(annotationDescriptor);
            AbstractAnnotationValue annotationValue = createAnnotationValue(annotationDescriptor, value);
            if (getTimepointColumn() != null) {
                String timepointValue = getDataValue(getTimepointColumn());
                handler.handleAnnotationValue(annotationValue, timepointValue);
            } else {
                handler.handleAnnotationValue(annotationValue);
            }
        }
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")   // switch statement and argument checking
     private AbstractAnnotationValue createAnnotationValue(AnnotationFieldDescriptor annotationDescriptor, 
            String value) throws ValidationException {
        if (annotationDescriptor.getDefinition() == null || annotationDescriptor.getDefinition().getType() == null) {
            throwValidationException("Type for field " + annotationDescriptor.getName() + " was not set.");
        }
        AnnotationTypeEnum type = AnnotationTypeEnum.getByValue(annotationDescriptor.getDefinition().getType());
        switch (type) {
        case DATE:
            return createDateAnnotationValue(annotationDescriptor, value);
        case STRING:
            return createStringAnnotationValue(annotationDescriptor, value);
        case NUMERIC:
            return createNumericAnnotationValue(annotationDescriptor, value);
        default:
            throwValidationException("Unknown AnnotationDefinitionType: " + type);
            return null;
        }
    }

    private StringAnnotationValue createStringAnnotationValue(AnnotationFieldDescriptor annotationDescriptor, 
            String value) {
        StringAnnotationValue annotationValue = new StringAnnotationValue();
        annotationValue.setStringValue(value);
        annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
        return annotationValue;
    }

    private DateAnnotationValue createDateAnnotationValue(AnnotationFieldDescriptor annotationDescriptor, 
            String value) throws ValidationException {
        DateAnnotationValue annotationValue = new DateAnnotationValue();
        try {
            annotationValue.setDateValue(getDateValue(value));
        } catch (ParseException e) {
            throwValidationException(createFormatErrorMsg(annotationDescriptor, value, 
                    "The two formats allowed are MM-dd-yyyy and MM/dd/yyyy"));
        }
        annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
        return annotationValue;
    }
    
    private Date getDateValue(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            return DateUtil.createDate(value);
        }
    }

    private NumericAnnotationValue createNumericAnnotationValue(AnnotationFieldDescriptor annotationDescriptor, 
            String value) throws ValidationException {
        NumericAnnotationValue annotationValue = new NumericAnnotationValue();
        try {
            annotationValue.setNumericValue(getNumericValue(value));
        } catch (NumberFormatException e) {
            throwValidationException(createFormatErrorMsg(annotationDescriptor, value, null));
        }
        annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
        return annotationValue;
    }

    private Double getNumericValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            return Double.parseDouble(value);
        }
    }

    private String createFormatErrorMsg(AnnotationFieldDescriptor descriptor, String value, String allowedFormats) {
        return "Invalid format for data type '" + descriptor.getDefinition().getType()
            + "' on field '" + descriptor.getName()
            + "' of descriptor '" + descriptor.getDefinition().getDisplayName()
            + "' with value = '" + value + "'. " + allowedFormats;
    }
    
    boolean isLoadable() {
        if (getIdentifierColumn() == null) {
            return false;
        }
        for (FileColumn column : getColumns()) {
            if (!column.isLoadable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the currentlyLoaded
     */
    public String getCurrentlyLoaded() {
        if (currentlyLoaded == null) {
            currentlyLoaded = String.valueOf(false);
        }
        return currentlyLoaded;
    }

    /**
     * @param currentlyLoaded the currentlyLoaded to set
     */
    public void setCurrentlyLoaded(String currentlyLoaded) {
        this.currentlyLoaded = currentlyLoaded;
    }

    


}
