/*     */ package server;
/*     */ 
/*     */ import client.MapleClient;
/*     */ import client.inventory.Equip;
/*     */ import client.inventory.Item;
/*     */ import client.inventory.ItemLoader;
/*     */ import client.inventory.MapleInventoryIdentifier;
/*     */ import client.inventory.MapleInventoryType;
/*     */ import client.inventory.MaplePet;
/*     */ import client.inventory.MapleRing;
/*     */ import constants.GameConstants;
/*     */ import database.DatabaseConnection;
/*     */ import java.io.Serializable;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import tools.FileoutputUtil;
/*     */ import tools.Pair;
/*     */ import tools.packet.CSPacket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CashShop
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 231541893513373579L;
/*     */   private int accountId;
/*     */   private int characterId;
/*  47 */   private ItemLoader factory = ItemLoader.CASHSHOP;
/*  48 */   private List<Item> inventory = new ArrayList<>();
/*  49 */   private List<Long> uniqueids = new ArrayList<>();
/*     */   
/*     */   public CashShop(int accountId, int characterId, int jobType) throws SQLException {
/*  52 */     this.accountId = accountId;
/*  53 */     this.characterId = characterId;
/*  54 */     for (Pair<Item, MapleInventoryType> item : (Iterable<Pair<Item, MapleInventoryType>>)this.factory.loadCSItems(false, accountId).values()) {
/*  55 */       this.inventory.add(item.getLeft());
/*     */     }
/*     */   }
/*     */   
/*     */   public int getItemsSize() {
/*  60 */     return this.inventory.size();
/*     */   }
/*     */   
/*     */   public List<Item> getInventory() {
/*  64 */     return this.inventory;
/*     */   }
/*     */   
/*     */   public Item findByCashId(long uniqueId, int itemId, byte type) {
/*  68 */     for (Item item : this.inventory) {
/*  69 */       if (item.getUniqueId() == uniqueId && item.getItemId() == itemId && GameConstants.getInventoryType(item.getItemId()).getType() == type) {
/*  70 */         return item;
/*     */       }
/*     */     } 
/*     */     
/*  74 */     return null;
/*     */   }
/*     */   
/*     */   public void checkExpire(MapleClient c) {
/*  78 */     List<Item> toberemove = new ArrayList<>();
/*  79 */     for (Item item : this.inventory) {
/*  80 */       if (item != null && !GameConstants.isPet(item.getItemId()) && item.getExpiration() > 0L && item.getExpiration() < System.currentTimeMillis()) {
/*  81 */         toberemove.add(item);
/*     */       }
/*     */     } 
/*  84 */     if (toberemove.size() > 0) {
/*  85 */       for (Item item : toberemove) {
/*  86 */         removeFromInventory(item);
/*  87 */         c.getSession().writeAndFlush(CSPacket.cashItemExpired(item.getUniqueId()));
/*     */       } 
/*  89 */       toberemove.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Item toItem(CashItemInfo cItem) {
/*  94 */     return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), "");
/*     */   }
/*     */   
/*     */   public Item toItem(CashItemInfo cItem, String gift) {
/*  98 */     return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), gift);
/*     */   }
/*     */   
/*     */   public Item toItem(CashItemInfo cItem, long uniqueid) {
/* 102 */     return toItem(cItem, uniqueid, "");
/*     */   }
/*     */   
/*     */   public Item toItem(CashItemInfo cItem, long uniqueid, String gift) {
/* 106 */     if (uniqueid <= 0L) {
/* 107 */       uniqueid = MapleInventoryIdentifier.getInstance();
/*     */     }
/* 109 */     long period = cItem.getPeriod();
/* 110 */     Item ret = null;
/* 111 */     if (GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.CODY || GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.EQUIP) {
/* 112 */       Equip eq = (Equip)MapleItemInformationProvider.getInstance().getEquipById(cItem.getId(), uniqueid);
/* 113 */       if (period > 0L) {
/* 114 */         eq.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
/*     */       }
/* 116 */       eq.setGMLog("Cash Shop: " + cItem.getSN() + " on " + FileoutputUtil.CurrentReadable_Date());
/* 117 */       eq.setGiftFrom(gift);
/* 118 */       if (GameConstants.isEffectRing(cItem.getId()) && uniqueid > 0L) {
/* 119 */         MapleRing ring = MapleRing.loadFromDb(uniqueid);
/* 120 */         if (ring != null) {
/* 121 */           eq.setRing(ring);
/*     */         }
/*     */       } 
/* 124 */       ret = eq.copy();
/*     */     } else {
/* 126 */       Item item = new Item(cItem.getId(), (short)0, (short)cItem.getCount(), 0, uniqueid);
/* 127 */       if (period > 0L) {
/* 128 */         item.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
/*     */       }
/* 130 */       item.setGMLog("Cash Shop: " + cItem.getSN() + " on " + FileoutputUtil.CurrentReadable_Date());
/* 131 */       item.setGiftFrom(gift);
/* 132 */       if (GameConstants.isPet(cItem.getId())) {
/* 133 */         MaplePet pet = MaplePet.createPet(cItem.getId(), uniqueid);
/* 134 */         if (pet != null) {
/* 135 */           item.setPet(pet);
/*     */         }
/*     */       } 
/* 138 */       ret = item.copy();
/*     */     } 
/* 140 */     return ret;
/*     */   }
/*     */   
/*     */   public void addToInventory(Item item) {
/* 144 */     this.inventory.add(item);
/*     */   }
/*     */   
/*     */   public void removeFromInventory(Item item) {
/* 148 */     this.inventory.remove(item);
/*     */   }
/*     */   
/*     */   public void gift(int recipient, String from, String message, int sn) {
/* 152 */     gift(recipient, from, message, sn, 0L);
/*     */   }
/*     */   
/*     */   public void gift(int recipient, String from, String message, int sn, long uniqueid) {
/* 156 */     Connection con = null;
/* 157 */     PreparedStatement ps = null;
/* 158 */     ResultSet rs = null;
/*     */     try {
/* 160 */       con = DatabaseConnection.getConnection();
/* 161 */       ps = con.prepareStatement("INSERT INTO `gifts` VALUES (DEFAULT, ?, ?, ?, ?, ?)");
/* 162 */       ps.setInt(1, recipient);
/* 163 */       ps.setString(2, from);
/* 164 */       ps.setString(3, message);
/* 165 */       ps.setInt(4, sn);
/* 166 */       ps.setLong(5, uniqueid);
/* 167 */       ps.executeUpdate();
/* 168 */       ps.close();
/* 169 */       con.close();
/* 170 */     } catch (SQLException sqle) {
/* 171 */       sqle.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 174 */         if (con != null) {
/* 175 */           con.close();
/*     */         }
/* 177 */         if (ps != null) {
/* 178 */           ps.close();
/*     */         }
/* 180 */         if (rs != null) {
/* 181 */           rs.close();
/*     */         }
/* 183 */       } catch (SQLException se) {
/* 184 */         se.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<Pair<Item, String>> loadGifts() {
/* 190 */     List<Pair<Item, String>> gifts = new ArrayList<>();
/* 191 */     Connection con = null;
/* 192 */     PreparedStatement ps = null;
/* 193 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 196 */       con = DatabaseConnection.getConnection();
/* 197 */       ps = con.prepareStatement("SELECT * FROM `gifts` WHERE `recipient` = ?");
/* 198 */       ps.setInt(1, this.characterId);
/* 199 */       rs = ps.executeQuery();
/*     */       
/* 201 */       while (rs.next()) {
/* 202 */         CashItemInfo cItem = CashItemFactory.getInstance().getItem(rs.getInt("sn"));
/* 203 */         if (cItem == null) {
/*     */           continue;
/*     */         }
/* 206 */         Item item = toItem(cItem, rs.getLong("uniqueid"), rs.getString("from"));
/* 207 */         gifts.add(new Pair(item, rs.getString("message")));
/* 208 */         this.uniqueids.add(Long.valueOf(item.getUniqueId()));
/* 209 */         List<Integer> packages = CashItemFactory.getInstance().getPackageItems(cItem.getId());
/* 210 */         if (packages != null && packages.size() > 0) {
/* 211 */           for (Iterator<Integer> iterator = packages.iterator(); iterator.hasNext(); ) { int packageItem = ((Integer)iterator.next()).intValue();
/* 212 */             CashItemInfo pack = CashItemFactory.getInstance().getSimpleItem(packageItem);
/* 213 */             if (pack != null)
/* 214 */               addToInventory(toItem(pack, rs.getString("from")));  }
/*     */           
/*     */           continue;
/*     */         } 
/* 218 */         addToInventory(item);
/*     */       } 
/*     */ 
/*     */       
/* 222 */       rs.close();
/* 223 */       ps.close();
/* 224 */       ps = con.prepareStatement("DELETE FROM `gifts` WHERE `recipient` = ?");
/* 225 */       ps.setInt(1, this.characterId);
/* 226 */       ps.executeUpdate();
/* 227 */       ps.close();
/* 228 */       save(null);
/* 229 */     } catch (SQLException sqle) {
/* 230 */       sqle.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 233 */         if (con != null) {
/* 234 */           con.close();
/*     */         }
/* 236 */         if (ps != null) {
/* 237 */           ps.close();
/*     */         }
/* 239 */         if (rs != null) {
/* 240 */           rs.close();
/*     */         }
/* 242 */       } catch (SQLException se) {
/* 243 */         se.printStackTrace();
/*     */       } 
/*     */     } 
/* 246 */     return gifts;
/*     */   }
/*     */   
/*     */   public boolean canSendNote(int uniqueid) {
/* 250 */     return this.uniqueids.contains(Integer.valueOf(uniqueid));
/*     */   }
/*     */   
/*     */   public void sendedNote(int uniqueid) {
/* 254 */     for (int i = 0; i < this.uniqueids.size(); i++) {
/* 255 */       if (((Long)this.uniqueids.get(i)).intValue() == uniqueid) {
/* 256 */         this.uniqueids.remove(i);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void save(Connection con) throws SQLException {
/* 262 */     List<Pair<Item, MapleInventoryType>> itemsWithType = new ArrayList<>();
/*     */     
/* 264 */     for (Item item : this.inventory) {
/* 265 */       itemsWithType.add(new Pair(item, GameConstants.getInventoryType(item.getItemId())));
/*     */     }
/*     */     
/* 268 */     this.factory.saveItems(itemsWithType, this.accountId);
/*     */   }
/*     */ }


/* Location:              C:\Users\Phellos\Desktop\크루엘라\Ozoh디컴.jar!\server\CashShop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */