package handling.channel.handler;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleCoolDownValueHolder;
import client.MapleStat;
import client.SecondaryStat;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import constants.GameConstants;
import handling.channel.ChannelServer;
import handling.world.World;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import scripting.NPCConversationManager;
import scripting.NPCScriptManager;
import scripting.ReactorScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.Timer;
import server.events.MapleTyoonKitchen;
import server.games.BattleGroundGameHandler;
import server.games.BattleReverse;
import server.games.BigWisp;
import server.games.BloomingRace;
import server.games.JumpingUnicon;
import server.games.MonsterPyramid;
import server.games.MultiYutGame;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MobSkillFactory;
import server.life.Spawns;
import server.maps.MapleDoor;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import server.maps.MapleReactor;
import server.maps.MapleRune;
import server.maps.MechDoor;
import server.marriage.MarriageDataEntry;
import server.marriage.MarriageEventAgent;
import server.marriage.MarriageManager;
import server.marriage.MarriageTicketType;
import server.polofritto.MapleRandomPortal;
import tools.FileoutputUtil;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PacketHelper;
import tools.packet.SLFCGPacket;

public class PlayersHandler {
  private static MapleMonster pvpMob;
  
  public static void Note(LittleEndianAccessor slea, MapleCharacter chr) {
    String name, msg;
    boolean fame;
    short num;
    int i;
    byte type = slea.readByte();
    switch (type) {
      case 0:
        System.out.printf("0", new Object[0]);
        name = slea.readMapleAsciiString();
        msg = slea.readMapleAsciiString();
        System.out.printf("1", new Object[0]);
        fame = (slea.readByte() > 0);
        try {
          System.out.printf("2", new Object[0]);
        } catch (Exception e) {
          e.printStackTrace();
        } 
        return;
      case 1:
        num = slea.readShort();
        if (num < 0)
          num = Short.MAX_VALUE; 
        slea.skip(1);
        for (i = 0; i < num; i++) {
          int id = slea.readInt();
          chr.deleteNote(id, (slea.readByte() > 0) ? 1 : 0);
        } 
        return;
    } 
    System.out.println("Unhandled note action, " + type + "");
  }
  
  public static void GiveFame(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
    int who = slea.readInt();
    int mode = slea.readByte();
    int famechange = (mode == 0) ? -1 : 1;
    MapleCharacter target = chr.getMap().getCharacterById(who);
    if (target == null || target == chr) {
      c.getSession().writeAndFlush(CWvsContext.OnFameResult(1, target.getName(), false, target.getFame()));
      return;
    } 
    if (chr.getLevel() < 15) {
      c.getSession().writeAndFlush(CWvsContext.OnFameResult(2, target.getName(), false, target.getFame()));
      return;
    } 
    switch (chr.canGiveFame(target)) {
      case OK:
        if (Math.abs(target.getFame() + famechange) <= 99999) {
          target.addFame(famechange);
          target.updateSingleStat(MapleStat.FAME, target.getFame());
        } 
        if (!chr.isGM())
          chr.hasGivenFame(target); 
        c.getSession().writeAndFlush(CWvsContext.OnFameResult(0, target.getName(), (famechange == 1), target.getFame()));
        target.getClient().getSession().writeAndFlush(CWvsContext.OnFameResult(5, chr.getName(), (famechange == 1), 0));
        break;
      case NOT_TODAY:
        c.getSession().writeAndFlush(CWvsContext.OnFameResult(3, target.getName(), false, target.getFame()));
        break;
      case NOT_THIS_MONTH:
        c.getSession().writeAndFlush(CWvsContext.OnFameResult(4, target.getName(), false, target.getFame()));
        break;
    } 
  }
  
  public static void UseDoor(LittleEndianAccessor slea, MapleCharacter chr) {
    int oid = slea.readInt();
    boolean mode = (slea.readByte() == 0);
    for (MapleMapObject obj : chr.getMap().getAllDoorsThreadsafe()) {
      MapleDoor door = (MapleDoor)obj;
      if (door.getOwnerId() == oid) {
        door.warp(chr, mode);
        break;
      } 
    } 
  }
  
  public static void UseRandomDoor(LittleEndianAccessor slea, MapleCharacter chr) {
    MapleRandomPortal portal = (MapleRandomPortal)chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.RANDOM_PORTAL);
    if (portal != null && 
      portal.getCharId() == chr.getId() && chr.getMapId() == portal.getMapId())
      if (portal.getPortalType() == 2) {
        NPCScriptManager.getInstance().start(chr.getClient(), portal.ispolo() ? 9001059 : 9001060, portal.ispolo() ? "poloEnter" : "FrittoEnter");
      } else if (portal.getPortalType() == 3) {
        NPCScriptManager.getInstance().start(chr.getClient(), 9001059, "FireWolfEnter");
      }  
  }
  
  public static void UseMechDoor(LittleEndianAccessor slea, MapleCharacter chr) {
    int oid = slea.readInt();
    Point pos = slea.readPos();
    int mode = slea.readByte();
    chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
    for (MapleMapObject obj : chr.getMap().getAllMechDoorsThreadsafe()) {
      MechDoor door = (MechDoor)obj;
      if (door.getOwnerId() == oid && door.getId() == mode) {
        chr.checkFollow();
        chr.getMap().movePlayer(chr, pos);
        chr.getMap().broadcastMessage(CField.getMechDoorCoolDown(door));
        break;
      } 
    } 
  }
  
  public static void FollowRequest(LittleEndianAccessor slea, MapleClient c) {
    MapleCharacter tt = c.getPlayer().getMap().getCharacterById(slea.readInt());
    if (slea.readByte() > 0) {
      tt = c.getPlayer().getMap().getCharacterById(c.getPlayer().getFollowId());
      if (tt != null && tt.getFollowId() == c.getPlayer().getId()) {
        tt.setFollowOn(true);
        c.getPlayer().setFollowOn(true);
      } else {
        c.getPlayer().checkFollow();
      } 
      return;
    } 
    if (slea.readByte() > 0) {
      tt = c.getPlayer().getMap().getCharacterById(c.getPlayer().getFollowId());
      if (tt != null && tt.getFollowId() == c.getPlayer().getId() && c.getPlayer().isFollowOn())
        c.getPlayer().checkFollow(); 
      return;
    } 
    tt.setFollowId(c.getPlayer().getId());
    tt.setFollowOn(false);
    tt.setFollowInitiator(false);
    c.getPlayer().setFollowOn(false);
    c.getPlayer().setFollowInitiator(false);
    tt.getClient().getSession().writeAndFlush(CWvsContext.followRequest(c.getPlayer().getId()));
  }
  
  public static void FollowReply(LittleEndianAccessor slea, MapleClient c) {
    if (c.getPlayer().getFollowId() > 0 && c.getPlayer().getFollowId() == slea.readInt()) {
      MapleCharacter tt = c.getPlayer().getMap().getCharacterById(c.getPlayer().getFollowId());
      if (tt != null && tt.getTruePosition().distanceSq(c.getPlayer().getTruePosition()) < 10000.0D && tt.getFollowId() == 0 && tt.getId() != c.getPlayer().getId()) {
        boolean accepted = (slea.readByte() > 0);
        if (accepted) {
          tt.setFollowId(c.getPlayer().getId());
          tt.setFollowOn(true);
          tt.setFollowInitiator(false);
          c.getPlayer().setFollowOn(true);
          c.getPlayer().setFollowInitiator(true);
          c.getPlayer().getMap().broadcastMessage(CField.followEffect(tt.getId(), c.getPlayer().getId(), null));
        } else {
          c.getPlayer().setFollowId(0);
          tt.setFollowId(0);
          tt.getClient().getSession().writeAndFlush(CField.getFollowMsg(5));
        } 
      } else {
        if (tt != null) {
          tt.setFollowId(0);
          c.getPlayer().setFollowId(0);
        } 
        c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "너무 멀리 있습니다."));
      } 
    } else {
      c.getPlayer().setFollowId(0);
    } 
  }
  
  public static void HitReactor(LittleEndianAccessor slea, MapleClient c) {
    int oid = slea.readInt();
    int charPos = slea.readInt();
    short stance = slea.readShort();
    MapleReactor reactor = c.getPlayer().getMap().getReactorByOid(oid);
    if (reactor == null || !reactor.isAlive())
      return; 
    if (reactor.getReactorId() < 100000 || reactor.getReactorId() > 102000)
      reactor.hitReactor(charPos, stance, c); 
    if (c.getPlayer().getMapId() == 109090300) {
      int rand = Randomizer.rand(1, 10), itemid = 0;
      if (rand <= 2) {
        itemid = 2022163;
      } else if (rand > 2 && rand <= 4) {
        itemid = 2022165;
      } else if (rand > 4 && rand <= 6) {
        itemid = 2022166;
      } 
      c.getPlayer().getMap().destroyReactor(oid);
      Item idrop = new Item(itemid, (short)0, (short)1, 0);
      c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), idrop, reactor.getPosition(), true, true);
    } 
  }
  
  public static void TouchReactor(LittleEndianAccessor slea, MapleClient c) {
    int oid = slea.readInt();
    if (c.getPlayer().getNettPyramid() != null) {
      c.getPlayer().getNettPyramid().minusLife(slea.readInt());
      return;
    } 
    boolean touched = (slea.available() == 0L || slea.readByte() > 0);
    MapleReactor reactor = c.getPlayer().getMap().getReactorByOid(oid);
    if (!touched || reactor == null || !reactor.isAlive() || reactor.getTouch() == 0)
      return; 
    if ((reactor.getMap().getId() >= 105200310 && reactor.getMap().getId() <= 105200317) || (reactor.getMap().getId() >= 105200710 && reactor.getMap().getId() <= 105200719)) {
      String text = "";
      boolean send = false;
      int type = reactor.getMap().getCustomValue0(105200310);
      switch (type) {
        case 0:
          text = "어머, 귀여운 손님들이 찾아왔네.";
          send = true;
          break;
        case 1:
          text = "무엄하다! 감히 대전을 함부로 드나들다니!";
          send = true;
          break;
        case 2:
          text = "킥킥, 여기가 죽을 자리인 줄도 모르고 왔구나.";
          send = true;
          break;
        case 3:
          text = "흑흑, 당신의 죽음을 미리 슬퍼해드리지요.";
          send = true;
          break;
      } 
      if (send) {
        reactor.getMap().broadcastMessage(CField.removeMapEffect());
        reactor.getMap().broadcastMessage(CField.startMapEffect(text, 5120099 + type, true));
      } 
      reactor.getMap().setCustomInfo(105200310, type + 1, 0);
      if (reactor.getMap().getCustomValue0(105200310) >= 5)
        reactor.getMap().removeCustomInfo(105200310); 
    } 
    if (reactor.getTouch() == 2) {
      ReactorScriptManager.getInstance().act(c, reactor);
    } else if (reactor.getTouch() == 1 && !reactor.isTimerActive()) {
      if (reactor.getReactorType() == 100) {
        int itemid = GameConstants.getCustomReactItem(reactor.getReactorId(), ((Integer)reactor.getReactItem().getLeft()).intValue());
        if (c.getPlayer().haveItem(itemid, ((Integer)reactor.getReactItem().getRight()).intValue())) {
          if (reactor.getArea().contains(c.getPlayer().getTruePosition())) {
            MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemid), itemid, ((Integer)reactor.getReactItem().getRight()).intValue(), true, false);
            reactor.hitReactor(c);
          } else {
            c.getPlayer().dropMessage(5, "너무 멀리 있습니다.");
          } 
        } else {
          c.getPlayer().dropMessage(5, "You don't have the item required.");
        } 
      } else {
        reactor.hitReactor(c);
      } 
    } 
  }
  
  public static void SpaceReactor(LittleEndianAccessor slea, MapleClient c) {
    int oid = slea.readInt();
    boolean touched = (slea.available() == 0L || slea.readByte() > 0);
    MapleReactor reactor = c.getPlayer().getMap().getReactorByOid(oid);
    if (reactor == null)
      return; 
    ReactorScriptManager.getInstance().act(c, reactor);
  }
  
  public static void DoRing(MapleClient c, String name, int itemid) {
    int newItemId = (itemid == 2240000) ? 1112803 : ((itemid == 2240001) ? 1112806 : ((itemid == 2240002) ? 1112807 : ((itemid == 2240003) ? 1112809 : (1112300 + itemid - 2240004))));
    MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
    int errcode = 0;
    if (c.getPlayer().getMarriageId() > 0) {
      errcode = 27;
    } else if (chr == null) {
      errcode = 22;
    } else {
      if (chr.getMapId() != c.getPlayer().getMapId()) {
        c.getPlayer().dropMessage(1, "상대방이 같은 맵에 없습니다.");
        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        return;
      } 
      if (!c.getPlayer().haveItem(itemid, 1) || itemid < 2240000 || itemid > 2240015) {
        errcode = 15;
      } else if (chr.getMarriageId() > 0 || chr.getMarriageItemId() > 0) {
        errcode = 28;
      } else if (!MapleInventoryManipulator.checkSpace(c, newItemId, 1, "")) {
        errcode = 24;
      } else if (!MapleInventoryManipulator.checkSpace(chr.getClient(), newItemId, 1, "")) {
        errcode = 25;
      } 
    } 
    if (errcode > 0) {
      c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)errcode, 0, null, null));
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      return;
    } 
    c.getPlayer().setMarriageItemId(itemid);
    chr.getClient().getSession().writeAndFlush(CWvsContext.sendEngagementRequest(c.getPlayer().getName(), c.getPlayer().getId()));
  }
  
  public static void RingAction(LittleEndianAccessor slea, MapleClient c) {
    byte mode = slea.readByte();
    if (mode == 0) {
      DoRing(c, slea.readMapleAsciiString(), slea.readInt());
    } else if (mode == 1) {
      c.getPlayer().setMarriageItemId(0);
    } else if (mode == 2) {
      boolean accepted = (slea.readByte() > 0);
      String name = slea.readMapleAsciiString();
      int id = slea.readInt();
      MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
      if (c.getPlayer().getMarriageId() > 0 || chr == null || chr.getId() != id || chr.getMarriageItemId() <= 0 || !chr.haveItem(chr.getMarriageItemId(), 1) || chr.getMarriageId() > 0 || !chr.isAlive() || chr.getEventInstance() != null || !c.getPlayer().isAlive() || c.getPlayer().getEventInstance() != null) {
        c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)33, 0, null, null));
        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        return;
      } 
      if (accepted) {
        int itemid = chr.getMarriageItemId();
        int newItemId = (itemid == 2240000) ? 1112803 : ((itemid == 2240001) ? 1112806 : ((itemid == 2240002) ? 1112807 : ((itemid == 2240003) ? 1112809 : (1112300 + itemid - 2240004))));
        if (!MapleInventoryManipulator.checkSpace(c, newItemId, 1, "") || !MapleInventoryManipulator.checkSpace(chr.getClient(), newItemId, 1, "")) {
          c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)25, 0, null, null));
          c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
          return;
        } 
        try {
          MarriageDataEntry data = MarriageManager.getInstance().makeNewMarriage(chr.getId());
          data.setStatus(1);
          data.setGroomId(chr.getId());
          data.setBrideId(c.getPlayer().getId());
          data.setBrideName(c.getPlayer().getName());
          data.setGroomName(chr.getName());
          long[] ringID = MapleRing.makeRing(newItemId, c.getPlayer(), chr);
          Equip eq = (Equip)MapleItemInformationProvider.getInstance().getEquipById(newItemId, ringID[1]);
          eq.setStr((short)300);
          eq.setDex((short)300);
          eq.setInt((short)300);
          eq.setLuk((short)300);
          eq.setWatk((short)300);
          eq.setMatk((short)300);
          MapleRing ring = MapleRing.loadFromDb(ringID[1]);
          if (ring != null)
            eq.setRing(ring); 
          MapleInventoryManipulator.addbyItem(c, eq);
          ring = MapleRing.loadFromDb(ringID[0]);
          if (ring != null)
            eq.setRing(ring); 
          MapleInventoryManipulator.addbyItem(chr.getClient(), eq);
          MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.USE, chr.getMarriageItemId(), 1, false, false);
          c.getPlayer().setMarriageId(data.getMarriageId());
          chr.setMarriageId(data.getMarriageId());
          chr.getClient().getSession().writeAndFlush(CWvsContext.sendEngagement((byte)13, newItemId, chr, c.getPlayer()));
          c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)13, newItemId, c.getPlayer(), chr));
          data.setEngagementTime(System.currentTimeMillis());
        } catch (Exception e) {
          FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e);
        } 
      } else {
        chr.getClient().getSession().writeAndFlush(CWvsContext.sendEngagement((byte)34, 0, null, null));
      } 
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      chr.setMarriageItemId(0);
    } else if (mode == 3) {
      int itemId = slea.readInt();
      MapleInventoryType type = GameConstants.getInventoryType(itemId);
      Item item = c.getPlayer().getInventory(type).findById(itemId);
      if (item != null && type == MapleInventoryType.ETC && itemId / 10000 == 421)
        MapleInventoryManipulator.drop(c, type, item.getPosition(), item.getQuantity()); 
    } else if (mode == 5) {
      String receiver = slea.readMapleAsciiString();
      int marriageId = slea.readInt();
      int slot = slea.readInt();
      MarriageDataEntry data = MarriageManager.getInstance().getMarriage(c.getPlayer().getMarriageId());
      if (data != null && 
        data.getMarriageId() == marriageId) {
        Item invatation = c.getPlayer().getInventory(MapleInventoryType.ETC).getItem((short)slot);
        if (invatation != null && invatation.getItemId() == data.getTicketType().getInvitationItemId()) {
          int channel = World.Find.findChannel(receiver);
          MapleCharacter chr = null;
          if (channel >= 0)
            chr = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(receiver); 
          if (chr != null) {
            if (data.getReservedPeopleList().contains(Integer.valueOf(chr.getId()))) {
              c.getPlayer().dropMessage(1, "대상은 이미 결혼식에 초대되었습니다.");
              c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
              return;
            } 
            MarriageTicketType type = data.getTicketType();
            if (MapleInventoryManipulator.checkSpace(chr.getClient(), type.getInvitedItemId(), 1, "")) {
              MapleCharacterUtil.sendNote(receiver, c.getPlayer().getName(), "Congratulations! 당신은 결혼식에 초대되었습니다! 기타창을 확인해주세요.", 0, 6, 0);
              Item item = new Item(type.getInvitedItemId(), (short)0, (short)1, 0);
              item.setMarriageId(data.getMarriageId());
              MapleInventoryManipulator.addbyItem(chr.getClient(), item);
              c.getPlayer().dropMessage(1, "청첩장을 보냈습니다.");
              c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
              data.getReservedPeopleList().add(Integer.valueOf(chr.getId()));
              MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, invatation.getItemId(), 1, true, false);
            } else {
              c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)25, 0, null, null));
              c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
            } 
          } else {
            c.getPlayer().dropMessage(1, "초대 받을 하객이 접속중이 아닙니다.");
            c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
          } 
        } 
      } 
    } else if (mode == 6) {
      int slot = slea.readInt();
      int itemid = slea.readInt();
      Item item = c.getPlayer().getInventory(MapleInventoryType.ETC).getItem((short)slot);
      if (item != null && item.getItemId() == itemid && item.getMarriageId() > 0) {
        MarriageDataEntry data = MarriageManager.getInstance().getMarriage(item.getMarriageId());
        if (data != null) {
          c.getSession().writeAndFlush(CWvsContext.showWeddingInvitation(data.getGroomName(), data.getBrideName(), data.getTicketType().getItemId() - 5251004));
          return;
        } 
      } 
      c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)34, 0, null, null));
    } else if (mode == 9) {
      int wishes = slea.readByte();
      MarriageDataEntry data = MarriageManager.getInstance().getMarriage(c.getPlayer().getMarriageId());
      if (data != null && 
        data.getStatus() == 1 && data.getWeddingStatus() >= 1 && data.getWeddingStatus() < 8) {
        if (data.getGroomId() == c.getPlayer().getId()) {
          if ((data.getStatus() & 0x2) > 0)
            return; 
          data.getGroomWishList().clear();
          for (int i = 0; i < wishes; i++)
            data.getGroomWishList().add(slea.readMapleAsciiString()); 
        } else if (data.getBrideId() == c.getPlayer().getId()) {
          if ((data.getStatus() & 0x4) > 0)
            return; 
          data.getBrideWishList().clear();
          for (int i = 0; i < wishes; i++)
            data.getBrideWishList().add(slea.readMapleAsciiString()); 
        } 
        if (data.getGroomId() == c.getPlayer().getId()) {
          MarriageTicketType type = data.getTicketType();
          if (MapleInventoryManipulator.checkSpace(c, type.getInvitationItemId(), type.getInvitationQuantity(), "")) {
            data.setWeddingStatus(data.getWeddingStatus() | 0x2);
            if (data.getWeddingStatus() < 7)
              c.getPlayer().dropMessage(1, "위시리스트를 등록했습니다. 여성분이 위시리스트 등록을 끝낼 때 까지 잠시 기다려주세요."); 
          } else {
            c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)24, 0, null, null));
            data.getGroomWishList().clear();
          } 
        } else if (data.getBrideId() == c.getPlayer().getId()) {
          MarriageTicketType type = data.getTicketType();
          if (MapleInventoryManipulator.checkSpace(c, type.getInvitationItemId(), type.getInvitationQuantity(), "")) {
            data.setWeddingStatus(data.getWeddingStatus() | 0x4);
            if (data.getWeddingStatus() < 7)
              c.getPlayer().dropMessage(1, "위시리스트를 등록했습니다. 남성분이 위시리스트 등록을 끝낼 때 까지 잠시 기다려주세요."); 
          } else {
            c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)24, 0, null, null));
            data.getBrideWishList().clear();
          } 
        } 
        if (data.getWeddingStatus() == 7) {
          MarriageTicketType type = data.getTicketType();
          c.getSession().writeAndFlush(CWvsContext.sendEngagement((byte)18, 0, null, null));
          MapleInventoryManipulator.addById(c, type.getInvitationItemId(), (short)type.getInvitationQuantity(), "");
          int channel = World.Find.findChannel(data.getPartnerId(c.getPlayer().getId()));
          if (channel >= 0) {
            MapleCharacter chr = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterById(data.getPartnerId(c.getPlayer().getId()));
            if (chr != null) {
              MapleInventoryManipulator.addById(chr.getClient(), type.getInvitationItemId(), (short)type.getInvitationQuantity(), "");
              chr.getClient().getSession().writeAndFlush(CWvsContext.sendEngagement((byte)18, 0, null, null));
            } 
          } 
          data.setMakeReservationTime(System.currentTimeMillis());
        } 
      } 
    } 
  }
  
  public static void Report(LittleEndianAccessor slea, MapleClient c) {
    byte type = slea.readByte();
    String name = slea.readMapleAsciiString();
    String desc = slea.readMapleAsciiString();
    MapleCharacter target = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
    if (target != null) {
      if (System.currentTimeMillis() - (c.getPlayer()).lastReportTime >= 600000L) {
        c.getSession().writeAndFlush(CWvsContext.report(2));
        String data = c.getPlayer().getName() + " 캐릭터가 " + target.getName() + " 캐릭터를 " + target.getMapId() + " 맵에서 신고 | 타입 : " + type + " / 내용 : " + desc + "\r\n\r\n";
        NPCConversationManager.writeLog("Log/Report.log", data, true);
      } else {
        c.getPlayer().dropMessage(1, "10분마다 신고하실 수 있습니다.");
      } 
    } else {
      c.getPlayer().dropMessage(1, "대상을 찾을 수 없습니다.");
    } 
  }
  
  public static final void StealSkill(LittleEndianAccessor slea, MapleClient c) {
    if (c.getPlayer() == null || c.getPlayer().getMap() == null || !GameConstants.isPhantom(c.getPlayer().getJob())) {
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      return;
    } 
    int skill = slea.readInt();
    int cid = slea.readInt();
    byte action = slea.readByte();
    if (action == 0) {
      MapleCharacter other = c.getPlayer().getMap().getCharacterById(cid);
      if (other != null && other.getId() != c.getPlayer().getId() && other.getTotalSkillLevel(skill) > 0) {
        c.getPlayer().addStolenSkill(skill, other.getTotalSkillLevel(skill));
      } else {
        c.getPlayer().dropMessage(1, "상대방이 해당 스킬을 올리지 않았습니다.");
        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      } 
    } else if (action == 1) {
      c.getPlayer().removeStolenSkill(skill);
    } 
  }
  
  public static final void ChooseSkill(LittleEndianAccessor slea, MapleClient c) {
    if (c.getPlayer() == null || c.getPlayer().getMap() == null || !GameConstants.isPhantom(c.getPlayer().getJob())) {
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      return;
    } 
    int base = slea.readInt();
    int skill = slea.readInt();
    if (skill <= 0) {
      c.getPlayer().unchooseStolenSkill(base);
    } else {
      c.getPlayer().chooseStolenSkill(skill);
    } 
  }
  
  public static final void viewSkills(LittleEndianAccessor slea, MapleClient c) {
    int victim = slea.readInt();
    int jobid = c.getChannelServer().getPlayerStorage().getCharacterById(victim).getJob();
    if (!c.getChannelServer().getPlayerStorage().getCharacterById(victim).getSkills().isEmpty() && GameConstants.isAdventurer(jobid)) {
      c.getSession().writeAndFlush(CField.viewSkills(c.getChannelServer().getPlayerStorage().getCharacterById(victim)));
    } else {
      c.getPlayer().dropMessage(6, "훔칠 수 있는 스킬이 없습니다.");
    } 
  }
  
  public static boolean inArea(MapleCharacter chr) {
    for (Rectangle rect : chr.getMap().getAreas()) {
      if (rect.contains(chr.getTruePosition()))
        return true; 
    } 
    for (MapleMist mist : chr.getMap().getAllMistsThreadsafe()) {
      if (mist.getOwnerId() == chr.getId() && mist.isPoisonMist() == 2 && mist.getBox().contains(chr.getTruePosition()))
        return true; 
    } 
    return false;
  }
  
  public static final void TouchRune(LittleEndianAccessor slea, MapleCharacter chr) {
    slea.skip(4);
    int type = slea.readInt();
    MapleRune rune = chr.getMap().getRune();
    long time = System.currentTimeMillis();
    for (MapleCoolDownValueHolder m : chr.getCooldowns()) {
      if (m.skillId == 80002282 && !chr.isGM()) {
        chr.getClient().getSession().writeAndFlush(CField.RuneAction(2, (int)(m.length + m.startTime - System.currentTimeMillis())));
        return;
      } 
    } 
    if (GameConstants.isYeti(chr.getJob()) || GameConstants.isPinkBean(chr.getJob())) {
      SecondaryStatEffect a;
      MapleMist mist;
      MapleMonster mob1;
      chr.setTouchedRune(type);
      if (chr.getBuffedValue(80002282) && !chr.isGM()) {
        chr.dropMessage(1, "룬 재사용 대기시간 중에는 룬을 사용하실 수 없습니다.");
        chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
        return;
      } 
      if (chr.getClient().getKeyValue("rune") == null) {
        chr.getClient().setKeyValue("rune", "1");
        chr.getClient().send(CField.ImageTalkNpc(9010049, 10000, "#b[안내] 룬#k\r\n\r\n신비의 돌 #b[룬]#k을 사용하셨군요!\r\n\r\n#b룬#k을 사용하면 일정 시간 동안 #b100% 추가 경험치#k와 룬의 종류에 따른 #b특수 효과#k를 얻을 수 있어요!"));
      } 
      if (chr.getQuestStatus(501227) == 1)
        if (chr.getKeyValue(501227, "RunAct") < 7L) {
          if (chr.getKeyValue(501227, "RunAct") < 0L)
            chr.setKeyValue(501227, "RunAct", "0"); 
          chr.setKeyValue(501227, "RunAct", "" + (chr.getKeyValue(501227, "RunAct") + 1L));
          if (chr.getKeyValue(501227, "RunAct") >= 7L && 
            chr.getKeyValue(501229, "state") != 2L)
            chr.setKeyValue(501229, "state", "2"); 
        } else if (chr.getKeyValue(501229, "state") != 2L) {
          chr.setKeyValue(501229, "state", "2");
        }  
      SecondaryStatEffect effect = SkillFactory.getSkill(80002280).getEffect(1);
      effect.applyTo(chr, false);
      boolean spawn = false;
      switch (chr.getTouchedRune()) {
        case 0:
          effect = SkillFactory.getSkill(80001427).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 1:
          effect = SkillFactory.getSkill(80001428).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 2:
          effect = SkillFactory.getSkill(80001432).getEffect(1);
          effect.applyTo(chr, false);
          a = SkillFactory.getSkill(80001431).getEffect(1);
          mist = new MapleMist(a.calculateBoundingBox(chr.getPosition(), chr.isFacingLeft()), chr, a, 30000, (byte)(chr.isFacingLeft() ? 1 : 0));
          mist.setPosition(chr.getPosition());
          chr.getMap().spawnMist(mist, false);
          break;
        case 3:
          effect = SkillFactory.getSkill(80001762).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 4:
          effect = SkillFactory.getSkill(80001757).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 5:
          for (MapleMonster mob : chr.getMap().getAllMonster()) {
            if (mob.isElitemonster() || mob.isEliteboss()) {
              spawn = true;
              break;
            } 
          } 
          if (!spawn) {
            List<MapleMapObject> monstersInRange = chr.getMap().getMapObjectsInRange(chr.getPosition(), 2.0E7D, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
            if (!monstersInRange.isEmpty()) {
              int random = Randomizer.rand(0, monstersInRange.size() - 1);
              int mobid = ((MapleMapObject)monstersInRange.get(random)).getObjectId();
              MapleMonster monster = chr.getMap().getMonsterByOid(mobid);
              if (monster == null) {
                chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
                return;
              } 
              if (monster != null && !monster.getStats().isBoss())
                for (int i = 0; i < 3; i++)
                  chr.getMap().makeEliteMonster(monster.getId(), Randomizer.rand(0, 2), ((Spawns)(chr.getMap()).monsterSpawn.get(Randomizer.rand(0, (chr.getMap()).monsterSpawn.size() - 1))).getPosition(), true, false);  
            } 
          } 
          effect = SkillFactory.getSkill(80001754).getEffect(1);
          effect.applyTo(chr);
          break;
        case 6:
          mob1 = MapleLifeFactory.getMonster(8220028);
          mob1.setCustomInfo(8220028, 10, 0);
          chr.getMap().spawnMonsterOnGroundBelow(mob1, new Point((chr.getPosition()).x, (chr.getPosition()).y));
          effect = SkillFactory.getSkill(80001755).getEffect(1);
          effect.applyTo(chr);
          break;
        case 7:
          effect = SkillFactory.getSkill(80001878).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 8:
          effect = SkillFactory.getSkill(80002888).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 9:
          effect = SkillFactory.getSkill(80002889).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 10:
          effect = SkillFactory.getSkill(80002890).getEffect(1);
          effect.applyTo(chr, false);
          break;
      } 
      chr.getMap().broadcastMessage(CField.showRuneEffect(chr.getTouchedRune()));
      chr.getMap().broadcastMessage(CField.removeRune(rune, chr, spawn ? 1 : 0));
      chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
      if (chr.getMap().getRuneCurse() > 0) {
        chr.getMap().setRuneCurse(0);
        chr.getMap().broadcastMessage(CField.runeCurse("엘리트 보스의 저주가 해제되었습니다!!", true));
      } 
      chr.getMap().setRune(null);
      chr.getMap().removeMapObject(rune);
      effect = SkillFactory.getSkill(80002282).getEffect(1);
      effect.applyTo(chr, false);
      chr.addCooldown(80002282, System.currentTimeMillis(), 900000L);
      chr.checkSpecialCoreSkills("rune", 0, effect);
      //chr.checkLiveQuest(3, false);
      if (chr.getKeyValue(51351, "startquestid") == 49010L) {
        chr.setKeyValue(51351, "queststat", "3");
        chr.getClient().send(CWvsContext.updateSuddenQuest((int)chr.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + chr.getKeyValue(51351, "startquestid") + ";state=3;"));
        chr.getClient().send(CWvsContext.updateSuddenQuest((int)chr.getKeyValue(51351, "startquestid"), false, chr.getKeyValue(51351, "endtime"), "RunAct=1;"));
      } 
    } else if (rune != null && rune.getRuneType() == type) {
      if (!chr.getBuffedValue(80002282) || chr.isGM()) {
        chr.setTouchedRune(type);
        chr.getClient().getSession().writeAndFlush(CField.RuneAction(9, (int)time));
      } else {
        for (MapleCoolDownValueHolder m : chr.getCooldowns()) {
          if (m.skillId == 80002282) {
            chr.getClient().getSession().writeAndFlush(CField.RuneAction(2, (int)(m.length + m.startTime - System.currentTimeMillis())));
            break;
          } 
        } 
      } 
    } 
    chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
  }
  
  public static final void UseRune(LittleEndianAccessor slea, MapleCharacter chr) {
    if (slea.available() < 19L)
      return; 
    slea.skip(9);
    slea.skip(8);
    int result = slea.readInt();
    MapleRune rune = chr.getMap().getRune();
    if (chr.getBuffedValue(80002282) && !chr.isGM()) {
      chr.dropMessage(1, "룬 재사용 대기시간 중에는 룬을 사용하실 수 없습니다.");
      chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
      return;
    } 
    if (result == 1) {
      SecondaryStatEffect a;
      MapleMist mist;
      MapleMonster mob1;
      if (chr.getClient().getKeyValue("rune") == null) {
        chr.getClient().setKeyValue("rune", "1");
        chr.getClient().send(CField.ImageTalkNpc(9010049, 10000, "#b[안내] 룬#k\r\n\r\n신비의 돌 #b[룬]#k을 사용하셨군요!\r\n\r\n#b룬#k을 사용하면 일정 시간 동안 #b100% 추가 경험치#k와 룬의 종류에 따른 #b특수 효과#k를 얻을 수 있어요!"));
      } 
      if (chr.getQuestStatus(501227) == 1)
        if (chr.getKeyValue(501227, "RunAct") < 7L) {
          if (chr.getKeyValue(501227, "RunAct") < 0L)
            chr.setKeyValue(501227, "RunAct", "0"); 
          chr.setKeyValue(501227, "RunAct", "" + (chr.getKeyValue(501227, "RunAct") + 1L));
          if (chr.getKeyValue(501227, "RunAct") >= 7L && 
            chr.getKeyValue(501229, "state") != 2L)
            chr.setKeyValue(501229, "state", "2"); 
        } else if (chr.getKeyValue(501229, "state") != 2L) {
          chr.setKeyValue(501229, "state", "2");
        }  
      SecondaryStatEffect effect = SkillFactory.getSkill(80002280).getEffect(1);
      effect.applyTo(chr, false);
      boolean spawn = false;
      switch (chr.getTouchedRune()) {
        case 0:
          effect = SkillFactory.getSkill(80001427).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 1:
          effect = SkillFactory.getSkill(80001428).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 2:
          effect = SkillFactory.getSkill(80001432).getEffect(1);
          effect.applyTo(chr, false);
          a = SkillFactory.getSkill(80001431).getEffect(1);
          mist = new MapleMist(a.calculateBoundingBox(chr.getPosition(), chr.isFacingLeft()), chr, a, 30000, (byte)(chr.isFacingLeft() ? 1 : 0));
          mist.setPosition(chr.getPosition());
          chr.getMap().spawnMist(mist, false);
          break;
        case 3:
          effect = SkillFactory.getSkill(80001762).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 4:
          effect = SkillFactory.getSkill(80001757).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 5:
          for (MapleMonster mob : chr.getMap().getAllMonster()) {
            if (mob.isElitemonster() || mob.isEliteboss()) {
              spawn = true;
              break;
            } 
          } 
          if (!spawn) {
            List<MapleMapObject> monstersInRange = chr.getMap().getMapObjectsInRange(chr.getPosition(), 2.0E7D, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
            if (!monstersInRange.isEmpty()) {
              int random = Randomizer.rand(0, monstersInRange.size() - 1);
              int mobid = ((MapleMapObject)monstersInRange.get(random)).getObjectId();
              MapleMonster monster = chr.getMap().getMonsterByOid(mobid);
              if (monster == null) {
                chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
                return;
              } 
              if (monster != null && !monster.getStats().isBoss())
                for (int i = 0; i < 3; i++)
                  chr.getMap().makeEliteMonster(monster.getId(), Randomizer.rand(0, 2), ((Spawns)(chr.getMap()).monsterSpawn.get(Randomizer.rand(0, (chr.getMap()).monsterSpawn.size() - 1))).getPosition(), true, false);  
            } 
          } 
          effect = SkillFactory.getSkill(80001754).getEffect(1);
          effect.applyTo(chr);
          break;
        case 6:
          mob1 = MapleLifeFactory.getMonster(8220028);
          mob1.setCustomInfo(8220028, 10, 0);
          chr.getMap().spawnMonsterOnGroundBelow(mob1, new Point((chr.getPosition()).x, (chr.getPosition()).y));
          effect = SkillFactory.getSkill(80001755).getEffect(1);
          effect.applyTo(chr);
          break;
        case 7:
          effect = SkillFactory.getSkill(80001878).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 8:
          effect = SkillFactory.getSkill(80002888).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 9:
          effect = SkillFactory.getSkill(80002889).getEffect(1);
          effect.applyTo(chr, false);
          break;
        case 10:
          effect = SkillFactory.getSkill(80002890).getEffect(1);
          effect.applyTo(chr, false);
          break;
      } 
      chr.getMap().broadcastMessage(CField.showRuneEffect(chr.getTouchedRune()));
      chr.getMap().broadcastMessage(CField.removeRune(rune, chr, spawn ? 1 : 0));
      chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
      if (chr.getMap().getRuneCurse() > 0) {
        chr.getMap().setRuneCurse(0);
        chr.getMap().broadcastMessage(CField.runeCurse("엘리트 보스의 저주가 해제되었습니다!!", true));
      } 
      chr.getMap().setRune(null);
      chr.getMap().removeMapObject(rune);
      effect = SkillFactory.getSkill(80002282).getEffect(1);
      effect.applyTo(chr, false);
      chr.addCooldown(80002282, System.currentTimeMillis(), 900000L);
      chr.checkSpecialCoreSkills("rune", 0, effect);
//      chr.checkLiveQuest(3, false);
      if (chr.getKeyValue(51351, "startquestid") == 49010L) {
        chr.setKeyValue(51351, "queststat", "3");
        chr.getClient().send(CWvsContext.updateSuddenQuest((int)chr.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + chr.getKeyValue(51351, "startquestid") + ";state=3;"));
        chr.getClient().send(CWvsContext.updateSuddenQuest((int)chr.getKeyValue(51351, "startquestid"), false, chr.getKeyValue(51351, "endtime"), "RunAct=1;"));
      } 
    } 
    chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
  }
  
  public static void WeddingPresent(LittleEndianAccessor slea, MapleClient c) {
    byte mode = slea.readByte();
    if (mode == 7) {
      byte invtype = slea.readByte();
      byte slot = slea.readByte();
      MarriageDataEntry entry = MarriageManager.getInstance().getMarriage(c.getPlayer().getMarriageId());
      if (entry != null) {
        List<Item> items = (c.getPlayer().getGender() == 0) ? entry.getGroomPresentList() : entry.getBridePresentList();
        if (null != items)
          try {
            Item item = items.get(slot);
            if (item != null && MapleInventoryType.getByType(invtype) == GameConstants.getInventoryType(item.getItemId()))
              if (MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), "")) {
                items.remove(slot);
                MapleInventoryManipulator.addbyItem(c, item);
                c.getSession().writeAndFlush(CWvsContext.showWeddingWishRecvToLocalResult(items));
              } else {
                c.getSession().writeAndFlush(CWvsContext.showWeddingWishRecvDisableHang());
                c.getPlayer().dropMessage(1, "인벤토리 공간이 부족합니 void WeddingPresent(LittleEndianAccessor slea, MapleClient c) {\n" +
"    byte mode = slea.readByte();\n" +
"    if (mode == 7) {\n" +
"      byte invtype = slea.readByte();\n" +
"      byte slot = slea.readByte();\n" +
"      MarriageDataEntry entry = MarriageManager.getInstance().getMarriag다.");
                return;
              }  
          } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {} 
      } 
    } else if (mode == 6) {
      short slot = slea.readShort();
      int itemid = slea.readInt();
      short quantity = slea.readShort();
      MapleInventoryType type = MapleInventoryType.getByType((byte)(itemid / 1000000));
      Item item = c.getPlayer().getInventory(type).getItem(slot);
      MarriageEventAgent agent = MarriageManager.getInstance().getEventAgent(c.getChannel());
      if (agent != null) {
        MarriageDataEntry dataEntry = agent.getDataEntry();
        if (dataEntry != null && 
          item != null && item.getItemId() == itemid && item.getQuantity() >= quantity) {
          Item item2 = item.copy();
          if (GameConstants.isRechargable(itemid))
            quantity = item.getQuantity(); 
          item2.setQuantity(quantity);
          MapleInventoryManipulator.removeFromSlot(c, type, slot, quantity, false);
          if (c.getPlayer().getWeddingGive() == 0) {
            dataEntry.getGroomPresentList().add(item2);
            c.getSession().writeAndFlush(CWvsContext.showWeddingWishGiveToServerResult(dataEntry.getGroomWishList(), type, dataEntry.getGroomPresentList()));
          } else {
            dataEntry.getBridePresentList().add(item2);
            c.getSession().writeAndFlush(CWvsContext.showWeddingWishGiveToServerResult(dataEntry.getBrideWishList(), type, dataEntry.getBridePresentList()));
          } 
        } 
      } 
    } 
  }
  
  public static void followCancel(LittleEndianAccessor slea, MapleClient c) {
    if (slea.readByte() == 0)
      c.getPlayer().checkFollow(); 
  }
  
  public static void auraPartyBuff(LittleEndianAccessor slea, MapleClient c) {
    slea.skip(4);
    int type = slea.readInt();
    int skillId = slea.readInt();
    int skillLv = slea.readInt();
    int ownerId = slea.readInt();
    int ownerPosX = slea.readInt();
    int ownerPosY = slea.readInt();
    SecondaryStatEffect effect = SkillFactory.getSkill(skillId).getEffect(skillLv);
    MapleCharacter owner = c.getPlayer().getMap().getCharacterById(ownerId);
    MapleCharacter chr = c.getPlayer();
    if (type == 1) {
      if (!chr.getBuffedValue(skillId) && c.getPlayer().getId() != owner.getId())
        effect.applyTo(owner, chr, false, chr.getPosition(), 0, (byte)0, false); 
    } else if (type == 2) {
      chr.cancelEffect(chr.getBuffedEffect(skillId));
    } 
  }
  
  public static void ColorCardHandler(LittleEndianAccessor slea, MapleClient c) {
    MapleCharacter chr = c.getPlayer();
    int num = slea.readInt();
    int type = slea.readInt();
    if (chr.getColorCardInstance() != null) {
      if (((Integer)chr.getColorCardInstance().getCardList().get(0)).intValue() == type || type == 3) {
        chr.getColorCardInstance().getCardList().remove(0);
        chr.getColorCardInstance().setCombo(chr.getColorCardInstance().getCombo() + 1);
        chr.getColorCardInstance().setComboCount(chr.getColorCardInstance().getComboCount() + 1);
        if (!chr.getColorCardInstance().isFeverTime()) {
          chr.getColorCardInstance().setGauge(chr.getColorCardInstance().getGauge() + 1);
          if (chr.getColorCardInstance().getGauge() > 50) {
            chr.getColorCardInstance().setGauge(0);
            chr.getColorCardInstance().setFeverTime(true);
            c.send(SLFCGPacket.ColorCardPacket.ColorCardState(4));
            Timer.EventTimer.getInstance().schedule(() -> {
                  if (chr != null) {
                    chr.getColorCardInstance().setFeverTime(false);
                    c.send(SLFCGPacket.ColorCardPacket.ColorCardState(3));
                  } 
                }, 5000L);
          } 
        } 
        switch (((Integer)chr.getColorCardInstance().getCardList().get(0)).intValue()) {
          case 0:
            chr.getColorCardInstance().setGreensuc(chr.getColorCardInstance().getGreensuc() + 1);
            break;
          case 1:
            chr.getColorCardInstance().setRedsuc(chr.getColorCardInstance().getRedsuc() + 1);
            break;
          case 2:
            chr.getColorCardInstance().setBluesuc(chr.getColorCardInstance().getBluesuc() + 1);
            break;
        } 
        int addPoint = 250;
        boolean bonus = Randomizer.isSuccess(35);
        if (bonus) {
          c.send(SLFCGPacket.ColorCardPacket.ColorCardBonus(bonus));
          addPoint *= 2;
        } 
        addPoint += chr.getColorCardInstance().getCombo() * Randomizer.rand(2, 5);
        chr.getColorCardInstance().setPoint(chr.getColorCardInstance().getPoint() + addPoint);
      } else {
        chr.getColorCardInstance().setCombo(0);
        chr.getColorCardInstance().setFailCount(chr.getColorCardInstance().getFailCount() + 1);
      } 
      c.send(SLFCGPacket.ColorCardPacket.ColorCardMain(chr.getColorCardInstance().getCombo(), chr.getColorCardInstance().getGauge(), chr.getColorCardInstance().getPoint()));
    } 
  }
  
  public static void ContentsWaiting(LittleEndianAccessor slea, MapleClient c) {
    slea.skip(4);
    int unk = slea.readByte();
    slea.readInt();
    int type = slea.readInt();
    boolean cancel = (unk == 5 || unk == 8);
    switch (type) {
      case 18:
        if (cancel) {
          if (MultiYutGame.multiYutMagchingQueue.contains(c.getPlayer()))
            c.getPlayer().CancelWating(c.getPlayer(), type); 
          break;
        } 
        c.getPlayer().EnterMultiYutGame();
        break;
      case 21:
      case 22:
        if (cancel) {
          c.send(SLFCGPacket.ContentsWaiting(c.getPlayer(), 0, new int[] { 11, 5, 1, type }));
          if (type == 21) {
            JumpingUnicon.ExitWaiting(c.getPlayer());
            break;
          } 
          BigWisp.ExitWaiting(c.getPlayer());
        } 
        break;
      case 23:
        if (cancel) {
          if (BattleReverse.BattleReverseMatchingQueue.contains(c.getPlayer()))
            c.getPlayer().CancelWating(c.getPlayer(), type); 
        } else {
          c.getPlayer().EnterBattleReverse();
        } 
      case 24:
        if (cancel) {
          if (MonsterPyramid.monsterPyramidMatchingQueue.contains(c.getPlayer()))
            c.getPlayer().CancelWating(c.getPlayer(), type); 
          break;
        } 
        c.getPlayer().EnterMonsterPyramid();
        break;
      case 25:
        if (cancel && 
          BloomingRace.getMatchinglist().contains(c.getPlayer()))
          BloomingRace.getMatchinglist().remove(c.getPlayer()); 
        break;
      case 26:
        if (cancel && 
          BattleGroundGameHandler.getMatchinglist().contains(c.getPlayer()))
          BattleGroundGameHandler.getMatchinglist().remove(c.getPlayer()); 
        break;
    } 
  }
  
  public static void MapleYutHandler(LittleEndianAccessor slea, MapleClient c) {
    int type = slea.readInt();
    if (c.getPlayer().getMultiYutInstance() != null) {
      int horseposition;
      int yuttype;
      int movecount;
      switch (type) {
        case 0:
          c.getPlayer().getMultiYutInstance().getPlayer(c.getPlayer()).ThrowYut();
          break;
        case 1:
          horseposition = slea.readInt();
          yuttype = slea.readInt();
          movecount = slea.readInt();
          c.getPlayer().getMultiYutInstance().getPlayer(c.getPlayer()).MoveHorse(horseposition, movecount, yuttype + 1);
          break;
      } 
    } 
  }
  
  public static void MapleTyoonKitchenSuc(LittleEndianAccessor slea, MapleClient c) {
    if (c.getPlayer().getMtk() == null)
      return; 
    boolean nodel = false;
    int type = slea.readInt();
    int id = slea.readInt();
    if (type == 0) {
      int itemid = 2024020 + id;
      String sound = "Sound/MiniGame.img/TyoonKitchen/";
      switch (id) {
        case 0:
          sound = sound + "get_bread";
          break;
        case 1:
          sound = sound + "get_meat";
          break;
        case 2:
          sound = sound + "get_egg";
          break;
        case 3:
          sound = sound + "get_vegetable";
          break;
        case 4:
          sound = sound + "get_fish";
          break;
        case 5:
          sound = sound + "get_fryingpan";
          break;
        case 6:
          sound = sound + "get_pot";
          break;
        case 7:
          sound = sound + "get_knife";
          break;
      } 
      c.send(CField.playSound("Sound/MiniGame.img/CrazySplash/install"));
      c.send(CField.playSound(sound));
      c.getPlayer().getMtk().setItemid(itemid);
      c.getPlayer().getMap().broadcastMessage(SLFCGPacket.TyoonKitchenPacket.Menu(c.getPlayer().getMtk().getRecipes(), MapleTyoonKitchen.getAllMtk(c.getPlayer())));
    } else if (type == 2) {
      MapleTyoonKitchen.MapleTyoonKitchenRecipe recipe = c.getPlayer().getMtk().getRecipe();
      if (recipe.getDestination() == id) {
        c.getPlayer().getMtk().setOwnmoney(c.getPlayer().getMtk().getOwnmoney() + recipe.getMoney());
        c.getPlayer().getMap().broadcastMessage(SLFCGPacket.TyoonKitchenPacket.Effect(c.getPlayer(), "Effect/OnUserEff.img/aquarisTower/success", 0));
        c.getPlayer().getMap().broadcastMessage(CField.playSound("Sound/Item.img/03015651/Appear"));
        c.getPlayer().getMap().broadcastMessage(CField.environmentMove("cookPlate" + (recipe.getMakenum() + 1), 0));
        if (MapleTyoonKitchen.getAllMoney(c.getPlayer()) < 50000)
          c.getPlayer().getMap().startMapEffect("잘했네! 어서 다음 주문을 준비하게!", 5120216, 4000); 
        c.getPlayer().getMtk().setItemid(0);
        c.getPlayer().getMtk().setType(-1);
        if (recipe.getMakeTime() != null)
          recipe.getMakeTime().cancel(true); 
        recipe.reset(false, recipe.getMakenum(), true);
        c.getPlayer().getMtk().setRecipe(null);
        if (MapleTyoonKitchen.getAllMoney(c.getPlayer()) >= 500) {
          c.getPlayer().getMap().broadcastMessage(CField.removeMapEffect());
          c.getPlayer().getMap().broadcastMessage(CField.environmentChange("Effect/EventEffect.img/2021BloomingRace/success", 16));
          c.getPlayer().getMap().startMapEffect("고생 많았네, 음식의 소중함을 깨달았기를 바라네.", 5120216, 4000);
          for (MapleCharacter chr : c.getPlayer().getMap().getAllChracater()) {
            if (chr.getMtk() != null) {
              chr.addKV("ClearTyKitchen", "1");
              MapleTyoonKitchen.ResetMtk(chr);
              Timer.EventTimer.getInstance().schedule(() -> chr.warp(993194401), 3000L);
            } 
          } 
        } 
      } else {
        nodel = true;
        c.getPlayer().giveDebuff(SecondaryStat.Stun, MobSkillFactory.getMobSkill(123, 119));
      } 
    } else {
      String cp = "cookPlate" + (id + 1);
      int number = slea.readInt();
      int unk = slea.readInt();
      if (number == 2) {
        MapleTyoonKitchen.MapleTyoonKitchenRecipe recipe = c.getPlayer().getMtk().getRecipes().get(id);
        c.getPlayer().getMtk().setItemid(recipe.getRecipe());
        c.getPlayer().getMtk().setType(id);
        c.getPlayer().getMtk().setRecipe(recipe);
        recipe.setType(3);
        nodel = true;
      } else {
        MapleTyoonKitchen.MapleTyoonKitchenRecipe recipe = c.getPlayer().getMtk().getRecipes().get(id);
        for (Triple<Integer, Integer, Integer> list : recipe.getRecipeInfo()) {
          if (((Integer)list.getRight()).intValue() == 0) {
            if (((Integer)list.getLeft()).intValue() == c.getPlayer().getMtk().getItemid()) {
                //             list.right = (G)Integer.valueOf(1);
              list.right = Integer.valueOf(1);
              boolean suc = true;
              for (Triple<Integer, Integer, Integer> lists : recipe.getRecipeInfo()) {
                if (((Integer)lists.getRight()).intValue() == 0) {
                  suc = false;
                  break;
                } 
              } 
              if (suc) {
                recipe.setType(2);
                c.getPlayer().getMap().broadcastMessage(CField.environmentMove(cp, 2));
                c.getPlayer().getMap().broadcastMessage(SLFCGPacket.TyoonKitchenPacket.Effect(c.getPlayer(), "Effect/OnUserEff.img/urus/great", 0));
                break;
              } 
              c.getPlayer().getMap().broadcastMessage(CField.environmentMove(cp, 1));
              c.getPlayer().getMap().broadcastMessage(SLFCGPacket.TyoonKitchenPacket.Effect(c.getPlayer(), "Effect/OnUserEff.img/urus/good", 0));
              break;
            } 
            String[] msg = { "오늘 장사는 접어야 할 것 같군.", "다시!", "손님들이 기다리다가 노인이 되겠군.", "빠르다고 다가 아니네!\r\n제대로 하게!", "지금 뭐 하는 건가!", "레시피를 제대로 보란 말일세!" };
            c.send(CField.enforceMsgNPC(9062552, 3000, msg[Randomizer.rand(0, msg.length - 1)]));
            c.getPlayer().giveDebuff(SecondaryStat.Stun, MobSkillFactory.getMobSkill(123, 119));
            c.getPlayer().getMap().broadcastMessage(SLFCGPacket.TyoonKitchenPacket.Effect(c.getPlayer(), "Effect/OnUserEff.img/urus/bad", 0));
            break;
          } 
        } 
      } 
      if (!nodel)
        c.getPlayer().getMtk().setItemid(0); 
      c.getPlayer().getMap().broadcastMessage(SLFCGPacket.TyoonKitchenPacket.Menu(c.getPlayer().getMtk().getRecipes(), MapleTyoonKitchen.getAllMtk(c.getPlayer())));
    } 
    if (c.getPlayer().getMtk() != null) {
      c.getPlayer().getMtk().OtherCookerSet();
      c.getPlayer().getMap().broadcastMessage(SLFCGPacket.TyoonKitchenPacket.Menu(c.getPlayer().getMtk().getRecipes(), MapleTyoonKitchen.getAllMtk(c.getPlayer())));
      c.getPlayer().getMap().broadcastMessage(SLFCGPacket.TyoonKitchenPacket.Unk());
    } 
  }
  
  public static void MapleTyoonKitchenMake(LittleEndianAccessor slea, MapleClient c) {
    int type = slea.readByte();
    if (type == 0) {
      c.getPlayer().getMap().broadcastMessage(SLFCGPacket.ShowActionEffect(c.getPlayer(), 0, 0, 0));
    } else {
      int type2 = slea.readInt();
      slea.skip(4);
      int motion = slea.readInt();
      int time = slea.readInt();
      c.getPlayer().getMap().broadcastMessage(SLFCGPacket.ShowActionEffect(c.getPlayer(), 1, motion, time));
    } 
  }
}
