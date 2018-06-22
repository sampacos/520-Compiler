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
 27         LOADL        11
 28         CALL         fieldupd
 29         LOADA        0[OB]
 30         LOADL        1
 31         CALL         fieldref
 32         LOADL        1
 33         CALL         fieldref
 34         LOADL        0
 35         CALL         fieldref
 36         STORE        3[LB]
 37         LOAD         3[LB]
 38         CALL         putintnl
 39         RETURN (0)   0
 40         LOADL        0
 41         LOADL        1
 42  L12:   LOADL        -1
 43         LOADL        2
 44         CALL         newobj  
 45         LOAD         3[LB]
 46         LOADL        1
 47         LOADL        -1
 48         LOADL        2
 49         CALL         newobj  
 50         CALL         fieldupd
 51         LOAD         3[LB]
 52         LOADL        1
 53         CALL         fieldref
 54         LOADL        1
 55         LOAD         3[LB]
 56         CALL         fieldupd
 57         LOAD         3[LB]
 58         CALLI        L11
 59         RETURN (0)   1
