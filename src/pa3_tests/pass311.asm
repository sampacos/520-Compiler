  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        4
  5         LOAD         3[LB]
  6         CALL         newarr  
  7         LOADL        0
  8         LOADL        1
  9         STORE        5[LB]
 10         LOAD         4[LB]
 11         LOADL        0
 12         LOAD         5[LB]
 13         CALL         arrayupd
 14         JUMP         L11
 15  L10:   LOAD         4[LB]
 16         LOAD         5[LB]
 17         LOAD         4[LB]
 18         LOAD         5[LB]
 19         LOADL        1
 20         CALL         sub     
 21         CALL         arrayref
 22         LOAD         5[LB]
 23         CALL         add     
 24         CALL         arrayupd
 25         LOAD         5[LB]
 26         LOADL        1
 27         CALL         add     
 28         STORE        5[LB]
 29         POP          0
 30  L11:   LOAD         5[LB]
 31         LOAD         3[LB]
 32         CALL         lt      
 33         JUMPIF (1)   L10
 34         RETURN (0)   1
 35  L12:   LOADL        4
 36         LOAD         3[LB]
 37         CALL         newarr  
 38         LOADL        0
 39         LOADL        1
 40         STORE        5[LB]
 41         LOAD         4[LB]
 42         LOADL        0
 43         LOAD         5[LB]
 44         CALL         arrayupd
 45         JUMP         L14
 46  L13:   LOAD         4[LB]
 47         LOAD         5[LB]
 48         LOAD         4[LB]
 49         LOAD         5[LB]
 50         LOADL        1
 51         CALL         sub     
 52         CALL         arrayref
 53         LOAD         5[LB]
 54         CALL         add     
 55         CALL         arrayupd
 56         LOAD         5[LB]
 57         LOADL        1
 58         CALL         add     
 59         STORE        5[LB]
 60         POP          0
 61  L14:   LOAD         5[LB]
 62         LOAD         3[LB]
 63         CALL         lt      
 64         JUMPIF (1)   L13
 65         RETURN (0)   1
