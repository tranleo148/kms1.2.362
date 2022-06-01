package handling.channel;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import handling.world.CharacterTransfer;
import handling.world.CheaterData;
import handling.world.World;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import server.Timer;

public class PlayerStorage {
  private final Map<String, MapleCharacter> nameToChar = new ConcurrentHashMap<>();
  
  private final Map<Integer, MapleCharacter> idToChar = new ConcurrentHashMap<>();
  
  private final Map<Integer, MapleClient> idToClient = new ConcurrentHashMap<>();
  
  private final Map<Integer, CharacterTransfer> PendingCharacter = new ConcurrentHashMap<>();
  
  public PlayerStorage() {
    Timer.PingTimer.getInstance().register(new PersistingTask(), 60000L);
  }
  
  public final Map<Integer, MapleCharacter> getAllCharacters() {
    return this.idToChar;
  }
  
  public final void registerPlayer(MapleCharacter chr) {
    this.nameToChar.put(chr.getName().toLowerCase(), chr);
    this.idToChar.put(Integer.valueOf(chr.getId()), chr);
    this.idToClient.put(Integer.valueOf(chr.getAccountID()), chr.getClient());
    World.Find.register(chr.getId(), chr.getAccountID(), chr.getName(), chr.getClient().getChannel());
  }
  
  public final void registerPendingPlayer(CharacterTransfer chr, int playerid) {
    this.PendingCharacter.put(Integer.valueOf(playerid), chr);
  }
  
  public final void deregisterPlayer(MapleCharacter chr) {
    this.nameToChar.remove(chr.getName().toLowerCase());
    this.idToChar.remove(Integer.valueOf(chr.getId()));
    this.idToClient.remove(Integer.valueOf(chr.getAccountID()));
    World.Find.forceDeregister(chr.getId(), chr.getAccountID(), chr.getName());
  }
  
  public final void deregisterPlayer(int idz, int accIdz, String namez) {
    this.nameToChar.remove(namez.toLowerCase());
    this.idToChar.remove(Integer.valueOf(idz));
    this.idToClient.remove(Integer.valueOf(accIdz));
    World.Find.forceDeregister(idz, accIdz, namez);
  }
  
  public final int pendingCharacterSize() {
    return this.PendingCharacter.size();
  }
  
  public final void deregisterPendingPlayer(int charid) {
    this.PendingCharacter.remove(Integer.valueOf(charid));
  }
  
  public final CharacterTransfer getPendingCharacter(int charid) {
    return this.PendingCharacter.remove(Integer.valueOf(charid));
  }
  
  public final MapleCharacter getCharacterByName(String name) {
    return this.nameToChar.get(name.toLowerCase());
  }
  
  public final MapleCharacter getCharacterById(int id) {
    return this.idToChar.get(Integer.valueOf(id));
  }
  
  public final MapleClient getClientById(int id) {
    return this.idToClient.get(Integer.valueOf(id));
  }
  
  public final int getConnectedClients() {
    return this.idToChar.size();
  }
  
  public final List<CheaterData> getCheaters() {
    List<CheaterData> cheaters = new ArrayList<>();
    return cheaters;
  }
  
  public final List<CheaterData> getReports() {
    List<CheaterData> cheaters = new ArrayList<>();
    return cheaters;
  }
  
  public final String getOnlinePlayers(boolean byGM) {
    StringBuilder sb = new StringBuilder();
    if (byGM) {
      Iterator<MapleCharacter> itr = this.nameToChar.values().iterator();
      while (itr.hasNext()) {
        sb.append(MapleCharacterUtil.makeMapleReadable(((MapleCharacter)itr.next()).getName()));
        sb.append(", ");
      } 
      sb.insert(0, "동접 (" + this.nameToChar.size() + "명) [");
      sb.append("]");
    } else {
      Iterator<MapleCharacter> itr = this.nameToChar.values().iterator();
      while (itr.hasNext()) {
        MapleCharacter chr = itr.next();
        if (!chr.isGM()) {
          sb.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
          sb.append(", ");
        } 
      } 
    } 
    return sb.toString();
  }
  
  public final List<String> getOnlinePlayers2(boolean byGM) {
    List<String> list = new ArrayList<>();
    if (byGM) {
      int i = 0;
      Iterator<MapleCharacter> itr = this.nameToChar.values().iterator();
      while (itr.hasNext()) {
        i++;
        list.add(((MapleCharacter)itr.next()).getName());
      } 
    } else {
      Iterator<MapleCharacter> itr = this.nameToChar.values().iterator();
      while (itr.hasNext()) {
        MapleCharacter chr = itr.next();
        if (!chr.isGM())
          list.add(((MapleCharacter)itr.next()).getName()); 
      } 
    } 
    return list;
  }
  
  public final void disconnectAll() {
    disconnectAll(false);
  }
  
  public final void disconnectAll(boolean checkGM) {
    Iterator<MapleCharacter> itr = this.nameToChar.values().iterator();
    while (itr.hasNext()) {
      MapleCharacter chr = itr.next();
      try {
        chr.getClient().disconnect(true, false, true);
        chr.getClient().getSession().close();
        World.Find.forceDeregister(chr.getId(), chr.getAccountID(), chr.getName());
        System.out.println(chr.getName() + " 캐릭터를 셧다운 했습니다.");
      } catch (Exception e) {
        e.printStackTrace();
      } 
    } 
  }
  
  public final void broadcastPacket(byte[] data) {
    Iterator<MapleCharacter> itr = this.nameToChar.values().iterator();
    while (itr.hasNext())
      ((MapleCharacter)itr.next()).getClient().getSession().writeAndFlush(data); 
  }
  
  public final void broadcastSmegaPacket(byte[] data) {
    Iterator<MapleCharacter> itr = this.nameToChar.values().iterator();
    while (itr.hasNext()) {
      MapleCharacter chr = itr.next();
      if (chr.getClient().isLoggedIn() && chr.getSmega())
        chr.getClient().getSession().writeAndFlush(data); 
    } 
  }
  
  public final void broadcastGMPacket(byte[] data) {
    Iterator<MapleCharacter> itr = this.nameToChar.values().iterator();
    while (itr.hasNext()) {
      MapleCharacter chr = itr.next();
      if (chr.getClient().isLoggedIn() && chr.isIntern())
        chr.getClient().getSession().writeAndFlush(data); 
    } 
  }
  
  public class PersistingTask implements Runnable {
    public void run() {
      long currenttime = System.currentTimeMillis();
      Iterator<Map.Entry<Integer, CharacterTransfer>> itr = PlayerStorage.this.PendingCharacter.entrySet().iterator();
      List<Map.Entry<Integer, CharacterTransfer>> removes = new ArrayList<>();
      while (itr.hasNext()) {
        Map.Entry<Integer, CharacterTransfer> target = itr.next();
        if (currenttime - ((CharacterTransfer)target.getValue()).TranferTime > 40000L)
          removes.add(target); 
      } 
      for (Map.Entry<Integer, CharacterTransfer> remove : removes)
        PlayerStorage.this.PendingCharacter.remove(remove.getKey()); 
    }
  }
}
