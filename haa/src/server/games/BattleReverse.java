// 
// Decompiled by Procyon v0.5.36
// 

package server.games;

import handling.channel.ChannelServer;
import handling.world.World;
import handling.world.MaplePartyCharacter;
import java.util.TimerTask;
import tools.packet.CWvsContext;
import tools.packet.CField;
import server.Timer;
import tools.packet.SLFCGPacket;
import server.Randomizer;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.awt.Point;
import client.MapleCharacter;
import java.util.List;

public class BattleReverse
{
    public static List<MapleCharacter> BattleReverseMatchingQueue;
    public static List<MapleCharacter> BattleReverseMatchingQueue2;
    public static List<BattleReverseMatchingInfo> BattleReverseGameList;
    private List<BattleReversePlayer> Players;
    private BattleReverseStone[][] Board;
    private Point lastPoint;
    private BattleReversePlayer CurrentPlayer;
    private ScheduledFuture<?> BattleReverseTimer;
    
    public BattleReverse(final List<MapleCharacter> chrs) {
        this.CurrentPlayer = null;
        this.BattleReverseTimer = null;
        this.Players = new ArrayList<BattleReversePlayer>();
        this.Board = new BattleReverseStone[8][8];
        this.lastPoint = new Point(0, 0);
        int stone = 0;
        for (final MapleCharacter chr : chrs) {
            this.Players.add(new BattleReversePlayer(chr, new int[] { 10000, stone++ }));
        }
    }
    
    private void InitBoard() {
        for (int a = 0; a < 8; ++a) {
            for (int b = 0; b < 8; ++b) {
                this.Board[a][b] = new BattleReverseStone(new int[] { a, b, -1 });
            }
        }
        this.Board[3][3].setStoneId(0);
        this.Board[4][4].setStoneId(0);
        this.Board[4][3].setStoneId(1);
        this.Board[3][4].setStoneId(1);
        for (int a = 0; a < 5; ++a) {
            for (boolean temp = false; !temp; temp = this.MakeHole(new Point(Randomizer.nextInt(8), Randomizer.nextInt(8)))) {}
        }
    }
    
    public List<BattleReversePlayer> getPlayers() {
        return this.Players;
    }
    
    public List<BattleReverseStone> getStones() {
        final List<BattleReverseStone> list = new ArrayList<BattleReverseStone>();
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                if (this.Board[x][y].getStoneId() != -1) {
                    list.add(this.Board[x][y]);
                }
            }
        }
        return list;
    }
    
    private BattleReversePlayer getOpponent(final int charid) {
        for (final BattleReversePlayer Player : this.Players) {
            if (Player.chr.getId() != charid) {
                return Player;
            }
        }
        return null;
    }
    
    private boolean MakeHole(final Point p) {
        if (this.Board[p.x][p.y].getStoneId() == -1) {
            this.Board[p.x][p.y].setStoneId(3);
            return true;
        }
        return false;
    }
    
    public BattleReversePlayer getPlayer(final int id) {
        for (final BattleReversePlayer player : this.Players) {
            if (player.chr.getId() == id) {
                return player;
            }
        }
        return null;
    }
    
    public boolean isValidMove(final MapleCharacter chr, final Point pt) {
        boolean contains = false;
        final BattleReversePlayer player = this.getPlayer(chr.getId());
        final BattleReversePlayer opponent = this.getOpponent(chr.getId());
        if (player != null && (opponent != null & pt != null) && (player.getStoneId() == 0 || player.getStoneId() == 1) && (opponent.getStoneId() == 0 || opponent.getStoneId() == 1) && pt.x >= 0 && pt.x < 8 && pt.y >= 0 && pt.y < 8) {
            contains = this.getPlaceablePoints(player).contains(pt);
        }
        return contains;
    }
    
    public void sendPlaceStone(final MapleCharacter mapleCharacter, final Point point) {
        if (this.CurrentPlayer.chr != null) {
            if (this.CurrentPlayer.chr.getId() != mapleCharacter.getId()) {
                return;
            }
            if (mapleCharacter.getBattleReverseInstance().isValidMove(mapleCharacter, point)) {
                mapleCharacter.getBattleReverseInstance().placeStone(point, mapleCharacter);
            }
        }
    }
    
    public void skipPlayer(final MapleCharacter mapleCharacter) {
        final List placeablePoints;
        if (!(placeablePoints = mapleCharacter.getBattleReverseInstance().getPlaceablePoints(mapleCharacter.getBattleReverseInstance().getPlayer(mapleCharacter.getId()))).isEmpty()) {
            final BattleReverse battleReverseInstance = mapleCharacter.getBattleReverseInstance();
            final List<Point> list = (List<Point>)placeablePoints;
            battleReverseInstance.sendPlaceStone(mapleCharacter, list.get(Randomizer.nextInt(list.size())));
            return;
        }
        mapleCharacter.getBattleReverseInstance().endGame(mapleCharacter, true);
        mapleCharacter.dropMessage(1, "\uc624\ub958\uac00 \ubc1c\uc0dd\ud558\uc5ec \uac8c\uc784\uc774 \ucde8\uc18c\ub429\ub2c8\ub2e4.");
    }
    
    public void placeStone(final Point lastPoint, final MapleCharacter mapleCharacter) {
        final BattleReversePlayer player = this.getPlayer(mapleCharacter.getId());
        final BattleReversePlayer opponent = this.getOpponent(mapleCharacter.getId());
        int x = lastPoint.x;
        int y = lastPoint.y;
        final int n = x;
        final int n2 = y;
        this.setLastPoint(lastPoint);
        int n3 = 0;
        while (x >= 0 && y >= 0 && (x == n || (this.Board[x][y].getStoneId() != -1 && this.Board[x][y].getStoneId() != 3))) {
            if (this.Board[x][y].getStoneId() == player.getStoneId()) {
                for (int i = 0; i < n - x; ++i) {
                    this.Board[x + i][y + i].setStoneId(player.getStoneId());
                    ++n3;
                }
                break;
            }
            --x;
            --y;
        }
        int n4;
        int n5;
        for (n4 = n, n5 = n2; n4 >= 0 && (n4 == n || (this.Board[n4][n5].getStoneId() != -1 && this.Board[n4][n5].getStoneId() != 3)); --n4) {
            if (this.Board[n4][n5].getStoneId() == player.getStoneId()) {
                for (int j = 0; j < n - n4; ++j) {
                    this.Board[n4 + j][n5].setStoneId(player.getStoneId());
                    ++n3;
                }
                break;
            }
        }
        for (int n6 = n; n6 >= 0 && n5 <= 7 && (n6 == n || (this.Board[n6][n5].getStoneId() != -1 && this.Board[n6][n5].getStoneId() != 3)); --n6, ++n5) {
            if (this.Board[n6][n5].getStoneId() == player.getStoneId()) {
                for (int k = 0; k < n - n6; ++k) {
                    this.Board[n6 + k][n5 - k].setStoneId(player.getStoneId());
                    ++n3;
                }
                break;
            }
        }
        int n7 = n;
        for (int n8 = n2; n8 <= 7 && (n8 == n2 || (this.Board[n7][n8].getStoneId() != -1 && this.Board[n7][n8].getStoneId() != 3)); ++n8) {
            if (this.Board[n7][n8].getStoneId() == player.getStoneId()) {
                for (int l = 0; l < n8 - n2; ++l) {
                    this.Board[n7][n8 - l].setStoneId(player.getStoneId());
                    ++n3;
                }
                break;
            }
        }
        for (int n9 = n2; n7 <= 7 && n9 <= 7 && (n7 == n || (this.Board[n7][n9].getStoneId() != -1 && this.Board[n7][n9].getStoneId() != 3)); ++n7, ++n9) {
            if (this.Board[n7][n9].getStoneId() == player.getStoneId()) {
                for (int n10 = 0; n10 < n7 - n; ++n10) {
                    this.Board[n7 - n10][n9 - n10].setStoneId(player.getStoneId());
                    ++n3;
                }
                break;
            }
        }
        int n11;
        int n12;
        for (n11 = n, n12 = n2; n11 <= 7 && (n11 == n || (this.Board[n11][n12].getStoneId() != -1 && this.Board[n11][n12].getStoneId() != 3)); ++n11) {
            if (this.Board[n11][n12].getStoneId() == player.getStoneId()) {
                for (int n13 = 0; n13 < n11 - n; ++n13) {
                    this.Board[n11 - n13][n12].setStoneId(player.getStoneId());
                    ++n3;
                }
                break;
            }
        }
        for (int n14 = n; n14 <= 7 && n12 >= 0 && (n14 == n || (this.Board[n14][n12].getStoneId() != -1 && this.Board[n14][n12].getStoneId() != 3)); ++n14, --n12) {
            if (this.Board[n14][n12].getStoneId() == player.getStoneId()) {
                for (int n15 = 0; n15 < n14 - n; ++n15) {
                    this.Board[n14 - n15][n12 + n15].setStoneId(player.getStoneId());
                    ++n3;
                }
                break;
            }
        }
        final int n16 = n;
        for (int n17 = n2; n17 >= 0 && (n17 == n2 || (this.Board[n16][n17].getStoneId() != -1 && this.Board[n16][n17].getStoneId() != 3)); --n17) {
            if (this.Board[n16][n17].getStoneId() == player.getStoneId()) {
                for (int n18 = 0; n18 < n2 - n17; ++n18) {
                    this.Board[n16][n17 + n18].setStoneId(player.getStoneId());
                    ++n3;
                }
                break;
            }
        }
        final int n19 = n2;
        final BattleReversePlayer battleReversePlayer = opponent;
        battleReversePlayer.setHP(battleReversePlayer.getHP() - n3 * 10);
        if (n3 >= 4) {
            if (n3 >= 6) {
                final BattleReversePlayer battleReversePlayer2 = opponent;
                battleReversePlayer2.setHP(battleReversePlayer2.getHP() - 100);
            }
            else {
                final BattleReversePlayer battleReversePlayer3 = opponent;
                battleReversePlayer3.setHP(battleReversePlayer3.getHP() - 50);
            }
        }
        this.Board[n16][n19].setStoneId(player.getStoneId());
        if (opponent.getHP() <= 0) {
            for (final BattleReversePlayer battleReversePlayer4 : this.Players) {
                battleReversePlayer4.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onBoardUpdate(opponent.getPlayer().getId() == battleReversePlayer4.getPlayer().getId(), lastPoint, player.getStoneId(), this.getOpponent(player.getPlayer().getId()).getHP(), this.getStones(), (byte)1));
            }
            this.endGame(mapleCharacter, false);
            return;
        }
        this.setTurn(mapleCharacter, player, opponent, lastPoint);
    }
    
    public void setTurn(final MapleCharacter mapleCharacter, final BattleReversePlayer battleReversePlayer, final BattleReversePlayer battleReversePlayer2, final Point point) {
        if (this.getPlaceablePoints(this.getOpponent(mapleCharacter.getId())).size() != 0) {
            for (final BattleReversePlayer battleReversePlayer3 : this.Players) {
                battleReversePlayer3.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onBoardUpdate(battleReversePlayer2.getPlayer().getId() == battleReversePlayer3.getPlayer().getId(), point, battleReversePlayer.getStoneId(), this.getOpponent(battleReversePlayer.getPlayer().getId()).getHP(), this.getStones(), (byte)1));
            }
            if (this.BattleReverseTimer != null) {
                this.BattleReverseTimer.cancel(false);
            }
            this.CurrentPlayer = battleReversePlayer2;
            this.BattleReverseTimer = Timer.EventTimer.getInstance().schedule(() -> this.skipPlayer(battleReversePlayer2.chr), 11200L);
            return;
        }
        if (this.getPlaceablePoints(this.getPlayer(mapleCharacter.getId())).size() == 0) {
            for (final BattleReversePlayer battleReversePlayer4 : this.Players) {
                battleReversePlayer4.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onBoardUpdate(battleReversePlayer2.getPlayer().getId() == battleReversePlayer4.getPlayer().getId(), point, battleReversePlayer.getStoneId(), this.getOpponent(battleReversePlayer.getPlayer().getId()).getHP(), this.getStones(), (byte)1));
            }
            this.endGame(mapleCharacter, false);
            return;
        }
        final Iterator<BattleReversePlayer> iterator3 = this.Players.iterator();
        while (iterator3.hasNext()) {
            final BattleReversePlayer battleReversePlayer5;
            (battleReversePlayer5 = iterator3.next()).chr.getClient().getSession().writeAndFlush((Object)CField.UIPacket.detailShowInfo("\ub458 \uc218 \uc788\ub294 \uacf3\uc774 \uc5c6\uc5b4 \ud134\uc774 \ub118\uc5b4\uac11\ub2c8\ub2e4.", 3, 20, 20));
            battleReversePlayer5.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onBoardUpdate(battleReversePlayer.getPlayer().getId() == battleReversePlayer5.getPlayer().getId(), point, this.getOpponent(mapleCharacter.getId()).getStoneId(), this.getPlayer(mapleCharacter.getId()).getHP(), this.getStones(), (byte)0));
        }
    }
    
    public void endGame(final MapleCharacter mapleCharacter, final boolean b) {
        final BattleReversePlayer player = this.getPlayer(mapleCharacter.getId());
        final BattleReversePlayer opponent = this.getOpponent(mapleCharacter.getId());
        BattleReversePlayer battleReversePlayer = null;
        BattleReversePlayer battleReversePlayer2 = null;
        if (!b) {
            if (player.getHP() <= 0) {
                battleReversePlayer = opponent;
                battleReversePlayer2 = player;
            }
            else if (opponent.getHP() <= 0) {
                battleReversePlayer = player;
                battleReversePlayer2 = opponent;
            }
            else if (player.getHP() > opponent.getHP()) {
                battleReversePlayer = player;
                battleReversePlayer2 = opponent;
            }
            else if (player.getHP() < opponent.getHP()) {
                battleReversePlayer = opponent;
                battleReversePlayer2 = player;
            }
            else {
                player.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onResult(2));
                opponent.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onResult(2));
            }
        }
        else {
            opponent.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onResult(4));
            opponent.chr.getClient().getSession().writeAndFlush((Object)CField.UIPacket.detailShowInfo("\uc0c1\ub300\ubc29\uc774 \ubbf8\ub2c8\uac8c\uc784\uc744 \uc885\ub8cc\ud558\uc5ec \uac8c\uc784\uc774 \uc885\ub8cc\ub429\ub2c8\ub2e4.", 3, 20, 20));
            opponent.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.playSE("Sound/MiniGame.img/oneCard/victory"));
        }
        if (battleReversePlayer != null && battleReversePlayer2 != null) {
            battleReversePlayer.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onResult(4));
            battleReversePlayer.chr.getClient().getSession().writeAndFlush((Object)CWvsContext.onSessionValue("svBattleResult", "win"));
            battleReversePlayer.chr.getClient().getSession().writeAndFlush((Object)CField.playSound("Sound/MiniGame.img/oneCard/victory"));
            battleReversePlayer2.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onResult(4));
            battleReversePlayer2.chr.getClient().getSession().writeAndFlush((Object)CWvsContext.onSessionValue("svBattleResult", "lose"));
            battleReversePlayer2.chr.getClient().getSession().writeAndFlush((Object)CField.playSound("Sound/MiniGame.img/oneCard/gameover"));
            battleReversePlayer.chr.getClient().getSession().writeAndFlush((Object)CField.UIPacket.detailShowInfo(battleReversePlayer.chr.getName() + "님의 승리!", 3, 20, 20));
            battleReversePlayer2.chr.getClient().getSession().writeAndFlush((Object)CField.UIPacket.detailShowInfo(battleReversePlayer.chr.getName() + "님의 승리!", 3, 20, 20));
            battleReversePlayer.chr.setKeyValue(100664, "reverse", "1");
            battleReversePlayer2.chr.setKeyValue(100664, "reverse", "2");
        }
        if (this.BattleReverseTimer != null) {
            this.BattleReverseTimer.cancel(false);
            this.BattleReverseTimer = null;
        }
        final Iterator<BattleReversePlayer> iterator;
        BattleReversePlayer p;
        Timer.EventTimer.getInstance().schedule(() -> {
          for (BattleReversePlayer pp : this.Players) {
            if (pp != null) {
              pp.chr.warp(993186800);
              pp.chr.setBattleReverseInstance(null);
            } 
          } 
        },4000L);
    }
    
    public List getPlaceablePoints(final BattleReversePlayer battleReversePlayer) {
        final ArrayList<Point> list = new ArrayList<Point>();
        final BattleReversePlayer opponent = this.getOpponent(battleReversePlayer.chr.getId());
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (this.Board[i][j].getStoneId() == opponent.getStoneId()) {
                    final int n = i;
                    final int n2 = j;
                    if (i - 1 >= 0 && j - 1 >= 0 && i + 1 <= 7 && j + 1 <= 7 && this.Board[i + 1][j + 1].getStoneId() == battleReversePlayer.getStoneId()) {
                        while (i - 1 >= 0 && j - 1 >= 0) {
                            if (this.Board[i - 1][j - 1].getStoneId() == opponent.getStoneId()) {
                                --i;
                                --j;
                            }
                            else {
                                if (this.Board[i - 1][j - 1].getStoneId() == -1) {
                                    list.add(new Point(i - 1, j - 1));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    int n3 = n;
                    int n4 = n2;
                    if (n3 + 1 <= 7 && n4 - 1 >= 0 && n3 - 1 >= 0 && n4 + 1 <= 7 && this.Board[n3 - 1][n4 + 1].getStoneId() == battleReversePlayer.getStoneId()) {
                        while (n3 + 1 <= 7 && n4 - 1 >= 0) {
                            if (this.Board[n3 + 1][n4 - 1].getStoneId() == opponent.getStoneId()) {
                                ++n3;
                                --n4;
                            }
                            else {
                                if (this.Board[n3 + 1][n4 - 1].getStoneId() == -1) {
                                    list.add(new Point(n3 + 1, n4 - 1));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    int n5 = n;
                    int n6 = n2;
                    if (n5 + 1 <= 7 && n6 + 1 <= 7 && n5 - 1 >= 0 && n6 - 1 >= 0 && this.Board[n5 - 1][n6 - 1].getStoneId() == battleReversePlayer.getStoneId()) {
                        while (n5 + 1 <= 7 && n6 + 1 <= 7) {
                            if (this.Board[n5 + 1][n6 + 1].getStoneId() == opponent.getStoneId()) {
                                ++n5;
                                ++n6;
                            }
                            else {
                                if (this.Board[n5 + 1][n6 + 1].getStoneId() == -1) {
                                    list.add(new Point(n5 + 1, n6 + 1));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    int n7 = n;
                    int n8 = n2;
                    if (n7 - 1 >= 0 && n8 + 1 <= 7 && n7 + 1 <= 7 && n8 - 1 >= 0 && this.Board[n7 + 1][n8 - 1].getStoneId() == battleReversePlayer.getStoneId()) {
                        while (n7 - 1 >= 0 && n8 + 1 <= 7) {
                            if (this.Board[n7 - 1][n8 + 1].getStoneId() == opponent.getStoneId()) {
                                --n7;
                                ++n8;
                            }
                            else {
                                if (this.Board[n7 - 1][n8 + 1].getStoneId() == -1) {
                                    list.add(new Point(n7 - 1, n8 + 1));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    final int n9 = n;
                    int n10;
                    if ((n10 = n2) - 1 >= 0 && n10 + 1 <= 7 && this.Board[n9][n10 + 1].getStoneId() == battleReversePlayer.getStoneId()) {
                        while (n10 - 1 >= 0) {
                            if (this.Board[n9][n10 - 1].getStoneId() == opponent.getStoneId()) {
                                --n10;
                            }
                            else {
                                if (this.Board[n9][n10 - 1].getStoneId() == -1) {
                                    list.add(new Point(n9, n10 - 1));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    final int n11 = n;
                    int n12;
                    if ((n12 = n2) + 1 <= 7 && n12 - 1 >= 0 && this.Board[n11][n12 - 1].getStoneId() == battleReversePlayer.getStoneId()) {
                        while (n12 + 1 <= 7) {
                            if (this.Board[n11][n12 + 1].getStoneId() == opponent.getStoneId()) {
                                ++n12;
                            }
                            else {
                                if (this.Board[n11][n12 + 1].getStoneId() == -1) {
                                    list.add(new Point(n11, n12 + 1));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    int n13 = n;
                    final int n14 = n2;
                    if (n13 + 1 <= 7 && n13 - 1 >= 0 && this.Board[n13 - 1][n14].getStoneId() == battleReversePlayer.getStoneId()) {
                        while (n13 + 1 <= 7) {
                            if (this.Board[n13 + 1][n14].getStoneId() == opponent.getStoneId()) {
                                ++n13;
                            }
                            else {
                                if (this.Board[n13 + 1][n14].getStoneId() == -1) {
                                    list.add(new Point(n13 + 1, n14));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    int n15 = n;
                    final int n16 = n2;
                    if (n15 - 1 >= 0 && n15 + 1 <= 7 && this.Board[n15 + 1][n16].getStoneId() == battleReversePlayer.getStoneId()) {
                        while (n15 - 1 >= 0) {
                            if (this.Board[n15 - 1][n16].getStoneId() == opponent.getStoneId()) {
                                --n15;
                            }
                            else {
                                if (this.Board[n15 - 1][n16].getStoneId() == -1) {
                                    list.add(new Point(n15 - 1, n16));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    i = n;
                    j = n2;
                }
            }
        }
        return list;
    }
    
    public static void addQueue(final MapleCharacter mapleCharacter, final int n) {
        if (BattleReverse.BattleReverseMatchingQueue.size() >= 2) {
            mapleCharacter.getClient().send(CField.NPCPacket.getNPCTalk(9062354, (byte)0, "\uc7a0\uc2dc \ud6c4\uc5d0 \ub2e4\uc2dc \uc2dc\ub3c4\ud574 \uc8fc\uc138\uc694.", "00 00", (byte)0, mapleCharacter.getId()));
        }
        else if (!BattleReverse.BattleReverseMatchingQueue.contains(mapleCharacter)) {
            BattleReverse.BattleReverseMatchingQueue.add(mapleCharacter);
            mapleCharacter.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter, 993186700, 11, 2, 1, 23));
            if (BattleReverse.BattleReverseMatchingQueue.size() == n) {
                for (final MapleCharacter mapleCharacter2 : BattleReverse.BattleReverseMatchingQueue) {
                    mapleCharacter2.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter2, 0, 19, 0, 1, 23));
                    (mapleCharacter2.ConstentTimer = new java.util.Timer()).schedule(new TimerTask() {
                        @Override
                        public void run() {
                            final List<MapleCharacter> remover = new ArrayList<MapleCharacter>();
                            BattleReverse.BattleReverseMatchingQueue.remove(mapleCharacter2);
                            for (final MapleCharacter chr2 : BattleReverse.BattleReverseMatchingQueue) {
                                if (mapleCharacter2.getId() != chr2.getId()) {
                                    remover.add(chr2);
                                    chr2.getClient().send(SLFCGPacket.ContentsWaiting(chr2, 0, 11, 5, 1, 23));
                                }
                            }
                            for (final MapleCharacter chr2 : remover) {
                                BattleReverse.BattleReverseMatchingQueue.remove(chr2);
                                BattleReverse.BattleReverseMatchingQueue2.remove(chr2);
                            }
                            mapleCharacter2.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter2, 0, 11, 5, 1, 23));
                            if (mapleCharacter2.ConstentTimer != null) {
                                mapleCharacter2.ConstentTimer.cancel();
                                mapleCharacter2.ConstentTimer = null;
                            }
                        }
                    }, 10000L);
                }
            }
        }
    }
    
    public static void addQueue(MapleCharacter mapleCharacter, boolean b) {
        block2: {
            block1: {
                if (b) break block1;
                if (BattleReverseMatchingQueue.contains(mapleCharacter)) break block2;
                BattleReverseMatchingQueue.add(mapleCharacter);
                if (BattleReverseMatchingQueue.size() < 2) break block2;
                BattleReverseMatchingInfo battleReverseMatchingInfo = new BattleReverseMatchingInfo(BattleReverseMatchingQueue.get(0), BattleReverseMatchingQueue.get(1));
                BattleReverseGameList.add(battleReverseMatchingInfo);
                BattleReverseMatchingQueue.remove(battleReverseMatchingInfo.p1);
                BattleReverseMatchingQueue.remove(battleReverseMatchingInfo.p2);
                battleReverseMatchingInfo.p1.warp(993186700);
                battleReverseMatchingInfo.p2.warp(993186700);
                Timer.MapTimer.getInstance().schedule(() -> {
                    ArrayList<MapleCharacter> chrs = new ArrayList<MapleCharacter>();
                    chrs.add(battleReverseMatchingInfo.p1);
                    chrs.add(battleReverseMatchingInfo.p2);
                    BattleReverse br = new BattleReverse(chrs);
                    for (MapleCharacter p : chrs) {
                        p.setBattleReverseInstance(br);
                    }
                    br.InitBoard();
                    for (MapleCharacter p : chrs) {
                        p.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.createUI(chrs, p, br.getPlayer(p.getId()).getStoneId()));
                    }
                    Timer.EventTimer.getInstance().schedule(() -> br.StartGame(), 3000L);
                }, 5000L);
                break block2;
            }
            for (MaplePartyCharacter maplePartyCharacter : mapleCharacter.getParty().getMembers()) {
                int channel = World.Find.findChannel(maplePartyCharacter.getId());
                if (channel <= 0) continue;
                BattleReverse.addQueue(ChannelServer.getInstance(channel).getPlayerStorage().getCharacterById(maplePartyCharacter.getId()), false);
            }
        }
    }
    
    public static void StartGame2() {
        if (BattleReverseMatchingQueue.size() >= 2) {
            BattleReverseMatchingInfo battleReverseMatchingInfo = new BattleReverseMatchingInfo(BattleReverseMatchingQueue.get(0), BattleReverseMatchingQueue.get(1));
            BattleReverseGameList.add(battleReverseMatchingInfo);
            BattleReverseMatchingQueue.remove(battleReverseMatchingInfo.p1);
            BattleReverseMatchingQueue.remove(battleReverseMatchingInfo.p2);
            battleReverseMatchingInfo.p1.warp(993186700);
            battleReverseMatchingInfo.p2.warp(993186700);
            battleReverseMatchingInfo.p1.getClient().send(SLFCGPacket.ContentsWaiting(battleReverseMatchingInfo.p1, 0, 11, 5, 1, 23));
            battleReverseMatchingInfo.p2.getClient().send(SLFCGPacket.ContentsWaiting(battleReverseMatchingInfo.p2, 0, 11, 5, 1, 23));
            Timer.MapTimer.getInstance().schedule(() -> {
                ArrayList<MapleCharacter> chrs = new ArrayList<MapleCharacter>();
                chrs.add(battleReverseMatchingInfo.p1);
                chrs.add(battleReverseMatchingInfo.p2);
                BattleReverse br = new BattleReverse(chrs);
                for (MapleCharacter p : chrs) {
                    p.setBattleReverseInstance(br);
                }
                br.InitBoard();
                for (MapleCharacter p : chrs) {
                    p.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.createUI(chrs, p, br.getPlayer(p.getId()).getStoneId()));
                }
                Timer.EventTimer.getInstance().schedule(() -> br.StartGame(), 3000L);
            }, 5000L);
        }
    }
    
    private void StartGame() {
        final BattleReversePlayer First = this.Players.get(0);
        for (final BattleReversePlayer bp : this.Players) {
            bp.chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.MultiOthelloGamePacket.onInit(this.getStones(), First.StoneId));
        }
        this.CurrentPlayer = First;
        if (this.BattleReverseTimer != null) {
            this.BattleReverseTimer.cancel(false);
        }
    }
    
    public Point getLastPoint() {
        return this.lastPoint;
    }
    
    public void setLastPoint(final Point lastPoint) {
        this.lastPoint = lastPoint;
    }
    
    public ScheduledFuture getOthelloTimer() {
        return this.BattleReverseTimer;
    }
    
    public void setOthelloTimer(final ScheduledFuture d) {
        this.BattleReverseTimer = (ScheduledFuture<?>)d;
    }
    
    static {
        BattleReverse.BattleReverseMatchingQueue = new ArrayList<MapleCharacter>();
        BattleReverse.BattleReverseMatchingQueue2 = new ArrayList<MapleCharacter>();
        BattleReverse.BattleReverseGameList = new ArrayList<BattleReverseMatchingInfo>();
    }
    
    public static class BattleReverseMatchingInfo
    {
        public MapleCharacter p1;
        public MapleCharacter p2;
        
        public BattleReverseMatchingInfo(final MapleCharacter... chrs) {
            this.p1 = chrs[0];
            this.p2 = chrs[1];
        }
    }
    
    public class BattleReversePlayer
    {
        private int HP;
        private int StoneId;
        private MapleCharacter chr;
        
        public BattleReversePlayer(final MapleCharacter player, final int... args) {
            this.chr = player;
            this.HP = args[0];
            this.StoneId = args[1];
        }
        
        public MapleCharacter getPlayer() {
            return this.chr;
        }
        
        public void setHP(final int a1) {
            this.HP = a1;
        }
        
        public int getHP() {
            return this.HP;
        }
        
        public int getStoneId() {
            return this.StoneId;
        }
    }
    
    public class BattleReverseStone
    {
        private int StoneId;
        private Point Position;
        
        public BattleReverseStone(final int... args) {
            this.Position = new Point(args[0], args[1]);
            this.StoneId = args[2];
        }
        
        public Point getStonePosition() {
            return this.Position;
        }
        
        public int getStoneId() {
            return this.StoneId;
        }
        
        public void setStoneId(final int a) {
            this.StoneId = a;
        }
    }
}
