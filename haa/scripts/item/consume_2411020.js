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
		cm.sendNext("원하시는 기능을 선택해 주세요.\r\n\r\n#L1##b 현재 적용 중인 데미지 스킨을 저장한다.#k\r\n   - 현재 적용되어 있는 스킨은 #b#t" + cm.getKeyValue(7293, "damage_skin") + "# 입니다.#k#l\r\n\r\n#L2##b 저장되어 있는 데미지 스킨을 적용한다.#k\r\n   - 현재 " + (cm.getKeyValue(7294, "save_damage_skin") == -1 ? "저장된 데미지 스킨이 없습니다." : "저장되어 있는 스킨은 #b#t" + cm.getKeyValue(7294, "save_damage_skin") + "# 입니다.#k#l"));
    } else if (status == 1) {
		selsave = selection;
		if (selsave == 1) {
			cm.sendYesNo("현재 데미지 스킨을 저장하려 하시는군요!\r\n" + (cm.getKeyValue(7294, "save_damage_skin") != -1 ? "현재 저장된 #b#t" + cm.getKeyValue(7294, "save_damage_skin") + "##k 대신 " : "") + "#b#t" + cm.getKeyValue(7293, "damage_skin") + "##k을 저장시키시겠습니까?");
		} else if (selsave == 2) {
			if (cm.getKeyValue(7294, "save_damage_skin") == -1) {
				cm.sendOk("저장된 데미지 스킨이 없습니다.");
				cm.dispose();
			} else {
				cm.sendYesNo("저장된 데미지 스킨을 적용하려 하시는군요!\r\n현재 적용된 #b#t" + cm.getKeyValue(7293, "damage_skin") + "##k 대신 #b#t" + cm.getKeyValue(7294, "save_damage_skin") + "##k을 적용시키시겠습니까?");
			}
		}
    } else if (status == 2) {
		if (selsave == 1) {
			cm.setKeyValue(7294, "save_damage_skin", (cm.getKeyValue(7293, "damage_skin") + ""));
			cm.sendOk("#b#t" + cm.getKeyValue(7293, "damage_skin") + "##k이 저장되었습니다.");
			cm.dispose();
		} else if (selsave == 2) {
			if (cm.getKeyValue(7294, "save_damage_skin") != -1) {
				cm.setDamageSkin(cm.getKeyValue(7294, "save_damage_skin"));
				cm.sendOk("#b#t" + cm.getKeyValue(7294, "save_damage_skin") + "##k이 적용되었습니다.");
				cm.dispose();
			}
		}
		cm.dispose();
	}
}