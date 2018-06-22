  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        0
  5         LOADA        0[OB]
  6         LOADL        0
  7         CALL         fieldref
  8         CALL         fieldref
  9         STORE        3[LB]
 10         RETURN (0)   1
 11  L10:   LOADA        0[OB]
 12         LOADL        0
 13         CALL         fieldref
 14         CALL         fieldref
 15         STORE        3[LB]
 16         RETURN (0)   1
