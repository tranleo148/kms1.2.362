importPackage(java.lang);
importPackage(java.io);

var St = -1;

 
function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	talk = ""
    dialogue = [
		["nextprev", 0, ""],
        ["nextprev", 0, "이곳은 보름달이 뜨면 월묘가 만든 맛있는 떡을 맛볼 수 있는 아주 축복받은 곳 이라 할 수 있지. 곳곳에서 자라고 있는 달맞이꽃 풀에서 씨앗을 얻어 초승달 주위의 작은 발판 위에 심으면 예븐 달맞이꽃이 필거야."],
        ["nextprev", 0, "초승달 주위 #r6개의 발판 위에 달맞이 꽃이 모두 피면#k 보름달이 뜨고 월묘가 나타나 떡방아를 찧는다네. 그런데 이곳에 나타난 돼지들이 달맞이꽃 풀을 다 먹어치우고 있어. 일단 돼지들을 처치하고 달맞이꽃 씨앗을 되찾아야 해."],
        ["nextprev", 0, "그런 후 월묘가 떡을 무사히 만들 수 있도록 #b몬스터들의 공격을 막아야 하는 게 자네들의 임무#k일세."],
        ["nextprev", 0, "자네가 파티원들과 힘을 합쳐서 떡을 80개 구해다 주게나.\r\n시간 내에 꼭 내게 떡을 가져다주길 바라네."]
    ]

	if(M == -1) {
		cm.dispose();
		return;
	}
	if (M == 0) {
        St--;
		cm.dispose();
		return;
    }
	if(M == 1){
	    St++;
	}
	if(St == 0) {
		var talk = "";
		if(cm.getPlayer().getMap().getPartyCount() == 3){
			talk = "냠냠 정말 맛있군 종종 나를 찾아와 #b월묘의 떡#k을 구해주게.";
                                    talk +="#L0#돼지의 마을에서 보상을 챙기겠습니다.";
		} else {
			talk +="어흥~ 나는 이곳을 지키는 어흥이님이라네. 무슨 일로 왔는가?\r\n";
			talk +="#b#L1#이곳에 대해 알려주세요.\r\n";
			talk +="#L2#이곳에서 나가고 싶습니다.#k";
		}
		cm.sendSimple(talk);
    } else {
		if(St == 1){
			stl = S;
		}
		if (stl == 1) {
            if (St <= dialogue.length) {
                sendByType(dialogue[St - 1][0], dialogue[St - 1][1], dialogue[St][2]);
            } else {
                cm.dispose();
            }
        } else {
			if(St == 1) {
				if(S == 0 || S == 2){
					if(cm.getPlayer().getMap().getPartyCount() == 3){
                                                                        cm.dispose();
						cm.openNpc(9090002);
					} else {
						cm.warp(933000000);
                                                                        cm.dispose();
					}	
				}
			} else if(St == 2) {
					cm.warp(933000000);
					cm.dispose();
			}
		}
	}
}

function sendByType(type, type2, text) {
    switch (type) {
        case "next":
            cm.sendNextS(text, type2);
            break;
        case "nextprev":
            cm.sendNextPrevS(text, type2);
            break;
    }
}