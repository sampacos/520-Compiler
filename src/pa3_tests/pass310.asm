  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        4
  5         LOAD         3[LB]
  6         CALL         newarr  
  7         RETURN (0)   1
  8  L10:   LOADL        4
  9         LOAD         3[LB]
 10         CALL         newarr  
 11         RETURN (0)   1
