package server.shops;

import client.MapleCharacter;
import client.MapleClient;
import java.util.List;
import tools.Pair;

public interface IMaplePlayerShop {
  public static final byte HIRED_MERCHANT = 1;
  
  public static final byte PLAYER_SHOP = 2;
  
  public static final byte OMOK = 3;
  
  public static final byte MATCH_CARD = 4;
  
  public static final byte MARRIAGE = 8;
  
  String getOwnerName();
  
  String getDescription();
  
  List<Pair<Byte, MapleCharacter>> getVisitors();
  
  List<MaplePlayerShopItem> getItems();
  
  boolean isOpen();
  
  boolean removeItem(int paramInt);
  
  boolean isOwner(MapleCharacter paramMapleCharacter);
  
  byte getShopType();
  
  byte getVisitorSlot(MapleCharacter paramMapleCharacter);
  
  byte getFreeSlot();
  
  int getItemId();
  
  long getMeso();
  
  int getOwnerId();
  
  int getOwnerAccId();
  
  void setOpen(boolean paramBoolean);
  
  void setMeso(long paramLong);
  
  void addItem(MaplePlayerShopItem paramMaplePlayerShopItem);
  
  void removeFromSlot(int paramInt);
  
  void broadcastToVisitors(byte[] paramArrayOfbyte);
  
  void addVisitor(MapleCharacter paramMapleCharacter);
  
  void removeVisitor(MapleCharacter paramMapleCharacter);
  
  void removeAllVisitors(int paramInt1, int paramInt2);
  
  void buy(MapleClient paramMapleClient, int paramInt, short paramShort);
  
  void closeShop(boolean paramBoolean1, boolean paramBoolean2);
  
  String getPassword();
  
  int getMaxSize();
  
  int getSize();
  
  int getGameType();
  
  void update();
  
  void setAvailable(boolean paramBoolean);
  
  boolean isAvailable();
  
  List<AbstractPlayerStore.BoughtItem> getBoughtItems();
  
  String getMemberNames();
}
