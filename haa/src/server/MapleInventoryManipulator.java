// 
// Decompiled by Procyon v0.5.36
// 

package server;

import server.maps.MapleMapObject;
import tools.FileoutputUtil;
import java.awt.Point;
import constants.ServerConstants;
import client.MapleQuestStatus;
import java.util.Map;
import client.SkillFactory;
import client.SecondaryStat;
import client.MapleTrait;
import client.inventory.MapleAndroid;
import client.StructPotentialItem;
import java.util.ArrayList;
import constants.KoreaCalendar;
import tools.StringUtil;
import client.inventory.EquipAdditions;
import server.quest.MapleQuest;
import java.util.Iterator;
import java.util.List;
import client.inventory.InventoryException;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MaplePet;
import client.inventory.MapleInventoryType;
import scripting.NPCConversationManager;
import client.inventory.Equip;
import tools.packet.CWvsContext;
import client.MapleClient;
import client.inventory.Item;
import tools.packet.CSPacket;
import constants.GameConstants;
import client.MapleCharacter;

public class MapleInventoryManipulator
{
    public static void addRing(final MapleCharacter chr, final int itemId, final long ringId, final int sn, final String partner) {
        final CashItemInfo csi = CashItemFactory.getInstance().getItem(sn);
        if (csi == null) {
            return;
        }
        final Item ring = chr.getCashInventory().toItem(csi, ringId);
        if (ring == null || ring.getUniqueId() != ringId || ring.getUniqueId() <= 0L || ring.getItemId() != itemId) {
            return;
        }
        chr.getCashInventory().addToInventory(ring);
        chr.getClient().getSession().writeAndFlush((Object)CSPacket.sendBoughtRings(GameConstants.isCrushRing(itemId), ring, sn, chr.getClient(), partner));
    }
    
    public static boolean addbyItem(final MapleClient c, final Item item) {
        return addbyItem(c, item, false, false) >= 0;
    }
    
    public static boolean addbyItem(final MapleClient c, final Item item, final boolean fromcs) {
        return addbyItem(c, item, false, false) >= 0;
    }
    
    public static short addbyItem(final MapleClient c, final Item item, final boolean sort, final boolean fromcs) {
        final MapleInventoryType type = GameConstants.getInventoryType(item.getItemId());
        final short newSlot = c.getPlayer().getInventory(type).addItem(item);
        if (newSlot == -1) {
            if (!fromcs) {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
            }
            return newSlot;
        }
        if (GameConstants.isHarvesting(item.getItemId())) {
            c.getPlayer().getStat().handleProfessionTool(c.getPlayer());
        }
        if (GameConstants.isPet(item.getItemId())) {}
        if (GameConstants.isArcaneSymbol(item.getItemId()) && !sort) {
            final Equip equip = (Equip)item;
            if (equip.getArcLevel() == 0) {
                equip.setArc((short)30);
                equip.setArcLevel(1);
                equip.setArcEXP(1);
                if (GameConstants.isXenon(c.getPlayer().getJob())) {
                    equip.setStr((short)117);
                    equip.setDex((short)117);
                    equip.setLuk((short)117);
                }
                else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
                    equip.setHp((short)525);
                }
                else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
                    equip.setStr((short)300);
                }
                else if (GameConstants.isMagician(c.getPlayer().getJob())) {
                    equip.setInt((short)300);
                }
                else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob()) || GameConstants.isMechanic(c.getPlayer().getJob()) || GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                    equip.setDex((short)300);
                }
                else if (GameConstants.isThief(c.getPlayer().getJob())) {
                    equip.setLuk((short)300);
                }
                else if (GameConstants.isPirate(c.getPlayer().getJob())) {
                    equip.setStr((short)300);
                }
            }
        }
        else if (GameConstants.isAuthenticSymbol(item.getItemId()) && !sort) {
            final Equip equip = (Equip)item;
            if (equip.getArcLevel() == 0) {
                equip.setArc((short)10);
                equip.setArcLevel(1);
                equip.setArcEXP(1);
                if (GameConstants.isXenon(c.getPlayer().getJob())) {
                    equip.setStr((short)317);
                    equip.setDex((short)317);
                    equip.setLuk((short)317);
                }
                else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
                    equip.setHp((short)725);
                }
                else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
                    equip.setStr((short)500);
                }
                else if (GameConstants.isMagician(c.getPlayer().getJob())) {
                    equip.setInt((short)500);
                }
                else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob()) || GameConstants.isMechanic(c.getPlayer().getJob()) || GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                    equip.setDex((short)500);
                }
                else if (GameConstants.isThief(c.getPlayer().getJob())) {
                    equip.setLuk((short)500);
                }
                else if (GameConstants.isPirate(c.getPlayer().getJob())) {
                    equip.setStr((short)500);
                }
            }
        }
        if (item.getItemId() >= 1113098 && item.getItemId() <= 1113128 && !sort) {
            final Equip equip = (Equip)item;
            if (equip.getBaseLevel() == 0) {
                final byte lvl = (byte)Randomizer.rand(1, 4);
                equip.setLevel(lvl);
            }
        }
        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, item));
        c.getPlayer().havePartyQuest(item.getItemId());
        if (item.getItemId() == 4001886) {
            c.getSession().writeAndFlush((Object)CWvsContext.setBossReward(c.getPlayer()));
        }
        if (item.getQuantity() >= 300 && !sort) {
            final String data = c.getPlayer().getName() + " | " + item.getItemId() + " (x" + item.getQuantity() + ")를 addbyItem을 통해 얻음.\r\n";
            NPCConversationManager.writeLog("Log/Item.log", data, true);
        }
        return newSlot;
    }
    
    public static long getUniqueId(final int itemId, final MaplePet pet) {
        long uniqueid = -1L;
        if (GameConstants.isPet(itemId)) {
            if (pet != null) {
                uniqueid = pet.getUniqueId();
            }
            else {
                uniqueid = MapleInventoryIdentifier.getInstance();
            }
        }
        else if (GameConstants.getInventoryType(itemId) == MapleInventoryType.CODY || GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH || MapleItemInformationProvider.getInstance().isCash(itemId)) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }
        return uniqueid;
    }
    
    public static boolean addById(final MapleClient c, final int itemId, final short quantity, final String gmLog) {
        return addById(c, itemId, quantity, null, null, 0L, gmLog, false);
    }
    
    public static boolean addById(final MapleClient c, final int itemId, final short quantity, final String owner, final String gmLog) {
        return addById(c, itemId, quantity, owner, null, 0L, gmLog, false);
    }
    
    public static boolean addById(final MapleClient c, final int itemId, final short quantity, final String gmLog, final boolean special) {
        return addById(c, itemId, quantity, null, null, 0L, gmLog, special);
    }
    
    public static byte addId(final MapleClient c, final int itemId, final short quantity, final String owner, final String gmLog) {
        return addId(c, itemId, quantity, owner, null, 0L, gmLog, false);
    }
    
    public static boolean addById(final MapleClient c, final int itemId, final short quantity, final String owner, final MaplePet pet, final String gmLog) {
        return addById(c, itemId, quantity, owner, pet, 0L, gmLog, false);
    }
    
    public static boolean addById(final MapleClient c, final int itemId, final short quantity, final String owner, final MaplePet pet, final long period, final String gmLog) {
        return addId(c, itemId, quantity, owner, pet, period, gmLog, false) >= 0;
    }
    
    public static boolean addById(final MapleClient c, final int itemId, final short quantity, final String owner, final MaplePet pet, final long period, final String gmLog, final boolean special) {
        return addId(c, itemId, quantity, owner, pet, period, gmLog, special) >= 0;
    }
    
    public static byte addId(final MapleClient c, final int itemId, short quantity, final String owner, final MaplePet pet, final long period, final String gmLog, final boolean special) {
        if (quantity >= 300) {
            final String data = c.getPlayer().getName() + " | " + itemId + " (x" + quantity + ")를 addbyId를 통해 얻음.\r\n";
            NPCConversationManager.writeLog("Log/Item.log", data, true);
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if ((ii.isPickupRestricted(itemId) && c.getPlayer().haveItem(itemId, 1, true, false)) || !ii.itemExists(itemId)) {
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.showItemUnavailable());
            return -1;
        }
        final MapleInventoryType type = GameConstants.getInventoryType(itemId);
        final long uniqueid = getUniqueId(itemId, pet);
        short newSlot = -1;
        if (!type.equals(MapleInventoryType.EQUIP) && !type.equals(MapleInventoryType.CODY)) {
            final short slotMax = ii.getSlotMax(itemId);
            final List<Item> existing = c.getPlayer().getInventory(type).listById(itemId);
            if (!GameConstants.isRechargable(itemId)) {
                if (!ii.isCash(itemId) && existing.size() > 0) {
                    final Iterator<Item> i = existing.iterator();
                    while (quantity > 0 && i.hasNext()) {
                        final Item eItem = i.next();
                        final short oldQ = eItem.getQuantity();
                        if (oldQ < slotMax && (eItem.getOwner().equals(owner) || owner == null) && eItem.getExpiration() == -1L) {
                            final short newQ = (short)Math.min(oldQ + quantity, slotMax);
                            quantity -= (short)(newQ - oldQ);
                            eItem.setQuantity(newQ);
                            int flag = eItem.getFlag();
                            if (ii.isCash(itemId)) {
                                if (GameConstants.isEquip(itemId)) {
                                    if (!ItemFlag.KARMA_EQUIP.check(flag)) {
                                        flag |= ItemFlag.KARMA_EQUIP.getValue();
                                    }
                                }
                                else if (!ItemFlag.KARMA_USE.check(flag)) {
                                    flag |= ItemFlag.KARMA_USE.getValue();
                                }
                            }
                            eItem.setFlag(flag);
                            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventorySlot(type, eItem, false));
                        }
                    }
                }
                while (quantity > 0) {
                    final short newQ2 = (short)Math.min(quantity, slotMax);
                    if (newQ2 == 0) {
                        c.getPlayer().havePartyQuest(itemId);
                        c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                        return (byte)newSlot;
                    }
                    quantity -= newQ2;
                    final Item nItem = new Item(itemId, (short)0, newQ2, 0, uniqueid);
                    newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                    if (newSlot == -1) {
                        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                        return -1;
                    }
                    if (gmLog != null) {
                        nItem.setGMLog(gmLog);
                    }
                    if (owner != null) {
                        nItem.setOwner(owner);
                    }
                    if (period > 0L) {
                        nItem.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
                    }
                    if (pet != null) {
                        nItem.setFlag(ItemFlag.KARMA_USE.getValue());
                        nItem.setPet(pet);
                        pet.setInventoryPosition(newSlot);
                    }
                    int flag2 = nItem.getFlag();
                    if (ii.isCash(itemId)) {
                        if (GameConstants.isEquip(itemId)) {
                            if (!ItemFlag.KARMA_EQUIP.check(flag2)) {
                                flag2 |= ItemFlag.KARMA_EQUIP.getValue();
                            }
                        }
                        else if (!ItemFlag.KARMA_USE.check(flag2)) {
                            flag2 |= ItemFlag.KARMA_USE.getValue();
                        }
                    }
                    nItem.setFlag(flag2);
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nItem));
                    if (GameConstants.isRechargable(itemId) && quantity == 0) {
                        break;
                    }
                }
            }
            else {
                final Item nItem = new Item(itemId, (short)0, quantity, 0, uniqueid);
                newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                    return -1;
                }
                if (period > 0L) {
                    nItem.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
                }
                if (gmLog != null) {
                    nItem.setGMLog(gmLog);
                }
                int flag3 = nItem.getFlag();
                if (ii.isCash(itemId)) {
                    if (ii.isCash(itemId)) {
                        if (GameConstants.isEquip(itemId)) {
                            if (!ItemFlag.KARMA_EQUIP.check(flag3)) {
                                flag3 |= ItemFlag.KARMA_EQUIP.getValue();
                            }
                        }
                        else if (!ItemFlag.KARMA_USE.check(flag3)) {
                            flag3 |= ItemFlag.KARMA_USE.getValue();
                        }
                    }
                    nItem.setUniqueId(MapleInventoryIdentifier.getInstance());
                }
                nItem.setFlag(flag3);
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nItem));
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            }
        }
        else {
            if (quantity != 1) {
                throw new InventoryException("Trying to create equip with non-one quantity");
            }
            final Item nEquip = ii.getEquipById(itemId, uniqueid);
            if (owner != null) {
                nEquip.setOwner(owner);
            }
            if (gmLog != null) {
                nEquip.setGMLog(gmLog);
            }
            if (period > 0L) {
                nEquip.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
            }
            int flag4 = nEquip.getFlag();
            if (ii.isCash(itemId)) {
                nEquip.setUniqueId(MapleInventoryIdentifier.getInstance());
                if (GameConstants.isEquip(itemId)) {
                    if (!ItemFlag.KARMA_EQUIP.check(flag4)) {
                        flag4 |= ItemFlag.KARMA_EQUIP.getValue();
                    }
                }
                else if (!ItemFlag.KARMA_USE.check(flag4)) {
                    flag4 |= ItemFlag.KARMA_USE.getValue();
                }
            }
            nEquip.setFlag(flag4);
            newSlot = c.getPlayer().getInventory(type).addItem(nEquip);
            if (newSlot == -1) {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                return -1;
            }
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nEquip));
            if (GameConstants.isHarvesting(itemId)) {
                c.getPlayer().getStat().handleProfessionTool(c.getPlayer());
            }
        }
        c.getPlayer().havePartyQuest(itemId);
        if (itemId == 4001886) {
            c.getSession().writeAndFlush((Object)CWvsContext.setBossReward(c.getPlayer()));
        }
        
        
        return (byte)newSlot;
    }
    
    public static Item addId_Item(final MapleClient c, final int itemId, short quantity, final String owner, final MaplePet pet, final long period, final String gmLog, final boolean special) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if ((ii.isPickupRestricted(itemId) && c.getPlayer().haveItem(itemId, 1, true, false)) || !ii.itemExists(itemId)) {
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.showItemUnavailable());
            return null;
        }
        final MapleInventoryType type = GameConstants.getInventoryType(itemId);
        final long uniqueid = getUniqueId(itemId, pet);
        short newSlot = -1;
        if (!type.equals(MapleInventoryType.EQUIP) && !type.equals(MapleInventoryType.CODY)) {
            final short slotMax = ii.getSlotMax(itemId);
            final List<Item> existing = c.getPlayer().getInventory(type).listById(itemId);
            if (!GameConstants.isRechargable(itemId)) {
                if (existing.size() > 0) {
                    final Iterator<Item> i = existing.iterator();
                    while (quantity > 0 && i.hasNext()) {
                        final Item eItem = i.next();
                        final short oldQ = eItem.getQuantity();
                        if (oldQ < slotMax && (eItem.getOwner().equals(owner) || owner == null) && eItem.getExpiration() == -1L) {
                            final short newQ = (short)Math.min(oldQ + quantity, slotMax);
                            quantity -= (short)(newQ - oldQ);
                            eItem.setQuantity(newQ);
                            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventorySlot(type, eItem, false));
                        }
                    }
                }
                Label_0581: {
                    if (quantity > 0) {
                        final short newQ2 = (short)Math.min(quantity, slotMax);
                        Item nItem;
                        if (newQ2 != 0) {
                            quantity -= newQ2;
                            nItem = new Item(itemId, (short)0, newQ2, 0, uniqueid);
                            newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                            if (newSlot == -1) {
                                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                                return null;
                            }
                            if (gmLog != null) {
                                nItem.setGMLog(gmLog);
                            }
                            if (owner != null) {
                                nItem.setOwner(owner);
                            }
                            if (period > 0L) {
                                nItem.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
                            }
                            if (pet != null) {
                                nItem.setFlag(ItemFlag.KARMA_USE.getValue());
                                nItem.setPet(pet);
                                pet.setInventoryPosition(newSlot);
                            }
                            int flag = nItem.getFlag();
                            if (ii.isCash(itemId)) {
                                if (GameConstants.isEquip(itemId)) {
                                    if (!ItemFlag.KARMA_EQUIP.check(flag)) {
                                        flag |= ItemFlag.KARMA_EQUIP.getValue();
                                    }
                                }
                                else if (!ItemFlag.KARMA_USE.check(flag)) {
                                    flag |= ItemFlag.KARMA_USE.getValue();
                                }
                            }
                            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nItem));
                            if (GameConstants.isRechargable(itemId) && quantity == 0) {
                                break Label_0581;
                            }
                        }
                        else {
                            c.getPlayer().havePartyQuest(itemId);
                            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                            nItem = null;
                        }
                        return nItem;
                    }
                }
                c.getPlayer().havePartyQuest(itemId);
                if (itemId == 4001886) {
                    c.getSession().writeAndFlush((Object)CWvsContext.setBossReward(c.getPlayer()));
                }
                return null;
            }
            Item nItem = new Item(itemId, (short)0, quantity, 0, uniqueid);
            newSlot = c.getPlayer().getInventory(type).addItem(nItem);
            if (newSlot == -1) {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                return null;
            }
            if (period > 0L) {
                nItem.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
            }
            if (gmLog != null) {
                nItem.setGMLog(gmLog);
            }
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nItem));
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return nItem;
        }
        else {
            if (quantity != 1) {
                throw new InventoryException("Trying to create equip with non-one quantity");
            }
            final Item nEquip = ii.getEquipById(itemId, uniqueid);
            if (owner != null) {
                nEquip.setOwner(owner);
            }
            if (gmLog != null) {
                nEquip.setGMLog(gmLog);
            }
            if (period > 0L) {
                nEquip.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
            }
            if (special) {
                final Equip eq = (Equip)nEquip;
                final short n = (short)Randomizer.rand(1, 5);
            }
            newSlot = c.getPlayer().getInventory(type).addItem(nEquip);
            if (newSlot == -1) {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                return null;
            }
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nEquip));
            if (GameConstants.isHarvesting(itemId)) {
                c.getPlayer().getStat().handleProfessionTool(c.getPlayer());
            }
            return nEquip;
        }
    }
    
    public static Item addbyId_Gachapon(final MapleClient c, final int itemId, short quantity) {
        if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot() == -1 || c.getPlayer().getInventory(MapleInventoryType.USE).getNextFreeSlot() == -1 || c.getPlayer().getInventory(MapleInventoryType.ETC).getNextFreeSlot() == -1 || c.getPlayer().getInventory(MapleInventoryType.SETUP).getNextFreeSlot() == -1) {
            return null;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if ((ii.isPickupRestricted(itemId) && c.getPlayer().haveItem(itemId, 1, true, false)) || !ii.itemExists(itemId)) {
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.showItemUnavailable());
            return null;
        }
        final MapleInventoryType type = GameConstants.getInventoryType(itemId);
        if (!type.equals(MapleInventoryType.EQUIP) && !type.equals(MapleInventoryType.CODY)) {
            final short slotMax = ii.getSlotMax(itemId);
            final List<Item> existing = c.getPlayer().getInventory(type).listById(itemId);
            if (!GameConstants.isRechargable(itemId)) {
                Item nItem = null;
                boolean recieved = false;
                if (existing.size() > 0) {
                    final Iterator<Item> i = existing.iterator();
                    while (quantity > 0 && i.hasNext()) {
                        nItem = i.next();
                        final short oldQ = nItem.getQuantity();
                        if (oldQ < slotMax) {
                            recieved = true;
                            final short newQ = (short)Math.min(oldQ + quantity, slotMax);
                            quantity -= (short)(newQ - oldQ);
                            nItem.setQuantity(newQ);
                            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventorySlot(type, nItem, false));
                        }
                    }
                }
                while (quantity > 0) {
                    final short newQ2 = (short)Math.min(quantity, slotMax);
                    if (newQ2 == 0) {
                        break;
                    }
                    quantity -= newQ2;
                    nItem = new Item(itemId, (short)0, newQ2, 0);
                    final short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                    if (newSlot == -1 && recieved) {
                        return nItem;
                    }
                    if (newSlot == -1) {
                        return null;
                    }
                    recieved = true;
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nItem));
                    if (GameConstants.isRechargable(itemId) && quantity == 0) {
                        break;
                    }
                }
                if (recieved) {
                    c.getPlayer().havePartyQuest(nItem.getItemId());
                    return nItem;
                }
                return null;
            }
            else {
                final Item nItem = new Item(itemId, (short)0, quantity, 0);
                final short newSlot2 = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot2 == -1) {
                    return null;
                }
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nItem));
                c.getPlayer().havePartyQuest(nItem.getItemId());
                return nItem;
            }
        }
        else {
            if (quantity != 1) {
                throw new InventoryException("Trying to create equip with non-one quantity");
            }
            final Item item = ii.getEquipById(itemId);
            final short newSlot3 = c.getPlayer().getInventory(type).addItem(item);
            if (newSlot3 == -1) {
                return null;
            }
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, item, true));
            c.getPlayer().havePartyQuest(item.getItemId());
            return item;
        }
    }
    
    public static boolean addFromDrop(final MapleClient c, final Item item, final boolean show) {
        return addFromDrop(c, item, show, false, false);
    }
    
    public static boolean addFromDrop(final MapleClient c, final Item item, final boolean show, final boolean enhance, final boolean pet) {
        return addFromDrop(c, item, show, enhance, pet, false);
    }
    
    public static boolean addFromDrop(final MapleClient c, Item item, final boolean show, final boolean enhance, final boolean pet, final boolean sort) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (c.getPlayer() == null || (ii.isPickupRestricted(item.getItemId()) && !sort && c.getPlayer().haveItem(item.getItemId(), 1, true, false)) || !ii.itemExists(item.getItemId())) {
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.showItemUnavailable());
            return false;
        }
        final int before = c.getPlayer().itemQuantity(item.getItemId());
        short quantity = item.getQuantity();
        final MapleInventoryType type = GameConstants.getInventoryType(item.getItemId());
        if (!type.equals(MapleInventoryType.EQUIP) && !type.equals(MapleInventoryType.CODY)) {
            final short slotMax = ii.getSlotMax(item.getItemId());
            final List<Item> existing = c.getPlayer().getInventory(type).listById(item.getItemId());
            if (!GameConstants.isRechargable(item.getItemId())) {
                if (quantity <= 0) {
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.showItemUnavailable());
                    return false;
                }
                if (existing.size() > 0) {
                    final Iterator<Item> i = existing.iterator();
                    while (quantity > 0 && i.hasNext()) {
                        final Item eItem = i.next();
                        final short oldQ = eItem.getQuantity();
                        if (oldQ < slotMax && item.getOwner().equals(eItem.getOwner()) && item.getExpiration() == eItem.getExpiration() && item.getFlag() == eItem.getFlag() && (!type.equals(MapleInventoryType.CASH) || (type.equals(MapleInventoryType.CASH) && item.getUniqueId() == eItem.getUniqueId()))) {
                            final short newQ = (short)Math.min(oldQ + quantity, slotMax);
                            quantity -= (short)(newQ - oldQ);
                            eItem.setQuantity(newQ);
                            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventorySlot(type, eItem, !pet));
                        }
                    }
                }
                while (quantity > 0) {
                    final short newQ2 = (short)Math.min(quantity, slotMax);
                    quantity -= newQ2;
                    final Item nItem = new Item(item.getItemId(), (short)0, newQ2, item.getFlag());
                    nItem.setExpiration(item.getExpiration());
                    nItem.setOwner(item.getOwner());
                    nItem.setPet(item.getPet());
                    nItem.setGMLog(item.getGMLog());
                    nItem.setReward(item.getReward());
                    final short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                    if (ii.isCash(nItem.getItemId())) {
                        nItem.setUniqueId(item.getUniqueId());
                    }
                    if (newSlot == -1) {
                        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                        item.setQuantity((short)(quantity + newQ2));
                        return false;
                    }
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nItem, !pet));
                }
            }
            else {
                final Item nItem2 = new Item(item.getItemId(), (short)0, quantity, item.getFlag());
                nItem2.setExpiration(item.getExpiration());
                nItem2.setOwner(item.getOwner());
                nItem2.setPet(item.getPet());
                nItem2.setGMLog(item.getGMLog());
                nItem2.setReward(item.getReward());
                final short newSlot2 = c.getPlayer().getInventory(type).addItem(nItem2);
                if (newSlot2 == -1) {
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                    return false;
                }
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, nItem2, !pet));
            }
        }
        else {
            if (quantity != 1) {
                throw new RuntimeException("Trying to create equip with non-one quantity");
            }
            if (enhance) {
                item = checkEnhanced(item, c.getPlayer());
            }
            final Equip equip = (Equip)item;
            if (equip.getItemId() == 1672082) {
                equip.setPotential1(60011);
                equip.setPotential2(60010);
            }
            final short newSlot3 = c.getPlayer().getInventory(type).addItem(item);
            if (newSlot3 == -1) {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                return false;
            }
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.addInventorySlot(type, item, !pet));
            if (GameConstants.isHarvesting(item.getItemId())) {
                c.getPlayer().getStat().handleProfessionTool(c.getPlayer());
            }
        }
        if (item.getQuantity() < 50 || item.getItemId() == 2340000) {}
        if (before == 0) {
            switch (item.getItemId()) {
                case 4001128: {
                    c.getPlayer().dropMessage(5, "You have gained a Powder Keg, you can give this in to Aramia of Henesys.");
                    break;
                }
                case 4001246: {
                    c.getPlayer().dropMessage(5, "You have gained a Warm Sun, you can give this in to Maple Tree Hill through @joyce.");
                    break;
                }
                case 4001473: {
                    c.getPlayer().dropMessage(5, "You have gained a Tree Decoration, you can give this in to White Christmas Hill through @joyce.");
                    break;
                }
            }
        }
        c.getPlayer().havePartyQuest(item.getItemId());
        if (show) {
            c.getSession().writeAndFlush((Object)CWvsContext.InfoPacket.getShowItemGain(item.getItemId(), item.getQuantity(), false));
        }
        if (item.getItemId() == 4001886) {
            c.getSession().writeAndFlush((Object)CWvsContext.setBossReward(c.getPlayer()));
        }
        return true;
    }
    
    private static Item checkEnhanced(final Item before, final MapleCharacter chr) {
        if (before instanceof Equip) {
            final Equip eq = (Equip)before;
            if (eq.getState() == 0 && (eq.getUpgradeSlots() >= 1 || eq.getLevel() >= 1) && GameConstants.canScroll(eq.getItemId()) && Randomizer.nextInt(100) >= 80) {
                eq.resetPotential();
            }
        }
        return before;
    }
    
    public static boolean checkSpace(final MapleClient c, final int itemid, int quantity, final String owner) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (c.getPlayer() == null || (ii.isPickupRestricted(itemid) && c.getPlayer().haveItem(itemid, 1, true, false)) || !ii.itemExists(itemid)) {
            return false;
        }
        if (quantity <= 0 && !GameConstants.isRechargable(itemid)) {
            return false;
        }
        final MapleInventoryType type = GameConstants.getInventoryType(itemid);
        if (c == null || c.getPlayer() == null || c.getPlayer().getInventory(type) == null) {
            return false;
        }
        if (!type.equals(MapleInventoryType.EQUIP) && !type.equals(MapleInventoryType.CODY)) {
            final short slotMax = ii.getSlotMax(itemid);
            final List<Item> existing = c.getPlayer().getInventory(type).listById(itemid);
            if (!GameConstants.isRechargable(itemid) && existing.size() > 0) {
                for (final Item eItem : existing) {
                    final short oldQ = eItem.getQuantity();
                    if (oldQ < slotMax && owner != null && owner.equals(eItem.getOwner())) {
                        final short newQ = (short)Math.min(oldQ + quantity, slotMax);
                        quantity -= newQ - oldQ;
                    }
                    if (quantity <= 0) {
                        break;
                    }
                }
            }
            int numSlotsNeeded;
            if (slotMax > 0 && !GameConstants.isRechargable(itemid)) {
                numSlotsNeeded = (int)Math.ceil(quantity / (double)slotMax);
            }
            else {
                numSlotsNeeded = 1;
            }
            return !c.getPlayer().getInventory(type).isFull(numSlotsNeeded - 1);
        }
        return !c.getPlayer().getInventory(type).isFull();
    }
    
    public static boolean removeFromSlot(final MapleClient c, final MapleInventoryType type, final short slot, final short quantity, final boolean fromDrop) {
        return removeFromSlot(c, type, slot, quantity, fromDrop, false);
    }
    
    public static boolean removeFromSlot(final MapleClient c, final MapleInventoryType type, final short slot, final short quantity, final boolean fromDrop, final boolean consume) {
        if (c.getPlayer() == null || c.getPlayer().getInventory(type) == null) {
            return false;
        }
        final Item item = c.getPlayer().getInventory(type).getItem(slot);
        if (item != null) {
            final boolean allowZero = consume && GameConstants.isRechargable(item.getItemId());
            c.getPlayer().getInventory(type).removeItem(slot, quantity, allowZero);
            
            
            if (GameConstants.isHarvesting(item.getItemId())) {
                c.getPlayer().getStat().handleProfessionTool(c.getPlayer());
            }
            if (item.getQuantity() == 0 && !allowZero) {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.clearInventoryItem(type, item.getPosition(), fromDrop));
            }
            else {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventorySlot(type, item, fromDrop));
            }
            if (item.getItemId() == 4001886) {
                c.getSession().writeAndFlush((Object)CWvsContext.setBossReward(c.getPlayer()));
            }
            return true;
        }
        return false;
    }
    
    public static boolean removeById(final MapleClient c, final MapleInventoryType type, final int itemId, final int quantity, final boolean fromDrop, final boolean consume) {
        int remremove = quantity;
        if (c.getPlayer() == null || c.getPlayer().getInventory(type) == null) {
            return false;
        }
        for (final Item item : c.getPlayer().getInventory(type).listById(itemId)) {
            final int theQ = item.getQuantity();
            if (remremove <= theQ && removeFromSlot(c, type, item.getPosition(), (short)remremove, fromDrop, consume)) {
                remremove = 0;
                break;
            }
            if (remremove > theQ && removeFromSlot(c, type, item.getPosition(), item.getQuantity(), fromDrop, consume)) {
                remremove -= theQ;
            }
            if (item.getItemId() != 4001886) {
                continue;
            }
            c.getSession().writeAndFlush((Object)CWvsContext.setBossReward(c.getPlayer()));
        }

        
        return remremove <= 0;
    }
    
    public static boolean removeFromSlot_Lock(final MapleClient c, final MapleInventoryType type, final short slot, final short quantity, final boolean fromDrop, final boolean consume) {
        if (c.getPlayer() == null || c.getPlayer().getInventory(type) == null) {
            return false;
        }
        final Item item = c.getPlayer().getInventory(type).getItem(slot);
        return item != null && !ItemFlag.LOCK.check(item.getFlag()) && !ItemFlag.UNTRADEABLE.check(item.getFlag()) && removeFromSlot(c, type, slot, quantity, fromDrop, consume);
    }
    
    public static boolean removeById_Lock(final MapleClient c, final MapleInventoryType type, final int itemId) {
        for (final Item item : c.getPlayer().getInventory(type).listById(itemId)) {
            if (removeFromSlot_Lock(c, type, item.getPosition(), (short)1, false, false)) {
                return true;
            }
        }
        return false;
    }
    
    public static void move(final MapleClient c, final MapleInventoryType type, final short src, final short dst) {
        if (src < 0 || dst < 0 || src == dst || type == MapleInventoryType.EQUIPPED) {
            return;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final Item source = c.getPlayer().getInventory(type).getItem(src);
        final Item initialTarget = c.getPlayer().getInventory(type).getItem(dst);
        if (source == null) {
            return;
        }
        short olddstQ = -1;
        if (initialTarget != null) {
            olddstQ = initialTarget.getQuantity();
        }
        final short oldsrcQ = source.getQuantity();
        final short slotMax = ii.getSlotMax(source.getItemId());
        c.getPlayer().getInventory(type).move(src, dst, slotMax);
        if (type != MapleInventoryType.EQUIP && type != MapleInventoryType.CASH && initialTarget != null && source.getItemId() == initialTarget.getItemId()) {
            if (olddstQ + oldsrcQ > slotMax || source.getExpiration() > 0L || initialTarget.getExpiration() > 0L) {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.moveAndCombineWithRestItem(source, initialTarget));
            }
            else {
                c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.moveAndCombineItem(source, initialTarget));
            }
        }
        else {
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.moveInventoryItem(type, src, dst, false, false));
        }
    }
    
    public static void equip(final MapleClient c, final short src, short dst, final MapleInventoryType type) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final MapleCharacter chr = c.getPlayer();
        if (chr == null) {
            return;
        }
        Equip source = (Equip)chr.getInventory(type).getItem(src);
        if (source == null || source.getDurability() == 0 || GameConstants.isHarvesting(source.getItemId())) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        final Map<String, Integer> stats = ii.getEquipStats(source.getItemId());
        if (stats == null) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (dst > -1200 && dst < -999 && !GameConstants.isEvanDragonItem(source.getItemId()) && !GameConstants.isMechanicItem(source.getItemId())) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (((dst <= -1200 && dst > -1300) || (dst >= -999 && dst < -99)) && !stats.containsKey("cash") && c.getPlayer().getAndroid() == null) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (dst <= -1300 && dst > -1400 && !GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (source.getItemId() != 1342069 && GameConstants.isWeapon(source.getItemId()) && !stats.containsKey("cash") && dst != -10 && dst != -11) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (dst == -18 && !GameConstants.isMountItemAvailable(source.getItemId(), c.getPlayer().getJob())) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (dst <= -100 && dst > -200 && !stats.containsKey("cash")) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (dst == -118 && source.getItemId() / 10000 != 190) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (dst == -55) {
            final MapleQuestStatus stat = c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122700));
            if (stat == null || stat.getCustomData() == null || Long.parseLong(stat.getCustomData()) < System.currentTimeMillis()) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
        }
        if (GameConstants.isKatara(source.getItemId()) || source.getItemId() / 10000 == 135) {
            if (source.getItemId() == 1342069) {
                dst = -110;
            }
            else {
                dst = -10;
            }
        }
        if (GameConstants.isEvanDragonItem(source.getItemId()) && (chr.getJob() < 2200 || chr.getJob() > 2218)) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (GameConstants.isMechanicItem(source.getItemId()) && (chr.getJob() < 3500 || chr.getJob() > 3512)) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (GameConstants.isArcaneSymbol(source.getItemId())) {
            boolean isequiped = false;
            for (short i = -1600; i >= -1605; --i) {
                if (chr.getInventory(MapleInventoryType.EQUIPPED).getItem(i) != null && chr.getInventory(MapleInventoryType.EQUIPPED).getItem(i).getItemId() == source.getItemId()) {
                    isequiped = true;
                }
            }
            if (isequiped) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
        }
        if (GameConstants.isAuthenticSymbol(source.getItemId())) {
            boolean isequiped = false;
            for (short i = -1700; i >= -1705; --i) {
                if (chr.getInventory(MapleInventoryType.EQUIPPED).getItem(i) != null && chr.getInventory(MapleInventoryType.EQUIPPED).getItem(i).getItemId() == source.getItemId()) {
                    isequiped = true;
                }
            }
            if (isequiped) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
        }
        if (source.getItemId() / 1000 == 1112) {
            for (final EquipAdditions.RingSet s : EquipAdditions.RingSet.values()) {
                if (s.id.contains(source.getItemId())) {
                    final List<Integer> theList = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).listIds();
                    for (final Integer j : s.id) {
                        if (theList.contains(j)) {
                            c.getPlayer().dropMessage(1, "You may not equip this item because you already have a " + StringUtil.makeEnumHumanReadable(s.name()) + " equipped.");
                            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                            return;
                        }
                    }
                }
            }
        }
        if (MapleItemInformationProvider.getInstance().getName(source.getItemId()).startsWith("\uc815\ub839\uc758")) {
            final KoreaCalendar kc = new KoreaCalendar();
            final String nowtime = kc.getYeal() % 100 + kc.getMonths() + kc.getDays() + kc.getHours() + kc.getMins() + kc.getMins();
            chr.setKeyValue(27040, "runnigtime", "1");
            chr.setKeyValue(27040, "firstequiptime", nowtime);
            chr.setKeyValue(27040, "firstequiptimemil", System.currentTimeMillis() + "");
            chr.setKeyValue(27040, "equipday", "" + kc.getDayt() + "");
            chr.setKeyValue(27040, "equipmonth", "" + kc.getMonth() + "");
            chr.updateInfoQuest(27039, source.getInventoryId() + "=" + nowtime + "|" + nowtime + "|0|0|0");
            chr.getClient().send(CWvsContext.SpritPandent(source.getPosition(), true, 0, 10, 0));
            chr.updateInfoQuest(27039, source.getInventoryId() + "=" + nowtime + "|" + nowtime + "|0|10|0");
        }
        switch (dst) {
            case -6: {
                final Item top = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short)(-5));
                if (top == null || !GameConstants.isOverall(top.getItemId())) {
                    break;
                }
                if (chr.getInventory(MapleInventoryType.EQUIP).isFull()) {
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                    return;
                }
                unequip(c, (short)(-5), chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot(), MapleInventoryType.EQUIP);
                break;
            }
            case -5: {
                final Item top = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short)(-5));
                final Item bottom = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short)(-6));
                if (top != null && GameConstants.isOverall(source.getItemId())) {
                    if (chr.getInventory(MapleInventoryType.EQUIP).isFull((bottom != null && GameConstants.isOverall(source.getItemId())) ? 1 : 0)) {
                        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (short)(-5), chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot(), MapleInventoryType.EQUIP);
                }
                if (bottom == null || !GameConstants.isOverall(source.getItemId())) {
                    break;
                }
                if (chr.getInventory(MapleInventoryType.EQUIP).isFull()) {
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getInventoryFull());
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.getShowInventoryFull());
                    return;
                }
                unequip(c, (short)(-6), chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot(), MapleInventoryType.EQUIP);
                break;
            }
        }
        source = (Equip)chr.getInventory(type).getItem(src);
        final Equip target = (Equip)chr.getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
        if (GameConstants.isZero(chr.getJob())) {
            chr.setSkillCustomInfo(10112, source.getItemId(), 0L);
        }
        if (source == null) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        List<Integer> potentials = new ArrayList<Integer>();
        potentials.add(source.getPotential1());
        potentials.add(source.getPotential2());
        potentials.add(source.getPotential3());
        potentials.add(source.getPotential4());
        potentials.add(source.getPotential5());
        potentials.add(source.getPotential6());
        for (final Integer potential : potentials) {
            int lv = ii.getReqLevel(source.getItemId()) / 10 - 1;
            if (lv < 0) {
                lv = 0;
            }
            if (potential != 0 && ii.getPotentialInfo(potential) != null) {
                if (ii.getPotentialInfo(potential).get(lv) == null) {
                    continue;
                }
                final int usefulSkill = ii.getPotentialInfo(potential).get(lv).skillID;
                if (usefulSkill <= 0) {
                    continue;
                }
                c.getPlayer().changeSkillLevel(GameConstants.getOrdinaryJobNumber(c.getPlayer().getJob()) * 10000 + usefulSkill, (byte)1, (byte)1);
            }
        }
        int flag = source.getFlag();
        if (stats.get("equipTradeBlock") != null || source.getItemId() / 10000 == 167 || source.getItemId() / 10000 == 166) {
            if ((source.getItemId() / 10000 == 167 || source.getItemId() / 10000 == 166) && ItemFlag.KARMA_EQUIP.check(flag)) {
                flag -= ItemFlag.KARMA_EQUIP.getValue();
                source.setFlag(flag);
            }
            if (!ItemFlag.UNTRADEABLE.check(flag)) {
                flag += ItemFlag.UNTRADEABLE.getValue();
                source.setFlag(flag);
            }
        }
        if (source.getItemId() / 10000 == 166) {
            final Item heart = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short)(-28));
            chr.removeAndroid();
            if (heart != null) {
                if (ItemFlag.ANDROID_ACTIVATED.check(flag)) {
                    if (source.getAndroid() == null) {
                        chr.dropMessage(1, "\uc548\ub4dc\ub85c\uc774\ub4dc \uc624\ub958\uac00 \ubc1c\uc0dd\ud558\uc600\uc2b5\ub2c8\ub2e4.");
                        return;
                    }
                    chr.setAndroid(source.getAndroid());
                }
                else {
                    final long uid = MapleInventoryIdentifier.getInstance();
                    source.setUniqueId(uid);
                    final MapleAndroid androids = MapleAndroid.create(source.getItemId(), uid);
                    source.setAndroid(androids);
                    flag += ItemFlag.ANDROID_ACTIVATED.getValue();
                    source.setFlag(flag);
                    chr.setAndroid(androids);
                }
            }
        }
        else if (source.getItemId() / 10000 == 167) {
            final Item android = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short)(-27));
            chr.removeAndroid();
            if (android != null) {
                final int androidflag = android.getFlag();
                if (ItemFlag.ANDROID_ACTIVATED.check(androidflag)) {
                    if (android.getAndroid() == null) {
                        chr.dropMessage(1, "\uc548\ub4dc\ub85c\uc774\ub4dc \uc624\ub958\uac00 \ubc1c\uc0dd\ud558\uc600\uc2b5\ub2c8\ub2e4.");
                        return;
                    }
                    chr.setAndroid(android.getAndroid());
                }
                else {
                    final long uid2 = MapleInventoryIdentifier.getInstance();
                    android.setUniqueId(uid2);
                    final MapleAndroid androids2 = MapleAndroid.create(android.getItemId(), uid2);
                    android.setAndroid(androids2);
                    flag += ItemFlag.ANDROID_ACTIVATED.getValue();
                    android.setFlag(flag);
                    c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, android));
                    chr.setAndroid(androids2);
                }
            }
        }
        if (source.getCharmEXP() > 0 && !ItemFlag.CHARM_EQUIPED.check(source.getFlag())) {
            chr.getTrait(MapleTrait.MapleTraitType.charm).addExp(source.getCharmEXP(), chr);
            source.setCharmEXP((short)0);
            source.setFlag(source.getFlag() + ItemFlag.CHARM_EQUIPED.getValue());
        }
        chr.getInventory(type).removeSlot(src);
        if (target != null) {
            if (MapleItemInformationProvider.getInstance().getName(target.getItemId()).startsWith("\uc815\ub839\uc758")) {
                c.getPlayer().removeKeyValue(27040);
                c.getPlayer().updateInfoQuest(27039, "0=0|0|0|0|0");
                c.getPlayer().getClient().send(CWvsContext.SpritPandent(target.getPosition(), false, 0, 0, 0));
            }
            potentials = new ArrayList<Integer>();
            potentials.add(target.getPotential1());
            potentials.add(target.getPotential2());
            potentials.add(target.getPotential3());
            potentials.add(target.getPotential4());
            potentials.add(target.getPotential5());
            potentials.add(target.getPotential6());
            for (final Integer potential2 : potentials) {
                int lv2 = ii.getReqLevel(source.getItemId()) / 10 - 1;
                if (lv2 < 0) {
                    lv2 = 0;
                }
                if (potential2 != 0 && ii.getPotentialInfo(potential2) != null) {
                    if (ii.getPotentialInfo(potential2).get(lv2) == null) {
                        continue;
                    }
                    final int usefulSkill2 = ii.getPotentialInfo(potential2).get(lv2).skillID;
                    if (usefulSkill2 <= 0) {
                        continue;
                    }
                    c.getPlayer().changeSkillLevel(GameConstants.getOrdinaryJobNumber(c.getPlayer().getJob()) * 10000 + usefulSkill2, (byte)(-1), (byte)0);
                }
            }
            chr.getInventory(MapleInventoryType.EQUIPPED).removeSlot(dst);
        }
        source.setPosition(dst);
        chr.getInventory(MapleInventoryType.EQUIPPED).addFromDB(source);
        if (target != null) {
            target.setPosition(src);
            chr.getInventory(type).addFromDB(target);
        }
        if (GameConstants.isWeapon(source.getItemId()) && source.getItemId() != 1342069) {
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.Booster);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.NoBulletConsume);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.SoulArrow);
        }
        if (source.getItemId() / 10000 == 190 || source.getItemId() / 10000 == 191) {
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.RideVehicle);
        }
        if (source.getItemId() >= 1113098 && source.getItemId() <= 1113128) {
            chr.changeSkillLevel(SkillFactory.getSkill(80001455 + (source.getItemId() - 1113098)), (byte)source.getBaseLevel(), (byte)4);
        }
        if (target != null && target.getItemId() >= 1113098 && target.getItemId() <= 1113128) {
            chr.changeSkillLevel(SkillFactory.getSkill(80001455 + (target.getItemId() - 1113098)), (byte)(-1), (byte)0);
        }
        if (source.getItemId() == 1112586) {
            MapleItemInformationProvider.getInstance().getItemEffect(2022747).applyTo(chr, false, 0);
        }
        else if (source.getItemId() == 1112663) {
            MapleItemInformationProvider.getInstance().getItemEffect(2022823).applyTo(chr, false, 0);
        }
        chr.setSoulMP(null);
        if (source != null) {
            chr.setSoulMP(source);
        }
        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.moveInventoryItem(type, src, dst, false, false));
        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventoryItem(false, type, source));
        if (target != null) {
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventoryItem(false, type, target));
        }
        if (dst <= -1200 && dst > -1300 && chr.getAndroid() != null) {
            chr.setAndroid(chr.getAndroid());
        }
        chr.equipChanged();
    }
    
    public static void unequip(final MapleClient c, short src, final short dst, final MapleInventoryType type) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final Equip source = (Equip)c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(src);
        final Equip target = (Equip)c.getPlayer().getInventory(type).getItem(dst);
        if (source.getFinalStrike() && GameConstants.getInventoryType(source.getItemId()) == MapleInventoryType.CODY) {
            c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot(src);
            c.send(CWvsContext.InventoryPacket.UpedateInventoryItem(dst, 0));
            c.send(CWvsContext.InventoryPacket.UpedateInventoryItem(0, src));
            c.getPlayer().equipChanged();
            return;
        }
        if (MapleItemInformationProvider.getInstance().getName(source.getItemId()).startsWith("\uc815\ub839\uc758")) {
            c.getPlayer().removeKeyValue(27040);
            c.getPlayer().updateInfoQuest(27039, "0=0|0|0|0|0");
            c.getPlayer().getClient().send(CWvsContext.SpritPandent(source.getPosition(), false, 0, 0, 0));
        }
        if (target != null) {
            if (src > -1200 && src < -999 && !GameConstants.isEvanDragonItem(target.getItemId()) && !GameConstants.isMechanicItem(target.getItemId())) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (src <= -1300 && src > -1400 && !GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (target.getItemId() != 1342069 && GameConstants.isWeapon(target.getItemId()) && src != -10 && src != -11) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (src == -18 && !GameConstants.isMountItemAvailable(target.getItemId(), c.getPlayer().getJob())) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (src <= -100 && !MapleItemInformationProvider.getInstance().isCash(target.getItemId())) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if ((src > -100 || src <= -200) && MapleItemInformationProvider.getInstance().isCash(target.getItemId())) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (src == -118 && target.getItemId() / 10000 != 190) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (src == -55) {
                final MapleQuestStatus stat = c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122700));
                if (stat == null || stat.getCustomData() == null || Long.parseLong(stat.getCustomData()) < System.currentTimeMillis()) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
            }
            if (GameConstants.isKatara(target.getItemId()) || target.getItemId() / 10000 == 135) {
                if (target.getItemId() == 1342069) {
                    src = -110;
                }
                else {
                    src = -10;
                }
            }
            if (GameConstants.isEvanDragonItem(target.getItemId()) && (c.getPlayer().getJob() < 2200 || c.getPlayer().getJob() > 2218)) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (GameConstants.isMechanicItem(target.getItemId()) && (c.getPlayer().getJob() < 3500 || c.getPlayer().getJob() > 3512)) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (ii.isOnlyEquip(target.getItemId()) && c.getPlayer().hasEquipped(target.getItemId())) {
                c.getPlayer().dropMessage(1, "\uace0\uc720\uc7a5\ucc29 \uc544\uc774\ud15c\uc740 1\uac1c\ub9cc \ucc29\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.");
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            int flag = target.getFlag();
            final Map<String, Integer> stats = ii.getEquipStats(target.getItemId());
            if ((stats.get("equipTradeBlock") != null || source.getItemId() / 10000 == 167 || source.getItemId() / 10000 == 166) && !ItemFlag.UNTRADEABLE.check(flag)) {
                flag += ItemFlag.UNTRADEABLE.getValue();
                target.setFlag(flag);
            }
        }
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot(src);
        if (target != null) {
            c.getPlayer().getInventory(type).removeSlot(dst);
        }
        source.setPosition(dst);
        c.getPlayer().getInventory(type).addFromDB(source);
        if (target != null) {
            target.setPosition(src);
            c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).addFromDB(target);
        }
        if (GameConstants.isWeapon(source.getItemId())) {
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.Booster);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.NoBulletConsume);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.SoulArrow);
        }
        else if (source.getItemId() / 10000 == 190 || source.getItemId() / 10000 == 191) {
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.RideVehicle);
        }
        else if (source.getItemId() / 10000 == 166) {
            c.getPlayer().removeAndroid();
        }
        else if (source.getItemId() / 10000 == 167) {
            c.getPlayer().removeAndroid();
        }
        else if (src <= -1200 && src > -1300 && c.getPlayer().getAndroid() != null) {
            c.getPlayer().setAndroid(c.getPlayer().getAndroid());
        }
        else if (source.getItemId() == 1112585) {
            c.getPlayer().dispelSkill(2022746);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.RepeatEffect);
        }
        else if (source.getItemId() == 1112586) {
            c.getPlayer().dispelSkill(2022747);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.RepeatEffect);
        }
        else if (source.getItemId() == 1112594) {
            c.getPlayer().dispelSkill(2022764);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.RepeatEffect);
        }
        else if (source.getItemId() == 1112663) {
            c.getPlayer().dispelSkill(2022823);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.RepeatEffect);
        }
        else if (source.getItemId() == 1112735) {
            c.getPlayer().dispelSkill(2022823);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.RepeatEffect);
        }
        c.getPlayer().setSoulMP(null);
        final List<Integer> potentials = new ArrayList<Integer>();
        potentials.add(source.getPotential1());
        potentials.add(source.getPotential2());
        potentials.add(source.getPotential3());
        potentials.add(source.getPotential4());
        potentials.add(source.getPotential5());
        potentials.add(source.getPotential6());
        for (final Integer potential : potentials) {
            int lv = ii.getReqLevel(source.getItemId()) / 10 - 1;
            if (lv < 0) {
                lv = 0;
            }
            if (potential != 0 && ii.getPotentialInfo(potential) != null) {
                if (ii.getPotentialInfo(potential).get(lv) == null) {
                    continue;
                }
                final int usefulSkill = ii.getPotentialInfo(potential).get(lv).skillID;
                if (usefulSkill <= 0) {
                    continue;
                }
                c.getPlayer().changeSkillLevel(GameConstants.getOrdinaryJobNumber(c.getPlayer().getJob()) * 10000 + usefulSkill, (byte)(-1), (byte)0);
            }
        }
        if (target != null && ItemFlag.KARMA_EQUIP.check(target.getFlag()) && !ItemFlag.UNTRADEABLE.check(target.getFlag())) {
            target.setFlag(target.getFlag() - ItemFlag.KARMA_EQUIP.getValue() + ItemFlag.UNTRADEABLE.getValue());
        }
        if (source.getItemId() >= 1113098 && source.getItemId() <= 1113128) {
            c.getPlayer().changeSkillLevel(SkillFactory.getSkill(80001455 + (source.getItemId() - 1113098)), (byte)(-1), (byte)0);
        }
        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.moveInventoryItem(type, src, dst, false, false));
        c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventoryItem(false, type, source));
        if (target != null) {
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventoryItem(false, type, target));
        }
        c.getPlayer().equipChanged();
    }
    
    public static boolean drop(final MapleClient c, final MapleInventoryType type, final short src, final short quantity) {
        return drop(c, type, src, quantity, false);
    }
    
    public static boolean drop(final MapleClient c, MapleInventoryType type, final short src, final short quantity, final boolean npcInduced) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (src < 0) {
            type = MapleInventoryType.EQUIPPED;
        }
        if (c.getPlayer().getMapId() == ServerConstants.warpMap) {
            c.getPlayer().dropMessage(1, "\ub9c8\uc744\uc5d0\uc120 \uc544\uc774\ud15c \ub4dc\ub86d\uc744 \uc774\uc6a9 \ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
            c.send(CWvsContext.enableActions(c.getPlayer()));
            return false;
        }
        if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
            return false;
        }
        final Item source = c.getPlayer().getInventory(type).getItem(src);
        if (quantity < 0 || source == null || (!npcInduced && GameConstants.isPet(source.getItemId())) || (quantity == 0 && !GameConstants.isRechargable(source.getItemId())) || c.getPlayer().inPVP()) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return false;
        }
        final int flag = source.getFlag();
        if (quantity > source.getQuantity() && !GameConstants.isRechargable(source.getItemId())) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return false;
        }
        if (ItemFlag.LOCK.check(flag) || (quantity != 1 && type == MapleInventoryType.EQUIP)) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return false;
        }
        if (type == MapleInventoryType.EQUIP) {
            final Equip equip = (Equip)source;
            if ((equip.getEnchantBuff() & 0x88) != 0x0) {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                c.getPlayer().dropMessage(1, "장비의 흔적은 버릴 수 없습니다.");
                return false;
            }
        }
        final Point dropPos = new Point(c.getPlayer().getPosition());
        FileoutputUtil.log(FileoutputUtil.아이템드롭로그, "[드롭] 계정번호 : " + c.getAccID() + " | " + c.getPlayer().getName() + "이 " + c.getPlayer().getMapId() + " 에서 " + ii.getName(source.getItemId()) + "(" + source.getItemId() + ")를 " + source.getQuantity() + "개를 드롭");
        if (quantity < source.getQuantity() && !GameConstants.isRechargable(source.getItemId())) {
            final Item target = source.copy();
            target.setQuantity(quantity);
            source.setQuantity((short)(source.getQuantity() - quantity));
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.updateInventorySlot(type, source, true));
            if (ii.isDropRestricted(target.getItemId()) || ii.isAccountShared(target.getItemId())) {
                if (ItemFlag.KARMA_EQUIP.check(target.getFlag())) {
                    target.setFlag(target.getFlag() - ItemFlag.KARMA_EQUIP.getValue());
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                }
                else if (ItemFlag.KARMA_USE.check(target.getFlag())) {
                    target.setFlag(target.getFlag() - ItemFlag.KARMA_USE.getValue());
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                }
                else {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                }
            }
            else if (GameConstants.isPet(target.getItemId()) || ItemFlag.UNTRADEABLE.check(target.getFlag())) {
                if (ItemFlag.KARMA_EQUIP.check(target.getFlag())) {
                    target.setFlag(target.getFlag() - ItemFlag.KARMA_EQUIP.getValue());
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                }
                else if (ItemFlag.KARMA_USE.check(target.getFlag())) {
                    target.setFlag(target.getFlag() - ItemFlag.KARMA_USE.getValue());
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                }
                else {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                }
            }
            else {
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
            }
        }
        else {
            c.getPlayer().getInventory(type).removeSlot(src);
            if (GameConstants.isHarvesting(source.getItemId())) {
                c.getPlayer().getStat().handleProfessionTool(c.getPlayer());
            }
            c.getSession().writeAndFlush((Object)CWvsContext.InventoryPacket.clearInventoryItem(type, src, true));
            if (src < 0) {
                c.getPlayer().equipChanged();
            }
            if (ii.isDropRestricted(source.getItemId()) || ii.isAccountShared(source.getItemId())) {
                if (ItemFlag.KARMA_EQUIP.check(flag)) {
                    source.setFlag(flag - ItemFlag.KARMA_EQUIP.getValue());
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                }
                else if (ItemFlag.KARMA_USE.check(flag)) {
                    source.setFlag(flag - ItemFlag.KARMA_USE.getValue());
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                }
                else {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                }
            }
            else if (GameConstants.isPet(source.getItemId()) || ItemFlag.UNTRADEABLE.check(flag)) {
                if (ItemFlag.KARMA_EQUIP.check(flag)) {
                    source.setFlag(flag - ItemFlag.KARMA_EQUIP.getValue());
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                }
                else if (ItemFlag.KARMA_USE.check(flag)) {
                    source.setFlag(flag - ItemFlag.KARMA_USE.getValue());
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                }
                else {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                }
            }
            else {
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
            }
        }
        return true;
    }
}
