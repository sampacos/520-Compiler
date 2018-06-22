  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L12
  3         HALT   (0)   
  4         LOADL        2
  5         LOADL        2
  6         LOADL        5
  7         CALL         mult    
  8         LOADL        3
  9         CALL         sub     
 10         CALL         newarr  
 11         STORE        4[LB]
 12         LOAD         4[LB]
 13         LOADL        0
 14         LOADL        13
 15         CALL         arrayupd
 16         LOAD         4[LB]
 17         LOAD         3[LB]
 18         LOADL        2
 19         CALL         sub     
 20         CALL         arrayref
 21         LOAD         3[LB]
 22         CALL         gt      
 23         STORE        5[LB]
 24         LOAD         5[LB]
 25         JUMPIF (0)   L10
 26         LOAD         4[LB]
 27         LOADL        0
 28         CALL         arrayref
 29         CALL         putintnl
 30         JUMP         L11
 31  L10:   LOADL        1
 32         CALL         neg     
 33         CALL         putintnl
 34  L11:   RETURN (0)   1
 35  L12:   LOADL        2
 36         LOADL        2
 37         LOADL        5
 38         CALL         mult    
 39         LOADL        3
 40         CALL         sub     
 41         CALL         newarr  
 42         STORE        4[LB]
 43         LOAD         4[LB]
 44         LOADL        0
 45         LOADL        13
 46         CALL         arrayupd
 47         LOAD         4[LB]
 48         LOAD         3[LB]
 49         LOADL        2
 50         CALL         sub     
 51         CALL         arrayref
 52         LOAD         3[LB]
 53         CALL         gt      
 54         STORE        5[LB]
 55         LOAD         5[LB]
 56         JUMPIF (0)   L13
 57         LOAD         4[LB]
 58         LOADL        0
 59         CALL         arrayref
 60         CALL         putintnl
 61         JUMP         L14
 62  L13:   LOADL        1
 63         CALL         neg     
 64         CALL         putintnl
 65  L14:   RETURN (0)   1
