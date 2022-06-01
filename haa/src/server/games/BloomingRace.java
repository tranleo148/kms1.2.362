package server.games;

import client.MapleCharacter;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.SkillFactory;
import handling.channel.ChannelServer;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import server.Randomizer;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.SLFCGPacket;

public class BloomingRace {
  private static List<MapleCharacter> Matchinglist = new ArrayList<>();
  
  private static List<MapleCharacter> Playerlist = new ArrayList<>();
  
  private static List<MapleCharacter> RankList = new ArrayList<>();
  
  private static int Rank;
  
  private static int randPortal1;
  
  private static int randPortal2;
  
  private static int randPortal3;
  
  private static boolean start = false, already = false;
  
  public static void RaceIinviTation() {
    Matchinglist = new ArrayList<>();
    Playerlist = new ArrayList<>();
    RankList = new ArrayList<>();
    randPortal1 = 0;
    randPortal2 = 0;
    randPortal3 = 0;
    Rank = 0;
    start = false;
    already = false;
    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
      for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
        if (player != null && player.getName() != null)
          player.getClient().send(SLFCGPacket.EventMsgSend(100796, 993192500, 30, "BloomingRace")); 
      } 
    } 
    Timer.EventTimer.getInstance().schedule(() -> {
          already = true;
          RaceWarp();
        }, 30000L);
  }
  
  public static void RaceAddChr(MapleCharacter chr) {
    Matchinglist.add(chr);
    chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 993192500, new int[] { 11, 2, 1, 25 }));
  }
  
  public static void RaceAddChangeChannelChr(int charid) {
    for (MapleCharacter chrs : ChannelServer.getInstance(1).getPlayerStorage().getAllCharacters().values()) {
      if (charid == chrs.getId()) {
        Matchinglist.add(chrs);
        chrs.getClient().send(SLFCGPacket.ContentsWaiting(chrs, 993192500, new int[] { 11, 2, 1, 25 }));
        break;
      } 
    } 
  }
  
  public static void RaceWarp() {
    ChannelServer.getInstance(1).getMapFactory().getMap(993192500).setCustomInfo(993192500, 0, 30000);
    for (MapleCharacter chr : Matchinglist) {
      for (Pair<SecondaryStat, SecondaryStatValueHolder> data : chr.getEffects()) {
        SecondaryStatValueHolder mbsvh = (SecondaryStatValueHolder)data.right;
        if (SkillFactory.getSkill(mbsvh.effect.getSourceId()) != null && 
          mbsvh.effect.getSourceId() != 80002282 && mbsvh.effect.getSourceId() != 2321055)
          chr.cancelEffect(mbsvh.effect, Arrays.asList(new SecondaryStat[] { (SecondaryStat)data.left })); 
      } 
      chr.warp(993192500);
    } 
  }
  
  public static void ReadyRace() {
    ChannelServer ch = ChannelServer.getInstance(1);
    MapleMap map = ch.getMapFactory().getMap(993192500);
    MapleMap playmap = ch.getMapFactory().getMap(993192600);
    playmap.resetFully();
    playmap.setCustomInfo(993192600, 0, 30000);
    for (MapleCharacter chr : map.getAllChracater()) {
      Playerlist.add(chr);
      chr.warp(993192600);
      SpawnMonster(chr);
    } 
    playmap.broadcastMessage(SLFCGPacket.BloomingRaceHandler(2, new int[] { 30000 }));
    Timer.EventTimer.getInstance().schedule(() -> {
          playmap.broadcastMessage(CWvsContext.serverNotice(5, "", "현재 맵에서는 잠재 능력과 에디셔널 잠재능력이 적용되지 않습니다."));
          playmap.broadcastMessage(CField.enforceMSG("<블루밍 레이스> 조금만 기다려줘. 곧 시작할 거야", 287, 3500));
        }, 2000L);
    Timer.EventTimer.getInstance().schedule(() -> playmap.broadcastMessage(CField.enforceMSG("블루밍 레이스에 온걸 환영해! 신나게 달릴 준비는 됐어?", 287, 3500)), 6000L);
    Timer.EventTimer.getInstance().schedule(() -> playmap.broadcastMessage(CField.enforceMSG("친구들이 심심하지 않게 여러가지 코스로 준비했으니 재미있게 즐겨줘.", 287, 3500)), 10000L);
    Timer.EventTimer.getInstance().schedule(() -> playmap.broadcastMessage(CField.enforceMSG("생각만큼 가는 길이 쉽지는 않을 거니까, 긴장하는 게 좋을 거야.", 287, 3500)), 14000L);
    Timer.EventTimer.getInstance().schedule(() -> playmap.broadcastMessage(CField.enforceMSG("히힛, 하지만 친구들이라면 이 정도는 충분히 가능할 거라고 믿어.", 287, 3500)), 18000L);
    Timer.EventTimer.getInstance().schedule(() -> playmap.broadcastMessage(CField.enforceMSG("다른 친구들보다 먼저 도착하면 더 좋은 선물을 줄게. 준비됐어?", 287, 3500)), 22000L);
  }
  
  public static void StartRace() {
    ChannelServer ch = ChannelServer.getInstance(1);
    MapleMap playmap = ch.getMapFactory().getMap(993192600);
    playmap.setCustomInfo(993192601, 0, 300000);
    playmap.setCustomInfo(993192602, 0, 4000);
    playmap.setCustomInfo(993192603, 0, 3500);
    playmap.setCustomInfo(993192604, 0, 3000);
    start = true;
    randPortal1 = Randomizer.rand(1, 3);
    randPortal2 = Randomizer.rand(1, 2);
    randPortal3 = Randomizer.rand(1, 3);
    playmap.broadcastMessage(CField.enforceMSG("시작! 포탈을 타고 레이스를 즐겨줘.", 287, 3500));
    playmap.broadcastMessage(SLFCGPacket.BloomingRaceHandler(4, new int[] { 300000 }));
  }
  
  public static void SpawnMonster(MapleCharacter chr) {
    ChannelServer ch = ChannelServer.getInstance(1);
    MapleMap playmap = ch.getMapFactory().getMap(993192600);
    MapleMonster monster = MapleLifeFactory.getMonster(9833959);
    monster.setOwner(chr.getId());
    playmap.spawnMonsterOnGroundBelow(monster, new Point(2452, 225));
    monster = MapleLifeFactory.getMonster(9833959);
    monster.setOwner(chr.getId());
    playmap.spawnMonsterOnGroundBelow(monster, new Point(2452, -41));
    monster = MapleLifeFactory.getMonster(9833959);
    monster.setOwner(chr.getId());
    playmap.spawnMonsterOnGroundBelow(monster, new Point(1980, -435));
    monster = MapleLifeFactory.getMonster(9833959);
    monster.setOwner(chr.getId());
    playmap.spawnMonsterOnGroundBelow(monster, new Point(2130, -435));
    monster = MapleLifeFactory.getMonster(9833959);
    monster.setOwner(chr.getId());
    playmap.spawnMonsterOnGroundBelow(monster, new Point(2280, -435));
    monster = MapleLifeFactory.getMonster(9833959);
    monster.setOwner(chr.getId());
    playmap.spawnMonsterOnGroundBelow(monster, new Point(2430, -435));
    monster = MapleLifeFactory.getMonster(9833959);
    monster.setOwner(chr.getId());
    playmap.spawnMonsterOnGroundBelow(monster, new Point(2580, -435));
  }
  
  public static int getRank() {
    return Rank;
  }
  
  public static void setRank(int Rank) {
    BloomingRace.Rank = Rank;
  }
  
  public static boolean isStart() {
    return start;
  }
  
  public static void setStart(boolean start) {
    BloomingRace.start = start;
  }
  
  public static int getRandPortal1() {
    return randPortal1;
  }
  
  public static void setRandPortal1(int randPortal1) {
    BloomingRace.randPortal1 = randPortal1;
  }
  
  public static int getRandPortal2() {
    return randPortal2;
  }
  
  public static void setRandPortal2(int randPortal2) {
    BloomingRace.randPortal2 = randPortal2;
  }
  
  public static int getRandPortal3() {
    return randPortal3;
  }
  
  public static void setRandPortal3(int randPortal3) {
    BloomingRace.randPortal3 = randPortal3;
  }
  
  public static List<MapleCharacter> getRankList() {
    return RankList;
  }
  
  public static void setRankList(List<MapleCharacter> RankList) {
    BloomingRace.RankList = RankList;
  }
  
  public static boolean isAlready() {
    return already;
  }
  
  public static void setAlready(boolean already) {
    BloomingRace.already = already;
  }
  
  public static List<MapleCharacter> getMatchinglist() {
    return Matchinglist;
  }
  
  public static List<MapleCharacter> getPlayerlist() {
    return Playerlist;
  }
}
