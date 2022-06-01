function enter(pi) { 
    pi.EnableUI(0);
    pi.DisableUI(false);
    if (pi.getQuestStatus(2568) == 2 && pi.getQuestStatus(2570) == 0) {
	pi.showInstruction("Where am I? My head hurts...",150,5);
    }
}  