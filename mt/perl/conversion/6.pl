#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/(?<l>\w)\g1/$+{l}/g;
    print;
}
