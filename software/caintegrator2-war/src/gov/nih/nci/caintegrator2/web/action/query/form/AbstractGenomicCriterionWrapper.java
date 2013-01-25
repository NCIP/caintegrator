/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;

import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * Wraps access to an <code>AbstractGenomicCriterion</code> subclass instance.
 */
abstract class AbstractGenomicCriterionWrapper extends AbstractCriterionWrapper {
    private static final String PLEASE_SELECT = "-- Please Select --";
    private static final String SYMBOL_LABEL = "Gene Symbol(s) (comma separated list) or blank for all genes";
    private static final String PLATFORM_NAME_LABEL = "Platform Name";
    private final AbstractCriterionRow row;


    AbstractGenomicCriterionWrapper(AbstractCriterionRow row) {
        this.row = row;
    }
    
    protected void setupDefaultGenomicParameters(GenomicCriterionTypeEnum type) {
        getParameters().add(createGeneSymbolParameter());
        if (GenomicCriterionTypeEnum.GENE_EXPRESSION.equals(type)) {
            if (isStudyHasMultipleGeneExpressionPlatforms()) {
                getParameters().add(createPlatformNameParameter(getGeneExpressionPlatformNames()));
            }
        } else {
            if (isStudyHasMultipleCopyNumberPlatforms()) {
                getParameters().add(createPlatformNameParameter(getCopyNumberPlatformNames()));
            }
        }
    }

    @Override
    final AbstractCriterion getCriterion() {
        return getAbstractGenomicCriterion();
    }

    abstract AbstractGenomicCriterion getAbstractGenomicCriterion();

    AbstractCriterionRow getRow() {
        return row;
    }
    
    protected AbstractCriterionParameter createGeneSymbolParameter() {
        TextFieldParameter geneSymbolParameter = new TextFieldParameter(getParameters().size(), getRow().getRowIndex(), 
                getAbstractGenomicCriterion().getGeneSymbol());
        geneSymbolParameter.setLabel(SYMBOL_LABEL);
        geneSymbolParameter.setTitle("Enter a comma separated list of gene symbols ( Ex: EGFR, BRCA1, etc. ) "
                + "or leave it blank for all genes");
        geneSymbolParameter.setGeneSymbol(true);
        geneSymbolParameter.setFoldChangeGeneSymbol(true);
        ValueHandler geneSymbolHandler = new ValueHandlerAdapter() {
            public void valueChanged(String value) {
                getAbstractGenomicCriterion().setGeneSymbol(value);
            }
        };
        geneSymbolParameter.setValueHandler(geneSymbolHandler);
        return geneSymbolParameter;
    }
    
    protected SelectListParameter<String> createPlatformNameParameter(List<String> platformNames) {
        OptionList<String> options = new OptionList<String>();
        String defaultUnknownPlatform = "";
        options.addOption(PLEASE_SELECT, defaultUnknownPlatform);
        for (String platformName : platformNames) {
            options.addOption(platformName, platformName);
        }
        ValueSelectedHandler<String> handler = new ValueSelectedHandler<String>() {
            public void valueSelected(String value) {
                getAbstractGenomicCriterion().setPlatformName(value);
                updateControlParameters();
            }
        };
        String currentPlatformName = StringUtils.isBlank(getAbstractGenomicCriterion().getPlatformName()) 
            ? defaultUnknownPlatform : getAbstractGenomicCriterion().getPlatformName();
        SelectListParameter<String> platformNameParameter = 
            new SelectListParameter<String>(getParameters().size(), getRow().getRowIndex(), 
                                                        options, handler, currentPlatformName);
        platformNameParameter.setLabel(PLATFORM_NAME_LABEL);
        platformNameParameter.setUpdateFormOnChange(platformParameterUpdateOnChange());
        return platformNameParameter;
    }
    
    protected abstract boolean platformParameterUpdateOnChange();

    protected abstract void updateControlParameters();
    
    protected Boolean isStudyHasMultipleGeneExpressionPlatforms() {
        return getRow().getGroup().getForm().isStudyHasMultipleGeneExpressionPlatforms();
    }
    
    protected Boolean isStudyHasMultipleCopyNumberPlatforms() {
        return getRow().getGroup().getForm().isStudyHasMultipleCopyNumberPlatforms();
    }
    
    protected List<String> getGeneExpressionPlatformNames() {
        return getRow().getGroup().getForm().getGeneExpressionPlatformNames();
    }
    
    protected List<String> getCopyNumberPlatformNames() {
        return getRow().getGroup().getForm().getCopyNumberPlatformNames();
    }

}
