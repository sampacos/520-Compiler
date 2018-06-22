  0         JUMP         L12
  1  L10:   LOADL        0
  2         CALL         fieldref
  3         LOADA        0[OB]
  4         LOADL        0
  5         LOADL        0
  6         CALL         fieldupd
  7  L11:   PUSH         2
  8         LOADL        0
  9         LOADL        1
 10         CALL         newobj  
 11         STORE        3[LB]
 12         LOADA        0[OB]
 13         LOAD         3[LB]
 14         LOADL        3
 15         RETURN (1)   0
 16         LOAD         3[LB]
 17         LOADL        0
 18         CALL         fieldref
 19         CALL         add     
 20         STORE        4[LB]
 21         RETURN (0)   1
 22         LOADL        3
 23         RETURN (1)   0
 24         RETURN (0)   0
 25  L12:   LOADL        -1
 26         LOADL        1
 27         LOADL        0
 28         LOADL        -1
 29         CALL         L11
 30         HALT   (0)   
