  0  L10:   LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        -1
  5         LOADL        1
  6         CALL         newobj  
  7         LOAD         3[LB]
  8         CALLI        L10
  9         RETURN (0)   1
 10         LOADL        0
 11  L11:   LOADL        10
 12         LOAD         3[LB]
 13         CALL         putintnl
 14         RETURN (0)   0
 15  L12:   LOADL        -1
 16         LOADL        1
 17         CALL         newobj  
 18         LOADA        0[OB]
 19         LOAD         3[LB]
 20         CALLI        L11
 21         RETURN (0)   1
