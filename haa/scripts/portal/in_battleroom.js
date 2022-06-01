function enter(pi) {
    if (pi.getQuestRecord(100188).getCustomData() != "complete") {
        pi.getPlayer().dropMessage(5, "어드벤처 폴로 엔피시를 통해 입장 퀘스트를 완료해 주세요.");
    } else {
        pi.openNpc(9062147, "adventure_drill");
    }
}