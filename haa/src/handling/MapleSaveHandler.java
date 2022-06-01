package handling;

import client.MapleCharacter;
import client.SecondaryStat;
import client.SkillFactory;
import client.inventory.AuctionHistory;
import client.inventory.AuctionItem;
import constants.KoreaCalendar;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.auction.AuctionServer;
import handling.auction.handler.AuctionHistoryIdentifier;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.farm.FarmServer;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import server.games.BattleGroundGameHandler;
import server.life.MapleMonsterInformationProvider;
import tools.CurrentTime;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PacketHelper;

public class MapleSaveHandler implements Runnable {
  private long lastSaveAuctionTime = 0L;
  
  private long lastClearDropTime = 0L;
  
  public static int date;
  
  public static int count;
  
  public static int month;
  
  private boolean minigamegive;
  
  private boolean give = false;
  
  private boolean show = false;
  
  public static Map<String, Integer> ids = new HashMap<>();
  
  public static Map<String, String> todayKeyValues = new HashMap<>();
  
  public static String[][] weekKeyValues = new String[][] { { "db_lastweek", "0" }, { "dojo", "0" }, { "dojo_time", "0" } };
  
  public static String[] clientDateKeyValues = new String[] { 
      "dailyGiftComplete", "hotelMapleToday", "ht", "mPark", "mpark_t", "jump_1", "jump_2", "jump_3", "day_reborn_1", "day_reborn_2", 
      "day_qitem", "day_summer_a", "day_summer_e", "day_colorlens", "day_MedalC", "day_MedalD" , "minigame"};
  
  public static List<Integer> updateAuctionClients = new ArrayList<>();
  
  static {
    todayKeyValues.put("muto", "0");
    todayKeyValues.put("arcane_quest_2", "-1");
    todayKeyValues.put("arcane_quest_3", "-1");
    todayKeyValues.put("arcane_quest_4", "-1");
    todayKeyValues.put("arcane_quest_5", "-1");
    todayKeyValues.put("arcane_quest_6", "-1");
    todayKeyValues.put("arcane_quest_7", "-1");
    todayKeyValues.put("NettPyramid", "0");
    todayKeyValues.put("linkMobCount", "0");
  }
  
  public MapleSaveHandler() {
    date = CurrentTime.getDay();
    month = CurrentTime.getMonth();
    count = 0;
    this.lastSaveAuctionTime = System.currentTimeMillis();
    this.lastClearDropTime = System.currentTimeMillis();
    System.out.println("[Loading Completed] MapleSaveHandler Start");
    for (String event : ChannelServer.getInstance(1).getEventSM().getEvents().keySet())
      todayKeyValues.put(event, "0"); 
  }
  
  public static void runningAuctionItems(long time) {
    updateAuctionClients.clear();
    Iterator<AuctionItem> items = AuctionServer.getItems().values().iterator();
    while (items.hasNext()) {
      AuctionItem aItem = items.next();
      if (aItem.getEndDate() < time && aItem.getState() == 0) {
        aItem.setState(4);
        AuctionHistory history = new AuctionHistory();
        history.setAuctionId(aItem.getAuctionId());
        history.setAccountId(aItem.getAccountId());
        history.setCharacterId(aItem.getCharacterId());
        history.setItemId(aItem.getItem().getItemId());
        history.setState(aItem.getState());
        history.setPrice(aItem.getPrice());
        history.setBuyTime(System.currentTimeMillis());
        history.setDeposit(aItem.getDeposit());
        history.setQuantity(aItem.getItem().getQuantity());
        history.setWorldId(aItem.getWorldId());
        history.setId(AuctionHistoryIdentifier.getInstance());
        aItem.setHistory(history);
        updateAuctionClients.add(Integer.valueOf(aItem.getAccountId()));
      } 
    } 
  }
  
  public void run() {
    long time = System.currentTimeMillis();
    runningAuctionItems(time);
    if (time - this.lastSaveAuctionTime >= 900000L) {
      count++;
      AuctionServer.saveItems();
      System.out.println("[알림] 서버 오픈 이후 " + (count * 15 / 60) + "시간 " + (count * 15 % 60) + "분 경과하였습니다.");
      this.lastSaveAuctionTime = time;
    } 
    Iterator<ChannelServer> channels = ChannelServer.getAllInstances().iterator();
    int dd = CurrentTime.getDay();
    int mon = CurrentTime.getDate();
    if (date != dd) {
      date = dd;
      ids.clear();
      reset(channels);
      month = mon;
    } 
    if (time - this.lastClearDropTime >= 3600000L) {
      this.lastClearDropTime = time;
      MapleMonsterInformationProvider.getInstance().clearDrops();
      AuctionServer.saveItems();
      System.out.println("드롭 데이터를 초기화했습니다.");
    } 
   
                while (channels.hasNext()) {
            ChannelServer cs = channels.next();
            Iterator<MapleCharacter> chrs = cs.getPlayerStorage().getAllCharacters().values().iterator();
            while (chrs.hasNext()) {
    
             MapleCharacter chr = chrs.next();

                if (chr.isAlive()) {
                    List<Integer> prevEffects = chr.getPrevBonusEffect();
                    List<Integer> curEffects = chr.getBonusEffect();

                    for (int i = 0; i < curEffects.size(); i++) {
                        if (prevEffects.get(i) != curEffects.get(i)) {
                            chr.cancelEffectFromBuffStat(SecondaryStat.IndieDamR, 80002419);
                            chr.cancelEffectFromBuffStat(SecondaryStat.IndieExp, 80002419);
                            chr.cancelEffectFromBuffStat(SecondaryStat.DropRate, 80002419);
                            chr.cancelEffectFromBuffStat(SecondaryStat.MesoUp, 80002419);
                            chr.cancelEffectFromBuffStat(SecondaryStat.IndieCD, 80002419);
                            chr.cancelEffectFromBuffStat(SecondaryStat.IndieBDR, 80002419);
                            chr.cancelEffectFromBuffStat(SecondaryStat.IndieAllStatR, 80002419);
                            chr.cancelEffectFromBuffStat(SecondaryStat.IndiePmdR, 80002419);
                            SkillFactory.getSkill(80002419).getEffect(1).applyTo(chr);
                            chr.getStat().recalcLocalStats(chr);
                            break;
                        }
                    }
                }
            }
        }
    if ((new Date()).getHours() >= 10 && (new Date()).getHours() <= 24)
      if (((new Date()).getMinutes() == 15 || (new Date()).getMinutes() == 45) && !this.minigamegive) {
        this.minigamegive = true;
        if ((new Date()).getHours() == 21)
          BattleGroundGameHandler.BattleGroundIinviTation(); 
      } else if ((new Date()).getMinutes() != 15 && (new Date()).getMinutes() != 45 && this.minigamegive == true) {
        this.minigamegive = false;
      }  
    if (ServerConstants.Event_MapleLive && (((
      new Date()).getHours() >= 10 && (new Date()).getHours() <= 24) || (new Date()).getHours() < 2))
      if ((new Date()).getMinutes() == 29 && !this.show) {
        this.show = true;
        String s = ((new Date()).getHours() % 2 == 0) ? "블루" : "핑크";
        String msg = "메이플 LIVE " + s + " 스튜디오 생방송이 시작됩니다 방송 시작 1분 전!";
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
          for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
            if (player != null && player.getName() != null)
              player.getClient().send(CField.QuestMsg(msg, 343, 30000, 100825)); 
          } 
        } 
      } else if (this.show) {
        this.show = false;
      }  
    if ((new Date()).getHours() == 21 && (new Date()).getMinutes() == 0 && !this.give) {
      this.give = true;
      List<MapleCharacter> chrs = new ArrayList<>();
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
          if (player != null && player.getName() != null && 
            !chrs.contains(player))
            chrs.add(player); 
        } 
      } 
      for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
        if (csplayer != null && csplayer.getName() != null && 
          !chrs.contains(csplayer))
          chrs.add(csplayer); 
      } 
      for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
        if (csplayer != null && csplayer.getName() != null && 
          !chrs.contains(csplayer))
          chrs.add(csplayer); 
      } 
      for (MapleCharacter csplayer : FarmServer.getPlayerStorage().getAllCharacters().values()) {
        if (csplayer != null && csplayer.getName() != null && 
          !chrs.contains(csplayer))
          chrs.add(csplayer); 
      } 
      for (MapleCharacter chr2 : chrs) {
        chr2.gainCabinetItem(2630442, 1);
        chr2.dropMessage(1, "[HOT] Happy !  핫타임 - 접속 보상이 지급되었습니다. 메이플 보관함을 확인해주세요.");
        chr2.dropMessage(6, "[HOT] Happy !  핫타임 - 접속 보상이 지급되었습니다. 메이플 보관함을 확인해주세요.");
      } 
      
    } else if ((new Date()).getHours() != 21 && (new Date()).getMinutes() != 0 && this.give == true) {
      this.give = false;
    } 
    if (((new Date()).getHours() == 19 && (new Date()).getMinutes() == 50) || ((new Date()).getHours() == 20 && (new Date()).getMinutes() == 0) || ((new Date()).getHours() == 21 && (new Date()).getMinutes() == 50) || ((new Date()).getHours() == 22 && (new Date()).getMinutes() == 0))
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        for (MapleCharacter hp : cserv.getPlayerStorage().getAllCharacters().values()) {
          if ((new Date()).getHours() == 19 && (new Date()).getMinutes() == 50) {
            if (hp.getSkillCustomValue0(20200720) == 0L) {
              hp.FeverTime(true, true);
              hp.setSkillCustomInfo(20200720, 1L, 0L);
            } 
            continue;
          } 
          if ((new Date()).getHours() == 20 && (new Date()).getMinutes() == 0) {
            ServerConstants.feverTime = true;
            cserv.JuhunFever(true);
            if (hp.getSkillCustomValue0(20200720) == 0L || hp.getSkillCustomValue0(20200720) == 1L) {
              hp.FeverTime(true, false);
              hp.setSkillCustomInfo(20200720, 2L, 0L);
            } 
            continue;
          } 
          if ((new Date()).getHours() == 21 && (new Date()).getMinutes() == 50) {
            if (hp.getSkillCustomValue0(20200720) == 0L || hp.getSkillCustomValue0(20200720) == 1L || hp.getSkillCustomValue0(20200720) == 2L) {
              hp.FeverTime(false, true);
              hp.setSkillCustomInfo(20200720, 3L, 0L);
            } 
            continue;
          } 
          if ((new Date()).getHours() == 22 && (new Date()).getMinutes() == 0) {
            ServerConstants.feverTime = false;
            cserv.JuhunFever(false);
            if (hp.getSkillCustomValue0(20200720) == 0L || hp.getSkillCustomValue0(20200720) == 1L || hp.getSkillCustomValue0(20200720) == 2L || hp.getSkillCustomValue0(20200720) == 3L) {
              hp.FeverTime(false, false);
              hp.setSkillCustomInfo(20200720, 4L, 0L);
            } 
          } 
        } 
      }  
    while (channels.hasNext()) {
      ChannelServer cs = channels.next();
      Iterator<MapleCharacter> chrs = cs.getPlayerStorage().getAllCharacters().values().iterator();
      while (chrs.hasNext()) {
        MapleCharacter chr = chrs.next();
        if (updateAuctionClients.contains(Integer.valueOf(chr.getClient().getAccID()))) {
          chr.getClient().getSession().writeAndFlush(CWvsContext.AlarmAuction(0, null));
          updateAuctionClients.remove(chr.getClient().getAccID());
        } 
      } 
    } 
  }
  
    public static void reset(Iterator<ChannelServer> channels) {
        Object ps;
        Connection con;
        KoreaCalendar kc = new KoreaCalendar();
        ArrayList<MapleGuild> noblessPoint = new ArrayList<MapleGuild>();
        for (MapleGuild g : World.Guild.getGuilds()) {
            int[] guildskill;
            GregorianCalendar after;
            g.setBeforeAttance(g.getAfterAttance());
            g.setAfterAttance(0);
            long keys = g.getLastResetDay();
            GregorianCalendar clear = new GregorianCalendar((int)keys / 10000, (int)(keys % 10000L / 100L) - 1, (int)keys % 100);
            Calendar ocal = Calendar.getInstance();
            int yeal = clear.get(1);
            int days = clear.get(5);
            int day = ocal.get(7);
            int day2 = clear.get(7);
            int maxday = ((Calendar)clear).getMaximum(5);
            int month = clear.get(2);
            int check = day2 == 7 ? 2 : (day2 == 6 ? 3 : (day2 == 5 ? 4 : (day2 == 4 ? 5 : (day2 == 3 ? 6 : (day2 == 2 ? 7 : (day2 == 1 ? 1 : 0))))));
            int afterday = days + check;
            if (afterday > maxday) {
                afterday -= maxday;
                ++month;
            }
            if (month > 12) {
                ++yeal;
                month = 1;
            }
            if ((after = new GregorianCalendar(yeal, month, afterday)).getTimeInMillis() >= System.currentTimeMillis()) continue;
            if (g.getGuildScore() > 0.0) {
                noblessPoint.add(g);
            }
            int[] arrn = guildskill = new int[]{91001022, 91001023, 91001024, 91001025};
            int n = arrn.length;
            for (int i = 0; i < n; ++i) {
                Integer skillid = arrn[i];
                if (g.getSkillLevel(skillid) <= 0) continue;
                g.getGuildSkills().remove(skillid);
                g.broadcast(CWvsContext.GuildPacket.guildSkillPurchased(g.getId(), skillid, 0, -1L, "메이플 운영자", "메이플 운영자"));
            }
            g.writeToDB(false);
            g.setWeekReputation(0);
            g.setNoblessSkillPoint(0);
            g.setLastResetDay(Integer.parseInt(kc.getYears() + kc.getMonths() + kc.getDays()));
            g.broadcast(CWvsContext.GuildPacket.showGuildInfo(g));
        }
    if (Calendar.getInstance().get(7) == 2) {
      Connection connection = null;
      PreparedStatement preparedStatement1 = null, ps1 = null;
      ResultSet resultSet1 = null, rs1 = null;
      try {
        connection = DatabaseConnection.getConnection();
        preparedStatement1 = connection.prepareStatement("SELECT * FROM dojorankings order by floor DESC, time DESC", 1005, 1008);
        resultSet1 = preparedStatement1.executeQuery();
        resultSet1.last();
        resultSet1.first();
        for (int i = 1; i <= resultSet1.getRow(); i++) {
          int id = resultSet1.getInt("playerid");
          String name = "";
          int accid = 0;
          int itemid = (i == 3) ? 1143850 : ((i == 2) ? 1143851 : 1143852);
          int itemid2 = (i == 3) ? 1672086 : ((i == 2) ? 1672085 : 1672083);
          if (World.getChar(id) != null) {
            World.getChar(id).gainCabinetItemPlayer(itemid, 1, 7, "무릉도장 랭커 보상 입니다. 보관 기간 내에 수령하지 않을 시 보관함에서 사라집니다.");
            World.getChar(id).gainCabinetItemPlayer(itemid2, 1, 7, "무릉도장 랭커 보상 입니다. 보관 기간 내에 수령하지 않을 시 보관함에서 사라집니다.");
            World.getChar(id).setKeyValue(100466, "Score", "0");
            World.getChar(id).setKeyValue(100466, "Floor", "0");
          } else {
            ps1 = connection.prepareStatement("UPDATE questInfo SET customData = ? WHERE quest = ? and characterid = ?");
            ps1.setString(1, "Score=0;Floor=0;");
            ps1.setInt(2, 100466);
            ps1.setInt(3, id);
            ps1.executeUpdate();
            ps1 = connection.prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps1.setInt(1, id);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
              accid = rs1.getInt("accountid");
              name = rs1.getString("name");
            } 
            for (int i2 = 0; i2 < 2; i2++) {
              ps1 = connection.prepareStatement("INSERT INTO `cabinet` (`accountid`, `itemid`, `count`, `bigname`, `smallname`, `savetime`, `delete`, `playerid`, `name`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
              ps1.setLong(1, accid);
              ps1.setInt(2, (i2 == 0) ? itemid : itemid2);
              ps1.setInt(3, 1);
              ps1.setString(4, "[The Black]");
              ps1.setString(5, "무릉도장 랭커 보상 입니다. 보관 기간 내에 수령하지 않을 시 보관함에서 사라집니다.");
              ps1.setLong(6, PacketHelper.getKoreanTimestamp(System.currentTimeMillis() + 604800000L));
              ps1.setInt(7, 0);
              ps1.setInt(8, id);
              ps1.setString(9, name);
              ps1.execute();
            } 
          } 
          if (i >= 3)
            break; 
          resultSet1.next();
        } 
        resultSet1.close();
        preparedStatement1.close();
        ps1 = connection.prepareStatement("DELETE FROM dojorankings");
        ps1.executeUpdate();
        ps1.close();
        rs1.close();
        connection.close();
      } catch (SQLException sQLException) {
      
      } finally {
        try {
          if (connection != null)
            connection.close(); 
          if (preparedStatement1 != null)
            preparedStatement1.close(); 
          if (resultSet1 != null)
            resultSet1.close(); 
          if (rs1 != null)
            rs1.close(); 
          if (ps1 != null)
            ps1.close(); 
        } catch (SQLException sQLException) {}
        World.Broadcast.broadcastMessage(CWvsContext.serverMessage(6, 1, "", "무릉도장 랭킹이 갱신 되었습니다.", true));
      } 
    } 
    if (Calendar.getInstance().get(7) == 4) {
      ServerConstants.starForceSalePercent = 10;
    } else {
      ServerConstants.starForceSalePercent = 0;
    } 
    if (!noblessPoint.isEmpty()) {
      Collections.sort(noblessPoint);
      int skilllv = 60;
      for (MapleGuild g : noblessPoint) {
        if (skilllv < 10)
          skilllv = 10; 
        g.setNoblessSkillPoint(skilllv);
        if (skilllv > 10)
          skilllv -= 3; 
        g.setGuildScore(0.0D);
        g.broadcast(CWvsContext.GuildPacket.showGuildInfo(g));
      } 
    } 
    MapleGuildRanking.getInstance().load();
    Connection con1 = null;
    PreparedStatement ps1 = null;
    ResultSet rs = null;
    try {
      con1 = DatabaseConnection.getConnection();
      ps = con1.prepareStatement("SELECT * FROM characters");
      rs = ps1.executeQuery();
      while (rs.next())
        ids.put(rs.getString("name"), Integer.valueOf(rs.getInt("id"))); 
      ps1.close();
      rs.close();
      con1.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (con1 != null)
          con1.close(); 
        if (ps1 != null)
          ps1.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
    while (channels.hasNext()) {
      ChannelServer cs = channels.next();
      Iterator<MapleCharacter> iterator = cs.getPlayerStorage().getAllCharacters().values().iterator();
      while (iterator.hasNext()) {
        MapleCharacter chr = iterator.next();
        chr.checkRestDay(false, false);
        chr.checkRestDay(false, true);
        chr.checkRestDay(true, false);
        chr.checkRestDay(true, true);
        chr.checkRestDayMonday();
        ResetHandler(chr);
        if (Calendar.getInstance().get(7) == 7) {
          ServerConstants.feverTime = true;
          chr.FeverTime(true, false);
          continue;
        } 
        ServerConstants.feverTime = false;
      } 
    } 
    Iterator<MapleCharacter> chrs = AuctionServer.getPlayerStorage().getAllCharacters().values().iterator();
    while (chrs.hasNext()) {
      MapleCharacter chr = chrs.next();
      chr.checkRestDay(false, false);
      chr.checkRestDay(false, true);
      chr.checkRestDay(true, false);
      chr.checkRestDay(true, true);
      chr.checkRestDayMonday();
      ResetHandler(chr);
    } 
  }
  
    public static void ResetHandler(final MapleCharacter chr) {
    }
    
    static {
        MapleSaveHandler.ids = new HashMap<String, Integer>();
        MapleSaveHandler.todayKeyValues = new HashMap<String, String>();
        MapleSaveHandler.weekKeyValues = new String[][] { { "db_lastweek", "0" }, { "dojo", "0" }, { "dojo_time", "0" } };
        MapleSaveHandler.clientDateKeyValues = new String[] { "dailyGiftComplete", "hotelMapleToday", "ht", "mPark", "mpark_t", "jump_1", "jump_2", "jump_3", "day_reborn_1", "day_reborn_2", "day_qitem", "day_summer_a", "day_summer_e", "day_colorlens", "day_MedalC", "day_MedalD" , "minigame"};
        MapleSaveHandler.updateAuctionClients = new ArrayList<Integer>();
        MapleSaveHandler.todayKeyValues.put("muto", "0");
        MapleSaveHandler.todayKeyValues.put("arcane_quest_2", "-1");
        MapleSaveHandler.todayKeyValues.put("arcane_quest_3", "-1");
        MapleSaveHandler.todayKeyValues.put("arcane_quest_4", "-1");
        MapleSaveHandler.todayKeyValues.put("arcane_quest_5", "-1");
        MapleSaveHandler.todayKeyValues.put("arcane_quest_6", "-1");
        MapleSaveHandler.todayKeyValues.put("arcane_quest_7", "-1");
        MapleSaveHandler.todayKeyValues.put("NettPyramid", "0");
        MapleSaveHandler.todayKeyValues.put("linkMobCount", "0");
    }
}
