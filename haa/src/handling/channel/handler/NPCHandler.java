package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleQuestStatus;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ServerConstants;
import handling.SendPacketOpcode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import scripting.NPCConversationManager;
import scripting.NPCScriptManager;
import server.DimentionMirrorEntry;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleStorage;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import server.quest.MapleQuest;
import server.shops.MapleShop;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.SLFCGPacket;

public class NPCHandler {
  public static final void NPCAnimation(LittleEndianAccessor slea, MapleClient c) {
    if (c.getPlayer() == null)
      return; 
    if (c.getPlayer().getMapId() == 910143000)
      return; 
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.NPC_ACTION.getValue());
    int length = (int)slea.available();
    if (length == 10) {
      mplew.writeInt(slea.readInt());
      mplew.write(slea.readByte());
      mplew.write(slea.readByte());
      mplew.writeInt(slea.readInt());
    } else if (length > 10) {
      mplew.write(HexTool.getByteArrayFromHexString(slea.toString()));
    } else {
      return;
    } 
    c.getSession().writeAndFlush(mplew.getPacket());
  }
  
    public static final void NPCShop(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        byte bmode = slea.readByte();
        if (chr == null) {
            return;
        }
        switch (bmode) {
            case 0: {
                MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                short slot = slea.readShort();
                slot = (short)(slot + 1);
                int itemId = slea.readInt();
                short quantity = slea.readShort();
                shop.buy(c, itemId, quantity, slot);
                FileoutputUtil.log(FileoutputUtil.엔피시상점구매로그, "[상점아이템구매] 계정번호 : " + c.getAccID() + " | " + c.getPlayer().getName() + "(" + c.getPlayer().getId() + ")이 " + c.getPlayer().getMapId() + " 에서 [" + shop.getShopString() + "] 상점 (" + MapleItemInformationProvider.getInstance().getName(itemId) + ")(" + itemId + ")를 " + quantity + "개 구매");
                break;
            }
            case 1: {
                MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                short slot = slea.readShort();
                int itemId = slea.readInt();
                short quantity = slea.readShort();
                shop.sell(c, GameConstants.getInventoryType(itemId), slot, quantity);
                FileoutputUtil.log(FileoutputUtil.엔피시상점판매로그, "[상점아이템판매] 계정번호 : " + c.getAccID() + " | " + c.getPlayer().getName() + "(" + c.getPlayer().getId() + ")이 " + c.getPlayer().getMapId() + " 에서 [" + shop.getShopString() + "] 상점 (" + MapleItemInformationProvider.getInstance().getName(itemId) + ")(" + itemId + ")를 " + quantity + "개 판매");
                break;
            }
            case 2: {
                MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                short slot = slea.readShort();
                shop.recharge(c, slot);
                break;
            }
            default: {
                chr.setConversation(0);
            }
        }
    }
  
  public static final void NPCTalk(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
    if (chr == null || chr.getMap() == null)
      return; 
    MapleNPC npc = chr.getMap().getNPCByOid(slea.readInt());
    if (npc == null)
      return; 
    if (npc.hasShop()) {
      chr.setConversation(1);
      npc.sendShop(c);
      FileoutputUtil.log(FileoutputUtil.엔피시상점로그, "[상점오픈] 계정번호 : " + c.getAccID() + " | " + c.getPlayer().getName() + "(" + c.getPlayer().getId() + ")이 " + c.getPlayer().getMapId() + " 에서 상점엔피시 : " + MapleLifeFactory.getNPC(npc.getId()).getName() + "(" + npc.getId() + ")를 오픈");
    } else {
      NPCScriptManager.getInstance().start(c, npc.getId(), null);
      c.getPlayer().dropMessageGM(6, "OpenNPC(" + npc.getId() + ") : " + npc.getName());
    } 
  }
  
  public static final void QuestAction(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
    int itemid, npc;
    byte action = slea.readByte();
    int quest = slea.readInt();
    if (chr == null)
      return; 
    MapleQuest q = MapleQuest.getInstance(quest);
    switch (action) {
      case 0:
        slea.readInt();
        itemid = slea.readInt();
        q.RestoreLostItem(chr, itemid);
        break;
      case 1:
        npc = slea.readInt();
        if (!q.hasStartScript())
          q.start(chr, npc); 
        break;
      case 2:
        npc = slea.readInt();
        slea.readInt();
        if (q.hasEndScript() && (quest < 1115 || quest > 1124))
          return; 
        if (slea.available() >= 4L) {
          q.complete(chr, npc, Integer.valueOf(slea.readInt()), false);
          break;
        } 
        q.complete(chr, npc);
        break;
      case 3:
        if (GameConstants.canForfeit(q.getId())) {
          q.forfeit(chr);
          break;
        } 
        chr.dropMessage(1, "You may not forfeit this quest.");
        break;
      case 4:
        npc = slea.readInt();
        if (quest >= 37151 && quest <= 37180) {
          NPCScriptManager.getInstance().startQuest(c, npc, quest);
          break;
        } 
        if (quest == 100114 || quest == 100188) {
          NPCScriptManager.getInstance().startQuest(c, npc, quest);
          break;
        } 
        NPCScriptManager.getInstance().startQuest(c, npc, quest);
        break;
      case 5:
        npc = slea.readInt();
        NPCScriptManager.getInstance().endQuest(c, npc, quest, false);
        break;
    } 
  }
  
    public static final void Storage(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        byte mode = slea.readByte();
        if (chr == null) {
            return;
        }
        MapleStorage storage = chr.getStorage();
        switch (mode) {
            case 3: {
                if (c.CheckSecondPassword(slea.readMapleAsciiString())) {
                    c.getPlayer().getStorage().sendStorage(c, chr.getStorageNPC());
                    break;
                }
                c.getSession().writeAndFlush((Object)CField.NPCPacket.getStorage((byte)1));
                break;
            }
            case 4: {
                byte type = slea.readByte();
                byte slot = storage.getSlot(MapleInventoryType.getByType(type), slea.readByte());
                Item item = storage.takeOut(slot);
                if (item != null) {
                    if (c.getPlayer().getInventory(MapleInventoryType.getByType(type)).getNextFreeSlot() <= -1) {
                        storage.store(item);
                        chr.dropMessage(1, "인벤토리의 공간이 부족합니다.");
                        c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                        break;
                    }
                    if (ItemFlag.KARMA_EQUIP.check(item.getFlag())) {
                        item.setFlag(item.getFlag() - ItemFlag.KARMA_EQUIP.getValue());
                    }
                    FileoutputUtil.log(FileoutputUtil.창고퇴고로그, "[창고 퇴고] 계정 아이디 : " + c.getAccID() + " | " + chr.getName() + "이 창고에서 " + MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "을 " + item.getQuantity() + "개 퇴고.");
                    MapleInventoryManipulator.addbyItem(c, item, false);
                    storage.sendTakenOut(c, GameConstants.getInventoryType(item.getItemId()));
                    break;
                }
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                break;
            }
            case 5: {
                short slot = slea.readShort();
                int itemId = slea.readInt();
                MapleInventoryType type = GameConstants.getInventoryType(itemId);
                short quantity = slea.readShort();
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                if (quantity < 1) {
                    return;
                }
                if (storage.isFull()) {
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.getStorageFull());
                    return;
                }
                if (chr.getInventory(type).getItem(slot) == null) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if (chr.getMeso() < 100L) {
                    chr.dropMessage(1, "아이템을 맡기려면 100메소가 필요합니다.");
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                Item item = chr.getInventory(type).getItem(slot).copy();
                if (GameConstants.isPet(item.getItemId())) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if (ii.isPickupRestricted(item.getItemId()) && storage.findById(item.getItemId()) != null) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if (item.getItemId() == itemId && (item.getQuantity() >= quantity || GameConstants.isThrowingStar(itemId) || GameConstants.isBullet(itemId))) {
                    if (GameConstants.isThrowingStar(itemId) || GameConstants.isBullet(itemId)) {
                        quantity = item.getQuantity();
                    }
                    MapleInventoryManipulator.removeFromSlot(c, type, slot, quantity, false);
                    FileoutputUtil.log(FileoutputUtil.창고입고로그, "[창고 입고] 계정 아이디 : " + c.getAccID() + " | " + chr.getName() + "이 인벤토리에서 " + ii.getName(item.getItemId()) + "(" + item.getItemId() + ")을 " + item.getQuantity() + "개 입고.");
                    chr.gainMeso(-100L, false, false);
                    item.setQuantity(quantity);
                    storage.store(item);
                    storage.sendStored(c, GameConstants.getInventoryType(itemId));
                    break;
                }
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            case 6: {
                storage.arrange();
                storage.update(c);
                break;
            }
            case 7: {
                long meso = slea.readLong();
                long storageMesos = storage.getMeso();
                long playerMesos = chr.getMeso();
                if (meso < 0L) {
                    if (-meso <= playerMesos) {
                        storage.setMeso(storageMesos - meso);
                        chr.gainMeso(meso, false, false);
                    }
                } else if (meso > 0L && meso <= storageMesos) {
                    storage.setMeso(storageMesos - meso);
                    chr.gainMeso(meso, false, false);
                }
                storage.sendMeso(c);
                break;
            }
            case 8: {
                storage.close();
                chr.setConversation(0);
                break;
            }
            default: {
                System.out.println("Unhandled Storage mode : " + mode);
            }
        }
    }
  
/*     */   public static final void NPCMoreTalk(LittleEndianAccessor slea, MapleClient c) {
            slea.skip(4); // 361 new
/* 371 */     byte lastMsg = slea.readByte();
        if (lastMsg == 0) {
            slea.readInt();
            slea.readMapleAsciiString();
        }
/* 372 */     if (c.getPlayer() == null) {
/*     */       return;
/*     */     }
/* 375 */     if (c.getPlayer().isWatchingWeb() && lastMsg == 22) {
/* 376 */       c.getPlayer().setWatchingWeb(false);
/* 377 */       c.getSession().writeAndFlush(SLFCGPacket.ChangeVolume(100, 1000));
/*     */     } 
/* 379 */     if (lastMsg == 10 && slea.available() >= 4L) {
/* 380 */       slea.skip(2);
/* 381 */     } else if (lastMsg == 44) {
/* 382 */       byte dispose = slea.readByte();
/* 383 */       if (dispose == 0) {
/*     */         return;
/*     */       }
/* 386 */     } else if (lastMsg == 37) {
/* 387 */       NPCConversationManager npccm = NPCScriptManager.getInstance().getCM(c);
/* 388 */       if (npccm != null) {
/* 389 */         if (npccm.getType() == -1 || npccm.getType() == -2) {
/* 390 */           NPCScriptManager.getInstance().action(c, (byte)1, lastMsg, 0);
/*     */         } else {
/* 392 */           NPCScriptManager.getInstance().startQuest(c, (byte)1, lastMsg, 0);
/*     */         } 
/*     */       }
/*     */       return;
/*     */     } 
/* 397 */     byte action = slea.readByte();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 403 */     NPCConversationManager cm = NPCScriptManager.getInstance().getCM(c);
/*     */     
/* 405 */     if (cm == null) {
/*     */       return;
/*     */     }
/*     */     
/* 409 */     if (c.getPlayer().getConversation() == 0) {
/* 410 */       cm.dispose();
/*     */       
/*     */       return;
/*     */     } 
/* 414 */     if (lastMsg == 6 && 
/* 415 */       action == 0 && slea.available() < 1L) {
/* 416 */       c.removeClickedNPC();
/* 417 */       NPCScriptManager.getInstance().dispose(c);
/* 418 */       c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
/*     */       
/*     */       return;
/*     */     } 
/* 422 */     cm.setLastMsg((byte)-1);
/* 423 */     if (lastMsg == 4) {
/* 424 */       if (action != 0) {
/* 425 */         cm.setGetText(slea.readMapleAsciiString());
/* 426 */         if (cm.getType() == 0) {
/* 427 */           NPCScriptManager.getInstance().startQuest(c, action, lastMsg, -1);
/* 428 */         } else if (cm.getType() == 1) {
/* 429 */           NPCScriptManager.getInstance().endQuest(c, action, lastMsg, -1);
/*     */         } else {
/* 431 */           NPCScriptManager.getInstance().action(c, action, lastMsg, -1);
/*     */         } 
/*     */       } else {
/* 434 */         cm.dispose();
/*     */       } 
/*     */     } else {
/* 437 */       int selection = -1;
/* 438 */       int selection2 = -1;
/* 439 */       if (slea.available() >= 6L && 
/* 440 */         lastMsg == 20 && action == 3) {
/* 441 */         int unk = slea.readByte();
/* 442 */         int unk2 = slea.readByte();
/* 443 */         if (unk == 0 && unk2 == 1) {
/*     */           return;
/*     */         }
/* 446 */         if (unk == 0 && unk2 == 0) {
/*     */           return;
/*     */         }
/* 449 */         NPCScriptManager.getInstance().startQuest(c, action, lastMsg, selection);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 455 */       if (slea.available() >= 4L) {
/*     */         
/* 457 */         selection = slea.readInt();
/* 458 */       } else if (slea.available() > 0L) {
/* 459 */         if (GameConstants.isZero(c.getPlayer().getJob()) && lastMsg == 32) {
/* 460 */           selection = slea.readByte();
/* 461 */           selection2 = slea.readByte();
/*     */         } else {
/* 463 */           if (lastMsg == 10) {
/* 464 */             slea.skip(1);
/*     */           }
/* 466 */           selection = slea.readByte();
/*     */         } 
/*     */       } 
/* 469 */       if (lastMsg == 44) {
/* 470 */         slea.skip(2);
/* 471 */         int nMixBaseHairColor = slea.readInt();
/* 472 */         int nMixAddHairColor = slea.readInt();
/* 473 */         int nMixHairBaseProb = slea.readInt();
/* 474 */         if (GameConstants.isZero(c.getPlayer().getJob())) {
/* 475 */           if (c.getPlayer().getGender() == 1) {
/* 476 */             c.getPlayer().setSecondBaseColor(nMixBaseHairColor);
/* 477 */             c.getPlayer().setSecondAddColor(nMixAddHairColor);
/* 478 */             c.getPlayer().setSecondBaseProb(nMixHairBaseProb);
/* 479 */             c.getPlayer().updateZeroStats();
/*     */           } else {
/* 481 */             c.getPlayer().setBaseColor(nMixBaseHairColor);
/* 482 */             c.getPlayer().setAddColor(nMixAddHairColor);
/* 483 */             c.getPlayer().setBaseProb(nMixHairBaseProb);
/*     */           } 
/* 485 */         } else if (GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
/* 486 */           if (c.getPlayer().getDressup()) {
/* 487 */             c.getPlayer().setSecondBaseColor(nMixBaseHairColor);
/* 488 */             c.getPlayer().setSecondAddColor(nMixAddHairColor);
/* 489 */             c.getPlayer().setSecondBaseProb(nMixHairBaseProb);
/* 490 */             c.getPlayer().updateAngelicStats();
/*     */           } else {
/* 492 */             c.getPlayer().setBaseColor(nMixBaseHairColor);
/* 493 */             c.getPlayer().setAddColor(nMixAddHairColor);
/* 494 */             c.getPlayer().setBaseProb(nMixHairBaseProb);
/*     */           } 
/*     */         } else {
/* 497 */           c.getPlayer().setBaseColor(nMixBaseHairColor);
/* 498 */           c.getPlayer().setAddColor(nMixAddHairColor);
/* 499 */           c.getPlayer().setBaseProb(nMixHairBaseProb);
/*     */         } 
/* 501 */         c.getPlayer().equipChanged();
/* 502 */         c.removeClickedNPC();
/* 503 */         NPCScriptManager.getInstance().dispose(c);
/* 504 */         c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
/*     */         return;
/*     */       } 
/* 507 */       if (lastMsg == 4 && selection == -1 && selection2 == -1) {
/* 508 */         cm.dispose();
/*     */         return;
/*     */       } 
/* 511 */       if (selection >= -1 && action != -1) {
/* 512 */         if (cm.getType() == 0) {
/* 513 */           NPCScriptManager.getInstance().startQuest(c, action, lastMsg, selection);
/* 514 */         } else if (cm.getType() == 1) {
/* 515 */           NPCScriptManager.getInstance().endQuest(c, action, lastMsg, selection);
/*     */         }
/* 517 */         else if (GameConstants.isZero(c.getPlayer().getJob()) && lastMsg == 32) {
/* 518 */           NPCScriptManager.getInstance().zeroaction(c, action, lastMsg, selection, selection2);
/*     */         } else {
/* 520 */           NPCScriptManager.getInstance().action(c, action, lastMsg, selection);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 525 */         cm.dispose();
/*     */       } 
/*     */     } 
/*     */   }
  
  public static final void repairAll(MapleClient c) {
    if (c.getPlayer().getMapId() != 240000000)
      return; 
    int price = 0;
    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
    Map<Equip, Integer> eqs = new HashMap<>();
    MapleInventoryType[] types = { MapleInventoryType.EQUIP, MapleInventoryType.EQUIPPED };
    for (MapleInventoryType type : types) {
      for (Item item : c.getPlayer().getInventory(type).newList()) {
        if (item instanceof Equip) {
          Equip eq = (Equip)item;
          if (eq.getDurability() >= 0) {
            Map<String, Integer> eqStats = ii.getEquipStats(eq.getItemId());
            if (eqStats.containsKey("durability") && ((Integer)eqStats.get("durability")).intValue() > 0 && eq.getDurability() < ((Integer)eqStats.get("durability")).intValue()) {
              double rPercentage = 100.0D - Math.ceil(eq.getDurability() * 1000.0D / ((Integer)eqStats.get("durability")).intValue() * 10.0D);
              eqs.put(eq, eqStats.get("durability"));
              price += (int)Math.ceil(rPercentage * ii.getPrice(eq.getItemId()) / ((ii.getReqLevel(eq.getItemId()) < 70) ? 100.0D : 1.0D));
            } 
          } 
        } 
      } 
    } 
    if (eqs.size() <= 0 || c.getPlayer().getMeso() < price)
      return; 
    c.getPlayer().gainMeso(-price, true);
    for (Map.Entry<Equip, Integer> eqqz : eqs.entrySet()) {
      Equip ez = eqqz.getKey();
      ez.setDurability(((Integer)eqqz.getValue()).intValue());
      c.getPlayer().forceReAddItem(ez.copy(), (ez.getPosition() < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP);
    } 
  }
  
  public static final void repair(LittleEndianAccessor slea, MapleClient c) {
    if (c.getPlayer().getMapId() != 240000000 || slea.available() < 4L)
      return; 
    int position = slea.readInt();
    MapleInventoryType type = (position < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP;
    Item item = c.getPlayer().getInventory(type).getItem((short)position);
    if (item == null)
      return; 
    Equip eq = (Equip)item;
    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
    Map<String, Integer> eqStats = ii.getEquipStats(item.getItemId());
    if (eq.getDurability() < 0 || !eqStats.containsKey("durability") || ((Integer)eqStats.get("durability")).intValue() <= 0 || eq.getDurability() >= ((Integer)eqStats.get("durability")).intValue())
      return; 
    double rPercentage = 100.0D - Math.ceil(eq.getDurability() * 1000.0D / ((Integer)eqStats.get("durability")).intValue() * 10.0D);
    int price = (int)Math.ceil(rPercentage * ii.getPrice(eq.getItemId()) / ((ii.getReqLevel(eq.getItemId()) < 70) ? 100.0D : 1.0D));
    if (c.getPlayer().getMeso() < price)
      return; 
    c.getPlayer().gainMeso(-price, false);
    eq.setDurability(((Integer)eqStats.get("durability")).intValue());
    c.getPlayer().forceReAddItem(eq.copy(), type);
  }
  
  public static final void UpdateQuest(LittleEndianAccessor slea, MapleClient c) {
    MapleQuest quest = MapleQuest.getInstance(slea.readInt());
    if (quest != null)
      c.getPlayer().updateQuest(c.getPlayer().getQuest(quest), true); 
  }
  
  public static final void UseItemQuest(LittleEndianAccessor slea, MapleClient c) {
    short slot = slea.readShort();
    int itemId = slea.readInt();
    Item item = c.getPlayer().getInventory(MapleInventoryType.ETC).getItem(slot);
    int qid = slea.readInt();
    MapleQuest quest = MapleQuest.getInstance(qid);
    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
    Pair<Integer, List<Integer>> questItemInfo = null;
    boolean found = false;
    for (Item i : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
      if (i.getItemId() / 10000 == 422) {
        questItemInfo = ii.questItemInfo(i.getItemId());
        if (questItemInfo != null && ((Integer)questItemInfo.getLeft()).intValue() == qid && questItemInfo.getRight() != null && ((List)questItemInfo.getRight()).contains(Integer.valueOf(itemId))) {
          found = true;
          break;
        } 
      } 
    } 
    if (quest != null && found && item != null && item.getQuantity() > 0 && item.getItemId() == itemId) {
      int newData = slea.readInt();
      MapleQuestStatus stats = c.getPlayer().getQuestNoAdd(quest);
      if (stats != null && stats.getStatus() == 1) {
        stats.setCustomData(String.valueOf(newData));
        c.getPlayer().updateQuest(stats, true);
        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.ETC, slot, (short)1, false);
      } 
    } 
  }
  
  public static void quickMove(LittleEndianAccessor slea, MapleClient c) {
    NPCScriptManager.getInstance().start(c, slea.readInt());
  }
  
  public static void dimentionMirror(LittleEndianAccessor slea, MapleClient c) {
    slea.skip(4);
    int id = slea.readInt();
    for (DimentionMirrorEntry dm : ServerConstants.mirrors) {
      if (dm.getId() == id)
        NPCScriptManager.getInstance().start(c, dm.getScript()); 
    } 
  }
}
