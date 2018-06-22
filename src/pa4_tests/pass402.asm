  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        1
  5         LOAD         3[LB]
  6         LOADL        2
  7         LOAD         3[LB]
  8         CALL         mult    
  9         CALL         add     
 10         LOADL        1
 11         CALL         sub     
 12         STORE        4[LB]
 13         LOAD         4[LB]
 14         CALL         putintnl
 15         RETURN (0)   1
 16  L10:   LOADL        1
 17         LOAD         3[LB]
 18         LOADL        2
 19         LOAD         3[LB]
 20         CALL         mult    
 21         CALL         add     
 22         LOADL        1
 23         CALL         sub     
 24         STORE        4[LB]
 25         LOAD         4[LB]
 26         CALL         putintnl
 27         RETURN (0)   1
