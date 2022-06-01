package server.games;

import client.MapleCharacter;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.SkillFactory;
import handling.channel.ChannelServer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import server.Timer;
import server.maps.MapleMap;
import tools.Pair;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class BigWisp {

    public static boolean isrun = false;

    public static int stage = 0;

    public static int count = 1;

    public static List<MapleCharacter> MatchingQueue = new ArrayList<>();

    public static List<MapleCharacter> MatchingQueue2 = new ArrayList<>();

    public static void StartGame() {
        stage = 0;
        MapleMap map = ChannelServer.getInstance(1).getMapFactory().getMap(993187400);
        Timer.EtcTimer.getInstance().schedule(() -> {
            map.broadcastMessage(CField.enforceMSG("거대위습의 폭격을 피해가며 사탕주머니를 획득해 보세요!", 162, 6000));
            map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispHandler(2, 1));
        }, 1500L);
        NextStage(map);
    }

    public static void NextStage(MapleMap map) {
        stage++;
        if (stage >= 4) {
            Map<MapleCharacter, Integer> Rank = new LinkedHashMap<>();
            for (MapleCharacter chr : map.getAllCharactersThreadsafe()) {
                if (chr.getKeyValue(100662, "BigWispPoint") > 30L) {
                    Rank.put(chr, Integer.valueOf((int) chr.getKeyValue(100661, "BigWispPoint")));
                }
            }
            int i = 1;
            for (Map.Entry<MapleCharacter, Integer> linfo : Rank.entrySet()) {
                ((MapleCharacter) linfo.getKey()).setKeyValue(100662, "rank", i + "");
                i++;
            }
            for (MapleCharacter chr : map.getAllChracater()) {
                chr.cancelEffect(chr.getBuffedEffect(80002557));
                chr.warp(993187500);
            }
        } else {
            int time = 5000;
            if (stage == 1) {
                map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispPutInfo("", new int[]{0, 0, 0, 22, 700, 60, 8}));
                map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispPutInfo("", new int[]{1, 1, 5000, 120, 3}));
            } else if (stage == 2) {
                map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispPutInfo("", new int[]{0, 0, 0, 12, 500, 60, 15}));
                map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispPutInfo("", new int[]{1, 1, 5000, 150, 10}));
            } else if (stage == 3) {
                map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispPutInfo("", new int[]{0, 0, 0, 12, 400, 60, 18}));
                map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispPutInfo("", new int[]{1, 1, 5000, 150, 8}));
            }
            map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispPutInfo("Effect/EventEffect.img/HundredShooting/user/hit2", new int[]{2, 0, 5000, 50, 15, 30}));
            map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispPutInfo("Effect/EventEffect.img/HundredShooting/user/hit3", new int[]{2, 1, 10000, 50, 5, 150}));
            Timer.EtcTimer.getInstance().schedule(() -> map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispHandler(3, stage)), time);
            Timer.EtcTimer.getInstance().schedule(() -> map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispHandler(4, stage)), (time + 4000));
            Timer.EtcTimer.getInstance().schedule(() -> map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispHandler(5, stage)), (time + 55000));
            Timer.EtcTimer.getInstance().schedule(() -> NextStage(map), (time + 57000));
            if (stage == 1) {
                count = 1;
                ChangePattren(map, time * count, 1, 0, 60, 500, 80, 3);
                ChangePattren(map, time * count, 0, 0, 22, 700, 60, 8);
                ChangePattren(map, time * count, 1, 0, 60, 500, 80, 3);
                ChangePattren(map, time * count, 0, 0, 22, 700, 60, 8);
                ChangePattren(map, time * count, 1, 0, 60, 500, 80, 3);
                ChangePattren(map, time * count, 0, 0, 22, 700, 60, 8);
                ChangePattren(map, time * count, 1, 0, 60, 500, 80, 3);
                ChangePattren(map, time * count, 0, 0, 22, 700, 60, 8);
                ChangePattren(map, time * count, 1, 0, 60, 500, 80, 3);
                ChangePattren(map, time * count, 0, 0, 22, 700, 60, 8);
            } else if (stage == 2) {
                count = 1;
                ChangePattren(map, time * count, 1, 0, 60, 300, 80, 5);
                ChangePattren(map, time * count, 0, 0, 20, 20, 80, 1);
                ChangePattren(map, time * count, 0, 0, 12, 500, 60, 15);
                ChangePattren(map, time * count, 1, 0, 60, 300, 80, 5);
                ChangePattren(map, time * count, 0, 0, 20, 20, 80, 1);
                ChangePattren(map, time * count, 1, 0, 60, 300, 80, 5);
                ChangePattren(map, time * count, 0, 0, 20, 20, 80, 1);
                ChangePattren(map, time * count, 0, 0, 12, 500, 60, 15);
                ChangePattren(map, time * count, 0, 0, 20, 20, 80, 1);
                ChangePattren(map, time * count, 0, 0, 12, 500, 60, 15);
            } else if (stage == 3) {
                count = 1;
                ChangePattren(map, time * count, 1, 0, 60, 300, 80, 6);
                ChangePattren(map, time * count, 1, 0, 60, 300, 80, 6);
                ChangePattren(map, time * count, 0, 0, 10, 400, 60, 18);
                ChangePattren(map, time * count, 1, 0, 60, 300, 80, 6);
                ChangePattren(map, time * count, 0, 0, 10, 400, 60, 18);
                ChangePattren(map, time * count, 0, 0, 1, 3, 80, 4);
                ChangePattren(map, time * count, 0, 0, 15, 15, 80, 3);
                ChangePattren(map, time * count, 0, 0, 1, 3, 80, 4);
                ChangePattren(map, time * count, 0, 0, 15, 15, 80, 3);
                ChangePattren(map, time * count, 0, 0, 10, 400, 60, 18);
                ChangePattren(map, time * count, 1, 0, 60, 300, 80, 6);
            }
        }
    }

    public static void ChangePattren(MapleMap map, int time, int a, int b, int c, int d, int e, int f) {
        count++;
        Timer.EtcTimer.getInstance().schedule(() -> {
            Map<MapleCharacter, Integer> Rank = new LinkedHashMap<>();
            for (MapleCharacter chr : map.getAllCharactersThreadsafe()) {
                if (Rank.size() < 30 && chr.getKeyValue(100662, "BigWispPoint") > 30L) {
                    Rank.put(chr, Integer.valueOf((int) chr.getKeyValue(100662, "BigWispPoint")));
                }
            }
            map.broadcastMessage(SLFCGPacket.BigWispPacket.BigWispRank(Rank));
            map.broadcastMessage(SLFCGPacket.BigWispPacket.BigPatternChange(new int[]{a, b, c, d, e, f}));
        }, time);
    }

    public static void ExitWaiting(MapleCharacter chr) {
        if (MatchingQueue != null && MatchingQueue2 != null) {
            if (MatchingQueue.contains(chr)) {
                MatchingQueue.remove(chr);
            }
            if (MatchingQueue2.contains(chr)) {
                MatchingQueue2.remove(chr);
            }
        }
    }

    public static void StartGame2(int n) {
        isrun = true;
        StartWaiting(n);
        Timer.EtcTimer.getInstance().schedule(() -> {
            isrun = false;
            if (MatchingQueue.size() >= n) {
                for (MapleCharacter chr : MatchingQueue) {
                    List<Pair<SecondaryStat, SecondaryStatValueHolder>> allBuffs = new ArrayList<>();
                    for (Pair<SecondaryStat, SecondaryStatValueHolder> buff : chr.getEffects()) {
                        if (SkillFactory.getSkill(((SecondaryStatValueHolder) buff.getRight()).effect.getSourceId()) != null) {
                            allBuffs.add(buff);
                        }
                    }
                    for (Pair<SecondaryStat, SecondaryStatValueHolder> data : allBuffs) {
                        SecondaryStatValueHolder mbsvh = (SecondaryStatValueHolder) data.right;
                        chr.cancelEffect(mbsvh.effect);
                    }
                    chr.warp(993187300);
                    chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, new int[]{11, 5, 1, 22}));
                }
            } else {
                for (MapleCharacter chr : MatchingQueue) {
                    chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, new int[]{11, 5, 1, 22}));
                    chr.dropMessage(5, "게임을 시작하기 위한 최소 인원이 부족하여 대기열이 취소 되었습니다.");
                }
                MatchingQueue.clear();
                MatchingQueue2.clear();
            }
        }, 30000L);
    }

    public static void StartWaiting(int n) {
        MapleMap map = ChannelServer.getInstance(1).getMapFactory().getMap(993187300);
        map.setCustomInfo(993100, 0, 60000);
        Timer.EtcTimer.getInstance().schedule(() -> {
            if (MatchingQueue.size() >= n) {
                for (MapleCharacter chr : MatchingQueue) {
                    chr.warp(993187400);
                    SkillFactory.getSkill(80002557).getEffect(1).applyTo(chr);
                    chr.setKeyValue(100662, "BigWispPoint", "0");
                    chr.setKeyValue(100662, "rank", "0");
                }
                MatchingQueue.clear();
                MatchingQueue2.clear();
                StartGame();
            }
        }, 60000L);
    }

    public static void addQueue(MapleCharacter chr) {
        if (!MatchingQueue.contains(chr)) {
            if (isrun) {
                MatchingQueue.add(chr);
                chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 993187300, new int[]{11, 2, 1, 22}));
            } else {
                chr.getClient().send(CField.NPCPacket.getNPCTalk(9062354, (byte) 0, "아쉽지만. 참여 가능한 시간이 다 지나 버렸어요!\r\n다음에 만나요!", "00 00", (byte) 0, chr.getId()));
            }
        }
    }
}
