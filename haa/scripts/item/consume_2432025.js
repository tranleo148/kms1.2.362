var status = 0;
var selsave = -1;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 1) 
        status++;
    else 
        status--;
    if (status == 0) {
		cm.getPlayer().dropMessage(5, "새로운 스킬이 등록되었습니다.");
		cm.teachSkill(60001218, 1, 1);
		cm.teachSkill(60000219, 1, 1);
		cm.gainItem(2432025, -1);
		cm.dispose();
	}
}