package server.quest;

import client.MapleCharacter;
import client.MapleQuestStatus;
import client.MapleStat;
import client.MapleTrait;
import client.Skill;
import client.SkillEntry;
import client.SkillFactory;
import client.inventory.InventoryException;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.RandomRewards;
import server.Randomizer;
import tools.FileoutputUtil;
import tools.Pair;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.SLFCGPacket;

public class MapleQuestAction implements Serializable {
  private static final long serialVersionUID = 9179541993413738569L;
  
  private MapleQuestActionType type;
  
  private MapleQuest quest;
  
  private int intStore;
  
  private List<Integer> applicableJobs;
  
  private List<QuestItem> items;
  
  private List<Triple<Integer, Integer, Integer>> skill;
  
  private List<Pair<Integer, Integer>> state;
  
  public MapleQuestAction(MapleQuestActionType type, ResultSet rse, MapleQuest quest, PreparedStatement pss, PreparedStatement psq, PreparedStatement psi) throws SQLException {
    ResultSet rs;
    this.intStore = 0;
    this.applicableJobs = new ArrayList<>();
    this.items = null;
    this.skill = null;
    this.state = null;
    this.type = type;
    this.quest = quest;
    this.intStore = rse.getInt("intStore");
    String[] jobs = rse.getString("applicableJobs").split(", ");
    if (jobs.length <= 0 && rse.getString("applicableJobs").length() > 0)
      this.applicableJobs.add(Integer.valueOf(Integer.parseInt(rse.getString("applicableJobs")))); 
    for (String j : jobs) {
      if (j.length() > 0)
        this.applicableJobs.add(Integer.valueOf(Integer.parseInt(j))); 
    } 
    switch (type) {
      case item:
        this.items = new ArrayList<>();
        psi.setInt(1, rse.getInt("uniqueid"));
        rs = psi.executeQuery();
        while (rs.next())
          this.items.add(new QuestItem(rs.getInt("itemid"), rs.getInt("count"), rs.getInt("period"), rs.getInt("gender"), rs.getInt("job"), rs.getInt("jobEx"), rs.getInt("prop"))); 
        rs.close();
        break;
      case quest:
        this.state = new ArrayList<>();
        psq.setInt(1, rse.getInt("uniqueid"));
        rs = psq.executeQuery();
        while (rs.next())
          this.state.add(new Pair<>(Integer.valueOf(rs.getInt("quest")), Integer.valueOf(rs.getInt("state")))); 
        rs.close();
        break;
      case skill:
        this.skill = new ArrayList<>();
        pss.setInt(1, rse.getInt("uniqueid"));
        rs = pss.executeQuery();
        while (rs.next())
          this.skill.add(new Triple<>(Integer.valueOf(rs.getInt("skillid")), Integer.valueOf(rs.getInt("skillLevel")), Integer.valueOf(rs.getInt("masterLevel")))); 
        rs.close();
        break;
    } 
  }
  
  private static boolean canGetItem(QuestItem item, MapleCharacter c) {
    if (item.gender != 2 && item.gender >= 0 && item.gender != c.getGender())
      return false; 
    if (item.job > 0) {
      List<Integer> code = getJobBy5ByteEncoding(item.job);
      boolean jobFound = false;
      for (Iterator<Integer> iterator = code.iterator(); iterator.hasNext(); ) {
        int codec = ((Integer)iterator.next()).intValue();
        if (codec / 100 == c.getJob() / 100) {
          jobFound = true;
          break;
        } 
      } 
      if (!jobFound && item.jobEx > 0) {
        List<Integer> codeEx = getJobBySimpleEncoding(item.jobEx);
        for (Iterator<Integer> iterator1 = codeEx.iterator(); iterator1.hasNext(); ) {
          int codec = ((Integer)iterator1.next()).intValue();
          if (codec / 100 % 10 == c.getJob() / 100 % 10) {
            jobFound = true;
            break;
          } 
        } 
      } 
      return jobFound;
    } 
    return true;
  }
  
  public final boolean RestoreLostItem(MapleCharacter c, int itemid) {
    if (this.type == MapleQuestActionType.item)
      for (QuestItem item : this.items) {
        if (item.itemid == itemid) {
          if (!c.haveItem(item.itemid, item.count, true, false))
            MapleInventoryManipulator.addById(c.getClient(), item.itemid, (short)item.count, "Obtained from quest (Restored) " + this.quest.getId() + " on " + FileoutputUtil.CurrentReadable_Date()); 
          return true;
        } 
      }  
    return false;
  }
  
  public void runStart(MapleCharacter c, Integer extSelection) {
    MapleQuestStatus status;
    Map<Integer, Integer> props;
    int selection;
    int extNum;
    Map<Skill, SkillEntry> sa;
    int fameGain;
    int tobuff;
    int sp_val;
    switch (this.type) {
      case exp:
        status = c.getQuest(this.quest);
        if (status.getForfeited() > 0)
          break; 
        c.gainExp((this.intStore * GameConstants.getExpRate_Quest(c.getLevel()) * (c.getStat()).questBonus * (c.getTrait(MapleTrait.MapleTraitType.sense).getLevel() * 3 / 10 + 100) / 100), true, true, true);
        break;
      case item:
        props = new HashMap<>();
        for (QuestItem item : this.items) {
          if (item.prop > 0 && canGetItem(item, c))
            for (int i = 0; i < item.prop; i++)
              props.put(Integer.valueOf(props.size()), Integer.valueOf(item.itemid));  
        } 
        selection = 0;
        extNum = 0;
        if (props.size() > 0)
          selection = ((Integer)props.get(Integer.valueOf(Randomizer.nextInt(props.size())))).intValue(); 
        for (QuestItem item : this.items) {
          if (!canGetItem(item, c))
            continue; 
          int id = item.itemid;
          if (item.prop != -2 && (
            (item.prop == -1) ? (
            extSelection != null && extSelection.intValue() != extNum++) : (

            
            id != selection)))
            continue; 
          short count = (short)item.count;
          if (count < 0) {
            try {
              MapleInventoryManipulator.removeById(c.getClient(), GameConstants.getInventoryType(id), id, count * -1, true, false);
            } catch (InventoryException ie) {
              System.err.println("[h4x] Completing a quest without meeting the requirements" + ie);
            } 
            c.getClient().getSession().writeAndFlush(CField.EffectPacket.showCharmEffect(c, id, 1, true, ""));
            continue;
          } 
          int period = item.period / 1440;
          String name = MapleItemInformationProvider.getInstance().getName(id);
          if (id / 10000 == 114 && name != null && name.length() > 0) {
            String msg = "[" + name + "] 훈장을 얻었습니다!!";
            c.getClient().send(SLFCGPacket.getItemTopMsg(id, msg));
            c.dropMessage(5, msg);
          } 
          MapleInventoryManipulator.addById(c.getClient(), id, count, "", null, period, "퀘스트 보상을 통해 얻은 아이템 입니다. // 퀘스트 번호 : " + this.quest.getId() + " " + FileoutputUtil.CurrentReadable_Date(), false);
          c.getClient().getSession().writeAndFlush(CField.EffectPacket.showCharmEffect(c, id, 1, true, ""));
        } 
        break;
      case nextQuest:
      case money:
        status = c.getQuest(this.quest);
        if (status.getForfeited() > 0)
          break; 
        c.gainMeso(this.intStore, true, true);
        break;
      case quest:
        for (Pair<Integer, Integer> q : this.state)
          c.updateQuest(new MapleQuestStatus(MapleQuest.getInstance(((Integer)q.left).intValue()), ((Integer)q.right).intValue())); 
        break;
      case skill:
        sa = new HashMap<>();
        for (Triple<Integer, Integer, Integer> skills : this.skill) {
          int skillid = ((Integer)skills.left).intValue();
          int skillLevel = ((Integer)skills.mid).intValue();
          int masterLevel = ((Integer)skills.right).intValue();
          Skill skillObject = SkillFactory.getSkill(skillid);
          boolean found = false;
          for (Iterator<Integer> iterator = this.applicableJobs.iterator(); iterator.hasNext(); ) {
            int applicableJob = ((Integer)iterator.next()).intValue();
            if (c.getJob() == applicableJob) {
              found = true;
              break;
            } 
          } 
          if (skillObject.isBeginnerSkill() || found)
            sa.put(skillObject, new SkillEntry((byte)Math.max(skillLevel, c.getSkillLevel(skillObject)), (byte)Math.max(masterLevel, c.getMasterLevel(skillObject)), SkillFactory.getDefaultSExpiry(skillObject))); 
        } 
        c.changeSkillsLevel(sa);
        break;
      case pop:
        status = c.getQuest(this.quest);
        if (status.getForfeited() > 0)
          break; 
        fameGain = this.intStore;
        c.addFame(fameGain);
        c.updateSingleStat(MapleStat.FAME, c.getFame());
        c.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.getShowFameGain(fameGain));
        break;
      case buffItemID:
        status = c.getQuest(this.quest);
        if (status.getForfeited() > 0)
          break; 
        tobuff = this.intStore;
        if (tobuff <= 0)
          break; 
        MapleItemInformationProvider.getInstance().getItemEffect(tobuff).applyTo(c, true);
        break;
      case sp:
        status = c.getQuest(this.quest);
        if (status.getForfeited() > 0)
          break; 
        sp_val = this.intStore;
        if (this.applicableJobs.size() > 0) {
          int finalJob = 0;
          for (Iterator<Integer> iterator = this.applicableJobs.iterator(); iterator.hasNext(); ) {
            int job_val = ((Integer)iterator.next()).intValue();
            if (c.getJob() >= job_val && job_val > finalJob)
              finalJob = job_val; 
          } 
          if (finalJob == 0) {
            c.gainSP(sp_val);
            break;
          } 
          c.gainSP(sp_val, GameConstants.getSkillBook(finalJob, 0));
          break;
        } 
        c.gainSP(sp_val);
        break;
      case charmEXP:
      case charismaEXP:
      case craftEXP:
      case insightEXP:
      case senseEXP:
      case willEXP:
        status = c.getQuest(this.quest);
        if (status.getForfeited() > 0)
          break; 
        c.getTrait(MapleTrait.MapleTraitType.getByQuestName(this.type.name())).addExp(this.intStore, c);
        break;
    } 
  }
  
  public boolean checkEnd(MapleCharacter c, Integer extSelection) {
    Map<Integer, Integer> props;
    long meso;
    int selection;
    int extNum;
    byte eq;
    byte use;
    byte setup;
    byte etc;
    byte cash;
    switch (this.type) {
      case item:
        props = new HashMap<>();
        for (QuestItem item : this.items) {
          if (item.prop > 0 && canGetItem(item, c))
            for (int i = 0; i < item.prop; i++)
              props.put(Integer.valueOf(props.size()), Integer.valueOf(item.itemid));  
        } 
        selection = 0;
        extNum = 0;
        if (props.size() > 0)
          selection = ((Integer)props.get(Integer.valueOf(Randomizer.nextInt(props.size())))).intValue(); 
        eq = 0;
        use = 0;
        setup = 0;
        etc = 0;
        cash = 0;
        for (QuestItem item : this.items) {
          if (!canGetItem(item, c))
            continue; 
          int id = item.itemid;
          if (item.prop != -2 && (
            (item.prop == -1) ? (
            extSelection != null && extSelection.intValue() != extNum++) : (

            
            id != selection)))
            continue; 
          short count = (short)item.count;
          if (count < 0) {
            if (!c.haveItem(id, count, false, true)) {
              c.dropMessage(1, "You are short of some item to complete quest.");
              return false;
            } 
            continue;
          } 
          if (MapleItemInformationProvider.getInstance().isPickupRestricted(id) && c.haveItem(id, 1, true, false)) {
            c.dropMessage(1, "You have this item already: " + MapleItemInformationProvider.getInstance().getName(id));
            return false;
          } 
          switch (GameConstants.getInventoryType(id)) {
            case EQUIP:
              eq = (byte)(eq + 1);
            case USE:
              use = (byte)(use + 1);
            case SETUP:
              setup = (byte)(setup + 1);
            case ETC:
              etc = (byte)(etc + 1);
            case CASH:
              cash = (byte)(cash + 1);
          } 
        } 
        if (c.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < eq) {
          c.dropMessage(1, "Please make space for your Equip inventory.");
          return false;
        } 
        if (c.getInventory(MapleInventoryType.USE).getNumFreeSlot() < use) {
          c.dropMessage(1, "Please make space for your Use inventory.");
          return false;
        } 
        if (c.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < setup) {
          c.dropMessage(1, "Please make space for your Setup inventory.");
          return false;
        } 
        if (c.getInventory(MapleInventoryType.ETC).getNumFreeSlot() < etc) {
          c.dropMessage(1, "Please make space for your Etc inventory.");
          return false;
        } 
        if (c.getInventory(MapleInventoryType.CASH).getNumFreeSlot() < cash) {
          c.dropMessage(1, "Please make space for your Cash inventory.");
          return false;
        } 
        return true;
      case money:
        meso = this.intStore;
        if (c.getMeso() + meso < 0L)
          return false; 
        if (meso < 0L && c.getMeso() < Math.abs(meso)) {
          c.dropMessage(1, "Insufficient meso.");
          return false;
        } 
        return true;
    } 
    return true;
  }
  
  public void runEnd(MapleCharacter c, Integer extSelection) {
    Map<Integer, Integer> props;
    Map<Skill, SkillEntry> sa;
    int selection;
    int fameGain;
    int tobuff;
    int sp_val;
    int extNum;
    switch (this.type) {
      case exp:
        c.gainExp((this.intStore * GameConstants.getExpRate_Quest(c.getLevel()) * (c.getStat()).questBonus * (c.getTrait(MapleTrait.MapleTraitType.sense).getLevel() * 3 / 10 + 100) / 100), true, true, true);
        break;
      case item:
        props = new HashMap<>();
        for (QuestItem item : this.items) {
          if (item.prop > 0 && canGetItem(item, c))
            for (int i = 0; i < item.prop; i++)
              props.put(Integer.valueOf(props.size()), Integer.valueOf(item.itemid));  
        } 
        selection = 0;
        extNum = 0;
        if (props.size() > 0)
          selection = ((Integer)props.get(Integer.valueOf(Randomizer.nextInt(props.size())))).intValue(); 
        for (QuestItem item : this.items) {
          if (!canGetItem(item, c))
            continue; 
          int id = item.itemid;
          if (item.prop != -2 && (
            (item.prop == -1) ? (
            extSelection != null && extSelection.intValue() != extNum++) : (

            
            id != selection)))
            continue; 
          short count = (short)item.count;
          if (count < 0) {
            MapleInventoryManipulator.removeById(c.getClient(), GameConstants.getInventoryType(id), id, count * -1, true, false);
            c.getClient().getSession().writeAndFlush(CField.EffectPacket.showCharmEffect(c, id, 1, true, ""));
            continue;
          } 
          int period = item.period / 1440;
          String name = MapleItemInformationProvider.getInstance().getName(id);
          if (id / 10000 == 114 && name != null && name.length() > 0) {
            String msg = "[" + name + "] 훈장을 획득 했습니다!!";
            c.getClient().send(SLFCGPacket.getItemTopMsg(id, msg));
            c.dropMessage(5, msg);
          } 
          MapleInventoryManipulator.addById(c.getClient(), id, count, "", null, period, "");
          c.getClient().getSession().writeAndFlush(CField.EffectPacket.showCharmEffect(c, id, 1, true, ""));
        } 
        break;
      case money:
        c.gainMeso(this.intStore, true, true);
        break;
      case quest:
        for (Pair<Integer, Integer> q : this.state)
          c.updateQuest(new MapleQuestStatus(MapleQuest.getInstance(((Integer)q.left).intValue()), ((Integer)q.right).intValue())); 
        break;
      case skill:
        sa = new HashMap<>();
        for (Triple<Integer, Integer, Integer> skills : this.skill) {
          int skillid = ((Integer)skills.left).intValue();
          int skillLevel = ((Integer)skills.mid).intValue();
          int masterLevel = ((Integer)skills.right).intValue();
          Skill skillObject = SkillFactory.getSkill(skillid);
          boolean found = false;
          for (Iterator<Integer> iterator = this.applicableJobs.iterator(); iterator.hasNext(); ) {
            int applicableJob = ((Integer)iterator.next()).intValue();
            if (c.getJob() == applicableJob) {
              found = true;
              break;
            } 
          } 
          if (skillObject.isBeginnerSkill() || found)
            sa.put(skillObject, new SkillEntry((byte)Math.max(skillLevel, c.getSkillLevel(skillObject)), (byte)Math.max(masterLevel, c.getMasterLevel(skillObject)), SkillFactory.getDefaultSExpiry(skillObject))); 
        } 
        c.changeSkillsLevel(sa);
        break;
      case pop:
        fameGain = this.intStore;
        c.addFame(fameGain);
        c.updateSingleStat(MapleStat.FAME, c.getFame());
        c.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.getShowFameGain(fameGain));
        break;
      case buffItemID:
        tobuff = this.intStore;
        if (tobuff <= 0)
          break; 
        MapleItemInformationProvider.getInstance().getItemEffect(tobuff).applyTo(c, true);
        break;
      case sp:
        sp_val = this.intStore;
        if (this.applicableJobs.size() > 0) {
          int finalJob = 0;
          for (Iterator<Integer> iterator = this.applicableJobs.iterator(); iterator.hasNext(); ) {
            int job_val = ((Integer)iterator.next()).intValue();
            if (c.getJob() >= job_val && job_val > finalJob)
              finalJob = job_val; 
          } 
          if (finalJob == 0) {
            c.gainSP(sp_val);
            break;
          } 
          c.gainSP(sp_val, GameConstants.getSkillBook(finalJob, 0));
          break;
        } 
        c.gainSP(sp_val);
        break;
      case charmEXP:
      case charismaEXP:
      case craftEXP:
      case insightEXP:
      case senseEXP:
      case willEXP:
        c.getTrait(MapleTrait.MapleTraitType.getByQuestName(this.type.name())).addExp(this.intStore, c);
        break;
    } 
  }
  
  private static List<Integer> getJobBy5ByteEncoding(int encoded) {
    List<Integer> ret = new ArrayList<>();
    if ((encoded & 0x1) != 0)
      ret.add(Integer.valueOf(0)); 
    if ((encoded & 0x2) != 0)
      ret.add(Integer.valueOf(100)); 
    if ((encoded & 0x4) != 0)
      ret.add(Integer.valueOf(200)); 
    if ((encoded & 0x8) != 0)
      ret.add(Integer.valueOf(300)); 
    if ((encoded & 0x10) != 0)
      ret.add(Integer.valueOf(400)); 
    if ((encoded & 0x20) != 0)
      ret.add(Integer.valueOf(500)); 
    if ((encoded & 0x400) != 0)
      ret.add(Integer.valueOf(1000)); 
    if ((encoded & 0x800) != 0)
      ret.add(Integer.valueOf(1100)); 
    if ((encoded & 0x1000) != 0)
      ret.add(Integer.valueOf(1200)); 
    if ((encoded & 0x2000) != 0)
      ret.add(Integer.valueOf(1300)); 
    if ((encoded & 0x4000) != 0)
      ret.add(Integer.valueOf(1400)); 
    if ((encoded & 0x8000) != 0)
      ret.add(Integer.valueOf(1500)); 
    if ((encoded & 0x20000) != 0) {
      ret.add(Integer.valueOf(2001));
      ret.add(Integer.valueOf(2200));
    } 
    if ((encoded & 0x100000) != 0) {
      ret.add(Integer.valueOf(2000));
      ret.add(Integer.valueOf(2001));
    } 
    if ((encoded & 0x200000) != 0)
      ret.add(Integer.valueOf(2100)); 
    if ((encoded & 0x400000) != 0) {
      ret.add(Integer.valueOf(2001));
      ret.add(Integer.valueOf(2200));
    } 
    if ((encoded & 0x40000000) != 0) {
      ret.add(Integer.valueOf(3000));
      ret.add(Integer.valueOf(3200));
      ret.add(Integer.valueOf(3300));
      ret.add(Integer.valueOf(3500));
    } 
    return ret;
  }
  
  private static List<Integer> getJobBySimpleEncoding(int encoded) {
    List<Integer> ret = new ArrayList<>();
    if ((encoded & 0x1) != 0)
      ret.add(Integer.valueOf(200)); 
    if ((encoded & 0x2) != 0)
      ret.add(Integer.valueOf(300)); 
    if ((encoded & 0x4) != 0)
      ret.add(Integer.valueOf(400)); 
    if ((encoded & 0x8) != 0)
      ret.add(Integer.valueOf(500)); 
    return ret;
  }
  
  public MapleQuestActionType getType() {
    return this.type;
  }
  
  public String toString() {
    return this.type.toString();
  }
  
  public List<Triple<Integer, Integer, Integer>> getSkills() {
    return this.skill;
  }
  
  public List<QuestItem> getItems() {
    return this.items;
  }
  
  public static class QuestItem {
    public int itemid;
    
    public int count;
    
    public int period;
    
    public int gender;
    
    public int job;
    
    public int jobEx;
    
    public int prop;
    
    public QuestItem(int itemid, int count, int period, int gender, int job, int jobEx, int prop) {
      if (RandomRewards.getTenPercent().contains(Integer.valueOf(itemid)))
        count += Randomizer.nextInt(3); 
      this.itemid = itemid;
      this.count = count;
      this.period = period;
      this.gender = gender;
      this.job = job;
      this.jobEx = jobEx;
      this.prop = prop;
    }
  }
}
