  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L13
  3         HALT   (0)   
  4         LOADL        -1
  5         LOADL        0
  6         CALL         newobj  
  7         LOADL        1
  8         LOADL        7
  9         LOADA        0[OB]
 10         LOAD         3[LB]
 11         CALL         add     
 12         CALL         putintnl
 13         RETURN (0)   1
 14         LOADL        1
 15         CALL         neg     
 16         LOAD         -1[LB]
 17         LOADL        0
 18         CALL         le      
 19         JUMPIF (0)   L10
 20         LOADL        0
 21         STORE        3[LB]
 22         JUMP         L12
 23  L10:   LOAD         -1[LB]
 24         LOADL        1
 25         CALL         eq      
 26         JUMPIF (0)   L11
 27         LOADL        1
 28         STORE        3[LB]
 29         JUMP         L12
 30  L11:   LOAD         -1[LB]
 31         LOADL        1
 32         CALL         sub     
 33         LOADA        0[OB]
 34         LOAD         -1[LB]
 35         LOADL        2
 36         CALL         sub     
 37         LOADA        0[OB]
 38         CALL         add     
 39         STORE        3[LB]
 40  L12:   LOAD         3[LB]
 41         RETURN (1)   1
 42  L13:   LOADL        -1
 43         LOADL        0
 44         CALL         newobj  
 45         LOADL        1
 46         LOADL        7
 47         LOADA        0[OB]
 48         LOAD         3[LB]
 49         CALL         add     
 50         CALL         putintnl
 51         RETURN (0)   1
