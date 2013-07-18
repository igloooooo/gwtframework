#!/usr/bin/perl -w
use strict;

my $dirname;
($dirname = $0) =~ s%/[^/]*$%%;

my $findBundlesPath = "$dirname/findi18nBundles.pl";
my $checkPath = "$dirname/checki18nConsistency.pl";

my $errorsfound = undef;
my @bundles = `perl $findBundlesPath`;
foreach my $bundle (@bundles)
{
    my $result = system("perl $checkPath $bundle");
    if ($result != 0)
    {
        $errorsfound = 1;
    }
}

if ($errorsfound) {
    exit(1);
}