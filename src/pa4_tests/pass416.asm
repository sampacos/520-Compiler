  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        1
  5         LOADL        5
  6         LOAD         3[LB]
  7         LOAD         4[LB]
  8         CALL         add     
  9         STORE        3[LB]
 10         POP          1
 11         LOAD         3[LB]
 12         LOADL        1
 13         CALL         add     
 14         STORE        3[LB]
 15         LOADL        9
 16         LOAD         3[LB]
 17         LOAD         6[LB]
 18         CALL         add     
 19         STORE        3[LB]
 20         POP          1
 21         LOAD         3[LB]
 22         CALL         putintnl
 23         RETURN (0)   1
 24  L10:   LOADL        1
 25         LOADL        5
 26         LOAD         3[LB]
 27         LOAD         4[LB]
 28         CALL         add     
 29         STORE        3[LB]
 30         POP          1
 31         LOAD         3[LB]
 32         LOADL        1
 33         CALL         add     
 34         STORE        3[LB]
 35         LOADL        9
 36         LOAD         3[LB]
 37         LOAD         6[LB]
 38         CALL         add     
 39         STORE        3[LB]
 40         POP          1
 41         LOAD         3[LB]
 42         CALL         putintnl
 43         RETURN (0)   1
