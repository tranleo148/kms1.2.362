var enter = "\r\n";
var seld = -1;

var pet = [5000930, 5000931, 5000932];

var hpet = [5000000];

var selpet;

var price = 0;

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
		var msg = "#fs11##b[선택형 루나 쁘띠 1기 펫 상자]#k를 사용하시겠습니까?  \r\n#b"+enter;
		msg += "#L1##b사용하기"+enter;

		cm.sendSimple(msg);
	} else if (status == 1) {
if (sel == 3) {
	cm.dispose();
	cm.openNpc(1540872);
return;
} else {
		selpet = sel == 1 ? pet : hpet;
		var msg = "#fs11#받고싶은 #b펫#k을 선택 해주세요"+enter;
		for (i = 0; i < selpet.length; i++)
			msg += "#L"+i+"##i"+selpet[i]+"##b#z"+selpet[i]+"##k"+enter;
		cm.sendSimple(msg);
}
	} else if (status == 2) {
		seld = sel;
		cm.sendYesNo("정말로 #i"+selpet[seld]+"##b#z"+selpet[seld]+"##k을(를) 받으시겠습니까?");
	} else if (status == 3) {
		Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), selpet[seld], 1, "", Packages.client.inventory.MaplePet.createPet(selpet[seld], -1), 30, "", false);
		cm.sendOk("교환이 완료되었습니다.");
		cm.gainItem(2630127, -1);
 		cm.dispose();
	}
}