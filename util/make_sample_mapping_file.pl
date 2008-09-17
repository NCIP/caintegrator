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

open(R, 'Rembrandt_sample_names.txt');

while (<R>) {
	if (/(E\d\d\d\d\d)/) {
		next if !defined($ncia_subjects{$1});
		print "$1,$_";
	}	
}
