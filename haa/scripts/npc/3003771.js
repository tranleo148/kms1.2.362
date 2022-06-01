importPackage(Packages.tools.packet);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_JinHillah", 1, 450010500, 255],
        ["Normal_JinHillah", 1, 450010500, 255]
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	cm.getPlayer().getMap().broadcastMessage(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
        cm.getPlayer().getMap().broadcastMessage(SLFCGPacket.MakeBlind(1, 0xff, 0x0, 0x0, 0x0, 0, 0));
        cm.getPlayer().getMap().broadcastMessage(CField.musicChange("Bgm00.img/Silence"));
        statusplus(100);

        Packages.server.Timer.MapTimer.getInstance().schedule(function () {
	cm.getPlayer().getMap().broadcastMessage(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/effect/BM2_hillahAppear_spine/hillah", "animation", 0, true, "intro"));
	cm.getPlayer().getMap().broadcastMessage(CField.playSound("Sound/SoundEff.img/BM2/hillahappear"));
	statusplus(13000);
        }, 500);

        Packages.server.Timer.MapTimer.getInstance().schedule(function () {
	cm.getPlayer().getMap().broadcastMessage(CField.showBlackOutScreen(1000, "BlackOut", "Map/Effect2.img/BlackOut", 13, 4, -1));
	statusplus(1000);
        }, 14000);

        Packages.server.Timer.MapTimer.getInstance().schedule(function () {
	cm.getPlayer().getMap().broadcastMessage(CField.removeIntro("intro", 100));
	cm.getPlayer().getMap().broadcastMessage(CField.removeBlackOutScreen("BlackOut", 100));
        }, 15000);

	if (cm.getPlayer().getMapId() == 450010400) {
	    cm.addBoss(setting[0][0]);
	    em = cm.getEventManager(setting[0][0]);
	    if (em != null) {
	        cm.getEventManager(setting[0][0]).startInstance_Party(setting[0][2] + "", cm.getPlayer());
	    }
	} else {
	    cm.addBossPractice(setting[1][0]);
	    em = cm.getEventManager(setting[1][0]);
	    if (em != null) {
	        cm.getEventManager(setting[1][0]).startInstance_Party(setting[1][2] + "", cm.getPlayer());
	    }
	}
        cm.dispose();
    }

}

function statusplus(time) {
    cm.getPlayer().getMap().broadcastMessage(SLFCGPacket.InGameDirectionEvent("", 1, time));
}