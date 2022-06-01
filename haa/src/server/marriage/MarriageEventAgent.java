package server.marriage;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import handling.world.World;
import java.util.concurrent.ScheduledFuture;
import server.MapleItemInformationProvider;
import server.Timer;
import server.maps.MapleMap;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MarriageEventAgent {
  private boolean canStart = true;
  
  private boolean canEnter = true;
  
  private boolean finaleStarted = false;
  
  private int finaleLevel = 0;
  
  private MarriageDataEntry runningDataEntry = null;
  
  private ScheduledFuture<?> endSchedule = null;
  
  private ScheduledFuture<?> ceremonySchedule = null;
  
  private long endTime = 0L;
  
  private final ChannelServer cserv;
  
  private final int channel;
  
  private static final int[] weddingMaps = new int[] { 680000200, 680000210, 680000300, 680000400, 680000401 };
  
  private boolean endedCeremony = false;
  
  public MarriageEventAgent(int channel) {
    this.channel = channel;
    this.cserv = ChannelServer.getInstance(channel);
  }
  
  public void setCanStart(boolean bln) {
    this.canStart = bln;
  }
  
  public boolean canStart() {
    return this.canStart;
  }
  
  public boolean canEnter() {
    return this.canEnter;
  }
  
  public final void setCanEnter(boolean bln) {
    this.canEnter = bln;
  }
  
  public void setDataEntry(MarriageDataEntry data) {
    this.runningDataEntry = data;
  }
  
  public MarriageDataEntry getDataEntry() {
    return this.runningDataEntry;
  }
  
  public int getChannel() {
    return this.channel;
  }
  
  public void registerEvent(MapleCharacter chr) {
    if (!canStart())
      return; 
    setCanStart(false);
    setDataEntry(MarriageManager.getInstance().getMarriage(chr.getMarriageId()));
    World.Broadcast.broadcastMessage(CWvsContext.serverNotice(5, "", this.runningDataEntry.getGroomName() + "님과 " + this.runningDataEntry.getBrideName() + "님의 결혼이 " + this.channel + "채널 대성당에서 시작되려 합니다."));
    if (this.endSchedule != null)
      this.endSchedule.cancel(false); 
    this.endSchedule = Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            MarriageEventAgent.this.endEvent(false);
          }
        },  600000L);
    this.endTime = System.currentTimeMillis() + 600000L;
  }
  
  public static boolean isWeddingMap(int map) {
    for (int i : weddingMaps) {
      if (map == i)
        return true; 
    } 
    return false;
  }
  
  public void checkEnterMap(MapleCharacter chr) {
    if (this.endTime > 0L)
      chr.getClient().getSession().writeAndFlush(CField.getClock((int)((this.endTime - System.currentTimeMillis()) / 1000L))); 
  }
  
  public void checkLeaveMap(MapleCharacter chr, int newMap) {
    if (this.runningDataEntry != null && !isWeddingMap(newMap) && (
      chr.getId() == this.runningDataEntry.getGroomId() || chr.getId() == this.runningDataEntry.getBrideId()))
      endEvent(true); 
  }
  
  public void startEvent() {
    if (this.endSchedule != null)
      this.endSchedule.cancel(true); 
    this.endTime = System.currentTimeMillis() + 600000L;
    this.endSchedule = Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            MarriageEventAgent.this.finaleEvent();
          }
        },  600000L);
    final MapleMap hall = this.cserv.getMapFactory().getMap(680000210);
    hall.broadcastMessage(CField.musicChange("BgmGL/cathedral"));
    hall.broadcastMessage(CField.getClock(600));
    setCanEnter(false);
    Runnable run = new Runnable() {
        private long firstTime = System.currentTimeMillis();
        
        private int count = 0;
        
        public void run() {
          long time = System.currentTimeMillis() - this.firstTime;
          if (this.count == 0) {
            this.count++;
            String str = "오늘 우리는 두 젊은이를 축복하기 위해 모였습니다.";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
            for (MapleCharacter chr : hall.getCharacters())
              MapleItemInformationProvider.getInstance().getItemEffect(MarriageEventAgent.this.runningDataEntry.getTicketType().getBuffEffectItemId()).applyTo(chr, true); 
          } else if (this.count == 1 && time >= 10000L) {
            this.count++;
            String str = "흔히 하늘이 내린 인연은 리본돼지의 붉은 리본으로 이어져 있다고 합니다.";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
          } else if (this.count == 2 && time >= 20000L) {
            this.count++;
            String str = "제가 보기에는 이 두사람은 그 리본의 끝에 있는 서로를 찾아낸 것 같습니다.";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
          } else if (this.count == 3 && time >= 30000L) {
            this.count++;
            String str = "수 많은 여행자들 중에서 서로를 찾아낸 두 사람이야말로 진정한 행운아일겁니다.";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
          } else if (this.count == 4 && time >= 40000L) {
            this.count++;
            String str = "서로에게 찾아 온 이 행운을 행복으로 만들어가는 것이 앞으로 두 사람에게 주어진 퀘스트입니다.";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
          } else if (this.count == 5 && time >= 50000L) {
            this.count++;
            String str = "신랑, 수십번 튕김과 빽섭, 밴 속에서도 검은머리가 예티의 털처럼 하얗게 변할때까지 신부를 사랑하시겠습니까?";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
          } else if (this.count == 6 && time >= 60000L) {
            this.count++;
            String str = "신부, 신랑이 자쿰 먹튀를 하더라도 현실 칼빵을 하지 않고 영원히 신랑을 사랑하시겠습니까?";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
          } else if (this.count == 7 && time >= 70000L) {
            this.count++;
            String str = "모두의 축복 속에 두 젊은이가 부부가 되었음을 선포합니다.";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
          } else if (this.count == 8 && time >= 80000L) {
            MarriageEventAgent.this.setEndedCeremony(true);
            this.count++;
            String str = "신랑은 신부에게 키스해도 좋습니다.";
            hall.broadcastMessage(CField.startMapEffect(str, 5120025, true));
            hall.broadcastMessage(CWvsContext.yellowChat(str));
          } else if (this.count == 9 && time >= 100000L) {
            this.count++;
            hall.broadcastMessage(CField.musicChange("BgmGL/chapel"));
            hall.broadcastMessage(CField.startMapEffect(null, 0, false));
          } 
        }
      };
    this.ceremonySchedule = Timer.EventTimer.getInstance().register(run, 5000L);
  }
  
  public final void setEndedCeremony(boolean fln) {
    this.endedCeremony = fln;
  }
  
  public final boolean isEndedCeremony() {
    return this.endedCeremony;
  }
  
  public final void setFinaleStarted(boolean f) {
    this.finaleStarted = f;
  }
  
  public final boolean isFinaleStarted() {
    return this.finaleStarted;
  }
  
  public final void doNextFinale() {
    if (this.finaleLevel == 0) {
      if (this.endSchedule != null) {
        this.endSchedule.cancel(false);
        this.endSchedule = null;
      } 
      if (this.ceremonySchedule != null) {
        this.ceremonySchedule.cancel(false);
        this.ceremonySchedule = null;
      } 
      this.endTime = System.currentTimeMillis() + 300000L;
      this.endSchedule = Timer.EventTimer.getInstance().schedule(new Runnable() {
            public void run() {
              MarriageEventAgent.this.doNextFinale();
            }
          },  300000L);
      MapleMap target = this.cserv.getMapFactory().getMap(680000300);
      for (MapleCharacter chr : this.cserv.getMapFactory().getMap(680000210).getCharacters())
        chr.changeMap(target, target.getPortal(0)); 
      this.finaleLevel++;
    } else if (this.finaleLevel == 1) {
      if (this.endSchedule != null) {
        this.endSchedule.cancel(false);
        this.endSchedule = null;
      } 
      this.endTime = System.currentTimeMillis() + 180000L;
      this.endSchedule = Timer.EventTimer.getInstance().schedule(new Runnable() {
            public void run() {
              MarriageEventAgent.this.doNextFinale();
            }
          },  180000L);
      MapleMap target = this.cserv.getMapFactory().getMap(680000400);
      target.respawn(true);
      for (MapleCharacter chr : this.cserv.getMapFactory().getMap(680000300).getCharacters())
        chr.changeMap(target, target.getPortal(0)); 
      this.finaleLevel++;
      if (this.runningDataEntry.getTicketType() == MarriageTicketType.SweetieTicket)
        this.finaleLevel++; 
    } else if (this.finaleLevel == 2) {
      if (this.endSchedule != null) {
        this.endSchedule.cancel(false);
        this.endSchedule = null;
      } 
      MapleMap target = this.cserv.getMapFactory().getMap(680000401);
      MapleMap target2 = this.cserv.getMapFactory().getMap(680000500);
      target.resetFully();
      for (MapleCharacter chr : this.cserv.getMapFactory().getMap(680000400).getCharacters()) {
        if (chr.getId() != this.runningDataEntry.getBrideId() && chr.getId() != this.runningDataEntry.getGroomId()) {
          chr.changeMap(target2, target2.getPortal(0));
          continue;
        } 
        chr.changeMap(target, target.getPortal(0));
      } 
      this.finaleLevel++;
    } else if (this.finaleLevel == 3) {
      endEvent(true);
    } 
  }
  
  public final void finaleEvent() {
    if (this.runningDataEntry != null) {
      switch (this.runningDataEntry.getTicketType()) {
        case CheapTicket:
          endEvent(true);
          break;
        case SweetieTicket:
        case PremiumTicket:
          setFinaleStarted(true);
          doNextFinale();
          break;
      } 
    } else {
      endEvent(true);
    } 
  }
  
  public final void endEvent(boolean cancelSchedule) {
    if (cancelSchedule && this.endSchedule != null) {
      this.endSchedule.cancel(false);
      this.endSchedule = null;
    } 
    if (this.ceremonySchedule != null) {
      this.ceremonySchedule.cancel(false);
      this.ceremonySchedule = null;
    } 
    if (this.runningDataEntry != null) {
      this.runningDataEntry.setStatus(2);
      this.runningDataEntry.setWeddingStatus(8);
      MapleCharacter chr = this.cserv.getPlayerStorage().getCharacterById(this.runningDataEntry.getBrideId());
      if (chr != null)
        chr.dropMessage(1, "결혼이 성립되었습니다."); 
      chr = this.cserv.getPlayerStorage().getCharacterById(this.runningDataEntry.getGroomId());
      if (chr != null)
        chr.dropMessage(1, "결혼이 성립되었습니다."); 
    } 
    this.endTime = 0L;
    this.runningDataEntry = null;
    setCanStart(true);
    setCanEnter(true);
    setFinaleStarted(false);
    this.finaleLevel = 0;
    MapleMap target = this.cserv.getMapFactory().getMap(680000500);
    for (int mapid : weddingMaps) {
      for (MapleCharacter dchr : this.cserv.getMapFactory().getMap(mapid).getCharacters())
        dchr.changeMap(target, target.getPortal(0)); 
    } 
  }
}
