/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.external.v1_0.AbstractCaArrayEntity;
import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.data.DataSet;
import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caarray.external.v1_0.data.FileStreamableContents;
import gov.nih.nci.caarray.external.v1_0.data.MageTabFileSet;
import gov.nih.nci.caarray.external.v1_0.data.QuantitationType;
import gov.nih.nci.caarray.external.v1_0.experiment.Person;
import gov.nih.nci.caarray.external.v1_0.query.AnnotationSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.BiomaterialKeywordSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.BiomaterialSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.DataSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.ExampleSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.ExperimentSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.FileSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.HybridizationSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.KeywordSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.LimitOffset;
import gov.nih.nci.caarray.external.v1_0.query.QuantitationTypeSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.SearchResult;
import gov.nih.nci.caarray.external.v1_0.sample.AnnotationSet;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.external.v1_0.vocabulary.Category;
import gov.nih.nci.caarray.external.v1_0.vocabulary.Term;
import gov.nih.nci.caarray.services.external.v1_0.InvalidReferenceException;
import gov.nih.nci.caarray.services.external.v1_0.NoEntityMatchingReferenceException;
import gov.nih.nci.caarray.services.external.v1_0.UnsupportedCategoryException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.DataTransferException;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.healthmarketscience.rmiio.RemoteInputStream;

public class ServiceStubFactory implements CaArrayServiceFactory {

    public SearchService createSearchService(ServerConnectionProfile profile) throws ConnectionException {
        return new SearchServiceStub();
    }

    public DataService createDataService(ServerConnectionProfile profile) {
        return new DataServiceStub();
    }

    static class SearchServiceStub implements SearchService {

        public List<Category> getAllCharacteristicCategories(CaArrayEntityReference arg0)
                throws InvalidReferenceException {
            return Collections.emptyList();
        }

        public List<Person> getAllPrincipalInvestigators() {
            return Collections.emptyList();
        }

        public AnnotationSet getAnnotationSet(AnnotationSetRequest arg0) throws InvalidReferenceException {
            return new AnnotationSet();
        }

        public AbstractCaArrayEntity getByReference(CaArrayEntityReference arg0)
                throws NoEntityMatchingReferenceException {
            return null;
        }

        public List<AbstractCaArrayEntity> getByReferences(List<CaArrayEntityReference> arg0)
                throws NoEntityMatchingReferenceException {
            return Collections.emptyList();
        }

        public List<Term> getTermsForCategory(CaArrayEntityReference arg0, String arg1)
                throws InvalidReferenceException {
            return Collections.emptyList();
        }

        public <T extends AbstractCaArrayEntity> SearchResult<T> searchByExample(ExampleSearchCriteria<T> criteria,
                LimitOffset arg1) {
            SearchResult<T> result = new SearchResult<T>();
            result.getResults().add(criteria.getExample());
            return result;
        }

        public SearchResult<Biomaterial> searchForBiomaterials(BiomaterialSearchCriteria arg0, LimitOffset arg1)
                throws InvalidReferenceException, UnsupportedCategoryException {
            return null;
        }

        public SearchResult<Biomaterial> searchForBiomaterialsByKeyword(BiomaterialKeywordSearchCriteria arg0,
                LimitOffset arg1) {
            return null;
        }

        public SearchResult<gov.nih.nci.caarray.external.v1_0.experiment.Experiment> searchForExperiments(
                ExperimentSearchCriteria arg0, LimitOffset arg1) throws InvalidReferenceException,
                UnsupportedCategoryException {
            return null;
        }

        public SearchResult<gov.nih.nci.caarray.external.v1_0.experiment.Experiment> searchForExperimentsByKeyword(
                KeywordSearchCriteria arg0, LimitOffset arg1) {
            return null;
        }

        public SearchResult<File> searchForFiles(FileSearchCriteria arg0, LimitOffset arg1)
                throws InvalidReferenceException {
            return null;
        }

        public SearchResult<Hybridization> searchForHybridizations(HybridizationSearchCriteria arg0, LimitOffset arg1)
                throws InvalidReferenceException {
            return null;
        }

        public List<QuantitationType> searchForQuantitationTypes(QuantitationTypeSearchCriteria arg0)
                throws InvalidReferenceException {
            List<QuantitationType> resultList = new ArrayList<QuantitationType>();
            QuantitationType quantitationType = new QuantitationType();
            quantitationType.setName("DataMatrixCopyNumber.Log2Ratio");
            resultList.add(quantitationType);
            return resultList;
        }

    }

    static class DataServiceStub implements DataService {

        public MageTabFileSet exportMageTab(CaArrayEntityReference arg0) throws InvalidReferenceException,
                DataTransferException {
            return null;
        }

        public DataSet getDataSet(DataSetRequest arg0) throws InvalidReferenceException, InconsistentDataSetsException,
                IllegalArgumentException {
            return new DataSet();
        }

        public RemoteInputStream streamMageTabZip(CaArrayEntityReference arg0, boolean arg1)
                throws InvalidReferenceException, DataTransferException {
            return null;
        }

        public FileStreamableContents streamFileContents(CaArrayEntityReference arg0, boolean arg1)
                throws InvalidReferenceException, DataTransferException {
            return null;
        }

    }

}
