package server.quest.party;

import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import constants.GameConstants;
import handling.channel.ChannelServer;
import handling.world.MaplePartyCharacter;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleNettPyramid {
  private int wave;
  
  private int life;
  
  private MapleMap map;
  
  private List<MapleNettPyramidMember> members = new ArrayList<>();
  
  private Point point1;
  
  private Point point2;
  
  private Point point3;
  
  private Point point4;
  
  private ScheduledFuture<?> monstertask;
  
  private ScheduledFuture<?> wavetask;
  
  private boolean next;
  
  private boolean hard;
  
  private long[] easyhp = new long[] { 
      100000000L, 200000000L, 300000000L, 400000000L, 500000000L, 600000000L, 700000000L, 800000000L, 900000000L, 1000000000L, 
      1200000000L, 1300000000L, 1400000000L, 1500000000L, 1600000000L, 1700000000L, 1800000000L, 1900000000L, 2000000000L, 2300000000L, 
      2500000000L, 3000000000L, 30000000000L };
  
  private long[] hardhp = new long[] { 
      1000000000L, 2000000000L, 3000000000L, 4000000000L, 5000000000L, 6000000000L, 7000000000L, 8000000000L, 9000000000L, 10000000000L, 
      12000000000L, 13000000000L, 14000000000L, 15000000000L, 16000000000L, 17000000000L, 18000000000L, 19000000000L, 20000000000L, 23000000000L, 
      25000000000L, 30000000000L, 300000000000L };
  
  private MapleNettPyramid() {
    this.wave = -1;
    this.life = 20;
    this.map = null;
    setMembers(new ArrayList<>());
    this.point1 = new Point(910, 155);
    this.point2 = new Point(910, -25);
    this.point3 = new Point(910, -205);
    this.point4 = new Point(910, -385);
    this.monstertask = null;
    this.wavetask = null;
    this.next = false;
  }
  
  public static MapleNettPyramid getInfo(MapleCharacter chr, boolean hard) {
    MapleNettPyramid ret = new MapleNettPyramid();
    MapleClient c = chr.getClient();
    ret.hard = hard;
    ret.map = chr.getMap();
    if (chr.getParty() != null) {
      for (MaplePartyCharacter member : chr.getParty().getMembers()) {
        MapleCharacter m = c.getChannelServer().getPlayerStorage().getCharacterById(member.getId());
        if (m != null)
          ret.getMembers().add(new MapleNettPyramidMember(m)); 
      } 
    } else {
      return null;
    } 
    return ret;
  }
  
  public static boolean warpNettPyramid(MapleCharacter chr, boolean hard) {
    if (chr.isLeader()) {
      MapleCharacter m = null;
      MapleClient c = chr.getClient();
      ChannelServer ch = chr.getClient().getChannelServer();
      MapleMap map = null;
      for (int i = 0; i < 20; i++) {
        map = ch.getMapFactory().getMap(926010300 + i);
        if (map.getCharactersSize() == 0) {
          map.resetFully(false);
          break;
        } 
        map = null;
      } 
      if (map != null) {
        for (MaplePartyCharacter member : chr.getParty().getMembers()) {
          m = c.getChannelServer().getPlayerStorage().getCharacterById(member.getId());
          m.nettDifficult = hard ? 2 : 1;
          if (m != null)
            m.changeMap(map); 
          m = null;
        } 
      } else {
        chr.Message("이용 가능한 맵이 없습니다. 채널 이동 후 시도해주세요.");
        chr.message("이용 가능한 맵이 없습니다. 채널 이동 후 시도해주세요.");
        return false;
      } 
    } else {
      chr.Message("파티장이 아니면 입장 신청을 하실 수 없습니다.");
      chr.message("파티장이 아니면 입장 신청을 하실 수 없습니다.");
      return false;
    } 
    return true;
  }
  
  public void firstNettPyramid(MapleCharacter chr) {
    try {
      setting();
      startNettPyramid();
    } catch (Exception e) {
      e.printStackTrace();
      chr.dropMessage(-8, "오류가 발생했습니다. 이 메세지를 찍어 1대1 문의에 제보하세요.");
    } 
  }
  
  public void setting() {
    if (this.map != null) {
      this.map.broadcastMessage(CField.NettPyramidPoint(0));
      this.map.broadcastMessage(CField.UIPacket.openUI(62));
    } 
    nextWave();
    changeLife();
  }
  
  public MapleNettPyramidMember getMember(int cid) {
    for (MapleNettPyramidMember mnpm : getMembers()) {
      if (mnpm.getCid() == cid)
        return mnpm; 
    } 
    return null;
  }
  
  public void startNettPyramid() {
    if (this.wavetask != null) {
      this.wavetask.cancel(true);
      this.wavetask = null;
    } 
    this.wavetask = Timer.EventTimer.getInstance().register(new Runnable() {
          int time = -1;
          
          public void run() {
            this.time++;
            if (this.time == 5) {
              MapleNettPyramid.this.showCount();
            } else if (this.time == 8) {
              MapleNettPyramid.this.startWaveNettPyramid();
              if (MapleNettPyramid.this.wavetask != null) {
                MapleNettPyramid.this.wavetask.cancel(true);
                MapleNettPyramid.this.wavetask = null;
              } 
              MapleNettPyramid.this.next = false;
            } 
          }
        },  1000L);
  }
  
  public void startWaveNettPyramid() {
    nextWave();
    if (this.monstertask != null) {
      this.monstertask.cancel(true);
      this.monstertask = null;
    } 
    this.monstertask = Timer.EventTimer.getInstance().register(new Runnable() {
          int time = -1;
          
          public void run() {
            this.time++;
            if (this.time >= 0) {
              int mobid = ((Integer)MapleNettPyramid.this.getMonsters().get(this.time)).intValue();
              MapleNettPyramid.this.spawnMonsters(mobid);
            } 
            if (this.time == MapleNettPyramid.this.getMonsters().size() - 1 && 
              MapleNettPyramid.this.monstertask != null) {
              MapleNettPyramid.this.monstertask.cancel(true);
              MapleNettPyramid.this.monstertask = null;
            } 
          }
        },  1000L);
  }
  
  public void nextWave() {
    this.wave++;
    changeWave();
  }
  
  public void changeWave() {
    if (this.map != null) {
      this.map.broadcastMessage(CField.NettPyramidWave(this.wave));
      if (this.wave >= 1) {
        this.map.broadcastMessage(CField.environmentChange("defense/wave/" + this.wave, 16));
        this.map.broadcastMessage(CField.environmentChange("killing/first/start", 16));
      } 
    } 
  }
  
  public void showCount() {
    if (this.map != null)
      this.map.broadcastMessage(CField.environmentChange("defense/count", 16)); 
  }
  
  public void minusLife(int objectId) {
    MapleMonster mob = this.map.getMonsterByOid(objectId);
    if (mob != null)
      this.map.killMonster(mob); 
    this.life--;
    if (this.life < 0)
      this.life = 0; 
    changeLife();
    if (this.life == 0)
      waveFail(); 
  }
  
  public void waveFail() {
    this.map.killAllMonsters(true);
    this.next = true;
    for (MapleNettPyramidMember mnpm : getMembers()) {
      mnpm.getChr().getClient().getSession().writeAndFlush(CField.environmentChange("killing/fail", 16));
      Timer.EventTimer.getInstance().schedule(new Runnable() {
            public void run() {
              mnpm.getChr().getClient().getSession().writeAndFlush(CField.NettPyramidClear(false, MapleNettPyramid.this.wave, MapleNettPyramid.this.life, mnpm.getPoint(), mnpm.getExp()));
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      MapleNettPyramid.this.endWave();
                      mnpm.getChr().changeMap(mnpm.getChr().getWarpMap(926010001));
                    }
                  },  7000L);
            }
          },2000L);
    } 
  }
  
  public void changeLife() {
    if (this.map != null)
      this.map.broadcastMessage(CField.NettPyramidLife(this.life)); 
  }
  
  public void spawnMonsters(int mid) {
    int level = getLevel(this.map);
    long hp = getHp(mid, level);
    int exp = getExp(level);
    this.map.spawnMonsterWithEffect(this.map.makePyramidMonster(MapleLifeFactory.getMonster(mid), hp, level, exp), -1, this.point1);
    this.map.spawnMonsterWithEffect(this.map.makePyramidMonster(MapleLifeFactory.getMonster(mid), hp, level, exp), -1, this.point3);
    if (this.wave != 20) {
      this.map.spawnMonsterWithEffect(this.map.makePyramidMonster(MapleLifeFactory.getMonster(mid), hp, level, exp), -1, this.point2);
      this.map.spawnMonsterWithEffect(this.map.makePyramidMonster(MapleLifeFactory.getMonster(mid), hp, level, exp), -1, this.point4);
    } 
  }
  
  public long getHp(int mid, int level) {
    int id = mid - 9305400;
    int plus = Math.max(1, level - 240);
    if (this.hard)
      return this.hardhp[id] * plus * ((4 + this.wave) / 4); 
    return this.easyhp[id] * plus * ((4 + this.wave) / 4);
  }
  
  public int getLevel(MapleMap map) {
    int total = 0;
    if (map.getAllCharactersThreadsafe().size() == 0)
      return 0; 
    for (MapleNettPyramidMember mnpm : getMembers())
      total += mnpm.getChr().getLevel(); 
    return total / map.getAllCharactersThreadsafe().size();
  }
  
  public int getExp(int level) {
    double exp = GameConstants.getExpNeededForLevel(level) * 1.0E-4D * this.wave / 10000.0D;
    if (this.hard)
      return (int)exp; 
    return (int)(exp * 0.01D);
  }
  
  public void check() {
    if (this.map.getAllMonster().size() == 0 && this.monstertask == null && this.wave > 0 && this.wave < 21 && !this.next) {
      waveClear();
      this.next = true;
      if (this.wave < 20)
        startNettPyramid(); 
    } 
  }
  
  public List<Integer> getMonsters() {
    Map<Integer, List<Integer>> info = this.map.getmonsterDefense();
    if (info == null || info.get(Integer.valueOf(this.wave)) == null) {
      System.out.println("NULL");
      return null;
    } 
    return info.get(Integer.valueOf(this.wave));
  }
  
  public void waveClear() {
    for (MapleNettPyramidMember mnpm : getMembers()) {
      if (this.wave == 20) {
        mnpm.getChr().getClient().getSession().writeAndFlush(CField.environmentChange("killing/clear", 16));
        Timer.EventTimer.getInstance().schedule(new Runnable() {
              public void run() {
                mnpm.getChr().getClient().getSession().writeAndFlush(CField.NettPyramidClear(true, MapleNettPyramid.this.wave, MapleNettPyramid.this.life, mnpm.getPoint(), mnpm.getExp()));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                      public void run() {
                        MapleNettPyramid.this.endWave();
                        mnpm.getChr().changeMap(mnpm.getChr().getWarpMap(MapleNettPyramid.this.hard ? 926010002 : 926010003));
                      }
                    },  7000L);
              }
            },2000L);
        continue;
      } 
      mnpm.getChr().getClient().getSession().writeAndFlush(CWvsContext.getTopMsg("WAVE를 막아냈습니다. 다음 WAVE를 준비해주세요."));
    } 
  }
  
  public void plusPoint(MapleCharacter chr, int point) {
    for (MapleNettPyramidMember mnpm : getMembers()) {
      if (mnpm.getChr().equals(chr)) {
        mnpm.plusPoint(point);
        break;
      } 
    } 
  }
  
  public void minusPoint(MapleCharacter chr, int point) {
    for (MapleNettPyramidMember mnpm : getMembers()) {
      if (mnpm.getChr().equals(chr)) {
        mnpm.minusPoint(point);
        break;
      } 
    } 
  }
  
  public int getPoint(MapleCharacter chr) {
    for (MapleNettPyramidMember mnpm : getMembers()) {
      if (mnpm.getChr().equals(chr))
        return mnpm.getPoint(); 
    } 
    return 0;
  }
  
  public void plusExp(MapleCharacter chr, int exp) {
    for (MapleNettPyramidMember mnpm : getMembers()) {
      if (mnpm.getChr().equals(chr)) {
        mnpm.plusExp(exp);
        break;
      } 
    } 
  }
  
  public void endWave() {
    for (MapleNettPyramidMember mnpm : getMembers()) {
      mnpm.getChr().setNettPyramid(null);
      mnpm.getChr().getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(62));
    } 
  }
  
  public void useSkill(MapleCharacter chr, int sid) {
    int usePoint = 0;
    if (sid == 2800014) {
      usePoint = 500;
      if (getPoint(chr) >= usePoint) {
        SkillFactory.getSkill(80001104).getEffect(1).applyTo(chr);
      } else {
        usePoint = -1;
      } 
    } else if (sid == 2800017) {
      usePoint = 500;
      if (getPoint(chr) >= usePoint) {
        for (MapleMonster mapleMonster : this.map.getAllMonster());
      } else {
        usePoint = -1;
      } 
    } else if (sid == 2800016) {
      usePoint = 500;
      for (MapleMonster mapleMonster : this.map.getAllMonster());
    } else if (sid == 2800015) {
      usePoint = 700;
      if (getPoint(chr) >= usePoint) {
        for (MapleMonster mapleMonster : this.map.getAllMonster());
      } else {
        usePoint = -1;
      } 
    } else if (sid == 2800019) {
      usePoint = 2000;
    } 
    if (usePoint == -1) {
      chr.dropMessage(5, "포인트가 부족합니다.");
    } else if (usePoint == 0) {
      chr.dropMessage(5, "사용 할 수 없는 스킬입니다.");
    } else {
      minusPoint(chr, usePoint);
    } 
  }
  
  public boolean isHard() {
    return this.hard;
  }
  
  public List<MapleNettPyramidMember> getMembers() {
    return this.members;
  }
  
  public void setMembers(List<MapleNettPyramidMember> members) {
    this.members = members;
  }
  
  public static class MapleNettPyramidMember {
    private MapleCharacter chr;
    
    private int point = 0, exp = 0;
    
    public MapleNettPyramidMember(MapleCharacter chr) {
      this.chr = chr;
    }
    
    public MapleCharacter getChr() {
      return this.chr;
    }
    
    public int getCid() {
      return this.chr.getId();
    }
    
    public void plusPoint(int point) {
      this.point += point;
      changePoint();
    }
    
    public void minusPoint(int point) {
      if (getPoint() > 0) {
        this.point -= point;
        changePoint();
      } 
    }
    
    public int getPoint() {
      return this.point;
    }
    
    public void changePoint() {
      getChr().getClient().getSession().writeAndFlush(CField.NettPyramidPoint(this.point));
    }
    
    public void plusExp(int exp) {
      this.exp += exp;
    }
    
    public int getExp() {
      return this.exp;
    }
  }
}
