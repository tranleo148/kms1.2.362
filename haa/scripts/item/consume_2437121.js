
var status = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;
    if (status == 0) {
        cm.getMaxSkillList();
    } else if (status == 1) {
        if (selection >= 0) {
            cm.ChangeMaxSkillLevel(selection);
                cm.gainItem(2437121, -1);
            cm.dispose();
        }
    }
}