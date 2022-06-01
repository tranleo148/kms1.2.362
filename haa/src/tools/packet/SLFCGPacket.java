package tools.packet;

import client.AvatarLook;
import client.DreamBreakerRank;
import client.MapleCharacter;
import handling.SendPacketOpcode;
import handling.channel.handler.PlayerInteractionHandler;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import server.Obstacle;
import server.Randomizer;
import server.events.MapleTyoonKitchen;
import server.games.BattleReverse;
import server.games.ColorInvitationCard;
import server.games.MultiYutGame;
import server.games.OneCardGame;
import server.polofritto.MapleRandomPortal;
import tools.HexTool;
import tools.Pair;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;

public class SLFCGPacket {
  public static final byte[] SendPacket(short nType, String iPacket) {
    MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
    w.writeShort(nType);
    w.write(HexTool.getByteArrayFromHexString(iPacket));
    return w.getPacket();
  }
  
  public static byte[] WeatherAddPacket(int type) {
    MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
    w.writeShort(SendPacketOpcode.FieldWeather_Add.getValue());
    w.writeInt(type);
    return w.getPacket();
  }
  
  public static byte[] WeatherRemovePacket(int type) {
    MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
    w.writeShort(SendPacketOpcode.FieldWeather_Remove.getValue());
    w.writeInt(type);
    return w.getPacket();
  }
  
  public static byte[] SetupZodiacInfo() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZodiacInfo.getValue());
        mplew.writeInt(0);
        mplew.write(1);
        mplew.write(HexTool.getByteArrayFromHexString("A0 96 97 C6 D4 09 00 00"));
        return mplew.getPacket();
    }

  public static byte[] ZodiacRankInfo(int cid, int rank) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZodiacRankInfo.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(0);
        mplew.writeInt(rank);

        return mplew.getPacket();
    }
  
  public static byte[] BlockGameCommandPacket(int command) {
    MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
    w.writeShort(SendPacketOpcode.BlockGameCommand.getValue());
    w.writeInt(command);
    return w.getPacket();
  }
  
//  public static byte[] BlockGameControlPacket(int velocity, int misplaceallowance) {
//    MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
//    w.writeShort(SendPacketOpcode.BlockGameControl.getValue());
//    w.writeZeroBytes(12);
//    w.writeInt(1);
//    w.writeInt(0);
//    w.writeInt(velocity);
//    w.writeInt(misplaceallowance);
//    w.writeZeroBytes(25);
//    return w.getPacket();
//  }
  
      public static byte[] BlockGameControlPacket(int velocity, int misplaceallowance) {
        final MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
        w.writeShort(SendPacketOpcode.BlockGameControl.getValue());
        w.writeInt(0);
        w.writeInt(0);
        w.writeInt(0);
        w.writeInt(0);
        w.writeInt(1);

        w.writeInt(0); // ?
        w.writeInt(velocity);// velocity(%)
        w.writeInt(misplaceallowance); // missplace allowance
        w.writeInt(0);
        w.writeInt(0);
        w.writeInt(0);
        w.writeInt(0);
        w.writeInt(0);
        return w.getPacket();
    }
  
  public static byte[] FrozenLinkMobCount(int count) {
    MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
    w.writeShort(SendPacketOpcode.FROZEN_LINK.getValue());
    w.write(1);
    w.writeInt(count);
    w.writeInt(0);
    return w.getPacket();
  }
  
  public static byte[] MesoChairPacket(int charid, int meso, int chairid) {
    MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
    w.writeShort(SendPacketOpcode.UserMesoChairAddMeso.getValue());
    w.writeInt(charid);
    w.writeInt(chairid);
    w.writeLong(meso);
    w.writeLong(meso);
    return w.getPacket();
  }
  
  public static byte[] TowerChairSaveDone() {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.UserTowerChairSettingResult.getValue());
    return packet.getPacket();
  }
  
  public static byte[] OXQuizCountdown(int time) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.HOxQuizCountEffect.getValue());
    packet.writeInt(time);
    return packet.getPacket();
  }
  
  public static byte[] OXQuizPlainText(List<String> texts) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.HOxQuizEnter.getValue());
    packet.writeInt(texts.size());
    for (String a : texts)
      packet.writeMapleAsciiString(a); 
    return packet.getPacket();
  }
  
  public static byte[] OXQuizQuestion(String text, int index, int leftquestion) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.HOxQuizQuestions.getValue());
    packet.writeInt(index);
    packet.writeInt(leftquestion + 1);
    packet.writeMapleAsciiString(text);
    packet.writeZeroBytes(5);
    return packet.getPacket();
  }
  
  public static byte[] OXQuizExplain(String text) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.HOxQuizExplan.getValue());
    packet.writeInt(0);
    packet.writeMapleAsciiString(text);
    return packet.getPacket();
  }
  
  public static byte[] OXQuizResult(boolean isX) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.HOxQuizResult.getValue());
    packet.writeInt(1);
    packet.writeInt(1);
    packet.writeInt(isX ? 0 : 1);
    return packet.getPacket();
  }
  
  public static byte[] OXQuizTelePort(byte point) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.HOxQuizMoveToPortal.getValue());
    packet.write(point);
    return packet.getPacket();
  }
  
  public static byte[] BingoUI(int type, int round) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.BingoGameState.getValue());
    packet.writeInt(type);
    packet.writeInt(round);
    packet.writeInt(5);
    packet.writeInt(1);
    return packet.getPacket();
  }
  
  public static byte[] BingoInit(int[][] table) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.EnterBingoGame.getValue());
    packet.writeInt(1);
    packet.writeInt(1);
    packet.writeInt(0);
    packet.writeInt(5);
    packet.writeInt(5);
    packet.writeInt(1);
    packet.write(1);
    packet.writeInt(1);
    packet.writeInt(25);
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++)
        packet.writeInt(table[x][y]); 
    } 
    return packet.getPacket();
  }
  
  public static byte[] BingoHostNumberReady() {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.HostNumberReady.getValue());
    return packet.getPacket();
  }
  
  public static byte[] BingoHostNumber(int number, int leftcount) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.HostNumber.getValue());
    packet.writeInt(number);
    if (number <= 0) {
      packet.writeInt(leftcount);
    } else {
      packet.writeInt(0);
      packet.writeInt(leftcount);
    } 
    return packet.getPacket();
  }
  
  public static byte[] BingoCheckNumber(int number) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.CheckNumberAck.getValue());
    packet.writeInt(1);
    packet.writeInt(0);
    packet.writeInt(number);
    packet.writeZeroBytes(12);
    return packet.getPacket();
  }
  
  public static byte[] BingoDrawLine(int index, int type, int junk) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.CheckNumberAck.getValue());
    packet.writeInt(1);
    packet.writeInt(index);
    packet.writeInt(junk);
    packet.writeInt(0);
    packet.writeInt(1);
    packet.writeInt(type);
    return packet.getPacket();
  }
  
  public static byte[] BingoAddRank(MapleCharacter chr) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.AddBingoRank.getValue());
    packet.writeInt(1);
    packet.writeInt(chr.getId());
    packet.writeMapleAsciiString(chr.getName());
    packet.writeInt(0);
    packet.writeInt(1);
    packet.writeInt(0);
    return packet.getPacket();
  }
  
  public static byte[] playSE(String SE) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
    packet.write(5);
    packet.writeMapleAsciiString(SE);
    packet.writeInt((SE.equals("seren") || SE.contains("seren")) ? 200 : 100);
    return packet.getPacket();
  }
  
  public static byte[] PoloFrittoEffect(int type, String path) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
    packet.write(type);
    switch (type) {
      case 0:
        packet.write(15);
        packet.writeMapleAsciiString(path);
        packet.write(7);
        packet.write(1);
        break;
      case 1:
        packet.write(14);
        packet.writeMapleAsciiString(path);
        packet.writeInt(0);
        break;
      case 4:
        packet.writeMapleAsciiString(path);
        break;
      case 19:
        packet.writeMapleAsciiString(path);
        packet.writeInt(0);
        break;
      case 20:
        packet.writeMapleAsciiString(path);
        packet.write(7);
        packet.write(1);
        break;
      case 29:
        packet.writeMapleAsciiString(path);
        packet.write(2);
        break;
    } 
    packet.writeZeroBytes(100);
    return packet.getPacket();
  }
  
  public static byte[] PoloFrittoPortal(MapleRandomPortal portal) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.BEGIN_RANDOMPORTALPOOL.getValue());
    packet.write(portal.getPortalType());
    packet.writeInt(portal.getObjectId());
    packet.writePos(portal.getPos());
    packet.writeInt(portal.getMapId());
    packet.writeInt(portal.getCharId());
    return packet.getPacket();
  }
  
  public static byte[] RemovePoloFrittoPortal(MapleRandomPortal portal) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.RandomPortalRemoved.getValue());
    packet.write(portal.getPortalType());
    packet.writeInt(portal.getObjectId());
    packet.writeInt(portal.getMapId());
    return packet.getPacket();
  }
  
  public static byte[] milliTimer(int mil) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.CLOCK.getValue());
    packet.write(6);
    packet.writeInt(mil);
    return packet.getPacket();
  }
  
  public static byte[] setBountyHuntingStage(int stage) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.BOUNTY_HUNTING.getValue());
    packet.writeInt(stage);
    return packet.getPacket();
  }
  
  public static byte[] setTowerDefenseWave(int wave) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.TOWER_DEFENSE_WAVE.getValue());
    packet.writeInt(wave);
    return packet.getPacket();
  }
  
  public static byte[] setTowerDefenseLife(int life) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.TOWER_DEFENSE_LIFE.getValue());
    packet.writeInt(life);
    return packet.getPacket();
  }
  
  public static byte[] courtShipDanceState(int state) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.COURTSHIP_STATE.getValue());
    packet.writeInt(state);
    return packet.getPacket();
  }
  
    public static byte[] courtShipDanceCommand(List<List<Integer>> list) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.COURTSHIP_COMMAND.getValue());
        packet.writeInt(list.size());
        for (List<Integer> list1 : list) {
            packet.writeInt(list1.size());
            for (int list11 : list1) {
                packet.writeInt(list11);
            }
        }
        return packet.getPacket();
    }
  
  public static byte[] createGun() {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.CREATE_GUN.getValue());
    return packet.getPacket();
  }
  
  public static byte[] clearGun() {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.CLEAR_GUN.getValue());
    return packet.getPacket();
  }
  
  public static byte[] setGun() {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.SET_GUN.getValue());
    packet.writeMapleAsciiString("shotgun");
    packet.writeMapleAsciiString("shotgun");
    packet.writeInt(1);
    packet.writeInt(200);
    packet.writeRect(new Rectangle(-8, -8, 16, 16));
    return packet.getPacket();
  }
  
  public static byte[] setAmmo(int bullet) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.SET_AMMO.getValue());
    packet.writeInt(bullet);
    return packet.getPacket();
  }
  
  public static byte[] attackRes() {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.SHOOT_RESULT.getValue());
    packet.write(1);
    return packet.getPacket();
  }
  
  public static byte[] deadOnFPSMode(int objectId, int point) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.DEAD_FPS_MODE.getValue());
    packet.writeInt(objectId);
    packet.writeInt(point);
    return packet.getPacket();
  }
  
    public static byte[] StarDustUI(String path, long Point, long Coin, boolean lock) {
        /*  515 */ MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        /*  516 */ packet.writeShort(SendPacketOpcode.STARDUST_UI.getValue());
        /*  517 */ packet.write(1);
     //   packet.writeInt(100901);
     //   packet.writeInt(100898);
        /*  518 */ packet.writeInt(100794);
        /*  519 */ packet.writeInt(100790);
        /*  520 */ packet.writeMapleAsciiString(path);
        /*  521 */ packet.writeInt((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000 : 3000);
        /*  522 */ packet.writeInt(0);
        /*  523 */ packet.writeInt(0);
        /*  524 */ packet.writeShort(0);
        /*  525 */ packet.writeShort(16368);

        /*  527 */ packet.writeInt(0);
        /*  528 */ packet.writeInt(Coin);
        /*  529 */ packet.write(lock);
        /*  530 */ packet.writeLong(PacketHelper.getTime(150842304000000000L));

        /*  532 */ return packet.getPacket();
    }

    public static byte[] StarDustIncrease(int totalGauge, int add, boolean lock, int total, int pointAdd, Point point) {
        /*  536 */ MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        /*  537 */ packet.writeShort(SendPacketOpcode.STARDUST_INCREASE.getValue());
        /*  538 */ packet.writeInt(totalGauge);
        /*  539 */ packet.writeInt(add);
        /*  540 */ packet.write(lock);
        /*  541 */ packet.writeInt(total);
        /*  542 */ packet.writeInt(pointAdd);
        /*  543 */ packet.writeInt(7);

        /*  546 */ packet.writeInt(point.x);
        /*  547 */ packet.writeInt(point.y);

        /*  549 */ packet.writeInt(0);
        /*  550 */ packet.writeInt(add);
        /*  551 */ packet.writeInt(0);
        /*  552 */ packet.writeMapleAsciiString("블루밍 코인");

        /*  554 */ return packet.getPacket();
    }
  
  public static byte[] SpiritSavedEffect(int SpiritCount) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
    mplew.write(16);
    mplew.writeMapleAsciiString("Map/Effect3.img/savingSpirit/" + SpiritCount);
    return mplew.getPacket();
  }
  
  public static byte[] SpawnPartner(boolean bShow, int oid, int skillId) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SPAWN_PARTNER.getValue());
    mplew.write(bShow);
    mplew.writeInt(oid);
    mplew.writeInt(skillId);
    mplew.write(0);
    return mplew.getPacket();
  }
  
  public static byte[] OnYellowDlg(int npcid, int duraction, String title, String msg) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.YELLOW_DLG.getValue());
    mplew.writeInt(npcid);
    mplew.writeInt(duraction);
    mplew.writeMapleAsciiString(title);
    mplew.writeMapleAsciiString(msg);
    mplew.write(false);
    return mplew.getPacket();
  }
  
  public static byte[] DreamBreakerRanking(String name) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    int count = 1;
    packet.writeShort(SendPacketOpcode.DREAM_BREAKER_RANKING.getValue());
    packet.writeInt(0);
    packet.write(43);
    packet.writeZeroBytes(8);
    packet.writeInt(DreamBreakerRank.Rank.containsKey(name) ? ((Integer)DreamBreakerRank.Rank.get(name)).intValue() : 0);
    packet.writeInt(DreamBreakerRank.Rank.containsKey(name) ? DreamBreakerRank.getRank(name) : 0);
    packet.writeZeroBytes(16);
    packet.write(43);
    packet.writeInt((DreamBreakerRank.Rank.size() > 100) ? 100 : DreamBreakerRank.Rank.size());
    for (Map.Entry<String, Integer> info : DreamBreakerRank.Rank.entrySet()) {
      if (count == 101)
        break; 
      packet.writeZeroBytes(8);
      packet.writeInt(((Integer)info.getValue()).intValue());
      packet.writeInt(count);
      packet.writeMapleAsciiString(info.getKey());
      packet.write(false);
      count++;
    } 
    return packet.getPacket();
  }
  
  public static byte[] SetDreamBreakerUI(int stage) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DREAM_BREAKER.getValue());
    mplew.writeInt(3);
    mplew.writeInt(500);
    mplew.writeInt(180000);
    mplew.writeInt(stage);
    return mplew.getPacket();
  }
  
  public static byte[] DreamBreakerGaugePacket(int Gauge) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DREAM_BREAKER.getValue());
    mplew.writeInt(4);
    mplew.writeInt(Gauge);
    return mplew.getPacket();
  }
  
  public static byte[] DreamBreakerCountdown(int stage) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DREAM_BREAKER.getValue());
    mplew.writeInt(5);
    mplew.writeInt(stage);
    return mplew.getPacket();
  }
  
  public static byte[] DreamBreakerDisableTimer(boolean Disable, int Time) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DREAM_BREAKER.getValue());
    mplew.writeInt(6);
    mplew.write(Disable);
    mplew.writeInt(Time);
    return mplew.getPacket();
  }
  
  public static byte[] DreamBreakerResult(int ClearTime) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DREAM_BREAKER.getValue());
    mplew.writeInt(7);
    mplew.writeInt(ClearTime);
    return mplew.getPacket();
  }
  
  public static byte[] DreamBreakeLockSkill(int SkillCode) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DREAM_BREAKER.getValue());
    mplew.writeInt(8);
    mplew.writeInt(SkillCode);
    return mplew.getPacket();
  }
  
  public static byte[] DreamBreakeSkillRes() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DREAM_BREAKER.getValue());
    mplew.writeInt(9);
    return mplew.getPacket();
  }
  
  public static byte[] DreamBreakerMsg(String msg) {
    return DreamBreakerMsg(msg, false);
  }
  
  public static byte[] DreamBreakerMsg(String msg, boolean unk) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DETAIL_SHOW_INFO.getValue());
    mplew.writeInt(3);
    mplew.writeInt(20);
    mplew.writeInt(20);
    mplew.writeInt(0);
    mplew.write(unk);
    mplew.writeMapleAsciiString(msg);
    return mplew.getPacket();
  }
  
  public static byte[] EventSkillEffect(int skillid, int time) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SKILL_EFFECT.getValue());
    mplew.writeInt(skillid);
    mplew.writeInt(time);
    mplew.writeZeroBytes(100);
    return mplew.getPacket();
  }
  
  public static byte[] SkillfromMonsterEffect(int skillid, int subeffect, int x, int y) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
    mplew.write(80);
    mplew.writeInt(skillid);
    mplew.writeInt(subeffect);
    mplew.writeInt(x);
    mplew.writeInt(y);
    return mplew.getPacket();
  }
  
  public static byte[] EventSkillStart(int skillid, int time) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SKILL_START.getValue());
    mplew.writeInt(skillid);
    mplew.writeInt(time);
    mplew.writeZeroBytes(100);
    return mplew.getPacket();
  }
  
  public static byte[] EventSkillEnd(int skillid) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SKILL_END.getValue());
    mplew.writeInt(skillid);
    mplew.writeZeroBytes(100);
    return mplew.getPacket();
  }
  
  public static byte[] EventSkillSetCount(int skillid, int count) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SKILL_SET.getValue());
    mplew.writeInt(skillid);
    mplew.writeInt(count);
    mplew.writeZeroBytes(100);
    return mplew.getPacket();
  }
  
  public static byte[] HundredDetectiveGameExplain() {
    String[] msg = { "<어드벤처 암호추리>\n조금만 기다려 주세YO!", "YO! 보물이 가득! 어드벤처 아일랜드!\n보물 상자의 암호를 맞춰YO!", "상자의 키패드 클릭 클릭 YO!\n상자의 암호를 맞춰 맞춰 YO!", "숫자도 맞고 위치도 맞으면 ○!\n숫자만 맞으면 △!", "암호 입력은 10초에 한 번씩 YO!", "I say ○! U say △! ○! △! ○! △!", "준비되셨나YO!\n이제부터 당신도 암호추리의 대가YO!" };
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.HDetectiveGamePlainText.getValue());
    mplew.writeInt(msg.length);
    for (String s : msg)
      mplew.writeMapleAsciiString(s); 
    return mplew.getPacket();
  }
  
  public static byte[] HundredDetectiveGameReady(int Stage) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.HDetectiveGameSetGame.getValue());
    mplew.writeInt(3);
    mplew.writeInt(Stage);
    mplew.writeInt(3);
    mplew.writeInt(15);
    mplew.writeInt(10000);
    return mplew.getPacket();
  }
  
  public static byte[] HundredDetectiveGameControl(int type, int Stage) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.HDetectiveGameCommand.getValue());
    mplew.writeInt(type);
    mplew.writeInt(Stage);
    return mplew.getPacket();
  }
  
  public static byte[] HundredDetectiveGameAddRank(int cid, String name) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.HDetectiveGameAddRank.getValue());
    mplew.writeInt(cid);
    mplew.writeMapleAsciiString(name);
    return mplew.getPacket();
  }
  
  public static byte[] HundredDetectiveGameResult(int input, int result) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.HDetectiveGameResult.getValue());
    mplew.writeInt(input);
    mplew.writeInt(result);
    return mplew.getPacket();
  }
  
  public static byte[] HundredDetectiveReEnable(int attempt) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.HDetectiveGameClear.getValue());
    mplew.writeInt(attempt);
    return mplew.getPacket();
  }
  
  public static byte[] ShowWeb(String URL) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_WEB.getValue());
    mplew.writeMapleAsciiString(URL);
    return mplew.getPacket();
  }
  
  public static byte[] SendUserClientResolutionRequest() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.UserClientResolutionRequest.getValue());
    return mplew.getPacket();
  }
  
  public static byte[] ChangeVolume(int Volume, int FadeTime) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
    mplew.write(9);
    mplew.writeInt(Volume);
    mplew.writeInt(FadeTime);
    return mplew.getPacket();
  }
  
  public static byte[] PlatformerStageInfo(int Stage) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
    mplew.write(1);
    mplew.writeInt(34502);
    mplew.write(1);
    mplew.writeMapleAsciiString(String.valueOf(Stage));
    return mplew.getPacket();
  }
  
  public static byte[] PlatformerTimerInfo() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.UserTimerInfo.getValue());
    mplew.write(
        HexTool.getByteArrayFromHexString("02 00 00 00 02 00 00 00 80 96 98 00 00 00 00 00 80 96 98 00 C4 86 00 00"));
    return mplew.getPacket();
  }
  
  public static byte[] createObstaclePlatformer(Obstacle[] obs) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.CREATE_OBSTACLE.getValue());
    mplew.writeInt(0);
    mplew.writeInt(obs.length);
    mplew.write(4);
    mplew.writeInt(15);
    mplew.writeZeroBytes(12);
    mplew.writeInt(1600);
    for (Obstacle ob : obs) {
      mplew.write(true);
      mplew.writeInt(ob.getKey());
      mplew.writeInt(Randomizer.nextInt());
      mplew.writeInt((ob.getOldPosition()).x);
      mplew.writeInt((ob.getOldPosition()).y);
      mplew.writeInt((ob.getNewPosition()).x);
      mplew.writeInt((ob.getNewPosition()).y);
      mplew.writeInt(40);
      mplew.writeInt(ob.getRangeed());
      mplew.writeInt(ob.getTrueDamage());
      mplew.writeInt(ob.getDelay());
      mplew.writeInt(ob.getHeight());
      mplew.writeInt(ob.getVperSec());
      mplew.writeInt(ob.getMaxP());
      mplew.writeInt(ob.getLength());
      mplew.writeInt(ob.getAngle());
    } 
    return mplew.getPacket();
  }
  
  public static byte[] ClearObstacles() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.CLEAR_OBSTACLE.getValue());
    return mplew.getPacket();
  }
  
  public static byte[] CameraCtrl(int nType, int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.CameraCtrlMsg.getValue());
    mplew.write(nType);
    switch (nType) {
      case 11:
        mplew.writeInt(args[0]);
        break;
      case 13:
        mplew.write(args[0]);
        mplew.writeInt(args[1]);
        mplew.writeInt(args[2]);
        mplew.writeInt(args[3]);
        break;
      case 15:
        mplew.write(args[0]);
        mplew.writeInt(args[1]);
        mplew.writeInt(args[2]);
        break;
    } 
    return mplew.getPacket();
  }
  
  public static byte[] SetIngameDirectionMode(boolean Enable, boolean BlackFrame, boolean ForceMouseOver, boolean ShowUI) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SetInGameDirectionMode.getValue());
    mplew.write(Enable ? 1 : 0);
    mplew.write(BlackFrame ? 1 : 0);
    mplew.write(ForceMouseOver ? 1 : 0);
    mplew.write(ShowUI ? 1 : 0);
    return mplew.getPacket();
  }
  
  public static byte[] SetStandAloneMode(boolean Enable) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SetStandAloneMode.getValue());
    mplew.write(Enable ? 1 : 0);
    return mplew.getPacket();
  }
  
  public static byte[] InGameDirectionEvent(String str, int... args) {
    int i;
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.UserInGameDirectionEvent.getValue());
    mplew.write(args[0]);
    switch (args[0]) {
      case 0:
        mplew.writeInt(args[1]);
        mplew.writeInt(args[2]);
        break;
      case 1:
        mplew.writeInt(args[1]);
        break;
      case 2:
        mplew.writeMapleAsciiString(str);
        mplew.writeInt(args[1]);
        mplew.writeInt(args[2]);
        mplew.writeInt(args[3]);
        mplew.write(true);
        mplew.writeInt(args[4]);
        mplew.write(args[5]);
        if (args[5] > 0) {
          mplew.writeInt(args[6]);
          mplew.write(args[7]);
          mplew.write(args[8]);
        } 
        mplew.write(args[9]);
        if (args[9] > 0)
          mplew.writeMapleAsciiString(str); 
        break;
      case 3:
        mplew.writeInt(args[1]);
        break;
      case 4:
        mplew.writeMapleAsciiString(str);
        mplew.writeInt(args[1]);
        mplew.writeInt(args[2]);
        mplew.writeInt(args[3]);
        break;
      case 5:
        mplew.write((args[1] > 0) ? 1 : 0);
        mplew.writeInt(args[2]);
        mplew.write((args[3] > 0) ? 1 : 0);
        if (args[1] > 0 && args[3] > 0) {
          mplew.writeInt(args[4]);
          mplew.writeInt(args[5]);
        } 
        break;
      case 6:
        mplew.writeInt(args[1]);
        break;
      case 7:
        mplew.write(false);
        mplew.writeInt(args[1]);
        mplew.writeInt(args[2]);
        mplew.writeInt(args[3]);
        mplew.writeInt(args[4]);
        mplew.writeInt(args[5]);
        break;
      case 10:
        mplew.write(args[1]);
        break;
      case 11:
        mplew.writeInt(args[1]);
        break;
      case 12:
        mplew.writeMapleAsciiString(str);
        mplew.write(args[1]);
        break;
      case 13:
        mplew.writeMapleAsciiString(str);
        mplew.write(args[1]);
        mplew.writeShort(args[2]);
        mplew.writeInt(args[3]);
        mplew.writeInt(args[4]);
        break;
      case 14:
        mplew.write(args[1]);
        for (i = 0; i < args[1]; i++)
          mplew.writeInt(args[2]); 
        break;
      case 17:
        mplew.writeInt(args[1]);
        mplew.writeInt(args[2]);
        mplew.write(args[3]);
        break;
      case 18:
        mplew.writeInt(args[1]);
        break;
      case 19:
        mplew.write(args[1]);
        break;
      case 20:
        mplew.write(args[1]);
        break;
      case 22:
        mplew.writeInt(args[1]);
        break;
      case 23:
        mplew.writeMapleAsciiString(str);
        break;
    } 
    return mplew.getPacket();
  }
  
  public static byte[] PlayAmientSound(String UOL, int nVolume, int unk) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PlayAmbientSound.getValue());
    mplew.writeMapleAsciiString(UOL);
    mplew.writeInt(nVolume);
    mplew.writeInt(unk);
    return mplew.getPacket();
  }
  
  public static byte[] StopAmientSound(String UOL) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.StopAmbientSound.getValue());
    mplew.writeMapleAsciiString(UOL);
    return mplew.getPacket();
  }
  
  public static byte[] SetNpcSpecialAction(int oid, String uol, int tDuration, boolean bLocalAct) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.NpcSpecialAction.getValue());
    mplew.writeInt(oid);
    mplew.writeMapleAsciiString(uol);
    mplew.writeInt(tDuration);
    mplew.write(bLocalAct);
    return mplew.getPacket();
  }
  
  public static byte[] SetNpcSpecialAction2(int oid, int type, int type2) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.NpcSpecialAction2.getValue());
    mplew.writeInt(oid);
    mplew.writeInt(type);
    mplew.writeInt(type2);
    return mplew.getPacket();
  }
  
  public static byte[] SetNpcSpecialAction3(int oid) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.NpcSpecialAction3.getValue());
    mplew.writeInt(oid);
    return mplew.getPacket();
  }
  
  public static byte[] SetNpcSpecialAction4(int oid, String ani, String ani2, int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.NpcSpecialAction4.getValue());
    mplew.writeInt(oid);
    mplew.write(args[0]);
    mplew.writeInt(args[1]);
    mplew.writeMapleAsciiString(ani);
    mplew.writeInt(args[2]);
    mplew.writeMapleAsciiString(ani2);
    mplew.writeInt(args[3]);
    return mplew.getPacket();
  }
  
  public static byte[] SetNpcMotion(int id, int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.NPC_MOTION.getValue());
    mplew.writeInt(id);
    mplew.writeInt(args[0]);
    mplew.writeInt(args[1]);
    mplew.writeInt(args[2]);
    return mplew.getPacket();
  }
  
  public static byte[] getNpcMoveAction(int oid, int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.NPC_MOVE_ACTION.getValue());
    mplew.writeInt(oid);
    mplew.writeInt(args[0]);
    mplew.writeInt(args[1]);
    mplew.writeInt(args[2]);
    return mplew.getPacket();
  }
  
  public static byte[] SpawnDirectionObject(int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SPAWN_DIRECTION_OBJECT.getValue());
    mplew.writeShort(args[0]);
    mplew.writeShort(args[1]);
    mplew.writeInt(args[2]);
    mplew.writeInt(args[3]);
    mplew.writeInt(args[4]);
    return mplew.getPacket();
  }
  
  public static byte[] MakeBlind(int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
    mplew.write(21);
    mplew.write(args[0]);
    mplew.writeShort(args[1]);
    mplew.writeShort(args[2]);
    mplew.writeShort(args[3]);
    mplew.writeShort(args[4]);
    mplew.writeInt(args[5]);
    mplew.writeInt(args[6]);
    return mplew.getPacket();
  }
  
  public static byte[] cMakeBlind(int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
    mplew.write(args[0]);
    mplew.write(args[1]);
    mplew.write(args[2]);
    mplew.write(args[3]);
    mplew.write(args[4]);
    mplew.writeInt(args[5]);
    mplew.writeInt(args[6]);
    mplew.write(args[7]);
    return mplew.getPacket();
  }
  
  public static byte[] cMakeBlind2(int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
    mplew.write(args[0]);
    mplew.writeInt(args[1]);
    mplew.writeInt(args[2]);
    mplew.writeInt(args[3]);
    mplew.write(args[4]);
    return mplew.getPacket();
  }
  
  public static byte[] ShowEffectParticle(boolean stop, String st, String st2, int x, int y) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
    mplew.write(35);
    mplew.writeInt(stop ? 1 : 0);
    mplew.writeMapleAsciiString(st);
    if (stop) {
      mplew.writeInt(x);
    } else {
      mplew.writeInt(0);
      mplew.writeInt(0);
      mplew.writeInt(x);
      mplew.writeInt(y);
      mplew.writeMapleAsciiString(st2);
      mplew.writeInt(4);
      mplew.write(1);
      mplew.writeInt(-1);
      mplew.writeInt(0);
      mplew.writeInt(0);
      mplew.write(0);
    } 
    return mplew.getPacket();
  }
  
    public static class OneCardGamePacket {

        public static byte[] CreateUI(MapleCharacter chr, int position, List<MapleCharacter> chrs) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(20);
            mplew.write(16);// GameCode
            mplew.write(chrs.size());
            mplew.write(position);
            for (int a = 0; a < chrs.size(); a++) {
                mplew.write(a);
                AvatarLook.encodeAvatarLook(mplew, chrs.get(a), (chr.getId() == ((MapleCharacter)chrs.get(a)).getId()), false);
                mplew.writeMapleAsciiString(chrs.get(a).getName());
                mplew.writeShort(chrs.get(a).getJob());
                mplew.writeInt(0);
            }
            mplew.write(-1);
            // COneCardGameRoomDlg::OnEnterResult
            mplew.write(chrs.size());
            for (int a = 0; a < chrs.size(); a++) {
                mplew.writeInt(chrs.get(a).getId());
            }
            return mplew.getPacket();
        }

        public static byte[] onChangeColorRequest(List<Integer> ableColors) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(2);

            //OneCardChangeColorDlg
            mplew.writeInt(15); // time

            mplew.write(ableColors.size());
            for (int color : ableColors) {
                mplew.write(color);
            }
            return mplew.getPacket();
        }

        public static byte[] onStart(List<OneCardGame.OneCardPlayer> players) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(119);
            //MakeSlotScore
            mplew.writeInt(players.size());

            for (int a = 0; a < players.size(); a++) {
                mplew.writeInt(players.get(a).getPlayer().getId());
                mplew.writeInt(0);
                mplew.writeInt(0); // playCount 등급 관련 패킷
                mplew.writeInt(players.get(a).getPosition()); // eSlot
                mplew.writeInt(players.get(a).getCards().size()); // nCardSet
            }
            return mplew.getPacket();
        }

        public static byte[] onPutCardResult(OneCardGame.OneCardPlayer player, OneCardGame.OneCard card) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(157);
            mplew.writeInt(player == null ? 0 : player.getPlayer().getId());
            mplew.writeInt(card.getObjectId());
            //MakeSlotScore
            mplew.write(card.getColor()); // color
            mplew.write(card.getType()); // type
            mplew.write(false); // Hide..?

            return mplew.getPacket();
        }

        public static byte[] onGetCardResult(OneCardGame.OneCardPlayer player, List<OneCardGame.OneCard> cards) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(158);
            mplew.writeInt(player.getPlayer().getId());
            mplew.write(cards.size());
            for (OneCardGame.OneCard card : cards) {
                mplew.writeInt(card.getObjectId());

                //MakeSlotScore
                mplew.write(card.getColor()); // color
                mplew.write(card.getType()); // type
            }
            return mplew.getPacket();
        }

        public static byte[] onChangeColorResult(boolean bHero, byte color) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(159);
            mplew.write(bHero);
            mplew.write(color);
            return mplew.getPacket();
        }

        public static byte[] onUserPossibleAction(OneCardGame.OneCardPlayer player, List<OneCardGame.OneCard> cards, boolean bGetCardFromGraves, boolean bClockWiseTurn) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(160);
            mplew.write(15); // time
            mplew.writeInt(player.getPlayer().getId()); // playerId
            mplew.write(bGetCardFromGraves); // bGetCardFromGraves
            mplew.write(bClockWiseTurn); // 시계방향 여부 ㅋㅋ
            mplew.writeInt(cards.size());
            for (OneCardGame.OneCard card : cards) {
                mplew.writeInt(card.getObjectId());
            }
            return mplew.getPacket();
        }

        public static byte[] onShowScreenEffect(String str) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(161);
            mplew.writeMapleAsciiString(str);
            return mplew.getPacket();
        }

        public static byte[] onEffectResult(int type, int data, int id, boolean gameOver) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(162);
            mplew.write(type);

            switch (type) {
                case 0: // 카드 덱 섞기
                case 1: // ShowSingleDeck? 효과없음
                    break;
                case 2: // 공격 이펙트
                    mplew.write(data); // 불 갯수
                    break;
                case 3: // 디펜스 이펙트
                case 4: // 디펜스 부수기 이펙트
                    mplew.writeInt(id);
                    break;
                case 5: // 공격 받는 이펙트
                    mplew.writeInt(id);
                    mplew.write(gameOver);
                    break;
            }
            return mplew.getPacket();
        }

        public static byte[] onEmotion(int charid, int eid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(163);
            mplew.writeInt(charid);
            mplew.writeInt(eid);
            return mplew.getPacket();
        }
    }
  
  public static byte[] onShowText(String str) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(165);
    mplew.writeMapleAsciiString(str);
    return mplew.getPacket();
  }
  
  public static final byte[] leaveResult(byte slot) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.EXIT.action);
    mplew.write(0);
    return mplew.getPacket();
  }
  
  public static class MultiYutGamePacket {
    public static byte[] createUI(MultiYutGame.MultiYutPlayer Me, MultiYutGame.MultiYutPlayer Other) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.MAPLE_YUT.getValue());
      mplew.write(1);
      mplew.writeInt(2);
      mplew.writeInt(0);
      mplew.writeInt(0);
      mplew.write(3);
      mplew.writeInt(0);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.writeInt(0);
      mplew.writeInt(Other.getChr().getId());
      mplew.writeInt(0);
      mplew.writeInt(-1);
      mplew.writeInt(4);
      mplew.writeInt(1);
      mplew.write(0);
      mplew.writeInt(4);
      mplew.writeInt(1);
      mplew.write(0);
      int i;
      for (i = 0; i < 4; i++) {
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
      } 
      for (i = 0; i < 6; i++)
        mplew.writeInt(0); 
      mplew.write(3);
      mplew.writeInt(1);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.writeInt(1);
      mplew.writeInt(Me.getChr().getId());
      mplew.writeInt(1);
      mplew.writeInt(-1);
      mplew.writeInt(4);
      mplew.writeInt(1);
      mplew.write(0);
      mplew.writeInt(4);
      mplew.writeInt(1);
      mplew.write(0);
      for (i = 0; i < 4; i++) {
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
      } 
      for (i = 0; i < 6; i++)
        mplew.writeInt(0); 
      mplew.write(4);
      mplew.writeInt(0);
      mplew.writeInt(-1);
      mplew.writeInt(0);
      mplew.write(0);
      return mplew.getPacket();
    }
    
    public static byte[] ThrowYut(List<MultiYutGame.MultiYutPlayer> players, int type, int whoturn, boolean more) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.MAPLE_YUT.getValue());
      mplew.write(1);
      mplew.writeInt(3);
      mplew.writeInt(0);
      mplew.writeInt(whoturn);
      for (MultiYutGame.MultiYutPlayer player : players) {
        mplew.write(3);
        mplew.writeInt(player.getPosition());
        mplew.write(0);
        mplew.write(0);
        mplew.write((more && player.getPosition() == whoturn) ? 1 : 0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(player.getPosition());
        mplew.writeInt(player.getPlayer().getId());
        mplew.writeInt(player.getPosition());
        mplew.writeInt(-1);
        for (MultiYutGame.PlayersSkill skill : player.getSkilllist()) {
          mplew.writeInt(skill.getSkill());
          mplew.writeInt(!skill.isSkillused() ? 1 : 0);
          mplew.write(0);
        } 
        for (MultiYutGame.PlayersHorses horse : player.getHorses()) {
          mplew.writeInt(horse.getNowposition());
          mplew.write((!horse.isOverlapOwner() && horse.getOverlap().size() > 0) ? 1 : 0);
          mplew.writeInt(!horse.isOverlapOwner() ? horse.getOverlap().size() : 0);
          mplew.writeInt(horse.isOverlapOwner() ? horse.getOverlap().size() : 0);
          if (horse.isOverlapOwner() && horse.getOverlap().size() > 0)
            for (Map.Entry<Integer, Integer> OverlapHorseInvPos : horse.getOverlap().entrySet())
              mplew.writeInt(((Integer)OverlapHorseInvPos.getKey()).intValue());  
        } 
        for (Map.Entry<Integer, Integer> Yut : player.getYut().entrySet())
          mplew.writeInt(((Integer)Yut.getValue()).intValue()); 
      } 
      mplew.write(4);
      mplew.writeInt(2);
      mplew.writeInt(-1);
      mplew.writeInt(2);
      mplew.writeInt(1);
      mplew.writeInt(type);
      if (more) {
        mplew.write(4);
        mplew.writeInt(6);
        mplew.writeInt(whoturn);
        mplew.writeInt(0);
      } else {
        mplew.write(0);
      } 
      mplew.write(0);
      return mplew.getPacket();
    }
    
    public static byte[] MovedHorse(List<MultiYutGame.MultiYutPlayer> players, MultiYutGame.MultiYutPlayer owner, List<Integer> layout, int nowpoint, int whoturn, int movehorsepos, boolean catched, boolean onemore) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.MAPLE_YUT.getValue());
      mplew.write(1);
      mplew.writeInt(3);
      mplew.writeInt(0);
      mplew.writeInt(whoturn);
      for (MultiYutGame.MultiYutPlayer player : players) {
        mplew.write(3);
        mplew.writeInt(player.getPosition());
        mplew.write(1);
        mplew.write(1);
        mplew.write((player.getPosition() == whoturn) ? 1 : 0);
        mplew.write(0);
        mplew.write((player.getChr() == owner.getChr() && catched) ? 1 : 0);
        mplew.write(1);
        mplew.writeInt(player.getPosition());
        mplew.writeInt(player.getPlayer().getId());
        mplew.writeInt(player.getPosition());
        mplew.writeInt(-1);
        for (MultiYutGame.PlayersSkill skill : player.getSkilllist()) {
          mplew.writeInt(skill.getSkill());
          mplew.writeInt(!skill.isSkillused() ? 1 : 0);
          mplew.write(0);
        } 
        for (MultiYutGame.PlayersHorses horse : player.getHorses()) {
          mplew.writeInt(horse.getNowposition());
          mplew.write((!horse.isOverlapOwner() && horse.getOverlap().size() > 0) ? 1 : 0);
          mplew.writeInt(!horse.isOverlapOwner() ? horse.getOverlap().size() : 0);
          mplew.writeInt(horse.isOverlapOwner() ? horse.getOverlap().size() : 0);
          if (horse.isOverlapOwner() && horse.getOverlap().size() > 0)
            for (Map.Entry<Integer, Integer> OverlapHorseInvPos : horse.getOverlap().entrySet())
              mplew.writeInt(((Integer)OverlapHorseInvPos.getKey()).intValue());  
        } 
        for (Map.Entry<Integer, Integer> Yut : player.getYut().entrySet())
          mplew.writeInt(((Integer)Yut.getValue()).intValue()); 
      } 
      if (catched) {
        mplew.write(4);
        mplew.writeInt(10);
        mplew.writeInt(-1);
        mplew.writeInt(1);
        mplew.writeInt(20000);
      } 
      mplew.write(4);
      mplew.writeInt(2);
      mplew.writeInt(-1);
      mplew.writeInt(9 + layout.size() - 1);
      mplew.writeInt(2);
      mplew.writeInt(owner.getPosition());
      mplew.writeInt(movehorsepos);
      mplew.writeInt(0);
      mplew.writeInt(catched ? 7 : -1);
      mplew.writeInt(0);
      mplew.writeInt(layout.size());
      for (Integer a : layout)
        mplew.writeInt(a.intValue()); 
      mplew.writeInt(0);
      if (!catched) {
        mplew.write(4);
        mplew.writeInt(6);
        mplew.writeInt(owner.getPosition());
        mplew.writeInt(0);
        mplew.write(0);
      } else {
        mplew.write(0);
      } 
      mplew.writeZeroBytes(100);
      mplew.write(0);
      return mplew.getPacket();
    }
    
    public static byte[] YutEtc(int type, int type2, int... args) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.MAPLE_YUT.getValue());
      mplew.write(type);
      mplew.writeInt(type2);
      mplew.writeInt(args[0]);
      mplew.writeInt(args[1]);
      mplew.writeInt(args[2]);
      mplew.write(0);
      return mplew.getPacket();
    }
    
    public static byte[] SettingAndStart(List<MultiYutGame.MultiYutPlayer> players) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.MAPLE_YUT.getValue());
      mplew.write(1);
      mplew.writeInt(3);
      mplew.writeInt(0);
      mplew.writeInt(0);
      for (MultiYutGame.MultiYutPlayer player : players) {
        mplew.write(3);
        mplew.writeInt(player.getPosition());
        mplew.write(0);
        mplew.write(0);
        mplew.write(player.Myturn(player) ? 1 : 0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(player.getPosition());
        mplew.writeInt(player.getPlayer().getId());
        mplew.writeInt((player.getPosition() == 1) ? 0 : 1);
        mplew.writeInt(-1);
        for (MultiYutGame.PlayersSkill skill : player.getSkilllist()) {
          mplew.writeInt(skill.getSkill());
          mplew.writeInt(!skill.isSkillused() ? 1 : 0);
          mplew.write(0);
        } 
        for (MultiYutGame.PlayersHorses horse : player.getHorses()) {
          mplew.writeInt(horse.getNowposition());
          mplew.write((!horse.isOverlapOwner() && horse.getOverlap().size() > 0) ? 1 : 0);
          mplew.writeInt(!horse.isOverlapOwner() ? horse.getOverlap().size() : 0);
          mplew.writeInt(horse.isOverlapOwner() ? horse.getOverlap().size() : 0);
          if (horse.isOverlapOwner() && horse.getOverlap().size() > 0)
            for (Map.Entry<Integer, Integer> OverlapHorseInvPos : horse.getOverlap().entrySet())
              mplew.writeInt(((Integer)OverlapHorseInvPos.getKey()).intValue());  
        } 
        for (Map.Entry<Integer, Integer> Yut : player.getYut().entrySet())
          mplew.writeInt(((Integer)Yut.getValue()).intValue()); 
      } 
      mplew.write(4);
      mplew.writeInt(1);
      mplew.writeInt(-1);
      mplew.writeInt(0);
      for (MultiYutGame.MultiYutPlayer player : players) {
        mplew.write(4);
        mplew.writeInt(2);
        mplew.writeInt(player.getPosition());
        mplew.writeInt(3);
        mplew.writeInt(3);
        for (MultiYutGame.PlayersSkill skill : player.getSkilllist())
          mplew.writeInt(skill.getSkill()); 
      } 
      mplew.write(0);
      return mplew.getPacket();
    }
  }
  
  public static class MonsterPyramidPacket {
    public static byte[] createUI(List<MapleCharacter> list) {
      MaplePacketLittleEndianWriter maplePacketLittleEndianWriter;
      (maplePacketLittleEndianWriter = new MaplePacketLittleEndianWriter()).writeShort(SendPacketOpcode.MONSTER_PYRAMID.getValue());
      maplePacketLittleEndianWriter.write(1);
      maplePacketLittleEndianWriter.writeInt(2);
      maplePacketLittleEndianWriter.writeInt(0);
      maplePacketLittleEndianWriter.writeInt(0);
      maplePacketLittleEndianWriter.writeInt(0);
      for (int i = 0; i < list.size(); i++) {
        maplePacketLittleEndianWriter.write(2);
        maplePacketLittleEndianWriter.writeInt(i);
        maplePacketLittleEndianWriter.write(true);
        maplePacketLittleEndianWriter.write(false);
        maplePacketLittleEndianWriter.write(false);
        maplePacketLittleEndianWriter.write(false);
        maplePacketLittleEndianWriter.writeInt(i);
        maplePacketLittleEndianWriter.writeInt(((MapleCharacter)list.get(i)).getId());
        maplePacketLittleEndianWriter.writeInt(0);
        maplePacketLittleEndianWriter.writeInt(1);
        for (int j = 0; j < 6; j++)
          maplePacketLittleEndianWriter.writeInt(0); 
      } 
      for (int k = 0; k < 7; k++) {
        for (int l = 0; l < 8 - k; l++) {
          maplePacketLittleEndianWriter.write(3);
          maplePacketLittleEndianWriter.writeInt(k);
          maplePacketLittleEndianWriter.writeInt(l);
          maplePacketLittleEndianWriter.writeInt(-1);
          maplePacketLittleEndianWriter.writeInt(0);
          maplePacketLittleEndianWriter.writeInt(l);
        } 
      } 
      maplePacketLittleEndianWriter.write(4);
      maplePacketLittleEndianWriter.writeInt(1);
      maplePacketLittleEndianWriter.writeInt(3);
      maplePacketLittleEndianWriter.writeInt(0);
      maplePacketLittleEndianWriter.write(0);
      return maplePacketLittleEndianWriter.getPacket();
    }
    
    public static byte[] onInit(List<MapleCharacter> list) {
      MaplePacketLittleEndianWriter maplePacketLittleEndianWriter;
      (maplePacketLittleEndianWriter = new MaplePacketLittleEndianWriter()).writeShort(SendPacketOpcode.MONSTER_PYRAMID.getValue());
      for (int i = 0; i < list.size(); i++) {
        maplePacketLittleEndianWriter.write(2);
        maplePacketLittleEndianWriter.writeInt(i);
        maplePacketLittleEndianWriter.write(true);
        maplePacketLittleEndianWriter.write(true);
        maplePacketLittleEndianWriter.write(false);
        maplePacketLittleEndianWriter.write(false);
        maplePacketLittleEndianWriter.writeInt(i);
        maplePacketLittleEndianWriter.writeInt(((MapleCharacter)list.get(i)).getId());
        maplePacketLittleEndianWriter.writeInt(0);
        maplePacketLittleEndianWriter.writeInt(1);
        for (int j = 0; j < 6; j++)
          maplePacketLittleEndianWriter.writeInt(((MapleCharacter)list.get(i)).getMonsterPyramidInstance().getPlayer(list.get(i)).getSelectBlockType(j)); 
      } 
      maplePacketLittleEndianWriter.write(0);
      return maplePacketLittleEndianWriter.getPacket();
    }
    
    public static byte[] Handler(List<MapleCharacter> list, boolean first, int... args) {
      int i, k;
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.MONSTER_PYRAMID.getValue());
      mplew.write(args[0]);
      mplew.writeInt(args[1]);
      mplew.writeInt(0);
      mplew.writeInt(args[2]);
      mplew.writeInt(args[3]);
      switch (args[1]) {
        case 3:
          if (!first) {
            for (int j = 0; j < list.size(); j++) {
              mplew.write(2);
              mplew.writeInt(j);
              mplew.write(true);
              mplew.write(true);
              mplew.write(false);
              mplew.write(false);
              mplew.writeInt(j);
              mplew.writeInt(((MapleCharacter)list.get(j)).getId());
              mplew.writeInt(((MapleCharacter)list.get(j)).getMonsterPyramidInstance().getPlayer(list.get(j)).getPoint());
              mplew.writeInt(((MapleCharacter)list.get(j)).getMonsterPyramidInstance().getPlayer(list.get(j)).getRank());
              for (int m = 0; m < 6; m++)
                mplew.writeInt(((MapleCharacter)list.get(j)).getMonsterPyramidInstance().getPlayer(list.get(j)).getSelectBlockType(m)); 
            } 
            mplew.write(3);
            mplew.writeInt(args[4]);
            mplew.writeInt(args[5]);
            mplew.writeInt(args[6]);
            mplew.writeInt(0);
            mplew.writeInt(3);
          } 
          mplew.write(4);
          mplew.writeInt(args[7]);
          mplew.writeInt(args[8]);
          mplew.writeInt(0);
          mplew.write(4);
          mplew.writeInt(args[9]);
          mplew.writeInt(3);
          mplew.writeInt(0);
          break;
        case 4:
          mplew.write(4);
          mplew.writeInt(args[4]);
          mplew.writeInt(3);
          mplew.writeInt(args[5]);
          if (first) {
            mplew.write(4);
            mplew.writeInt(args[6]);
            mplew.writeInt(3);
            mplew.writeInt(0);
          } 
          break;
        case 5:
          for (i = 0; i < list.size(); i++) {
            mplew.write(2);
            mplew.writeInt(i);
            mplew.write(true);
            mplew.write(true);
            mplew.write(false);
            mplew.write(false);
            mplew.writeInt(i);
            mplew.writeInt(((MapleCharacter)list.get(i)).getId());
            mplew.writeInt(((MapleCharacter)list.get(i)).getMonsterPyramidInstance().getPlayer(list.get(i)).getPoint());
            mplew.writeInt(((MapleCharacter)list.get(i)).getMonsterPyramidInstance().getPlayer(list.get(i)).getRank());
            for (int j = 0; j < 6; j++)
              mplew.writeInt(((MapleCharacter)list.get(i)).getMonsterPyramidInstance().getPlayer(list.get(i)).getSelectBlockType(j)); 
          } 
          for (k = 0; k < 7; k++) {
            for (int l = 0; l < 8 - k; l++) {
              mplew.write(3);
              mplew.writeInt(k);
              mplew.writeInt(l);
              mplew.writeInt(-1);
              mplew.writeInt(0);
              mplew.writeInt(l);
            } 
          } 
          mplew.write(4);
          mplew.writeInt(10);
          mplew.writeInt(3);
          mplew.writeInt(0);
          mplew.write(0);
          break;
        case 6:
          mplew.write(4);
          mplew.writeInt(4);
          mplew.writeInt(3);
          mplew.writeInt(1);
          mplew.writeInt(args[4]);
          break;
      } 
      mplew.write(0);
      return mplew.getPacket();
    }
  }
  
  public static class MultiOthelloGamePacket {
    public static byte[] createUI(List<MapleCharacter> list, MapleCharacter mapleCharacter, int i) {
      MaplePacketLittleEndianWriter maplePacketLittleEndianWriter;
      (maplePacketLittleEndianWriter = new MaplePacketLittleEndianWriter()).writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
      maplePacketLittleEndianWriter.write(20);
      maplePacketLittleEndianWriter.write(19);
      maplePacketLittleEndianWriter.write(list.size());
      maplePacketLittleEndianWriter.write(i);
      for (i = 0; i < list.size(); i++) {
        maplePacketLittleEndianWriter.write(((MapleCharacter)list.get(i)).getBattleReverseInstance().getPlayer(((MapleCharacter)list.get(i)).getId()).getStoneId());
        AvatarLook.encodeAvatarLook(maplePacketLittleEndianWriter, list.get(i), (mapleCharacter.getId() == ((MapleCharacter)list.get(i)).getId()), false);
        maplePacketLittleEndianWriter.writeMapleAsciiString(((MapleCharacter)list.get(i)).getName());
        maplePacketLittleEndianWriter.writeShort(((MapleCharacter)list.get(i)).getJob());
      } 
      maplePacketLittleEndianWriter.write(-1);
      return maplePacketLittleEndianWriter.getPacket();
    }
    
    public static byte[] onInit(List<BattleReverse.BattleReverseStone> list, int n) {
      MaplePacketLittleEndianWriter maplePacketLittleEndianWriter;
      (maplePacketLittleEndianWriter = new MaplePacketLittleEndianWriter()).writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue() + 1);
      maplePacketLittleEndianWriter.writeInt(96);
      maplePacketLittleEndianWriter.writeInt(n);
      maplePacketLittleEndianWriter.writeInt(10000);
      maplePacketLittleEndianWriter.writeInt(10);
      maplePacketLittleEndianWriter.writeInt(list.size());
      for (BattleReverse.BattleReverseStone battleReverseStone : list) {
        maplePacketLittleEndianWriter.writeInt((battleReverseStone.getStonePosition()).x);
        maplePacketLittleEndianWriter.writeInt((battleReverseStone.getStonePosition()).y);
        maplePacketLittleEndianWriter.writeInt(battleReverseStone.getStoneId());
      } 
      return maplePacketLittleEndianWriter.getPacket();
    }
    
    public static byte[] onBoardUpdate(boolean b, Point point, int n, int n2, List<BattleReverse.BattleReverseStone> list, byte turn) {
      MaplePacketLittleEndianWriter maplePacketLittleEndianWriter;
      (maplePacketLittleEndianWriter = new MaplePacketLittleEndianWriter()).writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue() + 1);
      maplePacketLittleEndianWriter.writeInt(185);
      maplePacketLittleEndianWriter.write(b);
      maplePacketLittleEndianWriter.writeInt(point.x);
      maplePacketLittleEndianWriter.writeInt(point.y);
      maplePacketLittleEndianWriter.writeInt(n);
      maplePacketLittleEndianWriter.write(turn);
      maplePacketLittleEndianWriter.writeInt(list.size());
      for (BattleReverse.BattleReverseStone battleReverseStone : list) {
        maplePacketLittleEndianWriter.writeInt((battleReverseStone.getStonePosition()).x);
        maplePacketLittleEndianWriter.writeInt((battleReverseStone.getStonePosition()).y);
        maplePacketLittleEndianWriter.writeInt(battleReverseStone.getStoneId());
      } 
      maplePacketLittleEndianWriter.writeInt(n2);
      return maplePacketLittleEndianWriter.getPacket();
    }
    
    
    public static byte[] onResult(int n) {
      MaplePacketLittleEndianWriter maplePacketLittleEndianWriter;
      (maplePacketLittleEndianWriter = new MaplePacketLittleEndianWriter()).writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue() + 1);
      maplePacketLittleEndianWriter.writeInt(189);
      maplePacketLittleEndianWriter.writeInt(n);
      return maplePacketLittleEndianWriter.getPacket();
    }
  }
  
  public static final byte[] BlackLabel(String msg, int delay, int textspeed, int type, int x, int y, int type1, int type2) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
    mplew.write(61);
    mplew.writeMapleAsciiString(msg);
    mplew.writeInt(delay);
    mplew.writeInt(textspeed);
    mplew.writeInt(type);
    mplew.writeInt(x);
    mplew.writeInt(y);
    mplew.writeInt(type1);
    mplew.writeInt(type2);
    mplew.writeInt(0);
    mplew.writeInt(0);
    mplew.writeInt(0);
    mplew.writeShort(0);
    mplew.writeLong(0L);
    return mplew.getPacket();
  }
  
  public static final byte[] BlackLabelE(String msg, int delay, int textspeed, int type, int x, int y, int type1, int type2, int type3, String t) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
    mplew.write(61);
    mplew.writeMapleAsciiString(msg);
    mplew.writeInt(delay);
    mplew.writeInt(textspeed);
    mplew.writeInt(type);
    mplew.writeInt(x);
    mplew.writeInt(y);
    mplew.writeInt(type1);
    mplew.writeInt(type2);
    mplew.writeInt(type3);
    mplew.writeInt(0);
    mplew.writeInt(0);
    mplew.writeMapleAsciiString(t);
    mplew.writeLong(0L);
    return mplew.getPacket();
  }
  
  public static final byte[] ShowEffectChatNpc(int type, String msg, int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
    mplew.write(60);
    mplew.write(type);
    mplew.writeInt(args[0]);
    mplew.writeInt(args[1]);
    mplew.writeMapleAsciiString(msg);
    mplew.writeInt(args[2]);
    mplew.writeInt(args[3]);
    mplew.writeInt(args[4]);
    mplew.writeInt(args[5]);
    mplew.writeInt(args[6]);
    mplew.writeInt(args[7]);
    mplew.writeInt(args[8]);
    mplew.writeInt(args[9]);
    mplew.writeInt(args[10]);
    return mplew.getPacket();
  }
  
  public static byte[] EventSkillOn(int mobid) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SKILL_ON.getValue());
    mplew.writeInt(mobid);
    return mplew.getPacket();
  }
  
  public static byte[] EventSkillOn(int type, int type2) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SKILL_ON.getValue());
    mplew.writeInt(type);
    mplew.writeInt(type2);
    mplew.writeInt(80003054);
    mplew.writeInt(80003055);
    mplew.writeZeroBytes(100);
    return mplew.getPacket();
  }
  
  public static byte[] EventSkillOnFlowerEffect(int type, int type2) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SKILL_ON_FLOWER.getValue());
    mplew.writeInt(0);
    mplew.writeInt(0);
    mplew.writeInt(type);
    mplew.writeInt(type2);
    return mplew.getPacket();
  }
  
  public static byte[] EventSkillOnEffect(int unk, int unk2, int type, int type2) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SKILL_ON_FLOWER.getValue());
    mplew.writeInt(unk);
    mplew.writeInt(unk2);
    mplew.writeInt(type);
    mplew.writeInt(type2);
    return mplew.getPacket();
  }
  
  public static class ColorCardPacket {
    public static byte[] ColorCardState(int type) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.COLOR_CARD_STATE.getValue());
      mplew.writeInt(type);
      return mplew.getPacket();
    }
    
    public static byte[] ColorCardMain(int combo, int gauge, int point) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.COLOR_CARD_MAIN.getValue());
      mplew.writeInt(combo);
      mplew.writeInt(gauge);
      mplew.writeInt(point);
      return mplew.getPacket();
    }
    
    public static byte[] ColorCardResult(ColorInvitationCard chr) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.COLOR_CARD_RESULT.getValue());
      mplew.writeInt(chr.getPoint());
      mplew.writeInt(chr.getComboCount());
      mplew.writeInt(chr.getFailCount());
      mplew.writeInt(223);
      mplew.writeInt(chr.getGreensuc());
      mplew.writeInt(chr.getRedsuc());
      mplew.writeInt(chr.getBluesuc());
      return mplew.getPacket();
    }
    
    public static byte[] ColorCardSetting() {
      List<Pair<Integer, Integer>> list = new ArrayList<>();
      list.add(new Pair<>(Integer.valueOf(0), Integer.valueOf(35)));
      list.add(new Pair<>(Integer.valueOf(150), Integer.valueOf(100)));
      list.add(new Pair<>(Integer.valueOf(230), Integer.valueOf(110)));
      list.add(new Pair<>(Integer.valueOf(310), Integer.valueOf(120)));
      list.add(new Pair<>(Integer.valueOf(390), Integer.valueOf(130)));
      list.add(new Pair<>(Integer.valueOf(470), Integer.valueOf(140)));
      list.add(new Pair<>(Integer.valueOf(550), Integer.valueOf(150)));
      list.add(new Pair<>(Integer.valueOf(630), Integer.valueOf(160)));
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.COLOR_CARD_SETTING.getValue());
      mplew.writeInt(list.size());
      for (Pair<Integer, Integer> alist : list) {
        mplew.writeInt(((Integer)alist.getLeft()).intValue());
        mplew.writeInt(((Integer)alist.getRight()).intValue());
      } 
      return mplew.getPacket();
    }
    
    public static byte[] ColorCardSetting1() {
      List<Triple<Integer, Integer, Integer>> list = new ArrayList<>();
      list.add(new Triple<>(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)));
      list.add(new Triple<>(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
      list.add(new Triple<>(Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(0)));
      list.add(new Triple<>(Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(1)));
      list.add(new Triple<>(Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(1)));
      list.add(new Triple<>(Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(1)));
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.COLOR_CARD_SETTING1.getValue());
      mplew.writeInt(list.size());
      for (Triple<Integer, Integer, Integer> alist : list) {
        mplew.writeInt(((Integer)alist.getLeft()).intValue());
        mplew.writeInt(((Integer)alist.getMid()).intValue());
        mplew.write(((Integer)alist.getRight()).intValue());
      } 
      return mplew.getPacket();
    }
    
    public static byte[] ColorCardSetting2(int unk1, int unk2) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.COLOR_CARD_SETTING2.getValue());
      mplew.writeInt(unk1);
      mplew.writeInt(unk2);
      return mplew.getPacket();
    }
    
    public static byte[] ColorCardKind(List<Integer> cardlist) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.COLOR_CARD_KIND.getValue());
      mplew.writeInt(cardlist.size());
      for (Integer card : cardlist) {
        boolean hidden = Randomizer.isSuccess(5);
        mplew.write(hidden ? (card.intValue() + 3) : card.intValue());
      } 
      return mplew.getPacket();
    }
    
    public static byte[] ColorCardBonus(boolean bonus) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.COLOR_CARD_BONUS.getValue());
      mplew.write(bonus);
      return mplew.getPacket();
    }
  }
  
  public static byte[] ContentsWaiting(MapleCharacter chr, int mapcode, int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.CONTENTS_WAITING.getValue());
    mplew.writeInt(chr.getId());
    mplew.write(args[0]);
    mplew.write(args[1]);
    mplew.writeInt(args[2]);
    mplew.writeInt(args[3]);
    mplew.writeInt(0);
    mplew.writeInt(0);
    mplew.writeInt(0);
    mplew.writeInt(mapcode);
    return mplew.getPacket();
  }
  
  public static class BigWispPacket {
    public static byte[] BigWispHandler(int type, int round) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.BIG_WISP_HANDLER.getValue());
      mplew.writeInt(type);
      mplew.writeInt(round);
      return mplew.getPacket();
    }
    
    public static byte[] BigWispRank(Map<MapleCharacter, Integer> info) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.BIG_WISP_RANK.getValue());
      mplew.writeInt(info.size());
      int i = 1;
      for (Map.Entry<MapleCharacter, Integer> linfo : info.entrySet()) {
        mplew.writeInt(i);
        mplew.writeInt(((MapleCharacter)linfo.getKey()).getId());
        mplew.writeMapleAsciiString(((MapleCharacter)linfo.getKey()).getName());
        mplew.writeInt(((Integer)linfo.getValue()).intValue());
        i++;
      } 
      return mplew.getPacket();
    }
    
    public static byte[] BigWispPutInfo(String info, int... args) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.BIG_WISP_INFO_PUT.getValue());
      mplew.writeInt(args[0]);
      mplew.writeInt(args[1]);
      mplew.writeInt(10);
      switch (args[0]) {
        case 0:
          mplew.writeInt(args[2]);
          mplew.writeInt(args[3]);
          mplew.writeInt(args[4]);
          mplew.writeInt(args[5]);
          mplew.writeInt(args[6]);
          break;
        case 1:
          mplew.writeInt(args[2]);
          mplew.writeInt(args[3]);
          mplew.writeInt(args[4]);
          break;
        case 2:
          mplew.writeInt(args[2]);
          mplew.writeInt(args[3]);
          mplew.writeInt(args[4]);
          mplew.writeInt(args[5]);
          mplew.writeMapleAsciiString(info);
          break;
      } 
      return mplew.getPacket();
    }
    
    public static byte[] BigPatternChange(int... args) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.BIG_WISP_PATTERN_CHANGE.getValue());
      mplew.writeInt(args[0]);
      mplew.writeInt(args[1]);
      mplew.writeInt(args[2]);
      mplew.writeInt(args[3]);
      mplew.writeInt(args[4]);
      mplew.writeInt(args[5]);
      return mplew.getPacket();
    }
  }
  
  public static byte[] ActiveUnion(boolean on) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.ACTIVE_UNICON.getValue());
    mplew.writeInt(on ? 1 : 0);
    return mplew.getPacket();
  }
  
  public static byte[] Chatonchr(MapleCharacter chr, String chat, int time) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
    mplew.writeInt(316);
    mplew.writeZeroBytes(6);
    mplew.writeMapleAsciiString(chat);
    mplew.writeInt(time);
    mplew.writeZeroBytes(20);
    mplew.writeInt(4);
    mplew.writeLong(time);
    mplew.writeInt(chr.getId());
    return mplew.getPacket();
  }
  
  public static byte[] getItemTopMsg(int itemid, String msg) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
    mplew.writeInt(itemid);
    mplew.writeMapleAsciiString(msg);
    return mplew.getPacket();
  }
  
  public static final byte[] showWZEffect(String data, int value) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
    mplew.write((value == 0) ? 12 : 29);
    mplew.writeMapleAsciiString(data);
    mplew.writeInt(value);
    return mplew.getPacket();
  }
  
  public static byte[] CharReLocationPacket(int x, int y) {
    MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
    w.writeShort(SendPacketOpcode.USER_TELEPORT.getValue());
    w.writeInt(x);
    w.writeInt(y);
    return w.getPacket();
  }
  
  public static byte[] playSound(String SE) {
    MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
    packet.writeShort(SendPacketOpcode.PLAY_SOUND.getValue());
    packet.writeMapleAsciiString(SE);
    return packet.getPacket();
  }
  
  public static byte[] UnionCoinMax(int coinmax) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.UNION_COIN_MAX.getValue());
    mplew.writeInt(0);
    mplew.writeInt(coinmax);
    return mplew.getPacket();
  }
  
  public static byte[] EventInfoPut(List<Triple<Integer, Integer, String>> info) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_INFO_PUT.getValue());
    mplew.writeInt(info.size());
    for (Triple<Integer, Integer, String> linfo : info) {
      mplew.writeInt(-1);
      mplew.writeInt(((Integer)linfo.getLeft()).intValue());
      mplew.writeInt(((Integer)linfo.getMid()).intValue());
      mplew.writeMapleAsciiString(linfo.getRight());
    } 
    return mplew.getPacket();
  }
  
  public static byte[] EventMsgSend(int questid, int mapid, int time, String info) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EVENT_SEND_MSG.getValue());
    mplew.writeInt(-1);
    mplew.writeInt(questid);
    mplew.writeInt(1);
    mplew.writeInt(mapid);
    mplew.writeInt(time);
    mplew.writeMapleAsciiString(info);
    return mplew.getPacket();
  }
  
  public static byte[] FollowNpctoSkill(boolean follow, int npcid, int skillid) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.FOLLOW_NPC_TO_SKILL.getValue());
    mplew.write(follow);
    mplew.writeInt(npcid);
    mplew.writeInt(skillid);
    mplew.write(0);
    return mplew.getPacket();
  }
  
  public static byte[] BloomingRaceHandler(int type, int... args) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BLOOMING_RACE.getValue());
    mplew.writeInt(type);
    switch (type) {
      case 2:
      case 4:
        mplew.writeInt(args[0]);
        break;
    } 
    return mplew.getPacket();
  }
  
  public static byte[] BloomingRaceAchieve(int achieve) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BLOOMING_RACE_ACHIEVE.getValue());
    mplew.writeInt(achieve);
    return mplew.getPacket();
  }
  
  public static byte[] BloomingRaceRanking(boolean first, List<MapleCharacter> str) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.BLOOMING_RACE_RANKING.getValue());
    mplew.writeInt(first ? 1 : 0);
    mplew.writeInt(str.size());
    for (MapleCharacter chr : str)
      mplew.writeMapleAsciiString(chr.getName()); 
    return mplew.getPacket();
  }
  
  public static byte[] FootHoldOnOffEffect(List<String> str, boolean invisible) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.FOOTHOLD_ON_OFF_EFFECT.getValue());
    mplew.writeInt(str.size());
    for (String foothold : str) {
      mplew.writeMapleAsciiString(foothold);
      mplew.writeInt(invisible ? 1 : 0);
      mplew.writeInt(0);
      mplew.write(0);
    } 
    return mplew.getPacket();
  }
  
  public static byte[] FootHoldOnOff(List<Integer> str, boolean invisible) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.FOOTHOLD_ON_OFF.getValue());
    mplew.writeInt(str.size());
    for (Integer foothold : str) {
      mplew.writeInt(foothold.intValue());
      mplew.write(invisible ? 0 : 1);
    } 
    return mplew.getPacket();
  }
  
  public static byte[] ExpPocket(MapleCharacter chr, int exp, int time) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.EXP_POCKET.getValue());
    mplew.writeInt(exp);
    mplew.writeInt(time);
    mplew.writeInt(247);
    mplew.writeInt(4320);
    mplew.writeInt(chr.getId());
    return mplew.getPacket();
  }
  
  public static byte[] ErdaSpectrumSetting(int time, int sendcount) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.ERDA_SPECTRUM_SETTING.getValue());
    mplew.writeInt(time);
    mplew.writeInt(sendcount);
    return mplew.getPacket();
  }
  
  public static byte[] ErdaSpectrumType(int type) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.ERDA_SPECTRUM_TYPE.getValue());
    mplew.writeInt(type);
    return mplew.getPacket();
  }
  
  public static byte[] ErdaSpectrumGauge(int unk, int unk2, int unk3) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.ERDA_SPECTRUM_GAUGE.getValue());
    mplew.writeInt(unk);
    mplew.writeLong(-1L);
    mplew.writeInt(unk2);
    mplew.writeInt(unk3);
    return mplew.getPacket();
  }
  
  public static byte[] ErdaSpectrumArea(int... area) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.ERDA_SPECTRUM_AREA.getValue());
    mplew.writeInt(1);
    mplew.writeInt(area[0]);
    mplew.writeInt(area[1]);
    mplew.writeInt(area[2]);
    mplew.writeInt(area[3]);
    mplew.writeInt(area[4]);
    mplew.writeInt(area[5]);
    mplew.writeInt(area[6]);
    mplew.writeInt(area[7]);
    mplew.writeInt(area[8]);
    mplew.writeInt(area[9]);
    return mplew.getPacket();
  }
  
  public static byte[] OnBomb(int skillid, int skilllevel, Point pos) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.ON_BOMB.getValue());
    mplew.writeInt(skillid);
    mplew.writeInt(skilllevel);
    mplew.writePosInt(pos);
    mplew.write(0);
    return mplew.getPacket();
  }
  
  public static class TyoonKitchenPacket {
    public static byte[] Handler(int type) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.TYOONKITCHEN_HANDLE.getValue());
      mplew.writeInt(type);
      return mplew.getPacket();
    }
    
    public static byte[] Setting() {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.TYOONKITCHEN_SETTING.getValue());
      mplew.write(HexTool.getByteArrayFromHexString("08 00 00 00 54 E2 1E 00 00 00 00 00 F4 01 00 00 00 00 55 E2 1E 00 00 00 00 00 F4 01 00 00 00 00 56 E2 1E 00 00 00 00 00 F4 01 00 00 00 00 57 E2 1E 00 00 00 00 00 F4 01 00 00 00 00 58 E2 1E 00 00 00 00 00 F4 01 00 00 00 00 59 E2 1E 00 01 00 00 00 DC 05 00 00 29 00 53 6F 75 6E 64 2F 4D 69 6E 69 47 61 6D 65 2E 69 6D 67 2F 54 79 6F 6F 6E 4B 69 74 63 68 65 6E 2F 63 6F 6F 6B 5F 62 61 6B 65 5A E2 1E 00 03 00 00 00 88 13 00 00 29 00 53 6F 75 6E 64 2F 4D 69 6E 69 47 61 6D 65 2E 69 6D 67 2F 54 79 6F 6F 6E 4B 69 74 63 68 65 6E 2F 63 6F 6F 6B 5F 62 6F 69 6C 5B E2 1E 00 02 00 00 00 88 13 00 00 2A 00 53 6F 75 6E 64 2F 4D 69 6E 69 47 61 6D 65 2E 69 6D 67 2F 54 79 6F 6F 6E 4B 69 74 63 68 65 6E 2F 63 6F 6F 6B 5F 73 6C 69 63 65"));
      return mplew.getPacket();
    }
    
    public static byte[] Menu(List<MapleTyoonKitchen.MapleTyoonKitchenRecipe> mty, List<MapleTyoonKitchen> mtkc) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.TYOONKITCHEN_MENU.getValue());
      mplew.writeInt(mty.size());
      int i = 0;
      for (MapleTyoonKitchen.MapleTyoonKitchenRecipe mt : mty) {
        mplew.writeInt(i);
        mplew.writeInt(mt.getType());
        mplew.writeInt(i);
        mplew.writeInt(mt.CookingCheck(i));
        mplew.writeInt(mt.getDestination());
        mplew.writeInt(mt.getType2());
        mplew.writeInt(mt.getLimittime() - System.currentTimeMillis());
        mplew.writeInt(mt.getRecipe());
        mplew.writeInt(mt.getMoney());
        int size = 0;
        for (Triple<Integer, Integer, Integer> list : mt.getRecipeInfo()) {
          if (((Integer)list.getMid()).intValue() == i)
            size++; 
        } 
        mplew.writeInt(size);
        int i2 = 0;
        for (Triple<Integer, Integer, Integer> list : mt.getRecipeInfo()) {
          if (((Integer)list.getMid()).intValue() == i) {
            mplew.writeInt(i2);
            mplew.writeInt(((Integer)list.getLeft()).intValue());
            i2++;
          } 
        } 
        i++;
      } 
      mplew.write(HexTool.getByteArrayFromHexString("08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 00 00 E8 FE FF FF 81 FF FF FF 42 FF FF FF DB FF FF FF 54 E2 1E 00 21 01 00 00 02 00 BB A7 01 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 00 00 E8 FE FF FF F6 FF FF FF 42 FF FF FF 50 00 00 00 55 E2 1E 00 21 01 00 00 04 00 B0 ED B1 E2 02 00 00 00 02 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 00 00 E8 FE FF FF 11 FF FF FF 42 FF FF FF 6B FF FF FF 56 E2 1E 00 21 01 00 00 04 00 B0 E8 B6 F5 03 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 00 00 E8 FE FF FF A3 FE FF FF 42 FF FF FF FD FE FF FF 57 E2 1E 00 21 01 00 00 04 00 C3 A4 BC D2 04 00 00 00 04 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 00 00 E8 FE FF FF 5F 00 00 00 42 FF FF FF B9 00 00 00 58 E2 1E 00 21 01 00 00 04 00 BB FD BC B1 05 00 00 00 05 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 00 00 32 00 00 00 E0 FE FF FF A0 00 00 00 4E FF FF FF 59 E2 1E 00 21 01 00 00 04 00 B1 C1 B1 E2 06 00 00 00 06 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 00 00 35 00 00 00 7E FF FF FF A3 00 00 00 EC FF FF FF 5A E2 1E 00 21 01 00 00 06 00 B2 FA C0 CC B1 E2 07 00 00 00 07 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 00 00 33 00 00 00 19 00 00 00 A1 00 00 00 87 00 00 00 5B E2 1E 00 21 01 00 00 04 00 BD E4 B1 E2 03 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 F4 01 00 00 00 00 75 FF FF FF E3 FE FF FF E3 FF FF FF 51 FF FF FF 88 E2 1E 00 21 01 00 00 0A 00 63 6F 6F 6B 50 6C 61 74 65 31 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 F4 01 00 00 00 00 73 FF FF FF 7F FF FF FF E1 FF FF FF ED FF FF FF 89 E2 1E 00 21 01 00 00 0A 00 63 6F 6F 6B 50 6C 61 74 65 32 02 00 00 00 02 00 00 00 01 00 00 00 00 00 00 00 F4 01 00 00 00 00 75 FF FF FF 19 00 00 00 E3 FF FF FF 87 00 00 00 8A E2 1E 00 21 01 00 00 0A 00 63 6F 6F 6B 50 6C 61 74 65 33 05 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 DC 05 00 00 00 00 C0 03 00 00 29 FE FF FF 24 04 00 00 8D FE FF FF 40 59 20 00 21 01 00 00 08 00 31 B9 F8 20 BC D5 B4 D4 01 00 00 00 01 00 00 00 02 00 00 00 00 00 00 00 DC 05 00 00 00 00 C0 03 00 00 C0 FE FF FF 24 04 00 00 24 FF FF FF 40 59 20 00 21 01 00 00 08 00 32 B9 F8 20 BC D5 B4 D4 02 00 00 00 02 00 00 00 02 00 00 00 00 00 00 00 DC 05 00 00 00 00 C0 03 00 00 59 FF FF FF 24 04 00 00 BD FF FF FF 40 59 20 00 21 01 00 00 08 00 33 B9 F8 20 BC D5 B4 D4 03 00 00 00 03 00 00 00 02 00 00 00 00 00 00 00 DC 05 00 00 00 00 C0 03 00 00 ED FF FF FF 24 04 00 00 51 00 00 00 40 59 20 00 21 01 00 00 08 00 34 B9 F8 20 BC D5 B4 D4 04 00 00 00 04 00 00 00 02 00 00 00 00 00 00 00 DC 05 00 00 00 00 C0 03 00 00 82 00 00 00 24 04 00 00 E6 00 00 00 40 59 20 00 21 01 00 00 08 00 35 B9 F8 20 BC D5 B4 D4"));
      mplew.writeInt(mtkc.size());
      MapleCharacter chr = null;
      for (MapleTyoonKitchen mt : mtkc) {
        chr = mt.getCooker();
        mplew.writeInt(mt.getCooker().getId());
        mplew.writeInt(mt.getItemid());
        mplew.writeInt(mt.getType());
      } 
      mplew.writeInt(MapleTyoonKitchen.getAllMoney(chr));
      mplew.writeZeroBytes(1000);
      return mplew.getPacket();
    }
    
    public static byte[] Effect(MapleCharacter chr, String eff, int type) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.TYOONKITCHEN_EFFECT.getValue());
      mplew.writeInt(chr.getId());
      mplew.writeMapleAsciiString(eff);
      mplew.write(type);
      return mplew.getPacket();
    }
    
    public static byte[] Unk() {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.TYOONKITCHEN_UNK.getValue());
      return mplew.getPacket();
    }
    
    public static byte[] getClock(int time) {
      MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
      mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
      mplew.writeInt(time);
      return mplew.getPacket();
    }
  }
  
  public static byte[] ShowActionEffect(MapleCharacter chr, int type, int motion, int time) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SHOW_ACTION_EFFECT.getValue());
    mplew.writeInt(chr.getId());
    mplew.write(type);
    mplew.writeInt(motion);
    mplew.writeInt(time);
    mplew.writeInt(0);
    mplew.write(0);
    return mplew.getPacket();
  }
}
