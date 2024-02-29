use strict;
use warnings FATAL => 'all';

my $html_tag = '<(\w+)($any*?)>(?<content>$any*?)<\/\g1>';
my $any = '(.|\s)';

my $html_file = join("", <STDIN>);
$html_file =~ s/<[^>]+>//g;
# while($html_file =~ /<(\w+)($any*?)>(?<content>$any*?)<\/\g1>/) {
#     $html_file =~ s/<(\w+)($any*?)>(?<content>$any*?)<\/\g1>/$+{content}/g;
# }


my $ws = '[ ]';
my $ln = '\s*\n';

my @lines = split('\n', $html_file);

foreach my $line (@lines) {
    $line =~ s/^$ws*//;
    $line =~ s/$ws*$//;
    $line =~ s/($ws){2,}/ /g;
}

my $file = join("\n", @lines);

$file =~ s/^$ln*//;
$file =~ s/$ln*$//;
$file =~ s/($ln){2,}/\n\n/g;
print($file)