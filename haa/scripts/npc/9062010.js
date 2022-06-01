var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
        status--;
    if (status == 0) {
        cm.sendOk("안녕하세요! 당신의 이름을 바꿔드릴 수 있는 #b미스터 뉴네임#k입니다. 무엇을 도와드릴까요?\r\n\r\n#b#L0# 캐릭터 이름 변경하기\r\n#L1# 대화를 종료한다.");
    } else if (status == 1) {
	if (selection == 0) {
	    cm.sendNameChangeWindow();
            cm.dispose();
	} else if (selection == 1) {
            cm.dispose();
	}
    }
}