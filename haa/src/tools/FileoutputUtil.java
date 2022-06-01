package tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FileoutputUtil {
  public static final String Acc_Stuck = "Log_AccountStuck.rtf";
  
  public static final String Login_Error = "Log_Login_Error.rtf";
  
  public static final String Pinkbean_Log = "Log_Pinkbean.rtf";
  
  public static final String Cooldown_Log = "Log_Cooldown.rtf";
  
  public static final String ScriptEx_Log = "Log_Script_Except.rtf";
  
  public static final String PacketEx_Log = "Log_Packet_Except.rtf";
  
  public static final String Donator_Log = "Log_Donator.rtf";
  
  public static final String ForceAtom_Log = "Log_ForceAtom.rtf";
  
  public static final String Buff_Error_Log = "Log_BuffError.rtf";
  
  public static final String Hacker_Log = "Log_Hacker.rtf";
  
  public static final String 프로세스로그 = "LogFile/프로세스로그/" + getDCurrentTime() + ".txt";
  
  public static final String 커넥터로그 = "LogFile/커넥터로그/" + getDCurrentTime() + ".txt";
  
  public static final String 감지로그 = "LogFile/감지로그/" + getDCurrentTime() + ".txt";
  
  public static final String 채팅로그 = "LogFile/채팅로그/" + getDCurrentTime() + ".txt";
  
  public static final String 타이머로그 = "LogFile/타이머로그/" + getDCurrentTime() + ".txt";
  
  public static final String 진실의방로그 = "LogFile/버그사용로그/진실의방/" + getDCurrentTime() + ".txt";
  
  public static final String Attack_Dealy = "LogFile/버그사용로그/딜레이/" + getDCurrentTime() + ".txt";
  
  public static final String Movement_Log = "Log_Movement.rtf";
  
  public static final String CommandEx_Log = "Log_Command_Except.rtf";
  
  public static final String Attack_Log = "Log_Attack.txt";
  
  public static final String Kill_Log = "Log_Kill.txt";
  
  public static final String 경매장구매로그 = "LogFile/경매장/구매로그/" + getDCurrentTime() + ".txt";
  
  public static final String 경매장대금수령로그 = "LogFile/경매장/대금수령로그/" + getDCurrentTime() + ".txt";
  
  public static final String 경매장물품반환로그 = "LogFile/경매장/물품반환로그/" + getDCurrentTime() + ".txt";
  
  public static final String 경매장입장로그 = "LogFile/경매장/입장로그/" + getDCurrentTime() + ".txt";
  
  public static final String 경매장퇴장로그 = "LogFile/경매장/퇴장로그/" + getDCurrentTime() + ".txt";
  
  public static final String 경매장판매등록로그 = "LogFile/경매장/판매등록로그/" + getDCurrentTime() + ".txt";
  
  public static final String 경매장판매중지로그 = "LogFile/경매장/판매중지로그/" + getDCurrentTime() + ".txt";
  
  public static final String 경매장검색로그 = "LogFile/경매장/검색로그/" + getDCurrentTime() + ".txt";
  
  public static final String 계정로그인로그 = "LogFile/계정/로그인" + getDCurrentTime() + ".txt";
  
  public static final String 교환메소로그 = "LogFile/교환/메소" + getDCurrentTime() + ".txt";
  
  public static final String 교환로그 = "LogFile/교환/아이템" + getDCurrentTime() + ".txt";
  
  public static final String 보스입장 = "LogFile/보스/입장" + getDCurrentTime() + ".txt";
  
  public static final String 보스클리어 = "LogFile/보스/클리어" + getDCurrentTime() + ".txt";
  
  public static final String 보스획득아이템 = "LogFile/보스/획득아이템" + getDCurrentTime() + ".txt";
  
  public static final String 캐시샵입장로그 = "LogFile/캐시샵/입장로그/" + getDCurrentTime() + ".txt";
  
  public static final String 캐시샵퇴장로그 = "LogFile/캐시샵/퇴장로그/" + getDCurrentTime() + ".txt";
  
  public static final String 캐시샵구매로그 = "LogFile/캐시샵/구매로그/" + getDCurrentTime() + ".txt";
  
  public static final String 캐시샵인벤로그 = "LogFile/캐시샵/인벤로그/" + getDCurrentTime() + ".txt";
  
  public static final String 아이템드롭로그 = "LogFile/아이템/아이템드롭로그/" + getDCurrentTime() + ".txt";
  
  public static final String 아이템사용로그 = "LogFile/아이템/아이템사용로그/" + getDCurrentTime() + ".txt";
  
  public static final String 스크립트아이템사용로그 = "LogFile/아이템/스크립트아이템/아이템사용/" + getDCurrentTime() + ".txt";
  
  public static final String 아이템획득로그 = "LogFile/아이템/아이템획득로그/" + getDCurrentTime() + ".txt";
  
  public static final String 엔피시대화로그 = "LogFile/엔피시/대화/" + getDCurrentTime() + ".txt";
  
  public static final String 엔피시상점로그 = "LogFile/엔피시/상점/" + getDCurrentTime() + ".txt";
  
  public static final String 엔피시상점구매로그 = "LogFile/엔피시/상점구매/" + getDCurrentTime() + ".txt";
  
  public static final String 엔피시상점판매로그 = "LogFile/엔피시/상점판매/" + getDCurrentTime() + ".txt";
  
  public static final String 전체채팅로그 = "LogFile/채팅/전체채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 확성기채팅로그 = "LogFile/채팅/확성기채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 일반채팅로그 = "LogFile/채팅/일반채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 친구채팅로그 = "LogFile/채팅/친구채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 길드채팅로그 = "LogFile/채팅/길드채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 파티채팅로그 = "LogFile/채팅/파티채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 귓속말채팅로그 = "LogFile/채팅/귓속말채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 연합채팅로그 = "LogFile/채팅/연합채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 메신저채팅로그 = "LogFile/채팅/메신저채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 교환채팅로그 = "LogFile/채팅/교환채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 미니게임채팅로그 = "LogFile/채팅/미니게임채팅/" + getDCurrentTime() + ".txt";
  
  public static final String 레벨업로그 = "LogFile/캐릭터/레벨업/" + getDCurrentTime() + ".txt";
  
  public static final String 접속로그 = "LogFile/캐릭터/접속/" + getDCurrentTime() + ".txt";
  
  public static final String 접속종료로그 = "LogFile/캐릭터/접속종료/" + getDCurrentTime() + ".txt";
  
  public static final String 명령어로그 = "LogFile/명령어/" + getDCurrentTime() + ".txt";
  
  public static final String 창고입고로그 = "LogFile/창고/입고/" + getDCurrentTime() + ".txt";
  
  public static final String 창고퇴고로그 = "LogFile/창고/퇴고/" + getDCurrentTime() + ".txt";
  
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  private static final SimpleDateFormat sdfGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  private static final SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd");
  
  static {
    sdfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
  }
  
  public static String getDCurrentTime() {
    Calendar calz = Calendar.getInstance(TimeZone.getTimeZone("KST"), Locale.KOREAN);
    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
    String time = simpleTimeFormat.format(calz.getTime());
    return time;
  }
  
  public static void log(String file, String msg) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file, true);
      out.write(("\n------------------------ " + CurrentReadable_Time() + " ------------------------\n\r\n").getBytes());
      out.write(msg.getBytes());
    } catch (IOException iOException) {
      try {
        if (out != null)
          out.close(); 
      } catch (IOException iOException1) {}
    } finally {
      try {
        if (out != null)
          out.close(); 
      } catch (IOException iOException) {}
    } 
  }
  
  public static void outputFileError(String file, Throwable t) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file, true);
      out.write(("\n------------------------ " + CurrentReadable_Time() + " ------------------------\n").getBytes());
      out.write(getString(t).getBytes());
    } catch (IOException iOException) {
      try {
        if (out != null)
          out.close(); 
      } catch (IOException iOException1) {}
    } finally {
      try {
        if (out != null)
          out.close(); 
      } catch (IOException iOException) {}
    } 
  }
  
  public static String CurrentReadable_Date() {
    return sdf_.format(Calendar.getInstance().getTime());
  }
  
  public static String CurrentReadable_Time() {
    return sdf.format(Calendar.getInstance().getTime());
  }
  
  public static String CurrentReadable_TimeGMT() {
    return sdfGMT.format(new Date());
  }
  
  public static String getString(Throwable e) {
    String retValue = null;
    StringWriter sw = null;
    PrintWriter pw = null;
    try {
      sw = new StringWriter();
      pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      retValue = sw.toString();
    } finally {
      try {
        if (pw != null)
          pw.close(); 
        if (sw != null)
          sw.close(); 
      } catch (IOException iOException) {}
    } 
    return retValue;
  }
}
