/**
 * @package : server.poloFritto
 * @author : Yein
 * @fileName : FrittoDancing.java
 * @date : 2019. 12. 1.
 */
package server.polofritto;

import client.MapleClient;
import server.Randomizer;
import server.Timer.EventTimer;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class FrittoDancing {

    private int state;
    private ScheduledFuture<?> sc;
    private List<List<Integer>> waveData = new ArrayList<>();

    public FrittoDancing(int state) {
        this.state = state;
    }

    public void updateDefenseWave(MapleClient c) {
        c.getSession().writeAndFlush(SLFCGPacket.courtShipDanceState(state)); // 2 or 1
    }

    public void updateNewWave(MapleClient c) {
        c.getSession().writeAndFlush(CField.environmentChange("defense/count", 16));

        EventTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (c.getPlayer().getMapId() == 993000400) {
                    c.getSession().writeAndFlush(CField.environmentChange("killing/first/start", 16));
                    c.getSession().writeAndFlush(SLFCGPacket.courtShipDanceCommand(waveData));
                }
            }
        }, 3 * 1000);

    }

    public void finish(MapleClient c) {
        if (sc != null) {
            sc.cancel(false);
        }

        c.getSession().writeAndFlush(CField.environmentChange("killing/clear", 16));

        EventTimer.getInstance().schedule(new Runnable() {
            public void run() {
                if (c != null && c.getPlayer() != null) {
                    c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
                    c.getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0xA, 0x0));
                    c.getPlayer().warp(993000601);
                }
            }
        }, 2000);
    }

    public void insertWaveData() {
        List<Integer> waves = new ArrayList<>();

        // 1웨이브
        for (int i = 0; i < 4; ++i) {
            waves.add(Randomizer.nextInt(4));
        }

        waveData.add(waves);

        List<Integer> waves2 = new ArrayList<>();

        //2웨이브
        for (int i = 0; i < 6; ++i) {
            waves2.add(Randomizer.nextInt(4));
        }

        waveData.add(waves2);

        //3, 4, 5웨이브
        for (int a = 0; a < 3; ++a) {
            List<Integer> waves3 = new ArrayList<>();
            for (int i = 0; i < 7; ++i) {
                waves3.add(Randomizer.nextInt(4));
            }
            waveData.add(waves3);
        }

        //6, 7, 8웨이브
        for (int a = 0; a < 3; ++a) {
            List<Integer> waves4 = new ArrayList<>();
            for (int i = 0; i < 8; ++i) {
                waves4.add(Randomizer.nextInt(4));
            }
            waveData.add(waves4);
        }

        //9, 10웨이브
        for (int a = 0; a < 2; ++a) {
            List<Integer> waves5 = new ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                waves5.add(Randomizer.nextInt(4));
            }
            waveData.add(waves5);
        }
    }

    public void start(MapleClient c) {

        updateDefenseWave(c);
        insertWaveData();

        c.getPlayer().setKeyValue(15143, "score", "0");
        c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
        c.getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0xA, 0x1));
        c.getSession().writeAndFlush(CField.environmentChange("PoloFritto/msg3", 20));
        c.getSession().writeAndFlush(CField.startMapEffect("달걀을 훔치려면 먼저 닭들을 속여야 해! 자, 나를 따라 구애의 춤을 춰!", 5120160, true));
        c.getSession().writeAndFlush(CField.getClock(1 * 60));
        updateNewWave(c);

        sc = EventTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (c.getPlayer().getMapId() == 993000400) {
                    c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
                    c.getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0xA, 0x0));
                    c.getPlayer().warp(993000601);
                }
            }
        }, 1 * 60 * 1000);

    }

    public List<List<Integer>> getWaveData() {
        return waveData;
    }

    public void setWaveData(List<List<Integer>> waveData) {
        this.waveData = waveData;
    }

    public ScheduledFuture<?> getSc() {
        return sc;
    }

    public void setSc(ScheduledFuture<?> sc) {
        this.sc = sc;
    }
}
