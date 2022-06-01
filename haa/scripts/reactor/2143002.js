


/*

	히나 온라인 소스 팩의 스크립트 입니다.

        제작 : 티썬

	엔피시아이디 : 
	
	엔피시 이름 :

	엔피시가 있는 맵 : 

	엔피시 설명 : 


*/


var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    
    if (status == 0) {
        if (cm.getQuestStatus(31251) == 1) {
           cm.gainItem(4030020,1);    
           cm.sendOk("쾌스트창 확인해주세요");
            cm.dispose();
        }else if (cm.getQuestStatus(31254) == 1) {
           cm.gainItem(4030021,1);    
           cm.sendOk("쾌스트창 확인해주세요");
            cm.dispose();
        } else {
            cm.sendOk("이미 완료");
            cm.dispose();
        }
    }
}


