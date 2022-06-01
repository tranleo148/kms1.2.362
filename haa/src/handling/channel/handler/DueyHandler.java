package handling.channel.handler;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleDueyActions;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class DueyHandler {
  public static final void DueyOperation(LittleEndianAccessor slea, MapleClient c) {
    String secondPassword;
    byte inventId;
    int packageid;
    short itemPos;
    MapleDueyActions dp;
    short amount;
    int mesos;
    String recipient;
    boolean quickdelivery;
    String letter;
    int qq;
    long finalcost;
    byte operation = slea.readByte();
    switch (operation) {
      case 1:
        secondPassword = slea.readMapleAsciiString();
        if (c.CheckSecondPassword(secondPassword)) {
          slea.skip(4);
          int conv = c.getPlayer().getConversation();
          if (conv == 2) {
            List<MapleDueyActions> list1 = new ArrayList<>();
            List<MapleDueyActions> list2 = new ArrayList<>();
            for (MapleDueyActions mapleDueyActions : loadItems(c.getPlayer())) {
              if (mapleDueyActions.isExpire()) {
                list2.add(mapleDueyActions);
                continue;
              } 
              list1.add(mapleDueyActions);
            } 
            c.getSession().writeAndFlush(CField.sendDuey((byte)10, list1, list2));
            for (MapleDueyActions mapleDueyActions : list2)
              removeItemFromDB(mapleDueyActions.getPackageId(), c.getPlayer().getId()); 
          } 
        } else {
          c.getSession().writeAndFlush(CField.checkFailedDuey());
        } 
        return;
      case 3:
        if (c.getPlayer().getConversation() != 2)
          return; 
        if (!c.getPlayer().isGM()) {
          c.getPlayer().dropMessage(1, "현재 아이템 및 메소 수령만 가능합니다.");
          return;
        } 
        inventId = slea.readByte();
        itemPos = slea.readShort();
        amount = slea.readShort();
        mesos = slea.readInt();
        recipient = slea.readMapleAsciiString();
        quickdelivery = (slea.readByte() > 0);
        letter = "";
        qq = 0;
        if (quickdelivery) {
          letter = slea.readMapleAsciiString();
          qq = slea.readInt();
        } 
        finalcost = mesos + GameConstants.getTaxAmount(mesos) + (quickdelivery ? 0L : 5000L);
        if (mesos >= 0 && mesos <= 100000000 && c.getPlayer().getMeso() >= finalcost) {
          int accid = MapleCharacterUtil.getAccByName(recipient);
          int cid = MapleCharacterUtil.getIdByName(recipient);
          if (accid != -1) {
            if (accid != c.getAccID()) {
              boolean recipientOn = false;
              MapleClient rClient = null;
              int channel = World.Find.findChannel(recipient);
              if (channel > -1) {
                recipientOn = true;
                ChannelServer rcserv = ChannelServer.getInstance(channel);
                rClient = rcserv.getPlayerStorage().getCharacterByName(recipient).getClient();
              } 
              if (inventId > 0) {
                MapleInventoryType inv = MapleInventoryType.getByType(inventId);
                Item item = c.getPlayer().getInventory(inv).getItem(itemPos);
                if (item == null) {
                  c.getSession().writeAndFlush(CField.sendDuey((byte)17, null, null));
                  return;
                } 
                List<MapleDueyActions> dps = loadItems(c.getPlayer());
                for (MapleDueyActions mda : dps) {
                  if (mda.getItem() != null) {
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    if (ii.isPickupRestricted(mda.getItem().getItemId()) && mda.getItem().getItemId() == item.getItemId()) {
                      c.getSession().writeAndFlush(CField.sendDuey((byte)18, null, null));
                      return;
                    } 
                  } 
                } 
                int flag = item.getFlag();
                if (ItemFlag.UNTRADEABLE.check(flag) || ItemFlag.LOCK.check(flag)) {
                  c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                  return;
                } 
                if ((GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) && 
                  item.getQuantity() == 0)
                  amount = 0; 
                if (c.getPlayer().getItemQuantity(item.getItemId(), false) >= amount) {
                  MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                  if (!ii.isDropRestricted(item.getItemId()) && !ii.isAccountShared(item.getItemId())) {
                    Item toSend = item.copy();
                    if (!GameConstants.isThrowingStar(toSend.getItemId()) && !GameConstants.isBullet(toSend.getItemId()))
                      toSend.setQuantity(amount); 
                    if (addItemToDB(toSend, mesos, c.getPlayer().getName(), cid, recipientOn, letter, qq, quickdelivery)) {
                      if (GameConstants.isThrowingStar(toSend.getItemId()) || GameConstants.isBullet(toSend.getItemId())) {
                        MapleInventoryManipulator.removeFromSlot(c, inv, (short)(byte)itemPos, toSend.getQuantity(), true, false);
                      } else {
                        MapleInventoryManipulator.removeFromSlot(c, inv, (short)(byte)itemPos, amount, true, false);
                      } 
                      c.getSession().writeAndFlush(CField.sendDuey((byte)19, null, null));
                    } else {
                      c.getSession().writeAndFlush(CField.sendDuey((byte)17, null, null));
                    } 
                  } else {
                    c.getSession().writeAndFlush(CField.sendDuey((byte)17, null, null));
                  } 
                } else {
                  c.getSession().writeAndFlush(CField.sendDuey((byte)17, null, null));
                } 
              } else if (addMesoToDB(mesos, c.getPlayer().getName(), cid, recipientOn, letter, quickdelivery)) {
                c.getSession().writeAndFlush(CField.sendDuey((byte)19, null, null));
              } else {
                c.getSession().writeAndFlush(CField.sendDuey((byte)17, null, null));
              } 
              if (recipientOn && rClient != null && 
                quickdelivery)
                rClient.getSession().writeAndFlush(CField.receiveParcel(c.getPlayer().getName(), quickdelivery)); 
            } else {
              c.getSession().writeAndFlush(CField.sendDuey((byte)15, null, null));
            } 
          } else {
            c.getSession().writeAndFlush(CField.sendDuey((byte)14, null, null));
          } 
        } else {
          c.getSession().writeAndFlush(CField.sendDuey((byte)12, null, null));
        } 
        return;
      case 5:
        if (c.getPlayer().getConversation() != 2)
          return; 
        packageid = slea.readInt();
        dp = loadSingleItem(packageid, c.getPlayer().getId());
        if (dp == null)
          return; 
        if (dp.isExpire() || !dp.canReceive())
          return; 
        if (dp.getItem() != null && !MapleInventoryManipulator.checkSpace(c, dp.getItem().getItemId(), dp.getItem().getQuantity(), dp.getItem().getOwner())) {
          c.getSession().writeAndFlush(CField.sendDuey((byte)16, null, null));
          return;
        } 
        if (dp.getMesos() < 0 || dp.getMesos() + c.getPlayer().getMeso() < 0L) {
          c.getSession().writeAndFlush(CField.sendDuey((byte)17, null, null));
          return;
        } 
        if (dp.getItem() != null && 
          c.getPlayer().haveItem(dp.getItem().getItemId(), 1, true, true) && MapleItemInformationProvider.getInstance().isPickupRestricted(dp.getItem().getItemId())) {
          c.getSession().writeAndFlush(CField.sendDuey((byte)18, null, null));
          return;
        } 
        removeItemFromDB(packageid, c.getPlayer().getId());
        if (dp.getItem() != null && dp.getItem().getQuantity() > 0)
          MapleInventoryManipulator.addbyItem(c, dp.getItem(), false); 
        if (dp.getMesos() != 0)
          c.getPlayer().gainMeso(dp.getMesos(), false); 
        c.getSession().writeAndFlush(CField.removeItemFromDuey(false, packageid));
        return;
      case 6:
        if (c.getPlayer().getConversation() != 2)
          return; 
        packageid = slea.readInt();
        removeItemFromDB(packageid, c.getPlayer().getId());
        c.getSession().writeAndFlush(CField.removeItemFromDuey(true, packageid));
        return;
      case 8:
        c.getPlayer().setConversation(0);
        return;
    } 
    System.out.println("Unhandled Duey operation : " + slea.toString());
  }
  
  private static final boolean addMesoToDB(int mesos, String sName, int recipientID, boolean isOn, String content, boolean quick) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("INSERT INTO dueypackages (RecieverId, SenderName, Mesos, TimeStamp, Checked, Type, `Quick`, content) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      ps.setInt(1, recipientID);
      ps.setString(2, sName);
      ps.setInt(3, mesos);
      ps.setLong(4, System.currentTimeMillis());
      ps.setInt(5, isOn ? 0 : 1);
      ps.setInt(6, 3);
      ps.setInt(7, quick ? 1 : 0);
      ps.setString(8, content);
      ps.executeUpdate();
      ps.close();
      return true;
    } catch (SQLException se) {
      se.printStackTrace();
      return false;
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  public static final boolean addItemToDB(Item item, int mesos, String sName, int recipientID, boolean isOn, String content, int qq, boolean Quick) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("INSERT INTO dueypackages (RecieverId, SenderName, Mesos, TimeStamp, Checked, Type, content, `Quick`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", 1);
      ps.setInt(1, recipientID);
      ps.setString(2, sName);
      ps.setInt(3, mesos);
      ps.setLong(4, System.currentTimeMillis());
      ps.setInt(5, isOn ? 0 : 1);
      ps.setInt(6, item.getType());
      ps.setString(7, content);
      ps.setInt(8, Quick ? 1 : 0);
      ps.executeUpdate();
      rs = ps.getGeneratedKeys();
      if (rs.next())
        ItemLoader.DUEY.saveItems(Collections.singletonList(item), con, rs.getInt(1), GameConstants.getInventoryType(item.getItemId()), true); 
      rs.close();
      ps.close();
      return true;
    } catch (SQLException se) {
      se.printStackTrace();
      return false;
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  public static final List<MapleDueyActions> loadItems(MapleCharacter chr) {
    List<MapleDueyActions> packages = new LinkedList<>();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM dueypackages WHERE RecieverId = ?");
      ps.setInt(1, chr.getId());
      rs = ps.executeQuery();
      while (rs.next()) {
        MapleDueyActions dueypack = getItemByPID(rs.getInt("packageid"));
        dueypack.setSender(rs.getString("SenderName"));
        dueypack.setMesos(rs.getInt("Mesos"));
        dueypack.setSentTime(rs.getLong("TimeStamp"));
        dueypack.setContent(rs.getString("content"));
        dueypack.setQuick((rs.getInt("Quick") > 0));
        packages.add(dueypack);
      } 
      rs.close();
      ps.close();
      return packages;
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  public static final MapleDueyActions loadSingleItem(int packageid, int charid) {
    List<MapleDueyActions> packages = new LinkedList<>();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM dueypackages WHERE PackageId = ? and RecieverId = ?");
      ps.setInt(1, packageid);
      ps.setInt(2, charid);
      rs = ps.executeQuery();
      if (rs.next()) {
        MapleDueyActions dueypack = getItemByPID(packageid);
        dueypack.setSender(rs.getString("SenderName"));
        dueypack.setMesos(rs.getInt("Mesos"));
        dueypack.setSentTime(rs.getLong("TimeStamp"));
        dueypack.setContent(rs.getString("content"));
        dueypack.setQuick((rs.getInt("Quick") > 0));
        packages.add(dueypack);
        rs.close();
        ps.close();
        return dueypack;
      } 
      rs.close();
      ps.close();
      return null;
    } catch (SQLException se) {
      return null;
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  public static final void reciveMsg(MapleClient c, int recipientId) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("UPDATE dueypackages SET Checked = 0 where RecieverId = ?");
      ps.setInt(1, recipientId);
      ps.executeUpdate();
      ps.close();
    } catch (SQLException se) {
      se.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  private static final void removeItemFromDB(int packageid, int charid) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("DELETE FROM dueypackages WHERE PackageId = ? and RecieverId = ?");
      ps.setInt(1, packageid);
      ps.setInt(2, charid);
      ps.executeUpdate();
      ps.close();
    } catch (SQLException se) {
      se.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  private static final MapleDueyActions getItemByPID(int packageid) {
    try {
      Map<Long, Item> iter = ItemLoader.DUEY.loadItems(false, packageid, null);
      if (iter != null && iter.size() > 0) {
        Iterator<Map.Entry<Long, Item>> iterator = iter.entrySet().iterator();
        if (iterator.hasNext()) {
          Map.Entry<Long, Item> i = iterator.next();
          return new MapleDueyActions(packageid, i.getValue());
        } 
      } 
    } catch (Exception se) {
      se.printStackTrace();
    } 
    return new MapleDueyActions(packageid);
  }
}
