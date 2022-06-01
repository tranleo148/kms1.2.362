package server.marriage;

import client.inventory.Item;
import java.util.ArrayList;
import java.util.List;

public class MarriageDataEntry {
  private int marriageId;
  
  private boolean newData;
  
  private int groomId;
  
  private int brideId;
  
  private String groomName;
  
  private String brideName;
  
  private boolean groomWhisCheck = false;
  
  private boolean brideWhisCheck = false;
  
  private int status;
  
  private int weddingStatus;
  
  private List<String> groomWishList;
  
  private List<String> brideWishList;
  
  private List<Item> groomWeddingPresents;
  
  private List<Item> brideWeddingPresents;
  
  private List<Integer> reservedPeople;
  
  private List<Integer> enteredPeople;
  
  private MarriageTicketType ticketType;
  
  private long EngagementTime;
  
  private long MakeReservationTime;
  
  private long RequestDivorceTimeGroom;
  
  private long RequestDivorceTimeBride;
  
  public MarriageDataEntry(int id, boolean newData) {
    this.marriageId = id;
    this.newData = newData;
    this.weddingStatus = 0;
    this.ticketType = null;
    this.MakeReservationTime = 0L;
    this.RequestDivorceTimeBride = 0L;
    this.RequestDivorceTimeGroom = 0L;
  }
  
  public int getMarriageId() {
    return this.marriageId;
  }
  
  public void setGroomId(int in) {
    this.groomId = in;
  }
  
  public int getGroomId() {
    return this.groomId;
  }
  
  public void setBrideId(int in) {
    this.brideId = in;
  }
  
  public int getBrideId() {
    return this.brideId;
  }
  
  public void setGroomName(String str) {
    this.groomName = str;
  }
  
  public String getGroomName() {
    return this.groomName;
  }
  
  public void setBrideName(String str) {
    this.brideName = str;
  }
  
  public String getBrideName() {
    return this.brideName;
  }
  
  public void setStatus(int stat) {
    this.status = stat;
  }
  
  public int getStatus() {
    return this.status;
  }
  
  public boolean isNewData() {
    return this.newData;
  }
  
  public void setNewData(boolean l) {
    this.newData = l;
  }
  
  public void setWeddingStatus(int g) {
    this.weddingStatus = g;
  }
  
  public int getWeddingStatus() {
    return this.weddingStatus;
  }
  
  public List<String> getGroomWishList() {
    if (this.groomWishList == null)
      this.groomWishList = new ArrayList<>(); 
    return this.groomWishList;
  }
  
  public List<String> getBrideWishList() {
    if (this.brideWishList == null)
      this.brideWishList = new ArrayList<>(); 
    return this.brideWishList;
  }
  
  public List<Item> getGroomPresentList() {
    if (this.groomWeddingPresents == null)
      this.groomWeddingPresents = new ArrayList<>(); 
    return this.groomWeddingPresents;
  }
  
  public List<Item> getBridePresentList() {
    if (this.brideWeddingPresents == null)
      this.brideWeddingPresents = new ArrayList<>(); 
    return this.brideWeddingPresents;
  }
  
  public List<Integer> getReservedPeopleList() {
    if (this.reservedPeople == null)
      this.reservedPeople = new ArrayList<>(); 
    return this.reservedPeople;
  }
  
  public List<Integer> getEnteredPeopleList() {
    if (this.enteredPeople == null)
      this.enteredPeople = new ArrayList<>(); 
    return this.enteredPeople;
  }
  
  public void setTicketType(MarriageTicketType type) {
    this.ticketType = type;
  }
  
  public MarriageTicketType getTicketType() {
    return this.ticketType;
  }
  
  public void setEngagementTime(long time) {
    this.EngagementTime = time;
  }
  
  public long getEngagementTime() {
    return this.EngagementTime;
  }
  
  public void setMakeReservationTime(long time) {
    this.MakeReservationTime = time;
  }
  
  public long getMakeReservationTime() {
    return this.MakeReservationTime;
  }
  
  public void setDivorceTimeGroom(long time) {
    this.RequestDivorceTimeGroom = time;
  }
  
  public long getDivorceTimeGroom() {
    return this.RequestDivorceTimeGroom;
  }
  
  public void setDivorceTimeBride(long time) {
    this.RequestDivorceTimeBride = time;
  }
  
  public long getDivorceTimeBride() {
    return this.RequestDivorceTimeBride;
  }
  
  public int getPartnerId(int id) {
    if (id == getGroomId())
      return getBrideId(); 
    if (id == getBrideId())
      return getGroomId(); 
    return -1;
  }
  
  public void setBWhisCheck(boolean a) {
    if (this.brideWishList != null && this.brideWishList.size() > 0) {
      this.brideWhisCheck = true;
      return;
    } 
    this.brideWhisCheck = a;
  }
  
  public boolean getBWhisCheck() {
    return this.brideWhisCheck;
  }
  
  public void setGWhisCheck(boolean a) {
    if (this.groomWishList != null && this.groomWishList.size() > 0) {
      this.groomWhisCheck = true;
      return;
    } 
    this.groomWhisCheck = a;
  }
  
  public boolean getGWhisCheck() {
    return this.groomWhisCheck;
  }
}
