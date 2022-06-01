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
		talk +="차원의 균열에서 나가시겠습니까?\r\n";
		if (cm.getPlayer().getMapId() == 922010800) {
		talk +="#L1##b발판 인형을 받는다.#k\r\n";
		}
		talk +="#L0#이곳에서 나가고 싶습니다.#k\r\n";
		cm.sendSimple(talk);
    } else {
		if(St == 1){
		seld = S;
			if(seld == 0){
				cm.getPlayer().getMap().setRPTicket(0);
				cm.gainItem(4001022, -cm.itemQuantity(4001022));
				cm.gainItem(4001454, -cm.itemQuantity(4001454));
				cm.warp(221023300);
				cm.dispose();
				return;
			} else {
				if(cm.canHold(4001454, 1)){
				cm.sendOk("발판 인형을 지급해 드렸습니다.");
				cm.gainItem(4001454, 1);
				cm.dispose();
				} else {
					cm.sendOk("인벤토리 창이 부족합니다.");
					cm.dispose();
				}
			}
		}
	}
}