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
		if(cm.getPlayer().getEventInstance().getProperty("stage4r") == "true"){
			cm.sendOk("다음 스테이지로 이동해주세요.");
			cm.dispose();
			return;
		}
		var talk = "";
		if(cm.getPlayer().getMap().getRPTicket() >= 0){
			talk +="#b차원의 킹 블록골렘#k들을 모두 퇴치하셧군요 다음 스테이지로 이동해주세요!";
		} else {
			talk += "네번째 스테이지에 대해 설명해 드리겠습니다. \r\n";
			talk +="이곳에는 #b차원의 킹 블록골렘#k들이 넘어와 탑을 지키고 있습니다. 다음 스테이지로 이동하기 위해서 모두 퇴치해주세요.";
			talk +="그럼 힘내 주세요!\r\n";
		}
		cm.sendNext(talk);
    } else {
		if(St == 1){
			if(cm.getPlayer().getMap().getRPTicket() >= 0){
				cm.getPlayer().getEventInstance().setProperty("stage4r", "true");
				cm.environmentChange(true, "gate");
				cm.dispose();
			} else {
				cm.dispose();
			}
			
			
		}
	}
}