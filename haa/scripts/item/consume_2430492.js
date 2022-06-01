importPackage(Packages.client.items);
var status = -1;
var jessicaj = new Array(15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
var jessicarebirth = new Array(20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    return;
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
var chat = "진정한 강함을 원하는가?\r\n";
chat += "그렇다면 #b각성 포인트#k로 진정한 강함을 찾아보는건 어때?\r\n\r\n";
chat += "#e#r현재 보유중인 각성 포인트 : #k" + cm.getPlayer().getGP() + "\r\n";
chat += "#r#e현재 누적된 스피릿 포인트 : #k" + cm.getPlayer().getSpiritp() + "\r\n";
chat += "\r\n#e<각성 안내>#n#b\r\n";
chat += "\r\n	각성포인트는 여신의 눈물에서 획득 가능 합니다.\r\n";
chat += "	각성을 하기위해선 [500 각성 포인트]가 필요합니다.\r\n";
chat += "	각성을 했을시에는 AP와 스피릿포인트를 획득 합니다.\r\n";

cm.sendYesNo(chat);
} else if (status == 1) {
if(cm.getPlayer().getGP() >= 500) {
var jessica = jessicaj[Math.floor(Math.random() * jessicaj.length)];
var jessica2 = jessicarebirth[Math.floor(Math.random() * jessicarebirth.length)];
var Jessica = cm.getPlayer().getSpiritp() + jessica2;
cm.gainAp(jessica);
cm.getPlayer().gainGP(-500);
cm.getPlayer().addSpiritPoint(jessica2);
cm.sendOk("#bAbillit Points + " + jessica + "\r\n스피릿 포인트 + " + jessica2 + "\r\n스피릿 포인트 합계 : " + cm.getPlayer().getSpiritp() + "\r\n\r\n#r각성#k 한것을 축하한다.");
cm.dispose();
} else {
cm.sendOk("각성 포인트는 확실히 모여있는건가?");
cm.dispose();
}
} else {
cm.dispose();
}
}
}