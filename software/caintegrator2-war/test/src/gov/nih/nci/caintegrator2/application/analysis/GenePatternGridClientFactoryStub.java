/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import org.genepattern.cagrid.service.preprocessdataset.mage.common.PreprocessDatasetMAGEServiceI;
import org.genepattern.cagrid.service.preprocessdataset.mage.stubs.types.InvalidParameterException;
import org.genepattern.gistic.GisticResult;
import org.genepattern.gistic.Marker;
import org.genepattern.gistic.common.GisticI;
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
    }
}
