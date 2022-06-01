package server.marriage;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleRing;
import java.lang.ref.WeakReference;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.maps.MapleMapObjectType;
import server.shops.AbstractPlayerStore;
import tools.packet.MarriageEXPacket;
import tools.packet.PlayerShopPacket;

public class MarriageMiniBox extends AbstractPlayerStore {
  private boolean weddingexStart = false;
  
  private static final int slots = 7;
  
  private boolean[] exitAfter;
  
  private int GameType = 0;
  
  private int storeid;
  
  int turn = 1;
  
  int piecetype = 0;
  
  int firstslot = 0;
  
  int tie = -1;
  
  int Partnerid = -1;
  
  private MapleCharacter player1;
  
  private MapleCharacter player2;
  
  public MarriageMiniBox(MapleCharacter owner, int itemId, String description, String pass, int GameType, int partnerid) {
    super(owner, itemId, description, pass, 7);
    this.Partnerid = partnerid;
    this.GameType = GameType;
    this.exitAfter = new boolean[8];
  }
  
  public MapleCharacter getPlayer1() {
    return this.player1;
  }
  
  public MapleCharacter getPlayer2() {
    return this.player2;
  }
  
  public void setPlayer1(MapleCharacter p) {
    this.player1 = p;
  }
  
  public void setPlayer2(MapleCharacter p) {
    this.player2 = p;
  }
  
  public int getPartnerId() {
    return this.Partnerid;
  }
  
  public byte getShopType() {
    return 8;
  }
  
  public final void setStoreid(int storeid) {
    this.storeid = storeid;
  }
  
  public void removeVisitor(MapleCharacter visitor) {
    byte slot = getVisitorSlot(visitor);
    if (slot > 0) {
      if (slot == 1) {
        closeMarriageBox(true, 24);
        return;
      } 
      broadcastToVisitors(PlayerShopPacket.shopVisitorLeave(slot), slot);
      this.chrs[slot - 1] = new WeakReference<>(null);
      update();
    } 
  }
  
  public void buy(MapleClient c, int z, short i) {}
  
  public void EndMarriage() {
    sendPackets(PlayerShopPacket.WEDDING_INTER(109));
    update();
    closeMarriageBox(false, 3);
    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
    Item item = ii.getEquipById(1112744);
    Item item1 = ii.getEquipById(1112744);
    item.setUniqueId(MapleInventoryIdentifier.getInstance());
    Equip eitem = (Equip)item;
    eitem.setStr((short)300);
    eitem.setDex((short)300);
    eitem.setInt((short)300);
    eitem.setLuk((short)300);
    eitem.setWatk((short)300);
    eitem.setMatk((short)300);
    item1.setUniqueId(MapleInventoryIdentifier.getInstance());
    Equip eitem1 = (Equip)item1;
    eitem1.setStr((short)300);
    eitem1.setDex((short)300);
    eitem1.setInt((short)300);
    eitem1.setLuk((short)300);
    eitem1.setWatk((short)300);
    eitem1.setMatk((short)300);
    try {
      MapleRing.makeRing(1112744, getPlayer1(), eitem.getUniqueId(), eitem1.getUniqueId());
      MapleRing.makeRing(1112744, getPlayer2(), eitem1.getUniqueId(), eitem.getUniqueId());
    } catch (Exception e) {
      e.printStackTrace();
    } 
    MapleInventoryManipulator.addbyItem(getPlayer1().getClient(), item);
    MapleInventoryManipulator.addbyItem(getPlayer2().getClient(), item1);
    getPlayer1().reloadChar();
    getPlayer2().reloadChar();
  }
  
  public void StartMarriage() {
    if (!getPlayer1().haveItem(5250500, 1)) {
      closeMarriageBox(true, 3);
      return;
    } 
    if (getPlayer1() == null || getPlayer1().getMarriageId() > 0 || 
      getPlayer2() == null || getPlayer2().getMarriageId() > 0 || 
      getPlayer2().getId() != getPartnerId() || 
      !getPlayer1().isAlive() || !getPlayer2().isAlive()) {
      closeMarriageBox(true, 3);
      return;
    } 
    if (!MapleInventoryManipulator.checkSpace(getPlayer1().getClient(), 1112744, 1, "") || !MapleInventoryManipulator.checkSpace(getPlayer2().getClient(), 1112744, 1, "")) {
      closeMarriageBox(true, 25);
      return;
    } 
    getPlayer1().removeItem(5250500, -1);
    MarriageDataEntry data = MarriageManager.getInstance().makeNewMarriage(1);
    data.setStatus(2);
    data.setWeddingStatus(8);
    data.setBrideId(getPlayer1().getId());
    data.setBrideName(getPlayer1().getName());
    data.setGroomId(getPlayer2().getId());
    data.setGroomName(getPlayer2().getName());
    getPlayer1().setMarriageId(data.getMarriageId());
    getPlayer2().setMarriageId(data.getMarriageId());
    setOpen(false);
    update();
    sendPackets(PlayerShopPacket.WEDDING_INTER(106));
  }
  
  public void sendPackets(byte[] packets) {
    getPlayer1().getClient().getSession().writeAndFlush(packets);
    for (int i = 0; i < this.chrs.length; i++) {
      MapleCharacter visitor = getVisitor(i);
      if (visitor != null)
        visitor.getClient().getSession().writeAndFlush(packets); 
    } 
  }
  
  public byte getVisitorSlot(MapleCharacter visitor) {
    for (byte i = 0; i < this.chrs.length; i = (byte)(i + 1)) {
      if (this.chrs[i] != null && this.chrs[i].get() != null && ((MapleCharacter)this.chrs[i].get()).getId() == visitor.getId())
        return (byte)(i + 1); 
    } 
    if (visitor.getId() == this.ownerId)
      return 0; 
    return -1;
  }
  
  public void removeAllVisitors(int error) {
    for (int i = 0; i < this.chrs.length; i++) {
      MapleCharacter visitor = getVisitor(i);
      if (visitor != null) {
        visitor.getClient().getSession().writeAndFlush(PlayerShopPacket.shopErrorMessage(error, i + 1));
        broadcastToVisitors(PlayerShopPacket.shopVisitorLeave(getVisitorSlot(visitor)), getVisitorSlot(visitor));
        visitor.setPlayerShop(null);
        this.chrs[i] = new WeakReference<>(null);
      } 
    } 
    update();
  }
  
  public void closeMarriageBox(boolean z, int error) {
    removeAllVisitors(error);
    if (z)
      getMCOwner().getClient().getSession().writeAndFlush(PlayerShopPacket.shopErrorMessage(error, 0)); 
    if (getMCOwner() != null)
      getMCOwner().setPlayerShop(null); 
    if (getPlayer1() != null)
      getPlayer1().setMarriage(null); 
    if (getPlayer2() != null)
      getPlayer2().setMarriage(null); 
    update();
    getMap().removeMapObject(this);
  }
  
  public void send(MapleClient c) {
    if (getMCOwner() == null) {
      closeMarriageBox(false, 3);
      return;
    } 
    c.getSession().writeAndFlush(MarriageEXPacket.MarriageRoom(c, this));
    update();
  }
  
  public final int getStoreId() {
    return this.storeid;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.HIRED_MERCHANT;
  }
  
  public void sendDestroyData(MapleClient client) {
    if (isAvailable())
      client.getSession().writeAndFlush(PlayerShopPacket.destroyHiredMerchant(getOwnerId())); 
  }
  
  public final void sendVisitor(MapleClient c) {
    c.getSession().writeAndFlush(PlayerShopPacket.MerchantVisitorView(this.visitors));
  }
  
  public void closeShop(boolean saveItems, boolean remove) {
    closeMarriageBox(false, 3);
  }
}
