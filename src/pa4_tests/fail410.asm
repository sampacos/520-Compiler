  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        10
  3         CALL         newarr  
  4         STORE        3[LB]
  5         LOAD         3[LB]
  6         LOADL        0
  7         LOADL        1
  8         CALL         neg     
  9         CALL         fieldupd
 10         RETURN (0)   1
 11         RETURN (0)   0
 12  L11:   LOADL        -1
 13         LOADL        0
 14         LOADL        0
 15         LOADL        -1
 16         CALL         L10
 17         HALT   (0)   
