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
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.domain.AbstractCaArrayObject;
import gov.nih.nci.caarray.domain.data.DataRetrievalRequest;
import gov.nih.nci.caarray.domain.data.DataSet;
import gov.nih.nci.caarray.domain.data.QuantitationType;
import gov.nih.nci.caarray.domain.hybridization.Hybridization;
import gov.nih.nci.caarray.domain.project.Experiment;
import gov.nih.nci.caarray.domain.sample.Extract;
import gov.nih.nci.caarray.domain.sample.LabeledExtract;
import gov.nih.nci.caarray.services.data.DataRetrievalService;
import gov.nih.nci.caarray.services.search.CaArraySearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Implementation of the CaArrayFacade subsystem.
 */
public class CaArrayFacadeImpl implements CaArrayFacade {
    
    private static final Logger LOGGER = Logger.getLogger(CaArrayFacadeImpl.class);
    
    private CaArrayServiceFactory serviceFactory;

    /**
     * {@inheritDoc}
     */
    public List<Sample> getSamples(String experimentIdentifier, ServerConnectionProfile profile) 
    throws ConnectionException {
        CaArraySearchService searchService = getServiceFactory().createSearchService(profile);
        return getSamples(searchService, experimentIdentifier);
    }

    private List<Sample> getSamples(CaArraySearchService searchService, String experimentIdentifier) {
        List<Sample> samples = new ArrayList<Sample>();
        for (gov.nih.nci.caarray.domain.sample.Sample experimentSample 
                : getCaArraySamples(experimentIdentifier, searchService)) {
            samples.add(translateSample(experimentSample));
        }
        return samples;
    }

    private List<gov.nih.nci.caarray.domain.sample.Sample> getCaArraySamples(String experimentIdentifier, 
            CaArraySearchService searchService) {
        gov.nih.nci.caarray.domain.sample.Sample searchSample = new gov.nih.nci.caarray.domain.sample.Sample();
        searchSample.setExperiment(new Experiment());
        searchSample.getExperiment().setPublicIdentifier(experimentIdentifier);
        return searchService.search(searchSample);
    }

    private Sample translateSample(gov.nih.nci.caarray.domain.sample.Sample loadedSample) {
        Sample sample = new Sample();
        sample.setName(loadedSample.getName());
        return sample;
    }

    /**
     * @return the serviceFactory
     */
    public CaArrayServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    /**
     * @param serviceFactory the serviceFactory to set
     */
    public void setServiceFactory(CaArrayServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }
    
    /**
     * Returns the data for the samples contained in the <code>GenomicDataSourceConfiguration</code>.
     * 
     * @param genomicSource retrieve data from this source.
     * @throws ConnectionException if the connection to the caArray server fails.
     * @return the data values.
     */
    public ArrayDataValues retrieveData(GenomicDataSourceConfiguration genomicSource) throws ConnectionException {
        CaArraySearchService searchService = getServiceFactory().createSearchService(genomicSource.getServerProfile());
        DataRetrievalService service = 
            getServiceFactory().createDataRetrievalService(genomicSource.getServerProfile());
        DataRetrievalRequest request = createRequest(genomicSource, searchService);
        DataSet dataSet = service.getDataSet(request);
        LOGGER.info("Retrieved " + dataSet);
        return null;
    }

    private DataRetrievalRequest createRequest(GenomicDataSourceConfiguration genomicSource,
            CaArraySearchService searchService) {
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addQuantitationType(getSignal(searchService));
        addHybridizations(request, genomicSource, searchService);
        return request;
    }

    private void addHybridizations(DataRetrievalRequest request, GenomicDataSourceConfiguration genomicSource,
            CaArraySearchService searchService) {
        List<Sample> samplesToRetrieve;
        if (!genomicSource.getMappedSamples().isEmpty()) {
            samplesToRetrieve = genomicSource.getMappedSamples();
        } else {
            samplesToRetrieve = genomicSource.getSamples();
        }
        for (Sample sample : samplesToRetrieve) {
            gov.nih.nci.caarray.domain.sample.Sample caArraySample = getCaArraySample(sample, 
                    genomicSource.getExperimentIdentifier(), searchService);
            addHybridizations(request, caArraySample, searchService);
        }
    }

    private gov.nih.nci.caarray.domain.sample.Sample getCaArraySample(Sample sample, String experimentIdentifier,
            CaArraySearchService searchService) {
        gov.nih.nci.caarray.domain.sample.Sample searchSample = new gov.nih.nci.caarray.domain.sample.Sample();
        searchSample.setExperiment(new Experiment());
        searchSample.getExperiment().setPublicIdentifier(experimentIdentifier);
        searchSample.setName(sample.getName());
        return searchService.search(searchSample).get(0);
    }

    private void addHybridizations(DataRetrievalRequest request, gov.nih.nci.caarray.domain.sample.Sample caArraySample,
            CaArraySearchService searchService) {
        Set<Extract> extracts = getLoadedCaArrayObjects(caArraySample.getExtracts(), searchService);
        for (Extract extract : extracts) {
            addHybridizations(request, extract, searchService);
        }
    }

    private void addHybridizations(DataRetrievalRequest request, Extract extract, CaArraySearchService searchService) {
        Set<LabeledExtract> labeledExtracts = getLoadedCaArrayObjects(extract.getLabeledExtracts(), searchService);
        for (LabeledExtract labeledExtract : labeledExtracts) {
            addHybridizations(request, labeledExtract);
        }
    }

    private void addHybridizations(DataRetrievalRequest request, LabeledExtract labeledExtract) {
        for (Hybridization hybridization : labeledExtract.getHybridizations()) {
            request.addHybridization(hybridization);
        }
    }

    private <T extends AbstractCaArrayObject> Set<T> getLoadedCaArrayObjects(Set<T> objects, 
            CaArraySearchService searchService) {
        Set<T> loadedObjects = new HashSet<T>(objects.size());
        for (T t : objects) {
            loadedObjects.add(searchService.search(t).get(0));
        }
        return loadedObjects;
    }

    private QuantitationType getSignal(CaArraySearchService searchService) {
        QuantitationType signalSearch = new QuantitationType();
        signalSearch.setName("CHPSignal");
        return searchService.search(signalSearch).iterator().next();
    }

}
