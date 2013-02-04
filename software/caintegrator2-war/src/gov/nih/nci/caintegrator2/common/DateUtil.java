/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * This is a static utility class to handle Date.
 */
public final class DateUtil {
    /**
     * String for timestamp if it is null.
     */
    public static final String TIMESTAMP_UNAVAILABLE_STRING = "Unavailable";
    private static final String TIMESTAMP_FORMAT = "MM/dd/yyyy HH:mm:ss";
    private static final Long MILLISECONDS_PER_SECOND = 1000L;
    private static final Long SECONDS_PER_MINUTE = 60L;
    private static DecimalFormat twoDigit = new DecimalFormat("00");
    private static final int TWELVE_HOURS = 12;
    
    private DateUtil() {
        
    }

    /**
     * Check for timeout based on the date against current time.
     * @param date the date to check for timeout
     * @return boolean
     */
    public static boolean isTimeout(Date date) {
        return DateUtils.addHours(date, TWELVE_HOURS).before(new Date());
    }
    
    /**
     * @param dateString string represent of Date
     * @return Date object
     * @throws ParseException parsing exception
     */
    public static Date createDate(String dateString) throws ParseException {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return (Date) formatter.parse(formatDate(dateString));
    }
    
    private static String formatDate(String dateString) throws ParseException {
        String[] dateElements = (dateString.contains("-"))
            ? dateString.split("-", 3) : dateString.split("/", 3);
        if (dateElements.length != 3) {
            throw new ParseException("Invalid date string: " + dateString, 0);
        }
        return twoDigit.format(Long.valueOf(dateElements[0])) + "/"
            + twoDigit.format(Long.valueOf(dateElements[1])) + "/"
            + dateElements[2];
    }
    
    /**
     * @param date the Date object
     * @return string date in "MM/dd/yyyy" format
     */
    public static String toString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return formatter.format(date);
    }
    
    /**
     * @param dates the list of dates to convert to our display format.
     * @return dates in "MM/dd/yyyy" format
     * @throws ParseException date parsing exception
     */
    public static List<String> toString(List<String> dates) throws ParseException {
        List<String> resultDates = new ArrayList<String>();
        for (String date : dates) {
            resultDates.add(toString(createDate(date)));
        }
        return resultDates;
    }
    
    /**
     * Compare the toString of the 2 dates.
     * 
     * @param date1 first date
     * @param date2 second date
     * @return boolean of string comparison
     */
    @SuppressWarnings("PMD.StringToString") // I thinks I'm calling toString on a string object.
    public static boolean equal(Date date1, Date date2) {
        return DateUtil.toString(date1).equalsIgnoreCase(DateUtil.toString(date2));
    }

    /**
     * @param date the Date object
     * @return string date in "yyyy/MM/dd" format
     */
    public static String toStringForComparison(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return formatter.format(date);
    }
    
    /**
     * Returns the difference between two times in Minutes apart.
     * @param date1 first date.
     * @param date2 second date.
     * @return difference between the dates.
     */
    public static Long compareDatesInMinutes(Date date1, Date date2) {
        return Math.abs(date1.getTime() - date2.getTime()) / MILLISECONDS_PER_SECOND / SECONDS_PER_MINUTE;
    }
    
    /**
     * Used for all of the timestamps in caIntegrator2.
     * @param timeStamp to format to a displayable string.
     * @return displayable timestamp string.
     */
    public static String getDisplayableTimeStamp(Date timeStamp) {
        return timeStamp == null ? TIMESTAMP_UNAVAILABLE_STRING 
                : new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.US).format(timeStamp);
    }
}
