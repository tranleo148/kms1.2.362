package handling.world;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class AccountIdChannelPair implements Externalizable, Comparable<AccountIdChannelPair> {
  private int accId = 0;
  
  private int channel = 1;
  
  public AccountIdChannelPair(int accId, int channel) {
    this.accId = accId;
    this.channel = channel;
  }
  
  public int getAcountId() {
    return this.accId;
  }
  
  public int getChannel() {
    return this.channel;
  }
  
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.accId = in.readInt();
    this.channel = in.readByte();
  }
  
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(this.accId);
    out.writeByte(this.channel);
  }
  
  public int compareTo(AccountIdChannelPair o) {
    return this.channel - o.channel;
  }
  
  public AccountIdChannelPair() {}
}
