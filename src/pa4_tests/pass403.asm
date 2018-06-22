  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        2
  5         LOAD         3[LB]
  6         LOADL        2
  7         CALL         eq      
  8         JUMPIF (0)   L10
  9         LOADL        3
 10         STORE        3[LB]
 11         JUMP         L11
 12  L10:   LOADL        1
 13         CALL         neg     
 14         STORE        3[LB]
 15  L11:   LOAD         3[LB]
 16         CALL         putintnl
 17         RETURN (0)   1
 18  L12:   LOADL        2
 19         LOAD         3[LB]
 20         LOADL        2
 21         CALL         eq      
 22         JUMPIF (0)   L13
 23         LOADL        3
 24         STORE        3[LB]
 25         JUMP         L14
 26  L13:   LOADL        1
 27         CALL         neg     
 28         STORE        3[LB]
 29  L14:   LOAD         3[LB]
 30         CALL         putintnl
 31         RETURN (0)   1
