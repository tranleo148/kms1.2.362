package server.games;

import client.MapleCharacter;
import client.inventory.MapleInventoryType;
import handling.channel.ChannelServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import server.Randomizer;
import server.Timer;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class DetectiveGame {

    private int Stage = 1;

    private int MessageTime = 3;

    private List<MapleCharacter> Rank = new ArrayList<>();

    private List<MapleCharacter> Fail = new ArrayList<>();

    private Map<MapleCharacter, Integer> Players = new HashMap<>();

    private Map<MapleCharacter, Integer> Answers = new HashMap<>();

    private ScheduledFuture<?> DetectiveTimer = null;

    private MapleCharacter Owner = null;

    public static boolean isRunning = false;

    public DetectiveGame(MapleCharacter owner, boolean isByAdmin) {
        isRunning = true;
        this.Owner = owner;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
                if (isByAdmin) {
                    chr.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003501, 7000, "#face0#운영자가 암호 추리 게임 참여자를 모집 중이야", ""));
                    continue;
                }
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003156, 7000, "#face0##b#e" + this.Owner.getName() + "#k#n가 암호 추리 게임 참여자를 모집 중이야!", ""));
            }
        }
    }

    public void sendMessage() {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
                if (this.Owner.isGM()) {
                    chr.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003501, 5000, "#face0#운영자가 암호 추리 게임 참여자를 모집 중이야!\r\n지금 #r" + this.Owner.getMap().getAllChracater().size() + "명#k이 대기실에 있어!", ""));
                    continue;
                }
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003156, 5000, "#face0##e#b" + this.Owner.getName() + "#k#n가 암호 추리 게임 참여자를 모집 중이야!\r\n지금 #r" + this.Owner.getMap().getAllChracater().size() + "명#k이 대기실에 있어!", ""));
            }
        }
        this.MessageTime--;
    }

    public void RegisterPlayers(List<MapleCharacter> chars) {
        for (MapleCharacter chr : chars) {
            this.Players.put(chr, Integer.valueOf(1));
        }
    }

    public void addAttempt(MapleCharacter chr) {
        this.Players.put(chr, Integer.valueOf(((Integer) this.Players.get(chr)).intValue() + 1));
        int temp = ((Integer) this.Players.get(chr)).intValue();
        if (((Integer) this.Players.get(chr)).intValue() == 15) {
            this.Fail.add(chr);
        }
    }

    public int getAnswer(MapleCharacter chr) {
        return ((Integer) this.Answers.get(chr)).intValue();
    }

    public List<MapleCharacter> getRanking() {
        return this.Rank;
    }

    public MapleCharacter getOwner() {
        return this.Owner;
    }

    public Set<MapleCharacter> getPlayers() {
        return this.Players.keySet();
    }

    public void addRank(MapleCharacter a1) {
        if (!this.Rank.contains(a1)) {
            this.Rank.add(a1);
            a1.getMap().broadcastMessage(SLFCGPacket.HundredDetectiveGameAddRank(a1.getId(), a1.getName()));
            if (this.Rank.size() == 30 || this.Rank.size() + this.Fail.size() == this.Players.size()) {
                this.DetectiveTimer.cancel(true);
                for (MapleCharacter chr : this.Players.keySet()) {
                    chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(4, this.Stage));
                    chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveReEnable(16));
                    this.Players.put(chr, Integer.valueOf(1));
                }
                if (this.Stage == 3) {
                    StopGame();
                } else {
                    this.Stage++;
                    this.Answers.clear();
                    this.Rank.clear();
                    this.Fail.clear();
                    for (MapleCharacter chr : this.Players.keySet()) {
                        int Answer = GetRandomNumber();
                        this.Answers.put(chr, Integer.valueOf(Answer));
                    }
                    Timer.EventTimer.getInstance().schedule(() -> {
                        for (MapleCharacter chr : this.Players.keySet()) {
                            chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameReady(this.Stage));
                            chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(2, this.Stage));
                            chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(3, this.Stage));
                        }
                    }, 5000L);
                    for (MapleCharacter chr : this.Rank) {
                        if (chr != null) {
                            int ranknumber = this.Rank.indexOf(chr) + 1;
                            if (ranknumber == 1) {
                                if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() >= 10) {
                                    chr.AddStarDustCoin(2, 100);
                                }
                                continue;
                            }
                            if (ranknumber >= 2 && ranknumber <= 10) {
                                if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() >= 5) {
                                    chr.AddStarDustCoin(2, 20);
                                }
                                continue;
                            }
                            if (ranknumber >= 11 && ranknumber <= 20) {
                                if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() >= 4) {
                                    chr.AddStarDustCoin(2, 10);
                                }
                                continue;
                            }
                            if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() >= 3) {
                                chr.AddStarDustCoin(2, 5);
                            }
                        }
                    }
                    for (MapleCharacter chr : this.Players.keySet()) {
                        if (chr != null
                                && !this.Rank.contains(chr)
                                && chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() > 0) {
                            chr.AddStarDustCoin(2, 2);
                        }
                    }
                }
            }
        }
    }

    private int GetRandomNumber() {
        List<Integer> temp = new ArrayList<>();
        while (temp.size() != 3) {
            int a = 0;
            while (a == 0) {
                a = Randomizer.nextInt(10);
            }
            if (!temp.contains(Integer.valueOf(a))) {
                temp.add(Integer.valueOf(a));
            }
        }
        int num = ((Integer) temp.get(0)).intValue() * 100 + ((Integer) temp.get(1)).intValue() * 10 + ((Integer) temp.get(2)).intValue();
        while (this.Answers.containsValue(Integer.valueOf(num))) {
            num = GetRandomNumber();
        }
        return num;
    }

    public void StartGame() {
        for (MapleCharacter chr : this.Players.keySet()) {
            chr.getClient().getSession().writeAndFlush(CField.musicChange("BgmEvent2/adventureIsland"));
            chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameExplain());
            int Answer = GetRandomNumber();
            this.Answers.put(chr, Integer.valueOf(Answer));
        }
        Timer.EventTimer.getInstance().schedule(() -> {
            for (MapleCharacter chr : this.Players.keySet()) {
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameReady(this.Stage));
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(2, this.Stage));
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(3, this.Stage));
            }
        }, 40000L);
    }

    public void StopGame() {
        Timer.EventTimer.getInstance().schedule(() -> {
            for (MapleCharacter chr : this.Players.keySet()) {
                if (chr != null) {
                    chr.warp(993022200);
                    chr.setDetectiveGame(null);
                }
            }
            isRunning = false;
        }, 5000L);
    }
}
