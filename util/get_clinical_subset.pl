#!/usr/bin/perl -w

use strict;

open(I, 'rembrandt_ncia_table.txt');

my %ncia_subjects;

while (<I>) {
	chomp;
	my @f = split /\t/;
	$f[0] =~ s/ //g;
	$ncia_subjects{$f[0]} = 1;
}

open(C, 'rembrandt_clinical_Aug08_fixed.txt');

while (<C>) {
	chomp;
	my @f = split /\t/;
	if (defined($ncia_subjects{$f[0]})) {
		s/\t/,/g;
		print "$_\n";
	}
}	