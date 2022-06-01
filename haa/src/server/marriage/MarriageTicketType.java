package server.marriage;

public enum MarriageTicketType {
  CheapTicket(5251004),
  SweetieTicket(5251005),
  PremiumTicket(5251006);
  
  private int itemid;
  
  private int invitationItemId;
  
  private int invitationQuantity;
  
  private int receiptItemId;
  
  private int invitedItemId;
  
  private int buffEffectItemId;
  
  MarriageTicketType(int i) {
    this.itemid = i;
    if (i == 5251004) {
      this.invitationItemId = 4211000;
      this.invitationQuantity = 10;
      this.invitedItemId = 4212000;
      this.receiptItemId = 4214000;
      this.buffEffectItemId = 2022196;
    } else if (i == 5251005) {
      this.invitationItemId = 4211001;
      this.invitationQuantity = 20;
      this.invitedItemId = 4212001;
      this.receiptItemId = 4214001;
      this.buffEffectItemId = 2022197;
    } else if (i == 5251006) {
      this.invitationItemId = 4211002;
      this.invitationQuantity = 30;
      this.invitedItemId = 4212002;
      this.receiptItemId = 4214002;
      this.buffEffectItemId = 2022200;
    } 
  }
  
  public int getItemId() {
    return this.itemid;
  }
  
  public int getInvitationItemId() {
    return this.invitationItemId;
  }
  
  public int getInvitationQuantity() {
    return this.invitationQuantity;
  }
  
  public int getReceiptItemId() {
    return this.receiptItemId;
  }
  
  public int getInvitedItemId() {
    return this.invitedItemId;
  }
  
  public int getBuffEffectItemId() {
    return this.buffEffectItemId;
  }
  
  public static MarriageTicketType getTypeById(int id) {
    if (id == 5251006)
      return PremiumTicket; 
    if (id == 5251005)
      return SweetieTicket; 
    if (id == 5251004)
      return CheapTicket; 
    return null;
  }
}
