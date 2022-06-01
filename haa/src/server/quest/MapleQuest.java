package server.quest;

import client.MapleCharacter;
import client.MapleQuestStatus;
import client.SkillFactory;
import database.DatabaseConnection;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import scripting.NPCScriptManager;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleQuest implements Serializable {
  private static final long serialVersionUID = 9179541993413738569L;
  
  private static final Map<Integer, MapleQuest> quests = new LinkedHashMap<>();
  
  protected int id;
  
  protected final List<MapleQuestRequirement> startReqs = new LinkedList<>();
  
  protected final List<MapleQuestRequirement> completeReqs = new LinkedList<>();
  
  protected final List<MapleQuestAction> startActs = new LinkedList<>();
  
  protected final List<MapleQuestAction> completeActs = new LinkedList<>();
  
  protected final Map<String, List<Pair<String, Pair<String, Integer>>>> partyQuestInfo = new LinkedHashMap<>();
  
  protected final Map<Integer, Integer> relevantMobs = new LinkedHashMap<>();
  
  private boolean autoStart = false;
  
  private boolean autoPreComplete = false;
  
  private boolean autoCompleteAction = false;
  
  private boolean repeatable = false;
  
  private boolean customend = false;
  
  private boolean blocked = false;
  
  private boolean autoAccept = false;
  
  private boolean autoComplete = false;
  
  private boolean scriptedStart = false;
  
  private boolean dailyAlarm = false;
  
  private int viewMedalItem = 0;
  
  private int selectedSkillID = 0;
  
  protected String name = "";
  
  protected MapleQuest(int id) {
    this.id = id;
  }
  
  private static MapleQuest loadQuest(ResultSet rs, PreparedStatement psr, PreparedStatement psa, PreparedStatement pss, PreparedStatement psq, PreparedStatement psi, PreparedStatement psp) throws SQLException {
    MapleQuest ret = new MapleQuest(rs.getInt("questid"));
    ret.name = rs.getString("name");
    ret.autoStart = (rs.getInt("autoStart") > 0);
    ret.autoPreComplete = (rs.getInt("autoPreComplete") > 0);
    ret.autoAccept = (rs.getInt("autoAccept") > 0);
    ret.autoComplete = (rs.getInt("autoComplete") > 0);
    ret.autoCompleteAction = (rs.getInt("autoCompleteAction") > 0);
    ret.viewMedalItem = rs.getInt("viewMedalItem");
    ret.selectedSkillID = rs.getInt("selectedSkillID");
    ret.blocked = (rs.getInt("blocked") > 0);
    psr.setInt(1, ret.id);
    ResultSet rse = psr.executeQuery();
    while (rse.next()) {
      MapleQuestRequirementType type = MapleQuestRequirementType.getByWZName(rse.getString("name"));
      MapleQuestRequirement req = new MapleQuestRequirement(ret, type, rse);
      if (type.equals(MapleQuestRequirementType.interval)) {
        ret.repeatable = true;
      } else if (type.equals(MapleQuestRequirementType.normalAutoStart)) {
        ret.repeatable = true;
        ret.autoStart = true;
      } else if (type.equals(MapleQuestRequirementType.startscript)) {
        ret.scriptedStart = true;
      } else if (type.equals(MapleQuestRequirementType.endscript)) {
        ret.customend = true;
      } else if (type.equals(MapleQuestRequirementType.mob)) {
        for (Pair<Integer, Integer> mob : req.getDataStore())
          ret.relevantMobs.put((Integer)mob.left, (Integer)mob.right); 
      } 
      if (rse.getInt("type") == 0) {
        ret.startReqs.add(req);
        continue;
      } 
      ret.completeReqs.add(req);
    } 
    rse.close();
    psa.setInt(1, ret.id);
    rse = psa.executeQuery();
    while (rse.next()) {
      MapleQuestActionType ty = MapleQuestActionType.getByWZName(rse.getString("name"));
      if (rse.getInt("type") == 0) {
        if (ty == MapleQuestActionType.item && ret.id == 7103)
          continue; 
        ret.startActs.add(new MapleQuestAction(ty, rse, ret, pss, psq, psi));
        continue;
      } 
      if (ty == MapleQuestActionType.item && ret.id == 7102)
        continue; 
      ret.completeActs.add(new MapleQuestAction(ty, rse, ret, pss, psq, psi));
    } 
    rse.close();
    psp.setInt(1, ret.id);
    rse = psp.executeQuery();
    while (rse.next()) {
      if (!ret.partyQuestInfo.containsKey(rse.getString("rank")))
        ret.partyQuestInfo.put(rse.getString("rank"), new ArrayList<>()); 
      ((List)ret.partyQuestInfo.get(rse.getString("rank"))).add(new Pair<>(rse.getString("mode"), new Pair<>(rse.getString("property"), Integer.valueOf(rse.getInt("value")))));
    } 
    rse.close();
    return ret;
  }
  
  public List<Pair<String, Pair<String, Integer>>> getInfoByRank(String rank) {
    return this.partyQuestInfo.get(rank);
  }
  
  public boolean isPartyQuest() {
    return (this.partyQuestInfo.size() > 0);
  }
  
  public final int getSkillID() {
    return this.selectedSkillID;
  }
  
  public final String getName() {
    return this.name;
  }
  
  public final List<MapleQuestAction> getCompleteActs() {
    return this.completeActs;
  }
  
  public static void initQuests() {
    Connection con = null;
    PreparedStatement ps = null, psr = null, psa = null, pss = null, psq = null, psi = null, psp = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM wz_questdata");
      psr = con.prepareStatement("SELECT * FROM wz_questreqdata WHERE questid = ?");
      psa = con.prepareStatement("SELECT * FROM wz_questactdata WHERE questid = ?");
      pss = con.prepareStatement("SELECT * FROM wz_questactskilldata WHERE uniqueid = ?");
      psq = con.prepareStatement("SELECT * FROM wz_questactquestdata WHERE uniqueid = ?");
      psi = con.prepareStatement("SELECT * FROM wz_questactitemdata WHERE uniqueid = ?");
      psp = con.prepareStatement("SELECT * FROM wz_questpartydata WHERE questid = ?");
      rs = ps.executeQuery();
      while (rs.next())
        quests.put(Integer.valueOf(rs.getInt("questid")), loadQuest(rs, psr, psa, pss, psq, psi, psp)); 
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      try {
        psr.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      try {
        psa.close();
      } catch (SQLException e1) {
        e1.printStackTrace();
      } 
      try {
        pss.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      try {
        psq.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      try {
        psi.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      try {
        psp.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      if (con != null)
        try {
          con.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
    } 
  }
  
  public static MapleQuest getInstance(int id) {
    MapleQuest ret = quests.get(Integer.valueOf(id));
    if (ret == null) {
      ret = new MapleQuest(id);
      quests.put(Integer.valueOf(id), ret);
    } 
    return ret;
  }
  
  public static Collection<MapleQuest> getAllInstances() {
    return quests.values();
  }
  
  public boolean canStart(MapleCharacter c, Integer npcid) {
    if (c.getQuest(this).getStatus() != 0 && (c.getQuest(this).getStatus() != 2 || !this.repeatable))
      return false; 
    if (this.blocked && !c.isGM())
      return false; 
    for (MapleQuestRequirement r : this.startReqs) {
      if (r.getType() == MapleQuestRequirementType.dayByDay && npcid != null) {
        forceComplete(c, npcid.intValue());
        return false;
      } 
      if (!r.check(c, npcid))
        return false; 
    } 
    return true;
  }
  
  public boolean canComplete(MapleCharacter chr, Integer npcid) {
    return canComplete(chr, npcid, false);
  }
  
  public boolean canComplete(MapleCharacter chr, Integer npcid, boolean acc) {
    if (acc) {
      if (chr.getClient().getQuest(this).getStatus() != 1)
        return false; 
    } else if (chr.getQuest(this).getStatus() != 1) {
      return false;
    } 
    if (this.blocked && !chr.isGM())
      return false; 
    if (this.autoComplete && npcid != null && this.viewMedalItem <= 0) {
      forceComplete(chr, npcid.intValue(), acc);
      return false;
    } 
    for (MapleQuestRequirement r : this.completeReqs) {
      if (!r.check(chr, npcid))
        return false; 
    } 
    return true;
  }
  
  public final void RestoreLostItem(MapleCharacter c, int itemid) {
    if (this.blocked && !c.isGM())
      return; 
    for (MapleQuestAction a : this.startActs) {
      if (a.RestoreLostItem(c, itemid))
        break; 
    } 
  }
  
  public void start(MapleCharacter c, int npc) {
    start(c, npc, false);
  }
  
  public void start(MapleCharacter c, int npc, boolean acc) {
    if (this.autoStart || checkNPCOnMap(c, npc)) {
      for (MapleQuestAction a : this.startActs) {
        if (!a.checkEnd(c, null))
          return; 
      } 
      for (MapleQuestAction a : this.startActs)
        a.runStart(c, null); 
      if (!this.customend) {
        forceStart(c, npc, null, acc);
        c.getClient().send(CWvsContext.InfoPacket.AcceptQuest(this.id, npc));
      } else {
        NPCScriptManager.getInstance().endQuest(c.getClient(), npc, getId(), true);
      } 
    } 
  }
  
  public void complete(MapleCharacter c, int npc) {
    complete(c, npc, null, false);
  }
  
  public void complete(MapleCharacter c, int npc, boolean acc) {
    complete(c, npc, null, acc);
  }
  
  public void complete(MapleCharacter c, int npc, Integer selection, boolean acc) {
    if (getId() == 100855) {
      forceComplete(c, npc, true);
      return;
    } 
    if (c.getMap() != null && (this.autoPreComplete || checkNPCOnMap(c, npc)) && canComplete(c, Integer.valueOf(npc))) {
      for (MapleQuestAction a : this.completeActs) {
        if (!a.checkEnd(c, selection))
          return; 
      } 
      forceComplete(c, npc, acc);
      for (MapleQuestAction a : this.completeActs)
        a.runEnd(c, selection); 
      if (this.id == 100707)
        c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(80003016), 1, (byte)1); 
      c.getClient().getSession().writeAndFlush(CField.EffectPacket.showNormalEffect(c, 15, true));
      c.getMap().broadcastMessage(c, CField.EffectPacket.showNormalEffect(c, 15, true), false);
    } 
  }
  
  public void forfeit(MapleCharacter c) {
    forfeit(c, false);
  }
  
  public void forfeit(MapleCharacter c, boolean acc) {
    if ((acc ? c.getClient().getQuest(this).getStatus() : c.getQuest(this).getStatus()) != 1)
      return; 
    MapleQuestStatus oldStatus = c.getQuest(this);
    if (acc)
      oldStatus = c.getClient().getQuest(this); 
    MapleQuestStatus newStatus = new MapleQuestStatus(this, 0);
    newStatus.setForfeited(oldStatus.getForfeited() + 1);
    newStatus.setCompletionTime(oldStatus.getCompletionTime());
    if (acc) {
      c.getClient().updateQuest(newStatus, true);
    } else {
      c.updateQuest(newStatus, true);
    } 
  }
  
  public void forceStart(MapleCharacter c, int npc, String customData) {
    forceStart(c, npc, customData, false);
  }
  
  public void forceStart(MapleCharacter c, int npc, String customData, boolean acc) {
    MapleQuestStatus newStatus = new MapleQuestStatus(this, (byte)1, npc);
    newStatus.setForfeited(acc ? c.getClient().getQuest(this).getForfeited() : c.getQuest(this).getForfeited());
    newStatus.setCompletionTime(acc ? c.getClient().getQuest(this).getCompletionTime() : c.getQuest(this).getCompletionTime());
    newStatus.setCustomData(customData);
    if (acc) {
      c.getClient().updateQuest(newStatus, true);
    } else {
      c.updateQuest(newStatus, true);
    } 
  }
  
  public void forceComplete(MapleCharacter c, int npc) {
    forceComplete(c, npc, true, false);
  }
  
  public void forceComplete(MapleCharacter c, int npc, boolean update) {
    forceComplete(c, npc, update, false);
  }
  
  public void forceComplete(MapleCharacter c, int npc, boolean update, boolean acc) {
    if (getId() != 100855 && ((getId() >= 100829 && getId() <= 100863) || (getId() >= 501460 && getId() <= 501592))) {
      if (getId() >= 100829 && getId() <= 100863) {
        if (c.getKeyValue(getId(), "state") != 2L)
          c.setKeyValue(getId(), "state", "1"); 
      } else if (c.getClient().getCustomKeyValue(getId(), "state") != 2L) {
        c.getClient().setCustomKeyValue(getId(), "state", "1");
      } 
      return;
    } 
    MapleQuestStatus newStatus = new MapleQuestStatus(this, (byte)2, npc);
    if (acc) {
      c.getClient().updateQuest(newStatus, true);
    } else {
      c.updateQuest(newStatus, true);
    } 
    newStatus.setForfeited(acc ? c.getClient().getQuest(this).getForfeited() : c.getQuest(this).getForfeited());
    c.getClient().getSession().writeAndFlush(CField.EffectPacket.showNormalEffect(c, 15, true));
    c.getMap().broadcastMessage(c, CField.EffectPacket.showNormalEffect(c, 15, false), false);
    if (this.id == 100707)
      c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(80003016), 1, (byte)1); 
    if (c.getClient().getQuestStatus(100825) == 2)
      if (this.id >= 34130 && this.id <= 34150) {
        if (c.getKeyValue(34127, "count") >= 5L && 
          c.getKeyValue(100837, "state") != 2L)
          c.setKeyValue(100837, "state", "1"); 
      } else if (this.id >= 39017 && this.id <= 39033) {
        if (c.getKeyValue(39016, "count") >= 3L && 
          c.getKeyValue(100839, "state") != 2L)
          c.setKeyValue(100839, "state", "1"); 
      } else if (this.id >= 34381 && this.id <= 34394) {
        if (c.getKeyValue(34380, "count") >= 3L && 
          c.getKeyValue(100841, "state") != 2L)
          c.setKeyValue(100841, "state", "1"); 
      } else if (this.id >= 39038 && this.id <= 34394) {
        if (c.getKeyValue(39037, "count") >= 3L && 
          c.getKeyValue(100843, "state") != 2L)
          c.setKeyValue(100843, "state", "1"); 
      } else if (this.id >= 34276 && this.id <= 34295) {
        if (c.getKeyValue(34298, "count") >= 3L && 
          c.getKeyValue(100845, "state") != 2L)
          c.setKeyValue(100845, "state", "1"); 
      } else if (this.id >= 34780 && this.id <= 34799) {
        if (c.getKeyValue(34775, "count") >= 3L)
          c.setKeyValue(100846, "state", "1"); 
      } else if (this.id >= 39820 && this.id <= 39825) {//mush
        if (c.getKeyValue(34776, "count") >= 3L &&
          c.getKeyValue(100847, "state") != 2L)
          c.setKeyValue(100847, "state", "1"); 
      } else if (this.id >= 39923 && this.id <= 39928) {
        if (c.getKeyValue(100848, "state") != 2L)
          c.setKeyValue(100848, "state", "1"); 
      } else if (this.id >= 38151 && this.id <= 38156) {
        if (c.getKeyValue(100849, "state") != 2L)
          c.setKeyValue(100849, "state", "1"); 
      }  
    switch (this.id) {
      case 16403:
      case 16404:
      case 16405:
      case 16406:
      case 16407:
      case 16408:
        c.setKeyValue(500862, "unlockBox", String.valueOf(c.getKeyValue(500862, "unlockBox") + 1L));
        c.setKeyValue(500862, "str", "일일 미션 " + (c.getKeyValue(500862, "openBox") + 1L) + "개 완료! " + (c.getKeyValue(500862, "openBox") + 1L) + "번째 상자를 클릭하세요!");
        if (c.getKeyValue(500862, "M5") > 0L) {
          c.setKeyValue(500862, "M6", "1");
          break;
        } 
        if (c.getKeyValue(500862, "M4") > 0L) {
          c.setKeyValue(500862, "M5", "1");
          break;
        } 
        if (c.getKeyValue(500862, "M3") > 0L) {
          c.setKeyValue(500862, "M4", "1");
          break;
        } 
        if (c.getKeyValue(500862, "M2") > 0L) {
          c.setKeyValue(500862, "M3", "1");
          break;
        } 
        if (c.getKeyValue(500862, "M1") > 0L) {
          c.setKeyValue(500862, "M2", "1");
          break;
        } 
        c.setKeyValue(500862, "M1", "1");
        break;
    } 
  }
  
  public int getId() {
    return this.id;
  }
  
  public Map<Integer, Integer> getRelevantMobs() {
    return this.relevantMobs;
  }
  
  private boolean checkNPCOnMap(MapleCharacter player, int npcid) {
    return true;
  }
  
  public int getMedalItem() {
    return this.viewMedalItem;
  }
  
  public boolean isBlocked() {
    return this.blocked;
  }
  
  public enum MedalQuest {
    Beginner(29005, 29015, 15, new int[] { 
        104000000, 104010001, 100000006, 104020000, 100000000, 100010000, 100040000, 100040100, 101010103, 101020000, 
        101000000, 102000000, 101030104, 101030406, 102020300, 103000000, 102050000, 103010001, 103030200, 110000000 }, "초보"),
    ElNath(29006, 29012, 50, new int[] { 200000000, 200010100, 200010300, 200080000, 200080100, 211000000, 211030000, 211040300, 211041200, 211041800 }, "엘나스 산맥"),
    LudusLake(29007, 29012, 40, new int[] { 222000000, 222010400, 222020000, 220000000, 220020300, 220040200, 221020701, 221000000, 221030600, 221040400 }, "루더스 호수"),
    Underwater(29008, 29012, 40, new int[] { 230000000, 230010400, 230010200, 230010201, 230020000, 230020201, 230030100, 230040000, 230040200, 230040400 }, "해저"),
    MuLung(29009, 29012, 50, new int[] { 251000000, 251010200, 251010402, 251010500, 250010500, 250010504, 250000000, 250010300, 250010304, 250020300 }, "무릉도원"),
    NihalDesert(29010, 29012, 70, new int[] { 261030000, 261020401, 261020000, 261010100, 261000000, 260020700, 260020300, 260000000, 260010600, 260010300 }, "니할사막"),
    MinarForest(29011, 29012, 70, new int[] { 240000000, 240010200, 240010800, 240020401, 240020101, 240030000, 240040400, 240040511, 240040521, 240050000 }, "미나르숲"),
    Sleepywood(29014, 29015, 50, new int[] { 105040300, 105070001, 105040305, 105090200, 105090300, 105090301, 105090312, 105090500, 105090900, 105080000 }, "슬리피우드");
    
    public int questid;
    
    public int level;
    
    public int lquestid;
    
    public String questname;
    
    public int[] maps;
    
    MedalQuest(int questid, int lquestid, int level, int[] maps, String questname) {
      this.questid = questid;
      this.level = level;
      this.lquestid = lquestid;
      this.maps = maps;
      this.questname = questname;
    }
  }
  
  public boolean hasStartScript() {
    return this.scriptedStart;
  }
  
  public boolean hasEndScript() {
    return this.customend;
  }
  
  public boolean isAutoCompleteAction() {
    return this.autoCompleteAction;
  }
  
  public void setAutoCompleteAction(boolean autoCompleteAction) {
    this.autoCompleteAction = autoCompleteAction;
  }
}
