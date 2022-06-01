package client.messages.commands;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.Skill;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.channel.handler.InterServerHandler;
import handling.login.handler.AutoRegister;
import handling.world.World;
import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import provider.MapleData;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.NPCScriptManager;
import scripting.PortalScriptManager;
import scripting.ReactorScriptManager;
import server.CashItemFactory;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.Setting;
import server.games.BattleGroundGameHandler;
import server.games.BloomingRace;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MapleNPC;
import server.life.MobSkill;
import server.life.OverrideMonsterStats;
import server.life.PlayerNPC;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleReactor;
import server.maps.MapleReactorFactory;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.StringUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.PacketHelper;

public class SuperGMCommand {
  public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
    return ServerConstants.PlayerGMRank.SUPERGM;
  }
  
  public static class 영구밴 extends InternCommand.밴 {}
  
  public static class 영구밴해제 extends 밴해제 {}
  
  public static class 밴해제 extends CommandExecute {
    protected boolean hellban = false;
    
    private String getCommand() {
      if (this.hellban)
        return "영구밴해제"; 
      return "밴해제";
    }
    
    public int execute(MapleClient c, String[] splitted) {
      byte ret;
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(6, "[Syntax] !" + getCommand() + " <IGN>");
        return 0;
      } 
      if (this.hellban) {
        ret = MapleClient.unHellban(splitted[1]);
      } else {
        ret = MapleClient.unban(splitted[1]);
      } 
      if (ret == -2) {
        c.getPlayer().dropMessage(6, "[" + getCommand() + "] SQL error.");
        return 0;
      } 
      if (ret == -1) {
        c.getPlayer().dropMessage(6, "[" + getCommand() + "] The character does not exist.");
        return 0;
      } 
      c.getPlayer().dropMessage(6, "[" + getCommand() + "] Successfully unbanned!");
      byte ret_ = MapleClient.unbanIPMacs(splitted[1]);
      if (ret_ == -2) {
        c.getPlayer().dropMessage(6, "[UnbanIP] SQL error.");
      } else if (ret_ == -1) {
        c.getPlayer().dropMessage(6, "[UnbanIP] The character does not exist.");
      } else if (ret_ == 0) {
        c.getPlayer().dropMessage(6, "[UnbanIP] No IP or Mac with that character exists!");
      } else if (ret_ == 1) {
        c.getPlayer().dropMessage(6, "[UnbanIP] IP/Mac -- one of them was found and unbanned.");
      } else if (ret_ == 2) {
        c.getPlayer().dropMessage(6, "[UnbanIP] Both IP and Macs were unbanned.");
      } 
      return (ret_ > 0) ? 1 : 0;
    }
  }
  
  public static class 아이피밴해제 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(6, "[Syntax] !unbanip <IGN>");
        return 0;
      } 
      byte ret = MapleClient.unbanIPMacs(splitted[1]);
      if (ret == -2) {
        c.getPlayer().dropMessage(6, "[UnbanIP] SQL error.");
      } else if (ret == -1) {
        c.getPlayer().dropMessage(6, "[UnbanIP] The character does not exist.");
      } else if (ret == 0) {
        c.getPlayer().dropMessage(6, "[UnbanIP] No IP or Mac with that character exists!");
      } else if (ret == 1) {
        c.getPlayer().dropMessage(6, "[UnbanIP] IP/Mac -- one of them was found and unbanned.");
      } else if (ret == 2) {
        c.getPlayer().dropMessage(6, "[UnbanIP] Both IP and Macs were unbanned.");
      } 
      if (ret > 0)
        return 1; 
      return 0;
    }
  }
  
  public static class 디스펠 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dispelDebuffs();
      return 1;
    }
  }
  
  public static class 관리자권한부여 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        MapleCharacter player = null;
        player = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
        if (player != null) {
          byte number = Byte.parseByte(splitted[2]);
          player.setGMLevel(number);
          player.dropMessage(5, "[알림] " + splitted[1] + " 플레이어가 GM레벨 " + splitted[2] + " (으)로 설정되었습니다.");
        } 
        c.getPlayer().dropMessage(5, "[알림] " + splitted[1] + " 플레이어가 GM레벨 " + splitted[2] + " (으)로 설정되었습니다.");
      } 
      return 1;
    }
  }
  
  public static class 캐릭터좌표이동 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        MapleCharacter player = null;
        player = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
        if (player != null)
          player.getClient().getSession().writeAndFlush(CField.onUserTeleport(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]))); 
      } 
      return 1;
    }
  }
  
  public static class 키벨류조작 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 5) {
        c.getPlayer().dropMessage(6, "!키벨류조작 <닉네임> <번호> <키> <벨류>");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "같은 채널에 없는듯?");
      } else {
        int t = Integer.parseInt(splitted[2]);
        String key = splitted[3];
        String value = splitted[4];
        chrs.setKeyValue(t, key, value);
      } 
      return 1;
    }
  }
  
  public static class 계정키벨류조작 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 5) {
        c.getPlayer().dropMessage(6, "!계정키벨류조작 <닉네임> <키> <벨류>");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "같은 채널에 없는듯?");
      } else {
        String key = splitted[2];
        String value = splitted[3];
        chrs.getClient().setKeyValue(key, value);
      } 
      return 1;
    }
  }
  
  public static class 모두저장 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "게임 데이터 저장을 시작합니다. 잠시 렉이 걸려도 나가지 말아주세요."));
      int saved = 0;
      if (splitted[1] == "-1") {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
          for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
            chr.saveToDB(false, false);
            chr.dropMessage(5, "저장되었습니다.");
            saved++;
          } 
        } 
      } else {
        ChannelServer ch = ChannelServer.getInstance(Integer.parseInt(splitted[1]));
        if (ch != null) {
          for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values()) {
            chr.saveToDB(false, false);
            chr.dropMessage(5, "저장되었습니다.");
            saved++;
          } 
        } else {
          c.getPlayer().dropMessageGM(6, "존재하지 않는 채널입니다.");
        } 
      } 
      World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "총  " + saved + "명의 데이터가 저장되었습니다."));
      return 1;
    }
  }
  
  public static class 스킬마스터 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        MapleData data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(StringUtil.getLeftPaddedStr(splitted[1], '0', 3) + ".img");
        byte maxLevel = 0;
        for (MapleData skill : data) {
          if (skill != null)
            for (MapleData skillId : skill.getChildren()) {
              if (!skillId.getName().equals("icon")) {
                maxLevel = (byte)MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"), 0);
                if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0)
                  c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel, SkillFactory.getSkill(Integer.parseInt(skillId.getName())).isFourthJob() ? maxLevel : 0); 
              } 
            }  
        } 
        c.getPlayer().dropMessage(-1, "해당 직업의 스킬을 모두 최대레벨로 올렸습니다.");
      } catch (Exception e) {
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class 맥스스킬 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Skill.img").getChildren()) {
        try {
          Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
          if (skill.getId() < 1009 || skill.getId() > 1011);
          c.getPlayer().changeSkillLevel(skill, (byte)skill.getMaxLevel(), (byte)skill.getMaxLevel());
        } catch (NumberFormatException nfe) {
          break;
        } catch (NullPointerException npe) {}
      } 
      return 1;
    }
  }
  
  public static class 스킬주기 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      Skill skill = SkillFactory.getSkill(Integer.parseInt(splitted[2]));
      byte level = (byte)CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);
      byte masterlevel = (byte)CommandProcessorUtil.getOptionalIntArg(splitted, 4, 1);
      if (level > skill.getMaxLevel())
        level = (byte)skill.getMaxLevel(); 
      if (masterlevel > skill.getMaxLevel())
        masterlevel = (byte)skill.getMaxLevel(); 
      victim.changeSingleSkillLevel(skill, level, masterlevel);
      return 1;
    }
  }
  
  public static class 인벤잠금해제 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      Map<Item, MapleInventoryType> eqs = new HashMap<>();
      boolean add = false;
      if (splitted.length < 2 || splitted[1].equals("모두")) {
        for (MapleInventoryType type : MapleInventoryType.values()) {
          for (Item item : c.getPlayer().getInventory(type)) {
            if (ItemFlag.LOCK.check(item.getFlag())) {
              item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
              add = true;
            } 
            if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
              item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
              add = true;
            } 
            if (add)
              eqs.put(item, type); 
            add = false;
          } 
        } 
      } else if (splitted[1].equals("장착장비")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).newList()) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.EQUIP); 
          add = false;
        } 
      } else if (splitted[1].equals("장비")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.EQUIP); 
          add = false;
        } 
      } else if (splitted[1].equals("소비")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.USE)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.USE); 
          add = false;
        } 
      } else if (splitted[1].equals("설치")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.SETUP); 
          add = false;
        } 
      } else if (splitted[1].equals("기타")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.ETC); 
          add = false;
        } 
      } else if (splitted[1].equals("캐시")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.CASH)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.CASH); 
          add = false;
        } 
      } else {
        c.getPlayer().dropMessage(6, "[모두/장착장비/장비/소비/설치/기타/캐시]");
      } 
      for (Map.Entry<Item, MapleInventoryType> eq : eqs.entrySet())
        c.getPlayer().forceReAddItem_NoUpdate(((Item)eq.getKey()).copy(), eq.getValue()); 
      return 1;
    }
  }
  
  public static class 드롭 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int itemId = Integer.parseInt(splitted[1]);
      short quantity = (short)CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
      MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
      if (GameConstants.isPet(itemId)) {
        c.getPlayer().dropMessage(5, "Please purchase a pet from the cash shop instead.");
      } else if (!ii.itemExists(itemId)) {
        c.getPlayer().dropMessage(5, itemId + " does not exist");
      } else {
        Item toDrop;
        if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
          toDrop = ii.getEquipById(itemId);
        } else {
          toDrop = new Item(itemId, (short)0, quantity, 0);
        } 
        if (!c.getPlayer().isAdmin()) {
          toDrop.setGMLog(c.getPlayer().getName() + " used !drop");
          toDrop.setOwner(c.getPlayer().getName());
        } 
        c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
      } 
      return 1;
    }
  }
  
  public static class 결혼 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need <name> <itemid>");
        return 0;
      } 
      int itemId = Integer.parseInt(splitted[2]);
      if (!GameConstants.isEffectRing(itemId)) {
        c.getPlayer().dropMessage(6, "Invalid itemID.");
      } else {
        MapleCharacter fff = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
        if (fff == null) {
          c.getPlayer().dropMessage(6, "Player must be online");
        } else {
          long[] ringID = { MapleInventoryIdentifier.getInstance(), MapleInventoryIdentifier.getInstance() };
          try {
            MapleCharacter[] chrz = { fff, c.getPlayer() };
            for (int i = 0; i < chrz.length; i++) {
              Equip eq = (Equip)MapleItemInformationProvider.getInstance().getEquipById(itemId, ringID[i]);
              if (eq == null) {
                c.getPlayer().dropMessage(6, "Invalid itemID.");
                return 0;
              } 
              MapleInventoryManipulator.addbyItem(chrz[i].getClient(), eq.copy());
              chrz[i].dropMessage(6, "Successfully married with " + chrz[(i == 0) ? 1 : 0].getName());
            } 
            MapleRing.addToDB(itemId, c.getPlayer(), fff.getName(), fff.getId(), ringID);
          } catch (SQLException e) {
            e.printStackTrace();
          } 
        } 
      } 
      return 1;
    }
  }
  
  public static class 포인트지급급급급 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need playername and amount.");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
      } else {
        chrs.setPoints(chrs.getPoints() + Integer.parseInt(splitted[2]));
        c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getPoints() + " points, after giving " + splitted[2] + ".");
      } 
      return 1;
    }
  }
  
  public static class 스킬포인트트트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need playername and amount.");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
      } else {
        chrs.gainDonationPoint(Integer.parseInt(splitted[2]));
        c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getDonationPoint() + " Dpoints, after giving " + splitted[2] + ".");
      } 
      return 1;
    }
  }
  
//  public static class 코인지급급급급 extends CommandExecute {
//    public int execute(MapleClient c, String[] splitted) {
//      if (splitted.length < 3) {
//        c.getPlayer().dropMessage(6, "Need playername and amount.");
//        return 0;
//      } 
//      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
//      if (chrs == null) {
//        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
//      } else {
//        chrs.AddStarDustCoin(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
//        c.getPlayer().dropMessage(6, "ok");
//      } 
//      return 1;
//    }
//  }
  
  public static class 홍보포인트지급 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need playername and amount.");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
      } else {
        chrs.gainHPoint(Integer.parseInt(splitted[2]));
        c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getHPoint() + " Hpoints, after giving " + splitted[2] + ".");
      } 
      return 1;
    }
  }
  
  public static class V포인트지급급급급 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need playername and amount.");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
      } else {
        chrs.setVPoints(chrs.getVPoints() + Integer.parseInt(splitted[2]));
        c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getVPoints() + " vpoints, after giving " + splitted[2] + ".");
      } 
      return 1;
    }
  }
  
  public static class ResetOther extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[2])).forfeit(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]));
      return 1;
    }
  }
  
  public static class FStartOther extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[2])).forceStart(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]), Integer.parseInt(splitted[3]), (splitted.length > 4) ? splitted[4] : null);
      return 1;
    }
  }
  
  public static class FCompleteOther extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[2])).forceComplete(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]), Integer.parseInt(splitted[3]));
      return 1;
    }
  }
  
  public static class 보스리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleLifeFactory.getMonsterStats().clear();
      Setting.setting();
      Setting.setting2();
      return 1;
    }
  }
  
  public static class 리엑터스폰 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleReactor reactor = new MapleReactor(MapleReactorFactory.getReactor(Integer.parseInt(splitted[1])), Integer.parseInt(splitted[1]));
      reactor.setDelay(-1);
      c.getPlayer().getMap().spawnReactorOnGroundBelow(reactor, new Point((c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y - 20));
      return 1;
    }
  }
  
  public static class 현재맵엔피시 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      c.getPlayer().dropMessage(6, "현재 맵에 있는 엔피시 리스트입니다.");
      for (MapleMapObject mo : map.getAllNPCs())
        c.getPlayer().dropMessage(6, ((MapleNPC)mo).getId() + " : " + ((MapleNPC)mo).getName()); 
      return 1;
    }
  }
  
  public static class 몬스터데미지OID extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      int targetId = Integer.parseInt(splitted[1]);
      int damage = Integer.parseInt(splitted[2]);
      MapleMonster monster = map.getMonsterByOid(targetId);
      if (monster != null) {
        map.broadcastMessage(MobPacket.damageMonster(targetId, damage, false));
        monster.damage(c.getPlayer(), damage, false);
      } 
      return 1;
    }
  }
  
  public static class 올몹데미지 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      double range = Double.POSITIVE_INFINITY;
      if (splitted.length > 1) {
        int irange = Integer.parseInt(splitted[1]);
        if (splitted.length <= 2) {
          range = (irange * irange);
        } else {
          map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
        } 
      } 
      if (map == null) {
        c.getPlayer().dropMessage(6, "Map does not exist");
        return 0;
      } 
      int damage = Integer.parseInt(splitted[1]);
      for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        MapleMonster mob = (MapleMonster)monstermo;
        map.broadcastMessage(MobPacket.damageMonster(mob.getObjectId(), damage, false));
        mob.damage(c.getPlayer(), damage, false);
      } 
      return 1;
    }
  }
  
  public static class 몹데미지 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      double range = Double.POSITIVE_INFINITY;
      int damage = Integer.parseInt(splitted[1]);
      for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        MapleMonster mob = (MapleMonster)monstermo;
        if (mob.getId() == Integer.parseInt(splitted[2])) {
          map.broadcastMessage(MobPacket.damageMonster(mob.getObjectId(), damage, false));
          mob.damage(c.getPlayer(), damage, false);
        } 
      } 
      return 1;
    }
  }
  
  public static class 현재맵 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      c.getPlayer().dropMessage(6, "현재 맵 : " + map.getId() + " - " + map.getStreetName() + " : " + map.getMapName());
      return 1;
    }
  }
  
  public static class 몬스터소환개체 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      double range = Double.POSITIVE_INFINITY;
      if (splitted.length > 1) {
        int irange = Integer.parseInt(splitted[1]);
        if (splitted.length <= 2) {
          range = (irange * irange);
        } else {
          map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
        } 
      } 
      if (map == null) {
        c.getPlayer().dropMessage(6, "맵이 존재하지 않습니다.");
        return 1;
      } 
      for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        MapleMonster mob = (MapleMonster)monstermo;
        c.getPlayer().dropMessage(6, "몬스터 " + mob.toString());
        if (mob.getStats().isBoss())
          for (MobSkill msi : mob.getSkills())
            c.getPlayer().dropMessageGM(5, msi.getSkillId() + "스킬레벨 : " + msi.getSkillLevel() + "  체력 : " + msi.getHP() + "  한번만" + msi.isOnlyOtherSkill() + " 다른스킬 : " + msi.isOnlyFsm());  
      } 
      return 1;
    }
  }
  
  public static class 킬몹 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      double range = Double.POSITIVE_INFINITY;
      if (splitted.length > 1) {
        int irange = Integer.parseInt(splitted[1]);
        if (splitted.length <= 2) {
          range = (irange * irange);
        } else {
          map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
        } 
      } 
      if (map == null) {
        c.getPlayer().dropMessage(6, "맵이 존재하지 않습니다.");
        return 0;
      } 
      for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        MapleMonster mob = (MapleMonster)monstermo;
        map.killMonster(mob, c.getPlayer(), true, false, (byte)1);
      } 
      return 1;
    }
  }
  
  public static class 엔피시 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int npcId = Integer.parseInt(splitted[1]);
      MapleNPC npc = MapleLifeFactory.getNPC(npcId);
      if (npc != null && !npc.getName().equals("MISSINGNO")) {
        npc.setPosition(c.getPlayer().getPosition());
        npc.setCy((c.getPlayer().getPosition()).y);
        npc.setRx0((c.getPlayer().getPosition()).x);
        npc.setRx1((c.getPlayer().getPosition()).x);
        npc.setFh(c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        npc.setCustom(true);
        c.getPlayer().getMap().addMapObject(npc);
        c.getPlayer().getMap().broadcastMessage(CField.NPCPacket.spawnNPC(npc, true));
      } else {
        c.getPlayer().dropMessage(6, "WZ데이터에 없는 엔피시코드를 입력하였습니다.");
        return 0;
      } 
      return 1;
    }
  }
  
  public static class 스공 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter chr = null;
      if (splitted.length <= 1) {
        chr = c.getPlayer();
      } else {
        chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      } 
      for (Skill sk : chr.getSkills().keySet()) {
        if (!PacketHelper.jobskill(c.getPlayer(), sk.getId()))
          System.out.println(sk.getName() + "(" + sk.getId() + ") : " + chr.getSkillLevel(sk.getId())); 
      } 
      if (chr != null) {
        String info = "#fs11##b" + chr.getName() + "#k님의 정보 입니다.\r\n\r\n";
        info = info + "MAXHP : " + chr.getStat().getCurrentMaxHp() + "\r\n";
        info = info + "MAXMP : " + chr.getStat().getCurrentMaxMp(chr) + "\r\n";
        info = info + "STR : " + chr.getStat().getTotalStr() + "\r\n";
        info = info + "DEX : " + chr.getStat().getTotalDex() + "\r\n";
        info = info + "INT : " + chr.getStat().getTotalInt() + "\r\n";
        info = info + "LUK : " + chr.getStat().getTotalLuk() + "\r\n";
        info = info + "숙련도 : " + chr.getStat().getMastery() + "  공격속도 : " + GameConstants.getJobAttackSpeed(c.getPlayer()) + " 단계\r\n";
        info = info + "공격력 : " + chr.getStat().getTotalWatk() + "   마력 : " + chr.getStat().getTotalMagic() + "\r\n\r\n";
        info = info + "스탯 공격력 : #e" + chr.getStat().BeforeStatWatk(chr) + " ~ " + chr.getStat().AfterStatWatk(chr) + "#n\r\n";
        info = info + "데미지 : #d" + chr.getStat().getDamagePercent() + "%#k  보스 데미지 : #d" + chr.getStat().getBossDamage() + "%#k\r\n";
        info = info + "최종 데미지 : #d" + chr.getStat().getFinalDamage() + "%#k  방어율 무시 : #d" + chr.getStat().getIgnoreMobpdpR() + "%#k\r\n";
        info = info + "크리티컬 확률 : #d" + chr.getStat().getCritical_rate() + "%#k\r\n";
        info = info + "크리티컬 데미지 : #d" + chr.getStat().getCritical_damage() + "%#k\r\n";
        info = info + "상태이상내성 : #d" + chr.getStat().getASR() + "#k  스탠스 : #d" + chr.getStat().getStance() + "#k\r\n";
        info = info + "스타포스 : #d" + chr.getStat().getStarforce() + "#k  아케인포스 : #d" + chr.getStat().getArc() + "#k\r\n ";
        c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkText(9062240, info));
      } 
      return 1;
    }
  }
  
  public static class 고정엔피시 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int npcId = Integer.parseInt(splitted[1]);
      MapleNPC npc = MapleLifeFactory.getNPC(npcId);
      if (npc != null && !npc.getName().equals("MISSINGNO")) {
        npc.setPosition(c.getPlayer().getPosition());
        npc.setCy((c.getPlayer().getPosition()).y);
        npc.setRx0((c.getPlayer().getPosition()).x);
        npc.setRx1((c.getPlayer().getPosition()).x);
        npc.setFh(c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        c.getPlayer().getMap().addMapObject(npc);
        c.getPlayer().getMap().broadcastMessage(CField.NPCPacket.spawnNPC(npc, true));
      } else {
        c.getPlayer().dropMessage(6, "WZ에 존재하지 않는 NPC를 입력했습니다.");
      } 
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        String sql = "INSERT INTO `spawn`(`lifeid`, `rx0`, `rx1`, `cy`, `fh`, `type`, `dir`, `mapid`, `mobTime`) VALUES (? ,? ,? ,? ,? ,? ,? ,? ,?)";
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, npcId);
        ps.setInt(2, (c.getPlayer().getPosition()).x - 50);
        ps.setInt(3, (c.getPlayer().getPosition()).x + 50);
        ps.setInt(4, (c.getPlayer().getPosition()).y);
        ps.setInt(5, c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        ps.setString(6, "n");
        ps.setInt(7, (c.getPlayer().getFacingDirection() == 1) ? 0 : 1);
        ps.setInt(8, c.getPlayer().getMapId());
        ps.setInt(9, 0);
        ps.executeUpdate();
        ps.close();
        con.close();
      } catch (Exception e) {
        System.err.println("[오류] 엔피시를 고정 등록하는데 실패했습니다.");
        e.printStackTrace();
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
      return 1;
    }
  }
  
  public static class 고정리엑터 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int npcId = Integer.parseInt(splitted[1]);
      MapleReactor reactor = null;
      try {
        reactor = new MapleReactor(MapleReactorFactory.getReactor(Integer.parseInt(splitted[1])), Integer.parseInt(splitted[1]));
      } catch (Exception e) {
        c.getPlayer().dropMessage(5, "존재하지 않는 리엑터 입니다.");
        return 1;
      } 
      if (splitted[2] == null) {
        c.getPlayer().dropMessage(5, "리엑터 재생성 값을 입력해주세요 기본값 : -1");
        return 1;
      } 
      reactor.setDelay(-1);
      c.getPlayer().getMap().spawnReactorOnGroundBelow(reactor, new Point((c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y));
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        String sql = "INSERT INTO `spawn`(`lifeid`, `rx0`, `rx1`, `cy`, `fh`, `type`, `dir`, `mapid`, `mobTime`) VALUES (? ,? ,? ,? ,? ,? ,? ,? ,?)";
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, npcId);
        ps.setInt(2, (c.getPlayer().getPosition()).x);
        ps.setInt(3, (c.getPlayer().getPosition()).x);
        ps.setInt(4, (c.getPlayer().getPosition()).y);
        ps.setInt(5, c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        ps.setString(6, "r");
        ps.setInt(7, (c.getPlayer().getFacingDirection() == 1) ? 0 : 1);
        ps.setInt(8, c.getPlayer().getMapId());
        ps.setInt(9, Integer.parseInt(splitted[2]));
        ps.executeUpdate();
        ps.close();
        con.close();
      } catch (Exception e) {
        System.err.println("[오류] 리엑터를 고정 등록하는데 실패했습니다.");
        e.printStackTrace();
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
      return 1;
    }
  }
  
  public static class 플레이어엔피시 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        c.getPlayer().dropMessage(6, "플레이어엔피시를 제작중입니다.");
        MapleCharacter chhr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
        if (chhr == null) {
          c.getPlayer().dropMessage(6, splitted[1] + "님은 온라인이 아니거나, 존재하지 않는 닉네임입니다. 상태를 확인하고 다시 시도해주세요.");
          return 0;
        } 
        PlayerNPC npc = new PlayerNPC(chhr, Integer.parseInt(splitted[2]), c.getPlayer().getMap(), c.getPlayer());
        npc.addToServer();
        c.getPlayer().dropMessage(6, "완료!");
      } catch (Exception e) {
        c.getPlayer().dropMessage(6, "플레이어엔피시를 제작하는데 실패하였습니다. : " + e.getMessage());
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class 오프플레이어엔피시 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        c.getPlayer().dropMessage(6, "오프 플레이어엔피시를 제작중입니다.");
        MapleClient cs = new MapleClient(c.getSession(), null, null);
        MapleCharacter chhr = MapleCharacter.loadCharFromDB(MapleCharacterUtil.getIdByName(splitted[1]), cs, false);
        if (chhr == null) {
          c.getPlayer().dropMessage(6, splitted[1] + "님이 존재하지 않습니다.");
          return 0;
        } 
        PlayerNPC npc = new PlayerNPC(chhr, Integer.parseInt(splitted[2]), c.getPlayer().getMap(), c.getPlayer());
        npc.addToServer();
        c.getPlayer().dropMessage(6, "완료!");
      } catch (Exception e) {
        c.getPlayer().dropMessage(6, "오프 플레이어엔피시를 제작하는데 실패하였습니다. : " + e.getMessage());
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class 플레이어엔피시삭제 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        c.getPlayer().dropMessage(6, "플레이어엔피시가 삭제중 입니다.");
        MapleNPC npc = c.getPlayer().getMap().getNPCByOid(Integer.parseInt(splitted[1]));
        if (npc instanceof PlayerNPC) {
          ((PlayerNPC)npc).destroy(true);
          c.getPlayer().dropMessage(6, "완료!");
        } else {
          c.getPlayer().dropMessage(6, "사용법 : !플레이어엔피시삭제 [엔피시코드]");
        } 
      } catch (Exception e) {
        c.getPlayer().dropMessage(6, "엔피시를 삭제하는데 실패하였습니다. : " + e.getMessage());
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class 서버메세지 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      String outputMessage = StringUtil.joinStringFrom(splitted, 1);
      for (ChannelServer cserv : ChannelServer.getAllInstances())
        cserv.setServerMessage(outputMessage); 
      return 1;
    }
  }
  
  public static class 스폰 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMonster onemob;
      int mid = Integer.parseInt(splitted[1]);
      int num = Math.min(CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1), 500);
      Integer level = CommandProcessorUtil.getNamedIntArg(splitted, 1, "lvl");
      Long hp = CommandProcessorUtil.getNamedLongArg(splitted, 1, "hp");
      Long exp = CommandProcessorUtil.getNamedLongArg(splitted, 1, "exp");
      Double php = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "php");
      Double pexp = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "pexp");
      try {
        onemob = MapleLifeFactory.getMonster(mid);
      } catch (RuntimeException e) {
        c.getPlayer().dropMessage(5, "Error: " + e.getMessage());
        return 0;
      } 
      if (onemob == null) {
        c.getPlayer().dropMessage(5, "Mob does not exist");
        return 0;
      } 
      long newhp = 0L;
      long newexp = 0L;
      if (hp != null) {
        newhp = hp.longValue();
      } else if (php != null) {
        newhp = (long)(onemob.getMobMaxHp() * php.doubleValue() / 100.0D);
      } else {
        newhp = onemob.getMobMaxHp();
      } 
      if (exp != null) {
        newexp = exp.longValue();
      } else if (pexp != null) {
        newexp = (long)(onemob.getMobExp() * pexp.doubleValue() / 100.0D);
      } else {
        newexp = onemob.getMobExp();
      } 
      if (newhp < 1L)
        newhp = 1L; 
      OverrideMonsterStats overrideStats = new OverrideMonsterStats(newhp, onemob.getMobMaxMp(), newexp, false);
      for (int i = 0; i < num; i++) {
        MapleMonster mob = MapleLifeFactory.getMonster(mid);
        mob.setHp(newhp);
        if (level != null) {
          mob.changeLevel(level.intValue(), false);
        } else {
          mob.setOverrideStats(overrideStats);
        } 
        c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, c.getPlayer().getPosition());
        c.getPlayer().dropMessage(6, "oid : " + mob.getObjectId());
      } 
      return 1;
    }
  }
  
  public static class 캐시샵 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      InterServerHandler.EnterCS(c, c.getPlayer(), false);
      return 1;
    }
  }
  
  public static class 캐시샵리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      Setting.CashShopSetting();
      CashItemFactory.getInstance().initialize();
      c.getPlayer().dropMessage(-8, "[GM 알림] 캐시샵 메인 리셋이 완료 되었습니다.");
      return 1;
    }
  }
  
  public static class 패킷 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      System.out.println(c.getCreated());
      System.out.println(c.getCreated().getMonth() + 1);
      System.out.println(c.getCreated().getDate());
      return 1;
    }
  }
  
  public static class 블루밍레이스 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      BloomingRace.RaceIinviTation();
      return 1;
    }
  }
  
  public static class 싸전귀직업 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().setBattleGrondJobName("" + splitted[1]);
      c.getPlayer().dropMessage(5, "직업 : " + splitted[1] + "로 지정 완료.");
      return 1;
    }
  }
  
  public static class 싸전귀 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      BattleGroundGameHandler.BattleGroundIinviTation();
      return 1;
    }
  }
  
  public static class 맵 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap target = null;
      ChannelServer cserv = c.getChannelServer();
      target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[1]));
      MaplePortal targetPortal = null;
      if (splitted.length > 2)
        try {
          targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
        } catch (IndexOutOfBoundsException e) {
          c.getPlayer().dropMessage(5, "Invalid portal selected.");
        } catch (NumberFormatException numberFormatException) {} 
      if (targetPortal == null)
        targetPortal = target.getPortal(0); 
      c.getPlayer().changeMap(target, targetPortal);
      return 1;
    }
  }
  
  public static class 맵리로딩 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int mapId = Integer.parseInt(splitted[1]);
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        if (cserv.getMapFactory().isMapLoaded(mapId) && cserv.getMapFactory().getMap(mapId).getCharactersSize() > 0) {
          c.getPlayer().dropMessage(5, "There exists characters on channel " + cserv.getChannel());
          return 0;
        } 
      } 
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        if (cserv.getMapFactory().isMapLoaded(mapId))
          cserv.getMapFactory().removeMap(mapId); 
      } 
      return 1;
    }
  }
  
  public static class 리스폰 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().respawn(true);
      return 1;
    }
  }
  
  public static class 맥스메소 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().gainMeso(9999999999L - c.getPlayer().getMeso(), true);
      return 1;
    }
  }
  
  public static class 메소 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().gainMeso(Long.parseLong(splitted[1]), true);
      return 1;
    }
  }
  
  public static class 캐시지급급급급 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().modifyCSPoints(1, Integer.parseInt(splitted[1]), true);
      return 1;
    }
  }
  public static class DebugLucidLaser extends CommandExecute { // WUT
    public int execute (MapleClient c, String[] splitted) {
      List<Integer> laserIntervals;
      Point pos = new Point(665,-490);
      MapleMonster monster = MapleLifeFactory.getMonster(8880151);
      laserIntervals = new ArrayList<Integer>() {
        {
          for (int i = 0; i < 15; ++i) {
            this.add(500);
          }
        }
      };
      monster.setCustomInfo(23807, 0, 10000);
      c.getPlayer().warp(450004250);
      c.getPlayer().getMap().spawnMonsterOnGroundBelow(monster,pos);
      c.getPlayer().getMap().broadcastMessage(MobPacket.BossLucid.doLaserRainSkill(4500, laserIntervals));
      c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("루시드가 강력한 공격을 사용하려 합니다!", 222, 2000));
      c.getPlayer().dropMessageGM(6, "[DEBUG] 루시드 레이저 패턴");
      return -1;
    }
  }
  
  public static class 후원포인트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (victim == null) {
        c.getPlayer().dropMessage(1, splitted[1] + " 캐릭터는 접속중이 아닙니다.");
        return 0;
      } 
      c.getPlayer().dropMessage(1, splitted[1] + "님에게 " + splitted[2] + " Happy !  포인트를 지급 하였습니다.");
      victim.modifyCSPoints(1, Integer.parseInt(splitted[2]), true);
      victim.sendNote(splitted[1], "메이플GM", "" + splitted[1] + "용사님! 용사님의 소중한 후원 감사드립니다, 용사님의 기대치에 부응하도록 많은 노력을 하겠습니다. 용사님에게 후원 포인트 " + splitted[2] + "포인트를 지급 해드렸습니다. 포인트 확인을 해주시길 바랍니다.", 0, 6, 0);
      return 1;
    }
  }
  
  public static class 홍보포인트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (victim == null) {
        c.getPlayer().dropMessage(1, splitted[1] + "캐릭터는 접속중이 아닙니다.");
        return 0;
      } 
      c.getPlayer().dropMessage(1, splitted[1] + "님에게 " + splitted[2] + " 홍보 포인트를 지급 하였습니다.");
      victim.modifyCSPoints(2, Integer.parseInt(splitted[2]), true);
      victim.sendNote(splitted[1], "메이플GM", "" + splitted[1] + "용사님! 용사님의 소중한 홍보 감사드립니다, 용사님의 기대치에 부응하도록 많은 노력을 하겠습니다. 용사님에게 홍보 포인트 " + splitted[2] + "포인트를 지급 해드렸습니다. 포인트 확인을 해주시길 바랍니다.", 0, 6, 0);
      return 1;
    }
  }
  
      public static class 후원포인트지급급 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, "Need playername and amount.");
                return 0;
            }
            MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (chrs == null) {
                c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
            } else {
                chrs.gainDonationPoint(Integer.parseInt(splitted[2]));
                //chrs.gainnDonationPoint(Integer.parseInt(splitted[2]));
                c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getDonationPoint() + " Dpoints, after giving " + splitted[2] + ".");
            }
            return 1;
        }
    }
  
  public static class 메이플포인트수정정정정 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().modifyCSPoints(2, Integer.parseInt(splitted[1]), true);
      return 1;
    }
  }
  
  public static class 포인트지급급급급급 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().setPoints(c.getPlayer().getPoints() + Integer.parseInt(splitted[1]));
      return 1;
    }
  }
  
  public static class V포인트트트트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().setVPoints(c.getPlayer().getVPoints() + Integer.parseInt(splitted[1]));
      return 1;
    }
  }
  
  public static class 옵코드리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      SendPacketOpcode.reloadValues();
      RecvPacketOpcode.reloadValues();
      return 1;
    }
  }
  
  public static class 드롭리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMonsterInformationProvider.getInstance().clearDrops();
      ReactorScriptManager.getInstance().clearDrops();
      return 1;
    }
  }
  
  public static class 포탈리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      PortalScriptManager.getInstance().clearScripts();
      return 1;
    }
  }
  
  public static class 엔피시리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      NPCScriptManager.getInstance().scriptClear();
      return 1;
    }
  }
  
  public static class 상점리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleShopFactory.getInstance().clear();
      return 1;
    }
  }
  
  public static class 이벤트리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(6, "이벤트 리셋 시작");
      c.getChannelServer().reloadEvents();
      c.getPlayer().dropMessage(6, "이벤트 리셋 종료");
      return 1;
    }
  }
  
  public static class 맵리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().resetFully();
      return 1;
    }
  }
  
  public static class 퀘스트리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).forfeit(c.getPlayer());
      return 1;
    }
  }
  
  public static class 퀘스트시작 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).start(c.getPlayer(), Integer.parseInt(splitted[2]));
      return 1;
    }
  }
  
  public static class 퀘스트완료 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).complete(c.getPlayer(), Integer.parseInt(splitted[2]), Integer.valueOf(Integer.parseInt(splitted[3])), false);
      return 1;
    }
  }
  
  public static class F퀘스트시작 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).forceStart(c.getPlayer(), Integer.parseInt(splitted[2]), (splitted.length >= 4) ? splitted[3] : null);
      return 1;
    }
  }
  
  public static class F퀘스트완료 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).forceComplete(c.getPlayer(), Integer.parseInt(splitted[2]));
      return 1;
    }
  }
  
  public static class 리엑터데미지 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().getReactorByOid(Integer.parseInt(splitted[1])).hitReactor(c);
      return 1;
    }
  }
  
  public static class 계정생성 extends CommandExecute {//주버
    public int execute(MapleClient c, String[] splitted) {
      String id = splitted[1];
      String pw = splitted[2];
      if (AutoRegister.getAccountExists(id) == true) {
        c.getPlayer().dropMessage(6, "이미 존재하는 계정입니다.");
      } else if (AutoRegister.createAccount(id, pw, "/" + id + ":")) {
        c.getPlayer().dropMessage(6, "계정생성에 성공하였습니다.");
      } else {
        c.getPlayer().dropMessage(6, "계정생성에 실패하였습니다.");
      } 
      return 1;
    }
  }
  
  public static class 리엑터삭제 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      List<MapleMapObject> reactors = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.REACTOR }));
      if (splitted[1].equals("all")) {
        for (MapleMapObject reactorL : reactors) {
          MapleReactor reactor2l = (MapleReactor)reactorL;
          c.getPlayer().getMap().destroyReactor(reactor2l.getObjectId());
        } 
      } else {
        c.getPlayer().getMap().destroyReactor(Integer.parseInt(splitted[1]));
      } 
      return 1;
    }
  }
  
  public static class 리셋리엑터 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().resetReactors(c);
      return 1;
    }
  }
  
  public static class 모두에게쪽지 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length >= 1) {
        String text = StringUtil.joinStringFrom(splitted, 1);
        for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters().values())
          mch.sendNote("[Happy ! ]", text, 6, 0); 
      } else {
        c.getPlayer().dropMessage(6, "Use it like this, !sendallnote <text>");
        return 0;
      } 
      return 1;
    }
  }
  
  public static class 버프스킬 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      SkillFactory.getSkill(Integer.parseInt(splitted[1])).getEffect(Integer.parseInt(splitted[2])).applyTo(c.getPlayer(), true);
      return 0;
    }
  }
  
  public static class 버프아이템 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleItemInformationProvider.getInstance().getItemEffect(Integer.parseInt(splitted[1])).applyTo(c.getPlayer(), true);
      return 0;
    }
  }
  
  public static class 버프아이템EX extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleItemInformationProvider.getInstance().getItemEffectEX(Integer.parseInt(splitted[1])).applyTo(c.getPlayer(), true);
      return 0;
    }
  }
  
  public static class 아이템사이즈 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(6, "Number of items: " + MapleItemInformationProvider.getInstance().getAllItems().size());
      return 0;
    }
  }
  
//  public static class 스타더스트트트트 extends CommandExecute {
//    public int execute(MapleClient c, String[] splitted) {
//      if (splitted.length == 2)
//        if (splitted[0].equals("포인트")) {
//          c.getPlayer().AddStarDustPoint(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), new Point(-1, -1));
//        } else if (splitted[0].equals("코인")) {
//          c.getPlayer().AddStarDustCoin(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
//        }  
//      return 0;
//    }
//  }
  
//  public static class 네오포스 extends CommandExecute {
//    public int execute(MapleClient c, String[] splitted) {
//      if (splitted.length < 3) {
//        c.getPlayer().dropMessage(6, "Need playername and amount.");
//        return 0;
//      } 
//      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[2]);
//      if (chrs == null) {
//        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
//      } else if (Integer.parseInt(splitted[1]) > 3 || Integer.parseInt(splitted[1]) < 1) {
//        c.getPlayer().dropMessage(6, "1 : 네오 스톤 ");
//        c.getPlayer().dropMessage(6, "2 : 네오 젬 ");
//        c.getPlayer().dropMessage(6, "3 : 네오 코어 ");
//      } else {
//        chrs.AddStarDustCoin(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[3]));
//        String coin = (Integer.parseInt(splitted[1]) == 1) ? "네오 스톤" : ((Integer.parseInt(splitted[1]) == 2) ? "네오 젬" : "네오 코어");
//        c.getPlayer().dropMessage(6, splitted[2] + "에게  " + coin + " " + Integer.parseInt(splitted[3]) + "개 지급완료");
//      } 
//      return 0;
//    }
//  }
  
  
  public static class 아무것도안했는데 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getSession().writeAndFlush(CWvsContext.InfoPacket.updateClientInfoQuest(217, "reward=" + GameConstants.getCurrentDate() + ";count=0;uDate=" + FileoutputUtil.CurrentReadable_Date() + ";qState=2;logis=0CL=9;T=20190114093031;use=47;total=" + splitted[1] + ";exp=" + splitted[2]));
      c.getSession().writeAndFlush(CField.UIPacket.openUI(1207));
      return 0;
    }
  }
  
      public static class 패킷출력 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;
            ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
            return 0;
        }
    }

    public static class 리시브출력 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;
            return 0;
        }
    }

    public static class 센드출력 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
            return 0;
        }
    }
  
  public static class 디버그 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted[1].equals("s") || splitted[1].equals("ㄴ")) {
        ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
      } else if (splitted[1].equals("r") || splitted[1].equals("ㄱ")) {
        ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;
      } 
      c.getPlayer().dropMessage(6, "SEND : " + ServerConstants.DEBUG_SEND + "");
      c.getPlayer().dropMessage(6, "RECV : " + ServerConstants.DEBUG_RECEIVE + "");
      return 0;
    }
  }
}
