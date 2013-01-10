/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.heatmap;

import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class invokeCBS2HeatMap {
    // static String small_bin_f = null, big_bin_f = null, seg_f = null, sample_f = null, genome_out_f = null,
    // gene_out_f = null, title = null, refFlat_f = null, gender_f = null;
    // static String protocol = "", scale = null;
    // // exclude very small segments: they are far more likely to be germline copy number variation loci
    // static Integer min_seg_length = 500; // default
    // static Integer BASE = 2;
    // static String platform = null, submitter = null, contact = null, cnv_track = null, project = null;

    public static void main(String[] args) {
        HeatMapArgs hma = new HeatMapArgs();
        ReadOptions(args, hma);

        if ((hma.getGenomeOutFile() == null) && (hma.getGeneOutFile() == null)) {
            System.out.println("must specify at least one (gene or genome) output file");
            System.exit(-1);
        }
        if (hma.getBigBinFile() == null) {
            System.out.println("must specify big bin file");
            System.exit(-1);
        }
        if (hma.getSmallBinFile() == null) {
            System.out.println("must specify small bin file");
            System.exit(-1);
        }
        if (hma.getSegmentFile() == null) {
            System.out.println("must specify segmented data file");
            System.exit(-1);
        }
        if (hma.getRefGenesFile() == null) {
            System.out.println("must specify RefSeq gene flat file");
            System.exit(-1);
        }
        CBSToHeatmap CBS2HM = new CBS2HeatMap();
        try {
            CBS2HM.runCBSToHeatmap(hma);
            System.out.println("Completed successfully, the output file(s) are here: " 
                    + hma.getGeneOutFile() + ", " + hma.getGenomeOutFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ReadOptions(String[] args, HeatMapArgs hma) {
        try {
            Options opt = new Options();

            opt.addOption("h", false, "Print help for this application");
            opt.addOption("big_bins", true, "name of big bin definition file");
            opt.addOption("small_bins", true, "name of small bin definition file");
            opt.addOption("seg", true, "name of file containing segmented copy number data");
            opt.addOption("samples", true, "name of file specifying samples to include");
            opt.addOption("genomeout", true, "name of genome matrix output file");
            opt.addOption("geneout", true, "name of gene matrix output file");
            opt.addOption("title", true, "title for output heatmap");
            opt.addOption("genes", true, "name of RefSeq gene flat file");
            opt.addOption("gender", true, "name of file mapping patients to gender");
            opt.addOption("protocol", true, "matched or unmatched");
            opt
                    .addOption("scale", true,
                            "comma-separated list of real positive numbers for interval upperbounds (default is '0.5,1.5,2.5,3.5,4.5')");
            opt.addOption("minseg", true, "minimum segment length to consider (default = 500)");
            opt.addOption("platform", true, "name of platform");
            opt.addOption("submitter", true, "name of center that submitted the data");
            opt.addOption("contact", true, "contact: name of NCI contact person for this heatmap");
            opt.addOption("cnv_track", true, "name of file with cnv track info");
            opt.addOption("project", true, "project: name of project");
            opt.addOption("base", true, "assumed base (can be float) for log ratio values (defaults to 2)");

            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);

            if (cl.hasOption("h")) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp("OptionsTip", opt);
                System.exit(0);
            } else {
                if (cl.hasOption("small_bins")) {
                    hma.setSmallBinFile(cl.getOptionValue("small_bins"));
                }
                if (cl.hasOption("big_bins")) {
                    hma.setBigBinFile(cl.getOptionValue("big_bins"));
                }
                if (cl.hasOption("seg")) {
                    hma.setSegmentFile(cl.getOptionValue("seg"));
                }
                if (cl.hasOption("samples")) {
                    hma.setSampleFile(cl.getOptionValue("samples"));
                }
                if (cl.hasOption("genomeout")) {
                    hma.setGenomeOutFile(cl.getOptionValue("genomeout"));
                }
                if (cl.hasOption("geneout")) {
                    hma.setGeneOutFile(cl.getOptionValue("geneout"));
                }
                if (cl.hasOption("title")) {
                    hma.setTitle(cl.getOptionValue("title"));
                }
                if (cl.hasOption("genes")) {
                    hma.setRefGenesFile(cl.getOptionValue("genes"));
                }
                if (cl.hasOption("gender")) {
                    hma.setGenderFile(cl.getOptionValue("gender"));
                }
                if (cl.hasOption("protocol")) {
                    hma.setProtocol(cl.getOptionValue("protocol"));
                }
                if (cl.hasOption("scale")) {
                    hma.setScale(cl.getOptionValue("scale"));
                }
                if (cl.hasOption("minseg")) {
                    hma.setMinSegLength(Integer.parseInt(cl.getOptionValue("minseg")));
                }
                if (cl.hasOption("platform")) {
                    hma.setPlatform(cl.getOptionValue("platform"));
                }
                if (cl.hasOption("submitter")) {
                    hma.setSubmitter(cl.getOptionValue("submitter"));
                }
                if (cl.hasOption("contact")) {
                    hma.setContact(cl.getOptionValue("contact"));
                }
                if (cl.hasOption("cnv_track")) {
                    hma.setCnvTrack(cl.getOptionValue("cnv_track"));
                }
                if (cl.hasOption("project")) {
                    hma.setProject(cl.getOptionValue("project"));
                }
                if (cl.hasOption("base")) {
                    hma.setBase(Integer.parseInt(cl.getOptionValue("base")));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
