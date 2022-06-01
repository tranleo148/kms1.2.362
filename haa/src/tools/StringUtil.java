package tools;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class StringUtil {
  private static Charset ASCII = Charset.forName("MS949");
  
  public static String getLeftPaddedStr(String in, char padchar, int length) {
    StringBuilder builder = new StringBuilder(length);
    for (int x = (in.getBytes(ASCII)).length; x < length; x++)
      builder.append(padchar); 
    builder.append(in);
    return builder.toString();
  }
  
  public static String getRightPaddedStr(String in, char padchar, int length) {
    StringBuilder builder = new StringBuilder(in);
    for (int x = (in.getBytes(ASCII)).length; x < length; x++)
      builder.append(padchar); 
    return builder.toString();
  }
  
  public static String joinStringFrom(String[] arr, int start) {
    return joinStringFrom(arr, start, " ");
  }
  
  public static String joinStringFrom(String[] arr, int start, String sep) {
    StringBuilder builder = new StringBuilder();
    for (int i = start; i < arr.length; i++) {
      builder.append(arr[i]);
      if (i != arr.length - 1)
        builder.append(sep); 
    } 
    return builder.toString();
  }
  
  public static String makeEnumHumanReadable(String enumName) {
    StringBuilder builder = new StringBuilder(enumName.length() + 1);
    for (String word : enumName.split("_")) {
      if (word.length() <= 2) {
        builder.append(word);
      } else {
        builder.append(word.charAt(0));
        builder.append(word.substring(1).toLowerCase());
      } 
      builder.append(' ');
    } 
    return builder.substring(0, enumName.length());
  }
  
  public static int countCharacters(String str, char chr) {
    int ret = 0;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == chr)
        ret++; 
    } 
    return ret;
  }
  
  public static String getReadableMillis(long startMillis, long endMillis) {
    StringBuilder sb = new StringBuilder();
    double elapsedSeconds = (endMillis - startMillis) / 1000.0D;
    int elapsedSecs = (int)elapsedSeconds % 60;
    int elapsedMinutes = (int)(elapsedSeconds / 60.0D);
    int elapsedMins = elapsedMinutes % 60;
    int elapsedHrs = elapsedMinutes / 60;
    int elapsedHours = elapsedHrs % 24;
    int elapsedDays = elapsedHrs / 24;
    if (elapsedDays > 0) {
      boolean mins = (elapsedHours > 0);
      sb.append(elapsedDays);
      sb.append(" day").append((elapsedDays > 1) ? "s" : "").append(mins ? ", " : ".");
      if (mins) {
        boolean secs = (elapsedMins > 0);
        if (!secs)
          sb.append("and "); 
        sb.append(elapsedHours);
        sb.append(" hour").append((elapsedHours > 1) ? "s" : "").append(secs ? ", " : ".");
        if (secs) {
          boolean millis = (elapsedSecs > 0);
          if (!millis)
            sb.append("and "); 
          sb.append(elapsedMins);
          sb.append(" minute").append((elapsedMins > 1) ? "s" : "").append(millis ? ", " : ".");
          if (millis) {
            sb.append("and ");
            sb.append(elapsedSecs);
            sb.append(" second").append((elapsedSecs > 1) ? "s" : "").append(".");
          } 
        } 
      } 
    } else if (elapsedHours > 0) {
      boolean mins = (elapsedMins > 0);
      sb.append(elapsedHours);
      sb.append(" hour").append((elapsedHours > 1) ? "s" : "").append(mins ? ", " : ".");
      if (mins) {
        boolean secs = (elapsedSecs > 0);
        if (!secs)
          sb.append("and "); 
        sb.append(elapsedMins);
        sb.append(" minute").append((elapsedMins > 1) ? "s" : "").append(secs ? ", " : ".");
        if (secs) {
          sb.append("and ");
          sb.append(elapsedSecs);
          sb.append(" second").append((elapsedSecs > 1) ? "s" : "").append(".");
        } 
      } 
    } else if (elapsedMinutes > 0) {
      boolean secs = (elapsedSecs > 0);
      sb.append(elapsedMinutes);
      sb.append(" minute").append((elapsedMinutes > 1) ? "s" : "").append(secs ? " " : ".");
      if (secs) {
        sb.append("and ");
        sb.append(elapsedSecs);
        sb.append(" second").append((elapsedSecs > 1) ? "s" : "").append(".");
      } 
    } else if (elapsedSeconds > 0.0D) {
      sb.append((int)elapsedSeconds);
      sb.append(" second").append((elapsedSeconds > 1.0D) ? "s" : "").append(".");
    } else {
      sb.append("None.");
    } 
    return sb.toString();
  }
  
  public static int getDaysAmount(long startMillis, long endMillis) {
    double elapsedSeconds = (endMillis - startMillis) / 1000.0D;
    int elapsedMinutes = (int)(elapsedSeconds / 60.0D);
    int elapsedHrs = elapsedMinutes / 60;
    int elapsedDays = elapsedHrs / 24;
    return elapsedDays;
  }
  
  public static int getOptionalIntArg(String[] splitted, int position, int def) {
    if (splitted.length > position)
      try {
        return Integer.parseInt(splitted[position]);
      } catch (NumberFormatException nfe) {
        return def;
      }  
    return def;
  }
  
  public static boolean isNumber(String s) {
    for (char c : s.toCharArray()) {
      if (!Character.isDigit(c))
        return false; 
    } 
    return true;
  }
  
  public static String getAllCurrentTime() {
    Calendar calz = Calendar.getInstance(TimeZone.getTimeZone("KST"), Locale.KOREAN);
    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String time = simpleTimeFormat.format(calz.getTime());
    return time;
  }
}
