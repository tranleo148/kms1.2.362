// 
// Decompiled by Procyon v0.5.36
// 

package handling.channel.handler;

import constants.ServerConstants;
import java.awt.Point;
import java.util.List;
import java.util.Iterator;
import client.inventory.Item;
import server.Randomizer;
import java.util.ArrayList;
import tools.packet.CField;
import server.games.OneCardGame;
import server.Timer;
import server.shops.MaplePlayerShopItem;
import server.MapleInventoryManipulator;
import client.inventory.ItemFlag;
import constants.GameConstants;
import server.MapleItemInformationProvider;
import tools.packet.SLFCGPacket;
import log.DBLogger;
import log.LogType;
import tools.FileoutputUtil;
import constants.programs.AdminTool;
import tools.packet.PlayerShopPacket;
import server.shops.MaplePlayerShop;
import server.marriage.MarriageMiniBox;
import client.inventory.MapleInventoryType;
import server.maps.MapleMapObject;
import server.shops.IMaplePlayerShop;
import server.shops.MapleMiniGame;
import server.maps.FieldLimitType;
import java.util.Arrays;
import server.maps.MapleMapObjectType;
import server.MapleTrade;
import tools.packet.CWvsContext;
import client.MapleCharacter;
import client.MapleClient;
import static handling.channel.handler.PlayerInteractionHandler.Interaction.values;
import tools.data.LittleEndianAccessor;

public class PlayerInteractionHandler
{
    public static final void PlayerInteraction(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final Interaction action = Interaction.getByAction(slea.readByte() & 0xFF);
        if (chr == null || action == null) {
            return;
        }
        c.getPlayer().setScrolledPosition((short)0);
        switch (action) {
            case CREATE: {
                if (chr.getPlayerShop() != null || c.getChannelServer().isShutdown()) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                final byte createType = slea.readByte();
                if (createType == 3 || createType == 4) {
                    MapleTrade.startTrade(chr, createType == 4);
                    break;
                }
                if (createType != 1 && createType != 2 && createType != 5 && createType != 6) {
                    break;
                }
                if (chr.getMap().getMapObjectsInRange(chr.getTruePosition(), 20000.0, Arrays.asList(MapleMapObjectType.SHOP, MapleMapObjectType.HIRED_MERCHANT)).size() != 0 || chr.getMap().getPortalsInRange(chr.getTruePosition(), 20000.0).size() != 0) {
            chr.dropMessage(1, "이곳에 상점을 세울 수 없습니다.");
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if ((createType == 1 || createType == 2) && (FieldLimitType.Minigames.check(chr.getMap().getFieldLimit()) || chr.getMap().allowPersonalShop())) {
                   chr.dropMessage(1, "이곳에 미니게임을 개설할 수 없습니다.");
                   c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                final String desc = slea.readMapleAsciiString();
                String pass = "";
                if (slea.readByte() > 0) {
                    pass = slea.readMapleAsciiString();
                }
                if (createType == 1 || createType == 2) {
                    final int piece = slea.readByte();
                    final int itemId = (createType == 1) ? (4080000 + piece) : 4080100;
                    if (!chr.haveItem(itemId) || (c.getPlayer().getMapId() >= 910000001 && c.getPlayer().getMapId() <= 910000022)) {
                        return;
                    }
                    final MapleMiniGame game = new MapleMiniGame(chr, itemId, desc, pass, createType);
                    game.setPieceType(piece);
                    chr.setPlayerShop(game);
                    game.setAvailable(true);
                    game.setOpen(true);
                    game.send(c);
                    chr.getMap().addMapObject(game);
                    game.update();
                }
                else if (chr.getMap().allowPersonalShop()) {
                    final Item shop = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(slea.readShort());
                    if (shop == null || shop.getQuantity() <= 0 || shop.getItemId() != slea.readInt() || c.getPlayer().getMapId() < 910000001 || c.getPlayer().getMapId() > 910000022) {
                        return;
                    }
                    if (createType == 4) {}
                }
                break;
            }
            case INVITE_TRADE: {
                if (chr.getMap() == null) {
                    return;
                }
                final int dd = slea.readInt();
                final MapleCharacter chrr = chr.getMap().getCharacterById(dd);
                if (chrr == null || c.getChannelServer().isShutdown()) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                MapleTrade.inviteTrade(chr, chrr, true);
                break;
            }
            case INVITE_ROCK_PAPER_SCISSORS: {
                if (chr.getMap() == null) {
                    return;
                }
                final MapleCharacter chrr2 = chr.getMap().getCharacterById(slea.readInt());
                if (chrr2 == null || c.getChannelServer().isShutdown()) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                MapleTrade.inviteTrade(chr, chrr2, false);
                break;
            }
            case DENY_TRADE: {
                if (chr.getMarriage() != null) {
                    chr.getMarriage().closeMarriageBox(true, 24);
                    chr.setMarriage(null);
                    break;
                }
                MapleTrade.declineTrade(chr);
                break;
            }
            case WEDDING_START: {
                c.getPlayer().getMarriage().StartMarriage();
                break;
            }
            case WEDDING_END: {
                c.getPlayer().getMarriage().EndMarriage();
                break;
            }
            case VISIT: {
                if (c.getChannelServer().isShutdown()) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if (chr.getTrade() == null && chr.getPlayerShop() != null) {
          chr.dropMessage(1, "이미 닫힌 방입니다.");
                    return;
                }
                if (chr.getTrade() != null && chr.getTrade().getPartner() != null && !chr.getTrade().inTrade()) {
                    MapleTrade.visitTrade(chr, chr.getTrade().getPartner().getChr(), chr.getTrade().getPartner().getChr().isTrade);
                    break;
                }
                if (chr.getMap() != null && chr.getTrade() == null) {
                    final int obid = slea.readInt();
                    if (obid == 0) {
                        if (chr.getMarriage() == null || chr.getMarriage().getPlayer1().getMarriage() == null) {
          chr.dropMessage(1, "이미 닫힌 방입니다.");
                            return;
                        }
                        if (chr.getMarriage() != null && chr.getMarriage().getPartnerId() == chr.getId()) {
                            chr.setPlayerShop(chr.getMarriage());
                            chr.getMarriage().setPlayer2(chr);
                            chr.getMarriage().setAvailable(true);
                            chr.getMarriage().addVisitor(chr);
                            chr.getMarriage().send(c);
                            chr.getMarriage().update();
                            return;
                        }
                    }
                    MapleMapObject ob = chr.getMap().getMapObject(obid, MapleMapObjectType.HIRED_MERCHANT);
                    if (ob == null) {
                        ob = chr.getMap().getMapObject(obid, MapleMapObjectType.SHOP);
                    }
                    if (ob instanceof IMaplePlayerShop && chr.getPlayerShop() == null) {
                        final IMaplePlayerShop ips = (IMaplePlayerShop)ob;
                        if (ips instanceof MaplePlayerShop && ((MaplePlayerShop)ips).isBanned(chr.getName())) {
              chr.dropMessage(1, "상점에서 강퇴당했습니다.");
                            return;
                        }
                        if (ips.getFreeSlot() < 0 || ips.getVisitorSlot(chr) > -1 || !ips.isOpen() || !ips.isAvailable()) {
                            c.getSession().writeAndFlush((Object)PlayerShopPacket.getMiniGameFull());
                        }
                        else {
                            if (slea.available() > 0L && slea.readByte() > 0) {
                                final String pass2 = slea.readMapleAsciiString();
                                if (!pass2.equals(ips.getPassword())) {
                                    c.getPlayer().dropMessage(1, "The password you entered is incorrect.");
                                    return;
                                }
                            }
                            else if (ips.getPassword().length() > 0) {
                                c.getPlayer().dropMessage(1, "The password you entered is incorrect.");
                                return;
                            }
                            chr.setPlayerShop(ips);
                            ips.addVisitor(chr);
                            if (ips instanceof MarriageMiniBox) {
                                ((MarriageMiniBox)ips).send(c);
                            }
                            else if (ips instanceof MapleMiniGame) {
                                ((MapleMiniGame)ips).send(c);
                            }
                            else {
                                c.getSession().writeAndFlush((Object)PlayerShopPacket.getPlayerStore(chr, false));
                            }
                        }
                    }
                    break;
                }
                break;
            }
            case HIRED_MERCHANT_MAINTENANCE: {
                if (c.getChannelServer().isShutdown() || chr.getMap() == null || chr.getTrade() != null) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                slea.skip(1);
                final byte type = slea.readByte();
                if (type != 5) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                final String password = slea.readMapleAsciiString();
                final int obid2 = slea.readInt();
                final MapleMapObject ob2 = chr.getMap().getMapObject(obid2, MapleMapObjectType.HIRED_MERCHANT);
                if (ob2 == null || chr.getPlayerShop() != null) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                break;
            }
            case CHAT: {
                slea.readInt();
                final String message = slea.readMapleAsciiString();
                if (chr.getTrade() != null) {
                    chr.getTrade().chat(message);
                 AdminTool.addMessage(7, "[" + c.getChannel() + "채널] " + c.getPlayer().getName() + " : " + message);
                 FileoutputUtil.log(FileoutputUtil.교환채팅로그, "[교환] [" + c.getChannel() + "채널] 상대방이름 : " + chr.getTrade().getPartner().getChr().getName() + " | " + c.getPlayer().getName() + " : " + message);
                  break;
                 } else if (chr.getPlayerShop() != null) {
          IMaplePlayerShop iMaplePlayerShop = chr.getPlayerShop();
          iMaplePlayerShop.broadcastToVisitors(PlayerShopPacket.shopChat(chr, chr.getName(), chr.getId(), chr.getName() + " : " + message, iMaplePlayerShop.getVisitorSlot(chr)));
          LogType.Chat chatType = LogType.Chat.PlayerShop;
          String etc = "";
          if (iMaplePlayerShop instanceof MaplePlayerShop) {
            chatType = LogType.Chat.PlayerShop;
            etc = "주인 : " + iMaplePlayerShop.getOwnerName() + " / 상점명 : " + iMaplePlayerShop.getDescription() + " / 수신 : " + iMaplePlayerShop.getMemberNames();
          } else if (iMaplePlayerShop instanceof MapleMiniGame) {
            chatType = LogType.Chat.MiniGame;
            etc = "주인 : " + iMaplePlayerShop.getOwnerName() + " / 게임명 : " + iMaplePlayerShop.getDescription() + " / 암호 : " + ((iMaplePlayerShop.getPassword() == null) ? "없음" : ("있음 - " + iMaplePlayerShop.getPassword())) + " / 수신 : " + iMaplePlayerShop.getMemberNames();
          } 
                    AdminTool.addMessage(8, "[" + c.getChannel() + "채널] " + c.getPlayer().getName() + " : " + message);
          FileoutputUtil.log(FileoutputUtil.미니게임채팅로그, "[미니게임] [" + c.getChannel() + "채널] | " + c.getPlayer().getName() + " : " + message);
          DBLogger.getInstance().logChat(chatType, c.getPlayer().getId(), c.getPlayer().getName(), message, etc);
                    if (chr.getClient().isMonitored()) {}
                    break;
                }
                break;
            }
            case EXIT: {
                if (chr.getTrade() != null) {
                    MapleTrade.cancelTrade(chr.getTrade(), chr.getClient(), chr);
                    break;
                }
                if (chr.getOneCardInstance() != null) {
                    chr.getOneCardInstance().sendPacketToPlayers(SLFCGPacket.leaveResult(chr.getOneCardInstance().getPlayer(chr).getPosition()));
                    chr.getOneCardInstance().playerDead(chr.getOneCardInstance().getPlayer(chr), false);
                    break;
                }
                if (chr.getBattleReverseInstance() != null) {
                    chr.getBattleReverseInstance().endGame(chr, true);
                    break;
                }
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null) {
                    return;
                }
                if (ips3.isOwner(chr) && ips3.getShopType() != 1) {
                    ips3.closeShop(false, ips3.isAvailable());
                }
                else {
                    ips3.removeVisitor(chr);
                }
                chr.setPlayerShop(null);
                break;
            }
            case OPEN: {
                final IMaplePlayerShop shop2 = chr.getPlayerShop();
                if (shop2 == null || !shop2.isOwner(chr) || shop2.getShopType() >= 3 || shop2.isAvailable()) {
                    break;
                }
                if (!chr.getMap().allowPersonalShop()) {
                    c.disconnect(true, false, false);
                    c.getSession().close();
                    break;
                }
                if (c.getChannelServer().isShutdown()) {
              chr.dropMessage(1, "서버가 곧 종료되기때문에, 상점을 세울수 없습니다.");
              c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    shop2.closeShop(shop2.getShopType() == 1, false);
                    return;
                }
                if (shop2.getShopType() == 2) {
                    shop2.setOpen(true);
                    shop2.setAvailable(true);
                    shop2.update();
                    break;
                }
                break;
            }
            case SET_ITEMS4:
            case SET_ITEMS3:
            case SET_ITEMS2:
            case SET_ITEMS1: {
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                final MapleInventoryType ivType = MapleInventoryType.getByType(slea.readByte());
                final Item item = chr.getInventory(ivType).getItem(slea.readShort());
                final short quantity = slea.readShort();
                final byte targetSlot = slea.readByte();
                if (chr.getTrade() != null && item != null && ((quantity <= item.getQuantity() && quantity >= 0) || GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId()))) {
                    chr.getTrade().setItems(c, item, targetSlot, quantity);
                    break;
                }
                break;
            }
            case SET_MESO4:
            case SET_MESO3:
            case SET_MESO2:
            case SET_MESO1: {
                final MapleTrade trade = chr.getTrade();
                if (trade != null) {
                    long meso = slea.readLong();
                    if (meso < 0L) {
                        meso &= 0xFFFFFFFFL;
                    }
                    trade.setMeso(meso);
                    break;
                }
                break;
            }
            case ADD_ITEM4:
            case ADD_ITEM3:
            case ADD_ITEM2:
            case ADD_ITEM1: {
                final MapleInventoryType type2 = MapleInventoryType.getByType(slea.readByte());
                final short slot = slea.readShort();
                final short bundles = slea.readShort();
                final short perBundle = slea.readShort();
                final long price = slea.readLong();
                if (price <= 0L || bundles <= 0 || perBundle <= 0) {
                    return;
                }
                final IMaplePlayerShop shop3 = chr.getPlayerShop();
                if (shop3 == null || !shop3.isOwner(chr) || shop3 instanceof MapleMiniGame) {
                    return;
                }
                final Item ivItem = chr.getInventory(type2).getItem(slot);
                final MapleItemInformationProvider ii2 = MapleItemInformationProvider.getInstance();
                if (ivItem == null) {
                    break;
                }
                final long check = bundles * perBundle;
                if (check > 32767L || check <= 0L) {
                    return;
                }
                final short bundles_perbundle = (short)(bundles * perBundle);
                if (ivItem.getQuantity() < bundles_perbundle) {
                      chr.dropMessage(1, "물품을 판매하려면 적어도 1개이상 있어야 합니다.");
                      c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                final int flag = ivItem.getFlag();
                if (ItemFlag.LOCK.check(flag)) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if ((ii2.isDropRestricted(ivItem.getItemId()) || ii2.isAccountShared(ivItem.getItemId()) || ItemFlag.UNTRADEABLE.check(flag)) && !ItemFlag.KARMA_EQUIP.check(flag) && !ItemFlag.KARMA_USE.check(flag)) {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if (bundles_perbundle < 50 || ivItem.getItemId() == 2340000) {}
                if (GameConstants.getLowestPrice(ivItem.getItemId()) > price) {
                    c.getPlayer().dropMessage(1, "The lowest you can sell this for is " + GameConstants.getLowestPrice(ivItem.getItemId()));
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if (GameConstants.isThrowingStar(ivItem.getItemId()) || GameConstants.isBullet(ivItem.getItemId())) {
                    MapleInventoryManipulator.removeFromSlot(c, type2, slot, ivItem.getQuantity(), true);
                    final Item sellItem = ivItem.copy();
                    shop3.addItem(new MaplePlayerShopItem(sellItem, (short)1, price));
                }
                else {
                    MapleInventoryManipulator.removeFromSlot(c, type2, slot, bundles_perbundle, true);
                    final Item sellItem = ivItem.copy();
                    sellItem.setQuantity(perBundle);
                    shop3.addItem(new MaplePlayerShopItem(sellItem, bundles, price));
                }
                c.getSession().writeAndFlush((Object)PlayerShopPacket.shopItemUpdate(shop3));
                break;
            }
            case CONFIRM_TRADE_MESO1:
            case CONFIRM_TRADE_MESO2:
            case CONFIRM_TRADE2:
            case CONFIRM_TRADE1:
            case BUY_ITEM_PLAYER_SHOP:
            case BUY_ITEM_STORE:
            case BUY_ITEM_HIREDMERCHANT: {
                if (chr.getTrade() != null) {
                    MapleTrade.completeTrade(chr);
                    break;
                }
                final int item2 = slea.readByte();
                final short quantity2 = slea.readShort();
                final IMaplePlayerShop shop4 = chr.getPlayerShop();
                if (shop4 == null || shop4.isOwner(chr) || shop4 instanceof MapleMiniGame || item2 >= shop4.getItems().size()) {
                    return;
                }
                final MaplePlayerShopItem tobuy = shop4.getItems().get(item2);
                if (tobuy == null) {
                    return;
                }
                final long check2 = tobuy.bundles * quantity2;
                final long check3 = tobuy.price * quantity2;
                final long check4 = tobuy.item.getQuantity() * quantity2;
                if (check2 <= 0L || check3 > 2147483647L || check3 <= 0L || check4 > 32767L || check4 < 0L) {
                    return;
                }
                if (tobuy.bundles < quantity2 || (tobuy.bundles % quantity2 != 0 && GameConstants.isEquip(tobuy.item.getItemId())) || chr.getMeso() - check3 < 0L || chr.getMeso() - check3 > 2147483647L || shop4.getMeso() + check3 < 0L || shop4.getMeso() + check3 > 2147483647L) {
                    return;
                }
                if (quantity2 >= 50 && tobuy.item.getItemId() == 2340000) {
                    c.setMonitored(true);
                }
                shop4.buy(c, item2, quantity2);
                shop4.broadcastToVisitors(PlayerShopPacket.shopItemUpdate(shop4));
                break;
            }
            case RESET_HIRED: {
                final byte subpacket = slea.readByte();
                final byte type3 = slea.readByte();
                if (subpacket == 19 && (type3 == 5 || type3 == 6)) {
                    final String secondPassword = slea.readMapleAsciiString();
                    if (c.CheckSecondPassword(secondPassword)) {
                        final int obid3 = slea.readInt();
                        final MapleMapObject ob3 = chr.getMap().getMapObject(obid3, MapleMapObjectType.HIRED_MERCHANT);
                        if (ob3 == null) {
                            return;
                        }
                    }
                    else {
            c.getPlayer().dropMessage(1, "2차비밀번호가 일치하지 않습니다. \r\n확인 후 다시 시도해 주세요.");
                  }
                    break;
                }
                if (subpacket == 11 && type3 == 4) {
                    final IMaplePlayerShop shop4 = chr.getPlayerShop();
                    shop4.setOpen(true);
                    shop4.setAvailable(true);
                    shop4.update();
                    break;
                }
                if (subpacket == 16 && type3 == 7) {
                    final String secondPassword = slea.readMapleAsciiString();
                    if (c.CheckSecondPassword(secondPassword)) {
                        final MapleCharacter chrr3 = chr.getMap().getCharacterById(slea.readInt());
                        if (chrr3 == null || c.getChannelServer().isShutdown()) {
                            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                            return;
                        }
                        MapleTrade.startCashTrade(chr);
                        MapleTrade.inviteCashTrade(chr, chrr3);
                    }
                    else {
                     c.getPlayer().dropMessage(1, "2차비밀번호가 일치하지 않습니다. \r\n확인 후 다시 시도해 주세요.");
         }
                    break;
                }
                if (subpacket == 19 && type3 == 7) {
                    final String secondPassword = slea.readMapleAsciiString();
                    if (c.CheckSecondPassword(secondPassword)) {
                        if (chr != null && chr.getTrade() != null && chr.getTrade().getPartner() != null && chr.getTrade().getPartner().getChr() != null) {
                            MapleTrade.visitCashTrade(chr, chr.getTrade().getPartner().getChr());
                        }
                        else {
                     c.getPlayer().dropMessage(1, "오류가 발생했습니다. \r\n잠시 후 다시 시도해 주세요.");
                 }
                    }
                    else {
                      c.getPlayer().dropMessage(1, "2차비밀번호가 일치하지 않습니다. \r\n확인 후 다시 시도해 주세요.");
          }
                    break;
                }
                break;
            }
            case REMOVE_ITEM: {
                slea.skip(1);
                final int slot2 = slea.readShort();
                final IMaplePlayerShop shop5 = chr.getPlayerShop();
                if (shop5 == null || !shop5.isOwner(chr) || shop5 instanceof MapleMiniGame || shop5.getItems().size() <= 0 || shop5.getItems().size() <= slot2 || slot2 < 0) {
                    return;
                }
                final MaplePlayerShopItem item3 = shop5.getItems().get(slot2);
                if (item3 != null && item3.bundles > 0) {
                    final Item item_get = item3.item.copy();
                    final long check2 = item3.bundles * item3.item.getQuantity();
                    if (check2 < 0L || check2 > 32767L) {
                        return;
                    }
                    item_get.setQuantity((short)check2);
                    if (item_get.getQuantity() >= 50 && item3.item.getItemId() == 2340000) {
                        c.setMonitored(true);
                    }
                    if (MapleInventoryManipulator.checkSpace(c, item_get.getItemId(), item_get.getQuantity(), item_get.getOwner())) {
                        MapleInventoryManipulator.addFromDrop(c, item_get, false);
                        item3.bundles = 0;
                        shop5.removeFromSlot(slot2);
                    }
                }
                c.getSession().writeAndFlush((Object)PlayerShopPacket.shopItemUpdate(shop5));
                break;
            }
            case MAINTANCE_ORGANISE: {
                final IMaplePlayerShop imps = chr.getPlayerShop();
                if (imps != null && imps.isOwner(chr) && !(imps instanceof MapleMiniGame)) {
                    for (int i = 0; i < imps.getItems().size(); ++i) {
                        if (imps.getItems().get(i).bundles == 0) {
                            imps.getItems().remove(i);
                        }
                    }
                    if (chr.getMeso() + imps.getMeso() > 0L) {
                        chr.gainMeso(imps.getMeso(), false);
                        imps.setMeso(0L);
                    }
                    c.getSession().writeAndFlush((Object)PlayerShopPacket.shopItemUpdate(imps));
                    break;
                }
                break;
            }
            case CLOSE_MERCHANT: {
                final IMaplePlayerShop merchant = chr.getPlayerShop();
                if (merchant != null && merchant.getShopType() == 1 && merchant.isOwner(chr)) {
                    boolean save = false;
                    if (chr.getMeso() + merchant.getMeso() < 0L) {
                        save = false;
                    }
                    else {
                        if (merchant.getMeso() > 0L) {
                            chr.gainMeso(merchant.getMeso(), false);
                        }
                        merchant.setMeso(0L);
                        if (merchant.getItems().size() > 0) {
                            for (final MaplePlayerShopItem items : merchant.getItems()) {
                                if (items.bundles > 0) {
                                    final Item item_get2 = items.item.copy();
                                    item_get2.setQuantity((short)(items.bundles * items.item.getQuantity()));
                                    if (!MapleInventoryManipulator.addFromDrop(c, item_get2, false)) {
                                        save = true;
                                        break;
                                    }
                                    items.bundles = 0;
                                    save = false;
                                }
                            }
                        }
                    }
                    if (save) {
                  c.getPlayer().dropMessage(1, "프레드릭 에게서 아이템을 찾아가 주십시오.");
                 c.getSession().writeAndFlush((Object)PlayerShopPacket.shopErrorMessage(20, 0));
                    }
                    else {
                        c.getSession().writeAndFlush((Object)PlayerShopPacket.MerchantClose(0, 0));
                    }
                    merchant.closeShop(save, true);
                    chr.setPlayerShop(null);
                    break;
                }
                break;
            }
            case TAKE_MESOS: {
                final IMaplePlayerShop imps = chr.getPlayerShop();
                if (imps == null || !imps.isOwner(chr)) {
                    break;
                }
                if (chr.getMeso() + imps.getMeso() < 0L) {
                    c.getSession().writeAndFlush((Object)PlayerShopPacket.shopItemUpdate(imps));
                    break;
                }
                chr.gainMeso(imps.getMeso(), false);
                imps.setMeso(0L);
                c.getSession().writeAndFlush((Object)PlayerShopPacket.shopItemUpdate(imps));
                break;
            }
            case ADMIN_STORE_NAMECHANGE: {
                final String storename = slea.readMapleAsciiString();
                c.getSession().writeAndFlush((Object)PlayerShopPacket.merchantNameChange(chr.getId(), storename));
                break;
            }
            case GIVE_UP: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                final MapleMiniGame game2 = (MapleMiniGame)ips3;
                if (game2.isOpen()) {
                    break;
                }
                game2.broadcastToVisitors(PlayerShopPacket.getMiniGameResult(game2, 0, game2.getVisitorSlot(chr)));
                game2.nextLoser();
                game2.setOpen(true);
                game2.update();
                game2.checkExitAfterGame();
                break;
            }
            case EXPEL: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                if (!((MapleMiniGame)ips3).isOpen()) {
                    break;
                }
                ips3.removeAllVisitors(5, 1);
                break;
            }
            case READY:
            case UN_READY: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 != null && ips3 instanceof MapleMiniGame) {
                    final MapleMiniGame game2 = (MapleMiniGame)ips3;
                    if (!game2.isOwner(chr) && game2.isOpen()) {
                        game2.setReady(game2.getVisitorSlot(chr));
                        game2.broadcastToVisitors(PlayerShopPacket.getMiniGameReady(game2.isReady(game2.getVisitorSlot(chr))));
                    }
                    break;
                }
                break;
            }
            case START: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 != null && ips3 instanceof MapleMiniGame) {
                    final MapleMiniGame game2 = (MapleMiniGame)ips3;
                    if (game2.isOwner(chr) && game2.isOpen()) {
                        for (int j = 1; j < ips3.getSize(); ++j) {
                            if (!game2.isReady(j)) {
                                return;
                            }
                        }
                        game2.setGameType();
                        game2.shuffleList();
                        if (game2.getGameType() == 1) {
                            game2.broadcastToVisitors(PlayerShopPacket.getMiniGameStart(game2.getLoser()));
                        }
                        else {
                            game2.broadcastToVisitors(PlayerShopPacket.getMatchCardStart(game2, game2.getLoser()));
                        }
                        game2.setOpen(false);
                        game2.update();
                        game2.broadcastToVisitors(PlayerShopPacket.getMiniGameInfoMsg((byte)102, chr.getName()));
                    }
                    break;
                }
                if (chr.getTrade() != null && chr.getTrade().getPartner() != null) {
                    c.getSession().writeAndFlush((Object)PlayerShopPacket.StartRPS());
                    chr.getTrade().getPartner().getChr().getClient().getSession().writeAndFlush((Object)PlayerShopPacket.StartRPS());
                    break;
                }
                break;
            }
            case REQUEST_TIE: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                final MapleMiniGame game2 = (MapleMiniGame)ips3;
                if (game2.isOpen()) {
                    break;
                }
                if (game2.isOwner(chr)) {
                    game2.broadcastToVisitors(PlayerShopPacket.getMiniGameRequestTie(), false);
                }
                else {
                    game2.getMCOwner().getClient().getSession().writeAndFlush((Object)PlayerShopPacket.getMiniGameRequestTie());
                }
                game2.setRequestedTie(game2.getVisitorSlot(chr));
                break;
            }
            case ANSWER_TIE: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                final MapleMiniGame game2 = (MapleMiniGame)ips3;
                if (game2.isOpen()) {
                    break;
                }
                if (game2.getRequestedTie() > -1 && game2.getRequestedTie() != game2.getVisitorSlot(chr)) {
                    if (slea.readByte() > 0) {
                        game2.broadcastToVisitors(PlayerShopPacket.getMiniGameResult(game2, 1, game2.getRequestedTie()));
                        game2.nextLoser();
                        game2.setOpen(true);
                        game2.update();
                        game2.checkExitAfterGame();
                    }
                    else {
                        game2.broadcastToVisitors(PlayerShopPacket.getMiniGameDenyTie());
                    }
                    game2.setRequestedTie(-1);
                }
                break;
            }
            case REQUEST_REDO: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                final MapleMiniGame game2 = (MapleMiniGame)ips3;
                if (game2.isOpen()) {
                    break;
                }
                if (game2.isOwner(chr)) {
                    game2.broadcastToVisitors(PlayerShopPacket.getMiniGameRequestRedo(), false);
                }
                else {
                    game2.getMCOwner().getClient().getSession().writeAndFlush((Object)PlayerShopPacket.getMiniGameRequestRedo());
                }
                break;
            }
            case ANSWER_REDO: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                final MapleMiniGame game2 = (MapleMiniGame)ips3;
                if (game2.isOpen()) {
                    break;
                }
                if (slea.readByte() > 0) {
                    ips3.broadcastToVisitors(PlayerShopPacket.getMiniGameSkip((int)((ips3.getVisitorSlot(chr) == 0) ? 1 : 0)));
                    game2.nextLoser();
                }
                else {
                    game2.broadcastToVisitors(PlayerShopPacket.getMiniGameDenyRedo());
                }
                break;
            }
            case SKIP: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 != null && ips3 instanceof MapleMiniGame) {
                    final MapleMiniGame game2 = (MapleMiniGame)ips3;
                    if (game2.isOpen()) {
                        break;
                    }
                    if (game2.getLoser() != ips3.getVisitorSlot(chr)) {
                        return;
                    }
                    ips3.broadcastToVisitors(PlayerShopPacket.getMiniGameSkip((int)((ips3.getVisitorSlot(chr) == 0) ? 1 : 0)));
                    game2.nextLoser();
                    break;
                }
                else {
                    if (chr.getTrade() != null && chr.getTrade().getPartner() != null) {
                        chr.getTrade().setRPS(slea.readByte());
                        Timer.ShowTimer.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                final byte result = PlayerInteractionHandler.getResult(chr.getTrade().getPRS(), chr.getTrade().getPartner().getPRS());
                                if (result == 2) {
                           chr.dropMessage(1, "아쉽지만, 가위바위보에서 지셨습니다!");
                          chr.addFame(-1);
                                }
                                else if (result == 0) {
                                   chr.dropMessage(1, "축하합니다! 가위바위보에서 이기셨습니다!");
                      chr.addFame(1);
                                }
                                c.getSession().writeAndFlush((Object)PlayerShopPacket.FinishRPS(result, chr.getTrade().getPartner().getPRS()));
                            }
                        }, 1000L);
                        break;
                    }
                    break;
                }
            }
            case MOVE_OMOK: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                final MapleMiniGame game2 = (MapleMiniGame)ips3;
                if (game2.isOpen()) {
                    break;
                }
                if (game2.getLoser() != game2.getVisitorSlot(chr)) {
                    return;
                }
                game2.setPiece(slea.readInt(), slea.readInt(), slea.readByte(), chr);
                break;
            }
            case SELECT_CARD: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                final MapleMiniGame game2 = (MapleMiniGame)ips3;
                if (game2.isOpen()) {
                    break;
                }
                if (game2.getLoser() != game2.getVisitorSlot(chr)) {
                    return;
                }
                if (slea.readByte() != game2.getTurn()) {
                    return;
                }
                final int slot3 = slea.readByte();
                final int turn = game2.getTurn();
                final int fs = game2.getFirstSlot();
                if (turn == 1) {
                    game2.setFirstSlot(slot3);
                    if (game2.isOwner(chr)) {
                        game2.broadcastToVisitors(PlayerShopPacket.getMatchCardSelect(turn, slot3, fs, turn), false);
                    }
                    else {
                        game2.getMCOwner().getClient().getSession().writeAndFlush((Object)PlayerShopPacket.getMatchCardSelect(turn, slot3, fs, turn));
                    }
                    game2.setTurn(0);
                    return;
                }
                if (fs > 0 && game2.getCardId(fs + 1) == game2.getCardId(slot3 + 1)) {
                    game2.broadcastToVisitors(PlayerShopPacket.getMatchCardSelect(turn, slot3, fs, game2.isOwner(chr) ? 2 : 3));
                    game2.setPoints(game2.getVisitorSlot(chr));
                }
                else {
                    game2.broadcastToVisitors(PlayerShopPacket.getMatchCardSelect(turn, slot3, fs, (int)(game2.isOwner(chr) ? 0 : 1)));
                    game2.nextLoser();
                }
                game2.setTurn(1);
                game2.setFirstSlot(0);
                break;
            }
            case EXIT_AFTER_GAME:
            case CANCEL_EXIT: {
                final IMaplePlayerShop ips3 = chr.getPlayerShop();
                if (ips3 == null || !(ips3 instanceof MapleMiniGame)) {
                    break;
                }
                final MapleMiniGame game2 = (MapleMiniGame)ips3;
                if (game2.isOpen()) {
                    break;
                }
                game2.setExitAfter(chr);
                if (game2.isExitAfter(chr)) {
                    game2.broadcastToVisitors(PlayerShopPacket.getMiniGameInfoMsg((byte)5, chr.getName()));
                }
                else {
                    game2.broadcastToVisitors(PlayerShopPacket.getMiniGameInfoMsg((byte)6, chr.getName()));
                }
                break;
            }
            case ONECARD: {

                byte type = slea.readByte();

                switch (type) {
                    case 0: {
                        int objectId = slea.readInt();
                        OneCardGame oc = chr.getOneCardInstance();

                        if (oc == null) {
                            return;
                        }

                        OneCardGame.OneCardPlayer ocp = oc.getPlayer(chr);

                        if (ocp == null) {
                            return;
                        }

                        OneCardGame.OneCard selCard = null;

                        for (OneCardGame.OneCard card : ocp.getCards()) {
                            if (card.getObjectId() == objectId) {
                                selCard = card;
                                break;
                            }
                        }

                        if (selCard != null) {
                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onPutCardResult(ocp, selCard));
                            ocp.getCards().remove(selCard);
                        } else {
                            System.out.println("selCard에 문제 발생.");
                            oc.sendPacketToPlayers(CWvsContext.serverNotice(1, "", "원카드에 문제가 발생하여 게임이 종료됩니다."));
                            oc.endGame(ocp, true);
                            return;
                        }

                        if (ocp.getCards().size() == 0 || ocp.getCards().isEmpty()) {
                            oc.endGame(ocp, false);
                            return;
                            //우승
                        } else if (ocp.getCards().size() == 1) {
                            chr.getClient().getSession().writeAndFlush(CField.playSound("Sound/MiniGame.img/oneCard/lastcard"));
                        }

                        oc.setLastCard(selCard);

                        if (oc.getLastCard().getType() == 11) {
                            oc.setbClockWiseTurn(!oc.isbClockWiseTurn());
                        }

                        OneCardGame.OneCardPlayer nextPlayer = null;
                        if (oc.getLastCard().getType() == 9 || oc.getLastCard().getType() == 8 || (oc.getLastCard().getType() == 12 && oc.getLastCard().getColor() == 3)) {
                            nextPlayer = oc.getLastPlayer();
                        } else {
                            if (oc.getLastCard().getType() == 10) {
                                nextPlayer = oc.setNextPlayer(oc.setNextPlayer(oc.getLastPlayer(), oc.isbClockWiseTurn()), oc.isbClockWiseTurn());
                            } else {
                                nextPlayer = oc.setNextPlayer(oc.getLastPlayer(), oc.isbClockWiseTurn());
                            }
                        }

                        if (oc.getLastCard().getType() == 6) {
                            oc.setFire(oc.getFire() + 2);
                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(2, 2, chr.getId(), false));
                            oc.sendPacketToPlayers(SLFCGPacket.onShowText("마법 : " + chr.getName() + "님의 공격!"));
                        } else if (oc.getLastCard().getType() == 7) {
                            oc.setFire(oc.getFire() + 3);
                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(2, 3, chr.getId(), false));
                            oc.sendPacketToPlayers(SLFCGPacket.onShowText("마법 : " + chr.getName() + "님의 공격!"));
                        } else if (oc.getLastCard().getType() == 12 & oc.getLastCard().getColor() == 0) {
                            oc.setFire(oc.getFire() + 5);
                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/oz"));
                            oc.sendPacketToPlayers(CField.playSound("Sound/MiniGame.img/oneCard/flame_burst"));
                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(2, 5, chr.getId(), false));
                            oc.sendPacketToPlayers(SLFCGPacket.onShowText("마법 : " + chr.getName() + "님의 공격!"));
                        }

                        if (oc.getLastCard().getType() == 8) {
                            oc.sendPacketToPlayers(SLFCGPacket.onShowText("마법 : 색 바꾸기!"));
                        }

                        if (oc.getLastCard().getType() == 9) {
                            oc.sendPacketToPlayers(SLFCGPacket.onShowText("마법 : 한 번 더!"));
                        }

                        if (oc.getLastCard().getType() == 10) {
                            oc.sendPacketToPlayers(SLFCGPacket.onShowText("마법 : 점프!"));
                        }

                        if (oc.getLastCard().getType() == 11) {
                            oc.sendPacketToPlayers(SLFCGPacket.onShowText("마법 : 거꾸로!"));
                        }

                        if (oc.getLastCard().getType() == 12) {
                            if (oc.getLastCard().getColor() == 1) {
                                oc.setFire(0);
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/michael"));
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(3, 0, chr.getId(), false));
                                oc.sendPacketToPlayers(CField.playSound("Sound/MiniGame.img/oneCard/shield_appear"));
                            } else if (oc.getLastCard().getColor() == 2) {
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/hawkeye"));
                                for (OneCardGame.OneCardPlayer bp : oc.getPlayers()) {
                                    List<OneCardGame.OneCard> newcards = new ArrayList<>();

                                    if (bp.getPlayer().getId() != chr.getId()) {
                                        for (int i = 0; i < 2; ++i) {
                                            if (oc.getOneCardDeckInfo().size() == 0) {
                                                oc.resetDeck();
                                                if (oc.getOneCardDeckInfo().size() == 0) {
                                                    break;
                                                }
                                            }
                                            int num = Randomizer.nextInt(oc.getOneCardDeckInfo().size());
                                            OneCardGame.OneCard card = oc.getOneCardDeckInfo().get(num);
                                            bp.getCards().add(card);
                                            newcards.add(card);
                                            oc.getOneCardDeckInfo().remove(num);
                                        }
                                        oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onGetCardResult(bp, newcards));
                                    }
                                }
                            } else if (oc.getLastCard().getColor() == 3) {
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/irina"));
                                for (OneCardGame.OneCardPlayer bp : oc.getPlayers()) {
                                    List<OneCardGame.OneCard> removes = new ArrayList<>();
                                    for (OneCardGame.OneCard card : bp.getCards()) {
                                        if (card.getColor() == 3) {
                                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onPutCardResult(bp, card));
                                            removes.add(card);
                                        }
                                    }

                                    for (OneCardGame.OneCard card : removes) {
                                        bp.getCards().remove(card);
                                    }

                                    if (bp.getCards().size() == 0 || bp.getCards().isEmpty()) {
                                        oc.endGame(bp, false);
                                        return;
                                        //우승
                                    } else if (ocp.getCards().size() == 1) {
                                        oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/lastcard"));
                                    }
                                }

                                List<Integer> ableColors = new ArrayList<>();
                                for (int i = 0; i <= 3; ++i) {
                                    ableColors.add(i);
                                }

                                c.getSession().writeAndFlush(SLFCGPacket.OneCardGamePacket.onChangeColorRequest(ableColors));
                                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));

                                oc.setLastPlayer(nextPlayer);

                                if (oc.getOneCardTimer() != null) {
                                    oc.getOneCardTimer().cancel(false);
                                }

                                oc.setOneCardTimer(Timer.ShowTimer.getInstance().schedule(() -> {
                                    //15초 잠수시 강제로 카드 맥이고 넘어가야함
                                    oc.skipPlayer();
                                }, 15 * 1000));

                                return;
                            } else if (oc.getLastCard().getColor() == 4) {
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/icart")); // 이카르트
                            }
                        }

                        oc.setLastPlayer(nextPlayer);

                        if (nextPlayer != null) {

                            if (oc.getLastCard().getType() == 8) {// || (oc.getLastCard().getType() == 12 && oc.getLastCard().getType() == 3)) {

                                List<Integer> ableColors = new ArrayList<>();

                                for (int i = 0; i <= 3; ++i) {
                                    if (i != oc.getLastCard().getColor()) {
                                        ableColors.add(i);
                                    }
                                }

                                c.getSession().writeAndFlush(SLFCGPacket.OneCardGamePacket.onChangeColorRequest(ableColors));
                                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                            } else {
                                List<OneCardGame.OneCard> possibleCards = new ArrayList<>();
                                if (oc.getLastCard().getColor() == 4) {
                                    for (OneCardGame.OneCard card : nextPlayer.getCards()) {
                                        if (oc.getFire() == 0) {
                                            possibleCards.add(card);
                                        } else {
                                            if (card.getType() == 6 || card.getType() == 7 || (card.getType() == 12 && card.getColor() == 0)) {
                                                possibleCards.add(card);
                                            }
                                        }
                                    }
                                } else {
                                    for (OneCardGame.OneCard card : nextPlayer.getCards()) {
                                        if (card.getType() <= 5) {
                                            if ((card.getColor() == oc.getLastCard().getColor() || card.getType() == oc.getLastCard().getType()) && oc.getFire() == 0) {
                                                possibleCards.add(card);
                                            }
                                        } else if (card.getType() <= 7) {
                                            if (oc.getFire() == 0) {
                                                if (card.getColor() == oc.getLastCard().getColor() || card.getType() == oc.getLastCard().getType()) {
                                                    possibleCards.add(card);
                                                }
                                            } else {
                                                if (oc.getLastCard().getType() == 6) {
                                                    if (card.getType() == 6) {
                                                        possibleCards.add(card);
                                                    } else if (card.getType() == 7 && card.getColor() == oc.getLastCard().getColor()) {
                                                        possibleCards.add(card);
                                                    }
                                                } else if (oc.getLastCard().getType() == 7) {
                                                    if (card.getType() == 7) {
                                                        possibleCards.add(card);
                                                    }
                                                }
                                            }
                                        } else if (card.getType() <= 11) {
                                            if ((card.getColor() == oc.getLastCard().getColor() || card.getType() == oc.getLastCard().getType()) && oc.getFire() == 0) {
                                                possibleCards.add(card);
                                            }
                                        } else {
                                            switch (card.getColor()) {
                                                case 0: // 오즈
                                                    if (oc.getFire() > 0) {
                                                        possibleCards.add(card);
                                                    }
                                                    break;
                                                case 1: // 미하일
                                                    if (oc.getFire() > 0 || oc.getLastCard().getColor() == 1) {
                                                        possibleCards.add(card);
                                                    }
                                                    break;
                                                case 2: // 호크아이
                                                    if (oc.getLastCard().getColor() == card.getColor() && oc.getFire() == 0) {
                                                        possibleCards.add(card);
                                                    }
                                                    break;
                                                case 3: // 이리나
                                                    if (oc.getLastCard().getColor() == card.getColor() && oc.getFire() == 0) {
                                                        possibleCards.add(card);
                                                    }
                                                    break;
                                                case 4: // 이카르트
                                                    possibleCards.add(card);
                                                    break;
                                            }
                                        }
                                    }
                                }

                                if (oc.getOneCardDeckInfo().size() == 0 || oc.getOneCardDeckInfo().isEmpty()) {
                                    oc.resetDeck();
                                    oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(0, 0, 0, false));
                                    oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(nextPlayer, possibleCards, (possibleCards.size() == 0 && nextPlayer.getCards().size() == 16) || nextPlayer.getCards().size() < 16, oc.isbClockWiseTurn()));
                                } else {
                                    oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(nextPlayer, possibleCards, (possibleCards.size() == 0 && nextPlayer.getCards().size() == 16) || nextPlayer.getCards().size() < 16, oc.isbClockWiseTurn()));
                                }

                                nextPlayer.getPlayer().getClient().getSession().writeAndFlush(CField.playSound("Sound/MiniGame.img/oneCard/myturn"));
                                nextPlayer.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.enableActions(nextPlayer.getPlayer()));
                                nextPlayer.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.onShowText("당신의 턴입니다."));
                            }

                            if (oc.getOneCardTimer() != null) {
                                oc.getOneCardTimer().cancel(false);
                            }

                            oc.setOneCardTimer(Timer.ShowTimer.getInstance().schedule(() -> {
                                //15초 잠수시 강제로 카드 맥이고 넘어가야함
                                oc.skipPlayer();
                            }, 15 * 1000));
                        }
                        break;
                    }
                    case 1: {
                        OneCardGame oc = chr.getOneCardInstance();
                        OneCardGame.OneCardPlayer ocp = oc.getPlayer(chr);
                        List<OneCardGame.OneCard> newcards = new ArrayList<>();

                        if (oc.getFire() > 0) {
                            oc.sendPacketToPlayers(SLFCGPacket.onShowText(chr.getName() + "님이 " + oc.getFire() + "의 피해를 입었습니다."));
                        }
                        for (int i = 0; i < (oc.getFire() > 0 ? oc.getFire() : 1); ++i) {
                            if (oc.getOneCardDeckInfo().size() == 0) {
                                oc.resetDeck();
                                if (oc.getOneCardDeckInfo().size() == 0) {
                                    break;
                                }
                            }
                            int num = Randomizer.nextInt(oc.getOneCardDeckInfo().size());
                            OneCardGame.OneCard card = oc.getOneCardDeckInfo().get(num);
                            ocp.getCards().add(card);
                            newcards.add(card);
                            oc.getOneCardDeckInfo().remove(num);
                        }
                        oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onGetCardResult(ocp, newcards));

                        if (ocp.getCards().size() >= 17) { // 파산
                            oc.setFire(0);
                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onShowScreenEffect("/Effect/screeneff/gameover"));
                            chr.getClient().getSession().writeAndFlush(CField.playSound("Sound/MiniGame.img/oneCard/gameover"));
                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(5, 0, chr.getId(), true));
                            oc.playerDead(ocp, false);
                        } else if (oc.getFire() > 0) {
                            oc.setFire(0);
                            oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(4, 0, chr.getId(), false));
                        }

                        OneCardGame.OneCardPlayer nextPlayer = oc.setNextPlayer(oc.getLastPlayer(), oc.isbClockWiseTurn());

                        oc.setLastPlayer(nextPlayer);

                        if (nextPlayer != null) {
                            List<OneCardGame.OneCard> possibleCards = new ArrayList<>();
                            if (oc.getLastCard().getColor() == 4) {
                                for (OneCardGame.OneCard card : nextPlayer.getCards()) {
                                    possibleCards.add(card);
                                }
                            } else {
                                for (OneCardGame.OneCard card : nextPlayer.getCards()) {
                                    if (card.getType() <= 11) {
                                        if ((card.getColor() == oc.getLastCard().getColor() || card.getType() == oc.getLastCard().getType())) {
                                            possibleCards.add(card);
                                        }
                                    } else {
                                        switch (card.getColor()) {
                                            case 0: // 오즈
                                                if (oc.getLastCard().getColor() == card.getColor()) {
                                                    possibleCards.add(card);
                                                }
                                                break;
                                            case 1: // 미하일
                                                possibleCards.add(card);
                                                break;
                                            case 2: // 호크아이
                                                if (oc.getLastCard().getColor() == card.getColor()) {
                                                    possibleCards.add(card);
                                                }
                                                break;
                                            case 3: // 이리나
                                                if (oc.getLastCard().getColor() == card.getColor()) {
                                                    possibleCards.add(card);
                                                }
                                                break;
                                            case 4: // 이카르트
                                                possibleCards.add(card);
                                                break;
                                        }
                                    }
                                }
                            }

                            if (oc.getOneCardDeckInfo().size() == 0 || oc.getOneCardDeckInfo().isEmpty()) {
                                oc.resetDeck();
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(0, 0, 0, false));
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(nextPlayer, possibleCards, nextPlayer.getCards().size() < 17, oc.isbClockWiseTurn()));
                            } else {
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(nextPlayer, possibleCards, nextPlayer.getCards().size() < 17, oc.isbClockWiseTurn()));
                            }

                            nextPlayer.getPlayer().getClient().getSession().writeAndFlush(CField.playSound("Sound/MiniGame.img/oneCard/myturn"));
                            nextPlayer.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.enableActions(nextPlayer.getPlayer()));

                            if (oc.getOneCardTimer() != null) {
                                oc.getOneCardTimer().cancel(false);
                            }

                            oc.setOneCardTimer(Timer.ShowTimer.getInstance().schedule(() -> {
                                //15초 잠수시 강제로 카드 맥이고 넘어가야함
                                oc.skipPlayer();
                            }, 15 * 1000));
                        }
                        break;
                    }
                    case 2: {
                        byte color = slea.readByte();
                        OneCardGame oc = chr.getOneCardInstance();

                        oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onChangeColorResult(oc.getLastCard().getType() == 12, color));

                        OneCardGame.OneCardPlayer nextPlayer = oc.setNextPlayer(oc.getLastPlayer(), oc.isbClockWiseTurn());

                        oc.getLastCard().setColor(color);

                        oc.setLastPlayer(nextPlayer);

                        if (nextPlayer != null) {

                            List<OneCardGame.OneCard> possibleCards = new ArrayList<>();
                            if (oc.getLastCard().getColor() == 4) {
                                for (OneCardGame.OneCard card : nextPlayer.getCards()) {
                                    possibleCards.add(card);
                                }
                            } else {
                                for (OneCardGame.OneCard card : nextPlayer.getCards()) {
                                    if (card.getType() <= 11) {
                                        if (card.getColor() == oc.getLastCard().getColor() || card.getType() == oc.getLastCard().getType()) {
                                            possibleCards.add(card);
                                        }
                                    } else {
                                        switch (card.getColor()) {
                                            case 0: // 오즈
                                                if (oc.getLastCard().getColor() == card.getColor()) {
                                                    possibleCards.add(card);
                                                }
                                                break;
                                            case 1: // 미하일
                                                possibleCards.add(card);
                                                break;
                                            case 2: // 호크아이
                                                if (oc.getLastCard().getColor() == card.getColor()) {
                                                    possibleCards.add(card);
                                                }
                                                break;
                                            case 3: // 이리나
                                                if (oc.getLastCard().getColor() == card.getColor()) {
                                                    possibleCards.add(card);
                                                }
                                                break;
                                            case 4: // 이카르트
                                                possibleCards.add(card);
                                                break;
                                        }
                                    }
                                }
                            }

                            if (oc.getOneCardDeckInfo().size() == 0 || oc.getOneCardDeckInfo().isEmpty()) {
                                oc.resetDeck();
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEffectResult(0, 0, 0, false));
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(nextPlayer, possibleCards, nextPlayer.getCards().size() < 17, oc.isbClockWiseTurn()));
                            } else {
                                oc.sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onUserPossibleAction(nextPlayer, possibleCards, nextPlayer.getCards().size() < 17, oc.isbClockWiseTurn()));
                            }

                            nextPlayer.getPlayer().getClient().getSession().writeAndFlush(CField.playSound("Sound/MiniGame.img/oneCard/myturn"));
                            nextPlayer.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.enableActions(nextPlayer.getPlayer()));

                            if (oc.getOneCardTimer() != null) {
                                oc.getOneCardTimer().cancel(false);
                            }

                            oc.setOneCardTimer(Timer.ShowTimer.getInstance().schedule(() -> {
                                //15초 잠수시 강제로 카드 맥이고 넘어가야함
                                oc.skipPlayer();
                            }, 15 * 1000));
                        }
                        break;
                    }
                }
                break;
            }
            case ONECARD_EMOTION: {
                slea.skip(4);
                int emotionId = slea.readInt();
                chr.getOneCardInstance().sendPacketToPlayers(SLFCGPacket.OneCardGamePacket.onEmotion(chr.getId(), emotionId));
                chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
                break;
            }
            default: {
                System.out.println("Unhandled interaction action by " + chr.getName() + " : " + action + ", " + slea.toString());
                break;
            }
        }
    }
    
    public static void minigameOperation(final LittleEndianAccessor slea, final MapleClient c) {
        final int type = slea.readInt();
        switch (type) {
            case 185: {
                final Point pos = new Point(slea.readInt(), slea.readInt());
                if (c.getPlayer().getBattleReverseInstance() != null) {
                    c.getPlayer().getBattleReverseInstance().sendPlaceStone(c.getPlayer(), pos);
                    break;
                }
                c.getPlayer().warp(ServerConstants.warpMap);
               c.getPlayer().dropMessage(5, "오류가 발생하여 게임이 취소됩니다.");
               break;
            }
        }
    }
    
    public static final byte getResult(final byte rps1, final byte rps2) {
        switch (rps1) {
            case 0: {
                if (rps2 == 1) {
                    return 2;
                }
                if (rps2 == 2) {
                    return 0;
                }
                break;
            }
            case 1: {
                if (rps2 == 2) {
                    return 2;
                }
                if (rps2 == 0) {
                    return 0;
                }
                break;
            }
            case 2: {
                if (rps2 == 0) {
                    return 2;
                }
                if (rps2 == 1) {
                    return 0;
                }
                break;
            }
        }
        return 1;
    }
    
    public enum Interaction
    {
        SET_ITEMS1(0), 
        SET_ITEMS2(1), 
        SET_ITEMS3(2), 
        SET_ITEMS4(3), 
        SET_MESO1(4), 
        SET_MESO2(5), 
        SET_MESO3(6), 
        SET_MESO4(7), 
        CONFIRM_TRADE1(8), 
        CONFIRM_TRADE2(9), 
        CONFIRM_TRADE_MESO1(10), 
        CONFIRM_TRADE_MESO2(11), 
        CREATE(16), 
        VISIT(19), 
        INVITE_TRADE(21), 
        DENY_TRADE(22), 
        CHAT(24), 
        OPEN(26), 
        EXIT(28), 
        HIRED_MERCHANT_MAINTENANCE(29), 
        RESET_HIRED(30), 
        ADD_ITEM1(31), 
        ADD_ITEM2(32), 
        ADD_ITEM3(33), 
        ADD_ITEM4(34), 
        BUY_ITEM_HIREDMERCHANT(35), 
        PLAYER_SHOP_ADD_ITEM(36), 
        BUY_ITEM_PLAYER_SHOP(37), 
        BUY_ITEM_STORE(38), 
        REMOVE_ITEM(47), 
        MAINTANCE_OFF(48), 
        MAINTANCE_ORGANISE(49), 
        CLOSE_MERCHANT(50), 
        TAKE_MESOS(52), 
        VIEW_MERCHANT_VISITOR(55), 
        VIEW_MERCHANT_BLACKLIST(56), 
        MERCHANT_BLACKLIST_ADD(57), 
        MERCHANT_BLACKLIST_REMOVE(58), 
        ADMIN_STORE_NAMECHANGE(59), 
        REQUEST_TIE(85), 
        ANSWER_TIE(86), 
        GIVE_UP(87), 
        REQUEST_REDO(89), 
        ANSWER_REDO(90), 
        EXIT_AFTER_GAME(91), 
        CANCEL_EXIT(92), 
        READY(93), 
        UN_READY(94), 
        EXPEL(95), 
        START(96), 
        GAME_RESULT(97), 
        SKIP(98), 
        MOVE_OMOK(99), 
        SELECT_CARD(103), 
        WEDDING_START(105), 
        WEDDING_END(108), 
        INVITE_ROCK_PAPER_SCISSORS(112), 
        ONECARD(155), 
        ONECARD_EMOTION(156);
        
        public int action;
        
        private Interaction(final int action) {
            this.action = action;
        }
        
        public static Interaction getByAction(final int i) {
            for (final Interaction s : values()) {
                if (s.action == i) {
                    return s;
                }
            }
            return null;
        }
    }
}
