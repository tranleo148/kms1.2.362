package server.events;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.SecondaryStat;
import client.SkillFactory;
import client.inventory.Item;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.Timer;
import server.games.BattleGroundGameHandler;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.Pair;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.BattleGroundPacket;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.SLFCGPacket;

public class MapleBattleGround {
  public static void GameStart(MapleMap map) {
    map.resetFully();
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303217), new Point(1217, -195));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303220), new Point(-1200, 1410));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303221), new Point(-400, 1352));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303200), new Point(520, 1410));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303201), new Point(220, 1410));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303200), new Point(-80, 1291));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303201), new Point(-220, 1352));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-340, 1291));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-340, 1352));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-518, 1410));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303226), new Point(-164, 1410));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303202), new Point(-2500, 634));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303222), new Point(-1900, 634));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303203), new Point(-1700, 634));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303223), new Point(-1100, 634));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303227), new Point(-700, 634));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303227), new Point(-1900, 634));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303202), new Point(-1900, 259));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303222), new Point(-1300, 259));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303203), new Point(-1600, 259));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303223), new Point(-1400, 259));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303227), new Point(-1600, 259));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303202), new Point(-2400, -99));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303222), new Point(-2300, -99));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303227), new Point(-2150, -99));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303202), new Point(-1300, -100));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303222), new Point(-1200, -100));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303203), new Point(-1100, -100));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303223), new Point(-1000, -100));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303227), new Point(-1000, -100));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303204), new Point(174, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303204), new Point(896, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303205), new Point(1801, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303224), new Point(356, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303224), new Point(1606, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303225), new Point(1073, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303204), new Point(1432, 260));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303205), new Point(500, 260));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303224), new Point(800, 260));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303225), new Point(700, 260));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303228), new Point(500, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303228), new Point(700, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303228), new Point(1500, 629));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303228), new Point(1700, 260));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303228), new Point(566, 260));
  }
  
  public static void Attack(LittleEndianAccessor slea, MapleClient c) {
    List<Triple<Integer, Integer, Integer>> info;
    int cooltime;
    List<Pair<Integer, Integer>> coollist;
    List<Integer> mob = new ArrayList<>();
    int range = 0, attackimg = 0, imgafter = 0, speed = 0, delay = 400, delay2 = 0, cool = 0, stack = 0;
    int skillid = slea.readInt();
    if (skillid == 80001739 && 
      c.getPlayer().getSkillCustomValue(skillid) != null) {
      c.send(CWvsContext.enableActions(c.getPlayer()));
      return;
    } 
    slea.readByte();
    int left = slea.readByte();
    int skilllv = slea.readInt();
    Point oldpos = slea.readIntPos();
    Point newpos = slea.readIntPos();
    int psize = slea.readInt();
    for (int i = 0; i < psize; i++)
      mob.add(Integer.valueOf(slea.readInt())); 
    int size = slea.readInt();
    for (int j = 0; j < size; j++)
      mob.add(Integer.valueOf(slea.readInt())); 
    slea.readInt();
    int moveing = slea.readInt();
    switch (skillid) {
      case 80001647:
        attackimg = Randomizer.rand(1111, 1112);
        imgafter = 300;
        speed = 1;
        break;
      case 80001648:
        attackimg = 1113;
        imgafter = 300;
        delay = 0;
        speed = 1;
        cool = 5;
        break;
      case 80001649:
        attackimg = 1114;
        imgafter = 360;
        delay = 0;
        speed = 1;
        cool = 7;
        break;
      case 80001650:
        attackimg = 1115;
        imgafter = 150;
        range = 240;
        delay = 0;
        speed = 0;
        cool = 8;
        break;
      case 80001651:
        attackimg = 1116;
        imgafter = 15000;
        delay = 0;
        cool = 70;
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 11) {
          skilllv = 2;
        } else {
          skilllv = 1;
        } 
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer());
        break;
      case 80001652:
        attackimg = Randomizer.rand(1125, 1126);
        imgafter = 330;
        speed = 1;
        break;
      case 80001653:
        attackimg = 1128;
        imgafter = 1780;
        delay = 0;
        cool = 6;
        speed = 1;
        break;
      case 80001654:
        attackimg = 1127;
        imgafter = 6270;
        delay = 0;
        cool = 9;
        speed = 0;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer());
        break;
      case 80001655:
        attackimg = 1125;
        imgafter = 6000;
        delay = 0;
        cool = 40;
        speed = 0;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer(), 3000);
        c.getPlayer().setSkillCustomInfo(80001655, 500L, 3000L);
        break;
      case 80001676:
        attackimg = 4;
        imgafter = 1920;
        delay = 0;
        delay2 = 1256;
        speed = 0;
        if (c.getPlayer().getBuffedEffect(80001655) != null)
          c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(80001655)); 
        c.getPlayer().removeSkillCustomInfo(80001655);
        break;
      case 80001656:
        attackimg = 1126;
        imgafter = 12000;
        delay = 0;
        cool = 120;
        speed = 0;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer());
        break;
      case 80001677:
        attackimg = 4;
        imgafter = 1920;
        delay = 0;
        delay2 = 1256;
        speed = 0;
        break;
      case 80001657:
        attackimg = Randomizer.rand(1137, 1138);
        imgafter = 366;
        delay = 330;
        speed = 2;
        range = 367;
        if (left != 1) {
          oldpos.x += 25;
          newpos.x += 25;
        } else {
          oldpos.x -= 25;
          newpos.x -= 25;
        } 
        oldpos.y -= 27;
        newpos.y -= 27;
        break;
      case 80001675:
        attackimg = Randomizer.rand(1137, 1138);
        imgafter = 366;
        delay = 330;
        speed = 2;
        range = 367;
        if (left != 1) {
          oldpos.x += 25;
          newpos.x += 25;
        } else {
          oldpos.x -= 25;
          newpos.x -= 25;
        } 
        oldpos.y -= 27;
        newpos.y -= 27;
        break;
      case 80001658:
        attackimg = 1139;
        imgafter = 3690;
        delay = 0;
        speed = 1;
        cool = 10;
        break;
      case 80001659:
        attackimg = 1140;
        imgafter = 1717;
        delay = 0;
        speed = 2;
        range = 1202;
        if (left != 1) {
          oldpos.x += 25;
          newpos.x += 25;
        } else {
          oldpos.x -= 25;
          newpos.x -= 25;
        } 
        oldpos.y -= 27;
        newpos.y -= 27;
        cool = 12;
        break;
      case 80001660:
        attackimg = 1141;
        imgafter = 10450;
        delay = 0;
        speed = 1;
        cool = 60;
        break;
      case 80001661:
        attackimg = 1150;
        imgafter = 900;
        speed = 1;
        break;
      case 80001662:
        attackimg = 1151;
        imgafter = 990;
        delay = 0;
        speed = 1;
        cool = 6;
        break;
      case 80001663:
        attackimg = 1151;
        imgafter = 6000;
        delay = 0;
        speed = 1;
        cool = 12;
        break;
      case 80001664:
        attackimg = 1151;
        imgafter = 3806;
        delay = 0;
        range = 190;
        speed = 2;
        cool = 10;
        if (left != 1) {
          oldpos.x += 80;
          newpos.x += 80;
        } else {
          oldpos.x -= 80;
          newpos.x -= 80;
        } 
        oldpos.y -= 27;
        newpos.y -= 27;
        break;
      case 80001665:
        attackimg = 1152;
        imgafter = 1980;
        delay = 0;
        range = 190;
        speed = 1;
        cool = 60;
        break;
      case 80001666:
        attackimg = Randomizer.rand(1161, 1162);
        imgafter = 900;
        speed = 1;
        break;
      case 80001667:
        attackimg = 1163;
        delay = 0;
        imgafter = 690;
        speed = 1;
        cool = 5;
        break;
      case 80001668:
        attackimg = 1164;
        delay = 0;
        imgafter = 3500;
        speed = 0;
        cool = 10;
        c.getPlayer().cancelAllBuffs();
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer(), 4000);
        break;
      case 80001669:
        attackimg = 1165;
        delay = 0;
        imgafter = 1000;
        speed = 1;
        cool = 7;
        break;
      case 80001679:
        attackimg = 1165;
        delay = 0;
        delay2 = 1835;
        imgafter = 540;
        speed = 1;
        break;
      case 80001670:
        attackimg = 1161;
        delay = 0;
        imgafter = 10000;
        speed = 0;
        cool = 50;
        break;
      case 80001732:
        attackimg = 1174;
        range = 366;
        speed = 2;
        oldpos.y -= 27;
        newpos.y -= 27;
        c.getPlayer().setSkillCustomInfo(skillid, 2L, 0L);
        break;
      case 80001733:
        attackimg = 4;
        imgafter = 4000;
        delay = 0;
        speed = 0;
        cool = 10;
        oldpos.y -= 27;
        newpos.y -= 27;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer(), 4000);
        c.getPlayer().getBattleGroundChr().setAttackSpeed(c.getPlayer().getBattleGroundChr().getAttackSpeed() - 60);
        c.getPlayer().getBattleGroundChr().setTeam(2);
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), GameConstants.BattleGroundJobType(c.getPlayer().getBattleGroundChr())), false);
        c.getPlayer().getBattleGroundChr().setTeam(1);
        c.getPlayer().getClient().send(BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), GameConstants.BattleGroundJobType(c.getPlayer().getBattleGroundChr())));
        break;
      case 80001734:
        attackimg = 1175;
        imgafter = 5880;
        delay = 0;
        speed = 1;
        cool = 15;
        c.getPlayer().setSkillCustomInfo(skillid, 2L, 0L);
        break;
      case 80001735:
        attackimg = 1176;
        imgafter = 736;
        range = 480;
        delay = 0;
        speed = 2;
        cool = 15;
        stack = 5;
        oldpos.y -= 27;
        newpos.y -= 27;
        break;
      case 80001736:
        attackimg = 1177;
        imgafter = 5340;
        range = 1;
        delay = 0;
        speed = 4;
        cool = 45;
        c.getPlayer().setSkillCustomInfo(80001736, 0L, imgafter);
        break;
      case 80001737:
        attackimg = Randomizer.rand(1186, 1187);
        imgafter = 300;
        speed = 1;
        break;
      case 80001738:
        attackimg = 1188;
        imgafter = 540;
        delay = 0;
        speed = 1;
        cool = 3;
        break;
      case 80001739:
        attackimg = 1190;
        imgafter = 390;
        range = 300;
        delay = 0;
        cool = 10;
        speed = 1;
        c.getPlayer().setSkillCustomInfo(skillid, 0L, 5L);
        break;
      case 80001740:
        attackimg = 1190;
        imgafter = 10000;
        range = 300;
        delay = 0;
        cool = 17;
        speed = 0;
        c.getPlayer().setSkillCustomInfo(skillid, 3L, 0L);
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer(), 10000);
        break;
      case 80001741:
        attackimg = 1191;
        stack = 3;
        imgafter = 8700;
        range = 300;
        delay = 0;
        speed = 3;
        c.getPlayer().removeSkillCustomInfo(skillid);
        c.getPlayer().getBattleGroundChr().setTeam(2);
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), GameConstants.BattleGroundJobType(c.getPlayer().getBattleGroundChr())), false);
        c.getPlayer().getBattleGroundChr().setTeam(1);
        c.getPlayer().getClient().send(BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), GameConstants.BattleGroundJobType(c.getPlayer().getBattleGroundChr())));
        break;
      case 80002338:
        attackimg = Randomizer.rand(1213, 1214);
        imgafter = 300;
        speed = 1;
        break;
      case 80002339:
        attackimg = 1216;
        cool = 7;
        delay = 0;
        range = 617;
        imgafter = 1916;
        speed = 2;
        break;
      case 80002340:
        attackimg = 4;
        delay = 0;
        delay2 = 370;
        range = 617;
        imgafter = 4090;
        speed = 1;
        break;
      case 80002341:
        attackimg = 1217;
        delay = 0;
        imgafter = 15000;
        speed = 0;
        cool = 20;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer(), 7000);
        break;
      case 80002342:
        attackimg = 1218;
        delay = 0;
        imgafter = 1560;
        cool = 20;
        speed = 0;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer(), 2000);
        c.getPlayer().setSkillCustomInfo(skillid, 2L, 0L);
        break;
      case 80002344:
        attackimg = 1219;
        delay = 0;
        imgafter = 2560;
        cool = 70;
        speed = 0;
        break;
      case 80002670:
        attackimg = 4;
        imgafter = 3000000;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer());
        break;
      case 80002675:
        attackimg = 1228;
        imgafter = 583;
        range = 700;
        speed = 2;
        oldpos.y -= 27;
        newpos.y -= 27;
        while (c.getPlayer().getBuffedValue(80002670))
          c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(80002670)); 
        break;
      case 80002680:
        attackimg = 1228;
        imgafter = 736;
        range = 367;
        speed = 2;
        stack = 3;
        oldpos.y -= 27;
        newpos.y -= 27;
        break;
      case 80002681:
        attackimg = 1228;
        imgafter = 389;
        range = 390;
        speed = 2;
        oldpos.y -= 27;
        newpos.y -= 27;
        while (c.getPlayer().getBuffedValue(80002670))
          c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(80002670)); 
        break;
      case 80002671:
        attackimg = 1229;
        imgafter = 4000;
        delay = 0;
        speed = 0;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer());
        cool = 15;
        break;
      case 80002673:
        attackimg = 1231;
        imgafter = 1530;
        delay = 0;
        speed = 1;
        cool = 12;
        c.getPlayer().setSkillCustomInfo(skillid, 2L, 0L);
        break;
      case 80002672:
        attackimg = 1230;
        imgafter = 10000;
        delay = 0;
        speed = 0;
        cool = 20;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer());
        break;
      case 80002674:
        attackimg = 1234;
        imgafter = 600000;
        delay = 0;
        speed = 0;
        cool = 50;
        SkillFactory.getSkill(skillid).getEffect(skilllv).applyTo(c.getPlayer());
        break;
      case 80002676:
      case 80002677:
      case 80002678:
        attackimg = 1232;
        imgafter = 600000;
        delay = 0;
        range = 850;
        delay2 = 978;
        speed = 2;
        oldpos.y -= 27;
        newpos.y -= 27;
        if (left != 1) {
          oldpos.x += 80;
          newpos.x += 80;
        } else {
          oldpos.x -= 80;
          newpos.x -= 80;
        } 
        while (c.getPlayer().getBuffedValue(80002674))
          c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(80002674)); 
        break;
      case 80003001:
        attackimg = 1242;
        imgafter = 400;
        range = 400;
        speed = 2;
        oldpos.y -= 27;
        newpos.y -= 27;
        break;
      case 80003002:
        info = new ArrayList<>();
        attackimg = (moveing == 0) ? 4 : 1243;
        c.getPlayer().getBattleGroundChr().setJobType(12);
        c.getPlayer().getBattleGroundChr().setTeam(2);
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), 24), false);
        c.getPlayer().getBattleGroundChr().setTeam(1);
        c.send(BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), 24));
        c.getPlayer().getBattleGroundChr().getSkillList().clear();
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(1)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80001678), Integer.valueOf(1)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003001), Integer.valueOf(1)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003002), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003003), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003004), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003005), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003006), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003007), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003008), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003009), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003010), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003011), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003012), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003013), Integer.valueOf(1)));
        info.add(new Triple<>(Integer.valueOf(80003006), Integer.valueOf(1), Integer.valueOf(0)));
        info.add(new Triple<>(Integer.valueOf(80003007), Integer.valueOf(1), Integer.valueOf(4000)));
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 5) {
          info.add(new Triple<>(Integer.valueOf(80003008), Integer.valueOf(1), Integer.valueOf(15000)));
          info.add(new Triple<>(Integer.valueOf(80003003), Integer.valueOf(1), Integer.valueOf(15000)));
        } 
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 7) {
          info.add(new Triple<>(Integer.valueOf(80003004), Integer.valueOf(1), Integer.valueOf(15000)));
          info.add(new Triple<>(Integer.valueOf(80003009), Integer.valueOf(1), Integer.valueOf(20000)));
          info.add(new Triple<>(Integer.valueOf(80003010), Integer.valueOf(1), Integer.valueOf(20000)));
        } 
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 8) {
          info.add(new Triple<>(Integer.valueOf(80003005), Integer.valueOf((c.getPlayer().getBattleGroundChr().getLevel() >= 11) ? 2 : 1), Integer.valueOf(50000)));
          info.add(new Triple<>(Integer.valueOf(80003012), Integer.valueOf((c.getPlayer().getBattleGroundChr().getLevel() >= 11) ? 2 : 1), Integer.valueOf(50000)));
        } 
        c.send(BattleGroundPacket.AvaterSkill(c.getPlayer().getBattleGroundChr(), 24));
        c.send(BattleGroundPacket.SkillOnList(c.getPlayer().getBattleGroundChr(), 24, info));
        c.send(BattleGroundPacket.UpgradeMainSkill(c.getPlayer().getBattleGroundChr()));
        imgafter = 550;
        delay = 0;
        speed = 0;
        cool = 4;
        c.send(BattleGroundPacket.CoolDown(80003007, cool * 1000, 0));
        coollist = new ArrayList<>();
        coollist.add(new Pair<>(Integer.valueOf(80003003), Integer.valueOf(5)));
        coollist.add(new Pair<>(Integer.valueOf(80003004), Integer.valueOf(15)));
        coollist.add(new Pair<>(Integer.valueOf(80003008), Integer.valueOf(15)));
        coollist.add(new Pair<>(Integer.valueOf(80003009), Integer.valueOf(20)));
        coollist.add(new Pair<>(Integer.valueOf(80003012), Integer.valueOf(50)));
        coollist.add(new Pair<>(Integer.valueOf(80001678), Integer.valueOf(30)));
        for (Pair<Integer, Integer> cooll : coollist) {
          if (c.getPlayer().skillisCooling(((Integer)cooll.getLeft()).intValue())) {
            c.send(BattleGroundPacket.CoolDown(((Integer)cooll.getLeft()).intValue(), ((Integer)cooll.getRight()).intValue() * 1000, (int)((((Integer)cooll.getRight()).intValue() * 1000) - c.getPlayer().getCooldownLimit(((Integer)cooll.getLeft()).intValue()))));
            if (((Integer)cooll.getLeft()).intValue() == 80003012)
              c.send(BattleGroundPacket.CoolDown(80003005, ((Integer)cooll.getRight()).intValue() * 1000, (int)((((Integer)cooll.getRight()).intValue() * 1000) - c.getPlayer().getCooldownLimit(((Integer)cooll.getLeft()).intValue())))); 
          } 
        } 
        break;
      case 80003003:
        attackimg = 1244;
        imgafter = 981;
        range = 380;
        delay = 0;
        speed = 2;
        cool = 5;
        c.getPlayer().addCooldown(skillid, System.currentTimeMillis(), (cool * 1000));
        break;
      case 80003004:
        attackimg = 1245;
        imgafter = 1740;
        delay = 0;
        speed = 0;
        cool = 15;
        c.getPlayer().addCooldown(skillid, System.currentTimeMillis(), (cool * 1000));
        break;
      case 80003006:
        attackimg = 1254;
        imgafter = 488;
        range = 367;
        stack = 2;
        speed = 2;
        oldpos.y -= 27;
        newpos.y -= 27;
        if (left != 1) {
          oldpos.x += 80;
          newpos.x += 80;
          break;
        } 
        oldpos.x -= 80;
        newpos.x -= 80;
        break;
      case 80003007:
        info = new ArrayList<>();
        attackimg = (moveing == 0) ? 4 : 1255;
        imgafter = 550;
        delay = 0;
        speed = 0;
        cool = 4;
        c.getPlayer().getBattleGroundChr().setJobType(11);
        c.getPlayer().getBattleGroundChr().setTeam(2);
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), 23), false);
        c.getPlayer().getBattleGroundChr().setTeam(1);
        c.send(BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), 23));
        c.getPlayer().getBattleGroundChr().getSkillList().clear();
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(1)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80001678), Integer.valueOf(1)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003001), Integer.valueOf(1)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003002), Integer.valueOf(1)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003003), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003004), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003005), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003006), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003007), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003008), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003009), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003010), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003011), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003012), Integer.valueOf(0)));
        c.getPlayer().getBattleGroundChr().getSkillList().add(new Triple<>(Integer.valueOf(0), Integer.valueOf(80003013), Integer.valueOf(1)));
        c.send(BattleGroundPacket.AvaterSkill(c.getPlayer().getBattleGroundChr(), 23));
        info.add(new Triple<>(Integer.valueOf(80003001), Integer.valueOf(1), Integer.valueOf(0)));
        info.add(new Triple<>(Integer.valueOf(80003002), Integer.valueOf(1), Integer.valueOf(4000)));
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 5) {
          info.add(new Triple<>(Integer.valueOf(80003008), Integer.valueOf(1), Integer.valueOf(15000)));
          info.add(new Triple<>(Integer.valueOf(80003003), Integer.valueOf(1), Integer.valueOf(15000)));
        } 
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 7) {
          info.add(new Triple<>(Integer.valueOf(80003004), Integer.valueOf(1), Integer.valueOf(15000)));
          info.add(new Triple<>(Integer.valueOf(80003009), Integer.valueOf(1), Integer.valueOf(20000)));
          info.add(new Triple<>(Integer.valueOf(80003010), Integer.valueOf(1), Integer.valueOf(20000)));
        } 
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 8) {
          info.add(new Triple<>(Integer.valueOf(80003005), Integer.valueOf((c.getPlayer().getBattleGroundChr().getLevel() >= 11) ? 2 : 1), Integer.valueOf(50000)));
          info.add(new Triple<>(Integer.valueOf(80003012), Integer.valueOf((c.getPlayer().getBattleGroundChr().getLevel() >= 11) ? 2 : 1), Integer.valueOf(50000)));
        } 
        c.send(BattleGroundPacket.SkillOnList(c.getPlayer().getBattleGroundChr(), 23, info));
        c.send(BattleGroundPacket.UpgradeMainSkill(c.getPlayer().getBattleGroundChr()));
        c.send(BattleGroundPacket.CoolDown(80003002, cool * 1000, 0));
        coollist = new ArrayList<>();
        coollist.add(new Pair<>(Integer.valueOf(80003003), Integer.valueOf(5)));
        coollist.add(new Pair<>(Integer.valueOf(80003004), Integer.valueOf(15)));
        coollist.add(new Pair<>(Integer.valueOf(80003008), Integer.valueOf(15)));
        coollist.add(new Pair<>(Integer.valueOf(80003009), Integer.valueOf(20)));
        coollist.add(new Pair<>(Integer.valueOf(80003012), Integer.valueOf(50)));
        coollist.add(new Pair<>(Integer.valueOf(80001678), Integer.valueOf(30)));
        for (Pair<Integer, Integer> cooll : coollist) {
          if (c.getPlayer().skillisCooling(((Integer)cooll.getLeft()).intValue())) {
            c.send(BattleGroundPacket.CoolDown(((Integer)cooll.getLeft()).intValue(), ((Integer)cooll.getRight()).intValue() * 1000, (int)((((Integer)cooll.getRight()).intValue() * 1000) - c.getPlayer().getCooldownLimit(((Integer)cooll.getLeft()).intValue()))));
            if (((Integer)cooll.getLeft()).intValue() == 80003012)
              c.send(BattleGroundPacket.CoolDown(80003005, ((Integer)cooll.getRight()).intValue() * 1000, (int)((((Integer)cooll.getRight()).intValue() * 1000) - c.getPlayer().getCooldownLimit(((Integer)cooll.getLeft()).intValue())))); 
          } 
        } 
        break;
      case 80003008:
        attackimg = 1256;
        imgafter = 1200;
        delay = 0;
        speed = 1;
        cool = 15;
        c.getPlayer().addCooldown(skillid, System.currentTimeMillis(), (cool * 1000));
        break;
      case 80003009:
        attackimg = 1257;
        imgafter = 2800;
        delay = 0;
        speed = 0;
        cool = 20;
        c.getPlayer().addCooldown(skillid, System.currentTimeMillis(), (cool * 1000));
        break;
      case 80003010:
        attackimg = 1258;
        imgafter = 499;
        delay = 0;
        range = 600;
        delay2 = 433;
        speed = 2;
        oldpos.y -= 27;
        newpos.y -= 27;
        if (left != 1) {
          oldpos.x += 80;
          newpos.x += 80;
          break;
        } 
        oldpos.x -= 80;
        newpos.x -= 80;
        break;
      case 80003011:
        attackimg = 1259;
        imgafter = 90;
        delay = 0;
        delay2 = 708;
        speed = 0;
        break;
      case 80003005:
      case 80003012:
        attackimg = 1260;
        imgafter = 4270;
        delay = 0;
        speed = 1;
        cool = 50;
        c.getPlayer().addCooldown(80003012, System.currentTimeMillis(), (cool * 1000));
        break;
      case 80003013:
        attackimg = 4;
        delay = 0;
        imgafter = 1740;
        speed = 1;
        break;
      case 80001678:
        cooltime = BattleGroundGameHandler.isEndOfGame() ? 60 : 30;
        attackimg = 4;
        delay = 0;
        cool = cooltime;
        imgafter = 1000;
        speed = 0;
        c.getPlayer().addCooldown(skillid, System.currentTimeMillis(), (cooltime * 1000));
        break;
    } 
    if (attackimg > 1000)
      attackimg += 4; 
    Map<MapleStat, Long> hpmpupdate = new EnumMap<>(MapleStat.class);
    c.getPlayer().setSkillCustomInfo(156789, c.getPlayer().getSkillCustomValue0(156789) + 1L, 0L);
    if (stack > 0) {
      c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.AttackSkillStack(c.getPlayer(), oldpos, newpos, skillid, attackimg, delay, delay2, imgafter, speed, left, range, stack));
    } else {
      c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.AttackSkill(c.getPlayer(), oldpos, newpos, skillid, attackimg, delay, delay2, imgafter, speed, left, range, mob, moveing));
    } 
    c.send(BattleGroundPacket.CoolDown(skillid, cool * 1000, 0));
    long mpchange = SkillFactory.getSkill(skillid).getEffect(1).getMPCon();
    if (mpchange > 0L) {
      c.getPlayer().getBattleGroundChr().setMp((int)(c.getPlayer().getBattleGroundChr().getMp() - mpchange));
      hpmpupdate.put(MapleStat.MP, Long.valueOf(c.getPlayer().getBattleGroundChr().getMp()));
      if (hpmpupdate.size() > 0)
        c.getSession().writeAndFlush(CWvsContext.updatePlayerStats(hpmpupdate, false, c.getPlayer())); 
    } 
    c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
  }
  
  public static void AttackRefresh(LittleEndianAccessor slea, MapleClient c) {
    int stype = slea.readInt();
    if (stype == 1) {
      int count = slea.readInt();
      int type = slea.readInt();
      c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.AttackRefresh(c.getPlayer(), stype, count, type, new int[] { 0 }));
    } else if (stype == 2) {
      int unk1 = slea.readInt();
      int unk2 = slea.readInt();
      int unk3 = slea.readInt();
      int unk4 = slea.readInt();
      c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.AttackRefresh(c.getPlayer(), stype, unk1, unk2, new int[] { unk3, unk4 }));
    } 
  }
  
  public static void MoveAttack(LittleEndianAccessor slea, MapleClient c) {
    int chrid = slea.readInt();
    int attackcount = slea.readInt();
    int skillid = slea.readInt();
    slea.skip(1);
    slea.skip(1);
    slea.skip(1);
    slea.skip(1);
    slea.skip(4);
    Point pos = slea.readIntPos();
    Point pos1 = slea.readIntPos();
    Point pos2 = slea.readIntPos();
    c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.MoveAttack(c.getPlayer(), pos, pos1, attackcount));
  }
  
  public static void AttackDamage(LittleEndianAccessor slea, MapleClient c) {
    List<MapleBattleGroundMobInfo> minfo = new ArrayList<>();
    MapleMonster monster = null;
    int damage = 0, mobcount = 0, peoplecount = 0;
    SecondaryStatEffect effect = null;
    int attackcount = slea.readInt();
    int skillid = slea.readInt();
    int unk = slea.readByte();
    int unk1 = slea.readShort();
    int unk2 = slea.readInt();
    int unk3 = slea.readByte();
    Point pos1 = slea.readIntPos();
    Point pos2 = slea.readIntPos();
    Point pos3 = slea.readIntPos();
    Point pos4 = slea.readIntPos();
    Point pos5 = slea.readIntPos();
    int unk4 = slea.readInt();
    int unk5 = slea.readInt();
    int unk6 = slea.readInt();
    int unk7 = slea.readInt();
    int unk8 = slea.readInt();
    int unk9 = slea.readInt();
    int unk10 = slea.readInt();
    int unk11 = slea.readInt();
    int unk12 = slea.readInt();
    peoplecount = slea.readInt();
    if (c.getPlayer().getBattleGroundChr() == null)
      return; 
    int skilllv = 1;
    switch (skillid) {
      case 80001656:
      case 80001660:
      case 80001665:
      case 80001670:
      case 80001736:
      case 80002344:
      case 80003005:
      case 80003012:
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 11)
          skilllv = 2; 
        break;
      case 80001738:
        c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) + 1L, 0L);
        if (c.getPlayer().getSkillCustomValue0(skillid) >= 3L) {
          c.getPlayer().removeSkillCustomInfo(skillid);
          Item toDrop = new Item((c.getPlayer().getBattleGroundChr().getTeam() == 1) ? 4001849 : 4001847, (short)0, (short)1, 0);
          c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, new Point((c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y), true, false);
        } 
        break;
    } 
    int i;
    for (i = 0; i < peoplecount; i++) {
      effect = SkillFactory.getSkill(skillid).getEffect(skilllv);
      int basedam = Randomizer.rand(c.getPlayer().getBattleGroundChr().getMindam(), c.getPlayer().getBattleGroundChr().getMaxdam());
      int damper = effect.getDamage();
      if (skillid == 80001732) {
        if (c.getPlayer().getSkillCustomValue0(skillid) == 1L)
          damper = 50; 
        c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) - 1L, 0L);
      } else if (skillid == 80001734) {
        if (c.getPlayer().getSkillCustomValue0(skillid) == 2L)
          damper = 250; 
        c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) - 1L, 0L);
      } else if (skillid == 80002342) {
        if (c.getPlayer().getSkillCustomValue0(skillid) == 2L) {
          damper = 150;
        } else {
          while (c.getPlayer().getBuffedValue(skillid))
            c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(skillid)); 
          damper = 400;
        } 
        c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) - 1L, 0L);
      } 
      damage = basedam * damper / 100;
      boolean cri = false;
      if (Randomizer.isSuccess(c.getPlayer().getBattleGroundChr().getCritical())) {
        damage *= 2;
        cri = true;
      } 
      if (c.getPlayer().getBuffedValue(80001651))
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 11) {
          damage *= 2;
        } else {
          damage = (int)(damage * 1.5D);
        }  
      if (skillid == 80001663) {
        damage = -150;
      } else if (skillid == 80001656) {
        if (c.getPlayer().getBattleGroundChr().getLevel() >= 11) {
          damage = -300;
        } else {
          damage = -100;
        } 
      } else if (skillid == 80001678) {
        damage = -(c.getPlayer().getBattleGroundChr().getMaxHp() / 100 * 50);
      } 
      slea.skip(27);
      int skid = slea.readInt();
      slea.readByte();
      int cid = slea.readInt();
      int oid = slea.readInt();
      MapleBattleGroundCharacter chred = null;
      for (MapleCharacter chr : c.getPlayer().getMap().getAllChracater()) {
        if (oid == chr.getId()) {
          chred = chr.getBattleGroundChr();
          break;
        } 
      } 
      if (chred != null) {
        minfo.clear();
        if (c.getPlayer().getBattleGroundChr().getJobType() == 6 && 
          chred.getChr().getBuffedValue(80001732))
          damage += damage / 100 * 10; 
        if (chred.getChr().getBuffedValue(80001655) && chred.getChr().getSkillCustomValue0(80001655) > 0L && damage > 0) {
          int shield = (int)chred.getChr().getSkillCustomValue0(80001655);
          if (shield - damage > 0) {
            chred.getChr().setSkillCustomInfo(80001655, chred.getChr().getSkillCustomValue0(80001655) - damage, 0L);
            damage = 1;
          } else {
            damage -= shield;
            chred.getChr().removeSkillCustomInfo(80001655);
            while (chred.getChr().getBuffedValue(80001655))
              chred.getChr().cancelEffect(chred.getChr().getBuffedEffect(80001655)); 
          } 
        } 
        if (chred.getChr().getBuffedValue(80001654)) {
          damage -= 100;
        } else if (chred.getChr().getBuffedValue(80001733)) {
          damage -= damage / 100 * 30;
        } else if (chred.getChr().getBuffedValue(80002672)) {
          if (damage >= chred.getMaxHp() / 100 * 10)
            damage = chred.getMaxHp() / 100 * 10; 
          int bandam = damage / 100 * 40;
          c.getPlayer().getMap().broadcastMessage(CField.playerDamaged(c.getPlayer().getId(), bandam));
          c.getPlayer().getBattleGroundChr().setHp(c.getPlayer().getBattleGroundChr().getHp() - bandam);
          Map<MapleStat, Long> statup = new EnumMap<>(MapleStat.class);
          statup.put(MapleStat.HP, Long.valueOf(c.getPlayer().getBattleGroundChr().getHp()));
          c.getPlayer().getMap().broadcastMessage(CField.updatePartyMemberHP(c.getPlayer().getId(), c.getPlayer().getBattleGroundChr().getHp(), c.getPlayer().getBattleGroundChr().getMaxHp()));
          c.send(CWvsContext.updatePlayerStats(statup, false, c.getPlayer()));
          if (c.getPlayer().getBattleGroundChr().getHp() <= 0 && c.getPlayer().getBattleGroundChr().isAlive()) {
            c.getPlayer().getBattleGroundChr().getChr().cancelAllBuffs();
            c.getPlayer().getBattleGroundChr().getChr().cancelAllDebuffs();
            c.getPlayer().getBattleGroundChr().setAlive(false);
            chred.setKill(chred.getKill() + 1);
            if (!BattleGroundGameHandler.isEndOfGame()) {
              chred.setMoney(chred.getMoney() + 500);
            } else {
              c.send(CField.environmentChange("Map/Effect2.img/PvP/Lose", 16));
              c.send(SLFCGPacket.playSE("Sound/MiniGame.img/BattlePvp/Lose"));
              int alivechr = 0;
              for (MapleCharacter chr : c.getPlayer().getMap().getAllChracater()) {
                if (chr.getBattleGroundChr() != null && chr.getId() != chred.getChr().getId() && 
                  chr.getBattleGroundChr().isAlive())
                  alivechr++; 
              } 
              int rank = alivechr + 2;
              c.getPlayer().addKV("BattlePVPRank", rank + "");
              c.getPlayer().addKV("BattlePVPLevel", c.getPlayer().getBattleGroundChr().getLevel() + "");
              c.getPlayer().addKV("BattlePVPKill", c.getPlayer().getBattleGroundChr().getKill() + "");
              if (alivechr == 0) {
                chred.getChr().addKV("BattlePVPRank", "1");
                chred.getChr().addKV("BattlePVPLevel", chred.getLevel() + "");
                chred.getChr().addKV("BattlePVPKill", chred.getKill() + "");
                chred.getChr().getClient().send(CField.environmentChange("Map/Effect2.img/PvP/Win", 16));
                chred.getChr().getClient().send(SLFCGPacket.playSE("Sound/MiniGame.img/BattlePvp/Win"));
                Timer.EtcTimer.getInstance().schedule(() -> {
                      c.getPlayer().getMap().broadcastMessage(CField.UIPacket.detailShowInfo("게임이 종료 되었습니다 잠시 후 퇴장맵으로 이동 됩니다.", 3, 20, 20));
                      c.getPlayer().getMap().broadcastMessage(CField.getClock(5));
                    },2000L);
                Timer.EtcTimer.getInstance().schedule(() -> {
                      c.getPlayer().getMap().resetFully();
                      for (MapleCharacter chr : c.getPlayer().getMap().getAllChracater()) {
                        chr.dispel();
                        chr.warp(921174002);
                      } 
                      MapleBattleGroundCharacter.bchr.clear();
                    },5000L);
              } 
            } 
            c.getPlayer().getBattleGroundChr().setDeath(c.getPlayer().getBattleGroundChr().getDeath() + 1);
            chred.getChr().getClient().send(SLFCGPacket.PoloFrittoEffect(30, "Map/Effect2/PvP/Kill"));
            c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("[" + chred.getName() + "]님이 [" + c.getPlayer().getName() + "]님을 제압 했습니다."));
            MapleBattleGroundCharacter custom = chred;
            custom.setTeam(2);
            chred.getChr().getMap().broadcastMessage(chred.getChr(), BattleGroundPacket.UpdateAvater(custom, GameConstants.BattleGroundJobType(custom)), false);
            chred.getChr().getClient().send(BattleGroundPacket.UpdateAvater(chred, GameConstants.BattleGroundJobType(chred)));
            custom = c.getPlayer().getBattleGroundChr();
            custom.setTeam(2);
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), BattleGroundPacket.UpdateAvater(custom, GameConstants.BattleGroundJobType(custom)), false);
            c.send(BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), GameConstants.BattleGroundJobType(c.getPlayer().getBattleGroundChr())));
          } 
        } 
        boolean debuff = true;
        if (chred.getChr().getBuffedValue(80001740)) {
          debuff = false;
          int heal = chred.getMaxHp() / 100 * 10;
          damage = -heal;
          chred.getChr().setSkillCustomInfo(80001740, chred.getChr().getSkillCustomValue0(80001740) - 1L, 0L);
          chred.getChr().getMap().broadcastMessage(BattleGroundPacket.TakeDamageEffect(chred, (int)chred.getChr().getSkillCustomValue0(80001740)));
          if (chred.getChr().getSkillCustomValue0(80001740) <= 0L) {
            chred.getChr().removeSkillCustomInfo(80001740);
            while (chred.getChr().getBuffedValue(80001740))
              chred.getChr().cancelEffect(chred.getChr().getBuffedEffect(80001740)); 
          } 
        } 
        if (chred.getChr().getSkillCustomValue(80001736) != null)
          debuff = false; 
        MapleBattleGroundMobInfo minfo2 = new MapleBattleGroundMobInfo(oid, cid, skid, damage, slea.readInt(), slea.readInt(), slea.readInt(), slea.readByte(), slea.readIntPos(), slea.readIntPos());
        if (!BattleGroundGameHandler.isNotDamage()) {
          if (cri)
            minfo2.setCritiCal(cri); 
          minfo.add(minfo2);
          c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.AttackDamage(c.getPlayer(), pos1, pos2, pos3, pos4, pos5, minfo, true, new int[] { 
                  attackcount, skillid, unk1, unk2, unk, unk4, unk5, unk6, unk7, unk8, 
                  unk9, unk10, unk11, unk12, 1 }));
          chred.setHp(chred.getHp() - damage);
          chred.getChr().getMap().broadcastMessage(CField.updatePartyMemberHP(chred.getChr().getId(), chred.getHp(), chred.getMaxHp()));
          Map<MapleStat, Long> statup = new EnumMap<>(MapleStat.class);
          statup.put(MapleStat.HP, Long.valueOf(chred.getHp()));
          chred.getChr().getClient().send(CWvsContext.updatePlayerStats(statup, false, chred.getChr()));
          if (chred.getHp() <= 0 && chred.isAlive()) {
            if (skillid == 80002342)
              c.send(BattleGroundPacket.CoolDown(skillid, 0, 0)); 
            chred.getChr().cancelAllBuffs();
            chred.getChr().cancelAllDebuffs();
            chred.setAlive(false);
            c.getPlayer().getBattleGroundChr().setKill(c.getPlayer().getBattleGroundChr().getKill() + 1);
            chred.setDeath(chred.getDeath() + 1);
            c.send(SLFCGPacket.PoloFrittoEffect(29, "Map/Effect2/PvP/Kill"));
            c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("[" + c.getPlayer().getName() + "]님이 [" + chred.getName() + "]님을 제압 했습니다."));
            if (!BattleGroundGameHandler.isEndOfGame()) {
              c.getPlayer().getBattleGroundChr().setMoney(c.getPlayer().getBattleGroundChr().getMoney() + 500);
            } else {
              chred.getChr().getClient().send(CField.environmentChange("Map/Effect2.img/PvP/Lose", 16));
              chred.getChr().getClient().send(SLFCGPacket.playSE("Sound/MiniGame.img/BattlePvp/Lose"));
              int alivechr = 0;
              for (MapleCharacter chr : c.getPlayer().getMap().getAllChracater()) {
                if (chr.getBattleGroundChr() != null && chr.getId() != c.getPlayer().getId() && 
                  chr.getBattleGroundChr().isAlive())
                  alivechr++; 
              } 
              int rank = alivechr + 2;
              chred.getChr().addKV("BattlePVPRank", rank + "");
              chred.getChr().addKV("BattlePVPLevel", chred.getLevel() + "");
              chred.getChr().addKV("BattlePVPKill", chred.getKill() + "");
              if (alivechr == 0) {
                c.getPlayer().addKV("BattlePVPRank", "1");
                c.getPlayer().addKV("BattlePVPLevel", c.getPlayer().getBattleGroundChr().getLevel() + "");
                c.getPlayer().addKV("BattlePVPKill", c.getPlayer().getBattleGroundChr().getKill() + "");
                c.send(CField.environmentChange("Map/Effect2.img/PvP/Win", 16));
                c.send(SLFCGPacket.playSE("Sound/MiniGame.img/BattlePvp/Win"));
                c.getPlayer().getMap().broadcastMessage(SLFCGPacket.playSE("Sound/MiniGame.img/BattlePvp/Win"));
                Timer.EtcTimer.getInstance().schedule(() -> {
                      c.getPlayer().getMap().broadcastMessage(CField.UIPacket.detailShowInfo("게임이 종료 되었습니다 잠시 후 퇴장맵으로 이동 됩니다.", 3, 20, 20));
                      c.getPlayer().getMap().broadcastMessage(CField.getClock(5));
                    },2000L);
                Timer.EtcTimer.getInstance().schedule(() -> {
                      c.getPlayer().getMap().resetFully();
                      List<MapleBattleGroundCharacter> remove = new ArrayList<>();
                      for (MapleCharacter chr : c.getPlayer().getMap().getAllChracater()) {
                        chr.dispel();
                        chr.warp(921174002);
                        if (chr.getBattleGroundChr() != null)
                          remove.add(chr.getBattleGroundChr()); 
                      } 
                      MapleBattleGroundCharacter.bchr.clear();
                    },5000L);
              } 
            } 
            MapleBattleGroundCharacter custom = chred;
            custom.setTeam(2);
            chred.getChr().getMap().broadcastMessage(chred.getChr(), BattleGroundPacket.UpdateAvater(custom, GameConstants.BattleGroundJobType(custom)), false);
            custom.setTeam(1);
            chred.getChr().getClient().send(BattleGroundPacket.UpdateAvater(chred, GameConstants.BattleGroundJobType(chred)));
            custom = c.getPlayer().getBattleGroundChr();
            custom.setTeam(2);
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), BattleGroundPacket.UpdateAvater(custom, GameConstants.BattleGroundJobType(custom)), false);
            c.getPlayer().getBattleGroundChr().setTeam(1);
            c.send(BattleGroundPacket.UpdateAvater(c.getPlayer().getBattleGroundChr(), GameConstants.BattleGroundJobType(c.getPlayer().getBattleGroundChr())));
          } else if (chred.getChr().getBuffedValue(SecondaryStat.NoDebuff) == null && debuff) {
            switch (skillid) {
              case 80001675:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(c.getPlayer(), chred.getChr()); 
                break;
              case 80001676:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 2000); 
                break;
              case 80001658:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 2000); 
                break;
              case 80001649:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 1000); 
                break;
              case 80003003:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 1500); 
                break;
              case 80003005:
              case 80003012:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 3000); 
                break;
              case 80003004:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(80003004).getEffect(1).applyTo(chred.getChr(), 10000); 
                break;
              case 80001732:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(80001732).getEffect(1).applyTo(chred.getChr(), 1000); 
                break;
              case 80001735:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 2000); 
                break;
              case 80002340:
                if (!chred.getChr().getBuffedValue(skillid))
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 1000); 
                break;
              case 80002338:
                if (chred.getChr().getBuffedEffect(SecondaryStat.Stun, skillid) == null)
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 1000); 
                break;
              case 80002344:
                SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 2000);
                break;
              case 80002673:
                if (c.getPlayer().getSkillCustomValue0(skillid) == 1L)
                  SkillFactory.getSkill(skillid).getEffect(1).applyTo(chred.getChr(), 3000); 
                c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) - 1L, 0L);
                break;
            } 
          } 
        } 
      } 
    } 
    mobcount = slea.readInt();
    for (i = 0; i < mobcount; i++) {
      effect = SkillFactory.getSkill(skillid).getEffect(skilllv);
      int basedam = Randomizer.rand(c.getPlayer().getBattleGroundChr().getMindam(), c.getPlayer().getBattleGroundChr().getMaxdam());
      int damper = effect.getDamage();
      if (skillid == 80001732) {
        if (c.getPlayer().getSkillCustomValue0(skillid) == 1L)
          damper = 50; 
        c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) - 1L, 0L);
      } else if (skillid == 80001734) {
        if (c.getPlayer().getSkillCustomValue0(skillid) == 2L)
          damper = 250; 
        c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) - 1L, 0L);
      } else if (skillid == 80002342) {
        if (c.getPlayer().getSkillCustomValue0(skillid) == 2L) {
          damper = 150;
        } else {
          damper = 400;
        } 
        c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) - 1L, 0L);
      } 
      damage = basedam * damper / 100;
      boolean cri = false;
      if (Randomizer.isSuccess(c.getPlayer().getBattleGroundChr().getCritical())) {
        damage *= 2;
        cri = true;
      } 
      slea.skip(27);
      int skid = slea.readInt();
      slea.readByte();
      int cid = slea.readInt();
      int oid = slea.readInt();
      monster = c.getPlayer().getMap().getMonsterByOid(oid);
      if (monster != null) {
        int dam;
        if (c.getPlayer().getBattleGroundChr().getJobType() == 6 && 
          monster.isBuffed(80001732))
          damage += damage / 100 * 10; 
        if (monster.getStats().getLevel() > c.getPlayer().getBattleGroundChr().getLevel()) {
          int chi = monster.getStats().getLevel() - c.getPlayer().getBattleGroundChr().getLevel();
          int percent = (chi == 1) ? 85 : ((chi == 2) ? 70 : ((chi == 3) ? 50 : ((chi == 4) ? 40 : ((chi == 5) ? 25 : ((chi == 6) ? 10 : 0)))));
          if (chi >= 7) {
            damage = 1;
          } else {
            damage = damage * percent / 100;
          } 
        } 
        List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<>();
        switch (skillid) {
          case 80001649:
            applys.add(new Pair<>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, effect.getSubTime(), 1L)));
            break;
          case 80001675:
            dam = (c.getPlayer().getBattleGroundChr().getLevel() >= 11) ? 60 : ((c.getPlayer().getBattleGroundChr().getLevel() >= 9) ? 50 : ((c.getPlayer().getBattleGroundChr().getLevel() >= 7) ? 40 : ((c.getPlayer().getBattleGroundChr().getLevel() >= 5) ? 30 : 20)));
            applys.add(new Pair<>(MonsterStatus.MS_Burned, new MonsterStatusEffect(skillid, 10000, dam)));
            break;
          case 80001676:
            effect = SkillFactory.getSkill(80001655).getEffect(1);
            applys.add(new Pair<>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, 2000, 1L)));
            break;
          case 80003003:
            applys.add(new Pair<>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, 1500, 1L)));
            break;
          case 80003005:
          case 80003012:
            applys.add(new Pair<>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, 3000, 1L)));
            break;
          case 80001658:
            applys.add(new Pair<>(MonsterStatus.MS_Speed, new MonsterStatusEffect(skillid, 2000, -80L)));
            break;
          case 80002340:
            if (monster.getBuff(skillid) == null)
              applys.add(new Pair<>(MonsterStatus.MS_Speed, new MonsterStatusEffect(skillid, 1000, -50L))); 
            break;
          case 80003004:
            applys.add(new Pair<>(MonsterStatus.MS_Speed, new MonsterStatusEffect(skillid, 10000, -50L)));
            break;
          case 80001732:
            applys.add(new Pair<>(MonsterStatus.MS_PvPHelenaMark, new MonsterStatusEffect(skillid, 4000, 10L)));
            break;
          case 80001735:
            applys.add(new Pair<>(MonsterStatus.MS_Speed, new MonsterStatusEffect(skillid, 2000, -30L)));
            break;
          case 80002338:
            if (monster.getBuff(MonsterStatus.MS_Stun) == null) {
              if (monster.getBuff(MonsterStatus.MS_PVPRude_Stack) == null && monster.getCustomValue0(skillid) > 0L)
                monster.removeCustomInfo(skillid); 
              monster.setCustomInfo(skillid, (int)monster.getCustomValue0(skillid) + 1, 0);
              if (monster.getCustomValue0(skillid) >= 3L) {
                monster.removeCustomInfo(skillid);
                monster.cancelSingleStatus(monster.getBuff(skillid), skillid);
                applys.add(new Pair<>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, 2000, 1L)));
                break;
              } 
              applys.add(new Pair<>(MonsterStatus.MS_PVPRude_Stack, new MonsterStatusEffect(skillid, 6000, monster.getCustomValue0(skillid))));
            } 
            break;
        } 
        if (applys != null && effect != null)
          monster.applyStatus(c, applys, effect); 
        minfo.clear();
        MapleBattleGroundMobInfo minfo2 = new MapleBattleGroundMobInfo(oid, cid, skid, damage, slea.readInt(), slea.readInt(), slea.readInt(), slea.readByte(), slea.readIntPos(), slea.readIntPos());
        if (cri)
          minfo2.setCritiCal(cri); 
        minfo.add(minfo2);
        c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.AttackDamage(c.getPlayer(), pos1, pos2, pos3, pos4, pos5, minfo, false, new int[] { 
                attackcount, skillid, unk1, unk2, unk, unk4, unk5, unk6, unk7, unk8, 
                unk9, unk10, unk11, unk12, 0 }));
        c.getPlayer().checkMonsterAggro(monster);
        monster.damage(c.getPlayer(), damage, true);
      } 
    } 
    if (skillid == 80003001 || skillid == 80002339)
      c.send(BattleGroundPacket.BonusAttack(skillid)); 
  }
  
  public static void TakeDamage(LittleEndianAccessor slea, MapleClient c) {
    Map<MapleStat, Long> hpmpupdate = new EnumMap<>(MapleStat.class);
    int randdam = 0;
    int oid = slea.readInt();
    slea.readInt();
    int type = slea.readInt();
    MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(oid);
    if (monster != null) {
      randdam = Randomizer.rand(monster.getStats().getPhysicalAttack() - 30, monster.getStats().getPhysicalAttack());
      if (c.getPlayer().getBuffedValue(80001655) && c.getPlayer().getSkillCustomValue0(80001655) > 0L) {
        int shield = (int)c.getPlayer().getSkillCustomValue0(80001655);
        if (shield - randdam > 0) {
          c.getPlayer().setSkillCustomInfo(80001655, c.getPlayer().getSkillCustomValue0(80001655) - randdam, 0L);
          randdam = 1;
        } else {
          randdam -= shield;
          c.getPlayer().removeSkillCustomInfo(80001655);
          while (c.getPlayer().getBuffedValue(80001655))
            c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(80001655)); 
        } 
      } 
      if (c.getPlayer().getBuffedValue(80001654)) {
        randdam -= 100;
      } else if (c.getPlayer().getBuffedValue(80001733)) {
        randdam -= randdam / 100 * 30;
      } else if (c.getPlayer().getBuffedValue(80002672)) {
        int bandam = randdam / 100 * 40;
        c.getPlayer().getMap().broadcastMessage(MobPacket.damageMonster(oid, bandam, false));
        monster.damage(c.getPlayer(), bandam, true);
      } 
      if (randdam <= 0)
        randdam = 1; 
      boolean healed = false;
      if (c.getPlayer().getBuffedValue(80001740) && c.getPlayer().getSkillCustomValue0(80001740) > 0L) {
        healed = true;
        int heal = c.getPlayer().getBattleGroundChr().getMaxHp() / 100 * 10;
        randdam = 1;
        randdam = -heal;
      } 
      c.getPlayer().getBattleGroundChr().setHp(c.getPlayer().getBattleGroundChr().getHp() - randdam);
      hpmpupdate.put(MapleStat.HP, Long.valueOf(c.getPlayer().getBattleGroundChr().getHp()));
      if (hpmpupdate.size() > 0)
        c.getSession().writeAndFlush(CWvsContext.updatePlayerStats(hpmpupdate, false, c.getPlayer())); 
      c.getPlayer().getMap().broadcastMessage(CField.updatePartyMemberHP(c.getPlayer().getId(), c.getPlayer().getBattleGroundChr().getHp(), c.getPlayer().getBattleGroundChr().getMaxHp()));
      if (c.getPlayer().getBuffedValue(80001740) && healed) {
        int heal = c.getPlayer().getBattleGroundChr().getMaxHp() / 100 * 10;
        randdam = 0;
        c.getPlayer().setSkillCustomInfo(80001740, c.getPlayer().getSkillCustomValue0(80001740) - 1L, 0L);
        c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.TakeDamage(c.getPlayer(), oid, randdam, type, heal));
        c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.TakeDamageEffect(c.getPlayer().getBattleGroundChr(), (int)c.getPlayer().getSkillCustomValue0(80001740)));
        if (c.getPlayer().getSkillCustomValue0(80001740) <= 0L) {
          c.getPlayer().removeSkillCustomInfo(80001740);
          while (c.getPlayer().getBuffedValue(80001740))
            c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(80001740)); 
        } 
      } else {
        c.getPlayer().getMap().broadcastMessage(BattleGroundPacket.TakeDamage(c.getPlayer(), oid, randdam, type, 0));
      } 
    } 
  }
  
  public static void MainTime(LittleEndianAccessor slea, MapleClient c) {
    if (c.getPlayer().getBattleGroundChr() != null && 
      c.getPlayer().getBattleGroundChr().getHp() > 0) {
      boolean heal = true;
      Map<MapleStat, Long> hpmpupdate = new EnumMap<>(MapleStat.class);
      if (c.getPlayer().getBuffedValue(80002341))
        c.getPlayer().getBattleGroundChr().setHp(c.getPlayer().getBattleGroundChr().getHp() + c.getPlayer().getBattleGroundChr().getMaxHp() / 100 * 3); 
      if (c.getPlayer().getBuffedValue(80002671))
        c.getPlayer().getBattleGroundChr().setHp(c.getPlayer().getBattleGroundChr().getHp() + c.getPlayer().getBattleGroundChr().getMaxHp() / 100 * 8); 
      if (heal) {
        if (c.getPlayer().getBattleGroundChr().getHp() < c.getPlayer().getBattleGroundChr().getMaxHp())
          c.getPlayer().getBattleGroundChr().setHp(c.getPlayer().getBattleGroundChr().getHp() + c.getPlayer().getBattleGroundChr().getHpRegen()); 
        if (c.getPlayer().getBattleGroundChr().getMp() < c.getPlayer().getBattleGroundChr().getMaxMp())
          c.getPlayer().getBattleGroundChr().setMp(c.getPlayer().getBattleGroundChr().getMp() + c.getPlayer().getBattleGroundChr().getMpRegen()); 
        if (c.getPlayer().getBattleGroundChr().getMp() > c.getPlayer().getBattleGroundChr().getMaxMp())
          c.getPlayer().getBattleGroundChr().setMp(c.getPlayer().getBattleGroundChr().getMaxMp()); 
        if (c.getPlayer().getBattleGroundChr().getHp() > c.getPlayer().getBattleGroundChr().getMaxHp())
          c.getPlayer().getBattleGroundChr().setHp(c.getPlayer().getBattleGroundChr().getMaxHp()); 
      } 
      hpmpupdate.put(MapleStat.HP, Long.valueOf(c.getPlayer().getBattleGroundChr().getHp()));
      hpmpupdate.put(MapleStat.MP, Long.valueOf(c.getPlayer().getBattleGroundChr().getMp()));
      if (hpmpupdate.size() > 0)
        c.getSession().writeAndFlush(CWvsContext.updatePlayerStats(hpmpupdate, false, c.getPlayer())); 
      c.getPlayer().getMap().broadcastMessage(CField.updatePartyMemberHP(c.getPlayer().getId(), c.getPlayer().getBattleGroundChr().getHp(), c.getPlayer().getBattleGroundChr().getMaxHp()));
    } 
  }
  
  public static void UpgradeSkill(LittleEndianAccessor slea, MapleClient c) {
    MapleBattleGroundCharacter chr = c.getPlayer().getBattleGroundChr();
    if (chr == null)
      return; 
    int skilllevel = 0, need = 0;
    int slot = slea.readInt();
    slea.skip(4);
    slea.skip(4);
    skilllevel = (slot == 0) ? chr.getAttackUp() : ((slot == 1) ? chr.getHpMpUp() : ((slot == 2) ? chr.getCriUp() : ((slot == 3) ? chr.getSpeedUp() : ((slot == 4) ? chr.getRegenUp() : 0))));
    switch (skilllevel) {
      case 1:
        need = 460;
        break;
      case 2:
        need = 840;
        break;
      case 3:
        need = 1360;
        break;
      case 4:
        need = 1980;
        break;
      case 5:
        need = 2800;
        break;
    } 
    if (chr != null && chr.getHp() > 0 && need <= chr.getMoney()) {
      if (slot == 0) {
        int attackup = (chr.getAttackUp() == 1) ? 2 : ((chr.getAttackUp() == 2) ? 5 : ((chr.getAttackUp() == 3) ? 10 : ((chr.getAttackUp() == 4) ? 15 : ((chr.getAttackUp() == 5) ? 20 : 0))));
        chr.setSkillMinDamUp(chr.getMindam() / 100 * attackup);
        chr.setSkillMaxDamUp(chr.getMaxdam() / 100 * attackup);
        chr.setAttackUp(chr.getAttackUp() + 1);
      } else if (slot == 1) {
        int hpmpup = (chr.getHpMpUp() == 1) ? 2 : ((chr.getHpMpUp() == 2) ? 7 : ((chr.getHpMpUp() == 3) ? 15 : ((chr.getHpMpUp() == 4) ? 20 : ((chr.getHpMpUp() == 5) ? 30 : 0))));
        chr.setSkillHPUP(chr.getMaxHp() / 100 * hpmpup);
        chr.setSkillMPUP(chr.getMaxMp() / 100 * hpmpup);
        chr.setHpMpUp(chr.getHpMpUp() + 1);
      } else if (slot == 2) {
        int criup = (chr.getCriUp() == 1) ? 3 : ((chr.getCriUp() == 2) ? 6 : ((chr.getCriUp() == 3) ? 12 : ((chr.getCriUp() == 4) ? 18 : ((chr.getCriUp() == 5) ? 25 : 0))));
        chr.setCritical(criup);
        chr.setCriUp(chr.getCriUp() + 1);
      } else if (slot == 3) {
        int attackspeed = (chr.getSpeedUp() == 5) ? -20 : -10;
        int speed = (chr.getSpeedUp() == 1) ? 6 : ((chr.getSpeedUp() == 2) ? 6 : ((chr.getSpeedUp() == 3) ? 12 : ((chr.getSpeedUp() == 4) ? 12 : ((chr.getSpeedUp() == 5) ? 12 : 0))));
        int jump = (chr.getSpeedUp() == 1) ? 2 : ((chr.getSpeedUp() == 2) ? 4 : ((chr.getSpeedUp() == 3) ? 2 : ((chr.getSpeedUp() == 4) ? 4 : ((chr.getSpeedUp() == 5) ? 3 : 0))));
        chr.setSpeedUp(chr.getSpeedUp() + 1);
        chr.setAttackSpeed(chr.getAttackSpeed() + attackspeed);
        chr.setSpeed(chr.getSpeed() + speed);
        chr.setJump(chr.getJump() + jump);
      } else if (slot == 4) {
        int hpup = (chr.getHpMpUp() == 1) ? 14 : ((chr.getHpMpUp() == 2) ? 17 : ((chr.getHpMpUp() == 3) ? 20 : ((chr.getHpMpUp() == 4) ? 23 : ((chr.getHpMpUp() == 5) ? 25 : 0))));
        int mpup = (chr.getHpMpUp() == 1) ? 6 : ((chr.getHpMpUp() == 2) ? 7 : ((chr.getHpMpUp() == 3) ? 8 : ((chr.getHpMpUp() == 4) ? 10 : ((chr.getHpMpUp() == 5) ? 11 : 0))));
        chr.setRegenUp(chr.getRegenUp() + 1);
        chr.setHpRegen(chr.getHpRegen() + hpup);
        chr.setMpRegen(chr.getMpRegen() + mpup);
      } 
      chr.setMoney(chr.getMoney() - need);
      c.send(BattleGroundPacket.UpgradeMainSkill(chr));
      chr.getChr().getMap().broadcastMessage(BattleGroundPacket.UpgradeSkillEffect(chr, slot * 100 + skilllevel + 1));
      c.send(BattleGroundPacket.UpdateAvater(chr, GameConstants.BattleGroundJobType(chr)));
    } 
  }
  
  public static void Respawn(LittleEndianAccessor slea, MapleClient c) {
    if (c.getPlayer().getBattleGroundChr() != null)
      server.Timer.MapTimer.getInstance().schedule(() -> {
          
          },1000L); 
  }
  
  public static void SelectAvater(LittleEndianAccessor slea, MapleClient c) {
    MapleCharacter chr = c.getPlayer();
    if (chr.getBattleGroundChr() == null) {
      int type = slea.readInt();
      String job = (type == 1) ? "만지" : ((type == 2) ? "마이크" : ((type == 3) ? "다크로드" : ((type == 4) ? "하인즈" : ((type == 5) ? "무공" : ((type == 6) ? "헬레나" : ((type == 7) ? "랑이" : ((type == 9) ? "류드" : ((type == 10) ? "웡키" : ((type == 11) ? "폴로&프리토" : "없음")))))))));
      chr.setBattleGrondJobName(job);
      MapleBattleGroundCharacter gchr = new MapleBattleGroundCharacter(chr, chr.getBattleGrondJobName());
      gchr.setDeathcount(3);
      chr.getMap().broadcastMessage(BattleGroundPacket.ChangeAvater(gchr, GameConstants.BattleGroundJobType(gchr)));
      chr.getClient().send(BattleGroundPacket.UpgradeMainSkill(gchr));
      chr.getClient().send(BattleGroundPacket.AvaterSkill(gchr, GameConstants.BattleGroundJobType(gchr)));
      chr.getClient().send(BattleGroundPacket.SelectAvaterOther(chr, 1, 1));
      MapleBattleGroundCharacter t = gchr;
      t.setTeam(2);
      chr.getMap().broadcastMessage(chr, BattleGroundPacket.ChangeAvater(t, GameConstants.BattleGroundJobType(t)), false);
      chr.getClient().send(CField.ImageTalkNpc(9001153, 4000, "싱크로 완료. 몬스터와 보스 몬스터를 처치해 더욱 강해질 수 있습니다.\r\n게임시작 후 1분간은 #r무적#k 상태 입니다."));
    } 
  }
}
