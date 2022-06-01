package server.games;

import client.MapleCharacter;
import client.MapleCoolDownValueHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import tools.Pair;
import tools.Triple;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class MonsterPyramid {

    public static List<MapleCharacter> monsterPyramidMatchingQueue;

    public static List<MapleCharacter> monsterPyramidMatchingQueue2 = new ArrayList<>();

    public static List<Triple<Integer, Integer, Integer>> pyramidcheck = new ArrayList<>();

    private List<PyramidPlayer> a;

    public int round = 0;

    private ScheduledFuture<?> MonsterPyramidTimer = null;

    public MonsterPyramid(List<MapleCharacter> list) {
        this.round = 1;
        this.a = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            this.a.add(new PyramidPlayer(list.get(i), (byte) i, false, i));
        }
        Iterator<PyramidPlayer> iterator = getPlayers().iterator();
        if (iterator.hasNext()) {
            PyramidPlayer p = iterator.next();
            p.randBlocks();
        }
    }

    public PyramidPlayer getPlayer(MapleCharacter mapleCharacter) {
        Iterator<PyramidPlayer> iterator = getPlayers().iterator();
        while (iterator.hasNext()) {
            PyramidPlayer pyramidPlayer;
            if ((pyramidPlayer = iterator.next()).getPlayer().getId() == mapleCharacter.getId()) {
                return pyramidPlayer;
            }
        }
        return null;
    }

    public void skipPlayer(PyramidPlayer p) {
        int floor = -2, pos = -2, type = -2;
        if (this.MonsterPyramidTimer != null || p.b == null) {
            this.MonsterPyramidTimer.cancel(true);
            this.MonsterPyramidTimer = null;
        }
        for (PyramidPlayer pp : getPlayers()) {
            if (pp.b.getId() == p.b.getId()) {
                for (Triple<Integer, Integer, Integer> check : pyramidcheck) {
                    if (((Integer) check.getLeft()).intValue() == 0 && ((Integer) check.getRight()).intValue() == -1) {
                        for (Pair<Integer, Integer> block : (Iterable<Pair<Integer, Integer>>) pp.blocktype) {
                            if (((Integer) block.getRight()).intValue() > 0) {
                                floor = ((Integer) check.getLeft()).intValue();
                                pos = ((Integer) check.getMid()).intValue();
                                type = ((Integer) block.getLeft()).intValue();
                            }
                        }
                    }
                }
                if (floor == -2) {
                    for (Triple<Integer, Integer, Integer> check : pyramidcheck) {
                        if (((Integer) check.getLeft()).intValue() > 0 && ((Integer) check.getRight()).intValue() == -1) {
                            for (Pair<Integer, Integer> block : (Iterable<Pair<Integer, Integer>>) pp.blocktype) {
                                if (test(((Integer) check.getLeft()).intValue(), ((Integer) check.getMid()).intValue(), ((Integer) block.getLeft()).intValue()) && ((Integer) block.getRight()).intValue() > 0) {
                                    floor = ((Integer) check.getLeft()).intValue();
                                    pos = ((Integer) check.getMid()).intValue();
                                    type = ((Integer) block.getLeft()).intValue();
                                }
                            }
                        }
                    }
                }
                if (p.b.getKeyValue(100668, "피라미드제한") < 0L) {
                    p.b.setKeyValue(100668, "피라미드제한", "0");
                }
                p.b.setKeyValue(100668, "피라미드제한", (p.b.getKeyValue(100668, "피라미드제한") + 1L) + "");
                p.b.getClient().getSession().writeAndFlush(CField.UIPacket.detailShowInfo("제한시간 총 " + p.b.getKeyValue(100668, "피라미드제한") + "번 이상 되셨습니다.(3번 이상 퇴장 및 패널티)", 3, 20, 20));
                List<MapleCharacter> chrs = new ArrayList<>();
                for (PyramidPlayer pp3 : getPlayers()) {
                    if (pp3 != null) {
                        chrs.add(pp3.b);
                    }
                }
                for (PyramidPlayer p2 : getPlayers()) {
                    p2.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(chrs, false, new int[]{1, 4, 0, pp.turn, 7, 0, 0}));
                }
                if (p.b.getKeyValue(100668, "피라미드제한") >= 3L) {
                    endGame(pp.b, true);
                    break;
                }
                setBlock(pp.b, floor, pos, type);
                break;
            }
        }
    }

    public void endGame(MapleCharacter chr, boolean gang) {
        if (this.MonsterPyramidTimer != null) {
            this.MonsterPyramidTimer.cancel(false);
            this.MonsterPyramidTimer = null;
        }
        if (gang) {
            chr.addCooldown(100668, System.currentTimeMillis(), 900000L);
            chr.setKeyValue(100668, "Rank", "0");
        }
        for (PyramidPlayer p : getPlayers()) {
            if (p != null) {
                if (gang) {
                    p.b.getClient().getSession().writeAndFlush(CField.UIPacket.detailShowInfo(chr.getName() + "님이 전장을 이탈하여 게임이 종료 되었습니다.", 3, 20, 20));
                }
                p.b.warp(993186500);
                p.b.setMonsterPyramidInstance(null);
            }
        }
    }

    public void playerDead() {
    }

    public static void StartGame(int n) {
        if (monsterPyramidMatchingQueue.size() == n) {
            MonsterPyramidMatchingInfo monsterPyramidMatchingInfo = new MonsterPyramidMatchingInfo(monsterPyramidMatchingQueue);
            ArrayList<MapleCharacter> list = new ArrayList<>();
            for (MapleCharacter mapleCharacter2 : monsterPyramidMatchingInfo.players) {
                monsterPyramidMatchingQueue.remove(mapleCharacter2);
                list.add(mapleCharacter2);
                mapleCharacter2.warp(993186400);
                mapleCharacter2.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter2, 0, new int[]{11, 5, 1, 24}));
            }
            pyramidcheck.clear();
            for (int i = 0; i < 8; i++) {
                for (int i2 = 0; i2 < 8 - i; i2++) {
                    pyramidcheck.add(new Triple<>(Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(-1)));
                }
            }
            server.Timer.EtcTimer.getInstance().schedule(() -> {
                MonsterPyramid monsterPyramidInstance = null;
                try {
                    monsterPyramidInstance = new MonsterPyramid(list);
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    for (MapleCharacter p : list) {
                        p.setMonsterPyramidInstance(monsterPyramidInstance);
                        p.setKeyValue(100668, "피라미드제한", "0");
                        p.getClient().send(SLFCGPacket.MonsterPyramidPacket.createUI(list));
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }, 5000L);
            return;
        }
    }

    private void startTimer(PyramidPlayer pp) {
        this.MonsterPyramidTimer = server.Timer.EventTimer.getInstance().schedule(() -> skipPlayer(pp), 11000L);
    }

    public static void CancelWaiting(MapleCharacter chr, int n) {
        List<MapleCharacter> remover = new ArrayList<>();
        monsterPyramidMatchingQueue.remove(chr);
        for (MapleCharacter chr2 : monsterPyramidMatchingQueue) {
            if (chr.getId() != chr2.getId()) {
                remover.add(chr2);
                chr2.getClient().send(SLFCGPacket.ContentsWaiting(chr2, 0, new int[]{11, 5, 1, 24}));
            }
        }
        for (MapleCharacter chr2 : remover) {
            monsterPyramidMatchingQueue.remove(chr2);
        }
        chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, new int[]{11, 5, 1, 24}));
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
            mapleCharacter.getClient().send(CField.NPCPacket.getNPCTalk(9062354, (byte) 0, "#r#e" + 초 + "분 후#n#k에 미니게임에 참여할 수 있어요!\r\n\r\n#b(미니게임 이용 중 포기를 하는 경우 잠시 동안 미니게임을 이용할 수 없습니다.)#k", "00 00", (byte) 0, mapleCharacter.getId()));
        } else if (monsterPyramidMatchingQueue.size() >= 3) {
            mapleCharacter.getClient().send(CField.NPCPacket.getNPCTalk(9062354, (byte) 0, "잠시 후에 다시 시도해 주세요.", "00 00", (byte) 0, mapleCharacter.getId()));
        } else if (!monsterPyramidMatchingQueue.contains(mapleCharacter)) {
            monsterPyramidMatchingQueue.add(mapleCharacter);
            mapleCharacter.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter, 993186400, new int[]{11, 2, 1, 24}));
            if (monsterPyramidMatchingQueue.size() == n) {
                for (MapleCharacter mapleCharacter2 : monsterPyramidMatchingQueue) {
                    mapleCharacter2.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter2, 0, new int[]{19, 0, 1, 24}));
                    mapleCharacter2.ConstentTimer = new Timer();
                    mapleCharacter2.ConstentTimer.schedule(new TimerTask() {
                        public void run() {
                            List<MapleCharacter> remover = new ArrayList<>();
                            MonsterPyramid.monsterPyramidMatchingQueue.remove(mapleCharacter2);
                            for (MapleCharacter chr2 : MonsterPyramid.monsterPyramidMatchingQueue) {
                                if (mapleCharacter2.getId() != chr2.getId()) {
                                    remover.add(chr2);
                                    chr2.getClient().send(SLFCGPacket.ContentsWaiting(chr2, 0, new int[]{11, 5, 1, 24}));
                                }
                            }
                            for (MapleCharacter chr2 : remover) {
                                MonsterPyramid.monsterPyramidMatchingQueue.remove(chr2);
                                MonsterPyramid.monsterPyramidMatchingQueue2.remove(chr2);
                            }
                            mapleCharacter2.getClient().send(SLFCGPacket.ContentsWaiting(mapleCharacter2, 0, new int[]{11, 5, 1, 24}));
                            if (mapleCharacter2.ConstentTimer != null) {
                                mapleCharacter2.ConstentTimer.cancel();
                                mapleCharacter2.ConstentTimer = null;
                            }
                        }
                    },
                            10000L);
                }
            }
        }

    }

    public List<PyramidPlayer> getPlayers() {
        return this.a;
    }

    public void setPlayers(List<PyramidPlayer> a) {
        this.a = a;
    }

    static {
        monsterPyramidMatchingQueue = new ArrayList<>();
    }

    public class PyramidPlayer {

        private byte a;

        private int point = 0;

        private int rank = 0;

        private int turn = 0;

        private MapleCharacter b;

        private List<Pair<Integer, Integer>> blocktype = new ArrayList<>();

        public PyramidPlayer(MapleCharacter b, byte a, boolean select6, int turn) {
            this.b = b;
            this.a = a;
            this.turn = turn;
        }

        public void randBlocks() {
            List<Pair<Integer, Integer>> blockadd = new ArrayList<>();
            blockadd.add(new Pair<>(Integer.valueOf(0), Integer.valueOf(7)));
            blockadd.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(7)));
            blockadd.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(7)));
            blockadd.add(new Pair<>(Integer.valueOf(3), Integer.valueOf(7)));
            blockadd.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(7)));
            blockadd.add(new Pair<>(Integer.valueOf(5), Integer.valueOf(1)));
            int i2 = 0;
            for (PyramidPlayer p : MonsterPyramid.this.getPlayers()) {
                p.blocktype.clear();
                if (p.blocktype.isEmpty()) {
                    for (int i = 0; i < 6; i++) {
                        p.blocktype.add(new Pair<>(Integer.valueOf(i), Integer.valueOf(0)));
                    }
                }
                p.randblock(blockadd, p, i2);
                i2++;
            }
        }

        public List<Pair<Integer, Integer>> randblock(List<Pair<Integer, Integer>> blockadd, PyramidPlayer p, int n) {
            boolean stop = false, pass = false;
            int beforepblocksize = 0, after = 0, typeblocksize = 0;
            if (n < 2) {
                while (!stop) {
                    beforepblocksize = 0;
                    for (Pair<Integer, Integer> bt : p.blocktype) {
                        beforepblocksize += ((Integer) bt.getRight()).intValue();
                    }
                    if (beforepblocksize < 12) {
                        for (Pair<Integer, Integer> blc : blockadd) {
                            if (((Integer) blc.getRight()).intValue() > 0) {
                                int type = ((Integer) blc.getLeft()).intValue();
                                int rand = 2;
                                for (Pair<Integer, Integer> bt : p.blocktype) {
                                    if (bt.getLeft() == blc.getLeft()) {
                                        typeblocksize = ((Integer) bt.getRight()).intValue();
                                        break;
                                    }
                                }
                                if (typeblocksize + rand > 4) {
                                    int su = beforepblocksize + rand - 4;
                                    rand -= su;
                                }
                                if (beforepblocksize + rand > 12) {
                                    int su = beforepblocksize + rand - 12;
                                    rand -= su;
                                }
                                if (rand > ((Integer) blc.getRight()).intValue()) {
                                    rand = ((Integer) blc.getRight()).intValue();
                                }
                                if (rand > 0) {
                                    blockadd.set(type, new Pair<>(Integer.valueOf(type), Integer.valueOf(((Integer) blc.getRight()).intValue() - rand)));
                                    beforepblocksize += rand;
                                    for (Pair<Integer, Integer> bt : p.blocktype) {
                                        if (((Integer) bt.getLeft()).intValue() == type) {
                                            p.blocktype.set(type, new Pair<>(Integer.valueOf(type), Integer.valueOf(((Integer) bt.getRight()).intValue() + rand)));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    after++;
                    if (beforepblocksize >= 12 || after > 100000) {
                        if (after > 100000) {
                            for (PyramidPlayer pp : MonsterPyramid.this.getPlayers()) {
                                pp.b.getClient().getSession().writeAndFlush(CField.UIPacket.detailShowInfo("오류발생! 게임을 다시 시작하여 주세요!", 3, 20, 20));
                            }
                            MonsterPyramid.this.endGame(p.getPlayer(), false);
                        }
                        stop = true;
                        break;
                    }
                }
            } else {
                for (Pair<Integer, Integer> bt : p.blocktype) {
                    for (Pair<Integer, Integer> blc : blockadd) {
                        if (bt.getLeft() == blc.getLeft()) {
                            p.blocktype.set(((Integer) blc.getLeft()).intValue(), new Pair<>(blc.getLeft(), blc.getRight()));
                        }
                    }
                }
            }
            return blockadd;
        }

        public MapleCharacter getPlayer() {
            return this.b;
        }

        public byte getPosition() {
            return this.a;
        }

        public List<Pair<Integer, Integer>> getBlocks() {
            return this.blocktype;
        }

        public int getSelectBlockType(int type) {
            int a = 0;
            for (Pair<Integer, Integer> block : this.blocktype) {
                if (((Integer) block.getLeft()).intValue() == type) {
                    a = ((Integer) block.getRight()).intValue();
                    break;
                }
            }
            return a;
        }

        public void setPosition(byte a) {
            this.a = a;
        }

        public int getPoint() {
            return this.point;
        }

        public void setPoint(int a) {
            this.point = a;
        }

        public int getRank() {
            return this.rank;
        }

        public void setRank(int a) {
            this.rank = a;
        }
    }

    public static class MonsterPyramidMatchingInfo {

        public List<MapleCharacter> players = new ArrayList<>();

        public MonsterPyramidMatchingInfo(List<MapleCharacter> list) {
            Iterator<MapleCharacter> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.players.add(iterator.next());
            }
        }
    }

    public void setBlock(MapleCharacter chr, int floor, int pos, int type) {
        int a = 0;
        for (Pair<Integer, Integer> block : (Iterable<Pair<Integer, Integer>>) (getPlayer(chr)).blocktype) {
            if (((Integer) block.getLeft()).intValue() == type) {
                a = ((Integer) block.getRight()).intValue();
                break;
            }
        }
        (getPlayer(chr)).blocktype.set(type, new Pair<>(Integer.valueOf(type), Integer.valueOf(a - 1)));
        int index = 0;
        for (Triple<Integer, Integer, Integer> check : pyramidcheck) {
            if (((Integer) check.getLeft()).intValue() == floor && ((Integer) check.getMid()).intValue() == pos && ((Integer) check.getRight()).intValue() == -1) {
                pyramidcheck.set(index, new Triple<>(Integer.valueOf(floor), Integer.valueOf(pos), Integer.valueOf(type)));
                break;
            }
            index++;
        }
        getPlayer(chr).setPoint(getPlayer(chr).getPoint() + 1);
        if (this.MonsterPyramidTimer != null) {
            this.MonsterPyramidTimer.cancel(false);
        }
        List<MapleCharacter> chrs = new ArrayList<>();
        for (PyramidPlayer pp : getPlayers()) {
            pp.setRank(2);
            for (PyramidPlayer ppp : getPlayers()) {
                if (pp.getPoint() > ppp.getPoint()) {
                    pp.setRank(pp.getRank() - 1);
                }
            }
            if (pp != null) {
                chrs.add(pp.b);
            }
        }
        int ntturn = nextTurn(chr.getMonsterPyramidInstance().getPlayer(chr)), passcount = 0;
        boolean passcheck = false;
        for (PyramidPlayer pp : getPlayers()) {
            if (pp.turn == ntturn) {
                this.MonsterPyramidTimer = server.Timer.EventTimer.getInstance().schedule(() -> skipPlayer(pp), 11000L);
                if (nextTurnCheck(pp)) {
                    passcheck = true;
                }
                break;
            }
        }
        if (passcheck) {
            passcheck = false;
            int nttturn = ntturn - 1;
            if (nttturn < 0) {
                nttturn = 2;
            }
            for (PyramidPlayer pp : getPlayers()) {
                pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(chrs, true, new int[]{1, 3, nttturn, this.round, 0, 0, 0, 3, 3, 0}));
            }
            ntturn++;
            if (ntturn >= 3) {
                ntturn = 0;
            }
            for (PyramidPlayer pp : getPlayers()) {
                if (pp.turn == ntturn) {
                    ntturn++;
                    if (nextTurnCheck(pp) && ntturn >= 3) {
                        ntturn = 0;
                        pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(chrs, true, new int[]{1, 3, ntturn, this.round, 0, 0, 0, 3, 3, 0}));
                    }
                    break;
                }
            }
        }
        for (PyramidPlayer pp : getPlayers()) {
            if (nextTurnCheck(pp)) {
                passcount++;
            }
        }
        for (PyramidPlayer pp : getPlayers()) {
            pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(chrs, false, new int[]{1, 3, ntturn, this.round, floor, pos, type, 13, 1, 6}));
        }
        int blockcount = 0;
        for (Pair<Integer, Integer> block : (Iterable<Pair<Integer, Integer>>) (getPlayer(chr)).blocktype) {
            blockcount += ((Integer) block.getRight()).intValue();
        }
        if (passcount >= 3 || blockcount == 0) {
            for (PyramidPlayer pp : getPlayers()) {
                if (this.round < 3) {
                    if (blockcount == 0) {
                        pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(chrs, true, new int[]{1, 6, ntturn, this.round, ntturn - 1}));
                    }
                    pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(chrs, true, new int[]{1, 4, pp.turn, this.round, 5, 0, 9}));
                }
            }
            nextStage(ntturn, (blockcount == 0));
        }
    }

    public void nextStage(int ntturn, boolean used) {
        this.round++;
        if (this.MonsterPyramidTimer != null) {
            this.MonsterPyramidTimer.cancel(true);
            this.MonsterPyramidTimer = null;
        }
        if (this.round >= 4) {
            List<MapleCharacter> list = new ArrayList<>();
            for (PyramidPlayer pp : getPlayers()) {
                list.add(pp.b);
            }
            for (PyramidPlayer pp : getPlayers()) {
                pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(list, true, new int[]{1, 4, pp.turn, this.round, 5, 0, 9}));
                pp.setRank(2);
                for (PyramidPlayer ppp : getPlayers()) {
                    if (pp.getPoint() > ppp.getPoint()) {
                        pp.setRank(pp.getRank() - 1);
                    }
                }
                if (pp.getRank() == 0) {
                    pp.b.setKeyValue(100668, "Rank", "1");
                    pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(list, true, new int[]{
                        1, 3, pp.turn, 3, 0, 0, 0, 11, 0, 0,
                        0}));
                    continue;
                }
                if (pp.getRank() == 1) {
                    pp.b.setKeyValue(100668, "Rank", "2");
                    pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(list, true, new int[]{
                        1, 3, pp.turn, 3, 0, 0, 0, 12, 0, 0,
                        0}));
                    continue;
                }
                pp.b.setKeyValue(100668, "Rank", "3");
                pp.b.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(list, true, new int[]{
                    1, 3, pp.turn, 3, 0, 0, 0, 12, 0, 0,
                    0}));
            }
            server.Timer.EtcTimer.getInstance().schedule(() -> endGame(null, false), 4000L);
        } else {
            pyramidcheck.clear();
            for (int i = 0; i < 8; i++) {
                for (int i2 = 0; i2 < 8 - i; i2++) {
                    pyramidcheck.add(new Triple<>(Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(-1)));
                }
            }
            List<MapleCharacter> list = new ArrayList<>();

            for (PyramidPlayer pp : getPlayers()) {
                list.add(pp.b);
            }
            server.Timer.EtcTimer.getInstance().schedule(() -> {
                for (MapleCharacter p : list) {
                    p.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(list, false, new int[]{1, 5, ntturn, this.round}));
                }
            },
                    4000L);
            server.Timer.EtcTimer.getInstance().schedule(() -> {
                for (MapleCharacter p : list) {
                    p.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(list, true, new int[]{1, 4, ntturn, this.round, 0, 0, 0, 8, 3, 6}));
                    p.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(list, true, new int[]{1, 4, ntturn, this.round, 8, 0, 6}));
                    p.getClient().send(SLFCGPacket.MonsterPyramidPacket.Handler(list, true, new int[]{1, 3, ntturn, this.round, 0, 0, 0, 8, 3, 6}));
                }
            },
                    6000L);
        }
    }

    public boolean nextTurnCheck(PyramidPlayer p) {
        boolean pass = false;
        int insertnow = 0;
        for (PyramidPlayer pp : getPlayers()) {
            if (pp.b.getId() == p.b.getId()) {
                for (Triple<Integer, Integer, Integer> check : pyramidcheck) {
                    if (((Integer) check.getLeft()).intValue() == 0 && ((Integer) check.getRight()).intValue() == -1) {
                        for (Pair<Integer, Integer> block : (Iterable<Pair<Integer, Integer>>) pp.blocktype) {
                            insertnow += ((Integer) block.getRight()).intValue();
                        }
                    }
                }
                for (Triple<Integer, Integer, Integer> check : pyramidcheck) {
                    if (((Integer) check.getLeft()).intValue() > 0 && ((Integer) check.getRight()).intValue() == -1) {
                        for (Pair<Integer, Integer> block : (Iterable<Pair<Integer, Integer>>) pp.blocktype) {
                            if (test(((Integer) check.getLeft()).intValue(), ((Integer) check.getMid()).intValue(), ((Integer) block.getLeft()).intValue())) {
                                insertnow += ((Integer) block.getRight()).intValue();
                            }
                        }
                    }
                }
                for (Pair<Integer, Integer> block : (Iterable<Pair<Integer, Integer>>) pp.blocktype) {
                    if (((Integer) block.getLeft()).intValue() == 5 && ((Integer) block.getRight()).intValue() > 0) {
                        insertnow++;
                    }
                }
                break;
            }
        }
        if (insertnow <= 0) {
            pass = true;
        }
        return pass;
    }

    public boolean test(int floor, int pos, int type) {
        boolean set = false;
        int a = -1, b = -1;
        for (Triple<Integer, Integer, Integer> check : pyramidcheck) {
            if (floor - 1 == ((Integer) check.getLeft()).intValue() && ((Integer) check.getMid()).intValue() == pos) {
                a = ((Integer) check.getRight()).intValue();
                break;
            }
        }
        for (Triple<Integer, Integer, Integer> check : pyramidcheck) {
            if (floor - 1 == ((Integer) check.getLeft()).intValue() && ((Integer) check.getMid()).intValue() == pos + 1) {
                b = ((Integer) check.getRight()).intValue();
                break;
            }
        }
        if ((type == a || type == b) && b != -1 && a != -1 && a != 5 && b != 5) {
            set = true;
        }
        return set;
    }

    public int nextTurn(PyramidPlayer p) {
        int a = 0;
        for (PyramidPlayer pp : getPlayers()) {
            if (pp.b.getId() == p.b.getId()) {
                a = p.turn;
                break;
            }
        }
        a++;
        if (a >= 3) {
            a = 0;
        }
        return a;
    }
}
