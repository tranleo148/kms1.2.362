package server.games;

import client.MapleCharacter;
import client.MapleCoolDownValueHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import server.Randomizer;
import server.Timer;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class MultiYutGame {

    private int objectId = 1;

    public static List<MapleCharacter> multiYutMagchingQueue = new ArrayList<>();

    public static List<MapleCharacter> multiYutMagchingQueue2 = new ArrayList<>();

    private ScheduledFuture<?> multiYutTimer = null;

    private List<MultiYutPlayer> players = new ArrayList<>();

    private MultiYutPlayer whoTurn = null;

    public class MultiYutPlayer {

        private byte position;

        private MapleCharacter chr;

        private List<MultiYutGame.PlayersHorses> Horses;

        private List<MultiYutGame.PlayersSkill> skilllist;

        private Map<Integer, Integer> Yut;

        private int TurnStack;

        public MultiYutPlayer() {
        }

        public MultiYutPlayer(MapleCharacter player, byte position) {
            this.chr = player;
            this.position = position;
            this.Yut = new HashMap<>();
            int i;
            for (i = 1; i < 7; i++) {
                this.Yut.put(Integer.valueOf(i), Integer.valueOf(0));
            }
            this.Horses = new ArrayList<>();
            this.skilllist = new ArrayList<>();
            this.TurnStack = 0;
            if (position == 0) {
                MultiYutGame.this.whoTurn = this;
            }
            for (i = 0; i < 4; i++) {
                MultiYutGame.PlayersHorses horse = new MultiYutGame.PlayersHorses((byte) i);
                this.Horses.add(horse);
            }
            for (i = 0; i < 2; i++) {
                MultiYutGame.PlayersSkill skill = new MultiYutGame.PlayersSkill(Randomizer.rand(0, 7));
                this.skilllist.add(skill);
            }
        }

        public void ThrowYut() {
            int type;
            boolean onemore = false, noadd = false;
            if (Randomizer.isSuccess(5)) {
                type = Randomizer.rand(6, 7);
                if (type == 7) {
                    noadd = true;
                    onemore = true;
                }
            } else if (Randomizer.isSuccess(10)) {
                type = Randomizer.rand(4, 5);
                onemore = true;
            } else {
                type = Randomizer.rand(1, 3);
            }
            if (!noadd) {
                this.TurnStack++;
                if (this.Yut.containsKey(Integer.valueOf(type))) {
                    for (Map.Entry<Integer, Integer> yut : this.Yut.entrySet()) {
                        if (((Integer) yut.getKey()).intValue() == type) {
                            yut.setValue(Integer.valueOf(((Integer) yut.getValue()).intValue() + 1));
                            break;
                        }
                    }
                } else {
                    this.Yut.put(Integer.valueOf(type), Integer.valueOf(1));
                }
            } else {
                MultiYutGame.this.whoTurn = MultiYutGame.this.getOpponent(this.chr);
            }
            for (MultiYutPlayer player : MultiYutGame.this.players) {
                player.getChr().getClient().send(SLFCGPacket.MultiYutGamePacket.ThrowYut(MultiYutGame.this.players, type - 1, MultiYutGame.this.whoTurn.getPosition(), onemore));
            }
        }

        public void MoveHorse(int horsePos, int movecount, int yuttype) {
            MultiYutGame.PlayersHorses horse = getHorses().get(horsePos);
            boolean nextturn = true, catched = false, onemore = false;
            if (horse != null && horse.getInvposition() == horsePos) {
                int beforecount = horse.getNowposition();
                horse.getLayout().clear();
                if (yuttype == 6) {
                    horse.getLayout().add(Integer.valueOf(beforecount));
                    if (beforecount == 26 || beforecount == 21 || beforecount == 28 || beforecount == 1 || (beforecount == 20 && movecount == 29) || (beforecount == 23 && movecount == 27) || (beforecount == 15 && movecount == 25)) {
                        int moved = (beforecount == 1) ? 20 : ((beforecount == 21) ? 5 : ((beforecount == 26) ? 10 : ((beforecount == 28) ? 23 : ((beforecount == 20) ? 29 : ((beforecount == 23) ? 27 : ((beforecount == 15) ? 25 : 0))))));
                        horse.getLayout().add(Integer.valueOf(moved));
                    } else {
                        horse.getLayout().add(Integer.valueOf(beforecount - 1));
                    }
                } else if (beforecount == 5 || beforecount == 10 || beforecount == 23) {
                    int startnum = (beforecount == 5) ? 21 : ((beforecount == 10) ? 26 : ((beforecount == 23) ? 28 : 0));
                    if (beforecount == 10) {
                        if (yuttype >= 3) {
                            horse.getLayout().add(Integer.valueOf(beforecount));
                            horse.getLayout().add(Integer.valueOf(26));
                            horse.getLayout().add(Integer.valueOf(27));
                            if (yuttype == 3) {
                                horse.getLayout().add(Integer.valueOf(23));
                            } else if (yuttype == 4) {
                                horse.getLayout().add(Integer.valueOf(23));
                                horse.getLayout().add(Integer.valueOf(28));
                            } else if (yuttype == 5) {
                                horse.getLayout().add(Integer.valueOf(23));
                                horse.getLayout().add(Integer.valueOf(28));
                                horse.getLayout().add(Integer.valueOf(29));
                            }
                        } else {
                            horse.getLayout().add(Integer.valueOf(beforecount));
                            for (int i = startnum; i < startnum + yuttype; i++) {
                                horse.getLayout().add(Integer.valueOf(i));
                            }
                        }
                    } else {
                        horse.getLayout().add(Integer.valueOf(beforecount));
                        for (int i = startnum; i < startnum + yuttype; i++) {
                            horse.getLayout().add(Integer.valueOf(i));
                        }
                    }
                } else if ((beforecount == 21 || beforecount == 22 || beforecount == 24 || beforecount == 25) && beforecount + yuttype > 25) {
                    int num = beforecount + yuttype - 25;
                    horse.getLayout().add(Integer.valueOf(beforecount));
                    int i;
                    for (i = beforecount; i < beforecount + yuttype; i++) {
                        if (beforecount < 25) {
                            horse.getLayout().add(Integer.valueOf(i));
                        }
                    }
                    for (i = 15; i < 15 + num; i++) {
                        horse.getLayout().add(Integer.valueOf(i));
                    }
                } else if (beforecount == 26 || beforecount == 27) {
                    if (beforecount == 26 && yuttype >= 2) {
                        horse.getLayout().add(Integer.valueOf(26));
                        horse.getLayout().add(Integer.valueOf(27));
                        horse.getLayout().add(Integer.valueOf(23));
                        switch (yuttype) {
                            case 3:
                                horse.getLayout().add(Integer.valueOf(28));
                                break;
                            case 4:
                                horse.getLayout().add(Integer.valueOf(28));
                                horse.getLayout().add(Integer.valueOf(29));
                                break;
                            case 5:
                                horse.getLayout().add(Integer.valueOf(28));
                                horse.getLayout().add(Integer.valueOf(29));
                                horse.getLayout().add(Integer.valueOf(20));
                                break;
                        }
                    } else if (beforecount == 27 && yuttype >= 1) {
                        horse.getLayout().add(Integer.valueOf(27));
                        switch (yuttype) {
                            case 2:
                                horse.getLayout().add(Integer.valueOf(23));
                                horse.getLayout().add(Integer.valueOf(28));
                                break;
                            case 3:
                                horse.getLayout().add(Integer.valueOf(23));
                                horse.getLayout().add(Integer.valueOf(28));
                                horse.getLayout().add(Integer.valueOf(29));
                                break;
                            case 4:
                            case 5:
                                horse.getLayout().add(Integer.valueOf(23));
                                horse.getLayout().add(Integer.valueOf(28));
                                horse.getLayout().add(Integer.valueOf(29));
                                horse.getLayout().add(Integer.valueOf(20));
                                if (yuttype == 5) {
                                    horse.getLayout().add(Integer.valueOf(31));
                                }
                                break;
                        }
                    }
                } else {
                    for (int i = beforecount; i <= beforecount + yuttype; i++) {
                        if (i >= 21 && (beforecount <= 20 || beforecount >= 30)) {
                            horse.getLayout().add(Integer.valueOf(31));
                        } else if (i == 30) {
                            horse.getLayout().add(Integer.valueOf(20));
                        } else {
                            horse.getLayout().add(Integer.valueOf(i));
                        }
                    }
                }
                for (Integer i : horse.getLayout()) {
                    if (i.intValue() > 30) {
                        horse.setFinish(true);
                        break;
                    }
                }
                System.out.println(beforecount + " : " + horse.getLayout());
                for (Map.Entry<Integer, Integer> yut : this.Yut.entrySet()) {
                    if (((Integer) yut.getKey()).intValue() == yuttype && ((Integer) yut.getValue()).intValue() > 0) {
                        yut.setValue(Integer.valueOf(((Integer) yut.getValue()).intValue() - 1));
                    }
                }
                horse.setNowposition((byte) movecount);
                if (horse.getOverlap().size() > 0) {
                    for (MultiYutGame.PlayersHorses hor : getHorses()) {
                        for (Map.Entry<Integer, Integer> overlap : horse.getOverlap().entrySet()) {
                            if (hor.getInvposition() == ((Integer) overlap.getKey()).intValue()) {
                                hor.setNowposition(horse.getNowposition());
                            }
                        }
                    }
                }
                for (MultiYutGame.PlayersHorses ophor : MultiYutGame.this.getOpponent(getChr()).getHorses()) {
                    if (ophor.getNowposition() == horse.getNowposition() && !ophor.isFinish()) {
                        ophor.getOverlap().clear();
                        ophor.setNowposition((byte) 0);
                        ophor.setOverlapOwner(false);
                        catched = true;
                        nextturn = false;
                    }
                }
                if (nextturn) {
                    MultiYutGame.this.whoTurn = MultiYutGame.this.getOpponent(getChr());
                    onemore = true;
                }
                for (MultiYutGame.PlayersHorses hor : getHorses()) {
                    if (hor.getInvposition() != horse.getInvposition()
                            && hor.getNowposition() == horse.getNowposition()) {
                        if (hor.getOverlap().size() > 0) {
                            if (hor.isOverlapOwner()
                                    && !hor.getOverlap().containsKey(Byte.valueOf(horse.getInvposition()))) {
                                hor.getOverlap().put(Integer.valueOf(horse.getInvposition()), Integer.valueOf(0));
                                horse.getOverlap().put(Integer.valueOf(hor.getInvposition()), Integer.valueOf(0));
                                break;
                            }
                            continue;
                        }
                        if (hor.getOverlap().size() <= 0) {
                            hor.setOverlapOwner(true);
                            hor.getOverlap().put(Integer.valueOf(horse.getInvposition()), Integer.valueOf(0));
                            horse.getOverlap().put(Integer.valueOf(hor.getInvposition()), Integer.valueOf(0));
                            break;
                        }
                    }
                }
                for (MultiYutPlayer player : MultiYutGame.this.players) {
                    player.getChr().getClient().send(SLFCGPacket.MultiYutGamePacket.MovedHorse(MultiYutGame.this.players, this, horse.getLayout(), beforecount, MultiYutGame.this.whoTurn.getPosition(), horsePos, catched, onemore));
                }
            }
        }

        public boolean Myturn(MultiYutPlayer player) {
            boolean my = false;
            if (MultiYutGame.this.whoTurn.getChr().getId() == player.getChr().getId()) {
                my = true;
            }
            return my;
        }

        public MapleCharacter getPlayer() {
            return this.chr;
        }

        public byte getPosition() {
            return this.position;
        }

        public void setPosition(byte position) {
            this.position = position;
        }

        public MapleCharacter getChr() {
            return this.chr;
        }

        public void setChr(MapleCharacter chr) {
            this.chr = chr;
        }

        public List<MultiYutGame.PlayersHorses> getHorses() {
            return this.Horses;
        }

        public void setHorses(List<MultiYutGame.PlayersHorses> Horses) {
            this.Horses = Horses;
        }

        public List<MultiYutGame.PlayersSkill> getSkilllist() {
            return this.skilllist;
        }

        public void setSkilllist(List<MultiYutGame.PlayersSkill> skilllist) {
            this.skilllist = skilllist;
        }

        public Map<Integer, Integer> getYut() {
            return this.Yut;
        }

        public void setYut(Map<Integer, Integer> Yut) {
            this.Yut = Yut;
        }

        public int getTurnStack() {
            return this.TurnStack;
        }

        public void setTurnStack(int TurnStack) {
            this.TurnStack = TurnStack;
        }
    }

    public class PlayersHorses extends MultiYutPlayer {

        private byte nowposition;

        private byte invposition;

        private boolean OverlapOwner;

        private boolean finish;

        private Map<Integer, Integer> overlap;

        private List<Integer> layout;

        public PlayersHorses(byte i) {
            this.nowposition = 0;
            this.invposition = i;
            this.overlap = new HashMap<>();
            this.layout = new ArrayList<>();
            this.OverlapOwner = false;
            this.finish = false;
        }

        public byte getNowposition() {
            return this.nowposition;
        }

        public void setNowposition(byte nowposition) {
            this.nowposition = nowposition;
        }

        public byte getInvposition() {
            return this.invposition;
        }

        public void setInvposition(byte invposition) {
            this.invposition = invposition;
        }

        public boolean isOverlapOwner() {
            return this.OverlapOwner;
        }

        public void setOverlapOwner(boolean OverlapOwner) {
            this.OverlapOwner = OverlapOwner;
        }

        public Map<Integer, Integer> getOverlap() {
            return this.overlap;
        }

        public List<Integer> getLayout() {
            return this.layout;
        }

        public void setLayout(List<Integer> layout) {
            this.layout = layout;
        }

        public boolean isFinish() {
            return this.finish;
        }

        public void setFinish(boolean finish) {
            this.finish = finish;
        }
    }

    public class PlayersSkill extends MultiYutPlayer {

        private int skill;

        boolean skillused;

        public PlayersSkill(int skill) {
            this.skill = skill;
            this.skillused = false;
        }

        public int getSkill() {
            return this.skill;
        }

        public void setSkill(int skill) {
            this.skill = skill;
        }

        public boolean isSkillused() {
            return this.skillused;
        }

        public void setSkillused(boolean skillused) {
            this.skillused = skillused;
        }
    }

    public static class multiYutMagchingInfo {

        public List<MapleCharacter> players = new ArrayList<>();

        public multiYutMagchingInfo(List<MapleCharacter> chrs) {
            for (MapleCharacter chr : chrs) {
                this.players.add(chr);
            }
        }
    }

    public MultiYutGame(List<MapleCharacter> chrs) {
        MapleCharacter first = Randomizer.nextBoolean() ? chrs.get(1) : chrs.get(0);
        for (int i = 0; i < chrs.size(); i++) {
            if (first.getId() == ((MapleCharacter) chrs.get(i)).getId()) {
                getPlayers().add(new MultiYutPlayer(chrs.get(i), (byte) 0));
            } else {
                getPlayers().add(new MultiYutPlayer(chrs.get(i), (byte) 1));
            }
        }
    }

    public MultiYutPlayer getPlayer(MapleCharacter chr) {
        for (MultiYutPlayer ocp : getPlayers()) {
            if (ocp.chr.getId() == chr.getId()) {
                return ocp;
            }
        }
        return null;
    }

    public MultiYutPlayer getOpponent(MapleCharacter chr) {
        for (MultiYutPlayer ocp : getPlayers()) {
            if (ocp.chr.getId() != chr.getId()) {
                return ocp;
            }
        }
        return null;
    }

    private List<MultiYutPlayer> getPlayers() {
        return this.players;
    }

    public static void StartGame(int n) {
        if (multiYutMagchingQueue.size() == n) {
            multiYutMagchingInfo yutmatchingInfo = new multiYutMagchingInfo(multiYutMagchingQueue);
            ArrayList<MapleCharacter> chrs = new ArrayList<>();
            for (MapleCharacter mapleCharacter2 : yutmatchingInfo.players) {
                multiYutMagchingQueue.remove(mapleCharacter2);
                chrs.add(mapleCharacter2);
                mapleCharacter2.warp(993189900);
            }
            Timer.EtcTimer.getInstance().schedule(() -> {
                MultiYutGame myg = new MultiYutGame(chrs);
                for (MapleCharacter p : chrs) {
                    p.setMultiYutInstance(myg);
                }
                for (MultiYutPlayer myp : myg.getPlayers()) {
                    MultiYutPlayer me = myg.getPlayer(myp.getPlayer());
                    MultiYutPlayer opponent = myg.getOpponent(myp.getPlayer());
                    myp.chr.getClient().getSession().writeAndFlush(SLFCGPacket.MultiYutGamePacket.createUI(me, opponent));
                }
            }, 5000L);
        }
    }

    private void StartGame(MapleCharacter chr) {
        MultiYutPlayer first = Randomizer.nextBoolean() ? getPlayers().get(1) : getPlayers().get(0);
    }

    public static void addQueue(MapleCharacter mapleCharacter, int n) {
        if (mapleCharacter.skillisCooling(100668) && !mapleCharacter.isGM()) {
            String 초 = "";
            for (MapleCoolDownValueHolder m : mapleCharacter.getCooldowns()) {
                if (m.skillId == 100668) {
                    초 = "" + ((m.length + m.startTime - System.currentTimeMillis()) / 1000L / 60L);
                    break;
                }
            }
            mapleCharacter.getClient().send(CField.NPCPacket.getNPCTalk(9062462, (byte) 0, "#r#e" + 초 + "분 후#n#k에 미니게임에 참여할 수 있어요!\r\n\r\n#b(미니게임 이용 중 포기를 하는 경우 잠시 동안 미니게임을 이용할 수 없습니다.)#k", "00 00", (byte) 0, mapleCharacter.getId()));
        } else if (multiYutMagchingQueue.size() >= 2) {
            mapleCharacter.getClient().send(CField.NPCPacket.getNPCTalk(9062462, (byte) 0, "잠시 후에 다시 시도해 주세요.", "00 00", (byte) 0, mapleCharacter.getId()));
        } else if (!multiYutMagchingQueue.contains(mapleCharacter)) {
            multiYutMagchingQueue.add(mapleCharacter);
            mapleCharacter.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter, 993189800, new int[]{11, 2, 1, 18}));
            if (multiYutMagchingQueue.size() == n) {
                for (MapleCharacter mapleCharacter2 : multiYutMagchingQueue) {
                    mapleCharacter2.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter2, 0, new int[]{19, 0, 1, 18}));
                }
            }
        }
    }

    public void sendPacketToPlayers(byte[] packet) {
        for (MultiYutPlayer player : this.players) {
            player.getPlayer().getClient().getSession().writeAndFlush(packet);
        }
    }

    public ScheduledFuture<?> getMultiYutTimer() {
        return this.multiYutTimer;
    }

    public void setMultiYutTimer(ScheduledFuture<?> multiYutTimer) {
        this.multiYutTimer = multiYutTimer;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public MultiYutPlayer getWhoTurn() {
        return this.whoTurn;
    }

    public void setWhoTurn(MultiYutPlayer whoTurn) {
        this.whoTurn = whoTurn;
    }
}
