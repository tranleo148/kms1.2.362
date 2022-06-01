importPackage(Packages.handler.channel);

var enter = "\r\n";
var seld = -1;

var mobid = [9833971, 9020107, 8880181, 8880183, 8880184, 8880185, 9305600, 9500149, 9500150, 9500147, 8210002, 9010180, 9440025, 8120105, 9300075, 9300076, 9500380, 9833556, 9300851, 9832021, 9833495, 9500136, 8144008, 8144005, 8210004, 8210003, 7130102, 9309083, 9833493, 9300136, 9410004, 8210000, 8644018, 8644019, 8644017, 9306007, 9306000, 8880405, 8880406, 8880407, 8880425, 9010129, 9010135, 9010128, 9300019, 9500675, 9420002, 8230076, 9300009, 9460026, 9400050, 9833530, 9833568, 9500475, 8800002, 8644619, 8645040, 4110301, 9305653, 8800102, 8850011, 8820001, 8860000, 8840000, 8840014, 8870000, 8870100, 8920100, 8920002, 8930100, 8930000, 8641010, 2600105, 9309207, 8910100, 8910000, 8500011, 8500012, 8500021, 8500022, 9300035, 8880002, 8810018, 8810122, 8880000, 8900100, 8900000, 8920000, 8950100, 8950101, 8950102, 8250031, 8250032, 8250033, 8250034, 8644821, 9803055, 8240124, 8240059, 8950000, 8950001, 8950002, 8880110, 8880111, 8880100, 8880101, 8880140, 8880150, 8880141, 8880151, 8880153, 8880343, 8880344, 8880340, 8880341, 8880342, 8800200, 8880303, 8880304, 8880300, 8880301, 8880302, 9101078, 8881000, 8645009, 8644630, 9303131, 8820200, 8220022, 8220023, 8220024, 8220026, 8220025, 9833400, 9300454, 9300012, 8644650, 8644655, 8644612, 9500360, 6500001, 8880413, 8880414, 8880415, 8880500, 8880501, 8880200, 8880505, 8880502, 8880503, 8880504, 8880600, 8880602, 8144000, 8144001, 8144002, 8144003, 8144004, 8144006, 8144007, 9833802, 8145001, 8145002, 8145003, 8145004, 8145005, 9833803, 9833804, 9833805, 9300800];

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
		// var msg="";
		// for(var i = 0; i<mobid.length; i++){
		// 	msg+= (i+1)+" #o"+mobid[i]+"# "+mobid[i]+"\r\n";
		// }
		// cm.sendOk(msg);
if (cm.getPlayer().getMap().getNPCById(cm.getNpc()).getPosition().distanceSq(cm.getPlayer().getPosition()) > 7000) {
cm.sendOk("조사하기에는 너무 멀다.");
cm.dispose();
} else{
cm.sendOk("anj");
cm.dispose();
}
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