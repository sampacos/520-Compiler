  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L11
  3         HALT   (0)   
  4         LOADL        0
  5         LOADL        -1
  6         LOADL        1
  7         CALL         newobj  
  8         LOAD         3[LB]
  9         LOADL        0
 10         LOADL        0
 11         CALL         fieldupd
 12         LOADL        22
 13         STORE        4[LB]
 14         LOADL        0
 15         LOADA        0[OB]
 16         LOAD         3[LB]
 17         CALL         and     
 18         STORE        5[LB]
 19         LOADL        1
 20         LOADA        0[OB]
 21         LOAD         3[LB]
 22         CALL         or      
 23         CALL         not     
 24         STORE        6[LB]
 25         LOAD         3[LB]
 26         LOADL        0
 27         CALL         fieldref
 28         JUMPIF (0)   L10
 29         LOADL        1
 30         CALL         neg     
 31         STORE        4[LB]
 32  L10:   LOAD         4[LB]
 33         CALL         putintnl
 34         RETURN (0)   1
 35         LOADA        0[OB]
 36         LOADL        0
 37         LOADL        1
 38         CALL         fieldupd
 39         LOADL        1
 40         RETURN (1)   0
 41  L11:   LOADL        -1
 42         LOADL        1
 43         CALL         newobj  
 44         LOAD         3[LB]
 45         LOADL        0
 46         LOADL        0
 47         CALL         fieldupd
 48         LOADL        22
 49         STORE        4[LB]
 50         LOADL        0
 51         LOADA        0[OB]
 52         LOAD         3[LB]
 53         CALL         and     
 54         STORE        5[LB]
 55         LOADL        1
 56         LOADA        0[OB]
 57         LOAD         3[LB]
 58         CALL         or      
 59         CALL         not     
 60         STORE        6[LB]
 61         LOAD         3[LB]
 62         LOADL        0
 63         CALL         fieldref
 64         JUMPIF (0)   L12
 65         LOADL        1
 66         CALL         neg     
 67         STORE        4[LB]
 68  L12:   LOAD         4[LB]
 69         CALL         putintnl
 70         RETURN (0)   1
