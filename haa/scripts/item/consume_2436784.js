var status = 0;

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
		cm.sendAcceptDecline("수락을 누르시면 데미지 스킨 저장 슬롯 1칸을 확장하실 수 있어요.\r\n\r\n#e지금 바로 데미지 스킨 저장 슬롯 1칸 확장권 1개를 사용하여 데미지 스킨 저장 슬롯 1칸을 확장 하시겠어요?#n\r\n\r\n#r(데미지 스킨 저장 슬롯은 최대 30개까지 이용할 수 있습니다.)#k");
    } else if (status == 1) {
		var haveitem = -1;
		if(!cm.haveItem(2436784, 1)) {
			cm.sendOk("errcode -31");
			cm.dispose();
			return;
		}
		if (cm.getKeyValue(13191, "skinroom") < 30) {
			cm.gainItem(2436784, -1);
			if (cm.getKeyValue(13191, "skinroom") == -1) {
				cm.setKeyValue(13191, "skinroom", "1");
				cm.sendNext("데미지 스킨 저장 슬롯이 1칸 확장되었습니다. 데미지 스킨 저장 정보는 #b[캐릭터정보]>[데미지스킨] 버튼#k을 통해 확인 하실 수 있습니다. 현재 보유하고 계신 데미지 스킨 슬롯은 #r1개#k 입니다.");
			} else {
				cm.setKeyValue(13191, "skinroom", (cm.getKeyValue(13191, "skinroom") + 1) + "");
				cm.sendOk("데미지 스킨 저장 슬롯이 1칸 확장되었습니다. 현재 보유하고 계신 데미지 스킨 슬롯은 #r" + cm.getKeyValue(13191, "skinroom") + "개#k 입니다.");
			}
			cm.getPlayer().updateDamageSkin();
		} else {
			cm.sendOk("데미지 스킨 저장 슬롯이 이미 최대입니다.");
		}
		cm.dispose();
    }
}