#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

use List::MoreUtils qw(uniq);
my $html_file = join("", <STDIN>);

my @links = $html_file =~ /<a.*?\shref="(.*?)".*?>/g;

# for my $link (@links) {
#     print("$link\n");
# }
my %seen = ();
my @uniq = ();
foreach my $item (@links) {
    if ($item =~ /^(.*?:\/\/)?(?<content>(\w.*?))($|\/|#|\?|:)/) {
        if (length($+{content}) != 0) {
            $seen{$+{content}}++;
        }
    }
}

foreach my $link (sort keys %seen) {
    print("$link\n")
}


