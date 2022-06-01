package server;

public enum WeekendMaple {
  STARFORCE_DISCOUNT(0, 0, "주문의 흔적 상시 버닝!"),
  AMAZINGSCROLL_ADD(0, 1, "소울 조각 사용 시 위대한 소울 획득 확률 5배!"),
  STARFORCE_ADDPROB(1, 0, "주문의 흔적 상시 버닝!"),
  SOUL_ADDPROB(1, 1, "소울 조각 사용 시 위대한 소울 획득 확률 5배!");
  
  private final int week;
  
  private final int date;
  
  private String event;
  
  WeekendMaple(int week, int date, String event) {
    this.week = week;
    this.date = date;
    this.event = event;
  }
  
  public static boolean hasEvent(WeekendMaple event, int week) {
    if (event.week == week)
      return true; 
    return false;
  }
  
  public static boolean hasEvent(WeekendMaple event, int week, int date) {
    if (event.week == week && event.date == date)
      return true; 
    return false;
  }
  
  public int getWeek() {
    return this.week;
  }
  
  public int getDate() {
    return this.date;
  }
  
  public String getEvent() {
    return this.event;
  }
}
