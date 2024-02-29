#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

while (<>) {
    s/\b(?<n>\d+)0\b/$+{n}/g;
    print;
}