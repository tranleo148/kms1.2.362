box = 2633336;
var seld = -1;

function start() {
    St = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        St++;
    }
	if (St == 0) {
	  var text = "#fs11#교환하고싶은 #어센틱 심볼#k 을 선택해주세요. 동일한 아이템으로  5개가 지급됩니다.\r\n";
	  text += "#L0##b#i1713000##z1713000#\r\n";
	  text += "#L1##b#i1713001##z1713001#\r\n";
	  cm.sendYesNo(text);
	} else if (St == 1) {
		seld = selection;
		cm.sendGetNumber("사용하실 아이템 갯수를 선택해주세요.", 1, 1, 100);
	} else if (St == 2) {
		if (!cm.haveItem(2633336, selection)) {
			cm.sendOk("교환권이 부족합니다.");
			cm.dispose();
			return;
		}
		cm.gainItem(1713000+seld,5*selection);
		cm.gainItem(2633336,-1*selection);
		cm.sendOk("교환이 완료되었습니다.");
		cm.dispose();
	}
}