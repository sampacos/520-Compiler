  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        4
  5         LOAD         3[LB]
  6         CALL         newarr  
  7         LOADL        1
  8         LOAD         4[LB]
  9         LOADL        0
 10         LOAD         5[LB]
 11         CALL         arrayupd
 12         JUMP         L11
 13  L10:   LOAD         4[LB]
 14         LOAD         5[LB]
 15         LOAD         4[LB]
 16         LOAD         5[LB]
 17         LOADL        1
 18         CALL         sub     
 19         CALL         arrayref
 20         LOAD         5[LB]
 21         CALL         add     
 22         CALL         arrayupd
 23         LOAD         5[LB]
 24         LOADL        1
 25         CALL         add     
 26         STORE        5[LB]
 27         POP          0
 28  L11:   LOAD         5[LB]
 29         LOAD         4[LB]
 30         CALL         arraylen
 31         CALL         lt      
 32         JUMPIF (1)   L10
 33         LOAD         4[LB]
 34         LOADL        3
 35         CALL         arrayref
 36         LOADL        2
 37         CALL         add     
 38         STORE        6[LB]
 39         LOAD         6[LB]
 40         CALL         putintnl
 41         RETURN (0)   1
 42  L12:   LOADL        4
 43         LOAD         3[LB]
 44         CALL         newarr  
 45         LOADL        1
 46         LOAD         4[LB]
 47         LOADL        0
 48         LOAD         5[LB]
 49         CALL         arrayupd
 50         JUMP         L14
 51  L13:   LOAD         4[LB]
 52         LOAD         5[LB]
 53         LOAD         4[LB]
 54         LOAD         5[LB]
 55         LOADL        1
 56         CALL         sub     
 57         CALL         arrayref
 58         LOAD         5[LB]
 59         CALL         add     
 60         CALL         arrayupd
 61         LOAD         5[LB]
 62         LOADL        1
 63         CALL         add     
 64         STORE        5[LB]
 65         POP          0
 66  L14:   LOAD         5[LB]
 67         LOAD         4[LB]
 68         CALL         arraylen
 69         CALL         lt      
 70         JUMPIF (1)   L13
 71         LOAD         4[LB]
 72         LOADL        3
 73         CALL         arrayref
 74         LOADL        2
 75         CALL         add     
 76         STORE        6[LB]
 77         LOAD         6[LB]
 78         CALL         putintnl
 79         RETURN (0)   1
