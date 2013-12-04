/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.heatmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

@SuppressWarnings("rawtypes")
public class CBS2HeatMap implements CBSToHeatmap {

    private HashMap<String, Object> bins = new HashMap<String, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put("big", new HashMap<String, Object>());
            put("small", new HashMap<String, Object>());
        }
    };

    private TreeMap<String, Integer> live_barcodes = new TreeMap<String, Integer>();

    // scales are used for converting gender-corrected copy numbers (real numbers) to a zero-normal integer scale
    private String[] chr_order_arr = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
            "14", "15", "16", "17", "18", "19", "20", "21", "22", "X", "Y" };

    private HashMap<String, Integer> chr_order = new HashMap<String, Integer>();

    private String scale = null;

    private String default_scale = "0.5,1.5,2.5,3.5,4.5"; // "0.3,0.8,1.3,1.8,2.2,2.7,3.2,3.7,4.2,6.0,8.0,10.0,12.0,14.0,16.0,18.0,20.0";

    // default cn levels: 0, 1, 2, 3, 4, >4
    // default maps to : -2, -1, 0, +1, +2, +3

    // another example: "0.3,0.8,1.3,1.8,2.21,2.71,3.21,3.71,4.21"

    private Double[] scale_domain;

    private HashMap<Double, Integer> scale_range = new HashMap<Double, Integer>();

    private Integer scale_cap;

    private StringArrayAndCounter scale_lines = new StringArrayAndCounter(5);

    // MJF: note switching bin2amp & bin2del to hashes from arrays, don't know array extents
    private HashMap<Integer, Integer> bin2amp = new HashMap<Integer, Integer>();

    private HashMap<Integer, Integer> bin2del = new HashMap<Integer, Integer>();

    private HashMap<Integer, IntegerArrayAndCounter> big2small;

    private HashMap<Integer, Integer> picked = new HashMap<Integer, Integer>();

    private HashMap<String, Integer[]> cn = new HashMap<String, Integer[]>();

    private StringArrayAndCounter gene_order = new StringArrayAndCounter(30);

    private TreeMap<String, HashMap<String, Integer>> gene_value = new TreeMap<String, HashMap<String, Integer>>();

    private HashMap<String, Integer> keep = new HashMap<String, Integer>();

    private HashMap<String, Double> xsum = new HashMap<String, Double>();

    private HashMap<String, Integer> xn = new HashMap<String, Integer>();

    private HashMap<String, Double> xmean = new HashMap<String, Double>();

    private HashMap<String, Double> ysum = new HashMap<String, Double>();

    private HashMap<String, Integer> yn = new HashMap<String, Integer>();

    private HashMap<String, Double> ymean = new HashMap<String, Double>();

    private HashMap<String, String> gender_annotation = new HashMap<String, String>();

    private HashMap<String, String> gender = new HashMap<String, String>();

    private HashMap<String, Integer> x_treatment = new HashMap<String, Integer>();

    private HashMap<String, Integer> y_treatment = new HashMap<String, Integer>();

    private final Integer NO_VALUE = -99;

    private final Double NO_VALUE_D = -99.0;

    private Double AMPLIFICATION_CEILING = 20.0;

    private Integer MIN_GENE_OVERLAP = 10000;

    private Double MIN_GENE_OVERLAP_PCT = 0.30;

    // exclude very small segments: they are far more likely to be germline copy number variation loci

    @SuppressWarnings("unchecked")
    public void runCBSToHeatmap(HeatMapArgs hma) throws IOException {
        for (int i = 0; i <= 23; i++) {
            chr_order.put(chr_order_arr[i], new Integer(i));
        }

        if (hma.getScale() == null) {
            scale = default_scale;
        } else {
            scale = hma.getScale();
        }
        makeScale(scale, 2.0);

        readBinFile(hma.getBigBinFile(), bins, "big");
        readBinFile(hma.getSmallBinFile(), bins, "small");
        big2small = crossMapBins((HashMap<String, Object>) bins.get("big"), (HashMap<String, Object>) bins.get("small"));

        if (hma.getGenderFile() != null) {
            readGenderFile(hma.getGenderFile());
        }

        List<GeneLocationWrapper> geneLocations = hma.getRefGenesFile() == null ? hma.getGeneLocations()
                : retrieveGeneLocationDataFromFile(hma.getRefGenesFile());
        loadGeneLocations(geneLocations, (HashMap) bins.get("big"));
        if (hma.getSampleFile() != null) {
            readSampleFile(hma.getSampleFile());
        }
        List<SegmentDataWrapper> segmentDatas = hma.getSegmentFile() == null ? hma.getSegmentDatas() 
                : retrieveSegmentDataFromFile(hma.getSegmentFile()); 
        loadSegmentationData(segmentDatas, 1, hma);
        loadSegmentationData(segmentDatas, 2, hma);

        if (hma.getGeneOutFile() != null) {
            geneOutput(hma);
        }
        if (hma.getGenomeOutFile() != null) {
            genomeOutput(hma);
        }
    }
    
    private List<GeneLocationWrapper> retrieveGeneLocationDataFromFile(String filename) throws IOException {
        List<GeneLocationWrapper> geneLocations = new ArrayList<GeneLocationWrapper>();
        String line = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                GeneLocationWrapper geneLocation = new GeneLocationWrapper();
                String[] line_pieces = line.split("\t");
                String gene = line_pieces[0];
                String chr = line_pieces[1];
                Integer start = Integer.parseInt(line_pieces[2]);
                Integer stop = Integer.parseInt(line_pieces[3]);
                geneLocation.setChromosome(chr);
                geneLocation.setGeneSymbol(gene);
                geneLocation.setStartPosition(start);
                geneLocation.setEndPosition(stop);
                geneLocations.add(geneLocation);
            }
        } finally {
            // Close the BufferedReader
            if (br != null) {
                br.close();
            }
        }
        return geneLocations;
    }

    // ######################################################################
    @SuppressWarnings("unchecked")
    private void loadGeneLocations(List<GeneLocationWrapper> geneLocations, HashMap<String, Object> bins) {
        // ## assume we have already chosen one location for each gene
        HashMap<String, Integer> chr_bins = (HashMap) bins.get("chr_bins");
        HashMap<Integer, HashMap> bin2gene = (HashMap) bins.get("bin2gene");
        HashMap<String, Segment> gene2seg = (HashMap) bins.get("gene2seg");
        Integer BIN_SIZE = (Integer) bins.get("BIN_SIZE");

        HashMap<String, Object> chr2gene_start = new HashMap<String, Object>();

        
        
        for(GeneLocationWrapper geneLocation : geneLocations) {
            String gene = geneLocation.getGeneSymbol();
            String chr = geneLocation.getChromosome();
            Integer start = geneLocation.getStartPosition();
            Integer stop = geneLocation.getEndPosition();

            if (chr_bins.get("start" + chr) == null) {
//                System.out.println("##bad chromosome: " + chr);
                continue;
            }

            Integer start_bin = (Integer) (chr_bins.get("start" + chr) + start / BIN_SIZE);
            if (start_bin > chr_bins.get("stop" + chr)) {
//                System.out.println("##bad bin	" + chr + "	" + start);
                continue;
            }
            Integer stop_bin = (Integer) (chr_bins.get("start" + chr) + stop / BIN_SIZE);
            if (stop_bin > chr_bins.get("stop" + chr)) {
//                System.out.println("##bad bin	" + chr + "	" + stop);
                continue;
            }
            Segment seg = new Segment(chr, start, stop, gene);
            gene2seg.put(gene + "," + chr + "," + start, seg);
            start_bin = (Integer) (chr_bins.get("start" + chr) + start / BIN_SIZE);
            stop_bin = (Integer) (chr_bins.get("start" + chr) + stop / BIN_SIZE);
            for (int b = start_bin; b <= stop_bin; b++) {
                if (bin2gene.get(b) == null) {
                    bin2gene.put(b, new HashMap<String, Integer>());
                }
                (bin2gene.get(b)).put(gene + "," + chr + "," + start, 1);
            }

            // push @{ $chr2gene_start{$chr}{$start} }, $gene;
            if (!chr2gene_start.containsKey(chr)) {
                chr2gene_start.put(chr, new TreeMap<String, StringArrayAndCounter>());
            }
            if (!((TreeMap) chr2gene_start.get(chr)).containsKey(start)) {
                ((TreeMap) chr2gene_start.get(chr)).put(start, new StringArrayAndCounter(5));
            }
            StringArrayAndCounter chr2gene_start_array = (StringArrayAndCounter) ((TreeMap) chr2gene_start.get(chr))
                    .get(start);
            chr2gene_start_array.push(gene);
        }
        for (int i = 0; i < chr_order_arr.length; i++) {
            String chr = chr_order_arr[i];
            Set set = ((TreeMap) chr2gene_start.get(chr)).entrySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry me = (Map.Entry) iter.next();
                Integer start = (Integer) me.getKey();
                String[] names = { "GFAM138F", "GFAM138E", "GFAM138D", "GFAM138C", "GFAM138B", "GFAM138A" };
                Arrays.sort(names, 0, 5);

                StringArrayAndCounter chr2gene_start_array = (StringArrayAndCounter) me.getValue();
                String[] copee = new String[chr2gene_start_array.counter];
                StringArrayAndCounter dopee = new StringArrayAndCounter(chr2gene_start_array.counter);
                for (int j = 0; j < chr2gene_start_array.counter; j++) {
                    copee[j] = chr2gene_start_array.array[j];
                    dopee.array[j] = chr2gene_start_array.array[j];
                }
                Arrays.sort((String[]) chr2gene_start_array.array, 0, chr2gene_start_array.counter);
                Arrays.sort(copee);
                Arrays.sort(dopee.array, (int) 0, (int) chr2gene_start_array.counter - 1);
                for (int j = 0; j < chr2gene_start_array.counter; j++) {
                    gene_order.push(chr2gene_start_array.array[j] + "," + chr + "," + start);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void assignValueToGene(String barcode, String chr, Integer start, Integer stop, Integer sv) {
        HashMap<String, Object> bindefs = (HashMap) bins.get("big");
        Integer BIN_SIZE = (Integer) bindefs.get("BIN_SIZE");
        HashMap<String, Integer> chr_bins = (HashMap) bindefs.get("chr_bins");
        HashMap<Integer, HashMap> bin2gene = (HashMap) bindefs.get("bin2gene");
        HashMap<String, Segment> gene2seg = (HashMap) bindefs.get("gene2seg");

        if (sv == NO_VALUE || sv == 0) {
            return;
        }

        // ## use big bins

        Integer start_bin = chr_bins.get("start" + chr) + (Integer) (start / BIN_SIZE);
        Integer stop_bin = chr_bins.get("start" + chr) + (Integer) (stop / BIN_SIZE);

        Segment s = new Segment(chr, start, stop);
        // ## If the overlap between a seg and a gene is at least one-third
        // ## the length of the gene, then assign the value of the seg
        // ## to the gene. Prefer a deletion over an amplification.

        for (int bin = start_bin; bin <= stop_bin; bin++) {
            if (!bin2gene.containsKey(bin)) {
                continue;
            }
            Set set = bin2gene.get(bin).entrySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry me = (Map.Entry) iter.next();
                // #1 $g now means gene_copy
                Segment gene_seg = gene2seg.get(me.getKey());
                // ## oops: same gene may appear on X and Y
                if (!(s.chr).equals(gene_seg.chr)) {
                    continue;
                }
                Integer overlap_len = (s.Overlap(gene_seg)).Length();
                if (overlap_len >= MIN_GENE_OVERLAP
                        || overlap_len.doubleValue() / gene_seg.Length().doubleValue() >= MIN_GENE_OVERLAP_PCT) {
                    if (gene_value.get(barcode) == null) {
                        gene_value.put(barcode, new HashMap<String, Integer>());
                    }
                    HashMap<String, Integer> inner_hash = gene_value.get(barcode);
                    if (inner_hash.get(me.getKey()) == null || sv < 0) {
                        inner_hash.put((String) me.getKey(), sv);
                    }
                }
            }
        }
    }

    private List<SegmentDataWrapper> retrieveSegmentDataFromFile(String fileName) throws NumberFormatException, IOException {
        BufferedReader br = null;
        List<SegmentDataWrapper> segmentDatas = new ArrayList<SegmentDataWrapper>();
        try {
            String line;
            br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                // s/[\r\n]+//;
                // # MJF: some files have a barcode as the first column, the files in RefNorm do not
                String barcode = "", chr;
                Integer start, stop, markers;
                Float mean;
                StringTokenizer st = new StringTokenizer(line);
                SegmentDataWrapper segmentData = new SegmentDataWrapper();
                if (st.countTokens() == 5) {
                    barcode = "";
                    chr = (String) st.nextElement();
                    if (chr.equals("chrom")) {
                        continue;
                    } // the files in RefNorm have a header line
                    start = Integer.parseInt((String) st.nextElement());
                    stop = Integer.parseInt((String) st.nextElement());
                    markers = Integer.parseInt((String) st.nextElement());
                    String meanString = (String) st.nextElement();
                    try {
                        mean = Float.parseFloat(meanString);
                    } catch ( NumberFormatException ex ) {
                        System.out.println( "Bad Mean " + meanString );
                        continue;
                    }
                } else {
                    barcode = (String) st.nextElement();
                    if (barcode.equals("barcode")) {
                        continue;
                    } // the files in RefNorm have a header line
                    chr = (String) st.nextElement();
                    start = Integer.parseInt((String) st.nextElement());
                    stop = Integer.parseInt((String) st.nextElement());
                    markers = Integer.parseInt((String) st.nextElement());
                    String meanString = (String) st.nextElement();
                    try {
                        mean = Float.parseFloat(meanString);
                    } catch ( NumberFormatException ex ) {
                        System.out.println( "Bad Mean " + meanString );
                        continue;
                    }
                }
                segmentData.setChromosome(chr);
                segmentData.setSampleIdentifier(barcode);
                segmentData.setNumberOfMarkers(markers);
                segmentData.setSegmentValue(mean);
                segmentData.setStartPosition(start);
                segmentData.setStopPosition(stop);
                segmentDatas.add(segmentData);
            }
        } finally {
            // Close the BufferedReader
            if (br != null) {
                br.close();
            }
        }
        return segmentDatas;
    }
    
    // ######################################################################
    @SuppressWarnings("unchecked")
    private void loadSegmentationData(List<SegmentDataWrapper> segmentDatas, Integer round, HeatMapArgs hma) {
        HashMap<String, Integer> chr_bins;
        Integer BIN_SIZE;
        
        HashMap<String, Object> bins_small = (HashMap<String, Object>) bins.get("small");
        chr_bins = (HashMap<String, Integer>) bins_small.get("chr_bins");
        BIN_SIZE = (Integer) bins_small.get("BIN_SIZE");

        HashMap<String, Byte> amp_cache = new HashMap<String, Byte>();
        HashMap<String, Byte> del_cache = new HashMap<String, Byte>();

        for (SegmentDataWrapper segmentData : segmentDatas) {
            // s/[\r\n]+//;
            // # MJF: some files have a barcode as the first column, the files in RefNorm do not

            String barcode = segmentData.getSampleIdentifier();
            String chr = segmentData.getChromosome();
            Integer start = segmentData.getStartPosition();
            Integer stop = segmentData.getStopPosition();
            Float mean = segmentData.getSegmentValue();

            // if ( barcode.length() > 20) { barcode = barcode.substring(0, 20); }
            if (hma.getSampleFile() != null && keep.get(barcode) == null) {
//                System.out.println("discarding barcode" + barcode);
                continue;
            }
            live_barcodes.put(barcode, 1);

            if (chr.equals("23")) {
                chr = "X";
            } else if (chr.equals("24")) {
                chr = "Y";
            }

            if (chr_order.get(chr) == null) {
//                System.out.println("bad chromosome " + chr);
                continue;
            }

            Integer start_bin = chr_bins.get("start" + chr) + (Integer) (start / BIN_SIZE);
            Integer stop_bin = chr_bins.get("start" + chr) + (Integer) (stop / BIN_SIZE);

            Integer seg_length = stop - start + 1;

            if (seg_length < hma.getMinSegLength()) {
//                System.out.println("short segment " + chr + ":" + (start - stop) + " for " + barcode);
                continue;
            }

            Double v0 = Math.pow(hma.getBase(), mean);
            Double v = v0 * 2;

            if (round == 1) {
                if (chr.equals("X")) {
                    if (xsum.get(barcode) == null) {
                        xsum.put(barcode, 0.0);
                        xn.put(barcode, 0);
                    }
                    xsum.put(barcode, xsum.get(barcode) + seg_length * v);
                    xn.put(barcode, xn.get(barcode) + seg_length);
                    continue; // don't pick based on amps/dels in X, Y
                } else if (chr.equals("Y")) {
                    if (ysum.get(barcode) == null) {
                        ysum.put(barcode, 0.0);
                        yn.put(barcode, 0);
                    }
                    ysum.put(barcode, ysum.get(barcode) + seg_length * v);
                    yn.put(barcode, yn.get(barcode) + seg_length);
                    continue; // don't pick based on amps/dels in X, Y
                }
            }

            Integer sv = 0;
            if (round == 1) {
                sv = scaleValue(v);
            } else if (round == 2) {
                sv = scaleValue(correctForGender(barcode, chr, v));
            }

            // do the genes
            if (round == 2) {
                assignValueToGene(barcode, chr, start, stop, sv);
            }

            HashMap<String, Integer> barcode_to_index = new HashMap<String, Integer>();
            Integer barcode_index = 0;
            for (int bin = start_bin; bin <= stop_bin; bin++) {
                // next line commented out in Perl version
                // # if ( overlap < BIN_SIZE/2) { next; }

                if (round == 2) {
                    if (picked.get(bin) != null) {
                        if (cn.get(barcode) == null) {
                            HashMap<String, Object> bins_big = (HashMap<String, Object>) bins.get("big");
                            HashMap<String, Integer> chr_bins_big = (HashMap<String, Integer>) bins_big
                                    .get("chr_bins");

                            Integer last_bin = chr_bins_big.get("stop" + "Y");
                            Integer[] cn_bins = new Integer[last_bin + 1];
                            for (int b = 1; b <= last_bin; b++) {
                                cn_bins[b] = NO_VALUE;
                            }
                            cn.put(barcode, cn_bins);
                        }
                        Integer big_bin = picked.get(bin);
                        Integer old_value = cn.get(barcode)[big_bin];
                        // ## let the last non-normal value win
                        if (old_value == NO_VALUE || old_value == 0) {
                            cn.get(barcode)[big_bin] = sv;
                        }
                    }
                } else if (round == 1) {
                    // ## this is just for selecting interesting bins
                    // ## tally max of one amp and one del per barcode per bin
                    if (!barcode_to_index.containsKey(barcode)) {
                        barcode_to_index.put(barcode, barcode_index++);
                    }
                    if (sv > 0) {
                        if (!amp_cache.containsKey(barcode_to_index.get(barcode) + "SEP" + bin)) {
                            amp_cache.put(barcode_to_index.get(barcode) + "SEP" + bin, (byte) 1);
                            if (bin2amp.containsKey(bin)) {
                                bin2amp.put(bin, bin2amp.get(bin) + 1);
                            } else {
                                bin2amp.put(bin, 1);
                            }
                        }
                    } else if (sv < 0) {
                        if (!del_cache.containsKey(barcode_to_index.get(barcode) + "SEP" + bin)) {
                            del_cache.put(barcode_to_index.get(barcode) + "SEP" + bin, (byte) 1);
                            if (bin2del.containsKey(bin)) {
                                bin2del.put(bin, bin2del.get(bin) + 1);
                            } else {
                                bin2del.put(bin, 1);
                            }
                        }
                    }
                }
            }
        }

        if (round == 1) {
            computeXYMeans(hma);
            // Integer last_bin = ( ( bins.get( "big" ) ).get( "chr_bins" ) ).get( "stop" + "Y" );
            HashMap<String, Object> big_local = (HashMap) bins.get("big");
            HashMap<String, Integer> chr_bins_local = (HashMap) big_local.get("chr_bins");
            Integer last_bin = chr_bins_local.get("stop" + "Y");
            for (int to_bin = 1; to_bin <= last_bin; to_bin++) {
                Integer score = null;
                Integer pick = -99;
                IntegerArrayAndCounter b2s = big2small.get(to_bin);
                for (int b2s_index = 0; b2s_index < b2s.counter; b2s_index++) {
                    Integer from_bin = b2s.array[b2s_index];
                    Integer bin2ampScore = -99;
                    Integer bin2delScore = -99;
                    if (bin2amp.containsKey(from_bin)) {
                        bin2ampScore = bin2amp.get(from_bin);
                    }
                    if (bin2del.containsKey(from_bin)) {
                        bin2delScore = bin2del.get(from_bin);
                    }
                    if (pick == -99) {
                        score = Math.max(bin2ampScore, bin2delScore);
                        pick = from_bin;
                    } else if (Math.max(bin2ampScore, bin2delScore) > score) {
                        score = Math.max(bin2ampScore, bin2delScore);
                        pick = from_bin;
                    }
                }
                if (score == -99) {
                    score = null;
                }
                picked.put(pick, to_bin);
            }
        }
    }

    private ComputeGenderReturn computeGender(String barcode, Double xmean, Double ymean, HeatMapArgs hma) {
        String gender_annotation_local = "";
        if (barcode.length() >= 12) {
            String patient = barcode.substring(0, 12);
            if (gender_annotation.get(patient) != null) {
                if ("MALE".equals(gender_annotation.get(patient))) {
                    gender_annotation_local = "M";
                } else if ("FEMALE".equals(gender_annotation.get(patient))) {
                    gender_annotation_local = "F";
                }
            }
        }
        String gender;
        Integer x_treatment, y_treatment;
        // ## means after subtracting 2

        if (xmean == NO_VALUE_D) {
            gender = "?";
            x_treatment = NO_VALUE;
            if (ymean == NO_VALUE_D) {
                y_treatment = NO_VALUE;
            } else {
                y_treatment = 0;
            }
        } else if (ymean == NO_VALUE_D) {
            gender = "F";
            x_treatment = 0;
            y_treatment = NO_VALUE;
        } else if (Math.round(xmean) > 1) {
            // probably need to adjust Fx -= 2
            gender = "F";
            x_treatment = -2;
            y_treatment = NO_VALUE;
        } else if (Math.round(xmean) > 0) {
            // probably need to adjust Fx -= 1
            gender = "F";
            x_treatment = -1;
            y_treatment = NO_VALUE;
        } else if (Math.round(xmean) == 0 && Math.round(ymean) < 0) {
            // probably a female but don't need to adjust X
            gender = "F";
            x_treatment = 0;
            y_treatment = NO_VALUE;
        } else if (Math.round(xmean) < 0) {
            // probably need to adjust Mx += 1, My += 1
            gender = "M";
            x_treatment = 1;
            y_treatment = 1;
        } else if (Math.round(xmean) == Math.round(ymean)) {
            // don't adjust
            gender = "MATCHED";
            x_treatment = 0;
            y_treatment = 0;
        } else {
            gender = "?";
            x_treatment = 0;
            y_treatment = 0;
        }

        // apply the gender-by-annotation after the computations

        if (hma.getProtocol() != null && hma.getProtocol().equals("matched")) {
            x_treatment = 0;
            y_treatment = 0;
        }
        if (!gender_annotation_local.equals("")) {
            gender = gender_annotation_local;
            if (gender.equals("F")) {
                y_treatment = NO_VALUE;
            }
        }

        ComputeGenderReturn cgr = new ComputeGenderReturn(gender, x_treatment, y_treatment);
        return cgr;
    }

    // ######################################################################
    private void geneHeader(Writer output, HashMap<String, Object> bindefs, HashMap<String, Integer> picked,
            HeatMapArgs hma) throws IOException {
        if (hma.getProject() != null) {
            output.write("#project=" + hma.getProject() + "\n");
        }
        output.write("#title=Gene " + hma.getTitle() + "\n");
        output.write("#genome=off\n");
        output.write("#total_histogram=off\n");
        output.write("#up_down_histogram=on\n");
        output.write("#high_label=Amplification\n");
        output.write("#low_label=Deletion\n");
        output.write("#platform=" + hma.getPlatform() + "\n");
        output.write("#submitter=" + hma.getSubmitter() + "\n");
        output.write("#local_contact=" + hma.getContact() + "\n");
        output.write("#neutral=0,0\n");
        output.write("#data_type=copy number\n");
        if (scale_lines != null) {
            output.write(scale_lines.array[0] + "\n");
            for (int i = 1; i < scale_lines.counter; i++) {
                output.write("" + scale_lines.array[i] + "\n");
            }
        }
        // following lines are commented out in the perl code as well
        // # output.write( "#scale=1,2,3,4\n" );
        // # output.write( "#scale=1,2\n" );
        // # output.write( "#brightness=30\n" );
        // # output.write( "#tooltip_bin_label=sample:sample_id region:bin_value\n"

        StringArrayAndCounter temp = new StringArrayAndCounter(5);
        temp.push("");
        HashMap<String, Integer> locations = new HashMap<String, Integer>();
        for (int i = 0; i < gene_order.counter; i++) {
            String gene_copy = gene_order.array[i];
            String[] line_pieces = gene_copy.split(",");
            String gene = line_pieces[0];
            if (!locations.containsKey(gene)) {
                locations.put(gene, 0);
            }
            locations.put(gene, locations.get(gene) + 1);
        }
        for (int i = 0; i < gene_order.counter; i++) {
            String gene_copy = gene_order.array[i];
            String[] line_pieces = gene_copy.split(",");
            String gene = line_pieces[0];
            String chr = line_pieces[1];
            Integer start = Integer.parseInt(line_pieces[2]);
            if (locations.get(gene) > 1) {
                temp.push(gene + " " + chr + ":" + start);
            } else {
                temp.push(gene);
            }
        }
        for (int i = 0; i < temp.counter; i++) {
            output.write(temp.array[i]);
            if (i < temp.counter - 1) {
                output.write("\t");
            } else {
                output.write("\n");
            }
        }
    }

    // ######################################################################
    @SuppressWarnings("unchecked")
    private void genomeHeader(Writer output, HashMap<String, Object> bindefs, HeatMapArgs hma) throws IOException {
        if (hma.getProject() != null) {
            output.write("#project=" + hma.getProject() + "\n");
        }
        output.write("#title=Genome " + hma.getTitle() + "\n");
        output.write("#genome=on\n");
        output.write("#total_histogram=off\n");
        output.write("#up_down_histogram=on\n");
        output.write("#high_label=Amplification\n");
        output.write("#low_label=Deletion\n");
        output.write("#neutral=0,0\n");
        output.write("#platform=" + hma.getPlatform() + "\n");
        output.write("#submitter=" + hma.getSubmitter() + "\n");
        output.write("#local_contact=" + hma.getContact() + "\n");
        output.write("#data_type=copy number\n");
        if (scale_lines != null) {
            output.write(scale_lines.array[0] + "\n");
            for (int i = 1; i < scale_lines.counter; i++) {
                output.write("" + scale_lines.array[i] + "\n");
            }
        }
        // following lines are commented out in the perl code as well
        // # output.write( "#scale=1,2,3,4\n" );
        // # output.write( "#scale=1,2\n" );
        // # output.write( "#brightness=30\n" );
        // # output.write( "#tooltip_bin_label=sample:sample_id region:bin_value\n"

        if (hma.getCnvTrack() != null) {
            BufferedReader cnv = null;
            String line = null;
            try {
                cnv = new BufferedReader(new FileReader(hma.getCnvTrack()));
                while ((line = cnv.readLine()) != null) {
                    output.write(line + "\n");
                }
            } finally {
                // Close the BufferedReader
                if (cnv != null) {
                    cnv.close();
                }
            }
        }

        HashMap<String, Integer> chr_bins = (HashMap<String, Integer>) bindefs.get("chr_bins");
        Integer last_bin = chr_bins.get("stop" + "Y");
        Integer[] bin2start = (Integer[]) bindefs.get("bin2start");
        Integer[] bin2stop = (Integer[]) bindefs.get("bin2stop");
        String[] bin2chr = (String[]) bindefs.get("bin2chr");
        HashMap<Integer, HashMap> bin2gene = (HashMap) bindefs.get("bin2gene");
        // Integer BIN_SIZE = (Integer) bindefs.get( "BIN_SIZE" );

        StringArrayAndCounter temp = new StringArrayAndCounter(5);
        temp.push("");
        for (int bin = 1; bin <= last_bin; bin++) {
            String label = "";
            StringArrayAndCounter gene_temp = new StringArrayAndCounter(5);
            if (bin2gene.get(bin) != null) {
                Set set = bin2gene.get(bin).entrySet();
                Iterator iter = set.iterator();
                while (iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    String g = (String) me.getKey();
                    String[] line_pieces = g.split(",");
                    String gene = line_pieces[0];
                    gene_temp.push(gene);
                }
            }
            if (gene_temp.counter != 0) {
                Arrays.sort(gene_temp.array, 0, gene_temp.counter);
                for (int i = 0; i < gene_temp.counter; i++) {
                    label = label + gene_temp.array[i];
                    if (i < gene_temp.counter - 1) {
                        label = label + ",";
                    }
                }
            } else {
                DecimalFormat myFormatter = new DecimalFormat("0.0");
                label = "chr" + bin2chr[bin] + ":";
                label = label + myFormatter.format(bin2start[bin] / 1000000.0) + "-";
                label = label + myFormatter.format(bin2stop[bin] / 1000000.0) + "Mb";
            }
            temp.push(label);
        }
        for (int i = 0; i < temp.counter; i++) {
            output.write(temp.array[i]);
            if (i < temp.counter - 1) {
                output.write("\t");
            } else {
                output.write("\n");
            }
        }
    }

    // ######################################################################
    @SuppressWarnings("unchecked")
    private void readBinFile(String FileName, HashMap bins, String id) throws IOException {
        HashMap<String, Integer> chr_bins = new HashMap<String, Integer>();
        Integer numBins = 1000000;
        String[] bin2chr = new String[numBins];
        Integer[] bin2start = new Integer[numBins];
        Integer[] bin2stop = new Integer[numBins];
        Integer[] bin2mid = new Integer[numBins];
        HashMap<Integer, HashMap> bin2gene = new HashMap<Integer, HashMap>();
        HashMap<String, Segment> gene2seg = new HashMap<String, Segment>();

        StringTokenizer st;
        String line = null;
        BufferedReader br = null;
        String[] tokens = new String[10];
        int tokenCnt = 0;

        try {
            br = new BufferedReader(new FileReader(FileName));
            while ((line = br.readLine()) != null) {

                st = new StringTokenizer(line);
                tokenCnt = 0;
                while (st.hasMoreTokens()) {
                    tokens[tokenCnt++] = st.nextToken();
                }
                if (tokens[0].equals("##")) {
                    int firstBin = Integer.parseInt(tokens[2].trim());
                    int lastBin = Integer.parseInt(tokens[3].trim());
                    chr_bins.put("start" + tokens[1], firstBin);
                    chr_bins.put("stop" + tokens[1], lastBin);
                } else {
                    int bin = Integer.parseInt(tokens[0].trim());
                    int start = Integer.parseInt(tokens[2].trim());
                    int stop = Integer.parseInt(tokens[3].trim());
                    bin2chr[bin] = tokens[1];
                    bin2start[bin] = start;
                    bin2stop[bin] = stop;
                    bin2mid[bin] = (Integer) ((start + stop) / 2);
                }
            }
        } finally {
            // Close the BufferedReader
            if (br != null) {
                br.close();
            }
        }

        HashMap<String, Object> bins_bors = (HashMap<String, Object>) bins.get(id);
        // (bins.get( id )).put( "chr_bins", chr_bins );
        bins_bors.put("chr_bins", chr_bins);
        bins_bors.put("bin2chr", bin2chr);
        bins_bors.put("bin2start", bin2start);
        bins_bors.put("bin2stop", bin2stop);
        bins_bors.put("bin2mid", bin2mid);
        bins_bors.put("BIN_SIZE", bin2stop[chr_bins.get("start1")] - bin2start[chr_bins.get("start1")] + 1);
        bins_bors.put("bin2gene", bin2gene);
        bins_bors.put("gene2seg", gene2seg);
    }

    // ######################################################################
    private void readGenderFile(String FileName) throws IOException {
        StringTokenizer st;
        String line = null;
        BufferedReader br = null;
        String[] tokens = new String[2];
        int tokenCnt = 0;
        try {
            br = new BufferedReader(new FileReader(FileName));
            while ((line = br.readLine()) != null) {
                st = new StringTokenizer(line);
                tokenCnt = 0;
                while (st.hasMoreTokens()) {
                    tokens[tokenCnt++] = st.nextToken();
                }
                // tokens[ 0 ] is patient, tokens[ 1 ] is gender
                gender_annotation.put(tokens[0], tokens[1]);
            }
        } finally {
            // Close the BufferedReader
            if (br != null) {
                br.close();
            }
        }
    }

    // ######################################################################
    @SuppressWarnings("unchecked")
    private void geneOutput(HeatMapArgs hma) throws IOException {
        HashMap<String, Integer> picked = new HashMap<String, Integer>();

        // Set set = gene_value.entrySet();
        // Iterator iter = set.iterator();
        // MJF: picked not used in GeneHeader
        // while( iter.hasNext() ){
        // Map.Entry me = (Map.Entry) iter.next(); // barcode is key, HashMap<String, Integer> is value
        // Set set2 = ( (HashMap) me.getValue() ).entrySet();
        // Iterator iter2 = set2.iterator();
        // while( iter2.hasNext() ){
        // Map.Entry me2 = (Map.Entry) iter2.next(); // gene_copy is key, $gene_value{$barcode}{$gene_copy} is value
        // if ( (Integer) me2.getValue() != 0 ) {
        // picked.put( (String) me2.getKey(), 1 );
        // }
        // }
        // }

        Writer output = new BufferedWriter(new FileWriter(hma.getGeneOutFile()));

        geneHeader(output, (HashMap) bins.get("big"), picked, hma);

        Set set = live_barcodes.entrySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next(); // barcode is key
            String barcode = (String) me.getKey();
            String label = barcode.substring(0, Math.min(barcode.length(), 30));
            IntegerArrayAndCounter line = new IntegerArrayAndCounter(5);
            HashMap<String, Integer> gene_value_barcode = (HashMap) gene_value.get(barcode);

            for (int i = 0; i < gene_order.counter; i++) {
                String gene_copy = gene_order.array[i];
                if (gene_value_barcode.get(gene_copy) != null) {
                    Integer v = gene_value_barcode.get(gene_copy);
                    line.push(v);
                } else {
                    line.push(0);
                }
            }
            output.write(label);
            for (int i = 0; i < line.counter; i++) {
                output.write("\t" + line.array[i]);
            }
            output.write("\n");
        }
        output.close();
    }

    // ######################################################################
    @SuppressWarnings("unchecked")
    private void genomeOutput(HeatMapArgs hma) throws IOException {
        HashMap<String, Object> bins_big = (HashMap<String, Object>) bins.get("big");
        HashMap<String, Integer> chr_bins_big = (HashMap<String, Integer>) bins_big.get("chr_bins");
        Integer last_bin = chr_bins_big.get("stop" + "Y");

        String[] bin2chr = (String[]) (bins_big.get("bin2chr"));

        Writer output = new BufferedWriter(new FileWriter(hma.getGenomeOutFile()));

        genomeHeader(output, (HashMap) bins.get("big"), hma);

        Set set = live_barcodes.entrySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next(); // barcode is key
            String barcode = (String) me.getKey();
            // for now, print the complete portion...
            // my $label = substr($barcode, 0, 12);
            String label = barcode.substring(0, Math.min(barcode.length(), 30));
            Integer[] temp;
            if (!cn.containsKey(barcode)) {
                temp = new Integer[last_bin + 1];
                for (int bin = 1; bin <= last_bin; bin++) {
                    temp[bin] = NO_VALUE;
                }
            } else {
                temp = cn.get(barcode);
            }

            IntegerArrayAndCounter line = new IntegerArrayAndCounter(5);
            for (int bin = 1; bin <= last_bin; bin++) {
                String chr = bin2chr[bin];
                if (chr.equals("Y") && (y_treatment.get(barcode) == NO_VALUE)) {
                    line.push(NO_VALUE);
                } else if (temp[bin] == NO_VALUE) {
                    line.push(0); // i.e., normal, after subtracting 2
                } else {
                    line.push(temp[bin]);
                }
            }
            output.write(label);
            for (int i = 0; i < line.counter; i++) {
                output.write("\t" + line.array[i]);
            }
            output.write("\n");
        }
        output.close();
    }

    // ######################################################################
    private Double correctForGender(String barcode, String chr, Double v) {
        if (v == NO_VALUE_D) {
            return NO_VALUE_D;
        }

        if (chr.equals("Y")) {
            if (y_treatment.get(barcode) == NO_VALUE) {
                return NO_VALUE_D;
            } else {
                return (v + y_treatment.get(barcode));
            }
        } else if (chr.equals("X")) {
            if (x_treatment.get(barcode) == NO_VALUE) {
                return NO_VALUE_D;
            } else {
                return (v + x_treatment.get(barcode));
            }
        } else {
            return v;
        }
    }

    // ######################################################################
    private void readSampleFile(String FileName) throws IOException {
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new FileReader(FileName));
            while ((line = br.readLine()) != null) {
                keep.put(line, 1);
            }
        } finally {
            // Close the BufferedReader
            if (br != null) {
                br.close();
            }
        }
    }

    // ######################################################################
    @SuppressWarnings("unchecked")
    private HashMap<Integer, IntegerArrayAndCounter> crossMapBins(HashMap<String, Object> from,
            HashMap<String, Object> to) {
        HashMap<Integer, IntegerArrayAndCounter> from2to = new HashMap<Integer, IntegerArrayAndCounter>();
        // go about this backwards, assuming there is one from for many to's
        HashMap<String, Integer> chr_bins_to = (HashMap<String, Integer>) to.get("chr_bins");
        HashMap<String, Integer> chr_bins_from = (HashMap<String, Integer>) from.get("chr_bins");
        Integer BIN_SIZE_from = (Integer) from.get("BIN_SIZE");

        for (Integer to_bin = 1; to_bin <= chr_bins_to.get("stop" + "Y"); to_bin++) {
            Integer[] bin2mid = (Integer[]) to.get("bin2mid");
            String[] bin2chr = (String[]) to.get("bin2chr");
            Integer mid = bin2mid[to_bin];
            String chr = bin2chr[to_bin];
            Integer from_bin = (int) chr_bins_from.get("start" + chr) + (int) (mid / BIN_SIZE_from);

            IntegerArrayAndCounter from2to_array;
            if (!from2to.containsKey(from_bin)) {
                from2to_array = new IntegerArrayAndCounter(30);
                from2to.put(from_bin, from2to_array);
            } else {
                from2to_array = from2to.get(from_bin);
            }
            from2to_array.push(to_bin);
        }
        return from2to;
    }

    // ######################################################################
    private void computeXYMeans(HeatMapArgs hma) {

        Set set = xn.entrySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next();
            String barcode = (String) me.getKey();
            if (xn.get(barcode) > 0) {
                Double cn = xsum.get(barcode) / xn.get(barcode);
                cn = cn - 2; // ## normal is cn == 2
                if (cn < -2) {
                    cn = -2.0;
                }
                if (cn > AMPLIFICATION_CEILING) {
                    cn = AMPLIFICATION_CEILING;
                }
                cn = (double) Math.round(cn * 100.0); // in perl: $ymean{$barcode} = sprintf("%.2f", cn);
                cn /= 100.0;
                xmean.put(barcode, cn);
            } else {
                xmean.put(barcode, NO_VALUE_D);
            }
            if (yn.get(barcode) != null && yn.get(barcode) > 0) {
                Double cn = ysum.get(barcode) / yn.get(barcode);
                cn = cn - 2; // normal is cn == 2
                if (cn < -2)
                    cn = -2.0;
                if (cn > AMPLIFICATION_CEILING)
                    cn = AMPLIFICATION_CEILING;
                cn = (double) Math.round(cn * 100.0); // in perl: $ymean{$barcode} = sprintf("%.2f", cn);
                cn /= 100.0;
                ymean.put(barcode, cn);
            } else {
                ymean.put(barcode, NO_VALUE_D);
            }
        }

        set = xmean.entrySet();
        iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next();
            String barcode = (String) me.getKey();
            // appended "_local" to differentiate these from the hashes with the same name (something Perl allows)
            String gender_local;
            Integer x_treatment_local, y_treatment_local;
            @SuppressWarnings("unused")
            Double xmean_local, ymean_local;
            if (ymean.get(barcode) != null) {
                ymean_local = ymean.get(barcode);
            } else {
                ymean_local = NO_VALUE_D;
            }
            if (xmean.get(barcode) != null) {
                xmean_local = xmean.get(barcode);
            } else {
                xmean_local = NO_VALUE_D;
            }

            ComputeGenderReturn cgr = computeGender(barcode, xmean.get(barcode), ymean.get(barcode), hma);
            gender_local = cgr.getGender();
            x_treatment_local = cgr.getX_treatment();
            y_treatment_local = cgr.getY_treatment();

            gender.put(barcode, gender_local);
            x_treatment.put(barcode, x_treatment_local);
            y_treatment.put(barcode, y_treatment_local);
        }
    }

    // MJF: routine never used?
    // ######################################################################
    // public Integer DefaultScaleValue( Double v )
    // {
    // Double cn = Math.round( (float) v );
    // cn = cn - 2; // normal is cn == 2
    // if ( cn < -2 ) {
    // cn = -2;
    // }
    // if ( cn > AMPLIFICATION_CEILING) {
    // cn = AMPLIFICATION_CEILING;
    // }
    // return cn;
    // }

    // ######################################################################
    private Integer scaleValue(Double v) {
        if (v == NO_VALUE_D) {
            return NO_VALUE;
        }

        for (int x = 0; x < scale_domain.length; x++) {
            if (v < scale_domain[x]) {
                return scale_range.get(scale_domain[x]);
            }
        }
        return scale_cap;
    }

    // ######################################################################
    private void makeScale(String s, Double neutral) {
        Integer i = 0;

        String[] scale_domain_str = s.split(",");
        scale_domain = new Double[scale_domain_str.length];
        for (int x = 0; x < scale_domain_str.length; x++)
            scale_domain[x] = Double.parseDouble(scale_domain_str[x]);

        while (scale_domain[i] < neutral) {
            i++;
        }

        Integer neutral_ubi = i;
        Integer int_interval = 0;
        for (Integer j = neutral_ubi - 1; j >= 0; j--) {
            int_interval--;
            scale_range.put(scale_domain[j], int_interval);
        }

        scale_range.put(scale_domain[neutral_ubi], 0);
        int_interval = 0;
        for (Integer j = neutral_ubi + 1; j < scale_domain.length; j++) {
            int_interval++;
            scale_range.put(scale_domain[j], int_interval);
        }
        scale_cap = scale_range.get(scale_domain[scale_domain.length - 1]) + 1;

        Double last_domain = 0.0;
        for (i = 0; i < scale_domain.length; i++) {
            Double domain = scale_domain[i];
            Integer range = scale_range.get(domain);
            DecimalFormat myFormatter = new DecimalFormat("0.#");
            scale_lines.push("#data_scale=" + myFormatter.format(range) + ":" + myFormatter.format(last_domain)
                    + "<=v<" + myFormatter.format(domain));
            if (i == scale_domain.length - 1) {
                scale_lines.push("#data_scale=" + scale_cap + ":" + myFormatter.format(domain) + "<=v");
            }
            last_domain = domain;
        }
    }
    
    class IntegerArrayAndCounter {
        Integer[] array;

        Integer counter;

        Integer current_length;

        public IntegerArrayAndCounter(Integer initial_length) {
            this.array = new Integer[initial_length];
            this.counter = 0;
            this.current_length = initial_length;
        }

        public void push(Integer value) {
            if (counter + 2 > current_length) {
                current_length = current_length + 16;
                Integer[] new_array = new Integer[current_length];
                for (int i = 0; i < counter; i++) {
                    new_array[i] = array[i];
                }
                array = new_array;
            }
            array[counter++] = value;
        }
    }

    class StringArrayAndCounter {
        String[] array;

        Integer counter;

        Integer current_length;

        public StringArrayAndCounter(Integer initial_length) {
            this.array = new String[initial_length];
            this.counter = 0;
            this.current_length = initial_length;
        }

        public void push(String value) {
            if (counter + 2 > current_length) {
                current_length = current_length + 16;
                String[] new_array = new String[current_length];
                for (int i = 0; i < counter; i++) {
                    new_array[i] = array[i];
                }
                array = new_array;
            }
            array[counter++] = value;
        }
    }

    class ComputeGenderReturn {
        private String gender;

        private Integer x_treatment;

        private Integer y_treatment;

        public ComputeGenderReturn(String gender, Integer x_treatment, Integer y_treatment) {
            this.gender = gender;
            this.x_treatment = x_treatment;
            this.y_treatment = y_treatment;
        }

        public String getGender() {
            return gender;
        }

        public Integer getX_treatment() {
            return x_treatment;
        }

        public Integer getY_treatment() {
            return y_treatment;
        }
    }
}


