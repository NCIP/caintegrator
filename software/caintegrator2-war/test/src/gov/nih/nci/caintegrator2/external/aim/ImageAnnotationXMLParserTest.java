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
package gov.nih.nci.caintegrator2.external.aim;

import static org.junit.Assert.assertEquals;
import edu.northwestern.radiology.aim.ImageAnnotation;

import org.junit.Test;

/**
 * 
 */
public class ImageAnnotationXMLParserTest {

    
    @Test
    public void testRetrieveImageAnnotationFromXMLString() {
        ImageAnnotation imageAnnotation = ImageAnnotationXMLParser.retrieveImageAnnotationFromXMLString("<ImageAnnotation aimVersion=\"TCGA\" codeMeaning=\"Brain tumor target lesion\" codeValue=\"TCGA_2010\" codingSchemeDesignator=\"VASARI\" dateTime=\"2010-05-18T14:51:51\" id=\"0\" name=\"TCGA-06-0132\" uniqueIdentifier=\"1.3.6.1.4.1.25403.1097781229962.4032.20100518025151.3\" xsi:schemaLocation=\"gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM AIM_TCGA09302009_XML.xsd\" xmlns=\"gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><calculationCollection><Calculation codeMeaning=\"Length\" codeValue=\"G-A22A\" codingSchemeDesignator=\"SRT\" description=\"Length\" id=\"0\" uid=\"1.3.6.1.4.1.25403.1097781229962.4032.20100518025151.1\"><referencedCalculationCollection/><calculationResultCollection><CalculationResult id=\"0\" numberOfDimensions=\"1\" type=\"Scalar\" unitOfMeasure=\"mm\"><dataCollection><Data id=\"0\" value=\"90.198742097795\"><coordinateCollection><Coordinate dimensionIndex=\"0\" id=\"0\" position=\"0\"/></coordinateCollection></Data></dataCollection><dimensionCollection><Dimension id=\"0\" index=\"0\" label=\"Value\" size=\"1\"/></dimensionCollection></CalculationResult></calculationResultCollection><referencedGeometricShapeCollection><ReferencedGeometricShape id=\"0\" referencedShapeIdentifier=\"0\"/></referencedGeometricShapeCollection></Calculation><Calculation codeMeaning=\"Length\" codeValue=\"G-A22A\" codingSchemeDesignator=\"SRT\" description=\"Length\" id=\"0\" uid=\"1.3.6.1.4.1.25403.1097781229962.4032.20100518025151.2\"><referencedCalculationCollection/><calculationResultCollection><CalculationResult id=\"0\" numberOfDimensions=\"1\" type=\"Scalar\" unitOfMeasure=\"mm\"><dataCollection><Data id=\"0\" value=\"60.5170235897321\"><coordinateCollection><Coordinate dimensionIndex=\"0\" id=\"0\" position=\"0\"/></coordinateCollection></Data></dataCollection><dimensionCollection><Dimension id=\"0\" index=\"0\" label=\"Value\" size=\"1\"/></dimensionCollection></CalculationResult></calculationResultCollection><referencedGeometricShapeCollection><ReferencedGeometricShape id=\"0\" referencedShapeIdentifier=\"1\"/></referencedGeometricShapeCollection></Calculation></calculationCollection><user><User id=\"0\" loginName=\"praghavan\" name=\"praghavan\" numberWithinRoleOfClinicalTrial=\"5\" roleInTrial=\"Performing\"/></user><equipment><Equipment id=\"0\" manufacturer=\"Northwestern University\" manufacturerModelName=\"AIM_TCGA_v2\" softwareVersion=\"AIM_TCGA09302009v2\"/></equipment><anatomicEntityCollection><AnatomicEntity codeMeaning=\"Temporal lobe\" codeValue=\"RID6476\" codingSchemeDesignator=\"RadLex\" id=\"0\"/><AnatomicEntity codeMeaning=\"Right\" codeValue=\"G-A100\" codingSchemeDesignator=\"SRT\" id=\"0\"/></anatomicEntityCollection><imagingObservationCollection><ImagingObservation codeMeaning=\"mass\" codeValue=\"RID3874\" codingSchemeDesignator=\"RadLex\" id=\"0\"><imagingObservationCharacteristicCollection><ImagingObservationCharacteristic codeMeaning=\"Enhancement Quality : Mark/Avid\" codeValue=\"69\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Proportion Enhancing : &lt; 5%\" codeValue=\"55\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Proportion nCET : 34-67%\" codeValue=\"22\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Proportion Necrosis : 6-33%\" codeValue=\"47\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Proportion of Edema : 34-67%\" codeValue=\"31\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Cysts No\" codeValue=\"36\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Focal\" codeValue=\"13\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Mixed T1&lt;FLAIR Ratio\" codeValue=\"91\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Thickness of the Enhancing Margin Thick/nodular (&gt;3mm)\" codeValue=\"82\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Definition of the Enhancing Margin Well-defined\" codeValue=\"11\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Definition of the Non-Enhancing Margin Indeterminate\" codeValue=\"722\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Hemorrhage Yes\" codeValue=\"106\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Diffusion : Mixed\" codeValue=\"113\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Pial Invasion No\" codeValue=\"115\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Ependymal Extension No\" codeValue=\"120\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Cortical involvement Yes\" codeValue=\"126\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Deep WM Invasion Internal Capsule\" codeValue=\"6\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"nCET Tumor Crosses Midline No\" codeValue=\"62\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Enhancing Tumor Crosses Midline No\" codeValue=\"2\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Satellites No\" codeValue=\"129\" codingSchemeDesignator=\"VASARI\" id=\"0\"/><ImagingObservationCharacteristic codeMeaning=\"Calvarial Remodeling No\" codeValue=\"135\" codingSchemeDesignator=\"VASARI\" id=\"0\"/></imagingObservationCharacteristicCollection></ImagingObservation></imagingObservationCollection><inferenceCollection><Inference codeMeaning=\"No Eloquent Brain\" codeValue=\"85\" codingSchemeDesignator=\"VASARI\" codingSchemeVersion=\"\" id=\"0\" truth=\"false\"/></inferenceCollection><imageReferenceCollection><ImageReference id=\"0\" xsi:type=\"DICOMImageReference\"><study><Study id=\"0\" instanceUID=\"1.3.6.1.4.1.9328.50.45.156068272915163991923280385588217079654\" studyDate=\"1891-04-11T00:00:00\" studyTime=\"000000\"><series><Series id=\"0\" instanceUID=\"1.3.6.1.4.1.9328.50.45.288861537619222119588401224045738055768\"><imageCollection><Image id=\"0\" sopClassUID=\"1.2.840.10008.5.1.4.1.1.4\" sopInstanceUID=\"1.3.6.1.4.1.9328.50.45.277983375086854441363125607879641395349\"/></imageCollection></Series></series></Study></study></ImageReference></imageReferenceCollection><patient><Patient birthDate=\"0001-01-01T00:00:00\" id=\"0\" name=\"141923\" patientID=\"TCGA-06-0132\"/></patient><geometricShapeCollection><GeometricShape id=\"0\" includeFlag=\"true\" shapeIdentifier=\"0\" xsi:type=\"MultiPoint\"><spatialCoordinateCollection><SpatialCoordinate coordinateIndex=\"0\" id=\"0\" imageReferenceUID=\"1.3.6.1.4.1.9328.50.45.277983375086854441363125607879641395349\" referencedFrameNumber=\"1\" x=\"92.9185104370117\" xsi:type=\"TwoDimensionSpatialCoordinate\" y=\"72.0592498779297\"/><SpatialCoordinate coordinateIndex=\"1\" id=\"0\" imageReferenceUID=\"1.3.6.1.4.1.9328.50.45.277983375086854441363125607879641395349\" referencedFrameNumber=\"1\" x=\"97.9753036499024\" xsi:type=\"TwoDimensionSpatialCoordinate\" y=\"168.138259887695\"/></spatialCoordinateCollection></GeometricShape><GeometricShape id=\"0\" includeFlag=\"true\" shapeIdentifier=\"1\" xsi:type=\"MultiPoint\"><spatialCoordinateCollection><SpatialCoordinate coordinateIndex=\"0\" id=\"0\" imageReferenceUID=\"1.3.6.1.4.1.9328.50.45.277983375086854441363125607879641395349\" referencedFrameNumber=\"1\" x=\"66.370361328125\" xsi:type=\"TwoDimensionSpatialCoordinate\" y=\"132.108627319336\"/><SpatialCoordinate coordinateIndex=\"1\" id=\"0\" imageReferenceUID=\"1.3.6.1.4.1.9328.50.45.277983375086854441363125607879641395349\" referencedFrameNumber=\"1\" x=\"130.844436645508\" xsi:type=\"TwoDimensionSpatialCoordinate\" y=\"128.948135375977\"/></spatialCoordinateCollection></GeometricShape></geometricShapeCollection></ImageAnnotation>");
        assertEquals(21, imageAnnotation.getImagingObservationCollection().getImagingObservation().length);
    }

}
