  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4         LOADL        8
  5         LOAD         3[LB]
  6         CALL         newarr  
  7         LOAD         4[LB]
  8         CALL         arraylen
  9         LOAD         5[LB]
 10         CALL         putintnl
 11         RETURN (0)   1
 12  L10:   LOADL        8
 13         LOAD         3[LB]
 14         CALL         newarr  
 15         LOAD         4[LB]
 16         CALL         arraylen
 17         LOAD         5[LB]
 18         CALL         putintnl
 19         RETURN (0)   1
