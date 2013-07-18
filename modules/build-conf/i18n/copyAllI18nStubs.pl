#!/usr/bin/perl -w
use strict;

my $dirname;
($dirname = "./".$0) =~ s%/[^/]*$%%;

my $findBundlesPath = "$dirname/findi18nBundles.pl";

my $errorsfound = undef;
my @bundles = `perl $findBundlesPath`;

my @copyTo = ("bg", "fr", "ru");

foreach my $bundle (@bundles)
{
    print "Bundle: $bundle\n";
    foreach my $copyTo (@copyTo) {
        $bundle =~ /^\s*(.*)(\.properties)\s*$/;
        my $copyToFile = $1."_".$copyTo.$2;
        open(FROM, "< $bundle") or die "Cannot open $bundle for reading: $!\n";
        open(TO, "> $copyToFile") or die "Cannot open $copyToFile for writing: $!\n";
        my $ct = uc($copyTo);
        while(<FROM>) {
            s/\=/\=$ct/g;
            s/^\s*(.*?)\s*$/$1/g;
            print TO $_."\n";
        }
        close(FROM);
        close(TO);
        print "Copied from $bundle to $copyTo\n";
    }
}

if ($errorsfound) {
    exit(1);
}

