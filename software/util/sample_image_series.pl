#!/usr/bin/perl -w

use strict;

open(I, 'rembrandt_ncia_image_series.txt');

<I>;

my %done;

while (<I>) {
	chomp;
	my @f = split /\t/;
	if (!$done{$f[0]}) {
		print "$_\n";
	}
	$done{$f[0]} = 1;
}