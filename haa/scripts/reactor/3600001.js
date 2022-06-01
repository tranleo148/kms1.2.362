importPackage(Packages.tools.packet);

function act() {
    var em = rm.getPlayer().getEventInstance();
    if (em != null) {
        if (em.getProperty("ObjectId4") != null && em.getProperty("ObjectId4") != "") {
            rm.getPlayer().getMap().destroyReactor(rm.getReactor().getObjectId());
            return;
        }
        var check = 0;
        for (var i = 0; i < 5; i++) {
            if (em.getProperty("ObjectId" + i) == null || em.getProperty("ObjectId" + i) == "") {
                em.setProperty("ObjectId" + i, "" + 100000 + i);
                rm.getClient().getSession().writeAndFlush(SLFCGPacket.SpawnPartner(true, parseInt(em.getProperty("ObjectId" + i)), 80002310));
                rm.getPlayer().setKeyValue(16215, "chase", "" + (1 + i));
                check = i;
                break;
            }
        }
        switch (check) {
            case 0:
                rm.getClient().getSession().writeAndFlush(CField.startMapEffect("", 0, false));
                rm.getClient().getSession().writeAndFlush(CField.startMapEffect("맹독의 정령이 눈치챈 모양이야! 어서 도망가람!", 5120175, true));
                break;
            case 1:
            case 2:
            case 3:
                rm.getClient().getSession().writeAndFlush(CField.startMapEffect("", 0, false));
                rm.getClient().getSession().writeAndFlush(CField.startMapEffect("맹독의 정령이 더 강해지고 있담..!", 5120175, true));
                break;
            case 4:
                rm.getClient().getSession().writeAndFlush(CField.startMapEffect("", 0, false));
                rm.getClient().getSession().writeAndFlush(CField.startMapEffect("맹독의 정령이 완전체가 되었담! 조심해람!", 5120175, true));
                break;
        }
        var map = rm.getPlayer().getMap();
        var mob = map.getMonsterById(8644301 + (check == 0 ? 0 : (check - 1)));
        var mob1 = Packages.server.life.MapleLifeFactory.getMonster(8644301 + check);
        if (check != 0 && check < 5 && mob != null) {
            var pos = mob.getPosition();
            rm.getPlayer().getMap().killMonster(mob);
            map.spawnMonsterOnGroundBelow(mob1, pos);
        } else if (check == 0) {
            map.spawnMonsterOnGroundBelow(mob1, new java.awt.Point(-194, -1391));
        }
    }
    var pos = [
        [-1288, -775],
        [-1981, -1014],
        [-3245, -1615],
        [908, -774],
        [1719, -1015],
        [2899, -1617],
        [2887, -1375],
        [-3245, -1315],
        [113, -1431],
        [-466, -1438],
        [-2192, -536],
        [1908, -533]
    ];
    var mob2 = Packages.server.life.MapleLifeFactory.getMonster(8644201);
    var rd = Math.floor(Math.random() * pos.length);
    map.spawnMonsterOnGroundBelow(mob2, new java.awt.Point(pos[rd][0], pos[rd][1]));

    rm.getPlayer().getMap().destroyReactor(rm.getReactor().getObjectId());
}