#!/usr/bin/perl -w

use strict;

open(M, 'MoffetData-C0130D.csv');

$_ = <M>;
s/LAST_CONTACT_DATE,/LAST_CONTACT_DATE,DEATH_DATE,/;
print $_;

while (<M>) {
	my @f = split /,/;
	my $dead_flag = $f[9];
	my $last_contact_data = $f[16];
	my @newf = @f;
	foreach my $i (17..$#f) {
		$newf[$i + 1] = $f[$i];
	}

	if ($dead_flag eq 'Dead') {
		$newf[17] = $f[16];
	} else {
		$newf[17] = '';
	}

	print join(",", @newf);
}
