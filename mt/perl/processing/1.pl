use strict;
use warnings FATAL => 'all';

my $ws = '[ ]';
my $ln = '\s*\n';


my @lines = <STDIN>;

foreach my $line (@lines) {
    $line =~ s/^$ws*//;
    $line =~ s/$ws*$//;
    $line =~ s/($ws){2,}/ /g;
}

my $file = join("", @lines);

$file =~ s/^$ln*//;
$file =~ s/$ln*$//;
$file =~ s/($ln){2,}/\n\n/g;
print($file)