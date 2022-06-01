/*      */ package handling.auction.handler;
/*      */ 
/*      */ import client.MapleCharacter;
/*      */ import client.MapleClient;
/*      */ import client.inventory.AuctionHistory;
/*      */ import client.inventory.AuctionItem;
/*      */ import client.inventory.Equip;
/*      */ import client.inventory.Item;
/*      */ import client.inventory.ItemFlag;
/*      */ import client.inventory.MapleInventoryType;
/*      */ import constants.GameConstants;
/*      */ import constants.ServerConstants;
/*      */ import handling.auction.AuctionServer;
/*      */ import handling.channel.ChannelServer;
/*      */ import handling.channel.handler.PlayerHandler;
/*      */ import handling.login.LoginServer;
/*      */ import handling.world.CharacterTransfer;
/*      */ import handling.world.MapleMessengerCharacter;
/*      */ import handling.world.PlayerBuffStorage;
/*      */ import handling.world.World;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import server.MapleInventoryManipulator;
/*      */ import server.MapleItemInformationProvider;
/*      */ import server.games.BattleReverse;
/*      */ import server.games.OneCardGame;
/*      */ import tools.FileoutputUtil;
/*      */ import tools.StringUtil;
/*      */ import tools.data.LittleEndianAccessor;
/*      */ import tools.packet.CField;
/*      */ import tools.packet.CWvsContext;
/*      */ import tools.packet.PacketHelper;
/*      */ 
/*      */ public class AuctionHandler {
/*      */   public static void LeaveAuction(MapleClient c, MapleCharacter chr) {
/*   38 */     AuctionServer.getPlayerStorage().deregisterPlayer(chr);
/*   39 */     c.updateLoginState(1, c.getSessionIPAddress());
/*      */     
/*      */     try {
/*   42 */       PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
/*   43 */       PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
/*   44 */       World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
/*   45 */       c.getSession().writeAndFlush(CField.getChannelChange(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
/*      */     } finally {
/*   47 */       String s = c.getSessionIPAddress();
/*   48 */       LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
/*   49 */       chr.saveToDB(true, false);
/*   50 */       c.setPlayer(null);
/*      */       
/*   52 */       c.setAuction(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void EnterAuction(MapleCharacter chr, MapleClient client) {
    if (chr.getMapId() != ServerConstants.warpMap) {
    chr.dropMessage(1, "경매장은 마을에서만 이용 가능합니다.");
     return;
    }
/*   61 */     chr.changeRemoval();
/*   62 */     ChannelServer ch = ChannelServer.getInstance(client.getChannel());
/*   63 */     if (chr.getMessenger() != null) {
/*   64 */       World.Messenger.silentLeaveMessenger(chr.getMessenger().getId(), new MapleMessengerCharacter(chr));
/*      */     }
/*   66 */     PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
/*   67 */     PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
/*   68 */     World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -20);
/*   69 */     ch.removePlayer(chr);
/*      */     
/*   71 */     client.setAuction(true);
/*   72 */     client.updateLoginState(3, client.getSessionIPAddress());
/*   73 */     String s = client.getSessionIPAddress();
/*   74 */     LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
/*   75 */     client.getSession().writeAndFlush(CField.enterAuction(chr));
/*      */     
/*   77 */     chr.saveToDB(true, false);
/*   78 */     chr.getMap().removePlayer(chr);
/*      */ 
      /*   if (chr.getClient().getLIEDETECT() != null) {
      PlayerHandler.EnterTrueRoom(chr.getClient());
    }*/
/*   84 */     if (OneCardGame.oneCardMatchingQueue.contains(chr)) {
/*   85 */       OneCardGame.oneCardMatchingQueue.remove(chr);
/*      */     }
/*      */     
/*   88 */     if (BattleReverse.BattleReverseMatchingQueue.contains(chr))
/*   89 */       BattleReverse.BattleReverseMatchingQueue.remove(chr);  } public static final void Handle(LittleEndianAccessor slea, MapleClient c) { CharacterTransfer transfer; int nAuctionType; long l1; int dwAuctionID; long dwInventoryId; List<AuctionItem> searchItems; int j; List<Integer> wishlist; int dwAuctionId; List<AuctionItem> sellingItems, completeItems; MapleCharacter chr; int nItemID; AuctionItem aItem; long nPrice; int searchType; AuctionItem auctionItem1; List<AuctionItem> wishItems; AuctionItem item; List<AuctionItem> list1; int nNumber, k; String nameWithSpace; int i; Iterator<Integer> iterator; List<AuctionItem> list2; long l2; int m, nCount, dwAccountId; String nameWithoutSpace; List<AuctionItem> marketPriceItems; int i1, n; AuctionItem auctionItem2; int dwCharacterId; List<AuctionItem> recentlySellItems; int nEndHour, nItemId; List<Integer> list3; byte nTI; int nState; List<AuctionItem> list4; int nItemPos; long l3; int i2; Item source, target;
/*      */     long nBuyTime;
/*      */     AuctionItem auctionItem3;
/*      */     int deposit, i3, nWorldId;
/*      */     AuctionItem auctionItem5, auctionItem4;
/*   94 */     int op = slea.readInt();
/*   95 */     Map<Integer, AuctionItem> items = AuctionServer.getItems();
/*   96 */     MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
/*   97 */     switch (op) {
/*      */       
/*      */       case 0:
/*  100 */         transfer = AuctionServer.getPlayerStorage().getPendingCharacter(c.getPlayer().getId());
/*  101 */         chr = MapleCharacter.ReconstructChr(transfer, c, false);
/*      */         
/*  103 */         c.setPlayer(chr);
/*  104 */         c.setAccID(chr.getAccountID());
/*      */         
/*  106 */         if (!c.CheckIPAddress()) {
/*  107 */           c.getSession().close();
/*      */           
/*      */           return;
/*      */         } 
/*  111 */         chr.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(chr.getId()));
/*  112 */         chr.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(chr.getId()));
/*      */         
/*  114 */         World.isCharacterListConnected(c.getPlayer().getName(), c.loadCharacterNames(c.getWorld()));
/*  115 */         c.updateLoginState(2, c.getSessionIPAddress());
/*  116 */         AuctionServer.getPlayerStorage().registerPlayer(chr);
/*      */         
/*  118 */         list1 = new ArrayList<>();
/*  119 */         list2 = new ArrayList<>();
/*  120 */         marketPriceItems = new ArrayList<>();
/*  121 */         recentlySellItems = new ArrayList<>();
/*  122 */         list3 = new ArrayList<>();
/*  123 */         list4 = new ArrayList<>();
/*      */         
/*  125 */         for (i2 = 0; i2 < 10; i2++) {
/*  126 */           String wish = c.getKeyValue("wish" + i2);
/*  127 */           if (wish != null) {
/*  128 */             list3.add(Integer.valueOf(Integer.parseInt(wish)));
/*      */           }
/*      */         } 
/*      */         
/*  132 */         for (Map.Entry<Integer, AuctionItem> itemz : items.entrySet()) {
/*  133 */           AuctionItem auctionItem = itemz.getValue();
/*  134 */           if ((auctionItem.getEndDate() < System.currentTimeMillis() || auctionItem.getState() >= 2) && ((
/*  135 */             auctionItem.getState() == 2 && auctionItem.getBidUserId() == c.getPlayer().getId()) || ((auctionItem.getState() == 3 || auctionItem.getState() == 4) && auctionItem.getAccountId() == c.getAccID()))) {
/*  136 */             list1.add(auctionItem);
/*      */           }
/*      */ 
/*      */           
/*  140 */           if (auctionItem.getAccountId() == c.getAccID() && auctionItem.getState() == 0) {
/*  141 */             list2.add(auctionItem);
/*      */           }
/*      */           
/*  144 */           if (auctionItem.getState() == 0 && recentlySellItems.size() < 1000)
/*      */           {
/*  146 */             recentlySellItems.add(auctionItem);
/*      */           }
/*      */           
/*  149 */           if ((auctionItem.getState() == 3 || auctionItem.getState() == 8) && marketPriceItems.size() < 1000) {
/*  150 */             marketPriceItems.add(auctionItem);
/*      */           }
/*      */           
/*  153 */           for (Iterator<Integer> iterator1 = list3.iterator(); iterator1.hasNext(); ) { int auctionId = ((Integer)iterator1.next()).intValue();
/*  154 */             if (auctionItem.getAuctionId() == auctionId) {
/*  155 */               list4.add(auctionItem);
/*      */             } }
/*      */         
/*      */         } 
/*      */         
/*  160 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItems(list1));
/*  161 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellingMyItems(list2));
/*  162 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlist(list4));
/*  163 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionOn());
/*  164 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionMarketPrice(marketPriceItems));
/*  165 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSearchItems(recentlySellItems));
/*  166 */         FileoutputUtil.log(FileoutputUtil.경매장입장로그, "[입장] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName());
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 10:
/*  174 */         nAuctionType = slea.readInt();
/*  175 */         nItemID = slea.readInt();
/*  176 */         nNumber = slea.readInt();
/*  177 */         l2 = slea.readLong();
/*      */         
/*  179 */         nEndHour = slea.readInt();
/*  180 */         nTI = slea.readByte();
/*  181 */         nItemPos = slea.readInt();
/*      */         
/*  183 */         source = c.getPlayer().getInventory(nTI).getItem((short)nItemPos);
/*      */         
/*  185 */         if (source == null || source.getItemId() != nItemID || source.getQuantity() < nNumber || nNumber < 0 || l2 < 0L) {
/*  186 */           System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
/*  187 */           c.getSession().close(); return;
/*      */         } 
/*  189 */         if (nNumber <= 0) {
/*  190 */           System.out.println("quantity 0이하");
/*  191 */           c.getSession().close();
/*      */           return;
/*      */         } 
/*  194 */         if (source.getInventoryId() <= 0L) {
/*  195 */           System.out.println("inventoryId : " + source.getInventoryId());
/*      */           return;
/*      */         } 
/*  198 */         target = source.copy();
/*      */         
/*  200 */         MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.getByType(nTI), (short)nItemPos, (short)nNumber, false);
/*      */         
/*  202 */         target.setQuantity((short)nNumber);
/*      */         
/*  204 */         if (target.getInventoryId() <= 0L) {
/*  205 */           System.out.println("inventoryId : " + target.getInventoryId());
/*      */           
/*      */           return;
/*      */         } 
/*  209 */         auctionItem3 = new AuctionItem();
/*  210 */         auctionItem3.setAuctionType(nAuctionType);
/*  211 */         auctionItem3.setItem(target);
/*  212 */         if (GameConstants.getInventoryType(auctionItem3.getItem().getItemId()) == MapleInventoryType.CASH && nNumber > 1) {
/*  213 */           auctionItem3.setPrice(l2);
/*  214 */           auctionItem3.setDirectPrice(l2);
/*      */         } else {
/*  216 */           auctionItem3.setPrice(l2);
/*  217 */           auctionItem3.setDirectPrice(l2 * nNumber);
/*      */         } 
/*  219 */         auctionItem3.setEndDate(System.currentTimeMillis() + (nEndHour * 60 * 60 * 1000));
/*  220 */         auctionItem3.setRegisterDate(System.currentTimeMillis());
/*  221 */         auctionItem3.setAccountId(c.getAccID());
/*  222 */         auctionItem3.setCharacterId(c.getPlayer().getId());
/*  223 */         auctionItem3.setState(0);
/*  224 */         auctionItem3.setWorldId(c.getWorld());
/*  225 */         auctionItem3.setName(c.getPlayer().getName());
/*  226 */         auctionItem3.setAuctionId(AuctionItemIdentifier.getInstance());
/*  227 */         items.put(Integer.valueOf(auctionItem3.getAuctionId()), auctionItem3);
/*  228 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellItemUpdate(auctionItem3));
/*  229 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellItem(auctionItem3));
/*  230 */         FileoutputUtil.log(FileoutputUtil.경매장판매등록로그, "[아이템등록] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 이름 : " + ii.getName(auctionItem3.getItem().getItemId()) + " | 아이템 코드 : " + auctionItem3.getItem().getItemId() + " | 수량 " + auctionItem3.getItem().getQuantity() + " | 판매 메소 : " + l2);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 11:
/*  236 */         l1 = slea.readLong();
/*  237 */         k = slea.readInt();
/*  238 */         m = slea.readInt();
/*  239 */         i1 = slea.readInt();
/*  240 */         nItemId = slea.readInt();
/*  241 */         nState = slea.readInt();
/*  242 */         l3 = slea.readLong();
/*  243 */         nBuyTime = slea.readLong();
/*  244 */         deposit = slea.readInt();
/*  245 */         deposit = slea.readInt();
/*  246 */         i3 = slea.readInt();
/*  247 */         nWorldId = slea.readInt();
/*      */         
/*  249 */         if (i3 < 0 || i3 > 32767 || l3 < 0L) {
/*  250 */           System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
/*  251 */           c.getSession().close();
/*      */           
/*      */           return;
/*      */         } 
/*  255 */         auctionItem5 = items.get(Integer.valueOf(k));
/*  256 */         if (auctionItem5 != null && auctionItem5.getItem() != null && auctionItem5.getHistory() != null) {
/*      */           
/*  258 */           AuctionHistory history = auctionItem5.getHistory();
/*      */           
/*  260 */           if (history.getId() != l1) {
/*  261 */             System.out.println("return 1");
/*      */             
/*      */             return;
/*      */           } 
/*  265 */           if (history.getAuctionId() != k) {
/*  266 */             System.out.println("return 2");
/*      */             
/*      */             return;
/*      */           } 
/*  270 */           if (history.getAccountId() != m) {
/*  271 */             System.out.println("return 3");
/*      */             
/*      */             return;
/*      */           } 
/*  275 */           if (history.getCharacterId() != i1) {
/*  276 */             System.out.println("return 4");
/*      */             
/*      */             return;
/*      */           } 
/*  280 */           if (history.getItemId() != nItemId) {
/*  281 */             System.out.println("return 5");
/*      */             
/*      */             return;
/*      */           } 
/*  285 */           if (history.getState() != nState) {
/*  286 */             System.out.println("return 6");
/*      */             
/*      */             return;
/*      */           } 
/*  290 */           if (history.getPrice() != l3) {
/*  291 */             System.out.println("return 7");
/*      */             
/*      */             return;
/*      */           } 
/*  295 */           if (PacketHelper.getTime(history.getBuyTime()) != nBuyTime) {
/*  296 */             System.out.println("return 8");
/*      */             
/*      */             return;
/*      */           } 
/*  300 */           if (history.getDeposit() != deposit) {
/*  301 */             System.out.println("return 9");
/*      */             
/*      */             return;
/*      */           } 
/*  305 */           if (history.getQuantity() != i3) {
/*  306 */             System.out.println("return 10");
/*      */             
/*      */             return;
/*      */           } 
/*  310 */           if (history.getWorldId() != nWorldId) {
/*  311 */             System.out.println("return 11");
/*      */             
/*      */             return;
/*      */           } 
/*  315 */           auctionItem5.setEndDate(System.currentTimeMillis() + 86400000L);
/*  316 */           auctionItem5.setRegisterDate(System.currentTimeMillis());
/*      */           
/*  318 */           auctionItem5.setState(9);
/*  319 */           history.setState(9);
/*  320 */           Item item1 = auctionItem5.getItem();
/*  321 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(auctionItem5));
/*      */           
/*  323 */           AuctionItem aItem2 = new AuctionItem();
/*  324 */           aItem2.setAuctionType(auctionItem5.getAuctionType());
/*  325 */           aItem2.setItem(item1);
/*  326 */           aItem2.setPrice(auctionItem5.getDirectPrice());
/*  327 */           aItem2.setSecondPrice(0L);
/*  328 */           aItem2.setDirectPrice(auctionItem5.getDirectPrice());
/*  329 */           aItem2.setEndDate(System.currentTimeMillis() + 86400000L);
/*  330 */           aItem2.setRegisterDate(System.currentTimeMillis());
/*  331 */           aItem2.setAccountId(c.getAccID());
/*  332 */           aItem2.setCharacterId(c.getPlayer().getId());
/*  333 */           aItem2.setState(0);
/*  334 */           aItem2.setWorldId(c.getWorld());
/*  335 */           aItem2.setName(c.getPlayer().getName());
/*  336 */           aItem2.setAuctionId(AuctionItemIdentifier.getInstance());
/*  337 */           items.put(Integer.valueOf(aItem2.getAuctionId()), aItem2);
/*      */           
/*  339 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellItemUpdate(aItem2));
/*  340 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionReSellItem(aItem2));
/*  341 */           FileoutputUtil.log(FileoutputUtil.경매장판매등록로그, "[아이템재등록] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 이름 : " + ii.getName(auctionItem5.getItem().getItemId()) + " | 아이템 코드 : " + auctionItem5.getItem().getItemId() + " | 수량 " + auctionItem5.getItem().getQuantity() + " | 판매 메소 : " + auctionItem5.getDirectPrice());
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 12:
/*  348 */         dwAuctionID = slea.readInt();
/*  349 */         aItem = items.get(Integer.valueOf(dwAuctionID));
/*  350 */         if (aItem != null && aItem.getItem() != null) {
/*      */           
/*  352 */           if (aItem.getState() != 0) {
/*      */             return;
/*      */           }
/*      */           
/*  356 */           aItem.setState(4);
/*  357 */           aItem.setPrice(0L);
/*  358 */           aItem.setSecondPrice(-1L);
/*      */           
/*  360 */           AuctionHistory history = new AuctionHistory();
/*      */           
/*  362 */           history.setAuctionId(aItem.getAuctionId());
/*  363 */           history.setAccountId(aItem.getAccountId());
/*  364 */           history.setCharacterId(aItem.getCharacterId());
/*  365 */           history.setItemId(aItem.getItem().getItemId());
/*  366 */           history.setState(aItem.getState());
/*  367 */           history.setPrice(aItem.getPrice());
/*  368 */           history.setBuyTime(System.currentTimeMillis());
/*  369 */           history.setDeposit(aItem.getDeposit());
/*  370 */           history.setQuantity(aItem.getItem().getQuantity());
/*  371 */           history.setWorldId(aItem.getWorldId());
/*  372 */           history.setId(AuctionHistoryIdentifier.getInstance());
/*  373 */           aItem.setHistory(history);
/*      */           
/*  375 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellItemUpdate(aItem));
/*  376 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(aItem));
/*  377 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionStopSell(aItem));
/*  378 */           FileoutputUtil.log(FileoutputUtil.경매장판매중지로그, "[판매중지] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 이름 : " + ii.getName(aItem.getItem().getItemId()) + " | 아이템 코드 : " + aItem.getItem().getItemId() + " | 갯수 : " + aItem.getItem().getQuantity());
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 20:
/*      */       case 21:
/*  384 */         dwAuctionID = slea.readInt();
/*  385 */         nPrice = slea.readLong();
/*  386 */         nCount = 1;
/*  387 */         if (op == 21) {
/*  388 */           nCount = slea.readInt();
/*      */         }
/*      */         
/*  391 */         for (n = 0; n < 10; n++) {
/*  392 */           String wish = c.getKeyValue("wish" + n);
/*  393 */           if (wish != null && wish.equals(String.valueOf(dwAuctionID))) {
/*  394 */             c.removeKeyValue("wish" + n);
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*  399 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlistUpdate(dwAuctionID));
/*      */         
/*  401 */         if (nPrice < 0L || nCount < 0 || nCount > 32767) {
/*  402 */           System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
/*  403 */           c.getSession().close(); return;
/*      */         } 
/*  405 */         if (c.getPlayer().getMeso() < nPrice) {
/*  406 */           if (op == 20) {
/*  407 */             c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyEquipResult(106, dwAuctionID)); break;
/*      */           } 
/*  409 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyItemResult(106, dwAuctionID));
/*      */           break;
/*      */         } 
/*  412 */         auctionItem2 = items.get(Integer.valueOf(dwAuctionID));
/*  413 */         if (op == 20 && auctionItem2.getItem() != null) {
/*  414 */           nCount = auctionItem2.getItem().getQuantity();
/*      */         }
/*  416 */         if (auctionItem2 != null && auctionItem2.getItem() != null && auctionItem2.getItem().getQuantity() >= nCount) {
/*  417 */           if (auctionItem2.getAccountId() == c.getAccID()) {
/*  418 */             c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "[알림] 자신이 올린 아이템은 구매 할 수 없습니다."));
/*      */             return;
/*      */           } 
/*  421 */           if (auctionItem2.getCharacterId() == c.getPlayer().getId() || auctionItem2.getState() != 0) {
/*      */             return;
/*      */           }
/*      */           
/*  425 */           if (auctionItem2.getPrice() * nCount != nPrice) {
/*  426 */             c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "[알림] 등록 가격과 구매 가격이 일치하지 않습니다."));
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  431 */           c.getPlayer().gainMeso(-nPrice, false);
/*      */           
/*  433 */           Item item1 = auctionItem2.getItem();
/*      */           
/*  435 */           Item item2 = item1.copy();
/*  436 */           item1.setQuantity((short)(item1.getQuantity() - nCount));
/*  437 */           item2.setQuantity((short)nCount);
/*      */           
/*  439 */           if (item1.getQuantity() <= 0) {
/*  440 */             item1.setQuantity((short)nCount);
/*      */             
/*  442 */             auctionItem2.setState(3);
/*  443 */             auctionItem2.setBidUserId(c.getPlayer().getId());
/*  444 */             auctionItem2.setBidUserName(c.getPlayer().getName());
/*  445 */             auctionItem2.setPrice(nPrice);
/*      */             
/*  447 */             AuctionHistory auctionHistory = new AuctionHistory();
/*      */             
/*  449 */             auctionHistory.setAuctionId(auctionItem2.getAuctionId());
/*  450 */             auctionHistory.setAccountId(auctionItem2.getAccountId());
/*  451 */             auctionHistory.setCharacterId(auctionItem2.getCharacterId());
/*  452 */             auctionHistory.setItemId(auctionItem2.getItem().getItemId());
/*  453 */             auctionHistory.setState(auctionItem2.getState());
/*  454 */             auctionHistory.setPrice(auctionItem2.getPrice());
/*  455 */             auctionHistory.setBuyTime(System.currentTimeMillis());
/*  456 */             auctionHistory.setDeposit(auctionItem2.getDeposit());
/*  457 */             auctionHistory.setQuantity(auctionItem2.getItem().getQuantity());
/*  458 */             auctionHistory.setWorldId(auctionItem2.getWorldId());
/*  459 */             auctionHistory.setId(AuctionHistoryIdentifier.getInstance());
/*  460 */             auctionHistory.setBidUserId(c.getPlayer().getId());
/*  461 */             auctionHistory.setBidUserName(c.getPlayer().getName());
/*  462 */             auctionItem2.setHistory(auctionHistory);
/*      */             
/*  464 */             c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyItemUpdate(auctionItem2, false));
/*  465 */             FileoutputUtil.log(FileoutputUtil.경매장구매로그, "[구입] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 이름 : " + ii.getName(auctionItem2.getItem().getItemId()) + " | 아이템 코드 : " + auctionItem2.getItem().getItemId() + " | 수량 " + auctionItem2.getItem().getQuantity() + " | 구매 메소 : " + nPrice + " | 해당 아이템 판매자 이름 : " + auctionItem2.getName());
/*      */           } else {
/*      */             
/*  468 */             c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyItemUpdate(auctionItem2, true));
/*      */ 
/*      */             
/*  471 */             AuctionItem auctionItem6 = new AuctionItem();
/*  472 */             auctionItem6.setAuctionType(auctionItem2.getAuctionType());
/*  473 */             auctionItem6.setItem(item2);
/*  474 */             auctionItem6.setPrice(nPrice);
/*  475 */             auctionItem6.setDirectPrice(nPrice);
/*  476 */             auctionItem6.setEndDate(auctionItem2.getEndDate());
/*  477 */             auctionItem6.setRegisterDate(auctionItem2.getRegisterDate());
/*  478 */             auctionItem6.setAccountId(auctionItem2.getAccountId());
/*  479 */             auctionItem6.setCharacterId(auctionItem2.getCharacterId());
/*  480 */             auctionItem6.setState(3);
/*  481 */             auctionItem6.setWorldId(auctionItem2.getWorldId());
/*  482 */             auctionItem6.setName(auctionItem2.getName());
/*  483 */             auctionItem6.setBidUserId(c.getPlayer().getId());
/*  484 */             auctionItem6.setBidUserName(c.getPlayer().getName());
/*      */             
/*  486 */             auctionItem6.setAuctionId(AuctionItemIdentifier.getInstance());
/*      */ 
/*      */             
/*  489 */             AuctionHistory auctionHistory = new AuctionHistory();
/*      */             
/*  491 */             auctionHistory.setAuctionId(auctionItem6.getAuctionId());
/*  492 */             auctionHistory.setAccountId(auctionItem6.getAccountId());
/*  493 */             auctionHistory.setCharacterId(auctionItem6.getCharacterId());
/*  494 */             auctionHistory.setItemId(auctionItem6.getItem().getItemId());
/*  495 */             auctionHistory.setState(auctionItem6.getState());
/*  496 */             auctionHistory.setPrice(auctionItem6.getPrice());
/*  497 */             auctionHistory.setBuyTime(System.currentTimeMillis());
/*  498 */             auctionHistory.setDeposit(auctionItem6.getDeposit());
/*  499 */             auctionHistory.setQuantity(auctionItem6.getItem().getQuantity());
/*  500 */             auctionHistory.setWorldId(auctionItem6.getWorldId());
/*  501 */             auctionHistory.setId(AuctionHistoryIdentifier.getInstance());
/*  502 */             auctionHistory.setBidUserId(c.getPlayer().getId());
/*  503 */             auctionHistory.setBidUserName(c.getPlayer().getName());
/*  504 */             auctionItem6.setHistory(auctionHistory);
/*      */             
/*  506 */             items.put(Integer.valueOf(auctionItem6.getAuctionId()), auctionItem6);
/*  507 */             FileoutputUtil.log(FileoutputUtil.경매장구매로그, "[구입] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 이름 : " + ii.getName(auctionItem2.getItem().getItemId()) + " | 아이템 코드 : " + auctionItem2.getItem().getItemId() + " | 수량 " + auctionItem2.getItem().getQuantity() + " | 구매 메소 : " + nPrice + " | 해당 아이템 판매자 이름 : " + auctionItem2.getName());
/*      */           } 
/*      */ 
/*      */           
/*  511 */           AuctionItem auctionItem = new AuctionItem();
/*  512 */           auctionItem.setAuctionType(auctionItem2.getAuctionType());
/*  513 */           auctionItem.setItem(item2);
/*  514 */           auctionItem.setPrice(nPrice);
/*  515 */           auctionItem.setDirectPrice(auctionItem2.getDirectPrice());
/*  516 */           auctionItem.setEndDate(auctionItem2.getEndDate());
/*  517 */           auctionItem.setRegisterDate(auctionItem2.getRegisterDate());
/*  518 */           auctionItem.setAccountId(auctionItem2.getAccountId());
/*  519 */           auctionItem.setCharacterId(auctionItem2.getCharacterId());
/*  520 */           auctionItem.setState(2);
/*  521 */           auctionItem.setWorldId(auctionItem2.getWorldId());
/*  522 */           auctionItem.setName(auctionItem2.getName());
/*  523 */           auctionItem.setBidUserId(c.getPlayer().getId());
/*  524 */           auctionItem.setBidUserName(c.getPlayer().getName());
/*      */           
/*  526 */           auctionItem.setAuctionId(AuctionItemIdentifier.getInstance());
/*      */ 
/*      */           
/*  529 */           AuctionHistory history = new AuctionHistory();
/*      */           
/*  531 */           history.setAuctionId(auctionItem.getAuctionId());
/*  532 */           history.setAccountId(auctionItem.getAccountId());
/*  533 */           history.setCharacterId(auctionItem.getCharacterId());
/*  534 */           history.setItemId(auctionItem.getItem().getItemId());
/*  535 */           history.setState(auctionItem.getState());
/*  536 */           history.setPrice(auctionItem.getPrice());
/*  537 */           history.setBuyTime(System.currentTimeMillis());
/*  538 */           history.setDeposit(auctionItem.getDeposit());
/*  539 */           history.setQuantity(auctionItem.getItem().getQuantity());
/*  540 */           history.setWorldId(auctionItem.getWorldId());
/*  541 */           history.setId(AuctionHistoryIdentifier.getInstance());
/*  542 */           history.setBidUserId(c.getPlayer().getId());
/*  543 */           history.setBidUserName(c.getPlayer().getName());
/*  544 */           auctionItem.setHistory(history);
/*      */           
/*  546 */           items.put(Integer.valueOf(auctionItem.getAuctionId()), auctionItem);
/*      */           
/*  548 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(auctionItem, item2));
/*  549 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyItemResult(0, dwAuctionID));
/*      */           
/*  551 */           int ch = World.Find.findAccChannel(auctionItem2.getAccountId());
/*  552 */           if (ch >= 0) {
/*  553 */             MapleClient ac = AuctionServer.getPlayerStorage().getClientById(auctionItem2.getAccountId());
/*  554 */             if (ac == null) {
/*  555 */               ac = ChannelServer.getInstance(ch).getPlayerStorage().getClientById(auctionItem2.getAccountId());
/*      */             }
/*  557 */             if (ac != null) {
/*  558 */               ac.getSession().writeAndFlush(CWvsContext.AlarmAuction(0, auctionItem));
/*      */             }
/*      */           } 
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 30:
/*  567 */         dwInventoryId = slea.readLong();
/*  568 */         k = slea.readInt();
/*  569 */         dwAccountId = slea.readInt();
/*  570 */         dwCharacterId = slea.readInt();
/*  571 */         nItemId = slea.readInt();
/*  572 */         nState = slea.readInt();
/*  573 */         l3 = slea.readLong();
/*  574 */         nBuyTime = slea.readLong();
/*  575 */         deposit = slea.readInt();
/*  576 */         deposit = slea.readInt();
/*  577 */         i3 = slea.readInt();
/*  578 */         nWorldId = slea.readInt();
/*      */         
/*  580 */         if (i3 < 0 || i3 > 32767 || l3 < 0L) {
/*  581 */           System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
/*  582 */           c.getSession().close();
/*      */           return;
/*      */         } 
/*  585 */         auctionItem4 = items.get(Integer.valueOf(k));
/*      */         
/*  587 */         if (auctionItem4 != null && auctionItem4.getItem() != null && auctionItem4.getHistory() != null) {
/*  588 */           AuctionHistory history = auctionItem4.getHistory();
/*      */           
/*  590 */           if (history.getId() != dwInventoryId) {
/*  591 */             System.out.println("return 1");
/*      */             
/*      */             return;
/*      */           } 
/*  595 */           if (history.getAuctionId() != k) {
/*  596 */             System.out.println("return 2");
/*      */             
/*      */             return;
/*      */           } 
/*  600 */           if (history.getAccountId() != dwAccountId) {
/*  601 */             System.out.println("return 3");
/*      */             
/*      */             return;
/*      */           } 
/*  605 */           if (history.getCharacterId() != dwCharacterId) {
/*  606 */             System.out.println("return 4");
/*      */             
/*      */             return;
/*      */           } 
/*  610 */           if (history.getItemId() != nItemId) {
/*  611 */             System.out.println("return 5");
/*      */             
/*      */             return;
/*      */           } 
/*  615 */           if (history.getState() != nState) {
/*  616 */             System.out.println("return 6");
/*      */             
/*      */             return;
/*      */           } 
/*  620 */           if (history.getPrice() != l3) {
/*  621 */             System.out.println("return 7");
/*      */             
/*      */             return;
/*      */           } 
/*  625 */           if (PacketHelper.getTime(history.getBuyTime()) != nBuyTime) {
/*  626 */             System.out.println("return 8");
/*      */             
/*      */             return;
/*      */           } 
/*  630 */           if (history.getDeposit() != deposit) {
/*  631 */             System.out.println("return 9");
/*      */             
/*      */             return;
/*      */           } 
/*  635 */           if (history.getQuantity() != i3) {
/*  636 */             System.out.println("return 10");
/*      */             
/*      */             return;
/*      */           } 
/*  640 */           if (history.getWorldId() != nWorldId) {
/*  641 */             System.out.println("return 11");
/*      */             
/*      */             return;
/*      */           } 
/*  645 */           history.setState(8);
/*  646 */           auctionItem4.setState(8);
/*  647 */           c.getPlayer().gainMeso((long)(l3 * 0.95D), false);
/*  648 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(auctionItem4));
/*  649 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteMesoResult());
/*  650 */           FileoutputUtil.log(FileoutputUtil.경매장대금수령로그, "[대금수령] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 이름 : " + ii.getName(auctionItem4.getItem().getItemId()) + " | 아이템 코드 : " + auctionItem4.getItem().getItemId() + " | 수량 " + auctionItem4.getItem().getQuantity() + " | 받은 메소 : " + (long)(l3 * 0.95D) + " | 해당 아이템 판매자 이름 : " + auctionItem4.getName());
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
            case 31: { // 물품 반환
                //리시브를 그대로 보냄
                dwInventoryId = slea.readLong();
                dwAuctionId = slea.readInt();
                dwAccountId = slea.readInt();
                dwCharacterId = slea.readInt();
                nItemId = slea.readInt();
                nState = slea.readInt();
                nPrice = slea.readLong();
                nBuyTime = slea.readLong();
                deposit = slea.readInt();
                deposit = slea.readInt();
                nCount = slea.readInt();
                nWorldId = slea.readInt();

                if (nCount < 0 || nCount > Short.MAX_VALUE || nPrice < 0) {
                    System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
                    c.getSession().close();
                    return;
                } else {
                    item = items.get(dwAuctionId);

                    if (item != null && item.getItem() != null && item.getHistory() != null) {
                        Item it = item.getItem().copy();
                        AuctionHistory history = item.getHistory();

                        if (history.getId() != dwInventoryId) {
                            System.out.println("return 1");
                            return;
                        }

                        if (history.getAuctionId() != dwAuctionId) {
                            System.out.println("return 2");
                            return;
                        }

                        if (history.getAccountId() != dwAccountId) {
                            System.out.println("return 3");
                            return;
                        }

                        if (history.getCharacterId() != dwCharacterId) {
                            System.out.println("return 4");
                            return;
                        }

                        if (history.getItemId() != nItemId) {
                            System.out.println("return 5");
                            return;
                        }

                        if (history.getState() != nState) {
                            System.out.println("return 6");
                            return;
                        }

                        if (history.getPrice() != nPrice) {
                            System.out.println("return 7");
                            return;
                        }

                        if (PacketHelper.getTime(history.getBuyTime()) != nBuyTime) {
                            System.out.println("return 8");
                            return;
                        }

                        if (history.getDeposit() != deposit) {
                            System.out.println("return 9");
                            return;
                        }

                        if (history.getQuantity() != nCount) {
                            System.out.println("return 10");
                            return;
                        }

                        if (history.getWorldId() != nWorldId) {
                            System.out.println("return 11");
                            return;
                        }

                        if (c.getPlayer().getId() != dwCharacterId) {
                            if (ItemFlag.KARMA_EQUIP.check(it.getFlag())) {
                                it.setFlag((it.getFlag() - ItemFlag.KARMA_EQUIP.getValue()));
                            } else if (ItemFlag.KARMA_USE.check(it.getFlag())) {
                                it.setFlag((it.getFlag() - ItemFlag.KARMA_USE.getValue()));
                            }
                        }

                        short slot = c.getPlayer().getInventory(GameConstants.getInventoryType(nItemId)).addItem(it);
                        if (slot >= 0) {
                            item.setState(item.getState() + 5);
                            history.setState(history.getState() + 5);
                            it.setGMLog(new StringBuilder().append(StringUtil.getAllCurrentTime()).append("에 ").append("경매장에서 얻은 " + dwCharacterId + "의 아이템.").toString());
                            c.getSession().writeAndFlush(CWvsContext.InventoryPacket.addInventorySlot(GameConstants.getInventoryType(nItemId), it));
                            c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(item));
                            c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemResult());
                        }
                        break;
                    }
                }
                break;
            }
/*      */ 
/*      */ 
/*      */       
/*      */       case 40:
/*      */       case 41:
/*  763 */         searchItems = new ArrayList<>();
/*  764 */         slea.skip(1);
/*  765 */         searchType = slea.readInt();
/*  766 */         nameWithSpace = slea.readMapleAsciiString();
/*  767 */         nameWithoutSpace = slea.readMapleAsciiString();
/*      */         
/*  769 */         if (searchType == -1) {
/*  770 */           for (AuctionItem auctionItem : items.values()) {
/*  771 */             String name = ii.getName(auctionItem.getItem().getItemId());
/*  772 */             if (name != null && (name.replaceAll(" ", "").contains(nameWithSpace) || name.replaceAll(" ", "").contains(nameWithoutSpace)) && ((
/*  773 */               op == 40 && auctionItem.getState() == 0) || (op == 41 && (auctionItem.getState() == 3 || auctionItem.getState() == 8)))) {
/*  774 */               searchItems.add(auctionItem);
/*      */             }
/*      */           } 
/*      */         } else {
/*      */           
/*  779 */           int itemType = slea.readInt();
/*  780 */           int itemSemiType = slea.readInt();
/*  781 */           int lvMin = slea.readInt();
/*  782 */           int lvMax = slea.readInt();
/*  783 */           long priceMin = slea.readLong();
/*  784 */           long priceMax = slea.readLong();
/*  785 */           int potentialType = slea.readInt();
/*  786 */           boolean and = (slea.readByte() == 1);
/*      */           
/*  788 */           int optionalSearchCount = slea.readInt();
/*      */           
/*  790 */           for (int i4 = 0; i4 < optionalSearchCount; i4++) {
/*  791 */             boolean isStarForce = (slea.readInt() == 1);
/*  792 */             int optionType = slea.readInt();
/*  793 */             int i5 = slea.readInt();
/*      */           } 
/*      */           
/*  796 */           if (searchType <= 1) {
/*  797 */             for (AuctionItem auctionItem : items.values()) {
/*  798 */               if (auctionItem.getItem() != null && auctionItem.getItem().getType() == 1) {
/*  799 */                 Equip equip = (Equip)auctionItem.getItem();
/*      */                 
/*  801 */                 int level = ii.getReqLevel(auctionItem.getItem().getItemId());
/*      */                 
/*  803 */                 boolean lvLimit = (level >= lvMin && level <= lvMax);
/*  804 */                 boolean priceLimit = (auctionItem.getPrice() >= priceMin && auctionItem.getPrice() <= priceMax);
/*  805 */                 boolean potentialLimit = (potentialType == -1 || (potentialType == 0 && equip.getState() == 0) || (potentialType > 0 && equip.getState() - 16 == potentialType));
/*  806 */                 boolean typeLimit = typeLimit(searchType, itemType, itemSemiType, equip.getItemId());
/*      */                 
/*  808 */                 String name = ii.getName(auctionItem.getItem().getItemId());
/*  809 */                 if (typeLimit && lvLimit && priceLimit && potentialLimit && (name.contains(nameWithSpace) || name.contains(nameWithoutSpace) || nameWithoutSpace.isEmpty()) && 
/*  810 */                   equipOptionTypes() && ((op == 40 && auctionItem.getState() == 0) || (op == 41 && (auctionItem.getState() == 3 || auctionItem.getState() == 8)))) {
/*  811 */                   searchItems.add(auctionItem);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/*  817 */             for (AuctionItem auctionItem : items.values()) {
/*  818 */               int level = ii.getReqLevel(auctionItem.getItem().getItemId());
/*  819 */               boolean lvLimit = (level >= lvMin && level <= lvMax);
/*  820 */               boolean priceLimit = (auctionItem.getPrice() >= priceMin && auctionItem.getPrice() <= priceMax);
/*  821 */               boolean typeLimit = typeLimit(searchType, itemType, itemSemiType, auctionItem.getItem().getItemId());
/*      */               
/*  823 */               String name = ii.getName(auctionItem.getItem().getItemId());
/*  824 */               if (typeLimit && lvLimit && priceLimit && (name.contains(nameWithSpace) || name.contains(nameWithoutSpace) || nameWithoutSpace.isEmpty()) && ((
/*  825 */                 op == 40 && auctionItem.getState() == 0) || (op == 41 && (auctionItem.getState() == 3 || auctionItem.getState() == 8)))) {
/*  826 */                 searchItems.add(auctionItem);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  833 */         if (op == 40) {
/*  834 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSearchItems(searchItems));
/*      */         } else {
/*  836 */           c.getSession().writeAndFlush(CField.AuctionPacket.AuctionMarketPrice(searchItems));
/*      */         } 
/*  838 */         FileoutputUtil.log(FileoutputUtil.경매장물품반환로그, "[검색] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 검색 : " + nameWithoutSpace);
/*      */         break;
/*      */       
/*      */       case 45:
/*  842 */         j = slea.readInt();
/*      */         
/*  844 */         auctionItem1 = items.get(Integer.valueOf(j));
/*  845 */         if (auctionItem1 != null) {
/*  846 */           for (int i4 = 0; i4 < 10; i4++) {
/*  847 */             if (c.getKeyValue("wish" + i4) == null) {
/*  848 */               c.setKeyValue("wish" + i4, String.valueOf(j));
/*  849 */               c.getSession().writeAndFlush(CField.AuctionPacket.AuctionAddWishlist(auctionItem1));
/*  850 */               c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlistResult(auctionItem1));
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/*      */         break;
/*      */       
/*      */       case 46:
/*  858 */         wishlist = new ArrayList<>();
/*  859 */         wishItems = new ArrayList<>();
/*      */         
/*  861 */         for (i = 0; i < 10; i++) {
/*  862 */           String wish = c.getKeyValue("wish" + i);
/*  863 */           if (wish != null) {
/*  864 */             wishlist.add(Integer.valueOf(Integer.parseInt(wish)));
/*      */           }
/*      */         } 
/*      */         
/*  868 */         for (iterator = wishlist.iterator(); iterator.hasNext(); ) { int i4 = ((Integer)iterator.next()).intValue();
/*  869 */           AuctionItem auctionItem = items.get(Integer.valueOf(i4));
/*  870 */           if (auctionItem != null) {
/*  871 */             wishItems.add(auctionItem);
/*      */           } }
/*      */ 
/*      */         
/*  875 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlist(wishItems));
/*      */         break;
/*      */       
/*      */       case 47:
/*  879 */         dwAuctionId = slea.readInt();
/*      */         
/*  881 */         item = items.get(Integer.valueOf(dwAuctionId));
/*  882 */         if (item != null) {
/*  883 */           for (int i4 = 0; i4 < 10; i4++) {
/*  884 */             if (c.getKeyValue("wish" + i4).equals(String.valueOf(dwAuctionId))) {
/*  885 */               c.removeKeyValue("wish" + i4);
/*  886 */               c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlistUpdate(dwAuctionId));
/*  887 */               c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlistDeleteResult(dwAuctionId));
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/*      */         break;
/*      */       
/*      */       case 50:
/*  895 */         sellingItems = new ArrayList<>();
/*      */         
/*  897 */         for (AuctionItem auctionItem : items.values()) {
/*  898 */           if (auctionItem.getAccountId() == c.getAccID() && auctionItem.getState() == 0) {
/*  899 */             sellingItems.add(auctionItem);
/*      */           }
/*      */         } 
/*      */         
/*  903 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellingMyItems(sellingItems));
/*      */         break;
/*      */       
/*      */       case 51:
/*  907 */         completeItems = new ArrayList<>();
/*      */         
/*  909 */         for (AuctionItem auctionItem : items.values()) {
/*  910 */           if (((auctionItem.getState() == 2 || auctionItem.getState() == 7) && auctionItem.getBidUserId() == c.getPlayer().getId()) || (auctionItem.getState() != 7 && auctionItem.getState() >= 3 && auctionItem.getAccountId() == c.getAccID())) {
/*  911 */             completeItems.add(auctionItem);
/*      */           }
/*      */         } 
/*      */         
/*  915 */         c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItems(completeItems));
/*      */         break;
/*      */     } 
/*      */     
/*  919 */     if (op != 0 && op != 1 && op != 41 && op != 40 && op != 20 && op != 21) {
/*  920 */       completeItems = new ArrayList<>();
/*  921 */       List<AuctionItem> list5 = new ArrayList<>();
/*  922 */       List<AuctionItem> list6 = new ArrayList<>();
/*  923 */       List<Integer> list = new ArrayList<>();
/*      */       
/*  925 */       for (Map.Entry<Integer, AuctionItem> itemz : items.entrySet()) {
/*  926 */         AuctionItem auctionItem = itemz.getValue();
/*  927 */         if ((auctionItem.getEndDate() < System.currentTimeMillis() || auctionItem.getState() >= 2) && ((
/*  928 */           auctionItem.getState() == 2 && auctionItem.getBidUserId() == c.getPlayer().getId()) || ((auctionItem.getState() == 3 || auctionItem.getState() == 4) && auctionItem.getAccountId() == c.getAccID()))) {
/*  929 */           completeItems.add(auctionItem);
/*      */         }
/*      */ 
/*      */         
/*  933 */         if (auctionItem.getState() == 0 && list6.size() < 1000)
/*      */         {
/*  935 */           list6.add(auctionItem);
/*      */         }
/*      */         
/*  938 */         if ((auctionItem.getState() == 3 || auctionItem.getState() == 8) && list5.size() < 1000) {
/*  939 */           list5.add(auctionItem);
/*      */         }
/*      */       } 
/*      */       
/*  943 */       c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItems(completeItems));
/*  944 */       c.getSession().writeAndFlush(CField.AuctionPacket.AuctionMarketPrice(list5));
/*  945 */       c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSearchItems(list6));
/*      */     }  }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean equipOptionTypes() {
/*  951 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean typeLimit(int searchType, int itemType, int itemSemiType, int itemId) {
/*  955 */     switch (searchType) {
/*      */       case 0:
/*  957 */         switch (itemType) {
/*      */           case 0:
/*  959 */             return !GameConstants.isWeapon(itemId);
/*      */           
/*      */           case 1:
/*  962 */             switch (itemSemiType) {
/*      */               case 0:
/*  964 */                 return (GameConstants.isWeapon(itemId) || GameConstants.isAccessory(itemId));
/*      */               case 1:
/*  966 */                 return (itemId / 1000 == 100);
/*      */               case 2:
/*  968 */                 return (itemId / 1000 == 104);
/*      */               case 3:
/*  970 */                 return (itemId / 1000 == 105);
/*      */               case 4:
/*  972 */                 return (itemId / 1000 == 106);
/*      */               case 5:
/*  974 */                 return (itemId / 1000 == 107);
/*      */               case 6:
/*  976 */                 return (itemId / 1000 == 108);
/*      */               case 7:
/*  978 */                 return (itemId / 1000 == 109);
/*      */               case 8:
/*  980 */                 return (itemId / 1000 == 110);
/*      */             } 
/*      */             
/*      */             break;
/*      */           case 2:
/*  985 */             switch (itemSemiType) {
/*      */               case 0:
/*  987 */                 return !GameConstants.isAccessory(itemId);
/*      */               case 1:
/*  989 */                 return (itemId / 1000 == 1012);
/*      */               case 2:
/*  991 */                 return (itemId / 1000 == 1022);
/*      */               case 3:
/*  993 */                 return (itemId / 1000 == 1032);
/*      */               case 4:
/*  995 */                 return GameConstants.isRing(itemId);
/*      */               case 5:
/*  997 */                 return (itemId / 1000 == 1122 || itemId / 1000 == 1123);
/*      */               case 6:
/*  999 */                 return (itemId / 1000 == 1132);
/*      */               case 7:
/* 1001 */                 return GameConstants.isMedal(itemId);
/*      */               case 8:
/* 1003 */                 return (itemId / 1000 == 1152);
/*      */               case 9:
/* 1005 */                 return (itemId / 1000 == 1162);
/*      */               case 10:
/* 1007 */                 return (itemId / 1000 == 1182);
/*      */             } 
/*      */             
/*      */             break;
/*      */           case 3:
/* 1012 */             switch (itemSemiType) {
/*      */               case 0:
/* 1014 */                 return (itemId / 1000 >= 1612 && itemId / 1000 <= 1652);
/*      */               case 1:
/* 1016 */                 return (itemId / 1000 == 1662);
/*      */               case 2:
/* 1018 */                 return (itemId / 1000 == 1672);
/*      */               case 3:
/* 1020 */                 return (itemId / 1000 >= 1942 && itemId / 1000 <= 1972);
/*      */             } 
/*      */             
/*      */             break;
/*      */         } 
/*      */       
/*      */       case 1:
/* 1027 */         switch (itemType) {
/*      */           case 0:
/* 1029 */             return GameConstants.isWeapon(itemId);
/*      */           
/*      */           case 1:
/* 1032 */             switch (itemSemiType) {
/*      */               case 0:
/* 1034 */                 return !GameConstants.isTwoHanded(itemId);
/*      */               case 1:
/* 1036 */                 return (itemId / 1000 == 1212);
/*      */               case 2:
/* 1038 */                 return (itemId / 1000 == 1222);
/*      */               case 3:
/* 1040 */                 return (itemId / 1000 == 1232);
/*      */               case 4:
/* 1042 */                 return (itemId / 1000 == 1242);
/*      */               case 5:
/* 1044 */                 return (itemId / 1000 == 1302);
/*      */               case 6:
/* 1046 */                 return (itemId / 1000 == 1312);
/*      */               case 7:
/* 1048 */                 return (itemId / 1000 == 1322);
/*      */               case 8:
/* 1050 */                 return (itemId / 1000 == 1332);
/*      */               case 9:
/* 1052 */                 return (itemId / 1000 == 1342);
/*      */               case 10:
/* 1054 */                 return (itemId / 1000 == 1362);
/*      */               case 11:
/* 1056 */                 return (itemId / 1000 == 1372);
/*      */               case 12:
/* 1058 */                 return (itemId / 1000 == 1262);
/*      */               case 13:
/* 1060 */                 return (itemId / 1000 == 1272);
/*      */               case 14:
/* 1062 */                 return (itemId / 1000 == 1282);
/*      */               case 15:
/* 1064 */                 return (itemId / 1000 == 1292);
/*      */               case 16:
/* 1066 */                 return (itemId / 1000 == 1213);
/*      */             } 
/*      */             
/*      */             break;
/*      */           case 2:
/* 1071 */             switch (itemSemiType) {
/*      */               case 0:
/* 1073 */                 return GameConstants.isTwoHanded(itemId);
/*      */               case 1:
/* 1075 */                 return (itemId / 1000 == 1402);
/*      */               case 2:
/* 1077 */                 return (itemId / 1000 == 1412);
/*      */               case 3:
/* 1079 */                 return (itemId / 1000 == 1422);
/*      */               case 4:
/* 1081 */                 return (itemId / 1000 == 1432);
/*      */               case 5:
/* 1083 */                 return (itemId / 1000 == 1442);
/*      */               case 6:
/* 1085 */                 return (itemId / 1000 == 1452);
/*      */               case 7:
/* 1087 */                 return (itemId / 1000 == 1462);
/*      */               case 8:
/* 1089 */                 return (itemId / 1000 == 1472);
/*      */               case 9:
/* 1091 */                 return (itemId / 1000 == 1482);
/*      */               case 10:
/* 1093 */                 return (itemId / 1000 == 1492);
/*      */               case 11:
/* 1095 */                 return (itemId / 1000 == 1522);
/*      */               case 12:
/* 1097 */                 return (itemId / 1000 == 1532);
/*      */               case 13:
/* 1099 */                 return (itemId / 1000 == 1582);
/*      */               case 14:
/* 1101 */                 return (itemId / 1000 == 1592);
/*      */             } 
/*      */             
/*      */             break;
/*      */           case 3:
/* 1106 */             switch (itemSemiType) {
/*      */               case 0:
/* 1108 */                 return (itemId / 1000 == 1352 || itemId / 1000 == 1353);
/*      */               case 1:
/* 1110 */                 return (itemId / 10 == 135220);
/*      */               case 2:
/* 1112 */                 return (itemId / 10 == 135221);
/*      */               case 3:
/* 1114 */                 return (itemId / 10 == 135222);
/*      */               case 4:
/* 1116 */                 return (itemId / 10 == 135223 || itemId / 10 == 135224 || itemId / 10 == 135225);
/*      */               case 5:
/* 1118 */                 return (itemId / 10 == 135226);
/*      */               case 6:
/* 1120 */                 return (itemId / 10 == 135227);
/*      */               case 7:
/* 1122 */                 return (itemId / 10 == 135228);
/*      */               case 8:
/* 1124 */                 return (itemId / 10 == 135229);
/*      */               case 9:
/* 1126 */                 return (itemId / 10 == 135290);
/*      */               case 10:
/* 1128 */                 return (itemId / 10 == 135291);
/*      */               case 11:
/* 1130 */                 return (itemId / 10 == 135292);
/*      */               case 12:
/* 1132 */                 return (itemId / 10 == 135297);
/*      */               case 13:
/* 1134 */                 return (itemId / 10 == 135293);
/*      */               case 14:
/* 1136 */                 return (itemId / 10 == 135294);
/*      */               case 15:
/* 1138 */                 return (itemId / 10 == 135240);
/*      */               case 16:
/* 1140 */                 return (itemId / 10 == 135201);
/*      */               case 17:
/* 1142 */                 return (itemId / 10 == 135210);
/*      */               case 18:
/* 1144 */                 return (itemId / 10 == 135310);
/*      */               case 19:
/* 1146 */                 return (itemId / 10 == 135295);
/*      */               case 20:
/* 1148 */                 return (itemId / 10 == 135296);
/*      */               case 21:
/* 1150 */                 return (itemId / 10 == 135300);
/*      */               case 22:
/* 1152 */                 return (itemId / 10 == 135270);
/*      */               case 23:
/* 1154 */                 return (itemId / 10 == 135250);
/*      */               case 24:
/* 1156 */                 return (itemId / 10 == 135260);
/*      */               case 25:
/* 1158 */                 return (itemId / 10 == 135320);
/*      */               case 26:
/* 1160 */                 return (itemId / 10 == 135340);
/*      */               case 27:
/* 1162 */                 return (itemId / 10 == 135330);
/*      */               case 28:
/* 1164 */                 return (itemId / 10 == 135350);
/*      */               case 29:
/* 1166 */                 return (itemId / 10 == 135360);
/*      */               case 30:
/* 1168 */                 return (itemId / 10 == 135370);
/*      */               case 31:
/* 1170 */                 return (itemId / 10 == 135380);
/*      */               case 32:
/* 1172 */                 return (itemId / 10 == 135390);
/*      */             } 
/*      */             
/*      */             break;
/*      */         } 
/*      */         
/*      */         break;
/*      */       case 2:
/* 1180 */         return (itemId / 1000000 == 2);
/*      */       
/*      */       case 3:
/* 1183 */         return MapleItemInformationProvider.getInstance().isCash(itemId);
/*      */       
/*      */       case 4:
/* 1186 */         return (itemId / 1000000 == 4 || itemId / 1000000 == 3);
/*      */     } 
/*      */     
/* 1189 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\Phellos\Desktop\크루엘라\Ozoh디컴.jar!\handling\auction\handler\AuctionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */