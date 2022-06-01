package server;

import constants.ServerConstants;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import tools.Pair;
import tools.Triple;

public class Setting {
  public static void setting() {
    try {
      FileInputStream setting = new FileInputStream("Properties/setMonsterHP.properties");
      Properties setting_ = new Properties();
      setting_.load(setting);
      setting.close();
      ServerConstants.boss.clear();
      String[] mob = setting_.getProperty(toUni("몹코드")).split("\\{");
      if (mob != null)
        for (String s : mob) {
          int monsterid = 0;
          long hp = 0L;
          for (String s2 : s.replaceAll("},", "").replaceAll("}", "").replaceAll(" ", "").split(",")) {
            if (s2.length() > 0)
              if (monsterid == 0) {
                monsterid = Integer.parseInt(s2);
              } else {
                hp = Long.parseLong(s2);
              }  
          } 
          if (monsterid != 0 && hp != 0L)
            ServerConstants.boss.add(new Pair<>(Integer.valueOf(monsterid), Long.valueOf(hp))); 
        }  
      List<Pair<Integer, Long>> list = ServerConstants.boss;
      for (int i = 0; i < list.size(); i++);
      System.out.println(ServerConstants.boss.size() + "개의 몬스터 체력 로딩완료.");
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  public static void setting2() {
    try {
      FileInputStream setting = new FileInputStream("Properties/setMonsterHP2.properties");
      Properties setting_ = new Properties();
      setting_.load(setting);
      setting.close();
      ServerConstants.boss2.clear();
      String[] mob = setting_.getProperty(toUni("몹코드")).split("\\{");
      if (mob != null)
        for (String s : mob) {
          int monsterid = 0;
          long hp = 0L;
          for (String s2 : s.replaceAll("},", "").replaceAll("}", "").replaceAll(" ", "").split(",")) {
            if (s2.length() > 0)
              if (monsterid == 0) {
                monsterid = Integer.parseInt(s2);
              } else {
                hp = Long.parseLong(s2);
              }  
          } 
          if (monsterid != 0 && hp != 0L)
            ServerConstants.boss2.add(new Pair<>(Integer.valueOf(monsterid), Long.valueOf(hp))); 
        }  
      List<Pair<Integer, Long>> list = ServerConstants.boss2;
      for (int i = 0; i < list.size(); i++);
      System.out.println(ServerConstants.boss2.size() + "개의 몬스터 체력 로딩완료.");
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  public static void CashShopSetting() {
    try {
      FileInputStream setting = new FileInputStream("Properties/CashShopMain.properties");
      Properties setting_ = new Properties();
      setting_.load(setting);
      setting.close();
      ServerConstants.CashMainInfo.clear();
      String[] mob = setting_.getProperty(toUni("Info")).split("\\{");
      if (mob != null)
        for (String s : mob) {
          int monsterid = 0;
          int hp = 0;
          for (String s2 : s.replaceAll("},", "").replaceAll("}", "").replaceAll(" ", "").split(",")) {
            if (s2.length() > 0)
              if (monsterid == 0) {
                monsterid = Integer.parseInt(s2);
              } else {
                hp = Integer.parseInt(s2);
              }  
          } 
          if (monsterid != 0 && hp != 0)
            ServerConstants.CashMainInfo.add(new Pair<>(Integer.valueOf(monsterid), Integer.valueOf(hp))); 
        }  
      System.out.println(ServerConstants.CashMainInfo.size() + "개의 캐시샵 정보 로딩 완료.");
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  public static void settingGoldApple() {
    try {
      FileInputStream setting = new FileInputStream("Properties/골드애플.properties");
      Properties setting_ = new Properties();
      setting_.load(setting);
      setting.close();
      ServerConstants.goldapple.clear();
      ServerConstants.Sgoldapple.clear();
      String[] items = setting_.getProperty(toUni("골드애플_아이템")).split("\\{");
      String[] Sitems = setting_.getProperty(toUni("골드애플_스페셜_아이템")).split("\\{");
      ServerConstants.SgoldappleSuc = Integer.parseInt(setting_.getProperty(toUni("골드애플_스페셜_확률")));
      if (items != null)
        for (String s : items) {
          int itemid = 0, count = 0, suc = 0;
          for (String s2 : s.replaceAll("},", "").replaceAll("}", "").replaceAll(" ", "").split(",")) {
            if (s2.length() > 0)
              if (itemid == 0) {
                itemid = Integer.parseInt(s2);
              } else if (count == 0) {
                count = Integer.parseInt(s2);
              } else if (suc == 0) {
                suc = Integer.parseInt(s2);
              }  
          } 
          if (itemid != 0 && count != 0 && suc != 0)
            ServerConstants.goldapple.add(new Triple<>(Integer.valueOf(itemid), Integer.valueOf(count), Integer.valueOf(suc))); 
        }  
      if (Sitems != null)
        for (String s : Sitems) {
          int itemid = 0, count = 0, suc = 0;
          for (String s2 : s.replaceAll("},", "").replaceAll("}", "").replaceAll(" ", "").split(",")) {
            if (s2.length() > 0)
              if (itemid == 0) {
                itemid = Integer.parseInt(s2);
              } else if (count == 0) {
                count = Integer.parseInt(s2);
              } else if (suc == 0) {
                suc = Integer.parseInt(s2);
              }  
          } 
          if (itemid != 0 && count != 0 && suc != 0)
            ServerConstants.Sgoldapple.add(new Triple<>(Integer.valueOf(itemid), Integer.valueOf(count), Integer.valueOf(suc))); 
        }  
      System.out.println("골드애플 일반 아이템 갯수 : " + ServerConstants.goldapple.size() + "개 로딩 완료!");
      System.out.println("골드애플 스페셜 아이템 갯수 : " + ServerConstants.Sgoldapple.size() + "개 로딩 완료!");
      System.out.println("골드애플 스페셜 아이템 확률 : '" + ServerConstants.SgoldappleSuc + "%' 로딩 완료!");
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  public static void settingNeoPos() {
    try {
      FileInputStream setting = new FileInputStream("Properties/위시코인.properties");
      Properties setting_ = new Properties();
      setting_.load(setting);
      setting.close();
      ServerConstants.NeoPosList.clear();
      String[] info = setting_.getProperty(toUni("몹코드")).split("\\{");
      if (info != null)
        for (String s : info) {
          int monsterid = 0, count = 0;
          for (String s2 : s.replaceAll("},", "").replaceAll("}", "").replaceAll(" ", "").split(",")) {
            if (s2.length() > 0)
              if (monsterid == 0) {
                monsterid = Integer.parseInt(s2);
              } else {
                count = Integer.parseInt(s2);
              }  
          } 
          if (monsterid != 0 && count != 0)
            ServerConstants.NeoPosList.add(new Pair<>(Integer.valueOf(monsterid), Integer.valueOf(count))); 
        }  
      System.out.println("위시코인 리스트 : " + ServerConstants.NeoPosList.size() + "개 로딩 완료!");
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  protected static String toUni(String kor) throws UnsupportedEncodingException {
    return new String(kor.getBytes("KSC5601"), "8859_1");
  }
}
