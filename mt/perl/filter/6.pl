use strict;
use warnings FATAL => 'all';

while (<>) {
    print if /\b(\d+(\.\d+)?\b)/
}