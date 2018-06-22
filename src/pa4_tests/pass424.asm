  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        24
  5         LOADL        0
  6         JUMP         L11
  7  L10:   LOAD         4[LB]
  8         LOAD         4[LB]
  9         LOADL        1
 10         CALL         add     
 11         STORE        4[LB]
 12         POP          1
 13  L11:   LOAD         4[LB]
 14         LOADL        1025
 15         CALL         lt      
 16         JUMPIF (1)   L10
 17         LOAD         3[LB]
 18         CALL         putintnl
 19         RETURN (0)   1
 20  L12:   LOADL        24
 21         LOADL        0
 22         JUMP         L14
 23  L13:   LOAD         4[LB]
 24         LOAD         4[LB]
 25         LOADL        1
 26         CALL         add     
 27         STORE        4[LB]
 28         POP          1
 29  L14:   LOAD         4[LB]
 30         LOADL        1025
 31         CALL         lt      
 32         JUMPIF (1)   L13
 33         LOAD         3[LB]
 34         CALL         putintnl
 35         RETURN (0)   1
