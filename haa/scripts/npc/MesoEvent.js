importPackage(Packages.client.items);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
             cm.getPlayer().startEventTimer(30);
        }
    }
}