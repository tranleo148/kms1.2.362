package tools;

import client.MapleCharacter;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import server.ShutdownServer;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CWvsContext;

public class CMDCommand {
  private static CMDCommand instance = new CMDCommand();
  
  public static String target = "";
  
  private static int a;
  
  public static void main() {
    Scanner scan = new Scanner(System.in);
    while (true) {
      System.out.print("Input Command : ");
      try {
        CPUSampler sampler;
        Thread t;
        String outputMessage;
        MaplePacketLittleEndianWriter mplew;
        target = scan.nextLine();
        String[] command = target.split(" ");
        command = target.split(" ");
        switch (command[0]) {
          case "도움말":
            System.out.println("<  CMD커맨드 도움말 >");
            System.out.println("[명령어 목록] :: \r\n");
            System.out.println("<공지> - 공지사항을 보냅니다.");
            System.out.println("<모두종료> - 서버에 있는 유저들을 모두 종료시킵니다.");
            System.out.println("<경매장저장> - 서버에 저장된 경매장 아이템을 모두 저장합니다.");
            System.out.println("<고상저장> - 서버에 열려있는 고용상인을 모두 저장합니다.");
            System.out.println("<임명> - 플레이어에게 GM권한 레벨을 부여합니다.");
            System.out.println("<패킷> - 서버에 센드 패킷 스트링을 보냅니다.");
            System.out.println("<OR> - 옵코드를 리로딩합니다.");
            System.out.println("<동접> - 현재 서버에 접속중인 유저를 표시합니다.");
          case "프로파일링":
            sampler = CPUSampler.getInstance();
            sampler.addIncluded("client");
            sampler.addIncluded("connector");
            sampler.addIncluded("constants");
            sampler.addIncluded("database");
            sampler.addIncluded("handling");
            sampler.addIncluded("log");
            sampler.addIncluded("provider");
            sampler.addIncluded("scripting");
            sampler.addIncluded("server");
            sampler.addIncluded("tools");
            sampler.start();
          case "종료":
            sampler = CPUSampler.getInstance();
            try {
              String filename = "CPU프로파일링.txt";
              if (command.length > 1)
                filename = command[1]; 
              File file = new File(filename);
              if (file.exists()) {
                System.out.println("이미 존재하는 파일입니다. 삭제나 이름 변경을 해주세요.");
                continue;
              } 
              sampler.stop();
              FileWriter fw = new FileWriter(file);
              sampler.save(fw, 1, 10);
              fw.close();
            } catch (IOException e) {
              System.err.println("Error saving profile" + e);
            } 
            sampler.reset();
          case "공지":
            for (ChannelServer ch : ChannelServer.getAllInstances()) {
              for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values())
                chr.dropMessage(1, "[공지사항]\r\n" + StringUtil.joinStringFrom(command, 1)); 
            } 
          case "경매장저장":
            AuctionServer.saveItems();
          case "모두종료":
            t = new Thread(ShutdownServer.getInstance());
            ShutdownServer.getInstance().shutdown();
            System.out.println("서버에 있는 유저들을 종료시켰습니다.");
            t.start();
          case "서버메세지":
            outputMessage = command[1];
            for (ChannelServer cserv : ChannelServer.getAllInstances())
              cserv.setServerMessage(outputMessage); 
          case "임명":
            a = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
              MapleCharacter player = null;
              if (command[1] == null) {
                System.out.println("캐릭터 이름을 입력해주세요.");
              } else {
                player = cserv.getPlayerStorage().getCharacterByName(command[1]);
              } 
              if (player != null) {
                String num = command[2];
                byte number = (num == null) ? 6 : Byte.parseByte(num);
                player.getClient().getSession().writeAndFlush(CWvsContext.getTopMsg("[알림] 해당 플레이어가 GM " + command[2] + "레벨이 되었습니다."));
                System.out.println("[알림] " + command[1] + " 플레이어를 GM레벨 " + command[2] + "(으)로 설정하였습니다.");
                player.setGMLevel(number);
                a = 1;
                continue;
              } 
              if (player == null && a == 0) {
                System.out.println("[알림] " + command[1] + " 플레이어를 찾지 못하였습니다.");
                a = 1;
              } 
            } 
          case "패킷":
            mplew = new MaplePacketLittleEndianWriter();
            mplew.write(HexTool.getByteArrayFromHexString(StringUtil.joinStringFrom(command, 1)));
            for (ChannelServer ch : ChannelServer.getAllInstances()) {
              for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values())
                chr.getClient().getSession().writeAndFlush(mplew.getPacket()); 
            } 
            for (MapleCharacter chr : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
              if (chr.getName().equals("시온"))
                chr.getClient().getSession().writeAndFlush(mplew.getPacket()); 
            } 
            System.out.println(StringUtil.joinStringFrom(command, 1));
          case "or":
            RecvPacketOpcode.reloadValues();
            SendPacketOpcode.reloadValues();
            System.out.print("옵코드 재설정이 완료되었습니다.");
        } 
      } catch (NoSuchElementException e) {}
    } 
  }
  
  public static String converToDecimalFromHex(String hex) {
    String decimal = "";
    hex = hex.trim();
    for (int i = 0; i < hex.length(); i += 2) {
      String tmp = hex.substring(i, i + 2);
      long val = Long.parseLong(tmp, 16);
      decimal = decimal + val;
      decimal = decimal + ",";
    } 
    return decimal;
  }
}
