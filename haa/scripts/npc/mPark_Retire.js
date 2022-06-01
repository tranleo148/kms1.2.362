var status = 0;

 

function start() {
    status = -1;
    action(1, 0, 0);
}

 

function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
        status--;
    if (status == 0) {
        cm.sendYesNo("왜? 벌써 나가려고? 아직 재미있는 일이 많이 남았는데?");
    } else if (status == 1) {
        cm.sendNext("근성이 부족하군. 뭐 가겠다면 말리진 않겠어. 잘가게.");
    } else if (status == 2) {
        cm.warp(951000000, 0);
        cm.dispose();
    }
}