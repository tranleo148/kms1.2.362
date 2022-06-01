importPackage(Packages.tools.packet);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    mobx = [-426, -234, 5, 214, 449, 642, 881, 1126]
    moby = 188
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() <= 262030200) {
            var em = cm.getEventManager("Normal_Hillah");
        } else {
            var em = cm.getEventManager("Hard_Hillah");
        }
        var eim = cm.getPlayer().getEventInstance();
        if (em == null || eim == null) {
            cm.mapMessage(5, "정상적으로 입장을 하지 않아 포탈이 정상 작동하지 않습니다. 운영자께 문의해 주세요.");
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getMapId() == 262030100 || cm.getPlayer().getMapId() == 262031100) {
            if (eim.getProperty("stage") == "0") {
                if (cm.getMonsterCount(cm.getPlayer().getMapId()) == 0) {
                    eim.setProperty("stage", "1");
                    cm.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("블러드투스가 우리의 침입을 눈치챘습니다!!! 블러드투스를 물리치세요."));
                    if (cm.getPlayer().getMapId() == 262030100) {
                        bloodid = 8870003;
                    } else {
                        bloodid = 8870103;
                    }
                    cm.spawnMob(bloodid, 781, 188);
                    cm.spawnMob(bloodid, 781, 188);
                    cm.spawnMob(bloodid, 781, 188);
                    cm.dispose();
                } else {
                    cm.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("힐라가 있는 탑 최상층으로 가기 위해서는 스켈레톤 나이트들을 모두 물리쳐야 합니다."));
                    cm.dispose();
                }
            } else if (eim.getProperty("stage") == "1") {
                if (cm.getMonsterCount(cm.getPlayer().getMapId()) == 0) {
                    cm.warpParty(Number(cm.getPlayer().getMapId()) + 100);
                    eim.setProperty("stage", "2");
                    cm.dispose();
                } else {
                    cm.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("블러드투스의 방해로 다음 장소로 이동할 수 없습니다."));
                    cm.dispose();
                }
            }
        } else {
            if (eim.getProperty("stage") == "2") {
                if (cm.getMonsterCount(cm.getPlayer().getMapId()) == 0) {
                    cm.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("블러드투스가 우리의 침입을 눈치챘습니다!!! 블러드투스를 물리치세요."));
                    eim.setProperty("stage", "3");
                    if (cm.getPlayer().getMapId() == 262030200) {
                        bloodid = 8870004;
                    } else {
                        bloodid = 8870104;
                    }
                    cm.spawnMob(bloodid, 781, 188);
                    cm.spawnMob(bloodid, 781, 188);
                    cm.spawnMob(bloodid, 781, 188);
                    cm.dispose();
                } else {
                    cm.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("힐라가 있는 탑 최상층으로 가기 위해서는 스켈레톤 나이트들을 모두 물리쳐야 합니다."));
                    cm.dispose();
                }
            } else if (eim.getProperty("stage") =="3") {
                if (cm.getMonsterCount(cm.getPlayer().getMapId()) == 0) {
                    cm.warpParty(Number(cm.getPlayer().getMapId()) + 100);
                    cm.dispose();
                } else {
                    cm.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("블러드투스의 방해로 다음 장소로 이동할 수 없습니다."));
                    cm.dispose();
                }
            }
        }
    }
}