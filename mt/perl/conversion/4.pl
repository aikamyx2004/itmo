#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

my @word = '\b\w+\b';
while (<>) {
    s/(?<f>@word)(?<m>\W+)(?<s>@word)/$+{s}$+{m}$+{f}/;
    print;
}