package client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import server.SecondaryStatEffect;
import tools.Pair;

public class SecondaryStatValueHolder {
  public SecondaryStatEffect effect;
  
  public long startTime;
  
  public int value;
  
  public int localDuration;
  
  public int cid;
  
  public List<Pair<Integer, Integer>> list1 = new ArrayList<>();
  
  public List<Pair<Integer, Integer>> list2 = new ArrayList<>();
  public ScheduledFuture<?> schedule;//와드
  
  public SecondaryStatValueHolder(SecondaryStatEffect effect, long startTime, int value, int localDuration, int cid, List<Pair<Integer, Integer>> list1, List<Pair<Integer, Integer>> list2) {
    this.effect = effect;
    this.startTime = startTime;
    this.value = value;
    this.localDuration = localDuration;
    this.cid = cid;
    this.list1 = list1;
    this.list2 = list2;
  }
}
