/*
    
    엔피시 이름 : 메이플 운영자

    엔피시 설명 : 블랙 큐브 조각

*/

var status;
var select;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    return;
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var text = "#b#t2431894# #c2431894#개#k를 가지고 있습니다. 고급 장비강화 주문서는 #r5개#k, 퍼펙트 각인의 인장은 #r15개#k, 스페셜 에디셔널 잠재능력 부여 주문서는 #r30개#k를 사용하여 교환할 수 있습니다.";
                text += "\r\n\r\n#b#L0##i2049300# #z2049300##l";
                text += "\r\n#b#L1##i2049512# #z2049512##l";
                text += "\r\n#b#L2##i2048307# #z2048307##l";
            cm.sendSimple(text);
        } else if(status == 1) {
            if (selection == 0) {
                if (cm.haveItem(2431486, 5)) {
		    cm.sendYesNo("정말 #b#i2049300# #t2049300##k 로 바꾸시겠습니까?");
		    select = 0;
                } else {
                    cm.sendNext("죄송하지만 #i2431486# #b#z2431486##k 가 충분하지 않으신 것 같네요.");
		    cm.dispose();
                }
	    } else if (selection == 1) {
                if (cm.haveItem(2431486, 15)) {
		    cm.sendYesNo("정말 #b#i2049512# #t2049512##k 로 바꾸시겠습니까?");
		    select = 1;
		} else {
                    cm.sendNext("죄송하지만 #i2431486# #b#z2431486##k 가 충분하지 않으신 것 같네요.");
		    cm.dispose();
		}        
	    } else if (selection == 2) {
                if (cm.haveItem(2431486, 30)) {
		    cm.sendYesNo("정말 #b#i2048307# #t2048307##k 로 바꾸시겠습니까?");
		    select = 2;
		} else {
                    cm.sendNext("죄송하지만 #i2431486# #b#z2431486##k 가 충분하지 않으신 것 같네요.");
		    cm.dispose();
		}        
            } else {
                cm.dispose();
            }
	} else if (status == 2) {
            if (select == 0) {
                if (cm.canHold(2049300)) {
                    cm.gainItem(2431486, -5);
                    cm.gainItem(2049300, 1);
		    cm.sendOk("교환이 완료되었습니다.");
                } else {
                    cm.sendNext("죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b소비#k탭의 인벤토리 공간을 비워주세요.");
                }
                cm.dispose();
	    } else if (select == 1) {
		if (cm.canHold(2049512)) {
                    cm.gainItem(2431486, -15);
                    cm.gainItem(2049512, 1);
		    cm.sendOk("교환이 완료되었습니다.");
                } else {
                    cm.sendNext("죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b소비#k탭의 인벤토리 공간을 비워주세요.");
                }
                cm.dispose();     
	    } else if (select == 2) {
		if (cm.canHold(2048307)) {
                    cm.gainItem(2431486, -30);
                    cm.gainItem(2048307, 1);
		    cm.sendOk("교환이 완료되었습니다.");
                } else {
                    cm.sendNext("죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b소비#k탭의 인벤토리 공간을 비워주세요.");
                }
                cm.dispose();      
            } else {
                cm.dispose();
            }
        } else { 
            cm.dispose();
        }
    }
}