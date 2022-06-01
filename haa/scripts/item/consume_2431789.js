importPackage(java.lang);
importPackage(Packages.handling.world);
importPackage(Packages.packet.creators);
var status;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
        if (mode == 1)
            status++;
        else	
            status--;
        if (status == 0) {
	var chat = "어떤 스킬을 마스터 해볼까?\r\n\r\n";
	chat += cm.마스터리북();
 	chat += "\r\n";
	chat += "#L0##r#fn돋움##fs14##e마스터리 북 사용을 취소한다.#n#fs##fn##l";
	cm.sendSimpleS(chat,2);
	} else if (status == 1) {
	if (selection != -1 && selection != 0) {
	    cm.teachSkill(selection,cm.getPlayer().getSkillLevel(selection),20);
	    cm.sendOkS("이걸로 나도 좀더 강해진걸까?",2);
	    cm.gainItem(2431789,-1);
	}
	cm.dispose();
	} else if (status < 0) {
	cm.dispose();
    }
}