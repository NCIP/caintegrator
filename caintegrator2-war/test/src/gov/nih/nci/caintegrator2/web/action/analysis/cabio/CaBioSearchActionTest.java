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
package gov.nih.nci.caintegrator2.web.action.analysis.cabio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.cabio.CaBioDisplayableGene;
import gov.nih.nci.caintegrator2.external.cabio.CaBioDisplayablePathway;
import gov.nih.nci.caintegrator2.external.cabio.CaBioFacade;
import gov.nih.nci.caintegrator2.external.cabio.CaBioSearchParameters;
import gov.nih.nci.caintegrator2.external.cabio.CaBioSearchTypeEnum;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class CaBioSearchActionTest extends AbstractSessionBasedTest {
    private CaBioSearchAction action;
    private CaBioFacade caBioFacade;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        caBioFacade = mock(CaBioFacade.class);
        when(caBioFacade.retrieveAllTaxons()).thenReturn(new ArrayList<String>());
        when(caBioFacade.retrievePathways(any(CaBioSearchParameters.class))).thenReturn(new ArrayList<CaBioDisplayablePathway>());
        when(caBioFacade.retrieveGenesFromPathways(any(CaBioSearchParameters.class))).thenReturn(Arrays.asList(new CaBioDisplayableGene()));
        action = new CaBioSearchAction();
        action.setWorkspaceService(workspaceService);
        action.setCaBioFacade(caBioFacade);
    }

    @Test
    public void testInput() {
        assertEquals(CaBioSearchAction.SUCCESS, action.input());
    }

    @Test
    public void geneSearchRunNotSelected() throws Exception {
        // If it's not "runSearchSelected" set to true it returns success.
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        verify(caBioFacade, never()).retrieveGenes(any(CaBioSearchParameters.class));
    }

    @Test
    public void geneSearchRunSelected() throws Exception {
        action.setRunSearchSelected(true);
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
    }

    @Test
    public void geneSearchUnderThreeCharacters() throws Exception {
        // If keywords is under 3 characters
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("aa");
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        assertFalse(action.isRunSearchSelected());
        verify(caBioFacade, never()).retrieveGenes(any(CaBioSearchParameters.class));
    }

    @Test
    public void geneSearchNoResults() throws Exception {
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        assertFalse(action.isRunSearchSelected());
        verify(caBioFacade, times(1)).retrieveGenes(any(CaBioSearchParameters.class));
    }

    @Test
    public void geneSearchResultsFound() throws Exception {
        // If keywords exist and something is returned.
        when(caBioFacade.retrieveGenes(any(CaBioSearchParameters.class))).thenReturn(Arrays.asList(new CaBioDisplayableGene()));
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        assertFalse(action.isRunSearchSelected());
        verify(caBioFacade, times(1)).retrieveGenes(any(CaBioSearchParameters.class));
    }

    @Test
    public void searchError() throws Exception {
        when(caBioFacade.retrieveGenes(any(CaBioSearchParameters.class))).thenThrow(new ConnectionException("Connection Error"));
        // Now do a connection exception.
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        assertEquals(CaBioSearchAction.ERROR, action.searchCaBio());
    }

    @Test
    public void searchPathwaysNoResults() {
        // Search on pathways now.
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        action.getSearchParams().setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
    }

    @Test
    public void searchPathways() throws Exception {
        when(caBioFacade.retrievePathways(any(CaBioSearchParameters.class))).thenReturn(Arrays.asList(new CaBioDisplayablePathway()));
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        action.getSearchParams().setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        verify(caBioFacade, times(1)).retrievePathways(any(CaBioSearchParameters.class));
    }

    @Test
    public void searchGenesFromPathways() throws Exception {
        //Now search for genes based on given pathways
        action.setRunSearchSelected(false);
        action.setRunCaBioGeneSearchFromPathways(true);
        action.setCheckedPathwayBoxes("0 ");
        CaBioDisplayablePathway pathway = new CaBioDisplayablePathway();
        pathway.setId("1");

        // First without any pathways selected.
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        action.getCaBioPathways().add(pathway);

        // Now with pathways.
        action.setRunSearchSelected(false);
        action.setRunCaBioGeneSearchFromPathways(true);
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        verify(caBioFacade, times(1)).retrieveGenesFromPathways(any(CaBioSearchParameters.class));
    }

}
