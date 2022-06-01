importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.io);

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
		if(cm.getPlayer().getEventInstance().getProperty("stage1r") == "true"){
			cm.sendOk("다음 스테이지로 이동해주세요.");
			cm.dispose();
			return;
		}
		if(cm.getPlayer().getMap().getRPTicket() != 20){
			talk = "첫번째 스테이지에 대해 설명해 드리겠습니다. 맵 곳곳에는\r\n";
			talk +="여러 종류의 몬스터가 있습니다. 몬스터들은 차원의 공간으로 갈 수 있는 #b차원의 통행증#k이라는 물건을 지니고 있습니다.\r\n";
			talk +="그것만 있다면 에오스 탑 꼭대기에 다른 차원의 문을 열고 자신의 부하들을 이곳으로 소환하고 있는 녀석에게 다다를 수 있겠죠.\r\n";
			talk +="몬스터를 쓰더뜨리고 #b차원의 통행증 20장#k을 모은 후 저에게 말을 걸어 주시면 다음 스테이지로 갈 수 있습니다. \r\n그럼 힘내 주세요!\r\n";
		} else {
			talk +="#b차원의 통행증 20장#k을 모아오셧군요! 다음 스테이지로 이동해주세요.\r\n";
		}
		cm.sendNext(talk);
    } else {
		if(St == 1){
			if(cm.getPlayer().getMap().getRPTicket() != 20 && cm.getPlayer().isGM()){
				cm.getPlayer().getMap().setRPTicket(20);
				cm.dispose();
			} else if(cm.getPlayer().getMap().getRPTicket() != 20){
				cm.dispose();
			} else {
				cm.getPlayer().getMap().setRPTicket(0);
				cm.getPlayer().getEventInstance().setProperty("stage1r", "true");
				cm.environmentChange(true, "gate");
				cm.dispose();
			}
		}
	}
}