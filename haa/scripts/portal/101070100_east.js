function enter(pi) {
    if (pi.getQuestStatus(32104) == 2) { 
        pi.warp(101070010, 2);
    } else {
        pi.warp(101070000, 1);
    }
    return true;
}