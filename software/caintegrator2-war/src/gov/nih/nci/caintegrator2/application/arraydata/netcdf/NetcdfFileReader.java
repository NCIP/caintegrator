package gov.nih.nci.caintegrator2.application.arraydata.netcdf;

import static gov.nih.nci.caintegrator2.application.arraydata.netcdf.NetcdfConstants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * This object will read a netcdf file with the copy number format of sample id and reporter id dimensions with
 * variables representing the chromosome and physical positions.
 * 
 * 
 */
public class NetcdfFileReader {

    // The name of the netCDF file
    private final String fileName;

    // The netCDF file itself
    private NetcdfFile ncFile = null;

    // Cached map of reporter ids to location within the netCDF array
    private Map<String, Integer> reporters;

    // Cached map of array ids to location within the netCDF array
    private Map<String, Integer> arrays;

    // Cached array of reporters from the file
    private String[] reporterArray;

    // Number of reporters
    private int numReporters = 0;


    /**
     * Constructor given a filename, opens and reads the file.
     * @param fileName - filename to read in.
     * @throws NetcdfReadException - Exception when trying to read file.
     */
    public NetcdfFileReader(String fileName) throws NetcdfReadException {
        this.fileName = fileName;
        openFile();
    }

    /**
     * Opens file.
     * @throws NetcdfReadException - Exception when trying to read file..
     */
    public void open() throws NetcdfReadException {
        openFile();
    }

    /**
     * Closes file.
     * @throws IOException - Exception when trying to read file.
     */
    public void close() throws IOException {
        if (null != ncFile) {
            ncFile.close();
        }
    }

    /**
     * This method opens the netCDF file and reads in the reporter ids, sample ids, chromosomes and locations to hold
     * them within the object for performance enhancements of multiple reads.
     */
    private void openFile() throws NetcdfReadException {
        try {
            ncFile = null;
            ncFile = NetcdfFile.open(fileName);

            reporters = readLabelVariable(REPORTER_LABEL, REPORTER_ID, ncFile);
            // reporterArray = new String[reporters.size()];
            reporterArray = new String[numReporters];

            for (String reporter : reporters.keySet()) {
                Integer pos = reporters.get(reporter);
                reporterArray[pos] = reporter;
            }
            arrays = readLabelVariable(ARRAY_LABEL, ARRAY_ID, ncFile);
        } catch (java.io.IOException e) {
            throw new NetcdfReadException(e);
        } catch (InvalidRangeException e) {
            throw new NetcdfReadException(e);
        }
    }

    /**
     * Gets array data value given the array name and reporter.
     * @param arrayName - Name of the array column.
     * @param reporter - Name of the reporter column.
     * @return Long value for this reporter/array combination.
     * @throws IOException - Read exception.
     * @throws InvalidRangeException - Range exception.
     */
    public Long getArrayData(String arrayName, String reporter) throws IOException, InvalidRangeException {
        Variable arrayData = ncFile.findVariable(ARRAY_DATA);
        int[] origin = new int[] {arrays.get(arrayName), reporters.get(reporter)};
        int[] size = new int[] {1, 1};
        Array data3D = arrayData.read(origin, size);
        Array data2D = data3D.reduce();
        Index index = data2D.getIndex();
        return data2D.getLong(index);
    }

    /**
     * Reads from the netCDF file to create a list of ReporterRow objects that will contain the array
     * value information.  This function assumes all reporters are used for only the array names given.
     * 
     * @param arrayNames - List of arrays we wish to return rows for, assumes all reporters.
     * @return - List of reporter rows for the given array names.
     * @throws NetcdfReadException - Exception when trying to read file.
     */
    public List<ReporterRow> getArrayDataForArrays(List<String> arrayNames) throws NetcdfReadException {
        // allSamples used to be a Long[][] but so that I could clear up memory, I
        // transformed it into this
        HashMap<Integer, List<Long>> allSamples = new HashMap<Integer, List<Long>>();
        try {
            for (int i = 0; i < arrayNames.size(); i++) {
                String sampleId = arrayNames.get(i);
                List<Long> copyNumberForSample = getArrayDataForArray(sampleId, ncFile);

                allSamples.put(i, copyNumberForSample);

            }
        } catch (IOException e) {
            throw new NetcdfReadException(e);
        } catch (InvalidRangeException e) {
            throw new NetcdfReadException(e);
        }

        List<ReporterRow> returnList = new ArrayList<ReporterRow>();
        fillReturnListForArrays(arrayNames, allSamples, returnList);

        return returnList;
    }

    /**
     * @param arrayNames
     * @param allSamples
     * @param returnList
     */
    private void fillReturnListForArrays(List<String> arrayNames, Map<Integer, List<Long>> allSamples,
            List<ReporterRow> returnList) {
        // Start from the end so we can delete the items off the end of the
        for (int i = allSamples.get(0).size() - 1; i >= 0; i--) {
            ReporterRow r = new ReporterRow();
            r.setReporterId(reporterArray[i]);
            Long[] arrayValues = new Long[arrayNames.size()];
            for (int j = 0; j < allSamples.size(); j++) {
                arrayValues[j] = allSamples.get(j).get(i);
                // Remove that element to free up memory
                allSamples.get(j).remove(i);
            }
            r.setArrayValues(arrayValues);
            // Since we are decrementing through the list, I am adding them into the front of the list
            returnList.add(0, r);
        }
    }

    /**
     * Reads from the netCDF file to create a list of ReporterRow objects that will contain the array
     * value information.  This function assumes all arrays are used for only the reporter names given.
     * 
     * @param reporterNames - List of reporters we wish to return rows for, assumes all arrays.
     * @return - List of reporter rows for the given reporter names.
     * @throws NetcdfReadException - Exception when trying to read file.
     */
    public List<ReporterRow> getArrayDataForReporters(List<String> reporterNames) throws NetcdfReadException {

        List<ReporterRow> returnList = new ArrayList<ReporterRow>();
        try {
            for (int i = 0; i < reporterNames.size(); i++) {
                String reporterId = reporterNames.get(i);
                double[] arrayValueForReporter = getArrayDataForReporter(reporterId, ncFile);

                ReporterRow r = new ReporterRow();
                r.setReporterId(reporterId);
                r.setArrayValues(createLongArray(arrayValueForReporter));
                returnList.add(r);

            }
        } catch (IOException e) {
            throw new NetcdfReadException(e);
        } catch (InvalidRangeException e) {
            throw new NetcdfReadException(e);
        }

        return returnList;
    }


    private List<Long> getArrayDataForArray(String arrayName, NetcdfFile nc) throws IOException, InvalidRangeException {
        Variable copyNumber = nc.findVariable(ARRAY_DATA);
        int[] origin = new int[] {arrays.get(arrayName), 0};
        int[] size = new int[] {1, reporters.size()};
        Array data3D = copyNumber.read(origin, size);
        Array data2D = data3D.reduce();
        double[] doubleArray = (double[]) data2D.copyTo1DJavaArray();
        ArrayList<Long> myArrayList = new ArrayList<Long>();
        for (int i = 0; i < doubleArray.length; i++) {
            // expect an Object as argument, but automatically changed to Long
            myArrayList.add(Math.round(doubleArray[i]));
        }

        doubleArray = null;
        return myArrayList;

    }


    private double[] getArrayDataForReporter(String reporter, NetcdfFile nc) throws IOException, InvalidRangeException {
        Variable copyNumber = nc.findVariable(ARRAY_DATA);
        int[] origin = new int[] {0, reporters.get(reporter)};
        int[] size = new int[] {arrays.size(), 1};
        Array data3D = copyNumber.read(origin, size);
        Array data2D = data3D.reduce();
        return (double[]) data2D.copyTo1DJavaArray();
    }

    private Map<String, Integer> readLabelVariable(String label, String dimensionName, NetcdfFile file)
            throws IOException, InvalidRangeException {

        Map<String, Integer> labelMap = new HashMap<String, Integer>();
        Variable labels = file.findVariable(label);

        @SuppressWarnings("unchecked")
        List<Dimension> dimensions = labels.getDimensions();
        int dimensionLength = 0;
        for (int i = 0; i < dimensions.size(); i++) {
            Dimension d = dimensions.get(i);
            if (dimensionName.equals(d.getName())) {
                dimensionLength = d.getLength();
            }
        }
        numReporters = dimensionLength;
        for (int i = 0; i < dimensionLength; i++) {
            int[] origin = new int[] {i, 0};
            int[] size = new int[] {1, NetcdfFileWriter.LABEL_LENGTH};
            Array data3D = labels.read(origin, size);
            Array data2D = data3D.reduce();
            char[] object = (char[]) data2D.copyTo1DJavaArray();
            StringBuffer buff = new StringBuffer();
            fillLabelMap(labelMap, i, object, buff);
        }
        return labelMap;
    }

    private void fillLabelMap(Map<String, Integer> labelMap, int i, char[] object, StringBuffer buff) {
        for (int j = 0; j < object.length; j++) {
            if (Character.UNASSIGNED == object[j]) {
                break;
            }
            buff.append(object[j]);
        }
        labelMap.put(buff.toString(), i);
    }

    private Long[] createLongArray(double[] array) {
        Long[] newArray = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = Math.round(array[i]);
        }
        return newArray;
    }

    /**
     * @return the reporters
     */
    public Map<String, Integer> getReporters() {
        return reporters;
    }

    /**
     * @return the arrays
     */
    public Map<String, Integer> getArrays() {
        return arrays;
    }

}
