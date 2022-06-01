function enter(pi) {
    var gomap = [100,200,300,400];
    var quest = [1041,1042,1043,1044];
    for (i = 0; i < 4; i++) {
	if (pi.getQuestStatus(quest[i]) == 1) {
	    pi.warp(1010000 + i,0);
	    return;
	}
    }
}