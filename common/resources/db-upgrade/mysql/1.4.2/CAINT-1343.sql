alter table abstract_reporter add column discriminator char(3);

-- Handle GENE_EXPRESSION_REPORTER data

update abstract_reporter ar 
set
	discriminator = 'EXP'
where
	id in (select id from gene_expression_reporter);

drop table gene_expression_reporter;
	
-- DNA_ANALYSIS_REPORTER fields
alter table abstract_reporter add column CHROMOSOME varchar(2);
alter table abstract_reporter add column POSITION INTEGER;
alter table abstract_reporter add column DB_SNP_ID varchar(255);
alter table abstract_reporter add column ALLELE_A CHAR;
alter table abstract_reporter add column ALLELE_B CHAR;

update abstract_reporter ar, dna_analysis_reporter dar
set
	ar.discriminator = 'DNA',
	ar.CHROMOSOME = dar.CHROMOSOME,	
	ar.POSITION = dar.POSITION,
	ar.DB_SNP_ID = dar.DB_SNP_ID,
	ar.ALLELE_A = dar.ALLELE_A,
	ar.ALLELE_B = dar.ALLELE_B
where ar.id = dar.id;

drop table dna_analysis_reporter;

-- GISTIC_GENOMIC_REGION_REPORTER

alter table abstract_reporter add column BROAD_OR_FOCAL varchar(55);
alter table abstract_reporter add column QVALUE double;
alter table abstract_reporter add column RESIDUAL_QVALUE double;
alter table abstract_reporter add column REGION_BOUNDARIES varchar(155);
alter table abstract_reporter add column WIDE_PEAK_BOUNDARIES varchar(155);
alter table abstract_reporter add column GENE_AMPLIFICATION_TYPE varchar(55);
alter table abstract_reporter add column GENOMIC_DESCRIPTOR varchar(100);

update abstract_reporter ar, gistic_genomic_region_reporter ggrr
set
	ar.discriminator = 'GIS',
	ar.BROAD_OR_FOCAL = ggrr.BROAD_OR_FOCAL,	
	ar.QVALUE = ggrr.QVALUE,	
	ar.RESIDUAL_QVALUE = ggrr.RESIDUAL_QVALUE,	
	ar.REGION_BOUNDARIES = ggrr.REGION_BOUNDARIES,	
	ar.WIDE_PEAK_BOUNDARIES = ggrr.WIDE_PEAK_BOUNDARIES,	
	ar.GENE_AMPLIFICATION_TYPE = ggrr.GENE_AMPLIFICATION_TYPE,	
	ar.GENOMIC_DESCRIPTOR = ggrr.GENOMIC_DESCRIPTOR
where ar.id = ggrr.id;

drop table gistic_genomic_region_reporter;
