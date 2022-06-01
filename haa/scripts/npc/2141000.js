var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    name = ["Normal_Pinkbean", "Chaos_Pinkbean"];
    mobid = [8820002, 8820003, 8820004, 8820005, 8820006, 8820008, 8820014]
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
   if (!cm.isLeader()) {
      cm.sendOk("파티장만 말 걸 수 있어요");
      cm.dispose();
      return;
   }
        cm.askAcceptDecline("여신의 거울만 있으면... 다시 검은 마법사를 불러낼 수 있어!...\r\n"
        +"이, 이상해... 왜 검은 마법사를 불러내지 않는 거지? 이 기운은 뭐지? 검은 마법사와는 전혀 다른... 크아아악!\r\n\r\n"
        +"#b(키르스턴의 어깨에 손을 댄다.)");
    } else if (status == 1) {
        var map = cm.getPlayer().getMapId()%10000;
        var check = map == 100 ? 0 : 1;
        var em = cm.getEventManager(name[check]);
        var eim = cm.getPlayer().getEventInstance();
        if (em == null || eim == null) {
            cm.getPlayer().dropMessage(5, "비정상적인 값이 발견되어 보스 인스턴스가 종료됩니다.");
            cm.warp(993080000);
            cm.dispose();
        }
        cm.getPlayer().getMap().killAllMonsters(false);
        for (i = 0; i < 7; i++) {
            var mobidss = mobid[i] + (check == 1 ? 100 : 0);
            mob = em.getMonster(mobidss);
            eim.registerMonster(mob);
            eim.getMapInstance(0).spawnMonsterWithEffectBelow(mob, new java.awt.Point(5, -42), -2);
            if (mob.getId() == 8820008 || mob.getId() == 8820108) {
                eim.getMapInstance(0).killMonster(mob);
            }
        }
        cm.getPlayer().getMap().resetNPCs();
        cm.dispose();
        return;
    }
}