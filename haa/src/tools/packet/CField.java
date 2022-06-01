package tools.packet;

import client.AvatarLook;
import client.InnerSkillValueHolder;
import client.MapleCabinet;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleKeyLayout;
import client.MapleQuestStatus;
import client.MapleUnion;
import client.MatrixSkill;
import client.RangeAttack;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.Skill;
import client.SkillMacro;
import client.VMatrix;
import client.inventory.AuctionHistory;
import client.inventory.AuctionItem;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleAndroid;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.MapleRing;
import constants.GameConstants;
import constants.ServerConstants;
import handling.SendPacketOpcode;
import handling.channel.handler.AttackInfo;
import handling.channel.handler.PlayerInteractionHandler;
import handling.world.World;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import java.awt.Point;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import scripting.EventInstanceManager;
import server.ChatEmoticon;
import server.DailyGiftItemInfo;
import server.DimentionMirrorEntry;
import server.MapleDueyActions;
import server.MapleItemInformationProvider;
import server.MapleTrade;
import server.QuickMoveEntry;
import server.Randomizer;
import server.WeekendMaple;
import server.enchant.EnchantFlag;
import server.enchant.StarForceStats;
import server.field.skill.MapleFieldAttackObj;
import server.field.skill.MapleMagicWreck;
import server.field.skill.MapleOrb;
import server.field.skill.SecondAtom;
import server.field.skill.SpecialPortal;
import server.life.MapleHaku;
import server.life.MapleMonster;
import server.life.MapleNPC;
import server.maps.ForceAtom;
import server.maps.MapleAtom;
import server.maps.MapleDragon;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMist;
import server.maps.MapleNodes;
import server.maps.MapleReactor;
import server.maps.MapleRune;
import server.maps.MapleSpecialChair;
import server.maps.MapleSummon;
import server.maps.MechDoor;
import server.maps.SummonMovementType;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import server.shops.MapleShop;
import tools.AttackPair;
import tools.HexTool;
import tools.Pair;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;

public class CField {

    public static byte[] getPacketFromHexString(String hex) {
        return HexTool.getByteArrayFromHexString(hex);
    }
    
    public static byte[] TangyoonMobList(int one, int two, int three, int four, int five) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TANGYOON_LIST.getValue());
        mplew.writeInt(5);
        mplew.writeInt(one);
        mplew.writeInt(two);
        mplew.writeInt(three);
        mplew.writeInt(four);
        mplew.writeInt(five);
        
        return mplew.getPacket();
    }

    public static byte[] getServerIP(MapleClient c, int port, int clientId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVER_IP.getValue());
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.write(GameConstants.getServerIp(ServerConstants.Gateway_IP));
        mplew.writeShort(port);
        mplew.writeInt(clientId);
        mplew.writeMapleAsciiString("normal");
        mplew.writeMapleAsciiString("normal");
        mplew.write(1);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.write(0);
        mplew.writeLong(1L);
        mplew.writeLong(0L);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] getChannelChange(MapleClient c, int port) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHANGE_CHANNEL.getValue());
        mplew.write(1);
        mplew.write(GameConstants.getServerIp(ServerConstants.Gateway_IP));
        mplew.writeShort(port);
        return mplew.getPacket();
    }

    public static byte[] PsychicGrabPreparation(MapleCharacter chr, int skillid, short level, int unk, int speed, int unk1, int unk2, int unk3, int unk4) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static class EffectPacket {

        public static byte[] showSummonEffect(MapleCharacter chr, int skillid, boolean own) {
            return showEffect(chr, 0, skillid, 4, 0, 0, (byte) 0, own, null, null, null);
        }

        public static byte[] showPortalEffect(int skillid) {
            return showEffect(null, 0, 0, 7, 0, 0, (byte) 0, true, null, null, null);
        }

        public static byte[] showDiceEffect(MapleCharacter chr, int oldskillid, int skillid, int subeffectid, int subeffectid2, boolean own) {
            return showEffect(chr, oldskillid, skillid, 6, subeffectid, subeffectid2, (byte) (chr.isFacingLeft() ? 1 : 0), own, chr.getTruePosition(), "", null);
        }

        public static byte[] showCharmEffect(MapleCharacter chr, int skillid, int subeffectid2, boolean own, String txt) {
            return showEffect(chr, 0, skillid, 8, 0, subeffectid2, (byte) 0, own, null, txt, null);
        }

        public static byte[] showPetLevelUpEffect(MapleCharacter chr, int skillid, boolean own) {
            return showEffect(chr, 0, skillid, 9, 0, 0, (byte) 0, own, null, null, null);
        }

        public static byte[] showRewardItemEffect(MapleCharacter chr, int skillid, boolean own, String txt) {
            return showEffect(chr, 0, skillid, 20, 0, 0, (byte) 0, own, null, txt, null);
        }

        public static byte[] showItemMakerEffect(MapleCharacter chr, int direction, boolean own) {
            return showEffect(chr, 0, 0, 22, 0, 0, (byte) direction, own, null, null, null);
        }

        public static byte[] showWheelEffect(int skillid) {
            return showEffect(null, 0, 0, 27, 0, 0, (byte) 0, true, null, null, null);
        }

        public static byte[] showWZEffect(String txt) {
            return showEffect(null, 0, 0, 28, 0, 0, (byte) 0, true, null, txt, null);
        }

        public static byte[] showWZEffect2(String txt) {
            return showEffect(null, 0, 0, 34, 0, 0, (byte) 0, true, null, txt, null);
        }

        public static byte[] showEffect(MapleCharacter chr, String txt) {
            return showEffect(chr, 0, 0, 29, 0, 0, (byte) 0, true, null, txt, null);
        }

        public static byte[] showHealEffect(MapleCharacter chr, int skillid, boolean own) {
            return showEffect(chr, 0, skillid, 37, 0, 0, (byte) 0, own, null, null, null);
        }

        public static byte[] showBoxEffect(MapleCharacter chr, int oldskillid, int skillid, boolean own) {
            return showEffect(chr, oldskillid, skillid, 53, 0, 0, (byte) 0, own, null, null, null);
        }

        public static byte[] showBurningFieldEffect(String txt) {
            return showEffect(null, 0, 0, 61, 0, 0, (byte) 0, true, null, txt, null);
        }

        public static byte[] showNormalEffect(MapleCharacter chr, int effectid, boolean own) {
            return showEffect(chr, 0, 0, effectid, 0, 0, (byte) 0, own, null, null, null);
        }

        public static byte[] showWillEffect(MapleCharacter chr, int subeffectid, int skillid, int skillLevel) {
            return showEffect(chr, skillLevel, skillid, 73, subeffectid, 0, (byte) 0, true, null, null, null);
        }

        public static byte[] showFieldSkillEffect(int skillid, int skillLevel, int type) {
            return showEffect(null, skillLevel, skillid, 74, type, 0, (byte) 0, true, null, null, null);
        }

        public static byte[] showFieldSkillEffect(MapleCharacter chr, int skillid, int skillLevel) {
            return showEffect(chr, skillLevel, skillid, 46, 0, 0, (byte) 0, true, null, null, null);
        }

        public static byte[] showFieldSkillEffect(MapleCharacter chr, int skillid, byte skillLevel) {
            return showEffect(chr, skillLevel, skillid, 36, 0, 0, (byte) 0, true, null, null, null);
        }

        public static byte[] showEffect(MapleCharacter chr, int oldskillid, int skillid, int effectid, int subeffectid, int subeffectid2, byte direction, boolean own, Point pos, String txt, Item item) {
            return showEffect(chr, oldskillid, skillid, effectid, subeffectid, subeffectid2, direction, own, pos, txt, item, null);
        }

        public static byte[] showEffect(MapleCharacter chr, int oldskillid, int skillid, int effectid, int subeffectid, int subeffectid2, byte direction, boolean own, Point pos, String txt, Item item, AttackInfo at) {
            boolean a;
            int j;
            boolean i;
            int b;
            boolean reset, z;
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            if (own) {
                mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
            } else {
                mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
                mplew.writeInt(chr.getId());
            }
            mplew.write(effectid);
            switch (effectid) {
                case 26:
                    mplew.write(false);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeMapleAsciiString(txt);
                    break;
                case 29:
                    mplew.writeMapleAsciiString(txt);
                    mplew.writeInt(1);
                    break;
                case 30:
                    a = false;
                    mplew.write(a);
                    if (a) {
                        mplew.writeMapleAsciiString(txt);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    }
                    break;
                case 31:
                    mplew.writeMapleAsciiString(txt);
                    mplew.writeInt(5000);
                    mplew.writeInt(0);
                    break;
                case 33:
                    mplew.writeMapleAsciiString(txt);
                    break;
                case 34:
                    mplew.writeMapleAsciiString(txt);
                    mplew.writeInt(100);
                    break;
                case 44:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.write(0);
                    break;
                case 45:
                    mplew.writeInt(oldskillid);
                    mplew.writeInt(skillid);
                    break;
                case 47:
                    mplew.write(false);
                    break;
                case 55:
                    mplew.writeMapleAsciiString(txt);
                    a = false;
                    mplew.write(a);
                    if (!a) {
                        mplew.writeInt(oldskillid);
                        mplew.writeInt(skillid);
                        mplew.writeInt(1);
                        break;
                    }
                    final boolean bool = false;
                        mplew.write(bool);
                        if (bool) {
                            mplew.write(false);
                            mplew.writeInt(0);
                            mplew.writeInt(0);
                        break;
                    }
                    break;
                case 60:
                    mplew.write(false);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeMapleAsciiString(txt);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    if (txt.length() > 0) {
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    }
                    break;
                case 61:
                    mplew.writeMapleAsciiString(txt);
                    mplew.writeInt(50);
                    mplew.writeInt(1500);
                    mplew.writeInt(4);
                    mplew.writeInt(0);
                    mplew.writeInt(-200);
                    mplew.writeInt(1);
                    mplew.writeInt(4);
                    mplew.writeInt(2);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeMapleAsciiString("");
                    mplew.writeInt(0);
                    mplew.write(0);
                    break;
                case 63:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case 64:
                    mplew.writeInt(0);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    break;
                case 65:
                    PacketHelper.addItemInfo(mplew, item);
                    break;
                case 83:
                    mplew.writeInt(skillid);
                    mplew.writeInt(chr.getId());
                    mplew.writeInt(1);
                    mplew.writeInt(pos.x);
                    mplew.writeInt(pos.y);
                    mplew.write(1);
                    mplew.writeInt(1);
                    mplew.writeInt(300);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case 1:
                case 2:
                    if (effectid == 2) {
                        mplew.writeInt(subeffectid2);
                    }
                    mplew.writeInt(skillid);
                    mplew.writeInt(chr.getLevel());
                    mplew.writeInt((chr.getTotalSkillLevel(skillid) == 0) ? 1 : chr.getTotalSkillLevel(skillid));
                    if (skillid == 22170074) {
                        mplew.write(0);
                    }
                    if (skillid == 1320016) {
                        mplew.write(chr.getReinCarnation());
                    }
                    if (skillid == 4331006) {
                        mplew.write(0);
                        mplew.writeInt(0);
                    }
                    if (skillid == 400020010) {
                        mplew.write(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    }
                    if (skillid == 3211010 || skillid == 3111010 || skillid == 1100012) {
                        mplew.write(direction);
                        mplew.writeInt(subeffectid2);
                        mplew.writeInt((subeffectid2 > 0) ? pos.x : 0);
                        mplew.writeInt((subeffectid2 > 0) ? pos.y : 0);
                    }
                    if (skillid == 64001000 || (skillid > 64001006 && skillid <= 64001008)) {
                        mplew.write(direction);
                    }
                    if (skillid - 64001009 >= -2 && skillid - 64001009 <= 2) {
                        mplew.write(direction);
                        mplew.writeInt(chr.getFH());
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                    }
                    if (skillid == 64001012) {
                        mplew.write(direction);
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                        mplew.writeInt(oldskillid);
                    }
                    if (skillid == 30001062) {
                        mplew.write(0);
                        mplew.writeShort(pos.x);
                        mplew.writeShort(pos.y);
                    }
                    if (skillid == 30001061) {
                        mplew.write(direction);
                    }
                    if (skillid == 60001218 || skillid == 60011218 || skillid == 400001000) {
                        mplew.writeInt(oldskillid);
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                        mplew.write(true);
                    }
                    if (skillid == 131003016) {
                        mplew.write(0);
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                    }
                    if (skillid == 400051025) {
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                    }
                    if (skillid == 20041222 || skillid == 15001021 || skillid == 20051284 || skillid == 4211016 || skillid == 400041026 || skillid == 152001004) {
                        mplew.writeInt(oldskillid);
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                        mplew.writeInt(subeffectid);
                    }
                    if (skillid == 4221052 || skillid == 65121052) {
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    }
                    if (GameConstants.sub_7F9870(skillid) > 0) {
                        mplew.writeInt(0);
                    }
                    if (skillid == 400041019) {
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    }
                    if (skillid == 400041009) {
                        mplew.writeInt((chr.getParty() != null) ? chr.getParty().getId() : 0);
                    }
                    if (skillid - 400041011 >= -4 && skillid - 400041011 <= 4) {
                        mplew.writeInt((chr.getParty() != null) ? chr.getParty().getId() : 0);
                    }
                    if (skillid == 400041036) {
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    }
                    if (skillid == 63001002 || skillid == 63001004) {
                        mplew.write(direction);
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                    } else if (skillid == 63101104) {
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                        mplew.write(direction);
                    }
                    if (skillid != 152111005 && skillid != 152111006) {
                        if (skillid == 80002393 || skillid == 80002394 || skillid == 80002395 || skillid == 80002421) {
                            mplew.writeInt(0);
                        }
                        if (GameConstants.sub_8242D0(skillid)) {
                            mplew.write(0);
                        }
                    }
                    break;
                case 3:
                    mplew.writeInt(skillid);
                    mplew.writeInt(chr.getLevel());
                    mplew.write(chr.getSkillLevel(GameConstants.getLinkedSkill(skillid)));
                    break;
                case 4:
                    mplew.writeInt(skillid);
                    mplew.write(chr.getSkillLevel(GameConstants.getLinkedSkill(skillid)));
                    if (skillid == 31111003) {
                        mplew.writeInt(0);
                    }
                    if (skillid == 25121006) {
                        mplew.writeInt(0);
                    }
                    break;
                case 5:
                    mplew.writeInt(skillid);
                    mplew.write(chr.getSkillLevel(GameConstants.getLinkedSkill(skillid)));
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case 6:
                    mplew.writeInt(subeffectid);
                    mplew.writeInt(subeffectid2);
                    mplew.writeInt(skillid);
                    mplew.write(chr.getSkillLevel(GameConstants.getLinkedSkill(skillid)));
                    mplew.write(oldskillid);
                    break;
                case 7:
                    mplew.writeInt(skillid);
                    mplew.write(0);
                    break;
                case 8:
                    mplew.write(subeffectid2);
                    for (j = 0; j < subeffectid2; j++) {
                        mplew.writeInt(oldskillid);
                        mplew.writeInt(skillid);
                    }
                    break;
                case 9:
                    mplew.write(0);
                    mplew.writeInt(chr.getPetIndex(skillid));
                    break;
                case 10:
                    mplew.writeInt(skillid);
                    if (GameConstants.sub_1F04F40(skillid)) {
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                        mplew.writeInt(chr.getSkillLevel(GameConstants.getLinkedSkill(skillid)));
                    }
                    if (skillid == 32111016) {
                        mplew.writeInt(0);
                        mplew.write(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    }
                    if (skillid == 80002206 || skillid == 80000257 || skillid == 80000260 || skillid == 80002599) {
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    }
                    if (skillid == 400021088) {
                        mplew.writeInt((chr.getPosition()).x);
                        mplew.writeInt((chr.getPosition()).y);
                        mplew.writeInt(at.acrossPosition.x);
                        mplew.writeInt(at.acrossPosition.y);
                        break;
                    }
                    if (skillid == 400031053) {
                        mplew.writeInt(0);
                        mplew.writeInt((chr.getPosition()).x);
                        mplew.writeInt((chr.getPosition()).y);
                        break;
                    }
                    if (skillid == 36110005) {
                        mplew.writeInt(pos.x);
                        mplew.writeInt(pos.y);
                        mplew.writeInt(subeffectid);
                    }
                    break;
                case 12:
                    i = false;
                    mplew.write(i);
                    mplew.write(0);
                    mplew.write(0);
                    mplew.writeInt(0);
                    break;
                case 16:
                    mplew.write(subeffectid);
                    break;
                case 17:
                    mplew.writeInt(skillid);
                    break;
                case 18:
                    mplew.writeMapleAsciiString(txt);
                    break;
                case 20:
                    mplew.writeInt(skillid);
                    mplew.write((txt.length() > 0));
                    if (txt.length() > 0) {
                        mplew.writeMapleAsciiString(txt);
                    }
                    break;
                case 22:
                    mplew.writeInt(direction);
                    break;
                case 23:
                    mplew.writeInt(0);
                    break;
                case 25:
                    mplew.writeInt(skillid);
                    break;
                case 27:
                    mplew.write(chr.getInventory(MapleInventoryType.CASH).countById(skillid));
                    break;
                case 28:
                    mplew.writeMapleAsciiString(txt);
                    break;
                case 32:
                    mplew.writeInt(skillid);
                    mplew.writeMapleAsciiString(txt);
                    break;
                case 36:
                    mplew.writeInt(skillid);
                    mplew.write(oldskillid);
                    mplew.write(false);
                    mplew.writeInt(skillid);
                    break;
                case 37:
                    mplew.writeInt(skillid);
                    break;
                case 38:
                    mplew.writeMapleAsciiString(txt);
                    mplew.write(1);
                    mplew.writeInt(oldskillid);
                    mplew.writeInt(subeffectid);
                    if (subeffectid == 2) {
                        mplew.writeInt(skillid);
                    }
                    break;
                case 39:
                    mplew.writeInt(0);
                    break;
                case 40:
                    mplew.writeInt(0);
                    break;
                case 46:
                    mplew.writeInt(skillid);
                    mplew.writeInt(oldskillid);
                    break;
                case 48:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case 50:
                    b = 0;
                    mplew.write(b);
                    switch (b) {
                        case 0:
                        case 2:
                        case 3:
                            mplew.writeInt(0);
                            break;
                        case 1:
                        case 4:
                            mplew.writeInt(0);
                            break;
                    }
                    break;
                case 51:
                    mplew.writeInt(0);
                    break;
                case 53:
                    mplew.write(0);
                    mplew.write(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case 54:
                    mplew.writeInt(0);
                    break;
                case 56:
                    reset = false;
                    mplew.write(reset);
                    if (!reset) {
                        mplew.writeInt(0);
                        mplew.write(0);
                    }
                    break;
                case 57:
                    mplew.writeInt(skillid);
                    mplew.writeInt(subeffectid);
                    mplew.writeInt(subeffectid2);
                    break;
                case 58:
                    mplew.writeInt(0);
                    break;
                case 59:
                    mplew.writeInt(pos.x);
                    mplew.writeInt(pos.y);
                    break;
                case 62:
                    mplew.writeInt(skillid);
                    mplew.writeInt(oldskillid);
                    break;
                case 66:
                    mplew.writeInt(skillid);
                    mplew.writeMapleAsciiString(txt);
                    break;
                case 69:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case 70:
                    z = false;
                    mplew.write(z);
                    if (z) {
                        mplew.writeInt(0);
                    }
                    break;
                case 71:
                    mplew.writeShort(0);
                    mplew.writeInt(0);
                    mplew.write(false);
                    mplew.write(0);
                    mplew.write(false);
                    break;
                case 72:
                    mplew.writeInt(0);
                    PacketHelper.addItemInfo(mplew, item);
                    break;
                case 73:
                    sub_1E4D510(mplew, subeffectid, skillid, oldskillid);
                    break;
                case 74:
                    sub_1E4DCD0(mplew, skillid, oldskillid, subeffectid);
                    break;
                case 75:
                    mplew.writeInt(0);
                    break;
                case 77:
                    mplew.writeMapleAsciiString(txt);
                    break;
                case 76:
                    mplew.writeMapleAsciiString(txt);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.write(false);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
            }
            mplew.writeZeroBytes(100);
            return mplew.getPacket();
        }

        public static byte[] sub_1E4D510(MaplePacketLittleEndianWriter mplew, int subeffectid, int skillid, int skillLevel) {
            mplew.write(subeffectid);
            mplew.writeInt(skillid);
            mplew.writeInt(skillLevel);
            return mplew.getPacket();
        }

        public static byte[] sub_1E4DCD0(MaplePacketLittleEndianWriter mplew, int skillId, int skillLv, int type) {
            mplew.writeInt(skillId);
            mplew.writeInt(skillLv);
            if (skillId == 100017) {
                mplew.writeShort(type);
            }
            return mplew.getPacket();
        }

        public static byte[] gainExp(long exp) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
            mplew.write(25);
            mplew.writeLong(exp);
            return mplew.getPacket();
        }
    }

    public static class UIPacket {

            public static byte[] greenShowInfo(String msg) {
            MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();

            packet.writeShort(SendPacketOpcode.GREEN_SHOW_INFO.getValue());
            packet.write(1);
            packet.writeMapleAsciiString(msg);
            packet.write(1); // 0 = Lock 1 = Clear

            return packet.getPacket();
        }
        
        public static byte[] detailShowInfo1(String msg, final int font, final int size, final long color) { // 보통 3,15 ,1
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.DETAIL_SHOW_INFO.getValue());
            mplew.writeInt(font);
            mplew.writeInt(size);
            mplew.writeInt(color);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.writeMapleAsciiString(msg);
            return mplew.getPacket();
        }
        
        public static byte[] detailShowInfo(String msg, int font, int size, int color) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.DETAIL_SHOW_INFO.getValue());
            mplew.writeInt(font);
            mplew.writeInt(size);
            mplew.writeInt(color);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.writeMapleAsciiString(msg);
            return mplew.getPacket();
        }

        public static byte[] getRainBowRushSetting() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.RAINBOW_RUSH.getValue());
            mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 48 E8 01 00 B8 0B 00 00 01 00 00 00 05 00 00 00 2C 01 00 00 10 27 00 00 10 27 00 00 00 00 00 00 9C FF FF FF 40 1F 00 00 88 13 00 00 01 00 00 00 16 00 00 00 C0 D4 01 00 01 00 00 00 00 00 00 00 00 6A E8 40 B0 AD 01 00 01 00 00 00 00 00 00 00 00 60 6D 40 28 9A 01 00 01 00 00 00 00 00 00 00 00 60 6D 40 A0 86 01 00 01 00 00 00 00 00 00 00 00 00 6E 40 18 73 01 00 01 00 00 00 00 00 00 00 00 40 70 40 90 5F 01 00 01 00 00 00 00 00 00 00 00 E0 70 40 08 4C 01 00 01 00 00 00 00 00 00 00 00 80 71 40 80 38 01 00 01 00 00 00 00 00 00 00 00 D0 71 40 F8 24 01 00 01 00 00 00 00 00 00 00 00 80 71 40 70 11 01 00 01 00 00 00 00 00 00 00 00 20 72 40 E8 FD 00 00 01 00 00 00 00 00 00 00 00 C0 72 40 D8 D6 00 00 01 00 00 00 00 00 00 00 00 00 74 40 C8 AF 00 00 01 00 00 00 00 00 00 00 00 E0 75 40 40 9C 00 00 01 00 00 00 00 00 00 00 00 00 79 40 B8 88 00 00 01 00 00 00 00 00 00 00 00 20 7C 40 30 75 00 00 01 00 00 00 00 00 00 00 00 40 7F 40 A8 61 00 00 01 00 00 00 00 00 00 00 00 C0 82 40 20 4E 00 00 01 00 00 00 00 00 00 00 00 E0 85 40 98 3A 00 00 01 00 00 00 00 00 00 00 00 00 89 40 10 27 00 00 01 00 00 00 00 00 00 00 00 40 8F 40 88 13 00 00 01 00 00 00 00 00 00 00 00 70 97 40 00 00 00 00 01 00 00 00 00 00 00 00 00 40 9F 40 01 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 F0 3F 15 00 00 00 28 9A 01 00 01 00 00 00 00 00 00 00 00 40 5A 40 A0 86 01 00 01 00 00 00 00 00 00 00 00 00 59 40 18 73 01 00 01 00 00 00 00 00 00 00 00 80 56 40 90 5F 01 00 01 00 00 00 00 00 00 00 00 80 56 40 08 4C 01 00 01 00 00 00 00 00 00 00 00 40 55 40 80 38 01 00 01 00 00 00 00 00 00 00 00 00 54 40 F8 24 01 00 01 00 00 00 00 00 00 00 00 C0 52 40 70 11 01 00 01 00 00 00 00 00 00 00 00 80 51 40 E8 FD 00 00 01 00 00 00 00 00 00 00 00 40 51 40 60 EA 00 00 01 00 00 00 00 00 00 00 00 C0 50 40 D8 D6 00 00 01 00 00 00 00 00 00 00 00 40 50 40 C8 AF 00 00 01 00 00 00 00 00 00 00 00 80 4F 40 40 9C 00 00 01 00 00 00 00 00 00 00 00 00 4E 40 B8 88 00 00 01 00 00 00 00 00 00 00 00 00 49 40 30 75 00 00 01 00 00 00 00 00 00 00 00 80 46 40 A8 61 00 00 01 00 00 00 00 00 00 00 00 80 41 40 20 4E 00 00 01 00 00 00 00 00 00 00 00 00 3E 40 98 3A 00 00 01 00 00 00 00 00 00 00 00 00 39 40 10 27 00 00 01 00 00 00 00 00 00 00 00 00 34 40 88 13 00 00 01 00 00 00 00 00 00 00 00 00 2E 40 00 00 00 00 01 00 00 00 00 00 00 00 00 00 24 40 0B 00 00 00 C0 D4 01 00 01 00 00 00 00 00 00 00 00 00 00 00 B0 AD 01 00 01 00 00 00 00 00 00 00 00 60 6D 40 A0 86 01 00 01 00 00 00 00 00 00 00 00 00 69 40 90 5F 01 00 01 00 00 00 00 00 00 00 00 C0 62 40 80 38 01 00 01 00 00 00 00 00 00 00 00 80 56 40 70 11 01 00 01 00 00 00 00 00 00 00 00 40 55 40 60 EA 00 00 01 00 00 00 00 00 00 00 00 00 54 40 50 C3 00 00 01 00 00 00 00 00 00 00 00 00 4E 40 40 9C 00 00 01 00 00 00 00 00 00 00 00 00 44 40 20 4E 00 00 01 00 00 00 00 00 00 00 00 00 34 40 00 00 00 00 01 00 00 00 00 00 00 00 00 00 24 40 15 00 00 00 28 9A 01 00 01 00 00 00 00 00 00 00 00 C0 72 40 A0 86 01 00 01 00 00 00 00 00 00 00 00 60 73 40 18 73 01 00 01 00 00 00 00 00 00 00 00 00 74 40 90 5F 01 00 01 00 00 00 00 00 00 00 00 A0 74 40 08 4C 01 00 01 00 00 00 00 00 00 00 00 40 75 40 80 38 01 00 01 00 00 00 00 00 00 00 00 E0 75 40 F8 24 01 00 01 00 00 00 00 00 00 00 00 80 76 40 70 11 01 00 01 00 00 00 00 00 00 00 00 20 77 40 E8 FD 00 00 01 00 00 00 00 00 00 00 00 60 78 40 60 EA 00 00 01 00 00 00 00 00 00 00 00 A0 79 40 D8 D6 00 00 01 00 00 00 00 00 00 00 00 E0 7A 40 C8 AF 00 00 01 00 00 00 00 00 00 00 00 20 7C 40 40 9C 00 00 01 00 00 00 00 00 00 00 00 60 7D 40 B8 88 00 00 01 00 00 00 00 00 00 00 00 A0 7E 40 30 75 00 00 01 00 00 00 00 00 00 00 00 E0 7F 40 A8 61 00 00 01 00 00 00 00 00 00 00 00 90 80 40 20 4E 00 00 01 00 00 00 00 00 00 00 00 30 81 40 98 3A 00 00 01 00 00 00 00 00 00 00 00 C0 82 40 10 27 00 00 01 00 00 00 00 00 00 00 00 50 84 40 88 13 00 00 01 00 00 00 00 00 00 00 00 E0 85 40 00 00 00 00 01 00 00 00 00 00 00 00 00 00 89 40 03 00 00 00 50 C3 00 00 01 00 00 00 00 00 00 00 00 00 49 40 40 9C 00 00 01 00 00 00 00 00 00 00 00 00 59 40 00 00 00 00 01 00 00 00 00 00 00 00 00 C0 72 40 02 00 00 00 B0 AD 01 00 01 00 00 00 00 00 00 00 00 00 F0 3F 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 03 00 00 00 90 5F 01 00 01 00 00 00 00 00 00 00 00 70 A7 40 60 EA 00 00 01 00 00 00 00 00 00 00 00 40 8F 40 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E8 03 00 00 09 00 00 00 C0 D4 01 00 01 00 00 00 00 00 00 00 00 00 24 40 B0 AD 01 00 01 00 00 00 00 00 00 00 00 00 34 40 50 C3 00 00 01 00 00 00 00 00 00 00 00 00 24 40 C8 AF 00 00 01 00 00 00 00 00 00 00 00 00 1C 40 40 9C 00 00 01 00 00 00 00 00 00 00 00 00 18 40 B8 88 00 00 01 00 00 00 00 00 00 00 00 00 10 40 30 75 00 00 01 00 00 00 00 00 00 00 00 00 08 40 10 27 00 00 01 00 00 00 00 00 00 00 00 00 00 40 00 00 00 00 01 00 00 00 00 00 00 00 00 00 F0 3F 00 00 00 00 02 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 FF FF FF FF 01 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF 19 00 00 00"));
            return mplew.getPacket();
        }

        public static byte[] getRainBowRushStart() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.RAINBOW_RUSH.getValue() + 1);
            mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"));
            return mplew.getPacket();
        }

        public static byte[] getRainBowResult(int jam, int time) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.RAINBOW_RUSH.getValue() + 6);
            mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 02 00 00 00 00 00 00 00 00 00 00 00"));
            mplew.writeInt(jam * 100);
            mplew.writeInt(jam * 100);
            mplew.writeInt(time);
            return mplew.getPacket();
        }

        public static byte[] getDirectionStatus(boolean enable) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.DIRECTION_STATUS.getValue());
            mplew.write(enable ? 1 : 0);
            return mplew.getPacket();
        }

        public static byte[] openUI(int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
            mplew.writeShort(SendPacketOpcode.OPEN_UI.getValue());
            mplew.writeInt(type);
            return mplew.getPacket();
        }

        public static byte[] closeUI(int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.CLOSE_UI.getValue());
            mplew.writeInt(type);
            return mplew.getPacket();
        }

        public static byte[] openUIOption(int type, int option) {
            return openUIOption(type, option, 0);
        }

        public static byte[] openUIOption(int type, int option, int option2) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);
            mplew.writeShort(SendPacketOpcode.OPEN_UI_OPTION.getValue());
            mplew.writeInt(type);
            mplew.writeInt(option);
            mplew.writeInt(option2);
            return mplew.getPacket();
        }

        public static byte[] IntroLock(boolean enable) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_LOCK.getValue());
            mplew.write(enable ? 1 : 0);
            mplew.writeInt(0);
            return mplew.getPacket();
        }

        public static byte[] IntroEnableUI(int wtf) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_ENABLE_UI.getValue());
            mplew.write((wtf > 0) ? 1 : 0);
            mplew.write(0);
            if (wtf > 0) {
                mplew.write(false);
                mplew.write(false);
            }
            return mplew.getPacket();
        }

        public static byte[] IntroDisableUI(boolean enable) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_DISABLE_UI.getValue());
            mplew.write(enable ? 1 : 0);
            return mplew.getPacket();
        }

        public static byte[] summonHelper(boolean summon) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SUMMON_HINT.getValue());
            mplew.write(summon ? 1 : 0);
            return mplew.getPacket();
        }

        public static byte[] summonMessage(int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
            mplew.write(1);
            mplew.writeInt(type);
            mplew.writeInt(7000);
            return mplew.getPacket();
        }

        public static byte[] summonMessage(String message) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
            mplew.write(0);
            mplew.writeMapleAsciiString(message);
            mplew.writeInt(200);
            mplew.writeShort(0);
            mplew.writeInt(10000);
            return mplew.getPacket();
        }

        public static byte[] getDirectionInfo(int type, int value) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.UserInGameDirectionEvent.getValue());
            mplew.write(type);
            mplew.writeLong(value);
            return mplew.getPacket();
        }

        public static byte[] getDirectionInfo(String data, int value, int x, int y, int a, int b) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.UserInGameDirectionEvent.getValue());
            mplew.write(2);
            mplew.writeMapleAsciiString(data);
            mplew.writeInt(value);
            mplew.writeInt(x);
            mplew.writeInt(y);
            mplew.write(a);
            if (a > 0) {
                mplew.writeInt(0);
            }
            mplew.write(b);
            if (b > 1) {
                mplew.writeInt(0);
                mplew.write(a);
                mplew.write(b);
            }
            return mplew.getPacket();
        }

        public static final byte[] playMovie(String data, boolean show) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAY_MOVIE.getValue());
            mplew.writeMapleAsciiString(data);
            mplew.write(show ? 1 : 0);
            return mplew.getPacket();
        }

        public static byte[] detailShowInfo(String msg, boolean RuneSystem) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.DETAIL_SHOW_INFO.getValue());
            mplew.writeInt(3);
            mplew.writeInt(RuneSystem ? 17 : 20);
            mplew.writeInt(RuneSystem ? 0 : 4);
            mplew.writeInt(0);
            mplew.write(false);
            mplew.writeMapleAsciiString(msg);
            return mplew.getPacket();
        }

        public static byte[] OnSetMirrorDungeonInfo(boolean clear) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.MIRROR_DUNGEON_INFO.getValue());
            mplew.writeInt(clear ? 0 : GameConstants.dList.size());
            for (Pair<String, String> d : GameConstants.dList) {
                mplew.writeMapleAsciiString((String) d.left);
                mplew.writeInt(0);
                mplew.writeMapleAsciiString((String) d.right);
            }
            return mplew.getPacket();
        }
    }

    public static class AttackObjPacket {

        public static byte[] ObjCreatePacket(MapleFieldAttackObj fao) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SPAWN_FIELDATTACK_OBJ.getValue());
            mplew.writeInt(fao.getObjectId());
            mplew.writeInt(1);
            mplew.writeInt(fao.getChr().getId());
            mplew.writeInt(0);
            mplew.write(false);
            mplew.writeInt((fao.getTruePosition()).x);
            mplew.writeInt((fao.getTruePosition()).y);
            mplew.write(fao.isFacingleft());
            return mplew.getPacket();
        }

        public static byte[] ObjRemovePacketByOid(int objectid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.REMOVE_FIELDATTACK_OBJ_KEY.getValue());
            mplew.writeInt(objectid);
            return mplew.getPacket();
        }

        public static byte[] ObjRemovePacketByList(List<MapleFieldAttackObj> removes) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.REMOVE_FIELDATTACK_OBJ_LIST.getValue());
            mplew.writeInt(removes.size());
            for (MapleMapObject obj : removes) {
                mplew.writeInt(obj.getObjectId());
            }
            return mplew.getPacket();
        }

        public static byte[] OnSetAttack(MapleFieldAttackObj fao) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.FIELDATTACK_OBJ_ATTACK.getValue());
            mplew.writeInt(fao.getObjectId());
            mplew.writeInt(0);
            return mplew.getPacket();
        }
    }

    public static class SummonPacket {

        public static byte[] spawnSummon(MapleSummon summon, boolean animated) {
            return spawnSummon(summon, animated, summon.getDuration());
        }

        public static byte[] spawnSummon(MapleSummon summon, boolean animated, int newDuration) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SPAWN_SUMMON.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(summon.getSkill());
            mplew.writeInt(summon.getOwner().getLevel());
            mplew.writeInt(summon.getSkillLevel());
            mplew.writePos(summon.getPosition());
            mplew.write(summon.getSkill() == 5320011 || summon.getSkill() == 61101002 || summon.getSkill() == 101100100 || summon.getSkill() == 14000027 || summon.getSkill() == 22171081 || summon.getSkill() == 400051046 ? 5 : 4);
            int Foothold = 0;
            if (summon.getOwner().getMap().getFootholds().findBelow(summon.getPosition()) != null && summon.getSkill() != 14121003 && summon.getSkill() != 151100002 && summon.getSkill() != 400021068) {
                Foothold = (short) summon.getOwner().getMap().getFootholds().findBelow(summon.getPosition()).getId();
            }
            mplew.writeShort(Foothold);
            mplew.write(summon.getMovementType().getValue());
            mplew.write(summon.getSummonType());
            mplew.write(animated ? 1 : 0);
            mplew.writeInt(summon.getOwner().maelstrom);
            mplew.write(0);
            mplew.write(1);
            mplew.writeInt(summon.getOwner().getJob() == 1412 ? 14120008 : 0);
            mplew.writeInt(0);
            mplew.write(summon.getSkill() == 400041028 || summon.getSkill() == 4341006 || summon.getMovementType() == SummonMovementType.ShadowServant || summon.getMovementType() == SummonMovementType.ShadowServantExtend && summon.getSkill() != 400011088 && summon.getSkill() != 152101000 && summon.getSkill() != 400021068);
            MapleCharacter chr = summon.getOwner();
            if (chr != null && (summon.getSkill() == 400041028 || summon.getSkill() == 4341006 || summon.getMovementType() == SummonMovementType.ShadowServant || summon.getMovementType() == SummonMovementType.ShadowServantExtend && summon.getSkill() != 400011088 && summon.getSkill() != 152101000 && summon.getSkill() != 400021068)) {
                AvatarLook.encodeAvatarLook(mplew, chr, true, false);
                if (summon.getSkill() == 400011005) {
                    mplew.writeInt(60);
                    mplew.writeInt(30);
                } else if (summon.getSkill() == 400041028) {
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                }
            }
            if (summon.getSkill() == 35111002) {
                ArrayList<Point> teslaz = new ArrayList<Point>();
                for (MapleSummon tesla : chr.getSummons()) {
                    if (tesla.getSkill() != 35111002) {
                        continue;
                    }
                    teslaz.add(new Point(tesla.getTruePosition()));
                }
                if (teslaz.size() != 3) {
                    mplew.write(false);
                } else {
                    mplew.write(true);
                    for (Point pos : teslaz) {
                        mplew.writePos(pos);
                    }
                }
            }
            if (SummonPacket.isSpecial(summon.getSkill())) {
                if (summon.getSkill() == 131001017) {
                    mplew.writeInt(400);
                    mplew.writeInt(30);
                } else if (summon.getSkill() == 131002017) {
                    mplew.writeInt(800);
                    mplew.writeInt(60);
                } else if (summon.getSkill() == 131003017) {
                    mplew.writeInt(1200);
                    mplew.writeInt(90);
                } else {
                    mplew.writeInt((summon.getSkill() - GameConstants.getLinkedSkill(summon.getSkill()) + 1) * 400);
                    mplew.writeInt((summon.getSkill() - GameConstants.getLinkedSkill(summon.getSkill()) + 1) * 30);
                }
            }
            boolean special = summon.getOwner().getBuffedValue(400031005);
            if (GameConstants.isWildHunter(chr.getJob()) && summon.getSkill() == GameConstants.getSelectJaguarSkillId(GameConstants.getMountItem(33001001, chr))) {
                special = false;
            }
            mplew.write(special);
            mplew.writeInt(summon.getSkill() == 35111002 ? 90000 : summon.getSkill() == 152101000 ? 0 : newDuration);
            mplew.write(1);
            mplew.writeInt(summon.getSummonRLType());
            mplew.writeInt(0);
            if (summon.getSkill() - 33001007 >= 0 && summon.getSkill() - 33001007 <= 8) {
                mplew.write(special);
                mplew.writeInt(newDuration);
            }
            mplew.writeInt(summon.getSkill() == 162101012 ? 400 : 0);
            mplew.write(summon.isControlCrystal() || summon.getSkill() == 400021068 || summon.getSkill() == 400051046);
            if (summon.isControlCrystal() || summon.getSkill() == 400021068 || summon.getSkill() == 400051046) {
                mplew.writeInt(summon.getEnergy());
                if (summon.getEnergy() >= 150) {
                    mplew.writeInt(4);
                } else if (summon.getEnergy() >= 90) {
                    mplew.writeInt(3);
                } else if (summon.getEnergy() >= 60) {
                    mplew.writeInt(2);
                } else if (summon.getEnergy() >= 30) {
                    mplew.writeInt(1);
                } else {
                    mplew.writeInt(0);
                }
            }
            mplew.writeInt(0);
            if (summon.getSkill() - 33001007 >= 0 && summon.getSkill() - 33001007 <= 8) {
                mplew.write(0);
            } else {
                mplew.writeInt(0);
            }
            mplew.writeInt(-1); //361 new?
            mplew.write(0);
            mplew.writeInt(0);
            return mplew.getPacket();
        }

        private static boolean isSpecial(int a1) {
            if (a1 > 131003017) {
                if (a1 == 400011005 || a1 == 400031007) {
                    return true;
                }
                boolean v2 = (a1 == 400041028);
                if (!v2) {
                    return (a1 - 400031007 >= -2 && a1 - 400031007 <= 2);
                }
                return true;
            }
            if (a1 == 131003017) {
                return true;
            }
            if (a1 > 131001017) {
                boolean v2 = (a1 == 131002017);
                if (!v2) {
                    return (a1 - 400031007 >= -2 && a1 - 400031007 <= 2);
                }
                return true;
            }
            if (a1 == 131001017 || a1 == 14111024 || (a1 > 14121053 && a1 <= 14121056)) {
                return true;
            }
            return (a1 - 400031007 >= -2 && a1 - 400031007 <= 2);
        }

        public static byte[] removeSummon(MapleSummon summon, boolean animated) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.REMOVE_SUMMON.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            if (animated) {
                switch (summon.getSkill()) {
                    case 14000027:
                    case 14111024:
                    case 14121054:
                    case 35121003:
                    case 400051011:
                        mplew.write(10);
                        return mplew.getPacket();
                    case 33101008:
                    case 35111001:
                    case 35111002:
                    case 35111005:
                    case 35111009:
                    case 35111010:
                    case 35111011:
                    case 35121009:
                    case 35121010:
                    case 35121011:
                        mplew.write(5);
                        return mplew.getPacket();
                    case 5321052:
                    case 14121003:
                    case 36121002:
                    case 36121013:
                    case 36121014:
                    case 101100100:
                    case 101100101:
                    case 400051017:
                        mplew.write(0);
                        return mplew.getPacket();
                }
                mplew.write(4);
            } else if (summon.getSkill() == 14000027 || summon.getSkill() == 14100027 || summon.getSkill() == 14110029 || summon.getSkill() == 14120008) {
                mplew.write(16);
            } else {
                mplew.write((summon.getSkill() == 35121003) ? 10 : 1);
            }
            return mplew.getPacket();
        }

        public static byte[] moveSummon(int cid, int oid, Point startPos, List<LifeMovementFragment> moves) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.MOVE_SUMMON.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(oid);
            mplew.writeInt(0);
            mplew.writePos(startPos);
            mplew.writeInt(0);
            PacketHelper.serializeMovementList(mplew, moves);
            return mplew.getPacket();
        }

        public static byte[] summonAttack(MapleSummon summon, int skillid, byte animation, byte tbyte, List<Pair<Integer, List<Long>>> allDamage, int level, Point pos, boolean darkFlare) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SUMMON_ATTACK.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(summon.getOwner().getLevel());
            mplew.write(animation);
            mplew.write(tbyte);
            for (Pair<Integer, List<Long>> attackEntry : allDamage) {
                mplew.writeInt(((Integer) attackEntry.left).intValue());
                if (((Integer) attackEntry.left).intValue() > 0) {
                    mplew.write(7);
                    for (Long damage : attackEntry.right) {
                        if (summon.getOwner().getStat().getCritical_rate() >= 100 || summon.getOwner().getSkillCustomValue(3310005) != null || Randomizer.isSuccess(summon.getOwner().getStat().getCritical_rate())) {
                            mplew.writeLong(damage.longValue() | 0x8000000000000001L);
                            continue;
                        }
                        mplew.writeLong(damage.longValue());
                    }
                }
            }
            mplew.write(darkFlare ? 1 : 0);
            mplew.write(summon.isNoapply());
            mplew.writePos(pos);
            mplew.writeInt(skillid);
            mplew.write(false);
            mplew.writePos(new Point(0, 0));
            return mplew.getPacket();
        }

        public static byte[] updateSummon(MapleSummon summon, int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.UPDATE_SUMMON.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.write((type == 99) ? 0 : type);
            mplew.writeInt(summon.getSkill());
            mplew.writeInt((type == 99) ? 1 : 0);
            return mplew.getPacket();
        }

        public static byte[] getSummonSkillAttackEffect(MapleSummon summon, int type, int skillid, int level, int unk1, int unk2, int bullet, Point pos1, Point pos2, Point pos3, int unk3, int unk4, int unk5, List<MatrixSkill> skills) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.UPDATE_SUMMON.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.write(type);
            mplew.writeInt(skillid);
            mplew.writeInt(skillid);
            mplew.writeInt(level);
            mplew.writeInt(unk1);
            mplew.writeInt(unk2);
            mplew.writeInt(bullet);
            mplew.writePos(pos1);
            mplew.writePosInt(pos2);
            mplew.writePosInt(pos3);
            mplew.writeInt(unk3);
            mplew.writeInt(unk4);
            mplew.write(unk5);
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

        public static byte[] summonSkill(int cid, int summonskillid, int newStance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SUMMON_SKILL.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(summonskillid);
            mplew.write(newStance);
            return mplew.getPacket();
        }

        public static byte[] JaguarAutoAttack(boolean on) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.JAGUAR_AUTO_ATTACK.getValue());
            mplew.write(on);
            return mplew.getPacket();
        }

        public static byte[] summonDebuff(int cid, int oid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SUMMON_DEBUFF.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(oid);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] damageSummon(int cid, int summonskillid, int damage, int unkByte, int monsterIdFrom) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.DAMAGE_SUMMON.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(summonskillid);
            mplew.writeInt(unkByte);
            mplew.writeInt(damage);
            mplew.writeInt(monsterIdFrom);
            mplew.writeInt(0);
            return mplew.getPacket();
        }

        public static byte[] damageSummon(MapleSummon summon) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.DAMAGE_SUMMON_2.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(8);
            return mplew.getPacket();
        }

        public static byte[] BeholderRevengeAttack(MapleCharacter chr, short damage, int oid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.BEHOLDER_REVENGE.getValue());
            mplew.writeInt(chr.getId());
            mplew.writeInt(damage);
            mplew.writeInt(oid);
            return mplew.getPacket();
        }

        public static byte[] transformSummon(MapleSummon summon, int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.TRANSFORM_SUMMON.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(type);
            if (type == 2) {
                mplew.writeInt(summon.getCrystalSkills().size());
                for (int i = 1; i <= summon.getCrystalSkills().size(); i++) {
                    mplew.writeInt(i);
                    mplew.writeInt(((Boolean) summon.getCrystalSkills().get(i - 1)).booleanValue() ? 1 : 0);
                }
            }
            return mplew.getPacket();
        }

        public static byte[] DeathAttack(MapleSummon summon) {
            return DeathAttack(summon, 0);
        }

        public static byte[] DeathAttack(MapleSummon summon, int skillvalue) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.DEATH_ATTACK.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(skillvalue);
            return mplew.getPacket();
        }

        public static byte[] ElementalRadiance(MapleSummon summon, int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ELEMENTAL_RADIANCE.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(type);
            switch (type) {
                case 2:
                    mplew.writeInt(summon.getEnergy());
                    if (summon.getEnergy() >= 150) {
                        mplew.writeInt(4);
                        break;
                    }
                    if (summon.getEnergy() >= 90) {
                        mplew.writeInt(3);
                        break;
                    }
                    if (summon.getEnergy() >= 60) {
                        mplew.writeInt(2);
                        break;
                    }
                    if (summon.getEnergy() >= 30) {
                        mplew.writeInt(1);
                        break;
                    }
                    mplew.writeInt(0);
                    break;
                case 5:
                    mplew.writeInt((summon.getOwner().getBuffedEffect(400021061) != null) ? 400021062 : 152110002);
                    mplew.writeInt((summon.getOwner().getBuffedEffect(400021061) != null) ? 1000 : 4000);
                    break;
            }
            return mplew.getPacket();
        }

        public static byte[] specialSummon(MapleSummon summon, int type) {
            return specialSummon(summon, type, 0);
        }

        public static byte[] specialSummon(MapleSummon summon, int type, int skillid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SPECIAL_SUMMON.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(type);
            switch (type) {
                case 2:
                    mplew.writeInt(summon.getEnergy());
                    mplew.writeInt(summon.getEnergy());
                    break;
                case 3:
                    mplew.writeInt(0);
                    break;
                case 4:
                    mplew.writeInt(skillid);
                    break;
            }
            return mplew.getPacket();
        }

        public static byte[] specialSummon2(MapleSummon summon, int skill) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SPECIAL_SUMMON2.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(skill);
            return mplew.getPacket();
        }

        public static byte[] AbsorbentEdificeps(int cid, int oid, int combo, int stack) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SPECIAL_SUMMON.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(oid);
            mplew.writeInt(2);
            mplew.writeInt(combo);
            mplew.writeInt(stack);
            return mplew.getPacket();
        }

        public static byte[] summonRangeAttack(MapleSummon summon, int skill) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SUMMON_RANGE_ATTACK.getValue());
            mplew.writeInt(summon.getOwner().getId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(skill);
            mplew.write((skill == 400041050 || skill == 400041051) ? 1 : 0);
            return mplew.getPacket();
        }
    }

    public static class NPCPacket {

        public static byte[] spawnNPC(MapleNPC life, boolean show) {
            /* 1578 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1580 */ mplew.writeShort(SendPacketOpcode.SPAWN_NPC.getValue());
            /* 1581 */ mplew.writeInt(life.getObjectId());
            /* 1582 */ mplew.writeInt(life.getId());
            /* 1583 */ mplew.writeShort((life.getPosition()).x);
            /* 1584 */ mplew.writeShort(life.getCy());
            /* 1585 */ mplew.writeLong(-1L);
            /* 1586 */ mplew.write(0);
            /* 1587 */ mplew.write((life.getF() == 1) ? 0 : 1);
            /* 1588 */ mplew.writeShort(life.getFh());
            /* 1589 */ mplew.writeShort(life.getRx0());
            /* 1590 */ mplew.writeShort(life.getRx1());
            /* 1591 */ mplew.write(show ? 1 : 0);
            /* 1592 */ mplew.writeInt(0);
            /* 1593 */ mplew.write(0);
            /* 1594 */ mplew.writeInt(-1);
            /* 1595 */ mplew.writeInt(0);
            /* 1596 */ mplew.writeInt(0);
            /* 1597 */ mplew.writeMapleAsciiString("");

            /* 1600 */ mplew.write(0);

            /* 1619 */ return mplew.getPacket();
        }

        public static byte[] getNPCTalks(int npc, byte msgType, String talk, String endBytes, byte type, int diffNPC) {
            /* 1623 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1625 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.writeInt(0); // 360 new
            /* 1626 */ mplew.write(3);
            /* 1627 */ mplew.writeInt(0);
            /* 1628 */ mplew.write(1);
            /* 1629 */ mplew.write(msgType);
            /* 1630 */ mplew.writeShort(0);
            /* 1631 */ mplew.write(0);
            /* 1632 */ mplew.write(0);
            /* 1633 */ mplew.writeShort(type);
            /* 1634 */ mplew.write(1);
            /* 1635 */ if (diffNPC > 0) {
                /* 1636 */ mplew.writeInt(diffNPC);
            }
            /* 1638 */ mplew.writeMapleAsciiString(talk);
            /* 1639 */ mplew.write(HexTool.getByteArrayFromHexString(endBytes));
            /* 1640 */ mplew.writeInt(0);
            /* 1641 */ return mplew.getPacket();
        }

        public static byte[] spawnNPC2(MapleNPC life, boolean show) {
            /* 1645 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1647 */ mplew.writeShort(SendPacketOpcode.SPAWN_NPC.getValue());
            /* 1648 */ mplew.writeInt(life.getObjectId());
            /* 1649 */ mplew.writeInt(life.getId());
            /* 1650 */ mplew.writeShort((life.getPosition()).x);
            /* 1651 */ mplew.writeShort(life.getCy());
            /* 1652 */ mplew.writeLong(-1L);
            /* 1653 */ mplew.write(1);
            /* 1654 */ mplew.write(1);
            /* 1655 */ mplew.writeShort(life.getFh());
            /* 1656 */ mplew.writeShort(life.getRx0());
            /* 1657 */ mplew.writeShort(life.getRx1());
            /* 1658 */ mplew.write(show ? 1 : 0);
            /* 1659 */ mplew.writeInt(0);
            /* 1660 */ mplew.write(0);
            /* 1661 */ mplew.writeInt(-1);
            /* 1662 */ mplew.writeInt(0);
            /* 1663 */ mplew.writeInt(1000);
            /* 1664 */ mplew.writeMapleAsciiString("");
            /* 1665 */ mplew.write(0);
            /* 1666 */ return mplew.getPacket();
        }

        public static byte[] removeNPC(int objectid) {
            /* 1670 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1672 */ mplew.writeShort(SendPacketOpcode.REMOVE_NPC.getValue());
            /* 1673 */ mplew.writeInt(objectid);

            /* 1675 */ return mplew.getPacket();
        }

        public static byte[] removeNPCController(int objectid) {
            /* 1679 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1681 */ mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
            /* 1682 */ mplew.write(0);
            /* 1683 */ mplew.writeInt(objectid);

            /* 1685 */ return mplew.getPacket();
        }

        public static byte[] spawnNPCRequestController(MapleNPC life, boolean MiniMap) {
            /* 1689 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1691 */ mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
            /* 1692 */ mplew.write(1);
            /* 1693 */ mplew.writeInt(life.getObjectId());
            /* 1694 */ mplew.writeInt(life.getId());
            /* 1695 */ mplew.writeShort((life.getPosition()).x);
            /* 1696 */ mplew.writeShort(life.getCy());
            /* 1697 */ mplew.writeLong(-1L);
            /* 1698 */ mplew.write((life.getF() == 1) ? 0 : 1);
            /* 1699 */ mplew.write(life.isLeft());
            /* 1700 */ mplew.writeShort(life.getFh());
            /* 1701 */ mplew.writeShort(life.getRx0());
            /* 1702 */ mplew.writeShort(life.getRx1());
            /* 1703 */ mplew.writeShort(MiniMap ? 1 : 0);
            /* 1704 */ mplew.writeInt(0);
            /* 1705 */ mplew.writeInt(-1);
            /* 1706 */ mplew.writeZeroBytes(11);
            /* 1707 */ return mplew.getPacket();
        }

        public static byte[] setNPCScriptable(List<Pair<Integer, String>> npcs) {
            /* 1711 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 1712 */ mplew.writeShort(SendPacketOpcode.NPC_SCRIPTABLE.getValue());
            /* 1713 */ mplew.write(npcs.size());
            /* 1714 */ for (Pair<Integer, String> s : npcs) {
                /* 1715 */ mplew.writeInt(((Integer) s.left).intValue());
                /* 1716 */ mplew.writeMapleAsciiString((String) s.right);
                /* 1717 */ mplew.writeInt(0);
                /* 1718 */ mplew.writeInt(2147483647);
            }
            /* 1720 */ return mplew.getPacket();
        }

        public static byte[] setNPCMoveAction(int oid, String s) {
            /* 1724 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 1725 */ mplew.writeShort(SendPacketOpcode.NPC_ACTION.getValue());
            /* 1726 */ mplew.writeInt(oid);
            /* 1727 */ mplew.write(HexTool.getByteArrayFromHexString(s));
            /* 1728 */ return mplew.getPacket();
        }

        public static byte[] setNPCMotion(int oid, int type) {
            /* 1732 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 1733 */ mplew.writeShort(SendPacketOpcode.NPC_CHANGE_ACTION.getValue());
            /* 1734 */ mplew.writeInt(oid);
            /* 1735 */ mplew.writeInt(type);
            /* 1736 */ return mplew.getPacket();
        }

        public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type) {
            /* 1740 */ return getNPCTalk(npc, msgType, talk, endBytes, type, npc, false, false);
        }

        public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type, int npc2) {
            /* 1744 */ return getNPCTalk(npc, msgType, talk, endBytes, type, npc2, false, false);
        }

        public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type, int diffNPC, boolean illust, boolean isLeft) {
            /* 1748 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1750 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            /* 1751 */ boolean sp = false;
            /* 1752 */ switch (type) {
                case 5:
                case 17:
                case 23:
                case 37:
                case 57:
                    /* 1758 */ sp = true;
                    break;
            }
            mplew.writeInt(0); // 360 new
            /* 1761 */ mplew.write(sp ? 3 : 4);
            /* 1762 */ mplew.writeInt((type == 17 || type == 23) ? 0 : npc);
            /* 1763 */ mplew.write(sp ? 1 : 0);
            /* 1764 */ mplew.write(msgType);
            /* 1765 */ if (sp) {
                /* 1766 */ mplew.writeInt(0);
            }
            /* 1768 */ mplew.write(type);
            /* 1769 */ mplew.write(0);
            /* 1770 */ mplew.write((sp || (type & 0x4) != 0) ? 1 : 0);
            if (msgType == 0) {
                mplew.writeInt(1); // 361
            }

            /* 1771 */ if ((type & 0x4) != 0 && type != 57) {
                /* 1772 */ if (diffNPC == 0) {
                    /* 1773 */ diffNPC = npc;
                }
                /* 1775 */ mplew.writeInt(diffNPC);
            }
            /* 1777 */ if (msgType == 19) {
                /* 1778 */ mplew.writeLong(5L);
            }
            /* 1780 */ mplew.writeMapleAsciiString(talk);
            /* 1781 */ if (msgType != 19) {
                /* 1782 */ if (msgType != 28 && msgType != 30) {
                    /* 1783 */ mplew.write(HexTool.getByteArrayFromHexString(endBytes));
                    /* 1784 */ if (type == 37 && talk.contains("꿈이 무너지")) {
                        /* 1785 */ mplew.writeInt(3000);
                    }
                }

                /* 1789 */ mplew.writeInt(illust ? npc : 0);
                /* 1790 */ if (illust) {
                    /* 1791 */ mplew.writeInt(diffNPC);
                    /* 1792 */ mplew.write(isLeft);
                }
            }

            /* 1796 */ return mplew.getPacket();
        }

        public static byte[] getPraticeReplace(int npc, byte msgType, String talk, String endBytes, byte type, int unk) {
            /* 1800 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1802 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.writeInt(0); // 360 new
            /* 1803 */ mplew.write(4);
            /* 1804 */ mplew.writeInt(npc);
            /* 1805 */ mplew.write(0);
            /* 1806 */ mplew.write(msgType);
            /* 1807 */ mplew.write(type);
            /* 1808 */ mplew.write(unk);
            /* 1809 */ mplew.write(0);
            /* 1810 */ mplew.writeMapleAsciiString(talk);
            /* 1811 */ return mplew.getPacket();
        }

        public static byte[] getNPCConductExchangeTalk(int npc, String msg) {
            /* 1815 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 1816 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
          //  System.err.println("getNPCConductExchangeTalk");
            mplew.writeInt(0); // 360 new
            /* 1817 */ mplew.write(4);
            /* 1818 */ mplew.writeInt(npc);
            /* 1819 */ mplew.write(0);
            /* 1820 */ mplew.writeShort(3);
            /* 1821 */ mplew.writeShort(1);
            /* 1822 */ mplew.writeMapleAsciiString(msg);
            /* 1823 */ return mplew.getPacket();
        }

        public static byte[] getIlust(int npc, int type, boolean lumi) {
            /* 1827 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 1828 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            //System.err.println("getIlust");
            mplew.writeInt(0); // 360 new
            /* 1829 */ mplew.write(3);
            /* 1830 */ mplew.writeInt(0);
            /* 1831 */ mplew.write(1);
            /* 1832 */ mplew.writeInt(npc);
            /* 1833 */ mplew.writeInt(type);
            /* 1834 */ mplew.writeInt(lumi ? 0 : 1);
            /* 1835 */ if (lumi) {
                /* 1836 */ mplew.write(1);
                /* 1837 */ mplew.writeInt(1);
                /* 1838 */ mplew.writeInt(2);
                /* 1839 */ mplew.writeMapleAsciiString("빛의 길");
                /* 1840 */ mplew.writeMapleAsciiString("어둠의 길");
            } else {
                /* 1842 */ mplew.write(1);
                /* 1843 */ mplew.writeInt(1);
                /* 1844 */ mplew.writeInt(2);
                /* 1845 */ mplew.writeMapleAsciiString("데몬 어벤져");
                /* 1846 */ mplew.writeMapleAsciiString("데몬 슬레이어");
            }
            /* 1848 */ return mplew.getPacket();
        }

        public static byte[] getMapSelection(int npcid, String sel) {
            /* 1852 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1854 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
           // System.err.println("getMapSelection");
            mplew.writeInt(0); // 360 new
            /* 1856 */ mplew.write(4);
            /* 1857 */ mplew.writeInt(npcid);
            /* 1858 */ mplew.write(0);
            /* 1859 */ mplew.write(17);
            /* 1860 */ mplew.writeShort(0);
            /* 1861 */ mplew.write(0);
            /* 1862 */ mplew.write(0);
            /* 1863 */ mplew.writeInt((npcid == 2083006) ? 1 : 0);
            /* 1864 */ mplew.write(0);
            /* 1865 */ mplew.writeInt((npcid == 9010022) ? 1 : 0);
            /* 1866 */ mplew.writeMapleAsciiString(sel);

            /* 1868 */ return mplew.getPacket();
        }

        public static byte[] getNPCTalkMixStyle(int npcId, String talk, boolean isZeroBeta, boolean isAngelicBuster) {
            /* 1872 */ MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
            /* 1873 */ packet.writeShort(SendPacketOpcode.NPC_TALK.getValue());
          //  System.err.println("getNPCTalkMixStyle");
            packet.writeInt(0); // 360 new
            /* 1874 */ packet.write(4);
            /* 1875 */ packet.writeInt(npcId);
            /* 1876 */ packet.write(0);
            /* 1877 */ packet.write(44);
            /* 1878 */ packet.writeShort(0);
            /* 1879 */ packet.write(0);

            /* 1881 */ packet.writeInt(0);
            /* 1882 */ packet.write(isAngelicBuster ? 1 : 0);
            /* 1883 */ packet.writeInt(isZeroBeta ? 1 : 0);
            /* 1884 */ packet.writeInt(50);
            /* 1885 */ packet.writeMapleAsciiString(talk);

            /* 1887 */ return packet.getPacket();
        }

        public static byte[] getNPCTalkStyle(int npc, String talk, List<Integer> args) {
            /* 1891 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1893 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
           // System.err.println("getNPCTalkStyle");
            mplew.writeInt(0); // 360 new
            /* 1895 */ mplew.write(4);
            /* 1896 */ mplew.writeInt(npc);
            /* 1897 */ mplew.write(0);
            /* 1898 */ mplew.write(10);
            /* 1899 */ mplew.writeShort(0);
            /* 1900 */ mplew.write(0);
            /* 1901 */ mplew.write(0);
            /* 1902 */ mplew.write(0);
            /* 1903 */ mplew.writeMapleAsciiString(talk);
            /* 1904 */ mplew.writeInt(0);
            /* 1905 */ mplew.write(args.size());

            /* 1907 */ for (int i = 0; i < args.size(); i++) {
                /* 1908 */ mplew.writeInt(((Integer) args.get(i)).intValue());
            }

            /* 1911 */ return mplew.getPacket();
        }

        public static byte[] getNPCTalkStyle(MapleCharacter chr, int npc, String talk, int... args) {
            /* 1915 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1917 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
         //   System.err.println("getNPCTalkStyle2");
            mplew.writeInt(0); // 360 new
            /* 1919 */ mplew.write(4);
            /* 1920 */ mplew.writeInt(npc);
            /* 1921 */ mplew.write(0);
            /* 1922 */ mplew.write(10);
            /* 1923 */ mplew.writeShort(0);
            /* 1924 */ mplew.write(0);

            /* 1926 */ mplew.write(GameConstants.isAngelicBuster(chr.getJob()) ? (chr.getDressup() ? 1 : 0) : 0);
            /* 1927 */ mplew.write(GameConstants.isZero(chr.getJob()) ? ((chr.getGender() == 1) ? 1 : 0) : 0);
            /* 1928 */ mplew.writeMapleAsciiString(talk);
            mplew.write(0);
            mplew.writeInt(chr.getHair());
            mplew.write(-1);
            mplew.write(0);
            mplew.write(0);
            mplew.writeInt(chr.getFace());
            /* 1932 */ mplew.write(args.length);

            /* 1934 */ for (int i = 0; i < args.length; i++) {
                /* 1935 */ mplew.writeInt(args[i]);
            }

            /* 1938 */ return mplew.getPacket();
        }

        public static byte[] getNPCTalkStyleAndroid(int npcId, String talk, int... args) {
            /* 1942 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1944 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
          //  System.err.println("getNPCTalkStyleAndroid");
            mplew.writeInt(0); // 360 new
            /* 1945 */ mplew.write(4);
            /* 1946 */ mplew.writeInt(npcId);
            /* 1947 */ mplew.write(0);

            /* 1949 */ mplew.write(11);
            /* 1950 */ mplew.writeShort(0);
            /* 1951 */ mplew.write(0);

            /* 1954 */ mplew.writeMapleAsciiString(talk);

            /* 1956 */ mplew.write(args.length);

            /* 1958 */ for (int i = 0; i < args.length; i++) {
                /* 1959 */ mplew.writeInt(args[i]);
            }
            /* 1961 */ return mplew.getPacket();
        }

        public static byte[] getNPCTalkStyleZero(int npcId, String talk, int[] args1, int[] args2) {
            /* 1965 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1967 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
         //   System.err.println("getNPCTalkStyleZero");
            mplew.writeInt(0); // 360 new
            /* 1968 */ mplew.write(4);
            /* 1969 */ mplew.writeInt(npcId);
            /* 1970 */ mplew.write(0);
            /* 1971 */ mplew.write(32);
            /* 1972 */ mplew.writeShort(0);
            /* 1973 */ mplew.write(0);
            /* 1974 */ mplew.writeMapleAsciiString(talk);
            /* 1975 */ mplew.writeInt(0);
            /* 1976 */ mplew.write(args1.length);
            int i;
            /* 1978 */ for (i = 0; i < args1.length; i++) {
                /* 1979 */ mplew.writeInt(args1[i]);
            }

            /* 1982 */ mplew.write(args2.length);

            /* 1984 */ for (i = 0; i < args2.length; i++) {
                /* 1985 */ mplew.writeInt(args2[i]);
            }
            /* 1987 */ return mplew.getPacket();
        }

        public static byte[] getNPCTalkNum(int npc, String talk, int def, int min, int max) {
            /* 1991 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 1993 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
           // System.err.println("getNPCTalkNum");
            mplew.writeInt(0); // 360 new
            /* 1995 */ mplew.write(4);
            /* 1996 */ mplew.writeInt(npc);
            /* 1997 */ mplew.write(0);
            /* 1998 */ mplew.write(5);
            /* 1999 */ mplew.writeShort(4);
            /* 2000 */ mplew.write(0);
            /* 2001 */ mplew.writeInt(npc);
            /* 2002 */ mplew.writeMapleAsciiString(talk);
            /* 2003 */ mplew.writeInt(def);
            /* 2004 */ mplew.writeInt(min);
            /* 2005 */ mplew.writeInt(max);
            /* 2006 */ mplew.writeInt(0);

            /* 2008 */ return mplew.getPacket();
        }

        public static byte[] getNPCTalkText(int npc, String talk) {
            /* 2012 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2014 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
          //  System.err.println("getNPCTalkText");
            mplew.writeInt(0); // 360 new
            /* 2015 */ mplew.write(4);
            /* 2016 */ mplew.writeInt(npc);
            /* 2017 */ mplew.write(0);
            /* 2018 */ mplew.write(4);
            /* 2019 */ mplew.writeShort(4);
            /* 2020 */ mplew.write(0);
            /* 2021 */ mplew.writeInt(npc);
            /* 2022 */ mplew.writeMapleAsciiString(talk);
            /* 2023 */ mplew.writeInt(0);
            /* 2024 */ mplew.writeInt(0);
            /* 2025 */ mplew.writeInt(0);

            /* 2027 */ return mplew.getPacket();
        }

        public static byte[] getEvanTutorial(String data) {
            /* 2031 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2033 */ mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
         //   System.err.println("getEvanTutorial");
            mplew.writeInt(0); // 360 new
            /* 2035 */ mplew.write(8);
            /* 2036 */ mplew.writeInt(0);
            /* 2037 */ mplew.write(1);
            /* 2038 */ mplew.write(1);
            /* 2039 */ mplew.writeShort(0);
            /* 2040 */ mplew.write(1);
            /* 2041 */ mplew.writeMapleAsciiString(data);
            /* 2042 */ mplew.writeInt(0);

            /* 2044 */ return mplew.getPacket();
        }

        public static byte[] getNPCShop(int sid, MapleShop shop, MapleClient c) {
            /* 2048 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2050 */ mplew.writeShort(SendPacketOpcode.OPEN_NPC_SHOP.getValue());
            /* 2051 */ mplew.writeInt(sid);
            /* 2052 */ mplew.write(0);

            /* 2054 */ mplew.writeInt(0);
            /* 2055 */ mplew.writeInt(0);
            /* 2056 */ mplew.writeInt(0);
            /* 2059 */ PacketHelper.addShopInfo(mplew, shop, c);
            /* 2060 */ return mplew.getPacket();
        }

        public static byte[] BossRewardSetting() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.OPEN_NPC_SHOP.getValue() + 2);
            List<Triple<Integer, Integer, Integer>> list = BossRewardMeso.getLists();
            mplew.write(1);
            mplew.writeInt(1);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(2);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(8);
            for (int i = 0; i < 90; i++) {
                mplew.writeLong(PacketHelper.getTime(-2));
                mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)));
                mplew.writeInt(list.size());
                for (Triple<Integer, Integer, Integer> info : list) {
                    mplew.writeInt(info.getMid());
                    mplew.writeInt(info.getRight());
                    mplew.writeInt(0);
                    mplew.writeInt(info.getLeft());
                }
            }
            return mplew.getPacket();
        }

        public static byte[] confirmShopTransactionItem(byte code, MapleShop shop, MapleClient c, int indexBought, int itemId, int quantity) {
            /* 2064 */ return confirmShopTransactionItem(code, shop, c, indexBought, itemId, false, false, quantity);
        }

        public static byte[] confirmShopTransactionItem(byte code, MapleShop shop, MapleClient c, int indexBought, int itemId) {
            /* 2068 */ return confirmShopTransactionItem(code, shop, c, indexBought, itemId, false, false, 999999);
        }

        public static byte[] ShopItemInfoReset(MapleShop shop, MapleClient c, int itemId, int bought, int itemposition) {
            /* 2072 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /* 2073 */ mplew.writeShort(SendPacketOpcode.SHOP_INFO_RESET.getValue());
            /* 2074 */ mplew.writeInt(shop.getId());
            /* 2075 */ mplew.writeShort(itemposition);
            /* 2076 */ mplew.writeInt(itemId);
            /* 2077 */ mplew.writeShort(bought);
            /* 2078 */ mplew.writeLong(PacketHelper.getTime(-2L));
            /* 2079 */ return mplew.getPacket();
        }

        public static byte[] confirmShopTransactionItem(byte code, MapleShop shop, MapleClient c, int indexBought, int itemId, boolean repurchase, boolean limit, int quantity) {
            /* 2083 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CONFIRM_SHOP_TRANSACTION.getValue());
            mplew.write(code);

            switch (code) {
                case 0:
                    /* 2120 */ mplew.write(repurchase);
                    /* 2121 */ if (repurchase) {
                        /* 2122 */ mplew.writeInt(indexBought);
                        break;
                    }
                    /* 2124 */ mplew.writeInt(itemId);
                    /* 2125 */ mplew.writeInt(quantity);
                    /* 2126 */ mplew.writeInt(0);
                    break;

                case 4:
                    /* 2130 */ mplew.writeInt(0);
                    break;
                case 8:
                case 11:
                    /* 2134 */ mplew.writeInt(0);
                    /* 2135 */ mplew.writeInt(0);
                    /* 2136 */ mplew.writeInt(0);
                    /* 2139 */ PacketHelper.addShopInfo(mplew, shop, c);
                    break;
                case 21:
                case 22:
                    /* 2143 */ mplew.writeInt(0);
                    break;
                case 24:
                    /* 2146 */ mplew.writeInt(itemId);
                    break;
                case 27:
                    /* 2149 */ mplew.writeInt(0);
                    break;
                case 30:
                    /* 2152 */ mplew.writeInt(0);
                    break;
                case 33:
                    /* 2155 */ mplew.write(true);
                    /* 2156 */ mplew.writeInt(0);
                    /* 2157 */ mplew.writeInt(0);
                    /* 2158 */ mplew.writeInt(0);
                    /* 2159 */ mplew.writeInt(2);
                    /* 2160 */ mplew.writeInt(0);
                    /* 2161 */ PacketHelper.addShopInfo(mplew, shop, c);
                    break;
            }
            /* 2164 */ return mplew.getPacket();
        }

        public static byte[] getStorage(int npcId, short slots, Collection<Item> items, long meso) {
            /* 2168 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2170 */ mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            /* 2171 */ mplew.write(24);
            /* 2172 */ mplew.writeInt(npcId);
            /* 2173 */ mplew.write(128);
            /* 2174 */ mplew.writeLong(-1L);
            /* 2175 */ mplew.writeLong(meso);
            /* 2176 */ mplew.write(items.size());
            /* 2177 */ for (Item item : items) {
                /* 2178 */ PacketHelper.addItemInfo(mplew, item);
            }
            /* 2180 */ mplew.writeZeroBytes(5);

            /* 2182 */ return mplew.getPacket();
        }

        public static byte[] getStorage(byte status) {
            /* 2186 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2188 */ mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            /* 2189 */ mplew.write(23);
            /* 2190 */ mplew.write(status);

            /* 2192 */ return mplew.getPacket();
        }

        public static byte[] getStorageFull() {
            /* 2196 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2198 */ mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            /* 2199 */ mplew.write(17);

            /* 2201 */ return mplew.getPacket();
        }

        public static byte[] mesoStorage(short slots, long meso) {
            /* 2205 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2207 */ mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            /* 2208 */ mplew.write(19);
            /* 2209 */ mplew.write(slots);
            /* 2210 */ mplew.writeLong(2L);
            /* 2211 */ mplew.writeLong(meso);

            /* 2213 */ return mplew.getPacket();
        }

        public static byte[] arrangeStorage(short slots, Collection<Item> items, boolean changed) {
            /* 2217 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2219 */ mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            /* 2220 */ mplew.write(15);
            /* 2221 */ mplew.write(slots);
            /* 2222 */ mplew.writeLong(140737488355452L);
            /* 2223 */ mplew.write(items.size());
            /* 2224 */ for (Item item : items) {
                /* 2225 */ PacketHelper.addItemInfo(mplew, item);
            }
            /* 2227 */ mplew.writeZeroBytes(5);
            /* 2228 */ return mplew.getPacket();
        }

        public static byte[] storeStorage(short slots, MapleInventoryType type, Collection<Item> items) {
            /* 2232 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2234 */ mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            /* 2235 */ mplew.write(13);
            /* 2236 */ mplew.write(slots);
            /* 2237 */ mplew.writeLong(type.getBitfieldEncoding());
            /* 2238 */ mplew.write(items.size());
            /* 2239 */ for (Item item : items) {
                /* 2240 */ PacketHelper.addItemInfo(mplew, item);
            }
            /* 2242 */ return mplew.getPacket();
        }

        public static byte[] takeOutStorage(short slots, MapleInventoryType type, Collection<Item> items) {
            /* 2246 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2248 */ mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            /* 2249 */ mplew.write(9);
            /* 2250 */ mplew.write(slots);
            /* 2251 */ mplew.writeLong(type.getBitfieldEncoding());
            /* 2252 */ mplew.write(items.size());
            /* 2253 */ for (Item item : items) {
                /* 2254 */ PacketHelper.addItemInfo(mplew, item);
            }
            /* 2256 */ return mplew.getPacket();
        }

        public static byte[] detailShowInfo1(String msg, final int font, final int size, final long color) { // 보통 3,15 ,1
            MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
            packet.writeShort(SendPacketOpcode.DETAIL_SHOW_INFO.getValue());
            packet.writeInt(font); //font
            packet.writeInt(size); //font size
            packet.writeLong(color); //color
            packet.write(0);
            packet.writeMapleAsciiString(msg);
            return packet.getPacket();
        }

        public static byte[] getShopLimit(int shopid, int position, int itemid, int buyer) {
            /* 2260 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2262 */ mplew.writeShort(SendPacketOpcode.SHOP_LIMIT.getValue());
            /* 2263 */ mplew.writeInt(shopid);
            /* 2264 */ mplew.writeShort(position);
            /* 2265 */ mplew.writeInt(itemid);
            /* 2266 */ mplew.writeShort(buyer);
            /* 2267 */ mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis()));

            /* 2269 */ return mplew.getPacket();
        }

        public static byte[] setNpcNameInvisible(int npcid, boolean show) {
            /* 2273 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            /* 2275 */ mplew.writeShort(SendPacketOpcode.NPC_NAME_INVISIBLE.getValue());
            /* 2276 */ mplew.writeInt(npcid);
            /* 2277 */ mplew.write(show);

            /* 2279 */ return mplew.getPacket();
        }
    }

    public static class InteractionPacket {

        public static byte[] getTradeInvite(MapleCharacter c, boolean isTrade) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.INVITE_TRADE.action);
            mplew.write(isTrade ? 4 : 3);
            mplew.writeMapleAsciiString(c.getName());
            mplew.writeInt(c.getId());
            mplew.writeInt(c.getJob());
            return mplew.getPacket();
        }

        public static byte[] getMarriageInvite(MapleCharacter c) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.INVITE_TRADE.action);
            mplew.write(8);
            mplew.writeMapleAsciiString(c.getName());
            mplew.writeInt(c.getJob());
            mplew.writeInt(0);
            return mplew.getPacket();
        }

        public static byte[] getCashTradeInvite(MapleCharacter c) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.INVITE_TRADE.action);
            mplew.write(7);
            mplew.writeMapleAsciiString(c.getName());
            mplew.writeInt(c.getJob());
            mplew.writeInt(0);
           
            return mplew.getPacket();
        }

        public static byte[] getTradeMesoSet(byte number, long meso) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.SET_MESO1.action);
            mplew.write(number);
            mplew.writeLong(meso);
            return mplew.getPacket();
        }

        public static byte[] getTradeItemAdd(byte number, Item item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.SET_ITEMS1.action);
            mplew.write(number);
            mplew.write(item.getPosition());
            PacketHelper.addItemInfo(mplew, item);
            return mplew.getPacket();
        }

        public static byte[] getTradeStart(MapleClient c, MapleTrade trade, byte number, boolean isTrade) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(20);
            mplew.write(isTrade ? 4 : 3);
            mplew.write(2);
            mplew.write(number);
            if (number == 1) {
                mplew.write(0);
                AvatarLook.encodeAvatarLook(mplew, trade.getPartner().getChr(), false, false);
                mplew.writeMapleAsciiString(trade.getPartner().getChr().getName());
                mplew.writeShort(trade.getPartner().getChr().getJob());
                mplew.writeInt(0);
            }
            mplew.write(number);
            AvatarLook.encodeAvatarLook(mplew, c.getPlayer(), false, false);
            mplew.writeMapleAsciiString(c.getPlayer().getName());
            mplew.writeShort(c.getPlayer().getJob());
            mplew.writeInt(0);
            mplew.write(255);
            return mplew.getPacket();
        }

        public static byte[] getCashTradeStart(MapleClient c, MapleTrade trade, byte number) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(20);
            mplew.write(7);
            mplew.write(2);
            mplew.write(number);
            if (number == 1) {
                mplew.write(0);
                AvatarLook.encodeAvatarLook(mplew, trade.getPartner().getChr(), false, false);
                mplew.writeMapleAsciiString(trade.getPartner().getChr().getName());
                mplew.writeShort(trade.getPartner().getChr().getJob());
                mplew.writeInt(0);
            }
            mplew.write(number);
            AvatarLook.encodeAvatarLook(mplew, c.getPlayer(), false, false);
            mplew.writeMapleAsciiString(c.getPlayer().getName());
            mplew.writeShort(c.getPlayer().getJob());
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(0xFF);

            return mplew.getPacket();
        }

        public static byte[] getTradeConfirmation() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.CONFIRM_TRADE1.action);
            return mplew.getPacket();
        }

        public static byte[] TradeMessage(byte UserSlot, byte message) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.EXIT.action);
            mplew.write(UserSlot);
            mplew.write(message);
            return mplew.getPacket();
        }

        public static byte[] getTradeCancel(byte UserSlot) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.EXIT.action);
            mplew.write(UserSlot);
            mplew.write(2);
            return mplew.getPacket();
        }
    }

    public static byte[] getMacros(SkillMacro[] macros) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SKILL_MACRO.getValue());
        int count = 0;
        int i;
        for (i = 0; i < 5; i++) {
            if (macros[i] != null) {
                count++;
            }
        }
        mplew.write(count);
        for (i = 0; i < 5; i++) {
            SkillMacro macro = macros[i];
            if (macro != null) {
                mplew.writeMapleAsciiString(macro.getName());
                mplew.write(macro.getShout());
                mplew.writeInt(macro.getSkill1());
                mplew.writeInt(macro.getSkill2());
                mplew.writeInt(macro.getSkill3());
            }
        }
        return mplew.getPacket();
    }

    public static byte[] getCharInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WARP_TO_MAP.getValue());
        mplew.writeInt(chr.getClient().getChannel() - 1);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(1);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(1550);
        mplew.writeInt(2070);
        mplew.write(1);
        mplew.writeShort((ServerConstants.serverMessage.length() > 0) ? 1 : 0);
        if (ServerConstants.serverMessage.length() > 0) {
            mplew.writeMapleAsciiString(ServerConstants.serverMessage);
            mplew.writeMapleAsciiString(ServerConstants.serverMessage);
        }
        int seed1 = Randomizer.nextInt();
        int seed2 = Randomizer.nextInt();
        int seed3 = Randomizer.nextInt();
        chr.getCalcDamage().SetSeed(seed1, seed2, seed3);
        mplew.writeInt(seed1);
        mplew.writeInt(seed2);
        mplew.writeInt(seed3);

        PacketHelper.addCharacterInfo(mplew, chr);

        mplew.write(true);
        mplew.write(0);
        mplew.writeLong(PacketHelper.getTime(-2L));
        mplew.write(0);
        mplew.writeLong(PacketHelper.getTime(-2L));
        mplew.write(false);
        mplew.write(false);

        mplew.write(0); //355
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeInt(100);
        mplew.write(0);

        mplew.write(0);
        mplew.write(GameConstants.isPhantom(chr.getJob()) ? 0 : 1);
        if (chr.getMapId() / 10 == 10520011 || chr.getMapId() / 10 == 10520051 || chr.getMapId() == 105200519) {
            mplew.write(0);
        }

        mplew.write(0);

        mplew.writeInt(0);

        mplew.write(0);
        mplew.writeInt(0);

        mplew.writeInt(0);

        mplew.write(true);
        mplew.writeInt(-1);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(999999999);
        mplew.writeInt(999999999);
        mplew.writeMapleAsciiString("");

        boolean sundayMaple = false;
        Date time = new Date();
        int day = time.getDay();
        if (day == 4 || day == 5 || day == 6 || day == 0) {
            sundayMaple = true;
        }
        mplew.write(sundayMaple);
        if (sundayMaple) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
            Date date = new Date();
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            cal.setTime(date);
            cal.add(5, 7 - cal.get(7));
            String day1 = sdf.format(cal.getTime());
            cal.setTime(date);
            cal.add(5, 8 - cal.get(7));
            String day2 = sdf.format(cal.getTime());
            Calendar now = Calendar.getInstance();
            ServerConstants.SundayMapleTEXTLINE_2 = "#fn나눔고딕 ExtraBold##fc0xFFB7EC00#" + day1 + " ~ " + day2;
            String a = "";
            for (WeekendMaple maple : WeekendMaple.values()) {
                if (WeekendMaple.hasEvent(maple, GameConstants.getWeek_WeekendMaple())) {
                    String days = (maple.getDate() == 0) ? "토" : "일";
                    a = a + "#sunday# #fn나눔고딕 ExtraBold##fs18##fc0xFFFAF4C0#" + maple.getEvent() + " (" + days + ")\r\n\r\n";
                }
            }
            ServerConstants.SundayMapleTEXTLINE_1 = a;
            mplew.writeMapleAsciiString(ServerConstants.SundayMapleUI);
            mplew.writeMapleAsciiString(ServerConstants.SundayMapleTEXTLINE_1);
            mplew.writeMapleAsciiString(ServerConstants.SundayMapleTEXTLINE_2);
            mplew.writeInt(60);
            mplew.writeInt(220);
        }
        mplew.writeInt(0);
        mplew.write(false);
        mplew.writeInt(0);
        mplew.write(false); //360 new
        return mplew.getPacket();
    }

    public static byte[] getWarpToMap(MapleMap to, int spawnPoint, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WARP_TO_MAP.getValue());
        mplew.writeInt(chr.getClient().getChannel() - 1);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(2);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(to.getId());
        mplew.write(spawnPoint);
        mplew.writeInt(chr.getStat().getHp());
        mplew.write(false);
        mplew.write(0); //355 new
        mplew.write(GameConstants.보스맵(to.getId()) ? 1 : 0);
        mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis()));
        mplew.writeInt(100);
        mplew.write(0);
        mplew.write(0);
        mplew.write(GameConstants.isPhantom(chr.getJob()) ? 0 : 1);
        if (to.getId() / 10 == 10520011 || to.getId() / 10 == 10520051 || to.getId() == 105200519) {
            mplew.write(0);
        }
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeShort(0);
        mplew.write(true);
        mplew.writeInt(-1);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(999999999);
        mplew.writeInt(999999999);
        mplew.writeMapleAsciiString("");
        mplew.write(false);
        mplew.writeInt(0);
        if (chr.getMap().getFieldType().equals("63")) {
            mplew.write(0);
        }
        mplew.write(false);
        mplew.write(true); //360 new
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] showEquipEffect() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_EQUIP_EFFECT.getValue());
        return mplew.getPacket();
    }

    public static byte[] showEquipEffect(int team) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_EQUIP_EFFECT.getValue());
        mplew.writeShort(team);
        return mplew.getPacket();
    }

    public static byte[] multiChat(MapleCharacter chr, String chattext, int mode, Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort((item == null) ? SendPacketOpcode.MULTICHAT.getValue() : SendPacketOpcode.MULTICHATITEM.getValue());
        mplew.write(mode);
        mplew.writeInt(chr.getAccountID());
        mplew.writeInt(chr.getId());
        mplew.writeMapleAsciiString(chr.getName());
        mplew.writeMapleAsciiString(chattext);
        PacketHelper.ChatPacket(mplew, chr.getName(), chattext);
        mplew.write((item != null));
        if (item != null) {
            PacketHelper.addItemInfo(mplew, item);
            mplew.writeMapleAsciiString(MapleItemInformationProvider.getInstance().getName(item.getItemId()));
        }
        return mplew.getPacket();
    }

    public static byte[] getFindReplyWithCS(String target, boolean buddy) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(buddy ? 72 : 9);
        mplew.writeMapleAsciiString(target);
        mplew.write(2);
        mplew.writeInt(-1);
        return mplew.getPacket();
    }

    public static byte[] getWhisper(String sender, int channel, String text, Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(18);
        mplew.writeMapleAsciiString(sender);
        mplew.writeInt(0);
        mplew.writeShort(channel - 1);
        mplew.writeMapleAsciiString(text);
        PacketHelper.ChatPacket(mplew, sender, text);
        mplew.write((item != null));
        if (item != null) {
            PacketHelper.addItemInfo(mplew, item);
            mplew.writeMapleAsciiString(MapleItemInformationProvider.getInstance().getName(item.getItemId()));
        }
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] getWhisperReply(String target, byte reply) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(10);
        mplew.writeMapleAsciiString(target);
        mplew.write(reply);
        return mplew.getPacket();
    }

    public static byte[] getWhisperReply(String target, byte write, byte reply) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(write);
        mplew.writeMapleAsciiString(target);
        mplew.write(reply);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] getFindReplyWithMap(String target, int mapid, boolean buddy) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(buddy ? 72 : 9);
        mplew.writeMapleAsciiString(target);
        mplew.write(1);
        mplew.writeInt(mapid);
        mplew.writeZeroBytes(8);
        return mplew.getPacket();
    }

    public static byte[] getFindReply(String target, int channel, boolean buddy) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(buddy ? 72 : 9);
        mplew.writeMapleAsciiString(target);
        mplew.write(3);
        mplew.writeInt(channel - 1);
        return mplew.getPacket();
    }

    public static final byte[] MapEff(String path) {
        return environmentChange(path, 12);
    }

    public static final byte[] MapNameDisplay(int mapid) {
        return environmentChange("maplemap/enter/" + mapid, 12);
    }

    public static final byte[] Aran_Start() {
        return environmentChange("Aran/balloon", 4);
    }

    public static byte[] getSelectPower(int type, int code) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BLACK_MAGE_TAMPORARY_SKILL.getValue());
        mplew.writeInt(type);
        mplew.writeInt(code);
        switch (type) {
            case 8:
                mplew.writeInt(1);
                mplew.writeInt(80002623);
                mplew.writeInt(3);
                mplew.writeInt(1);
                mplew.writeInt(1278807629);
                break;
            case 9:
                mplew.writeInt(80002623);
                break;
        }
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] FlagRaceSkill(int... args) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BLACK_MAGE_TAMPORARY_SKILL.getValue());
        mplew.writeInt(args[0]);
        switch (args[0]) {
            case 5:
                mplew.writeInt(args[1]);
                mplew.writeInt(args[2]);
                mplew.writeInt(args[3]);
                mplew.writeInt(0);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] musicChange(String song) {
        return environmentChange(song, 7);
    }

    public static byte[] showEffect(String effect) {
        return environmentChange(effect, 4);
    }

    public static byte[] playSound(String sound) {
        return environmentChange(sound, 5);
    }

    public static byte[] environmentChange(String env, int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(mode);
        mplew.writeMapleAsciiString(env);
        if (mode != 4 && mode != 11 && mode != 19 && mode != 20 && mode != 16) {
            mplew.writeInt(100);
        }
        if (mode == 7 || mode == 19) {
            mplew.writeInt(0);
        }
        if (mode == 20) {
            mplew.write(7);
            mplew.write(1);
        }
        mplew.writeInt(-1);
        return mplew.getPacket();
    }

    public static byte[] KaiserChangeColor(int cid, int color1, int color2, byte premium) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.KAISER_CHANGE_COLOR.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(color1);
        mplew.writeInt(color2);
        mplew.write(premium);
        return mplew.getPacket();
    }

    public static byte[] trembleEffect(int type, int delay) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(1);
        mplew.write(type);
        mplew.writeInt(delay);
        return mplew.getPacket();
    }

    public static byte[] environmentMove(String env, int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MOVE_ENV.getValue());
        mplew.writeMapleAsciiString(env);
        mplew.writeInt(mode);
        return mplew.getPacket();
    }

    public static byte[] getUpdateEnvironment(List<MapleNodes.Environment> list) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_ENV.getValue());
        mplew.writeInt(list.size());
        for (MapleNodes.Environment mp : list) {
            mplew.writeMapleAsciiString(mp.getName());
            mplew.write(false);
            mplew.writeInt(mp.isShow() ? 1 : 0);
            mplew.writeInt(mp.getX());
            mplew.writeInt(mp.getY());
        }
        return mplew.getPacket();
    }

    public static byte[] startMapEffect(String msg, int itemid, boolean active) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MAP_EFFECT.getValue());
        mplew.writeInt(itemid);
        if (active) {
            mplew.writeMapleAsciiString(msg);
            mplew.writeInt((itemid == 5120025) ? 3872 : ((itemid == 5121101 || itemid == 5121041) ? 10 : ((itemid == 5121112 || itemid == 5121113 || itemid == 5121114 || itemid == 5121115) ? 600 : 3)));
            mplew.write(0);
        }
        return mplew.getPacket();
    }

    public static byte[] removeMapEffect() {
        return startMapEffect(null, 0, false);
    }

    public static byte[] getPVPClock(int type, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(3);
        mplew.write(type);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] getVanVanClock(byte type, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(5);
        mplew.write(type);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] getTrueRoomClock(int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(2);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] getDojoClockStop(boolean stop, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(7);
        mplew.write(stop);
        mplew.writeInt(time);
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] getDojoClock(int endtime, int starttime) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(8);
        mplew.writeInt(endtime);
        mplew.writeInt(starttime);
        return mplew.getPacket();
    }

    public static byte[] getClock(int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(2);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static final byte[] VonVonStopWatch(int timer) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(4);
        mplew.writeInt(20000);
        mplew.writeInt(timer);
        return mplew.getPacket();
    }

    public static byte[] getClockMilliEvent(long time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(13);
        mplew.writeLong(PacketHelper.getTime(time));
        return mplew.getPacket();
    }

    public static byte[] getClockTime(int hour, int min, int sec) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(1);
        mplew.write(hour);
        mplew.write(min);
        mplew.write(sec);
        return mplew.getPacket();
    }

    public static byte[] PunchKingPacket(MapleCharacter chr, int type, int... args) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PUNCH_KING.getValue());
        mplew.writeInt(type);
        switch (type) {
            case 0:
            case 1:
                mplew.writeInt(args[0]);
                break;
            case 2:
                mplew.writeInt(args[0]);
                break;
            case 3:
                mplew.writeInt(args[0]);
                mplew.writeInt(args[1]);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] boatPacket(int effect, int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOAT_MOVE.getValue());
        mplew.write(effect);
        mplew.write(mode);
        return mplew.getPacket();
    }

    public static byte[] stopClock() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.STOP_CLOCK.getValue());
        return mplew.getPacket();
    }

    public static byte[] achievementRatio(int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ACHIEVEMENT_RATIO.getValue());
        mplew.writeInt(amount);
        return mplew.getPacket();
    }

   public static byte[] spawnPlayerMapobject(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_PLAYER.getValue());
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getGuildId());
        mplew.writeInt(chr.getLevel());
        mplew.writeMapleAsciiString(chr.getName());
        mplew.writeMapleAsciiString("");
        mplew.writeInt(chr.getGuildId());
        MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
        if (gs != null) {
            mplew.writeMapleAsciiString(gs.getName());
            mplew.writeShort(gs.getLogoBG());
            mplew.write(gs.getLogoBGColor());
            mplew.writeShort(gs.getLogo());
            mplew.write(gs.getLogoColor());
            mplew.writeInt((gs.getCustomEmblem() != null) ? gs.getId() : 0);
            mplew.writeInt((gs.getCustomEmblem() != null) ? 1 : 0);
        } else {
            mplew.writeLong(0L);
            mplew.writeLong(0L);
        }
        mplew.write(chr.getGender());
        mplew.writeInt(chr.getFame());
        mplew.writeInt(1);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(0);
        Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
        chr.getEffects().stream().forEach(effect -> {
            if (effect.getLeft() != SecondaryStat.EnergyCharged) {
                statups.put(effect.getLeft(), new Pair<>(Integer.valueOf(((SecondaryStatValueHolder) effect.getRight()).value), Integer.valueOf(((SecondaryStatValueHolder) effect.right).localDuration)));
            }
        });
        if (chr.getKaiserCombo() > 0) {
            statups.put(SecondaryStat.SmashStack, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
        }

        PacketHelper.writeBuffMask(mplew, PacketHelper.sortBuffStats(statups));
        PacketHelper.encodeForRemote(mplew, statups, chr);
        mplew.writeShort(chr.getJob());
        mplew.writeShort(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        AvatarLook.encodeAvatarLook(mplew, chr, true, false);
        if (GameConstants.isZero(chr.getJob())) {
            AvatarLook.encodeAvatarLook(mplew, chr, true, true);
        }
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt((chr.getKeyValue(27038, "itemid") <= 0L) ? 0L : chr.getKeyValue(27038, "itemid"));
        mplew.writeInt(0);
        mplew.writeInt(chr.getKeyValue(19019, "id"));
        mplew.write(0);
        MapleQuestStatus stat = chr.getQuestNoAdd(MapleQuest.getInstance(7291));
        mplew.writeInt((stat != null && stat.getCustomData() != null) ? Integer.valueOf(stat.getCustomData()).intValue() : 0);
        mplew.writeInt((stat != null && stat.getCustomData() != null) ? Integer.valueOf(stat.getCustomData()).intValue() : 0);
        mplew.writeInt(0);
        mplew.writeMapleAsciiString("");
        mplew.writeMapleAsciiString("");
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(true);
        mplew.writeInt(0);
        mplew.writeShort(-1);
        mplew.writeInt(chr.getChair());
        mplew.writeInt(0); // 1.2.355++
        mplew.writePos((chr.getSkillCustomValue(201212) != null) ? new Point((chr.getPosition()).x, (chr.getPosition()).y - 30) : chr.getTruePosition());
        mplew.write((chr.getSkillCustomValue(201212) != null) ? 0 : chr.getStance());
        mplew.writeShort((chr.getSkillCustomValue(201212) != null) ? 0 : chr.getFH());
        mplew.write((chr.getChair() != 0));
        if (chr.getChair() != 0) {
            PacketHelper.chairPacket(mplew, chr, chr.getChair());
        }
        int petindex = 0;
        if ((chr.getPets()).length > 0 && chr.getMapId() != ServerConstants.warpMap) {
            for (MaplePet pet : chr.getPets()) {
                if (pet != null) {
                    mplew.write(true);
                    mplew.writeInt(petindex++);
                    mplew.writeInt(pet.getPetItemId());
                    mplew.writeMapleAsciiString(pet.getName());
                    mplew.writeLong(pet.getUniqueId());
                    mplew.writeShort((pet.getPos()).x);
                    mplew.writeShort((pet.getPos()).y - 20);
                    mplew.write(pet.getStance());
                    mplew.writeShort(pet.getFh());
                    mplew.writeInt(pet.getColor());
                    mplew.writeShort(pet.getWonderGrade());
                    mplew.writeShort(pet.getPetSize());
                    mplew.write(0);
                    mplew.write(0);
                }
            }
        }
        mplew.write(false);
        mplew.write(false);
        mplew.writeInt(chr.getMount().getLevel());
        mplew.writeInt(chr.getMount().getExp());
        mplew.writeInt(chr.getMount().getFatigue());
        mplew.write(0);
        PacketHelper.addAnnounceBox(mplew, chr);
        mplew.write((chr.getChalkboard() != null && chr.getChalkboard().length() > 0) ? 1 : 0);
        if (chr.getChalkboard() != null && chr.getChalkboard().length() > 0) {
            mplew.writeMapleAsciiString(chr.getChalkboard());
        }
        Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> rings = chr.getRings(false);
        addRingInfo(mplew, rings.getLeft());
        addRingInfo(mplew, rings.getMid());
        addMRingInfo(mplew, rings.getRight(), chr);

        mplew.write(true);

        byte flag = 0;
        if (chr.getSkillLevel(1320016) > 0 && chr.getJob() == 132) {
            flag = (byte) (flag | 0x1);
        }
        if (GameConstants.isEvan(chr.getJob())) {
            flag = (byte) (flag | 0x2);
        }
        mplew.writeInt(0);
        mplew.write(flag);
        mplew.writeInt(0);
        if (GameConstants.isKaiser(chr.getJob())) {
            mplew.writeInt((chr.getKeyValue(12860, "extern") == -1L) ? 0L : chr.getKeyValue(12860, "extern"));
            mplew.writeInt((chr.getKeyValue(12860, "inner") == -1L) ? 1L : chr.getKeyValue(12860, "inner"));
            mplew.write((chr.getKeyValue(12860, "premium") == -1L) ? 0 : (byte) (int) chr.getKeyValue(12860, "premium"));
        }
        mplew.writeInt(0);
        PacketHelper.addFarmInfo(mplew, chr.getClient(), 0);
        for (int i = 0; i < 5; i++) {
            mplew.write(-1);
        }
        mplew.writeInt(0);
        mplew.write(1);
        if (chr.getBuffedValue(SecondaryStat.RideVehicle) != null && chr.getBuffedValue(SecondaryStat.RideVehicle).intValue() == 1932249) {
            mplew.writeInt(0);
        }
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.write((chr.getBuffedEffect(SecondaryStat.KinesisPsychicEnergeShield) != null) ? 1 : 0);
        mplew.write((chr.getKeyValue(1544, "20040217") == 1L));
        mplew.write((chr.getKeyValue(1544, "20040219") == 1L));
        mplew.writeInt(1051291);
        mplew.write(false);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] removePlayerFromMap(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_PLAYER_FROM_MAP.getValue());
        mplew.writeInt(cid);
        return mplew.getPacket();
    }

    public static byte[] getChatText(MapleCharacter chr, String text, boolean whiteBG, int show, Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort((item == null) ? SendPacketOpcode.CHATTEXT.getValue() : SendPacketOpcode.CHATTEXTITEM.getValue());
        int emoticon = 0;
        if (show == 11) {
            emoticon = Integer.parseInt(text.replace(":", ""));
            text = "";
        }
        mplew.writeInt(chr.getId());
        mplew.write(whiteBG ? 1 : 0);
        mplew.writeMapleAsciiString(text);
        PacketHelper.ChatPacket(mplew, chr.getName(), text);
        mplew.write(show);
        mplew.write(0);
        if (item != null) {
            mplew.write(1);
            mplew.writeInt(1);
            mplew.write(true);
            PacketHelper.addItemInfo(mplew, item);
            mplew.writeMapleAsciiString(MapleItemInformationProvider.getInstance().getName(item.getItemId()));
        } else if (show == 11) {
            mplew.write(5);
            mplew.writeInt(emoticon);
        } else {
            mplew.write(false);
        }
        return mplew.getPacket();
    }

    public static byte[] getUniverseChat(boolean disableworldname, String name, String text) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MULTICHAT.getValue() + 2);
        mplew.write(disableworldname ? 1 : 0);
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(text);
        return mplew.getPacket();
    }

    public static byte[] getScrollEffect(int chr, Equip.ScrollResult scrollSuccess, boolean legendarySpirit, int scrollid, int victimid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_SCROLL_EFFECT.getValue());
        mplew.writeInt(chr);
        switch (scrollSuccess) {
            case SUCCESS:
                mplew.write(1);
                mplew.write(legendarySpirit ? 1 : 0);
                mplew.writeInt(scrollid);
                mplew.writeInt(victimid);
                break;
            case FAIL:
                mplew.write(0);
                mplew.write(legendarySpirit ? 1 : 0);
                mplew.writeInt(scrollid);
                mplew.writeInt(victimid);
                break;
            case CURSE:
                mplew.write(2);
                mplew.write(legendarySpirit ? 1 : 0);
                mplew.writeInt(scrollid);
                mplew.writeInt(victimid);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] showMagnifyingEffect(int chr, short pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_MAGNIFYING_EFFECT.getValue());
        mplew.writeInt(chr);
        mplew.writeShort(pos);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] showPotentialReset(int chr, boolean success, int itemid, int equipId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_POTENTIAL_RESET.getValue());
        mplew.writeInt(chr);
        mplew.write(success ? 1 : 0);
        mplew.writeInt(itemid);
        mplew.writeInt(0);
        mplew.writeInt(equipId);
        return mplew.getPacket();
    }

    public static byte[] getRedCubeStart(MapleCharacter chr, Item item, boolean up, int cubeId, int remainCount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_REDCUBE_EFFECT.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(up);
        mplew.writeInt(cubeId);
        mplew.writeInt(item.getPosition());
        mplew.writeInt(remainCount);
        PacketHelper.addItemInfo(mplew, item);
        return mplew.getPacket();
    }

    public static byte[] getCubeStart(MapleCharacter chr, Item item, boolean up, int cubeId, int remainCount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        boolean adi = false;
        switch (cubeId) {
            case 2730000:
            case 2730001:
            case 2730002:
            case 2730004:
            case 2730005:
                adi = true;
                break;
        }
        mplew.writeShort(adi ? (SendPacketOpcode.SHOW_CUBE_EFFECT.getValue() + 1) : SendPacketOpcode.SHOW_CUBE_EFFECT.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(up);
        mplew.writeInt(cubeId);
        mplew.writeInt(item.getPosition());
        mplew.writeInt(remainCount);
        PacketHelper.addItemInfo(mplew, item);
        return mplew.getPacket();
    }

    public static byte[] getEditionalCubeStart(MapleCharacter chr, Item item, boolean up, int cubeId, int remainCount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_EDITIONALCUBE_EFFECT.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(up);
        mplew.writeInt(cubeId);
        mplew.writeInt(item.getPosition());
        mplew.writeInt(remainCount);
        PacketHelper.addItemInfo(mplew, item);
        return mplew.getPacket();
    }

    public static byte[] getWhiteCubeStart(MapleCharacter chr, Item item, boolean up, int cubeId, int cubePosition) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WHITE_CUBE_WINDOW.getValue());
        mplew.writeLong((item.getInventoryId() <= 0L) ? -1L : item.getInventoryId());
        mplew.write(1);
        PacketHelper.addItemInfo(mplew, item);
        mplew.writeInt(cubeId);
        mplew.writeInt(item.getPosition());
        mplew.writeInt(cubePosition);
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] getBlackCubeStart(MapleCharacter chr, Item item, boolean up, int cubeId, int cubePosition, int remainCount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BLACK_CUBE_WINDOW.getValue());
        /* 3423 */ mplew.writeLong((item.getInventoryId() <= 0L) ? -1L : item.getInventoryId());
        /* 3424 */ mplew.write(1);
        /* 3425 */ PacketHelper.addItemInfo(mplew, item);
        /* 3426 */ mplew.writeInt(cubeId);
        /* 3427 */ mplew.writeInt(item.getPosition());
        /* 3428 */ mplew.writeInt(remainCount);
        /* 3429 */ mplew.writeInt(cubePosition);
        mplew.writeZeroBytes(10);
        return mplew.getPacket();
    }

    public static byte[] getBlackCubeEffect(int cid, boolean up, int cubeId, int equipId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_BLACKCUBE_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.write(1);
        mplew.writeInt(cubeId);
        mplew.writeInt(2460000);
        mplew.writeInt(equipId);
        return mplew.getPacket();
    }

    public static byte[] getAnvilStart(Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
        mplew.write(0);
        mplew.write(2);
        mplew.write(0);
        mplew.write(3);
        mplew.write(1);
        mplew.writeShort(item.getPosition());
        mplew.write(0);
        mplew.write(0);
        mplew.write(1);
        mplew.writeShort(item.getPosition());
        PacketHelper.addItemInfo(mplew, item);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] showEnchanterEffect(int cid, byte result) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_ENCHANTER_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.write(result);
        return mplew.getPacket();
    }

    public static byte[] showSoulScrollEffect(int cid, byte result, boolean destroyed, Equip equip) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_SOULSCROLL_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.write(result);
        mplew.write(destroyed ? 1 : 0);
        mplew.writeInt(equip.getItemId());
        mplew.writeInt(equip.getSoulPotential());
        return mplew.getPacket();
    }

    public static byte[] showSoulEffect(MapleCharacter chr, byte on) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.SHOW_SOULEFFECT_RESPONSE.getValue());
        packet.writeInt(chr.getId());
        packet.write(on);
        return packet.getPacket();
    }

     public static byte[] showSoulEffect(MapleCharacter chr, byte use, int skillid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_SOUL_EFFECT.getValue());
        mplew.writeInt(use);
        mplew.writeInt(skillid);
        mplew.writeInt(chr.getId());
        return mplew.getPacket();
    }

    public static byte[] teslaTriangle(int cid, int sum1, int sum2, int sum3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TESLA_TRIANGLE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(sum1);
        mplew.writeInt(sum2);
        mplew.writeInt(sum3);
        return mplew.getPacket();
    }

    public static byte[] harvestResult(int cid, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HARVESTED.getValue());
        mplew.writeInt(cid);
        mplew.write(success ? 0 : 1);
        return mplew.getPacket();
    }

    public static byte[] playerDamaged(int cid, int dmg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PLAYER_DAMAGED.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(dmg);
        mplew.write(false);
        return mplew.getPacket();
    }

    public static byte[] spawnDragon(MapleDragon d) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DRAGON_SPAWN.getValue());
        mplew.writeInt(d.getOwner());
        mplew.writeInt((d.getPosition()).x);
        mplew.writeInt((d.getPosition()).y);
        mplew.write(d.getStance());
        mplew.writeShort(0);
        mplew.writeShort(d.getJobId());
        return mplew.getPacket();
    }

    public static byte[] removeDragon(int chrid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DRAGON_REMOVE.getValue());
        mplew.writeInt(chrid);
        return mplew.getPacket();
    }

    public static byte[] moveDragon(MapleDragon d, Point startPos, List<LifeMovementFragment> moves) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DRAGON_MOVE.getValue());
        mplew.writeInt(d.getOwner());
        mplew.writeInt(0);
        mplew.writePos(startPos);
        mplew.writeInt(0);
        PacketHelper.serializeMovementList(mplew, moves);
        return mplew.getPacket();
    }

    public static byte[] spawnAndroid(MapleCharacter cid, MapleAndroid android) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ANDROID_SPAWN.getValue());
        mplew.writeInt(cid.getId());
        mplew.write(GameConstants.getAndroidType(android.getItemId()));
        mplew.writePos((cid.getSkillCustomValue(201212) != null) ? new Point((android.getPos()).x, (android.getPos()).y - 30) : android.getPos());
        mplew.write((cid.getSkillCustomValue(201212) != null) ? 0 : cid.getStance());
        mplew.writeShort((cid.getSkillCustomValue(201212) != null) ? 0 : cid.getFH());
        mplew.writeInt(0);
        mplew.writeShort(android.getSkin());
        mplew.writeShort(android.getHair() - 30000);
        mplew.writeShort(0); // 1.2.355 ++ 
        mplew.writeShort(android.getFace() - 20000);
        mplew.writeShort(0); // 1.2.355 ++ 
        mplew.writeMapleAsciiString(android.getName());
        mplew.writeInt(android.getEar() ? 0 : 1032024);
        mplew.writeLong(PacketHelper.getTime(-2L));
        for (short i = -1200; i > -1207; i = (short) (i - 1)) {
            Item item = cid.getInventory(MapleInventoryType.EQUIPPED).getItem(i);
            mplew.writeInt((item != null) ? item.getItemId() : 0);
        }
        return mplew.getPacket();
    }

    public static byte[] moveAndroid(int cid, Point pos, List<LifeMovementFragment> res, int unk1, int unk2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ANDROID_MOVE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(0);
        mplew.writePos(pos);
        mplew.writeInt(0);
        PacketHelper.serializeMovementList(mplew, res);
        return mplew.getPacket();
    }

    public static byte[] showAndroidEmotion(int cid, int animation) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ANDROID_EMOTION.getValue());
        mplew.writeInt(cid);
        mplew.write(0);
        mplew.write(animation);
        return mplew.getPacket();
    }

    public static byte[] spawnHaku(MapleCharacter cid, MapleHaku haku) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HAKU_SPAWN.getValue());
        mplew.writeInt(cid.getId());
        mplew.writeShort(1);
        mplew.writePos(haku.getPos());
        mplew.write(haku.getStance());
        mplew.writeShort(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] moveHaku(int cid, Point pos, List<LifeMovementFragment> res) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HAKU_MOVE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(0);
        mplew.writePos(pos);
        mplew.writeInt(2147483647);
        PacketHelper.serializeMovementList(mplew, res);
        return mplew.getPacket();
    }

    public static byte[] updateAndroidLook(boolean itemOnly, MapleCharacter cid, MapleAndroid android) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ANDROID_UPDATE.getValue());
        mplew.writeInt(cid.getId());
        mplew.write(itemOnly ? 1 : 0);
        if (itemOnly) {
            short i;
            for (i = -1200; i > -1207; i = (short) (i - 1)) {
                Item item = cid.getInventory(MapleInventoryType.EQUIPPED).getItem(i);
                mplew.writeInt((item != null) ? item.getItemId() : 0);
            }
        } else {
            mplew.writeShort(0);
            mplew.writeShort(android.getHair() - 30000);
            mplew.writeShort(android.getFace() - 20000);
            mplew.writeMapleAsciiString(android.getName());
        }
        return mplew.getPacket();
    }

    public static byte[] deactivateAndroid(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ANDROID_DEACTIVATED.getValue());
        mplew.writeInt(cid);
        return mplew.getPacket();
    }

    public static byte[] NameChanger(byte status) {
        return NameChanger(status, 0);
    }

    public static byte[] NameChanger(byte status, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.NAME_CHANGER.getValue());
        mplew.write(status);
        if (status == 9) {
            mplew.writeInt(itemid);
        }
        return mplew.getPacket();
    }

    public static byte[] movePlayer(int cid, List<LifeMovementFragment> moves, Point startPos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MOVE_PLAYER.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(0);
        mplew.writePos(startPos);
        mplew.writePos(new Point(0, 0));
        PacketHelper.serializeMovementList(mplew, moves);
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] addAttackInfo(int type, MapleCharacter chr, AttackInfo attack) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (type == 0) {
            mplew.writeShort(SendPacketOpcode.CLOSE_RANGE_ATTACK.getValue());
        } else if (type == 1 || type == 2) {
            mplew.writeShort(SendPacketOpcode.RANGED_ATTACK.getValue());
        } else if (type == 3) {
            mplew.writeShort(SendPacketOpcode.MAGIC_ATTACK.getValue());
        } else {
            mplew.writeShort(SendPacketOpcode.BUFF_ATTACK.getValue());
        }
        mplew.writeInt(chr.getId());
        mplew.write(GameConstants.isEvan(chr.getJob()));
        mplew.write(attack.tbyte);
        mplew.writeInt(chr.getLevel());
        mplew.writeInt(attack.skilllevel);
        if (attack.skilllevel > 0) {
            mplew.writeInt(attack.skill);
        }
        if (GameConstants.isZeroSkill(attack.skill)) {
            mplew.write(attack.asist);
            if (attack.asist > 0) {
                mplew.writePos(attack.position);
            }
        }
        if ((type == 1 || type == 2) && (GameConstants.bullet_count_bonus(attack.skill) != 0
                || GameConstants.attack_count_bonus(attack.skill) != 0)) {
            int passiveId = 0;
            int passiveLv = 0;
            if (GameConstants.bullet_count_bonus(attack.skill) == 0) {
                if (GameConstants.attack_count_bonus(attack.skill) == 0) {
                    passiveId = 0;
                    passiveLv = 0;
                } else {
                    passiveId = GameConstants.attack_count_bonus(attack.skill);
                    passiveLv = chr.getSkillLevel(passiveId);
                }
            } else {
                passiveId = GameConstants.bullet_count_bonus(attack.skill);
                passiveLv = chr.getSkillLevel(passiveId);
            }
            mplew.writeInt(passiveLv);
            if (passiveLv != 0) {
                mplew.writeInt(passiveId);
            }
        }
        if (attack.skill == 80001850) {
            int passiveLv = chr.getSkillLevel(80001851);
            mplew.writeInt(passiveLv);
            if (passiveLv != 0) {
                mplew.writeInt(80001851);
            }
        }
        mplew.write((attack.skill == 5220023 || attack.skill == 5220024 || attack.skill == 5220025 || attack.skill == 95001000 || attack.skill == 21001008) ? 4 : attack.isShadowPartner);
        mplew.write(attack.isBuckShot);
        mplew.writeInt(0);
        mplew.writeInt((attack.summonattack > 0) ? attack.summonattack : 0);
        mplew.writeInt(attack.count);
        mplew.write(0); // 362 +
        if ((attack.isBuckShot & 0x2) != 0) {
            if (chr.getBuffedValue(SecondaryStat.Buckshot) == null) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            } else {
                mplew.writeInt(chr.getBuffSource(SecondaryStat.Buckshot));
                mplew.writeInt(chr.getBuffedValue(SecondaryStat.Buckshot).intValue());
            }
        }
        if ((attack.isBuckShot & 0x8) != 0) {
            mplew.write(attack.skilllevel);
        }
        mplew.write(attack.display);
        mplew.write(attack.facingleft);
        mplew.write(attack.nMoveAction);
        if (GameConstants.isZero(chr.getJob()) && chr.getGender() == 1) {
            mplew.writeShort(0);
            mplew.writeShort(0);
        } else if (attack.position != null && !GameConstants.isZeroSkill(attack.skill) && attack.skill != 400031016) {
            mplew.writeShort(attack.position.x);
            mplew.writeShort(attack.position.y);
        } else {
            mplew.writeShort(0);
            mplew.writeShort(0);
        }
        mplew.write(attack.isLink);
        mplew.write(attack.bShowFixedDamage);
        mplew.write(attack.speed);
        mplew.write(chr.getStat().passive_mastery());
        mplew.writeInt(attack.item);
        for (AttackPair oned : attack.allDamage) {
            if (oned.attack != null) {
                mplew.writeInt(oned.objectId);
                if (oned.objectId == 0) {
                    continue;
                }
                mplew.write(7);
                mplew.write(0);
                mplew.write(0);
                mplew.writeShort(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                if (attack.skill == 80001835) {
                    mplew.write(oned.attack.size());
                    for (Pair<Long, Boolean> eachd : oned.attack) {
                        mplew.writeLong(((Long) eachd.left).longValue());
                    }
                } else {
                    for (Pair<Long, Boolean> eachd : oned.attack) {
                        if (((Boolean) eachd.right).booleanValue() || chr.getStat().getCritical_rate() >= 100 || chr.getSkillCustomValue(3310005) != null || Randomizer.isSuccess(chr.getStat().getCritical_rate())) {
                            mplew.writeLong(((Long) eachd.left).longValue() | 0x8000000000000001L);
                            continue;
                        }
                        mplew.writeLong(((Long) eachd.left).longValue());
                    }
                }
                if (sub_6F2500(attack.skill) > 0) {
                    mplew.writeInt(0);
                }
                if (attack.skill == 37111005) {
                    mplew.write(((chr.getPosition()).x < attack.position.x) ? 1 : 0);
                    continue;
                }
                if (attack.skill == 164001002) {
                    mplew.writeInt(0);
                }
            }
        }
        if (attack.skill == 2321001 || attack.skill == 2221052 || attack.skill == 11121052 || attack.skill == 12121054) {
            mplew.writeInt(attack.charge);
        }
        if (GameConstants.is_super_nova_skill(attack.skill) || GameConstants.is_screen_attack(attack.skill) || attack.skill == 101000202 || attack.skill == 101000102
                || GameConstants.is_thunder_rune(attack.skill) || attack.skill == 400041019 || attack.skill == 400031016 || attack.skill == 400041024
                || GameConstants.sub_84ABA0(attack.skill) || attack.skill == 400021075 || attack.skill == 400001055 || attack.skill == 400001056) {
            mplew.writeInt(attack.position.x);
            mplew.writeInt(attack.position.y);
        }
        if (attack.skill == 80002452) {
            mplew.writeInt(attack.position.x);
            mplew.writeInt(attack.position.y);
        }
        if (GameConstants.sub_8327B0(attack.skill) && attack.skill != 13111020) {
            mplew.writePos(attack.plusPosition2);
        }
        if (attack.skill == 63111004 || attack.skill == 63111104 || attack.skill == 80003017) {
            mplew.writePos(attack.position);
        } else if (attack.skill == 63111005 || attack.skill == 63111105 || attack.skill == 63111106 || attack.skill == 400051075) {
            if (attack.skill == 400051075) {
                mplew.write(attack.rlType);
            }
            mplew.writePosInt(attack.plusPosition2);
        } else if (attack.skill == 400031059) {
            mplew.writePosInt(attack.plusPosition2);
        }
        if (attack.skill == 51121009) {
            mplew.write(attack.bShowFixedDamage);
        }
        if (attack.skill == 21120019 || attack.skill == 37121052 || GameConstants.is_shadow_assult(attack.skill) || attack.skill == 11121014 || attack.skill == 5101004) {
            mplew.write(attack.plusPos);
            mplew.writeInt(attack.plusPosition.x);
            mplew.writeInt(attack.plusPosition.y);
        }
        if (GameConstants.sub_7FB860(attack.skill)) {
            mplew.writePos(attack.position);
            if (GameConstants.is_pathfinder_blast_skill(attack.skill)) {
                mplew.writeInt(attack.skilllevel);
                mplew.write(0);
            }
        }
        if (GameConstants.sub_6F5530(attack.skill)) {
            mplew.writeInt(attack.skilllevel);
            mplew.write(0);
        }
        if (attack.skill == 155101104 || attack.skill == 155101204 || attack.skill == 400051042 || attack.skill == 151101003 || attack.skill == 151101004) {
            mplew.write(attack.across);
            if (attack.across) {
                mplew.writeInt(attack.acrossPosition.width);
                mplew.writeInt(attack.acrossPosition.height);
            }
        }
        if (attack.skill == 23121011 || attack.skill == 80001913) {
            mplew.write(0);
        }
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static int sub_6F2500(int a1) {
        if (a1 > 142111002) {
            if (a1 < 142120000 || (a1 > 142120002 && a1 != 142120014)) {
                return 0;
            }
        } else if (a1 != 142111002 && a1 != 142100010 && a1 != 142110003 && a1 != 142110015) {
            return 0;
        }
        return 1;
    }

    public static byte[] skillEffect(MapleCharacter from, int skillid, int level, short display, byte unk) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SKILL_EFFECT.getValue());
        mplew.writeInt(from.getId());
        mplew.writeInt(skillid);
        mplew.write(level);
        mplew.writeShort(display);
        mplew.write(unk);
        if (skillid == 13111020) {
            mplew.writePos(from.getTruePosition());
        }
        return mplew.getPacket();
    }

    public static byte[] skillCancel(MapleCharacter from, int skillid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CANCEL_SKILL_EFFECT.getValue());
        mplew.writeInt(from.getId());
        mplew.writeInt(skillid);
        mplew.writeInt(0); //new 362
        return mplew.getPacket();
    }

    public static byte[] damagePlayer(int skill, int monsteridfrom, int cid, int damage) {
        return damagePlayer(cid, skill, damage, monsteridfrom, (byte) 0, 0, 0, false, 0, (byte) 0, null, (byte) 0, 0, 0);
    }

    public static byte[] damagePlayer(int cid, int type, int damage, int monsteridfrom, byte direction, int skillid, int pDMG, boolean pPhysical, int pID, byte pType, Point pPos, byte offset, int offset_d, int fake) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DAMAGE_PLAYER.getValue());
        mplew.writeInt(cid);
        mplew.write(type);
        mplew.writeInt(damage);
        mplew.write(0);
        mplew.write(false);
        mplew.write(0);
        if (type == -8) {
            mplew.writeInt(skillid);
            mplew.writeInt(pDMG);
            mplew.writeInt(0);
        } else if (type >= -1) {
            mplew.writeInt(monsteridfrom);
            mplew.write(direction);
            mplew.writeInt(0);
            mplew.writeInt(skillid);
            mplew.writeInt(pDMG);
            mplew.write(0);
            if (pDMG > 0) {
                mplew.write(pPhysical ? 1 : 0);
                mplew.writeInt(pID);
                mplew.write(pType);
                mplew.writePos(pPos);
            }
            mplew.write(offset);
            if ((offset & 0x1) != 0) {
                mplew.writeInt(offset_d);
            }
        }
        mplew.writeInt(damage);
        if (fake > 0) {
            mplew.writeInt(fake);
        }
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] facialExpression(MapleCharacter from, int expression) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FACIAL_EXPRESSION.getValue());
        mplew.writeInt(from.getId());
        mplew.writeInt(expression);
        mplew.writeInt(-1);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] ChangeFaceMotion(int type, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHANGE_FACE_MOTION.getValue());
        mplew.writeInt(type);
        mplew.writeInt(time);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] itemEffect(int characterid, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_EFFECT.getValue());
        mplew.writeInt(characterid);
        mplew.writeInt(itemid);
        return mplew.getPacket();
    }

    public static byte[] showTitle(int characterid, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_TITLE.getValue());
        mplew.writeInt(characterid);
        mplew.writeInt(itemid);
        mplew.write(false);
        return mplew.getPacket();
    }

    public static void specialChairPacket(MaplePacketLittleEndianWriter mplew, MapleSpecialChair chair) {
        mplew.writeInt(chair.getItemId());
        mplew.writeInt(chair.getPlayers().size());
        mplew.writeRect(chair.getRect());
        mplew.writeInt((chair.getPoint()).x);
        mplew.writeInt((chair.getPoint()).y);
        mplew.writeInt(chair.getPlayers().size());
        for (int i = 0; i < chair.getPlayers().size(); i++) {
            boolean isCharEnable = (((MapleSpecialChair.MapleSpecialChairPlayer) chair.getPlayers().get(i)).getPlayer() != null);
            mplew.writeInt(isCharEnable ? ((MapleSpecialChair.MapleSpecialChairPlayer) chair.getPlayers().get(i)).getPlayer().getId() : 0);
            mplew.write(isCharEnable);
            mplew.writeInt(((MapleSpecialChair.MapleSpecialChairPlayer) chair.getPlayers().get(i)).getEmotion());
        }
    }

    public static byte[] specialChair(MapleCharacter chr, boolean isCreate, boolean isShow, boolean isUpdate, MapleSpecialChair myChair) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPECIAL_CHAIR.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(isCreate);
        mplew.write(isShow);
        if (isShow) {
            mplew.writeInt(chr.getMap().getAllSpecialChairs().size());
            for (MapleSpecialChair chair : chr.getMap().getAllSpecialChairs()) {
                mplew.writeInt(chair.getObjectId());
                mplew.write(isCreate);
                if (isCreate) {
                    specialChairPacket(mplew, chair);
                }
            }
        } else {
            mplew.writeInt(myChair.getObjectId());
            mplew.write(isUpdate);
            if (isUpdate) {
                specialChairPacket(mplew, myChair);
            }
        }
        return mplew.getPacket();
    }

    public static byte[] showChair(MapleCharacter chr, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_CHAIR.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(itemid);
        mplew.write((itemid != 0));
        if (itemid != 0) {
            PacketHelper.chairPacket(mplew, chr, itemid);
        }
        if (itemid == 3018599 || itemid == 3015798 || itemid == 3018352 || itemid == 3018464 || itemid == 3015520) {
            mplew.writeShort(0);
        }
        return mplew.getPacket();
    }

    public static byte[] updateCharLook(MapleCharacter chr, boolean DressUp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_LOOK.getValue());
        mplew.writeInt(chr.getId());
        byte flag = 1;
        if (GameConstants.isZero(chr.getJob())) {
            flag += 8;
        }
        mplew.write(flag);
        AvatarLook.encodeAvatarLook(mplew, chr, false, DressUp);
        if (GameConstants.isZero(chr.getJob())) {
            AvatarLook.encodeAvatarLook(mplew, chr, false, !DressUp);
        }
        Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> rings = chr.getRings(false);
        addRingInfo(mplew, rings.getLeft());
        addRingInfo(mplew, rings.getMid());
        addMRingInfo(mplew, rings.getRight(), chr);
        mplew.writeInt(0);
        mplew.writeInt(0); // -> charid to follow (4)
        mplew.writeInt(0); // 262 ++
        mplew.writeInt(0); // 262 ++
        return mplew.getPacket();
    }

    public static byte[] ZeroTagUpdateCharLook(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_LOOK.getValue());
        mplew.writeInt(chr.getId());
        byte flag = 1;
        if (GameConstants.isZero(chr.getJob())) {
            flag = (byte) (flag + 8);
        }
        mplew.write(flag);
        AvatarLook.encodeAvatarLook(mplew, chr, false, (chr.getGender() == 1));
        if (GameConstants.isZero(chr.getJob())) {
            AvatarLook.encodeAvatarLook(mplew, chr, false, (chr.getGender() != 1));
        }
        Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> rings = chr.getRings(false);
        addRingInfo(mplew, rings.getLeft());
        addRingInfo(mplew, rings.getMid());
        addMRingInfo(mplew, rings.getRight(), chr);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] updatePartyMemberHP(int cid, int curhp, int maxhp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_PARTYMEMBER_HP.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(curhp);
        mplew.writeInt(maxhp);
        return mplew.getPacket();
    }

    public static byte[] loadGuildName(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LOAD_GUILD_NAME.getValue());
        mplew.writeInt(chr.getId());
        if (chr.getGuildId() <= 0) {
            mplew.writeShort(0);
        } else {
            MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
            if (gs != null) {
                mplew.writeMapleAsciiString(gs.getName());
            } else {
                mplew.writeShort(0);
            }
        }
        return mplew.getPacket();
    }

    public static byte[] loadGuildIcon(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        /* 4206 */ mplew.writeShort(SendPacketOpcode.LOAD_GUILD_ICON.getValue());
        /* 4207 */ mplew.writeInt(chr.getId());
        /* 4208 */ if (chr.getGuildId() <= 0) {
            /* 4209 */ mplew.writeZeroBytes(12);
            /* 4210 */ mplew.writeInt(0);
        } else {
            /* 4212 */ MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
            /* 4213 */ if (gs != null) {
                /* 4214 */ mplew.writeInt(gs.getId());
                /* 4215 */ mplew.writeMapleAsciiString(gs.getName());
                /* 4216 */ mplew.writeShort(gs.getLogoBG());
                /* 4217 */ mplew.write(gs.getLogoBGColor());
                /* 4218 */ mplew.writeShort(gs.getLogo());
                /* 4219 */ mplew.write(gs.getLogoColor());
                /* 4220 */ mplew.writeInt((gs.getCustomEmblem() != null && (gs.getCustomEmblem()).length > 0) ? 1 : 0);
                /* 4221 */ if (gs.getCustomEmblem() != null && (gs.getCustomEmblem()).length > 0) {
                    /* 4222 */ mplew.writeInt(gs.getId());
                    /* 4223 */ mplew.writeInt((gs.getCustomEmblem()).length);
                    /* 4224 */ mplew.write(gs.getCustomEmblem());
                }
            } else {
                /* 4227 */ mplew.writeZeroBytes(12);
                /* 4228 */ mplew.writeInt(0);
            }
        }
        return mplew.getPacket();
    }

    public static byte[] showHarvesting(int cid, int tool) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_HARVEST.getValue());
        mplew.writeInt(cid);
        if (tool > 0) {
            mplew.writeInt(1);
            mplew.writeInt(tool);
        } else {
            mplew.writeInt(0);
        }
        return mplew.getPacket();
    }

    public static byte[] cancelChair(int id, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CANCEL_CHAIR.getValue());
        mplew.writeInt(chr.getId());
        mplew.write((id != -1));
        if (id != -1) {
            mplew.writeShort(id);
        }
        return mplew.getPacket();
    }

    public static byte[] instantMapWarp(MapleCharacter chr, byte portal) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CURRENT_MAP_WARP.getValue());
        mplew.write(0);
        mplew.write(portal);
        mplew.writeInt((chr.getMapId() == 993192600) ? 12 : chr.getId());
        if (portal != 0) {
            mplew.writeShort((chr.getMap().getPortal(portal).getPosition()).x);
            mplew.writeShort((chr.getMap().getPortal(portal).getPosition()).y - 20);
        }
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] sendHint(String hint, int width, int height) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PLAYER_HINT.getValue());
        mplew.writeMapleAsciiString(hint);
        mplew.writeShort((width < 1) ? Math.max(hint.length() * 10, 40) : width);
        mplew.writeShort(Math.max(height, 5));
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] aranCombo(int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.AranCombo.getValue());
        mplew.writeInt(value);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] rechargeCombo(int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.AranCombo_RECHARGE.getValue());
        mplew.writeInt(value);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] getGameMessage(int type, String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GAME_MESSAGE.getValue());
        mplew.writeShort(type);
        mplew.writeMapleAsciiString(msg);
        return mplew.getPacket();
    }

    public static byte[] createUltimate(int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CREATE_ULTIMATE.getValue());
        mplew.writeInt(amount);
        return mplew.getPacket();
    }

    public static byte[] harvestMessage(int oid, int msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HARVEST_MESSAGE.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(msg);
        return mplew.getPacket();
    }

    public static byte[] openBag(int index, int itemId, boolean firstTime) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.OPEN_BAG.getValue());
        mplew.writeInt(index);
        mplew.writeInt(itemId);
        mplew.writeShort(1);
        return mplew.getPacket();
    }

    public static byte[] fireBlink(int cid, Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CURRENT_MAP_WARP.getValue());
        mplew.write(0);
        mplew.write(2);
        mplew.writeInt(cid);
        mplew.writePos(pos);
        return mplew.getPacket();
    }

    public static byte[] fireBlinkMulti(MapleCharacter chr, boolean warp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FIRE_BLINK.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(0);
        mplew.write(warp ? 1 : 0);
        mplew.writeShort(5);
        mplew.writeShort((chr.getPosition()).x);
        mplew.writeShort((chr.getPosition()).y);
        mplew.writeShort((chr.getPosition()).x);
        mplew.writeShort((chr.getPosition()).y + 15);
        return mplew.getPacket();
    }

    public static byte[] skillCooldown(int sid, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.COOLDOWN.getValue());
        mplew.writeInt(1);
        mplew.writeInt((sid == 25121133 || sid == 400041051 || sid == 152110004 || sid == 400011135 || sid == 400001063) ? sid : GameConstants.getLinkedSkill(sid));
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] skillCooldown(Map<Integer, Integer> datas) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.COOLDOWN.getValue());
        mplew.writeInt(datas.size());
        for (Map.Entry<Integer, Integer> data : datas.entrySet()) {
            mplew.writeInt(GameConstants.getLinkedSkill(((Integer) data.getKey()).intValue()));
            mplew.writeInt(((Integer) data.getValue()).intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] dropItemFromMapObject(MapleMap map, MapleMapItem drop, Point dropfrom, Point dropto, byte mod, boolean pickPocket) {
        return dropItemFromMapObject(map, drop, dropfrom, dropto, mod, pickPocket, 0, (byte) 0);
    }

    public static byte[] dropItemFromMapObject(MapleMap map, MapleMapItem drop, Point dropfrom, Point dropto, byte mod, boolean pickPocket, int delay, byte bloody) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DROP_ITEM_FROM_MAPOBJECT.getValue());
        boolean spitem = false;
        switch (drop.getItemId()) {
            case 2632342:
            case 2632343:
            case 2632344:
                drop.setFlyingDrop(true);
                drop.setTouchDrop(true);
                drop.setFlyingSpeed(-235080451);
                spitem = true;
                break;
            case 2432391:
            case 2432392:
            case 2432393:
            case 2432394:
            case 2432395:
            case 2432396:
            case 2432397:
            case 2432398:
                if (map.getElitebossrewardtype() == 2 || map.getElitebossrewardtype() == 3) {
                    drop.setFlyingDrop(true);
                    drop.setTouchDrop(true);
                    drop.setFlyingSpeed(150);
                }
                break;
            case 2022570:
            case 2022571:
            case 2022572:
            case 2022573:
            case 2022574:
            case 4001847:
            case 4001849:
                spitem = true;
                break;
        }
        mplew.write(spitem ? 1 : 0);
        mplew.write(mod);
        mplew.writeInt(drop.getObjectId());
        mplew.write((drop.getMeso() > 0) ? 1 : 0);
        mplew.writeInt(drop.isFlyingDrop() ? 1 : 0);
        mplew.writeInt(drop.getFlyingSpeed());
        mplew.writeInt(drop.getFlyingAngle());
        mplew.writeInt(drop.getItemId());
        mplew.writeInt(drop.getOwner());
        mplew.write(drop.getDropType());
        mplew.writePos(dropto);
        mplew.writeInt(pickPocket ? 4048947 : 3072528);
        mplew.writeInt(0);
        mplew.writeLong(0L);
        mplew.writeInt(0);
        mplew.writeLong(0L);
        mplew.writeInt(0);
        mplew.writeLong(0L);
        mplew.write((drop.getItemId() / 1000000 == 1));
        mplew.write(bloody);
        mplew.write(0);
        mplew.write(0);
        if (mod != 2) {
            mplew.writePos(dropfrom);
            mplew.writeInt(delay);
        }
        mplew.write(((drop.getDropType() == 3 && drop.getItemId() / 1000000 == 1) || drop.getItemId() == 2633609) ? 1 : 0);
        if (drop.getMeso() == 0) {
            PacketHelper.addExpirationTime(mplew, drop.getItem().getExpiration());
        }
        mplew.write(drop.isPlayerDrop() ? 0 : 1);
        mplew.write(0);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(drop.isTouchDrop() ? 1 : 0);
        if (drop.getItemId() / 1000000 == 1 && drop.getMeso() == 0 && drop.getEquip() != null) {
            if (drop.getEquip().getState() <= 4) {
                mplew.write(drop.getEquip().getState());
            } else if (drop.getEquip().getState() <= 20) {
                mplew.write(drop.getEquip().getState() - 16);
            } else {
                mplew.write(0);
            }
        } else {
            mplew.write(0);
        }
        mplew.write((drop.getItemId() == 2434851));
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] explodeDrop(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue());
        mplew.write(4);
        mplew.writeInt(oid);
        mplew.writeShort(0);
        mplew.writeShort(655);
        return mplew.getPacket();
    }

    public static byte[] removeItemFromMap(int oid, int animation, int cid) {
        return removeItemFromMap(oid, animation, cid, 0);
    }

    public static byte[] removeItemFromMap(int oid, int animation, int cid, int index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue());
        mplew.write(animation);
        mplew.writeInt(oid);
        switch (animation) {
            case 2:
            case 3:
            case 5:
                mplew.writeInt(cid);
                break;
            case 4:
                mplew.writeShort(0);
                break;
        }
        if (animation == 5 || animation == 7) {
            mplew.writeInt(index);
        }
        return mplew.getPacket();
    }

    public static byte[] spawnMist(MapleMist mist) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_MIST.getValue());
        mplew.writeInt(mist.getObjectId());
        mplew.write(mist.isMobMist() ? 1 : mist.isPoisonMist());
        mplew.writeInt(mist.getOwnerId());
        int skillId = (mist.getSourceSkill() != null) ? mist.getSourceSkill().getId() : ((mist.getMobSkill() != null) ? mist.getMobSkill().getSkillId() : 0);
        if (mist.getMobSkill() == null) {
            switch (skillId) {
                case 21121057:
                    skillId = 21121068;
                    break;
                case 400011058:
                    skillId = 400011060;
                    break;
            }
            mplew.writeInt(skillId);
        } else {
            mplew.writeInt(mist.getMobSkill().getSkillId());
        }
        if (mist.getMobSkill() != null) {
            mplew.writeShort(mist.getMobSkill().getSkillLevel());
        } else {
            mplew.writeShort(mist.getSkillLevel());
        }
        mplew.writeShort(mist.getSkillDelay());
        if (skillId == 186 && (mist.getSkillLevel() == 3 || mist.getSkillLevel() == 5 || mist.getSkillLevel() == 6)) {
            mplew.writeNRect(mist.getBox());
        } else {
            mplew.writeRect(mist.getBox());
            if (skillId == 162111000) {
                mplew.writeInt((mist.getPosition()).x + (int) mist.getSource().getLt3().getX());
                mplew.writeInt((mist.getPosition()).y + (int) mist.getSource().getLt3().getY());
                mplew.writeInt((mist.getPosition()).x + (int) mist.getSource().getRb3().getX());
                mplew.writeInt((mist.getPosition()).y + (int) mist.getSource().getRb3().getY());
            }
        }
        if (mist.getMobSkill() != null) {
            mplew.writeInt((mist.getMobSkill().getSkillId() == 186 || mist.getMobSkill().getSkillId() == 227) ? 8 : mist.isPoisonMist());
        } else {
            mplew.writeInt(mist.isPoisonMist());
        }
        if (mist.getTruePosition() != null) {
            mplew.writePos(mist.getTruePosition());
        } else if (mist.getPosition() != null) {
            mplew.writePos(mist.getPosition());
        } else if (mist.getOwner() != null) {
            mplew.writePos(mist.getOwner().getTruePosition());
        } else if (mist.getMob() != null) {
            mplew.writePos(mist.getMob().getTruePosition());
        } else if (mist.getMobSkill().getSkillId() == 183 && mist.getSkillLevel() == 13) {
            mplew.writePos(mist.getTruePosition());
        } else {
            mplew.writeShort((mist.getBox()).x);
            mplew.writeShort((mist.getBox()).y);
        }
        if (mist.getMobSkill() != null) {
            mplew.writeShort((skillId == 186 && (mist.getSkillLevel() == 3 || mist.getSkillLevel() == 5 || mist.getSkillLevel() == 6)) ? mist.getCustomx() : (mist.getPosition()).x);
            mplew.writeShort(mist.getMobSkill().getForce());
        } else {
            mplew.writeInt(0);
        }
        mplew.writeInt((mist.getDamup() > 0) ? mist.getDamup() : ((skillId == 131 && mist.getSkillLevel() == 28) ? 5 : 0));
        mplew.write((skillId == 131 && mist.getSkillLevel() == 28));
        mplew.writeInt((skillId == 400011060) ? 200 : ((mist.getMob() != null && mist.getMob().getId() / 10000 == 895) ? 210 : ((skillId == 217 && mist.getSkillLevel() == 21) ? 180 : ((skillId == 186 && mist.getSkillLevel() == 3) ? 190 : 0))));
        if (mist.getSource() != null
                && sub_783400(mist.getSourceSkill().getId())) {
            mplew.write((mist.getRltype() == 0) ? 1 : 0);
        }
        mplew.writeInt(mist.getDuration());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write((mist.getSource() != null) ? ((mist.getSourceSkill().getId() == 151121041)) : false);
        mplew.write(false);
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static boolean sub_783400(int a1) {
        boolean v1;
        if (a1 == 135001012) {
            return true;
        }
        if (a1 > 35121052) {
            if (a1 == 400020046) {
                return true;
            }
            v1 = (a1 == 400020051);
        } else {
            if (a1 == 35121052 || a1 == 33111013 || a1 - 33111013 == 9999) {
                return true;
            }
            v1 = (a1 - 33111013 == 10003);
        }
        if (!v1) {
            boolean v2;
            if (a1 > 131001207) {
                if (a1 == 152121041 || a1 == 400001017) {
                    return true;
                }
                v2 = (a1 == 400041041);
            } else {
                if (a1 == 131001207 || a1 == 4121015 || a1 == 51120057) {
                    return true;
                }
                v2 = (a1 == 131001107);
            }
            if (!v2) {
                return false;
            }
        }
        return true;
    }

    public static byte[] removeMist(MapleMist mist) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_MIST.getValue());
        mplew.writeInt(mist.getObjectId());
        mplew.writeInt(0); // 351 new
        if (mist.getSourceSkill() != null
                && mist.getSourceSkill().getId() == 2111003) {
            mplew.write(0);
        }
        return mplew.getPacket();
    }

    public static byte[] spawnDoor(int oid, Point pos, boolean animation) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_DOOR.getValue());
        mplew.write(animation ? 0 : 1);
        mplew.writeInt(oid);
        mplew.writePos(pos);
        return mplew.getPacket();
    }

    public static byte[] removeDoor(int oid, boolean animation) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_DOOR.getValue());
        mplew.write(animation ? 0 : 1);
        mplew.writeInt(oid);
        return mplew.getPacket();
    }

    public static byte[] spawnMechDoor(MechDoor md, boolean animated) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MECH_DOOR_SPAWN.getValue());
        mplew.write(animated ? 0 : 1);
        mplew.writeInt(md.getOwnerId());
        mplew.writePos(md.getTruePosition());
        mplew.write(md.getId());
        mplew.write(0); //new 361
        return mplew.getPacket();
    }

    public static byte[] removeMechDoor(MechDoor md, boolean animated) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MECH_DOOR_REMOVE.getValue());
        mplew.write(animated ? 0 : 1);
        mplew.writeInt(md.getOwnerId());
        mplew.write(md.getId());
        return mplew.getPacket();
    }

    public static byte[] triggerReactor(MapleReactor reactor, int stance, int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REACTOR_HIT.getValue());
        mplew.writeInt(reactor.getObjectId());
        mplew.write(reactor.getState());
        mplew.writePos(reactor.getTruePosition());
        mplew.writeShort(stance);
        mplew.write(0);
        mplew.write(7);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(cid);
        return mplew.getPacket();
    }
    
        public static byte[] triggerReactor1(MapleReactor reactor, int stance) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REACTOR_HIT.getValue());
        mplew.writeInt(reactor.getObjectId());
        mplew.write(reactor.getState());
        mplew.writePos(reactor.getTruePosition());
        mplew.writeShort(0); //274 ++
//        mplew.write(-1); //274 ++
        mplew.write(0); //274 ++
        mplew.writeInt(stance);
        mplew.writeInt(0); // 324 ++
        return mplew.getPacket();
    }

    public static byte[] spawnReactor(MapleReactor reactor) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REACTOR_SPAWN.getValue());
        mplew.writeInt(reactor.getObjectId());
        mplew.writeInt(reactor.getReactorId());
        mplew.write(reactor.getState());
        mplew.writePos(reactor.getTruePosition());
        mplew.write(reactor.getFacingDirection());
        mplew.writeMapleAsciiString(reactor.getName());
        return mplew.getPacket();
    }

    public static byte[] destroyReactor(MapleReactor reactor) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REACTOR_DESTROY.getValue());
        mplew.writeInt(reactor.getObjectId());
        mplew.write(false);
        mplew.write(reactor.getState());
        mplew.writePos(reactor.getPosition());
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] makeExtractor(int cid, String cname, Point pos, int timeLeft, int itemId, int fee) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_EXTRACTOR.getValue());
        mplew.writeInt(cid);
        mplew.writeMapleAsciiString(cname);
        mplew.writeInt(pos.x);
        mplew.writeInt(pos.y);
        mplew.writeShort(timeLeft);
        mplew.writeInt(itemId);
        mplew.writeInt(fee);
        return mplew.getPacket();
    }

    public static byte[] removeExtractor(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_EXTRACTOR.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(1);
        return mplew.getPacket();
    }

    public static byte[] showChaosZakumShrine(boolean spawned, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHAOS_ZAKUM_SHRINE.getValue());
        mplew.write(spawned ? 1 : 0);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] showChaosHorntailShrine(boolean spawned, int time) {
        return showHorntailShrine(spawned, time);
    }

    public static byte[] showHorntailShrine(boolean spawned, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HORNTAIL_SHRINE.getValue());
        mplew.write(spawned ? 1 : 0);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] messengerInvite(String from, int messengerid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(3);
        mplew.writeMapleAsciiString(from);
        mplew.writeInt(0);
        mplew.write(1);
        mplew.writeInt(messengerid);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] addMessengerPlayer(String from, MapleCharacter chr, int position, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(0);
        mplew.write(position);
        mplew.writeInt(0);
        AvatarLook.encodeAvatarLook(mplew, chr, true, (GameConstants.isZero(chr.getJob()) && chr.getGender() == 1));
        mplew.writeMapleAsciiString(from);
        mplew.write(channel);
        mplew.write(position);
        mplew.writeInt(chr.getJob());
        return mplew.getPacket();
    }

    public static byte[] removeMessengerPlayer(int position) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(2);
        mplew.write(position);
        return mplew.getPacket();
    }

    public static byte[] updateMessengerPlayer(String from, MapleCharacter chr, int position, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(8);
        mplew.write(position);
        AvatarLook.encodeAvatarLook(mplew, chr, true, (GameConstants.isZero(chr.getJob()) && chr.getGender() == 1));
        mplew.writeMapleAsciiString(from);
        return mplew.getPacket();
    }

    public static byte[] joinMessenger(int position) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(1);
        mplew.write(position);
        return mplew.getPacket();
    }

    public static byte[] messengerChat(String charname, String text) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(6);
        mplew.writeMapleAsciiString(charname);
        mplew.writeMapleAsciiString(text);
        PacketHelper.ChatPacket(mplew, charname, text);
        return mplew.getPacket();
    }

    public static byte[] ChrlistMap(List<MapleCharacter> chrs) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER_SEARCH.getValue());
        mplew.write(chrs.size());
        for (MapleCharacter mapchr : chrs) {
            mplew.writeInt(mapchr.getId());
            mplew.writeMapleAsciiString(mapchr.getName());
        }
        return mplew.getPacket();
    }

    public static byte[] messengerChatNow(MapleCharacter chr, int id, String charname, String text) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(7);
        mplew.writeMapleAsciiString(charname);
        mplew.writeMapleAsciiString(text);
        mplew.writeMapleAsciiString(charname);
        mplew.writeMapleAsciiString(text);
        mplew.writeInt(chr.getId());
        mplew.writeInt(id);
        mplew.write(-1);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] messengerWhisperChat(String charname, String text) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(7);
        mplew.writeMapleAsciiString(charname);
        mplew.writeMapleAsciiString(text);
        PacketHelper.ChatPacket(mplew, charname, text);
        return mplew.getPacket();
    }

    public static byte[] messengerNote(String text, int mode, int mode2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(mode);
        mplew.writeMapleAsciiString(text);
        mplew.write(mode2);
        return mplew.getPacket();
    }

    public static byte[] messengerLike(short like, String charname, String othername) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.writeShort(like);
        mplew.writeMapleAsciiString(charname);
        mplew.writeMapleAsciiString(othername);
        return mplew.getPacket();
    }

    public static byte[] resultSkill(MapleCharacter chr, int update) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_SKILLS.getValue());
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.writeShort(chr.getMatrixs().size());
        for (VMatrix matrix : chr.getMatrixs()) {
            mplew.writeInt(matrix.getId());
            mplew.writeInt(matrix.getLevel());
            mplew.writeInt(matrix.getMaxLevel());
            mplew.writeLong(PacketHelper.getTime(-1L));
        }
        mplew.write(update);
        return mplew.getPacket();
    }

    public static byte[] messengerCharInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(11);
        mplew.writeMapleAsciiString(chr.getName());
        mplew.writeInt(chr.getLevel());
        mplew.writeShort(chr.getJob());
        mplew.writeShort(chr.getSubcategory());
        mplew.writeInt(chr.getFame());
        mplew.writeInt(0);
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
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] removeItemFromDuey(boolean remove, int Package) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DUEY.getValue());
        mplew.write(24);
        mplew.writeInt(Package);
        mplew.write(remove ? 3 : 4);
        return mplew.getPacket();
    }

    public static byte[] checkFailedDuey() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DUEY.getValue());
        mplew.write(9);
        mplew.write(-1);
        return mplew.getPacket();
    }

    public static byte[] sendDuey(byte operation, List<MapleDueyActions> packages, List<MapleDueyActions> expired) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DUEY.getValue());
        mplew.write(operation);
        if (packages == null) {
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            return mplew.getPacket();
        }
        switch (operation) {
            case 9:
                mplew.write(1);
                break;
            case 10:
                mplew.write(0);
                mplew.write(packages.size());
                for (MapleDueyActions dp : packages) {
                    mplew.writeInt(dp.getPackageId());
                    mplew.writeAsciiString(dp.getSender(), 13);
                    mplew.writeLong(dp.getMesos());
                    mplew.writeLong(PacketHelper.getTime(dp.getExpireTime()));
                    mplew.write(dp.isQuick() ? 1 : 0);
                    mplew.writeAsciiString(dp.getContent(), 100);
                    mplew.writeZeroBytes(101);
                    if (dp.getItem() != null) {
                        mplew.write(1);
                        PacketHelper.addItemInfo(mplew, dp.getItem());
                        continue;
                    }
                    mplew.write(0);
                }
                if (expired == null) {
                    mplew.write(0);
                    return mplew.getPacket();
                }
                mplew.write(expired.size());
                for (MapleDueyActions dp : expired) {
                    mplew.writeInt(dp.getPackageId());
                    mplew.writeAsciiString(dp.getSender(), 13);
                    mplew.writeLong(dp.getMesos());
                    if (dp.canReceive()) {
                        mplew.writeLong(PacketHelper.getTime(dp.getExpireTime()));
                    } else {
                        mplew.writeLong(0L);
                    }
                    mplew.write(dp.isQuick() ? 1 : 0);
                    mplew.writeAsciiString(dp.getContent(), 100);
                    mplew.writeZeroBytes(101);
                    if (dp.getItem() != null) {
                        mplew.write(1);
                        PacketHelper.addItemInfo(mplew, dp.getItem());
                        continue;
                    }
                    mplew.write(0);
                }
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] receiveParcel(String from, boolean quick) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DUEY.getValue());
        mplew.write(26);
        mplew.writeMapleAsciiString(from);
        mplew.write(quick ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] getKeymap(MapleKeyLayout layout) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.KEYMAP.getValue());
        if (layout != null) {
            mplew.write(0);
            layout.writeData(mplew);
            mplew.write(1); //1.2.362++
            mplew.write(1); //1.2.362++
        } else {
            mplew.write(1);
            mplew.write(1); //1.2.362++
            mplew.write(1); //1.2.362++
        }
        return mplew.getPacket();
    }

    public static byte[] petAutoHP(int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PET_AUTO_HP.getValue());
        mplew.writeInt(itemId);
        return mplew.getPacket();
    }

    public static byte[] petAutoMP(int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PET_AUTO_MP.getValue());
        mplew.writeInt(itemId);
        return mplew.getPacket();
    }

    public static void addRingInfo(MaplePacketLittleEndianWriter mplew, List<MapleRing> rings) {
        mplew.write(rings.size());
        for (MapleRing ring : rings) {
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
            mplew.writeInt(ring.getItemId());
        }
    }

    public static void addMRingInfo(MaplePacketLittleEndianWriter mplew, List<MapleRing> rings, MapleCharacter chr) {
        mplew.write(rings.size());
        for (MapleRing ring : rings) {
            mplew.writeInt(chr.getId());
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeInt(ring.getItemId());
        }
    }

    public static byte[] updateInnerPotential(byte ability, int skill, int level, int rank) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ENABLE_INNER_ABILITY.getValue());
        mplew.write(1);
        mplew.write(1);
        mplew.writeShort(ability);
        mplew.writeInt(skill);
        mplew.writeShort(level);
        mplew.writeShort(rank);
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] updateInnerAbility(InnerSkillValueHolder skill, int index, boolean last) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.ENABLE_INNER_ABILITY.getValue());
        packet.write(last ? 1 : 0);
        packet.write(1);
        packet.writeShort(index);
        packet.writeInt(skill.getSkillId());
        packet.writeShort(skill.getSkillLevel());
        packet.writeShort(skill.getRank());
        packet.write(last ? 1 : 0);
        return packet.getPacket();
    }

    public static byte[] HeadTitle(List<Integer> num) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HEAD_TITLE.getValue());
        for (Integer num_ : num) {
            mplew.writeMapleAsciiString("");
            mplew.write((num_.intValue() == 0) ? -1 : num_.intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] getInternetCafe(byte type, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.INTERNET_CAFE.getValue());
        mplew.write(type);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static class AuctionPacket {

        public static void auctionHistory(MaplePacketLittleEndianWriter mplew, AuctionHistory history) {
            //52 -> 60 Byte
            mplew.writeLong(history.getId()); // 307++
            mplew.writeInt(history.getAuctionId());
            mplew.writeInt(history.getAccountId());
            mplew.writeInt(history.getCharacterId());
            mplew.writeInt(history.getItemId());
            mplew.writeInt(history.getState());
            mplew.writeLong(history.getPrice()); // 아이템 템가격
            mplew.writeLong(PacketHelper.getTime(history.getBuyTime()));
            mplew.writeInt(history.getDeposit());
            mplew.writeInt(history.getDeposit());
            mplew.writeInt(history.getQuantity());
            mplew.writeInt(history.getWorldId());
        }

        public static void auctionItem(MaplePacketLittleEndianWriter mplew, AuctionItem item) {


            //122 -> 138 Byte
/* 5213 */ mplew.writeInt(item.getAuctionId());
            /* 5214 */ mplew.writeInt(item.getAuctionType());
            /* 5215 */ mplew.writeInt(item.getState());
            /* 5216 */ mplew.writeInt(item.getWorldId());
            /* 5217 */ mplew.writeLong(item.getPrice());
            /* 5218 */ mplew.writeLong(item.getSecondPrice());
            /* 5219 */ mplew.writeLong(item.getDirectPrice());
            /* 5220 */ mplew.writeLong(item.getPrice());
            /* 5221 */ if (GameConstants.getInventoryType(item.getItem().getItemId()) == MapleInventoryType.CASH && item.getItem().getQuantity() > 1) {
                /* 5222 */ mplew.writeLong(Double.doubleToRawLongBits((item.getPrice() / item.getItem().getQuantity())));
                /*      */            } else {
                /* 5224 */ mplew.writeLong(Double.doubleToRawLongBits(item.getPrice()));
                /*      */            }
            /* 5226 */ mplew.writeLong(PacketHelper.getTime(item.getEndDate()));
            /* 5227 */ mplew.writeLong(PacketHelper.getTime(item.getRegisterDate()));
            /* 5228 */ mplew.writeInt(item.getDeposit());
            /* 5229 */ mplew.writeInt(item.getDeposit());
            /* 5230 */ mplew.writeInt(item.getsStype());
            /* 5231 */ mplew.writeInt(item.getBidWorld());
            /* 5232 */ mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));

            mplew.write(item.getState() == 4); // 347++

            if (item.getState() == 4) {
                mplew.writeLong(0);
                mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
                mplew.writeShort(0);
            }
        }

        public static byte[] AuctionCompleteItems(List<AuctionItem> items) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(51);
            mplew.writeInt(0);
            mplew.writeInt(0);

            mplew.writeInt(items.size());

            for (AuctionItem item : items) {

                //60바이트
                auctionHistory(mplew, item.getHistory());

                //boolean = 판매 성공 실패, 구매 성공 시 메소, 혹은 아이템을 받았는가?
                mplew.write(item.getState() <= 4 || item.getState() >= 7);

                if (item.getState() <= 4 || item.getState() >= 7) {
                    //138바이트
                    auctionItem(mplew, item);
                    PacketHelper.addItemInfo(mplew, item.getItem());
                }
            }
            return mplew.getPacket();
        }

        public static byte[] AuctionCompleteItemUpdate(AuctionItem item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(71);
            mplew.writeInt(0);
            mplew.writeInt(0);

            mplew.writeLong(item.getItem().getInventoryId()); // 이상한 헥스
            mplew.write(true);

            //60바이트
            auctionHistory(mplew, item.getHistory());

            //boolean = 판매 성공 실패, 구매 성공 시 메소, 혹은 아이템을 받았는가?
            mplew.write(item.getState() <= 4 || item.getState() >= 7);

            if (item.getState() <= 4 || item.getState() >= 7) {
                //138바이트
                auctionItem(mplew, item);

                PacketHelper.addItemInfo(mplew, item.getItem());
            }
            return mplew.getPacket();
        }

        public static byte[] AuctionCompleteItemUpdate(AuctionItem item, Item item2) {
            //item : 구매한 아이템 데이터, item2 : 판매한 아이템 데이터

            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(71);
            mplew.writeInt(0);
            mplew.writeInt(0);

            mplew.writeLong(item.getItem().getInventoryId()); // 이상한 헥스
            mplew.write(true);

            auctionHistory(mplew, item.getHistory());
            mplew.write(true);

            if (true) {
                //138바이트
                auctionItem(mplew, item);

                PacketHelper.addItemInfo(mplew, item2);
            }
            return mplew.getPacket();
        }

        public static byte[] AuctionBuyItemUpdate(AuctionItem item, boolean remain) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(73);
            mplew.writeInt(0);
            mplew.writeInt(item.getAuctionId());
            mplew.write(remain);

            if (remain) {
                //138 Byte
                auctionItem(mplew, item);

                PacketHelper.addItemInfo(mplew, item.getItem());
            }
            return mplew.getPacket();
        }

        public static byte[] AuctionSellingMyItems(List<AuctionItem> items) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(50);
            mplew.writeInt(0);
            mplew.writeInt(0);

            mplew.writeInt(items.size());

            for (AuctionItem item : items) {

                //138Byte
                auctionItem(mplew, item);
                //End
                PacketHelper.addItemInfo(mplew, item.getItem());
            }
            return mplew.getPacket();
        }

        public static byte[] AuctionStopSell(AuctionItem item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(12);
            mplew.writeInt(0);
            mplew.writeInt(item.getAuctionId());

            return mplew.getPacket();
        }

        public static byte[] AuctionCompleteMesoResult() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(30);
            mplew.writeInt(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] AuctionCompleteItemResult() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(31);
            mplew.writeInt(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] AuctionWishlist(List<AuctionItem> wishItems) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(46);
            mplew.writeInt(0);
            mplew.writeInt(0);

            mplew.writeInt(wishItems.size()); // size, 일단 넘기겠삼

            for (AuctionItem item : wishItems) {
                //138Byte
                auctionItem(mplew, item);
                //End
                PacketHelper.addItemInfo(mplew, item.getItem());
            }

            return mplew.getPacket();
        }

        public static byte[] AuctionBuyEquipResult(int type, int dwAuctionID) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(20);
            mplew.writeInt(type);
            mplew.writeInt(dwAuctionID);

            return mplew.getPacket();
        }

        public static byte[] AuctionBuyItemResult(int type, int dwAuctionID) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(21);
            mplew.writeInt(type);
            mplew.writeInt(dwAuctionID);

            return mplew.getPacket();
        }

        public static byte[] AuctionWishlistUpdate(int dwAuctionID) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(72);
            mplew.writeInt(0);
            mplew.writeInt(dwAuctionID);

            mplew.write(false);
            return mplew.getPacket();
        }

        public static byte[] AuctionWishlistDeleteResult(int dwAuctionId) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(47);
            mplew.writeInt(0);
            mplew.writeInt(dwAuctionId);
            return mplew.getPacket();
        }

        public static byte[] AuctionAddWishlist(AuctionItem item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(72);
            mplew.writeInt(0);
            mplew.writeInt(item.getAuctionId());

            mplew.write(true);

            //138Byte
            auctionItem(mplew, item);
            //End
            PacketHelper.addItemInfo(mplew, item.getItem());

            return mplew.getPacket();
        }

        public static byte[] AuctionOn() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] AuctionOff() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(1);
            mplew.writeInt(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] AuctionWishlistResult(AuctionItem item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(45);
            mplew.writeInt(0);
            mplew.writeInt(item.getAuctionId());

            return mplew.getPacket();
        }

        public static byte[] AuctionMarketPrice(List<AuctionItem> items) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(41);
            mplew.writeInt(1000);
            mplew.writeInt(0);

            mplew.write(true); // ?
            mplew.writeShort(1);

            mplew.writeInt(items.size());

            List<AuctionItem> itemz = new CopyOnWriteArrayList<>();
            itemz.addAll(items);

            for (AuctionItem item : itemz) {
                item.setState(3);

                auctionItem(mplew, item);
                //End
                PacketHelper.addItemInfo(mplew, item.getItem());
            }
            return mplew.getPacket();
        }

        public static byte[] AuctionSearchItems(List<AuctionItem> items) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(40);
            mplew.writeInt(1000); // nMaxViewCountMaybe
            mplew.writeInt(0);

            mplew.write(true);

            mplew.writeShort(1);

            mplew.writeInt(items.size());

            for (AuctionItem item : items) {
                auctionItem(mplew, item);
                //End
                PacketHelper.addItemInfo(mplew, item.getItem());
            }

            return mplew.getPacket();
        }

        public static byte[] AuctionSellItemUpdate(AuctionItem item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(70);
            mplew.writeInt(0);
            mplew.writeInt(item.getAuctionId());

            mplew.write(true);

            //138Byte
            auctionItem(mplew, item);
            //End
            PacketHelper.addItemInfo(mplew, item.getItem());

            return mplew.getPacket();
        }

        public static byte[] AuctionSellItem(AuctionItem item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(10);
            mplew.writeInt(0);
            mplew.writeInt(item.getAuctionId());

            return mplew.getPacket();
        }

        public static byte[] AuctionReSellItem(AuctionItem item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.AUCTION.getValue());
            mplew.writeInt(11);
            mplew.writeInt(0);
            mplew.writeInt(item.getAuctionId());

            return mplew.getPacket();
        }
    }

    public static byte[] showSpineScreen(boolean isBinary, boolean isLoop, boolean isPostRender, String path, String animationName, int endDelay, boolean useKey, String key) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(30);
        mplew.write(isBinary);
        mplew.write(isLoop);
        mplew.write(isPostRender);
        mplew.writeInt(endDelay);
        mplew.writeMapleAsciiString(path);
        mplew.writeMapleAsciiString(animationName);
        mplew.writeMapleAsciiString("");
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(useKey);
        if (useKey) {
            mplew.writeMapleAsciiString(key);
        }
        return mplew.getPacket();
    }

    public static byte[] endscreen(String str) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(31);
        mplew.writeMapleAsciiString(str);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] showBlackOutScreen(int delay, String path, String animationName, int unk, int unk2, int unk3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(23);
        mplew.write(0);
        mplew.writeInt(delay);
        mplew.writeMapleAsciiString(path);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(unk);
        mplew.writeMapleAsciiString(animationName);
        mplew.writeInt(unk2);
        mplew.write(true);
        mplew.writeInt(unk3);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] removeBlackOutScreen(int delay, String path) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(23);
        mplew.write(2);
        mplew.writeInt(delay);
        mplew.writeMapleAsciiString(path);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] removeIntro(String animationName, int delay) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(31);
        mplew.writeMapleAsciiString(animationName);
        mplew.writeInt(delay);
        return mplew.getPacket();
    }

    public static byte[] spawnRune(MapleRune rune, boolean respawn) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(respawn ? SendPacketOpcode.RESPAWN_RUNE.getValue() : SendPacketOpcode.SPAWN_RUNE.getValue());
        mplew.writeInt(respawn ? 1 : 0);
        mplew.writeInt(0);
        mplew.writeInt(2);
        mplew.writeInt(rune.getRuneType());
        mplew.writeInt(rune.getPositionX());
        mplew.writeInt(rune.getPositionY());
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] removeRune(MapleRune rune, MapleCharacter chr, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_RUNE.getValue());
        mplew.writeInt(0);
        mplew.writeInt(chr.getId());
        mplew.writeInt(100);
        mplew.write(0);
        mplew.write(type);
        return mplew.getPacket();
    }

    public static byte[] RuneAction(int type, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.RUNE_ACTION.getValue());
        mplew.writeInt(type);
        if (type == 8 || type == 9) {
            mplew.write(0);
            mplew.writeShort(1);
            mplew.writeInt(545538968);
            mplew.writeInt(24);
            mplew.writeInt(-1586129021);
            mplew.writeInt(1397677378);
            for (int i = 0; i < 4; i++) {
                mplew.writeInt(Randomizer.rand(1397677436, 1397677439));
            }
        } else {
            mplew.writeInt(time);
        }
        return mplew.getPacket();
    }

    public static byte[] showRuneEffect(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.RUNE_EFFECT.getValue());
        mplew.writeInt(type);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] MultiTag(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZERO_MUlTITAG.getValue());
        mplew.writeInt(chr.getId());
        AvatarLook.encodeAvatarLook(mplew, chr, false, (chr.getGender() == 1));
        return mplew.getPacket();
    }

    public static byte[] MultiTagRemove(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZERO_MUlTITAG_REMOVE.getValue());
        mplew.writeInt(cid);
        return mplew.getPacket();
    }

    public static byte[] getWpGain(int gain) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(36);
        mplew.writeInt(gain);
        return mplew.getPacket();
    }

    public static byte[] updateWP(int wp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WP_UPDATE.getValue());
        mplew.writeInt(wp);
        return mplew.getPacket();
    }

    public static byte[] ZeroScroll(int scroll) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZERO_SCROLL.getValue());
        mplew.writeInt(scroll);
        if (scroll == 0) {
            mplew.writeInt(50000);
            mplew.writeInt(500);
        } else if (scroll == 1) {
            mplew.writeInt(100000);
            mplew.writeInt(600);
        }
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] ZeroScrollSend(int scroll) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZERO_SCROLL_SEND.getValue());
        mplew.writeShort(1);
        mplew.write(0);
        mplew.writeInt(scroll);
        return mplew.getPacket();
    }

    public static byte[] ZeroScrollStart() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZERO_SCROLL_START.getValue());
        return mplew.getPacket();
    }

    public static byte[] WeaponInfo(int type, int level, int action, int weapon, int itemid, int quantity) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZERO_WEAPON_INFO.getValue());
        mplew.write(1);
        mplew.write(action);
        mplew.writeInt(type);
        mplew.writeInt(level);
        mplew.writeInt(weapon + 10001);
        mplew.writeInt(weapon + 1);
        mplew.writeInt(type + 1);
        mplew.writeInt(itemid);
        mplew.writeInt(quantity);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] WeaponLevelUp() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZERO_WEAPON_UPGRADE.getValue());
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] Clothes(int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ZERO_TAG.getValue());
        mplew.write(0);
        mplew.write(1);
        mplew.writeInt(value);
        return mplew.getPacket();
    }

    public static byte[] ZeroTag(MapleCharacter chr, byte Gender, int nowhp, int maxhp) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.ZERO_TAG.getValue());
        int a = (chr.getSkillLevel(80000406) > 0) ? chr.getSkillLevel(80000406) : 0;
        packet.writeShort(199);
        packet.write(Gender);
        packet.writeInt(nowhp);
        packet.writeInt(chr.getSkillCustomValue0(101000201));
        packet.writeInt(maxhp);
        packet.writeInt(((Gender == 1) ? 100 : 100) + a * 10 + ((Gender == 1 && chr.getSkillLevel(101100203) > 0) ? 30 : 0));
        return packet.getPacket();
    }

    public static byte[] Reaction() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
        mplew.write(0);
        mplew.writeShort(0);
        return mplew.getPacket();
    }

    public static byte[] OnOffFlipTheCoin(boolean on) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FlipTheCoin.getValue());
        mplew.write(on ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] replaceStolenSkill(int base, int skill) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REPLACE_SKILLS.getValue());
        mplew.write(1);
        mplew.write((skill > 0) ? 1 : 0);
        mplew.writeInt(base);
        mplew.writeInt(skill);
        return mplew.getPacket();
    }

    public static byte[] addStolenSkill(int jobNum, int index, int skill, int level) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_STOLEN_SKILLS.getValue());
        mplew.write(1);
        mplew.write(0);
        mplew.writeInt(jobNum);
        mplew.writeInt(index);
        mplew.writeInt(skill);
        mplew.writeInt(level);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] removeStolenSkill(int jobNum, int index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_STOLEN_SKILLS.getValue());
        mplew.write(1);
        mplew.write(3);
        mplew.writeInt(jobNum);
        mplew.writeInt(index);
        return mplew.getPacket();
    }

    public static byte[] viewSkills(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TARGET_SKILL.getValue());
        List<Integer> skillz = new ArrayList<>();
        for (Skill sk : chr.getSkills().keySet()) {
            if (sk.canBeLearnedBy(chr) && !chr.getStolenSkills().contains(new Pair<>(Integer.valueOf(sk.getId()), Boolean.valueOf(true))) && !chr.getStolenSkills().contains(new Pair<>(Integer.valueOf(sk.getId()), Boolean.valueOf(false)))) {
                skillz.add(Integer.valueOf(sk.getId()));
            }
        }
        mplew.write(1);
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillz.isEmpty() ? 2 : 4);
        mplew.writeInt(chr.getJob());
        mplew.writeInt(skillz.size());
        for (Iterator<Integer> i$ = skillz.iterator(); i$.hasNext();) {
            int i = ((Integer) i$.next()).intValue();
            mplew.writeInt(i);
        }
        return mplew.getPacket();
    }

    public static byte[] updateCardStack(boolean unk, int total) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PHANTOM_CARD.getValue());
        mplew.write(unk);
        mplew.write(total);
        return mplew.getPacket();
    }

    public static byte[] showVoydPressure(int cid, List<Byte> arrays) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_VOYD_PRESSURE.getValue());
        mplew.writeInt(cid);
        mplew.write(arrays.size());
        for (Byte aray : arrays) {
            mplew.write(aray.byteValue());
        }
        return mplew.getPacket();
    }

    public static byte[] TheSidItem(int... args) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.THE_SEED_ITEM.getValue());
        mplew.writeShort(7);
        mplew.writeInt(2028272);
        mplew.write(1);
        mplew.writeInt(args.length);
        for (int i = 0; i < args.length; i++) {
            mplew.writeInt(args[i]);
        }
        return mplew.getPacket();
    }

    public static byte[] showForeignDamageSkin(MapleCharacter chr, int skinid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_DAMAGE_SKIN.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(skinid);
        mplew.writeMapleAsciiString("");
        mplew.writeMapleAsciiString("");
        return mplew.getPacket();
    }

    public static byte[] updateDress(int code, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_DRESS.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(code);
        return mplew.getPacket();
    }

    public static byte[] keepDress(boolean isDress) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.KEEP_DRESSUP.getValue());
        mplew.write(isDress);
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] lockSkill(int skillid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LOCK_SKILL.getValue());
        mplew.writeInt(skillid);
        return mplew.getPacket();
    }

    public static byte[] unlockSkill() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNLOCK_SKILL.getValue());
        return mplew.getPacket();
    }

    public static byte[] setPlayerDead() {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.SET_DEAD.getValue());
        packet.write(1);
        packet.writeInt(0);
        return packet.getPacket();
    }

    public static byte[] OpenDeadUI(MapleCharacter chr, int flag) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.OPEN_UI_DEAD.getValue());
        packet.writeInt((chr.getItemQuantity(5133000, false) > 0 || GameConstants.보스맵(chr.getMapId())) ? 3 : flag);
        packet.write(0);
        packet.writeInt((GameConstants.보스맵(chr.getMapId()) && (chr.getDeathCount() >= 0 || chr.liveCounts() >= 0)) ? 3 : 0);
        packet.writeInt((chr.getItemQuantity(5133000, false) > 0) ? 0 : -1);
        packet.write((GameConstants.보스맵(chr.getMapId()) && (chr.getDeathCount() >= 0 || chr.liveCounts() >= 0)) ? 1 : 0);
        packet.writeInt((GameConstants.보스맵(chr.getMapId()) && (chr.getDeathCount() >= 0 || chr.liveCounts() >= 0)) ? 30 : 0);
        packet.writeInt((GameConstants.보스맵(chr.getMapId()) && (chr.getDeathCount() >= 0 || chr.liveCounts() >= 0)) ? 5 : 0);
        packet.write((GameConstants.보스맵(chr.getMapId()) && chr.getDeathCount() <= 0 && chr.liveCounts() <= 0) ? 1 : 0);
        return packet.getPacket();
    }

    public static byte[] BlackMageDeathCountEffect() {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.BLACKMAGE_DEATHCOUNT.getValue());
        packet.writeInt(0);
        return packet.getPacket();
    }

    public static byte[] Aggressive(List<Pair<String, Long>> a, MapleMap map) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.AGGRESSIVE.getValue());
        mplew.writeInt(a.size());
        for (Pair<String, Long> b : a) {
            mplew.writeMapleAsciiString(b.getLeft());
        }
        return mplew.getPacket();
    }

    public static byte[] getDeathCount(byte count) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.DEATH_COUNT.getValue());
        packet.writeInt(count);
        return packet.getPacket();
    }

    public static byte[] setDeathCount(MapleCharacter chr, int count) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SET_DEATH_COUNT.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(count);
        return mplew.getPacket();
    }

    public static byte[] showDeathCount(MapleCharacter chr, int count) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_DEATH_COUNT.getValue());
        if (count <= 0) {
            mplew.writeShort(-1);
        } else {
            mplew.writeShort(count);
            mplew.writeShort(1);
            mplew.writeInt(chr.getId());
            mplew.writeInt(count);
        }
        return mplew.getPacket();
    }

    public static byte[] getPracticeMode(boolean practice) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.PRACTICE_MODE.getValue());
        packet.write(practice);
        return packet.getPacket();
    }

    public static byte[] enterAuction(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ENTER_AUCTION.getValue());
        PacketHelper.addCharacterInfo(mplew, chr);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        return mplew.getPacket();
    }

    public static byte[] dailyGift(MapleCharacter chr, int type, int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DAILY_GIFT.getValue());
        mplew.write((type != 1) ? 2 : 0);
        if (type != 1) {
            mplew.writeInt(type);
            mplew.writeInt(itemId);
        } else {
            mplew.write(1);
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
            mplew.writeLong(PacketHelper.getTime(-1L));
            mplew.writeInt(28);
            mplew.writeInt(2);
            mplew.writeInt(16700);
            mplew.writeInt(300);
            mplew.writeInt(GameConstants.dailyItems.size());
            for (DailyGiftItemInfo item : GameConstants.dailyItems) {
                mplew.writeInt(item.getId());
                mplew.writeInt(item.getItemId());
                mplew.writeInt(item.getQuantity());
                mplew.write(1);
                mplew.writeInt((item.getSN() > 0) ? 0 : 10080);
                mplew.writeInt((item.getSN() > 0) ? 1 : 0);
                mplew.writeInt(item.getSN());
                mplew.writeShort(0);
            }
            mplew.writeInt(ServerConstants.ReqDailyLevel);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        return mplew.getPacket();
    }

    public static byte[] momentAreaOnOffAll(List<String> info) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.MOMENT_AREA_ON_OFF_ALL.getValue());
        packet.writeShort(0);
        packet.write((info.size() > 0) ? 1 : 0);
        if (info.size() > 0) {
            packet.writeInt(info.size());
            for (String list : info) {
                packet.writeMapleAsciiString(list);
            }
        }
        return packet.getPacket();
    }

    public static byte[] onUserTeleport(int x, int y) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.USER_TELEPORT.getValue());
        packet.writeInt(x);
        packet.writeInt(y);
        return packet.getPacket();
    }

    public static byte[] Respawn(int cid, int hp) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.RESPAWN.getValue());
        packet.writeInt(cid);
        packet.writeInt(hp);
        return packet.getPacket();
    }

    public static byte[] showProjectileEffect(MapleCharacter chr, int x, int y, int delay, int skillId, int level, int unk, byte facingleft, int objectId, int number) {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.SHOW_PROJECTILE_EFFECT.getValue());
        pw.writeInt(chr.getId());
        pw.writeInt(1);
        pw.writeInt(x);
        pw.writeInt(y);
        pw.writeInt(delay);
        pw.writeInt(skillId);
        pw.writeInt(unk);
        pw.writeInt(level);
        pw.write(facingleft);
        pw.writeInt(objectId);
        pw.writeInt(0);
        pw.writeInt(number);
        return pw.getPacket();
    }

    public static byte[] updateProjectileEffect(int id, int unk1, int unk2, int unk3, int unk4, byte facingleft) {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.UPDATE_PROJECTILE_EFFECT.getValue());
        pw.writeInt(id);
        pw.writeInt(unk1);
        pw.writeInt(unk2);
        pw.writeInt(unk3);
        pw.writeInt(unk4);
        pw.write(facingleft);
        return pw.getPacket();
    }

    public static byte[] removeProjectile(int unk) {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.REMOVE_PROJECTILE.getValue());
        pw.writeInt(unk);
        return pw.getPacket();
    }

    public static byte[] removeProjectileEffect(int id, int unk) {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.REMOVE_PROJECTILE_EFFECT.getValue());
        pw.writeInt(id);
        pw.writeInt(unk);
        return pw.getPacket();
    }

    public static byte[] bonusAttackRequest(int skillid, List<Triple<Integer, Integer, Integer>> mobList, boolean unk, int jaguarBleedingAttackCount, int... args) {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.BONUS_ATTACK_REQUEST.getValue());
        pw.writeInt(skillid);
        pw.writeInt(mobList.size());
        pw.write(unk);
        pw.writeInt((skillid + 1 == 3321014 || skillid + 1 == 3321016 || skillid + 1 == 3321018 || skillid + 1 == 3321022) ? jaguarBleedingAttackCount : 0);
        pw.writeInt(jaguarBleedingAttackCount);
        for (Triple<Integer, Integer, Integer> mob : mobList) {
            pw.writeInt(((Integer) mob.getLeft()).intValue());
            pw.writeInt(((Integer) mob.getMid()).intValue());
            if (skillid == 400041030) {
                pw.writeInt(((Integer) mob.getRight()).intValue());
            }
        }
        if (skillid == 400051067 || skillid == 400051065) {
            pw.writeInt(args[0]);
            Rectangle rect = new Rectangle(args[1], args[2], args[3], args[4]);
            pw.writeRect(rect);
            pw.writeInt(-201);
            pw.writeInt(-214);
            pw.writeInt(244);
            pw.writeInt(101);
        }
        if (skillid == 400011133) {
            pw.writeInt(400011028);
        }
        return pw.getPacket();
    }

    public static byte[] ShadowServentExtend(Point newpos) {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.SHADOW_SERVENT_EXTEND.getValue());
        pw.writeInt(newpos.x);
        pw.writeInt(newpos.y);
        return pw.getPacket();
    }

    public static byte[] ShadowServentEffect(MapleCharacter chr, MapleSummon summon, int skillid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPECIAL_SUMMON.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(summon.getObjectId());
        mplew.writeInt(2);
        return mplew.getPacket();
    }

    public static byte[] ShadowServentRefresh(MapleCharacter chr, MapleSummon summon, int count) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ELEMENTAL_RADIANCE.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(summon.getObjectId());
        mplew.writeInt(2);
        mplew.writeInt(count);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] DebuffObjON(int[] list, boolean hard) {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.DEBUFF_OBJECT.getValue());
        int arrayOfInt[], i;
        byte b;
        for (arrayOfInt = list, i = arrayOfInt.length, b = 0; b < i;) {
            Integer a = Integer.valueOf(arrayOfInt[b]);
            pw.write(1);
            pw.writeInt(a.intValue());
            pw.writeInt(1);
            pw.writeMapleAsciiString("sleepGas" + (hard ? a.intValue() : (a.intValue() * 10)));
            pw.writeMapleAsciiString("sleepGas");
            b++;
        }
        pw.write(0);
        return pw.getPacket();
    }

    public static byte[] lightningUnionSubAttack(int attackskillid, int skillid, int skillLevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LIGHTING_ATTACK.getValue());
        mplew.writeInt(attackskillid);
        mplew.writeInt(skillid);
        mplew.writeInt(skillLevel);
        return mplew.getPacket();
    }

    public static byte[] openUnionUI(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        List<MapleUnion> equipped = new ArrayList<>();
        for (MapleUnion union : c.getPlayer().getUnions().getUnions()) {
            if (union.getPosition() != -1) {
                equipped.add(union);
            }
        }
        mplew.writeShort(SendPacketOpcode.OPEN_UNION.getValue());
        mplew.writeInt(c.getPlayer().getUnionCoin());
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

    public static byte[] finalAttackRequest(int attackCount, int skillId, int FinalAttackId, int weaponType, MapleMonster monster) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.FINAL_ATTACK_REQUEST.getValue());
        packet.writeInt(attackCount);
        packet.writeInt(skillId);
        packet.writeInt(FinalAttackId);
        packet.writeInt(weaponType);
        if (skillId > 0) {
            packet.writeInt(1);
            packet.writeInt((monster == null) ? 0 : monster.getObjectId());
        } else {
            packet.writeInt(0);
        }
        return packet.getPacket();
    }

    public static byte[] RoyalGuardDamage() {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.ROYAL_DAMAGE.getValue());
        pw.writeInt(1);
        return pw.getPacket();
    }

    public static byte[] DamagePlayer2(int dam) {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.DAMAGE_PLAYER2.getValue());
        pw.writeInt(dam);
        return pw.getPacket();
    }

    public static byte[] EnterFieldPyschicInfo() {
        MaplePacketLittleEndianWriter pw = new MaplePacketLittleEndianWriter();
        pw.writeShort(SendPacketOpcode.ENTER_FIELD_PSYCHIC_INFO.getValue());
        pw.write(0);
        return pw.getPacket();
    }

    public static byte[] enforceMSG(String a, int id, int delay) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.ENFORCE_MSG.getValue());
        packet.writeMapleAsciiString(a);
        packet.writeInt(id);
        packet.writeInt(delay);
        packet.write(0);
        return packet.getPacket();
    }

    public static byte[] enforceMsgNPC(int npcid, int delay, String a) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.YELLOW_DLG.getValue());
        packet.writeInt(npcid);
        packet.writeInt(delay);
        packet.writeMapleAsciiString(a);
        packet.write(0);
        packet.write(0);
        packet.write(0);
        return packet.getPacket();
    }

    public static byte[] spawnSubSummon(short type, int key) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.SPAWN_SUB_SUMMON.getValue());
        packet.writeInt(type);
        packet.writeInt(key);
        return packet.getPacket();
    }

    public static byte[] jaguarAttack(int skillid) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.JAGUAR_ATTACK.getValue());
        packet.writeInt(skillid);
        return packet.getPacket();
    }

    public static byte[] B2BodyResult(MapleCharacter chr, int cid, short type, short type2, int key, Point pos, Point oldPos, short unk1, int sourceid, int level, int duration, short unk2, boolean isFacingLeft, int unk3, int unk4, String unk) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.B2BODY_RESULT.getValue());
        packet.writeShort(type);
        MapleSummon sum = null;
        packet.writeInt(cid);
        packet.writeInt(chr.getMapId());
        if (type == 0) {
            packet.writeShort(1);
            packet.writeInt(key);
            packet.write(type2);
            packet.write(0);
            packet.writePos(pos);
            if (type2 == 5) {
                packet.writePos(oldPos);
            } else if (type2 == 6) {
                packet.writeInt(0);
            }
            packet.writeShort(unk1);
            packet.writeInt(duration);
            packet.writeShort(unk2);
            packet.writeInt(sourceid);
            packet.writeShort(level);
            packet.write(0);
        } else if (type == 3) {
            packet.writeInt(chr.getId());
            packet.writeInt(sourceid);
            packet.writeInt(unk3);
            packet.writeInt(unk4);
        } else if (type == 4) {
            packet.writeShort(1);
            packet.write(0);
            packet.writePos(pos);
            packet.writeInt(10000);
            packet.writeShort(type2);
            packet.writeShort(unk1);
            packet.writeShort(unk2);
            packet.write(unk3);
            if (unk3 > 0) {
                packet.writeMapleAsciiString(unk);
            }
            packet.writeInt(unk4);
            packet.writeInt(sourceid);
            packet.write(isFacingLeft);
            packet.writeInt(0);
            packet.writeInt(0);
            packet.write(0);
            packet.writeInt(0);
            packet.writeInt(0);
            packet.writeInt(isFacingLeft ? -oldPos.x : oldPos.x);
            packet.writeInt(oldPos.y);
        }
        return packet.getPacket();
    }

    public static byte[] blackJack(MapleCharacter chr, int skillid, Point point) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.BLACKJACK.getValue());
        packet.writeInt(skillid);
        packet.writeInt(chr.getSkillLevel(GameConstants.getLinkedSkill(skillid)));
        packet.writeInt(1);
        packet.writeInt(point.x);
        packet.writeInt(point.y);
        if (skillid == 400041080) {
            packet.writeInt(chr.getSkillCustomValue0(400041080));
        }
        return packet.getPacket();
    }

    public static byte[] rangeAttack(int firstSkill, List<RangeAttack> skills) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.RANGE_ATTACK.getValue());
        packet.writeInt(firstSkill);
        packet.writeShort(skills.size());
        packet.writeInt(0);
        for (RangeAttack skill : skills) {
            packet.writeInt(skill.getSkillId());
            packet.writeInt((skill.getPosition()).x);
            packet.writeInt((skill.getPosition()).y);
            packet.writeShort(skill.getType());
            packet.writeInt(skill.getDelay());
            packet.writeInt(skill.getAttackCount());
            packet.writeInt(skill.getList().size());
            for (Integer list : skill.getList()) {
                packet.writeInt(list.intValue());
            }
            packet.writeInt(0);
            packet.writeInt(0);
            if (skill.getSkillId() != 400041079) {
                packet.writeInt(0);
            }
        }
        return packet.getPacket();
    }

    public static byte[] createMagicWreck(MapleMagicWreck mw) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.CREATE_MAGIC_WRECK.getValue());
        packet.writeInt(mw.getChr().getId());
        packet.writeInt((mw.getTruePosition()).x);
        packet.writeInt((mw.getTruePosition()).y);
        packet.writeInt(mw.getDuration());
        packet.writeInt(mw.getObjectId());
        packet.writeInt(mw.getSourceid());
        packet.writeInt(0);
        packet.writeInt(mw.getChr().getMwSize(mw.getSourceid()));
        return packet.getPacket();
    }

    public static byte[] removeMagicWreck(MapleCharacter chr, List<MapleMagicWreck> mws) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.REMOVE_MAGIC_WRECK.getValue());
        packet.writeInt(chr.getId());
        packet.writeInt(mws.size());
        packet.write(0);
        packet.write(0);
        for (MapleMagicWreck mw : mws) {
            packet.writeInt(mw.getObjectId());
        }
        return packet.getPacket();
    }

    public static byte[] ForceAtomAttack(int atomid, int cid, int mobid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FORCE_ATOM_ATTACK.getValue());
        mplew.writeInt(atomid);
        mplew.writeInt(cid);
        mplew.writeInt(1);
        mplew.writeInt(1);
        mplew.writeInt(1);
        mplew.writeInt(mobid);
        return mplew.getPacket();
    }

    public static byte[] ForceAtomEffect(MapleCharacter chr, int atomid, int type, int unk, int unk2, boolean left, Point pos1, Point pos2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FORCE_ATOM_ATTACK.getValue());
        mplew.writeInt(atomid);
        mplew.writeInt(chr.getId());
        mplew.writeInt(type);
        if (type == 3) {
            mplew.writeInt(unk);
            mplew.writePos(pos1);
            mplew.writePos(pos2);
        } else {
            mplew.writeInt(unk);
            mplew.writeInt(unk2);
            mplew.write(left);
            mplew.writePos(pos1);
            mplew.writePos(pos2);
        }
        return mplew.getPacket();
    }

    public static byte[] screenAttack(int mobId, int skillId, int skillLevel, long damage) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SCREEN_ATTACK.getValue());
        mplew.writeInt(mobId);
        mplew.writeInt(skillId);
        mplew.writeInt(skillLevel);
        mplew.writeLong(damage);
        return mplew.getPacket();
    }

    public static byte[] mutoSetTime(int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HUGNRY_MUTO.getValue());
        mplew.writeInt(1);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] finishMuto() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HUGNRY_MUTO.getValue());
        mplew.writeInt(2);
        return mplew.getPacket();
    }

    public static byte[] setMutoNewRecipe(int[] recipe, int length, EventInstanceManager eim) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HUGNRY_MUTO.getValue());
        mplew.writeInt(3);
        mplew.writeInt(recipe[0]);
        mplew.writeInt(recipe[1]);
        mplew.writeInt(recipe[2]);
        mplew.writeInt(recipe[3]);
        mplew.writeInt(recipe[4]);
        mplew.writeInt(length);
        for (int i = 0; i < length; i++) {
            if (eim.getProperty("recipeHidden" + i) != null) {
                mplew.writeInt(0);
            } else {
                mplew.writeInt(Integer.parseInt(eim.getProperty("recipeItem" + i)));
            }
            mplew.writeInt(Integer.parseInt(eim.getProperty("recipeReq" + i)));
            mplew.writeInt(Integer.parseInt(eim.getProperty("recipeCount" + i)));
        }
        return mplew.getPacket();
    }

    public static byte[] setMutoRecipe(int[] recipe, int length, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HUGNRY_MUTO.getValue());
        mplew.writeInt(4);
        mplew.writeInt(recipe[0]);
        mplew.writeInt(recipe[1]);
        mplew.writeInt(recipe[2]);
        mplew.writeInt(length);
        for (int i = 0; i < length; i++) {
            mplew.writeInt(Integer.parseInt(chr.getEventInstance().getProperty("recipeItem" + i)));
            mplew.writeInt(Integer.parseInt(chr.getEventInstance().getProperty("recipeReq" + i)));
            mplew.writeInt(Integer.parseInt(chr.getEventInstance().getProperty("recipeCount" + i)));
        }
        return mplew.getPacket();
    }

    public static byte[] addItemMuto(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HUGNRY_MUTO.getValue());
        mplew.writeInt(5);
        mplew.writeInt(1);
        mplew.writeInt(chr.getId());
        mplew.writeInt(((Integer) (chr.getRecipe()).left).intValue() - 1599086);
        mplew.writeInt(((Integer) (chr.getRecipe()).right).intValue());
        return mplew.getPacket();
    }

    public static byte[] ChainArtsFury(Point truePosition) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHAINARTS_FURY.getValue());
        mplew.writeInt(truePosition.x);
        mplew.writeInt(truePosition.y);
        return mplew.getPacket();
    }

    public static byte[] ICBM(boolean cancel, int skillid, Rectangle calculateBoundingBox) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ICBM.getValue());
        mplew.writeInt(cancel ? 0 : 1);
        mplew.writeInt(skillid);
        mplew.writeInt(1);
        mplew.writeInt(0);
        mplew.writeInt(calculateBoundingBox.x);
        mplew.writeInt(calculateBoundingBox.y);
        mplew.writeInt(calculateBoundingBox.width);
        mplew.writeInt(calculateBoundingBox.height);
        return mplew.getPacket();
    }

    public static byte[] specialMapSound(String str) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPECIAL_MAP_SOUND.getValue());
        mplew.writeMapleAsciiString(str);
        return mplew.getPacket();
    }

    public static byte[] specialMapEffect(int type, boolean isEliteMonster, String bgm, String back, String effect, String obj, String tile) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPECIAL_MAP_EFFECT.getValue());
        mplew.writeInt(type);
        mplew.writeInt(isEliteMonster ? 1 : 0);
        mplew.writeInt(0);
        switch (type - 2) {
            case 0:
            case 3:
                mplew.writeMapleAsciiString(bgm);
                mplew.writeMapleAsciiString(back);
                mplew.writeMapleAsciiString(effect);
                break;
            case 1:
            case 2:
                mplew.write(true);
                mplew.writeMapleAsciiString(bgm);
                mplew.writeMapleAsciiString(back);
                mplew.writeMapleAsciiString(effect);
                mplew.writeMapleAsciiString(obj);
                mplew.writeMapleAsciiString(tile);
                mplew.write(0);
                break;
            case 4:
                mplew.writeMapleAsciiString(bgm);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] unstableMemorize(int skillId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNSTABLE_MEMORIZE.getValue());
        mplew.writeInt(skillId);
        mplew.writeInt(4);
        return mplew.getPacket();
    }

    public static byte[] SpiritFlow(List<Pair<Integer, Integer>> skills) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPIRIT_FLOW.getValue());
        mplew.writeInt(skills.size());
        for (Pair<Integer, Integer> skill : skills) {
            mplew.writeInt(((Integer) skill.left).intValue());
            mplew.writeInt(((Integer) skill.right).intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] airBone(MapleCharacter chr, MapleMonster mob, int skill, int level, int end) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.AIRBONE.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(mob.getObjectId());
        mplew.writeInt(skill);
        mplew.writeInt(level);
        mplew.writeInt(end);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] poisonNova(MapleCharacter chr, List<Integer> novas) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.POISON_NOVA.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(novas.size());
        for (Iterator<Integer> iterator = novas.iterator(); iterator.hasNext();) {
            int nova = ((Integer) iterator.next()).intValue();
            mplew.writeInt(nova);
        }
        return mplew.getPacket();
    }

    public static byte[] runeCurse(String string, boolean delete) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.RUNE_CURSE.getValue());
        mplew.writeMapleAsciiString(string);
        mplew.writeInt(231);
        mplew.write(delete);
        mplew.write(delete);
        mplew.writeInt(50);
        mplew.writeInt(50); // 361 new
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] buffFreezer(int itemId, boolean use) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BUFF_FREEZER.getValue());
        mplew.writeInt(itemId);
        mplew.write(use);
        return mplew.getPacket();
    }

    public static byte[] quickSlot(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.QUICK_SLOT.getValue());
        mplew.write(true);
        for (int i = 0; i < 32; i++) {
            mplew.writeInt((chr.getKeyValue(333333, "quick" + i) < 0L) ? 0L : chr.getKeyValue(333333, "quick" + i));
        }
        return mplew.getPacket();
    }

    public static byte[] ignitionBomb(int skillId, int objectId, Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.IGNITION_BOMB.getValue());
        mplew.writeInt(skillId);
        mplew.writeInt(pos.x);
        mplew.writeInt(pos.y);
        mplew.writeInt(objectId);
        mplew.writeInt(5);
        return mplew.getPacket();
    }

    public static byte[] quickMove(List<QuickMoveEntry> quicks) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.QUICK_MOVE.getValue());
        mplew.write(quicks.size());
        for (QuickMoveEntry quick : quicks) {
            mplew.writeInt(quick.getType());
            mplew.writeInt(quick.getId());
            mplew.writeInt(quick.getIcon());
            mplew.writeInt(quick.getLevel());
            mplew.writeMapleAsciiString(quick.getDesc());
            mplew.writeLong(PacketHelper.getTime(-2L));
            mplew.writeLong(PacketHelper.getTime(-1L));
        }
        return mplew.getPacket();
    }

    public static byte[] dimentionMirror(List<DimentionMirrorEntry> quicks) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DIMENTION_MIRROR.getValue());
        mplew.writeInt(quicks.size());
        for (DimentionMirrorEntry quick : quicks) {
            mplew.writeMapleAsciiString(quick.getName());
            mplew.writeMapleAsciiString(quick.getDesc());
            mplew.writeInt(quick.getLevel());
            mplew.writeInt(quick.getType());
            mplew.writeInt(quick.getId());
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeMapleAsciiString("");
            mplew.write(false);
            mplew.writeInt(quick.getItems().size());
            for (Item item : quick.getItems()) {
                mplew.writeInt(item.getItemId());
            }
        }
        return mplew.getPacket();
    }

    public static byte[] TimeCapsule(int motionid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TIME_CAPSULE.getValue());
        mplew.writeInt(motionid);
        mplew.writeInt(1);
        return mplew.getPacket();
    }

    public static byte[] NettPyramidWave(int wave) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.NETT_PYRAMID_WAVE.getValue());
        mplew.writeInt(wave);
        return mplew.getPacket();
    }

    public static byte[] NettPyramidLife(int life) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.NETT_PYRAMID_LIFE.getValue());
        mplew.writeInt(life);
        return mplew.getPacket();
    }

    public static byte[] NettPyramidPoint(int point) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.NETT_PYRAMID_POINT.getValue());
        mplew.writeInt(point);
        return mplew.getPacket();
    }

    public static byte[] NettPyramidClear(boolean clear, int wave, int life, int point, int exp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.NETT_PYRAMID_CLEAR.getValue());
        mplew.write(clear);
        mplew.writeInt(wave);
        mplew.writeInt(life);
        mplew.writeInt(point);
        mplew.writeInt(exp);
        return mplew.getPacket();
    }

    public static byte[] ImageTalkNpc(int npcid, int time, String message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.YELLOW_DLG.getValue());
        mplew.writeInt(npcid);
        mplew.writeInt(time);
        mplew.writeMapleAsciiString(message);
        mplew.writeMapleAsciiString("");
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] inviteChair(int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.INVITE_CHAIR.getValue());
        mplew.writeInt(value);
        return mplew.getPacket();
    }

    public static byte[] requireChair(int id) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REQUIRE_CHAIR.getValue());
        mplew.writeInt(id);
        return mplew.getPacket();
    }

    public static byte[] resultChair(int v1, int v2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.RESULT_CHAIR.getValue());
        mplew.writeInt(v1);
        mplew.writeInt(v2);
        return mplew.getPacket();
    }

    public static byte[] fishing(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FISHING.getValue());
        mplew.writeInt(type);
        switch (type) {
            case 2:
                mplew.write(HexTool.getByteArrayFromHexString("00 00 00 40 0B 16 40 40 00 00 00 00 00 00 00 2E 40 00 00 00 00 00 80 41 40 01 00 00 00 00 00 00 00 00 C0 58 40 04 00 00 00 00 00 00 00 F4 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B8 0B 00 00 B8 0B 00 00 00 00 00 A0 99 99 B9 3F 00 00 00 A0 99 99 C9 3F 02 00 00 00 E8 03 00 00 D0 07 00 00 00 00 00 A0 99 99 A9 BF 00 00 00 00 00 00 00 00 03 00 00 00 F4 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"));
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] fishingResult(int cid, int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FISHING_RESULT.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(itemId);
        return mplew.getPacket();
    }

    public static byte[] ReturnSynthesizing() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.RETURN_SYNTHESIZING.getValue());
        mplew.writeInt(0);
        mplew.writeInt(2432805);
        mplew.writeInt(0);
        mplew.writeInt(-1509298872);
        return mplew.getPacket();
    }

    public static byte[] StigmaTime(int i) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.STIGMA_TIME.getValue());
        mplew.writeInt(i);
        return mplew.getPacket();
    }

    public static byte[] UseSkillWithUI(int unk, int skillid, int skilllevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.USE_SKILL_WITH_UI.getValue());
        mplew.writeInt(unk);
        if (unk > 0) {
            mplew.writeInt(unk);
            mplew.write(false);
            mplew.writeInt(1);
            mplew.write(false);
            mplew.writeInt(skillid);
            mplew.writeInt(skilllevel);
            mplew.writeZeroBytes(23);
        }
        return mplew.getPacket();
    }

    public static byte[] ActivePotionCooldown(int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ACTIVE_POTION_COOL.getValue());
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] potionCooldown() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.POTION_COOLDOWN.getValue());
        return mplew.getPacket();
    }

    public static byte[] JinHillah(int type, MapleCharacter chr, MapleMap map) {
        int i, x;
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.JIN_HILLAH.getValue());
        mplew.writeInt(type);
        switch (type) {
            case 0:
                mplew.writeInt(map.getCandles());
                mplew.write(false);
                break;
            case 1:
                mplew.writeInt(map.getLightCandles());
                break;
            case 3:
                mplew.writeInt((chr.getDeathCounts()).length);
                for (i = 0; i < (chr.getDeathCounts()).length; i++) {
                    mplew.writeInt(0);
                    mplew.write(chr.getDeathCounts()[i]);
                }
                break;
            case 4:
                mplew.writeInt(map.getSandGlassTime() * 1000L);
                mplew.writeInt(247);
                mplew.writeInt(1);
                break;
            case 6:
                x = Randomizer.rand(-700, 700);
                mplew.writeInt(x);
                chr.getMap().setCustomInfo(28002, x, 0);
                mplew.writeInt(266);
                mplew.writeInt(30);
                break;
            case 7:
                mplew.writeInt(30 - map.getReqTouched());
                break;
            case 8:
                mplew.write((map.getReqTouched() == 0));
                break;
            case 10:
                mplew.writeInt(5);
                mplew.writeInt(chr.getId());
                mplew.writeInt(chr.liveCounts());
                break;
        }
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] showICBM(int id, int readInt, int readInt2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHOW_ICBM.getValue());
        mplew.writeInt(id);
        mplew.writeInt(readInt);
        mplew.writeInt(readInt2);
        return mplew.getPacket();
    }

    public static byte[] followEffect(int initiator, int replier, Point toMap) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FOLLOW_EFFECT.getValue());
        mplew.writeInt(initiator);
        mplew.writeInt(replier);
        if (replier == 0) {
            mplew.write((toMap == null) ? 0 : 1);
            if (toMap != null) {
                mplew.writeInt(toMap.x);
                mplew.writeInt(toMap.y);
            }
        }
        return mplew.getPacket();
    }

    public static byte[] moveFollow(Point otherStart, Point myStart, Point otherEnd, List<LifeMovementFragment> moves) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FOLLOW_MOVE.getValue());
        mplew.writePos(otherStart);
        mplew.writePos(myStart);
        PacketHelper.serializeMovementList(mplew, moves);
        mplew.write(17);
        for (int i = 0; i < 8; i++) {
            mplew.write(0);
        }
        mplew.write(0);
        mplew.writePos(otherEnd);
        mplew.writePos(otherStart);
        return mplew.getPacket();
    }

    public static byte[] getFollowMsg(int opcode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FOLLOW_MSG.getValue());
        mplew.writeLong(opcode);
        return mplew.getPacket();
    }

    public static byte[] battleStatistics() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_STATISTICS.getValue());
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] createAtom(MapleAtom atom) {
        /* 7140 */ MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        /* 7141 */ mplew.writeShort(SendPacketOpcode.CREATE_FORCE_ATOM.getValue());
        /* 7142 */ mplew.write(atom.isByMob());
        /* 7143 */ if (atom.isByMob()) {
            /* 7144 */ mplew.writeInt(atom.getDwUserOwner());
        }
        /* 7146 */ mplew.writeInt(atom.getDwTargetId());
        /* 7147 */ mplew.writeInt(atom.getnForceAtomType());
        /* 7148 */ if (atom.getnForceAtomType() != 36 && atom.getnForceAtomType() != 37) {
            /* 7149 */ switch (atom.getnForceAtomType()) {
                case 0:
                case 9:
                case 14:
                case 29:
                case 42:
                    /* 7155 */ if (atom.getnForceAtomType() == 29 || atom.getnForceAtomType() == 42) {
                        /* 7156 */ mplew.writeInt(atom.getnSkillId());
                        /* 7157 */ if (atom.getnSkillId() == 400021069) {
                            /* 7158 */ mplew.writeInt(200);
                        }
                    }
                    break;
                default:
                    /* 7163 */ mplew.write(atom.isToMob());
                    /* 7164 */ switch (atom.getnForceAtomType()) {

                        case 2:
                        case 3:
                        case 6:
                        case 7:
                        case 11:
                        case 12:
                        case 13:
                        case 17:
                        case 19:
                        case 20:
                        case 23:
                        case 24:
                        case 25:
                        case 27:
                        case 28:
                        case 30:
                        case 32:
                        case 34:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 47:
                        case 48:
                        case 49:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 60:
                        case 64:
                        case 65:
                        case 67:
                        case 72:
                        case 73:
                        case 75:
                            /* 7205 */ mplew.writeInt(atom.getDwTargets().size());
                            /* 7206 */ for (Integer dwTarget : atom.getDwTargets()) {
                                /* 7207 */ mplew.writeInt(dwTarget.intValue());
                            }
                            break;
                        default:
                            /* 7211 */ if (atom.getnForceAtomType() == 62) {
                                /* 7212 */ mplew.writeInt(atom.getDwTargets().size());
                                /* 7213 */ for (Integer dwTarget : atom.getDwTargets()) /* 7214 */ {
                                    mplew.writeInt(dwTarget.intValue());
                                }
                                break;
                            } else {
                                /* 7217 */ mplew.writeInt(atom.getDwFirstTargetId());
                            }
                            break;
                    }
            }
        }
        /* 7224 */ if (atom.getnForceAtomType() != 29 && atom.getnForceAtomType() != 42 && atom.getnForceAtomType() != 0) {
            /* 7225 */ mplew.writeInt(atom.getnSkillId());
        }

        /* 7228 */ for (ForceAtom forceAtom : atom.getForceAtoms()) {
            /* 7229 */ mplew.write(true);
            /* 7230 */ mplew.writeInt(forceAtom.getnAttackCount());
            /* 7231 */ mplew.writeInt(forceAtom.getnInc());
            /* 7232 */ mplew.writeInt(forceAtom.getnFirstImpact());
            /* 7233 */ mplew.writeInt(forceAtom.getnSecondImpact());
            /* 7234 */ mplew.writeInt(forceAtom.getnAngle());
            /* 7235 */ mplew.writeInt(forceAtom.getnStartDelay());
            /* 7236 */ mplew.writeInt(forceAtom.getnStartX());
            /* 7237 */ mplew.writeInt(forceAtom.getnStartY());
            /* 7238 */ mplew.writeInt(forceAtom.getDwCreateTime());
            /* 7239 */ mplew.writeInt(forceAtom.getnMaxHitCount());
            /* 7240 */ mplew.writeInt(forceAtom.getnEffectIdx());
            /* 7241 */ mplew.writeInt((atom.getnSkillId() == 400011058 || atom.getnSkillId() == 400011059) ? 2000 : 0);
        }

        /* 7244 */ mplew.write(false);

        /* 7246 */ switch (atom.getnForceAtomType()) {
            case 7:
                mplew.writeInt(atom.getnForcedTargetX());
                mplew.writeInt(atom.getnForcedTargetY());
                mplew.writeInt(atom.getnForcedTargetX() + 500);
                mplew.writeInt(atom.getnForcedTargetY() + 500);
                break;
            case 11:
                /* 7248 */ mplew.writeInt(atom.getnForcedTargetX() - 240);
                /* 7249 */ mplew.writeInt(atom.getnForcedTargetY() - 120);
                /* 7250 */ mplew.writeInt(atom.getnForcedTargetX() + 240);
                /* 7251 */ mplew.writeInt(atom.getnForcedTargetY() + 120);
                /* 7252 */ mplew.writeInt(atom.getnItemId());
                break;
            case 13:
                /* 7255 */ mplew.writeInt(25121005);
                break;
            case 9:
                /* 7258 */ mplew.writeInt(-atom.getnForcedTargetX());
                /* 7259 */ mplew.writeInt(-atom.getnForcedTargetY());
                /* 7260 */ mplew.writeInt(atom.getnForcedTargetX());
                /* 7261 */ mplew.writeInt(atom.getnForcedTargetY());
                break;
            case 15:
                /* 7264 */ mplew.writeInt(atom.getnForcedTargetX() - 10);
                /* 7265 */ mplew.writeInt(atom.getnForcedTargetY() - 10);
                /* 7266 */ mplew.writeInt(atom.getnForcedTargetX() + 10);
                /* 7267 */ mplew.writeInt(atom.getnForcedTargetY() + 10);
                /* 7268 */ mplew.write(false);
                break;
            case 29:
                /* 7271 */ mplew.writeInt(atom.getnForcedTargetX() - 100);
                /* 7272 */ mplew.writeInt(atom.getnForcedTargetY() - 100);
                /* 7273 */ mplew.writeInt(atom.getnForcedTargetX() + 100);
                /* 7274 */ mplew.writeInt(atom.getnForcedTargetY() + 100);

                /* 7276 */ mplew.writeInt(atom.getnForcedTargetX());
                /* 7277 */ mplew.writeInt(atom.getnForcedTargetY());
                break;
            case 33:
                /* 7280 */ mplew.writeInt(atom.getnForcedTargetX());
                /* 7281 */ mplew.writeInt(atom.getnForcedTargetY());
                /* 7282 */ mplew.writeInt(0);
                /* 7283 */ mplew.writeInt(atom.getSearchX());
                /* 7284 */ mplew.writeInt(atom.getSearchX1());
                break;
        }
        /* 7287 */ switch (atom.getnForceAtomType()) {
            case 4:
            case 16:
            case 20:
            case 26:
            case 30:
            case 61:
            case 64:
            case 67:
                /* 7296 */ mplew.writeInt(atom.getnForcedTargetX());
                /* 7297 */ mplew.writeInt(atom.getnForcedTargetY());
                mplew.writeZeroBytes(1000);
                break;
        }

        /* 7301 */ switch (atom.getnForceAtomType()) {
            case 17:
                /* 7303 */ mplew.writeInt(atom.getnArriveDir());
                /* 7304 */ mplew.writeInt(atom.getnArriveRange());
                break;
            case 18:
                /* 7307 */ mplew.writeInt(atom.getnForcedTargetX());
                /* 7308 */ mplew.writeInt(atom.getnForcedTargetY());
                break;
            case 27:
                /* 7311 */ mplew.writeInt(-atom.getnForcedTargetX());
                /* 7312 */ mplew.writeInt(-atom.getnForcedTargetY());
                /* 7313 */ mplew.writeInt(atom.getnForcedTargetX());
                /* 7314 */ mplew.writeInt(atom.getnForcedTargetY());

                /* 7316 */ mplew.writeInt(0);
                break;
            case 28:
            case 34:
                /* 7320 */ mplew.writeInt(atom.getnForcedTargetX() - 5000);
                /* 7321 */ mplew.writeInt(atom.getnForcedTargetY() - 5000);
                /* 7322 */ mplew.writeInt(atom.getnForcedTargetX() + 5000);
                /* 7323 */ mplew.writeInt(atom.getnForcedTargetY() + 5000);

                /* 7325 */ mplew.writeInt(20);
                break;
            case 57:
            case 58:
                /* 7329 */ mplew.writeInt(-atom.getSearchX1());
                /* 7330 */ mplew.writeInt(-atom.getSearchY1());
                /* 7331 */ mplew.writeInt(atom.getSearchX1());
                /* 7332 */ mplew.writeInt(atom.getSearchY1());

                /* 7334 */ mplew.writeInt(atom.getnDuration());

                /* 7336 */ mplew.writeInt(atom.getSearchX());
                /* 7337 */ mplew.writeInt(atom.getSearchY());
                break;
            case 36:
            case 39:
                /* 7341 */ mplew.writeInt(5);
                /* 7342 */ mplew.writeInt(550);
                /* 7343 */ mplew.writeInt(3);

                /* 7345 */ mplew.writeInt(-300);
                /* 7346 */ mplew.writeInt(-300);
                /* 7347 */ mplew.writeInt(300);
                /* 7348 */ mplew.writeInt(300);

                /* 7350 */ if (atom.getnForceAtomType() == 36) {
                    /* 7351 */ mplew.writeInt(-50);
                    /* 7352 */ mplew.writeInt(-50);
                    /* 7353 */ mplew.writeInt(50);
                    /* 7354 */ mplew.writeInt(50);

                    /* 7356 */ mplew.writeInt(atom.getDwUnknownPoint());
                }
                break;
            case 37:
                /* 7360 */ mplew.writeInt(0);

                /* 7362 */ mplew.writeInt(-300);
                /* 7363 */ mplew.writeInt(-300);
                /* 7364 */ mplew.writeInt(300);
                /* 7365 */ mplew.writeInt(300);

                /* 7367 */ mplew.writeInt(200);
                /* 7368 */ mplew.writeInt(atom.getDwUnknownPoint());
                break;
            case 42:
                /* 7371 */ mplew.writeInt(atom.getnForcedTargetX() - 240);
                /* 7372 */ mplew.writeInt(atom.getnForcedTargetY() - 120);
                /* 7373 */ mplew.writeInt(atom.getnForcedTargetX() + 240);
                /* 7374 */ mplew.writeInt(atom.getnForcedTargetY() + 120);
            case 49:
                /* 7376 */ mplew.writeInt(atom.getnItemId());
                /* 7377 */ mplew.writeInt(atom.getDwSummonObjectId());

                /* 7379 */ mplew.writeInt(atom.getnForcedTargetX() - 50);
                /* 7380 */ mplew.writeInt(atom.getnForcedTargetY() - 100);
                /* 7381 */ mplew.writeInt(atom.getnForcedTargetX() + 50);
                /* 7382 */ mplew.writeInt(atom.getnForcedTargetY() + 100);
                break;
            case 50:
                /* 7385 */ mplew.writeInt(atom.getnForcedTargetX());
                /* 7386 */ mplew.writeInt(atom.getnForcedTargetY());
                mplew.writeInt(0);
                break;
        }

        /* 7390 */ if (atom.getnSkillId() == 25100010 || atom.getnSkillId() == 25120115) {
            /* 7391 */ mplew.writeInt(atom.getnFoxSpiritSkillId());
            /* 7392 */        } else if (atom.getnSkillId() == 400011131) {
            /* 7393 */ mplew.writeInt(atom.getDwUnknownInteger());
            /* 7394 */ mplew.write(atom.getDwUnknownByte());
        }
        /* 7397 */ return mplew.getPacket();
    }

    public static byte[] RemoveAtom(MapleCharacter chr, int type, int objid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_FORCE_ATOM.getValue());
        mplew.writeInt(type);
        mplew.writeInt(objid);
        mplew.writeInt(chr.getId());
        return mplew.getPacket();
    }

    public static byte[] onUIEventInfo(MapleClient c, boolean open) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        LocalDate startDate = LocalDate.of(2021, 2, 25);
        LocalDate finishDate = LocalDate.of(2021, 6, 16);
        mplew.writeShort(SendPacketOpcode.UI_EVENT_INFO.getValue());
        mplew.writeInt(100748);
        mplew.write(open);
        mplew.writeShort(open ? 0 : 1);
        mplew.writeLong(PacketHelper.getTime(finishDate.toEpochDay()));
        mplew.writeLong(PacketHelper.getTime(startDate.toEpochDay()));
        mplew.write(0);
        mplew.writeShort(0);
        mplew.writeInt(100748);
        mplew.writeLong(0L);
        mplew.write(0);
        mplew.writeInt(252);
        mplew.writeInt(253);
        mplew.writeInt(254);
        mplew.writeInt(3600);
        mplew.writeMapleAsciiString("chariotInfo4");
        mplew.writeMapleAsciiString("");
        mplew.writeMapleAsciiString("chariotInfo4");
        mplew.writeMapleAsciiString("chariotAttend");
        mplew.writeInt(0);
        mplew.writeInt(GameConstants.chariotItems.size());
        for (Triple<Integer, Integer, Integer> item : GameConstants.chariotItems) {
            mplew.writeInt(((Integer) item.left).intValue());
            mplew.writeInt(((Integer) item.mid).intValue());
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(0);
        }
        mplew.writeInt(0);
        mplew.writeInt(1254);
        return mplew.getPacket();
    }

    public static byte[] onUIEventInfoSet() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UI_EVENT_INFO_SET.getValue());
        mplew.write(HexTool.getByteArrayFromHexString("19 00 00 00 72 00 00 00 67 42 00 00 72 00 00 00 FA 38 00 00 E6 04 00 00 70 87 01 00 E2 04 00 00 59 A4 07 00 72 00 00 00 E1 39 00 00 E6 04 00 00 8C 89 01 00 72 00 00 00 0B 39 00 00 72 00 00 00 2B 39 00 00 72 00 00 00 8C A3 07 00 00 05 00 00 90 A5 07 00 72 00 00 00 69 3A 00 00 E6 04 00 00 C7 88 01 00 72 00 00 00 09 AB 00 00 72 00 00 00 FC 3C 00 00 72 00 00 00 B5 38 00 00 72 00 00 00 21 3F 00 00 E6 04 00 00 28 89 01 00 72 00 00 00 6B 3A 00 00 72 00 00 00 AF 38 00 00 72 00 00 00 84 3A 00 00 72 00 00 00 41 40 00 00 72 00 00 00 6D 3A 00 00 72 00 00 00 18 41 00 00 F0 04 00 00 DF A4 07 00 72 00 00 00 BA 38 00 00"));
        return mplew.getPacket();
    }

    public static byte[] onUIEventSet(int objectId, int windowId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UI_EVENT_SET.getValue());
        mplew.writeInt(objectId);
        mplew.writeInt(windowId);
        return mplew.getPacket();
    }

    public static byte[] portalTeleport(String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PORTAL_TELEPORT.getValue());
        mplew.writeMapleAsciiString(name);
        return mplew.getPacket();
    }

    public static byte[] spawnSecondAtoms(int cid, List<SecondAtom> tiles, int spawnType) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_SECOND_ATOMS.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(tiles.size());
        int i = 1;
        for (SecondAtom tile : tiles) {
            mplew.writeInt(tile.getObjectId());
            mplew.writeInt((tile.getSkillId() == 5201017) ? 11 : tile.getSkillId() == 2121052 ? 3 : 0);
            mplew.writeInt(tile.getProjectileType());
            mplew.writeInt(i);
            mplew.writeInt((tile.getLocalOnly() == 1) ? tile.getTargetId() : tile.getOwnerId());
            mplew.writeInt(tile.getTargetId());
            mplew.writeInt((tile.getSkillId() == 5201017) ? 450 + ((i - 1) * 120) : (tile.getSkillId() == 2121052) ? 480 : (tile.getSkillId() == 4121020) ? 810 + ((i - 1) * 60) : (tile.getSkillId() == 162121010) ? 1110 : ((tile.getSkillId() == 162111005) ? 600 : ((tile.getSkillId() == 400051069) || (tile.getSkillId() == 2311017) ? (i * 120) : ((tile.getSkillId() == 400041058) ? 150 : 0))));
            mplew.writeInt((tile.getSkillId() == 5201017) ? 480 + ((i - 1) * 120) : (tile.getSkillId() == 5121027) ? 1080 : (tile.getSkillId() == 2121052) ? 720 : (tile.getSkillId() == 4121020) ? 930 + ((i - 1) * 60) : tile.getSkillId() == 2311017 ? 60 + ((i - 1) * 120) : tile.getDelay());
            mplew.writeInt((tile.getSkillId() == 162121010) ? (20 + (i - 1) * 60) : 0);
            mplew.writeInt(tile.getSkillId());
            mplew.writeInt(0);
            mplew.writeInt((tile.getSkillId() == 5201017) ? 10 : (tile.getSkillId() == 4121020) ? 30 : (tile.getSkillId() == 162111002) ? 20 : ((tile.getSkillId() == 400051069 || tile.getSkillId() == 400021092 || tile.getSkillId() == 400051069 || tile.getSkillId() == 162101000 || tile.getSkillId() == 2121052) ? 1 : 0));
            mplew.writeInt(tile.getDuration());
            mplew.writeInt(tile.getCustom());
            mplew.writeInt(tile.getMaxPerHit());
            mplew.writeInt((tile.getSkillId() == 400011047) ? 1 : 0);
            mplew.writeInt(0);
            mplew.writeInt((tile.getPoint()).x);
            mplew.writeInt((tile.getPoint()).y);
            mplew.write((tile.getSkillId() == 400011047 || tile.getSkillId() == 162101000));
            mplew.write(false);
            mplew.write(false);
            mplew.writeInt(tile.getPoints().size());
            for (Iterator<Integer> iterator = tile.getPoints().iterator(); iterator.hasNext();) {
                int point = ((Integer) iterator.next()).intValue();
                mplew.writeInt(point);
            }
            i++;
        }
        mplew.writeInt(spawnType);
        return mplew.getPacket();
    }

    public static byte[] removeSecondAtom(int cid, int objectId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_SECOND_ATOM.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(1);
        mplew.writeInt(objectId);
        mplew.writeInt(0);
        mplew.writeInt(1); // 351 new
        return mplew.getPacket();
    }

    public static byte[] createSpecialPortal(int cid, List<SpecialPortal> lists) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CREATE_SPECIAL_PORTAL.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(lists.size());
        for (SpecialPortal list : lists) {
            mplew.writeInt(list.getOwnerId());
            mplew.writeInt(list.getObjectId());
            mplew.writeInt(list.getSkillType());
            mplew.writeInt(list.getSkillId());
            mplew.writeInt(list.getMapId());
            mplew.writeInt(list.getPointX());
            mplew.writeInt(list.getPointY());
            mplew.writeInt(list.getDuration());
        }
        mplew.writeInt(0); //355 
        return mplew.getPacket();
    }

    public static byte[] removeSpecialPortal(int cid, List<SpecialPortal> lists) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_SPECIAL_PORTAL.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(lists.size());
        for (SpecialPortal list : lists) {
            mplew.writeInt(list.getObjectId());
        }
        return mplew.getPacket();
    }

    public static byte[] showUnionRaidHpUI(int mobid, long currenthp, long maxhp, int mobid2, long currenthp2, long maxhp2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNION_RAID_HP.getValue());
        mplew.writeInt(mobid);
        mplew.writeLong(currenthp);
        mplew.writeLong(maxhp);
        mplew.writeInt(mobid2);
        mplew.writeLong(currenthp2);
        mplew.writeLong(maxhp2);
        return mplew.getPacket();
    }

    public static byte[] setUnionRaidScore(long score) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNION_RAID_SCORE.getValue());
        mplew.writeLong(score);
        return mplew.getPacket();
    }

    public static byte[] setUnionRaidCoinNum(int qty, boolean set) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UNION_RAID_COIN.getValue());
        mplew.writeInt(qty);
        mplew.write(set);
        return mplew.getPacket();
    }

    public static byte[] showScrollOption(int itemId, int scrollId, StarForceStats es) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SCROLL_CHAT.getValue());
        mplew.writeInt(scrollId);
        mplew.writeInt(itemId);
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
        return mplew.getPacket();
    }

    public static class FarmPacket {

        public static byte[] onEnterFarm(MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ENTER_FARM.getValue());
            PacketHelper.addCharacterInfo(mplew, chr);
            int v13 = 0;
            while (v13 < 37500) {
                mplew.writeInt((v13 < 3000) ? 4150001 : 0);
                mplew.writeInt(0);
                mplew.write(0);
                mplew.writeInt(0);
                mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
                v13 += 60;
            }
            mplew.writeInt(14);
            mplew.writeInt(14);
            mplew.writeInt(0);
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
            return mplew.getPacket();
        }

        public static byte[] onSetFarmUser(MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SET_FARM_USER.getValue());
            mplew.writeInt(chr.getClient().getAccID());
            mplew.writeInt(0);
            mplew.writeLong(0L);
            PacketHelper.addFarmInfo(mplew, chr.getClient(), 0);
            farmUserGameInfo(mplew, false, chr);
            PacketHelper.addFarmInfo(mplew, chr.getClient(), 0);
            farmUserGameInfo(mplew, false, chr);
            mplew.writeInt(0);
            mplew.writeInt(-1);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static void farmUserGameInfo(MaplePacketLittleEndianWriter mplew, boolean unk, MapleCharacter chr) {
            mplew.write(unk);
            if (unk) {
                mplew.writeInt(chr.getClient().getWorld());
                mplew.writeMapleAsciiString("et");
                mplew.writeInt(chr.getId());
                mplew.writeMapleAsciiString(chr.getName());
            }
        }

        public static byte[] onFarmNotice(String str) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.FARM_NOTICE.getValue());
            mplew.writeMapleAsciiString(str);
            return mplew.getPacket();
        }

        public static byte[] onFarmSetInGameInfo(MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.FARM_SET_INGAME_INFO.getValue());
            farmUserGameInfo(mplew, false, chr);
            return mplew.getPacket();
        }

        public static byte[] onFarmRequestSetInGameInfo(MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.FARM_REQ_SET_INGAME_INFO.getValue());
            farmUserGameInfo(mplew, false, chr);
            mplew.writeInt(chr.getClient().getWorld());
            mplew.writeMapleAsciiString("The Black");
            mplew.writeInt(chr.getId());
            mplew.writeMapleAsciiString(chr.getName());
            return mplew.getPacket();
        }

        public static byte[] onFarmImgUpdate(MapleClient c, int length, byte[] img) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.FARM_IMG_UPDATE.getValue());
            mplew.writeInt(c.getAccID());
            mplew.writeInt(length);
            mplew.write(img);
            return mplew.getPacket();
        }
    }

    public static byte[] popupHomePage() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.POPUP_HOMEPAGE.getValue());
        mplew.write(0);
        mplew.write(1);
        mplew.writeMapleAsciiString("");
        return mplew.getPacket();
    }

    public static byte[] getTpAdd(int type, int count) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TP_ADD.getValue());
        mplew.write(type);
        mplew.writeInt(count);
        return mplew.getPacket();
    }

    public static byte[] getPhotoResult(MapleClient c, byte[] farmImg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PHOTO_RESULT.getValue());
        mplew.writeInt(c.getAccID());
        mplew.writeInt(farmImg.length);
        if (farmImg.length > 0) {
            mplew.write(farmImg);
        }
        return mplew.getPacket();
    }

    public static byte[] updateGuildScore(int guildScore) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.UPDATE_GUILD_SCORE.getValue());
        mplew.writeInt(guildScore);
        return mplew.getPacket();
    }

    public static byte[] updateShapeShift(int id, boolean use) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHAPE_SHIFT.getValue());
        mplew.writeInt(id);
        mplew.write(use);
        return mplew.getPacket();
    }

    public static byte[] flowOfFight(int skillId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FLOW_OF_FIGHT.getValue());
        mplew.writeInt(skillId);
        return mplew.getPacket();
    }

    public static byte[] spawnOrb(int ownerId, List<MapleOrb> orbs) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_ORB.getValue());
        mplew.writeInt(ownerId);
        mplew.writeInt(orbs.size());
        for (MapleOrb orb : orbs) {
            mplew.write(true);
            mplew.writeInt(orb.getOrbType());
            mplew.writeInt(orb.getObjectId());
            mplew.writeInt(orb.getPlayerId());
            mplew.writeInt((orb.getPos()).x);
            mplew.writeInt((orb.getPos()).y);
            mplew.writeInt(orb.getFacing());
            mplew.writeInt(orb.getUnk3());
            mplew.writeInt(orb.getSkillId());
            mplew.writeInt(orb.getAttackCount());
            mplew.writeInt(orb.getSubTime());
            mplew.writeInt(orb.getDuration());
            mplew.writeInt(orb.getDelay());
            mplew.writeInt(orb.getUnk1());
            mplew.writeInt(orb.getUnk2());
        }
        return mplew.getPacket();
    }

    public static byte[] removeOrb(int ownerId, List<MapleOrb> orbs) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_ORB.getValue());
        mplew.writeInt(ownerId);
        mplew.writeInt(orbs.size());
        for (MapleOrb orb : orbs) {
            mplew.writeInt(orb.getObjectId());
        }
        return mplew.getPacket();
    }

    public static byte[] moveOrb(int cid, int type, int objectId, int action, Point pos, int unk1, int unk2, int unk3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MOVE_ORB.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(type);
        mplew.writeInt(objectId);
        mplew.writeInt(action);
        if (type == 1) {
            mplew.writePos(pos);
            mplew.writeInt(unk1);
            mplew.writeInt(unk2);
            mplew.writeInt(unk3);
        }
        return mplew.getPacket();
    }

    public static byte[] fullMaker(int remainCount, int remainTime) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FULL_MAKER.getValue());
        mplew.write((remainCount > 0));
        if (remainCount > 0) {
            mplew.writeInt(remainCount);
            mplew.writeInt(remainTime);
        }
        return mplew.getPacket();
    }

    public static byte[] egoWeapon(int skillId, Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.EGO_WEAPON.getValue());
        mplew.writeInt(skillId);
        mplew.writeInt(pos.x);
        mplew.writeInt(pos.y);
        return mplew.getPacket();
    }

    public static byte[] getRefreshQuestInfo(int cid, int active, int skillid, int type) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.REFRESH_QUESTINFO.getValue() + type);
        packet.writeInt(cid);
        packet.write(active);
        packet.writeInt(skillid);
        return packet.getPacket();
    }

    public static byte[] getDotge() {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.DOTGE.getValue());
        return packet.getPacket();
    }

    public static byte[] getExpertThrow() {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.EXPERT_THROW.getValue());
        return packet.getPacket();
    }

    public static byte[] getScatteringShot(int skillid, int nowcount, int maxcount, int cool) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SCATTERING_SHOT.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(nowcount);
        mplew.writeInt(maxcount);
        mplew.writeInt(cool);
        return mplew.getPacket();
    }

    public static byte[] getWreckAttack(MapleCharacter chr, List<MapleMagicWreck> mw) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WRECK_ATTACK.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(mw.size());
        for (MapleMagicWreck m : mw) {
            mplew.writeInt(m.getObjectId());
            mplew.writePosInt(m.getPosition());
        }
        return mplew.getPacket();
    }

    public static byte[] getDeathBlessStack(MapleCharacter chr, List<MapleMonster> monster) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DEATH_BLESS_STACK.getValue());
        mplew.write(1);
        mplew.writeInt(monster.size());
        for (MapleMonster mob : monster) {
            mplew.writeInt(mob.getObjectId());
        }
        mplew.writeInt(monster.size());
        for (MapleMonster mob : monster) {
            mplew.writeInt(mob.getObjectId());
            mplew.writeInt(mob.getCustomValue0(63110011));
            mplew.writeInt(0);
            mplew.writeInt(mob.getCustomTime(63110011).intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] getDeathBlessAttack(List<MapleMonster> monster, int skillid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DEATH_BLESS_ATTACK.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(0);
        mplew.writeInt(monster.size());
        for (MapleMonster mob : monster) {
            mplew.writeInt(mob.getObjectId());
            mplew.writeInt(1);
            mplew.writeInt(300);
        }
        return mplew.getPacket();
    }

    public static byte[] FireWork(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FIRE_WORK.getValue());
        mplew.writeInt(chr.getId());
        return mplew.getPacket();
    }

    public static byte[] NightWalkerShadowSpearBig(int x, int y) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SHADOW_SPEAR_BIG.getValue());
        mplew.writeInt(x);
        mplew.writeInt(y);
        return mplew.getPacket();
    }

    public static byte[] getLightOfCurigi(int maxcount, int oid, boolean on) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.APPLY_LIGHT_OF_CURIGI.getValue());
        if (!on) {
            mplew.write(0);
        } else {
            mplew.write(1);
            mplew.writeShort(15);
            mplew.writeShort(maxcount);
            mplew.writeInt(0);
            mplew.writeInt(1);
            mplew.writeInt(oid);
        }
        return mplew.getPacket();
    }

    public static byte[] setFallingTime(int fallingspeed, int fallingtime) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FALLING_TIME.getValue());
        mplew.writeShort(fallingspeed);
        mplew.writeShort(fallingtime);
        return mplew.getPacket();
    }

    public static byte[] getBlizzardTempest(List<Triple<Integer, Integer, Integer>> lis) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BlizzardTempest.getValue());
        mplew.writeInt(lis.size());
        for (Triple<Integer, Integer, Integer> list : lis) {
            mplew.writeInt(((Integer) list.getLeft()).intValue());
            mplew.writeInt(((Integer) list.getMid()).intValue());
            mplew.writeInt(((Integer) list.getRight()).intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] getDragonForm(MapleCharacter chr, int unk, int skillid, int skilllevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DRAGON_CHANGE.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(unk);
        mplew.writeInt(skillid);
        mplew.writeInt(skilllevel);
        return mplew.getPacket();
    }

    public static byte[] getDragonAttack(MapleCharacter chr, int skillid, int skilllevel, Point pos, Point pos2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DRAGON_ATTACK.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillid);
        mplew.writeInt(skilllevel);
        mplew.writePosInt(pos);
        mplew.writePosInt(pos2);
        return mplew.getPacket();
    }

    public static byte[] getMechDoorCoolDown(MechDoor md) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MECH_DOOR_COOLDOWN.getValue());
        mplew.writeInt(md.getOwnerId());
        return mplew.getPacket();
    }

    public static byte[] RebolvingBunk(int cid, int oid, int mobcode, Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REBOLVING_BUNK.getValue());
        mplew.writeInt(cid);
        mplew.write(1);
        mplew.write(1);
        mplew.writeInt(oid);
        mplew.writeInt(mobcode);
        mplew.writeInt(pos.x);
        mplew.writeInt(pos.y);
        return mplew.getPacket();
    }

    public static byte[] Novilityshiled(int shiled) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.NOVILITY_SHILED.getValue());
        mplew.writeInt(shiled);
        return mplew.getPacket();
    }

    public static byte[] getEarlySkillActive(int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.EARLY_SKILL_ACTIVE.getValue());
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static final byte[] CrystalControl(MapleCharacter chr, int oid, Point pos, int skillid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DAMAGE_SUMMON.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(oid);
        mplew.writeInt(skillid);
        mplew.writeInt((skillid == 152101008) ? 20 : 1);
        mplew.writePosInt(pos);
        return mplew.getPacket();
    }

    public static final byte[] MarkinaMoveAttack(MapleCharacter chr, int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DAMAGE_SUMMON_2.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(oid);
        mplew.writeInt(9);
        return mplew.getPacket();
    }

    public static final byte[] CrystalTeleport(MapleCharacter chr, int oid, Point pos, int skillid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CRYSTAL_TELEPORT.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(oid);
        mplew.writePosInt(pos);
        return mplew.getPacket();
    }

    public static final byte[] BossMatchingChance(List<Pair<Integer, Integer>> list) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BOSS_MATCHING_CHANCE.getValue());
        mplew.writeInt(list.size());
        for (Pair<Integer, Integer> li : list) {
            mplew.writeInt(((Integer) li.getLeft()).intValue());
            mplew.writeInt(((Integer) li.getRight()).intValue());
        }
        return mplew.getPacket();
    }

    public static final byte[] ExpDropPenalty(boolean first, int totaltime, int nowtime, int exp, int drop) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.EXP_DROP_PENALTY.getValue());
        mplew.writeShort(first ? 0 : 1);
        mplew.writeInt(totaltime);
        mplew.writeInt(nowtime);
        mplew.writeInt(exp);
        mplew.writeInt(drop);
        return mplew.getPacket();
    }

    public static final byte[] PenaltyMsg(String msg, int type, int time, int type2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PENALTY_MSG.getValue());
        mplew.writeMapleAsciiString(msg);
        mplew.writeInt(type);
        mplew.writeInt(time);
        mplew.write(1);
        mplew.writeInt(type2);
        return mplew.getPacket();
    }

    public static final byte[] QuestMsg(String msg, int type, int time, int type2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.QUEST_MSG.getValue());
        mplew.writeMapleAsciiString(msg);
        mplew.writeInt(type);
        mplew.writeInt(time);
        mplew.write(1);
        mplew.writeInt(type2);
        return mplew.getPacket();
    }

    public static byte[] getMapleCabinetList(List<MapleCabinet> mc, boolean give, int get, boolean show) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MAPLE_CABINET.getValue());
        if (show) {
            mplew.writeInt(11);
            mplew.writeInt(0);
        } else if (give) {
            mplew.writeInt(12);
            mplew.writeInt(get);
        } else {
            mplew.writeInt(9);
            mplew.writeInt(mc.size());
            if (mc.isEmpty()) {
                mplew.writeZeroBytes(100);
            }
            int i = mc.size();
            for (MapleCabinet cb : mc) {
                mplew.write(1);
                mplew.writeInt(i);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeLong(cb.getSaveTime());
                mplew.writeMapleAsciiString(cb.getBigname());
                mplew.writeMapleAsciiString(cb.getSmallname());
                Item item = new Item(cb.getItemid(), (short) 0, (short) cb.getCount(), 0, -1L);
                mplew.write(2);
                PacketHelper.addItemInfo(mplew, item);
                i--;
            }
        }
        return mplew.getPacket();
    }

    public static byte[] getFieldSkillEffectAdd(int skillid, int skilllv, int mobid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FIELD_SKILL.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(skilllv);
        mplew.writeInt(mobid);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] getFieldSkillDuskAdd(int skillid, int skilllv, Point pos, boolean right, boolean sp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FIELD_SKILL.getValue());
        if (sp) {
            mplew.writeInt(skillid);
            mplew.writeInt(skilllv);
            int ran = Randomizer.rand(0, 1);
            if (ran == 0) {
                mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 78 5A 00 00 01 8C 55 00 00 94 02 00 00 B4 FF FF FF B4 FF FF FF BC 07 00 00 03 00 00 00 00 00 00 00 02 00 61 31 00 00 77 F7 FF FF 2C 01 00 00 9C FF FF FF 64 00 00 00 16 03 00 00 61 FF FF FF 00 01 B0 4F 00 00 C8 0A 00 00 51 00 00 00 51 00 00 00 BC 07 00 00 04 00 00 00 00 00 00 00 02 00 61 32 00 00 77 F7 FF FF 2C 01 00 00 9C FF FF FF 64 00 00 00 0C FE FF FF 61 FF FF FF 00 00 00"));
            } else {
                mplew.write(HexTool.getByteArrayFromHexString("03 00 00 00 78 5A 00 00 01 B0 4F 00 00 C8 0A 00 00 4B 00 00 00 4B 00 00 00 BC 07 00 00 05 00 00 00 03 00 00 00 02 00 62 31 00 00 77 F7 FF FF 2C 01 00 00 9C FF FF FF 64 00 00 00 9E FD FF FF 61 FF FF FF 00 01 8C 55 00 00 94 02 00 00 AF FF FF FF AF FF FF FF BC 07 00 00 06 00 00 00 03 00 00 00 02 00 62 32 00 00 77 F7 FF FF 2C 01 00 00 9C FF FF FF 64 00 00 00 58 02 00 00 61 FF FF FF 00 00 00"));
            }
        } else {
            mplew.writeInt(skillid);
            mplew.writeInt(skilllv);
            mplew.writeInt(0);
            mplew.writeInt(1501);
            mplew.write(1);
            mplew.writeInt(1500);
            mplew.writeInt(1);
            mplew.writeInt(35);
            mplew.writeInt(75);
            mplew.writeInt(1020);
            mplew.writeInt(6);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(-2185);
            mplew.writeInt(300);
            mplew.writeInt(-120);
            mplew.writeInt(120);
            mplew.writeInt(pos.x);
            mplew.writeInt(pos.y);
            mplew.write(right);
            mplew.write(0);
        }
        return mplew.getPacket();
    }

    public static byte[] getDestoryedBackImg(String info1, String info2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DESTORY_BACK_IMG.getValue());
        mplew.writeMapleAsciiString(info1);
        mplew.writeMapleAsciiString(info2);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] getFieldSkillAdd(int skillid, int skilllv, boolean remove) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(remove ? SendPacketOpcode.FIELD_SKILL_REMOVE.getValue() : SendPacketOpcode.FIELD_SKILL.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(skilllv);
        return mplew.getPacket();
    }

    public static byte[] getFieldSkillEffectAdd(int skillid, int skilllv, List<Point> startPoint) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FIELD_SKILL.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(skilllv);
        mplew.writeInt(startPoint.size());
        for (Point sp : startPoint) {
            mplew.writePosInt(sp);
        }
        return mplew.getPacket();
    }

    public static byte[] getFieldFootHoldAdd(int skillid, int skilllv, List<Triple<Point, String, Integer>> info, boolean remove) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(remove ? SendPacketOpcode.FIELD_SKILL_REMOVE.getValue() : SendPacketOpcode.FIELD_SKILL.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(skilllv);
        mplew.writeInt(info.size());
        for (Triple<Point, String, Integer> sinfo : info) {
            mplew.writePosInt(sinfo.getLeft());
            mplew.writeMapleAsciiString(sinfo.getMid());
            mplew.writeInt(((Integer) sinfo.getRight()).intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] getFieldLaserAdd(int skillid, int skilllv, List<Triple<Point, Integer, Integer>> info) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FIELD_SKILL.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(skilllv);
        mplew.writeInt(info.size());
        for (Triple<Point, Integer, Integer> sinfo : info) {
            mplew.writePosInt(sinfo.getLeft());
            mplew.writeInt(((Integer) sinfo.getMid()).intValue());
            mplew.writeInt(((Integer) sinfo.getRight()).intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] getFieldFinalLaserAdd(int skillid, int skilllv, List<Triple<Point, Point, Integer>> info, int delay) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.FIELD_SKILL.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(skilllv);
        mplew.writeInt(info.size());
        mplew.writeInt((skillid == 100016) ? 1400 : 2700);
        mplew.write(1);
        for (Triple<Point, Point, Integer> sinfo : info) {
            mplew.writePosInt(sinfo.getLeft());
            mplew.writePosInt(sinfo.getMid());
            mplew.writeInt(((Integer) sinfo.getRight()).intValue());
            mplew.writeInt(delay);
        }
        return mplew.getPacket();
    }

    public static byte[] setMapOBJ(String str, int unk, int unk2, int unk3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHANNEL_BACK_IMG.getValue());
        mplew.write(1);
        mplew.writeMapleAsciiString(str);
        mplew.writeInt(unk);
        mplew.writeInt(unk2);
        mplew.write(unk3);
        return mplew.getPacket();
    }

    public static byte[] setSpecialMapEffect(String str, int unk, int unk2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPECIAL_MAP_EFFECT_SET.getValue());
        mplew.writeMapleAsciiString(str);
        mplew.writeInt(unk);
        mplew.writeInt(unk2);
        return mplew.getPacket();
    }

    public static byte[] ChangeSpecialMapEffect(List<Pair<String, Integer>> str) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPECIAL_MAP_EFFECT_CHANGE.getValue());
        mplew.writeInt(str.size());
        for (Pair<String, Integer> eff : str) {
            mplew.writeMapleAsciiString(eff.getLeft());
            mplew.write(((Integer) eff.getRight()).intValue());
        }
        return mplew.getPacket();
    }

    public static byte[] getNowClock(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(type);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis() + 120000L));
        return mplew.getPacket();
    }

    public static byte[] SetForceAtomTarget(int skillid, int unk, int size, int objid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SET_FORCE_ATOM_TARGET.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(unk);
        mplew.writeInt(size);
        mplew.writeInt(objid);
        return mplew.getPacket();
    }

    public static byte[] getChatEmoticon(byte type, short slot, short slot2, int emoticon, String a) {
        int size;
        Iterator<Integer> iterator;
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHAT_EMOTICON.getValue());
        mplew.write(type);
        switch (type) {
            case 0:
                size = ((List) ChatEmoticon.getEmoticons().get(Integer.valueOf(emoticon))).size();
                mplew.writeShort(slot);
                mplew.writeInt(emoticon);
                mplew.writeInt(size);
                for (iterator = ((List) ChatEmoticon.getEmoticons().get(Integer.valueOf(emoticon))).iterator(); iterator.hasNext();) {
                    int em = ((Integer) iterator.next()).intValue();
                    mplew.writeInt(em);
                    mplew.writeLong(PacketHelper.getTime(-2L));
                    mplew.writeShort(0);
                }
                break;
            case 1:
            case 7:
            case 9:
                mplew.writeShort(slot);
                mplew.writeShort(slot2);
                break;
            case 2:
            case 3:
                mplew.writeShort(slot);
                break;
            case 4:
                mplew.writeShort(slot);
                mplew.writeZeroBytes(14);
                break;
            case 5:
                mplew.writeInt(emoticon);
                mplew.writeShort(slot);
                break;
            case 6:
                mplew.writeInt(emoticon);
                break;
            case 8:
                mplew.writeShort(slot);
                mplew.writeInt(emoticon);
                mplew.writeAsciiString(a, 25);
                break;
            case 10:
                mplew.writeShort(slot);
                break;
            case 11:
                mplew.writeInt(emoticon);
                mplew.writeMapleAsciiString(a);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] thunderAttack(int x, int y, int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.THUNDER_ATTACK.getValue());
        mplew.writeInt(80001762);
        mplew.writeInt(1);
        mplew.writeInt(x);
        mplew.writeInt(y);
        mplew.writeInt(oid);
        return mplew.getPacket();
    }

    public static byte[] FeverMessage(byte mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.EQUIPMENT_ENCHANT_MESSAGE.getValue());
        mplew.writeInt(mode);
        return mplew.getPacket();
    }

    public static byte[] SpPortal(int mode, String path) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SP_PORTAL.getValue());
        mplew.writeInt(0);
        mplew.writeInt(mode);
        mplew.writeMapleAsciiString(path);
        return mplew.getPacket();
    }

    public static byte[] DojangRank(ResultSet ranks, ResultSet rank, int count) throws SQLException {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DOJANG_RANK.getValue());
        mplew.write(HexTool.getByteArrayFromHexString("00 CE 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 65 00 00 00 00 00 00 00 00 00 00 00 65 00 00 00 01 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 65 00 00 00 00 00 00 00 00 00 00 00 65 00 00 00 03 00 00 00 00 "));
        mplew.writeInt(count);
        int i;
        for (i = 1; i <= count; i++) {
            mplew.writeInt(ranks.getInt("job"));
            mplew.writeInt(ranks.getInt("level"));
            String timerecode = ranks.getInt("floor") + "" + ranks.getInt("time");
            mplew.writeInt(Integer.parseInt(timerecode));
            mplew.writeInt(i);
            mplew.writeMapleAsciiString(ranks.getString("name"));
            boolean avater = (i <= 3);
            mplew.write(avater);
            if (avater) {
                AvatarLook a = AvatarLook.init(ranks);
                a.encodeUnpackAvatarLook(mplew);
            }
            ranks.next();
        }
        mplew.writeInt(1);
        mplew.write(0);
        mplew.write(2);
        mplew.writeInt(count);
        for (i = 1; i <= count; i++) {
            mplew.writeInt(rank.getInt("job"));
            mplew.writeInt(rank.getInt("level"));
            String timerecode = rank.getInt("floor") + "" + rank.getInt("time");
            mplew.writeInt(Integer.parseInt(timerecode));
            mplew.writeInt(i);
            mplew.writeMapleAsciiString(rank.getString("name"));
            boolean avater = (i <= 3);
            mplew.write(avater);
            if (avater) {
                AvatarLook a = AvatarLook.init(rank);
                a.encodeUnpackAvatarLook(mplew);
            }
            rank.next();
        }
        return mplew.getPacket();
    }

    public static byte[] craftMake(int cid, int something, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CRAFT_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(something);
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] craftFinished(int cid, int craftID, int ranking, int itemId, int quantity, int exp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CRAFT_COMPLETE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(0);
        mplew.writeInt(craftID);
        mplew.writeInt(ranking);
        mplew.write(1);
        if (ranking == 25 || ranking == 21 || ranking == 26 || ranking == 27) {
            mplew.writeInt(itemId);
            mplew.writeInt(quantity);
        }
        mplew.writeInt(exp);
        return mplew.getPacket();
    }

    public static byte[] craftFinished2(int cid, int craftID, int ranking, List<Pair<Integer, Short>> itemlist) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CRAFT_COMPLETE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(1);
        mplew.writeInt(ranking);
        mplew.write(1);
        mplew.writeInt(itemlist.size());
        for (int i = 0; i < itemlist.size(); i++) {
            mplew.writeInt(craftID);
            mplew.writeInt(((Integer) ((Pair) itemlist.get(i)).left).intValue());
            mplew.writeInt(((Short) ((Pair) itemlist.get(i)).right).shortValue());
            mplew.writeLong(0L);
        }
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] ItemMakerCooldown(int id, int cool) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ITEMMAKER_COOLDOWN.getValue());
        mplew.writeInt(id);
        mplew.writeInt(cool);
        return mplew.getPacket();
    }

    public static byte[] PangPangReactionReady(int skillid, int delay) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PANGPANG_REACTION_READY.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(delay);
        return mplew.getPacket();
    }

    public static byte[] PangPangReactionAct(int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PANGPANG_REACTION_ACT.getValue());
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] PangPangReactionEnd(int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PANGPANG_REACTION_END.getValue());
        mplew.writeInt(time);
        return mplew.getPacket();
    }

    public static byte[] onUserTeleport(MapleCharacter chr, int x, int y) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.USER_TELEPORT.getValue());
        packet.writeInt(chr.getId());
        packet.writeInt(x);
        packet.writeInt(y);
        return packet.getPacket();
    }

    public static byte[] onUserTeleport(int cid, int x, int y) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.TELEPORT_PLAYER.getValue());
        packet.writeInt(cid);
        packet.writeInt(x);
        packet.writeInt(y);
        return packet.getPacket();
    }

    public static byte[] MonkeyTogether(int type, boolean on) {
        MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.MONKEY_TOGETHER.getValue());
        packet.write(type);
        packet.write(1);
        packet.writeShort(0);
        packet.write(on);
        return packet.getPacket();
    }
}
