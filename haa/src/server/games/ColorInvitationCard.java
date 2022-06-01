package server.games;

import client.MapleCharacter;
import java.util.ArrayList;
import java.util.List;
import server.Randomizer;

public class ColorInvitationCard {
  private MapleCharacter chr;
  
  private int Combo;
  
  private int Gauge;
  
  private int Point;
  
  private int Redsuc;
  
  private int Bluesuc;
  
  private int Greensuc;
  
  private int FailCount;
  
  private int ComboCount;
  
  private boolean FeverTime;
  
  private List<Integer> CardList;
  
  public ColorInvitationCard(MapleCharacter chr) {
    this.CardList = new ArrayList<>();
    this.chr = chr;
    this.Combo = 0;
    this.Gauge = 0;
    this.Point = 0;
    this.Redsuc = 0;
    this.Bluesuc = 0;
    this.Greensuc = 0;
    this.FailCount = 0;
    this.ComboCount = 0;
    this.FeverTime = false;
    for (int i = 0; i < 1000; i++)
      this.CardList.add(Integer.valueOf(Randomizer.rand(0, 2))); 
  }
  
  public MapleCharacter getChr() {
    return this.chr;
  }
  
  public void setChr(MapleCharacter chr) {
    this.chr = chr;
  }
  
  public int getCombo() {
    return this.Combo;
  }
  
  public void setCombo(int Combo) {
    this.Combo = Combo;
  }
  
  public int getGauge() {
    return this.Gauge;
  }
  
  public void setGauge(int Gauge) {
    this.Gauge = Gauge;
  }
  
  public int getPoint() {
    return this.Point;
  }
  
  public void setPoint(int Point) {
    this.Point = Point;
  }
  
  public int getRedsuc() {
    return this.Redsuc;
  }
  
  public void setRedsuc(int Redsuc) {
    this.Redsuc = Redsuc;
  }
  
  public int getBluesuc() {
    return this.Bluesuc;
  }
  
  public void setBluesuc(int Bluesuc) {
    this.Bluesuc = Bluesuc;
  }
  
  public int getGreensuc() {
    return this.Greensuc;
  }
  
  public void setGreensuc(int Greensuc) {
    this.Greensuc = Greensuc;
  }
  
  public int getFailCount() {
    return this.FailCount;
  }
  
  public void setFailCount(int FailCount) {
    this.FailCount = FailCount;
  }
  
  public int getComboCount() {
    return this.ComboCount;
  }
  
  public void setComboCount(int ComboCount) {
    this.ComboCount = ComboCount;
  }
  
  public boolean isFeverTime() {
    return this.FeverTime;
  }
  
  public void setFeverTime(boolean FeverTime) {
    this.FeverTime = FeverTime;
  }
  
  public List<Integer> getCardList() {
    return this.CardList;
  }
  
  public void setCardList(List<Integer> CardList) {
    this.CardList = CardList;
  }
}
