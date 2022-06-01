/*
 * 프로젝트 : 1.2.214 SpiritStyle
 * Script Author : 하요(ifhayo)
 * 이 주석은 지우지 않아주셨으면 좋겠습니다.
 *
 */


importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);

var status = 0;
var itemid = new Array(1003864, 1102563, 1052613, 1012377, 1122253, 1132229)
var itemNed = 2028209

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
	if (status == 0) {
		cm.getPlayer().innerLevelUp();
		cm.gainItem(2432970,-1);
		cm.sendOk("성공적으로 #b이너 어빌리티#k를 개방 하였습니다.");
		cm.dispose();
	}
}
}