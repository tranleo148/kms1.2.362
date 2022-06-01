importPackage(Packages.constants);
importPackage(Packages.tools.packet);

var status = -1;
var atype = 0;

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.getPlayer().getClient().send(CField.onUIEventInfo(qm.getPlayer().getClient(), true));
        qm.dispose();
    }
}