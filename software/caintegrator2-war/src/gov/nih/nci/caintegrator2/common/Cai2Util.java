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

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.caintegrator2.application.analysis.InvalidSurvivalValueDefinitionException;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * This is a static utility class used by different caIntegrator2 objects. 
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveClassLength" }) // See method retrieveValueFromRowColumn
public final class Cai2Util {
    private static final Integer BUFFER_SIZE = 4096;
    private static final String ZIP_FILE_SUFFIX = ".zip";
    private static final double COLOR_SATURATION = 0.89;
    private static final double COLOR_BRIGHTNESS = 0.89;
    private static final double GOLDEN_ANGLE = 0.381966;
    private static final double MAX_ANGLE_CONSTANT = 360;
    private static final Set<Color> COLOR_PALETTE = new HashSet<Color>();
    
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
            if (curRow.getSubjectAssignment() == rowToTest.getSubjectAssignment()
                    && curRow.getImageSeries() == rowToTest.getImageSeries()
                    && curRow.getSampleAcquisition() == rowToTest.getSampleAcquisition()) {
                    return true;
                }
            }
        return false;
    }
    
    /**
     * Given a ResultRow and ResultColumn, we can retrieve the specific ResultValue object that 
     * associates the two.  It uses the AnnotationDefinition from the Column to compare so this
     * must be non-null and equal.
     * @param row object to retrieve value from.
     * @param column object to retrieve value from.
     * @return ResultValue, assuming the row/column pair isn't a null value.
     */
    @SuppressWarnings({ "PMD.CyclomaticComplexity" }) // Lots of null checks
    public static ResultValue retrieveValueFromRowColumn(ResultRow row, ResultColumn column) {
        if (row.getValueCollection() != null && !row.getValueCollection().isEmpty()) {
            for (ResultValue value : row.getValueCollection()) {
                if (value.getColumn().getAnnotationDefinition() != null 
                    && column.getAnnotationDefinition() != null
                    && value.getColumn().getAnnotationDefinition().equals(column.getAnnotationDefinition())) { 
                    return value;
                }
            }
        }
        return null;
    }
    
    /**
     * Tests a collection of result columns to see if a particular result column already exists in it.
     * @param columns collection of ResultColumns to test.
     * @param column object we are testing to see if it exist in the collection.
     * @return true or false depending on if column exists in result columns colleection
     */
    public static boolean columnCollectionContainsColumn(Collection<ResultColumn> columns, ResultColumn column) {
        if (columns != null && !columns.isEmpty()) {
            for (ResultColumn currentColumn : columns) {
                if (currentColumn.getAnnotationDefinition() != null 
                        && currentColumn.getAnnotationDefinition().equals(column.getAnnotationDefinition())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This function checks to see if an AnnotationValue belongs to a PermissibleValue.
     * @param value object to check to see if it belongs to permissible value.
     * @param permissibleValue object that the value uses to validate against.
     * @return true or false value.
     */
    public static boolean annotationValueBelongToPermissibleValue(AbstractAnnotationValue value, 
                                                                  PermissibleValue permissibleValue) {
        if (value instanceof StringAnnotationValue) {
            return handleStringValues(value, permissibleValue);
        } else if (value instanceof NumericAnnotationValue) {
            return handleNumericValues(value, permissibleValue);
        } else if (value instanceof DateAnnotationValue) {
            return handleDateValues(value, permissibleValue);
        }
        return false; 
    }
    
    private static boolean handleStringValues(AbstractAnnotationValue value, 
                                              PermissibleValue permissibleValue) {
        StringAnnotationValue stringValue = (StringAnnotationValue) value;
        if (stringValue.getStringValue() != null
            && stringValue.getStringValue().equalsIgnoreCase(permissibleValue.getValue())) {
            return true;
        }
        return false;
    }
    
    private static boolean handleNumericValues(AbstractAnnotationValue value, 
                                               PermissibleValue permissibleValue) {
        if (!NumberUtils.isNumber(permissibleValue.getValue())) {
            throw new IllegalArgumentException("value is of type Numeric, but permissibleValue is not.");    
        }
        NumericAnnotationValue numericValue = (NumericAnnotationValue) value;
        if (numericValue.getNumericValue() != null 
             && numericValue.getNumericValue().equals(Double.valueOf(permissibleValue.getValue()))) {
            return true;
        }
        return false;
    }

    private static boolean handleDateValues(AbstractAnnotationValue value, PermissibleValue permissibleValue) {
        DateAnnotationValue dateValue = (DateAnnotationValue) value;
        if (dateValue.getDateValue() != null && permissibleValue.getValue() != null 
            && permissibleValue.getValue().equals(DateUtil.toString(dateValue.getDateValue()))) {
            return true;
        }
        return false;
    }
    
    /**
     * Write a byte array to a file.
     * @param fileBytes the byte array
     * @param tempFile the output file
     * @throws IOException the I/O exception
     */
    public static void byteArrayToFile(byte[] fileBytes, File tempFile) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
        int totalBytesWritten = 0;
        int fileBytesLength = fileBytes.length;
        while (totalBytesWritten < fileBytesLength) {
            int lengthToWrite = fileBytesLength - totalBytesWritten < BUFFER_SIZE 
                ? fileBytesLength - totalBytesWritten : BUFFER_SIZE;
            bos.write(fileBytes, totalBytesWritten, lengthToWrite);
            totalBytesWritten += lengthToWrite;
        }
        bos.flush();
        bos.close();
    }

    /**
     * Takes in a directory and zips it up.
     * 
     * @param dir - Directory to zip.
     * @return Zipped file, stored in same location, with same name as the directory with .zip attached.
     * @throws IOException - In case cannot be read.
     */
    public static File zipAndDeleteDirectory(String dir) throws IOException {
        File directory = new File(dir);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory:  " + dir);
        }
        int index = directory.getPath().indexOf(directory.getName());
        String[] entries = directory.list();
        if (entries.length == 0) {
            return null;
        }
        File zipfile = new File(dir + ZIP_FILE_SUFFIX);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
        addDir(directory, out, index);
        out.close();
        FileUtils.deleteDirectory(directory);
        return zipfile;
    }
    
    /**
     * Adds files to an already created zip file.
     * @param sourceZipfile to add files to.
     * @param files to add to zip file.
     * @return the new zip file.
     * @throws IOException if unable to add to zip file.
     */
    public static File addFilesToZipFile(File sourceZipfile, File... files) throws IOException {
        if (!sourceZipfile.getAbsolutePath().endsWith(ZIP_FILE_SUFFIX)) {
            throw new IllegalArgumentException("The zipfile isn't a .zip type.");
        }
        for (File file : files) {
            ZipUtilities.insertEntry(sourceZipfile, file.getName(), streamFile(file));
        }
        return sourceZipfile;
    }
    
    private static byte[] streamFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            return null;
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length) {
            numRead = is.read(bytes, offset, bytes.length - offset);
            offset += numRead;
            if (numRead <= 0) {
                break;
            }
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }
    
    private static void addDir(File dirObj, ZipOutputStream out, int index) throws IOException {
        File[] files = dirObj.listFiles();

        for (int i = 0; i < files.length; i++) {
            File curFile = files[i];
            if (curFile.isDirectory()) {
                addDir(curFile, out, index);
                continue;
            }
            addFile(curFile, out, index);
        }
    }
    
    private static void addFile(File curFile, ZipOutputStream out, int index) 
    throws IOException {
        byte[] tmpBuf = new byte[BUFFER_SIZE];
        FileInputStream in = new FileInputStream(curFile);
        String relativePathName = curFile.getPath().substring(index);
        out.putNextEntry(new ZipEntry(relativePathName));
        int len;
        while ((len = in.read(tmpBuf)) > 0) {
            out.write(tmpBuf, 0, len);
        }
        // Complete the entry
        out.closeEntry();
        in.close();
    }
    
    /**
     * Extract the host name from the url.
     * 
     * @param url
     *            the url
     * @return the host name
     */
    public static String getHostNameFromUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        } else {
            try {
                return url.split("/")[2];
            } catch (Exception e) {
                return null; // Error parsing url.
            }
        }
    }

    /**
     * Used by classes to create a palette of unique colors.
     * @param totalNumberOfUniqueColors - total number of unique colors to be created in the palette.
     */
    public static void setColorPalette(int totalNumberOfUniqueColors) {

        for (int i = 0; i < MAX_ANGLE_CONSTANT; i += MAX_ANGLE_CONSTANT / totalNumberOfUniqueColors) {
            double colorNumberAsAngle = i * GOLDEN_ANGLE;
            double hue = colorNumberAsAngle - Math.floor(colorNumberAsAngle);

            COLOR_PALETTE.add(Color.getHSBColor((float) hue, (float) COLOR_SATURATION, (float) COLOR_BRIGHTNESS));
        }
    }

    /**
     * Used by classes to retrieve a color based on a number (0-10) uses a basic
     * ten color palette.  Greater than 9 uses an unlimited color palette.
     * @param colorNumber - a number which selects the color in the palette.
     * @return - Color object for that number.
     */
    public static Color getColor(int colorNumber) {

        Color colorToBeReturned;

        if (colorNumber > 10) {
            colorToBeReturned = getUnlimitedColor(colorNumber);
        } else {
            colorToBeReturned = getBasicColor(colorNumber);
        }
        return colorToBeReturned;
    }
    
    /**
     * Used by classes to retrieve a color based on a number from an unlimited number of colors.
     * @param colorNumber - number to use.
     * @return - Color object for that number.
     */
    public static Color getUnlimitedColor(int colorNumber) {

        int maxColorsInPalette = COLOR_PALETTE.size();
        Color[] colorsArray = new Color[maxColorsInPalette];
        Color colorToBeReturned;
        
        colorsArray = COLOR_PALETTE.toArray(colorsArray);

        if (colorNumber < 1 || colorsArray.length == 0) {
            colorToBeReturned = Color.BLACK;
        } else if (colorNumber > maxColorsInPalette) {
            colorToBeReturned = Color.BLACK;
        } else {
            colorToBeReturned = colorsArray[colorNumber - 1];
        }
        return colorToBeReturned;
    }
    
    /**
     * Used by classes to retrieve a color based on a number from a ten color palette.
     * (1-10) are colors and anything else returns black.
     * @param colorNumber - number to use.
     * @return - Color object for that number.
     */
    public static Color getBasicColor(int colorNumber) {
        switch(colorNumber) {
            case 1:
                return Color.GREEN;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.RED;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.DARK_GRAY;
            case 6:
                return Color.YELLOW;
            case 7:
                return Color.LIGHT_GRAY;
            case 8:
                return Color.MAGENTA;
            case 9:
                return Color.ORANGE;
            case 10:
                return Color.PINK;
            default:
                return Color.BLACK;
        }
    }
    
    /**
     * Recursive function that goes through all criterion in a CompoundCriterion to determine
     * if any of them are genomic based criterion.
     * @param criterion input for the recursive function.
     * @return T/F value.
     */
    public static boolean isCompoundCriterionGenomic(CompoundCriterion criterion) {
        for (AbstractCriterion abstractCriterion : criterion.getCriterionCollection()) {
            if (abstractCriterion instanceof CompoundCriterion) {
                if (isCompoundCriterionGenomic((CompoundCriterion) abstractCriterion)) {
                    return true;
                }
            } else if (abstractCriterion instanceof GeneNameCriterion) {
                return true;
            } else if (abstractCriterion instanceof FoldChangeCriterion) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Determines if a query has fold change criterion.
     * @param query check to see if this query has fold change criterion.
     * @return T/F value.
     */
    public static boolean isFoldChangeQuery(Query query) {
        return getFoldChangeCriterion(query) != null;
    }

    /**
     * Retrieves the fold change criterion for a given query.
     * @param query to retrieve fold change criterion for.
     * @return the fold change criterion.
     */
    public static FoldChangeCriterion getFoldChangeCriterion(Query query) {
        return getFoldChangeCriterionFromCompoundCriterion(query.getCompoundCriterion());
    }

    private static FoldChangeCriterion getFoldChangeCriterion(AbstractCriterion criterion) {
        if (criterion instanceof FoldChangeCriterion) {
            return (FoldChangeCriterion) criterion;
        } else if (criterion instanceof CompoundCriterion) {
            CompoundCriterion compoundCriterion = (CompoundCriterion) criterion;
            return getFoldChangeCriterionFromCompoundCriterion(compoundCriterion);
        } else {
            return null;
        }
    }

    private static FoldChangeCriterion getFoldChangeCriterionFromCompoundCriterion(
            CompoundCriterion compoundCriterion) {
        for (AbstractCriterion criterion : compoundCriterion.getCriterionCollection()) {
            FoldChangeCriterion foldChangeCriterion = getFoldChangeCriterion(criterion);
            if (foldChangeCriterion != null) {
                return foldChangeCriterion;
            }
        }
        return null;
    }
    
    /**
     * Creates a query for all genomic data in the study, and limits it to the given clinical queries (if any
     * are given).
     * @param studySubscription for querying.
     * @param clinicalQueries to limit genomic data returned based.
     * @return a Query which contains all genomic data for all clinical queries given (if any).
     */
    public static Query createAllGenomicDataQuery(StudySubscription studySubscription, Set<Query> clinicalQueries) {
        Query query = createQuery(studySubscription);
        query.setResultType(ResultTypeEnum.GENOMIC);
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        if (clinicalQueries != null && !clinicalQueries.isEmpty()) {
            CompoundCriterion clinicalCompoundCriterions = new CompoundCriterion();
            clinicalCompoundCriterions.setBooleanOperator(BooleanOperatorEnum.OR);
            for (Query clinicalQuery : clinicalQueries) {
                CompoundCriterion clinicalCompoundCriterion = clinicalQuery.getCompoundCriterion();
                if (isCompoundCriterionGenomic(clinicalCompoundCriterion)) {
                    throw new IllegalArgumentException("Clinical query has genomic criterion");
                }
                clinicalCompoundCriterions.getCriterionCollection().add(clinicalCompoundCriterion);
            }
            query.getCompoundCriterion().getCriterionCollection().add(clinicalCompoundCriterions);
        }
        return query;
    }

    private static Query createQuery(StudySubscription studySubscription) {
        Query query = new Query();
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setSubscription(studySubscription);
        return query;
    }
    
    /**
     * Turns a comma separated list of strings into a List of strings.
     * @param commaDelimitedString comma separated list of strings.
     * @return List of strings.
     */
    public static List<String> createListFromCommaDelimitedString(String commaDelimitedString) {
        return StringUtils.isBlank(commaDelimitedString) ? new ArrayList<String>()
            : Arrays.asList(commaDelimitedString.replaceAll(" ", "").split(","));
    }
    
    /**
     * Goes through all criterion in a query looking for the given abstractCriterionType and returning that set.
     * @param <T> a subclass of AbstractCriterion.
     * @param query to retrieve criterion from.
     * @param abstractCriterionType must be a subclass of AbstractCriterion.
     * @return Set of all criterion matching the given class type.
     */
    public static <T> Set <T> getCriterionTypeFromQuery(Query query, 
            Class<T> abstractCriterionType) {
        Set<T> criterionSet = new HashSet<T>();
        CompoundCriterion compoundCriterion = query.getCompoundCriterion();
        getCriterionFromCompoundCriterion(abstractCriterionType, criterionSet, compoundCriterion);
        return criterionSet;
    }

   @SuppressWarnings("unchecked") // converting T to the class type.
   private static <T> void getCriterionFromCompoundCriterion(Class<T> type,
            Set<T> criterionSet, CompoundCriterion compoundCriterion) {
        for (AbstractCriterion criterion : compoundCriterion.getCriterionCollection()) {
            if (criterion.getClass().equals(type)) {
                criterionSet.add((T) criterion);
            } else if (criterion instanceof CompoundCriterion) {
                getCriterionFromCompoundCriterion(type, criterionSet, (CompoundCriterion) criterion);
            }
            
        }
    }
   
   /**
    * Validates the survival value definition, and throws an exception if it is invalid.
    * @param survivalValueDefinition to validate.
    * @throws InvalidSurvivalValueDefinitionException if the definition is invalid.
    */
   public static void validateSurvivalValueDefinition(SurvivalValueDefinition survivalValueDefinition) 
   throws InvalidSurvivalValueDefinitionException {
       if (survivalValueDefinition.getSurvivalStartDate() == null
            || survivalValueDefinition.getDeathDate() == null 
            || survivalValueDefinition.getLastFollowupDate() == null) {
           throw new InvalidSurvivalValueDefinitionException("Must have a Start Date, Death Date, and Last Followup " 
                   + " Date defined for definition '" + survivalValueDefinition.getName() + "'.");
       }
       if (survivalValueDefinition.getSurvivalStartDate() == survivalValueDefinition.getDeathDate()
           || survivalValueDefinition.getSurvivalStartDate() == survivalValueDefinition.getLastFollowupDate() 
           || survivalValueDefinition.getLastFollowupDate() == survivalValueDefinition.getDeathDate()) {
           throw new InvalidSurvivalValueDefinitionException("Start Date, Death Date, and Last Followup " 
                   + " Date must be unique for definition '" + survivalValueDefinition.getName() + "'.");
       }
       if (!AnnotationTypeEnum.DATE.equals(survivalValueDefinition.getSurvivalStartDate().getDataType())
           || !AnnotationTypeEnum.DATE.equals(survivalValueDefinition.getDeathDate().getDataType())
           || !AnnotationTypeEnum.DATE.equals(survivalValueDefinition.getLastFollowupDate().getDataType())) {
           throw new InvalidSurvivalValueDefinitionException("Start Date, Death Date, and Last Followup "
                   + " Date must all be a 'DATE' type for definition '" + survivalValueDefinition.getName() + "'.");
       }
   }
   
   /**
    * Validates the given SurvivalvalueDefinitions and only returns valid ones.
    * @param survivalValueDefinitions to validate.
    * @return valid survival value definitions.
    */
   public static Set<SurvivalValueDefinition> retrieveValidSurvivalValueDefinitions(
           Collection<SurvivalValueDefinition> survivalValueDefinitions) {
       Set<SurvivalValueDefinition> validDefinitions = new HashSet<SurvivalValueDefinition>();
       for (SurvivalValueDefinition survivalValueDefinition : survivalValueDefinitions) {
           try {
               Cai2Util.validateSurvivalValueDefinition(survivalValueDefinition);
           } catch (InvalidSurvivalValueDefinitionException e) {
              continue;
           }
           validDefinitions.add(survivalValueDefinition);
       }
       return validDefinitions;
   }
    
}
