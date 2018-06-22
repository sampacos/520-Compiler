  0  L10:   LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        -1
  5         LOADL        0
  6         CALL         newobj  
  7         LOAD         3[LB]
  8         CALLI        L10
  9         RETURN (0)   1
 10  L11:   LOADA        0[OB]
 11         LOADA        0[OB]
 12         STORE        3[LB]
 13         RETURN (0)   0
 14         LOADL        5
 15         RETURN (1)   1
 16  L12:   LOADL        -1
 17         LOADL        0
 18         CALL         newobj  
 19         LOADA        0[OB]
 20         LOAD         3[LB]
 21         CALLI        L11
 22         RETURN (0)   1
