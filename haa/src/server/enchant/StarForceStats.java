package server.enchant;

import java.util.ArrayList;
import java.util.List;
import tools.Pair;

public class StarForceStats {
  private int flag;
  
  private List<Pair<EnchantFlag, Integer>> stats = new ArrayList<>();
  
  public StarForceStats(List<Pair<EnchantFlag, Integer>> stats) {
    setStats(stats);
    setFlag();
  }
  
  public List<Pair<EnchantFlag, Integer>> getStats() {
    return this.stats;
  }
  
  public void setStats(List<Pair<EnchantFlag, Integer>> stats) {
    for (Pair<EnchantFlag, Integer> stat : stats)
      this.stats.add(stat); 
  }
  
  public int getFlag() {
    return this.flag;
  }
  
  public Pair<EnchantFlag, Integer> getFlag(EnchantFlag flag) {
    for (Pair<EnchantFlag, Integer> stat : this.stats) {
      if (flag.getValue() == ((EnchantFlag)stat.left).getValue())
        return stat; 
    } 
    return null;
  }
  
  public void setFlag() {
    int flag = 0;
    if (getFlag(EnchantFlag.Watk) != null)
      flag |= EnchantFlag.Watk.getValue(); 
    if (getFlag(EnchantFlag.Matk) != null)
      flag |= EnchantFlag.Matk.getValue(); 
    if (getFlag(EnchantFlag.Str) != null)
      flag |= EnchantFlag.Str.getValue(); 
    if (getFlag(EnchantFlag.Dex) != null)
      flag |= EnchantFlag.Dex.getValue(); 
    if (getFlag(EnchantFlag.Int) != null)
      flag |= EnchantFlag.Int.getValue(); 
    if (getFlag(EnchantFlag.Luk) != null)
      flag |= EnchantFlag.Luk.getValue(); 
    if (getFlag(EnchantFlag.Wdef) != null)
      flag |= EnchantFlag.Wdef.getValue(); 
    if (getFlag(EnchantFlag.Mdef) != null)
      flag |= EnchantFlag.Mdef.getValue(); 
    if (getFlag(EnchantFlag.Hp) != null)
      flag |= EnchantFlag.Hp.getValue(); 
    if (getFlag(EnchantFlag.Mp) != null)
      flag |= EnchantFlag.Mp.getValue(); 
    if (getFlag(EnchantFlag.Acc) != null)
      flag |= EnchantFlag.Acc.getValue(); 
    if (getFlag(EnchantFlag.Avoid) != null)
      flag |= EnchantFlag.Avoid.getValue(); 
    this.flag = flag;
  }
  
  public void setFlag(int flag) {
    this.flag = flag;
  }
}
