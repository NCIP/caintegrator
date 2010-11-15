package gov.nih.nci.caintegrator2.heatmap;

public class HeatMapArgs {
    String small_bin_f = null, big_bin_f = null, seg_f = null, sample_f = null, genome_out_f = null, gene_out_f = null,
            title = null, refFlat_f = null, gender_f = null;

    String protocol = "", scale = null;

    // exclude very small segments: they are far more likely to be germline copy number variation loci
    Integer min_seg_length = 500; // default

    Integer BASE = 2;

    String platform = null, submitter = null, contact = null, cnv_track = null, project = null;
}
