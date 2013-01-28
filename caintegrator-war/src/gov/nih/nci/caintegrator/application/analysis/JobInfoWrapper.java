/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import java.net.URL;

import org.genepattern.webservice.JobInfo;

/**
 * Wraps the <code>JobInfo</code> object.
 */
public class JobInfoWrapper {

    private JobInfo jobInfo;
    private URL url;
    
    
    /**
     * @return the jobInfo
     */
    public JobInfo getJobInfo() {
        return jobInfo;
    }
    /**
     * @param jobInfo the jobInfo to set
     */
    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }
    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
    }
    
}
