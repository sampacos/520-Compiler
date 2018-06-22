  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        0
  5         RETURN (0)   1
  6         LOADA        0[OB]
  7         LOADL        0
  8         LOADL        5
  9         CALL         fieldupd
 10         RETURN (0)   0
 11         LOADL        0
 12  L10:   RETURN (0)   1
