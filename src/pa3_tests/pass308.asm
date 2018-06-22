  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        20
  5         LOADA        0[OB]
  6         RETURN (0)   1
  7         LOADL        50
  8         RETURN (1)   1
  9  L10:   LOADL        20
 10         LOADA        0[OB]
 11         RETURN (0)   1
