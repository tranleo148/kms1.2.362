package tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime {
  public static String getAllCurrentTime() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
  }
  
  public static int getYear() {
    return LocalDateTime.now().getYear();
  }
  
  public static int getMonth() {
    return LocalDateTime.now().getMonth().getValue();
  }
  
  public static int getDate() {
    return LocalDateTime.now().getDayOfMonth();
  }
  
  public static int getDay() {
    return LocalDateTime.now().getDayOfWeek().getValue();
  }
  
  public static int getHour() {
    return LocalDateTime.now().getHour();
  }
  
  public static int getMinute() {
    return LocalDateTime.now().getMinute();
  }
  
  public static int getSecond() {
    return LocalDateTime.now().getSecond();
  }
}
