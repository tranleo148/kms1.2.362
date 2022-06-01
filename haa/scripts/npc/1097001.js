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
            var msg = "";
            if (cm.getPlayer().getMapId() == 912080100) {
                msg += "요리를 그만 만들고 나가겠나? 요리를 완성하지 못하면 보상은 받을 수 없다네. 그래도 상관 없다면 내보내 주지.";
            } else if (cm.getPlayer().getMapId() == 912080110) {
                msg += "자네 덕분에 맛있는 요리가 만들어졌군! 맛있는 요리를 만든사람에게는 더 많은 경험치 보상을 지급하지! 물론 요리사 자격증도 더 주고 말이야! 정말 고맙네! 그럼 보상을 받고 나가겠나?";
            } else {
                msg += "음... 자네의 요리는 솔직히 썩 훌륭하지 못하군. 잘못된 재료를 넣은 것이 아닌가? 요리에 맞는 재료를 넣어야 맛있는 요리가 되는 법인데... 뭐, 하지만 계속 도전해 보면 실력이 늘겠지. 자, 그럼 보상을 받고 나가게.";
            }
            cm.sendYesNo(msg);
	} else if (status == 1) {
            if (cm.getPlayer().getMapId() == 912080110) {
                cm.gainItem(4033668, 2);
            } else if (cm.getPlayer().getMapId() == 912080120) {
                cm.gainItem(4033668, 1);
            }
            cm.warp(912080000, 0);
            cm.dispose();
	}
}