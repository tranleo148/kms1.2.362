package server;

import client.DreamBreakerRank;
import client.MapleCharacter;
import client.SkillFactory;
import client.inventory.MapleInventoryIdentifier;
import constants.GameConstants;
import constants.ServerConstants;
import constants.programs.AdminTool;
//import constants.programs.KuronekoServer;
import database.DatabaseConnection;
import ellesia.connector.EllesiaConnectorServer;
import handling.MapleSaveHandler;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.channel.handler.MatrixHandler;
import handling.channel.handler.UnionHandler;
import handling.farm.FarmServer;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.world.World;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import server.control.MapleEtcControl;
import server.events.MapleOxQuizFactory;
import server.field.boss.FieldSkillFactory;
import server.field.boss.lucid.Butterfly;
import server.field.boss.will.SpiderWeb;
import server.life.AffectedOtherSkillInfo;
import server.life.EliteMonsterGradeInfo;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MobAttackInfoFactory;
import server.life.MobSkillFactory;
import server.life.PlayerNPC;
import server.maps.MapleMap;
import server.marriage.MarriageManager;
import server.quest.MapleQuest;
import server.quest.QuestCompleteStatus;
import tools.CMDCommand;
import tools.packet.BossRewardMeso;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class Start {
    
  public static transient ScheduledFuture<?> boss;
  public static long startTime = System.currentTimeMillis();
  
  public static final Start instance = new Start();
  
  public static AtomicInteger CompletedLoadingThreads = new AtomicInteger(0);
  
  public void run() throws InterruptedException {
    DatabaseConnection.init();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("SELECT * FROM auth_server_channel_ip");
        rs = ps.executeQuery();
        while (rs.next())
          ServerProperties.setProperty(rs.getString("name") + rs.getInt("channelid"), rs.getString("value")); 
        rs.close();
        ps.close();
        con.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
        System.exit(0);
      } finally {
        try {
          if (con != null)
            con.close(); 
          if (ps != null)
            ps.close(); 
          if (rs != null)
            rs.close(); 
        } catch (SQLException se) {
          se.printStackTrace();
        } 
      } 
      if (Boolean.parseBoolean(ServerProperties.getProperty("world.admin"))) {
        ServerConstants.Use_Fixed_IV = false;
        System.out.println("[!!! Admin Only Mode Active !!!]");
      } 
      System.setProperty("wz", "wz");
      try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("UPDATE accounts SET loggedin = 0, allowed = 0, connecterClient = null, acIP = null");
        ps.executeUpdate();
        ps.close();
        con.close();
      } catch (SQLException ex) {
        throw new RuntimeException("[EXCEPTION] Please check if the SQL server is active.");
      } finally {
        try {
          if (con != null)
            con.close(); 
          if (ps != null)
            ps.close(); 
          if (rs != null)
            rs.close(); 
        } catch (SQLException se) {
          se.printStackTrace();
        } 
      } 
      World.init();
      Timer.WorldTimer.getInstance().start();
      Timer.EtcTimer.getInstance().start();
      Timer.MapTimer.getInstance().start();
      Timer.MobTimer.getInstance().start();
      Timer.CloneTimer.getInstance().start();
      Timer.EventTimer.getInstance().start();
      Timer.BuffTimer.getInstance().start();
      Timer.PingTimer.getInstance().start();
      Timer.ShowTimer.getInstance().start();
      ServerConstants.WORLD_UI = ServerProperties.getProperty("login.serverUI");
      ServerConstants.ChangeMapUI = Boolean.parseBoolean(ServerProperties.getProperty("login.ChangeMapUI"));
      DreamBreakerRank.LoadRank();
      Butterfly.load();
      SpiderWeb.load();
      Setting.CashShopSetting();
      AllLoding allLoding = new AllLoding();
      allLoding.start();
      System.out.println("[Loading LOGIN]");
      LoginServer.run_startup_configurations();
      System.out.println("[Loading CHANNEL]");
      ChannelServer.startChannel_Main();
      System.out.println("[Loading CASH SHOP]");
      CashShopServer.run_startup_configurations();
      System.out.println("[Loading Farm]");
      FarmServer.run_startup_configurations();
      Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
      PlayerNPC.loadAll();
      LoginServer.setOn();
      Timer.WorldTimer.getInstance().register(new MapleEtcControl(), 1000);
      EliteMonsterGradeInfo.loadFromWZData();
      AffectedOtherSkillInfo.loadFromWZData();
      InnerAbillity.getInstance().load();
            Setting.setting();
            Setting.setting2();
            Setting.settingGoldApple();
            Setting.settingNeoPos();
            BossRewardMeso.Setting();
            Timer.WorldTimer.getInstance().register(new MapleSaveHandler(), 10000L);
            new AdminTool().setVisible(true);
        }
        catch (Exception ex2) {}
    }
  
  private class AllLoding extends Thread {
    private AllLoding() {}
    
    public void run() {
      Start.LoadingThread SkillLoader = new Start.LoadingThread(() -> SkillFactory.load(), "SkillLoader", this);
      Start.LoadingThread QuestLoader = new Start.LoadingThread(() -> {
            MapleQuest.initQuests();
            MapleLifeFactory.loadQuestCounts();
          },"QuestLoader", this);
      Start.LoadingThread QuestCustomLoader = new Start.LoadingThread(() -> {
            MapleLifeFactory.loadNpcScripts();
            QuestCompleteStatus.run();
          },"QuestCustomLoader", this);
      Start.LoadingThread ItemLoader = new Start.LoadingThread(() -> {
            MapleInventoryIdentifier.getInstance();
            CashItemFactory.getInstance().initialize();
            MapleItemInformationProvider.getInstance().runEtc();
            MapleItemInformationProvider.getInstance().runItems();
            AuctionServer.run_startup_configurations();
          },"ItemLoader", this);
      Start.LoadingThread GuildRankingLoader = new Start.LoadingThread(() -> MapleGuildRanking.getInstance().load(), "GuildRankingLoader", this);
      Start.LoadingThread EtcLoader = new Start.LoadingThread(() -> {
            LoginInformationProvider.getInstance();
            RandomRewards.load();
            MapleOxQuizFactory.getInstance();
            UnionHandler.loadUnion();
          },"EtcLoader", this);
      Start.LoadingThread MonsterLoader = new Start.LoadingThread(() -> {
            MobSkillFactory.getInstance();
            FieldSkillFactory.getInstance();
            MobAttackInfoFactory.getInstance();
          },"MonsterLoader", this);
      Start.LoadingThread EmoticonLoader = new Start.LoadingThread(() -> ChatEmoticon.LoadEmoticon(), "EmoticonLoader", this);
      Start.LoadingThread MatrixLoader = new Start.LoadingThread(() -> MatrixHandler.loadCore(), "MatrixLoader", this);
      Start.LoadingThread MarriageLoader = new Start.LoadingThread(() -> MarriageManager.getInstance(), "MarriageLoader", this);
      Start.LoadingThread[] LoadingThreads = { SkillLoader, QuestLoader, QuestCustomLoader, ItemLoader, GuildRankingLoader, EtcLoader, MonsterLoader, MatrixLoader, MarriageLoader, EmoticonLoader };
      for (Thread t : LoadingThreads)
        t.start(); 
      synchronized (this) {
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } 
      } 
      while (Start.CompletedLoadingThreads.get() != LoadingThreads.length) {
        synchronized (this) {
          try {
            wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          } 
        } 
      } 
      World.Guild.load();
      timeBossHottime();
      AutoGame();
       try {
           // new EllesiaConnectorServer().run_startup_configurations();
        } catch (Exception e) {
            e.printStackTrace();
        }
      GameConstants.isOpen = true;
      System.out.println("[Fully Initialized in " + ((System.currentTimeMillis() - Start.startTime) / 1000L) + " seconds]");
      if (!ServerConstants.ConnectorSetting)
        CMDCommand.main(); 
    }
  }
  
  private static class LoadingThread extends Thread {
    protected String LoadingThreadName;
    
    private LoadingThread(Runnable r, String t, Object o) {
      super(new Start.NotifyingRunnable(r, o, t));
      this.LoadingThreadName = t;
    }
    
    public synchronized void start() {
      System.out.println("[Loading...] Started " + this.LoadingThreadName + " Thread");
      super.start();
    }
  }
  
  private static class NotifyingRunnable implements Runnable {
    private String LoadingThreadName;
    
    private long StartTime;
    
    private Runnable WrappedRunnable;
    
    private final Object ToNotify;
    
    private NotifyingRunnable(Runnable r, Object o, String name) {
      this.WrappedRunnable = r;
      this.ToNotify = o;
      this.LoadingThreadName = name;
    }
    
    public void run() {
      this.StartTime = System.currentTimeMillis();
      this.WrappedRunnable.run();
      System.out.println("[Loading Completed] " + this.LoadingThreadName + " | Completed in " + (System.currentTimeMillis() - this.StartTime) + " Milliseconds. (" + (Start.CompletedLoadingThreads.get() + 1) + "/10)");
      synchronized (this.ToNotify) {
        Start.CompletedLoadingThreads.incrementAndGet();
        this.ToNotify.notify();
      } 
    }
  }
  
  public static class Shutdown implements Runnable {
    public void run() {
      ShutdownServer.getInstance().run();
    }
  }
  
  public static void main(String[] args) throws InterruptedException {
    instance.run();
  }
  
      public static void AutoGame() { //Maked By 키네시스, 라피스 (네이트온: Kinesis8@nate.com , 디스코드 : 라피스#2519)
        Timer.WorldTimer tMan = Timer.WorldTimer.getInstance();
        Runnable r = new Runnable() {
            public void run() {
                if (new Date().getMinutes() % 2 == 1) {
                    //날짜함수
                    Date time = new Date();
                    String Year = (time.getYear() + 1900) + "";
                    String Month = ((time.getMonth() + 1) < 10) ? ("0" + (time.getMonth() + 1)) : ((time.getMonth() + 1) + "");
                    String Day = (time.getDate() < 10) ? ("0" + time.getDate()) : (time.getDate() + "");
                    int Today = Integer.parseInt(Year + "" + Month + "" + Day);
                    String Hour = (time.getHours() < 10) ? ("0" + time.getHours()) : (time.getHours() + "");
                    String Minute = (time.getMinutes() < 10) ? ("0" + time.getMinutes()) : (time.getMinutes() + "");
                    String HM = Hour + "" + Minute;
                    String holjjak = (Randomizer.rand(1, 2) == 1) ? "홀" : "짝";
                    String leftright = (Randomizer.rand(1, 2) == 1) ? "좌" : "우";
                    String threefour = (Randomizer.rand(1, 2) == 1) ? "3" : "4";
                    int count = 0;
                    //SQL 쿼리문 처리
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    try {
                        //회차 확인하기
                        con = DatabaseConnection.getConnection();
                        ps = con.prepareStatement("SELECT * FROM `bettingresult` WHERE `date` = ?");
                        ps.setInt(1, Today);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            count++;
                            if (rs.getInt("count") != count) {
                                System.out.println("오늘의 회차값 정보가 누락되거나 일치하지 않습니다. 쿼리를 확인해주세요.");
                                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(2, "","[시스템] : 자동게임 시스템에 문제가 있을 수 있으니 상태를 점검해 주세요."));
                            }
                        }
                        rs.close();
                        ps.close();
                        //새로운 회차 정보 등록하기
                        count++;
                        ps = con.prepareStatement("INSERT INTO `bettingresult` (`date`, `time`, `count`, `holjjack`, `leftright`, `threefour`) VALUES (?, ?, ?, ?, ?, ?)");
                        ps.setInt(1, Today);
                        ps.setString(2, HM);
                        ps.setInt(3, count);
                        ps.setString(4, holjjak);
                        ps.setString(5, leftright);
                        ps.setString(6, threefour);
                        ps.executeUpdate();
                        ps.close();
                        con.close();
                        //zWorld.Broadcast.broadcastMessage(CWvsContext.serverNotice(2, "[시스템] : " + count + "회 홀짝 추첨 결과는 [" + holjjak + "] 입니다."));
                        World.Broadcast.broadcastMessage(CWvsContext.serverNotice(2,"", "[시스템] : " + count + "회 사다리 추첨 결과는 [" + leftright + "" + threefour + "" + holjjak + "] 입니다."));
                    } catch (Exception 오류) {
                        System.out.println("자동게임 시스템에 오류가 발생했습니다.");
                        System.out.println(오류);
                    } finally {
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (Exception e) {
                                System.out.println("자동게임 시스템 rs 커넥션 오류가 발생했습니다.");
                                System.out.println(e);
                            }
                        }
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (Exception e) {
                                System.out.println("자동게임 시스템 ps 커넥션 오류가 발생했습니다.");
                                System.out.println(e);
                            }
                        }
                        if (con != null) {
                            try {
                                con.close();
                            } catch (Exception e) {
                                System.out.println("자동게임 시스템 con 커넥션 오류가 발생했습니다.");
                                System.out.println(e);
                            }
                        }
                    }
                }
            }
        };
        tMan.register(r, 60000); //건드리지 말것
        System.out.println("[알림] 자동게임 시스템이 활성화 되었습니다.");
    }
  
  
     public static void timeBossHottime() {
        final int[] hour = ServerConstants.hour;
        final int pice = ServerConstants.pice;

        if (boss == null) {
            Calendar cal = Calendar.getInstance();
            long time = cal.getTimeInMillis();
            long schedulewait = 0;
            if (time > System.currentTimeMillis()) {
                schedulewait = time - System.currentTimeMillis();
            } else {
                while (true) {
                    cal.add(Calendar.SECOND, 1);
                    for (int ho : hour) {
                        if (cal.getTimeInMillis() > System.currentTimeMillis() && cal.getTime().getHours() == (ho - 1) && cal.getTime().getMinutes() >= 50 && cal.getTime().getSeconds() == 0) {
                            schedulewait = cal.getTimeInMillis() - System.currentTimeMillis();
                            break;
                        }
                    }
                    if (schedulewait > 0) {
                        break;
                    }
                }
            }

            boss = Timer.WorldTimer.getInstance().register(new Runnable() {
                public void run() {
                    Date nowtime = new Date();
                    for (int ho : hour) {
                        if (ho <= 0) {
                            ho = 24;
                        }
                        if (nowtime.getMinutes() >= 50 && nowtime.getMinutes() <= 59 && nowtime.getHours() == (ho - 1)) {
                            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                                for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values()) {
                                    chr.sethottimeboss(true);
                                }
                            }
                            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6,"", (60 - nowtime.getMinutes()) + "분후 월드 보스가 시작됩니다. 월드 보스 NPC 를 통해 어서 입장하세요."));
                        } else if (nowtime.getMinutes() == 0 && nowtime.getHours() == (ho == 24 ? 0 : ho)) {
                            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                                if (ch.getChannel() == 1) {
                                    MapleMonster mob = MapleLifeFactory.getMonster(ServerConstants.worldboss);
                                    ch.getMapFactory().getMap(ServerConstants.worldbossmap).spawnMonsterOnGroundBelow(mob, new Point(-161, -181));
                                }
                                if (ch.getChannel() == 2) {
                                    MapleMonster mob = MapleLifeFactory.getMonster(ServerConstants.worldboss);
                                    ch.getMapFactory().getMap(ServerConstants.worldbossmap).spawnMonsterOnGroundBelow(mob, new Point(-161, -181));
                                }
                                if (ch.getChannel() == 3) {
                                    MapleMonster mob = MapleLifeFactory.getMonster(ServerConstants.worldboss);
                                    ch.getMapFactory().getMap(ServerConstants.worldbossmap).spawnMonsterOnGroundBelow(mob, new Point(-161, -181));
                                }
                                if (ch.getChannel() == 4) {
                                    MapleMonster mob = MapleLifeFactory.getMonster(ServerConstants.worldboss);
                                    ch.getMapFactory().getMap(ServerConstants.worldbossmap).spawnMonsterOnGroundBelow(mob, new Point(-161, -181));
                                }
                                for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values()) {
                                    chr.sethottimeboss(false);
                                    chr.sethottimebossattackcheck(false);
                                    chr.sethottimebosslastattack(false);
                                }
                            }
                            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "[Happy]월드 보스가 시작되었습니다. 마지막일격을 가해 추가아이템을 획득해보세요!"));
                        } else if (nowtime.getMinutes() == 30 && nowtime.getHours() == (ho == 24 ? 0 : ho)) {
                            MapleMap map = ChannelServer.getInstance(1).getMapFactory().getMap(ServerConstants.worldbossmap);
                            if (map != null) {
                                for (MapleCharacter chr : map.getCharactersThreadsafe()) {
                                    map.resetFully();
                                    chr.dropMessage(6, "월드 보스가 종료 됐습니다.");
                                    if (ServerConstants.worldbossmap == chr.getMapId()) {
                                        chr.sethottimeboss(false);
                                        chr.getClient().getSession().writeAndFlush(CField.sendDuey((byte) 28, null, null));
                                        //DueyHandler.addNewItemToDb(failitem, pice, chr.getId(), "[실패]", "월드보스 보상이 지급 됐습니다.", true);
                                    }
                                }
                            }
                            ChannelServer.getInstance(1).getMapFactory().getMap(ServerConstants.worldbossmap).resetNPCs();
                        }
                    }
                }
            }, 1000 * 60, schedulewait);
        }
    }
}
