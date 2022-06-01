package client;

public class HelpTools {
  public static void PrintDebug(String text) {
    boolean isTestMode = false;
    if (isTestMode)
      System.out.println(text); 
  }
  
  public static String CalcComa(short values) {
    return CalcComa(String.valueOf(values));
  }
  
  public static String CalcComa(long values) {
    return CalcComa(String.valueOf(values));
  }
  
  public static String CalcComa(int values) {
    return CalcComa(String.valueOf(values));
  }
  
  public static String CalcComa(String values) {
    String temp = "";
    values = reverseString(values);
    for (int i = 0; i < values.length(); i++) {
      if (i % 3 == 0 && i != 0) {
        temp = temp + ",";
        temp = temp + values.charAt(i);
      } else {
        temp = temp + values.charAt(i);
      } 
    } 
    temp = reverseString(temp);
    return temp;
  }
  
  public static String reverseString(String values) {
    String temp = "";
    for (int i = values.length() - 1; i >= 0; i--)
      temp = temp + values.charAt(i); 
    return temp;
  }
  
  public static String calcTime(long time) {
    String text = "";
    long min = 0L, sec = 0L, hour = 0L, day = 0L, week = 0L, mon = 0L, year = 0L;
    if (time / 60L > 0L) {
      min = time / 60L;
      time -= min * 60L;
    } 
    if (min / 60L > 0L) {
      hour = min / 60L;
      min -= hour * 60L;
    } 
    if (hour / 24L > 0L) {
      day = hour / 24L;
      hour -= day * 24L;
    } 
    if (day / 7L > 0L) {
      week = day / 7L;
      day -= week * 7L;
    } 
    if (week / 4L > 0L) {
      mon = week / 4L;
      week -= mon * 4L;
    } 
    if (mon / 12L > 0L) {
      year = mon / 12L;
      mon -= year * 12L;
    } 
    sec = (int)time;
    if (year > 0L)
      text = text + year + "년 "; 
    if (mon > 0L)
      text = text + mon + "달 "; 
    if (week > 0L)
      text = text + week + "주 "; 
    if (day > 0L)
      text = text + day + "일 "; 
    if (hour > 0L)
      text = text + hour + "시간 "; 
    if (min > 0L)
      text = text + min + "분 "; 
    text = text + sec + "초";
    return text;
  }
}
