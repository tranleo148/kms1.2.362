importPackage(Packages.tools.packet);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
//        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 0xff, 0xf0, 0xf0, 0xf0, 0, 0));
        statusplus(500);
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("Map/Effect3.img/BossLucid/Lucid5", 0x02, 0, 0x59, 0x24, 0x1, 0x1, 0, 0x1, 0));
    } else if (status == 1) {
        cm.sendNextS("#face6#어머, 이를 어째? 꿈이 무너지나봐요~!", 37, 0, 3003250);
    } else if (status == 2) {
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1500, 0));
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/ArcaneRiver/phase2"));
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("Map/Effect3.img/BossLucid/Lucid2", 0x02, 0, 0x59, 0x24, 0xA, 0x1, 0, 0x1, 0));
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("Map/Effect3.img/BossLucid/Lucid3", 0x02, 0, -140, 0x24, 0xB, 0x1, 0, 0x1, 0));
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("Map/Effect3.img/BossLucid/Lucid4", 0x02, 0, 0x59, 0x24, 0x1, 0x1, 0, 0x1, 0));
        statusplus(2000);
    } else if (status == 3) {
        statusplus(1600);
    } else if (status == 4) {
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        cm.dispose();
        return;
    }

}

function statusplus(time) {
    cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, time));
}