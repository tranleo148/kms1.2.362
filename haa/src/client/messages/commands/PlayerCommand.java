package client.messages.commands;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.SecondaryStat;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ServerConstants;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.farm.FarmServer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import scripting.NPCScriptManager;
import server.MapleDonationSkill;
import server.MapleInventoryManipulator;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.Pair;
import tools.StringUtil;
import tools.packet.CWvsContext;

public class PlayerCommand {
  public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
    return ServerConstants.PlayerGMRank.NORMAL;
  }
  
  public static class 힘 extends DistributeStatCommands {}
  
  public static class 덱스 extends DistributeStatCommands {}
  
  public static class 인트 extends DistributeStatCommands {}
  
  public static class 럭 extends DistributeStatCommands {}
  
  public static class 초기화 extends DistributeStatCommands {}
  
  public static abstract class DistributeStatCommands extends CommandExecute {
    protected MapleStat stat = null;
    
    private void setStat(MapleCharacter player, int amount) {
      switch (this.stat) {
        case STR:
          player.getStat().setStr((short)amount, player);
          player.updateSingleStat(MapleStat.STR, player.getStat().getStr());
          break;
        case DEX:
          player.getStat().setDex((short)amount, player);
          player.updateSingleStat(MapleStat.DEX, player.getStat().getDex());
          break;
        case INT:
          player.getStat().setInt((short)amount, player);
          player.updateSingleStat(MapleStat.INT, player.getStat().getInt());
          break;
        case LUK:
          player.getStat().setLuk((short)amount, player);
          player.updateSingleStat(MapleStat.LUK, player.getStat().getLuk());
          break;
        case AVAILABLEAP:
          player.setRemainingAp((short)0);
          player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
          break;
      } 
    }
    
    private int getStat(MapleCharacter player) {
      switch (this.stat) {
        case STR:
          return player.getStat().getStr();
        case DEX:
          return player.getStat().getDex();
        case INT:
          return player.getStat().getInt();
        case LUK:
          return player.getStat().getLuk();
      } 
      throw new RuntimeException();
    }
    
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "잘못된 정보입니다.");
        return 0;
      } 
      int change = 0;
      try {
        change = Integer.parseInt(splitted[1]);
      } catch (NumberFormatException nfe) {
        c.getPlayer().dropMessage(5, "제대로 입력되지 못했습니다.");
        return 0;
      } 
      if (change <= 0) {
        c.getPlayer().dropMessage(5, "0보다 큰 숫자를 입력해야합니다.");
        return 0;
      } 
      if (c.getPlayer().getRemainingAp() < change) {
        c.getPlayer().dropMessage(5, "AP포인트보다 작은 숫자를 입력해야합니다.");
        return 0;
      } 
      if (getStat(c.getPlayer()) + change > statLim) {
        c.getPlayer().dropMessage(5, statLim + " 이상 스탯에 ap를 투자하실 수 없습니다.");
        return 0;
      } 
      setStat(c.getPlayer(), getStat(c.getPlayer()) + change);
      c.getPlayer().setRemainingAp((short)(c.getPlayer().getRemainingAp() - change));
      c.getPlayer().updateSingleStat(MapleStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
      c.getPlayer().dropMessage(5, StringUtil.makeEnumHumanReadable(this.stat.name()) + " 스탯이 " + change + " 만큼 증가하였습니다.");
      return 1;
    }
    
    private static int statLim = 32767;
  }
  
  public static class 몬스터 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMonster mob = null;
      for (MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000.0D, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        mob = (MapleMonster)monstermo;
        if (mob.isAlive()) {
          c.getPlayer().dropMessage(6, "몬스터 정보 :  " + mob.toString());
          break;
        } 
      } 
      if (mob == null)
        c.getPlayer().dropMessage(6, "주변에 몬스터가 없습니다."); 
      return 1;
    }
  }
  
      public static class 벅샷 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getKeyValue(201910, "DonationSkill") > 0) {
                for (final MapleDonationSkill stat : MapleDonationSkill.values()) {
                    if (stat.getSkillId() == 5321054) {
                        if ((c.getPlayer().getKeyValue(201910, "DonationSkill") & stat.getValue()) != 0) {
                            c.getPlayer().getStat().setMp(c.getPlayer().getStat().getCurrentMaxMp(c.getPlayer()), c.getPlayer());
                            if (!c.getPlayer().getBuffedValue(stat.getSkillId())) {
                                SkillFactory.getSkill(stat.getSkillId()).getEffect(SkillFactory.getSkill(stat.getSkillId()).getMaxLevel()).applyTo(c.getPlayer(), Integer.MAX_VALUE);
                            } else {
                                c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.Buckshot);
                            }
                        }
                    }
                }
            }
            return 1;
        }
    }
  
  public static class 명성치알림 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (c.getPlayer().getKeyValue(5, "show_honor") > 0L) {
        c.getPlayer().setKeyValue(5, "show_honor", "0");
      } else {
        c.getPlayer().setKeyValue(5, "show_honor", "1");
      } 
      return 1;
    }
  }
  
  public static abstract class OpenNPCCommand extends CommandExecute {
    protected int npc = -1;
    
    public int execute(MapleClient c, String[] splitted) {
      NPCScriptManager.getInstance().start(c, npcs[this.npc]);
      return 1;
    }
    
    private static int[] npcs = new int[] { 9000162, 9000000, 9010000 };
  }
  
       public static class 메획 extends OpenNPCCommand {
         
     public int execute(MapleClient c, String[] splitted) {
         StringBuilder String = new StringBuilder();
         String.append("메소 획득량 정보 (최대 300.0%) : 현재 획득량 ");
         String.append(c.getPlayer().getStat().mesoBuff);
         String.append("%                        기본 100.0% 임");
         c.getPlayer().dropMessage(5, String.toString());
         return 1;
       }
     }
    public static class 아획 extends OpenNPCCommand {

        public int execute(MapleClient c, String[] splitted) {
            StringBuilder String = new StringBuilder();
            String.append("아이템 획득량 정보 (최대 400.0%) : 현재");
            double dropBuff = c.getPlayer().getStat().dropBuff;
            if (!c.getPlayer().getBuffedValue(80002282)) {
                dropBuff -= c.getPlayer().getMap().getRuneCurseDecrease();
            }
            String.append(dropBuff);
            String.append("%                                  (기본 100.0%이며 400.0%를 초과해도 효과를 받을 수 없습니다.           보스 몬스터를 대상으로는 최대 300%만 적용됩니다)");
            c.getPlayer().dropMessage(5, String.toString());
            return 1;
        }
    }
  
  public static class 동접 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(-8, "[공지] Happy ! 에 접속중인 목록입니다.");
      int ret = 0;
      int cashshop = 0;
      for (ChannelServer csrv : ChannelServer.getAllInstances()) {
        int a = csrv.getPlayerStorage().getAllCharacters().size();
        ret += a;
        c.getPlayer().dropMessage(6, csrv.getChannel() + "채널 : " + a + "명\r\n");
      } 
      ret += CashShopServer.getPlayerStorage().getAllCharacters().size();
      c.getPlayer().dropMessage(6, "캐시샵 : " + CashShopServer.getPlayerStorage().getAllCharacters().size() + "명\r\n");
      ret += AuctionServer.getPlayerStorage().getAllCharacters().size();
      c.getPlayer().dropMessage(6, "경매장 : " + AuctionServer.getPlayerStorage().getAllCharacters().size() + "명\r\n");
      ret += FarmServer.getPlayerStorage().getAllCharacters().size();
      c.getPlayer().dropMessage(6, "농장 : " + FarmServer.getPlayerStorage().getAllCharacters().size() + "명\r\n");
      c.getPlayer().dropMessage(-8, "[Happy ! ] 총 유저 접속 수 : " + ret);
      return 1;
    }
  }
  
  public static class 워프 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      NPCScriptManager.getInstance().start(c, 9201305);
      return 1;
    }
  }
  
  public static class 코디 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      NPCScriptManager.getInstance().start(c, 3003273);
      return 1;
    }
  }
  
  public static class 엔피시 extends OpenNPCCommand {}
  
  public static class 이벤트엔피시 extends OpenNPCCommand {}
  
  public static class 드롭체크 extends OpenNPCCommand {}
  
  public static class 렉 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      c.getPlayer().dropMessage(5, "렉이 해제되었습니다.");
      return 1;
    }
  }
  
  public static class 보조무기해제 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      Equip equip = null;
      equip = (Equip)c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short)-10);
      if (equip == null) {
        c.getPlayer().dropMessage(1, "장착중인 보조무기가 존재하지 않습니다.");
        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        return 1;
      } 
      if (GameConstants.isZero(c.getPlayer().getJob())) {
        c.getPlayer().dropMessage(1, "제로는 보조무기를 해제하실 수 없습니다.");
        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        return 1;
      } 
      c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot((short)-10);
      c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIPPED, equip));
      return 1;
    }
  }
  
  public static class 보조무기장착 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int itemid = 0;
      switch (c.getPlayer().getJob()) {
        case 5100:
          itemid = 1098000;
          break;
        case 3100:
        case 3101:
          itemid = 1099000;
          break;
        case 6100:
          itemid = 1352500;
          break;
        case 6500:
          itemid = 1352600;
          break;
      } 
      if (itemid != 0) {
        Item item = MapleInventoryManipulator.addId_Item(c, itemid, (short)1, "", null, -1L, "", false);
        if (item != null) {
          MapleInventoryManipulator.equip(c, item.getPosition(), (short)-10, MapleInventoryType.EQUIP);
        } else {
          c.getPlayer().dropMessage(1, "오류가 발생했습니다.");
        } 
      } else {
        c.getPlayer().dropMessage(1, "보조무기 장착이 불가능한 직업군입니다.");
      } 
      return 1;
    }
  }
  
  public static class 저장 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().saveToDB(false, false);
      c.getPlayer().dropMessage(5, "저장되었습니다.");
      return 1;
    }
  }
  
    public static class 매그너스퀘스트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().forceCompleteQuest(31833);
      c.getPlayer().dropMessage(5, "매그너스 퀘스트가 정상적으로 완료되었습니다.");
      return 1;
    }
  }
  
  public static class 인벤초기화 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      Map<Pair<Short, Short>, MapleInventoryType> eqs = new HashMap<>();
      if (splitted[1].equals("모두")) {
        for (MapleInventoryType type : MapleInventoryType.values()) {
          for (Item item : c.getPlayer().getInventory(type))
            eqs.put(new Pair<>(Short.valueOf(item.getPosition()), Short.valueOf(item.getQuantity())), type); 
        } 
      } else if (splitted[1].equals("장착")) {
        for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED))
          eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())), MapleInventoryType.EQUIPPED); 
      } else if (splitted[1].equals("장비")) {
        for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.EQUIP))
          eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())), MapleInventoryType.EQUIP); 
      } else if (splitted[1].equals("소비")) {
        for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.USE))
          eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())), MapleInventoryType.USE); 
      } else if (splitted[1].equals("설치")) {
        for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.SETUP))
          eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())), MapleInventoryType.SETUP); 
      } else if (splitted[1].equals("기타")) {
        for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.ETC))
          eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())), MapleInventoryType.ETC); 
      } else if (splitted[1].equals("캐시")) {
        for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.CASH))
          eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())), MapleInventoryType.CASH); 
      } else if (splitted[1].equals("코디")) {
        for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.CODY))
          eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())), MapleInventoryType.CODY); 
      } else {
        c.getPlayer().dropMessage(6, "[모두/장착/장비/소비/설치/기타/캐시/코디]");
      } 
      for (Map.Entry<Pair<Short, Short>, MapleInventoryType> eq : eqs.entrySet())
        MapleInventoryManipulator.removeFromSlot(c, eq.getValue(), ((Short)((Pair)eq.getKey()).left).shortValue(), ((Short)((Pair)eq.getKey()).right).shortValue(), false, false); 
      return 1;
    }
  }
  
  public static class 데스카운트 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(-8, "남은 데스카운트 수 : " + c.getPlayer().getDeathCount());
      return 1;
    }
  }
  
  public static class 이동 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      NPCScriptManager.getInstance().start(c, 9062294, null);
      return 1;
    }
  }
  
    public static class 보스 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      NPCScriptManager.getInstance().start(c, 1540936, null);
      return 1;
    }
  }
    
    public static class 성장 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      NPCScriptManager.getInstance().start(c, 9062284, null);
      return 1;
    }
  }
        
    public static class 상점 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      NPCScriptManager.getInstance().start(c, 9062277, null);
      return 1;
    }
  }
    
        public static class 월드보스 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
      NPCScriptManager.getInstance().start(c, 9000044, null);
      return 1;
    }
  }

   
  public static class 마을 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (GameConstants.isContentsMap(c.getPlayer().getMapId())) {
        c.getPlayer().dropMessage(5, "해당 맵에선 이동이 불가능 합니다.");
        return 0;
      } 
      MapleMap mapz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(ServerConstants.warpMap);
      c.getPlayer().setDeathCount((byte)0);
      c.getPlayer().changeMap(mapz, mapz.getPortal(0));
      c.getPlayer().dispelDebuffs();
      (c.getPlayer()).Stigma = 0;
      Map<SecondaryStat, Pair<Integer, Integer>> dds = new HashMap<>();
      dds.put(SecondaryStat.Stigma, new Pair<>(Integer.valueOf((c.getPlayer()).Stigma), Integer.valueOf(0)));
      c.getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(dds, c.getPlayer()));
      c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CWvsContext.BuffPacket.cancelForeignBuff(c.getPlayer(), dds), false);
      c.getPlayer().addKV("bossPractice", "0");
      c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.DebuffIncHp);
      c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.FireBomb);
      return 1;
    }
  }
  
  public static class 버프 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (c.getPlayer().getBuffedEffect(80002924) != null) {
        c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(80002924));
      } else {
        SkillFactory.getSkill(80002924).getEffect(1).applyTo(c.getPlayer(), 0);
      } 
      return 0;
    }
  }
  
  public static class 빙고입장 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
                if (c.getChannelServer().getMapFactory().getMap(922290000).isBingoGame()) {
             if (c.getPlayer().getParty() != null) {
             c.getPlayer().dropMessage(6, "파티를 해제해주세요.");
             } else if (c.getPlayer().getMapId() / 1000 != 680000) {
             c.getPlayer().dropMessage(6, "마을이나 쉼터에서 시도해주세요.");
             } else if (c.getPlayer().getMapId() == 922290000) {
             c.getPlayer().dropMessage(6, "이미 빙고맵에 입장했습니다.");
             } else {
             c.getPlayer().warp(922290000);
             }
             } else {
             c.getPlayer().dropMessage(6, "이 채널에서는 빙고게임이 개설되고있지 않습니다.");
             }
      return 1;
    }
  }
  
    public static class 스킬마스터 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().skillMaster();
            return 1;
        }
    }
  
public static class Couuuuuter extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      return 1;
    }
  }
  
  public static class 도움말 extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(5, "@힘, @덱스, @인트, @럭 스탯포인트");
      c.getPlayer().dropMessage(5, "@렉 [엔피시,아이템 등 먹통일때 사용]");
      c.getPlayer().dropMessage(5, "@저장 [현재 캐릭터 저장]");
      c.getPlayer().dropMessage(5, "@마을 [Happy !  광장으로 이동]");
      c.getPlayer().dropMessage(5, "@상점 [상점 엔피시 호출]");
      c.getPlayer().dropMessage(5, "@보스 [보스 엔피시 호출]");
      c.getPlayer().dropMessage(5, "@성장 [성장 엔피시 호출]");
      c.getPlayer().dropMessage(5, "@아획 [아이템 확률 보기]");
      c.getPlayer().dropMessage(5, "@메획 [메소 확률 보기]");
      c.getPlayer().dropMessage(5, "@동접 [현재 서버에 접속중인 유저 수 확인]");
      c.getPlayer().dropMessage(5, "@인벤초기화 장비,소비,기타,설치,캐시,코디 ");
      c.getPlayer().dropMessage(5, "@이동 [이동 엔피시 오픈]");
      c.getPlayer().dropMessage(5, "~할말 [전채 채팅]");
      c.getPlayer().dropMessage(5, "[아이템 링크]~할말 [채팅] 시 아이템 확성기로 변환.");
      return 1;
    }
  }
}
