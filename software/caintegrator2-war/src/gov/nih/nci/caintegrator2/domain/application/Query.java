package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class Query extends AbstractCaIntegrator2Object implements Cloneable, TimeStampable {

    private static final long serialVersionUID = 1L;

    private String description;
    private String name;
    private ReporterTypeEnum reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET;
    private ResultTypeEnum resultType = ResultTypeEnum.CLINICAL;
    private ResultsOrientationEnum orientation = ResultsOrientationEnum.SUBJECTS_AS_ROWS;
    private String visibility;
    private Date lastModifiedDate;
    private StudySubscription subscription;
    private CompoundCriterion compoundCriterion = new CompoundCriterion();
    private Collection<ResultColumn> columnCollection = new HashSet<ResultColumn>();
    private transient boolean needsGenomicHighlighting = true;
    private transient boolean allGenomicDataQuery = false;
    private transient boolean subjectListQuery = false;
    private transient Visibility subjectListVisibility = null;
    private transient List<String> geneSymbolsNotFound = new ArrayList<String>();
    private transient List<String> subjectIdsNotFound = new ArrayList<String>();
    private transient boolean hasMaskedValues = false;
    private transient Platform geneExpressionPlatform;
    private transient Platform copyNumberPlatform;
    private CopyNumberCriterionTypeEnum copyNumberCriterionType = CopyNumberCriterionTypeEnum.SEGMENT_VALUE;


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

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
     * @return the visibility
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the lastModifiedDate
     */
    @Override
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    @Override
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayableLastModifiedDate() {
        return DateUtil.getDisplayableTimeStamp(lastModifiedDate);
    }

    /**
     * @return the subscription
     */
    public StudySubscription getSubscription() {
        return subscription;
    }

    /**
     * @param subscription the subscription to set
     */
    public void setSubscription(StudySubscription subscription) {
        this.subscription = subscription;
    }

    /**
     * @return the compoundCriterion
     */
    public CompoundCriterion getCompoundCriterion() {
        return compoundCriterion;
    }

    /**
     * @param compoundCriterion the compoundCriterion to set
     */
    public void setCompoundCriterion(CompoundCriterion compoundCriterion) {
        this.compoundCriterion = compoundCriterion;
    }

    /**
     * @return the columnCollection
     */
    public Collection<ResultColumn> getColumnCollection() {
        return columnCollection;
    }

    /**
     * @param columnCollection the columnCollection to set
     */
    public void setColumnCollection(Collection<ResultColumn> columnCollection) {
        this.columnCollection = columnCollection;
    }

    /**
     * @return a  boolean of is a gene expression result type
     */
    public boolean isGeneExpressionResultType() {
        return getResultType().equals(ResultTypeEnum.GENE_EXPRESSION);
    }

    /**
     * @return the reporterType
     */
    public ReporterTypeEnum getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(ReporterTypeEnum reporterType) {
        this.reporterType = reporterType;
    }

    /**
     * @return the resultType
     */
    public ResultTypeEnum getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(ResultTypeEnum resultType) {
        this.resultType = resultType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query clone() throws CloneNotSupportedException {
        Query clone = (Query) super.clone();
        clone.setCompoundCriterion(compoundCriterion.clone());
        clone.setColumnCollection(cloneColumnCollection());
        clone.setLastModifiedDate(new Date());
        return clone;
    }

    private Collection<ResultColumn> cloneColumnCollection() throws CloneNotSupportedException {
        Collection<ResultColumn> clone = new HashSet<ResultColumn>();
        for (ResultColumn resultColumn : columnCollection) {
            clone.add(resultColumn.clone());
        }
        return clone;
    }

    /**
     * @return the orientation
     */
    public ResultsOrientationEnum getOrientation() {
        return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation(ResultsOrientationEnum orientation) {
        this.orientation = orientation;
    }

    /**
     * @return the subjectListQuery
     */
    public boolean isSubjectListQuery() {
        return subjectListQuery;
    }

    /**
     * @param subjectListQuery the subjectListQuery to set
     */
    public void setSubjectListQuery(boolean subjectListQuery) {
        this.subjectListQuery = subjectListQuery;
    }

    /**
     * Retrieves all visible columns in the query.
     * @return visible columns in the query.
     */
    public Collection<ResultColumn> retrieveVisibleColumns() {
        List<ResultColumn> columns = new ArrayList<ResultColumn>();
        for (ResultColumn column : getColumnCollection()) {
            if (column.getAnnotationFieldDescriptor() != null
                 && column.getAnnotationFieldDescriptor().isShownInBrowse()) {
                columns.add(column);
            }
        }
        reindexColumns(columns);
        return columns;
    }

    private void reindexColumns(List<ResultColumn> columns) {
        Collections.sort(columns);
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setColumnIndex(i);
        }
    }

    /**
     * @return the geneSymbolsNotFound
     */
    public List<String> getGeneSymbolsNotFound() {
        return geneSymbolsNotFound;
    }

    /**
     * @param geneSymbolsNotFound the geneSymbolsNotFound to set
     */
    public void setGeneSymbolsNotFound(List<String> geneSymbolsNotFound) {
        this.geneSymbolsNotFound = geneSymbolsNotFound;
    }

    /**
     * @return the subjectIdsNotFound
     */
    public List<String> getSubjectIdsNotFound() {
        return subjectIdsNotFound;
    }

    /**
     * @param subjectIdsNotFound the subjectIdsNotFound to set
     */
    public void setSubjectIdsNotFound(List<String> subjectIdsNotFound) {
        this.subjectIdsNotFound = subjectIdsNotFound;
    }

    /**
     * @return the hasMaskedValues
     */
    public boolean isHasMaskedValues() {
        return hasMaskedValues;
    }

    /**
     * @param hasMaskedValues the hasMaskedValues to set
     */
    public void setHasMaskedValues(boolean hasMaskedValues) {
        this.hasMaskedValues = hasMaskedValues;
    }

    /**
     * @return the subjectListVisibility
     */
    public Visibility getSubjectListVisibility() {
        return subjectListVisibility;
    }

    /**
     * @param subjectListVisibility the subjectListVisibility to set
     */
    public void setSubjectListVisibility(Visibility subjectListVisibility) {
        this.subjectListVisibility = subjectListVisibility;
    }


    /**
     * @return the geneExpressionPlatform
     */
    public Platform getGeneExpressionPlatform() {
        return geneExpressionPlatform;
    }

    /**
     * @param geneExpressionPlatform the geneExpressionPlatform to set
     */
    public void setGeneExpressionPlatform(Platform geneExpressionPlatform) {
        this.geneExpressionPlatform = geneExpressionPlatform;
    }

    /**
     * @return the copyNumberPlatform
     */
    public Platform getCopyNumberPlatform() {
        return copyNumberPlatform;
    }

    /**
     * @param copyNumberPlatform the copyNumberPlatform to set
     */
    public void setCopyNumberPlatform(Platform copyNumberPlatform) {
        this.copyNumberPlatform = copyNumberPlatform;
    }

    /**
     * @return T/F is a 2 color channel type platform
     */
    public boolean isTwoChannelType() {
        return PlatformChannelTypeEnum.TWO_COLOR.equals(
                geneExpressionPlatform.getPlatformConfiguration().getPlatformChannelType());
    }

    /**
     * If the query has both copy number and fold change platforms.
     * @return T/F value.
     */
    public boolean isMultiplePlatformQuery() {
        return geneExpressionPlatform != null && copyNumberPlatform != null;
    }

    /**
     * @return the allGenomicDataQuery
     */
    public boolean isAllGenomicDataQuery() {
        return allGenomicDataQuery;
    }

    /**
     * @param allGenomicDataQuery the allGenomicDataQuery to set
     */
    public void setAllGenomicDataQuery(boolean allGenomicDataQuery) {
        this.allGenomicDataQuery = allGenomicDataQuery;
    }

    /**
     * @return the needsGenomicHighlighting
     */
    public boolean isNeedsGenomicHighlighting() {
        return needsGenomicHighlighting;
    }

    /**
     * @param needsGenomicHighlighting the needsGenomicHighlighting to set
     */
    public void setNeedsGenomicHighlighting(boolean needsGenomicHighlighting) {
        this.needsGenomicHighlighting = needsGenomicHighlighting;
    }


    /**
     * @return the copyNumberCriterionType
     */
    public CopyNumberCriterionTypeEnum getCopyNumberCriterionType() {
        return copyNumberCriterionType;
    }

    /**
     * @param copyNumberCriterionType the copyNumberCriterionType to set
     */
    public void setCopyNumberCriterionType(CopyNumberCriterionTypeEnum copyNumberCriterionType) {
        this.copyNumberCriterionType = copyNumberCriterionType;
    }
}