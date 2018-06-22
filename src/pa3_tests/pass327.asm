  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        0
  5         RETURN (0)   1
  6         LOAD         -1[LB]
  7         LOADA        0[OB]
  8         LOADL        0
  9         LOAD         3[LB]
 10         CALL         fieldupd
 11         RETURN (0)   1
 12  L10:   RETURN (0)   1
