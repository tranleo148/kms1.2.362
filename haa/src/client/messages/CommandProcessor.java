package client.messages;

import client.MapleCharacter;
import client.MapleClient;
import client.messages.commands.AdminCommand;
import client.messages.commands.CommandExecute;
import client.messages.commands.CommandObject;
import client.messages.commands.DonatorCommand;
import client.messages.commands.GMCommand;
import client.messages.commands.InternCommand;
import client.messages.commands.PlayerCommand;
import client.messages.commands.SLFCGGameCommand;
import client.messages.commands.SuperDonatorCommand;
import client.messages.commands.SuperGMCommand;
import constants.ServerConstants;
import database.DatabaseConnection;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import tools.FileoutputUtil;

public class CommandProcessor {
  private static void sendDisplayMessage(MapleClient c, String msg, ServerConstants.CommandType type) {
    if (c.getPlayer() == null)
      return; 
    switch (type) {
      case NORMAL:
        c.getPlayer().dropMessage(6, msg);
        break;
      case TRADE:
        c.getPlayer().dropMessage(-2, "오류 : " + msg);
        break;
    } 
  }
  
  public static void dropHelp(MapleClient c) {
    c.getPlayer().dropMessage(5, "명령어 리스트 : ");
    for (int i = 0; i <= c.getPlayer().getGMLevel(); i++) {
      if (commandList.containsKey(Integer.valueOf(i)))
        for (String s : commandList.get(Integer.valueOf(i)))
          c.getPlayer().dropMessage(6, s);  
    } 
  }
  
  public static boolean processCommand(MapleClient c, String line, ServerConstants.CommandType type) {
    if (line.charAt(0) == ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix() || (c.getPlayer().getGMLevel() > ServerConstants.PlayerGMRank.NORMAL.getLevel() && line.charAt(0) == ServerConstants.PlayerGMRank.DONATOR.getCommandPrefix())) {
      String[] arrayOfString = line.split(" ");
      arrayOfString[0] = arrayOfString[0].toLowerCase();
      CommandObject commandObject = commands.get(arrayOfString[0]);
      if (commandObject == null || commandObject.getType() != type) {
        sendDisplayMessage(c, "현재 입력한 플레이어 명령어가 존재하지 않습니다.", type);
        return true;
      } 
      if (c.getPlayer().getMapId() == 121212121) {
        sendDisplayMessage(c, "현재 맵에서는 명령어 사용이 불가능합니다.", type);
        return true;
      } 
      try {
        commandObject.execute(c, arrayOfString);
      } catch (Exception exception) {}
      return true;
    } 
    if (c.getPlayer().getGMLevel() <= ServerConstants.PlayerGMRank.NORMAL.getLevel() || (line.charAt(0) != ServerConstants.PlayerGMRank.SUPERGM.getCommandPrefix() && line.charAt(0) != ServerConstants.PlayerGMRank.INTERN.getCommandPrefix() && line.charAt(0) != ServerConstants.PlayerGMRank.GM.getCommandPrefix() && line.charAt(0) != ServerConstants.PlayerGMRank.ADMIN.getCommandPrefix()))
      return false; 
    String[] splitted = line.split(" ");
    splitted[0] = splitted[0].toLowerCase();
    CommandObject co = commands.get(splitted[0]);
    if (co == null) {
      if (splitted[0].equals(line.charAt(0) + "help")) {
        dropHelp(c);
        return true;
      } 
      sendDisplayMessage(c, "현재 입력한 관리자 명령어가 존재하지 않습니다.", type);
      return true;
    } 
    if (c.getPlayer().getGMLevel() >= co.getReqGMLevel()) {
      int ret = 0;
      try {
        ret = co.execute(c, splitted);
      } catch (ArrayIndexOutOfBoundsException x) {
        sendDisplayMessage(c, "The command was not used properly: " + x, type);
      } catch (Exception e) {
        FileoutputUtil.outputFileError("Log_Command_Except.rtf", e);
      } 
      if (ret > 0 && c.getPlayer() != null)
        if (c.getPlayer().isGM()) {
          logCommandToDB(c.getPlayer(), line, "gmlog");
        } else {
          logCommandToDB(c.getPlayer(), line, "internlog");
        }  
    } else {
      sendDisplayMessage(c, "해당 명령어를 사용하는데 권한레벨이 충분하지 않습니다.", type);
    } 
    return true;
  }
  
  private static void logCommandToDB(MapleCharacter player, String command, String table) {
    PreparedStatement ps = null;
    Connection con = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("INSERT INTO " + table + " (cid, command, mapid) VALUES (?, ?, ?)");
      ps.setInt(1, player.getId());
      ps.setString(2, command);
      ps.setInt(3, player.getMap().getId());
      ps.executeUpdate();
    } catch (SQLException ex) {
      FileoutputUtil.outputFileError("Log_Packet_Except.rtf", ex);
      ex.printStackTrace();
    } finally {
      try {
        ps.close();
        con.close();
      } catch (SQLException sQLException) {}
    } 
  }
  
  private static final HashMap<String, CommandObject> commands = new HashMap<>();
  
  private static final HashMap<Integer, ArrayList<String>> commandList = new HashMap<>();
  
  static {
    Class[] array = { PlayerCommand.class, InternCommand.class, GMCommand.class, AdminCommand.class, DonatorCommand.class, SuperDonatorCommand.class, SuperGMCommand.class, SLFCGGameCommand.class }, CommandFiles = array;
    for (Class<?> clasz : array) {
      try {
        ServerConstants.PlayerGMRank rankNeeded = (ServerConstants.PlayerGMRank)clasz.getMethod("getPlayerLevelRequired", new Class[0]).invoke(null, (Object[])null);
        Class<?>[] a = clasz.getDeclaredClasses();
        ArrayList<String> cL = new ArrayList<>();
        for (Class<?> c : a) {
          try {
            if (!Modifier.isAbstract(c.getModifiers()) && !c.isSynthetic()) {
              boolean enabled;
              Object o = c.newInstance();
              try {
                enabled = c.getDeclaredField("enabled").getBoolean(c.getDeclaredField("enabled"));
              } catch (NoSuchFieldException ex3) {
                enabled = true;
              } 
              if (o instanceof CommandExecute && enabled) {
                cL.add(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase());
                commands.put(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase(), new CommandObject((CommandExecute)o, rankNeeded.getLevel()));
                if (rankNeeded.getCommandPrefix() != ServerConstants.PlayerGMRank.GM.getCommandPrefix() && rankNeeded.getCommandPrefix() != ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix())
                  commands.put("!" + c.getSimpleName().toLowerCase(), new CommandObject((CommandExecute)o, ServerConstants.PlayerGMRank.GM.getLevel())); 
              } 
            } 
          } catch (Exception ex) {
            ex.printStackTrace();
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex);
          } 
        } 
        Collections.sort(cL);
        commandList.put(Integer.valueOf(rankNeeded.getLevel()), cL);
      } catch (Exception ex2) {
        ex2.printStackTrace();
        FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex2);
      } 
    } 
  }
}
