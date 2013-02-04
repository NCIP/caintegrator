/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * Wraps a log4j logger object to show start/stop time stamps in a generic log message.
 */
@SuppressWarnings("PMD.LoggerIsNotStaticFinal") // This wraps the logger and it is passed in.
public class TimeLoggerHelper {

    private final Logger logger;
    private Date startTime;
    private boolean isStarted = false;
    private static final Long MILLISECONDS_PER_SECOND = 1000L;

    /**
     * Constructor for wrapping the logger.
     * @param clazz to build the logger.
     */
    public TimeLoggerHelper(Class<?> clazz) {
        this.logger = Logger.getLogger(clazz);
    }

    /**
     * Starts the log timer and displays the data.
     * @param description to display.
     */
    public void startLog(String description) {
        startTime = new Date();
        logger.info("+++ " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(startTime)
                + " Starting " + description + "  +++ ");
        isStarted = true;
    }

    /**
     * Stops the log timer and displays the data.
     * @param description description to display.
     */
    public void stopLog(String description) {
        if (!isStarted) {
            throw new IllegalStateException("Must startLog() before you can stopLog()!");
        }
        Date stopTime = new Date();
        logger.info("+++ " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(stopTime)
                + " Stopping " + description + " +++ ");
        Long deltaTime = stopTime.getTime() - startTime.getTime();
        logger.info("~~~ TOTAL TIME for " + description + " = " + deltaTime / MILLISECONDS_PER_SECOND + " seconds ~~~");
        isStarted = false;
    }

    /**
     * Logs info.
     * @param info to log.
     */
    public void logInfo(String info) {
       logger.info(info);
    }
}
