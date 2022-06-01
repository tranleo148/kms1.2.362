importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

box = 2437750;

function start() {
    status = -1;
    action(1, 0, 0);
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
        var text = "#b#e<#t" + box + "#>#k#n\r\n\r\n";
        text += "교환하고싶은 #b아케인 심볼#k 을 선택해주세요. 동일한 아이템으로 1개가 지급됩니다.\r\n";
        for (i = 0; i < 6; i++) {
            text += "#L" + (1712001 + i) + "# #i" + (1712001 + i) + "# #b#z" + (1712001 + i) + "##k\r\n";
        }
        cm.sendYesNo(text);

    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(box, -1);
        cm.sendOk("교환이 완료되었습니다.");
        cm.dispose();
    }
}