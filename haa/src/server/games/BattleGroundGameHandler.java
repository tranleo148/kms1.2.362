package server.games;

import client.MapleCharacter;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.SkillFactory;
import client.inventory.Item;
import constants.GameConstants;
import handling.channel.ChannelServer;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import server.Randomizer;
import server.Timer;
import server.events.MapleBattleGroundCharacter;
import server.life.MapleLifeFactory;
import server.maps.MapleMap;
import tools.Pair;
import tools.packet.BattleGroundPacket;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class BattleGroundGameHandler {
  private static List<MapleCharacter> Matchinglist = new ArrayList<>();
  
  private static List<MapleCharacter> Playerlist = new ArrayList<>();
  
  private static boolean start = false, already = false, EndOfGame = false, NotDamage = false;
  
  public static void BattleGroundIinviTation() {
    Matchinglist = new ArrayList<>();
    Playerlist = new ArrayList<>();
    start = false;
    already = false;
    EndOfGame = false;
    NotDamage = false;
    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
      for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
        if (player != null && player.getName() != null)
          player.getClient().send(SLFCGPacket.EventMsgSend(100199, 993026900, 30, "NewYear")); 
      } 
    } 
    Timer.EventTimer.getInstance().schedule(() -> {
          already = true;
          Warp();
        }, 30000L);
  }
  
  public static void AddChr(MapleCharacter chr) {
    Matchinglist.add(chr);
    chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 993026900, new int[] { 11, 2, 1, 26 }));
  }
  
  public static void AddChangeChannelChr(int charid) {
    for (MapleCharacter chrs : ChannelServer.getInstance(1).getPlayerStorage().getAllCharacters().values()) {
      if (charid == chrs.getId()) {
        Matchinglist.add(chrs);
        chrs.getClient().send(SLFCGPacket.ContentsWaiting(chrs, 993026900, new int[] { 11, 2, 1, 26 }));
        break;
      } 
    } 
  }
  
  public static void Warp() {
    ChannelServer.getInstance(1).getMapFactory().getMap(993026900).setCustomInfo(993026900, 0, 30000);
    for (MapleCharacter chr : Matchinglist) {
      for (Pair<SecondaryStat, SecondaryStatValueHolder> data : chr.getEffects()) {
        SecondaryStatValueHolder mbsvh = (SecondaryStatValueHolder)data.right;
        if (SkillFactory.getSkill(mbsvh.effect.getSourceId()) != null && 
          mbsvh.effect.getSourceId() != 80002282 && mbsvh.effect.getSourceId() != 2321055)
          chr.cancelEffect(mbsvh.effect, Arrays.asList(new SecondaryStat[] { (SecondaryStat)data.left })); 
      } 
      chr.warp(993026900);
    } 
  }
  
  public static void Ready() {
    int totalPeople = ChannelServer.getInstance(1).getMapFactory().getMap(993026900).getAllChracater().size();
    int averPeople = 999, now = 0;
    int baseMap = 921174100;
    int group = 0;
    for (int i = 921174100; i < 921174110; i++) {
      MapleMap map = ChannelServer.getInstance(1).getMapFactory().getMap(i);
      if (map != null) {
        map.resetFully();
        map.setBattleGroundTimer(30);
      } 
    } 
    MapleBattleGroundCharacter.bchr.clear();
    List<MapleCharacter> chrs = new ArrayList<>();
    List<MapleCharacter> move = new ArrayList<>();
    while (totalPeople > 0) {
      for (MapleCharacter chr : Matchinglist) {
        if (chr.getMapId() == 993026900 && !move.contains(chr)) {
          chrs.add(chr);
          move.add(chr);
          now++;
          totalPeople--;
          if (now >= averPeople) {
            now = 0;
            group++;
            break;
          } 
        } 
      } 
      int portal = 0;
      for (MapleCharacter chr : chrs) {
        for (int j = 921174100; j < 921174110; j++) {
          MapleMap map = ChannelServer.getInstance(1).getMapFactory().getMap(j);
          if (map.getCharactersSize() < 999) {
            chr.warp(map.getId());
            break;
          } 
        } 
        chr.getClient().send(CField.instantMapWarp(chr, (byte)Randomizer.rand(0, 11)));
        chr.getClient().send(BattleGroundPacket.SelectAvater());
        for (Pair<SecondaryStat, SecondaryStatValueHolder> data : chr.getEffects()) {
          SecondaryStatValueHolder mbsvh = (SecondaryStatValueHolder)data.right;
          if (SkillFactory.getSkill(mbsvh.effect.getSourceId()) != null && 
            mbsvh.effect.getSourceId() != 80002282 && mbsvh.effect.getSourceId() != 2321055)
            chr.cancelEffect(mbsvh.effect, Arrays.asList(new SecondaryStat[] { (SecondaryStat)data.left })); 
        } 
        portal++;
      } 
    } 
  }
  
  public static void StartGame(MapleMap map) {
    if (map != null && 
      map.getAllChracater().size() > 0) {
      for (MapleCharacter chr : map.getAllChracater()) {
        if (chr.getBattleGroundChr() == null) {
          int type = Randomizer.rand(1, 11);
          while (type == 8)
            type = Randomizer.rand(1, 11); 
          String job = (type == 1) ? "만지" : ((type == 2) ? "마이크" : ((type == 3) ? "다크로드" : ((type == 4) ? "하인즈" : ((type == 5) ? "무공" : ((type == 6) ? "헬레나" : ((type == 7) ? "랑이" : ((type == 9) ? "류드" : ((type == 10) ? "웡키" : ((type == 11) ? "폴로&프리토" : "없음")))))))));
          chr.setBattleGrondJobName(job);
          MapleBattleGroundCharacter gchr = new MapleBattleGroundCharacter(chr, chr.getBattleGrondJobName());
          chr.getMap().broadcastMessage(BattleGroundPacket.ChangeAvater(gchr, GameConstants.BattleGroundJobType(gchr)));
          chr.getClient().send(BattleGroundPacket.UpgradeMainSkill(gchr));
          chr.getClient().send(BattleGroundPacket.AvaterSkill(gchr, GameConstants.BattleGroundJobType(gchr)));
          chr.getClient().send(BattleGroundPacket.SelectAvaterOther(chr, 1, 1));
          MapleBattleGroundCharacter t = gchr;
          t.setTeam(2);
          chr.getMap().broadcastMessage(chr, BattleGroundPacket.ChangeAvater(t, GameConstants.BattleGroundJobType(t)), false);
        } 
      } 
      NotDamage = true;
      spawnMob(map);
      map.broadcastMessage(CField.ImageTalkNpc(9001153, 5000, "전투 시작. 몬스터와 보스 몬스터를 처치해 강해질 수 있습니다. 게임시작 후 1분간은 #r무적#k 상태 입니다."));
      map.broadcastMessage(CField.getClock(599));
      map.setBattleGroundMainTimer(599);
    } 
  }
  
  public static void EndPlayGamez(MapleMap map) {
    map.broadcastMessage(CField.getClock(120));
    map.resetFully();
    map.resetFully();
    List<Point> pt = new ArrayList<>();
    pt.add(new Point(-1401, -1423));
    pt.add(new Point(380, -1423));
    map.broadcastMessage(CField.getFieldSkillEffectAdd(100008, 1, pt));
    map.broadcastMessage(CField.ImageTalkNpc(9001153, 5000, "15초마다 전장이 양쪽에서 줄어들기 시작합니다. 주의하세요!"));
    map.setCustomInfo(921174100, 0, 120000);
    map.setCustomInfo(921174101, 0, 15000);
    map.setCustomInfo(921174102, 0, 0);
    EndOfGame = true;
    for (MapleCharacter chr : map.getAllChracater()) {
      if (chr.getBattleGroundChr() != null) {
        chr.getBattleGroundChr().Heal();
        chr.getBattleGroundChr().setAlive(true);
        Point pos = null;
        switch (Randomizer.rand(0, 11)) {
          case 0:
            pos = new Point(-991, -1898);
            break;
          case 1:
            pos = new Point(-1344, -1733);
            break;
          case 2:
            pos = new Point(-1171, -1733);
            break;
          case 3:
            pos = new Point(-1146, -1552);
            break;
          case 4:
            pos = new Point(-958, -1552);
            break;
          case 5:
            pos = new Point(-1258, -1423);
            break;
          case 6:
            pos = new Point(-54, -1896);
            break;
          case 7:
            pos = new Point(130, -1724);
            break;
          case 8:
            pos = new Point(311, -1724);
            break;
          case 9:
            pos = new Point(81, -1555);
            break;
          case 10:
            pos = new Point(101, -1555);
            break;
          case 11:
            pos = new Point(318, -1423);
            break;
        } 
        chr.getClient().send(CField.fireBlink(chr.getId(), pos));
      } 
    } 
  }
  
  public static List<MapleCharacter> getMatchinglist() {
    return Matchinglist;
  }
  
  public static void setMatchinglist(List<MapleCharacter> Matchinglist) {
    BattleGroundGameHandler.Matchinglist = Matchinglist;
  }
  
  public static List<MapleCharacter> getPlayerlist() {
    return Playerlist;
  }
  
  public static void setPlayerlist(List<MapleCharacter> Playerlist) {
    BattleGroundGameHandler.Playerlist = Playerlist;
  }
  
  public static boolean isStart() {
    return start;
  }
  
  public static void setStart(boolean start) {
    BattleGroundGameHandler.start = start;
  }
  
  public static boolean isAlready() {
    return already;
  }
  
  public static void setAlready(boolean already) {
    BattleGroundGameHandler.already = already;
  }
  
  public static boolean isEndOfGame() {
    return EndOfGame;
  }
  
  public static void setEndOfGame(boolean EndOfGame) {
    BattleGroundGameHandler.EndOfGame = EndOfGame;
  }
  
  public static boolean isNotDamage() {
    return NotDamage;
  }
  
  public static void setNotDamage(boolean NotDamage) {
    BattleGroundGameHandler.NotDamage = NotDamage;
  }
  
  public static void spawnMob(MapleMap map) {
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-2674, 367));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-2485, 367));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2735, 449));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2585, 449));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2410, 449));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2854, 529));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2697, 529));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2463, 529));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2301, 529));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-2674, 1253));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-2485, 1253));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2735, 1335));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2585, 1335));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2410, 1335));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2854, 1415));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2697, 1415));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2463, 1415));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2301, 1415));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-2674, 2145));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-2485, 2145));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2735, 2227));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2585, 2227));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-2410, 2227));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2854, 2307));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2697, 2307));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2463, 2307));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-2301, 2307));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(1703, 377));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(1910, 377));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(1614, 459));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(1827, 459));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(2053, 459));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1546, 539));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1728, 539));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1953, 539));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(2134, 539));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(1703, 1261));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(1910, 1261));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(1614, 1343));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(1827, 1343));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(2053, 1343));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1546, 1423));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1728, 1423));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1953, 1423));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(2134, 1423));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(1703, 2145));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(1910, 2145));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(1614, 2227));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(1827, 2227));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(2053, 2227));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1546, 2307));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1728, 2307));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(1953, 2307));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(2134, 2307));
    int i;
    for (i = 0; i < 6; i++) {
      int mobid = (i == 5 || i == 6) ? 9303227 : 9303222;
      map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobid), new Point(Randomizer.rand(-876, 80), 825));
    } 
    for (i = 0; i < 9; i++) {
      int mobid = (i < 4) ? 9303224 : ((i < 7) ? 9303225 : 9303228);
      map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobid), new Point(Randomizer.rand(-1117, 396), 1495));
    } 
    for (i = 0; i < 6; i++) {
      int mobid = (i == 5 || i == 6) ? 9303227 : 9303222;
      map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobid), new Point(Randomizer.rand(-679, -83), 2004));
    } 
  }
  
  public static void DropItems(MapleMap map, MapleCharacter chr) {
    List<Point> pos = new ArrayList<>();
    pos.add(new Point(-960, 2385));
    pos.add(new Point(-1419, 1943));
    pos.add(new Point(-1027, 1495));
    pos.add(new Point(-876, 456));
    pos.add(new Point(-685, 2093));
    pos.add(new Point(389, 2385));
    pos.add(new Point(672, 1943));
    pos.add(new Point(258, 1854));
    pos.add(new Point(868, 609));
    pos.add(new Point(258, 1854));
    pos.add(new Point(77, 459));
    pos.add(new Point(1820, 1261));
    pos.add(new Point(1817, 377));
    pos.add(new Point(1841, 2145));
    pos.add(new Point(-2617, 2145));
    pos.add(new Point(1841, 2145));
    pos.add(new Point(-2637, 1253));
    pos.add(new Point(-2599, 367));
    for (int i = 0; i < 5; i++) {
      int rand = Randomizer.rand(0, pos.size() - 1);
      Point p = pos.get(rand);
      map.removeDrops();
      Item toDrop = new Item(Randomizer.rand(2022570, 2022574), (short)0, (short)1, 0);
      map.spawnItemDrop(chr, chr, toDrop, p, true, false);
      pos.remove(rand);
    } 
  }
}
