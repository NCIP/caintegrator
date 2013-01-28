/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interface to the subsystem used store and retrieve microarray data.
 */
public interface ArrayDataService {
    
    /**
     * Stores the values of an <code>ArrayDataValues</code> for later search and retrieval.
     * 
     * @param values the data matrix values to store.
     */
    void save(ArrayDataValues values);

    /**
     * Retrieves the requested array data.
     * 
     * @param request encapsulated retrieval configuration.
     * @return the requested data.
     */
    ArrayDataValues getData(DataRetrievalRequest request);
    
    /**
     * Retrieves the fold change values from the given arrays for the given reporters.
     * 
     * @param request encapsulated retrieval configuration.
     * @param controlArrayValuesList compute fold change values compared to these arrays
     * @param channelType the array data channel type.
     * @return the fold change values.
     */
    ArrayDataValues getFoldChangeValues(DataRetrievalRequest request,
            List<ArrayDataValues> controlArrayValuesList,
            PlatformChannelTypeEnum channelType);
    
    /**
     * Retrieves the fold change values from the given arrays for the given reporters.
     * 
     * @param request encapsulated retrieval configuration.
     * @param query to get controlSampleSets.
     * @return the fold change values.
     */
    ArrayDataValues getFoldChangeValues(DataRetrievalRequest request, Query query);
    
    /**
     * Loads the given array design into the system.
     * 
     * @param platformConfiguration contains the platformSource necessary to load the design
     * @return updated PlatformConfiguration object.
     */
     PlatformConfiguration loadArrayDesign(PlatformConfiguration platformConfiguration);

     /**
      * Returns the platform of name.
      * 
      * @param name the platform name to get.
      * @return the platform.
      */
     Platform getPlatform(String name);

     /**
      * Returns all platforms in alphabetical order.
      * 
      * @return the platforms.
      */
     List<Platform> getPlatforms();
     
     /**
      * 
      * @param study that the platforms exist in.
      * @param sourceType to narrow down the platform type based on GeneExpression or CopyNumber.
      * @return Platforms in the study matching the given source.
      */
     List<Platform> getPlatformsInStudy(Study study, PlatformDataTypeEnum sourceType);
     
     /**
      * 
      * @param study that the platforms exist in.
      * @param sourceType to narrow down the platform type based on GeneExpression or CopyNumber.
      * @return Platforms in the study matching the given source with cghCall.
      */
     List<Platform> getPlatformsWithCghCallInStudy(Study study, PlatformDataTypeEnum sourceType);

     /**
      * Returns the PlatformConfiguration of name.
      * 
      * @param name the platformConfiguration name to get.
      * @return the platformConfiguration.
      */
     PlatformConfiguration getPlatformConfiguration(String name);

     /**
      * Returns all PlatformConfigurations.
      * 
      * @return the platformConfigurations.
      */
     List<PlatformConfiguration> getPlatformConfigurations();

     /**
      * Delete the platform with given id.
      * 
      * @param platformConfigurationId the id of the platform configuration to delete.
      */
     void deletePlatform(Long platformConfigurationId);
     
     /**
      * Saves platform configuration.
      * @param platformConfiguration to save.
      */
     void savePlatformConfiguration(PlatformConfiguration platformConfiguration);
     
     /**
      * Gets refreshed platformConfiguration from database.
      * @param id of platformConfiguration.
      * @return refreshed platformConfiguration.
      */
     PlatformConfiguration getRefreshedPlatformConfiguration(Long id);
     
     /**
      * Deletes GisticAnalysis netCDF file.
      * @param study that contains the Gistic job.
      * @param reporterListId for gistic analysis.
      */
     void deleteGisticAnalysisNetCDFFile(Study study, Long reporterListId);
     
     /**
      * Loads gene location file.
      * @param geneLocationFile to load.
      * @param genomeBuildVersion build version.
      * @return the geneLocationConfiguration created from the file.
      * @throws ValidationException if something is not valid.
      * @throws IOException if problem opening file.
      */
     GeneLocationConfiguration loadGeneLocationFile(File geneLocationFile, GenomeBuildVersionEnum genomeBuildVersion) 
     throws ValidationException, IOException;

    /**
     * @param platform Platform to query for.
     * @return list of studyConfigurations for this Platform
     */
     List<StudyConfiguration> getStudyConfigurationsWhichNeedThisPlatform(Platform platform);
    
}
