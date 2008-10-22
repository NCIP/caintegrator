package gov.nih.nci.caintegrator2.application.arraydata.netcdf;

import static gov.nih.nci.caintegrator2.application.arraydata.netcdf.NetcdfConstants.*;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayFloat;
import ucar.ma2.DataType;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriteable;


/**
 * This object will create a netCDF file from the tab delimited file that has been provided. The format of the file must
 * be the following: First row is tab delimited with the column labels for annotations and the sample IDs labeling the
 * corresponding sample columns i.e. "reporter id", "chromosome", "physical position", sample id[0] .... sample id[n]
 * The following rows are the columns in the following order: reporter id, chromosome, physical position, copy number
 * value[0] ... copy number value[n]
 * 
 * It creates 2 dimensions, one for reporters, one for samples. It stores the reporter, chromosome and physical position
 * as variables. It stores the copy number values as a variable.
 * 
 * 
 */
public class NetcdfFileWriter {

    /**
     * Length of all netCDF column and row labels.
     */
    public static final int LABEL_LENGTH = 100;
    
    // The name of the array data values we are reading from.
    private final ArrayDataValues arrayDataValues;

    // The name of the netcdf file that is being output.
    private final String outputFile;
    
    

    /**
     * Constructor that takes the input file and output file name.
     * 
     * @param arrayDataValues - Values object which we are retrieving our data from.
     * @param netcdfFilename - Filename to save to.
     */
    public NetcdfFileWriter(ArrayDataValues arrayDataValues, String netcdfFilename) {
        this.arrayDataValues = arrayDataValues;
        this.outputFile = netcdfFilename;

    }

    /**
     * Creates the netCDF file to suit our Reporter -> Array format.
     */
    @SuppressWarnings("PMD.ExcessiveMethodLength")
    public void create() {
        try {
            NetcdfFileWriteable ncfile = NetcdfFileWriteable.createNew(outputFile, true);
            int numMicroArrays = arrayDataValues.getAllArrayDatas().size();
            int numReporters = arrayDataValues.getAllReporters().size();

            Dimension arrayDim = new Dimension(ARRAY_ID, numMicroArrays, true);
            ncfile.addDimension(null, arrayDim);
            Dimension reporterDim = ncfile.addDimension(REPORTER_ID, numReporters);

            Dimension reporterLen = ncfile.addDimension(REPORTER_LEN, LABEL_LENGTH);
            Dimension arrayLen = ncfile.addDimension(ARRAY_LEN, LABEL_LENGTH);

            // Create variables for the dimension labels
            List<Dimension> repLabel = new ArrayList<Dimension>(2);
            repLabel.add(reporterDim);
            repLabel.add(reporterLen);
            ncfile.addVariable(REPORTER_LABEL, DataType.CHAR, repLabel);

            List<Dimension> arrayLabel = new ArrayList<Dimension>(2);
            arrayLabel.add(arrayDim);
            arrayLabel.add(arrayLen);
            ncfile.addVariable(ARRAY_LABEL, DataType.CHAR, arrayLabel);

            // Create variable for copy number values
            ArrayList<Dimension> dims = new ArrayList<Dimension>();
            dims.add(arrayDim);
            dims.add(reporterDim);
            ncfile.addVariable(ARRAY_DATA, DataType.DOUBLE, dims);

            ncfile.create();

            writeData(ncfile, numMicroArrays, numReporters);

            // create the file
            ncfile.close();
        } catch (IOException e) {
            throw new NetcdfCreationException("Problem creating file.", e);
        } catch (InvalidRangeException e) {
            throw new NetcdfCreationException("Range Invalid", e);
        }

    }

    @SuppressWarnings("PMD.ExcessiveMethodLength")
    private void writeData(NetcdfFileWriteable ncfile, int numMicroArrays, int numReporters) throws IOException,
            InvalidRangeException {
        ArrayList<String> microArrayNames = new ArrayList<String>();
        ArrayList<ArrayData> orderedArrayDataList = new ArrayList<ArrayData>();
        for (ArrayData arrayData : arrayDataValues.getAllArrayDatas()) {
            orderedArrayDataList.add(arrayData);
            microArrayNames.add(arrayData.getArray().getName());
        }
        
        List<String> arrayList = new ArrayList<String>(microArrayNames);
        ArrayChar chars = createCharacterList(new ArrayList<String>(arrayList));
        Map<String, Integer> arrayMap = new HashMap<String, Integer>();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayMap.put(arrayList.get(i), i);
        }

        ncfile.write(ARRAY_LABEL, chars);

        // Batch reporters in groups
        ArrayChar reporterCharacters = new ArrayChar.D2(numReporters, LABEL_LENGTH);
        // Create a 2D array of longs and fill it
        ArrayFloat values = new ArrayFloat.D2(numMicroArrays, numReporters);
        List<ReporterRow> rows = retrieveRows(arrayDataValues, orderedArrayDataList);
        int curr = 0;
        for (ReporterRow r : rows) {
            int currentReporter = curr;
            reporterCharacters = appendCharacterList(reporterCharacters, currentReporter, r.getReporterId());
            Index in = values.getIndex();
            for (int i = 0; i < r.getArrayValues().length; i++) {
                values.set(in.set(i, curr), r.getArrayValues()[i]);
            }
            curr++;
        }
        ncfile.write(ARRAY_DATA, values);
        ncfile.write(REPORTER_LABEL, reporterCharacters);
    }

    /**
     * Helper method to create a 2-D array of characters for strings (how netcdf likes it).
     * 
     * @param labels
     * @return
     */
    private static ArrayChar createCharacterList(List<String> labels) {
        String[] testName = new String[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            testName[i] = labels.get(i);
        }

        ArrayChar ac = new ArrayChar.D2(labels.size(), LABEL_LENGTH);

        for (int y = 0; y < labels.size(); y++) {
            ac.setString(y, testName[y]);
        }
        return ac;
    }


    private static ArrayChar appendCharacterList(ArrayChar array, int currPos, String characters) {
        array.setString(currPos, characters);
        return array;
    }

    private List<ReporterRow> retrieveRows(ArrayDataValues values, List<ArrayData> arrayDataList) {
        List<ReporterRow> rows = new ArrayList<ReporterRow>();

        for (AbstractReporter reporter : values.getAllReporters()) {
            ReporterRow row = new ReporterRow();
            row.setReporterId(reporter.getName());
            List<Float> arrayValues = new ArrayList<Float>();
            for (ArrayData arrayData : arrayDataList) {
                arrayValues.add(values.getValue(arrayData, reporter));
            }

            row.setArrayValues(arrayValues.toArray(new Float[arrayValues.size()]));
            rows.add(row);
        }

        return rows;
    }

}
