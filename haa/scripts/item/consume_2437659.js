var status = -1;

function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
       status --;
    }
    if (mode == 1) {
        status++;
    }



    if (status == 0) {
	cm.sendYesNo("#fs11#안녕하세요? 슬롯제를 구매한 당신에게만 특별한 사냥터로 이동시켜드리는 #b프리미엄 혜택#k 입니다. 티어사냥터보다 #b높은 경험치#k 와 #b특별한 아이템#k들을 휙득 하실 수 있습니다. 지금 바로 이동하시겠어요?");
    } else if (status == 1) {
	cm.dispose();
	cm.warp(261020700);
    }
}