/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface to the CaArrayFacade subsystem used to interface with an external caArray 2 server.
 */
public interface CaArrayFacade {

    /**
     * Returns all the samples in the experiment indicated by identifier.
     *
     * @param experimentIdentifier identifies the experiment
     * @param profile contains connection information for the caArray server
     * @return the samples in the experiment.
     * @throws ConnectionException if the subsystem can't connect to the caArray server.
     * @throws ExperimentNotFoundException if the experiment cannot be found.
     */
    List<Sample> getSamples(String experimentIdentifier, ServerConnectionProfile profile)
    throws ConnectionException, ExperimentNotFoundException;

    /**
     * Returns the data for the samples contained in the <code>GenomicDataSourceConfiguration</code>.
     *
     * @param genomicSource retrieve data from this source.
     * @throws ConnectionException if the connection to the caArray server fails.
     * @return the array data values.
     * @throws ConnectionException if the subsystem can't connect to the caArray server.
     * @throws DataRetrievalException if the data couldn't be retrieved from caArray.
     */
    ArrayDataValues retrieveData(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, DataRetrievalException;

    /**
     * Returns the data for the samples contained in the <code>GenomicDataSourceConfiguration</code>.
     *
     * @param genomicSource retrieve data from this source.
     * @param arrayDataService to save array data to.
     * @throws ConnectionException if the connection to the caArray server fails.
     * @return the list of array data values.
     * @throws ConnectionException if the subsystem can't connect to the caArray server.
     * @throws DataRetrievalException if the data couldn't be retrieved from caArray.
     */
    List<ArrayDataValues> retrieveDnaAnalysisData(GenomicDataSourceConfiguration genomicSource,
            ArrayDataService arrayDataService)
    throws ConnectionException, DataRetrievalException;

    /**
     * Retrieves a file from a caArray experiment.
     *
     * @param genomicSource retrieve file from this experiment and server
     * @param filename retrieve the file with this name
     * @return the downloaded file contents.
     * @throws ConnectionException if the server couldn't be reached.
     * @throws FileNotFoundException if the experiment doesn't contain the requested file.
     */
    byte[] retrieveFile(GenomicDataSourceConfiguration genomicSource, String filename)
    throws ConnectionException, FileNotFoundException;

    /**
     * Retrieves a list of files for a caArray experiment.
     * @param genomicSource retrieve the list of files from this experiment and server.
     * @return file list.
     * @throws ConnectionException if the server couldn't be reached.
     * @throws FileNotFoundException if the experiment doesn't contain the requested file.
     */
    List<File> retrieveFilesForGenomicSource(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, FileNotFoundException;


    /**
     * Use this to verify a genomic source will connect and find experiment without
     * throwing an exception.
     * @param genomicSource to test connection.
     * @throws ConnectionException if the server couldn't be reached.
     * @throws ExperimentNotFoundException if the experiment doesn't exist on the server.
     */
    void validateGenomicSourceConnection(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, ExperimentNotFoundException;

    /**
     * Retrieves a map of updated, added, deleted samples for a given experiment.
     * @param experimentIdentifier exp id.
     * @param profile connection profile
     * @return map
     * @throws ConnectionException if the server couldn't be reached.
     * @throws ExperimentNotFoundException if the experiment doesn't contain the requested file.
     */
    Map<String, Date> checkForSampleUpdates(String experimentIdentifier, ServerConnectionProfile profile)
    throws ConnectionException, ExperimentNotFoundException;

    /**
     * Retrieves the date of last data modification for a data source.
     * @param genomicSource data source to retrieve date for
     * @return the date of last data modification for this data source
     * @throws ConnectionException if the server couldn't be reached
     * @throws ExperimentNotFoundException if the experiment doesn't exist on the server
     */
    Date getLastDataModificationDate(GenomicDataSourceConfiguration genomicSource) throws ConnectionException,
        ExperimentNotFoundException;
}
