/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;

/**
 * Copy study.
 * @author mshestopalov
 *
 */
public class CopyStudyHelper {

    private final StudyManagementService studyManagementSvc;;
    /**
     * Const.
     * @param svc study management service.
     */
    public CopyStudyHelper(StudyManagementService svc) {
        studyManagementSvc = svc;
    }

    /**
     * Copy external links.
     * @param copyFrom original study configuration.
     * @param copyTo new study configuration.
     */
    public void copyExternalLinks(StudyConfiguration copyFrom, StudyConfiguration copyTo) {
        if (!copyFrom.getExternalLinkLists().isEmpty()) {

            for (ExternalLinkList extList : copyFrom.getExternalLinkLists()) {
                ExternalLinkList newExtList = new ExternalLinkList();
                newExtList.setDescription(extList.getDescription());
                newExtList.setName(extList.getName());
                newExtList.setFileName(extList.getFileName());

                for (ExternalLink extLink : extList.getExternalLinks()) {
                    ExternalLink newExtLink = new ExternalLink();
                    newExtLink.setCategory(extLink.getCategory());
                    newExtLink.setName(extLink.getName());
                    newExtLink.setUrl(extLink.getUrl());
                    newExtList.getExternalLinks().add(newExtLink);
                }
                copyTo.getExternalLinkLists().add(newExtList);
            }
        }
    }

    /**
     * Copy survival definitions.
     * @param copyFrom original study configuration.
     * @param copyTo new study configuration.
     */
    public void copySurvivalDefinitions(StudyConfiguration copyFrom, StudyConfiguration copyTo) {
        if (CollectionUtils.isNotEmpty(copyFrom.getStudy().getSurvivalValueDefinitionCollection())) {
            for (SurvivalValueDefinition survivalValueDef : copyFrom.getStudy()
                    .getSurvivalValueDefinitionCollection()) {
                SurvivalValueDefinition newSurv = new SurvivalValueDefinition();
                newSurv.setName(survivalValueDef.getName());
                newSurv.setDeathDate(survivalValueDef.getDeathDate());
                newSurv.setLastFollowupDate(survivalValueDef.getLastFollowupDate());
                newSurv.setSurvivalLength(survivalValueDef.getSurvivalLength());
                newSurv.setSurvivalLengthUnits(survivalValueDef.getSurvivalLengthUnits());
                newSurv.setSurvivalStartDate(survivalValueDef.getSurvivalStartDate());
                newSurv.setSurvivalStatus(survivalValueDef.getSurvivalStatus());
                newSurv.setValueForCensored(survivalValueDef.getValueForCensored());
                newSurv.setSurvivalValueType(survivalValueDef.getSurvivalValueType());
                copyTo.getStudy().getSurvivalValueDefinitionCollection().add(newSurv);
            }
        }
    }

    /**
     * Copy Subject Annotation Groups.
     * @param copyFrom original study configuration.
     * @param copyTo new study configuration.
     * @throws ValidationException on error
     * @throws IOException on error
     */
    public void copySubjectAnnotationGroups(StudyConfiguration copyFrom, StudyConfiguration copyTo)
        throws ValidationException, IOException {
        if (!copyFrom.getClinicalConfigurationCollection().isEmpty()) {
            for (AbstractClinicalSourceConfiguration clinicalSource
                    : copyFrom.getClinicalConfigurationCollection()) {

                DelimitedTextClinicalSourceConfiguration orgTextSource =
                        (DelimitedTextClinicalSourceConfiguration) clinicalSource;
                File newFile = orgTextSource.getAnnotationFile().getFile();
                processSubjectAnnotationFile(copyTo, clinicalSource, newFile);
            }
        }
    }

    private void processSubjectAnnotationFile(StudyConfiguration copyTo,
            AbstractClinicalSourceConfiguration clinicalSource,
            File newFile) throws ValidationException, IOException {
        DelimitedTextClinicalSourceConfiguration newClinicalSource = null;
        DelimitedTextClinicalSourceConfiguration orgTextSource =
            (DelimitedTextClinicalSourceConfiguration) clinicalSource;
        if (newFile != null && newFile.exists()) {
            newClinicalSource =
                studyManagementSvc.addClinicalAnnotationFile(copyTo, newFile, newFile.getName(), false);
            newClinicalSource.setLastModifiedDate(new Date());
            newClinicalSource.setStatus(orgTextSource.getStatus());
        } else {
            studyManagementSvc.setStudyLastModifiedByCurrentUser(copyTo, copyTo.getUserWorkspace(),
                    null, LogEntry.getSystemLogSkipSubjAnotCopy(newFile.getPath()));
        }
    }

    /**
     * Copy annotation groups.
     * @param copyFrom original study configuration.
     * @param copyTo new study configuration.
     * @throws ValidationException on error
     * @throws ConnectionException on error
     * @throws IOException on error
     */
    public void copyAnnotationGroups(StudyConfiguration copyFrom, StudyConfiguration copyTo)
        throws ValidationException, ConnectionException, IOException {

        if (CollectionUtils.isNotEmpty(copyFrom.getStudy().getAnnotationGroups())) {
            for (AnnotationGroup orgAnGrp : copyFrom.getStudy().getAnnotationGroups()) {
                AnnotationGroup newGrp = new AnnotationGroup();
                newGrp.setName(orgAnGrp.getName());
                newGrp.setDescription(orgAnGrp.getDescription());
                newGrp.setStudy(copyTo.getStudy());
                studyManagementSvc.saveAnnotationGroup(newGrp, copyTo, null);
                for (AnnotationFieldDescriptor anFieldDesc : orgAnGrp.getAnnotationFieldDescriptors()) {
                    AnnotationFieldDescriptor newAnFieldDesc = new AnnotationFieldDescriptor();
                    newAnFieldDesc.setAnnotationEntityType(anFieldDesc.getAnnotationEntityType());
                    newAnFieldDesc.setAnnotationGroup(newGrp);
                    newAnFieldDesc.setDefinition(anFieldDesc.getDefinition());
                    newAnFieldDesc.setHasValidationErrors(anFieldDesc.isHasValidationErrors());
                    newAnFieldDesc.setUsePermissibleValues(anFieldDesc.isUsePermissibleValues());
                    newAnFieldDesc.setType(anFieldDesc.getType());
                    newAnFieldDesc.setName(anFieldDesc.getName());
                    newAnFieldDesc.setShownInBrowse(anFieldDesc.isShownInBrowse());
                    newGrp.getAnnotationFieldDescriptors().add(newAnFieldDesc);
                }
                copyTo.getStudy().getAnnotationGroups().add(newGrp);
           }
        }
    }

    /**
     * Copy study logo.
     * @param copyFrom original study configuration.
     * @param copyTo new study configuration.
     * @throws IOException on error
     */
    public void copyStudyLogo(StudyConfiguration copyFrom, StudyConfiguration copyTo) throws IOException {
        if (copyFrom.getStudyLogo() != null) {
            File originalFile = new File(copyFrom.getStudyLogo().getPath());
            if (originalFile != null && originalFile.exists()) {
                studyManagementSvc.addStudyLogo(copyTo, originalFile,
                    copyFrom.getStudyLogo().getFileName(), copyFrom.getStudyLogo().getFileType());
            } else if (!copyFrom.getStudyLogo().getPath().isEmpty()) {
                studyManagementSvc.setStudyLastModifiedByCurrentUser(copyTo, copyTo.getUserWorkspace(),
                        null, LogEntry.getSystemLogSkipLogoCopy(copyFrom.getStudyLogo().getPath()));

            }
        }
    }

    /**
     * Copy study genomic source configuration.
     * @param copyFrom original study configuration.
     * @param copyTo new study configuration.
     */
    public void copyStudyGenomicSource(StudyConfiguration copyFrom, StudyConfiguration copyTo) {
        for (GenomicDataSourceConfiguration genomicDsConf : copyFrom.getGenomicDataSources()) {
            GenomicDataSourceConfiguration newGenomicDsConf = new GenomicDataSourceConfiguration();
            newGenomicDsConf.setDataType(genomicDsConf.getDataType());
            newGenomicDsConf.setDataTypeString(genomicDsConf.getDataTypeString());
            newGenomicDsConf.setExperimentIdentifier(genomicDsConf.getExperimentIdentifier());
            copyServerConnectionProfile(genomicDsConf.getServerProfile(), newGenomicDsConf.getServerProfile());
            newGenomicDsConf.setPlatformName(genomicDsConf.getPlatformName());
            newGenomicDsConf.setPlatformVendor(genomicDsConf.getPlatformVendor());
            newGenomicDsConf.setPlatformVendorString(genomicDsConf.getPlatformVendorString());
            newGenomicDsConf.setLoadingType(genomicDsConf.getLoadingType());
            newGenomicDsConf.setLoadingTypeString(genomicDsConf.getLoadingTypeString());
            newGenomicDsConf.setStatus(Status.NOT_MAPPED);
            newGenomicDsConf.setSampleMappingFileName("None Configured");
            newGenomicDsConf.setTechnicalReplicatesCentralTendency(genomicDsConf
                    .getTechnicalReplicatesCentralTendency());
            newGenomicDsConf.setTechnicalReplicatesCentralTendencyString(genomicDsConf
                    .getTechnicalReplicatesCentralTendencyString());
            newGenomicDsConf.setUseHighVarianceCalculation(genomicDsConf.isUseHighVarianceCalculation());
            newGenomicDsConf.setHighVarianceCalculationType(genomicDsConf.getHighVarianceCalculationType());
            newGenomicDsConf.setHighVarianceCalculationTypeString(genomicDsConf
                    .getHighVarianceCalculationTypeString());
            newGenomicDsConf.setHighVarianceThreshold(genomicDsConf.getHighVarianceThreshold());
            newGenomicDsConf.setLastModifiedDate(copyTo.getLastModifiedDate());
            copyTo.getGenomicDataSources().add(newGenomicDsConf);
        }
    }

    private void copyServerConnectionProfile(ServerConnectionProfile copyFrom, ServerConnectionProfile copyTo) {
        copyTo.setHostname(copyFrom.getHostname());
        copyTo.setPassword(copyFrom.getPassword());
        copyTo.setUrl(copyFrom.getUrl());
        copyTo.setPort(copyFrom.getPort());
        copyTo.setUsername(copyFrom.getUsername());
        copyTo.setWebUrl(copyFrom.getWebUrl());
    }

    /**
     * Copy study image source configuration.
     * @param copyFrom original study configuration.
     * @param copyTo new study configuration.
     */
    public void copyStudyImageSource(StudyConfiguration copyFrom, StudyConfiguration copyTo) {
        for (ImageDataSourceConfiguration imageDsConf : copyFrom.getImageDataSources()) {
            ImageDataSourceConfiguration newImageDsConf = new ImageDataSourceConfiguration();
            copyServerConnectionProfile(imageDsConf.getServerProfile(), newImageDsConf.getServerProfile());
            newImageDsConf.setCollectionName(imageDsConf.getCollectionName());
            newImageDsConf.setMappingFileName(imageDsConf.getMappingFileName());
            newImageDsConf.setStatus(Status.NOT_MAPPED);
            newImageDsConf.setLastModifiedDate(copyTo.getLastModifiedDate());
            copyTo.getImageDataSources().add(newImageDsConf);
        }
    }
}
