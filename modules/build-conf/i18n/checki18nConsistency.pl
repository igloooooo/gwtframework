#!/usr/bin/perl -w
use strict;

if ($#ARGV < 0)
{
    die "Usage: $0 <filename> where filename is the base of the i18n constants file in the resource bundle\n";
}

my $debug = undef;

my $errorsfound = undef;

my $filename = $ARGV[0];
$filename =~ /(.*[\/\\].*?)\.properties\s*$/;
my @bundlefiles = <$1*\.properties>;

my %constants;

# load constants with the things in the first file
open(F, "< $filename") or die "Cannot open '$filename': $!\n";
while (<F>) {
    if ( /^\s*\w/ ) {
        /^\s*(\w+)\s*\=.*?$/;
        my $c = $1;
        $constants{$c} = 1;
        if ($debug) { print "$c\n"; }
    }
}
close(F);

print "root bundle: $filename constants: ".scalar(keys %constants)."\n";
foreach my $f (@bundlefiles)
{
	#print "checking bundle file: $f\n";
    my %fconstants;
    my %additionalConstants;
    my %missingConstants;

    open(F, "< $f") or die "Cannot open '$f': $!\n";
    while (<F>) {
        if ( /^\s*\w/ ) {
            /^\s*(\w+)\s*\=.*?$/;
            my $c = $1;
            $fconstants{$c} = 1;
            unless (defined($constants{$c})) {
                $additionalConstants{$c} = 1;
            }
        }
    }
    close(F);

    foreach my $realconstant (sort keys %constants) {
        unless (defined($fconstants{$realconstant})) {
            $missingConstants{$realconstant} = 1;
        }
    }

    if ((keys %additionalConstants) > 0) {
        $errorsfound = 1;
        warn "$f contains additional constants\n";
        foreach (sort keys %additionalConstants) {
            warn "\t$_\n";
        }
    }

    if ((keys %missingConstants) > 0) {
        $errorsfound = 1;
        warn "$f is missing constants\n";
        foreach (sort keys %missingConstants) {
            warn "\t$_\n";
        }
    }
}

if ($errorsfound) {
    exit(1);
}

