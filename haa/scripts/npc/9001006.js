importPackage(Packages.client.items);

var item = [4000620]; // 퀘스트 재료 코드, 만약 아이템 종류를 늘리고 싶다면 [4000000, 40000001] 이런식으로 수정
var cost = [30000]; // 퀘스트 재료 개수, 위와 설명 같음
var gain = 4009703; // 퀘스트 보상 코드

// 여기서부터
var icon_start = "#fUI/UIWindow2/UtilDlgEx/list1#"
var icon_doing = "#fUI/UIWindow2/UtilDlgEx/list0#"
var icon_complete = "#fUI/UIWindow2/UtilDlgEx/list3#"
var icon_etc = "#fUI/UIWindow2/UtilDlgEx/list2#"
var icon_getitem = "#fUI/UIWindow2/QuestIcon/4/0#"
// 여기까지 퀘스트 아이콘, 건들지말 것

var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0 && status == 2) {
       cm.sendNext("#fs11#흥!..너가 내마음을 모르는구나!"); // 퀘스트 수락 여부 물을 때 거절할 시 메세지
       cm.dispose();
       return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
   // 여기서부터
   quest = cm.getQuestRecord(19021502);
   if (quest.getCustomData() == null) {
	quest.setStatus(0);
	quest.setCustomData("0");
   }
   // 여기까지 퀘스트 선언, 건들지말 것

    if (status == 0) {
	qstatus = quest.getStatus(); // 퀘스트 진행도
	for (i = 0; i < item.length; i++) {
		itemcheck = (cm.haveItem(item[i], cost[i]) && cm.itemQuantity(item[i]) >= cost[i]); // 퀘스트 재료 체크
	}
	var ms = ""
	ms += "#fs11#으앙 나도 날개가생기고싶다..\r\n이봐! 너가 나좀 도와줄래?\r\n\r\n" // 퀘스트 메세지
	if (qstatus != 2) {
		if (qstatus == 0) {
			ms += icon_start + "\r\n"
		} else if (qstatus == 1 && !itemcheck) {
			ms += icon_doing + "\r\n"
		} else if (qstatus == 1 && itemcheck) {
			ms += icon_complete + "\r\n"
		}
		ms += "#fs11##b#L0#푸치의 고민#l\r\n" // 퀘스트 제목 이름
		if (qstatus != 0) {
			ms += "\r\n\r\n"
			ms += icon_etc + "\r\n"
			ms += "#fs11##b#L1#퀘스트를 포기하겠습니다.#l\r\n" // 퀘스트 포기 이름
		}
		cm.sendNext(ms);
	} else {
		cm.sendNext("#fs11#이미 이 퀘스트를 수행하였습니다."); // 퀘스트 이미 완료 했을 시 메세지
		cm.dispose();
	}
    } else if (status == 1) {
	sel = selection;
	if (sel == 0) {
		if (qstatus == 0) {
			cm.sendNext("난말야. 곰이지만 날개가 생기고싶어!.. #b#z4000620##k #b3만개#k를 구해오면 #i4009703# #b 800개를 주도록할게."); // 퀘스트 수락할 시 메세지 첫번째
		} else if (qstatus == 1 && !itemcheck) {
			var ms = ""
			ms += "아직 재료가 부족해!\r\n\r\n"
			cm.sendNext(ms); // 퀘스트 진행 중일 시 메세지
			cm.dispose();
		} else if (qstatus == 1 && itemcheck) {
			cm.sendNext("헉! 재료를 모두구해온거야? 나의 애장품인 증표를 보상으로 줄게!\r\n"); // 퀘스트 완료할 시 메세지, 첫번째
		}
	} else if (sel == 1) {
		quest.setCustomData("0");
		quest.setStatus(0);
		cm.sendNext("알겠어~ 도울마음생기면 다시와!\r\n"); // 퀘스트 포기할 시 메세지
		cm.dispose();
	}
    } else if (status == 2) {
	if (qstatus == 0) {
		cm.sendYesNo("내 제안을 받아주겠어?"); // 퀘스트 수락하겠냐고 물어볼 메세지
	} else if (qstatus == 1) {
			var ms = ""
			ms += "고마워! 나도 드디어 날개가 생기는건가?!\r\n\r\n" // 퀘스트 완료할 시 메세지, 두번째
			ms += icon_getitem + "\r\n"
			ms += "#i" + gain + "# #b#z" + gain + "# 1개#k\r\n"
			quest.setStatus(2);
			quest.setCustomData("2");
			for (i = 0; i < item.length; i++) {
				cm.gainItem(item[i], -cost[i]);
			}
			cm.gainItem(gain, 800);
			cm.sendNext(ms);
			cm.dispose();
	}
    } else if (status == 3) {
	if (qstatus == 0) {
		quest.setStatus(1);
		quest.setCustomData("1");
		cm.sendNext("귀여운 페어리..살살 죽여줘!"); // 퀘스트 수락한다고 했을 때 감사 메세지
		cm.dispose();
	}
    }
}
