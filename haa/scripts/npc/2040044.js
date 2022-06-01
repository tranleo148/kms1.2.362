var St = -1;
var seld = -1;
var icon = "#fUI/UIWindow2.img/QuestIcon/3/0#"

//루피 파퀘 퇴장
function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M == -1) {
		cm.dispose();
		return;
	}
	if (M == 0) {
        St--;
		cm.dispose();
		return;
    }
	if(M == 1){
	    St++;
	}
	if(St == 0) {
		var talk = "";
		if(cm.getClient().getChannelServer().getMapFactory().getMap(cm.getMapId()).getNumMonsters() != 0) {
			talk +="알리샤르를 물리쳐주세요!!";
		} else {
			talk +="알리샤르를 물리쳐주셧군요!! 고맙습니다!"
		}
		cm.sendNext(talk);
    } else {
		if(St == 1){
			if(cm.getClient().getChannelServer().getMapFactory().getMap(cm.getMapId()).getNumMonsters() == 0){
				cm.warp(221023300);
                                                   cm.gainItem(2437558, 1);
                                                   cm.gainItem(4033235, 10);
				cm.dispose();
			} else {
				cm.dispose();
			}
			
			
		}
	}
}