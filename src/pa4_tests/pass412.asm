  0  L10:   LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        -1
  5         LOADL        2
  6         CALL         newobj  
  7         LOAD         3[LB]
  8         LOADL        1
  9         LOADL        -1
 10         LOADL        2
 11         CALL         newobj  
 12         CALL         fieldupd
 13         LOAD         3[LB]
 14         LOADL        1
 15         CALL         fieldref
 16         LOADL        1
 17         LOAD         3[LB]
 18         CALL         fieldupd
 19         LOAD         3[LB]
 20         CALLI        L10
 21         RETURN (0)   1
 22         LOADL        0
 23         LOADL        1
 24  L11:   LOADL        10
 25         LOADA        0[OB]
 26         LOADL        0
 27         LOADL        4
 28         CALL         fieldupd
 29         LOADL        1
 30         LOADL        3
 31         LOADL        4
 32         LOADA        0[OB]
 33         CALL         add     
 34         STORE        3[LB]
 35         LOAD         3[LB]
 36         CALL         putintnl
 37         RETURN (0)   0
 38         LOADA        0[OB]
 39         LOADL        0
 40         CALL         fieldref
 41         CALL         fieldref
 42         LOAD         -2[LB]
 43         CALL         add     
 44         LOAD         -1[LB]
 45         CALL         add     
 46         RETURN (1)   2
 47         LOADL        0
 48         LOADL        1
 49  L12:   LOADL        -1
 50         LOADL        2
 51         CALL         newobj  
 52         LOAD         3[LB]
 53         LOADL        1
 54         LOADL        -1
 55         LOADL        2
 56         CALL         newobj  
 57         CALL         fieldupd
 58         LOAD         3[LB]
 59         LOADL        1
 60         CALL         fieldref
 61         LOADL        1
 62         LOAD         3[LB]
 63         CALL         fieldupd
 64         LOAD         3[LB]
 65         CALLI        L11
 66         RETURN (0)   1
