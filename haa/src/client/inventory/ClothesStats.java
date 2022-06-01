package client.inventory;

public enum ClothesStats {
  a(1, 3),
  b(2, 1),
  c(4, 2),
  d(8, 4),
  e(16, 9),
  f(32, 5),
  g(64, 8),
  h(128, 11),
  i(256, 6),
  j(512, 7),
  k(1024, 12),
  l(2048, 13);
  
  private final int value;
  
  private final int order;
  
  ClothesStats(int value, int order) {
    this.value = value;
    this.order = order;
  }
  
  public static int getValueByOrder(int order) {
    for (ClothesStats cs : values()) {
      if (cs.order == order)
        return cs.value; 
    } 
    return 0;
  }
}
