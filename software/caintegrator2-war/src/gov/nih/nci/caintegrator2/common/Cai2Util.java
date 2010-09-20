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
import gov.nih.nci.caintegrator2.application.study.AbstractClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.AmplificationTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GisticGenomicRegionReporter;

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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * This is a static utility class used by different caIntegrator2 objects. 
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See method retrieveValueFromRowColumn
public final class Cai2Util {
    private static final Integer BUFFER_SIZE = 4096;
    private static final String ZIP_FILE_SUFFIX = ".zip";
    private static final double COLOR_SATURATION = 0.89;
    private static final double COLOR_BRIGHTNESS = 0.89;
    private static final double GOLDEN_ANGLE = 0.381966;
    private static final double MAX_ANGLE_CONSTANT = 360;
    private static final Set<Color> COLOR_PALETTE = new HashSet<Color>();
    private static final double NATURAL_LOG_OF_2 = Math.log(2);
    
    private Cai2Util() { }
    
    /**
     * Trims string if it is greater than the maxCharacters to be '[trimmedString]...'.
     * @param string to possibly trim.
     * @param maxCharacters of the string.
     * @return trimmed string if too long, or full string if it isn't too long.
     */
    public static String trimStringIfTooLong(String string, int maxCharacters) {
        if (maxCharacters <= 3) {
            throw new IllegalArgumentException("maxCharacters has to be at least 4 characters.");
        }
        return (string != null && string.length() > maxCharacters) 
            ? string.substring(0, maxCharacters - 3) + "..." : string;
    }
    
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
     * Turns a comma separated list of strings into a List of strings.
     * @param commaDelimitedString comma separated list of strings.
     * @return List of strings.
     */
    public static List<String> createListFromCommaDelimitedString(String commaDelimitedString) {
        return StringUtils.isBlank(commaDelimitedString) ? new ArrayList<String>()
            : Arrays.asList(commaDelimitedString.replaceAll(" ", "").split(","));
    }
    
   /**
    * Validates the survival value definition, and throws an exception if it is invalid.
    * @param survivalValueDefinition to validate.
    * @throws InvalidSurvivalValueDefinitionException if the definition is invalid.
    */
   public static void validateSurvivalValueDefinition(SurvivalValueDefinition survivalValueDefinition) 
   throws InvalidSurvivalValueDefinitionException {
       if (SurvivalValueTypeEnum.DATE.equals(survivalValueDefinition.getSurvivalValueType())) {
           validateDateTypeSurvivalValueDefinition(survivalValueDefinition);
       } else if (SurvivalValueTypeEnum.LENGTH_OF_TIME.equals(survivalValueDefinition.getSurvivalValueType())) {
           validateLengthTypeSurvivalValueDefinition(survivalValueDefinition);
       }
   }

   private static void validateLengthTypeSurvivalValueDefinition(SurvivalValueDefinition survivalValueDefinition)
       throws InvalidSurvivalValueDefinitionException {
       if (survivalValueDefinition.getSurvivalLength() == null) {
           throw new InvalidSurvivalValueDefinitionException(
                "Must have a Survival Length defined for definition.");
       }
       if (survivalValueDefinition.getSurvivalStatus() != null
               && StringUtils.isBlank(survivalValueDefinition.getValueForCensored())) {
           throw new InvalidSurvivalValueDefinitionException("'Value for Censored' cannot be blank.");
       }
   }
   
   private static void validateDateTypeSurvivalValueDefinition(SurvivalValueDefinition survivalValueDefinition)
        throws InvalidSurvivalValueDefinitionException {
        if (survivalValueDefinition.getSurvivalStartDate() == null
                || survivalValueDefinition.getDeathDate() == null 
                || survivalValueDefinition.getLastFollowupDate() == null) {
               throw new InvalidSurvivalValueDefinitionException(
                       "Must have a Start Date, Death Date, and Last Followup " 
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
   
   /**
    * Compute log2 of a given value.
    * @param value to do log2
    * @return log2 of the input value
    */
   public static double log2(double value) {
       return Math.log(value) / NATURAL_LOG_OF_2;
   }
   
   /**
    * Compute the anti-log2 of a given value.
    * @param value to do anti-log2
    * @return anti-log2 of the input value
    */
   public static double antiLog2(double value) {
       return Math.pow(2, value);
   }
   
   /**
    * Retrieves the last modified date (in String format) of the list of timestamped objects.
    * @param timeStampedObjects list of timestamped objects to retrieve latest date from.
    * @return last modified date of latest timestamped object (in string format).
    */
   public static String retrieveLatestLastModifiedDate(List<TimeStampable> timeStampedObjects) {
       TimeStampable latestTimestamp = null;
       for (TimeStampable currentTimetStampedObject 
               : timeStampedObjects) {
           if (latestTimestamp == null) {
               latestTimestamp = currentTimetStampedObject;
               continue;
           }
           if (currentTimetStampedObject.getDisplayableLastModifiedDate() != null
               && (latestTimestamp.getLastModifiedDate() == null
                 || currentTimetStampedObject.getLastModifiedDate().compareTo(
                         latestTimestamp.getLastModifiedDate()) > 0)) {
               latestTimestamp = currentTimetStampedObject;
           }
       }
       return latestTimestamp == null ? DateUtil.TIMESTAMP_UNAVAILABLE_STRING 
               : latestTimestamp.getDisplayableLastModifiedDate();
   }
   
   /**
    * The editable-select for internet explorer submits an extra URL after comma.
    * ex: "http://url, http://url" instead of just "http://url".
    * @param originalUrl url to convert.
    * @return fixed URL.
    */
   public static String fixUrlForEditableSelect(String originalUrl) {
       return Pattern.compile(",\\s.*").matcher(originalUrl).replaceAll("");
   }
   
   /**
    * Fills the amplifiedGenes and deletedGenes with the amplified and deleted genes associated 
    * with the gisticAnalysis.
    * @param gisticAnalysis to get genes for.
    * @param amplifiedGenes amplified genes associated with gisticAnalysis.
    * @param deletedGenes deleted genes associated with gisticAnalysis.
    */
   public static void retrieveGisticAmplifiedDeletedGenes(GisticAnalysis gisticAnalysis, List<Gene> amplifiedGenes, 
           List<Gene> deletedGenes) {
       if (gisticAnalysis.getReporterList() != null) {
           for (AbstractReporter reporter : gisticAnalysis.getReporterList().getReporters()) {
               if (reporter instanceof GisticGenomicRegionReporter) {
                   GisticGenomicRegionReporter gisticReporter = (GisticGenomicRegionReporter) reporter;
                   if (AmplificationTypeEnum.AMPLIFIED.equals(gisticReporter.getGeneAmplificationType())) {
                       amplifiedGenes.addAll(gisticReporter.getGenes());
                   } else if (AmplificationTypeEnum.DELETED.equals(gisticReporter.getGeneAmplificationType())) {
                       deletedGenes.addAll(gisticReporter.getGenes());
                   }
               }
           }
           Collections.sort(amplifiedGenes);
           Collections.sort(deletedGenes);
       }
   }

   /**
     * @param chromosomeNumber to convert to 1-24 range
     * @return internal value of chromosome number.
     */
    public static String getInternalChromosomeNumber(String chromosomeNumber) {
        if ("X".equalsIgnoreCase(chromosomeNumber)) {
            return "23";
        } else if ("Y".equalsIgnoreCase(chromosomeNumber)) {
            return "24";
        }
        return chromosomeNumber;
    }

    /**
      * @param chromosomeNumber the internal chromosome number
      * @return display value of chromosome number.
      */
     public static String getDisplayChromosomeNumber(String chromosomeNumber) {
         if ("23".equals(chromosomeNumber)) {
             return "X / 23";
         } else if ("24".equals(chromosomeNumber)) {
             return "Y / 24";
         }
         return chromosomeNumber;
     }

    /**
     * @param chromosomeNumber to get the alternate
     * @return the alternate chromosome number if any
     */
    public static String getAlternateChromosome(String chromosomeNumber) {
        if ("X".equalsIgnoreCase(chromosomeNumber)) {
            return "23";
        } else if ("Y".equalsIgnoreCase(chromosomeNumber)) {
            return "24";
        } else if ("23".equals(chromosomeNumber)) {
            return "X";
        } else if ("24".equals(chromosomeNumber)) {
            return "Y";
        }
        return null;
    }
    
    /**
     * 
     * @param studyConfiguration the study.
     * @return true if any subject source is in progress (currently being loaded).
     */
    public static boolean isAnySubjectSourceInProgress(StudyConfiguration studyConfiguration) {
        for (AbstractClinicalSourceConfiguration source : studyConfiguration.getClinicalConfigurationCollection()) {
            if (Status.PROCESSING.equals(source.getStatus())) {
                return true;
            }
        }
        return false;
    }
    /**
     * Read the next data line.
     * @param reader the CSVReader
     * @return the data fields
     * @throws IOException reading error
     */
    public static String[] readDataLine(CSVReader reader) throws IOException {
        String[] fields;
        while ((fields = reader.readNext()) != null) {
            if (!(fields.length == 1 && StringUtils.isBlank(fields[0]))
                    && !fields[0].startsWith("#-#")) {
                return fields;
            }
        }
        return fields;
    }
}
