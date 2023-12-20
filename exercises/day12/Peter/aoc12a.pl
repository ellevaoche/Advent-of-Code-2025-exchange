use strict;

use List::Util qw(sum);


sub completeRep {
    my ( $str, $numLst, $valRepRef, $doneStr ) = @_;
    my @num = split (/,/, $numLst );
#print "- $doneStr\n";
    if (sum(@num) > length($str)) {
        return;
    }

    if( $str =~ /^[\.\?]/) {
        my $inpStr = substr($str, 1);
        $inpStr =~ /^(\.*)(.+)/;
        completeRep($2, $numLst, $valRepRef, $doneStr.'.'.$1 );
    }
#print ": $doneStr|$str\n";

    while (length($str) >= $num[0]) {
#print ", ".substr($str.'.',0,$num[0]+1)."\n";
        if (substr($str.'.',0,$num[0]+1) =~ /^[#\?]+[\.\?]$/) {
            if ($#num) {
                my $t = substr($str,0,$num[0]);
                $t =~ tr/\?/#/;
                my $inpStr = substr($str, $num[0]+1);
                $inpStr =~ /^(\.*)(.+)/;
#print "-- $str $numLst $inpStr\n";
#print "- ".$doneStr.$t.'.'.$1.'|'.$2."  ($#num)\n";
                completeRep($2, substr($numLst, index($numLst,',')+1), $valRepRef, $doneStr.$t.'.'.$1 );
            } elsif (! (substr($str,$num[0]) =~ /#/)) {
                my $t = substr($str,0,$num[0]);
                $t =~ tr/\?/#/;
                $t .= substr($str,$num[0]);
                $t =~ tr/\?/./;
                $doneStr .= $t;
 print "= ".$doneStr."\n";
                (grep($doneStr eq $_, @$valRepRef)) or push(@$valRepRef, $doneStr);
            }
            last;
        } else {
            my $t = substr($str,0,$num[0]);
            if ($t =~ /^([#\?]+\.)([#\.\?]+)$/) {
                $t = $1;
                $str = $2.substr($str,$num[0]);
                $t =~ tr/\?/./;
                $doneStr .= $t; 
            } else {
                $t =~ tr/\?/./;
                $doneStr .= $t; 
                $str = substr($str,$num[0]);
            }
#print ". $doneStr|$str  (".length($str)." >= $num[0])\n";
        }
    }
#print "\n";

}





my $totalscore = 0;
my @map = ();

while (<>) {
	chop();
    if ( $_ =~ /([\.\?#]+) ([,\d]+)/ ) {
        push(@map, { str => $1, numLst => $2 } );
    } 
}


foreach my $row ( @map ) {
    my @valRep = ();
    my $inpStr = $$row{str};
print ": $$row{str} $$row{numLst}\n";
    $inpStr =~ /^(\.*)(.+)/;
    completeRep($2, $$row{numLst}, \@valRep, $1);
    $totalscore += $#valRep+1;
print " ".($#valRep+1)."\n";
}


print "\n== $totalscore\n";
