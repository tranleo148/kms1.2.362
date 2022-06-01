package client.inventory;

public class AuctionItem {
  private int auctionId;
  
  private int auctionType;
  
  private int accountId;
  
  private int characterId;
  
  private int state;
  
  private int worldId;
  
  private int bidUserId = 0;
  
  private int nexonOid = -1;
  
  private int deposit = 0;
  
  private int sStype = 0;
  
  private int bidWorld = 0;
  
  private long price;
  
  private long secondPrice = 0L;
  
  private long directPrice;
  
  private long endDate;
  
  private long registerDate;
  
  private String name;
  
  private String bidUserName = "";
  
  private Item item;
  
  private AuctionHistory history;
  
  public int getAuctionId() {
    return this.auctionId;
  }
  
  public void setAuctionId(int auctionId) {
    this.auctionId = auctionId;
  }
  
  public int getAuctionType() {
    return this.auctionType;
  }
  
  public void setAuctionType(int auctionType) {
    this.auctionType = auctionType;
  }
  
  public int getAccountId() {
    return this.accountId;
  }
  
  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }
  
  public int getCharacterId() {
    return this.characterId;
  }
  
  public void setCharacterId(int characterId) {
    this.characterId = characterId;
  }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int state) {
    this.state = state;
  }
  
  public int getWorldId() {
    return this.worldId;
  }
  
  public void setWorldId(int worldId) {
    this.worldId = worldId;
  }
  
  public int getBidUserId() {
    return this.bidUserId;
  }
  
  public void setBidUserId(int bidUserId) {
    this.bidUserId = bidUserId;
  }
  
  public int getNexonOid() {
    return this.nexonOid;
  }
  
  public void setNexonOid(int nexonOid) {
    this.nexonOid = nexonOid;
  }
  
  public int getDeposit() {
    return this.deposit;
  }
  
  public void setDeposit(int deposit) {
    this.deposit = deposit;
  }
  
  public int getsStype() {
    return this.sStype;
  }
  
  public void setsStype(int sStype) {
    this.sStype = sStype;
  }
  
  public int getBidWorld() {
    return this.bidWorld;
  }
  
  public void setBidWorld(int bidWorld) {
    this.bidWorld = bidWorld;
  }
  
  public long getPrice() {
    return this.price;
  }
  
  public void setPrice(long price) {
    this.price = price;
  }
  
  public long getSecondPrice() {
    return this.secondPrice;
  }
  
  public void setSecondPrice(long secondPrice) {
    this.secondPrice = secondPrice;
  }
  
  public long getDirectPrice() {
    return this.directPrice;
  }
  
  public void setDirectPrice(long directPrice) {
    this.directPrice = directPrice;
  }
  
  public long getEndDate() {
    return this.endDate;
  }
  
  public void setEndDate(long endDate) {
    this.endDate = endDate;
  }
  
  public long getRegisterDate() {
    return this.registerDate;
  }
  
  public void setRegisterDate(long registerDate) {
    this.registerDate = registerDate;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getBidUserName() {
    return this.bidUserName;
  }
  
  public void setBidUserName(String bidUserName) {
    this.bidUserName = bidUserName;
  }
  
  public Item getItem() {
    return this.item;
  }
  
  public void setItem(Item item) {
    this.item = item;
  }
  
  public AuctionHistory getHistory() {
    return this.history;
  }
  
  public void setHistory(AuctionHistory history) {
    this.history = history;
  }
}
