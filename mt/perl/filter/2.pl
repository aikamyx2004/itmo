use strict;
use warnings FATAL => 'all';


while (<>) {
    print if /(^(\W*)cat(\W*)$)|(^(\W*)cat(\W))|((\W)cat(\W))|((\W)cat(\W*)$)/;
}