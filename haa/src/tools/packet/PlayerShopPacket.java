package tools.packet;

import client.AvatarLook;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Item;
import constants.GameConstants;
import handling.SendPacketOpcode;
import handling.channel.handler.PlayerInteractionHandler;
import java.util.List;
import server.MerchItemPackage;
import server.shops.IMaplePlayerShop;
import server.shops.MapleMiniGame;
import server.shops.MaplePlayerShop;
import server.shops.MaplePlayerShopItem;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class PlayerShopPacket {
  public static final byte[] removeCharBox(MapleCharacter c) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_BOX.getValue());
    mplew.writeInt(c.getId());
    mplew.write(0);
    return mplew.getPacket();
  }
  
  public static final byte[] sendPlayerShopBox(MapleCharacter c) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_BOX.getValue());
    mplew.writeInt(c.getId());
    PacketHelper.addAnnounceBox(mplew, c);
    return mplew.getPacket();
  }
  
  public static final byte[] getPlayerStore(MapleCharacter chr, boolean firstTime) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    IMaplePlayerShop ips = chr.getPlayerShop();
    mplew.write(5);
    switch (ips.getShopType()) {
      case 2:
        mplew.write(4);
        mplew.write(4);
        break;
      case 3:
        mplew.write(2);
        mplew.write(2);
        break;
      case 4:
        mplew.write(1);
        mplew.write(2);
        break;
    } 
    mplew.writeShort(ips.getVisitorSlot(chr));
    AvatarLook.encodeAvatarLook(mplew, ((MaplePlayerShop)ips).getMCOwner(), false, (GameConstants.isZero(chr.getJob()) && chr.getGender() == 1));
    mplew.writeMapleAsciiString(ips.getOwnerName());
    mplew.writeShort(((MaplePlayerShop)ips).getMCOwner().getJob());
    for (Pair<Byte, MapleCharacter> storechr : ips.getVisitors()) {
      mplew.write(((Byte)storechr.left).byteValue());
      AvatarLook.encodeAvatarLook(mplew, (MapleCharacter)storechr.right, false, (GameConstants.isZero(((MapleCharacter)storechr.right).getJob()) && ((MapleCharacter)storechr.right).getGender() == 1));
      mplew.writeMapleAsciiString(((MapleCharacter)storechr.right).getName());
      mplew.writeShort(((MapleCharacter)storechr.right).getJob());
    } 
    mplew.write(255);
    mplew.writeInt(2665);
    mplew.writeMapleAsciiString(ips.getDescription());
    mplew.write(16);
    mplew.write(ips.getItems().size());
    for (MaplePlayerShopItem item : ips.getItems()) {
      mplew.writeShort(item.bundles);
      mplew.writeShort(item.item.getQuantity());
      mplew.writeLong(item.price);
      PacketHelper.addItemInfo(mplew, item.item);
    } 
    return mplew.getPacket();
  }
  
  public static final byte[] shopChat(MapleCharacter chr, String name, int cid, String message, int slot) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.CHAT.action);
    mplew.write(25);
    mplew.write(slot);
    mplew.writeMapleAsciiString(chr.getName());
    mplew.writeMapleAsciiString(message);
    mplew.writeInt(cid);
    PacketHelper.ChatPacket(mplew, name, message);
    return mplew.getPacket();
  }
  
  public static final byte[] shopErrorMessage(int error, int type) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(28);
    mplew.write(type);
    mplew.write(error);
    return mplew.getPacket();
  }
  
  public static final byte[] WEDDING_INTER(int type) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(type);
    mplew.write(0);
    mplew.writeInt(2000000);
    mplew.writeInt(2000000);
    return mplew.getPacket();
  }
  
  public static final byte[] destroyHiredMerchant(int id) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.DESTROY_HIRED_MERCHANT.getValue());
    mplew.writeInt(id);
    return mplew.getPacket();
  }
  
  public static final byte[] shopItemUpdate(IMaplePlayerShop shop) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(75);
    if (shop.getShopType() == 1) {
      mplew.writeInt(0);
      mplew.writeInt(0);
    } 
    mplew.write(shop.getItems().size());
    for (MaplePlayerShopItem item : shop.getItems()) {
      mplew.writeShort(item.bundles);
      mplew.writeShort(item.item.getQuantity());
      mplew.writeLong(item.price);
      PacketHelper.addItemInfo(mplew, item.item);
    } 
    mplew.writeShort(0);
    return mplew.getPacket();
  }
  
  public static final byte[] shopVisitorAdd(MapleCharacter chr, int slot) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.VISIT.action);
    mplew.write(slot);
    AvatarLook.encodeAvatarLook(mplew, chr, false, (GameConstants.isZero(chr.getJob()) && chr.getGender() == 1));
    mplew.writeMapleAsciiString(chr.getName());
    mplew.writeShort(chr.getJob());
    mplew.writeInt(0);
    return mplew.getPacket();
  }
  
  public static final byte[] shopVisitorLeave(byte slot) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.EXIT.action);
    mplew.write(slot);
    return mplew.getPacket();
  }
  
  public static final byte[] Merchant_Buy_Error(byte message) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(44);
    mplew.write(message);
    return mplew.getPacket();
  }
  
  public static final byte[] merchantNameChange(int cid, String name) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.MERCHANT_NAME_CHANGE.getValue());
    mplew.write(17);
    mplew.writeInt(cid);
    mplew.writeInt(957);
    mplew.writeMapleAsciiString(name);
    mplew.write(5);
    mplew.write(1);
    mplew.write(7);
    return mplew.getPacket();
  }
  
  public static final byte[] merchItem_Message(int op) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.MERCH_ITEM_MSG.getValue());
    mplew.write(op);
    return mplew.getPacket();
  }
  
  public static final byte[] merchItemStore(byte op, int days, int fees) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.MERCH_ITEM_STORE.getValue());
    mplew.write(op);
    switch (op) {
      case 39:
        mplew.writeInt(999999999);
        mplew.writeInt(999999999);
        mplew.write(0);
        break;
      case 38:
        mplew.writeInt(days);
        mplew.writeInt(fees);
        break;
    } 
    return mplew.getPacket();
  }
  
  public static final byte[] merchItemStore_ItemData(MerchItemPackage pack) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.MERCH_ITEM_STORE.getValue());
    mplew.write(40);
    mplew.writeInt(9030000);
    mplew.writeInt(32272);
    mplew.writeZeroBytes(5);
    mplew.writeLong(pack.getMesos());
    mplew.writeZeroBytes(3);
    mplew.write(pack.getItems().size());
    for (Item item : pack.getItems())
      PacketHelper.addItemInfo(mplew, item); 
    mplew.writeShort(0);
    return mplew.getPacket();
  }
  
  public static final byte[] merchItemStore_ItemDataNone() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.MERCH_ITEM_STORE.getValue());
    mplew.write(42);
    mplew.writeInt(9030000);
    mplew.write(-1);
    mplew.writeInt(3906249);
    return mplew.getPacket();
  }
  
  public static final byte[] merchItemStore2PWCheck(byte type) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.MERCH_ITEM_STORE.getValue());
    mplew.write(39);
    mplew.write(type);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGame(MapleClient c, MapleMiniGame minigame) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(20);
    mplew.write(minigame.getGameType());
    mplew.write(minigame.getMaxSize());
    mplew.writeShort(minigame.getVisitorSlot(c.getPlayer()));
    AvatarLook.encodeAvatarLook(mplew, minigame.getMCOwner(), false, (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1));
    mplew.writeMapleAsciiString(minigame.getOwnerName());
    mplew.writeShort(minigame.getMCOwner().getJob());
    mplew.writeInt(0);
    for (Pair<Byte, MapleCharacter> visitorz : minigame.getVisitors()) {
      mplew.write(((Byte)visitorz.getLeft()).byteValue());
      AvatarLook.encodeAvatarLook(mplew, visitorz.getRight(), false, (GameConstants.isZero(((MapleCharacter)visitorz.right).getJob()) && ((MapleCharacter)visitorz.right).getGender() == 1));
      mplew.writeMapleAsciiString(((MapleCharacter)visitorz.getRight()).getName());
      mplew.writeShort(((MapleCharacter)visitorz.getRight()).getJob());
      mplew.writeInt(0);
    } 
    mplew.write(-1);
    mplew.write(0);
    addGameInfo(mplew, minigame.getMCOwner(), minigame);
    for (Pair<Byte, MapleCharacter> visitorz : minigame.getVisitors()) {
      mplew.write(((Byte)visitorz.getLeft()).byteValue());
      addGameInfo(mplew, visitorz.getRight(), minigame);
    } 
    mplew.write(-1);
    mplew.writeMapleAsciiString(minigame.getDescription());
    mplew.writeShort(minigame.getPieceType());
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameReady(boolean ready) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(ready ? PlayerInteractionHandler.Interaction.READY.action : PlayerInteractionHandler.Interaction.UN_READY.action);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameInfoMsg(byte type, String name) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.CHAT.action);
    mplew.write(23);
    mplew.write(type);
    mplew.writeMapleAsciiString(name);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameStart(int loser) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.START.action);
    mplew.write((loser == 1) ? 0 : 1);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameSkip(int slot) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.SKIP.action);
    mplew.write(slot);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameRequestTie() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.REQUEST_TIE.action);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameDenyTie() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.ANSWER_TIE.action);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameRequestRedo() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.REQUEST_REDO.action);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameDenyRedo() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.ANSWER_REDO.action);
    mplew.writeLong(0L);
    mplew.writeLong(0L);
    mplew.writeLong(0L);
    mplew.writeLong(0L);
    mplew.writeLong(0L);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameFull() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(5);
    mplew.write(0);
    mplew.write(2);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameMoveOmok(int move1, int move2, int move3) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.MOVE_OMOK.action);
    mplew.writeInt(move1);
    mplew.writeInt(move2);
    mplew.write(move3);
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameNewVisitor(MapleCharacter c, int slot, MapleMiniGame game) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.VISIT.action);
    mplew.write(slot);
    AvatarLook.encodeAvatarLook(mplew, c, false, (GameConstants.isZero(c.getJob()) && c.getGender() == 1));
    mplew.writeMapleAsciiString(c.getName());
    mplew.writeShort(c.getJob());
    mplew.writeInt(0);
    addGameInfo(mplew, c, game);
    return mplew.getPacket();
  }
  
  public static void addGameInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, MapleMiniGame game) {
    mplew.writeInt(game.getGameType());
    mplew.writeInt(game.getWins(chr));
    mplew.writeInt(game.getTies(chr));
    mplew.writeInt(game.getLosses(chr));
    mplew.writeInt(game.getScore(chr));
  }
  
  public static byte[] getMatchCardStart(MapleMiniGame game, int loser) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.START.action);
    mplew.write((loser == 1) ? 0 : 1);
    int times = (game.getPieceType() == 1) ? 20 : ((game.getPieceType() == 2) ? 30 : 12);
    mplew.write(times);
    for (int i = 1; i <= times; i++)
      mplew.writeInt(game.getCardId(i)); 
    return mplew.getPacket();
  }
  
  public static byte[] getMatchCardSelect(int turn, int slot, int firstslot, int type) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(103);
    mplew.write(turn);
    mplew.write(slot);
    if (turn == 0) {
      mplew.write(firstslot);
      mplew.write(type);
    } 
    return mplew.getPacket();
  }
  
  public static byte[] getMiniGameResult(MapleMiniGame game, int type, int x) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.GAME_RESULT.action);
    mplew.write(type);
    game.setPoints(x, type);
    if (type != 0)
      game.setPoints((x == 1) ? 0 : 1, (type == 2) ? 0 : 1); 
    if (type != 1)
      if (type == 0) {
        mplew.write((x == 1) ? 0 : 1);
      } else {
        mplew.write(x);
      }  
    addGameInfo(mplew, game.getMCOwner(), game);
    for (Pair<Byte, MapleCharacter> visitorz : game.getVisitors())
      addGameInfo(mplew, (MapleCharacter)visitorz.right, game); 
    return mplew.getPacket();
  }
  
  public static final byte[] MerchantClose(int error, int type) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.CLOSE_MERCHANT.action + 1);
    mplew.write(type);
    mplew.write(error);
    return mplew.getPacket();
  }
  
  public static final byte[] MerchantBlackListView(List<String> blackList) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.VIEW_MERCHANT_BLACKLIST.action);
    mplew.writeShort(blackList.size());
    for (String visit : blackList)
      mplew.writeMapleAsciiString(visit); 
    return mplew.getPacket();
  }
  
  public static final byte[] MerchantVisitorView(List<String> visitor) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.VIEW_MERCHANT_VISITOR.action);
    mplew.writeShort(visitor.size());
    for (String visit : visitor) {
      mplew.writeMapleAsciiString(visit);
      mplew.writeInt(1);
    } 
    return mplew.getPacket();
  }
  
  public static byte[] StartRPS() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.START.action);
    return mplew.getPacket();
  }
  
  public static byte[] FinishRPS(byte result, byte rps) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(113);
    mplew.write(result);
    mplew.write(rps);
    return mplew.getPacket();
  }
}
