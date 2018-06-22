  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        0
  5         LOADL        1
  6         LOADL        -1
  7         LOADL        2
  8         CALL         newobj  
  9         LOAD         3[LB]
 10         LOADL        0
 11         LOAD         3[LB]
 12         CALL         fieldupd
 13         LOAD         3[LB]
 14         LOADL        0
 15         CALL         fieldref
 16         LOADL        0
 17         CALL         fieldref
 18         LOADL        1
 19         LOADL        3
 20         CALL         fieldupd
 21         RETURN (0)   1
 22  L10:   LOADL        -1
 23         LOADL        2
 24         CALL         newobj  
 25         LOAD         3[LB]
 26         LOADL        0
 27         LOAD         3[LB]
 28         CALL         fieldupd
 29         LOAD         3[LB]
 30         LOADL        0
 31         CALL         fieldref
 32         LOADL        0
 33         CALL         fieldref
 34         LOADL        1
 35         LOADL        3
 36         CALL         fieldupd
 37         RETURN (0)   1
