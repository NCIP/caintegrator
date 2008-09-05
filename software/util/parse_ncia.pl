#!/usr/bin/perl -w

use strict;

open(R, 'rembrandt_clinical_report_fixed.txt');

my %sample_id;

while (<R>) {
	my @f = split /\t/;
	$sample_id{$f[0]} = 1; 
}	

open(I, 'rembrandt_ncia_table.txt');

while (<I>) {
	next if /^\s*NCI-1\s*$/;

	my @f = split /\s+/;

	print "$f[0]\t$f[1]\t$f[2]\n";

	print STDERR "sample id $f[0] not in clinical annotation\n" if !defined($sample_id{$f[0]});
}