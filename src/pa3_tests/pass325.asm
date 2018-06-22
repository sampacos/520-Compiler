  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        0
  5         LOADA        0[HB]
  6         LOADL        0
  7         CALL         fieldref
  8         LOADL        0
  9         LOADL        3
 10         CALL         fieldupd
 11         RETURN (0)   0
 12         LOADL        0
 13         RETURN (0)   1
 14  L10:   RETURN (0)   1
