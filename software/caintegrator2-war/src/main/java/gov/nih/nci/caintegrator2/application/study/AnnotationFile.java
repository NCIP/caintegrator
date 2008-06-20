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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Represents a CSV annotation text file.
 */
public class AnnotationFile {
    
    private String path;
    private List<FileColumn> columns = new ArrayList<FileColumn>();
    private FileColumn identifierColumn;
    private FileColumn timepointColumn;
    private transient File file;
    private transient CSVReader reader;
    private transient String[] currentLineValues;
    private transient Map<AnnotationFieldDescriptor, FileColumn> descriptorToColumnMap;

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
    
    static AnnotationFile load(File file) throws ValidationException {
        AnnotationFile annotationFile = new AnnotationFile(file);
        annotationFile.validateFileFormat();
        annotationFile.loadColumns();
        return annotationFile;
    }

    private void loadColumns() {
        resetReader();
        loadNextLine();
        for (int index = 0; index < currentLineValues.length; index++) {
            FileColumn column = new FileColumn();
            column.setPosition(index);
            column.setName(currentLineValues[index]);
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
        String[] values;
        while ((values = readNext()) != null) {
            if (values.length != numberOfHeaders) {
                throwValidationException("Number of values inconsistent with header line.");
            }
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
    }
    
    private String[] readNext() {
        try {
            return reader.readNext();
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected failure: unable to read validated file", e);
        }
    }

    void positionAtData() {
        resetReader();
        loadNextLine();
    }
    
    private void loadNextLine() {
         currentLineValues = readNext();            
    }

    boolean hasNextDataLine() {
        loadNextLine();
        return currentLineValues != null;
    }

    String getDataValue(AnnotationFieldDescriptor descriptor) {
        return getDataValue(getDescriptorToColumnMap().get(descriptor));
    }

    String getDataValue(FileColumn fileColumn) {
        if (currentLineValues == null) {
            throw new IllegalStateException("Caller must first call hasNextDataLine() before retrieving data.");
        }
        return currentLineValues[fileColumn.getPosition()];
    }

    private void resetReader() {
        try {
            reader = new CSVReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unexpected failure: unable to reset file", e);
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
        column.setFieldDescriptor(new AnnotationFieldDescriptor());
        column.getFieldDescriptor().setName(column.getName());
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

}
