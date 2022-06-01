package server.field.boss.will;

import client.MapleClient;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.Pair;
import tools.packet.MobPacket;

public class SpiderWeb // mush
extends MapleMapObject {
    private int pattern;
    private int x1;
    private int y1;
    private int num;
    private static List<Pair<Integer, Point>> spiderPoint;

    public static void load() {
        spiderPoint = new ArrayList<Pair<Integer, Point>>();
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(-683, 395)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-701, 182)));
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(702, -280)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-711, -254)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(718, 432)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(712, 310)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-577, -298)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(552, 459)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(531, -268)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(699, -82)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-594, 251)));
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(378, 480)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(577, 345)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-506, 432)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-733, -122)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-626, -179)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(604, -153)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-405, 484)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(736, 56)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-749, 17)));
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(-366, -325)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(391, -307)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-197, -300)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(458, -163)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-282, 488)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(80, 482)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-485, -148)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-606, -75)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(772, 169)));
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(-84, 481)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-650, 45)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(558, -58)));
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(164, -308)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-61, -275)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(210, 450)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-555, 151)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-520, 331)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-420, 373)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-280, 365)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-500, 15)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-355, -45)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-270, 65)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-344, -140)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-220, -200)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-230, -65)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-90, -153)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(50, -155)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(200, -120)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(320, -170)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(395, -45)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(275, -20)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(130, 0)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(622, 64)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(497, 44)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(651, 228)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(563, 188)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(460, 290)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(470, 165)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(382, 80)));
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(-388, 200)));
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(-84, 48)));
        spiderPoint.add(new Pair<Integer, Point>(2, new Point(250, 230)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(-168, 245)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(-5, 370)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(85, 150)));
        spiderPoint.add(new Pair<Integer, Point>(0, new Point(110, 345)));
        spiderPoint.add(new Pair<Integer, Point>(1, new Point(10, 240)));
    }

    public SpiderWeb(int num) {
        this.num = num;
        this.pattern = spiderPoint.get(num).getLeft();
        this.x1 = SpiderWeb.spiderPoint.get((int)num).getRight().x;
        this.y1 = SpiderWeb.spiderPoint.get((int)num).getRight().y;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPattern() {
        return this.pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public int getX1() {
        return this.x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return this.y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public MapleMapObjectType getType() {
        return MapleMapObjectType.WEB;
    }

    public void sendSpawnData(MapleClient client) {
        client.getSession().writeAndFlush((Object)MobPacket.BossWill.willSpider(true, this));
    }

    public void sendDestroyData(MapleClient client) {
        client.getSession().writeAndFlush((Object)MobPacket.BossWill.willSpider(false, this));
    }
}

