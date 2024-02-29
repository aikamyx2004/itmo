#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/(?<f>\w)(?<s>\w)(?<r>\w*)/$+{s}$+{f}$+{r}/g;
    print;
}