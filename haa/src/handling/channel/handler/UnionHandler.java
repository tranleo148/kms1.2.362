// 
// Decompiled by Procyon v0.5.36
// 

package handling.channel.handler;

import client.MapleUnion;
import tools.packet.CWvsContext;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import client.MapleClient;
import java.util.Iterator;
import provider.MapleDataProvider;
import java.util.ArrayList;
import java.util.HashMap;
import provider.MapleDataTool;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import java.io.File;
import java.util.Map;
import java.awt.Point;
import java.util.List;

public class UnionHandler
{
    public static List<Integer> groupIndex;
    public static List<Point> boardPos;
    public static List<Integer> openLevels;
    public static Map<Integer, Integer> cardSkills;
    public static Map<Integer, Map<Integer, List<Point>>> characterSizes;
    public static List<Integer> skills;
    
    public static void loadUnion() {
        final String WZpath = System.getProperty("wz");
        final MapleDataProvider prov = MapleDataProviderFactory.getDataProvider(new File(WZpath + "/Etc.wz"));
        final MapleData nameData = prov.getData("mapleUnion.img");
        try {
            for (final MapleData dat : nameData) {
                final String name = dat.getName();
                switch (name) {
                    case "BoardInfo": {
                        for (final MapleData d : dat) {
                            UnionHandler.groupIndex.add(MapleDataTool.getInt(d.getChildByPath("groupIndex")));
                            UnionHandler.boardPos.add(new Point(MapleDataTool.getInt(d.getChildByPath("xPos")), MapleDataTool.getInt(d.getChildByPath("yPos"))));
                            UnionHandler.openLevels.add(MapleDataTool.getInt(d.getChildByPath("openLevel")));
                        }
                        continue;
                    }
                    case "Card": {
                        for (final MapleData d : dat) {
                            UnionHandler.cardSkills.put(Integer.parseInt(d.getName()), MapleDataTool.getInt(d.getChildByPath("skillID")));
                        }
                        continue;
                    }
                    case "CharacterSize": {
                        for (final MapleData d : dat) {
                            final int num = Integer.parseInt(d.getName());
                            final Map<Integer, List<Point>> array = new HashMap<Integer, List<Point>>();
                            for (final MapleData z : d) {
                                final int idx = Integer.parseInt(z.getName());
                                final List<Point> arr = new ArrayList<Point>();
                                for (final MapleData zz : z) {
                                    final Point data = MapleDataTool.getPoint(zz);
                                    arr.add(data);
                                }
                                array.put(idx, arr);
                            }
                            UnionHandler.characterSizes.put(num, array);
                        }
                        continue;
                    }
                    case "SkillInfo": {
                        for (final MapleData d : dat) {
                            UnionHandler.skills.add(MapleDataTool.getInt(d.getChildByPath("skillID")));
                        }
                        continue;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void openUnion(final MapleClient c) {
        if (Integer.parseInt(c.getKeyValue("rank")) > c.getPlayer().getKeyValue(18771, "rank")) {
            c.getPlayer().setKeyValue(18771, "rank", c.getKeyValue("rank"));
        }
        if (c.getPlayer().getUnionDamage() > 0L) {
            c.getPlayer().RefreshUnionRaid(false);
        }
        c.getSession().writeAndFlush((Object)CField.openUnionUI(c));
    }
    
    public static void unionFreeset(final LittleEndianAccessor slea, final MapleClient c) {
        c.getSession().writeAndFlush((Object)CWvsContext.unionFreeset(c, slea.readInt()));
    }
    
    public static void setUnion(final LittleEndianAccessor slea, final MapleClient c) {
        try {
            final int priset = slea.readInt();
            c.getPlayer().removeKeyValue(500630);
            c.getPlayer().setKeyValue(500630, "presetNo", "" + priset + "");
            c.setKeyValue("presetNo", "" + priset);
            for (int batch = slea.readInt(), i = 0; i < batch; ++i) {
                final int t = slea.readInt();
                c.getPlayer().setKeyValue(18791, i + "", t + "");
                c.setCustomData(500627 + priset, i + "", t + "");
            }
            final int size2 = slea.readInt();
            slea.skip(2);
            if (size2 == 0) {
                for (final MapleUnion union : c.getPlayer().getUnions().getUnions()) {
                    if (priset == 0) {
                        union.setPriset(0);
                        union.setPos(0);
                    }
                    else if (priset == 1) {
                        union.setPriset1(0);
                        union.setPos1(0);
                    }
                    else if (priset == 2) {
                        union.setPriset2(0);
                        union.setPos2(0);
                    }
                    else if (priset == 3) {
                        union.setPriset3(0);
                        union.setPos3(0);
                    }
                    else {
                        if (priset != 4) {
                            continue;
                        }
                        union.setPriset4(0);
                        union.setPos4(0);
                    }
                }
            }
            final List<String> names = new ArrayList<String>();
            for (int j = 0; j < size2; ++j) {
                slea.skip(4);
                final int id = slea.readInt();
                final int lv = slea.readInt();
                final int job = slea.readInt();
                final int unk1 = slea.readInt();
                final int unk2 = slea.readInt();
                final int pos = slea.readInt();
                final int unk3 = slea.readInt();
                final String name = slea.readMapleAsciiString();
                names.add(name);
                for (final MapleUnion union2 : c.getPlayer().getUnions().getUnions()) {
                    if (union2.getCharid() == id) {
                        union2.setLevel(lv);
                        union2.setJob(job);
                        union2.setUnk1(unk1);
                        union2.setUnk2(unk2);
                        union2.setPosition(pos);
                        union2.setUnk3(unk3);
                        union2.setName(name);
                        if (priset == 0) {
                            union2.setPriset(pos);
                            union2.setPos(unk2);
                        }
                        else if (priset == 1) {
                            union2.setPriset1(pos);
                            union2.setPos1(unk2);
                        }
                        else if (priset == 2) {
                            union2.setPriset2(pos);
                            union2.setPos2(unk2);
                        }
                        else if (priset == 3) {
                            union2.setPriset3(pos);
                            union2.setPos3(unk2);
                        }
                        else {
                            if (priset != 4) {
                                continue;
                            }
                            union2.setPriset4(pos);
                            union2.setPos4(unk2);
                        }
                    }
                }
            }
            for (final MapleUnion union3 : c.getPlayer().getUnions().getUnions()) {
                if (union3.getPosition() != -1 && !names.contains(union3.getName())) {
                    union3.setPosition(-1);
                }
            }
            c.getSession().writeAndFlush((Object)CWvsContext.setUnion(c));
            c.send(CWvsContext.enableActions(c.getPlayer()));
            c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setUnionPriset(final LittleEndianAccessor slea, final MapleClient c) {
        try {
            final int priset = slea.readInt();
            c.getPlayer().removeKeyValue(500630);
            c.getPlayer().setKeyValue(500630, "presetNo", "" + priset + "");
            c.setKeyValue("presetNo", "" + priset);
            slea.skip(5);
            slea.skip(4);
            slea.skip(4);
            slea.skip(4);
            slea.skip(4);
            slea.skip(4);
            slea.skip(4);
            slea.skip(4);
            final int size2 = slea.readInt();
            final List<String> names = new ArrayList<String>();
            for (int i = 0; i < size2; ++i) {
                final int key = slea.readInt();
                final int id = slea.readInt();
                final int lv = slea.readInt();
                final int job = slea.readInt();
                final int unk1 = slea.readInt();
                final int unk2 = slea.readInt();
                final int pos = slea.readInt();
                final int unk3 = slea.readInt();
                final String name = slea.readMapleAsciiString();
                names.add(name);
                for (final MapleUnion union : c.getPlayer().getUnions().getUnions()) {
                    if (union.getCharid() == id) {
                        union.setLevel(lv);
                        union.setJob(job);
                        union.setUnk1(unk1);
                        union.setUnk2(unk2);
                        union.setPosition(pos);
                        union.setUnk3(unk3);
                        union.setName(name);
                        if (priset == 0) {
                            union.setPriset(pos);
                            union.setPos(unk2);
                        }
                        else if (priset == 1) {
                            union.setPriset1(pos);
                            union.setPos1(unk2);
                        }
                        else if (priset == 2) {
                            union.setPriset2(pos);
                            union.setPos2(unk2);
                        }
                        else if (priset == 3) {
                            union.setPriset3(pos);
                            union.setPos3(unk2);
                        }
                        else {
                            if (priset != 4) {
                                continue;
                            }
                            union.setPriset4(pos);
                            union.setPos4(unk2);
                        }
                    }
                }
            }
            for (final MapleUnion union2 : c.getPlayer().getUnions().getUnions()) {
                if (union2.getPosition() != -1 && !names.contains(union2.getName())) {
                    union2.setPosition(-1);
                }
            }
            c.getSession().writeAndFlush((Object)CWvsContext.setUnion(c));
            c.send(CWvsContext.enableActions(c.getPlayer()));
            c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        UnionHandler.groupIndex = new ArrayList<Integer>();
        UnionHandler.boardPos = new ArrayList<Point>();
        UnionHandler.openLevels = new ArrayList<Integer>();
        UnionHandler.cardSkills = new HashMap<Integer, Integer>();
        UnionHandler.characterSizes = new HashMap<Integer, Map<Integer, List<Point>>>();
        UnionHandler.skills = new ArrayList<Integer>();
    }
}
