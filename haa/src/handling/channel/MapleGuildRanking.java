package handling.channel;

import handling.world.World;
import handling.world.guild.MapleGuild;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import tools.Triple;

public class MapleGuildRanking {
  private static MapleGuildRanking instance = new MapleGuildRanking();
  
  private List<GuildRankingInfo> honorRank = new LinkedList<>();
  
  private List<GuildRankingInfo> flagRaceRank = new LinkedList<>();
  
  private List<GuildRankingInfo> culvertRank = new LinkedList<>();
  
  private static long lastReloadTime = 0L;
  
  public static MapleGuildRanking getInstance() {
    return instance;
  }
  
  public void load() {
    reload();
  }
  
  public List<GuildRankingInfo> getHonorRank() {
    return this.honorRank;
  }
  
  private void reload() {
    this.honorRank.clear();
    this.flagRaceRank.clear();
    this.culvertRank.clear();
    List<Triple<String, Integer, Integer>> honorranks = new ArrayList<>();
    List<Triple<String, Integer, Integer>> flagRaceRanks = new ArrayList<>();
    List<Triple<String, Integer, Integer>> culvertRanks = new ArrayList<>();
    for (MapleGuild g : World.Guild.getGuilds()) {
      if (g.getWeekReputation() > 0)
        honorranks.add(new Triple<>(g.getName(), Integer.valueOf(g.getWeekReputation()), Integer.valueOf(g.getId()))); 
      if (g.getGuildScore() > 0.0D)
        culvertRanks.add(new Triple<>(g.getName(), Integer.valueOf((int)g.getGuildScore()), Integer.valueOf(g.getId()))); 
    } 
    int i;
    for (i = 0; i < honorranks.size() - 1; i++) {
      for (int j = 0; j < honorranks.size() - i - 1; j++) {
        if (((Integer)((Triple)honorranks.get(j)).getMid()).intValue() < ((Integer)((Triple)honorranks.get(j + 1)).getMid()).intValue()) {
          String names = (String)((Triple)honorranks.get(j + 1)).getLeft();
          int chridtmp = ((Integer)((Triple)honorranks.get(j + 1)).getMid()).intValue();
          int chrpointtmp = ((Integer)((Triple)honorranks.get(j + 1)).getRight()).intValue();
          honorranks.set(j + 1, honorranks.get(j));
          honorranks.set(j, new Triple<>(names, Integer.valueOf(chridtmp), Integer.valueOf(chrpointtmp)));
        } 
      } 
    } 
    for (i = 0; i < culvertRanks.size() - 1; i++) {
      for (int j = 0; j < culvertRanks.size() - i - 1; j++) {
        if (((Integer)((Triple)culvertRanks.get(j)).getMid()).intValue() < ((Integer)((Triple)culvertRanks.get(j + 1)).getMid()).intValue()) {
          String names = (String)((Triple)culvertRanks.get(j + 1)).getLeft();
          int chridtmp = ((Integer)((Triple)culvertRanks.get(j + 1)).getMid()).intValue();
          int chrpointtmp = ((Integer)((Triple)culvertRanks.get(j + 1)).getRight()).intValue();
          culvertRanks.set(j + 1, culvertRanks.get(j));
          culvertRanks.set(j, new Triple<>(names, Integer.valueOf(chridtmp), Integer.valueOf(chrpointtmp)));
        } 
      } 
    } 
    for (Triple<String, Integer, Integer> list : honorranks)
      this.honorRank.add(new GuildRankingInfo(list.getLeft(), ((Integer)list.getMid()).intValue(), ((Integer)list.getRight()).intValue())); 
    for (Triple<String, Integer, Integer> list : culvertRanks)
      this.culvertRank.add(new GuildRankingInfo(list.getLeft(), ((Integer)list.getMid()).intValue(), ((Integer)list.getRight()).intValue())); 
  }
  
  public List<GuildRankingInfo> getCulvertRank() {
    return this.culvertRank;
  }
  
  public void setCulvertRank(List<GuildRankingInfo> culvertRank) {
    this.culvertRank = culvertRank;
  }
  
  public List<GuildRankingInfo> getFlagRaceRank() {
    return this.flagRaceRank;
  }
  
  public void setFlagRaceRank(List<GuildRankingInfo> flagRaceRank) {
    this.flagRaceRank = flagRaceRank;
  }
  
  public static class GuildRankingInfo {
    private String name;
    
    private int score;
    
    private int id;
    
    public GuildRankingInfo(String name, int score, int id) {
      this.name = name;
      this.score = score;
      this.id = id;
    }
    
    public String getName() {
      return this.name;
    }
    
    public int getScore() {
      return this.score;
    }
    
    public int getId() {
      return this.id;
    }
    
    public void setId(int gid) {
      this.id = gid;
    }
  }
}
