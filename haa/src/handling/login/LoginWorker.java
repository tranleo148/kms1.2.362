package handling.login;

import client.MapleClient;
import handling.channel.ChannelServer;
import handling.login.handler.CharLoginHandler;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import tools.FileoutputUtil;
import tools.packet.CWvsContext;
import tools.packet.LoginPacket;

public class LoginWorker {
  public static void registerClient(MapleClient c, String id, String pwd) throws UnsupportedEncodingException {
    if (LoginServer.isAdminOnly() && !c.isGm() && !c.isLocalhost()) {
      c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "서버 점검중입니다."));
      c.getSession().writeAndFlush(LoginPacket.getLoginFailed(21));
      return;
    } 
    if (System.currentTimeMillis() - lastUpdate > 600000L) {
      lastUpdate = System.currentTimeMillis();
      Map<Integer, Integer> load = ChannelServer.getChannelLoad();
      int usersOn = 0;
      if (load == null || load.size() <= 0) {
        lastUpdate = 0L;
        c.getSession().writeAndFlush(LoginPacket.getLoginFailed(7));
        return;
      } 
      LoginServer.setLoad(load, 0);
      lastUpdate = System.currentTimeMillis();
    } 
    FileoutputUtil.log(FileoutputUtil.계정로그인로그, "[로그인] 계정 번호 : " + c.getAccID() + " |  로그인을 시도");
    if (c.finishLogin() == 0) {
      c.getSession().writeAndFlush(LoginPacket.checkLogin());
      c.getSession().writeAndFlush(LoginPacket.successLogin());
      c.getSession().writeAndFlush(LoginPacket.getAuthSuccessRequest(c, id, pwd));
      CharLoginHandler.ServerListRequest(c, false);
      return;
    } 
    c.getSession().writeAndFlush(LoginPacket.getLoginFailed(7));
  }
  
  private static long lastUpdate = 0L;
}
