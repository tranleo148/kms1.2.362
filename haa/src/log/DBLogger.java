package log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import tools.FileoutputUtil;

public class DBLogger {
  private static final DBLogger instance = new DBLogger();
  
  public static DBLogger getInstance() {
    return instance;
  }
  
  private String escape(String input) {
    return input.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
  }
  
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  private static final SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd");
  
  public static String CurrentReadable_Time() {
    return sdf.format(Calendar.getInstance().getTime());
  }
  
  public void logChat(LogType.Chat type, int cid, String charname, String message, String etc) {
    FileoutputUtil.log(FileoutputUtil.채팅로그, "[" + CurrentReadable_Time() + "] 캐릭터ID : " + cid + "  /  닉네임 : " + escape(charname) + "  /  메세지 : " + escape(message) + "  /  맵 : " + escape(etc) + "");
  }
}
