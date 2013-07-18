#!/usr/bin/perl -w
use strict;

my $dirname;
($dirname = $0) =~ s%/[^/]*$%%;

my $checkAllScript = "$dirname/checkAlli18n.pl";
my $result = system("perl $checkAllScript");
my $errorsfound;

if (@ARGV > 0)
{
    my $tmpDir = $ARGV[0];
    system("mkdir $tmpDir");
}

if ($result != 0)
{
        $errorsfound = 1;
}

if ($errorsfound) {
    exit(1);
}