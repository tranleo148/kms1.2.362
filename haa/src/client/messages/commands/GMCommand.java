package client.messages.commands;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleStat;
import client.SecondaryStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import constants.ServerConstants;
import handling.MapleSaveHandler;
import handling.channel.ChannelServer;
import handling.world.World;
import java.awt.Point;
import java.util.Iterator;
import scripting.EventInstanceManager;
import scripting.EventManager;
import scripting.NPCConversationManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.events.MapleEvent;
import server.events.MapleEventType;
import server.life.MapleMonster;
import server.life.MobSkillFactory;
import server.life.Spawns;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleReactor;
import server.maps.MapleRune;
import server.polofritto.MapleRandomPortal;
import server.shops.MapleShopFactory;
import tools.StringUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class GMCommand {
  public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
    return ServerConstants.PlayerGMRank.GM;
  }
  
  public static class 룬 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted[1] == null) {
        c.getPlayer().dropMessage(6, "Syntax: !룬 <id>");
        return 0;
      } 
      int id = Integer.parseInt(splitted[1]);
      int sppoint = Randomizer.rand(0, (c.getPlayer().getMap()).monsterSpawn.size() - 1);
      MapleReactor ract = c.getPlayer().getMap().getAllReactor().get(0);
      if (ract != null)
        while (sppoint == ract.getSpawnPointNum())
          sppoint = Randomizer.rand(0, (c.getPlayer().getMap()).monsterSpawn.size() - 1);  
      Point poss = ((Spawns)(c.getPlayer().getMap()).monsterSpawn.get(sppoint)).getPosition();
      MapleRune rune = new MapleRune(Randomizer.rand(0, 10), poss.x, poss.y, c.getPlayer().getMap());
      rune.setSpawnPointNum(sppoint);
      c.getPlayer().getMap().spawnRune(rune);
      return 1;
    }
  }
  
  public static class 벅샷 extends CommandExecute {

    @Override
    public int execute(MapleClient c, String[] splitted) {
        if (c.getPlayer().getBuffedEffect(SecondaryStat.Buckshot) != null) {
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.Buckshot);
        } else {
            SkillFactory.getSkill(5321054).getEffect(1).applyTo(c.getPlayer(), 0);
        }
        return 0;
    }
}
  
  public static class 테스트서버 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      ServerConstants.ConnectorSetting = !ServerConstants.ConnectorSetting;
      System.out.println("테스트 서버 " + (ServerConstants.ConnectorSetting ? "해제" : "설정") + " 완료.");
      return 1;
    }
  }
  
  public static class 캐시확인 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter player = c.getPlayer();
      if (splitted.length < 1) {
        c.getPlayer().dropMessage(6, "Syntax: !캐시확인 <itemid>");
        return 0;
      } 
      MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
      player.dropMessage(6, "이 아이템은 캐시가 " + (ii.isCash(Integer.parseInt(splitted[1])) ? "맞습니다." : "아닙니다."));
      return 1;
    }
  }
  
  public static class 인기도 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter player = c.getPlayer();
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(6, "Syntax: !fame <player> <amount>");
        return 0;
      } 
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      int fame = 0;
      try {
        fame = Integer.parseInt(splitted[2]);
      } catch (NumberFormatException nfe) {
        c.getPlayer().dropMessage(6, "Invalid Number...");
        return 0;
      } 
      if (victim != null && player.allowedToTarget(victim)) {
        victim.addFame(fame);
        victim.updateSingleStat(MapleStat.FAME, victim.getFame());
      } 
      return 1;
    }
  }
  
  public static class 인빈서블 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter player = c.getPlayer();
      if (player.isInvincible()) {
        player.setInvincible(false);
        player.dropMessage(6, "Invincibility deactivated.");
      } else {
        player.setInvincible(true);
        player.dropMessage(6, "Invincibility activated.");
      } 
      return 1;
    }
  }
  
  public static class SP extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().setRemainingSp(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 1));
      c.getPlayer().updateSingleStat(MapleStat.AVAILABLESP, 0L);
      return 1;
    }
  }
  
  public static class 직업 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().changeJob(Integer.parseInt(splitted[1]));
      return 1;
    }
  }
  
  public static class 샵 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleShopFactory shop = MapleShopFactory.getInstance();
      int shopId = Integer.parseInt(splitted[1]);
      if (shop.getShop(shopId) != null)
        shop.getShop(shopId).sendShop(c); 
      return 1;
    }
  }
  
  public static class 스킬 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      Skill skill = SkillFactory.getSkill(Integer.parseInt(splitted[1]));
      byte level = (byte)CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
      byte masterlevel = (byte)CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);
      if (level > skill.getMaxLevel())
        level = (byte)skill.getMaxLevel(); 
      if (masterlevel > skill.getMasterLevel())
        masterlevel = (byte)skill.getMasterLevel(); 
      c.getPlayer().changeSkillLevel(skill, level, masterlevel);
      return 1;
    }
  }
  
  public static class 레벨업 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length == 1) {
        if (c.getPlayer().getLevel() < 300)
          c.getPlayer().gainExp(GameConstants.getExpNeededForLevel(c.getPlayer().getLevel()), true, false, true); 
      } else if (splitted.length == 2) {
        int lvup = Integer.parseInt(splitted[1]);
        for (int i = 0; i < lvup && 
          c.getPlayer().getLevel() < 300; i++)
          c.getPlayer().gainExp(GameConstants.getExpNeededForLevel(c.getPlayer().getLevel()), true, false, true); 
      } 
      return 1;
    }
  }
  
  public static class 아이템 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        int itemId = Integer.parseInt(splitted[1]);
        short quantity = (short)CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
        if (!c.getPlayer().isAdmin())
          for (int i : GameConstants.itemBlock) {
            if (itemId == i) {
              c.getPlayer().dropMessage(5, "이 아이템은 GM레벨이 부족해 차단된 아이템입니다.");
              return 0;
            } 
          }  
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (!ii.itemExists(itemId)) {
          c.getPlayer().dropMessage(5, itemId + "코드는 존재하지 않습니다.");
        } else {
          Item item;
          if (GameConstants.isPet(itemId)) {
            MapleInventoryManipulator.addId(c, itemId, (short)1, "", MaplePet.createPet(itemId, -1L), 1L, "", false);
            NPCConversationManager.writeLog("Log/Command_Item.log", c.getPlayer().getName() + " | " + itemId + " (x" + quantity + ")를 아이템 명령어를 통해 얻음.\r\n", true);
            return 1;
          } 
          MapleInventoryType type = GameConstants.getInventoryType(itemId);
          if (type == MapleInventoryType.EQUIP || type == MapleInventoryType.CODY) {
            item = ii.getEquipById(itemId);
          } else {
            item = new Item(itemId, (short)0, quantity, 0);
          } 
          int flag = item.getFlag();
          if (ii.isCash(itemId)) {
            if (type == MapleInventoryType.EQUIP || type == MapleInventoryType.CODY) {
              flag |= ItemFlag.KARMA_EQUIP.getValue();
            } else {
              flag |= ItemFlag.KARMA_USE.getValue();
            } 
            item.setUniqueId(MapleInventoryIdentifier.getInstance());
          } 
          item.setFlag(flag);
          if (!c.getPlayer().isAdmin()) {
            item.setOwner(c.getPlayer().getName());
            item.setGMLog(c.getPlayer().getName() + " 사용법 : !아이템 <아이템코드>");
          } 
          MapleInventoryManipulator.addbyItem(c, item);
          NPCConversationManager.writeLog("Log/Command_Item.log", c.getPlayer().getName() + " | " + itemId + " (x" + quantity + ")를 아이템 명령어를 통해 얻음.\r\n", true);
        } 
      } catch (Exception e) {
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class 레벨 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().setLevel(Short.parseShort(splitted[1]));
      c.getPlayer().levelUp();
      if (c.getPlayer().getExp() < 0L)
        c.getPlayer().gainExp(-c.getPlayer().getExp(), false, false, true); 
      return 1;
    }
  }
  
  public static class 랜덤이벤트시작 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      EventManager em = c.getChannelServer().getEventSM().getEventManager("AutomatedEvent");
      if (em != null)
        em.scheduleRandomEvent(); 
      return 1;
    }
  }
  
  public static class SetEvent extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleEvent.onStartEvent(c.getPlayer());
      return 1;
    }
  }
  
  public static class StartEvent extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (c.getChannelServer().getEvent() == c.getPlayer().getMapId()) {
        MapleEvent.setEvent(c.getChannelServer(), false);
        c.getPlayer().dropMessage(5, "Started the event and closed off");
        return 1;
      } 
      c.getPlayer().dropMessage(5, "!scheduleevent must've been done first, and you must be in the event map.");
      return 0;
    }
  }
  
  public static class 스케쥴이벤트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleEventType type = MapleEventType.getByString(splitted[1]);
      if (type == null) {
        StringBuilder sb = new StringBuilder("Wrong syntax: ");
        for (MapleEventType t : MapleEventType.values())
          sb.append(t.name()).append(","); 
        c.getPlayer().dropMessage(5, sb.toString().substring(0, sb.toString().length() - 1));
        return 0;
      } 
      String msg = MapleEvent.scheduleEvent(type, c.getChannelServer());
      if (msg.length() > 0) {
        c.getPlayer().dropMessage(5, msg);
        return 0;
      } 
      return 1;
    }
  }
  
  public static class RemoveItem extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need <name> <itemid>");
        return 0;
      } 
      MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chr == null) {
        c.getPlayer().dropMessage(6, "This player does not exist");
        return 0;
      } 
      chr.removeAll(Integer.parseInt(splitted[2]), false);
      c.getPlayer().dropMessage(6, "All items with the ID " + splitted[2] + " has been removed from the inventory of " + splitted[1] + ".");
      return 1;
    }
  }
  
  public static class KillMap extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (MapleCharacter map : c.getPlayer().getMap().getCharactersThreadsafe()) {
        if (map != null && !map.isGM()) {
          map.getStat().setHp(0L, map);
          map.getStat().setMp(0L, map);
          map.updateSingleStat(MapleStat.HP, 0L);
          map.updateSingleStat(MapleStat.MP, 0L);
        } 
      } 
      return 1;
    }
  }
  
  public static class SpeakMega extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      World.Broadcast.broadcastSmega(CWvsContext.serverNotice(3, (victim == null) ? c.getChannel() : victim.getClient().getChannel(), (victim == null) ? "" : victim.getName(), (victim == null) ? splitted[1] : (victim.getName() + " : " + StringUtil.joinStringFrom(splitted, 2)), true));
      return 1;
    }
  }
  
  public static class Speak extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (victim == null) {
        c.getPlayer().dropMessage(5, "unable to find '" + splitted[1]);
        return 0;
      } 
      victim.getMap().broadcastMessage(CField.getChatText(victim, StringUtil.joinStringFrom(splitted, 2), victim.isGM(), 0, null));
      return 1;
    }
  }
  
  public static class 디버프 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().giveDebuff(SecondaryStat.Darkness, MobSkillFactory.getMobSkill(121, 1));
      return 1;
    }
  }
  
  public static class SetInstanceProperty extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
      if (em == null || em.getInstances().size() <= 0) {
        c.getPlayer().dropMessage(5, "none");
      } else {
        em.setProperty(splitted[2], splitted[3]);
        for (EventInstanceManager eim : em.getInstances())
          eim.setProperty(splitted[2], splitted[3]); 
      } 
      return 1;
    }
  }
  
  public static class ListInstanceProperty extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
      if (em == null || em.getInstances().size() <= 0) {
        c.getPlayer().dropMessage(5, "none");
      } else {
        for (EventInstanceManager eim : em.getInstances())
          c.getPlayer().dropMessage(5, "Event " + eim.getName() + ", eventManager: " + em.getName() + " iprops: " + eim.getProperty(splitted[2]) + ", eprops: " + em.getProperty(splitted[2])); 
      } 
      return 0;
    }
  }
  
  public static class LeaveInstance extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (c.getPlayer().getEventInstance() == null) {
        c.getPlayer().dropMessage(5, "You are not in one");
      } else {
        c.getPlayer().getEventInstance().unregisterPlayer(c.getPlayer());
      } 
      return 1;
    }
  }
  
  public static class WhosThere extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      StringBuilder builder = (new StringBuilder("Players on Map: ")).append(c.getPlayer().getMap().getCharactersThreadsafe().size()).append(", ");
      for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
        if (builder.length() > 150) {
          builder.setLength(builder.length() - 2);
          c.getPlayer().dropMessage(6, builder.toString());
          builder = new StringBuilder();
        } 
        builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
        builder.append(", ");
      } 
      builder.setLength(builder.length() - 2);
      c.getPlayer().dropMessage(6, builder.toString());
      return 1;
    }
  }
  
  public static class StartInstance extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (c.getPlayer().getEventInstance() != null) {
        c.getPlayer().dropMessage(5, "You are in one");
      } else if (splitted.length > 2) {
        EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
        if (em == null || em.getInstance(splitted[2]) == null) {
          c.getPlayer().dropMessage(5, "Not exist");
        } else {
          em.getInstance(splitted[2]).registerPlayer(c.getPlayer());
        } 
      } else {
        c.getPlayer().dropMessage(5, "!startinstance [eventmanager] [eventinstance]");
      } 
      return 1;
    }
  }
  
  public static class ResetMobs extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().killAllMonsters(false);
      return 1;
    }
  }
  
  public static class KillMonsterByOID extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      int targetId = Integer.parseInt(splitted[1]);
      MapleMonster monster = map.getMonsterByOid(targetId);
      if (monster != null)
        map.killMonster(monster, c.getPlayer(), false, false, (byte)1); 
      return 1;
    }
  }
  
  public static class 엔피시삭제 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().resetNPCs();
      return 1;
    }
  }
  
  public static class 공지 extends CommandExecute {
    protected static int getNoticeType(String typestring) {
      if (typestring.equals("n"))
        return 0; 
      if (typestring.equals("p"))
        return 1; 
      if (typestring.equals("l"))
        return 2; 
      if (typestring.equals("nv"))
        return 5; 
      if (typestring.equals("v"))
        return 5; 
      if (typestring.equals("b"))
        return 6; 
      return -1;
    }
    
    public int execute(MapleClient c, String[] splitted) {
      int joinmod = 1;
      int range = -1;
      if (splitted[1].equals("m")) {
        range = 0;
      } else if (splitted[1].equals("c")) {
        range = 1;
      } else if (splitted[1].equals("w")) {
        range = 2;
      } 
      int tfrom = 2;
      if (range == -1) {
        range = 2;
        tfrom = 1;
      } 
      int type = getNoticeType(splitted[tfrom]);
      if (type == -1) {
        type = 0;
        joinmod = 0;
      } 
      StringBuilder sb = new StringBuilder();
      if (splitted[tfrom].equals("nv")) {
        sb.append("[Notice]");
      } else {
        sb.append("");
      } 
      joinmod += tfrom;
      sb.append(StringUtil.joinStringFrom(splitted, joinmod));
      World.Broadcast.broadcastMessage(CWvsContext.serverNotice(type, "", sb.toString()));
      World.Broadcast.broadcastMessage(CField.UIPacket.detailShowInfo(sb.toString(), false));
      return 1;
    }
  }
  
  public static class WhatsMyIP extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(5, "IP: " + c.getSession().remoteAddress().toString().split(":")[0]);
      return 1;
    }
  }
  
  public static class 기간밴아이피 extends InternCommand.기간밴 {}
  
  public static class 밴아이피 extends InternCommand.밴 {}
  
  public static class TDrops extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().toggleDrops();
      return 1;
    }
  }
  
  public static class rb extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().doReborn();
      return 1;
    }
  }
  
  public static class 리엑터디버그 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (MapleMapObject reactor1l : c.getPlayer().getMap().getAllReactorsThreadsafe()) {
        MapleReactor reactor2l = (MapleReactor)reactor1l;
        c.getPlayer().dropMessage(5, "Reactor: oID: " + reactor2l.getObjectId() + " reactorID: " + reactor2l.getReactorId() + " Position: " + reactor2l.getPosition().toString() + " State: " + reactor2l.getState() + " Name: " + reactor2l.getName());
      } 
      return 0;
    }
  }
  
  public static class 무적 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (c.getPlayer().getBuffedValue(1221054)) {
        c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.IndieNotDamaged, 1221054);
      } else {
        SkillFactory.getSkill(1221054).getEffect(1).applyTo(c.getPlayer(), 0);
      } 
      return 0;
    }
  }
 
  
  public static class 테스트버프수정 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int getBuff = Integer.parseInt(splitted[1]);
      c.getPlayer().setAttackerSkill(getBuff);
      return 0;
    }
  }
  
  public static class 랜덤포탈 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleRandomPortal portal = new MapleRandomPortal(Integer.parseInt(splitted[1]), c.getPlayer().getTruePosition(), c.getPlayer().getMapId(), c.getPlayer().getId(), Randomizer.nextBoolean());
      c.getPlayer().getMap().spawnRandomPortal(portal);
      return 0;
    }
  }
  
  public static class 채팅금지 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      String name = splitted[1];
      MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
      if (chr != null)
        chr.canTalk(!chr.getCanTalk()); 
      c.getPlayer().dropMessage(6, "대상 채팅 " + (chr.getCanTalk() ? "금지" : "해제") + " 완료.");
      chr.dropMessage(1, c.getPlayer().getName() + "에 의해 채팅금지 상태가 되었습니다.");
      return 0;
    }
  }
  
  public static class 리셋 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      Iterator<ChannelServer> channels = ChannelServer.getAllInstances().iterator();
      MapleSaveHandler.reset(channels);
      return 1;
    }
  }
  
  public static class 레러무적 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().setNoDeadRush(!c.getPlayer().isNoDeadRush());
      c.getPlayer().dropMessageGM(6, "레인보우 러쉬 무적이 " + (c.getPlayer().isNoDeadRush() ? "적용" : "해제") + "되었습니다.");
      return 1;
    }
  }
  
  public static class 레러 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().runRainBowRush();
      return 1;
    }
  }
}
