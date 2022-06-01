package server.games;

import client.MapleCharacter;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.SkillFactory;
import client.inventory.Item;
import handling.channel.ChannelServer;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import server.Timer;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import tools.Pair;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class JumpingUnicon {
  public static boolean isrun = false;
  
  public static int stage = 0;
  
  public static int count = 1;
  
  public static List<MapleCharacter> dropowner = new ArrayList<>();
  
  public static List<MapleCharacter> MatchingQueue = new ArrayList<>();
  
  public static List<MapleCharacter> MatchingQueue2 = new ArrayList<>();
  
  public static void StartGame() {
    final MapleMap map = ChannelServer.getInstance(1).getMapFactory().getMap(993074001);
    map.resetFully();
    for (MapleCharacter chr : map.getAllChracater()) {
      SkillFactory.getSkill(80002669).getEffect(1).applyTo(chr);
      chr.startSound();
    } 
    map.broadcastMessage(CField.enforceMSG("솜사탕 유니콘을 타고 점프 점프! 다양한 팅커들을 모아보세요!", 162, 6000));
    map.broadcastMessage(CField.showEffect("defense/count"));
    Timer.EtcTimer.getInstance().schedule(new Runnable() {
          public void run() {
            map.broadcastMessage(CField.showEffect("Effect/EventEffect.img/HundredRSP/start/1"));
            map.broadcastMessage(SLFCGPacket.ActiveUnion(true));
            map.broadcastMessage(SLFCGPacket.milliTimer(60000));
          }
        },  3300L);
    Timer.EtcTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (MapleCharacter chr : map.getAllChracater()) {
              if (chr.getMapId() == 993074001)
                chr.warp(993074800); 
            } 
          }
        },  63300L);
    if (MapleMap.uniconflower.isEmpty()) {
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -1020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -1020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -1020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -1020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -1020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -1100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -1100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -1100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -1100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -1100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -1100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -1170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -1170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -1170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -1170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -1170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -1170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -1260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -1260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -1260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -1260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -1260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -1260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -1320)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -1320)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -1320)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -1320)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -1320)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -1400)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -1400)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -1400)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -1400)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -1400)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -1400)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -1470)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -1470)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -1470)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -1470)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -1470)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -1470)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -1560)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -1560)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -1560)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -1560)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -1560)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -1560)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -1620)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -1620)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -1620)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -1620)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -1620)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -1700)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -1700)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -1700)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -1700)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -1700)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -1700)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -1770)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -1770)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -1770)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -1770)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -1770)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -1770)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -1860)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -1860)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -1860)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -1860)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -1860)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -1860)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -1920)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -1920)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -1920)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -1920)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -1920)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -2000)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -2000)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -2000)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -2000)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -2000)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -2000)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -2070)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -2070)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -2070)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -2070)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -2070)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -2070)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -2160)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -2160)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -2160)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -2160)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -2160)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -2160)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -2220)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -2220)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -2220)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -2220)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -2220)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -2300)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -2300)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -2300)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -2300)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -2300)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -2300)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -2370)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -2370)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -2370)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -2370)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -2370)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -2370)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -2460)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -2460)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -2460)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -2460)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -2460)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -2460)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -2520)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -2520)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -2520)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -2520)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -2520)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -2600)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -2600)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -2600)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -2600)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -2600)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -2600)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -2670)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -2670)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -2670)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -2670)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -2670)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -2670)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -2760)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -2760)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -2760)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -2760)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -2760)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -2760)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -2820)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -2820)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -2820)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -2820)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -2820)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -2900)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -2900)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -2900)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -2900)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -2900)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -2900)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -2970)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -2970)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -2970)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -2970)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -2970)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -2970)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -3060)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -3060)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -3060)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -3060)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -3060)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -3060)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -3120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -3120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -3120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -3120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -3120)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -3200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -3200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -3200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -3200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -3200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -3200)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -3270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -3270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -3270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -3270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -3270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -3270)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -3360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -3360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -3360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -3360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -3360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -3360)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -3420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -3420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -3420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -3420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -3420)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -3500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -3500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -3500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -3500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -3500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -3500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -3570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -3570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -3570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -3570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -3570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -3570)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -3660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -3660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -3660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -3660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -3660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -3660)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -3720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -3720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -3720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -3720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -3720)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -3800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -3800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -3800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -3800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -3800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -3800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -3870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -3870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -3870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -3870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -3870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -3870)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -3960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -3960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -3960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -3960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -3960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -3960)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-680, -4020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-380, -4020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-80, -4020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(220, -4020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(520, -4020)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -4100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -4100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -4100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -4100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -4100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -4100)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-680, -4170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-380, -4170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(-80, -4170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(220, -4170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(520, -4170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632343), new Point(820, -4170)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-835, -4260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-535, -4260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(-235, -4260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(65, -4260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(365, -4260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632342), new Point(665, -4260)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(-335, -4380)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(265, -4380)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(-90, -4500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(510, -4500)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(-335, -4680)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(265, -4680)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(-90, -4800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(510, -4800)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(-335, -4980)));
      MapleMap.uniconflower.add(new Pair<>(Integer.valueOf(2632344), new Point(265, -4980)));
    } 
    if (!MapleMap.uniconflower.isEmpty())
      for (Pair<Integer, Point> a : MapleMap.uniconflower) {
        for (MapleCharacter chr : dropowner) {
          Item idrop = new Item(((Integer)a.left).intValue(), (short)0, (short)1, 0);
          MapleMapItem mdrop = new MapleMapItem(idrop, (Point)a.right, chr, chr, (byte)0, false, 0);
          map.addMapObject(mdrop);
          for (MapleCharacter chr2 : map.getAllChracater()) {
            if (chr.getSkillCustomValue0(993074001) == chr2.getSkillCustomValue0(993074001)) {
              chr2.addVisibleMapObject(mdrop);
              chr2.getClient().send(CField.dropItemFromMapObject(map, mdrop, (Point)a.right, (Point)a.right, (byte)2, false));
            } 
          } 
        } 
      }  
    dropowner.clear();
  }
  
  public static void ExitWaiting(MapleCharacter chr) {
    if (MatchingQueue != null && MatchingQueue2 != null) {
      if (MatchingQueue.contains(chr))
        MatchingQueue.remove(chr); 
      if (MatchingQueue2.contains(chr))
        MatchingQueue2.remove(chr); 
    } 
  }
  
  public static void StartGame2(int n) {
    isrun = true;
    StartWaiting(n);
    Timer.EtcTimer.getInstance().schedule(() -> {
          isrun = false;
          if (MatchingQueue.size() >= n) {
            for (MapleCharacter chr : MatchingQueue) {
              List<Pair<SecondaryStat, SecondaryStatValueHolder>> allBuffs = new ArrayList<>();
              for (Pair<SecondaryStat, SecondaryStatValueHolder> buff : chr.getEffects()) {
                if (SkillFactory.getSkill(((SecondaryStatValueHolder)buff.getRight()).effect.getSourceId()) != null)
                  allBuffs.add(buff); 
              } 
              for (Pair<SecondaryStat, SecondaryStatValueHolder> data : allBuffs) {
                SecondaryStatValueHolder mbsvh = (SecondaryStatValueHolder)data.right;
                chr.cancelEffect(mbsvh.effect);
              } 
              chr.warp(993074000);
              chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, new int[] { 11, 5, 1, 21 }));
            } 
          } else {
            for (MapleCharacter chr : MatchingQueue) {
              chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, new int[] { 11, 5, 1, 21 }));
              chr.dropMessage(5, "게임을 시작하기 위한 최소 인원이 부족하여 대기열이 취소 되었습니다.");
            } 
            MatchingQueue.clear();
            MatchingQueue2.clear();
          } 
        }, 30000L);
  }
  
  public static void StartWaiting(int n) {
    MapleMap map = ChannelServer.getInstance(1).getMapFactory().getMap(993074000);
    map.setCustomInfo(993100, 0, 60000);
    Timer.EtcTimer.getInstance().schedule(() -> {
          if (MatchingQueue.size() >= n) {
            int size = 1;
            int top = 1;
            for (MapleCharacter chr : MatchingQueue) {
              chr.removeSkillCustomInfo(993074001);
              chr.setSkillCustomInfo(993074001, top, 0L);
              chr.setKeyValue(2632342, "꽃잎팅커", "0");
              chr.setKeyValue(2632342, "풀잎팅커", "0");
              chr.setKeyValue(2632342, "달잎팅커", "0");
              chr.setKeyValue(2632342, "총합", "0");
              if (size == 1)
                dropowner.add(chr); 
              if (++size > 5) {
                size = 1;
                top++;
              } 
              chr.warp(993074001);
            } 
            MatchingQueue.clear();
            MatchingQueue2.clear();
            StartGame();
          } 
        }, 60000L);
  }
  
  public static void addQueue(MapleCharacter chr) {
    if (!MatchingQueue.contains(chr))
      if (isrun) {
        MatchingQueue.add(chr);
        chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 993074000, new int[] { 11, 2, 1, 21 }));
      } else {
        chr.getClient().send(CField.NPCPacket.getNPCTalk(9062354, (byte)0, "아쉽지만. 참여 가능한 시간이 다 지나 버렸어요!\r\n다음에 만나요!", "00 00", (byte)0, chr.getId()));
      }  
  }
}
