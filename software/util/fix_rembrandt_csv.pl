#!/usr/bin/perl -w

use strict;

open(C, 'rembrandt_clinical_report.txt');

$_ = <C>; s/,/\t/g; print;

my %multi_cols = (8 => 1, 9 => 1, 10 => 1, 11 => 1, 15 => 1, 16 => 1, 17 => 1, 18 => 1, 19 => 1, 20 => 1, 21 => 1, 22 => 1);

my $r = 1;

while (<C>) {
	chomp;
	my @f = split /,/;

	my %skip;

	foreach my $i (0 .. $#f) {
		next if $skip{$i};

		if ($i >= 8) {
			if ($f[$i] eq  ' --') {
				print "\t--";
			} elsif ($f[$i] =~ / $/) {
				$f[$i] =~ s/^ //;
				$f[$i] =~ s/ $//;
				print "\t$f[$i]" if $i > 0;
				print "$f[$i]" if $i == 0; 
			} else {
				$f[$i] =~ s/^ //;
				$f[$i] =~ s/ $//;
				print "\t\"$f[$i]";
				foreach my $j ($i + 1 .. $#f) {
					$skip{$j} = 1;
					if ($f[$j] =~ / $/) {
						$f[$j] =~ s/^ //;
						$f[$j] =~ s/ $//;
						print ", $f[$j]\"";
						last;
					} else {
						$f[$j] =~ s/^ //;
						$f[$j] =~ s/ $//;
						print ", $f[$j]";
					}
				}
			}
		} else {
			$f[$i] =~ s/^ //;
			print "\t$f[$i]" if $i > 0;
			print "$f[$i]" if $i == 0; 
		}
	}

	print "\n";
	# last;
	$r++;
}
 