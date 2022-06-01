package tools.packet;

import client.AvatarLook;
import client.BuddylistEntry;
import client.Core;
import client.InnerSkillValueHolder;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleHyperStats;
import client.MapleMannequin;
import client.MapleQuestStatus;
import client.MapleStat;
import client.MapleTrait;
import client.MapleTrait.MapleTraitType;
import client.MapleUnion;
import client.MatrixSkill;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.Skill;
import client.SkillEntry;
import client.SkillFactory;
import client.inventory.AuctionItem;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.MapleRing;
import constants.GameConstants;
import constants.ServerConstants;
import handling.SendPacketOpcode;
import handling.channel.MapleGuildRanking;
import handling.channel.handler.PsychicGrabEntry;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import handling.world.guild.MapleGuildCharacter;
import handling.world.guild.MapleGuildSkill;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.enchant.EnchantFlag;
import server.enchant.EquipmentEnchant;
import server.enchant.EquipmentScroll;
import server.enchant.StarForceStats;
import server.life.MapleMonster;
import server.life.MobSkill;
import server.life.PlayerNPC;
import server.maps.MapleMist;
import server.marriage.MarriageDataEntry;
import server.marriage.MarriageManager;
import tools.HexTool;
import tools.Pair;
import tools.StringUtil;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;

public class CWvsContext {

    public static class InventoryPacket {

        public static byte[] addInventorySlot(MapleInventoryType type, Item item) {
            return addInventorySlot(type, item, false);
        }

        public static byte[] addInventorySlot(MapleInventoryType type, Item item, boolean fromDrop) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(GameConstants.isInBag(item.getPosition(), type.getType()) ? 9 : 0);
            mplew.write(Math.max(1, type.getType()));
            mplew.writeShort(item.getPosition());
            PacketHelper.addItemInfo(mplew, item);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] updateInventoryItem(boolean fromDrop, MapleInventoryType type, Item item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop);
            mplew.write(2);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(3);
            mplew.write(Math.max(1, type.getType()));
            mplew.writeShort(item.getPosition());
            mplew.write(0);
            mplew.write(Math.max(1, type.getType()));
            mplew.writeShort(item.getPosition());
            PacketHelper.addItemInfo(mplew, item);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] updateInventorySlot(MapleInventoryType type, Item item, boolean fromDrop) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(GameConstants.isInBag(item.getPosition(), type.getType()) ? 6 : 1);
            mplew.write(Math.max(1, type.getType()));
            mplew.writeShort(item.getPosition());
            mplew.writeShort(item.getQuantity());
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] moveInventoryItem(MapleInventoryType type, short source, short target, boolean bag, boolean bothBag) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(bag ? (bothBag ? 8 : 5) : 2);
            mplew.write(Math.max(1, type.getType()));
            mplew.writeShort(source);
            if (bag && !bothBag) {
                mplew.writeInt(target);
            } else {
                mplew.writeShort(target);
            }
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] moveInventoryItem(MapleInventoryType type, List<Pair<Short, Short>> updateSlots) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(updateSlots.size());
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            for (Pair<Short, Short> updateSlot : updateSlots) {
                mplew.write(2);
                mplew.write(Math.max(1, type.getType()));
                mplew.writeShort(((Short) updateSlot.left).shortValue());
                mplew.writeShort(((Short) updateSlot.right).shortValue());
            }
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] updateScrollandItem(Item scroll, Item item) {
            return updateScrollandItem(scroll, item, false);
        }

        public static byte[] updateScrollandItem(Item scroll, Item item, boolean destroyed) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(false);
            mplew.write(2);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            if (scroll.getQuantity() > 0) {
                mplew.write(1);
                mplew.write(GameConstants.getInventoryType(scroll.getItemId()).getType());
                mplew.writeShort(scroll.getPosition());
                mplew.writeShort(scroll.getQuantity());
            } else {
                mplew.write(3);
                mplew.write(GameConstants.getInventoryType(scroll.getItemId()).getType());
                mplew.writeShort(scroll.getPosition());
            }
            if (!destroyed) {
                mplew.write(0);
                mplew.write(GameConstants.getInventoryType(item.getItemId()).getType());
                mplew.writeShort(item.getPosition());
                PacketHelper.addItemInfo(mplew, item);
            } else {
                mplew.write(3);
                mplew.write(GameConstants.getInventoryType(item.getItemId()).getType());
                mplew.writeShort(item.getPosition());
            }
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] moveAndCombineItem(Item src, Item dst) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(2);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(3);
            mplew.write(GameConstants.getInventoryType(src.getItemId()).getType());
            mplew.writeShort(src.getPosition());
            mplew.write(1);
            mplew.write(GameConstants.getInventoryType(dst.getItemId()).getType());
            mplew.writeShort(dst.getPosition());
            mplew.writeShort(dst.getQuantity());
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] moveAndCombineWithRestItem(Item src, Item dst) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(2);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(1);
            mplew.write(GameConstants.getInventoryType(src.getItemId()).getType());
            mplew.writeShort(src.getPosition());
            mplew.writeShort(src.getQuantity());
            mplew.write(1);
            mplew.write(GameConstants.getInventoryType(dst.getItemId()).getType());
            mplew.writeShort(dst.getPosition());
            mplew.writeShort(dst.getQuantity());
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] clearInventoryItem(MapleInventoryType type, short slot, boolean fromDrop) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop ? 1 : 0);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(3);
            mplew.write(Math.max(1, type.getType()));
            mplew.writeShort(slot);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] UpedateInventoryItem(int src, int dst) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(2);
            mplew.write(6);
            mplew.writeShort(src);
            mplew.writeShort(dst);
            mplew.write(8);
            return mplew.getPacket();
        }

        public static byte[] UpedateInventoryItemS(int src, int dst) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(2);
            mplew.write(1);
            mplew.writeShort(src);
            mplew.writeShort(dst);
            mplew.write(24);
            return mplew.getPacket();
        }

        public static byte[] getInventoryFull() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] getInventoryStatus(boolean fromDrop) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop ? 1 : 0);
            mplew.write(0);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] getSlotUpdate(byte invType, byte newSlots) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_GROW.getValue());
            mplew.write(invType);
            mplew.write(newSlots);
            return mplew.getPacket();
        }

        public static byte[] getFusionAnvil(boolean success, int index, int itemID) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.FUSION_ANVIL.getValue());
            mplew.write(success);
            mplew.writeInt(index);
            mplew.writeInt(itemID);
            mplew.writeZeroBytes(200);
            return mplew.getPacket();
        }

        public static byte[] updateEquipSlot(Item item) {
            return updateEquipSlot(item, false);
        }

        public static byte[] updateEquipSlot(Item item, boolean symbollevelup) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(ii.isCash(item.getItemId()) ? 6 : item.getType());
            mplew.writeShort(item.getPosition());
            PacketHelper.addItemInfo(mplew, item);
            mplew.write(GameConstants.isArcaneSymbol(item.getItemId()) ? (symbollevelup ? 2 : 1) : 0);
            if (GameConstants.isArcaneSymbol(item.getItemId())) {
                PacketHelper.ArcaneSymbol(mplew, item);
            }
            mplew.writeMapleAsciiString("");
            if (item.getPosition() < 0) {
                mplew.write(2);
            }
            return mplew.getPacket();
        }

        public static byte[] getShowInventoryFull() {
            return CWvsContext.InfoPacket.getShowInventoryStatus(255);
        }

        public static byte[] showItemUnavailable() {
            return CWvsContext.InfoPacket.getShowInventoryStatus(254);
        }
    }

    public static class BuffPacket {

        public static byte[] checkSunfireSkill(int gauge) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.LUMINOUS_MORPH.getValue());
            mplew.writeInt(Math.min(gauge, 9999));
            mplew.write((gauge <= 1) ? 1 : 2);
            return mplew.getPacket();
        }

        public static byte[] checkEclipseSkill(int gauge) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.LUMINOUS_MORPH.getValue());
            mplew.writeInt(Math.min(gauge, 9999));
            mplew.write((gauge >= 9999) ? 2 : 1);
            return mplew.getPacket();
        }

        public static byte[] LuminusMorph(int gauge, boolean islight) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.LUMINOUS_MORPH.getValue());
            mplew.writeInt(Math.min(gauge, 9999));
            mplew.write(islight ? 2 : 1);
            return mplew.getPacket();
        }

        public static byte[] giveBuff(Map<SecondaryStat, Pair<Integer, Integer>> statups, SecondaryStatEffect effect, MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups = PacketHelper.sortBuffStats(statups);
            PacketHelper.writeBuffMask(mplew, newstatups);
            for (Pair<SecondaryStat, Pair<Integer, Integer>> stat : newstatups) {
                if (!((SecondaryStat) stat.getLeft()).canStack() && !((SecondaryStat) stat.getLeft()).isSpecialBuff() && !statups.containsKey(SecondaryStat.KillingPoint) && !statups.containsKey(SecondaryStat.PinkbeanRollingGrade)) {
                    if (SecondaryStat.isEncode4Byte(statups) || (stat.getLeft() == SecondaryStat.IgnisRore && effect.getSourceId() == 400031017)) {
                        mplew.writeInt(((Integer) ((Pair) stat.getRight()).left).intValue());
                    } else {
                        mplew.writeShort(((Integer) ((Pair) stat.getRight()).left).intValue());
                    }
                    if (stat.getLeft() == SecondaryStat.KinesisPsychicPoint || ((SecondaryStat) stat.getLeft()).SpectorEffect() || stat.getLeft() == SecondaryStat.HoyoungThirdProperty || stat.getLeft() == SecondaryStat.TidalForce || stat.getLeft() == SecondaryStat.YetiAnger || stat.getLeft() == SecondaryStat.YetiAngerMode) {
                        mplew.writeInt(chr.getJob());
                    } else if (stat.getLeft() == SecondaryStat.CardinalMark) {
                        mplew.writeInt(chr.cardinalMark);
                    } else if (stat.getLeft() == SecondaryStat.AdelGauge) {
                        mplew.writeInt(15002);
                    } else if (stat.getLeft() == SecondaryStat.BlackMageDebuff) {
                        mplew.writeInt(80002623);
                    } else if (stat.getLeft() == SecondaryStat.PoseType && effect.getSourceId() == 11121011) {
                        mplew.writeInt(11111022);
                    } else if ((stat.getLeft() == SecondaryStat.Buckshot || stat.getLeft() == SecondaryStat.PoseType) && effect.getSourceId() == 11121012) {
                        mplew.writeInt(11101022);
                    } else if (stat.getLeft() == SecondaryStat.IgnisRore && effect.getSourceId() == 400031017) {
                        mplew.writeInt(23110004);
                    } else if (effect == null) {
                        if (stat.getLeft() == SecondaryStat.AncientGuidance) {
                            mplew.writeInt(chr.getJob());
                        } else if (stat.getLeft() == SecondaryStat.RWCylinder) {
                            mplew.writeInt(1);
                        } else if (stat.getLeft() == SecondaryStat.Malice) {
                            mplew.writeInt(6003);
                        } else {
                            mplew.writeInt(0);
                        }
                    } else if ((((SecondaryStat) stat.getLeft()).isItemEffect() && effect.getSourceId() != 135001009) || !effect.isSkill() || SkillFactory.getSkill(effect.getSourceId()) == null) {
                        mplew.writeInt(-effect.getSourceId());
                    } else {
                        mplew.writeInt(effect.getSourceId());
                    }
                    mplew.writeInt((effect == null) ? 0 : ((Integer) ((Pair) stat.right).right).intValue());
                }
            }
            if (statups.containsKey(SecondaryStat.SoulMP)) {
                mplew.writeInt(1000);
                mplew.writeInt(effect.getSourceId());
            }
            if (statups.containsKey(SecondaryStat.FullSoulMP)) {
                mplew.writeInt(0);
            }
            System.err.println("0 : " + mplew);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            mplew.writeShort(statups.containsKey(SecondaryStat.Etherealform) ? -29 : 0);
            mplew.write(0);
            if (chr.getBuffedValue(23121054)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.Morph)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.Asr) && (statups.containsKey(SecondaryStat.Ter)) && chr.getBuffedValue(1121054)) {
                mplew.writeInt(chr.발할라검격);
            }
            if (statups.containsKey(SecondaryStat.DiceRoll)) {
                giveDice(mplew, effect, chr);
            }
            if (statups.containsKey(SecondaryStat.CurseOfCreation)) {
                mplew.writeInt(10);
            }
            if (statups.containsKey(SecondaryStat.CurseOfDestruction)) {
                mplew.writeInt(15);
            }
            if (statups.containsKey(SecondaryStat.UnkBuffStat28)) {
                mplew.write(0);
            }
            if (statups.containsKey(SecondaryStat.KeyDownMoving)) {
                if (effect.getSourceId() == 131001020) {
                    mplew.writeInt((int) chr.getSkillCustomValue0(effect.getSourceId()));
                } else {
                    mplew.writeInt(1);
                }
            }
            if (statups.containsKey(SecondaryStat.KillingPoint)) {
                mplew.write(chr.killingpoint);
            }
            if (statups.containsKey(SecondaryStat.PinkbeanRollingGrade)) {
                mplew.write(1);
            }
            if (statups.containsKey(SecondaryStat.Judgement)) {
                if (((Integer) ((Pair) statups.get(SecondaryStat.Judgement)).left).intValue() == 1) {
                    mplew.writeInt(effect.getV());
                } else if (((Integer) ((Pair) statups.get(SecondaryStat.Judgement)).left).intValue() == 2) {
                    mplew.writeInt(effect.getW());
                } else if (((Integer) ((Pair) statups.get(SecondaryStat.Judgement)).left).intValue() == 3) {
                    mplew.writeInt((effect.getX() << 8) + effect.getY());
                } else {
                    mplew.writeInt(0);
                }
            }
            if (statups.containsKey(SecondaryStat.StackBuff)) {
                mplew.write(chr.stackbuff);
            }
            if (statups.containsKey(SecondaryStat.Trinity)) {
                mplew.write((int) chr.getSkillCustomValue0(65121101));
            }
            if (statups.containsKey(SecondaryStat.ElementalCharge)) {
                mplew.write(chr.getElementalCharge());
                mplew.writeShort(effect.getY() * chr.getElementalCharge());
                mplew.write(effect.getU() * chr.getElementalCharge());
                mplew.write(effect.getW() * chr.getElementalCharge());
            }
            if (statups.containsKey(SecondaryStat.LifeTidal)) {
                switch (((Integer) ((Pair) statups.get(SecondaryStat.LifeTidal)).left).intValue()) {
                    case 1:
                        mplew.writeInt(effect.getX());
                        break;
                    case 2:
                        mplew.writeInt(effect.getProp());
                        break;
                    case 3:
                        mplew.writeInt(chr.getStat().getCurrentMaxHp());
                        break;
                    default:
                        mplew.writeInt(0);
                        break;
                }
            }
            if (statups.containsKey(SecondaryStat.AntiMagicShell)) {
                mplew.write(chr.getAntiMagicShell());
                mplew.writeInt(effect.getDuration());
            }
            if (statups.containsKey(SecondaryStat.Larkness)) {
                mplew.writeInt(effect.getSourceId());
                mplew.writeInt(effect.getDuration());
                mplew.writeInt((effect.getSourceId() == 20040216 || effect.getSourceId() == 20040217) ? 0 : ((effect.getSourceId() == 20040219) ? 20040220 : 20040219));
                mplew.writeInt(effect.getDuration());
                mplew.writeInt((effect.getSourceId() == 20040217) ? 10000 : -1);
                mplew.writeInt((effect.getSourceId() == 20040216) ? -1 : 1);
                mplew.writeInt((chr.getSkillLevel(400021005) > 0 && !chr.getUseTruthDoor() && (effect.getSourceId() == 20040219 || effect.getSourceId() == 20040220)) ? 1 : 0);
            }
            if (statups.containsKey(SecondaryStat.IgnoreTargetDEF)) {
                mplew.writeInt(chr.lightning);
            }
            if (statups.containsKey(SecondaryStat.StopForceAtominfo)) {
                mplew.writeInt((effect.getSourceId() == 61121217) ? 4 : ((effect.getSourceId() == 61110211) ? 3 : ((effect.getSourceId() != 61101002 && effect.getSourceId() != 61110211) ? 2 : 1)));
                mplew.writeInt((effect.getSourceId() != 61101002 && effect.getSourceId() != 61110211) ? 5 : 3);
                mplew.writeInt((chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11) == null) ? 0 : chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId());
                mplew.writeInt((effect.getSourceId() != 61101002 && effect.getSourceId() != 61110211) ? 5 : 3);
                mplew.writeZeroBytes((effect.getSourceId() != 61101002 && effect.getSourceId() != 61110211) ? 20 : 12);
            }
            if (statups.containsKey(SecondaryStat.SmashStack)) {
                mplew.writeInt((chr.getKaiserCombo() >= 300) ? 2 : ((chr.getKaiserCombo() >= 100) ? 1 : 0));
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.MobZoneState)) {
                mplew.writeInt(((Integer) ((Pair) statups.get(SecondaryStat.MobZoneState)).right).intValue());
                mplew.writeInt(-1);
            }
            if (statups.containsKey(SecondaryStat.IncreaseJabelinDam)) {
                mplew.writeInt(2);
                mplew.writeInt(152120001);
                mplew.writeInt(400021000);
            }
            if (statups.containsKey(SecondaryStat.Slow)) {
                mplew.write(0);
            }
            if (statups.containsKey(SecondaryStat.IgnoreMobPdpR)) {
                mplew.write(0);
            }
            if (statups.containsKey(SecondaryStat.BdR)) {
                mplew.write(0);
            }
            if (statups.containsKey(SecondaryStat.DropRIncrease)) {
                mplew.writeInt(0);
                mplew.write(0);
            }
            if (statups.containsKey(SecondaryStat.PoseType)) {
                mplew.write(chr.getBuffedValue(11121005));
            }
            if (statups.containsKey(SecondaryStat.Beholder)) {
                mplew.writeInt(chr.getBeholderSkill1());
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.CrossOverChain)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.Reincarnation)) {
                mplew.writeInt(chr.getReinCarnation());
            }
            if (statups.containsKey(SecondaryStat.ExtremeArchery)) {
                mplew.writeInt(effect.getX());
                mplew.writeInt(effect.getZ());
            }
            if (statups.containsKey(SecondaryStat.QuiverCatridge)) {
                mplew.writeInt(chr.getQuiverType());
            }
            if (statups.containsKey(SecondaryStat.ImmuneBarrier)) {
                mplew.writeInt(((Integer) ((Pair) statups.get(SecondaryStat.ImmuneBarrier)).left).intValue());
            }
            if (statups.containsKey(SecondaryStat.ArmorPiercing)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.SharpEyes)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.AdvancedBless) || statups.containsKey(SecondaryStat.Bless)) {
                if (chr.getSkillLevel(2320050) > 0) {
                    mplew.writeInt(SkillFactory.getSkill(2320050).getEffect(1).getBdR());
                } else {
                    mplew.writeInt(0);
                }
                mplew.writeInt(30);
            }
            if (statups.containsKey(SecondaryStat.DotHealHPPerSecond)) {
                mplew.writeInt(180);
            }
            if (statups.containsKey(SecondaryStat.DotHealMPPerSecond)) {
                mplew.writeInt(180);
            }
            if (statups.containsKey(SecondaryStat.SpiritGuard)) {
                mplew.writeInt(chr.getSpiritGuard());
            }
            if (statups.containsKey(SecondaryStat.DemonDamageAbsorbShield)) {
                mplew.writeInt(chr.getSkillCustomValue0(400001016));
            }
            if (statups.containsKey(SecondaryStat.KnockBack)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.ShieldAttack)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.SSFShootingAttack)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.BattlePvP_Helena_Mark)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.PinkbeanAttackBuff)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.RoyalGuardState)) {
                mplew.writeInt(((Integer) ((Pair) statups.get(SecondaryStat.IndiePad)).left).intValue());
                mplew.writeInt(((Integer) ((Pair) statups.get(SecondaryStat.RoyalGuardState)).left).intValue());
            }
            if (statups.containsKey(SecondaryStat.MichaelSoulLink)) {
                boolean isParty = (chr.getParty() != null);
                int size = 0;
                if (isParty) {
                    for (MaplePartyCharacter chr1 : chr.getParty().getMembers()) {
                        MapleCharacter chr2 = chr.getMap().getCharacter(chr1.getId());
                        if ((chr1.isOnline() && chr2.getBuffedValue(51111008)) || ((chr.getTruePosition()).x + (effect.getLt()).x < (chr2.getTruePosition()).x && (chr.getTruePosition()).x - (effect.getLt()).x > (chr2.getTruePosition()).x && (chr.getTruePosition()).y + (effect.getLt()).y < (chr2.getTruePosition()).y && (chr.getTruePosition()).y - (effect.getLt()).y > (chr2.getTruePosition()).y)) {
                            size++;
                        }
                    }
                }
                mplew.writeInt(isParty ? chr.getParty().getMembers().size() : 1);
                mplew.write((size >= 2) ? 0 : 1);
                mplew.writeInt(isParty ? chr.getParty().getId() : 0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.AdrenalinBoost)) {
                mplew.write((int) chr.getSkillCustomValue0(21110016));
            }
            if (statups.containsKey(SecondaryStat.RWCylinder)) {
                mplew.write(chr.getBullet());
                mplew.writeShort(chr.getCylinderGauge());
                mplew.write((int) chr.getSkillCustomValue0(400011091));
            }
            if (statups.containsKey(SecondaryStat.BodyOfSteal)) {
                mplew.writeInt(chr.bodyOfSteal);
            }
            if (statups.containsKey(SecondaryStat.RWUnk)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.RwMagnumBlow)) {
                mplew.writeShort(0);
                mplew.write(0);
            }
            if (statups.containsKey(SecondaryStat.BladeStance)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.DarkSight)) {
                mplew.writeInt(1000000);
                mplew.writeInt(0);
            }
            /*if (statups.containsKey(SecondaryStat.Stigma)) {
                mplew.writeInt(0);
            }*/
            if (statups.containsKey(SecondaryStat.BonusAttack)) {
                mplew.writeInt(0);
            }
            if (chr.getBuffedEffect(SecondaryStat.EnergyCharged) != null) {
                mplew.writeInt(chr.energyCharge ? chr.getBuffedEffect(SecondaryStat.EnergyCharged).getSourceId() : 0);
            }
            /*else {
                mplew.writeInt(0);
            }*/
            for (Pair<SecondaryStat, Pair<Integer, Integer>> stat : newstatups) {
                if (!((SecondaryStat) stat.left).canStack() && ((SecondaryStat) stat.left).isSpecialBuff()) {
                    mplew.writeInt(((Integer) ((Pair) stat.right).left).intValue());
                    mplew.writeInt((stat.left == SecondaryStat.EnergyCharged) ? 0 : chr.getBuffSource((SecondaryStat) stat.left));
                    if (stat.left == SecondaryStat.PartyBooster) {
                        mplew.write(1);
                        mplew.writeInt(effect.getStarttime());
                    } else if (stat.left == SecondaryStat.EnergyCharged) {
                        mplew.write(chr.energyCharge);
                    }
                    mplew.write(0);
                    mplew.writeInt(0);
                    if (stat.left == SecondaryStat.GuidedBullet) {
                        mplew.writeInt(chr.guidedBullet);
                        mplew.writeInt(0);
                        continue;
                    }
                    if (stat.left == SecondaryStat.RideVehicleExpire || stat.left == SecondaryStat.PartyBooster || stat.left == SecondaryStat.DashJump || stat.left == SecondaryStat.DashSpeed) {
                        mplew.writeShort(((Integer) ((Pair) stat.right).right).intValue() / 1000);
                        continue;
                    }
                    if (stat.left == SecondaryStat.Grave) {
                        mplew.writeInt(chr.graveObjectId);
                        mplew.writeInt(0);
                    }
                }
            }
            BuffPacket.encodeIndieTempStat(mplew, newstatups, chr);
            if (statups.containsKey(SecondaryStat.NewFlying) && effect.getSourceId() == 80003059) {
                MapleMist m = chr.getMap().getMist(chr.getBuffedOwner(80003059), 162111000);
                mplew.writeInt(m != null ? m.getObjectId() : 0);
            }
            if (statups.containsKey(SecondaryStat.PinkBeanFighting)) {
                mplew.writeInt(1);
            }
            if (statups.containsKey(SecondaryStat.Ember)) {
                mplew.writeInt(chr.getIgnition());
            }
            if (statups.containsKey(SecondaryStat.PickPocket)) {
                mplew.writeInt(chr.getPickPocket().size());
            }
            if (statups.containsKey(SecondaryStat.HolyUnity)) {
                mplew.writeShort(effect.getLevel());
            }
            if (statups.containsKey(SecondaryStat.DemonFrenzy)) {
                mplew.write(chr.getBuffedValue(SecondaryStat.DemonFrenzy));
            }
            if (statups.containsKey(SecondaryStat.ShadowSpear)) {
                mplew.write(0);
            }
            if (statups.containsKey(SecondaryStat.RhoAias)) {
                mplew.writeInt(chr.getBuffedOwner(400011011) == chr.getId() ? chr.getId() : chr.getBuffedOwner(400011011));
                if (chr.getRhoAias() <= effect.getY()) {
                    mplew.writeInt(3);
                } else if (chr.getRhoAias() <= effect.getY() + effect.getW()) {
                    mplew.writeInt(2);
                } else {
                    mplew.writeInt(1);
                }
                mplew.writeInt(chr.getRhoAias());
                if (chr.getRhoAias() <= effect.getY()) {
                    mplew.writeInt(3);
                } else if (chr.getRhoAias() <= effect.getY() + effect.getW()) {
                    mplew.writeInt(2);
                } else {
                    mplew.writeInt(1);
                }
            }
            if (statups.containsKey(SecondaryStat.VampDeath)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.HolyMagicShell)) {
                mplew.writeInt((chr.getSkillLevel(2320045) > 0 ? SkillFactory.getSkill(2320045).getEffect(1).getW() : 0) + effect.getW());
            }
            if (statups.containsKey(SecondaryStat.UsingScouter)) {
                mplew.writeInt(effect.getSourceId());
            }
            if (statups.containsKey(SecondaryStat.KawoongDebuff)) {
                mplew.writeInt(effect.getSourceId());
            }
            if (statups.containsKey(SecondaryStat.GloryWing)) {
                mplew.writeInt(chr.canUseMortalWingBeat ? 1 : 0);
                mplew.writeInt(1);
            }
            if (statups.containsKey(SecondaryStat.BlessMark)) {
                mplew.writeInt(chr.blessMarkSkill);
                switch (chr.blessMarkSkill) {
                    case 152000007: {
                        mplew.writeInt(3);
                        break;
                    }
                    case 152110009: {
                        mplew.writeInt(6);
                        break;
                    }
                    case 152120012: {
                        mplew.writeInt(10);
                    }
                }
            }
            if (statups.containsKey(SecondaryStat.ShadowerDebuff)) {
                mplew.writeInt(chr.shadowerDebuffOid);
            }
            if (statups.containsKey(SecondaryStat.WeaponVariety)) {
                int flag = 0;
                if (chr.getWeaponChanges().contains(64001002)) {
                    ++flag;
                }
                if (chr.getWeaponChanges().contains(64101001)) {
                    flag += 2;
                }
                if (chr.getWeaponChanges().contains(64101002)) {
                    flag += 4;
                }
                if (chr.getWeaponChanges().contains(64111002)) {
                    flag += 8;
                }
                if (chr.getWeaponChanges().contains(64111003)) {
                    flag += 16;
                }
                if (chr.getWeaponChanges().contains(64111012)) {
                    flag += 32;
                }
                if (chr.getWeaponChanges().contains(64121003) || chr.getWeaponChanges().contains(64121011) || chr.getWeaponChanges().contains(64121016)) {
                    flag += 64;
                }
                if (chr.getWeaponChanges().contains(64121021) || chr.getWeaponChanges().contains(64121022) || chr.getWeaponChanges().contains(64121023) || chr.getWeaponChanges().contains(64121024)) {
                    flag += 128;
                }
                mplew.writeInt(flag);
            }
            if (statups.containsKey(SecondaryStat.Overload)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.SpectorGauge)) {
                mplew.writeInt(chr.SpectorGauge);
            }
            if (statups.containsKey(SecondaryStat.PlainBuff)) {
                mplew.writeInt(chr.getSkillCustomValue0(155001100) * 2L);
                mplew.writeInt(chr.getSkillCustomValue0(155001101) * 2L);
            }
            if (statups.containsKey(SecondaryStat.ScarletBuff)) {
                mplew.writeInt(chr.getSkillCustomValue0(155101100));
                mplew.writeInt(chr.getSkillCustomValue0(155101101));
            }
            if (statups.containsKey(SecondaryStat.GustBuff)) {
                mplew.writeInt(chr.getSkillCustomValue0(155111102));
                mplew.writeInt(chr.getSkillCustomValue0(155111103));
            }
            if (statups.containsKey(SecondaryStat.AbyssBuff)) {
                mplew.writeInt(chr.getSkillCustomValue0(155121102));
                mplew.writeInt(chr.getSkillCustomValue0(155121103));
            }
            if (statups.containsKey(SecondaryStat.WillPoison)) {
                mplew.writeInt(30);
            }
            if (statups.containsKey(SecondaryStat.UnkBuffStat29)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.MarkOfPhantomStack)) {
                mplew.writeInt(chr.getMarkofPhantom());
            }
            if (statups.containsKey(SecondaryStat.MarkOfPhantomDebuff)) {
                mplew.writeInt(chr.getMarkOfPhantomOid());
            }
            if (statups.containsKey(SecondaryStat.EventSpecialSkill)) {
                if (effect.getSourceId() == 80003064) {
                    mplew.writeInt(10L - chr.getKeyValue(100857, "feverCnt"));
                } else {
                    mplew.writeInt(0);
                }
            }
            if (statups.containsKey(SecondaryStat.UnkBuffStat46)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.PapyrusOfLuck)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.PmdReduce)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.ForbidEquipChange)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.YalBuff)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.ComboCounter)) {
                mplew.writeInt(chr.getKeyValue(1548, "버프이펙트") == 1L ? 1 : 0);
            }
            if (statups.containsKey(SecondaryStat.Bless5th)) {
                mplew.writeInt(chr.getSkillCustomValue0(400001050));
            }
            if (statups.containsKey(SecondaryStat.AncientGuidance)) {
                mplew.writeInt(effect != null ? 3000 : 0);
                mplew.writeInt(chr.에인션트가이던스);
            }
            if (statups.containsKey(SecondaryStat.BattlePvP_Wonky_ChargeA)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.BattlePvP_Wonky_Awesome)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.HolySymbol)) {
                mplew.writeInt(chr.getId());
                mplew.writeInt(effect.getLevel());
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.write(chr.getSkillCustomValue0(2311004) == 0L ? 1 : 0);
                mplew.write(0);
                mplew.writeInt(chr.getSkillCustomValue0(2311004) == 0L ? (int) chr.getSkillCustomValue0(2320048) : 0);
            }
            if (statups.containsKey(SecondaryStat.UnkBuffStat50)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.HoyoungThirdProperty)) {
                mplew.writeInt(chr.useJi ? 1 : 0);
                mplew.writeInt(chr.useIn ? 1 : 0);
            }
            if (statups.containsKey(SecondaryStat.TidalForce)) {
                mplew.writeInt(chr.scrollGauge);
            }
            if (statups.containsKey(SecondaryStat.SageWrathOfGods)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.EmpiricalKnowledge)) {
                mplew.writeInt(chr.empiricalKnowledge == null ? 0 : chr.empiricalKnowledge.getObjectId());
            }
            if (statups.containsKey(SecondaryStat.Graffiti)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.Novility)) {
                mplew.writeInt(chr.getSkillCustomValue0(151111005));
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.Revenant)) {
                mplew.writeInt(chr.getSkillCustomValue0(400011112));
            }
            if (statups.containsKey(SecondaryStat.RevenantDamage)) {
                mplew.writeInt(chr.getSkillCustomValue0(400011112) + chr.getStat().getCurrentMaxHp() / 100L * 25L);
                mplew.writeInt(chr.getSkillCustomValue0(400011129));
            }
            if (statups.containsKey(SecondaryStat.SilhouetteMirage)) {
                mplew.writeInt(chr.getSkillCustomValue0(400031053));
            }
            if (statups.containsKey(SecondaryStat.RuneOfPure)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.BlessOfDarkness)) {
                mplew.writeInt(chr.getBlessofDarkness());
            }
            if (statups.containsKey(SecondaryStat.YellowAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(32001016));
                mplew.writeInt(chr.getSkillCustomValue0(32001016) != (long) chr.getId() ? 0 : 1);
            }
            if (statups.containsKey(SecondaryStat.DrainAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(32101009));
                mplew.writeInt(chr.getSkillCustomValue0(32101009) != (long) chr.getId() ? 0 : 1);
            }
            if (statups.containsKey(SecondaryStat.BlueAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(32111012));
                mplew.writeInt(chr.getSkillCustomValue0(32111012) != (long) chr.getId() ? 0 : 1);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.DarkAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(32121017));
                mplew.writeInt(chr.getSkillCustomValue0(32121017) != (long) chr.getId() ? 0 : 1);
            }
            if (statups.containsKey(SecondaryStat.DebuffAura)) {
                mplew.writeInt(chr.getId());
                mplew.writeInt(1);
            }
            if (statups.containsKey(SecondaryStat.UnionAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(400021006));
                mplew.writeInt(chr.getSkillCustomValue0(400021006) != (long) chr.getId() ? 0 : 1);
            }
            if (statups.containsKey(SecondaryStat.IceAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(2221054));
                mplew.writeInt(chr.getSkillCustomValue0(2221054) != (long) chr.getId() ? 0 : 1);
            }
            if (statups.containsKey(SecondaryStat.KnightsAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(1211014));
                mplew.writeInt(chr.getSkillCustomValue0(1211014) != (long) chr.getId() ? 0 : 1);
            }
            if (statups.containsKey(SecondaryStat.ZeroAuraStr)) {
                mplew.writeInt(chr.getId());
                mplew.writeInt(1);
            }
            if (statups.containsKey(SecondaryStat.ZeroAuraSpd)) {
                mplew.writeInt(chr.getId());
                mplew.writeInt(1);
            }
            if (statups.containsKey(SecondaryStat.IncarnationAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(63121044));
                mplew.writeInt(chr.getSkillCustomValue0(63121044) != (long) chr.getId() ? 0 : 1);
            }
            if (statups.containsKey(SecondaryStat.BlizzardTempest)) {
                mplew.writeInt(1);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.PhotonRay)) {
                mplew.writeInt(chr.photonRay);
            }
            if (statups.containsKey(SecondaryStat.AbyssalLightning)) {
                try {
                    mplew.writeInt(chr.getMap().SpecialPortalSize(chr.getId()).size());
                } catch (Exception e) {
                    mplew.writeInt(0);
                }
            }
            if (statups.containsKey(SecondaryStat.Striker4th)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.LawOfGravity)) {
                mplew.writeInt(chr.lawOfGravity);
            }
            if (statups.containsKey(SecondaryStat.CrystalGate)) {
                mplew.writeInt(chr.getSkillCustomValue0(400021099));
            }
            if (statups.containsKey(SecondaryStat.WeaponVarietyFinale)) {
                mplew.writeInt(chr.getSkillCustomValue0(64121020));
            }
            if (statups.containsKey(SecondaryStat.LiberationOrb)) {
                mplew.writeInt(chr.getSkillCustomValue0(400021107));
                mplew.writeInt(chr.getSkillCustomValue0(400021108));
            }
            if (statups.containsKey(SecondaryStat.DarknessAura)) {
                mplew.writeInt(chr.getSkillCustomValue0(400011047));
            }
            if (statups.containsKey(SecondaryStat.ThanatosDescent)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.DragonPang)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.LuckOfUnion) && effect.getSourceId() == 135001009) {
                mplew.write(1);
            }
            if (statups.containsKey(SecondaryStat.YetiAnger)) {
                mplew.writeInt(chr.getSkillCustomValue0(135001005));
            }
            if (statups.containsKey(SecondaryStat.YetiAngerMode)) {
                mplew.writeInt(chr.getSkillCustomValue0(13500));
            }
            if (statups.containsKey(SecondaryStat.BattlePvP_Rude_Stack)) {
                mplew.writeInt(1);
            }
            if (statups.containsKey(SecondaryStat.Stance) && chr.getBuffedValue(1121054)) {
                mplew.writeInt(chr.발할라검격);
            }
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            if (statups.containsKey(SecondaryStat.LuckOfUnion) && effect.getSourceId() == 135001009) {
                mplew.writeInt(chr.getId());
            }
            boolean unk = false;
            if (statups.containsKey(SecondaryStat.Etherealform) || statups.containsKey(SecondaryStat.Transform) || statups.containsKey(SecondaryStat.IndieJointAttack) || statups.containsKey(SecondaryStat.SwordOfSoulLight) || statups.containsKey(SecondaryStat.RideVehicle) || statups.containsKey(SecondaryStat.SpreadThrow) || statups.containsKey(SecondaryStat.Dike) || statups.containsKey(SecondaryStat.IceAura) || statups.containsKey(SecondaryStat.Novility) || statups.containsKey(SecondaryStat.ThrowBlasting) || statups.containsKey(SecondaryStat.DarknessAura) || statups.containsKey(SecondaryStat.RoyalKnights) || statups.containsKey(SecondaryStat.AbyssalLightning) || statups.containsKey(SecondaryStat.PhotonRay) || statups.containsKey(SecondaryStat.ThanatosDescent) || statups.containsKey(SecondaryStat.DragonPang)) {
                unk = true;
            }
            mplew.write(unk);
            mplew.write(true);
            mplew.write(true);
            for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> stat : statups.entrySet()) {
                if (!GameConstants.MovementAffectingStat(stat.getKey())) {
                    continue;
                }
                mplew.write(statups.containsKey(SecondaryStat.NewFlying) && effect.getSourceId() == 80003059 ? 76 : (statups.containsKey(SecondaryStat.RWCylinder) ? 0 : (effect.getSourceId() == 35111003 ? 16 : (effect.getSourceId() == 35001002 ? 6 : 1))));
                break;
            }
            mplew.writeInt(0);
            if (statups.containsKey(SecondaryStat.NewFlying) && effect.getSourceId() == 80003059) {
                mplew.write(16);
            }
            //System.err.println("기브버프 : " + mplew.toString());
            mplew.writeZeroBytes(100);
            return mplew.getPacket();
        }//mush

        public static byte[] giveDisease(Map<SecondaryStat, Pair<Integer, Integer>> statups, MobSkill skill, MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            //mplew.writeInt(0); // 361 --
            //mplew.writeInt(0); // 361 --
            List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups = PacketHelper.sortBuffStats(statups);
            PacketHelper.writeBuffMask(mplew, newstatups);
            for (Pair<SecondaryStat, Pair<Integer, Integer>> pair : newstatups) {
                if (pair.getLeft().canStack() || pair.getLeft().isSpecialBuff()) {
                    continue;
                }
                if (SecondaryStat.isEncode4Byte(statups)) {
                    if (pair.left == SecondaryStat.ReturnTeleport) {
                        mplew.writeShort(chr.getPosition().y);
                        mplew.writeShort(chr.getPosition().x);
                    } else {
                        mplew.writeInt(pair.getLeft().getX() != 0 ? pair.getLeft().getX() : ((Integer) pair.getRight().left).intValue());
                    }
                } else {
                    mplew.writeShort((Integer) pair.getRight().left);
                }
                mplew.writeShort(skill.getSkillId());
                mplew.writeShort(skill.getSkillId() == 237 ? 0 : skill.getSkillLevel());
                mplew.writeInt(skill.getSkillId() == 237 ? 0 : (Integer) ((Pair) pair.right).right);
            }
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            mplew.writeInt(0);
            if (statups.containsKey(SecondaryStat.Slow)) {
                mplew.write(0);
            }
            if (statups.containsKey(SecondaryStat.CurseOfCreation)) {
                mplew.writeInt(10);
            }
            if (statups.containsKey(SecondaryStat.CurseOfDestruction)) {
                mplew.writeInt(15);
            }
            if (statups.containsKey(SecondaryStat.Stigma)) {
                mplew.writeInt(7);
            }
            mplew.writeInt(0);
            BuffPacket.encodeIndieTempStat(mplew, newstatups, chr);
            for (Pair<SecondaryStat, Pair<Integer, Integer>> pair : newstatups) {
                if (((SecondaryStat) pair.left).canStack() || !((SecondaryStat) pair.left).isSpecialBuff()) {
                    continue;
                }
                mplew.writeInt((Integer) ((Pair) pair.right).left);
                mplew.writeShort(skill.getSkillId());
                mplew.writeShort(skill.getSkillId() == 237 ? 0 : skill.getSkillLevel());
                mplew.write(0);
                mplew.writeInt(0);
                mplew.writeShort((Integer) ((Pair) pair.right).right);
            }
            if (statups.containsKey(SecondaryStat.VampDeath)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(SecondaryStat.KawoongDebuff)) {
                mplew.writeInt(1000);
            }
            if (statups.containsKey(SecondaryStat.WillPoison)) {
                mplew.writeInt(30);
            }
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(true);
            mplew.write(true);
            for (Map.Entry entry : statups.entrySet()) {
                if (!GameConstants.MovementAffectingStat((SecondaryStat) entry.getKey())) {
                    continue;
                }
                mplew.write(0);
                break;
            }
            mplew.writeInt(0);
            mplew.writeZeroBytes(100);
            return mplew.getPacket();
        }

        public static void encodeIndieTempStat(MaplePacketLittleEndianWriter mplew, List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups, MapleCharacter chr) {
            HashMap<SecondaryStat, List<SecondaryStatValueHolder>> indiestats = new HashMap<SecondaryStat, List<SecondaryStatValueHolder>>();
            newstatups.stream().filter(statup -> ((SecondaryStat) statup.getLeft()).canStack()).map(statup -> {
                indiestats.put(statup.getLeft(), new ArrayList<>());
                return statup;
            }).forEach(statup -> {
                Iterator<Pair<SecondaryStat, SecondaryStatValueHolder>> effects = chr.getEffects().iterator();

                while (effects.hasNext()) {
                    Pair<SecondaryStat, SecondaryStatValueHolder> effect = effects.next();
                    if (statup.getLeft() == effect.left) {
                        indiestats.get(statup.getLeft()).add(effect.right);
                    }
                }
            });
            List<Pair<SecondaryStat, List<SecondaryStatValueHolder>>> indiestatz = PacketHelper.sortIndieBuffStats(indiestats);
            for (Pair<SecondaryStat, List<SecondaryStatValueHolder>> indiestat : indiestatz) {
                mplew.writeInt(indiestat.getRight().size());
                for (SecondaryStatValueHolder indie : indiestat.getRight()) {
                    int sourceid = 0;
                    if (indie.effect.getSourceId() == 400011127) {
                        if (indiestat.getLeft() == SecondaryStat.IndieDamR) {
                            sourceid = 400011128;
                            indie.list1.clear();
                        } else {
                            indie.list1.add(new Pair<Integer, Integer>(1, 2));
                            indie.list1.add(new Pair<Integer, Integer>(2, 8738));
                            indie.list1.add(new Pair<Integer, Integer>(3, 3520));
                        }
                    }
                    if (indiestat.getLeft().isItemEffect() || !indie.effect.isSkill() || SkillFactory.getSkill(indie.effect.getSourceId()) == null) {
                        mplew.writeInt(-indie.effect.getSourceId());
                    } else {
                        mplew.writeInt(sourceid > 0 ? sourceid : indie.effect.getSourceId());
                    }
                    mplew.writeInt(indie.value);
                    mplew.writeInt(indie.startTime);
                    mplew.writeInt(indiestat.getLeft() == SecondaryStat.IndieJointAttack ? indie.value : 0);
                    mplew.writeInt(indie.localDuration);
                    mplew.writeInt(0);
                    mplew.writeInt(indie.list1.size());
                    for (Pair<Integer, Integer> list : indie.list1) {
                        mplew.writeInt((Integer) list.left);
                        mplew.writeInt((Integer) list.right);
                    }
                    mplew.writeInt(indie.list2.size());
                    for (Pair<Integer, Integer> list : indie.list2) {
                        mplew.writeInt((Integer) list.left);
                        mplew.writeInt((Integer) list.right);
                    }
                }
            }
        }

        private static void giveDice(MaplePacketLittleEndianWriter mplew, SecondaryStatEffect effect, MapleCharacter chr) {
            int doubledice, dice, thirddice;
            if (chr.getDice() >= 100) {
                thirddice = chr.getDice() / 100;
                doubledice = (chr.getDice() - thirddice * 100) / 10;
                dice = chr.getDice() - chr.getDice() / 10 * 10;
            } else {
                thirddice = 1;
                doubledice = chr.getDice() / 10;
                dice = chr.getDice() - doubledice * 10;
            }
            if (dice == 3 || doubledice == 3 || thirddice == 3) {
                if (dice == 3 && doubledice == 3 && thirddice == 3) {
                    mplew.writeInt(effect.getPercentHP() + 15);
                    mplew.writeInt(effect.getPercentMP() + 15);
                } else if ((dice == 3 && doubledice == 3) || (dice == 3 && thirddice == 3) || (thirddice == 3 && doubledice == 3)) {
                    mplew.writeInt(effect.getPercentHP() + 10);
                    mplew.writeInt(effect.getPercentMP() + 10);
                } else {
                    mplew.writeInt(effect.getPercentHP());
                    mplew.writeInt(effect.getPercentMP());
                }
            } else {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (dice == 4 || doubledice == 4 || thirddice == 4) {
                if (dice == 4 && doubledice == 4 && thirddice == 4) {
                    mplew.writeInt(effect.getCr() + 15);
                } else if ((dice == 4 && doubledice == 4) || (dice == 4 && thirddice == 4) || (thirddice == 4 && doubledice == 4)) {
                    mplew.writeInt(effect.getCr() + 10);
                } else {
                    mplew.writeInt(effect.getCr());
                }
            } else {
                mplew.writeInt(0);
            }
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            if (dice == 2 || doubledice == 2 || thirddice == 2) {
                if (dice == 2 && doubledice == 2 && thirddice == 2) {
                    mplew.writeInt(effect.getWDEFRate() + 15);
                } else if ((dice == 2 && doubledice == 2) || (dice == 2 && thirddice == 2) || (thirddice == 2 && doubledice == 2)) {
                    mplew.writeInt(effect.getWDEFRate() + 10);
                } else {
                    mplew.writeInt(effect.getWDEFRate());
                }
            } else {
                mplew.writeInt(0);
            }
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            if (dice == 5 || doubledice == 5 || thirddice == 5) {
                if (dice == 5 && doubledice == 5 && thirddice == 5) {
                    mplew.writeInt(effect.getDAMRate() + 15);
                } else if ((dice == 5 && doubledice == 5) || (dice == 5 && thirddice == 5) || (thirddice == 5 && doubledice == 5)) {
                    mplew.writeInt(effect.getDAMRate() + 10);
                } else {
                    mplew.writeInt(effect.getDAMRate());
                }
            } else {
                mplew.writeInt(0);
            }
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            if (dice == 6 || doubledice == 6 || thirddice == 6) {
                if (dice == 6 && doubledice == 6 && thirddice == 6) {
                    mplew.writeInt(effect.getEXPRate() + 15);
                } else if ((dice == 6 && doubledice == 6) || (dice == 6 && thirddice == 6) || (thirddice == 6 && doubledice == 6)) {
                    mplew.writeInt(effect.getEXPRate() + 10);
                } else {
                    mplew.writeInt(effect.getEXPRate());
                }
            } else {
                mplew.writeInt(0);
            }
            if (dice == 7 || doubledice == 7 || thirddice == 7) {
                if (dice == 7 && doubledice == 7 && thirddice == 7) {
                    mplew.writeInt(effect.getIgnoreMob() + 15);
                } else if ((dice == 7 && doubledice == 7) || (dice == 7 && thirddice == 7) || (thirddice == 7 && doubledice == 7)) {
                    mplew.writeInt(effect.getIgnoreMob() + 10);
                } else {
                    mplew.writeInt(effect.getIgnoreMob());
                }
            } else {
                mplew.writeInt(0);
            }
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }

        public static byte[] cancelBuff(Map<SecondaryStat, Pair<Integer, Integer>> statups, MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());
            // mplew.writeInt(0); // 361
            mplew.write(true);
            mplew.write(true);
            mplew.write(10); // 361 ++
            List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups = PacketHelper.sortBuffStats(statups);
            PacketHelper.writeBuffMask(mplew, newstatups);
            BuffPacket.encodeIndieTempStat(mplew, newstatups, chr);
            for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> stat : statups.entrySet()) {
                if (!GameConstants.MovementAffectingStat(stat.getKey())) {
                    continue;
                }
                mplew.write(1);
                break;
            }
            mplew.write(statups.containsKey(SecondaryStat.Transform) || statups.containsKey(SecondaryStat.IndieJointAttack) || statups.containsKey(SecondaryStat.RideVehicle) || statups.containsKey(SecondaryStat.SpreadThrow) || statups.containsKey(SecondaryStat.PoseType));
            mplew.writeInt(0);
            return mplew.getPacket();
        }

        public static byte[] giveForeignBuff(MapleCharacter chr, Map<SecondaryStat, Pair<Integer, Integer>> statups, SecondaryStatEffect effect) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
            mplew.writeInt(chr.getId());
            List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups = PacketHelper.sortBuffStats(statups);
            PacketHelper.writeBuffMask(mplew, newstatups);
            PacketHelper.encodeForRemote(mplew, statups, chr);
            mplew.writeShort(0);
            mplew.write((statups.containsKey(SecondaryStat.Larkness) || statups.containsKey(SecondaryStat.Transform) || statups.containsKey(SecondaryStat.BlessedHammer) || statups.containsKey(SecondaryStat.IceAura)));
            mplew.writeLong(0);
            return mplew.getPacket();
        }

        public static byte[] giveForeignDeBuff(MapleCharacter chr, Map<SecondaryStat, Pair<Integer, Integer>> statups) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
            mplew.writeInt(chr.getId());
            List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups = PacketHelper.sortBuffStats(statups);
            PacketHelper.writeBuffMask(mplew, newstatups);
            PacketHelper.encodeForRemote(mplew, statups, chr);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.writeLong(0L);
            mplew.writeLong(0L);
            mplew.writeLong(0L);
            mplew.writeLong(0L);
            return mplew.getPacket();
        }

        public static byte[] cancelForeignBuff(MapleCharacter chr, Map<SecondaryStat, Pair<Integer, Integer>> statups) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.CANCEL_FOREIGN_BUFF.getValue());
            mplew.writeInt(chr.getId());
            List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups = PacketHelper.sortBuffStats(statups);
            PacketHelper.writeBuffMask(mplew, newstatups);
            encodeIndieTempStat(mplew, newstatups, chr);
            if (statups.containsKey(SecondaryStat.PoseType)) {
                mplew.write(true);
            }
            if (statups.containsKey(SecondaryStat.UnkBuffStat42)) {
                mplew.write(true);
            }
            mplew.write((statups.containsKey(SecondaryStat.PoseType) || statups.containsKey(SecondaryStat.BlessedHammer) || statups.containsKey(SecondaryStat.RideVehicle)));
            return mplew.getPacket();
        }
    }

    public static class InfoPacket {

        public static byte[] showMesoGain(long gain, boolean pet, boolean inChat) {
            /* 1605 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1607 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1608 */ mplew.write(0);
            /* 1609 */ mplew.write(pet);
            /* 1610 */ mplew.write(1);
            /* 1611 */ mplew.write(0);
            /* 1612 */ mplew.writeInt(gain);
            /* 1613 */ mplew.writeShort(0);

            /* 1615 */ return mplew.getPacket();
        }

        public static byte[] showMesoGain(long gain, boolean inChat, int jan) {
            /* 1619 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1621 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1622 */ if (!inChat) {
                /* 1623 */ mplew.write(0);
                /* 1624 */ mplew.write(0);
                /* 1625 */ mplew.write(1);
                /* 1626 */ mplew.write(0);
                /* 1627 */ mplew.writeInt(gain);
                /* 1628 */ mplew.writeShort(jan);
                /* 1629 */ mplew.writeShort(0);
            } else {
                /* 1631 */ mplew.write(6);
                /* 1632 */ if (gain > 0L) {
                    /* 1633 */ mplew.writeInt(gain);
                    /* 1634 */ mplew.writeInt(0);
                    /* 1635 */ mplew.writeInt(-1);
                    /* 1636 */ mplew.writeInt(0);
                } else {
                    /* 1638 */ mplew.writeInt(gain);
                    /* 1639 */ mplew.writeInt(-1);
                    /* 1640 */ mplew.writeInt(-1);
                    /* 1641 */ mplew.writeInt(-1);
                }
            }

            /* 1646 */ return mplew.getPacket();
        }

        public static byte[] getShowInventoryStatus(int mode) {
            /* 1650 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1652 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1653 */ mplew.write(0);
            /* 1654 */ mplew.write(0);
            /* 1655 */ mplew.write(mode);
            /* 1656 */ mplew.writeInt(0);
            /* 1657 */ mplew.writeInt(0);
            /* 1658 */ mplew.writeInt(0);

            /* 1660 */ return mplew.getPacket();
        }

        public static byte[] getShowItemGain(int itemId, short quantity, boolean inChat) {
            /* 1664 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1666 */ if (inChat) {
                /* 1667 */ mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
                /* 1668 */ mplew.write(8);
                /* 1669 */ mplew.write(1);
                /* 1670 */ mplew.writeInt(itemId);
                /* 1671 */ mplew.writeInt(quantity);
                /* 1672 */ mplew.write(0);
            } else {
                /* 1674 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
                /* 1675 */ mplew.writeShort(0);
                /* 1676 */ mplew.write(0);
                /* 1677 */ mplew.writeInt(itemId);
                /* 1678 */ mplew.writeInt(quantity);
                /* 1679 */ mplew.write(0);
            }

            /* 1682 */ return mplew.getPacket();
        }

        public static byte[] updateQuest(MapleQuestStatus quest) {
            /* 1686 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1688 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1689 */ mplew.write(1);
            /* 1690 */ mplew.writeInt(quest.getQuest().getId());
            /* 1691 */ mplew.write(quest.getStatus());
            /* 1692 */ switch (quest.getStatus()) {
                case 0:
                    /* 1694 */ mplew.write(0);
                    break;
                case 1:
                    /* 1697 */ mplew.writeMapleAsciiString((quest.getCustomData() != null) ? quest.getCustomData() : "");
                    break;
                case 2:
                    /* 1700 */ mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
                    break;
            }
            /* 1703 */ return mplew.getPacket();
        }

        public static byte[] updateQuestInfo(int status, int type, int questid) {
            /* 1707 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1709 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1710 */ mplew.write(status);
            /* 1711 */ mplew.writeInt(questid);
            /* 1712 */ mplew.write(type);
            /* 1713 */ mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));

            /* 1715 */ return mplew.getPacket();
        }

        public static byte[] AcceptQuest(int questid, int npcid) {
            /* 1719 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1721 */ mplew.writeShort(SendPacketOpcode.ACCEPT_QUEST.getValue());
            /* 1722 */ mplew.write(11);
            /* 1723 */ mplew.writeInt(questid);
            /* 1724 */ mplew.writeInt(npcid);
            /* 1725 */ mplew.writeInt(0);
            /* 1726 */ mplew.write(0);

            /* 1728 */ return mplew.getPacket();
        }

        public static byte[] updateQuestMobKills(MapleQuestStatus status) {
            /* 1732 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1734 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1735 */ mplew.write(1);
            /* 1736 */ mplew.writeInt(status.getQuest().getId());
            /* 1737 */ mplew.write(1);
            /* 1738 */ StringBuilder sb = new StringBuilder();
            /* 1739 */ for (Iterator<Integer> iterator = status.getMobKills().values().iterator(); iterator.hasNext();) {
                int kills = ((Integer) iterator.next()).intValue();
                /* 1740 */ sb.append(StringUtil.getLeftPaddedStr(String.valueOf(kills), '0', 3));
            }

            /* 1742 */ mplew.writeMapleAsciiString(sb.toString());
            /* 1743 */ mplew.writeLong(0L);

            /* 1745 */ return mplew.getPacket();
        }

        public static byte[] itemExpired(int itemid) {
            /* 1749 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1751 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1752 */ mplew.write(2);
            /* 1753 */ mplew.writeInt(itemid);

            /* 1755 */ return mplew.getPacket();
        }

        public static byte[] GainEXP_Monster(MapleCharacter chr, long gain, boolean white, long flag, int eventBonusExp, int weddingExp, int partyExp, int itemEquipExp, int pcExp, int rainbowWeekExp, int boomupExp, int portionExp, int skillExp, int buffExp, int restExp, int itemExp, int valueExp, int bonusExp, int bloodExp, int iceExp, Pair<Integer, Integer> burningExp, int hpLiskExp, int fieldBonusExp, int eventBonusExp2, int fieldBonusExp2) {
            /* 1759 */ MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
            /* 1760 */ packet.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1761 */ packet.write(3);
            /* 1762 */ packet.write(white ? 1 : 0);
            /* 1763 */ packet.writeLong(gain);

            /* 1765 */ packet.write(0);
            packet.writeInt(1);
            /* 1767 */ packet.writeLong(0);
            /* 1769 */ packet.writeLong(flag);

            /* 1771 */ if ((flag & 0x1L) != 0L) {
                /* 1772 */ packet.writeInt(eventBonusExp);
            }

            /* 1775 */ if ((flag & 0x4L) != 0L) {
                /* 1776 */ byte a = 1;
                /* 1777 */ packet.write(a);
                /* 1778 */ if (a > 0) {
                    /* 1779 */ packet.write(2);
                }
            }

            /* 1783 */ if ((flag & 0x10L) != 0L) {
                /* 1784 */ packet.writeInt(weddingExp);
            }
            /* 1786 */ if ((flag & 0x20L) != 0L) {
                /* 1787 */ packet.writeInt(partyExp);
            }
            /* 1789 */ if ((flag & 0x40L) != 0L) {
                /* 1790 */ packet.writeInt(itemEquipExp);
            }
            /* 1792 */ if ((flag & 0x80L) != 0L) {
                /* 1793 */ packet.writeInt(pcExp);
            }
            /* 1795 */ if ((flag & 0x100L) != 0L) {
                /* 1796 */ packet.writeInt(rainbowWeekExp);
            }
            /* 1798 */ if ((flag & 0x200L) != 0L) {
                /* 1799 */ packet.writeInt(boomupExp);
            }
            /* 1801 */ if ((flag & 0x400L) != 0L) {
                /* 1802 */ packet.writeInt(portionExp);
            }
            /* 1804 */ if ((flag & 0x800L) != 0L) {
                /* 1805 */ packet.writeInt(skillExp);
            }
            /* 1807 */ if ((flag & 0x1000L) != 0L) {
                /* 1808 */ packet.writeInt(buffExp);
            }
            /* 1810 */ if ((flag & 0x2000L) != 0L) {
                /* 1811 */ packet.writeInt(restExp);
            }
            /* 1813 */ if ((flag & 0x4000L) != 0L) {
                /* 1814 */ packet.writeInt(itemExp);
            }
            /* 1816 */ if ((flag & 0x10000L) != 0L) {
                /* 1817 */ packet.writeInt(14);
            }
            /* 1819 */ if ((flag & 0x20000L) != 0L) {
                /* 1820 */ packet.writeInt(valueExp);
            }
            /* 1822 */ if ((flag & 0x40000L) != 0L) {
                /* 1823 */ packet.writeInt(16);
            }
            /* 1825 */ if ((flag & 0x80000L) != 0L) {
                /* 1826 */ packet.writeInt(bonusExp);
            }
            /* 1828 */ if ((flag & 0x100000L) != 0L) {
                /* 1829 */ packet.writeInt(bloodExp);
            }
            /* 1831 */ if ((flag & 0x200000L) != 0L) {
                /* 1832 */ packet.writeInt(iceExp);
            }
            /* 1834 */ if ((flag & 0x400000L) != 0L) {
                /* 1835 */ packet.writeInt(((Integer) burningExp.left).intValue());
                /* 1836 */ packet.writeInt(((Integer) burningExp.right).intValue());
            }
            /* 1838 */ if ((flag & 0x800000L) != 0L) {
                /* 1839 */ packet.writeInt(hpLiskExp);
            }
            /* 1841 */ if ((flag & 0x1000000L) != 0L) {
                /* 1842 */ packet.writeInt(fieldBonusExp);
            }
            /* 1844 */ if ((flag & 0x2000000L) != 0L) {
                /* 1845 */ packet.writeInt(23);
            }
            /* 1847 */ if ((flag & 0x4000000L) != 0L) {
                /* 1848 */ packet.writeInt(eventBonusExp2);
            }
            /* 1850 */ if ((flag & 0x8000000L) != 0L) {
                /* 1851 */ packet.writeInt(25);
            }
            /* 1853 */ if ((flag & 0x10000000L) != 0L) {
                /* 1854 */ packet.writeInt(fieldBonusExp2);
            }
            /* 1856 */ if ((flag & 0x20000000L) != 0L) {
                /* 1857 */ packet.writeInt(27);
            }
            /* 1859 */ if ((flag & 0x40000000L) != 0L) {
                /* 1860 */ packet.writeInt(28);
            }
            /* 1862 */ packet.write(0);
            /* 1863 */ return packet.getPacket();
        }

        public static byte[] GainEXP_Others(long gain, boolean inChat, boolean white) {
            /* 1867 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1869 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1870 */ mplew.write(3);
            /* 1871 */ mplew.write(white ? 1 : 0);
            /* 1872 */ mplew.writeLong(gain);
            /* 1873 */ mplew.write(0);
            /* 1874 */ mplew.writeLong(0L);
            /* 1875 */ mplew.writeInt(0);
            /* 1876 */ mplew.write(0);
            /* 1877 */ mplew.write(0);
            /* 1878 */ mplew.write(0);
            /* 1879 */ mplew.writeInt(0);
            /* 1880 */ mplew.writeInt(0);

            /* 1882 */ mplew.writeInt(0);
            /* 1883 */ mplew.writeInt(0);
            /* 1884 */ mplew.writeInt(0);
            /* 1885 */ mplew.writeInt(0);
            /* 1886 */ mplew.writeInt(0);
            /* 1887 */ mplew.writeInt(0);
            /* 1888 */ mplew.writeInt(0);
            /* 1889 */ mplew.writeInt(0);
            /* 1890 */ mplew.writeInt(0);
            /* 1891 */ mplew.writeInt(0);
            /* 1892 */ mplew.writeInt(0);
            /* 1893 */ mplew.writeInt(0);
            /* 1894 */ mplew.writeInt(0);
            /* 1895 */ mplew.writeInt(0);
            /* 1896 */ mplew.writeInt(0);
            /* 1897 */ mplew.writeInt(0);
            /* 1898 */ mplew.writeInt(0);
            /* 1899 */ mplew.writeInt(0);
            /* 1900 */ mplew.writeInt(0);
            /* 1901 */ mplew.writeInt(0);

            /* 1903 */ return mplew.getPacket();
        }

        public static byte[] getSPMsg(byte sp, short job) {
            /* 1907 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1909 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1910 */ mplew.write(4);
            /* 1911 */ mplew.writeShort(job);
            /* 1912 */ mplew.write(sp);

            /* 1914 */ return mplew.getPacket();
        }

        public static byte[] getShowFameGain(int gain) {
            /* 1918 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1920 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1921 */ mplew.write(5);
            /* 1922 */ mplew.writeInt(gain);

            /* 1924 */ return mplew.getPacket();
        }

        public static byte[] getGPMsg(int itemid) {
            /* 1928 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1930 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1931 */ mplew.write(9);
            /* 1932 */ mplew.writeInt(itemid);
            /* 1933 */ mplew.writeInt(itemid);
            /* 1934 */ mplew.writeInt(itemid);

            /* 1936 */ return mplew.getPacket();
        }

        public static byte[] getGPContribution(int itemid) {
            /* 1940 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1942 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1943 */ mplew.write(8);
            /* 1944 */ mplew.writeInt(itemid);
            /* 1945 */ mplew.writeInt(itemid);
            /* 1946 */ mplew.writeInt(itemid);

            /* 1948 */ return mplew.getPacket();
        }

        public static byte[] getStatusMsg(int itemid) {
            /* 1952 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1954 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1955 */ mplew.write(9);
            /* 1956 */ mplew.writeInt(itemid);

            /* 1958 */ return mplew.getPacket();
        }

        public static byte[] updateInfoQuest(int quest, String data) {
            /* 1962 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1964 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1965 */ mplew.write(13);
            /* 1966 */ mplew.writeInt(quest);
            /* 1967 */ mplew.writeMapleAsciiString(data);

            /* 1969 */ return mplew.getPacket();
        }

        public static byte[] updateClientInfoQuest(int quest, String data) {
            /* 1973 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1975 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1976 */ mplew.write(14);
            /* 1977 */ mplew.writeInt(quest);
            /* 1978 */ mplew.writeMapleAsciiString(data);

            /* 1980 */ return mplew.getPacket();
        }

        public static byte[] showItemReplaceMessage(List<String> message) {
            /* 1984 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1986 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 1987 */ mplew.write(16);
            /* 1988 */ mplew.write(message.size());
            /* 1989 */ for (String x : message) {
                /* 1990 */ mplew.writeMapleAsciiString(x);
            }

            /* 1993 */ return mplew.getPacket();
        }

        public static byte[] showTraitGain(MapleTrait.MapleTraitType trait, int amount) {
            /* 1997 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1999 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 2000 */ mplew.write(19);
            /* 2001 */ mplew.writeInt(trait.getStat().getValue());
            /* 2002 */ mplew.writeInt(amount);

            /* 2004 */ return mplew.getPacket();
        }

        public static byte[] showPetSkills(int pet, String data) {
            /* 1997 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1999 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(13);
            mplew.write(216 + pet);
            mplew.write(138);
            mplew.writeShort(1);
            mplew.writeMapleAsciiString(data);
            /* 2004 */ return mplew.getPacket();
        }

        public static byte[] showTraitMaxed(MapleTrait.MapleTraitType trait) {
            /* 2008 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2010 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 2011 */ mplew.write(19);
            /* 2012 */ mplew.writeLong(trait.getStat().getValue());

            /* 2014 */ return mplew.getPacket();
        }

        public static byte[] getBPMsg(int amount) {
            /* 2018 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2020 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 2021 */ mplew.write(20);
            /* 2022 */ mplew.writeInt(amount);
            /* 2023 */ mplew.writeInt(0);
            /* 2024 */ mplew.writeInt(0);

            /* 2026 */ return mplew.getPacket();
        }

        public static final byte[] comboKill(int combo, int monster) {
            /* 2030 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2031 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 2032 */ mplew.write(38);
            /* 2033 */ mplew.write(1);
            /* 2034 */ mplew.writeInt(combo + 1);
            /* 2035 */ mplew.writeInt(monster);
            /* 2036 */ mplew.writeInt(0);
            /* 2037 */ mplew.writeInt(combo);
            /* 2038 */ return mplew.getPacket();
        }

        public static byte[] multiKill(int count, long exp) {
            /* 2042 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2043 */ mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            /* 2044 */ mplew.write(38);
            /* 2045 */ mplew.write(0);
            /* 2046 */ mplew.writeLong(exp);
            /* 2047 */ mplew.writeInt(count);
            /* 2048 */ mplew.writeInt(count);
            /* 2049 */ mplew.writeInt(2);
            /* 2050 */ return mplew.getPacket();
        }

    }

    public static class GuildPacket {

        public static byte[] changeCustomGuildEmblem(MapleCharacter chr, byte[] imgdata) {
            /* 2059 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2060 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2061 */ mplew.write(135);
            /* 2062 */ mplew.writeInt(chr.getGuildId());
            /* 2063 */ mplew.writeInt(chr.getId());
            /* 2064 */ mplew.write(1);

            /* 2066 */ mplew.writeShort(0);
            /* 2067 */ mplew.write(0);
            /* 2068 */ mplew.writeShort(0);
            /* 2069 */ mplew.write(0);

            /* 2071 */ writeImageData(mplew, imgdata);
            /* 2072 */ mplew.writeInt(1);
            /* 2073 */ return mplew.getPacket();
        }

        public static void writeImageData(MaplePacketLittleEndianWriter mplew, byte[] imgdata) {
            /* 2077 */ int size = imgdata.length;
            /* 2078 */ mplew.writeInt(size);
            /* 2079 */ if (size > 0) {
                /* 2080 */ mplew.write(imgdata);
            }
        }

        public static byte[] useNoblessSkill(int guildid, int skillLevel) {
            /* 2085 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2086 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2087 */ mplew.write(158);
            /* 2088 */ mplew.writeInt(guildid);
            /* 2089 */ mplew.writeInt(skillLevel);
            /* 2090 */ return mplew.getPacket();
        }

        public static byte[] guildInvite(int gid, String guildname, MapleCharacter chr) {
            /* 2094 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2096 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2097 */ mplew.write(103); //-9 355
            /* 2098 */ mplew.writeInt(gid);
            /* 2099 */ mplew.writeMapleAsciiString(guildname);
            /* 2100 */ mplew.writeInt(chr.getId());
            /* 2101 */ mplew.writeMapleAsciiString(chr.getName());
            /* 2102 */ mplew.writeInt(chr.getLevel());
            /* 2103 */ mplew.writeInt(chr.getJob());
            /* 2104 */ mplew.writeInt(0);
            /* 2105 */ return mplew.getPacket();
        }

        public static byte[] cancelGuildRequest(int gid) {
            /* 2109 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2110 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2111 */ mplew.write(74);
            /* 2112 */ mplew.writeInt(gid);
            /* 2113 */ mplew.writeInt(0);
            /* 2114 */ return mplew.getPacket();
        }

        public static byte[] requestGuild(MapleGuild g, MapleCharacter chr) {
            /* 2118 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2120 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2121 */ mplew.write(85);
            /* 2122 */ mplew.writeInt(g.getId());
            /* 2123 */ mplew.write(g.getLevel());
            /* 2124 */ mplew.writeMapleAsciiString(g.getName());
            /* 2125 */ mplew.writeMapleAsciiString(g.getLeaderName());
            /* 2126 */ mplew.writeShort(g.getMembers().size());
            /* 2127 */ mplew.writeShort(g.avergeMemberLevel());
            /* 2128 */ mplew.write(0);
            /* 2129 */ mplew.writeLong(PacketHelper.getTime(-2L));
            /* 2130 */ mplew.write(1);
            /* 2131 */ mplew.writeMapleAsciiString("");

            /* 2133 */ mplew.writeInt(0);
            /* 2134 */ mplew.writeInt(0);
            /* 2135 */ mplew.writeInt(0);
            /* 2136 */ mplew.write(false);
            /* 2137 */ return mplew.getPacket();
        }

        public static byte[] getGuildInfo(MapleGuild g) {
            /* 2141 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2142 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2143 */ mplew.write(60);
            /* 2144 */ mplew.writeInt(g.getId());
            /* 2145 */ mplew.writeMapleAsciiString("");
            /* 2146 */ getGuildInfo(mplew, g);
            /* 2147 */ mplew.writeShort(0);
            /* 2148 */ mplew.writeShort(0);
            /* 2149 */ mplew.writeShort(0);
            /* 2150 */ return mplew.getPacket();
        }

        public static byte[] showGuildInfo(MapleCharacter c) {
            /* 2154 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2155 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2156 */ mplew.write(53);
            /* 2157 */ if (c == null || c.getMGC() == null) {
                /* 2158 */ mplew.writeInt(0);
                /* 2159 */ mplew.write(0);
                /* 2160 */ return mplew.getPacket();
            }
            /* 2162 */ MapleGuild g = World.Guild.getGuild(c.getGuildId());
            /* 2163 */ mplew.writeInt(0);
            /* 2164 */ mplew.write(true);
            /* 2166 */ getGuildInfo(mplew, g);
            /* 2167 */ mplew.writeInt(30);
            /* 2168 */ for (int i = 0; i < 30; i++) {
                /* 2169 */ mplew.writeInt(GameConstants.getGuildExpNeededForLevel(i));
            }
            /* 2171 */ return mplew.getPacket();
        }

        public static byte[] showGuildInfo(MapleGuild g) {
            /* 2175 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2176 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2177 */ mplew.write(53);
            /* 2178 */ mplew.writeInt(0);
            /* 2179 */ mplew.write(true);
            /* 2181 */ getGuildInfo(mplew, g);
            /* 2182 */ mplew.writeInt(30);
            /* 2183 */ for (int i = 0; i < 30; i++) {
                /* 2184 */ mplew.writeInt(GameConstants.getGuildExpNeededForLevel(i));
            }
            /* 2186 */ return mplew.getPacket();
        }

        public static void getGuildInfo(MaplePacketLittleEndianWriter mplew, MapleGuild guild) {
            /* 2190 */ mplew.writeInt(guild.getId());
            /* 2191 */ mplew.writeMapleAsciiString(guild.getName());
            /* 2192 */ for (int i = 1; i <= 10; i++) {
                /* 2193 */ mplew.writeMapleAsciiString(i > 5 ? "" : guild.getRankTitle(i));
                /* 2194 */ mplew.writeInt(i > 5 ? 0x400 : guild.getRankRole(i));
            }
            /* 2196 */ guild.addMemberData(mplew);
            /* 2197 */ guild.addRequestMemberData(mplew);

            /* 2199 */ mplew.writeInt(guild.getCapacity());
            /* 2200 */ mplew.writeShort(guild.getLogoBG());
            /* 2201 */ mplew.write(guild.getLogoBGColor());
            /* 2202 */ mplew.writeShort(guild.getLogo());
            /* 2203 */ mplew.write(guild.getLogoColor());
            /* 2204 */ mplew.writeMapleAsciiString(guild.getNotice());
            /* 2205 */ mplew.writeInt(guild.getFame());
            /* 2206 */ mplew.writeInt(guild.getFame());
            /* 2207 */ mplew.writeInt((guild.getAllianceId() > 0) ? guild.getAllianceId() : 0);
            /* 2208 */ mplew.write(guild.getLevel());
            /* 2209 */ mplew.writeInt(guild.getGP());
            /* 2210 */ mplew.writeInt(guild.getBeforeAttance());
            /* 2211 */ mplew.writeInt(GameConstants.getCurrentDateYesterday());

            /* 2213 */ mplew.write(1);
            /* 2214 */ mplew.writeLong(PacketHelper.getTime(-2L));
            /* 2215 */ mplew.writeInt(0);
            /* 2216 */ mplew.writeInt(0);
            /* 2217 */ mplew.writeInt(0);
            /* 2218 */ mplew.writeShort(guild.getSkills().size());
            /* 2219 */ for (MapleGuildSkill mapleGuildSkill : guild.getSkills()) {
                /* 2220 */ mplew.writeInt(mapleGuildSkill.skillID);
                /* 2221 */ mplew.writeShort(mapleGuildSkill.level);
                /* 2222 */ mplew.writeLong(PacketHelper.getTime(mapleGuildSkill.timestamp));
                /* 2223 */ mplew.writeMapleAsciiString(mapleGuildSkill.purchaser);
                /* 2224 */ mplew.writeMapleAsciiString(mapleGuildSkill.activator);
            }
            /* 2226 */ mplew.write(false);
            mplew.write(-1);
            /* 2227 */ int size = (guild.getCustomEmblem() == null) ? 0 : (guild.getCustomEmblem()).length;
            /* 2228 */ mplew.writeInt(size);
            /* 2229 */ if (size > 0) {
                /* 2230 */ mplew.write(guild.getCustomEmblem());
            }
            /* 2232 */ mplew.writeInt(0);
        }

        public static byte[] newGuildInfo(MapleCharacter c) {
            /* 2236 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2238 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2239 */ mplew.write(61);
            /* 2240 */ if (c == null || c.getMGC() == null) {
                /* 2241 */ return genericGuildMessage(92);
            }
            /* 2243 */ MapleGuild g = World.Guild.getGuild(c.getGuildId());
            /* 2244 */ if (g == null) {
                /* 2245 */ return genericGuildMessage(92);
            }
            /* 2247 */ getGuildInfo(mplew, g);

            /* 2249 */ return mplew.getPacket();
        }

        public static byte[] newGuildMember(MapleGuildCharacter mgc) {
            /* 2253 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2255 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2256 */ mplew.write(69);
            /* 2257 */ mplew.writeInt(mgc.getGuildId());
            /* 2258 */ mplew.writeInt(mgc.getId());
            /* 2259 */ addNewMemberData(mplew, mgc);
            /* 2260 */ return mplew.getPacket();
        }

        public static byte[] addRegisterRequest(MapleGuildCharacter mgc) { //ok
            /* 2264 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2265 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2266 */ mplew.write(78); //-10 355 362 +0 ok
            /* 2267 */ mplew.writeInt(mgc.getGuildId());
            /* 2268 */ mplew.writeInt(mgc.getId());
            mplew.writeMapleAsciiString(""); //자기소개
            /* 2269 */ addNewMemberData(mplew, mgc);
            /* 2270 */ return mplew.getPacket();
        }

        public static void addNewMemberData(MaplePacketLittleEndianWriter mplew, MapleGuildCharacter mgc) {
            mplew.writeInt(mgc.getGuildId()); // 355 new
            /* 2274 */ mplew.writeAsciiString(mgc.getName(), 13);
            /* 2275 */ mplew.writeInt(mgc.getJobId());
            /* 2276 */ mplew.writeInt(mgc.getLevel());
            /* 2277 */ mplew.writeInt(mgc.getGuildRank());
            /* 2278 */ mplew.writeInt(mgc.isOnline() ? 1 : 0);
            /* 2279 */ mplew.writeLong(PacketHelper.getTime(-2L));
            /* 2280 */ mplew.writeInt(mgc.getAllianceRank());
            /* 2281 */ mplew.writeInt(mgc.getGuildContribution());
            /* 2282 */ mplew.writeInt(0);
            /* 2283 */ mplew.writeLong(PacketHelper.getTime(-2L));
            /* 2284 */ mplew.writeInt(0);
            /* 2285 */ mplew.writeLong(PacketHelper.getTime(-2L));
            /* 2286 */ mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis()));
        }

        public static byte[] RequestDeny(MapleCharacter chr, MapleGuild g) {
            /* 2290 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2292 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2293 */ mplew.write(87); //-9 355 362 +1 ok
            /* 2294 */ mplew.writeInt(chr.getId());
            /* 2295 */ mplew.writeInt(g.getId());
            /* 2296 */ return mplew.getPacket();
        }

        public static byte[] DelayRequest() {
            /* 2300 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2302 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2303 */ mplew.write(85); //-9 355 362 +1 ok
            /* 2304 */ return mplew.getPacket();
        }

        public static byte[] memberLeft(MapleGuildCharacter mgc, boolean bExpelled) {
            /* 2308 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2310 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2311 */ mplew.write(91); //-6 362 +1 ok
            /* 2312 */ mplew.writeInt(mgc.getGuildId());
            /* 2313 */ mplew.writeInt(mgc.getId());
            /* 2314 */ mplew.writeMapleAsciiString(mgc.getName());
            /* 2315 */ return mplew.getPacket();
        }

        public static byte[] guildDisband(int gid) {
            /* 2319 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2321 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2322 */ mplew.write(94);
            /* 2323 */ mplew.writeInt(gid);

            /* 2325 */ return mplew.getPacket();
        }

        public static byte[] guildDisband2() {
            /* 2329 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2330 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2331 */ mplew.write(158);
            /* 2332 */ mplew.writeMapleAsciiString("");
            /* 2333 */ return mplew.getPacket();
        }

        public static byte[] guildCapacityChange(int gid, int capacity) {
            /* 2337 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2339 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2340 */ mplew.write(112);
            /* 2341 */ mplew.writeInt(gid);
            /* 2342 */ mplew.write(capacity);

            /* 2344 */ return mplew.getPacket();
        }

        public static byte[] guildContribution(int gid, int cid, int c) {
            /* 2348 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2350 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2351 */ mplew.write(130); // 362 +1 ok
            /* 2352 */ mplew.writeInt(gid);
            /* 2353 */ mplew.writeInt(cid);
            /* 2354 */ mplew.writeInt(c);
            /* 2355 */ mplew.writeInt(Math.min(c, 5000));
            /* 2356 */ mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis() + 43200000L));
            /* 2357 */ return mplew.getPacket();
        }

        public static byte[] changeRank(MapleGuildCharacter mgc) { //OK
            /* 2361 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2363 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2364 */ mplew.write(128); //362 +1
            /* 2365 */ mplew.writeInt(mgc.getGuildId());
            /* 2366 */ mplew.writeInt(mgc.getId());
            /* 2367 */ mplew.write(mgc.getGuildRank());

            /* 2369 */ return mplew.getPacket();
        }

        public static byte[] rankTitleChange(int type, MapleCharacter chr, String[] ranks, int[] roles) {
            int i;
            /* 2373 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2375 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2376 */ mplew.write(type);
            /* 2377 */ mplew.writeInt(chr.getGuildId());
            /* 2378 */ mplew.writeInt(chr.getId());
            /* 2379 */ switch (type) {
                case 122:
                    for (i = 0; i < ranks.length; i++) {
                        mplew.writeInt(roles[i]);
                        mplew.writeMapleAsciiString(ranks[i]);
                    }
                    for (int w = 0; w < 5; w++) {
                        mplew.writeInt(0);
                        mplew.writeMapleAsciiString("");
                    }
                    break;
                case 125:
                    /* 2381 */ for (i = 0; i < ranks.length; i++) {
                        /* 2382 */ mplew.writeMapleAsciiString(ranks[i]);
                    }
                    break;

                case 127:
                    /* 2387 */ for (i = 0; i < ranks.length; i++) {
                        /* 2388 */ mplew.writeInt(roles[i]);
                    }
                    break;

                case 129:
                    /* 2393 */ for (i = 0; i < ranks.length; i++) {
                        /* 2394 */ mplew.writeInt(roles[i]);
                        /* 2395 */ mplew.writeMapleAsciiString(ranks[i]);
                    }
                    break;
            }

            /* 2400 */ return mplew.getPacket();
        }

        public static byte[] guildEmblemChange(MapleCharacter chr, short bg, byte bgcolor, short logo, byte logocolor) {
            /* 2404 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2406 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2407 */ mplew.write(132); // 362 +1
            /* 2408 */ mplew.writeInt(chr.getGuildId());
            /* 2409 */ mplew.writeInt(chr.getId());
            /* 2410 */ mplew.write(0);
            /* 2411 */ mplew.writeShort(bg);
            /* 2412 */ mplew.write(bgcolor);
            /* 2413 */ mplew.writeShort(logo);
            /* 2414 */ mplew.write(logocolor);
            mplew.write(0); // 351
            /* 2415 */ mplew.writeInt(0);
            /* 2416 */ mplew.writeInt(0);
            mplew.writeShort(0); // 352
            /* 2417 */ return mplew.getPacket();
        }

        public static byte[] updateGP(int gid, int GP, int glevel) {
            /* 2421 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2423 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2424 */ mplew.write(145); //355 -> 362 +1
            /* 2425 */ mplew.writeInt(gid);
            /* 2426 */ mplew.writeInt(GP);
            /* 2427 */ mplew.writeInt(glevel);
            /* 2428 */ mplew.writeInt(150);
            /* 2429 */ mplew.writeInt(0);

            /* 2431 */ return mplew.getPacket();
        }

        public static byte[] guildNotice(MapleCharacter chr, String notice) {
            /* 2435 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2437 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2438 */ mplew.write(142); //355 -> 362 +1 ok
            /* 2439 */ mplew.writeInt(chr.getGuildId());
            /* 2440 */ mplew.writeInt(chr.getId());
            /* 2441 */ mplew.writeMapleAsciiString(notice);

            /* 2443 */ return mplew.getPacket();
        }

        public static byte[] guildMemberLevelJobUpdate(MapleGuildCharacter mgc) {
            /* 2447 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2449 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2450 */ mplew.write(115); //-10 355 362 +1
            /* 2451 */ mplew.writeInt(mgc.getGuildId());
            /* 2452 */ mplew.writeInt(mgc.getId());
            /* 2453 */ mplew.writeInt(mgc.getLevel());
            /* 2454 */ mplew.writeInt(mgc.getJobId());

            /* 2456 */ return mplew.getPacket();
        }

        public static byte[] guildMemberOnline(int gid, int cid, boolean bOnline) {
            /* 2460 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2462 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2463 */ mplew.write(116); //-10 355
            /* 2464 */ mplew.writeInt(gid);
            /* 2465 */ mplew.writeInt(cid);
            /* 2466 */ mplew.write(bOnline ? 1 : 0);
            /* 2467 */ if (!bOnline) {
                /* 2468 */ mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
            }
            /* 2470 */ mplew.write(1);

            /* 2472 */ return mplew.getPacket();
        }

        public static byte[] guildRankingRequest() {
            /* 2476 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2477 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2478 */ mplew.write(146); //355 -> 362 +1
            /* 2479 */ return mplew.getPacket();
        }

        public static byte[] showGuildRanks(byte type, MapleClient c, MapleGuildRanking ranks) {
            /* 2483 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2485 */ mplew.writeShort(SendPacketOpcode.SHOW_GUILD_RANK.getValue());
            /* 2486 */ mplew.write(type);

            /* 2488 */ if (type == 1 || type == 2) {
                /* 2489 */ for (int i = 0; i < 3; i++) {
                    /* 2490 */ List<MapleGuildRanking.GuildRankingInfo> rank = null;
                    /* 2491 */ switch (i) {
                        case 0:
                            /* 2493 */ rank = ranks.getFlagRaceRank();
                            break;
                        case 1:
                            /* 2496 */ rank = ranks.getHonorRank();
                            break;
                        case 2:
                            /* 2499 */ rank = ranks.getCulvertRank();
                            break;
                    }
                    /* 2502 */ if (rank == null) {
                        /* 2503 */ mplew.writeInt(0);
                    } else {
                        /* 2505 */ mplew.writeInt(rank.size());
                        /* 2506 */ for (MapleGuildRanking.GuildRankingInfo info : rank) {
                            /* 2507 */ mplew.writeInt(info.getId());
                            /* 2508 */ mplew.writeInt(info.getScore());
                            /* 2509 */ mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
                            /* 2510 */ mplew.writeMapleAsciiString(info.getName());
                        }
                    }
                }
            } else if (type == 6) {
                mplew.writeLong(0);
                mplew.writeInt(-2);
                mplew.writeInt(-1);
                mplew.writeInt(c.getPlayer().getGuildId());
                mplew.writeInt(0);
                mplew.write(1);
                MapleGuild g = World.Guild.getGuild(c.getPlayer().getGuildId());
                mplew.writeInt(g.getNoblessSkillPoint()); //노블
                mplew.writeInt(3);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);

                mplew.writeInt(1);
                mplew.writeInt(c.getPlayer().getId());
                mplew.writeInt(0);
                mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));

                mplew.writeInt(2);
                mplew.writeInt(0);
                mplew.writeInt(0);

                mplew.writeInt(1);
                mplew.writeInt(c.getPlayer().getId());
                mplew.writeInt(0);
                mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));

                mplew.writeInt(3);
                mplew.writeInt(0);
                mplew.writeInt(0);

                mplew.writeInt(1);
                mplew.writeInt(c.getPlayer().getId());
                mplew.writeInt(0);
                mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));

                mplew.writeInt(30);
                mplew.writeInt(0x0258);
                mplew.write(HexTool.getByteArrayFromHexString("03 00 00 00 00 00 00 00 0A 00 00 00 01 00 00 00 14 00 00 00 02 00 00 00 11 00 00 00 03 00 00 00 0F 00 00 00 04 00 00 00 0D 00 00 00 05 00 00 00 0D 00 00 00 06 00 00 00 0D 00 00 00 07 00 00 00 0D 00 00 00 08 00 00 00 0D 00 00 00 09 00 00 00 0D 00 00 00 0A 00 00 00 0D 00 00 00 0A 00 00 00 0A 00 00 00 0B 00 00 00 14 00 00 00 09 00 00 00 1E 00 00 00 07 00 00 00 28 00 00 00 05 00 00 00 32 00 00 00 04 00 00 00 3C 00 00 00 03 00 00 00 46 00 00 00 02 00 00 00 50 00 00 00 01 00 00 00 5A 00 00 00 00 00 00 00 64 00 00 00 00 00 00 00 00 00 00 00 E8 03 00 00 05 00 00 00 02 00 00 00 0A 00 00 00 01 00 00 00 14 00 00 00 02 00 00 00 12 00 00 00 03 00 00 00 10 00 00 00 04 00 00 00 0F 00 00 00 05 00 00 00 0F 00 00 00 06 00 00 00 0F 00 00 00 07 00 00 00 0F 00 00 00 08 00 00 00 0F 00 00 00 09 00 00 00 0F 00 00 00 0A 00 00 00 0F 00 00 00 0A 00 00 00 0A 00 00 00 0E 00 00 00 14 00 00 00 0C 00 00 00 1E 00 00 00 0C 00 00 00 28 00 00 00 0A 00 00 00 32 00 00 00 0A 00 00 00 3C 00 00 00 0A 00 00 00 46 00 00 00 05 00 00 00 50 00 00 00 05 00 00 00 5A 00 00 00 00 00 00 00 64 00 00 00 00 00 00 00 00 00 00 00 F4 01 00 00 05 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 0A 00 00 00 C8 00 00 00 0A 00 00 00 B4 00 00 00 09 00 00 00 A0 00 00 00 08 00 00 00 8C 00 00 00 07 00 00 00 78 00 00 00 06 00 00 00 64 00 00 00 05 00 00 00 50 00 00 00 04 00 00 00 3C 00 00 00 03 00 00 00 28 00 00 00 02 00 00 00 14 00 00 00 01 00 00 00 00 01 00 00 00 00 00 00"));
            }
            mplew.writeZeroBytes(100);
            /* 2516 */ return mplew.getPacket();
        }

        public static byte[] getGuildRanksInfo(int rank1, int rank2, int rank3) {
            /* 2520 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2521 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2522 */ mplew.write(61);
            /* 2523 */ mplew.writeShort(rank1);
            /* 2524 */ mplew.writeShort(rank2);
            /* 2525 */ mplew.writeShort(rank3);
            /* 2526 */ return mplew.getPacket();
        }

        public static byte[] guildSkillPurchased(int gid, int sid, int level, long expiration, String purchase, String activate) {
            /* 2530 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2532 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2533 */ mplew.write(160); //355 -> 362 +1 ok
            /* 2534 */ mplew.writeInt(gid);
            /* 2535 */ mplew.writeInt(sid);
            /* 2536 */ mplew.writeInt(0);
            /* 2537 */ mplew.writeShort(level);
            /* 2538 */ mplew.writeLong(PacketHelper.getTime(expiration));
            /* 2539 */ mplew.writeMapleAsciiString(purchase);
            /* 2540 */ mplew.writeMapleAsciiString(activate);
            /* 2541 */ return mplew.getPacket();
        }

        public static byte[] guildLeaderChanged(int gid, int oldLeader, int newLeader, int allianceId) {
            /* 2545 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2547 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2548 */ mplew.write(175); //362
            /* 2549 */ mplew.writeInt(gid);
            /* 2550 */ mplew.writeInt(oldLeader);
            /* 2551 */ mplew.writeInt(newLeader);
            /* 2552 */ mplew.write(0);
            /* 2553 */ mplew.write(1);
            /* 2554 */ mplew.writeInt(allianceId);

            /* 2556 */ return mplew.getPacket();
        }

        public static byte[] denyGuildInvitation(String charname) {
            /* 2560 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2562 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2563 */ mplew.write(102); //355 -> 362 +1 ok
            /* 2564 */ mplew.writeMapleAsciiString(charname);

            /* 2566 */ return mplew.getPacket();
        }

        public static byte[] genericGuildMessage(int code, String name) {
            /* 2570 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2571 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2572 */ mplew.write(code);
            /* 2573 */ switch (code) {
                case 42:
                    /* 2575 */ mplew.writeMapleAsciiString(name);
                    break;
            }

            /* 2579 */ return mplew.getPacket();
        }

        public static byte[] genericGuildMessage(int code) {
            /* 2583 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2585 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2586 */ mplew.write(code);
            /* 2587 */ switch (code) {
                case 135:
                    /* 2589 */ mplew.writeInt(0);
                    break;
                case 64:
                    /* 2592 */ mplew.writeInt(0);
                    /* 2593 */ mplew.writeInt(0);
                    break;
            }
            /* 2596 */ if (code == 92 || code == 127 || code == 135 || code == 118) {
                /* 2597 */ mplew.writeInt(11);
            }
            /* 2599 */ if (code == 5 || code == 103 || code == 104 || code == 100 || code == 101 || code == 102) {
                /* 2600 */ mplew.writeMapleAsciiString("");
            }
            /* 2602 */ return mplew.getPacket();
        }

        public static byte[] RecruitmentGuild(MapleCharacter chr) {
            /* 2606 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2607 */ mplew.writeShort(SendPacketOpcode.SEARCH_GUILD.getValue());
            /* 2608 */ mplew.writeShort(4);
            /* 2609 */ mplew.writeShort(0);
            /* 2610 */ mplew.write(0);
            /* 2611 */ mplew.write(0);
            /* 2612 */ mplew.write(1);
            /* 2613 */ mplew.write(0);

            /* 2615 */ mplew.writeInt(0);
            /* 2616 */ mplew.writeInt(World.Guild.getGuilds().size());
            /* 2617 */ int i = 0;
            /* 2618 */ for (MapleGuild guild : World.Guild.getGuilds()) {
                /* 2619 */ mplew.writeInt(guild.getId());
                /* 2620 */ mplew.write(guild.getLevel());
                /* 2621 */ mplew.writeMapleAsciiString(guild.getName());
                /* 2622 */ mplew.writeMapleAsciiString(guild.getLeaderName());
                /* 2623 */ mplew.writeShort(guild.getMembers().size());
                /* 2624 */ mplew.writeShort(guild.avergeMemberLevel());
                /* 2625 */ mplew.write((guild.getRequest(chr.getId()) != null) ? 0 : 1);
                /* 2626 */ mplew.writeLong(0L);
                /* 2627 */ mplew.write(1);
                /* 2628 */ mplew.writeMapleAsciiString(guild.getNotice());
                /* 2629 */ mplew.writeInt(0);
                /* 2630 */ mplew.writeInt(0);
                /* 2631 */ mplew.writeInt(0);
                /* 2632 */ mplew.write((i < 5) ? 1 : 0);
                /* 2633 */ i++;
            }
            /* 2635 */ mplew.writeInt(0);
            /* 2636 */ return mplew.getPacket();
        }

        public static byte[] RequestListGuild(List<MapleGuild> g) {
            /* 2640 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2641 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2642 */ mplew.write(188); //355 -> 362 +1 188
            /* 2643 */ mplew.writeInt(g.size());
            /* 2644 */ for (MapleGuild guild : g) {
                /* 2645 */ mplew.writeInt(guild.getId());
                /* 2646 */ mplew.write(guild.getLevel());
                /* 2647 */ mplew.writeMapleAsciiString(guild.getName());
                /* 2648 */ mplew.writeMapleAsciiString(guild.getLeaderName());
                /* 2649 */ mplew.writeShort(guild.getMembers().size());
                /* 2650 */ mplew.writeShort(guild.avergeMemberLevel());
                /* 2651 */ mplew.write(0);
                /* 2652 */ mplew.writeLong(PacketHelper.getTime(-2L));
                /* 2653 */ mplew.write(1);
                /* 2654 */ mplew.writeMapleAsciiString(guild.getNotice());
                /* 2655 */ mplew.writeInt(0);
                /* 2656 */ mplew.writeInt(0);
                /* 2657 */ mplew.writeInt(0);
                /* 2658 */ mplew.write(0);
            }
            /* 2660 */ mplew.writeInt(0);
            /* 2661 */ return mplew.getPacket();
        }

        public static byte[] showSearchGuildInfo(MapleCharacter chr, List<MapleGuild> gss, String text, byte mode, int option) {
            /* 2665 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2667 */ mplew.writeShort(SendPacketOpcode.SEARCH_GUILD.getValue());
            /* 2668 */ mplew.writeShort(mode);
            /* 2669 */ mplew.writeMapleAsciiString(text);
            /* 2670 */ mplew.write(option);
            /* 2671 */ mplew.write(0);
            /* 2672 */ mplew.write(1);
            /* 2673 */ mplew.write(0);
            /* 2674 */ mplew.writeInt(0);
            /* 2675 */ mplew.writeInt(gss.size());
            /* 2676 */ for (MapleGuild gs : gss) {
                /* 2677 */ mplew.writeInt(gs.getId());
                /* 2678 */ mplew.write(gs.getLevel());
                /* 2679 */ mplew.writeMapleAsciiString(gs.getName());
                /* 2680 */ mplew.writeMapleAsciiString(gs.getLeaderName());
                /* 2681 */ mplew.writeShort(gs.getMembers().size());
                /* 2682 */ mplew.writeShort(gs.avergeMemberLevel());
                /* 2683 */ mplew.write((gs.getRequest(chr.getId()) != null) ? 0 : 1);
                /* 2684 */ mplew.writeLong(0L);
                /* 2685 */ mplew.write(1);
                /* 2686 */ mplew.writeMapleAsciiString(gs.getNotice());
                /* 2687 */ mplew.writeInt(135);
                /* 2688 */ mplew.writeInt(55);
                /* 2689 */ mplew.writeInt(14);
                /* 2690 */ mplew.write(0);
            }
            /* 2692 */ return mplew.getPacket();
        }

        public static byte[] LooksGuildInformation(MapleGuild guild) {
            /* 2696 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2698 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2699 */ mplew.write(55); //-9 355 -> 362 +0
            /* 2700 */ mplew.writeInt(guild.getId());
            /* 2701 */ mplew.writeShort(0);
            /* 2702 */ getGuildInfo(mplew, guild);
            /* 2703 */ mplew.writeShort(0);
            /* 2704 */ mplew.writeShort(0);
            /* 2705 */ mplew.writeShort(0);
            mplew.writeZeroBytes(10);
            /* 2706 */ return mplew.getPacket();
        }

        public static byte[] guildUpdateOnlyGP(int gid, int gp) {
            /* 2710 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2712 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2713 */ mplew.write(154); //355 -> 362 +1 ok
            /* 2714 */ mplew.writeInt(gid);
            /* 2715 */ mplew.writeInt(gp);

            /* 2717 */ return mplew.getPacket();
        }

        public static byte[] updateGP(int gid, int contribution, int GP, int glevel) { //ok
            /* 2721 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2723 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2724 */ mplew.write(152); //355 -> 362 +1 ok
            /* 2725 */ mplew.writeInt(gid);
            /* 2726 */ mplew.writeInt(contribution);
            /* 2727 */ mplew.writeInt(glevel);
            /* 2728 */ mplew.writeLong(GP);

            /* 2730 */ return mplew.getPacket();
        }

        public static byte[] GainGP(MapleGuild guild, MapleCharacter chr, int amount) { //ok
            /* 2734 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2735 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2736 */ mplew.write(130); //355 -4 362 +1
            /* 2737 */ mplew.writeInt(guild.getId());
            /* 2738 */ mplew.writeInt(chr.getId());
            /* 2739 */ mplew.writeInt(amount);
            /* 2740 */ mplew.writeInt(amount);
            /* 2741 */ mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
            /* 2742 */ return mplew.getPacket();
        }

        public static byte[] guildLoadAattendance() {
            /* 2746 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2747 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2748 */ mplew.write(186); //-4 355 -> 362 +2
            /* 2749 */ mplew.writeInt(4);

            /* 2751 */ mplew.writeInt(10);
            /* 2752 */ mplew.writeInt(50);

            /* 2754 */ mplew.writeInt(30);
            /* 2755 */ mplew.writeInt(100);

            /* 2757 */ mplew.writeInt(60);
            /* 2758 */ mplew.writeInt(1000);

            /* 2760 */ mplew.writeInt(100);
            /* 2761 */ mplew.writeInt(2000);
            /* 2762 */ return mplew.getPacket();
        }

        public static byte[] guildAattendance(MapleGuild guild, MapleCharacter chr) { //ok
            /* 2766 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2767 */ mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            /* 2768 */ mplew.write(183); //355 -> 362 +1 ok
            /* 2769 */ mplew.writeInt(guild.getId());
            /* 2770 */ mplew.writeInt(chr.getId());
            /* 2771 */ mplew.writeInt(GameConstants.getCurrentDateday());
            /* 2772 */ return mplew.getPacket();
        }
    }

    public static class PartyPacket {

        public static byte[] partyCreated(MapleParty party) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(15);
            mplew.writeInt(party.getId());
            mplew.writeInt(party.getLeader().getDoorTown());
            mplew.writeInt(party.getLeader().getDoorTarget());
            mplew.writeInt(0);
            mplew.write(0); // 352 new
            mplew.writeShort((party.getLeader().getDoorPosition()).x);
            mplew.writeShort((party.getLeader().getDoorPosition()).y);
            mplew.write(party.getVisible());
            mplew.writeMapleAsciiString(party.getPatryTitle());
            mplew.write(1);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] partyInvite(MapleCharacter from) {
            /* 2798 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2800 */ mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            /* 2801 */ mplew.write(4);
            /* 2802 */ mplew.writeInt((from.getParty() == null) ? 0 : from.getParty().getId());
            /* 2803 */ mplew.writeMapleAsciiString(from.getName());
            /* 2804 */ mplew.writeInt(from.getLevel());
            /* 2805 */ mplew.writeInt(from.getJob());
            /* 2806 */ mplew.writeInt(1);
            /* 2807 */ mplew.write(0);
            /* 2808 */ mplew.write(0);

            /* 2810 */ return mplew.getPacket();
        }

        public static byte[] partyRequestInvite(MapleCharacter from) {
            /* 2814 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2816 */ mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            /* 2817 */ mplew.write(8);
            /* 2818 */ mplew.writeInt(from.getId());
            /* 2819 */ mplew.writeMapleAsciiString(from.getName());
            /* 2820 */ mplew.writeInt(from.getLevel());
            /* 2821 */ mplew.writeInt(from.getJob());
            /* 2822 */ mplew.writeInt(0);

            /* 2824 */ return mplew.getPacket();
        }

        public static byte[] partyStatusMessage(int message, String charname) {
            /* 2828 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2847 */ mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            /* 2848 */ mplew.write(message);
            /* 2849 */ if (message == 26 || message == 52) {
                /* 2850 */ mplew.writeMapleAsciiString(charname);
                /* 2851 */            } else if (message == 45) {
                /* 2852 */ mplew.write(0);
            }
            /* 2854 */ return mplew.getPacket();
        }

        public static void addPartyStatus(int forchannel, MapleParty party, MaplePacketLittleEndianWriter lew, boolean leaving) {
            /* 2858 */ addPartyStatus(forchannel, party, lew, leaving, false);
        }

        public static void addPartyStatus(int forchannel, MapleParty party, MaplePacketLittleEndianWriter lew, boolean leaving, boolean exped) {
            List<MaplePartyCharacter> partymembers;
            if (party == null) {
                partymembers = new ArrayList<>();
            } else {
                partymembers = new ArrayList<>(party.getMembers());
            }
            while (partymembers.size() < 6) {
                partymembers.add(new MaplePartyCharacter());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getId());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeAsciiString(partychar.getName(), 13);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getJobId());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.isOnline() ? 1 : 0);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getLevel());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.isOnline() ? (partychar.getChannel() - 1) : -2);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(0);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(0);
            }
            lew.writeInt((party == null) ? 0 : party.getLeader().getId());
            if (exped) {
                return;
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt((partychar.getChannel() == forchannel) ? partychar.getMapid() : 0);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                if (partychar.getChannel() == forchannel && !leaving) {
                    lew.writeInt(partychar.getDoorTown());
                    lew.writeInt(partychar.getDoorTarget());
                    lew.writeInt(partychar.getDoorSkill());
                    lew.writeInt((partychar.getDoorPosition()).x);
                    lew.writeInt((partychar.getDoorPosition()).y);
                    continue;
                }
                lew.writeInt(leaving ? 999999999 : 0);
                lew.writeLong(leaving ? 999999999L : 0L);
                lew.writeLong(leaving ? -1L : 0L);
            }
            lew.write(party.getVisible());
            lew.writeShort(30);
            lew.writeAsciiString(party.getPatryTitle(), 30);
            lew.write(1);
            lew.write(0);
        }

        public static byte[] updateParty(int forChannel, MapleParty party, PartyOperation op, MaplePartyCharacter target) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            switch (op) {
                case DISBAND:
                case EXPEL:
                case LEAVE:
                    mplew.write(17); // 362
                    mplew.writeInt(party.getId());
                    mplew.writeInt(target.getId());
                    mplew.write(op == PartyOperation.DISBAND ? 0 : 1);
                    if (op == PartyOperation.DISBAND) {
                        mplew.writeInt(target.getId());
                        break;
                    } else {
                        if (op == PartyOperation.EXPEL) {
                            mplew.write(1);
                        } else {
                            mplew.write(0);
                        }
                        mplew.writeMapleAsciiString(target.getName());
                        addPartyStatus(forChannel, party, mplew, op == PartyOperation.LEAVE);
                        break;
                    }
                case JOIN:
                    mplew.write(19); // 355 -1
                    mplew.writeInt(party.getId());
                    mplew.writeMapleAsciiString(target.getName());
                    mplew.write(0); //274 ++
                    mplew.writeInt(0); //274 ++
                    addPartyStatus(forChannel, party, mplew, false);
                    break;
                case SILENT_UPDATE:
                case LOG_ONOFF:
                    mplew.write(14); //274 +2
                    mplew.writeInt(party.getId());
                    addPartyStatus(forChannel, party, mplew, op == PartyOperation.LOG_ONOFF);
                    break;
                case CHANGE_LEADER:
                case CHANGE_LEADER_DC:
                    mplew.write(47); //274 +3
                    mplew.writeInt(target.getId());
                    mplew.write(op == PartyOperation.CHANGE_LEADER_DC ? 1 : 0);
                    break;
                case CHANGE_PARTY_TITLE:
                    mplew.write(81);
//                    mplew.write(party.getVisible());
                    mplew.writeMapleAsciiString(party.getPatryTitle());
                    mplew.writeInt(target.getId());
                    mplew.writeInt(party.getId());
                    mplew.writeInt(1);
                    mplew.writeInt(1);
//                    mplew.write(1);
                    break;
            }

            return mplew.getPacket();
        }

        public static byte[] partyPortal(int townId, int targetId, int skillId, Point position, boolean animation) {
            /* 2982 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2984 */ mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            /* 2985 */ mplew.write(86);
            /* 2986 */ mplew.write(animation ? 0 : 1);
            /* 2987 */ mplew.writeInt(townId);
            /* 2988 */ mplew.writeInt(targetId);
            /* 2989 */ mplew.writeInt(skillId);
            /* 2990 */ mplew.writePos(position);

            /* 2992 */ return mplew.getPacket();
        }

        public static byte[] showMemberSearch(List<MapleCharacter> chr) {
            /* 2997 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2998 */ mplew.writeShort(SendPacketOpcode.MEMBER_SEARCH.getValue());
            /* 2999 */ mplew.write(chr.size());
            /* 3000 */ for (MapleCharacter c : chr) {
                /* 3001 */ mplew.writeInt(c.getId());
                /* 3002 */ mplew.writeMapleAsciiString(c.getName());
                /* 3003 */ mplew.writeShort(c.getJob());
                /* 3004 */ mplew.write(c.getLevel());
            }
            /* 3006 */ return mplew.getPacket();
        }

        public static byte[] showPartySearch(List<MapleParty> chr) {
            /* 3010 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 3011 */ mplew.writeShort(SendPacketOpcode.PARTY_SEARCH.getValue());
            /* 3012 */ mplew.write(chr.size());
            /* 3013 */ for (MapleParty c : chr) {
                /* 3014 */ mplew.writeInt(c.getId());
                /* 3015 */ mplew.writeMapleAsciiString(c.getLeader().getName());
                /* 3016 */ mplew.write(c.getLeader().getLevel());
                /* 3017 */ mplew.write(c.getLeader().isOnline() ? 1 : 0);
                /* 3018 */ mplew.write(c.getMembers().size());
                /* 3019 */ for (MaplePartyCharacter ch : c.getMembers()) {
                    /* 3020 */ mplew.writeInt(ch.getId());
                    /* 3021 */ mplew.writeMapleAsciiString(ch.getName());
                    /* 3022 */ mplew.writeShort(ch.getJobId());
                    /* 3023 */ mplew.write(ch.getLevel());
                    /* 3024 */ mplew.write(ch.isOnline() ? 1 : 0);
                }
            }
            /* 3027 */ return mplew.getPacket();
        }
    }

    public static class BuddylistPacket {

        public static byte[] updateBuddylist(Collection<BuddylistEntry> buddylist, BuddylistEntry buddies, byte op) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(op);
            if (op == 20) {
                mplew.writeInt(buddylist.size());
                for (BuddylistEntry buddy : buddylist) {
                    mplew.writeInt(buddy.getCharacterId()); // dwCharacterID
                    mplew.writeAsciiString(buddy.getName(), 13); // sFriendName
                    mplew.write(7); // bAccountFriend
                    mplew.writeInt(buddy.getChannel() == -1 ? -1 : (buddy.getChannel() - 1));
                    mplew.writeAsciiString(buddy.getGroupName(), 18); // sGroupName
                    mplew.writeInt(buddy.getAccountId()); // dwAccountID
                    mplew.writeAsciiString(buddy.getRepName(), 13); // sFriendNick
                    mplew.writeAsciiString(buddy.getMemo(), 260); // sFriendMemo
                }

            } else if (op == 23) { //updateFriend
                mplew.writeInt(buddies.getCharacterId());
                mplew.writeInt(buddies.getAccountId());
                mplew.writeInt(buddies.getCharacterId());
                mplew.writeAsciiString(buddies.getName(), 13);
                mplew.write(7);//buddies.isVisible() ? 0 : 7);
                mplew.writeInt(buddies.getChannel() == -1 ? -1 : (buddies.getChannel() - 1));
                mplew.writeAsciiString(buddies.getGroupName(), 18);
                mplew.writeInt(buddies.getAccountId());
                mplew.writeAsciiString(buddies.getRepName(), 13);
                mplew.writeAsciiString(buddies.getMemo(), 261);
            } else if (op == 38) {
                mplew.writeInt(buddies.getCharacterId());
                mplew.writeAsciiString(buddies.getName(), 13);
                mplew.write(7);//buddies.isVisible() ? 0 : 7);
                mplew.writeInt(buddies.getChannel() == -1 ? -1 : (buddies.getChannel() - 1));
                mplew.writeAsciiString(buddies.getGroupName(), 18);
                mplew.writeInt(buddies.getAccountId());
                mplew.writeAsciiString(buddies.getRepName(), 13);
                mplew.writeAsciiString(buddies.getMemo(), 260);
                }
            return mplew.getPacket();
            }

        public static byte[] deleteBuddy(int accId) {
            /* 3087 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 3089 */ mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            /* 3090 */ mplew.write(41);
            /* 3091 */ mplew.write(1);
            /* 3092 */ mplew.writeInt(accId);
            /* 3093 */ return mplew.getPacket();
        }

        public static byte[] buddyAddMessage(String charname) {
            /* 3097 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 3099 */ mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            /* 3100 */ mplew.write(25);
            /* 3101 */ mplew.writeMapleAsciiString(charname);
            /* 3102 */ return mplew.getPacket();
        }

        public static byte[] buddyDeclineMessage(String charname) {
            /* 3106 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 3108 */ mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            /* 3109 */ mplew.write(54);
            /* 3110 */ mplew.writeMapleAsciiString(charname);
            /* 3111 */ return mplew.getPacket();
        }

        public static byte[] requestBuddylistAdd(int cidFrom, int accId, String nameFrom, int levelFrom, int jobFrom, MapleClient c, String groupName, String memo) {
            /* 3115 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 3117 */ mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            /* 3118 */ mplew.write(24);
            /* 3119 */ mplew.write(1);
            /* 3120 */ mplew.writeInt(cidFrom);
            /* 3121 */ mplew.writeInt(accId);
            /* 3122 */ mplew.writeMapleAsciiString(nameFrom);
            /* 3123 */ mplew.writeInt(levelFrom);
            /* 3124 */ mplew.writeInt(jobFrom);
            /* 3125 */ mplew.writeInt(0);

            /* 3127 */ mplew.writeInt(cidFrom);
            /* 3128 */ mplew.writeAsciiString(nameFrom, 13);
            /* 3129 */ mplew.write(6);
            /* 3130 */ mplew.writeInt(c.getChannel() - 1);
            /* 3131 */ mplew.writeAsciiString(groupName, 18);

            /* 3133 */ mplew.writeInt(accId);
            /* 3134 */ mplew.writeAsciiString(nameFrom, 13);
            /* 3135 */ mplew.writeAsciiString(memo, 261);
            /* 3136 */ return mplew.getPacket();
        }

        public static byte[] updateBuddyChannel(int characterid, int accountId, int channel, String name) {
            /* 3140 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 3142 */ mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            /* 3143 */ mplew.write(43);
            /* 3144 */ mplew.writeInt(characterid);
            /* 3145 */ mplew.writeInt(accountId);
            /* 3146 */ mplew.write(1);
            /* 3147 */ mplew.writeInt(channel);
            /* 3148 */ mplew.write(1);
            /* 3149 */ mplew.write(1);
            /* 3150 */ mplew.writeMapleAsciiString(name);

            /* 3152 */ return mplew.getPacket();
        }

        public static byte[] updateBuddyCapacity(int capacity) {
            /* 3156 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 3158 */ mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            /* 3159 */ mplew.write(45);
            /* 3160 */ mplew.write(capacity);

            /* 3162 */ return mplew.getPacket();
        }

        public static byte[] buddylistMessage(byte message) {
            /* 3166 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 3167 */ mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            /* 3168 */ mplew.write(message);
            /* 3169 */ return mplew.getPacket();
        }
    }

    public static class AlliancePacket {

        public static byte[] getAllianceInfo(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(12);
            mplew.write((alliance == null) ? 0 : 1);
            if (alliance != null) {
                addAllianceInfo(mplew, alliance);
            }
            return mplew.getPacket();
        }

        private static void addAllianceInfo(MaplePacketLittleEndianWriter mplew, MapleGuildAlliance alliance) {
            mplew.writeInt(alliance.getId());
            mplew.writeMapleAsciiString(alliance.getName());
            int i;
            for (i = 1; i <= 5; i++) {
                mplew.writeMapleAsciiString(alliance.getRank(i));
            }
            mplew.write(alliance.getNoGuilds());
            for (i = 0; i < alliance.getNoGuilds(); i++) {
                mplew.writeInt(alliance.getGuildId(i));
            }
            mplew.writeInt(alliance.getCapacity());
            mplew.writeMapleAsciiString(alliance.getNotice());
        }

        public static byte[] getGuildAlliance(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(13);
            if (alliance == null) {
                mplew.writeInt(0);
                return mplew.getPacket();
            }
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null);
            }
            mplew.writeInt(noGuilds);
            for (MapleGuild gg : g) {
                if (gg != null) {
                    CWvsContext.GuildPacket.getGuildInfo(mplew, gg);
                }
            }
            return mplew.getPacket();
        }

        public static byte[] allianceMemberOnline(int alliance, int gid, int id, boolean online) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(14);
            mplew.writeInt(alliance);
            mplew.writeInt(gid);
            mplew.writeInt(id);
            mplew.write(online ? 1 : 0);
            mplew.write(online ? 1 : 0);
            return mplew.getPacket();
        }

        public static byte[] removeGuildFromAlliance(MapleGuildAlliance alliance, MapleGuild expelledGuild, boolean expelled) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(16);
            addAllianceInfo(mplew, alliance);
            mplew.writeInt(expelledGuild.getId());
            if (expelledGuild != null) {
                CWvsContext.GuildPacket.getGuildInfo(mplew, expelledGuild);
            }
            mplew.write(expelled ? 1 : 0);
            return mplew.getPacket();
        }

        public static byte[] addGuildToAlliance(MapleGuildAlliance alliance, MapleGuild newGuild) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(18);
            addAllianceInfo(mplew, alliance);
            mplew.writeInt(newGuild.getId());
            if (newGuild != null) {
                CWvsContext.GuildPacket.getGuildInfo(mplew, newGuild);
            }
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] sendAllianceInvite(String allianceName, MapleCharacter inviter) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(3);
            mplew.writeInt(inviter.getGuildId());
            mplew.writeMapleAsciiString(inviter.getName());
            mplew.writeMapleAsciiString(allianceName);
            return mplew.getPacket();
        }

        public static byte[] getAllianceUpdate(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(23);
            addAllianceInfo(mplew, alliance);
            return mplew.getPacket();
        }

        public static byte[] createGuildAlliance(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(15);
            addAllianceInfo(mplew, alliance);
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null);
            }
            for (MapleGuild gg : g) {
                if (gg != null) {
                    CWvsContext.GuildPacket.getGuildInfo(mplew, gg);
                }
            }
            return mplew.getPacket();
        }

        public static byte[] updateAlliance(MapleGuildCharacter mgc, int allianceid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(24);
            mplew.writeInt(allianceid);
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.writeInt(mgc.getLevel());
            mplew.writeInt(mgc.getJobId());
            return mplew.getPacket();
        }

        public static byte[] updateAllianceLeader(int allianceid, int newLeader, int oldLeader) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(25);
            mplew.writeInt(allianceid);
            mplew.writeInt(oldLeader);
            mplew.writeInt(newLeader);
            return mplew.getPacket();
        }

        public static byte[] allianceRankChange(int aid, String[] ranks) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(26);
            mplew.writeInt(aid);
            for (String r : ranks) {
                mplew.writeMapleAsciiString(r);
            }
            return mplew.getPacket();
        }

        public static byte[] updateAllianceRank(MapleGuildCharacter mgc) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(27);
            mplew.writeInt(mgc.getId());
            mplew.write(mgc.getAllianceRank());
            return mplew.getPacket();
        }

        public static byte[] changeAllianceNotice(int allianceid, String notice) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(29);
            mplew.writeInt(allianceid);
            mplew.writeMapleAsciiString(notice);
            return mplew.getPacket();
        }

        public static byte[] disbandAlliance(int alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(30);
            mplew.writeInt(alliance);
            return mplew.getPacket();
        }

        public static byte[] changeAlliance(MapleGuildAlliance alliance, boolean in) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(1);
            mplew.write(in ? 1 : 0);
            mplew.writeInt(in ? alliance.getId() : 0);
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            int i;
            for (i = 0; i < noGuilds; i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null);
            }
            mplew.write(noGuilds);
            for (i = 0; i < noGuilds; i++) {
                if (g[i] != null) {
                    mplew.writeInt(g[i].getId());
                    Collection<MapleGuildCharacter> members = g[i].getMembers();
                    mplew.writeInt(members.size());
                    for (MapleGuildCharacter mgc : members) {
                        mplew.writeInt(mgc.getId());
                        mplew.write(in ? mgc.getAllianceRank() : 0);
                    }
                }
            }
            return mplew.getPacket();
        }

        public static byte[] changeAllianceLeader(int allianceid, int newLeader, int oldLeader) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(2);
            mplew.writeInt(allianceid);
            mplew.writeInt(oldLeader);
            mplew.writeInt(newLeader);
            return mplew.getPacket();
        }

        public static byte[] changeGuildInAlliance(MapleGuildAlliance alliance, MapleGuild guild, boolean add) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(4);
            mplew.writeInt(add ? alliance.getId() : 0);
            mplew.writeInt(guild.getId());
            Collection<MapleGuildCharacter> members = guild.getMembers();
            mplew.writeInt(members.size());
            for (MapleGuildCharacter mgc : members) {
                mplew.writeInt(mgc.getId());
                mplew.write(add ? mgc.getAllianceRank() : 0);
            }
            return mplew.getPacket();
        }

        public static byte[] changeAllianceRank(int allianceid, MapleGuildCharacter player) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(5);
            mplew.writeInt(allianceid);
            mplew.writeInt(player.getId());
            mplew.writeInt(player.getAllianceRank());
            return mplew.getPacket();
        }
    }

    public static byte[] enableActions(MapleCharacter chr) {
        return updatePlayerStats(new EnumMap<>(MapleStat.class), true, false, chr, false);
    }

    public static byte[] enableActions(MapleCharacter chr, boolean itemReaction) {
        return updatePlayerStats(new EnumMap<>(MapleStat.class), itemReaction, chr);
    }

    public static byte[] enableActions(MapleCharacter chr, boolean itemReaction, boolean itemReaction2) {
        return updatePlayerStats(new EnumMap<>(MapleStat.class), itemReaction, itemReaction2, chr, false);
    }

    public static byte[] updatePlayerStats(Map<MapleStat, Long> stats, MapleCharacter chr) {
        return updatePlayerStats(stats, true, chr);
    }

    public static byte[] onSkillUseResult(int skillId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SKILL_USE_RESULT.getValue());
        mplew.write(true);
        mplew.writeInt(skillId);
        return mplew.getPacket();
    }

    public static byte[] updateZeroSecondStats(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_ZERO_STATS.getValue());
        PacketHelper.addZeroInfo(mplew, chr);
        return mplew.getPacket();
    }

    public static byte[] updateAngelicBusterInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_ANGELIC_STATS.getValue());
        mplew.writeInt(chr.getSecondFace());
        int hair = chr.getSecondHair();
        if (chr.getSecondBaseColor() != -1) {
            hair = chr.getSecondHair() / 10 * 10 + chr.getSecondBaseColor();
        }
        mplew.writeInt(hair);
        mplew.writeInt(1051291);
        mplew.write(chr.getSecondSkinColor());
        mplew.writeInt(chr.getSecondBaseColor());
        mplew.writeInt(chr.getSecondAddColor());
        mplew.writeInt(chr.getSecondBaseProb());
        return mplew.getPacket();
    }

    public static byte[] updatePlayerStats(Map<MapleStat, Long> mystats, boolean itemReaction, MapleCharacter chr) {
        return updatePlayerStats(mystats, itemReaction, itemReaction, chr, false);
    }

    public static byte[] updatePlayerStats(final Map<MapleStat, Long> mystats, final boolean itemReaction, final boolean itemReaction2, final MapleCharacter chr, boolean isPet) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
        mplew.write(itemReaction ? 1 : 0);
        mplew.write(isPet);
        mplew.write(0);
        int updateMask = 0;
        for (MapleStat statupdate : mystats.keySet()) {
            updateMask |= statupdate.getValue();
        }
        mplew.writeInt(updateMask);
        for (final Entry<MapleStat, Long> statupdate : mystats.entrySet()) {
            switch (statupdate.getKey()) {
                case SKIN:
                case BATTLE_RANK:
                case ICE_GAGE:
                    mplew.write((statupdate.getValue()).byteValue());
                    break;
                case JOB:
                    mplew.writeShort(statupdate.getValue().shortValue());
                    mplew.writeShort(chr.getSubcategory());
                    break;
                case STR:
                case DEX:
                case INT:
                case LUK:
                case AVAILABLEAP:
                case FATIGUE:
                    mplew.writeShort((statupdate.getValue()).shortValue());
                    break;
                case AVAILABLESP:
                    if (GameConstants.isSeparatedSp(chr.getJob())) {
                        mplew.write(chr.getRemainingSpSize());
                        for (int i = 0; i < chr.getRemainingSps().length; i++) {
                            if (chr.getRemainingSp(i) > 0) {
                                mplew.write(i + 1);
                                mplew.writeInt(chr.getRemainingSp(i));
                            }
                        }
                    } else {
                        mplew.writeShort(0);
                    }
                    break;
                case TRAIT_LIMIT:
                    for (MapleTraitType t : MapleTraitType.values()) {
                        mplew.writeShort(chr.getTrait(t).getExp());
                    }
                    mplew.write(0);
                    mplew.writeLong(PacketHelper.getTime(-2));
                    break;
                case EXP:
                case MESO:
                    mplew.writeLong((statupdate.getValue()).longValue());
                    break;
                case PET:
                    mplew.writeLong((statupdate.getValue()).intValue());
                    mplew.writeLong((statupdate.getValue()).intValue());
                    mplew.writeLong((statupdate.getValue()).intValue());
                    break;
                case BATTLE_POINTS:
                case VIRTUE:
                    mplew.writeLong((statupdate.getValue()).longValue());
                    break;
                default:
                    mplew.writeInt((statupdate.getValue()).intValue());
                    break;
            }
        }
        mplew.write(chr == null ? -1 : chr.getBaseColor());
        mplew.write(chr == null ? 0 : chr.getAddColor());
        mplew.write(chr == null ? 0 : chr.getBaseProb());
        mplew.write(itemReaction2);
        if (itemReaction2) {
            mplew.write(1);
        }
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] temporaryStats_Aran() {
        Map<MapleStat.Temp, Integer> stats = new EnumMap<>(MapleStat.Temp.class);
        stats.put(MapleStat.Temp.STR, Integer.valueOf(999));
        stats.put(MapleStat.Temp.DEX, Integer.valueOf(999));
        stats.put(MapleStat.Temp.INT, Integer.valueOf(999));
        stats.put(MapleStat.Temp.LUK, Integer.valueOf(999));
        stats.put(MapleStat.Temp.WATK, Integer.valueOf(255));
        stats.put(MapleStat.Temp.ACC, Integer.valueOf(999));
        stats.put(MapleStat.Temp.AVOID, Integer.valueOf(999));
        stats.put(MapleStat.Temp.SPEED, Integer.valueOf(140));
        stats.put(MapleStat.Temp.JUMP, Integer.valueOf(120));
        return temporaryStats(stats);
    }

    public static byte[] temporaryStats_Balrog(MapleCharacter chr) {
        Map<MapleStat.Temp, Integer> stats = new EnumMap<>(MapleStat.Temp.class);
        int offset = 1 + (chr.getLevel() - 90) / 20;
        stats.put(MapleStat.Temp.STR, Integer.valueOf(chr.getStat().getTotalStr() / offset));
        stats.put(MapleStat.Temp.DEX, Integer.valueOf(chr.getStat().getTotalDex() / offset));
        stats.put(MapleStat.Temp.INT, Integer.valueOf(chr.getStat().getTotalInt() / offset));
        stats.put(MapleStat.Temp.LUK, Integer.valueOf(chr.getStat().getTotalLuk() / offset));
        stats.put(MapleStat.Temp.WATK, Integer.valueOf(chr.getStat().getTotalWatk() / offset));
        stats.put(MapleStat.Temp.MATK, Integer.valueOf(chr.getStat().getTotalMagic() / offset));
        return temporaryStats(stats);
    }

    public static byte[] updateHyperSp(String value, int array, int mode, int table, boolean skill) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.HYPER.getValue());
        packet.writeMapleAsciiString(value);
        packet.writeInt(array);
        packet.writeInt(mode);
        packet.write(skill);
        packet.writeInt(table);
        packet.writeZeroBytes(120);
        return packet.getPacket();
    }

    public static byte[] temporaryStats(Map<MapleStat.Temp, Integer> mystats) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TEMP_STATS.getValue());
        int updateMask = 0;
        for (MapleStat.Temp statupdate : mystats.keySet()) {
            updateMask |= statupdate.getValue();
        }
        mplew.writeInt(updateMask);
        for (Map.Entry<MapleStat.Temp, Integer> statupdate : mystats.entrySet()) {
            switch ((MapleStat.Temp) statupdate.getKey()) {
                case SPEED:
                case JUMP:
                case UNKNOWN:
                    mplew.write(((Integer) statupdate.getValue()).byteValue());
                    continue;
            }
            mplew.writeShort(((Integer) statupdate.getValue()).shortValue());
        }
        return mplew.getPacket();
    }

    public static byte[] temporaryStats_Reset() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TEMP_STATS_RESET.getValue());
        return mplew.getPacket();
    }

    public static byte[] updateSkills(Map<Skill, SkillEntry> update) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_SKILLS.getValue());
        mplew.write(1);
        mplew.write(0);
        mplew.write(0);
        mplew.writeShort(update.size());
        for (Map.Entry<Skill, SkillEntry> z : update.entrySet()) {
            mplew.writeInt(((Skill) z.getKey()).getId());
            mplew.writeInt(((SkillEntry) z.getValue()).skillevel);
            mplew.writeInt(((SkillEntry) z.getValue()).masterlevel);
            PacketHelper.addExpirationTime(mplew, ((SkillEntry) z.getValue()).expiration);
        }
        mplew.write(7);
        return mplew.getPacket();
    }

    public static byte[] OnFameResult(int op, String charname, boolean raise, int newFame) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FAME_RESPONSE.getValue());
        mplew.write(op);
        mplew.writeMapleAsciiString(charname);
        mplew.write(raise);
        mplew.writeInt(newFame);
        return mplew.getPacket();
    }

    public static byte[] LieDetector(MapleClient c, int type, int count) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        if (type == 0) {
            String packet = "";
            mplew.write(HexTool.getByteArrayFromHexString("8D 35 5C 0B 0E 00 00 00 76 DB A8 78 8F DA A8 78 8C DA A8 78 CF CF"));
            mplew.write(count);
            int rand = Randomizer.rand(0, 12);
            if (rand == 0) {
                c.setLIEDETECT("이드땅굴두더지");
                packet = "BA 16 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F6 E9 2D 26 86 EE 39 E2 96 E2 57 2B 24 43 3B 4A A6 F6 DE 19 C6 57 70 5C 05 50 39 01 8F 5C 92 24 86 EE DE FA DE 2B 84 80 C9 22 C4 B7 31 C7 F2 B3 2E E5 60 36 B6 76 E4 8D C3 21 B1 CF 5C 1C D4 51 B3 D9 CD E5 05 94 C6 0C 61 B7 34 B3 61 49 64 52 18 A9 C9 F9 50 B7 3C 64 B1 C7 DE 6F 2D B6 D5 FC 4F F1 27 5E D5 34 BB 5D 68 68 FA 65 88 01 A4 B7 B6 25 9D D1 F8 6C E7 28 49 19 C6 F1 80 31 F3 60 9A 71 D5 D9 74 04 B9 55 D1 B1 F1 37 46 D7 24 D2 26 BF B2 F1 05 EC 5F 61 8C 4E B6 D1 CB 83 30 56 05 98 AC 68 A4 15 3B 4E 49 61 FE EF 52 DF 07 78 FE 1D 57 C1 D6 F2 EB 53 4B 73 A8 45 3B 45 28 58 B0 24 5C 7F 10 05 55 86 D6 C6 0F 7C 1C 1C 54 5E 08 D6 F5 1D 72 F3 59 F0 7F 88 A7 83 51 9E D2 16 F2 AF 22 61 F3 29 5F 2C E1 80 EB 87 E1 B1 9E 4E 73 5C 9F C3 DB 01 65 E2 8F 10 F8 56 F5 57 ED 3E 54 82 02 1B 3F 3A 36 70 0F 19 0C 00 24 77 D8 33 D0 56 B1 56 4E 2F 6B 5D 7E A4 C9 DD 27 D9 D8 F7 2D 36 49 25 B1 DC 26 92 65 6F 9A 29 E6 DB 97 56 1B 81 21 42 81 8C ED C7 5F 97 DF 35 0E A9 79 6F A7 C1 FD A7 7D 70 F6 D6 F6 A1 C9 47 9E 38 A3 76 3F 2A EE 2C C0 64 F3 B4 16 0B F3 72 32 06 32 FC 1D 7B 1D D6 8B 0C 37 1E 5F 9B 6D 31 8E 2D C7 E6 FB A4 8C 67 BE 0B 0E 3B 03 EF 52 78 C3 4D D3 35 4F 0C 5D DA EB 51 F9 F0 45 04 97 59 65 70 8A 50 67 24 A1 1D 33 C2 EE C9 00 F5 C1 35 95 56 E0 DB 5F D7 F4 8A A7 17 2B 24 72 77 5F 12 E6 BF B5 4D 1F C0 D6 53 6B FA B2 46 A9 25 E0 89 D2 DA 13 82 0B 33 48 73 93 B7 8D CD 83 9F BC C7 83 AF E0 8F 0E 78 9A CC DE 5F 78 A7 C4 52 EA 33 DD 18 D9 60 86 42 21 8F 69 0C 19 08 23 83 C0 20 2A 83 82 0E E0 6B C8 FE 1C FC 42 BE F0 F7 86 2E 34 3D 1B 49 9B 53 D5 AE 2E CC 90 47 B4 98 E2 52 AA 32 70 72 79 1F 77 8E E7 77 6A F4 4F 08 69 5F 11 61 B9 BE BF F1 05 D2 DC 4B 72 22 09 05 C3 AB C5 11 DD F7 96 35 20 02 B8 04 E3 6F B6 E3 D2 22 F5 BA D7 4F E9 24 12 77 FB FF 00 A7 FD 7D C7 A0 4B 1C E9 A5 10 B1 E2 E4 83 29 1F 6B 6D 89 27 2F 83 21 1B B6 6E E3 EE 9E 0E 36 E3 8A F2 3F 11 78 EF 5A D2 AE AD EF 74 AF 15 7F 6A 69 8B 73 E4 5C A0 B4 85 4A BA E3 20 30 5E 55 C0 62 A4 76 EE 48 AC 9B 98 83 F8 9F 55 B5 F8 87 AA 5C 44 D1 59 E6 DF CB 91 CC 6C 4F 09 85 C1 63 D7 70 18 CE 41 DD DE A2 D1 2C 75 DF 11 78 0E E7 41 B4 D1 AD AD ED 6D A6 6B 8B 8B FB 96 29 BA 45 00 05 51 C0 0D 81 82 79 EB CE 09 C9 39 9B 7C FF 00 3F 2D C1 FF 00 2B F4 FC 0F 72 37 16 1A EE 8F F6 BB 7B D6 16 8C 4A 99 92 69 22 18 59 30 FC AB 29 07 28 46 73 F9 82 41 A6 E9 AB E8 56 2E A9 33 6A 76 88 18 B4 B2 CA B1 DD 44 0E 49 6D CD FB B9 30 49 23 3E 58 55 5C 7C D5 E6 BE 0B B1 9E 2B 4F 0D EB F7 DA 96 A7 22 45 34 90 88 25 94 F9 50 81 94 1B 54 8E 9B 7D 3D 08 AF 63 B9 6D DB 20 4B A5 82 49 77 00 70 0B 91 B4 F2 99 E3 20 90 79 04 71 D3 9A B6 AC DB 44 26 ED 62 1B 3D 5A D2 F6 32 D1 34 82 55 DB E6 5B BC 6C 25 8B 71 C0 DE 98 DC A0 E0 F2 46 30 33 9C 73 50 DB 5C 58 3C 96 51 DA 3C 51 C5 1A 2A C5 6C C8 22 C2 B4 79 42 8A 57 3C 2A B0 00 60 63 7E 79 5C 0A 7A 8E 9B A1 6C B5 6B B9 1A 21 A6 A3 45 1C B1 4C E8 60 42 17 20 BA 9C A1 DA 10 96 C8 3B 43 73 B4 B6 78 BD 5B E2 3E 9D A4 CA 1A 1D 5A 1D 75 ED 26 25 23 96 13 04 C0 E0 A3 0F 30 42 51 87 CC 4E 57 CB 20 2E 32 F9 20 AE 64 9D 8D 1A F7 5E 9F D7 A1 E8 72 5A C2 F6 72 2D BC 29 3C 6D 28 32 84 98 AB 3E CC 29 DC D9 F9 DB E4 0A 43 10 08 1B 5B 8C E5 D0 49 E5 5B C5 67 6F 6C 2D C8 12 47 0A A4 4C 62 89 10 ED 52 78 50 06 36 E1 47 5C 9D B9 00 B5 73 DA 2F 8B 2F 35 DD 37 ED DA 6C 3A 65 D1 79 17 7C 03 50 90 B5 B8 6C 2A 82 05 BE E1 96 0C 4E E1 81 82 72 57 A7 31 F1 13 59 D6 AC F4 5B 49 D6 EC 5A DD 1B B7 8E DD F4 ED 51 9F 7B 6E 3B 91 D3 CA 40 55 71 B7 A9 20 80 0E 72 4D 4C A4 A2 AF B9 0A 77 77 6D 9E 8C EB 16 A0 44 F1 5B B9 93 EC B2 22 3C 9E 64 0E A1 C8 F9 41 DB 95 C9 41 92 39 5C 29 C7 22 AD 3A 4C 8C 8F 16 C9 08 C0 6F 30 90 70 4A E4 82 33 8E 01 38 C7 27 1C 8E B5 E5 57 F6 1F 13 DA D8 1B 7B 9B BB A9 64 2A 65 86 EA D6 C5 63 5C 72 0A FE F1 BA 1C 7F 08 F5 AE C2 CB 58 D6 6D 74 48 DB 59 D0 75 27 D4 3E 52 EB 04 F6 F8 91 D5 72 7C B0 25 04 8C 26 4A 81 CE 4F 04 66 92 6A DA 8B DA 7F 5F D2 35 AE 56 19 27 9A 4B 5B 71 2D DD AB A9 69 02 2B C9 B8 02 FE 58 2C 46 09 49 1C 02 48 0B E6 F1 D4 E2 C2 58 C5 16 E9 9E 19 25 96 6C AC A8 66 69 17 0C 46 46 18 81 B4 76 18 E0 6E C0 CB 10 6B 26 AD 75 BB 64 7E 1D D4 FC A0 0E C7 DD 6E AA 40 1C 60 19 43 0C FA 10 08 CF 38 AE 77 C5 57 1A 8E B7 E1 D9 A3 B2 83 57 D2 65 59 77 0B B8 A4 B6 72 A5 03 07 03 6C DB 86 30 C0 EC F9 BA F0 79 04 9C D4 57 36 E9 17 07 7D 3F 42 2D 73 C6 7A 46 81 AF 5C 69 17 A9 73 7B 73 76 C9 12 D9 DB C5 1C CC EA C4 90 AC CE DD 09 66 1B 5B 00 06 50 A0 00 58 F1 FF 00 13 5A C1 BE 22 78 5F 50 B6 54 CB DC 6C 9E 44 43 96 68 A7 08 41 EE 48 20 8F C3 E9 55 FC 21 7B E1 9D 3B C0 06 FA F2 F2 6D 1F 5B BC 69 63 FE D8 FB 14 B3 BE ED CD F7 5C A9 00 ED 38 21 48 3D F2 0F 35 CE 78 AB 42 8B 4D D6 FC 3F 3C 1A 9E A7 A9 DC 5F 95 96 4B FB BB 67 FD E9 DC BB 76 2B 02 CD 85 C7 1F 37 6F 5C 56 77 F7 A3 7E EB FE 1B FE 09 69 A7 19 5B B3 FF 00 87 FF 00 86 3D EB 58 D7 6E F4 AB 94 8E 5D 39 64 B6 99 C4 71 CC B7 1B 4E 4F 62 31 90 7A 9F CB BD 5E 8D 04 7A A4 AF 0C E2 34 96 5C 4F 14 D1 36 65 93 CB 5C 18 D8 90 3E E8 19 C0 61 F2 9E 84 35 79 DF 8C 3C 5A B6 69 05 9C 9A 0E A3 6B 6D 05 D4 7F 62 94 5A 6D 8D E2 F2 C6 00 04 8C 36 49 1B 38 20 01 9C 1C 81 63 C4 1E 3C B9 FF 00 84 58 EB BA 4D BC D0 3D AD F8 8A E6 DA FE D1 55 64 C8 CA EE 1B 8B 65 7E 4E 55 B3 9E D8 E9 BC 13 D6 FB 7E 9F F0 E6 32 A9 15 A7 97 F5 F8 1D EC 7A 65 8D CA F9 F7 10 0B A7 94 97 0F 75 10 2C AA 79 0A 01 03 68 03 8C 60 1E E7 24 92 4A 83 49 96 DB C4 3A 2D 8E AB 71 A7 44 AF 73 02 C9 B2 55 57 2A 08 CE 33 8E 9C FE B4 54 CA 9A E6 77 4B FA F9 1B 46 BC DA 4E 32 76 F5 7F E6 69 48 F2 AC 40 F9 6C CD E6 01 B6 26 04 ED DD 8C FC D8 E3 1C 9E FD 71 93 8C F8 FF 00 89 FC 1B FD 8F E3 23 7D A1 EB 6F A6 F9 FB EE EE 44 B1 86 8A 25 C3 EF 70 3B E1 0B E1 4A F7 3C F3 C7 AB 5C 35 AB EE 92 7B D4 4B 79 48 B7 4D B3 B4 67 CC CB 21 50 C1 87 CC 4B 60 00 01 04 75 24 0C 73 FA EE 91 35 AD CD 8E A5 00 B4 77 B5 6D AB 00 51 12 94 52 CC 81 41 27 95 41 DB FB B9 00 74 1A C1 59 D9 91 AB B9 89 E1 CF 86 7F D9 7A 65 E4 AF A9 5C CB AB DD AE DB 99 5B 72 29 27 25 D7 9F BE A7 23 9E E4 67 3C E0 70 B3 CB 7D 6F F1 3F 48 D5 B7 BA CF 77 28 B7 79 46 14 F9 84 79 7C FC A4 03 82 BD 41 E9 92 0D 7A 69 F8 87 A6 6A 5E 44 7A 35 A3 EA 7A 93 E4 79 10 AE F3 10 E3 E6 2C 01 50 BB F6 0C B1 51 DC 91 8A CA 9B C3 1A AD DD BE 9C 67 4B 6D 3D A1 BE 5B FD F7 53 AC 92 B4 AB C2 AF 94 A0 29 05 C8 03 13 7F 1E 4F 24 28 B8 4E 4A 57 9F 4F EB 6F 42 39 7D D7 E7 FD 2F C4 D9 D0 C3 69 DE 2F BE D3 E6 08 A9 70 3C D5 41 C8 DD C3 0C 1E 3A 65 BB 76 1D 2B 6F 52 D5 6D 2C 6C 3E D5 AB 48 96 76 D3 32 C3 24 37 92 C2 A1 41 62 09 EA 41 CE 46 46 EF BB D3 07 20 F2 1A BE 9B 7F A4 EB 5A 56 A9 7F 7D 75 70 62 93 0D 87 55 88 00 C7 00 10 03 9F 97 3C 39 7E B8 2C E4 66 B2 B5 DF 02 F8 27 52 F1 8D E6 A1 AB 5D 5E 49 7C 64 0D 36 99 61 2B DD 3B 0E 4E F7 44 56 95 51 94 C6 3A 28 53 C0 24 15 35 95 44 DC 53 F9 32 92 77 DC C6 93 C4 7A 2C 7E 26 D3 62 F0 0E 8A FA 99 80 92 FE 4C 42 DA 29 64 CB 31 CB 15 03 3F 45 03 A0 1C 60 53 B5 CD 2F C4 1E 27 53 FD BF E3 3B 68 63 8D F6 BE 93 A2 87 98 18 D5 4B 67 EF 7C CE 5A 3C FC E4 85 C8 24 8E 16 BA DF F8 45 46 AB 04 96 16 1E 1B 3A 35 BC 2B E4 A4 DA 95 D3 C8 D0 92 37 7E EA 08 DC A3 2F CC 39 F3 14 06 27 20 ED C1 D9 B4 F0 7D B3 48 F1 DE DC 4F 74 53 69 65 05 61 B7 3B 8F EF 23 31 C4 55 A4 04 2A E4 4C 64 E0 AF 2D F3 67 34 9D FF 00 AF EB FA D0 77 95 EF FD 7D E7 1B AA E8 F6 13 EA 4F A9 7F 66 4D 32 C2 C9 6E 2E 75 0B 97 92 51 22 8D DF 32 33 12 A4 82 08 0C B8 DB 82 00 CF 38 9F 10 65 BA 8F C4 16 31 6A F0 CA 34 98 80 8E 59 12 77 99 66 7C 07 C3 3E D5 27 82 BC 0E 83 38 E6 BD 3F 51 B5 D1 20 D3 2E 74 ED 3A D9 1E 54 53 1C 56 B6 30 83 F6 72 58 12 36 A0 C4 60 B7 CC 49 C6 4F AE 00 14 B4 8D 26 FE 6D 45 63 D7 DE 21 0E CF 2E 2B 59 AE 37 39 3B 70 0A AA 9C 0F 95 1B DF E5 27 1D 4D 34 EF BF 72 65 1B 6A BB 1C 07 82 ED E6 BB FE D7 B3 D3 5A F2 6D 15 18 CD 65 15 C9 00 82 33 99 38 1E 9F 2E 07 04 B8 EE 2B AB D4 EC B5 BB 8D 3B 4E D7 3C 36 4C D7 C8 76 DD 45 29 CF 9C 77 A2 81 9C 82 BF 81 51 B7 39 23 BE B5 BD A4 93 DA 94 D1 A5 9A D2 3B 06 45 95 DC 96 92 72 08 DE 59 53 86 20 64 E0 02 18 E0 70 0F 15 63 D7 0F 84 EC B5 38 35 0C A7 D9 E3 F3 20 73 19 28 92 30 F9 77 6C 56 2B 92 C0 13 C8 18 C7 D5 BE 56 AC DF F5 FF 00 04 71 8F BD 61 D6 9E 2A F1 16 99 E5 2E B9 E0 5B B5 62 AE 4C DA 56 D9 F2 CC D9 3F 20 27 68 27 93 96 39 3C D7 2B AA 6B BE 1C 17 92 DC 47 A0 5A 4B E2 6B C0 0C B6 DA A4 6B 1C 16 A7 82 C2 43 2E C0 58 8E 84 0E 70 3B 92 5A ED A7 8C B5 F5 B4 9E E2 DF C6 1E 15 BD B6 49 1D D1 AF C3 C1 3B 28 1F 77 CB 55 5F AF 00 E4 F7 23 8A CC F8 75 E1 E8 BC 4E BA 85 F6 A1 65 78 B2 DC 48 1D EE 5D 87 D9 E6 F9 CB E0 26 CC 36 1D 57 2A 49 04 67 A7 43 1F 13 34 56 8E BF 2F 22 0F 0C 78 43 5E B5 B4 BA D6 74 3B 8D 03 50 BD 60 9B 60 82 7D CF 01 3C 9D AE A5 42 30 E9 8C 95 60 58 1C 8E 09 3F 89 65 D4 61 B5 D3 FC 53 E0 EB 8B FB 33 BA 5B 29 E0 CA BB 9D A5 9D D5 A2 0A 92 A9 C8 39 5C 00 BC 92 DD 6B BE D1 B4 6D 37 C2 27 52 D4 2C 2C AD FC A8 63 22 EE 60 F2 46 C3 69 25 C6 C6 2C 38 00 30 FB A0 86 18 C8 34 69 5A DC 77 9A AC 3E 6D A0 96 24 1E 5D 84 51 ED 89 23 F9 09 E1 18 E0 C8 50 3F F1 70 A0 80 07 CE 5A 67 4F A3 D3 42 64 B5 72 3C 8A DD BC 12 1D A6 BB B1 F1 1D AB 40 CA A5 AD 0A 61 5F 2C 70 DB C9 2A 7E 5E 39 E7 07 81 8C 9F 6C 8B 59 FB 7E 83 1E A7 A6 6B 70 BD 90 CB 89 E5 B5 61 F2 A3 00 C2 46 3C 28 F5 3B 47 19 3C 01 91 E5 BE 01 D2 F4 FD 4B 52 D5 35 6D 69 04 11 4B 71 E5 40 E2 36 2A AE C5 89 D8 E3 E5 52 A4 A1 C9 04 01 9E 07 51 E8 AF A2 DE 5A F9 53 68 37 16 D3 58 41 39 96 38 2C A0 86 37 8D CA 14 24 63 6C 72 13 B8 F5 29 81 FD E3 8A 54 94 B9 17 9F 62 6C AF F8 1D 0C 51 6A 12 C3 6E B7 0A 77 46 A5 24 26 E3 61 73 F2 A9 7F 91 7B 8F 31 80 C8 FE 1C 80 4F C9 57 52 D1 A5 93 C3 77 BA 3E 9C 65 8E 43 65 F6 7B 69 A5 93 72 A6 43 2A 8E A4 E5 41 E5 B1 92 08 E5 88 A7 E9 3A C9 BD 8E 34 B7 B4 B8 30 82 62 12 4F 95 65 2A C7 89 14 E5 D4 98 F6 BA 96 FB C1 86 76 92 33 7A 51 38 B8 9A 56 51 1C 22 36 5F 32 13 BE 46 00 29 53 B7 61 E4 13 26 00 27 B7 07 76 17 57 EF 41 C5 E8 82 2E CF 99 2B DF FA F4 3C 47 43 F8 61 26 97 AF E8 F1 EB F7 16 F7 99 9B 9B 28 98 B2 27 DD 6C 39 C7 27 E6 CE DC 60 F1 C9 06 BA FF 00 89 1A 59 D5 F5 AF 08 B5 AC 37 2E D1 5E CA BE 54 20 47 29 54 C3 31 5D F8 03 1E 59 C1 24 0E 41 07 91 5B 3E 2A 8E 78 F5 6D 31 81 BC 90 F9 92 61 A2 45 69 00 38 38 4C 63 A0 CE 33 CF 19 39 EF AB 14 3A 96 A9 30 5B B9 1A DA D5 1C 99 2D 4C 64 B4 A8 C0 E1 4C 83 03 83 8E 17 3C 0C 36 49 CD 67 15 A7 2F 66 5D ED BE ED 1E 53 E3 5D 0F 5D D4 51 75 6B 9B 38 22 B4 57 C4 36 69 3F DA 24 76 07 0C 24 90 12 4B 61 7A 02 40 E8 30 73 93 C7 3E 24 5F 11 78 67 42 D2 60 B4 9A CE E0 DD B4 53 5A CE EC EF 13 20 08 15 99 B9 27 E7 C9 CF 3E BE FD D7 89 2E E3 9E 24 B4 85 E2 6D B7 91 98 52 10 48 8D 7C B0 02 31 C9 5D DB 83 70 BC 60 7A E4 9C 18 B4 BB 9D 7F E2 A4 17 37 12 9B 9F EC 98 8C D7 0A 98 C2 C8 1D 82 46 A7 38 EA 01 ED D0 E6 BA 69 F6 92 D1 3B FD C8 E3 AA AC 9B 83 D5 AB 7D FB 7F C3 7A 9D 54 29 77 A0 C6 2D 60 BA 86 35 21 72 2E 13 2D F2 A8 8C 11 B4 91 B4 88 C1 1C E7 07 9E 73 45 73 FA E5 D5 CC D7 EA 2E 0B 89 D2 24 59 15 9D 58 A3 11 B8 AE 54 00 70 49 19 03 9C 51 4D 42 72 5C D7 DF C8 25 56 9C 24 E1 6D BC CF 48 89 25 8D FC BC 2F 90 AA 15 09 76 67 E0 0E 49 3D 7F FA D9 C9 CF 1C EE AD A3 69 32 36 FB E8 24 9A 3B BB 93 08 6B B9 A5 B8 48 5D C1 0A E9 14 8A F1 A3 6F C2 AE 40 5C 37 07 9D A7 A5 30 C4 D3 A4 E6 24 33 22 B2 2C 85 46 E5 56 20 90 0F 60 4A AE 47 B0 F4 AA CA 8C F7 B7 31 49 34 AF 1E 63 99 06 76 F9 7D B6 82 B8 25 72 99 C1 CE 77 30 E9 80 33 D5 6C 74 F5 BB 1A 2D 6D DE 2B DD B3 05 8E E9 D8 3B 42 44 6C 1B 02 33 F3 2E 0E E0 57 19 CE 41 E3 B0 C3 44 76 96 1A 93 CA D2 CC AF 73 1A AE 64 2C D1 8C 48 70 37 9E 03 16 9B 01 49 E4 00 14 61 4D 72 BA AF 8A F5 3D 3B 65 AC 66 17 6D B2 21 99 D3 E7 25 65 78 C3 71 85 CE 10 1E 98 CE 78 C7 15 A1 A3 E9 71 EB FA 54 37 FA B5 C5 CD D3 4B B8 98 5A 5D B1 29 0C 54 10 AB 8C 1C 0F D4 D5 A8 D9 5F A0 94 94 B5 45 2F 16 CB A5 DD 68 C9 A7 EF 82 15 8D 19 05 A8 60 C1 76 E1 44 6C 8A 19 40 C6 7A 91 8C 01 D0 B6 2B D9 78 98 69 FA 3C 56 7A 26 99 68 91 C1 0C 78 84 34 71 95 66 20 92 22 0C 0B 2B 97 4C 11 8C 97 EF 9A E8 AF 2C D3 44 D0 92 1D 39 BC 89 9E 4B 6B 66 B9 58 D3 CC 21 A4 48 CB 9F 97 69 6C 31 3C 8C 64 F4 ED 54 35 1D 7E ED 64 B1 8C 2C 5B 25 BA 92 39 17 69 F9 95 6F 22 B7 00 F3 FD D9 49 3E A4 0E D9 52 E2 D3 F7 12 D4 B7 06 A3 CE C9 EC 6E F5 7D 77 4B 84 C3 6E BA 7C 4D 1A 32 5D 4C 44 AC E7 6F DE 58 CE 7A 37 20 B3 76 07 9C 91 55 B5 8D 06 FB EC 33 4A 35 69 1D 65 8D DA E1 6F D8 F9 31 7C 84 E4 6D 75 08 BC 63 91 26 38 20 70 49 D9 FE D2 9B FE 11 4F ED 5D B1 F9 FF 00 61 FB 4E DC 1D BB B6 6E C6 33 9C 67 DE BC F3 47 D5 B5 2F 11 F8 99 2D 6E 35 0B 9B 68 2E 9C BB 25 AC 85 02 14 46 23 6E 73 8E 79 3E A7 93 CE 2B 2A 89 5D C5 8A 16 6A EB 66 75 36 DE 28 4D 35 6D E2 D6 74 F6 D1 5B 2C 3C 9F 32 39 E3 0B C8 0A 82 26 DC 01 DB BB 73 26 07 CC B9 C9 15 14 7A ED EE A2 B7 9F D9 3A 1D C5 D3 DC 29 53 7A D2 B4 29 20 55 0A 19 09 39 51 B8 BE 11 58 11 C9 CE E6 6C 5A 9B 44 D2 FC 37 15 B4 96 7A 7C 12 4C CC 41 96 E0 19 1B 29 14 8E 18 64 FC A7 72 0E 98 FE 58 BB AF 49 3E 9B 1A 5F C7 73 34 86 4B AB 7B 5F 21 CE 23 54 9E 78 22 62 36 E0 EE 03 71 52 49 20 BB 75 18 01 2B AD 1B E9 7F C6 DF 9A 1D D3 4D 25 FF 00 0F 6B 98 E3 C2 F7 96 F0 1B FF 00 0A EB 52 46 97 06 29 C4 17 13 99 A2 97 90 DC C8 4B 36 DC 67 EE 91 BB A1 6C 1C 8A 1E 2A F0 E7 88 6F 3C 3D A9 D9 A4 31 DF 5C DE 32 31 BA B6 94 46 CF B4 82 14 C4 F8 08 80 02 32 24 62 4E 0E 30 70 BD 1E AF 76 ED A7 EB 18 48 D6 48 6E 21 B7 49 02 FC C0 11 1B 06 CF F7 94 C8 C5 4F 62 07 1E B2 F8 8D DB 4F D3 05 DD 9B 79 13 46 55 14 A7 00 AF 23 04 74 20 64 E3 3D 3B 51 38 FB B7 7B 7F C3 17 CB 28 CA CF 7F EB FC CF 15 B4 B8 92 EA EE 3D 1F 56 F0 76 90 22 79 44 28 23 C5 94 E1 C2 60 04 95 9B E7 E5 49 3B B7 02 CC 01 3C AD 7A 7D 85 8C 76 37 DF D9 FE 19 B5 FB 3D 9D 9B 97 94 30 12 83 20 04 7C A5 CF 52 3E 5F BC 38 3D B1 56 FC 32 F6 FA C5 95 C5 9D DE 9D 60 60 88 20 D8 B6 EA 15 BE 5D 99 2B D3 EE A8 5E 9D 38 E9 51 EB 3A 15 96 87 A5 5C 4F A6 79 D6 A1 E7 49 3C 98 A5 61 12 1E 3E EC 79 DA BC 8D DC 0C E7 F0 A4 F4 57 FC 7F AF EB E4 2B DD F2 D8 97 52 8B 59 B9 9E 6B 79 DE 57 D3 D4 9F B4 34 01 23 0C A5 54 ED 5D F8 38 18 C9 F9 98 1D EC 32 71 B5 6D E8 F3 D8 DD 3B DA 5B 41 35 AC 06 00 CB 13 46 63 2E 1B 82 FB 83 1C E4 6C C1 E0 8C 1E 4E 78 7F 86 DA 7B ED 1E 3B CB 8B 97 69 66 9D E4 7D AA 8A 1B 69 28 01 C2 82 47 CA 0F AE 78 CE DE 2A EB 5C 9B 7D 72 D3 4F 8A 38 D6 19 ED EE 2E 1B 0B 83 BD 5E 2F C3 9F 31 89 F5 35 A3 83 6D C5 93 2F 75 9C B4 30 C2 FA 9E 93 A4 5A E9 72 E9 D6 56 8C C5 2D E5 45 0C 76 93 96 CA B3 64 12 0F 27 92 72 79 04 1A DD 5B 15 D4 A3 8E F3 50 B7 B5 F2 DE 15 66 69 60 68 E4 0A 53 E6 56 05 BE 42 18 03 9C 9C 02 57 00 8D C6 1B 1B 68 9B C5 5A 8A 60 81 1C 0A 8B 86 20 80 C3 24 E7 AE 49 C9 CF 5E 4D 6B 58 C8 5A 6B E8 79 DB 0D C6 D5 25 8B 13 B9 15 CE 49 27 BB 9C 0E 80 60 0E 95 9A 49 DD F7 BF F9 7E 84 A4 EE C8 B4 FD 3D 6C 6C 8F D9 53 CA 79 23 07 C9 69 5D E3 47 C7 6C E0 E3 27 9E 07 4E 82 AD 38 F2 91 52 29 23 8D 9E 4C 8D EB 9D DC EE 60 00 23 92 37 73 F8 9C E0 E6 A5 C4 A5 B5 7B 5D 3D C0 68 A6 B7 B8 98 BF 21 D4 AB 46 A3 69 18 C7 12 B0 CF 5E 9C F5 CC 3A 3D C4 B7 1A A6 B5 14 8D B9 6C 6E D6 DA 2C F2 70 D0 C5 2B 12 4F 39 2D 29 18 1C 00 AA 00 18 39 BE 56 AC C3 D5 17 D2 5B 8B 9B 71 24 21 60 DC EA C9 E7 46 C4 98 F2 0B 65 7E 52 AC 46 E0 33 D3 82 47 55 A8 65 83 ED 88 A2 48 61 B9 64 94 83 BC 34 69 B0 90 78 1F 30 7C 29 5F 62 C3 F8 7A 0B 4D 6F 13 CB 14 A5 48 78 B3 B0 86 23 19 18 23 8E A3 D8 F1 90 0F 50 30 E9 22 8E 5D BE 64 6A FB 58 32 EE 19 C1 1D 08 F7 A1 36 81 A4 D5 8C DB 9D 36 3F B3 45 12 59 AB A4 17 69 24 2A 18 BE 01 70 59 B0 59 71 8D CF 81 92 00 03 00 F0 95 45 23 B5 D4 E5 B0 D6 E3 92 4B 79 93 2C E1 62 0E CE A3 70 E4 02 DB 72 15 87 3C F3 8E 1B 81 BA 2D C0 88 A9 72 58 33 32 B9 55 CA 16 CF 4E 31 C0 24 74 FA E7 9C D4 D5 55 A0 D0 6E 84 72 B2 BA 44 4E F0 17 2C 7A 9C F1 8E 79 CE 07 73 8C 51 77 D0 1A BE E7 31 A7 E8 AF AE AD C5 F9 91 17 CC 9D C8 DF 19 27 1F 83 0A 2B A4 F0 E4 6B 1E 81 66 14 75 4D C7 EA 4E 68 AE 8F 6F 38 7B AB 64 72 C7 0D 09 AE 69 6E CF FF D9";
            } else if (rand == 1) {
                c.setLIEDETECT("러셀스퀴드");
                packet = "06 17 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F7 6B 78 A4 58 71 2C C5 AE 09 53 33 A2 95 56 60 17 3B 55 8B 6D 53 8E 80 9C 64 F3 9C 9A 86 DD 3C BB 98 AD 52 15 FB 35 B4 23 61 70 E5 90 82 55 70 CC 30 D9 50 73 CE 47 19 C8 70 43 21 26 CB 43 57 8D 56 D8 25 B1 91 9A F1 80 28 F8 DC 4C BB 78 CE 49 2C 41 EB 9E B9 CD 4B 71 6C AB 1D C3 E2 35 79 1F 72 6D 91 A0 DE C5 02 00 EE A7 24 9E 80 E3 8F 97 03 2A 0D 12 5D 02 2D 35 79 11 43 75 14 A6 DE E9 26 B8 54 BC 98 F9 48 19 65 49 40 46 C3 02 37 05 46 55 0E 30 47 41 D1 98 83 64 DB A9 89 C2 A9 94 79 65 04 73 3B 10 DC 60 86 CE 72 0E 07 38 3D CF 39 35 C8 78 D6 7B DB 49 BC 2D 74 97 13 46 D7 1A ED BC 32 40 C4 6D 11 B8 E5 19 41 2A C4 15 07 27 71 0D CA 91 C5 74 ED 62 96 A5 A4 B6 92 1B 4C EC 33 4E 57 7C 92 AA 6D FB EE C7 27 E5 0C B9 39 3C 83 91 8A 7C AA CA 4F A8 59 35 61 F7 16 C6 F6 59 D5 6E 84 60 42 D0 6E 85 40 9A 26 61 92 43 F3 8E 0A 1C 63 A8 07 DA B2 75 8F 1B E8 5A 3E 9E 67 BC D5 21 B3 69 01 16 ED 24 6D 28 93 23 2B 22 AA 73 22 10 41 CA 9C 73 82 41 E0 68 28 7B 25 92 EA E7 CF 71 6A 8E AF 20 4E 67 52 10 F9 9B 11 B0 48 0B 83 F2 EE C8 3B 40 07 0D E1 1A ED BF 87 BC 4B 0D C6 9D E0 6F 0D DD 5F DE B4 89 25 E6 AA 44 A5 57 BE 54 48 CC C3 71 2D 9C E0 F1 FC 5D 68 EB A7 F5 FD 7F C0 B1 B5 18 29 FC 46 BF C5 B9 ED 96 1F 0E 6B BE 1E BD BD 80 C8 A1 6D CC 2D 24 51 AC 65 70 A6 30 70 10 E0 10 40 C7 04 67 A8 CF AA 6A DA AE A5 A7 5A 19 16 CC CA B1 28 92 47 8E 50 B8 50 E7 F8 99 48 3B 80 19 50 32 B9 E0 9E 08 F0 EF 1F 27 8A D9 FC 3F 0E B3 2D 80 B6 32 F9 36 56 B6 4A 4A C6 53 62 E4 93 9D D9 05 7F 89 87 51 C7 22 BD 1B E2 47 8D 2D FC 37 A0 EA 16 32 6A 76 B7 1A 94 91 A4 70 5A 75 61 8D 85 8B 85 5F 94 B0 66 3C 90 30 AB 81 D6 9A 8F BB 68 77 7F A1 55 1A 8B A7 CF DB F5 3B 78 B5 7B 49 B4 CB 1B AB EF 2E DC 5D 9C C6 92 1C 80 C1 5A 4E B8 C0 C2 A1 6C 9C 74 AB 66 E1 12 EC 47 33 A2 48 CD B2 15 12 12 5C 15 DD CA E3 83 F2 3F AF 0B D7 92 2B C5 AE 3C 67 AF F8 6B 4F F0 BE A9 6D 24 12 59 EB 11 08 46 98 C9 BA 65 00 8C 91 29 1B 89 CB 12 A3 1B 57 7E 30 40 06 B7 BE 1A F8 B3 57 F1 33 78 83 4C D5 7C 9D 4E D6 D2 E4 43 1C F3 A0 53 2A 3B B0 65 60 AA 54 FC 80 90 00 03 B1 20 1C 8B 95 27 AF 97 E8 FF 00 E1 8E 49 4D 5D 5B AF EB 7B 7E 47 A5 C6 55 A6 1E 7D BA A4 F2 02 72 AA 58 6C 46 F9 72 F8 C0 3F 30 3B 7D 4B 63 38 26 AB B2 2C AF 6A D6 DE 6C 91 C7 70 D2 33 6F E3 0D 1B 9C 82 C0 EE 5C B8 03 69 18 E9 9C 29 5A 92 FD 0E 17 8B 73 14 EC B0 DC 7D A5 CE CF 2C E4 60 27 42 CC 58 2F 6F BD 93 9D A1 4C 8E E1 2F 10 C9 72 D1 09 1C 47 14 4C 50 2C AC 15 98 ED E3 71 38 C9 23 3F F2 CF 38 03 39 CF 5D D1 4D 5D 58 AF A6 D9 DA E9 AF 3D AD AA C6 A8 EF BC 47 0C 0A 8B 1E 11 01 04 A8 03 27 86 E7 93 9E 38 1C 72 5A 7F 8E ED 2F 3C 6B 73 E1 5B 9D 1E E3 CD 54 30 49 77 70 10 99 91 15 89 32 2A 8C 05 24 9C 76 F9 89 C2 E7 15 A5 AB EB F6 DA 43 48 C9 61 F6 A9 C1 68 F4 E8 A1 91 19 59 A3 5D A7 0B BB E4 DA 5E 40 CF B4 00 AA 43 1E 14 1C 7D 2F E1 86 9F A8 2C F7 FE 2F B5 FB 7E B1 76 FE 7C D2 A4 EE 91 A6 7A 46 A1 58 67 68 03 92 3B F0 4E 2B 29 CA 52 95 A2 EE BA DF FA 7F D7 C8 A9 34 D7 BD BB 3B 6B 5D 46 D3 52 8C DD 59 5F 5B 4F 67 19 E6 6B 79 95 D5 88 07 72 B1 19 00 0C A9 E0 E7 E8 3A 8D E4 41 64 EB 71 70 12 DE D1 50 BC B3 46 A8 06 C0 18 B1 24 05 C6 31 C8 00 0C 1C 60 8E 3C EB C3 36 5A 26 81 AE 5E E9 9A 7E AD B6 D2 19 33 2C 2B 7C 23 79 18 B0 19 20 B8 38 51 8C 91 D7 69 C0 E4 0A C3 F8 BD A8 F8 5E F8 DB E9 EF E2 AB A8 1A 15 01 EC 2C 6D 04 EA C8 40 2A 01 CA 28 E5 73 82 C7 9D BC 7C A3 17 4F F7 B6 4B AF F5 F3 1A 4D BE 56 B4 3B EB ED 79 A6 82 EF 52 F0 C0 D2 35 B1 69 22 DC CB 15 9E A3 B2 49 3F 74 C8 43 6C 0C AC DC 29 1B B8 20 63 00 A2 B1 A1 F0 EB C7 91 F8 D3 4F 9A 51 A6 3A 4F 6B 20 04 35 CA 49 23 36 C3 99 31 B5 02 83 C0 F9 47 DE 73 C0 19 35 E4 DA 1E 8D A9 59 7C 4E D0 AD B4 6D 22 FF 00 49 5B 88 81 B8 49 E6 DB 24 B6 FB D8 48 EE 80 E6 20 54 60 29 24 82 AA 77 12 77 1D 2F 85 8D 36 99 E3 7F 13 78 7E C7 55 6B 52 5C 88 64 8E 25 98 39 8E 5D 83 E5 20 E4 61 C9 C8 23 03 93 C0 35 D4 E9 C5 5D 5F A5 FE E7 66 53 8F BB A7 97 E3 73 DF 96 D7 CC 58 9E E4 A3 DC AC 26 26 96 24 D9 F7 B6 EE DB C9 65 04 A8 38 DD D8 72 70 0D 2C F7 51 5A 09 A7 BA 94 41 6D 14 61 9A 69 5D 56 35 E4 E7 92 72 3B 67 3C 72 31 DE B9 2F 0A DD 1D 6E D2 EE EB 53 81 27 BF B7 7F 2A 49 98 24 7B E2 C1 FD D9 DB 8D CA 32 C7 6B 7C B9 23 EA 3A 4D 41 EC 2E E1 36 97 F0 5B 4F 6B 2A B6 E8 2E 97 25 CA BA 81 88 D8 7C C3 71 1C FA 94 C6 77 03 5C F3 BC 5D 99 36 B6 8C E1 6F FC 76 97 2D 71 69 E0 4D 39 F5 99 DD 9A 59 9F C9 7F B3 C3 27 5D DB 98 81 9F 91 9B 6A 8F 99 8E E0 77 64 37 4F E1 E8 7C 43 A6 E8 B2 2F 89 B5 9B 5B 9B F7 70 23 9C 44 AB 12 16 3B 51 38 D8 5C EE C7 61 92 C0 03 5E 39 E1 2D 6B C5 1A 7F 87 75 31 E1 FD 19 6D 60 96 F1 E4 1A 8D C0 C4 70 2B 61 76 22 11 82 C0 A2 F3 F3 63 18 23 BD 7A 57 86 F4 C4 F0 EA AD 9E BB E3 39 6E EE F5 01 1C 89 1D C5 F7 96 F2 31 E0 04 04 EF C7 40 0A B7 CD D0 8E 00 A4 BB F7 B7 F9 D8 BA 8A DE EF 44 DF FC 39 D2 47 71 64 9A C4 71 3D CF 9D A8 22 CE 16 25 0B 23 05 77 0F 82 FB 41 5C 05 51 B7 20 74 CE EC 03 49 15 F4 37 D7 6E 96 F0 CC EF 6E F3 62 78 24 49 00 28 EA 1A 26 62 70 19 8E 7E 4C F0 14 13 B0 80 07 3D 7D E3 3D 27 C2 32 FD 86 EB 48 D5 AC 6D 5A E5 4F DB A3 B6 2D 03 6F 3B F3 BD FE 62 76 FD E5 0A 4A E1 80 FB A0 D6 B5 B7 88 EC 7C 4D 62 07 86 75 8B 55 BA 9D 18 B6 E5 56 92 01 D0 C8 D1 12 18 90 40 51 9E 3E 60 79 18 CD 4A 32 B5 DA 31 76 4D 38 EC 62 7C 3F BC 8E C1 3C 4D 04 ED 79 3C 8B AF 5D 66 55 B5 79 0B FD D1 92 63 4D A0 9C 67 00 0F A0 14 57 6F 63 61 69 A6 5A 2D A5 8D BC 76 F6 E8 59 96 28 C6 15 77 31 63 81 D8 64 9E 28 A8 9B 94 A4 DD FF 00 02 5A 9D F4 7F 87 FC 12 BA 6A 11 5D DE 4F 6B 6E A5 2E A2 8B 2B 24 91 E4 2E 7D 40 21 87 20 70 DB 77 63 8C 80 48 A9 A8 CF 0F 87 2C EE AE E1 87 55 BA F3 1C 4D F6 4B 44 33 BB 31 71 BB 60 6E 99 DD 92 B9 03 0A C5 40 3B 8D 5D 83 4B B6 82 36 8E 28 12 12 B2 6F 49 50 EE 76 72 9B 3C C6 2C 39 7C 64 65 B7 67 03 27 B0 B2 8A 2D 2D 48 67 96 45 8C 13 96 05 DF 1D 71 C0 CB 60 71 DC 9C 77 3C D5 37 67 A6 C5 23 C8 7C 4F E2 F6 D7 87 86 EC 6C 34 3D 4E C6 78 75 DB 76 86 4D 42 CB C9 B7 05 59 D1 50 E1 89 C8 3C 10 3F BA DD 31 8A F4 5B 6D 56 C7 FB 56 E6 C4 38 BA D5 2C 11 56 E2 53 19 55 85 E6 D8 55 01 39 60 AE 70 70 37 05 0B 82 46 14 57 29 F1 64 4D A5 68 5A 05 CE 9D 1C B7 17 76 FA BD AF 91 14 8F 24 DE 63 22 C8 50 63 39 62 49 C1 3F 79 B8 C9 38 15 3F 83 AD 74 EF 0D 5A 24 36 8A BA A6 B3 32 B4 B7 A7 4C 8B 02 47 91 83 0E AC B1 C6 81 47 C9 BF 68 61 CA 7D E6 07 69 A4 E0 9A F3 FF 00 82 37 B5 91 D8 C9 01 3A 84 53 8B 76 8E 65 0D 24 8D 03 60 CF B5 4A AA 31 C6 19 7F 78 C4 06 2B 86 50 40 23 24 78 C7 8A B4 0D 52 1F 19 6B 91 41 AB 79 7A 66 A9 32 3D EC 16 CE 3C DE F8 8E 42 33 E5 EE 05 8A E4 FC C0 E7 07 04 0F 4F BB D3 B5 6B C3 66 75 3B CB 88 00 3E 50 8B 4F 96 50 A4 B2 90 1A 57 8C 2B 3E 19 55 B2 04 2A A1 88 C9 EF 99 E2 C3 65 A7 E8 B6 96 F6 C0 C3 17 DA D6 44 8A 45 F2 80 1B 73 B5 10 E3 6A A8 2A 0E 00 00 9E 7E 6D D5 8A B2 69 C9 FF 00 57 36 A3 51 A9 DB A7 FC 3F DC 73 3E 28 F0 8D C4 DA 87 82 F4 66 02 DA CE D9 A5 31 C7 66 4B CD 1C 48 91 B3 B9 7C 0D CE 58 7F 0A 8E 48 EA 6B 17 E2 27 80 B4 8B 0F 0F DB DC E8 CD 7B 77 2C 52 A1 7B F6 97 CC 8E 28 72 EC C1 56 30 15 4E 5D 0E 02 8C E4 15 C9 DE 6B D1 A6 D4 B5 7B 98 DE EE 3D 02 E9 23 91 BE 78 20 F2 E0 96 57 20 28 0F 24 AD 19 00 ED 8C 65 54 F1 81 9E 30 39 BF 12 78 9A 35 F0 B4 FA 5E A5 0A E8 BA B2 A4 4A F0 C5 6E 4A C7 8C 9D C6 5C 18 B6 92 C3 0B B9 B1 9C 12 58 90 2A 33 77 BC 75 D6 FF 00 D7 97 E6 26 E3 26 A3 37 A2 FF 00 83 FD 76 38 0D 66 D3 C1 96 FE 29 D2 2E 34 4D 42 D2 08 61 D3 CD E5 CC E2 52 C2 4B 80 A4 A2 85 39 0A C5 B6 E5 40 00 7A 76 AE CF E1 E5 85 F7 83 FC 2D 0C 17 26 4B 1B DD 57 74 D1 49 3A 01 1A C8 D8 44 0E 40 62 70 3E 60 08 51 9E A4 83 95 F2 69 8A 78 85 6C D6 EB 5D D1 A0 BD B8 95 A3 99 1B 4E 10 08 F0 7E 56 69 62 8B E6 2C 73 CF 38 EE 7D 3D D4 69 D2 D9 EA DE 19 F0 EA DA DB C7 0E 9B 1A CA 55 2E 5A 40 54 1C B1 2C 50 1C FC A4 E3 18 39 C7 1D BA A5 15 CB CA DE FF 00 96 AF 7F B8 E2 97 BC FD 3F 3D BF 13 AD B0 D7 17 56 9A E2 C1 AC 16 3B FB 69 50 CD 04 CD 94 00 38 F9 D5 C2 9C 90 39 1C 0C 90 3A 75 1A 8D 28 9A 55 8C C2 8F 19 21 97 73 A9 2D 82 84 32 8E E0 13 D4 E0 82 A3 00 E4 1A E4 BC 0D 03 5E 4D AA EB 26 24 56 B9 B9 C4 72 7F 16 DD DB 99 7A 70 3A 77 E7 1D B0 0D 74 73 43 1D 9D A2 4C 2D 5B 50 BA B4 90 98 D9 E2 51 21 67 3F 31 42 14 28 24 31 19 E0 75 DC 40 C9 1C 93 B4 65 6F EA F6 37 84 B7 B9 C4 DF 78 D6 C6 D7 C4 33 69 1A 7E 97 A8 EB 1A BC 6C 37 40 B6 CA AB 21 0C 0A 99 64 75 DC 36 36 5D 4A A8 45 DC 31 C0 06 A1 BB 83 C5 BA AC F1 69 F7 BA A4 5A 4A 48 23 89 AD F4 C6 69 67 8D 4F 2A 26 9C E5 8B 81 FD DE 08 DC C4 81 92 7B 77 BD 99 6C 9A 25 BF B5 12 82 B0 09 E4 91 72 AC 65 31 06 6E 00 2E DD 94 28 1B D4 AE 79 06 96 1B CB 1B 16 58 60 9A 1F 2D B6 C9 C6 E9 65 B8 DC 09 24 01 CB 7F 09 DC 37 67 E6 1C 63 35 8C A0 F6 9B D3 FA E9 E9 FD 6E 5B 6F A2 FE BD 4C BD 1F C0 DE 19 F0 F5 B7 DA 6D 34 32 66 78 D5 65 F3 8F 9D 26 38 CF 04 91 9E E7 6F 5C 71 9E 05 72 3E 29 D3 3C 6D 69 AD 5C 4B A1 E9 5A 15 8D 82 87 96 2D 40 C8 CE D0 85 52 D9 65 6F 94 13 83 8F 90 81 C0 CF 00 D7 7A F0 5C EA B3 09 6E 34 DB 77 8D 09 6B 66 BB 88 2B 44 AC 85 5B 20 33 6F 24 16 C8 21 38 38 F7 AC 3B CB 4D 32 DA CE 39 FC 53 A9 CF 22 18 F7 C6 AD 13 A8 10 C5 B4 6E 90 60 B6 7E 7E 4B 63 1E 66 3A F3 57 1B 73 E9 1B 8D 3B 5D 7F C1 3C 96 0D 2E 15 F8 C1 73 E1 FF 00 13 CD 73 AE DB 79 29 01 16 A1 A0 04 05 56 50 22 88 FD D4 E7 E5 04 63 19 EA 30 6F 68 F0 5A 78 7B E3 F5 A4 76 D6 32 E9 9A 75 E2 18 61 82 71 E5 B2 83 19 4F 5E EE BC 1C E4 E4 1C 9C D7 47 A3 6A 7E 14 D1 BC 4F AD EB DA 15 BC FA C4 B2 46 8B 1B 59 5B 4B 24 76 B1 04 19 0E CA 18 A9 62 A4 E4 A8 CE 3A E3 38 A7 0A DE F8 AF C6 FA 6F 88 60 D3 F5 09 DE 3F 34 A8 02 2B 70 B9 05 54 16 67 DE 23 19 20 C9 E5 83 F2 9D A1 89 E3 AD 4B 67 2D AD 67 F3 BF 42 E7 6B 4B 99 E8 ED F7 E8 75 1E 1E D1 EC FF 00 E1 30 D5 F4 FB 98 E4 0F 0B 79 B0 BA CA E9 2A 8E 9C 48 AD BB 04 38 C8 27 9E 33 C8 A3 5B F1 36 8B E0 AB 8B 24 BD BD D6 9A F9 A2 8D 85 A1 79 67 99 B2 18 31 25 A4 F2 49 CE 38 19 C1 07 1D 41 58 AD 85 DE 95 F1 16 D6 E3 55 B4 B1 B0 86 5B 77 E5 2E DA E3 90 08 DE 64 74 4C 70 BC E7 24 EE E4 8E 45 75 77 96 BA DD F4 D7 16 B3 5C E9 F6 76 13 E6 05 46 1F 6A 79 90 86 CE 03 2A 04 6D B9 24 37 9A BE D8 07 76 15 55 D2 7D 1A 21 C5 29 5D A3 CF AE AC FC 71 AB E8 EE EC D7 BA 7E 9B 3B E6 3B 4B 88 D6 E6 E9 DB 3B B3 F2 C6 A2 14 DA 0F 07 A1 18 C7 22 AB 78 73 C3 9A 61 BD 87 53 B2 B3 B8 F1 1D FC 6C 93 2D E0 93 71 8D 86 4E 5D 8C DE 53 B0 CA 0D 85 D1 C0 0A 76 91 93 5D C6 A1 A3 F8 71 8A 5B 6A 97 2F AB DD 2B 01 E4 5D DC B3 2F 64 C8 82 31 E5 AB 84 63 B7 6C 60 9C 67 20 E5 AB 46 D3 ED 11 B5 C4 F6 1A 5D B5 AF 9D 24 71 F9 CC 14 92 8A 15 07 C8 80 31 0B F3 0C 31 05 47 A7 DD 58 BA 5A A0 E6 4A 36 ED FD 7C CE 5B C6 5E 1F B9 BF F0 4E A4 D7 C8 E3 51 8E C9 6E 50 B3 93 0C 4A 9B 1A 58 D7 68 00 11 B5 80 C8 1B 81 07 27 0D B7 5B 44 BC F0 F5 D4 AB 3E 99 A4 41 68 F7 36 C6 66 7B 18 90 4C 61 65 2C 77 94 1B 81 04 C6 36 A9 2F BC 8E 36 8D C6 E6 BF A2 2E B1 A3 EA 56 53 6A 0C 2E A4 86 5C 2B 8F 22 12 FE 5E 03 10 06 59 01 64 24 E5 B0 40 19 EA 2A 87 C3 AD 67 4F BF F0 C6 9D 67 6B 04 B1 BA 5B 44 5C A4 3B 54 BA 82 8D B9 94 91 92 D1 13 F3 63 2A CB C1 CD 5A 7C B0 E5 13 7D FF 00 AF E9 1D AA 17 2A 7C C5 55 6C 9C 05 6C F1 9E 0F 41 CE 31 F4 F7 EB 45 56 B0 BA FB 54 24 FD A2 DA 72 9B 41 92 DD B2 AC 4A 2B 67 1C ED CE EC 81 93 F2 95 39 E6 8A 86 AC 4D AD A3 20 BC D5 A1 D3 EF A0 B7 BA B8 85 7E D0 EC 54 B0 65 11 A2 C6 58 E5 B0 57 3F 23 1E 4A FC A1 B1 9D A7 28 91 CB 6D 32 5C 4B 2C 71 46 91 B4 97 22 67 32 2C 79 2C CC 12 46 00 E0 B1 19 C9 C0 58 D4 05 5C E4 12 DC CA 9A E5 9E 9C 58 BC 53 5B CF 70 CE 4E 1C 34 72 45 B4 02 B8 F9 7F 78 41 18 E7 03 3D F2 C5 B6 B7 9B C4 77 62 4B 78 9C A4 16 F3 02 CB 9C 3E E9 40 60 0F 01 80 00 64 73 8E 33 C0 C5 59 DB 42 94 25 FA 9E 77 E3 AB 19 FC 47 E2 9D 0B 50 D3 EF 2E 35 4B 4B 2D 46 18 BE CB A7 DA 38 11 2B 15 69 1F ED 2A D8 DD F2 A8 EA BB 72 3A 10 49 EF 2C 6C EF 20 B4 86 0B 2D 32 C7 4E 58 D5 95 1D 91 73 10 63 B9 80 8A 3F 94 02 C3 A6 FF 00 EE 92 49 C8 AB 9A AB A6 95 A5 6A BA A5 B5 BC 02 E9 2D 9E 56 72 9F EB 0A 21 2A 18 8C 12 07 4E B5 76 1B 78 AD D0 AC 28 11 4B 3B 90 0F 1B 99 8B 31 FC 49 27 F1 A8 6E 52 49 37 B7 6D 04 D5 D5 CC 2B AB 17 BD D3 27 9E EB C4 37 B6 B1 46 64 32 49 1C 91 44 91 6C 2C AC C1 97 90 06 09 01 D9 B1 C6 E0 48 22 B4 A1 D3 ED 6C AD 59 E3 B7 B5 B6 76 D9 25 C4 85 77 EE DA 72 4B 31 C1 63 D7 0C DC E4 E4 FA 56 85 64 DC AA 9D 72 C6 D3 1F 23 41 73 31 72 49 90 1D C8 B8 0C 79 51 FB C2 78 C1 F9 54 02 00 C1 14 57 44 16 BB 65 89 44 6B B9 AE 85 BC 90 87 DF 21 DB CE F0 CA 62 F9 70 72 C3 03 9C E7 20 60 72 31 18 D3 85 DC 62 2B E5 59 2D E2 90 34 11 B0 6D EA 54 90 19 9B 79 DF 91 83 CE 3D F9 38 13 45 71 23 6B 57 56 C5 BF 75 1D BC 32 28 C7 42 CD 20 3F FA 08 A9 2D E7 69 A6 BB 8D 82 ED 86 51 1A E0 75 1B 15 B9 FC 58 D3 E6 E8 53 52 8E 8F C9 FF 00 5F 23 8C BF F0 99 D5 75 F7 96 EA 1D 37 52 92 DA 46 74 92 73 2D BD D4 08 EC 5D 15 24 5C 9D AA 7E EB 0E 87 78 5D B8 20 66 5E 68 B7 7A 0D CE A7 AC 4B FD A7 12 18 0C 11 A9 BD 4B B8 FE 65 DB 92 F3 13 2F DE 20 80 31 8C 77 04 83 A3 E1 DD 36 D6 E3 C5 DA F2 EC 78 56 17 08 8B 6D 23 42 00 C9 E3 E4 23 D0 56 A7 88 34 51 1E 8F 75 3B EA 17 B3 47 13 09 D6 09 9D 5D 32 A0 00 32 57 76 30 3A 67 9E 49 E4 93 5B 2B A6 93 7B DB EE 76 31 B2 D7 CB F4 27 F0 E5 8F D9 7C 39 A5 46 B6 51 C9 2A 0F 38 3C 9C 08 CB 1C 31 04 8D C1 B6 3B 63 03 07 90 48 07 35 A4 E2 1F EC F6 B9 89 22 BC 8D C2 DC 6E 20 37 98 55 41 57 1B 14 EE 39 55 23 00 9F 4E 80 55 59 AD 05 AC 9A 44 42 6B 89 4B 32 5B 33 CD 29 72 CA 88 D2 06 2A 7E 5D E4 C6 32 F8 DD 82 70 47 04 53 9F FE 26 FA 7F 87 DE E3 E5 4D 45 97 ED 50 A7 11 C8 1A 06 76 0C A7 3B 94 EC DB 86 C8 DA C7 BE 08 CA 52 BC F4 2D 5B 93 4E 9A 7D EA E4 BA 94 DA 56 A3 A4 5D 4D 71 73 35 CD B5 B4 BE 44 82 3C 2B 43 2A B1 46 6C 60 12 DF 36 71 CE E0 06 D5 39 1B A5 82 D1 34 FD 36 E2 D7 4B 80 C3 7A B0 AC 81 D6 DC E2 56 E7 1F 7D 86 46 41 05 4B 82 01 EA B9 0D 55 6F F5 5B CB 3F 1A 69 3A 64 52 FF 00 A2 5C 80 AE 8C 01 23 11 5C 36 43 75 C9 28 99 C9 3F 77 B6 4E 7A 28 A1 58 8C 84 34 8C 64 60 CC 5D C9 E7 00 70 3A 28 E0 70 30 33 93 D4 9A CA 13 52 6F 97 A6 9F 3D 3F CC AD AC C6 49 0B F9 FE 72 AA 48 DF 20 0A EC 40 5C 13 B9 87 51 9D AC 7A 01 9C 60 9C 63 1C FC BE 06 F0 DD BD C4 9A 85 96 8B 65 6F 7C 59 5D 65 86 DE 2D CA C0 10 AC 81 D4 AA B0 27 70 20 0C B0 5C 92 2A F5 C5 D4 93 F8 A6 0D 2D C2 7D 99 6D BE D8 41 40 4B 3A B8 0A 0E 73 C0 24 30 C6 0E 54 73 8C 83 5F 59 BA 97 4D 32 28 2B 73 1A E9 F3 DE 04 B9 50 E0 4B 0B 23 2B 76 3C 97 CE 3A 0D AB B7 6E 0E 75 A7 19 55 97 2C 37 FF 00 86 FF 00 33 3A 92 50 57 64 F6 B6 7A 85 DD 9C AB A9 24 11 17 92 54 30 87 79 54 C2 FF 00 78 1C B6 09 DD 9D AC 47 DD C7 CA 99 2A 32 3C 05 6A F1 E9 77 16 F7 31 4B 9B 7B 97 09 BD 58 23 0C AF 23 3F 2B 10 C8 79 EA 3D B3 CD 5F 19 78 A2 FB 41 F1 15 AA DA C5 6C E0 5A 13 FB D8 F2 7E 77 E7 90 41 1F EA C7 00 E3 9E 73 81 8D 8D 1E 0B 8D 5F 4E 99 EF F5 2B B9 63 FB 55 D5 BB 44 BB 23 0C B1 CC F1 AE 4A 28 6C E1 41 38 23 27 3D 8E 2A F9 5A 8D FA 33 69 6B 1D 4C BF 19 DB 5D 43 AB E8 9A 8C 66 29 1D 2E 44 48 A1 0A 92 4B 64 02 D9 3C 63 8E 9E A7 BE 06 C8 D7 34 99 F5 59 F4 ED FE 75 D4 D8 46 43 1A B7 C9 82 30 71 C9 01 B7 65 4F CC B9 24 80 BC D4 AB A1 69 31 DD 47 03 69 F0 4E 1D 64 90 B5 C8 33 30 3B 94 9C 33 E4 80 4B 31 C7 A9 27 B9 AD 46 B5 B7 6B 61 6C D0 44 6D C2 85 11 14 1B 70 3A 0C 74 C5 66 E4 A7 1E 55 D2 FF 00 A1 17 4F E4 63 CB 73 A1 C4 BE 7C 70 DB 89 E6 50 5E 23 6E C2 56 12 0E 04 88 17 7A E4 B2 E7 72 F5 20 63 38 A7 42 F7 B1 DB 5D 4D 67 A3 43 66 CF 24 92 C8 0E D3 24 AE 3F 8B 62 E0 31 60 14 64 B8 E7 B9 00 13 7D 95 34 DB 18 A3 B7 8D 15 16 48 D0 28 50 A3 E6 70 09 C0 C0 CF 24 FD 69 6E 6C 12 E2 68 65 59 64 85 E2 94 4B 98 B0 37 B6 00 F9 B2 0E 72 B9 5F A1 C8 C1 0A 41 6E A2 E6 4D DB A9 5E 4D 22 3B B4 DF 78 E6 EA 65 C9 41 3A 9F 29 5B 04 7F AA 04 02 06 4E 33 93 8F E2 E8 6A 87 82 F4 2B DF 0D F8 4B 4E D2 6E A6 B7 69 AD CC 9E 69 8B 2C AC 19 DD 80 52 70 41 F9 97 92 3B 11 EF 5B 72 11 1D C4 4C A8 BB A5 6F 2D 9B 1C 90 15 88 19 FA E7 F3 35 8F 26 A4 F2 BE A6 64 B7 B7 73 65 6D E7 44 59 33 F3 09 25 1C F3 D3 F7 28 78 C7 23 E9 87 1B B5 2B 74 DF FA F9 8E EF EE 13 50 D2 35 1D 46 ED AE 2C B5 69 F4 B4 20 06 48 E1 53 E6 9F EF 9C E0 E7 18 5C 11 91 B7 D3 14 56 CD A5 B2 DA 40 22 56 2D C9 62 C5 55 4B 31 E5 98 85 00 64 9C B1 E3 A9 34 55 C6 AC E2 AC 9F E0 BF C8 97 18 37 77 15 F7 7F C1 3F FF D9";
            } else if (rand == 2) {
                c.setLIEDETECT("버터에이르나");
                packet = "77 17 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F7 2B 7B 5B AB 7B 65 59 AE 05 FC EB 31 65 92 65 58 CA A9 62 38 DA B8 CA A1 23 A7 CC 7D 33 C4 69 37 33 99 6F E3 32 45 23 16 58 80 4C 22 F2 15 83 93 CE 19 72 C3 6E 72 A7 80 45 48 8A D1 2E F9 C3 AA 2C F2 38 3E 69 C2 2E 1B 97 25 B9 53 C9 03 A2 E5 46 06 DC 8A 56 57 91 4B 1C 30 5B 0B 9B 8B 78 8C 6A 67 33 F9 9F 29 07 63 6E 42 4B 93 B5 18 EE 39 DB 28 27 82 45 0D A6 EE 0D 7B D7 34 2E 64 B9 86 CA 66 8C 47 25 CE 08 87 2A C1 0B 13 84 0D B7 71 00 65 77 36 3D 4F 03 A5 6B 7B 88 6D 75 05 D3 A2 F2 45 B3 26 20 09 24 60 46 C8 00 31 04 00 10 02 E1 86 37 7F 16 76 80 A0 96 5A 94 1A CF DA 16 D2 F2 35 6B 5B 93 1C 82 DE 78 E5 6F 94 F4 70 37 05 DD 83 C7 DE C6 3E E9 C8 1C BE 8F E3 AD 0F 50 F1 BE A1 A5 3E A1 78 B7 5B 94 DB 24 C5 56 DD 91 50 FF 00 AA 28 C4 36 E0 CC F9 6E 48 2B FD D1 87 7D 6D DC 1B 69 5C EC 2D E2 8A E2 D5 24 10 4D 6C B2 30 93 CB C9 8D 81 DD BF 24 29 E3 27 92 3B E7 0D DC 54 2F 67 62 9A BC 9A CC 80 47 71 05 B9 B6 79 99 C0 5F 2B 21 F9 E7 8C 1F 5C 75 3D B1 55 3C 51 79 63 65 E1 DB BD 42 E6 F3 EC 6A 20 68 D6 F9 2D 7E D0 61 0F 8E 76 85 6C AE 42 E4 74 38 19 AF 1A 82 28 BC 49 7A 92 DC E9 DE 29 F1 B4 1F BD 99 E7 B8 94 D9 5A 6D 0C 01 7B 68 B3 F3 30 CE 36 6E 5F 4C 7A 4A 93 BD 97 CF FA 44 37 64 91 DA FC 4C F1 AF 88 7C 15 7B 69 73 6F 63 A7 5C E9 53 91 1E E9 37 2C C0 9E 59 03 07 E3 85 CE EC 77 1C 1C 73 D3 FF 00 C2 53 12 1B 66 6D 37 52 85 A4 4C DC 03 A6 DD 4A D1 10 7E E0 29 13 2B F3 BC 64 36 3A 11 B8 1A F1 AF 17 6B 5A AF 8A F5 CF 09 69 1A 97 87 64 D1 2D BE DB B2 18 24 24 33 C6 5D 14 1D 84 02 A0 2F 1E E7 3D 31 8A F7 CD 46 CA 6B 98 9B EC D7 06 DE 46 8D A3 67 41 96 C1 1C 6D E4 00 41 E8 4E 71 CE 31 9C D2 8F C2 E5 E6 FF 00 2F F3 1D F9 A5 6F 25 F7 FD DF D5 CC 9B 6F 19 78 4F FD 69 F1 1E 99 1B 14 44 29 2E A9 13 60 01 9E 9B C8 CF CC 41 3D 4E 3A 90 05 5D 9A F2 76 D3 AE 2E 1E CC BC D1 D9 93 25 AC 0F 99 22 97 68 66 8B 7A 9C E4 82 B8 DA BD B3 9E 56 B8 7B 8F 1B 6A 3E 1B B6 D5 E4 BC B8 8A F9 A0 98 5B 5B 44 21 54 F3 26 66 6F 9B E5 C7 18 56 24 73 9E C4 77 E1 E5 37 DE 0E F1 0D 9D C6 B7 A6 68 DE 5D D4 8E 65 B5 D3 AD 61 59 ED B8 03 08 E8 03 C6 E1 59 59 76 BF 70 73 92 4D 4A AA 96 8F EF ED 7F EB B0 A7 38 2D D3 FC 0D AD 77 E2 0D DF 88 B4 1D 60 7D 9E FF 00 44 D4 74 B6 47 89 AD AE 99 78 2E A8 D1 C8 46 D2 4E 49 3D 3B 7B 73 DE 78 06 F7 51 9F C0 7A 4C F7 73 C9 71 73 3A C8 4C B7 05 9D 8E 1D 88 0C 46 70 0A 83 F3 13 C7 1C 1C E2 BC 16 65 86 E3 45 D6 75 5C 4D E6 4D 7D 1C 51 34 93 31 62 8D BD D8 37 3F 31 F9 53 93 9E 79 AF 58 B0 37 9E 15 F0 8D 8C D6 B7 77 3F 68 7B 58 9A 58 A6 B7 53 8F 90 B0 54 7D B8 2A 39 F9 46 70 58 93 CB 1C E3 4A AD 93 94 FC BE F3 9D 4E F3 BE EB 5F CE C8 EF E2 BD D3 24 D4 E2 09 7F 0C B7 0E 8D B4 29 46 DC 01 E3 2C 07 05 77 10 06 79 0C DC 1E 48 F2 0F 89 C3 C4 3E 1A BB B0 F1 36 9D AF 6B 26 09 EE 1C 35 B5 DB FC B6 D2 E0 8C 05 1F 2E 0A EE 03 00 8E 32 18 EE 06 B0 F4 78 7C 49 E3 C6 BE D6 53 5D 36 97 D9 67 8C 45 70 6D A2 88 2F 24 B6 C0 4F 43 C7 7E E4 D5 A7 F1 7D DF 8B FE 19 5C 69 BA AC 06 F7 53 57 30 DB 4D 1D D2 2B 31 5D AF BA 48 D8 AE 4E D1 B5 59 43 16 39 5F 94 B0 DD DB 1B C5 F3 36 AE AD 7F E9 9D 54 ED 2B C6 DA 6D F8 1E E3 69 A9 5A EA DA 44 17 AE 44 76 77 0B 1E DF 34 A6 C9 C4 8A 00 1C E7 82 5F 6E 0E 09 61 8E 87 97 42 49 8A DD E0 BB 66 2F 2E DF 31 49 9D 24 50 5C 9D C4 60 2E 46 79 18 01 B6 AF 20 05 3E 25 E0 CB DB 2D 47 E1 F8 8E 4B 1B FB AD 52 D6 E0 DB AB DA DB 4B 28 48 F1 B9 4B 10 76 2A E4 90 4F A0 E9 FC 55 DF E8 DE 36 58 34 C8 AD A6 9B 45 80 DB 44 AA 5A FB 54 FB 3B B0 1C 03 B4 C6 78 E8 33 92 09 3D 73 90 1D 48 C6 2D B5 D1 84 53 69 1D 5E 9A CA 74 E1 3A 24 4F 3C 6A 64 7B 7B 26 0A 3C C7 40 EC A4 6F DA 59 99 8B 64 9F E3 07 3F C4 5F 2D E3 C4 91 4B 34 09 22 08 84 91 B8 0C AE CF 82 5D 56 22 0B 06 D9 92 00 C9 3F 30 38 C6 4F 19 AF F8 C3 4C D2 F4 E8 CE A1 E2 1D 3A 5B 6B 80 D7 16 C6 C3 4C 67 13 48 8F B8 6C 94 B4 91 06 12 28 C9 20 90 79 E0 E0 D6 55 9F C4 7F 06 3C 68 97 7E 22 D4 9A DD 02 88 63 D9 2D B3 C2 36 A7 CB 8B 75 55 60 32 C0 92 78 DB 85 04 1C 9C E0 A4 BE C8 68 95 A4 FF 00 AF EB 53 D4 11 EC AE C4 81 1E 3B 95 13 E1 C0 6F 30 24 89 83 83 D4 29 05 41 C7 18 3E E6 B9 9F 10 F8 CE C7 4D F0 95 F5 D5 AE B3 61 2E A0 21 6F 2F CB B9 49 36 48 D9 DA 00 03 E6 00 90 07 CB CE 06 EC 0C 91 26 8B A7 F8 7B 5F B6 3A AD 88 B6 D4 EC 6E 88 45 92 68 C9 90 2A 67 3B 9D C1 91 D8 B0 00 86 20 6D 0A 00 E0 EE CC F1 E5 C7 DB 2D BC 3B A2 07 5B E8 B5 8D 42 21 38 81 09 37 16 A8 C1 CF 2A 08 00 02 A4 B0 65 E9 91 C1 38 A5 16 9E A2 94 AD 0B C7 FE 01 AB A4 6B BA 95 C6 8B 6F 6F 71 61 A8 DC EA 4B 00 4B B9 A0 82 38 7C A9 B6 A9 23 12 B0 1B 80 65 39 00 AB 67 23 8C 81 2C C3 52 49 55 A4 B3 B3 8E 19 C4 31 41 0D CD DB 4A 63 64 2C EA C2 25 8F 99 07 52 03 FF 00 CB 31 F3 00 37 56 9F DA AE 63 8B CC 9A 2B 88 CC F7 31 AC 71 6D F3 1A 35 21 41 0D E5 AB 05 1C 31 C9 24 73 F7 97 23 6C B7 D7 AD 69 6D 72 EF 0C AC 55 19 A2 5B 71 B9 DC 00 38 1B 80 50 E5 8E 02 93 CF E6 04 49 B4 9B 4C A4 EC B6 D3 FA EE 63 C7 A6 5F 47 6D E4 FD BA DE 1D 3E 34 59 63 1A 6D 8C 51 28 20 B3 64 79 8C E3 19 C1 FB A3 92 A4 11 F3 55 5B C4 B7 F3 55 26 D5 75 4B 9D 48 47 85 B4 8A E5 BC E5 62 01 0A F1 DB B2 22 A8 25 72 ED 81 96 19 70 31 9A 5E 10 F1 BC 3E 31 8A F2 3D 0D 22 B3 B8 8D 89 2B 3C 04 8B 78 C1 0B 1E E5 0E 37 B3 00 C7 E5 20 2E 00 39 C0 2F 5F C6 5F DA 9E 09 F0 DB 78 8B 4C BF B7 7B D8 84 43 50 37 10 0F F4 F6 CE DC 93 9D CB CB 9C 28 3C 00 A0 10 17 06 5A 4B 57 E4 4A 94 A4 DB 4F FA FE BF E0 9D 96 89 64 D6 36 92 C5 22 D9 AC 8D 29 77 5B 65 6F 94 95 1C 3B B1 26 47 F5 72 01 6E 0E 05 15 CD 69 3A EF 88 3C 5B A1 D8 6B 5A 4D B2 5A C1 71 17 CF 14 D7 82 32 1C 33 03 8C DB C9 95 E0 60 E4 7D 07 72 B7 54 65 6D D7 DE 84 A7 75 72 CF 8E 75 A4 F0 F7 80 75 39 AF 19 2E A4 D8 6D 94 4A 85 04 C5 F8 03 8E 09 DA 79 23 8C 83 C0 E8 3C 5B C2 9A A7 89 7C 09 E1 B6 D4 E3 B4 D3 6F 74 AB A6 8A E6 6B 6B 99 7F 79 16 1B F7 6C 17 23 0C D8 04 60 31 01 54 90 38 CF B5 78 A7 C1 5A 7F 8B A3 8A C7 54 B8 78 E1 8A 76 B8 B7 86 D1 84 45 94 85 0E 5F 21 B7 1D CC C7 20 0F BC 01 EE 4F 95 6B FE 13 D2 24 F1 35 B5 A4 FE 2A D4 5F 4E B5 C2 3B EA 72 1B 90 76 FF 00 0A 05 DB 85 19 C7 E2 7A 57 34 93 53 6F 6B E9 F2 EA 54 9B 7A AE 9F 9F 91 97 F0 EB C5 5A 37 86 EE 75 AF 12 EA 9A 93 7F 6A 4E 92 2C 3A 6C 51 3E 25 2C 77 64 B6 D2 A0 6E C0 1C F1 CF B5 6F 7C 34 D0 AE B5 4B BB EF 88 1A F3 47 2C E5 DA 5B 73 73 20 8C 70 47 99 30 24 10 02 2F 0B C6 DC 8C 65 40 04 73 FE 32 BD B0 F1 B7 8E 85 A6 97 1D B4 3A 55 92 08 D6 E6 1B 5D AC E1 54 0C 33 75 23 2B B5 73 80 07 6F 5F 60 F0 70 D7 6C 34 58 ED 92 C0 C9 01 55 FB 37 DA 26 11 C6 91 85 C6 01 5D ED E9 80 50 0E 09 CF 20 15 09 5A D2 5B 2D 17 A7 FC 12 24 9B 7C 97 F3 7E BF F0 0D 7F 14 4B 71 A8 78 63 57 B6 D1 AE 62 96 F5 A0 78 15 23 1E 63 09 1B E5 50 70 7E 5E E0 93 C0 EA 48 00 D7 98 E9 1E 0C F1 7F 87 74 48 EF 75 4D 7E 4B 38 34 E5 89 E3 B0 B6 91 CA BE 4E 4A 33 86 1B 79 23 72 AE 55 B8 E4 91 91 A1 AA EA 9A B5 EF 88 EF 35 4B 1D 26 E3 1A 7A 86 9E E2 CA 48 E5 4D AA 3E 6D B9 68 D9 F8 07 8C 64 F2 33 8C 62 DF 89 A4 F1 64 DE 1F 47 BC 9E D9 23 BA 74 05 16 72 C1 49 1B 80 F2 5A DE 37 1F 46 93 23 8C E7 B9 7E AB 77 6F F8 16 2A 4A F7 BE CA FF 00 F0 4E 73 54 B0 FE DD F8 99 E1 E8 B5 8B AB 89 60 87 48 59 E6 2A 9B 9F E6 DE 15 54 2A 92 C4 92 BD 72 4F 35 EA 29 37 85 B4 07 93 4F F3 6D ED 9C E5 64 B6 69 4B E4 31 41 97 5C 9E 0E 57 1B BA 29 63 C0 DD 5C BC 5F 0A E0 37 0D A8 6B 7A F5 EB DF CD 88 84 B6 70 22 22 46 89 85 0D BD 64 23 E5 53 96 24 0E 40 EB 8C EB DB 5B 78 7F 48 F0 E5 E5 E6 93 14 D2 C8 D6 ED 27 DA E7 8A 4C B3 15 DB 95 2C A1 57 24 8F 95 36 81 CE 00 C1 15 A2 93 8A 7F 3F 5D 7F A4 44 61 69 5E 5D 6D F7 25 FF 00 0E 72 33 78 78 F8 B3 C3 77 BE 51 BC FE D0 5B B6 BA B6 F2 AD DB 6B 9C 60 82 E4 84 EF EB B8 60 E3 39 C5 39 3C 3B 73 A2 6B A3 5A D4 E5 B8 9F 56 90 FC 92 DD 79 10 AF DD DB 92 88 64 0D D8 13 B9 0F A1 27 24 77 1E 17 2F 67 E1 3D 32 5B 54 85 D5 DC 9B 99 19 82 AA 47 97 2C 4B 67 8D BF 43 CF 07 00 96 5A 77 A7 EC 7E 29 4B AB B8 E5 6B 2B E4 F2 C4 C4 98 99 15 C6 39 E1 4A 95 CE 3A 86 0A 33 92 78 AC A7 4E CD 59 F9 7E 16 FF 00 80 4D 95 94 ED AD FF 00 5F BB EF 3C 7F 57 F0 E5 DE 97 E1 4F 0F AC D2 36 FD 5A 77 9F C8 C7 DD E1 42 9F A9 56 CF 4E F5 E9 77 91 33 3E 9D 1B EB 8D 3B B4 4B B9 E6 95 1A 0B 6C 67 03 09 B0 E4 03 CB 7C A5 B0 39 18 C0 BD AE 68 D7 3E 2C F1 3E 93 31 8C 3E 9F 62 0B B4 91 7D DD CF B4 10 AC 71 BF 6E D6 3B 80 1D 47 19 07 37 2E 5E 2B CF 17 69 C4 CD 1C 70 45 1A B4 6D 0C D9 42 BB 8F 97 83 C7 DE CA 0C 0F EF 63 27 AD 4C 62 ED E4 DF E4 BF E0 19 4A 1C 8E 56 DB 45 F3 6F 53 CB 75 1F 87 7A 35 A6 BB 34 77 02 E2 75 12 10 CB 6B 27 90 85 89 E1 54 32 C8 7B E3 05 BA 02 73 91 83 D6 69 3A 46 A9 A1 D9 DC 49 A4 69 32 DB C4 19 8A 35 A1 B7 92 02 A1 71 C9 B8 91 64 46 04 BA B6 E0 48 39 1D B9 B7 1E 8D 73 AA 58 DE CF 1D C4 72 5F 45 72 19 64 38 8F CE 60 18 B1 03 A0 27 EF 7E 06 BA AF ED F9 A5 D2 1A 77 B1 BB B5 BD 85 4B 6D 9E 06 58 81 0B CB 33 12 AB B3 AF DE 61 D3 D4 0A EB A7 39 4A 8A D6 EF F4 E9 A1 D3 4D B7 AE F7 7F A9 E5 3F 0E A6 9B C3 3E 20 F1 56 93 2A 5E C4 F3 46 BE 53 5A 58 BD CB 42 DB 98 06 D8 8A C3 0B B8 F5 E0 ED C7 3D 2B B9 F0 9D E5 A5 97 88 B5 1D 36 3B EB 55 86 59 73 03 A3 29 32 95 6E 10 36 70 78 C8 2B 82 7A E3 18 A6 5C A6 A1 E0 F9 67 D6 A1 D0 4E AD AB 5F 86 DE 90 5D 98 F8 04 B1 8D 57 69 DE C3 EF 0C 02 48 0F D0 2F CD C1 EA 1F 13 74 2B BD 6E D6 7D 5F 43 BD DF 1A F9 3A 85 9D C4 51 C8 19 94 91 C8 3B 43 1E 87 05 57 04 74 E2 B6 6D CF EE 5F D7 E8 53 9A 8D FD 6E 32 EB 58 D4 75 CF 88 BA B4 3A 75 C7 87 A7 8E 1B 82 D0 DF 6A 7A 74 58 42 06 C5 02 45 46 27 1F 29 0C 4F 3E 52 E4 81 95 3B 8B E1 1F 15 EA F7 76 F2 26 A1 A0 0D 3E 16 30 5C 41 A6 DF 5E 98 DA 27 2A 0C 58 04 ED 40 00 C2 29 55 51 D8 03 5C 7D B4 9F 0D E5 D4 6E AE A5 8E 15 B7 69 0B 47 6D 70 F7 4A 70 5B 38 06 35 23 81 90 39 E3 23 EF 60 E7 B1 F0 85 D7 C3 3B 8B BF B2 68 FA 74 AB A8 CB 08 57 93 6B 1C 90 41 26 34 95 DF 3C 8C ED 01 8E 01 04 11 9A 5C A9 25 A6 CB FA FE B5 31 F6 B2 6D BB FA 6B 6F BB FA 47 62 9A 74 1A 26 99 18 87 5D D6 2D 34 EB 35 76 30 DB 5B DA F9 4A B0 B7 EF 19 99 21 38 C9 05 8A EE 0C 72 C0 0C 82 A3 CE F5 6F 19 6A D7 9E 2A 93 51 8D A0 D5 6D F4 AB 37 FB 25 C5 8E 9F 28 88 34 B1 86 62 D8 98 34 67 68 65 2C 58 ED D8 4E CC 83 8F 42 4D 33 49 D4 2F A1 94 68 F7 76 81 64 DA CB 78 65 85 36 B1 E0 A1 19 01 83 60 05 CA 8F 98 FB 63 87 F0 7D CC FE 0C 9E EB C4 0B 0B C9 E1 0D 46 F5 E2 17 71 00 64 8D 11 9D 63 91 D3 19 08 4B 10 40 00 E5 47 4E 15 E2 29 2D 42 75 1D D6 CB F1 DB EE D0 EC 7C 37 E3 18 3C 4D A4 CE C9 FD 9D 09 D3 A6 62 60 B8 BE 32 6F 8A 25 04 4C CD 24 7B F6 EF 29 F3 15 05 4A EE 24 9F 94 EC 5E 5A CF 7B 63 34 BF 69 8D F4 A9 1C 4A 16 DD 77 B5 C2 96 04 B4 8C ED 83 16 09 0C 8B F7 91 7E 53 86 D9 58 1F 0F AC A1 B0 F0 74 5E 25 BE B3 2B A9 5C 09 EE 27 98 95 59 2E 43 31 20 B3 3E D0 A0 85 18 05 82 1C 86 3C 90 46 AD D6 BD 6D 62 B0 4F A9 D9 5E E9 F3 C2 8C 8F 71 A9 C7 BE 3F 25 B0 CE 5A 48 98 C2 19 D9 02 85 2C A4 12 30 B8 21 5B 39 D9 5D 5C D2 9C B9 E2 B9 8F 2E F8 67 AE C3 E1 BD 4B 5C 92 5F 0A EB 77 37 B3 CC 78 D2 ED 8C B1 C5 19 3B 82 ED DC 00 1C E4 1E 78 23 1D 79 A3 F1 1B C6 5A 6F 8A 6C 60 B2 D1 A2 BB 8E FA F6 F7 7D E5 A4 C2 45 78 DD 51 23 55 2A 49 42 49 5E A3 04 01 82 01 27 3E AD E0 47 8E EE D2 5D 55 65 32 4B A8 5D 37 EF 22 6D E3 60 DC 72 70 08 C1 65 65 C9 C7 70 0E 70 0F 17 E2 2F 0E 9F 1D FC 73 7B 0B 9C 3E 9B A6 5B 44 6E 00 56 03 66 37 6C 24 30 39 62 C7 91 8C 0E DC 73 0B 9A D1 83 F2 FC 35 25 49 B8 C9 AE FF 00 9B 3B EB 0D 17 53 D3 74 DB 4D 3E C7 5C 36 50 5A 42 B0 08 65 B7 8A 43 95 18 DC 08 EC 7A F3 CF 3F 85 15 E6 3A 86 81 61 E1 FD 4A E7 4D D3 2E AF 1E D2 19 08 45 9D F2 50 FF 00 12 8E 07 1B B3 8F 5C E7 9C E6 8A 89 39 49 B7 7D C8 E7 E5 F7 6D B7 99 EA 1E 2F D5 AE AD A1 86 1D 35 E4 FB 5D C1 68 92 35 89 F7 ED 04 87 20 67 19 24 2E 0E D3 C6 EC 7D EC 8C 6F 21 AD BC 2F 2E 8D 65 3D B6 9B 79 71 B0 BD DE A0 5E 16 53 BC EE 75 52 A3 76 D0 AA 54 6E E4 9F 9B 6E 39 EF FE CB 0F 95 E5 3A 79 89 E6 79 B8 94 97 C3 6E DE 0F CD 9E 8D C8 F4 C0 C6 30 2A 37 B4 8A EA DA 68 9F 72 A4 B9 47 08 C4 02 37 1C FD 33 93 92 39 E7 AF 4A A6 AC DA DE E7 46 CD 34 79 46 83 E1 EB 08 6F 22 D2 B4 98 D6 7B 0B 57 DF 23 CA E4 35 E4 9B 95 4B 1D AA C7 19 65 E8 30 AB D4 8C E4 F5 BE 22 D7 6F 2D 2D 4E 8F 05 87 91 7D 70 48 5F B3 B8 91 4A 1E A5 70 03 64 9C F5 51 DC D6 E4 B6 B1 68 BA 54 90 59 A0 58 5B 79 58 D9 41 09 FB B2 70 3D 79 5C FC D9 3C 9E D8 03 98 F0 84 71 EB 9E 20 D5 35 0D 46 34 9E 78 9A 33 1E F5 C8 43 B8 90 40 3D 08 D8 B8 3D A9 35 AA 8B EA 42 6A ED 2D D7 EB FD 7D E6 DE 81 66 DA 2D AC 76 91 4F 65 2C 6E FB 03 06 DA 65 94 31 F3 30 D9 3C AA AB 0D 9B 7F 83 A8 E4 2C 1E 2A D3 BF B6 7E C8 58 FD 84 5A E6 66 B9 B8 88 14 55 FE 30 5B 27 1B 76 82 77 61 5B 23 04 E0 E3 A1 5D 3A DD 35 07 BF 50 FF 00 68 71 B5 9D 98 B7 CB 8F BA 01 CE D1 90 09 0B 8C 90 09 CD 64 68 9A C4 D7 7E 26 F1 0E 8E 61 82 3B 5D 29 ED E3 B7 F2 94 83 B5 E1 0E 73 CE 38 27 03 00 71 5A A4 E5 F2 FD 2C 5F 22 B3 4B 62 26 D0 5A E2 E5 A2 D5 35 29 6F 65 30 17 44 20 2E D7 53 80 E8 9F EA CF 04 64 32 9C 12 32 48 6A A1 E2 AB 8B 91 E1 73 72 F0 F9 32 5E A8 8E 58 C2 12 73 B8 14 19 24 30 01 43 F1 B4 67 76 48 43 91 4C D5 FC 4D 79 1F 8C A3 D1 96 1B 5F 28 4F 12 47 31 8F 32 46 5D 40 2C A4 9C 06 C3 B0 1C 77 E7 3C D5 DF 11 E9 22 3B 6B 6B AB 8B BB 8B A2 2F AD 22 29 20 8D 03 07 99 63 E5 A3 55 6F 94 4A CC 39 E0 FA 8C 83 3C BC DE EA EE 4C 6C F4 EA 8B 96 F6 A9 15 BD 86 6D AE 26 86 DD 12 DD 91 91 B0 5D 64 50 1F CB 62 06 15 97 76 FD B9 C6 D2 A4 8C D6 AC AE AF 1E 67 55 10 4A DE 53 C7 70 54 0C 12 54 60 60 E4 B1 2A 30 4F 43 D8 F0 6A 5A 68 D6 02 73 74 D6 F1 C9 2A B6 15 A5 51 23 A9 56 6C 36 F6 05 C9 E8 39 63 8D A3 18 AB 50 D8 47 1C 72 C7 2B 9B 88 DE 73 3A A4 B1 A6 23 3B 83 00 A1 54 74 61 B8 13 96 C9 CE 69 C6 5C DE FA D9 97 D1 27 D0 60 B3 68 AE D5 E2 DC D1 16 69 5B 75 C3 02 1C 9C 74 C1 DC 0A B3 70 C7 0B B1 00 03 A8 C5 86 CE F1 3C 43 7D AB 48 26 6B 68 A2 63 19 60 AA F2 7C B9 08 14 A8 E0 67 AE 41 CA 8C 93 F3 57 4B 1C 49 1E 76 E7 24 92 4B 31 27 92 4F 53 DB 93 81 DB B5 43 A8 CA 60 D3 2E E6 0A AE 63 85 D8 2B 8C A9 C2 93 82 3D 29 38 39 B4 BA FF 00 9E 84 F2 73 34 BC CC 0F 0A A4 90 78 7D EE 9A 10 EC D3 99 06 41 CE DE 03 11 80 49 38 DD 81 8E 4F 1D F3 5B 06 DD A3 85 CD DD CB 18 BC A9 16 69 9A 40 8D B7 39 52 59 55 71 85 DD C8 23 19 EE 79 09 15 B3 25 FB C5 15 C4 B1 47 1C 2B 84 4D BB 7E 62 F8 E3 18 1B 7B 63 19 FE 2D D8 14 B3 5D 32 78 86 CA D3 CB 8C AC 96 97 12 17 2B F3 82 AF 08 C0 3E 87 79 CF D0 7A 52 8A 4A CB A2 26 31 E5 8A 4C AF 36 91 A9 34 F2 4F 07 89 2F D0 96 2F 1C 12 41 6E F0 AF 39 0A 40 8C 39 5E DF 7C 36 3F 88 1E 6B 9D F1 16 8D E2 AD 4B 4C 93 9D 3A F6 55 91 42 42 22 7B 53 80 4F CE AC 65 95 7A 13 C1 0A 4F 73 F2 80 64 F1 A5 E5 DE 93 7B 67 35 85 DC B0 6E 8A 57 31 A1 1B 37 07 4E 4A E3 04 9F 30 E7 39 E8 3A 73 9C 8D 37 C6 7A 94 D7 90 DB CD 15 AC 8D 3B 24 32 4F E5 95 91 97 71 C7 2A 46 31 B8 E3 D3 35 BC 62 E4 EE 89 75 23 1D 19 3E 97 7D AF E8 5A 6C 50 DE 78 58 5D 18 4B 81 2A DF 5B A4 6A 54 C9 92 A5 A4 E4 ED 2D 93 B5 0E 01 07 38 CD 67 69 CF A5 1D 5E 0B FD 27 C1 33 DB EB 56 F2 1D FE 56 9F 24 30 A2 14 DC 51 8B 20 0B 26 D2 39 00 12 70 41 21 B6 B7 6B 73 AD 5C 0F 14 49 A5 6C 8F C8 48 A2 94 30 DC 1F 26 54 5E A0 F4 C3 1E 3F 3C 8E 2B 62 0B 21 6F 03 45 14 CE A1 97 19 54 41 F3 92 4B 49 80 B8 DC C5 B2 78 C6 7B 72 73 2A 6B 99 F7 FF 00 32 9C 55 9C 16 FF 00 D5 8C CD 63 51 BF 5F 0B DF DD DB 5A 5E DB 5F 45 03 F9 31 2C 4B 33 34 BB 48 50 15 77 64 6E 20 E7 03 A6 4F 19 AC C8 7C 39 73 6D F0 CA DB C3 F6 82 3B 5B C9 2C 4C 0F 0C B3 7C 8D 2C 91 B7 98 0B 61 CF 0C CC F8 5E EA 06 42 E6 BA D8 D1 91 48 69 1A 43 B8 9C B6 32 01 24 81 C0 1C 0E 83 BE 07 39 3C D3 16 D6 14 8D 63 8D 4C 71 A0 45 54 8D 8A AA 85 39 00 01 C0 1D B1 DC 70 72 29 5C 1C 35 EF A1 4B 4D B0 9B 49 F0 F5 8E 9A B7 2A EF 6B 14 56 FE 70 80 FC CA B8 5C ED C9 C1 20 75 C9 00 F2 41 03 14 D4 8A F6 FB 51 BE 8A F5 2D BF B3 82 79 31 C6 A5 8B BE E0 0B 16 21 B1 8C 63 19 00 E4 91 D0 06 79 BF B3 19 6D 65 82 3D 46 F9 0C B3 3C A6 4F 30 33 8D C4 9D 8A 58 1D AA 33 C0 1D 30 39 AC AF 0E 6A 57 1A 86 A3 AC D9 DD 14 73 67 2F 92 B3 85 DB 23 AE E7 03 71 18 1C 63 8C 01 D4 D4 E8 F7 1E CD 23 2E E7 E1 BD 82 EA F1 5D E9 92 5D 58 F9 AC 45 D4 B6 D7 8D 0C A2 3D BC 2A B2 AE E6 1B 82 64 33 63 03 B9 C6 22 B9 F0 D6 A7 A0 7F 69 5D E8 3E 25 7F B5 CD 9B BB D5 BE B6 82 46 90 E3 0A 77 28 42 8B F2 B7 66 1C 1D A3 20 83 BB 61 7B 37 F6 FC 30 6E CC 57 11 5E 33 29 E4 A9 86 E1 55 48 27 9E 44 A7 20 92 06 D5 DA 14 67 35 7C 5F 7F 32 69 9A B5 AA ED 54 4B 68 18 30 EA 7C C9 1D 58 7D 30 83 F3 34 4E 2D 45 F7 09 25 1D 1F 73 9E D0 3C 26 7C 45 63 26 A7 7B 73 32 4B 34 CC 41 00 7C FE AD D3 D7 3F 95 15 D4 78 35 DF FB 36 E2 DC B9 68 AD E4 8D 22 52 07 CA AD 6F 0B 91 C0 E7 E6 76 3C FA FA 62 8A D1 B7 0F 75 74 33 F6 29 AB CB 73 FF D9";
            } else if (rand == 3) {
                c.setLIEDETECT("페인형자판기");
                packet = "1A 17 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F6 E8 27 BD FB 22 29 B3 68 A5 56 85 4A ED 4C 6D 21 77 90 03 E0 63 2C 31 B9 B1 B7 20 30 C6 64 B6 BF B6 BC BC 9E 2B 7B AD D2 C0 13 CE B7 65 C3 47 B9 4B 29 20 80 C3 20 8E BC 7C A4 70 41 A9 E4 DA DE 52 CD 26 C6 66 01 51 64 C6 E6 1F 36 01 E0 9F BA 78 EE 33 91 8A CD D4 34 9D 33 53 BB 8F FB 46 DE DF 50 0B 33 2A C7 73 68 93 08 73 18 25 41 DB F2 67 68 6C B1 E7 76 3B A8 02 D0 1E DA 2D 89 DA D0 5C 22 9B 4B BC C0 64 63 32 48 4C E9 28 2C DB 90 EE 27 03 E6 6E 98 C1 0A 3E EA ED 36 2E A1 92 59 AD 5A 36 95 7C 99 3C C2 16 40 A8 E3 05 4A BF 04 91 86 2C 00 FE 24 19 22 B8 85 92 CB 42 86 2B 6B 7D 76 EA 2D 4A 5F BF A7 5B DF 0B C9 5D 99 77 6E 26 55 70 87 1B DF 76 23 42 58 6E 38 19 A3 4F B1 D7 B5 0D 36 1B A8 B5 3B 39 F4 DD EE 90 DA DB DF BC 06 48 C9 5D A6 49 E2 DE 03 A9 5C 6C 84 2A 64 9C 36 DF 94 DA 8A DE E1 AE 97 D8 DB D7 75 CB 9B 6B C7 B3 D2 2D 46 AF 76 53 64 D6 51 DC 98 9A 0E 32 09 70 84 46 59 58 9F 9D D3 3B 06 CC 9C D7 39 E2 EB 8F 16 D8 E9 57 37 7A 6D 9F 86 A0 93 4E B4 2F 3C 26 57 9E 41 6D BB 3B 51 0A 28 55 61 19 53 C7 CD B7 8C 15 18 E3 7C 78 F6 D6 BE 34 F0 FD 8F 8A 74 A9 34 CF 06 C7 1B 8F B2 DB 11 E4 34 DF 36 E7 5F 28 82 46 59 48 24 2B 60 9F 94 64 D6 6F 81 35 6B 4D 2E FF 00 C5 10 58 5D 7D A7 41 68 F3 6D 6E 24 68 56 59 8F 46 58 DD CB EC E0 86 E5 8E 31 BB 35 9C 9B 94 5A 5E 7F 87 7E 86 C9 45 59 DA FB 7E 3D BC D1 E9 1E 01 F1 17 88 3C 6B A0 FF 00 6D 8B DD 32 D6 66 73 04 90 FD 8A 67 45 D8 72 31 FB F0 01 21 FA E0 13 C0 39 C0 C6 C4 5F DB 9A AA 7E E3 C4 FA 2C B2 C6 11 DA 0B 5B 67 DA 19 49 38 2C B3 6E DA 4E 33 D3 3B 40 C6 0B 03 E6 FF 00 04 21 58 F5 5F 12 68 17 09 6F 71 6D 6F 26 55 5E 20 E4 E5 B0 48 6C E3 69 D8 99 18 39 3B 4F 18 E7 B1 BB 81 34 6F 1B 44 6D 6C DE D2 2D 42 09 20 11 A0 03 E7 C9 50 54 23 0C 02 42 30 E5 70 1B 9D A7 38 6D B7 67 D1 AF D3 FC D1 2F DD E6 5D 51 D3 59 69 57 F6 53 46 B3 F8 87 53 BC 96 41 99 37 2D B2 A6 76 6D 2C 17 60 2A 01 DA 70 09 E4 8C E7 2D 54 AC EC EE 66 B7 BC 83 4C F1 16 A1 21 B3 95 AD 95 57 EC 82 30 EA A1 B6 E4 42 C5 40 24 29 18 C8 20 E1 71 8A 96 DF 5F 98 78 D2 E7 47 9C DB FD 9A E6 2F 3B 4F 9D 18 90 EC 99 49 A3 24 9C 17 56 00 EC 51 C2 E4 9E 6B 96 93 41 D2 27 F8 81 AA 68 FA ED AD BB DA 5F 5A 5B 5E C4 A9 2B 42 90 B4 4C 6D A3 41 82 32 58 38 C0 CF 04 ED 1B B8 34 39 39 30 8B 6E ED 9B BA 45 A6 9F AB D9 79 B1 78 9A F3 5B 68 51 49 36 F7 ED 0B 84 29 C2 B2 C4 E8 03 16 0C 77 38 DD D8 9E 32 27 D5 74 6F 0F E9 B6 0F 26 A7 7F 7E 51 3F 7C AB 71 AD CC 9B 8A B0 2B 8D D2 85 C8 62 80 12 46 0E DC 9C F3 5A 16 9A 0D 8E 81 A2 C9 6F A3 C3 15 93 AC 2A 8F 71 1D B0 79 5C 20 FB CC 14 02 EF 8C E3 39 E4 F4 3D 0E 57 8F 3F B5 6E 74 59 2D F4 D8 F4 A6 85 51 9E F5 AF CB 95 58 C0 1B 82 ED 1C 1D AC 72 72 0E D3 F2 F3 C8 53 A9 6B C9 89 CD E8 AF FD 7A 15 ED E3 F0 CE B5 24 FA 66 93 AB 7F 69 DE 45 12 BB 2D DD DD C5 FD B1 07 A1 74 32 79 6D D3 A6 78 38 3E 95 C9 78 2B 55 B7 97 C4 BA 86 8B AE 68 DE 19 B2 BF D3 5D 5A 3B A4 B4 58 89 29 20 0C 41 E3 9C 72 A4 63 07 07 06 B8 38 2F AE 21 8A DF 57 61 73 6D 6F 3C CD 12 DC D8 5C 43 6B B5 B9 50 EB 6F 18 05 4E DC 8C 92 41 F5 AB 5E 14 BD D1 6C FE 25 58 BD A5 BD E1 D2 AE 14 C1 20 BF 8C 3C 92 31 52 0B 61 73 FC 60 1E 33 8A 88 D4 93 92 57 F2 FF 00 23 8D E2 67 DF 67 F8 6D F3 3E 84 7B C7 BB 5B 59 34 F9 A1 76 CB B9 88 DC 28 12 28 42 30 48 57 C8 0E C9 9C 63 1D 72 47 CA CB 0C 16 F7 B0 4B 2D B6 A1 24 F6 F3 30 C6 D9 16 54 1B 7E 56 51 90 72 0E D2 18 1C F3 93 C1 E6 B9 AF 0B 5F DE 5A C7 75 A6 C1 61 35 C1 86 56 C1 25 63 54 CF DD DC 59 83 01 C1 CE 15 8F 3D 38 C1 BA 2E 6F 9B 5B 9A C3 CD B1 B1 BF 9D 3C D0 62 BA 92 E9 80 2A 14 9F 24 85 0B 81 18 C3 91 B4 12 00 04 BB 65 F3 5D 68 6D 06 E5 13 A3 82 DA 3B 73 29 46 94 F9 B2 19 1B CC 95 9F 04 80 3E 5D C4 ED 1C 7D D1 81 D7 8E 4D 55 B8 B0 8D A5 B4 58 AD 50 C7 02 32 C6 03 18 D6 20 76 A9 2B 83 F2 B0 42 E0 61 73 D4 6E 50 4E 60 82 DA E8 5E 19 AE EE DE EB 2C B1 CB 11 8C 45 04 64 7C CA F1 A9 04 93 92 A3 3B CF 24 FF 00 74 01 1D BE BB 06 B9 04 CB A3 34 77 49 B1 95 A6 59 82 88 DB A0 56 00 87 56 CF 3D 07 00 90 73 81 4F 9B 7E E5 BB A5 73 42 7F 22 CB 4C 2B 22 99 2D A3 40 92 79 B2 06 C4 7C 06 67 67 3C 80 32 49 24 92 01 EA 7A E2 78 97 C5 9A 06 93 A5 CA 97 9A C4 76 CC CE D0 05 89 D8 3E F5 C1 28 76 2B 32 0C 10 0B 01 90 18 11 82 41 AB 7E 21 B2 D4 B5 7D 25 23 D1 F5 19 B4 DB 95 92 39 92 71 18 39 19 E5 4A 36 3B 76 38 19 C0 3D F1 E3 9A 66 91 65 AB E9 BA 9D AE 93 2E A4 BE 2A D2 90 39 97 54 89 0B B4 69 80 23 8D 4B 37 95 B4 81 82 0E 47 03 38 38 11 3A 96 DB D7 E5 D6 CB C8 76 EA FA FE 7E 63 FC 55 7D AB 78 5A F7 45 D7 74 BF 16 6A BA 96 9B 72 CB 24 8B 34 EF B4 36 15 F6 94 27 85 65 60 42 91 90 3B 9E B5 EB F1 EB 11 5C DB C6 63 D5 B4 C9 1A 60 7E 41 26 DC 06 61 B7 04 36 72 AB 9E 30 0B 36 39 4E 95 E3 3A 9F 8A A7 F1 CF 81 A1 D1 E5 17 BA A6 BE AC B7 1B 6D 2C 03 2C 60 16 5F 98 A9 18 24 30 24 81 80 0A 8C 67 71 A7 78 5F C2 53 6A 3E 17 8F C4 F7 73 DE CB 35 B8 29 0A 97 1E 58 44 C4 7B 48 23 3D 31 8C 1C 7C A4 1C 1E B9 A9 72 F3 24 AE B7 F9 6C 2F 89 A6 BA 9E D9 15 C1 89 64 92 3D 4A 1B CD AE B6 F2 09 65 44 48 A5 03 18 1B 54 90 CC E5 01 52 4E 33 C7 A1 B5 1D 9C 6A CA 19 8C 87 01 A5 2F 1A 7E F9 C6 DC 48 D8 51 F3 0D 83 18 C0 1E 9C 0C 79 F6 8F F1 47 4B 12 5B E9 BA 9D 96 A1 A2 CB 74 64 29 3D C4 59 8D 99 9C FC E1 89 E3 2C 5B F8 4A 83 DF 02 AD 78 E7 C6 3A A7 87 B4 EB 1D 7B 4C 30 B5 97 9B F6 7B 8B 3B A8 59 59 D9 86 ED CA E3 AE 00 20 15 25 79 27 E6 ED D4 DF 6D 8D 23 4E 6F D7 FA EB F7 9D 94 0A E6 DE 28 F4 EF 2A D6 DA 28 D5 12 29 2C DD 76 8D A0 80 06 57 00 02 06 31 C6 08 EA 30 0A A9 61 AB 99 74 8D 3E 7B 1D 0E EC DB 4F 6B 1C B1 A4 46 15 11 29 1C 26 19 D7 18 18 E8 31 45 68 E3 2B FF 00 C3 7F 99 8F B2 E6 D6 EF EF 38 5F 88 FE 23 30 4B 65 E1 E7 D3 20 BE 82 F5 8D CC F0 4F E7 34 91 C2 30 FB B2 87 72 90 44 BD 32 02 A8 E3 1C 57 17 A1 EB 1A 6D C6 A1 2C 36 DE 22 D7 B4 9D 26 C6 19 26 4B 79 B5 5C 35 C8 1F 76 38 F0 15 63 3D 7B B1 3D BD 6B B0 1F 09 22 82 09 63 B0 D7 35 78 ED 6E 62 36 F7 2B B8 A1 69 54 EC 56 31 ED 1B E2 07 3C 12 3E 5E 43 10 6B 8B F1 16 8D 14 DA 81 D1 A2 BA 8E 4B 4D 11 58 EA 1A A7 90 91 AA 65 89 11 A2 AE 39 C9 20 2E 72 58 91 C0 1C 73 37 C8 F4 DF FA FC BF AE C5 BB 5F D3 FA FC 48 07 89 24 87 C4 B3 78 C2 08 E4 8A DA 15 FB 35 AA CC F9 7B C6 55 09 89 0F 25 8E D3 BD CF 3D 00 C8 2C 0D 77 7F 07 34 4B AD 3B 4E 6D 4A F4 CB E4 DF B8 36 B0 84 DE 88 C0 38 2E 48 C9 46 C2 91 93 81 86 03 24 9C 0E 43 C2 3A 05 C7 8A 6F 63 D5 A7 B0 57 D0 F4 D2 22 82 D3 7F 96 AF 81 B8 82 48 23 FD A6 27 19 27 19 E7 15 EA 97 FA F5 CC 51 D8 9B 49 5C EA 1B 8C 32 DB 09 12 65 6D A4 83 9D 9C 06 DC 08 38 00 F5 1D AA A2 F9 75 7F D5 FF 00 CF F2 23 E2 7F D7 A7 F5 DC DE B9 75 B6 BC 86 74 67 9A 32 5D E6 48 8F CC 36 23 06 93 6A 7C D2 1C ED 42 B8 6E 4A 10 01 5E 7C 6B C5 1E 29 D7 EC 3C 53 7F 71 7B 65 65 75 A4 3D C8 B4 9D E5 47 2D 6D 01 60 AC B9 C9 08 1D 72 A5 82 93 C9 C7 27 9F 42 D4 F5 FD 6E 0D 66 E3 4E B7 D3 AD EF 56 48 FF 00 79 BE F4 DB 34 43 68 27 6C 88 A4 E0 65 B9 3B 48 3C FA 1A A7 71 E1 29 7C 4F 67 1E B7 AA EA CD 04 37 16 7B E4 8F 4E D3 FC 89 24 46 50 71 28 76 9B 71 0B 91 B5 40 27 38 C9 E2 93 7A A9 47 A7 F5 FD 7D E6 D4 E5 1B 72 BF F8 27 21 2E A1 E1 59 FE 26 DB 41 E0 C9 2E 16 F6 E2 65 86 69 34 E8 55 60 89 54 0F 99 48 E0 8E B9 38 2A 71 CE 47 5D BF 14 5D 69 D7 06 DB 50 D3 BC 52 9A C5 C4 0C A1 99 1A 27 31 8E A3 F7 90 AA 85 24 E4 85 6C 93 86 2B 80 AF 50 E9 FE 0D F8 6D 1D EC DA 3C 36 50 DC 4C 42 AD BD DC F2 CD 30 92 56 2C 30 55 76 A8 0B F2 F4 23 70 23 9C 8A ED 65 F0 BE 91 1E 9F A9 E9 F6 B6 F6 90 DE 4C AD 89 88 4F 33 6B B9 74 0C 40 C8 40 F9 55 1E 88 00 E9 4A ED 43 7F 31 B9 2E 65 A6 9F D6 A7 21 E2 4D 77 FB 5F CB 4D 36 39 06 B5 69 B6 E6 C1 F4 7C 5C F9 72 60 F9 8A E5 A3 4F 95 B7 E1 B0 58 1D 9F 30 04 6D 6B BA 7F 8D AD F5 A1 0E A0 74 C4 D3 98 C3 10 5D 5A FE 61 6C B2 84 60 CF 08 9B CA 21 81 65 6F 94 63 20 39 C2 91 5D 96 97 61 A8 58 DA DA 5A BD F4 64 5B 42 A8 50 5B 9D AC 3E 4F E2 27 92 36 C8 06 31 F7 C6 54 E3 9B C1 12 22 6E 2E 72 0A 21 DD 2C 92 65 54 26 EC 36 38 0A 48 62 49 00 71 C1 3C 0A D5 A5 7B A6 64 9B 7A 36 60 CF A4 EA 9A 8A DD 5C 5A 6A 96 6D 61 A8 B0 95 A0 50 CE 19 08 8D 43 24 C1 B1 F7 23 DD 8D 85 49 72 0E E1 C9 92 DB C3 A3 54 D2 12 0D 6A E2 EE E6 DC E7 16 8E 12 14 51 92 17 FD 5A AB 7D DE C4 8E B9 2A A7 85 93 50 F0 E7 87 85 CC D7 FF 00 D9 2A B7 E1 1D C4 B6 40 C1 3C AC D9 66 0A E8 54 97 3B 0F 7C E0 90 4E 18 E6 AE A5 A2 6B 90 5B A4 5A 26 A4 F3 ED C1 36 DA 95 C3 79 1E BC B7 96 D3 38 24 13 B4 CB EC 72 BF 29 87 18 3E BF D7 F5 E4 4B 5C BB A3 CC EF FC 1F E1 DD 17 FE 12 2D 4E 08 67 B9 4D 33 2D 6D 63 34 FB A3 DF BB 6E E3 8C 31 0A 71 C6 73 D8 93 5C EE 9F AB DF C3 E2 BD 0B 5B 4D 59 75 9D 4A 79 63 0D 6E D1 82 CA 58 05 65 E4 F0 D8 F9 73 C7 40 7B 57 A8 DC F8 77 C6 3A AD CA 4D A9 7F 64 6D F3 37 DC 9B 10 F1 DC 0C 0D A1 22 69 43 A3 29 50 AD F3 01 CB 30 CA F2 6B 99 1E 1D D3 6C 35 A8 E2 D4 7C 3B 7E 67 9E 43 E7 B4 50 42 EE B1 15 0A 3F 71 09 F2 CA F1 BC ED 46 60 73 D4 A8 66 85 17 16 9F F5 D7 F3 39 A7 06 F4 5F D7 6F B8 DC BE F1 4D BE 9B E3 0D 57 4F 68 EE DA EE EE 0F 2D AD F4 AC CD 3A B9 40 73 1F CA 06 E5 07 77 23 03 9E 49 04 57 39 F1 2A EF 53 D5 F4 DB 39 75 0F 0F 0D 3E C6 4B 82 B0 EA BA 99 02 E1 03 12 CA 86 38 BE 64 50 37 0C 10 C4 80 0F DE E6 BA 8D 17 54 D1 2D 5F 4B 3E 16 D5 96 E2 DE 49 5A 26 B2 66 4D E8 9B C8 24 26 01 45 24 93 D0 67 20 D7 43 E2 4B 3D 1D 3C 2B A8 CD E2 11 15 D5 92 9D CF 21 6C C9 F2 64 2E 1B A6 FD F9 F9 40 55 1B C8 C7 5C CC A3 EE B6 FA 1D 34 65 69 F2 B5 AB FD 4E 13 50 83 41 F8 63 A4 E9 FA CE 99 79 70 FA B5 DD A2 AC 76 D6 F7 0C 6D 2E 5F 66 0C CE 0F 25 46 FC 81 9C 67 18 03 92 37 BE 18 78 56 E7 C3 9A 6D D7 88 35 9B 9F 2A E2 FE 33 34 F1 CA 76 88 93 EF 06 6E 40 07 EF 13 91 C6 7B 73 9F 3C 83 47 D5 7C 48 C3 5F BC D4 12 D2 78 C2 A6 95 65 71 6C 27 2F 1A E3 60 DA 46 D0 B8 23 04 83 B8 E4 E2 BD 76 DB 54 BD 9B C1 44 78 9E CE 68 E7 95 4D BC CD 0C 7B 44 99 C8 2D B5 B1 B4 60 1C EE C0 6E 36 E7 70 15 69 A4 DC 9A B3 FD 3F CF F2 35 71 BD A3 F7 FA 9D 04 C2 FE E7 47 90 44 23 82 F2 50 42 89 81 02 30 4F 1B B6 39 39 0B DD 58 64 8E 08 ED E4 57 9F 0C B5 9D 57 5F BC D4 BC 4D AA 5B 3D CC E5 0A C3 67 0C 82 29 89 DD B6 36 93 6A 85 1F 20 5E A4 F2 B9 20 B2 E7 D2 3C 2D 79 A9 DC C7 BE EE CE 3F 2E 70 D2 B5 D2 80 8C EC 36 AA EE 50 39 24 03 C8 E8 10 67 A8 26 C6 AB 71 FD 9D 18 40 93 35 C4 D0 B4 51 5D 41 08 7B 83 29 03 24 2E CD 84 E1 43 72 70 76 7D DC 0A 27 18 4B DE 91 0D F4 B1 E5 FA 37 8B B4 DD 37 55 7F 0C EB 3E 14 B0 D1 6D 24 C4 69 32 B0 F9 1C 2E DD CE ED F7 B9 24 EE C8 2B D0 E6 A7 F0 C7 8E 34 1B 68 DB 44 37 9E 55 8A 43 22 48 19 15 21 66 77 19 6F 34 B1 62 42 E4 05 0A 01 24 92 71 82 BB 9A EE 95 61 AC A4 B7 5A D6 9C B6 70 4E C2 32 D7 27 74 EF 86 50 24 44 1B 4A 36 00 DD 83 D0 9C 03 DF 6B C1 BA 7E 91 62 27 87 40 4B 28 AD 62 C2 B0 D8 CD 71 2E 76 E1 DD 8E 36 83 B5 C0 18 20 E0 1C E7 2A 33 82 77 B3 6B 6B 7A 8A DD 4F 2B BC BD BA F1 4D 84 BA 16 8B A5 DF 6A E7 79 55 93 7E 2D A0 61 9C 30 ED BB E6 7F 9B 70 07 E5 FB C3 8A B9 F1 0F ED AF A0 F8 3B C3 37 10 88 2E E5 20 CD 12 94 21 5C 62 31 8D A4 E7 92 C7 39 E7 3F 80 F4 07 F0 CC B6 FA EC 37 52 79 30 DA 48 A8 F2 86 8F ED 08 AC 00 32 2B 33 8C 9D C1 5C F9 A7 69 C9 19 E4 E1 B8 95 D1 5B 5F F8 B5 67 16 99 3F FA 26 97 02 CE F7 0A 1B 62 B9 66 7F 95 58 9C 65 9B A6 4E 70 49 CF 26 B7 A6 92 B2 F3 BF DC 74 C1 AB 73 2D 2C 9F E2 7A AD CC 73 DB 18 A0 B7 D3 AE 67 8A 38 95 15 E2 BB F2 86 07 18 C6 E1 CD 14 CD 5B C4 49 A3 DD AC 0D A5 EA 57 45 D0 49 BE D2 00 EB D4 8C 13 91 CF 1F CA 8A 7C CB AD BF AF 99 31 C2 4E 51 4D 42 EB FA F3 30 65 F1 6D C1 B5 B9 4D 42 C0 3E C8 A4 89 E2 82 65 1B BB 16 92 37 DA EA 70 38 58 DA 43 F3 10 72 42 E7 CE 3F B0 2E BC 43 AA C5 E1 CB 0D F6 F6 C8 16 E2 EA 19 3E 49 19 B1 F7 99 58 6F 18 0C 06 E6 EA 4E 46 78 CF B4 EA 51 5A FD 93 52 BC 92 C6 D2 59 AD D1 99 5A 58 43 67 11 86 19 EF 58 77 16 7A 5D F6 8F A5 4B 75 A2 E9 B2 FD B5 21 96 45 68 06 D5 67 68 A3 3B 7D 38 95 B1 EF 83 53 28 BB 29 3E E9 1C F0 97 B4 97 24 77 B3 7F 2B D8 CE 83 4C D3 BC 2B 6D 67 7D 61 7C 59 D2 29 12 38 C4 2F B1 98 10 AC A4 82 30 41 3C AB E4 E5 49 C6 57 85 75 9A CC 7F 6A EA 0A EF AA 5C 32 BD A2 AC 8E AC A7 90 4B 28 C7 CB 82 B8 19 20 E7 04 71 4F D3 B4 D8 A5 F1 BC B6 B3 49 34 D6 D6 DB BC 98 A6 90 C9 B7 04 9E 59 B2 ED 96 62 7E 66 3E 83 03 8A BD E1 CF F8 9B 78 8E FA FA F3 E7 96 0C 08 87 F0 AF 24 70 3D B1 FA 9A 9B DD AB 75 2B E1 D8 B7 E1 FB 33 A5 43 35 D5 CC 2F 34 D2 A8 96 49 E2 06 43 B4 86 6E 00 FB DD 07 DD CB 12 7A 63 04 EB A5 EC 92 DB 88 C0 68 EE A4 8D 8C 6E 6D 65 68 C7 DD 0A 58 10 31 F7 D4 95 24 1F BC 33 F2 B1 0B 05 94 76 F2 59 C6 CC D2 BC 08 FB 25 60 AA C0 70 36 FC 81 46 DC 11 C6 31 F2 A9 EA 33 55 E4 82 6B BD 72 EA DE 4B EB A5 B5 48 22 90 43 1B 04 19 62 E0 FC CA 03 8F B8 0F 0D EB DB 8A BF 86 C2 4D 47 47 DC C3 D7 F4 DB CB D9 ED 75 78 6E 62 B2 9E C2 4C 30 D4 3E 48 C7 20 83 B8 67 20 93 D9 B1 82 07 04 1A 6D C5 F6 A3 A8 BC B1 86 9B 63 5B 80 65 D2 A3 92 48 E4 39 F9 CC 72 EE 40 1F 0A 3A EE C6 30 09 24 A9 D8 D1 6C ED 45 FD F8 6B 68 A4 B8 B1 B8 58 12 EE 41 BE 77 06 08 D8 96 73 C9 3F 39 1D 86 30 31 51 E9 32 06 F1 5E A7 12 C7 1C 69 15 8D B2 22 A2 E0 00 B3 5D 28 1F 92 8A 4E 1C 9E E3 FE B4 46 92 95 9F 2F 5B 7F 97 F9 99 BA 5E 91 78 B7 D3 5D 5B 95 92 FA 19 1A 36 B8 D4 6E 5E 59 63 05 77 08 CC 69 F2 F4 65 39 0C 78 39 C0 FB A3 AB 8F 75 B4 0D 35 EC F1 97 54 CC 92 00 63 8C 28 C9 CE D2 C7 6F 07 93 9E DF 4C 65 68 37 AF 73 69 A4 4E EA 03 DF 69 C2 E2 5F 9D DB 0D F2 36 06 E6 38 19 95 BD FA 0C F1 5A 2B A5 D9 C7 2E F8 A0 48 B7 2E D9 16 35 01 65 5D A1 40 71 8C 1C 00 00 EE 00 C0 E0 90 6D 25 A5 F6 06 EE EF 2E A2 5B AE D8 10 59 48 AF 14 CC 67 13 6D 46 8F 6B 38 62 06 D2 B9 DC 19 B0 DC FA 92 7A 15 BE BA 92 D5 23 23 C8 01 E4 54 0D 2C A6 31 B9 9C 00 B9 DA C3 90 48 1E AD B4 0F BD 91 0E BA 63 87 46 B9 BD 7B 68 27 96 CA 27 BA 80 4C 81 82 C8 8A 48 61 E8 7A F2 30 79 35 36 93 73 25 EE 91 67 79 2E 3C CB 88 23 95 95 46 02 96 50 48 1E D9 F5 CD 4B 69 BB 19 AE C5 09 EF 2C EE A1 11 26 A4 8C 5C 03 3F F6 72 96 95 D8 15 E4 6C 2C 55 48 52 A7 BE 18 61 81 03 34 35 5D 2D B5 C9 15 DF 4C BA 0E EC CA 26 69 D2 25 11 6D 23 07 82 D8 3F DD 2B 90 58 8C E2 BA 39 2D 23 95 E5 66 69 81 92 3F 29 B6 CC EA 00 E7 90 01 F9 5B 93 F3 0C 1E 9C F0 2B 9F F1 9F 88 EF 3C 3B 69 14 B6 91 C0 EC E9 2B 1F 39 49 19 50 31 D0 8F 5A 6A 3C CD 47 BF EB A0 D4 1C AD 13 3E F7 C2 3A 8D F4 B9 B9 8F 4D B8 4C B9 41 32 2C 81 59 8E ED C5 5A 32 B9 FB DC AA A9 25 89 35 9D 3F C3 3B 9B FB 08 6D 9B 5E 99 6D ED 5C 2D B5 AC C8 F3 43 18 40 54 30 56 60 77 71 91 92 54 64 80 08 C6 3B AD 1A F6 4D 47 47 B6 BB 99 51 64 95 37 30 40 40 EB DB 35 66 48 9D E2 99 16 79 23 69 01 0A EB B4 98 F8 C6 57 20 8F 7E 41 E7 DB 8A CE 1C B3 4A 5D F5 05 A3 3C F7 C2 E3 57 B2 B9 BF 89 21 D3 23 96 C1 CF 98 27 DD 99 D7 90 73 71 81 B0 0C 6E 23 CA C1 C0 C1 E4 ED 4B BD 7F FB 63 53 B2 FE D7 85 AD F4 D4 77 DD F6 76 69 63 66 50 4E 72 55 58 83 C0 E1 72 37 74 E7 35 7B 5C 95 61 D1 34 CF B2 C1 0D AF F6 8C 51 89 C4 09 B0 15 0A 30 A3 D0 7C DF 90 02 BB 08 34 EB 5B 7B 48 ED 52 21 E4 24 66 3F 2C 9C AB 03 8C EE 1D 09 38 EA 47 73 EA 68 8B 72 49 76 FE BF AF 32 AF 6D 7B 94 74 8B F5 BE D3 AD 2E 6D EC 0D BC 93 43 0C 86 23 95 40 1D 46 46 E0 B8 25 42 95 C1 00 E5 40 3B 43 03 56 35 57 92 2D 22 E5 9A 54 41 B5 83 CB F7 04 48 72 37 93 BD 48 08 0E E2 43 03 85 38 C1 22 AB E8 77 02 E1 2F AD C4 10 C1 15 AD C2 24 69 02 EC 18 31 45 29 CE 0F F7 A4 6F A8 EB DF 37 E1 84 40 96 D6 EC CD 37 95 1F 12 CC 77 39 20 05 DC 4F A9 04 E4 FB D5 B6 96 A4 EA BF AE FA 91 DD CE 8E E2 CD ED 5E E6 39 C9 8E 44 30 92 BB 71 F3 12 48 DA 46 18 70 48 C8 DD 8C 91 B6 B9 9B 4B 39 FC 3F 2D DB BC B6 02 CC CA 64 B7 92 56 2E 63 61 B9 48 55 C0 25 F6 F0 46 47 5E 09 E4 56 CE 95 A3 59 C2 25 99 23 0A 4D C3 36 15 55 4F 98 8E E3 CC 2E 06 F6 72 A4 2B 16 63 90 3D DB 36 64 06 C6 F2 D1 23 77 74 B9 9D D0 89 58 B9 4C AC 92 12 A4 F2 32 40 18 CE 00 00 00 31 42 87 3B 4E 2F FA EA 13 B4 77 E9 FD 7E A7 35 3E 9D 77 AF DF C3 6B 77 3C B0 5B 9D F3 20 95 02 34 80 30 0C 52 30 3E 50 0B 81 97 24 FC C0 FA 8A DE D3 EC 2D 74 0B 29 FC 9B 11 BC C9 82 D6 F0 7C F3 E4 E1 33 C9 24 80 C1 4B 31 00 60 9F 95 7A 6A BC 4B 23 C6 C4 B8 31 B6 E5 DA E4 02 70 47 20 1E 47 27 83 91 9C 1E A0 54 6D 69 1B 43 E5 12 C5 7C D1 2F CE 77 9C 87 DF FC 59 E3 3D 3D 38 C6 30 30 E2 92 29 BB E9 D0 F3 CF 10 6A 97 10 EA 65 63 B8 77 56 5F 30 07 46 42 81 C9 60 A5 5B E6 04 06 19 07 BE 70 14 61 41 59 7A E1 2F AF 6A 05 8B 13 F6 87 1C B1 3D 09 F5 A2 9F B1 52 F7 AC 7A 34 DB 8C 12 3F FF D9";
            } else if (rand == 4) {
                c.setLIEDETECT("스틱위습카카");
                packet = "C1 16 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F7 69 25 32 DB C7 3A 49 34 51 15 12 60 40 7C C2 06 1B 05 48 C8 C8 04 11 8C F3 C6 0D 57 B9 D5 AD 2C 0C AD 79 7F 6B 1C 5E 78 87 7B 48 91 88 09 8C 30 57 2C DC B1 EA 30 33 87 5E 31 96 A9 84 32 5B 43 71 1D AC 51 A2 ED 69 23 62 4B 13 2B 33 33 65 4E 38 C9 07 EF 73 92 3E 5C 64 F9 87 C5 C9 13 51 9E D3 43 B6 D3 6C 2F 35 1B DC 2D BB C6 E5 AE 50 AB 75 20 28 0A 9F EB 07 2C 79 39 C0 C1 A5 E4 BA 89 C9 25 77 FD 7F 5F D2 3D 22 52 8F AA 1B 74 B9 C4 A5 44 C6 09 50 C8 84 06 4C B6 0F DD 38 00 2E 1B 19 2C 76 92 A6 A5 8A 09 E5 62 D3 4B 2C 71 91 34 6D 10 E3 70 2F F2 30 60 C4 82 14 1C 60 8F BD C8 5C 00 3C A2 DE 2F 88 3E 16 D2 ED F4 7D 2F 4E D3 6E 7E CF 07 94 2F E5 66 DD 1B 4C DC 2C 6F 37 96 BD 42 7C 80 30 C8 5C F2 40 AF 58 B3 91 9A 28 2E 2E ED A2 B4 BC B8 89 15 E3 DE 19 83 00 58 C7 B8 7D ED B9 6E 9F ED 1A B7 B6 E4 C1 B9 3D 56 A1 64 91 B7 DA 97 C9 60 BE 6B 2E E9 4B B3 38 27 71 CE F1 9D BB 99 80 00 95 C6 31 81 C0 AF 69 15 D4 31 24 6F 7E 92 DB C6 E2 35 95 5B E7 20 1C 6D 72 DB B7 3E 54 29 20 AE 77 B7 00 81 4D 17 76 F6 BA 68 D4 60 B5 37 39 B6 6B 94 8F 4F 41 28 7C FC EF E5 B0 00 31 72 C0 F2 46 EC 64 0E B5 E7 32 7C 72 F0 F2 E4 8B 1D 51 2F 04 2D 19 95 AD D3 68 7E 31 98 FC EE 46 72 70 5B 23 90 0F 24 D6 72 69 3B 37 A9 AA 52 92 6A 2B 4B 9E 93 A7 88 6C 31 65 E6 4D 1C 51 18 ED 2D 92 71 1A AC 9B 62 0D 98 F6 80 4F 19 CF BA 36 00 03 95 7B 96 42 A8 BA 7A EE 87 7B 45 01 91 04 AC A8 0A EE 8D 73 B7 9C A0 19 65 C0 7E 76 F4 3C 79 F1 D5 94 FE 0A 93 C4 BA 2A CD 77 1E 9D 21 3F 61 79 04 04 2E 57 70 60 AB 83 B1 64 C0 03 72 F2 A4 E5 80 C5 83 E3 CD 07 56 F0 CC 3E 28 89 2E 9E 1D 3A 56 93 CA 3B 55 D2 62 A6 30 8E 32 7A 89 49 04 64 71 D7 B1 72 94 52 6D BD B5 63 51 6A D6 5A 1D 78 BB 73 7E 6D 36 C2 58 1D E4 09 09 61 16 DE 18 8D B8 04 B8 2A 01 3C 85 24 1E 0A 8A 96 C0 35 C4 E8 5A 09 E2 60 7E D5 1C 97 2E C6 00 C3 78 52 8D 90 72 5D FA EC C2 ED 18 21 46 38 49 7C 77 AD 85 D3 F5 AD 5F 41 B8 B1 D0 65 95 59 66 B7 B8 DC C5 08 C8 0E A0 82 46 E0 AD 9C 2E 40 DB C8 62 1A 4F 1B F8 AF 55 F0 A6 A1 61 7F A5 DE C1 71 A5 6A 0A CC B6 CD 12 61 58 15 2C CA 40 0C 77 67 9C 93 C9 6E 9C 63 39 56 51 57 5E 9B 6D D8 88 EA DF F5 73 D0 25 99 0C 71 5D EE B8 11 10 87 CB 10 C9 BB 0C 78 F9 40 DC 0E 76 E7 20 E0 03 90 32 4D 4D 69 74 97 91 19 A2 28 D1 16 21 0A 93 9E 38 21 81 03 6B 06 0C 0A F5 18 E7 07 20 45 05 CC 37 DF 67 93 EC E4 B2 8D E7 76 C2 6D DC A0 3B 5B 92 43 15 7E D9 E0 9E 70 46 69 C8 F3 68 B6 B0 1F 2A 7B 95 08 A6 E6 63 38 11 46 14 22 B3 9F 31 D9 C6 14 33 00 09 CE 1B 27 27 27 56 EC 1A 74 2F CE D0 3A C5 37 96 93 18 A6 01 18 2E F2 8C 49 8C 91 80 48 20 33 02 7B 0C E4 81 9A 8D AD 66 79 AD 25 92 5F DF 47 19 47 96 18 D5 41 C9 46 6C 06 0C 42 9D 98 C0 6E FD C8 0C B9 6F E2 13 2D C4 CB A6 C5 35 E9 36 BB D1 D3 6F D9 B7 03 82 56 43 B4 36 0B 0D D8 6E 30 30 33 BA B3 B5 3F 11 4B 6B 75 A6 5B DF EB 56 9A 7A EA 20 B4 4F 68 9E 60 18 00 03 E6 BA 95 60 C5 81 C6 C4 E3 F8 B8 F9 E7 DA A5 6B 7F 57 E8 4B 69 AD 0E 8E CB 54 B4 BB 40 E8 56 13 2C BE 5A AC 8C A1 9E 40 9B 8A E0 12 77 28 04 15 38 23 61 E3 03 35 14 16 CC FA 6C DE 5C 1A 7A DE 12 E0 48 91 E6 26 95 5D 98 31 03 9F BE 4B 15 CE 43 16 E4 9F 98 C6 34 CB 68 59 A4 B2 12 C7 79 14 B1 C4 D7 53 23 CD 21 8F 7A 3B 46 1E 4C 92 84 71 C1 C0 24 9E 08 38 F3 BF 13 34 D7 13 BD DE AD E3 E1 A6 CD 6F 29 11 59 5A 20 73 19 2B FC 3E 54 9B 88 C1 20 33 85 38 3C 81 9C 52 9C D4 7A 7E 5F D3 2E 3A F9 7F 5F D5 CE 8F C5 BA C7 89 7C 35 6D 3D F6 9A 9A 7C B6 36 D1 26 F8 25 B7 70 23 53 23 85 28 41 19 3B 76 86 19 E3 00 80 33 5B 1E 17 F1 20 D7 F4 14 D4 24 44 4B 8F 24 3C 90 24 88 78 E4 6E 1C E4 02 CA CB F3 63 94 3D 86 4F 1D A3 EB 72 F8 BF C1 FA A6 95 AB 6A 0B 6F A9 69 EB 29 99 E4 06 35 92 2D 8C BB A4 07 1C 0C 9F 4C 15 52 47 63 8D E0 0F 16 DA D8 78 7D F4 8B FD 2F 50 BE 22 66 44 FB 24 1B F6 2B E0 95 2D B8 63 24 74 1E 95 9A 95 A5 BE 8D 26 BF 20 B5 A2 9F AD CF 55 82 4D 35 AE 64 BD 8D DF 74 D2 A9 13 08 99 15 FE 55 45 1B C2 81 22 9F 30 11 92 C3 24 E0 FC BF 2D 8B D4 92 38 5A E8 B5 C4 A6 0D D2 08 2D CE DF 30 00 C3 6E 3A B1 C3 74 27 05 95 4F 15 C4 6B 9A 74 9A 5C B7 36 90 6B FF 00 D8 91 DD 46 71 7F 26 D3 E4 29 6F BB 92 57 1C 0D A0 E7 23 23 92 79 3E 5D A9 DD 58 E8 32 A6 BF E1 9F 15 6B 5A B6 B3 6D 26 2F AE 64 B7 66 85 B0 DB 57 7B 93 C2 B0 C8 03 2F 9F 55 AE 8A 49 54 BA 6F CB FA FE BE F2 B9 1B BD BF AF 99 D5 F8 AF 5A F1 47 83 BC 77 67 6F 2E AE 97 7A 2D F1 16 F1 C7 73 68 85 56 3E 03 29 55 54 07 1B FA A9 1B 80 00 9E 30 3D 5E F7 4E 9E 78 DE 38 AE 82 4D 3E E5 92 72 3E 65 8C 6F 64 DA A3 E5 3B 59 94 1C 8C 30 C8 6C E6 BC 83 E2 50 B3 F1 2F C3 4D 3F C5 76 6C F3 48 67 8C 49 2B 46 77 AA EC 28 CA E7 18 18 70 7A 05 5C B1 C0 CB 64 E8 E9 FF 00 11 BC 4B AB E9 30 8F EC 56 B4 B1 30 AE FD 4A 62 63 13 B0 C6 56 33 C6 49 C1 1F 29 2D 80 58 6D 3F 77 5E 59 38 DB AA 6D 3F EB FA B8 E4 B9 DA 7D FF 00 34 CE 8F 53 F1 B6 83 E1 CD 7A CB 4F D6 DF 54 B4 9D 61 32 71 70 D3 43 16 59 F9 62 AC 5D F7 6D 18 DC A7 0A 57 84 CB 0A E9 AD 35 5B 5D 6E 1B 4B DD 22 F2 3B AB 06 90 19 A4 86 62 58 1D A3 6A 15 0A 48 E5 94 B0 25 08 C7 39 04 83 C7 C1 77 FF 00 09 5F C4 2D 20 4B 31 78 B4 FD 32 59 CC B6 2A CB E5 4F 31 2A 62 91 81 6D 9F BB 53 8E 54 E4 64 11 90 2B 62 4F 06 78 72 C7 5A B2 D4 ED 34 08 ED AF 66 6D 89 24 12 AA 1B 77 D9 C3 08 8B 79 67 00 31 38 04 E4 03 86 E4 88 6A DA 3D C7 35 0D 13 DE DF 2E BD F6 3A 87 96 E9 B6 35 B4 11 3C 6C A0 E6 59 1A 36 1F 86 C3 FA D1 4E B3 45 4B 44 DB 2F 9A 1F 32 19 03 16 0C 58 EE 25 72 49 0B 93 C0 C9 00 60 0E 00 A2 A0 C7 73 2A F6 58 92 C9 65 B8 D5 2E 92 DE 04 28 D3 E5 61 C3 AB 84 69 64 62 15 78 3C ED E1 58 6E F9 5B 80 39 D8 3C 4D E0 5F 0C 03 71 FD AD 60 F7 B3 B0 59 AF 23 91 AE E4 9B 01 49 24 86 77 0B C6 00 63 80 47 7E FD 3E A9 15 BC F6 32 35 D4 31 6D BB 84 C5 35 AC FB 03 CC A1 1D BC B0 DB 80 0D C9 39 C9 00 06 C6 32 5A B1 6C F4 6D 2F 47 BE B8 BE D1 3C 35 6C 65 96 35 92 DF 6D AB 45 2A C8 54 97 C9 90 01 1A 6D 31 80 A3 1C EF 04 67 38 69 35 A8 4A 2F 96 CB FA FE BF A6 72 9A F7 8B 2E AE 21 71 A0 68 FA F4 57 BF 3D C3 DC 5E 5C C9 0C 26 3C 16 2C 9E 63 64 C7 C6 70 02 60 01 D3 A5 6E 78 43 C5 3A E4 B1 DC 41 E3 08 2C F4 EF 23 CB 48 40 77 59 9C F5 CB 02 CD 91 C0 E7 8F C7 35 D5 E9 DA 2A 5A 5D BE A1 71 2B 4F A8 48 A5 5E 5D C4 28 04 83 B4 2E 71 81 B4 60 9E 7A FA E2 AC 9B 64 9E 06 B6 78 8B 47 13 2A 0F B4 8F 31 64 5F 94 E7 93 CF A6 5B 9C 82 79 EE 68 8C F9 64 DF 35 EC CF 26 F8 BF E3 2B 3F 3B 4C D2 B4 BD 4E 78 2F A3 94 4E D7 B6 B7 0C A9 02 30 2A 73 B3 96 38 27 A7 20 7D 48 AC 2B 59 AC 6E 6F 52 2B 7F 8D 3A 9A 33 B1 09 F6 88 2E 15 47 FB CC EE 14 7D 4E 2B BE F1 1F 88 75 9D 23 57 B5 B6 F0 7D 8E 85 7D 6D 2A 96 96 2E 23 74 23 AF CF BD 53 90 3E A3 1C F6 AE 13 C6 9E 2A BC B8 F2 86 BB E1 38 C2 D9 C9 B6 56 9E FE 29 9D 9F 0A A4 29 09 85 CE DC 90 83 19 C9 C5 73 36 E2 EE FB F9 A3 BA 9F BC AC BB 74 B3 2D FC 46 F1 2C 9A EC 7A 57 85 B4 39 ED F5 1D 46 F6 35 17 93 D9 CD E7 21 62 72 63 46 EC A5 86 E3 D3 85 4C F0 30 2E 43 75 E1 5F 0B E8 70 F8 67 55 B1 BE 68 64 89 D2 5B A8 ED 10 09 5D 8A 6F 70 E5 B7 02 AC 98 DB 80 46 00 65 CA D3 FC 0F FF 00 08 10 86 7D 5A 2B B9 A1 D4 AE 59 CB 34 51 BC 06 D5 49 C0 44 44 24 01 8F 52 D9 C7 27 B5 74 D6 DE 20 8E DD 2E 65 D2 6E B5 EB F7 88 BA 98 6E 6C A6 9D 65 C0 3B 59 19 53 00 16 18 E5 97 B9 ED 82 4F E1 6D EE F7 EB A7 6B 12 F6 49 6C B4 57 EF DF 53 CF EE 1B 47 9A C6 3D 19 BE 22 CF 26 91 B8 CB E5 CB A7 4C C6 36 5D A1 14 77 DB 8C F0 08 00 8C E3 26 93 C4 D2 E9 DA DF 8C 34 2D 3B 4A 9E 5B FB 58 D2 0B 70 C7 66 E6 00 85 DA 31 8E 30 33 83 8E 59 BA 0A 5F 0C E8 37 1A 86 E9 EE E0 6B D9 6F 65 59 9E 08 DE 32 CC A4 6E C8 DE C1 77 1C 9E A7 23 EB 91 42 D9 48 BF 15 DE 3B 5B 21 68 D6 D2 07 8E DE 30 80 C7 F2 8D A0 EC 01 72 37 00 71 9F F7 9B EF 1E 68 B6 F9 5B EA D7 7E 9E A4 CA EA 32 FB 8F 4B D4 22 9E CA 64 4B D5 82 D2 DA 78 5A 39 05 BC A6 EA 57 18 44 25 BC D0 15 86 D5 8C 12 51 8F BE 5A B2 F4 AF 12 F8 36 4D 6E EA 59 6E 24 B7 D5 CE 11 66 D6 23 0C 03 A8 63 E6 2B 67 08 0E 7A 06 41 C0 00 0A DC D7 AD 92 C7 4E 87 4E B6 84 A2 4D 38 60 C6 42 E5 F8 EE 58 E7 8E 00 1D 00 0A 07 03 03 98 F8 95 2E 91 A7 69 79 9B 46 86 5B D9 66 58 9B ED 40 EF 31 AA E0 34 6E A7 2A 3E 41 80 08 1F 31 CA 92 4D 6F 39 DA 52 95 F4 5A 6B DD FE 5F 71 97 2A 49 5B 73 6E 6F F8 4D 6F 2D A3 97 4A D5 74 79 A1 92 13 21 9E 28 5F 78 DC D9 22 3C BB 46 E4 63 03 24 7B E3 39 AE 47 C5 3E 01 D7 EE 2C 3F B7 EE 35 89 35 A9 22 3E 69 B5 9A 16 84 F9 59 CF CA B9 CA 9C 63 28 00 23 90 39 1C D6 7B 7F 0D 5B 68 11 EB 3A 5D F4 1A 5D DF D9 BC C7 8A C7 50 65 B9 DE 48 1E 50 06 46 C8 EA 49 C0 23 03 83 93 8B 10 68 5E 21 D6 74 45 D4 FC 2F AE 49 7B 62 49 DB 6D 7C AA B3 86 1F 2E D2 C7 2A C4 00 BC E4 71 8F 41 53 27 CC 9D 95 DA D7 7B FE 02 5B 24 CF 40 F0 37 88 A4 F1 16 80 F7 77 28 F1 4C 97 12 AB 23 E0 6D 4D E4 A0 1C 0C 80 A4 2E 71 C9 53 9E 6A 8E AD 6D F6 AB A8 CE 8F A3 D9 D8 5D DE C9 85 D6 27 81 23 92 35 68 8B B1 0A C1 64 32 E0 30 C6 38 EE 46 18 2C FA 25 E1 B0 82 D6 4F 10 69 A9 A6 EA 4B 1B A0 F2 06 E8 8A B1 5C 81 B4 9C 93 B1 09 CE 4F 1D 79 C5 74 28 13 4F 82 DE D2 28 E1 8C 48 ED 1C 42 24 54 44 18 66 1F 2E E1 9E 06 0E DE 49 E7 00 67 1D 3E EC D5 9B BF 70 8C AC B5 38 EF 0F 78 77 C3 7E 1A 79 B3 25 AD DB B3 2A 44 E1 4C 92 C9 F2 82 DB BA 8E 58 90 00 18 00 0E 49 CD 72 7A 56 91 7F 67 E3 ED 46 DA 2B 47 B7 7B 98 85 DC 30 EF 03 69 3F 30 01 88 20 00 77 00 4A F6 E4 76 AF 4F D5 63 5B 4B DB 4B 98 E7 B7 B5 33 4A 62 24 A4 6A 64 66 8D F0 0B B7 39 DC 13 1B 43 1E 0E 41 04 95 8F 4D B5 4D 47 55 B9 D6 1D 30 1B 11 40 C3 2A C5 14 E7 39 1C E3 3D BD C8 EF 59 38 3F 69 1B E9 E8 B4 B7 FC 3D 86 9D 93 48 CA F1 15 89 8E 11 79 76 AD 7D 10 94 44 63 95 72 65 45 50 08 64 6C 26 59 95 FE E8 55 20 8F AD 61 5D F8 2A EB C6 8D 6E 92 43 69 A2 F8 7A D5 89 8B 4A 85 30 E5 F9 3B A5 54 C2 83 83 90 01 E8 D9 CF 3C F6 7E 23 B3 6B 8B 29 16 28 0A 2A 96 B9 92 55 09 87 65 50 A0 36 7E 6C 90 7A 8F F9 E7 82 70 70 62 8E 3B 6D 5F C2 F1 5B 3C 93 B4 6C 1A 3B 87 8E E3 6F 95 F2 92 77 92 7E E9 E0 6D C1 E1 87 18 E4 6D 16 E3 51 FC 9F E8 57 37 57 A1 97 AE F8 7A D6 4F 08 5D E8 D7 9A 9A 47 0B C4 EB 04 2B 00 B7 89 58 67 60 44 5C 16 C3 00 71 96 CF A7 20 D7 29 F0 AB 4B B8 9F C0 B7 5B A0 97 76 2E 60 FD DC 71 F9 8D 82 87 CB 0C E7 8C E0 8C 74 F5 2A 40 35 D7 EA B6 1A 26 8F 61 77 97 6B 9D 50 ED 92 29 67 6D F3 19 43 02 85 49 18 CA B1 42 71 DB 6E 78 23 36 F4 CD 3E C7 41 F0 ED A4 F7 B1 C9 1C 16 6A 64 75 F2 77 28 77 2A C6 42 A0 16 05 4E 46 E3 8D A3 71 3C 73 5D 11 9A E5 69 3B B7 A1 52 9D A2 92 3C C2 2B 8D 37 41 F0 FE B9 A8 49 F6 B9 B5 7B EC A5 BC D6 69 83 6C 12 26 D8 EA EA C1 90 03 8D CD 9C 30 20 05 23 76 3D 2B C0 D7 32 5F 78 53 4D D5 6E AE 6F E3 66 B6 0F 3A 5D 48 59 59 86 54 CB B9 86 40 3B 77 6D 07 68 04 1C 73 9A C1 BB B1 9A CE E3 C5 DA 4D AA E6 39 F4 CB 86 82 DA 28 F9 66 64 F9 42 81 E9 92 00 C7 E5 DF A1 F0 51 9A FB C0 9A 58 4B B3 1C 6D 60 91 2C 90 B2 39 42 23 45 05 1B 18 05 48 6C 86 52 43 E4 74 03 25 5B 49 36 91 A5 44 A5 0B BD EF FA 1D 2C 09 22 F9 86 46 FB CE 4A A8 20 85 1D 06 38 1D 71 B8 E7 3C B1 19 23 14 55 3B 8D 6E D6 CE 41 1D C2 DC 87 39 23 CB B4 9A 41 80 C4 75 54 F6 FF 00 F5 8C 12 57 3D E2 B4 BA FB D7 F9 A3 9B 99 32 BC A9 A9 BC 32 5D 9B 9B 58 A0 78 F7 34 6B 64 ED 30 8F 93 B7 74 72 F2 C0 13 CA F7 E9 55 25 B9 D4 65 9A D5 22 BA D2 2E A6 2C 24 82 57 B6 64 8F 2C 8D 8D 8D BD B7 39 4D E7 0B FC 39 27 00 8C EE 49 68 B2 86 49 25 95 A2 6C E5 37 63 04 90 41 04 7C C0 82 38 C1 E3 3E C3 10 4B 7B 24 62 7C 2A 7E EE EA 28 46 41 FB AD E5 E7 F1 F9 CF E9 4A CA 2E E5 72 F3 BB 2D 0E 53 51 D3 BC 75 A8 DC 3C 42 F3 41 48 C2 E4 C0 F1 BC 80 06 0C BC 12 99 E4 64 7E 27 D6 A9 45 E1 CF 11 49 A8 18 2E 2E 62 42 11 A5 68 12 E0 18 19 99 CF CD 18 9A 09 48 3C 12 D9 2C 73 20 39 5C E2 BB D9 EC 23 99 23 01 E4 47 8A 53 34 72 E4 3B 23 1C E7 1B 81 C6 43 32 FB 03 81 8E 30 9A AD D3 D8 E9 17 B7 71 05 32 41 04 92 A8 6E 84 AA 92 33 ED C5 2F 65 29 B4 AE FF 00 AF 90 95 BA A3 88 B9 D0 F5 4D 32 1B A9 A5 42 B0 0B 66 90 CD 0E B1 3C 3B 64 5E 84 A4 4B 1C 64 90 54 00 B1 8F B8 77 31 24 57 13 A8 7C 3F D7 BC 47 AC 44 93 D9 29 B0 B3 FF 00 5C 61 BB 87 CD 79 1D 55 8A E7 2D B4 80 57 A8 EF 91 90 73 5E CB 67 14 37 F6 2A B3 41 10 8A 19 A5 89 22 45 01 42 A3 B2 0E 3B 7C A3 04 0E 08 24 63 07 15 9B A6 68 16 12 5B 34 F6 C2 6B 19 D2 E2 68 D6 4B 69 98 10 16 62 31 86 24 7C C2 35 CE 47 3F 96 05 46 FE F3 E8 6F 1E 6A 7C C4 6F 36 BA BA 7A 59 D9 78 7E 4B 14 8D 16 38 9A DA F6 1F DD A8 C6 00 0C A5 7A 0C 72 0F 1D 2B 94 D5 B5 6F 14 5A C9 25 94 F7 87 6D CA 63 CB 9D A0 95 C4 6E BB 41 02 1D 8C 32 43 E1 B1 D8 01 8C 12 DB DA FD FD E7 85 2E 6D 64 B7 BB 9E F0 CB 0F 94 56 F1 CB AA 85 23 04 05 DA 37 72 72 C7 24 F1 93 C5 59 F0 BD D2 78 8E 47 D4 6F AD 60 37 56 A4 47 13 2E EC 01 D7 A1 62 33 EF D6 A5 AE 69 59 36 27 14 95 DA 4D 33 99 D3 E5 D4 74 18 DF ED BE 17 4B C2 B2 98 E7 9F FB 41 5B 0C 54 38 0D 10 0C 15 B6 E0 8D C7 8D F8 DC 37 73 57 C3 30 DD D8 DF EA 57 AF A6 6A 36 53 5C B8 09 70 D6 2F 2C 9E 58 18 5D 8C 91 C8 80 F1 96 25 70 77 0D B8 C5 75 BA F5 CC A7 4C 9E CD 9C BA 5B 5D C7 00 76 39 67 1E 4A 3E 58 F7 39 63 E9 5D 5C 10 A4 28 42 6E C3 60 E0 B1 20 60 01 C0 3D 06 00 E0 71 D4 F5 26 A7 D9 CB 9E D7 F8 6D DB AA BF 64 66 E4 A4 BD 7F 47 63 CC 57 5B D4 35 0D 46 37 8E 79 2F 92 C6 E1 D5 A2 B9 81 BC D2 CA 46 47 EE A3 0C A0 60 70 62 2D 9C E7 19 E3 9C F1 8D F6 AD AE EB 56 DA 96 A7 A2 DC 5B 59 5A AA 79 76 17 4B 22 09 33 CC 87 78 5D A1 41 EA 49 5F 97 1E F8 F4 CB 6B 68 C7 8D 2F 63 84 79 1F BB 66 53 18 1F 2B 30 04 B0 04 11 92 58 9E 9C 92 49 CE 4D 52 B7 9D A6 F1 88 77 54 2F 2D D3 43 BB 1F 74 08 A4 21 87 FB 5F BB 51 CE 7B FB 63 15 09 4D A4 9E ED BF B8 99 C9 46 2F EE 3C CD D2 C3 C6 9A ED 9A AE 99 65 E1 CD 33 2D E7 5C A3 2E C6 65 00 95 0D 85 5C E0 8F 97 B6 EC 9C F1 5E 97 6F A9 69 7A 16 9B 6D 0D AE A3 69 15 B5 80 2E 20 66 30 B3 9C 38 28 76 E4 4C FC 3E 40 FE 20 AD CF 15 7B 52 F0 FD 84 9A D5 BB B4 65 66 16 F7 13 0B 88 71 0C A3 0C 30 A1 E3 0A C1 71 29 1D 72 76 8E 79 6D D5 3C 2B A5 C7 7C 2F 66 BF 9E E2 F1 B2 A8 86 77 04 C6 07 20 AB 00 1B 70 C0 C3 12 48 C0 39 CF 35 54 E2 E3 3F 67 D5 EA FE FF 00 EB 42 A4 AD A8 AD E2 4D 37 57 B1 93 4F BC 9A DE CA 67 52 C5 AD A5 8E E5 22 3B 89 DD BF 18 39 18 CF CA 31 93 CF 7A D5 8B C4 9E 1A 95 2D C0 D4 EC A3 67 B7 3B 37 4C AA D1 A1 DB 95 2D 9F 94 FD DE 32 09 C7 FB 27 16 ED 2D 6C E6 97 50 81 AC AD BC B8 E4 68 31 E5 83 B9 1D 16 46 53 9E A0 B3 9E 3A 7B 54 B6 91 C9 29 49 85 CC C8 91 CD 38 68 41 0C B2 7C E4 0C 96 05 86 31 C0 52 07 38 C6 30 07 42 8C E2 F5 6A EF D7 FA EA 66 A7 17 F3 BD 8C E9 64 B2 BE 82 0B 0D 2E 28 D0 18 5C C3 FB BF 29 1A 38 B6 A8 54 38 C8 43 E6 00 1D 43 2E D2 71 9C 8C EA A5 AC 17 16 B6 C2 48 63 0D 03 2B A0 89 C9 11 3A F0 76 B0 C1 F5 53 D3 20 90 46 09 15 46 EB 43 D2 27 D4 15 5F 4B B1 2F 28 69 64 90 DA C6 CC C4 32 E7 24 83 D7 27 3D F9 EA 2A 29 34 5D 15 35 7B 7B 41 A2 69 9E 5C B0 4B 21 3F 64 4C E5 5A 30 3B 74 F9 CF E9 53 05 27 27 CB 6F 3D FA 5B FC C6 E5 1B 6A 5F F2 20 47 8A 39 2E E5 0C E0 C7 1C 32 4C 0E 41 4F BB 8F E2 3F 21 6C 9C B7 0D CE 32 29 34 BD 38 58 59 C5 6D 20 12 18 89 75 90 81 80 4E 41 C7 70 40 27 9F 7F AD 54 83 42 D1 6E 2E EF 4C 9A 36 9C 4C 72 84 07 EC C8 73 F2 2B 64 F1 D7 2C 79 FA 52 1F 0D E9 29 7F 1B 43 66 B6 AE 63 7F 9A CD DA DC E3 29 C1 31 95 C8 E3 3C E6 9C DC A0 D3 97 A7 DF B0 46 4A 4A EB FA B6 9F A1 11 D2 A7 D5 B5 B8 B5 2B 92 56 CE 3C 6C B5 99 84 81 C6 D6 1B 80 56 DA BC 90 41 F9 89 19 07 F8 76 EC FD B2 16 B3 6B A8 A5 88 C2 A4 EF 77 7D AA 81 4E 1F 27 B1 5C 36 41 EE 30 71 DB 27 4D D1 ED 26 86 59 8B 5E AB B4 F3 23 6D D4 2E 3E 6D AE C8 09 F9 FA ED 51 CF B5 64 59 4C 12 D3 CC 55 97 7D E5 AE 98 F2 BB 5D CE CD FB E9 99 58 2B 17 25 40 04 91 82 39 27 39 AD D3 9B B2 92 B7 4D FE 7D BB 75 34 E5 94 AA 72 75 35 1B 4D 95 FC 5D 67 7F 6C A4 DB 43 6E D1 4B 29 90 36 E2 0B 2E DE A5 8B 03 D7 3F 9E 6B 1B E1 59 45 F8 67 A4 CB 38 89 62 84 4D 20 91 DB EE 1F 36 50 C7 91 C6 07 7C F7 3D 3B F7 20 00 30 33 D7 3C 9C D7 0D F0 9F 7C DF 0B F4 F8 96 46 89 BF 7E 8B 22 01 B9 73 23 F2 32 08 C8 CF 70 47 B5 0D B7 0B 3E 85 5D B8 3E 9A AF C9 9D A5 AC F2 4F 1E F9 2D 66 B6 6E 3E 49 4A 13 CA 82 7E EB 11 C1 25 4F 3D 54 E3 23 04 95 9F 71 E1 DB 3B BB 89 2E 26 B8 D4 BC C9 0E 5B C9 D4 67 85 7D BE 58 DD 57 81 81 D3 3C 73 93 CD 14 24 BA B3 3D 0F FF D9";
            } else if (rand == 5) {
                c.setLIEDETECT("탑의로얄네펜데");
                packet = "02 17 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F7 14 82 29 ED ED 98 DA 43 28 94 A4 92 17 87 CB C1 19 91 5B 63 0C 82 1F 07 07 90 4E 7A 8A 81 A5 BE B9 B8 BB 48 2D CC 6B 03 A9 5F 3D C0 59 98 02 70 19 1C 94 19 F2 F3 94 E9 9F 95 B7 70 F1 79 0F DA 6D E5 77 B3 71 2E 16 0B 9D DB 77 AC 9B 88 54 38 21 89 08 9C 06 E7 AE 06 00 3E 7D F1 7E 4B 6D 3F 47 D2 E2 BD F0 FB 6B 16 05 BE CF 1B 4B A8 49 18 8A 62 06 C2 FB 49 92 5C 80 4F 5E C7 27 24 61 69 B5 86 97 2E B6 B1 B5 7F F1 03 C3 71 5E CF A7 D9 5F 58 CB AA 08 DE E2 6F B3 A4 93 2C 6E 91 F2 E5 A3 5C 38 50 B8 61 B8 36 D5 C0 04 90 B5 89 E0 5F 1E 6A 5A D6 BF AE 58 78 8A C7 49 85 34 51 E5 2C B6 70 BE 77 79 81 42 A8 25 89 CB 2A E0 01 92 42 8C 13 8A C2 F0 B7 88 C7 87 75 97 81 35 58 35 44 86 3E 34 1F 08 69 A6 68 B2 40 06 43 29 03 78 00 26 49 62 DB 98 8C F0 41 C0 F0 46 9F E2 55 93 52 D5 A1 D7 AC F4 2B 2D 4A F2 41 70 D7 B6 EB 2D D4 CA A5 B3 B6 26 52 08 DC C4 1E 9C 83 E9 8A D6 30 5C DA EB A7 EB 65 FD 6A 66 DE 9A F7 FC 2D AF F5 A1 F4 63 2C DB 25 D9 22 87 6F F5 65 93 21 38 EE 32 33 CF 3D 47 A5 79 CF 8B 3E 22 4F A7 25 DE 95 E1 DD 2E EF 54 D5 E2 8D 5A E8 47 1C 92 C1 69 BB 25 B7 32 E1 F3 83 81 F7 40 E3 A1 52 B5 B5 7D 7F A8 68 BE 24 D3 6C 21 8E C9 E0 BE 98 B7 98 22 65 7E 5D 8B 03 F3 9C FD F2 79 18 CB 70 3D 38 BB 3F 0C 78 D3 49 BA D7 E1 D2 35 5D 26 3D 1F 56 66 BC 1A A4 EC CC 56 32 72 59 5C 02 03 6D 61 F7 B8 3D 54 F0 48 88 47 AC BC FE FF 00 51 EB 6D 8E 6F C6 BA ED CF 8B BE 1B 59 EB 32 E9 5A 7C 31 2D D1 56 9D 64 60 F1 CD FF 00 2D 11 13 60 CA BE 44 9F 79 B1 86 04 93 8C 7A 1F 82 2F AE ED 3C 0F A7 BC DA 7E AE 96 86 DA DD 04 86 11 28 0B 83 B8 C6 91 49 E6 60 F6 3B 37 02 E1 8E 54 61 7C DB C7 3A AF 87 74 AF 06 C3 E0 DD 02 F4 EA 0B 04 B1 4F 2D CC 63 7C 65 F0 FB C8 7D C4 73 F2 7C AA 30 39 E4 92 71 ED 9E 17 8E 1B AF 07 59 C1 14 F1 15 5B 78 E3 06 DD D9 4A 95 8D 40 DC 41 07 77 00 91 C7 04 0E 7A 9D EA 24 A0 D4 6F 6B FE 9F D5 89 BB E7 8E DB 6B F7 8F 1E 2B D1 6C 24 FB 1E A3 AA 0B 59 22 45 FD F6 A5 8B 53 39 CB 29 2A 1C 2E EE 57 24 A8 DB F3 0C 70 6B 9F BF F1 7F 86 6E F5 A8 ED A7 D7 74 AB 95 5B B4 9E D4 0B B7 54 46 48 89 04 C8 A0 AE 77 91 D5 B6 91 9E 37 29 DD CB 78 C2 E2 DD 6D B5 B8 35 19 75 CB CB 68 A4 2B 1C 56 DA 89 52 02 B7 3B D5 B2 0A 64 8C B6 D6 2B F2 FA D7 19 76 96 BA 85 A6 99 A8 C7 A5 69 1A 0E 8F 0C CC 63 0F BA 79 E7 40 57 7E E2 DF EB B0 49 C2 F0 47 4E 07 35 0A 9A 7B F9 7E 5F D6 87 44 21 CD EF 7A FF 00 5F D5 BF 47 F4 8D 8F 29 2B FD B2 6B 80 F2 6F 0B 32 AA B4 20 80 44 78 0A A4 00 08 38 6C B7 3C 9A 2E ED AD A5 B0 FB 35 CA 49 2D BB 14 46 4C BB 16 1B 80 01 B1 C9 53 FC 59 E0 8C EE E3 35 E7 FA 9C 31 C1 67 15 E5 82 4F 0D 9F 94 92 A4 70 6B 77 3E 5F 97 2E 76 6D 8E 39 14 20 01 30 00 F9 00 38 5C E3 8D A8 21 D5 B4 DD 16 5D 42 F3 C5 33 C7 F6 56 74 FF 00 4C 48 2E 21 65 56 28 19 84 71 C6 E5 88 19 0A 1F 21 88 04 B7 20 CB 8B 8E AC 89 69 EF 1A FA EE B7 A7 68 16 83 53 D4 BC A8 36 66 28 E5 94 8C FC C0 36 06 32 D8 25 40 21 41 3C 67 18 19 AE 7F 50 F1 07 84 7C 79 E1 FD 4F 4F B7 BC 82 F4 45 17 98 52 40 F0 B2 B7 21 19 77 28 39 DD 81 C7 F7 80 3D 70 78 9F 11 CF E2 58 3C 61 A4 6B 5A C6 89 2E AB 6D 0E F8 A0 86 0B 66 81 83 EF 61 CA 86 93 0C 08 CA 90 48 2A 14 83 93 9A A1 06 B2 DA 06 B9 AA EA BA 9C 30 B5 DE AC F2 2C DA 6C 12 C7 38 8D 4E EC C7 22 AB EF 0F EC 57 6F 3C 9C E4 0C 5D E4 9D FB 3F F8 05 46 CA CF D0 6F C3 9D 47 C4 FA 6E 8D 17 F6 73 5A 45 A5 CD A8 0F 35 E4 8C C9 21 60 AB B9 55 47 24 11 8F 7E 38 23 BF BB 4D 31 2E 55 26 58 44 72 46 19 DF 69 56 2C 71 B3 19 C8 6C 11 8E 9C B2 F5 E4 57 86 F8 2F C3 BA 6C DA 16 97 AB B5 B4 A2 E7 FB 4C AC 53 CB 29 23 00 8C 10 80 E0 00 7D 72 72 0F 6C 57 AB E9 D1 D8 CB 70 E1 65 BA D4 5A 4F 39 5E 66 84 98 99 49 24 C4 49 F9 30 72 3A 60 12 BD 40 38 AB 6F FA F9 22 39 6F FD 79 FF 00 C0 34 5E 5F B1 3D 96 9E ED 66 22 91 9D 4C 6A AA 81 61 1F 2A 00 85 BA 02 D1 A1 23 3C B0 1B 40 6F 95 F0 9B 44 90 20 86 34 99 5E 46 6D 8D 86 8D 19 D9 8B 9C E1 82 B3 27 38 04 13 EA 06 6A 5B D7 4B 7B 4B DB BF 24 07 48 4E 5B 90 5D 54 12 06 50 16 C0 24 F4 04 8E 70 0F 7E 2F FE 13 EB 3B 4F 07 DE 6B 9A 3C 5F DA 70 DB 5C 22 4D 6E 66 11 2C 0A 55 40 F2 C6 C0 76 03 80 32 A0 9E 4D 4B 94 63 A9 32 6F 67 D7 F4 D4 ED 2F 2E 62 B7 DD 23 5E C1 0B C7 19 91 84 CE 02 88 D4 A9 91 88 C8 C7 04 0D C7 85 C8 3D F0 66 40 B2 4D E7 A8 3C 06 8C EF 56 07 86 ED 9E 83 83 CE 39 E0 E7 00 57 2B 61 E3 9B 5D 4F 41 FE DD B2 DC 62 65 11 C9 6F 3E E0 B6 F2 02 49 2C 63 8D C8 52 37 65 CF CB F2 28 C0 24 D7 4F 0D FD BD C8 89 AD 5F ED 11 4D 1B 48 93 43 F3 46 C0 10 31 B8 71 93 9E 39 E7 07 D2 A9 D9 2D 41 5A 49 34 52 B3 56 B5 97 CB FE CF C3 C3 6C 02 25 B2 62 28 54 05 C4 28 CC 54 31 27 76 0A AA 8C 2A 86 DB 85 CD 9D 39 26 8E D6 25 7B 61 0E ED EF 22 B4 DB D9 5C B6 7D 30 73 96 3D B1 C0 03 1D 19 7B AB E9 96 97 06 CF 50 BB B5 80 C8 8B B5 6E 26 45 F3 37 12 30 14 9C 9E 9E 98 39 E3 3C E2 0D 37 51 D2 A4 D4 75 0D 2E D6 E6 21 73 0C CC D2 5A E1 50 A9 60 AC CC 14 00 4A 96 72 4B 1C E5 98 F3 D2 8E 5B 0D B5 7B BF EB FA FE AE 58 98 DD FC B7 05 E2 8A 14 FF 00 5D 14 AD 8C 05 6F BE B2 0E 9C 64 90 41 07 81 F2 F3 4B 6B 07 D9 21 8F EC DB 9A D8 8E 21 29 B4 A0 25 42 85 04 A8 45 45 C8 DB 8C F1 EB 9D D9 52 6A 56 76 9A 8C EA 22 4F ED 39 63 8A F4 58 4D 24 11 C8 4E D6 46 D8 47 DE 90 22 10 49 62 BC 28 DC AB 92 1B A8 78 C7 4F D3 F4 A1 79 0D DC 1A 94 70 AA FD A6 4B 57 0C B1 E7 E5 0E C5 77 6D 5D D8 E0 64 E3 24 03 B4 D2 6D 47 7F D0 76 BB BD 8D D4 5B A3 B9 89 8A 32 58 FC B9 69 06 33 80 73 C6 32 A0 12 31 C1 CF 27 A9 2B CD B5 AF 14 E9 0D 71 04 DA 84 57 97 3E 7C 0B 34 12 D8 D9 DB CB 19 8D 89 38 DD 36 5C 90 DB 81 C8 4E 9F 74 77 2B 17 56 8A 76 94 AC FD 4D A1 4B 9A 2A 56 97 DC BF CC F4 76 BD 8E 0B 5F B4 5D BC 70 20 DD E6 33 B6 D5 4D A0 96 39 6C 64 0D A4 E7 1D 39 E9 CD 72 5E 2B F0 CC 7E 36 D2 F4 C9 F5 0D 36 57 82 17 6B A3 67 3D D3 40 C0 15 C0 56 09 1B B1 24 72 40 20 83 C0 26 BA 48 A7 86 29 52 CE 38 62 8D 2D 64 F2 D6 38 60 79 15 14 2A 85 19 0A 04 67 F7 8B C7 23 6E 7B 02 56 D2 EF 75 5D 86 19 ED A5 72 72 58 F1 19 5C F1 D4 3E 5B FD D1 B4 FB 73 BA 69 EA 62 F7 D0 F9 8B 4B F1 6E B2 6D F5 9B 4B 3D 2A 5F B2 DB F9 D2 B4 3A 5D D9 86 1B 6E A4 48 36 64 1D 8C 01 04 70 70 3F 1E EB E1 2C AF 6B E1 B8 A4 D6 E0 B6 86 DA 66 2C 2E F5 26 11 99 91 C3 29 45 67 53 BD 30 01 C0 23 1D CE 0E 2A 09 FE 19 47 A4 78 9B 50 93 55 96 4B ED 3F 51 95 D8 1B 8B F1 67 04 85 88 6F DE 04 E5 B6 93 9C 0C 64 8C 81 DA BD 13 C3 DE 1D B2 8E D9 21 1A 8C 33 5B AC 51 E3 4F B2 9D BE CA 88 32 AB 85 DC 49 05 83 E4 9E 18 83 90 48 E3 4B DE 37 5B D9 7F C1 27 ED 79 26 CE 77 53 3A 86 A5 E2 0D 36 DF 49 D4 52 7D 42 2B 69 0A 5D A4 C9 38 46 66 6C 3B 1D A1 78 05 49 01 7A 70 3B 54 69 F0 E6 7B 9D 4B ED FE 3D D7 BF B6 25 91 8A DA 5A 79 8D 0C 05 C2 33 73 80 36 8C 2B 1F 94 01 C1 27 39 C5 5D B3 B8 D4 23 F1 96 A5 F6 6D 13 4F 13 47 1C 70 18 84 CE 8A 8B 95 55 21 E3 8D 8F F7 4F DD 18 03 24 AE D3 5D 24 96 1E 2A B9 8D 63 7D 66 DE D4 60 12 6D 2D 97 7A 93 9E 0B C9 BD 64 C7 7C 47 1E EE 30 57 91 42 93 82 56 FE B5 22 49 36 EE 8F 34 F1 86 90 7C 45 6D 61 63 A7 C7 15 AD 8F F6 D2 5A 41 67 02 AC 69 18 2A C5 9F 62 81 9C 8D CC 4F 6E 7D 78 DD B6 F1 1E 89 69 E2 7D 4A 1D 16 FC B2 4D 1A A4 11 69 BB 64 0E EB B4 93 9D AC 88 BF 7B 2C D8 00 16 20 F1 C6 AD C7 87 3C 1E D7 D2 5B CF 67 71 AB DE 12 24 95 4C EF 38 92 4C 90 C4 C6 1B 64 6C 09 C9 F9 55 40 6C 0E 32 07 45 6B A6 CE F6 4D 6B 24 69 63 60 DB C9 B5 8D FC C7 21 89 2C A5 8F 0A A7 71 F9 57 A7 40 40 18 AB 94 FD CB 7F 5B 2F 9F 40 B3 E6 BF F5 BD CE 4E CB 55 F0 FB DB 78 96 CB 57 D4 6C E3 37 73 BC 6F 04 97 71 C5 23 29 CF 4D EC 30 79 EB 5E 4D A6 69 71 FF 00 C2 53 0C 9E 1A D3 A7 D5 EC C1 F2 E6 8A 5B 55 BB 11 93 C1 C9 28 A9 D7 90 DC 01 F4 EB EA 5E 14 F0 2F 86 AF A0 93 57 BA D1 52 EE E3 ED 5B 51 4B 1D 80 64 0C 94 2C 14 81 92 4E 73 9C 70 09 EB D4 C9 A0 41 63 A9 5D DE 58 DD 7D 92 13 6F 89 AD AD A0 CB AA FA A0 5E 41 3B 4E 30 A4 E4 1C 66 9D D4 65 E7 FF 00 00 E9 A7 52 2A 2D 2E B7 FC FC 8E 42 C0 F8 D7 59 D6 86 9B AD E9 36 7A 6D 8B C6 F3 C1 6D 1C C8 AA 4C 6E 9B 81 DA 59 88 25 80 DD 8E 37 EE 19 E8 7A E5 BD D2 8C D1 BE A7 6B 1B 5C 5B C6 53 CF 46 6B C8 A0 07 70 6C C9 8C C7 F2 9F 99 9C 26 41 C6 58 29 C6 4C 1F 63 9F C5 17 36 FA BC D2 35 BD AE F1 07 9A 9E 4A 82 C7 2C CD B5 57 EF 73 C9 C0 6D DD F2 2B 4F ED BA 4D 8F 99 0D 9D B5 C4 91 5D B1 48 CD B2 34 51 82 4E DC 89 09 03 25 9F 01 94 F7 50 3A 0C E5 35 27 61 B8 BD AD B7 61 6F 2D 25 D6 2D AD B5 3D 2A F6 1B D1 05 D3 CB 11 E1 44 80 1C 15 C8 E1 B0 54 A8 3C 71 8E 4F 53 E5 9E 2C BB 8E C3 C5 37 3A 66 B7 0D 96 85 66 23 13 E7 43 B3 53 3D DA B3 0C 21 94 E3 07 EF 12 70 07 07 83 C5 7A 4C 7A 3E AF AB 5C BD F3 DB 59 68 7A 82 ED 49 6E 63 32 4F 34 B8 19 1F 30 F2 93 00 11 C1 12 2B 1C 02 3E 4C 56 2A E8 FA 6B F8 82 69 65 B4 B0 D4 64 92 DC 18 A5 D6 10 20 2C EC 81 0E 14 98 FE 62 40 01 62 46 39 1C 92 4E 71 D6 EA E2 BF 73 07 41 F1 1E 86 BA 35 9D A6 95 A1 4F 1C 16 F7 0D 32 F9 97 0B 20 75 18 DC F3 31 C2 23 15 46 25 59 94 0C 02 32 08 06 6F 0C CD E2 2B 9F 8A 1E 24 B9 D3 2E 65 B8 B0 B6 9A 56 9A 19 24 26 39 5B 38 58 C7 20 06 3B 70 1B F8 40 E7 23 83 D8 4B AE E9 76 F7 0D 3E B1 25 C0 BF 82 5C 8B 71 10 8B CA 25 7F BC AC 55 B2 AB 93 F3 B6 07 18 18 22 B9 7D 3F E1 F2 8D 49 67 B2 F1 2E AF 6D 65 34 ED 2B 5A 69 EF 9F 25 8A 90 49 9C 36 C0 40 F6 CE 0E 39 CE 69 5E F2 4D 79 FE 3F F0 C6 35 62 EC AD DD 0F F1 E7 C4 59 6C 6C AF 74 CF EC 6D 4E 09 AE 56 58 91 F5 0B 74 54 53 85 43 E5 90 48 75 DA 5C E7 9E 58 73 8E 07 01 36 97 7B 6B 0D 9F 83 6D 4A B5 F5 EB A5 CD F8 8E 26 2E 87 6E 56 36 C1 3B 82 AE 5C 80 3A 9E E4 56 97 89 AF E5 BD D7 5F C4 32 3C 8F A6 69 C0 41 A6 19 5C B9 B8 28 70 1F 27 EF 02 C3 25 BB FB E0 D7 61 F0 97 C3 F3 2C 77 3E 2E D5 95 DE F3 50 72 21 2C 84 B6 D6 6F 99 F8 E9 B8 9E BD 80 CF 43 51 4F DF 7E F6 DD 7D 3A 2F 9E EC C6 72 BC AD FD 79 FF 00 92 1F E2 8D 66 D3 C3 1E 01 B7 F0 DE 8D 77 67 FD A9 84 B4 96 DE 07 59 8A 92 0F 99 9C 8E E7 23 24 03 F3 74 15 C1 5E DA 69 B6 1A FD A5 8B E8 29 7B F6 55 03 54 16 72 4A D9 91 89 C8 42 1B 03 68 20 63 00 6E 04 72 2B D0 3E 22 69 DE 1E 7D 36 49 86 9D 1C FA AC 51 ED CB 3B 07 5D AA 0E 5D 81 CC 87 18 19 62 7A 1C 1C E6 B8 8D 1E D7 4F BF D1 2E 27 D3 B4 E1 67 79 21 16 E2 57 9E 46 0A B8 CB BA F3 C9 E9 80 7A 67 AF 15 AF 3D DB 6B 5F EA DF D7 FC 02 2B 24 9A 8A DB FA FC 7F AE A5 6D 53 50 B6 D7 6C ED 74 DD 3C DD C5 E1 FD 1A 26 91 A4 BD 61 E6 B1 63 F7 49 5C 80 49 C2 A8 03 D4 9E 33 8D BF 04 09 B5 1F 14 C9 E2 BB C8 EE 31 08 02 DA DA 0E 64 98 63 CB C2 93 F7 82 80 33 92 33 C9 CF 04 56 7D 9F 84 1B 50 BA 8A D4 AD C5 B6 83 6A E5 AE 6F 4C 2C 4C CF 9D AC 14 E3 04 F1 B4 76 5E 7B 9E 7A FD 3A EA CF 45 B6 5B DB 9D 36 EA D6 24 50 96 02 74 0E AD B7 82 EE C8 48 1B 78 E5 B0 39 E3 85 E2 B9 B9 75 FE BF AE DE 44 24 E4 FF 00 AF EB 4F CC E7 EE F5 9D 17 C6 FE 26 0D A9 C9 1D 9C 36 A8 6D 6C 6D 26 8E 59 25 B9 73 90 9E 6C 88 37 01 BB 19 1B B3 D7 9E 49 38 D7 97 8D 14 33 78 6A 28 61 D3 2C C3 F9 DA 93 DB DC F9 F1 C9 B4 E4 14 CE 4F 42 A0 29 63 92 17 38 39 27 D7 F4 B8 AE E1 D1 3E D7 A4 7D 9E FA EF 50 27 FD 21 25 06 38 79 C6 E2 47 0C 17 2C 48 07 24 8C 01 D4 8F 2B B7 F0 BC 5A F6 B4 F6 7A 6B BC DA 7D 92 99 EF EF EE 36 C4 F3 3F 05 C0 62 0E DF 4C 12 40 EA 4D 71 CA 12 56 84 7A EF E7 D7 EE FC 36 47 A1 06 DC 79 DE 9F D7 E7 FF 00 05 90 69 9E 07 F1 07 8B AC 97 52 B0 8E 28 6C 54 98 6D D2 59 36 E1 17 D3 03 07 92 72 78 CB 6E 34 57 B3 E9 52 DF E9 9A 7C 7A 6D A6 82 E2 2B 25 58 41 69 D5 77 61 41 DD F7 40 6C E7 24 81 8C E7 B8 20 15 B2 A7 45 2D 63 7F BC 57 9F 73 76 E5 2E 25 86 44 82 61 04 98 05 24 DB BF E6 07 38 2A 7A AF 00 1C 10 48 27 05 4E 0D 32 14 B8 7B 75 69 C4 22 E9 53 0B 20 53 8C 95 19 25 73 C0 DD FC 3B 8F 41 CF A5 0B 8D 4E 61 A3 DE DC 6D 4D F1 47 76 54 0C 81 FB B7 2A BD 0E 7A 0E 70 7E 98 AD 18 F1 2C AE 24 55 73 04 80 23 10 32 0E C1 CF B1 F9 88 E3 B1 AD A5 17 77 16 54 A1 28 EF E9 F9 3F D4 E5 FC 4D A9 39 D1 F5 47 59 7C A4 96 24 89 12 48 DA 37 DB FB CC 91 BD 06 E2 C4 11 B7 3C 22 EF 04 6E C5 52 F0 F1 B1 8B 4C B2 B7 17 77 F3 21 88 4D 32 D9 82 CA 8E 46 36 B1 83 12 06 E9 F7 F2 30 31 D4 71 D3 EA 9E 1D B1 D5 6D E5 86 6F 31 16 69 D6 E2 53 19 19 76 08 10 72 C0 E3 E5 00 7C B8 3C 7B 9C C5 79 E7 69 DA 1D FD AD AD CC A8 D6 7A 78 68 67 21 4B A9 0A C0 1E 46 D3 F7 01 E4 75 27 B7 15 49 DE D1 EA DA 46 53 69 59 FF 00 5F D6 86 0F 84 AC CB 6A DA ED DA 43 71 E4 FD A8 22 87 95 84 87 61 2D 8C 93 92 73 B4 10 4E 30 4E 7D 2B A5 96 CA 2B 9D 36 28 AE E2 4B 90 09 2E AE DE 68 32 1C 82 33 B7 20 06 63 C8 0B B7 1D 00 E0 59 B5 66 94 DD 5B CC 44 A2 17 11 6E 60 32 E0 C6 84 96 C7 19 25 8F 40 07 B5 73 13 EA 0F 37 8F 53 4A 10 5B C6 63 4F 2E 3B D1 10 6B 84 5F 2F 79 01 DB 23 93 C1 E3 A7 BF 34 F9 5B 7E 88 7F 03 77 EA 6D E9 5F D9 91 5D CD 67 63 6B E5 9B 62 50 48 B1 82 98 C2 6E 02 45 CF 39 00 15 62 1F 2B 9C 63 69 2B 25 C5 8D 94 17 16 2B 3B 49 33 3C 8C D1 40 9B A4 5F 31 8B 12 56 21 90 06 EF BD 8C F4 24 92 72 69 4F A7 C4 7C 4D 67 6D 3B C9 72 2E 2C E6 92 66 99 B2 24 31 BC 41 01 41 84 C0 F3 1F 8D BD 4E 7A F3 5A DA 78 8E 36 BB B7 86 18 E1 86 09 82 22 46 BB 40 05 15 8F 1D 07 2C 7A 54 B9 26 C4 E4 93 49 69 FD 5C E7 B4 01 72 9A 0D B5 8E 9E 4F 98 10 5C 9B 81 3C 6A B2 46 E5 B6 B0 C8 76 C3 63 8C A2 E7 69 E8 46 2A F4 5A 1C 71 29 B9 D5 27 B9 B9 B8 66 31 03 01 75 50 AE F8 E1 63 03 01 BE 42 F9 C8 18 C9 38 19 AD C4 81 23 60 CA 39 08 10 93 C9 60 3A 64 9E 4E 32 7B F7 35 50 C9 E4 EB 76 F0 88 E3 2D 35 AC 8D 24 C5 46 F6 F2 D9 02 8C 8E DF BC 73 8F 53 C6 39 AA E7 72 93 D0 B8 2B 68 88 6D 34 5B 28 63 8E 4B 7B 24 B2 94 14 70 C1 51 A4 50 02 E5 49 21 80 C8 05 4E 09 EA 48 39 39 A9 2D A1 92 EB CA BB 92 E2 D6 54 38 96 17 B7 8F 8E 49 E4 31 62 1B 28 55 77 00 0F DE 23 1B B0 B2 6A D3 7D 93 4A BA BC 58 E3 79 6D 61 92 68 F7 8C 80 C1 0F FF 00 5C 71 D8 9A 9E 1B 64 86 38 54 96 96 48 A3 F2 C4 B2 72 E4 71 9C 9F 7C 02 7D 71 50 F5 09 6B B9 92 D7 D6 96 B7 7E 5B D9 6A 0B 72 2E 4A 43 9D C7 ED 24 86 6F 95 F7 6D 64 01 E4 21 58 80 BB 4E 14 6D 15 50 69 F7 57 D7 8D 73 7D 69 6F 61 3C C4 44 31 29 94 C8 BE 58 62 31 B9 54 E0 86 1C A9 CE 3E EE 06 5B A1 9E 66 8E 5B 64 00 62 59 0A 36 7D 36 31 E3 F2 15 93 71 A9 4C B2 5E 3E D5 CD AE A1 6F 68 9F 33 00 52 43 01 62 40 38 2D F3 9C 1E DF 89 CA 93 7C AD F6 1C 75 D0 C0 96 DF 54 D3 AD D1 A6 92 C3 51 41 21 8E 3B 69 B4 E9 60 13 32 97 DE 11 44 8E 14 AA A3 B0 2B 11 DC 3E EE ED D9 A9 EE 63 9F 49 B7 93 6E 89 71 A7 7C A5 DC E8 97 36 EF 0B 20 EB B9 27 08 37 74 C9 11 93 8D A3 77 50 3B 03 11 32 C8 C6 59 0A 3A 05 F2 F8 01 71 9C 90 40 CE 4E 47 7F E1 18 C7 39 A1 13 7F 6A 4D AA DA 5C AA B5 BC 13 7D 9C 20 18 DE A6 28 DF 24 F5 C8 2C 71 8C 7E 7C D2 94 5B 5A 11 AA EA 78 E6 B4 66 F1 5E B5 6B 77 AB C1 77 0E 8F 1B 09 26 99 E0 6B 78 E5 C7 02 34 79 30 8B DF EF 38 EA 70 4B 60 1F 40 B0 BD F0 8F 88 21 92 48 AE 6C 25 B5 B7 DD 04 6A AE D1 C8 50 46 0E 36 E1 0E 00 0E 31 86 C8 19 CF 50 36 2D 3C 3B A5 2D DC C5 2D 11 44 12 ED DA DF 38 75 31 83 82 1F 3D DB 39 18 3C 0E D9 07 97 D7 A2 B2 9B 4C 86 EC 69 7A 7C 57 3F 6D B9 B7 13 45 6E A1 C2 45 2B A2 80 DD 46 40 E7 DC 9F A5 67 04 FD 9B 94 76 BF E3 76 BF 46 66 E3 C9 AC B5 64 73 E9 11 6B FA 56 AB 75 77 73 F6 48 6E 15 A3 B6 79 DD 9B 07 A0 C9 27 24 F4 00 67 9E 78 E2 B9 5D 0F C3 9E 34 B6 8A DE E2 CB 50 D3 96 D2 D6 50 88 D7 04 04 51 92 72 72 BD 32 7B 1C E5 B8 AE B7 5B 80 68 F7 97 5A 7D 94 92 C7 69 24 16 F3 C9 11 72 C1 A4 63 28 2D 93 92 38 8D 06 06 07 1D 2B BB B1 D3 6C EC 61 DB 6F 6F 1A 16 45 57 60 A3 73 81 D3 71 EF D4 FE 66 B5 51 71 D3 AE 86 6A 0A 53 49 ED BB F9 9C AD D5 C4 BA 99 78 EE 2E 25 93 4F B2 CB 5C 4D 85 5F 35 C7 18 5C 60 72 7A 7D 79 AB 1A 56 9F 26 B7 7D FD A1 73 14 B6 F6 F0 98 CD 98 42 A1 70 AD 9D BE B8 F9 40 3C 0C EE E0 FA 59 D4 34 9B 29 B5 CB 1B 4F 25 52 12 B2 48 55 06 3F BB 90 3D 14 ED 19 03 19 23 3D 79 AD 9B 67 12 BD D5 BE D0 91 DB CC A9 18 8F 2B C0 44 6E DE E7 E9 8E 28 E6 B5 BB BF EB FE 18 B8 C2 F2 D7 A7 F4 BF AE AC E5 BC 57 A4 E9 09 3C 3E 55 A4 30 DF CB B5 52 78 B1 03 44 A0 14 CF 98 98 6C 15 3B 70 49 18 03 8E 2A 0D 3A CE 0D 2F 4F 5D 2B 49 F1 14 36 AA E8 59 92 E6 DC 38 88 80 A7 E4 92 36 4E 7E 71 F7 99 F3 83 83 F2 B5 75 F3 58 59 BC 91 F9 96 90 CA 5A 46 25 A5 40 E7 24 64 F2 7F DD 51 F4 00 76 15 93 A0 5C BC DE 05 B1 BB 89 56 D9 FE C5 0C FB 62 E4 64 C6 AE 47 CD B8 90 49 20 92 49 C1 EB 9E 6B 27 1F 8A 4F FA D2 FF 00 F0 4E 8D 5B 57 63 2F 35 0B 7D 26 61 6B FD AB 76 08 1B 88 58 A2 6E 4F 3C 9D BD 4F 5F C7 34 55 6D 0B 47 B4 D5 EC 64 BC BD 12 49 3B CA D9 6D E4 7A 51 59 FB 2A 93 F7 B9 AD 7F 50 E6 4B 4B 1F FF D9";
            } else if (rand == 6) {
                c.setLIEDETECT("블랙윙의");
                packet = "4A 16 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F6 A7 D2 ED 5A 1B 7B 67 69 CD B2 44 91 15 52 04 33 20 56 F9 59 14 6D 03 80 4E 02 83 C2 E4 A9 2B 53 A2 5B 99 96 C6 27 45 16 6B 13 AC 71 CE C1 D4 7C C0 06 50 47 CB 81 C6 49 04 83 C7 CB 50 B3 25 E9 9B 4D BB B4 82 E2 73 0C 7F 68 59 55 44 72 A1 76 53 F2 E5 C8 1F 2B 30 0D C1 CE 33 90 DB 6F C3 91 BA 3D 92 AA C5 84 52 EC 1B CC 18 07 76 72 4F A8 E7 07 20 FB 13 5A B5 76 F5 25 DD B2 BB 8B 6B 38 91 7E CD 1A C1 68 15 A2 0A 81 16 15 C1 5C 82 70 A0 2A EE CE 0E 42 F1 8E 99 8D 19 EF 6D C3 46 F2 95 52 B3 C6 C2 50 AB 2E 46 E5 42 CA 3E E8 24 74 C8 20 0C 96 CB 0A 96 CE 15 48 E6 80 5B BC 0A B7 0C E3 12 12 24 2C DE 61 60 73 9C 12 C7 20 F7 C8 E4 60 98 EC EC A4 B1 B4 8A C4 BB DD C0 32 A6 59 82 6E 20 EE 3B 4A A8 55 0A 3E 55 18 07 8E 31 C6 6B 37 1D 37 34 D6 D6 FE BF AF CC A5 7F 72 34 FD 0E E6 FE EE 52 61 82 07 86 45 B6 64 72 C0 36 D0 DB A5 EA C0 7F 09 27 92 C3 E7 38 AC 8F 05 DF C2 6E B5 0D 34 5E 1F 92 51 35 AA 92 3E 78 25 8D 5E 21 96 1B D9 91 17 1C F4 04 0E 40 18 D3 F1 13 DD 5B F8 73 59 83 EC CD 2D A8 B0 65 8A 55 93 2E 33 1B 86 DF B8 E4 E3 0A 77 64 93 BF 18 E3 27 95 D2 2E 97 48 D5 FC 33 73 6B 72 93 2E A7 A5 88 6E 60 56 50 59 E2 4C A6 0E E0 BB F3 88 C6 EE 9B 48 CE 49 23 45 18 D8 23 1F 70 EE AD 1B 52 92 D2 77 99 6D DE 52 AA 61 05 B0 03 84 1B 95 80 DD B4 07 07 A3 3F 5E BC 54 D3 34 73 5E 5B 5B CA 22 C9 56 9C 46 F2 E1 F2 85 40 21 07 0C 01 7E 4E 7E 53 B3 A9 20 8C ED 53 CD B3 82 1B 3B 3B 78 EF EE D8 86 B4 8E FA 70 16 32 9B 17 3B 88 2E C4 64 B9 3F 33 1C 37 3D 05 5B 6B BB 69 61 86 F6 5C DB EC 2C 41 B9 52 36 2E 32 59 86 7E 42 50 12 0B 60 80 DC 81 CA D4 B4 96 AF A9 1A 4B 4F EB FA FE BB 0C 87 30 5C CF 73 A8 C1 02 CE 77 32 4D 12 31 02 04 C9 5D E4 8C 2B 01 23 0C 03 CE 58 8E 32 05 64 D4 B4 ED 4F 48 9E F9 2F 26 36 4B 0B 4A 6F 16 36 47 11 EF 6D EA A7 60 20 0F 2F 1F 2F CD 80 0F 07 6B 1D 47 9A 58 9E 36 9F C8 8A 12 C5 4B 99 BF 88 B0 58 D7 05 79 DD 9F 51 83 80 37 67 35 CB 0B 79 34 8F 15 DE 59 4F 6F 27 F6 0E AE DE 70 98 0D A2 1B A2 54 1F DE 06 CA 96 38 C1 E0 EE 2A 17 A1 34 D6 AF FA FE BE 43 D5 33 A4 D3 EF 2C 75 8D 36 2B BB 19 0B DA C8 C5 D1 D0 34 79 2A FC FA 1F BC 0E 73 D7 9C E4 1A AB 78 F7 77 77 37 31 59 45 1A CF 17 EE 04 B2 AE C7 8D 59 37 97 46 E7 72 B1 11 A0 F9 70 19 58 9D DB 76 D5 EB 18 99 21 0E C1 63 32 0D ED 0C 6B 84 56 24 B3 11 90 09 24 B7 24 E3 24 67 00 93 59 BE 2E BA BB B1 F0 BE A3 75 61 7A B6 D7 91 40 D2 40 CE AA C0 B2 02 E4 00 47 39 55 23 D8 73 DA A5 B4 90 F9 5B 76 46 C8 12 F9 EE 4B A1 84 AA 85 50 87 70 6C 9C 92 73 C8 23 6E 06 06 30 79 39 E3 3B 4E 3B 6F A6 FD F7 99 15 C2 79 B6 FB 65 92 65 F2 F2 4E E2 C7 E5 5C 97 E1 7A E1 78 24 28 09 E5 6B F1 6F 52 BC F0 4C F3 47 A4 DE DB 6A 31 A9 98 DE C7 6A D3 5A 0F DE 64 64 B1 25 43 61 93 9C 80 47 1E DD 9F 82 AE 9B 5E F0 A5 BE A3 34 5F 63 D4 2E 7C B9 30 97 D2 3B C8 B1 B9 64 C9 7C B2 A3 32 C9 F2 FC DF 29 6E 79 38 A7 16 9B 6F A7 EA 66 AA 47 BE E7 47 61 B2 18 63 65 8A 58 52 4C 28 F3 88 F3 67 61 F2 87 7E E4 EC 45 39 27 38 27 70 05 70 23 68 DF 4E D2 E4 92 EB 50 21 E3 5C 0B 9B A7 1B 10 28 18 67 2A 10 10 76 EF 39 C7 2C CA 08 04 53 2F 6F 34 AB 1D D6 3A A6 A9 A7 C3 0B E4 8B 69 DD 14 C9 13 29 52 24 DE 49 70 5B 71 C8 C6 78 07 38 39 E0 7E 29 6B D6 0F A4 C4 F2 78 6E E7 50 8D 5F 89 6F 96 E2 DA 28 5C 7D DC 29 0A 5F 3B 98 10 08 3F 2F 39 00 54 C9 A7 A5 FB 15 78 ED FD 7E A4 7E 36 F8 8B A6 CB 6B 71 2F 85 F5 9B 25 D4 ED 19 1B 7B D9 6F 79 00 2C A4 47 23 02 A7 01 DB B7 46 6C 1E 4D 72 9A 53 BE A3 F1 4F C3 77 8F 0D AC 17 37 B0 AD D4 CD 6C 00 05 D8 3B 6E DB 8C 2B 01 8F 5C E0 12 72 4D 62 DB 7D AA 2D 3B 5C D3 A3 8E 2D 42 F7 52 31 23 45 A6 05 29 16 19 4A B9 F2 D3 61 04 B0 50 A1 86 09 E4 67 18 EB BC 37 E1 2B 6F 0D F8 A5 EE AF B5 8B 1B 2B 9B 1B 35 D9 0D 85 C4 72 5C 3C A5 7E 6C A4 BB B0 D8 27 3C 63 E6 04 10 33 5A C6 D1 D7 B7 F9 3D BC 88 93 94 A3 65 FD 6A AD FA FE A7 B4 24 51 C6 3C 98 D6 E5 87 98 A1 99 A5 63 B7 6A 82 0E 58 E4 8F 94 03 8C E4 93 9E AC 6B 37 50 F1 16 87 14 72 C1 A8 5E 5A C7 6E D3 9B 49 16 E0 06 59 89 50 19 40 CF 40 5C 06 24 60 60 83 8E B5 C6 47 2D ED F6 B8 F6 93 5F 5F DE AB 3B 24 D8 79 23 1B 55 80 6C A8 01 06 D0 8C 32 17 0D 92 33 96 0C 39 FF 00 0C 6B FE 24 BA D2 EF AF 74 8D 37 40 1A 5C 52 B3 4D 73 A8 67 7A 18 80 74 2F 86 07 AF 2B 80 70 73 D3 AD 60 E4 E3 AF 5D FF 00 AB 76 35 50 9C F6 FC 4F 4F 83 C4 76 B2 BB C5 63 15 FC CB 0C FB 09 16 92 CA 2E 37 26 E1 E5 C9 F7 40 25 97 0C C7 6E 07 65 2A D4 B6 33 6B 17 91 43 15 DE 85 1D AF 90 E0 EF 7B A0 99 C7 DD 64 58 F7 E3 3C 82 A5 B0 01 C6 5C 13 5C 4D FF 00 C4 8D 4E 5F 85 91 EB E9 6B 6F 6F 77 34 AD 6E C1 8B 6C 7E 76 9F 2F 0E 1C 1E 49 1D 71 B1 B9 E8 6B 42 F7 C7 B7 56 BE 06 D3 BC 4F 1E 9D 6E 97 BA 80 16 FE 4B 8C E5 C1 3B 5B 78 20 EC 18 90 ED 3C FC E3 91 83 91 CB 47 77 F7 79 ED F9 83 A5 2D 1B EB FA 7F C3 1D 45 8C 5A 85 95 E9 17 22 29 7C E6 06 24 55 72 B0 EF 67 79 47 98 4B 33 7F 0F 50 89 C2 01 8C E0 68 23 A1 86 59 AE 24 10 AA 85 92 64 33 1F DC B8 01 88 2D 9C 01 8D BC 0C 0E A4 E7 75 70 FA 47 89 F5 3B 0F 11 D8 E9 DE 20 B4 B6 FF 00 89 9B F9 96 D7 96 33 CA D1 97 2B 9D 87 79 3B 97 E7 18 DA 76 02 78 E4 1C 58 B3 F1 76 A4 FF 00 13 AE 7C 33 3B 47 25 BA C9 E6 C7 24 11 8F 95 3C AC F9 6F 9E D9 21 B7 0E 72 31 D0 E1 5C 6C 9D BD 7F 0D C2 D2 57 BF 4F E9 1D CC 13 2C F1 97 51 80 1D 93 A8 3C AB 15 3D 09 F4 FA FA E0 F1 45 32 CE 39 E2 B7 58 EE 0C 2C E9 C0 68 50 A2 91 DB E5 24 ED F4 C6 4F 4F 7C 02 A8 45 79 35 58 A1 BC 68 A5 DA 91 00 9F BC 2D 82 A4 99 06 5D 48 1B 57 F7 78 0C 4E 18 9C 0F 7B 37 10 EE 8E 47 8E 35 33 60 15 F9 CC 7B 8A 9C A8 2C 39 DB 9F 63 C1 3C 1C E2 AA DB 99 60 BB 75 B8 B9 37 12 BC 51 97 11 AE 16 23 F7 78 4D C4 85 63 B8 8C E7 18 6C B1 00 01 C9 EA B3 C7 E1 EF 17 C5 E2 26 B6 9D B4 BB C1 1C 37 F2 18 36 88 26 0B B6 39 8E FF 00 98 0D 92 14 3B 70 07 20 92 DF 2D 38 AB BB 5C 34 DD 92 78 D7 7D FE 91 06 80 62 68 EF B5 CB A1 F2 89 5A 5F 26 28 CA B3 48 01 E3 21 11 72 8A 47 CC C7 05 8E 49 D2 D2 3F B4 74 BB 48 74 ED 6E E9 F5 0B E8 26 FF 00 44 96 1D D1 9B 88 C8 2A BB CB 15 47 70 BB D8 A6 49 C2 86 E4 80 4E 66 85 79 6D 37 88 75 8F 12 DC 3C 87 4F 8D 17 4F B1 B8 65 77 02 18 D8 09 5C B8 05 4A 19 18 1D EC 49 C2 B1 24 05 6C 75 F2 39 F2 52 E6 67 16 B1 22 79 92 09 02 EE 8F 18 3C B6 4A 80 06 E0 7A F5 E0 8C 73 53 6D 25 11 D9 DF 43 13 C4 42 3B CF 09 EA AC D2 CB 35 BB 43 2C B1 31 58 DE 39 57 C9 2E A4 15 07 08 0E 08 27 0D B9 47 24 11 9C 4D 62 CE 41 F0 BB 4D BF B5 69 FE D7 65 15 A5 EC 5B 49 70 AE A8 8B 9D A7 20 28 5C B1 03 03 23 27 BE 77 3C 5D A9 5B C5 A4 DC D8 BC D2 47 35 CD 85 D4 B1 28 51 89 02 47 82 A7 23 39 F9 C3 60 73 F2 93 D0 1A C7 D0 F5 09 F5 6F 08 69 FE 4E 8D 7F 77 12 59 FD 86 68 56 74 8A 19 17 72 A3 9C B1 52 58 22 96 18 F9 7E 66 5D D9 C9 09 4B 95 5D 8D 3E 55 73 B4 59 C3 DB 47 32 DC C0 63 91 94 AC 80 65 5D 58 FC A0 1C F5 20 80 0F 73 DB 9C 54 3A 6C 57 50 FD A1 2E 9D A4 22 40 16 66 3C CA 36 AF CD 80 70 BE 84 00 A0 B2 B3 00 37 62 B9 0F 09 4B AB 5F E8 76 D6 F7 1F D9 71 45 63 E6 69 F7 0D 75 13 49 29 3B 94 08 B6 FC 8A A3 6E D0 7E 66 C9 0B 91 9C D6 C5 CD BE BC 2E 55 64 D6 67 39 C0 22 CA C9 23 45 DD BB 69 63 20 90 9E 40 04 A9 3B 46 D2 57 04 B0 96 EC 99 0D B4 ED 63 37 59 F1 32 69 BE 25 7B 0D 57 51 B8 B1 D3 AE B4 E1 30 01 01 92 19 09 64 DA 19 01 23 8C B6 72 70 CA 30 71 C1 D8 8B C4 7A 2C 37 93 AA EA D6 13 79 EE 64 51 6F 24 65 F2 15 14 26 D5 62 F2 3B 60 E3 0A 78 1B 7B 0C F3 5E 31 D2 B4 FB 7D 27 4D D5 63 B6 BF BD 9A 09 92 E3 C8 B8 92 79 77 42 06 E9 03 A4 AC 4C 6B 80 32 4A F0 70 08 E6 AD C9 E0 BD 07 57 8E 2B ED 1A DC DB C5 39 2E 6E AD AE DA 22 CA D8 0C 15 76 B0 0B B5 A4 C8 C2 9C A8 5E 01 24 4A B9 3C D2 BF 2E C3 BC 49 AD EA 3A B6 85 05 EF 84 84 B3 18 99 2E 8B 5C DA AA 5B 4D 18 F9 86 E6 9B 61 C7 1B 83 47 9C 6D E4 8C 83 5C 96 AB E2 0F 1D 5E 41 77 A0 EA DE 13 B4 17 FA 8D AB 46 A6 DE F5 22 F9 00 63 B8 7C C4 90 30 F9 CB 63 95 18 1B B0 FD E5 BD BE AB A3 69 F7 C9 7F E2 19 EE 96 DE D8 4A B3 9B 54 8F CA 50 18 64 BB E5 5F A6 4E 5B 23 68 CF DE E7 CF BE 25 6B D6 3A 94 F6 36 D3 68 D6 BA A4 52 03 14 22 69 5E DA E6 DA E1 89 E0 B7 0A 50 0E 31 C8 CA E5 8F 4C BD 5D A3 DC 7A A5 CC DF E8 71 92 E9 F7 30 78 42 E2 D6 43 E3 18 ED 93 69 F2 E3 41 35 8A 8C EE 7D C4 30 5E A7 3E C4 73 D7 8E EF 4A D6 FC 39 75 A3 D9 B6 87 25 DD EC 10 6C B6 9B 4A D4 E5 91 C0 1C ED 6D 9B 8C 67 3B 73 C0 60 0E 08 00 8C 57 27 A4 F8 63 C4 3A 47 8D F4 B9 23 D0 6E B4 18 6D BE 7B 9B A4 9D 9E 12 83 2E DB E6 F9 91 41 03 69 ED EA 0D 7A 96 B7 63 2B EB 76 72 CD A6 79 31 42 5A E2 EE E8 48 59 0A E7 76 4C A4 03 85 00 F0 71 81 C0 18 C5 6F 2E 57 AB D9 FE 87 3A 8B B3 F2 3A 1D 1A 2B 5D 3D AE E0 B5 86 38 ED D9 C4 DF B8 85 23 86 1C 82 8C 9B 94 00 C5 5A 26 CE 79 1B 94 1E 3A 65 78 9F C2 96 7E 2B B5 B2 B7 D5 C4 B6 70 C3 32 4F 88 A4 55 8C BB 90 1A 26 E7 2C C4 96 01 80 1F 78 75 39 07 AA C4 AE 37 87 31 B1 53 84 65 04 02 71 F7 B0 79 20 E7 A1 03 9F C6 B1 2E 2E B5 C6 96 7B 2D 37 45 96 34 3E 6E 2F 2F 2F 91 00 25 B8 31 E3 CE 63 D4 90 19 00 00 01 8E D5 86 EE E8 E8 5B 1C 25 C7 81 75 0D 16 EA 7B 76 F1 20 D3 7C 36 EE 25 8A D7 4F 3B 26 25 32 FC 17 62 43 6E 38 DC 19 C9 F9 01 01 55 42 F2 76 9A 04 16 BE 3E D6 2E A0 5F B1 68 B6 01 1D AE 5A 76 1E 5A 3E DD AD BA 41 BB 7B 0C 90 30 79 38 19 E3 3E 9F AD E9 DE 27 D5 A3 B9 D3 A5 BD 8A 58 80 56 CD 84 5F 67 56 1D 4A C8 64 2E 77 64 0D A1 19 70 09 2C 46 57 39 DA 4E 81 E1 ED 2C AD F5 C4 56 57 57 51 B3 4A E5 58 CD E5 80 42 BB B3 FC F2 1F 97 2C 01 C2 67 20 ED DD 9A D6 32 EA C2 5F D7 F5 B1 8F 75 7D AA AF 88 B5 6D 57 C3 3A 8E 91 3E 91 65 66 76 0B 84 64 8A 28 D1 00 2B 1F 96 00 70 3A 0E 48 E3 1D 45 72 5E 0E F0 E4 9E 23 BC FB 6D DE 85 A5 1B 47 F3 1D 7E D5 3D C5 A8 9C 0C 06 F2 DD 32 BF 29 19 3C 7F 13 75 C7 CB DE F8 FA 76 D2 BC 3B 26 AB 7D A1 19 E6 98 C2 B2 2D C5 CE E8 96 55 C6 37 2C 78 DE 87 93 F3 60 12 BC A8 E0 1E 1F 54 D5 BC 4B 6F A4 E9 B7 29 E2 84 BB 9B 50 8D 2D D3 4B 82 14 54 54 DC 71 88 C6 17 AA A8 0C AB 9C EE 5C 8C 60 E0 EC 9D B7 B6 9F 36 75 C5 49 AD 3A F5 F2 5F D6 EC 65 D8 B9 F1 6F 88 D7 4C B6 B2 61 A2 E9 0E 5E 4B 68 AE 4B C7 D4 29 08 4A A9 F9 C8 01 50 2E 72 4E D1 DA BB 2F 10 F8 AB 4A 9E 2B 5F 0E 78 A3 4A 9A CA 28 EE 36 CA D0 A1 31 24 61 18 23 47 B5 B2 18 33 2F 18 61 80 78 39 15 D0 68 1A 31 F0 A7 86 CE 99 16 92 65 9E E2 53 E6 CB E6 A6 DB 83 8C B0 72 FC 81 B5 58 7D DC 63 90 46 73 57 74 D8 E0 D2 E2 BF D5 EE 6E A2 9A E1 99 A0 90 D9 90 EB 6E C3 F8 3D 88 20 0C 11 C7 19 A8 6B 4B 5E FD FD 42 52 8C F5 ED B7 F5 F8 9E 75 A1 EA 3E 1A 1E 23 B2 BF FE D9 B9 92 5B 44 60 92 6A D7 2D 2A 44 A0 71 B4 6C 43 91 CE 17 27 93 D2 AD FC 31 B9 8F 54 F1 D6 AB AD DE 2E E9 AE 19 D6 07 65 E1 59 8E 48 1E 9F 28 03 E8 71 56 F5 B8 1E EB C2 9A B6 AF 78 6C A7 94 C4 F9 59 B6 34 91 92 54 2B 28 20 80 72 48 EC 71 D3 AE 45 FF 00 04 78 53 51 D2 B4 8B 26 31 20 B9 21 67 31 B8 75 0A 24 27 3B 9B 8C 36 C5 C6 17 24 12 A4 8C 75 8A 69 DF D3 F5 FF 00 86 22 A2 B2 B2 FE AD FF 00 0E 6F 6A B3 AE 95 A8 38 B3 BE 7B 51 70 04 EE 9E 50 39 6C 05 CF 3C 8F 95 54 63 B6 31 ED 45 60 6B 77 5F 6A D6 6E 9E 4B 88 78 90 A2 65 82 FC A0 E0 70 4F EB FF 00 EA A2 B2 6E 5D 27 6F 9A FF 00 32 97 22 56 71 3B E5 88 9B 78 6F 2F 9F C8 92 15 69 18 6E 1B 22 C9 C9 CE 49 19 0A 0A EE F4 2D 8C 03 81 9D E2 AD 3A F6 F7 C3 17 7A 65 8D BA DD CD 78 DE 50 FB 4B 02 91 07 6C 99 1B 24 10 13 9D BB 72 41 0B C1 C7 1B 93 44 F2 C5 2A 25 C4 90 B3 A1 55 74 0A 4C 67 9F 98 64 11 9E 7B 82 38 1C 75 CE 66 A1 05 AC 33 69 16 A6 D2 19 63 95 DE C8 F9 AB BC 88 4C 2E CC B9 3D 41 31 26 41 C8 38 E7 90 08 EE 5A 34 FB 1C ED DD 6A 26 81 05 BD 8E 89 6F 67 A4 C4 5A CE 02 23 85 E5 F9 3C C5 04 6E 7C E3 E6 C9 2E C0 E0 06 ED F2 90 C7 56 09 0C B1 06 6D 9B B9 0C 23 7D C0 10 70 46 70 3A 1F 6A 86 1D 36 D2 09 96 68 A1 0B 2A AE C0 F9 39 23 24 9C 9E F9 24 93 9E A7 93 92 05 67 F8 69 7E D7 E1 2D 2A 59 0B 0F 3E 08 AE 76 C6 C5 02 16 C4 81 17 18 C2 2E 76 85 FE E8 00 E7 9C B7 AE A4 A5 6D 10 FD 47 48 7B E8 19 64 31 5C 48 64 28 AD 28 64 22 DE 42 04 B1 92 84 6E F9 37 63 B6 44 64 E4 AE EA C4 F0 B4 B7 3A 17 81 34 DF B5 5B 4D 11 8C 4E 65 8E 48 B6 94 01 9E 4C B1 76 50 80 AA 91 96 E3 2C BC 81 5D 73 46 E6 14 41 3C 8A CA 54 97 01 72 D8 20 90 78 C7 38 C1 C0 EE 71 8E 0D 66 69 02 3D 5F C2 9A 7C 97 31 28 17 76 B1 CC E8 8C C0 23 30 0F F2 1C E5 70 4F CB 83 95 C0 C6 30 2A E3 F0 A6 F6 B9 7C CD E8 F6 2A 69 9A 6D D5 B6 A3 AD DF 43 34 50 DB DF BC 4F 04 48 37 F9 52 85 DB 21 75 5F 97 25 BE F6 0F 38 3C 8E B5 A3 6E 0C C2 48 DE 79 33 23 0B 88 A3 25 C3 44 B9 CA EE 3F 2B 60 91 9D AD FE D2 F2 A3 02 5D 4A 41 69 61 73 7C 91 C6 D3 DB DB C8 C8 CC B9 C7 19 C7 AE 09 51 9F A0 A9 4D AA 99 59 FC D9 80 66 DE C8 24 38 2C 36 E0 FA 81 F2 F4 04 03 96 C8 39 AC 74 6E CD EB FD 22 6F F3 65 2B DB 6D 3A F2 C6 4B B7 5F B5 41 34 2D B7 C9 70 3C E4 91 02 ED 52 08 DD B8 01 8C 9E B8 F4 18 B5 69 6D 0E 9B 04 36 90 99 7C 91 88 E1 42 0B 08 D4 2F 0B 9C 67 1F 29 E5 8F 53 8C F4 14 B0 28 37 77 2E 06 D2 1C 2B 05 E8 DF 2A 9C 9F 7E 71 9F 4C 7A 0A B0 01 03 92 4F 3D E9 DA CC 50 92 92 BA 5D FF 00 AF BE E5 64 48 03 4E 96 A9 6C 73 27 FA 42 28 00 97 21 49 2D 8E FB 48 38 23 9C 8E 40 AE 63 C4 7A 5D 9F 8C 6C A0 B0 13 69 D7 F0 48 05 C0 8A E2 46 49 50 11 95 64 29 C8 F9 5B B8 3D B3 9E 6B A6 90 FD AA E2 EA C6 4D C2 23 02 9D D1 BB 23 FC E5 C1 C3 29 04 7D D1 82 30 47 AD 72 9A C3 9D 4B C4 FA 34 33 E4 20 96 51 FB B6 2A 7E 59 18 0E 47 23 85 1D 2A D4 79 9D FF 00 AF EB 41 4D A8 FA BF F8 1F E6 60 78 6F E1 CD 8F 87 F5 75 BE B3 B7 D5 2E 92 36 65 2D 06 AB 19 4D C0 32 37 45 8D B7 29 CA E3 3C 65 BD 30 7A 1D 2E EE 7D 7E C9 61 B2 D5 B5 04 91 1A 3D CE 22 5D 8B B4 6E DC 49 25 B6 96 18 0B BD 8E 0F 3B 86 71 D1 47 A4 7D 9D 4C 76 97 D7 76 D1 16 2D E5 A9 47 1B 98 92 C7 2E AC 72 49 24 F3 D4 D6 3F 82 82 BE 9F 77 19 5C 18 AE 98 6F 0C 43 3F 43 96 E7 93 DB E9 F5 34 DC 9B F9 12 A3 CA D7 9F F9 1A 5A 54 9A B4 71 5F 7F 68 1F B4 B4 2D B6 20 91 08 DE 4C 2E 78 CE 14 83 C1 07 38 C9 20 90 41 02 D4 2E 5F 1E 4C CB 71 6F 24 FB 83 44 D9 D8 85 37 FC CC 5F 9C B6 08 C0 C6 D6 51 B7 1F 35 16 FA 54 16 F7 97 77 01 E4 7F B4 34 67 CB 7C 15 88 20 01 55 38 C8 5C 8C E3 24 64 92 31 93 98 2C AF A7 BA B3 D1 AF 1D 82 9B B8 94 CB 1A 8F 97 2D 1E FC 8C F2 30 57 03 9E 84 E7 3C 11 9C E6 91 AB F7 63 CC F6 BF E6 5F 25 65 67 DB 10 67 43 E5 93 22 95 18 38 27 04 8E 46 31 D3 8C 8C 67 83 88 E1 B2 B4 8C AC 96 D1 AC 40 F3 FB 93 B1 5B 24 1C 90 BC 13 F2 8E 4F 6C 8E 84 E7 23 C4 1A C5 DE 8D 20 30 94 94 35 B4 92 ED 95 72 01 59 62 51 8C 63 8C 4A 7F EF 95 F7 CB B4 61 3E B5 A5 5B DF 5E 5E DC FE F1 9D 8C 30 B8 89 06 19 80 00 A8 0D 8C 7A B5 3B 75 44 29 5F A1 17 88 6C AC 67 B2 BF 82 E9 20 8D 6E 23 75 58 EE 25 55 5B 87 21 7E 62 0B 61 B1 84 00 90 19 76 71 C1 AE 56 D3 C2 FA 06 89 62 62 D3 34 03 36 AC D0 A8 92 79 A3 66 10 93 91 BB 12 9C A0 E1 B0 D8 00 E0 F3 C1 C7 6D 3D AC 5A 45 91 78 17 74 93 5C 47 13 C8 DC 33 2C 93 2A 9C 95 C1 25 43 10 18 E4 F1 92 49 24 9C 7F 10 C6 5F 5C D3 F4 B3 23 0B 2B 99 7C C7 89 00 51 92 79 E8 39 C9 CB 73 93 96 3C F4 C4 B4 97 CF FA FF 00 33 78 73 37 AB D3 7F C8 A3 69 0F 87 F4 9B 78 E7 9E E9 6E E4 12 04 92 34 58 A4 57 20 3E 48 0C 0B 05 CB 0E 72 33 E5 A9 1C 13 BA D6 AD A7 5B EA 7A EC 1A 66 9F 65 04 51 40 CA F7 52 C7 18 4C 03 FC 39 03 AE 33 81 DC FD 2B A0 D5 74 EB 43 A2 5C 45 E4 05 8A 28 58 A4 68 4A A8 23 E6 E8 31 DC 7F 3F 53 9C FF 00 07 C6 D2 E8 51 4B 24 F3 30 E1 15 0B FC AA 15 D8 82 3B F3 9C 1F 50 00 A5 66 DD 9F 42 9C 9A 8F 32 D5 96 75 9D 2E CA 3D 26 69 20 F0 FD A6 A1 2C 60 32 5B 14 45 DF CF A9 07 90 32 7F 0F 5A AB A9 69 1A 56 99 6F 2C DE 44 71 C2 D0 C8 8D BA 77 0C EE 40 DA 80 67 04 11 BB 23 D8 7B D7 42 20 89 66 33 6D CC 87 3F 33 12 4A 83 8C 81 9E 80 ED 5E 07 19 19 EB 59 1E 2C 24 68 4E 01 20 17 50 71 DC 66 9D 4B 28 E8 B5 33 5B EE 63 69 9A 76 B8 9A 7C 4F 65 E4 18 A6 1E 61 DF 70 22 20 9F 50 61 93 3C 01 CE 47 5C 63 8C 92 BA CB 16 CD A8 50 A1 55 1D E3 50 3B 05 62 A3 F4 14 55 C2 A5 E2 B9 76 21 72 49 73 5B 7F 5F F3 3F FF D9";
            } else if (rand == 7) {
                c.setLIEDETECT("둠의궤듀나미");
                packet = "1C 17 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F7 4B 3B 98 AE E0 06 2B 95 72 0E 7E 41 86 51 B8 80 19 5B 24 1F 94 A9 CE 0E 43 74 3C 0A F6 D2 C7 E7 35 B5 81 54 36 61 21 7B 1C A2 A4 48 5B 86 F9 54 90 76 0C AA E4 70 46 42 E7 22 7B 83 F2 C0 6E 1D A0 90 4E 44 46 37 62 84 FC C1 77 E0 01 82 3B 37 1B 88 00 EE DA 6B 9F F1 47 88 74 AD 0B 4A BD D7 6F 2D 6E 6E 6D E3 CD 94 93 69 92 E5 C2 93 86 0C 43 2E D2 AE 08 CE 72 A4 F0 41 66 00 E6 4A C9 0E CD BB 23 A4 9E D9 E7 B5 F2 7E D0 E3 80 19 8A 23 6F 00 8C 86 04 63 04 02 0E 31 C3 1C 60 E0 86 DC 5C 08 1A 49 1E 37 55 8A 30 44 AC DF BB 25 8E 08 38 C9 18 C0 25 8A E0 06 CE 7E F6 38 4B 6F 8A 9E 0A 79 5E 5B 7F 13 24 6A C2 33 E5 DC C1 72 C5 4F 01 FE F1 C1 CA AA 81 81 F2 B6 E6 3B B7 11 5A 76 37 BA 7E AF 3D B6 AF A4 E9 97 5A B4 1B 48 6D 42 27 81 45 C3 8D 83 2C 8C CB F3 66 35 25 B6 A9 06 24 C6 56 94 BD DF 51 38 F5 68 EA 5E 38 7E D2 04 B2 33 4A E4 B4 43 80 C8 A3 69 20 15 00 ED CA 82 73 9C 93 83 C6 05 53 17 0A BA 64 6D 69 77 84 B8 58 D2 D4 DC B9 46 F9 94 74 67 52 59 B6 FC C0 30 24 90 41 F6 E6 FC 5D E3 F8 BC 1F 64 B7 D7 1E 1B BD 37 B7 4C B6 F0 23 3C 20 CC 41 24 29 64 77 6C 0D CC 47 07 93 EF 5C D4 DF 13 7C 63 E1 87 B4 5F 15 78 4A 38 AD 6F 65 58 6D 67 FB 6A 6F 1C 73 E6 EC 0C 0B 72 09 C2 A7 7C 2F 60 26 9E DE 9F F0 3D 47 24 D6 AF FA FF 00 80 7A 5C BA 7D AA 06 57 B7 B7 8D 02 A2 C1 34 70 20 30 04 C0 8D 46 EC E5 83 33 95 C2 E0 67 18 FE F4 91 C6 65 8A 19 16 69 9E 39 CA 3F EE 66 0C 88 72 64 2C 1C E1 99 58 E1 71 C8 C6 00 00 66 BC FB E2 1F C4 8D 7B C0 D7 5A 4E ED 33 4B 96 3B A6 7F 36 04 BA 79 24 65 1B 7E E9 DA BB 7A 91 9D AD FE 3D 44 47 59 BE D2 51 EE AC 2C 6D 37 AB CE B7 12 5D C8 65 B3 F3 03 7C C0 3C 7C 3A AB 91 8D C0 75 1F 28 38 04 64 B9 6F D0 76 D6 C6 F5 C1 82 E1 24 57 8A 66 30 C8 14 15 46 56 0C 54 72 8D C7 67 C6 E0 70 3E 60 48 C1 C7 86 7C 43 F1 5E AD A9 EB 97 3A 4B C3 AE 68 17 90 DB B9 86 18 EF C3 45 74 80 16 F9 D1 30 32 57 3C 87 61 91 B7 07 A8 BB E3 FF 00 08 78 87 C4 0B A9 6B 1A AE AF 66 74 DD 3E DD A4 B1 86 C6 27 2B 31 3D 0E 09 20 33 1E A7 73 1C E3 8C 10 6B CF AF 0B 45 A1 68 9A F2 EA 51 EA 17 D6 53 A2 DC 21 B7 63 E5 67 E6 89 1E 53 C4 9F 2A 11 8E AA 3E 5E 83 8E 8A 50 4F 5B FF 00 C0 BE DF F0 4A 82 4B 55 D7 F4 3D B3 E1 6F 8A 24 D5 7E 1F 45 75 7F 77 3C F7 16 37 0D 0C EC CC 64 92 4C 9C AE 73 96 6F BE 30 07 24 A8 03 3D 0F 79 15 B4 71 F9 90 EC 76 88 C9 E7 0F 30 86 01 8B 16 20 67 9E 1B E6 E7 A6 46 38 18 1F 35 F8 2E 57 D1 BC 5F A9 69 91 4D 71 2D A4 D0 34 8B 1C 12 2A 24 F1 EC DC 0B AB AB AB 0F 2D 9B E5 20 F5 EB 9A F4 67 BA D4 34 C1 67 A9 AE A5 7F 2D 85 E6 59 80 7D EE A7 EE 95 3B 81 47 60 07 04 8E 70 32 06 39 2A 52 E6 93 69 5B B7 DD FF 00 01 91 CA 95 D1 EA 0F 34 CA D1 84 81 64 CC BB 24 DB 20 FD DA E0 E1 B9 EB FC 39 1E E7 AE 39 6C 92 24 ED F6 6F 34 C3 29 2C 42 E7 0E 42 E3 E6 5F 50 0B 27 A8 E7 04 73 8A C0 D3 2D AD 75 3B BB B4 92 E7 5A 66 52 AF BE 73 F6 23 29 2A 01 60 B1 88 DD 80 0A AB B9 D7 1C 61 4F 06 B9 DF 01 58 49 E2 4F 0B 4D A8 DD EA 9A AC E6 6B 99 DF 4C 9E 6B B9 44 90 C3 96 8D 37 85 60 86 40 55 C9 C6 78 61 CE 0E 2B 99 36 D6 DF 88 AD D4 EE DA FA 3B 38 2E E7 BE 88 5B 08 23 37 13 C8 81 99 36 0D DC EE DA 37 30 54 04 8C 64 64 0E 78 26 D9 8A 37 2C AE BB F2 CA E4 3E 48 04 63 04 67 81 82 A0 F1 DF 9A E7 24 D1 74 41 AD 9F B5 CD 34 A2 18 11 4C 17 B7 92 4B 11 F3 A4 DA 99 59 32 09 2D 1E 00 CF 50 3E 5C ED 34 DB BD 13 C3 D6 FA 68 1F F0 8F E9 B2 DC 46 55 5A 38 A2 05 63 70 A1 FF 00 79 20 5C AA 80 32 4B 0E 41 C6 18 B0 52 28 CF B2 FE BE 41 A4 55 DE 9F D6 E6 F5 8D DC B7 36 70 4B 73 65 35 9C F2 12 AD 04 9B 5C A1 19 EA C8 48 C1 C6 41 CF 71 D0 9C 55 18 BC 49 A3 C6 99 B8 D7 2C 06 E0 AE 8B 2C AB 14 8A A5 41 F9 D5 88 39 3C 9E 8B C1 03 1C 64 F9 BC BE 25 8E 0F 10 EB 5A 64 5E 05 D2 B5 3B 7B 19 4B 01 6B 02 2B 22 A9 2B B9 B0 1C 31 F9 BB 60 8E 72 07 20 74 6D F1 1B 4E B5 8E D2 C7 51 F0 CE B3 63 15 C1 FB 3A C7 3D 90 58 F6 F4 C0 04 FC C3 04 70 07 43 F9 CA 93 76 57 40 E5 15 7B 97 A0 F1 8E 95 72 25 92 DB 50 86 44 C8 29 2D DD EA 40 5C F9 99 0A 23 C8 2A AA 0B 02 CC A1 88 55 E2 4C D5 AB 9D 7F 48 F3 A2 11 AF F6 CC AF 32 B2 3D AD B1 9F CA 50 DC 92 C8 A4 65 37 F4 1F 30 0C BC 73 B8 ED F9 31 BE C9 9E E2 E3 74 6E E1 19 9C A0 CB 1C 60 A8 C2 B6 38 0B B8 1E C7 92 72 70 BC 53 A5 F8 87 53 F0 E4 B6 FA 5E A4 D6 77 B1 1C C6 D1 B6 1A 70 03 0C 17 1B 76 96 05 4F 00 6D 60 7A 8C 1A 6D 3B DD B5 71 C9 C5 EC AE 8D 0D 37 51 17 A5 E0 57 D4 C4 88 0B 03 71 60 D0 EE 50 36 FD E6 40 A4 93 F3 E0 60 F2 38 00 11 5C 27 C6 88 83 78 5A D4 32 BC 92 5B 4F 17 EF 9D 30 48 75 70 7E 6D A0 67 28 32 14 F7 19 03 E5 A8 7E 1B 78 BB FB 3F C3 DA B4 FE 28 D6 A4 DD 05 CE D5 5B C9 8B CA 08 5E 55 41 3B 8F D0 56 1F C4 3F 13 DE 78 8B C3 BE 65 AE 8A 6D 74 67 B9 59 05 EC EB B6 4B A9 02 85 0C A9 D9 71 C6 E3 91 80 A3 20 F1 57 EC DA 92 B7 97 F5 FD 7E 46 0A 71 E4 D7 CC F4 1F 0C 43 2C FE 0A F0 FF 00 9D A8 FD 96 79 6D 52 2B 64 01 1C 6F 08 E7 72 92 A1 B7 18 F3 90 0F 01 78 EE 5A CF 86 6E 67 B9 D2 EE 96 66 BA BC DF 23 46 59 27 F9 95 76 13 90 C5 81 19 3C 02 0E 72 47 41 92 3C 83 C1 FE 31 B3 59 EC AE B5 EF 19 CF A7 43 A7 A7 93 6F A7 C5 6B 23 E0 04 64 0C 70 A5 01 F9 B3 92 18 9E 41 C5 74 F6 FE 20 87 53 F8 61 AF 6A FA 3E A3 75 6B 71 65 2A CE 8D 13 ED 74 71 82 15 C1 38 65 3D 08 39 07 DC 8C 57 44 E0 EF 26 D7 F5 72 20 DF BB FD 74 3D 3F 52 BA BE 86 E1 56 DA 10 E8 50 12 77 38 E7 27 D2 27 FE 7F 87 A9 5C EF C2 BF 10 6A 1E 23 F0 25 AD DE A0 03 5C 44 ED 6E 66 2E 49 94 2E 30 C7 DF B1 F5 23 3D E8 AE 3A B8 79 29 B4 E5 F9 FF 00 9A 3A 63 38 B5 74 8A 1A B7 8C B5 FF 00 08 6B 97 36 77 1E 0F BB D4 34 73 23 5C 47 7D A6 87 7D 91 B1 2C FB C1 04 6E 04 B1 FB CA 31 8E 82 B8 CF 14 EB 9E 1E F8 83 A0 E9 FA 67 87 BC 55 6F A3 A4 05 A4 9B 4D D5 9E 4B 78 5D 4B 02 14 BF 2B F2 9F BA 80 90 01 E3 6E DA D7 F8 BB AE 45 E1 F5 D0 FE C5 A5 D9 4B A9 CB 3B 8F B3 DE DA A5 C3 3C 20 9C 2F 72 14 B3 70 14 82 3A 0C 72 2B 8D F1 BF 8A AD E7 D1 9B 49 BD F0 1D 86 81 24 D3 22 4B 75 17 93 2C D1 80 41 6D 91 ED 53 9C 0F EF 0E B8 CF 35 D3 05 CC 94 BC FF 00 A7 F8 FE 06 D1 8D 92 B2 B1 77 54 BA D5 B4 F3 0C 1A BF C3 9F 0A EA 56 77 6A B1 5B CD A5 20 DF 3C 7C 2A B4 6E AC CE 01 18 DA D8 06 BB ED 1A 0F 0F FC 3F D0 B5 1D 4E D7 40 BF B2 BA F2 89 92 DA 44 76 0C EA 39 58 E4 25 80 0C CB FD E3 9C 02 01 E3 3C 8C 5F 05 F4 8D 47 45 D3 2E B4 7B DB D3 73 2C 11 CA DE 76 D8 64 5D CA 48 91 87 3B 57 23 80 01 27 1F 78 F2 6B 7B 48 D4 FC 23 6F 63 1D 84 9A A6 A1 77 7D 68 82 77 B8 87 56 92 E1 77 BB 94 69 73 0C B8 1F 33 92 78 52 43 63 92 C3 3A 4E DC AD 27 A9 0E 50 95 BB 19 3E 2B F1 27 C3 9F 88 30 59 DB DD 78 95 ED 75 16 94 0B 7B 95 B4 78 96 DB 96 23 CC 2C 30 47 21 49 DD 8C 8C 8D A0 B5 47 65 6F E1 D9 75 9B 3D 7B C5 BF 10 21 F1 11 B4 85 2E 2D E2 5B 88 22 8A 27 18 C8 F2 BC CD EC DC 06 C0 45 27 6F 39 27 6D 75 36 FE 0C F8 7B AF DC 4E D6 D6 76 B3 DC 4E FB FC C2 EC 24 24 6D 24 30 90 92 C5 BE 6C B0 5E 99 EE 32 7C B7 C6 DE 11 8E 7F 1C 45 E1 7F 0F E8 B1 D9 BD C1 13 10 23 DC D1 45 8E 58 9E 4E 38 63 C1 39 C6 06 72 2B 9E 57 52 49 68 DF F5 7D 7B 21 D9 72 EB D3 FA FE 97 9F 99 6F C3 7A 8A 78 FB E2 B1 F1 2E AF 13 C9 69 0C C2 3B 0B 50 B9 19 19 F2 C3 63 3C 2F DE 62 33 C9 E8 46 6B DF 63 86 E2 48 9D 05 D5 BD F4 28 A7 CB 69 D5 4B 19 D5 D8 FC C5 00 50 14 85 1C 2E 41 07 B8 AF 39 D7 7C 35 A2 F8 6B 43 B4 D0 F4 C9 AE 5E E6 30 A1 20 5B B6 70 EE 79 79 1E 33 95 43 DC 15 C6 77 1E C2 B7 F5 53 FF 00 08 DF 86 52 E7 FB 5A FD 35 70 16 26 C5 D9 9C 34 A5 41 2A 52 4D EA 07 43 90 01 C6 30 40 3C A7 6E 5B 27 A2 FC 49 5A CA F7 D5 FF 00 5F 23 86 9F 47 D4 3C 6E FA C4 1A AF 88 75 0B 83 14 12 CF 65 63 00 FD C6 FC BB 2E 59 72 1D 40 2B 86 38 27 70 00 E0 00 73 34 6F 02 EA 9E 28 F8 77 60 AD 36 A8 06 C9 1A CE 37 BA 1F 65 0E 19 C8 FD D2 A1 20 91 B8 6E 66 1C 9C 67 A0 3E 95 E1 8F 0E C9 14 45 A4 D6 B5 0B 5D 4A E3 F7 D7 10 C5 14 60 81 96 51 BF CC 8D 8E 09 DF 82 70 18 82 46 71 9A 54 87 54 B4 5F EC DD 22 E3 C4 67 63 B2 C6 D7 10 DB 24 2A 32 4E 43 18 8F CA 3B 28 C6 06 00 C0 00 56 D0 9C 92 71 EB A7 CB FA D0 A5 27 2D 7C CF 0F D1 05 CC 1E 23 F0 B6 A1 72 AC A9 2C 89 67 31 3F 29 D9 B8 C6 41 3D 7E E7 1E BF 2D 7A D2 D9 3C FE 1E D5 B4 A2 49 93 4C B8 37 08 41 FB CB C8 C7 4E 46 37 37 5E B8 AA D6 BA 2D 86 AB 67 69 3E A5 3E A9 26 A1 6B 78 04 30 D8 DB 42 A1 59 39 5C A8 8F 67 19 3D 71 90 0F 07 06 AE 47 67 A9 58 78 B5 22 BC B9 BA B5 8F 50 02 15 91 05 B9 72 9B 54 6D 38 4D 9D 40 E8 A0 F6 19 EA 7A 25 27 2D 16 EB FE 1F FC C6 D2 BD FB 7E 8C E4 3C 60 97 D7 77 9A 73 D9 47 1D C4 BE 22 D3 BE C2 C0 41 E6 B2 CF 14 A3 7C CE 8A 87 25 61 72 A1 C0 2E 17 76 3D 4F A0 5A 3E 9B A5 EA 89 69 A4 6A DF 66 B9 B1 10 5A 3E 97 AA DC 37 97 00 96 30 F1 C3 1B F2 4C 8C 63 51 C3 B8 03 76 01 C0 15 E5 5A B5 DD FE 99 E2 5F B6 C7 AA C5 15 A7 86 2F 51 4A E7 E6 9A 47 70 B2 AC 40 11 BF E5 51 B9 0B AF 01 BA 64 9A E9 FC 25 E0 7D 5A C7 C8 F1 77 89 65 67 BF 9A ED EE A2 B1 92 C2 5B 91 0C 92 7C CD 2B 47 11 1B 24 3B 47 6C 2E 17 23 70 01 79 6A C6 37 6C C2 54 E2 DE BD 0E DD B5 DB C8 B4 5B EB CB 4D 19 74 FD 52 38 E2 43 1C F1 6E 83 21 04 9B 59 D4 29 1B 51 C0 2C FB 51 4F 00 F0 4D 73 F7 7F 10 21 D4 7C 3E C7 C4 9E 1D D7 2C 20 98 AF 95 75 68 0E CE A1 D1 D5 CE D1 B8 15 04 75 CE 3D 09 15 BB FD B2 F7 96 77 04 78 93 46 96 38 22 69 DD 52 09 04 A9 D3 92 9F 68 DC 00 DC 46 1B 69 56 0B D3 03 1C EB F8 12 FB C5 1A F4 F3 6A F1 49 A5 E9 11 CC 40 B3 82 E4 4D 33 48 40 3E 63 9C B0 0C 41 5C 9E 4E 00 1D 3E 6A C2 57 E8 EE 8C EC E3 B6 CB FA FC FD 0C 8D 16 7B 4D 1A D6 EA DF C3 7F 11 6D 6D C3 B6 F0 97 5A 60 8C 3B 1E 00 69 1C 13 C0 1D 71 81 9E 95 DA F8 3E E7 56 83 4F BB 7D 4F 5C D3 35 58 C4 D0 E2 7D 29 96 59 77 12 A9 89 0E D0 0F CA 00 CE 0B 11 9C 1C 81 5C 24 D1 5D F8 CF C6 F1 C6 B6 B1 5F 69 DA 40 F2 11 6D 11 21 12 AA 64 85 02 47 39 04 8C 1E 4F CB CE 2B B5 F0 EF 87 BC 23 71 7F 7D 25 8E 98 B6 F7 6E 3C A9 34 EB B9 9D 24 48 B2 A5 9C C6 C3 2B 9D DC 75 18 0B 82 B9 20 25 25 F7 ED FD 7F C1 12 BF 35 BA AF E9 AF 3B 1D 6D BB 5C 46 52 DE 65 92 E2 78 50 83 2A B9 8D 5F 1E 59 3F 2B 1C 9F BD 81 F7 C6 14 86 70 49 C9 75 1C D2 4B 6D F6 49 15 6D 4C E2 49 15 21 C9 7C 2B 3F 0C 41 5C 16 D9 CF 1D FE 6D C4 53 24 D0 51 65 92 6B 4B DB CB 69 5D 83 B6 D9 4B 2B 30 00 6E 60 79 6E 00 1C 9E 80 0A 95 E3 BB 96 D6 10 D3 AA 8F 29 E2 B9 94 82 AC A7 18 F3 10 30 2A 70 CB D1 97 04 36 73 81 86 BB 76 FC 4D 95 D5 CF 3B D5 7E 18 69 DA 64 FA A6 B9 73 74 F7 AD 34 DE 64 30 BC 61 56 32 CF 93 BB 9F 9F AF 1C 01 ED E9 AB E3 ED 39 A7 F8 65 77 22 CA E5 45 BC 12 F9 6C DF 2A B2 95 C9 51 8C F2 0F 23 38 F9 46 06 49 27 A8 F1 2A 35 D6 81 22 5B A3 CC D2 15 28 22 52 D9 E7 39 E3 B6 05 73 37 1A 94 97 F6 D2 68 DA A5 A5 E4 56 28 90 AB AC 7A 45 D4 EE 4A 85 6F BC 8A 53 D3 1D C7 71 5A 25 CD 1B 76 B7 E8 64 D2 8C EE 96 EB FC CF 27 F0 E4 BA 97 81 17 5F 37 9A 26 A3 F6 AB F8 5A DA DA 45 B6 DC 31 B5 B9 FB C0 85 24 A1 0C 03 02 14 81 EA 20 78 06 85 F0 76 F2 09 3C D8 6F 6F EF 23 12 45 24 4C B9 03 27 19 E3 90 17 BE 47 27 8E 72 3D 5F C4 5A AE 8B AA C1 65 A4 E8 FA 8D 85 E4 93 5D AB 37 D9 AE D6 79 18 63 1B 9F 04 9C E5 B1 CE 7A 0F A0 E1 FC 79 63 2E A9 71 A6 68 70 EE 02 7D 4A 46 7E 72 55 51 7E 66 C7 5F 95 4B 1C FF 00 3A EA 53 E6 B5 D5 AF F9 26 4B 4E 2D 2E D7 7F 3B 1E 89 F0 F9 AC 34 DF 00 68 B6 A7 58 82 09 16 DC 3C 91 AC D1 70 CE 4B 9C E4 1E 7E 6A 2B 2B 44 F0 18 D6 74 B8 EF 9E F1 ED 04 A5 B6 44 B1 0C 05 07 03 BF F9 14 54 54 8D 37 36 DC BF AF B8 23 07 65 A7 E7 FE 65 2F 11 FC 3E D5 F5 3D 6E DF 5D 7B 9B A9 2E EC C2 41 64 62 09 0C 91 08 C9 64 7D 8A 24 8E 52 49 E7 26 04 CA FF 00 08 6C AF 1F BB 3E 3B 37 5E 3A D4 24 BC FB 18 F2 A1 7D 46 D9 56 28 24 24 B7 CE 91 B1 8D F1 FD D5 93 3D 73 F7 76 D7 A9 59 6B 97 17 5E 13 D5 F5 41 14 50 5D 42 F1 4A 1A 12 C3 73 C9 6B 0B 92 D9 63 BB 06 52 00 39 00 2A 71 F2 8A 93 C0 16 D0 A5 8C A5 23 55 9D E1 57 37 1B 41 93 E7 2C 08 C9 ED F2 29 C7 AF 5C D6 4A 6A 16 4F E5 F3 57 F9 68 74 BA A9 A5 26 B7 D1 7E 66 5D BC B1 DF F8 92 CE D7 C2 F7 22 06 82 16 59 6F 5D 40 DC 08 5D FB 62 20 28 C9 45 24 2A 8C 90 0F 00 57 47 AB 69 37 F7 B6 BE 4E A3 A5 E9 5A E4 71 63 C9 13 2E 30 C4 60 B9 89 C9 46 61 EB B9 4E 09 00 8C 9A C9 F1 26 9D A6 69 9A 36 AD 0E 9F A4 E9 D6 89 02 C6 50 43 69 1A FC D2 10 19 B8 1D 70 A0 64 63 F4 18 BD E1 68 7F B3 FC 19 6D 3C 12 CB E6 5C C8 8A 4B B9 61 1E E9 42 1D AA 78 1D 49 E9 D7 AE 7A 54 7B 48 CA 9B A8 B6 5F E6 54 A4 AD CC 8E 40 DB B5 A7 89 A7 B5 D1 EC 75 9B 59 E6 71 E4 84 BD 78 1A 22 98 21 02 33 3C 06 35 5E 02 94 C0 52 31 D0 1A 9F 4B D1 35 DB 79 2F 40 D1 A2 BB 9A 51 1D C3 3E A7 02 5C 5D 4E 0A 95 50 F7 0D 22 AA 91 B4 81 88 8E C1 82 55 89 3B BD 0B 53 9E 5B 3F 0A 5C 5D EF 69 67 B6 B4 37 01 99 8A EF 74 5D E3 76 CD BC 12 BC 81 80 41 23 A1 C5 5B 97 4D B4 96 09 62 10 44 9E 64 4B 09 65 89 49 0A B9 DA 30 41 04 29 24 80 41 03 27 8A 4F BA 21 CB 9B 45 B1 CD 69 33 DF 45 66 2E C7 84 2D B4 D9 99 C1 32 59 4F 04 C8 D0 70 C4 EE CC 7C 9E 47 A0 E1 B2 7A 56 7E 90 1B C6 3E 2C 93 57 78 E4 1A 75 8B 0F 26 39 58 90 5F 8E 7B 8E D9 20 71 F7 7D 49 3A 1E 3D 2F A7 F8 63 75 B4 B3 AB 31 4B 72 4C EE 72 98 6C E7 27 92 7B 93 92 7F 2A D4 F0 ED 85 B5 B6 99 14 56 F1 2C 46 15 45 12 2A 8D EC 0A A3 B0 24 8E E4 9C FF 00 4A 51 BB BB EC 37 B2 5D CB 2D 66 B3 47 6E 8C F1 45 08 26 38 ED 55 B7 42 E9 C8 C6 00 5D D9 8F 3F 29 CA 83 CE 1B 68 34 5B 7D 8E E3 49 88 B8 81 F4 EB 94 56 45 94 E5 08 7D BB 54 06 03 82 4F 00 FB 00 3B 08 EC ED 61 B6 D5 E7 B6 81 4A 08 E1 86 57 94 B1 79 66 C9 94 6D 77 6C B3 01 80 47 39 1D 33 8E 2B 4F CB 6D FB BC E7 C6 ED DB 70 B8 C6 31 B7 A6 71 9E 7D 73 DF 1C 53 6E FB 74 FE BF E0 EE 26 9E D7 D0 E5 34 88 DF 4F F1 9E B7 0C 0A 66 89 A2 13 B2 44 47 0E 79 0B C9 C0 27 2D 80 48 E3 1D AA 87 8E AF C8 D0 17 57 57 B7 59 2C C3 DE 5B 3A 4C 0A 4B 12 91 86 59 0F CB 96 DF 16 17 19 DD 90 37 8C 13 D8 DD 4C D6 F2 34 A9 CB 33 41 19 0C C4 8C 34 9B 4E 06 70 0E 18 F3 F4 CE 70 2B 3B 5D D2 ED 75 BD 46 C7 4C BF 46 92 CD 83 5D 49 16 F3 B6 56 89 D3 6A BA F4 64 CC 84 95 23 92 AB E9 44 6A 5D A7 DB 4F BB 40 72 F7 AE 79 0F 81 3C 13 7B A9 78 9D 35 3F 10 25 B5 ED C4 F2 49 73 73 6B 3C 42 54 81 9C 9D DE 6A 0C 2A C8 D8 38 5F E1 E0 91 91 B4 7B 58 29 2D F3 01 74 CA BE 6E D9 22 FB 40 C8 91 54 15 50 06 48 0C B8 72 B9 1C 01 C7 CC D9 AF 67 3C FA A4 D0 B4 93 3C 3B 6C A2 9C 08 4E 06 F9 43 82 4E 73 9D A1 78 07 23 9C 90 48 52 25 D6 26 6D 1F C3 37 97 76 FB 9D F4 FB 49 25 88 4D 23 BE E2 91 9C 6F 39 CB 7B E4 E4 F5 EB CD 39 EB 6B F6 2F 97 DF E5 EB 7B 7E 85 59 74 4D 16 E6 E2 56 9D A0 B9 B3 F2 11 CD 9C EC 24 86 10 01 54 91 10 9D B1 82 A1 86 40 C1 DB 91 83 BB 77 3D AF 68 9A 96 9B 63 75 15 B7 88 35 08 AD AE 51 D3 7C 8F 2D C3 02 46 14 12 CA FB 40 27 A8 29 9C 9C 9E 01 1D DC 71 6C E5 DD A4 7C B6 1D C0 C8 04 E7 6F 00 70 38 1F 80 CE 4F 35 CB 78 85 CC DA DD 86 92 70 B6 84 23 14 4E 3A B1 5F D0 0E 3E B5 94 A3 D0 C6 72 B2 BB 32 2C 62 D5 AD FC 3B 6D 69 69 A3 59 0B 48 DB CE 56 B6 40 A5 B6 F0 57 C9 71 21 93 3C 65 B7 A1 3B B1 B9 7E F5 6D DF E9 DA 7D F4 D6 B1 42 5F 6C 32 89 16 25 9D 83 44 FB D0 06 11 95 62 A4 06 72 08 00 0C 72 40 39 58 8F 88 EE ED ED D1 62 86 DD 54 1B D5 03 6B 70 21 BB 48 53 BF F7 58 93 EF 8C 60 71 52 F8 66 C2 DA FB 4F B8 BA B9 89 25 9E 69 5B 2E E8 1B 1D F2 32 31 D4 9A A6 9D EC FB 5F F4 FD 05 6E 57 C8 F5 B7 FC 39 65 B4 CD 4E 0B 51 6A FA A5 AD E4 52 EE 46 B7 BE 80 91 38 20 92 9B B7 12 32 33 D4 36 00 E8 40 C5 4D 6D AD 95 64 17 96 B7 31 7D A1 D0 43 26 52 48 1B 38 5F 92 45 E8 A4 E0 8F 33 6B 36 EC 00 4F CA 30 FC 45 A6 C1 A0 B6 91 77 63 BD 65 93 54 82 DC 17 3B 82 09 5C A3 32 FA 30 56 20 7B 75 07 9C F5 36 97 32 5C DD DF C6 D8 55 B5 B8 11 AE D1 F7 81 89 1B 9F C5 CF 4C 74 1F 8D FB 29 72 29 F4 FF 00 83 62 AD 25 AF 72 48 78 B7 B7 82 59 4B 5C 18 D5 88 9F 61 91 B6 ED DC C4 2E 06 72 46 4A F0 09 1E D4 DB 77 8E 28 90 B2 49 14 B3 9F 34 C0 E4 33 A9 24 64 61 49 1C 12 32 46 47 72 4E 49 39 57 48 D6 96 FA BC 72 48 6E 84 76 51 DC 37 9A AA BE 6B 85 70 4B EC 0B BB 77 94 B9 1D 08 C8 E0 71 57 63 B9 71 AD 45 0E 3E 59 E1 9D 9B 2C C7 06 39 11 57 03 38 1C 39 CE 07 3C 7A 54 CA 3B 58 D3 D9 C9 AE 63 1F C5 7E 1E 8F C4 5A 9E 9B 1C F2 59 FD 9A D4 B4 B3 2D C2 24 98 07 A6 11 81 19 21 5C 64 F0 30 4F 38 C1 C0 8F E1 95 B7 DB 65 6B 7B 89 ED 5D 92 54 49 61 57 44 85 4B 01 85 45 74 5E 70 4E 36 14 38 24 F5 5C FA 05 CE 9F 0D DB 49 E6 B4 85 25 81 A0 74 0E 40 2A DD C1 1C A9 EB CA 91 D7 9C E1 71 61 63 55 6D F8 05 CA 85 2F 81 92 06 71 9F CC FE 66 A9 54 92 D1 11 64 F7 47 2D A7 F8 6F 50 FB 04 28 7C 55 AE C6 B1 A2 A2 62 1B 58 83 2E D0 41 08 61 2C BD 71 86 E7 20 D1 5D 15 8D C3 DD 5B BC 8E 14 11 34 B1 FC BE 8B 23 28 FD 00 A2 A5 4F 9B 51 F2 72 FB AF A1 FF D9";
            } else if (rand == 8) {
                c.setLIEDETECT("레톤스피어");
                packet = "7A 16 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F7 14 D9 75 68 E9 1B 79 F1 C9 34 91 CA 66 88 10 40 72 AE A5 7E 5C 8C 02 80 F3 D8 9D C3 24 E7 F8 83 5F D2 B4 64 47 D5 EE 2E AC 62 59 90 C7 32 2B 95 90 F2 76 FC 80 E4 70 41 0C 3F A1 A5 F1 3C 7A CC B6 B1 7F 63 B6 9A A2 32 CF 70 35 12 E2 26 8C A9 18 3B 7A 8E 49 20 FA 0A F2 8B 7D 72 F2 EF 5A D7 F4 BD 77 C5 96 97 76 92 D8 11 BC B2 9B 79 24 D8 A4 08 FA 00 41 27 91 82 48 07 06 B9 EA 56 E4 BA 8A FE B7 FE BC CA 47 5F 61 F1 1A E6 4F 19 C1 A3 DE DA 5B 1B 7B B6 22 D6 E2 CE E8 49 1B A9 FB A7 24 7C C7 72 95 EA BD 48 20 90 2B B2 89 23 BB B4 85 E4 4F 2D 9E 71 24 71 DD 29 2E 84 30 76 5F BC 77 10 55 88 2A 4A 8C 29 19 0A 33 F3 A5 95 D7 D9 6D 3C 3B AA E7 FE 3C AF 5A 36 3E 81 59 24 1F FA 1B 7E 55 ED ED AC 5F 58 DB 5A DE C3 75 15 CE 9E 55 20 2A C1 D9 95 86 72 77 36 19 8E 3F 88 F5 C7 DD 1C E5 53 A8 9C 5F 3B EB FE 56 FC F7 25 B6 DD BF AD CE A2 2D AC 4C 69 6E F1 C5 81 28 70 02 06 66 24 91 8C EE CE 46 4E 40 CE EE FC E2 29 E5 F2 A7 F2 A2 B8 82 39 A6 96 32 A2 67 2C 5B BB 2A AE 46 09 44 6C 63 8C 82 48 38 39 79 B5 51 0B C2 86 53 1C CC C6 46 37 0F B9 43 03 9D A7 39 1C E3 00 11 8E D8 C5 54 B1 B4 8E DB 50 BA 92 1B 58 DA 57 F2 92 EE F6 45 D9 35 C3 2A 70 4E 10 2B 00 0A 60 A9 C6 59 C7 05 70 7A 74 41 6D 34 26 6B D9 23 8A DE E7 EC E6 68 24 8C B4 B2 5B 3F 98 23 C2 EE 05 57 19 75 3C 8C A8 DC 49 5F 94 82 4A 90 D8 41 0C 82 E1 94 49 22 8D CD 29 8F 0F 24 9B 42 19 18 28 01 98 AA AA 82 17 20 64 0E 0E 2B 1A E7 57 FB 5F 89 74 3D 3A C0 5A 5E 2B 5A C9 7C F7 32 C7 B9 A2 42 9B 23 95 08 20 7C C5 99 4E 39 C1 3D 01 AD B4 85 89 79 84 97 50 7C EC 5A 36 65 7D D8 61 C8 CE EC 02 13 80 A4 70 E7 20 37 49 8C EE BD 37 25 49 4A FE 41 34 52 0B B0 D1 79 87 CC 41 1B F2 48 4C 12 55 C6 E3 B4 11 F3 67 0A 49 25 33 C0 AE 7F C5 5A E4 DA 52 BC 92 84 B5 53 A7 CD 35 94 D2 BA 80 B7 4A 31 B0 E4 10 5F 6B 65 00 27 76 24 C8 E0 1A D9 BA 5D 32 EF 44 8D 30 F2 E9 D2 C3 FB BF B1 6F 2A D1 EC 24 6D F2 B9 2A 57 A6 38 3C 01 C9 15 C8 7C 53 9A 67 F0 FA C1 67 23 B4 97 77 31 5B 49 6C B1 82 EF CB 94 E3 1B B9 64 38 C7 5E DD F3 AC 57 BD FD 7F 5E A4 D4 76 83 95 F5 47 43 A1 EA F7 53 78 4A 0D 5E E7 17 60 D9 09 E4 FB 3B AB 3B 3A AF CC 8A A1 54 64 90 78 27 86 24 74 15 6D AD 6D 1D 27 7B 87 7B 59 A3 59 0C 8C 2E CE E5 89 9D 88 62 D9 F9 41 01 B0 78 28 0B 05 2B CD 62 F8 3F 48 93 43 D1 E3 D0 EF 4A 7F 6A 24 53 1D E1 5D E0 96 36 90 95 23 38 0C 17 70 05 4E 08 25 B8 C3 6E 3D 70 42 1C 10 ED 80 0E 57 A8 24 9C E7 D7 8F EB F4 C4 4A EA 56 B6 83 8D EC AE 60 EB 92 5C C3 67 24 36 FA 9D BC 77 56 F6 FF 00 69 56 BF DF 12 31 52 B8 76 78 D9 06 C1 86 DE B8 3F 7D 72 00 C2 B7 93 43 77 75 E2 6B 89 05 DB DC F8 DB 52 85 BF 73 6F 6E 1A 0D 32 D9 80 24 34 84 84 0C DF 7B 1D 98 71 9E 78 F6 4D 4A DD 35 0B 19 F4 E7 B8 8A 42 23 F9 83 88 E4 69 08 19 21 D1 94 AE 39 43 D3 F8 87 4E 33 F3 25 B5 86 AA B7 D0 68 D6 AE FA 9C 52 CE 66 B8 D3 74 F6 62 BB 43 00 43 15 C6 33 8C 1F 4C 03 59 36 F9 AC 77 50 4B 91 BD AD FD 7F 5A A4 7B 6F 83 6F F5 88 74 EB 8B 51 79 A7 EB 12 6E 02 38 74 81 1C 56 D6 2B CF C8 1C 2E 09 F9 94 E3 04 F0 7E A7 AA 86 05 D3 85 BF 9F A7 C0 15 84 D3 5C DC A3 81 1D BB 96 12 31 F9 CE 42 B3 02 DC 74 20 12 3B 8F 24 87 4B F1 5E 94 20 8A EE 0D 3B 49 D2 61 F9 E5 D1 ED 9D 4C 97 08 80 65 A4 64 0D 9D C0 72 EC DC 6D 27 1D 01 F4 4F 12 F8 95 B4 2F 0E 7F 68 36 9B 33 D8 87 FB 34 B1 41 70 B1 18 91 89 42 43 21 27 7A 95 C6 01 5C 6F C6 72 32 2B 9A 2B 57 FD 7F 5F 3F 33 2A 91 B6 DE 7F D7 FC 32 B7 9B 3A 81 B2 E7 C9 63 14 AD 1B 01 2A 97 5D A1 48 C6 01 53 83 9E 73 82 38 23 9C 10 2A 3B CD 4A CF 49 B6 13 EA 97 F6 96 B1 B3 94 59 27 90 44 A4 9C 90 B9 63 D7 03 F1 C1 3C 56 14 23 4A F1 96 9F 06 A1 2C 73 4D 6D 73 0E D8 D5 66 79 61 1D 07 EF 21 3F BB 2E AE C7 1B 91 BE E0 6C F1 81 7B 44 D0 AC F4 58 E2 6B 4D 2E DA 09 E4 55 4B 89 23 86 18 DB 01 39 39 8D 17 77 CC 3D 07 24 90 00 C0 AA D1 3B 33 15 28 D8 A9 2F 8A B4 DB DB 21 2D 85 E7 F6 8D AD C0 DA 45 84 13 C8 F1 A9 D9 9D CD 08 66 8D 82 96 20 10 A4 9C 0C AE 09 AA FA CF 8A EC 1A D2 19 96 DF 53 10 C5 72 8C F3 BC 7F 62 54 65 39 DA 5E E3 66 77 6D 65 C2 F5 FB AC 54 37 37 F5 6B 6B E7 D3 A4 1E 19 8F 4E 87 50 8C 88 56 6B B8 48 48 D5 55 B0 17 68 E7 6E E2 07 50 32 D9 E7 20 F9 25 BE 9A 3C 61 6F 78 D7 9E 25 83 C4 1A DD 94 32 4A 9A 43 43 35 B5 B0 6D DF 31 E0 46 4B 11 9E 81 7E 62 32 70 39 4E AE 9E EF 4D 45 36 97 BA 96 FF 00 D2 2A F8 A3 55 D6 6D 23 4F 1A 78 63 56 D4 6D F4 9B AB A2 8F 04 B3 A1 C4 E3 20 93 1A 65 0A 90 A0 65 B2 C7 19 39 C8 27 D4 BC 39 A9 DD F8 8A CA D5 DE FB 4D 09 25 B2 DC BD 9C 11 C8 26 1B 81 E7 CF 56 50 18 B7 2D 88 F8 DD 8E 72 18 F0 BA 24 AD E3 5F 84 7A FD 9C 76 16 D6 CF 6C E6 48 E0 B6 25 51 19 15 19 42 29 C9 19 DA D9 CB 12 58 93 DF 89 3E 1B 6B B6 DA 8F 81 62 D1 A7 5B 8B 8B 9B 3B 9C 2C 11 DB 89 89 88 B0 72 70 71 8E 37 A6 73 91 BB 22 A6 3A 5E 1E 8F EF DC C3 9E 3C CA 5D 1D D7 CD 1E AD 6E B2 C9 0B 09 ED 96 07 B8 0B 25 C3 C0 08 0E 58 15 DA AC 08 7D CA 02 02 E5 47 00 63 1C 85 92 53 67 3C 5E 7C DE 4C D6 73 AC 41 18 BF 98 8E 4B 7C B8 5F BB D5 97 0C 3A F1 E8 2B 80 5D 43 42 D0 7C 74 F6 70 EA 76 96 F1 CB 19 8B FE 3E 63 2F 6E E4 63 F8 89 C3 02 3A 1C F5 E8 6B BC B9 BB 8E DA 06 BE B9 41 13 5A DB B3 4C FE 5B 38 8D 4F 2D B4 ED CB F2 83 81 8C 8C 1C 7D DA 7C DA 5F FA FC 74 36 83 6D 38 BF 42 DD B5 D4 57 71 BC 90 92 C8 B2 3C 79 C6 32 CA C5 5B F2 60 47 E1 45 66 59 5E DA F8 9A CE 2D 4B 46 D7 66 FB 23 0D A3 EC CB 19 19 EB C8 74 2C 1B 91 C1 C7 6E 28 AD 79 6D BB 2A E9 EC 47 E2 7D 26 CF 5B D1 A6 D3 2E E2 33 BB A3 CB 06 F0 C1 56 41 C2 E5 97 00 1C B8 00 13 93 CF 5C 1A F2 CF 0D E8 DA FD ED 86 9F A5 5C 78 5A D2 CE D2 D6 E5 0D CD ED E6 9E 4C B2 83 28 21 76 E3 24 1C 28 63 D3 03 E6 60 2B D8 6D 2C DE DF CB 31 88 63 09 B6 26 2D 02 87 78 51 48 51 95 20 0F 98 96 1C 60 06 23 68 27 23 17 5D D4 75 3B 35 78 D6 DD 7C 9B 86 DA 5A 65 5D A7 AA ED 1F 3B E0 10 B9 FB 98 C3 74 CE 40 E7 A9 15 19 73 FC BF 1D 2E 0A ED 58 F1 ED 63 C3 D2 D8 68 7E 21 8D 56 46 86 CB 56 41 1E C8 8E DD B8 90 12 7F BA 3E E8 C9 38 CE 07 52 2B D0 34 A8 8C 9E 0F BC 0F BB CE 8B C9 72 0B 92 07 D0 74 07 93 92 39 3C 67 A0 C6 CE B7 65 A5 5C E8 97 26 76 BD 96 DE E0 03 79 22 CB E5 CA 38 0C 88 63 20 61 CE E5 F9 59 54 00 49 24 60 66 3B 1B 36 87 50 D5 34 D6 C7 EF AD 49 4F BD 83 C7 1C 37 23 AF 43 92 3A 64 F5 AE 77 49 C1 5B BA B6 FD 97 FC 04 56 EF 99 7F 57 3A 2D 36 E5 A7 B2 B7 54 59 11 99 43 87 95 4B AB 8F 94 B6 08 3C 7D E2 A0 1C 1E 0E 01 02 9D 25 C5 9E 9C B7 7A 94 F2 C4 B0 48 82 53 36 F2 C4 AA A1 24 01 CF 01 54 B0 0B EA C7 19 C9 39 9E 18 B8 85 F4 98 44 D7 0B E6 5B C9 B5 50 4A 57 01 CE 17 70 DD 86 C9 CE 32 3D 80 C8 AE 3B C5 16 87 5D F1 ED A6 9B 6E 22 8E DB 56 B6 44 BC 1B D7 7A B4 32 16 76 F9 72 3C C5 55 31 86 20 8F BC B9 E0 E3 A2 75 9A 8A 92 EB FA 98 57 9C A1 14 D6 BF D5 8D 7F 0C 44 CB 6D 79 AB 6A 30 5B DB DB EA 51 AD D4 A0 96 53 0C 0A AF E4 2C 6C A0 21 28 91 83 F2 E1 FE 7C 9D A5 06 FE A1 91 E1 BC 96 F1 EE 0C 12 B4 01 1E 37 79 65 84 3B 36 10 A0 25 54 9E 08 2A 06 E3 95 E4 67 E6 B0 5A 44 B1 92 2B 48 E6 24 47 28 89 B6 2A 6C 65 38 55 00 E3 FE 02 70 41 0B 92 79 04 A3 3C D0 DF C8 EE F3 79 00 E4 2A C2 59 58 14 E9 C1 2D B8 14 27 20 05 3B F1 82 DC 8D 52 71 56 FC 7F 32 E2 B9 63 BF 4F E9 FE 6C 5B 79 E7 32 C5 04 D8 F3 8A 96 9B 03 E4 52 15 72 A9 9C 31 19 60 77 61 87 04 12 0E 05 71 9E 2D BF 8A FF 00 C6 1A 5D 9C 6F 21 87 49 8A 6D 42 F4 EF 28 B0 ED 1F 23 90 01 25 94 8D C0 15 39 0C 3A 86 35 D7 EE B9 4B D6 36 D6 F3 18 92 5D 93 19 5C B1 70 70 C0 C6 0B 60 28 32 12 5B AE 23 2A 14 FC BB 7C D2 7B 68 7C 51 F1 22 4D 1E D6 35 5D 36 0B 78 A2 90 27 CA 05 B2 11 2B 46 54 E1 95 BC C2 A9 F2 95 28 06 36 F0 71 A4 53 DC 8A EB 96 31 8A FE BF AF EB A1 D5 69 5A 76 A5 E2 2D 0E CF 53 D4 2F 67 8A F5 9D AE EC 4E 10 AD BA 96 46 8C 30 4D BB F2 AA 41 23 69 29 2C 89 95 DD 5B 3A 3E A9 1D ED A9 B6 69 1A D7 54 64 79 9A D6 E1 8B 48 80 B1 01 D5 58 2B 18 B3 F7 49 03 8C 02 14 82 05 C6 D4 6D E1 8F ED 32 DE 40 6D 4C 42 4F 31 47 CA AB B5 98 C8 CF 92 02 10 BC 13 81 C7 53 90 2B 2B 57 B3 8A EE FA 3B DB 08 F1 AA 40 14 12 EA 02 CA 82 51 B6 39 41 56 60 85 D5 99 64 0A 76 ED 66 04 02 43 26 D3 56 65 A8 B5 6B FE 27 17 AA FC 3E F1 3E AD 7B 73 77 E2 6F 13 DC 5F E9 B0 46 42 5A E9 D1 79 72 5C 47 9D C5 4A 0C 28 E4 29 FE 32 76 8E E0 56 0F 82 3C 5B A6 D8 F8 7B 5A B4 9C 1D 12 1B 48 FC C8 C2 DC 00 66 91 81 D9 C8 40 EC DF 28 3C 31 C8 ED 8A F4 B1 26 A9 A9 69 B0 6A DA 3C AB 1B 5C C7 11 B8 88 C7 B2 50 CA C1 5D 4A B3 B2 2B AE 5C 10 79 06 35 52 C7 19 18 3A 6F 86 3C 27 E2 AB F9 6F 75 3D 19 0D F4 72 2C 66 49 AF A4 94 CE C1 49 EC 42 B1 C2 FA 10 40 F4 AC 5C 6C EC B4 FE BF AE A7 6C 6A 27 17 7D 7D 3F AE BD EC 51 D1 2F E7 F1 17 86 AD 65 B2 8E C6 2D 3C 89 52 71 AB 42 1A 0B A9 32 59 8B AE E5 27 6A 20 20 8D DD 5B 21 76 83 57 B5 3F 00 5E 78 9B 53 82 5F 12 F8 8A F2 E2 DD A2 67 8E 1B 5B 51 04 50 36 57 0B F3 16 E7 E6 C0 CA EE 21 79 3C 62 AB 6B FE 1F D4 7C 3A 97 1A 9F 86 3C 47 26 9D 6E EE C8 9A 63 95 68 50 EE 3B CA 02 C4 0E 46 70 14 B0 CE 38 E7 08 7C 47 E3 9B 0D 0A DE E7 50 BE F0 DC 6B 22 33 46 D7 12 98 67 9C 28 C9 2A 0E 13 27 80 3E 5E E3 8E F4 A5 25 67 CD D3 EE 39 E4 DF C5 6D 1F A8 CD 36 EB C4 3F 0F 75 5D 33 C3 72 C3 6F 7F A2 BC A7 6D C5 BC 64 4C AA E4 FD F0 09 C6 09 CF 4E 71 80 DD 87 A3 5A 3B 6A 56 76 B7 8C B7 16 93 14 1B A3 3B 86 D3 B8 16 52 AC 00 3C AE 37 15 CE 09 2A 46 73 5E 47 69 E3 19 AD B5 9B 41 77 A2 DC 79 B9 12 3D DA DF 2C E9 28 C8 C1 12 28 00 28 2A 41 21 88 E0 AB 64 64 57 74 DE 20 9C DE AE 9B 6B A7 CA F7 EF 12 DB C9 73 A8 4D E4 86 60 09 19 F2 95 88 24 96 39 DA 8B 92 00 39 2A 2A 93 F7 6F 26 4B D1 B6 BF AF F8 73 5A 69 C5 C6 95 7B 1C 9F DA 1A 7C CC 86 43 1A CA 0D C6 D5 44 2D B3 96 5E E1 72 84 80 4E 72 18 E4 78 EE A7 E2 2D 3A F7 E2 52 6A 7E 03 D3 6E 35 0D 46 68 5A 39 40 47 8E 29 19 86 3C CE 0A B2 E3 3C 93 B4 67 9C 8E A7 D9 2C 6C 6E A6 32 49 77 0D AD BC 72 DB 88 48 81 E4 6B 86 E0 7C C6 73 B1 86 06 46 36 EE C8 07 76 78 13 99 2D FE CF 70 6E AF CA 47 0C 72 1B A8 DE 64 1E 52 B7 CD 97 61 82 BB 57 38 20 8E 0F 7E 0D 54 97 33 E6 5D 0C E5 17 38 F2 BD BF E0 FC 8F 2B F0 EF C3 4D 46 F6 DA F3 FB 53 51 43 6B 34 A6 E2 5D 33 4F BA F2 ED DE 43 F3 79 72 3A EE 63 82 AA 08 C7 01 81 56 27 22 B9 3D 32 CB 58 F0 FF 00 89 3C 43 E1 BB 06 FB 1B 4E 81 A3 59 F6 F1 D4 C5 96 E4 29 1B C1 DC 0F 0C A0 E7 8E 7D 0B 5E D6 2D 34 EB DB 5D 53 40 BF 97 51 91 B6 C7 3C 96 A0 3C 72 B8 19 3B E4 40 22 0C 40 C9 5C 83 D0 81 C8 AA 7A B5 8D E4 BE 34 B2 F1 0C 50 59 E9 3F 68 48 F6 2C 91 99 A5 23 25 8B 48 91 90 AC 49 1D 56 42 71 B7 20 13 C6 6B 96 E9 2D 3A 18 B6 A3 AA DD 34 EF F3 D7 D3 FC CF 3D BE D3 23 B4 F0 85 8E 90 DE 11 D4 6D B5 D9 19 DA 7B BB 9B 62 A8 EB B8 7C CA E4 E7 81 B0 11 80 01 63 DC F3 B5 7F F1 04 43 E0 FB 3F 0B E9 36 FA 84 9A C8 87 EC 66 F6 F2 04 8D D2 26 C6 E5 4F 98 90 0E D5 1C E3 80 0F 61 8D EF 1B 4F 77 A5 89 EE F5 03 33 CF 72 03 5A 03 6A AB E7 CC 76 AE 02 A9 E3 81 81 B8 B3 85 55 04 93 8A C2 F0 D7 86 60 83 4A 93 5B D4 2E BC CD 7A 59 B1 14 19 2C C3 23 18 20 11 C6 18 9C FF 00 78 2E 3E E9 0D D9 1E 59 BF 7B 67 AF FC 0F F3 34 F8 5A 6B 73 D2 BC 19 F0 F7 4D D0 FC 39 15 AC CF 73 35 C9 76 79 DD 67 9A 15 32 74 3B 54 15 F9 7E 5C 03 8E 40 CF 42 28 AD AD 0F 5D B3 3A 35 B0 BE D4 ED C5 D2 AE D9 44 D2 84 70 C0 E3 04 1C 1C D1 59 4B 99 B7 70 54 E9 BD 5A 35 23 80 7D A1 24 0E CF 34 4A 21 92 49 90 E5 97 19 F9 7A 28 24 95 24 A8 C1 C6 3B 0C 62 6B 5A 7D DC 9A 46 6F EF A4 97 C9 3B CB DB C0 30 EC CF DE 30 0B 05 45 20 64 31 E3 71 60 71 5A 7A 6B 3D EC 4B 72 D2 49 1B 45 75 73 19 54 62 55 D4 4A E8 03 06 CF A0 3C 63 04 60 60 12 09 A6 C6 2E 24 92 F6 47 76 60 ED 02 46 CC 59 13 CA 92 55 0E A0 F4 72 1B 0C 73 CE 05 44 92 92 B7 73 6B F2 CB 95 95 34 7D 22 0B 45 69 B3 15 D9 8E 66 48 8C 1B 42 21 DD 87 21 06 15 58 3E ED D8 C9 E0 9E A4 8A 61 B5 BB 5F 11 5B DE CD 18 21 60 75 99 A1 56 2A 76 E7 04 0C 67 24 15 3B 79 E7 20 13 8C D6 EC 30 A5 BC 2B 14 60 08 D0 61 14 00 02 8E C0 01 C0 00 70 3E 95 4B 50 9D E1 BE D3 A2 C2 C9 1D D5 CF 94 C8 EA 08 5D B1 C9 20 65 F7 DC 8B D7 3D 38 C1 E6 85 4F 9D DB E7 F8 30 49 BF 75 2F E9 7F C3 1C FC 3A 64 72 68 3A E6 99 A8 C9 15 B5 AC E6 44 8E 4B 84 1B 54 85 27 78 C9 00 ED DA 1B B6 36 E7 3E 90 7C 3B B2 BC B8 B2 3A F6 AC B2 35 ED D4 31 C7 0C 8E 49 C4 28 0A 8E BF 36 58 83 23 7F 0B 6E 56 1C E7 1D AC 51 24 21 82 6E C1 39 C1 62 71 F4 CF 41 EC 38 A6 88 9C 4A AE 67 90 A8 0C 0A 10 B8 39 20 82 78 CF 18 C0 E7 A1 39 C9 C1 19 46 8A 4E 2F B2 33 74 D4 AA 73 B0 9C 29 11 97 98 C6 A1 D4 F0 DB 77 1C E0 29 3E E4 8E 07 5E 9D 09 06 10 CE B7 AB E5 49 0C 91 C9 90 C8 D2 36 E5 0A 5B 73 0E A1 BE 66 45 C6 17 1C F2 70 16 A3 D1 AE 26 B8 D3 B3 3C 86 49 23 9A 68 4C 84 00 5C 47 23 20 63 8C 0C 90 A0 9C 00 32 4E 00 1C 54 F2 D9 41 2E EC AB A1 66 2E C6 29 1A 32 C4 A6 CC 92 A4 13 F2 FA F4 C0 3D 40 C6 C9 F5 2C A9 79 AA 47 68 93 5E 35 F5 A1 B4 8A 19 4F 97 82 58 BC 64 EF 3B 94 9E 17 04 10 10 90 47 E1 58 3E 0E D3 A4 7B 43 AD DE 86 5B AD 56 58 E7 88 11 E6 BC 31 2A 91 1A 97 39 07 F7 7C 6F 21 58 EF 20 9D C4 56 FE A9 A6 D9 EA 16 23 4C BC 88 CF 69 73 2F EF 11 E4 6C 9E B2 70 41 C8 F9 94 71 9C 01 C7 4A BE 91 2C 65 B6 7C AA 79 D8 00 03 24 92 4F D4 93 CF FF 00 AE 85 25 AA 46 6B 59 EB D0 A3 2B 6A 6B 25 95 B8 48 E4 59 37 8B AB A8 C0 41 10 0A 76 ED 46 24 E4 B1 1E A3 83 9E B5 1C F2 4D A7 5B 87 D3 6C 5A FA 10 63 85 2D 6D 9A 38 C4 2A A4 86 C1 66 0B 80 06 02 8E 72 31 9C 7D DB 27 4B B3 12 A4 B1 C1 1C 53 21 4D B2 2C 6A 59 55 32 02 8C 83 81 B5 9D 70 31 80 ED 8C 12 4D 5C A7 7D 8B 49 A3 83 D4 46 BD BA 69 F4 DB 7B 0B 23 34 28 75 08 AD AE 25 9A 51 0B 02 AB 32 C0 23 8D 84 AA 14 80 43 06 2A 85 70 EC A8 17 47 4F D0 A5 B5 92 5D 4E CB C4 32 9B 0B 9C DC 15 B6 81 65 F3 54 82 54 87 6D E5 B8 3D 47 DE FC 6A 7D 1A FE 4D 7E E3 51 82 FE 38 9E DE 33 24 5E 4E DF 91 94 5C 5C C2 77 03 F7 B2 91 AE 41 E3 3D 85 6B E8 F7 92 6A 1A 25 85 EC A1 56 4B 8B 68 E5 70 83 00 16 50 4E 3D B9 AA 95 27 15 AF 4D 3F 0B 96 A6 D2 6A 3B 1C E5 FF 00 86 E2 BB BE 8A E0 33 6A 37 56 F1 28 9A DE E6 69 50 48 09 60 4E 37 6D C3 15 61 B4 82 B8 CF 18 AB FA 7A A5 8C 60 5B F8 72 2B 18 64 25 27 6B 32 9F 28 19 C6 04 63 73 10 C4 F1 81 8C 93 5A 1A 1B 2D C6 95 06 A0 63 54 9E FE 38 EE 67 DA 4E 0B 98 D4 70 09 38 18 50 3F 0A A7 15 DD C7 FC 25 CB A5 34 CC F1 5B E9 C9 39 90 F0 D2 B3 3B 21 DE 06 17 F8 01 E1 41 07 38 38 24 56 4E 9A 84 AC 82 A3 70 6E 32 7E 5F A7 DD 72 5B 7B B8 A0 36 F0 25 D4 77 13 2A AC 65 AF 65 11 4F FC 00 FC BB 01 24 80 CD DB 27 03 A1 CA CA F2 58 5C EA 30 16 48 26 BC 8E 52 99 8A 55 2D 0F 0E 41 6E 41 C1 DA 78 00 F2 7A 60 12 17 51 78 E2 BD B0 8C DB C1 20 BE 9F EC F3 17 4C 92 8B 14 B2 01 F8 32 F7 CF 53 EB 51 E9 BA 65 89 5B 7D 46 3B 54 86 E2 48 95 8F 94 CC AB CA 8C 8D A0 E3 15 4D 3D 1B 27 75 A7 4D 3F AF 90 D8 EC 44 F6 CC D6 97 91 84 B8 08 D1 CF 6B 12 A8 48 81 06 20 8C 0E 19 40 07 AE E0 4B E7 1B 4E CA A4 3C 31 A5 5A 4D 0E A0 2D 3C E9 D5 83 28 D5 2E 9A 7F 2E 53 B4 2B 2B C9 E6 15 7C AA AF C8 46 78 CE ED AB 8D 9B 86 7B 5B 18 41 91 A6 71 24 31 B4 8F 80 CF 97 55 24 ED C0 CF 39 E0 01 ED 8E 2A D9 1C 60 12 3E 94 9D AE D2 33 BA 72 71 ED FA DC E4 92 2D 6A 6C 22 49 A7 59 43 03 AD BE EB 38 5D E4 8D 59 53 08 14 83 B0 60 A6 46 17 18 DC 70 06 6B 57 4F D1 AD 60 06 EA CA E8 C9 F6 88 7F E3 E8 95 96 46 27 EE BA B9 C8 E9 DB 04 1C 2F BE 74 A0 B3 86 D9 22 58 D5 8F 95 10 89 0B B9 76 0A 3B 65 89 27 A0 C9 EA 70 33 9C 52 34 02 3C B4 4E D1 96 E1 B6 81 F3 13 B4 06 24 8C 96 00 60 7E B9 E3 0B E1 D4 A5 05 F3 2B C9 A4 D9 DE 3A 4D 79 A7 D9 9B 88 D9 C2 3F 96 B2 10 85 8E 70 59 78 DC 3E F0 1E A4 64 F5 AE 43 C3 F6 3A 6C FA 9E B3 63 3C 51 C9 69 3C 44 C7 21 1B 0F 94 E3 1F 2B 0C 15 CA B8 E4 11 50 4B E3 3D 56 14 D4 54 0B 73 24 7A 9D DD 9A 4A 63 F9 84 71 95 29 DF 04 8F 30 8E 47 61 DF 24 E6 5E 4B 71 A9 E9 B1 EA 57 57 12 35 C4 D7 1F 64 7D A1 54 34 60 2B 0C 80 06 4E 7D 7D 07 A5 36 A5 ED 54 53 F2 FB C8 94 EF 2B 2D D6 BF 71 AF 7F E3 8B F4 BD 96 3B 23 6B 2C 28 EC AB 21 42 BB 86 E3 8E 37 1E D8 19 EF 8C E0 67 00 AE B6 D3 C3 7A 4D A4 02 11 63 04 AA 3A 34 D1 2B 37 E6 46 4F 39 3C FA FA 60 02 AA EB B7 E6 5B 73 E9 63 FF D9";
            } else if (rand == 9) {
                c.setLIEDETECT("격노의불");
                packet = "BE 16 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F6 DD 4A 5B B4 D1 A7 29 11 37 D2 5B BA C7 14 6C CC 82 5D 8C 71 BC 6C 20 64 63 71 29 DB 95 26 B1 21 F8 87 E1 3B FD 5D 74 FB 2D 6E 09 6F CB 18 63 40 58 23 12 E1 78 27 08 FC 8C 8C 12 48 CE DC E7 9D 1D 6F 45 B4 F1 25 9C BA 4E A1 3C AD 14 8B 22 CA B6 F9 55 2B 95 2A 18 F2 03 0C A1 19 E4 91 91 F2 96 07 CA 3E 21 78 4F C1 5E 1D B1 FE CC D2 FC 37 2C BA FD EA 85 B1 48 AE A5 90 F2 58 16 DB BC 9E 02 F7 5C 12 40 19 C3 62 A2 93 5C AD 1A 41 26 27 C5 9D 5B 56 F0 E6 BF 6D A9 69 7E 31 98 5D EF 28 FA 5A DC 2B 79 03 EF 29 28 00 1B 48 2B C3 A9 3E EC 3A 7A AE 81 7E B2 78 53 4F BD 82 4B 9B B7 BD 8C 49 1B 4E 59 C9 77 1B 80 66 55 3B 14 1E 33 8C 00 3B 9E BE 1B AB 68 1A 9F 87 FC 24 FA 15 E7 83 2C 85 FD DC 5E 7C 9A AC 77 09 BB 0A 54 EC 51 D8 82 54 10 A7 0D 86 20 1F BD 5D 77 C3 F9 35 A5 F8 45 15 DD B5 FB 45 15 B3 CC A2 07 85 1D 64 8F 7E 49 04 AE 47 25 BD 7A 76 AB E5 4A 2D 2E 9F D7 E6 13 85 D4 5A DD E8 7A 66 B1 A8 46 B6 B3 C7 6F 2C 92 DC 5B E2 46 86 CD C3 4E 0A 34 6E 47 97 90 58 61 94 B2 82 09 56 00 64 B0 AC 4F 19 D9 5D D9 59 69 DA E5 90 96 E6 F3 42 CC EC F2 3A 83 24 21 76 CC A4 F4 DC CB CE 76 9C 6C 38 C1 23 3B FA 56 AA 97 BA 14 57 ED E6 36 22 0D 26 C8 D9 89 60 B9 6D A0 0C B7 FC 04 1E 78 EB C5 72 BF 14 61 4F F8 40 F5 42 C6 18 65 69 23 9B 64 6C 43 4B 89 11 37 36 08 DD F2 98 F8 20 80 40 EB 85 23 36 AC C8 5D D9 D4 C5 25 BE B1 63 6D 77 63 72 F1 5A 5D 93 23 95 8D A2 7B 84 31 B2 8C 13 86 53 F7 58 30 E7 08 31 C5 53 D5 3C 45 A6 69 F3 CA 6E 75 4D 39 A4 86 58 D6 3B 70 54 CD 1B 13 B5 B3 97 18 C8 6E B8 50 A0 92 72 29 DE 1E D0 57 40 B4 3A 6C 57 82 5B 78 6E A4 9E DE 11 94 36 F0 BE ED B1 9E 49 70 09 6E 5B AF D4 0A B1 3C 96 F0 DC 18 20 FD EA F9 81 A5 B7 B7 F2 89 85 83 09 1A 42 87 E6 24 EF 52 D8 CB 7C CA 40 C9 CD 16 BE DA 89 3D 6C 53 BE D7 34 A9 AD A5 B4 B4 9A 4B E9 37 AB 31 B3 89 AE 8C 04 BE 43 E4 2B A8 65 20 B2 82 3F 80 60 54 F2 6B 76 11 59 C6 2E 4E AA 55 15 64 69 8D 85 C2 13 B4 E7 2C 55 00 1C AE 48 E0 11 DB 06 B5 AD C9 F2 63 53 14 91 E1 17 89 18 31 1E C4 E4 E4 8E E7 27 EA 6A 08 B7 8B 88 14 F9 7B E3 8B F7 8A 2E 18 94 07 18 E0 8F 9F 25 4F CC 70 78 38 EA 6A 75 4C 2C CC F9 75 B8 EE B4 E9 65 8F 49 D6 24 02 02 D2 C6 B6 ED 0C A8 08 E5 57 71 52 5B AF DC 27 EE F5 C9 5C D5 B8 D5 74 F8 67 6B 8D 4E 5B 9D 1D 4C F1 86 7B DB C8 4C 4C EA 03 08 F6 97 75 42 40 07 80 A7 B8 39 35 AD A7 40 F6 B1 24 6D 1D C3 B8 3B 5E 69 56 15 69 32 37 EF 6F 2F 03 EF 33 2F 00 72 49 C1 07 75 64 F8 9F 46 B4 D6 AC 24 8F 5E 8A 11 64 25 44 8F 13 10 15 4B A9 2E C4 8C 06 20 6D 18 19 50 5B 0E 03 12 B2 E5 24 AE 98 29 25 B9 A3 6F 73 0C F6 70 5F 69 D7 F1 DE DA 87 67 9E 6B 7D 92 19 C0 52 BF C0 30 48 3B 78 5E 7E 5C 7B 1B 13 5C AE 99 6A D2 5C 6D 4B 58 8A A2 B9 94 B3 10 76 81 9D DC 93 92 46 32 49 C0 C6 49 C0 F1 3B 6D 33 C4 3E 0D F0 F6 B1 6B A6 D8 69 5A B6 81 77 2A 4D 15 FD FD CC 62 28 C7 6D D1 48 C0 6F CE CE DF 79 47 DE E0 0D EF 02 78 64 EA D7 32 F8 A5 FC 60 75 9D 59 E2 31 1F 26 49 12 28 43 96 04 37 DD 72 07 DE 09 88 F0 40 E9 90 43 8D A4 F4 1D AC AD FD 7D E7 A6 69 A2 61 61 0B 89 60 94 14 55 41 0A 08 E1 D8 09 C3 20 05 8E 0A 91 81 B8 8E 07 DD C9 AB 88 AC B9 0D 21 71 92 46 40 C8 E4 9C 71 D8 70 07 D3 9C D7 2D E3 9F 1A 5B 78 4F 4F 89 4D AC D7 77 D7 52 79 76 D6 F1 F0 59 B8 F9 B2 41 E9 91 8E 0E 4E 06 3A E3 13 4C F8 89 0D E8 FE CC D7 6C F5 7D 0B 51 F2 7C DD B3 46 4B DC 46 80 92 10 85 5C 33 61 87 CA 83 B8 52 1B 18 98 4A 2D F2 DF FA FF 00 3F 21 5A DB 9D D0 80 B2 5D F9 D3 3D D8 90 60 DB 91 19 58 F2 BC A2 FC A3 20 E4 7D F2 7A F6 14 8F 2C 97 1B C2 85 10 BA C5 84 9A DC F2 AC DF 36 72 C3 AA F1 B7 00 A9 19 21 B2 05 60 5E F8 EE C3 48 F1 5A E8 BA AD AD C5 9C 97 01 7E CD 3B BA 98 A4 5D C5 72 7E 6C 27 39 3F 40 33 CE 16 B7 2F A5 95 04 CB 6D 10 B8 BE 8E DE 49 AD 95 F3 B4 BF 20 06 38 0A BC 90 01 27 24 6E C7 46 35 77 E6 D5 75 D0 7B 4A C5 97 4B 89 16 3D B3 2C 65 48 2E 76 67 76 18 67 1C F0 08 0C 3B F5 07 A8 AA 53 DF DF 5A 69 D7 37 D3 5A 6E 54 89 5D 2D C0 01 D3 39 2D BC 86 61 F2 82 32 14 31 F9 49 5D C5 82 89 D4 AC 2C 86 EF 72 14 60 CB 33 48 AA 85 E4 62 3C B1 8C 13 82 54 0C AF 39 4E AD 9C 64 DE 78 97 4E D3 AD A3 5B BD 73 4F B4 74 D9 23 43 F6 84 79 0C 58 1D 0B 36 58 13 CE EC 64 AF 00 6E 20 D3 5A A1 C6 F7 FF 00 80 73 37 3F 16 3C 39 73 A7 5D 5C DB CA A2 EC 00 2C E1 B9 8A 47 26 45 27 05 90 0D AA 32 41 C8 6C 91 EE 31 57 7E 1D F8 93 5A D7 F4 38 35 0D 44 C6 EB 25 D4 96 E4 24 2E 0E 02 EE 0E 08 04 75 CA 9C E1 71 8C 10 54 86 F2 E9 24 D3 F4 E6 D6 EE 2D FC 41 2C BA 7D E5 C3 2C B1 69 76 0C 5C C4 58 9D 8D 3C 8A A2 30 7E 50 76 96 07 3C 82 07 3D AF 84 EF 7C 51 A7 68 B6 89 37 86 EC E3 F0 BC 31 89 E1 33 CE 1E 76 CB 06 43 9D DF 7B 71 0D F7 07 4E 00 E2 9D 39 25 AB 5D 11 73 82 5A 45 6B 73 D3 C4 96 E6 31 7D 71 1A 41 24 31 1D ED 29 5D D0 29 01 99 59 81 20 74 52 70 71 C0 3C F1 5C CE 87 35 D6 AF 1B EA FA 83 5C 5B C1 75 3A 25 94 0B 98 DD E3 D8 CA 58 95 CB 15 3E 63 B8 52 48 4D A0 83 81 BA B3 FC 57 AA C9 79 A6 5A AD C5 8C D0 E8 A5 12 F3 50 29 16 EF 35 4C AA 16 25 6C A8 0C 58 E4 F2 0E 30 41 EA 0F 5F 04 30 D8 4B 70 D6 E2 28 EC E2 8C 6E 82 DE 2C B6 F0 39 C8 00 93 84 11 85 51 DB 8C 1F 97 18 5E 53 A8 E2 9D AD FD 7D DF A9 CF EF 49 DB B7 F5 FD 79 8D B6 8E F9 E1 F3 6D EF ED DA 19 49 74 2E A6 70 54 F4 2A C0 AF 04 61 B1 83 82 C4 03 80 30 55 8B 75 91 56 48 92 48 80 89 CA E0 21 38 07 E6 03 EF 1C 60 30 1D BA 70 00 20 51 56 D2 93 BA 6F EF 34 53 6B A1 14 E6 EA 21 2B C5 30 CB BA A9 49 47 11 B3 6C 50 51 8E 01 51 F3 1C 60 96 27 00 8E 95 C8 6B BE 1B B4 D2 6C 97 5C D4 0E B1 AA EA 3E 5A C1 7B 25 8C 61 E5 B9 42 0E E5 08 30 A8 9D C8 18 18 07 39 27 35 D8 D8 AC 93 04 BA BA 86 3F 37 66 21 7C 29 74 8D B6 E5 4B 03 82 49 50 4E DC 2F 40 33 B7 27 0A 7F 11 E9 B6 50 49 6D 6D A8 D9 1B DB 39 9A 3F 22 CE 33 78 D0 C2 1F 04 3C 50 A8 64 01 40 1D 00 56 0A 0B 1E FA 73 35 AC 77 08 C9 B5 A2 3C DE E3 57 F1 16 BB 68 74 EB 1F 0C D9 78 7D 27 9B EC 8D 2C F1 97 BA 27 72 B8 18 38 20 E0 C6 4E FF 00 BD 9C 8F 6A DF 0E AD FF 00 B2 3C 4D E2 1F 0E F8 81 26 BC 6B 65 10 DB A3 2E F2 A0 C8 72 D1 A1 E7 90 FB FE 5E 71 93 C9 AE AD E5 D6 B5 3D 4C 6B 0B 3F D9 6D 03 13 61 3E A3 77 15 A9 91 70 17 72 21 8A 5E A0 64 86 54 39 60 46 33 81 25 97 85 67 96 6B AB E8 35 AD 4E EB 51 BB 8C C5 35 EE 9B 67 0D 8C 72 47 BB 3C B3 A9 39 CE 7E 78 CF 40 38 CF 27 55 2D 35 46 9C CF 97 FA FC CD 8F 05 5C 5C 47 A7 5C 5A 84 79 64 B2 99 A2 11 15 0A 70 EC BC 92 D8 2A 06 18 91 D7 1D 89 00 55 4F 88 90 DB CF 0E 97 1C EB 6C DA 6D DE B1 6B 15 C1 25 79 3F 3A C8 49 DB 94 21 55 57 70 6C E0 11 C0 07 38 9A 36 99 7F A0 F8 82 54 6B BD 42 DA 6B C8 24 94 BB F9 73 DC C8 57 38 57 90 C2 CC FC A1 C6 03 70 46 DE BC A7 C4 01 77 79 AC E9 5E 1A D3 F5 FB AB BD 48 CF F6 A6 33 C9 0C 4B 69 B1 49 0E 4C 71 A9 07 69 66 EB 90 17 A1 CA D2 92 4A 57 FE BF AD 0C D5 9C AF D1 EB F7 9A E7 56 D6 EE BE 21 43 A5 E9 3A D4 B2 E9 96 A6 3F ED 18 A3 B5 59 92 DB 00 0F 28 CC 7E 77 66 60 41 3C 15 C9 3F C2 DB 7B BB 65 58 ED 63 B5 5D D0 32 A1 48 D4 84 0D B5 7E 50 C0 2F CB 8E 87 A7 19 19 03 A5 72 FA 47 85 BF B0 ED 6C EC 74 7D 4F 54 82 07 96 4F 35 59 6D 55 E3 18 3B 9C EE 84 B3 FC C1 17 A9 E0 A9 07 00 55 DB 0B 0B E7 B4 FE D0 1A DE A7 39 75 12 AA 24 76 43 CF 5C 6E 55 C8 42 0F 07 6E 77 00 48 24 10 08 AC DD 98 DD CD 6B EB E6 B0 B6 BC B8 66 8A 5F 22 33 28 85 7E 56 03 1F 2E E6 27 00 12 1B E6 38 00 75 C6 D2 4C F0 C9 6B 71 96 86 64 98 48 12 61 89 37 8D A4 7C AC 39 E0 1D B9 18 E0 90 4F 5C D6 27 FC 23 D2 DD A9 82 E3 58 BD BB B4 DA D2 23 4F 0D B4 9B 8C 81 D5 B9 31 9E 02 B7 03 68 18 62 32 C0 ED 5E 67 C1 DA 7E A4 9E 16 8E 13 AF C9 69 1D 85 E4 B1 5F 42 D1 C3 B6 D0 21 66 3B 59 A3 6D CD BB 69 DC 5B 68 0C 7B A8 A5 A6 C6 33 93 8C D4 3B DF F0 B1 DF DA 1C 2F CE CE F3 39 26 46 28 EA A5 97 08 4A AB 13 B5 49 19 03 3C F2 46 72 49 C7 F1 3C F6 70 68 77 52 EA 8F 62 E2 D7 13 33 C8 8D 88 14 C9 84 62 8A 4B 30 00 1C 80 46 FD A4 70 18 81 8D A0 2F 88 35 F5 BB D4 E3 F1 0D E2 69 DE 63 26 9F F2 C1 BA 74 5D CA 59 BF 73 85 CB 05 C7 CB 90 37 0C 73 93 7A C2 DB 54 7D 2A CD DB 5E D6 9A 59 02 22 FE EA D8 87 F9 37 16 DC 22 70 06 03 00 59 B0 58 01 B8 EE 56 2A 5C AE 36 BF E0 55 39 3A 8B 99 6C 79 E6 8D F1 0E C9 2C AE F4 3B 5D 3D F5 A9 25 60 23 8C 26 CB 69 15 88 0E 18 C8 41 50 40 E0 B2 F0 4F B9 15 0E 89 E0 9F 15 59 69 7A B1 B3 D4 A4 F0 D5 95 CC BE 7B 26 08 F2 23 52 C4 07 99 8A 32 85 5E E8 0E 47 52 39 15 EA 97 1A 3C B6 E1 6F 7F B7 F5 64 21 71 29 5F B3 46 1C 31 50 5D C1 8C 0C AA AA F3 D4 2A E0 72 48 3C BB 69 17 09 AD B0 D1 2F F5 1B 98 65 40 24 BB B6 58 46 18 10 D8 2D B0 46 47 00 91 FC 5C 82 79 35 2E 09 6F FD 7F 5A 17 76 F5 47 3D 6D F0 C2 E8 68 96 BA A7 87 BC 46 9A 95 FD 8C FE 7D B2 99 84 96 EC E0 EE 75 46 07 00 96 03 AE 39 1C F5 CD 58 7D 2B C6 7E 27 F1 3E 95 AB F8 A7 4B B2 B4 D3 B4 A6 79 1C DB 4D 16 0E DC B1 07 32 1E A5 02 9C 90 07 7E F5 2C 5F 0A 2D CE B5 3D ED 8E A9 7F A6 4D 00 32 3B C1 20 69 23 66 04 ED 02 38 D5 54 ED 3C AA 93 80 47 62 33 91 75 6D E2 CB 5F 0A 6A 37 E7 C5 F7 53 DA 59 64 34 37 30 BB F9 CA 48 0C AC C3 71 FE 20 30 7B 13 C8 00 9A C5 CD 41 DD AD B5 B5 F4 D3 41 B5 75 A7 5D 0A 9A C1 97 C6 BF 18 4D BB 4A CD 67 A7 33 17 78 2D C9 2B 14 64 BB 01 1F CC 4B 64 ED E8 72 7F 87 F8 6B DB AE 6E 53 CC 86 DA 7B 81 04 D3 CA 8F 0A 32 B0 38 52 A4 A1 65 6C 13 C1 1D 71 C8 04 30 E1 BC 6B E1 2F 9B A5 4D 27 88 2F 22 0F 1E A3 31 B5 13 31 3B 86 08 2C DE E0 92 33 FE ED 7A 9F 89 5F 4E B3 D0 6F 61 92 4F 36 49 5C B2 47 24 E5 D8 48 70 7E 5D C7 20 0D C0 E0 70 07 41 CD 3A 72 E4 A5 7E BB BF 57 AF E4 2F 8A 6D AF 97 A1 7B EC D7 2F 05 CD BD E5 C3 C5 6B 25 C1 31 4A B7 05 26 0A 59 48 4C 8E C5 8B A8 C1 04 2E C1 F7 89 23 9B F1 1E 81 E1 C8 E4 BC D7 2F 74 28 AD AE 52 3E 6F AE 21 59 A1 04 AB 12 ED 0A BF CE 41 E0 9D A4 E4 83 C8 04 8D 8F 0D C5 78 74 EB 49 26 D4 E6 92 40 81 DA D5 99 08 D8 C5 B0 49 2A 5F A7 23 9C 7C A0 74 CD 4B 73 75 31 09 12 D8 EA 9F 6A 92 24 02 56 8D 1D 54 8C FD E0 18 20 3C 90 C5 71 91 D0 F0 A4 74 C9 B9 47 CF EE B5 CA 4F AA FE BF AF B8 F0 5F B1 6A BE 21 B2 B8 B6 B3 BA B9 D4 04 5B 58 CE 91 A5 AD A6 33 F3 65 DF 69 3C 90 00 20 74 E9 C5 7A CE AC 90 C7 A5 41 6B 6F 32 DE 83 1C 70 AC F2 4A F7 32 B4 D9 E4 6F 39 54 E1 7A 03 96 DD D1 42 FC D8 49 F0 D3 4C D3 2D 1E FE EE C6 E1 A3 84 3B 47 6D 7D 7C 25 44 6D C3 1F 2C 61 7E F6 3F BC 78 3C 8C F1 5D 3D 8E AF 0E EB 75 6B EB D4 BC B9 8A 35 16 D6 C4 3A AA 82 55 0F EF 0B 00 4F CD 96 04 67 6F CD C8 A4 AD CB 63 49 3D 53 5B 21 35 F2 B7 33 78 67 40 47 05 6E 26 8A 67 81 D0 80 D0 C4 A5 9D 64 53 CE 1B E5 C2 95 C6 54 E4 FA 6F ED 7D 33 4E 77 51 7B 7E 90 06 91 CB B1 33 B0 50 49 08 00 05 89 75 1F 2F 03 E6 E0 E0 05 AE 7E 6F 0F C5 A6 F8 8E 7D 54 DA C9 16 99 65 A7 AC 71 CA 5D 64 64 93 3C CA 8A C4 E0 22 0E 72 07 4C 2A B5 68 5C CC B6 DE 2A B0 D3 2E 6D D1 A1 BA 69 24 B6 26 DE 37 56 20 F9 AE 4B 64 15 60 E1 08 C2 91 C6 49 66 39 4C E2 B5 73 96 CD FE 5F F0 FA 1C CA 31 BB 6D 9B B6 16 E6 D2 D5 6D F6 46 A9 1F 09 E5 93 B7 18 04 E1 4E 76 8C E4 05 04 80 00 C1 EC 0A 65 AD C5 C4 D0 82 45 BB C8 3F D6 2A B3 2F 96 4F CC 14 82 33 90 A5 7A E0 9E B8 19 C5 14 F9 92 D2 CF EE 28 A1 A8 49 73 15 BC F6 36 96 32 DF CE 2D C4 92 C9 F6 A4 B6 79 7F 87 EF 26 18 48 42 9C 1D AA BC 63 72 E3 8B 77 96 D1 26 93 77 09 86 46 8D 84 8D B6 28 91 99 8B 12 DF 2A E3 69 39 3F C4 39 3D 73 C9 33 47 A7 5A C3 22 49 14 5B 24 50 A3 7A B1 05 C2 A9 55 0E 7F 8C 00 C7 01 B3 CF 3D 79 AC 5B 69 FE DD A7 E9 8F 75 0C 32 BE A5 33 45 3B 14 C3 6C 55 96 44 00 8C 11 B4 A8 C1 EA 39 3D 79 AB 8B 6D A8 F5 DF EE D4 1C E3 16 95 FF 00 AF E9 FF 00 5A 9A 72 69 56 57 12 5A CB 75 65 6E 5D 22 F2 84 42 15 91 17 A1 1F 31 5C 80 B8 20 74 1F 31 C8 CE 31 23 5A C9 7F A4 4B 67 A8 AC 79 9A 37 86 5F 2C 7C AC A7 2B 90 0E 71 91 CE 0E 71 9C 64 E3 27 9B 9F 57 B9 93 C5 D2 F8 66 11 1D A5 A4 9B 89 9A DD 76 CA 0B 27 98 C4 1E 80 96 27 9C 67 92 7A F3 55 E2 56 BA D2 74 EB EB A9 64 B9 92 F3 50 58 A4 8E E0 F9 91 A8 F3 36 9D A8 D9 55 24 21 E4 0E 37 B6 30 30 06 91 A6 DB 5A DB FA FF 00 80 53 7C BB 96 7C 41 3D B5 F6 A9 A4 5D E9 73 2D F5 D5 B4 E0 B4 36 84 48 C6 32 CA 09 24 7C AA 06 73 F3 10 31 9E A7 8A A3 FF 00 08 DB 45 E3 BD 3F 59 B1 D0 1E CA DE 16 B9 B9 BB 99 E5 47 92 E2 59 57 68 50 03 31 18 27 3C 90 A3 2D D3 BF 69 68 8B 2E 93 04 6C 64 DA F0 2A 92 25 6D D8 2B FD FC EE CF BE 73 DF 39 AA B6 0B FD AD A5 DA DF 5C B3 07 B9 B3 4C AC 4C 53 CB DE B9 6D 8C 3E 71 9C 8F E2 E3 6A 91 82 33 51 CE 9A 49 7F 5F D7 61 73 5D 5D 13 10 BA 7B 4B 33 48 C2 0F 9A 49 49 40 CD 23 B1 1B 40 DB C9 20 0D A0 60 92 0A 81 92 2A 4B EF B1 A4 0C D7 61 44 72 B4 70 B1 C1 F9 F7 38 55 53 8E A0 B3 63 07 8E 4E 78 26 AA EB D2 9D 37 C3 DA C6 A1 68 A9 15 D2 5A 4B 30 90 20 C9 75 8C ED 27 D7 18 1D 6A D9 B1 88 40 90 C2 5E 08 E3 64 65 58 58 A8 50 B8 C2 81 D0 2E 06 31 D3 AD 25 6D AE 34 9A 5C DD 06 EE 58 63 17 B3 3C D0 20 8C 99 22 91 83 05 C9 CE 5B AE 36 F2 32 0E 30 4E 72 00 C7 95 5D DB 4B 77 75 E2 9B 0B 61 14 DA 65 AD CC BA AD EC DB CA 34 AC 63 2F 1D BB 00 73 80 E1 B7 0E 0F CA 7E E3 01 9F 52 85 45 C2 5F DA 39 90 46 25 68 F2 B2 B8 7C 32 2B 1C 36 72 0E 5C E3 04 60 63 18 C5 71 97 3E 1C D3 34 3B 4F 18 5A 59 5B ED B6 93 49 8E E3 CA 90 EF 08 E0 4E 01 19 C9 EA 81 B9 27 07 F0 C4 CF 54 72 62 A9 39 25 6D 95 EF F7 7F C3 5F FE 1C EA 7C 3B 2C 92 E8 16 37 3F 65 86 08 E6 B7 85 D2 0B 50 15 14 32 86 62 17 03 6F 2C DC 64 F0 A0 F5 24 52 AF 97 65 66 90 47 6B 7B 29 43 34 8A 88 E8 92 3F CE 43 1C 6E 5D D9 DE 5B 27 39 E0 93 B8 8C B3 C3 61 64 F0 96 8D 0C 88 AF 1B E9 D1 06 56 19 04 79 6A 30 7D B9 AD 59 E0 13 85 FD E4 B1 B2 12 55 A3 72 30 4A 95 C9 1D 1B A9 38 60 46 70 71 90 2A 93 D2 C8 E8 A7 27 C8 9F 92 FC 8C 0B 5F 0A DA 47 F3 DC 5B C3 F6 F9 57 73 CE 8A EE A8 F8 5D E7 12 33 02 C5 8B 10 48 FA 83 83 9B 97 56 CD 6B 75 6F 34 76 B0 9B 6B 66 1E 5A A2 C8 CD 1E 40 8C ED 0B 9C 0D AC 7E 50 B8 F9 72 48 CE 57 51 3F 78 03 1C E5 58 E3 04 8E 84 8F C6 B9 BF 13 EB 33 E8 3A 66 A7 73 69 14 4D 25 94 10 4C 9E 66 E3 BD 9D DA 33 BF 04 16 F9 54 75 EF F4 14 A9 AF 68 AF 1E BF AE 86 D0 A7 2A B3 E5 5B FF 00 4B F5 36 61 D5 2C EF 64 FB 3C 4F 27 9A C3 2D 1B AB 45 22 A9 19 CE D6 C3 7E 43 20 9A E1 BE 21 E8 F7 FF 00 D8 D7 16 1A 24 32 4B 16 B1 22 99 B8 51 14 25 48 62 C6 42 C0 28 6E 80 1E 0F 4C 8E 01 EC 75 8D 12 C3 51 65 9E EA 26 79 37 46 80 F9 8C 00 5D FD 00 CE 06 77 10 48 19 3C 7A 0C 53 D5 74 F6 D3 21 82 EA CA FE FA 1C 5C 5B C0 22 F3 CB C6 15 E5 48 C9 DA F9 E7 0C 71 D8 10 38 AC F9 5D 49 72 5B FA 7F F0 C4 C5 3B AB 18 3E 1A F0 94 ED 6B A7 C5 70 AD 06 9F A7 EE 8E 38 1C 32 BC A4 39 3B CA 90 00 DC 4E 73 80 48 C0 23 35 37 89 4C D3 DC 5A 69 33 19 24 9D A4 13 4F 22 A9 21 FE 40 37 46 80 B1 55 C6 E0 17 AE 41 EA 4E E2 CD 47 5D BD F0 DE B3 77 6B 6B E4 4A 8E C2 56 32 C2 AA 59 C8 19 63 B0 2E 49 E3 93 E8 2B 3F 45 BF 96 4D 5C 5E ED 45 92 33 6F 0A 2F 24 2A 34 B1 C4 40 C9 27 EE B1 E4 9E F5 94 AA 46 4F 95 77 BB F9 12 FD C8 B6 CE F0 21 9E CA 48 9E DA E0 40 F1 A5 BB 5A 61 55 A3 07 86 3B C3 60 8D AC 33 B4 9C 6D 38 C9 E2 97 51 B5 83 65 C5 D8 B5 6F B4 B4 3E 49 B9 B6 DA B3 AC 7C 9E 18 E3 80 49 38 C9 1E DC D3 34 49 13 52 D0 74 AB F9 AD A0 59 A5 85 2E B0 89 80 92 48 99 62 B9 E8 4E F6 E7 AF 27 D4 D4 7A BD C9 5B BF B2 98 D0 A4 9A 7D D4 85 8E 43 0D A6 21 80 41 E0 1D E7 3F 41 E9 5D 4E EB D4 84 D2 8F 37 65 7F 96 E6 76 98 D6 DA 83 C5 3D A5 AC F7 52 C4 18 AC 9A 8D C9 3B 41 61 C8 03 70 07 23 00 E0 1E 0E 09 E6 9F 34 36 E6 F6 D2 F7 51 D5 23 80 05 66 4B 72 59 1C 09 09 EE ED B8 63 38 CE 17 18 E8 B8 E2 C5 B7 84 B4 DB 6C 30 7B 96 95 71 E5 CA 66 2A D1 E0 30 05 76 E0 7F CB 47 FF 00 BE 8F A9 AA F7 6B 6D A6 6A 12 8B 6B 1B 60 EB 25 91 12 15 25 F3 2C E6 26 E7 39 FB AA 3F 12 73 9C 9A 5C DC B6 35 72 BB E5 EF A7 E6 6D 5C 4B 3C 50 99 6C AD 8D CB BC 98 28 F2 94 C7 F0 EE 19 07 E5 04 02 71 DB 24 06 38 0D C5 78 65 46 BA FF 00 F0 94 4B 04 8E B0 3C 76 B6 F6 A2 73 3B C7 12 0E 4B 36 F1 BC EF 61 27 CE 19 BE 40 40 24 A9 AE B1 E7 37 BA 9C FA 74 C8 86 0D B2 02 31 9D C3 64 5C 10 78 23 F7 AD C1 04 70 3D 29 9A 7E 93 6B A3 EA D7 02 C8 18 A1 BD 5F 35 AD D7 0B 14 6C 81 57 28 A0 00 32 0F 3D 73 81 59 5D CA 69 AD 93 7F 7F FC 0F D4 C9 A7 1E 56 FF 00 AD CD 2B 67 9C A3 25 C2 11 24 78 53 20 50 AB 29 DA 09 65 1B 98 85 C9 23 0D CF 07 A8 C1 25 2D BD BC 56 B1 08 E2 53 80 00 2C CC 59 9B 00 00 59 8E 4B 1C 01 C9 24 F1 45 6C 5B 3F FF D9";
            } else if (rand == 10) {
                c.setLIEDETECT("길잃은도요새");
                packet = "D8 16 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F6 3B 55 8A 0B 7F B1 9B 2F B4 4B 2C BE 4D E8 F2 D4 09 33 BB 32 B0 51 E5 E5 87 CE C0 90 76 B0 CF 3B 54 DD B6 16 F0 E9 F6 5F 61 84 5D 5A E5 7E CE 60 11 84 8E 32 3E 52 B8 C0 D8 AA 70 36 E4 E3 D6 B8 FF 00 13 EB 7E 35 D0 75 B8 EF 74 DD 1D B5 6D 08 44 37 C6 BF EB 77 33 13 D1 46 E5 20 60 7D D6 18 1C F2 4E 35 FC 21 E2 CB 4F 14 68 A2 EF 4B B4 78 62 B6 8F CB 7B 5D AA 0A C8 07 08 A7 70 18 00 77 00 1D CB C8 C1 00 8C B9 AE BA 97 24 D2 FE BF AB 9A C9 72 2F FF 00 7F 62 D2 98 8C 91 AB 4A A4 A8 65 07 76 50 3A ED 65 20 80 59 7A 82 70 72 A2 AE 79 66 54 21 CB 8F DE 06 01 82 9C 60 F1 8E 0F 07 1F 5E 7B 1E 9C 2F 85 FC 70 B7 FE 30 BB D0 75 BB 08 AD 75 C8 81 89 26 88 E5 26 55 25 B6 81 93 B4 E0 E7 19 39 E7 9E 00 AE BD DA F2 6B D7 8E 29 A5 8D 11 C7 CF F6 70 10 28 F2 98 A9 2C 72 CC C0 B8 0C 9F 28 CB 03 F3 28 C9 15 CC AF D0 9B 34 DA 7D 09 75 19 66 B6 B1 9A 7B 78 5A 6B 90 36 C2 B1 C4 1D 81 6C 01 90 59 72 33 82 7E 65 E0 75 18 CD 2F EE F5 01 B6 4B 69 96 25 09 22 3B FC 81 89 19 1C 67 70 2B C7 DE 03 07 04 72 38 E0 07 C5 2D 47 50 D4 AF 2D B4 2F 08 DD 6A 51 D9 21 37 0E 2E 02 ED 61 D4 0C 2B 03 C8 20 00 49 6C 70 2B 67 42 F1 25 A7 8C 3C 21 73 77 A6 85 87 51 3B 52 50 D0 67 CB BA C2 84 72 B8 6F 94 30 52 18 83 80 BC FD D3 88 4D 4E 3A 21 27 1F 85 9B F0 DC 47 1A 8D AB 0D 9C 29 03 14 49 10 29 68 D4 29 56 03 20 AA A8 62 0A B0 04 13 D8 72 C4 37 B6 E2 F2 48 CC 57 21 D4 CA 51 A4 88 93 B4 11 BB 6F F1 60 B0 3B 41 FB DB 09 5C A8 06 BC E3 E1 C7 8D F5 9F 10 CF 7D A0 EA 71 43 35 EC 21 A5 8A F3 C8 06 35 70 DF C6 A9 85 38 3F 30 20 AE 71 8C E4 E6 BD 36 7B A8 EC A2 53 71 79 1C 49 1A 9D F2 DC 0D A1 B6 80 CC 77 70 A3 E5 0C 7D 3A 9E 8A 45 5E EA EB A9 0A 51 77 72 FC 7B FF 00 5F D2 20 D5 75 35 D2 F4 BB ED 52 E2 46 16 76 B1 19 0A 2C 58 93 28 5B 70 CB 70 43 60 01 C0 EE 72 72 31 E5 B0 78 83 E2 16 B7 A4 B7 8B 34 98 F4 6B 0B 18 91 D9 6C CA 66 5B 98 62 27 82 48 E4 2E 58 70 C9 D4 E0 0C 8A F4 7D 5A DE 4D 5B 42 9E D3 50 D2 64 BB B6 BD 40 AF 6F 0C D1 97 8C 14 53 9C B6 D5 E1 B7 72 18 9C 85 23 AE 17 C4 E7 F0 F5 D6 86 B7 5A 25 A7 8A E6 B4 D2 6E 1A 35 B8 96 EA CF EC 9B 99 89 01 09 66 DF B4 02 49 E8 39 27 07 04 8C A5 CD AD BB 69 EB E6 67 56 56 B7 E3 D3 FA FF 00 33 D8 7C 11 E2 49 BC 63 E1 31 A9 48 E9 04 D2 33 C4 C2 04 C1 85 80 03 F8 8B 02 7F 88 12 31 C8 18 38 C9 E8 3C D6 B8 7B AB 75 12 C2 63 01 04 EA 50 F2 57 3F 28 C9 C1 19 1F 79 47 51 D4 57 8C 69 1E 37 B3 F0 95 84 16 DE 1D F0 F5 D6 B3 69 6B BE 27 BD 85 D9 62 32 3E C2 C3 3E 48 24 92 80 F3 EB C6 46 31 EB 7A 7C F3 6A 5A 04 57 D2 69 B0 45 75 71 1A DC 0B 59 24 0C A2 40 01 4D D2 00 73 C8 5F 98 0C 8E 38 E2 B6 4D 49 DE 2F B7 F5 AF F5 F2 1D 2A 97 8A 8B DC 87 55 D4 B4 AF 0D BF F6 8E A9 75 14 11 CA C6 34 66 87 73 8C E0 ED 52 A3 3B 72 19 8E 73 CB 75 1C 0A E5 35 6F 19 EA 1A A4 6D 77 E1 9B 08 E2 B6 8C 18 9F 5A D4 76 C3 14 6A CC 06 63 2E 46 46 42 E7 AF 20 02 BD 2B A3 F1 CC 26 E7 C2 1A 8C 31 5A 25 DD C7 92 CF 1C 2D 82 78 C0 2E 07 72 A1 B3 F5 C7 7C 57 9A 69 5A F6 AB 79 A6 E9 FE 16 B2 BC B7 D0 55 02 A3 B6 E9 1E E6 56 2C 4B 15 D8 BF 29 EF B4 ED 27 D7 06 B2 EB CA 6D 7B 6A 8E F7 46 F1 1E 9F 63 61 1F F6 AF 89 34 BB ED 43 23 CD 9A 0B B8 23 46 DC C0 1E 37 28 3B 42 8E 48 CE 3A 64 F1 57 0F 89 BC 3B 67 25 B9 1A E6 9D 2B 2E E8 59 85 EE E6 11 80 48 C8 05 B7 36 54 0C B1 1C B1 C1 CB 6D 32 69 E9 3E 9D 63 04 0F 1D C5 ED ED AD B3 7F A7 5D C6 77 3A 93 93 82 A1 98 92 55 7E 4E A7 68 CF 20 56 8D 85 94 90 4E EF 25 E0 93 69 61 E5 44 81 11 4B 10 CD 90 3A 9C F3 93 CF 27 D4 D6 89 F9 94 B9 6D B3 28 6B 1A DD 96 85 A3 5D 5C EA 57 72 4A B6 8A B2 E1 41 8E 56 C1 01 43 63 00 97 75 6C 70 AA C3 23 04 03 5C 0E A1 F1 0F C7 76 1A 32 EB D3 68 3A 7D BE 97 24 C1 11 25 49 0C A1 4E 48 CF CC 38 C0 FB D8 00 E4 71 5B 7E 35 F8 71 A5 6B 5A 29 9B 49 B5 B1 D3 6E D1 32 24 65 F2 22 09 90 C4 B8 0B D4 01 D4 8C 8C 9A F3 DF 18 F8 7B 59 F0 F5 A6 9D 73 AD C5 A1 CD 6D 03 24 66 1D 35 16 27 90 F3 86 90 F9 63 23 03 1E 9C 74 EB 94 A5 D5 F9 7F 5F 33 19 B6 95 FA 6B FF 00 00 F7 53 2D FC FA 61 92 D4 42 2E BC AC A1 9D 48 8E 46 29 91 D0 92 AB B8 8C E7 27 00 F1 D0 D3 2D EF DA E6 E2 6F 20 89 B6 EC 1B 04 91 ED 03 CD 74 2C 36 92 73 85 27 9E 0E DC 0C 1D C0 71 C9 E1 7B 4D 43 41 B5 D4 65 82 CE 1B 99 60 17 69 67 E4 46 B1 42 BB 41 DA 23 2A 77 60 90 09 E3 EF 64 FA 1E 5B E2 11 D0 A0 F8 65 1C A3 4B B3 B3 D5 6E 64 55 B7 92 D6 DE 38 D9 CA B7 CE 7E 51 9C 05 1C F4 19 61 4D C5 DD FF 00 5D 4B A4 D4 9A 8B EB D4 EE FC 4D F1 0F 4E F0 D3 DB 47 77 A7 6B 06 3B BC 24 57 31 5B 2A A8 72 01 C7 EF 19 48 20 30 3C 8C 67 23 AA B0 1C 8F C2 AD 73 57 93 C4 3E 22 D2 EE 75 3B BD 56 DE D6 E8 24 2B 75 38 69 42 EF 65 69 01 63 92 06 13 20 1C 0D D9 03 27 07 80 D5 21 B3 D6 34 6F 05 59 68 D7 56 27 52 95 9A 1B 9B 98 C3 C2 3C D5 2A 57 78 0A 0E 54 3F DF C1 27 9E 6A FF 00 C3 DB 79 9B 47 BD D5 DE 25 94 DD 5F 1B 46 7F B5 5C 46 C7 72 86 61 F2 3A E4 1E 33 92 49 E2 B7 8D 3D D7 5D BF 15 6D FE 66 CD 45 C7 4D 3F 3E BF E4 7B EC 57 6F 2E A1 0E DB 6B 96 89 D1 B3 37 98 86 28 9B A3 46 42 B1 25 C1 4E A4 60 7C C0 36 7E 53 14 FE 55 8D AB 5E 5C 19 41 B7 45 F3 75 01 1C 46 46 85 14 3B 33 60 72 AC 41 52 15 41 C9 E0 0E 1A B9 65 D2 F4 DD 27 C6 DA 6A D8 DA CB 1D BA 5A B5 CB 8C C9 23 9E 1B F8 5B 2D 9E 07 1D 7B 62 AD F8 B6 C2 F1 F4 6B AD 2F 48 B1 B9 7D 47 55 99 22 9E F0 9D A1 D1 51 77 48 EC A3 68 1B 54 26 D3 B7 39 6C 29 EF 93 F7 57 32 FE BA 19 49 F2 A6 D6 AB FA FF 00 80 55 83 5F F1 6F 88 9E 7B FF 00 0D 5A D8 C7 A4 99 36 42 D7 B1 B2 49 26 00 DC DC 1C 11 9C 80 47 A6 3A 83 45 68 59 DD 6A BA 36 8F A6 D9 68 9E 1C 3A 8D A4 76 CA 0C DF 69 8E DB E7 04 83 F2 9C E4 9C 6E DC 09 0D BB 20 90 73 45 73 4B 2D 9D 47 CE E6 D5 FF 00 BC 97 FE DC 73 73 5B 46 DD FD 1F F9 19 1A 9F 8D F5 61 34 96 DE 15 D0 75 0B A6 79 0C 6B 3E A8 A6 0B 7F 33 79 C8 4F 30 AB 33 12 CC BB 0B 0D BB 06 06 01 AC 7F 03 C1 AC E8 7A 9D E5 E7 88 8C 31 DC EB 22 49 62 9E 34 12 4A A5 79 90 A0 45 23 07 E4 3F DD 21 43 72 07 3D 87 C4 6B 8D 22 0F 0C 37 F6 D5 BD EC B6 A5 C3 06 B1 0B E6 C4 C0 F0 EA 58 80 08 FC 4F 3D 30 0D 78 95 CF 8C F5 0B 4B 1B 81 A6 78 C3 55 BA 59 B8 F2 75 0B 70 D2 01 C8 E1 CB 38 5E 09 E5 48 CD 37 3E 56 F5 D6 D6 3B E3 0E 65 E5 73 A6 F8 49 68 FA 9F 8C 35 9F 10 41 0C 2A B0 87 F2 A0 C6 D1 F3 93 85 5C 70 B8 00 0C E0 80 0E 31 CE 47 AA 59 6B FE 74 B7 B6 BF D9 C9 6D A9 C7 FB C9 21 96 55 54 94 F4 E1 C0 24 90 AA BC 91 E8 01 20 64 63 7C 38 D1 27 D0 FC 35 61 6F 3C 76 71 4B 22 8B A9 7C A8 1D B7 29 0C 06 65 DD B7 7F 2B D3 A0 0C 00 39 C8 93 44 8E EB 51 D4 F5 5D 66 D5 23 69 B7 18 ED FC D9 59 53 3F ED 6D 07 20 2E 38 FF 00 F5 8B 69 C6 D0 5D 17 F5 F3 BE 82 93 E6 BC DF 5F EB FE 09 C0 DD 5C DC F8 27 51 D4 64 F0 7E B5 A1 EA 96 1A A4 9F F1 E8 6F 14 CB 0B 1C FD DD AE A7 BE 01 07 3D 38 E3 35 A5 E0 8B 4B EF 0F DA EB 33 DD BB 7F 6C 4E 1E F2 E1 1B 72 85 0B 13 90 00 E3 73 EF 7E 41 1B 71 C8 27 02 BC D9 B4 AB AF 10 EB CE 82 CF 4D D3 8B 5F 0B 79 4C 12 08 D1 5D 9C 2E 11 1D F9 03 39 C2 F6 A9 F5 6B A9 65 D6 2F AC E4 8E 35 D5 14 AC 2F 71 A7 EA 41 20 B8 DB 80 59 B7 E4 C8 ED DC EE 07 27 24 67 35 9E F0 F5 56 5E 96 FF 00 86 39 B9 AD 2B BD 93 BF CF A9 E8 5F 04 ED FE C5 A0 6A FA BF 96 D7 12 4B 32 A1 8A 12 AD 20 55 19 CE 32 31 92 C7 8E A7 1C 57 A5 58 DD DA EA 73 49 2E 9F 79 72 56 44 73 2E 1C 11 1B E1 15 4E D7 C9 53 85 38 0A 36 1F 9C 90 49 06 B9 ED 17 59 5D 3B 47 D2 60 D4 7C 3E DA 40 86 3C A3 48 10 DA C6 49 00 9F 35 54 EC 90 A3 92 32 00 62 E5 37 6E CE 2C 78 3E 18 E5 8A E3 53 9C 43 B9 EE F2 AD 22 8C 82 41 1F 29 EC 49 93 1F 89 1D EB 64 FD EB 74 40 93 51 5E 66 77 8A B4 CF 88 5A A6 A4 06 8D AD 58 5A E9 12 A0 C0 6B 62 B2 21 DC 31 B8 15 76 0F C8 3B 81 50 36 E7 0A 46 4F 9D B4 5A 77 83 BC 74 B0 78 C3 46 BC D5 D6 49 30 BA 9D F3 B3 6F E9 F3 AA 12 55 D7 91 90 49 23 3E A3 07 D9 2E AE 61 B6 46 BA 93 4E 94 41 6A 4A DB 2C 6B 29 C2 8C 2C 99 8C A6 C4 C0 52 14 E4 82 A4 95 60 1C E7 0F C4 5A 7D C7 8C 74 9B AB 3B AD 12 5F 2B 63 4B 1C CF 10 86 60 C8 CD B5 54 12 D9 6D AE 31 92 A0 90 DD 01 22 B2 94 5A 9D E3 AB FE BA EC 67 56 11 7E BF 87 DD FD 79 75 2A F8 9A EF C3 D1 5B 42 65 7B 5B 73 70 D1 E2 FE E2 D8 42 FE 58 0A B8 5F 94 34 9D BE E8 C0 07 9C 01 C6 ED FA E8 7E 1E 10 BC DA AD F5 9C DE 4B 6D 61 71 24 F8 4C 0C E0 3E E5 51 90 A7 A0 CE DC 74 C8 AE 4E CF E1 76 8D 1C D1 EA 5A D6 B3 7B AB EE B6 69 4E A7 24 C1 62 88 00 0C 6C 33 BB 20 28 63 92 C5 46 D1 91 F3 0A EE A2 B4 7B 19 16 4D 37 4A 8D 26 31 01 B2 49 02 0F 98 92 4C 92 6C 62 48 0A 07 CA 5B 96 E4 63 0D 55 CA F9 5B EE 11 A7 37 2E 66 97 F5 AF F5 FD 23 88 3F 12 80 B6 10 D9 EA 73 EA 13 48 EC 91 C6 34 B0 B7 1C 9C 20 2C 1F 66 49 60 A0 84 3C 8E 54 F4 34 F5 2D 3F C4 1A CF 8D 62 D5 97 4F 5D 26 F6 15 01 96 39 22 B8 90 1C 00 AD 87 2A 87 EF 28 EB 9E 7D AB B3 D3 B4 ED 53 CF B8 96 DA 4B 4D 3E DE EE 47 F3 23 B0 B7 89 8C 2F 19 2A 7F 7C 71 B8 B3 02 4E E8 98 8C 91 F2 91 9A B9 FF 00 08 9D 8C BF BF D4 96 5D 42 F1 B8 92 49 9F 28 FD 83 18 B8 8B 81 83 8D BC 63 3C 9E 4B 92 D9 F5 35 E5 69 34 71 BE 16 D4 74 8F 08 DB DD 35 E7 8B 6E 2E 0A C7 E5 C7 63 2C 4D 88 4E 73 C2 2B 30 6C E4 60 A9 C1 19 E4 F5 1D 7E 8F AA 5B C8 93 B5 AD CE 9B 06 9B 6A 7C C9 B1 72 B3 15 52 1F AB 07 C2 0F BB 8C F0 02 15 03 18 DB 5F 44 51 14 88 5E E6 1B 6D 3E 25 9E D9 21 8D 84 0A 59 8A 38 2A AA 00 C8 0B 21 CF 51 93 8E A6 A9 14 D1 6D 4D B5 AD AF 84 6D 75 15 48 42 CB F6 48 92 67 56 E9 8D D2 05 DC BF ED 93 93 C7 1C 92 04 DE 85 DA 5B B6 60 78 DF C5 9E 13 F1 1E 9B 3E 91 6B 73 75 AA 5D 4A CC 21 B7 B3 B7 79 18 4E 00 08 EA 5B 03 68 C1 04 2E 73 BF 38 EF 58 92 F8 33 C5 57 BE 15 D3 74 6B CB 35 B2 B1 B3 66 57 11 29 9E 72 E5 B7 92 40 21 76 9D DC 6D 27 1B 48 24 1A EB A2 F8 7E CD 75 25 FD A5 85 9E 85 21 66 91 1E C5 DD EE 76 B1 39 55 F9 C4 51 3E C3 B7 8F 31 72 4E 30 00 AA 7E 21 F0 84 91 A5 DA 5F 5F 6A 5A 95 95 BA C3 3C 4F 7F 3B C8 1D DC B2 15 C6 42 02 81 41 C8 50 71 2E 0D 54 55 AF F2 32 94 5C 96 DB 5C CE D2 F4 FF 00 0C E8 B3 BC FE 1F D4 6F AF E5 BF 06 DE 7B F9 63 2E 90 E4 86 7C 49 80 A0 ED 04 95 39 63 C6 08 38 0D D9 E9 5E 19 D2 56 EA 1D 6A FE F2 0B E9 61 41 6D 66 C4 18 E2 81 1F 8D A1 0B 1C BB EF 19 27 93 90 00 1D 2A 84 E2 6B AB 01 6E 96 F3 DC CA C9 E5 A9 B7 D5 2E 27 2A D9 EB 2A 30 0A A3 AF 52 7B 63 20 64 56 7B F7 82 DE 28 34 F9 75 0B 76 96 22 8A 31 71 33 5C 61 79 F2 B8 31 80 7A 86 46 E9 82 0F 63 49 CB A8 53 94 62 FD 7F E0 1C EF 8B 7C 17 A5 69 DE 27 D3 2D B4 5B 78 AC 60 B4 B6 B8 BA 7D B2 12 4C 84 64 72 49 39 C2 71 E9 C6 3D AA F8 4B 4F 3A 6F 84 7C 37 6D 72 C2 DB ED B7 1F 6C 91 DA 52 8C 83 79 50 4A 30 C2 8D A0 10 DC EE E7 B2 8C F5 2F A1 A7 88 ED AF A0 3A F5 8D F5 DC A8 B3 BA 41 73 14 9E 6B 00 54 A9 50 81 91 46 54 02 18 83 93 C0 A9 A5 BA 10 EA BE 13 1A 9D C4 64 AC 61 BC C5 20 A1 2E DB 63 00 AF 07 27 68 C8 FA E7 BD 6B 07 7F 77 FA EA CE 8E 68 C9 5F D7 F2 B7 EA CB D6 17 F1 5F F8 FC DD 47 2A 5C 40 2D 42 AC B1 E1 55 37 1C AE 72 73 9E 42 FA E4 F4 1C E3 A6 0C 2F A5 9D D6 02 EB 07 C9 1A CE 1E 35 69 55 B3 9D AC B8 20 15 42 1C 6E EF 8C 63 9E 63 C3 FF 00 F1 30 F1 9E A5 7B FB 9B 9F 28 A2 F9 BB 48 51 F2 15 2E 9C 1E 78 C0 1B 87 CA C7 93 8E 65 F0 CF DA BC 41 25 CE AD 35 CC D3 E9 8F 76 3E C7 1C C5 4A BA 44 02 F9 BB 76 F5 69 23 56 03 6A 94 20 95 3F 31 07 19 CB 65 E5 FF 00 07 F5 14 EC 9F 2F 53 AC B7 20 09 22 05 C9 8D F6 96 76 04 B1 20 36 78 3C 7D EE 9C 7B 0C 62 8A AB 79 02 CF 70 4B 5B DF B6 D0 14 34 37 46 35 3D FA 07 1E BD C5 15 37 B6 87 33 A9 28 BB 25 F8 FF 00 C0 67 13 E2 6D 12 F9 74 AD 42 09 D6 C3 53 79 ED D8 4A D1 23 C7 35 B8 20 ED 93 0E D2 96 FB 84 0F 9A 35 C8 39 38 E5 38 1B 1B CF 15 5A 5B DB 69 BA 9F 86 F4 5B AB 2B 78 D6 14 D4 35 2B 44 68 61 41 9C 0F B4 03 E5 FF 00 16 39 27 E6 38 EA 48 AF 62 F1 55 FC 96 A9 0C 49 1C 4D BE 19 DC 3B 2E 5A 36 55 00 15 3D 8E 1D 87 E3 59 3E 1C B7 40 DA 65 BC 6F 2C 7F 6B B4 B8 9D DE 39 59 48 78 E4 8D 54 E0 1D A7 89 0E 43 03 D0 7B E6 5C 6D 2D 3C BF 5B 1D CD 7B 89 BE A4 D7 DA CF 97 A6 4C E6 C8 C7 6C D6 B1 9D 3D 91 55 A2 52 CA CA 76 3A 64 7D D3 C1 C8 CA F0 14 75 6D 0F 0D E9 EF 6B E1 C8 52 E6 18 6E 44 B2 A4 C8 91 A8 3B 46 54 AB 12 C7 04 A9 1B B2 31 8C 00 01 23 27 93 BB D3 AC A0 F1 C3 DA FD 92 DD 8C B7 68 F3 4F E4 22 CB 2E F5 42 C1 99 40 E3 3C E3 B9 E4 E4 F3 5E 8D 72 8A E2 28 C9 65 56 62 BF 23 95 20 6C 6F 42 3F CF 3D 40 A6 B7 72 64 4A D1 49 1E 7F A9 78 27 C0 17 9A FB 5D DE C5 24 13 DC 6E 76 8C CA E9 0C A5 8E 7C C0 DD 3E 98 60 39 E4 66 B9 09 FE 0F DF 4B E7 C5 A6 6B 1A 5C FA 42 CF E6 F9 F9 CC EA 31 CA F1 F2 E4 0F F6 80 3D 4E 33 C7 A5 E8 F7 F7 57 1E 21 BD D2 6E 27 92 5B 78 09 75 62 C4 3E E5 93 77 2C 31 C1 CE 0A F4 C0 03 18 C8 38 D0 CD 35 96 9F 7B AD 41 33 2D D0 BF 28 C3 00 AB AF A1 E3 38 E4 F1 9F 4E E0 52 F7 55 9F 4F F8 28 C1 D9 B7 A7 53 A3 D5 B5 15 83 C3 37 00 31 DA F6 CB F6 77 66 F9 E5 42 14 16 2A 40 20 82 DC 8C 77 1C E4 90 32 34 CD 3F 5B B4 B6 B2 4D 36 EA 25 46 93 75 CC 12 4C CC 37 6C 59 15 70 23 26 22 D8 DA 4E ED A1 4E 42 B3 3F 15 F4 B8 E0 D5 F4 DD 5A EE 58 12 16 89 84 FE 5D B9 64 47 C2 7D D2 33 F7 72 80 FA E4 B1 04 66 BB 7B 38 D5 E3 8E ED 86 67 92 21 B9 FA 64 13 B8 2F 1D 40 C9 C6 79 19 3E A7 34 B5 6E 4F 7F F8 72 A1 25 D1 74 B7 E4 63 79 BA 74 D0 3D AD CD 9D CB DD C1 68 E8 F0 5F 23 34 A6 D9 CA 19 55 59 43 79 D8 01 01 D8 5C 92 14 31 CB 66 AA 78 93 43 5B FD 2E 6B 95 49 66 D4 ED F6 87 75 59 02 B9 C0 2D B1 58 91 B7 9C F0 4E 0E 46 49 06 BA 4B FD 36 CF 54 81 61 BD B7 49 95 1B 7A 13 C3 46 E0 10 1D 18 72 AC 32 70 CA 41 1D 88 A7 07 30 DC C1 6A B9 28 62 63 96 25 9B E5 2A 07 24 F3 F7 8F 5A 26 EF 1E 59 0E 51 8C A3 63 06 D7 C4 D0 CD 69 6D 1C 46 E6 5D 40 DB A9 6B 74 83 38 DC 42 89 0E 76 E5 77 02 32 18 0E B9 E9 90 F5 5F 11 EA 91 C5 BE 44 D2 90 C6 DB B6 22 C8 CC 41 C6 48 27 E4 C8 E4 00 49 1D C8 3C 56 D5 E5 84 17 D0 4B 14 AB 81 28 55 76 5E 18 80 72 06 7A 8E F8 23 91 9C 82 0F 35 CF DE 6A 2D 26 95 75 AB 35 B5 BB 5E 58 45 A8 9B 79 0A 67 CB 31 48 50 11 93 DC 28 CF AF 3D 01 C5 29 6F A9 11 83 B5 9B 35 AE C6 A1 6F 66 96 F6 B0 25 EE 54 23 3C F2 0C 9E B9 2C 30 01 1F 8F 7E 94 E8 23 D4 67 67 17 72 98 02 F0 04 0A A0 3F A9 C9 C9 FE 5F E1 9B 0C E2 D3 5C 3A 35 8D BD BD 9D B1 C1 CD BC 41 58 62 30 7F DD E8 00 E9 D0 0A B8 2C 22 9B 5A 7F B4 3C 93 F9 36 EB C4 84 10 DB D8 E7 2B D3 8F 2D 70 00 03 B9 C9 C1 0D 2E 67 71 E8 AC 99 02 58 E8 B1 C2 A9 1C F1 CC 59 8C 80 00 93 3B AB 02 40 03 04 95 01 81 18 1F C2 09 27 9C BA DA 6F 3F 62 D9 DB 96 78 D5 12 1B C2 FB 8B 46 02 31 0E CE 0B AE EE 98 39 24 61 B3 CE 45 DB 89 FE C3 2D BD BD BC 51 24 4D FC 21 70 07 EF 23 5E 31 FE F9 FC 69 FA 54 82 7D 26 D2 E3 CB 8E 36 9E 25 99 D6 35 C2 EE 71 B9 8E 3D C9 26 A9 6D 75 E8 55 DA 22 BA 8A FA 3D 2E 3B 7D 3A 38 2D A6 0A 51 02 80 D1 C4 02 9D BE 98 1C 01 C2 9C 67 A1 1C D6 7E A3 0F D8 AD 16 4B CB C1 7B 70 CF 14 7B 2E 5D 63 84 82 DC 92 BF 74 7C A1 8E 4E 4F CB C6 4E 01 D2 6D 33 16 EB 6F 05 ED D5 BC 2A 9B 36 C6 CA 49 F5 62 CC 0B 16 39 E4 E7 39 E7 AF 34 8D 61 6D 66 AD 2C 11 95 9A 47 45 69 99 8B 48 41 65 1F 78 92 48 E0 70 78 E3 A5 0B A2 48 4D 39 7A 94 64 BE 5B 16 23 4D B1 76 69 9C 3B 44 60 11 17 C2 AC 78 03 86 E0 F9 7F 39 56 50 38 25 47 2A 5C DB 6B 17 29 73 3C 52 28 B9 46 F2 E0 89 9B CA 8C AB 6C DF F3 80 CC 40 C1 C1 C2 B1 20 E3 00 82 2C EB 53 DC 58 DB 2D C4 37 0E 0C 97 10 41 B0 AA 95 51 2C B1 C6 58 71 9C 80 58 8C 92 32 C7 20 8C 01 2E 89 70 D7 9A 06 99 74 51 22 32 DA C5 21 8E 25 C2 0C A0 38 03 B0 19 E3 E9 4D BE 85 7B 37 CB CD D0 AA B6 BA 38 B6 B3 D3 E2 D3 E2 9E 3D D1 C8 B0 4B 1E 4C 0A 55 B6 BB AB 0D C9 C2 32 8C 8E A0 2F 1D A9 5A 78 7A CE C7 59 8B 51 B4 99 9E 1D CF 02 A4 42 35 8E DC EF 62 46 32 00 5C FE EF 6A AE EC 9C 9C 9D CD 5A 06 37 87 C4 B0 C6 2E 2E 1A 2B 88 E6 B9 68 DA 42 55 5D 44 31 80 07 65 C1 63 B7 A6 E6 2D D7 18 B0 6E 0D BE AF 65 A7 45 1C 69 6E F6 B2 C9 85 5C 6D D8 D1 2A 81 D8 0C 39 FC 85 12 95 B6 09 47 96 CF BF F5 FA 1C C5 EE 83 AA FF 00 63 EA 36 D6 84 B5 DE AF 74 23 BB 90 00 16 DA 17 E5 D8 06 FB FF 00 29 C7 07 BF 07 20 D7 4B 15 A5 AD A6 8D 66 AC 0D A4 36 31 A3 23 4C 50 B4 0A 8B 83 96 39 03 E5 DC A4 83 D0 9C 1E F5 24 32 9F ED 9B A8 39 DA 96 F1 38 CB 13 CB 34 B9 E0 9C 7F 08 E8 3F 90 C3 2C 6D 22 9A C5 E4 B8 02 73 7A 9B A7 12 28 DA E1 81 F9 4A 81 82 02 90 B9 23 25 55 41 27 19 A9 83 57 BB DF FA 41 CA 9D E6 CB 11 25 D1 79 7C F9 10 28 60 22 F2 87 55 DA 32 58 1C E0 EE DD D0 F4 C7 7C D1 53 2C 68 8C EC 88 AA D2 36 E7 20 60 B1 C0 19 3E A7 00 0F C0 51 49 C1 09 1F FF D9";
            } else if (rand == 11) {
                c.setLIEDETECT("리관헤카톤타나");
                packet = "A4 16 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F7 0D 24 6A 30 D8 DB 5B 6A 8E 2E 2F 12 05 F3 AE E3 40 91 C8 FD 08 0B 9C E7 8C 9E 00 39 E3 1D 05 3F 37 4E F0 FE 84 B7 7A A5 EC 06 DA D9 A4 B8 8E E1 93 0B 1A B1 62 AA 83 9C 61 5F 62 81 C9 1C 0F 4A B8 2E 22 D2 74 A9 26 BF 9B 60 82 39 2E 26 66 95 A5 21 46 59 9B A0 24 0C F4 00 01 C0 00 0C 0A E1 7E 20 EA 7E 19 D3 F4 EB 4B 2F 17 69 F7 9A 8C C2 47 36 B2 85 5D CC 30 3E 7D C8 63 C0 E4 02 BC 1C AF 42 00 62 A4 D6 E5 43 DF D9 0B 7D F1 5B 43 8B 5B 87 4C D3 A2 9B 50 9E E2 E5 63 76 B3 00 A1 21 8A 8D A4 94 DE CC 02 0E BB 40 3D 58 0C 1E 3E E3 4A D3 74 CF 8F B6 16 DA 7C 06 D6 DC CC B2 98 FE E2 87 2A 49 DA 0F F0 93 DB A7 3C 71 8A BF A1 69 1A A6 AB E2 2D 3F C5 FF 00 D9 16 BE 1E D0 74 B8 8B C7 14 85 A4 66 84 EF 76 2A B8 E7 97 62 38 50 03 0C 67 15 92 35 25 9F E3 54 17 96 BA CC 57 C9 1C 7F BB BC 96 20 EA D9 84 E0 15 88 2E 4F CD 8E 31 83 9E 38 C5 67 AA 9C 5B DE EC D7 95 28 C9 47 6B 7E 27 B2 4D AF E9 BA 65 F4 F6 D7 57 2B 1E CD A1 15 23 6C 2A E3 EE E0 13 CF 7C E0 0C 30 1C 95 35 76 EE F6 C9 2C E1 BA B9 9A 14 B3 24 48 64 99 7E 40 00 DC AC 49 E1 30 40 39 3D F0 3A 91 5C 57 88 61 B9 B5 B0 7B 67 68 E7 10 94 59 27 7B 29 95 F0 77 32 81 34 B2 31 93 EF 37 03 38 C9 04 8E 01 E2 75 28 A0 B1 F1 9D AE 9B AC 68 7A 86 AF 7B 72 01 D2 E3 BE BF 69 AD 21 04 01 B8 29 1C AE 54 B3 03 C0 18 18 18 CD 5C 25 67 66 AD FF 00 0C 4C 60 A5 1E 65 FD 6D E8 6E 78 CB E2 A4 DA 75 AC 5A 8F 85 6E 34 6B DB 14 B8 78 64 49 37 F9 81 F0 0E ED A1 D3 2A 77 37 62 3E 5C EE C9 C0 F4 5B 4D 44 DE 78 52 DF 56 BD 85 A1 66 B3 17 32 C7 1C C1 31 94 C9 C3 16 00 0E 4E 09 23 1C 13 8C 71 F3 3E B4 9A 6E 9F E1 0D 33 C3 D0 6C B9 D5 9E F1 AE AE 24 81 E2 9C 28 61 B0 22 3C 6C D9 E8 32 38 C9 03 DA BD AF C3 DE 32 8F C4 B6 93 E9 FA 96 93 73 A6 C5 16 CF B3 AC 4F 24 F3 48 17 90 D8 8D 72 A5 4A A9 EF FA 66 B7 71 8F 2D D6 D7 34 AB 49 28 A5 15 A6 BF 75 F4 D0 EC 56 41 67 31 96 6B B0 EB 1A F9 26 1B 78 F6 C6 98 8C 3E 5F 24 84 23 0C 41 25 46 1D 41 C9 DA 6B 32 D7 C5 1A 65 D6 AC B6 91 6A FA 7D ED E4 81 D6 CC 5A DC 65 1F 3B 9B 63 A2 B3 60 AA C6 B9 72 00 F9 8E DC 64 8A E3 97 C5 C3 46 D1 75 17 BA 86 5D 6D 2E A6 30 2D 9C D7 20 C8 46 00 04 A9 04 ED 2A 58 13 8E AA A3 1C 92 38 0B 2B DB 58 35 28 24 D2 25 87 48 75 91 40 B5 B0 32 DC DE 4B 8E C6 5D A5 3E 6E 78 42 07 23 2A 71 C4 C6 2A 4F 96 FB 7F 5E A7 2D 5F DD 4B 96 F7 68 D9 F1 57 88 B5 5D 61 F5 ED 0F C4 96 5A 6B CB A6 42 D3 47 35 A2 BE 52 4D F1 80 54 92 7E 5C 36 08 C0 F5 3C 8A E9 3E 17 5F DE AF 82 3E C7 69 2D AF 9A 1A 49 04 53 79 B9 29 FC 4C 18 1C 0C 92 00 03 18 C1 3C EE E3 CD AF B5 29 6E F5 1F 14 5E 5D C4 6D 6E 2E 2D 95 0C 2D 09 8C 82 64 8B 80 B9 3B 78 53 C1 27 8E 33 5D 2F 85 3C 53 69 E1 9D 23 7D C6 9F 7D 22 35 91 8C 4B 0C 41 A3 DE FC 8C B1 61 8F 43 EF 5A 37 18 52 7C DA 5E DF 7E A6 1C C9 4F 4D AE FE ED 3F E0 9E C7 A1 6A 37 9A BE 98 B7 92 79 6A FE 61 4D 89 F2 A9 5C AE 4F 20 9C 81 9F AE 7F 11 76 0D E5 55 2E D2 63 2A 4A 63 49 8A 00 64 51 F3 06 CA 13 80 76 80 73 B4 12 3A 0C 81 5C 77 87 2F 2F F5 3F 09 46 BA 34 12 46 71 2C 90 EA 12 4E 89 10 6C 30 03 68 DC CC 41 23 2A E9 8E 09 CE 42 E6 AF 80 BC 4D E2 2F 1A DB 5D C7 73 7D 05 A4 76 85 23 6B 8B 5B 40 64 94 90 79 56 76 28 0E 47 23 CB 3C 1E 31 C1 18 CB 97 DA 38 FC CD 62 EF 15 7E A7 75 14 77 F2 2B BC CB 6F 1D D0 81 52 39 36 EF 45 90 8C B1 03 86 29 BB 6F 56 04 ED E8 31 93 09 9F 4F FB 7D A9 85 EC 1C 6A 7F BD 05 AE 00 69 F6 28 65 78 D3 04 48 40 DB 96 04 10 02 F2 70 31 9D 73 A3 AB 8B 77 BA B8 BF D4 D5 EE 3C 89 8D ED E3 5B 46 B1 E4 8C 18 A3 54 49 41 60 14 06 53 BF 78 E4 AE 2B 03 C5 97 76 5E 10 92 31 A3 5E 5A 78 7A 69 08 62 06 95 FE 8F 77 B7 1F 23 3A 21 6C 8C 9E 47 F7 88 EB CD 26 D5 35 EA 68 A3 D1 1D FC 8E 1A 40 91 14 69 A3 2A E6 33 21 5C 29 24 64 81 9C F0 1B 00 8C 12 3B 75 0B 3A 48 D0 CD E4 B1 59 99 08 43 BB 18 38 38 EA 08 1C 9E B8 3F 43 5E 61 1F 8A FC 48 D0 C1 67 6B A8 F8 3F 51 6D B1 EC B6 86 F6 6F 3A 61 90 02 EE 91 86 5B D4 31 C9 E7 20 E7 07 D0 1A D6 D6 4B AB 59 6E 27 9A 3B B4 6F 29 73 33 27 9A C5 55 D9 47 40 C3 F7 6A C7 68 C7 CA C3 A1 70 48 FB D7 B0 5D 7F 5F D2 2C C5 6F FE 90 B3 3C F7 1E 6E 5C 6D 79 06 19 37 1C 7C A3 E5 C0 C8 C1 C6 EC 00 09 EB 9A 62 09 E1 91 E0 77 BD 48 21 8C 47 6F 74 92 F9 AC C6 43 B7 E6 5D A7 25 30 BF 33 06 18 39 62 4E E2 2F 4A D1 59 DA CC 63 11 C0 AA 1A 46 62 A0 22 96 24 B3 9E 40 EB 96 3C E7 AF AD 54 9F 58 B7 8F 56 5B 18 AE ED 26 B8 58 D8 BD 8A 4C 9F 69 27 E5 2A 42 96 1F 28 5D E4 E7 DB 1E EB 46 EC 81 69 72 FA 2F 92 E2 25 49 59 1B 7B 99 19 F7 05 25 B3 8E 4E 79 DC 70 07 00 0C 71 C0 AA EB 0D DA C7 28 06 28 88 99 A4 89 21 01 54 AF F7 5D 88 3C B1 CB 12 00 C6 71 CE 32 D9 F0 EB B6 77 1F 64 B7 97 52 B7 F3 EE E2 FB 4C 50 08 B1 24 D0 05 E4 84 2C C7 E6 39 20 63 3B 72 31 90 48 E7 3C 59 F1 0B 4A F0 BF 88 ED D2 F6 CD EE 2C E5 87 6C 93 DB 18 DC 89 14 AB AA B2 91 9F 94 3A B0 21 BF E5 A1 38 3C 10 49 DA C9 F5 FF 00 82 2E 4E 6B AF EB E4 75 53 5E B5 B6 A7 E4 CF 32 7D 9E 7B 84 8A 3F 3A 5F 29 95 F6 16 2A 83 03 CC 07 6A 74 2C 49 77 07 85 C5 4E 26 BF 96 ED 56 35 B7 FB 3B 06 CC AA 77 84 29 20 1B 4F 20 92 CA 4F 41 F2 32 9C EE C8 AA 3A 66 AF 63 E2 5B 41 AB E8 77 56 F7 71 26 E0 9B 5D E3 2F 20 18 55 90 E3 2A 06 E6 F9 59 5B EF 2B 0E 40 CE 67 8C 3C 66 9E 0D 16 52 5D D9 83 63 75 29 49 27 89 BE 6B 56 23 2A 5A 31 F7 C1 21 89 21 87 00 F5 3D 5B B5 D7 7F EA C3 6D 5B 43 A4 F2 65 BA 8A 29 53 51 91 41 8C 65 AD D5 36 3F FB 43 70 63 83 F5 34 56 7C 36 16 97 F0 25 C4 B7 0D 1C 2C 3F D1 56 CA FE 68 93 C9 FE 03 F2 B2 82 48 E7 80 30 08 5E 42 E4 94 73 B5 A7 F9 17 15 49 A4 DC F5 F4 FF 00 82 5A 9B 67 D8 1E F2 28 4D D4 B1 6E 92 08 1E 55 C8 9B E6 05 43 92 76 B1 2C 53 AE 07 4E 99 AE 2F E2 85 A6 BB A8 78 2A CB 4B B0 D2 04 93 DC DC A2 4D 15 A1 32 A4 4A 32 40 07 6A E1 72 17 E6 20 01 8A EE 20 7B 76 B5 4B 44 84 DB AA C2 A8 D6 F1 B2 86 B7 04 00 A8 76 1F 94 E0 F0 57 81 83 CF 4A B2 4A 49 23 88 CA 79 D1 8D BB 8A E7 6E 79 C7 F2 38 CF A7 B5 29 A5 2F EB B3 12 A8 A3 2F 33 CB 34 FF 00 86 9A A6 B4 96 D2 F8 A3 5B FB 64 31 3A 86 D3 AD 1F 64 0B B3 09 86 65 FE 20 B9 1C 2E 7E 5C 6E 1D 6B 03 45 F0 D5 8D 9F C6 26 B2 88 40 B6 16 D0 C4 54 B7 29 2E 63 5E 99 27 3B 8E 7B 9E BD EB D2 ED AE 35 79 FC 51 77 A5 CD AA 13 1C 71 99 01 8A 14 5C 67 04 0E 41 38 1B B1 D7 3E F5 7A D7 4A D3 F4 34 7B E6 48 A2 BF B9 91 5A 69 A4 2D 3B B3 1E B1 AB 1C 33 71 95 5C 0C F7 20 9C E6 55 AE A4 BC FD 4A 57 8C 5C 5F F5 D4 C2 F1 D5 A6 A3 FF 00 08 E9 B7 4B C5 8D 9E 68 FC BB AD 8A F2 97 F9 F7 16 5D AA A4 6D 08 06 07 AF A0 27 CF 3C 63 A1 D9 78 78 C3 A9 F8 AB 51 D5 FC 54 CD 28 10 B0 DA 96 91 16 C3 32 30 12 6E 56 D9 F3 28 5D AA 7E 5E A3 35 D4 78 A1 F5 F5 9E 33 7F 6D 2F 94 5F 64 2F 0A C3 6D 6C 73 F3 19 18 79 AF 20 60 49 C9 20 E0 64 81 92 73 5F C5 9E 12 D4 35 4D 36 EA 29 B4 E5 13 6F 97 CB 6F 3E 7D 43 32 49 FB CD B1 EF F2 BC A0 0E DF 9A 34 7C E3 07 EE B0 6D 29 AF 79 AB FF 00 5F A1 B5 36 E3 CA 9B B2 77 DB FC CC DB AB 5F 07 4B E2 8F 0C D9 DB BD C1 B2 D3 9E 69 67 B5 28 64 6F 37 E5 61 1F 94 99 E4 15 F9 B6 AE 30 39 CF 51 D8 C5 73 A9 0D 52 FB 5F 8E C6 EA 78 7C B6 89 25 8D A2 8A 22 8A A4 12 C1 C9 70 37 00 D8 51 90 01 FB C4 6D 3C B7 86 FE 15 1B 4B C8 B5 54 D4 6E 63 DA A6 D9 A4 F3 8C 92 6F 1F BB 25 7C AC 34 78 61 82 03 E5 40 21 89 5D D9 E9 EF 7C 2D 63 E1 EF 0F 5E C7 14 37 92 C7 02 A9 59 AF 2E 9A 45 62 53 CB 5D 8B B8 FD D1 85 01 94 63 39 07 8A D6 7A AD 1F F4 D9 6D C5 C9 46 F7 BD 97 EA FE F6 61 AC 5E 22 D4 7C 17 3E 8D A3 59 27 FA 4A B4 86 46 53 99 3E 60 0E 19 8A AA 11 81 D3 7E 73 FC 38 E7 95 D2 FC 1F AD 6A 07 4A D3 B5 6D 1E 3B 0B 3B 69 98 31 31 22 DC 5D 30 3C A8 C6 18 83 9F BC 4E DC 0C E7 80 2B DA 7C 39 61 34 1A 04 10 48 25 B6 62 15 9F 6C A5 F7 70 BF 77 3F 70 10 30 54 01 82 58 83 9F 98 E4 DE 6A 69 A4 F8 D6 5B AB E8 E5 68 24 8D 63 89 87 CC 10 7C A0 91 93 D0 7C D9 C7 3D 78 24 D3 BF BE D6 9F F0 C7 05 76 AA 37 2B 5B FE 09 E3 3A FE 92 2C FF 00 E1 26 92 EB 4F 10 CC B7 31 C7 00 42 4A 46 72 0B 01 C9 CF 05 7D 7A D4 F6 7A 9C BA 36 96 F0 5B F8 99 FC C9 ED 16 31 69 0D A2 5C 97 DC A3 31 97 27 0A 33 C7 A8 EC 0D 7A CD DE 87 69 E3 4D 4D E3 B8 85 ED F4 D8 42 BF 94 18 23 CA 4B 96 DC 57 F8 43 30 6C B6 32 70 79 CF 4C 8B 85 B9 BA 97 51 BF 82 DC C7 6A 27 2D 2A 38 DA E9 B9 5D 40 20 1E 31 BF 91 CF 38 FA D6 35 55 E1 6D B4 F3 F4 FE B7 32 B2 72 BD CC 5F 06 5D EA 3A 37 C3 6D 72 73 74 D6 92 41 E6 30 B7 B8 88 10 DB 94 0C AA F0 C0 E7 68 0C 49 5E 4F CB DE 9B F0 C2 E3 EC BA 5C 71 42 F6 93 DC B5 C0 BA 8E 04 01 E7 07 26 36 F5 D8 30 54 6E 2B 9D B2 3F 20 06 65 BD E3 0B 8B 87 F8 5C D1 CB 69 F2 99 A3 8A 07 54 1C 10 E7 3F 36 EC B6 79 E8 A3 1C 8C 9E B5 AD E0 CF 06 58 69 9A 4A 6A FA 86 9F 01 9E 1B 62 63 76 EA 72 A4 39 65 E8 72 3A 13 9E BD BB F2 C5 B8 D4 93 8B D1 2B 5F E5 FD 69 EA 09 5B 95 2D 77 7F 7F F9 0C D6 7C 79 AF DB C5 70 E3 C3 DE 7E 9C 54 89 5A D6 49 7C CB 65 5C 79 85 DD 40 DB 95 CE D2 31 B4 8C F3 8A E5 FC 7F E2 08 F5 86 B0 D5 51 75 9D 2E EE CF 2B 0A DD D8 79 71 CB 2A 37 25 5B 71 C3 29 04 60 E7 1E DD FD 0F C3 FE 54 16 76 B7 17 17 09 11 96 69 25 26 46 45 03 A0 C8 DC 09 DD F7 97 82 3E 57 7E 72 05 71 9A 86 A7 73 E1 EF 1E 5C 5A EB DA DB 49 A6 0C 5E 5B 0B 91 23 AB 16 23 E5 52 A1 8A A8 C3 0C 74 E2 9B 77 8C 53 7A FC B7 FF 00 33 A6 9B 6E 37 B7 FC 14 63 F8 67 C4 36 E6 EF 4D FE D3 4F 0C CE 67 70 8E D2 69 CF 2D D4 6C 01 21 D9 82 FC CC 48 1C EE 24 96 1E E4 7A 7C 9E 28 B7 BA B6 8A 28 AE 74 EB 97 13 2A FC B7 D1 AB B3 23 F5 DB 22 01 82 57 82 07 43 95 3D 0D 73 96 FA 6F 85 FC 49 AE DC 49 A8 5A 47 75 13 45 1C 71 B5 B2 EE 13 C8 A5 9D E6 2F 12 A9 2C DE 67 21 78 CA 9D DC 80 17 A3 D2 EE AD 7F B4 12 D4 5D DD DA 3C 6B B5 63 17 45 E1 18 3B 42 7E F5 43 6E 23 B6 38 F5 AB 5C CA CA 2C 52 D7 5F CC D8 05 AD B4 D9 AE D2 4B C2 25 75 9D 62 53 E7 B8 24 8C A2 9F 9B E4 6F 6F BA 18 90 40 C6 DF 0E D4 B5 AD 27 C4 57 29 77 3F 80 35 0B DB BB 69 0C 17 11 43 33 2A 28 48 C0 54 DF 12 0E 9B 5D 88 DB 9E 06 1B 68 DB 5E 87 E3 6F 0F EB 53 45 34 9E 15 B0 B3 4D 42 6B 86 F3 2E 62 F2 D2 44 46 44 0C 77 B7 CC AE 49 6F BA 7E E9 24 FC C4 57 33 63 E1 CF 16 F8 1B C3 77 56 C9 FF 00 08 A4 F6 EF 2A 99 84 A6 79 24 91 FA 81 D0 0C E0 8F 4C 0E 7A F3 4D B7 7B BE 9F 9F FC 31 74 9E 97 EA FF 00 AF F2 36 FC 05 E1 8D 02 ED BF B5 BF E1 0D BF F0 EE A1 65 30 31 9B 89 E6 6C 8C 75 1E 66 32 31 90 7E 5E 33 C1 CF 4E 23 5C B8 7F 88 DE 31 BB FB 14 06 4D 07 42 B5 91 A3 86 2F 94 3A 20 27 0B 81 C1 76 00 0C 74 1D B8 A8 7C 49 AB F8 CB C3 BA 2C 5A 5D D5 FD A4 49 A9 C6 36 DB 5A C9 21 98 26 73 BB 07 EE 06 24 8E 3A E0 8C 56 97 C3 2B CB FF 00 0F EA 2F A2 CB 6B 7D A7 C9 33 19 24 96 4B 11 22 C8 53 E5 2A DD 18 00 D8 51 B4 9C B3 01 C1 34 9A E6 76 ED F9 FF 00 5A 9A A8 F2 DE 49 DD BF CB FA D0 6F C2 8F 16 F8 43 C2 BE 18 BA 8F 51 D6 0D AE A3 79 21 32 6D B5 91 99 00 04 2E 18 2B 29 C6 49 19 1D 49 EB 47 C4 BD 66 DF C7 1E 2A D0 34 4D 26 71 7D 6C 23 F3 E6 16 6F E6 E4 90 4B 05 1D D8 22 9E 30 0F 38 38 AE 8B 54 9A 7F 10 F8 A6 68 D1 15 9D 98 41 18 CE 55 40 EA 41 C0 E3 20 9C 9E C6 B8 CF 04 78 2D FC 41 AC 6B 3A 9D 8E A3 A8 68 F1 C5 72 60 D3 EE AC A1 62 0B 16 39 CE CC 0D A1 46 0E 08 1F 30 C9 EC 5C 65 CF 24 DA D1 6B F7 7F 48 E5 E4 B4 34 DD FE A7 BA C9 AC E9 BA 4B FD 86 55 9E 0F 28 00 83 C8 76 05 71 C1 52 01 E3 B7 E1 45 72 B7 FE 2A D5 B4 19 93 4D BC 78 2E EE 20 8D 55 EE 50 63 CD 3F DE 2B 80 10 9F EE 8C 81 EB 45 44 EA CF 99 D9 5F FA F5 2E 30 8B 4A EE DF D7 A1 D6 6C BC 82 C4 C8 22 9A 57 89 8F 93 02 EC 0C A4 B3 2F 66 44 68 C2 B2 ED 53 83 85 04 E5 BA 3F 11 DA EA 37 77 52 C3 1D B2 C8 63 88 5D 4D 3F 32 B1 C0 45 0B D9 77 36 00 C8 25 99 B0 BF 36 5A C1 B2 07 50 5B C1 34 81 95 4A EC C2 91 83 8C E0 91 B8 03 85 38 04 0C A8 38 EB 98 E6 8D 6C 4C 97 31 64 C9 3C B1 23 EF 62 DC 17 0B C6 79 03 0C 70 3A 03 DB 93 9E 95 79 3D 3A 98 49 F2 AB F4 FE BF E1 8C 19 F4 3D 52 7F 11 4D 7D 1B 41 02 4C 23 2B 21 2C CD 19 00 7D DD B8 E4 15 C1 CF 04 1C 1C 82 45 6D 59 E9 6B 69 71 1C F3 4E 2E 2E 59 0A BC D2 AE 5D 9B AF C9 CE 11 7E F7 CA 07 71 CF 1C E8 A8 2A 30 58 B7 24 E4 E3 D7 A7 1F 95 2D 67 15 64 6C E4 D9 C5 EB 97 0F 7D AA E9 43 CB 8D 6D 5E 44 B8 8A 61 23 9D C8 40 2C 48 65 01 71 8E 80 9E 39 3B 73 5D 15 94 B7 66 68 D2 E1 C1 26 36 70 23 42 15 53 E5 0B BF 71 DF BC E1 88 1D 07 CC 0E 4A 86 2F 82 28 6E 26 B9 8E 68 52 41 6F 71 FB B3 26 5C 82 42 49 9C B6 71 86 3C 63 81 81 8E 82 B9 15 D7 2F 75 8F 18 4B A2 CC CB 15 B7 99 2C 01 A2 C8 3B 54 96 E4 12 55 89 D8 01 C8 3C 64 0C 64 E6 E1 06 9B B1 6A 0E 7B 6C 97 EA 45 FD 98 D6 9E 29 8D A4 8A 4B 93 69 6D 18 96 E6 3B 42 56 7B 95 0A DB 98 2A 30 52 DC 13 C6 46 72 39 C1 AD FD 53 5B 8A 4B 38 18 59 21 B7 BB 3B 21 B8 BE 75 5B 66 CA 97 56 60 09 38 F9 38 CA 8F 4C 82 6A 7B 0D 39 B5 0B 3B 2B EB BD 46 FE 49 24 B7 0C C8 B3 98 90 96 0A 7A 47 B7 A7 38 FF 00 78 E7 38 18 AC BA 56 91 77 E2 4B ED 36 7D 17 4C 92 DE 0B 4B 6B 84 0D 68 84 EF 77 9D 4F 51 E9 1A FE B5 A2 49 AD 75 B7 F5 FA A2 A7 34 A4 94 B7 5A 7D C2 E9 BA D5 E5 D4 08 F3 BA CC 24 0A D1 B6 9F 6B 2E 0F 20 85 DC EA 57 9E 41 C9 5D B8 23 39 E5 6D E9 21 5A 59 E4 B4 B9 B7 2B 39 F3 E5 50 44 8D BC C8 C0 E4 86 27 21 57 CB CE E2 3E 40 14 28 5D A4 1E 11 F0 E0 1C E8 5A 6B 9C 9E 64 B6 47 3F 4C 90 4E 07 40 3A 00 00 18 00 0A 5F F8 44 BC 37 FF 00 42 F6 93 FF 00 80 51 FF 00 85 44 9A 7B 19 B7 16 56 97 5D D0 34 5B 9B D5 97 52 B7 8E F5 63 46 92 CD AF C0 0A 42 E1 52 35 91 82 21 20 74 1B 73 90 4F 5C D6 66 A1 79 E1 97 B7 9A 08 B5 23 24 4D 3B 4F 72 F6 57 91 31 04 AB 70 E3 76 4A F6 C6 0F 3B 4F 62 47 5D 6B 69 6D 63 6C 96 D6 76 F1 5B C0 99 DB 14 28 11 57 27 27 00 70 39 24 D5 27 74 8B C4 96 D0 A5 BC 20 CF 6B 3C AF 2E CF 9F 28 D1 00 33 E9 F3 9C FD 05 4A 52 6A D1 7A FF 00 91 0D C5 23 8F 3A C6 83 AE CF 66 FA 8E AD A5 41 A6 DB 30 78 6D DE F6 2C AE CE 8C C0 9E A7 A7 D3 3C 8E 37 6A 5F EB 5A 11 F0 ED C4 3A 55 FD 94 B3 4A A8 AE 91 5C A4 B2 13 80 B9 72 18 96 6D AA 06 49 3D 3A D7 41 7B 0A DB E9 D7 73 26 0C 80 9B 82 C5 17 E6 65 C1 19 E3 9C 05 55 CF 5C 01 CE 46 6B 1B C5 16 D1 C7 6B A6 5B 26 E1 11 B8 0A 41 72 49 CF 52 49 EA 7D CE 4F 5F 53 59 54 8A 50 B2 EB BF CF A8 9C 54 62 E4 8C 4B 2D 43 4A 8A DD 20 3E 23 D1 60 B7 28 B2 DC 47 24 96 F2 89 24 04 60 1E 8C 48 0A B9 CF 23 E5 00 B7 68 62 83 40 BD 9D E2 BF D2 97 53 83 71 78 EE 6D 21 69 C4 41 80 E0 2A 6E 24 64 63 20 B7 27 D3 9A EC 16 E4 4D AF D8 45 25 B5 BB 30 86 ED 92 52 99 78 F6 49 1A 61 4F 60 43 73 EB 81 58 D0 5E 49 A6 58 78 AF 53 88 2B DC 5A A4 B2 A7 98 32 09 55 76 00 E3 1C 64 50 E2 B9 AD FD 74 2A 8D 9C 53 87 5B 7F 91 9B A6 0D 1B 45 82 DA EA D9 2F 22 8A 18 01 11 CB A4 5D 49 1A 1F BC 4A BF 96 BC 75 F9 88 CF 53 53 EA FA C6 B7 FD A4 A9 65 A6 22 AC 0E D9 DF A4 CF 70 A7 77 96 43 2B 61 39 0D BB A1 FA 8F 97 71 EE 96 20 9B 02 1D 91 A2 ED 11 A8 01 7B 63 B7 6C 7E BF 4A CE B1 B9 96 E6 67 82 66 2C 03 CA C1 81 DA 7E 59 98 28 E3 1C 60 01 EF DF 3C D5 49 24 93 DB 5F CC BB A9 36 CC 48 EE 35 EB 8B 22 83 4D B0 94 E2 43 C4 F2 5B 3C 9F 30 66 3F 66 20 A9 F9 B0 30 65 C3 64 E5 94 31 03 9E 93 55 1A F7 89 65 5D 47 4C BF 82 C2 09 09 65 86 CD A7 77 20 63 0E D6 FB F9 C8 38 27 1C 70 32 41 27 D3 76 37 94 C9 E6 BE 4E 70 F8 19 5C F4 C7 18 E3 B7 1D B9 CD 73 16 BE 0D B1 4B A3 75 05 DD F5 BC AF 1E 77 43 28 52 A1 8E 4A 83 8C E3 E5 5E A7 B0 CE 69 4A E9 AB 84 1A 5A BF 91 E7 DA 26 99 A6 78 AB C5 97 DA E6 B7 AB 3D BD D5 AC AD 1D 8E 99 14 E5 EE 90 44 09 27 63 6E 90 91 86 21 76 E7 20 9C 0E 05 7A 13 2C 70 68 AF A9 45 E2 2B CB DB 28 9F 7C 0F 04 B1 B7 00 85 60 CD D2 4E 77 82 09 E0 1E 06 F5 56 AB 43 C2 1A 63 D8 C9 63 78 F7 77 B6 92 10 4C 17 33 B1 4E BB BE E8 C0 3C F3 CF 7A E3 EF 6C ED 23 8F C3 FA 75 B5 AC 76 D6 B7 11 45 31 8E 22 D8 43 21 F9 82 06 27 68 C9 27 8E A7 AE 6A AD 68 59 7A 7D E3 73 4E 5A BF E9 7F C3 19 B6 11 43 6D 61 3B 5C 7D 9A 73 79 1B 40 B1 89 42 CA 81 BE 56 3F 32 32 2E 41 23 E6 C1 C1 38 AE B3 44 65 F0 DE 8E A2 DB C3 C6 18 40 40 8E B7 30 B2 F9 21 BA 79 A3 04 90 64 76 50 72 59 98 F3 C9 23 A3 94 AA 6A 16 DA 4A A2 8B 49 AC E5 CA 8C 82 A1 0C 6A 00 23 A7 12 1F C8 63 1D F3 FC 60 56 CB C2 37 31 DB C7 1A 27 CA 81 55 40 00 16 1D 05 12 E6 8C 48 6E FB 1C 37 F6 46 AF E2 29 A6 D5 22 D3 E4 9D 27 91 88 71 32 27 7C 63 04 F6 E9 45 7A 07 84 1B 77 85 AC 70 AA B8 0C B8 5F 67 23 3F 5E 28 A7 1D B4 DB D1 19 5B 9B 53 FF D9";
            } else if (rand == 12) {
                c.setLIEDETECT("을알아보는");
                packet = "7A 16 00 00 FF D8 FF E0 00 10 4A 46 49 46 00 01 01 00 00 01 00 01 00 00 FF DB 00 43 00 08 06 06 07 06 05 08 07 07 07 09 09 08 0A 0C 14 0D 0C 0B 0B 0C 19 12 13 0F 14 1D 1A 1F 1E 1D 1A 1C 1C 20 24 2E 27 20 22 2C 23 1C 1C 28 37 29 2C 30 31 34 34 34 1F 27 39 3D 38 32 3C 2E 33 34 32 FF DB 00 43 01 09 09 09 0C 0B 0C 18 0D 0D 18 32 21 1C 21 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 32 FF C0 00 11 08 00 2C 00 C4 03 01 22 00 02 11 01 03 11 01 FF C4 00 1F 00 00 01 05 01 01 01 01 01 01 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 10 00 02 01 03 03 02 04 03 05 05 04 04 00 00 01 7D 01 02 03 00 04 11 05 12 21 31 41 06 13 51 61 07 22 71 14 32 81 91 A1 08 23 42 B1 C1 15 52 D1 F0 24 33 62 72 82 09 0A 16 17 18 19 1A 25 26 27 28 29 2A 34 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E1 E2 E3 E4 E5 E6 E7 E8 E9 EA F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FF C4 00 1F 01 00 03 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 01 02 03 04 05 06 07 08 09 0A 0B FF C4 00 B5 11 00 02 01 02 04 04 03 04 07 05 04 04 00 01 02 77 00 01 02 03 11 04 05 21 31 06 12 41 51 07 61 71 13 22 32 81 08 14 42 91 A1 B1 C1 09 23 33 52 F0 15 62 72 D1 0A 16 24 34 E1 25 F1 17 18 19 1A 26 27 28 29 2A 35 36 37 38 39 3A 43 44 45 46 47 48 49 4A 53 54 55 56 57 58 59 5A 63 64 65 66 67 68 69 6A 73 74 75 76 77 78 79 7A 82 83 84 85 86 87 88 89 8A 92 93 94 95 96 97 98 99 9A A2 A3 A4 A5 A6 A7 A8 A9 AA B2 B3 B4 B5 B6 B7 B8 B9 BA C2 C3 C4 C5 C6 C7 C8 C9 CA D2 D3 D4 D5 D6 D7 D8 D9 DA E2 E3 E4 E5 E6 E7 E8 E9 EA F2 F3 F4 F5 F6 F7 F8 F9 FA FF DA 00 0C 03 01 00 02 11 03 11 00 3F 00 F6 B1 0A DA C0 AB 6D 69 33 49 04 0C B0 41 24 8C 41 F2 B8 4F 9F 25 54 B0 6E A7 E6 60 79 FB A4 09 1E 4B 6B C2 BE 6A 6E 92 0B 8F 94 2C 26 43 13 AB 00 0F 2B F2 92 AC 39 FE EB 12 0E 3E 6A 9A 44 56 BA 8E 36 BA 3B CB 34 AB 11 7D A7 68 5D A4 00 A4 12 01 60 79 DD 82 7B 7C B8 F3 ED 6F 58 F1 06 AF F1 14 78 5E D7 51 B6 D2 2D E3 88 4C AE F6 E9 70 66 60 43 A9 01 C7 51 80 70 31 82 A4 E4 E2 93 5A DA DB 8F 96 2A 3A F9 7F 5F D6 C7 A1 8B A4 DA 19 83 2A 19 0C 7B DD 76 0C EE 2A 06 1B 04 E4 80 01 1D 72 08 C8 34 5A DC 41 7B 0A DC C1 29 92 26 24 A3 0C 80 47 4C 8F 55 3D 41 E4 1C 82 38 C5 70 FE 10 F1 66 A3 71 AE 6A BE 1E D4 6F 6D 75 2B BB 65 F3 2D 2F 23 DB 1A 5C 29 00 80 76 82 07 50 78 07 03 3D 71 5D 99 B7 8A 39 04 16 B6 D1 C7 FB B4 8E 46 55 31 ED 88 06 DA AA CA 39 20 E4 00 08 DB BB 3E 80 BD 2C 4D F5 6B B0 40 B0 C4 1E 43 70 D3 4F 6E AC B3 88 E4 76 01 8E 1C 8F 2F 73 60 F2 08 1C 90 08 03 8A E5 BC 69 E2 8D 33 C3 B6 62 D6 EA E2 CE 1B FD AB 24 0C F1 80 12 2F 30 1F 94 61 CE E1 B1 40 C2 B7 CD B5 B6 ED 56 DB D6 09 4D B0 29 22 4A 63 53 B6 32 11 9C 90 10 1C 9C 64 9E 8C 32 70 49 C0 E7 20 9F 09 BD 9A F2 DF C5 DE 29 87 58 F0 85 CE B1 AB 5F 4A 63 D3 E4 9E 11 24 50 44 4B 24 6C 0B 02 02 EE 31 80 46 3B 8C 82 30 5D 38 F3 3B 76 FB D9 A5 38 5D 73 7F C0 FC CE 9F E2 2E B8 97 FE 07 93 5F D1 B5 2B DB 79 22 75 11 5E D8 5C 88 92 65 66 5C 46 E1 5C B1 20 3B 70 40 21 91 CE 14 30 DD B3 E0 89 F5 0D 4B C3 BA 67 F6 89 8D F5 51 0C 77 36 D7 37 0C 5E 46 89 82 A4 8E 8C C1 BE 60 A7 9C 63 E6 60 18 60 87 93 CC 35 3D 43 4E D1 7E 13 B7 84 A5 BC 56 D4 98 A5 D3 85 6D C0 48 5D 1B CA C0 E4 61 4F 3B B1 F3 29 15 D8 C5 F0 EB C3 DE 1F F0 7C 7A D5 A4 97 37 17 B2 5B A1 13 C9 21 DA DB CC 67 85 C0 C0 00 1C 64 67 0C 73 9E 31 D1 C8 92 71 BF 5D 3E E3 6E 5D 22 9E FA 9E 99 6E 48 B3 97 ED 0F 78 CA BB 64 49 1F 89 65 55 44 25 82 20 04 7C D9 05 36 8C 9C F1 83 8A 99 A3 44 BB 9A 4B 63 11 BA 11 96 68 72 AA 1C B6 02 B3 90 A5 87 FA BD B9 E9 81 D0 E0 63 1E C6 6B 79 F4 F8 BC 3F 7E 25 B4 9E 68 36 22 AB B0 17 11 05 5C 98 DF 03 07 69 C3 28 C3 29 DC 47 67 3A 2D 1B CB 7C 61 B8 12 6F 64 69 13 E4 2F 6E 76 C8 0A 13 FD D7 5F 94 E3 2B BB 71 C6 ED BF 2F 3B B2 D3 FA D0 E6 52 30 34 E3 79 7B E2 FF 00 11 48 31 73 15 AB 47 F6 39 89 2D 1C 33 08 99 4A 2A EE 00 9C 48 C1 86 E1 CF 5C 67 8E 81 62 92 3B AB 65 9E D0 34 68 E4 C0 6D C6 12 D9 76 05 01 B2 D9 62 72 D8 21 70 01 C1 C6 37 36 07 82 25 91 F4 FF 00 ED 14 8B 29 AC 5E DC 5D 48 A5 9C F9 03 24 05 18 52 A7 95 EA 76 75 EF 8C 56 CE B9 E2 1D 2F 42 D2 A5 D4 35 09 A3 85 57 7C 71 AC E0 A9 91 81 C6 D0 30 58 82 47 50 0F 1C F2 28 6A C6 30 7F BB BB F3 7F A9 6E 18 E7 9B 4E F2 DD 1A DA 57 66 F3 15 A5 69 30 0B 9D DB 5C 30 38 23 3B 4F 05 41 5F 94 63 6D 41 66 97 B0 42 81 FC A8 8C D3 8D B1 CD 33 CA C9 10 50 31 92 4E E9 08 4C 9C 10 01 62 7E 72 A4 BE 2F 86 FC 67 E1 DD 7E EE 58 34 BD 56 46 BD 67 6F DC DD 6F 05 91 64 73 94 52 42 F2 09 23 1F 30 5D BB 87 CB 81 CF 7C 52 F1 7E A9 E1 F1 A6 B5 94 8F 6A 0D C3 89 6D E4 2B 9B 84 42 A5 5C 14 6D CA 84 E4 75 52 79 04 50 E3 AF A9 A7 B4 8D 9B 4F 44 7A 0E D8 99 FF 00 B4 26 68 11 A1 F3 11 A4 12 96 55 8C 16 CF 3C 05 3C 02 DC 75 5C 1C ED 04 02 C9 26 B7 8E DA 58 E3 FB 3A 67 11 AC 2A 23 68 C8 65 11 95 6C 91 85 23 38 C6 7E 84 AD 63 DA 78 AA 6D 4E CA D2 EB 4E D1 B5 3B 81 24 6B 24 C9 F6 51 16 CD CA 48 01 A6 78 C3 73 8E 53 70 C2 9C E3 70 35 64 5D EB F7 31 84 64 D2 F4 99 99 7C D4 59 A4 6B B7 28 3E F0 64 5F 2C 29 05 93 24 3B 01 9C 73 90 68 77 BE FB 15 15 2D FF 00 AF F8 06 BD BF C9 6C 09 89 22 6E 59 D2 30 48 0C 4E 5B 1C 02 79 CF 38 19 EB DE A9 B5 E5 95 9E 97 73 24 B7 C9 69 69 0B B4 7F 6A 92 58 C2 21 CE 38 27 2A 36 B1 D8 03 0E 0A E3 07 BE 0B 59 DB EB 5A 33 5D 5D F8 9E 4D 42 2C 86 CD AC 80 5A C1 38 C3 06 CC 1B 64 D8 87 9C 3C 9C 0C 12 72 03 09 B4 D5 F0 DD 96 A5 6D 0D BC 96 97 1A AA 91 1B B4 AE B3 DF 23 15 3B 8C 8C 58 BF 40 A0 F5 DA 07 65 1F 28 AC B4 EA 55 A2 BD E1 F7 9A E5 A6 A9 6F 25 B5 89 BE D4 3C D4 75 8E 4D 36 DB 31 96 C8 2A E9 3B 8F 27 72 60 F3 E6 01 B9 48 C1 6C 00 C8 75 7D 4F 55 BC 8E E2 CA C6 CA 08 D2 59 6C 99 E7 94 CF 3C 0F 8F 98 3C 71 29 55 C3 22 9C 79 A0 15 C6 48 24 63 54 3C 7A 8D CB D9 5E 05 90 24 82 74 45 0E 85 44 72 83 1B 30 23 8F 9D 4E 32 D8 71 1E 46 41 20 71 BE 27 B5 9B C1 5A 91 D7 6D 9E EE 5D 0A F2 45 8F 51 B2 86 57 46 8D C8 03 CE 56 52 30 49 51 92 48 2C 4E 09 3B FE 59 9C 9C 56 BF F0 DE 66 72 A9 EC D7 32 5A 75 EE 6E D9 25 DE AA F7 76 D7 1E 26 B9 96 E2 C9 84 37 56 B6 36 AB 69 B8 63 3F 36 F0 D2 02 C3 38 74 75 04 63 6E 08 26 AE 5A 68 7A 65 B4 8D A8 43 E7 99 23 70 04 F7 CC D2 49 10 46 65 6D B2 4C 19 C2 90 58 75 DA 41 25 71 B8 B1 E6 FC 27 AF 69 D7 9E 3A D7 EE A0 BB 86 58 75 0B 38 2F 04 91 B1 DB 12 C4 0C 4E AF B8 29 53 93 91 91 CA E0 F1 9A ED A2 93 C9 B7 83 ED EF 6F 04 FF 00 29 22 39 48 46 76 C2 9C 67 19 05 DF 03 3D 49 5E E6 95 37 CC B4 15 3A BC D1 DF A9 9F 69 A7 A5 B5 B2 45 18 59 A2 68 D2 D6 7B 98 AE 24 57 F9 5A 40 42 AA 0F 94 2B B6 30 0A 85 0C DF 74 20 07 55 D9 ED E0 61 18 42 15 02 C5 E6 CC 46 E7 E8 03 31 04 F2 76 8C F2 4E 7A 7A C9 30 91 A0 90 44 C1 64 2A 42 B1 19 00 E3 83 8A F2 9F 01 78 E6 E3 5C F0 F5 CF FC 26 97 FA 1C 29 15 C8 86 09 EF 1A 34 37 05 4E 64 05 0B 05 C8 0C A0 10 06 37 03 83 DC DB DD 5D BA FD C6 96 6F DF DC F5 78 E3 8E 25 2B 1A 2A 29 62 C4 28 C0 C9 39 27 EA 49 27 F1 A2 B0 6C ED 34 8F 14 DA A6 AB 79 61 A3 EA 09 2F 16 D3 2C 6B 3F EE 87 40 59 94 1C E7 76 46 38 27 1D 41 34 56 C9 53 B6 AD DF D1 7F 99 2F 9A E7 39 A9 78 56 EB 45 D6 75 2D 73 4D F1 34 DA 2C 17 19 79 23 F2 05 D2 C8 70 5D 88 43 8C 11 87 38 01 88 19 20 E0 90 38 AD 5B 5E 83 56 F2 20 D5 8F 87 75 BB 78 94 C7 6B 71 1C 92 59 4E 73 C2 EE E8 10 0E 32 08 DA 3D 7B D7 AB EA F7 76 9A 96 87 A8 5B DA 6A 8B 6D 72 EA 70 5F 96 56 50 38 F2 DF AA 9C 60 AE 30 72 7D 4D 78 BC 76 DA F6 AD E1 27 B8 B6 F0 CE 91 25 BD CB B6 6E D2 D3 13 2E 38 24 39 38 03 23 EB 9F C6 B0 49 2D 2D FD 7A 8E F7 77 66 FF 00 83 75 DD 2F 40 BC 83 49 3E 1A BA B4 6B B9 62 46 BF 8E E1 6E CC AE AE 0A 83 F2 ED DB 9C 83 B7 91 DB 9E 47 AB 9B A8 21 B0 82 E2 C5 A3 74 92 42 63 8C 32 C4 2E 4B 12 58 A9 23 05 8F CC C3 18 0C 79 CE 0E 6B 85 F0 3E 89 24 76 D0 41 6D 6E A9 02 5B 14 9D E7 4D EA EE E9 F3 2B 80 70 C0 31 C6 01 FB A0 73 DE BB D8 BC CB F9 A1 9A 58 A6 82 24 45 93 EC F3 26 0E F2 01 53 95 6C 71 96 05 4E 7E 60 A4 63 00 B6 D2 42 8A BC 47 AD AE 9F 6F 0C 76 9F 67 89 61 52 85 55 A3 F9 77 21 40 9C 91 8D C0 84 C7 7C 81 8E 9C 71 FF 00 11 E7 BA 8B C2 51 69 D2 5B 3D EB 5C 49 E5 C8 82 7F 2C CC 8A 47 57 0A A1 4B 0C 12 31 8C E4 0C 81 5D 8C 22 5B 76 58 7F 7B 3C 87 69 90 F2 23 52 4B 16 60 58 93 D7 23 68 2D 8F 94 60 0E 69 2F 6C ED 75 4B 24 B7 BE B5 2C 92 E0 F9 6E 32 51 B1 9E AB 90 08 E4 64 1C 76 CF 34 94 AC EE CB A7 35 19 26 CF 99 D2 5B 1B CF 0A C3 6E F7 96 36 36 B2 C9 98 74 CD 3E DC 4F 79 71 22 36 D1 E7 B9 F9 83 1D E4 A9 FB A7 2C 00 5E 05 7B 8F 88 2D A4 B7 F0 76 9B A4 2A C8 D7 12 79 51 8F 91 B0 48 C6 41 38 C0 39 23 83 EF 8E 87 0D D1 BC 35 69 A5 6A 77 27 45 F0 9D 8E 9F 24 72 FC B7 D7 4D E6 37 20 67 60 E4 E0 A9 3C 2B 05 EA 3A E4 52 EB 1F 6D 93 55 B7 B5 83 53 9E 6B AF 31 7E D4 2C D0 C6 22 8F 9D A0 EC 0C D8 EB D5 8E 33 9C 73 5D 2E 7C CD 25 DE FF 00 D7 F5 F7 1B 39 7B C9 AE 97 30 3C 63 A5 47 6D A8 E9 37 16 57 D3 5B 5C 3D D9 B6 96 EA 57 74 9D 98 E0 6E 50 42 8D 8A 38 DC A4 0F 98 0F 7A D6 F1 57 89 D7 48 D3 6E F4 6B F9 C5 C5 D1 8C AC 53 40 C8 5D CE 01 4F 36 3E 83 D1 C1 1B 58 10 54 10 CC A8 CB FD 36 5D 7F C7 EE 20 92 0B 73 A3 18 EE 5A 26 FF 00 97 89 89 04 1D C0 64 02 89 10 27 07 69 18 00 F5 3B 7A 47 87 BC 3C FA 74 83 4E D3 96 2B 79 F0 EB 3A B1 67 75 0F 95 64 93 24 85 3B 15 D4 AB 64 64 30 DA 70 6B 96 6E FB 9E 6F 2C A6 E7 CB B3 7E BB 2F 97 E8 73 FE 10 6D 7A 0B 0B 34 93 40 91 A2 B5 49 21 B7 8B 6B 41 E5 CA C4 66 49 4C AC 0E 39 3F 34 68 FC 3B 81 DD 4F 3B F1 03 4D F1 3C FE 2B D3 F5 47 D1 E1 D4 63 B5 B7 44 7B 5B 36 FB 4E C2 DB B2 DB 19 32 32 43 6D 66 46 5C A0 CE 79 5A ED A0 D6 E1 D2 F5 77 83 51 37 73 5E 79 86 24 BD B5 86 5B 98 EE 54 67 11 BA C6 B8 59 82 A1 62 A0 00 0E E6 18 0E CB 52 EA 5E 32 D3 60 B7 2D 16 A9 A7 BD D2 39 11 DB FD A3 64 BB 8F CA AA F1 12 18 11 B8 E5 5B BA F4 04 FC A3 93 BD FB 1A D4 51 8C 3D 9B 7A 2B 7F 99 C2 E8 BE 1B D7 7C 51 E2 C8 3C 47 E2 3B 6B CD 36 C6 CE 03 99 26 90 43 71 21 5C F5 31 88 D9 7A 9F 9B 68 F9 54 0E 7A D7 33 63 AB F8 7B 54 F8 9E BA 95 CB 43 A5 E9 B1 4A AB 66 91 DB A8 40 50 61 0B 80 30 17 E5 19 EE 37 0C 60 0D CB DA 78 AE DE EE F7 45 B3 D3 ED 83 DE 6B DA DB 14 CC CA DF B9 84 1F 9C E0 FD D5 1C 7C D8 C1 04 E3 8A B7 A1 F8 57 47 D3 B5 9B 0D 1E 2B 38 6F 92 18 9B ED 46 EE DC 36 64 05 B7 30 0C 38 E7 18 3E 98 C1 20 E4 E9 07 67 E9 7F F8 3F D7 73 9A 6E 52 5E B6 6F F4 D3 F1 B2 E8 76 F0 D8 91 67 12 C3 6F 65 02 41 97 80 79 01 42 4B 96 0D 22 85 62 02 B8 62 78 20 80 C7 39 DC 40 C0 B9 F1 64 AB E1 7D 53 54 BA 8B CB FB 0C 67 12 C2 C3 CB 95 B2 30 9B 5B 3F 7B 81 9C 13 86 38 20 9A C6 F1 EF 88 75 ED 32 E6 CA C7 4A B2 FB 65 84 B0 23 08 D4 4C 2E 81 60 C8 03 64 FC D9 F4 2A C4 1E A0 1D A6 B3 8F 89 6C 2E 3C 3A BA 4F 88 7C 1F E2 08 A3 11 02 F1 0B 67 08 1B 7E 77 B3 6E 52 46 42 7F 0F 1C 8E 41 AC ED EE B6 FA FF 00 5F E6 77 2E 79 55 8B E9 D7 FA FC CE 63 C2 5A 3F 8A 74 3F 0B 4F E2 9B 3B AD 32 D7 4D 76 49 E5 8E F0 39 32 88 98 95 F9 42 9E 0B 74 C1 07 81 C8 1D 77 BE 1B E9 B3 78 83 C4 DA 97 8C F5 66 8D 04 CE 63 5B 50 CA 37 89 7E 5F 98 1F E0 C7 03 3F 78 8E E4 73 C7 78 2E 4F 06 CC FA 8D C7 8C 44 4C EC 41 B7 82 24 96 32 5B 9C E3 CB C2 28 E8 06 48 FC 05 7B A6 A9 7D 6F A6 78 6E DE E7 4F BC 87 CB 58 52 2B 37 B7 0A E1 86 39 C3 31 6C AE 02 FB FC BD 4D 6B 3D 1D ED BE 86 92 E6 72 E5 5D 5E BF D7 EA 6B C8 E5 5D 9A DE F2 34 8A 02 DF 68 F3 5B 78 52 59 5C 82 0F 23 E4 2D 83 B8 05 0C 0E D6 18 C4 33 D8 C4 9A 35 CC 33 A7 EE A7 63 24 B1 86 52 91 16 20 BF 27 6E E4 DC 59 98 37 50 58 63 04 2D 26 9D 7F 70 D6 B6 F3 6A 17 16 51 86 5D 8C 15 B0 4C BB C2 05 EB 80 41 C2 91 C9 2C D8 E3 1C BA 59 EC 22 88 45 14 09 25 BE F9 26 6F 22 D9 A5 51 22 4A A5 B8 45 23 7E F2 4F 5D DB 81 38 38 6C 62 FB 11 7B 68 F6 3C A7 47 D0 2F FC 19 F1 66 D3 4F D2 EE A2 9A DA EF 72 17 95 73 FB AC 6F 78 D8 81 C4 81 42 91 8F 54 27 01 B1 5E B9 25 93 3C 12 25 AD CB DB 20 87 C9 86 31 0A 79 70 B2 93 B5 C2 95 C9 23 8C 0C ED C0 18 1D EB 8A F1 88 D3 D3 58 F0 FE A3 67 75 13 18 F5 54 D3 E6 89 16 36 85 15 D4 A4 CA FF 00 2F 0C 50 22 90 C7 80 A3 00 72 6B BC 59 6D D6 F1 ED D0 A8 B8 65 12 C8 AA 39 C7 DD 0C DF 5C 60 67 AE D3 8F BA 71 85 38 5A F1 E8 73 51 8A A7 29 46 1B 5C F2 9D 03 56 F8 83 6F AA DC 69 17 4B A1 EA BF D9 D0 16 16 76 B7 B1 DB 48 FB 5C 2A 01 B0 7C B8 F2 C9 DA 42 8C 49 86 EC A3 13 CB 7D 6A F3 54 93 5A F8 6A B7 56 B0 CC 5E 71 62 61 6B 90 E7 77 02 48 4C 6C F9 6E BC 37 1C F6 E6 E7 8A 34 AD 63 4C F8 9C 3C 55 1F 84 AE B5 B8 AC E1 41 B6 09 58 16 9B 24 2C 98 D8 4B 60 60 E1 01 0B C6 48 C6 2B 73 C3 1A 9C D3 45 34 D2 69 5A CF 86 E6 59 91 67 00 FD A6 20 A5 37 B3 C8 A5 40 8D 88 DA 30 54 6D DC 84 0D BB AB 44 9C B5 96 FA FE 7A 7E 07 7B 76 7A 75 B1 7B C2 1E 30 F0 8E 9B A2 1B 0B 3B 5B CD 1A 2B 69 DE 3F B2 DF 5B EC 94 1C E4 E7 04 E7 19 DB 92 73 F2 F3 EB 45 6D 5B 6A 91 41 17 D9 A0 F1 56 99 24 70 62 30 F7 6B BE 46 18 04 65 FC C5 0E 70 47 CC 07 5C E7 9C D1 5A AA 90 B6 B1 6F E6 66 E0 9F 57 F8 7F 91 73 53 82 3F ED 01 77 3E 94 2F 63 45 31 EE 48 F7 30 04 29 C1 42 70 DC 8C 86 E7 03 23 03 24 9E 46 EF E1 D7 83 6E 6F D6 F5 4D DE 97 21 F9 C4 2E 4C 51 17 CE 7A 38 F5 23 85 60 38 E3 15 AF 3A 88 7C 51 6F A6 DA 83 69 6E 9B 61 C5 BB 14 2C A1 01 19 20 F3 8C E0 7B 54 9E 21 D4 6E B4 9B A1 0A C8 6E 3C D8 B3 BE 7F BD 1F DE 19 5D BB 40 38 27 9C 67 DF 81 52 F4 D7 A9 9B 5D 19 6E 5D 32 E6 68 A5 8A DA EA 1B E8 AE 43 34 82 E6 69 19 4A 30 61 83 87 C6 08 66 E0 28 5E 3B 60 54 D6 2F 71 0D 95 BE 9D 76 8F 66 62 85 63 8F CB 61 24 92 60 A2 87 5D BD 02 92 37 65 4A FC C3 27 19 14 9E 14 B7 89 BC 37 13 04 01 E5 67 66 6C 02 72 AE 40 3C F7 18 15 B6 D0 C6 E5 C9 5C 33 A8 56 65 3B 58 81 9C 0C 8E 7B 9F CE A9 DD 3B 36 3D 52 29 DB DB 0B 38 7E CE B2 C5 6A B3 3C A1 15 31 9D C4 96 5D 99 01 41 0A 09 2B B4 F3 92 49 C1 2C EF B3 DB 49 76 E9 1B 34 13 C4 55 C8 8E 40 32 AC 49 27 6E 48 C3 1D E0 92 01 25 49 EA AA 45 99 30 F2 08 1D 15 E3 92 36 DC AC 32 08 E0 63 E9 C9 A8 DF 4E B2 79 25 91 AD 61 F3 25 78 DE 47 08 03 3B 21 05 0B 1E A7 69 03 19 E9 51 BE FA 8B 7B 9C 46 9F A6 46 BE 28 D5 18 69 77 37 16 CD BD 23 68 9B CC 46 6D DF 36 E6 72 15 8E E0 49 0D 9C 30 3D 48 AD EC EA 52 58 46 F6 62 DF 47 D3 DD 15 A3 02 D5 9E 64 2C C3 EF 47 80 13 AF CD 9C ED E4 93 D4 8D A8 2C E2 82 34 41 B9 C2 46 91 8D DD 30 BD 08 51 85 07 9E C0 76 F4 18 82 DB 1A 8D AD C7 DA 95 65 86 46 96 DD A0 64 06 32 AB 23 A7 20 8E 72 30 08 27 1C 74 1C E7 59 54 BB 57 2E 55 2F 25 7F EA C4 3A 5D 9D B6 92 05 A0 BB 43 73 23 BC B3 29 2A 1A 79 24 62 DB C8 C6 73 F2 3E 00 E0 00 47 3B 46 08 EC 60 BE BA B7 BA 91 A7 8E E7 4F 9A 54 01 67 20 ED 6F E1 7C 31 CA 91 E5 BE 09 EC A4 81 F7 6B 42 1B 74 88 87 3F BC 9C C6 B1 BC EC AA 1E 40 B9 C6 E2 00 EE 58 E0 71 C9 C0 19 A6 A4 CC D7 F3 40 40 DA 91 23 83 DF 2C 5C 1F FD 04 52 4D BB B4 44 DC 74 4C A5 79 A7 47 3E 97 0D AC A8 16 34 45 55 44 F3 1F 64 9C 04 6C A9 0C 42 9E 72 70 41 01 B2 A5 73 55 D6 C5 75 CF 0D FD 86 FE ED 2F 95 C2 87 B9 8E 20 8B 3A 70 C1 86 41 53 94 23 25 7E 52 49 C6 3A 0D 58 94 1B BB 89 32 DB 86 D8 F1 BD B6 E0 0D C3 E5 CE 01 CB 1C 90 32 78 CE 70 29 AD 0A CB 77 28 63 20 CC 4A A7 6C AC A3 04 B7 40 0E 01 F7 1C FB D6 6F 57 76 0A CD 24 B6 39 66 F0 44 16 F0 34 76 A6 DF 4F C3 AC 71 DC E9 F6 CD 15 C3 29 23 87 78 99 49 19 3C 83 95 F9 43 1C 11 C6 8E 95 A0 5B 78 76 5B 9B 88 B5 4B B9 54 A6 F9 52 E9 92 4C 0C E4 B6 ED BE 61 3C 37 56 23 93 C7 02 9F 61 27 DB 61 D0 F5 79 57 17 57 70 A7 99 E5 BB 2A E1 A1 2F 8D A0 E1 80 39 C6 ED D8 DC D8 C6 6A 8F 8B 6E 2E 1B 44 B3 91 66 64 4B 8C 24 B1 A8 1B 5B 20 36 7A 67 8C 63 AE 39 39 CF 18 B9 5D 4B 92 FA BF F3 B7 E6 44 E9 C2 9C 79 ED B1 57 4D 17 1A CD E5 EE BC 2F 22 B6 58 89 8A 36 91 03 F9 2B 8C E4 E5 80 18 53 9E 73 D4 D4 1A A6 99 AA A4 45 2F EE 63 6D 3B 4F 88 31 B8 B8 95 F1 32 A8 38 2C 32 C4 B7 24 1E 37 1F 52 48 35 9E 92 DC E8 96 C6 E6 C6 EE 58 DD EE 2E 60 20 ED 2B B2 3B 89 11 78 23 1C 01 D7 EB EA 68 B8 F1 8E AF 73 19 8C BC 51 A1 89 A3 60 89 8D D9 C7 CD 9E A0 8C 10 31 81 F3 1E 3A 62 92 6F E1 E9 FA 15 46 97 54 F5 FF 00 33 5F 50 B9 D1 3C 46 8B 6D 7F A3 C5 3A 5A 33 2C D3 3C 1E 6A 43 19 04 1D 85 72 DC E3 83 F2 E3 1B BF 84 03 0E 8D 06 99 A8 78 91 26 82 DE 0B 5B 08 49 8E D2 28 E1 08 B2 3A 80 D8 E0 63 38 3B B0 79 C0 E3 21 5B 1D 4F 87 EC E2 B3 D1 6D 56 10 40 78 D5 DB 3D D8 8C 93 FE 7D 05 1A BD CC 9A 66 95 11 B7 3F 31 B8 B6 B6 0C E4 B9 0B 24 C9 19 39 3D 58 06 24 13 9E 7A E6 92 76 76 46 C9 A9 69 11 92 C5 2A 87 8E 59 21 37 51 5A B0 8F 50 96 13 90 AC DF 30 62 BB 40 E1 50 9D AC 32 46 70 A0 0A 7C 7F 69 BB 2C 67 59 ED C4 E8 0A 41 22 21 36 C5 73 F3 64 07 52 FB 8A 91 96 23 00 60 65 5B 73 2F AC EC F4 BD 3E EF 51 B5 B4 85 6E AD ED A4 29 21 5F 9B 68 26 4D 9B BA EC DD CE DC E3 B5 50 B7 D4 DA 5D 1A 7D 40 DA DA 89 E6 D5 5A C6 5C 47 C4 91 AD E1 B7 05 B9 C9 3B 07 7E 33 DB 1C 56 37 77 D4 85 7D A2 70 3A 8A C8 BE 0A F1 A5 DA B1 47 B5 F1 0C B2 40 EA 46 E4 93 CE 88 6F 07 19 56 03 20 10 7F 89 B2 3A 57 AC 28 9E 1F DC 8D AB 18 FD DC 24 97 95 9B E5 04 33 93 D3 90 C0 E4 9C FC A7 70 27 15 CC 78 0A 35 33 78 96 5C B6 E5 D7 AF 14 00 C7 18 3B 3A 8E 84 F0 39 EA 39 C7 53 5B 57 8E B0 EB 16 F6 C6 18 E5 5B 94 33 48 D2 82 C7 74 72 42 A9 8C 9C 00 37 96 C0 1F 7B 9E B9 CA A6 97 2D EF D3 FC CC 70 D4 DC 95 D7 6F C9 B3 9F 86 E6 E2 F3 E2 24 D1 DB 49 79 1D AC 0D FB E8 92 5F 95 DC 21 F9 8A 13 80 BF 28 19 00 F2 57 D7 23 B2 0A D2 4A 0C D0 C5 FB B6 2D 1B 67 71 1C 60 1E 47 07 05 87 F8 E6 A1 9F 4A B2 B9 10 24 D6 D0 C9 04 28 55 20 78 51 90 74 C1 00 8E 30 06 06 30 39 3D 78 C5 09 3C 31 6C 43 2D BD F6 A5 66 84 92 B1 DB 5D 32 22 67 1C 2A F4 03 83 C7 FB 47 DB 17 AA 48 D9 D9 B3 6E 8A CE 8F 44 B1 F2 22 8E EA 25 BE 92 35 DA 27 BC 45 92 42 32 4E 0B 11 EF 45 56 83 5C 9D 6E 7F FF D9";
            }
            mplew.write(HexTool.getByteArrayFromHexString(packet));
        } else if (type == 1) {
            mplew.write(HexTool.getByteArrayFromHexString("FC FA 23 7B 0E 00 00 00 84 A5 36 08 84 A5 36 08 84 A5 36 08 01 01"));
        } else if (type == 2) {
            mplew.write(HexTool.getByteArrayFromHexString("A8 6E BE 49 0E 00 00 00 2E 44 C6 3A 26 44 C6 3A 38 44 C6 3A C3 C3"));
        } else if (type == 3) {
            mplew.write(HexTool.getByteArrayFromHexString("DC 8F 75 12 14 00 00 00 95 33 93 61 93 33 93 61 92 33 93 61 8D 33 2D 54 D3 F7 D1 24"));
        }
        return mplew.getPacket();
    }

    public static byte[] LieDetectorMulti(int senderid, int recverid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR_MULTI.getValue());
        mplew.writeInt(recverid);
        mplew.write(1);
        mplew.writeInt(senderid);
        return mplew.getPacket();
    }

    public static byte[] report(int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REPORT_RESPONSE.getValue());
        mplew.write(mode);
        if (mode == 2) {
            mplew.write(0);
            mplew.writeInt(1);
            mplew.writeInt(0);
        }
        return mplew.getPacket();
    }

    public static byte[] OnSetClaimSvrAvailableTime(int from, int to) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(4);
        mplew.writeShort(SendPacketOpcode.REPORT_TIME.getValue());
        mplew.write(from);
        mplew.write(to);
        return mplew.getPacket();
    }

    public static byte[] OnCoreEnforcementResult(int nSlot, int maxLevel, int currentlevel, int afterlevel) {
        MaplePacketLittleEndianWriter p = new MaplePacketLittleEndianWriter();
        p.writeShort(SendPacketOpcode.ENFORCE_CORE.getValue());
        p.writeInt(nSlot);
        p.writeInt(maxLevel);
        p.writeInt(currentlevel);
        p.writeInt(afterlevel);
        return p.getPacket();
    }

    public static byte[] OnClaimSvrStatusChanged(boolean enable) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
        mplew.writeShort(SendPacketOpcode.REPORT_STATUS.getValue());
        mplew.write(enable ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] updateMount(MapleCharacter chr, boolean levelup) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_MOUNT.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getMount().getLevel());
        mplew.writeInt(chr.getMount().getExp());
        mplew.writeInt(chr.getMount().getFatigue());
        mplew.write(levelup ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] getShowQuestCompletion(int id) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_QUEST_COMPLETION.getValue());
        mplew.writeInt(id);
        return mplew.getPacket();
    }

    public static byte[] useSkillBook(MapleCharacter chr, int skillid, int maxlevel, boolean canuse, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.USE_SKILL_BOOK.getValue());
        mplew.write(0);
        mplew.writeInt(chr.getId());
        mplew.write(1);
        mplew.writeInt(skillid);
        mplew.writeInt(maxlevel);
        mplew.write(canuse ? 1 : 0);
        mplew.write(success ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] useAPSPReset(boolean spReset, int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(spReset ? SendPacketOpcode.SP_RESET.getValue() : SendPacketOpcode.AP_RESET.getValue());
        mplew.write(1);
        mplew.writeInt(cid);
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] expandCharacterSlots(int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.EXPAND_CHARACTER_SLOTS.getValue());
        mplew.writeInt(mode);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] finishedGather(int type) {
        return gatherSortItem(true, type);
    }

    public static byte[] finishedSort(int type) {
        return gatherSortItem(false, type);
    }

    public static byte[] gatherSortItem(boolean gather, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(gather ? SendPacketOpcode.FINISH_GATHER.getValue() : SendPacketOpcode.FINISH_SORT.getValue());
        mplew.write(true);
        mplew.write(type);
        return mplew.getPacket();
    }

    public static byte[] updateGender(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_GENDER.getValue());
        mplew.write(chr.getGender());
        return mplew.getPacket();
    }

    public static byte[] updateDamageSkin(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_DAMAGE_SKIN.getValue());
        mplew.write(2);
        mplew.write(4);
        DamageSkinInfo(chr, mplew);
        return mplew.getPacket();
    }

    public static byte[] updateDailyGift(String key) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(34);
        mplew.writeInt(15);
        mplew.writeMapleAsciiString(key);
        return mplew.getPacket();
    }

    public static void DamageSkinInfo(MapleCharacter chr, MaplePacketLittleEndianWriter mplew) {
        int skinid = (int) chr.getKeyValue(7293, "damage_skin");
        mplew.write(1);
        int skinnum = GameConstants.getDSkinNum(skinid);
        mplew.writeInt(skinnum);
        mplew.writeInt(skinid);
        mplew.write(0);
        mplew.writeMapleAsciiString(MapleItemInformationProvider.getInstance().getName(skinid) + "이다.\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n");
        mplew.writeInt(0);
        mplew.writeInt(-1);
        mplew.writeInt(0);
        mplew.write(1);
        mplew.writeMapleAsciiString("");
        mplew.writeInt(0);
        mplew.writeInt(-1);
        mplew.writeInt(0);
        mplew.write(1);
        mplew.writeMapleAsciiString("");
        mplew.writeInt(0);
        int skinroom = (int) chr.getKeyValue(13191, "skinroom");
        int skinsize = (int) chr.getKeyValue(13191, "skins");
        mplew.writeShort((skinroom == -1) ? 0 : skinroom);
        mplew.writeShort((skinsize == -1) ? 0 : skinsize);
        for (int i = 0; i < skinsize; i++) {
            int skin = (int) chr.getKeyValue(13191, i + "");
            mplew.writeInt(skin);
            mplew.writeInt(GameConstants.getItemIdbyNum(skin));
            mplew.write(0);
            mplew.writeMapleAsciiString(MapleItemInformationProvider.getInstance().getName(GameConstants.getItemIdbyNum(skin)) + "이다.\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n");
            mplew.writeInt(0);
        }
    }

    public static byte[] charInfo(MapleCharacter chr, boolean isSelf) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHAR_INFO.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getLevel());
        mplew.writeShort(chr.getJob());
        mplew.writeShort(chr.getSubcategory());
        mplew.write((chr.getStat()).pvpRank);
        mplew.writeInt(chr.getFame());
        if (chr.getMarriageId() <= 0) {
            mplew.write(0);
        } else {
            MarriageDataEntry data = MarriageManager.getInstance().getMarriage(chr.getMarriageId());
            if (data == null || data.getStatus() < 1) {
                mplew.write(0);
            } else if (data.getStatus() >= 2) {
                mplew.write(1);
                MapleRing ring = ((List<MapleRing>) chr.getRings(true).getRight()).listIterator().next();
                if (ring == null) {
                    mplew.writeZeroBytes(48);
                } else {
                    mplew.writeInt(chr.getMarriageId());
                    mplew.writeInt((data.getBrideId() == chr.getId()) ? data.getBrideId() : data.getGroomId());
                    mplew.writeInt((data.getBrideId() == chr.getId()) ? data.getGroomId() : data.getBrideId());
                    mplew.writeShort((data.getStatus() == 2) ? 3 : data.getStatus());
                    mplew.writeInt(ring.getItemId());
                    mplew.writeInt(ring.getItemId());
                    mplew.writeAsciiString((data.getBrideId() == chr.getId()) ? data.getBrideName() : data.getGroomName(), 13);
                    mplew.writeAsciiString((data.getBrideId() == chr.getId()) ? data.getGroomName() : data.getBrideName(), 13);
                }
            }
        }
        int prof = 0;
        if (chr.getSkillLevel(92010000) > 0) {
            prof++;
        }
        if (chr.getSkillLevel(92020000) > 0) {
            prof++;
        }
        if (chr.getSkillLevel(92030000) > 0) {
            prof++;
        }
        if (chr.getSkillLevel(92040000) > 0) {
            prof++;
        }
        mplew.write(prof);
        if (chr.getSkillLevel(92010000) > 0) {
            mplew.writeShort(9201);
        }
        if (chr.getSkillLevel(92020000) > 0) {
            mplew.writeShort(9202);
        }
        if (chr.getSkillLevel(92030000) > 0) {
            mplew.writeShort(9203);
        }
        if (chr.getSkillLevel(92040000) > 0) {
            mplew.writeShort(9204);
        }
        if (chr.getGuildId() <= 0) {
            mplew.writeMapleAsciiString("-");
            mplew.writeMapleAsciiString("");
        } else {
            MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
            if (gs != null) {
                mplew.writeMapleAsciiString(gs.getName());
                if (gs.getAllianceId() > 0) {
                    MapleGuildAlliance allianceName = World.Alliance.getAlliance(gs.getAllianceId());
                    if (allianceName != null) {
                        mplew.writeMapleAsciiString(allianceName.getName());
                    } else {
                        mplew.writeMapleAsciiString("");
                    }
                } else {
                    mplew.writeMapleAsciiString("");
                }
            } else {
                mplew.writeMapleAsciiString("-");
                mplew.writeMapleAsciiString("");
            }
        }
        mplew.write(isSelf ? -1 : 0);
        mplew.write(0);
        mplew.write(((chr.getPets()).length > 0) ? 1 : 0);
        Item inv = null;
        int peteqid = 0, petindex = 0, position = 114;
        for (MaplePet pet : chr.getPets()) {
            if (pet != null) {
                if (petindex >= 1) {
                    position = 123 + petindex;
                }
                inv = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -position);
                peteqid = (inv != null) ? inv.getItemId() : 0;
                mplew.write(true);
                mplew.writeInt(petindex++);
                mplew.writeInt(pet.getPetItemId());
                mplew.writeMapleAsciiString(pet.getName());
                mplew.write(pet.getLevel());
                mplew.writeShort(pet.getCloseness());
                mplew.write(100);
                mplew.writeShort(pet.getFlags());
                mplew.writeInt(peteqid);
                mplew.writeInt(pet.getColor());
            }
        }
        mplew.write(false);
        Item medal = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -21);
        mplew.writeInt((medal == null) ? 0 : medal.getItemId());
        List<Pair<Integer, Long>> medalQuests = chr.getCompletedMedals();
        mplew.writeShort(medalQuests.size());
        for (Pair<Integer, Long> x : medalQuests) {
            mplew.writeInt(((Integer) x.left).intValue());
            mplew.writeLong(((Long) x.right).longValue());
        }
        DamageSkinInfo(chr, mplew);
        for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {
            mplew.write(chr.getTrait(t).getLevel());
        }
        mplew.writeInt(chr.getAccountID());
        PacketHelper.addFarmInfo(mplew, chr.getClient(), 0);
        return mplew.getPacket();
    }

    public static byte[] spawnPortal(int townId, int targetId, int skillId, Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_PORTAL.getValue());
        mplew.writeInt(townId);
        mplew.writeInt(targetId);
        if (townId != 999999999 && targetId != 999999999) {
            mplew.writeInt(skillId);
            mplew.writePos(pos);
        }
        return mplew.getPacket();
    }

    public static byte[] echoMegaphone(String name, String message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ECHO_MESSAGE.getValue());
        mplew.write(0);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(message);
        return mplew.getPacket();
    }

    public static byte[] showQuestMsg(String name, String msg) {
        return serverNotice(5, name, msg);
    }

    public static byte[] Mulung_Pts(int recv, int total) {
        return showQuestMsg("", "You have received " + recv + " training points, for the accumulated total of " + total + " training points.");
    }

    public static byte[] serverMessage(String name, String message) {
        return serverMessage(4, 0, name, message, false);
    }

    public static byte[] serverNotice(int type, String name, String message) {
        return serverMessage(type, 0, name, message, false);
    }

    public static byte[] serverNotice(int type, int channel, String name, String message) {
        return serverMessage(type, channel, name, message, false);
    }

    public static byte[] serverNotice(int type, int channel, String name, String message, boolean smegaEar) {
        return serverMessage(type, channel, name, message, smegaEar);
    }

    public static byte[] serverMessage(int type, int channel, String name, String message, boolean megaEar) {
        return serverMessage(type, channel, name, message, megaEar, null);
    }

    public static byte[] serverMessage(int type, int channel, String name, String message, boolean megaEar, Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(type);
        int v6 = 1;
        if (type == 13 || type == 14 || type == 27) {
            v6 = 0;
        }
        if (type == 4 || type == 26) {
            v6 = 1;
            mplew.write(v6);
        }
        if (v6 == 1) {
            mplew.writeMapleAsciiString(message);
        }
        if (type == 3 || type == 23) {
            PacketHelper.ChatPacket(mplew, name, message);
            mplew.write(channel - 1);
            mplew.write(megaEar ? 1 : 0);
        } else if (type == 8) {
            PacketHelper.ChatPacket(mplew, name, message);
            mplew.write(channel - 1);
            mplew.write(megaEar ? 1 : 0);
            mplew.writeInt(0);
            mplew.write((item != null));
            if (item != null) {
                PacketHelper.addItemInfo(mplew, item);
                mplew.writeMapleAsciiString(MapleItemInformationProvider.getInstance().getName(item.getItemId()));
            }
        } else if (type == 9) {
            PacketHelper.ChatPacket(mplew, name, message);
            mplew.write(channel - 1);
        } else if (type == 10) {
            PacketHelper.ChatPacket(mplew, name, message);
            mplew.write(0);
            mplew.write(channel - 1);
            mplew.write(megaEar ? 1 : 0);
        } else if (type == 12) {
            mplew.writeInt(channel);
        } else if (type == 24) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(false);
        } else if (type == 17) {
            mplew.write((item != null));
            if (item != null) {
                PacketHelper.addItemInfo(mplew, item);
            }
        }
        if (type != 21) {
            if (type == 22) {
                PacketHelper.addItemInfo(mplew, item);
            } else if (type == 26) {
                mplew.writeInt(channel - 1);
                mplew.writeInt(megaEar ? 1 : 0);
            }
        } else {
            PacketHelper.addItemInfo(mplew, item);
        }
        switch (type) {
            case 2:
                PacketHelper.ChatPacket(mplew, name, message);
                break;
            case 6:
            case 18:
                mplew.writeInt((channel >= 1000000 && channel < 6000000) ? channel : 0);
                break;
            case 7:
                mplew.writeInt(channel);
                break;
            case 11:
                mplew.writeInt(channel);
                mplew.write((item != null));
                if (item != null) {
                    PacketHelper.addItemInfo(mplew, item);
                }
                break;
            case 16:
                mplew.writeInt(channel);
                break;
            case 20:
                mplew.writeInt(channel);
                mplew.writeInt(0);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] getGachaponMega(String name, String message, Item item, byte rareness, String gacha) {
        return getGachaponMega(name, message, item, rareness, false, gacha);
    }

    public static byte[] getGachaponMega(String name, String message, Item item, byte rareness, boolean dragon, String gacha) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(13);
        mplew.writeMapleAsciiString(name + message);
        if (!dragon) {
            mplew.writeInt(0);
            mplew.writeInt(item.getItemId());
        }
        mplew.writeMapleAsciiString(gacha);
        PacketHelper.addItemInfo(mplew, item);
        return mplew.getPacket();
    }

    public static byte[] getAniMsg(int questID, int time, String name, String text) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(23);
        PacketHelper.ChatPacket(mplew, name, text);
        mplew.writeInt(questID);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] tripleSmega(String name, List<String> message, boolean ear, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(10);
        if (message.get(0) != null) {
            mplew.writeMapleAsciiString(message.get(0));
        }
        PacketHelper.ChatPacket(mplew, name, message.get(0));
        mplew.write(message.size());
        for (int i = 1; i < message.size(); i++) {
            if (message.get(i) != null) {
                mplew.writeMapleAsciiString(message.get(i));
                PacketHelper.ChatPacket(mplew, name, message.get(i));
            }
        }
        mplew.write(channel - 1);
        mplew.write(ear ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] itemMegaphone(String name, String msg, boolean whisper, int channel, Item item, int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(8);
        mplew.writeMapleAsciiString(msg);
        PacketHelper.ChatPacket(mplew, name, msg);
        mplew.write(channel - 1);
        mplew.write(whisper ? 1 : 0);
        mplew.writeInt(itemId);
        mplew.write(item != null);
        if (item != null) {
            PacketHelper.addItemInfo(mplew, item);
            mplew.writeMapleAsciiString(MapleItemInformationProvider.getInstance().getName(item.getItemId()));
        }

        return mplew.getPacket();
    }

    public static byte[] getOwlOpen() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.OWL_OF_MINERVA.getValue());
        mplew.write(10);
        mplew.write(GameConstants.owlItems.length);
        for (int i : GameConstants.owlItems) {
            mplew.writeInt(i);
        }
        return mplew.getPacket();
    }

    public static byte[] getOwlMessage(int msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
        mplew.writeShort(SendPacketOpcode.OWL_RESULT.getValue());
        mplew.write(msg);
        return mplew.getPacket();
    }

    public static byte[] showWeddingInvitation(String groom, String bride, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ENGAGE_RESULT.getValue());
        mplew.write(17);
        mplew.writeMapleAsciiString(groom);
        mplew.writeMapleAsciiString(bride);
        mplew.writeShort(type);
        return mplew.getPacket();
    }

    public static byte[] showWeddingWishInputDialog() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ENGAGE_REQUEST.getValue());
        mplew.write(9);
        return mplew.getPacket();
    }

    public static byte[] sendEngagementRequest(String name, int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ENGAGE_REQUEST.getValue());
        mplew.write(0);
        mplew.writeMapleAsciiString(name);
        mplew.writeInt(cid);
        return mplew.getPacket();
    }

    public static byte[] sendEngagement(byte msg, int item, MapleCharacter male, MapleCharacter female) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ENGAGE_RESULT.getValue());
        mplew.write(msg);
        if (msg == 13 || msg == 14) {
            mplew.writeInt(male.getMarriageId());
            mplew.writeInt(male.getId());
            mplew.writeInt(female.getId());
            mplew.writeShort(1);
            mplew.writeInt(item);
            mplew.writeInt(item);
            mplew.writeAsciiString(male.getName(), 13);
            mplew.writeAsciiString(female.getName(), 13);
        } else if (msg == 15) {
            mplew.writeAsciiString(male.getName(), 13);
            mplew.writeAsciiString(female.getName(), 13);
            mplew.writeShort(0);
        }
        return mplew.getPacket();
    }

    public static byte[] sendWeddingGive() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(9);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] sendWeddingReceive() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(10);
        mplew.writeLong(-1L);
        mplew.writeInt(0);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] giveWeddingItem() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(11);
        mplew.write(0);
        mplew.writeLong(0L);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] receiveWeddingItem() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(15);
        mplew.writeLong(0L);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] yellowChat(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.YELLOW_CHAT.getValue());
        mplew.write(-1);
        mplew.writeMapleAsciiString(msg);
        return mplew.getPacket();
    }

    public static byte[] catchMob(int mobid, int itemid, byte success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CATCH_MOB.getValue());
        mplew.write(success);
        mplew.writeInt(itemid);
        mplew.writeInt(mobid);
        return mplew.getPacket();
    }

    public static byte[] spawnPlayerNPC(PlayerNPC npc, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PLAYER_NPC.getValue());
        mplew.write(1);
        mplew.writeInt(npc.getId());
        mplew.writeMapleAsciiString(npc.getName());
        AvatarLook.encodeAvatarLook(mplew, chr, true, (GameConstants.isZero(chr.getJob()) && chr.getGender() == 1));
        return mplew.getPacket();
    }

    public static byte[] sendLevelup(boolean family, int level, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LEVEL_UPDATE.getValue());
        mplew.write(family ? 1 : 2);
        mplew.writeInt(level);
        mplew.writeMapleAsciiString(name);
        return mplew.getPacket();
    }

    public static byte[] sendMarriage(boolean family, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MARRIAGE_UPDATE.getValue());
        mplew.write(family ? 1 : 0);
        mplew.writeMapleAsciiString(name);
        return mplew.getPacket();
    }

    public static byte[] sendJobup(boolean family, int jobid, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.JOB_UPDATE.getValue());
        mplew.write(family ? 1 : 0);
        mplew.writeInt(jobid);
        mplew.writeMapleAsciiString((!family ? "> " : "") + name);
        return mplew.getPacket();
    }

    public static byte[] getAvatarMega(MapleCharacter chr, int channel, int itemId, List<String> text, boolean ear) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.AVATAR_MEGA.getValue());
        mplew.writeInt(itemId);
        mplew.writeMapleAsciiString(chr.getName());
        for (String i : text) {
            mplew.writeMapleAsciiString(i);
        }
        PacketHelper.ChatPacket(mplew, chr.getName(), "");
        mplew.writeInt(channel - 1);
        mplew.write(ear ? 1 : 0);
        AvatarLook.encodeAvatarLook(mplew, chr, true, (GameConstants.isZero(chr.getJob()) && chr.getGender() == 1));
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] pendantSlot(boolean p) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PENDANT_SLOT.getValue());
        mplew.write(p ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] getTopMsg(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TOP_MSG.getValue());
        mplew.writeMapleAsciiString(msg);
        return mplew.getPacket();
    }

    public static byte[] getMidMsg(String msg, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
        mplew.writeInt(itemid);
        mplew.writeMapleAsciiString(msg);
        return mplew.getPacket();
    }

    public static byte[] clearMidMsg() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLEAR_MID_MSG.getValue());
        return mplew.getPacket();
    }

    public static byte[] updateJaguar(MapleCharacter from) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_JAGUAR.getValue());
        PacketHelper.addJaguarInfo(mplew, from);
        return mplew.getPacket();
    }

    public static byte[] ultimateExplorer() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ULTIMATE_EXPLORER.getValue());
        return mplew.getPacket();
    }

    public static byte[] updateHyperPresets(MapleCharacter chr, int pos, byte action) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.HYPER_PRESET.getValue());
        packet.write(pos);
        packet.write(action);
        if (action != 0) {
            for (int i = 0; i <= 2; i++) {
                packet.writeInt(chr.loadHyperStats(i).size());
                for (MapleHyperStats mhsz : chr.loadHyperStats(i)) {
                    packet.writeInt(mhsz.getPosition());
                    packet.writeInt(mhsz.getSkillid());
                    packet.writeInt(mhsz.getSkillLevel());
                }
            }
        }
        return packet.getPacket();
    }

    public static byte[] updateSpecialStat(String stat, int array, int mode, int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HYPER.getValue());
        mplew.writeMapleAsciiString(stat);
        mplew.writeInt(array);
        mplew.writeInt(mode);
        mplew.write(1);
        mplew.writeInt(amount);
        return mplew.getPacket();
    }

    public static byte[] updateAzwanFame(int fame) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_HONOR.getValue());
        mplew.writeInt(fame);
        return mplew.getPacket();
    }

    public static byte[] showPopupMessage(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.POPUP_MSG.getValue());
        mplew.writeMapleAsciiString(msg);
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] mannequinRes(byte type, byte result, int type2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MANNEQUIN_RES.getValue());
        mplew.write(type);
        mplew.write(result);
        mplew.writeInt(type2);
        return mplew.getPacket();
    }

    public static byte[] mannequin(byte type, byte result, byte type2, byte slot, MapleMannequin mannequin) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MANNEQUIN.getValue());
        mplew.write(type);
        mplew.write(result);
        mplew.write(type2);
        switch (type2) {
            case 1:
                mplew.write(9);
                mplew.write(9);
                break;
            case 2:
                mplew.write(slot);
                mplew.write(0);
                mplew.write(0);
                mplew.writeInt(mannequin.getValue());
                mplew.write(mannequin.getBaseColor());
                mplew.write(mannequin.getAddColor());
                mplew.write(mannequin.getBaseProb());
                break;
            case 3:
                mplew.write(8);
                mplew.write(slot);
                mplew.write(slot - 1);
                mplew.write(0);
                mplew.write(0);
                mplew.writeInt(0);
                mplew.write(-1);
                mplew.write((type == 1 || type == 2) ? -1 : 0);
                mplew.write(0);
                break;
            case 5:
                mplew.writeInt(0);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] nameChangeUI(boolean use) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHAR_NAME_CHANGE.getValue());
        mplew.writeInt(use ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] PsychicGrab(int cid, int skillid, int unk, int id, Map<Integer, List<PsychicGrabEntry>> grab) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PSYCHIC_GREP.getValue());
        mplew.writeInt(cid);
        mplew.write(1);
        mplew.writeInt(skillid);
        mplew.writeShort(unk);
        mplew.writeInt(id);
        mplew.writeInt(6);
        for (int i = 1; i <= grab.size(); i++) {
            for (PsychicGrabEntry pg : grab.get(Integer.valueOf(i))) {
                mplew.write(pg.getFirstSize());
                mplew.write(pg.getFirstSize());
                mplew.writeInt(pg.getA());
                mplew.writeInt(pg.getB());
                mplew.writeInt(pg.getMobId());
                mplew.writeInt(pg.getUnk2());
                mplew.writeShort(pg.getSecondSize());
                mplew.writeLong(pg.getMobMaxHp());
                mplew.writeLong(pg.getMobHp());
                mplew.write(pg.getUnk());
                mplew.writeNRect(pg.getRect());
            }
        }
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] PsychicGrabAttack(int cid, int skillid, int subskillid, int unk, int id, byte a, int b, List<Integer> grab) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PSYCHIC_GREP_ATTACK.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(skillid);
        mplew.writeShort(unk);
        mplew.writeInt(id);
        mplew.writeInt(6);
        mplew.write(a);
        mplew.writeInt(b);
        if (skillid == 142110003 || skillid == 142120001) {
            mplew.writeInt(subskillid);
            mplew.writeInt(unk);
        }
        mplew.writeInt(grab.size());
        for (Integer g : grab) {
            mplew.writeInt(g.intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] PsychicGrabPreparation(MapleCharacter chr, int skillid, short level, int unk, int speed, int[] unk2, int[] mob, short[] unk3, byte[] unk4, Point[] pos1, Point[] pos2, Point[] pos3, Point[] pos4, Point[] pos5) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PSYCHIC_GREP.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(1);
        mplew.writeInt(skillid);
        mplew.writeShort(level);
        mplew.writeInt(unk);
        mplew.writeInt(speed);
        MapleMonster target = null;
        int k = (skillid == 142120000) ? 5 : 3;
        for (int i = 0; i < k; i++) {
            mplew.write(1);
            mplew.write(1);
            mplew.writeInt(unk2[i]);
            mplew.writeInt(i + 1);
            mplew.writeInt(mob[i]);
            mplew.writeShort(unk3[i]);
            if (mob[i] != 0) {
                target = chr.getMap().getMonsterByOid(mob[i]);
            }
            mplew.writeLong((mob[i] != 0) ? (int) target.getHp() : 100L);
            mplew.writeLong((mob[i] != 0) ? (int) target.getHp() : 100L);
            mplew.write(unk4[i]);
            mplew.writePos(pos1[i]);
            mplew.writePos(pos2[i]);
            mplew.writePos(pos3[i]);
            mplew.writePos(pos4[i]);
            mplew.writePos(pos5[i]);
        }
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] UltimateMaterial(int code, int speed, int unk0, int skill, short level, byte unk1, short unk2, short unk3, short unk4, int posx, int posy) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ULTIMATE_MATERIAL.getValue());
        mplew.writeInt(85930057);
        mplew.writeInt(1);
        mplew.writeInt(code);
        mplew.writeInt(speed);
        mplew.writeInt(unk0);
        mplew.writeInt(skill);
        mplew.writeShort(level);
        mplew.writeInt(1);
        mplew.writeInt(14000);
        mplew.write(unk1);
        mplew.writeShort(unk2);
        mplew.writeShort(unk3);
        mplew.writeShort(unk4);
        mplew.writeInt(posx);
        mplew.writeInt(posy);
        return mplew.getPacket();
    }

    public static byte[] Test() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(427);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        return mplew.getPacket();
    }

    public static byte[] Test1() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(1714);
        mplew.write(HexTool.getByteArrayFromHexString("61 BC 0B 93 06 00 00 00 00"));
        return mplew.getPacket();
    }

    public static byte[] AddCore(Core core) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ADD_CORE.getValue());
        mplew.writeInt(core.getCoreId());
        mplew.writeInt(core.getLevel());
        mplew.writeInt(core.getSkill1());
        mplew.writeInt(core.getSkill2());
        mplew.writeInt(core.getSkill3());
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] UpdateCore(MapleCharacter chr) {
        return UpdateCore(chr, 0);
    }

    public static byte[] UpdateCore(MapleCharacter chr, int unk, int unk2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CORE_LIST.getValue());
        PacketHelper.addMatrixInfo(mplew, chr);
        mplew.write(unk);
        if (unk > 0) {
            mplew.writeInt(unk2);
        }
        return mplew.getPacket();
    }

    public static byte[] UpdateCore(MapleCharacter chr, int equip) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CORE_LIST.getValue());
        PacketHelper.addMatrixInfo(mplew, chr);
        mplew.write((equip > 0));
        if (equip > 0) {
            mplew.writeInt(equip);
        }
        return mplew.getPacket();
    }

    public static byte[] DeleteCore(int count) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DELETE_CORE.getValue());
        mplew.writeInt(count);
        return mplew.getPacket();
    }

    public static byte[] ViewNewCore(Core core, int nCount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.VIEW_CORE.getValue());
        mplew.writeInt(core.getCoreId());
        mplew.writeInt(core.getLevel());
        mplew.writeInt(core.getSkill1());
        mplew.writeInt(core.getSkill2());
        mplew.writeInt(core.getSkill3());
        mplew.writeInt(nCount);
        return mplew.getPacket();
    }

    public static byte[] openCore() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.OPEN_CORE.getValue());
        mplew.writeInt(2);
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] PsychicUltimateDamager(int c, MapleCharacter player) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PSYCHIC_ULTIMATE.getValue());
        mplew.writeInt(player.getId());
        mplew.writeInt(c);
        return mplew.getPacket();
    }

    public static byte[] PsychicDamage(LittleEndianAccessor slea, MapleClient c) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.PSYCHIC_DAMAGE.getValue());
        packet.writeInt(slea.readInt());
        if (slea.available() >= 2L) {
            packet.writeInt(slea.readShort());
        }
        return packet.getPacket();
    }

    public static byte[] OnCreatePsychicArea(int cid, int nAction, int ActionSpeed, int LocalKey, int SkillID, int SLV, int PsychicAreaKey, int DurationTime, int second, int SkeletonFieldPathIdx, int SkeletonAnildx, int SkeletonLoop, int mask8, int mask9) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.PSYCHIC_ATTACK.getValue());
        packet.writeInt(cid);
        packet.write(1);
        packet.writeInt(nAction);
        packet.writeInt(ActionSpeed);
        packet.writeInt(LocalKey);
        packet.writeInt(SkillID);
        packet.writeShort(SLV);
        packet.writeInt(PsychicAreaKey);
        packet.writeInt(DurationTime + 4000);
        packet.write(second);
        packet.writeShort(SkeletonFieldPathIdx);
        packet.writeShort(SkeletonAnildx);
        packet.writeShort(SkeletonLoop);
        packet.writeInt(mask8);
        packet.writeInt(mask9);
        return packet.getPacket();
    }

    public static void CancelPsychicGrep(LittleEndianAccessor rh, MapleClient c) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.CANCEL_PSYCHIC_GREP.getValue());
        packet.writeInt(c.getPlayer().getId());
        packet.writeInt(rh.readInt());
        c.getPlayer().getMap().broadcastMessage(packet.getPacket());
    }

    public static byte[] MatrixSkill(int skillid, int level, List<MatrixSkill> skills) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MATRIX_SKILL.getValue());
        mplew.write(true);
        mplew.writeInt(skillid);
        mplew.writeInt(level);
        mplew.writeInt(skills.size());
        for (MatrixSkill skill : skills) {
            mplew.writeInt(skill.getUnk1());
        }
        return mplew.getPacket();
    }

    public static byte[] MatrixSkillMulti(MapleCharacter chr, int skillid, int level, int unk1, int unk2, int bullet, boolean data, List<Integer> datas, List<MatrixSkill> skills) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MATRIX_MULTI.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillid);
        mplew.writeInt(level);
        mplew.writeInt(unk1);
        mplew.writeInt(unk2);
        mplew.writeInt(bullet);
        mplew.write(data);
        if (data) {
            mplew.writeInt(((Integer) datas.get(0)).intValue());
            mplew.writeInt(((Integer) datas.get(1)).intValue());
            mplew.writeInt(((Integer) datas.get(2)).intValue());
            mplew.writeInt(((Integer) datas.get(3)).intValue());
            mplew.writeInt(((Integer) datas.get(4)).intValue());
            mplew.writeInt(((Integer) datas.get(5)).intValue());
            mplew.write(((Integer) datas.get(6)).intValue());
        }
        mplew.write(0); //362 new 위치정확하지않음
        mplew.writeInt(skills.size());
        for (MatrixSkill skill : skills) {
            mplew.writeInt(skill.getSkill());
            mplew.writeInt(skill.getLevel());
            mplew.writeInt(skill.getUnk1());
            mplew.writeShort(skill.getUnk2());
            mplew.writePos(skill.getAngle());
            mplew.writeInt(skill.getUnk3());
            mplew.write(skill.getUnk4());
            mplew.write(skill.getUnk5());
            if (skill.getUnk5() > 0) {
                mplew.writeInt(skill.getX());
                mplew.writeInt(skill.getY());
            }
            mplew.write(skill.getUnk6());
            if (skill.getUnk6() > 0) {
                mplew.writeInt(skill.getX2());
                mplew.writeInt(skill.getY2());
            }
        }
        return mplew.getPacket();
    }

    public static byte[] Unlinkskill(int skillid, int linkedcid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNLINK_SKILL.getValue());
        mplew.writeInt(1);
        mplew.writeInt(skillid);
        mplew.writeInt(linkedcid);
        return mplew.getPacket();
    }

    public static byte[] Unlinkskillunlock(int skillid, int unlock) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNLINK_SKILL_UNLOCK.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(unlock);
        mplew.writeInt(0); //355 new?
        return mplew.getPacket();
    }

    public static byte[] Unlocklinkskill(int oriskillid, Map<Integer, Integer> unlock) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNLINK_SKILL.getValue());
        mplew.writeInt(unlock.size() + 1);
        mplew.writeInt(oriskillid);
        mplew.writeInt(unlock.size());
        for (Map.Entry<Integer, Integer> skill : unlock.entrySet()) {
            mplew.writeInt(((Integer) skill.getKey()).intValue());
            mplew.writeInt(((Integer) skill.getValue()).intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] Linkskill(int skillid, int sendid, int recvid, int level, int totalskilllv) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LINK_SKILL.getValue());
        PacketHelper.addLinkSkillInfo(mplew, skillid, sendid, recvid, level);
        boolean ordinarySkill = true;
        if (skillid >= 80000066 && skillid <= 80000070) {
            mplew.writeInt(80000055);
        } else if ((skillid >= 80000333 && skillid <= 80000335) || skillid == 80000378) {
            mplew.writeInt(80000329);
        } else if (skillid >= 80002759 && skillid <= 80002761) {
            mplew.writeInt(80002758);
        } else if (skillid >= 80002763 && skillid <= 80002765) {
            mplew.writeInt(80002762);
        } else if (skillid >= 80002767 && skillid <= 80002769) {
            mplew.writeInt(80002766);
        } else if (skillid >= 80002771 && skillid <= 80002773) {
            mplew.writeInt(80002770);
        } else if ((skillid >= 80002775 && skillid <= 80002776) || skillid == 80000000) {
            mplew.writeInt(80002774);
        } else {
            ordinarySkill = false;
            mplew.writeInt(0);
        }
        if (ordinarySkill) {
            mplew.writeInt(totalskilllv);
        }
        return mplew.getPacket();
    }

    public static byte[] AlarmAuction(int type, AuctionItem item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ALARM_AUCTION.getValue());
        mplew.write(type);
        if (item == null) {
            mplew.writeZeroBytes(10000);
            return mplew.getPacket();
        }
        mplew.writeLong(item.getItem().getInventoryId());
        mplew.write(HexTool.getByteArrayFromHexString("35 C2 31 06"));
        mplew.writeInt(item.getBidUserId());
        mplew.writeInt(item.getCharacterId());
        mplew.writeInt(item.getItem().getItemId());
        mplew.writeInt(3);
        mplew.writeLong(30000L);
        mplew.writeLong(PacketHelper.getTime(item.getEndDate()));
        mplew.writeLong(2000L);
        mplew.writeInt(item.getItem().getQuantity());
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] onUserSoulMatching(int type, List<Pair<Integer, MapleCharacter>> chrs) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SOUL_MATCHING.getValue());
        mplew.writeInt(type);
        if (type == 0 || type == 1 || type == 2 || type == 10) {
            mplew.writeShort(chrs.size());
            for (Pair<Integer, MapleCharacter> chr : chrs) {
                mplew.writeInt(((MapleCharacter) chr.getRight()).getLevel());
                mplew.writeInt(((MapleCharacter) chr.getRight()).getJob());
                mplew.writeInt(((Integer) chr.getLeft()).intValue());
                mplew.writeInt(((MapleCharacter) chr.getRight()).getId());
            }
        } else if (type == 3) {
            mplew.writeInt(0);
        }
        return mplew.getPacket();
    }

    public static byte[] showWeddingWishGiveDialog(List<String> wishes) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(9);
        mplew.write(wishes.size());
        for (String s : wishes) {
            mplew.writeMapleAsciiString(s);
        }
        return mplew.getPacket();
    }

    public static byte[] showWeddingWishRecvDialog(Collection<Item> items) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(10);
        mplew.writeLong(126L);
        mplew.write(items.size());
        for (Item i : items) {
            PacketHelper.addItemInfo(mplew, i);
        }
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] showWeddingWishRecvToLocalResult(Collection<Item> items) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(15);
        mplew.writeLong(126L);
        mplew.write(items.size());
        for (Item i : items) {
            PacketHelper.addItemInfo(mplew, i);
        }
        mplew.writeInt(0);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] showWeddingWishRecvDisableHang() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(16);
        return mplew.getPacket();
    }

    public static byte[] showWeddingWishGiveToServerResult(List<String> wishes, MapleInventoryType type, List<Item> items) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(11);
        mplew.write(wishes.size());
        for (String s : wishes) {
            mplew.writeMapleAsciiString(s);
        }
        mplew.writeLong(type.getBitfieldEncoding());
        mplew.write(items.size());
        for (Item item : items) {
            PacketHelper.addItemInfo(mplew, item);
        }
        return mplew.getPacket();
    }

    public static byte[] MiracleCirculator(List<InnerSkillValueHolder> newValues, int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MIRACLE_CIRCULATOR.getValue());
        mplew.writeInt(newValues.size());
        for (int i = 0; i < newValues.size(); i++) {
            mplew.writeInt(((InnerSkillValueHolder) newValues.get(i)).getSkillId());
            mplew.write(((InnerSkillValueHolder) newValues.get(i)).getSkillLevel());
            mplew.write(i + 1);
            mplew.write(((InnerSkillValueHolder) newValues.get(i)).getRank());
        }
        mplew.writeInt(itemId);
        mplew.writeLong(PacketHelper.getTime(-2L));
        mplew.writeInt(6);
        return mplew.getPacket();
    }

    public static byte[] setBossReward(MapleCharacter player) {
        /* 5214 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        /* 5215 */ mplew.writeShort(SendPacketOpcode.BOSS_REWARD.getValue());
        /* 5216 */ Iterator<Item> ite = player.getInventory(MapleInventoryType.ETC).iterator();
        /* 5217 */ List<Item> items = new ArrayList<>();
        /* 5218 */ while (ite.hasNext()) {
            /* 5219 */ Item item = ite.next();
            /* 5220 */ if (item.getReward() != null) {
                /* 5221 */ items.add(item);
            }
        }
        /* 5225 */ mplew.writeInt(items.size());
        /* 5227 */ for (int i = 0; i < items.size(); i++) {
            /* 5228 */ mplew.writeLong(((Item) items.get(i)).getReward().getObjectId());
            /* 5229 */ mplew.writeInt(((Item) items.get(i)).getReward().getMobId());
            /* 5230 */ mplew.writeInt(((Item) items.get(i)).getReward().getPartyId());
            /* 5231 */ mplew.writeInt(((Item) items.get(i)).getReward().getPrice());
            mplew.writeInt(0);
            mplew.writeLong(0);
            mplew.writeLong(PacketHelper.getKoreanTimestamp(items.get(i).getExpiration() - (7 * 24 * 60 * 60 * 1000)));
        }
        /* 5235 */ return mplew.getPacket();
    }

    public static byte[] returnEffectConfirm(Equip item, int scrollId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.RETURNEFFECT_CONFIRM.getValue());
        mplew.writeLong((item.getInventoryId() <= 0L) ? -1L : item.getInventoryId());
        mplew.write(1);
        PacketHelper.addItemInfo(mplew, item);
        mplew.writeInt(scrollId);
        return mplew.getPacket();
    }

    public static byte[] returnEffectModify(Equip item, int scrollId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.RETURNEFFECT_MODIFY.getValue());
        mplew.write((item != null));
        if (item != null) {
            PacketHelper.addItemInfo(mplew, item);
            mplew.writeInt(scrollId);
        }
        return mplew.getPacket();
    }

    public static byte[] eliteWarning(int mapid, int monsterid, int unk) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ELITE_WARNING.getValue());
        mplew.write(0);
        mplew.write(unk);
        mplew.writeInt(mapid);
        mplew.writeInt(monsterid);
        mplew.writeInt(507180);
        return mplew.getPacket();
    }

    public static byte[] mixLense(int itemId, int baseFace, int newFace, boolean isDreeUp, boolean isBeta, boolean isAlphaBeta, MapleCharacter player) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MIX_LENSE.getValue());
        mplew.writeInt(itemId);
        mplew.write(true);
        mplew.write((isDreeUp || isBeta));
        mplew.write(isAlphaBeta);
        mplew.write(false);
        mplew.writeInt(1);
        mplew.writeInt(1);
        mplew.write(2);
        mplew.writeInt(newFace);
        mplew.writeInt(baseFace);
        mplew.writeShort(0);
        mplew.writeShort(-1);
        mplew.writeZeroBytes(5);
        return mplew.getPacket();
    }

    public static byte[] setUnion(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SET_UNION.getValue());
        if (c.getKeyValue("presetNo") == null) {
            c.setKeyValue("presetNo", "0");
        }
        long preset = Integer.parseInt(c.getKeyValue("presetNo"));
        List<MapleUnion> equipped = new ArrayList<>();
        for (MapleUnion union : c.getPlayer().getUnions().getUnions()) {
            if (union.getPosition() >= 0) {
                equipped.add(union);
            }
        }
        mplew.writeInt(0);
        mplew.writeInt(c.getPlayer().getUnions().getUnions().size());
        for (MapleUnion chr : c.getPlayer().getUnions().getUnions()) {
            mplew.writeInt(1);
            mplew.writeInt(chr.getCharid());
            mplew.writeInt(chr.getLevel());
            mplew.writeInt(chr.getJob());
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(-1);
            mplew.writeInt(chr.getUnk3());
            mplew.writeMapleAsciiString(chr.getName());
        }
        mplew.writeInt(equipped.size());
        for (MapleUnion chr : equipped) {
            mplew.writeInt(1);
            mplew.writeInt(chr.getCharid());
            mplew.writeInt(chr.getLevel());
            mplew.writeInt(chr.getJob());
            mplew.writeInt(chr.getUnk1());
            mplew.writeInt(chr.getUnk2());
            mplew.writeInt(chr.getPosition());
            mplew.writeInt(chr.getUnk3());
            mplew.writeMapleAsciiString("");
        }
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] unionFreeset(MapleClient c, int presetNum) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNION_FREESET.getValue());
        List<MapleUnion> equipped = new ArrayList<>();
        for (MapleUnion union : c.getPlayer().getUnions().getUnions()) {
            if (presetNum == 0 && union.getPriset() > 0) {
                equipped.add(union);
                continue;
            }
            if (presetNum == 1 && union.getPriset1() > 0) {
                equipped.add(union);
                continue;
            }
            if (presetNum == 2 && union.getPriset2() > 0) {
                equipped.add(union);
                continue;
            }
            if (presetNum == 3 && union.getPriset3() > 0) {
                equipped.add(union);
                continue;
            }
            if (presetNum == 4 && union.getPriset4() > 0) {
                equipped.add(union);
            }
        }
        mplew.writeInt(presetNum);
        mplew.write(true);
        for (int i = 0; i < 8; i++) {
            mplew.writeInt((c.getCustomData(500627 + presetNum, i + "") == null) ? i : Integer.parseInt(c.getCustomData(500627 + presetNum, i + "")));
        }
        mplew.writeInt(equipped.size());
        for (MapleUnion chr : equipped) {
            mplew.writeInt(1);
            mplew.writeInt(chr.getCharid());
            mplew.writeInt(chr.getLevel());
            mplew.writeInt(chr.getJob());
            mplew.writeInt(chr.getUnk1());
            mplew.writeInt((presetNum == 4) ? chr.getPos4() : ((presetNum == 3) ? chr.getPos3() : ((presetNum == 2) ? chr.getPos2() : ((presetNum == 1) ? chr.getPos1() : chr.getPos()))));
            mplew.writeInt((presetNum == 4) ? chr.getPriset4() : ((presetNum == 3) ? chr.getPriset3() : ((presetNum == 2) ? chr.getPriset2() : ((presetNum == 1) ? chr.getPriset1() : chr.getPriset()))));
            mplew.writeInt(chr.getUnk3());
            mplew.writeMapleAsciiString(chr.getName());
        }
        return mplew.getPacket();
    }

    public static byte[] equipmentEnchantResult(int op, Equip item, Equip item2, EquipmentScroll scroll, StarForceStats stats, int... args) {
        List<EquipmentScroll> ess;
        double rate;
        long meso;
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.EQUIPMENT_ENCHANT.getValue());
        mplew.write(op);
        switch (op) {
            case 50:
                mplew.write(ServerConstants.feverTime);
                ess = EquipmentEnchant.equipmentScrolls(item);
                mplew.write(ess.size());
                for (EquipmentScroll es : ess) {
                    mplew.writeInt(EquipmentEnchant.scrollType(es.getName()));
                    mplew.writeMapleAsciiString(es.getName());
                    mplew.writeInt(es.getName().contains("순백") ? 2 : (es.getName().contains("이노센트") ? 1 : 0));
                    mplew.writeInt(es.getName().contains("아크") ? 4 : ((es.getName().contains("이노센트") || es.getName().contains("순백")) ? 1 : 0));
                    mplew.writeInt(es.getFlag());
                    if (EnchantFlag.Watk.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Watk)).right).intValue());
                    }
                    if (EnchantFlag.Matk.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Matk)).right).intValue());
                    }
                    if (EnchantFlag.Str.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Str)).right).intValue());
                    }
                    if (EnchantFlag.Dex.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Dex)).right).intValue());
                    }
                    if (EnchantFlag.Int.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Int)).right).intValue());
                    }
                    if (EnchantFlag.Luk.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Luk)).right).intValue());
                    }
                    if (EnchantFlag.Wdef.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Wdef)).right).intValue());
                    }
                    if (EnchantFlag.Mdef.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Mdef)).right).intValue());
                    }
                    if (EnchantFlag.Hp.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Hp)).right).intValue());
                    }
                    if (EnchantFlag.Mp.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Mp)).right).intValue());
                    }
                    if (EnchantFlag.Acc.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Avoid)).right).intValue());
                    }
                    if (EnchantFlag.Avoid.check(es.getFlag())) {
                        mplew.writeInt(((Integer) (es.getFlag(EnchantFlag.Avoid)).right).intValue());
                    }
                    mplew.writeInt(es.getJuhun());
                    mplew.writeInt(es.getJuhun());
                    mplew.write(es.getName().contains("100%"));
                }
                break;
            case 52:
                mplew.write((args[0] > 0) ? ((args[1] > 0) ? 2 : 1) : 0);
                rate = (100 - ServerConstants.starForceSalePercent) / 100.0D;
                meso = (long) (args[3] * rate);
                if (meso < 0L) {
                    meso &= 0xFFFFFFFFL;
                }
                mplew.writeLong(meso);
                mplew.writeLong(0L);
                mplew.writeLong((ServerConstants.starForceSalePercent > 0) ? args[3] : 0L);
                mplew.write(0);
                mplew.write(0);
                mplew.writeInt(args[2]);
                mplew.writeInt(args[1]);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.write(item.getEnchantBuff() & 0x20);
                mplew.writeInt(stats.getFlag());
                if (EnchantFlag.Watk.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Watk)).right).intValue());
                }
                if (EnchantFlag.Matk.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Matk)).right).intValue());
                }
                if (EnchantFlag.Str.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Str)).right).intValue());
                }
                if (EnchantFlag.Dex.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Dex)).right).intValue());
                }
                if (EnchantFlag.Int.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Int)).right).intValue());
                }
                if (EnchantFlag.Luk.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Luk)).right).intValue());
                }
                if (EnchantFlag.Wdef.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Wdef)).right).intValue());
                }
                if (EnchantFlag.Mdef.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Mdef)).right).intValue());
                }
                if (EnchantFlag.Hp.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Hp)).right).intValue());
                }
                if (EnchantFlag.Mp.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Mp)).right).intValue());
                }
                if (EnchantFlag.Acc.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Avoid)).right).intValue());
                }
                if (EnchantFlag.Avoid.check(stats.getFlag())) {
                    mplew.writeInt(((Integer) (stats.getFlag(EnchantFlag.Avoid)).right).intValue());
                }
                break;
            case 53:
                mplew.write(ServerConstants.feverTime);
                mplew.writeInt(args[0]);
                break;
            case 100:
                mplew.write(ServerConstants.feverTime);
                mplew.writeInt(args[0]);
                mplew.writeMapleAsciiString(scroll.getName());
                PacketHelper.addItemInfo(mplew, item);
                PacketHelper.addItemInfo(mplew, item2);
                break;
            case 101:
                mplew.write(args[0]);
                mplew.writeInt(0);
                PacketHelper.addItemInfo(mplew, item);
                if (args[0] != 4) {
                    PacketHelper.addItemInfo(mplew, item2);
                }
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] onMesoPickupResult(int meso) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MOB_DROP_MESO_PICKUP.getValue());
        mplew.writeInt(meso);
        return mplew.getPacket();
    }

    public static byte[] onSessionValue(String key, String value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SESSION_VALUE.getValue());
        mplew.writeMapleAsciiString(key);
        mplew.writeMapleAsciiString(value);
        return mplew.getPacket();
    }

    public static byte[] onFieldSetVariable(String key, String value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FIELD_SET_VARIABLE.getValue());
        mplew.writeMapleAsciiString(key);
        mplew.writeMapleAsciiString(value);
        return mplew.getPacket();
    }

    public static byte[] quickPass() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.QUICK_PASS.getValue());
        mplew.writeInt(1);
        return mplew.getPacket();
    }

    public static byte[] updateMaplePoint(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_MAPLEPOINT.getValue());
        mplew.writeInt(chr.getDonationPoint());
        return mplew.getPacket();
    }

    public static byte[] ArcaneCatalyst(Equip equip, int slot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ARCANE_CATALYST.getValue());
        PacketHelper.addItemInfo(mplew, equip);
        mplew.writeInt(slot);
        return mplew.getPacket();
    }

    public static byte[] ArcaneCatalyst2(Equip equip) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ARCANE_CATALYST2.getValue());
        PacketHelper.addItemInfo(mplew, equip);
        return mplew.getPacket();
    }

    public static byte[] useBlackRebirthScroll(Equip item, Item rebirth, long newRebirth, boolean result) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BLACK_REBIRTH_SCROLL.getValue());
        mplew.writeLong((item.getInventoryId() <= 0L) ? -1L : item.getInventoryId());
        mplew.writeLong(result ? 0L : newRebirth);
        mplew.writeInt(result ? 0 : item.getPosition());
        mplew.writeInt(result ? 0 : rebirth.getItemId());
        mplew.writeInt(80);
        mplew.writeInt(0);
        mplew.writeShort(0);
        return mplew.getPacket();
    }

    public static byte[] blackRebirthResult(boolean before, long newRebirth, Equip equip) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BLACK_REBIRTH_RESULT.getValue());
        mplew.writeInt(0);
        mplew.write(1);
        mplew.write(before);
        GameConstants.sendFireOption(mplew, newRebirth, equip);
        if (!before) {
            PacketHelper.addItemInfo(mplew, equip);
        }
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] goldApple(Item item, Item apple) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GOLD_APPLE.getValue());
        mplew.write((item != null));
        if (item != null) {
            mplew.writeInt(item.getItemId());
            mplew.writeShort(item.getQuantity());
            mplew.writeInt(apple.getItemId());
            mplew.writeInt(apple.getPosition());
            mplew.writeInt(2435458);
            mplew.writeInt(1);
            mplew.write((item.getType() == 1));
            if (item.getType() == 1) {
                PacketHelper.addItemInfo(mplew, item);
            }
        } else {
            mplew.writeInt(0);
        }
        return mplew.getPacket();
    }

    public static byte[] followRequest(int chrid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FOLLOW_REQUEST.getValue());
        mplew.writeInt(chrid);
        return mplew.getPacket();
    }

    public static byte[] initSecurity() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.INIT_SECURITY.getValue());
        mplew.writeInt(2);
        mplew.write(0);
        mplew.write(0);
        mplew.writeMapleAsciiString("10000-AABBCCDD-EEFFAA");
        mplew.write(1);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(1);
        mplew.writeInt(0);
        mplew.write(1);
        mplew.writeShort(0);
        return mplew.getPacket();
    }

    public static byte[] updateSecurity() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_SECURITY.getValue());
        int count = 1;
        mplew.writeInt(count);
        for (int i = 0; i < count; i++) {
            mplew.writeLong(0L);
            mplew.writeMapleAsciiString("10000-AABBCCDD-EEFFAA");
            mplew.write(0);
            mplew.writeMapleAsciiString("Black Festival");
            mplew.writeInt(0);
            mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis()));
            mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis()));
            mplew.write(1);
            mplew.writeInt(0);
            mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis()));
            mplew.writeLong(0L);
        }
        return mplew.getPacket();
    }

    public static byte[] blizzardTempest(List<Pair<Integer, Integer>> lists) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BLIZZARD_TEMPEST.getValue());
        mplew.writeInt(lists.size());
        for (Pair<Integer, Integer> list : lists) {
            mplew.writeInt(((Integer) list.left).intValue());
            mplew.writeInt(((Integer) list.right).intValue());
            mplew.writeInt(10000);
        }
        return mplew.getPacket();
    }

    public static byte[] savePassword(String pwd) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SAVE_PASSWORD.getValue());
        mplew.writeLong(-1L);
        mplew.writeMapleAsciiString(pwd);
        return mplew.getPacket();
    }

    public static byte[] HyperMegaPhone(String msg, String name, String rawmsg, int channel, boolean ear, Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(8);
        mplew.writeMapleAsciiString(msg);
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(rawmsg);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(channel - 1);
        mplew.write(ear ? 1 : 0);
        mplew.writeInt(5076100);
        mplew.writeInt(1); // 1.2.362 +
        mplew.write((item == null) ? 0 : 1);
        if (item != null) {
            PacketHelper.addItemInfo(mplew, item);
            mplew.writeMapleAsciiString(MapleItemInformationProvider.getInstance().getName(item.getItemId()));
        }
        return mplew.getPacket();
    }

    public static byte[] RebirthScrollWindow(int scrollid, int pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REBIRTH_SCROLL_WINDOW.getValue());
        mplew.writeInt(scrollid);
        mplew.writeInt(pos);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] SpritPandent(int pos, boolean on, int level, int expplus, int todaytime) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPRIT_PANDENT.getValue());
        mplew.writeInt(-pos);
        mplew.writeInt(level);
        mplew.writeInt(expplus);
        mplew.writeInt(todaytime);
        mplew.writeInt(0);
        mplew.write(on ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] updateSuddenQuest(int quest, boolean timeset, long time, String data) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(13);
        mplew.writeInt(quest);
        if (timeset) {
            mplew.writeShort(15);
            mplew.writeAsciiString("QET=");
            mplew.write(1);
            mplew.write(1);
            mplew.writeLong(time);
            mplew.write(1);
        } else {
            mplew.writeShort(data.length() + 15);
            mplew.writeAsciiString(data);
            mplew.writeAsciiString("QET=");
            mplew.write(1);
            mplew.write(1);
            mplew.writeLong(time);
            mplew.write(1);
        }
        return mplew.getPacket();
    }

    public static byte[] CreateCore(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CREATE_CORE.getValue());
        mplew.write(type);
        return mplew.getPacket();
    }

    public static byte[] UseMakeUpCoupon(MapleCharacter chr, int itemid, int... make) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.USE_MAKEUP_COUPON.getValue());
        mplew.write(1);
        mplew.writeInt(1);
        mplew.writeInt(4);
        mplew.writeInt(itemid);
        mplew.write(chr.isdressup);
        mplew.write((GameConstants.isZero(chr.getJob()) && chr.getGender() == 1) ? 1 : 0);
        mplew.write(make.length);
        for (int i = 0; i < make.length; i++) {
            mplew.writeInt(make[i]);
        }
        return mplew.getPacket();
    }
}
