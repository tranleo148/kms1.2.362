package server;

import client.MapleClient;
import handling.channel.ChannelServer;
import java.awt.Point;
import scripting.PortalScriptManager;
import server.maps.MapleMap;
import tools.packet.CWvsContext;

public class MaplePortal {

    public static final int MAP_PORTAL = 2;

    public static final int DOOR_PORTAL = 6;

    private String name;

    private String target;

    private String scriptName;

    private Point position;

    private int targetmap;

    private int type;

    private int id;

    private boolean portalState = true;

    public MaplePortal(int type) {
        this.type = type;
    }

    public final int getId() {
        return this.id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final String getName() {
        return this.name;
    }

    public final Point getPosition() {
        return this.position;
    }

    public final String getTarget() {
        return this.target;
    }

    public final int getTargetMapId() {
        return this.targetmap;
    }

    public final int getType() {
        return this.type;
    }

    public final String getScriptName() {
        return this.scriptName;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final void setPosition(Point position) {
        this.position = position;
    }

    public final void setTarget(String target) {
        this.target = target;
    }

    public final void setTargetMapId(int targetmapid) {
        this.targetmap = targetmapid;
    }

    public final void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public final void enterPortal(MapleClient c) {
        MapleMap currentmap = c.getPlayer().getMap();
        if (this.portalState || c.getPlayer().isGM()) {
            if (getScriptName() != null) {
                c.getPlayer().checkFollow();
                try {
                    PortalScriptManager.getInstance().executePortalScript(this, c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (getTargetMapId() != 999999999) {
                MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(getTargetMapId());
                if (to == null) {
                    c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                if (!c.getPlayer().isGM()
                        && to.getLevelLimit() > 0 && to.getLevelLimit() > c.getPlayer().getLevel()) {
                    c.getPlayer().dropMessage(-1, "You are too low of a level to enter this place.");
                    c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
                MaplePortal pt = (to.getPortal(getTarget()) == null) ? to.getPortal(0) : to.getPortal(getTarget());
                c.getPlayer().changeMapPortal(to, pt);
            }
        }
        if (c != null && c.getPlayer() != null && c.getPlayer().getMap() == currentmap) {
            c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        }
    }

    public boolean getPortalState() {
        return this.portalState;
    }

    public void setPortalState(boolean ps) {
        this.portalState = ps;
    }
}
