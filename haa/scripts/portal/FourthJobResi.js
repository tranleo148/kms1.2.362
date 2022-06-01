function enter(pi) {
    var FirstQuest = [23043,23044,23045];
    var SecondQuest = [23046,23047,23048];
    var EnterMap = [931000300,931000301,931000302];
    for (i = 0; i < 3; i++) {
	if (pi.getQuestStatus(FirstQuest[i]) == 1) {
	    if (pi.getQuestStatus(SecondQuest[i]) == 2 || pi.haveItem(4032743,1)) {
		pi.warp(EnterMap[i],0);
		return;
	    } else {
		pi.playerMessage("대화가 없습니다.");
	    }
	} else {
	    pi.warp(310060220,0);
	}
    }
}