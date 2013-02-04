/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.duke.cabig.rproteomics.model.statml.Data;
import edu.wustl.icr.asrv1.common.GenomeAnnotationInformation;
import edu.wustl.icr.asrv1.segment.ChromosomalSegment;
import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.mageom.domain.bioassay.BioAssay;
import gridextensions.ClassMembership;
import gridextensions.ComparativeMarkerSelectionParameterSet;
import gridextensions.ComparativeMarkerSelectionResultCollection;
import gridextensions.MarkerResult;
import gridextensions.PreprocessDatasetParameterSet;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.cabig.icr.asbp.parameter.ParameterList;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.genepattern.cagrid.service.compmarker.mage.common.ComparativeMarkerSelMAGESvcI;
import org.genepattern.cagrid.service.compmarker.mage.context.client.ComparativeMarkerSelMAGESvcContextClient;
import org.genepattern.cagrid.service.preprocessdataset.mage.common.PreprocessDatasetMAGEServiceI;
import org.genepattern.cagrid.service.preprocessdataset.mage.context.client.PreprocessDatasetMAGEServiceContextClient;
import org.genepattern.cagrid.service.preprocessdataset.mage.stubs.types.InvalidParameterException;
import org.genepattern.gistic.GisticResult;
import org.genepattern.gistic.Marker;
import org.genepattern.gistic.common.GisticI;
import org.genepattern.gistic.context.client.GisticContextClient;
import org.genepattern.pca.PCAResult;
import org.genepattern.pca.common.PCAI;
import org.genepattern.pca.context.client.PCAContextClient;
import org.genepattern.pca.context.stubs.types.AnalysisNotComplete;
import org.genepattern.pca.context.stubs.types.CannotLocateResource;
import org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse;
import org.oasis.wsrf.properties.GetMultipleResourceProperties_Element;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;
import org.oasis.wsrf.properties.QueryResourceProperties_Element;

/**
 * 
 */
public class GenePatternGridClientFactoryStub implements GenePatternGridClientFactory {

    public static Marker[] GISTIC_MARKERS_INPUT = null;
    public static SampleWithChromosomalSegmentSet[] GISTIC_SAMPLES_INPUT = null;
    
    public PreprocessDatasetMAGEServiceI createPreprocessDatasetClient(ServerConnectionProfile server)
            throws ConnectionException {
        
        return new PreprocessDatasetMAGEServiceStub();
    }
    
    private static class PreprocessDatasetMAGEServiceStub implements PreprocessDatasetMAGEServiceI {

        public GetMultipleResourcePropertiesResponse getMultipleResourceProperties(
                GetMultipleResourceProperties_Element params) throws RemoteException {
            return null;
        }

        public GetResourcePropertyResponse getResourceProperty(QName arg0) throws RemoteException {
            return null;
        }

        public BioAssay[] performAnalysis(BioAssay[] bioAssay,
                PreprocessDatasetParameterSet preprocessDatasetParameterSet) throws RemoteException,
                InvalidParameterException {
            return bioAssay;
        }

        public QueryResourcePropertiesResponse queryResourceProperties(QueryResourceProperties_Element arg0)
                throws RemoteException {
            return null;
        }

        public PreprocessDatasetMAGEServiceContextClient createAnalysis() throws RemoteException, MalformedURIException {
            return new PreprocessContextClientStub("http://test");
        }
        
    }
    
    private static class PreprocessContextClientStub extends PreprocessDatasetMAGEServiceContextClient {
        
        public PreprocessContextClientStub(String url) throws MalformedURIException, RemoteException {
            super(url);
        }
        
        @Override
        public TransferServiceContextReference submitData(ParameterList parameterList) throws RemoteException,
                org.genepattern.cagrid.service.preprocessdataset.mage.context.stubs.types.CannotLocateResource {
            return new TransferServiceContextReferenceStub();
        }
        
        @Override
        public TransferServiceContextReference getResult() throws RemoteException ,org.genepattern.cagrid.service.preprocessdataset.mage.context.stubs.types.CannotLocateResource ,org.genepattern.cagrid.service.preprocessdataset.mage.context.stubs.types.AnalysisNotComplete {
            return new TransferServiceContextReferenceStub();
            
        }
    }

    public ComparativeMarkerSelMAGESvcI createComparativeMarkerSelClient(ServerConnectionProfile server)
            throws ConnectionException {
        return new ComparativeMarkerSelStub();
    }
    
    private static class ComparativeMarkerSelStub implements ComparativeMarkerSelMAGESvcI {

        public GetMultipleResourcePropertiesResponse getMultipleResourceProperties(
                GetMultipleResourceProperties_Element params) throws RemoteException {
            return null;
        }

        public GetResourcePropertyResponse getResourceProperty(QName arg0) throws RemoteException {
            return null;
        }

        
        public ComparativeMarkerSelectionResultCollection performAnalysis(BioAssay[] bioAssay,
                ClassMembership classMembership,
                ComparativeMarkerSelectionParameterSet comparativeMarkerSelectionParameterSet) throws RemoteException,
                org.genepattern.cagrid.service.compmarker.mage.stubs.types.InvalidParameterException {
            ComparativeMarkerSelectionResultCollection result = new ComparativeMarkerSelectionResultCollection();
            
            MarkerResult[] results = new MarkerResult[1];
            results[0] = new MarkerResult();
            results[0].setDescription("test");
            result.setMarkerResult(results);
            return result;
        }

        public QueryResourcePropertiesResponse queryResourceProperties(QueryResourceProperties_Element arg0)
                throws RemoteException {
            return null;
        }

        public ComparativeMarkerSelMAGESvcContextClient createAnalysis() throws RemoteException, MalformedURIException {
            return null;
        }
        
    }
    

    public PCAI createPCAClient(ServerConnectionProfile server) throws ConnectionException {
        return new PcaClientStub();
    }

    private static class PcaClientStub implements PCAI {


        public PCAContextClient createAnalysis() throws RemoteException, MalformedURIException {
            return new PCAContextClientStub("http://test");
        }

        public GetMultipleResourcePropertiesResponse getMultipleResourceProperties(
                GetMultipleResourceProperties_Element params) throws RemoteException {
            return null;
        }

        public GetResourcePropertyResponse getResourceProperty(QName arg0) throws RemoteException {
            return null;
        }

        public PCAResult performAnalysis(Data data, ParameterList parameterList) throws RemoteException {
            return null;
        }

        public QueryResourcePropertiesResponse queryResourceProperties(QueryResourceProperties_Element arg0)
                throws RemoteException {
            return null;
        }
        
    }
    
    private static class PCAContextClientStub extends PCAContextClient {

        public PCAContextClientStub(String url) throws MalformedURIException, RemoteException {
            super(url);
        }
        
        @Override
        public TransferServiceContextReference submitData(ParameterList parameterList) throws RemoteException,
                CannotLocateResource {
            return new TransferServiceContextReferenceStub();
        }
        
        @Override
        public TransferServiceContextReference getResult() throws RemoteException, AnalysisNotComplete,
                CannotLocateResource {
            return new TransferServiceContextReferenceStub();
        }
    }
    
    @SuppressWarnings("serial")
    private static class TransferServiceContextReferenceStub extends TransferServiceContextReference {
        
        @Override
        public EndpointReferenceType getEndpointReference() {
            return new EndpointReferenceType();
        }
    }

    public GisticI createGisticClient(ServerConnectionProfile server) throws ConnectionException {
        return new GisticClientStub();
    }
    
    private static class GisticClientStub implements GisticI {
        public GetMultipleResourcePropertiesResponse getMultipleResourceProperties(
                GetMultipleResourceProperties_Element params) throws RemoteException {
            
            return null;
        }

        public GetResourcePropertyResponse getResourceProperty(QName arg0) throws RemoteException {
            return null;
        }

        public QueryResourcePropertiesResponse queryResourceProperties(QueryResourceProperties_Element arg0)
                throws RemoteException {
            return null;
        }

        public GisticResult[] runAnalysis(ParameterList parameterList, ChromosomalSegment[] cnvSegmentsToIgnore,
                GenomeAnnotationInformation genomeBuild, Marker[] markers, SampleWithChromosomalSegmentSet[] samples)
                throws RemoteException, org.genepattern.gistic.stubs.types.InvalidParameterException {
            GISTIC_MARKERS_INPUT = markers;
            GISTIC_SAMPLES_INPUT = samples;
            GisticResult[] gisticResult = new GisticResult[1];
            gisticResult[0] = new GisticResult();
            return gisticResult;
        }

        public GisticContextClient createAnalysis() throws RemoteException, MalformedURIException {
            return null;
        }        
    }
}
