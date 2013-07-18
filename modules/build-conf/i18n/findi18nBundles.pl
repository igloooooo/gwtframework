#!/usr/bin/perl -w
use strict;
use Cwd 'abs_path';

my $dirname;
($dirname = "./".$0) =~ s%/[^/]*$%%;

my $bssTopLevel = abs_path($dirname);
# go up 2 dirs to get to the bss root
$bssTopLevel =~ s/(.*[\\\/]).*?[\\\/].*?[\\\/].*?[\\\/]*\s*$/$1/g;

my @propertiesFiles = `find $bssTopLevel/modules/*/src/ -name \*Constants.properties`;

foreach my $prop (@propertiesFiles)
{
    $prop =~ s/^\s*(.*?)\s*$/$1/g;
    print $prop."\n";
}


