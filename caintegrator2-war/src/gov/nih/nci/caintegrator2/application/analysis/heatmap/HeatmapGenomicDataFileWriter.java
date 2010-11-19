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
package gov.nih.nci.caintegrator2.application.analysis.heatmap;

import gov.nih.nci.caintegrator2.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator2.domain.genomic.GeneChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.heatmap.GeneLocationWrapper;
import gov.nih.nci.caintegrator2.heatmap.HeatMapArgs;
import gov.nih.nci.caintegrator2.heatmap.SegmentDataWrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Calls cbs2heatmap algorithm to write the data file for the input segment datas.
 */
public class HeatmapGenomicDataFileWriter {
    private static final Character TAB = '\t';
    private static final Character NEW_LINE = '\n';
    private static final String HEADER = "BinName" + TAB + "c1" + TAB + "c2";
    private static final Integer NUMBER_CHROMOSOMES = 24;
    private final Collection<SegmentData> segmentDatas;
    private final GeneLocationConfiguration geneLocationConfiguration;
    private final HeatmapParameters parameters; 
    private final CBSToHeatmapFactory cbsToHeatmapFactory;
    private final Map<String, Integer> chromosomeGeneSize = new HashMap<String, Integer>();
    
    
    /**
     * @param segmentDatas the segment data to run cbs to heatmap algorithm on.
     * @param geneLocationConfiguration to look up gene chromosome locations.
     * @param parameters for heatmap.
     * @param cbsToHeatmapFactory factory that creates CBSToHeatmap object, which runs cbsToHeatmap algorithm.
     */
    public HeatmapGenomicDataFileWriter(Collection<SegmentData> segmentDatas,
            GeneLocationConfiguration geneLocationConfiguration, HeatmapParameters parameters, 
            CBSToHeatmapFactory cbsToHeatmapFactory) {
        this.segmentDatas = segmentDatas;
        this.geneLocationConfiguration = geneLocationConfiguration;
        this.parameters = parameters;
        this.cbsToHeatmapFactory = cbsToHeatmapFactory;
        chromosomeGeneSize.clear();
    }
    
    /**
     * Writes genomic data files to the given path.
     * @param genomoicDataFilePath path for genomic data file.
     * @param layoutFilePath path for layout file(chr2genesize).
     * @throws IOException if unable to write files.
     */
    public void writeGenomicDataFiles(String genomoicDataFilePath, String layoutFilePath) throws IOException {
        writeGenomicDataFile(genomoicDataFilePath);
        writeLayoutFile(layoutFilePath);
    }
    
    private void writeGenomicDataFile(String genomicDataFilePath) throws IOException {
        HeatMapArgs hma = new HeatMapArgs();
        hma.setBigBinFile(parameters.getLargeBinsFile());
        hma.setSmallBinFile(parameters.getSmallBinsFile());
        hma.setGeneOutFile(genomicDataFilePath); // If we want to do gene based.
        hma.getGeneLocations().addAll(convertGeneLocations(geneLocationConfiguration.getGeneLocations()));
        hma.getSegmentDatas().addAll(convertSegmentDatas());
        cbsToHeatmapFactory.getCbsToHeatmap().runCBSToHeatmap(hma);
    }
    
    private void writeLayoutFile(String layoutFilePath) throws IOException {
        FileWriter writer = new FileWriter(layoutFilePath);
        writer.write(HEADER + NEW_LINE);
        Integer prevEndValue = writeChromosomeLine(writer, "1", 1);
        for (Integer curChromNum = 2; curChromNum <= NUMBER_CHROMOSOMES - 2; curChromNum++) {
            prevEndValue = writeChromosomeLine(writer, String.valueOf(curChromNum), prevEndValue + 1);
        }
        prevEndValue = writeChromosomeLine(writer, "X", prevEndValue + 1);
        prevEndValue = writeChromosomeLine(writer, "Y", prevEndValue + 1);
        writer.flush();
        writer.close();
    }
    
    private Integer writeChromosomeLine(FileWriter writer, String chromosome, Integer startNumber) throws IOException {
        Integer numberGenes = chromosomeGeneSize.get(chromosome);
        if (numberGenes == null) {
            numberGenes = 0;
        }
        Integer endNumber = startNumber + numberGenes - 1;
        writer.write("chr" + chromosome + TAB + startNumber + TAB + endNumber + NEW_LINE);
        return endNumber;
    }
    
    private List<GeneLocationWrapper> convertGeneLocations(Collection<GeneChromosomalLocation> geneLocations) {
        List<GeneLocationWrapper> geneLocationWrappers = new ArrayList<GeneLocationWrapper>();
        for (GeneChromosomalLocation geneLocation : geneLocations) {
            GeneLocationWrapper geneLocationWrapper = new GeneLocationWrapper();
            geneLocationWrapper.setChromosome(geneLocation.getLocation().getChromosome());
            geneLocationWrapper.setEndPosition(geneLocation.getLocation().getEndPosition());
            geneLocationWrapper.setStartPosition(geneLocation.getLocation().getStartPosition());
            geneLocationWrapper.setGeneSymbol(geneLocation.getGeneSymbol());
            geneLocationWrappers.add(geneLocationWrapper);
            increaseChromosomeSize(geneLocation.getLocation().getChromosome());
        }
        return geneLocationWrappers;
    }
    
    private List<SegmentDataWrapper> convertSegmentDatas() {
        List<SegmentDataWrapper> segmentDataWrappers = new ArrayList<SegmentDataWrapper>();
        for (SegmentData segmentData : segmentDatas) {
            SegmentDataWrapper segmentDataWrapper = new SegmentDataWrapper();
            segmentDataWrapper.setChromosome(segmentData.getLocation().getChromosome());
            segmentDataWrapper.setNumberOfMarkers(segmentData.getNumberOfMarkers());
            segmentDataWrapper.setSampleIdentifier(segmentData.getArrayData().getSample().getName());
            segmentDataWrapper.setSegmentValue(segmentData.getSegmentValue());
            segmentDataWrapper.setStartPosition(segmentData.getLocation().getStartPosition());
            segmentDataWrapper.setStopPosition(segmentData.getLocation().getEndPosition());
            segmentDataWrappers.add(segmentDataWrapper);
        }
        return segmentDataWrappers;
    }
    
    private void increaseChromosomeSize(String chromosome) {
        if (!chromosomeGeneSize.containsKey(chromosome)) {
            chromosomeGeneSize.put(chromosome, 0);
        }
        chromosomeGeneSize.put(chromosome, chromosomeGeneSize.get(chromosome) + 1);
    }
}
