package discord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DiscordClientStorage {
  private final Map<String, DiscordClient> Clients = new ConcurrentHashMap<>();
  
  public final void registerClient(DiscordClient c, String s) {
    this.Clients.put(s.toLowerCase(), c);
  }
  
  public final void deregisterClient(DiscordClient c) {
    if (c != null) {
      Iterator<DiscordClient> main = getClients().iterator();
      while (main.hasNext()) {
        DiscordClient cli = main.next();
        if (cli.getAddressIP().equals(c.getAddressIP()))
          removeClient(cli.getAddressIP()); 
      } 
    } 
  }
  
  public final DiscordClient getClientByName(String c) {
    if (this.Clients.get(c) != null)
      return this.Clients.get(c); 
    return null;
  }
  
  public DiscordClient getClient(String c) {
    return this.Clients.get(c.toLowerCase());
  }
  
  public void removeClient(String c) {
    this.Clients.remove(c.toLowerCase());
  }
  
  public final List<DiscordClient> getClients() {
    Iterator<DiscordClient> itr = this.Clients.values().iterator();
    List<DiscordClient> asd = new ArrayList<>();
    while (itr.hasNext())
      asd.add(itr.next()); 
    return asd;
  }
}
