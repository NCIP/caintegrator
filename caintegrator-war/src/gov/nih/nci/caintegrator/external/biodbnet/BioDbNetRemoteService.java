/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet;

import gov.nih.nci.caintegrator.external.biodbnet.domain.Db2DbParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbFindParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbOrthoParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbReportParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbWalkParams;

/**
 * Interface for connecting to bioDbNet's provided web services.
 * There's no need to implement this class directly, Spring will set up a proxy for you and handle the implementation.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public interface BioDbNetRemoteService {

    /**
     * Gets all possible bioDBnet input identifiers.
     *
     * @return the comma separated list of all input identifiers
     */
    String getInputs();

    /**
     * Gets all the available outputs for an input identifier.
     *
     * @param input the input identifier
     * @return the comma separated list of available outputs for the given input
     */
    String getOutputsForInput(String input);

    /**
     * Gets all the direct path outputs for an input identifier.
     *
     * @param input the input identifier
     * @return the comma separated list of direct path outputs for the given input
     */
    String getDirectOutputsForInput(String input);

    /**
     * Converts identifiers from one database to other database identifiers or annotations.
     *
     * @param db2dbParams the db2db parameters
     * @return the results of the conversion, tab delimited one result per line with the 1st line being
     * a header of columns
     */
    String db2db(Db2DbParams db2dbParams);

    /**
     * Reports all the database identifiers and annotations that it can find for a particular type of input.
     * In other words, it would be the same as db2db if all of the outputs are selected.
     *
     * @param dbReportParams the dbReport parameters
     * @return returns all the database identifiers and annotations for the given input
     */
    String dbReport(DbReportParams dbReportParams);

    /**
     * bioDBnet dbFind - Finds the type and converted ID of biological identifiers.
     * Given input IDs, a taxon id and an desired output (i.e. Gene ID) it'll return to you a string representing a tab
     * delimited file. The outputted info is : <Input ID> <Input Type> <Id of the desired output>
     *
     * @param dbFindParams the dbFind parameters
     * @return returns the discovered type and id of the given inputs
     */
    String dbFind(DbFindParams dbFindParams);

    /**
     * bioDBnet dbWalk - biological identifier conversions following custom paths.
     *
     * @param dbWalkParams the db walk parameters
     * @return returns the results of the conversion
     */
    String dbWalk(DbWalkParams dbWalkParams);

    /**
     * bioDBnet dbOrtho - identifier conversions across species.
     * Helps users run ortholog conversions where one identifier from one species can be converted to an identifier
     * in a different species. The input and output identifier types can be the same or different
     *
     * @param dbOrthoParams db ortho parameters
     * @return returns the converted identifiers
     */
    String dbOrtho(DbOrthoParams dbOrthoParams);
}
