## PX转SP、DP、PT

```java

  private int px2sp(int sp) {
         return (int) TypedValue.applyDimension(
                 TypedValue.COMPLEX_UNIT_SP, 
                 sp, 
                 getResources().getDisplayMetrics());
  }
 
```
