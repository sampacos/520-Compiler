  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        1
  5         CALL         neg     
  6         LOADL        0
  7         JUMP         L11
  8  L10:   LOAD         4[LB]
  9         LOADL        1
 10         CALL         add     
 11         STORE        4[LB]
 12         LOAD         4[LB]
 13         STORE        3[LB]
 14         POP          0
 15  L11:   LOAD         4[LB]
 16         LOADL        4
 17         CALL         lt      
 18         JUMPIF (1)   L10
 19         LOAD         3[LB]
 20         CALL         putintnl
 21         RETURN (0)   1
 22  L12:   LOADL        1
 23         CALL         neg     
 24         LOADL        0
 25         JUMP         L14
 26  L13:   LOAD         4[LB]
 27         LOADL        1
 28         CALL         add     
 29         STORE        4[LB]
 30         LOAD         4[LB]
 31         STORE        3[LB]
 32         POP          0
 33  L14:   LOAD         4[LB]
 34         LOADL        4
 35         CALL         lt      
 36         JUMPIF (1)   L13
 37         LOAD         3[LB]
 38         CALL         putintnl
 39         RETURN (0)   1
