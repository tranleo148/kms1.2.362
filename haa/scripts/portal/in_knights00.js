/*
 * 
 */
importPackage(java.lang);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);

function enter(pi) {
if (pi.getQuestStatus(31124) < 2) {

if (pi.getQuestStatus(31124) == 1) {
    pi.getPlayer().send(UIPacket.showInfo("길이 막혀있다. 알렉스한테 가서 보고하자."));
    pi.forceCompleteQuest(31124);
} else {
    pi.getPlayer().send(UIPacket.showInfo("시그너스 요새 정찰 퀘스트를 클리어 해주십시오."));
}
} else if (pi.getQuestStatus(31124) == 2) {
    pi.playPortalSE();
    pi.warp(271030010);
}
    return true;
}