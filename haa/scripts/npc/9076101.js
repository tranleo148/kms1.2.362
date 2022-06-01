function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	} 
	if (status == 0) {
	cm.sendOk("월묘와 떡 파티퀘스트를 클리어 하셨습니다. 경험치 보상을 받고 대기실로 이동합니다.");
            if (cm.getPlayer().getLevel() < 260) {
                for(var i = 0; i<2; i++){
                    cm.getPlayer().gainExp(cm.getPlayer().getNeededExp() - cm.getPlayer().getExp(), false, false, false);
                }
            } else if (cm.getPlayer().getLevel() > 260 && cm.getPlayer().getLevel() < 270) {
                    cm.getPlayer().gainExp(cm.getPlayer().getNeededExp() - cm.getPlayer().getExp(), false, false, false);
            } else if( cm.getPlayer().getLevel() >= 270 && cm.getPlayer().getLevel() < 280){
                  cm.getPlayer().gainExp((cm.getPlayer().getNeededExp() - cm.getPlayer().getExp()) / 2, false, false, false);
            } else if( cm.getPlayer().getLevel() >= 280 && cm.getPlayer().getLevel() < 290){
                 cm.getPlayer().gainExp((cm.getPlayer().getNeededExp() - cm.getPlayer().getExp()) / 5, false, false, false);
            } else if( cm.getPlayer().getLevel() >= 290 && cm.getPlayer().getLevel() < 300){
                 cm.getPlayer().gainExp((cm.getPlayer().getNeededExp() - cm.getPlayer().getExp()) / 20, false, false, false);
            }
	cm.warp(933000000);
	cm.dispose();
	}
}