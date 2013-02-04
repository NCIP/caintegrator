/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.heatmap;

// package Segment;
// require Exporter;
// @ISA = qw(Exporter);
// @EXPORT = qw(
// );

// ## $Source: /cgap/schaefec/cvsroot/TCGA/Segment.pm,v $
// ## $Revision: 1.1 $
// ## $Date: 2009/02/26 14:19:43 $

// use strict;

public class Segment 
{
	public String chr;
	public Integer left, right;
	public String id;
	
	// ######################################################################
	public Segment( String chr, Integer left, Integer right, String id ) {
		// my ($self, $chr, $left, $right, $id) = @_;
		// my $s = {};
		// $s->{chr} = $chr;$s->{left} = $left;$s->{right} = $right;if (defined $id) {$s->{id} = $id;} return bless $s;
		this.chr = chr;
		this.left = left;
		this.right = right;
		if ( id != null ) {
			this.id = id;
		}
	}

	// ######################################################################
	public Segment( String chr, Integer left, Integer right ) {
		this.chr = chr;
		this.left = left;
		this.right = right;
	}

	// ######################################################################
	private Integer Min( Integer a, Integer b ) {
		if ( a < b ) {
			return a;
		} else {
			return b;
		}
	}

	// ######################################################################
	private Integer Max( Integer a, Integer b ) {
		if ( a > b ) {
			return a;
		} else {
			return b;
		}
	}

	// ######################################################################
	private Segment CreateNullSegment() {
		return new Segment("", 1, 0);
	}

	// ######################################################################
	private Integer IsNull() {
		if ( this.left > this.right ) {
			return 1;
		} else {
			return 0;
		}
	}

	// ######################################################################
	// sub Chr {
	//   my ($self) = @_;
	//   return $self->{chr};
	// }

	// ######################################################################
	// sub Left {
	//   my ($self) = @_;
	//   return $self->{left};
	// }

	// ######################################################################
	// sub Right {
	//   my ($self) = @_;
	//   return $self->{right};
	// }

	// ######################################################################
	// sub Id {
	//   my ($self) = @_;
	//   return $self->{id};
	// }

	// ######################################################################
	// sub Pred {
	//   my ($self, $s) = @_;

	//   if ($self->IsNull || $s->IsNull) {
	//     die "Pred: null segment";
	//   }
	//   my $chr = $self->{chr};
	//   if ($chr ne $s->{chr}) {
	//     die "Pred: incompatible chromosomes $self->{chr}, and $s->{chr}";
	//   }
	//   ## is $self a pred of $s?
	//   if (
	//       $self->{left}  <= $s->{left} &&
	//       $self->{right} >= $s->{left}) {
	//     return 1;
	//   } else {
	//     return 0;
	//   }
	// }

	// ######################################################################
	// sub Succ {
	//   my ($self, $s) = @_;

	//   if ($self->IsNull || $s->IsNull) {
	//     die "Succ: null segment";
	//   }
	//   my $chr = $self->{chr};
	//   if ($chr ne $s->{chr}) {
	//     die "Succ: incompatible chromosomes $self->{chr}, and $s->{chr}";
	//   }
	//   ## is $self a succ of $s?
	//   if (
	//       $self->{right} >= $s->{right} &&
	//       $self->{left}  <= $s->{right}) {
	//     return 1;
	//   } else {
	//     return 0;
	//   }
	// }

	// ######################################################################
	public Segment Overlap( Segment seg ) {
		if ( ! this.chr.equals( seg.chr ) ) {
			System.out.println( "Overlap: incompatible chromosomes " + this.chr + ", and " + seg.chr );
			System.exit( -1 );
		}
		if ( this.IsNull() == 1 || seg.IsNull() == 1 ) {
			return CreateNullSegment();
		}
		Integer max_left = Max( this.left, seg.left );
		Integer min_right = Min( this.right, seg.right );
		return new Segment( this.chr, max_left, min_right );
	}

	// ######################################################################
	public Integer Length() {
		return this.right - this.left + 1;
	}

	// ######################################################################
	// 1;
	// ######################################################################
}
