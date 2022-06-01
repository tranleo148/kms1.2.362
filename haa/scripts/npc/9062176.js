importPackage(Packages.handler.channel);

var enter = "\r\n";
var seld = -1;


var topic;
var question;
var answer;
var reward, qty;

var map;

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
		var msg = "요~ 여기서는 문제를 맞추면 소정의 상품을 얻을수 있어! 어때 나의 문제를 풀어볼래?\r\n";
		if (cm.getPlayer().isGM()) {
			map = cm.getPlayer().getMap();
			if (!cm.onQuiz(map)) {
				msg += "#L1#이 맵에 깜짝퀴즈를 내겠습니다.\r\n"
			} else {
				msg += "#L2#문제를 거두겠습니다.\r\n";
			}
		}
			msg += "#L3#퇴장한다";
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				var msg = "주제를 적어주세요.";
				cm.sendGetText(msg);
			break;
			case 2:
				cm.sendYesNo("정말 문제를 그만두시겠습니까?");
			break;
			case 3:
				cm.warp(100000000);
				cm.dispose();
			break;
		}
	} else if (status == 2) {
		switch (seld) {
			case 1:
				topic = cm.getText();
				var msg = "현재 입력된 주제 : #b"+topic+"#k"+enter;
				msg += "문제를 적어주세요.";
				cm.sendGetText(msg);
			break;
			case 2:
				cm.doneQuiz(map);
				cm.sendOk("완료되었습니다.");
				cm.dispose();
			break;
		}
	} else if (status == 3) {
		question = cm.getText();
		var msg = "현재 맵 : #b" + map.getStreetName() + " - " + map.getMapName() + "#fs11#("+map.getId()+")#k"+enter;
		msg += "현재 입력된 주제 : #b"+topic+"#k"+enter;
		msg +=  "현재 입력된 문제 : #b"+question+"#k"+enter;
		msg += "정답을 입력해주세요.";
		cm.sendGetText(msg);
	} else if (status == 4) {
		answer = cm.getText();
		var msg = "현재 맵 : #b" + map.getStreetName() + " - " + map.getMapName() + "#fs11#("+map.getId()+")#k"+enter;
		msg += "현재 입력된 주제 : #b"+topic+"#k"+enter;
		msg += "현재 입력된 문제 : #b"+question+"#k"+enter;
		msg += "현재 입력된 정답 : #b"+answer+"#k"+enter;
		msg += "보상을 검색해주세요. (검색캐시 같은 개념)";
		cm.sendGetText(msg);
	} else if (status == 5) {
		var msg = "현재 맵 : #b" + map.getStreetName() + " - " + map.getMapName() + "#fs11#("+map.getId()+")#k"+enter;
		msg += "현재 입력된 주제 : #b"+topic+"#k"+enter;
		msg +=  "현재 입력된 문제 : #b"+question+"#k"+enter;
		msg += "현재 입력된 정답 : #b"+answer+"#k"+enter;
		msg += "보상을 선택해주세요."+enter;
		msg += cm.searchItem2(cm.getText());
		cm.sendSimple(msg);
	} else if (status == 6) {
		reward = sel;
		var msg = "현재 맵 : #b" + map.getStreetName() + " - " + map.getMapName() + "#fs11#("+map.getId()+")#k"+enter;

		msg += "현재 입력된 주제 : #b"+topic+"#k"+enter;
		msg +=  "현재 입력된 문제 : #b"+question+"#k"+enter;
		msg += "현재 입력된 정답 : #b"+answer+"#k"+enter;
		msg += "현재 선택된 보상 : #b#i"+reward+"##z"+reward+"##k"+enter;
		msg += "보상을 몇 개 주실지 입력해주세요.";
		cm.sendGetNumber(msg, 1, 1, 30000);
	} else if (status == 7) {
		qty = sel;
		var msg = "현재 맵 : #b" + map.getStreetName() + " - " + map.getMapName() + "#fs11#("+map.getId()+")#k"+enter;
		msg += "현재 입력된 주제 : #b"+topic+"#k"+enter;
		msg +=  "현재 입력된 문제 : #b"+question+"#k"+enter;
		msg += "현재 입력된 정답 : #b"+answer+"#k"+enter;
		msg += "현재 선택된 보상 : #b#i"+reward+"##z"+reward+"##k"+enter;
		msg += "현재 입력된 보상 개수 : #b"+qty+"#k"+enter+enter;
		msg += "#b이대로 이 맵에 문제를 내시려면 '예'를 눌러주세요.";
		cm.sendYesNo(msg);
	} else if (status == 8) {
		cm.doQuiz(map, topic, question, answer, reward, qty);
		cm.dispose();
	}
}