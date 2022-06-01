package client.inventory;

public class AuctionHistory {
  private int auctionId;
  
  private int accountId;
  
  private int characterId;
  
  private int itemId;
  
  private int state;
  
  private int deposit;
  
  private int quantity;
  
  private int worldId;
  
  private int BidUserId;
  
  private long id;
  
  private long price;
  
  private long buyTime;
  
  private String BidUserName;
  
  public long getId() {
    return this.id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public long getPrice() {
    return this.price;
  }
  
  public void setPrice(long price) {
    this.price = price;
  }
  
  public long getBuyTime() {
    return this.buyTime;
  }
  
  public void setBuyTime(long buyTime) {
    this.buyTime = buyTime;
  }
  
  public int getAuctionId() {
    return this.auctionId;
  }
  
  public void setAuctionId(int auctionId) {
    this.auctionId = auctionId;
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
  
  public int getItemId() {
    return this.itemId;
  }
  
  public void setItemId(int itemId) {
    this.itemId = itemId;
  }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int state) {
    this.state = state;
  }
  
  public int getDeposit() {
    return this.deposit;
  }
  
  public void setDeposit(int deposit) {
    this.deposit = deposit;
  }
  
  public int getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public int getWorldId() {
    return this.worldId;
  }
  
  public void setWorldId(int worldId) {
    this.worldId = worldId;
  }
  
  public int getBidUserId() {
    return this.BidUserId;
  }
  
  public void setBidUserId(int BidUserId) {
    this.BidUserId = BidUserId;
  }
  
  public String getBidUserName() {
    return this.BidUserName;
  }
  
  public void setBidUserName(String BidUserName) {
    this.BidUserName = BidUserName;
  }
}
