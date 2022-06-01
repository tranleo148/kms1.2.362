importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
var status = -1;
var check = false;

function start(mode, type, selection) {
    talk = ""
    for (i = 1; i <= 10; i++) {
        if (i != 1)
            talk += "\r\n";
        talk += "#e- " + i + " ~ " + (i * 4) + "마리 : 디에즈 코인 " + (i * 10) + "개#n"
    }
    dialogue = [
        ["next", 0, "흠. 반갑다. #b" + qm.getPlayer().getName() + "#k."],
        ["nextprev", 0, "보아하니 #b#e<어드벤처 드릴>#k#n에는 처음인 것 같군."],
        ["nextprev", 0, "간단히 설명해 줄테니 잘 듣도록."],
        ["nextprev", 0, "#b#e<어드벤처 드릴>#k#n은 1분 안에 최대한 많은 #e허수아비 몬스터#n를 처치하는 미션이다."],
        ["nextprev", 0, "총 10개의 발판마다 각각 종류가 다른 허수아비 몬스터 4종이 배치되어 있지."],
        ["nextprev", 0, "#b#e<일반 허수아비>#k#n\r\n\r\n#fMob/9300799.img/stand/0#\r\n첫 번째로 #e일반 허수아비.#n 별 다른 특징은 없다."],
        ["nextprev", 0, "#b#e<웡키 허수아비>#k#n\r\n\r\n#fMob/9305681.img/stand/0#\r\n두 번째는 #e웡키 허수아비#n다.\r\n일반 허수아비와 다르게 #e방어율#n을 갖추고 있지."],
        ["nextprev", 0, "#b#e<껨디 허수아비>#k#n\r\n\r\n#fMob/9305678.img/stand/0#\r\n세 번째로 #e껨디 허수아비#n다.\r\n일반 허수아비와 다르게 #e높은 체력과 보스 몬스터 속성#n을 지녔다고 알려져 있다."],
        ["nextprev", 0, "#b#e<소공 허수아비>#k#n\r\n\r\n#fMob/9305679.img/stand/0#\r\n마지막으로 #e소공 허수아비.#n\r\n위쪽 발판으로 갈수록 점점 #e레벨#n이 올라가지."],
        ["nextprev", 0, "당연히 각각의 허수아비들은 올라갈수록 더 강한 체력과 능력을 갖추고 있다."],
        ["nextprev", 0, "1분 동안 얼마나 많은 허수아비를 처치했느냐에 따라서 다음과 같은 #b#e디에즈 코인#k#n을 지급해 주도록 하지."],
        ["nextprev", 0, talk],
        ["nextprev", 0, "#b#e<어드벤처 드릴>#k#n은 #r#e캐릭터 당 하루 한 번#k#n만 받아갈 수 있다는 점 명심하도록."],
        ["nextprev", 0, "참고로 처치 결과가 마음에 들지 않는다면 #e보상 획득을 포기#n하고 언제든 #e재도전#n이 가능하다."],
        ["nextprev", 0, "오직 강인한 전사만이 새로운 모험을 즐길 자격이 있는 법!"],
        ["nextprev", 0, "가라! #b" + qm.getPlayer().getName() + "#k!\r\n#b#e<어드벤처 드릴>#k#n을 정복하고 돌아와라!"]
    ]
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (qm.getQuestRecord(100188).getCustomData() != "complete") {
        if (status < dialogue.length) {
            sendByType(dialogue[status][0], dialogue[status][1], dialogue[status][2]);
        } else {
            qm.getQuestRecord(100188).setCustomData("complete");
            qm.dispose();
        }
    } else {
        qm.dispose();
        qm.openNpcCustom(qm.getClient(), 9062147, "adventure_drill");
    }
}

function sendByType(type, type2, text) {
    switch (type) {
        case "next":
            qm.sendNextS(text, type2, 9062147, 0);
            break;
        case "nextprev":
            qm.sendNextPrevS(text, type2, 9062147, 0);
            break;
    }
}