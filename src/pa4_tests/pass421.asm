  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        2
  5         CALL         newarr  
  6         LOADL        -1
  7         LOADL        1
  8         CALL         newobj  
  9         LOAD         4[LB]
 10         LOADL        0
 11         LOADL        3
 12         CALL         fieldupd
 13         LOAD         3[LB]
 14         LOADL        0
 15         LOAD         4[LB]
 16         CALL         arrayupd
 17         LOADL        -1
 18         LOADL        1
 19         CALL         newobj  
 20         STORE        5[LB]
 21         LOAD         5[LB]
 22         LOADL        0
 23         LOADL        5
 24         CALL         fieldupd
 25         LOAD         3[LB]
 26         LOADL        1
 27         LOAD         5[LB]
 28         CALL         arrayupd
 29         LOAD         3[LB]
 30         LOADL        0
 31         CALL         arrayref
 32         STORE        6[LB]
 33         LOAD         3[LB]
 34         LOADL        1
 35         CALL         arrayref
 36         STORE        7[LB]
 37         LOAD         6[LB]
 38         LOADL        0
 39         CALL         fieldref
 40         LOAD         7[LB]
 41         LOADL        0
 42         CALL         fieldref
 43         CALL         add     
 44         LOADL        13
 45         CALL         add     
 46         STORE        8[LB]
 47         LOAD         8[LB]
 48         CALL         putintnl
 49         RETURN (0)   1
 50         LOADL        0
 51  L10:   LOADL        2
 52         CALL         newarr  
 53         LOADL        -1
 54         LOADL        1
 55         CALL         newobj  
 56         LOAD         4[LB]
 57         LOADL        0
 58         LOADL        3
 59         CALL         fieldupd
 60         LOAD         3[LB]
 61         LOADL        0
 62         LOAD         4[LB]
 63         CALL         arrayupd
 64         LOADL        -1
 65         LOADL        1
 66         CALL         newobj  
 67         STORE        5[LB]
 68         LOAD         5[LB]
 69         LOADL        0
 70         LOADL        5
 71         CALL         fieldupd
 72         LOAD         3[LB]
 73         LOADL        1
 74         LOAD         5[LB]
 75         CALL         arrayupd
 76         LOAD         3[LB]
 77         LOADL        0
 78         CALL         arrayref
 79         STORE        6[LB]
 80         LOAD         3[LB]
 81         LOADL        1
 82         CALL         arrayref
 83         STORE        7[LB]
 84         LOAD         6[LB]
 85         LOADL        0
 86         CALL         fieldref
 87         LOAD         7[LB]
 88         LOADL        0
 89         CALL         fieldref
 90         CALL         add     
 91         LOADL        13
 92         CALL         add     
 93         STORE        8[LB]
 94         LOAD         8[LB]
 95         CALL         putintnl
 96         RETURN (0)   1
