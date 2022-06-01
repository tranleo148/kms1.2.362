package handling.cashshop.handler;

import client.AvatarLook;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleQuestStatus;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import server.CashItemFactory;
import server.CashItemInfo;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.quest.MapleQuest;
import tools.CurrentTime;
import tools.FileoutputUtil;
import tools.StringUtil;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CSPacket;
import tools.packet.CWvsContext;

public class CashShopOperation {

    public static final int R = 3;

    public static void LeaveCS(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        /*  36 */ CashShopServer.getPlayerStorage().deregisterPlayer(chr);
        /*  37 */ c.updateLoginState(1, c.getSessionIPAddress());

        try {
            /*  40 */ PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
            /*  41 */ PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
            /*  42 */ World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
            /*  43 */ c.getSession().writeAndFlush(CField.getChannelChange(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
        } finally {
            /*  45 */ FileoutputUtil.log(FileoutputUtil.캐시샵퇴장로그, "[퇴장] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  캐시샵 퇴장");
            /*  46 */ String s = c.getSessionIPAddress();
            /*  47 */ LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
            /*  48 */ chr.saveToDB(true, true);
            /*  49 */ c.setPlayer(null);
            /*  50 */ c.setReceiving(false);
            /*  51 */ c.setCashShop(false);
        }
    }

    public static void EnterCS(int playerid, MapleClient c) {
        /*  57 */ CharacterTransfer transfer = CashShopServer.getPlayerStorage().getPendingCharacter(playerid);
        /*  58 */ MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);

        /*  60 */ c.setPlayer(chr);
        /*  61 */ c.setAccID(chr.getAccountID());

        /*  63 */ if (!c.CheckIPAddress()) {
            /*  64 */ c.getSession().close();

            return;
        }
        /*  68 */ c.loadKeyValues();
        /*  69 */ c.loadCustomDatas();
        /*  70 */ updateCharge(c);
        /*  71 */ c.setCashShop(true);

        /*  73 */ chr.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(chr.getId()));

        /*  75 */ World.isCharacterListConnected(c.getPlayer().getName(), c.loadCharacterNames(c.getWorld()));
        /*  76 */ c.updateLoginState(2, c.getSessionIPAddress());
        /*  77 */ CashShopServer.getPlayerStorage().registerPlayer(chr);
        /*  78 */ c.getSession().writeAndFlush(CSPacket.warpCS(c));
        /*  79 */ FileoutputUtil.log(FileoutputUtil.캐시샵입장로그, "[입장] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  캐시샵 입장");
        int i;
        /*  80 */ for (i = 120000000; i < 120000100; i++) {

            /*  82 */ if (c.getKeyValue(i + "") != null) {
                /*  83 */ int value = Integer.parseInt(c.getKeyValue(i + ""));
                /*  84 */ c.getSession().writeAndFlush(CSPacket.showCount(i, value));
            } else {
                /*  86 */ c.getSession().writeAndFlush(CSPacket.showCount(i, 0));
            }
        }
        /*  89 */ for (i = 120100000; i < 120100100; i++) {

            /*  91 */ if (c.getKeyValue(i + "") != null) {
                /*  92 */ int value = Integer.parseInt(c.getKeyValue(i + ""));
                /*  93 */ c.getSession().writeAndFlush(CSPacket.showCount(i, value));
            } else {
                /*  95 */ c.getSession().writeAndFlush(CSPacket.showCount(i, 0));
            }
        }
        /*  98 */ c.getSession().writeAndFlush(CSPacket.getCSInventory(c));

        /* 100 */ c.getSession().writeAndFlush(CSPacket.coodinationResult(0, 0, chr));
        /* 101 */ doCSPackets(c);
    }

    public static void updateCharge(MapleClient c) {
        /* 105 */ String lastEnter = c.getCustomData(6, "enter");
        /* 106 */ String date = String.valueOf(CurrentTime.getYear()) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2);

        /* 108 */ if (lastEnter == null || !lastEnter.equals(date)) {
            /* 109 */ c.setCustomData(6, "enter", date);

            /* 111 */ if (lastEnter != null) {
                /* 112 */ int grade = c.getMVPGrade();
                /* 113 */ c.setCustomData(6, "sp_" + grade, date);
            }
        }
    }

    public static void csCharge(MapleClient c) {
        /* 119 */ c.getSession().writeAndFlush(CField.popupHomePage());
    }

    public static void CSUpdate(MapleClient c) {
        /* 123 */ c.getSession().writeAndFlush(CSPacket.getCSGifts(c));
        int i;
        /* 124 */ for (i = 120000000; i < 120000100; i++) {

            /* 126 */ if (c.getKeyValue(i + "") != null) {
                /* 127 */ int value = Integer.parseInt(c.getKeyValue(i + ""));
                /* 128 */ c.getSession().writeAndFlush(CSPacket.showCount(i, value));
            }
        }

        /* 132 */ for (i = 120100000; i < 120100100; i++) {

            /* 134 */ if (c.getKeyValue(i + "") != null) {
                /* 135 */ int value = Integer.parseInt(c.getKeyValue(i + ""));
                /* 136 */ c.getSession().writeAndFlush(CSPacket.showCount(i, value));
            }
        }
        /* 139 */ doCSPackets(c);
        /* 140 */ c.getSession().writeAndFlush(CSPacket.sendWishList(c.getPlayer(), false));
    }

    public static void updateCharge(MapleClient c, int value) {
        /* 144 */ String date = String.valueOf(CurrentTime.getYear()) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2);
        /* 145 */ int grade = c.getMVPGrade();
        /* 146 */ String charge = c.getCustomData(4, date);
        /* 147 */ int add = (charge == null) ? 0 : Integer.parseInt(charge);
        /* 148 */ c.setCustomData(4, date, String.valueOf(add + value));

        /* 150 */ if (grade != c.getMVPGrade()) {
            /* 151 */ c.setCustomData(6, "sp_" + c.getMVPGrade(), date);
        }
    }

    public static void mvpSpecialPack(int grade, MapleClient c) {
        /* 156 */ int mvpGrade = c.getMVPGrade();
        /* 157 */ if (grade <= mvpGrade) {
            /* 158 */ String date = String.valueOf(CurrentTime.getYear()) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2);

            /* 160 */ c.setCustomData(6, "sp_" + grade, "R" + date);
        } else {
            /* 162 */ c.getPlayer().dropMessage(1, "스페셜팩 수령이 불가능합니다.");
        }
    }

    public static void mvpGiftPack(MapleClient c) {
        /* 167 */ String data = c.getCustomData(6, "gp");

        /* 169 */ System.out.println("MVP 등급 : " + c.getMVPGrade());
        /* 170 */ System.out.println("최근 3달 충전 내역 : " + c.chargePoint());
        /* 171 */ System.out.println("총 충전 내역 : " + c.allChargePoint());

        /* 174 */ String date = String.valueOf(CurrentTime.getYear()) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getDate()), '0', 2) + StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getHour()), '0', 2);
        /* 175 */ if (data == null || (data != null && !data.contains(date))) {
            /* 176 */ c.setCustomData(6, "gp", date);
        } else {
            /* 178 */ c.getPlayer().dropMessage(1, "기프트팩 수령이 불가능합니다.");
        }

        /* 181 */ System.out.println("String : " + date);
    }

    public static void coodinationResult(LittleEndianAccessor slea, MapleClient c) throws IOException {
        /* 186 */ int type = slea.readInt();

        /* 188 */ MapleCharacter chr = c.getPlayer();

        /* 190 */ if (chr == null) {
            /* 191 */ c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 1, chr));

            return;
        }
        /* 195 */ AvatarLook a = new AvatarLook();

        /* 197 */ if (type == 1) {
            /* 198 */ AvatarLook.decodeAvatarLook(a, slea);

            /* 200 */ for (AvatarLook aL : chr.getCoodination()) {
                /* 201 */ if (aL != null && aL.compare(a)) {
                    /* 202 */ c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 3, chr));

                    return;
                }
            }
            /* 207 */ if (chr.getCoodination().size() < 6) {
                /* 208 */ chr.getCoodination().add(a);
                /* 209 */ c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 0, chr));
            } else {
                /* 211 */ c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 4, chr));
            }
            /* 213 */        } else if (type == 2) {

            /* 216 */ AvatarLook.decodeUnpackAvatarLook(a, slea);

            /* 218 */ AvatarLook target = null;

            /* 220 */ for (AvatarLook aL : chr.getCoodination()) {
                /* 221 */ if (aL != null && aL.compare(a)) {
                    /* 222 */ target = aL;

                    break;
                }
            }
            /* 227 */ if (target != null) {
                /* 228 */ chr.getCoodination().remove(target);
                /* 229 */ c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 0, chr));
            } else {
                /* 231 */ c.getSession().writeAndFlush(CSPacket.coodinationResult(type, 6, chr));
            }
        }
    }

    public static void csGift(LittleEndianAccessor slea, MapleClient c) {
        /* 237 */ c.getSession().writeAndFlush(CSPacket.resultGiftItem(true, "", 0));
    }

    public static void CouponCode(String code, MapleClient c) {
        /* 259 */ if (code.length() <= 0) {
            return;
        }
        /* 262 */ Triple<Boolean, Integer, Integer> info = null;
        try {
            /* 264 */ info = MapleCharacterUtil.getNXCodeInfo(code);
            /* 265 */        } catch (SQLException e) {
            /* 266 */ e.printStackTrace();
        }

        /* 269 */ if (info != null && ((Boolean) info.left).booleanValue()) {
            /* 270 */ CashItemInfo itez;
            byte slot;
            int type = ((Integer) info.mid).intValue(), item = ((Integer) info.right).intValue();
            try {
                /* 272 */ MapleCharacterUtil.setNXCodeUsed(c.getPlayer().getName(), code);
                /* 273 */            } catch (SQLException e) {
                /* 274 */ e.printStackTrace();
            }

            /* 286 */ Map<Integer, Item> itemz = new HashMap<>();
            /* 287 */ int maplePoints = 0, mesos = 0;
            /* 288 */ switch (type) {
                case 1:
                case 2:
                    /* 291 */ c.getPlayer().modifyCSPoints(type, item, false);
                    /* 292 */ maplePoints = item;
                    break;
                case 3:
                    /* 295 */ itez = CashItemFactory.getInstance().getItem(item);
                    /* 296 */ if (itez == null) {
                        /* 297 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                        return;
                    }
                    /* 300 */ slot = MapleInventoryManipulator.addId(c, itez.getId(), (short) 1, "", "Cash shop: coupon code on " + FileoutputUtil.CurrentReadable_Date());
                    /* 301 */ if (slot <= -1) {
                        /* 302 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                        return;
                    }
                    /* 305 */ itemz.put(Integer.valueOf(item), c.getPlayer().getInventory(GameConstants.getInventoryType(item)).getItem((short) slot));
                    break;

                case 4:
                    /* 309 */ c.getPlayer().gainMeso(item, false);
                    /* 310 */ mesos = item;
                    break;
            }
            /* 313 */ c.getSession().writeAndFlush(CSPacket.showCouponRedeemedItem(itemz, mesos, maplePoints, c));
        } else {
            /* 315 */ c.getSession().writeAndFlush(CSPacket.sendCSFail((info == null) ? 167 : 165));
        }
        /* 317 */ doCSPackets(c);
    }

    public static final void BuyCashItem(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        boolean useMaplePoint;
        int wishlist[], j;
        Item item1;
        short slot;
        String pwd;
        int toCharge;
        CashItemInfo item;
        boolean useMileage;
        int i;
        boolean coupon;
        CashItemInfo cashItemInfo1;
        int sn;
        long uniqueId;
        int id;
        boolean bool1, useAllMileage;
        MapleInventoryType type;
        int slots;
        CashItemInfo cashItemInfo2, cashItemInfo3;
        MapleQuestStatus marr;
        int itemId;
        List<Integer> ccc;
        int k;
        byte b;
        int quantity;
        boolean bool;
        MapleInventory inv;
        Map<Integer, Item> ccz;
        Item item2;
        /* 321 */ int a, action = slea.readByte();
        /* 322 */ switch (action) {
            case 0:
                /* 324 */ slea.skip(2);
                /* 325 */ CouponCode(slea.readMapleAsciiString(), c);
                break;

            case 3: 
                useMaplePoint = slea.readByte() == 1;
                useMileage = slea.readByte() == 1;
                useAllMileage = slea.readByte() == 1;
                item = CashItemFactory.getInstance().getItem(slea.readInt());
                quantity = slea.readInt();
                if (useMaplePoint) {
                    toCharge = 2;
                }
                else if (useAllMileage) {
                    toCharge = 4;
                }
                else {
                    toCharge = 1;
                }
                if ((item.getSN() >= 130400000 && item.getSN() <= 130599999) || (item.getSN() >= 130000000 && item.getSN() <= 130000500) || (item.getSN() >= 130002000 && item.getSN() <= 130002500)) {
                    toCharge = 2;
                    if (!useMaplePoint) {
            c.send(CWvsContext.serverNotice(1, "", "홍보 아이템은 홍보 포인트로만 구입이 가능합니다."));
            doCSPackets(c);
                        return;
                    }
                }
                else if (item.getPrice() > 0) {
                    if (useMaplePoint) {
           c.send(CWvsContext.serverNotice(1, "", "후원 아이템은 후원 포인트로만 구입이 가능합니다."));
                       doCSPackets(c);
                        return;
                    }
                    toCharge = 1;
                }
                if (item.getLimitMax() > 0) {
                    if (c.getKeyValue(item.getSN() + "") != null && item.getLimitMax() <= Integer.parseInt(c.getKeyValue(item.getSN() + ""))) {
                        return;
                    }
                    final int value = (c.getKeyValue(item.getSN() + "") != null) ? (Integer.parseInt(c.getKeyValue(item.getSN() + "")) + 1) : 1;
                    c.setKeyValue(item.getSN() + "", value + "");
                    c.getSession().writeAndFlush((Object)CSPacket.showCount(item.getSN(), value));
                }
                if (item == null) {
                    c.getSession().writeAndFlush((Object)CSPacket.sendCSFail(0));
                    break;
                }
                if (!item.genderEquals(c.getPlayer().getGender())) {
                    c.getSession().writeAndFlush((Object)CSPacket.sendCSFail(166));
                    doCSPackets(c);
                    return;
                }
                if (c.getPlayer().getCashInventory().getItemsSize() >= 500) {
                    c.getSession().writeAndFlush((Object)CSPacket.sendCSFail(177));
                    doCSPackets(c);
                    return;
                }
                if (Arrays.asList(GameConstants.cashBlock).contains(item.getId())) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                    doCSPackets(c);
                    return;
                }
                final int paybackRate = useAllMileage ? 0 : 30;
                final int discountRate = useMileage ? 30 : 0;
                int price = item.getPrice();
                if (discountRate > 0) {
                    price = price * (100 - discountRate) / 100;
                }
                chr.modifyCSPoints(toCharge, -price, false);
                if (useMileage) {
                    chr.modifyCSPoints(4, -(price * discountRate / 100), false);
                }
                if (toCharge == 1) {
                    updateCharge(c, price);
                }
                final Item itemz = chr.getCashInventory().toItem(item);
                if (itemz != null && itemz.getUniqueId() > 0L && itemz.getItemId() == item.getId()) {
                    if (toCharge == 1) {
                        itemz.setFlag(itemz.getFlag() | ((itemz.getType() == 1) ? ItemFlag.KARMA_EQUIP.getValue() : ItemFlag.KARMA_USE.getValue()));
                    }
                    itemz.setPosition((short)(chr.getCashInventory().getItemsSize() + 1));
                    chr.getCashInventory().addToInventory(itemz);
                    final String amount = "amount=900;";
                    final String given = "given=-1;";
                    final String per = "per=9";
                    c.getSession().writeAndFlush((Object)CSPacket.showBoughtCSItem(itemz, c, item.getSN(), paybackRate, discountRate));
                    save(c);
              FileoutputUtil.log(FileoutputUtil.캐시샵구매로그, "[아이템 구입] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  | 캐시샵에서 " + MapleItemInformationProvider.getInstance().getName(itemz.getItemId()) + "(" + itemz.getItemId() + ")를 [" + itemz.getQuantity() + "]개 구입");
                     break;
                }
                c.getSession().writeAndFlush((Object)CSPacket.sendCSFail(0));
                break;

            case 5:
                /* 442 */ chr.clearWishlist();
                /* 443 */ slea.skip(1);
                /* 444 */ if (slea.available() < 48L) {
                    /* 445 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                    /* 446 */ doCSPackets(c);
                    return;
                }
                /* 449 */ wishlist = new int[12];
                /* 450 */ for (i = 0; i < 12; i++) {
                    /* 451 */ wishlist[i] = slea.readInt();
                }
                /* 453 */ chr.setWishlist(wishlist);
                /* 454 */ c.getSession().writeAndFlush(CSPacket.sendWishList(chr, true));
                break;

            case 6:
                /* 458 */ slea.skip(1);
                /* 459 */ j = 1;
                /* 460 */ coupon = (slea.readByte() > 0);
                /* 461 */ if (coupon) {
                    /* 462 */ MapleInventoryType mapleInventoryType = getInventoryType(slea.readInt());
                    /* 463 */ if (chr.getCSPoints(1) >= 12000 && chr.getInventory(mapleInventoryType).getSlotLimit() < 89) {
                        /* 464 */ chr.modifyCSPoints(1, -12000, false);
                        /* 465 */ chr.getInventory(mapleInventoryType).addSlot((short) 8);
                        /* 466 */ chr.dropMessage(1, "인벤토리 공간을 늘렸습니다. 현재 " + chr.getInventory(mapleInventoryType).getSlotLimit() + " 슬롯이 되었습니다.\r\n\r\n캐시샵에서 늘려진 슬롯이 바로 보이지 않아도 실제로는 늘려졌으니, 캐시샵에서 나가시면 정상적으로 슬롯이 늘어난걸 볼 수 있습니다.");
                        break;
                    }
                    /* 468 */ chr.dropMessage(1, "슬롯을 더 이상 늘릴 수 없습니다.");
                    break;
                }
                /* 471 */ type = MapleInventoryType.getByType(slea.readByte());
                /* 472 */ if (chr.getCSPoints(1) >= 8000 && chr.getInventory(type).getSlotLimit() < 93) {
                    /* 473 */ chr.modifyCSPoints(1, -8000, false);
                    /* 474 */ chr.getInventory(type).addSlot((short) 4);
                    /* 475 */ chr.dropMessage(1, "인벤토리 공간을 늘렸습니다. 현재 " + chr.getInventory(type).getSlotLimit() + " 슬롯이 되었습니다.\r\n\r\n캐시샵에서 늘려진 슬롯이 바로 보이지 않아도 실제로는 늘려졌으니, 캐시샵에서 나가시면 정상적으로 슬롯이 늘어난걸 볼 수 있습니다.");
                    break;
                }
                /* 477 */ chr.dropMessage(1, "슬롯을 더 이상 늘릴 수 없습니다.");
                break;

            case 7:
                /* 483 */ if (chr.getCSPoints(1) >= 8000 && chr.getStorage().getSlots() < 48) {
                    /* 484 */ chr.modifyCSPoints(1, -8000, false);
                    /* 485 */ chr.getStorage().increaseSlots((byte) 4);
                    /* 486 */ chr.dropMessage(1, "창고슬롯을 늘렸습니다. 현재 창고 슬롯은 " + chr.getStorage().getSlots() + "칸 입니다.");
                    break;
                }
                /* 488 */ chr.dropMessage(1, "슬롯을 더 이상 늘릴 수 없습니다.");
                break;

            case 8:
                /* 493 */ slea.skip(1);
                /* 494 */ j = 1;
                /* 495 */ cashItemInfo1 = CashItemFactory.getInstance().getItem(slea.readInt());
                /* 496 */ slots = c.getCharacterSlots();
                /* 497 */ if (cashItemInfo1 == null || c.getPlayer().getCSPoints(1) < cashItemInfo1.getPrice() || slots > 15 || cashItemInfo1.getId() != 5430000) {
                    /* 498 */ doCSPackets(c);
                    return;
                }
                /* 501 */ if (c.gainCharacterSlot()) {
                    /* 502 */ c.getPlayer().modifyCSPoints(1, -cashItemInfo1.getPrice(), false);
                    /* 503 */ c.getSession().writeAndFlush(CSPacket.buyCharacterSlot());
                    break;
                }
                /* 505 */ chr.dropMessage(1, "슬롯을 더 이상 늘릴 수 없습니다.");
                break;

            case 9:
                /* 510 */ j = slea.readByte() + 1;
                /* 511 */ sn = slea.readInt();
                /* 512 */ cashItemInfo2 = CashItemFactory.getInstance().getItem(sn);
                /* 513 */ if (cashItemInfo2 == null || c.getPlayer().getCSPoints(j) < cashItemInfo2.getPrice() || cashItemInfo2.getId() / 10000 != 555) {
                    /* 514 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                    /* 515 */ doCSPackets(c);
                    return;
                }
                /* 518 */ marr = c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122700));

                /* 520 */ chr.dropMessage(1, "이미 펜던트 늘리기가 적용중입니다.");
                /* 521 */ doCSPackets(c);
                break;

            case 13:
                /* 536 */ item1 = chr.getCashInventory().findByCashId(slea.readLong(), slea.readInt(), slea.readByte());
                /* 537 */ if (item1 != null && item1.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item1.getItemId(), item1.getQuantity(), item1.getOwner())) {
                    /* 538 */ Item item_ = item1.copy();
                    /* 539 */ short pos = MapleInventoryManipulator.addbyItem(c, item_, false, true);
                    /* 540 */ if (pos >= 0) {
                        /* 541 */ if (item_.getPet() != null) {
                            /* 542 */ item_.getPet().setInventoryPosition(pos);
                        }
                        /* 544 */ chr.getCashInventory().removeFromInventory(item1);
                        /* 545 */ c.getSession().writeAndFlush(CSPacket.confirmFromCSInventory(item_, c, pos));
                        /* 546 */ save(c);
                        /* 547 */ FileoutputUtil.log(FileoutputUtil.캐시샵인벤로그, "[캐시템 꺼내기] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  | 캐시샵에서 " + MapleItemInformationProvider.getInstance().getName(item_.getItemId()) + "(" + item_.getItemId() + ")를 [" + item_.getQuantity() + "] 인벤으로 옮김");
                        break;
                    }
                    /* 550 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(177));
                    break;
                }
                /* 553 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(177));
                break;

            case 14:
                /* 558 */ slot = -1;
                /* 559 */ uniqueId = slea.readLong();
                /* 560 */ itemId = slea.readInt();
                /* 561 */ b = slea.readByte();
                /* 562 */ inv = chr.getInventory(b);
                /* 563 */ item2 = inv.findByUniqueId(uniqueId);
                /* 564 */ for (MapleInventory iv : c.getPlayer().getInventorys()) {
                    /* 565 */ item2 = iv.findByUniqueId(uniqueId);
                    /* 566 */ if (item2 != null) {
                        /* 567 */ slot = item2.getPosition();
                        /* 568 */ inv = iv;
                        break;
                    }
                }
                /* 572 */ if (GameConstants.isPet(item2.getItemId())) {
                    /* 573 */ for (int abc = 0; abc < (c.getPlayer()).pets.length; abc++) {
                        /* 574 */ if ((c.getPlayer()).pets[abc] != null
                                && /* 575 */ slot == (c.getPlayer()).pets[abc].getInventoryPosition()) {
                            /* 576 */ c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "장착 중인 펫은 캐시 보관함에 넣으실 수 없습니다."));
                            /* 577 */ c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                            /* 578 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(3));
                            /* 579 */ c.getSession().writeAndFlush(CSPacket.showNXMapleTokens(c.getPlayer()));
                            /* 580 */ c.getSession().writeAndFlush(CSPacket.enableCSUse());
                            /* 581 */ c.getPlayer().getCashInventory().checkExpire(c);

                            return;
                        }
                    }
                }

                /* 588 */ if (item2 != null && item2.getItemId() == itemId) {
                    /* 589 */ c.getPlayer().getCashInventory().addToInventory(item2);
                    /* 590 */ c.getSession().writeAndFlush(CSPacket.confirmToCSInventory(item2, c, -1));
                    /* 591 */ FileoutputUtil.log(FileoutputUtil.캐시샵인벤로그, "[캐시템 넣기] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  | 캐시샵에서 " + MapleItemInformationProvider.getInstance().getName(item2.getItemId()) + "(" + item2.getItemId() + ")를 [" + item2.getQuantity() + "] 캐시샵에 넣음");
                    /* 592 */ if (item2.getPet() != null) {
                        /* 593 */ c.getPlayer().removePet(item2.getPet(), false);
                    }
                    /* 595 */ save(c);
                    /* 596 */ inv.removeSlot(slot);
                    break;
                }
                /* 598 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(177));
                break;

            case 31:
                /* 603 */ slea.skip(5);
                /* 604 */ pwd = slea.readMapleAsciiString();
                /* 605 */ id = slea.readInt();
                break;

            case 34:
                /* 609 */ toCharge = slea.readByte() + 1;
                /* 610 */ bool1 = (toCharge == 2);
                /* 611 */ cashItemInfo2 = CashItemFactory.getInstance().getItem(slea.readInt());
                /* 612 */ ccc = null;
                /* 613 */ if (cashItemInfo2 != null) {
                    /* 614 */ ccc = CashItemFactory.getInstance().getPackageItems(cashItemInfo2.getId());
                }
                /* 616 */ if (cashItemInfo2 == null || ccc == null || ccc.isEmpty()) {
                    /* 617 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(3));
                    /* 618 */ doCSPackets(c);
                    return;
                }
                /* 621 */ if ((cashItemInfo2.getSN() >= 130400000 && cashItemInfo2.getSN() <= 130599999) || (cashItemInfo2.getSN() >= 130000000 && cashItemInfo2.getSN() <= 130000500) || (cashItemInfo2.getSN() >= 130002000 && cashItemInfo2.getSN() <= 130002500)) {

                    /* 623 */ toCharge = 2;
                    /* 624 */ if (!bool1) {
                        /* 625 */ c.send(CWvsContext.serverNotice(1, "", "홍보 아이템은 홍보 포인트로만 구입이 가능합니다."));
                        /* 626 */ doCSPackets(c);
                        return;
                    }
                    /* 629 */                } else if (cashItemInfo2.getPrice() > 0) {
                    /* 630 */ if (bool1) {
                        /* 631 */ c.send(CWvsContext.serverNotice(1, "", "후원 아이템은 후원 포인트로만 구입이 가능합니다."));
                        /* 632 */ doCSPackets(c);
                        return;
                    }
                    /* 635 */ toCharge = 1;
                }
                /* 637 */ if (cashItemInfo2.getLimitMax() > 0) {
                    /* 638 */ if (c.getKeyValue(cashItemInfo2.getSN() + "") != null
                            && /* 639 */ cashItemInfo2.getLimitMax() <= Integer.parseInt(c.getKeyValue(cashItemInfo2.getSN() + ""))) {
                        return;
                    }

                    /* 643 */ int value = (c.getKeyValue(cashItemInfo2.getSN() + "") != null) ? (Integer.parseInt(c.getKeyValue(cashItemInfo2.getSN() + "")) + 1) : 1;
                    /* 644 */ c.setKeyValue(cashItemInfo2.getSN() + "", value + "");
                    /* 645 */ c.getSession().writeAndFlush(CSPacket.showCount(cashItemInfo2.getSN(), value));
                }

                /* 648 */ quantity = slea.readInt() / ccc.size();

                /* 650 */ if (c.getPlayer().getCSPoints(toCharge) < cashItemInfo2.getPrice() * quantity) {
                    /* 651 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(3));
                    /* 652 */ doCSPackets(c);
                    return;
                }
                /* 654 */ if (!cashItemInfo2.genderEquals(c.getPlayer().getGender())) {
                    /* 655 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(11));
                    /* 656 */ doCSPackets(c);
                    return;
                }
                /* 658 */ if (c.getPlayer().getCashInventory().getItemsSize() >= 500 - ccc.size() * quantity) {
                    /* 659 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(24));
                    /* 660 */ doCSPackets(c);
                    return;
                }
                /* 663 */ if (Arrays.<Integer>asList(GameConstants.cashBlock).contains(Integer.valueOf(cashItemInfo2.getId()))) {
                    /* 664 */ c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(cashItemInfo2.getId()));
                    /* 665 */ doCSPackets(c);
                    return;
                }
                /* 668 */ ccz = new HashMap<Integer, Item>();
                /*
                for (a = 0; a < quantity; ++a) {
                    for (int i2 : ccc) {
                        Item itemz;
                        CashItemInfo cii = CashItemFactory.getInstance().getSimpleItem(i2);
                        if (cii == null || (itemz = c.getPlayer().getCashInventory().toItem(cii)) == null || itemz.getUniqueId() <= 0L) continue;
                        if (Arrays.asList(GameConstants.cashBlock).contains(item.getId())) {
                            c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                            CashShopOperation.doCSPackets(c);
                            continue;
                        }
                        ccz.put(i2, itemz);
                    }
                /*
                     for (Item itemsa : ccz.values()) {
                        if (toCharge == 1) {
                       itemsa.setFlag(itemsa.getFlag() | ((itemsa.getType() == 1) ? ItemFlag.KARMA_EQUIP.getValue() : ItemFlag.KARMA_USE.getValue()));
                        }
                       itemsa.setPosition((short) (chr.getCashInventory().getItemsSize() + 1));
                     FileoutputUtil.log(FileoutputUtil.캐시샵구매로그, "[패키지 구입] 계정 번호 : " + chr.getClient().getAccID() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ")  | 패키지 SN :" + cashItemInfo2.getSN() + " | 캐시샵에서 " + MapleItemInformationProvider.getInstance().getName(itemsa.getItemId()) + "(" + itemsa.getItemId() + ")를 [" + itemsa.getQuantity() + "]개 구입");
                    c.getPlayer().getCashInventory().addToInventory(itemsa);
                    }
                  c.getSession().writeAndFlush(CSPacket.showBoughtCSPackage(ccz, c));
                }
                /* 696 */ chr.modifyCSPoints(toCharge, -cashItemInfo2.getPrice() * quantity, false);
                /* 697 */ save(c);
                /* 698 */ if (toCharge == 1) {
                    /* 699 */ updateCharge(c, cashItemInfo2.getPrice() * quantity);
                }
                break;

            case 48:
                /* 705 */ item = CashItemFactory.getInstance().getItem(slea.readInt());
                /* 706 */ c.getSession().writeAndFlush(CSPacket.updatePurchaseRecord(item.getSN()));
                break;

            default:
                /* 710 */ System.out.println("New Action: " + action + " Remaining: " + slea.toString());
                /* 711 */ c.getSession().writeAndFlush(CSPacket.sendCSFail(0));
                break;
        }
        /* 714 */ doCSPackets(c);
    }

    private static final MapleInventoryType getInventoryType(int id) {
        /* 718 */ switch (id) {
            case 50200093:
                /* 720 */ return MapleInventoryType.EQUIP;
            case 50200094:
                /* 722 */ return MapleInventoryType.USE;
            case 50200197:
                /* 724 */ return MapleInventoryType.SETUP;
            case 50200095:
                /* 726 */ return MapleInventoryType.ETC;
        }
        /* 728 */ return MapleInventoryType.UNDEFINED;
    }

    public static final void doCSPackets(MapleClient c) {
        /* 733 */ c.getSession().writeAndFlush(CSPacket.showNXMapleTokens(c.getPlayer()));
        /* 734 */ c.getSession().writeAndFlush(CSPacket.enableCSUse());
        /* 735 */ c.getPlayer().getCashInventory().checkExpire(c);
    }

    public static void save(MapleClient c) {
        /* 739 */ Connection con = null;

        /* 741 */ try {
            con = DatabaseConnection.getConnection();
            /* 742 */ c.getPlayer().getCashInventory().save(con);
            /* 743 */ con.close();
        } /* 744 */ catch (Exception exception) {
            try {
                /* 748 */ if (con != null) {
                    /* 749 */ con.close();
                }
                /* 751 */            } catch (Exception exception1) {
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }

    }
}


/* Location:              C:\Users\Phellos\Desktop\크루엘라\Ozoh디컴.jar!\handling\cashshop\handler\CashShopOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
