package handling.channel.handler;

import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataTool;
import provider.MapleDataProviderFactory;
import java.io.File;
import java.awt.Rectangle;
import server.life.MobSkill;
import server.SecondaryStatEffect;
import server.maps.MapleMapObject;
import server.maps.BossReward;
import handling.world.MaplePartyCharacter;
import server.life.MobSkillFactory;
import tools.packet.SLFCGPacket;
import tools.packet.BattleGroundPacket;
import server.maps.MapleMapItem;
import server.maps.MapleMapObjectType;
import server.MapleDueyActions;
import server.shops.MapleShopFactory;
import tools.packet.PetPacket;
import log.LogType;
import log.DBLogger;
import tools.packet.CSPacket;
import java.util.Calendar;
import constants.EdiCubeOption;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MaplePet;
import tools.Triple;
import constants.ServerConstants;
import server.InnerAbillity;
import client.InnerSkillValueHolder;
import client.SkillEntry;
import java.util.HashMap;
import java.util.EnumMap;
import server.life.MapleLifeFactory;
import client.PlayerStats;
import client.MapleQuestStatus;
import server.quest.MapleQuest;
import client.MapleStat;
import server.maps.SavedLocationType;
import server.Timer;
import client.inventory.MapleMount;
import server.maps.MapleMap;
import server.life.MapleMonster;
import tools.packet.MobPacket;
import client.Skill;
import java.util.Map;
import client.SkillFactory;
import server.enchant.StarForceStats;
import server.enchant.EquipmentEnchant;
import handling.RecvPacketOpcode;
import client.inventory.ItemFlag;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import database.DatabaseConnection;
import client.MapleTrait;
import constants.CubeOption;
import server.maps.FieldLimitType;
import client.SecondaryStat;
import scripting.NPCScriptManager;
import server.RandomRewards;
import handling.world.World;
import java.awt.Point;
import client.inventory.Equip;
import tools.packet.CField;
import tools.FileoutputUtil;
import server.Randomizer;
import server.StructRewardItem;
import server.MapleItemInformationProvider;
import constants.GameConstants;
import client.MapleCharacter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import client.inventory.MapleInventory;
import tools.Pair;
import java.util.ArrayList;
import client.inventory.Item;
import java.util.LinkedList;
import tools.packet.CWvsContext;
import server.MapleInventoryManipulator;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import scripting.EventManager;
import tools.data.LittleEndianAccessor;
import tools.packet.BossRewardMeso;

public class InventoryHandler {

    public static final void ItemMove(final LittleEndianAccessor slea, final MapleClient c) {
        try {
            c.getPlayer().setScrolledPosition((short) 0);
            slea.readInt();
            final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
            final short src = slea.readShort();
            final short dst = slea.readShort();
            final short quantity = slea.readShort();
            c.getPlayer().dropMessageGM(5, src + " / " + dst);
            if (src < 0 && dst > 0) {
                MapleInventoryManipulator.unequip(c, src, dst, type);
            } else if (dst < 0) {
                MapleInventoryManipulator.equip(c, src, dst, type);
            } else if (dst == 0) {
                MapleInventoryManipulator.drop(c, type, src, quantity);
            } else {
                MapleInventoryManipulator.move(c, type, src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final void SwitchBag(final LittleEndianAccessor slea, final MapleClient c) {
        c.getPlayer().setScrolledPosition((short) 0);
        slea.readInt();
        final short src = (short) slea.readInt();
        final short dst = (short) slea.readInt();
        if (src < 100 || dst < 100) {
            return;
        }
        MapleInventoryManipulator.move(c, MapleInventoryType.ETC, src, dst);
    }

    public static final void MoveBag(final LittleEndianAccessor slea, final MapleClient c) {
        c.getPlayer().setScrolledPosition((short) 0);
        slea.readInt();
        final boolean srcFirst = slea.readInt() > 0;
        if (slea.readByte() != 4) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        final short dst = (short) slea.readInt();
        final short src = slea.readShort();
        MapleInventoryManipulator.move(c, MapleInventoryType.ETC, srcFirst ? dst : src, srcFirst ? src : dst);
    }

    public static final void ItemSort(final LittleEndianAccessor slea, final MapleClient c) {
        slea.readInt();
        c.getPlayer().setScrolledPosition((short) 0);
        final MapleInventoryType pInvType = MapleInventoryType.getByType(slea.readByte());
        if (pInvType == MapleInventoryType.UNDEFINED) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        final MapleInventory pInv = c.getPlayer().getInventory(pInvType);
        final List<Item> itemMap = new LinkedList<Item>();
        for (final Item item : pInv.list()) {
            itemMap.add(item.copy());
        }
        final List<Pair<Short, Short>> updateSlots = new ArrayList<Pair<Short, Short>>();
        for (int i = 1; i <= pInv.getSlotLimit(); ++i) {
            final Item item2 = pInv.getItem((short) i);
            if (item2 == null) {
                final Item nextItem = pInv.getItem(pInv.getNextItemSlot((short) i));
                if (nextItem != null) {
                    final short oldPos = nextItem.getPosition();
                    pInv.removeItem(nextItem.getPosition());
                    final short nextPos = pInv.addItem(nextItem);
                    updateSlots.add(new Pair<Short, Short>(oldPos, nextPos));
                }
            }
        }
        c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.moveInventoryItem(pInvType, updateSlots));
        c.getSession().writeAndFlush((Object) CWvsContext.finishedSort(pInvType.getType()));
    }

    public static final void ItemGather(final LittleEndianAccessor slea, final MapleClient c) {
        slea.readInt();
        c.getPlayer().setScrolledPosition((short) 0);
        final byte mode = slea.readByte();
        final MapleInventoryType invType = MapleInventoryType.getByType(mode);
        final MapleInventory inv = c.getPlayer().getInventory(invType);
        if (mode > MapleInventoryType.UNDEFINED.getType()) {
            final List<Item> itemMap = new LinkedList<Item>();
            for (final Item item : inv.list()) {
                itemMap.add(item.copy());
            }
            for (final Item itemStats : itemMap) {
                MapleInventoryManipulator.removeFromSlot(c, invType, itemStats.getPosition(), itemStats.getQuantity(), true, false);
            }
            final List<Item> sortedItems = sortItems(itemMap);
            for (final Item item2 : sortedItems) {
                MapleInventoryManipulator.addFromDrop(c, item2, false, false, false, true);
            }
        }
        c.getSession().writeAndFlush((Object) CWvsContext.finishedGather(mode));
    }

    private static final List<Item> sortItems(final List<Item> passedMap) {
        final List<Integer> itemIds = new ArrayList<Integer>();
        for (final Item item : passedMap) {
            itemIds.add(item.getItemId());
        }
        Collections.sort(itemIds);
        final List<Item> sortedList = new LinkedList<Item>();
        for (final Integer val : itemIds) {
            for (final Item item2 : passedMap) {
                if (val == item2.getItemId()) {
                    sortedList.add(item2);
                    passedMap.remove(item2);
                    break;
                }
            }
        }
        return sortedList;
    }

    public static final boolean UseRewardItem(final short slot, final int itemId, final MapleClient c, final MapleCharacter chr) {
        final Item toUse = c.getPlayer().getInventory(GameConstants.getInventoryType(itemId)).getItem(slot);
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
        if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId) {
            if (chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot() > -1 && chr.getInventory(MapleInventoryType.USE).getNextFreeSlot() > -1 && chr.getInventory(MapleInventoryType.SETUP).getNextFreeSlot() > -1 && chr.getInventory(MapleInventoryType.ETC).getNextFreeSlot() > -1) {
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                final Pair<Integer, List<StructRewardItem>> rewards = ii.getRewardItem(itemId);
                if (rewards != null && rewards.getLeft() > 0) {
                    StructRewardItem reward = null;
                    Block_11:
                    while (true) {
                        final Iterator<StructRewardItem> iterator = rewards.getRight().iterator();
                        while (iterator.hasNext()) {
                            reward = iterator.next();
                            if (reward.prob > 0 && Randomizer.nextInt(rewards.getLeft()) < reward.prob) {
                                break Block_11;
                            }
                        }
                    }
                    if (GameConstants.getInventoryType(reward.itemid) == MapleInventoryType.EQUIP) {
                        final Item item = ii.getEquipById(reward.itemid);
                        if (reward.period > 0L) {
                            item.setExpiration(System.currentTimeMillis() + reward.period * 60L * 60L * 10L);
                        }
                        item.setGMLog("Reward item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
                        MapleInventoryManipulator.addbyItem(c, item);
                    } else {
                        MapleInventoryManipulator.addById(c, reward.itemid, reward.quantity, "Reward item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
                    }
                    MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemId), itemId, 1, false, false);
                    c.getSession().writeAndFlush((Object) CField.EffectPacket.showRewardItemEffect(chr, reward.itemid, true, reward.effect));
                    chr.getMap().broadcastMessage(chr, CField.EffectPacket.showRewardItemEffect(chr, reward.itemid, false, reward.effect), false);
                    return true;
                }
                if (itemId == 2028154) {
                    final int reward2 = 1113097 + Randomizer.rand(1, 31);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.getByType((byte) (toUse.getItemId() / 1000000)), slot, (short) 1, false);
                    final Equip item2 = (Equip) ii.getEquipById(reward2);
                    MapleInventoryManipulator.addbyItem(c, item2);
                    c.getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(c.getPlayer(), reward2, 0, 38, 1, 0, (byte) 0, true, null, "", null));
                    c.getSession().writeAndFlush((Object) CField.EffectPacket.showRewardItemEffect(c.getPlayer(), itemId, true, ""));
                    if (item2.getBaseLevel() >= 4 && (reward2 == 1113098 || reward2 == 1113099 || (reward2 >= 1113113 && reward2 <= 1113116) || reward2 == 1113122)) {
                        World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, c.getChannel(), c.getPlayer().getName(), c.getPlayer().getName() + "님이 상자에서 [" + ii.getName(reward2) + "] 아이템을 획득했습니다.", true, item2));
                    }
                    c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                } else if (itemId == 2028272) {
                    final int reward2 = RandomRewards.getTheSeedReward();
                    if (reward2 == 0) {
                        c.getSession().writeAndFlush((Object) CField.NPCPacket.getNPCTalk(9000155, (byte) 0, "아쉽지만, 꽝이 나왔습니다. 다음 기회에 다시 이용해주세요!", "00 00", (byte) 0));
                    } else if (reward2 == 1) {
                        chr.gainMeso(10000000L, true);
                        c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(9000155, (byte) 0, "1천만메소를 획득하셨습니다!", "00 00", (byte) 0));
                    } else {
                        int max_quantity = 1;
                        switch (reward2) {
                            case 4310034: {
                                max_quantity = 10;
                                break;
                            }
                            case 4310014: {
                                max_quantity = 10;
                                break;
                            }
                            case 4310016: {
                                max_quantity = 10;
                                break;
                            }
                            case 4001208:
                            case 4001209:
                            case 4001210:
                            case 4001211:
                            case 4001547:
                            case 4001548:
                            case 4001549:
                            case 4001550:
                            case 4001551: {
                                max_quantity = 1;
                                break;
                            }
                        }
                        c.getSession().writeAndFlush((Object) CField.NPCPacket.getNPCTalk(9000155, (byte) 0, "축하드립니다!!\r\n돌림판에서 [#b#i" + reward2 + "##z" + reward2 + "#](이)가 나왔습니다.", "00 00", (byte) 0));
                        c.getPlayer().gainItem(reward2, max_quantity);
                        c.getSession().writeAndFlush((Object) CField.EffectPacket.showCharmEffect(c.getPlayer(), reward2, 1, true, ""));
                        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                    }
                } else if (itemId == 2028208 || itemId == 2028209) {
                    NPCScriptManager.getInstance().startItem(c, 9000162, "consume_" + itemId);
                } else {
                    chr.dropMessage(6, "아이템 보상 정보를 찾을 수 없습니다.");
                }
            } else {
                chr.dropMessage(6, "Insufficient inventory slot.");
            }
        }
        return false;
    }

    public static final void UseItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || !chr.isAlive() || chr.getBuffedEffect(SecondaryStat.DebuffIncHp) != null || chr.getMap() == null || chr.hasDisease(SecondaryStat.StopPortion) || chr.getBuffedValue(SecondaryStat.StopPortion) != null) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        try {
            final long time = System.currentTimeMillis();
            slea.skip(4);
            final short slot = slea.readShort();
            final int itemId = slea.readInt();
            final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
            if (toUse.getItemId() != itemId) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit())) {
                if (MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyTo(chr, true)) {
                    if (toUse.getItemId() != 2000054) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    }
                    c.getSession().writeAndFlush((Object) CField.potionCooldown());
                }
            } else {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final void UseReturnScroll(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        if (!chr.isAlive() || chr.getMapId() == 749040100 || chr.inPVP()) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        slea.readInt();
        short slot = slea.readShort();
        int itemId = slea.readInt();
        Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        FileoutputUtil.log(FileoutputUtil.아이템사용로그, "[주문서 사용] 계정 아이디 : " + c.getAccID() + " | " + c.getPlayer().getName() + "이 " + MapleItemInformationProvider.getInstance().getName(toUse.getItemId()) + "를 " + MapleItemInformationProvider.getInstance().getName(itemId) + "에 사용함.");
        if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit())) {
            if (MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyReturnScroll(chr)) {
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            } else {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            }
        } else {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
        }
    }

    public static void UseMagnify(final LittleEndianAccessor slea, final MapleClient c) {
        try {
            slea.skip(4);
            boolean useGlass = false;
            boolean isEquipped = false;
            final short useSlot = slea.readShort();
            final short equSlot = slea.readShort();
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            Equip equip;
            if (equSlot < 0) {
                equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equSlot);
                isEquipped = true;
            } else {
                equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(equSlot);
            }
            final Item glass = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(useSlot);
            if (useSlot != 20000) {
                if (glass == null || equip == null) {
                    c.getPlayer().dropMessage(1, "GLASS NULL!");
                    c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryFull());
                    return;
                }
                useGlass = true;
            } else {
                final long price = GameConstants.getMagnifyPrice(equip);
                c.getPlayer().gainMeso(-price, false);
            }
            if (equip.getState() == 1) {
                final int rank = Randomizer.nextInt(100);
                if (equip.getLines() == 0) {
                    equip.setLines((byte) 2);
                }
                if (rank < 3) {
                    equip.setState((byte) 18);
                } else if (rank < 1) {
                    equip.setState((byte) 19);
                } else {
                    equip.setState((byte) 17);
                }
            } else {
                equip.setState((byte) (equip.getState() + 16));
            }
            final int level = equip.getState() - 16;
            equip.setPotential1(CubeOption.getRedCubePotentialId(equip.getItemId(), level, 1, new int[0]));
            equip.setPotential2(CubeOption.getRedCubePotentialId(equip.getItemId(), level, 2, new int[0]));
            if (equip.getLines() == 3) {
                equip.setPotential3(CubeOption.getRedCubePotentialId(equip.getItemId(), level, 3, equip.getPotential1(), equip.getPotential2()));
            }
            if (GameConstants.isZero(c.getPlayer().getJob())) {
                if (equSlot == -10) {
                    final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
                    if (eq2 != null) {
                        eq2.setState(equip.getState());
                        eq2.setLines(equip.getLines());
                        eq2.setPotential1(equip.getPotential1());
                        eq2.setPotential2(equip.getPotential2());
                        eq2.setPotential3(equip.getPotential3());
                        c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
                    }
                } else if (equSlot == -11) {
                    final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
                    if (eq2 != null) {
                        eq2.setState(equip.getState());
                        eq2.setLines(equip.getLines());
                        eq2.setPotential1(equip.getPotential1());
                        eq2.setPotential2(equip.getPotential2());
                        eq2.setPotential3(equip.getPotential3());
                        c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
                    }
                }
            }
            if (useGlass) {
                final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
                useInventory.removeItem(useSlot, (short) 1, false);
            }
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, equip));
            c.getPlayer().getTrait(MapleTrait.MapleTraitType.insight).addExp(10, c.getPlayer());
            c.getPlayer().getMap().broadcastMessage(CField.showMagnifyingEffect(c.getPlayer().getId(), equSlot));
            if (isEquipped) {
                c.getPlayer().forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIPPED);
            } else {
                c.getPlayer().forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIP);
            }
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int potential(final int itemid, final int level) {
        return potential(itemid, level, false);
    }

    public static int potential(final int itemid, final int level, final boolean editional) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final int itemtype = itemid / 1000;
        return ii.getPotentialOptionID(Math.max(1, level), editional, itemtype);
    }

    public static void UseStamp(final LittleEndianAccessor slea, final MapleClient c) {
        slea.skip(4);
        final short slot = slea.readShort();
        final short dst = slea.readShort();
        boolean sucstamp = false;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip toStamp;
        if (dst < 0) {
            toStamp = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
        } else {
            toStamp = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
        }
        final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
        final Item stamp = useInventory.getItem(slot);
        if (GameConstants.isZero(c.getPlayer().getJob())) {
            final Equip toStamp2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
            if (Randomizer.isSuccess(ii.getSuccess(toStamp2.getItemId(), c.getPlayer(), toStamp2))) {
                toStamp2.setLines((byte) 3);
                int temp;
                final int level = temp = toStamp2.getState() - 16;
                for (int a = 0; temp > 1; --temp, ++a) {
                    if (temp > 1) {
                    }
                }
                toStamp2.setPotential3(potential(toStamp2.getItemId(), (level == 1 || Randomizer.nextInt(100) < 1) ? level : (level - 1)));
                sucstamp = true;
            }
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(stamp, toStamp2));
        }
        if (Randomizer.isSuccess(ii.getSuccess(toStamp.getItemId(), c.getPlayer(), toStamp))) {
            toStamp.setLines((byte) 3);
            int temp2;
            final int level2 = temp2 = toStamp.getState() - 16;
            for (int a2 = 0; temp2 > 1; --temp2, ++a2) {
                if (temp2 > 1) {
                }
            }
            toStamp.setPotential3(potential(toStamp.getItemId(), (level2 == 1 || Randomizer.nextInt(100) < 1) ? level2 : (level2 - 1)));
            sucstamp = true;
        }
        useInventory.removeItem(stamp.getPosition(), (short) 1, false);
        c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(stamp, toStamp));
        c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), sucstamp, stamp.getItemId(), toStamp.getItemId()));
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static void UseEditionalStamp(final LittleEndianAccessor slea, final MapleClient c) {
        slea.skip(4);
        final short slot = slea.readShort();
        final short dst = slea.readShort();
        boolean sucstamp = false;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip toStamp;
        if (dst < 0) {
            toStamp = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
        } else {
            toStamp = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
        }
        final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
        final Item stamp = useInventory.getItem(slot);
        if (GameConstants.isZero(c.getPlayer().getJob())) {
            final Equip toStamp2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
            final int level = toStamp2.getState() - 16;
            if (Randomizer.isSuccess(ii.getSuccess(toStamp2.getItemId(), c.getPlayer(), toStamp2))) {
                toStamp2.setPotential6(potential(toStamp2.getItemId(), (level == 1 || Randomizer.nextInt(100) < 1) ? level : (level - 1), true));
                sucstamp = true;
            }
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(stamp, toStamp));
        }
        if (Randomizer.isSuccess(ii.getSuccess(toStamp.getItemId(), c.getPlayer(), toStamp))) {
            final int level2 = toStamp.getState() - 16;
            toStamp.setPotential6(potential(toStamp.getItemId(), (level2 == 1 || Randomizer.nextInt(100) < 1) ? level2 : (level2 - 1), true));
            sucstamp = true;
        }
        useInventory.removeItem(stamp.getPosition(), (short) 1, false);
        c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(stamp, toStamp));
        c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), sucstamp, stamp.getItemId(), toStamp.getItemId()));
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static void UseChooseCube(final LittleEndianAccessor slea, final MapleClient c) {
        slea.skip(4);
        final byte type = slea.readByte();
        Equip equip = null;
        Equip zeroequip = null;
        if (c.getPlayer().choicepotential.getPosition() > 0) {
            equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(c.getPlayer().choicepotential.getPosition());
        } else {
            equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(c.getPlayer().choicepotential.getPosition());
        }
        final int cube = Integer.parseInt(c.getPlayer().getV("lastCube"));
        if (type == 6) {
            if (c.getPlayer().choicepotential.getPosition() > 0) {
                equip.set(c.getPlayer().choicepotential);
            } else {
                equip.set(c.getPlayer().choicepotential);
            }
        }
        if (GameConstants.isZeroWeapon(c.getPlayer().choicepotential.getItemId())) {
            zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
            zeroequip.setState(equip.getState());
            zeroequip.setLines(equip.getLines());
            zeroequip.setPotential1(equip.getPotential1());
            zeroequip.setPotential2(equip.getPotential2());
            zeroequip.setPotential3(equip.getPotential3());
            zeroequip.setPotential4(equip.getPotential4());
            zeroequip.setPotential5(equip.getPotential5());
            zeroequip.setPotential6(equip.getPotential6());
        }
        c.getPlayer().choicepotential = null;
        c.getPlayer().memorialcube = null;
        if (zeroequip != null) {
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIPPED, equip));
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIPPED, zeroequip));
            c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIPPED);
        } else {
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, equip));
            c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIP);
        }
    }

    public static final void addToScrollLog(final int accountID, final int charID, final int scrollID, final int itemID, final byte oldSlots, final byte newSlots, final byte viciousHammer, final String result, final boolean ws, final boolean ls, final int vega) {
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("INSERT INTO scroll_log VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, accountID);
            ps.setInt(2, charID);
            ps.setInt(3, scrollID);
            ps.setInt(4, itemID);
            ps.setByte(5, oldSlots);
            ps.setByte(6, newSlots);
            ps.setByte(7, viciousHammer);
            ps.setString(8, result);
            ps.setByte(9, (byte) (ws ? 1 : 0));
            ps.setByte(10, (byte) (ls ? 1 : 0));
            ps.setInt(11, vega);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException e) {
            FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e);
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }

    public static void useSilverKarma(final LittleEndianAccessor slea, final MapleCharacter chr) {
        slea.skip(4);
        final Item scroll = chr.getInventory(MapleInventoryType.USE).getItem(slea.readShort());
        final Item toScroll = chr.getInventory(MapleInventoryType.getByType((byte) slea.readShort())).getItem(slea.readShort());
        if (scroll.getItemId() == 2720000 || scroll.getItemId() == 2720001) {
            if (!MapleItemInformationProvider.getInstance().isKarmaEnabled(toScroll.getItemId())) {
                chr.dropMessage(5, "\uac00\uc704\ub97c \uc0ac\uc6a9\ud560 \uc218 \uc5c6\ub294 \uc544\uc774\ud15c\uc785\ub2c8\ub2e4.");
                return;
            }
            if (toScroll.getType() == 1) {
                final Equip nEquip = (Equip) toScroll;
                if (nEquip.getKarmaCount() > 0) {
                    nEquip.setKarmaCount((byte) (nEquip.getKarmaCount() - 1));
                } else if (nEquip.getKarmaCount() == 0) {
                    chr.dropMessage(5, "\uac00\uc704\ub97c \uc0ac\uc6a9\ud560 \uc218 \uc5c6\ub294 \uc544\uc774\ud15c\uc785\ub2c8\ub2e4.");
                    return;
                }
            }
            int flag = toScroll.getFlag();
            if (toScroll.getType() == 1) {
                flag += ItemFlag.KARMA_EQUIP.getValue();
            } else {
                flag += ItemFlag.KARMA_USE.getValue();
            }
            toScroll.setFlag(flag);
        }
        chr.removeItem(scroll.getItemId(), -1);
        chr.getClient().getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, GameConstants.getInventoryType(toScroll.getItemId()), toScroll));
    }

    public static boolean UseUpgradeScroll(final RecvPacketOpcode header, final short slot, final short dst, final byte ws, final MapleClient c, final MapleCharacter chr) {
        boolean whiteScroll = false;
        final boolean legendarySpirit = false;
        boolean recovery = false;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip toScroll = null;
        Equip toScroll2 = null;
        Item scroll = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        if (dst < 0) {
            toScroll = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
            if (GameConstants.isZero(chr.getJob())) {
                if (toScroll.getPosition() == -11) {
                    toScroll2 = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
                } else {
                    toScroll2 = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
                }
            }
        } else {
            toScroll = (Equip) chr.getInventory(MapleInventoryType.EQUIP).getItem(dst);
        }
        if (ii.getName(scroll.getItemId()).contains("펫장비")) {
            toScroll = (Equip) chr.getInventory(MapleInventoryType.CODY).getItem(dst);
        }
        if (toScroll == null) {
            return false;
        }
        final byte oldLevel = toScroll.getLevel();
        final byte oldEnhance = toScroll.getEnhance();
        final byte oldState = toScroll.getState();
        final int oldFlag = toScroll.getFlag();
        final byte oldSlots = toScroll.getUpgradeSlots();
        if (scroll == null || header == RecvPacketOpcode.USE_FLAG_SCROLL) {
            scroll = chr.getInventory(MapleInventoryType.CASH).getItem(slot);
        } else if (!GameConstants.isSpecialScroll(scroll.getItemId()) && !GameConstants.isCleanSlate(scroll.getItemId()) && !GameConstants.isEquipScroll(scroll.getItemId()) && !GameConstants.isPotentialScroll(scroll.getItemId()) && !GameConstants.isRebirthFireScroll(scroll.getItemId()) && scroll.getItemId() / 10000 != 204 && scroll.getItemId() / 10000 != 272 && scroll.getItemId() / 10000 != 264) {
            scroll = chr.getInventory(MapleInventoryType.CASH).getItem(slot);
        }
        if (scroll == null) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "존재하지 않는 주문서입니다.");
            return false;
        }
        final MapleItemInformationProvider ii2 = MapleItemInformationProvider.getInstance();
        if (scroll != null && toScroll != null) {
            FileoutputUtil.log(FileoutputUtil.아이템사용로그, "[주문서 사용] 계정 아이디 : " + c.getAccID() + " | " + c.getPlayer().getName() + "이 " + ii2.getName(scroll.getItemId()) + "를 " + ii2.getName(toScroll.getItemId()) + "에 사용함.");
        }
        if (scroll.getItemId() / 100 == 20496) {
            final Equip origin = (Equip) MapleItemInformationProvider.getInstance().getEquipById(toScroll.getItemId());
            toScroll.setAcc(origin.getAcc());
            toScroll.setAvoid(origin.getAvoid());
            toScroll.setDex(origin.getDex());
            toScroll.setHands(origin.getHands());
            toScroll.setHp(origin.getHp());
            toScroll.setInt(origin.getInt());
            toScroll.setJump(origin.getJump());
            toScroll.setLevel(origin.getLevel());
            toScroll.setLuk(origin.getLuk());
            toScroll.setMatk(origin.getMatk());
            toScroll.setMdef(origin.getMdef());
            toScroll.setMp(origin.getMp());
            toScroll.setSpeed(origin.getSpeed());
            toScroll.setStr(origin.getStr());
            toScroll.setUpgradeSlots(origin.getUpgradeSlots());
            toScroll.setWatk(origin.getWatk());
            toScroll.setWdef(origin.getWdef());
            toScroll.setEnhance((byte) 0);
            toScroll.setViciousHammer((byte) 0);
            chr.getInventory(MapleInventoryType.USE).removeItem(scroll.getPosition());
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(scroll, toScroll));
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(), Equip.ScrollResult.SUCCESS, legendarySpirit, scroll.getItemId(), toScroll.getItemId()), true);
            return false;
        }
        if (!GameConstants.isSpecialScroll(scroll.getItemId()) && !GameConstants.isCleanSlate(scroll.getItemId()) && !GameConstants.isEquipScroll(scroll.getItemId()) && scroll.getItemId() != 2049360 && scroll.getItemId() != 2049361 && !GameConstants.isPotentialScroll(scroll.getItemId()) && !GameConstants.isRebirthFireScroll(scroll.getItemId()) && !GameConstants.isLuckyScroll(scroll.getItemId())) {
            if (toScroll.getUpgradeSlots() < 1 && scroll.getItemId() != 2644001 && scroll.getItemId() != 2644002 && scroll.getItemId() != 2644004 && scroll.getItemId() != 2049371 && scroll.getItemId() != 2049372 && GameConstants.isStarForceScroll(scroll.getItemId()) < 0) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
                chr.dropMessage(1, "업그레이드 슬롯이 부족합니다.");
                return false;
            }
        } else if (GameConstants.isEquipScroll(scroll.getItemId()) && ((scroll.getItemId() != 2049360 && scroll.getItemId() != 2049361 && toScroll.getUpgradeSlots() >= 1) || toScroll.getEnhance() >= 15 || ii.isCash(toScroll.getItemId()))) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "더 이상 강화할 수 없는 아이템입니다.");
            return false;
        }
        if ((scroll.getItemId() == 2049166 || scroll.getItemId() == 2049167)
                && !GameConstants.isWeapon(toScroll.getItemId())) {
            chr.dropMessage(1, "해당 아이템에 주문서를 사용할 수 없습니다.");
            chr.getClient().send(CWvsContext.enableActions(chr));
            return false;
        }
        if (toScroll.getItemId() / 1000 != 1672 && !GameConstants.canScroll(toScroll.getItemId()) && GameConstants.isChaosScroll(scroll.getItemId())) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "주문서를 사용하실 수 없는 아이템입니다.");
            return false;
        }
        if (ii.isCash(toScroll.getItemId())) {
            if (toScroll.getItemId() / 1000 != 1802) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
                chr.dropMessage(1, "캐시 아이템은 강화가 불가능합니다.");
                return false;
            }
            if (!ii.getName(scroll.getItemId()).contains("펫장비")) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
                chr.dropMessage(1, "펫장비에는 펫장비 주문서만 사용하실 수 있습니다.");
                return false;
            }
        }
        if (scroll.getItemId() == 2049135 && (toScroll.getItemId() < 1182000 || toScroll.getItemId() > 1182005)) {
            chr.dropMessage(1, "여명 아이템에만 사용하실 수 있습니다.");
            chr.getClient().send(CWvsContext.enableActions(chr));
            return false;
        }
        if (GameConstants.isTablet(scroll.getItemId()) && toScroll.getDurability() < 0) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "연성서를 사용하실 수 없는 아이템입니다.");
            return false;
        }
        Item wscroll = null;
        final List<Integer> scrollReqs = ii.getScrollReqs(scroll.getItemId());
        if (scrollReqs.size() > 0 && !scrollReqs.contains(toScroll.getItemId())) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "RETURN 8");
            return false;
        }
        if (whiteScroll) {
            wscroll = chr.getInventory(MapleInventoryType.USE).findById(2340000);
            if (wscroll == null) {
                whiteScroll = false;
            }
        }
        if (scroll.getItemId() == 2041200 && toScroll.getItemId() != 1122000 && toScroll.getItemId() != 1122076) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "드래곤의 돌은 혼테일의 목걸이에만 사용 가능한 아이템입니다.");
            return false;
        }
        if ((scroll.getItemId() == 2046856 || scroll.getItemId() == 2046857) && (toScroll.getItemId() / 1000 == 1152 || !GameConstants.isAccessory(toScroll.getItemId()))) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "악세서리에만 사용 가능한 주문서입니다.");
            return false;
        }
        if ((scroll.getItemId() == 2049166 || scroll.getItemId() == 2046991 || scroll.getItemId() == 2046992 || scroll.getItemId() == 2046996 || scroll.getItemId() == 2046997) && GameConstants.isTwoHanded(toScroll.getItemId())) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "한손무기에만 사용 가능한 주문서입니다.");
            return false;
        }
        if ((scroll.getItemId() == 2049167 || scroll.getItemId() == 2047814 || scroll.getItemId() == 2047818) && !GameConstants.isTwoHanded(toScroll.getItemId()) && toScroll.getItemId() / 1000 != 1672) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "두손무기에만 사용 가능한 주문서입니다.");
            return false;
        }
        if (GameConstants.isAccessoryScroll(scroll.getItemId()) && !GameConstants.isAccessory(toScroll.getItemId())) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "악세서리 주문서를 사용하실 수 없는 아이템입니다.");
            return false;
        }
        if (toScroll.getUpgradeSlots() > 0 && scroll.getItemId() >= 2049370 && scroll.getItemId() <= 2049377) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
            chr.dropMessage(1, "아직 업그레이드 슬롯이 남아있습니다.");
            return false;
        }
        if (scroll.getQuantity() <= 0) {
            chr.dropMessage(1, "존재하지 않는 주문서는 사용할 수 없습니다.");
            return false;
        }
        if (ItemFlag.RETURN_SCROLL.check(toScroll.getFlag())) {
            chr.returnSc = scroll.getItemId();
            chr.returnscroll = (Equip) toScroll.copy();
        }
        if (header == RecvPacketOpcode.USE_BLACK_REBIRTH_SCROLL) {
            final long newRebirth = toScroll.newRebirth(ii.getReqLevel(toScroll.getItemId()), scroll.getItemId(), false);
            c.getSession().writeAndFlush((Object) CWvsContext.useBlackRebirthScroll(toScroll, scroll, newRebirth, false));
            MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(scroll.getItemId()), scroll.getPosition(), (short) 1, false, false);
            c.getSession().writeAndFlush((Object) CWvsContext.blackRebirthResult(true, toScroll.getFire(), toScroll));
            c.getSession().writeAndFlush((Object) CWvsContext.blackRebirthResult(false, newRebirth, toScroll));
            chr.blackRebirth = newRebirth;
            chr.blackRebirthScroll = (Equip) toScroll.copy();
            chr.blackRebirthPos = slot;
            return false;
        }
        final Equip scrolled = (Equip) ii.scrollEquipWithId(toScroll, scroll, whiteScroll, chr);
        Equip.ScrollResult scrollSuccess;
        if (scrolled == null) {
            scrollSuccess = Equip.ScrollResult.CURSE;
        } else if (GameConstants.isRebirthFireScroll(scroll.getItemId())) {
            scrollSuccess = Equip.ScrollResult.SUCCESS;
        } else if (scrolled.getLevel() > oldLevel || scrolled.getEnhance() > oldEnhance || scrolled.getState() != oldState || scrolled.getFlag() > oldFlag) {
            scrollSuccess = Equip.ScrollResult.SUCCESS;
        } else if (GameConstants.isCleanSlate(scroll.getItemId()) && scrolled.getUpgradeSlots() > oldSlots) {
            scrollSuccess = Equip.ScrollResult.SUCCESS;
        } else {
            scrollSuccess = Equip.ScrollResult.FAIL;
            if (ItemFlag.RECOVERY_SHIELD.check(toScroll.getFlag())) {
                recovery = true;
            }
        }
        if (recovery) {
            chr.dropMessage(5, "\uc8fc\ubb38\uc11c\uc758 \ud6a8\uacfc\ub85c \uc0ac\uc6a9\ub41c \uc8fc\ubb38\uc11c\uac00 \ucc28\uac10\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4.");
        } else if (GameConstants.isZero(chr.getJob()) && toScroll.getPosition() == -11) {
            chr.getInventory(GameConstants.getInventoryType(scroll.getItemId())).removeItem(scroll.getPosition(), (short) 1, false);
        } else {
            chr.getInventory(GameConstants.getInventoryType(scroll.getItemId())).removeItem(scroll.getPosition(), (short) 1, false);
        }
        if (scrollSuccess == Equip.ScrollResult.SUCCESS) {
            EquipmentEnchant.checkEquipmentStats(c, toScroll);
            if (toScroll2 != null) {
                EquipmentEnchant.checkEquipmentStats(c, toScroll2);
            }
        }
        if (whiteScroll) {
            MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(scroll.getItemId()), wscroll.getPosition(), (short) 1, false, false);
        }
        if (header != RecvPacketOpcode.USE_FLAG_SCROLL) {
            if (ItemFlag.RECOVERY_SHIELD.check(toScroll.getFlag())) {
                toScroll.setFlag(toScroll.getFlag() - ItemFlag.RECOVERY_SHIELD.getValue());
                if (GameConstants.isZero(chr.getJob()) && toScroll2 != null) {
                    toScroll2.setFlag(toScroll2.getFlag() - ItemFlag.RECOVERY_SHIELD.getValue());
                }
            }
            if (ItemFlag.SAFETY_SHIELD.check(toScroll.getFlag())) {
                toScroll.setFlag(toScroll.getFlag() - ItemFlag.SAFETY_SHIELD.getValue());
                if (GameConstants.isZero(chr.getJob()) && toScroll2 != null) {
                    toScroll2.setFlag(toScroll2.getFlag() - ItemFlag.SAFETY_SHIELD.getValue());
                }
            }
            if (ItemFlag.PROTECT_SHIELD.check(toScroll.getFlag())) {
                toScroll.setFlag(toScroll.getFlag() - ItemFlag.PROTECT_SHIELD.getValue());
                if (GameConstants.isZero(chr.getJob()) && toScroll2 != null) {
                    toScroll2.setFlag(toScroll2.getFlag() - ItemFlag.PROTECT_SHIELD.getValue());
                }
            }
            if (ItemFlag.LUCKY_PROTECT_SHIELD.check(toScroll.getFlag())) {
                toScroll.setFlag(toScroll.getFlag() - ItemFlag.LUCKY_PROTECT_SHIELD.getValue());
                if (GameConstants.isZero(chr.getJob()) && toScroll2 != null) {
                    toScroll2.setFlag(toScroll2.getFlag() - ItemFlag.LUCKY_PROTECT_SHIELD.getValue());
                }
            }
        }
        if (scrollSuccess == Equip.ScrollResult.CURSE) {
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(scroll, toScroll, true));
            if (dst < 0) {
                chr.getInventory(MapleInventoryType.EQUIPPED).removeItem(toScroll.getPosition());
            } else {
                chr.getInventory(MapleInventoryType.EQUIP).removeItem(toScroll.getPosition());
            }
        } else {
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(scroll, toScroll, false));
            if (toScroll2 != null) {
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(scroll, toScroll2, false));
            }
            if (!GameConstants.isSpecialScroll(scroll.getItemId()) && !GameConstants.isCleanSlate(scroll.getItemId()) && !GameConstants.isEquipScroll(scroll.getItemId()) && scroll.getItemId() != 2049360 && scroll.getItemId() != 2049361 && !GameConstants.isPotentialScroll(scroll.getItemId()) && !GameConstants.isRebirthFireScroll(scroll.getItemId()) && !GameConstants.isLuckyScroll(scroll.getItemId()) && c.getPlayer().returnscroll != null && scrollSuccess == Equip.ScrollResult.SUCCESS) {
                c.getSession().writeAndFlush((Object) CWvsContext.returnEffectConfirm(c.getPlayer().returnscroll, scroll.getItemId()));
                c.getSession().writeAndFlush((Object) CWvsContext.returnEffectModify(c.getPlayer().returnscroll, scroll.getItemId()));
            } else if (!GameConstants.isSpecialScroll(scroll.getItemId()) && !GameConstants.isCleanSlate(scroll.getItemId()) && !GameConstants.isEquipScroll(scroll.getItemId()) && scroll.getItemId() != 2049360 && scroll.getItemId() != 2049361 && !GameConstants.isPotentialScroll(scroll.getItemId()) && !GameConstants.isRebirthFireScroll(scroll.getItemId()) && !GameConstants.isLuckyScroll(scroll.getItemId()) && c.getPlayer().returnscroll != null && scrollSuccess == Equip.ScrollResult.FAIL) {
                c.getPlayer().returnscroll = null;
                toScroll.setFlag(toScroll.getFlag() - ItemFlag.RETURN_SCROLL.getValue());
                toScroll.setUpgradeSlots((byte) (toScroll.getUpgradeSlots() + 1));
                chr.getClient().getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateEquipSlot(toScroll));
            }
        }
        if (GameConstants.isZero(chr.getJob()) && toScroll.getPosition() == -11) {
            chr.getMap().broadcastMessage(chr, CField.getScrollEffect(c.getPlayer().getId(), scrollSuccess, legendarySpirit, scroll.getItemId(), toScroll.getItemId()), true);
        } else {
            chr.getMap().broadcastMessage(chr, CField.getScrollEffect(c.getPlayer().getId(), scrollSuccess, legendarySpirit, scroll.getItemId(), toScroll.getItemId()), true);
        }
        if (scrolled.getShowScrollOption() != null) {
            chr.getClient().getSession().writeAndFlush((Object) CField.showScrollOption(scrolled.getItemId(), scroll.getItemId(), scrolled.getShowScrollOption()));
            scrolled.setShowScrollOption(null);
        }
        if (dst < 0 && (scrollSuccess == Equip.ScrollResult.SUCCESS || scrollSuccess == Equip.ScrollResult.CURSE)) {
            chr.equipChanged();
        }
        if (header == RecvPacketOpcode.USE_REBIRTH_SCROLL) {
            chr.getClient().send(CWvsContext.RebirthScrollWindow(scroll.getItemId(), toScroll.getPosition()));
        }
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
        return true;
    }

    public static void UseEditionalScroll(final LittleEndianAccessor slea, final MapleClient c) {
        try {
            slea.skip(4);
            final short mode = slea.readShort();
            final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(mode);
            if (toUse.getItemId() >= 2048305 && toUse.getItemId() <= 2048316) {
                final short slot = slea.readShort();
                Item item;
                if (slot < 0) {
                    item = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
                } else {
                    item = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
                }
                Equip zeroEquip = null;
                if (GameConstants.isAlphaWeapon(item.getItemId())) {
                    zeroEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
                } else if (GameConstants.isBetaWeapon(item.getItemId())) {
                    zeroEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
                }
                if (GameConstants.isZero(c.getPlayer().getJob()) && zeroEquip != null) {
                    final Item item2 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
                    final Item item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
                    final Equip eq1 = (Equip) item2;
                    final Equip eq2 = (Equip) item3;
                    if (eq1.getState() == 0 || eq2.getState() == 0 || (eq1.getState() == 1 && eq1.getPotential1() == 0) || (eq2.getState() == 1 && eq2.getPotential1() == 0)) {
                        c.getPlayer().dropMessage(1, "\uba3c\uc800 \uc7a0\uc7ac\ub2a5\ub825\uc744 \uc5f4\uc5b4\uc8fc\uc138\uc694.");
                        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    final boolean succes = Randomizer.isSuccess(ii.getSuccess(item.getItemId(), c.getPlayer(), item));
                    if (succes) {
                        int alpha_option = 0;
                        int alpha_option2 = 0;
                        int alpha_option3_sbal = 0;
                        final int alpha_level = 1;
                        final int beta_option = 0;
                        final int beta_option2 = 0;
                        final int beta_option3_sbal = 0;
                        final int beta_level = 1;
                        alpha_option = potential(eq1.getItemId(), alpha_level, true);
                        alpha_option2 = potential(eq1.getItemId(), alpha_level, true);
                        alpha_option3_sbal = potential(eq1.getItemId(), alpha_level, true);
                        if (Randomizer.nextInt(100) < 20 || toUse.getItemId() == 2048306) {
                            eq1.setPotential4(alpha_option);
                            eq1.setPotential5(alpha_option2);
                            eq1.setPotential6(alpha_option3_sbal);
                            eq2.setPotential4(alpha_option);
                            eq2.setPotential5(alpha_option2);
                            eq2.setPotential6(alpha_option3_sbal);
                        } else {
                            eq1.setPotential4(alpha_option);
                            eq1.setPotential5(alpha_option2);
                            eq2.setPotential4(alpha_option);
                            eq2.setPotential5(alpha_option2);
                        }
                    }
                    c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(toUse, item2));
                    c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(toUse, item3));
                    c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), succes, toUse.getItemId(), item.getItemId()));
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false);
                    c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                } else {
                    final Equip eq3 = (Equip) item;
                    if (eq3.getState() == 0 || eq3.getPotential1() == 0) {
                        c.getPlayer().dropMessage(1, "\uba3c\uc800 \uc7a0\uc7ac\ub2a5\ub825\uc744 \uc5f4\uc5b4\uc8fc\uc138\uc694.");
                        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    final MapleItemInformationProvider ii2 = MapleItemInformationProvider.getInstance();
                    final boolean succes2 = Randomizer.isSuccess(ii2.getSuccess(item.getItemId(), c.getPlayer(), item));
                    if (succes2) {
                        int option = 0;
                        int option2 = 0;
                        int option3_sbal = 0;
                        final int level = 2;
                        option = potential(eq3.getItemId(), level, true);
                        option2 = potential(eq3.getItemId(), level, true);
                        option3_sbal = potential(eq3.getItemId(), level, true);
                        if (Randomizer.nextInt(100) < 20 || toUse.getItemId() == 2048306) {
                            eq3.setPotential4(option);
                            eq3.setPotential5(option2);
                            eq3.setPotential6(option3_sbal);
                        } else {
                            eq3.setPotential4(option);
                            eq3.setPotential5(option2);
                        }
                    }
                    c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(toUse, item));
                    c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), succes2, toUse.getItemId(), eq3.getItemId()));
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false);
                    c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static final boolean UseSkillBook(final short slot, final int itemId, final MapleClient c, final MapleCharacter chr) {
        final Item toUse = chr.getInventory(GameConstants.getInventoryType(itemId)).getItem(slot);
        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
            return false;
        }
        final Map<String, Integer> skilldata = MapleItemInformationProvider.getInstance().getEquipStats(toUse.getItemId());
        if (skilldata == null) {
            return false;
        }
        boolean canuse = false;
        boolean success = false;
        final int skill = 0;
        final int maxlevel = 0;
        final Integer SuccessRate = skilldata.get("success");
        final Integer ReqSkillLevel = skilldata.get("reqSkillLevel");
        final Integer MasterLevel = skilldata.get("masterLevel");
        byte i = 0;
        while (true) {
            final Integer CurrentLoopedSkillId = skilldata.get("skillid" + i);
            ++i;
            if (CurrentLoopedSkillId == null) {
                break;
            }
            if (MasterLevel == null) {
                break;
            }
            final Skill CurrSkillData = SkillFactory.getSkill(CurrentLoopedSkillId);
            if (CurrSkillData != null && CurrSkillData.canBeLearnedBy(chr) && (ReqSkillLevel == null || chr.getSkillLevel(CurrSkillData) >= ReqSkillLevel) && chr.getMasterLevel(CurrSkillData) < MasterLevel) {
                canuse = true;
                if (SuccessRate == null || Randomizer.nextInt(100) <= SuccessRate) {
                    success = true;
                    chr.changeSingleSkillLevel(CurrSkillData, chr.getSkillLevel(CurrSkillData), (byte) (int) MasterLevel);
                } else {
                    success = false;
                }
                MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(itemId), slot, (short) 1, false);
                break;
            }
        }
        c.getPlayer().getMap().broadcastMessage(CWvsContext.useSkillBook(chr, skill, maxlevel, canuse, success));
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
        return canuse;
    }

    public static final void UseCatchItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        slea.readInt();
        c.getPlayer().setScrolledPosition((short) 0);
        final short slot = slea.readShort();
        final int itemid = slea.readInt();
        final MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
        final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        final MapleMap map = chr.getMap();
        if (toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemid && mob != null && itemid / 10000 == 227 && MapleItemInformationProvider.getInstance().getCardMobId(itemid) == mob.getId()) {
            if (!MapleItemInformationProvider.getInstance().isMobHP(itemid) || mob.getHp() <= mob.getMobMaxHp() / 2L) {
                map.broadcastMessage(MobPacket.catchMonster(mob.getObjectId(), (byte) 1));
                map.killMonster(mob, chr, true, false, (byte) 1);
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false, false);
                if (MapleItemInformationProvider.getInstance().getCreateId(itemid) > 0) {
                    MapleInventoryManipulator.addById(c, MapleItemInformationProvider.getInstance().getCreateId(itemid), (short) 1, "Catch item " + itemid + " on " + FileoutputUtil.CurrentReadable_Date());
                }
            } else {
                map.broadcastMessage(MobPacket.catchMonster(mob.getObjectId(), (byte) 0));
                c.getSession().writeAndFlush((Object) CWvsContext.catchMob(mob.getId(), itemid, (byte) 0));
            }
        }
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static final void UseMountFood(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        slea.readInt();
        final short slot = slea.readShort();
        final int itemid = slea.readInt();
        final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        final MapleMount mount = chr.getMount();
        if (itemid / 10000 == 226 && toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemid && mount != null) {
            final int fatigue = mount.getFatigue();
            boolean levelup = false;
            mount.setFatigue((byte) (-30));
            if (fatigue > 0) {
                mount.increaseExp();
                final int level = mount.getLevel();
                if (level < 30 && mount.getExp() >= GameConstants.getMountExpNeededForLevel(level + 1)) {
                    mount.setLevel((byte) (level + 1));
                    levelup = true;
                }
            }
            chr.getMap().broadcastMessage(CWvsContext.updateMount(chr, levelup));
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
        }
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static final void UseScriptedNPCItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final short slot = slea.readShort();
        final int itemId = slea.readInt();
        final Item toUse = chr.getInventory(GameConstants.getInventoryType(itemId)).getItem(slot);
        long expiration_days = 0L;
        int mountid = 0;
        if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId && !chr.inPVP()) {
            FileoutputUtil.log(FileoutputUtil.스크립트아이템사용로그, "[스크립트 아이템 사용] 계정 아이디 : " + c.getAccID() + " | " + c.getPlayer().getName() + "이 " + MapleItemInformationProvider.getInstance().getName(toUse.getItemId()) + "(" + toUse.getItemId() + ")을 사용함.");
            switch (toUse.getItemId()) {
                case 2432290:
                case 2631501: {
                    if (c.getPlayer().getMap().MapiaIng) {
                        c.send(CWvsContext.serverNotice(1, "", "지금은 사용 할 수 없습니다."));
                        c.send(CWvsContext.enableActions(chr));
                        return;
                    }
                    c.getPlayer().getMap().MapiaIng = true;
                    for (final MapleCharacter chrs : c.getPlayer().getMap().getAllChracater()) {
                        if (chrs.isAlive()) {
                            chrs.cancelEffect(MapleItemInformationProvider.getInstance().getItemEffect((toUse.getItemId() == 2631501) ? 2023300 : 2023912));
                            MapleItemInformationProvider.getInstance().getItemEffect((toUse.getItemId() == 2631501) ? 2023912 : 2023300).applyTo(chrs);
                        }
                    }
                    c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("길드의 축복이 여러분과 함께 하기를...", (toUse.getItemId() == 2631501) ? 5121101 : 5121041, true));
                    Timer.BuffTimer.getInstance().schedule(() -> {
                        c.getPlayer().getMap().MapiaIng = false;
                        c.getPlayer().getMap().broadcastMessage(CField.removeMapEffect());
                        return;
                    }, 10000L);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                }
                
                 case 2430732: {
                    int id = 0;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430885: {
                    int id = 1;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430886: {
                    int id = 2;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430887: {
                    int id = 3;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430888: {
                    int id = 4;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430889: {
                    int id = 5;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430890: {
                    int id = 6;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430891: {
                    int id = 7;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430892: {
                    int id = 8;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430893: {
                    int id = 9;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430894: {
                    int id = 10;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430945: {
                    int id = 11;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                case 2430946: {
                    int id = 12;
                    chr.addCustomItem(id);
                    c.getPlayer().dropMessage(5, GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                
                case 2433509: {
                    NPCScriptManager.getInstance().startItem(c, 9000162, "consume_2433509");
                    break;
                }
                case 2433510: {
                    NPCScriptManager.getInstance().startItem(c, 9000162, "consume_2433510");
                    break;
                }
                case 2434006: {
                    NPCScriptManager.getInstance().startItem(c, 9000162, "consume_2434006");
                    break;
                }
                case 5680222: {
                    NPCScriptManager.getInstance().startItem(c, 9000216, "mannequin_add");
                    break;
                }
                case 5680531: {
                    NPCScriptManager.getInstance().startItem(c, 9000216, "mannequin_slotadd");
                    break;
                }
                case 2431940: {
                    int pirodo = 1000;
                    switch (1) {
                        case 1: {
                            pirodo = 60;
                            break;
                        }
                        case 2: {
                            pirodo = 80;
                            break;
                        }
                        case 3: {
                            pirodo = 100;
                            break;
                        }
                        case 4: {
                            pirodo = 120;
                            break;
                        }
                        case 5: {
                            pirodo = 160;
                            break;
                        }
                        case 6: {
                            pirodo = 160;
                            break;
                        }
                        case 7: {
                            pirodo = 160;
                            break;
                        }
                        case 8: {
                            pirodo = 160;
                            break;
                        }
                    }
                    long point = c.getPlayer().getKeyValue(123, "pp") + 10;
                    if (c.getPlayer().getKeyValue(123, "pp") >= pirodo) {
                        c.getPlayer().dropMessage(5, "이미 모든 피로도가 충전되있습니다.");
                        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    if (c.getPlayer().getKeyValue(123, "pp") + 10 > pirodo) {
                        point = pirodo;
                    }
                    c.getPlayer().setKeyValue(123, "pp", String.valueOf(point));
                    c.getPlayer().dropMessage(5, "피로도가 증가했습니다. 피로도 : " + c.getPlayer().getKeyValue(123, "pp"));
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                    break;
                }
                
                case 2350000: {
                    if (c.getCharacterSlots() >= 48) {
                        c.getPlayer().dropMessage(5, "현재 캐릭터 슬롯 증가쿠폰을 사용하실 수 없습니다.");
                        return;
                    }
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    if (!c.gainCharacterSlot()) {
                        c.getPlayer().dropMessage(5, "현재 캐릭터 슬롯 증가쿠폰을 사용하실 수 없습니다.");
                        break;
                    }
                    c.getPlayer().dropMessage(1, "캐릭터 슬롯 개수를 늘렸습니다.");
                    break;
                }
                case 2435719:
                case 2435902: {
                    if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
                        c.getPlayer().dropMessage(1, "핑크빈과 예티는 불가능한 행동입니다.");
                        break;
                    }
                    MatrixHandler.UseCoreJamStone(c, toUse.getItemId(), Randomizer.nextLong());
                    break;
                }
                case 2631527: {
                    if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
                        c.getPlayer().dropMessage(1, "핑크빈과 예티는 불가능한 행동입니다.");
                        break;
                    }
                    MatrixHandler.UseEnforcedCoreJamStone(c, toUse.getItemId(), Randomizer.nextInt());
                    break;
                }
                case 2438411:
                case 2438412: {
                    if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
                        c.getPlayer().dropMessage(1, "핑크빈과 예티는 불가능한 행동입니다.");
                        break;
                    }
                    MatrixHandler.UseMirrorCoreJamStone(c, toUse.getItemId(), Randomizer.nextInt());
                    break;
                }
                case 2632972: {
                    if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
                        c.getPlayer().dropMessage(1, "핑크빈과 예티는 불가능한 행동입니다.");
                        break;
                    }
                    MatrixHandler.UseCraftCoreJamStone(c, toUse.getItemId(), Randomizer.nextInt());
                    break;
                }
                case 2430008: {
                    chr.saveLocation(SavedLocationType.RICHIE);
                    boolean warped = false;
                    for (int i = 390001000; i <= 390001004; ++i) {
                        final MapleMap map = c.getChannelServer().getMapFactory().getMap(i);
                        if (map.getCharactersSize() == 0) {
                            chr.changeMap(map, map.getPortal(0));
                            warped = true;
                            break;
                        }
                    }
                    if (warped) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        break;
                    }
                    c.getPlayer().dropMessage(5, "All maps are currently in use, please try again later.");
                    break;
                }
                case 2430112: {
                    if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 1) {
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                    if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430112) >= 25) {
                        if (MapleInventoryManipulator.checkSpace(c, 2049400, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 25, true, false)) {
                            MapleInventoryManipulator.addById(c, 2049400, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                            break;
                        }
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    } else {
                        if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430112) < 10) {
                            c.getPlayer().dropMessage(5, "There needs to be 10 Fragments for a Potential Scroll, 25 for Advanced Potential Scroll.");
                            break;
                        }
                        if (MapleInventoryManipulator.checkSpace(c, 2049400, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 10, true, false)) {
                            MapleInventoryManipulator.addById(c, 2049401, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                            break;
                        }
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                }
                case 2430481: {
                    if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 1) {
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                    if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430481) >= 30) {
                        if (MapleInventoryManipulator.checkSpace(c, 2049701, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 30, true, false)) {
                            MapleInventoryManipulator.addById(c, 2049701, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                            break;
                        }
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    } else {
                        if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430481) < 20) {
                            c.getPlayer().dropMessage(5, "There needs to be 20 Fragments for a Advanced Equip Enhancement Scroll, 30 for Epic Potential Scroll 80%.");
                            break;
                        }
                        if (MapleInventoryManipulator.checkSpace(c, 2049300, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 20, true, false)) {
                            MapleInventoryManipulator.addById(c, 2049300, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                            break;
                        }
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                }
                case 2430691: {
                    if (c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() < 1) {
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                    if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430691) < 10) {
                        c.getPlayer().dropMessage(5, "There needs to be 10 Fragments for a Nebulite Diffuser.");
                        break;
                    }
                    if (MapleInventoryManipulator.checkSpace(c, 5750001, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 10, true, false)) {
                        MapleInventoryManipulator.addById(c, 5750001, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                        break;
                    }
                    c.getPlayer().dropMessage(5, "Please make some space.");
                    break;
                }
                case 2430748: {
                    if (c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 1) {
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                    if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430748) < 20) {
                        c.getPlayer().dropMessage(5, "There needs to be 20 Fragments for a Premium Fusion Ticket.");
                        break;
                    }
                    if (MapleInventoryManipulator.checkSpace(c, 4420000, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 20, true, false)) {
                        MapleInventoryManipulator.addById(c, 4420000, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                        break;
                    }
                    c.getPlayer().dropMessage(5, "Please make some space.");
                    break;
                }
                case 5680019: {
                    final int hair = 32150 + c.getPlayer().getHair() % 10;
                    c.getPlayer().setHair(hair);
                    c.getPlayer().updateSingleStat(MapleStat.HAIR, hair);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                    break;
                }
                case 5680020: {
                    final int hair = 32160 + c.getPlayer().getHair() % 10;
                    c.getPlayer().setHair(hair);
                    c.getPlayer().updateSingleStat(MapleStat.HAIR, hair);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
                    break;
                }
                case 3994225: {
                    c.getPlayer().dropMessage(5, "Please bring this item to the NPC.");
                    break;
                }
                case 2430212: {
                    final MapleQuestStatus marr = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122500));
                    if (marr.getCustomData() == null) {
                        marr.setCustomData("0");
                    }
                    final long lastTime = Long.parseLong(marr.getCustomData());
                    if (lastTime + 600000L > System.currentTimeMillis()) {
                        c.getPlayer().dropMessage(5, "You can only use one energy drink per 10 minutes.");
                        break;
                    }
                    if (c.getPlayer().getFatigue() > 0) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 5);
                        break;
                    }
                    break;
                }
                case 2430213: {
                    final MapleQuestStatus marr = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122500));
                    if (marr.getCustomData() == null) {
                        marr.setCustomData("0");
                    }
                    final long lastTime = Long.parseLong(marr.getCustomData());
                    if (lastTime + 600000L > System.currentTimeMillis()) {
                        c.getPlayer().dropMessage(5, "You can only use one energy drink per 10 minutes.");
                        break;
                    }
                    if (c.getPlayer().getFatigue() > 0) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 10);
                        break;
                    }
                    break;
                }
                case 2430214:
                case 2430220: {
                    if (c.getPlayer().getFatigue() > 0) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 30);
                        break;
                    }
                    break;
                }
                case 2430227: {
                    if (c.getPlayer().getFatigue() > 0) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 50);
                        break;
                    }
                    break;
                }
                case 2430231: {
                    final MapleQuestStatus marr = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122500));
                    if (marr.getCustomData() == null) {
                        marr.setCustomData("0");
                    }
                    final long lastTime = Long.parseLong(marr.getCustomData());
                    if (lastTime + 600000L > System.currentTimeMillis()) {
                        c.getPlayer().dropMessage(5, "You can only use one energy drink per 10 minutes.");
                        break;
                    }
                    if (c.getPlayer().getFatigue() > 0) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 40);
                        break;
                    }
                    break;
                }
                case 2430144: {
                    final int itemid = Randomizer.nextInt(373) + 2290000;
                    if (MapleItemInformationProvider.getInstance().itemExists(itemid) && !MapleItemInformationProvider.getInstance().getName(itemid).contains("Special") && !MapleItemInformationProvider.getInstance().getName(itemid).contains("Event")) {
                        MapleInventoryManipulator.addById(c, itemid, (short) 1, "Reward item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        break;
                    }
                    break;
                }
                case 2430370: {
                    if (MapleInventoryManipulator.checkSpace(c, 2028062, 1, "")) {
                        MapleInventoryManipulator.addById(c, 2028062, (short) 1, "Reward item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        break;
                    }
                    break;
                }
                case 2430158: {
                    if (c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 1) {
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                    if (c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000630) >= 100) {
                        if (MapleInventoryManipulator.checkSpace(c, 4310010, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false)) {
                            MapleInventoryManipulator.addById(c, 4310010, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                            break;
                        }
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    } else {
                        if (c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000630) < 50) {
                            c.getPlayer().dropMessage(5, "There needs to be 50 Purification Totems for a Noble Lion King Medal, 100 for Royal Lion King Medal.");
                            break;
                        }
                        if (MapleInventoryManipulator.checkSpace(c, 4310009, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false)) {
                            MapleInventoryManipulator.addById(c, 4310009, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                            break;
                        }
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                }
                case 2430159: {
                    MapleQuest.getInstance(3182).forceComplete(c.getPlayer(), 2161004);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                }
                case 2430200: {
                    if (c.getPlayer().getQuestStatus(31152) != 2) {
                        c.getPlayer().dropMessage(5, "You have no idea how to use it.");
                        break;
                    }
                    if (c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 1) {
                        c.getPlayer().dropMessage(5, "Please make some space.");
                        break;
                    }
                    if (c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000660) < 1 || c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000661) < 1 || c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000662) < 1 || c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000663) < 1) {
                        c.getPlayer().dropMessage(5, "There needs to be 1 of each Stone for a Dream Key.");
                        break;
                    }
                    if (MapleInventoryManipulator.checkSpace(c, 4032923, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false) && MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4000660, 1, true, false) && MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4000661, 1, true, false) && MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4000662, 1, true, false) && MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4000663, 1, true, false)) {
                        MapleInventoryManipulator.addById(c, 4032923, (short) 1, "Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                        break;
                    }
                    c.getPlayer().dropMessage(5, "Please make some space.");
                    break;
                }
                case 2430130:
                case 2430131: {
                    if (GameConstants.isResist(c.getPlayer().getJob())) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        c.getPlayer().gainExp(20000 + c.getPlayer().getLevel() * 50 * c.getChannelServer().getExpRate(), true, true, false);
                        break;
                    }
                    c.getPlayer().dropMessage(5, "You may not use this item.");
                    break;
                }
                case 2430132:
                case 2430133:
                case 2430134:
                case 2430142: {
                    if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < 1) {
                        c.getPlayer().dropMessage(5, "Make some space.");
                        break;
                    }
                    if (c.getPlayer().getJob() == 3200 || c.getPlayer().getJob() == 3210 || c.getPlayer().getJob() == 3211 || c.getPlayer().getJob() == 3212) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        MapleInventoryManipulator.addById(c, 1382101, (short) 1, "Scripted item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
                        break;
                    }
                    if (c.getPlayer().getJob() == 3300 || c.getPlayer().getJob() == 3310 || c.getPlayer().getJob() == 3311 || c.getPlayer().getJob() == 3312) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        MapleInventoryManipulator.addById(c, 1462093, (short) 1, "Scripted item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
                        break;
                    }
                    if (c.getPlayer().getJob() == 3500 || c.getPlayer().getJob() == 3510 || c.getPlayer().getJob() == 3511 || c.getPlayer().getJob() == 3512) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                        MapleInventoryManipulator.addById(c, 1492080, (short) 1, "Scripted item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
                        break;
                    }
                    c.getPlayer().dropMessage(5, "You may not use this item.");
                    break;
                }
                case 2430036: {
                    mountid = 1027;
                    expiration_days = 1L;
                    break;
                }
                case 2430053: {
                    mountid = 1027;
                    expiration_days = 30L;
                    break;
                }
                case 2430037: {
                    mountid = 1028;
                    expiration_days = 1L;
                    break;
                }
                case 2430054: {
                    mountid = 1028;
                    expiration_days = 30L;
                    break;
                }
                case 2430038: {
                    mountid = 1029;
                    expiration_days = 1L;
                    break;
                }
                case 2430257: {
                    mountid = 1029;
                    expiration_days = 7L;
                    break;
                }
                case 2430055: {
                    mountid = 1029;
                    expiration_days = 30L;
                    break;
                }
                case 2430039: {
                    mountid = 1030;
                    expiration_days = 1L;
                    break;
                }
                case 2430040: {
                    mountid = 1031;
                    expiration_days = 1L;
                    break;
                }
                case 2430259: {
                    mountid = 1031;
                    expiration_days = 3L;
                    break;
                }
                case 2430225: {
                    mountid = 1031;
                    expiration_days = 10L;
                    break;
                }
                case 2430242: {
                    mountid = 1063;
                    expiration_days = 10L;
                    break;
                }
                case 2430261: {
                    mountid = 1064;
                    expiration_days = 3L;
                    break;
                }
                case 2430243: {
                    mountid = 1064;
                    expiration_days = 10L;
                    break;
                }
                case 2430249: {
                    mountid = 80001027;
                    expiration_days = 3L;
                    break;
                }
                case 2430056: {
                    mountid = 1035;
                    expiration_days = 30L;
                    break;
                }
                case 2430057: {
                    mountid = 1033;
                    expiration_days = 30L;
                    break;
                }
                case 2430072: {
                    mountid = 1034;
                    expiration_days = 7L;
                    break;
                }
                case 2430272: {
                    mountid = 80001032;
                    expiration_days = 3L;
                    break;
                }
                case 2430275: {
                    mountid = 80001033;
                    expiration_days = 7L;
                    break;
                }
                case 2430075: {
                    mountid = 1038;
                    expiration_days = 15L;
                    break;
                }
                case 2430076: {
                    mountid = 1039;
                    expiration_days = 15L;
                    break;
                }
                case 2430077: {
                    mountid = 1040;
                    expiration_days = 15L;
                    break;
                }
                case 2430080: {
                    mountid = 1042;
                    expiration_days = 20L;
                    break;
                }
                case 2430082: {
                    mountid = 1044;
                    expiration_days = 7L;
                    break;
                }
                case 2430260: {
                    mountid = 1044;
                    expiration_days = 3L;
                    break;
                }
                case 2430091: {
                    mountid = 1049;
                    expiration_days = 10L;
                    break;
                }
                case 2430092: {
                    mountid = 1050;
                    expiration_days = 10L;
                    break;
                }
                case 2430263: {
                    mountid = 1050;
                    expiration_days = 3L;
                    break;
                }
                case 2430093: {
                    mountid = 1051;
                    expiration_days = 10L;
                    break;
                }
                case 2430101: {
                    mountid = 1052;
                    expiration_days = 10L;
                    break;
                }
                case 2430102: {
                    mountid = 1053;
                    expiration_days = 10L;
                    break;
                }
                case 2430103: {
                    mountid = 1054;
                    expiration_days = 30L;
                    break;
                }
                case 2430266: {
                    mountid = 1054;
                    expiration_days = 3L;
                    break;
                }
                case 2430265: {
                    mountid = 1151;
                    expiration_days = 3L;
                    break;
                }
                case 2430258: {
                    mountid = 1115;
                    expiration_days = 365L;
                    break;
                }
                case 2430117: {
                    mountid = 1036;
                    expiration_days = 365L;
                    break;
                }
                case 2430118: {
                    mountid = 1039;
                    expiration_days = 365L;
                    break;
                }
                case 2430119: {
                    mountid = 1040;
                    expiration_days = 365L;
                    break;
                }
                case 2430120: {
                    mountid = 1037;
                    expiration_days = 365L;
                    break;
                }
                case 2430271: {
                    mountid = 80001191;
                    expiration_days = 3L;
                    break;
                }
                case 2430149: {
                    mountid = 1072;
                    expiration_days = 30L;
                    break;
                }
                case 2430262: {
                    mountid = 1072;
                    expiration_days = 3L;
                    break;
                }
                case 2430264: {
                    mountid = 1019;
                    expiration_days = 3L;
                    break;
                }
                case 2430179: {
                    mountid = 80001026;
                    expiration_days = 15L;
                    break;
                }
                case 2430283: {
                    mountid = 1025;
                    expiration_days = 10L;
                    break;
                }
                case 2430313: {
                    mountid = 1156;
                    expiration_days = -1L;
                    break;
                }
                case 2430317: {
                    mountid = 1121;
                    expiration_days = -1L;
                    break;
                }
                case 2430319: {
                    mountid = 1122;
                    expiration_days = -1L;
                    break;
                }
                case 2430321: {
                    mountid = 1123;
                    expiration_days = -1L;
                    break;
                }
                case 2430323: {
                    mountid = 1124;
                    expiration_days = -1L;
                    break;
                }
                case 2430325: {
                    mountid = 1129;
                    expiration_days = -1L;
                    break;
                }
                case 2430327: {
                    mountid = 1130;
                    expiration_days = -1L;
                    break;
                }
                case 2430329: {
                    mountid = 1063;
                    expiration_days = -1L;
                    break;
                }
                case 2430331: {
                    mountid = 1025;
                    expiration_days = -1L;
                    break;
                }
                case 2430333: {
                    mountid = 1034;
                    expiration_days = -1L;
                    break;
                }
                case 2430335: {
                    mountid = 1136;
                    expiration_days = -1L;
                    break;
                }
                case 2430337: {
                    mountid = 1051;
                    expiration_days = -1L;
                    break;
                }
                case 2430339: {
                    mountid = 1138;
                    expiration_days = -1L;
                    break;
                }
                case 2430341: {
                    mountid = 1139;
                    expiration_days = -1L;
                    break;
                }
                case 2430343: {
                    mountid = 1027;
                    expiration_days = -1L;
                    break;
                }
                case 2430346: {
                    mountid = 1029;
                    expiration_days = -1L;
                    break;
                }
                case 2430348: {
                    mountid = 1028;
                    expiration_days = -1L;
                    break;
                }
                case 2430350: {
                    mountid = 1033;
                    expiration_days = -1L;
                    break;
                }
                case 2430352: {
                    mountid = 1064;
                    expiration_days = -1L;
                    break;
                }
                case 2430354: {
                    mountid = 1096;
                    expiration_days = -1L;
                    break;
                }
                case 2430356: {
                    mountid = 1101;
                    expiration_days = -1L;
                    break;
                }
                case 2430358: {
                    mountid = 1102;
                    expiration_days = -1L;
                    break;
                }
                case 2430360: {
                    mountid = 1054;
                    expiration_days = -1L;
                    break;
                }
                case 2430362: {
                    mountid = 1053;
                    expiration_days = -1L;
                    break;
                }
                case 2430292: {
                    mountid = 1145;
                    expiration_days = 90L;
                    break;
                }
                case 2430294: {
                    mountid = 1146;
                    expiration_days = 90L;
                    break;
                }
                case 2430296: {
                    mountid = 1147;
                    expiration_days = 90L;
                    break;
                }
                case 2430298: {
                    mountid = 1148;
                    expiration_days = 90L;
                    break;
                }
                case 2430300: {
                    mountid = 1149;
                    expiration_days = 90L;
                    break;
                }
                case 2430302: {
                    mountid = 1150;
                    expiration_days = 90L;
                    break;
                }
                case 2430304: {
                    mountid = 1151;
                    expiration_days = 90L;
                    break;
                }
                case 2430306: {
                    mountid = 1152;
                    expiration_days = 90L;
                    break;
                }
                case 2430308: {
                    mountid = 1153;
                    expiration_days = 90L;
                    break;
                }
                case 2430310: {
                    mountid = 1154;
                    expiration_days = 90L;
                    break;
                }
                case 2430312: {
                    mountid = 1156;
                    expiration_days = 90L;
                    break;
                }
                case 2430314: {
                    mountid = 1156;
                    expiration_days = 90L;
                    break;
                }
                case 2430316: {
                    mountid = 1118;
                    expiration_days = 90L;
                    break;
                }
                case 2430318: {
                    mountid = 1121;
                    expiration_days = 90L;
                    break;
                }
                case 2430320: {
                    mountid = 1122;
                    expiration_days = 90L;
                    break;
                }
                case 2430322: {
                    mountid = 1123;
                    expiration_days = 90L;
                    break;
                }
                case 2430326: {
                    mountid = 1129;
                    expiration_days = 90L;
                    break;
                }
                case 2430328: {
                    mountid = 1130;
                    expiration_days = 90L;
                    break;
                }
                case 2430330: {
                    mountid = 1063;
                    expiration_days = 90L;
                    break;
                }
                case 2430332: {
                    mountid = 1025;
                    expiration_days = 90L;
                    break;
                }
                case 2430334: {
                    mountid = 1034;
                    expiration_days = 90L;
                    break;
                }
                case 2430336: {
                    mountid = 1136;
                    expiration_days = 90L;
                    break;
                }
                case 2430338: {
                    mountid = 1051;
                    expiration_days = 90L;
                    break;
                }
                case 2430340: {
                    mountid = 1138;
                    expiration_days = 90L;
                    break;
                }
                case 2430342: {
                    mountid = 1139;
                    expiration_days = 90L;
                    break;
                }
                case 2430344: {
                    mountid = 1027;
                    expiration_days = 90L;
                    break;
                }
                case 2430347: {
                    mountid = 1029;
                    expiration_days = 90L;
                    break;
                }
                case 2430349: {
                    mountid = 1028;
                    expiration_days = 90L;
                    break;
                }
                case 2430369: {
                    mountid = 1049;
                    expiration_days = 10L;
                    break;
                }
                case 2430392: {
                    mountid = 80001038;
                    expiration_days = 90L;
                    break;
                }
                case 2430232: {
                    mountid = 1106;
                    expiration_days = 10L;
                    break;
                }
                case 2430206: {
                    mountid = 1033;
                    expiration_days = 7L;
                    break;
                }
                case 2430211: {
                    mountid = 80001009;
                    expiration_days = 30L;
                    break;
                }
                case 2430934: {
                    mountid = 1042;
                    expiration_days = 0L;
                    break;
                }
                case 2430458: {
                    mountid = 80001044;
                    expiration_days = 7L;
                    break;
                }
                case 2430521: {
                    mountid = 80001044;
                    expiration_days = 30L;
                    break;
                }
                case 2430506: {
                    mountid = 80001082;
                    expiration_days = 30L;
                    break;
                }
                case 2430507: {
                    mountid = 80001083;
                    expiration_days = 30L;
                    break;
                }
                case 2430508: {
                    mountid = 80001175;
                    expiration_days = 30L;
                    break;
                }
                case 2430518: {
                    mountid = 80001090;
                    expiration_days = 30L;
                    break;
                }
                case 2430908: {
                    mountid = 80001175;
                    expiration_days = 30L;
                    break;
                }
                case 2430927: {
                    mountid = 80001183;
                    expiration_days = 30L;
                    break;
                }
                case 2430727: {
                    mountid = 80001148;
                    expiration_days = 30L;
                    break;
                }
                case 2430938: {
                    mountid = 80001148;
                    expiration_days = 0L;
                    break;
                }
                case 2430937: {
                    mountid = 80001193;
                    expiration_days = 0L;
                    break;
                }
                case 2430939: {
                    mountid = 80001195;
                    expiration_days = 0L;
                    break;
                }
                case 2434290: {
                    chr.gainHonor(10000);
                    chr.removeItem(2434290, -1);
                    break;
                }
                case 2434287: {
                    chr.gainHonor(-10000);
                    chr.gainItem(2432970, 1);
                    chr.removeItem(2434287, -1);
                    break;
                }
                case 2434813: {
                    chr.gainItem(4001852, 1);
                    chr.removeItem(2434813, -1);
                    break;
                }
                case 2434814: {
                    chr.gainItem(4001853, 1);
                    chr.removeItem(2434814, -1);
                    break;
                }
                case 2434815: {
                    chr.gainItem(4001854, 1);
                    chr.removeItem(2434815, -1);
                    break;
                }
                case 2434816: {
                    chr.gainItem(4001862, 1);
                    chr.removeItem(2434816, -1);
                    break;
                }
                case 2435122:
                case 2435513:
                case 2436784:
                case 2439631: {
                    if (!GameConstants.isZero(chr.getJob())) {
                        NPCScriptManager.getInstance().startItem(c, 9010000, "consume_" + toUse.getItemId());
                        break;
                    }
                    chr.dropMessage(5, "\uc81c\ub85c \uc9c1\uc5c5\uad70\uc740 \ub370\ubbf8\uc9c0 \uc2a4\ud0a8\uc744 \uc0ac\uc6a9\ud574\ub3c4 \uc544\ubb34 \ud6a8\uacfc\ub3c4 \uc5bb\uc744 \uc218 \uc5c6\ub2e4.");
                    break;
                }
                case 2432636: {
                    if (!GameConstants.isZero(chr.getJob())) {
                        NPCScriptManager.getInstance().startItem(c, 9010000, "consume_2411020");
                        break;
                    }
                    chr.dropMessage(5, "\uc81c\ub85c \uc9c1\uc5c5\uad70\uc740 \ub370\ubbf8\uc9c0 \uc2a4\ud0a8\uc744 \uc0ac\uc6a9\ud574\ub3c4 \uc544\ubb34 \ud6a8\uacfc\ub3c4 \uc5bb\uc744 \uc218 \uc5c6\ub2e4.");
                    break;
                }
                case 2430469: {
                    chr.gainItem(1122017, (short) 1, false, System.currentTimeMillis() + 604800000L, "\uc815\ub839\uc758 \ud39c\ub358\ud2b8");
                    chr.removeItem(toUse.getItemId(), -1);
                    break;
                }
                case 2434021: {
                    chr.gainHonor(10000);
                    chr.removeItem(toUse.getItemId(), -1);
                    break;
                }
                case 2431174: {
                    chr.gainHonor(Randomizer.rand(1, 50));
                    chr.removeItem(toUse.getItemId(), -1);
                    break;
                }
                default: {
                    NPCScriptManager.getInstance().startItem(c, 9010060, "consume_" + toUse.getItemId());
                    break;
                }
            }
            if (GameConstants.getDSkinNum(toUse.getItemId()) != -1) {
                final MapleQuest quest = MapleQuest.getInstance(7291);
                final MapleQuestStatus queststatus = new MapleQuestStatus(quest, 1);
                final int skinnum = GameConstants.getDSkinNum(toUse.getItemId());
                final String skinString = String.valueOf(skinnum);
                queststatus.setCustomData((skinString == null) ? "0" : skinString);
                chr.updateQuest(queststatus, true);
                chr.setKeyValue(7293, "damage_skin", String.valueOf(toUse.getItemId()));
                chr.dropMessage(5, "\ub370\ubbf8\uc9c0 \uc2a4\ud0a8\uc774 \ubcc0\uacbd\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
                chr.getMap().broadcastMessage(chr, CField.showForeignDamageSkin(chr, skinnum), false);
                chr.updateDamageSkin();
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            }
            if (GameConstants.getRidingNum(toUse.getItemId()) != -1) {
                final int skinnum2 = GameConstants.getRidingNum(toUse.getItemId());
                chr.changeSkillLevel(skinnum2, (byte) 1, (byte) 1);
                chr.dropMessage(5, MapleItemInformationProvider.getInstance().getName(GameConstants.getRidingItemIdbyNum(skinnum2)) + "(\uc774)\uac00 \ub4f1\ub85d \ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            } else if (GameConstants.getMountItemEx(toUse.getItemId()) != 0) {
                final int skillid = GameConstants.getMountItemEx(toUse.getItemId());
                chr.changeSkillLevel(skillid, (byte) 1, (byte) 1);
                chr.dropMessage(-1, MapleItemInformationProvider.getInstance().getName(skillid) + "\uc744(\ub97c) \ud68d\ub4dd \ud558\uc600\uc2b5\ub2c8\ub2e4!!");
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            }
        }
        if (mountid > 0) {
            c.getPlayer().getStat();
            mountid = PlayerStats.getSkillByJob(mountid, c.getPlayer().getJob());
            final int fk = GameConstants.getMountItem(mountid, c.getPlayer());
            if (fk > 0 && mountid < 80001000) {
                for (int j = 80001001; j < 80001999; ++j) {
                    final Skill skill = SkillFactory.getSkill(j);
                    if (skill != null && GameConstants.getMountItem(skill.getId(), c.getPlayer()) == fk) {
                        mountid = j;
                        break;
                    }
                }
            }
            if (c.getPlayer().getSkillLevel(mountid) > 0) {
                c.getPlayer().dropMessage(5, "\uc774\ubbf8 \ud574\ub2f9 \ub77c\uc774\ub529\uc2a4\ud0ac\uc774 \uc788\uc2b5\ub2c8\ub2e4.");
            } else if (SkillFactory.getSkill(mountid) == null) {
                c.getPlayer().dropMessage(5, "\ud574\ub2f9\uc2a4\ud0ac\uc740 \uc5bb\uc73c\uc2e4 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
            } else if (expiration_days > 0L) {
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(mountid), 1, (byte) 1, System.currentTimeMillis() + expiration_days * 24L * 60L * 60L * 1000L);
                c.getPlayer().dropMessage(-1, "[" + SkillFactory.getSkillName(mountid) + "] \uc2a4\ud0ac\uc744 \uc5bb\uc5c8\uc2b5\ub2c8\ub2e4.");
            } else if (expiration_days == 0L) {
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                c.getPlayer().changeSkillLevel(SkillFactory.getSkill(mountid), (byte) 1, (byte) 1);
                c.getPlayer().dropMessage(-1, "[" + SkillFactory.getSkillName(mountid) + "] \uc2a4\ud0ac\uc744 \uc5bb\uc5c8\uc2b5\ub2c8\ub2e4.");
            }
        }
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static final void UseSummonBag(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (!chr.isAlive() || chr.inPVP()) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        slea.readInt();
        final short slot = slea.readShort();
        final int itemId = slea.readInt();
        final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId && (c.getPlayer().getMapId() < 910000000 || c.getPlayer().getMapId() > 910000022)) {
            final Map<String, Integer> toSpawn = MapleItemInformationProvider.getInstance().getEquipStats(itemId);
            if (toSpawn == null) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            MapleMonster ht = null;
            final int type = 0;
            for (final Map.Entry<String, Integer> i : toSpawn.entrySet()) {
                if (i.getKey().startsWith("mob") && Randomizer.nextInt(99) <= i.getValue()) {
                    ht = MapleLifeFactory.getMonster(Integer.parseInt(i.getKey().substring(3)));
                    chr.getMap().spawnMonster_sSack(ht, chr.getPosition(), type);
                }
            }
            if (ht == null) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
        }
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static final void UseCashItem(LittleEndianAccessor slea, MapleClient c) {
        int unk;
        short questid;
        Map<MapleStat, Long> statupdate;
        int skill1;
        List<InnerSkillValueHolder> newValues;
        int j;
        Item item3;
        int mapid;
        Item item2;
        Equip equip2;
        Item item1;
        boolean up;
        int i;
        MapleItemInformationProvider mapleItemInformationProvider2;
        int pos;
        MapleItemInformationProvider mapleItemInformationProvider1;
        int[] forbiddenFaces;
        MapleInventoryType mapleInventoryType2;
        int[] wonderblack;
        MapleInventoryType mapleInventoryType1;
        Equip equip1;
        int[] SLabel;
        MapleInventoryType type;
        Equip toScroll;
        Item item;
        String message;
        int tvType;
        long uniqueId, l1;
        Skill skil;
        long uniqueid;
        String nName;
        MapleItemInformationProvider ii;
        short viewSlot;
        int code, npcid, apto, skill2;
        InnerSkillValueHolder ivholder;
        String effect;
        MapleItemInformationProvider mapleItemInformationProvider4;
        MapleMap target;
        MapleItemInformationProvider mapleItemInformationProvider3;
        short s3;
        int n;
        Item item8;
        int m;
        Item item7;
        int k, arrayOfInt2[];
        boolean bool1;
        int[] arrayOfInt1;
        boolean dressUp;
        Item item6;
        int[] luna;
        Item item5;
        int[] 모자;
        Item item4;
        short s2, s1, dst;
        StringBuilder sb;
        boolean ear;
        long toAdd;
        String[] arrayOfString;
        short descSlot;
        MapleQuest quest;
        int apfrom;
        Skill skillSPTo;
        InnerSkillValueHolder ivholder2;
        int days;
        Item item10;
        boolean bool4;
        Item item9;
        int i2;
        boolean bool3;
        int i1;
        boolean isBeta;
        short baseslot;
        int 한벌남[], flag;
        boolean bool2;
        MapleCharacter victim;
        MaplePet maplePet1;
        int color;
        MaplePet pet;
        Equip view_Item;
        Skill skillSPFrom;
        boolean bool5, isAlphaBeta;
        short usingslot;
        int[] 한벌여;
        Item item11;
        String str1;
        int petIndex;
        MaplePet maplePet2;
        long expire;
        int slo;
        Equip desc_Item;
        PlayerStats playerst;
        int ordinaryColor, baseFace;
        Item baseitem;
        int[] 신발;
        MaplePet.PetFlag zz;
        int i3;
        String str2;
        int addColor, i4;
        Item usingitem;
        int 무기[], i6, i5, basegrade, 망토장갑[], newFace;
        List<Pair<Integer, Integer>> random;
        MapleItemInformationProvider mapleItemInformationProvider5;
        boolean event;
        int baseitemid;
        boolean sp;
        short baseitempos;
        MaplePet maplePet3;
        int useitemid;
        Item item12;
        Equip equip3;
        short useitempos;
        int i7;
        Equip equip4;
        int itemidselect;
        Equip equip5;
        int 마라벨나올확률, MItemidselect, Label, own, two, three;
        boolean 마라벨인가;
        /* 2487 */ if (c.getPlayer() == null || c.getPlayer().getMap() == null || c.getPlayer().inPVP()) {
            /* 2488 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        /* 2491 */ slea.readInt();
        /* 2492 */ c.getPlayer().setScrolledPosition((short) 0);
        /* 2493 */ short slot = slea.readShort();
        /* 2494 */ int itemId = slea.readInt();
        /* 2495 */ if (itemId == 5064000 || itemId == 5064100 || itemId == 5064300 || itemId == 5064400) {
            /* 2496 */ slea.readShort();
        }
        /* 2498 */ Item toUse = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(slot);
        /* 2499 */ if ((toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) && itemId != 5153015 && itemId != 5150132 && itemId != 5152020) {
            /* 2500 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));

            return;
        }
        /* 2504 */ boolean used = false, cc = false;
        /* 2505 */ MapleItemInformationProvider ii3 = MapleItemInformationProvider.getInstance();
        /* 2506 */ if (itemId != 5062009 && itemId != 5062010 && itemId != 5062500 && itemId != 5153015 && itemId != 5150132 && itemId != 5152020) {
            /* 2507 */ FileoutputUtil.log(FileoutputUtil.아이템사용로그, "[캐시 아이템 사용] 계정 아이디 : " + c.getAccID() + " | " + c.getPlayer().getName() + "이 " + ii3.getName(toUse.getItemId()) + "(" + toUse.getItemId() + ")를 사용함.");
        }
        /* 2509 */ switch (itemId) {
            case 5150132:
            case 5152020:
            case 5153015:
                /* 2513 */ unk = slea.readInt();
                /* 2514 */ slea.skip(2);
                /* 2515 */ code = slea.readInt();
                /* 2516 */ if (itemId == 5150132) {

                    /* 2518 */ if (c.getPlayer().getDressup() || (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1)) {
                        /* 2519 */ c.getPlayer().setSecondHair(code);
                        /* 2520 */ if (c.getPlayer().getDressup()) {
                            /* 2521 */ c.getPlayer().updateAngelicStats();
                        } else {
                            /* 2523 */ c.send(CWvsContext.updateZeroSecondStats(c.getPlayer()));
                        }
                    } else {
                        /* 2526 */ c.getPlayer().setHair(code);
                        /* 2527 */ c.getPlayer().updateSingleStat(MapleStat.HAIR, code);
                    }
                    /* 2529 */ c.getPlayer().equipChanged();
                    /* 2530 */                } else if (itemId == 5153015) {

                    /* 2532 */ if (c.getPlayer().getDressup() || (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1)) {
                        /* 2533 */ c.getPlayer().setSecondSkinColor((byte) code);
                        /* 2534 */ if (c.getPlayer().getDressup()) {
                            /* 2535 */ c.getPlayer().updateAngelicStats();
                        } else {
                            /* 2537 */ c.send(CWvsContext.updateZeroSecondStats(c.getPlayer()));
                        }
                    } else {
                        /* 2540 */ c.getPlayer().setSkinColor((byte) code);
                        /* 2541 */ c.getPlayer().updateSingleStat(MapleStat.SKIN, code);
                    }
                    /* 2543 */ c.getPlayer().equipChanged();
                    /* 2544 */                } else if (itemId == 5152020) {

                    /* 2546 */ if (c.getPlayer().getDressup() || (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1)) {
                        /* 2547 */ c.getPlayer().setSecondFace(code);
                        /* 2548 */ if (c.getPlayer().getDressup()) {
                            /* 2549 */ c.getPlayer().updateAngelicStats();
                        } else {
                            /* 2551 */ c.send(CWvsContext.updateZeroSecondStats(c.getPlayer()));
                        }
                    } else {
                        /* 2554 */ c.getPlayer().setFace(code);
                        /* 2555 */ c.getPlayer().updateSingleStat(MapleStat.FACE, code);
                    }
                    /* 2557 */ c.getPlayer().equipChanged();
                }
                /* 2559 */ c.getPlayer().dropMessage(5, "성공적으로 변경 되었습니다.");
                /* 2560 */ c.send(CWvsContext.enableActions(c.getPlayer()));
                return;

            case 5043000:
            case 5043001:
                /* 2565 */ questid = slea.readShort();
                /* 2566 */ npcid = slea.readInt();
                /* 2567 */ quest = MapleQuest.getInstance(questid);

                /* 2569 */ if (c.getPlayer().getQuest(quest).getStatus() == 1 && quest.canComplete(c.getPlayer(), Integer.valueOf(npcid))) {
                    /* 2570 */ int mapId = MapleLifeFactory.getNPCLocation(npcid);
                    /* 2571 */ if (mapId != -1) {
                        /* 2572 */ MapleMap map = c.getChannelServer().getMapFactory().getMap(mapId);
                        /* 2573 */ if (map.containsNPC(npcid) && !FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()) && !FieldLimitType.VipRock.check(map.getFieldLimit())) {
                            /* 2574 */ c.getPlayer().changeMap(map, map.getPortal(0));
                        }
                        /* 2576 */ used = true;
                        break;
                    }
                    /* 2578 */ c.getPlayer().dropMessage(1, "Unknown error has occurred.");
                }
                break;

            case 2320000:
            case 5040000:
            case 5040001:
            case 5040002:
            case 5040003:
            case 5040004:
            case 5041000:
            case 5041001:
                /* 2591 */ used = UseTeleRock(slea, c, itemId);
                break;

            case 5450005:
                /* 2595 */ c.getPlayer().setConversation(4);
                /* 2596 */ c.getPlayer().getStorage().sendStorage(c, 1022005);
                break;

            case 5050000:
                /* 2600 */ statupdate = new EnumMap<>(MapleStat.class);
                /* 2601 */ apto = slea.readInt();
                /* 2602 */ apfrom = slea.readInt();

                /* 2604 */ if (apto == apfrom) {
                    break;
                }
                /* 2607 */ int job = c.getPlayer().getJob();
                /* 2608 */ playerst = c.getPlayer().getStat();
                /* 2609 */ used = true;

                /* 2611 */ switch (apto) {
                    case 64:
                        /* 2613 */ if (playerst.getStr() >= 999) {
                            /* 2614 */ used = false;
                        }
                        break;
                    case 128:
                        /* 2618 */ if (playerst.getDex() >= 999) {
                            /* 2619 */ used = false;
                        }
                        break;
                    case 256:
                        /* 2623 */ if (playerst.getInt() >= 999) {
                            /* 2624 */ used = false;
                        }
                        break;
                    case 512:
                        /* 2628 */ if (playerst.getLuk() >= 999) {
                            /* 2629 */ used = false;
                        }
                        break;
                    case 2048:
                        /* 2633 */ if (playerst.getMaxHp() >= 500000L) {
                            /* 2634 */ used = false;
                        }
                        break;
                    case 8192:
                        /* 2638 */ if (playerst.getMaxMp() >= 500000L) {
                            /* 2639 */ used = false;
                        }
                        break;
                }
                /* 2643 */ switch (apfrom) {
                    case 64:
                        /* 2645 */ if (playerst.getStr() <= 4 || (c.getPlayer().getJob() % 1000 / 100 == 1 && playerst.getStr() <= 35)) {
                            /* 2646 */ used = false;
                        }
                        break;
                    case 128:
                        /* 2650 */ if (playerst.getDex() <= 4 || (c.getPlayer().getJob() % 1000 / 100 == 3 && playerst.getDex() <= 25) || (c.getPlayer().getJob() % 1000 / 100 == 4 && playerst.getDex() <= 25) || (c.getPlayer().getJob() % 1000 / 100 == 5 && playerst.getDex() <= 20)) {
                            /* 2651 */ used = false;
                        }
                        break;
                    case 256:
                        /* 2655 */ if (playerst.getInt() <= 4 || (c.getPlayer().getJob() % 1000 / 100 == 2 && playerst.getInt() <= 20)) {
                            /* 2656 */ used = false;
                        }
                        break;
                    case 512:
                        /* 2660 */ if (playerst.getLuk() <= 4) {
                            /* 2661 */ used = false;
                        }
                        break;
                    case 2048:
                        /* 2665 */ if (c.getPlayer().getHpApUsed() <= 0 || c.getPlayer().getHpApUsed() >= 10000) {
                            /* 2666 */ used = false;
                            /* 2667 */ c.getPlayer().dropMessage(1, "You need points in HP or MP in order to take points out.");
                        }
                        break;
                    case 8192:
                        /* 2671 */ if (c.getPlayer().getHpApUsed() <= 0 || c.getPlayer().getHpApUsed() >= 10000) {
                            /* 2672 */ used = false;
                            /* 2673 */ c.getPlayer().dropMessage(1, "You need points in HP or MP in order to take points out.");
                        }
                        break;
                }
                /* 2677 */ if (used) {
                    /* 2678 */ int i8;
                    long l2;
                    int toSet;
                    long maxhp, maxmp;
                    switch (apto) {
                        case 64:
                            /* 2680 */ i8 = playerst.getStr() + 1;
                            /* 2681 */ playerst.setStr((short) i8, c.getPlayer());
                            /* 2682 */ statupdate.put(MapleStat.STR, Long.valueOf(i8));
                            break;

                        case 128:
                            /* 2686 */ i8 = playerst.getDex() + 1;
                            /* 2687 */ playerst.setDex((short) i8, c.getPlayer());
                            /* 2688 */ statupdate.put(MapleStat.DEX, Long.valueOf(i8));
                            break;

                        case 256:
                            /* 2692 */ i8 = playerst.getInt() + 1;
                            /* 2693 */ playerst.setInt((short) i8, c.getPlayer());
                            /* 2694 */ statupdate.put(MapleStat.INT, Long.valueOf(i8));
                            break;

                        case 512:
                            /* 2698 */ i8 = playerst.getLuk() + 1;
                            /* 2699 */ playerst.setLuk((short) i8, c.getPlayer());
                            /* 2700 */ statupdate.put(MapleStat.LUK, Long.valueOf(i8));
                            break;

                        case 2048:
                            /* 2704 */ l2 = playerst.getMaxHp();
                            /* 2705 */ if (GameConstants.isBeginnerJob(job)) {
                                /* 2706 */ l2 += Randomizer.rand(4, 8);
                                /* 2707 */                            } else if ((job >= 100 && job <= 132) || (job >= 3200 && job <= 3212) || (job >= 1100 && job <= 1112) || (job >= 3100 && job <= 3112)) {
                                /* 2708 */ l2 += Randomizer.rand(36, 42);
                                /* 2709 */                            } else if ((job >= 200 && job <= 232) || GameConstants.isEvan(job) || (job >= 1200 && job <= 1212)) {
                                /* 2710 */ l2 += Randomizer.rand(10, 12);
                                /* 2711 */                            } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 3300 && job <= 3312) || (job >= 2300 && job <= 2312)) {
                                /* 2712 */ l2 += Randomizer.rand(14, 18);
                                /* 2713 */                            } else if ((job >= 510 && job <= 512) || (job >= 1510 && job <= 1512)) {
                                /* 2714 */ l2 += Randomizer.rand(24, 28);
                                /* 2715 */                            } else if ((job >= 500 && job <= 532) || (job >= 3500 && job <= 3512) || job == 1500) {
                                /* 2716 */ l2 += Randomizer.rand(16, 20);
                                /* 2717 */                            } else if (job >= 2000 && job <= 2112) {
                                /* 2718 */ l2 += Randomizer.rand(34, 38);
                            } else {
                                /* 2720 */ l2 += Randomizer.rand(50, 100);
                            }
                            /* 2722 */ l2 = Math.min(500000L, Math.abs(l2));
                            /* 2723 */ c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() + 1));
                            /* 2724 */ playerst.setMaxHp(l2, c.getPlayer());
                            /* 2725 */ statupdate.put(MapleStat.MAXHP, Long.valueOf(l2));
                            break;

                        case 8192:
                            /* 2729 */ maxmp = playerst.getMaxMp();

                            /* 2731 */ if (GameConstants.isBeginnerJob(job)) /* 2732 */ {
                                maxmp += Randomizer.rand(6, 8);
                            } /* 2733 */ else {
                                if (job >= 3100 && job <= 3112) {
                                    break;
                                }
                                /* 2735 */ if ((job >= 100 && job <= 132) || (job >= 1100 && job <= 1112) || (job >= 2000 && job <= 2112)) {
                                    /* 2736 */ maxmp += Randomizer.rand(4, 9);
                                    /* 2737 */                                } else if ((job >= 200 && job <= 232) || GameConstants.isEvan(job) || (job >= 3200 && job <= 3212) || (job >= 1200 && job <= 1212)) {
                                    /* 2738 */ maxmp += Randomizer.rand(32, 36);
                                    /* 2739 */                                } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 500 && job <= 532) || (job >= 3200 && job <= 3212) || (job >= 3500 && job <= 3512) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 1500 && job <= 1512) || (job >= 2300 && job <= 2312)) {
                                    /* 2740 */ maxmp += Randomizer.rand(8, 10);
                                } else {
                                    /* 2742 */ maxmp += Randomizer.rand(50, 100);
                                }
                            }
                            /* 2744 */ maxmp = Math.min(500000L, Math.abs(maxmp));
                            /* 2745 */ c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() + 1));
                            /* 2746 */ playerst.setMaxMp(maxmp, c.getPlayer());
                            /* 2747 */ statupdate.put(MapleStat.MAXMP, Long.valueOf(maxmp));
                            break;
                    }
                    /* 2750 */ switch (apfrom) {
                        case 64:
                            /* 2752 */ toSet = playerst.getStr() - 1;
                            /* 2753 */ playerst.setStr((short) toSet, c.getPlayer());
                            /* 2754 */ statupdate.put(MapleStat.STR, Long.valueOf(toSet));
                            break;

                        case 128:
                            /* 2758 */ toSet = playerst.getDex() - 1;
                            /* 2759 */ playerst.setDex((short) toSet, c.getPlayer());
                            /* 2760 */ statupdate.put(MapleStat.DEX, Long.valueOf(toSet));
                            break;

                        case 256:
                            /* 2764 */ toSet = playerst.getInt() - 1;
                            /* 2765 */ playerst.setInt((short) toSet, c.getPlayer());
                            /* 2766 */ statupdate.put(MapleStat.INT, Long.valueOf(toSet));
                            break;

                        case 512:
                            /* 2770 */ toSet = playerst.getLuk() - 1;
                            /* 2771 */ playerst.setLuk((short) toSet, c.getPlayer());
                            /* 2772 */ statupdate.put(MapleStat.LUK, Long.valueOf(toSet));
                            break;

                        case 2048:
                            /* 2776 */ maxhp = playerst.getMaxHp();
                            /* 2777 */ if (GameConstants.isBeginnerJob(job)) {
                                /* 2778 */ maxhp -= 12L;
                                /* 2779 */                            } else if ((job >= 200 && job <= 232) || (job >= 1200 && job <= 1212)) {
                                /* 2780 */ maxhp -= 10L;
                                /* 2781 */                            } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 3300 && job <= 3312) || (job >= 3500 && job <= 3512) || (job >= 2300 && job <= 2312)) {
                                /* 2782 */ maxhp -= 15L;
                                /* 2783 */                            } else if ((job >= 500 && job <= 532) || (job >= 1500 && job <= 1512)) {
                                /* 2784 */ maxhp -= 22L;
                                /* 2785 */                            } else if ((job >= 100 && job <= 132) || (job >= 1100 && job <= 1112) || (job >= 3100 && job <= 3112)) {
                                /* 2786 */ maxhp -= 32L;
                                /* 2787 */                            } else if ((job >= 2000 && job <= 2112) || (job >= 3200 && job <= 3212)) {
                                /* 2788 */ maxhp -= 40L;
                            } else {
                                /* 2790 */ maxhp -= 20L;
                            }
                            /* 2792 */ c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() - 1));
                            /* 2793 */ playerst.setMaxHp(maxhp, c.getPlayer());
                            /* 2794 */ statupdate.put(MapleStat.MAXHP, Long.valueOf(maxhp));
                            break;
                        case 8192:
                            /* 2797 */ maxmp = playerst.getMaxMp();
                            /* 2798 */ if (GameConstants.isBeginnerJob(job)) /* 2799 */ {
                                maxmp -= 8L;
                            } /* 2800 */ else {
                                if (job >= 3100 && job <= 3112) {
                                    break;
                                }
                                /* 2802 */ if ((job >= 100 && job <= 132) || (job >= 1100 && job <= 1112)) {
                                    /* 2803 */ maxmp -= 4L;
                                    /* 2804 */                                } else if ((job >= 200 && job <= 232) || (job >= 1200 && job <= 1212)) {
                                    /* 2805 */ maxmp -= 30L;
                                    /* 2806 */                                } else if ((job >= 500 && job <= 532) || (job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 1500 && job <= 1512) || (job >= 3300 && job <= 3312) || (job >= 3500 && job <= 3512) || (job >= 2300 && job <= 2312)) {
                                    /* 2807 */ maxmp -= 10L;
                                    /* 2808 */                                } else if (job >= 2000 && job <= 2112) {
                                    /* 2809 */ maxmp -= 5L;
                                } else {
                                    /* 2811 */ maxmp -= 20L;
                                }
                            }
                            /* 2813 */ c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() - 1));
                            /* 2814 */ playerst.setMaxMp(maxmp, c.getPlayer());
                            /* 2815 */ statupdate.put(MapleStat.MAXMP, Long.valueOf(maxmp));
                            break;
                    }
                    /* 2818 */ c.getSession().writeAndFlush(CWvsContext.updatePlayerStats(statupdate, false, c.getPlayer()));
                }
                break;

            case 5050001:
            case 5050002:
            case 5050003:
            case 5050004:
            case 5050005:
            case 5050006:
            case 5050007:
            case 5050008:
            case 5050009:
                /* 2831 */ if (itemId >= 5050005 && !GameConstants.isEvan(c.getPlayer().getJob())) {
                    /* 2832 */ c.getPlayer().dropMessage(1, "This reset is only for Evans.");
                    break;
                }
                /* 2835 */ if (itemId < 5050005 && GameConstants.isEvan(c.getPlayer().getJob())) {
                    /* 2836 */ c.getPlayer().dropMessage(1, "This reset is only for non-Evans.");
                    break;
                }
                /* 2839 */ skill1 = slea.readInt();
                /* 2840 */ skill2 = slea.readInt();
                /* 2841 */ for (int i8 : GameConstants.blockedSkills) {
                    /* 2842 */ if (skill1 == i8) {
                        /* 2843 */ c.getPlayer().dropMessage(1, "You may not add this skill.");

                        return;
                    }
                }
                /* 2848 */ skillSPTo = SkillFactory.getSkill(skill1);
                /* 2849 */ skillSPFrom = SkillFactory.getSkill(skill2);

                /* 2851 */ if (skillSPTo.isBeginnerSkill() || skillSPFrom.isBeginnerSkill()) {
                    /* 2852 */ c.getPlayer().dropMessage(1, "You may not add beginner skills.");
                    break;
                }
                /* 2855 */ if (GameConstants.getSkillBookForSkill(skill1) != GameConstants.getSkillBookForSkill(skill2)) {
                    /* 2856 */ c.getPlayer().dropMessage(1, "You may not add different job skills.");

                    break;
                }

                /* 2863 */ if (c.getPlayer().getSkillLevel(skillSPTo) + 1 <= skillSPTo.getMaxLevel() && c.getPlayer().getSkillLevel(skillSPFrom) > 0 && skillSPTo.canBeLearnedBy(c.getPlayer())) {
                    /* 2864 */ if (skillSPTo.isFourthJob() && c.getPlayer().getSkillLevel(skillSPTo) + 1 > c.getPlayer().getMasterLevel(skillSPTo)) {
                        /* 2865 */ c.getPlayer().dropMessage(1, "You will exceed the master level.");
                        break;
                    }
                    /* 2868 */ if (itemId >= 5050005) {
                        /* 2869 */ if (GameConstants.getSkillBookForSkill(skill1) != (itemId - 5050005) * 2 && GameConstants.getSkillBookForSkill(skill1) != (itemId - 5050005) * 2 + 1) {
                            /* 2870 */ c.getPlayer().dropMessage(1, "You may not add this job SP using this reset.");
                            break;
                        }
                    } else {
                        /* 2874 */ int theJob = GameConstants.getJobNumber(skill2);
                        /* 2875 */ switch (skill2 / 10000) {
                            case 430:
                                /* 2877 */ theJob = 1;
                                break;
                            case 431:
                            case 432:
                                /* 2881 */ theJob = 2;
                                break;
                            case 433:
                                /* 2884 */ theJob = 3;
                                break;
                            case 434:
                                /* 2887 */ theJob = 4;
                                break;
                        }
                        /* 2890 */ if (theJob != itemId - 5050000) {
                            /* 2891 */ c.getPlayer().dropMessage(1, "You may not subtract from this skill. Use the appropriate SP reset.");
                            break;
                        }
                    }
                    /* 2895 */ Map<Skill, SkillEntry> sa = new HashMap<>();
                    /* 2896 */ sa.put(skillSPFrom, new SkillEntry((byte) (c.getPlayer().getSkillLevel(skillSPFrom) - 1), c.getPlayer().getMasterLevel(skillSPFrom), SkillFactory.getDefaultSExpiry(skillSPFrom)));
                    /* 2897 */ sa.put(skillSPTo, new SkillEntry((byte) (c.getPlayer().getSkillLevel(skillSPTo) + 1), c.getPlayer().getMasterLevel(skillSPTo), SkillFactory.getDefaultSExpiry(skillSPTo)));
                    /* 2898 */ c.getPlayer().changeSkillsLevel(sa);
                    /* 2899 */ used = true;
                }
                break;

            case 5062800:
            case 5062801:
                /* 2905 */ newValues = new LinkedList<>();
                /* 2906 */ ivholder = null;
                /* 2907 */ ivholder2 = null;
                /* 2908 */ for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills()) {
                    /* 2909 */ if (ivholder == null) {
                        /* 2910 */ int nowrank = -1;
                        /* 2911 */ int rand = Randomizer.nextInt(100);
                        /* 2912 */ if (isvh.getRank() == 3) {
                            /* 2913 */ nowrank = 3;
                            /* 2914 */                        } else if (isvh.getRank() == 2) {
                            /* 2915 */ if (rand < 5) {
                                /* 2916 */ nowrank = 3;
                            } else {
                                /* 2918 */ nowrank = 2;
                            }
                            /* 2920 */                        } else if (isvh.getRank() == 1) {
                            /* 2921 */ if (rand < 10) {
                                /* 2922 */ nowrank = 2;
                            } else {
                                /* 2924 */ nowrank = 1;
                            }
                            /* 2926 */                        } else if (rand < 40) {
                            /* 2927 */ nowrank = 1;
                        } else {
                            /* 2929 */ nowrank = 0;
                        }
                        /* 2931 */ ivholder = InnerAbillity.getInstance().renewSkill(nowrank, true);
                        /* 2932 */ while (isvh.getSkillId() == ivholder.getSkillId()) {
                            /* 2933 */ ivholder = InnerAbillity.getInstance().renewSkill(nowrank, true);
                        }
                        /* 2935 */ newValues.add(ivholder);
                        continue;
                        /* 2936 */                    }
                    if (ivholder2 == null) {
                        /* 2937 */ ivholder2 = InnerAbillity.getInstance().renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), true);
                        /* 2938 */ while (isvh.getSkillId() == ivholder2.getSkillId() || ivholder.getSkillId() == ivholder2.getSkillId()) {
                            /* 2939 */ ivholder2 = InnerAbillity.getInstance().renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), true);
                        }
                        /* 2941 */ newValues.add(ivholder2);
                        continue;
                    }
                    /* 2943 */ InnerSkillValueHolder ivholder3 = InnerAbillity.getInstance().renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), true);
                    /* 2944 */ while (isvh.getSkillId() == ivholder3.getSkillId() || ivholder.getSkillId() == ivholder3.getSkillId() || ivholder2.getSkillId() == ivholder3.getSkillId()) {
                        /* 2945 */ ivholder3 = InnerAbillity.getInstance().renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), true);
                    }
                    /* 2947 */ newValues.add(ivholder3);
                }

                /* 2950 */ (c.getPlayer()).innerCirculator = newValues;
                /* 2951 */ c.getSession().writeAndFlush(CWvsContext.MiracleCirculator(newValues, itemId));
                /* 2952 */ used = true;
                break;

            case 5060048:
                /* 2956 */ if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() > 0
                        && /* 2957 */ c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 2
                        && /* 2958 */ c.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot() > 0
                        && /* 2959 */ c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() > 0) {
                    /* 2960 */ boolean fromSpecial = Randomizer.isSuccess(ServerConstants.SgoldappleSuc);

                    /* 2962 */ List<Triple<Integer, Integer, Integer>> list = ServerConstants.goldapple;
                    /* 2963 */ if (fromSpecial) {
                        /* 2964 */ list = ServerConstants.Sgoldapple;
                    }

                    /* 2967 */ int itemid = 0, count = 0;
                    /* 2968 */ double bestValue = Double.MAX_VALUE;

                    /* 2970 */ for (Triple<Integer, Integer, Integer> element : list) {
                        /* 2971 */ double a = ((Integer) element.getRight()).intValue();
                        /* 2972 */ double r = a / 10000.0D;
                        /* 2973 */ double value = -Math.log(Randomizer.nextDouble()) / r;
                        /* 2974 */ if (value < bestValue) {
                            /* 2975 */ bestValue = value;
                            /* 2976 */ itemid = ((Integer) element.getLeft()).intValue();
                            /* 2977 */ count = ((Integer) element.getMid()).intValue();
                        }
                    }
                    /* 2980 */ if (itemid > 0 && count > 0) {
                        /* 2981 */ Item item13 = new Item(itemid, (short) 0, (short) count);
                        MapleItemInformationProvider mapleItemInformationProvider = MapleItemInformationProvider.getInstance();

                        /* 2983 */ if (GameConstants.isPet(itemid)) {
                            /* 2984 */ item13 = MapleInventoryManipulator.addId_Item(c, itemid, (short) 1, "", MaplePet.createPet(itemid, -1L), 30L, "", false);
                            break;
                        }
                        if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
                            item13 = (Equip) mapleItemInformationProvider.getEquipById(itemid);
                        }
                        /* 2991 */ if (MapleItemInformationProvider.getInstance().isCash(itemid)) {
                            /* 2992 */ item13.setUniqueId(MapleInventoryIdentifier.getInstance());
                        }

                        /* 2998 */ MapleInventoryManipulator.addbyItem(c, item13);
                        /* 2999 */ c.getSession().writeAndFlush(CWvsContext.goldApple(item13, toUse));
                        /* 3000 */ c.getPlayer().gainItem(2435458, 1);

                        /* 3002 */ used = true;
                        /* 3003 */ if (fromSpecial) {
                            /* 3004 */ World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(), "", c.getPlayer().getName() + "님이 골드애플에서 {} 을 획득하였습니다.", true, item13));
                        }
                    }
                }
                break;

            case 5155000:
            case 5155004:
            case 5155005:
                /* 3017 */ j = slea.readInt();
                /* 3018 */ effect = "";
                /* 3019 */ switch (j) {
                    case 0:
                        /* 3021 */ if (GameConstants.isMercedes(c.getPlayer().getJob())) {
                            /* 3022 */ j = 1;
                        }
                        /* 3024 */ if (GameConstants.isIllium(c.getPlayer().getJob())) {
                            /* 3025 */ j = 2;
                        }
                        /* 3027 */ if (GameConstants.isAdel(c.getPlayer().getJob()) || GameConstants.isArk(c.getPlayer().getJob())) {
                            /* 3028 */ j = 3;
                        }
                        /* 3030 */ effect = "Effect/BasicEff.img/JobChanged";
                        break;
                    case 1:
                        /* 3033 */ if (GameConstants.isMercedes(c.getPlayer().getJob())) {
                            /* 3034 */ j = 0;
                        }
                        /* 3036 */ effect = "Effect/BasicEff.img/JobChangedElf";
                        break;
                    case 2:
                        /* 3039 */ if (GameConstants.isIllium(c.getPlayer().getJob())) {
                            /* 3040 */ j = 0;
                        }
                        /* 3042 */ effect = "Effect/BasicEff.img/JobChangedIlliumFront";
                        break;
                    case 3:
                        /* 3045 */ if (GameConstants.isAdel(c.getPlayer().getJob()) || GameConstants.isArk(c.getPlayer().getJob())) {
                            /* 3046 */ j = 0;
                        }
                        /* 3048 */ effect = "Effect/BasicEff.img/JobChangedArkFront";
                        break;
                }
                /* 3051 */ c.getPlayer().setKeyValue(7784, "sw", String.valueOf(j));
                /* 3052 */ c.getSession().writeAndFlush(CField.EffectPacket.showEffect(c.getPlayer(), 0, itemId, 38, 2, 0, (byte) 0, true, null, effect, null));
                /* 3053 */ used = true;
                break;

            case 5155001:
                /* 3057 */ if (GameConstants.isKaiser(c.getPlayer().getJob())) {
                    /* 3058 */ if (c.getPlayer().getKeyValue(7786, "sw") == 0L) {
                        /* 3059 */ c.getPlayer().setKeyValue(7786, "sw", "1");
                    } else {
                        /* 3061 */ c.getPlayer().setKeyValue(7786, "sw", "0");
                    }
                    /* 3063 */ c.getPlayer().dropMessage(5, "드래곤 테일 쉬프트의 신비로운 힘으로 모습이 바뀌었습니다.");
                    /* 3064 */ used = true;
                    break;
                }
                /* 3066 */ c.getPlayer().dropMessage(5, "드래곤 테일 쉬프트는 카이저에게만 효과가 있는것 같다.");
                break;

            case 5500000:
                /* 3071 */ item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
                /* 3072 */ mapleItemInformationProvider4 = MapleItemInformationProvider.getInstance();
                /* 3073 */ days = 1;
                /* 3074 */ if (item3 != null && !GameConstants.isAccessory(item3.getItemId()) && item3.getExpiration() > -1L && !mapleItemInformationProvider4.isCash(item3.getItemId()) && System.currentTimeMillis() + 8640000000L > item3.getExpiration() + 86400000L) {
                    /* 3075 */ boolean change = true;
                    /* 3076 */ for (String z : GameConstants.RESERVED) {
                        /* 3077 */ if (c.getPlayer().getName().indexOf(z) != -1 || item3.getOwner().indexOf(z) != -1) {
                            /* 3078 */ change = false;
                        }
                    }
                    /* 3081 */ if (change) {
                        /* 3082 */ item3.setExpiration(item3.getExpiration() + 86400000L);
                        /* 3083 */ c.getPlayer().forceReAddItem(item3, MapleInventoryType.EQUIPPED);
                        /* 3084 */ used = true;
                        break;
                    }
                    /* 3086 */ c.getPlayer().dropMessage(1, "It may not be used on this item.");
                }
                break;

            case 5500001:
                /* 3092 */ item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
                /* 3093 */ mapleItemInformationProvider4 = MapleItemInformationProvider.getInstance();
                /* 3094 */ days = 7;
                /* 3095 */ if (item3 != null && !GameConstants.isAccessory(item3.getItemId()) && item3.getExpiration() > -1L && !mapleItemInformationProvider4.isCash(item3.getItemId()) && System.currentTimeMillis() + 8640000000L > item3.getExpiration() + 604800000L) {
                    /* 3096 */ boolean change = true;
                    /* 3097 */ for (String z : GameConstants.RESERVED) {
                        /* 3098 */ if (c.getPlayer().getName().indexOf(z) != -1 || item3.getOwner().indexOf(z) != -1) {
                            /* 3099 */ change = false;
                        }
                    }
                    /* 3102 */ if (change) {
                        /* 3103 */ item3.setExpiration(item3.getExpiration() + 604800000L);
                        /* 3104 */ c.getPlayer().forceReAddItem(item3, MapleInventoryType.EQUIPPED);
                        /* 3105 */ used = true;
                        break;
                    }
                    /* 3107 */ c.getPlayer().dropMessage(1, "It may not be used on this item.");
                }
                break;

            case 5500002:
                /* 3113 */ item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
                /* 3114 */ mapleItemInformationProvider4 = MapleItemInformationProvider.getInstance();
                /* 3115 */ days = 20;
                /* 3116 */ if (item3 != null && !GameConstants.isAccessory(item3.getItemId()) && item3.getExpiration() > -1L && !mapleItemInformationProvider4.isCash(item3.getItemId()) && System.currentTimeMillis() + 8640000000L > item3.getExpiration() + 1728000000L) {
                    /* 3117 */ boolean change = true;
                    /* 3118 */ for (String z : GameConstants.RESERVED) {
                        /* 3119 */ if (c.getPlayer().getName().indexOf(z) != -1 || item3.getOwner().indexOf(z) != -1) {
                            /* 3120 */ change = false;
                        }
                    }
                    /* 3123 */ if (change) {
                        /* 3124 */ item3.setExpiration(item3.getExpiration() + 1728000000L);
                        /* 3125 */ c.getPlayer().forceReAddItem(item3, MapleInventoryType.EQUIPPED);
                        /* 3126 */ used = true;
                        break;
                    }
                    /* 3128 */ c.getPlayer().dropMessage(1, "It may not be used on this item.");
                }
                break;

            case 5500005:
                /* 3134 */ item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
                /* 3135 */ mapleItemInformationProvider4 = MapleItemInformationProvider.getInstance();
                /* 3136 */ days = 50;
                /* 3137 */ if (item3 != null && !GameConstants.isAccessory(item3.getItemId()) && item3.getExpiration() > -1L && !mapleItemInformationProvider4.isCash(item3.getItemId()) && System.currentTimeMillis() + 8640000000L > item3.getExpiration() + 4320000000L) {
                    /* 3138 */ boolean change = true;
                    /* 3139 */ for (String z : GameConstants.RESERVED) {
                        /* 3140 */ if (c.getPlayer().getName().indexOf(z) != -1 || item3.getOwner().indexOf(z) != -1) {
                            /* 3141 */ change = false;
                        }
                    }
                    /* 3144 */ if (change) {
                        /* 3145 */ item3.setExpiration(item3.getExpiration() + 25032704L);
                        /* 3146 */ c.getPlayer().forceReAddItem(item3, MapleInventoryType.EQUIPPED);
                        /* 3147 */ used = true;
                        break;
                    }
                    /* 3149 */ c.getPlayer().dropMessage(1, "It may not be used on this item.");
                }
                break;

            case 5044000:
            case 5044001:
            case 5044002:
            case 5044006:
            case 5044007:
                /* 3159 */ slea.readByte();
                /* 3160 */ mapid = slea.readInt();
                /* 3161 */ if (mapid == 180000000) {
                    /* 3162 */ c.getPlayer().dropMessage(1, "그곳으로 이동하실 수 없습니다.");
                    /* 3163 */ c.getPlayer().ban("텔레포트월드맵 악용", true, true, true);
                    return;
                }
                /* 3166 */ target = c.getChannelServer().getMapFactory().getMap(mapid);
                /* 3167 */ c.getPlayer().changeMap(target, target.getPortal(0));

                /* 3169 */ if (ItemFlag.KARMA_USE.check(toUse.getFlag())) {
                    /* 3170 */ toUse.setFlag(toUse.getFlag() - ItemFlag.KARMA_USE.getValue() + ItemFlag.UNTRADEABLE.getValue());
                    /* 3171 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.CASH, toUse));
                }
                break;

            case 5500006:
                /* 3176 */ item2 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
                /* 3177 */ mapleItemInformationProvider3 = MapleItemInformationProvider.getInstance();
                /* 3178 */ days = 99;
                /* 3179 */ if (item2 != null && !GameConstants.isAccessory(item2.getItemId()) && item2.getExpiration() > -1L && !mapleItemInformationProvider3.isCash(item2.getItemId()) && System.currentTimeMillis() + 8640000000L > item2.getExpiration() + 8553600000L) {
                    /* 3180 */ boolean change = true;
                    /* 3181 */ for (String z : GameConstants.RESERVED) {
                        /* 3182 */ if (c.getPlayer().getName().indexOf(z) != -1 || item2.getOwner().indexOf(z) != -1) {
                            /* 3183 */ change = false;
                        }
                    }
                    /* 3186 */ if (change) {
                        /* 3187 */ item2.setExpiration(item2.getExpiration() + -36334592L);
                        /* 3188 */ c.getPlayer().forceReAddItem(item2, MapleInventoryType.EQUIPPED);
                        /* 3189 */ used = true;
                        break;
                    }
                    /* 3191 */ c.getPlayer().dropMessage(1, "It may not be used on this item.");
                }
                break;

            case 5064200:
                /* 3198 */ slea.skip(4);
                /* 3199 */ s3 = slea.readShort();
                /* 3200 */ if (s3 < 0) {
                    /* 3201 */ equip2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(s3);
                } else {
                    /* 3203 */ equip2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(s3);
                }
                /* 3205 */ if (equip2 != null) {
                    /* 3206 */ Equip origin = (Equip) MapleItemInformationProvider.getInstance().getEquipById(equip2.getItemId());
                    /* 3207 */ equip2.setAcc(origin.getAcc());
                    /* 3208 */ equip2.setAvoid(origin.getAvoid());
                    /* 3209 */ equip2.setDex(origin.getDex());
                    /* 3210 */ equip2.setHands(origin.getHands());
                    /* 3211 */ equip2.setHp(origin.getHp());
                    /* 3212 */ equip2.setInt(origin.getInt());
                    /* 3213 */ equip2.setJump(origin.getJump());
                    /* 3214 */ equip2.setLevel(origin.getLevel());
                    /* 3215 */ equip2.setLuk(origin.getLuk());
                    /* 3216 */ equip2.setMatk(origin.getMatk());
                    /* 3217 */ equip2.setMdef(origin.getMdef());
                    /* 3218 */ equip2.setMp(origin.getMp());
                    /* 3219 */ equip2.setSpeed(origin.getSpeed());
                    /* 3220 */ equip2.setStr(origin.getStr());
                    /* 3221 */ equip2.setUpgradeSlots(origin.getUpgradeSlots());
                    /* 3222 */ equip2.setWatk(origin.getWatk());
                    /* 3223 */ equip2.setWdef(origin.getWdef());
                    /* 3224 */ equip2.setEnhance((byte) 0);
                    /* 3225 */ equip2.setViciousHammer((byte) 0);
                    /* 3226 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) equip2));
                    /* 3227 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(), Equip.ScrollResult.SUCCESS, cc, toUse.getItemId(), equip2.getItemId()), true);
                    /* 3228 */ used = true;
                    break;
                }
                /* 3230 */ c.getPlayer().dropMessage(1, "널 오류가 발생했습니다. 오류게시판에 어떤아이템을 사용하셨는지 자세히 설명해주세요.");
                break;

            case 5060000:
                /* 3235 */ item1 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
                /* 3236 */ if (item1 != null && item1.getOwner().equals("")) {
                    /* 3237 */ boolean change = true;
                    /* 3238 */ for (String z : GameConstants.RESERVED) {
                        /* 3239 */ if (c.getPlayer().getName().indexOf(z) != -1) {
                            /* 3240 */ change = false;
                        }
                    }
                    /* 3243 */ if (change) {
                        /* 3244 */ item1.setOwner(c.getPlayer().getName());
                        /* 3245 */ c.getPlayer().forceReAddItem(item1, MapleInventoryType.EQUIPPED);
                        /* 3246 */ used = true;
                    }
                }
                break;

            case 5062500:
                /* 3252 */ up = false;
                /* 3253 */ n = slea.readInt();
                /* 3254 */ item10 = c.getPlayer().getInventory((n < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP).getItem((short) n);
                /* 3255 */ if (item10 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    /* 3256 */ Equip equip = (Equip) item10;
                    /* 3257 */ if (equip.getPotential4() <= 0) {
                        /* 3258 */ c.getPlayer().dropMessage(1, "에디셔널 잠재능력이 부여되지 않았습니다.");
                        /* 3259 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    /* 3262 */ if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {

                        /* 3264 */ int level = 0;
                        /* 3265 */ level = (equip.getPotential4() >= 10000) ? (equip.getPotential4() / 10000) : (equip.getPotential4() / 100);
                        /* 3266 */ if (level >= 4) {
                            /* 3267 */ level = 4;
                        }
                        /* 3269 */ int rate = (level == 3) ? 3 : ((level == 2) ? 5 : ((level == 1) ? 10 : 0));
                        /* 3270 */ if (Randomizer.nextInt(100) < rate) {
                            /* 3271 */ up = true;
                            /* 3272 */ level++;
                        }
                        /* 3274 */ int temp = level;
                        /* 3275 */ int a = 0;
                        /* 3276 */ while (temp > 1) {
                            /* 3277 */ if (temp > 1) {
                                /* 3278 */ temp--;
                                /* 3279 */ a++;
                            }
                        }
                        /* 3282 */ equip.setPotential4(EdiCubeOption.getEdiCubePotentialId(item10.getItemId(), level, 1, new int[0]));
                        /* 3283 */ equip.setPotential5(EdiCubeOption.getEdiCubePotentialId(item10.getItemId(), level, 2, new int[0]));
                        /* 3284 */ if (equip.getPotential6() > 0) {
                            /* 3285 */ equip.setPotential6(EdiCubeOption.getEdiCubePotentialId(item10.getItemId(), level, 3, new int[]{equip.getPotential4(), equip.getPotential5()}));
                            /* 3286 */ while (!GameConstants.getPotentialCheck(equip.getPotential6(), equip.getPotential4(), equip.getPotential5())) {
                                /* 3287 */ equip.setPotential6(EdiCubeOption.getEdiCubePotentialId(item10.getItemId(), level, 3, new int[]{equip.getPotential4(), equip.getPotential5()}));
                            }
                        }

                        /* 3291 */ if (GameConstants.isZeroWeapon(equip.getItemId())) {
                            /* 3292 */ Equip zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                            /* 3293 */ zeroequip.setPotential4(equip.getPotential4());
                            /* 3294 */ zeroequip.setPotential5(equip.getPotential5());
                            /* 3295 */ zeroequip.setPotential6(equip.getPotential6());
                            /* 3296 */ if (zeroequip != null) {
                                /* 3297 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) zeroequip));
                            }
                        }

                        /* 3301 */ c.getSession().writeAndFlush(CField.getEditionalCubeStart(c.getPlayer(), item10, up, itemId, c.getPlayer().itemQuantity(toUse.getItemId()) - 1));
                        /* 3302 */ c.getSession().writeAndFlush(CField.showPotentialReset(c.getPlayer().getId(), true, itemId, equip.getItemId()));
                        /* 3303 */ c.getPlayer().forceReAddItem(item10, (n < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP);
                        /* 3304 */ MapleInventoryManipulator.addById(c, 2430915, (short) 1, null, null, 0L, "Reward item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
                        /* 3305 */ used = true;

                        /* 3306 */ c.getPlayer().gainMeso(-GameConstants.getCubeMeso(equip.getItemId()), false);
                        break;
                    }
                    /* 3309 */ c.getPlayer().dropMessage(5, "소비 아이템 여유 공간이 부족하여 잠재능력 재설정을 실패하였습니다.");
                }
                break;

           case 5062503:
                /* 3315 */ i = slea.readInt();
                /* 3316 */ item8 = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) i);
                /* 3317 */ if (GameConstants.isZero(c.getPlayer().getJob()) && item8 == null) {
                    /* 3318 */ item8 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) i);
                }
                /* 3320 */ bool4 = false;
                /* 3321 */ if (item8 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    /* 3322 */ Equip eq = (Equip) item8;

                    /* 3324 */ Equip neq = (Equip) eq.copy();
                    /* 3325 */ c.getPlayer().addKV("lastCube", itemId + "");
                    /* 3326 */ if (neq.getPotential4() >= 0) {
                        /* 3327 */ int level = 0;
                        /* 3328 */ level = (neq.getPotential4() >= 10000) ? (neq.getPotential4() / 10000) : (neq.getPotential4() / 100);
                        /* 3329 */ if (level >= 4) {
                            /* 3330 */ level = 4;
                        }
                        /* 3332 */ int rate = (level == 3) ? 5 : ((level == 2) ? 7 : ((level == 1) ? 12 : 0));
                        /* 3333 */ if (Randomizer.nextInt(100) < rate) {
                            /* 3334 */ bool4 = true;
                            /* 3335 */ level++;
                        }
                        /* 3337 */ neq.setPotential4(EdiCubeOption.getEdiCubePotentialId(item8.getItemId(), level, 1, new int[0]));
                        /* 3338 */ neq.setPotential5(EdiCubeOption.getEdiCubePotentialId(item8.getItemId(), level, 2, new int[0]));
                        /* 3339 */ if (neq.getPotential6() > 0) {
                            /* 3340 */ neq.setPotential6(EdiCubeOption.getEdiCubePotentialId(item8.getItemId(), level, 3, new int[]{neq.getPotential4(), neq.getPotential5()}));
                            /* 3341 */ while (!GameConstants.getPotentialCheck(neq.getPotential6(), neq.getPotential4(), neq.getPotential5())) {
                                /* 3342 */ neq.setPotential6(EdiCubeOption.getEdiCubePotentialId(item8.getItemId(), level, 3, new int[]{neq.getPotential4(), neq.getPotential5()}));
                            }
                        }

                        /* 3346 */ c.getPlayer().gainMeso(-GameConstants.getCubeMeso(neq.getItemId()), false);

                        /* 3348 */ c.getSession().writeAndFlush(CField.getWhiteCubeStart(c.getPlayer(), (Item) neq, bool4, 5062503, c.getPlayer().itemQuantity(toUse.getItemId()) - 1));
                        /* 3349 */ c.getPlayer().getMap().broadcastMessage(CField.getBlackCubeEffect(c.getPlayer().getId(), bool4, 5062503, neq.getItemId()));

                        /* 3351 */ (c.getPlayer()).choicepotential = neq;

                        /* 3353 */ if ((c.getPlayer()).memorialcube == null) {
                            /* 3354 */ (c.getPlayer()).memorialcube = toUse.copy();
                        }
                        /* 3356 */ used = true;
                        break;
                    }
                    /* 3358 */ c.getPlayer().dropMessage(5, "Make sure your equipment has a potential.");
                    break;
                }
                /* 3361 */ c.getPlayer().dropMessage(5, "소비 인벤토리의 공간이 부족하여 잠재 설정을 할 수 없습니다.");
                break;

          case 5062009: { //레드 큐브
                ii = MapleItemInformationProvider.getInstance();
                pos = slea.readInt();
                item = c.getPlayer().getInventory(pos < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP).getItem((short) pos);
                up = false;
                if (item != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    Equip eq = (Equip) item;
                    int rand = Randomizer.nextInt(100);
                    MapleInventoryManipulator.addById(c, 2431893, (short) 1, null, null, 0L, "Reward item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
                    if (eq.getState() == 1 || eq.getState() == 17) {
                        if (rand < 10) {
                            up = true;
                            eq.setState((byte) 18);
                        } else {
                            eq.setState((byte) 17);
                        }
                    } else if (eq.getState() == 18 && !ii.isCash(eq.getItemId())) {
                        if (rand < 5) {
                            up = true;
                            eq.setState((byte) 19);
                        } else {
                            eq.setState((byte) 18);
                        }
                    } else if (eq.getState() == 19) {
                        if (rand < 3) {
                            up = true;
                            eq.setState((byte) 20);
                        } else {
                            eq.setState((byte) 19);
                        }
                    }
                    int level = eq.getState() - 16;
                    eq.setPotential1(potential(item.getItemId(), level));
                    eq.setPotential2(potential(item.getItemId(), ((level == 1) || (Randomizer.nextInt(100) < 2)) ? level : (level - 1)));
                    eq.setPotential3(eq.getPotential3() != 0 ? potential(item.getItemId(), ((level == 1) || (Randomizer.nextInt(100) < 1)) ? level : (level - 1)) : 0);
                    eq.setLines((byte) (eq.getPotential3() > 0 ? 3 : 2));
                    c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);

                    if (GameConstants.isZeroWeapon(eq.getItemId())) {
                        Equip zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                        zeroequip.setState(eq.getState());
                        zeroequip.setLines(eq.getLines());
                        zeroequip.setPotential1(eq.getPotential1());
                        zeroequip.setPotential2(eq.getPotential2());
                        zeroequip.setPotential3(eq.getPotential3());
                        if (zeroequip != null) {
                            c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, zeroequip));
                        }
                    }

                    c.getPlayer().forceReAddItem(item, pos < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP);
                    c.getSession().writeAndFlush(CField.showPotentialReset(c.getPlayer().getId(), true, itemId, eq.getItemId()));
                    c.getSession().writeAndFlush(CField.getRedCubeStart(c.getPlayer(), item, up, itemId, c.getPlayer().itemQuantity(toUse.getItemId()) - 1));
//                    c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "소비 인벤토리의 공간이 부족하여 잠재 설정을 할 수 없습니다.");
                }
                break;
            }
          
             case 5062010:
                /* 3517 */ mapleItemInformationProvider1 = MapleItemInformationProvider.getInstance();
                /* 3518 */ k = slea.readInt();
                /* 3519 */ item9 = c.getPlayer().getInventory((k < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP).getItem((short) k);
                /* 3520 */ bool5 = false;
                /* 3521 */ if (item9 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    /* 3522 */ Equip eq = (Equip) item9;
                    /* 3523 */ int rand = Randomizer.nextInt(100);
                    /* 3524 */ MapleInventoryManipulator.addById(c, 2431894, (short) 1, null, null, 0L, "");
                    /* 3525 */ c.getPlayer().addKV("lastCube", itemId + "");
                    /* 3526 */ Equip neq = (Equip) eq.copy();
                    /* 3527 */ if (neq.getState() >= 17) {
                        /* 3528 */ if (neq.getState() == 1 || neq.getState() == 17) {
                            /* 3529 */ if (rand < 10) {
                                /* 3530 */ bool5 = true;
                                /* 3531 */ neq.setState((byte) 18);
                            } else {
                                /* 3533 */ neq.setState((byte) 17);
                            }
                            /* 3535 */                        } else if (neq.getState() == 18 && !mapleItemInformationProvider1.isCash(neq.getItemId())) {
                            /* 3536 */ if (rand < 5) {
                                /* 3537 */ bool5 = true;
                                /* 3538 */ neq.setState((byte) 19);
                            } else {
                                /* 3540 */ neq.setState((byte) 18);
                            }
                            /* 3542 */                        } else if (neq.getState() == 19) {
                            /* 3543 */ if (rand < 3) {
                                /* 3544 */ bool5 = true;
                                /* 3545 */ neq.setState((byte) 20);
                            } else {
                                /* 3547 */ neq.setState((byte) 19);
                            }
                        }
                        /* 3550 */ int level = neq.getState() - 16;
                        /* 3551 */ neq.setPotential1(CubeOption.getBlackCubePotentialId(item9.getItemId(), level, 1, new int[0]));
                        /* 3552 */ neq.setPotential2(CubeOption.getBlackCubePotentialId(item9.getItemId(), level, 2, new int[0]));
                        /* 3553 */ if (neq.getPotential3() > 0) {
                            /* 3554 */ neq.setPotential3(CubeOption.getBlackCubePotentialId(item9.getItemId(), level, 3, new int[]{neq.getPotential1(), neq.getPotential2()}));
                            /* 3555 */ while (!GameConstants.getPotentialCheck(neq.getPotential3(), neq.getPotential1(), neq.getPotential2())) {
                                /* 3556 */ neq.setPotential3(CubeOption.getBlackCubePotentialId(item9.getItemId(), level, 3, new int[]{neq.getPotential1(), neq.getPotential2()}));
                            }
                        }

                        /* 3560 */ neq.setLines((byte) ((neq.getPotential3() > 0) ? 3 : 2));
                        /* 3561 */ c.getPlayer().gainMeso(-GameConstants.getCubeMeso(neq.getItemId()), false);

                        /* 3563 */ c.getSession().writeAndFlush(CField.getBlackCubeStart(c.getPlayer(), (Item) neq, bool5, 5062010, toUse.getPosition(), c.getPlayer().itemQuantity(toUse.getItemId()) - 1));
                        /* 3564 */ c.getPlayer().getMap().broadcastMessage(CField.getBlackCubeEffect(c.getPlayer().getId(), bool5, 5062010, neq.getItemId()));

                        /* 3566 */ (c.getPlayer()).choicepotential = neq;

                        /* 3568 */ if ((c.getPlayer()).memorialcube == null) {
                            /* 3569 */ (c.getPlayer()).memorialcube = toUse.copy();
                        }
                        /* 3571 */ used = true;
                        break;
                    }
                    /* 3573 */ c.getPlayer().dropMessage(5, "Make sure your equipment has a potential.");
                    break;
                }
                /* 3576 */ c.getPlayer().dropMessage(5, "소비 인벤토리의 공간이 부족하여 잠재 설정을 할 수 없습니다.");
                break;
                

            case 5062005://어메이징 미라클 큐브
                /* 3432 */ pos = slea.readInt();
                /* 3433 */ item7 = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) pos);
                /* 3434 */ if (GameConstants.isZero(c.getPlayer().getJob()) && item7 == null) {
                    /* 3435 */ item7 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) pos);
                }
                    MapleInventoryManipulator.addById(c, 2430759, (short) 1, null, null, 0, "");
                /* 3437 */ if (item7 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    /* 3438 */ Equip eq = (Equip) item7;
                    /* 3439 */ int level = eq.getState() - 16;
                    /* 3440 */ if (level < 4) {
                        /* 3441 */ c.getPlayer().dropMessage(1, "레전더리 등급인 아이템만 사용 가능합니다.");
                        /* 3442 */ c.send(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    int poten = potential(item7.getItemId(), level);
                    eq.setPotential1(poten);
                    eq.setPotential2(poten);
                    eq.setPotential3(poten);
                    c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), true, itemId, eq.getItemId()));
                    c.getPlayer().forceReAddItem_NoUpdate(item7, MapleInventoryType.EQUIP);
                    used = true;
                        
                   
                    /* 3453 */ if (GameConstants.isZeroWeapon(eq.getItemId())) {
                        /* 3454 */ Equip zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                        /* 3455 */ zeroequip.setState(eq.getState());
                        /* 3456 */ zeroequip.setLines(eq.getLines());
                        /* 3457 */ zeroequip.setPotential1(eq.getPotential1());
                        /* 3458 */ zeroequip.setPotential2(eq.getPotential2());
                        /* 3459 */ zeroequip.setPotential3(eq.getPotential3());
                        /* 3460 */ if (zeroequip != null) {
                            /* 3461 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) zeroequip));
                        }
                    }
                    /* 3464 */ c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), true, itemId, eq.getItemId()));
                    /* 3465 */ c.getPlayer().forceReAddItem_NoUpdate(item7, MapleInventoryType.EQUIP);
                    /* 3466 */ c.send(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) eq));
                    /* 3467 */ used = true;
                    break;
                }
                /* 3469 */ c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), false, itemId, 0));
                break;

            case 5062006:
                /* 3475 */ pos = slea.readInt();
                /* 3476 */ item7 = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) pos);
                /* 3477 */ if (GameConstants.isZero(c.getPlayer().getJob()) && item7 == null) {
                    /* 3478 */ item7 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) pos);
                }
                /* 3480 */ if (item7 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    /* 3481 */ Equip eq = (Equip) item7;
                    /* 3482 */ int level = eq.getState() - 16;
                    /* 3483 */ if (level < 4) {
                        /* 3484 */ c.getPlayer().dropMessage(1, "레전더리 등급인 아이템만 사용 가능합니다.");
                        /* 3485 */ c.send(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    /* 3488 */ eq.setPotential1(CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 1, new int[0]));
                    /* 3489 */ eq.setPotential2(CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 2, new int[0]));
                    /* 3490 */ if (eq.getPotential3() > 0) {
                        /* 3491 */ eq.setPotential3(CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 3, new int[]{eq.getPotential1(), eq.getPotential2()}));
                        /* 3492 */ while (!GameConstants.getPotentialCheck(eq.getPotential3(), eq.getPotential1(), eq.getPotential2())) {
                            /* 3493 */ eq.setPotential3(CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 3, new int[]{eq.getPotential1(), eq.getPotential2()}));
                        }
                    }
                    /* 3496 */ if (GameConstants.isZeroWeapon(eq.getItemId())) {
                        /* 3497 */ Equip zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                        /* 3498 */ zeroequip.setState(eq.getState());
                        /* 3499 */ zeroequip.setLines(eq.getLines());
                        /* 3500 */ zeroequip.setPotential1(eq.getPotential1());
                        /* 3501 */ zeroequip.setPotential2(eq.getPotential2());
                        /* 3502 */ zeroequip.setPotential3(eq.getPotential3());
                        /* 3503 */ if (zeroequip != null) {
                            /* 3504 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) zeroequip));
                        }
                    }
                    /* 3507 */ c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), true, itemId, eq.getItemId()));
                    /* 3508 */ c.getPlayer().forceReAddItem_NoUpdate(item7, MapleInventoryType.EQUIP);
                    /* 3509 */ c.send(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) eq));
                    /* 3510 */ used = true;
                    break;
                }
                /* 3512 */ c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), false, itemId, 0));
                break;

          

            case 5152300:
                /* 3581 */ forbiddenFaces = new int[]{22100, 22200, 22300, 22400, 22500, 22600, 22700, 22800};

                for (int face : forbiddenFaces) {
                    if (c.getPlayer().getFace() == face) {
                        used = false;
                        c.getPlayer().dropMessage(1, "믹스렌즈가 불가능한 성형입니다.");
                        return;
                    }
                }

                /* 3590 */ bool1 = (slea.readByte() == 1);
                /* 3591 */ bool3 = (slea.readByte() == 1);
                /* 3592 */ isAlphaBeta = (slea.readByte() == 1);
                /* 3593 */ slea.readByte();

                /* 3595 */ ordinaryColor = slea.readInt();
                /* 3596 */ addColor = slea.readInt();

                /* 3598 */ i6 = (c.getPlayer().getFace() < 100000) ? c.getPlayer().getFace() : (c.getPlayer().getFace() / 1000);
                /* 3599 */ if (bool1 || bool3) {
                    /* 3600 */ i6 = (c.getPlayer().getSecondFace() < 100000) ? c.getPlayer().getSecondFace() : (c.getPlayer().getSecondFace() / 1000);
                }
                /* 3602 */ i6 = i6 - i6 % 1000 + i6 % 100 + ordinaryColor * 100;

                /* 3604 */ newFace = i6 * 1000 + addColor * 100 + slea.readInt();

                /* 3606 */ if (bool1) {
                    /* 3607 */ c.getPlayer().setSecondFace(newFace);
                    /* 3608 */ c.getPlayer().updateAngelicStats();
                    /* 3609 */                } else if (bool3) {
                    /* 3610 */ c.getPlayer().setSecondFace(newFace);
                    /* 3611 */ c.getPlayer().updateZeroStats();
                    /* 3612 */                } else if (isAlphaBeta) {
                    /* 3613 */ c.getPlayer().setFace(newFace);
                    /* 3614 */ c.getPlayer().setSecondFace(newFace);
                    /* 3615 */ c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
                } else {
                    /* 3617 */ c.getPlayer().setFace(newFace);
                    /* 3618 */ c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
                }

                /* 3621 */ c.getSession().writeAndFlush(CWvsContext.mixLense(itemId, i6, newFace, bool1, bool3, isAlphaBeta, c.getPlayer()));
                /* 3622 */ c.getPlayer().equipChanged();
                /* 3623 */ used = true;
                break;

            case 5152301:
                /* 3627 */ forbiddenFaces = new int[]{22100, 22200, 22300, 22400, 22500, 22600, 22700, 22800};

                for (int face : forbiddenFaces) {
                    if (c.getPlayer().getFace() == face) {
                        used = false;
                        c.getPlayer().dropMessage(1, "믹스렌즈가 불가능한 성형입니다.");
                        return;
                    }
                }

                /* 3636 */ dressUp = (slea.readByte() == 1);
                /* 3637 */ isBeta = (slea.readByte() == 1);
                /* 3638 */ isAlphaBeta = (slea.readByte() == 1);
                /* 3639 */ baseFace = (c.getPlayer().getFace() < 100000) ? c.getPlayer().getFace() : (c.getPlayer().getFace() / 1000);

                /* 3641 */ i4 = Randomizer.nextInt(8);
                /* 3642 */ i5 = Randomizer.nextInt(8);
                /* 3643 */ while (i5 == i4) {
                    /* 3644 */ i5 = Randomizer.nextInt(8);
                }

                /* 3647 */ baseFace = baseFace - baseFace % 1000 + baseFace % 100 + i4 * 100;

                /* 3649 */ newFace = baseFace * 1000 + i5 * 100 + Randomizer.rand(1, 99);

                /* 3651 */ c.getSession().writeAndFlush(CWvsContext.mixLense(itemId, baseFace, newFace, dressUp, isBeta, isAlphaBeta, c.getPlayer()));

                /* 3653 */ if (dressUp) {
                    /* 3654 */ c.getPlayer().setSecondFace(newFace);
                    /* 3655 */ if (c.getPlayer().getDressup()) {
                        /* 3656 */ c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
                    }
                    /* 3658 */                } else if (isBeta) {
                    /* 3659 */ c.getPlayer().setSecondFace(newFace);
                    /* 3660 */ if (c.getPlayer().getGender() == 1) {
                        /* 3661 */ c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
                    }
                    /* 3663 */                } else if (isAlphaBeta) {
                    /* 3664 */ c.getPlayer().setFace(newFace);
                    /* 3665 */ c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
                } else {
                    /* 3667 */ c.getPlayer().setFace(newFace);
                    /* 3668 */ c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
                }

                /* 3671 */ c.getPlayer().equipChanged();
                /* 3672 */ used = true;
                break;

            case 5521000:
                /* 3676 */ mapleInventoryType2 = MapleInventoryType.getByType((byte) slea.readInt());
                /* 3677 */ item6 = c.getPlayer().getInventory(mapleInventoryType2).getItem((short) slea.readInt());

                /* 3679 */ if (item6 != null && !ItemFlag.TRADEABLE_ONETIME_EQUIP.check(item6.getFlag())
                        && /* 3680 */ MapleItemInformationProvider.getInstance().isShareTagEnabled(item6.getItemId())) {
                    /* 3681 */ int i8 = item6.getFlag();
                    /* 3682 */ if (mapleInventoryType2 == MapleInventoryType.EQUIP) {
                        /* 3683 */ i8 += ItemFlag.TRADEABLE_ONETIME_EQUIP.getValue();
                    } else {
                        return;
                    }
                    /* 3687 */ item6.setFlag(i8);
                    /* 3688 */ c.getPlayer().forceReAddItem_NoUpdate(item6, mapleInventoryType2);
                    /* 3689 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item6));
                    /* 3690 */ used = true;
                }
                break;

//5000930, 5000931, 5000932,5002079,5002080,5002081,5002254,5002255,5002256
    case 5069100://루나 크리스탈
                 wonderblack = new int[]{5000762, 5000763, 5000764, 5000790, 5000791, 5000792, 5000918, 5000919, 5000920, 5000933, 5000934, 5000935, 5000963, 5000964, 5000965, 5002033, 5002034, 5002035, 5002082, 5002083, 5002084, 5002137, 5002138, 5002139, 5002161, 5002162, 5002163, 5002186, 5002187, 5002188, 5002200, 5002201, 5002202, 5002226, 5002227, 5002228};
                luna = new int[]{5000930, 5000931, 5000932,5002079,5002080,5002081,5002254,5002255,5002256};
                slea.skip(4);
                baseslot = slea.readShort();
                slea.skip(12);
                usingslot = slea.readShort();
                baseitem = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(baseslot);
                usingitem = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(usingslot);
                basegrade = baseitem.getPet().getWonderGrade();
                if (baseitem == null || usingitem == null) {
                    return;
                }

                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, baseitem.getPosition(), (short) 1, false);
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, usingitem.getPosition(), (short) 1, false);

                random = new ArrayList<>();

                event = (Calendar.getInstance().get(7) == 3);
                sp = false;

                maplePet3 = null;
                item12 = null;

                if (basegrade == 1) {
                    random.add(new Pair(Integer.valueOf(1), Integer.valueOf(9140 + (event ? -500 : 0))));
                    random.add(new Pair(Integer.valueOf(2), Integer.valueOf(664 + (event ? 500 : 0))));
                    random.add(new Pair(Integer.valueOf(3), Integer.valueOf(196)));
                } else if (basegrade == 4) {
                    random.add(new Pair(Integer.valueOf(1), Integer.valueOf(8460 + (event ? -500 : 0))));
                    random.add(new Pair(Integer.valueOf(2), Integer.valueOf(1140 + (event ? 500 : 0))));
                    random.add(new Pair(Integer.valueOf(3), Integer.valueOf(400)));
                }

                i7 = GameConstants.getWeightedRandom(random);
                itemidselect = 0;

                if (i7 == 1 || i7 == 2) {
                    itemidselect = wonderblack[Randomizer.rand(0, wonderblack.length - 1)];
                    if (i7 == 2) {
                        itemidselect = luna[Randomizer.rand(0, luna.length - 1)];
                        sp = true;
                    }
                    maplePet3 = MaplePet.createPet(itemidselect, -1);
                    if (basegrade == 1 && i7 == 1) {
                        maplePet3.setWonderGrade(4);
                    } else if (i7 == 1) {
                        maplePet3.setWonderGrade(5);
                    }
                    Connection con = null;

                    try {
                        con = DatabaseConnection.getConnection();
                        maplePet3.saveToDb(con);
                        con.close();
                    } catch (Exception exception) {
                        try {
                            if (con != null) {
                                con.close();
                            }
                        } catch (Exception exception1) {
                        }
                    } finally {
                        try {
                            if (con != null) {
                                con.close();
                            }
                        } catch (Exception exception) {
                        }
                    }

                    item12 = MapleInventoryManipulator.addId_Item(c, itemidselect, (short) 1, "", maplePet3, 30L, "", false);
               /* } else if (i7 == 3) {
                    equip3 = (Equip) MapleItemInformationProvider.getInstance().getEquipById(4310027);
                    equip3.setUniqueId(MapleInventoryIdentifier.getInstance());
                    MapleInventoryManipulator.addbyItem(c, (Item) equip3);
                }*/
                if (item12 != null) {
                    c.getSession().writeAndFlush(CSPacket.LunaCrystal((Item) item12));
                    if (sp) {
                        World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(), "", c.getPlayer().getName() + "님이 루나 크리스탈에서 {} 을 획득하였습니다.", true, (Item) item12));
                    }
                    used = true;
                }
                break;
}

            case 5068301:
                if (c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() >= 1 && c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() >= 1 && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    final List<Pair<Integer, Integer>> random3 = new ArrayList<Pair<Integer, Integer>>();
                    final int[] UniqueItem = { 5000762, 5000763, 5000764, 5000790, 5000791, 5000792, 5000918, 5000919, 5000920, 5000933, 5000934, 5000935, 5000963, 5000964, 5000965, 5002033, 5002034, 5002035, 5002082, 5002083, 5002084, 5002137, 5002138, 5002139, 5002161, 5002162, 5002163, 5002186, 5002187, 5002188, 5002200, 5002201, 5002202, 5002226, 5002227, 5002228 };
                    int itemid = 0;
                    final int count = 1;
                    final int rand3 = Randomizer.rand(0, UniqueItem.length - 1);
                    itemid = UniqueItem[rand3];
                    final MapleItemInformationProvider ii7 = MapleItemInformationProvider.getInstance();
                   // Item item6;
                    if (GameConstants.isPet(itemid)) {
                        item6 = MapleInventoryManipulator.addId_Item(c, itemid, (short)1, "", MaplePet.createPet(itemid, -1L), 30L, "", false);
                    }
                    else {
                        if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
                            item6 = ii7.getEquipById(itemid);
                        }
                        else {
                            item6 = new Item(itemid, (short)0, (short)count);
                        }
                        if (MapleItemInformationProvider.getInstance().isCash(itemid)) {
                            item6.setUniqueId(MapleInventoryIdentifier.getInstance());
                        }
                        MapleInventoryManipulator.addbyItem(c, item6);
                    }
                    if (item6 != null) {
                        World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(), "", c.getPlayer().getName() + "님이 위습의 블랙베리에서 {} 을 획득하였습니다.", true, item6));
                        c.getSession().writeAndFlush((Object)CSPacket.WonderBerry((byte)1, item6, toUse.getItemId()));
                    }
                    used = true;
                    break;
                }
                c.getPlayer().dropMessage(5, "소비, 캐시, 장비 여유 공간이 각각 한칸이상 부족합니다.");
                break;

            case 5068302:
                /* 3818 */ if (c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() >= 1 && c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() >= 1 && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    /* 3819 */ Item item13;
                    int[][] itemid = {{1113070, 1}, {1152155, 1}, {1032216, 1}, {2435755, 1}, {2439653, 1}, {2046996, 1}, {2046997, 1}, {2047818, 1}, {5000930, 1}, {5000931, 1}, {5000932, 1}, {2591659, 1}, {2438686, 1}, {2591640, 1}, {2591676, 1}, {1113063, 1}, {1113064, 1}, {1022232, 1}, {1672077, 1}, {1113063, 1}, {1113064, 1}, {1113065, 1}, {1113066, 1}, {1112663, 1}, {1112586, 1}, {2438686, 1}, {5002079, 1}, {5002080, 1}, {5002081, 1}, {2438686, 1}};
                    /* 3820 */ int i8 = Randomizer.nextInt(itemid.length);
                    /* 3821 */ MapleItemInformationProvider mapleItemInformationProvider = MapleItemInformationProvider.getInstance();

                    /* 3823 */ if (GameConstants.isPet(itemid[i8][0])) {
                        /* 3824 */ item13 = MapleInventoryManipulator.addId_Item(c, itemid[i8][0], (short) itemid[i8][1], "", MaplePet.createPet(itemid[i8][0], -1L), 30L, "", false);
                    } else {

                        /* 3827 */ if (GameConstants.getInventoryType(itemid[i8][0]) == MapleInventoryType.EQUIP) {
                            /* 3828 */ item13 = (Equip) mapleItemInformationProvider.getEquipById(itemid[i8][0]);
                        } else {
                            /* 3830 */ item13 = new Item(itemid[i8][0], (short) 0, (short) itemid[i8][1], (byte) ItemFlag.UNTRADEABLE.getValue());
                        }
                        /* 3832 */ if (MapleItemInformationProvider.getInstance().isCash(itemid[i8][0])) {
                            /* 3833 */ item13.setUniqueId(MapleInventoryIdentifier.getInstance());
                        }

                        /* 3836 */ MapleInventoryManipulator.addbyItem(c, item13);
                    }
                    /* 3838 */ if (item13 != null) {
                        /* 3839 */ c.getSession().writeAndFlush(CSPacket.WonderBerry((byte) 1, item13, toUse.getItemId()));
                    }
                    /* 3841 */ used = true;
                    break;
                }
                /* 3843 */ c.getPlayer().dropMessage(5, "소비, 캐시, 장비 여유 공간이 각각 한칸이상 부족합니다.");
                break;

         case 5068300:
                /* 3848 */ if (c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() >= 1 && c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() >= 1 && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    /* 3849 */ Item item13;
                    List<Pair<Integer, Integer>> list = new ArrayList<>();

                    /* 3851 */ int[][] NormalItem = {{5068300,1}};
                    /* 3852 */ int[] HighuerItem = {5000765, 5000766, 5000767, 5000768, 5000769, 5000793, 5000794, 5000795, 5000796, 5000797, 5000921, 5000922, 5000923, 5000924, 5000925, 5000936, 5000937, 5000938, 5000939, 5000940, 5000966, 5000967, 5000968, 5000965, 5000970, 5002036, 5002037, 5002038, 5002039, 5002040, 5002085, 5002086, 5002140, 5002141, 5002164, 5002165, 5002189, 5002190, 5002203, 5002204, 5002229, 5002230, 5002231};
                    /* 3853 */ int[] UniqueItem = {5000762, 5000763, 5000764, 5000790, 5000791, 5000792, 5000918, 5000919, 5000920, 5000933, 5000934, 5000935, 5000963, 5000964, 5000965, 5002033, 5002034, 5002035, 5002082, 5002083, 5002084, 5002137, 5002138, 5002139, 5002161, 5002162, 5002163, 5002186, 5002187, 5002188, 5002200, 5002201, 5002202, 5002226, 5002227, 5002228};

                    /* 3855 */ boolean bool = (Calendar.getInstance().get(7) == 2);

                    /* 3857 */ list.add(new Pair(Integer.valueOf(1), Integer.valueOf(3004)));
                    /* 3858 */ list.add(new Pair(Integer.valueOf(2), Integer.valueOf(6000 + (bool ? -500 : 0))));
                    /* 3859 */ list.add(new Pair(Integer.valueOf(3), Integer.valueOf(996 + (bool ? 500 : 0))));
                    /* 3860 */ int itemid = 0;
                    /* 3861 */ int count = 1;

                    /* 3885 */ int i8 = GameConstants.getWeightedRandom(list);
                    /* 3886 */ if (i8 == 1) {
                        /* 3887 */ int rand = Randomizer.rand(0, NormalItem.length - 1);
                        /* 3888 */ itemid = NormalItem[rand][0];
                        /* 3889 */ count = NormalItem[rand][1];
                        /* 3890 */                    } else if (i8 == 2) {
                        /* 3891 */ int rand = Randomizer.rand(0, HighuerItem.length - 1);
                        /* 3892 */ itemid = HighuerItem[rand];
                        /* 3893 */                    } else if (i8 == 3) {
                        /* 3894 */ int rand = Randomizer.rand(0, UniqueItem.length - 1);
                        /* 3895 */ itemid = UniqueItem[rand];
                    }

                    /* 3898 */ MapleItemInformationProvider mapleItemInformationProvider = MapleItemInformationProvider.getInstance();

                    /* 3900 */ if (GameConstants.isPet(itemid)) {
                        /* 3901 */ item13 = MapleInventoryManipulator.addId_Item(c, itemid, (short) 1, "", MaplePet.createPet(itemid, -1L), 30L, "", false);
                    } else {

                        /* 3904 */ if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
                            /* 3905 */ item13 = (Equip) mapleItemInformationProvider.getEquipById(itemid);
                        } else {
                            /* 3907 */ item13 = new Item(itemid, (short) 0, (short) count);
                        }
                        /* 3909 */ if (MapleItemInformationProvider.getInstance().isCash(itemid)) {
                            /* 3910 */ item13.setUniqueId(MapleInventoryIdentifier.getInstance());
                        }

                        /* 3913 */ MapleInventoryManipulator.addbyItem(c, item13);
                    }
                    /* 3915 */ if (item13 != null) {
                        /* 3916 */ if (i8 == 3) {
                            /* 3917 */ World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(), "", c.getPlayer().getName() + "님이 위습의 원더베리에서 {} 을 획득하였습니다.", true, item13));
                        }
                        /* 3919 */ c.getSession().writeAndFlush(CSPacket.WonderBerry((byte) 1, item13, toUse.getItemId()));
                    }
                    /* 3921 */ used = true;
                    break;
                }
                /* 3923 */ c.getPlayer().dropMessage(5, "소비, 캐시, 장비 여유 공간이 각각 한칸이상 부족합니다.");
                break;

            

            case 5520000:
            case 5520001:
                /* 3929 */ mapleInventoryType1 = MapleInventoryType.getByType((byte) slea.readInt());
                /* 3930 */ item5 = c.getPlayer().getInventory(mapleInventoryType1).getItem((short) slea.readInt());

                /* 3932 */ if (item5 != null && !ItemFlag.KARMA_EQUIP.check(item5.getFlag()) && !ItemFlag.KARMA_USE.check(item5.getFlag()) && (( /* 3933 */itemId == 5520000 && MapleItemInformationProvider.getInstance().isKarmaEnabled(item5.getItemId())) || (itemId == 5520001 && MapleItemInformationProvider.getInstance().isPKarmaEnabled(item5.getItemId())))) {
                    /* 3934 */ int i8 = item5.getFlag();
                    /* 3935 */ if (mapleInventoryType1 == MapleInventoryType.EQUIP) {
                        /* 3936 */ i8 += ItemFlag.KARMA_EQUIP.getValue();
                    } else {
                        /* 3938 */ i8 += ItemFlag.KARMA_USE.getValue();
                    }

                    /* 3941 */ if (item5.getType() == 1) {
                        /* 3942 */ Equip eq = (Equip) item5;
                        /* 3943 */ if (eq.getKarmaCount() > 0) {
                            /* 3944 */ eq.setKarmaCount((byte) (eq.getKarmaCount() - 1));
                        }
                    }

                    /* 3948 */ item5.setFlag(i8);
                    /* 3949 */ c.getPlayer().forceReAddItem_NoUpdate(item5, mapleInventoryType1);
                    /* 3950 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, mapleInventoryType1, item5));
                    /* 3951 */ used = true;
                }
                break;

            case 5570000:
                /* 3957 */ slea.readInt();
                /* 3958 */ equip1 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slea.readInt());

                /* 3960 */ if (equip1 != null) {
                    /* 3961 */ if (GameConstants.canHammer(equip1.getItemId()) && MapleItemInformationProvider.getInstance().getSlots(equip1.getItemId()) > 0 && equip1.getViciousHammer() < 2) {
                        /* 3962 */ equip1.setViciousHammer((byte) (equip1.getViciousHammer() + 1));
                        /* 3963 */ equip1.setUpgradeSlots((byte) (equip1.getUpgradeSlots() + 1));
                        /* 3964 */ c.getPlayer().forceReAddItem((Item) equip1, MapleInventoryType.EQUIP);
                        /* 3965 */ c.getSession().writeAndFlush(CSPacket.ViciousHammer(true, true));
                        /* 3966 */ used = true;
                        break;
                    }
                    /* 3968 */ c.getPlayer().dropMessage(5, "You may not use it on this item.");
                    /* 3969 */ c.getSession().writeAndFlush(CSPacket.ViciousHammer(true, false));
                }
                break;

            case 5069000:
            case 5069001:
                /* 3977 */ SLabel = new int[]{1003548, 1003549, 1050234, 1051284, 1702357, 1102420, 1003831, 1052605, 1702415, 1072808, 1082520, 1003867, 1042264, 1060182, 1061206, 1702424, 1082527, 1003892, 1050285, 1051352, 1702433, 1072831, 1102583, 1003909, 1050291, 1051357, 1702442, 1072836, 1102593, 1003945, 1050296, 1051362, 1702451, 1072852, 1102608, 1003957, 1003958, 1050300, 1051367, 1702457, 1072862, 1102619, 1003971, 1003972, 1051369, 1050302, 1702464, 1072868, 1102621, 1004002, 1050305, 1051373, 1702473, 1070057, 1071074, 1102632, 1003998, 1050304, 1051372, 1702468, 1072876, 1082565, 1000069, 1050311, 1051383, 1702486, 1072901, 1102667, 1000072, 1001095, 1050310, 1051382, 1702485, 1072897, 1102669, 1000074, 1001097, 1050319, 1051390, 1702503, 1071076, 1102674, 1004158, 1050322, 1051392, 1702512, 1070061, 1071078, 1102688, 1004180, 1042319, 1062207, 1702523, 1072934, 1004192, 1050335, 1051405, 1702528, 1072943, 1102706, 1004213, 1050337, 1051406, 1702535, 1072951, 1102712, 1004279, 1050341, 1051410, 1702540, 1072998, 1102748, 1004327, 1050346, 1051415, 1702549, 1073011, 1102758, 1000079, 1001100, 1050351, 1051420, 1702553, 1070064, 1100004, 1101000, 1004411, 1050353, 1051422, 1702561, 1070065, 1081014, 1004453, 1050359, 1051429, 1702570, 1070067, 1071083, 1102811, 1004468, 1050362, 1051432, 1702574, 1073050, 1102816, 1004486, 1050364, 1051434, 1702586, 1073056, 1102822, 1004527, 1050368, 1051437, 1702591, 1071085, 1070069, 1102831, 1004541, 1050370, 1051439, 1702595, 1073075, 1102836, 1004568, 1050372, 1051441, 1702607, 1073079, 1102844, 1000088, 1001110, 1050380, 1051450, 1702620, 1073084, 1102848, 1004590, 1050386, 1051456, 1073088, 1702625, 1102859, 1004602, 1050389, 1051459, 1702628, 1070072, 1071089, 1102864, 1000091, 1001113, 1050392, 1051463, 1702631, 1070073, 1071090, 1102869, 1004602, 1050394, 1051465, 1702637, 1073105, 1102876, 1002447, 1004690, 1050414, 1051483, 1702654, 1073127, 1102900, 1004701, 1050417, 1051486, 1702668, 1073128, 1102907, 1004716, 1050422, 1051490, 1070078, 1071095, 1102915, 1702676, 1004739, 1050423, 1051491, 1702681, 1070079, 1071096, 1102920, 1004774, 1004775, 1050424, 1051492, 1702687, 1073148, 1102928, 1004797, 1050430, 1051498, 1702696, 1073152, 1102936, 1004794, 1050429, 1051497, 1702694, 1070082, 1071099, 1102934, 1004814, 1050432, 1051500, 1702706, 1070083, 1071100, 1102951, 1004845, 1004846, 1050435, 1051503, 1702715, 1073170, 1073171, 1102959, 1102960, 1004852, 1050438, 1051506, 1702717, 1073175, 1073176, 1102964, 1004873, 1050441, 1051509, 1702726, 1073183, 1073184, 1102974, 1004881, 1004882, 1003460, 1050442, 1051510, 1702728, 1070084, 1071101, 1102976, 1004894, 1004895, 1050444, 1051512, 1070085, 1071102, 1102992, 1004923, 1050452, 1051519, 1702744, 1073200, 1102998, 1004947, 1004948, 1050454, 1051521, 1702755, 1073212, 1103010, 1004954, 1004955, 1050456, 1051523, 1702759, 1070088, 1071105, 1103013, 1004965, 1050461, 1051528, 1702766, 1070089, 1071106, 1103018, 1004975, 1050463, 1051530, 1702770, 1073226, 1103023, 1004988, 1050464, 1051531, 1702774, 1070090, 1071107, 1103029, 1005000, 1050468, 1051535, 1702779, 1073237, 1103035, 1005032, 1005033, 1050470, 1051537, 1702790, 1073246, 1103050, 1005043, 1050474, 1051541, 1702795, 1070093, 1071110, 1103055, 1005065, 1005066, 1053257, 1702804, 1073254, 1103067, 1005083, 1005084, 1050477, 1051544, 1702807, 1073255, 1103072, 1005092, 1050481, 1051548, 1702810, 1073258, 1103074, 1005111, 1050484, 1051551, 1702815, 1070097, 1071114, 1103079, 1005143, 1050486, 1051553, 1702826, 1073271, 1103094, 1005152, 1053305, 1702830, 1073273, 1103096, 1005166, 1050491, 1051559, 1702837, 1073280, 1103101, 1005184, 1005185, 1050492, 1051560, 1073290, 1702844, 1103114, 1005193, 1050495, 1051563, 1702850, 1073298, 1103118, 1005217, 1005218, 1050499, 1051567, 1702858, 1073302, 1103130, 1005231, 1005232, 1053351, 1053352, 1702865, 1073308, 1103138, 1005243, 1005244, 1050503, 1051573, 1702870, 1070103, 1103144, 1005260, 1005261, 1050505, 1051575, 1702876, 1070105, 1071121, 1103148, 1005272, 1050507, 1051577, 1702882, 1073322, 1103152, 1005280, 1005281, 1050509, 1051579, 1702887, 1070107, 1071123, 1103157, 1005319, 1050514, 1051584, 1702901, 1073335, 1103171, 1005324, 1050516, 1051586, 1702905, 1070110, 1071126, 1103175, 1005327, 1053416, 1702907, 1073342, 1103177, 1005354, 1053435, 1702918, 1073355, 1103185, 1005368, 1005369, 1050523, 1051593, 1702928, 1070113, 1071129, 1082744, 1005386, 1050525, 1051595, 1702937, 1070114, 1071130, 1103202, 1005399, 1050530, 1051601, 1702945, 1073378, 1103212, 1005412, 1050531, 1051602, 1103219, 1073362, 1702951, 1005419, 1005420, 1050534, 1051605, 1073390, 1103221, 1702956, 1005437, 1005438, 1050535, 1051606, 1073394, 1103224, 1702961, 1005458, 1053516, 1073402, 1103232, 1702970, 1005477, 1005478, 1050538, 1051609, 1073415, 1103235, 1702973, 1005499, 1053543, 1073428, 1103243, 1702981};

                /* 3979 */ 모자 = new int[]{1000070, 1000076, 1004897, 1001093, 1001098, 1004898, 1003955, 1004450, 1004591, 1004592, 1004777, 1005037, 1005038, 1005209, 1005210, 1005356, 1005495};

                /* 3981 */ 한벌남 = new int[]{1050299, 1050312, 1050339, 1050356, 1050385, 1050427, 1050445, 1050472, 1050497, 1050520, 1050542};

                /* 3983 */ 한벌여 = new int[]{1051366, 1051384, 1051408, 1051426, 1051455, 1051495, 1051513, 1051539, 1051565, 1051589, 1051613};

                /* 3985 */ 신발 = new int[]{1070071, 1070080, 1070086, 1070091, 1070100, 1070111, 1071088, 1071097, 1071103, 1071108, 1071117, 1071127, 1072860, 1072908, 1072978, 1073041, 1073425};

                /* 3987 */ 무기 = new int[]{1702456, 1702488, 1702538, 1702565, 1702624, 1702689, 1702736, 1702786, 1702856, 1702919, 1702976};

                /* 3989 */ 망토장갑 = new int[]{1102729, 1102809, 1102858, 1102932, 1102988, 1103053, 1103126, 1103127, 1103187, 1103241, 1082555, 1082580};

                /* 3991 */ mapleItemInformationProvider5 = MapleItemInformationProvider.getInstance();
                /* 3992 */ baseitemid = slea.readInt();
                /* 3993 */ baseitempos = slea.readShort();
                /* 3994 */ slea.skip(8);
                /* 3995 */ useitemid = slea.readInt();
                /* 3996 */ useitempos = slea.readShort();
                /* 3997 */ slea.skip(8);
                /* 3998 */ equip4 = (Equip) c.getPlayer().getInventory(MapleInventoryType.CODY).getItem(baseitempos);
                /* 3999 */ equip5 = (Equip) c.getPlayer().getInventory(MapleInventoryType.CODY).getItem(useitempos);

                /* 4007 */ 마라벨나올확률 = (equip4.getEquipmentType() == 1) ? 7 : (ServerConstants.ServerTest ? 100 : 5);

                /* 4017 */ MItemidselect = 0;
                Label = 0;
                own = 0;
                two = 0;
                three = 0;
                /* 4018 */ 마라벨인가 = false;
                try {
                    /* 4020 */ if (Randomizer.isSuccess(마라벨나올확률)) {
                        /* 4021 */ if (GameConstants.isCap(baseitemid)) {
                            /* 4022 */ boolean a = true;
                            /* 4023 */ int Random = (int) Math.floor(Math.random() * 모자.length - 1.0D);
                            /* 4024 */ MItemidselect = 모자[Random];
                            /* 4025 */ if (c.getPlayer().getGender() == 0) {
                                /* 4026 */ while (a) {
                                    /* 4027 */ if (GameConstants.여자모자(MItemidselect)) {
                                        /* 4028 */ Random = (int) Math.floor(Math.random() * 모자.length - 1.0D);
                                        /* 4029 */ MItemidselect = 모자[Random];
                                        continue;
                                    }
                                    /* 4031 */ a = false;
                                }
                            } else {

                                /* 4035 */ while (a) {
                                    /* 4036 */ if (GameConstants.남자모자(MItemidselect)) {
                                        /* 4037 */ Random = (int) Math.floor(Math.random() * 모자.length - 1.0D);
                                        /* 4038 */ MItemidselect = 모자[Random];
                                        continue;
                                    }
                                    /* 4040 */ a = false;
                                }

                            }
                            /* 4044 */                        } else if (GameConstants.isLongcoat(baseitemid)) {
                            /* 4045 */ if (c.getPlayer().getGender() == 0) {
                                /* 4046 */ int Random = (int) Math.floor(Math.random() * 한벌남.length - 1.0D);
                                /* 4047 */ MItemidselect = 한벌남[Random];
                            } else {
                                /* 4049 */ int Random = (int) Math.floor(Math.random() * 한벌여.length - 1.0D);
                                /* 4050 */ MItemidselect = 한벌여[Random];
                            }
                            /* 4052 */                        } else if (GameConstants.isCape(baseitemid) || GameConstants.isGlove(baseitemid)) {
                            /* 4053 */ int Random = (int) Math.floor(Math.random() * 망토장갑.length - 1.0D);
                            /* 4054 */ MItemidselect = 망토장갑[Random];
                            /* 4055 */                        } else if (GameConstants.isShoes(baseitemid)) {
                            /* 4056 */ boolean a = true;
                            /* 4057 */ int Random = (int) Math.floor(Math.random() * 신발.length - 1.0D);
                            /* 4058 */ MItemidselect = 신발[Random];
                            /* 4059 */ if (c.getPlayer().getGender() == 0) {
                                /* 4060 */ while (a) {
                                    /* 4061 */ if (GameConstants.여자신발(MItemidselect)) {
                                        /* 4062 */ Random = (int) Math.floor(Math.random() * 신발.length - 1.0D);
                                        /* 4063 */ MItemidselect = 신발[Random];
                                        continue;
                                    }
                                    /* 4065 */ a = false;
                                }
                            } else {

                                /* 4069 */ while (a) {
                                    /* 4070 */ if (GameConstants.남자신발(MItemidselect)) {
                                        /* 4071 */ Random = (int) Math.floor(Math.random() * 신발.length - 1.0D);
                                        /* 4072 */ MItemidselect = 신발[Random];
                                        continue;
                                    }
                                    /* 4074 */ a = false;
                                }

                            }
                            /* 4078 */                        } else if (GameConstants.isWeapon(baseitemid)) {
                            /* 4079 */ int Random = (int) Math.floor(Math.random() * 무기.length - 1.0D);
                            /* 4080 */ MItemidselect = 무기[Random];
                        }
                        /* 4082 */ 마라벨인가 = true;
                    } else {
                        /* 4084 */ if (equip4.getEquipmentType() == 1) {
                            /* 4085 */ Label = 2;
                        } else {
                            /* 4087 */ Label = 1;
                        }
                        /* 4089 */ if (GameConstants.isCap(baseitemid)) {
                            /* 4090 */ boolean a = true;
                            /* 4091 */ int Random = (int) Math.floor(Math.random() * SLabel.length);
                            /* 4092 */ MItemidselect = SLabel[Random];
                            /* 4093 */ if (c.getPlayer().getGender() == 0) {
                                /* 4094 */ while (a) {
                                    /* 4095 */ if (!GameConstants.isCap(MItemidselect)) {
                                        /* 4096 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4097 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4099 */ if (GameConstants.여자모자(MItemidselect)) {
                                        /* 4100 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4101 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4103 */ a = false;
                                }

                            } else {

                                /* 4108 */ while (a) {
                                    /* 4109 */ if (!GameConstants.isCap(MItemidselect)) {
                                        /* 4110 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4111 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4113 */ if (GameConstants.남자모자(MItemidselect)) {
                                        /* 4114 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4115 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4117 */ a = false;
                                }

                            }

                            /* 4122 */                        } else if (GameConstants.isLongcoat(baseitemid)) {
                            /* 4123 */ boolean a = true;
                            /* 4124 */ int Random = (int) Math.floor(Math.random() * SLabel.length);
                            /* 4125 */ MItemidselect = SLabel[Random];
                            /* 4126 */ if (c.getPlayer().getGender() == 0) {
                                /* 4127 */ while (a) {
                                    /* 4128 */ if (!GameConstants.isLongcoat(MItemidselect)) {
                                        /* 4129 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4130 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4132 */ if (GameConstants.여자한벌(MItemidselect)) {
                                        /* 4133 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4134 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4136 */ a = false;
                                }

                            } else {

                                /* 4141 */ while (a) {
                                    /* 4142 */ if (!GameConstants.isLongcoat(MItemidselect)) {
                                        /* 4143 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4144 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4146 */ if (GameConstants.남자한벌(MItemidselect)) {
                                        /* 4147 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4148 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4150 */ a = false;
                                }

                            }

                            /* 4155 */                        } else if (GameConstants.isCape(baseitemid)) {
                            /* 4156 */ boolean a = true;
                            /* 4157 */ int Random = (int) Math.floor(Math.random() * SLabel.length);
                            /* 4158 */ MItemidselect = SLabel[Random];
                            /* 4159 */ while (a) {
                                /* 4160 */ if (!GameConstants.isCape(MItemidselect) && !GameConstants.isGlove(baseitemid)) {
                                    /* 4161 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                    /* 4162 */ MItemidselect = SLabel[Random];
                                    continue;
                                }
                                /* 4164 */ a = false;
                            }

                            /* 4167 */                        } else if (GameConstants.isGlove(baseitemid)) {
                            /* 4168 */ boolean a = true;
                            /* 4169 */ int Random = (int) Math.floor(Math.random() * SLabel.length);
                            /* 4170 */ MItemidselect = SLabel[Random];
                            /* 4171 */ while (a) {
                                /* 4172 */ if (!GameConstants.isGlove(MItemidselect) && !GameConstants.isCape(MItemidselect)) {
                                    /* 4173 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                    /* 4174 */ MItemidselect = SLabel[Random];
                                    continue;
                                }
                                /* 4176 */ a = false;
                            }

                            /* 4179 */                        } else if (GameConstants.isShoes(baseitemid)) {
                            /* 4180 */ boolean a = true;
                            /* 4181 */ int Random = (int) Math.floor(Math.random() * SLabel.length);
                            /* 4182 */ MItemidselect = SLabel[Random];
                            /* 4183 */ if (c.getPlayer().getGender() == 0) {
                                /* 4184 */ while (a) {
                                    /* 4185 */ if (!GameConstants.isShoes(MItemidselect)) {
                                        /* 4186 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4187 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4189 */ if (GameConstants.여자신발(MItemidselect)) {
                                        /* 4190 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4191 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4193 */ a = false;
                                }

                            } else {

                                /* 4198 */ while (a) {
                                    /* 4199 */ if (!GameConstants.isShoes(MItemidselect)) {
                                        /* 4200 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4201 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4203 */ if (GameConstants.남자신발(MItemidselect)) {
                                        /* 4204 */ Random = (int) Math.floor(Math.random() * SLabel.length);
                                        /* 4205 */ MItemidselect = SLabel[Random];
                                        continue;
                                    }
                                    /* 4207 */ a = false;
                                }

                            }

                            /* 4212 */                        } else if (GameConstants.isWeapon(baseitemid)) {
                            /* 4213 */ boolean a = true;
                            /* 4214 */ while (a) {
                                /* 4215 */ if (!GameConstants.isWeapon(MItemidselect)) {
                                    /* 4216 */ int Random = (int) Math.floor(Math.random() * SLabel.length);
                                    /* 4217 */ MItemidselect = SLabel[Random];
                                    continue;
                                }
                                /* 4219 */ a = false;
                            }
                        }
                    }

                    /* 4224 */ if (MItemidselect <= 0) {
                        /* 4225 */ c.getPlayer().dropMessage(1, "마스터 피스가 준비되지 않았습니다. 다시 시도해주세요.");
                        /* 4226 */ MapleMap mapleMap = c.getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
                        /* 4227 */ c.getPlayer().changeMap(mapleMap, mapleMap.getPortal(0));
                        /* 4228 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    /* 4231 */ Equip equip6 = (Equip) mapleItemInformationProvider5.getEquipById(MItemidselect);
                    /* 4232 */ if (equip6 == null) {
                        /* 4233 */ c.getPlayer().dropMessage(1, "마스터 피스가 준비되지 않았습니다. 다시 시도해주세요.");
                        /* 4234 */ MapleMap mapleMap = c.getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
                        /* 4235 */ c.getPlayer().changeMap(mapleMap, mapleMap.getPortal(0));
                        /* 4236 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    /* 4239 */ Equip equip = equip6;
                    /* 4240 */ equip.setEquipmentType(Label);

                    /* 4255 */ if (GameConstants.isWeapon(equip.getItemId())) {
                        /* 4256 */ boolean 공 = false, suc = false;
                        /* 4257 */ int attack = 0, stat = 0;

                        /* 4259 */ attack = Randomizer.isSuccess(50) ? 21 : 22;
                        /* 4260 */ if (attack == 21) {
                            /* 4261 */ 공 = true;
                        }
                        /* 4263 */ own = attack * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
                        /* 4264 */ if (Randomizer.isSuccess(60)) {
                            /* 4265 */ suc = true;
                            /* 4266 */ two = (공 ? 22 : 21) * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
                        }
                        /* 4268 */ if (Randomizer.isSuccess(10)) {
                            /* 4269 */ three = (Randomizer.isSuccess(50) ? 21 : 22) * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
                            /* 4270 */ if (!suc) {
                                /* 4271 */ three = (공 ? 22 : 21) * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));

                            }

                        }

                    } else {

                        /* 4290 */ boolean suc = false;
                        /* 4291 */ int stat = 0, stat1 = 0, stat2 = 0;

                        /* 4293 */ stat = Randomizer.rand(11, 14);

                        /* 4295 */ own = stat * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
                        /* 4296 */ if (Randomizer.isSuccess(60)) {
                            /* 4297 */ suc = true;
                            /* 4298 */ stat1 = Randomizer.rand(11, 14);
                            /* 4299 */ while (stat == stat1) {
                                /* 4300 */ stat1 = Randomizer.rand(11, 14);
                            }
                            /* 4302 */ two = stat1 * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
                        }
                        /* 4304 */ if (마라벨인가
                                && /* 4305 */ Randomizer.isSuccess(30)) {
                            /* 4306 */ if (!suc) {
                                /* 4307 */ stat1 = Randomizer.rand(11, 14);
                                /* 4308 */ while (stat == stat1) {
                                    /* 4309 */ stat1 = Randomizer.rand(11, 14);
                                }
                                /* 4311 */ three = stat1 * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
                            } else {
                                /* 4313 */ stat2 = Randomizer.rand(11, 14);
                                /* 4314 */ while (stat1 == stat2 || stat == stat2) {
                                    /* 4315 */ stat2 = Randomizer.rand(11, 14);
                                }
                                /* 4317 */ three = stat2 * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
                            }
                        }
                    }

                    /* 4322 */ if (c.getPlayer().getQuestStatus(50008) == 1) {
                        /* 4324 */ c.getPlayer().setKeyValue(50008, "1", "1");
                    }
                    /* 4326 */ equip.setCoption1(own);
                    /* 4327 */ equip.setCoption2(two);
                    /* 4328 */ equip.setCoption3(three);
                    /* 4329 */ int i8 = equip6.getFlag();
                    /* 4330 */ if (itemId == 5069001) {
                        /* 4331 */ i8 |= ItemFlag.KARMA_EQUIP.getValue();
                        /* 4332 */ i8 |= ItemFlag.CHARM_EQUIPED.getValue();
                    }
                    /* 4334 */ equip.setUniqueId(MapleInventoryIdentifier.getInstance());
                    /* 4335 */ equip.setFlag(i8);
                    /* 4336 */ equip.setOptionExpiration(System.currentTimeMillis() + (Randomizer.isSuccess(10) ? 60L : (Randomizer.isSuccess(30) ? 28L : 14L)) * 24L * 60L * 60L * 1000L);
                    /* 4337 */ equip.setKarmaCount((byte) -1);
                    /* 4338 */ MapleInventoryManipulator.addbyItem(c, (Item) equip);
                    /* 4339 */ MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CODY, equip4.getPosition(), (short) 1, false);
                    /* 4340 */ MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CODY, equip5.getPosition(), (short) 1, false);
                    /* 4341 */ c.getSession().writeAndFlush(CSPacket.LunaCrystal((Item) equip));
                    /* 4342 */ if (마라벨인가) {
                        /* 4343 */ World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(), "", c.getPlayer().getName() + "님이 프리미엄 마스터피스에서 {} 을 획득하였습니다.", true, (Item) equip));
                    }
                    /* 4345 */ used = true;
                    /* 4346 */                } catch (Exception e) {
                    /* 4347 */ c.getPlayer().dropMessage(1, "마스터 피스가 준비되지 않았습니다. 다시 시도해주세요.");
                    /* 4348 */ MapleMap mapleMap = c.getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
                    /* 4349 */ c.getPlayer().changeMap(mapleMap, mapleMap.getPortal(0));
                    /* 4350 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                break;

            case 5060001:
                /* 4356 */ type = MapleInventoryType.getByType((byte) slea.readInt());
                /* 4357 */ item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());

                /* 4359 */ if (item4 != null && item4.getExpiration() == -1L) {
                    /* 4360 */ int i8 = item4.getFlag();
                    /* 4361 */ i8 |= ItemFlag.LOCK.getValue();
                    /* 4362 */ item4.setFlag(i8);

                    /* 4364 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
                    /* 4365 */ used = true;
                }
                break;

            case 5061000:
                /* 4370 */ type = MapleInventoryType.getByType((byte) slea.readInt());
                /* 4371 */ item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());

                /* 4373 */ if (item4 != null && item4.getExpiration() == -1L) {
                    /* 4374 */ int i8 = item4.getFlag();
                    /* 4375 */ i8 |= ItemFlag.LOCK.getValue();
                    /* 4376 */ item4.setFlag(i8);
                    /* 4377 */ item4.setExpiration(System.currentTimeMillis() + 604800000L);

                    /* 4379 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
                    /* 4380 */ used = true;
                }
                break;

            case 5061001:
                /* 4385 */ type = MapleInventoryType.getByType((byte) slea.readInt());
                /* 4386 */ item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());

                /* 4388 */ if (item4 != null && item4.getExpiration() == -1L) {
                    /* 4389 */ int i8 = item4.getFlag();
                    /* 4390 */ i8 |= ItemFlag.LOCK.getValue();
                    /* 4391 */ item4.setFlag(i8);

                    /* 4393 */ item4.setExpiration(System.currentTimeMillis() + -1702967296L);

                    /* 4395 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
                    /* 4396 */ used = true;
                }
                break;

            case 5061002:
                /* 4401 */ type = MapleInventoryType.getByType((byte) slea.readInt());
                /* 4402 */ item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());

                /* 4404 */ if (item4 != null && item4.getExpiration() == -1L) {
                    /* 4405 */ int i8 = item4.getFlag();
                    /* 4406 */ i8 |= ItemFlag.LOCK.getValue();
                    /* 4407 */ item4.setFlag(i8);

                    /* 4409 */ item4.setExpiration(System.currentTimeMillis() + -813934592L);

                    /* 4411 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
                    /* 4412 */ used = true;
                }
                break;

            case 5061003:
                /* 4417 */ type = MapleInventoryType.getByType((byte) slea.readInt());
                /* 4418 */ item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());

                /* 4420 */ if (item4 != null && item4.getExpiration() == -1L) {
                    /* 4421 */ int i8 = item4.getFlag();
                    /* 4422 */ i8 |= ItemFlag.LOCK.getValue();
                    /* 4423 */ item4.setFlag(i8);

                    /* 4425 */ item4.setExpiration(System.currentTimeMillis() + 1471228928L);

                    /* 4427 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
                    /* 4428 */ used = true;
                }
                break;

            case 5063000:
                /* 4433 */ type = MapleInventoryType.getByType((byte) slea.readInt());
                /* 4434 */ item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());

                /* 4436 */ if (item4 != null && item4.getType() == 1) {
                    /* 4437 */ int i8 = item4.getFlag();
                    /* 4438 */ i8 |= ItemFlag.LUCKY_PROTECT_SHIELD.getValue();
                    /* 4439 */ item4.setFlag(i8);

                    /* 4441 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
                    /* 4442 */ used = true;
                }
                break;

            case 5064000:
                /* 4448 */ s2 = slea.readShort();
                /* 4449 */ if (s2 < 0) {
                    /* 4450 */ toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(s2);
                } else {
                    /* 4452 */ toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(s2);
                }
                /* 4454 */ if (toScroll.getEnhance() >= 12) {
                    break;
                }
                /* 4457 */ flag = toScroll.getFlag();
                /* 4458 */ flag |= ItemFlag.PROTECT_SHIELD.getValue();
                /* 4459 */ toScroll.setFlag(flag);

                /* 4461 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) toScroll));
                /* 4462 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(), Equip.ScrollResult.SUCCESS, false, toUse.getItemId(), toScroll.getItemId()), true);
                /* 4463 */ used = true;
                break;

            case 5064100:
                /* 4468 */ s1 = slea.readShort();
                /* 4469 */ if (s1 < 0) {
                    /* 4470 */ toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(s1);
                } else {
                    /* 4472 */ toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(s1);
                }
                /* 4474 */ if (toScroll.getUpgradeSlots() == 0) {
                    break;
                }
                /* 4477 */ flag = toScroll.getFlag();
                /* 4478 */ flag |= ItemFlag.SAFETY_SHIELD.getValue();
                /* 4479 */ toScroll.setFlag(flag);

                /* 4481 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) toScroll));
                /* 4482 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(), Equip.ScrollResult.SUCCESS, false, toUse.getItemId(), toScroll.getItemId()), true);
                /* 4483 */ used = true;
                break;

            case 5064300:
                /* 4488 */ dst = slea.readShort();
                /* 4489 */ if (dst < 0) {
                    /* 4490 */ toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
                } else {
                    /* 4492 */ toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
                }
                /* 4494 */ flag = toScroll.getFlag();
                /* 4495 */ flag |= ItemFlag.RECOVERY_SHIELD.getValue();
                /* 4496 */ toScroll.setFlag(flag);
                /* 4497 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.addInventorySlot(MapleInventoryType.EQUIP, (Item) toScroll));
                /* 4498 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) toScroll));
                /* 4499 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(), Equip.ScrollResult.SUCCESS, false, toUse.getItemId(), toScroll.getItemId()), true);
                /* 4500 */ used = true;
                break;

            case 5064400:
                /* 4505 */ dst = slea.readShort();
                /* 4506 */ if (dst < 0) {
                    /* 4507 */ toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
                } else {
                    /* 4509 */ toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
                }
                /* 4511 */ flag = toScroll.getFlag();
                /* 4512 */ flag |= ItemFlag.RETURN_SCROLL.getValue();
                /* 4513 */ toScroll.setFlag(flag);

                /* 4515 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) toScroll));
                /* 4516 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(), Equip.ScrollResult.SUCCESS, false, toUse.getItemId(), toScroll.getItemId()), true);
                /* 4517 */ used = true;
                break;

            case 5060003:
            case 5060004:
                /* 4525 */ item = c.getPlayer().getInventory(MapleInventoryType.ETC).findById((itemId == 5060003) ? 4170023 : 4170024);
                /* 4526 */ if (item == null || item.getQuantity() <= 0) {
                    return;
                }
                break;

            case 5070000:
                /* 4533 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4534 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4537 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4538 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4541 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 4542 */ String str = slea.readMapleAsciiString();

                    /* 4544 */ if (str.length() > 65) {
                        break;
                    }
                    /* 4547 */ StringBuilder stringBuilder = new StringBuilder();
                    /* 4548 */ addMedalString(c.getPlayer(), stringBuilder);
                    /* 4549 */ stringBuilder.append(c.getPlayer().getName());
                    /* 4550 */ stringBuilder.append(" : ");
                    /* 4551 */ stringBuilder.append(str);

                    /* 4553 */ c.getPlayer().getMap().broadcastMessage(CWvsContext.serverNotice(2, c.getPlayer().getName(), stringBuilder.toString()));
                    /* 4554 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str, "채널 : " + c.getChannel());
                    /* 4555 */ used = true;
                    break;
                }
                /* 4557 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5071000:
                /* 4562 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4563 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4566 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4567 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4570 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 4571 */ String str = slea.readMapleAsciiString();

                    /* 4573 */ if (str.length() > 65) {
                        break;
                    }
                    /* 4576 */ StringBuilder stringBuilder = new StringBuilder();
                    /* 4577 */ addMedalString(c.getPlayer(), stringBuilder);
                    /* 4578 */ stringBuilder.append(c.getPlayer().getName());
                    /* 4579 */ stringBuilder.append(" : ");
                    /* 4580 */ stringBuilder.append(str);

                    /* 4582 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                        /* 4583 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                        /* 4584 */ c.getChannelServer().broadcastSmegaPacket(CWvsContext.serverNotice(2, c.getPlayer().getName(), stringBuilder.toString()));
                        /* 4585 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str, "채널 : " + c.getChannel());
                        /* 4586 */ used = true;
                        break;
                    }
                    /* 4588 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                    break;
                }
                /* 4591 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5077000:
                /* 4596 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4597 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4600 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4601 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4604 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 4605 */ byte numLines = slea.readByte();
                    /* 4606 */ if (numLines > 3) {
                        return;
                    }
                    /* 4609 */ List<String> messages = new LinkedList<>();

                    /* 4611 */ for (int i8 = 0; i8 < numLines; i8++) {
                        /* 4612 */ String str = slea.readMapleAsciiString();
                        /* 4613 */ if (str.length() > 65) {
                            break;
                        }
                        /* 4616 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str, "채널 : " + c.getChannel());
                        /* 4617 */ messages.add(c.getPlayer().getName() + " : " + str);
                    }
                    /* 4619 */ boolean bool = (slea.readByte() > 0);

                    /* 4621 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                        /* 4622 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                        /* 4623 */ World.Broadcast.broadcastSmega(CWvsContext.tripleSmega(c.getPlayer().getName(), messages, bool, c.getChannel()));
                        /* 4624 */ used = true;
                        break;
                    }
                    /* 4626 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                    break;
                }
                /* 4629 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5079004:
                /* 4634 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4635 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4638 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4639 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4642 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 4643 */ String str = slea.readMapleAsciiString();

                    /* 4645 */ if (str.length() > 65) {
                        break;
                    }

                    /* 4649 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                        /* 4650 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                        /* 4651 */ World.Broadcast.broadcastSmega(CWvsContext.echoMegaphone(c.getPlayer().getName(), str));
                        /* 4652 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str, "채널 : " + c.getChannel());
                        /* 4653 */ used = true;
                        break;
                    }
                    /* 4655 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                    break;
                }
                /* 4658 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5073000:
                /* 4663 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4664 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4667 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4668 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4671 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 4672 */ String str = slea.readMapleAsciiString();

                    /* 4674 */ if (str.length() > 65) {
                        break;
                    }
                    /* 4677 */ StringBuilder stringBuilder = new StringBuilder();
                    /* 4678 */ addMedalString(c.getPlayer(), stringBuilder);
                    /* 4679 */ stringBuilder.append(c.getPlayer().getName());
                    /* 4680 */ stringBuilder.append(" : ");
                    /* 4681 */ stringBuilder.append(str);

                    /* 4683 */ boolean bool = (slea.readByte() != 0);

                    /* 4685 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                        /* 4686 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                        /* 4687 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str, "채널 : " + c.getChannel());
                        /* 4688 */ World.Broadcast.broadcastSmega(CWvsContext.serverNotice(9, c.getChannel(), c.getPlayer().getName(), stringBuilder.toString(), bool));
                        /* 4689 */ used = true;
                        break;
                    }
                    /* 4691 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                    break;
                }
                /* 4694 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5074000:
                /* 4699 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4700 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4703 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4704 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4707 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 4708 */ String str = slea.readMapleAsciiString();

                    /* 4710 */ if (str.length() > 65) {
                        break;
                    }
                    /* 4713 */ StringBuilder stringBuilder = new StringBuilder();
                    /* 4714 */ addMedalString(c.getPlayer(), stringBuilder);
                    /* 4715 */ stringBuilder.append(c.getPlayer().getName());
                    /* 4716 */ stringBuilder.append(" : ");
                    /* 4717 */ stringBuilder.append(str);

                    /* 4719 */ boolean bool = (slea.readByte() != 0);

                    /* 4721 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                        /* 4722 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                        /* 4723 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str, "채널 : " + c.getChannel());
                        /* 4724 */ World.Broadcast.broadcastSmega(CWvsContext.serverNotice(22, c.getChannel(), c.getPlayer().getName(), stringBuilder.toString(), bool));
                        /* 4725 */ used = true;
                        break;
                    }
                    /* 4727 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                    break;
                }
                /* 4730 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5072000:
                /* 4735 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4736 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4739 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4740 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4743 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 4744 */ String str = slea.readMapleAsciiString();

                    /* 4746 */ if (str.length() > 65) {
                        break;
                    }
                    /* 4749 */ StringBuilder stringBuilder = new StringBuilder();
                    /* 4750 */ addMedalString(c.getPlayer(), stringBuilder);
                    /* 4751 */ stringBuilder.append(c.getPlayer().getName());
                    /* 4752 */ stringBuilder.append(" : ");
                    /* 4753 */ stringBuilder.append(str);

                    /* 4755 */ boolean bool = (slea.readByte() != 0);

                    /* 4757 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                        /* 4758 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                        /* 4759 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str, "채널 : " + c.getChannel());
                        /* 4760 */ World.Broadcast.broadcastSmega(CWvsContext.serverNotice(3, c.getChannel(), c.getPlayer().getName(), stringBuilder.toString(), bool));
                        /* 4761 */ used = true;
                        break;
                    }
                    /* 4763 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                    break;
                }
                /* 4766 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5076000:
                /* 4771 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4772 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4775 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4776 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4779 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 4780 */ String str = slea.readMapleAsciiString();

                    /* 4782 */ if (str.length() > 65) {
                        break;
                    }
                    /* 4785 */ StringBuilder stringBuilder = new StringBuilder();
                    /* 4786 */ addMedalString(c.getPlayer(), stringBuilder);
                    /* 4787 */ stringBuilder.append(c.getPlayer().getName());
                    /* 4788 */ stringBuilder.append(" : ");
                    /* 4789 */ stringBuilder.append(str);

                    /* 4791 */ boolean bool = (slea.readByte() > 0);

                    /* 4793 */ Item item13 = null;
                    /* 4794 */ if (slea.readByte() == 1) {
                        /* 4795 */ byte invType = (byte) slea.readInt();
                        /* 4796 */ byte b1 = (byte) slea.readInt();
                        /* 4797 */ if (b1 <= 0) {
                            /* 4798 */ invType = -1;
                        }
                        /* 4800 */ item13 = c.getPlayer().getInventory(MapleInventoryType.getByType(invType)).getItem((short) b1);
                    }

                    /* 4803 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                        /* 4804 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                        /* 4805 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str, "채널 : " + c.getChannel());
                        /* 4806 */ World.Broadcast.broadcastSmega(CWvsContext.itemMegaphone(c.getPlayer().getName(), stringBuilder.toString(), bool, c.getChannel(), item13, itemId));
                        /* 4807 */ used = true;
                        break;
                    }
                    /* 4809 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                    break;
                }
                /* 4812 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5076100: {
                message = slea.readMapleAsciiString();
                sb = new StringBuilder();
                addMedalString(c.getPlayer(), sb);
                sb.append(c.getPlayer().getName());
                sb.append(" : ");
                sb.append(message);
                ear = slea.readByte() > 0;
                item = null;
                if (slea.readInt() == 1) { // item
                    byte invType = (byte) slea.readInt();
                    byte pos2 = (byte) slea.readInt();
                    if (pos2 <= 0) {
                        invType = -1;
                    }
                    item = c.getPlayer().getInventory(MapleInventoryType.getByType(invType)).getItem(pos2);
                }
                World.Broadcast.broadcastSmega(CWvsContext.HyperMegaPhone(sb.toString(), c.getPlayer().getName(), message,
                        c.getChannel(), ear, item));
                used = true;
                break;
            }
            case 5075003:
            case 5075004:
            case 5075005:
                /* 4845 */ if (c.getPlayer().getLevel() < 10) {
                    /* 4846 */ c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
                    break;
                }
                /* 4849 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 4850 */ c.getPlayer().dropMessage(5, "Cannot be used here.");
                    break;
                }
                /* 4853 */ tvType = itemId % 10;
                /* 4854 */ if (tvType == 3) {
                    /* 4855 */ slea.readByte();
                }
                /* 4857 */ ear = (tvType != 1 && tvType != 2 && slea.readByte() > 1);
                /* 4858 */ victim = (tvType == 1 || tvType == 4) ? null : c.getChannelServer().getPlayerStorage().getCharacterByName(slea.readMapleAsciiString());
                /* 4859 */ if (tvType == 0 || tvType == 3) {
                    /* 4860 */ victim = null;
                    /* 4861 */                } else if (victim == null) {
                    /* 4862 */ c.getPlayer().dropMessage(1, "That character is not in the channel.");
                    break;
                }
                /* 4865 */ str1 = slea.readMapleAsciiString();

                /* 4867 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                    /* 4868 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                    /* 4869 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), str1, "채널 : " + c.getChannel());
                    /* 4870 */ World.Broadcast.broadcastSmega(CWvsContext.serverNotice(3, c.getChannel(), c.getPlayer().getName(), c.getPlayer().getName() + " : " + str1, ear));
                    /* 4871 */ used = true;
                    break;
                }
                /* 4873 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                break;

            case 5100000:
                /* 4878 */ c.getPlayer().getMap().broadcastMessage(CField.musicChange("Jukebox/Congratulation"));
                /* 4879 */ used = true;
                break;

            case 5190000:
            case 5190001:
            case 5190002:
            case 5190003:
            case 5190004:
            case 5190005:
            case 5190006:
            case 5190010:
            case 5190011:
            case 5190012:
            case 5190013:
                /* 4894 */ uniqueId = slea.readLong();
                /* 4895 */ maplePet1 = null;
                /* 4896 */ petIndex = c.getPlayer().getPetIndex(uniqueId);
                /* 4897 */ if (petIndex >= 0) {
                    /* 4898 */ maplePet1 = c.getPlayer().getPet(petIndex);
                } else {
                    /* 4900 */ maplePet1 = c.getPlayer().getInventory(MapleInventoryType.CASH).findByUniqueId(uniqueId).getPet();
                }
                /* 4902 */ if (maplePet1 == null) {
                    /* 4903 */ c.getPlayer().dropMessage(1, "펫을 찾는데 실패하였습니다!");
                    /* 4904 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                /* 4907 */ zz = MaplePet.PetFlag.getByAddId(itemId);
                /* 4908 */ maplePet1.setFlags(maplePet1.getFlags() | zz.getValue());
                /* 4909 */ c.getPlayer().getMap().broadcastMessage(PetPacket.updatePet(c.getPlayer(), maplePet1, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(maplePet1.getInventoryPosition()), false, c.getPlayer().getPetLoot()));
                /* 4910 */ used = true;
                break;

            case 5191000:
            case 5191001:
            case 5191002:
            case 5191003:
            case 5191004:
                /* 4918 */ uniqueId = slea.readLong();
                /* 4919 */ maplePet1 = null;
                /* 4920 */ petIndex = c.getPlayer().getPetIndex(uniqueId);
                /* 4921 */ if (petIndex >= 0) {
                    /* 4922 */ maplePet1 = c.getPlayer().getPet(petIndex);
                } else {
                    /* 4924 */ maplePet1 = c.getPlayer().getInventory(MapleInventoryType.CASH).findByUniqueId(uniqueId).getPet();
                }
                /* 4926 */ if (maplePet1 == null) {
                    /* 4927 */ c.getPlayer().dropMessage(1, "펫을 찾는데 실패하였습니다!");
                    /* 4928 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                /* 4931 */ zz = MaplePet.PetFlag.getByAddId(itemId);
                /* 4932 */ maplePet1.setFlags(maplePet1.getFlags() - zz.getValue());
                /* 4933 */ c.getPlayer().getMap().broadcastMessage(PetPacket.updatePet(c.getPlayer(), maplePet1, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(maplePet1.getInventoryPosition()), false, c.getPlayer().getPetLoot()));
                /* 4934 */ used = true;
                break;

            case 5781002:
                /* 4938 */ l1 = slea.readLong();
                /* 4939 */ color = slea.readInt();
                /* 4940 */ maplePet2 = c.getPlayer().getPet(0L);
                /* 4941 */ i3 = 0;
                /* 4942 */ if (maplePet2 == null) {
                    break;
                }
                /* 4945 */ if (maplePet2.getUniqueId() != l1) {
                    /* 4946 */ maplePet2 = c.getPlayer().getPet(1L);
                    /* 4947 */ i3 = 1;
                    /* 4948 */ if (maplePet2 != null) {
                        /* 4949 */ if (maplePet2.getUniqueId() != l1) {
                            /* 4950 */ maplePet2 = c.getPlayer().getPet(2L);
                            /* 4951 */ i3 = 2;
                            /* 4952 */ if (maplePet2 == null
                                    || /* 4953 */ maplePet2.getUniqueId() != l1) {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }

                /* 4964 */ maplePet2.setColor(color);
                /* 4965 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PetPacket.showPet(c.getPlayer(), maplePet2, false, false), true);
                break;

            case 5501001:
            case 5501002:
                /* 4970 */ skil = SkillFactory.getSkill(slea.readInt());
                /* 4971 */ if (skil == null || skil.getId() / 10000 != 8000 || c.getPlayer().getSkillLevel(skil) <= 0 || !skil.isTimeLimited() || GameConstants.getMountItem(skil.getId(), c.getPlayer()) <= 0) {
                    break;
                }
                /* 4974 */ toAdd = (((itemId == 5501001) ? 30 : 60) * 24 * 60 * 60) * 1000L;
                /* 4975 */ expire = c.getPlayer().getSkillExpiry(skil);
                /* 4976 */ if (expire < System.currentTimeMillis() || expire + toAdd >= System.currentTimeMillis() + 31536000000L) {
                    break;
                }
                /* 4979 */ c.getPlayer().changeSingleSkillLevel(skil, c.getPlayer().getSkillLevel(skil), c.getPlayer().getMasterLevel(skil), expire + toAdd);
                /* 4980 */ used = true;
                break;

            case 5170000:
                /* 4984 */ uniqueid = slea.readLong();
                /* 4985 */ pet = c.getPlayer().getPet(0L);
                /* 4986 */ slo = 0;

                /* 4988 */ if (pet == null) {
                    break;
                }
                /* 4991 */ if (pet.getUniqueId() != uniqueid) {
                    /* 4992 */ pet = c.getPlayer().getPet(1L);
                    /* 4993 */ slo = 1;
                    /* 4994 */ if (pet != null) {
                        /* 4995 */ if (pet.getUniqueId() != uniqueid) {
                            /* 4996 */ pet = c.getPlayer().getPet(2L);
                            /* 4997 */ slo = 2;
                            /* 4998 */ if (pet == null
                                    || /* 4999 */ pet.getUniqueId() != uniqueid) {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }

                /* 5010 */ str2 = slea.readMapleAsciiString();
                /* 5011 */ for (String z : GameConstants.RESERVED) {
                    /* 5012 */ if (pet.getName().indexOf(z) != -1 || str2.indexOf(z) != -1) {
                        break;
                    }
                }
                /* 5016 */ pet.setName(str2);
                /* 5017 */ c.getSession().writeAndFlush(PetPacket.updatePet(c.getPlayer(), pet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false, c.getPlayer().getPetLoot()));
                /* 5018 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                /* 5019 */ c.getPlayer().getMap().broadcastMessage(CSPacket.changePetName(c.getPlayer(), str2, slo));
                /* 5020 */ used = true;
                break;

            case 5700000:
                /* 5024 */ slea.skip(8);
                /* 5025 */ if (c.getPlayer().getAndroid() == null) {
                    /* 5026 */ c.getPlayer().dropMessage(1, "장착중인 안드로이드가 없어 작명 할 수 없습니다.");
                    break;
                }
                /* 5029 */ nName = slea.readMapleAsciiString();
                for (String z : GameConstants.RESERVED) {
                    if (c.getPlayer().getAndroid().getName().indexOf(z) != -1 || nName.indexOf(z) != -1) {
                        break;
                    }
                }
                /* 5035 */ c.getPlayer().getAndroid().setName(nName);
                /* 5036 */ c.getPlayer().setAndroid(c.getPlayer().getAndroid());
                /* 5037 */ used = true;
                break;

            case 5240000:
            case 5240001:
            case 5240002:
            case 5240003:
            case 5240004:
            case 5240005:
            case 5240006:
            case 5240007:
            case 5240008:
            case 5240009:
            case 5240010:
            case 5240011:
            case 5240012:
            case 5240013:
            case 5240014:
            case 5240015:
            case 5240016:
            case 5240017:
            case 5240018:
            case 5240019:
            case 5240020:
            case 5240021:
            case 5240022:
            case 5240023:
            case 5240024:
            case 5240025:
            case 5240026:
            case 5240027:
            case 5240028:
            case 5240029:
            case 5240030:
            case 5240031:
            case 5240032:
            case 5240033:
            case 5240034:
            case 5240035:
            case 5240036:
            case 5240037:
            case 5240038:
            case 5240039:
            case 5240040:
            case 5240088:
                /* 5082 */ for (MaplePet maplePet : c.getPlayer().getPets()) {
                    /* 5083 */ if (maplePet != null
                            && /* 5084 */ !maplePet.canConsume(itemId)) {
                        /* 5085 */ int petindex = c.getPlayer().getPetIndex(maplePet);
                        /* 5086 */ maplePet.setFullness(100);
                        /* 5087 */ if (maplePet.getCloseness() < 30000) {
                            /* 5088 */ if (maplePet.getCloseness() + 100 > 30000) {
                                /* 5089 */ maplePet.setCloseness(30000);
                            } else {
                                /* 5091 */ maplePet.setCloseness(maplePet.getCloseness() + 100);
                            }
                            /* 5093 */ if (maplePet.getCloseness() >= GameConstants.getClosenessNeededForLevel(maplePet.getLevel() + 1)) {
                                /* 5094 */ maplePet.setLevel(maplePet.getLevel() + 1);
                                /* 5095 */ c.getSession().writeAndFlush(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), maplePet.getPetItemId(), true));
                                /* 5096 */ c.getPlayer().getMap().broadcastMessage(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), maplePet.getPetItemId(), false));
                            }
                        }
                        /* 5099 */ c.getSession().writeAndFlush(PetPacket.updatePet(c.getPlayer(), maplePet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(maplePet.getInventoryPosition()), false, c.getPlayer().getPetLoot()));
                        /* 5100 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PetPacket.commandResponse(c.getPlayer().getId(), (byte) 1, (byte) petindex, true), true);
                    }
                }

                /* 5104 */ used = true;
                break;

            case 5370000:
            case 5370001:
                /* 5121 */ c.getPlayer().setChalkboard(slea.readMapleAsciiString());
                break;

            case 5079000:
            case 5079001:
            case 5390000:
            case 5390001:
            case 5390002:
            case 5390003:
            case 5390004:
            case 5390005:
            case 5390006:
            case 5390007:
            case 5390008:
            case 5390009:
            case 5390010:
            case 5390011:
            case 5390012:
            case 5390013:
            case 5390014:
            case 5390015:
            case 5390016:
            case 5390017:
            case 5390018:
            case 5390019:
            case 5390020:
            case 5390021:
            case 5390022:
            case 5390023:
            case 5390024:
            case 5390025:
            case 5390026:
            case 5390027:
            case 5390028:
            case 5390029:
            case 5390030:
            case 5390031:
            case 5390032:
            case 5390033:
                /* 5160 */ if (c.getPlayer().getLevel() < 10) {
                    /* 5161 */ c.getPlayer().dropMessage(5, "10 레벨 이상이어야합니다.");
                    break;
                }
                /* 5164 */ if (c.getPlayer().getMapId() == 180000002) {
                    /* 5165 */ c.getPlayer().dropMessage(5, "여기에서는 사용하실 수 없습니다.");
                    break;
                }
                /* 5168 */ if (!c.getChannelServer().getMegaphoneMuteState()) {
                    /* 5169 */ List<String> lines = new LinkedList<>();
                    /* 5170 */ StringBuilder stringBuilder = new StringBuilder();
                    /* 5171 */ for (int i8 = 0; i8 < 4; i8++) {
                        /* 5172 */ String text = slea.readMapleAsciiString();
                        /* 5173 */ if (text.length() > 55) {
                            /* 5174 */ lines.add("");
                        } else {
                            /* 5176 */ lines.add(text);
                            /* 5177 */ stringBuilder.append(text);
                        }
                    }
                    /* 5180 */ boolean bool = (slea.readByte() != 0);

                    /* 5182 */ if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
                        /* 5183 */ World.Broadcast.chatDelay = System.currentTimeMillis();
                        /* 5184 */ DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(), stringBuilder.toString(), "채널 : " + c.getChannel());
                        /* 5185 */ World.Broadcast.broadcastSmega(CWvsContext.getAvatarMega(c.getPlayer(), c.getChannel(), itemId, lines, bool));
                        /* 5186 */ used = true;
                        break;
                    }
                    /* 5188 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
                    break;
                }
                /* 5191 */ c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
                break;

            case 5450000:
            case 5450003:
            case 5452001:
                /* 5198 */ for (int i8 : GameConstants.blockedMaps) {
                    /* 5199 */ if (c.getPlayer().getMapId() == i8) {
                        /* 5200 */ c.getPlayer().dropMessage(5, "You may not use this command here.");
                        /* 5201 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                }
                /* 5205 */ if (c.getPlayer().getLevel() < 10) {
                    /* 5206 */ c.getPlayer().dropMessage(5, "You must be over level 10 to use this command.");
                    break;
                    /* 5207 */                }
                if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || c.getPlayer().getMapId() / 100 == 1030008 || c.getPlayer().getMapId() / 100 == 922010 || c.getPlayer().getMapId() / 10 == 13003000) {
                    /* 5208 */ c.getPlayer().dropMessage(5, "You may not use this command here.");
                    break;
                }
                /* 5210 */ MapleShopFactory.getInstance().getShop(61).sendShop(c);
                break;

            case 5300000:
            case 5300001:
            case 5300002:
                /* 5218 */ ii = MapleItemInformationProvider.getInstance();
                /* 5219 */ ii.getItemEffect(itemId).applyTo(c.getPlayer(), true);
                /* 5220 */ used = true;
                break;

            case 5330000:
                /* 5224 */ c.getPlayer().setConversation(2);
                /* 5225 */ c.getSession().writeAndFlush(CField.sendDuey((byte) 9, null, null));
                break;

            case 5062400:
            case 5062402:
            case 5062405:
                /* 5241 */ viewSlot = (short) slea.readInt();
                /* 5242 */ descSlot = (short) slea.readInt();
                /* 5243 */ view_Item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(viewSlot);
                /* 5244 */ desc_Item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(descSlot);
                /* 5245 */ if (view_Item.getMoru() != 0) {
                    /* 5246 */ desc_Item.setMoru(view_Item.getMoru());
                } else {
                    /* 5248 */ String lol = Integer.valueOf(view_Item.getItemId()).toString();
                    /* 5249 */ String ss = lol.substring(3, 7);
                    /* 5250 */ desc_Item.setMoru(Integer.parseInt(ss));
                }
                /* 5252 */ c.getPlayer().forceReAddItem((Item) desc_Item, MapleInventoryType.EQUIP);

                /* 5254 */ used = true;
                break;

            case 5130000:
                /* 5258 */ if (c.getPlayer().getKeyValue(210416, "TotalDeadTime") <= 0L) {
                    /* 5259 */ c.getPlayer().dropMessage(5, "캐릭터 사망으로 인한 경험치 획득, 드롭률 감소 효과가 적용 중일 때에만 사용할 수 있습니다.");
                    /* 5260 */ c.send(CWvsContext.enableActions(c.getPlayer()));
                    break;
                }
                /* 5262 */ c.send(CField.ExpDropPenalty(false, 0, 0, 0, 0));
                /* 5263 */ c.getPlayer().removeKeyValue(210416);
                /* 5264 */ used = true;
                break;

            case 5155006:
                /* 5269 */ if (c.getPlayer().getKeyValue(7786, "sw") == 0L) {
                    /* 5270 */ c.getPlayer().setKeyValue(7786, "sw", "1");
                    /* 5271 */ c.getPlayer().dropMessage(5, "신비한 힘으로 심연의 세례가 드러났습니다.");
                } else {
                    /* 5273 */ c.getPlayer().setKeyValue(7786, "sw", "0");
                    /* 5274 */ c.getPlayer().dropMessage(5, "신비한 힘으로 심연의 세례가 감춰졌습니다.");
                }
                /* 5276 */ used = true;
                break;

            case 5155002:
                /* 5280 */ if (c.getPlayer().getKeyValue(7786, "sw") == 0L) {
                    /* 5281 */ c.getPlayer().setKeyValue(7786, "sw", "1");
                    /* 5282 */ c.getPlayer().dropMessage(5, "신비한 힘으로 제너레이트 마크가 드러났습니다.");
                } else {
                    /* 5284 */ c.getPlayer().setKeyValue(7786, "sw", "0");
                    /* 5285 */ c.getPlayer().dropMessage(5, "신비한 힘으로 제너레이트 마크가 감춰졌습니다.");
                }
                /* 5287 */ used = true;
                break;

            case 5155003:
                /* 5291 */ if (c.getPlayer().getKeyValue(7786, "sw") == 0L) {
                    /* 5292 */ c.getPlayer().setKeyValue(7786, "sw", "1");
                    /* 5293 */ c.getPlayer().dropMessage(5, "신비한 힘으로 마족의 표식이 드러났습니다.");
                } else {
                    /* 5295 */ c.getPlayer().setKeyValue(7786, "sw", "0");
                    /* 5296 */ c.getPlayer().dropMessage(5, "신비한 힘으로 마족의 표식이 감춰졌습니다.");
                }
                /* 5298 */ used = true;
                break;

            default:
                /* 5302 */ if (itemId / 10000 == 512 || itemId == 2432290) {
                    /* 5303 */ MapleItemInformationProvider mapleItemInformationProvider = MapleItemInformationProvider.getInstance();
                    /* 5304 */ String msg = mapleItemInformationProvider.getMsg(itemId);
                    /* 5305 */ String ourMsg = slea.readMapleAsciiString();

                    /* 5320 */ c.getPlayer().getMap().startMapEffect(ourMsg, itemId);

                    /* 5322 */ int buff = mapleItemInformationProvider.getStateChangeItem(itemId);
                    /* 5323 */ if (buff != 0) {
                        /* 5324 */ for (MapleCharacter mChar : c.getPlayer().getMap().getCharactersThreadsafe()) {
                            /* 5325 */ mapleItemInformationProvider.getItemEffect(buff).applyTo(mChar, true);
                        }
                    }
                    /* 5328 */ used = true;
                    break;
                    /* 5329 */                }
                if (itemId / 10000 == 510) {
                    /* 5330 */ c.getPlayer().getMap().startJukebox(c.getPlayer().getName(), itemId);
                    /* 5331 */ used = true;
                    break;
                    /* 5332 */                }
                if (itemId / 10000 == 562) {
                    /* 5333 */ if (UseSkillBook(slot, itemId, c, c.getPlayer())) /* 5334 */ {
                        c.getPlayer().gainSP(1);
                    }
                    break;
                }
                /* 5336 */ if (itemId / 10000 == 553) {
                    /* 5337 */ UseRewardItem(slot, itemId, c, c.getPlayer());
                    break;
                    /* 5338 */                }
                if (itemId / 10000 == 524) {
                    /* 5339 */ for (MaplePet maplePet : c.getPlayer().getPets()) {
                        /* 5340 */ if (maplePet != null
                                && /* 5341 */ maplePet.canConsume(itemId)) {
                            /* 5342 */ int petindex = c.getPlayer().getPetIndex(maplePet);
                            /* 5343 */ maplePet.setFullness(100);
                            /* 5344 */ if (maplePet.getCloseness() < 30000) {
                                /* 5345 */ if (maplePet.getCloseness() + 100 > 30000) {
                                    /* 5346 */ maplePet.setCloseness(30000);
                                } else {
                                    /* 5348 */ maplePet.setCloseness(maplePet.getCloseness() + 100);
                                }
                                /* 5350 */ if (maplePet.getCloseness() >= GameConstants.getClosenessNeededForLevel(maplePet.getLevel() + 1)) {
                                    /* 5351 */ maplePet.setLevel(maplePet.getLevel() + 1);
                                    /* 5352 */ c.getSession().writeAndFlush(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), maplePet.getPetItemId(), true));
                                    /* 5353 */ c.getPlayer().getMap().broadcastMessage(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), maplePet.getPetItemId(), false));
                                }
                            }
                            /* 5356 */ c.getSession().writeAndFlush(PetPacket.updatePet(c.getPlayer(), maplePet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(maplePet.getInventoryPosition()), false, c.getPlayer().getPetLoot()));
                            /* 5357 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PetPacket.commandResponse(c.getPlayer().getId(), (byte) 1, (byte) petindex, true), true);
                        }
                    }

                    /* 5361 */ used = true;
                    break;
                    /* 5362 */                }
                if (itemId / 10000 != 519) {
               //     /* 5363 */ System.out.println("Unhandled CS item : " + itemId);
                 //   /* 5364 */ System.out.println(slea.toString(true));
                }
                break;
        }

        /* 5369 */ if (used) {
            /* 5370 */ if (ItemFlag.KARMA_USE.check(toUse.getFlag())) {
                /* 5371 */ toUse.setFlag(toUse.getFlag() - ItemFlag.KARMA_USE.getValue() + ItemFlag.UNTRADEABLE.getValue());
                /* 5372 */ c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.CASH, toUse));
            }

            /* 5375 */ MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false, true);
        }

        /* 5379 */ c.getSession()
                /* 5380 */.writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        /* 5381 */ if (cc) {
            /* 5382 */ if (!c.getPlayer().isAlive() || c.getPlayer().getEventInstance() != null || FieldLimitType.ChannelSwitch.check(c.getPlayer().getMap().getFieldLimit())) {
                /* 5383 */ c.getPlayer().dropMessage(1, "Auto relog failed.");
                return;
            }
            /* 5386 */ c.getPlayer().dropMessage(5, "Auto relogging. Please wait.");
            /* 5387 */ c.getPlayer().fakeRelog();
        }
    }

    public static final void Pickup_Player(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        slea.readInt();
        slea.skip(1);
        Point Client_Reportedpos = slea.readPos();
        if (chr == null || chr.getMap() == null) {
            return;
        }
        chr.setScrolledPosition((short) 0);
        MapleMapObject ob = chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.ITEM);
        if (ob == null) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        MapleMapItem mapitem = (MapleMapItem) ob;
        try {
            mapitem.getLock().lock();
            if (mapitem.isPickedUp()) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (mapitem.getItemId() == 2431174) {
                int rand = Randomizer.rand(1, 10) * 10;
                c.getPlayer().gainHonor(rand);
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
                InventoryHandler.removeItem(chr, mapitem, ob);
                return;
            }
            
             if (mapitem.getItemId() == 2433103) {
                c.getPlayer().gainHonor(10000);
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
                InventoryHandler.removeItem(chr, mapitem, ob);
                return;
            }
            
            if (mapitem.getItemId() == 2433019) {
                int ranmeso = Randomizer.rand(1000000, 100000000);
                chr.gainMeso(ranmeso, true);
                InventoryHandler.removeItem(chr, mapitem, ob);
                 c.getPlayer().dropMessage(-8, "메소럭키백에서 메소를 " + ranmeso + "메소 만큼 휙득 하였습니다.");
                return;
            }
            
                        if (mapitem.getItemId() == 2433979) {
                int ranmeso = Randomizer.rand(1000000, 100000000);
                chr.gainMeso(ranmeso, true);
                InventoryHandler.removeItem(chr, mapitem, ob);
                 c.getPlayer().dropMessage(-8, "메소럭키백에서 메소를 " + ranmeso + "메소 만큼 휙득 하였습니다.");
                return;
            }
            
            /* 4813 */       if (mapitem.getItemId() == 4001169 && c.getPlayer().getMap().getMonstermarble() != 20) {
/* 4814 */         c.getPlayer().getMap().setMonstermarble(c.getPlayer().getMap().getMonstermarble() + 1);
/* 4815 */         c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
/* 4816 */         c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("몬스터구슬 " + c.getPlayer().getMap().getMonstermarble() + " / 20"));
/* 4817 */         removeItem(chr, mapitem, ob); return;
/*      */       } 
/* 4819 */       if (mapitem.getItemId() == 4001169) {
/* 4820 */         removeItem(chr, mapitem, ob);
/*      */         
/*      */         return;
/*      */       } 
            //파티퀘스트 시작
            /* 4824 */       if (mapitem.getItemId() == 4001101 && c.getPlayer().getMap().getMoonCake() != 80) {
/* 4825 */         c.getPlayer().getMap().setMoonCake(c.getPlayer().getMap().getMoonCake() + 1);
/* 4826 */         c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
/* 4827 */         c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("어흥이를 위해 월묘의 떡 " + c.getPlayer().getMap().getMoonCake() + "개를 모았습니다.  앞으로 " + (80 - c.getPlayer().getMap().getMoonCake()) + "개 더!"));
/* 4828 */         removeItem(chr, mapitem, ob); return;
/*      */       } 
/* 4830 */       if (mapitem.getItemId() == 4001101) {
/* 4831 */         removeItem(chr, mapitem, ob);
/*      */         return;
/*      */       } 
/* 4834 */       if (mapitem.getItemId() == 4000884) {
/* 4835 */         c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("달맞이꽃 씨앗을 되찾았습니다.", 5120016, true));
/*      */       }

/* 4837 */       if (mapitem.getItemId() >= 2432139 && mapitem.getItemId() < 2432149) {
/* 4838 */         EventManager em = c.getChannelServer().getEventSM().getEventManager("KerningPQ");
/* 4839 */         String stage4 = em.getProperty("stage4r");
/* 4840 */         String stage4M = c.getPlayer().getEventInstance().getProperty("stage4M");
/* 4841 */         int getPQ = Integer.parseInt(stage4M);
/* 4842 */         for (int i = 1; i < 10; i++) {
/* 4843 */           if (mapitem.getItemId() == 2432139 + i) {
/* 4844 */             if (getPQ != c.getPlayer().getMap().getKerningPQ()) {
/* 4845 */               if (stage4 == "0") {
/* 4846 */                 c.getPlayer().getMap().setKerningPQ(i);
/*      */               } else {
/* 4848 */                 int a; switch (stage4) {
/*      */                   case "1":
/* 4850 */                     c.getPlayer().dropMessage(5, "" + i + "/" + stage4);
/* 4851 */                     c.getPlayer().getMap().setKerningPQ(c.getPlayer().getMap().getKerningPQ() + i);
/* 4852 */                     c.getPlayer().dropMessage(5, "" + c.getPlayer().getMap().getKerningPQ());
/*      */                     break;
/*      */                   case "2":
/* 4855 */                     if (stage4 == "0") {
/*      */                       break;
/*      */                     }
/* 4858 */                     c.getPlayer().getMap().setKerningPQ(c.getPlayer().getMap().getKerningPQ() - i);
/*      */                     break;
/*      */                   
/*      */                   case "3":
/* 4862 */                     if (stage4 == "0") {
/*      */                       break;
/*      */                     }
/* 4865 */                     c.getPlayer().getMap().setKerningPQ(c.getPlayer().getMap().getKerningPQ() * i);
/*      */                     break;
/*      */                   
/*      */                   case "4":
/* 4869 */                     if (stage4 == "0") {
/*      */                       break;
/*      */                     }
/* 4872 */                     a = (int)Math.floor((c.getPlayer().getMap().getKerningPQ() / i));
/* 4873 */                     c.getPlayer().getMap().setKerningPQ(a);
/*      */                     break;
/*      */                 } 
/*      */               
/*      */               } 
/* 4878 */               c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("현재 숫자 : " + c.getPlayer().getMap().getKerningPQ()));
/* 4879 */               c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("목표 숫자 : " + stage4M, 5120017, true));
/* 4880 */               if (getPQ == c.getPlayer().getMap().getKerningPQ()) {
/* 4881 */                 c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(75));
/* 4882 */                 c.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 4883 */                 c.getPlayer().getMap().broadcastMessage(CField.environmentChange("gate", 2));
/*      */               } 
/*      */             } 
/* 4886 */             c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
/* 4887 */             removeItem(chr, mapitem, ob);
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } 

/* 4892 */       if (mapitem.getItemId() == 4001022) {
/* 4893 */         c.getPlayer().getMap().setRPTicket(c.getPlayer().getMap().getRPTicket() + 1);
/* 4894 */         c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
/* 4895 */         c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("통행증 " + c.getPlayer().getMap().getRPTicket() + "장을 모았습니다."));
/* 4896 */         if (c.getPlayer().getMap().getRPTicket() == 20) {
/* 4897 */           c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(10));
/* 4898 */           c.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 4899 */           c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("통행증을 모두 모았습니다. 레드 벌룬에게 말을 걸어 다음 단계로 이동해 주세요.", 5120018, true));
/*      */         } 
/* 4901 */         removeItem(chr, mapitem, ob); return;
/*      */       } 
/* 4903 */       if (mapitem.getItemId() == 4001022) {
/* 4904 */         removeItem(chr, mapitem, ob);
/*      */         return;
/*      */       } 
            
            if (mapitem.getItemId() == 2023484 || mapitem.getItemId() == 2023494 || mapitem.getItemId() == 2023495 || mapitem.getItemId() == 2023669 || mapitem.getItemId() == 2023927) {
                if (mapitem.getDropper() instanceof MapleMonster) {
                    if (chr.getClient().getKeyValue("combokill") == null) {
                        chr.getClient().setKeyValue("combokill", "1");
                        chr.getClient().send(CField.ImageTalkNpc(9010049, 10000, "#b[안내] 콤보킬 퍼레이드#k\r\n\r\n콤보를 50단위씩 쌓아서 얻는 #b[콤보킬 퍼레이드]#k를 획득 하셨어요!\r\n\r\n#b[콤보킬 퍼레이드]#k에 접촉 시 #b추가 보너스 경험치#k를 획득할 수 있어요!"));
                    }
                    int bonus = mapitem.getItemId() % 100 == 84 ? 5 : (mapitem.getItemId() % 100 == 94 ? 7 : (mapitem.getItemId() % 100 == 95 ? 10 : 12));
                    if (chr.getSkillLevel(20000297) > 0) {
                        bonus += SkillFactory.getSkill(20000297).getEffect(chr.getSkillLevel(20000297)).getX() / 100;
                    } else if (chr.getSkillLevel(80000370) > 0) {
                        bonus += SkillFactory.getSkill(80000370).getEffect(chr.getSkillLevel(80000370)).getX() / 100;
                    }
                    if (mapitem.getItemId() == 2023927) {
                        bonus = 30;
                    }
                    MapleMonster mob = (MapleMonster) mapitem.getDropper();
                    long exp = mob.getMobExp() * (long) bonus;
                    chr.gainExp(exp, true, true, false);
                    c.send(CField.EffectPacket.gainExp(exp));
                }
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
                InventoryHandler.removeItem(chr, mapitem, ob);
                return;
            }
            if (mapitem.getItemId() == 2434851) {
                if (!chr.getBuffedValue(25121133)) {
                    c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                SecondaryStatEffect effect = SkillFactory.getSkill(25121133).getEffect(1);
                long duration = chr.getBuffLimit(25121133);
                if ((duration += 4000L) >= (long) effect.getCoolRealTime()) {
                    duration = effect.getCoolRealTime();
                }
                effect.applyTo(chr, chr, false, chr.getPosition(), (int) duration, (byte) 0, false);
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
                InventoryHandler.removeItem(chr, mapitem, ob);
                return;
            }
            if (mapitem.getItemId() == 2002058) {
                if (c.getPlayer().hasDisease(SecondaryStat.DeathMark)) {
                    c.getPlayer().cancelDisease(SecondaryStat.DeathMark);
                }
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
                InventoryHandler.removeItem(chr, mapitem, ob);
                return;
            }
            if (mapitem.getItemId() == 4310229) {
              if (c.getKeyValue("유니온코인") == null) {
                   c.setKeyValue("유니온코인", "0");
        }
                short qty = mapitem.getItem().getQuantity();
            c.getPlayer().setKeyValue(500629, "point", "" + (Integer.parseInt(c.getKeyValue("유니온코인")) + qty) + "");
            c.setKeyValue("유니온코인", Integer.parseInt(c.getKeyValue("유니온코인")) + qty + "");
                c.getPlayer().setKeyValue(500629, "point", (c.getPlayer().getKeyValue(500629, "point") + qty) + "");
                c.getPlayer().dropMessage(-8, "유니온 코인을 " + qty + "개 획득하여 " + c.getPlayer().getKeyValue(500629, "point") + "개를 소지하고 있습니다.");
                removeItem(chr, mapitem, ob);
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (mapitem.getItemId() == 4001849 || mapitem.getItemId() == 4001847) {
                if (chr.getBattleGroundChr() != null) {
                    chr.setSkillCustomInfo(80001741, chr.getSkillCustomValue0(80001741) + 1L, 0L);
                    chr.getBattleGroundChr().setTeam(2);
                    chr.getMap().broadcastMessage(chr, BattleGroundPacket.UpdateAvater(chr.getBattleGroundChr(), GameConstants.BattleGroundJobType(chr.getBattleGroundChr())), false);
                    chr.getBattleGroundChr().setTeam(1);
                    chr.getClient().send(BattleGroundPacket.UpdateAvater(chr.getBattleGroundChr(), GameConstants.BattleGroundJobType(chr.getBattleGroundChr())));
                }
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                InventoryHandler.removeItem(chr, mapitem, ob);
                return;
            }
            if (mapitem.getItemId() >= 4034942 && mapitem.getItemId() <= 4034958) {
                if (((Integer) c.getPlayer().getRecipe().left).intValue() == mapitem.getItemId()) {
                    c.getPlayer().setRecipe(new Pair<Integer, Integer>(mapitem.getItemId(), (Integer) c.getPlayer().getRecipe().right + 1));
                } else {
                    c.getPlayer().setRecipe(new Pair<Integer, Integer>(mapitem.getItemId(), 1));
                }
                chr.getMap().broadcastMessage(CField.addItemMuto(chr));
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
                InventoryHandler.removeItem(chr, mapitem, ob);
                return;
            }
            boolean touchitem = false;
            switch (mapitem.getItemId()) {
                case 2633304:
                case 2633609: {
                    String[] bossname = new String[]{"이지 시그너스", "하드 힐라", "카오스 핑크빈", "노멀 시그너스", "카오스 자쿰", "카오스 피에르", "카오스 반반", "카오스 블러디퀸", "하드 매그너스", "카오스 벨룸", "카오스 파풀라투스", "노멀 스우", "노멀 데미안", "이지 루시드", "노멀 루시드", "노멀 윌", "노멀 더스크", "노멀 듄켈", "하드 데미안", "하드 스우", "하드 루시드", "하드 윌", "카오스 더스크", "하드 듄켈", "진 힐라", "세렌"};
                    String[] wishCoinCheck = null;
                    if (ServerConstants.Event_Blooming) {
                        wishCoinCheck = chr.getClient().getKeyValue("WishCoin").split("");
                    } else if (ServerConstants.Event_MapleLive) {
                        wishCoinCheck = chr.getClient().getCustomKeyValueStr(501468, "reward").split("");
                    }
                    String NewKeyvalue = "";
                    int i = 0;
                    for (Pair<Integer, Integer> list : ServerConstants.NeoPosList) {
                        MapleMonster mob = (MapleMonster) mapitem.getDropper();
                        if (list.getLeft().intValue() == mob.getId()) {
                            int questid;
                            int coincount = list.getRight();
                            if (ServerConstants.Event_Blooming) {
                                if (Randomizer.isSuccess(50)) {
                                    chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "자아, #r" + bossname[i] + "#k 처치~\r\n위시 코인 #b" + coincount + "#k개 찾았지"));
                                } else {
                                    chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "우와! #r" + bossname[i] + "#k 처치~\r\n위시 코인 #b" + coincount + "#k개 찾았다~"));
                                }
                            } else if (ServerConstants.Event_MapleLive) {
                                questid = 501471 + i;
                                if (chr.getClient().getCustomKeyValue(questid, "state") == 1L || chr.getClient().getCustomKeyValue(questid, "state") == 2L) {
                                    chr.getClient().send(CField.enforceMsgNPC(9062558, 3000, "이런, 이미 이번주에 #r" + bossname[i] + "#k를 처치했군."));
                                    break;
                                }
                                if (Randomizer.isSuccess(50)) {
                                    chr.getClient().send(CField.enforceMsgNPC(9062558, 3000, "호오, #r" + bossname[i] + "#k를 처치했군.\r\n검은콩 #b" + coincount + "#k개를 찾았다."));
                                } else {
                                    chr.getClient().send(CField.enforceMsgNPC(9062558, 3000, "좋아, #r" + bossname[i] + "#k를 처치했군.\r\n검은콩 #b" + coincount + "#k개를 찾았어."));
                                }
                            }
                            wishCoinCheck[i] = "1";
                            if (ServerConstants.Event_Blooming) {
                                chr.getClient().removeKeyValue("WishCoin");
                            }
                            for (int to = 0; to < wishCoinCheck.length; ++to) {
                                NewKeyvalue = NewKeyvalue + wishCoinCheck[to];
                            }
                            if (ServerConstants.Event_Blooming) {
                                chr.getClient().setKeyValue("WishCoin", NewKeyvalue);
                                if (Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) >= 400) {
                                    chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "이번 주차 위시 코인 #r400개#k\r\n 모두 획득~ 냐냐냐~"));
                                    return;
                                }
                                if (Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) + coincount >= 400) {
                                    coincount = Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) + coincount - 400;
                                }
                                chr.getClient().setKeyValue("WishCoinGain", Integer.parseInt(chr.getClient().getKeyValue("WishCoinGain")) + coincount + "");
                                chr.getClient().setKeyValue("WishCoinWeekGain", Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) + coincount + "");
                                break;
                            }
                            if (!ServerConstants.Event_MapleLive) {
                                break;
                            }
                            questid = 501471 + i;
                            chr.getClient().setCustomKeyValue(501468, "reward", NewKeyvalue);
                            chr.getClient().setCustomKeyValue(questid, "clear", "1");
                            chr.getClient().setCustomKeyValue(questid, "state", "1");
                            break;
                        }
                        ++i;
                    }
                    touchitem = true;
                    break;
                }
                case 2633343:
                    /* 5543 */ if (chr.getBuffedValue(80003046)) {
                        /* 5544 */ int added = 0;
                        /* 5545 */ int adddd = (chr.getQuestStatus(100801) == 2) ? 1 : ((chr.getQuestStatus(100802) == 2) ? 2 : 0);
                        /* 5546 */ for (int j = 0; j < adddd; j++) {
                            /* 5547 */ if (Randomizer.isSuccess(50)) {
                                /* 5548 */ added++;
                            }
                        }
                        /* 5551 */ int now = (int) chr.getKeyValue(100803, "count");
                        /* 5552 */ if (now < 0) {
                            /* 5553 */ now = 0;
                        }
                        /* 5555 */ chr.setKeyValue(100803, "count", (now + 1) + "");
                        /* 5556 */ chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 2 : 1, mapitem.getPosition());
                        /* 5557 */ chr.getClient().send(SLFCGPacket.EventSkillOnFlowerEffect(7, 1));
                        /* 5558 */ if (chr.getKeyValue(100803, "count") == 5L) {
                            /* 5559 */ chr.getClient().send(CField.enforceMsgNPC(9062527, 1300, "꽃의 보석에 햇살의 힘이 #r절반#k이나 모였어요!"));
                            /* 5560 */                        } else if (chr.getKeyValue(100803, "count") == 10L) {

                            /* 5562 */ chr.setKeyValue(100803, "count", "0");
                            /* 5563 */ chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20, chr.getPosition());
                            /* 5564 */ chr.getClient().send(SLFCGPacket.EventSkillOn(1, 1 + added));
                            /* 5565 */ chr.getClient().send(CField.enforceMsgNPC(9062527, 1000, "햇살의 힘으로 #r꽃씨#k를 펑!펑!"));
                            /* 5566 */ chr.getClient().send(CField.UIPacket.detailShowInfo("하나의 꽃씨 뿌리기로 블루밍 코인 " + ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20) + "개를 획득했습니다.", 3, 20, 20));
                            /* 5567 */ if (added >= 1 && chr.getQuestStatus(100801) == 2) {
                                /* 5568 */ Timer.BuffTimer.getInstance().schedule(() -> {
                                    /* 5569 */ chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 20 : 10, chr.getPosition());
                                    chr.getClient().send(CField.enforceMsgNPC(9062528, 1000, "#r꽃비#k가 쏴아아!\r\n너무 예뻐!"));
                                    /* 5571 */ chr.getClient().send(CField.UIPacket.detailShowInfo("두나의 꽃비로 블루밍 코인 " + ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 20 : 10) + "개를 획득했습니다.", 3, 20, 20));
                                },
                                        2500L);
                            }
                            /* 5574 */ if (added == 2 && chr.getQuestStatus(100802) == 2) {
                                /* 5575 */ Timer.BuffTimer.getInstance().schedule(() -> {
                                    /* 5576 */ chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20, chr.getPosition());
                                    chr.getClient().send(CField.enforceMsgNPC(9062529, 1000, "#r햇살#k이 샤라랑!\r\n꽃들아 잘 자라라~!"));
                                    /* 5578 */ chr.getClient().send(CField.UIPacket.detailShowInfo("세나의 햇살 비추기로 블루밍 코인 " + ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20) + "개를 획득했습니다.", 3, 20, 20));
                                },
                                        4500L);
                            }
                        }
                        /* 5582 */ if (chr.getKeyValue(100711, "today") >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000L : 3000L)) {
                            /* 5583 */ chr.getClient().send(CField.enforceMsgNPC(9062527, 1000, "오늘은 이만하면 됐어요."));
                            /* 5584 */ chr.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(1297));
                            /* 5585 */ chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062524, 0));
                            /* 5586 */ chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062525, 0));
                            /* 5587 */ chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062526, 0));

                            /* 5589 */ chr.cancelEffect(chr.getBuffedEffect(SecondaryStat.EventSpecialSkill));
                        }
                    }
                    /* 5592 */ touchitem = true;
                    break;
                case 2432391: {
                    int ranexp = Randomizer.rand(1000000, 3000000);
                    chr.gainExp(ranexp, true, true, true);
                    touchitem = true;
                    break;
                }
                case 2432392: {
                    int ranexp = Randomizer.rand(1000000, 3000000);
                    chr.gainExp(ranexp * 2, true, true, true);
                    touchitem = true;
                    break;
                }
                case 2432393: {
                    int ranmeso = Randomizer.rand(50000, 70000);
                    chr.gainMeso(ranmeso, true);
                    touchitem = true;
                    break;
                }
                case 2432394: {
                    int ranmeso = Randomizer.rand(50000, 100000);
                    chr.gainMeso(ranmeso, true);
                    touchitem = true;
                    break;
                }
                case 2432395: {
                    int[] itemlist = new int[]{2000005, 2001556, 2001554, 2001530};
                    int Random2 = (int) Math.floor(Math.random() * (double) itemlist.length);
                    int finalitemid = itemlist[Random2];
                    chr.gainItem(finalitemid, Randomizer.rand(1, 10));
                    touchitem = true;
                    break;
                }
                case 2432396: {
                    int[] itemlist = new int[]{1082608, 1082609, 1082610, 1082611, 1082612, 1072967, 1072968, 1072969, 1072970, 1072971, 1052799, 0x101080, 0x101081, 1052802, 1052803, 1004229, 1004230, 1004231, 1004232, 1004233, 1102718, 1102719, 1102720, 1102721, 1102722};
                    int Random3 = (int) Math.floor(Math.random() * (double) itemlist.length);
                    int finalitemid = itemlist[Random3];
                    Equip Item2 = (Equip) MapleItemInformationProvider.getInstance().getEquipById(finalitemid);
                    if (Item2 == null) {
                        InventoryHandler.removeItem(chr, mapitem, ob);
                        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    byte state = 1;
                    state = Randomizer.isSuccess(50) ? (byte) 2 : (Randomizer.isSuccess(30) ? (byte) 3 : 1);
                    Item2.setState(state);
                    MapleInventoryManipulator.addbyItem(c, Item2);
                    touchitem = true;
                    break;
                }
                case 2432397: {
                    int[] itemlist = new int[]{1212101, 1222095, 1232095, 1242102, 1242133, 1262011, 1272013, 1282013, 1292014, 1302315, 1312185, 1322236, 1332260, 1342100, 1362121, 1372207, 1382245, 1402236, 1412164, 1422171, 1432200, 1442254, 1452238, 1462225, 1472247, 1482202, 1492212, 1522124, 1532130, 1582011, 1592016};
                    int Random4 = (int) Math.floor(Math.random() * (double) itemlist.length);
                    int finalitemid = itemlist[Random4];
                    Equip Item3 = (Equip) MapleItemInformationProvider.getInstance().getEquipById(finalitemid);
                    if (Item3 == null) {
                        InventoryHandler.removeItem(chr, mapitem, ob);
                        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    byte state = 1;
                    state = Randomizer.isSuccess(50) ? (byte) 2 : (Randomizer.isSuccess(30) ? (byte) 3 : 1);
                    Item3.setState(state);
                    MapleInventoryManipulator.addbyItem(c, Item3);
                    touchitem = true;
                    break;
                }
                case 2432398: {
                    int rand;
                    ArrayList<Pair<Integer, Integer>> random = new ArrayList<Pair<Integer, Integer>>();
                    int[][] NormalItem = new int[][]{{5062009, 5}, {5062009, 10}, {5062500, 10}, {2435719, 1}, {2435719, 2}, {2435719, 3}, {4001832, 100}, {4001832, 150}, {4001832, 200}, {4310012, 15}, {4310012, 20}, {4310012, 25}, {4310012, 30}, {4310012, 35}};
                    int[][] HighuerItem = new int[][]{{2048716, 1}, {2048716, 2}, {2048716, 3}, {2048717, 1}, {2048717, 2}, {2049752, 1}, {4310005, 1}, {4310005, 2}, {5069000, 1}};
                    int[][] UniqueItem = new int[][]{{5062503, 10}, {5062503, 15}, {2049153, 1}, {2049153, 2}, {2049153, 3}, {5068300, 1}, {5069001, 1}, {2048753, 1}, {2049370, 1}, {5062503, 5}};
                    random.add(new Pair<Integer, Integer>(1, 6400));
                    random.add(new Pair<Integer, Integer>(2, 3500));
                    random.add(new Pair<Integer, Integer>(3, 100));
                    int itemid = 0;
                    int count = 0;
                    int type = GameConstants.getWeightedRandom(random);
                    if (type == 1) {
                        rand = Randomizer.rand(0, NormalItem.length - 1);
                        itemid = NormalItem[rand][0];
                        count = NormalItem[rand][1];
                    } else if (type == 2) {
                        rand = Randomizer.rand(0, HighuerItem.length - 1);
                        itemid = HighuerItem[rand][0];
                        count = HighuerItem[rand][1];
                    } else if (type == 3) {
                        rand = Randomizer.rand(0, UniqueItem.length - 1);
                        itemid = UniqueItem[rand][0];
                        count = UniqueItem[rand][1];
                    }
                    chr.gainItem(itemid, count);
                    chr.dropMessage(5, "[" + MapleItemInformationProvider.getInstance().getName(itemid) + "]를 " + count + "개 획득 했습니다.");
                    touchitem = true;
                    break;
                }
            }
            if (mapitem.getItemId() >= 2437659 && mapitem.getItemId() <= 2437664) {
                int plushour = 0;
                int plusmit = 0;
                switch (mapitem.getItemId()) {
                    case 2437659: {
                        plusmit = 10;
                        break;
                    }
                    case 2437660: {
                        plusmit = 30;
                        break;
                    }
                    case 2437661: {
                        plusmit = 50;
                        break;
                    }
                    case 2437662: {
                        plushour = 2;
                        break;
                    }
                    case 2437663: {
                        plushour = 4;
                        break;
                    }
                    case 2437664: {
                        plushour = 9;
                    }
                }
                c.getPlayer().getMap().Papullatushour += plushour;
                if (c.getPlayer().getMap().Papullatushour > 12) {
                    c.getPlayer().getMap().Papullatushour -= 12;
                }
                c.getPlayer().getMap().Papullatusminute += plusmit;
                if (c.getPlayer().getMap().Papullatusminute >= 60) {
                    ++c.getPlayer().getMap().Papullatushour;
                    c.getPlayer().getMap().Papullatusminute -= 60;
                }
                c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("파풀라투스의 시계가 움직입니다. 차원의 포탈을 통해 시간을 봉인하세요.", 5120177, true));
                c.getPlayer().getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTimePatten(1, plushour, plusmit, 0, new int[0]));
                InventoryHandler.removeItem(chr, mapitem, ob);
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (mapitem.getItemId() == 4310229) {
              if (c.getKeyValue("유니온코인") == null) {
                   c.setKeyValue("유니온코인", "0");
        }
             
                short qty = mapitem.getItem().getQuantity();
            c.getPlayer().setKeyValue(500629, "point", "" + (Integer.parseInt(c.getKeyValue("유니온코인")) + qty) + "");
            c.setKeyValue("유니온코인", Integer.parseInt(c.getKeyValue("유니온코인")) + qty + "");
                c.getPlayer().setKeyValue(500629, "point", (c.getPlayer().getKeyValue(500629, "point") + qty) + "");
                c.getPlayer().dropMessage(-8, "유니온 코인을 " + qty + "개 획득하여 " + c.getPlayer().getKeyValue(500629, "point") + "개를 소지하고 있습니다.");
                removeItem(chr, mapitem, ob);
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (mapitem.getItemId() == 2437606 || mapitem.getItemId() == 2437607) {
                MobSkill ms1 = MobSkillFactory.getMobSkill(241, 3);
                int duration = 0;
                if (chr.getSkillCustomTime(241) != null) {
                    duration = mapitem.getItemId() == 2437606 ? chr.getSkillCustomTime(241) * 2 : chr.getSkillCustomTime(241) / 2;
                    chr.cancelDisease(SecondaryStat.PapulCuss);
                    if (duration > 60000) {
                        duration = 60000;
                    }
                    if (duration > 0) {
                        ms1.setDuration(duration);
                        chr.setSkillCustomInfo(241, 0L, duration);
                        chr.giveDebuff(SecondaryStat.PapulCuss, ms1);
                    }
                }
                InventoryHandler.removeItem(chr, mapitem, ob);
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (touchitem) {
                InventoryHandler.removeItem(chr, mapitem, ob);
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (mapitem.getQuest() > 0 && chr.getQuestStatus(mapitem.getQuest()) != 1) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (mapitem.getOwner() != chr.getId() && (!mapitem.isPlayerDrop() && mapitem.getDropType() == 0 || mapitem.isPlayerDrop() && chr.getMap().getEverlast())) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (!(mapitem.isPlayerDrop() || mapitem.getDropType() != 1 || mapitem.getOwner() == chr.getId() || chr.getParty() != null && chr.getParty().getMemberById(mapitem.getOwner()) != null)) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (mapitem.getMeso() > 0) {
                if (chr.getParty() != null && mapitem.getOwner() != chr.getId()) {
                    LinkedList<MapleCharacter> toGive = new LinkedList<MapleCharacter>();
                    int splitMeso = mapitem.getMeso() * 40 / 100;
                    for (MaplePartyCharacter z : chr.getParty().getMembers()) {
                        MapleCharacter m = chr.getMap().getCharacterById(z.getId());
                        if (m == null || m.getId() == chr.getId()) {
                            continue;
                        }
                        toGive.add(m);
                    }
                    for (MapleCharacter m : toGive) {
                        int mesos = splitMeso / toGive.size() + (m.getStat().hasPartyBonus ? (int) ((double) mapitem.getMeso() / 20.0) : 0);
                        if (mapitem.getDropper() instanceof MapleMonster && m.getStat().incMesoProp > 0) {
                            mesos = (int) ((double) mesos + Math.floor((float) (m.getStat().incMesoProp * mesos) / 100.0f));
                        }
                        m.gainMeso(mesos, true, false, false, true);
                    }
                    int mesos = mapitem.getMeso() - splitMeso;
                    if (mapitem.getDropper() instanceof MapleMonster && chr.getStat().incMesoProp > 0) {
                        mesos = (int) ((double) mesos + Math.floor((float) (chr.getStat().incMesoProp * mesos) / 100.0f));
                    }
                    chr.gainMeso(mesos, true, false, false, true);
                } else {
                    int mesos = mapitem.getMeso();
                    if (mapitem.getDropper() instanceof MapleMonster && chr.getStat().incMesoProp > 0) {
                        mesos = (int) ((double) mesos + Math.floor((float) (chr.getStat().incMesoProp * mesos) / 100.0f));
                    }
                    chr.gainMeso(mesos, true, false, false, true);
                }
                if (mapitem.getDropper().getType() == MapleMapObjectType.MONSTER) {
                    c.getSession().writeAndFlush((Object) CWvsContext.onMesoPickupResult(mapitem.getMeso()));
                }
                InventoryHandler.removeItem(chr, mapitem, ob);
            } else if (MapleItemInformationProvider.getInstance().isPickupBlocked(mapitem.getItemId())) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                c.getPlayer().dropMessage(5, "This item cannot be picked up.");
            } else if (c.getPlayer().inPVP() && Integer.parseInt(c.getPlayer().getEventInstance().getProperty("ice")) == c.getPlayer().getId()) {
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryFull());
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getShowInventoryFull());
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            } else if (mapitem.getItemId() == 2431835) {
                MapleItemInformationProvider.getInstance().getItemEffect(2002093).applyTo(chr, true);
                InventoryHandler.removeItem(chr, mapitem, ob);
            } else if (mapitem.getItemId() / 10000 != 291 && MapleInventoryManipulator.checkSpace(c, mapitem.getItemId(), mapitem.getItem().getQuantity(), mapitem.getItem().getOwner())) {
                Equip equip;
                if (mapitem.getItem().getQuantity() >= 50 && mapitem.getItemId() == 2340000) {
                    c.setMonitored(true);
                }
                if (mapitem.getEquip() != null && mapitem.getDropper().getType() == MapleMapObjectType.MONSTER && mapitem.getEquip().getState() > 0) {
                    c.getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 0, 65, 0, 0, (byte) 0, true, null, null, mapitem.getItem()));
                }
                if (GameConstants.isArcaneSymbol(mapitem.getItemId())) {
                    Equip equip2 = (Equip) mapitem.getItem();
                    equip2.setArc((short) 30);
                    equip2.setArcLevel(1);
                    equip2.setArcEXP(1);
                    if (GameConstants.isXenon(c.getPlayer().getJob())) {
                        equip2.setStr((short) 117);
                        equip2.setDex((short) 117);
                        equip2.setLuk((short) 117);
                    } else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
                        equip2.setHp((short) 525);
                    } else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
                        equip2.setStr((short) 300);
                    } else if (GameConstants.isMagician(c.getPlayer().getJob())) {
                        equip2.setInt((short) 300);
                    } else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob()) || GameConstants.isMechanic(c.getPlayer().getJob()) || GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                        equip2.setDex((short) 300);
                    } else if (GameConstants.isThief(c.getPlayer().getJob())) {
                        equip2.setLuk((short) 300);
                    } else if (GameConstants.isPirate(c.getPlayer().getJob())) {
                        equip2.setStr((short) 300);
                    }
                } else if (GameConstants.isAuthenticSymbol(mapitem.getItemId()) && (equip = (Equip) mapitem.getItem()).getArcLevel() == 0) {
                    equip.setArc((short) 10);
                    equip.setArcLevel(1);
                    equip.setArcEXP(1);
                    if (GameConstants.isXenon(c.getPlayer().getJob())) {
                        equip.setStr((short) 317);
                        equip.setDex((short) 317);
                        equip.setLuk((short) 317);
                    } else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
                        equip.setHp((short) 725);
                    } else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
                        equip.setStr((short) 500);
                    } else if (GameConstants.isMagician(c.getPlayer().getJob())) {
                        equip.setInt((short) 500);
                    } else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob()) || GameConstants.isMechanic(c.getPlayer().getJob()) || GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                        equip.setDex((short) 500);
                    } else if (GameConstants.isThief(c.getPlayer().getJob())) {
                        equip.setLuk((short) 500);
                    } else if (GameConstants.isPirate(c.getPlayer().getJob())) {
                        equip.setStr((short) 500);
                    }
                }
                if (mapitem.getItem().getItemId() == 4001886) {
                    if (mapitem.getItem().getBossid() != 0) {
                        int party = chr.getParty() != null ? chr.getParty().getMembers().size() : 1;
                        int meso = 0, mobid = 0;
                        for (Triple<Integer, Integer, Integer> list : BossRewardMeso.getLists()) {
                            int bossid = BossRewardMeso.RewardBossId(list.getMid());
                            if (bossid == 0) {
                                bossid = list.getLeft();
                            }
                            if (bossid == mapitem.getItem().getBossid() || list.getMid() == mapitem.getItem().getBossid()) {
                                meso = list.getRight();
                                mobid = list.getMid();
                                break;
                            }
                        }
                        mapitem.getItem().setReward(new BossReward(chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, mobid, party, meso));
                        mapitem.getItem().setExpiration(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
                    } else {
                        mapitem.getItem().setReward(new BossReward(chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, 100100, 1, 1));
                    }
                }
                FileoutputUtil.log(FileoutputUtil.아이템획득로그, "[PickUP_Player] 계정번호 : " + chr.getClient().getAccID() + " | " + chr.getName() + "이 " + c.getPlayer().getMapId() + "에서 " + MapleItemInformationProvider.getInstance().getName(mapitem.getItem().getItemId()) + "(" + mapitem.getItem().getItemId() + ")를 " + mapitem.getItem().getQuantity() + " 개 획득");
                MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true, mapitem.getDropper() instanceof MapleMonster, false);
                InventoryHandler.removeItem(chr, mapitem, ob);
            } else {
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryFull());
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getShowInventoryFull());
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            }
        } finally {
            mapitem.getLock().unlock();
        }
    }

    public static final void Pickup_Pet(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        if (c.getPlayer().inPVP()) {
            return;
        }
        c.getPlayer().setScrolledPosition((short) 0);
        final byte petz = (byte) slea.readInt();
        final MaplePet pet = chr.getPet(petz);
        slea.skip(1);
        slea.readInt();
        final Point Client_Reportedpos = slea.readPos();
        final MapleMapObject ob = chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.ITEM);
        if (ob == null || pet == null) {
            return;
        }
        final MapleMapItem mapitem = (MapleMapItem) ob;
        try {
            mapitem.getLock().lock();
            if (mapitem.isPickedUp()) {
                return;
            }
            if ((mapitem.getOwner() != chr.getId() && mapitem.isPlayerDrop()) || mapitem.getItemId() == 2023484 || mapitem.getItemId() == 2023494 || mapitem.getItemId() == 2023495 || mapitem.getItemId() == 2023669 || mapitem.getItemId() == 2023927) {
                return;
            }
            if (mapitem.getItemId() == 4310229) {
              if (c.getKeyValue("유니온코인") == null) {
                   c.setKeyValue("유니온코인", "0");
        }
             
                short qty = mapitem.getItem().getQuantity();
            c.getPlayer().setKeyValue(500629, "point", "" + (Integer.parseInt(c.getKeyValue("유니온코인")) + qty) + "");
            c.setKeyValue("유니온코인", Integer.parseInt(c.getKeyValue("유니온코인")) + qty + "");
                c.getPlayer().setKeyValue(500629, "point", (c.getPlayer().getKeyValue(500629, "point") + qty) + "");
                c.getPlayer().dropMessage(-8, "유니온 코인을 " + qty + "개 획득하여 " + c.getPlayer().getKeyValue(500629, "point") + "개를 소지하고 있습니다.");
                removeItem(chr, mapitem, ob);
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (mapitem.getItemId() == 2431174) {
                final int rand = Randomizer.rand(1, 10) * 10;
                c.getPlayer().gainHonor(rand);
                removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                return;
            }
            
             if (mapitem.getItemId() == 2433103) {
                final int rand = Randomizer.rand(1, 10) * 10;
                c.getPlayer().gainHonor(10000);
                removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                return;
            }
            
/* 5181 */       if (mapitem.getItemId() == 4001169 && c.getPlayer().getMap().getMonstermarble() != 20) {
/* 5182 */         c.getPlayer().getMap().setMonstermarble(c.getPlayer().getMap().getMonstermarble() + 1);
/* 5183 */         c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("몬스터구슬 " + c.getPlayer().getMap().getMonstermarble() + " / 20"));
/* 5184 */         removeItem_Pet(chr, mapitem, petz, pet.getPetItemId()); return;
/*      */       } 
/* 5186 */       if (mapitem.getItemId() == 4001169) {
/* 5187 */         removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
/*      */         
/*      */         return;
/*      */       } 
            //파티퀘스트 시작
            /* 5191 */       if (mapitem.getItemId() == 4001101 && c.getPlayer().getMap().getMoonCake() != 80) {
/* 5192 */         c.getPlayer().getMap().setMoonCake(c.getPlayer().getMap().getMoonCake() + 1);
/* 5193 */         c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
/* 5194 */         c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("어흥이를 위해 월묘의 떡 " + c.getPlayer().getMap().getMoonCake() + "개를 모았습니다.  앞으로 " + (80 - c.getPlayer().getMap().getMoonCake()) + "개 더!"));
/* 5195 */         removeItem(chr, mapitem, ob); return;
/*      */       } 
/* 5197 */       if (mapitem.getItemId() == 4001101) {
/* 5198 */         removeItem(chr, mapitem, ob);
/*      */         return;
/*      */       } 
/* 5201 */       if (mapitem.getItemId() == 4000884) {
/* 5202 */         c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("달맞이꽃 씨앗을 되찾았습니다.", 5120016, true));
/*      */       }
            
/* 4892 */       if (mapitem.getItemId() == 4001022) {
/* 4893 */         c.getPlayer().getMap().setRPTicket(c.getPlayer().getMap().getRPTicket() + 1);
/* 4894 */         c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
/* 4895 */         c.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("통행증 " + c.getPlayer().getMap().getRPTicket() + "장을 모았습니다."));
/* 4896 */         if (c.getPlayer().getMap().getRPTicket() == 20) {
/* 4897 */           c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(10));
/* 4898 */           c.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 4899 */           c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("통행증을 모두 모았습니다. 레드 벌룬에게 말을 걸어 다음 단계로 이동해 주세요.", 5120018, true));
/*      */         } 
/* 4901 */         removeItem(chr, mapitem, ob); return;
/*      */       } 
/* 4903 */       if (mapitem.getItemId() == 4001022) {
/* 4904 */         removeItem(chr, mapitem, ob);
/*      */         return;
/*      */       } 

            if (mapitem.getItemId() == 2633304 || mapitem.getItemId() == 2633609) {
                final String[] bossname = {"이지 시그너스", "하드 힐라", "카오스 핑크빈", "노멀 시그너스", "카오스 자쿰", "카오스 피에르", "카오스 반반", "카오스 블러디퀸", "하드 매그너스", "카오스 벨룸", "카오스 파풀라투스", "노멀 스우", "노멀 데미안", "이지 루시드", "노멀 루시드", "노멀 윌", "노멀 더스크", "노멀 듄켈", "하드 데미안", "하드 스우", "하드 루시드", "하드 윌", "카오스 더스크", "하드 듄켈", "진 힐라", "세렌"};
                String[] wishCoinCheck = null;
                if (ServerConstants.Event_Blooming) {
                    wishCoinCheck = chr.getClient().getKeyValue("WishCoin").split("");
                } else if (ServerConstants.Event_MapleLive) {
                    wishCoinCheck = chr.getClient().getCustomKeyValueStr(501468, "reward").split("");
                }
                String NewKeyvalue = "";
                int i = 0;
                for (final Pair<Integer, Integer> list : ServerConstants.NeoPosList) {
                    final MapleMonster mob = (MapleMonster) mapitem.getDropper();
                    if (list.getLeft() == mob.getId()) {
                        int coincount = list.getRight();
                        if (ServerConstants.Event_Blooming) {
                            if (Randomizer.isSuccess(50)) {
                                chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "자아, #r" + bossname[i] + "#k 처치~\r\n위시 코인 #b" + coincount + "#k개 찾았지"));
                            } else {
                                chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "우와! #r" + bossname[i] + "#k 처치~\r\n위시 코인 #b" + coincount + "#k개 찾았다~"));
                            }
                        } else if (ServerConstants.Event_MapleLive) {
                            final int questid = 501471 + i;
                            if (chr.getClient().getCustomKeyValue(questid, "state") == 1L || chr.getClient().getCustomKeyValue(questid, "state") == 2L) {
                                chr.getClient().send(CField.enforceMsgNPC(9062558, 3000, "이런, 이미 이번주에 #r" + bossname[i] + "#k를 처치했군."));
                                break;
                            }
                            if (Randomizer.isSuccess(50)) {
                                chr.getClient().send(CField.enforceMsgNPC(9062558, 3000, "호오, #r" + bossname[i] + "#k를 처치했군.\r\n검은콩 #b" + coincount + "#k개를 찾았다."));
                            } else {
                                chr.getClient().send(CField.enforceMsgNPC(9062558, 3000, "좋아, #r" + bossname[i] + "#k를 처치했군.\r\n검은콩 #b" + coincount + "#k개를 찾았어."));
                            }
                        }
                        wishCoinCheck[i] = "1";
                        if (ServerConstants.Event_Blooming) {
                            chr.getClient().removeKeyValue("WishCoin");
                        }
                        for (int to = 0; to < wishCoinCheck.length; ++to) {
                            NewKeyvalue += wishCoinCheck[to];
                        }
                        if (ServerConstants.Event_Blooming) {
                            chr.getClient().setKeyValue("WishCoin", NewKeyvalue);
                            if (Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) >= 400) {
                                chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "이번 주차 위시 코인 #r400개#k\r\n 모두 획득~ 냐냐냐~"));
                                return;
                            }
                            if (Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) + coincount >= 400) {
                                coincount = Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) + coincount - 400;
                            }
                            chr.getClient().setKeyValue("WishCoinGain", Integer.parseInt(chr.getClient().getKeyValue("WishCoinGain")) + coincount + "");
                            chr.getClient().setKeyValue("WishCoinWeekGain", Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) + coincount + "");
                            break;
                        } else {
                            if (ServerConstants.Event_MapleLive) {
                                final int questid = 501471 + i;
                                chr.getClient().setCustomKeyValue(501468, "reward", NewKeyvalue);
                                chr.getClient().setCustomKeyValue(questid, "state", "1");
                                break;
                            }
                            break;
                        }
                    } else {
                        ++i;
                    }
                }
                removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                return;
            }
            if (mapitem.getItemId() == 2633343) {
                if (chr.getBuffedValue(80003046)) {
                    int added = 0;
                    for (int adddd = (chr.getQuestStatus(100801) == 2) ? 1 : ((chr.getQuestStatus(100802) == 2) ? 2 : 0), j = 0; j < adddd; ++j) {
                        if (Randomizer.isSuccess(50)) {
                            ++added;
                        }
                    }
                    int now = (int) chr.getKeyValue(100803, "count");
                    if (now < 0) {
                        now = 0;
                    }
                    chr.setKeyValue(100803, "count", now + 1 + "");
                    chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 2 : 1, mapitem.getPosition());
                    chr.getClient().send(SLFCGPacket.EventSkillOnFlowerEffect(7, 1));
                    if (chr.getKeyValue(100803, "count") == 5L) {
                        chr.getClient().send(CField.enforceMsgNPC(9062527, 1300, "꽃의 보석에 햇살의 힘이 #r절반#k이나 모였어요!"));
                    } else if (chr.getKeyValue(100803, "count") == 10L) {
                        chr.setKeyValue(100803, "count", "0");
                        chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20, chr.getPosition());
                        chr.getClient().send(SLFCGPacket.EventSkillOn(1, 1 + added));
                        chr.getClient().send(CField.enforceMsgNPC(9062527, 1000, "햇살의 힘으로 #r꽃씨#k를 펑!펑!"));
                        chr.getClient().send(CField.UIPacket.detailShowInfo("하나의 꽃씨 뿌리기로 블루밍 코인 " + ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20) + "개를 획득했습니다.", 3, 20, 20));
                        if (added >= 1 && chr.getQuestStatus(100801) == 2) {
                            Timer.BuffTimer.getInstance().schedule(() -> {
                                chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 20 : 10, chr.getPosition());
                                chr.getClient().send(CField.enforceMsgNPC(9062528, 1000, "#r꽃비#k가 쏴아아!\r\n너무 예뻐!"));
                                chr.getClient().send(CField.UIPacket.detailShowInfo("두나의 꽃비로 블루밍 코인 " + ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 20 : 10) + "개를 획득했습니다.", 3, 20, 20));
                                return;
                            }, 2500L);
                        }
                        if (added == 2 && chr.getQuestStatus(100802) == 2) {
                            Timer.BuffTimer.getInstance().schedule(() -> {
                                chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20, chr.getPosition());
                                chr.getClient().send(CField.enforceMsgNPC(9062529, 1000, "#r햇살#k이 샤라랑!\r\n꽃들아 잘 자라라~!"));
                                chr.getClient().send(CField.UIPacket.detailShowInfo("세나의 햇살 비추기로 블루밍 코인 " + ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20) + "개를 획득했습니다.", 3, 20, 20));
                                return;
                            }, 4500L);
                        }
                    }
                    if (chr.getKeyValue(100794, "today") >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000 : 3000)) {
                        chr.getClient().send(CField.enforceMsgNPC(9062527, 1000, "오늘은 이만하면 됐어요."));
                        chr.getClient().getSession().writeAndFlush((Object) CField.UIPacket.closeUI(1297));
                        chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062524, 0));
                        chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062525, 0));
                        chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062526, 0));
                        chr.cancelEffect(chr.getBuffedEffect(SecondaryStat.EventSpecialSkill));
                    }
                }
                removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                return;
            }
            if (mapitem.getItemId() == 2002058) {
                if (c.getPlayer().hasDisease(SecondaryStat.DeathMark)) {
                    c.getPlayer().cancelDisease(SecondaryStat.DeathMark);
                }
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
                removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                return;
            }
            if (mapitem.getItemId() == 2434851) {
                if (!chr.getBuffedValue(25121133)) {
                    c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                final SecondaryStatEffect effect = SkillFactory.getSkill(25121133).getEffect(1);
                long duration = chr.getBuffLimit(25121133);
                duration += 4000L;
                if (duration >= effect.getCoolRealTime()) {
                    duration = effect.getCoolRealTime();
                }
                effect.applyTo(chr, chr, false, chr.getPosition(), (int) duration, (byte) 0, false);
                c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
                removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
            } else {
                if (mapitem.getOwner() != chr.getId() && ((!mapitem.isPlayerDrop() && mapitem.getDropType() == 0) || (mapitem.isPlayerDrop() && chr.getMap().getEverlast()))) {
                    return;
                }
                if (!mapitem.isPlayerDrop() && mapitem.getDropType() == 1 && mapitem.getOwner() != chr.getId() && (chr.getParty() == null || chr.getParty().getMemberById(mapitem.getOwner()) == null)) {
                    return;
                }
                if (GameConstants.isArcaneSymbol(mapitem.getItemId())) {
                    final Equip equip = (Equip) mapitem.getItem();
                    equip.setArc((short) 30);
                    equip.setArcLevel(1);
                    equip.setArcEXP(1);
                    if (GameConstants.isXenon(c.getPlayer().getJob())) {
                        equip.setStr((short) 117);
                        equip.setDex((short) 117);
                        equip.setLuk((short) 117);
                    } else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
                        equip.setHp((short) 525);
                    } else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
                        equip.setStr((short) 300);
                    } else if (GameConstants.isMagician(c.getPlayer().getJob())) {
                        equip.setInt((short) 300);
                    } else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob()) || GameConstants.isMechanic(c.getPlayer().getJob()) || GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                        equip.setDex((short) 300);
                    } else if (GameConstants.isThief(c.getPlayer().getJob())) {
                        equip.setLuk((short) 300);
                    } else if (GameConstants.isPirate(c.getPlayer().getJob())) {
                        equip.setStr((short) 300);
                    }
                } else if (GameConstants.isAuthenticSymbol(mapitem.getItemId())) {
                    final Equip equip = (Equip) mapitem.getItem();
                    if (equip.getArcLevel() == 0) {
                        equip.setArc((short) 10);
                        equip.setArcLevel(1);
                        equip.setArcEXP(1);
                        if (GameConstants.isXenon(c.getPlayer().getJob())) {
                            equip.setStr((short) 317);
                            equip.setDex((short) 317);
                            equip.setLuk((short) 317);
                        } else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
                            equip.setHp((short) 725);
                        } else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
                            equip.setStr((short) 500);
                        } else if (GameConstants.isMagician(c.getPlayer().getJob())) {
                            equip.setInt((short) 500);
                        } else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob()) || GameConstants.isMechanic(c.getPlayer().getJob()) || GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                            equip.setDex((short) 500);
                        } else if (GameConstants.isThief(c.getPlayer().getJob())) {
                            equip.setLuk((short) 500);
                        } else if (GameConstants.isPirate(c.getPlayer().getJob())) {
                            equip.setStr((short) 500);
                        }
                    }
                }
                if (mapitem.isPickpoket()) {
                    return;
                }
                if (mapitem.getMeso() > 0) {
                    chr.gainMeso(mapitem.getMeso(), true, false, true, true);
                    removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                } else {
                    if (MapleItemInformationProvider.getInstance().isPickupBlocked(mapitem.getItemId()) || mapitem.getItemId() / 10000 == 291) {
                        return;
                    }
                    if (useItem(c, mapitem.getItemId())) {
                        removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                    } else if (mapitem.getItemId() == 2431835) {
                        MapleItemInformationProvider.getInstance().getItemEffect(2002093).applyTo(chr, true);
                        removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                    } else if (MapleInventoryManipulator.checkSpace(c, mapitem.getItemId(), mapitem.getItem().getQuantity(), mapitem.getItem().getOwner())) {
                    if (mapitem.getItem().getItemId() == 4001886) {
                        if (mapitem.getItem().getBossid() != 0) {
                            int party = chr.getParty() != null ? chr.getParty().getMembers().size() : 1;
                            int meso = 0, mobid = 0;
                            for (Triple<Integer, Integer, Integer> list : BossRewardMeso.getLists()) {
                                int bossid = BossRewardMeso.RewardBossId(list.getMid());
                                if (bossid == 0) {
                                    bossid = list.getLeft();
                                }

                                if (bossid == mapitem.getItem().getBossid() || list.getMid() == mapitem.getItem().getBossid()) {
                                    meso = list.getRight();
                                    mobid = list.getMid();
                                    break;
                                }
                            }
                            mapitem.getItem().setExpiration(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
                            mapitem.getItem().setReward(new BossReward(chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, mobid, party, meso));
                        } else {
                            mapitem.getItem().setReward(new BossReward(chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, 100100, 1, 1));
                        }
                    }
                        MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), false, mapitem.getDropper() instanceof MapleMonster, true);
                        removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
                        if (mapitem.getEquip() != null && mapitem.getDropper().getType() == MapleMapObjectType.MONSTER && mapitem.getEquip().getState() > 0) {
                            c.getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 0, 65, 0, 0, (byte) 0, true, null, null, mapitem.getItem()));
                        }
                    }
                }
            }
        } finally {
            mapitem.getLock().unlock();
        }
    }

    public static final boolean useItem(final MapleClient c, final int id) {
        if (GameConstants.isUse(id)) {
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            final SecondaryStatEffect eff = ii.getItemEffect(id);
            if (eff == null) {
                return false;
            }
            if (id / 10000 == 291) {
                boolean area = false;
                for (final Rectangle rect : c.getPlayer().getMap().getAreas()) {
                    if (rect.contains(c.getPlayer().getTruePosition())) {
                        area = true;
                        break;
                    }
                }
                if (!c.getPlayer().inPVP() || (c.getPlayer().getTeam() == id - 2910000 && area)) {
                    return false;
                }
            }
            final int consumeval = eff.getConsume();
            if (consumeval > 0) {
                if (c.getPlayer().getMapId() == 109090300) {
                    for (final MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
                        if (chr != null && ((id == 2022163 && c.getPlayer().isCatched == chr.isCatched) || ((id == 2022165 || id == 2022166) && c.getPlayer().isCatched != chr.isCatched))) {
                            if (id == 2022163) {
                                ii.getItemEffect(id).applyTo(chr);
                            } else if (id == 2022166) {
                                chr.giveDebuff(SecondaryStat.Stun, MobSkillFactory.getMobSkill(123, 1));
                            } else {
                                if (id != 2022165) {
                                    continue;
                                }
                                chr.giveDebuff(SecondaryStat.Slow, MobSkillFactory.getMobSkill(126, 1));
                            }
                        }
                    }
                    c.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, (short) (-1), true));
                    return true;
                }
                consumeItem(c, eff);
                consumeItem(c, ii.getItemEffectEX(id));
                c.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, (short) (-1), true));
                return true;
            }
        }
        return false;
    }

    public static final void consumeItem(final MapleClient c, final SecondaryStatEffect eff) {
        if (eff == null) {
            return;
        }
        if (eff.getConsume() == 2) {
            if (c.getPlayer().getParty() != null && c.getPlayer().isAlive()) {
                for (final MaplePartyCharacter pc : c.getPlayer().getParty().getMembers()) {
                    final MapleCharacter chr = c.getPlayer().getMap().getCharacterById(pc.getId());
                    if (chr != null && chr.isAlive()) {
                        eff.applyTo(chr, true);
                    }
                }
            } else {
                eff.applyTo(c.getPlayer(), true);
            }
        } else if (c.getPlayer().isAlive()) {
            eff.applyTo(c.getPlayer(), true);
        }
    }

    public static final void removeItem_Pet(final MapleCharacter chr, final MapleMapItem mapitem, final int index, final int id) {
        mapitem.setPickedUp(true);
        chr.getMap().broadcastMessage(CField.removeItemFromMap(mapitem.getObjectId(), 5, chr.getId(), index));
        chr.getMap().removeMapObject(mapitem);
        if (mapitem.isRandDrop()) {
            chr.getMap().spawnRandDrop();
        }
    }

    public static final void removeItem(final MapleCharacter chr, final MapleMapItem mapitem, final MapleMapObject ob) {
        mapitem.setPickedUp(true);
        chr.getMap().broadcastMessage(CField.removeItemFromMap(mapitem.getObjectId(), 2, chr.getId()), mapitem.getPosition());
        chr.getMap().removeMapObject(mapitem);
        if (mapitem.isRandDrop()) {
            chr.getMap().spawnRandDrop();
        }
    }

    public static final void addMedalString(final MapleCharacter c, final StringBuilder sb) {
        final Item medal = c.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-21));
        if (medal != null) {
            sb.append("<");
            if (medal.getItemId() == 1142257 && GameConstants.isAdventurer(c.getJob())) {
                final MapleQuestStatus stat = c.getQuestNoAdd(MapleQuest.getInstance(111111));
                if (stat != null && stat.getCustomData() != null) {
                    sb.append(stat.getCustomData());
                    sb.append("'s Successor");
                } else {
                    sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
                }
            } else {
                sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
            }
            sb.append("> ");
        }
    }

    public static final void TeleRock(final LittleEndianAccessor slea, final MapleClient c) {
        final short slot = slea.readShort();
        final int itemId = slea.readInt();
        final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId || itemId / 10000 != 232) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        final boolean used = UseTeleRock(slea, c, itemId);
        if (used) {
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
        }
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static final boolean UseTeleRock(final LittleEndianAccessor slea, final MapleClient c, final int itemId) {
        boolean used = false;
        if (slea.readByte() == 0) {
            final MapleMap target = c.getChannelServer().getMapFactory().getMap(slea.readInt());
            if (target != null && ((itemId == 5041000 && c.getPlayer().isRockMap(target.getId())) || ((itemId == 5040000 || itemId == 5040001) && c.getPlayer().isRegRockMap(target.getId())) || ((itemId == 5040004 || itemId == 5041001) && (c.getPlayer().isHyperRockMap(target.getId()) || GameConstants.isHyperTeleMap(target.getId()))))) {
                if (!FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()) && !FieldLimitType.VipRock.check(target.getFieldLimit())) {
                    c.getPlayer().changeMap(target, target.getPortal(0));
                    used = true;
                } else {
                    c.getPlayer().dropMessage(1, "You cannot go to that place.");
                }
            } else {
                c.getPlayer().dropMessage(1, "You cannot go to that place.");
            }
        } else {
            final String name = slea.readMapleAsciiString();
            final MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
            if (victim != null && !victim.isIntern() && c.getPlayer().getEventInstance() == null && victim.getEventInstance() == null) {
                if (!FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()) && !FieldLimitType.VipRock.check(c.getChannelServer().getMapFactory().getMap(victim.getMapId()).getFieldLimit())) {
                    if (itemId == 5041000 || itemId == 5040004 || itemId == 5041001 || victim.getMapId() / 100000000 == c.getPlayer().getMapId() / 100000000) {
                        c.getPlayer().changeMap(victim.getMap(), victim.getMap().findClosestPortal(victim.getTruePosition()));
                        used = true;
                    } else {
                        c.getPlayer().dropMessage(1, "You cannot go to that place.");
                    }
                } else {
                    c.getPlayer().dropMessage(1, "You cannot go to that place.");
                }
            } else {
                c.getPlayer().dropMessage(1, "(" + name + ") is currently difficult to locate, so the teleport will not take place.");
            }
        }
        return used;
    }

    public static void UsePetLoot(final LittleEndianAccessor slea, final MapleClient c) {
        slea.readInt();
        final short mode = slea.readShort();
        c.getPlayer().setPetLoot(mode == 1);
        for (int i = 0; i < c.getPlayer().getPets().length; ++i) {
            if (c.getPlayer().getPet(i) != null) {
                c.getSession().writeAndFlush((Object) PetPacket.updatePet(c.getPlayer(), c.getPlayer().getPet(i), c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(c.getPlayer().getPet(i).getInventoryPosition()), false, c.getPlayer().getPetLoot()));
            }
        }
        c.getSession().writeAndFlush((Object) PetPacket.updatePetLootStatus(mode));
    }

    public static void SelectPQReward(final LittleEndianAccessor slea, final MapleClient c) {
        slea.skip(1);
        final int randval = RandomRewards.getRandomReward();
        final short quantity = (short) Randomizer.rand(1, 10);
        MapleInventoryManipulator.addById(c, randval, quantity, "Reward item: " + randval + " on " + FileoutputUtil.CurrentReadable_Date());
        if (c.getPlayer().getMapId() == 100000203) {
            final MapleMap map = c.getChannelServer().getMapFactory().getMap(960000000);
            c.getPlayer().changeMap(map, map.getPortal(0));
        } else {
            c.getPlayer().fakeRelog();
        }
        c.getSession().writeAndFlush((Object) CField.EffectPacket.showCharmEffect(c.getPlayer(), randval, 1, true, ""));
    }

    public static void resetZeroWeapon(final MapleCharacter chr) {
        final Equip newa = (Equip) MapleItemInformationProvider.getInstance().getEquipById(chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11)).getItemId());
        final Equip newb = (Equip) MapleItemInformationProvider.getInstance().getEquipById(chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10)).getItemId());
        ((Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11))).set(newa);
        ((Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10))).set(newb);
        chr.getClient().getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11))));
        chr.getClient().getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10))));
        chr.dropMessage(5, "\uc81c\ub85c\uc758 \uc7a5\ube44\ub294 \ud30c\uad34\ub418\ub294\ub300\uc2e0 \ucc98\uc74c \uc0c1\ud0dc\ub85c \ub418\ub3cc\uc544\uac11\ub2c8\ub2e4.");
    }

    public static void UseNameChangeCoupon(final LittleEndianAccessor slea, final MapleClient c) {
        final short slot = slea.readShort();
        final int itemId = slea.readInt();
        final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
            c.getSession().writeAndFlush((Object) CWvsContext.nameChangeUI(false));
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        c.setNameChangeEnable((byte) 1);
        MapleCharacter.updateNameChangeCoupon(c);
        c.getSession().writeAndFlush((Object) CWvsContext.nameChangeUI(true));
        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
    }

    public static void UseKaiserColorChange(final LittleEndianAccessor slea, final MapleClient c) {
        final short slot = slea.readShort();
        slea.skip(2);
        final int itemId = slea.readInt();
        final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
            return;
        }
        final int[] colors = {841, 842, 843, 758, 291, 317, 338, 339, 444, 445, 446, 458, 461, 447, 450, 454, 455, 456, 457, 459, 460, 462, 463, 464, 289, 4, 34, 35, 64, 9, 10, 12, 11, 16, 17, 22, 24, 53, 61, 62, 63, 67, 68, 109, 110, 111, 112, 113, 114, 115, 116, 117, 121, 125, 128, 129, 145, 150};
        if (itemId == 2350004) {
            c.getPlayer().setKeyValue(12860, "extern", colors[Randomizer.nextInt(colors.length)] + "");
        } else if (itemId == 2350005) {
            c.getPlayer().setKeyValue(12860, "inner", colors[Randomizer.nextInt(colors.length)] + "");
        } else if (itemId == 2350006) {
            c.getPlayer().setKeyValue(12860, "extern", "842");
        } else if (itemId == 2350007) {
            c.getPlayer().setKeyValue(12860, "premium", "0");
            c.getPlayer().setKeyValue(12860, "inner", "0");
            c.getPlayer().setKeyValue(12860, "extern", "0");
        }
        if (c.getPlayer().getKeyValue(12860, "extern") == -1L) {
            c.getPlayer().setKeyValue(12860, "extern", "0");
        }
        if (c.getPlayer().getKeyValue(12860, "inner") == -1L) {
            c.getPlayer().setKeyValue(12860, "inner", "0");
        }
        if (c.getPlayer().getKeyValue(12860, "premium") == -1L) {
            c.getPlayer().setKeyValue(12860, "premium", "0");
        }
        c.getPlayer().getMap().broadcastMessage(CField.KaiserChangeColor(c.getPlayer().getId(), (c.getPlayer().getKeyValue(12860, "extern") == -1L) ? 0 : ((int) c.getPlayer().getKeyValue(12860, "extern")), (c.getPlayer().getKeyValue(12860, "inner") == -1L) ? 0 : ((int) c.getPlayer().getKeyValue(12860, "inner")), (byte) ((c.getPlayer().getKeyValue(12860, "premium") == -1L) ? 0 : ((byte) c.getPlayer().getKeyValue(12860, "premium")))));
        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static final void UseSoulEnchanter(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        slea.skip(4);
        final short useslot = slea.readShort();
        final short slot = slea.readShort();
        final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
        final Item enchanter = useInventory.getItem(useslot);
        Item equip;
        if (slot == -11) {
            equip = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
        } else {
            equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        }
        final Equip nEquip = (Equip) equip;
        nEquip.setSoulEnchanter((short) 9);
        c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, nEquip));
        chr.getMap().broadcastMessage(chr, CField.showEnchanterEffect(chr.getId(), (byte) 1), true);
        MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.USE, enchanter.getItemId(), 1, true, false);
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static final void UseSoulScroll(final LittleEndianAccessor rh, final MapleClient c, final MapleCharacter chr) {
        rh.skip(4);
        final short useslot = rh.readShort();
        final short slot = rh.readShort();
        final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
        final Item soul = useInventory.getItem(useslot);
        final int soula = soul.getItemId() - 2590999;
        final int soulid = soul.getItemId();
        boolean great = false;
        final MapleDataProvider sourceData = MapleDataProviderFactory.getDataProvider(new File("wz/Item.wz"));
        final MapleData dd = sourceData.getData("SkillOption.img");
        final int skillid = MapleDataTool.getIntConvert(dd.getChildByPath("skill/" + soula + "/skillId"));
        Item equip;
        if (slot == -11) {
            equip = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
        } else {
            equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        }
        if (slot == -11) {
            chr.setSoulMP((Equip) equip);
        }
        if (dd.getChildByPath("skill/" + soula + "/tempOption/1/id") != null) {
            great = true;
        }
        short statid = 0;
        if (great) {
            statid = (short) MapleDataTool.getIntConvert(dd.getChildByPath("skill/" + soula + "/tempOption/" + Randomizer.nextInt(7) + "/id"));
        } else {
            statid = (short) MapleDataTool.getIntConvert(dd.getChildByPath("skill/" + soula + "/tempOption/0/id"));
        }
        final Equip nEquip = (Equip) equip;
        if (SkillFactory.getSkill(nEquip.getSoulSkill()) != null) {
            chr.changeSkillLevel(nEquip.getSoulSkill(), (byte) (-1), (byte) 0);
        }
        nEquip.setSoulName(GameConstants.getSoulName(soulid));
        nEquip.setSoulPotential(statid);
        nEquip.setSoulSkill(skillid);
        Equip zeros = null;
        if (GameConstants.isZero(c.getPlayer().getJob())) {
            if (slot == -11) {
                zeros = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
            } else if (slot == -10) {
                zeros = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
            }
        }
        if (zeros != null) {
            if (SkillFactory.getSkill(zeros.getSoulSkill()) != null) {
                chr.changeSkillLevel(zeros.getSoulSkill(), (byte) (-1), (byte) 0);
            }
            zeros.setSoulName(nEquip.getSoulName());
            zeros.setSoulPotential(nEquip.getSoulPotential());
            zeros.setSoulSkill(nEquip.getSoulSkill());
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, zeros));
        }
        chr.changeSkillLevel(skillid, (byte) 1, (byte) 1);
        c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, nEquip));
        chr.getMap().broadcastMessage(chr, CField.showSoulScrollEffect(chr.getId(), (byte) 1, false, nEquip), true);
        MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.USE, soulid, 1, true, false);
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
    }

    public static void UseCube(final LittleEndianAccessor slea, final MapleClient c) {
        int pos = 0;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final Item cube = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slea.readShort());
        if (cube.getItemId() >= 2730000 && cube.getItemId() <= 2730005) {
            final int itemid = slea.readInt();
            if (itemid != cube.getItemId()) {
                c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                return;
            }
        }
        pos = slea.readShort();
        Equip eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) pos);
        switch (cube.getItemId()) {
            case 2436499:
            case 2711000:
            case 2711001:
            case 2711009:
            case 2711011: {
                if (GameConstants.isZero(c.getPlayer().getJob()) && eq == null) {
                    eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
                    final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
                    final int rand = Randomizer.nextInt(100);
                    boolean up = false;
                    if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
                        c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
                        return;
                    }
                    if (eq.getState() == 17) {
                        if (rand < 2) {
                            eq.setState((byte) 18);
                            eq2.setState((byte) 18);
                            up = true;
                        } else {
                            eq.setState((byte) 17);
                            eq2.setState((byte) 17);
                        }
                    } else if (eq.getState() == 18) {
                        eq.setState((byte) 18);
                        eq2.setState((byte) 18);
                    }
                    final int level = eq.getState() - 16;
                    final int potential1 = potential(eq.getItemId(), level);
                    final int potential2 = potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(2, 200)) ? level : (level - 1));
                    final int potential3 = (eq.getPotential3() != 0) ? potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(1, 200)) ? level : (level - 1)) : 0;
                    eq.setPotential1(potential1);
                    eq.setPotential2(potential2);
                    eq.setPotential3(potential3);
                    eq2.setPotential1(potential1);
                    eq2.setPotential2(potential2);
                    eq2.setPotential3(potential3);
                    c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
                    c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true, cube.getItemId(), eq.getItemId()));
                    c.getSession().writeAndFlush((Object) CField.getCubeStart(c.getPlayer(), eq, up, cube.getItemId(), cube.getQuantity()));
                    c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIPPED);
                    c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
                    break;
                } else {
                    final int rand2 = Randomizer.nextInt(100);
                    boolean up2 = false;
                    if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
                        c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
                        return;
                    }
                    if (eq.getState() == 17) {
                        if (rand2 < 2) {
                            eq.setState((byte) 18);
                            up2 = true;
                        } else {
                            eq.setState((byte) 17);
                        }
                    } else if (eq.getState() == 18) {
                        eq.setState((byte) 18);
                    }
                    final int level2 = eq.getState() - 16;
                    eq.setPotential1(potential(eq.getItemId(), level2));
                    eq.setPotential2(potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1)));
                    eq.setPotential3((eq.getPotential3() != 0) ? potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(1, 200)) ? level2 : (level2 - 1)) : 0);
                    c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
                    c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true, cube.getItemId(), eq.getItemId()));
                    c.getSession().writeAndFlush((Object) CField.getCubeStart(c.getPlayer(), eq, up2, cube.getItemId(), cube.getQuantity()));
                    c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
                    break;
                }
            }
            case 2730000:
            case 2730001:
            case 2730002:
            case 2730004:
            case 2730005: {
                final int rand2 = Randomizer.nextInt(100);
                boolean up2 = false;
                if (eq == null || c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 1) {
                    break;
                }
                if (eq.getPotential4() <= 0) {
                    c.getPlayer().dropMessage(1, "\uc5d0\ub514\uc154\ub110 \uc7a0\uc7ac\ub2a5\ub825\uc774 \ubd80\uc5ec\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4.");
                    c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    if (GameConstants.isZero(c.getPlayer().getJob())) {
                        final Item item2 = c.getPlayer().getInventory((pos < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP).getItem((short) (-11));
                        final Equip eq3 = (Equip) item2;
                        if (eq3 != null) {
                            eq3.setState((byte) (eq.getState() + 32));
                            c.getPlayer().forceReAddItem_NoUpdate(item2, MapleInventoryType.EQUIPPED);
                        }
                    }
                    int level2 = 0;
                    level2 = ((eq.getPotential4() >= 10000) ? (eq.getPotential4() / 10000) : (eq.getPotential4() / 100));
                    if (level2 >= 4) {
                        level2 = 4;
                    }
                    final int rate = (level2 == 1) ? 3 : 0;
                    if (Randomizer.nextInt(100) < rate) {
                        up2 = true;
                        ++level2;
                    }
                    if (eq.getPotential6() > 0) {
                        eq.setPotential4(potential(eq.getItemId(), level2, true));
                        eq.setPotential5(potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1), true));
                        eq.setPotential6(potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1), true));
                    } else {
                        eq.setPotential4(potential(eq.getItemId(), level2, true));
                        eq.setPotential5(potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1), true));
                    }
                    c.getSession().writeAndFlush((Object) CField.getCubeStart(c.getPlayer(), eq, up2, cube.getItemId(), cube.getQuantity()));
                    c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true, cube.getItemId(), eq.getItemId()));
                    c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
                    c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
                    break;
                }
                c.getPlayer().dropMessage(5, "\uc18c\ube44 \uc544\uc774\ud15c \uc5ec\uc720 \uacf5\uac04\uc774 \ubd80\uc871\ud558\uc5ec \uc7a0\uc7ac\ub2a5\ub825 \uc7ac\uc124\uc815\uc744 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.");
                break;
            }
            case 2711003:
            case 2711005:
            case 2711012: {
                if (GameConstants.isZero(c.getPlayer().getJob()) && eq == null) {
                    eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
                    final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
                    final int rand = Randomizer.nextInt(100);
                    boolean up = false;
                    if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
                        c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
                        return;
                    }
                    if (eq.getState() == 17) {
                        if (Randomizer.isSuccess(2, 200)) {
                            eq.setState((byte) 18);
                            eq2.setState((byte) 18);
                            up = true;
                        } else {
                            eq.setState((byte) 17);
                            eq2.setState((byte) 17);
                        }
                    } else if (eq.getState() == 18) {
                        if (Randomizer.isSuccess(1, 200)) {
                            up = true;
                            eq.setState((byte) 19);
                            eq2.setState((byte) 19);
                        } else {
                            eq.setState((byte) 18);
                        }
                    } else if (eq.getState() == 19) {
                        eq.setState((byte) 19);
                    }
                    final int level = eq.getState() - 16;
                    final int potential1 = potential(eq.getItemId(), level);
                    final int potential2 = potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(2, 200)) ? level : (level - 1));
                    final int potential3 = (eq.getPotential3() != 0) ? potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(1, 200)) ? level : (level - 1)) : 0;
                    eq.setPotential1(potential1);
                    eq.setPotential2(potential2);
                    eq.setPotential3(potential3);
                    eq2.setPotential1(potential1);
                    eq2.setPotential2(potential2);
                    eq2.setPotential3(potential3);
                    c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
                    c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true, cube.getItemId(), eq.getItemId()));
                    c.getSession().writeAndFlush((Object) CField.getCubeStart(c.getPlayer(), eq, up, cube.getItemId(), cube.getQuantity()));
                    c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIPPED);
                    c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
                    break;
                } else {
                    final int rand2 = Randomizer.nextInt(100);
                    boolean up2 = false;
                    if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
                        c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
                        return;
                    }
                    if (eq.getState() == 17) {
                        if (Randomizer.isSuccess(2, 200)) {
                            eq.setState((byte) 18);
                            up2 = true;
                        } else {
                            eq.setState((byte) 17);
                        }
                    } else if (eq.getState() == 18) {
                        if (Randomizer.isSuccess(1, 200)) {
                            up2 = true;
                            eq.setState((byte) 19);
                        } else {
                            eq.setState((byte) 18);
                        }
                    } else if (eq.getState() == 19) {
                        eq.setState((byte) 19);
                    }
                    final int level2 = eq.getState() - 16;
                    eq.setPotential1(potential(eq.getItemId(), level2));
                    eq.setPotential2(potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1)));
                    eq.setPotential3((eq.getPotential3() != 0) ? potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(1, 200)) ? level2 : (level2 - 1)) : 0);
                    c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
                    c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true, cube.getItemId(), eq.getItemId()));
                    c.getSession().writeAndFlush((Object) CField.getCubeStart(c.getPlayer(), eq, up2, cube.getItemId(), cube.getQuantity()));
                    c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
                    break;
                }
            }
            case 2711004:
            case 2711006:
            case 2711013:
            case 2711017: {
                if (GameConstants.isZero(c.getPlayer().getJob()) && eq == null) {
                    eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
                    final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
                    final int rand = Randomizer.nextInt(100);
                    boolean up = false;
                    if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
                        c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
                        return;
                    }
                    if (eq.getState() == 17) {
                        if (Randomizer.isSuccess(2, 200)) {
                            eq.setState((byte) 18);
                            eq2.setState((byte) 18);
                            up = true;
                        } else {
                            eq.setState((byte) 17);
                            eq2.setState((byte) 17);
                        }
                    } else if (eq.getState() == 18) {
                        if (Randomizer.isSuccess(2, 200)) {
                            up = true;
                            eq.setState((byte) 19);
                            eq2.setState((byte) 19);
                        } else {
                            eq.setState((byte) 18);
                        }
                    } else if (eq.getState() == 19) {
                        if (Randomizer.isSuccess(1, 200)) {
                            up = true;
                            eq.setState((byte) 20);
                            eq2.setState((byte) 20);
                        } else {
                            eq.setState((byte) 19);
                        }
                    }
                    final int level = eq.getState() - 16;
                    final int potential1 = potential(eq.getItemId(), level);
                    final int potential2 = potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(2, 200)) ? level : (level - 1));
                    final int potential3 = (eq.getPotential3() != 0) ? potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(1, 200)) ? level : (level - 1)) : 0;
                    eq.setPotential1(potential1);
                    eq.setPotential2(potential2);
                    eq.setPotential3(potential3);
                    eq2.setPotential1(potential1);
                    eq2.setPotential2(potential2);
                    eq2.setPotential3(potential3);
                    c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
                    c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true, cube.getItemId(), eq.getItemId()));
                    c.getSession().writeAndFlush((Object) CField.getCubeStart(c.getPlayer(), eq, up, cube.getItemId(), cube.getQuantity()));
                    c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIPPED);
                    c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
                    break;
                } else {
                    final int rand2 = Randomizer.nextInt(100);
                    boolean up2 = false;
                    if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
                        c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
                        return;
                    }
                    if (eq.getState() == 17) {
                        if (Randomizer.isSuccess(3, 200)) {
                            eq.setState((byte) 18);
                            up2 = true;
                        } else {
                            eq.setState((byte) 17);
                        }
                    } else if (eq.getState() == 18) {
                        if (Randomizer.isSuccess(2, 200)) {
                            up2 = true;
                            eq.setState((byte) 19);
                        } else {
                            eq.setState((byte) 18);
                        }
                    } else if (eq.getState() == 19) {
                        if (Randomizer.isSuccess(1, 200)) {
                            up2 = true;
                            eq.setState((byte) 20);
                        } else {
                            eq.setState((byte) 19);
                        }
                    }
                    final int level2 = eq.getState() - 16;
                    eq.setPotential1(potential(eq.getItemId(), level2));
                    eq.setPotential2(potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1)));
                    eq.setPotential3((eq.getPotential3() != 0) ? potential(eq.getItemId(), (level2 == 1 || Randomizer.isSuccess(1, 200)) ? level2 : (level2 - 1)) : 0);
                    c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
                    c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true, cube.getItemId(), eq.getItemId()));
                    c.getSession().writeAndFlush((Object) CField.getCubeStart(c.getPlayer(), eq, up2, cube.getItemId(), cube.getQuantity()));
                    c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
                    break;
                }
            }
        }
        c.getPlayer().removeItem(cube.getItemId(), -1);
    }

    public static void UseGoldenHammer(final LittleEndianAccessor rh, final MapleClient c) {
        c.getPlayer().vh = false;
        rh.skip(4);
        final byte slot = (byte) rh.readInt();
        final int itemId = rh.readInt();
        rh.skip(4);
        final byte victimslot = (byte) rh.readInt();
        final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
        Equip victim = null;
        Equip victim_ = null;
        if (victimslot < 0) {
            victim = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(victimslot);
            if (GameConstants.isZero(c.getPlayer().getJob())) {
                if (victim.getPosition() == -10) {
                    victim_ = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
                } else {
                    victim_ = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
                }
            }
        } else {
            victim = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(victimslot);
        }
        if (victim == null || toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) {
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        c.getSession().writeAndFlush((Object) CSPacket.ViciousHammer(true, c.getPlayer().vh));
        victim.setViciousHammer((byte) 1);
        if (victim_ != null) {
            victim_.setViciousHammer((byte) 1);
        }
        if ((itemId == 2470001 || itemId == 2470002) && Randomizer.nextInt(100) > 50) {
            victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
            if (victim_ != null) {
                victim_.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
            }
            c.getPlayer().vh = true;
        } else if (itemId == 2470000) {
            victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
            if (victim_ != null) {
                victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 1));
            }
            c.getPlayer().vh = true;
        } else if (itemId == 2470003) {
            victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
            if (victim_ != null) {
                victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 1));
            }
            c.getPlayer().vh = true;
        } else if (itemId == 2470007) {
            victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
            if (victim_ != null) {
                victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 1));
            }
            c.getPlayer().vh = true;
        } else if (itemId == 2470010) {
            victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
            if (victim_ != null) {
                victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 1));
            }
            c.getPlayer().vh = true;
        } else if (itemId == 2470021) {//황망 5회
            victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 5));
            if (victim_ != null) {
                victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 5));
            }
            c.getPlayer().vh = true;
        }
        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
        c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, victim));
        if (victim_ != null) {
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIPPED, victim));
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIPPED, victim_));
            c.getPlayer().getMap().broadcastMessage(CField.getScrollEffect(c.getPlayer().getId(), Equip.ScrollResult.SUCCESS, false, itemId, victim_.getItemId()));
        }
    }

    public static void Todd(final LittleEndianAccessor slea, final MapleClient c) {
    }

    public static void returnScrollResult(LittleEndianAccessor slea, MapleClient c) {
        slea.skip(4);
        byte type = slea.readByte();
        Equip equip = null;
        Equip zeroequip = null;

        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        if (c.getPlayer().returnscroll == null) {
            c.getPlayer().dropMessage(1, "리턴 스크롤 사용 중 오류가 발생하였습니다.");
            return;
        }

        equip = c.getPlayer().returnscroll.getPosition() > 0 ? (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(c.getPlayer().returnscroll.getPosition()) : (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(c.getPlayer().returnscroll.getPosition());
        if (equip == null) {
            c.getPlayer().dropMessage(1, "리턴 스크롤 사용 중 오류가 발생하였습니다.");
            return;
        }
        if (type == 1) {
            if (c.getPlayer().returnscroll.getPosition() > 0) {
                equip.set(c.getPlayer().returnscroll);
            } else {
                equip.set(c.getPlayer().returnscroll);
            }
        }
        equip.setFlag(equip.getFlag() - ItemFlag.RETURN_SCROLL.getValue());
        if (GameConstants.isZeroWeapon(c.getPlayer().returnscroll.getItemId())) {
            zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
            zeroequip.set(c.getPlayer().returnscroll);
            zeroequip.setFlag(equip.getFlag() - ItemFlag.RETURN_SCROLL.getValue());
        }
        c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, equip));
        if (zeroequip != null) {
            c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, zeroequip));
        }
        if (type == 1) {
            c.getSession().writeAndFlush((Object) CField.getGameMessage(11, "리턴 주문서의 힘으로 " + ii.getName(equip.getItemId()) + "가 " + ii.getName(c.getPlayer().returnSc) + " 사용 이전 상태로 돌아왔습니다."));
        } else {
            c.getSession().writeAndFlush((Object) CField.getGameMessage(11, "리턴 주문서의 효과가 사라졌습니다."));
        }
        c.getSession().writeAndFlush((Object) CWvsContext.returnEffectModify(null, 0));
        c.getPlayer().returnscroll = null;
        c.getPlayer().returnSc = 0;
    }

    public static void ArcaneCatalyst(final LittleEndianAccessor slea, final MapleClient c) {
        slea.skip(4);
        final int slot = slea.readInt();
        final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slot).copy();
        equip.setEquipmentType(equip.getEquipmentType() | 0x4000);
        equip.setArcLevel(1);
        int totalexp = 0;
        for (int i = 1; i < equip.getArcLevel(); ++i) {
            totalexp += GameConstants.ArcaneNextUpgrade(i);
        }
        totalexp += equip.getArcEXP();
        equip.setArcEXP((int) Math.floor(totalexp * 0.8));
        if (GameConstants.isXenon(c.getPlayer().getJob())) {
            equip.setStr((short) 117);
            equip.setDex((short) 117);
            equip.setLuk((short) 117);
        } else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
            equip.setHp((short) 4200);
        } else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
            equip.setStr((short) 300);
        } else if (GameConstants.isMagician(c.getPlayer().getJob())) {
            equip.setInt((short) 300);
        } else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob()) || GameConstants.isMechanic(c.getPlayer().getJob()) || GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
            equip.setDex((short) 300);
        } else if (GameConstants.isThief(c.getPlayer().getJob())) {
            equip.setLuk((short) 300);
        } else if (GameConstants.isPirate(c.getPlayer().getJob())) {
            equip.setStr((short) 300);
        }
        c.getSession().writeAndFlush((Object) CWvsContext.ArcaneCatalyst(equip, slot));
    }

    public static void ArcaneCatalyst2(final LittleEndianAccessor slea, final MapleClient c) {
        slea.skip(4);
        final int slot = slea.readInt();
        final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slot);
        if ((equip.getEquipmentType() & 0x4000) == 0x0) {
            equip.setArc((short) 30);
            int totalexp = 0;
            for (int i = 1; i < equip.getArcLevel(); ++i) {
                totalexp += GameConstants.ArcaneNextUpgrade(i);
            }
            totalexp += equip.getArcEXP();
            equip.setArcEXP((int) Math.floor(totalexp * 0.8));
            equip.setArcLevel(1);
            if (GameConstants.isXenon(c.getPlayer().getJob())) {
                equip.setStr((short) 117);
                equip.setDex((short) 117);
                equip.setLuk((short) 117);
            } else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
                equip.setHp((short) 4200);
            } else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
                equip.setStr((short) 300);
            } else if (GameConstants.isMagician(c.getPlayer().getJob())) {
                equip.setInt((short) 300);
            } else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob()) || GameConstants.isMechanic(c.getPlayer().getJob()) || GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
                equip.setDex((short) 300);
            } else if (GameConstants.isThief(c.getPlayer().getJob())) {
                equip.setLuk((short) 300);
            } else if (GameConstants.isPirate(c.getPlayer().getJob())) {
                equip.setStr((short) 300);
            }
            equip.setEquipmentType(equip.getEquipmentType() | 0x4000);
        } else {
            equip.setEquipmentType(equip.getEquipmentType() - 16384);
        }
        c.getSession().writeAndFlush((Object) CWvsContext.ArcaneCatalyst2(equip));
        c.getPlayer().removeItem(2535000, -1);
        c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIP);
    }

    public static void ArcaneCatalyst3(final LittleEndianAccessor slea, final MapleClient c) {
        final int slot = slea.readInt();
        final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slot).copy();
        equip.setEquipmentType(equip.getEquipmentType() - 16384);
        c.getSession().writeAndFlush((Object) CWvsContext.ArcaneCatalyst(equip, slot));
    }

    public static void ArcaneCatalyst4(final LittleEndianAccessor slea, final MapleClient c) {
        final int slot = slea.readInt();
        final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slot);
        equip.setEquipmentType(equip.getEquipmentType() - 16384);
        c.getSession().writeAndFlush((Object) CWvsContext.ArcaneCatalyst2(equip));
        c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIP);
    }

    public static void ReturnSynthesizing(final LittleEndianAccessor slea, final MapleClient c) {
        slea.skip(4);
        final int scrollId = slea.readInt();
        slea.skip(4);
        final int eqpId = slea.readInt();
        final int eqpslot = slea.readInt();
        final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) eqpslot);
        if (equip.getItemId() == eqpId) {
            equip.setMoru(0);
            c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIP);
            final StringBuilder msg = new StringBuilder("[");
            msg.append(MapleItemInformationProvider.getInstance().getName(equip.getItemId()));
            msg.append("]\uc758 \uc678\ud615\uc774 \uc6d0\ub798\ub300\ub85c \ubcf5\uad6c\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
            c.getSession().writeAndFlush((Object) CWvsContext.showPopupMessage(msg.toString()));
            c.getPlayer().gainItem(scrollId, -1);
        }
    }

    public static void blackRebirthResult(final LittleEndianAccessor slea, final MapleClient c) {
        final int result = slea.readInt();
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(c.getPlayer().blackRebirthScroll.getPosition());
        if (eq == null) {
            eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(c.getPlayer().blackRebirthScroll.getPosition());
            if (eq == null) {
                return;
            }
        }
        Equip zeroequip = null;
        if (eq.getPosition() == -11) {
            zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
        } else if (eq.getPosition() == -10) {
            zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
        }
        if (result == 2) {
            eq.resetRebirth(ii.getReqLevel(eq.getItemId()));
            final int[] rebirth = new int[4];
            final String fire = String.valueOf(c.getPlayer().blackRebirth);
            final Equip ordinary = (Equip) MapleItemInformationProvider.getInstance().getEquipById(eq.getItemId(), false);
            Equip ordinary2 = null;
            int ordinaryPad2 = 0;
            int ordinaryMad2 = 0;
            if (zeroequip != null) {
                zeroequip.resetRebirth(ii.getReqLevel(zeroequip.getItemId()));
                ordinary2 = (Equip) MapleItemInformationProvider.getInstance().getEquipById(zeroequip.getItemId(), false);
                ordinaryPad2 = ((ordinary2.getWatk() > 0) ? ordinary2.getWatk() : ordinary2.getMatk());
                ordinaryMad2 = ((ordinary2.getMatk() > 0) ? ordinary2.getMatk() : ordinary2.getWatk());
            }
            final int ordinaryPad3 = (ordinary.getWatk() > 0) ? ordinary.getWatk() : ordinary.getMatk();
            final int ordinaryMad3 = (ordinary.getMatk() > 0) ? ordinary.getMatk() : ordinary.getWatk();
            if (fire.length() == 12) {
                rebirth[0] = Integer.parseInt(fire.substring(0, 3));
                rebirth[1] = Integer.parseInt(fire.substring(3, 6));
                rebirth[2] = Integer.parseInt(fire.substring(6, 9));
                rebirth[3] = Integer.parseInt(fire.substring(9));
            } else if (fire.length() == 11) {
                rebirth[0] = Integer.parseInt(fire.substring(0, 2));
                rebirth[1] = Integer.parseInt(fire.substring(2, 5));
                rebirth[2] = Integer.parseInt(fire.substring(5, 8));
                rebirth[3] = Integer.parseInt(fire.substring(8));
            } else if (fire.length() == 10) {
                rebirth[0] = Integer.parseInt(fire.substring(0, 1));
                rebirth[1] = Integer.parseInt(fire.substring(1, 4));
                rebirth[2] = Integer.parseInt(fire.substring(4, 7));
                rebirth[3] = Integer.parseInt(fire.substring(7));
            }
            eq.setFire(c.getPlayer().blackRebirth);
            if (zeroequip != null) {
                zeroequip.setFire(c.getPlayer().blackRebirth);
            }
            for (int i = 0; i < rebirth.length; ++i) {
                final int value = rebirth[i] - rebirth[i] / 10 * 10;
                eq.setFireOption(rebirth[i] / 10, ii.getReqLevel(eq.getItemId()), value, ordinaryPad3, ordinaryMad3);
                if (zeroequip != null && ordinaryPad2 != 0) {
                    zeroequip.setFireOption(rebirth[i] / 10, ii.getReqLevel(zeroequip.getItemId()), value, ordinaryPad2, ordinaryMad2);
                }
            }
            c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
            if (zeroequip != null) {
                c.getPlayer().forceReAddItem(zeroequip, MapleInventoryType.EQUIP);
            }
        } else if (result == 3) {
            final Item scroll = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(c.getPlayer().blackRebirthPos);
            if (scroll != null) {
                final Equip neweqs = (Equip) eq.copy();
                neweqs.resetRebirth(ii.getReqLevel(neweqs.getItemId()));
                neweqs.setFire(neweqs.newRebirth(ii.getReqLevel(neweqs.getItemId()), scroll.getItemId(), true));
                final long newRebirth = neweqs.getFire();
                c.getSession().writeAndFlush((Object) CWvsContext.useBlackRebirthScroll(eq, scroll, newRebirth, false));
                MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(scroll.getItemId()), scroll.getPosition(), (short) 1, false, false);
                c.getSession().writeAndFlush((Object) CWvsContext.blackRebirthResult(true, eq.getFire(), eq));
                c.getSession().writeAndFlush((Object) CWvsContext.blackRebirthResult(false, newRebirth, neweqs));
                c.getPlayer().blackRebirth = newRebirth;
                c.getPlayer().blackRebirthScroll = (Equip) eq.copy();
            }
        }
        if (result == 1 || result == 2) {
            c.getSession().writeAndFlush((Object) CWvsContext.useBlackRebirthScroll(eq, null, 0L, true));
        }
        c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
    }

    public static void UseCirculator(LittleEndianAccessor slea, MapleClient c) {
        int itemId = slea.readInt();
        int slot = slea.readInt();
        Item item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem((short) slot);
        if (item.getItemId() == itemId) {

            List<InnerSkillValueHolder> newValues = new LinkedList<InnerSkillValueHolder>();
            InnerSkillValueHolder ivholder = null;
            InnerSkillValueHolder ivholder2 = null;
            int nowrank = -1;
            switch (itemId) {
                case 2702003:
                case 2702004:
                    if (c.getPlayer().getInnerSkills().size() > 0) {
                        nowrank = c.getPlayer().getInnerSkills().get(0).getRank();
                    }

                    for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills()) {
                        newValues.add(InnerAbillity.getInstance().renewLevel(nowrank, isvh.getSkillId()));
                        c.getPlayer().changeSkillLevel_Inner(SkillFactory.getSkill(isvh.getSkillId()), (byte) 0, (byte) 0);
                    }
                    break;
                case 2702006:
                    nowrank = 3;
                    for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills()) {
                        if (ivholder == null) {
                            ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false);
                            while (isvh.getSkillId() == ivholder.getSkillId()) {
                                ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false);
                            }
                            newValues.add(ivholder);
                        } else if (ivholder2 == null) {
                            ivholder2 = InnerAbillity.getInstance().renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false);
                            while (isvh.getSkillId() == ivholder2.getSkillId() || ivholder.getSkillId() == ivholder2.getSkillId()) {
                                ivholder2 = InnerAbillity.getInstance().renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false);
                            }
                            newValues.add(ivholder2);
                        } else {
                            InnerSkillValueHolder ivholder3 = InnerAbillity.getInstance().renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false);
                            while (isvh.getSkillId() == ivholder3.getSkillId() || ivholder.getSkillId() == ivholder3.getSkillId() || ivholder2.getSkillId() == ivholder3.getSkillId()) {
                                ivholder3 = InnerAbillity.getInstance().renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false);
                            }
                            newValues.add(ivholder3);
                        }
                        c.getPlayer().changeSkillLevel_Inner(SkillFactory.getSkill(isvh.getSkillId()), (byte) 0, (byte) 0);
                    }
                    break;
                case 2702002:
                    nowrank = 2;
                    for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills()) {
                        if (ivholder == null) {
                            ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false);
                            while (isvh.getSkillId() == ivholder.getSkillId()) {
                                ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false);
                            }
                            newValues.add(ivholder);
                        } else if (ivholder2 == null) {
                            ivholder2 = InnerAbillity.getInstance().renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false);
                            while (isvh.getSkillId() == ivholder2.getSkillId() || ivholder.getSkillId() == ivholder2.getSkillId()) {
                                ivholder2 = InnerAbillity.getInstance().renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false);
                            }
                            newValues.add(ivholder2);
                        } else {
                            InnerSkillValueHolder ivholder3 = InnerAbillity.getInstance().renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false);
                            while (isvh.getSkillId() == ivholder3.getSkillId() || ivholder.getSkillId() == ivholder3.getSkillId() || ivholder2.getSkillId() == ivholder3.getSkillId()) {
                                ivholder3 = InnerAbillity.getInstance().renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false);
                            }
                            newValues.add(ivholder3);
                        }
                        c.getPlayer().changeSkillLevel_Inner(SkillFactory.getSkill(isvh.getSkillId()), (byte) 0, (byte) 0);
                    }
                    break;
            }
            if (newValues.size() == 3) {
                c.getPlayer().getInnerSkills().clear();
                for (InnerSkillValueHolder isvh : newValues) {
                    c.getPlayer().getInnerSkills().add(isvh);
                    c.getPlayer().changeSkillLevel_Inner(SkillFactory.getSkill(isvh.getSkillId()), isvh.getSkillLevel(), isvh.getSkillLevel());
                    c.getPlayer().getClient().getSession().writeAndFlush(CField.updateInnerAbility(isvh, c.getPlayer().getInnerSkills().size(), c.getPlayer().getInnerSkills().size() == 3));
                }
                c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, (short) slot, (short) 1, false);
            }
        }
    }
}
