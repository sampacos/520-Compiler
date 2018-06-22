  0  L10:   LOADL        0
  1         CALL         newarr  
  2         CALL         L14
  3         HALT   (0)   
  4         LOADL        -1
  5         LOADL        0
  6         CALL         newobj  
  7         LOADL        0
  8         JUMP         L12
  9  L11:   LOADA        0[OB]
 10         LOAD         3[LB]
 11         CALLI        L10
 12         POP          1
 13         LOAD         4[LB]
 14         LOADL        1
 15         CALL         add     
 16         STORE        4[LB]
 17         POP          0
 18  L12:   LOAD         4[LB]
 19         LOADL        1025
 20         CALL         lt      
 21         JUMPIF (1)   L11
 22         LOADL        25
 23         CALL         putintnl
 24         RETURN (0)   1
 25  L13:   LOADL        55
 26         RETURN (1)   0
 27  L14:   LOADL        -1
 28         LOADL        0
 29         CALL         newobj  
 30         LOADL        0
 31         JUMP         L16
 32  L15:   LOADA        0[OB]
 33         LOAD         3[LB]
 34         CALLI        L13
 35         POP          1
 36         LOAD         4[LB]
 37         LOADL        1
 38         CALL         add     
 39         STORE        4[LB]
 40         POP          0
 41  L16:   LOAD         4[LB]
 42         LOADL        1025
 43         CALL         lt      
 44         JUMPIF (1)   L15
 45         LOADL        25
 46         CALL         putintnl
 47         RETURN (0)   1
