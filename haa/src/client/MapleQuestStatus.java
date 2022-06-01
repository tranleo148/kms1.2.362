/*     */ package client;
/*     */ 
/*     */ import constants.GameConstants;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import server.life.MapleLifeFactory;
/*     */ import server.quest.MapleQuest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapleQuestStatus
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 91795419934134L;
/*     */   private transient MapleQuest quest;
/*     */   private byte status;
/*  37 */   private Map<Integer, Integer> killedMobs = null;
/*     */   private int npc;
/*     */   private long completionTime;
/*  40 */   private int forfeited = 0;
/*     */ 
/*     */   
/*     */   private String customData;
/*     */ 
/*     */   
/*     */   public MapleQuestStatus(MapleQuest quest, int status) {
/*  47 */     this.quest = quest;
/*  48 */     setStatus((byte)status);
/*  49 */     this.completionTime = System.currentTimeMillis();
/*  50 */     if (status == 1 && 
/*  51 */       !quest.getRelevantMobs().isEmpty()) {
/*  52 */       registerMobs();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MapleQuestStatus(MapleQuest quest, byte status, int npc) {
/*  58 */     this.quest = quest;
/*  59 */     setStatus(status);
/*  60 */     setNpc(npc);
/*  61 */     this.completionTime = System.currentTimeMillis();
/*  62 */     if (status == 1 && 
/*  63 */       !quest.getRelevantMobs().isEmpty()) {
/*  64 */       registerMobs();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setQuest(int qid) {
/*  70 */     this.quest = MapleQuest.getInstance(qid);
/*     */   }
/*     */   
/*     */   public final MapleQuest getQuest() {
/*  74 */     return this.quest;
/*     */   }
/*     */   
/*     */   public final byte getStatus() {
/*  78 */     return this.status;
/*     */   }
/*     */   
/*     */   public final void setStatus(byte status) {
/*  82 */     this.status = status;
/*     */   }
/*     */   
/*     */   public final int getNpc() {
/*  86 */     return this.npc;
/*     */   }
/*     */   
/*     */   public final void setNpc(int npc) {
/*  90 */     this.npc = npc;
/*     */   }
/*     */   
/*     */   public boolean isCustom() {
/*  94 */     return GameConstants.isCustomQuest(this.quest.getId());
/*     */   }
/*     */   
/*     */   private final void registerMobs() {
/*  98 */     this.killedMobs = new LinkedHashMap<>();
/*  99 */     for (Iterator<Integer> iterator = this.quest.getRelevantMobs().keySet().iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 100 */       this.killedMobs.put(Integer.valueOf(i), Integer.valueOf(0)); }
/*     */   
/*     */   }
/*     */   
/*     */   private final int maxMob(int mobid) {
/* 105 */     for (Map.Entry<Integer, Integer> qs : (Iterable<Map.Entry<Integer, Integer>>)this.quest.getRelevantMobs().entrySet()) {
/* 106 */       if (((Integer)qs.getKey()).intValue() == mobid) {
/* 107 */         return ((Integer)qs.getValue()).intValue();
/*     */       }
/*     */     } 
/* 110 */     return 0;
/*     */   }
/*     */   
/*     */   public final boolean mobKilled(int id, int skillID, MapleCharacter chr) {
/* 114 */     if (this.quest != null && this.quest.getSkillID() > 0 && 
/* 115 */       this.quest.getSkillID() != skillID) {
/* 116 */       return false;
/*     */     }
/*     */     
/* 119 */     Integer mob = this.killedMobs.get(Integer.valueOf(id));
/* 120 */     if (mob != null) {
/* 121 */       int mo = maxMob(id);
/* 122 */       if (mob.intValue() >= mo) {
/* 123 */         return false;
/*     */       }
/* 125 */       this.killedMobs.put(Integer.valueOf(id), Integer.valueOf(Math.min(mob.intValue() + 1, mo)));
/* 126 */       return true;
/*     */     } 
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
/* 145 */     for (Map.Entry<Integer, Integer> mo : this.killedMobs.entrySet()) {
/* 146 */       if (questCount(((Integer)mo.getKey()).intValue(), id)) {
/* 147 */         int mobb = maxMob(((Integer)mo.getKey()).intValue());
/* 148 */         if (((Integer)mo.getValue()).intValue() >= mobb) {
/* 149 */           return false;
/*     */         }
/* 151 */         if (((Integer)mo.getKey()).intValue() == 9101025) {
/* 152 */           int reqLevel = MapleLifeFactory.getMonster(id).getStats().getLevel();
/* 153 */           if (reqLevel >= chr.getLevel() - 20 && reqLevel <= chr.getLevel() + 20) {
/* 154 */             this.killedMobs.put(mo.getKey(), Integer.valueOf(Math.min(((Integer)mo.getValue()).intValue() + 1, mobb)));
/*     */           }
/* 156 */         } else if (((Integer)mo.getKey()).intValue() == 9101067) {
/* 157 */           int scale = MapleLifeFactory.getMonster(id).getScale();
/* 158 */           if (scale > 100) {
/* 159 */             this.killedMobs.put(mo.getKey(), Integer.valueOf(Math.min(((Integer)mo.getValue()).intValue() + 1, mobb)));
/*     */           }
/*     */         } else {
/* 162 */           this.killedMobs.put(mo.getKey(), Integer.valueOf(Math.min(((Integer)mo.getValue()).intValue() + 1, mobb)));
/*     */         } 
/* 164 */         return true;
/*     */       } 
/*     */     } 
/* 167 */     return false;
/*     */   }
/*     */   
/*     */   private final boolean questCount(int mo, int id) {
/* 171 */     if (MapleLifeFactory.getQuestCount(mo) != null) {
/* 172 */       for (Iterator<Integer> iterator = MapleLifeFactory.getQuestCount(mo).iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 173 */         if (i == id || mo == 9101025) {
/* 174 */           return true;
/*     */         } }
/*     */     
/*     */     }
/* 178 */     return false;
/*     */   }
/*     */   
/*     */   public final void setMobKills(int id, int count) {
/* 182 */     if (this.killedMobs == null) {
/* 183 */       registerMobs();
/*     */     }
/* 185 */     this.killedMobs.put(Integer.valueOf(id), Integer.valueOf(count));
/*     */   }
/*     */   
/*     */   public final boolean hasMobKills() {
/* 189 */     if (this.killedMobs == null) {
/* 190 */       return false;
/*     */     }
/* 192 */     return (this.killedMobs.size() > 0);
/*     */   }
/*     */   
/*     */   public final int getMobKills(int id) {
/* 196 */     Integer mob = this.killedMobs.get(Integer.valueOf(id));
/* 197 */     if (mob == null) {
/* 198 */       return 0;
/*     */     }
/* 200 */     return mob.intValue();
/*     */   }
/*     */   
/*     */   public final Map<Integer, Integer> getMobKills() {
/* 204 */     return this.killedMobs;
/*     */   }
/*     */   
/*     */   public final long getCompletionTime() {
/* 208 */     return this.completionTime;
/*     */   }
/*     */   
/*     */   public final void setCompletionTime(long completionTime) {
/* 212 */     this.completionTime = completionTime;
/*     */   }
/*     */   
/*     */   public final int getForfeited() {
/* 216 */     return this.forfeited;
/*     */   }
/*     */   
/*     */   public final void setForfeited(int forfeited) {
/* 220 */     if (forfeited >= this.forfeited) {
/* 221 */       this.forfeited = forfeited;
/*     */     } else {
/* 223 */       throw new IllegalArgumentException("Can't set forfeits to something lower than before.");
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void setCustomData(String customData) {
/* 228 */     this.customData = customData;
/*     */   }
/*     */   
/*     */   public final String getCustomData() {
/* 232 */     return this.customData;
/*     */   }
/*     */ }


/* Location:              C:\Users\Phellos\Desktop\크루엘라\Ozoh디컴.jar!\client\MapleQuestStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */