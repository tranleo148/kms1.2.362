package tools;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
  public static <T> List<T> copyFirst(List<T> list, int count) {
    List<T> ret = new ArrayList<>((list.size() < count) ? list.size() : count);
    int i = 0;
    for (T elem : list) {
      ret.add(elem);
      if (i++ > count)
        break; 
    } 
    return ret;
  }
}
