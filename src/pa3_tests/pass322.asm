  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        -1
  5         LOADL        0
  6         CALL         newobj  
  7         LOADA        0[OB]
  8         LOAD         3[LB]
  9         LOADA        0[OB]
 10         LOAD         3[LB]
 11         LOADL        5
 12         CALL         eq      
 13         CALL         and     
 14         STORE        4[LB]
 15         RETURN (0)   1
 16         LOADL        5
 17         RETURN (1)   0
 18         LOADL        1
 19         LOADL        0
 20         CALL         eq      
 21         RETURN (1)   0
 22  L10:   LOADL        -1
 23         LOADL        0
 24         CALL         newobj  
 25         LOADA        0[OB]
 26         LOAD         3[LB]
 27         LOADA        0[OB]
 28         LOAD         3[LB]
 29         LOADL        5
 30         CALL         eq      
 31         CALL         and     
 32         STORE        4[LB]
 33         RETURN (0)   1
