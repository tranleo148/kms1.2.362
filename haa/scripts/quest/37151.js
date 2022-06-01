var status = -1;

function start(mode, type, selection) {
	dialogue = [
		["next", 4, "#fs10#저... 저기요..."],
		["nextprev", 2, "응? 누군가 부르는 소리가..."],
		["nextprev", 4, "저... 제 목소리가 들리시나요..."],
		["nextprev", 4, "전 엘리니아에서 연구 중인 베티 박사의 딸 앤이라고 해요."],
		["nextprev", 4, "꼭 부탁드리고 싶은 게 있는데 말이죠... 들어주실 수 있나요?"],
		["yesno", 4, "그렇다면... 제게 와주세요... 부탁이에요...\r\n#r(수락 시 엘리니아의 앤 앞으로 자동 이동합니다.)"],
		["next", 4, "전 엘리니아에 있어요. 기다릴게요."]
	]
	if (mode == -1) {
		qm.dispose();
		return;
	}
	if (mode == 0) {
		if (type == 3) {
			qm.sendOkS("정말 큰 용기 내서 물어봤는데...", 4, 1012110);
			qm.dispose();
			return;
		} else {
			status--;
		}
	}
	if (mode == 1) {
		status++;
	}
	if (status < dialogue.length) {
		sendByType(dialogue[status][0], dialogue[status][1], dialogue[status][2]);
	} else {
		qm.forceStartQuest(37151);
		qm.warp(101000000, 2);
		qm.dispose();
	}
}

function sendByType(type, type2, text) {
	switch (type) {
		case "next":
			qm.sendNextS(text, type2, 1012110, 0);
			break;
		case "nextprev":
			qm.sendNextPrevS(text, type2, 1012110, 0);
			break;
		case "yesno":
			qm.sendYesNoS(text, type2, 1012110);
			break;
		default:
			qm.sendOk(text, 1012110);
			break;
	}
}