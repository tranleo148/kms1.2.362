package client.messages.commands;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.SkillFactory;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import constants.KoreaCalendar;
import constants.ServerConstants;
import handling.auction.AuctionServer;
import handling.channel.ChannelServer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import server.ShutdownServer;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import tools.CPUSampler;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class AdminCommand {
  public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
    return ServerConstants.PlayerGMRank.ADMIN;
  }
  
  public static class 버프테스트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int rate = Integer.parseInt(splitted[1]);
      c.getPlayer().setSkillBuffTest(rate);
      c.getPlayer().dropMessage(6, "[설정완료] 현재 버프 코드 : " + c.getPlayer().getSkillBuffTest());
      return 1;
    }
  }
  
  public static class MesoEveryone extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters().values())
          mch.gainMeso(Integer.parseInt(splitted[1]), true); 
      } 
      return 1;
    }
  }
  
  public static class ExpRate extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length > 1) {
        int rate = Integer.parseInt(splitted[1]);
        if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
          for (ChannelServer cserv : ChannelServer.getAllInstances())
            cserv.setExpRate(rate); 
        } else {
          c.getChannelServer().setExpRate(rate);
        } 
        c.getPlayer().dropMessage(6, "Exprate has been changed to " + rate + "x");
      } else {
        c.getPlayer().dropMessage(6, "Syntax: !exprate <number> [all]");
      } 
      return 1;
    }
  }
  
  public static class 링크소환 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().setLinkMobCount(100);
      for (int i = 0; i < 10; i++) {
        MapleMonster m = MapleLifeFactory.getMonster(100100);
        m.setHp(m.getStats().getHp());
        m.getStats().setHp(m.getStats().getHp());
        m.setOwner(c.getPlayer().getId());
        c.getPlayer().getMap().spawnMonsterOnGroundBelow(m, c.getPlayer().getTruePosition());
      } 
      return 1;
    }
  }
  
  public static class 경매장저장 extends CommandExecute {
    public int execute(MapleClient c, String[] Splitted) {
      AuctionServer.saveItems();
      c.getPlayer().dropMessage(6, "경매장 데이터를 저장하였습니다.");
      return 1;
    }
  }
  
  public static class MesoRate extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length > 1) {
        int rate = Integer.parseInt(splitted[1]);
        if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
          for (ChannelServer cserv : ChannelServer.getAllInstances())
            cserv.setMesoRate(rate); 
        } else {
          c.getChannelServer().setMesoRate(rate);
        } 
        c.getPlayer().dropMessage(6, "Meso Rate has been changed to " + rate + "x");
      } else {
        c.getPlayer().dropMessage(6, "Syntax: !mesorate <number> [all]");
      } 
      return 1;
    }
  }
  
  public static class DCAll extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int range = -1;
      if (splitted[1].equals("m")) {
        range = 0;
      } else if (splitted[1].equals("c")) {
        range = 1;
      } else if (splitted[1].equals("w")) {
        range = 2;
      } 
      if (range == -1)
        range = 1; 
      if (range == 0) {
        c.getPlayer().getMap().disconnectAll();
      } else if (range == 1) {
        c.getChannelServer().getPlayerStorage().disconnectAll(true);
      } else if (range == 2) {
        for (ChannelServer cserv : ChannelServer.getAllInstances())
          cserv.getPlayerStorage().disconnectAll(true); 
      } 
      return 1;
    }
  }
  
  public static class Shutdown extends CommandExecute {
    protected static Thread t = null;
    
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(6, "Shutting down...");
      if (t == null || !t.isAlive()) {
        t = new Thread(ShutdownServer.getInstance());
        ShutdownServer.getInstance().shutdown();
        t.start();
      } else {
        c.getPlayer().dropMessage(6, "A shutdown thread is already in progress or shutdown has not been done. Please wait.");
      } 
      return 1;
    }
  }
  
  public static class 리붓 extends Shutdown {
    private static ScheduledFuture<?> ts = null;
    
    private int minutesLeft = 0;
    
    public int execute(MapleClient c, String[] splitted) {
      this.minutesLeft = Integer.parseInt(splitted[1]);
      KoreaCalendar kc = new KoreaCalendar();
      int hour = kc.getHour();
      int min = kc.getMin() + this.minutesLeft;
      if (min >= 60) {
        hour++;
        min -= 60;
        if (hour >= 24)
          hour = 0; 
      } 
      String am = (hour >= 12) ? "오후" : "오전";
      String type = (Integer.parseInt(splitted[2]) == 1) ? "패치가" : "점검이";
      for (ChannelServer cserv : ChannelServer.getAllInstances())
        cserv.setServerMessage("안녕하세요, Happy ! 입니다. 잠시 후 " + am + " " + hour + "시 " + min + "분 부터 서버" + type + " 진행됩니다. 원활한 진행을 위해 지금 바로 접속을 종료해주시기 바랍니다. 이용에 불편을 끼쳐 드려 죄송합니다."); 
      if (ts == null && (t == null || !t.isAlive())) {
        t = new Thread(ShutdownServer.getInstance());
        ts = Timer.EventTimer.getInstance().register(new Runnable() {
              public void run() {
                if (AdminCommand.리붓.this.minutesLeft == 0) {
                  ShutdownServer.getInstance().shutdown();
                  AdminCommand.Shutdown.t.start();
                  AdminCommand.리붓.ts.cancel(false);
                  return;
                } 
                AdminCommand.리붓.this.minutesLeft--;
              }
            },  60000L);
      } else {
        c.getPlayer().dropMessage(6, "이미 저장된 타이머가 있습니다.");
      } 
      return 1;
    }
  }
  
  public static class StartProfiling extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      CPUSampler sampler = CPUSampler.getInstance();
      sampler.addIncluded("client");
      sampler.addIncluded("connector");
      sampler.addIncluded("constants");
      sampler.addIncluded("database");
      sampler.addIncluded("handling");
      sampler.addIncluded("log");
      sampler.addIncluded("provider");
      sampler.addIncluded("scripting");
      sampler.addIncluded("server");
      sampler.addIncluded("tools");
      sampler.start();
      c.getPlayer().dropMessageGM(-5, "프로파일링을 시작합니다.");
      return 1;
    }
  }
  
  public static class StopProfiling extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      CPUSampler sampler = CPUSampler.getInstance();
      try {
        String filename = "CPU프로파일링.txt";
        if (splitted.length > 1)
          filename = splitted[1]; 
        File file = new File(filename);
        if (file.exists()) {
          c.getPlayer().dropMessage(6, "이미 존재하는 파일입니다. 삭제나 이름 변경을 해주세요.");
          return 0;
        } 
        sampler.stop();
        FileWriter fw = new FileWriter(file);
        sampler.save(fw, 1, 10);
        fw.close();
        sampler.reset();
        c.getPlayer().dropMessage(6, "파일을 저장했습니다.");
      } catch (IOException e) {
        System.err.println("Error saving profile" + e);
      } 
      return 1;
    }
  }
  
    public static class 맥스스탯 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getStat().setDex((short) 32767, c.getPlayer());
            c.getPlayer().getStat().setInt((short) 32767, c.getPlayer());
            c.getPlayer().getStat().setLuk((short) 32767, c.getPlayer());
            c.getPlayer().getStat().setMaxHp(500000, c.getPlayer());
            if (!GameConstants.isZero(c.getPlayer().getJob())) {
                c.getPlayer().getStat().setMaxMp(500000, c.getPlayer());
                c.getPlayer().getStat().setMp(500000, c.getPlayer());
            }
            c.getPlayer().getStat().setHp(500000, c.getPlayer());
            c.getPlayer().getStat().setStr((short) 32767, c.getPlayer());
            c.getPlayer().updateSingleStat(MapleStat.STR, 32767);
            c.getPlayer().updateSingleStat(MapleStat.DEX, 32767);
            c.getPlayer().updateSingleStat(MapleStat.INT, 32767);
            c.getPlayer().updateSingleStat(MapleStat.LUK, 32767);
            c.getPlayer().updateSingleStat(MapleStat.MAXHP, 500000);
            if (!GameConstants.isZero(c.getPlayer().getJob())) {
                c.getPlayer().updateSingleStat(MapleStat.MAXMP, 500000);
                c.getPlayer().updateSingleStat(MapleStat.MP, 500000);
            }
            c.getPlayer().updateSingleStat(MapleStat.HP, 500000);
            return 1;
        }
    }
  
  public static class 스탯초기화 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getStat().setStr((short)100, c.getPlayer());
      c.getPlayer().getStat().setDex((short)100, c.getPlayer());
      c.getPlayer().getStat().setInt((short)100, c.getPlayer());
      c.getPlayer().getStat().setLuk((short)100, c.getPlayer());
      c.getPlayer().getStat().setMaxHp(10000L, c.getPlayer());
      if (!GameConstants.isZero(c.getPlayer().getJob())) {
        c.getPlayer().getStat().setMaxMp(10000L, c.getPlayer());
        c.getPlayer().getStat().setMp(10000L, c.getPlayer());
      } 
      c.getPlayer().getStat().setHp(10000L, c.getPlayer());
      c.getPlayer().updateSingleStat(MapleStat.STR, 100L);
      c.getPlayer().updateSingleStat(MapleStat.DEX, 100L);
      c.getPlayer().updateSingleStat(MapleStat.INT, 100L);
      c.getPlayer().updateSingleStat(MapleStat.LUK, 100L);
      c.getPlayer().updateSingleStat(MapleStat.MAXHP, 10000L);
      if (!GameConstants.isZero(c.getPlayer().getJob())) {
        c.getPlayer().updateSingleStat(MapleStat.MAXMP, 10000L);
        c.getPlayer().updateSingleStat(MapleStat.MP, 10000L);
      } 
      c.getPlayer().updateSingleStat(MapleStat.HP, 10000L);
      return 1;
    }
  }
  
  public static class 시간 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().broadcastMessage(CField.getClock(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 60)));
      return 1;
    }
  }
  
  public static class 피시방시간 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().setInternetCafeTime(c.getPlayer().getInternetCafeTime() + Integer.parseInt(splitted[1]));
      c.getPlayer().dropMessage(6, "PC방 정량제를 " + Integer.parseInt(splitted[1]) + "분 늘렸습니다.");
      return 1;
    }
  }
  
  public static class 쿨 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().clearAllCooldowns();
      return 1;
    }
  }
  
  public static class 버프리스트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(5, "현재 버프 리스트입니다.");
      for (Pair<SecondaryStat, SecondaryStatValueHolder> effect : c.getPlayer().getEffects())
        c.getPlayer().dropMessage(-8, ((SecondaryStat)effect.left).name() + " : " + ((SecondaryStatValueHolder)effect.right).effect.getSourceId() + " / " + ((SecondaryStatValueHolder)effect.right).localDuration); 
      return 1;
    }
  }
  
  public static class 버프스탯테스트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "버프를 선택해주세요.");
        return 0;
      } 
      int type = Integer.parseInt(splitted[1]);
      if (type > SecondaryStat.getUnkBuffStats().size()) {
        c.getPlayer().dropMessage(5, "최대 사이즈 : " + SecondaryStat.getUnkBuffStats().size());
        return 0;
      } 
      SecondaryStat stat = SecondaryStat.getUnkBuffStats().get(type);
      Map<SecondaryStat, Pair<Integer, Integer>> dds = new HashMap<>();
      dds.put(stat, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
      c.getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(dds, SkillFactory.getSkill(2121004).getEffect(20), c.getPlayer()));
      c.getPlayer().dropMessage(5, "적용된 버프 : " + stat.name());
      return 1;
    }
  }
}
