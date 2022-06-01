package server.games;

import client.MapleCharacter;
import client.SecondaryStat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class OXQuizGame {
  private List<MapleCharacter> chars = new ArrayList<>();
  
  private List<MapleCharacter> deadchars = new ArrayList<>();
  
  private List<OXQuiz> quizes = new ArrayList<>();
  
  private ScheduledFuture<?> QuizTimer = null;
  
  private MapleCharacter Owner = null;
  
  private int MessageTime = 3;
  
  public static boolean isRunning = false;
  
  public OXQuizGame(MapleCharacter owner, boolean isByAdmin) {
    isRunning = true;
    this.Owner = owner;
  }
  
  public void sendMessage() {
    this.MessageTime--;
  }
  
  public int getMessageTime() {
    return this.MessageTime;
  }
  
  public MapleCharacter getOwner() {
    return this.Owner;
  }
  
  public void RegisterPlayers(List<MapleCharacter> players) {
    this.chars = players;
  }
  
  public void InitGame() {
    this.quizes = OXQuizProvider.getQuizList2(200);
    for (MapleCharacter chr : this.chars)
      chr.getClient().getSession()
        .writeAndFlush(SLFCGPacket.OXQuizExplain("문제를 많이 맞힐수록 더 많은 보상이 기다리고 있지. 너의 상식은 어디까지?")); 
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (MapleCharacter chr : OXQuizGame.this.chars)
              chr.getClient().getSession()
                .writeAndFlush(SLFCGPacket.OXQuizExplain("확실히 선택해야 해. 가운데 애매하게 걸치면 광속으로 탈락할 수 있다구!")); 
          }
        },  7000L);
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (MapleCharacter chr : OXQuizGame.this.chars)
              chr.getClient().getSession().writeAndFlush(
                  SLFCGPacket.OXQuizExplain("모두들 밑으로 내려가서 문제를 잘 들어봐. 맞다고 생각하면 O! 틀린 것 같다면 X!")); 
          }
        },  14000L);
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (MapleCharacter chr : OXQuizGame.this.chars)
              chr.getClient().getSession()
                .writeAndFlush(SLFCGPacket.OXQuizExplain("왼쪽? 오른쪽? 숫자가 모두 카운트되기 전에 확실히 이동하라구!")); 
          }
        },  21000L);
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (MapleCharacter chr : OXQuizGame.this.chars) {
              chr.getClient().getSession()
                .writeAndFlush(SLFCGPacket.OXQuizExplain("그럼 지금부터 시작한다, 스릴 넘치는 OX 퀴즈!"));
              chr.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("multiBingo/start"));
              chr.getClient().getSession().writeAndFlush(CField.MapEff("Gstar/start"));
            } 
          }
        },  28000L);
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            OXQuizGame.this.StartQuiz();
          }
        },  30000L);
  }
  
  public void StartQuiz() {
    this.QuizTimer = Timer.EventTimer.getInstance().register(new Runnable() {
          int count = 0;
          
          int index = -1;
          
          OXQuiz tempq = null;
          
          public void run() {
            if (OXQuizGame.this.chars.size() == OXQuizGame.this.deadchars.size())
              OXQuizGame.this.StopQuiz(); 
            if (this.count == 0) {
              this.index++;
              this.tempq = OXQuizGame.this.quizes.get(this.index);
              for (MapleCharacter chr : OXQuizGame.this.chars) {
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.OXQuizQuestion(this.tempq.getQuestion(), OXQuizGame.this
                      .quizes.indexOf(this.tempq), OXQuizGame.this.quizes.size() - OXQuizGame.this.quizes.indexOf(this.tempq) + 1));
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.OXQuizCountdown(10));
              } 
              this.count = 1;
            } else {
              this.count = 0;
              for (MapleCharacter chr : OXQuizGame.this.chars) {
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.OXQuizResult(this.tempq.isX()));
                if (chr.getPosition().getY() >= 89.0D && chr.getPosition().getY() <= 576.0D) {
                  if (chr.getPosition().getX() <= -752.0D && chr.getPosition().getX() >= -1450.0D) {
                    if (this.tempq.isX()) {
                      chr.getClient().getSession().writeAndFlush(SLFCGPacket.OXQuizTelePort((byte)10));
                      OXQuizGame.this.deadchars.add(chr);
                      continue;
                    } 
                    int amount = (this.index + 1) * 2;
                    chr.AddStarDustCoin(2, 100);
                    continue;
                  } 
                  if (chr.getPosition().getX() <= -500.0D && chr.getPosition().getX() >= -753.0D) {
                    OXQuizGame.this.deadchars.add(chr);
                    chr.getClient().getSession().writeAndFlush(SLFCGPacket.OXQuizTelePort((byte)10));
                    continue;
                  } 
                  if (this.tempq.isX()) {
                    int amount = (this.index + 1) * 2;
                    chr.AddStarDustCoin(2, 100);
                    continue;
                  } 
                  chr.getClient().getSession().writeAndFlush(SLFCGPacket.OXQuizTelePort((byte)10));
                  OXQuizGame.this.deadchars.add(chr);
                  continue;
                } 
                if (!OXQuizGame.this.deadchars.contains(chr))
                  OXQuizGame.this.deadchars.add(chr); 
              } 
            } 
          }
        }, 11000L);
  }
  
  public void StopQuiz() {
    this.QuizTimer.cancel(true);
    for (MapleCharacter chr : this.chars) {
      if (chr != null) {
        chr.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("multiBingo/gameover"));
        chr.setOXGame((OXQuizGame)null);
        chr.getClient().getSession().writeAndFlush(SLFCGPacket.OXQuizExplain("잠시후 이동됩니다. 맵을 이탈하지 마세요."));
      } 
    } 
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (MapleCharacter chr : OXQuizGame.this.chars) {
              if (chr != null && chr.getMapId() == 910048100) {
                chr.warp(910048200);
                chr.cancelEffectFromBuffStat(SecondaryStat.RideVehicle, 80001013);
              } 
            } 
          }
        }, 10000L);
  }
}
