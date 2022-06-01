package server.shops;

import client.MapleClient;
import client.MapleShopLimit;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.GameConstants;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.StringUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleShop {
  private static final Set<Integer> rechargeableItems = new LinkedHashSet<>();
  
  private int id;
  
  private int npcId;
  
  private int coinKey;
  
  private int rechargeshop;
  
  private int questEx;
  
  private String saleString;
  
  private String shopString;
  
  private List<MapleShopItem> items;
  
  static {
    rechargeableItems.add(Integer.valueOf(2070000));
    rechargeableItems.add(Integer.valueOf(2070001));
    rechargeableItems.add(Integer.valueOf(2070002));
    rechargeableItems.add(Integer.valueOf(2070003));
    rechargeableItems.add(Integer.valueOf(2070004));
    rechargeableItems.add(Integer.valueOf(2070005));
    rechargeableItems.add(Integer.valueOf(2070006));
    rechargeableItems.add(Integer.valueOf(2070007));
    rechargeableItems.add(Integer.valueOf(2070008));
    rechargeableItems.add(Integer.valueOf(2070009));
    rechargeableItems.add(Integer.valueOf(2070010));
    rechargeableItems.add(Integer.valueOf(2070011));
    rechargeableItems.add(Integer.valueOf(2070012));
    rechargeableItems.add(Integer.valueOf(2070013));
    rechargeableItems.add(Integer.valueOf(2070023));
    rechargeableItems.add(Integer.valueOf(2070024));
    rechargeableItems.add(Integer.valueOf(2070026));
    rechargeableItems.add(Integer.valueOf(2330000));
    rechargeableItems.add(Integer.valueOf(2330001));
    rechargeableItems.add(Integer.valueOf(2330002));
    rechargeableItems.add(Integer.valueOf(2330003));
    rechargeableItems.add(Integer.valueOf(2330004));
    rechargeableItems.add(Integer.valueOf(2330005));
    rechargeableItems.add(Integer.valueOf(2330008));
    rechargeableItems.add(Integer.valueOf(2330016));
    rechargeableItems.add(Integer.valueOf(2331000));
    rechargeableItems.add(Integer.valueOf(2332000));
  }
  
  public MapleShop(int id, int npcId, int coinKey, int questEx, String shopString, String saleString) {
    this.id = id;
    this.npcId = npcId;
    this.coinKey = coinKey;
    this.questEx = questEx;
    this.shopString = shopString;
    this.saleString = saleString;
    this.items = new LinkedList<>();
  }
  
  public MapleShop(int id, int npcId, int coinKey, String shopString, String saleString, int rechargeshop) {
    this.id = id;
    this.npcId = npcId;
    this.coinKey = coinKey;
    this.shopString = shopString;
    this.saleString = saleString;
    this.rechargeshop = rechargeshop;
    this.items = new LinkedList<>();
  }
  
  public void addItem(MapleShopItem item) {
    this.items.add(item);
  }
  
  public void sendShop(MapleClient c) {
    sendShop(c, 0);
  }
  
    public void sendShop(MapleClient c, int id2) {
        if (this.items == null) {
            System.out.println("상점에 아무정보가 없습니다.");
            return;
        }
        if (MapleShopFactory.getInstance().getShop(this.id).getRechargeShop() == 1) {
            boolean active = false;
            boolean save = false;
            Calendar ocal = Calendar.getInstance();
            for (MapleShopItem item : MapleShopFactory.getInstance().getShop(this.id).getItems()) {
                int maxday = ocal.getActualMaximum(5);
                int month = ocal.get(2) + 1;
                int day = ocal.get(5);
                if (item.getReCharge() <= 0) continue;
                for (MapleShopLimit shl : c.getShopLimit()) {
                    if (shl.getLastBuyMonth() <= 0 || shl.getLastBuyDay() <= 0) continue;
                    GregorianCalendar baseCal = new GregorianCalendar(ocal.get(1), shl.getLastBuyMonth(), shl.getLastBuyDay());
                    GregorianCalendar targetCal = new GregorianCalendar(ocal.get(1), month, day);
                    long diffSec = (targetCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 1000L;
                    long diffDays = diffSec / 86400L;
                    if (shl.getItemid() != item.getItemId() || shl.getShopId() != MapleShopFactory.getInstance().getShop(this.id).getId() || shl.getPosition() != item.getPosition() || diffDays < 0L) continue;
                    shl.setLastBuyMonth(0);
                    shl.setLastBuyDay(0);
                    shl.setLimitCountAcc(0);
                    shl.setLimitCountChr(0);
                    save = true;
                    break;
                }
                if (item.getReChargeDay() > day || item.getReChargeMonth() > month) continue;
                active = true;
                int afterday = day + item.getReCharge();
                if (afterday > maxday) {
                    afterday -= maxday;
                    if (++month > 12) {
                        month = 1;
                    }
                }
                Connection con = null;
                PreparedStatement ps = null;
                try {
                    con = DatabaseConnection.getConnection();
                    ps = con.prepareStatement("UPDATE shopitems SET rechargemonth = ?, rechargeday = ?, resetday = ? WHERE position = ? AND itemid = ? AND tab = ?");
                    ps.setInt(1, month);
                    ps.setInt(2, afterday);
                    ps.setInt(3, day);
                    ps.setInt(4, item.getPosition());
                    ps.setInt(5, item.getItemId());
                    ps.setByte(6, item.getTab());
                    ps.executeUpdate();
                    ps.close();
                    con.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (con != null) {
                            con.close();
                        }
                        if (ps == null) continue;
                        ps.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (active) {
                MapleShopFactory.getInstance().clear();
                MapleShopFactory.getInstance().getShop(this.id);
            } else if (save) {
                c.saveShopLimit(c.getShopLimit());
            }
        }
        c.getPlayer().setShop(this);
        c.getSession().writeAndFlush((Object)CField.NPCPacket.getNPCShop(id2 == 0 ? this.getNpcId() : id2, MapleShopFactory.getInstance().getShop(this.id), c));
        block13: for (MapleShopItem item : this.getItems()) {
            if (item.getReCharge() <= 0) continue;
            for (MapleShopLimit shl : c.getShopLimit()) {
                if (shl.getItemid() != item.getItemId() || shl.getShopId() != this.getId() || shl.getPosition() != item.getPosition()) continue;
                c.send(CField.NPCPacket.getShopLimit(this.getNpcId(), item.getPosition() - 1, item.getItemId(), shl.getLimitCountAcc()));
                continue block13;
            }
        }
    }
  
  public List<MapleShopItem> getItems() {
    return this.items;
  }
  
    public void buy(MapleClient c, int itemId, short quantity, short position) {
        MapleItemInformationProvider ii;
        int x = 0;
        int index = -1;
        for (Item i : c.getPlayer().getRebuy()) {
            if (i.getItemId() == itemId) {
                index = x;
                break;
            }
            ++x;
        }
        if (index >= 0) {
            Item i;
            MapleItemInformationProvider ii2 = MapleItemInformationProvider.getInstance();
            i = c.getPlayer().getRebuy().get(index);
            int price = (int)Math.max(Math.ceil(ii2.getPrice(itemId) * (double)(GameConstants.isRechargable(itemId) ? (short)1 : i.getQuantity())), 0.0);
            if (price >= 0 && c.getPlayer().getMeso() >= (long)price) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, i.getQuantity(), i.getOwner())) {
                    c.getPlayer().gainMeso(-price, false);
                    MapleInventoryManipulator.addbyItem(c, i);
                    c.getPlayer().getRebuy().remove(index);
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, index, itemId, true, false, 999999));
                } else {
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)7, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)2, this, c, -1, itemId));
            }
            return;
        }
        MapleShopItem item = this.findById(itemId, position);
        if (item == null || item.getItemId() != itemId) {
            c.getPlayer().dropMessage(1, "아이템 정보를 불러오는 도중 오류가 발생했습니다.");
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        if (item.getReCharge() > 0) {
            boolean add = true;
            for (MapleShopLimit shl : c.getShopLimit()) {
                if (shl.getItemid() != item.getItemId() || shl.getShopId() != this.getId() || shl.getPosition() != item.getPosition()) continue;
                add = false;
                shl.setLimitCountAcc(shl.getLimitCountAcc() + 1);
                shl.setLimitCountChr(shl.getLimitCountChr() + 1);
                shl.setLastBuyMonth(item.getReChargeMonth());
                shl.setLastBuyDay(item.getReChargeDay());
                c.send(CField.NPCPacket.getShopLimit(this.getNpcId(), item.getPosition() - 1, itemId, shl.getLimitCountAcc()));
                break;
            }
            if (add) {
                MapleShopLimit Sl = new MapleShopLimit(c.getPlayer().getAccountID(), c.getPlayer().getId(), this.getId(), item.getItemId(), item.getPosition(), 1, 1, item.getReChargeMonth(), item.getReChargeDay());
                c.getShopLimit().add(Sl);
                c.send(CField.NPCPacket.getShopLimit(this.getNpcId(), item.getPosition() - 1, itemId, 1));
            }
            c.saveShopLimit(c.getShopLimit());
        }
        if (item != null && item.getPrice() > 0L && item.getPriceQuantity() == 0 && this.coinKey == 0) {
            if (c.getPlayer().getMeso() >= item.getPrice() * (long)quantity) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                    if (GameConstants.isPet(itemId)) {
                        MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, MaplePet.createPet(itemId, item.getPeriod()), 0L, StringUtil.getAllCurrentTime() + "에 " + (this.id + " 상점에서 구매한 아이템."));
                    } else {
                        ii = MapleItemInformationProvider.getInstance();
                        if (GameConstants.isRechargable(itemId)) {
                            quantity = ii.getSlotMax(item.getItemId());
                            c.getPlayer().gainMeso(-item.getPrice(), false);
                            MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod(), "");
                        } else {
                            c.getPlayer().gainMeso(-(item.getPrice() * (long)quantity), false);
                            c.getPlayer().gainItem(itemId, (short)(item.getQuantity() * quantity), false, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), "상점에서 구입한 아이템");
                        }
                    }
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                } else {
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)7, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)2, this, c, -1, itemId));
            }
        } else if (item != null && item.getPrice() > 0L && item.getPriceQuantity() > 0) {
            if (c.getPlayer().haveItem((int)item.getPrice(), item.getPriceQuantity() * quantity, false, true)) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                    if (GameConstants.isPet(itemId)) {
                        MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType((int)item.getPrice()), (int)item.getPrice(), item.getPriceQuantity(), false, false);
                        MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, MaplePet.createPet(itemId, item.getPeriod()), 30L, StringUtil.getAllCurrentTime() + "에 " + (this.id + " 상점에서 구매한 아이템."));
                    } else {
                        ii = MapleItemInformationProvider.getInstance();
                        if (GameConstants.isRechargable(itemId)) {
                            quantity = ii.getSlotMax(item.getItemId());
                            MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType((int)item.getPrice()), (int)item.getPrice(), item.getPriceQuantity(), false, false);
                            MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod(), StringUtil.getAllCurrentTime() + "에 " + (this.id + " 상점에서 구매한 아이템."));
                        } else {
                            MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType((int)item.getPrice()), (int)item.getPrice(), item.getPriceQuantity() * quantity, false, false);
                            if (GameConstants.getInventoryType(item.getItemId()) == MapleInventoryType.EQUIP || GameConstants.getInventoryType(item.getItemId()) == MapleInventoryType.CODY) {
                                for (int i = 0; i < quantity; ++i) {
                                    c.getPlayer().gainItem(itemId, (short)1, false, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod() * 60L * 1000L + System.currentTimeMillis(), StringUtil.getAllCurrentTime() + "에 " + (this.id + " 상점에서 구입한 아이템"));
                                }
                            } else {
                                c.getPlayer().gainItem(itemId, (short)(item.getQuantity() * quantity), false, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), StringUtil.getAllCurrentTime() + "에 " + (this.id + " 상점에서 구입한 아이템"));
                            }
                        }
                    }
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                } else {
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)7, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)1, this, c, -1, itemId));
            }
        } else if (this.coinKey == 500629 && item != null && item.getPrice() > 0L) {
            if (c.getKeyValue("유니온코인") == null) {
                c.setKeyValue("유니온코인", c.getPlayer().getKeyValue(this.coinKey, "point") + "");
            }
            if (item.getPrice() >= 0L && (long)Integer.parseInt(c.getKeyValue("유니온코인")) >= item.getPrice()) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                    c.getPlayer().setKeyValue(this.coinKey, "point", c.getPlayer().getKeyValue(this.coinKey, "point") - item.getPrice() + "");
                    c.setKeyValue("유니온코인", "" + ((long)Integer.parseInt(c.getKeyValue("유니온코인")) - item.getPrice()));
                    MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), "");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                } else {
                    c.getPlayer().dropMessage(1, "인벤토리가 부족합니다.");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
            }
        } else if (this.coinKey == 501372 && item != null && item.getPrice() > 0L) {
            if (item.getPrice() >= 0L && (long)Integer.parseInt(c.getKeyValue("WishCoinGain")) >= item.getPrice()) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                    c.getPlayer().setKeyValue(this.coinKey, "point", c.getPlayer().getKeyValue(this.coinKey, "point") - item.getPrice() + "");
                    c.setKeyValue("WishCoinGain", "" + ((long)Integer.parseInt(c.getKeyValue("WishCoinGain")) - item.getPrice()));
                    MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), "");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                } else {
                    c.getPlayer().dropMessage(1, "인벤토리가 부족합니다.");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
            }
        } else if (this.coinKey == 501215 && item != null && item.getPrice() > 0L) {
            if (item.getPrice() >= 0L && (long)Integer.parseInt(c.getKeyValue("RecommendPoint")) >= item.getPrice()) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                    c.getPlayer().setKeyValue(this.coinKey, "point", c.getPlayer().getKeyValue(this.coinKey, "point") - item.getPrice() + "");
                    c.setKeyValue("RecommendPoint", "" + ((long)Integer.parseInt(c.getKeyValue("RecommendPoint")) - item.getPrice()));
                    MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), "");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                } else {
                    c.getPlayer().dropMessage(1, "인벤토리가 부족합니다.");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
            }
        } else if (this.coinKey == 501368 && item != null && item.getPrice() > 0L) {
            if (item.getPrice() >= 0L && (long)Integer.parseInt(c.getCustomData(501368, "spoint")) >= item.getPrice()) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                    c.getPlayer().setKeyValue(501368, "point", c.getPlayer().getKeyValue(501368, "point") - item.getPrice() + "");
                    c.setCustomData(501368, "spoint", "" + ((long)Integer.parseInt(c.getCustomData(501368, "spoint")) - item.getPrice()));
                    MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), "");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                } else {
                    c.getPlayer().dropMessage(1, "인벤토리가 부족합니다.");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
            }
        } else if (this.coinKey == 501468 && item != null && item.getPrice() > 0L) {
            if (item.getPrice() >= 0L && c.getCustomKeyValue(501468, "point") >= item.getPrice()) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                    c.setCustomKeyValue(this.coinKey, "point", c.getCustomKeyValue(501468, "point") - item.getPrice() + "");
                    MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), "");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                } else {
                    c.getPlayer().dropMessage(1, "인벤토리가 부족합니다.");
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
            }
        } else if (this.coinKey != 0 && item != null && item.getPrice() > 0L) {
            if (!MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                c.getPlayer().dropMessage(1, "인벤토리가 부족합니다.");
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
            } else if (item.getPrice() >= 0L && c.getPlayer().getKeyValue(this.coinKey, "point") >= item.getPrice()) {
                int buyable = (int)c.getPlayer().getKeyValue(this.getQuestEx() + 100000, item.getShopItemId() + "_buyed");
                if (item.getBuyQuantity() != 0 && item.getBuyQuantity() - buyable <= 0) {
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)20, this, c, -1, itemId));
                    return;
                }
                if (MapleInventoryManipulator.checkSpace(c, itemId, (short)(quantity * item.getQuantity()), "")) {
                    c.getPlayer().setKeyValue(this.coinKey, "point", String.valueOf(c.getPlayer().getKeyValue(this.coinKey, "point") - item.getPrice()));
                    if (GameConstants.isPet(itemId)) {
                        MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, MaplePet.createPet(itemId, item.getPeriod()), 0L, "");
                    } else {
                        MapleItemInformationProvider ii3 = MapleItemInformationProvider.getInstance();
                        if (GameConstants.isRechargable(itemId)) {
                            quantity = ii3.getSlotMax(item.getItemId());
                            c.getPlayer().gainMeso(-item.getPrice(), false);
                            MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod(), StringUtil.getAllCurrentTime() + "에 " + (this.id + " 상점에서 구매한 아이템."));
                        } else if (GameConstants.getInventoryType(item.getItemId()) == MapleInventoryType.EQUIP) {
                            for (int i = 0; i < quantity; ++i) {
                                c.getPlayer().gainItem(itemId, (short)1, false, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), StringUtil.getAllCurrentTime() + "에 " + (this.id + " 상점에서 구입한 아이템"));
                            }
                        } else {
                            MapleInventoryManipulator.addById(c, itemId, (short)(quantity * item.getQuantity()), null, null, item.getPeriod() <= 0 ? 0L : (long)item.getPeriod(), "");
                        }
                    }
                    if (item.getBuyQuantity() != 0) {
                        if (c.getPlayer().getKeyValue(this.getQuestEx() + 100000, item.getShopItemId() + "_buyed") == -1L) {
                            c.getPlayer().setKeyValue(this.getQuestEx() + 100000, item.getShopItemId() + "_buyed", "0");
                        }
                        c.getPlayer().setKeyValue(this.getQuestEx() + 100000, item.getShopItemId() + "_buyed", c.getPlayer().getKeyValue(this.getQuestEx() + 100000, item.getShopItemId() + "_buyed") + 1L + "");
                        buyable = (int)c.getPlayer().getKeyValue(this.getQuestEx() + 100000, item.getShopItemId() + "_buyed");
                        c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId, item.getBuyQuantity() - buyable));
                        c.getSession().writeAndFlush((Object)CField.NPCPacket.ShopItemInfoReset(this, c, itemId, buyable, item.getPosition()));
                    } else {
                        c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, itemId));
                    }
                } else {
                    c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)7, this, c, -1, itemId));
                }
            } else {
                c.getSession().writeAndFlush((Object)CField.NPCPacket.confirmShopTransactionItem((byte)3, this, c, -1, itemId));
            }
            return;
        }
    }
  
    public void sell(MapleClient c, MapleInventoryType type, short slot, short quantity) {
        if (quantity <= 0) {
            quantity = 1;
        }
        Item item = c.getPlayer().getInventory(type).getItem(slot);
        if (item == null) {
            return;
        }


        if (item.getType() == 1) {
            Equip eq = (Equip) item;
            if (eq.getEnchantBuff() > 0) {
                c.getPlayer().dropMessage(1, "장비의 흔적은 이동이 불가합니다.");
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
        }
        if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
            quantity = item.getQuantity();
        }
        short iQuant = item.getQuantity();
        if (iQuant == 0xFFFF) {
            iQuant = 1;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (quantity <= iQuant && iQuant > 0) {
            Item itemm = item.copy();
            itemm.setQuantity((short) quantity);
            c.getPlayer().getRebuy().add(itemm);
            MapleInventoryManipulator.removeFromSlot(c, type, slot, quantity, false);
            if (itemm.getReward() != null) {
                if (c.getPlayer().getKeyValue(501619, "count") <= 0) {
                    System.out.println(c.getPlayer() + " 새끼가 결정석 핵 쓰려고 시도함.");
                    c.disconnect(true, false, false);
                    c.getSession().close();
                    return;
                } else {
                    c.getSession().writeAndFlush(CWvsContext.setBossReward(c.getPlayer()));
                }
            }
            //c.getPlayer().getInventory(type).removeItem(slot, quantity, false);
            double price;
            if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
                price = ii.getWholePrice(item.getItemId()) / (double) ii.getSlotMax(item.getItemId());
            } else if (itemm.getReward() != null) {
        price = itemm.getReward().getPrice();
                quantity = 1;
            } else {
                price = ii.getPrice(item.getItemId());
            }
            final long recvMesos = (long) Math.max(Math.ceil(price * quantity), 0);
            if (price != -1 && recvMesos > 0) {
                c.getPlayer().gainMeso(recvMesos, false);
            }
            c.getSession().writeAndFlush(CField.NPCPacket.confirmShopTransactionItem((byte) 11, this, c, -1, item.getItemId()));
        }
    }
  
  public void recharge(MapleClient c, short slot) {
    Item item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
    if (item == null || (!GameConstants.isThrowingStar(item.getItemId()) && !GameConstants.isBullet(item.getItemId())))
      return; 
    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
    short slotMax = ii.getSlotMax(item.getItemId());
    int skill = GameConstants.getMasterySkill(c.getPlayer().getJob());
    if (skill != 0)
      slotMax = (short)(slotMax + c.getPlayer().getSkillLevel(SkillFactory.getSkill(skill)) * 10); 
    if (item.getQuantity() < slotMax) {
      int price = (int)Math.round(ii.getPrice(item.getItemId()) * (slotMax - item.getQuantity()));
      if (c.getPlayer().getMeso() >= price) {
        item.setQuantity(slotMax);
        c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventorySlot(MapleInventoryType.USE, item, false));
        c.getPlayer().gainMeso(-price, false, true);
        c.getSession().writeAndFlush(CField.NPCPacket.confirmShopTransactionItem((byte)0, this, c, -1, item.getItemId()));
      } 
    } 
  }
  
  protected MapleShopItem findById(int itemId, int position) {
    for (MapleShopItem item : this.items) {
      if (item.getItemId() == itemId && item.getPosition() == position)
        return item; 
    } 
    return null;
  }
  
  public static MapleShop createFromDB(int id, boolean isShopId) {
    MapleShop ret = null;
    Connection con = null;
    try {
      int shopId;
      con = DatabaseConnection.getConnection();
      PreparedStatement ps = con.prepareStatement(isShopId ? "SELECT * FROM shops WHERE shopid = ?" : "SELECT * FROM shops WHERE npcid = ?");
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        shopId = rs.getInt("shopid");
        ret = new MapleShop(shopId, rs.getInt("npcid"), rs.getInt("coinKey"), rs.getString("shopString"), rs.getString("saleString"), rs.getInt("rechargeshop"));
        rs.close();
        ps.close();
      } else {
        rs.close();
        ps.close();
        return null;
      } 
      ps = con.prepareStatement("SELECT * FROM shopitems WHERE shopid = ? ORDER BY position ASC");
      ps.setInt(1, shopId);
      rs = ps.executeQuery();
      List<Integer> recharges = new ArrayList<>(rechargeableItems);
      int i = 1;
      while (rs.next()) {
        if (GameConstants.isThrowingStar(rs.getInt("itemid")) || GameConstants.isBullet(rs.getInt("itemid"))) {
          MapleShopItem starItem = new MapleShopItem(rs.getInt("shopitemid"), (short)1, rs.getInt("itemid"), rs.getInt("price"), rs.getInt("pricequantity"), (byte)rs.getInt("Tab"), rs.getInt("quantity"), rs.getInt("period"), i, rs.getInt("itemrate"), rs.getInt("buyquantity"), rs.getInt("rechargemonth"), rs.getInt("rechargeday"), rs.getInt("resetday"), rs.getInt("recharge"), rs.getInt("rechargecount"));
          ret.addItem(starItem);
          if (rechargeableItems.contains(Integer.valueOf(starItem.getItemId())))
            recharges.remove(Integer.valueOf(starItem.getItemId())); 
        } else {
          ret.addItem(new MapleShopItem(rs.getInt("shopitemid"), (short)1000, rs.getInt("itemid"), rs.getInt("price"), rs.getInt("pricequantity"), (byte)rs.getInt("Tab"), rs.getInt("quantity"), rs.getInt("period"), i, rs.getInt("itemrate"), rs.getInt("buyquantity"), rs.getInt("rechargemonth"), rs.getInt("rechargeday"), rs.getInt("resetday"), rs.getInt("recharge"), rs.getInt("rechargecount")));
        } 
        i++;
      } 
      i = 1;
      for (Integer recharge : recharges) {
        ret.addItem(new MapleShopItem(0, (short)1000, recharge.intValue(), 0L, 0, (byte)0, 0, 0, i, 0, 0, 0, 0, 0, 0, 0));
        i++;
      } 
      rs.close();
      ps.close();
    } catch (SQLException e) {
      System.err.println("Could not load shop" + e);
    } finally {
      if (con != null)
        try {
          con.close();
        } catch (SQLException ex) {
          Logger.getLogger(MapleShop.class.getName()).log(Level.SEVERE, (String)null, ex);
        }  
    } 
    return ret;
  }
  
  public int getNpcId() {
    return this.npcId;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getCoinKey() {
    return this.coinKey;
  }
  
  public void setCoinKey(int coinKey) {
    this.coinKey = coinKey;
  }
  
  public String getSaleString() {
    return this.saleString;
  }
  
  public void setSaleString(String saleString) {
    this.saleString = saleString;
  }
  
  public String getShopString() {
    return this.shopString;
  }
  
  public void setShopString(String shopString) {
    this.shopString = shopString;
  }
  
  public int getQuestEx() {
    return this.questEx;
  }
  
  public void setQuestEx(int questEx) {
    this.questEx = questEx;
  }
  
  public int getRechargeShop() {
    return this.rechargeshop;
  }
}
