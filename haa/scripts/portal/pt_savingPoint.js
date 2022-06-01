

function enter(pi) {
    var em = pi.getPlayer().getEventInstance();

    if (em != null) {
        var check = -1;
        var count = 0;
        for (var i = 0; i < 5; i++) {
            if (em.getProperty("ObjectId" + i) != null && em.getProperty("ObjectId" + i) != "") {
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SpawnPartner(false, parseInt(em.getProperty("ObjectId" + i)), 0));
                em.setProperty("ObjectId" + i, "");
                check = i;
                count++;
            }
        }
        if(count > 0 ){
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SpiritSavedEffect(count));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.playSE("Sound/MiniGame.img/Result_Yut")); 
        }
     
        var mob = pi.getPlayer().getMap().getMonsterById(8644301 + check);
        if (mob != null) {
            pi.getPlayer().getMap().killMonster(mob);
        }
        var point = parseInt(pi.getPlayer().getKeyValue(16215, "point"));
        switch (check) {
            case 0:
                pi.getPlayer().setKeyValue(16215, "point", "" + (point + 200));
                break;
            case 1:
                pi.getPlayer().setKeyValue(16215, "point", "" + (point + 500));
                break;
            case 2:
                pi.getPlayer().setKeyValue(16215, "point", "" + (point + 1000));
                break;
            case 3:
                pi.getPlayer().setKeyValue(16215, "point", "" + (point + 1500));
                break;
            case 4:
                pi.getPlayer().setKeyValue(16215, "point", "" + (point + 2500));
                break;
        }
    }
    return false;
}