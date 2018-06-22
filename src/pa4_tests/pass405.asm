  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        -1
  5         LOADL        1
  6         CALL         newobj  
  7         LOADL        5
  8         LOAD         3[LB]
  9         LOADL        0
 10         CALL         fieldref
 11         CALL         add     
 12         STORE        4[LB]
 13         LOAD         4[LB]
 14         CALL         putintnl
 15         RETURN (0)   1
 16         LOADL        0
 17  L10:   LOADL        -1
 18         LOADL        1
 19         CALL         newobj  
 20         LOADL        5
 21         LOAD         3[LB]
 22         LOADL        0
 23         CALL         fieldref
 24         CALL         add     
 25         STORE        4[LB]
 26         LOAD         4[LB]
 27         CALL         putintnl
 28         RETURN (0)   1
