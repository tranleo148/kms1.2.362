function enter(pi) {
    var quest = [3719,3724,3730,3736,3742,3748];
    for (i = 0; i < 6; i++) {
	if (pi.getQuestStatus(quest[i]) == 1) {
	    pi.forceCompleteQuest(quest[i]);
	}
    }
}