//와호

var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
if (selection == 0){  
	if (!checkPos(cm)) {
	cm.getPlayer().dropMessage(5,"X값");
	cm.dispose();
	} else {
cm.getPlayer().dropMessage(5,"O값");
            cm.dispose();
}
}
}
}

function checkPos(cm) {
    var ltx = -952;
    var lty = -150;
    var rbx = -308;
    var rby = 334;
    var curx = cm.getPlayer().getPosition().getX();
    var cury = cm.getPlayer().getPosition().getY();
    return curx >= ltx && cury >= lty && curx <= rbx && cury <= rby;
}