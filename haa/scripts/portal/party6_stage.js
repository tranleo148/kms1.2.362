var stage2 = true;

var conn;

function enter(pi) {
    if(pi.getPlayer().getMapId() == 930000000){
        pi.warpParty(930000010);
        pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(0));
    } else if(pi.getPlayer().getMapId() == 930000010){
        pi.resetMap(930000100);
        pi.warpParty(930000100);
        pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(5));
    } else if(pi.getPlayer().getMapId() == 930000100){
        if(pi.getPlayer().getMap().getAllMonstersThreadsafe().size() != 0){
            pi.getPlayer().dropMessage(5, "필드의 몹을 전부 해치워야 이동 할 수 있습니다.");
        } else {
            pi.resetMap(930000200);
            pi.warpParty(930000200);
            stage2 = true;
            pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(15));
        }
    } else if(pi.getPlayer().getMapId() == 930000200){
        re = pi.getPlayer().getMap().getAllReactorsThreadsafe();
        if(stage2 == true){
            for(var i =0; i<2; i++){
                if(re[i].getReactorId() == 3009000 && re[i].getState() != 4){
                    pi.getPlayer().dropMessage(5, "가시덤불이 방해되어 지나갈 수 없습니다.");
                } else if(re[i].getReactorId() == 3009000 && re[i].getState() == 4) {
                    stage2 = false;
                }
            }
        } else {
            for (i = 0; i < pi.getPlayer().getParty().getMembers().size(); ++i) {
                mem = pi.getPlayer().getParty().getMembers().get(i).getName();
                conn = pi.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem);
                conn.removeItem(4001161, -conn.itemQuantity(4001161));
                conn.removeItem(4001162, -conn.itemQuantity(4001162));
            }
            pi.resetMap(930000300);
            pi.warpParty(930000300);
            pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(25));
        }
    }
    //pi.warp(pi.getPlayer().getMapId());
}