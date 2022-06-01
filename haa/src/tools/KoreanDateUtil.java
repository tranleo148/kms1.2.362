package tools;

import java.util.Date;
import java.util.SimpleTimeZone;

public class KoreanDateUtil {
  private static final int ITEM_YEAR2000 = -1085019342;
  
  private static final long REAL_YEAR2000 = 946681229830L;
  
  public static final long getTempBanTimestamp(long realTimestamp) {
    return realTimestamp * 10000L + 116445060000000000L;
  }
  
  public static final int getItemTimestamp(long realTimestamp) {
    int time = (int)((realTimestamp - 946681229830L) / 1000L / 60L);
    return (int)(time * 35.762787D) + -1085019342;
  }
  
  public static boolean isDST() {
    return SimpleTimeZone.getDefault().inDaylightTime(new Date());
  }
  
  public static long getFileTimestamp(long timeStampinMillis, boolean roundToMinutes) {
    long time;
    if (isDST())
      timeStampinMillis -= 3600000L; 
    if (roundToMinutes) {
      time = timeStampinMillis / 1000L / 60L * 600000000L;
    } else {
      time = timeStampinMillis * 10000L;
    } 
    return time + 116445060000000000L;
  }
}
