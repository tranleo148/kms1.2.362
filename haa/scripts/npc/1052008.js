var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendNextS("#fn나눔고딕 Extrabold##d(보물상자 안에 무언가가 있는 것 같다.)#k\r\n혹시 이게 슈미가 잃어버린 #b동전#k 일까?",2);
    } else if (status == 1) {
        cm.sendOk("#fn나눔고딕 Extrabold#보물상자 에서 아이템을 얻었습니다.\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n#i4031039# #b#z4031039##k");
    } else if (status == 2) {
        cm.warp(100030301,0);
        cm.gainItem(4031039, 1);
        cm.dispose();
    }
}
