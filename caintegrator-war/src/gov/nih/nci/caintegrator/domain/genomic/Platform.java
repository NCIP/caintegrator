/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class Platform extends AbstractCaIntegrator2Object implements Comparable<Platform> {

    private static final long serialVersionUID = 1L;

    private String name;
    private String version = "";
    private PlatformVendorEnum vendor;
    private PlatformConfiguration platformConfiguration;
    private SortedSet<ReporterList> reporterListsInternal = new TreeSet<ReporterList>();
    private Map<ReporterTypeEnum, SortedSet<ReporterList>> typeToReporterListMap;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the vendor
     */
    public PlatformVendorEnum getVendor() {
        return vendor;
    }

    /**
     * @param vendor the vendor to set
     */
    public void setVendor(PlatformVendorEnum vendor) {
        this.vendor = vendor;
    }

    /**
     * @return the reporterLists
     */
    public SortedSet<ReporterList> getReporterLists() {
        return Collections.unmodifiableSortedSet(getReporterListsInternal());
    }

    /**
     * Returns all <code>ReporterLists</code> of the requested type.
     *
     * @param reporterType get lists of this type.
     * @return the reporterLists
     */
    public SortedSet<ReporterList> getReporterLists(ReporterTypeEnum reporterType) {
        return Collections.unmodifiableSortedSet(getTypeToReporterListMap().get(reporterType));
    }

    private Map<ReporterTypeEnum, SortedSet<ReporterList>> getTypeToReporterListMap() {
        if (typeToReporterListMap == null) {
            createTypeToReporterListMap();
        }
        return typeToReporterListMap;
    }

    /**
     *
     */
    private void createTypeToReporterListMap() {
        typeToReporterListMap = new HashMap<ReporterTypeEnum, SortedSet<ReporterList>>();
        for (ReporterList reporterList : getReporterListsInternal()) {
            SortedSet<ReporterList> listsForType = typeToReporterListMap.get(reporterList.getReporterType());
            if (listsForType == null) {
                listsForType = new TreeSet<ReporterList>();
                typeToReporterListMap.put(reporterList.getReporterType(), listsForType);
            }
            listsForType.add(reporterList);
        }
    }

    /**
     * Creates a new <code>ReporterList</code> with the given name.
     *
     * @param listName the reporter list name
     * @param reporterType the reporter type in this list
     * @return the reporter list.
     */
    public ReporterList addReporterList(String listName, ReporterTypeEnum reporterType) {
        for (ReporterList reporterList : getReporterLists()) {
            if (reporterList.getName().equals(listName) && reporterList.getReporterType().equals(reporterType)) {
                throw new IllegalArgumentException("ReporterList name " + listName + " is not unique for type "
                        + reporterType + ".");
            }
        }
        ReporterList reporterList = new ReporterList(listName, reporterType);
        reporterList.setPlatform(this);
        this.getReporterListsInternal().add(reporterList);
        return reporterList;
    }

    /**
     * @param reporterList to add.
     */
    public void addReporterList(ReporterList reporterList) {
        this.getReporterListsInternal().add(reporterList);
    }

    /**
     * @return the reporterLists
     */
    public String getReporterListListing() {
        StringBuffer listing = new StringBuffer();
        for (ReporterList reporterList : getReporterLists()) {
            if (listing.length() > 0) {
                listing.append(", ");
            }
            listing.append(reporterList.getName());
        }
        return listing.toString();
    }

    private SortedSet<ReporterList> getReporterListsInternal() {
        return reporterListsInternal;
    }

    /**
     * @param reporterListsInternal the reporterListsInternal to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setReporterListsInternal(SortedSet<ReporterList> reporterListsInternal) {
        this.reporterListsInternal = reporterListsInternal;
    }

    /**
     * Retrieves the displayable (comma separated) array names for the this platform.  Because
     * some of the ReporterLists are duplicate names, have to strip those out so they are only
     * displayed once.
     * @return comma separated array names.
     */
    public String getDisplayableArrayNames() {
        Set<String> arrayNamesUsed = new HashSet<String>();
        StringBuffer arrayNames = new StringBuffer();
        int count = 0;
        for (ReporterList reporterList : getReporterLists()) {
            count++;
            String arrayName = reporterList.getName();
            if (!arrayNamesUsed.contains(arrayName)) {
                arrayNamesUsed.add(arrayName);
                if (count > 1) {
                    arrayNames.append(", ");
                }
                arrayNames.append(arrayName);
            }
        }
        return arrayNames.toString();
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the platformConfiguration
     */
    public PlatformConfiguration getPlatformConfiguration() {
        return platformConfiguration;
    }

    /**
     * @param platformConfiguration the platformConfiguration to set
     */
    public void setPlatformConfiguration(PlatformConfiguration platformConfiguration) {
        this.platformConfiguration = platformConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(Platform o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * Loop through the reporterLists to look for genome version.
     * @return the genome version enum
     */
    public GenomeBuildVersionEnum getGenomeVersion() {
        for (ReporterList reporterList : getReporterLists()) {
            if (!StringUtils.isBlank(reporterList.getGenomeVersion())) {
                return GenomeBuildVersionEnum.matchGenomVersion(reporterList.getGenomeVersion());
            }
        }
        return null;
    }

    /**
     * @return the display string of the genome version.
     */
    public String getDisplayGenomeVersion() {
        GenomeBuildVersionEnum versionEnum = getGenomeVersion();
        return versionEnum == null ? "N/A" : versionEnum.getValue();
    }
}
