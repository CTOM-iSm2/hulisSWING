%chk=furane_nrt5.chk
# pop=nboread b3lyp/6-31g(d)

Furane

0 1

$nbo nrt
$end
$NRTSTR
     STR        ! Wgt = 42.26%
     LONE 5 2 END
     BOND D 1 2 S 1 5 S 1 6 S 2 3 S 2 7 D 3 4 S 3 8 S 4 5 S 4 9 END
   END
   STR        ! Wgt =  9.10%
     LONE 3 1 5 1 END
     BOND D 1 2 S 1 5 S 1 6 S 2 3 S 2 7 S 3 4 S 3 8 D 4 5 S 4 9 END
   END
   STR        ! Wgt =  8.96%
     LONE 2 1 5 1 END
     BOND S 1 2 D 1 5 S 1 6 S 2 3 S 2 7 D 3 4 S 3 8 S 4 5 S 4 9 END
   END
   STR        ! Wgt =  7.06%
     LONE 5 2 END
     BOND S 1 2 S 1 4 S 1 5 S 1 6 D 2 3 S 2 7 S 3 4 S 3 8 S 4 5 S 4 9 END
   END
   STR        ! Wgt =  2.54%
     LONE 1 1 5 2 END
     BOND S 1 2 S 1 5 S 1 6 D 2 3 S 2 7 S 3 4 S 3 8 S 4 5 S 4 9 END
   END
   STR        ! Wgt =  2.54%
     LONE 4 1 5 2 END
     BOND S 1 2 S 1 5 S 1 6 D 2 3 S 2 7 S 3 4 S 3 8 S 4 5 S 4 9 END
   END 
$END



